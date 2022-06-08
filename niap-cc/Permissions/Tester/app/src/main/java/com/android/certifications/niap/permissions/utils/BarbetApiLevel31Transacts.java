package com.android.certifications.niap.permissions.utils;

import java.util.HashMap;
import java.util.Map;

public class BarbetApiLevel31Transacts extends Transacts {
    public BarbetApiLevel31Transacts() {
        mDeviceApiLevel = 31;
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
        // Failed to obtain transactId of resetTimeout under android.hardware.fingerprint.IFingerprintService: No field TRANSACTION_resetTimeout in class Landroid/hardware/fingerprint/IFingerprintService$Stub; (declaration of 'android.hardware.fingerprint.IFingerprintService$Stub' appears in /system/framework/framework.jar)
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
        // Failed to obtain transactId of setPowerSaveMode under android.os.IPowerManager: No field TRANSACTION_setPowerSaveMode in class Landroid/os/IPowerManager$Stub; (declaration of 'android.os.IPowerManager$Stub' appears in /system/framework/framework.jar!classes2.dex)
        transactIds.put(setDynamicPowerSaveHint, 21);
        transactIds.put(setBatteryDischargePrediction, 25);
        mDescriptorTransacts.put(POWER_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(notifySystemEvent, 17);
        transactIds.put(injectCamera, 15);
        mDescriptorTransacts.put(CAMERA_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of isRecognitionActive under com.android.internal.app.ISoundTriggerService: No field TRANSACTION_isRecognitionActive in class Lcom/android/internal/app/ISoundTriggerService$Stub; (declaration of 'com.android.internal.app.ISoundTriggerService$Stub' appears in /system/framework/framework.jar!classes4.dex)
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
        // Failed to obtain transactId of getVtDataUsage under com.android.internal.telephony.ITelephony: No field TRANSACTION_getVtDataUsage in class Lcom/android/internal/telephony/ITelephony$Stub; (declaration of 'com.android.internal.telephony.ITelephony$Stub' appears in /system/framework/framework.jar!classes4.dex)
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
        // Failed to obtain transactId of setAlwaysOnEffect under android.os.IVibratorService: android.os.IVibratorService$Stub
        // Failed to obtain transactId of isVibrating under android.os.IVibratorService: android.os.IVibratorService$Stub
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
        // Failed to obtain transactId of addOnPermissionsChangeListener under android.content.pm.IPackageManager: No field TRANSACTION_addOnPermissionsChangeListener in class Landroid/content/pm/IPackageManager$Stub; (declaration of 'android.content.pm.IPackageManager$Stub' appears in /system/framework/framework.jar)
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
        // Failed to obtain transactId of dismissKeyguard under android.app.IActivityTaskManager: No field TRANSACTION_dismissKeyguard in class Landroid/app/IActivityTaskManager$Stub; (declaration of 'android.app.IActivityTaskManager$Stub' appears in /system/framework/framework.jar)
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
        // Failed to obtain transactId of getGrantedUriPermissions under android.app.IActivityManager: No field TRANSACTION_getGrantedUriPermissions in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        transactIds.put(setHasTopUi, 191);
        // Failed to obtain transactId of dismissKeyguard under android.app.IActivityManager: No field TRANSACTION_dismissKeyguard in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        transactIds.put(resumeAppSwitches, 81);
        transactIds.put(getContentProviderExternal, 115);
        transactIds.put(getIntentForIntentSender, 143);
        // Failed to obtain transactId of getTaskDescription under android.app.IActivityManager: No field TRANSACTION_getTaskDescription in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of getAssistContextExtras under android.app.IActivityManager: No field TRANSACTION_getAssistContextExtras in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        transactIds.put(unhandledBack, 10);
        // Failed to obtain transactId of inputDispatchingTimedOut under android.app.IActivityManager: No field TRANSACTION_inputDispatchingTimedOut in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of setFrontActivityScreenCompatMode under android.app.IActivityManager: No field TRANSACTION_setFrontActivityScreenCompatMode in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        transactIds.put(setAlwaysFinish, 36);
        transactIds.put(startActivityFromRecents, 160);
        // Failed to obtain transactId of releasePersistableUriPermission under android.app.IActivityManager: No field TRANSACTION_releasePersistableUriPermission in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        transactIds.put(requestBugReport, 133);
        // Failed to obtain transactId of getFrontActivityScreenCompatMode under android.app.IActivityManager: No field TRANSACTION_getFrontActivityScreenCompatMode in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
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
        // Failed to obtain transactId of requestUserActivityNotification under android.view.IWindowManager: No field TRANSACTION_requestUserActivityNotification in class Landroid/view/IWindowManager$Stub; (declaration of 'android.view.IWindowManager$Stub' appears in /system/framework/framework.jar!classes3.dex)
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
        // Failed to obtain transactId of isAutoRevokeWhitelisted under android.permission.IPermissionManager: No field TRANSACTION_isAutoRevokeWhitelisted in class Landroid/permission/IPermissionManager$Stub; (declaration of 'android.permission.IPermissionManager$Stub' appears in /system/framework/framework.jar!classes2.dex)
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
        // Failed to obtain transactId of tether under android.net.IConnectivityManager: No field TRANSACTION_tether in class Landroid/net/IConnectivityManager$Stub; (declaration of 'android.net.IConnectivityManager$Stub' appears in /apex/com.android.tethering/javalib/framework-connectivity.jar)
        // Failed to obtain transactId of getAlwaysOnVpnPackage under android.net.IConnectivityManager: No field TRANSACTION_getAlwaysOnVpnPackage in class Landroid/net/IConnectivityManager$Stub; (declaration of 'android.net.IConnectivityManager$Stub' appears in /apex/com.android.tethering/javalib/framework-connectivity.jar)
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
        // Failed to obtain transactId of hideBiometricDialog under com.android.internal.statusbar.IStatusBarService: No field TRANSACTION_hideBiometricDialog in class Lcom/android/internal/statusbar/IStatusBarService$Stub; (declaration of 'com.android.internal.statusbar.IStatusBarService$Stub' appears in /system/framework/framework.jar!classes4.dex)
        transactIds.put(onBiometricHelp, 47);
        mDescriptorTransacts.put(STATUS_BAR_DESCRIPTOR, transactIds);
    }
}
