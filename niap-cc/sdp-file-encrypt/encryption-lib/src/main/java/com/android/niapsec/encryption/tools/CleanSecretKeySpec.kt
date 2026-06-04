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

import java.security.spec.KeySpec
import javax.crypto.SecretKey

/**
 * Custom SecretKeySpec class that implements the Destroyable interface.
 * Default SecretKeySpec has destroy() interface derived from SecretKey, but not implemented.
 */
class CleanSecretKeySpec(
    key: ByteArray,
    private val algorithm: String
) : KeySpec, SecretKey{

    private val keyMaterial: ByteArray = key.clone()
    private var isDestroyedFlag: Boolean = false

    override fun getAlgorithm(): String = algorithm

    override fun getFormat(): String = "RAW"

    override fun getEncoded(): ByteArray? {
        if (isDestroyedFlag) {
            throw IllegalStateException("This key has already been destroyed.")
        }
        return keyMaterial
    }

    override fun destroy() {
        if (!isDestroyedFlag) {
            keyMaterial.fill(0)
            isDestroyedFlag = true
        }
    }

    override fun isDestroyed(): Boolean = isDestroyedFlag
}