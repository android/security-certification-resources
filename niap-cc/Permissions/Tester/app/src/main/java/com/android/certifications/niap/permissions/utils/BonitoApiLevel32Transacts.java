package com.android.certifications.niap.permissions.utils;

import java.util.HashMap;
import java.util.Map;

public class BonitoApiLevel32Transacts extends Transacts {
    public BonitoApiLevel32Transacts() {
        mDeviceApiLevel = 32;
        Map<String, Integer> transactIds;

        transactIds = new HashMap<>();
        // Failed to obtain transactId of updateState under com.android.internal.app.IVoiceInteractionManagerService: No field TRANSACTION_updateState in class Lcom/android/internal/app/IVoiceInteractionManagerService$Stub; (declaration of 'com.android.internal.app.IVoiceInteractionManagerService$Stub' appears in /system/framework/framework.jar!classes4.dex)
        // Failed to obtain transactId of getActiveServiceComponentName under com.android.internal.app.IVoiceInteractionManagerService: No field TRANSACTION_getActiveServiceComponentName in class Lcom/android/internal/app/IVoiceInteractionManagerService$Stub; (declaration of 'com.android.internal.app.IVoiceInteractionManagerService$Stub' appears in /system/framework/framework.jar!classes4.dex)
        // Failed to obtain transactId of updateKeyphraseSoundModel under com.android.internal.app.IVoiceInteractionManagerService: No field TRANSACTION_updateKeyphraseSoundModel in class Lcom/android/internal/app/IVoiceInteractionManagerService$Stub; (declaration of 'com.android.internal.app.IVoiceInteractionManagerService$Stub' appears in /system/framework/framework.jar!classes4.dex)
        // Failed to obtain transactId of isSessionRunning under com.android.internal.app.IVoiceInteractionManagerService: No field TRANSACTION_isSessionRunning in class Lcom/android/internal/app/IVoiceInteractionManagerService$Stub; (declaration of 'com.android.internal.app.IVoiceInteractionManagerService$Stub' appears in /system/framework/framework.jar!classes4.dex)
        mDescriptorTransacts.put(VOICE_INTERACTION_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(bootFinished, 1);
        transactIds.put(showCpu, 1000);
        mDescriptorTransacts.put(SURFACE_FLINGER_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of resetLockout under android.hardware.fingerprint.IFingerprintService: No field TRANSACTION_resetLockout in class Landroid/hardware/fingerprint/IFingerprintService$Stub; (declaration of 'android.hardware.fingerprint.IFingerprintService$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of cancelAuthenticationFromService under android.hardware.fingerprint.IFingerprintService: No field TRANSACTION_cancelAuthenticationFromService in class Landroid/hardware/fingerprint/IFingerprintService$Stub; (declaration of 'android.hardware.fingerprint.IFingerprintService$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of resetTimeout under android.hardware.fingerprint.IFingerprintService: No field TRANSACTION_resetTimeout in class Landroid/hardware/fingerprint/IFingerprintService$Stub; (declaration of 'android.hardware.fingerprint.IFingerprintService$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of cancelEnrollment under android.hardware.fingerprint.IFingerprintService: No field TRANSACTION_cancelEnrollment in class Landroid/hardware/fingerprint/IFingerprintService$Stub; (declaration of 'android.hardware.fingerprint.IFingerprintService$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(FINGERPRINT_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of retainSubscriptionsForFactoryReset under com.android.internal.telephony.euicc.IEuiccController: No field TRANSACTION_retainSubscriptionsForFactoryReset in class Lcom/android/internal/telephony/euicc/IEuiccController$Stub; (declaration of 'com.android.internal.telephony.euicc.IEuiccController$Stub' appears in /system/framework/framework.jar!classes4.dex)
        mDescriptorTransacts.put(EUICC_CONTROLLER_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of takePersistableUriPermission under android.app.IUriGrantsManager: No field TRANSACTION_takePersistableUriPermission in class Landroid/app/IUriGrantsManager$Stub; (declaration of 'android.app.IUriGrantsManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of getGrantedUriPermissions under android.app.IUriGrantsManager: No field TRANSACTION_getGrantedUriPermissions in class Landroid/app/IUriGrantsManager$Stub; (declaration of 'android.app.IUriGrantsManager$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(URI_GRANTS_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of verifyCredential under com.android.internal.widget.ILockSettings: No field TRANSACTION_verifyCredential in class Lcom/android/internal/widget/ILockSettings$Stub; (declaration of 'com.android.internal.widget.ILockSettings$Stub' appears in /system/framework/framework.jar!classes4.dex)
        mDescriptorTransacts.put(LOCK_SETTINGS_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of getAvailableGameModes under android.app.IGameManagerService: No field TRANSACTION_getAvailableGameModes in class Landroid/app/IGameManagerService$Stub; (declaration of 'android.app.IGameManagerService$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(GAME_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of reportChangeByUid under com.android.internal.compat.IPlatformCompat: No field TRANSACTION_reportChangeByUid in class Lcom/android/internal/compat/IPlatformCompat$Stub; (declaration of 'com.android.internal.compat.IPlatformCompat$Stub' appears in /system/framework/framework.jar!classes4.dex)
        // Failed to obtain transactId of removeOverridesOnReleaseBuilds under com.android.internal.compat.IPlatformCompat: No field TRANSACTION_removeOverridesOnReleaseBuilds in class Lcom/android/internal/compat/IPlatformCompat$Stub; (declaration of 'com.android.internal.compat.IPlatformCompat$Stub' appears in /system/framework/framework.jar!classes4.dex)
        // Failed to obtain transactId of clearOverridesForTest under com.android.internal.compat.IPlatformCompat: No field TRANSACTION_clearOverridesForTest in class Lcom/android/internal/compat/IPlatformCompat$Stub; (declaration of 'com.android.internal.compat.IPlatformCompat$Stub' appears in /system/framework/framework.jar!classes4.dex)
        mDescriptorTransacts.put(PLATFORM_COMPAT_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of beginRecognition under android.media.musicrecognition.IMusicRecognitionManager: No field TRANSACTION_beginRecognition in class Landroid/media/musicrecognition/IMusicRecognitionManager$Stub; (declaration of 'android.media.musicrecognition.IMusicRecognitionManager$Stub' appears in /system/framework/framework.jar!classes2.dex)
        mDescriptorTransacts.put(MUSIC_RECOGNITION_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of reboot under android.os.IPowerManager: No field TRANSACTION_reboot in class Landroid/os/IPowerManager$Stub; (declaration of 'android.os.IPowerManager$Stub' appears in /system/framework/framework.jar!classes2.dex)
        // Failed to obtain transactId of isAmbientDisplaySuppressedForTokenByApp under android.os.IPowerManager: No field TRANSACTION_isAmbientDisplaySuppressedForTokenByApp in class Landroid/os/IPowerManager$Stub; (declaration of 'android.os.IPowerManager$Stub' appears in /system/framework/framework.jar!classes2.dex)
        // Failed to obtain transactId of setPowerSaveMode under android.os.IPowerManager: No field TRANSACTION_setPowerSaveMode in class Landroid/os/IPowerManager$Stub; (declaration of 'android.os.IPowerManager$Stub' appears in /system/framework/framework.jar!classes2.dex)
        // Failed to obtain transactId of setDynamicPowerSaveHint under android.os.IPowerManager: No field TRANSACTION_setDynamicPowerSaveHint in class Landroid/os/IPowerManager$Stub; (declaration of 'android.os.IPowerManager$Stub' appears in /system/framework/framework.jar!classes2.dex)
        // Failed to obtain transactId of setBatteryDischargePrediction under android.os.IPowerManager: No field TRANSACTION_setBatteryDischargePrediction in class Landroid/os/IPowerManager$Stub; (declaration of 'android.os.IPowerManager$Stub' appears in /system/framework/framework.jar!classes2.dex)
        mDescriptorTransacts.put(POWER_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of notifySystemEvent under android.hardware.ICameraService: No field TRANSACTION_notifySystemEvent in class Landroid/hardware/ICameraService$Stub; (declaration of 'android.hardware.ICameraService$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of injectCamera under android.hardware.ICameraService: No field TRANSACTION_injectCamera in class Landroid/hardware/ICameraService$Stub; (declaration of 'android.hardware.ICameraService$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(CAMERA_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of isRecognitionActive under com.android.internal.app.ISoundTriggerService: No field TRANSACTION_isRecognitionActive in class Lcom/android/internal/app/ISoundTriggerService$Stub; (declaration of 'com.android.internal.app.ISoundTriggerService$Stub' appears in /system/framework/framework.jar!classes4.dex)
        // Failed to obtain transactId of attachAsOriginator under com.android.internal.app.ISoundTriggerService: No field TRANSACTION_attachAsOriginator in class Lcom/android/internal/app/ISoundTriggerService$Stub; (declaration of 'com.android.internal.app.ISoundTriggerService$Stub' appears in /system/framework/framework.jar!classes4.dex)
        // Failed to obtain transactId of attachAsMiddleman under com.android.internal.app.ISoundTriggerService: No field TRANSACTION_attachAsMiddleman in class Lcom/android/internal/app/ISoundTriggerService$Stub; (declaration of 'com.android.internal.app.ISoundTriggerService$Stub' appears in /system/framework/framework.jar!classes4.dex)
        mDescriptorTransacts.put(SOUND_TRIGGER_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of grantPermissionFromUser under android.app.slice.ISliceManager: No field TRANSACTION_grantPermissionFromUser in class Landroid/app/slice/ISliceManager$Stub; (declaration of 'android.app.slice.ISliceManager$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(SLICE_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of getUiPackage under android.hardware.biometrics.IAuthService: No field TRANSACTION_getUiPackage in class Landroid/hardware/biometrics/IAuthService$Stub; (declaration of 'android.hardware.biometrics.IAuthService$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(AUTH_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of isControllerAlwaysOnSupported under android.nfc.INfcAdapter: No field TRANSACTION_isControllerAlwaysOnSupported in class Landroid/nfc/INfcAdapter$Stub; (declaration of 'android.nfc.INfcAdapter$Stub' appears in /system/framework/framework.jar!classes2.dex)
        mDescriptorTransacts.put(NFC_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of setRingtonePlayer under android.media.IAudioService: No field TRANSACTION_setRingtonePlayer in class Landroid/media/IAudioService$Stub; (declaration of 'android.media.IAudioService$Stub' appears in /system/framework/framework.jar!classes2.dex)
        // Failed to obtain transactId of getRingtonePlayer under android.media.IAudioService: No field TRANSACTION_getRingtonePlayer in class Landroid/media/IAudioService$Stub; (declaration of 'android.media.IAudioService$Stub' appears in /system/framework/framework.jar!classes2.dex)
        // Failed to obtain transactId of isAudioServerRunning under android.media.IAudioService: No field TRANSACTION_isAudioServerRunning in class Landroid/media/IAudioService$Stub; (declaration of 'android.media.IAudioService$Stub' appears in /system/framework/framework.jar!classes2.dex)
        // Failed to obtain transactId of getDeviceVolumeBehavior under android.media.IAudioService: No field TRANSACTION_getDeviceVolumeBehavior in class Landroid/media/IAudioService$Stub; (declaration of 'android.media.IAudioService$Stub' appears in /system/framework/framework.jar!classes2.dex)
        mDescriptorTransacts.put(AUDIO_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of noteStartAudio under com.android.internal.app.IBatteryStats: No field TRANSACTION_noteStartAudio in class Lcom/android/internal/app/IBatteryStats$Stub; (declaration of 'com.android.internal.app.IBatteryStats$Stub' appears in /system/framework/framework.jar!classes4.dex)
        // Failed to obtain transactId of getAwakeTimeBattery under com.android.internal.app.IBatteryStats: No field TRANSACTION_getAwakeTimeBattery in class Lcom/android/internal/app/IBatteryStats$Stub; (declaration of 'com.android.internal.app.IBatteryStats$Stub' appears in /system/framework/framework.jar!classes4.dex)
        mDescriptorTransacts.put(BATTERY_STATS_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of onApplicationActive under android.content.pm.IShortcutService: No field TRANSACTION_onApplicationActive in class Landroid/content/pm/IShortcutService$Stub; (declaration of 'android.content.pm.IShortcutService$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(SHORTCUT_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(unregisterObserver, 2);
        mDescriptorTransacts.put(RESOURCE_OBSERVER_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of setWallpaper under android.app.IWallpaperManager: No field TRANSACTION_setWallpaper in class Landroid/app/IWallpaperManager$Stub; (declaration of 'android.app.IWallpaperManager$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(WALLPAPER_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of getVtDataUsage under com.android.internal.telephony.ITelephony: No field TRANSACTION_getVtDataUsage in class Lcom/android/internal/telephony/ITelephony$Stub; (declaration of 'com.android.internal.telephony.ITelephony$Stub' appears in /system/framework/framework.jar!classes4.dex)
        // Failed to obtain transactId of enableLocationUpdates under com.android.internal.telephony.ITelephony: No field TRANSACTION_enableLocationUpdates in class Lcom/android/internal/telephony/ITelephony$Stub; (declaration of 'com.android.internal.telephony.ITelephony$Stub' appears in /system/framework/framework.jar!classes4.dex)
        mDescriptorTransacts.put(TELEPHONY_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of destroySmartspaceSession under android.app.smartspace.ISmartspaceManager: No field TRANSACTION_destroySmartspaceSession in class Landroid/app/smartspace/ISmartspaceManager$Stub; (declaration of 'android.app.smartspace.ISmartspaceManager$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(SMART_SPACE_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of getPrimaryClipSource under android.content.IClipboard: No field TRANSACTION_getPrimaryClipSource in class Landroid/content/IClipboard$Stub; (declaration of 'android.content.IClipboard$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(CLIPBOARD_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of requestEmbeddedSubscriptionInfoListRefresh under com.android.internal.telephony.ISub: No field TRANSACTION_requestEmbeddedSubscriptionInfoListRefresh in class Lcom/android/internal/telephony/ISub$Stub; (declaration of 'com.android.internal.telephony.ISub$Stub' appears in /system/framework/framework.jar!classes4.dex)
        mDescriptorTransacts.put(ISUB_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of setAlwaysOnEffect under android.os.IVibratorService: android.os.IVibratorService$Stub
        // Failed to obtain transactId of isVibrating under android.os.IVibratorService: android.os.IVibratorService$Stub
        mDescriptorTransacts.put(VIBRATOR_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of clearInteractAcrossProfilesAppOps under android.content.pm.ICrossProfileApps: No field TRANSACTION_clearInteractAcrossProfilesAppOps in class Landroid/content/pm/ICrossProfileApps$Stub; (declaration of 'android.content.pm.ICrossProfileApps$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(CROSS_PROFILE_APPS_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of isConversation under android.app.people.IPeopleManager: No field TRANSACTION_isConversation in class Landroid/app/people/IPeopleManager$Stub; (declaration of 'android.app.people.IPeopleManager$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(PEOPLE_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of getActiveNotifications under android.app.INotificationManager: No field TRANSACTION_getActiveNotifications in class Landroid/app/INotificationManager$Stub; (declaration of 'android.app.INotificationManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of getZenRules under android.app.INotificationManager: No field TRANSACTION_getZenRules in class Landroid/app/INotificationManager$Stub; (declaration of 'android.app.INotificationManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of isNotificationPolicyAccessGrantedForPackage under android.app.INotificationManager: No field TRANSACTION_isNotificationPolicyAccessGrantedForPackage in class Landroid/app/INotificationManager$Stub; (declaration of 'android.app.INotificationManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of getEnabledNotificationListeners under android.app.INotificationManager: No field TRANSACTION_getEnabledNotificationListeners in class Landroid/app/INotificationManager$Stub; (declaration of 'android.app.INotificationManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of setToastRateLimitingEnabled under android.app.INotificationManager: No field TRANSACTION_setToastRateLimitingEnabled in class Landroid/app/INotificationManager$Stub; (declaration of 'android.app.INotificationManager$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(NOTIFICATION_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of movePackage under android.content.pm.IPackageManager: No field TRANSACTION_movePackage in class Landroid/content/pm/IPackageManager$Stub; (declaration of 'android.content.pm.IPackageManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of installExistingPackageAsUser under android.content.pm.IPackageManager: No field TRANSACTION_installExistingPackageAsUser in class Landroid/content/pm/IPackageManager$Stub; (declaration of 'android.content.pm.IPackageManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of setKeepUninstalledPackages under android.content.pm.IPackageManager: No field TRANSACTION_setKeepUninstalledPackages in class Landroid/content/pm/IPackageManager$Stub; (declaration of 'android.content.pm.IPackageManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of getRuntimePermissionsVersion under android.content.pm.IPackageManager: No field TRANSACTION_getRuntimePermissionsVersion in class Landroid/content/pm/IPackageManager$Stub; (declaration of 'android.content.pm.IPackageManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of addOnPermissionsChangeListener under android.content.pm.IPackageManager: No field TRANSACTION_addOnPermissionsChangeListener in class Landroid/content/pm/IPackageManager$Stub; (declaration of 'android.content.pm.IPackageManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of getHarmfulAppWarning under android.content.pm.IPackageManager: No field TRANSACTION_getHarmfulAppWarning in class Landroid/content/pm/IPackageManager$Stub; (declaration of 'android.content.pm.IPackageManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of isPackageDeviceAdminOnAnyUser under android.content.pm.IPackageManager: No field TRANSACTION_isPackageDeviceAdminOnAnyUser in class Landroid/content/pm/IPackageManager$Stub; (declaration of 'android.content.pm.IPackageManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of isPackageStateProtected under android.content.pm.IPackageManager: No field TRANSACTION_isPackageStateProtected in class Landroid/content/pm/IPackageManager$Stub; (declaration of 'android.content.pm.IPackageManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of getMoveStatus under android.content.pm.IPackageManager: No field TRANSACTION_getMoveStatus in class Landroid/content/pm/IPackageManager$Stub; (declaration of 'android.content.pm.IPackageManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of resetApplicationPreferences under android.content.pm.IPackageManager: No field TRANSACTION_resetApplicationPreferences in class Landroid/content/pm/IPackageManager$Stub; (declaration of 'android.content.pm.IPackageManager$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(PACKAGE_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of getSubscriptionPlans under android.net.INetworkPolicyManager: No field TRANSACTION_getSubscriptionPlans in class Landroid/net/INetworkPolicyManager$Stub; (declaration of 'android.net.INetworkPolicyManager$Stub' appears in /system/framework/framework.jar!classes2.dex)
        // Failed to obtain transactId of isUidNetworkingBlocked under android.net.INetworkPolicyManager: No field TRANSACTION_isUidNetworkingBlocked in class Landroid/net/INetworkPolicyManager$Stub; (declaration of 'android.net.INetworkPolicyManager$Stub' appears in /system/framework/framework.jar!classes2.dex)
        // Failed to obtain transactId of getUidPolicy under android.net.INetworkPolicyManager: No field TRANSACTION_getUidPolicy in class Landroid/net/INetworkPolicyManager$Stub; (declaration of 'android.net.INetworkPolicyManager$Stub' appears in /system/framework/framework.jar!classes2.dex)
        // Failed to obtain transactId of registerListener under android.net.INetworkPolicyManager: No field TRANSACTION_registerListener in class Landroid/net/INetworkPolicyManager$Stub; (declaration of 'android.net.INetworkPolicyManager$Stub' appears in /system/framework/framework.jar!classes2.dex)
        mDescriptorTransacts.put(NET_POLICY_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(set, 1);
        // Failed to obtain transactId of setTimeZone under android.app.IAlarmManager: No field TRANSACTION_setTimeZone in class Landroid/app/IAlarmManager$Stub; (declaration of 'android.app.IAlarmManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of setTime under android.app.IAlarmManager: No field TRANSACTION_setTime in class Landroid/app/IAlarmManager$Stub; (declaration of 'android.app.IAlarmManager$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(ALARM_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of getActiveProjectionInfo under android.media.projection.IMediaProjectionManager: No field TRANSACTION_getActiveProjectionInfo in class Landroid/media/projection/IMediaProjectionManager$Stub; (declaration of 'android.media.projection.IMediaProjectionManager$Stub' appears in /system/framework/framework.jar!classes2.dex)
        mDescriptorTransacts.put(MEDIA_PROJECTION_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of dismissKeyguard under android.app.IActivityClientController: No field TRANSACTION_dismissKeyguard in class Landroid/app/IActivityClientController$Stub; (declaration of 'android.app.IActivityClientController$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(ACTIVITY_CLIENT_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of getFrontActivityScreenCompatMode under android.app.IActivityTaskManager: No field TRANSACTION_getFrontActivityScreenCompatMode in class Landroid/app/IActivityTaskManager$Stub; (declaration of 'android.app.IActivityTaskManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of getTaskDescription under android.app.IActivityTaskManager: No field TRANSACTION_getTaskDescription in class Landroid/app/IActivityTaskManager$Stub; (declaration of 'android.app.IActivityTaskManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of getAssistContextExtras under android.app.IActivityTaskManager: No field TRANSACTION_getAssistContextExtras in class Landroid/app/IActivityTaskManager$Stub; (declaration of 'android.app.IActivityTaskManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of setFrontActivityScreenCompatMode under android.app.IActivityTaskManager: No field TRANSACTION_setFrontActivityScreenCompatMode in class Landroid/app/IActivityTaskManager$Stub; (declaration of 'android.app.IActivityTaskManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of getWindowOrganizerController under android.app.IActivityTaskManager: No field TRANSACTION_getWindowOrganizerController in class Landroid/app/IActivityTaskManager$Stub; (declaration of 'android.app.IActivityTaskManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of startActivityAsCaller under android.app.IActivityTaskManager: No field TRANSACTION_startActivityAsCaller in class Landroid/app/IActivityTaskManager$Stub; (declaration of 'android.app.IActivityTaskManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of dismissKeyguard under android.app.IActivityTaskManager: No field TRANSACTION_dismissKeyguard in class Landroid/app/IActivityTaskManager$Stub; (declaration of 'android.app.IActivityTaskManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of getActivityClientController under android.app.IActivityTaskManager: No field TRANSACTION_getActivityClientController in class Landroid/app/IActivityTaskManager$Stub; (declaration of 'android.app.IActivityTaskManager$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(ACTIVITY_TASK_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of setPersistentVrModeEnabled under android.service.vr.IVrManager: No field TRANSACTION_setPersistentVrModeEnabled in class Landroid/service/vr/IVrManager$Stub; (declaration of 'android.service.vr.IVrManager$Stub' appears in /system/framework/framework.jar!classes3.dex)
        // Failed to obtain transactId of getVrModeState under android.service.vr.IVrManager: No field TRANSACTION_getVrModeState in class Landroid/service/vr/IVrManager$Stub; (declaration of 'android.service.vr.IVrManager$Stub' appears in /system/framework/framework.jar!classes3.dex)
        // Failed to obtain transactId of setStandbyEnabled under android.service.vr.IVrManager: No field TRANSACTION_setStandbyEnabled in class Landroid/service/vr/IVrManager$Stub; (declaration of 'android.service.vr.IVrManager$Stub' appears in /system/framework/framework.jar!classes3.dex)
        mDescriptorTransacts.put(VR_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of overridePid under android.media.IResourceManagerService: No field TRANSACTION_overridePid in class Landroid/media/IResourceManagerService$Stub; (declaration of 'android.media.IResourceManagerService$Stub' appears in /system/framework/framework.jar!classes2.dex)
        mDescriptorTransacts.put(RESOURCE_MANAGER_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of createAssociation under android.companion.ICompanionDeviceManager: No field TRANSACTION_createAssociation in class Landroid/companion/ICompanionDeviceManager$Stub; (declaration of 'android.companion.ICompanionDeviceManager$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(COMPANION_DEVICE_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of getCacheSizeBytes under android.os.storage.IStorageManager: No field TRANSACTION_getCacheSizeBytes in class Landroid/os/storage/IStorageManager$Stub; (declaration of 'android.os.storage.IStorageManager$Stub' appears in /system/framework/framework.jar!classes2.dex)
        // Failed to obtain transactId of getEncryptionState under android.os.storage.IStorageManager: No field TRANSACTION_getEncryptionState in class Landroid/os/storage/IStorageManager$Stub; (declaration of 'android.os.storage.IStorageManager$Stub' appears in /system/framework/framework.jar!classes2.dex)
        // Failed to obtain transactId of benchmark under android.os.storage.IStorageManager: No field TRANSACTION_benchmark in class Landroid/os/storage/IStorageManager$Stub; (declaration of 'android.os.storage.IStorageManager$Stub' appears in /system/framework/framework.jar!classes2.dex)
        mDescriptorTransacts.put(MOUNT_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of suggestExternalTime under android.app.timedetector.ITimeDetectorService: No field TRANSACTION_suggestExternalTime in class Landroid/app/timedetector/ITimeDetectorService$Stub; (declaration of 'android.app.timedetector.ITimeDetectorService$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of getCapabilitiesAndConfig under android.app.timedetector.ITimeDetectorService: No field TRANSACTION_getCapabilitiesAndConfig in class Landroid/app/timedetector/ITimeDetectorService$Stub; (declaration of 'android.app.timedetector.ITimeDetectorService$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(TIME_DETECTOR_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of removeRequestRebootReadinessStatusListener under android.scheduling.IRebootReadinessManager: No field TRANSACTION_removeRequestRebootReadinessStatusListener in class Landroid/scheduling/IRebootReadinessManager$Stub; (declaration of 'android.scheduling.IRebootReadinessManager$Stub' appears in /apex/com.android.scheduling/javalib/framework-scheduling.jar)
        mDescriptorTransacts.put(REBOOT_READINESS_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of cancelRequest under android.hardware.devicestate.IDeviceStateManager: No field TRANSACTION_cancelRequest in class Landroid/hardware/devicestate/IDeviceStateManager$Stub; (declaration of 'android.hardware.devicestate.IDeviceStateManager$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(DEVICE_STATE_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of setDataSaverModeEnabled under android.os.INetworkManagementService: No field TRANSACTION_setDataSaverModeEnabled in class Landroid/os/INetworkManagementService$Stub; (declaration of 'android.os.INetworkManagementService$Stub' appears in /system/framework/framework.jar!classes2.dex)
        mDescriptorTransacts.put(NETWORK_MANAGEMENT_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of resetAppErrors under android.app.IActivityManager: No field TRANSACTION_resetAppErrors in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of bindBackupAgent under android.app.IActivityManager: No field TRANSACTION_bindBackupAgent in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of performIdleMaintenance under android.app.IActivityManager: No field TRANSACTION_performIdleMaintenance in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of setDumpHeapDebugLimit under android.app.IActivityManager: No field TRANSACTION_setDumpHeapDebugLimit in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of updateLockTaskPackages under android.app.IActivityManager: No field TRANSACTION_updateLockTaskPackages in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of getGrantedUriPermissions under android.app.IActivityManager: No field TRANSACTION_getGrantedUriPermissions in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of setHasTopUi under android.app.IActivityManager: No field TRANSACTION_setHasTopUi in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of dismissKeyguard under android.app.IActivityManager: No field TRANSACTION_dismissKeyguard in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of resumeAppSwitches under android.app.IActivityManager: No field TRANSACTION_resumeAppSwitches in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of getContentProviderExternal under android.app.IActivityManager: No field TRANSACTION_getContentProviderExternal in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of getIntentForIntentSender under android.app.IActivityManager: No field TRANSACTION_getIntentForIntentSender in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of getTaskDescription under android.app.IActivityManager: No field TRANSACTION_getTaskDescription in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of getAssistContextExtras under android.app.IActivityManager: No field TRANSACTION_getAssistContextExtras in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of unhandledBack under android.app.IActivityManager: No field TRANSACTION_unhandledBack in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of inputDispatchingTimedOut under android.app.IActivityManager: No field TRANSACTION_inputDispatchingTimedOut in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of setFrontActivityScreenCompatMode under android.app.IActivityManager: No field TRANSACTION_setFrontActivityScreenCompatMode in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of setAlwaysFinish under android.app.IActivityManager: No field TRANSACTION_setAlwaysFinish in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of startActivityFromRecents under android.app.IActivityManager: No field TRANSACTION_startActivityFromRecents in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of releasePersistableUriPermission under android.app.IActivityManager: No field TRANSACTION_releasePersistableUriPermission in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of requestBugReport under android.app.IActivityManager: No field TRANSACTION_requestBugReport in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of getFrontActivityScreenCompatMode under android.app.IActivityManager: No field TRANSACTION_getFrontActivityScreenCompatMode in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of setProcessLimit under android.app.IActivityManager: No field TRANSACTION_setProcessLimit in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of signalPersistentProcesses under android.app.IActivityManager: No field TRANSACTION_signalPersistentProcesses in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of updateConfiguration under android.app.IActivityManager: No field TRANSACTION_updateConfiguration in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of appNotRespondingViaProvider under android.app.IActivityManager: No field TRANSACTION_appNotRespondingViaProvider in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of shutdown under android.app.IActivityManager: No field TRANSACTION_shutdown in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(ACTIVITY_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of generateChallenge under android.hardware.face.IFaceService: No field TRANSACTION_generateChallenge in class Landroid/hardware/face/IFaceService$Stub; (declaration of 'android.hardware.face.IFaceService$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(FACE_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of awaken under android.service.dreams.IDreamManager: No field TRANSACTION_awaken in class Landroid/service/dreams/IDreamManager$Stub; (declaration of 'android.service.dreams.IDreamManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of isDreaming under android.service.dreams.IDreamManager: No field TRANSACTION_isDreaming in class Landroid/service/dreams/IDreamManager$Stub; (declaration of 'android.service.dreams.IDreamManager$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(DREAMS_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of setRefreshRateSwitchingType under android.hardware.display.IDisplayManager: No field TRANSACTION_setRefreshRateSwitchingType in class Landroid/hardware/display/IDisplayManager$Stub; (declaration of 'android.hardware.display.IDisplayManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of setTemporaryAutoBrightnessAdjustment under android.hardware.display.IDisplayManager: No field TRANSACTION_setTemporaryAutoBrightnessAdjustment in class Landroid/hardware/display/IDisplayManager$Stub; (declaration of 'android.hardware.display.IDisplayManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of startWifiDisplayScan under android.hardware.display.IDisplayManager: No field TRANSACTION_startWifiDisplayScan in class Landroid/hardware/display/IDisplayManager$Stub; (declaration of 'android.hardware.display.IDisplayManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of shouldAlwaysRespectAppRequestedMode under android.hardware.display.IDisplayManager: No field TRANSACTION_shouldAlwaysRespectAppRequestedMode in class Landroid/hardware/display/IDisplayManager$Stub; (declaration of 'android.hardware.display.IDisplayManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of requestColorMode under android.hardware.display.IDisplayManager: No field TRANSACTION_requestColorMode in class Landroid/hardware/display/IDisplayManager$Stub; (declaration of 'android.hardware.display.IDisplayManager$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(DISPLAY_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of requestProjection under android.app.IUiModeManager: No field TRANSACTION_requestProjection in class Landroid/app/IUiModeManager$Stub; (declaration of 'android.app.IUiModeManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of getActiveProjectionTypes under android.app.IUiModeManager: No field TRANSACTION_getActiveProjectionTypes in class Landroid/app/IUiModeManager$Stub; (declaration of 'android.app.IUiModeManager$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(UI_MODE_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of createInputConsumer under android.view.IWindowManager: No field TRANSACTION_createInputConsumer in class Landroid/view/IWindowManager$Stub; (declaration of 'android.view.IWindowManager$Stub' appears in /system/framework/framework.jar!classes3.dex)
        // Failed to obtain transactId of setAnimationScale under android.view.IWindowManager: No field TRANSACTION_setAnimationScale in class Landroid/view/IWindowManager$Stub; (declaration of 'android.view.IWindowManager$Stub' appears in /system/framework/framework.jar!classes3.dex)
        // Failed to obtain transactId of requestUserActivityNotification under android.view.IWindowManager: No field TRANSACTION_requestUserActivityNotification in class Landroid/view/IWindowManager$Stub; (declaration of 'android.view.IWindowManager$Stub' appears in /system/framework/framework.jar!classes3.dex)
        // Failed to obtain transactId of removeWindowToken under android.view.IWindowManager: No field TRANSACTION_removeWindowToken in class Landroid/view/IWindowManager$Stub; (declaration of 'android.view.IWindowManager$Stub' appears in /system/framework/framework.jar!classes3.dex)
        // Failed to obtain transactId of thawRotation under android.view.IWindowManager: No field TRANSACTION_thawRotation in class Landroid/view/IWindowManager$Stub; (declaration of 'android.view.IWindowManager$Stub' appears in /system/framework/framework.jar!classes3.dex)
        // Failed to obtain transactId of registerShortcutKey under android.view.IWindowManager: No field TRANSACTION_registerShortcutKey in class Landroid/view/IWindowManager$Stub; (declaration of 'android.view.IWindowManager$Stub' appears in /system/framework/framework.jar!classes3.dex)
        // Failed to obtain transactId of clearWindowContentFrameStats under android.view.IWindowManager: No field TRANSACTION_clearWindowContentFrameStats in class Landroid/view/IWindowManager$Stub; (declaration of 'android.view.IWindowManager$Stub' appears in /system/framework/framework.jar!classes3.dex)
        // Failed to obtain transactId of overridePendingAppTransitionRemote under android.view.IWindowManager: No field TRANSACTION_overridePendingAppTransitionRemote in class Landroid/view/IWindowManager$Stub; (declaration of 'android.view.IWindowManager$Stub' appears in /system/framework/framework.jar!classes3.dex)
        // Failed to obtain transactId of setRecentsVisibility under android.view.IWindowManager: No field TRANSACTION_setRecentsVisibility in class Landroid/view/IWindowManager$Stub; (declaration of 'android.view.IWindowManager$Stub' appears in /system/framework/framework.jar!classes3.dex)
        // Failed to obtain transactId of stopFreezingScreen under android.view.IWindowManager: No field TRANSACTION_stopFreezingScreen in class Landroid/view/IWindowManager$Stub; (declaration of 'android.view.IWindowManager$Stub' appears in /system/framework/framework.jar!classes3.dex)
        // Failed to obtain transactId of dismissKeyguard under android.view.IWindowManager: No field TRANSACTION_dismissKeyguard in class Landroid/view/IWindowManager$Stub; (declaration of 'android.view.IWindowManager$Stub' appears in /system/framework/framework.jar!classes3.dex)
        // Failed to obtain transactId of screenshotWallpaper under android.view.IWindowManager: No field TRANSACTION_screenshotWallpaper in class Landroid/view/IWindowManager$Stub; (declaration of 'android.view.IWindowManager$Stub' appears in /system/framework/framework.jar!classes3.dex)
        mDescriptorTransacts.put(WINDOW_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of clearHistory under com.android.internal.app.IAppOpsService: No field TRANSACTION_clearHistory in class Lcom/android/internal/app/IAppOpsService$Stub; (declaration of 'com.android.internal.app.IAppOpsService$Stub' appears in /system/framework/framework.jar!classes4.dex)
        transactIds.put(setUserRestriction, 34);
        // Failed to obtain transactId of getUidOps under com.android.internal.app.IAppOpsService: No field TRANSACTION_getUidOps in class Lcom/android/internal/app/IAppOpsService$Stub; (declaration of 'com.android.internal.app.IAppOpsService$Stub' appears in /system/framework/framework.jar!classes4.dex)
        transactIds.put(noteOperation, 2);
        mDescriptorTransacts.put(APP_OPS_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of temporaryEnableAccessibilityStateUntilKeyguardRemoved under android.view.accessibility.IAccessibilityManager: No field TRANSACTION_temporaryEnableAccessibilityStateUntilKeyguardRemoved in class Landroid/view/accessibility/IAccessibilityManager$Stub; (declaration of 'android.view.accessibility.IAccessibilityManager$Stub' appears in /system/framework/framework.jar!classes3.dex)
        // Failed to obtain transactId of registerUiTestAutomationService under android.view.accessibility.IAccessibilityManager: No field TRANSACTION_registerUiTestAutomationService in class Landroid/view/accessibility/IAccessibilityManager$Stub; (declaration of 'android.view.accessibility.IAccessibilityManager$Stub' appears in /system/framework/framework.jar!classes3.dex)
        // Failed to obtain transactId of setPictureInPictureActionReplacingConnection under android.view.accessibility.IAccessibilityManager: No field TRANSACTION_setPictureInPictureActionReplacingConnection in class Landroid/view/accessibility/IAccessibilityManager$Stub; (declaration of 'android.view.accessibility.IAccessibilityManager$Stub' appears in /system/framework/framework.jar!classes3.dex)
        // Failed to obtain transactId of getWindowToken under android.view.accessibility.IAccessibilityManager: No field TRANSACTION_getWindowToken in class Landroid/view/accessibility/IAccessibilityManager$Stub; (declaration of 'android.view.accessibility.IAccessibilityManager$Stub' appears in /system/framework/framework.jar!classes3.dex)
        mDescriptorTransacts.put(ACCESSIBILITY_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of removeCredentialManagementApp under android.security.IKeyChainService: No field TRANSACTION_removeCredentialManagementApp in class Landroid/security/IKeyChainService$Stub; (declaration of 'android.security.IKeyChainService$Stub' appears in /system/framework/framework.jar!classes2.dex)
        mDescriptorTransacts.put(KEY_CHAIN_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of updateUiTranslationState under android.view.translation.ITranslationManager: No field TRANSACTION_updateUiTranslationState in class Landroid/view/translation/ITranslationManager$Stub; (declaration of 'android.view.translation.ITranslationManager$Stub' appears in /system/framework/framework.jar!classes3.dex)
        mDescriptorTransacts.put(TRANSLATION_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of setBindAppWidgetPermission under com.android.internal.appwidget.IAppWidgetService: No field TRANSACTION_setBindAppWidgetPermission in class Lcom/android/internal/appwidget/IAppWidgetService$Stub; (declaration of 'com.android.internal.appwidget.IAppWidgetService$Stub' appears in /system/framework/framework.jar!classes4.dex)
        mDescriptorTransacts.put(APPWIDGET_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of setBackupEnabled under android.app.backup.IBackupManager: No field TRANSACTION_setBackupEnabled in class Landroid/app/backup/IBackupManager$Stub; (declaration of 'android.app.backup.IBackupManager$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(BACKUP_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of reportEnabledTrustAgentsChanged under android.app.trust.ITrustManager: No field TRANSACTION_reportEnabledTrustAgentsChanged in class Landroid/app/trust/ITrustManager$Stub; (declaration of 'android.app.trust.ITrustManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of unregisterTrustListener under android.app.trust.ITrustManager: No field TRANSACTION_unregisterTrustListener in class Landroid/app/trust/ITrustManager$Stub; (declaration of 'android.app.trust.ITrustManager$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(TRUST_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of getFontConfig under com.android.internal.graphics.fonts.IFontManager: No field TRANSACTION_getFontConfig in class Lcom/android/internal/graphics/fonts/IFontManager$Stub; (declaration of 'com.android.internal.graphics.fonts.IFontManager$Stub' appears in /system/framework/framework.jar!classes4.dex)
        mDescriptorTransacts.put(FONT_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of getSpecificationInfo under android.uwb.IUwbAdapter: No field TRANSACTION_getSpecificationInfo in class Landroid/uwb/IUwbAdapter$Stub; (declaration of 'android.uwb.IUwbAdapter$Stub' appears in /system/framework/framework.jar!classes3.dex)
        mDescriptorTransacts.put(UWB_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of setSensorPrivacy under android.hardware.ISensorPrivacyManager: No field TRANSACTION_setSensorPrivacy in class Landroid/hardware/ISensorPrivacyManager$Stub; (declaration of 'android.hardware.ISensorPrivacyManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of isSensorPrivacyEnabled under android.hardware.ISensorPrivacyManager: No field TRANSACTION_isSensorPrivacyEnabled in class Landroid/hardware/ISensorPrivacyManager$Stub; (declaration of 'android.hardware.ISensorPrivacyManager$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(SENSOR_PRIVACY_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of getWifiApConfiguration under android.net.wifi.IWifiManager: No field TRANSACTION_getWifiApConfiguration in class Landroid/net/wifi/IWifiManager$Stub; (declaration of 'android.net.wifi.IWifiManager$Stub' appears in /apex/com.android.wifi/javalib/framework-wifi.jar)
        // Failed to obtain transactId of stopSoftAp under android.net.wifi.IWifiManager: No field TRANSACTION_stopSoftAp in class Landroid/net/wifi/IWifiManager$Stub; (declaration of 'android.net.wifi.IWifiManager$Stub' appears in /apex/com.android.wifi/javalib/framework-wifi.jar)
        // Failed to obtain transactId of setCoexUnsafeChannels under android.net.wifi.IWifiManager: No field TRANSACTION_setCoexUnsafeChannels in class Landroid/net/wifi/IWifiManager$Stub; (declaration of 'android.net.wifi.IWifiManager$Stub' appears in /apex/com.android.wifi/javalib/framework-wifi.jar)
        // Failed to obtain transactId of setOverrideCountryCode under android.net.wifi.IWifiManager: No field TRANSACTION_setOverrideCountryCode in class Landroid/net/wifi/IWifiManager$Stub; (declaration of 'android.net.wifi.IWifiManager$Stub' appears in /apex/com.android.wifi/javalib/framework-wifi.jar)
        // Failed to obtain transactId of unregisterCoexCallback under android.net.wifi.IWifiManager: No field TRANSACTION_unregisterCoexCallback in class Landroid/net/wifi/IWifiManager$Stub; (declaration of 'android.net.wifi.IWifiManager$Stub' appears in /apex/com.android.wifi/javalib/framework-wifi.jar)
        // Failed to obtain transactId of setWifiEnabled under android.net.wifi.IWifiManager: No field TRANSACTION_setWifiEnabled in class Landroid/net/wifi/IWifiManager$Stub; (declaration of 'android.net.wifi.IWifiManager$Stub' appears in /apex/com.android.wifi/javalib/framework-wifi.jar)
        // Failed to obtain transactId of restartWifiSubsystem under android.net.wifi.IWifiManager: No field TRANSACTION_restartWifiSubsystem in class Landroid/net/wifi/IWifiManager$Stub; (declaration of 'android.net.wifi.IWifiManager$Stub' appears in /apex/com.android.wifi/javalib/framework-wifi.jar)
        mDescriptorTransacts.put(WIFI_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of getControlFd under android.hardware.usb.IUsbManager: No field TRANSACTION_getControlFd in class Landroid/hardware/usb/IUsbManager$Stub; (declaration of 'android.hardware.usb.IUsbManager$Stub' appears in /system/framework/framework.jar!classes2.dex)
        mDescriptorTransacts.put(USB_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of setTemporaryComponent under android.speech.IRecognitionServiceManager: No field TRANSACTION_setTemporaryComponent in class Landroid/speech/IRecognitionServiceManager$Stub; (declaration of 'android.speech.IRecognitionServiceManager$Stub' appears in /system/framework/framework.jar!classes3.dex)
        mDescriptorTransacts.put(SPEECH_RECOGNITION_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of isAutoRevokeExempted under android.permission.IPermissionManager: No field TRANSACTION_isAutoRevokeExempted in class Landroid/permission/IPermissionManager$Stub; (declaration of 'android.permission.IPermissionManager$Stub' appears in /system/framework/framework.jar!classes2.dex)
        // Failed to obtain transactId of addOnPermissionsChangeListener under android.permission.IPermissionManager: No field TRANSACTION_addOnPermissionsChangeListener in class Landroid/permission/IPermissionManager$Stub; (declaration of 'android.permission.IPermissionManager$Stub' appears in /system/framework/framework.jar!classes2.dex)
        // Failed to obtain transactId of isAutoRevokeWhitelisted under android.permission.IPermissionManager: No field TRANSACTION_isAutoRevokeWhitelisted in class Landroid/permission/IPermissionManager$Stub; (declaration of 'android.permission.IPermissionManager$Stub' appears in /system/framework/framework.jar!classes2.dex)
        mDescriptorTransacts.put(PERMISSION_MANAGER_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of getModuleProperties under com.android.internal.app.ISoundTriggerSession: No field TRANSACTION_getModuleProperties in class Lcom/android/internal/app/ISoundTriggerSession$Stub; (declaration of 'com.android.internal.app.ISoundTriggerSession$Stub' appears in /system/framework/framework.jar!classes4.dex)
        mDescriptorTransacts.put(SOUND_TRIGGER_SESSION_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of installCaCert under android.app.admin.IDevicePolicyManager: No field TRANSACTION_installCaCert in class Landroid/app/admin/IDevicePolicyManager$Stub; (declaration of 'android.app.admin.IDevicePolicyManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of getNearbyNotificationStreamingPolicy under android.app.admin.IDevicePolicyManager: No field TRANSACTION_getNearbyNotificationStreamingPolicy in class Landroid/app/admin/IDevicePolicyManager$Stub; (declaration of 'android.app.admin.IDevicePolicyManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of markProfileOwnerOnOrganizationOwnedDevice under android.app.admin.IDevicePolicyManager: No field TRANSACTION_markProfileOwnerOnOrganizationOwnedDevice in class Landroid/app/admin/IDevicePolicyManager$Stub; (declaration of 'android.app.admin.IDevicePolicyManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of forceSecurityLogs under android.app.admin.IDevicePolicyManager: No field TRANSACTION_forceSecurityLogs in class Landroid/app/admin/IDevicePolicyManager$Stub; (declaration of 'android.app.admin.IDevicePolicyManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of getDoNotAskCredentialsOnBoot under android.app.admin.IDevicePolicyManager: No field TRANSACTION_getDoNotAskCredentialsOnBoot in class Landroid/app/admin/IDevicePolicyManager$Stub; (declaration of 'android.app.admin.IDevicePolicyManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of clearSystemUpdatePolicyFreezePeriodRecord under android.app.admin.IDevicePolicyManager: No field TRANSACTION_clearSystemUpdatePolicyFreezePeriodRecord in class Landroid/app/admin/IDevicePolicyManager$Stub; (declaration of 'android.app.admin.IDevicePolicyManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of setDeviceOwner under android.app.admin.IDevicePolicyManager: No field TRANSACTION_setDeviceOwner in class Landroid/app/admin/IDevicePolicyManager$Stub; (declaration of 'android.app.admin.IDevicePolicyManager$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(DEVICE_POLICY_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of getCurrentTunedInfos under android.media.tv.ITvInputManager: No field TRANSACTION_getCurrentTunedInfos in class Landroid/media/tv/ITvInputManager$Stub; (declaration of 'android.media.tv.ITvInputManager$Stub' appears in /system/framework/framework.jar!classes2.dex)
        mDescriptorTransacts.put(TV_INPUT_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of getNextEntry under com.android.internal.os.IDropBoxManagerService: No field TRANSACTION_getNextEntry in class Lcom/android/internal/os/IDropBoxManagerService$Stub; (declaration of 'com.android.internal.os.IDropBoxManagerService$Stub' appears in /system/framework/framework.jar!classes4.dex)
        mDescriptorTransacts.put(DROPBOX_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of pendingRequestForNetwork under android.net.IConnectivityManager: No field TRANSACTION_pendingRequestForNetwork in class Landroid/net/IConnectivityManager$Stub; (declaration of 'android.net.IConnectivityManager$Stub' appears in /apex/com.android.tethering/javalib/framework-connectivity.jar)
        // Failed to obtain transactId of tether under android.net.IConnectivityManager: No field TRANSACTION_tether in class Landroid/net/IConnectivityManager$Stub; (declaration of 'android.net.IConnectivityManager$Stub' appears in /apex/com.android.tethering/javalib/framework-connectivity.jar)
        // Failed to obtain transactId of getAlwaysOnVpnPackage under android.net.IConnectivityManager: No field TRANSACTION_getAlwaysOnVpnPackage in class Landroid/net/IConnectivityManager$Stub; (declaration of 'android.net.IConnectivityManager$Stub' appears in /apex/com.android.tethering/javalib/framework-connectivity.jar)
        // Failed to obtain transactId of getActiveNetworkForUid under android.net.IConnectivityManager: No field TRANSACTION_getActiveNetworkForUid in class Landroid/net/IConnectivityManager$Stub; (declaration of 'android.net.IConnectivityManager$Stub' appears in /apex/com.android.tethering/javalib/framework-connectivity.jar)
        // Failed to obtain transactId of startNattKeepalive under android.net.IConnectivityManager: No field TRANSACTION_startNattKeepalive in class Landroid/net/IConnectivityManager$Stub; (declaration of 'android.net.IConnectivityManager$Stub' appears in /apex/com.android.tethering/javalib/framework-connectivity.jar)
        mDescriptorTransacts.put(CONNECTIVITY_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of checkPermission under android.permission.IPermissionChecker: No field TRANSACTION_checkPermission in class Landroid/permission/IPermissionChecker$Stub; (declaration of 'android.permission.IPermissionChecker$Stub' appears in /system/framework/framework.jar!classes2.dex)
        mDescriptorTransacts.put(PERMISSION_CHECKER_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of getAlwaysOnVpnPackage under android.net.IVpnManager: No field TRANSACTION_getAlwaysOnVpnPackage in class Landroid/net/IVpnManager$Stub; (declaration of 'android.net.IVpnManager$Stub' appears in /system/framework/framework.jar!classes2.dex)
        mDescriptorTransacts.put(VPN_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of hasEnrolledBiometrics under android.hardware.biometrics.IBiometricService: No field TRANSACTION_hasEnrolledBiometrics in class Landroid/hardware/biometrics/IBiometricService$Stub; (declaration of 'android.hardware.biometrics.IBiometricService$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(BIOMETRIC_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of requestAvailability under android.telephony.ims.aidl.IImsRcsController: No field TRANSACTION_requestAvailability in class Landroid/telephony/ims/aidl/IImsRcsController$Stub; (declaration of 'android.telephony.ims.aidl.IImsRcsController$Stub' appears in /system/framework/framework.jar!classes3.dex)
        // Failed to obtain transactId of triggerNetworkRegistration under android.telephony.ims.aidl.IImsRcsController: No field TRANSACTION_triggerNetworkRegistration in class Landroid/telephony/ims/aidl/IImsRcsController$Stub; (declaration of 'android.telephony.ims.aidl.IImsRcsController$Stub' appears in /system/framework/framework.jar!classes3.dex)
        mDescriptorTransacts.put(TELEPHONY_IMS_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of registerNetworkStatsProvider under android.net.INetworkStatsService: No field TRANSACTION_registerNetworkStatsProvider in class Landroid/net/INetworkStatsService$Stub; (declaration of 'android.net.INetworkStatsService$Stub' appears in /system/framework/framework.jar!classes2.dex)
        // Failed to obtain transactId of forceUpdate under android.net.INetworkStatsService: No field TRANSACTION_forceUpdate in class Landroid/net/INetworkStatsService$Stub; (declaration of 'android.net.INetworkStatsService$Stub' appears in /system/framework/framework.jar!classes2.dex)
        mDescriptorTransacts.put(NETWORK_STATS_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of setOnMediaKeyListener under android.media.session.ISessionManager: No field TRANSACTION_setOnMediaKeyListener in class Landroid/media/session/ISessionManager$Stub; (declaration of 'android.media.session.ISessionManager$Stub' appears in /system/framework/framework.jar!classes2.dex)
        // Failed to obtain transactId of setOnVolumeKeyLongPressListener under android.media.session.ISessionManager: No field TRANSACTION_setOnVolumeKeyLongPressListener in class Landroid/media/session/ISessionManager$Stub; (declaration of 'android.media.session.ISessionManager$Stub' appears in /system/framework/framework.jar!classes2.dex)
        mDescriptorTransacts.put(MEDIA_SESSION_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of addOnRoleHoldersChangedListenerAsUser under android.app.role.IRoleManager: No field TRANSACTION_addOnRoleHoldersChangedListenerAsUser in class Landroid/app/role/IRoleManager$Stub; (declaration of 'android.app.role.IRoleManager$Stub' appears in /apex/com.android.permission/javalib/framework-permission-s.jar)
        // Failed to obtain transactId of setBypassingRoleQualification under android.app.role.IRoleManager: No field TRANSACTION_setBypassingRoleQualification in class Landroid/app/role/IRoleManager$Stub; (declaration of 'android.app.role.IRoleManager$Stub' appears in /apex/com.android.permission/javalib/framework-permission-s.jar)
        mDescriptorTransacts.put(ROLE_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of queryValidVerificationPackageNames under android.content.pm.verify.domain.IDomainVerificationManager: No field TRANSACTION_queryValidVerificationPackageNames in class Landroid/content/pm/verify/domain/IDomainVerificationManager$Stub; (declaration of 'android.content.pm.verify.domain.IDomainVerificationManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of setDomainVerificationLinkHandlingAllowed under android.content.pm.verify.domain.IDomainVerificationManager: No field TRANSACTION_setDomainVerificationLinkHandlingAllowed in class Landroid/content/pm/verify/domain/IDomainVerificationManager$Stub; (declaration of 'android.content.pm.verify.domain.IDomainVerificationManager$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(DOMAIN_VERIFICATION_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of setTouchCalibrationForInputDevice under android.hardware.input.IInputManager: No field TRANSACTION_setTouchCalibrationForInputDevice in class Landroid/hardware/input/IInputManager$Stub; (declaration of 'android.hardware.input.IInputManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of isInTabletMode under android.hardware.input.IInputManager: No field TRANSACTION_isInTabletMode in class Landroid/hardware/input/IInputManager$Stub; (declaration of 'android.hardware.input.IInputManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of monitorGestureInput under android.hardware.input.IInputManager: No field TRANSACTION_monitorGestureInput in class Landroid/hardware/input/IInputManager$Stub; (declaration of 'android.hardware.input.IInputManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of addKeyboardLayoutForInputDevice under android.hardware.input.IInputManager: No field TRANSACTION_addKeyboardLayoutForInputDevice in class Landroid/hardware/input/IInputManager$Stub; (declaration of 'android.hardware.input.IInputManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of enableInputDevice under android.hardware.input.IInputManager: No field TRANSACTION_enableInputDevice in class Landroid/hardware/input/IInputManager$Stub; (declaration of 'android.hardware.input.IInputManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of tryPointerSpeed under android.hardware.input.IInputManager: No field TRANSACTION_tryPointerSpeed in class Landroid/hardware/input/IInputManager$Stub; (declaration of 'android.hardware.input.IInputManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of removePortAssociation under android.hardware.input.IInputManager: No field TRANSACTION_removePortAssociation in class Landroid/hardware/input/IInputManager$Stub; (declaration of 'android.hardware.input.IInputManager$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(INPUT_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of reloadPersistedData under android.content.rollback.IRollbackManager: No field TRANSACTION_reloadPersistedData in class Landroid/content/rollback/IRollbackManager$Stub; (declaration of 'android.content.rollback.IRollbackManager$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(ROLLBACK_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of hideBiometricDialog under com.android.internal.statusbar.IStatusBarService: No field TRANSACTION_hideBiometricDialog in class Lcom/android/internal/statusbar/IStatusBarService$Stub; (declaration of 'com.android.internal.statusbar.IStatusBarService$Stub' appears in /system/framework/framework.jar!classes4.dex)
        // Failed to obtain transactId of onBiometricHelp under com.android.internal.statusbar.IStatusBarService: No field TRANSACTION_onBiometricHelp in class Lcom/android/internal/statusbar/IStatusBarService$Stub; (declaration of 'com.android.internal.statusbar.IStatusBarService$Stub' appears in /system/framework/framework.jar!classes4.dex)
        mDescriptorTransacts.put(STATUS_BAR_DESCRIPTOR, transactIds);
    }
}
