/*
 * Copyright 2021 The Android Open Source Project
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
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

/**
 * Instrumentation test to verify internal protection level permissions properly grant access to
 * their API, resources, etc., when the corresponding permission is granted. Internal
 * permissions are not granted to apps signed with the platform's signing key, but many are granted
 * to the shell user. Since instrumentation tests allow adopting the shell permission identity,
 * this test class can adopt this identity to be granted these permissions and verify the platform
 * behavior.
 */
@RunWith(AndroidJUnit4.class)
public class InternalPermissionsTest {
    /**
     *
     * A list of permissions that can be granted to the shell identity.
     */
    private static final List<String> mPermissions;
    static {
        mPermissions = new ArrayList<>();
        //12
        mPermissions.add(permission.MANAGE_HOTWORD_DETECTION);
        mPermissions.add(permission.OBSERVE_SENSOR_PRIVACY);
        mPermissions.add(permission.DOMAIN_VERIFICATION_AGENT);
        mPermissions.add(permission.ACCESS_RCS_USER_CAPABILITY_EXCHANGE);
        mPermissions.add(permission.ASSOCIATE_COMPANION_DEVICES);
        mPermissions.add(permission.BYPASS_ROLE_QUALIFICATION);
        mPermissions.add(permission.PERFORM_IMS_SINGLE_REGISTRATION);
        //13
        mPermissions.add(permission.SET_DEFAULT_ACCOUNT_FOR_CONTACTS);
        mPermissions.add(permission.SUBSCRIBE_TO_KEYGUARD_LOCKED_STATE);
        mPermissions.add(permission.CREATE_VIRTUAL_DEVICE);
        mPermissions.add(permission.SEND_SAFETY_CENTER_UPDATE);
        mPermissions.add(permission.ACCESS_AMBIENT_CONTEXT_EVENT);
        mPermissions.add(permission.REQUEST_COMPANION_PROFILE_AUTOMOTIVE_PROJECTION);
        mPermissions.add(permission.MANAGE_SAFETY_CENTER);
        mPermissions.add(SignaturePermissions.permission.MANAGE_SENSOR_PRIVACY);
        mPermissions.add(SignaturePermissions.permission.TOGGLE_AUTOMOTIVE_PROJECTION);
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
        InternalPermissionTester permissionTester = new InternalPermissionTester(
                new InternalTestConfiguration(mPermissions), rule.getActivity());

        mUiAutomation.adoptShellPermissionIdentity();
        //For query contacts
        mUiAutomation.grantRuntimePermission(null,"android.permission.QUERY_ALL_PACKAGES");

        assertTrue(permissionTester.runPermissionTests());

    }

    public static class InternalTestConfiguration implements TestConfiguration {
        private Optional<List<String>> mPermissions;

        public InternalTestConfiguration(List<String> permissions) {
            mPermissions = Optional.of(permissions);
        }

        @Override
        public List<BasePermissionTester> getPermissionTesters(Activity activity) {
            List<BasePermissionTester> permissionTesters = new ArrayList<>();
            permissionTesters.add(new InternalPermissionTester(this, activity));
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