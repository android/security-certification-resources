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
import com.android.niapsec.encryption.BuildConfig
import android.util.Log

/**
 * [Security Component: Audit Logger]
 *
 * [isAuditLogEnabled] should be false when the app is released.
 */
object SecurityAuditLogger {
    // toggle switch for audit
    var isAuditLogEnabled = true

    private const val TAG = "AUDIT_LOGGER KMD"

    fun logMaterial(tag: String = TAG, name: String, bytes: ByteArray?) {
        if (!isAuditLogEnabled || bytes == null) return
        Log.d(tag,  bytes.toHexDumpString())
    }

    fun logKeyMaterial(name: String, bytes: ByteArray?) {
        return logKeyMaterial(tag = TAG, name = name, bytes = bytes);
    }
    fun logKeyMaterial(tag: String= TAG, name: String, bytes: ByteArray?) {
        if (!isAuditLogEnabled || bytes== null) {
            Log.d("${name} KMD",  "returns null (error or hardware backend)")
            return
        }
        Log.d("${name} KMD",  bytes.toHexString())
        bytes.fill(0)
    }

    fun logLine(msg: String) {
        logLine(tag = TAG+BuildConfig.BUILD_DATE, msg = msg)
    }
    fun logLine(tag:String,msg: String) {
        Log.d(tag,msg)
    }
}

fun ByteArray.toHexString(): String {
    return joinToString("") { "%02x".format(it) }
}
fun ByteArray.toHexDumpString(): String = buildString {
    for (rowAddr in this@toHexDumpString.indices step 16) {
        val rowEnd = minOf(rowAddr + 16, this@toHexDumpString.size)
        val rowBytes = this@toHexDumpString.sliceArray(rowAddr until rowEnd)

        append("%08x  ".format(rowAddr))

        val hexPart = rowBytes.joinToString(" ") { "%02x".format(it) }
        append(hexPart.padEnd(47)) // Fixed width for 16 bytes

        append("  |")
        rowBytes.forEach {
            val char = it.toInt().toChar()
            append(if (it in 32..126) char else '.')
        }
        append("|\n")
    }
}.trimEnd()