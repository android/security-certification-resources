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

package com.android.certifications.niap.permissions;

import static com.android.certifications.niap.permissions.utils.InternalPermissions.permission;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.app.UiAutomation;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.android.certifications.niap.permissions.activities.MainActivity;
import com.android.certifications.niap.permissions.config.TestConfiguration;
import com.android.certifications.niap.permissions.utils.SignaturePermissions;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.hamcrest.CoreMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Instrumentation test to verify internal protection level permissions properly grant access to
 * their API, resources, etc., when the corresponding permission is granted. Internal
 * permissions are not granted to apps signed with the platform's signing key, but many are granted
 * to the shell user. Since instrumentation tests allow adopting the shell permission identity,
 * this test class can adopt this identity to be granted these permissions and verify the platform
 * behavior.
 */
@RunWith(AndroidJUnit4.class)
public class DevicePolicyPermissionsJUnitTest {

    @Rule
    public
    ErrorCollector errs = new ErrorCollector();
    @Rule
    public TestName name= new TestName();
    TestAssertLogger a = new TestAssertLogger(name);

    /**
     *
     * A list of permissions that can be granted to the shell identity.
     */
    private static final List<String> mPermissions;
    static {
        mPermissions = new ArrayList<>();

        //14
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_ACCOUNT_MANAGEMENT);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_LOCALE);
        //mPermissions.add(permission.MANAGE_DEVICE_POLICY_APP_EXEMPTIONS);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_APP_RESTRICTIONS);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_AIRPLANE_MODE);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_APPS_CONTROL);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_AUDIO_OUTPUT);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_AUTOFILL);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_BLUETOOTH);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_AUTOFILL);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_CALLS);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_CAMERA);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_CERTIFICATES);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_COMMON_CRITERIA_MODE);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_DEBUGGING_FEATURES);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_DEFAULT_SMS);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_DISPLAY);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_FACTORY_RESET);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_FUN);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_INPUT_METHODS);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_INSTALL_UNKNOWN_SOURCES);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_KEYGUARD);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_LOCALE);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_LOCATION);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_LOCK);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_LOCK_CREDENTIALS);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_LOCK_TASK);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_MICROPHONE);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_MOBILE_NETWORK);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_MODIFY_USERS);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_NEARBY_COMMUNICATION);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_ORGANIZATION_IDENTITY);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_PACKAGE_STATE);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_PHYSICAL_MEDIA);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_PRINTING);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_PROFILE_INTERACTION);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_PROFILES);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_RESET_PASSWORD);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_RESTRICT_PRIVATE_DNS);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_RUN_IN_BACKGROUND);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_RUNTIME_PERMISSIONS);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_SAFE_BOOT);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_SCREEN_CAPTURE);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_SCREEN_CONTENT);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_SECURITY_LOGGING);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_SMS);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_STATUS_BAR);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_SUPPORT_MESSAGE);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_SYSTEM_DIALOGS);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_SYSTEM_UPDATES);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_TIME);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_USB_DATA_SIGNALLING);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_VPN);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_WALLPAPER);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_WIFI);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_WINDOWS);
        mPermissions.add(permission.MANAGE_DEVICE_POLICY_WIPE_DATA);
    }

    private UiAutomation mUiAutomation;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, false,
            true);

    @Before
    public void setUp() {
        mUiAutomation = InstrumentationRegistry.getInstrumentation().getUiAutomation();
    }

    @After
    public void tearDown() {
        mUiAutomation.dropShellPermissionIdentity();
    }

    @Test
    public void runPermissionTests_shellIdentity_apisSuccessful() throws Exception {
        DevicePolicyPermissionTester permissionTester = new DevicePolicyPermissionTester(
                new InternalTestConfiguration(mPermissions), rule.getActivity());
        //Adopt the permission identity of the shell UID for all permissions.
        //This allows you to call APIs protected permissions which normal apps cannot hold but are granted to the shell UID.
        mUiAutomation.adoptShellPermissionIdentity();
        //For query contacts
        mUiAutomation.grantRuntimePermission(null,"android.permission.QUERY_ALL_PACKAGES");

        permissionTester.runPermissionTestsByThreads(
                (result)->{
                    errs.checkThat(a.Msg("Evaluate "+result.getName()+"/"+result.result),
                            result.result,org.hamcrest.CoreMatchers.is(true));
                }
        );

    }

    public static class InternalTestConfiguration implements TestConfiguration {
        private Optional<List<String>> mPermissions;

        public InternalTestConfiguration(List<String> permissions) {
            mPermissions = Optional.of(permissions);
        }

        @Override
        public List<BasePermissionTester> getPermissionTesters(Activity activity) {
            List<BasePermissionTester> permissionTesters = new ArrayList<>();
            permissionTesters.add(new DevicePolicyPermissionTester(this, activity));
            return permissionTesters;
        }

        @Override
        public Optional<List<String>> getInternalPermissions() {
            return mPermissions;
        }

        @Override
        public int getButtonTextId() {
            return R.string.run_platform_tests;
        }
    }
}