package com.android.certifications.niap.permissions.utils;

import android.content.ComponentName;

import com.android.certifications.niap.permissions.activities.MainActivity;

public class DevicePolicyManagerHelper {

    /*
    android.permission.MANAGE_DEVICE_POLICY_INPUT_METHODS: PASSED (granted = true, api successful = true)
    android.permission.MANAGE_DEVICE_POLICY_LOCK: PASSED (granted = true, api successful = true)
    android.permission.MANAGE_DEVICE_POLICY_WIPE_DATA: PASSED (granted = true, api successful = true)
     */

    /*CallerIdentity getCallerIdentity(ComponentName adminComponent,
                                     String callerPackage) {

        final int callerUid = mInjector.binderGetCallingUid();

        if (callerPackage != null) {
            if (!isCallingFromPackage(callerPackage, callerUid)) {
                throw new SecurityException(
                        String.format("Caller with uid %d is not %s", callerUid, callerPackage));
            }
        }

        if (adminComponent != null) {
            final DevicePolicyData policy = getUserData(UserHandle.getUserId(callerUid));
            ActiveAdmin admin = policy.mAdminMap.get(adminComponent);

            // Throwing combined exception message for both the cases here, because from different
            // security exceptions it could be deduced if particular package is admin package.
            if (admin == null || admin.getUid() != callerUid) {
                throw new SecurityException(String.format(
                        "Admin %s does not exist or is not owned by uid %d", adminComponent,
                        callerUid));
            }
            if (callerPackage != null) {
                Preconditions.checkArgument(callerPackage.equals(adminComponent.getPackageName()));
            } else {
                callerPackage = adminComponent.getPackageName();
            }
        }

        return new CallerIdentity(callerUid, callerPackage, adminComponent);
    }*/

    /*
    private int getDpcType(CallerIdentity caller) {
        // Check the permissions of DPCs
        if (isDefaultDeviceOwner(caller)) {
            return DEFAULT_DEVICE_OWNER;
        }
        if (isFinancedDeviceOwner(caller)) {
            return FINANCED_DEVICE_OWNER;
        }
        if (isProfileOwner(caller)) {
            if (isProfileOwnerOfOrganizationOwnedDevice(caller)) {
                return PROFILE_OWNER_OF_ORGANIZATION_OWNED_DEVICE;
            }
            if (isManagedProfile(caller.getUserId())) {
                return PROFILE_OWNER;
            }
            if (isProfileOwnerOnUser0(caller)) {
                return PROFILE_OWNER_ON_USER_0;
            }
            if (isUserAffiliatedWithDevice(caller.getUserId())) {
                return AFFILIATED_PROFILE_OWNER_ON_USER;
            }
            return PROFILE_OWNER_ON_USER;
        }
        return NOT_A_DPC;
    }

     */
}
