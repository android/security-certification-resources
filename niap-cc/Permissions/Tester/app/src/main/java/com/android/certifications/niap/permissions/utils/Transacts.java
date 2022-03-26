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

package com.android.certifications.niap.permissions.utils;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.accounts.Account;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.os.RemoteException;
import android.text.TextUtils;

import com.android.certifications.niap.permissions.BasePermissionTester;
import com.android.certifications.niap.permissions.log.Logger;
import com.android.certifications.niap.permissions.log.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Provides utility methods and constants for invoking transacts against system services. Clients
 * can obtain a new instance through the static factory method {@link
 * #createTransactsForApiLevel(int)}. Note that transact IDs can be device specific; implementers
 * may need to create their own extensions of {@code Transacts} and return an instance of this
 * subclass in {@link #createTransactsForApiLevel(int)}. Once clients have an instance that defines
 * the transact IDs for the device under test a direct binder transact can be invoked through {@link
 * #invokeTransact(String, String, String, Object...)}.
 */
public class Transacts {
    private static final String TAG = "PermissionTesterTransacts";
    private final Logger mLogger = LoggerFactory.createDefaultLogger(TAG);

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
    public static final String DEVICE_STATE_DESCRIPTOR =
            "android.hardware.devicestate.IDeviceStateManager";

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
    // This should map to an invalid transaction ID for the SurfaceFlinger; this invalid ID triggers
    // a check for the HARDWARE_TEST permission.
    public static final String showCpu = "showCpu";

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
    public static final String isControllerAlwaysOnSupported = "isControllerAlwaysOnSupported";
    public static final String removeOverridesOnReleaseBuilds = "removeOverridesOnReleaseBuilds";
    public static final String getNearbyNotificationStreamingPolicy =
            "getNearbyNotificationStreamingPolicy";
    public static final String unregisterObserver = "unregisterObserver";
    public static final String checkPermission = "checkPermission";
    public static final String restartWifiSubsystem = "restartWifiSubsystem";
    public static final String set = "set";
    public static final String removeRequestRebootReadinessStatusListener =
            "removeRequestRebootReadinessStatusListener";
    public static final String attachAsMiddleman = "attachAsMiddleman";
    public static final String suggestExternalTime = "suggestExternalTime";
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
     * Contains a mapping from the descriptor to a Map of transact names to their IDs on the device
     * under test.
     */
    protected final Map<String, Map<String, Integer>> mDescriptorTransacts = new HashMap<>();
    /**
     * The API level of the device under test.
     */
    protected int mDeviceApiLevel;

    /**
     * Initializes the mapping of transact names to their expected IDs for a device running Android
     * 9 / API level 28.
     */
    private static class PTransacts extends Transacts {
        public PTransacts() {
            super();
            mDeviceApiLevel = Build.VERSION_CODES.P;

            Map<String, Integer> transactIds = new HashMap<>();
            transactIds.put(getActiveServiceComponentName, 20);
            transactIds.put(updateKeyphraseSoundModel, 14);
            mDescriptorTransacts.put(VOICE_INTERACTION_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(bootFinished, 1);
            transactIds.put(showCpu, 1000);
            mDescriptorTransacts.put(SURFACE_FLINGER_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(resetTimeout, 13);
            transactIds.put(cancelEnrollment, 4);
            mDescriptorTransacts.put(FINGERPRINT_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(retainSubscriptionsForFactoryReset, 12);
            mDescriptorTransacts.put(EUICC_CONTROLLER_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(reboot, 18);
            transactIds.put(setPowerSaveMode, 15);
            mDescriptorTransacts.put(POWER_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(notifySystemEvent, 14);
            mDescriptorTransacts.put(CAMERA_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(isRecognitionActive, 12);
            mDescriptorTransacts.put(SOUND_TRIGGER_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(grantPermissionFromUser, 11);
            mDescriptorTransacts.put(SLICE_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setRingtonePlayer, 47);
            transactIds.put(getRingtonePlayer, 48);
            transactIds.put(isAudioServerRunning, 83);
            mDescriptorTransacts.put(AUDIO_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(noteStartAudio, 5);
            transactIds.put(getAwakeTimeBattery, 75);
            mDescriptorTransacts.put(BATTERY_STATS_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(onApplicationActive, 19);
            mDescriptorTransacts.put(SHORTCUT_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setWallpaper, 1);
            mDescriptorTransacts.put(WALLPAPER_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getVtDataUsage, 173);
            transactIds.put(enableLocationUpdates, 34);
            mDescriptorTransacts.put(TELEPHONY_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(requestEmbeddedSubscriptionInfoListRefresh, 11);
            mDescriptorTransacts.put(ISUB_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getActiveNotifications, 38);
            transactIds.put(getZenRules, 90);
            transactIds.put(isNotificationPolicyAccessGrantedForPackage, 86);
            mDescriptorTransacts.put(NOTIFICATION_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(movePackage, 126);
            transactIds.put(installExistingPackageAsUser, 131);
            transactIds.put(addOnPermissionsChangeListener, 159);
            transactIds.put(getHarmfulAppWarning, 190);
            transactIds.put(isPackageDeviceAdminOnAnyUser, 179);
            transactIds.put(isPackageStateProtected, 194);
            transactIds.put(getMoveStatus, 123);
            transactIds.put(resetApplicationPreferences, 66);
            mDescriptorTransacts.put(PACKAGE_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getSubscriptionPlans, 18);
            transactIds.put(getUidPolicy, 4);
            transactIds.put(registerListener, 6);
            mDescriptorTransacts.put(NET_POLICY_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setTimeZone, 3);
            transactIds.put(setTime, 2);
            mDescriptorTransacts.put(ALARM_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getActiveProjectionInfo, 4);
            mDescriptorTransacts.put(MEDIA_PROJECTION_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setPersistentVrModeEnabled, 7);
            transactIds.put(getVrModeState, 5);
            transactIds.put(setStandbyEnabled, 11);
            mDescriptorTransacts.put(VR_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getCacheSizeBytes, 77);
            transactIds.put(getEncryptionState, 32);
            transactIds.put(benchmark, 60);
            mDescriptorTransacts.put(MOUNT_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setDataSaverModeEnabled, 50);
            mDescriptorTransacts.put(NETWORK_MANAGEMENT_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(bindBackupAgent, 88);
            transactIds.put(performIdleMaintenance, 180);
            transactIds.put(setDumpHeapDebugLimit, 225);
            transactIds.put(updateLockTaskPackages, 228);
            transactIds.put(getGrantedUriPermissions, 263);
            transactIds.put(setHasTopUi, 285);
            transactIds.put(dismissKeyguard, 289);
            transactIds.put(resumeAppSwitches, 87);
            transactIds.put(getContentProviderExternal, 138);
            transactIds.put(getIntentForIntentSender, 161);
            transactIds.put(getTaskDescription, 80);
            transactIds.put(getAssistContextExtras, 162);
            transactIds.put(unhandledBack, 7);
            transactIds.put(inputDispatchingTimedOut, 159);
            transactIds.put(setFrontActivityScreenCompatMode, 123);
            transactIds.put(setAlwaysFinish, 38);
            transactIds.put(startActivityFromRecents, 198);
            transactIds.put(releasePersistableUriPermission, 182);
            transactIds.put(requestBugReport, 156);
            transactIds.put(getFrontActivityScreenCompatMode, 122);
            transactIds.put(setProcessLimit, 47);
            transactIds.put(signalPersistentProcesses, 55);
            transactIds.put(updateConfiguration, 43);
            transactIds.put(appNotRespondingViaProvider, 184);
            transactIds.put(shutdown, 85);
            mDescriptorTransacts.put(ACTIVITY_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(awaken, 2);
            transactIds.put(isDreaming, 7);
            mDescriptorTransacts.put(DREAMS_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setTemporaryAutoBrightnessAdjustment, 26);
            transactIds.put(startWifiDisplayScan, 4);
            transactIds.put(requestColorMode, 13);
            mDescriptorTransacts.put(DISPLAY_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setAnimationScale, 47);
            transactIds.put(requestUserActivityNotification, 92);
            transactIds.put(removeWindowToken, 18);
            transactIds.put(thawRotation, 60);
            transactIds.put(registerShortcutKey, 85);
            transactIds.put(clearWindowContentFrameStats, 76);
            transactIds.put(overridePendingAppTransitionRemote, 30);
            transactIds.put(setRecentsVisibility, 67);
            transactIds.put(stopFreezingScreen, 36);
            transactIds.put(dismissKeyguard, 42);
            transactIds.put(screenshotWallpaper, 62);
            mDescriptorTransacts.put(WINDOW_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setUserRestriction, 20);
            transactIds.put(noteOperation, 2);
            mDescriptorTransacts.put(APP_OPS_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(temporaryEnableAccessibilityStateUntilKeyguardRemoved, 11);
            transactIds.put(registerUiTestAutomationService, 9);
            transactIds.put(setPictureInPictureActionReplacingConnection, 8);
            transactIds.put(getWindowToken, 12);
            mDescriptorTransacts.put(ACCESSIBILITY_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setBindAppWidgetPermission, 20);
            mDescriptorTransacts.put(APPWIDGET_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setBackupEnabled, 7);
            mDescriptorTransacts.put(BACKUP_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(reportEnabledTrustAgentsChanged, 3);
            transactIds.put(unregisterTrustListener, 5);
            mDescriptorTransacts.put(TRUST_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getWifiApConfiguration, 48);
            transactIds.put(stopSoftAp, 42);
            transactIds.put(setWifiEnabled, 25);
            mDescriptorTransacts.put(WIFI_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getControlFd, 21);
            mDescriptorTransacts.put(USB_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(installCaCert, 82);
            transactIds.put(getDoNotAskCredentialsOnBoot, 181);
            transactIds.put(setDeviceOwner, 65);
            mDescriptorTransacts.put(DEVICE_POLICY_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getNextEntry, 3);
            mDescriptorTransacts.put(DROPBOX_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(tether, 20);
            transactIds.put(getAlwaysOnVpnPackage, 48);
            transactIds.put(getActiveNetworkForUid, 2);
            transactIds.put(startNattKeepalive, 72);
            mDescriptorTransacts.put(CONNECTIVITY_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(forceUpdate, 8);
            mDescriptorTransacts.put(NETWORK_STATS_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setOnMediaKeyListener, 12);
            transactIds.put(setOnVolumeKeyLongPressListener, 11);
            mDescriptorTransacts.put(MEDIA_SESSION_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setTouchCalibrationForInputDevice, 10);
            transactIds.put(isInTabletMode, 20);
            transactIds.put(addKeyboardLayoutForInputDevice, 17);
            transactIds.put(enableInputDevice, 4);
            transactIds.put(tryPointerSpeed, 7);
            mDescriptorTransacts.put(INPUT_DESCRIPTOR, transactIds);
        }
    }

    /**
     * Initializes the mapping of transact names to their expected IDs for a device running Android
     * 10 / API level 29.
     */
    private static class QTransacts extends Transacts {
        public QTransacts() {
            super();
            mDeviceApiLevel = Build.VERSION_CODES.Q;

            Map<String, Integer> transactIds = new HashMap<>();
            transactIds.put(getActiveServiceComponentName, 20);
            transactIds.put(updateKeyphraseSoundModel, 14);
            mDescriptorTransacts.put(VOICE_INTERACTION_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(bootFinished, 1);
            transactIds.put(showCpu, 1000);
            mDescriptorTransacts.put(SURFACE_FLINGER_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(cancelAuthenticationFromService, 5);
            transactIds.put(resetTimeout, 16);
            transactIds.put(cancelEnrollment, 7);
            mDescriptorTransacts.put(FINGERPRINT_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(retainSubscriptionsForFactoryReset, 12);
            mDescriptorTransacts.put(EUICC_CONTROLLER_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(takePersistableUriPermission, 1);
            transactIds.put(getGrantedUriPermissions, 4);
            mDescriptorTransacts.put(URI_GRANTS_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(reboot, 22);
            mDescriptorTransacts.put(POWER_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(notifySystemEvent, 15);
            mDescriptorTransacts.put(CAMERA_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(isRecognitionActive, 11);
            mDescriptorTransacts.put(SOUND_TRIGGER_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(grantPermissionFromUser, 11);
            mDescriptorTransacts.put(SLICE_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setRingtonePlayer, 56);
            transactIds.put(getRingtonePlayer, 57);
            transactIds.put(isAudioServerRunning, 93);
            mDescriptorTransacts.put(AUDIO_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(noteStartAudio, 5);
            transactIds.put(getAwakeTimeBattery, 75);
            mDescriptorTransacts.put(BATTERY_STATS_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(onApplicationActive, 19);
            mDescriptorTransacts.put(SHORTCUT_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setWallpaper, 1);
            mDescriptorTransacts.put(WALLPAPER_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getVtDataUsage, 178);
            transactIds.put(enableLocationUpdates, 23);
            mDescriptorTransacts.put(TELEPHONY_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(requestEmbeddedSubscriptionInfoListRefresh, 11);
            mDescriptorTransacts.put(ISUB_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getActiveNotifications, 50);
            transactIds.put(getZenRules, 106);
            transactIds.put(isNotificationPolicyAccessGrantedForPackage, 102);
            mDescriptorTransacts.put(NOTIFICATION_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(movePackage, 129);
            transactIds.put(installExistingPackageAsUser, 134);
            transactIds.put(getRuntimePermissionsVersion, 207);
            transactIds.put(addOnPermissionsChangeListener, 162);
            transactIds.put(getHarmfulAppWarning, 194);
            transactIds.put(isPackageDeviceAdminOnAnyUser, 182);
            transactIds.put(isPackageStateProtected, 203);
            transactIds.put(getMoveStatus, 126);
            transactIds.put(resetApplicationPreferences, 69);
            mDescriptorTransacts.put(PACKAGE_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getSubscriptionPlans, 18);
            transactIds.put(getUidPolicy, 4);
            transactIds.put(registerListener, 6);
            mDescriptorTransacts.put(NET_POLICY_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setTimeZone, 3);
            transactIds.put(setTime, 2);
            mDescriptorTransacts.put(ALARM_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getActiveProjectionInfo, 4);
            mDescriptorTransacts.put(MEDIA_PROJECTION_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getFrontActivityScreenCompatMode, 25);
            transactIds.put(getTaskDescription, 52);
            transactIds.put(getAssistContextExtras, 99);
            transactIds.put(setFrontActivityScreenCompatMode, 26);
            transactIds.put(startActivityAsCaller, 12);
            transactIds.put(dismissKeyguard, 134);
            mDescriptorTransacts.put(ACTIVITY_TASK_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setPersistentVrModeEnabled, 7);
            transactIds.put(getVrModeState, 5);
            transactIds.put(setStandbyEnabled, 11);
            mDescriptorTransacts.put(VR_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getCacheSizeBytes, 77);
            transactIds.put(getEncryptionState, 32);
            transactIds.put(benchmark, 60);
            mDescriptorTransacts.put(MOUNT_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setDataSaverModeEnabled, 46);
            mDescriptorTransacts.put(NETWORK_MANAGEMENT_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(unhandledBack, 8);
            transactIds.put(bindBackupAgent, 77);
            transactIds.put(setAlwaysFinish, 33);
            transactIds.put(performIdleMaintenance, 139);
            transactIds.put(startActivityFromRecents, 148);
            transactIds.put(setDumpHeapDebugLimit, 159);
            transactIds.put(updateLockTaskPackages, 161);
            transactIds.put(setHasTopUi, 185);
            transactIds.put(requestBugReport, 125);
            transactIds.put(resumeAppSwitches, 76);
            transactIds.put(getContentProviderExternal, 108);
            transactIds.put(getIntentForIntentSender, 128);
            transactIds.put(setProcessLimit, 40);
            transactIds.put(signalPersistentProcesses, 48);
            transactIds.put(updateConfiguration, 38);
            transactIds.put(appNotRespondingViaProvider, 140);
            transactIds.put(shutdown, 74);
            mDescriptorTransacts.put(ACTIVITY_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(generateChallenge, 12);
            mDescriptorTransacts.put(FACE_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(awaken, 2);
            transactIds.put(isDreaming, 7);
            mDescriptorTransacts.put(DREAMS_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setTemporaryAutoBrightnessAdjustment, 27);
            transactIds.put(startWifiDisplayScan, 5);
            transactIds.put(requestColorMode, 14);
            mDescriptorTransacts.put(DISPLAY_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setAnimationScale, 35);
            transactIds.put(requestUserActivityNotification, 89);
            transactIds.put(removeWindowToken, 17);
            transactIds.put(thawRotation, 48);
            transactIds.put(registerShortcutKey, 80);
            transactIds.put(clearWindowContentFrameStats, 70);
            transactIds.put(overridePendingAppTransitionRemote, 20);
            transactIds.put(setRecentsVisibility, 61);
            transactIds.put(stopFreezingScreen, 24);
            transactIds.put(dismissKeyguard, 30);
            transactIds.put(screenshotWallpaper, 53);
            mDescriptorTransacts.put(WINDOW_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(clearHistory, 20);
            transactIds.put(setUserRestriction, 27);
            transactIds.put(noteOperation, 2);
            mDescriptorTransacts.put(APP_OPS_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(temporaryEnableAccessibilityStateUntilKeyguardRemoved, 11);
            transactIds.put(registerUiTestAutomationService, 9);
            transactIds.put(setPictureInPictureActionReplacingConnection, 8);
            transactIds.put(getWindowToken, 12);
            mDescriptorTransacts.put(ACCESSIBILITY_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setBackupEnabled, 13);
            mDescriptorTransacts.put(BACKUP_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setBindAppWidgetPermission, 20);
            mDescriptorTransacts.put(APPWIDGET_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(reportEnabledTrustAgentsChanged, 3);
            transactIds.put(unregisterTrustListener, 5);
            mDescriptorTransacts.put(TRUST_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setSensorPrivacy, 4);
            mDescriptorTransacts.put(SENSOR_PRIVACY_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getWifiApConfiguration, 49);
            transactIds.put(stopSoftAp, 43);
            transactIds.put(setWifiEnabled, 26);
            mDescriptorTransacts.put(WIFI_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getControlFd, 21);
            mDescriptorTransacts.put(USB_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(installCaCert, 85);
            transactIds.put(getDoNotAskCredentialsOnBoot, 186);
            transactIds.put(setDeviceOwner, 66);
            mDescriptorTransacts.put(DEVICE_POLICY_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getNextEntry, 3);
            mDescriptorTransacts.put(DROPBOX_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(tether, 20);
            transactIds.put(getAlwaysOnVpnPackage, 48);
            transactIds.put(getActiveNetworkForUid, 2);
            transactIds.put(startNattKeepalive, 78);
            mDescriptorTransacts.put(CONNECTIVITY_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(hasEnrolledBiometrics, 4);
            mDescriptorTransacts.put(BIOMETRIC_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(forceUpdate, 8);
            mDescriptorTransacts.put(NETWORK_STATS_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(addOnRoleHoldersChangedListenerAsUser, 7);
            mDescriptorTransacts.put(ROLE_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setOnMediaKeyListener, 19);
            transactIds.put(setOnVolumeKeyLongPressListener, 18);
            mDescriptorTransacts.put(MEDIA_SESSION_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setTouchCalibrationForInputDevice, 10);
            transactIds.put(isInTabletMode, 20);
            transactIds.put(monitorGestureInput, 27);
            transactIds.put(addKeyboardLayoutForInputDevice, 17);
            transactIds.put(enableInputDevice, 4);
            transactIds.put(tryPointerSpeed, 7);
            mDescriptorTransacts.put(INPUT_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(reloadPersistedData, 5);
            mDescriptorTransacts.put(ROLLBACK_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(hideBiometricDialog, 45);
            transactIds.put(onBiometricHelp, 43);
            mDescriptorTransacts.put(STATUS_BAR_DESCRIPTOR, transactIds);
        }
    }

    /**
     * Initializes the mapping of transact names to their expected IDs for a device running Android
     * 11 / API level 30.
     */
    private static class RTransacts extends Transacts {
        public RTransacts() {
            super();
            mDeviceApiLevel = Build.VERSION_CODES.R;

            Map<String, Integer> transactIds = new HashMap<>();
            transactIds.put(getActiveServiceComponentName, 24);
            transactIds.put(updateKeyphraseSoundModel, 14);
            mDescriptorTransacts.put(VOICE_INTERACTION_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(bootFinished, 1);
            transactIds.put(showCpu, 1000);
            mDescriptorTransacts.put(SURFACE_FLINGER_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(resetTimeout, 16);
            transactIds.put(cancelEnrollment, 7);
            mDescriptorTransacts.put(FINGERPRINT_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(retainSubscriptionsForFactoryReset, 13);
            mDescriptorTransacts.put(EUICC_CONTROLLER_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(takePersistableUriPermission, 1);
            transactIds.put(getGrantedUriPermissions, 4);
            mDescriptorTransacts.put(URI_GRANTS_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(reportChangeByUid, 3);
            transactIds.put(clearOverridesForTest, 13);
            mDescriptorTransacts.put(PLATFORM_COMPAT_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(reboot, 26);
            mDescriptorTransacts.put(POWER_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(isRecognitionActive, 11);
            mDescriptorTransacts.put(SOUND_TRIGGER_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(grantPermissionFromUser, 11);
            mDescriptorTransacts.put(SLICE_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setRingtonePlayer, 61);
            transactIds.put(getRingtonePlayer, 62);
            transactIds.put(isAudioServerRunning, 98);
            mDescriptorTransacts.put(AUDIO_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(noteStartAudio, 5);
            transactIds.put(getAwakeTimeBattery, 75);
            mDescriptorTransacts.put(BATTERY_STATS_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(onApplicationActive, 16);
            mDescriptorTransacts.put(SHORTCUT_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setWallpaper, 1);
            mDescriptorTransacts.put(WALLPAPER_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(enableLocationUpdates, 21);
            mDescriptorTransacts.put(TELEPHONY_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(requestEmbeddedSubscriptionInfoListRefresh, 11);
            mDescriptorTransacts.put(ISUB_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setAlwaysOnEffect, 8);
            transactIds.put(isVibrating, 2);
            mDescriptorTransacts.put(VIBRATOR_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(clearInteractAcrossProfilesAppOps, 10);
            mDescriptorTransacts.put(CROSS_PROFILE_APPS_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getActiveNotifications, 60);
            transactIds.put(getZenRules, 120);
            transactIds.put(isNotificationPolicyAccessGrantedForPackage, 116);
            mDescriptorTransacts.put(NOTIFICATION_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(movePackage, 114);
            transactIds.put(installExistingPackageAsUser, 118);
            transactIds.put(getRuntimePermissionsVersion, 181);
            transactIds.put(getHarmfulAppWarning, 165);
            transactIds.put(isPackageDeviceAdminOnAnyUser, 153);
            transactIds.put(isPackageStateProtected, 177);
            transactIds.put(getMoveStatus, 111);
            transactIds.put(resetApplicationPreferences, 52);
            mDescriptorTransacts.put(PACKAGE_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getSubscriptionPlans, 17);
            transactIds.put(getUidPolicy, 4);
            transactIds.put(registerListener, 6);
            mDescriptorTransacts.put(NET_POLICY_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setTimeZone, 3);
            transactIds.put(setTime, 2);
            mDescriptorTransacts.put(ALARM_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getActiveProjectionInfo, 4);
            mDescriptorTransacts.put(MEDIA_PROJECTION_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getFrontActivityScreenCompatMode, 25);
            transactIds.put(getTaskDescription, 52);
            transactIds.put(getAssistContextExtras, 98);
            transactIds.put(setFrontActivityScreenCompatMode, 26);
            transactIds.put(dismissKeyguard, 126);
            mDescriptorTransacts.put(ACTIVITY_TASK_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setPersistentVrModeEnabled, 7);
            transactIds.put(getVrModeState, 5);
            transactIds.put(setStandbyEnabled, 11);
            mDescriptorTransacts.put(VR_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(overridePid, 6);
            mDescriptorTransacts.put(RESOURCE_MANAGER_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getCacheSizeBytes, 77);
            transactIds.put(getEncryptionState, 32);
            transactIds.put(benchmark, 60);
            mDescriptorTransacts.put(MOUNT_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setDataSaverModeEnabled, 43);
            mDescriptorTransacts.put(NETWORK_MANAGEMENT_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(unhandledBack, 9);
            transactIds.put(bindBackupAgent, 81);
            transactIds.put(performIdleMaintenance, 152);
            transactIds.put(startActivityFromRecents, 161);
            transactIds.put(setDumpHeapDebugLimit, 172);
            transactIds.put(updateLockTaskPackages, 174);
            transactIds.put(setHasTopUi, 197);
            transactIds.put(requestBugReport, 132);
            transactIds.put(resumeAppSwitches, 80);
            transactIds.put(getContentProviderExternal, 113);
            transactIds.put(getIntentForIntentSender, 142);
            transactIds.put(signalPersistentProcesses, 51);
            transactIds.put(updateConfiguration, 40);
            transactIds.put(appNotRespondingViaProvider, 153);
            transactIds.put(shutdown, 78);
            transactIds.put(setAlwaysFinish, 35);
            mDescriptorTransacts.put(ACTIVITY_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(generateChallenge, 12);
            mDescriptorTransacts.put(FACE_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(awaken, 2);
            transactIds.put(isDreaming, 7);
            mDescriptorTransacts.put(DREAMS_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setTemporaryAutoBrightnessAdjustment, 28);
            transactIds.put(startWifiDisplayScan, 5);
            transactIds.put(requestColorMode, 14);
            mDescriptorTransacts.put(DISPLAY_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setAnimationScale, 40);
            transactIds.put(removeWindowToken, 19);
            transactIds.put(thawRotation, 53);
            transactIds.put(registerShortcutKey, 84);
            transactIds.put(clearWindowContentFrameStats, 76);
            transactIds.put(overridePendingAppTransitionRemote, 25);
            transactIds.put(setRecentsVisibility, 68);
            transactIds.put(stopFreezingScreen, 29);
            transactIds.put(dismissKeyguard, 35);
            transactIds.put(screenshotWallpaper, 59);
            mDescriptorTransacts.put(WINDOW_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(clearHistory, 23);
            transactIds.put(noteOperation, 2);
            transactIds.put(setUserRestriction, 31);
            mDescriptorTransacts.put(APP_OPS_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(temporaryEnableAccessibilityStateUntilKeyguardRemoved, 11);
            transactIds.put(registerUiTestAutomationService, 9);
            transactIds.put(setPictureInPictureActionReplacingConnection, 8);
            transactIds.put(getWindowToken, 12);
            mDescriptorTransacts.put(ACCESSIBILITY_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setBackupEnabled, 13);
            mDescriptorTransacts.put(BACKUP_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setBindAppWidgetPermission, 20);
            mDescriptorTransacts.put(APPWIDGET_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(reportEnabledTrustAgentsChanged, 3);
            transactIds.put(unregisterTrustListener, 5);
            mDescriptorTransacts.put(TRUST_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setSensorPrivacy, 4);
            mDescriptorTransacts.put(SENSOR_PRIVACY_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getWifiApConfiguration, 55);
            transactIds.put(stopSoftAp, 49);
            transactIds.put(setWifiEnabled, 30);
            mDescriptorTransacts.put(WIFI_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getControlFd, 28);
            mDescriptorTransacts.put(USB_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(addOnPermissionsChangeListener, 14);
            transactIds.put(isAutoRevokeWhitelisted, 40);
            mDescriptorTransacts.put(PERMISSION_MANAGER_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(installCaCert, 91);
            transactIds.put(markProfileOwnerOnOrganizationOwnedDevice, 289);
            transactIds.put(getDoNotAskCredentialsOnBoot, 203);
            transactIds.put(setDeviceOwner, 70);
            mDescriptorTransacts.put(DEVICE_POLICY_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getNextEntry, 3);
            mDescriptorTransacts.put(DROPBOX_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getAlwaysOnVpnPackage, 44);
            transactIds.put(getActiveNetworkForUid, 2);
            transactIds.put(startNattKeepalive, 77);
            mDescriptorTransacts.put(CONNECTIVITY_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(hasEnrolledBiometrics, 4);
            mDescriptorTransacts.put(BIOMETRIC_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(registerNetworkStatsProvider, 14);
            transactIds.put(forceUpdate, 8);
            mDescriptorTransacts.put(NETWORK_STATS_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setOnMediaKeyListener, 22);
            transactIds.put(setOnVolumeKeyLongPressListener, 21);
            mDescriptorTransacts.put(MEDIA_SESSION_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setTouchCalibrationForInputDevice, 11);
            transactIds.put(isInTabletMode, 21);
            transactIds.put(monitorGestureInput, 29);
            transactIds.put(addKeyboardLayoutForInputDevice, 18);
            transactIds.put(enableInputDevice, 4);
            transactIds.put(tryPointerSpeed, 7);
            transactIds.put(removePortAssociation, 31);
            mDescriptorTransacts.put(INPUT_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(reloadPersistedData, 5);
            mDescriptorTransacts.put(ROLLBACK_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(onBiometricHelp, 46);
            mDescriptorTransacts.put(STATUS_BAR_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(addOnRoleHoldersChangedListenerAsUser, 7);
            mDescriptorTransacts.put(ROLE_DESCRIPTOR, transactIds);
        }
    }

    /**
     * Initializes the mapping of transact names to their expected IDs for a device running Android
     * 12 / API level 31.
     */
    private static class STransacts extends Transacts {
        public STransacts() {
            super();
            mDeviceApiLevel = Build.VERSION_CODES.S;

            Map<String, Integer> transactIds;
            transactIds = new HashMap<>();
            transactIds.put(updateState, 33);
            transactIds.put(getActiveServiceComponentName, 18);
            transactIds.put(updateKeyphraseSoundModel, 14);
            transactIds.put(isSessionRunning, 22);
            mDescriptorTransacts.put(VOICE_INTERACTION_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(bootFinished, 1);
            transactIds.put(showCpu, 1000);
            mDescriptorTransacts.put(SURFACE_FLINGER_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(resetLockout, 28);
            transactIds.put(cancelAuthenticationFromService, 11);
            transactIds.put(cancelEnrollment, 13);
            mDescriptorTransacts.put(FINGERPRINT_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(retainSubscriptionsForFactoryReset, 13);
            mDescriptorTransacts.put(EUICC_CONTROLLER_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(takePersistableUriPermission, 1);
            transactIds.put(getGrantedUriPermissions, 4);
            mDescriptorTransacts.put(URI_GRANTS_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(verifyCredential, 10);
            mDescriptorTransacts.put(LOCK_SETTINGS_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getAvailableGameModes, 3);
            mDescriptorTransacts.put(GAME_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(reportChangeByUid, 3);
            transactIds.put(removeOverridesOnReleaseBuilds, 12);
            transactIds.put(clearOverridesForTest, 16);
            mDescriptorTransacts.put(PLATFORM_COMPAT_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(beginRecognition, 1);
            mDescriptorTransacts.put(MUSIC_RECOGNITION_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(reboot, 30);
            transactIds.put(isAmbientDisplaySuppressedForTokenByApp, 48);
            transactIds.put(setDynamicPowerSaveHint, 21);
            transactIds.put(setBatteryDischargePrediction, 25);
            mDescriptorTransacts.put(POWER_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(notifySystemEvent, 17);
            transactIds.put(injectCamera, 15);
            mDescriptorTransacts.put(CAMERA_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(attachAsOriginator, 1);
            transactIds.put(attachAsMiddleman, 2);
            mDescriptorTransacts.put(SOUND_TRIGGER_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(grantPermissionFromUser, 11);
            mDescriptorTransacts.put(SLICE_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getUiPackage, 3);
            mDescriptorTransacts.put(AUTH_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(isControllerAlwaysOnSupported, 30);
            mDescriptorTransacts.put(NFC_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setRingtonePlayer, 68);
            transactIds.put(getRingtonePlayer, 69);
            transactIds.put(isAudioServerRunning, 105);
            transactIds.put(getDeviceVolumeBehavior, 122);
            mDescriptorTransacts.put(AUDIO_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(noteStartAudio, 5);
            transactIds.put(getAwakeTimeBattery, 76);
            mDescriptorTransacts.put(BATTERY_STATS_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(onApplicationActive, 16);
            mDescriptorTransacts.put(SHORTCUT_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(unregisterObserver, 2);
            mDescriptorTransacts.put(RESOURCE_OBSERVER_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setWallpaper, 1);
            mDescriptorTransacts.put(WALLPAPER_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(enableLocationUpdates, 23);
            mDescriptorTransacts.put(TELEPHONY_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(destroySmartspaceSession, 6);
            mDescriptorTransacts.put(SMART_SPACE_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getPrimaryClipSource, 10);
            mDescriptorTransacts.put(CLIPBOARD_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(requestEmbeddedSubscriptionInfoListRefresh, 11);
            mDescriptorTransacts.put(ISUB_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            mDescriptorTransacts.put(VIBRATOR_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(clearInteractAcrossProfilesAppOps, 10);
            mDescriptorTransacts.put(CROSS_PROFILE_APPS_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(isConversation, 5);
            mDescriptorTransacts.put(PEOPLE_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getActiveNotifications, 62);
            transactIds.put(getZenRules, 125);
            transactIds.put(isNotificationPolicyAccessGrantedForPackage, 121);
            transactIds.put(getEnabledNotificationListeners, 107);
            transactIds.put(setToastRateLimitingEnabled, 144);
            mDescriptorTransacts.put(NOTIFICATION_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(movePackage, 114);
            transactIds.put(installExistingPackageAsUser, 118);
            transactIds.put(setKeepUninstalledPackages, 204);
            transactIds.put(getRuntimePermissionsVersion, 182);
            transactIds.put(getHarmfulAppWarning, 165);
            transactIds.put(isPackageDeviceAdminOnAnyUser, 153);
            transactIds.put(isPackageStateProtected, 178);
            transactIds.put(getMoveStatus, 111);
            transactIds.put(resetApplicationPreferences, 53);
            mDescriptorTransacts.put(PACKAGE_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getSubscriptionPlans, 18);
            transactIds.put(isUidNetworkingBlocked, 23);
            transactIds.put(getUidPolicy, 4);
            transactIds.put(registerListener, 6);
            mDescriptorTransacts.put(NET_POLICY_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(set, 1);
            transactIds.put(setTimeZone, 3);
            transactIds.put(setTime, 2);
            mDescriptorTransacts.put(ALARM_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getActiveProjectionInfo, 4);
            mDescriptorTransacts.put(MEDIA_PROJECTION_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(dismissKeyguard, 49);
            mDescriptorTransacts.put(ACTIVITY_CLIENT_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getFrontActivityScreenCompatMode, 17);
            transactIds.put(getTaskDescription, 26);
            transactIds.put(getAssistContextExtras, 57);
            transactIds.put(setFrontActivityScreenCompatMode, 18);
            transactIds.put(getWindowOrganizerController, 64);
            transactIds.put(startActivityAsCaller, 13);
            transactIds.put(getActivityClientController, 16);
            mDescriptorTransacts.put(ACTIVITY_TASK_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setPersistentVrModeEnabled, 7);
            transactIds.put(getVrModeState, 5);
            transactIds.put(setStandbyEnabled, 11);
            mDescriptorTransacts.put(VR_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(overridePid, 6);
            mDescriptorTransacts.put(RESOURCE_MANAGER_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(createAssociation, 12);
            mDescriptorTransacts.put(COMPANION_DEVICE_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getCacheSizeBytes, 77);
            transactIds.put(getEncryptionState, 32);
            transactIds.put(benchmark, 60);
            mDescriptorTransacts.put(MOUNT_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(suggestExternalTime, 3);
            transactIds.put(getCapabilitiesAndConfig, 1);
            mDescriptorTransacts.put(TIME_DETECTOR_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(removeRequestRebootReadinessStatusListener, 5);
            mDescriptorTransacts.put(REBOOT_READINESS_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(cancelRequest, 4);
            mDescriptorTransacts.put(DEVICE_STATE_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setDataSaverModeEnabled, 41);
            mDescriptorTransacts.put(NETWORK_MANAGEMENT_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(resetAppErrors, 211);
            transactIds.put(bindBackupAgent, 82);
            transactIds.put(performIdleMaintenance, 153);
            transactIds.put(setDumpHeapDebugLimit, 170);
            transactIds.put(updateLockTaskPackages, 172);
            transactIds.put(setHasTopUi, 191);
            transactIds.put(resumeAppSwitches, 81);
            transactIds.put(getContentProviderExternal, 115);
            transactIds.put(getIntentForIntentSender, 143);
            transactIds.put(unhandledBack, 10);
            transactIds.put(setAlwaysFinish, 36);
            transactIds.put(startActivityFromRecents, 160);
            transactIds.put(requestBugReport, 133);
            transactIds.put(setProcessLimit, 44);
            transactIds.put(signalPersistentProcesses, 52);
            transactIds.put(updateConfiguration, 41);
            transactIds.put(appNotRespondingViaProvider, 154);
            transactIds.put(shutdown, 79);
            mDescriptorTransacts.put(ACTIVITY_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(generateChallenge, 19);
            mDescriptorTransacts.put(FACE_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(awaken, 2);
            transactIds.put(isDreaming, 7);
            mDescriptorTransacts.put(DREAMS_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setRefreshRateSwitchingType, 41);
            transactIds.put(setTemporaryAutoBrightnessAdjustment, 35);
            transactIds.put(startWifiDisplayScan, 6);
            transactIds.put(shouldAlwaysRespectAppRequestedMode, 40);
            transactIds.put(requestColorMode, 19);
            mDescriptorTransacts.put(DISPLAY_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(requestProjection, 15);
            transactIds.put(getActiveProjectionTypes, 20);
            mDescriptorTransacts.put(UI_MODE_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(createInputConsumer, 81);
            transactIds.put(setAnimationScale, 37);
            transactIds.put(removeWindowToken, 18);
            transactIds.put(thawRotation, 50);
            transactIds.put(registerShortcutKey, 80);
            transactIds.put(clearWindowContentFrameStats, 72);
            transactIds.put(overridePendingAppTransitionRemote, 23);
            transactIds.put(setRecentsVisibility, 64);
            transactIds.put(stopFreezingScreen, 26);
            transactIds.put(dismissKeyguard, 32);
            transactIds.put(screenshotWallpaper, 57);
            mDescriptorTransacts.put(WINDOW_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(clearHistory, 26);
            transactIds.put(setUserRestriction, 34);
            transactIds.put(getUidOps, 28);
            transactIds.put(noteOperation, 2);
            mDescriptorTransacts.put(APP_OPS_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(temporaryEnableAccessibilityStateUntilKeyguardRemoved, 12);
            transactIds.put(registerUiTestAutomationService, 10);
            transactIds.put(setPictureInPictureActionReplacingConnection, 9);
            transactIds.put(getWindowToken, 13);
            mDescriptorTransacts.put(ACCESSIBILITY_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(removeCredentialManagementApp, 25);
            mDescriptorTransacts.put(KEY_CHAIN_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(updateUiTranslationState, 5);
            mDescriptorTransacts.put(TRANSLATION_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setBindAppWidgetPermission, 20);
            mDescriptorTransacts.put(APPWIDGET_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setBackupEnabled, 13);
            mDescriptorTransacts.put(BACKUP_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(reportEnabledTrustAgentsChanged, 3);
            transactIds.put(unregisterTrustListener, 5);
            mDescriptorTransacts.put(TRUST_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getFontConfig, 1);
            mDescriptorTransacts.put(FONT_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getSpecificationInfo, 4);
            mDescriptorTransacts.put(UWB_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setSensorPrivacy, 8);
            transactIds.put(isSensorPrivacyEnabled, 6);
            mDescriptorTransacts.put(SENSOR_PRIVACY_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getWifiApConfiguration, 65);
            transactIds.put(stopSoftAp, 59);
            transactIds.put(setCoexUnsafeChannels, 54);
            transactIds.put(setOverrideCountryCode, 34);
            transactIds.put(unregisterCoexCallback, 56);
            transactIds.put(setWifiEnabled, 31);
            transactIds.put(restartWifiSubsystem, 127);
            mDescriptorTransacts.put(WIFI_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getControlFd, 32);
            mDescriptorTransacts.put(USB_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setTemporaryComponent, 2);
            mDescriptorTransacts.put(SPEECH_RECOGNITION_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(isAutoRevokeExempted, 25);
            transactIds.put(addOnPermissionsChangeListener, 10);
            mDescriptorTransacts.put(PERMISSION_MANAGER_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getModuleProperties, 13);
            mDescriptorTransacts.put(SOUND_TRIGGER_SESSION_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(installCaCert, 98);
            transactIds.put(getNearbyNotificationStreamingPolicy, 57);
            transactIds.put(markProfileOwnerOnOrganizationOwnedDevice, 300);
            transactIds.put(forceSecurityLogs, 254);
            transactIds.put(getDoNotAskCredentialsOnBoot, 214);
            transactIds.put(clearSystemUpdatePolicyFreezePeriodRecord, 211);
            transactIds.put(setDeviceOwner, 77);
            mDescriptorTransacts.put(DEVICE_POLICY_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getCurrentTunedInfos, 35);
            mDescriptorTransacts.put(TV_INPUT_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getNextEntry, 4);
            mDescriptorTransacts.put(DROPBOX_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(pendingRequestForNetwork, 41);
            transactIds.put(getActiveNetworkForUid, 2);
            transactIds.put(startNattKeepalive, 56);
            mDescriptorTransacts.put(CONNECTIVITY_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(checkPermission, 1);
            mDescriptorTransacts.put(PERMISSION_CHECKER_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(getAlwaysOnVpnPackage, 13);
            mDescriptorTransacts.put(VPN_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(hasEnrolledBiometrics, 6);
            mDescriptorTransacts.put(BIOMETRIC_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(requestAvailability, 10);
            transactIds.put(triggerNetworkRegistration, 19);
            mDescriptorTransacts.put(TELEPHONY_IMS_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(registerNetworkStatsProvider, 14);
            transactIds.put(forceUpdate, 8);
            mDescriptorTransacts.put(NETWORK_STATS_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setOnMediaKeyListener, 22);
            transactIds.put(setOnVolumeKeyLongPressListener, 21);
            mDescriptorTransacts.put(MEDIA_SESSION_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(addOnRoleHoldersChangedListenerAsUser, 7);
            transactIds.put(setBypassingRoleQualification, 10);
            mDescriptorTransacts.put(ROLE_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(queryValidVerificationPackageNames, 1);
            transactIds.put(setDomainVerificationLinkHandlingAllowed, 6);
            mDescriptorTransacts.put(DOMAIN_VERIFICATION_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(setTouchCalibrationForInputDevice, 11);
            transactIds.put(isInTabletMode, 21);
            transactIds.put(monitorGestureInput, 36);
            transactIds.put(addKeyboardLayoutForInputDevice, 18);
            transactIds.put(enableInputDevice, 4);
            transactIds.put(tryPointerSpeed, 7);
            transactIds.put(removePortAssociation, 38);
            mDescriptorTransacts.put(INPUT_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(reloadPersistedData, 5);
            mDescriptorTransacts.put(ROLLBACK_DESCRIPTOR, transactIds);

            transactIds = new HashMap<>();
            transactIds.put(onBiometricHelp, 47);
            mDescriptorTransacts.put(STATUS_BAR_DESCRIPTOR, transactIds);
        }
    }

    /**
     * Returns a new instance containing the appropriate transact ID mappings for the specified
     * {@code apiLevel} that can be used to invoke direct binder transacts.
     */
    public static Transacts createTransactsForApiLevel(int apiLevel) {
        switch (apiLevel) {
            // TODO: Replace the appropriate statement below with an instantiation of the java
            // class created by the app under the TransactIds/ directory. This will allow the tester
            // to use the specific transact IDs for the device under test.
            case Build.VERSION_CODES.P:
                return new PTransacts();
            case Build.VERSION_CODES.Q:
                return new QTransacts();
            case Build.VERSION_CODES.R:
                return new RTransacts();
            case Build.VERSION_CODES.S:
                return new STransacts();
            default:
                throw new IllegalArgumentException(
                        "The provided API level, " + apiLevel + ", is not supported");
        }
    }

    /**
     * Returns whether the provided {@code descriptor} contains an ID for the specified {@code
     * transactName} for this instance's API level.
     */
    public boolean descriptorContainsTransactName(String descriptor, String transactName) {
        Map<String, Integer> transactIds = mDescriptorTransacts.get(descriptor);
        if (transactIds == null) {
            return false;
        }
        return transactIds.containsKey(transactName);
    }

    /**
     * Returns the ID that can be used on the provided {@code descriptor} to invoke the specified
     * {@code transactName}.
     */
    public int getTransactId(String descriptor, String transactName) {
        Map<String, Integer> transactIds = mDescriptorTransacts.get(descriptor);
        if (transactIds == null) {
            throw new IllegalArgumentException(
                    "No transact entries found for the requested descriptor, " + descriptor);
        }
        if (!transactIds.containsKey(transactName)) {
            throw new IllegalArgumentException("The requested transaction name, " + transactName
                    + ", does not have a corresponding ID");
        }
        return transactIds.get(transactName);
    }

    /**
     * @see #invokeTransactWithCharSequence(String, String, String, boolean, Object...)
     */
    public Parcel invokeTransact(String service, String descriptor, String transactName,
            Object... parameters) {
        return invokeTransactWithCharSequence(service, descriptor, transactName, false, parameters);
    }

    /**
     * Invokes a direct binder transact using the specified {@code intent} to bind to the service
     * within the provided {@code context}; the service's {@code transactName} method is invoked
     * with the provided {@code parameters} using the {@code descriptor} to look up the transact ID.
     *
     * <p>To facilitate invoking direct transacts for permission tests this method will check if
     * the {@link RemoteException} caught as a result of running the transact is a {@link
     * SecurityException}; if so the {@code SecurityException} is rethrown as is, otherwise an
     * {@link BasePermissionTester.UnexpectedPermissionTestFailureException} is thrown.
     *
     * @return the {@link Parcel} received as a result of invoking the transact
     */
    public Parcel invokeTransactWithServiceFromIntent(Context context, Intent intent,
            String descriptor, String transactName, Object... parameters) {
        final CountDownLatch latch = new CountDownLatch(1);
        IBinder[] connectedBinder = new IBinder[1];
        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName className, IBinder binder) {
                mLogger.logDebug("onServiceConnected: className = " + className);
                connectedBinder[0] = binder;
                latch.countDown();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mLogger.logDebug("onServiceDisconnected: componentName = " + componentName);
            }
        };
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        boolean connectionSuccessful = false;
        try {
            connectionSuccessful = latch.await(5000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            mLogger.logError("Caught an InterruptedException waiting for the service from " + intent
                    + " to connect: ", e);
        }
        if (!connectionSuccessful) {
            throw new BasePermissionTester.UnexpectedPermissionTestFailureException(
                    "Failed to connect to the service for descriptor " + descriptor);
        }
        return invokeTransactWithCharSequence(connectedBinder[0], descriptor, transactName, false,
                parameters);

    }

    /**
     * Invokes a direct binder transact against the specified {@code service} using the {@code
     * descriptor}; the service's {@code transactName} method is invoked with the provided {@code
     * parameters} treating {@link String}s as {@link CharSequence}s if {@code useCharSequence} is
     * {@code true} (this is required for transacts that accept {@link CharSequence} instances).
     *
     * <p>To facilitate invoking direct transacts for permission tests this method will check if
     * the {@link RemoteException} caught as a result of running the transact is a {@link
     * SecurityException}; if so the {@code SecurityException} is rethrown as is, otherwise an
     * {@link BasePermissionTester.UnexpectedPermissionTestFailureException} is thrown.
     *
     * @return the {@link Parcel} received as a result of invoking the transact
     */
    public Parcel invokeTransactWithCharSequence(String service, String descriptor,
            String transactName, boolean useCharSequence, Object... parameters) {
        IBinder binder = null;
        try {
            // Obtain the IBinder for the specified service.
            binder = (IBinder) Class.forName("android.os.ServiceManager")
                    .getMethod("getService", String.class).invoke(null, service);
            if (binder == null) {
                throw new BasePermissionTester.BypassTestException("The " + service
                        + " service guarded by this permission is not available on this device");
            }
        } catch (ReflectiveOperationException e) {
            throw new BasePermissionTester.UnexpectedPermissionTestFailureException(e);
        }
        return invokeTransactWithCharSequence(binder, descriptor, transactName, useCharSequence,
                parameters);
    }

    /**
     * Invokes a direct binder transact against the specified {@code binder} using the {@code
     * descriptor}; the service's {@code transactName} method is invoked with the provided {@code
     * parameters} treating {@link String}s as {@link CharSequence}s if {@code useCharSequence} is
     * {@code true} (this is required for transacts that accept {@link CharSequence} instances).
     *
     * <p>To facilitate invoking direct transacts for permission tests this method will check if
     * the {@link RemoteException} caught as a result of running the transact is a {@link
     * SecurityException}; if so the {@code SecurityException} is rethrown as is, otherwise an
     * {@link BasePermissionTester.UnexpectedPermissionTestFailureException} is thrown.
     *
     * @return the {@link Parcel} received as a result of invoking the transact
     */
    public Parcel invokeTransactWithCharSequence(IBinder binder, String descriptor,
            String transactName, boolean useCharSequence, Object... parameters) {
        if (!descriptorContainsTransactName(descriptor, transactName)) {
            throw new BasePermissionTester.UnexpectedPermissionTestFailureException(
                    "Transact action " + transactName + " does not have a corresponding ID for SDK "
                            + mDeviceApiLevel);
        }
        int transactId = getTransactId(descriptor, transactName);
        try {
            Parcel reply = Parcel.obtain();
            // Write all of the provided parameters to the data Parcel to be passed to the transact.
            // Each parameter must be written to the Parcel based on its class; if any new classes
            // need to be supported for direct transacts they should be added here with an
            // appropriate means of writing the instance of the class to the Parcel.
            Parcel data = Parcel.obtain();
            data.writeInterfaceToken(descriptor);
            for (Object parameter : parameters) {
                if (parameter instanceof CharSequence && useCharSequence) {
                    if (parameter == null) {
                        data.writeInt(0);
                    } else {
                        data.writeInt(1);
                        TextUtils.writeToParcel((CharSequence) parameter, data, 0);
                    }
                } else if (parameter instanceof String) {
                    data.writeString((String) parameter);
                } else if (parameter instanceof Long) {
                    data.writeLong((Long) parameter);
                } else if (parameter instanceof Integer) {
                    data.writeInt((Integer) parameter);
                } else if (parameter instanceof Boolean) {
                    data.writeInt((Boolean) parameter ? 1 : 0);
                } else if (parameter instanceof int[]) {
                    data.writeIntArray((int[]) parameter);
                } else if (parameter instanceof byte[]) {
                    data.writeByteArray((byte[]) parameter);
                } else if (parameter instanceof IInterface) {
                    data.writeStrongBinder(
                            parameter != null ? ((IInterface) parameter).asBinder() : null);
                } else if (parameter instanceof IBinder) {
                    data.writeStrongBinder((IBinder) parameter);
                } else if (parameter instanceof byte[]) {
                    data.writeByteArray((byte[]) parameter);
                } else if (parameter instanceof ComponentName) {
                    if (parameter == null) {
                        data.writeInt(0);
                    } else {
                        data.writeInt(1);
                        ((ComponentName) parameter).writeToParcel(data, 0);
                    }
                } else if (parameter instanceof Uri) {
                    data.writeInt(1);
                    ((Uri) parameter).writeToParcel(data, 0);
                } else if (parameter instanceof String[]) {
                    data.writeStringArray((String[]) parameter);
                } else if (parameter instanceof Account) {
                    data.writeInt(1);
                    ((Account) parameter).writeToParcel(data, 0);
                } else if (parameter instanceof AccessibilityServiceInfo) {
                    data.writeInt(1);
                    ((AccessibilityServiceInfo) parameter).writeToParcel(data, 0);
                } else if (parameter instanceof ParcelUuid) {
                    data.writeInt(1);
                    ((ParcelUuid) parameter).writeToParcel(data, 0);
                } else if (parameter instanceof PendingIntent) {
                    data.writeInt(1);
                    ((PendingIntent) parameter).writeToParcel(data, 0);
                } else if (parameter instanceof NetworkCapabilities) {
                    data.writeInt(1);
                    ((NetworkCapabilities) parameter).writeToParcel(data, 0);
                } else if (parameter instanceof Parcelable) {
                    data.writeInt(1);
                    ((Parcelable) parameter).writeToParcel(data, 0);
                }
            }
            binder.transact(transactId, data, reply, 0);
            reply.readException();
            return reply;
        } catch (RemoteException re) {
            Throwable cause = re.getCause();
            if (cause != null && cause instanceof SecurityException) {
                throw (SecurityException) cause;
            } else {
                throw new BasePermissionTester.UnexpectedPermissionTestFailureException(re);
            }
        }
    }
}
