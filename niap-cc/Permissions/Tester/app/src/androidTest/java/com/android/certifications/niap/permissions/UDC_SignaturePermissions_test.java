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
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.os.Parcelable;
import android.os.UserHandle;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.android.certifications.niap.permissions.activities.MainActivity;
import com.android.certifications.niap.permissions.activities.TestActivity;
import com.android.certifications.niap.permissions.utils.ReflectionUtils;
import com.android.certifications.niap.permissions.utils.SignaturePermissions;
import com.android.certifications.niap.permissions.utils.Transacts;
import com.google.android.gms.threadnetwork.ThreadNetwork;
import com.google.android.gms.threadnetwork.ThreadNetworkClient;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Instrumentation test : sandbox purpose
 */
@RunWith(AndroidJUnit4.class)
public class UDC_SignaturePermissions_test {

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
        mUiAutomation.adoptShellPermissionIdentity();
    }

    @After
    public void tearDown() {
        mUiAutomation.dropShellPermissionIdentity();
    }

    int SYSTEM_UID = 1000;

    @Test
    public void TRIGGER_SHELL_PROFCOLLECT_UPLOAD_Test() {
        runShellCommandTest(
                "am broadcast --allow-background-activity-starts " +
                        "-a com.android.shell.action.PROFCOLLECT_UPLOAD");

    }

    @Test
    public void EMERGENCY_INSTALL_PACKAGE_Test() {

        //Development

        List<String> not_granted = new ArrayList<String>();
        if (checkSelfPermission(mContext,
                SignaturePermissions.permission.EMERGENCY_INSTALL_PACKAGES)
                != PackageManager.PERMISSION_GRANTED) {
            not_granted.add(SignaturePermissions.permission.EMERGENCY_INSTALL_PACKAGES);
        }
        try {
            String TEST_APP_PKG = "com.android.certifications.niap.permissions";
            PackageInstaller.SessionParams params = new PackageInstaller.SessionParams(
                    PackageInstaller.SessionParams.MODE_FULL_INSTALL);
            params.setAppPackageName(TEST_APP_PKG);
            PackageInstaller packageInstaller =
                    mInstrumentation.getTargetContext().getPackageManager().getPackageInstaller();
            int sessionId = packageInstaller.createSession(params);
            //packageInstaller.
            //Transacts mTransacts = Transacts.createTransactsForApiLevel(35);
            //read from file

            //Parcelable _packageInstallerSession =
            //    mTransacts.invokeTransact(Transacts.INST)
            //PackageInstaller.Session session = packageInstaller.openSession(sessionId);

            //Log.d("tag","Installing " + TEST_APP_PKG);
        } catch (IOException e) {

        }

    }

    @Test
    public void THREAD_Test(){
        Class<?> threadNetworkConClazz = null;
        String FEATURE_THREAD_NETWORK = "android.hardware.thread_network";
        if(mContext.getPackageManager().hasSystemFeature(FEATURE_THREAD_NETWORK)){
            throw new BasePermissionTester.BypassTestException("thread netrowk manager is not supported.");
        } else {
            try {
                threadNetworkConClazz = Class.forName(
                        "android.net.thread.ThreadNetworkManager");
                Object threadNetworkCon = mContext.getSystemService(threadNetworkConClazz);
                System.out.println(ReflectionUtils.checkDeclaredMethod(threadNetworkCon,"set").toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Test
    public void START_ACTIVITIES_FROM_SANDBOX_Test() {

        //Eval variant and determine expected result//
        List<String> not_granted = new ArrayList<String>();
        if (checkSelfPermission(mContext,
                SignaturePermissions.permission.START_ACTIVITIES_FROM_SDK_SANDBOX)
                != PackageManager.PERMISSION_GRANTED) {
            not_granted.add(SignaturePermissions.permission.START_ACTIVITIES_FROM_SDK_SANDBOX);
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

    /**
     * Invokes the provided shell {@code command} as a permission test; a non-zero return code
     * is treated as a test failure.
     */
    protected void runShellCommandTest(String command) {
        int returnCode = runShellCommand(command);
        if (returnCode != 0) {
            throw new SecurityException(command + " failed with return code " + returnCode);
        }
    }

    /**
     * Invokes and logs the stdout / stderr of the provided shell {@code command}, returning the
     * exit code from the command.
     */
    protected int runShellCommand(String command) {
        try {
            Log.d("tag","Attempting to run command " + command);
            java.lang.Process process = Runtime.getRuntime().exec(command);
            int returnCode = process.waitFor();
            BufferedReader stdout = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            BufferedReader stderr = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()));
            StringBuilder stdoutBuilder = new StringBuilder();
            String line;
            while ((line = stdout.readLine()) != null) {
                stdoutBuilder.append(line + "\n");
            }

            StringBuilder stderrBuilder = new StringBuilder();
            while ((line = stderr.readLine()) != null) {
                stderrBuilder.append(line + "\n");
            }
            Log.d("tag","Process return code: " + returnCode);
            Log.d("tag","Process stdout: " + stdoutBuilder.toString());
            Log.d("tag","Process stderr: " + stderrBuilder.toString());
            return returnCode;
        } catch (Throwable e) {
            throw new BasePermissionTester.UnexpectedPermissionTestFailureException(e);
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