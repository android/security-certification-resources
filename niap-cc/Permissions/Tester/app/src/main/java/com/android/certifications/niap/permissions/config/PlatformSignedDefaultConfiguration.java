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

package com.android.certifications.niap.permissions.config;

import static com.android.certifications.niap.permissions.utils.SignaturePermissions.permission;

import com.android.certifications.niap.permissions.utils.InternalPermissions;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * Configuration designed to perform permission tests with an app signed by the platform's signing
 * key. This configuration is intended to skip any tests that could cause the app to be
 * interrupted, or that are expected to fail due to the permissions granted to the platform signed
 * app.
 */
class PlatformSignedDefaultConfiguration implements TestConfiguration {
    @Override
    public Optional<Set<String>> getSkippedSignaturePermissions() {
        return Optional.of(Collections.singleton(permission.MANAGE_BIOMETRIC_DIALOG));
    }

    @Override
    public Optional<Set<String>> getSkippedInternalPermissions() {
        // The DomainVerificationEnforcer will also check for the INTENT_FILTER_VERIFICATION_AGENT
        // permission if this internal permission is not granted.
        return Optional.of(
                Collections.singleton(InternalPermissions.permission.DOMAIN_VERIFICATION_AGENT));
    }
}
