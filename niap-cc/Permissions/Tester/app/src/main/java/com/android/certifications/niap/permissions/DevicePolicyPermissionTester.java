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

import static com.android.certifications.niap.permissions.utils.ReflectionUtils.stubHiddenObject;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.app.admin.SystemUpdatePolicy;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.UserManager;

import androidx.core.util.Consumer;

import com.android.certifications.niap.permissions.activities.MainActivity;
import com.android.certifications.niap.permissions.config.TestConfiguration;
import com.android.certifications.niap.permissions.log.Logger;
import com.android.certifications.niap.permissions.log.LoggerFactory;
import com.android.certifications.niap.permissions.utils.InternalPermissions;
import com.android.certifications.niap.permissions.utils.PermissionUtils;
import com.android.certifications.niap.permissions.utils.Transacts;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DevicePolicyPermissionTester extends BasePermissionTester {
    private static final String TAG = "DevicePolicyPermissionTester";
    private final Logger mLogger = LoggerFactory.createDefaultLogger(TAG);

    /**
     * Map of internal protection level permissions tto their corresponding {@link
     * PermissionTest}s.
     */
    private final Map<String, BasePermissionTester.PermissionTest> mPermissionTasks;

    public DevicePolicyPermissionTester(TestConfiguration configuration, Activity activity) {
        super(configuration, activity);
        mPermissionTasks = new HashMap<>();
        applyTests(mPermissionTasks);
    }
    public IBinder getActivityToken() {
        try {
            Field tokenField = Activity.class.getDeclaredField("mToken");
            tokenField.setAccessible(true);
            return (IBinder) tokenField.get(mActivity);
        } catch (ReflectiveOperationException e) {
            throw new UnexpectedPermissionTestFailureException(e);
        }
    }
    private void applyTests(Map<String, PermissionTest> m) {

        final String PACKAGE_NAME = mContext.getPackageName();
        final ComponentName ADMIN_COMPONENT
                = new ComponentName(PACKAGE_NAME,PACKAGE_NAME+".receivers.Admin");

        DevicePolicyManager dpm =
                (DevicePolicyManager)
                        mActivity.getSystemService(Context.DEVICE_POLICY_SERVICE);

        //mContext.getApplicationInfo();
        //int id = mAppInfo.uid;//The kernel user-ID that has been assigned to this application; currently this is not a unique ID


         //Manage Device Policy Group :
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_CAMERA,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
//                        mTransacts.invokeTransact(
//                                Transacts.DEVICE_POLICY_SERVICE,
//                                Transacts.DEVICE_POLICY_DESCRIPTOR,
//                                Transacts.setCameraDisabled,
//                                ADMIN_COMPONENT,PACKAGE_NAME,false,true
//                        );

                        dpm.setCameraDisabled(ADMIN_COMPONENT,false);
                    }
                }));

        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_ACCOUNT_MANAGEMENT,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
//                        mTransacts.invokeTransact(
//                                Transacts.DEVICE_POLICY_SERVICE,
//                                Transacts.DEVICE_POLICY_DESCRIPTOR,
//                                Transacts.setAccountManagementDisabled,
//                                ADMIN_COMPONENT,PACKAGE_NAME,"accountType",true,true
//                        );
                        dpm.setAccountManagementDisabled(ADMIN_COMPONENT,"accountType",false);
                        //
                    }
                }));

        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_APP_EXEMPTIONS,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {


                        //Failed for other reason
                        //java.lang.SecurityException: Calling identity is not authorized
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setApplicationExemptions,
                                PACKAGE_NAME,PACKAGE_NAME,new int[]{4}
                        );

                        //
                    }
                }));
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_APP_RESTRICTIONS,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        //java.lang.SecurityException: Calling identity is not authorized
//                        mTransacts.invokeTransact(
//                                Transacts.DEVICE_POLICY_SERVICE,
//                                Transacts.DEVICE_POLICY_DESCRIPTOR,
//                                Transacts.setApplicationRestrictions,ADMIN_COMPONENT,
//                                PACKAGE_NAME,PACKAGE_NAME,new Bundle()
//                        );
                        dpm.setApplicationRestrictions(ADMIN_COMPONENT,PACKAGE_NAME,new Bundle());
                        //
                    }
                }));

        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_APPS_CONTROL,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {

//                        mTransacts.invokeTransact(
//                                Transacts.DEVICE_POLICY_SERVICE,
//                                Transacts.DEVICE_POLICY_DESCRIPTOR,
//                                Transacts.setUserControlDisabledPackages,ADMIN_COMPONENT,
//                                PACKAGE_NAME,List.of("com.package","com.package2")
//                        );

                        dpm.setUserControlDisabledPackages(ADMIN_COMPONENT,List.of("com.package","com.package2"));
                    }
                }));

        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_CERTIFICATES,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {

                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.installKeyPair,ADMIN_COMPONENT, PACKAGE_NAME,
                                //privKey, cert, chain, alias, requestAccess,isUserSelectable
                                new byte[]{0,0,0,0},new byte[]{0,0,0,0},new byte[]{0,0,0,0},"alias",true,true
                        );
                        //
                    }
                }));

        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_COMMON_CRITERIA_MODE,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {

//                        mTransacts.invokeTransact(
//                                Transacts.DEVICE_POLICY_SERVICE,
//                                Transacts.DEVICE_POLICY_DESCRIPTOR,
//                                Transacts.setCommonCriteriaModeEnabled,
//                                ADMIN_COMPONENT, PACKAGE_NAME,true
//                        );

                        dpm.setCommonCriteriaModeEnabled(ADMIN_COMPONENT,true);
                        //
                    }
                }));
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_DEFAULT_SMS,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
//                        mTransacts.invokeTransact(
//                                Transacts.DEVICE_POLICY_SERVICE,
//                                Transacts.DEVICE_POLICY_DESCRIPTOR,
//                                Transacts.setDefaultSmsApplication,
//                                ADMIN_COMPONENT, PACKAGE_NAME,
//                                "sms.packagename",true
//                        );
                        dpm.setDefaultSmsApplication(ADMIN_COMPONENT,"sms.packagename");
                        //
                    }
                }));

        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_FACTORY_RESET,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setFactoryResetProtectionPolicy,
                                ADMIN_COMPONENT, PACKAGE_NAME,
                                null
                        );
                        //
                    }
                }));
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_INPUT_METHODS,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {

                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setPermittedInputMethods,
                                ADMIN_COMPONENT, PACKAGE_NAME,
                                List.of("com.package","com.package2"),true
                        );
                        //
                    }
                }));

        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_KEYGUARD,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        Object CONFIG = stubHiddenObject("android.os.PersistableBundle");
                        if(CONFIG != null) {
                            ComponentName TRUST_AGENT_COMPONENT =
                                    new ComponentName("com.trustagent", "com.trustagent.xxx");
                            mTransacts.invokeTransact(
                                    Transacts.DEVICE_POLICY_SERVICE,
                                    Transacts.DEVICE_POLICY_DESCRIPTOR,
                                    Transacts.setTrustAgentConfiguration,
                                    ADMIN_COMPONENT, PACKAGE_NAME, TRUST_AGENT_COMPONENT,
                                    CONFIG, true
                            );
                        } else {
                            throw new IllegalArgumentException("failed to create a PersistableBundle");
                        }
                        //
                    }
                }));

        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_LOCK,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {

                        //KEYGUARD_DISABLE_FINGERPRINT
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setMaximumTimeToLock,
                                ADMIN_COMPONENT, PACKAGE_NAME,
                                1000*30,false/* parent false=success, */
                        );
                        //
                    }
                }));

        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_LOCK_CREDENTIALS,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        //KEYGUARD_DISABLE_FINGERPRINT
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setPasswordExpirationTimeout,
                                ADMIN_COMPONENT, PACKAGE_NAME,
                                30,true
                        );
                        //
                    }
                }));

        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_LOCK_TASK,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        //KEYGUARD_DISABLE_FINGERPRINT
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setLockTaskPackages,
                                ADMIN_COMPONENT, PACKAGE_NAME,
                                List.of("com.package","com.package2").toArray()
                        );
                        //
                    }
                }));
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_MTE,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        try {
                            mTransacts.invokeTransact(
                                    Transacts.DEVICE_POLICY_SERVICE,
                                    Transacts.DEVICE_POLICY_DESCRIPTOR,
                                    Transacts.setMtePolicy,
                                    DevicePolicyManager.MTE_NOT_CONTROLLED_BY_POLICY,
                                    PACKAGE_NAME
                            );

                        } catch (UnsupportedOperationException ex){
                            throw new BypassTestException("The device not support MTE.("+ex.getMessage()+")");
                        }
                    }
                }));

        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_ORGANIZATION_IDENTITY,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {

                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setOrganizationName,
                                ADMIN_COMPONENT,
                                PACKAGE_NAME, "Organization Name".toCharArray()
                        );

                    }
                }));

        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_PACKAGE_STATE,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {

                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.isPackageSuspended,
                                ADMIN_COMPONENT,
                                PACKAGE_NAME, "com.google.android.youtube"
                        );

                    }
                }));

        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_PROFILE_INTERACTION,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {

                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.addCrossProfileWidgetProvider,
                                ADMIN_COMPONENT,
                                PACKAGE_NAME,"com.packagename"
                        );

                    }
                }));

        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_RESET_PASSWORD,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        //byte array describes 'password'x4.
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setResetPasswordToken,
                                ADMIN_COMPONENT,
                                PACKAGE_NAME,new byte[]{
                                        0x70,0x61,0x73,0x73,0x77,0x6f,0x72,0x64,
                                        0x70,0x61,0x73,0x73,0x77,0x6f,0x72,0x64,
                                        0x70,0x61,0x73,0x73,0x77,0x6f,0x72,0x64,
                                        0x70,0x61,0x73,0x73,0x77,0x6f,0x72,0x64}
                        );
                    }
                }));

        /*
        m.put(permission.MANAGE_DEVICE_POLICY_RUNTIME_PERMISSIONS,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {

                        Object callback = ReflectionUtils.stubRemoteCallback();
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setPermissionGrantState,
                                ADMIN_COMPONENT,
                                PACKAGE_NAME,
                                "com.package",ACCESS_FINE_LOCATION,
                                DevicePolicyManager.PERMISSION_GRANT_STATE_DEFAULT,
                                callback
                        );
                    }
            }));*/

        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_SCREEN_CAPTURE,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {

                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setScreenCaptureDisabled,
                                ADMIN_COMPONENT,
                                PACKAGE_NAME,false,true);
                    }
                }));

        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_SECURITY_LOGGING,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {

                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setSecurityLoggingEnabled,
                                ADMIN_COMPONENT,
                                PACKAGE_NAME,false);
                    }
                }));

        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_STATUS_BAR,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {

                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setStatusBarDisabled,
                                ADMIN_COMPONENT,
                                PACKAGE_NAME,false);
                    }
                }));
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_SUPPORT_MESSAGE,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setShortSupportMessage,
                                ADMIN_COMPONENT,
                                PACKAGE_NAME,"Hello Short Support Message!".toCharArray());
                    }
                }));

        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_SYSTEM_UPDATES,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setSystemUpdatePolicy,
                                ADMIN_COMPONENT,
                                PACKAGE_NAME, SystemUpdatePolicy.createAutomaticInstallPolicy());
                    }
                }));
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_USB_DATA_SIGNALLING,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {


                        dpm.setUsbDataSignalingEnabled(true);

                        /*mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setUsbDataSignalingEnabled,
                                PACKAGE_NAME,true);*/
                    }
                }));

        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_WIFI,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {

                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.hasLockdownAdminConfiguredNetworks,
                                ADMIN_COMPONENT);
                    }
                }));

        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_WIPE_DATA,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setMaximumFailedPasswordsForWipe,
                                ADMIN_COMPONENT,PACKAGE_NAME,3000,true);
                    }
                }));

        //UserManager + DevicePolicyService related Permissions

        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_AUDIO_OUTPUT,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setUserRestriction,
                                ADMIN_COMPONENT,PACKAGE_NAME,
                                UserManager.DISALLOW_ADJUST_VOLUME,false,true);
                    }
                }));
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_AUTOFILL,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setUserRestriction,
                                ADMIN_COMPONENT,PACKAGE_NAME,
                                UserManager.DISALLOW_AUTOFILL,false,true);
                    }
                }));

        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_BLUETOOTH,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setUserRestriction,
                                ADMIN_COMPONENT,PACKAGE_NAME,
                                UserManager.DISALLOW_BLUETOOTH,false,true);
                    }
                }));

        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_CALLS,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setUserRestriction,
                                ADMIN_COMPONENT,PACKAGE_NAME,
                                UserManager.DISALLOW_OUTGOING_CALLS,false,true);
                    }
                }));
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_DEBUGGING_FEATURES,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setUserRestriction,
                                ADMIN_COMPONENT,PACKAGE_NAME,
                                UserManager.DISALLOW_DEBUGGING_FEATURES,false,true);
                    }
                }));
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_DISPLAY,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setUserRestriction,
                                ADMIN_COMPONENT,PACKAGE_NAME,
                                UserManager.DISALLOW_AMBIENT_DISPLAY,false,true);
                    }
                }));
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_INSTALL_UNKNOWN_SOURCES,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setUserRestriction,
                                ADMIN_COMPONENT,PACKAGE_NAME,
                                UserManager.DISALLOW_INSTALL_UNKNOWN_SOURCES,false,true);
                    }
                }));
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_LOCALE,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setUserRestriction,
                                ADMIN_COMPONENT,PACKAGE_NAME,
                                UserManager.DISALLOW_CONFIG_LOCALE,false,true);
                    }
                }));
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_LOCATION,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setUserRestriction,
                                ADMIN_COMPONENT,PACKAGE_NAME,
                                UserManager.DISALLOW_CONFIG_LOCATION,false,true);
                    }
                }));
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_MOBILE_NETWORK,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setUserRestriction,
                                ADMIN_COMPONENT,PACKAGE_NAME,
                                UserManager.DISALLOW_CONFIG_MOBILE_NETWORKS,false,true);
                    }
                }));
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_MODIFY_USERS,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setUserRestriction,
                                ADMIN_COMPONENT,PACKAGE_NAME,
                                UserManager.DISALLOW_ADD_USER,false,true);
                    }
                }));
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_NEARBY_COMMUNICATION,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setUserRestriction,
                                ADMIN_COMPONENT,PACKAGE_NAME,
                                UserManager.DISALLOW_OUTGOING_BEAM,false,true);
                    }
                }));
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_PHYSICAL_MEDIA,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setUserRestriction,
                                ADMIN_COMPONENT,PACKAGE_NAME,
                                UserManager.DISALLOW_MOUNT_PHYSICAL_MEDIA,false,true);
                    }
                }));
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_PRINTING,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setUserRestriction,
                                ADMIN_COMPONENT,PACKAGE_NAME,
                                UserManager.DISALLOW_PRINTING,false,true);
                    }
                }));
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_PROFILES,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setUserRestriction,
                                ADMIN_COMPONENT,PACKAGE_NAME,
                                UserManager.ALLOW_PARENT_PROFILE_APP_LINKING,true,true);
                    }
                }));
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_RESTRICT_PRIVATE_DNS,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setUserRestriction,
                                ADMIN_COMPONENT,PACKAGE_NAME,
                                UserManager.DISALLOW_CONFIG_PRIVATE_DNS,false,true);
                    }
                }));
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_RUN_IN_BACKGROUND,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        String DISALLOW_RUN_IN_BACKGROUND = "no_run_in_background";//SYSTEM_API
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setUserRestriction,
                                ADMIN_COMPONENT,PACKAGE_NAME,
                                DISALLOW_RUN_IN_BACKGROUND,false,true);
                    }
                }));
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_SAFE_BOOT,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setUserRestriction,
                                ADMIN_COMPONENT,PACKAGE_NAME,
                                UserManager.DISALLOW_SAFE_BOOT,false,true);
                    }
                }));
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_SCREEN_CONTENT,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setUserRestriction,
                                ADMIN_COMPONENT,PACKAGE_NAME,
                                UserManager.DISALLOW_CONTENT_CAPTURE,false,true);
                    }
                }));
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_MICROPHONE,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setUserRestriction,
                                ADMIN_COMPONENT,PACKAGE_NAME,
                                UserManager.DISALLOW_MICROPHONE_TOGGLE,false,true);
                    }
                }));

        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_SMS,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setUserRestriction,
                                ADMIN_COMPONENT,PACKAGE_NAME,
                                UserManager.DISALLOW_SMS,false,true);
                    }
                }));

        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_SYSTEM_DIALOGS,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
//                        mTransacts.invokeTransact(
//                                Transacts.DEVICE_POLICY_SERVICE,
//                                Transacts.DEVICE_POLICY_DESCRIPTOR,
//                                Transacts.setUserRestriction,
//                                ADMIN_COMPONENT,PACKAGE_NAME,
//                                UserManager.DISALLOW_SYSTEM_ERROR_DIALOGS,false,true);
                        dpm.clearUserRestriction(ADMIN_COMPONENT,UserManager.DISALLOW_SYSTEM_ERROR_DIALOGS);
                    }
                }));

        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_TIME,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
//                        mTransacts.invokeTransact(
//                                Transacts.DEVICE_POLICY_SERVICE,
//                                Transacts.DEVICE_POLICY_DESCRIPTOR,
//                                Transacts.setUserRestriction,
//                                ADMIN_COMPONENT,PACKAGE_NAME,
//                                UserManager.DISALLOW_CONFIG_DATE_TIME,false,true);

                        dpm.clearUserRestriction(ADMIN_COMPONENT,UserManager.DISALLOW_CONFIG_DATE_TIME);
                    }
                }));

        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_VPN,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
//                        mTransacts.invokeTransact(
//                                Transacts.DEVICE_POLICY_SERVICE,
//                                Transacts.DEVICE_POLICY_DESCRIPTOR,
//                                Transacts.setUserRestriction,
//                                ADMIN_COMPONENT,PACKAGE_NAME,
//                                UserManager.DISALLOW_CONFIG_VPN,false,true);

                        dpm.clearUserRestriction(ADMIN_COMPONENT,UserManager.DISALLOW_CONFIG_VPN);
                    }
                }));

        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_WALLPAPER,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
//                        mTransacts.invokeTransact(
//                                Transacts.DEVICE_POLICY_SERVICE,
//                                Transacts.DEVICE_POLICY_DESCRIPTOR,
//                                Transacts.setUserRestriction,
//                                ADMIN_COMPONENT,PACKAGE_NAME,
//                                UserManager.DISALLOW_SET_WALLPAPER,false,true);

                        dpm.clearUserRestriction(ADMIN_COMPONENT,UserManager.DISALLOW_SET_WALLPAPER);
                    }
                }));

        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_WINDOWS,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
//                        mTransacts.invokeTransact(
//                                Transacts.DEVICE_POLICY_SERVICE,
//                                Transacts.DEVICE_POLICY_DESCRIPTOR,
//                                Transacts.setUserRestriction,
//                                ADMIN_COMPONENT,PACKAGE_NAME,
//                                UserManager.DISALLOW_CREATE_WINDOWS,false,true);

                        dpm.clearUserRestriction(ADMIN_COMPONENT,UserManager.DISALLOW_CREATE_WINDOWS);
                    }
                }));

        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_FUN,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {

                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setUserRestriction,
                                ADMIN_COMPONENT,PACKAGE_NAME,
                                UserManager.DISALLOW_FUN,false,true);
                    }
                }));


        //MANAGE_DEVICE_POLICY_* stuffs from Android 15
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_THREAD_NETWORK,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    mLogger.logDebug("Test case for android.permission.MANAGE_DEVICE_POLICY_THREAD_NETWORK not implemented yet");
                    //mTransacts.invokeTransact(Transacts.SERVICE, Transacts.DESCRIPTOR,
                    //       Transacts.unregisterCoexCallback, (Object) null);
                }));
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_ASSIST_CONTENT,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    mLogger.logDebug("Test case for android.permission.MANAGE_DEVICE_POLICY_ASSIST_CONTENT not implemented yet");
                    //mTransacts.invokeTransact(Transacts.SERVICE, Transacts.DESCRIPTOR,
                    //       Transacts.unregisterCoexCallback, (Object) null);
                }));
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_AUDIT_LOGGING,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    //mLogger.logDebug("Test case for android.permission.MANAGE_DEVICE_POLICY_AUDIT_LOGGING not implemented yet");
                    //Hidden Method
                    //mTransacts.invokeTransact(Transacts.SERVICE, Transacts.DESCRIPTOR,
                    //       Transacts.unregisterCoexCallback, (Object) null);

                }));

        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_CONTENT_PROTECTION,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    //mLogger.logDebug("Test case for android.permission.MANAGE_DEVICE_POLICY_CONTENT_PROTECTION not implemented yet");
                    //mTransacts.invokeTransact(Transacts.SERVICE, Transacts.DESCRIPTOR,
                    //       Transacts.unregisterCoexCallback, (Object) null)
                    int flag = 1 << 7;//See enterprisepolicy.java
                    dpm.setContentProtectionPolicy(ADMIN_COMPONENT,flag);
                }));
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_MANAGED_SUBSCRIPTIONS,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    //mLogger.logDebug("Test case for android.permission.MANAGE_DEVICE_POLICY_MANAGED_SUBSCRIPTIONS not implemented yet");
                    dpm.setUninstallBlocked(ADMIN_COMPONENT,mPackageName,true);
                }));
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_BLOCK_UNINSTALL,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    mLogger.logDebug("Test case for android.permission.MANAGE_DEVICE_POLICY_BLOCK_UNINSTALL not implemented yet");
                    //Hidden method dpm.forceSetMaxPolicyStorageLimit
                    //mTransacts.invokeTransact(Transacts.SERVICE, Transacts.DESCRIPTOR,
                    //       Transacts.unregisterCoexCallback, (Object) null);

                }));
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_CAMERA_TOGGLE,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    mLogger.logDebug("Test case for android.permission.MANAGE_DEVICE_POLICY_CAMERA_TOGGLE not implemented yet");
                    //mTransacts.invokeTransact(Transacts.SERVICE, Transacts.DESCRIPTOR,
                    //       Transacts.unregisterCoexCallback, (Object) null);
                }));
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_MICROPHONE_TOGGLE,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    mLogger.logDebug("Test case for android.permission.MANAGE_DEVICE_POLICY_MICROPHONE_TOGGLE not implemented yet");
                    //mTransacts.invokeTransact(Transacts.SERVICE, Transacts.DESCRIPTOR,
                    //       Transacts.unregisterCoexCallback, (Object) null);
                }));
        m.put(InternalPermissions.permission.MANAGE_DEVICE_POLICY_STORAGE_LIMIT,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    mLogger.logDebug("Test case for android.permission.MANAGE_DEVICE_POLICY_STORAGE_LIMIT not implemented yet");
                    //mTransacts.invokeTransact(Transacts.SERVICE, Transacts.DESCRIPTOR,
                    //       Transacts.unregisterCoexCallback, (Object) null);
                }));
    }



    @Override
    public boolean runPermissionTests() {
        return false;
    }

    @Override
    public void runPermissionTestsByThreads(Consumer<Result> callback) {
        Result.testerName = this.getClass().getSimpleName();

        List<String> permissions = mConfiguration.getInternalPermissions().orElse(
                new ArrayList<>(mPermissionTasks.keySet()));

        int no=0;
        AtomicInteger cnt = new AtomicInteger(0);
        AtomicInteger err = new AtomicInteger(0);

        final int total = permissions.size();
        for (String permission : permissions) {
            Thread thread = new Thread(() -> {
                String tester = this.getClass().getSimpleName();
                if (runPermissionTest(permission, mPermissionTasks.get(permission), true)) {
                    callback.accept(new Result(true, permission, aiIncl(cnt), total,err.get(),tester));
                } else {
                    callback.accept(new Result(false, permission, aiIncl(cnt), total,aiIncl(err),tester));
                }
            });
            thread.start();
            try {
                thread.join(THREAD_JOIN_DELAY);
            } catch (InterruptedException e) {
                mLogger.logError(String.format(Locale.US,"%d %s failed due to the timeout.",no,permission));
            }
        }
    }
    @Override
    public boolean isPermissionGranted(String permission) {
        boolean r1 = mContext.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;

        boolean r3 = mContext.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;

        final String PACKAGE_NAME = mContext.getPackageName();
        final ComponentName ADMIN_COMPONENT
                = new ComponentName(PACKAGE_NAME,PACKAGE_NAME+".receivers.Admin");


        boolean r2 = PermissionUtils.ensureRequiredPermissions(new String[]{permission}
                    ,mContext
                );

        mLogger.logDebug(String.format("Permission %s checkSelf=%b ensure=%b calling=%b",permission,r1,r2,r3));

        return r2;//mContext.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }
    @Override
    public Map<String, PermissionTest> getRegisteredPermissions() {
        return mPermissionTasks;
    }
}
