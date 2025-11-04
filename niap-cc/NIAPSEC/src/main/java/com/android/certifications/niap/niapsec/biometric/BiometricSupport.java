/*
 * Copyright 2020 The Android Open Source Project
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
package com.android.certifications.niap.niapsec.biometric;

import javax.crypto.Cipher;

import com.android.certifications.niap.niapsec.crypto.SecureCipher;

/**
 * Interface to define various events and enums used by BiometricPrompt.
 *
 */
public interface BiometricSupport {

    /**
     * Statuses of biometric authentication
     */
    public enum BiometricStatus {
        SUCCESS(0),
        FAILED(1),
        CANCELLED(2);

        private final int type;

        BiometricStatus(int type) {
            this.type = type;
        }

        public int getType() {
            return this.type;
        }

        public static BiometricStatus fromId(int id) {
            switch (id) {
                case 0:
                    return SUCCESS;
                case 1:
                    return FAILED;
                case 2:
                    return CANCELLED;
            }
            return CANCELLED;
        }
    }

    void onAuthenticationSucceeded();

    void onAuthenticationFailed();

    void onMessage(String message);

    void authenticate(Cipher cipher, SecureCipher.SecureAuthCallback callback);

    void authenticateDeviceCredential(SecureCipher.SecureAuthCallback callback);

}
