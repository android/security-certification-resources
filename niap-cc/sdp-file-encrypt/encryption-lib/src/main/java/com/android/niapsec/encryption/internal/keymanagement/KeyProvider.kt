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

import com.google.crypto.tink.Aead
import com.google.crypto.tink.StreamingAead

/**
 * A common interface for key providers, allowing different underlying key management
 * strategies (hardware-backed, software-only, etc.) to be used interchangeably.
 */
interface KeyProvider {
    /**
     * Retrieves the AEAD primitive for performing cryptographic operations.
     */
    fun getAead(): Aead

    /**
     * Retrieves the StreamingAead primitive for performing streaming cryptographic operations.
     * Returns null if streaming is not supported by this provider.
     */
    fun getStreamingAead(): StreamingAead? = null

    fun getUnlockDeviceRequired(): Boolean

    //new methods for rewrapping with UDR symmetric keys
    fun rewrapKeyToSymmetricUdr(encryptedDek: ByteArray): ByteArray

    fun isSymmetricallyWrapped(encryptedDek: ByteArray): Boolean

    /**
     * Destroys all cryptographic material associated with this provider.
     */
    fun destroy()
}

