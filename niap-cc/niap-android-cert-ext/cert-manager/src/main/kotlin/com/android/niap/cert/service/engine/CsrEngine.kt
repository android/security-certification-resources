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

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.android.niap.cert.manager.CsrSpec
import com.android.niap.cert.manager.KeyType
import com.android.niap.cert.manager.SigAlg
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x509.Extension
import org.bouncycastle.asn1.x509.ExtensionsGenerator
import org.bouncycastle.asn1.x509.GeneralName
import org.bouncycastle.asn1.x509.GeneralNames
import org.bouncycastle.pkcs.PKCS10CertificationRequest
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.spec.ECGenParameterSpec

class CsrEngine : INiapCsrEngine {

    override fun generateCsr(alias: String, subjectDn: String, sans: List<String>, spec: CsrSpec): ByteArray {
        // 1. Generate KeyPair in Android KeyStore based on CsrSpec
        val keyPair = generateKeyPairInKeyStore(alias, spec.keyType)

        // 2. Build Subject DN
        val subject = X500Name(subjectDn)

        // 3. Initialize CSR Builder with Public Key
        val csrBuilder = JcaPKCS10CertificationRequestBuilder(subject, keyPair.public)

        // 4. Add SAN (Subject Alternative Name) Extensions if present
        if (sans.isNotEmpty()) {
            val extensionsGenerator = ExtensionsGenerator()
            val generalNames = sans.map { san ->
                if (android.util.Patterns.IP_ADDRESS.matcher(san).matches()) {
                    GeneralName(GeneralName.iPAddress, san)
                } else {
                    GeneralName(GeneralName.dNSName, san)
                }
            }.toTypedArray()

            val sanSeq = GeneralNames(generalNames)
            extensionsGenerator.addExtension(
                Extension.subjectAlternativeName,
                false,
                sanSeq.toASN1Primitive()
            )
            csrBuilder.addAttribute(
                PKCSObjectIdentifiers.pkcs_9_at_extensionRequest,
                extensionsGenerator.generate()
            )
        }

        // 5. Sign CSR using Private Key from Android KeyStore based on CsrSpec
        val signer = JcaContentSignerBuilder(spec.sigAlg.algorithmName)
            .setProvider("AndroidKeyStoreBCWorkaround")
            .build(keyPair.private)
        val csr: PKCS10CertificationRequest = csrBuilder.build(signer)

        // 6. Return DER encoded CSR
        return csr.encoded
    }

    private fun generateKeyPairInKeyStore(alias: String, keyType: KeyType): java.security.KeyPair {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        if (keyStore.containsAlias(alias)) {
            keyStore.deleteEntry(alias)
        }

        val keyPairGenerator = KeyPairGenerator.getInstance(keyType.keyAlgorithm, "AndroidKeyStore")
        val builder = KeyGenParameterSpec.Builder(
            alias,
            KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
        )

        val paramSpecName = keyType.parameterSpecName
        val size = keyType.keySize
        if (paramSpecName != null) {
            builder.setAlgorithmParameterSpec(ECGenParameterSpec(paramSpecName))
        } else if (size != null) {
            builder.setKeySize(size)
        }

        builder.setDigests(
            KeyProperties.DIGEST_NONE,   // Required: Conscrypt uses NONEwithECDSA for TLS CertificateVerify
            KeyProperties.DIGEST_SHA256,
            KeyProperties.DIGEST_SHA384,
            KeyProperties.DIGEST_SHA512
        )

        keyPairGenerator.initialize(builder.build())
        return keyPairGenerator.generateKeyPair()
    }
}
