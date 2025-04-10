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

import static android.Manifest.permission.BLUETOOTH_PRIVILEGED;

import android.Manifest;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.app.usage.UsageStatsManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.IOnPermissionsChangeListener;
import android.content.pm.LauncherApps;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.display.DisplayManager;
import android.hardware.usb.UsbManager;
import android.media.MediaRecorder;
import android.media.session.MediaSessionManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.Uri;
import android.net.VpnService;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IInterface;
import android.os.Build;
import android.os.DropBoxManager;
import android.os.Environment;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.storage.StorageManager;
import android.print.PrintManager;
import android.provider.BlockedNumberContract;
import android.provider.CallLog;
import android.provider.Settings;
import android.provider.Telephony;
import android.provider.VoicemailContract;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;

import com.android.certification.niap.permission.dpctester.MainActivity;
import com.android.certification.niap.permission.dpctester.activity.TestActivity;
import com.android.certification.niap.permission.dpctester.common.Constants;
import com.android.certification.niap.permission.dpctester.common.ReflectionUtil;
import com.android.certification.niap.permission.dpctester.service.TestService;
import com.android.certification.niap.permission.dpctester.test.exception.BypassTestException;
import com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException;
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestRunner;
import com.android.certification.niap.permission.dpctester.test.runner.SignaturePermissionTestModuleBase;
import com.android.certification.niap.permission.dpctester.test.tool.BinderTransaction;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTest;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestModule;
import com.android.internal.policy.IKeyguardDismissCallback;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.TimeZone;
import java.util.UUID;
import java.util.function.Consumer;

@PermissionTestModule(name="Signature Test Cases",prflabel = "Signature")
public class SignatureTestModule extends SignaturePermissionTestModuleBase {
	public SignatureTestModule(@NonNull Activity activity) {
		super(activity);
	}

	BluetoothAdapter mBluetoothAdapter;

	@NonNull
	@Override
	public PrepareInfo prepare(Consumer<PermissionTestRunner.Result> callback){
		try {
			mBluetoothAdapter = systemService(BluetoothManager.class).getAdapter();
		} catch (NullPointerException e) { /*Leave bluetoothAdapter as null, if manager isn't available*/ }
		return super.prepare(callback);
	}


	private <T> T systemService(Class<T> clazz) {
		return Objects.requireNonNull(getService(clazz), "[npe_system_service]" + clazz.getSimpleName());
	}

	@PermissionTest(permission = "ACCESS_AMBIENT_LIGHT_STATS",developmentProtection=true)
	public void testAccessAmbientLightStats() {
		ReflectionUtil.invoke(systemService(DisplayManager.class), "getAmbientBrightnessStats");
	}

	@PermissionTest(permission = "ACCESS_CACHE_FILESYSTEM")
	public void testAccessCacheFilesystem() {
		try {
			File f = new File(Environment.getDownloadCacheDirectory(),
					"test_access_cache_filesystem.out");
			if (f.createNewFile()) {
				logger.info("test_access_cache_filesystem.out generated.");
			}
		} catch (IOException e) {
			// If an app does not have this permission then an IOException will be
			// thrown with "Permission denied" in the message.
			if (Objects.requireNonNull(e.getMessage()).contains("Permission denied")) {
				throw new SecurityException(e);
			} else {
				throw new UnexpectedTestFailureException(e);
			}
		}
	}

	@PermissionTest(permission = "ACCESS_CONTENT_PROVIDERS_EXTERNALLY")
	public void testAccessContentProvidersExternally() {
		BinderTransaction.getInstance().invoke(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
				"getContentProviderExternal",
				"settings", 0,
				getActivityToken(), getTAG());
	}

	@PermissionTest(permission = "ACCESS_INSTANT_APPS")
	public void testAccessInstantApps() {
		ReflectionUtil.invoke(mPackageManager, "getInstantApps");
	}

	@PermissionTest(permission = "ACCESS_KEYGUARD_SECURE_STORAGE")
	public void testAccessKeyguardSecureStorage() {
		BinderTransaction.getInstance().invoke(Transacts.TRUST_SERVICE, Transacts.TRUST_DESCRIPTOR,
				"reportEnabledTrustAgentsChanged",
				0);
	}

	@PermissionTest(permission = "ACCESS_MTP")
	public void testAccessMtp() {
		BinderTransaction.getInstance().invoke(Transacts.USB_SERVICE, Transacts.USB_DESCRIPTOR,
				"getControlFd",
				0L);
	}

	@PermissionTest(permission = "ACCESS_NOTIFICATIONS")
	public void testAccessNotifications() {
		BinderTransaction.getInstance().invoke(Transacts.NOTIFICATION_SERVICE,
				Transacts.NOTIFICATION_DESCRIPTOR, "getActiveNotifications",
				mContext.getPackageName());
	}

	@PermissionTest(permission = "ACCESS_SHORTCUTS")
	public void testAccessShortcuts() {
		LauncherApps launcherApps = systemService(LauncherApps.class);
		launcherApps.hasShortcutHostPermission();
		int queryFlags = LauncherApps.ShortcutQuery.FLAG_MATCH_DYNAMIC
				| LauncherApps.ShortcutQuery.FLAG_MATCH_PINNED_BY_ANY_LAUNCHER;
		launcherApps.getShortcuts(new LauncherApps.ShortcutQuery().setQueryFlags(queryFlags),
				android.os.Process.myUserHandle());
	}

	@PermissionTest(permission = "ACCESS_SURFACE_FLINGER")
	public void testAccessSurfaceFlinger() {
		// SurfaceFlinger.cpp CheckTransactCodeCredentials should check this;
		// BOOT_FINISHED is set to FIRST_CALL_TRANSACTION since it is anticipated to be
		// called from the ActivityManagerService.
		try {
			BinderTransaction.getInstance().invoke(
					Transacts.SURFACE_FLINGER_SERVICE,
					Transacts.SURFACE_FLINGER_DESCRIPTOR,
					"showCpu");
		} catch (NoSuchElementException e) {
			//intended : ignore
		}
	}

	@PermissionTest(permission = "ACCESS_VOICE_INTERACTION_SERVICE")
	public void testAccessVoiceInteractionService() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
			// As of Android 12 this permission is no longer required to invoke this
			// transact.
			BinderTransaction.getInstance().invoke(Transacts.VOICE_INTERACTION_SERVICE,
					Transacts.VOICE_INTERACTION_DESCRIPTOR,
					"getActiveServiceComponentName");
		} else {
			BinderTransaction.getInstance().invoke(Transacts.VOICE_INTERACTION_SERVICE,
					Transacts.VOICE_INTERACTION_DESCRIPTOR,
					"isSessionRunning");
		}
	}

	@PermissionTest(permission = "ACCESS_VR_MANAGER")
	public void testAccessVrManager() {
		BinderTransaction.getInstance().invoke(Transacts.VR_SERVICE, Transacts.VR_DESCRIPTOR,
				"setStandbyEnabled",
				true);
	}

	@PermissionTest(permission = "ACCESS_VR_STATE")
	public void testAccessVrState() {
		BinderTransaction.getInstance().invoke(Transacts.VR_SERVICE, Transacts.VR_DESCRIPTOR,
				"getVrModeState");
	}

	@PermissionTest(permission = "ALLOCATE_AGGRESSIVE")
	public void testAllocateAggressive() {
		UUID storageUUID;
		StorageManager storageManager = systemService(StorageManager.class);
		try {
			storageUUID = storageManager.getUuidForPath(
					Environment.getDataDirectory());
		} catch (IOException e) {
			throw new UnexpectedTestFailureException(e);
		}
		ReflectionUtil.invoke(storageManager, "getAllocatableBytes",
				new Class[]{UUID.class, int.class}, storageUUID, 1);
	}

	@PermissionTest(permission = "BACKUP")
	public void testBackup() {
		// This test will be skipped if the backup service is disabled; if this happens check
		// for the following touch file on the device:
		// /data/backup/backup-suppress
		BinderTransaction.getInstance().invoke(Transacts.BACKUP_SERVICE, Transacts.BACKUP_DESCRIPTOR,
				"setBackupEnabled",
				true);
	}

	@PermissionTest(permission = "BATTERY_STATS",developmentProtection = true)
	public void testBatteryStats() {
		BinderTransaction.getInstance().invoke(Transacts.BATTERY_STATS_SERVICE, Transacts.BATTERY_STATS_DESCRIPTOR,
				"getAwakeTimeBattery");
	}

	@PermissionTest(permission = "BRIGHTNESS_SLIDER_USAGE",developmentProtection = true)
	public void testBrightnessSliderUsage() {

		ReflectionUtil.invoke(systemService(DisplayManager.class), "getBrightnessEvents");
	}

	@SuppressLint("WrongConstant")
	@PermissionTest(permission = "CAPTURE_AUDIO_OUTPUT")
	public void testCaptureAudioOutput() {

		MediaRecorder recorder;
		try {
			recorder = new MediaRecorder();
			recorder.setAudioSource(MediaRecorder.AudioSource.REMOTE_SUBMIX);
			String fileName = mContext.getFilesDir() + "/test_capture_audio_output.3gpp";
			recorder.setOutputFile(new File(fileName));
			recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			recorder.prepare();
			recorder.start();
		} catch (RuntimeException e) {
			// The call to start was observed to fail with a RuntimeException with
			// the following:
			// ServiceManager: Permission failure:
			// android.permission.CAPTURE_AUDIO_OUTPUT from uid=10167 pid=24184
			throw new SecurityException(e);
		} catch (IOException ioe) {
			// An IOException indicates that the permission check was passed and the
			// API should be considered as being successfully invoked.
			logger.debug("Caught an IOException: ", ioe);
			return;
		}
		// If the call to start has been passed then the permission check was
		// successful and any failures in stop can still be treated as a successful
		// API invocation.
		try {
			Thread.sleep(1000);//need wait a while
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		recorder.stop();

	}

	@PermissionTest(permission = "CAPTURE_SECURE_VIDEO_OUTPUT")
	public void testCaptureSecureVideoOutput() {
		systemService(DisplayManager.class).createVirtualDisplay(getTAG(), 10, 10, 1, null,
				DisplayManager.VIRTUAL_DISPLAY_FLAG_SECURE);
	}

	@PermissionTest(permission = "CAPTURE_VIDEO_OUTPUT")
	public void testCaptureVideoOutput() {
		systemService(DisplayManager.class).createVirtualDisplay(getTAG(), 10, 10, 1, null,
				DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR);
	}

	@PermissionTest(permission = "CHANGE_APP_IDLE_STATE")
	public void testChangeAppIdleState() {
		ReflectionUtil.invoke(systemService(UsageStatsManager.class), "setAppStandbyBucket",
				new Class[]{String.class, int.class},
				Constants.COMPANION_PACKAGE,
				UsageStatsManager.STANDBY_BUCKET_ACTIVE);
	}

	//TODO: REMOTE_VIEW SERVICE?
	@PermissionTest(permission = "CHANGE_COMPONENT_ENABLED_STATE")
	public void testChangeComponentEnabledState() {
		ComponentName testComponent =
				new ComponentName("android.backup.app",
						".FullBackupBackupAgent");
		try {
			mPackageManager.setComponentEnabledSetting(testComponent,
					PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, 0);
		} catch (IllegalArgumentException ex){
			//expected..
		}
	}

	@PermissionTest(permission = "CHANGE_CONFIGURATION",developmentProtection = true)
	public void testChangeConfiguration() {
		BinderTransaction.getInstance().invoke(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
				"updateConfiguration",
				0);
	}

	@PermissionTest(permission = "CLEAR_APP_CACHE",sdkMax = 34)
	public void testClearAppCache() {

		ReflectionUtil.invoke(mPackageManager, "freeStorage",
				new Class<?>[]{long.class, IntentSender.class}, "",100, null);

	}

	@PermissionTest(permission = "CLEAR_APP_USER_DATA")
	public void testClearAppUserData() {

		if (Constants.BYPASS_TESTS_AFFECTING_UI)
			throw new BypassTestException("This test case affects to UI. skip to avoiding ui stuck.");

		Class<?> packageDataObserverClass;
		try {
			packageDataObserverClass = Class
					.forName("android.content.pm.IPackageDataObserver");
		} catch (ReflectiveOperationException e) {
			throw new UnexpectedTestFailureException(e);
		}
		ReflectionUtil.invoke(mPackageManager, "clearApplicationUserData",
				new Class<?>[]{String.class, packageDataObserverClass},
				Constants.COMPANION_PACKAGE, null);
		// After clearing the application user data sleep for a couple seconds to allow
		// time for the operation to complete; if another operation is attempted against
		// the companion package, such as attempting to bind to one of its test
		// services, this could fail with the error "Package is currently frozen"
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			logger.error(
					"Caught an InterruptedException while sleeping after clearing app user data:",
					e);
		}
	}

	@PermissionTest(permission = "CONFIGURE_DISPLAY_BRIGHTNESS",developmentProtection = true)
	public void testConfigureDisplayBrightness() {
		ReflectionUtil.invoke(systemService(DisplayManager.class), "getBrightnessConfiguration");
	}

	@PermissionTest(permission = "CONFIGURE_DISPLAY_COLOR_MODE")
	public void testConfigureDisplayColorMode() {
		BinderTransaction.getInstance().invoke(Transacts.DISPLAY_SERVICE,
				Transacts.DISPLAY_DESCRIPTOR,
				"requestColorMode",
				0, 0);
	}

	@PermissionTest(permission = "CONFIGURE_WIFI_DISPLAY")
	public void testConfigureWifiDisplay() {
		BinderTransaction.getInstance().invoke(Transacts.DISPLAY_SERVICE,
				Transacts.DISPLAY_DESCRIPTOR,
				"startWifiDisplayScan");
	}

	@PermissionTest(permission = "CONFIRM_FULL_BACKUP")
	public void testConfirmFullBackup() {
		BinderTransaction.getInstance().invoke(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
				"bindBackupAgent",
				mContext.getPackageName(), 1, appUid);
	}

	@PermissionTest(permission = "CONNECTIVITY_INTERNAL")
	public void testConnectivityInternal() {
		BinderTransaction.getInstance().invoke(Transacts.CONNECTIVITY_SERVICE,
				Transacts.CONNECTIVITY_DESCRIPTOR,
				"getActiveNetworkForUid",
				appUid, false);
	}

	@PermissionTest(permission = "CONNECTIVITY_USE_RESTRICTED_NETWORKS")
	public void testConnectivityUseRestrictedNetworks() {
		// Note, the platform will first check for CONNECTIVITY_USE_RESTRICTED_NETWORKS,
		// and if that fails then it will check for CONNECTIVITY_INTERNAL which is also
		// a signature permission. CONNECTIVITY_INTERNAL is being phased out, but this
		// is why SecurityException messages may reference CONNECTIVITY_INTERNAL
		// instead of the permission under test.

		// Prior to Android 12 the NetworkRequest could be modified via reflection to
		// request restricted networks.
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
			try {
				NetworkRequest.Builder networkRequestBuilder =
						new NetworkRequest.Builder()
								.addCapability(
										NetworkCapabilities.NET_CAPABILITY_INTERNET);
				NetworkRequest networkRequest = networkRequestBuilder.build();
				Field capabilitiesField = networkRequest.getClass().getField(
						"networkCapabilities");
				NetworkCapabilities capabilities =
						(NetworkCapabilities) capabilitiesField
								.get(networkRequest);
				//The field access throw exception as of SDK 33.
				@SuppressLint("SoonBlockedPrivateApi") Field capabilitiesValueField =
						capabilities.getClass().getDeclaredField("mNetworkCapabilities");

				capabilitiesValueField.setAccessible(true);
				long capabilitiesValue = (long) capabilitiesValueField.get(
						capabilities);
				capabilitiesValueField.set(capabilities, capabilitiesValue & ~(1
						<< NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED));
				systemService(ConnectivityManager.class).requestNetwork(networkRequest,
						new ConnectivityManager.NetworkCallback() {
							@Override
							public void onAvailable(Network network) {
								logger.debug(
										"onAvailable called with network " + network);
							}
						});
			} catch (ReflectiveOperationException e) {
				throw new UnexpectedTestFailureException(e);
			}
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			// The following are the default NetworkCapabilities without
			// NET_CAPABILITY_NOT_RESTRICTED set.
			Class<?> ncBuilderClazz = null;
			try {
				ncBuilderClazz = Class.forName("android.net.NetworkCapabilities$Builder");

				Object ncBuilderObj = ReflectionUtil.invoke(ncBuilderClazz,
						"withoutDefaultCapabilities");
				ncBuilderObj = ReflectionUtil.invoke(ncBuilderObj,
						"addCapability",
						new Class<?>[]{int.class}, NetworkCapabilities.NET_CAPABILITY_TRUSTED);
				ncBuilderObj = ReflectionUtil.invoke(ncBuilderObj,
						"addCapability",
						new Class<?>[]{int.class}, NetworkCapabilities.NET_CAPABILITY_NOT_VPN);

				NetworkCapabilities nc = (NetworkCapabilities) ReflectionUtil.invoke(ncBuilderObj,
						"build");

				Intent intent = new Intent(mContext, TestActivity.class);
				PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent,
						PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

				BinderTransaction.getInstance().invoke(Transacts.CONNECTIVITY_SERVICE,
						Transacts.CONNECTIVITY_DESCRIPTOR,
						"pendingRequestForNetwork",
						nc,
						pendingIntent, mContext.getPackageName(), mContext.getAttributionTag());


			} catch (ClassNotFoundException e) {
				throw new UnexpectedTestFailureException(e);
			}
		}
	}

	@PermissionTest(permission = "CONTROL_DISPLAY_BRIGHTNESS")
	public void testControlDisplayBrightness() {
		BinderTransaction.getInstance().invoke(Transacts.DISPLAY_SERVICE,
				Transacts.DISPLAY_DESCRIPTOR,
				"setTemporaryAutoBrightnessAdjustment",
				0.0f);

	}

	@PermissionTest(permission = "CONTROL_KEYGUARD")
	public void testControlKeyguard() {
		BinderTransaction.getInstance().invoke(Transacts.WINDOW_SERVICE, Transacts.WINDOW_DESCRIPTOR,
				"dismissKeyguard",
				(IBinder) null);
	}

	@PermissionTest(permission = "CONTROL_LOCATION_UPDATES")
	public void testControlLocationUpdates() {
		BinderTransaction.getInstance().invoke(Transacts.TELEPHONY_SERVICE,
				Transacts.TELEPHONY_DESCRIPTOR,
				"enableLocationUpdates");
	}

	@PermissionTest(permission = "CONTROL_REMOTE_APP_TRANSITION_ANIMATIONS")
	public void testControlRemoteAppTransitionAnimations() {

		if (Constants.BYPASS_TESTS_AFFECTING_UI)
			throw new BypassTestException("This test case affects to UI. skip to avoiding ui stuck.");

		BinderTransaction.getInstance().invoke(Transacts.WINDOW_SERVICE, Transacts.WINDOW_DESCRIPTOR,
				"overridePendingAppTransitionRemote",
				 new IInterface() {
					@Override
					public IBinder asBinder() {
						return null;
					}
				});
	}

	@PermissionTest(permission = "CONTROL_VPN")
	public void testControlVpn() {
		ReflectionUtil.invoke(VpnService.class, "prepareAndAuthorize",
				new Class<?>[]{Context.class}, mContext);
	}

	@PermissionTest(permission = "CREATE_USERS")
	public void testCreateUsers() {
		UserManager userManager = systemService(UserManager.class);
		Object userInfo = ReflectionUtil.invoke(userManager, "createUser",
				new Class<?>[]{String.class, int.class}, "test_user", 0);
		try {
			Field idField = userInfo.getClass().getField("id");
			int userId = (int) idField.get(userInfo);
			ReflectionUtil.invoke(userManager, "removeUser",
					new Class[]{int.class}, userId);
		} catch (ReflectiveOperationException e) {
			logger.debug(
					"Caught an exception attempting to remove the user for the CREATE_USERS "
							+ "test: ",
					e);
		}
	}

	//TODO:delete Package failed?
	@SuppressLint("PrivateApi")
    @PermissionTest(permission = "DELETE_PACKAGES")
	public void testDeletePackages() {
		Class<?> packageDeleteObserverClass = null;
		try {
			packageDeleteObserverClass = Class
					.forName("android.content.pm.IPackageDeleteObserver");
		} catch (ReflectiveOperationException e) {
			throw new UnexpectedTestFailureException(e);
		}
		try {
			ReflectionUtil.invoke(mPackageManager, "deletePackage",
					new Class<?>[]{String.class, packageDeleteObserverClass, int.class},
					"com.example.test", null, 0);
		} catch (ReflectionUtil.ReflectionIsTemporaryException e) {
			// The only guaranteed package on the device that can be deleted is the companion
			// package, but that would break any subsequent tests that require connecting
			// to the services exported by the companion app. A ReflectiveOperationException
			// can be thrown due to an IllegalArgumentException since the specified package
			// does not exist on the device, but if that point is reached it indicates the
			// permission check was passed and the API invocation should be considered
			// successful.
			Throwable cause = e.getCause();
			while (cause != null) {
				if (cause instanceof IllegalArgumentException) {
					logger.debug("Permission check successful with following exception: ",
							cause);
					return;
				}
				cause = cause.getCause();
			}
			throw e;
		}
	}

	@PermissionTest(permission = "DEVICE_POWER")
	public void testDevicePower() {
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P) {
			BinderTransaction.getInstance().invoke(Transacts.POWER_SERVICE, Transacts.POWER_DESCRIPTOR,
					"setPowerSaveMode",
					false);
		} else {
			ReflectionUtil.invoke(systemService(PowerManager.class), "setPowerSaveModeEnabled",
					new Class<?>[]{boolean.class}, false);
		}

	}

	@PermissionTest(permission = "DISABLE_INPUT_DEVICE")
	public void testDisableInputDevice() {
		BinderTransaction.getInstance().invoke(Transacts.INPUT_SERVICE, Transacts.INPUT_DESCRIPTOR,
				"enableInputDevice",
				1);
	}

	@PermissionTest(permission = "DUMP",developmentProtection = true)
	public void testDump() {
		// The stats service cannot be used for this test since it is guarded by SELinux
		// policy.
		BinderTransaction.getInstance().invoke(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
				"requestBugReport",
				0);
	}

	@PermissionTest(permission = "FORCE_BACK")
	public void testForceBack() {
		// if the permission if granted then do not invoke this as it can interrupt the test
		if (checkPermissionGranted("android.permission.FORCE_BACK") && !acceptDangerousApi) {
			throw new BypassTestException(
					"The API guarded by this permission will exit this activity");
		}
		BinderTransaction.getInstance().invoke(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
				"unhandledBack");
	}

	@PermissionTest(permission = "FORCE_PERSISTABLE_URI_PERMISSIONS")
	public void testForcePersistableUriPermissions() {
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P) {
			BinderTransaction.getInstance().invoke(Transacts.ACTIVITY_SERVICE,
					Transacts.ACTIVITY_DESCRIPTOR,
					"releasePersistableUriPermission",
					0,
					Intent.FLAG_GRANT_READ_URI_PERMISSION, mContext.getPackageName(),
					appUid);
		} else {
			BinderTransaction.getInstance().invoke(Transacts.URI_GRANTS_SERVICE,
					Transacts.URI_GRANTS_DESCRIPTOR,
					"takePersistableUriPermission",
					0,
					Intent.FLAG_GRANT_READ_URI_PERMISSION, mContext.getPackageName(),
					appUid);
		}
	}

	@PermissionTest(permission = "FORCE_STOP_PACKAGES")
	public void testForceStopPackages() {
		ReflectionUtil.invoke(systemService(ActivityManager.class), "forceStopPackage"
				, new Class<?>[]{String.class}, "com.example.test");
	}

	@PermissionTest(permission = "FRAME_STATS")
	public void testFrameStats() {
		try {
			Field tokenField = Activity.class.getDeclaredField("mToken");
			tokenField.setAccessible(true);
			IBinder token = (IBinder) tokenField.get(mActivity);
			BinderTransaction.getInstance().invoke(Transacts.WINDOW_SERVICE, Transacts.WINDOW_DESCRIPTOR,
					"clearWindowContentFrameStats",
					token);

		} catch (ReflectiveOperationException e) {
			throw new UnexpectedTestFailureException(e);
		}
	}

	@PermissionTest(permission = "FREEZE_SCREEN")
	public void testFreezeScreen() {
		BinderTransaction.getInstance().invoke(Transacts.WINDOW_SERVICE, Transacts.WINDOW_DESCRIPTOR,
				"stopFreezingScreen");
	}

	@PermissionTest(permission = "GET_APP_GRANTED_URI_PERMISSIONS")
	public void testGetAppGrantedUriPermissions() {
		String service = Transacts.URI_GRANTS_SERVICE;
		String descriptor = Transacts.URI_GRANTS_DESCRIPTOR;
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P) {
			service = Transacts.ACTIVITY_SERVICE;
			descriptor = Transacts.ACTIVITY_DESCRIPTOR;
		}
		BinderTransaction.getInstance().invoke(service, descriptor,
				"getGrantedUriPermissions",
				mContext.getPackageName(), appUid);
	}

	@PermissionTest(permission = "GET_APP_OPS_STATS",developmentProtection = true)
	public void testGetAppOpsStats() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {

			ReflectionUtil.invoke(systemService(AppOpsManager.class), "getPackagesForOps",
					new Class<?>[]{int[].class}, new int[]{0});
		} else {
			// Starting in Android 12 getPackagesForOps can return the calling package
			// without the permission.
			BinderTransaction.getInstance().invoke(Transacts.APP_OPS_SERVICE,
					Transacts.APP_OPS_DESCRIPTOR,
					"getUidOps",
					1000,
					new int[]{0});
		}
	}

	@PermissionTest(permission = "GET_INTENT_SENDER_INTENT")
	public void testGetIntentSenderIntent() {
		IBinder token = getActivityToken();
		BinderTransaction.getInstance().invoke(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
				"getIntentForIntentSender",
				token);
	}

	@PermissionTest(permission = "GET_TOP_ACTIVITY_INFO")
	public void testGetTopActivityInfo() {
		String service = Transacts.ACTIVITY_TASK_SERVICE;
		String descriptor = Transacts.ACTIVITY_TASK_DESCRIPTOR;
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P) {
			service = Transacts.ACTIVITY_SERVICE;
			descriptor = Transacts.ACTIVITY_DESCRIPTOR;
		}
		BinderTransaction.getInstance().invoke(service, descriptor,
				"getAssistContextExtras",
				0);
	}

	@PermissionTest(permission = "HARDWARE_TEST")
	public void testHardwareTest() {
		// The showCpu transaction should result in an invalid transaction ID; when the
		// SurfaceFlinger receives this UNKNOWN_TRANSACTION result it will check if the caller
		// has the HARDWARE_TEST permission before proceeding.
		try {
			BinderTransaction.getInstance().invoke(Transacts.SURFACE_FLINGER_SERVICE,
					Transacts.SURFACE_FLINGER_DESCRIPTOR,
					"showCpu");
		} catch (NoSuchElementException ex){
			//Intended ignore
		}
	}

	@PermissionTest(permission = "INSTALL_GRANT_RUNTIME_PERMISSIONS")
	public void testInstallGrantRuntimePermissions() {
		try {
			PackageInstaller installer = mPackageManager.getPackageInstaller();
			PackageInstaller.SessionParams params = new PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL);
			ReflectionUtil.invoke(params, "setGrantedRuntimePermissions",
					new Class<?>[]{String[].class},
					(Object) new String[]{Manifest.permission.CAMERA});
			installer.createSession(params);
		} catch (IOException e) {
			throw new UnexpectedTestFailureException(e);
		}
	}

	@PermissionTest(permission = "INSTALL_PACKAGES")
	public void testInstallPackages() {
		BinderTransaction.getInstance().invoke(Transacts.PACKAGE_SERVICE, Transacts.PACKAGE_DESCRIPTOR,
				"installExistingPackageAsUser",
				"com.example.test", 0, 0, 0);
	}

	@PermissionTest(permission = "INTENT_FILTER_VERIFICATION_AGENT")
	public void testIntentFilterVerificationAgent() {
		ReflectionUtil.invoke(mPackageManager, "verifyIntentFilter",
				new Class<?>[]{int.class, int.class, List.class}, 0, 0,
				Collections.singletonList("test_intent_filter"));
	}

	@PermissionTest(permission = "INTERACT_ACROSS_USERS",developmentProtection = true)
	public void testInteractAcrossUsers() {
		ReflectionUtil.invoke(systemService(ActivityManager.class), "isUserRunning",
				new Class<?>[]{int.class}, 1);
	}

	@PermissionTest(permission = "INTERACT_ACROSS_USERS_FULL")
	public void testInteractAcrossUsersFull() {
		ReflectionUtil.invoke(mPackageManager,
				"getDefaultBrowserPackageNameAsUser",
				new Class<?>[]{int.class}, 1);
	}

	@PermissionTest(permission = "INTERNAL_DELETE_CACHE_FILES")
	public void testInternalDeleteCacheFiles() {
		try {
			Class<?> packageDataObserverClass = Class
					.forName("android.content.pm.IPackageDataObserver");
			ReflectionUtil.invoke(mPackageManager,
					"deleteApplicationCacheFiles",
					new Class<?>[]{String.class, packageDataObserverClass},
					Constants.COMPANION_PACKAGE, null);
		} catch (ReflectiveOperationException e) {
			throw new UnexpectedTestFailureException(e);
		}
	}

	@PermissionTest(permission = "INTERNAL_SYSTEM_WINDOW")
	public void testInternalSystemWindow() {
		BinderTransaction.getInstance().invoke(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
				"setHasTopUi",
				true);
	}

	@PermissionTest(permission = "KILL_UID")
	public void testKillUid() {
		ReflectionUtil.invoke(systemService(ActivityManager.class), "killUid",
				new Class<?>[]{int.class, String.class}, 99999, "Test KILL_UID");
	}

	@PermissionTest(permission = "LOCAL_MAC_ADDRESS")
	public void testLocalMacAddress() {
		//Change the process since m
		String macAddress = "02:00:00:00:00:00";
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			macAddress = Settings.Secure.getString(
					mContext.getContentResolver(), "bluetooth_address");
		} else {
			macAddress = mBluetoothAdapter.getAddress();
		}
		//String
		// The BluetoothAdapter class indicates that the hidden DEFAULT_MAC_ADDRESS
		// field's value will be returned to apps that do not have the LOCAL_MAC_ADDRESS
		// permission.
		if (macAddress.equals("02:00:00:00:00:00")) {
			throw new SecurityException(
					"Received the default MAC address for apps without the "
							+ "LOCAL_MAC_ADDRESS permission");
		}
		logger.debug("Bluetooth address: " + macAddress);
	}


	@PermissionTest(permission = "MANAGE_APP_OPS_MODES")
	public void testManageAppOpsModes() {

		int uid = mContext.getApplicationContext().getApplicationInfo().uid;

		ReflectionUtil.invoke(systemService(AppOpsManager.class), "setMode",
				new Class<?>[]{String.class, int.class, String.class, int.class},
				AppOpsManager.OPSTR_CAMERA, uid, mContext.getPackageName(),
				AppOpsManager.MODE_ALLOWED);

	}

	@PermissionTest(permission = "MANAGE_APP_OPS_RESTRICTIONS")
	public void testManageAppOpsRestrictions() {
		IBinder token = getActivityToken();
		BinderTransaction.getInstance().invoke(Transacts.APP_OPS_SERVICE,
				Transacts.APP_OPS_DESCRIPTOR,
				"setUserRestriction",
				0, false, token, 0, 0);
	}

	@PermissionTest(permission = "MANAGE_APP_TOKENS")
	public void testManageAppTokens() {
		IBinder token = getActivityToken();
		BinderTransaction.getInstance().invoke(Transacts.WINDOW_SERVICE, Transacts.WINDOW_DESCRIPTOR,
				"removeWindowToken",
				token, 1);
	}

	@PermissionTest(permission = "MANAGE_AUTO_FILL")
	public void testManageAutoFill() {
		runShellCommandTest("cmd autofill list sessions");
	}

	@PermissionTest(permission = "MANAGE_CA_CERTIFICATES")
	public void testManageCaCertificates() {
		BinderTransaction.getInstance().invoke(Transacts.DEVICE_POLICY_SERVICE,
				Transacts.DEVICE_POLICY_DESCRIPTOR,
				"installCaCert",
				0,
				mContext.getPackageName(), new byte[0]);
	}

	@PermissionTest(permission = "MANAGE_DEVICE_ADMINS")
	public void testManageDeviceAdmins() {
		BinderTransaction.getInstance().invoke(Transacts.PACKAGE_SERVICE, Transacts.PACKAGE_DESCRIPTOR,
				"isPackageStateProtected",
				mContext.getPackageName(), appUid);
	}

	@PermissionTest(permission = "MANAGE_DOCUMENTS",sdkMax = 28)
	public void testManageDocuments() {
		// Without this permission this query fails with a SecurityException, but with it it
		// fails with an Unsupported Uri exception.


		//TODO: CHAnage Test Routine as of Android Q
		//Because SAF(Storage Access Framework installed)
		//oogleplex-android/platform/superproject/main/+/main:vendor/xts/gts-tests/tests/permission/src/com/google/android/permission/gts/ManageDocumentsPermissionTest.java;l=42?q=MANAGE_DOCUMENTS&sq=repo:googleplex-android%2Fplatform%2Fsuperproject%2Fmain%20branch:main
		mContentResolver.query(Uri.parse("content://com.android.externalstorage.documents/"),
				null, null, null, null);
	}

	@PermissionTest(permission = "MANAGE_FINGERPRINT")
	public void testManageFingerprint() {
		IBinder token = getActivityToken();
		BinderTransaction.getInstance().invoke(Transacts.FINGERPRINT_SERVICE,
				Transacts.FINGERPRINT_DESCRIPTOR,
				"cancelEnrollment",
				token);
	}

	@PermissionTest(permission = "MANAGE_MEDIA_PROJECTION")
	public void testManageMediaProjection() {
		BinderTransaction.getInstance().invoke(Transacts.MEDIA_PROJECTION_SERVICE,
				Transacts.MEDIA_PROJECTION_DESCRIPTOR,
				"getActiveProjectionInfo");
	}

	@PermissionTest(permission = "MANAGE_NETWORK_POLICY")
	public void testManageNetworkPolicy() {
		BinderTransaction.getInstance().invoke(Transacts.NET_POLICY_SERVICE,
				Transacts.NET_POLICY_DESCRIPTOR,
				"getUidPolicy",
				appUid);
	}

	@PermissionTest(permission = "MANAGE_NOTIFICATIONS")
	public void testManageNotifications() {
		BinderTransaction.getInstance().invoke(Transacts.NOTIFICATION_SERVICE,
				Transacts.NOTIFICATION_DESCRIPTOR,
				"getZenRules");
	}

	@PermissionTest(permission = "MANAGE_PROFILE_AND_DEVICE_OWNERS",sdkMax = 33)
	public void testManageProfileAndDeviceOwners() {
		// Tests fails with a SecurityException without the permission but still
		// fails with the permission with another exception since the device owner
		// cannot be set if the device is already setup-up.
		ComponentName componentName = new ComponentName(mContext, MainActivity.class);
		try {
			BinderTransaction.getInstance().invoke(Transacts.DEVICE_POLICY_SERVICE,
					Transacts.DEVICE_POLICY_DESCRIPTOR,
					"setDeviceOwner",
					componentName, "test owner", 0);
		} catch(IllegalStateException ex){
			//expected ignore
		}
	}

	@PermissionTest(permission = "MANAGE_SENSORS")
	public void testManageSensors() {
		runShellCommandTest("cmd sensorservice reset-uid-state " + mContext.getPackageName());
	}

	@PermissionTest(permission = "MANAGE_SLICE_PERMISSIONS")
	public void testManageSlicePermissions() {
		Uri testUri = Uri.parse("content://" + mContext.getPackageName() + "/test");
		BinderTransaction.getInstance().invoke(Transacts.SLICE_SERVICE, Transacts.SLICE_DESCRIPTOR,
				"grantPermissionFromUser",
				testUri, "android",
				mContext.getPackageName(), true);
	}

	@PermissionTest(permission = "MANAGE_SOUND_TRIGGER")
	public void testManageSoundTrigger() {
		ParcelUuid uuid = new ParcelUuid(UUID.randomUUID());
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
			BinderTransaction.getInstance().invoke(Transacts.SOUND_TRIGGER_SERVICE,
					Transacts.SOUND_TRIGGER_DESCRIPTOR,
					"isRecognitionActive",
					uuid);
		} else {
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
			Parcel result = BinderTransaction.getInstance().invoke(Transacts.SOUND_TRIGGER_SERVICE,
					Transacts.SOUND_TRIGGER_DESCRIPTOR,
					"attachAsOriginator",
					identity, new Binder());
			IBinder binder = result.readStrongBinder();

			BinderTransaction.getInstance().invokeIBinder(binder,
					Transacts.SOUND_TRIGGER_SESSION_DESCRIPTOR,
					"getModuleProperties",
					false, uuid);
		}
	}

	@PermissionTest(permission = "MANAGE_SUBSCRIPTION_PLANS")
	public void testManageSubscriptionPlans() {
		BinderTransaction.getInstance().invoke(Transacts.NET_POLICY_SERVICE,
				Transacts.NET_POLICY_DESCRIPTOR,
				"getSubscriptionPlans",
				0, mContext.getPackageName());
	}

	@PermissionTest(permission = "MANAGE_USB")
	public void testManageUsb() {
		ReflectionUtil.invoke(systemService(UsbManager.class), "getPorts");
	}

	@PermissionTest(permission = "MANAGE_USERS")
	public void testManageUsers() {
		BinderTransaction.getInstance().invoke(Transacts.PACKAGE_SERVICE, Transacts.PACKAGE_DESCRIPTOR,
				"isPackageDeviceAdminOnAnyUser",
				mContext.getPackageName());
	}

	@PermissionTest(permission = "MASTER_CLEAR")
	public void testMasterClear() {
		// Note, if this permission is granted this action could potentially interrupt the test
		// activity. If the test is interrupted consider skipping this test if the permission
		// is granted.
		Intent intent = new Intent(mContext, TestActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
		// In Android 9 this API only accepted a PendingIntent
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P) {
			BinderTransaction.getInstance().invoke(Transacts.EUICC_CONTROLLER_SERVICE,
					Transacts.EUICC_CONTROLLER_DESCRIPTOR,
					"retainSubscriptionsForFactoryReset",
					pendingIntent);
		} else {
			// Later Android platforms also required a cardId
			BinderTransaction.getInstance().invoke(Transacts.EUICC_CONTROLLER_SERVICE,
					Transacts.EUICC_CONTROLLER_DESCRIPTOR,
					"retainSubscriptionsForFactoryReset",
					0, pendingIntent);
		}
	}

	@PermissionTest(permission = "MEDIA_CONTENT_CONTROL")
	public void testMediaContentControl() {
		MediaSessionManager mediaSessionManager =
				(MediaSessionManager) mContext.getSystemService(
						Context.MEDIA_SESSION_SERVICE);
		mediaSessionManager.getActiveSessions(
				new ComponentName(mContext, MainActivity.class));
	}

	@PermissionTest(permission = "MODIFY_ACCESSIBILITY_DATA")
	public void testModifyAccessibilityData() {
		BinderTransaction.getInstance().invoke(Transacts.ACCESSIBILITY_SERVICE,
				Transacts.ACCESSIBILITY_DESCRIPTOR,
				"setPictureInPictureActionReplacingConnection",
				getActivityToken());
	}

	@PermissionTest(permission = "MODIFY_APPWIDGET_BIND_PERMISSIONS")
	public void testModifyAppwidgetBindPermissions() {
		BinderTransaction.getInstance().invoke(Transacts.APPWIDGET_SERVICE,
				Transacts.APPWIDGET_DESCRIPTOR,
				"setBindAppWidgetPermission",
				mContext.getPackageName(), 0,
				true);
	}

	@PermissionTest(permission = "MODIFY_AUDIO_ROUTING")
	public void testModifyAudioRouting() {
		BinderTransaction.getInstance().invoke(Transacts.AUDIO_SERVICE, Transacts.AUDIO_DESCRIPTOR,
				"isAudioServerRunning");
	}

	@PermissionTest(permission = "MODIFY_PHONE_STATE")
	public void testModifyPhoneState() {
		int phoneId = SubscriptionManager.getDefaultSubscriptionId();
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
			try {
				ReflectionUtil.invoke(Class.forName("com.android.ims.ImsManager"),
						"getInstance", null,
						new Class<?>[]{Context.class, int.class}, mContext, phoneId);
			} catch (ReflectiveOperationException e) {
				throw new UnexpectedTestFailureException(e);
			}
		} else {
			if (phoneId == -1) {
				throw new BypassTestException(
						"This device does not have a valid subscription on which to "
								+ "run this test");
			}
			ReflectionUtil.invoke(systemService(TelephonyManager.class), "nvReadItem",
					new Class[]{int.class}, 0);
		}
	}

	@PermissionTest(permission = "MOUNT_FORMAT_FILESYSTEMS")
	public void testMountFormatFilesystems() {
		//
		BinderTransaction.getInstance().invoke(Transacts.MOUNT_SERVICE, Transacts.MOUNT_DESCRIPTOR,
				"benchmark",
				"private", (IBinder) null);
	}

	@PermissionTest(permission = "MOUNT_UNMOUNT_FILESYSTEMS")
	public void testMountUnmountFilesystems() {
		BinderTransaction.getInstance().invoke(Transacts.PACKAGE_SERVICE, Transacts.PACKAGE_DESCRIPTOR,
				"getMoveStatus",
				1);
	}

	@PermissionTest(permission = "MOVE_PACKAGE")
	public void testMovePackage() {
		BinderTransaction.getInstance().invoke(Transacts.PACKAGE_SERVICE, Transacts.PACKAGE_DESCRIPTOR,
				"movePackage",
				"com.example.test", "test");
	}

	@PermissionTest(permission = "NETWORK_SETTINGS")
	public void testNetworkSettings() {
		BinderTransaction.getInstance().invoke(Transacts.NETWORK_MANAGEMENT_SERVICE,
				Transacts.NETWORK_MANAGEMENT_DESCRIPTOR,
				"setDataSaverModeEnabled",
				false);
	}

	@PermissionTest(permission = "NETWORK_STACK")
	public void testNetworkStack() {
		BinderTransaction.getInstance().invoke(Transacts.WIFI_SERVICE, Transacts.WIFI_DESCRIPTOR,
				"stopSoftAp");
	}

	@PermissionTest(permission = "NOTIFY_PENDING_SYSTEM_UPDATE")
	public void testNotifyPendingSystemUpdate() {
		ReflectionUtil.invoke(systemService(DevicePolicyManager.class),
				"notifyPendingSystemUpdate",
				new Class<?>[]{long.class}, System.currentTimeMillis());
	}

	@PermissionTest(permission = "OBSERVE_APP_USAGE")
	public void testObserveAppUsage() {
		ReflectionUtil.invoke(systemService(UsageStatsManager.class),
				"unregisterAppUsageObserver",
				new Class<?>[]{int.class}, 1);
	}

	@PermissionTest(permission = "OBSERVE_GRANT_REVOKE_PERMISSIONS")
	public void testObserveGrantRevokePermissions() {
		IOnPermissionsChangeListener listener = new IOnPermissionsChangeListener() {
			@Override
			public void onPermissionsChange(int uid) {
				logger.debug(
						"Received notification of a changed permission for UID " + uid);
			}

			@Override
			public Binder asBinder() {
				return null;
			}
		};
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
			BinderTransaction.getInstance().invoke(Transacts.PACKAGE_SERVICE, Transacts.PACKAGE_DESCRIPTOR,
					"addOnPermissionsChangeListener",
					listener);
		} else {
			BinderTransaction.getInstance().invoke(Transacts.PERMISSION_MANAGER_SERVICE,
					Transacts.PERMISSION_MANAGER_DESCRIPTOR,
					"addOnPermissionsChangeListener",
					listener);
		}
	}

	@PermissionTest(permission = "OVERRIDE_WIFI_CONFIG")
	public void testOverrideWifiConfig() {
		BinderTransaction.getInstance().invoke(Transacts.WIFI_SERVICE, Transacts.WIFI_DESCRIPTOR,
				"getWifiApConfiguration");
	}

	@PermissionTest(permission = "PACKAGE_USAGE_STATS",developmentProtection = true)
	public void testPackageUsageStats() {
		ReflectionUtil.invoke(systemService(ActivityManager.class), "getPackageImportance",
				new Class<?>[]{String.class},
				mContext.getPackageName());
	}

	@PermissionTest(permission = "PACKAGE_VERIFICATION_AGENT")
	public void testPackageVerificationAgent() {
		mPackageManager.verifyPendingInstall(0, 0);
	}

	@PermissionTest(permission = "PACKET_KEEPALIVE_OFFLOAD")
	public void testPacketKeepaliveOffload() {
		BinderTransaction.getInstance().invoke(Transacts.CONNECTIVITY_SERVICE,
				Transacts.CONNECTIVITY_DESCRIPTOR,
				"startNattKeepalive",
				0, 0, 0, "127.0.0.1", 0, "127.0.0.1");
	}

	@PermissionTest(permission = "QUERY_DO_NOT_ASK_CREDENTIALS_ON_BOOT")
	public void testQueryDoNotAskCredentialsOnBoot() {
		BinderTransaction.getInstance().invoke(Transacts.DEVICE_POLICY_SERVICE,
				Transacts.DEVICE_POLICY_DESCRIPTOR,
				"getDoNotAskCredentialsOnBoot");
	}

	@PermissionTest(permission = "READ_BLOCKED_NUMBERS")
	public void testReadBlockedNumbers() {
		mContentResolver.query(BlockedNumberContract.BlockedNumbers.CONTENT_URI,
				null, null, null, null);
	}

	@PermissionTest(permission = "READ_DREAM_STATE")
	public void testReadDreamState() {
		BinderTransaction.getInstance().invoke(Transacts.DREAMS_SERVICE, Transacts.DREAMS_DESCRIPTOR,
				"isDreaming");
	}

	@PermissionTest(permission = "READ_FRAME_BUFFER")
	public void testReadFrameBuffer() {
		BinderTransaction.getInstance().invoke(Transacts.WINDOW_SERVICE, Transacts.WINDOW_DESCRIPTOR,
				"screenshotWallpaper");
	}

	@PermissionTest(permission = "READ_LOGS",developmentProtection = true)
	public void testReadLogs() {
		DropBoxManager manager = (DropBoxManager) mContext.getSystemService(
				Context.DROPBOX_SERVICE);
		manager.getNextEntry(null, System.currentTimeMillis());
	}

	@PermissionTest(permission = "READ_NETWORK_USAGE_HISTORY")
	public void testReadNetworkUsageHistory() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
			BinderTransaction.getInstance().invoke(Transacts.TELEPHONY_SERVICE,
					Transacts.TELEPHONY_DESCRIPTOR,
					"getVtDataUsage",
					0, true);
		} else {
			BinderTransaction.getInstance().invoke(Transacts.NETWORK_STATS_SERVICE,
					Transacts.NETWORK_STATS_DESCRIPTOR,
					"forceUpdate");
		}
	}

	@PermissionTest(permission = "READ_PRECISE_PHONE_STATE")
	public void testReadPrecisePhoneState() {
		if (Looper.myLooper() == null) {
			Looper.prepare();
		}
		systemService(TelephonyManager.class).listen(new PhoneStateListener(), 0x00000800);
	}

	@PermissionTest(permission = "READ_PRINT_SERVICE_RECOMMENDATIONS")
	public void testReadPrintServiceRecommendations() {

		ReflectionUtil.invoke(systemService(PrintManager.class), "getPrintServiceRecommendations");
	}

	@PermissionTest(permission = "READ_PRINT_SERVICES")
	public void testReadPrintServices() {
		ReflectionUtil.invoke(systemService(PrintManager.class), "getPrintServices",
				new Class<?>[]{int.class}, 3);
	}

	@PermissionTest(permission = "READ_PRIVILEGED_PHONE_STATE")
	public void testReadPrivilegedPhoneState() {
		ReflectionUtil.invoke(systemService(TelephonyManager.class), "getUiccSlotsInfo");
	}

	@PermissionTest(permission = "READ_SEARCH_INDEXABLES")
	public void testReadSearchIndexables() {
		try {
			Cursor cursor =
					mContentResolver.query(Uri.parse("content://com.android.settings/"), null, null,
							null, null);
			cursor.close();
		} catch (UnsupportedOperationException e) {
			//expected
		}
	}

	@PermissionTest(permission = "READ_WIFI_CREDENTIAL")
	public void testReadWifiCredential() {
		ReflectionUtil.invoke(systemService(WifiManager.class), "getPrivilegedConfiguredNetworks");
	}

	@PermissionTest(permission = "REBOOT")
	public void testReboot() {
		if (checkPermissionGranted(Manifest.permission.REBOOT) && !acceptDangerousApi ) {
			throw new BypassTestException(
					"Skipping this test to avoid rebooting the device and interrupting the "
							+ "rest of the tests");
		}
		BinderTransaction.getInstance().invoke(Transacts.POWER_SERVICE, Transacts.POWER_DESCRIPTOR,
				"reboot",
				false,
				"Test REBOOT permission", false);
	}

	@PermissionTest(permission = "REGISTER_SIM_SUBSCRIPTION")
	public void testRegisterSimSubscription() {
		PhoneAccountHandle handle = new PhoneAccountHandle(
				new ComponentName(mContext, TestService.class), "test_provider");
		PhoneAccount account = PhoneAccount.builder(
						handle, "ConnectionService")
				.setAddress(Uri.parse("tel:555-TEST2"))
				.setSubscriptionAddress(Uri.parse("tel:555-TEST2"))
				.setCapabilities(PhoneAccount.CAPABILITY_SIM_SUBSCRIPTION)
				.addSupportedUriScheme(PhoneAccount.SCHEME_TEL)
				.addSupportedUriScheme(PhoneAccount.SCHEME_VOICEMAIL)
				.build();
		systemService(TelecomManager.class).registerPhoneAccount(account);
	}

	@PermissionTest(permission = "REGISTER_WINDOW_MANAGER_LISTENERS")
	public void testRegisterWindowManagerListeners() {

		BinderTransaction.getInstance().invoke(Transacts.WINDOW_SERVICE, Transacts.WINDOW_DESCRIPTOR,
				"registerShortcutKey",
				1L, getActivityToken());

	}

	@PermissionTest(permission = "REMOTE_AUDIO_PLAYBACK")
	public void testRemoteAudioPlayback() {
		Parcel reply = BinderTransaction.getInstance().invoke(Transacts.AUDIO_SERVICE,
				Transacts.AUDIO_DESCRIPTOR,
				"getRingtonePlayer");
		IBinder player = reply.readStrongBinder();
		BinderTransaction.getInstance().invoke(Transacts.AUDIO_SERVICE, Transacts.AUDIO_DESCRIPTOR,
				"setRingtonePlayer",
				player);
	}

	@PermissionTest(permission = "REMOVE_TASKS")
	public void testRemoveTasks() {
		BinderTransaction.getInstance().invoke(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
				"appNotRespondingViaProvider",
				(IBinder) null);
	}

	@PermissionTest(permission = "RESET_FINGERPRINT_LOCKOUT")
	public void testResetFingerprintLockout() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
			BinderTransaction.getInstance().invoke(Transacts.FINGERPRINT_SERVICE,
					Transacts.FINGERPRINT_DESCRIPTOR,
					"resetTimeout");
		} else {
			// The byte array has 70 elements to avoid an IndexOutOfBoundException
			// in HardwareAuthTokenUtils#toHardwareAuthToken.
			BinderTransaction.getInstance().invoke(Transacts.FINGERPRINT_SERVICE,
					Transacts.FINGERPRINT_DESCRIPTOR,
					"resetLockout",
					getActivityToken(), 0, 0, new byte[70], mContext.getPackageName());

		}
	}

	@PermissionTest(permission = "RESET_SHORTCUT_MANAGER_THROTTLING")
	public void testResetShortcutManagerThrottling() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
			BinderTransaction.getInstance().invoke(Transacts.SHORTCUT_SERVICE,
					Transacts.SHORTCUT_DESCRIPTOR,
					"onApplicationActive",
					mContext.getPackageName(), appUid);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			Parcel reply = BinderTransaction.getInstance().invoke(Transacts.SHORTCUT_SERVICE,
					Transacts.SHORTCUT_DESCRIPTOR,
					"onApplicationActive",
					mContext.getPackageName(), appUid);
			// The first parameter in the reply is an int indicating whether or not
			// an AndroidFuture was written to the Parcel.
			if (reply.readInt() != 0) {
				// The first field in the Future is a boolean indicating whether the
				// Future completed.
				if (reply.readBoolean()) {
					// If the Future completed the second and third fields are booleans
					// indicating whether it completed with a Throwable.
					if (reply.readBoolean() && reply.readBoolean()) {
						// When the Future results in an Exception an additional boolean
						// is written indicating whether the exception is Parcelable.
						if (reply.readBoolean()) {
							Throwable throwable = reply.readParcelable(
									Parcelable.class.getClassLoader());
							logger.debug("AndroidFuture resulted in a Throwable",
									throwable);
							if (throwable instanceof SecurityException) {
								throw (SecurityException) throwable;
							}
						} else {
							logger.debug("The received Throwable is not parcelable");
							String className = reply.readString();
							String message = reply.readString();
							String stackTrace = reply.readString();
							if (className.contains("SecurityException")) {
								throw new SecurityException(message + ":" + stackTrace);
							} else {
								logger.debug(
										"Caught the following exception class: "
												+ className
												+ " with message: " + message
												+ " and stackTrace: " + stackTrace);
							}
						}
					} else {
						logger.debug("The Future completed without a Throwable");
					}
				} else {
					logger.debug("The future is still running");
				}
			} else {
				logger.debug(
						"An AndroidFuture was not written to the reply Parcel");
			}
		}
	}

	@PermissionTest(permission = "RESTRICTED_VR_ACCESS")
	public void testRestrictedVrAccess() {
		BinderTransaction.getInstance().invoke(Transacts.VR_SERVICE, Transacts.VR_DESCRIPTOR,
				"setPersistentVrModeEnabled",
				true);
	}

	@PermissionTest(permission = "RETRIEVE_WINDOW_CONTENT")
	public void testRetrieveWindowContent() {
		//Skip if permission is granted(dangerous permission)
		if (checkPermissionGranted("android.permission.RETRIEVE_WINDOW_CONTENT")
			&& !acceptDangerousApi) {
			throw new BypassTestException(
					"Skip if the target permission is granted.");
		}
		IBinder token = getActivityToken();
		AccessibilityServiceInfo serviceInfo = new AccessibilityServiceInfo();
		BinderTransaction.getInstance().invoke(Transacts.ACCESSIBILITY_SERVICE,
				Transacts.ACCESSIBILITY_DESCRIPTOR,
				"registerUiTestAutomationService",
				getActivityToken(), token,
				serviceInfo, 0);
	}

	@PermissionTest(permission = "RETRIEVE_WINDOW_TOKEN")
	public void testRetrieveWindowToken() {
		if (checkPermissionGranted("android.permission.RETRIEVE_WINDOW_TOKEN")
			&& !acceptDangerousApi) {
			throw new BypassTestException(
					"Skip if the target permission is granted.");
		}
		BinderTransaction.getInstance().invoke(Transacts.ACCESSIBILITY_SERVICE,
				Transacts.ACCESSIBILITY_DESCRIPTOR,
				"getWindowToken",
				-3, appUid);
	}

	@PermissionTest(permission = "REVOKE_RUNTIME_PERMISSIONS")
	public void testRevokeRuntimePermissions() {
		ReflectionUtil.invoke(mPackageManager, "revokeRuntimePermission",
				new Class<?>[]{String.class, String.class, UserHandle.class},
				Constants.COMPANION_PACKAGE, Manifest.permission.CAMERA,
				UserHandle.getUserHandleForUid(appUid));
	}

	@PermissionTest(permission = "SET_ACTIVITY_WATCHER")
	public void testSetActivityWatcher() {
		BinderTransaction.getInstance().invoke(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
				"performIdleMaintenance");
	}

	@PermissionTest(permission = "SET_ALWAYS_FINISH",developmentProtection = true)
	public void testSetAlwaysFinish() {
		BinderTransaction.getInstance().invoke(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
				"setAlwaysFinish",
				false);
	}

	@PermissionTest(permission = "SET_ANIMATION_SCALE",developmentProtection = true)
	public void testSetAnimationScale() {
		BinderTransaction.getInstance().invoke(Transacts.WINDOW_SERVICE, Transacts.WINDOW_DESCRIPTOR,
				"setAnimationScale",
				0, 0.1f);
	}

	@PermissionTest(permission = "SET_DEBUG_APP",developmentProtection = true)
	public void testSetDebugApp() {
		BinderTransaction.getInstance().invoke(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
				"setDumpHeapDebugLimit",
				mContext.getPackageName(), appUid,
				1073741824L, mContext.getPackageName());
	}

	@PermissionTest(permission = "SET_HARMFUL_APP_WARNINGS")
	public void testSetHarmfulAppWarnings() {
		// This test can potentially interrupt the test app; if so the test may need to
		// be bypassed when the permission is granted.
		BinderTransaction.getInstance().invoke(Transacts.PACKAGE_SERVICE, Transacts.PACKAGE_DESCRIPTOR,
				"getHarmfulAppWarning",
				Constants.COMPANION_PACKAGE, appUid);
	}

	@PermissionTest(permission = "SET_INPUT_CALIBRATION")
	public void testSetInputCalibration() {
		try {
			BinderTransaction.getInstance().invoke(Transacts.INPUT_SERVICE, Transacts.INPUT_DESCRIPTOR,
					"setTouchCalibrationForInputDevice",
					"test_device", 0, 0);
		} catch (NullPointerException ex){
			//logger.system(ex.getMessage());
			if(!ex.getMessage().startsWith("calibration must not be null")){
				throw ex;
			}
			//expected exception
		}
	}

	@PermissionTest(permission = "SET_KEYBOARD_LAYOUT",sdkMin = 34)
	public void testSetKeyboardLayout() {

		BinderTransaction.getInstance().invoke(Transacts.INPUT_SERVICE, Transacts.INPUT_DESCRIPTOR,
				"addKeyboardLayoutForInputDevice",
				0, "test_descriptor");

	}

	@PermissionTest(permission = "SET_MEDIA_KEY_LISTENER",developmentProtection = true)
	public void testSetMediaKeyListener() {
		BinderTransaction.getInstance().invoke(Transacts.MEDIA_SESSION_SERVICE,
				Transacts.MEDIA_SESSION_DESCRIPTOR,
				"setOnMediaKeyListener");
	}

	@PermissionTest(permission = "SET_ORIENTATION")
	public void testSetOrientation() {
		BinderTransaction.getInstance().invoke(Transacts.WINDOW_SERVICE, Transacts.WINDOW_DESCRIPTOR,
				"thawRotation");
	}

	@PermissionTest(permission = "SET_POINTER_SPEED")
	public void testSetPointerSpeed() {
		BinderTransaction.getInstance().invoke(Transacts.INPUT_SERVICE, Transacts.INPUT_DESCRIPTOR,
				"tryPointerSpeed", 7);
	}

	@PermissionTest(permission = "SET_PREFERRED_APPLICATIONS")
	public void testSetPreferredApplications() {
		// If this permission is granted then do not invoke the method; the permissions
		// will be revoked causing the test app to immediately exit.
		if (checkPermissionGranted(Manifest.permission.SET_PREFERRED_APPLICATIONS) && !acceptDangerousApi) {
			throw new BypassTestException(
					"Skipping test to prevent killing test app when permissions are "
							+ "revoked");
		}
		BinderTransaction.getInstance().invoke(Transacts.PACKAGE_SERVICE, Transacts.PACKAGE_DESCRIPTOR,
				"resetApplicationPreferences",
				0);
	}

	@PermissionTest(permission = "SET_SCREEN_COMPATIBILITY")
	public void testSetScreenCompatibility() {
		String service = "activity_task";
		String descriptor = Transacts.ACTIVITY_TASK_DESCRIPTOR;
		// The ActivityManagerService provided the screen compat actions in P.
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P) {
			service = Context.ACTIVITY_SERVICE;
			descriptor = Transacts.ACTIVITY_DESCRIPTOR;
		}
		Parcel reply = BinderTransaction.getInstance().invoke(service, descriptor,
				"getFrontActivityScreenCompatMode",
				20);
		int mode = reply.readInt();
		BinderTransaction.getInstance().invoke(service, descriptor,
				"setFrontActivityScreenCompatMode",
				mode);
	}

	@PermissionTest(permission = "SET_TIME")
	public void testSetTime() {
		BinderTransaction.getInstance().invoke(Transacts.ALARM_SERVICE, Transacts.ALARM_DESCRIPTOR,
				"setTime",
				System.currentTimeMillis());
	}

	@PermissionTest(permission = "SET_TIME_ZONE")
	public void testSetTimeZone() {
		String timeZone = TimeZone.getDefault().getID();
		BinderTransaction.getInstance().invoke(Transacts.ALARM_SERVICE, Transacts.ALARM_DESCRIPTOR,
				"setTimeZone",
				timeZone);
	}

	@PermissionTest(permission = "SET_VOLUME_KEY_LONG_PRESS_LISTENER",developmentProtection = true)
	public void testSetVolumeKeyLongPressListener() {
		BinderTransaction.getInstance().invoke(Transacts.MEDIA_SESSION_SERVICE,
				Transacts.MEDIA_SESSION_DESCRIPTOR,
				"setOnVolumeKeyLongPressListener",
				(IBinder) null);
	}

	@PermissionTest(permission = "SET_WALLPAPER_COMPONENT")
	public void testSetWallpaperComponent() {
		ComponentName testComp = new ComponentName(mContext, MainActivity.class);
		try {
			BinderTransaction.getInstance().invoke(
					Transacts.WALLPAPER_SERVICE, Transacts.WALLPAPER_DESCRIPTOR,
					"setWallpaperComponent", testComp);
		} catch (IllegalStateException ex) {
			//IllegalStateException raised if wallpaper component is not initiated.
			logger.debug("Test succeeded. But setWallpaperComponent failed due to " + ex.getMessage());
		}
	}

	@PermissionTest(permission="SHOW_KEYGUARD_MESSAGE")
	public void testShowKeyguardMessage(){
		IBinder token = getActivityToken();
		IKeyguardDismissCallback callback = new IKeyguardDismissCallback() {
			@Override
			public void onDismissError() {
				logger.debug("onDismissError called");
			}

			@Override
			public void onDismissSucceeded() {
				logger.debug("onDismissSucceed called");
			}

			@Override
			public void onDismissCancelled() {
				logger.debug("onDismissCancelled called");
			}

			@Override
			public IBinder asBinder() {
				return null;
			}
		};
		String service = Transacts.ACTIVITY_TASK_SERVICE;
		String descriptor = Transacts.ACTIVITY_TASK_DESCRIPTOR;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
			if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P) {
				service = Context.ACTIVITY_SERVICE;
				descriptor = Transacts.ACTIVITY_DESCRIPTOR;
			}
			BinderTransaction.getInstance().invokeCS(service, descriptor,
					"dismissKeyguard",
					true, token, callback,
					"Test SHOW_KEYGUARD_MESSAGE");
		} else {
			// In Android 12 this transact is now in the ActivityClientController which
			// must first be obtained through a call to ActivityTaskManager.
			Parcel reply = BinderTransaction.getInstance().invoke(Transacts.ACTIVITY_TASK_SERVICE,
					Transacts.ACTIVITY_TASK_DESCRIPTOR,
					"getActivityClientController");
			IBinder activityClientController = reply.readStrongBinder();
			BinderTransaction.getInstance().invokeIBinder(activityClientController,
					Transacts.ACTIVITY_CLIENT_DESCRIPTOR,
					"dismissKeyguard",
					true, token, callback, "Test SHOW_KEYGUARD_MESSAGE");
		}
	}

	@PermissionTest(permission="SHUTDOWN")
	public void testShutdown(){
		// only invoke the action if the permission is not granted to avoid interrupting the
		// test
		if (mContext.checkSelfPermission("android.permission.SHUTDOWN")
				== PackageManager.PERMISSION_GRANTED) {
			throw new BypassTestException(
					"This test will shutdown the device and interrupt the test app");
		}
		BinderTransaction.getInstance().invoke(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
				"shutdown",
				1000);
	}

	@PermissionTest(permission="SIGNAL_PERSISTENT_PROCESSES",developmentProtection = true)
	public void testSignalPersistentProcesses(){
		BinderTransaction.getInstance().invoke(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
				"signalPersistentProcesses",
				android.os.Process.SIGNAL_USR1);
	}

	@PermissionTest(permission="START_ANY_ACTIVITY")
	public void testStartAnyActivity(){

		if(Constants.BYPASS_TESTS_AFFECTING_UI)
			throw new BypassTestException("This test case affects to UI. skip to avoiding ui stuck.");

		ComponentName componentName;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
			componentName = new ComponentName("android",
					"com.android.internal.app.AccessibilityButtonChooserActivity");
		} else {
			componentName = new ComponentName("android",
					"com.android.internal.accessibility.dialog"
							+ ".AccessibilityButtonChooserActivity");
		}
		Intent intent = new Intent();
		intent.setComponent(componentName);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);

		//}

	}

	@PermissionTest(permission="START_TASKS_FROM_RECENTS")
	public void testStartTasksFromRecents(){
		try {
			BinderTransaction.getInstance().invoke(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
					"startActivityFromRecents",
					0, 0);
		} catch (IllegalArgumentException ex){
			//expected
		}
	}

	@PermissionTest(permission="STATUS_BAR")
	public void testStatusBar(){
		BinderTransaction.getInstance().invoke(Transacts.WINDOW_SERVICE, Transacts.WINDOW_DESCRIPTOR,
				"setRecentsVisibility",
				(IBinder) null);
	}

	@PermissionTest(permission="STATUS_BAR_SERVICE")
	public void testStatusBarService(){
		BinderTransaction.getInstance().invoke(Transacts.NOTIFICATION_SERVICE,
				Transacts.NOTIFICATION_DESCRIPTOR,
				"isNotificationPolicyAccessGrantedForPackage",
				"android");
	}

	@PermissionTest(permission="STOP_APP_SWITCHES")
	public void testStopAppSwitches(){
		BinderTransaction.getInstance().invoke(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
				"resumeAppSwitches");
	}

	@PermissionTest(permission="STORAGE_INTERNAL")
	public void testStorageInternal(){
		UUID storageUUID = null;
		try {
			storageUUID = systemService(StorageManager.class).getUuidForPath(Environment.getDataDirectory());
		} catch (IOException e) {
			throw new UnexpectedTestFailureException(e);
		}
		BinderTransaction.getInstance().invoke(Transacts.MOUNT_SERVICE, Transacts.MOUNT_DESCRIPTOR,
				"getCacheSizeBytes",
				storageUUID.toString(),
				1000);

	}

	@PermissionTest(permission="SUSPEND_APPS")
	public void testSuspendApps(){
		ReflectionUtil.invoke(mPackageManager.getClass(), "setPackagesSuspended",
				mPackageManager,
				new Class<?>[]{String[].class, boolean.class,
						PersistableBundle.class, PersistableBundle.class, String.class},
				new String[]{mContext.getPackageName()}, false, null, null,
				"Test SUSPEND_APPS");
	}

	@PermissionTest(permission="TABLET_MODE")
	public void testTabletMode(){
		BinderTransaction.getInstance().invoke(Transacts.INPUT_SERVICE, Transacts.INPUT_DESCRIPTOR,
				"isInTabletMode");
	}

	@PermissionTest(permission="TETHER_PRIVILEGED",sdkMax = 29)
	public void testTetherPrivileged(){
		BinderTransaction.getInstance().invoke(Transacts.CONNECTIVITY_SERVICE,
				Transacts.CONNECTIVITY_DESCRIPTOR,
				"tether",
				"wlan0",
				mContext.getPackageName());
	}

	@PermissionTest(permission="TRUST_LISTENER")
	public void testTrustListener(){
		BinderTransaction.getInstance().invoke(Transacts.TRUST_SERVICE, Transacts.TRUST_DESCRIPTOR,
				"unregisterTrustListener",
				getActivityToken());
	}

	@PermissionTest(permission="UPDATE_APP_OPS_STATS")
	public void testUpdateAppOpsStats(){
		BinderTransaction.getInstance().invoke(Transacts.APP_OPS_SERVICE,
				Transacts.APP_OPS_DESCRIPTOR,
				"noteOperation",
				0,
				"android", 1000);
	}

	@PermissionTest(permission="UPDATE_DEVICE_STATS")
	public void testUpdateDeviceStats(){

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			BinderTransaction.getInstance().invoke(Transacts.BATTERY_STATS_SERVICE,
					Transacts.BATTERY_STATS_DESCRIPTOR,
					"noteStartAudio",
					0);
		} else {
			BinderTransaction.getInstance().invoke(Transacts.BATTERY_STATS_SERVICE,
					Transacts.BATTERY_STATS_DESCRIPTOR,
					"noteStartAudio",
					0, "test", appUid);
		}

	}

	@PermissionTest(permission="UPDATE_LOCK_TASK_PACKAGES")
	public void testUpdateLockTaskPackages(){
		BinderTransaction.getInstance().invoke(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
				"updateLockTaskPackages",
				appUid, new String[]{mContext.getPackageName()});
	}

	@PermissionTest(permission="WRITE_APN_SETTINGS")
	public void testWriteApnSettings(){
		ContentValues contentValues = new ContentValues();
		contentValues.put(Telephony.Carriers.APN, "testApn");
		contentValues.put(Telephony.Carriers.NAME, "testName");
		contentValues.put(Telephony.Carriers.NUMERIC, "123456");
		contentValues.put(Telephony.Carriers.MCC, "123");
		contentValues.put(Telephony.Carriers.MNC, "456");
		mContentResolver.insert(Telephony.Carriers.CONTENT_URI, contentValues);
	}

	@PermissionTest(permission="WRITE_BLOCKED_NUMBERS")
	public void testWriteBlockedNumbers(){
		ContentValues contentValues = new ContentValues();
		contentValues.put(BlockedNumberContract.BlockedNumbers.COLUMN_ORIGINAL_NUMBER, "520-555-1234");
		mContentResolver.insert(BlockedNumberContract.BlockedNumbers.CONTENT_URI, contentValues);
	}

	@PermissionTest(permission="WRITE_DREAM_STATE")
	public void testWriteDreamState(){
		BinderTransaction.getInstance().invoke(Transacts.DREAMS_SERVICE, Transacts.DREAMS_DESCRIPTOR,
				"awaken");
	}

	@PermissionTest(permission="WRITE_EMBEDDED_SUBSCRIPTIONS",developmentProtection = true)
	public void testWriteEmbeddedSubscriptions(){

		if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
			BinderTransaction.getInstance().invoke(Transacts.EUICC_CONTROLLER_SERVICE,
					Transacts.EUICC_CONTROLLER_DESCRIPTOR,
					"getSupportedCountries",
					true);
		} else {
			BinderTransaction.getInstance().invoke(Transacts.ISUB_SERVICE, Transacts.ISUB_DESCRIPTOR,
					"requestEmbeddedSubscriptionInfoListRefresh",
					0);
		}
	}

	@PermissionTest(permission="WRITE_SECURE_SETTINGS",developmentProtection = true)
	public void testWriteSecureSettings(){
		Settings.Secure.putString(mContentResolver, "TEST_KEY", "TEST_VALUE");
	}

	@PermissionTest(permission="READ_VOICEMAIL")
	public void testReadVoicemail(){
		mContentResolver.query(CallLog.Calls.CONTENT_URI_WITH_VOICEMAIL, null, null, null, null);
	}

	@PermissionTest(permission="WRITE_VOICEMAIL")
	public void testWriteVoicemail(){
		ContentValues values = new ContentValues();
		values.put(VoicemailContract.Voicemails.SOURCE_PACKAGE, mContext.getPackageName());
		mContentResolver.insert(VoicemailContract.Voicemails.CONTENT_URI, values);
	}

	@PermissionTest(permission=BLUETOOTH_PRIVILEGED, sdkMin=28)
	public void testBluetoothPrivileged(){
		ReflectionUtil.invoke(mBluetoothAdapter,"clearBluetooth");
	}
}









