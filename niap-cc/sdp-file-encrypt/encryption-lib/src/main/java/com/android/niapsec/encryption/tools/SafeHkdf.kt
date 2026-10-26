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
    // FIPS 180-4 / FIPS 198-1: SHA-512 uses a 1024-bit (128-byte) block and a 512-bit (64-byte) output.
    private const val SHA512_BLOCK_SIZE = 128
    private const val SHA512_HASH_SIZE = 64

    /**
     * Computes HMAC-SHA512 using MessageDigest directly, without relying on JNI Mac.
     */
    private fun hmacSha512(key: ByteArray, data: ByteArray): ByteArray {
        val md = MessageDigest.getInstance("SHA-512")
        val paddedKey = ByteArray(SHA512_BLOCK_SIZE)

        // Key padding based on HMAC specification
        if (key.size > SHA512_BLOCK_SIZE) {
            val hashedKey = md.digest(key)
            System.arraycopy(hashedKey, 0, paddedKey, 0, hashedKey.size)
            hashedKey.fill(0) // Immediately clear the intermediate buffer
        } else {
            System.arraycopy(key, 0, paddedKey, 0, key.size)
        }

        val oPad = ByteArray(SHA512_BLOCK_SIZE)
        val iPad = ByteArray(SHA512_BLOCK_SIZE)

        for (i in 0 until SHA512_BLOCK_SIZE) {
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
     * A function completely compatible with Tink's Hkdf.computeHkdf (using HMAC-SHA512).
     *
     * The PRK from HKDF-Extract is the full, untruncated 64-byte HMAC-SHA512 output.
     * Only the final output keying material is truncated to [length] bytes (e.g., 32 bytes for AES-256).
     */
    fun computeHkdf(ikm: ByteArray, salt: ByteArray?, info: ByteArray, length: Int): ByteArray {
        val actualSalt = if (salt == null || salt.isEmpty()) ByteArray(SHA512_HASH_SIZE) else salt

        // 1. HKDF-Extract
        val prk = hmacSha512(actualSalt, ikm)

        // 2. HKDF-Expand
        val result = ByteArrayOutputStream()
        var t = ByteArray(0)
        val iterations = ceil(length.toDouble() / SHA512_HASH_SIZE).toInt()

        require(iterations <= 255) { "Requested length is too large" }

        for (i in 1..iterations) {
            val input = ByteArray(t.size + info.size + 1)
            System.arraycopy(t, 0, input, 0, t.size)
            System.arraycopy(info, 0, input, t.size, info.size)
            input[input.size - 1] = i.toByte()

            if (t.isNotEmpty()) t.fill(0) // Clear previous T

            t = hmacSha512(prk, input)
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