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
import static org.junit.Assume.assumeTrue;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.UiAutomation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.android.certification.niap.permission.dpctester.test.SignatureTestModule;
import com.android.certification.niap.permission.dpctester.test.SignatureTestModuleR;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTest;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Instrumentation test to verify internal protection level permissions properly grant access to
 * their API, resources, etc., when the corresponding permission is granted. Internal
 * permissions are not granted to apps signed with the platform's signing key, but many are granted
 * to the shell user. Since instrumentation tests allow adopting the shell permission identity,
 * this test class can adopt this identity to be granted these permissions and verify the platform
 * behavior.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class DangerousPermissionJUnitTest {
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

    static  protected MainActivity mainActivity;
    static protected ContentResolver mContentResolver;
    static protected PackageManager mPackageManager;
    static private UiAutomation mUiAutomation;
    static private Context mContext;
    static private Activity mActivity;


    //Test Module
    static SignatureTestModule signatureTestModule;// = new SignatureTestModule(mActivity);
    static SignatureTestModuleR signatureTestModuleR;// = new SignatureTestModuleR(mActivity);


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
        mUiAutomation.adoptShellPermissionIdentity();
        //For query contacts
        mUiAutomation.grantRuntimePermission(null,"android.permission.QUERY_ALL_PACKAGES");

        signatureTestModule = new SignatureTestModule(mActivity);
        signatureTestModuleR = new SignatureTestModuleR(mActivity);
        signatureTestModule.acceptDangerousApi = true;
        signatureTestModuleR.acceptDangerousApi = true;
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

    }
    @Test
    @PermissionTest(permission = "FORCE_BACK")
    public void testForceBack() {
        signatureTestModule.testForceBack();
    }

    @Test
    @PermissionTest(permission = "RETRIEVE_WINDOW_CONTENT")
    public void testRetrieveWindowContent() {
        try {
            signatureTestModule.testRetrieveWindowContent();
        } catch (IllegalStateException ex){
            //Expected : IAccessibilityServiceClient already registered!
        }
    }
    @Test
    @PermissionTest(permission = "RETRIEVE_WINDOW_TOKEN")
    public void testRetrieveWindowToken() {
        signatureTestModule.testRetrieveWindowToken();
    }
    @Test
    @PermissionTest(permission = "SET_PREFERRED_APPLICATIONS")
    public void testSetPreferredApplications() {
        signatureTestModule.testSetPreferredApplications();
    }
    @Test
    @PermissionTest(permission="MARK_DEVICE_ORGANIZATION_OWNED", sdkMin=30,sdkMax = 32)
    public void testMarkDeviceOrganizationOwned(){
        signatureTestModuleR.testMarkDeviceOrganizationOwned();
    }

    @Test
    @PermissionTest(permission="FILTER_EVENTS", sdkMin=28, sdkMax=28)
    public void testFilterEvents(){
        /*// This causes an ANR, so skip the test if the permission is granted
        if (checkPermissionGranted("android.permission.FILTER_EVENTS")) {
            throw new BypassTestException(
                    "The API guarded by this permission will cause an ANR");
        }
        BinderTransaction.getInstance().invoke(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
                "inputDispatchingTimedOut", 1, false, "Test FILTER_EVENTS");*/
    }

    //need to execute it last
    @Test
    @PermissionTest(permission = "REBOOT")
    public void x_testReboot() {
        signatureTestModule.testReboot();
    }

}
