/*
 * Copyright 2026 The Android Open Source Project
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

package com.android.niap.cert.manager

import java.security.cert.X509Certificate

/**
 * Abstraction over key storage backends for mTLS client authentication.
 *
 * Implementations:
 *  - Cert-manager backed (service-resident keys, signing via AIDL) — the default,
 *    wired through [NiapCertManager.getKeyManager] and [NiapKeyManager].
 *  - [KeyChainMtlsKeyProvider]: system KeyChain (keys accessible cross-process).
 *    Not yet implemented — kept as a stub for a future installToKeyChain() flow.
 */
interface MtlsKeyProvider {
    fun getCertificateChain(alias: String): Array<X509Certificate>?
    /** Sign pre-hashed digest bytes; algorithm is NONEwithECDSA (Conscrypt convention). */
    fun sign(alias: String, digestBytes: ByteArray): ByteArray
}

/**
 * KeyChain-backed provider: the key lives in the system credential store and is
 * accessible cross-process, so signing runs directly in the caller's process.
 *
 * TODO: implement once KeyChain enrollment flow is added.
 */
class KeyChainMtlsKeyProvider(private val context: android.content.Context) : MtlsKeyProvider {
    override fun getCertificateChain(alias: String): Array<X509Certificate>? =
        android.security.KeyChain.getCertificateChain(context, alias)

    override fun sign(alias: String, digestBytes: ByteArray): ByteArray {
        throw UnsupportedOperationException("KeyChain signing not yet implemented")
    }
}
