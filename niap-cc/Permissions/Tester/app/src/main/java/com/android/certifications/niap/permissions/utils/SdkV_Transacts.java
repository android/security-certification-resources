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
package com.android.certifications.niap.permissions.utils;

import java.util.HashMap;
import java.util.Map;

public class SdkV_Transacts extends Transacts {
    public SdkV_Transacts() {
        mDeviceApiLevel = 35;
        Map<String, Integer> transactIds;

        transactIds = new HashMap<>();
        transactIds.put(getBackgroundInstalledPackages, 1);
        mDescriptorTransacts.put(BACKGROUND_INSTALL_CONTROL_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(connect, 1);
        mDescriptorTransacts.put(NSD_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of setTransactionState under android.ui.ISurfaceComposer: android.ui.ISurfaceComposer$Stub
        transactIds.put(bootFinished, 1);
        transactIds.put(showCpu, 1000);
        mDescriptorTransacts.put(SURFACE_FLINGER_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(resetLockout, 27);
        transactIds.put(cancelAuthenticationFromService, 11);
        // Failed to obtain transactId of resetTimeout under android.hardware.fingerprint.IFingerprintService: No field TRANSACTION_resetTimeout in class Landroid/hardware/fingerprint/IFingerprintService$Stub; (declaration of 'android.hardware.fingerprint.IFingerprintService$Stub' appears in /system/framework/framework.jar!classes2.dex)
        transactIds.put(cancelEnrollment, 13);
        mDescriptorTransacts.put(FINGERPRINT_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(takePersistableUriPermission, 1);
        transactIds.put(getGrantedUriPermissions, 4);
        mDescriptorTransacts.put(URI_GRANTS_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(verifyCredential, 10);
        transactIds.put(startRemoteLockscreenValidation, 46);
        mDescriptorTransacts.put(LOCK_SETTINGS_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of createGameSession under android.app.IGameManagerService: No field TRANSACTION_createGameSession in class Landroid/app/IGameManagerService$Stub; (declaration of 'android.app.IGameManagerService$Stub' appears in /system/framework/framework.jar)
        transactIds.put(getAvailableGameModes, 3);
        transactIds.put(setGameServiceProvider, 8);
        mDescriptorTransacts.put(GAME_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(reportChooserSelection, 13);
        mDescriptorTransacts.put(USAGE_STATS_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(beginRecognition, 1);
        mDescriptorTransacts.put(MUSIC_RECOGNITION_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(reboot, 48);
        transactIds.put(isAmbientDisplaySuppressedForTokenByApp, 66);
        transactIds.put(acquireWakeLock, 1);
        transactIds.put(isWakeLockLevelSupported, 10);
        transactIds.put(releaseLowPowerStandbyPorts, 46);
        // Failed to obtain transactId of setPowerSaveMode under android.os.IPowerManager: No field TRANSACTION_setPowerSaveMode in class Landroid/os/IPowerManager$Stub; (declaration of 'android.os.IPowerManager$Stub' appears in /system/framework/framework.jar!classes3.dex)
        transactIds.put(setDynamicPowerSaveHint, 26);
        transactIds.put(setBatteryDischargePrediction, 30);
        mDescriptorTransacts.put(POWER_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(isControllerAlwaysOnSupported, 25);
        mDescriptorTransacts.put(NFC_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(setOverrideLocaleConfig, 4);
        mDescriptorTransacts.put(LOCALE_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(unregisterObserver, 2);
        mDescriptorTransacts.put(RESOURCE_OBSERVER_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(generateCinematicEffect, 1);
        mDescriptorTransacts.put(WALLPAPER_EFFECTS_GENERATION_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(setWallpaper, 1);
        transactIds.put(setWallpaperComponent, 3);
        mDescriptorTransacts.put(WALLPAPER_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(destroySmartspaceSession, 6);
        mDescriptorTransacts.put(SMART_SPACE_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getPrimaryClipSource, 10);
        transactIds.put(areClipboardAccessNotificationsEnabledForUser, 11);
        // Failed to obtain transactId of showAccessNotificationLocked under android.content.IClipboard: No field TRANSACTION_showAccessNotificationLocked in class Landroid/content/IClipboard$Stub; (declaration of 'android.content.IClipboard$Stub' appears in /system/framework/framework.jar)
        mDescriptorTransacts.put(CLIPBOARD_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of setAlwaysOnEffect under android.os.IVibratorService: android.os.IVibratorService$Stub
        // Failed to obtain transactId of isVibrating under android.os.IVibratorService: android.os.IVibratorService$Stub
        mDescriptorTransacts.put(VIBRATOR_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(clearInteractAcrossProfilesAppOps, 10);
        mDescriptorTransacts.put(CROSS_PROFILE_APPS_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getActiveNotifications, 64);
        transactIds.put(getZenRules, 132);
        transactIds.put(isNotificationPolicyAccessGrantedForPackage, 126);
        transactIds.put(getEnabledNotificationListeners, 112);
        transactIds.put(setToastRateLimitingEnabled, 154);
        mDescriptorTransacts.put(NOTIFICATION_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getActiveProjectionInfo, 6);
        mDescriptorTransacts.put(MEDIA_PROJECTION_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(dismissKeyguard, 62);
        mDescriptorTransacts.put(ACTIVITY_CLIENT_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(setPersistentVrModeEnabled, 7);
        transactIds.put(getVrModeState, 5);
        transactIds.put(setStandbyEnabled, 11);
        mDescriptorTransacts.put(VR_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getEnhancedConfirmationTrustedPackages, 8);
        mDescriptorTransacts.put(SYSTEM_CONFIG_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(createAssociation, 14);
        transactIds.put(removeOnTransportsChangedListener, 18);
        // Failed to obtain transactId of dispatchMessage under android.companion.ICompanionDeviceManager: No field TRANSACTION_dispatchMessage in class Landroid/companion/ICompanionDeviceManager$Stub; (declaration of 'android.companion.ICompanionDeviceManager$Stub' appears in /system/framework/framework.jar)
        transactIds.put(addOnTransportsChangedListener, 17);
        transactIds.put(sendMessage, 19);
        transactIds.put(addOnMessageReceivedListener, 20);
        transactIds.put(startObservingDevicePresence, 11);
        transactIds.put(getAllAssociationsForUser, 3);
        mDescriptorTransacts.put(COMPANION_DEVICE_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getCacheSizeBytes, 77);
        // Failed to obtain transactId of getEncryptionState under android.os.storage.IStorageManager: No field TRANSACTION_getEncryptionState in class Landroid/os/storage/IStorageManager$Stub; (declaration of 'android.os.storage.IStorageManager$Stub' appears in /system/framework/framework.jar!classes3.dex)
        transactIds.put(benchmark, 60);
        // Failed to obtain transactId of isConvertibleToFBE under android.os.storage.IStorageManager: No field TRANSACTION_isConvertibleToFBE in class Landroid/os/storage/IStorageManager$Stub; (declaration of 'android.os.storage.IStorageManager$Stub' appears in /system/framework/framework.jar!classes3.dex)
        mDescriptorTransacts.put(MOUNT_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(isInputMethodPickerShownForTest, 16);
        mDescriptorTransacts.put(INPUTMETHOD_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(deactivateFactoryResetProtection, 12);
        mDescriptorTransacts.put(PDB_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(setDataSaverModeEnabled, 30);
        mDescriptorTransacts.put(NETWORK_MANAGEMENT_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(isDeviceLocked, 3);
        mDescriptorTransacts.put(DEVICELOCK_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(updateDataDownloadState, 32);
        transactIds.put(deleteAllStagedRemoteData, 31);
        transactIds.put(startMigration, 24);
        transactIds.put(getHealthConnectDataState, 33);
        mDescriptorTransacts.put(HEALTH_CONNECT_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of search under android.app.cloudsearch.ICloudSearchManager: android.app.cloudsearch.ICloudSearchManager$Stub
        mDescriptorTransacts.put(CLOUDSEARCH_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(createVirtualDevice, 1);
        mDescriptorTransacts.put(VIRTUAL_DEVICE_MANAGER_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(setupFsverity, 4);
        transactIds.put(createAuthToken, 3);
        mDescriptorTransacts.put(FILE_INTEGRITY_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(removeRestrictedMetricsChangedOperation, 16);
        transactIds.put(setRestrictedMetricsChangedOperation, 15);
        mDescriptorTransacts.put(STATS_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(awaken, 2);
        transactIds.put(isDreaming, 7);
        mDescriptorTransacts.put(DREAMS_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(setRefreshRateSwitchingType, 51);
        transactIds.put(setHdrConversionMode, 45);
        transactIds.put(setTemporaryAutoBrightnessAdjustment, 38);
        transactIds.put(startWifiDisplayScan, 6);
        transactIds.put(setUserPreferredDisplayMode, 42);
        transactIds.put(shouldAlwaysRespectAppRequestedMode, 50);
        transactIds.put(requestDisplayModes, 59);
        transactIds.put(requestColorMode, 20);
        transactIds.put(enableConnectedDisplay, 56);
        transactIds.put(getUserPreferredDisplayMode, 43);
        mDescriptorTransacts.put(DISPLAY_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(requestAppKeyboardShortcuts, 80);
        transactIds.put(createInputConsumer, 84);
        transactIds.put(registerTaskFpsCallback, 133);
        // Failed to obtain transactId of requestUserActivityNotification under android.view.IWindowManager: No field TRANSACTION_requestUserActivityNotification in class Landroid/view/IWindowManager$Stub; (declaration of 'android.view.IWindowManager$Stub' appears in /system/framework/framework.jar!classes4.dex)
        transactIds.put(clearWindowContentFrameStats, 76);
        transactIds.put(registerScreenRecordingCallback, 149);
        transactIds.put(addKeyguardLockedStateListener, 33);
        transactIds.put(setRecentsVisibility, 70);
        transactIds.put(stopFreezingScreen, 26);
        transactIds.put(setInTouchMode, 42);
        transactIds.put(dismissKeyguard, 32);
        transactIds.put(screenshotWallpaper, 62);
        transactIds.put(setAnimationScale, 39);
        transactIds.put(removeWindowToken, 18);
        transactIds.put(thawRotation, 55);
        // Failed to obtain transactId of getInTouchMode under android.view.IWindowManager: No field TRANSACTION_getInTouchMode in class Landroid/view/IWindowManager$Stub; (declaration of 'android.view.IWindowManager$Stub' appears in /system/framework/framework.jar!classes4.dex)
        transactIds.put(registerShortcutKey, 83);
        transactIds.put(overridePendingAppTransitionRemote, 23);
        mDescriptorTransacts.put(WINDOW_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getSafetyCenterConfig, 7);
        transactIds.put(getSafetySourceData, 3);
        transactIds.put(isSafetyCenterEnabled, 1);
        mDescriptorTransacts.put(SAFETY_CENTER_MANAGER_MANAGER_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(removeCredentialManagementApp, 25);
        mDescriptorTransacts.put(KEY_CHAIN_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(updateUiTranslationState, 5);
        mDescriptorTransacts.put(TRANSLATION_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(setBackupEnabled, 14);
        mDescriptorTransacts.put(BACKUP_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(resetFlag, 6);
        transactIds.put(overrideFlag, 5);
        mDescriptorTransacts.put(FEATURE_FLAGS_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getFontConfig, 1);
        mDescriptorTransacts.put(FONT_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(setSensorPrivacy, 9);
        transactIds.put(isSensorPrivacyEnabled, 6);
        mDescriptorTransacts.put(SENSOR_PRIVACY_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(setTemporaryComponent, 2);
        mDescriptorTransacts.put(SPEECH_RECOGNITION_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getModuleProperties, 13);
        mDescriptorTransacts.put(SOUND_TRIGGER_SESSION_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getNextEntry, 4);
        mDescriptorTransacts.put(DROPBOX_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(pendingRequestForNetwork, 43);
        // Failed to obtain transactId of tether under android.net.IConnectivityManager: No field TRANSACTION_tether in class Landroid/net/IConnectivityManager$Stub; (declaration of 'android.net.IConnectivityManager$Stub' appears in /apex/com.android.tethering/javalib/framework-connectivity.jar)
        // Failed to obtain transactId of getAlwaysOnVpnPackage under android.net.IConnectivityManager: No field TRANSACTION_getAlwaysOnVpnPackage in class Landroid/net/IConnectivityManager$Stub; (declaration of 'android.net.IConnectivityManager$Stub' appears in /apex/com.android.tethering/javalib/framework-connectivity.jar)
        transactIds.put(getActiveNetworkForUid, 2);
        transactIds.put(startNattKeepalive, 58);
        mDescriptorTransacts.put(CONNECTIVITY_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(checkPermission, 1);
        mDescriptorTransacts.put(PERMISSION_CHECKER_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(hasEnrolledBiometrics, 7);
        mDescriptorTransacts.put(BIOMETRIC_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(registerNetworkStatsProvider, 14);
        transactIds.put(forceUpdate, 8);
        mDescriptorTransacts.put(NETWORK_STATS_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(setTouchCalibrationForInputDevice, 15);
        transactIds.put(registerStickyModifierStateListener, 64);
        transactIds.put(setKeyboardLayoutForInputDevice, 19);
        transactIds.put(isInTabletMode, 25);
        transactIds.put(monitorGestureInput, 38);
        // Failed to obtain transactId of addKeyboardLayoutForInputDevice under android.hardware.input.IInputManager: No field TRANSACTION_addKeyboardLayoutForInputDevice in class Landroid/hardware/input/IInputManager$Stub; (declaration of 'android.hardware.input.IInputManager$Stub' appears in /system/framework/framework.jar!classes2.dex)
        transactIds.put(enableInputDevice, 4);
        transactIds.put(registerKeyboardBacklightListener, 61);
        transactIds.put(getModifierKeyRemapping, 23);
        transactIds.put(unregisterStickyModifierStateListener, 65);
        transactIds.put(tryPointerSpeed, 10);
        transactIds.put(removePortAssociation, 40);
        mDescriptorTransacts.put(INPUT_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(updateState, 35);
        transactIds.put(getActiveServiceComponentName, 19);
        transactIds.put(updateKeyphraseSoundModel, 14);
        transactIds.put(isSessionRunning, 23);
        mDescriptorTransacts.put(VOICE_INTERACTION_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(retainSubscriptionsForFactoryReset, 14);
        transactIds.put(getSupportedCountries, 16);
        mDescriptorTransacts.put(EUICC_CONTROLLER_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(reportChangeByUid, 3);
        transactIds.put(removeOverridesOnReleaseBuilds, 14);
        transactIds.put(clearOverridesForTest, 18);
        mDescriptorTransacts.put(PLATFORM_COMPAT_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(notifySystemEvent, 20);
        transactIds.put(injectCamera, 16);
        mDescriptorTransacts.put(CAMERA_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getCredentialProviderServices, 10);
        mDescriptorTransacts.put(CREDENTIAL_DESCRIPTOR, transactIds);

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
        transactIds.put(setRingtonePlayer, 78);
        transactIds.put(getRingtonePlayer, 79);
        transactIds.put(isAudioServerRunning, 138);
        transactIds.put(forceRemoteSubmixFullVolume, 18);
        transactIds.put(setVolumeGroupVolumeIndex, 25);
        transactIds.put(getDeviceVolumeBehavior, 163);
        mDescriptorTransacts.put(AUDIO_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(noteStartAudio, 5);
        transactIds.put(getAwakeTimeBattery, 77);
        mDescriptorTransacts.put(BATTERY_STATS_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(onApplicationActive, 16);
        mDescriptorTransacts.put(SHORTCUT_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of getVtDataUsage under com.android.internal.telephony.ITelephony: No field TRANSACTION_getVtDataUsage in class Lcom/android/internal/telephony/ITelephony$Stub; (declaration of 'com.android.internal.telephony.ITelephony$Stub' appears in /system/framework/framework.jar!classes5.dex)
        transactIds.put(enableLocationUpdates, 26);
        transactIds.put(requestIsSatelliteEnabled, 360);
        transactIds.put(getLastKnownCellIdentity, 341);
        transactIds.put(requestSatelliteEnabled, 359);
        mDescriptorTransacts.put(TELEPHONY_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(requestEmbeddedSubscriptionInfoListRefresh, 10);
        transactIds.put(setSubscriptionUserHandle, 54);
        mDescriptorTransacts.put(SUBSCRIPTION_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(isConversation, 5);
        mDescriptorTransacts.put(PEOPLE_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getAppMetadataFd, 37);
        transactIds.put(movePackage, 117);
        transactIds.put(installExistingPackageAsUser, 121);
        transactIds.put(setKeepUninstalledPackages, 210);
        transactIds.put(setPackagesSuspendedAsUser, 69);
        transactIds.put(isPackageDeviceAdminOnAnyUser, 156);
        transactIds.put(makeUidVisible, 205);
        transactIds.put(getRuntimePermissionsVersion, 184);
        // Failed to obtain transactId of addOnPermissionsChangeListener under android.content.pm.IPackageManager: No field TRANSACTION_addOnPermissionsChangeListener in class Landroid/content/pm/IPackageManager$Stub; (declaration of 'android.content.pm.IPackageManager$Stub' appears in /system/framework/framework.jar)
        transactIds.put(getHarmfulAppWarning, 168);
        transactIds.put(isPackageStateProtected, 180);
        transactIds.put(getMoveStatus, 114);
        transactIds.put(resetApplicationPreferences, 55);
        mDescriptorTransacts.put(PACKAGE_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(startContextualSearch, 1);
        mDescriptorTransacts.put(CONTEXTUAL_SEARCH_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getSubscriptionPlans, 20);
        transactIds.put(isUidNetworkingBlocked, 25);
        transactIds.put(getUidPolicy, 4);
        transactIds.put(registerListener, 6);
        mDescriptorTransacts.put(NET_POLICY_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(queryServiceStatus, 4);
        mDescriptorTransacts.put(AMBIENT_CONTEXT_MANAGER_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(set, 1);
        transactIds.put(setTimeZone, 3);
        transactIds.put(setTime, 2);
        mDescriptorTransacts.put(ALARM_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getFrontActivityScreenCompatMode, 18);
        // Failed to obtain transactId of getTaskDescription under android.app.IActivityTaskManager: No field TRANSACTION_getTaskDescription in class Landroid/app/IActivityTaskManager$Stub; (declaration of 'android.app.IActivityTaskManager$Stub' appears in /system/framework/framework.jar)
        transactIds.put(getAssistContextExtras, 57);
        transactIds.put(setFrontActivityScreenCompatMode, 19);
        transactIds.put(getWindowOrganizerController, 64);
        transactIds.put(startActivityFromGameSession, 11);
        transactIds.put(startActivityAsCaller, 14);
        // Failed to obtain transactId of dismissKeyguard under android.app.IActivityTaskManager: No field TRANSACTION_dismissKeyguard in class Landroid/app/IActivityTaskManager$Stub; (declaration of 'android.app.IActivityTaskManager$Stub' appears in /system/framework/framework.jar)
        transactIds.put(getActivityClientController, 17);
        mDescriptorTransacts.put(ACTIVITY_TASK_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(overridePid, 6);
        mDescriptorTransacts.put(RESOURCE_MANAGER_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(suggestExternalTime, 8);
        transactIds.put(getCapabilitiesAndConfig, 1);
        mDescriptorTransacts.put(TIME_DETECTOR_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(removeRequestRebootReadinessStatusListener, 5);
        mDescriptorTransacts.put(REBOOT_READINESS_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of cancelRequest under android.hardware.devicestate.IDeviceStateManager: No field TRANSACTION_cancelRequest in class Landroid/hardware/devicestate/IDeviceStateManager$Stub; (declaration of 'android.hardware.devicestate.IDeviceStateManager$Stub' appears in /system/framework/framework.jar!classes2.dex)
        transactIds.put(cancelStateRequest, 4);
        mDescriptorTransacts.put(DEVICE_STATE_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(resetAppErrors, 235);
        transactIds.put(bindBackupAgent, 93);
        transactIds.put(performIdleMaintenance, 170);
        transactIds.put(setDumpHeapDebugLimit, 188);
        transactIds.put(getBindingUidProcessState, 259);
        transactIds.put(updateLockTaskPackages, 190);
        transactIds.put(stopAppForUser, 81);
        // Failed to obtain transactId of getGrantedUriPermissions under android.app.IActivityManager: No field TRANSACTION_getGrantedUriPermissions in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        transactIds.put(setHasTopUi, 210);
        // Failed to obtain transactId of dismissKeyguard under android.app.IActivityManager: No field TRANSACTION_dismissKeyguard in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        transactIds.put(resumeAppSwitches, 92);
        transactIds.put(getContentProviderExternal, 129);
        transactIds.put(getIntentForIntentSender, 160);
        // Failed to obtain transactId of getTaskDescription under android.app.IActivityManager: No field TRANSACTION_getTaskDescription in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of getAssistContextExtras under android.app.IActivityManager: No field TRANSACTION_getAssistContextExtras in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        transactIds.put(broadcastIntentWithFeature, 22);
        transactIds.put(unhandledBack, 16);
        // Failed to obtain transactId of inputDispatchingTimedOut under android.app.IActivityManager: No field TRANSACTION_inputDispatchingTimedOut in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of setFrontActivityScreenCompatMode under android.app.IActivityManager: No field TRANSACTION_setFrontActivityScreenCompatMode in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        transactIds.put(getMimeTypeFilterAsync, 112);
        transactIds.put(setAlwaysFinish, 43);
        transactIds.put(startActivityFromRecents, 177);
        transactIds.put(killAllBackgroundProcesses, 128);
        // Failed to obtain transactId of releasePersistableUriPermission under android.app.IActivityManager: No field TRANSACTION_releasePersistableUriPermission in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        transactIds.put(logFgsApiBegin, 10);
        transactIds.put(requestBugReport, 149);
        // Failed to obtain transactId of getFrontActivityScreenCompatMode under android.app.IActivityManager: No field TRANSACTION_getFrontActivityScreenCompatMode in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        transactIds.put(setProcessLimit, 51);
        transactIds.put(signalPersistentProcesses, 60);
        transactIds.put(updateConfiguration, 48);
        transactIds.put(appNotRespondingViaProvider, 171);
        transactIds.put(shutdown, 90);
        mDescriptorTransacts.put(ACTIVITY_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(generateChallenge, 19);
        mDescriptorTransacts.put(FACE_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(requestProjection, 21);
        transactIds.put(getActiveProjectionTypes, 26);
        mDescriptorTransacts.put(UI_MODE_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(clearHistory, 27);
        transactIds.put(setUserRestriction, 35);
        transactIds.put(getHistoricalOps, 20);
        transactIds.put(getUidOps, 29);
        transactIds.put(noteOperation, 2);
        transactIds.put(permissionToOpCode, 7);
        mDescriptorTransacts.put(APP_OPS_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getVersion, 2);
        transactIds.put(getFeature, 3);
        mDescriptorTransacts.put(ON_DEVICE_INTELLINGENCE_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of temporaryEnableAccessibilityStateUntilKeyguardRemoved under android.view.accessibility.IAccessibilityManager: No field TRANSACTION_temporaryEnableAccessibilityStateUntilKeyguardRemoved in class Landroid/view/accessibility/IAccessibilityManager$Stub; (declaration of 'android.view.accessibility.IAccessibilityManager$Stub' appears in /system/framework/framework.jar!classes4.dex)
        transactIds.put(registerUiTestAutomationService, 10);
        transactIds.put(setPictureInPictureActionReplacingConnection, 9);
        transactIds.put(getWindowToken, 12);
        mDescriptorTransacts.put(ACCESSIBILITY_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(setBindAppWidgetPermission, 21);
        mDescriptorTransacts.put(APPWIDGET_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(reportEnabledTrustAgentsChanged, 5);
        transactIds.put(unregisterTrustListener, 7);
        mDescriptorTransacts.put(TRUST_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getSpecificationInfo, 8);
        transactIds.put(openRanging, 9);
        mDescriptorTransacts.put(UWB_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(verifyToken, 2);
        transactIds.put(verifyAttestation, 1);
        mDescriptorTransacts.put(ATTESTATION_VERIFICATION_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getWifiApConfiguration, 85);
        transactIds.put(stopSoftAp, 76);
        transactIds.put(setCoexUnsafeChannels, 70);
        transactIds.put(setOverrideCountryCode, 50);
        transactIds.put(unregisterCoexCallback, 72);
        transactIds.put(setWifiEnabled, 43);
        transactIds.put(restartWifiSubsystem, 159);
        mDescriptorTransacts.put(WIFI_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getControlFd, 36);
        mDescriptorTransacts.put(USB_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(isAutoRevokeExempted, 26);
        transactIds.put(addOnPermissionsChangeListener, 10);
        // Failed to obtain transactId of isAutoRevokeWhitelisted under android.permission.IPermissionManager: No field TRANSACTION_isAutoRevokeWhitelisted in class Landroid/permission/IPermissionManager$Stub; (declaration of 'android.permission.IPermissionManager$Stub' appears in /system/framework/framework.jar!classes3.dex)
        mDescriptorTransacts.put(PERMISSION_MANAGER_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(registerProxyRouter, 29);
        transactIds.put(registerManager, 28);
        mDescriptorTransacts.put(MEDIA_ROUTER_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(setUserControlDisabledPackages, 340);
        transactIds.put(setKeyguardDisabledFeatures, 61);
        transactIds.put(getDoNotAskCredentialsOnBoot, 234);
        transactIds.put(setShortSupportMessage, 248);
        transactIds.put(getNearbyNotificationStreamingPolicy, 58);
        transactIds.put(setPermittedInputMethods, 144);
        transactIds.put(setStrings, 376);
        transactIds.put(setApplicationRestrictions, 127);
        transactIds.put(getWifiSsidPolicy, 368);
        transactIds.put(setPermissionGrantState, 239);
        transactIds.put(setUsbDataSignalingEnabled, 362);
        transactIds.put(forceSecurityLogs, 274);
        transactIds.put(setResetPasswordToken, 295);
        transactIds.put(setFactoryResetProtectionPolicy, 42);
        transactIds.put(addCrossProfileWidgetProvider, 215);
        transactIds.put(setConfiguredNetworksLockdownState, 185);
        transactIds.put(setCommonCriteriaModeEnabled, 342);
        transactIds.put(getCrossProfileWidgetProviders, 217);
        transactIds.put(setAccountManagementDisabled, 170);
        transactIds.put(setStatusBarDisabled, 232);
        transactIds.put(setSystemUpdatePolicy, 228);
        transactIds.put(setDeviceOwner, 78);
        // Failed to obtain transactId of markProfileOwnerOnOrganizationOwnedDevice under android.app.admin.IDevicePolicyManager: No field TRANSACTION_markProfileOwnerOnOrganizationOwnedDevice in class Landroid/app/admin/IDevicePolicyManager$Stub; (declaration of 'android.app.admin.IDevicePolicyManager$Stub' appears in /system/framework/framework.jar)
        transactIds.put(isPackageSuspended, 99);
        transactIds.put(setProfileOwnerOnOrganizationOwnedDevice, 323);
        transactIds.put(setTrustAgentConfiguration, 213);
        transactIds.put(getString, 378);
        transactIds.put(hasLockdownAdminConfiguredNetworks, 186);
        transactIds.put(setLockTaskPackages, 177);
        transactIds.put(setOrganizationName, 259);
        transactIds.put(isDevicePotentiallyStolen, 369);
        transactIds.put(getPermittedInputMethodsAsUser, 146);
        transactIds.put(setApplicationExemptions, 382);
        transactIds.put(setDefaultSmsApplication, 125);
        transactIds.put(setPasswordExpirationTimeout, 20);
        transactIds.put(installCaCert, 101);
        transactIds.put(setUserRestriction, 134);
        transactIds.put(setSecurityLoggingEnabled, 269);
        transactIds.put(setMaximumTimeToLock, 36);
        transactIds.put(setScreenCaptureDisabled, 55);
        transactIds.put(clearSystemUpdatePolicyFreezePeriodRecord, 230);
        transactIds.put(setCameraDisabled, 53);
        transactIds.put(installUpdateFromFile, 324);
        transactIds.put(setMtePolicy, 384);
        transactIds.put(setMaximumFailedPasswordsForWipe, 33);
        transactIds.put(installKeyPair, 106);
        mDescriptorTransacts.put(DEVICE_POLICY_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getAvailableExtensionInterfaceNames, 5);
        transactIds.put(getCurrentTunedInfos, 41);
        mDescriptorTransacts.put(TV_INPUT_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getSystemGrammaticalGender, 3);
        mDescriptorTransacts.put(GRAMMATICAL_INFLECTION_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getAlwaysOnVpnPackage, 16);
        mDescriptorTransacts.put(VPN_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(provideDataStream, 2);
        mDescriptorTransacts.put(WEARABLES_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(requestAvailability, 10);
        transactIds.put(triggerNetworkRegistration, 19);
        mDescriptorTransacts.put(TELEPHONY_IMS_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(setOnMediaKeyListener, 22);
        transactIds.put(setOnVolumeKeyLongPressListener, 21);
        mDescriptorTransacts.put(MEDIA_SESSION_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getDefaultApplicationAsUser, 7);
        transactIds.put(addOnRoleHoldersChangedListenerAsUser, 9);
        transactIds.put(setBypassingRoleQualification, 12);
        mDescriptorTransacts.put(ROLE_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(queryValidVerificationPackageNames, 1);
        transactIds.put(setDomainVerificationLinkHandlingAllowed, 6);
        mDescriptorTransacts.put(DOMAIN_VERIFICATION_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(reloadPersistedData, 5);
        mDescriptorTransacts.put(ROLLBACK_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of hideBiometricDialog under com.android.internal.statusbar.IStatusBarService: No field TRANSACTION_hideBiometricDialog in class Lcom/android/internal/statusbar/IStatusBarService$Stub; (declaration of 'com.android.internal.statusbar.IStatusBarService$Stub' appears in /system/framework/framework.jar!classes5.dex)
        transactIds.put(onBiometricHelp, 49);
        mDescriptorTransacts.put(STATUS_BAR_DESCRIPTOR, transactIds);
    }
}
