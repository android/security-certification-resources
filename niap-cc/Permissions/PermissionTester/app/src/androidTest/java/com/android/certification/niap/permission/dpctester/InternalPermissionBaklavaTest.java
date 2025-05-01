package com.android.certification.niap.permission.dpctester;
/*
 * Copyright 2024 The Android Open Source Project
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

import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.LAUNCH_CAPTURE_CONTENT_ACTIVITY_FOR_NOTE;
import static android.Manifest.permission.MANAGE_DEVICE_LOCK_STATE;
import static android.Manifest.permission.REQUEST_COMPANION_PROFILE_AUTOMOTIVE_PROJECTION;
import static android.Manifest.permission.SUBSCRIBE_TO_KEYGUARD_LOCKED_STATE;
import static android.content.Intent.ACTION_LAUNCH_CAPTURE_CONTENT_ACTIVITY_FOR_NOTE;
import static android.hardware.display.DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.UiAutomation;
import android.app.appfunctions.AppFunctionException;
import android.app.appfunctions.AppFunctionManager;
import android.app.appfunctions.AppFunctionService;
import android.app.appfunctions.ExecuteAppFunctionRequest;
import android.app.appfunctions.ExecuteAppFunctionResponse;
import android.companion.AssociationRequest;
import android.companion.CompanionDeviceManager;
import android.content.AttributionSource;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplayConfig;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.IBinder;
import android.os.OutcomeReceiver;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SdkSuppress;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.android.certification.niap.permission.dpctester.activity.TestActivity;
import com.android.certification.niap.permission.dpctester.common.Constants;
import com.android.certification.niap.permission.dpctester.common.ReflectionUtil;
import com.android.certification.niap.permission.dpctester.common.SignatureUtils;
import com.android.certification.niap.permission.dpctester.test.Transacts;
import com.android.certification.niap.permission.dpctester.test.exception.BypassTestException;
import com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException;
import com.android.certification.niap.permission.dpctester.test.tool.BinderTransaction;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTest;
import com.android.certification.niap.permission.dpctester.test.tool.TesterUtils;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Instrumentation test to verify internal protection level permissions properly grant access to
 * their API, resources, etc., when the corresponding permission is granted. Internal
 * permissions are not granted to apps signed with the platform's signing key, but many are granted
 * to the shell user. Since instrumentation tests allow adopting the shell permission identity,
 * this test class can adopt this identity to be granted these permissions and verify the platform
 * behavior.
 */
@RunWith(AndroidJUnit4.class)
public class InternalPermissionBaklavaTest {
    @Rule
    public
    ErrorCollector errs = new ErrorCollector();
    @Rule
    public TestName name= new TestName();
    TestAssertLogger a = new TestAssertLogger(name);

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, false,true);
    static private Activity getActivity() {
        Activity activity = null;
        Instrumentation.ActivityMonitor monitor =
                InstrumentationRegistry.getInstrumentation().addMonitor(
                        "com.android.certification.niap.permission.dpctester.MainActivity",
                        null, false);

        Intent intent = mContext.getPackageManager()
                .getLaunchIntentForPackage("com.android.certification.niap.permission.dpctester");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(intent);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        activity = monitor.waitForActivityWithTimeout(2000);

        return activity;
    }
    /**
     * Returns the {@link IBinder} token for the current activity.
     *
     * <p>This token can be used in any binder transaction that requires the activity's token.
     */
    public IBinder getActivityToken() {
        try {
            Field tokenField = Activity.class.getDeclaredField("mToken");
            tokenField.setAccessible(true);
            return (IBinder) tokenField.get(mActivity);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Unable to get activity token", e);
        }
    }
    private void logline(String message){

        //mainActivity.addLogLine(message);
        Log.d("InternalPermissionBaklava",message);
    }
   static  protected MainActivity mainActivity;
    static protected ContentResolver mContentResolver;
    static protected PackageManager mPackageManager;
    static private UiAutomation mUiAutomation;
    static private Context mContext;
    static private Activity mActivity;
    static private Boolean nonperm = false;


    @BeforeClass
    static public void setUp() {

        mUiAutomation = InstrumentationRegistry.getInstrumentation().getUiAutomation();
        mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        mContentResolver = mContext.getContentResolver();
        mPackageManager = mContext.getPackageManager();
        mActivity = getActivity();
        mainActivity = (MainActivity)mActivity;

        //Adopt the permission identity of the shell UID for all permissions.
        //This allows you to call APIs protected permissions which normal apps cannot hold but are granted to the shell UID.
        //Test Internet Permission to verify
        if(mPackageManager.checkPermission(INTERNET,mContext.getPackageName())
                ==PackageManager.PERMISSION_GRANTED) {
            mUiAutomation.adoptShellPermissionIdentity();
        }else{
            mUiAutomation.dropShellPermissionIdentity();
            nonperm = true;
        }

        //For query contacts
        mUiAutomation.grantRuntimePermission(null,"android.permission.QUERY_ALL_PACKAGES");
    }
    @AfterClass
    static public void tearDown() {
        mUiAutomation.dropShellPermissionIdentity();
    }

    @Rule public TestName testName = new TestName();
    String targetPermission = "";
    Boolean permissionGranted = false;
    Boolean sdkNotSupported = false;
    @Before
    public void setUpTest() throws NoSuchMethodException {
        String name = testName.getMethodName();
        Method m = this.getClass().getDeclaredMethod(name);
        m.setAccessible(true);
        //Normalize the permission name
        String permission_ =  m.getAnnotation(PermissionTest.class).permission();
        if(!permission_.contains(".")){
            targetPermission="android.permission."+permission_;
        } else {
            targetPermission = permission_;
        }

        if(mPackageManager.checkPermission(targetPermission,mContext.getPackageName())
                == PackageManager.PERMISSION_GRANTED){
            permissionGranted = true;
        } else {
            permissionGranted = false;
        }
        //Skip if the target sdk is not support the test case
        int sdkMax_ =  m.getAnnotation(PermissionTest.class).sdkMax();
        int sdkMin_ =  m.getAnnotation(PermissionTest.class).sdkMin();
        sdkNotSupported = Build.VERSION.SDK_INT < sdkMin_ || Build.VERSION.SDK_INT > sdkMax_;
        assumeTrue(!sdkNotSupported);

        if(nonperm && permissionGranted){
            Log.d("Instrumentation Setup","Permission "+targetPermission+" is unintentionally granted");
            assumeFalse(permissionGranted);
        }

        if(!nonperm && !permissionGranted){
            Log.d("Instrumentation Setup","Permission "+targetPermission+" can not be granted");
            assumeTrue(permissionGranted);
        }

        //label =  aModule.javaClass.getAnnotation(PermissionTestModule::class.java)?.label
        Log.d("Instrumentation Setup","Method Info TAG=>"+testName.getMethodName());
        Log.d("Instrumentation Setup","Permission=>"+targetPermission+":"+permissionGranted);
    }

    @Test
    @PermissionTest(permission="ADD_MIRROR_DISPLAY",sdkMin=35,sdkMax = 36)
    public void testAddMirrorDisplay(){
        DisplayManager displayManager = mContext.getSystemService(DisplayManager.class);
        VirtualDisplayConfig.Builder builder =
                new VirtualDisplayConfig.Builder("",600,800,320)
                        .setFlags(VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR);//setUniqueId("uniqueId -- mirror");
        displayManager.createVirtualDisplay(builder.build());
        //Log.d("Add Mirror Display","The test for android.permission.ADD_MIRROR_DISPLAY is not implemented yet");
    }


    @Test
    @PermissionTest(permission="EXECUTE_APP_FUNCTIONS",sdkMin=35,sdkMax = 36)
    public void testExecuteAppFunctions(){
        ExecuteAppFunctionRequest request =
                new ExecuteAppFunctionRequest.Builder(Constants.COMPANION_PACKAGE,"noOp").build();

        AppFunctionManager manager = mContext.getSystemService(AppFunctionManager.class);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean result = new AtomicBoolean(false);
        manager.executeAppFunction(request, mContext.getMainExecutor(), new CancellationSignal(),
                new OutcomeReceiver<ExecuteAppFunctionResponse, AppFunctionException>() {
            @Override
            public void onResult(ExecuteAppFunctionResponse executeAppFunctionResponse) {
                result.set(true);
                latch.countDown();
            }
            @Override
            public void onError(@NonNull AppFunctionException error) {
                result.set(false);
                latch.countDown();
            }
        });

        try {
            latch.await(5000,TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Log.d("Execute App Function","result = "+result.get());
    }
    @Test
    @PermissionTest(permission="THREAD_NETWORK_TESTING",sdkMin=35)
    public void testThreadNetworkTesting(){
        //  runShellCommand("force-country-code", "enabled", "US");?
        //The command related to network requires root access , so we can't test it on normal app
        int shellRet  = runShellCommand("cmd wifi help");//wifi get-country-code");
        //Log.d("Shell",">"+shellRet);
    }

    protected int runShellCommand(String command) {
        try {
            Log.d("shell","Attempting to run command " + command);
            java.lang.Process process = Runtime.getRuntime().exec(command);//.exec("su");
            //DataOutputStream outputStream = new DataOutputStream(process.getOutputStream());
            //outputStream.writeBytes(command);
            //outputStream.flush();
            process.waitFor();
            //command);
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
            Log.d("shell","Process return code: " + returnCode);
            Log.d("shell","Process stdout: " + stdoutBuilder.toString());
            Log.d("shell","Process stderr: " + stderrBuilder.toString());
            return returnCode;
        } catch (Throwable e) {
            throw new UnexpectedTestFailureException(e);
        }
    }
}
