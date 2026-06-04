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

import java.io.ByteArrayOutputStream
import java.security.PrivateKey
import java.security.Provider
import java.security.PublicKey
import java.security.SignatureSpi
import java.security.interfaces.ECKey
import java.security.spec.ECParameterSpec

/**
 * Marker key that carries the alias, EC curve params, and a per-instance
 * signing function. The private key material itself never leaves the
 * cert-manager service; this object is only a routing handle for JCA.
 *
 * Conscrypt requires the key to implement [ECKey] so it can derive the TLS
 * signing algorithm (ecdsa_secp384r1_sha384 etc.) from the curve.
 *
 * The [signingFn] is invoked by [RemoteSignatureSpi.engineSign] with the
 * pre-hashed digest bytes; implementations typically delegate to the
 * cert-manager service over AIDL.
 */
internal class RemotePrivateKey(
    val alias: String,
    private val params: ECParameterSpec,
    val signingFn: (ByteArray) -> ByteArray
) : PrivateKey, ECKey {
    override fun getAlgorithm() = "EC"
    override fun getFormat() = null
    override fun getEncoded() = null   // key material stays in the oracle
    override fun getParams() = params
}

/**
 * [SignatureSpi] for "NONEwithECDSA". Buffers update bytes during the TLS
 * handshake and invokes the [RemotePrivateKey.signingFn] captured in
 * [engineInitSign] on [engineSign]. No thread-local state; each Signature
 * instance is bound to one [RemotePrivateKey], so concurrent handshakes
 * are independent.
 */
class RemoteSignatureSpi : SignatureSpi() {
    private val buffer = ByteArrayOutputStream()
    private var signingFn: ((ByteArray) -> ByteArray)? = null

    override fun engineInitSign(privateKey: PrivateKey?) {
        signingFn = (privateKey as? RemotePrivateKey)?.signingFn
            ?: throw java.security.InvalidKeyException(
                "RemoteSignatureSpi requires a RemotePrivateKey; got ${privateKey?.javaClass?.name}"
            )
        buffer.reset()
    }

    override fun engineInitVerify(publicKey: PublicKey?) {}
    override fun engineUpdate(b: Byte) { buffer.write(b.toInt()) }
    override fun engineUpdate(b: ByteArray, off: Int, len: Int) { buffer.write(b, off, len) }

    override fun engineSign(): ByteArray {
        val fn = signingFn
            ?: throw java.security.SignatureException("engineSign called before engineInitSign")
        return fn(buffer.toByteArray()).also { buffer.reset() }
    }

    override fun engineVerify(sigBytes: ByteArray?) = false
    override fun engineSetParameter(param: String?, value: Any?) {}
    @Suppress("OVERRIDE_DEPRECATION")
    override fun engineGetParameter(param: String?) = null
}

/**
 * JCA [Provider] registered once at library init. Handles "NONEwithECDSA"
 * only for [RemotePrivateKey] instances via [Service.supportsParameter];
 * all other keys fall through to the next provider in the chain.
 */
internal class RemoteSigningProvider : Provider("NiapRemoteSigning", 1.0, "") {
    init {
        val svc = object : Service(
            this, "Signature", "NONEwithECDSA",
            RemoteSignatureSpi::class.java.name, null, null
        ) {
            override fun supportsParameter(key: Any?) = key is RemotePrivateKey
        }
        putService(svc)
    }
}
