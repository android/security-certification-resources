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

package com.android.niapsec.encryption.tools
import java.io.ByteArrayOutputStream
import java.security.MessageDigest
import kotlin.math.ceil

object SafeHkdf {
    private const val SHA256_BLOCK_SIZE = 64
    private const val SHA256_HASH_SIZE = 32

    /**
     * Computes HMAC-SHA256 using MessageDigest directly, without relying on JNI Mac.
     */
    private fun hmacSha256(key: ByteArray, data: ByteArray): ByteArray {
        val md = MessageDigest.getInstance("SHA-256")
        val paddedKey = ByteArray(SHA256_BLOCK_SIZE)

        // Key padding based on HMAC specification
        if (key.size > SHA256_BLOCK_SIZE) {
            val hashedKey = md.digest(key)
            System.arraycopy(hashedKey, 0, paddedKey, 0, hashedKey.size)
            hashedKey.fill(0) // Immediately clear the intermediate buffer
        } else {
            System.arraycopy(key, 0, paddedKey, 0, key.size)
        }

        val oPad = ByteArray(SHA256_BLOCK_SIZE)
        val iPad = ByteArray(SHA256_BLOCK_SIZE)

        for (i in 0 until SHA256_BLOCK_SIZE) {
            oPad[i] = (paddedKey[i].toInt() xor 0x5c).toByte()
            iPad[i] = (paddedKey[i].toInt() xor 0x36).toByte()
        }

        // Inner hash: H(iPad || data)
        md.reset()
        md.update(iPad)
        md.update(data)
        val innerHash = md.digest()

        // Outer hash: H(oPad || innerHash)
        md.reset()
        md.update(oPad)
        md.update(innerHash)
        val mac = md.digest()

        // [CRITICAL] Ensure all used buffers are zero-cleared
        paddedKey.fill(0)
        oPad.fill(0)
        iPad.fill(0)
        innerHash.fill(0)

        return mac
    }

    /**
     * A function completely compatible with Tink's Hkdf.computeHkdf
     */
    fun computeHkdf(ikm: ByteArray, salt: ByteArray?, info: ByteArray, length: Int): ByteArray {
        val actualSalt = if (salt == null || salt.isEmpty()) ByteArray(SHA256_HASH_SIZE) else salt

        // 1. HKDF-Extract
        val prk = hmacSha256(actualSalt, ikm)

        // 2. HKDF-Expand
        val result = ByteArrayOutputStream()
        var t = ByteArray(0)
        val iterations = ceil(length.toDouble() / SHA256_HASH_SIZE).toInt()

        require(iterations <= 255) { "Requested length is too large" }

        for (i in 1..iterations) {
            val input = ByteArray(t.size + info.size + 1)
            System.arraycopy(t, 0, input, 0, t.size)
            System.arraycopy(info, 0, input, t.size, info.size)
            input[input.size - 1] = i.toByte()

            if (t.isNotEmpty()) t.fill(0) // Clear previous T

            t = hmacSha256(prk, input)
            result.write(t)

            input.fill(0) // Clear temporary loop buffer
        }

        val finalResult = result.toByteArray().copyOf(length)

        // [CRITICAL] Zero out PRK and the final T
        prk.fill(0)
        t.fill(0)

        // Note: The actual ikm (sharedSecret) is expected to be cleared
        // in the finally block of the caller (RawHybridKeyProvider).

        return finalResult
    }
}