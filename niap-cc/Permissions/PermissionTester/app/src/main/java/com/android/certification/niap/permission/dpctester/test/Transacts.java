package com.android.certification.niap.permission.dpctester.test;
/*
 * Copyright (C) 2024 The Android Open Source Project
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
import android.content.Context;

public class Transacts {
    // Following are the service and descriptor values for exported system services.
    public static final String ACCESSIBILITY_SERVICE = Context.ACCESSIBILITY_SERVICE;
    public static final String ACTIVITY_SERVICE = Context.ACTIVITY_SERVICE;
    public static final String ACTIVITY_TASK_SERVICE = "activity_task";
    public static final String ALARM_SERVICE = Context.ALARM_SERVICE;
    public static final String APP_OPS_SERVICE = Context.APP_OPS_SERVICE;
    public static final String APPWIDGET_SERVICE = Context.APPWIDGET_SERVICE;
    public static final String AUDIO_SERVICE = Context.AUDIO_SERVICE;
    public static final String AUTH_SERVICE = "auth";
    public static final String BACKUP_SERVICE = "backup";
    public static final String BATTERY_STATS_SERVICE = "batterystats";
    public static final String BIOMETRIC_SERVICE = Context.BIOMETRIC_SERVICE;
    public static final String CAMERA_SERVICE = "media.camera";
    public static final String CLIPBOARD_SERVICE = Context.CLIPBOARD_SERVICE;
    public static final String COMPANION_DEVICE_SERVICE = Context.COMPANION_DEVICE_SERVICE;
    public static final String CONNECTIVITY_SERVICE = Context.CONNECTIVITY_SERVICE;
    public static final String CROSS_PROFILE_APPS_SERVICE = Context.CROSS_PROFILE_APPS_SERVICE;
    public static final String DEVICE_POLICY_SERVICE = Context.DEVICE_POLICY_SERVICE;
    public static final String DEVICE_STATE_SERVICE = "device_state";
    public static final String DISPLAY_SERVICE = Context.DISPLAY_SERVICE;
    public static final String DOMAIN_VERIFICATION_SERVICE = Context.DOMAIN_VERIFICATION_SERVICE;
    public static final String DREAMS_SERVICE = "dreams";
    public static final String DROPBOX_SERVICE = Context.DROPBOX_SERVICE;
    public static final String EUICC_CONTROLLER_SERVICE = "econtroller";
    public static final String FACE_SERVICE = "face";
    public static final String FINGERPRINT_SERVICE = Context.FINGERPRINT_SERVICE;
    public static final String FONT_SERVICE = "font";
    public static final String GAME_SERVICE = Context.GAME_SERVICE;
    public static final String INPUT_SERVICE = Context.INPUT_SERVICE;
    public static final String ISUB_SERVICE = "isub";
    public static final String LOCK_SETTINGS_SERVICE = "lock_settings";
    public static final String MEDIA_PROJECTION_SERVICE = Context.MEDIA_PROJECTION_SERVICE;
    public static final String MEDIA_SESSION_SERVICE = "media_session";
    public static final String MOUNT_SERVICE = "mount";
    public static final String MUSIC_RECOGNITION_SERVICE = "music_recognition";
    public static final String NET_POLICY_SERVICE = "netpolicy";
    public static final String NETWORK_MANAGEMENT_SERVICE = "network_management";
    public static final String NETWORK_STATS_SERVICE = Context.NETWORK_STATS_SERVICE;
    public static final String NFC_SERVICE = Context.NFC_SERVICE;
    public static final String NOTIFICATION_SERVICE = Context.NOTIFICATION_SERVICE;
    public static final String PACKAGE_SERVICE = "package";
    public static final String PEOPLE_SERVICE = Context.PEOPLE_SERVICE;
    public static final String PERMISSION_CHECKER_SERVICE = "permission_checker";
    public static final String PERMISSION_MANAGER_SERVICE = "permissionmgr";
    public static final String PLATFORM_COMPAT_SERVICE = "platform_compat";
    public static final String POWER_SERVICE = Context.POWER_SERVICE;
    public static final String REBOOT_READINESS_SERVICE = "reboot_readiness";
    public static final String RESOURCE_MANAGER_SERVICE = "media.resource_manager";
    public static final String RESOURCE_OBSERVER_SERVICE = "media.resource_observer";
    public static final String ROLE_SERVICE = Context.ROLE_SERVICE;
    public static final String ROLLBACK_SERVICE = "rollback";
    public static final String SENSOR_PRIVACY_SERVICE = "sensor_privacy";
    public static final String SHORTCUT_SERVICE = Context.SHORTCUT_SERVICE;
    public static final String SLICE_SERVICE = "slice";
    public static final String SMART_SPACE_SERVICE = "smartspace";
    public static final String SOUND_TRIGGER_SERVICE = "soundtrigger";
    public static final String SPEECH_RECOGNITION_SERVICE = "speech_recognition";
    public static final String STATUS_BAR_SERVICE = "statusbar";
    public static final String SURFACE_FLINGER_SERVICE = "SurfaceFlinger";
    public static final String TELEPHONY_IMS_SERVICE = "telephony_ims";
    public static final String TELEPHONY_SERVICE = Context.TELEPHONY_SERVICE;
    public static final String TIME_DETECTOR_SERVICE = "time_detector";
    public static final String TRANSLATION_SERVICE = "translation";
    public static final String TRUST_SERVICE = "trust";
    public static final String TV_INPUT_SERVICE = "tv_input";
    public static final String UI_MODE_SERVICE = Context.UI_MODE_SERVICE;
    public static final String URI_GRANTS_SERVICE = "uri_grants";
    public static final String USB_SERVICE = Context.USB_SERVICE;
    public static final String UWB_SERVICE = "uwb";
    public static final String VIBRATOR_SERVICE = Context.VIBRATOR_SERVICE;
    public static final String VOICE_INTERACTION_SERVICE = "voiceinteraction";
    public static final String VPN_SERVICE = Context.VPN_MANAGEMENT_SERVICE;
    public static final String VR_SERVICE = "vrmanager";
    public static final String WALLPAPER_SERVICE = Context.WALLPAPER_SERVICE;
    public static final String WIFI_SERVICE = Context.WIFI_SERVICE;
    public static final String WINDOW_SERVICE = Context.WINDOW_SERVICE;
    //For Android T
    public static final String AMBIENT_CONTEXT_MANAGER_SERVICE = "ambient_context";
    public static final String SAFETY_CENTER_MANAGER_SERVICE = "safety_center";
    public static final String VIRTUAL_DEVICE_MANAGER_SERVICE = "virtualdevice";
    public static final String ATTESTATION_VERIFICATION_SERVICE = "attestation_verification";
    //CloudSearchManager
    public static final String CLOUDSEARCH_SERVICE = "cloudsearch";//Context.CLOUD_SEARCH
    public static final String ETHERNET_MANAGER_SERVICE = "ethernet";
    public static final String FILE_INTEGRITY_SERVICE = "file_integrity";//PersistentDataBlockService

    public static final String WALLPAPER_EFFECTS_GENERATION_SERVICE =  "wallpaper_effects_generation";//PersistentDataBlockService
    public static final String NSD_SERVICE = "servicediscovery";
    public static final String FEATURE_FLAGS_SERVICE = "feature_flags";
    public static final String GRAMMATICAL_INFLECTION_SERVICE = "grammatical_inflection";//PersistentDataBlockService
    public static final String BACKGROUND_INSTALL_CONTROL_SERVICE = "background_install_control";;//PersistentDataBlockService
    public static final String ON_DEVICE_INTELLIGENCE_SERVICE = "on_device_intelligence";//PersistentDataBlockService
    public static final String CONTEXTUAL_SEARCH_SERVICE = "contextual_search";//PersistentDataBlockService
    public static final String USAGE_STATS_SERVICE = "usagestats";//PersistentDataBlockService
    public static final String MEDIA_ROUTER_SERVICE = "media_router";//PersistentDataBlockService
    public static final String PDB_SERVICE = "persistent_data_block";//PersistentDataBlockService

    public static final String ISUB_DESCRIPTOR = "com.android.internal.telephony.ISub";
    //Generate from log cat from synonim db for porting
    public static final String GRAMMATICAL_INFLECTION_DESCRIPTOR = "android.app.IGrammaticalInflectionManager";
    public static final String VIBRATOR_DESCRIPTOR = "android.os.IVibratorService";
    public static final String MUSIC_RECOGNITION_DESCRIPTOR = "android.media.musicrecognition.IMusicRecognitionManager";
    public static final String WINDOW_DESCRIPTOR = "android.view.IWindowManager";
    public static final String ROLLBACK_DESCRIPTOR = "android.content.rollback.IRollbackManager";
    public static final String DEVICELOCK_DESCRIPTOR = "android.devicelock.IDeviceLockService";
    public static final String NETWORK_STATS_DESCRIPTOR = "android.net.INetworkStatsService";
    public static final String DOMAIN_VERIFICATION_DESCRIPTOR = "android.content.pm.verify.domain.IDomainVerificationManager";
    public static final String NETWORK_MANAGEMENT_DESCRIPTOR = "android.os.INetworkManagementService";
    public static final String ALARM_DESCRIPTOR = "android.app.IAlarmManager";
    public static final String DISPLAY_DESCRIPTOR = "android.hardware.display.IDisplayManager";
    public static final String KEY_CHAIN_DESCRIPTOR = "android.security.IKeyChainService";
    public static final String PERMISSION_CHECKER_DESCRIPTOR = "android.permission.IPermissionChecker";
    public static final String DREAMS_DESCRIPTOR = "android.service.dreams.IDreamManager";
    public static final String RESOURCE_OBSERVER_DESCRIPTOR = "android.media.IResourceObserverService";
    public static final String SLICE_DESCRIPTOR = "android.app.slice.ISliceManager";
    public static final String BACKUP_DESCRIPTOR = "android.app.backup.IBackupManager";
    public static final String SUBSCRIPTION_DESCRIPTOR = "com.android.internal.telephony.ISub";
    public static final String FILE_INTEGRITY_DESCRIPTOR = "android.security.IFileIntegrityService";
    public static final String HEALTH_CONNECT_DESCRIPTOR = "android.health.connect.aidl.IHealthConnectService";
    public static final String ROLE_DESCRIPTOR = "android.app.role.IRoleManager";
    public static final String INPUTMETHOD_DESCRIPTOR = "com.android.internal.view.IInputMethodManager";
    public static final String ON_DEVICE_INTELLINGENCE_DESCRIPTOR = "android.app.ondeviceintelligence.IOnDeviceIntelligenceManager";
    public static final String TELEPHONY_IMS_DESCRIPTOR = "android.telephony.ims.aidl.IImsRcsController";
    public static final String CONNECTIVITY_DESCRIPTOR = "android.net.IConnectivityManager";
    public static final String PDB_DESCRIPTOR = "android.service.persistentdata.IPersistentDataBlockService";
    public static final String TIME_DETECTOR_DESCRIPTOR = "android.app.timedetector.ITimeDetectorService";
    public static final String DEVICE_STATE_DESCRIPTOR = "android.hardware.devicestate.IDeviceStateManager";
    public static final String COMPANION_DEVICE_DESCRIPTOR = "android.companion.ICompanionDeviceManager";
    public static final String FEATURE_FLAGS_DESCRIPTOR = "android.flags.IFeatureFlags";
    public static final String INPUT_DESCRIPTOR = "android.hardware.input.IInputManager";
    public static final String URI_GRANTS_DESCRIPTOR = "android.app.IUriGrantsManager";
    public static final String WIFI_DESCRIPTOR = "android.net.wifi.IWifiManager";
    public static final String DROPBOX_DESCRIPTOR = "com.android.internal.os.IDropBoxManagerService";
    public static final String UI_MODE_DESCRIPTOR = "android.app.IUiModeManager";
    public static final String NFC_DESCRIPTOR = "android.nfc.INfcAdapter";
    public static final String AUDIO_DESCRIPTOR = "android.media.IAudioService";
    public static final String SOUND_TRIGGER_SESSION_DESCRIPTOR = "com.android.internal.app.ISoundTriggerSession";
    public static final String CONTEXTUAL_SEARCH_DESCRIPTOR = "android.app.contextualsearch.IContextualSearchManager";
    public static final String APPWIDGET_DESCRIPTOR = "com.android.internal.appwidget.IAppWidgetService";
    public static final String ACCESSIBILITY_DESCRIPTOR = "android.view.accessibility.IAccessibilityManager";
    public static final String VR_DESCRIPTOR = "android.service.vr.IVrManager";
    public static final String CLOUDSEARCH_DESCRIPTOR = "android.app.cloudsearch.ICloudSearchManager";
    public static final String RESOURCE_MANAGER_DESCRIPTOR = "android.media.IResourceManagerService";
    public static final String SENSOR_PRIVACY_DESCRIPTOR = "android.hardware.ISensorPrivacyManager";
    public static final String NSD_DESCRIPTOR = "android.net.nsd.INsdManager";
    public static final String MOUNT_DESCRIPTOR = "android.os.storage.IStorageManager";
    public static final String TV_INPUT_DESCRIPTOR = "android.media.tv.ITvInputManager";
    public static final String LOCK_SETTINGS_DESCRIPTOR = "com.android.internal.widget.ILockSettings";
    public static final String SPEECH_RECOGNITION_DESCRIPTOR = "android.speech.IRecognitionServiceManager";
    public static final String WALLPAPER_EFFECTS_GENERATION_DESCRIPTOR = "android.app.wallpapereffectsgeneration.IWallpaperEffectsGenerationManager";
    public static final String POWER_DESCRIPTOR = "android.os.IPowerManager";
    public static final String UWB_DESCRIPTOR = "android.uwb.IUwbAdapter";
    public static final String REBOOT_READINESS_DESCRIPTOR = "android.scheduling.IRebootReadinessManager";
    public static final String WALLPAPER_DESCRIPTOR = "android.app.IWallpaperManager";
    public static final String AUTH_DESCRIPTOR = "android.hardware.biometrics.IAuthService";
    public static final String BATTERY_STATS_DESCRIPTOR = "com.android.internal.app.IBatteryStats";
    public static final String PERMISSION_MANAGER_DESCRIPTOR = "android.permission.IPermissionManager";
    public static final String NOTIFICATION_DESCRIPTOR = "android.app.INotificationManager";
    public static final String USB_DESCRIPTOR = "android.hardware.usb.IUsbManager";
    public static final String FINGERPRINT_DESCRIPTOR = "android.hardware.fingerprint.IFingerprintService";
    public static final String ACTIVITY_DESCRIPTOR = "android.app.IActivityManager";
    public static final String FACE_DESCRIPTOR = "android.hardware.face.IFaceService";
    public static final String USAGE_STATS_DESCRIPTOR = "android.app.usage.IUsageStatsManager";
    public static final String SAFETY_CENTER_MANAGER_MANAGER_DESCRIPTOR = "android.safetycenter.ISafetyCenterManager";
    public static final String PEOPLE_DESCRIPTOR = "android.app.people.IPeopleManager";
    public static final String TRANSLATION_DESCRIPTOR = "android.view.translation.ITranslationManager";
    public static final String SMART_SPACE_DESCRIPTOR = "android.app.smartspace.ISmartspaceManager";
    public static final String CLIPBOARD_DESCRIPTOR = "android.content.IClipboard";
    public static final String CAMERA_DESCRIPTOR = "android.hardware.ICameraService";
    public static final String VIRTUAL_DEVICE_MANAGER_DESCRIPTOR = "android.companion.virtual.IVirtualDeviceManager";
    public static final String LOCALE_DESCRIPTOR = "android.app.ILocaleManager";
    public static final String WEARABLES_DESCRIPTOR = "android.app.wearable.IWearableSensingManager";
    public static final String STATUS_BAR_DESCRIPTOR = "com.android.internal.statusbar.IStatusBarService";
    public static final String SHORTCUT_DESCRIPTOR = "android.content.pm.IShortcutService";
    public static final String CREDENTIAL_DESCRIPTOR = "android.credentials.ICredentialManager";
    public static final String SURFACE_FLINGER_DESCRIPTOR = "android.ui.ISurfaceComposer";
    public static final String SOUND_TRIGGER_DESCRIPTOR = "com.android.internal.app.ISoundTriggerService";
    public static final String CROSS_PROFILE_APPS_DESCRIPTOR = "android.content.pm.ICrossProfileApps";
    public static final String TRUST_DESCRIPTOR = "android.app.trust.ITrustManager";
    public static final String SYSTEM_CONFIG_DESCRIPTOR = "android.os.ISystemConfig";
    public static final String APP_OPS_DESCRIPTOR = "com.android.internal.app.IAppOpsService";
    public static final String STATS_DESCRIPTOR = "android.os.IStatsManagerService";
    public static final String VPN_DESCRIPTOR = "android.net.IVpnManager";
    public static final String GAME_DESCRIPTOR = "android.app.IGameManagerService";
    public static final String DEVICE_POLICY_DESCRIPTOR = "android.app.admin.IDevicePolicyManager";
    public static final String FONT_DESCRIPTOR = "com.android.internal.graphics.fonts.IFontManager";
    public static final String TELEPHONY_DESCRIPTOR = "com.android.internal.telephony.ITelephony";
    public static final String NET_POLICY_DESCRIPTOR = "android.net.INetworkPolicyManager";
    public static final String EUICC_CONTROLLER_DESCRIPTOR = "com.android.internal.telephony.euicc.IEuiccController";
    public static final String BIOMETRIC_DESCRIPTOR = "android.hardware.biometrics.IBiometricService";
    public static final String ATTESTATION_VERIFICATION_DESCRIPTOR = "android.security.attestationverification.IAttestationVerificationManagerService";
    public static final String PACKAGE_DESCRIPTOR = "android.content.pm.IPackageManager";
    public static final String ACTIVITY_CLIENT_DESCRIPTOR = "android.app.IActivityClientController";
    public static final String AMBIENT_CONTEXT_MANAGER_DESCRIPTOR = "android.app.ambientcontext.IAmbientContextManager";
    public static final String VOICE_INTERACTION_DESCRIPTOR = "com.android.internal.app.IVoiceInteractionManagerService";
    public static final String ACTIVITY_TASK_DESCRIPTOR = "android.app.IActivityTaskManager";
    public static final String MEDIA_PROJECTION_DESCRIPTOR = "android.media.projection.IMediaProjectionManager";
    public static final String BACKGROUND_INSTALL_CONTROL_DESCRIPTOR = "android.content.pm.IBackgroundInstallControlService";
    public static final String PLATFORM_COMPAT_DESCRIPTOR = "com.android.internal.compat.IPlatformCompat";
    public static final String MEDIA_SESSION_DESCRIPTOR = "android.media.session.ISessionManager";
    public static final String MEDIA_ROUTER_DESCRIPTOR = "android.media.IMediaRouterService";


    /* For Android 36*/
    public static final String INTRUSION_DETECTION_DESCRIPTOR =
            "android.security.intrusiondetection.IIntrusionDetectionService";
    public static final String INTRUSION_DETECTION_SERVICE = "intrusion_detection";
    public static final String enable = "enable";
    public static final String disable  = "disable";
    public static final String addStateCallback  = "addStateCallback";

    /* For IInputManger */

    public static final String registerKeyGestureEventListener  = "registerKeyGestureEventListener";
    public static final String registerKeyEventActivityListener = "registerKeyEventActivityListener";
    public static final String unregisterKeyEventActivityListener = "unregisterKeyEventActivityListener";
    /*For IHealthConnectService */
    public static final String getChangesForBackup  = "getChangesForBackup";
    public static final String canRestore  = "canRestore";
    public static final String restoreChanges  = "restoreChanges";

    /*For IHealthConnectService */
    public static final String TRADE_IN_MODE_DESCRIPTOR = "android.os.ITradeInMode";
    public static final String TRADE_IN_MODE_SERVICE = "tradeinmode";
    public static final String start = "start";
    public static final String enterEvaluationMode = "enterEvaluationMode";

    public static final String VIBRATOR_MANAGER_DESCRIPTOR = "android.os.IVibratorManagerService";
    public static final String startVendorVibrationSession="startVendorVibrationSession";

    public static final String AUDIO_POLICY_SERVICE_DESCRIPTOR = "android.media.IAudioPolicyService";
    public static final String AUDIO_POLICY_SERVICE  = "audio_policy";
    public static final String getInputForAttr = "getInputForAttr";

    public static final String AUTHENTICATION_POLICY_SERVICE = "authentication_policy";
    public static final String AUTHENTICATION_POLICY_SERVICE_DESCRIPTOR
            = "android.security.authenticationpolicy.IAuthenticationPolicyService";

    public static final String enableSecureLockDevice = "enableSecureLockDevice";//
    public static final String disableSecureLockDevice = "disableSecureLockDevice";//


}
