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

package com.android.niap.cert.service.engine

import android.util.Log
import java.io.ByteArrayInputStream
import java.security.KeyStore
import java.security.PrivateKey
import java.security.Signature
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

class KeyStoreEngine {

    private val keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }

    fun storeCertificate(alias: String, certData: ByteArray): Boolean {
        try {
            Log.d("KeyStoreEngine", "Storing certificate chain for alias: $alias")
            val factory = CertificateFactory.getInstance("X.509")
            
            // Try to Base64 decode the response if it is Base64 encoded, otherwise use raw bytes.
            val decodedData = try {
                android.util.Base64.decode(certData, android.util.Base64.DEFAULT)
            } catch (e: Exception) {
                certData
            }

            // Parse all certs in the chain from the PKCS#7 container
            val certificates = factory.generateCertificates(ByteArrayInputStream(decodedData))
            val certsChain = certificates.filterIsInstance<X509Certificate>().toTypedArray()

            if (certsChain.isEmpty()) {
                throw java.security.cert.CertificateException("No certificates found in PKCS#7 response to store")
            }

            if (keyStore.containsAlias(alias)) {
                val entry = keyStore.getEntry(alias, null)
                if (entry is KeyStore.PrivateKeyEntry) {
                    // Update the hardware-backed key entry with the complete X.509 chain
                    val privateKey: PrivateKey = entry.privateKey
                    keyStore.setKeyEntry(alias, privateKey, null, certsChain)
                    Log.d("KeyStoreEngine", "Successfully updated hardware key chain with ${certsChain.size} certificates for $alias")
                    return true
                }
            }

            // Fallback: store the leaf certificate
            keyStore.setCertificateEntry(alias, certsChain[0])
            Log.d("KeyStoreEngine", "Successfully stored certificate entry for $alias")
            return true
        } catch (e: Exception) {
            Log.e("KeyStoreEngine", "Failed to store certificate in Android KeyStore", e)
            return false
        }
    }

    fun deleteCertificate(alias: String): Boolean {
        try {
            if (keyStore.containsAlias(alias)) {
                keyStore.deleteEntry(alias)
                Log.d("KeyStoreEngine", "Deleted KeyStore entry for $alias")
            }
            return true
        } catch (e: Exception) {
            Log.e("KeyStoreEngine", "Failed to delete KeyStore entry", e)
            return false
        }
    }

    fun getCertificateData(alias: String): ByteArray {
        try {
            if (keyStore.containsAlias(alias)) {
                val cert = keyStore.getCertificate(alias)
                if (cert != null) {
                    return cert.encoded
                }
            }
        } catch (e: Exception) {
            Log.e("KeyStoreEngine", "Failed to get certificate data from KeyStore", e)
        }
        return ByteArray(0)
    }

    /**
     * Signs [digestBytes] with the AndroidKeyStore key bound to [alias] using NONEwithECDSA.
     * Conscrypt passes a pre-computed digest for TLS CertificateVerify; the key must have
     * DIGEST_NONE in its permitted digests (see CsrEngine).
     */
    fun sign(alias: String, digestBytes: ByteArray): ByteArray {
        val key = keyStore.getKey(alias, null) as? PrivateKey
            ?: throw IllegalStateException("No private key found for alias '$alias'")
        return Signature.getInstance("NONEwithECDSA").run {
            initSign(key)
            update(digestBytes)
            sign()
        }
    }
}
