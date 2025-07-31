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


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.flags.SyncableFlag;
import android.hardware.biometrics.BiometricPrompt;
import android.hardware.input.IStickyModifierStateListener;
import android.media.IMediaRouter2Manager;
import android.media.MediaRoute2Info;
import android.media.RouteDiscoveryPreference;
import android.media.RouteListingPreference;
import android.media.RoutingSessionInfo;
import android.net.nsd.NsdManager;
import android.net.nsd.OffloadEngine;
import android.net.nsd.OffloadServiceInfo;
import android.os.Binder;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.DropBoxManager;
import android.os.IBinder;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.UserHandle;
import android.provider.E2eeContactKeysManager;
import android.security.FileIntegrityManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.android.certification.niap.permission.dpctester.common.Constants;
import com.android.certification.niap.permission.dpctester.common.ReflectionUtil;
import com.android.certification.niap.permission.dpctester.test.exception.BypassTestException;
import com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException;
import com.android.certification.niap.permission.dpctester.test.runner.SignaturePermissionTestModuleBase;
import com.android.certification.niap.permission.dpctester.test.tool.BinderTransaction;
import com.android.certification.niap.permission.dpctester.test.tool.DeviceConfigTool;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTest;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestModule;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.function.LongConsumer;

@PermissionTestModule(name="Signature 35(V) Test Cases",prflabel="VIC(15)")
public class SignatureTestModuleV extends SignaturePermissionTestModuleBase {
	public SignatureTestModuleV(@NonNull Activity activity) {
		super(activity);
	}


	private <T> T systemService(Class<T> clazz){
		return Objects.requireNonNull(getService(clazz),"[npe_system_service]"+clazz.getSimpleName());
	}

	@RequiresApi(api = 35)
	@SuppressLint("MissingPermission")
	@PermissionTest(permission="WRITE_VERIFICATION_STATE_E2EE_CONTACT_KEYS", sdkMin=35)
	public void testWriteVerificationStateE2eeContactKeys(){

		String LOOKUP_KEY = "0r1-423A2E4644502A2E50";
		String DEVICE_ID = "device_id_value";
		String ACCOUNT_ID = "+1 (555) 555-1234";

		E2eeContactKeysManager e2m = (E2eeContactKeysManager) mContext.getSystemService
				(Context.CONTACT_KEYS_SERVICE);
		//Put dummy data for test
		e2m.updateOrInsertE2eeContactKey(LOOKUP_KEY,DEVICE_ID,ACCOUNT_ID, new byte[]{0});
		// Call hidden method to enable check for this permission.
		//                        boolean b = e2m.updateE2eeContactKeyLocalVerificationState
		//                                (LOOKUP_KEY,DEVICE_ID,ACCOUNT_ID,mContext.getPackageName(),
		//                                        E2eeContactKeysManager.VERIFICATION_STATE_VERIFIED);
		boolean b = (boolean) ReflectionUtil.invoke(e2m,
				"updateE2eeContactKeyLocalVerificationState",
				new Class<?>[]{String.class,String.class,String.class,String.class,int.class},
				LOOKUP_KEY,DEVICE_ID,ACCOUNT_ID,mContext.getPackageName(),
				E2eeContactKeysManager.VERIFICATION_STATE_VERIFIED
		);
		logger.debug("updateE2eeContactKeyLocalVerificationState result: " + b);
		if(!b){
			throw new SecurityException("call updateE2eeContactKeyLocalVerificaionState failed");
		}
	}

	@PermissionTest(permission="CAMERA_HEADLESS_SYSTEM_USER", sdkMin=35)
	public void testCameraHeadlessSystemUser(){
		//Permission for automotive
		if(!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE)){
			throw new BypassTestException(
					"This permission requires feature "
							+ PackageManager.FEATURE_AUTOMOTIVE);
		}
		//no implementation here
	}

	@PermissionTest(permission="CAMERA_PRIVACY_ALLOWLIST", sdkMin=35)
	public void testCameraPrivacyAllowlist(){
		//Permission for automotive
		if(!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE)){
			throw new BypassTestException(
					"This permission requires feature "
							+ PackageManager.FEATURE_AUTOMOTIVE);
		}
		//no implementation here
	}

//	@PermissionTest(permission="MANAGE_REMOTE_AUTH", sdkMin=35)
//	public void testManageRemoteAuth(){
//		logger.debug("MANAGE_REMOTE_AUTH is not implemented yet");
//		throw new BypassTestException("MANAGE_REMOTE_AUTH permission is not implemented yet");
//		//BinderTransaction.getInstance().invoke(Transacts.SERVICE, Transacts.DESCRIPTOR,
//		//       Transacts.unregisterCoexCallback, (Object) null);
//	}
//
//	@PermissionTest(permission="USE_REMOTE_AUTH", sdkMin=35)
//	public void testUseRemoteAuth(){
//		logger.debug("USE_REMOTE_AUTH is not implemented yet");
//		throw new BypassTestException("USE_REMOTE_AUTH permission is not implemented yet");
//		//BinderTransaction.getInstance().invoke(Transacts.SERVICE, Transacts.DESCRIPTOR,
//		//       Transacts.unregisterCoexCallback, (Object) null);
//	}

	@PermissionTest(permission="THREAD_NETWORK_PRIVILEGED", sdkMin=35)
	public void testThreadNetworkPrivileged(){
		Class<?> threadNetworkConClazz = null;
		String FEATURE_THREAD_NETWORK = "android.hardware.thread_network";
		if(!mContext.getPackageManager().hasSystemFeature(FEATURE_THREAD_NETWORK)){
			throw new BypassTestException("thread netrowk manager is not supported.");
		} else {
			try {
				threadNetworkConClazz = Class.forName(
						"android.net.thread.ThreadNetworkManager");
				Object threadNetworkCon = mContext.getSystemService(threadNetworkConClazz);
				//Yet Implemented Because I couldn't find the system it it enabled.
				//System.out.println(ReflectionUtils.checkDeclaredMethod(threadNetworkCon,"set").toString());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	@PermissionTest(permission="REGISTER_NSD_OFFLOAD_ENGINE", sdkMin=35)
	public void testRegisterNsdOffloadEngine(){
		//Check Routine as it is like below.
		//=>INSDServiceConnector client = INSDManager.connect(INSdMangerCallback cb,booleanuseJava)
		//=>client.regsiterOffloadEnginge,unregisterOffloadEngine

		//manager has hidden method below
		//unregisterOffloadEngine( android.net.nsd.OffloadEngine)
		//registerOffloadEngine( java.lang.String long long java.util.concurrent.Executor android.net.nsd.OffloadEngine),
		NsdManager manager = (NsdManager) mContext.getSystemService(Context.NSD_SERVICE);
		int OFFLOAD_TYPE_REPLY = 1;
		int OFFLOAD_CAPABILITY_BYPASS_MULTICAST_LOCK = 1;
		OffloadEngine mock = new OffloadEngine() {
			@Override
			public void onOffloadServiceUpdated(OffloadServiceInfo info) {

			}

			@Override
			public void onOffloadServiceRemoved(OffloadServiceInfo info) {

			}
		};
		ReflectionUtil.invoke(manager,
				"registerOffloadEngine",
				new Class<?>[]{String.class,long.class,long.class, Executor.class,OffloadEngine.class},
				"iface1",
				OFFLOAD_TYPE_REPLY,
				OFFLOAD_CAPABILITY_BYPASS_MULTICAST_LOCK,
				mContext.getMainExecutor(),
				mock);
		//If succeed unregister it for in the case.
		ReflectionUtil.invoke(manager,
				"unregisterOffloadEngine",
				new Class<?>[]{OffloadEngine.class},
				mock);
	}
	/* TODO:Should be moved to DPM test cases
	@PermissionTest(permission="QUARANTINE_APPS", sdkMin=35)
	public void testQuarantineApps(){
		//final boolean quarantined = ((flags & PackafgeManager.FLAG_SUSPEND_QUARANTINED) != 0)
		//        && Flags.quarantinedEnabled();
		// Flags.quarantinedEnabled();
		try {

			Class<?> clazzDialogBuilder = null;
			clazzDialogBuilder = Class.forName("android.content.pm.SuspendDialogInfo$Builder");
			Constructor<?> constructor = clazzDialogBuilder.getConstructor();
			Object builderObj = constructor.newInstance();

			Object dialogInfo =
					ReflectionUtil.invoke(builderObj, "build");

			int FLAG_SUSPEND_QUARANTINED = 0x00000001;
			BinderTransaction.getInstance().invoke(
					Transacts.PACKAGE_SERVICE,
					Transacts.PACKAGE_DESCRIPTOR,
					"setPackagesSuspendedAsUser",
					new String[]{mContext.getPackageName()}, false,
					new PersistableBundle(),new PersistableBundle(),dialogInfo,
					FLAG_SUSPEND_QUARANTINED,mContext.getPackageName(),appUid,appUid);

		} catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException | InstantiationException ex){
			throw new UnexpectedTestFailureException(ex);
		}

    }*/

	@PermissionTest(permission="CONFIGURE_FACTORY_RESET_PROTECTION", sdkMin=35)
	public void testConfigureFactoryResetProtection(){
		BinderTransaction.getInstance().invoke(Transacts.PDB_SERVICE, Transacts.PDB_DESCRIPTOR,
				"deactivateFactoryResetProtection",
				(Object) "dummy_bytes".getBytes());

	}

	@PermissionTest(permission="ACCESS_LAST_KNOWN_CELL_ID", sdkMin=35)
	public void testAccessLastKnownCellId(){
		BinderTransaction.getInstance().invoke(Transacts.TELEPHONY_SERVICE, Transacts.TELEPHONY_DESCRIPTOR,
				"getLastKnownCellIdentity",
				0,mContext.getPackageName(),"callingFeatureId");

	}

	@PermissionTest(permission="ACCESS_HIDDEN_PROFILES_FULL", sdkMin=35)
	public void testAccessHiddenProfilesFull(){
		//same as access_hidden_profiles install permission
		//those two permissions are treated as 'either is fine' condition.
		LauncherApps launcherApps = (LauncherApps)
				mContext.getSystemService(Context.LAUNCHER_APPS_SERVICE);
		//If the caller cannot access hidden profiles the method returns null
		Object intent = ReflectionUtil.invoke
				(launcherApps, "getPrivateSpaceSettingsIntent");
		if(intent == null){
			throw new SecurityException("Caller cannot access hidden profiles");
		}

	}

	@PermissionTest(permission="MANAGE_ENHANCED_CONFIRMATION_STATES", sdkMin=35)
	public void testManageEnhancedConfirmationStates(){
		//ReflectionUtils.checkDeclaredMethod("android.os.SystemConfigManager")
		@SuppressLint("WrongConstant")
		Object systemConfig = mContext.getSystemService("system_config");
		//logger.logSystem(">"+ReflectionUtils.checkDeclaredMethod(systemConfig,"get").toString());
		ReflectionUtil.invoke(systemConfig,
				"getEnhancedConfirmationTrustedInstallers");
		//getEnhancedConfirmationTrustedInstallers
		//logger.debug("Test case for android.permission.MANAGE_ENHANCED_CONFIRMATION_STATES not implemented yet");
		//BinderTransaction.getInstance().invoke(Transacts.SYSTEM_CONFIG_SERVICE, Transacts.SYSTEM_CONFIG_DESCRIPTOR,
		//"getEnhancedConfirmationTrustedPackages, (Object) null");
	}

	@PermissionTest(permission="READ_DROPBOX_DATA", sdkMin=35,developmentProtection = true)
	public void testReadDropboxData(){
		//Same as PEEK_DROPBOX_DATA
		if (mContext.checkSelfPermission(Manifest.permission.READ_LOGS)
				== PackageManager.PERMISSION_GRANTED &&
				mContext.checkSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS)
						== PackageManager.PERMISSION_GRANTED) {
			if(mContext.checkSelfPermission(Manifest.permission.READ_DROPBOX_DATA)
					!= PackageManager.PERMISSION_GRANTED)
				throw new BypassTestException(
						"Bypass the check due to avoid unexpected behaviour. ");
		}

		long currTimeMs = System.currentTimeMillis();

		Parcel result= BinderTransaction.getInstance().invoke(Transacts.DROPBOX_SERVICE,
				Transacts.DROPBOX_DESCRIPTOR,
				"getNextEntry",
				"test-companion-tag", currTimeMs-(1000*60*60*8), mContext.getPackageName());

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

	@PermissionTest(permission="LAUNCH_PERMISSION_SETTINGS", sdkMin=35)
	public void testLaunchPermissionSettings(){

		if (Constants.BYPASS_TESTS_AFFECTING_UI)
			throw new BypassTestException("This test case affects to UI. skip to avoiding ui stuck.");


		//The action may affect to ui
		String ACTION_MANAGE_APP_PERMISSIONS = "android.intent.action.MANAGE_APP_PERMISSIONS";
		Intent intent = new Intent(ACTION_MANAGE_APP_PERMISSIONS);
		intent.setAction("android.settings.APP_PERMISSIONS_SETTINGS");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);

	}

	@PermissionTest(permission="REQUEST_OBSERVE_DEVICE_UUID_PRESENCE", sdkMin=35)
	public void testRequestObserveDeviceUuidPresence(){

		if(!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE)){
			throw new BypassTestException(
					"This permission requires feature "
							+ PackageManager.FEATURE_AUTOMOTIVE);
		}
		try {
			Class<?> clazzOdprBuilder = null;
			clazzOdprBuilder = Class.forName("android.companion.ObservingDevicePresenceRequest$Builder");
			Constructor constructor = clazzOdprBuilder.getConstructor();
			Object builderObj = constructor.newInstance();
			ReflectionUtil.invoke(
					builderObj.getClass(),
					"setUuid",builderObj,new Class[]{ParcelUuid.class},
					ParcelUuid.fromString("00000000-0000-1000-8000-00805F9B34FB"));

			Object odprParams =
					ReflectionUtil.invoke(clazzOdprBuilder, "build", builderObj, new Class[]{});

			BinderTransaction.getInstance().invoke(
					Transacts.COMPANION_DEVICE_SERVICE, Transacts.COMPANION_DEVICE_DESCRIPTOR,
					"startObservingDevicePresence",
					odprParams,mContext.getPackageName(),appUid);
		} catch (SecurityException ex) {
			throw ex;
		} catch (Exception ex){
			throw new UnexpectedTestFailureException(ex);
		}

	}

	@PermissionTest(permission="USE_COMPANION_TRANSPORTS", sdkMin=35)
	public void testUseCompanionTransports(){
		final int MESSAGE_REQUEST_PING = 0x63807378;
		BinderTransaction.getInstance().invoke(
				Transacts.COMPANION_DEVICE_SERVICE, Transacts.COMPANION_DEVICE_DESCRIPTOR,
				"sendMessage",
				MESSAGE_REQUEST_PING, new byte[]{0}, new int[]{0});
	}

	@PermissionTest(permission="MEDIA_ROUTING_CONTROL", sdkMin=35)
	public void testMediaRoutingControl(){
		IMediaRouter2Manager manager = new IMediaRouter2Manager() {
			@Override
			public void notifySessionCreated(int requestId, RoutingSessionInfo session) throws RemoteException {

			}
			@Override
			public void notifySessionUpdated(RoutingSessionInfo session) throws RemoteException {

			}
			@Override
			public void notifySessionReleased(RoutingSessionInfo session) throws RemoteException {

			}
			@Override
			public void notifyDiscoveryPreferenceChanged(String packageName, RouteDiscoveryPreference discoveryPreference) throws RemoteException {

			}
			@Override
			public void notifyRouteListingPreferenceChange(String packageName, RouteListingPreference routeListingPreference) throws RemoteException {

			}
			@Override
			public void notifyRoutesUpdated(List<MediaRoute2Info> routes) throws RemoteException {

			}
			@Override
			public void notifyRequestFailed(int requestId, int reason) throws RemoteException {

			}
			@Override
			public void invalidateInstance() throws RemoteException {

			}
			@Override
			public IBinder asBinder() {
				return new Binder();
			}
		};
		BinderTransaction.getInstance().invoke(Transacts.MEDIA_ROUTER_SERVICE, Transacts.MEDIA_ROUTER_DESCRIPTOR,
				"registerManager",
				manager,mContext.getPackageName());
	}

	@PermissionTest(permission="REPORT_USAGE_STATS", sdkMin=35)
	public void testReportUsageStats(){
		BinderTransaction.getInstance().invoke(Transacts.USAGE_STATS_SERVICE, Transacts.USAGE_STATS_DESCRIPTOR,
				"reportChooserSelection",
				mContext.getPackageName(),appUid,"text/html",
				new String[]{"annotation-1"},"action");
	}

	@PermissionTest(permission="SET_BIOMETRIC_DIALOG_ADVANCED", sdkMin=35)
	public void testSetBiometricDialogAdvanced(){

		//NOTE :
		//Conflict to USE_BIOMETRIC permission
		//You should put below in noperm/AndroidManifest.xml to test
		//<uses-permission android:name="android.permission.USE_BIOMETRIC" />

		BiometricPrompt.Builder bmBuilder = new BiometricPrompt.Builder(mContext)
				.setTitle("a").setNegativeButton("text", mContext.getMainExecutor(), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {

					}
				});
		ReflectionUtil.invoke(bmBuilder,
				"setLogoDescription",  new Class<?>[]{String.class},
				"dummy-logo-desription");
		BiometricPrompt bmPrompt = bmBuilder.build();
		bmPrompt.authenticate(new CancellationSignal(),
				mContext.getMainExecutor()
				, new BiometricPrompt.AuthenticationCallback() {
					@Override
					public void onAuthenticationError(int errorCode, CharSequence errString) {
						super.onAuthenticationError(errorCode, errString);
					}
				}
		);

	}

	@PermissionTest(permission="RECORD_SENSITIVE_CONTENT", sdkMin=35)
	public void testRecordSensitiveContent(){

		String ACTION_MANAGE_APP_PERMISSIONS = "android.intent.action.MANAGE_APP_PERMISSIONS";
		Intent intent = new Intent(ACTION_MANAGE_APP_PERMISSIONS);
		intent.setAction("android.settings.APP_PERMISSIONS_SETTINGS");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);


	}

	@PermissionTest(permission="ACCESS_SMARTSPACE", sdkMin=35,developmentProtection = true)
	public void testAccessSmartspace(){
		// Regard same as MANAGE_SMARTSPACE
		// Note this is fragile since the implementation of SmartspaceSessionId can
		// change in the future, but since there is no way to construct an instance
		// of SmartspaceSessionId this at least allows the test to proceed.
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
				Transacts.SMART_SPACE_DESCRIPTOR, "destroySmartspaceSession",
				smartspaceId);
	}

	@PermissionTest(permission="ACCESS_CONTEXTUAL_SEARCH", sdkMin=35)
	public void testAccessContextualSearch(){
		int ENTRYPOINT_LONG_PRESS_HOME = 2;
		BinderTransaction.getInstance().invoke(Transacts.CONTEXTUAL_SEARCH_SERVICE,
				Transacts.CONTEXTUAL_SEARCH_DESCRIPTOR,
				"startContextualSearch",
				(Object) ENTRYPOINT_LONG_PRESS_HOME);
	}

	@PermissionTest(permission="SET_THEME_OVERLAY_CONTROLLER_READY", sdkMin=35)
	public void testSetThemeOverlayControllerReady(){
		//Unreachable
		if(!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE)){
			throw new BypassTestException(
					"This permission requires feature "
							+ PackageManager.FEATURE_AUTOMOTIVE);
		}
		//Flags.setHomeDelay should be false to chek it and it is not allowed to automotive
		//com.android.systemui.shared.Flags.enableHomeDelay;
		ReflectionUtil.invoke(systemService(ActivityManager.class),
				"setThemeOverlayReady",
				new Class<?>[]{int.class},appUid);
	}

	@PermissionTest(permission="SHOW_CUSTOMIZED_RESOLVER", sdkMin=35)
	public void testShowCustomizedResolver(){

		if (Constants.BYPASS_TESTS_AFFECTING_UI)
			throw new BypassTestException("This test case affects to UI. skip to avoiding ui stuck.");

		String ACTION_SHOW_NFC_RESOLVER = "android.nfc.action.SHOW_NFC_RESOLVER";
		Intent intent = new Intent(ACTION_SHOW_NFC_RESOLVER);
		intent.putExtra(Intent.EXTRA_INTENT,new Intent());
		intent.putExtra(Intent.EXTRA_TITLE,"test-title");
		//dintent.setAction("android.settings.APP_PERMISSIONS_SETTINGS");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		ResolveInfo shareRi = mPackageManager.
				resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
		if(shareRi.activityInfo != null) {
			mContext.startActivity(intent);
		} else {
			throw new UnexpectedTestFailureException("Corresponding receiver was not found");
		}
	}

	@PermissionTest(permission="MONITOR_STICKY_MODIFIER_STATE", sdkMin=35)
	public void testMonitorStickyModifierState(){

		IStickyModifierStateListener listener = new IStickyModifierStateListener(){
			@Override
			public IBinder asBinder() {
				return new Binder();
			}
			@Override
			public void onStickyModifierStateChanged(int modifierState, int lockedModifierState) throws RemoteException {

			}
		};
		BinderTransaction.getInstance().invoke(Transacts.INPUT_SERVICE, Transacts.INPUT_DESCRIPTOR,
				"registerStickyModifierStateListener",
				listener);
	}

	@PermissionTest(permission="USE_ON_DEVICE_INTELLIGENCE", sdkMin=35)
	public void testUseOnDeviceIntelligence(){
		@SuppressLint("WrongConstant") Object ond = mContext.getSystemService("on_device_intelligence");
		//logger.system(">"+ond.toString());
		try {
			ReflectionUtil.invoke(ond,
					"getVersion",
					new Class<?>[]{Executor.class, LongConsumer.class},
					mContext.getMainExecutor(), (LongConsumer) result -> {
					});
		} catch (ReflectionUtil.ReflectionIsTemporaryException ex){
			Throwable cause = ex.getCause();
			if(cause instanceof InvocationTargetException){
				Throwable cause2 = cause.getCause();
				if(cause2 instanceof IllegalStateException){
					logger.debug("Expected Result:Remote service is not configured.");
					//expected message : Remote service is not configured
				} else {
					throw ex;//Secuirty Exception?
				}
			}
		}
	}

	@PermissionTest(permission="SYNC_FLAGS", sdkMin=35)
	public void testSyncFlags(){
		try {
			Object featureFlags = ReflectionUtil.invoke(
					Class.forName("android.flags.FeatureFlags"),"getInstance");

			ReflectionUtil.invoke(Class.forName("android.flags.FeatureFlags"),
					"booleanFlag",
					new Class<?>[]{String.class,String.class,boolean.class},"dummy","dummy-boolean",true);
			ReflectionUtil.invoke(featureFlags, "sync");
		} catch (ClassNotFoundException e) {
			throw new UnexpectedTestFailureException(e);
		}
	}

	@PermissionTest(permission="WRITE_FLAGS", sdkMin=35)
	public void testWriteFlags(){
		BinderTransaction.getInstance().invoke(Transacts.FEATURE_FLAGS_SERVICE, Transacts.FEATURE_FLAGS_DESCRIPTOR,
				"overrideFlag",
				new SyncableFlag("test","permission_tester_writeflag","dummy",true));
	}

	@PermissionTest(permission="GET_BINDING_UID_IMPORTANCE", sdkMin=35)
	public void testGetBindingUidImportance(){
		if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.PACKAGE_USAGE_STATS)
				== PackageManager.PERMISSION_GRANTED) {
			throw new BypassTestException(
					"This permission is not evlauated when PACKAGE_USAGE_STATS is allowed");
		}
		BinderTransaction.getInstance().invoke(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
				"getBindingUidProcessState",
				appUid, mContext.getPackageName());
	}

	@PermissionTest(permission="MANAGE_DISPLAYS", sdkMin=35)
	public void testManageDisplays(){
		//logger.debug("Test case for android.permission.MANAGE_DISPLAYS not implemented yet");
		BinderTransaction.getInstance().invoke(Transacts.DISPLAY_SERVICE,Transacts.DISPLAY_DESCRIPTOR,
				"enableConnectedDisplay",
				0);
	}

	@PermissionTest(permission="PREPARE_FACTORY_RESET", sdkMin=35)
	public void testPrepareFactoryReset(){

		if (Constants.BYPASS_TESTS_AFFECTING_UI)
			throw new BypassTestException("This test case affects to UI. skip to avoiding ui stuck.");


		//String ACTION_SHOW_NFC_RESOLVER = "android.nfc.action.SHOW_NFC_RESOLVER";
		String ACTION_PREPARE_FACTORY_RESET =
				"com.android.settings.ACTION_PREPARE_FACTORY_RESET";
		Intent intent = new Intent(ACTION_PREPARE_FACTORY_RESET);
		//intent.putExtra(Intent.EXTRA_INTENT,new Intent());
		//intent.putExtra(Intent.EXTRA_TITLE,"test-title");
		//intent.setAction("android.settings.APP_PERMISSIONS_SETTINGS");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		ResolveInfo res = null;
		try {
			res = mPackageManager.
					resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

			if(res != null && res.activityInfo != null) {
				mContext.startActivity(intent);
			} else {
				throw new BypassTestException("Unable to resolve a Factory Reset Handler Activty");
			}

		} catch (ActivityNotFoundException ex){
			throw new BypassTestException("Unable to resolve a Factory Reset Handler Activty");
		}
	}

	@PermissionTest(permission="GET_BACKGROUND_INSTALLED_PACKAGES", sdkMin=35)
	public void testGetBackgroundInstalledPackages(){
		BinderTransaction.getInstance().invoke(Transacts.BACKGROUND_INSTALL_CONTROL_SERVICE,
				Transacts.BACKGROUND_INSTALL_CONTROL_DESCRIPTOR,
				"getBackgroundInstalledPackages",
				PackageManager.MATCH_ALL,appUid);
	}


	@RequiresApi(api = Build.VERSION_CODES.S)
    @PermissionTest(permission="READ_SYSTEM_GRAMMATICAL_GENDER", sdkMin=35)
	public void testReadSystemGrammaticalGender(){
		BinderTransaction.getInstance().invoke(Transacts.GRAMMATICAL_INFLECTION_SERVICE,
				Transacts.GRAMMATICAL_INFLECTION_DESCRIPTOR,
				"getSystemGrammaticalGender",
				mContext.getAttributionSource(),appUid);
	}

	@PermissionTest(permission="RESTRICT_DISPLAY_MODES", sdkMin=35)
	public void testRestrictDisplayModes(){
		BinderTransaction.getInstance().invoke(Transacts.DISPLAY_SERVICE, Transacts.DISPLAY_DESCRIPTOR,
				"requestDisplayModes",
				getActivityToken(),0,null);
	}

	@RequiresApi(api = Build.VERSION_CODES.R)
    @PermissionTest(permission="SETUP_FSVERITY", sdkMin=35)
	public void testSetupFsverity(){
		String prop =
				DeviceConfigTool.Companion.getProperty("hardware_backed_security",
						"android.security.fsverity_api");
		@SuppressLint("WrongConstant") FileIntegrityManager fmg =
				(FileIntegrityManager) mContext.getSystemService(Context.FILE_INTEGRITY_SERVICE);

		if(prop==null || !prop.equals("true") || !fmg.isApkVeritySupported()){
			throw new BypassTestException("fsverity api is not supported");
		}

        File file = null;
        try {
            file = File.createTempFile("authfd", ".tmp",mContext.getCacheDir());
			String TEST_FILE_CONTENT = "fs-verity";
			try (var fos = new FileOutputStream(file)) {
				fos.write(TEST_FILE_CONTENT.getBytes());
			}
			//Thread.sleep(250);
			logger.debug("testEnableAndMeasureFsVerityByFile: "+file.getAbsolutePath());
        } catch (IOException e) {
            throw new UnexpectedTestFailureException("Failed to generate a temporary file.");
        }
		//logger.system("fmg="+fmg.isApkVeritySupported());
		//"android.security.fsverity_api";
		try {
			ReflectionUtil.invoke
					(fmg, "setupFsVerity", file);
		} catch (ReflectionUtil.ReflectionIsTemporaryException ex){
			Throwable cause = ex.getCause();
			if(cause instanceof InvocationTargetException){
				Throwable cause2 = cause.getCause();
				if(cause2 instanceof NullPointerException){
					logger.debug("Expected Result:Null pointer Exception.");
				} else {
					throw ex;
				}
			} else {
				throw ex;
			}
		}

	}

	@PermissionTest(permission="SOUNDTRIGGER_DELEGATE_IDENTITY", sdkMin=35)
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
		//From SDK35, a parameter has been added
		BinderTransaction.getInstance().invoke(Transacts.SOUND_TRIGGER_SERVICE,
				Transacts.SOUND_TRIGGER_DESCRIPTOR, "attachAsMiddleman",
				identity, identity, null, new Binder());
	}

	@PermissionTest(permission = "CLEAR_APP_CACHE",sdkMin = 35)
	public void testClearAppCache() {
		ReflectionUtil.invoke(mPackageManager, "freeStorage",
				new Class<?>[]{String.class,long.class, IntentSender.class}, "",100, null);
	}

}









