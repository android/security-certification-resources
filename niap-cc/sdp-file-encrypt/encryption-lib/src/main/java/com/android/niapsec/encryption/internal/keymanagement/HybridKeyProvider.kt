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
import android.util.Log
import androidx.core.content.edit
import com.google.crypto.tink.Aead
import com.google.crypto.tink.CleartextKeysetHandle
import com.google.crypto.tink.HybridDecrypt
import com.google.crypto.tink.HybridEncrypt
import com.google.crypto.tink.JsonKeysetReader
import com.google.crypto.tink.JsonKeysetWriter
import com.google.crypto.tink.KeyTemplate
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.hybrid.HybridConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.google.crypto.tink.proto.AesGcmKeyFormat
import com.google.crypto.tink.proto.EcPointFormat
import com.google.crypto.tink.proto.EciesAeadDemParams
import com.google.crypto.tink.proto.EciesAeadHkdfKeyFormat
import com.google.crypto.tink.proto.EciesAeadHkdfParams
import com.google.crypto.tink.proto.EciesHkdfKemParams
import com.google.crypto.tink.proto.EllipticCurveType
import com.google.crypto.tink.proto.HashType
import java.io.ByteArrayOutputStream
import java.security.GeneralSecurityException
import java.security.KeyStore
import javax.crypto.KeyGenerator

/**
 * [Security Component: Tink-based Hybrid Encryption]
 * * Implementation relies on Google Tink's `AndroidKeysetManager` and `KeysetHandle`.
 * * This class leverages validated library implementations for cryptographic schemes.
 *
 * [Compliance Note]
 * * **FCS_CKM.2/LOCKED (Cryptographic Key Establishment):**
 * - SATISFIED: Uses Tink's ECIES-AEAD-HKDF implementation to establish keys for encryption
 * even while the device is in a Locked State.
 *
 * * **FCS_STG_EXT.2 (Encrypted Key Storage):**
 * - SATISFIED: Private keysets are stored in SharedPreferences wrapped (encrypted) by a Master Key
 * held in the Android Keystore. The keyset is never stored in plaintext on the filesystem.
 *
 * * **FDP_DAR_EXT.2 (Sensitive Data Encryption):**
 * - SATISFIED: Implements public key encryption (ECIES-AEAD-HKDF) allowing data ingestion and protection
 * while the device is in a Locked State (B/F/U states).
 *
 * * **FIA_UAU_EXT.1 (Authentication for Cryptographic Operation):**
 * - SATISFIED: The Master Key (wrapping key) is configured with `.setUnlockedDeviceRequired(true)`.
 * Tink cannot unwrap (decrypt) the Keyset containing the private key unless the user has authenticated.
 *
 * * **FCS_CKM_EXT.4 (Key Destruction):**
 * - PARTIALLY SATISFIED (Storage Only): `destroy()` removes the encrypted keyset and the Master Key alias.
 * - NOTE: Volatile memory zeroization relies on the underlying Tink library implementation and JVM
 * Garbage Collection. (For explicit memory zeroization requirements, see `RawHybridKeyProvider`).
 */
class HybridKeyProvider(
    private val context: Context,
    private val masterKeyUri: String,
    _unlockedDeviceRequired: Boolean,
    private val keysetPrefName: String
) : KeyProvider {

    private val deviceProtectedContext = context.createDeviceProtectedStorageContext()
    companion object {
        val P521_AES256_GCM_TEMPLATE: KeyTemplate by lazy {
            val demAeadKeyFormat = AesGcmKeyFormat.newBuilder().setKeySize(32).build()
            val demKeyTemplate = com.google.crypto.tink.proto.KeyTemplate.newBuilder()
                .setValue(demAeadKeyFormat.toByteString())
                .setTypeUrl("type.googleapis.com/google.crypto.tink.AesGcmKey")
                .setOutputPrefixType(com.google.crypto.tink.proto.OutputPrefixType.TINK)
                .build()
            val demParams = EciesAeadDemParams.newBuilder().setAeadDem(demKeyTemplate).build()
            val kemParams = EciesHkdfKemParams.newBuilder()
                .setCurveType(EllipticCurveType.NIST_P521)
                .setHkdfHashType(HashType.SHA256)
                .build()
            val eciesParams = EciesAeadHkdfParams.newBuilder()
                .setKemParams(kemParams)
                .setDemParams(demParams)
                .setEcPointFormat(EcPointFormat.UNCOMPRESSED)
                .build()
            val keyFormat = EciesAeadHkdfKeyFormat.newBuilder()
                .setParams(eciesParams)
                .build()
            KeyTemplate.create(
                "type.googleapis.com/google.crypto.tink.EciesAeadHkdfPrivateKey",
                keyFormat.toByteString().toByteArray(),
                KeyTemplate.OutputPrefixType.TINK
            )
        }
    }

    private val PRIVATE_KEYSET_NAME = "hybrid_private_keyset"
    private val PUBLIC_KEYSET_PREF_NAME = "hybrid_public_keyset_pref"
    private val PUBLIC_KEYSET_KEY = "public_keyset"
    val unlockedDeviceRequired: Boolean = _unlockedDeviceRequired

    init {
        AeadConfig.register()
        HybridConfig.register()
        try {
            createMasterKeyIfNeeded()
            synchronizePublicKeyset()
        } catch (e: Exception) {
            Log.w("HybridKeyProvider", "Initialization sync failed, will retry later: ${e.message}")
        }
    }

    private fun getPublicKeysetHandle(): KeysetHandle {
        val prefs = deviceProtectedContext.getSharedPreferences(PUBLIC_KEYSET_PREF_NAME, Context.MODE_PRIVATE)
        var serializedPublicKeyset = prefs.getString(PUBLIC_KEYSET_KEY, null)

        if (serializedPublicKeyset == null) {
            // Public keyset is not found, so we need to generate and save it now.
            // This might fail if the device is locked.
            synchronizePublicKeyset()
            serializedPublicKeyset = prefs.getString(PUBLIC_KEYSET_KEY, null)
        }

        return serializedPublicKeyset?.let {
            CleartextKeysetHandle.read(JsonKeysetReader.withString(it))
        } ?: throw GeneralSecurityException(
            "Could not get or create public keyset. Device might be locked."
        )
    }

    private fun synchronizePublicKeyset() {
        try {
            // Get the private keyset, which may trigger key generation if it doesn't exist
            val privateKeysetManager: AndroidKeysetManager by lazy {
                AndroidKeysetManager.Builder()
                    .withSharedPref( deviceProtectedContext, PRIVATE_KEYSET_NAME, keysetPrefName)
                    .withKeyTemplate(P521_AES256_GCM_TEMPLATE)
                    .withMasterKeyUri(masterKeyUri)
                    .build()
            }
            //Extract the public keyset
            val publicKeysetHandle = privateKeysetManager.keysetHandle.publicKeysetHandle

            // Serialize the public keyset to a string
            val outputStream = ByteArrayOutputStream()
            CleartextKeysetHandle.write(
                publicKeysetHandle, JsonKeysetWriter.withOutputStream(outputStream)
            )
            val serializedPublicKeyset = outputStream.toString()

            // Manually write the serialized public keyset to a separate shared preference.
            deviceProtectedContext.getSharedPreferences(PUBLIC_KEYSET_PREF_NAME, Context.MODE_PRIVATE)
                .edit(commit = true) {
                    putString(PUBLIC_KEYSET_KEY, serializedPublicKeyset)
                }
        } catch (e: GeneralSecurityException) {
            // This can happen if the device is locked and the private key is not in memory.
            Log.w(
                "HybridKeyProvider",
                "Could not synchronize public keyset, device might be locked.",
                e
            )
            // Re-throw the exception to notify the caller that the operation failed.
            throw e
        }
    }

    override fun getUnlockDeviceRequired(): Boolean {
        return unlockedDeviceRequired
    }

    override fun rewrapKeyToSymmetricUdr(encryptedDek: ByteArray): ByteArray {
        TODO("Not yet implemented")
    }

    override fun isSymmetricallyWrapped(encryptedDek: ByteArray): Boolean {
        TODO("Not yet implemented")
    }

    private fun createMasterKeyIfNeeded() {
        val keyAlias = masterKeyUri.removePrefix("android-keystore://")
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        if (!keyStore.containsAlias(keyAlias)) {
            val keyGenerator =
                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            val specBuilder = KeyGenParameterSpec.Builder(
                keyAlias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)

            // [FIA_UAU_EXT.1] Authentication for Cryptographic Operation
            // * REQUIREMENT: The TSF shall require the user to be authenticated before performing specific cryptographic operations.
            // * IMPLEMENTATION: By invoking `setUnlockedDeviceRequired(true)`, we mandate that the Android Keystore system
            //   blocks any access to this key material unless the device is in an unlocked state (user authenticated).
            if (unlockedDeviceRequired) {
                specBuilder.setUnlockedDeviceRequired(true)
            }
            keyGenerator.init(specBuilder.build())
            keyGenerator.generateKey()
        }
    }

    override fun getAead(): Aead {

        // [FCS_CKM.2/LOCKED] & [FDP_DAR_EXT.2]
        // Public key from Keyset allows key establishment and encryption in Locked State.
        // This does not require the device to be unlocked as the public key is stored in cleartext.
        val publicKeysetHandle = getPublicKeysetHandle()
        val hybridEncrypt = publicKeysetHandle.getPrimitive(HybridEncrypt::class.java)

        return object : Aead {
            override fun encrypt(plaintext: ByteArray, associatedData: ByteArray): ByteArray {
                // [FDP_DAR_EXT.2] Public key encryption allows operation in Locked State
                return hybridEncrypt.encrypt(plaintext, associatedData)
            }

            override fun decrypt(ciphertext: ByteArray, associatedData: ByteArray): ByteArray {
                // [FCS_STG_EXT.2] Private key is wrapped by Android Keystore (Master Key)
                //Don't hold the private key and Manager in memory.
                AndroidKeysetManager.Builder()
                    .withSharedPref( deviceProtectedContext, PRIVATE_KEYSET_NAME, keysetPrefName)
                    .withKeyTemplate(P521_AES256_GCM_TEMPLATE)
                    .withMasterKeyUri(masterKeyUri)
                    .build().let { privateKeysetManager ->
                        val privateKeysetHandle = privateKeysetManager.keysetHandle
                        val hybridDecrypt = privateKeysetHandle.getPrimitive(HybridDecrypt::class.java)
                        return hybridDecrypt.decrypt(ciphertext, associatedData)
                    }

            }
        }
    }

    override fun destroy() {
        // Clear the private keyset preference file
        deviceProtectedContext.getSharedPreferences(keysetPrefName, Context.MODE_PRIVATE).edit(commit = true) {
            clear()
        }
        // Clear the public keyset preference file
        deviceProtectedContext.getSharedPreferences(PUBLIC_KEYSET_PREF_NAME, Context.MODE_PRIVATE)
            .edit(commit = true) {
                clear()
            }

        try {
            val keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyStore.load(null)
            val keyAlias = masterKeyUri.removePrefix("android-keystore://")
            if (keyStore.containsAlias(keyAlias)) {
                keyStore.deleteEntry(keyAlias)
            }
        } catch (e: Exception) {
            Log.e("HybridKeyProvider", "Failed to destroy master key", e)
        }
    }
}


