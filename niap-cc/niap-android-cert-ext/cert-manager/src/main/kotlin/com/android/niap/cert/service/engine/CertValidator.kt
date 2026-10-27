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
import com.android.niap.cert.validator.NiapCertValidator
import java.io.ByteArrayInputStream
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

import com.android.niap.cert.manager.ValidatorConfig

class CertValidator {

    var lastError: String? = null

    fun validate(certData: ByteArray, config: ValidatorConfig): Boolean {
        lastError = null
        try {
            Log.d("CertValidator", "Validating received certificate data (${certData.size} bytes)")
            val factory = CertificateFactory.getInstance("X.509")
            
            // Try to Base64 decode the response if it is Base64 encoded, otherwise use raw bytes.
            val decodedData = try {
                android.util.Base64.decode(certData, android.util.Base64.DEFAULT)
            } catch (e: Exception) {
                certData
            }

            // PKCS#7 responses contain multiple certificates in a CMS signed data block.
            // We must parse all certs in the chain using generateCertificates.
            val certificates = factory.generateCertificates(ByteArrayInputStream(decodedData))
            val certsChain = certificates.filterIsInstance<X509Certificate>().toTypedArray()
            
            if (certsChain.isEmpty()) {
                throw java.security.cert.CertificateException("No certificates found in PKCS#7 response")
            }

            Log.d("CertValidator", "Parsed chain of ${certsChain.size} certificates, initiating validation")
            val validator = NiapCertValidator(
                strictSigAlg = config.strictSigAlg,
                enforceCaConstraints = config.enforceCaConstraints,
                enforceMandatoryExtensions = config.enforceMandatoryExtensions,
                enforceEku = config.enforceEku,
                requiredEkus = listOf("clientAuth")
            )
            validator.validateCertificate(certsChain)
            Log.d("CertValidator", "Certificate validation passed successfully")
            return true
        } catch (e: Exception) {
            lastError = e.message ?: e.javaClass.simpleName
            Log.e("CertValidator", "Certificate validation failed: ${e.message}")
            return false
        }
    }
}
