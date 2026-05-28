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

package com.android.niapsec.encryption.internal

import android.content.Context
import android.net.Uri
import android.util.Log
import com.android.niapsec.encryption.internal.keymanagement.KeyProvider
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.security.GeneralSecurityException

class TinkEncryptionProvider(
    private val context: Context,
    val keyProvider: KeyProvider,
    private val providerHeader: ByteArray // [MDFPP] 4-byte Magic Header: ERAW, EHBT, or EHBR
) : EncryptionProvider {

    // --- File Operations (In-Memory) ---

    override fun encrypt(file: File): OutputStream {
        val aead = keyProvider.getAead()
        val fileOutputStream = FileOutputStream(file)
        fileOutputStream.write(providerHeader)

        return object : ByteArrayOutputStream() {
            override fun close() {
                val ciphertext = aead.encrypt(toByteArray(), providerHeader)
                fileOutputStream.write(ciphertext)
                fileOutputStream.close()
                super.close()
            }
        }
    }

    override fun decrypt(file: File): InputStream {
        val fileInputStream = FileInputStream(file)
        try {
            val header = ByteArray(providerHeader.size)
            if (fileInputStream.read(header) != header.size || !header.contentEquals(providerHeader)) {
                throw GeneralSecurityException("Invalid provider header.")
            }
            val ciphertext = fileInputStream.readBytes()
            val aead = keyProvider.getAead()
            val plaintext = aead.decrypt(ciphertext, providerHeader)
            return ByteArrayInputStream(plaintext)
        } finally {
            fileInputStream.close()
        }
    }

    // --- Stream Operations (StreamingAead) ---

    override fun encryptStream(file: File): OutputStream {
        val streamingAead = keyProvider.getStreamingAead() ?: throw UnsupportedOperationException("Streaming not supported")
        val fileOutputStream = FileOutputStream(file)
        fileOutputStream.write(providerHeader)
        return streamingAead.newEncryptingStream(fileOutputStream, providerHeader)
    }

    override fun decryptStream(file: File): InputStream {
        val streamingAead = keyProvider.getStreamingAead() ?: throw UnsupportedOperationException("Streaming not supported")
        val fileInputStream = FileInputStream(file)
        try {
            val header = ByteArray(providerHeader.size)
            if (fileInputStream.read(header) != header.size || !header.contentEquals(providerHeader)) {
                throw GeneralSecurityException("Invalid provider header.")
            }
            return streamingAead.newDecryptingStream(fileInputStream, providerHeader)
        } catch (e: Exception) {
            fileInputStream.close()
            throw e
        }
    }

    // --- String / Misc Operations ---

    override fun encrypt(plaintext: String): ByteArray {
        val aead = keyProvider.getAead()
        val ciphertext = aead.encrypt(plaintext.toByteArray(), providerHeader)
        return providerHeader + ciphertext
    }

    override fun decrypt(ciphertext: ByteArray): String {
        if (ciphertext.size < providerHeader.size) {
            throw GeneralSecurityException("Invalid ciphertext size.")
        }

        val header = ciphertext.copyOfRange(0, providerHeader.size)
        val actualCiphertext = ciphertext.copyOfRange(providerHeader.size, ciphertext.size)

        if (!header.contentEquals(providerHeader)) {
            throw GeneralSecurityException("Invalid provider header.")
        }

        val aead = keyProvider.getAead()
        val plaintext = aead.decrypt(actualCiphertext, providerHeader)
        return String(plaintext)
    }

    override fun rewrapFileKey(fileUri: Uri): Boolean {
        val path = fileUri.path ?: return false
        val file = File(path)
        if (!file.exists() || !file.isFile) return false

        return try {
            val fileBytes = file.readBytes()
            if (fileBytes.size <= providerHeader.size) return false

            val header = fileBytes.copyOfRange(0, providerHeader.size)
            val actualCiphertext = fileBytes.copyOfRange(providerHeader.size, fileBytes.size)

            if (!header.contentEquals(providerHeader)) return false

            if (keyProvider.isSymmetricallyWrapped(actualCiphertext)) {
                return true
            }

            val newCiphertext = keyProvider.rewrapKeyToSymmetricUdr(actualCiphertext)
            file.writeBytes(providerHeader + newCiphertext)
            true
        } catch (e: Exception) {
            Log.e("TinkEncryptionProvider", "Failed to rewrap file: $path", e)
            false
        }
    }

    override fun sweepAndRewrapPendingFiles() {
        val filesDir = context.filesDir
        filesDir.listFiles()?.forEach { file ->
            if (file.isFile) {
                rewrapFileKey(Uri.fromFile(file))
            }
        }
    }

    override fun destroy() {
        keyProvider.destroy()
    }
}
