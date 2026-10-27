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

/**
 * Asynchronous result channel for INiapCertManager.requestCertificate.
 *
 * The service invokes exactly one of onSuccess / onError per enrollment.
 * If the service dies before either is called, the caller-side
 * IBinder.linkToDeath() hook synthesises an onError.
 */
oneway interface IEnrollmentCallback {
    /** Enrollment completed: certificate is stored and ready to use. */
    void onSuccess(in byte[] certificateData);

    /** Enrollment failed: [errorMessage] is human-readable diagnostic text. */
    void onError(String errorMessage);
}
