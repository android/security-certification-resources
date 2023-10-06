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
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.util.Log;

import com.android.certifications.niap.permissions.BasePermissionTester;
import com.android.certifications.niap.permissions.DevicePolicyPermissionTester;
import com.android.certifications.niap.permissions.R;
import com.android.certifications.niap.permissions.utils.ReflectionUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.Collections;
import java.util.List;

/**
 * Configuration to verify the Google Play Services {GMS} client side libraries properly guard their
 * APIs behind platform permissions.
 */
class DevicePolicyConfiguration implements TestConfiguration {
    private final Activity mActivity;

    public DevicePolicyConfiguration(Activity activity) {
        mActivity = activity;
    }

    public boolean enabled(){
        Context context = mActivity.getApplicationContext();
        //Check Device Policy Manager Settings
        //DevicePolicyManager mDPM = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        //boolean isDeviceOwner = (mDPM != null && mDPM.isDeviceOwnerApp(context.getPackageName()));

        //Log.d("TAG","device owner>"+isDeviceOwner+","+context.getPackageName());
        //if(!isDeviceOwner) return false;
        //Check the experimental flag //"enable_device_policy_engine";
        String d1 = ReflectionUtils.deviceConfigGetProperty("device_policy_manager", "enable_device_policy_engine");
        String d2 = ReflectionUtils.deviceConfigGetProperty("device_policy_manager", "enable_permission_based_access");
        //Log.d("TAG","device owner>"+d1+","+d2);

        return (d1 != null && d1.equals("true")) && (d2 != null && d2.equals("true"));
   }

    @Override
    public void preRunSetup() throws BypassConfigException {
        //Check if the experimental flag was set //
        //Check the application was registered as device policy manager //
        if(!enabled())
            throw new BypassConfigException("The application is not a device owner :)");
    }

    @Override
    public List<BasePermissionTester> getPermissionTesters(Activity activity) {
        return Collections.singletonList(new DevicePolicyPermissionTester(this, mActivity));
    }

    @Override
    public int getButtonTextId() {
        return R.string.run_dpm_test;
    }
}
