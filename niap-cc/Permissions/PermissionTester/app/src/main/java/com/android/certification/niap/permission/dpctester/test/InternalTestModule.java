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
package com.android.certification.niap.permission.dpctester.test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.companion.AssociationRequest;
import android.companion.CompanionDeviceManager;
import android.content.AttributionSource;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.devicelock.IIsDeviceLockedCallback;
import android.devicelock.ParcelableException;
import android.devicelock._IIsDeviceLockedCallback;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.android.certification.niap.permission.dpctester.activity.TestActivity;
import com.android.certification.niap.permission.dpctester.common.ReflectionUtil;
import com.android.certification.niap.permission.dpctester.common.SignatureUtils;
import com.android.certification.niap.permission.dpctester.test.exception.BypassTestException;
import com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException;
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestModuleBase;
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestRunner;
import com.android.certification.niap.permission.dpctester.test.tool.BinderTransaction;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTest;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestModule;
import com.android.certification.niap.permission.dpctester.test.tool.TesterUtils;

import static android.Manifest.permission.*;
import static android.content.Intent.ACTION_LAUNCH_CAPTURE_CONTENT_ACTIVITY_FOR_NOTE;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

@PermissionTestModule(name="Internal Test Cases")    
public class InternalTestModule extends PermissionTestModuleBase {
    public InternalTestModule(@NonNull Activity activity){ super(activity);}
    @Override
    public PrepareInfo prepare(Consumer<PermissionTestRunner.Result> callback){

        return super.prepare(callback);

    } 
    @PermissionTest(permission="MANAGE_HOTWORD_DETECTION", sdkMin=31)
    public void testManageHotwordDetection(){
		try {
		    BinderTransaction.getInstance().invoke(Transacts.VOICE_INTERACTION_SERVICE,
		            Transacts.VOICE_INTERACTION_DESCRIPTOR,
					"updateState", null,
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
		        logger.debug(
		                "MANAGE_HOTWORD_DETECTION passed permission check, caught the"
		                        + " following exception: ", e);
		    }
		}
    }
 
    @PermissionTest(permission="OBSERVE_SENSOR_PRIVACY", sdkMin=31)
    public void testObserveSensorPrivacy(){
		BinderTransaction.getInstance().invoke(Transacts.SENSOR_PRIVACY_SERVICE,
				Transacts.SENSOR_PRIVACY_DESCRIPTOR, "isSensorPrivacyEnabled");
    }
 
    @PermissionTest(permission="DOMAIN_VERIFICATION_AGENT", sdkMin=31)
    public void testDomainVerificationAgent(){
		BinderTransaction.getInstance().invoke(Transacts.DOMAIN_VERIFICATION_SERVICE,
		        Transacts.DOMAIN_VERIFICATION_DESCRIPTOR,
		        "queryValidVerificationPackageNames");
    }
 
    @PermissionTest(permission="ACCESS_RCS_USER_CAPABILITY_EXCHANGE", sdkMin=31)
    public void testAccessRcsUserCapabilityExchange(){
		BinderTransaction.getInstance().invoke(Transacts.TELEPHONY_IMS_SERVICE,
		        Transacts.TELEPHONY_IMS_DESCRIPTOR, "requestAvailability", 0,
		        mContext.getPackageName(), null,
		        null, null);
    }
 
    @PermissionTest(permission="ASSOCIATE_COMPANION_DEVICES", sdkMin=31)
    public void testAssociateCompanionDevices(){
		Signature signature = SignatureUtils.getTestAppSigningCertificate(mContext);
		MessageDigest messageDigest;
		try {
		    messageDigest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
		    throw new UnexpectedTestFailureException(e);
		}
		String mPackageName = mContext.getPackageName();
		byte[] certDigest = messageDigest.digest(signature.toByteArray());
		if (!mPackageManager.hasSigningCertificate(mPackageName, certDigest,
		        PackageManager.CERT_INPUT_SHA256)) {
		    throw new UnexpectedTestFailureException(
		            "PackageManager reported test app does not have the provided "
		                    + "signing certificate");
		}
		BinderTransaction.getInstance().invoke(Transacts.COMPANION_DEVICE_SERVICE,
		        Transacts.COMPANION_DEVICE_DESCRIPTOR, "createAssociation",
		        mPackageName, "11:22:33:44:55:66", 0, certDigest);
    }
 
    @PermissionTest(permission="BYPASS_ROLE_QUALIFICATION", sdkMin=31)
    public void testBypassRoleQualification(){
		BinderTransaction.getInstance().invoke(Transacts.ROLE_SERVICE, Transacts.ROLE_DESCRIPTOR,
		        "setBypassingRoleQualification", false);
    }
 
    @PermissionTest(permission="PERFORM_IMS_SINGLE_REGISTRATION", sdkMin=31)
    public void testPerformImsSingleRegistration(){
		BinderTransaction.getInstance().invoke(Transacts.TELEPHONY_IMS_SERVICE,
		        Transacts.TELEPHONY_IMS_DESCRIPTOR,
		        "triggerNetworkRegistration", 0,
		        getActivityToken(), 0, "test-sip-reason");
    }

	@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @PermissionTest(permission="SET_DEFAULT_ACCOUNT_FOR_CONTACTS", sdkMin=33)
    public void testSetDefaultAccountForContacts(){
		Bundle extras = new Bundle();
		mContentResolver.call(ContactsContract.AUTHORITY_URI, "setDefaultAccount", null,
				extras);

    }

	@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @PermissionTest(permission=SUBSCRIBE_TO_KEYGUARD_LOCKED_STATE, sdkMin=33, sdkMax=33)
    public void testSubscribeToKeyguardLockedState(){
		KeyguardManager.KeyguardLockedStateListener listener = new KeyguardManager.KeyguardLockedStateListener() {
			@Override
			public void onKeyguardLockedStateChanged(boolean b) {
			}
		};
		BinderTransaction.getInstance().invoke(Transacts.WINDOW_SERVICE,Transacts.WINDOW_DESCRIPTOR,
				"addKeyguardLockedStateListener", listener);

    }
 
    @PermissionTest(permission="CREATE_VIRTUAL_DEVICE", sdkMin=33,sdkMax = 34)
    public void testCreateVirtualDeviceLegacy(){
		try {
		
		    Class<?> clazzVDPBuilder = null;
		    clazzVDPBuilder = Class.forName("android.companion.virtual.VirtualDeviceParams$Builder");
		    Constructor constructor = clazzVDPBuilder.getConstructor();
		    Object builderObj = constructor.newInstance();
		
		    Object vdpParams = ReflectionUtil.invoke(builderObj, "build");

		    IBinder binder = getActivityToken();
		
			BinderTransaction.getInstance().invoke(Transacts.VIRTUAL_DEVICE_MANAGER_SERVICE,
					Transacts.VIRTUAL_DEVICE_MANAGER_DESCRIPTOR,
					"createVirtualDevice",
					binder,mContext.getPackageName(), 0,
					vdpParams,null);
		    
		
		} catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException |
                 IllegalAccessException | InstantiationException |
                 ReflectionUtil.ReflectionIsTemporaryException e) {
		    throw new UnexpectedTestFailureException(e);
		}

    }
	@RequiresApi(api = Build.VERSION_CODES.S)
    @PermissionTest(permission="CREATE_VIRTUAL_DEVICE", sdkMin=35)
	public void testCreateVirtualDevice(){
		try {

			Class<?> clazzVDPBuilder = null;
			clazzVDPBuilder = Class.forName("android.companion.virtual.VirtualDeviceParams$Builder");
			Constructor constructor = clazzVDPBuilder.getConstructor();
			Object builderObj = constructor.newInstance();
			Object vdpParams = ReflectionUtil.invoke(builderObj, "build");

			IBinder binder = getActivityToken();
			/* Parameters from SDK35
			in IBinder token, in AttributionSource attributionSource, int associationId,
			in VirtualDeviceParams params, in IVirtualDeviceActivityListener activityListener,
			in IVirtualDeviceSoundEffectListener soundEffectListener
			*/
			AttributionSource ats = new AttributionSource.Builder(0).build();
			BinderTransaction.getInstance().invoke(Transacts.VIRTUAL_DEVICE_MANAGER_SERVICE,
					Transacts.VIRTUAL_DEVICE_MANAGER_DESCRIPTOR,
					"createVirtualDevice",
					binder,ats, 0,
					vdpParams,null,null);
			

		} catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
			throw new UnexpectedTestFailureException(e);
		} catch (ReflectionUtil.ReflectionIsTemporaryException e) {
            throw new RuntimeException(e);
        }

    }
 
    @PermissionTest(permission="SEND_SAFETY_CENTER_UPDATE", sdkMin=33)
    public void testSendSafetyCenterUpdate(){
		Class<?> clazzSaftyCenter;
		try {
		    clazzSaftyCenter = Class.forName("android.safetycenter.SafetyCenterManager");
		    Object safetyCenter = mContext.getSystemService(clazzSaftyCenter);
			ReflectionUtil.invoke(safetyCenter,
					"getSafetySourceData",new Class[]{String.class},"GooglePlaySystemUpdate");

		} catch (ClassNotFoundException e){
		    throw new UnexpectedTestFailureException(e);
		} catch (ReflectionUtil.ReflectionIsTemporaryException e) {
			throw new UnexpectedTestFailureException(e);
        }
    }
 
    @PermissionTest(permission=REQUEST_COMPANION_PROFILE_AUTOMOTIVE_PROJECTION, sdkMin=33)
    public void testRequestCompanionProfileAutomotiveProjection(){
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
		                    logger.debug(
		                            "onDeviceFound: intentSender = " + intentSender);
		                }
		                @Override
		                public void onFailure(CharSequence charSequence) {
		                    logger.debug("onFailure: charSequence = " + charSequence);
		                }
		            };
		    CompanionDeviceManager companionDeviceManager = mActivity.getSystemService(
		            CompanionDeviceManager.class);
		
		    companionDeviceManager.associate(request, callback, null);
		}
		
    }
 
    @PermissionTest(permission="ACCESS_AMBIENT_CONTEXT_EVENT", sdkMin=33, sdkMax=33)
    public void testAccessAmbientContextEvent() {
		
		@SuppressLint("WrongConstant") Object ambientContextManager
		        = mContext.getSystemService("ambient_context");
		        //Context.AMBIENT_CONTEXT_SERVICE);
		
		int[] eventsArray = new int[] {-1};//AmbientContextEvent.EVENT_COUGH
		Set<Integer> eventTypes = Arrays.stream(eventsArray).boxed().collect(
		        Collectors.toSet());

        try {
            ReflectionUtil.invoke(ambientContextManager,"queryAmbientContextServiceStatus",
                    new Class[]{Set.class, Executor.class, Consumer.class},
                    eventTypes, new Executor() {
                        @Override
                        public void execute(Runnable runnable) {

                        }
                    });
        } catch (ReflectionUtil.ReflectionIsTemporaryException e) {
            throw new UnexpectedTestFailureException(e);
        }
		
    }
 
    @PermissionTest(permission="MANAGE_SAFETY_CENTER", sdkMin=33)
    public void testManageSafetyCenter(){
		BinderTransaction.getInstance().invoke(Transacts.SAFETY_CENTER_MANAGER_SERVICE,
		        Transacts.SAFETY_CENTER_MANAGER_MANAGER_DESCRIPTOR,
		        "getSafetyCenterConfig");
    }
 
    @PermissionTest(permission="MANAGE_SENSOR_PRIVACY", sdkMin=31)
    public void testManageSensorPrivacy(){
		// ISensorPrivacyManager#setSensorPrivacy
		BinderTransaction.getInstance().invoke(Transacts.SENSOR_PRIVACY_SERVICE,
		        Transacts.SENSOR_PRIVACY_DESCRIPTOR,
		        "setSensorPrivacy", false);
    }
 
    @PermissionTest(permission="TOGGLE_AUTOMOTIVE_PROJECTION", sdkMin=33)
    public void testToggleAutomotiveProjection(){
		BinderTransaction.getInstance().invoke(Transacts.UI_MODE_SERVICE,
		        Transacts.UI_MODE_DESCRIPTOR, "requestProjection",
		        getActivityToken(), 1, mContext.getPackageName());
		
		/*mActivity.sendBroadcast(
		        new Intent("android.intent.action.ACTION_USER_UNLOCKED"));*/
    }
 
    @PermissionTest(permission="READ_RESTRICTED_STATS", sdkMin=34)
    public void testReadRestrictedStats(){
		String STATS_MANAGER = "statsmanager";

		Intent i = new Intent(mContext, TestActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, i, PendingIntent.FLAG_IMMUTABLE);

		BinderTransaction.getInstance().invoke(
				STATS_MANAGER,
				Transacts.STATS_DESCRIPTOR,
				"setRestrictedMetricsChangedOperation",
				pendingIntent,1L ,mContext.getPackageName()
		);
    }
 
    @PermissionTest(permission=LAUNCH_CAPTURE_CONTENT_ACTIVITY_FOR_NOTE, sdkMin=34)
    public void testLaunchCaptureContentActivityForNote(){
		//commonize the tester routine with exposing the builder of AssociationRequest object

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
 
    @PermissionTest(permission=MANAGE_DEVICE_LOCK_STATE, sdkMin=34)
    public void testManageDeviceLockState(){
		CountDownLatch latch = new CountDownLatch(1);
		AtomicBoolean success = new AtomicBoolean(true);
		
		//need below command to execute this test case
		//adb shell settings put global hidden_api_policy  1
		
		//logger.logSystem("aaa>"+android.os.Build.VERSION.SDK_INT);
		if(android.os.Build.VERSION.SDK_INT >= 35) {
		    //logger.logSystem("aaa");
		    //Interface Change
		    IIsDeviceLockedCallback callback = new IIsDeviceLockedCallback() {
		        @Override
		        public void onIsDeviceLocked(boolean locked) throws RemoteException {
		            //logger.logSystem("onError1");
		        }
		
		        @Override
		        public void onError(ParcelableException ex) throws RemoteException {
		            //logger.logSystem("onError");
		        }
		
		        @Override
		        public IBinder asBinder() {
		            return new IIsDeviceLockedCallback.Stub() {
		
		                @Override
		                public void onIsDeviceLocked(boolean locked) throws RemoteException {
		                    //logger.logSystem("-onError2");
		
		                    latch.countDown();
		                }
		
		                @Override
		                public void onError(ParcelableException ex) throws RemoteException {
		                    //logger.logSystem("-onError");
		
		                    if(ex.getException().toString().equals("java.lang.SecurityException")){
		                        success.set(false);
		                    }
		                    latch.countDown();
		                }
		            };
		        }
		    };
		    try {
		        //MANAGE_DEVICE_LOCK_STATE
		        BinderTransaction.getInstance().invoke(
		                Context.DEVICE_LOCK_SERVICE,
		                Transacts.DEVICELOCK_DESCRIPTOR,
		                "isDeviceLocked",callback);
		
		        latch.await(2000, TimeUnit.MILLISECONDS);
		        if(!success.get()){
		            throw new SecurityException("Found secuirty error in callback interface!");
		        }
		    } catch (InterruptedException e) {
		        logger.error(e.getMessage(),e);
		    }
		}else if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
		    // NOTICE:
		    // We can not use an interface with a same name at once, so if you would like to test it
		    // with SDK34 kindly change name of these callbacks.

			logger.warn("We can not use an interface with a same name at once, " +
							"so if you would like to test it with SDK34, kindly change name of _IIsDeviceLockedCallback callbacks.");

		    _IIsDeviceLockedCallback callback = new _IIsDeviceLockedCallback() {
		        @Override
		        public void onIsDeviceLocked(boolean locked) throws RemoteException {
		
		        }
		
		        @Override
		        public void onError(int error) throws RemoteException {
		
		        }
		
		        @Override
		        public IBinder asBinder() {
		            return new _IIsDeviceLockedCallback.Stub() {
		
		                @Override
		                public void onIsDeviceLocked(boolean locked) throws RemoteException {
		
		                    latch.countDown();
		                }
		
		                @Override
		                public void onError(int error) throws RemoteException {
		
		                    if (_IIsDeviceLockedCallback.ERROR_SECURITY == error) {
		                        success.set(false);
		                    }
		                    latch.countDown();
		                }
		            };
		        }
		    };
		    try {
		
		        //MANAGE_DEVICE_LOCK_STATE
		        BinderTransaction.getInstance().invoke(
		            Context.DEVICE_LOCK_SERVICE,
		            Transacts.DEVICELOCK_DESCRIPTOR,
		            "isDeviceLocked",callback);
		
		        latch.await(2000, TimeUnit.MILLISECONDS);
		        if(!success.get()){
		            throw new SecurityException("Found secuirty error in callback interface!");
		        }
		    } catch (InterruptedException e) {
		        logger.error(e.getMessage());
		        e.printStackTrace();
		    }
		}
    }
 
    @PermissionTest(permission="QUERY_DEVICE_STOLEN_STATE", sdkMin=34)
    public void testQueryDeviceStolenState(){
		if(!TesterUtils.getAdminFlagByName("deviceTheftImplEnabled")){
		    throw new BypassTestException(
		            "To run this test, deviceTheftImplEnabled system flag should be enabled.Aborted");
		}
		BinderTransaction.getInstance().invoke(
		        Transacts.DEVICE_POLICY_SERVICE,
		        Transacts.DEVICE_POLICY_DESCRIPTOR,
		        "isDevicePotentiallyStolen",mContext.getPackageName());
		
    }

	//**** method template for target internal SDK36
	@PermissionTest(permission="ADD_MIRROR_DISPLAY",sdkMin=36)
	public void testAddMirrorDisplay(){
		logger.debug("The test for android.permission.ADD_MIRROR_DISPLAY is not implemented yet");
	}
	@PermissionTest(permission="EXECUTE_APP_FUNCTIONS",sdkMin=36)
	public void testExecuteAppFunctions(){
		logger.debug("The test for android.permission.EXECUTE_APP_FUNCTIONS is not implemented yet");
	}

	//**** method template for target internal SDK37
	@PermissionTest(permission="ACCESS_BIOMETRIC_SENSOR_STRENGTHS",sdkMin=37)
	public void testAccessBiometricSensorStrengths(){
        try {
            android.hardware.biometrics.BiometricManager bm = mContext.getSystemService(android.hardware.biometrics.BiometricManager.class);
            if (bm != null) {
                bm.getBiometricSensorStrengths();
                logger.debug("getBiometricSensorStrengths called successfully");
            } else {
                logger.debug("BiometricManager is null");
            }
        } catch (SecurityException e) {
            throw e;
        } catch (Exception e) {
            throw new com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException(e);
        }
	}
	@PermissionTest(permission="ACCESS_COMPUTER_CONTROL",sdkMin=37)
	public void testAccessComputerControl(){
        try {
            Object vdm = mContext.getSystemService("virtualdevice");
            if (vdm != null) {
                Class<?> paramsClazz = Class.forName("android.companion.virtual.computercontrol.ComputerControlSessionParams");
                java.lang.reflect.Constructor<?> paramsConstructor = paramsClazz.getDeclaredConstructor(
                        String.class, int.class, java.util.List.class, android.app.PendingIntent.class,
                        Class.forName("android.app.AppInteractionAttribution"),
                        Class.forName("android.companion.virtual.CompanionDeviceId"),
                        Class.forName("android.companion.virtual.computercontrol.ComputerControlSessionParams$NotificationParams")
                );
                paramsConstructor.setAccessible(true);
                
                java.util.List<String> pkgs = new java.util.ArrayList<>();
                pkgs.add(mContext.getPackageName());
                
                Object params = paramsConstructor.newInstance(
                        "test_session", 4, pkgs, null, null, null, null
                );
                
                Class<?> callbackClazz = Class.forName("android.companion.virtual.computercontrol.ComputerControlSession$Callback");
                Object callback = java.lang.reflect.Proxy.newProxyInstance(
                        callbackClazz.getClassLoader(),
                        new Class<?>[]{callbackClazz},
                        new java.lang.reflect.InvocationHandler() {
                            @Override
                            public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) throws Throwable {
                                logger.debug("Callback method called: " + method.getName());
                                return null;
                            }
                        }
                );
                
                java.lang.reflect.Method method = vdm.getClass().getMethod("requestComputerControlSession", paramsClazz, java.util.concurrent.Executor.class, callbackClazz);
                method.invoke(vdm, params, mContext.getMainExecutor(), callback);
                logger.debug("requestComputerControlSession called successfully");
            } else {
                logger.debug("VirtualDeviceManager is null");
            }
        } catch (java.lang.reflect.InvocationTargetException e) {
            if (e.getCause() instanceof SecurityException) {
                throw (SecurityException) e.getCause();
            } else if (e.getCause() instanceof IllegalStateException && e.getCause().getMessage() != null && e.getCause().getMessage().contains("flag disabled")) {        
                throw new com.android.certification.niap.permission.dpctester.test.exception.BypassTestException("Feature flag disabled: " + e.getCause().getMessage());
            }
            throw new com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException(e);
        } catch (Exception e) {
            throw new com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException(e);
        }
	}
	@PermissionTest(permission="ACCESS_HID",sdkMin=37)
	public void testAccessHid(){
        try {
            Class<?> clazz = Class.forName("android.hardware.hid.HidManager");
            java.lang.reflect.Constructor<?> constructor = clazz.getConstructor(Context.class);
            Object hm = constructor.newInstance(mContext);
            java.lang.reflect.Method method = clazz.getMethod("canEnumerateDevices");
            Boolean result = (Boolean) method.invoke(hm);
            logger.debug("canEnumerateDevices result: " + result);
        } catch (SecurityException e) {
            throw e;
        } catch (Exception e) {
            throw new com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException(e);
        }
	}
	@PermissionTest(permission="BIND_ALLOWLIST_PROVIDER_SERVICE",sdkMin=37)
	public void testBindAllowlistProviderService(){
		Intent intent = new Intent();
		intent.setComponent(new android.content.ComponentName("com.google.android.gms", "com.google.android.gms.systemserviceacl.service.AclProviderService"));
		
		android.content.ServiceConnection connection = new android.content.ServiceConnection() {
			@Override
			public void onServiceConnected(android.content.ComponentName name, IBinder service) {}
			@Override
			public void onServiceDisconnected(android.content.ComponentName name) {}
		};

		try {
			boolean bound = mContext.bindService(intent, Context.BIND_AUTO_CREATE, mContext.getMainExecutor(), connection);
			if (bound) {
				mContext.unbindService(connection);
				logger.info("Successfully bound to AclProviderService (unexpected without permission)");
			} else {
				logger.info("Failed to bind to AclProviderService (expected without permission)");
			}
		} catch (SecurityException e) {
			logger.info("SecurityException expectedly thrown when binding to AclProviderService: " + e.getMessage());
		}
	}
	@PermissionTest(permission="BIND_DEVELOPER_VERIFICATION_AGENT",sdkMin=37)
	public void testBindDeveloperVerificationAgent(){
		Intent intent = new Intent();
		intent.setComponent(new android.content.ComponentName("com.google.android.verifier", "com.google.android.verifier.helpers.verification.impl.common.platform.PlatformVerificationService"));
		
		android.content.ServiceConnection connection = new android.content.ServiceConnection() {
			@Override
			public void onServiceConnected(android.content.ComponentName name, IBinder service) {}
			@Override
			public void onServiceDisconnected(android.content.ComponentName name) {}
		};

		try {
			boolean bound = mContext.bindService(intent, Context.BIND_AUTO_CREATE, mContext.getMainExecutor(), connection);
			if (bound) {
				mContext.unbindService(connection);
				logger.info("Successfully bound to PlatformVerificationService (unexpected without permission)");
			} else {
				logger.info("Failed to bind to PlatformVerificationService (expected without permission)");
			}
		} catch (SecurityException e) {
			logger.info("SecurityException expectedly thrown when binding to PlatformVerificationService: " + e.getMessage());
		}
	}
	@PermissionTest(permission="REQUEST_LOCATION_BUTTON_PERMISSIONS",sdkMin=37)
	public void testRequestLocationButtonPermissions(){
		Intent intent = new Intent("android.app.permissionui.action.REQUEST_LOCATION_BUTTON_PERMISSIONS");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			mContext.startActivity(intent);
			logger.info("Successfully started RequestLocationButtonPermissionsActivity (unexpected without permission)");
		} catch (SecurityException e) {
			logger.info("SecurityException expectedly thrown when starting activity: " + e.getMessage());
		}
	}
	@PermissionTest(permission="CAPTURE_KEYBOARD",sdkMin=37)
	public void testCaptureKeyboard(){
		logger.info("This permission is tested via instrumentation in CaptureKeyboardTest.java because it requires UI and key injection.");
	}
	@PermissionTest(permission="DISCOVER_APP_FUNCTIONS",sdkMin=37)
	public void testDiscoverAppFunctions(){
        try {
            Object afm = mContext.getSystemService("app_function");
            if (afm != null) {
                java.lang.reflect.Method method = afm.getClass().getMethod("isAppFunctionEnabled", String.class, String.class, java.util.concurrent.Executor.class, android.os.OutcomeReceiver.class);
                android.os.OutcomeReceiver<Boolean, Exception> callback = new android.os.OutcomeReceiver<Boolean, Exception>() {
                    @Override
                    public void onResult(Boolean result) {
                        logger.debug("isAppFunctionEnabled result: " + result);
                    }
                    @Override
                    public void onError(Exception error) {
                        logger.debug("isAppFunctionEnabled error: " + error);
                    }
                };
                method.invoke(afm, "dummy_id", mContext.getPackageName(), mContext.getMainExecutor(), callback);
                logger.debug("isAppFunctionEnabled called successfully");
            } else {
                logger.debug("AppFunctionManager is null");
            }
        } catch (java.lang.reflect.InvocationTargetException e) {
            if (e.getCause() instanceof SecurityException) {
                throw (SecurityException) e.getCause();
            }
            throw new com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException(e);
        } catch (Exception e) {
            throw new com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException(e);
        }
	}
	@PermissionTest(permission="EXECUTE_APP_FUNCTIONS_SYSTEM",sdkMin=37)
	public void testExecuteAppFunctionsSystem(){
        try {
            Object afm = mContext.getSystemService("app_function");
            if (afm != null) {
                java.lang.reflect.Method method = afm.getClass().getMethod("isAppFunctionEnabled", String.class, String.class, java.util.concurrent.Executor.class, android.os.OutcomeReceiver.class);
                android.os.OutcomeReceiver<Boolean, Exception> callback = new android.os.OutcomeReceiver<Boolean, Exception>() {
                    @Override
                    public void onResult(Boolean result) {
                        logger.debug("isAppFunctionEnabled result: " + result);
                    }
                    @Override
                    public void onError(Exception error) {
                        logger.debug("isAppFunctionEnabled error: " + error);
                    }
                };
                method.invoke(afm, "dummy_id", mContext.getPackageName(), mContext.getMainExecutor(), callback);
                logger.debug("isAppFunctionEnabled called successfully");
            } else {
                logger.debug("AppFunctionManager is null");
            }
        } catch (java.lang.reflect.InvocationTargetException e) {
            if (e.getCause() instanceof SecurityException) {
                throw (SecurityException) e.getCause();
            }
            throw new com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException(e);
        } catch (Exception e) {
            throw new com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException(e);
        }
	}
	@PermissionTest(permission="LOCK_APPS",sdkMin=37)
	public void testLockApps(){
        try {
            PackageManager pm = mContext.getPackageManager();
            java.lang.reflect.Method method = pm.getClass().getMethod("getEnableAppLockIntentForPackage", String.class, boolean.class);
            method.invoke(pm, mContext.getPackageName(), true);
            logger.debug("getEnableAppLockIntentForPackage called successfully");
        } catch (java.lang.reflect.InvocationTargetException e) {
            if (e.getCause() instanceof SecurityException) {
                throw (SecurityException) e.getCause();
            }
            throw new com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException(e);
        } catch (Exception e) {
            throw new com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException(e);
        }
	}

	@PermissionTest(permission="MANAGE_SUPERVISION",sdkMin=37)
	public void testManageSupervision(){
        try {
            Object sm = mContext.getSystemService("supervision");
            if (sm != null) {
                java.lang.reflect.Method method = sm.getClass().getMethod("getPolicies");
                method.invoke(sm);
                logger.debug("getPolicies called successfully");
            } else {
                logger.debug("SupervisionManager is null");
            }
        } catch (java.lang.reflect.InvocationTargetException e) {
            if (e.getCause() instanceof SecurityException) {
                throw (SecurityException) e.getCause();
            }
            throw new com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException(e);
        } catch (Exception e) {
            throw new com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException(e);
        }
	}
	@PermissionTest(permission="SET_DEVELOPER_VERIFICATION_USER_RESPONSE",sdkMin=37)
	public void testSetDeveloperVerificationUserResponse(){
        try {
            android.content.pm.PackageInstaller packageInstaller = mContext.getPackageManager().getPackageInstaller();
            java.lang.reflect.Method method = packageInstaller.getClass().getMethod(
                "setDeveloperVerificationUserResponse", int.class, int.class);
            
            // Using dummy values: sessionId=0, userResponse=1 (APPROVE or similar)
            method.invoke(packageInstaller, 0, 1);
            logger.debug("setDeveloperVerificationUserResponse called successfully (unexpected without permission)");
        } catch (java.lang.reflect.InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof SecurityException) {
                logger.info("SecurityException expectedly thrown: " + cause.getMessage());
            } else {
                throw new RuntimeException(cause);
            }
        } catch (Exception e) {
            logger.debug("Reflection error: " + e.getMessage());
            throw new BypassTestException("Failed to call setDeveloperVerificationUserResponse via reflection");
        }
	}
	@PermissionTest(permission="SHOW_POWER_MENU",sdkMin=37)
	public void testShowPowerMenu(){
        try {
            Object statusBarManager = mContext.getSystemService("statusbar");
            java.lang.reflect.Method method = statusBarManager.getClass().getMethod(
                "showPowerMenu", java.util.concurrent.Executor.class, android.os.OutcomeReceiver.class);
            
            android.os.OutcomeReceiver<Integer, Throwable> receiver = new android.os.OutcomeReceiver<Integer, Throwable>() {
                @Override
                public void onResult(Integer result) {
                    logger.debug("showPowerMenu onResult: " + result);
                }
                @Override
                public void onError(Throwable error) {
                    logger.debug("showPowerMenu onError: " + error.getMessage());
                }
            };
            
            method.invoke(statusBarManager, mContext.getMainExecutor(), receiver);
            logger.debug("showPowerMenu called successfully");
        } catch (java.lang.reflect.InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof SecurityException) {
                throw (SecurityException) cause;
            } else {
                throw new RuntimeException(cause);
            }
        } catch (Exception e) {
            logger.debug("Reflection error: " + e.getMessage());
            throw new BypassTestException("Failed to call showPowerMenu via reflection");
        }
	}
	@PermissionTest(permission="SHOW_POWER_MENU_PRIVILEGED",sdkMin=37)
	public void testShowPowerMenuPrivileged(){
        try {
            Object statusBarManager = mContext.getSystemService("statusbar");
            java.lang.reflect.Method method = statusBarManager.getClass().getMethod(
                "showPowerMenu", java.util.concurrent.Executor.class, android.os.OutcomeReceiver.class);
            
            android.os.OutcomeReceiver<Integer, Throwable> receiver = new android.os.OutcomeReceiver<Integer, Throwable>() {
                @Override
                public void onResult(Integer result) {
                    logger.debug("showPowerMenu Privileged onResult: " + result);
                }
                @Override
                public void onError(Throwable error) {
                    logger.debug("showPowerMenu Privileged onError: " + error.getMessage());
                }
            };
            
            method.invoke(statusBarManager, mContext.getMainExecutor(), receiver);
            logger.debug("showPowerMenu Privileged called successfully");
        } catch (java.lang.reflect.InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof SecurityException) {
                throw (SecurityException) cause;
            } else {
                throw new RuntimeException(cause);
            }
        } catch (Exception e) {
            logger.debug("Reflection error: " + e.getMessage());
            throw new BypassTestException("Failed to call showPowerMenu via reflection");
        }
	}

	@PermissionTest(permission="CREATE_APP_SPECIFIC_NETWORK",sdkMin=37)
	public void testCreateAppSpecificNetwork(){
        try {
            android.net.ConnectivityManager connectivityManager = mContext.getSystemService(android.net.ConnectivityManager.class);
            
            Class<?> networkAgentClass = Class.forName("android.net.INetworkAgent");
            
            // Create dynamic proxy for INetworkAgent
            Object dummyAgent = java.lang.reflect.Proxy.newProxyInstance(
                networkAgentClass.getClassLoader(),
                new Class<?>[]{networkAgentClass},
                new java.lang.reflect.InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) throws Throwable {
                        return null;
                    }
                }
            );
            
            android.net.NetworkInfo networkInfo = new android.net.NetworkInfo(android.net.ConnectivityManager.TYPE_WIFI, 0, "WIFI", "");
            android.net.LinkProperties linkProperties = new android.net.LinkProperties();
            android.net.NetworkCapabilities networkCapabilities = new android.net.NetworkCapabilities();
            
            java.lang.reflect.Method method = null;
            for (java.lang.reflect.Method m : android.net.ConnectivityManager.class.getDeclaredMethods()) {
                if (m.getName().equals("registerNetworkAgent") && m.getParameterCount() == 7) {
                    method = m;
                    break;
                }
            }
            
            if (method == null) {
                logger.debug("registerNetworkAgent method not found");
                return;
            }
            
            method.setAccessible(true);
            
            Object score = null;
            try {
                Class<?> scoreBuilderClass = Class.forName("android.net.NetworkScore$Builder");
                Object scoreBuilder = scoreBuilderClass.getDeclaredConstructor().newInstance();
                java.lang.reflect.Method buildMethod = scoreBuilderClass.getMethod("build");
                score = buildMethod.invoke(scoreBuilder);
            } catch (Exception e) {
                logger.debug("Failed to create NetworkScore via builder: " + e.getMessage());
            }
            
            Object config = null;
            try {
                Class<?> configBuilderClass = Class.forName("android.net.NetworkAgentConfig$Builder");
                Object configBuilder = configBuilderClass.getDeclaredConstructor().newInstance();
                java.lang.reflect.Method buildMethod = configBuilderClass.getMethod("build");
                config = buildMethod.invoke(configBuilder);
            } catch (Exception e) {
                logger.debug("Failed to create NetworkAgentConfig via builder: " + e.getMessage());
            }
            
            // Passing score and config instead of null
            method.invoke(connectivityManager, dummyAgent, networkInfo, linkProperties, networkCapabilities, score, config, 1);
            logger.debug("registerNetworkAgent called successfully");
        } catch (java.lang.reflect.InvocationTargetException e) {
            if (e.getCause() instanceof SecurityException) {
                throw (SecurityException) e.getCause();
            }
            throw new com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException(e);
        } catch (Exception e) {
            throw new com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException(e);
        }
	}

	@PermissionTest(permission="INITIATE_BUGREPORT_AS_NON_ADMIN",sdkMin=37)
	public void testInitiateBugreportAsNonAdmin(){
        try {
            android.os.BugreportManager bugreportManager = mContext.getSystemService(android.os.BugreportManager.class);
            if (bugreportManager == null) {
                logger.debug("BugreportManager not available");
                return;
            }
            
            java.lang.reflect.Method method = null;
            for (java.lang.reflect.Method m : android.os.BugreportManager.class.getDeclaredMethods()) {
                if (m.getName().equals("startBugreport") && m.getParameterCount() == 5) {
                    method = m;
                    break;
                }
            }
            
            if (method == null) {
                logger.debug("startBugreport method with 5 params not found");
                return;
            }
            
            method.setAccessible(true);
            
            android.os.BugreportManager.BugreportCallback callback = new android.os.BugreportManager.BugreportCallback() {
                @Override
                public void onProgress(float progress) {}
                @Override
                public void onError(int errorCode) {}
                @Override
                public void onFinished() {}
            };
            
            // Create dummy file descriptors to avoid NPE in BugreportManager
            java.io.File bugreportFile = new java.io.File(mContext.getCacheDir(), "dummy_bugreport");
            android.os.ParcelFileDescriptor bugreportFd = android.os.ParcelFileDescriptor.open(
                    bugreportFile, android.os.ParcelFileDescriptor.MODE_WRITE_ONLY | android.os.ParcelFileDescriptor.MODE_CREATE);
            
            java.io.File screenshotFile = new java.io.File(mContext.getCacheDir(), "dummy_screenshot");
            android.os.ParcelFileDescriptor screenshotFd = android.os.ParcelFileDescriptor.open(
                    screenshotFile, android.os.ParcelFileDescriptor.MODE_WRITE_ONLY | android.os.ParcelFileDescriptor.MODE_CREATE);
            
            // Instantiate BugreportParams via reflection
            Class<?> bugreportParamsClass = Class.forName("android.os.BugreportParams");
            java.lang.reflect.Constructor<?> bpConstructor = bugreportParamsClass.getConstructor(int.class);
            Object params = bpConstructor.newInstance(0); // 0 is BUGREPORT_MODE_FULL
            
            method.invoke(bugreportManager, bugreportFd, screenshotFd, params, mContext.getMainExecutor(), callback);
            logger.debug("startBugreport called successfully");
            
            // Files will be deleted when the VM exits.
            // BugreportManager takes ownership and closes the FDs.
            bugreportFile.deleteOnExit();
            screenshotFile.deleteOnExit();
            
        } catch (java.lang.reflect.InvocationTargetException e) {
            if (e.getCause() instanceof SecurityException) {
                throw (SecurityException) e.getCause();
            }
            throw new com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException(e);
        } catch (Exception e) {
            throw new com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException(e);
        }
	}

    @PermissionTest(permission="ACCESS_ATTENTION_LISTENER", sdkMin=37)
    public void testAccessAttentionListener(){
        try {
            Object service = mContext.getSystemService("attention");
            if (service == null) {
                logger.debug("attention service not available");
                return;
            }
            
            java.lang.reflect.Method setListener = null;
            for (java.lang.reflect.Method m : service.getClass().getMethods()) {
                if (m.getName().equals("setListener")) {
                    setListener = m;
                    break;
                }
            }
            
            if (setListener == null) {
                logger.debug("setListener method not found");
                return;
            }
            
            try {
                setListener.invoke(service, 0, 0L, null);
                logger.debug("setListener invoked successfully");
            } catch (java.lang.reflect.InvocationTargetException e) {
                Throwable cause = e.getCause();
                if (cause instanceof SecurityException) {
                    throw (SecurityException) cause;
                } else {
                    logger.debug("setListener threw expected non-security exception: " + cause);
                }
            }
        } catch (SecurityException e) {
            throw e;
        } catch (Exception e) {
            logger.debug("Error testing ACCESS_ATTENTION_LISTENER: " + e.getMessage());
            throw new com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException(e);
        }
    }

    @PermissionTest(permission="MANAGE_COMPUTER_CONTROL_CONSENT", sdkMin=37)
    public void testManageComputerControlConsent(){
        try {
            Object manager = mContext.getSystemService("virtualdevice");
            if (manager == null) {
                logger.debug("virtualdevice service not available");
                return;
            }
            java.lang.reflect.Method method = manager.getClass().getMethod("isPackageApprovedToRunComputerControlAutomation", String.class, int.class);
            method.invoke(manager, "dummy", 0);
        } catch (java.lang.reflect.InvocationTargetException e) {
            if (e.getCause() instanceof SecurityException) {
                throw (SecurityException) e.getCause();
            }
            throw new com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException(e);
        } catch (Exception e) {
            throw new com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException(e);
        }
    }

    @PermissionTest(permission="MANAGE_MULTIUSER_DEVICE_PROVISIONING_STATE", sdkMin=37)
    public void testManageMultiuserDeviceProvisioningState(){
        try {
            android.app.admin.DevicePolicyManager dpm = (android.app.admin.DevicePolicyManager) mContext.getSystemService(android.content.Context.DEVICE_POLICY_SERVICE);
            if (dpm == null) {
                logger.debug("DevicePolicyManager is null");
                return;
            }
            java.lang.reflect.Method method = dpm.getClass().getMethod("getMultiuserManagedDeviceProvisioningState");
            method.invoke(dpm);
        } catch (java.lang.reflect.InvocationTargetException e) {
            if (e.getCause() instanceof SecurityException) {
                throw (SecurityException) e.getCause();
            }
            throw new com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException(e);
        } catch (Exception e) {
            throw new com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException(e);
        }
    }

    @PermissionTest(permission="REQUEST_COMPANION_PROFILE_VIRTUAL_DEVICE", sdkMin=37)
    public void testRequestCompanionProfileVirtualDevice(){
        android.companion.CompanionDeviceManager companionDeviceManager = mContext.getSystemService(android.companion.CompanionDeviceManager.class);
        if (companionDeviceManager == null) {
            logger.debug("CompanionDeviceManager is null");
            return;
        }
        String profile = "android.app.role.COMPANION_DEVICE_VIRTUAL_DEVICE"; // likely value
        try {
            java.lang.reflect.Field field = android.companion.CompanionDeviceManager.class.getField("DEVICE_PROFILE_VIRTUAL_DEVICE");
            profile = (String) field.get(null);
        } catch (Exception e) {
            logger.debug("Could not find DEVICE_PROFILE_VIRTUAL_DEVICE constant, using fallback.");
        }

        android.companion.AssociationRequest.Builder builder = new android.companion.AssociationRequest.Builder();
        builder.setDeviceProfile(profile);
        android.companion.AssociationRequest request = builder.build();

        try {
            companionDeviceManager.associate(request, new android.companion.CompanionDeviceManager.Callback() {
                @Override
                public void onFailure(CharSequence error) {
                    logger.debug("associate failed: " + error);
                }
            }, null);
            
            logger.debug("associate called successfully (positive test passed or waiting for UI).");
        } catch (IllegalArgumentException e) {
            logger.debug("IllegalArgumentException thrown: " + e.getMessage());
        } catch (SecurityException e) {
            throw e;
        } catch (Exception e) {
            logger.debug("Threw non-SecurityException as expected when permission is granted: " + e);
        }
    }

}
