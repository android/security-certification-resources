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

import android.app.Activity;

import com.android.certifications.niap.permissions.BasePermissionTester;
import com.android.certifications.niap.permissions.GmsPermissionTester;
import com.android.certifications.niap.permissions.R;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.Collections;
import java.util.List;

/**
 * Configuration to verify the Google Play Services {GMS} client side libraries properly guard their
 * APIs behind platform permissions.
 */
class GmsPermissionConfiguration implements TestConfiguration {
    private final Activity mActivity;

    public GmsPermissionConfiguration(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void preRunSetup() throws BypassConfigException {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        if (apiAvailability.isGooglePlayServicesAvailable(mActivity) != ConnectionResult.SUCCESS) {
            throw new BypassConfigException(
                    mActivity.getResources().getString(R.string.gms_not_available));
        }
    }

    @Override
    public List<BasePermissionTester> getPermissionTesters(Activity activity) {
        return Collections.singletonList(new GmsPermissionTester(this, mActivity));
    }

    @Override
    public int getButtonTextId() {
        return R.string.run_gms_tests;
    }
}
