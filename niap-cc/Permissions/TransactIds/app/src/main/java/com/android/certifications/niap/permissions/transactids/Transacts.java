/*
 * Copyright 2020 The Android Open Source Project
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

package com.android.certifications.niap.permissions.transactids;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains constants for the system services and descriptors taken from the Permission Test Tool's
 * Transacts class. These values are used to reflectively query the descriptor Stub classes for the
 * IDs of the transact methods invoked by the Permission Test Tool.
 */
public class Transacts {
    private static final String TAG = "TransactIds";

    public static final String TRANSACT_PREFIX = "TRANSACTION_";

    // Following are the service and descriptor values for exported system services.
    public static final String ACCESSIBILITY_SERVICE = Context.ACCESSIBILITY_SERVICE;
    public static final String ACCESSIBILITY_DESCRIPTOR =
            "android.view.accessibility.IAccessibilityManager";

    public static final String ACTIVITY_CLIENT_DESCRIPTOR = "android.app.IActivityClientController";

    public static final String ACTIVITY_SERVICE = Context.ACTIVITY_SERVICE;
    public static final String ACTIVITY_DESCRIPTOR = "android.app.IActivityManager";

    public static final String ACTIVITY_TASK_SERVICE = "activity_task";
    public static final String ACTIVITY_TASK_DESCRIPTOR = "android.app.IActivityTaskManager";

    public static final String ALARM_SERVICE = Context.ALARM_SERVICE;
    public static final String ALARM_DESCRIPTOR = "android.app.IAlarmManager";

    public static final String APP_OPS_SERVICE = Context.APP_OPS_SERVICE;
    public static final String APP_OPS_DESCRIPTOR = "com.android.internal.app.IAppOpsService";

    public static final String APPWIDGET_SERVICE = Context.APPWIDGET_SERVICE;
    public static final String APPWIDGET_DESCRIPTOR =
            "com.android.internal.appwidget.IAppWidgetService";

    public static final String AUDIO_SERVICE = Context.AUDIO_SERVICE;
    public static final String AUDIO_DESCRIPTOR = "android.media.IAudioService";

    public static final String AUTH_SERVICE = "auth";
    public static final String AUTH_DESCRIPTOR = "android.hardware.biometrics.IAuthService";

    public static final String BACKUP_SERVICE = "backup";
    public static final String BACKUP_DESCRIPTOR = "android.app.backup.IBackupManager";

    public static final String BATTERY_STATS_SERVICE = "batterystats";
    public static final String BATTERY_STATS_DESCRIPTOR = "com.android.internal.app.IBatteryStats";

    public static final String BIOMETRIC_SERVICE = Context.BIOMETRIC_SERVICE;
    public static final String BIOMETRIC_DESCRIPTOR =
            "android.hardware.biometrics.IBiometricService";

    public static final String CAMERA_SERVICE = "media.camera";
    public static final String CAMERA_DESCRIPTOR = "android.hardware.ICameraService";

    public static final String CLIPBOARD_SERVICE = Context.CLIPBOARD_SERVICE;
    public static final String CLIPBOARD_DESCRIPTOR = "android.content.IClipboard";

    public static final String COMPANION_DEVICE_SERVICE = Context.COMPANION_DEVICE_SERVICE;
    public static final String COMPANION_DEVICE_DESCRIPTOR =
            "android.companion.ICompanionDeviceManager";

    public static final String CONNECTIVITY_SERVICE = Context.CONNECTIVITY_SERVICE;
    public static final String CONNECTIVITY_DESCRIPTOR = "android.net.IConnectivityManager";

    public static final String CROSS_PROFILE_APPS_SERVICE = Context.CROSS_PROFILE_APPS_SERVICE;
    public static final String CROSS_PROFILE_APPS_DESCRIPTOR =
            "android.content.pm.ICrossProfileApps";

    public static final String DEVICE_POLICY_SERVICE = Context.DEVICE_POLICY_SERVICE;
    public static final String DEVICE_POLICY_DESCRIPTOR = "android.app.admin.IDevicePolicyManager";

    public static final String DEVICE_STATE_SERVICE = "device_state";
    public static final String DEVICE_STATE_DESCRIPTOR = "android.hardware.devicestate.IDeviceStateManager";

    public static final String DISPLAY_SERVICE = Context.DISPLAY_SERVICE;
    public static final String DISPLAY_DESCRIPTOR = "android.hardware.display.IDisplayManager";

    public static final String DOMAIN_VERIFICATION_SERVICE = Context.DOMAIN_VERIFICATION_SERVICE;
    public static final String DOMAIN_VERIFICATION_DESCRIPTOR =
            "android.content.pm.verify.domain.IDomainVerificationManager";

    public static final String DREAMS_SERVICE = "dreams";
    public static final String DREAMS_DESCRIPTOR = "android.service.dreams.IDreamManager";

    public static final String DROPBOX_SERVICE = Context.DROPBOX_SERVICE;
    public static final String DROPBOX_DESCRIPTOR =
            "com.android.internal.os.IDropBoxManagerService";

    public static final String EUICC_CONTROLLER_SERVICE = "econtroller";
    public static final String EUICC_CONTROLLER_DESCRIPTOR =
            "com.android.internal.telephony.euicc.IEuiccController";

    public static final String FACE_SERVICE = "face";
    public static final String FACE_DESCRIPTOR = "android.hardware.face.IFaceService";

    public static final String FINGERPRINT_SERVICE = Context.FINGERPRINT_SERVICE;
    public static final String FINGERPRINT_DESCRIPTOR =
            "android.hardware.fingerprint.IFingerprintService";

    public static final String FONT_SERVICE = "font";
    public static final String FONT_DESCRIPTOR = "com.android.internal.graphics.fonts.IFontManager";

    public static final String GAME_SERVICE = Context.GAME_SERVICE;
    public static final String GAME_DESCRIPTOR = "android.app.IGameManagerService";

    public static final String INPUT_SERVICE = Context.INPUT_SERVICE;
    public static final String INPUT_DESCRIPTOR = "android.hardware.input.IInputManager";

    public static final String ISUB_SERVICE = "isub";
    public static final String ISUB_DESCRIPTOR = "com.android.internal.telephony.ISub";

    public static final String KEY_CHAIN_DESCRIPTOR = "android.security.IKeyChainService";

    public static final String LOCK_SETTINGS_SERVICE = "lock_settings";
    public static final String LOCK_SETTINGS_DESCRIPTOR =
            "com.android.internal.widget.ILockSettings";

    public static final String MEDIA_PROJECTION_SERVICE = Context.MEDIA_PROJECTION_SERVICE;
    public static final String MEDIA_PROJECTION_DESCRIPTOR =
            "android.media.projection.IMediaProjectionManager";

    public static final String MEDIA_SESSION_SERVICE = "media_session";
    public static final String MEDIA_SESSION_DESCRIPTOR = "android.media.session.ISessionManager";

    public static final String MOUNT_SERVICE = "mount";
    public static final String MOUNT_DESCRIPTOR = "android.os.storage.IStorageManager";

    public static final String MUSIC_RECOGNITION_SERVICE = "music_recognition";
    public static final String MUSIC_RECOGNITION_DESCRIPTOR =
            "android.media.musicrecognition.IMusicRecognitionManager";

    public static final String NET_POLICY_SERVICE = "netpolicy";
    public static final String NET_POLICY_DESCRIPTOR = "android.net.INetworkPolicyManager";

    public static final String NETWORK_MANAGEMENT_SERVICE = "network_management";
    public static final String NETWORK_MANAGEMENT_DESCRIPTOR =
            "android.os.INetworkManagementService";

    public static final String NETWORK_STATS_SERVICE = Context.NETWORK_STATS_SERVICE;
    public static final String NETWORK_STATS_DESCRIPTOR = "android.net.INetworkStatsService";

    public static final String NFC_SERVICE = Context.NFC_SERVICE;
    public static final String NFC_DESCRIPTOR = "android.nfc.INfcAdapter";

    public static final String NOTIFICATION_SERVICE = Context.NOTIFICATION_SERVICE;
    public static final String NOTIFICATION_DESCRIPTOR = "android.app.INotificationManager";

    public static final String PACKAGE_SERVICE = "package";
    public static final String PACKAGE_DESCRIPTOR = "android.content.pm.IPackageManager";

    public static final String PEOPLE_SERVICE = Context.PEOPLE_SERVICE;
    public static final String PEOPLE_DESCRIPTOR = "android.app.people.IPeopleManager";

    public static final String PERMISSION_CHECKER_SERVICE = "permission_checker";
    public static final String PERMISSION_CHECKER_DESCRIPTOR =
            "android.permission.IPermissionChecker";

    public static final String PERMISSION_MANAGER_SERVICE = "permissionmgr";
    public static final String PERMISSION_MANAGER_DESCRIPTOR =
            "android.permission.IPermissionManager";

    public static final String PLATFORM_COMPAT_SERVICE = "platform_compat";
    public static final String PLATFORM_COMPAT_DESCRIPTOR =
            "com.android.internal.compat.IPlatformCompat";

    public static final String POWER_SERVICE = Context.POWER_SERVICE;
    public static final String POWER_DESCRIPTOR = "android.os.IPowerManager";

    public static final String REBOOT_READINESS_SERVICE = "reboot_readiness";
    public static final String REBOOT_READINESS_DESCRIPTOR =
            "android.scheduling.IRebootReadinessManager";

    public static final String RESOURCE_MANAGER_SERVICE = "media.resource_manager";
    public static final String RESOURCE_MANAGER_DESCRIPTOR =
            "android.media.IResourceManagerService";

    public static final String RESOURCE_OBSERVER_SERVICE = "media.resource_observer";
    public static final String RESOURCE_OBSERVER_DESCRIPTOR =
            "android.media.IResourceObserverService";

    public static final String ROLE_SERVICE = Context.ROLE_SERVICE;
    public static final String ROLE_DESCRIPTOR = "android.app.role.IRoleManager";

    public static final String ROLLBACK_SERVICE = "rollback";
    public static final String ROLLBACK_DESCRIPTOR = "android.content.rollback.IRollbackManager";

    public static final String SENSOR_PRIVACY_SERVICE = "sensor_privacy";
    public static final String SENSOR_PRIVACY_DESCRIPTOR = "android.hardware.ISensorPrivacyManager";

    public static final String SHORTCUT_SERVICE = Context.SHORTCUT_SERVICE;
    public static final String SHORTCUT_DESCRIPTOR = "android.content.pm.IShortcutService";

    public static final String SLICE_SERVICE = "slice";
    public static final String SLICE_DESCRIPTOR = "android.app.slice.ISliceManager";

    public static final String SMART_SPACE_SERVICE = "smartspace";
    public static final String SMART_SPACE_DESCRIPTOR = "android.app.smartspace.ISmartspaceManager";

    public static final String SOUND_TRIGGER_SERVICE = "soundtrigger";
    public static final String SOUND_TRIGGER_DESCRIPTOR =
            "com.android.internal.app.ISoundTriggerService";

    public static final String SOUND_TRIGGER_SESSION_DESCRIPTOR =
            "com.android.internal.app.ISoundTriggerSession";

    public static final String SPEECH_RECOGNITION_SERVICE = "speech_recognition";
    public static final String SPEECH_RECOGNITION_DESCRIPTOR =
            "android.speech.IRecognitionServiceManager";

    public static final String STATUS_BAR_SERVICE = "statusbar";
    public static final String STATUS_BAR_DESCRIPTOR =
            "com.android.internal.statusbar.IStatusBarService";

    public static final String SURFACE_FLINGER_SERVICE = "SurfaceFlinger";
    public static final String SURFACE_FLINGER_DESCRIPTOR = "android.ui.ISurfaceComposer";

    public static final String TELEPHONY_IMS_SERVICE = "telephony_ims";
    public static final String TELEPHONY_IMS_DESCRIPTOR =
            "android.telephony.ims.aidl.IImsRcsController";

    public static final String TELEPHONY_SERVICE = Context.TELEPHONY_SERVICE;
    public static final String TELEPHONY_DESCRIPTOR = "com.android.internal.telephony.ITelephony";

    public static final String TIME_DETECTOR_SERVICE = "time_detector";
    public static final String TIME_DETECTOR_DESCRIPTOR =
            "android.app.timedetector.ITimeDetectorService";

    public static final String TRANSLATION_SERVICE = "translation";
    public static final String TRANSLATION_DESCRIPTOR =
            "android.view.translation.ITranslationManager";

    public static final String TRUST_SERVICE = "trust";
    public static final String TRUST_DESCRIPTOR = "android.app.trust.ITrustManager";

    public static final String TV_INPUT_SERVICE = "tv_input";
    public static final String TV_INPUT_DESCRIPTOR = "android.media.tv.ITvInputManager";

    public static final String UI_MODE_SERVICE = Context.UI_MODE_SERVICE;
    public static final String UI_MODE_DESCRIPTOR = "android.app.IUiModeManager";

    public static final String URI_GRANTS_SERVICE = "uri_grants";
    public static final String URI_GRANTS_DESCRIPTOR = "android.app.IUriGrantsManager";

    public static final String USB_SERVICE = Context.USB_SERVICE;
    public static final String USB_DESCRIPTOR = "android.hardware.usb.IUsbManager";

    public static final String UWB_SERVICE = "uwb";
    public static final String UWB_DESCRIPTOR = "android.uwb.IUwbAdapter";

    public static final String VIBRATOR_SERVICE = Context.VIBRATOR_SERVICE;
    public static final String VIBRATOR_DESCRIPTOR = "android.os.IVibratorService";

    public static final String VOICE_INTERACTION_SERVICE = "voiceinteraction";
    public static final String VOICE_INTERACTION_DESCRIPTOR =
            "com.android.internal.app.IVoiceInteractionManagerService";

    public static final String VPN_SERVICE = Context.VPN_MANAGEMENT_SERVICE;
    public static final String VPN_DESCRIPTOR = "android.net.IVpnManager";

    public static final String VR_SERVICE = "vrmanager";
    public static final String VR_DESCRIPTOR = "android.service.vr.IVrManager";

    public static final String WALLPAPER_SERVICE = Context.WALLPAPER_SERVICE;
    public static final String WALLPAPER_DESCRIPTOR = "android.app.IWallpaperManager";

    public static final String WIFI_SERVICE = Context.WIFI_SERVICE;
    public static final String WIFI_DESCRIPTOR = "android.net.wifi.IWifiManager";

    public static final String WINDOW_SERVICE = Context.WINDOW_SERVICE;
    public static final String WINDOW_DESCRIPTOR = "android.view.IWindowManager";

    //For Android T
    public static final String AMBIENT_CONTEXT_MANAGER_SERVICE =
            "ambient_context";
    public static final String AMBIENT_CONTEXT_MANAGER_DESCRIPTOR =
            "android.app.ambientcontext.IAmbientContextManager";
    public static final String queryServiceStatus="queryServiceStatus";

    public static final String SAFETY_CENTER_MANAGER_SERVICE =
            "safety_center";
    public static final String SAFETY_CENTER_MANAGER_MANAGER_DESCRIPTOR =
            "android.safetycenter.ISafetyCenterManager";
    public static final String isSafetyCenterEnabled="isSafetyCenterEnabled";
    public static final String getSafetyCenterConfig="getSafetyCenterConfig";
    public static final String getSafetySourceData="getSafetySourceData";

    public static final String VIRTUAL_DEVICE_MANAGER_SERVICE = "virtualdevice";
    public static final String VIRTUAL_DEVICE_MANAGER_DESCRIPTOR =
            "android.companion.virtual.IVirtualDeviceManager";
    public static final String createVirtualDevice="createVirtualDevice";
    //For SUBSCRIBE_TO_KEYGUARD_LOCKED_STATE permission of IWindowManger
    public static final String addKeyguardLockedStateListener
            ="addKeyguardLockedStateListener";
    public static final String removeKeyguardLockedStateListener
            ="removeKeyguardLockedStateListener";

    public static final String ATTESTATION_VERIFICATION_SERVICE = "attestation_verification";
    public static final String ATTESTATION_VERIFICATION_DESCRIPTOR =
            "android.security.attestationverification.IAttestationVerificationManagerService";

    public static final String verifyAttestation = "verifyAttestation";
    public static final String verifyToken = "verifyToken";

    //WindowManager
    public static final String setInTouchMode = "setInTouchMode";
    public static final String getInTouchMode = "getInTouchMode";

    //CloudSearchManager
    public static final String CLOUDSEARCH_SERVICE = "cloudsearch";//Context.CLOUD_SEARCH
    public static final String CLOUDSEARCH_DESCRIPTOR =
            "android.app.cloudsearch.ICloudSearchManager";
    public static final String search="search";
    //WallPaperEffectsGenerationManager
    public static final String WALLPAPER_EFFECTS_GENERATION_SERVICE =
            "wallpaper_effects_generation";;
    public static final String WALLPAPER_EFFECTS_GENERATION_DESCRIPTOR
            = "android.app.wallpapereffectsgeneration.IWallpaperEffectsGenerationManager";
    //generateCinematicEffect(in CinematicEffectRequest request,in ICinematicEffectListener listener);
    public static final String generateCinematicEffect="generateCinematicEffect";
    //ClibboardService#showAccessNotificationLocked(String callingPackage, int uid, @UserIdInt int userId,
    //            PerUserClipboard clipboard) {
    public static final String showAccessNotificationLocked="showAccessNotificationLocked";
    //GameManagerService#setGameServiceProvider(@Nullable String packageName)
    public static final String setGameServiceProvider="setGameServiceProvider";
    public static final String createGameSession="createGameSession";
    //WindowManagerService#registerTaskFpsCallback(@IntRange(from = 0) int taskId,ITaskFpsCallback callback)
    public static final String registerTaskFpsCallback="registerTaskFpsCallback";

    //TVManager
    public static final String getAvailableExtensionInterfaceNames="getAvailableExtensionInterfaceNames";
    //PackageManagerService#makeUidVisible
    public static final String makeUidVisible="makeUidVisible";
    //AppOpsService
    public static final String getHistoricalOps="getHistoricalOps";
    //DevicePolicyManager#
    public static final String setStrings="setStrings";
    public static final String getString="getString";
    public static final String getPermittedInputMethodsAsUser="getPermittedInputMethodsAsUser";

    public static final String dispatchMessage="dispatchMessage";
    public static final String startActivityFromGameSession="startActivityFromGameSession";
    public static final String getWifiSsidPolicy="getWifiSsidPolicy";
    //AudioServcie#forceRemoteSubmixFullVolume
    public static final String forceRemoteSubmixFullVolume="forceRemoteSubmixFullVolume";
    public static final String isConvertibleToFBE="isConvertibleToFBE";
    public static final String setProfileOwnerOnOrganizationOwnedDevice="setProfileOwnerOnOrganizationOwnedDevice";
    public static final String cancelStateRequest="cancelStateRequest";
    public static final String setUserPreferredDisplayMode="setUserPreferredDisplayMode";
    public static final String stopAppForUser="stopAppForUser";

    public static final String ETHERNET_MANAGER_SERVICE = "ethernet";
    public static final String ETHERNET_MANAGER_DESCRIPTOR =
            "android.net.IEthernetManager";
    //public static final String stopAppForUser


    // Following are constants for transact methods that are invoked as part of permission tests.
    public static final String getVtDataUsage = "getVtDataUsage";
    public static final String getNextEntry = "getNextEntry";
    public static final String cancelAuthentication = "cancelAuthentication";
    public static final String setAlwaysOnEffect = "setAlwaysOnEffect";
    public static final String getDoNotAskCredentialsOnBoot = "getDoNotAskCredentialsOnBoot";
    public static final String setRecentsVisibility = "setRecentsVisibility";
    public static final String stopFreezingScreen = "stopFreezingScreen";
    public static final String registerListener = "registerListener";
    public static final String getKeyphraseSoundModel = "getKeyphraseSoundModel";
    public static final String requestEmbeddedSubscriptionInfoListRefresh =
            "requestEmbeddedSubscriptionInfoListRefresh";
    public static final String getAssistContextExtras = "getAssistContextExtras";
    public static final String getAlwaysOnVpnPackage = "getAlwaysOnVpnPackage";
    public static final String noteStartAudio = "noteStartAudio";
    public static final String tryPointerSpeed = "tryPointerSpeed";
    public static final String getMoveStatus = "getMoveStatus";
    public static final String setTime = "setTime";
    public static final String getVrModeState = "getVrModeState";
    public static final String takePersistableUriPermission = "takePersistableUriPermission";
    public static final String markProfileOwnerOnOrganizationOwnedDevice =
            "markProfileOwnerOnOrganizationOwnedDevice";
    public static final String registerNetworkStatsProvider = "registerNetworkStatsProvider";
    public static final String isRecognitionActive = "isRecognitionActive";
    public static final String clearWindowContentFrameStats = "clearWindowContentFrameStats";
    public static final String isPackageDeviceAdminOnAnyUser = "isPackageDeviceAdminOnAnyUser";
    public static final String benchmark = "benchmark";
    public static final String hideBiometricDialog = "hideBiometricDialog";
    public static final String onBiometricHelp = "onBiometricHelp";
    public static final String setAirplaneMode = "setAirplaneMode";
    public static final String addOnPermissionsChangeListener = "addOnPermissionsChangeListener";
    public static final String thawRotation = "thawRotation";
    public static final String setWifiEnabled = "setWifiEnabled";
    public static final String setPowerSaveMode = "setPowerSaveMode";
    public static final String getUidPolicy = "getUidPolicy";
    public static final String getActiveServiceComponentName = "getActiveServiceComponentName";
    public static final String isVibrating = "isVibrating";
    public static final String requestColorMode = "requestColorMode";
    public static final String resetApplicationPreferences = "resetApplicationPreferences";
    public static final String AMdismissKeyguard = "AMdismissKeyguard";
    public static final String performIdleMaintenance = "performIdleMaintenance";
    public static final String updateLockTaskPackages = "updateLockTaskPackages";
    public static final String grantPermissionFromUser = "grantPermissionFromUser";
    public static final String updateKeyphraseSoundModel = "updateKeyphraseSoundModel";
    public static final String setTemporaryBrightness = "setTemporaryBrightness";
    public static final String setTemporaryAutoBrightnessAdjustment =
            "setTemporaryAutoBrightnessAdjustment";
    public static final String resumeAppSwitches = "resumeAppSwitches";
    public static final String bootFinished = "bootFinished";
    public static final String startWifiDisplayScan = "startWifiDisplayScan";
    public static final String resetTimeout = "resetTimeout";
    public static final String isAutoRevokeWhitelisted = "isAutoRevokeWhitelisted";
    public static final String setTimeZone = "setTimeZone";
    public static final String reportEnabledTrustAgentsChanged = "reportEnabledTrustAgentsChanged";
    public static final String clearOverridesForTest = "clearOverridesForTest";
    public static final String isDreaming = "isDreaming";
    public static final String setDeviceOwner = "setDeviceOwner";
    public static final String addListener = "addListener";
    public static final String setPersistentVrModeEnabled = "setPersistentVrModeEnabled";
    public static final String unhandledBack = "unhandledBack";
    public static final String grantDefaultPermissionsToActiveLuiApp =
            "grantDefaultPermissionsToActiveLuiApp";
    public static final String setFrontActivityScreenCompatMode =
            "setFrontActivityScreenCompatMode";
    public static final String isNotificationPolicyAccessGrantedForPackage =
            "isNotificationPolicyAccessGrantedForPackage";
    public static final String enableLocationUpdates = "enableLocationUpdates";
    public static final String setAlwaysFinish = "setAlwaysFinish";
    public static final String addKeyboardLayoutForInputDevice = "addKeyboardLayoutForInputDevice";
    public static final String setWallpaper = "setWallpaper";
    public static final String setWallpaperComponent = "setWallpaperComponent";
    public static final String setStandbyEnabled = "setStandbyEnabled";
    public static final String getCacheSizeBytes = "getCacheSizeBytes";
    public static final String setUserRestriction = "setUserRestriction";
    public static final String monitorGestureInput = "monitorGestureInput";
    public static final String moveActivityTaskToBack = "moveActivityTaskToBack";
    public static final String getRuntimePermissionsVersion = "getRuntimePermissionsVersion";
    public static final String getEncryptionState = "getEncryptionState";
    public static final String startLoading = "startLoading";
    public static final String signalPersistentProcesses = "signalPersistentProcesses";
    public static final String queryCratesForUser = "queryCratesForUser";
    public static final String isAudioServerRunning = "isAudioServerRunning";
    public static final String overridePendingAppTransitionRemote =
            "overridePendingAppTransitionRemote";
    public static final String removePortAssociation = "removePortAssociation";
    public static final String startActivityAsCaller = "startActivityAsCaller";
    public static final String movePackage = "movePackage";
    public static final String retainSubscriptionsForFactoryReset =
            "retainSubscriptionsForFactoryReset";
    public static final String isInTabletMode = "isInTabletMode";
    public static final String getRingtonePlayer = "getRingtonePlayer";
    public static final String setDumpHeapDebugLimit = "setDumpHeapDebugLimit";
    public static final String setHasTopUi = "setHasTopUi";
    public static final String startNattKeepalive = "startNattKeepalive";
    public static final String dismissKeyguard = "dismissKeyguard";
    public static final String getIntentForIntentSender = "getIntentForIntentSender";
    public static final String generateChallenge = "generateChallenge";
    public static final String getSubscriptionPlans = "getSubscriptionPlans";
    public static final String getTaskDescription = "getTaskDescription";
    public static final String isPackageStateProtected = "isPackageStateProtected";
    public static final String allowDebugging = "allowDebugging";
    public static final String installExistingPackageAsUser = "installExistingPackageAsUser";
    public static final String tether = "tether";
    public static final String registerUiTestAutomationService = "registerUiTestAutomationService";
    public static final String startActivityFromRecents = "startActivityFromRecents";
    public static final String getActiveNetworkForUid = "getActiveNetworkForUid";
    public static final String getAwakeTimeBattery = "getAwakeTimeBattery";
    public static final String releasePersistableUriPermission = "releasePersistableUriPermission";
    public static final String getWifiApConfiguration = "getWifiApConfiguration";
    public static final String requestBugReport = "requestBugReport";
    public static final String reportChangeByUid = "reportChangeByUid";
    public static final String overridePid = "overridePid";
    public static final String getFrontActivityScreenCompatMode =
            "getFrontActivityScreenCompatMode";
    public static final String setRingtonePlayer = "setRingtonePlayer";
    public static final String setAnimationScale = "setAnimationScale";
    public static final String getHarmfulAppWarning = "getHarmfulAppWarning";
    public static final String unregisterTrustListener = "unregisterTrustListener";
    public static final String updateConfiguration = "updateConfiguration";
    public static final String shutdown = "shutdown";
    public static final String appNotRespondingViaProvider = "appNotRespondingViaProvider";
    public static final String getActiveProjectionInfo = "getActiveProjectionInfo";
    public static final String temporaryEnableAccessibilityStateUntilKeyguardRemoved =
            "temporaryEnableAccessibilityStateUntilKeyguardRemoved";
    public static final String setActiveAdmin = "setActiveAdmin";
    public static final String requestUserActivityNotification = "requestUserActivityNotification";
    public static final String bindBackupAgent = "bindBackupAgent";
    public static final String awaken = "awaken";
    public static final String getGrantedUriPermissions = "getGrantedUriPermissions";
    public static final String cancelEnrollment = "cancelEnrollment";
    public static final String setBindAppWidgetPermission = "setBindAppWidgetPermission";
    public static final String stopSoftAp = "stopSoftAp";
    public static final String getContentProviderExternal = "getContentProviderExternal";
    public static final String registerShortcutKey = "registerShortcutKey";
    public static final String clearInteractAcrossProfilesAppOps =
            "clearInteractAcrossProfilesAppOps";
    public static final String setDataSaverModeEnabled = "setDataSaverModeEnabled";
    public static final String hasEnrolledBiometrics = "hasEnrolledBiometrics";
    public static final String reboot = "reboot";
    public static final String setOnMediaKeyListener = "setOnMediaKeyListener";
    public static final String inputDispatchingTimedOut = "inputDispatchingTimedOut";
    public static final String getZenRules = "getZenRules";
    public static final String notifySystemEvent = "notifySystemEvent";
    public static final String enableInputDevice = "enableInputDevice";
    public static final String getWindowToken = "getWindowToken";
    public static final String noteOperation = "noteOperation";
    public static final String screenshotWallpaper = "screenshotWallpaper";
    public static final String onApplicationActive = "onApplicationActive";
    public static final String setTouchCalibrationForInputDevice =
            "setTouchCalibrationForInputDevice";
    public static final String installCaCert = "installCaCert";
    public static final String setProcessLimit = "setProcessLimit";
    public static final String removeWindowToken = "removeWindowToken";
    public static final String setPictureInPictureActionReplacingConnection =
            "setPictureInPictureActionReplacingConnection";
    public static final String setSensorPrivacy = "setSensorPrivacy";
    public static final String setOnVolumeKeyLongPressListener = "setOnVolumeKeyLongPressListener";
    public static final String setPowerSaveModeEnabled = "setPowerSaveModeEnabled";
    public static final String clearHistory = "clearHistory";
    public static final String reloadPersistedData = "reloadPersistedData";
    public static final String pendingRequestForNetwork = "pendingRequestForNetwork";
    public static final String cancelAuthenticationFromService = "cancelAuthenticationFromService";
    public static final String setBackupEnabled = "setBackupEnabled";
    public static final String getControlFd = "getControlFd";
    public static final String getActiveNotifications = "getActiveNotifications";
    public static final String addOnRoleHoldersChangedListenerAsUser =
            "addOnRoleHoldersChangedListenerAsUser";
    public static final String forceUpdate = "forceUpdate";
    public static final String showCpu = "showCpu";
    public static final String isControllerAlwaysOnSupported = "isControllerAlwaysOnSupported";
    public static final String removeOverridesOnReleaseBuilds = "removeOverridesOnReleaseBuilds";
    public static final String getNearbyNotificationStreamingPolicy =
            "getNearbyNotificationStreamingPolicy";
    public static final String restartWifiSubsystem = "restartWifiSubsystem";
    public static final String set = "set";
    public static final String removeRequestRebootReadinessStatusListener =
            "removeRequestRebootReadinessStatusListener";
    public static final String attachAsMiddleman = "attachAsMiddleman";
    public static final String suggestExternalTime = "suggestExternalTime";

    // The following are the transacts required for new permissions in Android 12.
    public static final String injectCamera = "injectCamera";
    public static final String clearSystemUpdatePolicyFreezePeriodRecord =
            "clearSystemUpdatePolicyFreezePeriodRecord";
    public static final String cancelRequest = "cancelRequest";
    public static final String forceSecurityLogs = "forceSecurityLogs";
    public static final String createInputConsumer = "createInputConsumer";
    public static final String setKeepUninstalledPackages = "setKeepUninstalledPackages";
    public static final String removeCredentialManagementApp = "removeCredentialManagementApp";
    public static final String getAvailableGameModes = "getAvailableGameModes";
    public static final String destroySmartspaceSession = "destroySmartspaceSession";
    public static final String setTemporaryComponent = "setTemporaryComponent";
    public static final String setToastRateLimitingEnabled = "setToastRateLimitingEnabled";
    public static final String setOverrideCountryCode = "setOverrideCountryCode";
    public static final String setRefreshRateSwitchingType = "setRefreshRateSwitchingType";
    public static final String shouldAlwaysRespectAppRequestedMode =
            "shouldAlwaysRespectAppRequestedMode";
    public static final String getDeviceVolumeBehavior = "getDeviceVolumeBehavior";
    public static final String isAmbientDisplaySuppressedForTokenByApp =
            "isAmbientDisplaySuppressedForTokenByApp";
    public static final String getActiveProjectionTypes = "getActiveProjectionTypes";
    public static final String resetAppErrors = "resetAppErrors";
    public static final String verifyCredential = "verifyCredential";
    public static final String getUiPackage = "getUiPackage";
    public static final String setDomainVerificationLinkHandlingAllowed =
            "setDomainVerificationLinkHandlingAllowed";
    public static final String getEnabledNotificationListeners = "getEnabledNotificationListeners";
    public static final String setBatteryDischargePrediction = "setBatteryDischargePrediction";
    public static final String getCapabilitiesAndConfig = "getCapabilitiesAndConfig";
    public static final String unregisterObserver = "unregisterObserver";
    public static final String checkPermission = "checkPermission";
    public static final String requestProjection = "requestProjection";
    public static final String getFontConfig = "getFontConfig";
    public static final String getSpecificationInfo = "getSpecificationInfo";
    public static final String beginRecognition = "beginRecognition";
    public static final String updateUiTranslationState = "updateUiTranslationState";
    public static final String getCurrentTunedInfos = "getCurrentTunedInfos";
    public static final String getWindowOrganizerController = "getWindowOrganizerController";
    public static final String getPrimaryClipSource = "getPrimaryClipSource";
    public static final String isConversation = "isConversation";
    public static final String unregisterCoexCallback = "unregisterCoexCallback";
    public static final String setCoexUnsafeChannels = "setCoexUnsafeChannels";
    public static final String updateState = "updateState";
    public static final String isSensorPrivacyEnabled = "isSensorPrivacyEnabled";
    public static final String queryValidVerificationPackageNames =
            "queryValidVerificationPackageNames";
    public static final String requestAvailability = "requestAvailability";
    public static final String createAssociation = "createAssociation";
    public static final String setBypassingRoleQualification = "setBypassingRoleQualification";
    public static final String destroySipDelegate = "destroySipDelegate";
    public static final String attachAsOriginator = "attachAsOriginator";
    public static final String setDynamicPowerSaveHint = "setDynamicPowerSaveHint";
    public static final String resetLockout = "resetLockout";
    public static final String isAutoRevokeExempted = "isAutoRevokeExempted";
    public static final String isUidNetworkingBlocked = "isUidNetworkingBlocked";
    public static final String isSessionRunning = "isSessionRunning";
    public static final String getActivityClientController = "getActivityClientController";
    public static final String getModuleProperties = "getModuleProperties";
    public static final String triggerNetworkRegistration = "triggerNetworkRegistration";
    public static final String getUidOps = "getUidOps";

    /**
     * Mapping from the descriptor class to the constant variable name for use when writing an
     * extension of the Transacts class for the Permission Test Tool.
     */
    private static final Map<String, String> sDescriptorNames = new HashMap<>();

    static {
        try {
            for (Field field : Transacts.class.getFields()) {
                String fieldName = field.getName();
                if (fieldName.endsWith("DESCRIPTOR")) {
                    Object fieldValue = field.get(null);
                    if (fieldValue instanceof String) {
                        sDescriptorNames.put((String) fieldValue, fieldName);
                    } else {
                        Log.e(TAG, "Field " + fieldName + " does not have a value of type String");
                    }
                }
            }
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Caught an IllegalAccessException: ", e);
        }
    }

    /**
     * Returns a mapping of the descriptor class names to their variable names defined in this
     * class.
     */
    public static Map<String, String> getDescriptorNames() {
        return sDescriptorNames;
    }
}
