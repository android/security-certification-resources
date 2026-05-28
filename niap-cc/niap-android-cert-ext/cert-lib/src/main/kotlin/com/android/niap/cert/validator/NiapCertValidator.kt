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

package com.android.niap.cert.validator

import android.util.Log
import java.security.cert.X509Certificate
import java.security.interfaces.ECPublicKey
import java.security.interfaces.RSAPublicKey

import java.security.cert.CertificateException
import java.security.cert.CertificateParsingException
import java.security.cert.CertPathValidator
import java.security.cert.PKIXParameters
import java.security.cert.PKIXRevocationChecker
import java.security.cert.CertificateFactory
import java.security.cert.CertPath
import java.util.EnumSet
import javax.net.ssl.SSLSession
import javax.net.ssl.HttpsURLConnection
import java.security.KeyStore
import java.security.cert.CertPathValidatorException
import java.security.GeneralSecurityException

class NiapCertValidator(
    private val strictSigAlg: Boolean = true,
    private val prohibitedTldWildcards: Set<String> = setOf("*.COM", "*.NET", "*.ORG"),
    private val enforceCaConstraints: Boolean = true,
    private val enforceMandatoryExtensions: Boolean = true,
    private val enforceEku: Boolean = true,
    private val requiredEkus: List<String> = listOf("serverAuth")
) {

    fun validateCertificate(chain: Array<out X509Certificate>) {
        if (chain.isEmpty()) {
            throw IllegalArgumentException("Certificate chain is empty")
        }
        
        val leafCert = chain[0]
        
        // 1. Algorithm Constraints (RFC 8603 CNSA)


        checkAlgorithmConstraints(leafCert)
        
        // 2. Mandatory Extension Checks


        checkMandatoryExtensions(leafCert)
        
        // 3. Field Enforcement (Basic Constraints, EKU, Name Constraints)


        checkFieldEnforcement(chain)
        
        // 4. TLD Wildcard Check (Carried over from SecureURL)


        checkTldWildcards(leafCert)
        
        // 5. Revocation Checks (OCSP / CRL)


        checkRevocation(leafCert)
    }

    fun validateUrl(urlString: String) {
        // Enforce HTTPS (blocking non-secure connections)
        if (urlString.startsWith("http://", ignoreCase = true)) {
            throw CertificateException("Insecure HTTP connections are blocked: $urlString")
        }
        
        // If no protocol, assume https
        val url = if (!urlString.contains("://")) {
            "https://$urlString"
        } else {
            urlString
        }
        // Hostname validation will be done by Conscrypt or OkHttp
    }

    private fun checkTldWildcards(cert: X509Certificate) {
        try {
            val subAltNames = cert.subjectAlternativeNames
            if (subAltNames != null) {
                for (tldList in subAltNames) {
                    if (tldList.size >= 2) {
                        val dnsName = tldList[1].toString().uppercase()
                        if (prohibitedTldWildcards.contains(dnsName)) {
                            throw CertificateException("Failed wildcard TLD check: $dnsName")
                        }
                    }
                }
            }
        } catch (e: CertificateParsingException) {
             throw CertificateException("Failed to parse SANs", e)
        }
    }

    private fun checkAlgorithmConstraints(cert: X509Certificate) {
       
        if (!strictSigAlg) return
        
        val sigAlgName = cert.sigAlgName
        val pubKey = cert.publicKey
        
        // Enforce SHA-384 or SHA-512 with ECDSA or RSA (RFC 8603)
        if (!sigAlgName.contains("SHA384", ignoreCase = true) && 
            !sigAlgName.contains("SHA512", ignoreCase = true)) {
            throw CertificateException("Signature algorithm must use SHA-384 or SHA-512: $sigAlgName")
        }
        
        if (pubKey is RSAPublicKey) {
            if (pubKey.modulus.bitLength() < 3072) {
                throw CertificateException("RSA key size must be 3072 bits or greater")
            }
        } else if (pubKey is ECPublicKey) {
            // Check field size (P-384 has field size 384)
            if (pubKey.params.curve.field.fieldSize < 384) {
                 throw CertificateException("EC key size must be 384 bits or greater")
            }
        } else {
            throw CertificateException("Unsupported public key type: ${pubKey.algorithm}")
        }
    }

    private fun checkMandatoryExtensions(cert: X509Certificate) {
        if (!enforceMandatoryExtensions) return
        
        val nonCriticalOids = cert.nonCriticalExtensionOIDs ?: emptySet()
        val criticalOids = cert.criticalExtensionOIDs ?: emptySet()
        val allOids = nonCriticalOids + criticalOids
        
        // OIDs referenced from FIA_X509_EXT.1.2 in MDFPP (Mobile Device Fundamentals Protection Profile)
        val mandatoryOids = listOf(
            "2.5.29.35", // Authority Key Identifier. Required by FIA_X509_EXT.1.2
            "2.5.29.14", // Subject Key Identifier. Required by FIA_X509_EXT.1.2
            "2.5.29.15"  // Key Usage. Required by FIA_X509_EXT.1.2
        )
        
        for (oid in mandatoryOids) {
            if (!allOids.contains(oid)) {
                throw CertificateException("Mandatory extension missing: $oid (Required by FIA_X509_EXT.1.2)")
            }
        }
    }

    private fun checkFieldEnforcement(chain: Array<out X509Certificate>) {
        val leafCert = chain[0]
        android.util.Log.d("NiapCertValidator", "DEBUG checkFieldEnforcement: enforceEku=$enforceEku eku=${leafCert.extendedKeyUsage}")
        
        // 1. Extended Key Usage (EKU) Check
        // Required by FIA_X509_EXT.1.1 or related evaluation activities in MDFPP.
        if (enforceEku) {
            val eku = leafCert.extendedKeyUsage
            if (eku != null) {
                for (reqEku in requiredEkus) {
                    val targetOid = mapEkuNameToOid(reqEku)
                    if (!eku.contains(targetOid)) {
                        throw CertificateException("Leaf certificate missing required EKU: $reqEku ($targetOid)")
                    }
                }
            } else {
                throw CertificateException("Leaf certificate missing Extended Key Usage extension")
            }
        }
        
        // Basic Constraints Check
        if (enforceCaConstraints) {
            val basicConstraints = leafCert.basicConstraints
            if (basicConstraints >= 0) {
                throw CertificateException("Leaf certificate must not be a CA (Enforced by FIA_X509_EXT.1.2 / Basic Constraints)")
            }
        }
    }

    private fun mapEkuNameToOid(name: String): String {
        return when (name.lowercase()) {
            "serverauth" -> "1.3.6.1.5.5.7.3.1"
            "clientauth" -> "1.3.6.1.5.5.7.3.2"
            "codesigning" -> "1.3.6.1.5.5.7.3.3"
            "emailprotection" -> "1.3.6.1.5.5.7.3.4"
            "timestamping" -> "1.3.6.1.5.5.7.3.8"
            "ocspsigning" -> "1.3.6.1.5.5.7.3.9"
            else -> name // Assume it's already an OID if not recognized
        }
    }

    private fun checkRevocation(cert: X509Certificate) {
        // Enforce OCSP Stapling or fallback to Out-of-Band CRL
        // TODO: Implement revocation check
    }

    fun checkCertPath(chain: Array<out X509Certificate>) {
        val leafCerts = chain.filter { !isRootCA(it) }
        if (leafCerts.isEmpty()) {
            throw CertificateException("No non-root certificates found in chain")
        }
        val certFactory = CertificateFactory.getInstance("X.509")
        val path = certFactory.generateCertPath(leafCerts)
        
        val ks = KeyStore.getInstance("AndroidCAStore")
        ks.load(null, null)
        
        val cpv = CertPathValidator.getInstance("PKIX")
        val params = PKIXParameters(ks)
        params.isRevocationEnabled = true
        
        val checker = cpv.revocationChecker as PKIXRevocationChecker
        // Android 9以降ではフォールバックが発生するとエラーになるため、NO_FALLBACKを強制する
        checker.options = EnumSet.of(PKIXRevocationChecker.Option.NO_FALLBACK)
        params.addCertPathChecker(checker)
        
        try {
            cpv.validate(path, params)
        } catch (e: CertPathValidatorException) {
            throw CertificateException("CertPath validation failed", e)
        } catch (e: GeneralSecurityException) {
            throw CertificateException("Security exception during CertPath validation", e)
        }
    }

    fun checkHostname(hostname: String, session: SSLSession) {
        val hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier()
        if (!hostnameVerifier.verify(hostname, session)) {
            throw CertificateException("Hostname verification failed for $hostname")
        }
    }

    private fun isRootCA(cert: X509Certificate): Boolean {
        return cert.subjectDN.name == cert.issuerDN.name
    }
}
