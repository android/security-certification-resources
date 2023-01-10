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

import com.android.certifications.niap.permissions.config.ConfigurationFactory;
import com.android.certifications.niap.permissions.config.TestConfiguration;
import com.android.certifications.niap.permissions.log.Logger;

/**
 * Provides a convenient central location for constants that can be used across the app. Certain
 * constants can also be set to control the behavior of the app.
 */
public class Constants {
    /**
     * Indicates whether debug log statements should be logged by the {@link
     * Logger}.
     */
    public static final boolean DEBUG = true;
    /**
     * Indicates whether the {@link ConfigurationFactory}
     * should return a debug configuration
     */
    public static final boolean USE_DEBUG_CONFIG = false;
    /**
     * Indicates whether the {@link PrivilegedPermissionTester} should be used to test platform
     * declared signature protection level permissions with the privileged protection flag. This
     * protection flag allows priv-apps to be assigned signature permissions. This should be set to
     * true if the app will be tested as a priv-app.
     */
    public static final boolean USE_PRIVILEGED_PERMISSION_TESTER = false;
    /**
     * Indicates whether the {@link ConfigurationFactory should
     * return a {@link TestConfiguration } to test GMS on the
     * device. While The GMS configuration will check for the presence of GMS on the device this
     * should be set to {@code false}} for devices that do not support GMS.
     */
    public static final boolean USE_GMS_CONFIGURATION = true;
    /**
     * Permission code used when requesting user consent for permissions required for runtime
     * dependent permission tests.
     */
    public static final int PERMISSION_CODE_RUNTIME_DEPENDENT_PERMISSIONS = 1;
    /**
     * Extra key when passing a permission in an {@link android.content.Intent}.
     */
    public static final String EXTRA_PERMISSION_NAME = "PERMISSION_NAME";
    /**
     * Extra key when passing a boolean indicating whether a permission has been granted in an
     * {@link android.content.Intent}.
     */
    public static final String EXTRA_PERMISSION_GRANTED = "PERMISSION_GRANTED";
    /**
     * The package name of the platform.
     */
    public static final String PLATFORM_PACKAGE = "android";
    /**
     * The package name of the companion app that should be installed and run on the device under
     * test before this test app is run. The companion app exports a number of services that can be
     * used for the BIND permission tests and sets up the environment for a number of other tests
     * run by this app.
     */
    public static final String COMPANION_PACKAGE = "com.android.certifications.niap.permissions.companion";
    /**
     * The package name of the app implementing IKeyChainService; this app uses certain signature
     * level permissions to guard KeyChain operations.
     */
    public static final String KEY_CHAIN_PACKAGE = "com.android.keychain";
    /**
     * The name of the Google Play Services package.
     */
    public static final String GMS_PACKAGE_NAME = "com.google.android.gms";
}
