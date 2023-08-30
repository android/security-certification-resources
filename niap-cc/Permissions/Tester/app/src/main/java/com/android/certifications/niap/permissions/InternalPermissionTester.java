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

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.LAUNCH_CAPTURE_CONTENT_ACTIVITY_FOR_NOTE;
import static android.Manifest.permission.SCHEDULE_EXACT_ALARM;
import static android.content.Intent.ACTION_LAUNCH_CAPTURE_CONTENT_ACTIVITY_FOR_NOTE;
import static com.android.certifications.niap.permissions.utils.InternalPermissions.permission;
import static com.android.certifications.niap.permissions.utils.ReflectionUtils.invokeReflectionCall;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.app.admin.SystemUpdatePolicy;
import android.companion.AssociationRequest;
import android.companion.CompanionDeviceManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;

import com.android.certifications.niap.permissions.activities.MainActivity;
import com.android.certifications.niap.permissions.activities.TestActivity;
import com.android.certifications.niap.permissions.config.TestConfiguration;
import com.android.certifications.niap.permissions.log.Logger;
import com.android.certifications.niap.permissions.log.LoggerFactory;
import com.android.certifications.niap.permissions.log.StatusLogger;
import com.android.certifications.niap.permissions.utils.ReflectionUtils;
import com.android.certifications.niap.permissions.utils.SignaturePermissions;
import com.android.certifications.niap.permissions.utils.SignatureUtils;
import com.android.certifications.niap.permissions.utils.Transacts;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;


/**
 * Permission tester to verify all platform declared internal permissions properly guard their API,
 * resources, etc. An internal protection level permission is different from a signature permission
 * in that the requesting app will not be granted the permission just by being signed with the same
 * signing key as the platform, but instead must meet other requirements as defined by the
 * permission's flags.
 */
public class InternalPermissionTester extends BasePermissionTester {
    private static final String TAG = "InternalPermissionTester";
    private final Logger mLogger = LoggerFactory.createDefaultLogger(TAG);

    /**
     * Map of internal protection level permissions tto their corresponding {@link
     * PermissionTest}s.
     */
    private final Map<String, BasePermissionTester.PermissionTest> mPermissionTasks;

    public InternalPermissionTester(TestConfiguration configuration, Activity activity) {
        super(configuration, activity);

        ReflectionUtils.deviceConfigSetProperty(
                "device_policy_manager",
                "enable_device_policy_engine","true",false
        );
        ReflectionUtils.deviceConfigSetProperty(
                "device_policy_manager",
                "enable_permission_based_access","true",false
        );

        final String PACKAGE_NAME = mContext.getPackageName();
        final ComponentName ADMIN_COMPONENT
                = new ComponentName(PACKAGE_NAME,PACKAGE_NAME+".receivers.Admin");

        mPermissionTasks = new HashMap<>();

        mPermissionTasks.put(permission.MANAGE_HOTWORD_DETECTION,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    try {
                        mTransacts.invokeTransact(Transacts.VOICE_INTERACTION_SERVICE,
                                Transacts.VOICE_INTERACTION_DESCRIPTOR, Transacts.updateState, null,
                                null, null);
                    } catch (SecurityException e) {
                        // Note, there are two places where this transact can throw a
                        // SecurityException; the first is during the permission check, the second
                        // is after the permission check is successful when determining if the
                        // current caller is the VoiceInteractionService. This could be flaky but
                        // treat the API as successful if the error message indicates the caller
                        // is not the VoiceInteractionService.
                        if (!e.getMessage().contains(
                                "Caller is not the current voice interaction service")) {
                            throw e;
                        } else {
                            mLogger.logDebug(
                                    "MANAGE_HOTWORD_DETECTION passed permission check, caught the"
                                            + " following exception: ", e);
                        }
                    }
                }));

        mPermissionTasks.put(permission.OBSERVE_SENSOR_PRIVACY,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.SENSOR_PRIVACY_SERVICE,
                            Transacts.SENSOR_PRIVACY_DESCRIPTOR, Transacts.isSensorPrivacyEnabled);
                }));

        mPermissionTasks.put(permission.DOMAIN_VERIFICATION_AGENT,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.DOMAIN_VERIFICATION_SERVICE,
                            Transacts.DOMAIN_VERIFICATION_DESCRIPTOR,
                            Transacts.queryValidVerificationPackageNames);
                }));

        mPermissionTasks.put(permission.ACCESS_RCS_USER_CAPABILITY_EXCHANGE,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.TELEPHONY_IMS_SERVICE,
                            Transacts.TELEPHONY_IMS_DESCRIPTOR, Transacts.requestAvailability, 0,
                            mPackageName, null,
                            null, null);
                }));

        mPermissionTasks.put(permission.ASSOCIATE_COMPANION_DEVICES,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    Signature signature = SignatureUtils.getTestAppSigningCertificate(mContext);
                    MessageDigest messageDigest;
                    try {
                        messageDigest = MessageDigest.getInstance("SHA-256");
                    } catch (NoSuchAlgorithmException e) {
                        throw new UnexpectedPermissionTestFailureException(e);
                    }
                    byte[] certDigest = messageDigest.digest(signature.toByteArray());
                    if (!mPackageManager.hasSigningCertificate(mPackageName, certDigest,
                            PackageManager.CERT_INPUT_SHA256)) {
                        throw new UnexpectedPermissionTestFailureException(
                                "PackageManager reported test app does not have the provided "
                                        + "signing certificate");
                    }
                    mTransacts.invokeTransact(Transacts.COMPANION_DEVICE_SERVICE,
                            Transacts.COMPANION_DEVICE_DESCRIPTOR, Transacts.createAssociation,
                            mPackageName, "11:22:33:44:55:66", 0, certDigest);
                }));

        mPermissionTasks.put(permission.BYPASS_ROLE_QUALIFICATION,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.ROLE_SERVICE, Transacts.ROLE_DESCRIPTOR,
                            Transacts.setBypassingRoleQualification, false);
                }));

        mPermissionTasks.put(permission.PERFORM_IMS_SINGLE_REGISTRATION,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.TELEPHONY_IMS_SERVICE,
                            Transacts.TELEPHONY_IMS_DESCRIPTOR,
                            Transacts.triggerNetworkRegistration, 0,
                            getActivityToken(), 0, "test-sip-reason");
                }));


        //Internal Permissions as of Android 13
        //permission.READ_ASSISTANT_APP_SEARCH_DATA depends on specific app role
        //permission.READ_HOME_APP_SEARCH_DATA depends on specific app role

        mPermissionTasks.put(permission.SET_DEFAULT_ACCOUNT_FOR_CONTACTS,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        Bundle extras = new Bundle();
                        mContentResolver.call(ContactsContract.AUTHORITY_URI, "setDefaultAccount", null,
                                extras);
                        }
                }));

        mPermissionTasks.put(permission.SUBSCRIBE_TO_KEYGUARD_LOCKED_STATE,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU,
                        Build.VERSION_CODES.TIRAMISU, () -> {
                    //WindowManagerService#addKeyguardLockedStateListener.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        KeyguardManager.KeyguardLockedStateListener listener = new KeyguardManager.KeyguardLockedStateListener() {
                            @Override
                            public void onKeyguardLockedStateChanged(boolean b) {
                            }
                        };
                        mTransacts.invokeTransact(Transacts.WINDOW_SERVICE, Transacts.WINDOW_DESCRIPTOR,
                                Transacts.addKeyguardLockedStateListener, listener);
                    }
                }));

        mPermissionTasks.put(permission.CREATE_VIRTUAL_DEVICE,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    try {

                        Class<?> clazzVDPBuilder = null;
                        clazzVDPBuilder = Class.forName("android.companion.virtual.VirtualDeviceParams$Builder");
                        Constructor constructor = clazzVDPBuilder.getConstructor();
                        Object builderObj = constructor.newInstance();

                        Object vdpParams =
                                invokeReflectionCall(clazzVDPBuilder, "build", builderObj, new Class[]{});

                        IBinder binder = getActivityToken();
                        //UserHandle uh = Binder.getCallingUserHandle();
                        mTransacts.invokeTransact(Transacts.VIRTUAL_DEVICE_MANAGER_SERVICE,
                                Transacts.VIRTUAL_DEVICE_MANAGER_DESCRIPTOR,
                                Transacts.createVirtualDevice, binder,mContext.getPackageName(),
                                0, vdpParams,null);

                    } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
                        throw new UnexpectedPermissionTestFailureException(e);
                    }

                }));

        mPermissionTasks.put(permission.SEND_SAFETY_CENTER_UPDATE,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    Class<?> clazzSaftyCenter;
                    try {
                        clazzSaftyCenter = Class.forName("android.safetycenter.SafetyCenterManager");
                        Object saftyCenter = mContext.getSystemService(clazzSaftyCenter);
                        invokeReflectionCall
                                (clazzSaftyCenter, "getSafetySourceData",
                                        saftyCenter, new Class[]{String.class},"GooglePlaySystemUpdate");
                    } catch (ClassNotFoundException e){
                        throw new UnexpectedPermissionTestFailureException(e);
                    }
                })
        );

        mPermissionTasks.put(permission.REQUEST_COMPANION_PROFILE_AUTOMOTIVE_PROJECTION,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    if (!mPackageManager.hasSystemFeature(
                            PackageManager.FEATURE_COMPANION_DEVICE_SETUP)) {
                        throw new BypassTestException(
                                "Device does not have the "
                                        + "PackageManager#FEATURE_COMPANION_DEVICE_SETUP feature "
                                        + "for this test");
                    }
                    AssociationRequest request;
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        request = new AssociationRequest.Builder().setDeviceProfile(
                                AssociationRequest.DEVICE_PROFILE_AUTOMOTIVE_PROJECTION).build();
                        CompanionDeviceManager.Callback callback =
                                new CompanionDeviceManager.Callback() {
                                    @Override
                                    public void onDeviceFound(IntentSender intentSender) {
                                        mLogger.logDebug(
                                                "onDeviceFound: intentSender = " + intentSender);
                                    }
                                    @Override
                                    public void onFailure(CharSequence charSequence) {
                                        mLogger.logDebug("onFailure: charSequence = " + charSequence);
                                    }
                                };
                        CompanionDeviceManager companionDeviceManager = mActivity.getSystemService(
                                CompanionDeviceManager.class);

                        companionDeviceManager.associate(request, callback, null);
                    }

                }));

        //Move to Signature Permission from U
        mPermissionTasks.put(permission.ACCESS_AMBIENT_CONTEXT_EVENT,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU,
                        Build.VERSION_CODES.TIRAMISU, () -> {

                    @SuppressLint("WrongConstant") Object ambientContextManager
                            = mContext.getSystemService("ambient_context");
                            //Context.AMBIENT_CONTEXT_SERVICE);

                    int[] eventsArray = new int[] {-1};//AmbientContextEvent.EVENT_COUGH
                    Set<Integer> eventTypes = Arrays.stream(eventsArray).boxed().collect(
                            Collectors.toSet());

                    invokeReflectionCall(ambientContextManager.getClass(),
                            "queryAmbientContextServiceStatus",
                            ambientContextManager, new Class[]{Set.class, Executor.class,
                                    Consumer.class}, eventTypes, new Executor() {
                                @Override
                                public void execute(Runnable runnable) {

                                }
                            }, null);

                }));

        // # Skip READ_ASSISTANT_APP_SEARCH_DATA
        //Reason : Need specific role to test this permission.

        // # Skip READ_HOME_APP_SEARCH_DATA
        //Reason : Need specific role to test this permission.

        mPermissionTasks.put(permission.MANAGE_SAFETY_CENTER,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU,() -> {
                    mTransacts.invokeTransact(Transacts.SAFETY_CENTER_MANAGER_SERVICE,
                            Transacts.SAFETY_CENTER_MANAGER_MANAGER_DESCRIPTOR,
                            Transacts.getSafetyCenterConfig);
                }));

        //The permission is moved from signature permission (since android s)
        mPermissionTasks.put(SignaturePermissions.permission.MANAGE_SENSOR_PRIVACY,
                new PermissionTest(false, Build.VERSION_CODES.S,() -> {
                    // ISensorPrivacyManager#setSensorPrivacy
                    mTransacts.invokeTransact(Transacts.SENSOR_PRIVACY_SERVICE,
                            Transacts.SENSOR_PRIVACY_DESCRIPTOR,
                            Transacts.setSensorPrivacy, false);
                }));

        //The permission is moved from signature permission (since android t)
        mPermissionTasks.put(SignaturePermissions.permission.TOGGLE_AUTOMOTIVE_PROJECTION,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    mTransacts.invokeTransact(Transacts.UI_MODE_SERVICE,
                            Transacts.UI_MODE_DESCRIPTOR, Transacts.requestProjection,
                            getActivityToken(), 1, mPackageName);
                }));



        //Android 14 Implementations//

        mPermissionTasks.put(permission.READ_RESTRICTED_STATS,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= 34) {

                        String STATS_MANAGER = "stats";

                        Intent i = new Intent(mContext, TestActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, i, PendingIntent.FLAG_IMMUTABLE);

                        mTransacts.invokeTransact(
                                STATS_MANAGER,
                                Transacts.STATS_DESCRIPTOR,
                                Transacts.setRestrictedMetricsChangedOperation,
                                pendingIntent,1 ,mContext.getPackageName()
                        );
                    }
                }));



        mPermissionTasks.put(permission.LAUNCH_CAPTURE_CONTENT_ACTIVITY_FOR_NOTE,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    //commonize the tester routine with exposing the builder of AssociationRequest object
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        Intent featuresIntent = new Intent(ACTION_LAUNCH_CAPTURE_CONTENT_ACTIVITY_FOR_NOTE)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        final PackageManager mgr = mContext.getPackageManager();

                        featuresIntent.setComponent(new
                                ComponentName("com.android.systemui",
                                "com.android.systemui.screenshot.appclips.AppClipsService"));

                        List<ResolveInfo> list =
                                mgr.queryIntentActivities(featuresIntent,
                                        PackageManager.MATCH_DEFAULT_ONLY);
                        if(list.size()>0){
                            mActivity.startActivity(featuresIntent);
                        } else {
                            throw new BypassTestException("AppClipService doesn't exist in this device.");
                        }

                    }
                }));

        mPermissionTasks.put(permission.MANAGE_DEVICE_LOCK_STATE,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {

                        //import android.devicelock.IIsDeviceLockedCallback;
                        //MANAGE_DEVICE_LOCK_STATE
                        mTransacts.invokeTransact(
                                Context.DEVICE_LOCK_SERVICE,
                                Transacts.DEVICELOCK_DESCRIPTOR,
                                Transacts.isDeviceLocked,(Object) getActivityToken());

                    }
                }));

        //Manage Device Policy Group :
        mPermissionTasks.put(permission.MANAGE_DEVICE_POLICY_CAMERA,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= 34) {
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setCameraDisabled,
                                ADMIN_COMPONENT,PACKAGE_NAME,true,true
                        );
                    }
                }));

        mPermissionTasks.put(permission.MANAGE_DEVICE_POLICY_ACCOUNT_MANAGEMENT,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= 34) {
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setAccountManagementDisabled,
                                ADMIN_COMPONENT,PACKAGE_NAME,"accountType",true,true
                        );
                        //
                    }
                }));
        mPermissionTasks.put(permission.MANAGE_DEVICE_POLICY_APP_EXEMPTIONS,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= 34) {


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
        mPermissionTasks.put(permission.MANAGE_DEVICE_POLICY_APP_RESTRICTIONS,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= 34) {

                        ReflectionUtils.deviceConfigSetProperty(
                                "device_policy_manager",
                                "enable_device_policy_engine","true",false
                        );
                        //Failed for other reason
                        //java.lang.SecurityException: Calling identity is not authorized
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setApplicationRestrictions,ADMIN_COMPONENT,
                                PACKAGE_NAME,PACKAGE_NAME,new Bundle()
                        );
                        //
                    }
                }));

        mPermissionTasks.put(permission.MANAGE_DEVICE_POLICY_APPS_CONTROL,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= 34) {

                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setUserControlDisabledPackages,ADMIN_COMPONENT,
                                PACKAGE_NAME,List.of("com.package","com.package2")
                        );
                    }
                }));

        mPermissionTasks.put(permission.MANAGE_DEVICE_POLICY_CERTIFICATES,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= 34) {

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

        mPermissionTasks.put(permission.MANAGE_DEVICE_POLICY_COMMON_CRITERIA_MODE,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= 34) {

                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setCommonCriteriaModeEnabled,
                                ADMIN_COMPONENT, PACKAGE_NAME,true
                        );
                        //
                    }
                }));
        mPermissionTasks.put(permission.MANAGE_DEVICE_POLICY_DEFAULT_SMS,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= 34) {

                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setDefaultSmsApplication,
                                ADMIN_COMPONENT, PACKAGE_NAME,
                                "sms.packagename",true
                        );
                        //
                    }
                }));

        mPermissionTasks.put(permission.MANAGE_DEVICE_POLICY_FACTORY_RESET,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= 34) {

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
        mPermissionTasks.put(permission.MANAGE_DEVICE_POLICY_INPUT_METHODS,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= 34) {

                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setPermittedInputMethods,
                                ADMIN_COMPONENT, PACKAGE_NAME,
                                List.of("com.package","com.package2"),false
                        );
                        //
                    }
                }));

        mPermissionTasks.put(permission.MANAGE_DEVICE_POLICY_KEYGUARD,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= 34) {

                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setKeyguardDisabledFeatures,
                                ADMIN_COMPONENT, PACKAGE_NAME,
                                0x00000020,false
                        );
                        //
                    }
                }));

        mPermissionTasks.put(permission.MANAGE_DEVICE_POLICY_LOCK,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= 34) {

                        //KEYGUARD_DISABLE_FINGERPRINT
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setMaximumTimeToLock,
                                ADMIN_COMPONENT, PACKAGE_NAME,
                                1000*30,false
                        );
                        //
                    }
                }));

        mPermissionTasks.put(permission.MANAGE_DEVICE_POLICY_LOCK_CREDENTIALS,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= 34) {
                        //KEYGUARD_DISABLE_FINGERPRINT
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setPasswordExpirationTimeout,
                                ADMIN_COMPONENT, PACKAGE_NAME,
                                30,false
                        );
                        //
                    }
                }));

        mPermissionTasks.put(permission.MANAGE_DEVICE_POLICY_LOCK_TASK,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= 34) {
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
        mPermissionTasks.put(permission.MANAGE_DEVICE_POLICY_MTE,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= 34) {
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

        mPermissionTasks.put(permission.MANAGE_DEVICE_POLICY_ORGANIZATION_IDENTITY,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= 34) {

                            mTransacts.invokeTransact(
                                    Transacts.DEVICE_POLICY_SERVICE,
                                    Transacts.DEVICE_POLICY_DESCRIPTOR,
                                    Transacts.setOrganizationName,
                                    ADMIN_COMPONENT,
                                    PACKAGE_NAME, "Google LLC".toCharArray()
                            );

                    }
                }));

        mPermissionTasks.put(permission.MANAGE_DEVICE_POLICY_PACKAGE_STATE,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= 34) {

                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.isPackageSuspended,
                                ADMIN_COMPONENT,
                                PACKAGE_NAME, "com.google.android.youtube"
                        );

                    }
                }));

        mPermissionTasks.put(permission.MANAGE_DEVICE_POLICY_PROFILE_INTERACTION,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= 34) {

                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.addCrossProfileWidgetProvider,
                                ADMIN_COMPONENT,
                                PACKAGE_NAME,"com.packagename"
                        );

                    }
                }));

        mPermissionTasks.put(permission.MANAGE_DEVICE_POLICY_RESET_PASSWORD,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= 34) {
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

        mPermissionTasks.put(permission.MANAGE_DEVICE_POLICY_RUNTIME_PERMISSIONS,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= 34) {

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
                }));

        mPermissionTasks.put(permission.MANAGE_DEVICE_POLICY_SCREEN_CAPTURE,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= 34) {

                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setScreenCaptureDisabled,
                                ADMIN_COMPONENT,
                                PACKAGE_NAME,false,true);
                    }
                }));

        mPermissionTasks.put(permission.MANAGE_DEVICE_POLICY_SECURITY_LOGGING,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= 34) {

                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setSecurityLoggingEnabled,
                                ADMIN_COMPONENT,
                                PACKAGE_NAME,true);
                    }
                }));

        mPermissionTasks.put(permission.MANAGE_DEVICE_POLICY_STATUS_BAR,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= 34) {

                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setStatusBarDisabled,
                                ADMIN_COMPONENT,
                                PACKAGE_NAME,false);
                    }
                }));
        mPermissionTasks.put(permission.MANAGE_DEVICE_POLICY_SUPPORT_MESSAGE,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= 34) {
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setShortSupportMessage,
                                ADMIN_COMPONENT,
                                PACKAGE_NAME,"Hello Short Support Message!".toCharArray());
                    }
                }));

        mPermissionTasks.put(permission.MANAGE_DEVICE_POLICY_SYSTEM_UPDATES,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= 34) {
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setSystemUpdatePolicy,
                                ADMIN_COMPONENT,
                                PACKAGE_NAME,SystemUpdatePolicy.createAutomaticInstallPolicy());
                    }
                }));
        mPermissionTasks.put(permission.MANAGE_DEVICE_POLICY_USB_DATA_SIGNALLING,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= 34) {

                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setUsbDataSignalingEnabled,
                                PACKAGE_NAME,false);
                    }
                }));

        mPermissionTasks.put(permission.MANAGE_DEVICE_POLICY_WIFI,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= 34) {

                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.hasLockdownAdminConfiguredNetworks,
                                ADMIN_COMPONENT);
                    }
                }));

        mPermissionTasks.put(permission.MANAGE_DEVICE_POLICY_WIPE_DATA,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= 34) {
                        mTransacts.invokeTransact(
                                Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setMaximumFailedPasswordsForWipe,
                                ADMIN_COMPONENT,PACKAGE_NAME,3000,true);
                    }
                }));
    }

    @Override
    public boolean runPermissionTests() {
        boolean allTestsPassed = true;
        List<String> permissions = mConfiguration.getInternalPermissions().orElse(
                new ArrayList<>(mPermissionTasks.keySet()));

        for (String permission : permissions) {
            BasePermissionTester.PermissionTest t = mPermissionTasks.get(permission);
            boolean testPassed = true;
            //System.out.println(permission+","+mPermissionTasks.size()+"/"+t);
            if(t != null) {
                testPassed = runPermissionTest(permission, mPermissionTasks.get(permission),
                        true);
            } else {
                testPassed = false;
                //mLogger.logInfo(permission+","+mPermissionTasks.size()+"/"+mPermissionTasks.keySet().toString());
                mLogger.logInfo(
                        "The test case for "+permission+" is not found");
            }
            if (!testPassed) {
                allTestsPassed = false;
            }
        }
        if (allTestsPassed) {
            mLogger.logInfo(
                    "*** PASSED - all internal permission tests completed successfully");
        } else {
            mLogger.logInfo(
                    "!!! FAILED - one or more internal permission tests failed");
        }
        return allTestsPassed;
    }
    public void runPermissionTestsByThreads(androidx.core.util.Consumer<Result> callback){
        Result.testerName = this.getClass().getSimpleName();

        List<String> permissions = mConfiguration.getInternalPermissions().orElse(
                new ArrayList<>(mPermissionTasks.keySet()));
        int numperms = permissions.size();
        int no=0;
        AtomicInteger cnt = new AtomicInteger(0);
        final int total = permissions.size();
        for (String permission : permissions) {
            // If the permission has a corresponding task then run it.
//            mLogger.logDebug("Starting test for internal permission: "+String.format(Locale.US,
//                    "%d/%d ",no,numperms) + permission);
            Thread thread = new Thread(() -> {
                if (runPermissionTest(permission, mPermissionTasks.get(permission), true)) {
                    callback.accept(new Result(true, permission, aiIncl(cnt), total));
                } else {
                    callback.accept(new Result(false, permission, aiIncl(cnt), total));
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
    public Map<String,PermissionTest> getRegisteredPermissions() {
        return mPermissionTasks;
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
            throw new UnexpectedPermissionTestFailureException(e);
        }
    }
}
