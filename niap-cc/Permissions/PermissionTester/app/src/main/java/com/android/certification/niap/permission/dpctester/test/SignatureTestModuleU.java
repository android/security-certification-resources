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


import static android.Manifest.permission.QUERY_ALL_PACKAGES;
import static android.Manifest.permission.SCHEDULE_EXACT_ALARM;
import static android.Manifest.permission.USE_EXACT_ALARM;
import static android.os.PowerManager.SCREEN_DIM_WAKE_LOCK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.BroadcastOptions;
import android.app.KeyguardManager;
import android.app.LocaleConfig;
import android.app.PendingIntent;
import android.companion.AssociationRequest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.display.HdrConversionMode;
import android.health.connect.aidl.IMigrationCallback;
import android.health.connect.migration.MigrationException;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IWakeLockCallback;
import android.os.LocaleList;
import android.os.Looper;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.WorkSource;
import android.security.KeyChain;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.android.certification.niap.permission.dpctester.MainActivity;
import com.android.certification.niap.permission.dpctester.common.Constants;
import com.android.certification.niap.permission.dpctester.common.ReflectionUtil;
import com.android.certification.niap.permission.dpctester.test.exception.BypassTestException;
import com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException;
import com.android.certification.niap.permission.dpctester.test.log.StaticLogger;
import com.android.certification.niap.permission.dpctester.test.runner.SignaturePermissionTestModuleBase;
import com.android.certification.niap.permission.dpctester.test.tool.BinderTransaction;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTest;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestModule;
import com.android.certification.niap.permission.dpctester.test.tool.ReflectionTool;
import com.android.certification.niap.permission.dpctester.test.tool.TesterUtils;

import org.robolectric.RuntimeEnvironment;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@PermissionTestModule(name="Signature 34(U) Test Cases",prflabel = "UDC(14)")
public class SignatureTestModuleU extends SignaturePermissionTestModuleBase {
	public SignatureTestModuleU(@NonNull Activity activity) {
		super(activity);
	}

	private <T> T systemService(Class<T> clazz){
		return Objects.requireNonNull(getService(clazz),"[npe_system_service]"+clazz.getSimpleName());
	}

	@PermissionTest(permission="ACCESS_AMBIENT_CONTEXT_EVENT", sdkMin=34, sdkMax=34)
	public void testAccessAmbientContextEvent(){

		@SuppressLint("WrongConstant") Object ambientContextManager
				= mContext.getSystemService("ambient_context");
		//Context.AMBIENT_CONTEXT_SERVICE);

		int[] eventsArray = new int[] {-1};//AmbientContextEvent.EVENT_COUGH
		Set<Integer> eventTypes = Arrays.stream(eventsArray).boxed().collect(
				Collectors.toSet());

		ReflectionUtil.invoke(ambientContextManager,
				"queryAmbientContextServiceStatus", new Class[]{Set.class, Executor.class,
						Consumer.class}, eventTypes, new Executor() {
					@Override
					public void execute(Runnable runnable) {

					}
				}, null);

	}

	@RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
	@PermissionTest(permission="SUBSCRIBE_TO_KEYGUARD_LOCKED_STATE", sdkMin=34)
	public void subscribeToKeyguardLockedState(){
		//WindowManagerService#addKeyguardLockedStateListener.
		KeyguardManager.KeyguardLockedStateListener listener = new KeyguardManager.KeyguardLockedStateListener() {
			@Override
			public void onKeyguardLockedStateChanged(boolean b) {
			}
		};

		//logger.system(ReflectionTool.Companion.checkDeclaredMethod(systemService(WindowManager.class),"add").toString());

		//systemService(WindowManager.class);
		//.addKeyguardLockedStateListener(listener);

		BinderTransaction.getInstance().invoke(Transacts.WINDOW_SERVICE,
				Transacts.WINDOW_DESCRIPTOR,
				"addKeyguardLockedStateListener", listener);

	}

	@PermissionTest(permission="SCHEDULE_EXACT_ALARM", sdkMin=34)
	public void testScheduleExactAlarm(){

		if (ActivityCompat.checkSelfPermission(mContext, USE_EXACT_ALARM)
				== PackageManager.PERMISSION_GRANTED &&
				ActivityCompat.checkSelfPermission(mContext, SCHEDULE_EXACT_ALARM)
						== PackageManager.PERMISSION_DENIED){
			throw new BypassTestException(
					"If the USE_EXACT_ALARM permission is granted, the test will be passed. let's skip it");
		}

		Intent intent = new Intent(mContext, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent,
				PendingIntent.FLAG_IMMUTABLE);
		AlarmManager alarmManager = mContext.getSystemService(AlarmManager.class);
		alarmManager.setExact(AlarmManager.RTC, System.currentTimeMillis() + 60 * 1000,
				pendingIntent);
		alarmManager.cancel(pendingIntent);
	}

	@RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @PermissionTest(permission="DELETE_STAGED_HEALTH_CONNECT_REMOTE_DATA", sdkMin=34)
	public void testDeleteStagedHealthConnectRemoteData(){
		BinderTransaction.getInstance().invoke(
				Context.HEALTHCONNECT_SERVICE ,
				Transacts.HEALTH_CONNECT_DESCRIPTOR,
				"deleteAllStagedRemoteData", UserHandle.getUserHandleForUid(appUid));
	}

	@RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @PermissionTest(permission="MIGRATE_HEALTH_CONNECT_DATA", sdkMin=34)
	public void testMigrateHealthConnectData(){
		CountDownLatch latch = new CountDownLatch(1);
		AtomicBoolean success = new AtomicBoolean(true);
		IMigrationCallback callback = new IMigrationCallback() {
			@Override
			public void onSuccess()
					throws RemoteException {}
			@Override
			public void onError(MigrationException exception)
					throws RemoteException {}
			@Override
			public IBinder asBinder() {
				return new Stub() {
					@Override
					public void onSuccess() throws RemoteException {
						latch.countDown();
					}
					@Override
					public void onError(MigrationException exception)
							throws RemoteException {
						//Hook Security Exception
						if(exception.getMessage() != null &&
								exception.getMessage().contains("java.lang.SecurityException")){
							success.set(false);
						}

						latch.countDown();
					}
				};
			}
		};
		BinderTransaction.getInstance().invoke(
				Context.HEALTHCONNECT_SERVICE,
				Transacts.HEALTH_CONNECT_DESCRIPTOR,
				"startMigration",
				mContext.getPackageName(), callback);
		try {
			latch.await(2000, TimeUnit.MILLISECONDS);
			if(!success.get()){
				throw new SecurityException("Found security error in callback interface!");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @PermissionTest(permission="STAGE_HEALTH_CONNECT_REMOTE_DATA", sdkMin=34)
	public void testStageHealthConnectRemoteData(){
		BinderTransaction.getInstance().invoke(
				Context.HEALTHCONNECT_SERVICE ,
				Transacts.HEALTH_CONNECT_DESCRIPTOR,
				"updateDataDownloadState",
				0);
	}

	@PermissionTest(permission="GET_APP_METADATA", sdkMin=34)
	public void testGetAppMetadata(){
			BinderTransaction.getInstance().invoke(
					Transacts.PACKAGE_SERVICE,
					Transacts.PACKAGE_DESCRIPTOR,
					"getAppMetadataFd",
					mContext.getPackageName(), appUid);

	}

	@RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @PermissionTest(permission="LIST_ENABLED_CREDENTIAL_PROVIDERS", sdkMin=34)
	public void testListEnabledCredentialProviders(){
		if(checkPermissionGranted(QUERY_ALL_PACKAGES)){
			throw new BypassTestException(
					"This test works only when QUERY_ALL_PACKAGES permission is not granted.");
		}
		BinderTransaction.getInstance().invoke(
				Context.CREDENTIAL_SERVICE,
				Transacts.CREDENTIAL_DESCRIPTOR,
				"getCredentialProviderServices",
				appUid, Binder.getCallingPid());

	}

	@PermissionTest(permission="LOG_FOREGROUND_RESOURCE_USE", sdkMin=34)
	public void testLogForegroundResourceUse(){
		BinderTransaction.getInstance().invoke(
				Transacts.ACTIVITY_SERVICE,
				Transacts.ACTIVITY_DESCRIPTOR,
				"logFgsApiBegin",
				0,appUid,Binder.getCallingPid()
		);
	}

	@PermissionTest(permission="MANAGE_CLIPBOARD_ACCESS_NOTIFICATION", sdkMin=34)
	public void testManageClipboardAccessNotification(){
		BinderTransaction.getInstance().invoke(
				Context.CLIPBOARD_SERVICE,
				Transacts.CLIPBOARD_DESCRIPTOR,
				"areClipboardAccessNotificationsEnabledForUser",
				appUid
		);
	}

	@PermissionTest(permission="MANAGE_SUBSCRIPTION_USER_ASSOCIATION", sdkMin=34)
	public void testManageSubscriptionUserAssociation(){
		BinderTransaction.getInstance().invoke(
				Context.TELEPHONY_SUBSCRIPTION_SERVICE,
				Transacts.SUBSCRIPTION_DESCRIPTOR,
				"setSubscriptionUserHandle",
				UserHandle.getUserHandleForUid(appUid),0
		);
	}

	@PermissionTest(permission="MANAGE_WEARABLE_SENSING_SERVICE", sdkMin=34)
	public void testManageWearableSensingService(){
		//Object callback = ReflectionUtils.stubRemoteCallback();
		//ParcelFileDescriptor descriptor = ParcelFileDescriptor//
		Consumer<Integer> statusCb = new Consumer<Integer>() {
			@Override
			public void accept(Integer integer) {

			}
		};

		try {
			@SuppressLint("WrongConstant") Object wearableSensing
					= mContext.getSystemService("wearable_sensing");
			ParcelFileDescriptor[] descriptors = ParcelFileDescriptor.createPipe();
			//wearableSensing.
			ReflectionUtil.invoke(wearableSensing,
					"provideDataStream",
					new Class[]{ParcelFileDescriptor.class, Executor.class, Consumer.class},
					descriptors[0], mContext.getMainExecutor(), statusCb);
		} catch (SecurityException e){
			throw e;
		} catch (Exception e) {
			throw new UnexpectedTestFailureException(e);
		}


	}

	@PermissionTest(permission="MODIFY_AUDIO_SETTINGS_PRIVILEGED", sdkMin=34)
	public void testModifyAudioSettingsPrivileged(){
		BinderTransaction.getInstance().invoke(
				Context.AUDIO_SERVICE,
				Transacts.AUDIO_DESCRIPTOR,
				"setVolumeGroupVolumeIndex",
				0,0,0
		);

	}

	@RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @PermissionTest(permission="MODIFY_HDR_CONVERSION_MODE", sdkMin=34)
	public void testModifyHdrConversionMode(){
		BinderTransaction.getInstance().invoke(
				Context.DISPLAY_SERVICE,
				Transacts.DISPLAY_DESCRIPTOR,
				"setHdrConversionMode",
				new HdrConversionMode(HdrConversionMode.HDR_CONVERSION_SYSTEM)
		);
	}

	@PermissionTest(permission="MONITOR_KEYBOARD_BACKLIGHT", sdkMin=34)
	public void testMonitorKeyboardBacklight(){
		BinderTransaction.getInstance().invoke(
				Context.INPUT_SERVICE,
				Transacts.INPUT_DESCRIPTOR,
				"registerKeyboardBacklightListener",
				getActivityToken());
	}

	@PermissionTest(permission="REMAP_MODIFIER_KEYS", sdkMin=34)
	public void testRemapModifierKeys(){
		BinderTransaction.getInstance().invoke(
				Context.INPUT_SERVICE,
				Transacts.INPUT_DESCRIPTOR,
				"getModifierKeyRemapping");

	}

	@PermissionTest(permission="SATELLITE_COMMUNICATION", sdkMin=34,sdkMax = 35)
	public void testSatelliteCommunication(){
		BinderTransaction.getInstance().invoke(
				Context.TELEPHONY_SERVICE,
				Transacts.TELEPHONY_DESCRIPTOR,
				"requestIsSatelliteEnabled",0
				,new android.os.ResultReceiver(new Handler(Looper.getMainLooper())));
	}

	@RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @PermissionTest(permission="SET_APP_SPECIFIC_LOCALECONFIG", sdkMin=34)
	public void testSetAppSpecificLocaleconfig(){
		if(!checkPermissionGranted(QUERY_ALL_PACKAGES)){
			throw new BypassTestException(
					"This test works only when QUERY_ALL_PACKAGES permission is granted.");
		}

		LocaleList OVERRIDE_LOCALES =
				LocaleList.forLanguageTags("en-US,fr-FR,zh-Hant-TW");

		//for checking it need to set other application locale config
		BinderTransaction.getInstance().invoke(
				Context.LOCALE_SERVICE,
				Transacts.LOCALE_DESCRIPTOR,
				"setOverrideLocaleConfig",
				"com.android.certifications.niap.permissions.companion",0,
				new LocaleConfig(OVERRIDE_LOCALES));

	}

	@PermissionTest(permission="SET_LOW_POWER_STANDBY_PORTS", sdkMin=34)
	public void testSetLowPowerStandbyPorts(){
		BinderTransaction.getInstance().invoke(
				Context.POWER_SERVICE,
				Transacts.POWER_DESCRIPTOR,
				"releaseLowPowerStandbyPorts",
				getActivityToken());

	}

	@PermissionTest(permission="TEST_INPUT_METHOD", sdkMin=34)
	public void testTestInputMethod(){
		BinderTransaction.getInstance().invoke(
				Context.INPUT_METHOD_SERVICE,
				Transacts.INPUTMETHOD_DESCRIPTOR,
				"isInputMethodPickerShownForTest"
		);
	}

	@PermissionTest(permission="TURN_SCREEN_ON", sdkMin=34)
	public void testTurnScreenOn(){
		//Hidden Options
		final int SYSTEM_WAKELOCK = 0x80000000;
		final int ACQUIRE_CAUSES_WAKEUP = 0x10000000;
		BinderTransaction.getInstance().invoke(
				Context.POWER_SERVICE,
				Transacts.POWER_DESCRIPTOR,
				"acquireWakeLock",
				getActivityToken(),SCREEN_DIM_WAKE_LOCK|SYSTEM_WAKELOCK|ACQUIRE_CAUSES_WAKEUP,
				"tag",mContext.getPackageName(),
				new WorkSource(),"historyTag",0,null
		);

	}

	@RequiresApi(api = Build.VERSION_CODES.Q)
    @PermissionTest(permission="MANAGE_DEFAULT_APPLICATIONS", sdkMin=34)
	public void testManageDefaultApplications(){
		BinderTransaction.getInstance().invoke(
				Context.ROLE_SERVICE,
				Transacts.ROLE_DESCRIPTOR,
				"getDefaultApplicationAsUser",
				"dummy",appUid
		);
	}

	@PermissionTest(permission="KILL_ALL_BACKGROUND_PROCESSES", sdkMin=34)
	public void testKillAllBackgroundProcesses(){
		BinderTransaction.getInstance().invoke(
				Transacts.ACTIVITY_SERVICE,
				Transacts.ACTIVITY_DESCRIPTOR,
				"killAllBackgroundProcesses");

	}

	@PermissionTest(permission="CHECK_REMOTE_LOCKSCREEN", sdkMin=34)
	public void testCheckRemoteLockscreen(){
		KeyguardManager manager = (KeyguardManager)
				mContext.getSystemService(Context.KEYGUARD_SERVICE);
		if(manager.isDeviceSecure()) {

			BinderTransaction.getInstance().invoke(
					Transacts.LOCK_SETTINGS_SERVICE,
					Transacts.LOCK_SETTINGS_DESCRIPTOR,
					"startRemoteLockscreenValidation");
		} else {
			throw new BypassTestException("The test is working when device is secured.");
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @PermissionTest(permission="REQUEST_COMPANION_PROFILE_NEARBY_DEVICE_STREAMING", sdkMin=34)
	public void testRequestCompanionProfileNearbyDeviceStreaming(){

		CompletableFuture<AssociationRequest> associationRequest =
				new CompletableFuture<AssociationRequest>().completeAsync(() ->
						new AssociationRequest.Builder().setDeviceProfile(
								AssociationRequest.DEVICE_PROFILE_NEARBY_DEVICE_STREAMING).build());
		TesterUtils.tryBluetoothAssociationRequest
				(mPackageManager, mActivity, associationRequest);

	}

	@PermissionTest(permission="LAUNCH_CREDENTIAL_SELECTOR", sdkMin=34)
	public void testLaunchCredentialSelector(){
		if (Constants.BYPASS_TESTS_AFFECTING_UI)
			throw new BypassTestException("This test case affects to UI. skip to avoiding ui stuck.");

		Intent featuresIntent = new Intent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		featuresIntent.setComponent(new
				ComponentName("com.android.credentialmanager",
				"com.android.credentialmanager.CredentialSelectorActivity"));
		mActivity.startActivity(featuresIntent);
	}

	@PermissionTest(permission="WRITE_ALLOWLISTED_DEVICE_CONFIG", sdkMin=34)
	public void testWriteAllowlistedDeviceConfig(){
		//frameworks/base/core/java/android/provider/Settings.java
		Uri CONTENT_URI = Uri.parse("content://settings/config");
		//CALL_METHOD_SET_ALL_CONFIG = "SET_ALL_config";
		mContentResolver.call(
				CONTENT_URI,
				"SET_ALL_config",null,null);
	}

	@PermissionTest(permission="READ_WRITE_SYNC_DISABLED_MODE_CONFIG", sdkMin=34)
	public void testReadWriteSyncDisabledModeConfig(){
		//commonize the tester routine with exposing the builder of AssociationRequest object

		//frameworks/base/core/java/android/provider/Settings.java
		Uri CONTENT_URI = Uri.parse("content://settings/config");
		String CALL_METHOD_SYNC_DISABLED_MODE_KEY = "_disabled_mode";
		Bundle args = new Bundle();
		args.putInt(CALL_METHOD_SYNC_DISABLED_MODE_KEY,1);
		mContentResolver.call(
				CONTENT_URI,
				"SET_SYNC_DISABLED_MODE_config",null,args);

	}



	@RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @PermissionTest(permission="BROADCAST_OPTION_INTERACTIVE", sdkMin=34,developmentProtection = true)
	public void testBroadcastOptionInteractive(){

		final String[] requiredPermissions = {"android.permission.BROADCAST_OPTION_INTERACTIVE"};
		final String[] excludePermissions = {};
		final String[] excludePackages = {};
		final BroadcastOptions bOptions;
		bOptions = BroadcastOptions.makeBasic();
		//setInteractive true
		ReflectionUtil.invoke(bOptions,
				"setInteractive",
				new Class<?>[]{boolean.class},true
		);
		//isAlarmBroadcast() should be true
		ReflectionUtil.invoke(bOptions,
				"setAlarmBroadcast",
				new Class<?>[]{boolean.class},false
		);
		Intent[] intents = new Intent[]{new Intent(Intent.ACTION_VIEW)};

		Object activityThread = null;
		try {
			Class<?> actThreadCls = Class.forName("android.app.ActivityThread");
			Method getActivityThread = actThreadCls.getDeclaredMethod("currentActivityThread");
			getActivityThread.setAccessible(true);
			activityThread = getActivityThread.invoke(null);

		} catch (Exception e){
			throw new UnexpectedTestFailureException(e);
		}

		if(activityThread != null){
			Object applicationThread = ReflectionUtil.invoke
					(activityThread,"getApplicationThread");
			for(Intent i:intents) {
				try {
					BinderTransaction.getInstance().invoke(
							Context.ACTIVITY_SERVICE,
							Transacts.ACTIVITY_DESCRIPTOR,
							"broadcastIntentWithFeature",
							applicationThread, "callingFeatureId",
							i, "resolvedType", null, 0,
							"resultData", new Bundle(), requiredPermissions, excludePermissions,
							excludePackages, 0 /*appOp*/, bOptions.toBundle(), true, false, Binder.getCallingUid());

				} catch (Exception ex){
					String name = ex.getClass().getSimpleName();
					ex.printStackTrace();
					//logger.system(name);
					if(name.equals("SecurityException")) {
						throw ex;
					}
				}
			}
		}
	}

//	@PermissionTest(permission="WAKEUP_SURFACE_FLINGER", sdkMin=34)
//	public void testWakeupSurfaceFlinger(){
//		//commonize the tester routine with exposing the builder of AssociationRequest object
//		if (android.os.Build.VERSION.SDK_INT >= VERSION_CODES.UPSIDE_DOWN_CAKE) {
//			//BinderTransaction.getInstance().invoke(Transacts.SURFACE_FLINGER_SERVICE,
//			//        Transacts.SURFACE_FLINGER_DESCRIPTOR, "showCpu");
//			throw new BypassTestException("WAKEUP_SURFACE_FLINGER permission is bypassed");
//		}
//	}

	@RequiresApi(api = Build.VERSION_CODES.Q)
    @PermissionTest(permission="MANAGE_FACE", sdkMin=34)
	public void testManageFace(){
		if (!mPackageManager.hasSystemFeature(PackageManager.FEATURE_FACE)) {
			throw new BypassTestException("This permission requires the feature "
					+ PackageManager.FEATURE_FACE);
		}
		runShellCommandTest("cmd face sync");
		
	}


	@RequiresApi(api = Build.VERSION_CODES.Q)
    @PermissionTest(permission="SCREEN_TIMEOUT_OVERRIDE", sdkMin=34)
	public void testScreenTimeoutOverride() {
		//Check EarlyScreenTimeoutDetectorEnable Flag Here and skip if it is disabled
		//test method : isWakeLockLevelSupported(int level) {(PowerManager.SCREEN_TIMEOUT_OVERRIDE_WAKE_LOCK)
		final int SCREEN_TIMEOUT_OVERRIDE_WAKE_LOCK = 0x00000100;
		Parcel isSupported = BinderTransaction.getInstance().invoke(Transacts.POWER_SERVICE, Transacts.POWER_DESCRIPTOR,
				"isWakeLockLevelSupported",
				SCREEN_TIMEOUT_OVERRIDE_WAKE_LOCK);
		if (!isSupported.readBoolean()) {
			throw new BypassTestException("WakeLock feature SCREEN_TIMEOUT_OVERRIDE_WAKE_LOCK is not supported on this device. Bypassed");
		}

		BinderTransaction.getInstance().invoke(Transacts.POWER_SERVICE, Transacts.POWER_DESCRIPTOR,
				"acquireWakeLock",
				new Binder(),
				SCREEN_TIMEOUT_OVERRIDE_WAKE_LOCK,
				"tag", mContext.getPackageName(),
				new WorkSource(), "historyTag", 1, new IWakeLockCallback.Stub() {
					@Override
					public void onStateChanged(boolean enabled) throws RemoteException {

					}
				});
	}

	@PermissionTest(permission = "MANAGE_PROFILE_AND_DEVICE_OWNERS",sdkMax = 34)
	public void testManageProfileAndDeviceOwners() {
		// Tests fails with a SecurityException without the permission but still
		// fails with the permission with another exception since the device owner
		// cannot be set if the device is already setup-up.
		ComponentName componentName = new ComponentName(mContext, MainActivity.class);
		try {
			//Interface changes since sdk 34
			BinderTransaction.getInstance().invoke(Transacts.DEVICE_POLICY_SERVICE,
					Transacts.DEVICE_POLICY_DESCRIPTOR,
					"setDeviceOwner",
					componentName, appUid);
		} catch(IllegalStateException ex){
			//expected ignore
		}
	}


	@PermissionTest(permission="MANAGE_CREDENTIAL_MANAGEMENT_APP", sdkMin=34)
	public void testManageCredentialManagementApp(){

		Intent intent = new Intent();
		intent.setAction("android.security.IKeyChainService");
		intent.setComponent(new ComponentName(Constants.KEY_CHAIN_PACKAGE,
			Constants.KEY_CHAIN_PACKAGE + ".KeyChainService"));
		Thread thread = new Thread(() -> {
			boolean permissionGranted =
					checkPermissionGranted("android.permission.MANAGE_CREDENTIAL_MANAGEMENT_APP");
			//logger.debug("Running MANAGE_CREDENTIAL_MANAGEMENT_APP test case.");
			try {
				if(permissionGranted)
					KeyChain.removeCredentialManagementApp(mContext);
//					getAndLogTestStatus(permission.MANAGE_CREDENTIAL_MANAGEMENT_APP,
//							permissionGranted, true);
			} catch (Exception ex){
				if(ex.getClass().getSimpleName().equals("SecurityException")){
//						getAndLogTestStatus(permission.MANAGE_CREDENTIAL_MANAGEMENT_APP,
//								permissionGranted, false);
					throw ex;
				}
			}
		});
		thread.start();
		try {
			thread.join(500);
			throw new BypassTestException("The test launch on the new thread, it will finish after other test cases.");
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

	}

	@SuppressLint("PrivateApi")
	@PermissionTest(permission="CAMERA_INJECT_EXTERNAL_CAMERA", sdkMin=34)
	public void testCameraInjectExternalCamera(){
		Object injectionCallback;
		Object injectionSession;
		try {
			Class<?> injectionCallbackClass = Class.forName(
					"android.hardware.camera2.ICameraInjectionCallback");
			injectionCallback = Proxy.newProxyInstance(
					injectionCallbackClass.getClassLoader(),
					new Class[]{injectionCallbackClass}, new InvocationHandler() {
						@Override
						public Object invoke(Object o, Method method, Object[] objects)
								throws Throwable {
							logger.debug("injectionCallback#invoke: " + method);
							if (method.toString().contains("asBinder")) {
								return new Binder();
							} else if (method.toString().contains("onInjectionError")) {
								return null;
							}
							return null;
						}
					});
			Class<?> injectionSessionClass = Class.forName(
					"android.hardware.camera2.ICameraInjectionSession");
			injectionSession = Proxy.newProxyInstance(
					injectionSessionClass.getClassLoader(),
					new Class[]{injectionSessionClass}, new InvocationHandler() {
						@Override
						public Object invoke(Object o, Method method, Object[] objects)
								throws Throwable {
							logger.debug("injectionSession#invoke: " + method);
							if (method.toString().contains("asBinder")) {
								return new Binder();
							}
							return null;
						}
					});
		} catch (ClassNotFoundException e) {
			throw new UnexpectedTestFailureException(e);
		}
		try {
			BinderTransaction.getInstance().invoke(Transacts.CAMERA_SERVICE,
					Transacts.CAMERA_DESCRIPTOR,
					"injectCamera", mContext.getPackageName(),
					"",
					"", injectionCallback);

		} catch (SecurityException e) {
			throw e;
		} catch (Exception e) {
			// If the test fails due to this package not holding the required permission
			// a ServiceSpecificException is thrown with the text "Permission Denial"
			String e_message = e.getMessage();
			if(e_message != null && e_message.contains("Permission Denial")){
				//e_message);
				throw new SecurityException(e);
			} else {
				throw e;
			}
		}
	}


}









