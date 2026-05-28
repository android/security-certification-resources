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

import android.net.Uri
import java.io.File
import java.io.InputStream
import java.io.OutputStream

/**
 * An interface for providing encryption and decryption operations.
 */
interface EncryptionProvider {

    /**
     * Encrypts a file using in-memory processing (Aead).
     * Best for small files.
     */
    fun encrypt(file: File): OutputStream

    /**
     * Encrypts a file using streaming processing (StreamingAead).
     * Best for large files. Throws exception if not supported by the provider.
     */
    fun encryptStream(file: File): OutputStream

    /**
     * Encrypts a plaintext string into a ciphertext byte array.
     */
    fun encrypt(plaintext: String): ByteArray

    /**
     * Decrypts a file using in-memory processing (Aead).
     */
    fun decrypt(file: File): InputStream

    /**
     * Decrypts a file using streaming processing (StreamingAead).
     * Throws exception if not supported by the provider.
     */
    fun decryptStream(file: File): InputStream

    /**
     * Decrypts a ciphertext byte array into a plaintext string.
     */
    fun decrypt(ciphertext: ByteArray): String

    //new methods for rewrap with symmetric keys
    fun rewrapFileKey(fileUri: Uri): Boolean

    fun sweepAndRewrapPendingFiles()

    /**
     * Destroys any cryptographic material associated with this provider.
     */
    fun destroy()
}