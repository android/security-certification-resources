package com.android.certification.niap.permission.dpctester.test.tool
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
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PermissionGroupInfo
import android.content.pm.PermissionInfo
import com.android.certification.niap.permission.dpctester.common.DevicePolicyManagerGateway
import com.android.certification.niap.permission.dpctester.common.DevicePolicyManagerGateway.DeviceOwnerLevel
import com.android.certification.niap.permission.dpctester.common.ReflectionUtil
import com.android.certification.niap.permission.dpctester.test.log.StaticLogger

class PermissionTool {
    companion object {
        fun getAdminFlag(method: String): Boolean {
            return ReflectionUtil.invoke<Boolean>(Class.forName("android.app.admin.flags.Flags"),
                method)
        }
        fun getDeviceOwnerLevel(dpm:DevicePolicyManagerGateway): DeviceOwnerLevel {
            if(dpm.isDeviceOwnerApp){
                return DeviceOwnerLevel.DPS_DEVICE_OWNER_APP
            }
            if(dpm.isProfileOwnerApp){
                return DeviceOwnerLevel.DPS_PROFILE_OWNER_APP
            }
            if(dpm.isAdminActive){
                return DeviceOwnerLevel.DPS_ACTIVE_ADMIN_APP
            } else {
                return DeviceOwnerLevel.DPS_DISABLED
            }

        }

        /**
         * Returns a [List] of [PermissionInfo] instances representing all permissions
         * declared on the device; this result includes permissions declared by the platform as well as
         * permissions declared by other packages installed on the device.
         */
        fun getAllDeclaredPermissions(context: Context): List<PermissionInfo> {
            val declaredPermissions: MutableList<PermissionInfo> =
                ArrayList() //getAllDeclaredPermissions(mContext);
            val packageManager = context.packageManager
            val groups: MutableList<PermissionGroupInfo?> =
                packageManager.getAllPermissionGroups(0)
            groups.add(null)
            for (group in groups) {
                val groupName = group?.name
                try {
                    val permissions: List<PermissionInfo> = packageManager.queryPermissionsByGroup(
                        groupName, 0
                    )

                    declaredPermissions.addAll(permissions)
                } catch (e: PackageManager.NameNotFoundException) {
                    StaticLogger.error("Caught a NameNotFoundException for group $groupName", e)
                }
            }
            return declaredPermissions
        }
        /*
        fun getGrantedPermissions(ctx: Context, appPackage: String?): List<String>
        {
            val granted: MutableList<String> = ArrayList()
            try {
                val pi = ctx.packageManager.getPackageInfo(appPackage!!, PackageManager.GET_PERMISSIONS)
                for (i in pi.requestedPermissions.indices) {
                    if ((pi.requestedPermissionsFlags[i] and PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0) {
                        granted.add(pi.requestedPermissions[i])
                    }
                }
            } catch (e: Exception) {
            }
            return granted
        }*/
    }
}