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
import static android.Manifest.permission.READ_PHONE_STATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.app.role.IOnRoleHoldersChangedListener;
import android.app.role.RoleManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcel;
import android.os.PowerManager;
import android.os.UserHandle;
import android.telephony.CellInfo;
import android.telephony.NetworkScanRequest;
import android.telephony.TelephonyManager;
import android.telephony.TelephonyScanManager;
import android.view.accessibility.AccessibilityManager;
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
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTest;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestModule;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

@PermissionTestModule(name="Signature 29(Q) Test Cases",prflabel = "Q(10)")
public class SignatureTestModuleQ extends SignaturePermissionTestModuleBase {
	public SignatureTestModuleQ(@NonNull Activity activity) {
		super(activity);
	}

	private <T> T systemService(Class<T> clazz){
		return Objects.requireNonNull(getService(clazz),"[npe_system_service]"+clazz.getSimpleName());
	}



	@PermissionTest(permission="INSTALL_EXISTING_PACKAGES", sdkMin=29)
	public void testInstallExistingPackages(){
		// installExistingPackageAsUser - checks both INSTALL_PACKAGES and
		// INSTALL_EXISTING_PACKAGES, but SecurityException only reports
		// INSTALL_PACKAGES.
		// deprecated since api 36?
		BinderTransaction.getInstance().invoke(Transacts.PACKAGE_SERVICE, Transacts.PACKAGE_DESCRIPTOR,
				"installExistingPackageAsUser", Constants.COMPANION_PACKAGE,
				appUid, 0, 0);
	}

	@PermissionTest(permission="MANAGE_APPOPS", sdkMin=29,sdkMax = 29)
	public void testManageAppops(){
		ReflectionUtil.invoke(systemService(AppOpsManager.class), "clearHistory");
	}

	@PermissionTest(permission="MANAGE_TEST_NETWORKS", sdkMin=29)
	public void testManageTestNetworks(){
		@SuppressLint("WrongConstant") Object test_network = mContext.getSystemService("test_network");
		//ToDO:it is not working test
	}

	@PermissionTest(permission="GRANT_PROFILE_OWNER_DEVICE_IDS_ACCESS", sdkMin=29, sdkMax=29)
	public void testGrantProfileOwnerDeviceIdsAccess(){
		ComponentName componentName = new ComponentName(mContext, MainActivity.class);
		// SecurityException message indicates the failure is due to the process not
		// being run with the system UID, but a check is performed for this permission
		// as well.
		ReflectionUtil.invoke(systemService(DevicePolicyManager.class),
				"setProfileOwnerCanAccessDeviceIds",
				new Class[]{ComponentName.class}, componentName);
	}

	@PermissionTest(permission="CONTROL_ALWAYS_ON_VPN", sdkMin=29)
	public void testControlAlwaysOnVpn(){
		//TODO: need to write a variant function
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
			BinderTransaction.getInstance().invoke(Transacts.CONNECTIVITY_SERVICE,
					Transacts.CONNECTIVITY_DESCRIPTOR,
					"getAlwaysOnVpnPackage", appUid);
		} else {
			BinderTransaction.getInstance().invoke(Transacts.VPN_SERVICE, Transacts.VPN_DESCRIPTOR,
					"getAlwaysOnVpnPackage", appUid);
		}
	}

	@PermissionTest(permission="CONTROL_KEYGUARD_SECURE_NOTIFICATIONS", sdkMin=29)
	public void testControlKeyguardSecureNotifications(){
		ReflectionUtil.invoke(systemService(KeyguardManager.class),
				"getPrivateNotificationsAllowed");
	}

	@PermissionTest(permission="ACCESS_SHARED_LIBRARIES", sdkMin=29)
	public void testAccessSharedLibraries(){
		ReflectionUtil.invoke(mPackageManager ,"getDeclaredSharedLibraries",
				 new Class[]{String.class, int.class}, "android", 0);
	}

	@RequiresApi(api = Build.VERSION_CODES.R)
    @PermissionTest(permission="MONITOR_INPUT", sdkMin=29)
	public void testMonitorInput(){
		try {
			BinderTransaction.getInstance().invoke(Transacts.INPUT_SERVICE, Transacts.INPUT_DESCRIPTOR,
					"monitorGestureInput", "test", mContext.getDisplay().getDisplayId());
		} catch (NullPointerException ignored){
			logger.debug("Token(Binder) check executed after permission check, and it causes npx. it's intended.");
		}
	}

	@PermissionTest(permission="POWER_SAVER", sdkMin=29)
	public void testPowerSaver(){
		// Starting in Android 12 this permission is no longer required to get the
		// battery saver control mode.
		//TODO: need to write a variant function
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
			ReflectionUtil.invoke(systemService(PowerManager.class), "getPowerSaveModeTrigger");
		} else {
			BinderTransaction.getInstance().invoke(Transacts.POWER_SERVICE,
					Transacts.POWER_DESCRIPTOR, "setDynamicPowerSaveHint",
					false, 80);
		}
	}

	@PermissionTest(permission="LOCK_DEVICE", sdkMin=29, sdkMax=34)
	public void testLockDevice(){

		if(Constants.BYPASS_TESTS_AFFECTING_UI)
			throw new BypassTestException("This test case affects to UI. skip to avoiding ui stuck.");

		systemService(DevicePolicyManager.class).lockNow();
	}

	@SuppressLint("MissingPermission")
    @PermissionTest(permission="NETWORK_SCAN", sdkMin=29)
	public void testNetworkScan(){
		// Starting in Android 12 attempting a network scan with both this permission
		// as well as a location permission can cause a RuntimeException.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			if (checkPermissionGranted("android.permission.NETWORK_SCAN")
					&& (checkPermissionGranted(ACCESS_COARSE_LOCATION)
					|| checkPermissionGranted(ACCESS_FINE_LOCATION))) {
				throw new BypassTestException("This test should only run when the "
						+ "location permissions are not granted");
			}
		}
		NetworkScanRequest request = new NetworkScanRequest(
				NetworkScanRequest.SCAN_TYPE_ONE_SHOT, null, 5, 60, true, 5, null);
		TelephonyScanManager.NetworkScanCallback callback =
				new TelephonyScanManager.NetworkScanCallback() {
					@Override
					public void onResults(List<CellInfo> results) {
						logger.debug("onResults: " + results);
					}

					@Override
					public void onComplete() {
						logger.debug("onComplete");
					}

					@Override
					public void onError(int error) {
						logger.debug("onError: " + error);
					}
				};
		systemService(TelephonyManager.class).requestNetworkScan(request, AsyncTask.SERIAL_EXECUTOR,
				callback);
	}

	@PermissionTest(permission="SEND_DEVICE_CUSTOMIZATION_READY", sdkMin=29)
	public void testSendDeviceCustomizationReady(){
		ReflectionUtil.invoke(mPackageManager,
				"sendDeviceCustomizationReadyBroadcast" );
	}

	@PermissionTest(permission="MANAGE_CONTENT_CAPTURE", sdkMin=29)
	public void testManageContentCapture(){
		runShellCommandTest("cmd content_capture get bind-instant-service-allowed");
	}

	@RequiresApi(api = Build.VERSION_CODES.Q)
    @PermissionTest(permission="MANAGE_ROLE_HOLDERS", sdkMin=29)
	public void testManageRoleHolders(){
		RoleManager roleManager = systemService(RoleManager.class);
		ReflectionUtil.invoke(roleManager, "getRoleHolders",
				new Class[]{String.class}, RoleManager.ROLE_SMS);

	}

	@PermissionTest(permission="OPEN_APP_OPEN_BY_DEFAULT_SETTINGS", sdkMin=29, sdkMax=30)
	public void testOpenAppOpenByDefaultSettings(){

		if(Constants.BYPASS_TESTS_AFFECTING_UI)
			throw new BypassTestException("This test case affects to UI. skip to avoiding ui stuck.");

		// TOOD: May need to skip if the permission is granted as opening the new
		// activity may interrupt the test app.
		//DevicePolicyManager.ACTION_
		Intent intent = new Intent("com.android.settings.APP_OPEN_BY_DEFAULT_SETTINGS");
		intent.setData(Uri.parse("package:" + mContext.getPackageName()));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
	}

	@PermissionTest(permission="RESET_PASSWORD", sdkMin=29)
	public void testResetPassword(){
		String newPassword = null;
		// In Android 10 a password had to be specified. Since this test only allows a
		// password to be set if it has not been previously set this password will need
		// to be cleared before a subsequent test invocation when the test app is
		// platform signed.
		// https://cs.android.com/android/_/android/platform/frameworks/base/+/d952240979aea7d10b5f81dfa9199323c79b4363
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
			newPassword = "1234";
		}
		KeyguardManager manager = (KeyguardManager)
				mContext.getSystemService(Context.KEYGUARD_SERVICE);
		if(manager.isDeviceSecure()){
			//To avoid untrusted password reset, resetPassword() will either
			//throw SecurityException, or fail silently, if once passowrd is specified.
			//So we need to reset it before testing.
			throw new BypassTestException("Skipped : " +
					"To avoid untrusted password reset, resetPassword() will " +
					"throw SecurityException, if once screen lock passowrd is specified."+
					"Please reset it before testing this permission");
		} else {
			systemService(DevicePolicyManager.class).resetPassword(newPassword, 0);
		}
	}

	@PermissionTest(permission="WRITE_DEVICE_CONFIG", sdkMin=29)
	public void testWriteDeviceConfig(){
		try {
			ReflectionUtil.invoke(Class.forName("android.provider.DeviceConfig"),
					"setProperty",
					new Class[]{String.class, String.class, String.class,
							boolean.class}, "privacy",
					"device_identifier_access_restrictions_disabled", "false", false);
		} catch (ClassNotFoundException e) {
			throw new UnexpectedTestFailureException(e);
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.Q)
    @PermissionTest(permission="NETWORK_MANAGED_PROVISIONING", sdkMin=29)
	public void testNetworkManagedProvisioning(){
		// IWifiManager#setWifiEnabled
		Parcel result = BinderTransaction.getInstance().invoke(Transacts.WIFI_SERVICE,
				Transacts.WIFI_DESCRIPTOR,
				"setWifiEnabled", mContext.getPackageName(), true);

		if (!result.readBoolean()) {
			throw new SecurityException("Unable to set WIFI enabled");
		}

	}

	//TODO:need to write a variant
	@PermissionTest(permission="MANAGE_ACCESSIBILITY", sdkMin=29,sdkMax = 29)
	public void testManageAccessibility(){
		ReflectionUtil.invoke(systemService(AccessibilityManager.class),
				"getAccessibilityShortcutService");
		/*
		if (mDeviceApiLevel == VERSION_CODES.Q) {

		} else {
			//TODO:need to write a variant

			ReflectionUtil.invoke(mAccessibilityManager.getClass(),
					"performAccessibilityShortcut", mAccessibilityManager, null);
			// Allow time for the service to be enabled before checking and disabling.
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				logger.logError(
						"Caught an InterruptedException while waiting for the "
								+ "accessibility service to be enabled",e);
			}
			if (mAccessibilityManager.isTouchExplorationEnabled()) {
				ReflectionUtil.invoke(mAccessibilityManager.getClass(),
						"performAccessibilityShortcut", mAccessibilityManager, null);
			}
		}*/
	}

	@PermissionTest(permission="REVIEW_ACCESSIBILITY_SERVICES", sdkMin=29)
	public void testReviewAccessibilityServices(){

		if(Constants.BYPASS_TESTS_AFFECTING_UI)
			throw new BypassTestException("This test case affects to UI. skip to avoiding ui stuck.");

		Intent intent = new Intent(
				"android.intent.action.REVIEW_ACCESSIBILITY_SERVICES");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);

	}

	@PermissionTest(permission="WRITE_SETTINGS_HOMEPAGE_DATA", sdkMin=29)
	public void testWriteSettingsHomepageData(){
		mContentResolver.query(Uri.parse(
						"content://com.android.settings.homepage.CardContentProvider/cards"),
				null, null, null);
	}

	@PermissionTest(permission="MANAGE_APP_PREDICTIONS", sdkMin=29)
	public void testManageAppPredictions(){
		ReflectionUtil.invoke(systemService(ShortcutManager.class),
				"getShareTargets",
				new Class[]{IntentFilter.class}, new IntentFilter());
	}

	@PermissionTest(permission="USE_BIOMETRIC_INTERNAL", sdkMin=29)
	public void testUseBiometricInternal(){
		BinderTransaction.getInstance().invoke(Transacts.BIOMETRIC_SERVICE, Transacts.BIOMETRIC_DESCRIPTOR,
				"hasEnrolledBiometrics", 0, mContext.getPackageName());
	}

	@PermissionTest(permission="OBSERVE_ROLE_HOLDERS", sdkMin=29)
	public void testObserveRoleHolders(){
		IOnRoleHoldersChangedListener listener = new IOnRoleHoldersChangedListener() {
			@Override
			public void onRoleHoldersChanged(String roleName, int userId) {
				logger.debug(
						"onRoleHoldersChanged: roleName = " + roleName + ", userId = "
								+ userId);
			}

			@Override
			public IBinder asBinder() {
				return new Binder();
			}
		};
		BinderTransaction.getInstance().invoke(Transacts.ROLE_SERVICE, Transacts.ROLE_DESCRIPTOR,
				"addOnRoleHoldersChangedListenerAsUser", listener, 0);
	}

	@PermissionTest(permission="MANAGE_BIOMETRIC", sdkMin=29)
	public void testManageBiometric(){

		try {
			BinderTransaction.getInstance().invoke(Transacts.FACE_SERVICE, Transacts.FACE_DESCRIPTOR,
					"generateChallenge", getActivityToken());
		} catch (BypassTestException e) {
			// For devices without the face service the fingerprint service can be
			// used to test this permission. The lock_settings service also uses
			// this permission, but it is guarded by SELinux policy.
			if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU) {
				BinderTransaction.getInstance().invoke(Transacts.FINGERPRINT_SERVICE,
						Transacts.FINGERPRINT_DESCRIPTOR,
						"cancelAuthenticationFromService", 0, getActivityToken(),
						mContext.getPackageName(), (long) Binder.getCallingPid());
			} else {
				BinderTransaction.getInstance().invoke(Transacts.FINGERPRINT_SERVICE,
						Transacts.FINGERPRINT_DESCRIPTOR,
						"cancelAuthenticationFromService", getActivityToken(),
						mContext.getPackageName(), appUid, Binder.getCallingPid(), 0);
			}
		}
	}

	@PermissionTest(permission="MANAGE_ROLLBACKS", sdkMin=29)
	public void testManageRollbacks(){
		@SuppressLint("WrongConstant") Object manager = mContext.getSystemService("rollback");
		ReflectionUtil.invoke(manager, "getAvailableRollbacks");
	}

	@PermissionTest(permission="TEST_MANAGE_ROLLBACKS", sdkMin=29)
	public void testTestManageRollbacks(){
		// TODO:need to wrie a variant
		// The reloadPersistedData API was only available via reflection in Q
		if (Build.VERSION.SDK_INT== Build.VERSION_CODES.Q) {
			@SuppressLint("WrongConstant") Object manager = mContext.getSystemService("rollback");
			ReflectionUtil.invoke(manager.getClass(), "reloadPersistedData", manager,
					null);
		} else {
			BinderTransaction.getInstance().invoke(Transacts.ROLLBACK_SERVICE,
					Transacts.ROLLBACK_DESCRIPTOR,
					"reloadPersistedData");
		}
	}

	@PermissionTest(permission="MANAGE_SENSOR_PRIVACY", sdkMin=29, sdkMax=30)
	public void testManageSensorPrivacy(){
		// ISensorPrivacyManager#setSensorPrivacy
		BinderTransaction.getInstance().invoke(Transacts.SENSOR_PRIVACY_SERVICE,
				Transacts.SENSOR_PRIVACY_DESCRIPTOR,
				"setSensorPrivacy", false);
	}

	@PermissionTest(permission="WIFI_SET_DEVICE_MOBILITY_STATE", sdkMin=29)
	public void testWifiSetDeviceMobilityState(){
		ReflectionUtil.invoke(systemService(WifiManager.class), "setDeviceMobilityState",
				 new Class[]{int.class}, 0);
	}

	@PermissionTest(permission="MANAGE_BIOMETRIC_DIALOG", sdkMin=29,sdkMax = 29)
	public void testManageBiometricDialog(){
		//TODO:need to write a variant
		BinderTransaction.getInstance().invoke(Transacts.STATUS_BAR_SERVICE,
				Transacts.STATUS_BAR_DESCRIPTOR,
				"hideBiometricDialog");
	}

	@PermissionTest(permission="START_VIEW_PERMISSION_USAGE", sdkMin=29)
	public void testStartViewPermissionUsage(){

		if(Constants.BYPASS_TESTS_AFFECTING_UI)
			throw new BypassTestException("This test case affects to UI. skip to avoiding ui stuck.");

		try {
			Intent intent = new Intent("android.intent.action.VIEW_PERMISSION_USAGE")
					.putExtra("android.intent.extra.PERMISSION_GROUP_NAME",
							"android.permission-group.CAMERA");
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
				//Corresponding Activity will be missing since S, so try to test an activity on
				//the companion app instead.
				intent.setComponent(new
						ComponentName("com.android.certifications.niap.permissions.companion",
						"com.android.certifications.niap.permissions.companion.ViewPermissionUsageActivity"));
			}

			ResolveInfo resolveInfo =mPackageManager.resolveActivity(intent, 0);
			if(resolveInfo == null){
				throw new BypassTestException("the system does not have corresponding activity to" +
						" ROLE_HOLDER_PROVISION_MANAGED_PROFILE action. Let's skip it...");
			}
			//alternative plan but it did not works:android.intent.action.VIEW_PERMISSION_USAGE_FOR_PERIOD
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			throw new BypassTestException(
					"No activity exists on the device to handle the "
							+ "VIEW_PERMISSION_USAGE action");
		}
	}

	@PermissionTest(permission="WIFI_UPDATE_USABILITY_STATS_SCORE", sdkMin=29)
	public void testWifiUpdateUsabilityStatsScore(){
		ReflectionUtil.invoke(systemService(WifiManager.class), "updateWifiUsabilityScore",
				 new Class[]{int.class, int.class, int.class}, 0, 0, 0);
	}

	@RequiresApi(api = Build.VERSION_CODES.Q)
    @PermissionTest(permission="WHITELIST_RESTRICTED_PERMISSIONS", sdkMin=29)
	public void testWhitelistRestrictedPermissions(){
		Set<String> permissions = mPackageManager.getWhitelistedRestrictedPermissions(
				"android", PackageManager.FLAG_PERMISSION_WHITELIST_SYSTEM);

	}

	@PermissionTest(permission="GET_RUNTIME_PERMISSIONS", sdkMin=29)
	public void testGetRuntimePermissions(){
		ReflectionUtil.invoke(mPackageManager, "getPermissionFlags",
				new Class[]{String.class, String.class, UserHandle.class},
				READ_PHONE_STATE, "android",
				UserHandle.getUserHandleForUid(appUid));
	}

	@PermissionTest(permission="READ_DEVICE_CONFIG", sdkMin=29)
	public void testReadDeviceConfig(){
		if (Build.VERSION.SDK_INT >= 35) {
			throw new BypassTestException(
					"READ_DEVICE_CONFIG permission check is disabled in SDK35 beta3. " +
							"Wating public release.");
		}
		try {
			ReflectionUtil.invoke(Class.forName("android.provider.DeviceConfig"),
					"getProperty", null, new Class[]{String.class, String.class},
					//"adservices", "sdksandbox_enforce_restrictions");
					"privacy", "device_identifier_access_restrictions_disabled");
			//                        ReflectionUtil.invoke(Class.forName("android.provider.DeviceConfig"),
			//                                "addOnPropertiesChangedListener", null,
			//                                new Class[]{String.class, Executor.class,DeviceConfig.OnPropertiesChangedListener.class},
			//                                //"adservices", "sdksandbox_enforce_restrictions");
			//                                "privacy", "device_identifier_access_restrictions_disabled");


		} catch (ReflectiveOperationException e) {
			throw new UnexpectedTestFailureException(e);
		}
	}

	@PermissionTest(permission="ADJUST_RUNTIME_PERMISSIONS_POLICY", sdkMin=29)
	public void testAdjustRuntimePermissionsPolicy(){
		BinderTransaction.getInstance().invoke(Transacts.PACKAGE_SERVICE, Transacts.PACKAGE_DESCRIPTOR,
				"getRuntimePermissionsVersion", 0);
	}

}









