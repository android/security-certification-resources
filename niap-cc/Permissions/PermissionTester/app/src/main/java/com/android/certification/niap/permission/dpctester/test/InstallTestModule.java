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
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.StatusBarManager;
import android.app.WallpaperManager;
import android.app.admin.DevicePolicyManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.companion.AssociationRequest;
import android.companion.CompanionDeviceManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.VersionedPackage;
import android.content.res.Resources;

import android.graphics.Rect;
import android.hardware.ConsumerIrManager;
import android.hardware.biometrics.BiometricManager;
import android.hardware.fingerprint.FingerprintManager;
import android.media.AudioManager;
import android.media.quality.AmbientBacklightEvent;
import android.media.quality.MediaQualityManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.nfc.NfcAdapter;
import android.nfc.cardemulation.CardEmulation;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.security.advancedprotection.AdvancedProtectionManager;
import android.service.notification.StatusBarNotification;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.window.IScreenRecordingCallback;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CredentialOption;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.GetPublicKeyCredentialOption;
import androidx.credentials.exceptions.GetCredentialException;

import com.android.certification.niap.permission.dpctester.MainActivity;
import com.android.certification.niap.permission.dpctester.R;
import com.android.certification.niap.permission.dpctester.activity.TestActivity;
import com.android.certification.niap.permission.dpctester.common.Constants;
import com.android.certification.niap.permission.dpctester.common.ReflectionUtil;
import com.android.certification.niap.permission.dpctester.service.FgCameraService;
import com.android.certification.niap.permission.dpctester.service.FgConnectedDeviceService;
import com.android.certification.niap.permission.dpctester.service.FgDataSyncService;
import com.android.certification.niap.permission.dpctester.service.FgHealthService;
import com.android.certification.niap.permission.dpctester.service.FgLocationService;
import com.android.certification.niap.permission.dpctester.service.FgMediaPlaybackService;
import com.android.certification.niap.permission.dpctester.service.FgMediaProcessingService;
import com.android.certification.niap.permission.dpctester.service.FgMediaProjectionService;
import com.android.certification.niap.permission.dpctester.service.FgMicrophoneService;
import com.android.certification.niap.permission.dpctester.service.FgPhoneCallService;
import com.android.certification.niap.permission.dpctester.service.FgRemoteMessagingService;
import com.android.certification.niap.permission.dpctester.service.FgSpecialUseService;
import com.android.certification.niap.permission.dpctester.service.FgSystemExemptedService;
import com.android.certification.niap.permission.dpctester.service.TestJobService;
import com.android.certification.niap.permission.dpctester.service.TestService;
import com.android.certification.niap.permission.dpctester.test.exception.BypassTestException;
import com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException;
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestModuleBase;
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestRunner;
import com.android.certification.niap.permission.dpctester.test.tool.BinderTransaction;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTest;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestModule;
import com.android.certification.niap.permission.dpctester.test.tool.TesterUtils;
import com.android.certifications.niap.permissions.companion.services.TestBindService;

import static android.Manifest.permission.*;
import static android.app.admin.PolicyUpdateReceiver.EXTRA_PERMISSION_NAME;
import static android.hardware.usb.UsbManager.EXTRA_PERMISSION_GRANTED;
import static android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET;
import static android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

@PermissionTestModule(name="Install Test Cases",label = "Run Install Permission Test",sync = true)
public class InstallTestModule extends PermissionTestModuleBase {
	public InstallTestModule(@NonNull Activity activity){ super(activity);}

	//PermissionTest parameters for bypassing testcases
	//required managers : requiredManagers
	//required permissions : requiredPermissions
	//isAffectingUI (label)

	BluetoothAdapter mBluetoothAdapter;

	@NonNull
	@Override
	public PrepareInfo prepare(Consumer<PermissionTestRunner.Result> callback){

		try {
			mBluetoothAdapter = systemService(BluetoothManager.class).getAdapter();
		} catch (NullPointerException e) { /*Leave bluetoothAdapter as null, if manager isn't available*/ }
		return super.prepare(callback);
	}

	private <T> T systemService(Class<T> clazz){
		return Objects.requireNonNull(getService(clazz),"[npe_system_service]"+clazz.getSimpleName());
	}

	@PermissionTest(permission=ACCESS_NETWORK_STATE)
	public void testAccessNetworkState(){
		//
		systemService(ConnectivityManager.class).getActiveNetworkInfo();
	}

	@SuppressLint("MissingPermission")
    @PermissionTest(permission=ACCESS_WIFI_STATE,requiredPermissions = {ACCESS_FINE_LOCATION})
	public void testAccessWifiState(){
		systemService(WifiManager.class).getConfiguredNetworks();
	}

	@SuppressLint({"HardwareIds", "MissingPermission"})
    @PermissionTest(permission=BLUETOOTH, sdkMin=28, sdkMax=30 ,requiredPermissions = {BLUETOOTH_CONNECT})
	public void testBluetooth(){
		if (mBluetoothAdapter == null) {
			throw new BypassTestException(
					"A bluetooth adapter is not available to run this test");
		} else {
			mBluetoothAdapter.getAddress();
		}
	}
	@SuppressLint("MissingPermission")
	@PermissionTest(permission=BLUETOOTH_ADMIN, sdkMin=28, sdkMax=30)
	public void testBluetoothAdmin(){
		if (mBluetoothAdapter == null) {
			throw new BypassTestException(
					"A bluetooth adapter is not available to run this test");
		}
		if (mBluetoothAdapter.isEnabled()) {
			mBluetoothAdapter.disable();
			mBluetoothAdapter.enable();
		} else {
			mBluetoothAdapter.enable();
		}
	}
	@SuppressLint("MissingPermission")
	@PermissionTest(permission=BROADCAST_STICKY,sdkMin = 27,sdkMax = 28)
	public void testBroadcastSticky(){
		Intent intent = new Intent();
		mContext.sendStickyBroadcast(intent);
	}

	@PermissionTest(permission=CHANGE_NETWORK_STATE)
	public void testChangeNetworkState(){
		NetworkRequest networkRequest = new NetworkRequest.Builder().addCapability(
				NET_CAPABILITY_INTERNET).build();
		systemService(ConnectivityManager.class).requestNetwork(networkRequest, new ConnectivityManager.NetworkCallback() {
			@Override
			public void onAvailable(Network network) {
			}
		});
	}

	@PermissionTest(permission=CHANGE_WIFI_MULTICAST_STATE)
	public void testChangeWifiMulticastState(){
		WifiManager.MulticastLock multicastLock =
				systemService(WifiManager.class).createMulticastLock("CHANGE_WIFI_MULTICAST_TEST");
		multicastLock.acquire();
		multicastLock.release();
	}

	@PermissionTest(permission=CHANGE_WIFI_STATE)
	public void testChangeWifiState(){
		systemService(WifiManager.class).setWifiEnabled(true);
	}

	@SuppressLint("MissingPermission")
	@PermissionTest(permission=DISABLE_KEYGUARD)
	public void testDisableKeyguard(){
		KeyguardManager.KeyguardLock keyguardLock =
				systemService(KeyguardManager.class).newKeyguardLock("KEYGUARDLOCK_TEST");
		keyguardLock.disableKeyguard();
	}

	@RequiresApi(api = Build.VERSION_CODES.Q)
    @PermissionTest(permission=EXPAND_STATUS_BAR)
	public void testExpandStatusBar(){

		/*if(Constants.BYPASS_TESTS_AFFECTING_UI)
			throw new BypassTestException("This test case affects to UI. skip to avoiding ui stuck.");

		@SuppressLint("WrongConstant") Object statusBarManager = mContext.getSystemService("statusbar");
		*/

		StatusBarManager statusBarManager = systemService(StatusBarManager.class);
		try {
			ReflectionUtil.invoke(statusBarManager, "expandNotificationsPanel");
			// A short sleep is required to allow the notification panel to be expanded before
			// collapsing it to clean up after this test.
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				logger.debug("Caught an InterruptedException: ", e);
			}
			// Starting in Android 12 this API is no longer available without the signature
			// permission STATUS_BAR.
			if(Build.VERSION.SDK_INT < Build.VERSION_CODES.S){
				ReflectionUtil.invoke(statusBarManager, "collapsePanels");
			}
			//ReflectionUtil.invoke(statusBarManager, "expandNotificationsPanel");

		} catch (ReflectionUtil.ReflectionIsTemporaryException e) {
			throw new RuntimeException(e);
		}
	}


	@PermissionTest(permission=INTERNET)
	public void testInternet(){
		try {
			// Use a simple ServerSocket with a value of 0 to pick a free port to verify the
			// Internet permission.
			ServerSocket socket = new ServerSocket(0);
			socket.close();
		} catch (Throwable t) {
			// Sockets that require the Internet permission will not throw SecurityExceptions
			// but instead will throw SocketExceptions with a message containing EACCES
			// (Permission Denied).
			// NOTE: Later versions of the platform also return an EPERM error for this case.
			if (t instanceof SocketException && (t.getMessage().contains("EACCES")
					|| t.getMessage().contains("EPERM"))) {
				throw new SecurityException(t);
			} else {
				throw new UnexpectedTestFailureException(t);
			}
		}
	}

	@PermissionTest(permission=KILL_BACKGROUND_PROCESSES)
	public void testKillBackgroundProcesses(){
		systemService(ActivityManager.class).killBackgroundProcesses(Constants.COMPANION_PACKAGE);
	}

	@PermissionTest(permission=MODIFY_AUDIO_SETTINGS)
	public void testModifyAudioSettings(){
		// This API does not throw a SecurityException but instead just logs a permission denial
		// similar to the following in logcat:
		// AS.AudioService: Audio Settings Permission Denial: setMicrophoneMute() from
		//     pid=26925, uid=10250

		AudioManager mAudioManager = systemService(AudioManager.class);

		boolean micMuted = mAudioManager.isMicrophoneMute();
		mAudioManager.setMicrophoneMute(!micMuted);
		if (mAudioManager.isMicrophoneMute() == micMuted) {
			throw new SecurityException("mic mute status could not be changed");
		}
		// restore the mic mute status to the original setting
		mAudioManager.setMicrophoneMute(micMuted);
	}

	@PermissionTest(permission=MANAGE_OWN_CALLS)
	public void testManageOwnCalls(){
		TelecomManager telecomManager = systemService(TelecomManager.class);
		Uri numberUri = Uri.fromParts(PhoneAccount.SCHEME_TEL, "886", null);
		telecomManager.addNewIncomingCall(null, null);
		PhoneAccountHandle phoneAccountHandle = new PhoneAccountHandle(
				new ComponentName(mContext, MainActivity.class), "TestId");
		telecomManager.isIncomingCallPermitted(phoneAccountHandle);
	}

	@PermissionTest(permission=NFC)
	public void testNfc(){
		// SELinux blocks access to the NFC service from platform apps, so skip this test if the
		// app is platform signed.
		// SELinux : avc:  denied  { find } for service=nfc pid=24835 uid=10144
		//     scontext=u:r:platform_app:s0:c512,c768 tcontext=u:object_r:nfc_service:s0
		//     tclass=service_manager permissive=0
		// NFC     : could not retrieve NFC service
		NfcAdapter adapter = NfcAdapter.getDefaultAdapter(mContext);
		if (adapter == null) {
			throw new BypassTestException("A NFC adapter is not available to run this test");
		}
		//:TODO setNdefPushMesssage is obsolated?
		//adapter.setNdefPushMessage(null, mActivity);

		CardEmulation emulation = CardEmulation.getInstance(adapter);
		emulation.isDefaultServiceForCategory(new ComponentName(mContext, TestService.class),
				CardEmulation.CATEGORY_PAYMENT);
	}

	@PermissionTest(permission=READ_SYNC_SETTINGS)
	public void testReadSyncSettings(){
		ContentResolver.getMasterSyncAutomatically();
	}

	@PermissionTest(permission=READ_SYNC_STATS)
	public void testReadSyncStats(){
		ContentResolver.getCurrentSyncs();
	}

	@PermissionTest(permission=REORDER_TASKS)
	public void testReorderTasks(){
		systemService(ActivityManager.class).moveTaskToFront(2, 0);
	}

	@PermissionTest(permission=REQUEST_DELETE_PACKAGES)
	public void testRequestDeletePackages(){
		Intent intent = new Intent(mActivity, TestActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent,
				PendingIntent.FLAG_IMMUTABLE);
		// Use a version of this package that does not exist on the device to make this a
		// noop after the permission check.
		VersionedPackage versionedPackage = new VersionedPackage(mContext.getPackageName(), 0);
		mPackageManager.getPackageInstaller().uninstall(versionedPackage, pendingIntent.getIntentSender());
	}

	@PermissionTest(permission=SET_WALLPAPER)
	public void testSetWallpaper(){
		systemService(WallpaperManager.class).clearWallpaper();
	}

	@PermissionTest(permission=SET_WALLPAPER_HINTS)
	public void testSetWallpaperHints(){
		Rect rect = new Rect(0, 0, 1, 1);
		systemService(WallpaperManager.class).setDisplayPadding(rect);
	}

	@PermissionTest(permission=TRANSMIT_IR)
	public void testTransmitIr(){

		ConsumerIrManager irManager = systemService(ConsumerIrManager.class);
		if(irManager.hasIrEmitter()){
			irManager.getCarrierFrequencies();
		} else {
			throw new BypassTestException("This device device doesn't have ir emitter");
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.Q)
    @PermissionTest(permission=USE_BIOMETRIC,sdkMin = 29)
	public void testUseBiometricQ() {
		// The BiometricManager was introduced in Android 10 and is more appropriate to use
		// for this permission. Android 10 introduced the canAuthenticate method while
		// Android 11 deprecated that in favor of an overloaded version that accepts
		// an int representing Authenticators.
		BiometricManager biometricManager = systemService(BiometricManager.class);

		biometricManager = (BiometricManager) mContext.getSystemService(
				Context.BIOMETRIC_SERVICE);

		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
			biometricManager.canAuthenticate();
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
			biometricManager.canAuthenticate(
					BiometricManager.Authenticators.BIOMETRIC_STRONG);
		}
	}

	@Deprecated
	@PermissionTest(permission=USE_BIOMETRIC,sdkMax = 28)
	public void testUseBiometricLegacy(){
		systemService(FingerprintManager.class).isHardwareDetected();
	}

    @PermissionTest(permission=VIBRATE)
	public void testVibrate(){
		systemService(Vibrator.class)
				.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
	}

	@PermissionTest(permission=WAKE_LOCK)
	public void testWakeLock(){
		PowerManager.WakeLock wakeLock =
				systemService(PowerManager.class).newWakeLock(
					PowerManager.PARTIAL_WAKE_LOCK,
				InstallTestModule.class.getSimpleName()+"::InstallPermissionTester");
		wakeLock.acquire(10*60*1000L );///*10 minutes
		wakeLock.release();
	}

	@PermissionTest(permission=WRITE_SYNC_SETTINGS)
	public void testWriteSyncSettings(){
		ContentResolver.setMasterSyncAutomatically(true);
	}

	@RequiresApi(api = Build.VERSION_CODES.Q)
    @PermissionTest(permission=REQUEST_PASSWORD_COMPLEXITY, sdkMin=29)
	public void testRequestPasswordComplexity(){
		systemService(DevicePolicyManager.class).getPasswordComplexity();
	}

	@PermissionTest(permission=USE_FULL_SCREEN_INTENT, sdkMin=29, sdkMax=31)
	public void testUseFullScreenIntent(){

		Intent notificationIntent = new Intent(mContext, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
				notificationIntent, PendingIntent.FLAG_IMMUTABLE);

		Resources resources = mContext.getResources();
		CharSequence channelName = resources.getString(R.string.tester_channel_name);
		NotificationChannel channel =
				new NotificationChannel(InstallTestModule.class.getSimpleName(),
				channelName,
				NotificationManager.IMPORTANCE_DEFAULT);
		NotificationManager notificationManager =
				systemService(NotificationManager.class);

		notificationManager.createNotificationChannel(channel);

		Notification notification =
				new Notification.Builder(mContext, InstallTestModule.class.getSimpleName())
						.setContentTitle(resources.getText(
								R.string.full_screen_intent_notification_title))
						.setContentText(resources.getText(
								R.string.full_screen_intent_notification_message))
						.setSmallIcon(R.drawable.ic_launcher_foreground)
						.setContentIntent(pendingIntent)
						.setFullScreenIntent(pendingIntent, false)
						.build();
		notificationManager.notify(0, notification);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			logger.error("Caught an InterruptedException: " + e);
		}
		StatusBarNotification[] notifications =
				notificationManager.getActiveNotifications();

		if (notifications.length == 0) {
			throw new SecurityException(
					"fullScreenIntent not displayed as an active notification");
		}
		for (StatusBarNotification statusBarNotification : notifications) {
			if (statusBarNotification.getNotification().fullScreenIntent == null) {
				throw new SecurityException(
						"fullScreenIntent field cleared after launching notification");
			}
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.R)
    @PermissionTest(permission=NFC_PREFERRED_PAYMENT_INFO, sdkMin=30)
	public void testNfcPreferredPaymentInfo(){
		NfcAdapter adapter = NfcAdapter.getDefaultAdapter(mContext);
		if (adapter == null) {
			throw new BypassTestException(
					"An NFC adapter is not available to run this test");
		}
		CardEmulation cardEmulation = CardEmulation.getInstance(adapter);
		cardEmulation.getDescriptionForPreferredPaymentService();
	}

	@PermissionTest(permission=QUERY_ALL_PACKAGES, sdkMin=30)
	public void testQueryAllPackages(){
		try {
			//If the app is running with platform signature the test will be skipped.
			// 1. We can test this permission with normal variant.
			// 2. There is a test case which fails it it is declared.
			if(isPlatformSignatureMatch){
				final String msg = "The test for QUERY_ALL_PACKAGES permission is bypassed " +
						"when the app is signing with a platform signature." +
						"(see details in the process document)";
				throw new BypassTestException(msg);
			}
			// The companion package should be installed to act as a queryable package
			// for this test; without a <queries> tag in the AndroidManifest and without
			// this permission granted a query for the companion package should result
			// in a NameNotFoundException.
			PackageInfo packageInfo = mPackageManager.getPackageInfo(
					Constants.COMPANION_PACKAGE, 0);
		} catch (PackageManager.NameNotFoundException e) {
			throw new SecurityException(e);
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.S)
    @PermissionTest(permission=HIDE_OVERLAY_WINDOWS, sdkMin=31)
	public void testHideOverlayWindows(){

		// The API guarded by this permission must be run on the UI thread if the
		// permission is granted, but if the permission is not granted the resulting
		// SecurityException will crash the app. If the permission is not granted then
		// run the API here where the SecurityException can be handled, and if the
		// permission is granted then run it on the UI thread since an exception should
		// not be thrown in that case.

		if (mContext.checkSelfPermission(HIDE_OVERLAY_WINDOWS) != PackageManager.PERMISSION_GRANTED) {
			mActivity.getWindow().setHideOverlayWindows(true);
		} else {
			mActivity.runOnUiThread(
					() -> mActivity.getWindow().setHideOverlayWindows(true));
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.S)
	@PermissionTest(permission=REQUEST_COMPANION_PROFILE_WATCH, sdkMin=31)
	public void testRequestCompanionProfileWatch(){
		//commonize the tester routine with exposing the builder of AssociationRequest object
		CompletableFuture<AssociationRequest> associationRequest =
				new CompletableFuture<AssociationRequest>().completeAsync(() ->
						new AssociationRequest.Builder().setDeviceProfile(
								AssociationRequest.DEVICE_PROFILE_WATCH).build());
		TesterUtils.tryBluetoothAssociationRequest
				(mPackageManager, mActivity, associationRequest);

	}

	@RequiresApi(api = Build.VERSION_CODES.S)
	@PermissionTest(permission=REQUEST_OBSERVE_COMPANION_DEVICE_PRESENCE, sdkMin=31)
	public void testRequestObserveCompanionDevicePresence(){
		// Note: this could potentially be a fragile test since there is no companion
		// device associated with this app so when the permission is granted the call
		// results in a RuntimeException in the binder call, but during testing this
		// Exception was not thrown back to this test. If in a future release this test
		// fails because the Exception crosses the binder call then this test will need
		// to differentiate between a SecurityException and the RuntimeException.

		systemService(CompanionDeviceManager.class)
				.startObservingDevicePresence("11:22:33:44:55:66");

	}

	@PermissionTest(permission=SCHEDULE_EXACT_ALARM, sdkMin=31, sdkMax=33)
	public void testScheduleExactAlarm(){
		Intent intent = new Intent(mContext, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent,
				PendingIntent.FLAG_IMMUTABLE);
		AlarmManager alarmManager = systemService(AlarmManager.class);
		alarmManager.setExact(AlarmManager.RTC, System.currentTimeMillis() + 60 * 1000,
				pendingIntent);
		alarmManager.cancel(pendingIntent);
	}

	@SuppressLint("MissingPermission")
    @PermissionTest(permission=READ_BASIC_PHONE_STATE, sdkMin=33)
	public void testReadBasicPhoneState(){
		//Get Cellular network type
		TelephonyManager tm = systemService(TelephonyManager.class);
		tm.getDataNetworkType();
	}

	@PermissionTest(permission=USE_EXACT_ALARM, sdkMin=33)
	public void testUseExactAlarm(){
		// USE_EXACT_ALARM is an install permission, and that is only
		// differenceies against the SCHEDULE_EXACT_ALRAM permission.
		Intent intent = new Intent(mContext, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent,
				PendingIntent.FLAG_IMMUTABLE);
		AlarmManager alarmManager = mContext.getSystemService(AlarmManager.class);
		alarmManager.setExact(AlarmManager.RTC, System.currentTimeMillis() + 60 * 1000,
				pendingIntent);
		alarmManager.cancel(pendingIntent);
	}

	@PermissionTest(permission=READ_NEARBY_STREAMING_POLICY, sdkMin=33)
	public void testReadNearbyStreamingPolicy(){
		//This permission's category has been moved to install permission after Android T
		if (!mPackageManager.hasSystemFeature(PackageManager.FEATURE_DEVICE_ADMIN)) {
			throw new BypassTestException("This permission requires feature "
					+ PackageManager.FEATURE_DEVICE_ADMIN);
		}

		BinderTransaction.getInstance().invoke(
				Context.DEVICE_POLICY_SERVICE,
				Transacts.DEVICE_POLICY_DESCRIPTOR,
				"getNearbyNotificationStreamingPolicy",0
		);
//      Original Call
//		mTransacts.invokeTransact(Transacts.DEVICE_POLICY_SERVICE,
//				Transacts.DEVICE_POLICY_DESCRIPTOR,
//				Transacts.getNearbyNotificationStreamingPolicy, 0);
	}

	@RequiresApi(api = Build.VERSION_CODES.S)
    @PermissionTest(permission=REQUEST_COMPANION_PROFILE_GLASSES, sdkMin=34)
	public void testRequestCompanionProfileGlasses(){
		//commonize the tester routine with exposing the builder of AssociationRequest object
		CompletableFuture<AssociationRequest> associationRequest =
				new CompletableFuture<AssociationRequest>().completeAsync(() -> {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
						return new AssociationRequest.Builder()
								.setDeviceProfile(AssociationRequest.DEVICE_PROFILE_GLASSES).build();
					} else {
						return null;
					}
				});
		TesterUtils.tryBluetoothAssociationRequest
				(mPackageManager,mActivity, associationRequest);

	}

	@RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @PermissionTest(permission=RUN_USER_INITIATED_JOBS, sdkMin=34)
	public void testRunUserInitiatedJobs(){

		NetworkRequest nreq = new NetworkRequest.Builder()
				.addCapability(NET_CAPABILITY_INTERNET)
				.addCapability(NET_CAPABILITY_VALIDATED).build();
		ComponentName componentName =
				new ComponentName(mContext, TestJobService.class);
		JobInfo jobInfo = new JobInfo.Builder(1001,componentName)
				.setUserInitiated(true)
				.setRequiredNetwork(nreq)
				.setEstimatedNetworkBytes(1024 * 1024,1024 * 1024)
				.build();
		JobScheduler jobScheduler = (JobScheduler)
				mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE);
		jobScheduler.schedule(jobInfo);

	}

	@RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @PermissionTest(permission=DETECT_SCREEN_CAPTURE, sdkMin=34)
	public void testDetectScreenCapture(){
		CountDownLatch latch = new CountDownLatch(1);
		final Activity.ScreenCaptureCallback cb
				= new Activity.ScreenCaptureCallback() {
			@Override
			public void onScreenCaptured() {
				// Add logic to take action in your app.
				latch.countDown();
				//logger.debug("screen captured");
			}
		};
		try {
			mActivity.registerScreenCaptureCallback(mExecutor, cb);
			//Screenshot.capture();//androidx.test.runner was deprecated?
			runShellCommand("input keyevent 120");
			try {
				latch.await(2, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				throw new SecurityException("Failed to detect screen capture in time");
			}
		} finally {
			mActivity.unregisterScreenCaptureCallback(cb);//close it anyway
		}

	}
	/**
	 * Invokes and logs the stdout / stderr of the provided shell {@code command}, returning the
	 * exit code from the command.
	 */
	protected int runShellCommand(String command) {
		try {
			logger.debug("Attempting to run command " + command);
			java.lang.Process process = Runtime.getRuntime().exec(command);
			int returnCode = process.waitFor();
			BufferedReader stdout = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			BufferedReader stderr = new BufferedReader(
					new InputStreamReader(process.getErrorStream()));
			StringBuilder stdoutBuilder = new StringBuilder();
			String line;
			while ((line = stdout.readLine()) != null) {
				stdoutBuilder.append(line + "\n");
			}

			StringBuilder stderrBuilder = new StringBuilder();
			while ((line = stderr.readLine()) != null) {
				stderrBuilder.append(line + "\n");
			}
			logger.debug("Process return code: " + returnCode);
			logger.debug("Process stdout: " + stdoutBuilder.toString());
			logger.debug("Process stderr: " + stderrBuilder.toString());
			return returnCode;
		} catch (Throwable e) {
			throw new UnexpectedTestFailureException(e);
		}
	}

	//Helper
	private static class LocalDirectExecutor implements Executor {
		public void execute(Runnable r) {
			r.run();
		}
	}

	@PermissionTest(permission=CREDENTIAL_MANAGER_SET_ORIGIN, sdkMin=34)
	public void testCredentialManagerSetOrigin(){

		CredentialManager credentialManager = CredentialManager.create(mContext);
		//credentialManager.getCredential()
		List<CredentialOption> options = new ArrayList<>();
		options.add(new GetPublicKeyCredentialOption("{}"));
		GetCredentialRequest credentialRequest = new GetCredentialRequest.Builder()
				.setCredentialOptions(options)
				//Check : use setOrigin method
				.setOrigin("hoge")
				.build();

		credentialManager.getCredentialAsync(mContext,
				credentialRequest,
				null,
                new LocalDirectExecutor(),
				new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
					@Override
					public void onResult(GetCredentialResponse getCredentialResponse) {
					}
					@Override
					public void onError(@NonNull GetCredentialException e) {
					}
				}
		);
	}

	@PermissionTest(permission=CREDENTIAL_MANAGER_SET_ALLOWED_PROVIDERS, sdkMin=34)
	public void testCredentialManagerSetAllowedProviders(){
		CredentialManager credentialManager = CredentialManager.create(mContext);
		//credentialManager.getCredential()
		List<CredentialOption> options = new ArrayList<>();
		Set<ComponentName> componentNames = new HashSet<>();
		componentNames.add(new ComponentName("package","cls"));
		//Check : add allowed provider to the option.
		options.add(new GetPublicKeyCredentialOption("{}",null, componentNames));
		GetCredentialRequest credentialRequest = new GetCredentialRequest.Builder()
				.setCredentialOptions(options)
				.build();
		credentialManager.getCredentialAsync(mContext,
				credentialRequest,
				null,
                new LocalDirectExecutor(),
				new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
					@Override
					public void onResult(GetCredentialResponse getCredentialResponse) {
					}
					@Override
					public void onError(@NonNull GetCredentialException e) {
					}
				}
		);
	}

	@PermissionTest(permission=DETECT_SCREEN_RECORDING, sdkMin=34)
	public void testDetectScreenRecording(){
		//IScreenCallback Constructor might be hidden
		//If the error message is shown please execute below command from adb
		//adb shell settings put global hidden_api_policy  1

		IScreenRecordingCallback callback = new IScreenRecordingCallback() {
			@Override
			public IBinder asBinder() {
				return getActivityToken();
			}
			@Override
			public void onScreenRecordingStateChanged(boolean visibleInScreenRecording) throws RemoteException {
				System.out.println("visibleInScreenRecording:"+visibleInScreenRecording);
			}
		};
		//OK
		/*mTransacts.invokeTransact(Transacts.WINDOW_SERVICE,
				Transacts.WINDOW_DESCRIPTOR,
				Transacts.registerScreenRecordingCallback, callback);

		 */
		BinderTransaction.getInstance().invoke(
				Context.WINDOW_SERVICE,
				Transacts.WINDOW_DESCRIPTOR,
				"registerScreenRecordingCallback",callback
		);
	}

//	@PermissionTest(permission=ACCESS_HIDDEN_PROFILES, sdkMin=34,sdkMax = 34)
//	public void testAccessHiddenProfiles(){
//		//We can't access space setting intent by normal signature as of sdk35.
//		//So we exec same test on signature permission tester. Ignore this result.
//		LauncherApps launcherApps = systemService(LauncherApps.class);
//		// If the caller cannot access hidden profiles the method returns null
//		// see also. areHiddenApisChecksEnabled() in LauncherAppService
//
//        Object intent = null;
//        try {
//            intent = ReflectionUtil.invoke(launcherApps,"getPrivateSpaceSettingsIntent");
//        } catch (ReflectionUtil.ReflectionIsTemporaryException e) {
//            throw new RuntimeException(e);
//        }
//
//        if(intent == null){
//			throw new SecurityException("Caller cannot access hidden profiles");
//		}
//	}

	////////////////////////////////////////////////////////////////////////////////
	//Fore-ground Service Test Cases
	@PermissionTest(permission=FOREGROUND_SERVICE, sdkMin=28, sdkMax=33, customCase=true)
	public void testForegroundService(){
		// This test must run as a custom test because it requires a separate service be run in
		// in the foreground that can invoke startForeground.
		String permission = FOREGROUND_SERVICE;
		boolean permissionGranted =
				mContext.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
		try {
			Intent serviceIntent = new Intent(mContext, TestService.class);
			serviceIntent.putExtra(EXTRA_PERMISSION_NAME, permission);
			serviceIntent.putExtra(EXTRA_PERMISSION_GRANTED, permissionGranted);
			mContext.startForegroundService(serviceIntent);
		} catch (Throwable t) {
			logger.error(permission, t);
			throw t;
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.Q)
    @PermissionTest(permission=FOREGROUND_SERVICE_CAMERA, sdkMin=34, customCase=true)
	public void testForegroundServiceCamera(){
		Intent serviceIntent = new Intent(mActivity, FgCameraService.class);
		try {
			mActivity.startForegroundService(serviceIntent);
			tryBindingForegroundService(serviceIntent);
		} catch(Throwable t){
			logger.debug("FOREGROUND_SERVICE_CAMERA", t);
			throw t;
		}
	}
	@RequiresApi(api = Build.VERSION_CODES.Q)
	@PermissionTest(permission=FOREGROUND_SERVICE_LOCATION, sdkMin=34, customCase=true)
	public void testForegroundServiceLocation(){
		Intent serviceIntent = new Intent(mActivity, FgLocationService.class);
		try {
			mActivity.startForegroundService(serviceIntent);
			tryBindingForegroundService(serviceIntent);
		} catch(Throwable t){
			logger.debug("FOREGROUND_SERVICE_LOCATION", t);
			throw t;
		}
	}
	@RequiresApi(api = Build.VERSION_CODES.Q)
	@PermissionTest(permission=FOREGROUND_SERVICE_MICROPHONE, sdkMin=34, customCase=true)
	public void testForegroundServiceMicrophone(){
		Intent serviceIntent = new Intent(mActivity, FgMicrophoneService.class);
		try {
			mActivity.startForegroundService(serviceIntent);
			tryBindingForegroundService(serviceIntent);
		} catch(Throwable t){
			logger.debug("FOREGROUND_SERVICE_MICROPHONE", t);
			throw t;
		}
	}
	@RequiresApi(api = Build.VERSION_CODES.Q)
	@PermissionTest(permission=FOREGROUND_SERVICE_CONNECTED_DEVICE, sdkMin=34, customCase=true)
	public void testForegroundServiceConnectedDevice(){
		Intent serviceIntent = new Intent(mActivity, FgConnectedDeviceService.class);
		try {
			mActivity.startForegroundService(serviceIntent);
			tryBindingForegroundService(serviceIntent);
		} catch(Throwable t){
			logger.debug("FOREGROUND_SERVICE_CONNECTED_DEVICE", t);
			throw t;
		}
	}
	@RequiresApi(api = Build.VERSION_CODES.Q)
	@PermissionTest(permission=FOREGROUND_SERVICE_DATA_SYNC, sdkMin=34, customCase=true)
	public void testForegroundServiceDataSync(){
	Intent serviceIntent = new Intent(mActivity, FgDataSyncService.class);

		mActivity.startForegroundService(serviceIntent);
		tryBindingForegroundService(serviceIntent);
	}
	@RequiresApi(api = Build.VERSION_CODES.Q)
	@PermissionTest(permission=FOREGROUND_SERVICE_HEALTH, sdkMin=34, customCase=true)
	public void testForegroundServiceHealth(){
		Intent serviceIntent = new Intent(mActivity, FgHealthService.class);
		mActivity.startForegroundService(serviceIntent);
		tryBindingForegroundService(serviceIntent);
	}

	@RequiresApi(api = Build.VERSION_CODES.Q)
	@PermissionTest(permission=FOREGROUND_SERVICE_MEDIA_PLAYBACK, sdkMin=34, customCase=true)
	public void testForegroundServiceMediaPlayback(){
		Intent serviceIntent = new Intent(mActivity, FgMediaPlaybackService.class);
		mActivity.startForegroundService(serviceIntent);
		tryBindingForegroundService(serviceIntent);
	}

	@RequiresApi(api = Build.VERSION_CODES.Q)
	@PermissionTest(permission=FOREGROUND_SERVICE_MEDIA_PROJECTION, sdkMin=34, customCase=true)
	public void testForegroundServiceMediaProjection(){
		Intent serviceIntent = new Intent(mActivity, FgMediaProjectionService.class);
		try {
			mActivity.startForegroundService(serviceIntent);
			tryBindingForegroundService(serviceIntent);
		} catch(Throwable t){
			logger.debug("FOREGROUND_SERVICE_MEDIA_PROJECTION", t);
			throw t;
		}
	}
	@RequiresApi(api = Build.VERSION_CODES.Q)
	@PermissionTest(permission=FOREGROUND_SERVICE_PHONE_CALL, sdkMin=34, customCase=true)
	public void testForegroundServicePhoneCall(){
		Intent serviceIntent = new Intent(mActivity, FgPhoneCallService.class);
		try {
			mActivity.startForegroundService(serviceIntent);
			tryBindingForegroundService(serviceIntent);
		} catch(Throwable t){
			logger.debug("FOREGROUND_SERVICE_PHONE_CALL", t);
			throw t;
		}
	}
	@RequiresApi(api = Build.VERSION_CODES.Q)
	@PermissionTest(permission=FOREGROUND_SERVICE_REMOTE_MESSAGING, sdkMin=34, customCase=true)
	public void testForegroundServiceRemoteMessaging(){
		Intent serviceIntent = new Intent(mActivity, FgRemoteMessagingService.class);
		try {
			mActivity.startForegroundService(serviceIntent);
			tryBindingForegroundService(serviceIntent);
		} catch(Throwable t){
			logger.debug("FOREGROUND_SERVICE_REMOTE_MESSAGING", t);
			throw t;
		}
	}
	@RequiresApi(api = Build.VERSION_CODES.Q)
	@PermissionTest(permission=FOREGROUND_SERVICE_SPECIAL_USE, sdkMin=34, customCase=true)
	public void testForegroundServiceSpecialUse(){
		Intent serviceIntent = new Intent(mActivity, FgSpecialUseService.class);
		try {
			mActivity.startForegroundService(serviceIntent);
			tryBindingForegroundService(serviceIntent);
		} catch(Throwable t){
			logger.debug("FOREGROUND_SERVICE_SPECIAL_USE", t);
			throw t;
		}
	}
	@RequiresApi(api = Build.VERSION_CODES.Q)
	@PermissionTest(permission=FOREGROUND_SERVICE_SYSTEM_EXEMPTED, sdkMin=34, customCase=true)
	public void testForegroundServiceSystemExempted(){
		Intent serviceIntent = new Intent(mActivity, FgSystemExemptedService.class);
		try {
			mActivity.startForegroundService(serviceIntent);
			tryBindingForegroundService(serviceIntent);
		} catch(Throwable t){
			logger.debug("FOREGROUND_SERVICE_SYSTEM_EXEMPTED", t);
			throw t;
		}
	}
	@RequiresApi(api = Build.VERSION_CODES.Q)
	@PermissionTest(permission=FOREGROUND_SERVICE_MEDIA_PROCESSING, sdkMin=34, customCase=true)
	public void testForegroundServiceMediaProcessing(){
		///New Foreground Service Permission
		Intent serviceIntent = new Intent(mActivity, FgMediaProcessingService.class);
		try {
			mActivity.startForegroundService(serviceIntent);
			tryBindingForegroundService(serviceIntent);
		} catch(Throwable t){
			logger.debug("FOREGROUND_SERVICE_MEDIA_PROCESSING", t);
			throw t;
		}
	}

	//**** method template for target install SDK36
	@PermissionTest(permission="APPLY_PICTURE_PROFILE",sdkMin=36)
	public void testApplyPictureProfile(){
		//the permission requires android.media.tv.flags.apply_picture_profiles to run
		//or leanback?
		if(!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_LEANBACK)){
			throw new BypassTestException(
					"This permission requires feature "
							+ PackageManager.FEATURE_LEANBACK);
		}
		logger.debug("The test for android.permission.APPLY_PICTURE_PROFILE is not implemented yet");
	}

	//@RequiresApi(36)
	@PermissionTest(permission="READ_COLOR_ZONES",sdkMin=36)
	public void testReadColorZones(){
		//For the leanback devices (control backlight of the televison)
		//if we use phone device MediaQualityManager should be null
		systemService(MediaQualityManager.class).registerAmbientBacklightCallback(mExecutor,
				new MediaQualityManager.AmbientBacklightCallback(){
					@Override
					public void onAmbientBacklightEvent(@NonNull AmbientBacklightEvent ambientBacklightEvent) {
						//logger.system("ambient callback"+ambientBacklightEvent.toString());
					}
				});
	}
	@PermissionTest(permission="QUERY_ADVANCED_PROTECTION_MODE",sdkMin=36)
	public void testQueryAdvancedProtectionMode(){
		systemService(AdvancedProtectionManager.class).isAdvancedProtectionEnabled();
	}
	@PermissionTest(permission="TV_IMPLICIT_ENTER_PIP",sdkMin=36)
	public void testTvImplicitEnterPip(){
		if(!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_LEANBACK)){
			throw new BypassTestException(
					"This permission requires feature "
							+ PackageManager.FEATURE_LEANBACK);
		}
		//PIP = Picture in Picture, it uses on the leanback devcies
		logger.debug("The test for android.permission.TV_IMPLICIT_ENTER_PIP is not implemented yet");
	}
	@PermissionTest(permission="XR_TRACKING_IN_BACKGROUND",sdkMin=36)
	public void testXrTrackingInBackground(){
		//For Android XR Device
		if(!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_VR_MODE_HIGH_PERFORMANCE)){
			throw new BypassTestException(
					"This permission requires feature "
							+ PackageManager.FEATURE_VR_MODE_HIGH_PERFORMANCE);
		}
		logger.debug("The test for android.permission.XR_TRACKING_IN_BACKGROUND is not implemented yet");
	}


	@RequiresApi(api = Build.VERSION_CODES.Q)
    public void tryBindingForegroundService(Intent serviceIntent){
		FgServiceConnection serviceConnection = new FgServiceConnection();

		mContext.bindService(serviceIntent,
				Context.BIND_AUTO_CREATE, mExecutor,serviceConnection);
		synchronized (lock) {
			try {
				int i=0;

				while (!serviceConnection.mConnected.get()) {
					try {
						//wait almost 1 sec along increasing waiting time
						lock.wait(10+(i*i));
						//final int n = i;

						if(i++>=40){
							throw new InterruptedException("Connection Timed Out");
						}
					} catch (InterruptedException e) {
						throw new UnexpectedTestFailureException(e);
					}
				}
				logger.info("Connected To Foreground Service in the Tester app="+serviceConnection.mComponentName+
						","+serviceConnection.binderSuccess.get());
				if(!serviceConnection.binderSuccess.get()){
					throw new SecurityException("Test for "+serviceConnection.mComponentName+" has been failed.");
				}
			} catch (Exception ex){
				throw new UnexpectedTestFailureException(ex);
			} finally {
				mContext.unbindService(serviceConnection);
			}
		}
	}

	final Object lock = new Object();
	private class FgServiceConnection implements android.content.ServiceConnection {
		public final AtomicBoolean binderSuccess = new AtomicBoolean();
		private final AtomicBoolean mConnected = new AtomicBoolean(false);
		public String mComponentName = "";
		public void onServiceConnected(ComponentName name, IBinder binder) {
			synchronized (lock) {
				mConnected.set(true);
				binderSuccess.set(false);
				mComponentName = name.getShortClassName();
				TestBindService service = TestBindService.Stub.asInterface(binder);
				//service.testMethod();
				try {
					service.testMethod();
					binderSuccess.set(true);
				} catch (RemoteException e) {
					binderSuccess.set(false);
					logger.error(name+" failure."+e.getMessage(),e);
				}
				lock.notify();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			//Unimplemented
		}
	}
}









