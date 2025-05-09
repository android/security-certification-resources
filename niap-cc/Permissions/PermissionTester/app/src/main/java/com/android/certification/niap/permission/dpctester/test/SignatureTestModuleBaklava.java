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


import static android.content.Context.ADVANCED_PROTECTION_SERVICE;
import static android.content.Context.INPUT_SERVICE;
import static android.content.Context.USER_SERVICE;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.hardware.input.AidlKeyGestureEvent;
import android.hardware.input.IKeyEventActivityListener;
import android.hardware.input.IKeyGestureEventListener;
import android.hardware.input.InputManager;
import android.health.connect.HealthConnectManager;
import android.health.connect.aidl.HealthConnectExceptionParcel;
import android.health.connect.aidl.IGetChangesForBackupResponseCallback;
import android.health.connect.backuprestore.GetChangesForBackupResponse;
import android.media.quality.MediaQualityManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.OutcomeReceiver;
import android.os.PowerManager;
import android.os.PowerMonitor;
import android.os.PowerMonitorReadings;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.VibrationAttributes;
import android.os.VibratorManager;
import android.os.health.SystemHealthManager;
import android.provider.ContactsContract;
import android.security.advancedprotection.AdvancedProtectionManager;
import android.security.authenticationpolicy.EnableSecureLockDeviceParams;
import android.security.intrusiondetection.IIntrusionDetectionServiceCommandCallback;
import android.security.intrusiondetection.IIntrusionDetectionServiceStateCallback;
import android.service.settings.preferences.MetadataRequest;
import android.service.settings.preferences.MetadataResult;
import android.service.settings.preferences.SetValueRequest;
import android.service.settings.preferences.SetValueResult;
import android.service.settings.preferences.SettingsPreferenceService;
import android.service.settings.preferences.SettingsPreferenceServiceClient;
import android.service.settings.preferences.SettingsPreferenceValue;
import android.view.SurfaceControl;
import android.view.textclassifier.TextClassificationManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.PeriodicWorkRequest;

import com.android.certification.niap.permission.dpctester.common.ReflectionUtil;
import com.android.certification.niap.permission.dpctester.test.exception.BypassTestException;
import com.android.certification.niap.permission.dpctester.test.runner.SignaturePermissionTestModuleBase;
import com.android.certification.niap.permission.dpctester.test.tool.BinderTransaction;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTest;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestModule;
import com.android.certification.niap.permission.dpctester.test.tool.ReflectionTool;
import com.android.certification.niap.permission.dpctester.test.tool.ReflectionToolJava;
import com.google.android.satellite.aidl.IBooleanConsumer;
import com.google.android.satellite.aidl.IIntegerConsumer;

import org.junit.Ignore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@PermissionTestModule(name="Signature 36(Baklava) Test Cases",prflabel="Baklava(16)")
public class SignatureTestModuleBaklava extends SignaturePermissionTestModuleBase {
	public SignatureTestModuleBaklava(@NonNull Activity activity) {
		super(activity);
	}

	private <T> T systemService(Class<T> clazz) {
		return Objects.requireNonNull(getService(clazz), "[npe_system_service]" + clazz.getSimpleName());
	}

	/*@PermissionTest(permission="OBSERVE_PICTURE_PROFILES",sdkMin=36)
	public void testObservePictureProfiles(){

		//Can not instantiate this object.
		Object listener = ReflectionToolJava.stubHiddenObjectSub
				("android.view.SurfaceControlActivePictureListener");
		logger.system("aaa:"+listener);

		if(listener == null){
			throw new SecurityException();
		}
		//listener.startListening();
	}*/

//    @RequiresApi(api = 36)
//    @PermissionTest(permission="MANAGE_GLOBAL_PICTURE_QUALITY_SERVICE",sdkMin=36)
//	public void testManageGlobalPictureQualityService(){
//		//MediaQualityManager is not implemented yet?
//		//MediaQUalityManger.getPictureProfilesByPackage may work for the test
//		//MediaQualityManager manager = systemService(MediaQualityManager.class);
//		//but we can not find corresponding service as of now
//		//List<String> methods = ReflectionTool.Companion.checkDeclaredMethod(manager, "get");
//	}

	@RequiresApi(36)
	@PermissionTest(permission = "MANAGE_GLOBAL_SOUND_QUALITY_SERVICE", sdkMin = 36)
	public void testManageGlobalSoundQualityService() {




		//MediaQualityManager.getSoundProfilePackageNames()
		MediaQualityManager manager = systemService(MediaQualityManager.class);
		//but we can not find corresponding service as of now
		List<String> methods = ReflectionTool.Companion.checkDeclaredMethod(manager, "get");

		logger.system(methods.toString());
	}

	@PermissionTest(permission = "THREAD_NETWORK_TESTING", sdkMin = 36, ignore = true)
	public void testThreadNetworkTesting() {

		//*** REASON FOR IGNORE ***
		//*** Below shell command can be only executed by the rooted system

		//  runShellCommand("force-country-code", "enabled", "US");?
		//  It doesn't work work except the system app
		int shellRet = runShellCommand("cmd thread_network get-country-code");
	}

	@PermissionTest(permission = "REMOVE_ACCOUNTS", sdkMin = 35)
	public void testRemoveAccounts() {

		Account account = new Account("dpctester.stub@gmail.com", "com.google");
		//account = accounts[0];
		systemService(AccountManager.class).removeAccount(account, mActivity, new AccountManagerCallback<Bundle>() {
			@Override
			public void run(AccountManagerFuture<Bundle> future) {
				//logger.info("account remove api");
			}
		}, null);
	}


	@PermissionTest(permission = "COPY_ACCOUNTS", sdkMin = 36)
	public void testCopyAccounts() {

		int count = systemService(UserManager.class).getUserCount();
		if (count >= 2) {
			UserManager umanager = systemService(UserManager.class);
			List<UserInfo> users = ReflectionUtil.invoke(umanager, "getUsers");
			AccountManager manager = systemService(AccountManager.class);
			Account account = new Account("dpctester.stub@gmail.com", "com.google");

			//account = accounts[0];

			//https://stackoverflow.com/questions/47027382/cant-find-getusers-method-in-class-usermanager
			//adb shell pm remove-user dummy2

			//Need some prerequisete to test
			//Create other user and switch to it
			//*disable* INTERACT_ACROSS_USERS_FULL permission for app. It's automaticaaly enabled

			//logger.system("UserAll = -1 SYSTEM_UID=1000 "+ Binder.getCallingUid());
			final Handler handler = new Handler(Looper.getMainLooper());
			//ReflectionUtil.in
			ReflectionUtil.invoke(manager, "copyAccountToUser",
					new Class[]{Account.class, UserHandle.class, UserHandle.class,
							Handler.class, AccountManagerCallback.class},
					account, users.get(1).getUserHandle(), users.get(0).getUserHandle(), handler, new AccountManagerCallback<Bundle>() {
						@Override
						public void run(AccountManagerFuture<Bundle> accountManagerFuture) {

						}
					});
		} else {
			throw new BypassTestException("Requires a multi user environment to run this test.");
		}
	}

	@RequiresApi(34)
	@PermissionTest(permission = "VIBRATE_VENDOR_EFFECTS", sdkMin = 36)
	public void testVibrateVendorEffects() {

		VibrationAttributes ATTRS = new VibrationAttributes.Builder()
				.setUsage(VibrationAttributes.USAGE_ALARM)
				.build();

		BinderTransaction.getInstance().invoke(Context.VIBRATOR_MANAGER_SERVICE,
				Transacts.VIBRATOR_MANAGER_DESCRIPTOR,
				"startVendorVibrationSession", appUid,
				mContext.getDeviceId(), mContext.getPackageName(), new int[]{1}, ATTRS, "testVibrate", null);

	}

	@RequiresApi(36)
	@PermissionTest(permission = "START_VIBRATION_SESSIONS", sdkMin = 36)
	public void testStartVibrationSessions() {
		VibrationAttributes ATTRS = new VibrationAttributes.Builder()
				.setUsage(VibrationAttributes.USAGE_ALARM)
				.build();

		BinderTransaction.getInstance().invoke(Context.VIBRATOR_MANAGER_SERVICE,
				Transacts.VIBRATOR_MANAGER_DESCRIPTOR,
				"startVendorVibrationSession", appUid,
				mContext.getDeviceId(), mContext.getPackageName(), new int[]{1}, ATTRS, "testVibrate", null);
	}

	@RequiresApi(36)
	@PermissionTest(permission = "MANAGE_ADVANCED_PROTECTION_MODE", sdkMin = 36)
	public void testManageAdvancedProtectionMode() {
		AdvancedProtectionManager manager = getService(AdvancedProtectionManager.class);
		ReflectionUtil.invoke(manager, "setAdvancedProtectionEnabled",
				new Class[]{boolean.class}, true);
	}

	@RequiresApi(36)
	@PermissionTest(permission = "READ_INTRUSION_DETECTION_STATE", sdkMin = 36)
	public void testReadIntrusionDetectionState() {

		if (checkPermissionGranted("android.permission.MANAGE_INTRUSION_DETECTION_STATE")) {
			throw new BypassTestException("MANAGE_INTRUSION_DETECTION_STATE test will crash system," +
					"when the target permission is allowed. So let us bypass it");
		}

		IIntrusionDetectionServiceStateCallback callback = new IIntrusionDetectionServiceStateCallback() {
			@Override
			public void onStateChange(byte state) throws RemoteException {

			}

			@Override
			public IBinder asBinder() {
				return null;
			}
		};
		BinderTransaction.getInstance().invoke(Transacts.INTRUSION_DETECTION_SERVICE,
				Transacts.INTRUSION_DETECTION_DESCRIPTOR,
				"addStateCallback", callback);
	}

	@PermissionTest(permission = "MANAGE_INTRUSION_DETECTION_STATE", sdkMin = 36)
	public void testManageIntrusionDetectionState() {

		if (checkPermissionGranted("android.permission.MANAGE_INTRUSION_DETECTION_STATE")) {
			throw new BypassTestException("MANAGE_INTRUSION_DETECTION_STATE test will crash system," +
					"when the target permission is allowed. So let us bypass it");
		}

		IIntrusionDetectionServiceCommandCallback callback = new IIntrusionDetectionServiceCommandCallback() {
			@Override
			public void onSuccess() throws RemoteException {

			}

			@Override
			public void onFailure(byte error) throws RemoteException {

			}

			@Override
			public IBinder asBinder() {
				return null;
			}
		};
		//MANAGE_INTRUSION_DETECTION_STATE
		BinderTransaction.getInstance().invoke(Transacts.INTRUSION_DETECTION_SERVICE,
				Transacts.INTRUSION_DETECTION_DESCRIPTOR,
				"disable", callback);
	}

//	@PermissionTest(permission="REQUEST_COMPANION_PROFILE_SENSOR_DEVICE_STREAMING",sdkMin=36)
//	public void testRequestCompanionProfileSensorDeviceStreaming(){}

	@RequiresApi(36)
	@PermissionTest(permission = "READ_SYSTEM_PREFERENCES", sdkMin = 36)
	public void testReadSystemPreferences() {
		//Prepare client and read from service.
		CountDownLatch bindingLatch = new CountDownLatch(1);
		CountDownLatch metadataLatch = new CountDownLatch(1);
		SettingsPreferenceServiceClient client =
				new SettingsPreferenceServiceClient(
						mContext, "com.android.settings", mExecutor,
						new OutcomeReceiver<SettingsPreferenceServiceClient, Exception>() {
							@Override
							public void onError(@NonNull Exception error) {
								OutcomeReceiver.super.onError(error);
								throw new RuntimeException("READ_SYSTEM_PREFERENCE:binding failed");
							}

							@Override
							public void onResult(SettingsPreferenceServiceClient settingsPreferenceServiceClient) {
								bindingLatch.countDown();
							}
						});
		try {
			if (!bindingLatch.await(5, TimeUnit.SECONDS)) {
				throw new RuntimeException("READ_SYSTEM_PREFERENCE:Binding Timeout");
			}
		} catch (InterruptedException e) {
			throw new RuntimeException("READ_SYSTEM_PREFERENCE:Binding Failed");
		}
		client.getAllPreferenceMetadata(
				new MetadataRequest.Builder().build(), mExecutor,
				new OutcomeReceiver<MetadataResult, Exception>() {
					@Override
					public void onError(@NonNull Exception error) {
						OutcomeReceiver.super.onError(error);
						throw new RuntimeException("READ_SYSTEM_PREFERENCE:binding failed");
					}

					@Override
					public void onResult(MetadataResult result) {
						if (result.getResultCode() != MetadataResult.RESULT_OK ||
								result.getMetadataList().isEmpty()) {
							throw new RuntimeException("READ_SYSTEM_PREFERENCE:No metadata");
						}
						//logger.debug(result.getMetadataList().toString());
						metadataLatch.countDown();
					}
				});
		try {
			if (!metadataLatch.await(10, TimeUnit.SECONDS)) {
				throw new RuntimeException("READ_SYSTEM_PREFERENCE:MetaData Timeout");
			}
		} catch (InterruptedException e) {
			throw new RuntimeException("READ_SYSTEM_PREFERENCE:MetaData Failed");
		}
	}

	@RequiresApi(36)
	@PermissionTest(permission = "WRITE_SYSTEM_PREFERENCES", sdkMin = 36)
	public void testWriteSystemPreferences() {
		//Prepare client and read from service.
		CountDownLatch bindingLatch = new CountDownLatch(1);
		CountDownLatch metadataLatch = new CountDownLatch(1);
		SettingsPreferenceServiceClient client =
				new SettingsPreferenceServiceClient(
						mContext, "com.android.settings", mExecutor,
						new OutcomeReceiver<SettingsPreferenceServiceClient, Exception>() {
							@Override
							public void onError(@NonNull Exception error) {
								OutcomeReceiver.super.onError(error);
								throw new RuntimeException("READ_SYSTEM_PREFERENCE:binding failed");
							}

							@Override
							public void onResult(SettingsPreferenceServiceClient settingsPreferenceServiceClient) {
								bindingLatch.countDown();
							}
						});
		try {
			if (!bindingLatch.await(5, TimeUnit.SECONDS)) {
				throw new RuntimeException("READ_SYSTEM_PREFERENCE:Binding Timeout");
			}
		} catch (InterruptedException e) {
			throw new RuntimeException("READ_SYSTEM_PREFERENCE:Binding Failed");
		}
		client.setPreferenceValue(
				new SetValueRequest.Builder("screen", "pref",
						new SettingsPreferenceValue.Builder(SettingsPreferenceValue.TYPE_BOOLEAN).setBooleanValue(true).build()
				).build(),
				mExecutor,
				new OutcomeReceiver<SetValueResult, Exception>() {
					@Override
					public void onError(@NonNull Exception error) {
						OutcomeReceiver.super.onError(error);
						throw new RuntimeException("READ_SYSTEM_PREFERENCE:binding failed");
					}

					@Override
					public void onResult(SetValueResult result) {
						metadataLatch.countDown();
					}
				});
		try {
			if (!metadataLatch.await(10, TimeUnit.SECONDS)) {
				throw new RuntimeException("READ_SYSTEM_PREFERENCE:MetaData Timeout");
			}
		} catch (InterruptedException e) {
			throw new RuntimeException("READ_SYSTEM_PREFERENCE:MetaData Failed");
		}
	}

	/*
	@PermissionTest(permission="EYE_CALIBRATION",sdkMin=36)
	public void testEyeCalibration(){
		logger.debug("The test for android.permission.EYE_CALIBRATION is not implemented yet");
	}
	@PermissionTest(permission="FACE_TRACKING_CALIBRATION",sdkMin=36)
	public void testFaceTrackingCalibration(){
		logger.debug("The test for android.permission.FACE_TRACKING_CALIBRATION is not implemented yet");
	}
	@PermissionTest(permission="IMPORT_XR_ANCHOR",sdkMin=36)
	public void testImportXrAnchor(){
		logger.debug("The test for android.permission.IMPORT_XR_ANCHOR is not implemented yet");
	}
	@PermissionTest(permission="ALWAYS_BOUND_TV_INPUT",sdkMin=36)
	public void testAlwaysBoundTvInput(){
		logger.debug("The test for android.permission.ALWAYS_BOUND_TV_INPUT is not implemented yet");
	}
	*/

//	@PermissionTest(permission="BYPASS_CONCURRENT_RECORD_AUDIO_RESTRICTION",sdkMin=36)
//	public void testBypassConcurrentRecordAudioRestriction(){
//	//		BinderTransaction.getInstance().invoke(Transacts.AUDIO_POLICY_SERVICE,
//	//		Transacts.AUDIO_POLICY_SERVICE_DESCRIPTOR,
//	//      "getInputForAttr", );
//
//		//logger.debug("The test for android.permission.BYPASS_CONCURRENT_RECORD_AUDIO_RESTRICTION is not implemented yet");
//	}

	@RequiresApi(35)
	@PermissionTest(permission = "ACCESS_FINE_POWER_MONITORS", sdkMin = 36,ignore = true)
	public void testAccessFinePowerMonitors() {
		// ** Ignore reason **
		// No fine power monitors found in the pixel.

		SystemHealthManager shm = systemService(SystemHealthManager.class);
		final List<PowerMonitor>[] mPowerMonitorInfo = new List[1];
		ConditionVariable done = new ConditionVariable();
		shm.getSupportedPowerMonitors(null, new Consumer<List<PowerMonitor>>() {
			@Override
			public void accept(List<PowerMonitor> powerMonitors) {
				mPowerMonitorInfo[0] = powerMonitors;
				done.open();
			}
		});
		done.block(5000);
		if (!mPowerMonitorInfo[0].isEmpty()) {
			PowerMonitor consumerMonitor = null;
			PowerMonitor measurementMonitor = null;
			//PowerMonitor fineMonitor = null;
			for (PowerMonitor pmi : mPowerMonitorInfo[0]) {
				if (pmi.getType() == PowerMonitor.POWER_MONITOR_TYPE_MEASUREMENT) {
					measurementMonitor = pmi;
				} else {
					consumerMonitor = pmi;
				}
			}
			List<PowerMonitor> selectedMonitors = new ArrayList<>();
			if (consumerMonitor != null) {
				selectedMonitors.add(consumerMonitor);
			}
			if (measurementMonitor != null) {
				selectedMonitors.add(measurementMonitor);
			}
			shm.getPowerMonitorReadings(selectedMonitors, null, new OutcomeReceiver<>() {
				@Override
				public void onError(@NonNull RuntimeException error) {
					OutcomeReceiver.super.onError(error);
					throw new RuntimeException("Error Reading:" + error.getMessage());
				}

				@Override
				public void onResult(PowerMonitorReadings powerMonitorReadings) {
					//logger.system(powerMonitorReadings.toString());
					done.open();
				}
			});
			done.block(5000);
		}
	}

//	@PermissionTest(permission="READ_SUBSCRIPTION_PLANS",sdkMin=36)
//	public void testReadSubscriptionPlans(){
//		//Found no implementations let us skip it.
//	}

//	@PermissionTest(permission="INSTALL_DEPENDENCY_SHARED_LIBRARIES",sdkMin=36)
//	public void testInstallDependencySharedLibraries(){
//		logger.debug("The test for android.permission.INSTALL_DEPENDENCY_SHARED_LIBRARIES is not implemented yet");
//	}

	@PermissionTest(permission = "MANAGE_KEY_GESTURES", sdkMin = 36)
	public void testManageKeyGestures() {
		//Need hidden prototype to test.
		IKeyGestureEventListener listener = new IKeyGestureEventListener() {

			@Override
			public IBinder asBinder() {
				return getActivityToken();
			}

			@Override
			public void onKeyGestureEvent(AidlKeyGestureEvent event) throws RemoteException {

			}
		};
		//The test would be executed appropriately only once in a session.
		BinderTransaction.getInstance().invoke(Transacts.INPUT_SERVICE, Transacts.INPUT_DESCRIPTOR,
				"registerKeyGestureEventListener",
				mExecutor, listener);

//		BinderTransaction.getInstance().invoke(Transacts.INPUT_SERVICE, Transacts.INPUT_DESCRIPTOR,
//				"unregisterKeyGestureEventListener",
//				mExecutor,listener);

	}

	@PermissionTest(permission = "LISTEN_FOR_KEY_ACTIVITY", sdkMin = 36, ignore = true)
	public void testListenForKeyActivity() {
		//*** REASON FOR IGNORE ***
		//*** This Permission is not managed by the system package manager ***

		IKeyEventActivityListener listener = new IKeyEventActivityListener() {
			@Override
			public IBinder asBinder() {
				return getActivityToken();
			}

			@Override
			public void onKeyEventActivity() throws RemoteException {

			}
		};
		BinderTransaction.getInstance().invoke(Transacts.INPUT_SERVICE, Transacts.INPUT_DESCRIPTOR,
				"registerKeyEventActivityListener",
				mExecutor, listener);
	}

	@RequiresApi(31)
	@PermissionTest(permission = "BACKUP_HEALTH_CONNECT_DATA_AND_SETTINGS", sdkMin = 36, ignore = true)
	public void testBackupHealthConnectDataAndSettings() {
		//*** REASON FOR IGNORE ***
		//*** HealthConnect Service is not responding.

		ConditionVariable done = new ConditionVariable();
		IGetChangesForBackupResponseCallback callback = new IGetChangesForBackupResponseCallback() {
			@Override
			public void onResult(GetChangesForBackupResponse parcel) throws RemoteException {
				done.open();
			}

			@Override
			public void onError(HealthConnectExceptionParcel exception) throws RemoteException {
				done.open();
			}

			@Override
			public IBinder asBinder() {
				return null;
			}
		};

		BinderTransaction.getInstance().invoke(Context.HEALTHCONNECT_SERVICE,
				Transacts.HEALTH_CONNECT_DESCRIPTOR,
				"getChangesForBackup", "", callback);

		if (!done.block(300)) {
			logger.debug("getChangesForBackup - timeout");
		}

		//getChangesForBackup
	}

	//	@PermissionTest(permission="RESTORE_HEALTH_CONNECT_DATA_AND_SETTINGS",sdkMin=36)
//	public void testRestoreHealthConnectDataAndSettings(){
//	}

	//	@PermissionTest(permission="CAPTURE_CONSENTLESS_BUGREPORT_DELEGATED_CONSENT",sdkMin=36)
//	public void testCaptureConsentlessBugreportDelegatedConsent(){
//	}
	@PermissionTest(permission = "MANAGE_SECURE_LOCK_DEVICE", sdkMin = 36, ignore = true)
	public void testManageSecureLockDevice() {
		//*** REASON FOR IGNORE ***
		//*** This Permission is not managed by the system package manager ***

		BinderTransaction.getInstance().invoke(Transacts.AUTHENTICATION_POLICY_SERVICE,
				Transacts.AUTHENTICATION_POLICY_SERVICE_DESCRIPTOR,
				"enableSecureLockDevice", new EnableSecureLockDeviceParams("foo"));
	}

	@PermissionTest(permission = "ENTER_TRADE_IN_MODE", sdkMin = 36)
	public void testEnterTradeInMode() {
		BinderTransaction.getInstance().invoke(Transacts.TRADE_IN_MODE_SERVICE,
				Transacts.TRADE_IN_MODE_DESCRIPTOR,
				"start");
	}

	//	@PermissionTest(permission="DYNAMIC_INSTRUMENTATION",sdkMin=36)
//	public void testDynamicInstrumentation(){
//	}
	@RequiresApi(33)
	@PermissionTest(permission = "RESOLVE_COMPONENT_FOR_UID", sdkMin = 36)
	public void testResolveComponentForUid() {
		if (!checkPermissionGranted("android.permission.RESOLVE_COMPONENT_FOR_UID")) {
			throw new BypassTestException("RESOLVE_COMPONENT_FOR_UID was not detected by package manager." +
					"So let us bypass it");
		}
		//This permission is not recognized by the package manager. so let me check if it's enabled...
		//mPackageManager.checkPermission("android.manifest.permission.RESOLVE_COMPONENT_FOR_UID")
		ReflectionUtil.invoke(mPackageManager,
				"resolveContentProviderForUid", new Class[]{String.class,
						PackageManager.ComponentInfoFlags.class, int.class},
				"android.packageinstaller.multiusercontentprovider",
				PackageManager.ComponentInfoFlags.of(0), -1);
	}

	@PermissionTest(permission = "RESERVED_FOR_TESTING_SIGNATURE", sdkMin = 36)
	public void testReservedForTestingSignature() {
		int r = mPackageManager.checkPermission(
				"android.permission.RESERVED_FOR_TESTING_SIGNATURE",
				mContext.getPackageName());
		//Check if the Package Manager detect this permission correctly in normal app//

		if (isPlatformSignatureMatch) {
			if (r == PackageManager.PERMISSION_GRANTED) {
				logger.debug("Testing RESERVED_FOR_TESTING_SIGNATURE:true");
			} else {
				throw new SecurityException("RESERVED_FOR_TESTING_SIGNATURE was not enabled in this system.");
			}
		} else {
			if (r != PackageManager.PERMISSION_GRANTED) {
				throw new SecurityException("Intended Behaviour. In this case, This permission should not be granted!");
			}
		}
	}

	//	@PermissionTest(permission="SINGLE_USER_TIS_ACCESS",sdkMin=36)
	//	public void testSingleUserTisAccess(){
	//	}

	@PermissionTest(permission = "ACCESS_TEXT_CLASSIFIER_BY_TYPE", sdkMin = 36)
	public void testAccessTextClassifierByType() {
		//only get Classifier is blocking by this permission.
		TextClassificationManager tcm = systemService(TextClassificationManager.class);
		List<String> methods = ReflectionTool.Companion.checkDeclaredMethod(tcm, "get");

		ReflectionUtil.invoke(tcm, "getClassifier", new Class[]{int.class}, 0);
	}


	@PermissionTest(permission = "SATELLITE_COMMUNICATION", sdkMin = 36)
	public void testSatelliteCommunication() {

		BinderTransaction.getInstance().invoke(
				Context.TELEPHONY_SERVICE,
				Transacts.TELEPHONY_DESCRIPTOR,
				"requestIsSatelliteEnabled",null,null);

		//cb1,cb2);
	}

	@PermissionTest(permission="USE_ATTESTATION_VERIFICATION_SERVICE", sdkMin=36)
	public void testUseAttestationVerificationService(){
		// in VerificationToken token,in ParcelDuration maximumTokenAge,in AndroidFuture resultCallback
		// Intended NPE will be raised
		//systemService(Attestation)
		try {
			BinderTransaction.getInstance().invoke(Transacts.ATTESTATION_VERIFICATION_SERVICE,
					Transacts.ATTESTATION_VERIFICATION_DESCRIPTOR,
					"verifyToken", null, null, null);
		} catch(UnsupportedOperationException ex){
			//Intended Ignore
		} catch(NullPointerException ex){
			//Intended ignore
		}

	}
}









