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

package com.android.certifications.niap.permissions;

import android.app.Activity;

import com.android.certifications.niap.permissions.config.TestConfiguration;
import com.android.certifications.niap.permissions.log.Logger;
import com.android.certifications.niap.permissions.log.LoggerFactory;
import com.android.certifications.niap.permissions.log.StatusLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Permission tester to verify all platform declared signature permissions with the privileged flag
 * properly guard their API, resources, etc. To be granted a platform declared signature permission
 * with the privileged flag an app must either be signed with the same signing key as the platform
 * or be preloaded on a device in the priv-apps directory.
 */
public class PrivilegedPermissionTester extends SignaturePermissionTester {
    private static final String TAG = "PrivilegedPermissionTester";
    private final Logger mLogger = LoggerFactory.createDefaultLogger(TAG);

    private boolean mIsPrivApp;

    public PrivilegedPermissionTester(TestConfiguration configuration, Activity activity) {
        super(configuration, activity);
        // In order to be a privileged app the test app must be preloaded on a build in the
        // priv-app/ directory. Note that this also requires whitelisting the privileged
        // permission for this app. For more details see:
        // https://source.android.com/devices/tech/config/perms-whitelist
        mIsPrivApp = mAppInfo.sourceDir != null && mAppInfo.sourceDir.contains("priv-app/");
        mLogger.logDebug("The current app installed at " + mAppInfo.sourceDir + " is a priv-app: "
                + mIsPrivApp);
    }

    @Override
    public boolean runPermissionTests() {
        boolean allTestsPassed = true;
        List<String> permissions = mConfiguration.getPrivilegedPermissions().orElse(
                new ArrayList<>(mPrivilegedPermissions));
        for (String permission : permissions) {
            boolean testPassed = true;
            // Only test signature permissions with the privileged protection flag; if a
            // non-privileged permission is attempted here it may be due to a configuration error.
            if (!mPrivilegedPermissions.contains(permission)) {
                mLogger.logDebug(permission + " is not a privileged permission");
                continue;
            }
            if (mPermissionTasks.containsKey(permission)) {
                testPassed = runPermissionTest(permission, mPermissionTasks.get(permission), true);
            } else {
                testPassed = getAndLogTestStatus(permission);
            }
            if (!testPassed) {
                allTestsPassed = false;
            }
        }
        if (allTestsPassed) {
            StatusLogger.logInfo(
                    "*** PASSED - all privileged permission tests completed successfully");
        } else {
            StatusLogger.logInfo(
                    "!!! FAILED - one or more privileged permission tests failed");
        }
        return allTestsPassed;
    }

    /**
     * Logs a status entry for the provided {@code permission}; the test app should only be granted
     * a signature level permission with the privilege flag  if it has been signed with the
     * platform's signing key or if it has been preloaded in the priv-apps directory.
     *
     * @return boolean indicating whether the test for the {@code permission} was successful
     */
    @Override
    protected boolean getAndLogTestStatus(String permission) {
        boolean testPassed = true;
        boolean permissionGranted = isPermissionGranted(permission);
        // All privileged permissions are also signature permissions, so it is not a failure if a
        // platform signed app holds a signature|privileged permission. Development permissions can
        // also be granted with the -g flag, so allow those as well since the test app is typically
        // installed with this flag.
        if (!mPlatformSignatureMatch && !mIsPrivApp && permissionGranted
                && !mDevelopmentPermissions.contains(permission)) {
            testPassed = false;
        }
        if (mIsPrivApp && !permissionGranted) {
            testPassed = false;
        }
        if (mPlatformSignatureMatch && !permissionGranted) {
            testPassed = false;
        }
        StatusLogger.logInfo(
                permission + ": " + (testPassed ? "PASSED" : "FAILED") + " (granted = "
                        + permissionGranted + ", signature match = " + mPlatformSignatureMatch
                        + ", priv-app = " + mIsPrivApp + ")");
        return testPassed;
    }

    /**
     * Logs a status entry for the provided {@code permission} based on the specified {@code
     * permissionGranted} and {@code apiSuccessful} flags.
     *
     * <p>The test is considered successful if the grant state of the permission is the same as
     * both whether the app is platform signed and the API for the test is successful.
     *
     * @return boolean indicating whether the test for the {@code permission} was successful
     */
    @Override
    public boolean getAndLogTestStatus(String permission, boolean permissionGranted,
            boolean apiSuccessful) {
        boolean testPassed = true;
        // if the app is not a priv-app then the permission should not be granted.
        if (!mPlatformSignatureMatch && mIsPrivApp != permissionGranted
                && !mDevelopmentPermissions.contains(permission)) {
            testPassed = false;
        }
        if (mPlatformSignatureMatch && permissionGranted != true) {
            testPassed = false;
        }
        // the API should only be successful when the permission is granted.
        if (permissionGranted != apiSuccessful) {
            testPassed = false;
        }
        StatusLogger.logInfo(
                permission + ": " + (testPassed ? "PASSED" : "FAILED") + " (granted = "
                        + permissionGranted + ", api successful = " + apiSuccessful
                        + ", signature match = " + mPlatformSignatureMatch + ", priv-app = "
                        + mIsPrivApp + ")");
        return testPassed;
    }
}
