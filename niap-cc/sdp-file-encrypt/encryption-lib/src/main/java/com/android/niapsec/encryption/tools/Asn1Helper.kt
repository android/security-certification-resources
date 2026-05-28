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

/**
 * Lightweight helper to construct DER-encoded ASN.1 structures.
 * Supports only basic types required for `WrappedKeyEntry` (SEQUENCE, OCTET_STRING, INTEGER).
 */
object Asn1Helper {

    private const val TAG_INTEGER = 0x02.toByte()
    private const val TAG_OCTET_STRING = 0x04.toByte()
    private const val TAG_SEQUENCE = 0x30.toByte()

    fun explicitTag(tagNum: Int, content: ByteArray): ByteArray {
        val tagByte = (0xA0 or (tagNum and 0x1F)).toByte()
        return encode(tagByte, content)
    }

    fun set(vararg elements: ByteArray): ByteArray {
        val bos = ByteArrayOutputStream()
        for (element in elements) {
            bos.write(element)
        }
        return encode(0x31.toByte(), bos.toByteArray())
    }

    fun sequence(vararg elements: ByteArray): ByteArray {
        val bos = ByteArrayOutputStream()
        for (element in elements) {
            bos.write(element)
        }
        return encode(TAG_SEQUENCE, bos.toByteArray())
    }

    fun octetString(content: ByteArray): ByteArray {
        return encode(TAG_OCTET_STRING, content)
    }

    fun integer(value: Int): ByteArray {
        val bos = ByteArrayOutputStream()
        bos.write((value shr 24) and 0xFF)
        bos.write((value shr 16) and 0xFF)
        bos.write((value shr 8) and 0xFF)
        bos.write(value and 0xFF)
        var bytes = bos.toByteArray()
        
        // Trim leading zeros if it's signed (0 padding needs to be careful if it's negative, but for simple use cases it's fine)
        var start = 0
        while (start < bytes.size - 1 && bytes[start] == 0.toByte()) {
            start++
        }
        if (start > 0) {
            bytes = bytes.copyOfRange(start, bytes.size)
        }
        return encode(TAG_INTEGER, bytes)
    }

    private fun encode(tag: Byte, content: ByteArray): ByteArray {
        val bos = ByteArrayOutputStream()
        bos.write(tag.toInt())
        writeLength(bos, content.size)
        bos.write(content)
        return bos.toByteArray()
    }

    private fun writeLength(bos: ByteArrayOutputStream, length: Int) {
        if (length < 128) {
            bos.write(length)
        } else {
            val lengthBos = ByteArrayOutputStream()
            var temp = length
            while (temp > 0) {
                lengthBos.write(temp and 0xFF)
                temp = temp ushr 8
            }
            val lengthBytes = lengthBos.toByteArray().reversedArray()
            bos.write(0x80 or lengthBytes.size)
            bos.write(lengthBytes)
        }
    }
}
