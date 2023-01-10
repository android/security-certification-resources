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

package com.android.certifications.niap.permissions.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;

import com.android.certifications.niap.permissions.BasePermissionTester;
import com.android.certifications.niap.permissions.Constants;
import com.android.certifications.niap.permissions.log.Logger;
import com.android.certifications.niap.permissions.log.LoggerFactory;
import com.android.certifications.niap.permissions.log.StatusLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Provides utility methods to obtain the declared permissions on the device under test.
 */
public class PermissionUtils {

    private static final String TAG = "PermissionUtils";
    private static final Logger sLogger = LoggerFactory.createDefaultLogger(TAG);


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
                StatusLogger.logError("Caught a NameNotFoundException for group " + groupName, e);
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


    public static void checkTester(BasePermissionTester tester){
        Map<String, BasePermissionTester.PermissionTest> pt =tester.getRegisteredPermissions();
        sLogger.logInfo("Tester Name: "+tester.getClass().getName());
        for(Map.Entry<String,BasePermissionTester.PermissionTest> entry:pt.entrySet()){
            BasePermissionTester.PermissionTest test = entry.getValue();
            sLogger.logInfo("  >"+entry.getKey()+"(min:"+test.mMinApiLevel+",max:"+test.mMaxApiLevel+")");
        }
    }

}
