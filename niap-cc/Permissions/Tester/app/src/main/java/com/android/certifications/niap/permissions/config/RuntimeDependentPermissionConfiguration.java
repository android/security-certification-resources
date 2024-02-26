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

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import com.android.certifications.niap.permissions.BasePermissionTester;
import com.android.certifications.niap.permissions.Constants;
import com.android.certifications.niap.permissions.R;
import com.android.certifications.niap.permissions.RuntimePermissionTester;
import com.android.certifications.niap.permissions.activities.LogListAdaptable;
import com.android.certifications.niap.permissions.log.Logger;
import com.android.certifications.niap.permissions.log.LoggerFactory;
import com.android.certifications.niap.permissions.utils.PermissionUtils;
import com.google.android.gms.common.internal.GmsLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Configuration designed to test runtime permissions that are dependent on other permissions. For
 * instance the ACCESS_MEDIA_LOCATION permission also requires the READ_EXTERNAL_STORAGE permission
 * to obtain an image from external storage for verification; this test allows these dependent
 * permissions to be granted to allow the API to be verified for the permissions under test.
 */
class RuntimeDependentPermissionConfiguration implements TestConfiguration {
    private static final String TAG = "PermissionTester";
    //private static final Logger sLogger = LoggerFactory.createDefaultLogger(TAG);
    private Logger mLogger;

    private final Activity mActivity;
    private CountDownLatch mCountDownLatch;

    private String[] REQUIRED_PERMISSIONS;
    private static final String[] PERMISSIONS_UNDER_TEST = new String[]{
            Manifest.permission.ACCESS_MEDIA_LOCATION,
            Manifest.permission.SEND_SMS,
    };

    RuntimeDependentPermissionConfiguration(Activity activity) {

        mActivity = activity;
        mLogger = LoggerFactory.createActivityLogger(TAG,(LogListAdaptable)mActivity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            REQUIRED_PERMISSIONS = new String[]{
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_PHONE_NUMBERS,
            };
        } else {
            REQUIRED_PERMISSIONS = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_NUMBERS,
            };
        }
    }

    //Set Enable In Case Below
    // 1. Required Runtime Permission is in Manifest and still not granted.
    // 2. Permissions under test are granted in Manifest
    public boolean checkStatusOfConfig() {
        Context c = mActivity.getApplicationContext();
        if(!PermissionUtils.ensureRequiredPermissions(REQUIRED_PERMISSIONS,c)){
            mLogger.logInfo("RuntimeDependentPermissionConfiguration is disabled. Required Permission is not in the Manifest");
            return false;
        }
        if(!PermissionUtils.ensureRequiredPermissions(PERMISSIONS_UNDER_TEST,c)) {
             mLogger.logInfo("RuntimeDependentPermissionConfiguration is disabled. Target Permission is not in the Manifest");
             return false;
        }
        for (String permission : REQUIRED_PERMISSIONS) {
            if (mActivity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                mLogger.logSystem(
                        "RuntimeDependentPermission test is bypassed: "+permission+" is already granted." +
                                "If you want to run this test configuration please revoke all runtime permissions at first.");
                return false;
            }
        }
        return true;
    }

    @Override
    public void preRunSetup() throws BypassConfigException {
        // If the required permissions have not yet been granted then request consent as part
        // of the setup.
        boolean permissionRequestRequired = !areRequiredPermissionsGranted();
        if (permissionRequestRequired) {

            List<String> not_granted = new ArrayList<String>();
            for(String p:REQUIRED_PERMISSIONS){
                if (mActivity.checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED) {
                    not_granted.add(p);
                }
            }
            String[] perms = not_granted.toArray(new String[0]);
            //Show Dialogue here and it will invoke the callback on activity.
            ActivityCompat.requestPermissions(mActivity,perms,
                    Constants.PERMISSION_CODE_RUNTIME_DEPENDENT_PERMISSIONS);

            //Log.d(TAG,"permission request required?");
            //ActivityCompat.requestPermissions(mActivity, REQUIRED_PERMISSIONS,
            //        Constants.PERMISSION_CODE_RUNTIME_DEPENDENT_PERMISSIONS);

            mCountDownLatch = new CountDownLatch(1);
            try {
                // Wait for the countdown on the latch; this will block the thread attempting to
                // run the permission test while the user is prompted for consent to the required
                // permissions.
                mCountDownLatch.await(10, TimeUnit.SECONDS);
                // If the user has not granted the required permissions then throw a bypass
                // exception to notify the user of this requirement.
                if (!areRequiredPermissionsGranted()) {
                    throw new BypassConfigException(mActivity.getResources().getString(
                            R.string.permissions_must_be_granted,
                            String.join(", ", REQUIRED_PERMISSIONS)));
                }


            } catch (InterruptedException e) {
                throw new BypassConfigException(
                        mActivity.getResources().getString(R.string.exception_permission_consent,
                                e.getMessage()));
            }
        } else {
            mLogger.logSystem("All required permissions are granted. skip the test");
            throw new BypassConfigException(
                    "All required permissions are granted. In this case, This test should be bypassed.To fix this problem, " +
                            "try uninstall the tester application to cleanup the permission condition.");
        }
    }

    /**
     * Returns whether any of the permissions to be tested have been granted.
     */
    public static boolean arePermissionsUnderTestGranted(Activity activity) {



        for (String permission : PERMISSIONS_UNDER_TEST) {
            if (activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                //Log.d(TAG,"permission granted?"+permission);

                return true;
            } else {
                //Log.d(TAG,"Not granted:"+permission);
            }
        }
        return false;
    }

    /**
     * Returns whether the permissions that are required to verify the permissions under test have
     * been granted.
     */
    private boolean areRequiredPermissionsGranted() {



        for (String permission : REQUIRED_PERMISSIONS) {
            if (mActivity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
            int[] grantResults) {
        boolean permissionGranted = true;
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                permissionGranted = false;
            }
        }
        //
        switch (requestCode) {
            case Constants.PERMISSION_CODE_RUNTIME_DEPENDENT_PERMISSIONS:
                if (!permissionGranted) {
                    mLogger.logSystem("Runtime Dependent Permission : The required permissions are not granted.");
                    throw new RuntimeException("The required permissions (" + String.join(", ", permissions)
                                    + ") were not granted");
                }
                break;
            default:
                throw new RuntimeException("An unexpected request code of " + requestCode + " with permissions "
                        + String.join(", ", permissions) + " + was received");
        }
        mCountDownLatch.countDown();
    }

    @Override
    public List<BasePermissionTester> getPermissionTesters(Activity activity) {
        return Collections.singletonList(new RuntimePermissionTester(this, activity));
    }

    @Override
    public Optional<List<String>> getRuntimePermissions() {
        List<String> permissions = new ArrayList<>(PERMISSIONS_UNDER_TEST.length);
        for (String permission : PERMISSIONS_UNDER_TEST) {
            permissions.add(permission);
        }
        return Optional.of(permissions);
    }

    @Override
    public int getButtonTextId() {
        return R.string.run_permission_dependent_tests;
    }


}
