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

package com.android.certification.niap.permission.dpctester.common;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.android.certification.niap.permission.dpctester.test.log.StaticLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Provides utility methods to obtain the declared permissions on the device under test.
 */
public class PermissionUtils {

    private static final String TAG = "PermissionUtils";


    /**
     * Returns a {@link List} of {@link PermissionInfo} instances representing all permissions
     * declared on the device; this result includes permissions declared by the platform as well as
     * permissions declared by other packages installed on the device.
     */
    public static List<PermissionInfo> getAllDeclaredPermissions(Context context) {
        List<PermissionInfo> declaredPermissions = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        // Query for all permission groups declared on the device, along with permissions that
        // are not part of a group.
        List<PermissionGroupInfo> groups = packageManager.getAllPermissionGroups(0);
        groups.add(null);
        for (PermissionGroupInfo group : groups) {
            String groupName = group != null ? group.name : null;
            try {
                List<PermissionInfo> permissions = packageManager.queryPermissionsByGroup(
                        groupName, 0);

                declaredPermissions.addAll(permissions);

            } catch (PackageManager.NameNotFoundException e) {
                StaticLogger.error("Caught a NameNotFoundException for group " + groupName, e);
            }
        }
        return declaredPermissions;
    }

    /**
     * Returns a {@link List} of {@link PermissionInfo} instances representing all permissions
     * declared by the platform on the device.
     */
    public static List<PermissionInfo> getAllDeclaredPlatformPermissions(Context context) {
        List<PermissionInfo> platformPermissions = new ArrayList<>();
        List<PermissionInfo> declaredPermissions = getAllDeclaredPermissions(context);
        for (PermissionInfo permission : declaredPermissions) {
            if (permission.packageName.equals(Constants.PLATFORM_PACKAGE)) {
                platformPermissions.add(permission);
            }
        }
        return platformPermissions;
    }

    public static boolean isNoPermissionManifest(Context c)
    {
        return !PermissionUtils.ensureRequiredPermissions(new String[]{"android.permission.INTERNET"},c);
    }

    /*
    public static void checkTester(BasePermissionTester tester){
        Map<String, BasePermissionTester.PermissionTest> pt =tester.getRegisteredPermissions();
        sLogger.logInfo("Tester Name: "+tester.getClass().getName());
        for(Map.Entry<String,BasePermissionTester.PermissionTest> entry:pt.entrySet()){
            BasePermissionTester.PermissionTest test = entry.getValue();
            sLogger.logInfo("  >"+entry.getKey()+"(min:"+test.mMinApiLevel+",max:"+test.mMaxApiLevel+")");
        }
    }*/



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

    /**
     * Attempts to grant a permission automatically if it is considered dangerous - this only happens
     * for PO/DO devices.
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private static boolean maybeGrantDangerousPermission(
            String permission, ComponentName admin, Context context) {
        if (!isPermissionDangerous(permission, context)) {
            return true;
        }
        /*if (!ProvisioningStateUtil.isManagedByTestDPC(context)) {
            return false;
        }*/
        if (hasPermissionGranted(admin, context, permission)) {
            return true;
        }
        DevicePolicyManager devicePolicyManager =
                (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        return devicePolicyManager.setPermissionGrantState(
                admin,
                context.getPackageName(),
                permission,
                DevicePolicyManager.PERMISSION_GRANT_STATE_GRANTED);
    }

    // Min API version required for DevicePolicyManager.getPermissionGrantState
    @RequiresApi(Build.VERSION_CODES.M)
    private static boolean hasPermissionGranted(
            ComponentName componentName, Context context, String permission) {
        DevicePolicyManager devicePolicyManager = context.getSystemService(DevicePolicyManager.class);
        return devicePolicyManager.getPermissionGrantState(
                componentName, context.getPackageName(), permission)
                == DevicePolicyManager.PERMISSION_GRANT_STATE_GRANTED;
    }

    private static boolean isPermissionDangerous(String permission, Context context) {
        PermissionInfo permissionInfo;
        try {
            permissionInfo = context.getPackageManager().getPermissionInfo(permission, 0);
        } catch (PackageManager.NameNotFoundException e) {
            StaticLogger.error("Failed to look up permission.", e);
            return false;
        }
        return permissionInfo != null
                && (permissionInfo.protectionLevel & PermissionInfo.PROTECTION_MASK_BASE)
                == PermissionInfo.PROTECTION_DANGEROUS;
    }

}
