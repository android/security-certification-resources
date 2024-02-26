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

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.android.certifications.niap.permissions.Constants;
import com.android.certifications.niap.permissions.log.Logger;
import com.android.certifications.niap.permissions.log.LoggerFactory;
import com.android.certifications.niap.permissions.utils.SignatureUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Factory to create {@link TestConfiguration} instances to be used by the tester. An {@link
 * Activity} driving the permission tester should invoke {@link #getConfigurations(Activity)} to
 * obtain the configurations that should be used for the test.
 */
public class ConfigurationFactory {
    private static final String TAG = "PermissionTesterConfig";
    private static final Logger sLogger = LoggerFactory.createDefaultLogger(TAG);

    // The TestConfiguration interface uses default methods that define the expected behavior
    // for the platform permission tests; an anonymous instantiation of the interface is used for
    // the default config.
    private static final TestConfiguration DEFAULT_CONFIGURATION = new TestConfiguration() {
    };

    /**
     * Returns a {@link List} of {@link TestConfiguration} instances representing the tests to be
     * run on the device.
     */
    public static List<TestConfiguration> getConfigurations(Activity activity) {
        // The DebugConfiguration class can be used when testing individual permissions,
        // a single configuration class, etc.
        if (Constants.USE_DEBUG_CONFIG) {
            return Collections.singletonList(new DebugConfiguration());
        }

        Context context = activity.getApplicationContext();
        // If the app is signed with the same signing key as the platform then return the platform
        // signed specific test configurations.
        if (SignatureUtils.hasSameSigningCertificateAsPackage(context, Constants.PLATFORM_PACKAGE)) {
            return getPlatformSignedConfiguration(activity);
        }

        List<TestConfiguration> configurations = getDefaultConfigurations(activity);

        RuntimeDependentPermissionConfiguration runtime_c =
                new RuntimeDependentPermissionConfiguration(activity);

        //Set Enable In Case Below
        // 1. Required Runtime Permission is in Manifest and still not granted.
        // 2. Permissions under test are granted in Manifest
        if(runtime_c.checkStatusOfConfig()){
            configurations.add(runtime_c);
        }


        // If the app has been granted any of the permissions to be tested by the
        // RuntimeDependentPermissionConfiguration then it has to be the version that has requested
        // and been granted all runtime permissions; return the default configurations as the
        // runtime dependent tests request that the app be installed without runtime permissions
        // granted.
        //  if (!RuntimeDependentPermissionConfiguration.arePermissionsUnderTestGranted(activity)) {
        //            // The permission tester app with no permissions requested may request a single
        //            // QUERY_ALL_PACKAGES permission to discover other packages and permissions on the
        //            // device; if the app has requested more than one permission then treat it as the
        //            // standard permission tester intended to run the runtime permission dependent tests.
        //            try {
        //                PackageInfo packageInfo = activity.getPackageManager().getPackageInfo(
        //                        activity.getPackageName(), PackageManager.GET_PERMISSIONS);
        //                if (packageInfo.requestedPermissions != null
        //                        && packageInfo.requestedPermissions.length > 1) {
        //
        //
        //                    //sLogger.logSystem("ensure?>/"+c.enabled());
        //                }
        //                return configurations;
        //            } catch (PackageManager.NameNotFoundException e) {
        //                sLogger.logError(
        //                        "Caught an exception querying for this app's requested permissions",
        //                        e);
        //                // Since the requested permissions cannot be determined use the default
        //                // configurations for the test.
        //            }
        // }
        return configurations;
    }

    /**
     * Returns a {@link List} of {@link TestConfiguration} instances to be used during testing when
     * the app is signed with the same key as the platform.
     */
    private static List<TestConfiguration> getPlatformSignedConfiguration(Activity activity) {
        // If the app has been granted any of the dependent permissions from the
        // SignatureDependentPermissionConfiguration then return the default configurations
        // along with any platform signed unique configurations.
        if (SignatureDependentPermissionConfiguration.areDependentPermissionsGranted(activity)) {
            List<TestConfiguration> configurations = new ArrayList<>();
            configurations.add(new PlatformSignedDefaultConfiguration());
            configurations.add(new NonFrameworkPermissionConfiguration());
            configurations.addAll(getAdditionalConfigurations(activity));
            configurations.add(new ManageBiometricDialogConfiguration());
            return configurations;
        }
        return Collections.singletonList(new SignatureDependentPermissionConfiguration(activity));
    }

    /**
     * Returns a {@link List} of {@link TestConfiguration} instances that should be used for most
     * testing instances.
     *
     * <p>The one exception is for a platform signed app that has not been granted runtime
     * permissions; in that case {@link #getPlatformSignedConfiguration(Activity)} should be used to
     * obtain the {@code TestConfiguration} instance(s) to run the signature dependent tests.
     */
    private static List<TestConfiguration> getDefaultConfigurations(Activity activity) {
        List<TestConfiguration> configurations = new ArrayList<>();
        configurations.add(DEFAULT_CONFIGURATION);

        InstallPermissionOnlyConfiguration installPermissionOnlyConfiguration =
                new  InstallPermissionOnlyConfiguration(activity);
        if(installPermissionOnlyConfiguration.enabled()){
            configurations.add(installPermissionOnlyConfiguration);
        }

        configurations.add(new NonFrameworkPermissionConfiguration());
        configurations.addAll(getAdditionalConfigurations(activity));
        return configurations;
    }

    /**
     * Returns a {@link List} of additional {@link TestConfiguration} instances that should be run
     * along with the default {@code TestConfiguration} instances.
     *
     * <p>This typically includes a configuration to test Google Play Services (if available) and
     * any other device specific configurations.
     */
    private static List<TestConfiguration> getAdditionalConfigurations(Activity activity) {
        List<TestConfiguration> additionalConfigurations = new ArrayList<>();
        if (Constants.USE_GMS_CONFIGURATION) {
            additionalConfigurations.add(new GmsPermissionConfiguration(activity));
        }

        DevicePolicyConfiguration devicePolicyConfiguration =
                new DevicePolicyConfiguration(activity);
        if(devicePolicyConfiguration.enabled()){
            additionalConfigurations.add(devicePolicyConfiguration);
        }



        // TODO: Any custom configurations that are intended to be run as part of this test should
        // be added here.

        return additionalConfigurations;
    }
}
