/*
 * Copyright 2023 The Android Open Source Project
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

package com.android.certifications.niap.permissions.utils;

import android.app.Activity;
import android.companion.AssociationRequest;
import android.companion.CompanionDeviceManager;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.certifications.niap.permissions.BasePermissionTester;
import com.android.certifications.niap.permissions.activities.LogListAdaptable;
import com.android.certifications.niap.permissions.log.Logger;
import com.android.certifications.niap.permissions.log.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TesterUtils {

    private static final Logger mLogger = LoggerFactory.createDefaultLogger("TesterUtils");

    public static boolean isAtLeastV() {
        return Build.VERSION.SDK_INT >= 34 && isAtLeastPreReleaseCodename("VanillaIceCream", Build.VERSION.CODENAME);
    }

    protected static boolean isAtLeastPreReleaseCodename(@NonNull String codename, @NonNull String buildCodename) {
        if ("REL".equals(buildCodename)) {
            return false;
        } else {
            String buildCodenameUpper = buildCodename.toUpperCase(Locale.ROOT);
            String codenameUpper = codename.toUpperCase(Locale.ROOT);
            return buildCodenameUpper.compareTo(codenameUpper) >= 0;
        }
    }

    /**
     * Try to connect the Bluetooth companion device manager service with a certain request.
     * With supplying the AssociationRequest by callback,
     * We can ignore the minor changes of AssociationRequest class.
     * These type of tests are available in Install and Signature level tester
     * @param packageManager
     * @param activity
     * @param arCallback
     */
    public static void tryBluetoothAssociationRequest(
            PackageManager packageManager, Activity activity,
            CompletableFuture<AssociationRequest> arCallback
    )  {
        if (!packageManager.hasSystemFeature( PackageManager.FEATURE_COMPANION_DEVICE_SETUP)) {
            throw new BasePermissionTester.BypassTestException(
                    "Device does not have the FEATURE_COMPANION_DEVICE_SETUP feature for this test require");
        }
        try {
            AssociationRequest request = arCallback.get(1000, TimeUnit.MILLISECONDS);
            if(request == null){
                throw new BasePermissionTester.BypassTestException("Failed to create the Association Request (Insufficient version?)");
            }
            CompanionDeviceManager.Callback callback = new CompanionDeviceManager.Callback() {
                @Override
                public void onFailure(@Nullable CharSequence charSequence) {
                    mLogger.logDebug("onFailure: charSequence = " + charSequence);
                }
            };
            CompanionDeviceManager companionDeviceManager = activity.getSystemService(
                    CompanionDeviceManager.class);
            companionDeviceManager.associate(request, callback, null);
        } catch (ExecutionException | InterruptedException | TimeoutException e){
            //
            throw new BasePermissionTester.BypassTestException(e.getMessage());
        }

    }

}
