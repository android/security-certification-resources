package com.android.certification.niap.permission.dpctester.test;
/*
 * Copyright (C) 2024 The Android Open Source Project
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
import static android.Manifest.permission.ACCESS_MEDIA_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.SEND_SMS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.android.certification.niap.permission.dpctester.common.Constants;
import com.android.certification.niap.permission.dpctester.common.PermissionUtils;
import com.android.certification.niap.permission.dpctester.test.exception.BypassTestException;
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestModuleBase;
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestRunner;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTest;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestModule;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@PermissionTestModule(name="Runtime Dependent Test Cases",
        prflabel = "Runtime Dependent",label = "Run Runtime Dependent Tests",sync = true)
public class RuntimeDependentTestModule extends PermissionTestModuleBase {
    RuntimeTestModule baseModule;
    public RuntimeDependentTestModule(@NonNull Activity activity) {
        super(activity);
        baseModule = new RuntimeTestModule(activity);
    }
    @PermissionTest(permission=ACCESS_MEDIA_LOCATION, sdkMin=29,
            requestedPermissions = {"android.permission.READ_MEDIA_IMAGES"})
    public void testAccessMediaLocation(){
        baseModule.testAccessMediaLocation();
    }
    @PermissionTest(permission=SEND_SMS,
            requestedPermissions = {"android.permission.READ_PHONE_NUMBERS"})
    public void testSendSms(){
        baseModule.testSendSms();
    }


    //maybe we can take these information from annotation, and it's a reasonable thoughts,
    //but currently the module is enough small
    String [] REQUIRED_PERMISSIONS = {"android.permission.READ_PHONE_NUMBERS",
            "android.permission.READ_MEDIA_IMAGES"};
    String [] PERMISSIONS_UNDER_TEST = {ACCESS_MEDIA_LOCATION,SEND_SMS};
    CountDownLatch mCountDownLatch  = null;
    @NonNull
    @Override
    public PrepareInfo prepare(Consumer<PermissionTestRunner.Result> callback) {

        var qs = super.prepare(callback);
        if(!areRequiredPermissionsGranted()) {
            //Show Dialogue here and it will invoke the callback on activity.
            ActivityCompat.requestPermissions(
                    mActivity, REQUIRED_PERMISSIONS,
                    Constants.PERMISSION_CODE_RUNTIME_DEPENDENT_PERMISSIONS
            );
            mCountDownLatch = new CountDownLatch(1);
            try {
                // Wait for the countdown on the latch; this will block the thread attempting to
                // run the permission test while the user is prompted for consent to the required
                // permissions.
                mCountDownLatch.await(10, TimeUnit.SECONDS);
                // If the user has not granted the required permissions then throw a bypass
                // exception to notify the user of this requirement.
                if (!areRequiredPermissionsGranted()) {
                    throw new BypassTestException("requested Permission is not granted");
                }
            } catch (InterruptedException e){
                throw new BypassTestException("Caught an Interruption");
            }
        }

        return qs;
    }
    private boolean areRequiredPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (mActivity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           int[] grantResults) {
        logger.system("onRequestPermissionResult event caught:"+requestCode);
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
                    logger.system("Runtime Dependent Permission : The required permissions are not granted.");
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

    //Tool for enable runtime dependent permission test cases
    // Enable In Case Below
    // 1. Required Runtime Permission is in Manifest and still not granted.
    // 2. Permissions under test are granted in Manifest
    public boolean checkModuleIsValid() {
        Context c = mActivity.getApplicationContext();


        if(!PermissionUtils.ensureRequiredPermissions(REQUIRED_PERMISSIONS,c)){
            logger.info("RuntimeDependentPermissionConfiguration is disabled. Required Permission is not in the Manifest");
            return false;
        }
        if(!PermissionUtils.ensureRequiredPermissions(PERMISSIONS_UNDER_TEST,c)) {
            logger.info("RuntimeDependentPermissionConfiguration is disabled. Target Permission is not in the Manifest");
            return false;
        }
        for (String permission : REQUIRED_PERMISSIONS) {
            if (mActivity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                logger.system(
                        "RuntimeDependentPermission test is bypassed: "+permission+" is already granted." +
                                "If you want to run this test configuration please revoke all runtime permissions at first.");
                return false;
            }
        }
        return true;
    }
}
