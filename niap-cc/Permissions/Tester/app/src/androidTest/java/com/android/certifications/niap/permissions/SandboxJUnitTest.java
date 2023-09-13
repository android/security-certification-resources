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
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.system.Os;

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
public class SandboxJUnitTest {
    /**
     *
     * A list of permissions that can be granted to the shell identity.
     */
    @Rule
    public
    ErrorCollector errs = new ErrorCollector();
    @Rule
    public TestName name= new TestName();
    TestAssertLogger a = new TestAssertLogger(name);
    private UiAutomation mUiAutomation;
    private Context mContext;
    private Context mShellContext;
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, false,
            true);

    @Before
    public void setUp() {
        mUiAutomation = InstrumentationRegistry.getInstrumentation().getUiAutomation();
        mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        mShellContext = InstrumentationRegistry.getInstrumentation().getContext();
    }

    @After
    public void tearDown() {
        mUiAutomation.dropShellPermissionIdentity();
    }
    int SYSTEM_UID = 1000;
    @Test
    public void runPermissionTests(){

        //try {
            mContext.sendOrderedBroadcastAsUser(new Intent("android.intent.action.QUERY_PACKAGE_RESTART"),
                    UserHandle.getUserHandleForUid(SYSTEM_UID),
                    null, null, null, 0, null, null);


        //} catch (Exception e){
            //assertTrue(false);
        //am broadcast -a <ACTION名> --es <KEY> <VALUE>
        //am broadcast -a android.intent.action.QUERY_PACKAGE_RESTART
        //am broadcast -a android.intent.action.QUERY_PACKAGE_RESTART

    }
    @Test
    public void runPermissionTest2(){
        try {
            mShellContext.sendOrderedBroadcastAsUser(new Intent("android.intent.action.QUERY_PACKAGE_RESTART"),
                    UserHandle.getUserHandleForUid(SYSTEM_UID),
                    null, null, null, 0, null, null);
        } catch (Exception e){
            e.printStackTrace();
            assertTrue(false);
        }

    }


}