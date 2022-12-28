/*
 * Copyright 2022 The Android Open Source Project
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

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_MEDIA_LOCATION;
import static android.Manifest.permission.ADD_VOICEMAIL;
import static android.Manifest.permission.ANSWER_PHONE_CALLS;
import static android.Manifest.permission.BLUETOOTH_ADVERTISE;
import static android.Manifest.permission.BLUETOOTH_CONNECT;
import static android.Manifest.permission.BLUETOOTH_SCAN;
import static android.Manifest.permission.BODY_SENSORS;
import static android.Manifest.permission.BODY_SENSORS_BACKGROUND;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.NEARBY_WIFI_DEVICES;
import static android.Manifest.permission.POST_NOTIFICATIONS;
import static android.Manifest.permission.READ_CALENDAR;
import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_AUDIO;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.READ_MEDIA_VIDEO;
import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.UWB_RANGING;
import static android.Manifest.permission.WRITE_CALENDAR;
import static android.Manifest.permission.WRITE_CALL_LOG;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.ACTIVITY_RECOGNITION;
import static android.Manifest.permission.CAMERA;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import com.android.certifications.niap.permissions.BasePermissionTester;
import com.android.certifications.niap.permissions.Constants;
import com.android.certifications.niap.permissions.R;
import com.android.certifications.niap.permissions.RuntimePermissionTester;
import com.android.certifications.niap.permissions.log.Logger;
import com.android.certifications.niap.permissions.log.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

/**
 * Configuration designed to test runtime permissions that are dependent on other permissions. For
 * instance the ACCESS_MEDIA_LOCATION permission also requires the READ_EXTERNAL_STORAGE permission
 * to obtain an image from external storage for verification; this test allows these dependent
 * permissions to be granted to allow the API to be verified for the permissions under test.
 */
class RuntimeDependentPermissionConfiguration implements TestConfiguration {
    private static final String TAG = "PermissionTester";
    private static final Logger sLogger = LoggerFactory.createDefaultLogger(TAG);

    private final Activity mActivity;
    private CountDownLatch mCountDownLatch;

    private static final String[] RUNTIME_SDK33 = new String[]{
            NEARBY_WIFI_DEVICES,
            POST_NOTIFICATIONS,
            READ_MEDIA_AUDIO,
            READ_MEDIA_IMAGES,
            READ_MEDIA_VIDEO
    };
    private static final String[] RUNTIME_SDK31 = new String[]{
            BLUETOOTH_ADVERTISE,
            BLUETOOTH_CONNECT,
            BLUETOOTH_SCAN,
            UWB_RANGING
    };
    private static final String[] RUNTIME_SDK29 = new String[]{
            ACCESS_MEDIA_LOCATION,
            ACTIVITY_RECOGNITION
    };
    private static final String[] RUNTIME_SDK28A = new String[]{
            ACCESS_COARSE_LOCATION,
            ACCESS_FINE_LOCATION,
            ANSWER_PHONE_CALLS,
            BODY_SENSORS,
            CALL_PHONE,
            CAMERA,
    };
    private static final String[] RUNTIME_SDK28B = new String[]{
            READ_CALENDAR,
            READ_CALL_LOG,
            READ_CONTACTS,
            //READ_EXTERNAL_STORAGE,//The permission does not show user consent dialogue as of 33
            READ_PHONE_NUMBERS,
            READ_PHONE_STATE,
            READ_SMS,
    };
    private static final String[] RUNTIME_SDK28C = new String[]{
            WRITE_CALENDAR,
            WRITE_CALL_LOG,
            WRITE_CONTACTS,
            //WRITE_EXTERNAL_STORAGE,//The permission does not show user consent dialogue
            ADD_VOICEMAIL
    };
    private static final String[] REQUIRED_PERMISSIONS = RUNTIME_SDK29;// new String[]{

    //}
    private static final String[] PERMISSIONS_UNDER_TEST = RUNTIME_SDK29;//new String[]{
    //};

    RuntimeDependentPermissionConfiguration(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void preRunSetup() throws BypassConfigException {
        // If the required permissions have not yet been granted then request consent as part
        // of the setup.
        boolean permissionRequestRequired = !areRequiredPermissionsGranted();
        if (permissionRequestRequired) {
            ActivityCompat.requestPermissions(mActivity, REQUIRED_PERMISSIONS,
                    Constants.PERMISSION_CODE_RUNTIME_DEPENDENT_PERMISSIONS);
            mCountDownLatch = new CountDownLatch(1);
            try {
                // Wait for the countdown on the latch; this will block the thread attempting to
                // run the permission test while the user is prompted for consent to the required
                // permissions.
                mCountDownLatch.await();
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
        }
    }

    /**
     * Returns whether any of the permissions to be tested have been granted.
     */
    public static boolean arePermissionsUnderTestGranted(Activity activity) {
        for (String permission : PERMISSIONS_UNDER_TEST) {
            if (activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                return true;
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
        switch (requestCode) {
            case Constants.PERMISSION_CODE_RUNTIME_DEPENDENT_PERMISSIONS:
                if (!permissionGranted) {
                    sLogger.logError("The required permissions (" + String.join(", ", permissions)
                            + ") were not granted");
                }
                break;
            default:
                sLogger.logError(
                        "An unexpected request code of " + requestCode + " with permissions "
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
