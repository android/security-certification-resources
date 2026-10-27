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

import android.os.IInterface

enum class KeyType(val keyAlgorithm: String, val parameterSpecName: String?, val keySize: Int?) {
    EC_P384(android.security.keystore.KeyProperties.KEY_ALGORITHM_EC, "secp384r1", null),
    EC_P256(android.security.keystore.KeyProperties.KEY_ALGORITHM_EC, "secp256r1", null),
    RSA_3072(android.security.keystore.KeyProperties.KEY_ALGORITHM_RSA, null, 3072),
    RSA_2048(android.security.keystore.KeyProperties.KEY_ALGORITHM_RSA, null, 2048)
}

enum class SigAlg(val algorithmName: String) {
    SHA384_ECDSA("SHA384withECDSA"),
    SHA256_ECDSA("SHA256withECDSA"),
    SHA384_RSA("SHA384withRSA"),
    SHA256_RSA("SHA256withRSA")
}

data class CsrSpec(
    val keyType: KeyType = KeyType.EC_P384,
    val sigAlg: SigAlg = SigAlg.SHA384_ECDSA
)

data class ValidatorConfig(
    val strictSigAlg: Boolean = true,
    val enforceCaConstraints: Boolean = true,
    val enforceMandatoryExtensions: Boolean = true,
    val enforceEku: Boolean = true
)

data class EnrollmentRequest(
    val alias: String,
    val estServerUrl: String,
    val authToken: String,
    val subjectDn: String,
    val sans: List<String> = emptyList(),
    val csrSpec: CsrSpec = CsrSpec(),
    val trustedCaPem: String = "",
    val validatorConfig: ValidatorConfig = ValidatorConfig()
)

/** Thrown via the enroll() future on enrollment failure (network, validation, etc.). */
class EnrollmentException(message: String) : Exception(message)

/** Thrown via the revoke() future on revocation failure. */
class RevocationException(message: String) : Exception(message)
