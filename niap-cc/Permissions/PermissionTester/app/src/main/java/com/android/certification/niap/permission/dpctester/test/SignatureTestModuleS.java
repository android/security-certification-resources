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


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.UserHandle;
import android.telecom.TelecomManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.security.KeyChain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.android.certification.niap.permission.dpctester.MainActivity;
import com.android.certification.niap.permission.dpctester.R;
import com.android.certification.niap.permission.dpctester.common.Constants;
import com.android.certification.niap.permission.dpctester.test.exception.BypassTestException;
import com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException;
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestRunner;
import com.android.certification.niap.permission.dpctester.test.runner.SignaturePermissionTestModuleBase;
import com.android.certification.niap.permission.dpctester.test.tool.BinderTransaction;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTest;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestModule;

import java.io.FileDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@PermissionTestModule(name="Signature 31(S),32(V2) Test Cases",prflabel = "Snow Corn(12)")
public class SignatureTestModuleS extends SignaturePermissionTestModuleBase {
	public SignatureTestModuleS(@NonNull Activity activity) {
		super(activity);
	}


	private <T> T systemService(Class<T> clazz){
		return Objects.requireNonNull(getService(clazz),"[npe_system_service]"+clazz.getSimpleName());
	}

	@PermissionTest(permission="ASSOCIATE_INPUT_DEVICE_TO_DISPLAY", sdkMin=31)
	public void testAssociateInputDeviceToDisplay(){
		BinderTransaction.getInstance().invoke(Transacts.INPUT_SERVICE, Transacts.INPUT_DESCRIPTOR,
				"removePortAssociation", "testPort");
	}

	@SuppressLint("PrivateApi")
	@PermissionTest(permission="CAMERA_INJECT_EXTERNAL_CAMERA", sdkMin=31,sdkMax = 32)
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
					"test-internal-cam",
					"test-external-cam", injectionCallback, injectionSession);

			/* TODO: Change the parameter depends on the version 31-32,33,34-
			if(mDeviceApiLevel >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE){
				BinderTransaction.getInstance().invoke(Context.CAMERA_SERVICE,
						Transacts.CAMERA_DESCRIPTOR,
						"injectCamera", mContext.getPackageName(),
						"",
						"", injectionCallback);
			} else if(mDeviceApiLevel == Build.VERSION_CODES.TIRAMISU){
				BinderTransaction.getInstance().invoke(Transacts.CAMERA_SERVICE,
						Transacts.CAMERA_DESCRIPTOR,
						"injectCamera", mContext.getPackageName(),
						"test-internal-cam",
						"test-external-cam", injectionCallback);

			} else {

			}*/

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

	@PermissionTest(permission="CLEAR_FREEZE_PERIOD", sdkMin=31)
	public void testClearFreezePeriod(){
		BinderTransaction.getInstance().invoke(Transacts.DEVICE_POLICY_SERVICE,
				Transacts.DEVICE_POLICY_DESCRIPTOR,
				"clearSystemUpdatePolicyFreezePeriodRecord");
	}

	@PermissionTest(permission="CONTROL_DEVICE_STATE", sdkMin=31, sdkMax=31)
	public void testControlDeviceState(){
		@SuppressLint("WrongConstant") Object deviceStateManager
				= mContext.getSystemService("device_state");
		Class<?> deviceStateRequestClass = null;
		try {
			deviceStateRequestClass = Class.forName(
					"android.hardware.devicestate.DeviceStateRequest");
		} catch (ClassNotFoundException e) {
			throw new UnexpectedTestFailureException(e);
		}
		BinderTransaction.getInstance().invoke(Transacts.DEVICE_STATE_SERVICE,
				Transacts.DEVICE_STATE_DESCRIPTOR, "cancelRequest",
				deviceStateRequestClass.cast(null));

	}

	@PermissionTest(permission="FORCE_DEVICE_POLICY_MANAGER_LOGS", sdkMin=31)
	public void testForceDevicePolicyManagerLogs(){
		try {
			BinderTransaction.getInstance().invoke(Transacts.DEVICE_POLICY_SERVICE,
					Transacts.DEVICE_POLICY_DESCRIPTOR, "forceSecurityLogs");
		} catch(IllegalStateException ignored){
			//IllegalStateExceptiont Exception will be caused by it, so it is an expected error
		}
	}

	@PermissionTest(permission="INPUT_CONSUMER", sdkMin=31)
	public void testInputConsumer(){
		BinderTransaction.getInstance().invoke(Transacts.WINDOW_SERVICE, Transacts.WINDOW_DESCRIPTOR,
				"createInputConsumer",
				getActivityToken(), "test", 1, null);

    }

	@PermissionTest(permission="KEEP_UNINSTALLED_PACKAGES", sdkMin=31)
	public void testKeepUninstalledPackages(){
		BinderTransaction.getInstance().invoke(Transacts.PACKAGE_SERVICE,
				Transacts.PACKAGE_DESCRIPTOR, "setKeepUninstalledPackages",
				List.of("com.example.app"));
	}

	@PermissionTest(permission="MANAGE_CREDENTIAL_MANAGEMENT_APP", sdkMin=31,sdkMax=33)
	public void testManageCredentialManagementApp(){
		Intent intent = new Intent();
		intent.setAction("android.security.IKeyChainService");
		intent.setComponent(new ComponentName(Constants.KEY_CHAIN_PACKAGE,
				Constants.KEY_CHAIN_PACKAGE + ".KeyChainService"));
		BinderTransaction.getInstance().invokeViaServiceFromIntent(mContext, intent,
				Transacts.KEY_CHAIN_DESCRIPTOR,
				"removeCredentialManagementApp");
	}

	@PermissionTest(permission="MANAGE_GAME_MODE", sdkMin=31)
	public void testManageGameMode(){
		BinderTransaction.getInstance().invoke(Transacts.GAME_SERVICE, Transacts.GAME_DESCRIPTOR,
				"getAvailableGameModes", mContext.getPackageName());
	}

	@PermissionTest(permission="MANAGE_SMARTSPACE", sdkMin=31)
	public void testManageSmartspace(){
		// Note this is fragile since the implementation of SmartspaceSessionId can
		// change in the future, but since there is no way to construct an instance
		// of SmartspaceSessionId this at least allows the test to proceed.
		if(checkPermissionGranted("android.permission.ACCESS_SMARTSPACE") && !isPlatformSignatureMatch){
			//when access smart space permission is granted this test would be passed with ordinal signature
			throw new BypassTestException("Cannot test this case when ACCESS_SMARTSPACE is granted.");
		}
		Parcelable smartspaceId = new Parcelable() {
			@Override
			public int describeContents() {
				return 0;
			}

			@Override
			public void writeToParcel(Parcel parcel, int i) {
				parcel.writeString("test-smartspace-id");
				parcel.writeTypedObject(UserHandle.getUserHandleForUid(appUid), 0);
			}
		};
		BinderTransaction.getInstance().invoke(Transacts.SMART_SPACE_SERVICE,
				Transacts.SMART_SPACE_DESCRIPTOR, "destroySmartspaceSession", smartspaceId);
	}

	@PermissionTest(permission="MANAGE_SPEECH_RECOGNITION", sdkMin=31)
	public void testManageSpeechRecognition(){
		ComponentName componentName = new ComponentName(mContext, MainActivity.class);
		BinderTransaction.getInstance().invoke(Transacts.SPEECH_RECOGNITION_SERVICE,
				Transacts.SPEECH_RECOGNITION_DESCRIPTOR,
				"setTemporaryComponent", componentName);
	}

	@PermissionTest(permission="MANAGE_TOAST_RATE_LIMITING", sdkMin=31)
	public void testManageToastRateLimiting(){
		BinderTransaction.getInstance().invoke(Transacts.NOTIFICATION_SERVICE,
				Transacts.NOTIFICATION_DESCRIPTOR,
				"setToastRateLimitingEnabled", false);
	}

	@PermissionTest(permission="MANAGE_WIFI_COUNTRY_CODE", sdkMin=31)
	public void testManageWifiCountryCode(){
		BinderTransaction.getInstance().invoke(Transacts.WIFI_SERVICE, Transacts.WIFI_DESCRIPTOR,
				"setOverrideCountryCode", "ja");
	}

	@PermissionTest(permission="MODIFY_REFRESH_RATE_SWITCHING_TYPE", sdkMin=31)
	public void testModifyRefreshRateSwitchingType(){
		BinderTransaction.getInstance().invoke(Transacts.DISPLAY_SERVICE,
				Transacts.DISPLAY_DESCRIPTOR, "setRefreshRateSwitchingType", 0);
	}

	@PermissionTest(permission="OVERRIDE_DISPLAY_MODE_REQUESTS", sdkMin=31)
	public void testOverrideDisplayModeRequests(){
		BinderTransaction.getInstance().invoke(Transacts.DISPLAY_SERVICE,
				Transacts.DISPLAY_DESCRIPTOR,
				"shouldAlwaysRespectAppRequestedMode");
	}

	@PermissionTest(permission="QUERY_AUDIO_STATE", sdkMin=31)
	public void testQueryAudioState(){
		Parcelable audioDeviceAttributes = new Parcelable() {
			@Override
			public int describeContents() {
				return 0;
			}

			@Override
			public void writeToParcel(Parcel parcel, int i) {
				parcel.writeInt(2); // Role - AudioPort#ROLE_SINK
				parcel.writeInt(2); // Type - AudioDeviceInfo#TYPE_BUILTIN_SPEAKER
				parcel.writeString("test-address"); // address
				parcel.writeInt(0); // native-type
			}
		};
		BinderTransaction.getInstance().invoke(Transacts.AUDIO_SERVICE, Transacts.AUDIO_DESCRIPTOR,
				"getDeviceVolumeBehavior", audioDeviceAttributes);
	}

	@PermissionTest(permission="READ_DREAM_SUPPRESSION", sdkMin=31)
	public void testReadDreamSuppression(){
		// Note, this transact requires both this permission and READ_DREAM_STATE,
		// but this is the only transact that checks for READ_DREAM_SUPPRESSION, so it
		// is included since the granted path can verify that the transact behaves
		// as expected when both permissions are granted.
		BinderTransaction.getInstance().invoke(Transacts.POWER_SERVICE, Transacts.POWER_DESCRIPTOR,
				"isAmbientDisplaySuppressedForTokenByApp", "test-token", appUid);
	}

	@PermissionTest(permission="READ_PROJECTION_STATE", sdkMin=31)
	public void testReadProjectionState(){
		BinderTransaction.getInstance().invoke(Transacts.UI_MODE_SERVICE,
				Transacts.UI_MODE_DESCRIPTOR, "getActiveProjectionTypes");
	}

	@PermissionTest(permission="RESET_APP_ERRORS", sdkMin=31)
	public void testResetAppErrors(){
		BinderTransaction.getInstance().invoke(Transacts.ACTIVITY_SERVICE,
				Transacts.ACTIVITY_DESCRIPTOR, "resetAppErrors");
	}

	@PermissionTest(permission="SET_AND_VERIFY_LOCKSCREEN_CREDENTIALS", sdkMin=31)
	public void testSetAndVerifyLockscreenCredentials(){
		if (!mPackageManager.hasSystemFeature(
				PackageManager.FEATURE_SECURE_LOCK_SCREEN)) {
			throw new BypassTestException("This permission requires feature "
					+ PackageManager.FEATURE_SECURE_LOCK_SCREEN);
		}
		Parcelable lockscreenCredential = new Parcelable() {
			@Override
			public int describeContents() {
				return 0;
			}

			@Override
			public void writeToParcel(Parcel parcel, int i) {
				// @see LockscreenCredential#createNone
				parcel.writeInt(-1); // Type - LockPatternUtils#CREDENTIAL_TYPE_NONE
				parcel.writeByteArray(new byte[0]); // Credential - empty byte array to
				// check for no credentials set.
			}
		};
		try {
			BinderTransaction.getInstance().invoke(Transacts.LOCK_SETTINGS_SERVICE,
					Transacts.LOCK_SETTINGS_DESCRIPTOR, "verifyCredential",
					lockscreenCredential, 0, 0);
		} catch(IllegalArgumentException ignored){
			//Credential Check will be executed after permission check.
			//IllegalArgument Exception will be caused by it, so it is expected error
		}
	}

	@PermissionTest(permission="TEST_BIOMETRIC", sdkMin=31)
	public void testTestBiometric(){
		BinderTransaction.getInstance().invoke(Transacts.AUTH_SERVICE, Transacts.AUTH_DESCRIPTOR,
				"getUiPackage");
	}

	@PermissionTest(permission="UPDATE_DOMAIN_VERIFICATION_USER_SELECTION", sdkMin=31)
	public void testUpdateDomainVerificationUserSelection(){
		BinderTransaction.getInstance().invoke(Transacts.DOMAIN_VERIFICATION_SERVICE,
				Transacts.DOMAIN_VERIFICATION_DESCRIPTOR,
				"setDomainVerificationLinkHandlingAllowed", mContext.getPackageName(), false,
				0);
	}

	@RequiresApi(api = Build.VERSION_CODES.S)
    @PermissionTest(permission="MANAGE_ONGOING_CALLS", sdkMin=31)
	public void testManageOngoingCalls(){
		// Note, MANAGE_ONGOING_CALLS is only used to determine the UI type when
		// the system binds to an in-call app, but this API at least performs a
		// permission grant check.
		boolean permissionGranted =
				systemService(TelecomManager.class).hasManageOngoingCallsPermission();
		if (!permissionGranted) {
			throw new SecurityException(
					mContext.getPackageName() + " has not been granted MANAGE_ONGOING_CALLS");
		}

	}

	@PermissionTest(permission="USE_ICC_AUTH_WITH_DEVICE_IDENTIFIER", sdkMin=31)
	public void testUseIccAuthWithDeviceIdentifier(){
		SubscriptionManager subscriptionManager = mContext.getSystemService(
				SubscriptionManager.class);
		List<SubscriptionInfo> subInfos;
		try {
			subInfos = subscriptionManager.getActiveSubscriptionInfoList();
		} catch (SecurityException e) {
			// A SecurityException indicates the app does not have permission to query
			// the active SubscriptionInfo instances; without these the test cannot
			// be run.
			subInfos = null;
		}
		if (subInfos == null || subInfos.size() == 0) {
			throw new BypassTestException(
					"This permission requires that the app be able to query at least "
							+ "one active subscription");
		}
		// The data parameter is supposed to be a base64 encoded value; the encoded
		// value used for this invocation is "test".
		systemService(TelephonyManager.class).getIccAuthentication(TelephonyManager.APPTYPE_SIM,
				TelephonyManager.AUTHTYPE_EAP_SIM, "dGVzdAo=");
	}

	@PermissionTest(permission="MANAGE_NOTIFICATION_LISTENERS", sdkMin=31)
	public void testManageNotificationListeners(){
		BinderTransaction.getInstance().invoke(Transacts.NOTIFICATION_SERVICE,
				Transacts.NOTIFICATION_DESCRIPTOR,
				"getEnabledNotificationListeners", 0);
	}

	@PermissionTest(permission="BATTERY_PREDICTION", sdkMin=31)
	public void testBatteryPrediction(){
		// Note, this is the only transact that checks for this permission, but it also
		// checks for the DEVICE_POWER permission. First the permission under test is
		// checked, then if that fails an enforcePermission call is invoked for
		// DEVICE_POWER; this is why the SecurityException for this transact only
		// reports the DEVICE_POWER permission even though both are checked.
		Parcelable parcelDuration = new Parcelable() {
			@Override
			public int describeContents() {
				return 0;
			}

			@Override
			public void writeToParcel(Parcel parcel, int i) {
				Duration duration = Duration.ofDays(1);
				parcel.writeLong(duration.getSeconds());
				parcel.writeInt(duration.getNano());
			}
		};
		try {
			BinderTransaction.getInstance().invoke(Transacts.POWER_SERVICE, Transacts.POWER_DESCRIPTOR,
					"setBatteryDischargePrediction", parcelDuration, false);
		} catch(IllegalStateException ignored){
			//Can not close correctly while charging
		}
	}

	@PermissionTest(permission="MANAGE_TIME_AND_ZONE_DETECTION", sdkMin=31)
	public void testManageTimeAndZoneDetection(){
		BinderTransaction.getInstance().invoke(Transacts.TIME_DETECTOR_SERVICE,
				Transacts.TIME_DETECTOR_DESCRIPTOR,
				"getCapabilitiesAndConfig", 0);
	}

	@PermissionTest(permission="NFC_SET_CONTROLLER_ALWAYS_ON", sdkMin=31)
	public void testNfcSetControllerAlwaysOn(){
		if (!mPackageManager.hasSystemFeature(PackageManager.FEATURE_NFC)) {
			throw new BypassTestException("This permission requires feature "
					+ PackageManager.FEATURE_NFC);
		}
		BinderTransaction.getInstance().invoke(Transacts.NFC_SERVICE, Transacts.NFC_DESCRIPTOR,
				"isControllerAlwaysOnSupported");
	}

	@PermissionTest(permission="OVERRIDE_COMPAT_CHANGE_CONFIG_ON_RELEASE_BUILD", sdkMin=31)
	public void testOverrideCompatChangeConfigOnReleaseBuild(){
		Parcelable compatOverridesToRemoveConfig = new Parcelable() {
			@Override
			public int describeContents() {
				return 0;
			}

			@Override
			public void writeToParcel(Parcel parcel, int i) {
				parcel.writeInt(0); // Number of changeIds to be removed.
			}
		};
		BinderTransaction.getInstance().invoke(Transacts.PLATFORM_COMPAT_SERVICE,
				Transacts.PLATFORM_COMPAT_DESCRIPTOR,
				"removeOverridesOnReleaseBuilds", compatOverridesToRemoveConfig,
				mContext.getPackageName());
	}

	@PermissionTest(permission="READ_NEARBY_STREAMING_POLICY", sdkMin=31, sdkMax=32)
	public void testReadNearbyStreamingPolicy(){
		// DevicePolicyManagerService first checks if this feature is available before
		// performing the permission check.
		if (!mPackageManager.hasSystemFeature(PackageManager.FEATURE_DEVICE_ADMIN)) {
			throw new BypassTestException("This permission requires feature "
					+ PackageManager.FEATURE_DEVICE_ADMIN);
		}
		BinderTransaction.getInstance().invoke(Transacts.DEVICE_POLICY_SERVICE,
				Transacts.DEVICE_POLICY_DESCRIPTOR,
				"getNearbyNotificationStreamingPolicy", 0);

	}

	@PermissionTest(permission="REGISTER_MEDIA_RESOURCE_OBSERVER", sdkMin=31)
	public void testRegisterMediaResourceObserver(){
		IBinder binder = new IBinder() {
			@Nullable
			@Override
			public String getInterfaceDescriptor() throws RemoteException {
				return "android.media.IResourceObserver";
			}

			@Override
			public boolean pingBinder() {
				return false;
			}

			@Override
			public boolean isBinderAlive() {
				return false;
			}

			@Nullable
			@Override
			public IInterface queryLocalInterface(@NonNull String s) {
				return null;
			}

			@Override
			public void dump(@NonNull FileDescriptor fileDescriptor,
							 @Nullable String[] strings) throws RemoteException {

			}

			@Override
			public void dumpAsync(@NonNull FileDescriptor fileDescriptor,
								  @Nullable String[] strings) throws RemoteException {

			}

			@Override
			public boolean transact(int i, @NonNull Parcel parcel,
									@Nullable Parcel parcel1, int i1) throws RemoteException {
				return false;
			}

			@Override
			public void linkToDeath(@NonNull DeathRecipient deathRecipient, int i)
					throws RemoteException {

			}

			@Override
			public boolean unlinkToDeath(@NonNull DeathRecipient deathRecipient,
										 int i) {
				return false;
			}
		};
		try {
			BinderTransaction.getInstance().invoke(Transacts.RESOURCE_OBSERVER_SERVICE,
					Transacts.RESOURCE_OBSERVER_DESCRIPTOR,
					"unregisterObserver", binder);
		} catch (Exception e) {
			// The following was logged and caught when the permission was not granted;
			// the code -1 signifies PERMISSION_DENIED.
			// ServiceManager: Permission failure: android.permission
			//   .REGISTER_MEDIA_RESOURCE_OBSERVER from uid=10430 pid=31259
			// ResourceObserverService: Permission Denial: can't unregisterObserver
			//   from pid=31259, uid=10430
			// SignaturePermissionTester: android.os.ServiceSpecificException: (code -1)

			// Note, the caught exception is a ServiceSpecificException which is hidden
			// from external apps; these Exceptions have an errorCode that is not part
			// of the message but is shown in #toString.
			if (e.toString().contains("code -1")) {
				throw new SecurityException("Transact failed with PERMISSION_DENIED",
						e);
			} else {
				// any other exceptions should be logged for debugging in case other
				// devices use a different error code for PERMISSION_DENIED.

				logger.debug("Intended Exception : Caught an exception invoking the transact: ", e);
			}
		}
	}

	@PermissionTest(permission="RESTART_WIFI_SUBSYSTEM", sdkMin=31)
	public void testRestartWifiSubsystem(){
		// This test may require some amount of sleep time after a successful run in
		// case other tests rely on the wifi subsystem and it has not yet fully
		// restarted.
		BinderTransaction.getInstance().invoke(Transacts.WIFI_SERVICE, Transacts.WIFI_DESCRIPTOR,
				"restartWifiSubsystem");

	}

	@PermissionTest(permission="SCHEDULE_PRIORITIZED_ALARM", sdkMin=31)
	public void testSchedulePrioritizedAlarm(){
		final int FLAG_PRIORITIZE = 1 << 6; // As defined in AlarmManager
		// Set the start time to be a day from now
		long windowStartMs = System.currentTimeMillis() + 60 * 60 * 24 * 1000;
		// null values are used to prevent an alarm from actually triggering.
		BinderTransaction.getInstance().invoke(Transacts.ALARM_SERVICE, Transacts.ALARM_DESCRIPTOR,
				"set", mContext.getPackageName(), 0, windowStartMs, 0, 0, FLAG_PRIORITIZE,
				null, null, null, null, null);
	}

	@PermissionTest(permission="SEND_CATEGORY_CAR_NOTIFICATIONS", sdkMin=31)
	public void testSendCategoryCarNotifications(){
		if (!mPackageManager.hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE)) {
			throw new BypassTestException("This permission requires the feature "
					+ PackageManager.FEATURE_AUTOMOTIVE);
		}
		Resources resources = mContext.getResources();
		CharSequence channelName = resources.getString(R.string.tester_channel_name);
		NotificationChannel channel = new NotificationChannel(getTAG(), channelName,
				NotificationManager.IMPORTANCE_DEFAULT);
		NotificationManager notificationManager = mContext.getSystemService(
				NotificationManager.class);
		notificationManager.createNotificationChannel(channel);

		Intent notificationIntent = new Intent(mContext, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
				notificationIntent,
				PendingIntent.FLAG_IMMUTABLE);
		Notification notification =
				new Notification.Builder(mContext, getTAG())
						.setContentTitle(
								resources.getText(R.string.status_notification_title))
						.setContentText(
								resources.getString(R.string.test_notification_message))
						.setSmallIcon(R.drawable.ic_launcher_foreground)
						.setContentIntent(pendingIntent)
						.setAutoCancel(true)
						.setCategory("car_emergency")
						.build();
		notificationManager.notify(0, notification);
	}

	@PermissionTest(permission="SIGNAL_REBOOT_READINESS", sdkMin=31)
	public void testSignalRebootReadiness(){
		BinderTransaction.getInstance().invoke(Transacts.REBOOT_READINESS_SERVICE,
				Transacts.REBOOT_READINESS_DESCRIPTOR,
				"removeRequestRebootReadinessStatusListener", getActivityToken());
	}

	@PermissionTest(permission="SOUNDTRIGGER_DELEGATE_IDENTITY", sdkMin=31,sdkMax = 34)
	public void testSoundtriggerDelegateIdentity(){
		Parcelable identity = new Parcelable() {
			@Override
			public int describeContents() {
				return 0;
			}

			@Override
			public void writeToParcel(Parcel parcel, int i) {
				int start_pos = parcel.dataPosition();
				parcel.writeInt(0);
				parcel.writeInt(appUid); // uid
				parcel.writeInt(Binder.getCallingPid()); // pid
				parcel.writeString(mContext.getPackageName()); // packageName
				parcel.writeString("test-attribution"); // attributionTag
				int end_pos = parcel.dataPosition();
				parcel.setDataPosition(start_pos);
				parcel.writeInt(end_pos - start_pos);
				parcel.setDataPosition(end_pos);
			}
		};

		BinderTransaction.getInstance().invoke(Transacts.SOUND_TRIGGER_SERVICE,
				Transacts.SOUND_TRIGGER_DESCRIPTOR, "attachAsMiddleman",
				identity, identity,new Binder());
	}

	@PermissionTest(permission="SUGGEST_EXTERNAL_TIME", sdkMin=31)
	public void testSuggestExternalTime()  {
		//logger.debug("external_time_suggestion1");
        try {
            Class<?> clazz = Class.forName("android.app.time.ExternalTimeSuggestion");
			Constructor<?> constructor = clazz.getConstructor(new Class<?>[]{long.class,long.class});
			Object ets = constructor.newInstance(0L,0L);
			//android.app.time.ExternalTimeSuggestion
			BinderTransaction.getInstance().invoke(Transacts.TIME_DETECTOR_SERVICE,
					Transacts.TIME_DETECTOR_DESCRIPTOR, "suggestExternalTime",
					(Object)ets);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException | InstantiationException e) {
            throw new UnexpectedTestFailureException(e);
        }

    }

	@PermissionTest(permission="TOGGLE_AUTOMOTIVE_PROJECTION", sdkMin=31, sdkMax=32)
	public void testToggleAutomotiveProjection(){
		BinderTransaction.getInstance().invoke(Transacts.UI_MODE_SERVICE,
				Transacts.UI_MODE_DESCRIPTOR, "requestProjection",
				IBinderMock, 1, mContext.getPackageName());
	}

	@PermissionTest(permission="UPDATE_FONTS", sdkMin=31)
	public void testUpdateFonts(){
		BinderTransaction.getInstance().invoke(Transacts.FONT_SERVICE, Transacts.FONT_DESCRIPTOR,
				"getFontConfig");
	}

	@PermissionTest(permission="UWB_PRIVILEGED", sdkMin=31)
	public void testUwbPrivileged(){
		BinderTransaction.getInstance().invoke(Transacts.UWB_SERVICE, Transacts.UWB_DESCRIPTOR,
				"getSpecificationInfo");
	}

	@PermissionTest(permission="BROADCAST_CLOSE_SYSTEM_DIALOGS", sdkMin=31)
	public void testBroadcastCloseSystemDialogs(){
		Intent intent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
		mContext.sendBroadcast(intent);
	}

	@PermissionTest(permission="MANAGE_MUSIC_RECOGNITION", sdkMin=31)
	public void testManageMusicRecognition(){
		//RecognitionRequest,IBinder
		BinderTransaction.getInstance().invoke(Transacts.MUSIC_RECOGNITION_SERVICE,
				Transacts.MUSIC_RECOGNITION_DESCRIPTOR, "beginRecognition",
				new Parcelable() {
					@Override
					public int describeContents() {
						return 0;
					}
					@Override
					public void writeToParcel(@NonNull Parcel dest, int flags) {
					}
				}, new Binder());
	}

	@PermissionTest(permission="MANAGE_UI_TRANSLATION", sdkMin=31)
	public void testManageUiTranslation(){
		BinderTransaction.getInstance().invoke(Transacts.TRANSLATION_SERVICE,
				Transacts.TRANSLATION_DESCRIPTOR, "updateUiTranslationState", 0,
				null, null, null, new Binder(), 0, null, 0);
	}

	@PermissionTest(permission="ACCESS_TUNED_INFO", sdkMin=31)
	public void testAccessTunedInfo(){
		BinderTransaction.getInstance().invoke(Transacts.TV_INPUT_SERVICE,
				Transacts.TV_INPUT_DESCRIPTOR, "getCurrentTunedInfos", 0);
	}

	@PermissionTest(permission="GET_PEOPLE_TILE_PREVIEW", sdkMin=31)
	public void testGetPeopleTilePreview(){
		//Invalid shortcut id may called.
		//Handle java.lang.IllegalArgumentException
		try {
			mContentResolver.call(
					Uri.parse("content://com.android.systemui.people.PeopleProvider"),
					"get_people_tile_preview", null, null);
		} catch (IllegalArgumentException ex){
			//expected
		}
	}

	@PermissionTest(permission="MANAGE_ACTIVITY_TASKS", sdkMin=31,sdkMax = 32)
	public void testManageActivityTasks(){
		BinderTransaction.getInstance().invoke(Transacts.ACTIVITY_TASK_SERVICE,
				Transacts.ACTIVITY_TASK_DESCRIPTOR,
				"getWindowOrganizerController");
	}

	@PermissionTest(permission="SET_CLIP_SOURCE", sdkMin=31)
	public void testSetClipSource(){
		BinderTransaction.getInstance().invoke(Transacts.CLIPBOARD_SERVICE,
				Transacts.CLIPBOARD_DESCRIPTOR, "getPrimaryClipSource",
				mContext.getPackageName(), 0);
	}

	@PermissionTest(permission="READ_PEOPLE_DATA", sdkMin=31)
	public void testReadPeopleData(){
		BinderTransaction.getInstance().invoke(Transacts.PEOPLE_SERVICE, Transacts.PEOPLE_DESCRIPTOR,
				"isConversation", mContext.getPackageName(), 0, "test-shortcut-id");
	}

	@PermissionTest(permission="WIFI_ACCESS_COEX_UNSAFE_CHANNELS", sdkMin=31)
	public void testWifiAccessCoexUnsafeChannels(){
		//WifiManager.CoexCallback()
		try {
			BinderTransaction.getInstance().invoke(Transacts.WIFI_SERVICE, Transacts.WIFI_DESCRIPTOR,
					"unregisterCoexCallback", (Object) null);
		} catch (IllegalArgumentException ignored){
			logger.debug("Intended Exception : for null callback object, permission check would be already executed");
		}

	}

	@PermissionTest(permission="WIFI_UPDATE_COEX_UNSAFE_CHANNELS", sdkMin=31)
	public void testWifiUpdateCoexUnsafeChannels(){
		BinderTransaction.getInstance().invoke(Transacts.WIFI_SERVICE, Transacts.WIFI_DESCRIPTOR,
				"setCoexUnsafeChannels", null, 0);
	}

	@PermissionTest(permission="TRIGGER_SHELL_PROFCOLLECT_UPLOAD", sdkMin=32)
	public void testTriggerShellProfcollectUpload(){

		//behaviour changes?
		runShellCommandTest(
				"am broadcast --allow-background-activity-starts " +
						"-a com.android.shell.action.PROFCOLLECT_UPLOAD");
	}


	private final IBinder IBinderMock = new IBinder() {
		@Nullable
		@Override
		public String getInterfaceDescriptor() throws RemoteException {
			return null;
		}

		@Override
		public boolean pingBinder() {
			return false;
		}

		@Override
		public boolean isBinderAlive() {
			return false;
		}

		@Nullable
		@Override
		public IInterface queryLocalInterface(@NonNull String s) {
			return null;
		}

		@Override
		public void dump(@NonNull FileDescriptor fileDescriptor,
						 @Nullable String[] strings) throws RemoteException {

		}
		@Override
		public void dumpAsync(@NonNull FileDescriptor fileDescriptor,
							  @Nullable String[] strings) throws RemoteException {

		}
		@Override
		public boolean transact(int i, @NonNull Parcel parcel,
								@Nullable Parcel parcel1, int i1) throws RemoteException {
			return false;
		}

		@Override
		public void linkToDeath(@NonNull DeathRecipient deathRecipient, int i)
				throws RemoteException {

		}

		@Override
		public boolean unlinkToDeath(@NonNull DeathRecipient deathRecipient,
									 int i) {
			return false;
		}
	};

}









