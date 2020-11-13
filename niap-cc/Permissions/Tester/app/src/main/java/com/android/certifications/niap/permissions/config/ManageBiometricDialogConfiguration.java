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

import android.app.Activity;

import com.android.certifications.niap.permissions.BasePermissionTester;
import com.android.certifications.niap.permissions.R;
import com.android.certifications.niap.permissions.SignaturePermissionTester;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Configuration designed to run the signature permission test for MANAGE_BIOMETRIC_DIALOG. When
 * invoked by an app signed with the platform signing key this test can cause the SystemUI to crash;
 * to prevent interrupting the rest of the tests this configuration allows this test to be run
 * standalone.
 */
class ManageBiometricDialogConfiguration implements TestConfiguration {
    @Override
    public List<BasePermissionTester> getPermissionTesters(Activity activity) {
        List<BasePermissionTester> permissionTesters = new ArrayList<>();
        permissionTesters.add(new SignaturePermissionTester(this, activity));
        return permissionTesters;
    }

    @Override
    public Optional<List<String>> getSignaturePermissions() {
        return Optional.of(Collections.singletonList(permission.MANAGE_BIOMETRIC_DIALOG));
    }

    @Override
    public int getButtonTextId() {
        return R.string.run_manage_biometric_dialog_test;
    }
}
