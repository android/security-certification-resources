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
package com.android.niapsec.encryption.api

import android.content.Context
import android.util.Base64
import android.util.Log
import com.android.niapsec.encryption.internal.EncryptionProvider
import com.android.niapsec.encryption.internal.TinkEncryptionProvider
import com.android.niapsec.encryption.internal.keymanagement.HybridKeyProvider
import com.android.niapsec.encryption.internal.keymanagement.RawHybridKeyProvider
import com.android.niapsec.encryption.internal.keymanagement.RawKeyProvider
import java.io.File
import java.io.InputStream
import java.io.OutputStream

/**
 * Manages encryption and decryption operations for files and strings using a configurable KeyProvider.
 */
class EncryptionManager(
    context: Context,
    masterKeyUri: String,
    providerType: KeyProviderType = KeyProviderType.HYBRID,
    unlockedDeviceRequired: Boolean = false,
    lockStatePollCount: Int = 5,
    lockStatePollIntervalMs: Long = 100,
    private val encryptionProvider: EncryptionProvider = TinkEncryptionProvider(context,
        when (providerType) {
            KeyProviderType.RAW ->
                RawKeyProvider(context, masterKeyUri.replace("android-keystore://", ""), unlockedDeviceRequired)
            KeyProviderType.HYBRID ->
                HybridKeyProvider(context, masterKeyUri, unlockedDeviceRequired, "tink_keyset_${masterKeyUri.replace("android-keystore://", "")}")
          KeyProviderType.RAW_HYBRID ->
                RawHybridKeyProvider(context, masterKeyUri, unlockedDeviceRequired, "tink_keyset_${masterKeyUri.replace("android-keystore://", "")}", lockStatePollCount, lockStatePollIntervalMs)
        },
        when (providerType) {
            KeyProviderType.RAW -> "ERAW".toByteArray()
            KeyProviderType.HYBRID -> "EHBT".toByteArray()
            KeyProviderType.RAW_HYBRID -> "EHBR".toByteArray()
        }
    )
) {

    fun destroy() {
        encryptionProvider.destroy()
    }

    fun getUnlockDeviceRequired(): Boolean {
        return (encryptionProvider as TinkEncryptionProvider).keyProvider.getUnlockDeviceRequired()
    }

    /**
     * Encrypts a file using in-memory processing.
     * Suitable for small files.
     */
    fun encryptToFile(file: File): OutputStream {
        val encryptedContent = encryptionProvider.encrypt(file);
        return encryptedContent
    }

    /**
     * Decrypts a file using in-memory processing.
     */
    fun decryptFromFile(file: File): InputStream {
        return encryptionProvider.decrypt(file)
    }

    /**
     * Encrypts a file using streaming processing.
     * Suitable for large files. Throws UnsupportedOperationException if the provider doesn't support streaming.
     */
    fun encryptToFileStream(file: File): OutputStream {
        return encryptionProvider.encryptStream(file)
    }

    /**
     * Encrypts a source file to a destination file using streaming processing.
     * Uses a temporary `.tmp` file during encryption to prevent corruption if interrupted,
     * and atomically renames it to the destination file upon successful completion.
     * 
     * @param sourceFile The plaintext input file.
     * @param destFile The target encrypted output file (e.g., `.enc`).
     * @param deleteOriginal If true, safely deletes the source file upon success.
     */
    fun encryptFile(sourceFile: File, destFile: File, deleteOriginal: Boolean = true) {
        val tmpFile = File(destFile.absolutePath + ".tmp")
        try {
            sourceFile.inputStream().use { input ->
                try {
                    encryptToFileStream(tmpFile).use { output ->
                        input.copyTo(output)
                    }
                } catch (e: java.lang.UnsupportedOperationException) {
                    // Fallback to in-memory encryption if streaming is not supported
                    encryptToFile(tmpFile).use { output ->
                        input.copyTo(output)
                    }
                }
            }
            // Stream writing completed and closed successfully. Atomic rename:
            if (!tmpFile.renameTo(destFile)) {
                throw java.io.IOException("Failed to rename temporary encrypted file to target destination.")
            }
            // If successful and requested, delete original
            if (deleteOriginal) {
                sourceFile.delete()
            }
        } catch (e: Exception) {
            // Clean up temporary file on failure
            if (tmpFile.exists()) {
                tmpFile.delete()
            }
            throw e
        }
    }

    /**
     * Decrypts a file using streaming processing.
     */
    fun decryptFromFileStream(file: File): InputStream {
        return encryptionProvider.decryptStream(file)
    }

    /**
     * Encrypts a string and returns it as a Base64-encoded ciphertext.
     */
    fun encryptToString(plaintext: String): String {
        val ciphertext = encryptionProvider.encrypt(plaintext)
        return Base64.encodeToString(ciphertext, Base64.DEFAULT)
    }

    /**
     * Decrypts a Base64-encoded ciphertext string and returns the plaintext.
     */
    fun decryptFromString(ciphertext: String): String {
        val ciphertextBytes = Base64.decode(ciphertext, Base64.DEFAULT)
        return encryptionProvider.decrypt(ciphertextBytes)
    }

    /**
     * Re-wraps the file key from asymmetric to symmetric scheme.
     * This is useful for transitioning data received while locked to a more permanent
     * symmetric protection after the device is unlocked (FDP_DAR_EXT.2.4 compliance).
     */
    fun rewrapFileKey(file: File): Boolean {
        return encryptionProvider.rewrapFileKey(android.net.Uri.fromFile(file))
    }

    /**
     * Sweeps the app's files directory and re-wraps all pending encrypted files
     * to the symmetric scheme.
     */
    fun sweepAndRewrapPendingFiles() {
        encryptionProvider.sweepAndRewrapPendingFiles()
    }
}
