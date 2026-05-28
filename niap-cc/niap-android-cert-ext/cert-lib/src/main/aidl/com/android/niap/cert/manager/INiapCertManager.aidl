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

package com.android.niap.cert.manager;

import android.os.Bundle;
import com.android.niap.cert.manager.IEnrollmentCallback;
import com.android.niap.cert.manager.IRevocationCallback;

interface INiapCertManager {
    /**
     * Async enrollment with callback. Service invokes exactly one of
     * callback.onSuccess(certData) or callback.onError(message).
     * Replaces the old polling-based requestCertificate.
     */
    void enroll(
        String alias,
        String estServerUrl,
        String authToken,
        String subjectDn,
        in List<String> sans,
        String keyType,
        String sigAlg,
        String trustedCaPem,
        in Bundle validatorConfig,
        IEnrollmentCallback callback
    );

    /** Async revocation with callback. */
    void revoke(String alias, IRevocationCallback callback);

    /** Whether an enrollment for [alias] exists in the service KeyStore. */
    boolean hasEnrollment(String alias);

    /** Returns DER-encoded leaf certificate for [alias], or empty if none. */
    byte[] getCertificateData(String alias);

    /**
     * Signs pre-hashed digest bytes with the key bound to alias (NONEwithECDSA).
     * The service acts as a signing oracle; callers own the TLS/HTTP flow.
     */
    byte[] sign(String alias, in byte[] digestBytes);
}
