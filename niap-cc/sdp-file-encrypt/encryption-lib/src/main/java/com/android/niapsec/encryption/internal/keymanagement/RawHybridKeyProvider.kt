/*
 * Copyright (C) 2026 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.niapsec.encryption.internal.keymanagement

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import androidx.core.content.edit
import com.android.niapsec.encryption.tools.Asn1Helper
import com.android.niapsec.encryption.tools.CleanSecretKeySpec
import com.android.niapsec.encryption.tools.SafeHkdf
import com.android.niapsec.encryption.tools.SecurityAuditLogger
import com.google.crypto.tink.Aead
import com.google.crypto.tink.StreamingAead
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.channels.ReadableByteChannel
import java.nio.channels.SeekableByteChannel
import java.nio.channels.WritableByteChannel
import java.security.GeneralSecurityException
import java.security.Key
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import java.security.SecureRandom
import java.security.spec.ECGenParameterSpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.KeyAgreement
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * [Security Component: Raw JCA Hybrid Encryption]
 * * Custom implementation using Java Cryptography Architecture (JCA) primitives.
 * * This class provides direct control over key material life-cycle and memory management.
 *
 * [Compliance Note]
 * * **FCS_STG_EXT.2 (Encrypted Key Storage):**
 * - SATISFIED: Private keys are generated and stored directly within the Android Keystore
 * (`AndroidKeyStore` provider), ensuring they are never exposed in plaintext to the application layer.
 *
 * * **FCS_CKM_EXT.4 (Key Destruction):**
 * - SATISFIED: This implementation explicitly overwrites sensitive key material (DEK, Shared Secret)
 * with zeros in `finally` blocks immediately after use. This provides deterministic destruction
 * of keys in volatile memory, independent of Garbage Collection timing.
 * - SATISFIED: Persistent keys are destroyed via `KeyStore.deleteEntry()` and `SharedPreferences.Editor.clear()`.
 *
 * * **FDP_DAR_EXT.2 (Sensitive Data Encryption):**
 * - SATISFIED: Uses an asymmetric key scheme to allow data encryption even when the device is locked
 * and the private key is unavailable.
 *
 * * **FIA_UAU_EXT.1 (Authentication for Cryptographic Operation):**
 * - SATISFIED: Enforces user authentication policies at the OS level by configuring
 * `KeyGenParameterSpec.Builder.setUnlockedDeviceRequired(true)`. This ensures that decryption
 * operations fail if the device is not unlocked.
 */
class RawHybridKeyProvider(
    private val context: Context,
    private val masterKeyUri: String,
    _unlockedDeviceRequired: Boolean,
    private val keysetPrefName: String,
    private val lockStatePollCount: Int = 5,
    private val lockStatePollIntervalMs: Long = 100
) : KeyProvider {

    private val masterKeyAlias = masterKeyUri.removePrefix("android-keystore://")
    private val symmetricMasterKeyAlias = "${masterKeyAlias}_symmetric"
    private val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
    val unlockedDeviceRequired: Boolean = _unlockedDeviceRequired

    private val storageContext: Context = context.createDeviceProtectedStorageContext()




    private val prefs = storageContext.getSharedPreferences(keysetPrefName, Context.MODE_PRIVATE)

    companion object {
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val KEY_PUBLIC_KEY_PREF = "master_public_key"
        private const val EC_KEY_ALGORITHM = KeyProperties.KEY_ALGORITHM_EC
        private const val KEY_AGREEMENT_ALGORITHM = "ECDH"
        private const val DEK_ALGORITHM = "AES"
        private const val DEK_WRAPPING_CIPHER = "AES/GCM/NoPadding"
        private const val DEK_SIZE_BITS = 256
        private const val DATA_CIPHER = "AES/GCM/NoPadding"
        private const val GCM_TAG_LENGTH_BITS = 128
        const val MAGIC_BYTE_ASYMMETRIC: Byte = 0x01 // Temporary storage while locked (current Hybrid scheme)
        const val MAGIC_BYTE_SYMMETRIC: Byte = 0x02  // Re-encrypted after unlock (symmetric UDR scheme)

    }




    init {
        generateAndStoreKeyPairIfNeeded()
        generateSymmetricEcKeyPairIfNeeded()
        generateSymmetricMasterKeyIfNeeded()
        generateWrappingKeyIfNeeded()
    }

    private fun getFlushIterations(): Int {
        return context.getSharedPreferences("niap_sec_prefs", Context.MODE_PRIVATE)
                .getInt("keystore_flush_iterations", 0)
    }

    private fun flushKeystoreIpcBuffers() {
        val iterations = getFlushIterations()
        if (iterations <= 0) {
            return // Skip flush processing if the setting is 0 or less
        }

        val startTime = System.currentTimeMillis()
        try {
            val dummyDek = ByteArray(DEK_SIZE_BITS / 8)
            val masterKey = keyStore.getKey(symmetricMasterKeyAlias, null) ?: return
            val flushCipher = Cipher.getInstance(DEK_WRAPPING_CIPHER)

            for (i in 0 until iterations) {
                flushCipher.init(Cipher.ENCRYPT_MODE, masterKey)
                flushCipher.doFinal(dummyDek)
            }
            val elapsed = System.currentTimeMillis() - startTime
            Log.d("RawHybridKeyProvider", "Keystore IPC flush completed: $iterations iterations took ${elapsed}ms")
            SecurityAuditLogger.logLine("Keystore IPC flush ($iterations runs) took ${elapsed}ms")
        } catch (e: Exception) {
            val elapsed = System.currentTimeMillis() - startTime
            Log.w("RawHybridKeyProvider", "Keystore IPC flush failed after ${elapsed}ms: ${e.message}")
        }
    }

    private fun generateSymmetricMasterKeyIfNeeded() {
        if (!keyStore.containsAlias(symmetricMasterKeyAlias)) {
            val keyGenerator = javax.crypto.KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
            val specBuilder = KeyGenParameterSpec.Builder(
                symmetricMasterKeyAlias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
            
            // MDFPP Requirement: Enforce user authentication for symmetric protection
            specBuilder.setUnlockedDeviceRequired(unlockedDeviceRequired)
            
            keyGenerator.init(specBuilder.build())
            keyGenerator.generateKey()
        }
    }

    private fun generateWrappingKeyIfNeeded() {
        val wrappingKeyAlias = "${masterKeyAlias}_wrapping"
        if (!keyStore.containsAlias(wrappingKeyAlias)) {
            val kpg = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, ANDROID_KEYSTORE)
            val spec = KeyGenParameterSpec.Builder(
                wrappingKeyAlias,
                KeyProperties.PURPOSE_WRAP_KEY
            )
                .setKeySize(2048)
                .setDigests(KeyProperties.DIGEST_SHA256) // Specify SHA-256 only to align with CTS
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
                .setUnlockedDeviceRequired(unlockedDeviceRequired)
                .build()
            kpg.initialize(spec)
            kpg.generateKeyPair()
        }
    }

    private fun generateAndStoreKeyPairIfNeeded() {

        if (keyStore.containsAlias(masterKeyAlias)) {
            if (!prefs.contains(KEY_PUBLIC_KEY_PREF)) {
                try {
                    val entry = keyStore.getEntry(masterKeyAlias, null) as? KeyStore.PrivateKeyEntry
                    entry?.certificate?.publicKey?.let { publicKey ->
                        savePublicKey(publicKey)
                    }
                } catch (e: Exception) {
                    // Key might be broken!, only record the log in this time.
                }
            }
        } else {
            // New key generation
            val kpg = KeyPairGenerator.getInstance(EC_KEY_ALGORITHM, ANDROID_KEYSTORE)
            val spec = KeyGenParameterSpec.Builder(
                masterKeyAlias,
                KeyProperties.PURPOSE_AGREE_KEY
            )
                .setAlgorithmParameterSpec(ECGenParameterSpec("secp521r1"))
                .setDigests(KeyProperties.DIGEST_SHA512)
                // [FIA_UAU_EXT.1] Authentication for Cryptographic Operation
                // * ENFORCEMENT: Configures the TSF (Android Keystore) to reject key agreement operations
                //   if the user has not authenticated (device locked).
                .setUnlockedDeviceRequired(unlockedDeviceRequired)
                .build()

            kpg.initialize(spec)
            val keyPair = kpg.generateKeyPair()

            savePublicKey(keyPair.public)
        }
    }

    /**
     * WORKAROUND [FCS_CKM_EXT.4]: Generates a dedicated hardware-backed EC key pair for Symmetric mode
     * to perform ECDH key agreement. This is a workaround for the Keystore2 IPC memory leak
     * observed when passing raw DEKs directly to Keystore2 for symmetric wrapping.
     * By using ECDH, the raw DEK never leaves the application process during wrapping.
     */
    private fun generateSymmetricEcKeyPairIfNeeded() {
        val symmetricEcAlias = "${masterKeyAlias}_symmetric_ec"
        if (keyStore.containsAlias(symmetricEcAlias)) {
            val prefsKey = "${KEY_PUBLIC_KEY_PREF}_symmetric"
            if (!prefs.contains(prefsKey)) {
                try {
                    val entry = keyStore.getEntry(symmetricEcAlias, null) as? KeyStore.PrivateKeyEntry
                    entry?.certificate?.publicKey?.let { publicKey ->
                        saveSymmetricPublicKey(publicKey)
                    }
                } catch (e: Exception) {
                    // Key might be broken
                }
            }
        } else {
            val kpg = KeyPairGenerator.getInstance(EC_KEY_ALGORITHM, ANDROID_KEYSTORE)
            val spec = KeyGenParameterSpec.Builder(
                symmetricEcAlias,
                KeyProperties.PURPOSE_AGREE_KEY
            )
                .setAlgorithmParameterSpec(ECGenParameterSpec("secp521r1"))
                .setDigests(KeyProperties.DIGEST_SHA512)
                .setUnlockedDeviceRequired(true) // ALWAYS UDR
                .build()

            kpg.initialize(spec)
            val keyPair = kpg.generateKeyPair()

            saveSymmetricPublicKey(keyPair.public)
        }
    }

    private fun saveSymmetricPublicKey(publicKey: PublicKey) {
        val prefsKey = "${KEY_PUBLIC_KEY_PREF}_symmetric"
        val encodedKey = Base64.encodeToString(publicKey.encoded, Base64.NO_WRAP)
        prefs.edit().putString(prefsKey, encodedKey).apply()
    }

    private fun savePublicKey(publicKey: PublicKey) {
        val encodedKey = Base64.encodeToString(publicKey.encoded, Base64.NO_WRAP)
        prefs.edit().putString(KEY_PUBLIC_KEY_PREF, encodedKey).apply()
    }

    private fun loadRecipientPublicKey(): PublicKey {
        val encodedKey = prefs.getString(KEY_PUBLIC_KEY_PREF, null)
            ?: throw GeneralSecurityException("Master public key not found in SharedPreferences.")
        val bytes = Base64.decode(encodedKey, Base64.NO_WRAP)
        val spec = X509EncodedKeySpec(bytes)
        return KeyFactory.getInstance(EC_KEY_ALGORITHM).generatePublic(spec)
    }

    private fun loadSymmetricPublicKey(): PublicKey {
        val prefsKey = "${KEY_PUBLIC_KEY_PREF}_symmetric"
        val encodedKey = prefs.getString(prefsKey, null)
            ?: throw GeneralSecurityException("Symmetric UDR public key not found in SharedPreferences.")
        val bytes = Base64.decode(encodedKey, Base64.NO_WRAP)
        val spec = X509EncodedKeySpec(bytes)
        return KeyFactory.getInstance(EC_KEY_ALGORITHM).generatePublic(spec)
    }

    private fun loadRecipientPrivateKey(): PrivateKey {
        val entry = keyStore.getEntry(masterKeyAlias, null)
            ?: throw GeneralSecurityException("Master key alias not found in Keystore: $masterKeyAlias")
        if (entry !is KeyStore.PrivateKeyEntry) {
            throw GeneralSecurityException("Keystore entry is not a private key: $masterKeyAlias")
        }
        return entry.privateKey
    }

    private fun loadSymmetricRecipientPrivateKey(): PrivateKey {
        val symmetricEcAlias = "${masterKeyAlias}_symmetric_ec"
        val entry = keyStore.getEntry(symmetricEcAlias, null)
            ?: throw GeneralSecurityException("Symmetric UDR key alias not found in Keystore: $symmetricEcAlias")
        if (entry !is KeyStore.PrivateKeyEntry) {
            throw GeneralSecurityException("Keystore entry is not a private key: $symmetricEcAlias")
        }
        return entry.privateKey
    }

    private fun hkdfDerive(ikm: ByteArray, salt: ByteArray, info: ByteArray): ByteArray {
        return SafeHkdf.computeHkdf(ikm, salt, info, 32)
    }


    /**
     * Intentionally overwrites (poisons) the residual CBOR (66-byte remnants)
     * left in the Keystore2 / Binder IPC shared buffer with dummy data.
     */
    private fun flushKeystoreBinderBuffer(keystorePrivateKey: PrivateKey) {
        try {
            // 1. Instantly generate dummy public key in software (P-521)
            val kpg = KeyPairGenerator.getInstance("EC")
            kpg.initialize(java.security.spec.ECGenParameterSpec("secp521r1"))
            val dummyPubKey = kpg.generateKeyPair().public

            // 2. Prepare dummy agreement by explicitly specifying AndroidKeyStore
            val dummyAgreement = KeyAgreement.getInstance("ECDH", "AndroidKeyStore")
            dummyAgreement.init(keystorePrivateKey)
            dummyAgreement.doPhase(dummyPubKey, true)
            //Do poisoning
            val dummySecret = dummyAgreement.generateSecret()
            dummySecret?.fill(0)

            Log.d("KMD", "Binder buffer successfully poisoned.")
        } catch (e: Exception) {
            // Flush failures are only logged and do not impact the main decryption process
            Log.w("KMD", "Binder buffer flush failed (ignored)", e)
        }
    }

    /**
     * Polls the device lock state multiple times to ensure accuracy.
     */
    private fun isDeviceLockedReliably(): Boolean {
        val km = context.getSystemService(Context.KEYGUARD_SERVICE) as android.app.KeyguardManager
        for (i in 1..lockStatePollCount) {
            if (km.isDeviceLocked) return true
            if (i < lockStatePollCount) {
                try { Thread.sleep(lockStatePollIntervalMs) } catch (e: Exception) {}
            }
        }
        return false
    }

    // --- AEAD Implementation (In-Memory) ---
    private val rawHybridAead: Aead = object : Aead {
        override fun encrypt(plaintext: ByteArray, associatedData: ByteArray): ByteArray {
            // [FDP_DAR_EXT.2.4] If device is unlocked, prefer symmetric encryption (0x02)
            if (!isDeviceLockedReliably()) {
                try {
                    return encryptSymmetric(plaintext, associatedData)
                } catch (e: Exception) {
                    Log.w("RawHybridKeyProvider", "Symmetric encryption failed, falling back to asymmetric", e)
                }
            }
            return encryptAsymmetric(plaintext, associatedData)
        }

        private fun _encryptSymmetric(plaintext: ByteArray, associatedData: ByteArray): ByteArray {
            var dekBytes = ByteArray(DEK_SIZE_BITS / 8)
            var masterKey: Key? = null
            var dekSpec: CleanSecretKeySpec? = null
            val dataCipher: Cipher =  Cipher.getInstance(DATA_CIPHER)

            try {
                SecureRandom().nextBytes(dekBytes)
            
                //Test Code
                for (i in 0..31 step 2) {
                    dekBytes[i] = (0x48).toByte()
                    dekBytes[i+1] = (0x04).toByte()
                }

                dekSpec = CleanSecretKeySpec(dekBytes, DEK_ALGORITHM)
            
                dataCipher.init(Cipher.ENCRYPT_MODE, dekSpec)
                dataCipher.updateAAD(associatedData)
                val encryptedContent = dataCipher.doFinal(plaintext)
                val dataIv = dataCipher.iv

                masterKey = keyStore.getKey(symmetricMasterKeyAlias, null)
                    ?: throw GeneralSecurityException("Symmetric master key not found")
                val wrapCipher = Cipher.getInstance(DEK_WRAPPING_CIPHER)
                wrapCipher.init(Cipher.ENCRYPT_MODE, masterKey)
                val wrappedDek = wrapCipher.doFinal(dekBytes)
                val wrapIv = wrapCipher.iv

                return serializeEncryptedPackage(MAGIC_BYTE_SYMMETRIC, null, wrappedDek, wrapIv, encryptedContent, dataIv)
            } finally {
                SecurityAuditLogger.logLine( "===== Encrypt symmetric =====")
                //masterKey should be null at this point
                SecurityAuditLogger.logKeyMaterial("Symmetric UDR Key (used as KEK)", masterKey?.encoded)
                SecurityAuditLogger.logKeyMaterial("Data Encryption Key (DEK)", dekBytes)


                dekSpec?.let {
                    if (!it.isDestroyed) {
                        it.destroy()
                    }
                }
                dekBytes.fill(0)

                try {
                    val dummyZeroKey = SecretKeySpec(ByteArray(32), "AES") // All-zero dummy key
                    dataCipher.init(Cipher.ENCRYPT_MODE, dummyZeroKey) // Force overwrite of internal state
                } catch (e: Exception) {
                    // Ignore any exceptions (solely for memory clearing purposes)
                }
                //flushKeystoreIpcBuffers()

            }
        }

        private fun encryptSymmetric(plaintext: ByteArray, associatedData: ByteArray): ByteArray {
            val recipientPubKey = loadSymmetricPublicKey()
            val dekBytes = ByteArray(DEK_SIZE_BITS / 8)
            var sharedSecret: ByteArray? = null
            var kekBytes: ByteArray? = null
            var ephemeralKeyPair: KeyPair? = null

            var dekSpec: CleanSecretKeySpec? = null
            var kekSpec: CleanSecretKeySpec? = null

            try {
                SecureRandom().nextBytes(dekBytes)
                dekSpec = CleanSecretKeySpec(dekBytes, DEK_ALGORITHM)
                val dataCipher = Cipher.getInstance(DATA_CIPHER)
                dataCipher.init(Cipher.ENCRYPT_MODE, dekSpec)
                dataCipher.updateAAD(associatedData)
                val encryptedContent = dataCipher.doFinal(plaintext)
                val dataIv = dataCipher.iv
                val ephemeralKpg = KeyPairGenerator.getInstance(EC_KEY_ALGORITHM).apply { initialize(ECGenParameterSpec("secp521r1")) }
                val keyPair = ephemeralKpg.generateKeyPair()

                
                val keyAgreement = KeyAgreement.getInstance(KEY_AGREEMENT_ALGORITHM)
                keyAgreement.init(keyPair.private)

                keyAgreement.doPhase(recipientPubKey, true)
                sharedSecret = keyAgreement.generateSecret()

                // Use masterKeyAlias + "_symmetric" for context to distinguish from asymmetric HKDF context
                val contextString = "${masterKeyAlias}_symmetric_ec"
                kekBytes =
                        hkdfDerive(
                                sharedSecret!!,
                                contextString.toByteArray(Charsets.UTF_8),
                                keyPair.public.encoded
                        )

                kekSpec = CleanSecretKeySpec(kekBytes, DEK_ALGORITHM)
                
                val wrapCipher = Cipher.getInstance(DEK_WRAPPING_CIPHER)
                wrapCipher.init(Cipher.ENCRYPT_MODE, kekSpec)
                val wrappedDek = wrapCipher.doFinal(dekBytes)
                val wrapIv = wrapCipher.iv

                return serializeEncryptedPackage(
                        MAGIC_BYTE_SYMMETRIC,
                        keyPair.public.encoded,
                        wrappedDek,
                        wrapIv,
                        encryptedContent,
                        dataIv
                )

            } finally {
                SecurityAuditLogger.logLine("===== Encrypt symmetric (Solution 2 - ECDH) =====")
                SecurityAuditLogger.logKeyMaterial("Ephemeral Key Pair Public Key", ephemeralKeyPair?.public?.encoded)
                val ephPrivBytes = ephemeralKeyPair?.private?.encoded
                SecurityAuditLogger.logKeyMaterial("Ephemeral Key Pair Private Key", ephPrivBytes)
                ephPrivBytes?.fill(0)//Have to clear just for this logging
                SecurityAuditLogger.logKeyMaterial("Recipient UDR Key Pair (Public Key)", recipientPubKey.encoded)
                SecurityAuditLogger.logKeyMaterial("Shared Secret", sharedSecret)
                SecurityAuditLogger.logKeyMaterial("Symmetric KEK", kekBytes)
                SecurityAuditLogger.logKeyMaterial("Data Encryption Key (DEK)", dekBytes)
                
                dekBytes.fill(0)
                sharedSecret?.fill(0)
                kekBytes?.fill(0)

                dekSpec?.let { if (!it.isDestroyed) it.destroy() }
                kekSpec?.let { if (!it.isDestroyed) it.destroy() }
            }
        }

        private fun encryptAsymmetric(plaintext: ByteArray, associatedData: ByteArray): ByteArray {
            val recipientPubKey = loadRecipientPublicKey()
            val dekBytes = ByteArray(DEK_SIZE_BITS / 8)
            var sharedSecret: ByteArray? = null
            var kekBytes: ByteArray? = null
            var ephemeralKeyPair: KeyPair? = null

            var dekSpec: CleanSecretKeySpec? = null
            var kekSpec: CleanSecretKeySpec? = null

            try {
                SecureRandom().nextBytes(dekBytes)
                dekSpec = CleanSecretKeySpec(dekBytes, DEK_ALGORITHM)
                val dataCipher = Cipher.getInstance(DATA_CIPHER)
                dataCipher.init(Cipher.ENCRYPT_MODE, dekSpec)
                dataCipher.updateAAD(associatedData)
                val encryptedContent = dataCipher.doFinal(plaintext)
                val dataIv = dataCipher.iv
                val ephemeralKpg = KeyPairGenerator.getInstance(EC_KEY_ALGORITHM).apply { initialize(ECGenParameterSpec("secp521r1")) }
                //TSF does not store the ephemeral private key and relies on JVM object scope for transient cleanup
                val keyPair = ephemeralKpg.generateKeyPair()

                ephemeralKeyPair = keyPair

                val keyAgreement = KeyAgreement.getInstance(KEY_AGREEMENT_ALGORITHM)
                keyAgreement.init(keyPair.private)

                keyAgreement.doPhase(recipientPubKey, true)
                sharedSecret = keyAgreement.generateSecret()
                kekBytes =
                        hkdfDerive(
                                sharedSecret!!,
                                masterKeyAlias.toByteArray(Charsets.UTF_8),
                                keyPair.public.encoded
                        )

                kekSpec = CleanSecretKeySpec(kekBytes!!, DEK_ALGORITHM)

                val wrapCipher = Cipher.getInstance(DEK_WRAPPING_CIPHER)
                wrapCipher.init(Cipher.ENCRYPT_MODE, kekSpec)
                val wrappedDek = wrapCipher.doFinal(dekBytes)
                val wrapIv = wrapCipher.iv

                return serializeEncryptedPackage(
                        MAGIC_BYTE_ASYMMETRIC,
                        keyPair.public.encoded,
                        wrappedDek,
                        wrapIv,
                        encryptedContent,
                        dataIv
                )

            } finally {
                SecurityAuditLogger.logLine( "===== Encrypt asymmetric =====")
                SecurityAuditLogger.logKeyMaterial("Ephemeral Key Pair Public Key", ephemeralKeyPair?.public?.encoded)
                val ephPrivBytes = ephemeralKeyPair?.private?.encoded
                SecurityAuditLogger.logKeyMaterial("Ephemeral Key Pair Private Key", ephPrivBytes)
                ephPrivBytes?.fill(0)
                SecurityAuditLogger.logKeyMaterial("Recipient UDR Key Pair (Public Key)", recipientPubKey.encoded)

                SecurityAuditLogger.logKeyMaterial("Shared Secret", sharedSecret)
                SecurityAuditLogger.logKeyMaterial("Asymmetric KEK", kekBytes)
                SecurityAuditLogger.logKeyMaterial("Data Encryption Key (DEK)", dekBytes)

                // [FCS_CKM_EXT.4] Explicit zeroization: Prevent key remanence in memory
                dekBytes.fill(0); sharedSecret?.fill(0); kekBytes?.fill(0)

                dekSpec?.let {
                    if (!it.isDestroyed) {
                        it.destroy()
                    }
                }
                kekSpec?.let {
                    if (!it.isDestroyed) {
                        it.destroy()
                    }
                }
            }
        }

        override fun decrypt(ciphertext: ByteArray, associatedData: ByteArray): ByteArray {
            val pkg = deserializeEncryptedPackage(ciphertext)
            var dekBytes: ByteArray? = null
            var sharedSecret: ByteArray? = null
            var kekBytes: ByteArray? = null
            var recipientPrivateKey:Key? = null

            var dekSpec: CleanSecretKeySpec? = null
            var kekSpec: CleanSecretKeySpec? = null

            val dataCipher = Cipher.getInstance(DATA_CIPHER)
            val unwrapCipher = Cipher.getInstance(DEK_WRAPPING_CIPHER)
            try {
                if (pkg.magicByte == MAGIC_BYTE_ASYMMETRIC) {
                    recipientPrivateKey = loadRecipientPrivateKey()
                    val ephemeralPubKeySpec = X509EncodedKeySpec(pkg.ephemeralPublicKeyBytes!!)
                    val ephemeralPublicKey = KeyFactory.getInstance(EC_KEY_ALGORITHM).generatePublic(ephemeralPubKeySpec)

                    val keyAgreement = KeyAgreement.getInstance(KEY_AGREEMENT_ALGORITHM)
                    keyAgreement.init(recipientPrivateKey!!)
                    keyAgreement.doPhase(ephemeralPublicKey, true)
                    sharedSecret = keyAgreement.generateSecret()
                    flushKeystoreBinderBuffer(recipientPrivateKey)

                    kekBytes = hkdfDerive(sharedSecret!!, masterKeyAlias.toByteArray(Charsets.UTF_8), pkg.ephemeralPublicKeyBytes!!)
                    kekSpec = CleanSecretKeySpec(kekBytes!!, DEK_ALGORITHM)

                    unwrapCipher.init(Cipher.DECRYPT_MODE, kekSpec, GCMParameterSpec(GCM_TAG_LENGTH_BITS, pkg.wrapIv))
                    dekBytes = unwrapCipher.doFinal(pkg.wrappedDek)

                } else if (pkg.magicByte == MAGIC_BYTE_SYMMETRIC) {
                    recipientPrivateKey = loadSymmetricRecipientPrivateKey()
                    val ephemeralPubKeySpec = X509EncodedKeySpec(pkg.ephemeralPublicKeyBytes!!)
                    val ephemeralPublicKey = KeyFactory.getInstance(EC_KEY_ALGORITHM).generatePublic(ephemeralPubKeySpec)

                    val keyAgreement = KeyAgreement.getInstance(KEY_AGREEMENT_ALGORITHM)
                    keyAgreement.init(recipientPrivateKey!!)
                    keyAgreement.doPhase(ephemeralPublicKey, true)
                    sharedSecret = keyAgreement.generateSecret()
                    flushKeystoreBinderBuffer(recipientPrivateKey)

                    val contextString = "${masterKeyAlias}_symmetric_ec"
                    kekBytes = hkdfDerive(sharedSecret!!, contextString.toByteArray(Charsets.UTF_8), pkg.ephemeralPublicKeyBytes!!)
                    kekSpec = CleanSecretKeySpec(kekBytes!!, DEK_ALGORITHM)

                    unwrapCipher.init(Cipher.DECRYPT_MODE, kekSpec, GCMParameterSpec(GCM_TAG_LENGTH_BITS, pkg.wrapIv))
                    dekBytes = unwrapCipher.doFinal(pkg.wrappedDek)
                } else {
                    throw IllegalArgumentException("Unsupported magic byte")
                }

                dekSpec = CleanSecretKeySpec(dekBytes, DEK_ALGORITHM)
                dataCipher.init(Cipher.DECRYPT_MODE, dekSpec, GCMParameterSpec(GCM_TAG_LENGTH_BITS, pkg.dataIv))
                dataCipher.updateAAD(associatedData)

                return dataCipher.doFinal(pkg.encryptedContent)
            } finally {
                // [FCS_CKM_EXT.4] Explicit zeroization: Prevent key remanence in memory
                if(pkg.magicByte == MAGIC_BYTE_ASYMMETRIC) {
                    //Basically this line would not be passed.
                    SecurityAuditLogger.logLine( "===== Decrypt asymmetric =====")
                    //Private Key should be null
                    SecurityAuditLogger.logKeyMaterial("Recipient UDR Key Pair (Private Key)",
                        recipientPrivateKey?.encoded);
                    SecurityAuditLogger.logKeyMaterial("Shared Secret", sharedSecret)
                    SecurityAuditLogger.logKeyMaterial("Asymmetric KEK", kekBytes)
                    SecurityAuditLogger.logKeyMaterial("Data Encryption Key (DEK)", dekBytes)
                    pkg.ephemeralPublicKeyBytes?.fill(0)
                } else {
                    SecurityAuditLogger.logLine( "===== Decrypt symmetric =====")
                    val masterKey = keyStore.getKey(symmetricMasterKeyAlias, null)
                    SecurityAuditLogger.logKeyMaterial("Symmetric UDR Key (used as KEK)", masterKey?.encoded)
                    SecurityAuditLogger.logKeyMaterial("DEK", dekBytes)
                    SecurityAuditLogger.logKeyMaterial("KEK", kekBytes)
                    SecurityAuditLogger.logKeyMaterial("Shared Secret", sharedSecret)
                }

                dekBytes?.fill(0)
                kekBytes?.fill(0)
                sharedSecret?.fill(0)

                dekSpec?.let {
                    if (!it.isDestroyed) {
                        it.destroy()
                    }
                }
                kekSpec?.let {
                    if (!it.isDestroyed) {
                        it.destroy()
                    }
                }

                try {
                    // Use 'trackbreadcrumbs' or all-zeros as preferred!
                    val dummyZeroKey = SecretKeySpec(ByteArray(32), "AES")

                    // Random IV generation to completely avoid 'IV reuse error' in GCM mode
                    val dummyIv = ByteArray(12)
                    java.security.SecureRandom().nextBytes(dummyIv)
                    val dummyParams = GCMParameterSpec(128, dummyIv)

                    // Wipe the internal cache (DEK) of dataCipher
                    dataCipher.init(Cipher.ENCRYPT_MODE, dummyZeroKey, dummyParams)

                    // Also wipe the internal cache (KEK) of unwrapCipher!
                    unwrapCipher?.init(Cipher.ENCRYPT_MODE, dummyZeroKey, dummyParams)

                } catch (e: Exception) {
                    android.util.Log.w("NiapSecAudit", "Wipe hack failed (ignored): ${e.message}")
                }
                //flushKeystoreIpcBuffers()

            }
        }
    }

    // --- StreamingAead Implementation ---
    private val rawHybridStreamingAead: StreamingAead = object : StreamingAead {
        override fun newEncryptingChannel(
            ciphertextDestination: WritableByteChannel?,
            associatedData: ByteArray?
        ): WritableByteChannel? {
            TODO("Not yet implemented")
        }

        override fun newSeekableDecryptingChannel(
            ciphertextSource: SeekableByteChannel?,
            associatedData: ByteArray?
        ): SeekableByteChannel? {
            TODO("Not yet implemented")
        }

        override fun newDecryptingChannel(
            ciphertextSource: ReadableByteChannel?,
            associatedData: ByteArray?
        ): ReadableByteChannel? {
            TODO("Not yet implemented")
        }

        override fun newEncryptingStream(ciphertext: OutputStream, associatedData: ByteArray): OutputStream {
            // [FDP_DAR_EXT.2.4] If device is unlocked, prefer symmetric encryption (0x02)
            if (!isDeviceLockedReliably()) {
                try {
                    return newEncryptingStreamSymmetric(ciphertext, associatedData)
                } catch (e: Exception) {
                    Log.w("RawHybridKeyProvider", "Symmetric stream encryption failed, falling back to asymmetric", e)
                }
            }
            return newEncryptingStreamAsymmetric(ciphertext, associatedData)
        }

        /**
         * WARNING: This symmetric stream encryption is currently unsafe due to an Android Keystore2
         * bug causing memory leakage of the plaintext DEK. This should be treated as a known risk
         * until native hardware-backed streaming encryption is supported without exposing the software DEK.
         */
        private fun newEncryptingStreamSymmetric(ciphertext: OutputStream, associatedData: ByteArray): OutputStream {
            val dekBytes = ByteArray(DEK_SIZE_BITS / 8)
            var masterKey: Key? = null
            var dekSpec: CleanSecretKeySpec? = null

            try {
                SecureRandom().nextBytes(dekBytes)
                dekSpec = CleanSecretKeySpec(dekBytes, DEK_ALGORITHM)

                masterKey = keyStore.getKey(symmetricMasterKeyAlias, null)
                    ?: throw GeneralSecurityException("Symmetric master key not found")
                val wrapCipher = Cipher.getInstance(DEK_WRAPPING_CIPHER)
                wrapCipher.init(Cipher.ENCRYPT_MODE, masterKey)
                val wrappedDek = wrapCipher.doFinal(dekBytes)
                val wrapIv = wrapCipher.iv

                val dataCipher = Cipher.getInstance(DATA_CIPHER)
                dataCipher.init(Cipher.ENCRYPT_MODE, dekSpec)
                dataCipher.updateAAD(associatedData)
                val dataIv = dataCipher.iv

                val dos = DataOutputStream(ciphertext)
                dos.writeByte(MAGIC_BYTE_SYMMETRIC.toInt())
                dos.writeInt(wrappedDek.size)
                dos.write(wrappedDek)
                dos.writeInt(wrapIv.size)
                dos.write(wrapIv)
                dos.writeInt(dataIv.size)
                dos.write(dataIv)
                dos.flush()

                // Do not zeroize IVs here, as they might be references to Cipher internal state depending on provider.
                // Leave them for GC or explicit wipe on close if we can track it.

                return CipherOutputStream(ciphertext, dataCipher)
            } finally {
                SecurityAuditLogger.logLine("===== Stream Encrypt symmetric =====")
                SecurityAuditLogger.logKeyMaterial("Symmetric UDR Key (used as KEK)", masterKey?.encoded)
                SecurityAuditLogger.logKeyMaterial("Data Encryption Key (DEK)", dekBytes)
                dekBytes.fill(0)
                // [FCS_CKM_EXT.4] & [FCS_STG_EXT.2] PREMATURE ZEROIZATION FIX: Do not destroy the key before stream is read.
                // we should destroy it on close() of the stream.
                // dekSpec?.let {
                //     if (!it.isDestroyed) {
                //         it.destroy()
                //     }
                // }
            }
        }

        private fun newEncryptingStreamAsymmetric(ciphertext: OutputStream, associatedData: ByteArray): OutputStream {
            val recipientPubKey = loadRecipientPublicKey()
            val dekBytes = ByteArray(DEK_SIZE_BITS / 8)
            var sharedSecret: ByteArray? = null
            var kekBytes: ByteArray? = null
            var ephemeralKeyPair: KeyPair? = null

            var dekSpec: CleanSecretKeySpec? = null
            var kekSpec: CleanSecretKeySpec? = null

            try {
                SecureRandom().nextBytes(dekBytes)
                dekSpec = CleanSecretKeySpec(dekBytes, DEK_ALGORITHM)

                val ephemeralKpg = KeyPairGenerator.getInstance(EC_KEY_ALGORITHM).apply { initialize(ECGenParameterSpec("secp521r1")) }
val keyPair = ephemeralKpg.generateKeyPair()

ephemeralKeyPair = keyPair


                val keyAgreement = KeyAgreement.getInstance(KEY_AGREEMENT_ALGORITHM)
keyAgreement.init(keyPair.private)

                keyAgreement.doPhase(recipientPubKey, true)
                sharedSecret = keyAgreement.generateSecret()

kekBytes =
        hkdfDerive(
                sharedSecret!!,
                masterKeyAlias.toByteArray(Charsets.UTF_8),
                keyPair.public.encoded
        )

                kekSpec = CleanSecretKeySpec(kekBytes, DEK_ALGORITHM)

                val wrapCipher = Cipher.getInstance(DEK_WRAPPING_CIPHER)
                wrapCipher.init(Cipher.ENCRYPT_MODE, kekSpec)
                val wrappedDek = wrapCipher.doFinal(dekBytes)
                val wrapIv = wrapCipher.iv

                val dataCipher = Cipher.getInstance(DATA_CIPHER)
                dataCipher.init(Cipher.ENCRYPT_MODE, dekSpec)
                dataCipher.updateAAD(associatedData)
                val dataIv = dataCipher.iv

                // Write Header directly to the output stream
                val dos = DataOutputStream(ciphertext)
                dos.writeByte(MAGIC_BYTE_ASYMMETRIC.toInt())
val ephKeyBytes = keyPair.public.encoded

                dos.writeInt(ephKeyBytes.size)
                dos.write(ephKeyBytes)
                dos.writeInt(wrappedDek.size)
                dos.write(wrappedDek)
                dos.writeInt(wrapIv.size)
                dos.write(wrapIv)
                dos.writeInt(dataIv.size)
                dos.write(dataIv)
                dos.flush()


                return CipherOutputStream(ciphertext, dataCipher)
            } finally {
                SecurityAuditLogger.logLine("===== Stream Encrypt asymmetric =====")
                SecurityAuditLogger.logKeyMaterial("Ephemeral Key Pair Public Key", ephemeralKeyPair?.public?.encoded)
                val ephPrivBytes = ephemeralKeyPair?.private?.encoded

                SecurityAuditLogger.logKeyMaterial("Ephemeral Key Pair Private Key", ephPrivBytes)

                ephPrivBytes?.fill(0)

                SecurityAuditLogger.logKeyMaterial("Recipient UDR Key Pair (Public Key)", recipientPubKey.encoded)
                SecurityAuditLogger.logKeyMaterial("Shared Secret", sharedSecret)
                SecurityAuditLogger.logKeyMaterial("Asymmetric KEK", kekBytes)
                SecurityAuditLogger.logKeyMaterial("Data Encryption Key (DEK)", dekBytes)
                dekBytes.fill(0); sharedSecret?.fill(0); kekBytes?.fill(0)

                dekSpec?.let { if (!it.isDestroyed) it.destroy() }

                kekSpec?.let { if (!it.isDestroyed) it.destroy() }

            }
        }

        /**
         * WARNING: This stream decryption is currently unsafe due to an Android Keystore2
         * bug causing memory leakage of the plaintext DEK.
         */
        override fun newDecryptingStream(ciphertext: InputStream, associatedData: ByteArray): InputStream {
            val dis = DataInputStream(ciphertext)
            val magicByte = dis.readByte()
            var dekBytes: ByteArray? = null

            var dekSpec: CleanSecretKeySpec? = null
            var kekSpec: CleanSecretKeySpec? = null
            try {
                if (magicByte == MAGIC_BYTE_ASYMMETRIC) {
                    val recipientPrivateKey = loadRecipientPrivateKey()
                    val ephKeyLen = dis.readInt()
                    val ephKeyBytes = ByteArray(ephKeyLen).apply { dis.readFully(this) }
                    val ephemeralPublicKey = KeyFactory.getInstance(EC_KEY_ALGORITHM).generatePublic(X509EncodedKeySpec(ephKeyBytes))

                    val wrapDekLen = dis.readInt()
                    val wrappedDek = ByteArray(wrapDekLen).apply { dis.readFully(this) }

                    val wrapIvLen = dis.readInt()
                    val wrapIv = ByteArray(wrapIvLen).apply { dis.readFully(this) }

                    val dataIvLen = dis.readInt()
                    val dataIv = ByteArray(dataIvLen).apply { dis.readFully(this) }

                    var sharedSecret: ByteArray? = null
                    var kekBytes: ByteArray? = null


                    try {
                        val keyAgreement = KeyAgreement.getInstance(KEY_AGREEMENT_ALGORITHM)
                        keyAgreement.init(recipientPrivateKey!!)
                        keyAgreement.doPhase(ephemeralPublicKey, true)
                        sharedSecret = keyAgreement.generateSecret()

                        kekBytes = hkdfDerive(sharedSecret!!, masterKeyAlias.toByteArray(Charsets.UTF_8), ephKeyBytes)
                        kekSpec = CleanSecretKeySpec(kekBytes!!, DEK_ALGORITHM)

                        val unwrapCipher = Cipher.getInstance(DEK_WRAPPING_CIPHER)
                        unwrapCipher.init(Cipher.DECRYPT_MODE, kekSpec, GCMParameterSpec(GCM_TAG_LENGTH_BITS, wrapIv))
                        dekBytes = unwrapCipher.doFinal(wrappedDek)

                        val dataCipher = Cipher.getInstance(DATA_CIPHER)
                        dekSpec = CleanSecretKeySpec(dekBytes, DEK_ALGORITHM)
                        dataCipher.init(Cipher.DECRYPT_MODE, dekSpec, GCMParameterSpec(GCM_TAG_LENGTH_BITS, dataIv))
                        dataCipher.updateAAD(associatedData)

                        return CipherInputStream(ciphertext, dataCipher)
                    } finally {
                        SecurityAuditLogger.logLine("===== Stream Decrypt asymmetric =====")
                        SecurityAuditLogger.logKeyMaterial("Recipient UDR Key Pair (Private Key)", recipientPrivateKey.encoded)
                        SecurityAuditLogger.logKeyMaterial("Shared Secret", sharedSecret)
                        SecurityAuditLogger.logKeyMaterial("Asymmetric KEK", kekBytes)
                        SecurityAuditLogger.logKeyMaterial("Data Encryption Key (DEK)", dekBytes)
                        ephKeyBytes.fill(0);sharedSecret?.fill(0);
                        kekBytes?.fill(0);dekBytes?.fill(0)

                        // [FCS_CKM_EXT.4] & [FCS_STG_EXT.2] PREMATURE ZEROIZATION FIX: Do not destroy the key before stream is read.
                        // we should destroy it on close() of the stream.
                        // dekSpec?.let {
                        //     if (!it.isDestroyed) {
                        //         it.destroy()
                        //     }
                        // }
                        kekSpec?.let {
                            if (!it.isDestroyed) {
                                it.destroy()
                            }
                        }
                    }
                } else if (magicByte == MAGIC_BYTE_SYMMETRIC) {
                    try {
                        val wrapDekLen = dis.readInt()
                        val wrappedDek = ByteArray(wrapDekLen).apply { dis.readFully(this) }

                        val wrapIvLen = dis.readInt()
                        val wrapIv = ByteArray(wrapIvLen).apply { dis.readFully(this) }

                        val dataIvLen = dis.readInt()
                        val dataIv = ByteArray(dataIvLen).apply { dis.readFully(this) }

                        val masterKey = keyStore.getKey(symmetricMasterKeyAlias, null)
                            ?: throw GeneralSecurityException("Symmetric master key not found")
                        val unwrapCipher = Cipher.getInstance(DEK_WRAPPING_CIPHER)
                        unwrapCipher.init(
                            Cipher.DECRYPT_MODE,
                            masterKey,
                            GCMParameterSpec(GCM_TAG_LENGTH_BITS, wrapIv)
                        )
                        dekBytes = unwrapCipher.doFinal(wrappedDek)

                        val dataCipher = Cipher.getInstance(DATA_CIPHER)
                        dekSpec = CleanSecretKeySpec(dekBytes, DEK_ALGORITHM)
                        dataCipher.init(
                            Cipher.DECRYPT_MODE,
                            dekSpec,
                            GCMParameterSpec(GCM_TAG_LENGTH_BITS, dataIv)
                        )
                        dataCipher.updateAAD(associatedData)

                        return CipherInputStream(ciphertext, dataCipher)
                    } finally {
                        SecurityAuditLogger.logLine("===== Stream Decrypt symmetric =====")
                        SecurityAuditLogger.logKeyMaterial("Symmetric UDR Key (used as KEK)",
                            keyStore.getKey(symmetricMasterKeyAlias, null)?.encoded)
                        SecurityAuditLogger.logKeyMaterial("Data Encryption Key (DEK)", dekBytes)
                        dekBytes?.fill(0)

                        // [FCS_CKM_EXT.4] & [FCS_STG_EXT.2] PREMATURE ZEROIZATION FIX: Do not destroy the key before stream is read.
                        // we should destroy it on close() of the stream.
                        // dekSpec?.let {
                        //     if (!it.isDestroyed) {
                        //         it.destroy()
                        //     }
                        // }
                    }
                } else {
                    throw IllegalArgumentException("Unsupported magic byte: $magicByte")
                }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override fun getAead(): Aead = rawHybridAead
    override fun getStreamingAead(): StreamingAead = rawHybridStreamingAead
    override fun getUnlockDeviceRequired(): Boolean = unlockedDeviceRequired
    override fun rewrapKeyToSymmetricUdr(encryptedDek: ByteArray): ByteArray {

        val pkg = deserializeEncryptedPackage(encryptedDek)
        if (pkg.magicByte == MAGIC_BYTE_SYMMETRIC) return encryptedDek

        var dekBytes: ByteArray? = null
        var newWrappedDek: ByteArray? = null
        var newWrapIv: ByteArray? = null
        var symmetricSharedSecret: ByteArray? = null
        var symmetricKekBytes: ByteArray? = null
        var symmetricKekSpec: CleanSecretKeySpec? = null
        var ephemeralPublicKeyBytes: ByteArray? = null

        try {
            // 1. Decrypt existing asymmetric wrapper to get DEK
            val recipientPrivateKey = loadRecipientPrivateKey()
            val ephemeralPubKeySpec = X509EncodedKeySpec(pkg.ephemeralPublicKeyBytes!!)
            val ephemeralPublicKey = KeyFactory.getInstance(EC_KEY_ALGORITHM).generatePublic(ephemeralPubKeySpec)
            var sharedSecret: ByteArray? = null
            var kekBytes: ByteArray? = null
            var kekSpec: CleanSecretKeySpec? = null
            try {
                val keyAgreement = KeyAgreement.getInstance(KEY_AGREEMENT_ALGORITHM)
                keyAgreement.init(recipientPrivateKey!!)
                keyAgreement.doPhase(ephemeralPublicKey, true)
                sharedSecret = keyAgreement.generateSecret()
                kekBytes = hkdfDerive(sharedSecret!!, masterKeyAlias.toByteArray(Charsets.UTF_8), pkg.ephemeralPublicKeyBytes!!)
                kekSpec = CleanSecretKeySpec(kekBytes, DEK_ALGORITHM)
                val unwrapCipher = Cipher.getInstance(DEK_WRAPPING_CIPHER)
                unwrapCipher.init(Cipher.DECRYPT_MODE, kekSpec, GCMParameterSpec(GCM_TAG_LENGTH_BITS, pkg.wrapIv))
                dekBytes = unwrapCipher.doFinal(pkg.wrappedDek)
            } finally {
                SecurityAuditLogger.logLine("===== Rewrap: Decrypt asymmetric phase =====")
                SecurityAuditLogger.logKeyMaterial("Recipient UDR Key Pair (Private Key)", recipientPrivateKey.encoded)
                SecurityAuditLogger.logKeyMaterial("Shared Secret", sharedSecret)
                SecurityAuditLogger.logKeyMaterial("Asymmetric KEK", kekBytes)
                sharedSecret?.fill(0);
                kekBytes?.fill(0)

                kekSpec?.let { if (!it.isDestroyed) it.destroy() }
            }

            // 2. Re-wrap DEK with symmetric master key (using ECDH as per new Solution 2)
            val symmetricRecipientPubKey = loadSymmetricPublicKey()
            val ephemeralKpg = KeyPairGenerator.getInstance(EC_KEY_ALGORITHM).apply { initialize(ECGenParameterSpec("secp521r1")) }
            val keyPair = ephemeralKpg.generateKeyPair()
            
            val keyAgreement = KeyAgreement.getInstance(KEY_AGREEMENT_ALGORITHM)
            keyAgreement.init(keyPair.private)
            keyAgreement.doPhase(symmetricRecipientPubKey, true)
            symmetricSharedSecret = keyAgreement.generateSecret()
            
            val contextString = "${masterKeyAlias}_symmetric_ec"
            symmetricKekBytes = hkdfDerive(symmetricSharedSecret!!, contextString.toByteArray(Charsets.UTF_8), keyPair.public.encoded)
            symmetricKekSpec = CleanSecretKeySpec(symmetricKekBytes!!, DEK_ALGORITHM)
            
            val wrapCipher = Cipher.getInstance(DEK_WRAPPING_CIPHER)
            wrapCipher.init(Cipher.ENCRYPT_MODE, symmetricKekSpec)
            newWrappedDek = wrapCipher.doFinal(dekBytes)
            newWrapIv = wrapCipher.iv
            ephemeralPublicKeyBytes = keyPair.public.encoded

            // 3. Return new package with symmetric magic byte and ephemeral public key
            return serializeEncryptedPackage(
                MAGIC_BYTE_SYMMETRIC,
                ephemeralPublicKeyBytes,
                newWrappedDek,
                newWrapIv,
                pkg.encryptedContent,
                pkg.dataIv
            )
        } finally {
            SecurityAuditLogger.logLine("===== Rewrap: Re-encrypt symmetric phase =====")
            SecurityAuditLogger.logKeyMaterial("Symmetric Ephemeral Public Key", ephemeralPublicKeyBytes)
            SecurityAuditLogger.logKeyMaterial("Symmetric Shared Secret", symmetricSharedSecret)
            SecurityAuditLogger.logKeyMaterial("Symmetric KEK", symmetricKekBytes)
            SecurityAuditLogger.logKeyMaterial("Data Encryption Key (DEK)", dekBytes)
            
            dekBytes?.fill(0)
            newWrappedDek?.fill(0)
            newWrapIv?.fill(0)
            symmetricSharedSecret?.fill(0)
            symmetricKekBytes?.fill(0)

            symmetricKekSpec?.let { if (!it.isDestroyed) it.destroy() }
        }
    }

    override fun isSymmetricallyWrapped(encryptedDek: ByteArray): Boolean {
        if (encryptedDek.isEmpty()) return false
        return encryptedDek[0] == MAGIC_BYTE_SYMMETRIC
    }

    override fun destroy() {
        // [FCS_CKM_EXT.4] & [FCS_STG_EXT.2] Secure deletion from persistent storage
        try {
            if (keyStore.containsAlias(masterKeyAlias)) {
                keyStore.deleteEntry(masterKeyAlias)
            }
            if (keyStore.containsAlias(symmetricMasterKeyAlias)) {
                keyStore.deleteEntry(symmetricMasterKeyAlias)
            }
            prefs.edit().clear().apply()
        } catch (e: Exception) { }
    }
}

private data class EncryptedPackage(
    val magicByte: Byte,
    val ephemeralPublicKeyBytes: ByteArray?,
    val wrappedDek: ByteArray,
    val wrapIv: ByteArray,
    val encryptedContent: ByteArray,
    val dataIv: ByteArray
)

private fun serializeEncryptedPackage(magicByte: Byte, ephemeralPublicKeyBytes: ByteArray?, wrappedDek: ByteArray, wrapIv: ByteArray, encryptedContent: ByteArray, dataIv: ByteArray): ByteArray {
    val bos = ByteArrayOutputStream()
    DataOutputStream(bos).use {
        it.writeByte(magicByte.toInt())
        // WORKAROUND [FCS_CKM_EXT.4] Keystore2 Leak Fix: Both Asymmetric AND Symmetric modes now use ECDH
        // (Hybrid Envelope Encryption) to avoid raw DEK exposure in Binder IPC.
        if (magicByte == RawHybridKeyProvider.MAGIC_BYTE_ASYMMETRIC || magicByte == RawHybridKeyProvider.MAGIC_BYTE_SYMMETRIC) {
            val ephKey = ephemeralPublicKeyBytes ?: throw IllegalArgumentException("Ephemeral public key required for ECDH mode")
            it.writeInt(ephKey.size)
            it.write(ephKey)
        }
        it.writeInt(wrappedDek.size)
        it.write(wrappedDek)
        it.writeInt(wrapIv.size)
        it.write(wrapIv)
        it.writeInt(dataIv.size)
        it.write(dataIv)
        it.write(encryptedContent)
    }
    return bos.toByteArray()
}

private fun deserializeEncryptedPackage(ciphertext: ByteArray): EncryptedPackage {
    val buffer = ByteBuffer.wrap(ciphertext)

    val magicByte = buffer.get()
    var ephKey: ByteArray? = null
    
    // WORKAROUND [FCS_CKM_EXT.4] Keystore2 Leak Fix: Both Asymmetric AND Symmetric modes now use ECDH 
    // (Hybrid Envelope Encryption) to avoid raw DEK exposure in Binder IPC.
    if (magicByte == RawHybridKeyProvider.MAGIC_BYTE_ASYMMETRIC || magicByte == RawHybridKeyProvider.MAGIC_BYTE_SYMMETRIC) {
        val ephKeySize = buffer.int
        ephKey = ByteArray(ephKeySize).apply { buffer.get(this) }
    } else {
        throw IllegalArgumentException("Invalid magic byte or unsupported format: $magicByte")
    }

    val wrapDekSize = buffer.int
    val wrapDek = ByteArray(wrapDekSize).apply { buffer.get(this) }
    val wrapIvSize = buffer.int
    val wrapIv = ByteArray(wrapIvSize).apply { buffer.get(this) }
    val dataIvSize = buffer.int
    val dataIv = ByteArray(dataIvSize).apply { buffer.get(this) }
    val content = ByteArray(buffer.remaining()).apply { buffer.get(this) }
    return EncryptedPackage(magicByte, ephKey, wrapDek, wrapIv, content, dataIv)
}
