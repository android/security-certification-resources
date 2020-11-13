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

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import com.android.certifications.niap.permissions.BasePermissionTester;
import com.android.certifications.niap.permissions.R;
import com.android.certifications.niap.permissions.SignaturePermissionTester;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Configuration designed to test signature permissions that are dependent on other permissions. In
 * this case the signature permission under test is RADIO_SCAN_WITHOUT_LOCATION, intended to allow a
 * radio scan to be performed without a location permission granted.
 */
public class SignatureDependentPermissionConfiguration implements TestConfiguration {
    private Activity mActivity;

    private static final String[] DEPENDENT_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    public SignatureDependentPermissionConfiguration(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void preRunSetup() throws BypassConfigException {
        if (areDependentPermissionsGranted(mActivity)) {
            throw new BypassConfigException(
                    mActivity.getResources().getString(R.string.permissions_must_not_be_granted,
                            String.join(", ", DEPENDENT_PERMISSIONS)));
        }
    }

    /**
     * Returns whether the dependent permissions have been granted; in order to run the test in this
     * configuration these permissions must not be granted.
     */
    public static boolean areDependentPermissionsGranted(Activity activity) {
        for (String permission : DEPENDENT_PERMISSIONS) {
            if (activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<BasePermissionTester> getPermissionTesters(Activity activity) {
        return Collections.singletonList(new SignaturePermissionTester(this, activity));
    }

    @Override
    public Optional<List<String>> getSignaturePermissions() {
        return Optional.of(Collections.singletonList(permission.RADIO_SCAN_WITHOUT_LOCATION));
    }

    @Override
    public int getButtonTextId() {
        return R.string.run_radio_scan_without_location_test;
    }
}
