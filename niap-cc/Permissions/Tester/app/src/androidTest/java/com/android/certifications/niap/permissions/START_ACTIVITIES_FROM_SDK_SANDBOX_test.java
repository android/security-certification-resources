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

import static androidx.core.content.PermissionChecker.checkSelfPermission;

import android.Manifest;
import android.app.Instrumentation;
import android.app.UiAutomation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.UserHandle;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.android.certifications.niap.permissions.activities.MainActivity;
import com.android.certifications.niap.permissions.activities.TestActivity;
import com.android.certifications.niap.permissions.utils.ReflectionUtils;
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

/**
 * Instrumentation test : sandbox purpose
 */
@RunWith(AndroidJUnit4.class)
public class START_ACTIVITIES_FROM_SDK_SANDBOX_test {

    @Rule
    public
    ErrorCollector errs = new ErrorCollector();
    @Rule
    public TestName name = new TestName();
    TestAssertLogger a = new TestAssertLogger(name);
    private UiAutomation mUiAutomation;
    private Context mContext;
    private Context mShellContext;
    private Instrumentation mInstrumentation;
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, false,
            true);

    @Before
    public void setUp() {
        mInstrumentation = InstrumentationRegistry.getInstrumentation();
        mUiAutomation = InstrumentationRegistry.getInstrumentation().getUiAutomation();
        mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        mShellContext = InstrumentationRegistry.getInstrumentation().getContext();
        //mUiAutomation.adoptShellPermissionIdentity();
    }

    @After
    public void tearDown() {
        mUiAutomation.dropShellPermissionIdentity();
    }

    int SYSTEM_UID = 1000;

    @Test
    public void runPermissionTest() {

        //Eval variant and determine expected result//
        List<String> not_granted = new ArrayList<String>();
        if (checkSelfPermission(mContext,
                SignaturePermissions.permission.START_ACTIVITIES_FROM_SDK_SANDBOX)
                != PackageManager.PERMISSION_GRANTED) {
            not_granted.add(SignaturePermissions.permission.START_ACTIVITIES_FROM_SDK_SANDBOX);
            //not_granted.add(Manifest.permission.READ_MEDIA_IMAGES);
        }

        //Here's just a placeeholder to try new implementations.
        Intent intent = new Intent();//mContext, TestActivity.class);
        intent.setClassName(Constants.COMPANION_PACKAGE,
                Constants.COMPANION_PACKAGE + ".ViewPermissionUsageActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //Log.d("Tag", ">" + ReflectionUtils.checkDeclaredMethod(mInstrumentation, "").toString());
        try {
            mInstrumentation.startActivitySync(intent);
        } catch (RuntimeException ex){
            //Log.d("Tag","not_granted>"+not_granted.size());
            //Log.d("Tag",">"+ex.getMessage());
            //Log.d("Tag",">"+ex.getCause().getMessage());

            String firstCause = ex.getMessage();
            String secondCause = ex.getCause().getMessage();

            if(!firstCause.equals("Could not launch activity"))
                throw new SecurityException("Unintended Success or unknown error:"+firstCause);

            if(not_granted.size()>0){
                //In Case Permission  is not available
                if(!secondCause.startsWith("Unable to resolve activity for:"))
                    throw new SecurityException("Unintended Runtime error found: "+secondCause);
            } else {
                if(!secondCause.startsWith("Intent in process system resolved to different process"))
                    throw new SecurityException("Unintended Runtime error found: "+secondCause);

            }
        }
     }
}


/*
[addCoverageListener( androidx.test.internal.runner.RunnerArgs androidx.test.internal.runner.TestExecutor$Builder)
 addDelayListener( androidx.test.internal.runner.RunnerArgs androidx.test.internal.runner.TestExecutor$Builder)
 addListenersFromArg( androidx.test.internal.runner.RunnerArgs androidx.test.internal.runner.TestExecutor$Builder)
 addListenersFromClasspath( androidx.test.internal.runner.TestExecutor$Builder)
 addListenersLegacyOrder( androidx.test.internal.runner.RunnerArgs androidx.test.internal.runner.TestExecutor$Builder)
 addListenersNewOrder( androidx.test.internal.runner.RunnerArgs androidx.test.internal.runner.TestExecutor$Builder)
 addScreenCaptureProcessors( androidx.test.internal.runner.RunnerArgs)
 getArguments()
 parseRunnerArgs( android.os.Bundle)
 registerTestStorage( androidx.test.internal.runner.RunnerArgs)
 shouldWaitForOrchestratorService()
 waitForDebugger( androidx.test.internal.runner.RunnerArgs)
 addListeners( androidx.test.internal.runner.RunnerArgs androidx.test.internal.runner.TestExecutor$Builder)
 buildRequest( androidx.test.internal.runner.RunnerArgs android.os.Bundle)
 createTestRequestBuilder( android.app.Instrumentation android.os.Bundle)
 getInstrumentationResultPrinter()
 onCreate( android.os.Bundle)
 onException( java.lang.Object java.lang.Throwable)
 onOrchestratorConnect()
 onStart()
 onTestEventClientConnect()
 sendStatus( int android.os.Bundle)]
*/