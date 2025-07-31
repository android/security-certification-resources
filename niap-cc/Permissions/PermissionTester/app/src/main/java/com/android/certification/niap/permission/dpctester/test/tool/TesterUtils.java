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

package com.android.certification.niap.permission.dpctester.test.tool;

import static android.Manifest.permission.BLUETOOTH_ADMIN;
import static android.Manifest.permission.BLUETOOTH_CONNECT;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.companion.AssociationRequest;
import android.companion.CompanionDeviceManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.android.certification.niap.permission.dpctester.test.exception.BypassTestException;
import com.android.certification.niap.permission.dpctester.test.log.StaticLogger;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TesterUtils {

    /*
    public static boolean getSystemUiFlagByName(String name){
        return (boolean) ReflectionUtils.invokeReflectionCall(
                "com.android.systemui.shared.Flags",name,null,
                new Class<?>[]{}
        );
    }*/
    public static boolean getAdminFlagByName(String name){
        return false;/*(boolean) ReflectionUtil.invoke(
                "android.app.admin.flags.Flags",name,null,
                new Class<?>[]{}
        );*/ //temporary
    }
    public static boolean isAtLeastV() {
        return Build.VERSION.SDK_INT >= 34 && !isAtLeastPreReleaseCodename("VanillaIceCream", Build.VERSION.CODENAME);
    }
    public static boolean isAtLeastBaklava() {
        return Build.VERSION.SDK_INT >= 35 && !isAtLeastPreReleaseCodename("Baklava", Build.VERSION.CODENAME);
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
     * The function to ensure that the passed in permissions are defined in manifest
     */
    public static boolean ensureRequiredPermissions(
            String[] requiredPermissions, Context context) {
        PackageInfo packageInfo;
        try {
            packageInfo =
                    context
                            .getPackageManager()
                            .getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            StaticLogger.error( "Could not find own package.", e);
            return false;
        }
        List<String> manifestPermissions = Arrays.asList(packageInfo.requestedPermissions);
        for (String expectedPermission : requiredPermissions) {
            if (!manifestPermissions.contains(expectedPermission)) {
                StaticLogger.error("Missing required permission from manifest: " + expectedPermission);
                return false;
            }
        }
        return true;
    }


    //recursive function to search am exception in the nested stack trace
    public static boolean findCauseInStackTraceElement(Boolean resp,Throwable ex,String nameMatches)
    {
        //Log.d("!*!name matches","in:"+resp+">"+ex.getClass().toString());
        //boolean bRet=false;
        if(ex.getCause() != null){
            resp = findCauseInStackTraceElement(resp,ex.getCause(),nameMatches);
        }
        if(resp) {
            return true;
        } else {
            if(ex.getClass().toString().indexOf(nameMatches)>-1){
                //Log.d("!*!name matches", ex.getClass().toString() + "??" + nameMatches);
                resp = true;
            }
            return resp;
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
            throw new BypassTestException(
                    "Device does not have the FEATURE_COMPANION_DEVICE_SETUP feature for this test require");
        }
        try {
            AssociationRequest request = arCallback.get(1000, TimeUnit.MILLISECONDS);
            if(request == null){
                throw new BypassTestException("Failed to create the Association Request (Insufficient version?)");
            }
            CompanionDeviceManager.Callback callback = new CompanionDeviceManager.Callback() {
                @Override
                public void onFailure(@Nullable CharSequence charSequence) {
                    StaticLogger.debug("onFailure: charSequence = " + charSequence);
                }
            };
            CompanionDeviceManager companionDeviceManager = activity.getSystemService(
                    CompanionDeviceManager.class);
            companionDeviceManager.associate(request, callback, null);
        } catch (ExecutionException | InterruptedException | TimeoutException e){
            //
            throw new BypassTestException(e.getMessage());
        }

    }

    /**
     * Enables the specified {@code bluetoothAdapter} if the required permission is granted.
     *
     * @param bluetoothAdapter the adapter to be enabled
     * @return {@code true} if the adapter is successfully enabled
     */
    @SuppressLint("MissingPermission")
    public static boolean enableBluetoothAdapter(Context ctx, BluetoothAdapter bluetoothAdapter) {
        // If the bluetooth adapter is enabled then no further work is required.
        if (bluetoothAdapter.isEnabled()) {
            return true;
        }
        // Android 12+ requires the BLUETOOTH_CONNECT permission to enable a bluetooth adapter.
        boolean canEnable = Build.VERSION.SDK_INT < Build.VERSION_CODES.S
                ? ActivityCompat.checkSelfPermission(ctx,BLUETOOTH_ADMIN)==PackageManager.PERMISSION_GRANTED :
                ActivityCompat.checkSelfPermission(ctx,BLUETOOTH_CONNECT)==PackageManager.PERMISSION_GRANTED;
        if (!canEnable) {
            return false;
        }
        StaticLogger.debug(
                "The bluetooth adapter is not enabled, but the permission required to enable it "
                        + "has been granted; enabling now");

        bluetoothAdapter.enable();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            StaticLogger.debug("Caught an InterruptedException waiting for the"
                    + " bluetooth adapter to be enabled");
        }
        return bluetoothAdapter.isEnabled();
    }


}
