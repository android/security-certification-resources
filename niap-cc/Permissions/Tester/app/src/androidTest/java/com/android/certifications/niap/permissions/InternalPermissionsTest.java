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
import static com.android.certifications.niap.permissions.utils.ReflectionUtils.invokeReflectionCall;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.UiAutomation;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.android.certifications.niap.permissions.activities.MainActivity;
import com.android.certifications.niap.permissions.activities.TestActivity;
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
import java.util.concurrent.TimeUnit;

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
     * A list of permissions that can be granted to the shell identity.
     */
    private static List<String> mPermissions;
    static {
        mPermissions = new ArrayList<>();
        mPermissions.add(permission.MANAGE_HOTWORD_DETECTION);
        mPermissions.add(permission.OBSERVE_SENSOR_PRIVACY);
        mPermissions.add(permission.ACCESS_RCS_USER_CAPABILITY_EXCHANGE);
        mPermissions.add(permission.BYPASS_ROLE_QUALIFICATION);
        mPermissions.add(permission.PERFORM_IMS_SINGLE_REGISTRATION);

        //mPermissions.add(permission.SET_DEFAULT_ACCOUNT_FOR_CONTACTS);
        mPermissions.add(permission.SUBSCRIBE_TO_KEYGUARD_LOCKED_STATE);
        mPermissions.add(permission.CREATE_VIRTUAL_DEVICE);
        mPermissions.add(permission.SEND_SAFETY_CENTER_UPDATE);
        mPermissions.add(permission.ACCESS_AMBIENT_CONTEXT_EVENT);
        mPermissions.add(permission.REQUEST_COMPANION_PROFILE_AUTOMOTIVE_PROJECTION);
        mPermissions.add(SignaturePermissions.permission.MODIFY_TOUCH_MODE_STATE);
        mPermissions.add(SignaturePermissions.permission.ADD_ALWAYS_UNLOCKED_DISPLAY);
        //mPermissions.add(permission.WRITE_SECURITY_LOG);
        //mPermissions.add(permission.PERFORM_IMS_SINGLE_REGISTRATION);

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
    public void modifyInTouchModeState()
    {
        mUiAutomation.adoptShellPermissionIdentity();
        getInstrumentation().setInTouchMode(true);
        getInstrumentation().setInTouchMode(false);
        mUiAutomation.dropShellPermissionIdentity();
    }

    @Test
    public void runManageGameSessionTest()
    {

        mUiAutomation.adoptShellPermissionIdentity();

        Intent testActivityIntent = new Intent();
        testActivityIntent.setClass(getInstrumentation().getTargetContext(), TestActivity.class);
        Intent trampolineActivityIntent;

        try {
            Class clazzActivity = Class.forName("android.service.games.GameSessionTrampolineActivity");
            Class clazzAndroidFuture = Class.forName("com.android.internal.infra.AndroidFuture");
            //createIntent( android.content.Intent android.os.Bundle com.android.internal.infra.AndroidFuture)
            Object androidFuture = clazzAndroidFuture.newInstance();
            trampolineActivityIntent=(Intent)invokeReflectionCall(clazzActivity,"createIntent",null,
                    new Class[]{Intent.class, Bundle.class,clazzAndroidFuture},
                    testActivityIntent,null,androidFuture);
            trampolineActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getInstrumentation().getTargetContext().startActivity(trampolineActivityIntent);
            getInstrumentation().waitForIdleSync();
            Object resultFuture = invokeReflectionCall(clazzAndroidFuture,"get",androidFuture,
                    new Class[]{long.class, TimeUnit.class},
                    20L,TimeUnit.SECONDS);
            Object res = invokeReflectionCall(resultFuture.getClass(),"getResultCode",resultFuture,
                    new Class[]{});
            Log.w("My Tag", "Result>"+androidFuture.toString()+","+res.toString());

            //mLogger.logDebug(ReflectionUtils.checkDeclaredMethod(clazzActivity,"").toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        mUiAutomation.dropShellPermissionIdentity();
    }

    @Test
    public void runPermissionTests_shellIdentity_apisSuccessful() throws Exception {
        InternalPermissionTester permissionTester = new InternalPermissionTester(
                new InternalTestConfiguration(mPermissions), rule.getActivity());
        //mUiAutomation.ç();
        mUiAutomation.adoptShellPermissionIdentity();
        //For query contacts
        mUiAutomation.grantRuntimePermission(null,"android.permission.QUERY_ALL_PACKAGES");
        //mUiAutomation.adoptShellPermissionIdentity(permission.WRITE_SECURITY_LOG);

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