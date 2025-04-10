//Auto generated file InstallPermissionTestModule.java by CoderPorterPlugin
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


import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.PACKAGE_USAGE_STATS;
import static android.Manifest.permission.READ_LOGS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.KeyguardManager;
import android.app.UiModeManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.companion.CompanionDeviceManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.display.DisplayManager;
import android.net.ConnectivityManager;
import android.net.MacAddress;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.UserHandle;
import android.provider.Settings;
import android.se.omapi.Reader;
import android.se.omapi.SEService;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.android.certification.niap.permission.dpctester.MainActivity;
import com.android.certification.niap.permission.dpctester.common.Constants;
import com.android.certification.niap.permission.dpctester.common.ReflectionUtil;
import com.android.certification.niap.permission.dpctester.test.exception.BypassTestException;
import com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException;
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestRunner;
import com.android.certification.niap.permission.dpctester.test.runner.SignaturePermissionTestModuleBase;
import com.android.certification.niap.permission.dpctester.test.tool.BinderTransaction;
import com.android.certification.niap.permission.dpctester.test.tool.NetworkStatsProviderStubCompat;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTest;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestModule;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@PermissionTestModule(name="Signature 30(R) Test Cases",prflabel = "R(11)")
public class SignatureTestModuleR extends SignaturePermissionTestModuleBase {
	public SignatureTestModuleR(@NonNull Activity activity){ super(activity);}

	private <T> T systemService(Class<T> clazz){
		return Objects.requireNonNull(getService(clazz),"[npe_system_service]"+clazz.getSimpleName());
	}
	/////////////////////////
	//Regex for converting

	//permission=(.*{0}),
	//permission="$1",

	//([\s])Transacts\.([a-z]{1}[a-zA-z]{1,40})(,|\))
	//$1"$2"$3

	@PermissionTest(permission="ACCESS_CONTEXT_HUB", sdkMin=30)
	public void testAccessContextHub(){
		// As of Android 11 all ACCESS_CONTEXT_HUB guarded methods are also guarded by
		// ACCESS_LOCATION_HARDWARE.
		@SuppressLint("WrongConstant") Object contextHubManager = mContext.getSystemService("contexthub");
		ReflectionUtil.invoke(contextHubManager, "getContextHubs");

    }

	@PermissionTest(permission="ACCESS_LOCUS_ID_USAGE_STATS", sdkMin=30)
	public void testAccessLocusIdUsageStats(){
		// This permission guards access to locus events from other apps; the companion
		// app should first be run to generate a locus event that can be queried by
		// this test.
		boolean locusEventFound = false;
		UsageEvents usageEvents = systemService(UsageStatsManager.class).queryEvents(0, Long.MAX_VALUE);
		UsageEvents.Event event = new UsageEvents.Event();
		while (usageEvents.hasNextEvent()) {
			usageEvents.getNextEvent(event);
			// 30 is the value of the hidden Event.LOCUS_ID_SET field indicating a
			// locus event.
			if (event.getEventType() == 30) {
				logger.debug(
						"Time of locus event: " + event.getTimeStamp() + ", package: "
								+ event.getPackageName());
				locusEventFound = true;
				break;
			}
		}
		if (!locusEventFound) {
			throw new SecurityException(
					"No locus events returned from usage stats query");
		}
	}

	@PermissionTest(permission="ACCESS_MESSAGES_ON_ICC", sdkMin=30)
	public void testAccessMessagesOnIcc(){
		SmsManager smsManager = SmsManager.getSmsManagerForSubscriptionId(0);
		ReflectionUtil.invoke(smsManager, "getAllMessagesFromIcc");
	}

	@PermissionTest(permission="ACCESS_VIBRATOR_STATE", sdkMin=30)
	public void testAccessVibratorState(){
		BinderTransaction.getInstance().invoke(Transacts.VIBRATOR_SERVICE, Transacts.VIBRATOR_DESCRIPTOR,
				"isVibrating");
	}

	@PermissionTest(permission="ASSOCIATE_INPUT_DEVICE_TO_DISPLAY_BY_PORT", sdkMax=30)
	public void testAssociateInputDeviceToDisplayByPort(){
		BinderTransaction.getInstance().invoke(Transacts.INPUT_SERVICE, Transacts.INPUT_DESCRIPTOR,
				"removePortAssociation", "testPort");
	}

	@PermissionTest(permission="COMPANION_APPROVE_WIFI_CONNECTIONS", sdkMin=30)
	public void testCompanionApproveWifiConnections(){
		// CompanionDeviceManager#isDeviceAssociatedForWifiConnection is the only
		// method that is guarded by this permission. While it does check
		// MANAGE_COMPANION_DEVICES first it will ultimately return true if all of
		// the permission checks are successful, including the check for
		// COMPANION_APPROVE_WIFI_CONNECTIONS.
		CompanionDeviceManager companionDeviceManager = systemService(CompanionDeviceManager.class);

		Object result = ReflectionUtil.invoke(companionDeviceManager,
				"isDeviceAssociatedForWifiConnection",
				new Class[]{String.class, MacAddress.class, UserHandle.class},
				mContext.getPackageName(), MacAddress.BROADCAST_ADDRESS,
				UserHandle.getUserHandleForUid(appUid));
		if (result instanceof Boolean && !((Boolean) result)) {
			throw new SecurityException(
					"isDeviceAssociatedForWifiConnection returned false indicating "
							+ "permission is not granted");
		}
	}

	@PermissionTest(permission="CONFIGURE_INTERACT_ACROSS_PROFILES", sdkMin=30)
	public void testConfigureInteractAcrossProfiles(){
		BinderTransaction.getInstance().invoke(Transacts.CROSS_PROFILE_APPS_SERVICE,
				Transacts.CROSS_PROFILE_APPS_DESCRIPTOR,
				"clearInteractAcrossProfilesAppOps");
	}

	@PermissionTest(permission="CONTROL_DEVICE_LIGHTS", sdkMin=30)
	public void testControlDeviceLights(){
		@SuppressLint("WrongConstant") Object lightsManager = mContext.getSystemService("lights");
		ReflectionUtil.invoke(lightsManager, "getLights");
	}

	@PermissionTest(permission="ENTER_CAR_MODE_PRIORITIZED", sdkMax=34)
	public void testEnterCarModePrioritized(){
		if(Constants.BYPASS_TESTS_AFFECTING_UI)
			throw new BypassTestException("This test case affects to UI. skip to avoiding ui stuck.");

		UiModeManager uiModeManager = systemService(UiModeManager.class);
		//mContext.getSystemService(
		//		Context.UI_MODE_SERVICE);
		// The reflective call is required since a priority other than 0 must be
		// specified to test this permission.
		ReflectionUtil.invoke(uiModeManager, "enableCarMode",
				new Class[]{int.class, int.class}, 1, 0);
		uiModeManager.disableCarMode(UiModeManager.DISABLE_CAR_MODE_GO_HOME);

	}


	@PermissionTest(permission="KEYPHRASE_ENROLLMENT_APPLICATION", sdkMin=30)
	public void testKeyphraseEnrollmentApplication(){
		// need to create SoundTrigger.KeyphraseSoundModel object for right test
		try {
			BinderTransaction.getInstance().invoke(Transacts.VOICE_INTERACTION_SERVICE,
					Transacts.VOICE_INTERACTION_DESCRIPTOR,
					"updateKeyphraseSoundModel", (Object) null);
		} catch(IllegalArgumentException ignored){
			logger.info("Expected IllegalArgumentException,the permission check is executed before checking model");
		}


	}

	@PermissionTest(permission="LISTEN_ALWAYS_REPORTED_SIGNAL_STRENGTH", sdkMin=30,sdkMax = 32)
	public void testListenAlwaysReportedSignalStrength(){

		HandlerThread handlerThread = new HandlerThread(getTAG());
		handlerThread.start();
		Handler handler = new Handler(handlerThread.getLooper());
		// An Exception array is used since local variables referenced from a lambda
		// expression must be effectively final.
		//https://cs.android.com/android/platform/superproject/+/master:cts/tests/tests/telephony/current/src/android/telephony/cts/PhoneStateListenerTest.java;l=328;drc=e64188140ba71c7b7424b044119b37af1dde6609?
		Exception[] caughtException = new Exception[1];
		CountDownLatch latch = new CountDownLatch(1);
		handler.post(() -> {
			PhoneStateListener listener = new PhoneStateListener() {
				@Override
				public void onSignalStrengthsChanged(SignalStrength signalStrength) {
					logger.debug("onSignalStrengthChanged: signalStrength = "
							+ signalStrength);
				}
			};
			try {
				systemService(TelephonyManager.class).listen(listener, 0x00000200);
			} catch (Exception e) {
				caughtException[0] = e;
				e.printStackTrace();
			}
			latch.countDown();
		});
		try {
			latch.await(2000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (caughtException[0] instanceof SecurityException) {
			throw (SecurityException) caughtException[0];
		}

		/*
		//TODO: Need Multiple Version Support
		if (Build.VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
			//The way to using the listner was obsolated after android T, so let me choose using this option instead
			//https://cs.android.com/android/platform/superproject/+/master:cts/tests/tests/telephony/current/src/android/telephony/cts/PhoneStateListenerTest.java;l=328;drc=e64188140ba71c7b7424b044119b37af1dde6609?=4186
			//problem : if the signature does not match it always fail,because the method also checks MODIFY_PHONE_STATE permission

			SignalStrengthUpdateRequest.Builder builder = new SignalStrengthUpdateRequest.Builder()
					.setSignalThresholdInfos(Collections.EMPTY_LIST);

			builder = (SignalStrengthUpdateRequest.Builder)
					ReflectionUtil.invoke(SignalStrengthUpdateRequest.Builder.class,
							"setSystemThresholdReportingRequestedWhileIdle", builder,
							new Class<?>[]{boolean.class},true);
			SignalStrengthUpdateRequest request = builder.build();
			try {
				mTelephonyManager.setSignalStrengthUpdateRequest(request);
			} catch (IllegalStateException ex){
				logger.logInfo("Expected:"+ex.getMessage());
			}

		} else {

		}*/

	}

	@PermissionTest(permission="LOG_COMPAT_CHANGE", sdkMin=30)
	public void testLogCompatChange(){
		BinderTransaction.getInstance().invoke(Transacts.PLATFORM_COMPAT_SERVICE,
				Transacts.PLATFORM_COMPAT_DESCRIPTOR,
				"reportChangeByUid", 0, appUid);
	}

	@RequiresApi(api = Build.VERSION_CODES.R)
    @PermissionTest(permission="MANAGE_EXTERNAL_STORAGE", sdkMin=30)
	public void testManageExternalStorage(){
		// Only an app granted this permission will receive a value of true back from
		// this API.
		if(Constants.BYPASS_TESTS_AFFECTING_UI)
			throw new BypassTestException("This test case affects to UI. skip to avoiding ui stuck.");

		if (!Environment.isExternalStorageManager()) {
			throw new SecurityException(
					"MANAGE_EXTERNAL_STORAGE: Test app is not the external storage "
							+ "manager");
		}
		// This activity can then be started to verify that this app has been granted
		// this permission; if the permission is not granted the following will be
		// in logcat:
		// CacheClearingActivity: Calling package com.android.permissions.tester
		//   has no permission clear app caches
		Intent intent = new Intent();
		intent.setAction("android.os.storage.action.CLEAR_APP_CACHE");
		intent.addCategory("android.intent.category.DEFAULT");
		mActivity.startActivityForResult(intent, 0);

	}

	@PermissionTest(permission="MARK_DEVICE_ORGANIZATION_OWNED", sdkMin=30,sdkMax = 32)
	public void testMarkDeviceOrganizationOwned(){
		ComponentName componentName = new ComponentName(mContext, MainActivity.class);

		// If the permission is granted an active admin must first be granted to
		// verify this test; since this is outside the scope of a permission test skip
		// the test when the permission is granted.

		if(checkPermissionGranted("android.permission.MARK_DEVICE_ORGANIZATION_OWNED")
			&& !acceptDangerousApi){
			throw new BypassTestException(
					"MARK_DEVICE_ORGANIZATION_OWNED. This permission requires an active admin to be set");
		}

		BinderTransaction.getInstance().invoke(Transacts.DEVICE_POLICY_SERVICE,
				Transacts.DEVICE_POLICY_DESCRIPTOR,
				"markProfileOwnerOnOrganizationOwnedDevice", componentName);

		/*
		//TODO: need multiple version support 33
		if (mDeviceApiLevel <= VERSION_CODES.S_V2) {

		} else {
			//Above methods are removed from Android T
			//The below api can also be granted to run with MANAGE_PROFILE_AND_DEVICE_OWNERS
			//permission. For checking please remove both of the permisssions.
			BinderTransaction.getInstance().invoke(Transacts.DEVICE_POLICY_SERVICE,
					Transacts.DEVICE_POLICY_DESCRIPTOR,
					"setProfileOwnerOnOrganizationOwnedDevice",
					componentName,Process.myUid(),true);
		}*/
	}

	@PermissionTest(permission="MEDIA_RESOURCE_OVERRIDE_PID", sdkMin=30)
	public void testMediaResourceOverridePid(){
		try {
			BinderTransaction.getInstance().invoke(Transacts.RESOURCE_MANAGER_SERVICE,
					Transacts.RESOURCE_MANAGER_DESCRIPTOR, "overridePid",
					1000, 1001);
		} catch (Exception e) {
			// The ResourceManagerService fails with a ServiceSpecificException when the
			// permission requirements are not met. Since the ServiceSpecificException
			// class is hidden from apps check the string of the Exception message to
			// confirm the proper Exception was thrown:
			// ServiceManager: Permission failure:
			//   android.permission.MEDIA_RESOURCE_OVERRIDE_PID from uid=10241 pid=18140
			// ResourceManagerService: Permission Denial: can't access overridePid
			//   method from pid=18140, self pid=1217
			// SignaturePermissionTester: android.os.ServiceSpecificException:
			//   (code -1)
			String exceptionMessage = e.toString();
			if (exceptionMessage.contains(("ServiceSpecificException"))) {
				throw new SecurityException(exceptionMessage);
			}
		}
	}

	@PermissionTest(permission="MODIFY_SETTINGS_OVERRIDEABLE_BY_RESTORE", sdkMin=30)
	public void testModifySettingsOverrideableByRestore(){
		ReflectionUtil.invoke(Settings.System.class, "putString",
				new Class[]{ContentResolver.class, String.class, String.class, boolean.class},
				mContext.getContentResolver(), "vibrate_on", "1", true);
	}

	@SuppressLint("PrivateApi")
	@RequiresApi(api = Build.VERSION_CODES.R)
	@PermissionTest(permission="MONITOR_DEVICE_CONFIG_ACCESS", sdkMin=30)
	public void testMonitorDeviceConfigAccess(){
		try {

			Class<?> onResultListenerClass = Class.forName(
					"android.os.RemoteCallback$OnResultListener");
			Object onResultListener = Proxy.newProxyInstance(
					onResultListenerClass.getClassLoader(),
					new Class[]{onResultListenerClass}, new InvocationHandler() {
						@Override
						public Object invoke(Object o, Method method, Object[] objects)
								throws Throwable {
							//logger.debug("invoke: " + method);
							return null;
						}
					});

			Class<?> remoteCallbackClass = Class.forName("android.os.RemoteCallback");
			Constructor<?> remoteCallbackConstructor = remoteCallbackClass.getConstructor(
					onResultListenerClass);
			Object remoteCallback = remoteCallbackConstructor.newInstance(
					(Object) onResultListener);

			Bundle bundle = new Bundle();
			bundle.putInt("_user", 0);
			bundle.putParcelable("_monitor_callback_key", (Parcelable) remoteCallback);

			mContentResolver.call("settings", "REGISTER_MONITOR_CALLBACK_config", null,
					bundle);

		} catch (ReflectiveOperationException e) {
			throw new UnexpectedTestFailureException(e);
		}
	}

	@PermissionTest(permission="NETWORK_AIRPLANE_MODE", sdkMin=30)
	public void testNetworkAirplaneMode(){

		ReflectionUtil.invoke(systemService(ConnectivityManager.class), "setAirplaneMode",
				 new Class[]{boolean.class}, false);

    }

	@PermissionTest(permission="NETWORK_FACTORY", sdkMin=30)
	public void testNetworkFactory(){
		try {
			HandlerThread handlerThread = new HandlerThread(getTAG());
			handlerThread.start();
			Class<?> networkProviderClass = Class.forName("android.net.NetworkProvider");
			Constructor<?> networkProviderConstructor =
					networkProviderClass.getConstructor(Context.class, Looper.class,
							String.class);
			Object networkProvider = networkProviderConstructor.newInstance(mContext,
					handlerThread.getLooper(), "test_network_provider");

			NetworkRequest networkRequest = new NetworkRequest.Builder().addCapability(
					NetworkCapabilities.NET_CAPABILITY_INTERNET).build();
			ConnectivityManager conManager = systemService(ConnectivityManager.class);
			ReflectionUtil.invoke(conManager,
					"unregisterNetworkProvider",
					new Class[]{Class.forName("android.net.NetworkProvider")},
					(Object) networkProvider);
		} catch (ReflectiveOperationException e) {
			throw new UnexpectedTestFailureException(e);
		}
    }

	@PermissionTest(permission="NETWORK_STATS_PROVIDER", sdkMin=30)
	public void testNetworkStatsProvider(){

		BinderTransaction.getInstance().invoke(Transacts.NETWORK_STATS_SERVICE,
				Transacts.NETWORK_STATS_DESCRIPTOR,
				"registerNetworkStatsProvider", "testTag", new NetworkStatsProviderStubCompat());
	}

	@PermissionTest(permission="OBSERVE_NETWORK_POLICY", sdkMin=30,sdkMax = 30)
	public void testObserveNetworkPolicy(){
		BinderTransaction.getInstance().invoke(Transacts.NET_POLICY_SERVICE,
				Transacts.NET_POLICY_DESCRIPTOR,
				"registerListener", (Object) null);
		//TODO: newe support MultipleVersion 31
		/*
		if (mDeviceApiLevel < VERSION_CODES.S) {

		} else {
			BinderTransaction.getInstance().invoke(Transacts.NET_POLICY_SERVICE,
					Transacts.NET_POLICY_DESCRIPTOR, "isUidNetworkingBlocked",
					appUid, false);
		}*/
	}

	@PermissionTest(permission="OVERRIDE_COMPAT_CHANGE_CONFIG", sdkMin=30)
	public void testOverrideCompatChangeConfig(){
		BinderTransaction.getInstance().invoke(Transacts.PLATFORM_COMPAT_SERVICE,
				Transacts.PLATFORM_COMPAT_DESCRIPTOR,
				"clearOverridesForTest", mContext.getPackageName());
	}

	@PermissionTest(permission="PEEK_DROPBOX_DATA", sdkMin=30)
	public void testPeekDropboxData(){

		//Just to be safe, if the PEEK_DROPBOX_DATA permission is not granted
		//but READ_LOGS and PACKAGE_USAGE_STATS are granted,
		//I think we should skip the test since it looks like these development permissions
		//that are granted with the -g flag will always allow the normal variant to access dropbox data.
		if(checkPermissionGranted(READ_LOGS) && checkPermissionGranted(PACKAGE_USAGE_STATS)){
			if(!checkPermissionGranted("android.permission.PEEK_DROPBOX_DATA")){
				throw new BypassTestException(
						"Bypass the check due to avoid unexpected behaviour. ");
			}
		}

		long currTimeMs = System.currentTimeMillis();

		//#add a line from companion app.=>need to run the companion app before testing
		//final DropBoxManager db = (DropBoxManager) mContext.getSystemService(Context.DROPBOX_SERVICE);
		//db.addText("test-tag","PEEK_DROPBOX_DATA test at :"+currTimeMs);

		Parcel result= BinderTransaction.getInstance().invoke(Transacts.DROPBOX_SERVICE,
				Transacts.DROPBOX_DESCRIPTOR,
				"getNextEntry", "test-companion-tag", currTimeMs-(1000*60*60*8), mContext.getPackageName());

		if (result.readInt() == 0) {
			throw new SecurityException(
					"Received DropBoxManager.Entry is null during PEEK_DROPBOX_DATA "
							+ "test. Please check you surely executed companion app before testing.");
		}
		DropBoxManager.Entry entry = DropBoxManager.Entry.CREATOR.createFromParcel(
				result);

		logger.debug(
				"Successfully parsed entry from parcel: " + entry.getText(100));
	}

	@PermissionTest(permission="RADIO_SCAN_WITHOUT_LOCATION", sdkMin=30)
	public void testRadioScanWithoutLocation(){
		// if the app has been granted a location permission then skip this test as this
		// permission is intended to allow scans without location.
		if (checkPermissionGranted(ACCESS_COARSE_LOCATION)||checkPermissionGranted(ACCESS_FINE_LOCATION)){
			throw new BypassTestException(
					"This app has been granted a location permission");
		}
		boolean scanStartedSuccessfully = systemService(WifiManager.class).startScan();
		if(!scanStartedSuccessfully) {
			throw new SecurityException(
					"Wifi scan could not be started during "
							+ "RADIO_SCAN_WITHOUT_LOCATION test");
		}
	}

	@SuppressLint("PrivateApi")
    @PermissionTest(permission="READ_COMPAT_CHANGE_CONFIG", sdkMin=30)
	public void testReadCompatChangeConfig(){
		try {
			ReflectionUtil.invoke(Class.forName("android.app.compat.CompatChanges"),
					"isChangeEnabled",
					new Class[]{long.class, String.class, UserHandle.class}, 0,
					"android", UserHandle.getUserHandleForUid(appUid));
		} catch (ReflectiveOperationException e) {
			throw new UnexpectedTestFailureException(e);
		}
	}

	@PermissionTest(permission="RESTORE_RUNTIME_PERMISSIONS", sdkMin=30)
	public void testRestoreRuntimePermissions(){
		@SuppressLint("WrongConstant") Object permissionControllerManager = mContext.getSystemService(
				"permission_controller");
		ReflectionUtil.invoke(permissionControllerManager,
				"stageAndApplyRuntimePermissionsBackup",
				new Class[]{byte[].class, UserHandle.class}, new byte[0],
				UserHandle.getUserHandleForUid(appUid));
	}

	@PermissionTest(permission="SECURE_ELEMENT_PRIVILEGED_OPERATION", sdkMin=30)
	public void testSecureElementPrivilegedOperation(){
		ExecutorService executor = Executors.newSingleThreadExecutor();
		final CountDownLatch latch = new CountDownLatch(1);
		SEService.OnConnectedListener listener = () -> {
			logger.debug("SECURE_ELEMENT_PRIVILEGED_OPERATION: onConnect invoked");
			latch.countDown();
		};
		SEService seService = new SEService(mContext, executor, listener);
		try {
			boolean connected = latch.await(2000, TimeUnit.MILLISECONDS);
			if (!connected) {
				throw new BypassTestException(
						"Unable to establish a connection to the SEService");
			}
		} catch (InterruptedException e) {
			throw new UnexpectedTestFailureException(e);
		}
		Reader[] readers = seService.getReaders();
		if (readers.length == 0) {
			throw new BypassTestException(
					"No Secure Element Readers available on the device");
		}
		boolean resetSuccessful = false;
		for (Reader reader : readers) {
			logger.debug("About to invoke method on isSecureElement: "
					+ reader.isSecureElementPresent());
			// If the HAL is not available to invoke the reset then a value of false
			// will be returned before making it to the permission check.
            Object result = null;
			result = ReflectionUtil.invoke(reader, "reset");

            if (result instanceof Boolean
					&& (Boolean) result) {
				// a result of true means the permission check was passed and the
				// reset was successfully invoked.
				resetSuccessful = true;
				break;
			}
		}
		if (!resetSuccessful) {
			throw new BypassTestException(
					"The SecureElement HAL v1.2 is not available for this test");
		}
	}

	@PermissionTest(permission="SET_INITIAL_LOCK", sdkMin=30)
	public void testSetInitialLock(){

		ReflectionUtil.invoke(systemService(KeyguardManager.class), "setLock",
				new Class[]{int.class, byte[].class, int.class}, 0, new byte[0], 0);
	}

	@PermissionTest(permission="SYSTEM_CAMERA", sdkMin=30)
	public void testSystemCamera(){
		try {
			CameraManager cameraManager = systemService(CameraManager.class);
			String[] cameraIds = cameraManager.getCameraIdList();
			for (String cameraId : cameraIds) {
				CameraCharacteristics cameraCharacteristics =
						cameraManager.getCameraCharacteristics(cameraId);

				for (int capability : Objects.requireNonNull(cameraCharacteristics.get(
                        CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES))) {
					// If a system camera was found then this indicates the API was
					// successful
					if (capability
							== CameraCharacteristics
							.REQUEST_AVAILABLE_CAPABILITIES_SYSTEM_CAMERA) {
						logger.debug("Found a system camera with ID " + cameraId);
						return;
					}
				}

			}
		} catch (CameraAccessException e) {
			throw new UnexpectedTestFailureException(e);
		}
		// If this point is reached there's no guarantee that the device has any
		// system cameras, so report a bypass for the test.
		throw new BypassTestException("No system cameras reported on the device");
	}

	@PermissionTest(permission="UPGRADE_RUNTIME_PERMISSIONS", sdkMin=30)
	public void testUpgradeRuntimePermissions(){
		@SuppressLint("WrongConstant") Object permissionManager = mContext.getSystemService("permission");
		ReflectionUtil.invoke(permissionManager,
				"getRuntimePermissionsVersion");
	}

	@PermissionTest(permission="VIBRATE_ALWAYS_ON", sdkMin=30)
	public void testVibrateAlwaysOn(){
		BinderTransaction.getInstance().invoke(Transacts.VIBRATOR_SERVICE, Transacts.VIBRATOR_DESCRIPTOR,
				"setAlwaysOnEffect", appUid, mContext.getPackageName(), 0, null,
				null);
	}

	@PermissionTest(permission="WHITELIST_AUTO_REVOKE_PERMISSIONS", sdkMin=30,sdkMax=30)
	public void testWhitelistAutoRevokePermissions(){

		BinderTransaction.getInstance().invoke(Transacts.PERMISSION_MANAGER_SERVICE,
				Transacts.PERMISSION_MANAGER_DESCRIPTOR,
				"isAutoRevokeWhitelisted", mContext.getPackageName(), true,
				0);
		//TODO : Multiple Version Support 31
		/*
		if (mDeviceApiLevel < VERSION_CODES.S) {

		} else {
			BinderTransaction.getInstance().invoke(Transacts.PERMISSION_MANAGER_SERVICE,
					Transacts.PERMISSION_MANAGER_DESCRIPTOR,
					"isAutoRevokeExempted", mPackageName, 0);
		}*/
	}

	@PermissionTest(permission="USE_INSTALLER_V2", sdkMin=30)
	public void testUseInstallerV2(){
		PackageInstaller packageInstaller = mPackageManager.getPackageInstaller();
		try {
			int sessionId = packageInstaller.createSession(
					new PackageInstaller.SessionParams(
							PackageInstaller.SessionParams.MODE_FULL_INSTALL));
			PackageInstaller.Session session = packageInstaller.openSession(sessionId);
			ReflectionUtil.invoke(session, "getDataLoaderParams");
		} catch (IOException | ReflectionUtil.ReflectionIsTemporaryException e) {
			throw new UnexpectedTestFailureException(e);
		}
	}

	@SuppressLint("WrongConstant")
    @PermissionTest(permission="ADD_TRUSTED_DISPLAY", sdkMin=30)
	public void testAddTrustedDisplay(){
		// DisplayManager#VIRTUAL_DISPLAY_FLAG_TRUSTED is set to 1 << 10, but the
		// flag is hidden so use the constant value for this test.
		systemService(DisplayManager.class)
				.createVirtualDisplay(getTAG(), 10, 10, 1, null,
				1 << 10);
	}
	@PermissionTest(permission="TETHER_PRIVILEGED",sdkMin = 30)
	public void testTetherPrivileged(){

		ReflectionUtil.invoke(systemService(ConnectivityManager.class),
				"isTetheringSupported");

	}

	@PermissionTest(permission="MANAGE_BIOMETRIC_DIALOG", sdkMin=30,sdkMax = 32)
	public void testManageBiometricDialog(){
		BinderTransaction.getInstance().invoke(Transacts.STATUS_BAR_SERVICE,
				Transacts.STATUS_BAR_DESCRIPTOR,
				"onBiometricHelp", "test");
	}

	@PermissionTest(permission="MANAGE_APPOPS", sdkMin=30)
	public void testManageAppops(){
		BinderTransaction.getInstance().invoke(Transacts.APP_OPS_SERVICE,
				Transacts.APP_OPS_DESCRIPTOR,
				"clearHistory");
	}
}









