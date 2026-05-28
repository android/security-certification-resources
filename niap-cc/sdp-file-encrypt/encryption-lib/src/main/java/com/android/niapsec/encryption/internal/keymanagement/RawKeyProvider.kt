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
import android.security.keystore.KeyProperties
import android.security.keystore.KeyProtection
import android.util.Log
import com.google.crypto.tink.Aead
import java.nio.ByteBuffer
import java.security.GeneralSecurityException
import java.security.KeyStore
import java.util.UUID
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

/**
 * A KeyProvider that wraps the raw JCA Cipher API into a Tink-compatible Aead.
 * This allows using the standard Android Keystore without the Tink library for key management.
 */
class RawKeyProvider(
    private val context: Context,
    private val keyAliasPrefix: String,
    private val unlockedDeviceRequired: Boolean
) : KeyProvider {
    private val KEY_ALIAS_PREFIX = "raw_provider_key_${keyAliasPrefix}_"
    private val ANDROID_KEYSTORE = "AndroidKeyStore"
    private val KEY_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
    private val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
    private val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
    private val TRANSFORMATION = "$KEY_ALGORITHM/$BLOCK_MODE/$PADDING"

    private val keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }

    private val rawAead: Aead = object : Aead {
        override fun encrypt(plaintext: ByteArray, associatedData: ByteArray): ByteArray {
            val keyAlias = KEY_ALIAS_PREFIX + UUID.randomUUID().toString()
            val cipher = Cipher.getInstance(TRANSFORMATION)
            val secretKey: SecretKey = generateEphemeralSoftwareKey(keyAlias)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)

            val spec = cipher.parameters.getParameterSpec(IvParameterSpec::class.java)
            val iv = spec.iv
            val aliasBytes = keyAlias.toByteArray(Charsets.UTF_8)

            val header = ByteBuffer.allocate(4 + aliasBytes.size + 4 + iv.size)
                .putInt(aliasBytes.size)
                .put(aliasBytes)
                .putInt(iv.size)
                .put(iv)
                .array()

            val ciphertext = cipher.doFinal(plaintext)
            return header + ciphertext
        }

        override fun decrypt(ciphertext: ByteArray, associatedData: ByteArray): ByteArray {
            val buffer = ByteBuffer.wrap(ciphertext)

            val aliasLength = buffer.int
            if (aliasLength <= 0 || aliasLength > buffer.remaining()) throw GeneralSecurityException("Invalid alias length")
            val aliasBytes = ByteArray(aliasLength)
            buffer.get(aliasBytes)
            val keyAlias = String(aliasBytes, Charsets.UTF_8)

            val ivLength = buffer.int
            if (ivLength <= 0 || ivLength > buffer.remaining()) throw GeneralSecurityException("Invalid IV length")
            val iv = ByteArray(ivLength)
            buffer.get(iv)

            val cipher = Cipher.getInstance(TRANSFORMATION)

            val actualCiphertext = ByteArray(buffer.remaining())
            buffer.get(actualCiphertext)

            val secretKey = getSecretKey(keyAlias)
            val spec = IvParameterSpec(iv)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

            return cipher.doFinal(actualCiphertext)
        }
    }

    private fun generateEphemeralSoftwareKey(keyAlias: String): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM, "AndroidOpenSSL")
        keyGenerator.init(256)
        val secretKey = keyGenerator.generateKey()
        val secretKeyEntry = KeyStore.SecretKeyEntry(secretKey)

        keyStore.setEntry(
            keyAlias, secretKeyEntry,
            KeyProtection.Builder((KeyProperties.PURPOSE_DECRYPT or KeyProperties.PURPOSE_ENCRYPT))
                .setEncryptionPaddings(PADDING)
                .setBlockModes(BLOCK_MODE)
                .setUnlockedDeviceRequired(unlockedDeviceRequired)
                .build()
        )
        return secretKey
    }

    private fun getSecretKey(keyAlias: String): SecretKey {
        return keyStore.getKey(keyAlias, null) as SecretKey
    }

    override fun getUnlockDeviceRequired(): Boolean {
        return unlockedDeviceRequired
    }

    override fun getAead(): Aead {
        return rawAead
    }

    override fun rewrapKeyToSymmetricUdr(encryptedDek: ByteArray): ByteArray {
        TODO("Not yet implemented")
    }

    override fun isSymmetricallyWrapped(encryptedDek: ByteArray): Boolean {
        TODO("Not yet implemented")
    }

    override fun destroy() {
        try {
            val aliases = keyStore.aliases()
            while (aliases.hasMoreElements()) {
                val alias = aliases.nextElement()
                if (alias.startsWith(KEY_ALIAS_PREFIX)) {
                    keyStore.deleteEntry(alias)
                }
            }
        } catch (e: Exception) {
            Log.e("RawKeyProvider", "Failed to destroy keys", e)
        }
    }
}
