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


import static android.security.keystore.KeyProperties.DIGEST_NONE;
import static android.security.keystore.KeyProperties.DIGEST_SHA256;
import static android.security.keystore.KeyProperties.DIGEST_SHA512;
import static android.security.keystore.KeyProperties.KEY_ALGORITHM_EC;
import static android.security.keystore.KeyProperties.PURPOSE_SIGN;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.LocaleManager;
import android.app.WallpaperManager;
import android.app.admin.DevicePolicyManager;
import android.app.usage.UsageStatsManager;
import android.app.wallpapereffectsgeneration.CinematicEffectRequest;
import android.app.wallpapereffectsgeneration.CinematicEffectResponse;
import android.app.wallpapereffectsgeneration.ICinematicEffectListener;
import android.companion.AssociationRequest;
import android.companion.CompanionDeviceManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.hardware.display.DisplayManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.IpConfiguration;
import android.net.NetworkCapabilities;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.OutcomeReceiver;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.security.keystore.KeyGenParameterSpec;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.SignalStrengthUpdateRequest;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.accessibility.CaptioningManager;
import android.window.ITaskFpsCallback;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresExtension;
import androidx.core.content.res.ResourcesCompat;

import com.android.certification.niap.permission.dpctester.MainActivity;
import com.android.certification.niap.permission.dpctester.activity.TestActivity;
import com.android.certification.niap.permission.dpctester.common.Constants;
import com.android.certification.niap.permission.dpctester.common.ReflectionUtil;
import com.android.certification.niap.permission.dpctester.test.exception.BypassTestException;
import com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException;
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestRunner;
import com.android.certification.niap.permission.dpctester.test.runner.SignaturePermissionTestModuleBase;
import com.android.certification.niap.permission.dpctester.test.tool.BinderTransaction;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTest;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestModule;
import com.android.certification.niap.permission.dpctester.test.tool.TesterUtils;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@PermissionTestModule(name="Signature 33(T) Test Cases",prflabel = "Tiramisu(13)")
public class SignatureTestModuleT extends SignaturePermissionTestModuleBase {
	public SignatureTestModuleT(@NonNull Activity activity) {
		super(activity);
	}


	private <T> T systemService(Class<T> clazz){
		return Objects.requireNonNull(getService(clazz),"[npe_system_service]"+clazz.getSimpleName());
	}


	@PermissionTest(permission="LOCATION_BYPASS", sdkMin=33)
	public void testLocationBypass(){
		ReflectionUtil.invoke(systemService(LocationManager.class),
				"setAdasGnssLocationEnabled",
				new Class<?>[]{boolean.class}, true);
	}

	@PermissionTest(permission="CONTROL_AUTOMOTIVE_GNSS", sdkMin=33)
	public void testControlAutomotiveGnss(){
		if (!mPackageManager.hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE)) {
			throw new BypassTestException("This permission requires feature "
					+ PackageManager.FEATURE_AUTOMOTIVE);
		}
		ReflectionUtil.invoke(systemService(LocationManager.class),
				"isAutomotiveGnssSuspended");
	}

	@PermissionTest(permission="MANAGE_WIFI_NETWORK_SELECTION", sdkMin=33)
	public void testManageWifiNetworkSelection(){
		ReflectionUtil.invoke(systemService(WifiManager.class),
				"getSsidsAllowlist");
	}

	@SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @PermissionTest(permission="MANAGE_WIFI_INTERFACES", sdkMin=33)
	public void testManageWifiInterfaces(){
		systemService(WifiManager.class).reportCreateInterfaceImpact(WifiManager.WIFI_INTERFACE_TYPE_STA,
				true, new Executor() {
					@Override
					public void execute(Runnable runnable) {}
				}, new BiConsumer<Boolean, Set<WifiManager.InterfaceCreationImpact>>() {
					@Override
					public void accept(Boolean aBoolean, Set<WifiManager.InterfaceCreationImpact> interfaceCreationImpacts) {}
				});

	}

	@PermissionTest(permission="TRIGGER_LOST_MODE", sdkMin=33)
	public void testTriggerLostMode(){

		DevicePolicyManager dm = systemService(DevicePolicyManager.class);
		try {
			ReflectionUtil.invoke(dm,
					"sendLostModeLocationUpdate",
					new Class<?>[]{Executor.class, Consumer.class},
					new Executor() {
						@Override
						public void execute(Runnable runnable) {
						}
					}, new Consumer<Boolean>() {
						@Override
						public void accept(Boolean aBoolean) {

						}

						@Override
						public Consumer<Boolean> andThen(Consumer<? super Boolean> after) {
							return Consumer.super.andThen(after);
						}
					});
		}catch (ReflectionUtil.ReflectionIsTemporaryException e){
			Throwable cause = e.getCause();
			if(cause != null && cause.getClass().getSimpleName().equals("InvocationTargetException")){
				Throwable cause2 = cause.getCause();
				if(cause2 != null && cause2.getClass().getSimpleName().equals("IllegalStateException")){
					logger.debug("The test should raise IllegalState Exception. Intended behaviour");
					return;
				}
			}
			throw e;
		}

	}

	@PermissionTest(permission="QUERY_USERS", sdkMin=33)
	public void testQueryUsers(){
		ReflectionUtil.invoke(systemService(UserManager.class),
				"getUserRestrictionSource",
				new Class<?>[]{java.lang.String.class, android.os.UserHandle.class},
				"Hello",UserHandle.getUserHandleForUid(0));
	}

	@PermissionTest(permission="QUERY_ADMIN_POLICY", sdkMin=33)
	public void testQueryAdminPolicy(){

		String devicePolicySettings="true";

		//TODO: need to prepare a version for U

		/*if(Build.VERSION.SDK_INT>=VERSION_CODES.UPSIDE_DOWN_CAKE){
			devicePolicySettings = ReflectionUtils.deviceConfigGetProperty("device_policy_manager", "enable_permission_based_access");
			ReflectionUtils.deviceConfigSetProperty("device_policy_manager", "enable_permission_based_access","false",false);
		}*/

		ReflectionUtil.invoke(systemService(DevicePolicyManager.class),
				"getWifiSsidPolicy");

		//Revert the settings
		/*if(Build.VERSION.SDK_INT>=VERSION_CODES.UPSIDE_DOWN_CAKE){
			ReflectionUtils.deviceConfigSetProperty("device_policy_manager", "enable_permission_based_access",devicePolicySettings,true);

		}*/

	}

	@PermissionTest(permission="PROVISION_DEMO_DEVICE", sdkMin=33, sdkMax=34)
	public void testProvisionDemoDevice(){

		Class<?> fmdpBuilderClazz = null;
		Class<?> fmdpClazz = null;
		try {
			fmdpBuilderClazz = Class.forName("android.app.admin.FullyManagedDeviceProvisioningParams$Builder");
			fmdpClazz = Class.forName("android.app.admin.FullyManagedDeviceProvisioningParams");

			Constructor<?> constructor = fmdpBuilderClazz.getConstructor(ComponentName.class,String.class);
			Object fmdpBuilderObj =
					constructor.newInstance(new ComponentName(mContext,MainActivity.class),"");
			Object fmdpObj = ReflectionUtil.invoke(fmdpBuilderClazz,
					"build", fmdpBuilderObj,
					new Class<?>[]{});
			ReflectionUtil.invoke(systemService(DevicePolicyManager.class),
					"provisionFullyManagedDevice",
					new Class<?>[]{fmdpClazz},fmdpObj);
		} catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException |
				 InvocationTargetException e) {
			throw new UnexpectedTestFailureException(e);
		} catch (UnexpectedTestFailureException e){
			if(e.getMessage().equals("java.lang.reflect.InvocationTargetException")){
				boolean foundExpected = TesterUtils.findCauseInStackTraceElement(false,e,
								"android.os.ServiceSpecificException");
				if(!foundExpected) throw e;
			} else {
				throw e;
			}
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @PermissionTest(permission="REQUEST_COMPANION_PROFILE_APP_STREAMING", sdkMin=33)
	public void testRequestCompanionProfileAppStreaming(){
		if (!mPackageManager.hasSystemFeature(
				PackageManager.FEATURE_COMPANION_DEVICE_SETUP)) {
			throw new BypassTestException(
					"Device does not have the "
							+ "PackageManager#FEATURE_COMPANION_DEVICE_SETUP feature "
							+ "for this test");
		}
		AssociationRequest request = null;
		request = new AssociationRequest.Builder().setDeviceProfile(
				AssociationRequest.DEVICE_PROFILE_APP_STREAMING).build();

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


	@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @PermissionTest(permission="REQUEST_COMPANION_PROFILE_COMPUTER", sdkMin=33)
	public void testRequestCompanionProfileComputer(){

		if (!mPackageManager.hasSystemFeature(
				PackageManager.FEATURE_COMPANION_DEVICE_SETUP)) {
			throw new BypassTestException(
					"Device does not have the "
							+ "PackageManager#FEATURE_COMPANION_DEVICE_SETUP feature "
							+ "for this test");
		}
		AssociationRequest request = null;
		request = new AssociationRequest.Builder().setDeviceProfile(
				AssociationRequest.DEVICE_PROFILE_COMPUTER).build();

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

	@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @PermissionTest(permission="REQUEST_COMPANION_SELF_MANAGED", sdkMin=33)
	public void testRequestCompanionSelfManaged(){
		if (!mPackageManager.hasSystemFeature(
				PackageManager.FEATURE_COMPANION_DEVICE_SETUP)) {
			throw new BypassTestException(
					"Device does not have the "
							+ "PackageManager#FEATURE_COMPANION_DEVICE_SETUP feature "
							+ "for this test");
		}
		AssociationRequest request = null;
		request = new AssociationRequest.Builder().setDisplayName("foo")
				.setSelfManaged(true).setForceConfirmation(true).build();


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

	@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @PermissionTest(permission="READ_APP_SPECIFIC_LOCALES", sdkMin=33)
	public void testReadAppSpecificLocales(){
		//if the caller is not an owner of the application, the api raise a security exception.
		try {
			//Check companion app
			systemService(LocaleManager.class).getApplicationLocales(
					"com.android.certifications.niap.permissions.companion");
		} catch (IllegalArgumentException ex){
			//if signed signature is not a platform one, the api may not find the package.
			throw new SecurityException(ex);
		}
	}


	@SuppressLint("PrivateApi")
	@PermissionTest(permission="USE_ATTESTATION_VERIFICATION_SERVICE", sdkMin=33,sdkMax = 35)
	public void testUseAttestationVerificationService(){
		// in VerificationToken token,in ParcelDuration maximumTokenAge,in AndroidFuture resultCallback
		// Intended NPE will be raised
		BinderTransaction.getInstance().invoke(Transacts.ATTESTATION_VERIFICATION_SERVICE,
				Transacts.ATTESTATION_VERIFICATION_DESCRIPTOR,
				"verifyToken", null, null, null);

    }

	@PermissionTest(permission="REQUEST_UNIQUE_ID_ATTESTATION", sdkMin=33)
	public void testRequestUniqueIdAttestation(){
		String keystoreAlias = "test_key";
		KeyGenParameterSpec.Builder builder =
				new KeyGenParameterSpec.Builder(keystoreAlias, PURPOSE_SIGN)
						.setAlgorithmParameterSpec(new ECGenParameterSpec("secp256r1"))
						.setDigests(DIGEST_NONE, DIGEST_SHA256, DIGEST_SHA512)
						.setAttestationChallenge(new byte[128]);
		//setUniqueIeIncluded is a hidden api so need to cast a spell to execute this method
		//
		builder = (KeyGenParameterSpec.Builder)ReflectionUtil.invoke(builder,
				"setUniqueIdIncluded",
				new Class<?>[]{boolean.class}, true);

		KeyGenParameterSpec spec = builder.build();
		KeyStore keyStore = null;
		try {
			keyStore = KeyStore.getInstance("AndroidKeyStore");
			keyStore.load(null);
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM_EC,
					"AndroidKeyStore");
			keyPairGenerator.initialize(spec);
			keyPairGenerator.generateKeyPair();
		} catch (java.security.ProviderException e){
			//If permission is not granted the module raise : java.security.ProviderException: Failed to generate key pair.
			throw new SecurityException(e);
		} catch (KeyStoreException |
                 InvalidAlgorithmParameterException | NoSuchProviderException |
                 CertificateException | IOException | NoSuchAlgorithmException e) {
			throw new UnexpectedTestFailureException(e);
		}
    }

	@PermissionTest(permission="SET_SYSTEM_AUDIO_CAPTION", sdkMin=33)
	public void testSetSystemAudioCaption(){
		ReflectionUtil.invoke(systemService(CaptioningManager.class),
				"setSystemAudioCaptioningEnabled",
				new Class<?>[]{boolean.class}, true);
	}

	@PermissionTest(permission="REVOKE_POST_NOTIFICATIONS_WITHOUT_KILL", sdkMin=33)
	public void testRevokePostNotificationsWithoutKill(){
		@SuppressLint("WrongConstant") Object permissionManager = mContext.getSystemService("permission");
		//revokePostNotificationPermissionWithoutKillForTest( java.lang.String int);
		ReflectionUtil.invoke(permissionManager,
				"revokePostNotificationPermissionWithoutKillForTest",
				new Class<?>[]{String.class,int.class}, mContext.getPackageName(),0);
	}

	@PermissionTest(permission="MODIFY_USER_PREFERRED_DISPLAY_MODE", sdkMin=33)
	public void testModifyUserPreferredDisplayMode(){

		Display.Mode[] modes = systemService(DisplayManager.class).getDisplays()[0].getSupportedModes();
		Display.Mode default_m = systemService(DisplayManager.class).getDisplays()[0].getMode();



		boolean found= false;
		Display.Mode target_m = null;
		for(int i=0;i<modes.length;i++){
			if(modes[i].getModeId()!=default_m.getModeId()) {
				target_m = modes[i];
				found=true;
				break;
			}
		}
		if(!found){
			throw new BypassTestException("display mode for transition was not found");
		}

		BinderTransaction.getInstance().invoke(Transacts.DISPLAY_SERVICE,Transacts.DISPLAY_DESCRIPTOR,
				"setUserPreferredDisplayMode",0,default_m);


		//The method may not find if the app is signing by the platform signing key
		//logger.logInfo(ReflectionUtils.checkDeclaredMethod(mDisplayManager,"setGlobalUser").toString());
		//ReflectionUtil.invoke(mDisplayManager.getClass(),
		//        "setGlobalUserPreferredDisplayMode", mDisplayManager,
		//        new Class[]{Display.Mode.class},mode);



		//Parcel result = BinderTransaction.getInstance().invoke(Transacts.DISPLAY_SERVICE,Transacts.DISPLAY_DESCRIPTOR,
		//		"getUserPreferredDisplayMode",0,default_m);default_m
		//check is changed?

		//systemService(DisplayManager.class).getDisplays()[0]
		//logger.system("set user preffered result: "+target_m.toString());
		//logger.system("set user preffered result: "+p.readInt());
	}

	@PermissionTest(permission="ACCESS_ULTRASOUND", sdkMin=33)
	public void testAccessUltrasound(){
		ReflectionUtil.invoke(systemService(AudioManager.class),
				"isUltrasoundSupported");
	}

	@PermissionTest(permission="CALL_AUDIO_INTERCEPTION", sdkMin=33)
	public void testCallAudioInterception(){
		ReflectionUtil.invoke(systemService(AudioManager.class),
				"isPstnCallAudioInterceptable");

	}

	@PermissionTest(permission="MANAGE_LOW_POWER_STANDBY", sdkMin=33)
	public void testManageLowPowerStandby(){
		ReflectionUtil.invoke(systemService(PowerManager.class),
				"isLowPowerStandbySupported");
	}

	@PermissionTest(permission="ACCESS_BROADCAST_RESPONSE_STATS", sdkMin=33,developmentProtection = true)
	public void testAccessBroadcastResponseStats(){
		ReflectionUtil.invoke(systemService(UsageStatsManager.class),
				"queryBroadcastResponseStats",
				new Class[]{String.class,long.class},mContext.getPackageName(),0);
	}

	@PermissionTest(permission="CHANGE_APP_LAUNCH_TIME_ESTIMATE", sdkMin=33)
	public void testChangeAppLaunchTimeEstimate(){
		ReflectionUtil.invoke(systemService(UsageStatsManager.class),
				"setEstimatedLaunchTimeMillis",
				new Class[]{String.class,long.class},mContext.getPackageName(),1000L);
	}

	@PermissionTest(permission="MANAGE_WEAK_ESCROW_TOKEN", sdkMin=33)
	public void testManageWeakEscrowToken(){
		if (!mPackageManager.hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE)) {
			throw new BypassTestException("This permission requires feature "
					+ PackageManager.FEATURE_AUTOMOTIVE);
		}
		ReflectionUtil.invoke(systemService(KeyguardManager.class),
				"isWeakEscrowTokenActive",
				new Class[]{long.class, UserHandle.class},
				100L, UserHandle.getUserHandleForUid(appUid));
	}

	@PermissionTest(permission="SET_WALLPAPER_DIM_AMOUNT", sdkMin=33)
	public void testSetWallpaperDimAmount(){
		ReflectionUtil.invoke(systemService(WallpaperManager.class),
				"getWallpaperDimAmount");
	}

	@PermissionTest(permission="START_REVIEW_PERMISSION_DECISIONS", sdkMin=33)
	public void testStartReviewPermissionDecisions(){
		if (!mPackageManager.hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE)) {
			throw new BypassTestException("This permission requires feature "
					+ PackageManager.FEATURE_AUTOMOTIVE);
		}
		if(Constants.BYPASS_TESTS_AFFECTING_UI)
			throw new BypassTestException("This test case affects to UI. skip to avoiding ui stuck.");

		//ACTION_REVIEW_PERMISSION_DECISIONS activity is running only on th automotive device.
		String ACTION_REVIEW_PERMISSION_DECISIONS =
				"android.permission.action.REVIEW_PERMISSION_DECISIONS";
		mContext.startActivity(new Intent(ACTION_REVIEW_PERMISSION_DECISIONS)
				.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

	}

	@PermissionTest(permission="MANAGE_CLOUDSEARCH", sdkMin=33)
	public void testManageCloudsearch(){
		//in SearchRequest request, in ICloudSearchManagerCallback.aidl callBack
		Class<?> clazzSearchBuilder = null;
		Object searchRequest = null;
		try {
			clazzSearchBuilder =
					Class.forName("android.app.cloudsearch.SearchRequest$Builder");
			Constructor constructor = clazzSearchBuilder.getConstructor(String.class);
			Object builderObj = constructor.newInstance("test");
			searchRequest = ReflectionUtil.invoke(builderObj, "build");

			BinderTransaction.getInstance().invoke(Transacts.CLOUDSEARCH_SERVICE, Transacts.CLOUDSEARCH_DESCRIPTOR,
					"search",searchRequest,null);
		} catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
			throw new UnexpectedTestFailureException(e);
		}
	}

	@PermissionTest(permission="MANAGE_WALLPAPER_EFFECTS_GENERATION", sdkMin=33)
	public void testManageWallpaperEffectsGeneration(){
		Bitmap.Config conf = Bitmap.Config.ARGB_4444; // see other conf types
		Bitmap bmp = Bitmap.createBitmap(4, 4, conf); // this creates a MUTABLE bitmap
		CinematicEffectRequest request =
				new CinematicEffectRequest("test-wallpaper-effects-generation",
						bmp);
		BinderTransaction.getInstance().invoke(Transacts.WALLPAPER_EFFECTS_GENERATION_SERVICE,
				Transacts.WALLPAPER_EFFECTS_GENERATION_DESCRIPTOR,
				"generateCinematicEffect",
				request,new ICinematicEffectListener.Stub() {
					@Override
					public void onCinematicEffectGenerated(CinematicEffectResponse response) throws RemoteException {

					}
				});
	}

	@PermissionTest(permission="SET_GAME_SERVICE", sdkMin=33)
	public void testSetGameService(){
		BinderTransaction.getInstance().invoke(Transacts.GAME_SERVICE, Transacts.GAME_DESCRIPTOR,
				"setGameServiceProvider", mContext.getPackageName());
	}

	@PermissionTest(permission="ACCESS_FPS_COUNTER", sdkMin=33)
	public void testAccessFpsCounter(){
		//ConditionVariable done = new ConditionVariable();
		BinderTransaction.getInstance().invoke(
				Transacts.WINDOW_SERVICE, Transacts.WINDOW_DESCRIPTOR,
				"registerTaskFpsCallback", 1, new ITaskFpsCallback.Stub() {
					@Override
					public void onFpsReported(float fps) throws RemoteException {
						//logger.system("fps reported=>"+fps);
						//done.open();
					}
				});
		//done.block(5000);
	}

	@PermissionTest(permission="MANAGE_GAME_ACTIVITY", sdkMin=33)
	public void testManageGameActivity(){

		BinderTransaction.getInstance().invoke(Transacts.ACTIVITY_TASK_SERVICE, Transacts.ACTIVITY_TASK_DESCRIPTOR,
				"startActivityFromGameSession",
				getActivityToken(),mContext.getPackageName(),"",
				0,0,new Intent().setClass(mContext, TestActivity.class),0,0);
	}

	@PermissionTest(permission="LAUNCH_DEVICE_MANAGER_SETUP", sdkMin=33)
	public void testLaunchDeviceManagerSetup(){

		//DeviceManger#ACTION_ROLE_HOLDER_PROVISION_FINALIZATION
		//DeviceManger#ACTION_ROLE_HOLDER_PROVISION_MANAGED_DEVICE_FROM_TRUSTED_SOURCE"
		//DeviceManger#ACTION_ROLE_HOLDER_PROVISION_MANAGED_PROFILE

		Intent ii = new Intent("android.app.action.ROLE_HOLDER_PROVISION_MANAGED_PROFILE");
		ResolveInfo resolveInfo =mPackageManager.resolveActivity(ii, 0);
		if(resolveInfo == null){
			throw new BypassTestException("the system does not have corresponding activity to" +
					" ROLE_HOLDER_PROVISION_MANAGED_PROFILE action. Let's skip it...");
		}
		//Simply call the acitivity on Companion app and verify whetehr it is guarded.
		Intent featuresIntent = new Intent("android.app.action.ROLE_HOLDER_PROVISION_MANAGED_PROFILE");
		featuresIntent.setComponent(new
				ComponentName("com.android.certifications.niap.permissions.companion",
				"com.android.certifications.niap.permissions.companion.PreProvisioningActivity"));
		mActivity.startActivity(featuresIntent);

		// Note :
		// The action for cloud dpc manager is guarded by this permission.
		// But this type of permissions are not working with platform signatue.
		// So you could not find the affect of the permission with the test above.
		//
		// If you'd like to test it with the platform signature, you can test the codes below.
		//
		// In this case, if the permission is not granted you'll got a blank chooser.
		// And if it's granted PreProvisioningActivity will be launched by default.
		// If we succeeded to call that activity, We can get an event regarding
		// selection of the chooser on BroadCastReciver.
		// We obsolate it because the test requires user interactions.
		//

		//Obsoleted Test Cade
	}

	@PermissionTest(permission="UPDATE_DEVICE_MANAGEMENT_RESOURCES", sdkMin=33)
	public void testUpdateDeviceManagementResources(){
		List<Object> array = new ArrayList<Object>();
		BinderTransaction.getInstance().invoke(Transacts.DEVICE_POLICY_SERVICE, Transacts.DEVICE_POLICY_DESCRIPTOR,
				"setStrings", array);
	}

	@PermissionTest(permission="READ_SAFETY_CENTER_STATUS", sdkMin=33)
	public void testReadSafetyCenterStatus(){

		Class<?> clazzSaftyCenter = null;
		try {
			clazzSaftyCenter = Class.forName("android.safetycenter.SafetyCenterManager");
			Object safetyCenter = mContext.getSystemService(clazzSaftyCenter);
			ReflectionUtil.invoke(safetyCenter, "isSafetyCenterEnabled");
		} catch (ClassNotFoundException e){// | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
			throw new UnexpectedTestFailureException(e);
		}
	}

	@PermissionTest(permission="TIS_EXTENSION_INTERFACE", sdkMin=33)
	public void testTisExtensionInterface(){
		//The tv_input service guarded by this permission is not available on this device
		BinderTransaction.getInstance().invoke(Transacts.TV_INPUT_SERVICE, Transacts.TV_INPUT_DESCRIPTOR,
				"getAvailableExtensionInterfaceNames");
	}

	@PermissionTest(permission="MAKE_UID_VISIBLE", sdkMin=33)
	public void testMakeUidVisible(){
		//mPackageManager.makeUidVisible(120000,130000);
		BinderTransaction.getInstance().invoke(Transacts.PACKAGE_SERVICE, Transacts.PACKAGE_DESCRIPTOR,
				"makeUidVisible",1200000,1300000);
	}

	@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @PermissionTest(permission="MANAGE_ETHERNET_NETWORKS", sdkMin=33)
	public void testManageEthernetNetworks(){
		//disableInterface,enableInterface,updateConfiguration
		//Those apis always raise UnsupportedOperationException
		//if the system doesn't have automotive feature
		if (!mPackageManager.hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE)) {
			throw new BypassTestException("This permission requires the feature "
					+ PackageManager.FEATURE_AUTOMOTIVE);
		}

		@SuppressLint("WrongConstant") Object ethernetManager =
				mContext.getSystemService("ethernet");
		Class<?> eurClazz = null;
		Class<?> eurBuilderClazz = null;
		try {
			//To construct NetworkCapabilities as a parameter of EthernetNetworkUpdateRequest
			Class<?> ncBuilderClazz = null;
			ncBuilderClazz = Class.forName("android.net.NetworkCapabilities$Builder");
			Object ncBuilderObj = ReflectionUtil.invoke(ncBuilderClazz,
					"withoutDefaultCapabilities");
			NetworkCapabilities nc  = (NetworkCapabilities) ReflectionUtil.invoke(ncBuilderObj,
					"build");
			//set up EthernetNetworkUpdateRequest
			eurClazz = Class.forName("android.net.EthernetNetworkUpdateRequest");
			eurBuilderClazz = Class.forName("android.net.EthernetNetworkUpdateRequest$Builder");
			Constructor<?> eurBuilderConstructor = eurBuilderClazz.getConstructor();
			Object eurBuilderObj = eurBuilderConstructor.newInstance();
			eurBuilderObj = ReflectionUtil.invoke(eurBuilderObj,
					"setIpConfiguration",
					new Class<?>[]{IpConfiguration.class}, new IpConfiguration.Builder().build());

			eurBuilderObj = ReflectionUtil.invoke(eurBuilderClazz,
					"setNetworkCapabilities", eurBuilderObj,
					new Class<?>[]{NetworkCapabilities.class}, nc);

			Object eurObj =  ReflectionUtil.invoke(eurBuilderClazz,
					"build", eurBuilderObj,
					new Class<?>[]{});

			ReflectionUtil.invoke(ethernetManager.getClass(),
					"updateConfiguration", ethernetManager,
					new Class[]{String.class,eurClazz,Executor.class, OutcomeReceiver.class},
					"test123abc789", eurObj, null, null
			);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
	@PermissionTest(permission="LOCATION_HARDWARE")
	public void testLocationHardware(){
		ReflectionUtil.invoke(systemService(LocationManager.class), "flushGnssBatch");
	}

	@PermissionTest(permission="MANAGE_ACTIVITY_TASKS", sdkMin=33)
	public void testManageActivityTasks(){
		BinderTransaction.getInstance().invoke(Transacts.ACTIVITY_SERVICE,
				Transacts.ACTIVITY_DESCRIPTOR,
				"stopAppForUser","test.packagename",0);

	}

	@RequiresApi(api = Build.VERSION_CODES.S)
    @PermissionTest(permission="LISTEN_ALWAYS_REPORTED_SIGNAL_STRENGTH", sdkMin=33)
	public void testListenAlwaysReportedSignalStrength(){

		//The way to using the listner was obsolated after android T, so let me choose using this option instead
		//https://cs.android.com/android/platform/superproject/+/master:cts/tests/tests/telephony/current/src/android/telephony/cts/PhoneStateListenerTest.java;l=328;drc=e64188140ba71c7b7424b044119b37af1dde6609?=4186
		//problem : if the signature does not match it always fail,because the method also checks MODIFY_PHONE_STATE permission

		SignalStrengthUpdateRequest.Builder builder = new SignalStrengthUpdateRequest.Builder()
				.setSignalThresholdInfos(Collections.EMPTY_LIST);

		builder = (SignalStrengthUpdateRequest.Builder)
				ReflectionUtil.invoke(builder,
						"setSystemThresholdReportingRequestedWhileIdle",
						new Class<?>[]{boolean.class},true);
		SignalStrengthUpdateRequest request = builder.build();
		try {
			systemService(TelephonyManager.class).setSignalStrengthUpdateRequest(request);
		} catch (IllegalStateException ex){
			logger.info("Expected:"+ex.getMessage());
		}
	}

	@PermissionTest(permission="MANAGE_BIOMETRIC_DIALOG", sdkMin=33)
	public void testManageBiometricDialog(){
		BinderTransaction.getInstance().invoke(Transacts.STATUS_BAR_SERVICE,
				Transacts.STATUS_BAR_DESCRIPTOR,
				"onBiometricHelp", 0, "test");
	}
}









