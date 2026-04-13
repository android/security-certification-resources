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
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.android.certification.niap.permission.dpctester.test.InternalTestModule;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTest;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

import java.lang.reflect.Method;

/**
 * Instrumentation test for CinnamonBun (SDK 37) internal permissions.
 * It utilizes InternalTestModule to execute tests with shell privileges.
 */
@RunWith(AndroidJUnit4.class)
public class InternalPermissionCinnamonBunTest {
    @Rule
    public ErrorCollector errs = new ErrorCollector();
    @Rule
    public TestName name = new TestName();
    TestAssertLogger a = new TestAssertLogger(name);

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, false, true);

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

    static protected ContentResolver mContentResolver;
    static protected PackageManager mPackageManager;
    static private UiAutomation mUiAutomation;
    static private Context mContext;
    static private Activity mActivity;

    static private InternalTestModule internalTestModule;

    @BeforeClass
    static public void setUp() {
        mUiAutomation = InstrumentationRegistry.getInstrumentation().getUiAutomation();
        mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        mPackageManager = mContext.getPackageManager();
        mActivity = getActivity();

        mUiAutomation.adoptShellPermissionIdentity();

        internalTestModule = new InternalTestModule(mActivity);
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

        int sdkMax_ =  m.getAnnotation(PermissionTest.class).sdkMax();
        int sdkMin_ =  m.getAnnotation(PermissionTest.class).sdkMin();
        sdkNotSupported = Build.VERSION.SDK_INT < sdkMin_ || Build.VERSION.SDK_INT > sdkMax_;
        assumeTrue(!sdkNotSupported);

        Log.d("Instrumentation Setup","Method Info TAG=>"+testName.getMethodName());
        Log.d("Instrumentation Setup","Permission=>"+targetPermission+":"+permissionGranted);
    }

    @Test
    @PermissionTest(permission="ADD_MIRROR_DISPLAY", sdkMin=36)
    public void testAddMirrorDisplay(){
        internalTestModule.testAddMirrorDisplay();
    }

    @Test
    @PermissionTest(permission="EXECUTE_APP_FUNCTIONS", sdkMin=36)
    public void testExecuteAppFunctions(){
        internalTestModule.testExecuteAppFunctions();
    }

    @Test
    @PermissionTest(permission="ACCESS_BIOMETRIC_SENSOR_STRENGTHS", sdkMin=37)
    public void testAccessBiometricSensorStrengths(){
        internalTestModule.testAccessBiometricSensorStrengths();
    }

    @Test
    @PermissionTest(permission="ACCESS_COMPUTER_CONTROL", sdkMin=37)
    public void testAccessComputerControl(){
        internalTestModule.testAccessComputerControl();
    }

    @Test
    @PermissionTest(permission="ACCESS_HID", sdkMin=37)
    public void testAccessHid(){
        internalTestModule.testAccessHid();
    }

    @Test
    @PermissionTest(permission="BIND_ALLOWLIST_PROVIDER_SERVICE", sdkMin=37)
    public void testBindAllowlistProviderService(){
        internalTestModule.testBindAllowlistProviderService();
    }

    @Test
    @PermissionTest(permission="BIND_DEVELOPER_VERIFICATION_AGENT", sdkMin=37)
    public void testBindDeveloperVerificationAgent(){
        internalTestModule.testBindDeveloperVerificationAgent();
    }

    @Test
    @PermissionTest(permission="REQUEST_LOCATION_BUTTON_PERMISSIONS", sdkMin=37)
    public void testRequestLocationButtonPermissions(){
        internalTestModule.testRequestLocationButtonPermissions();
    }

    @Test
    @PermissionTest(permission="DISCOVER_APP_FUNCTIONS", sdkMin=37)
    public void testDiscoverAppFunctions(){
        internalTestModule.testDiscoverAppFunctions();
    }

    @Test
    @PermissionTest(permission="EXECUTE_APP_FUNCTIONS_SYSTEM", sdkMin=37)
    public void testExecuteAppFunctionsSystem(){
        internalTestModule.testExecuteAppFunctionsSystem();
    }

    // Fails on platform variant due to missing ROLE_HOME. Requires DPC or specific role.
    // @Test
    // @PermissionTest(permission="LOCK_APPS", sdkMin=37)
    // public void testLockApps(){
    //     internalTestModule.testLockApps();
    // }

    // Fails on platform variant due to missing authorized identity. Requires DPC or supervision app.
    // @Test
    // @PermissionTest(permission="MANAGE_SUPERVISION", sdkMin=37)
    // public void testManageSupervision(){
    //     internalTestModule.testManageSupervision();
    // }

    @Test
    @PermissionTest(permission="SHOW_POWER_MENU", sdkMin=37)
    public void testShowPowerMenu(){
        internalTestModule.testShowPowerMenu();
    }

    @Test
    @PermissionTest(permission="SHOW_POWER_MENU_PRIVILEGED", sdkMin=37)
    public void testShowPowerMenuPrivileged(){
        internalTestModule.testShowPowerMenuPrivileged();
    }

    @Test
    @PermissionTest(permission="SET_DEVELOPER_VERIFICATION_USER_RESPONSE", sdkMin=37)
    public void testSetDeveloperVerificationUserResponse(){
        internalTestModule.testSetDeveloperVerificationUserResponse();
    }

    // @Test
    // @PermissionTest(permission="CREATE_APP_SPECIFIC_NETWORK", sdkMin=37)
    // public void testCreateAppSpecificNetwork(){
    //     internalTestModule.testCreateAppSpecificNetwork();
    // }

    @Test
    @PermissionTest(permission="INITIATE_BUGREPORT_AS_NON_ADMIN", sdkMin=37)
    public void testInitiateBugreportAsNonAdmin(){
        // Fails with SecurityException: requires DUMP permission or bugreport whitelisting.
        // Even with shell permission identity, it fails if the package is not whitelisted in sysconfig.
        internalTestModule.testInitiateBugreportAsNonAdmin();
    }
}
