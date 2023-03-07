/*
 * Copyright 2022 The Android Open Source Project
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

public class BarbetApiLevel33Transacts extends Transacts {
    public BarbetApiLevel33Transacts() {
        mDeviceApiLevel = 33;
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
        transactIds.put(resetLockout, 27);
        transactIds.put(cancelAuthenticationFromService, 11);
        // Failed to obtain transactId of resetTimeout under android.hardware.fingerprint.IFingerprintService: No field TRANSACTION_resetTimeout in class Landroid/hardware/fingerprint/IFingerprintService$Stub; (declaration of 'android.hardware.fingerprint.IFingerprintService$Stub' appears in /system/framework/framework.jar)
        transactIds.put(cancelEnrollment, 13);
        mDescriptorTransacts.put(FINGERPRINT_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(retainSubscriptionsForFactoryReset, 14);
        mDescriptorTransacts.put(EUICC_CONTROLLER_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(takePersistableUriPermission, 1);
        transactIds.put(getGrantedUriPermissions, 4);
        mDescriptorTransacts.put(URI_GRANTS_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(verifyCredential, 10);
        mDescriptorTransacts.put(LOCK_SETTINGS_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of createGameSession under android.app.IGameManagerService: No field TRANSACTION_createGameSession in class Landroid/app/IGameManagerService$Stub; (declaration of 'android.app.IGameManagerService$Stub' appears in /system/framework/framework.jar)
        transactIds.put(getAvailableGameModes, 3);
        transactIds.put(setGameServiceProvider, 8);
        mDescriptorTransacts.put(GAME_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(reportChangeByUid, 3);
        transactIds.put(removeOverridesOnReleaseBuilds, 14);
        transactIds.put(clearOverridesForTest, 18);
        mDescriptorTransacts.put(PLATFORM_COMPAT_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(beginRecognition, 1);
        mDescriptorTransacts.put(MUSIC_RECOGNITION_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(reboot, 36);
        transactIds.put(isAmbientDisplaySuppressedForTokenByApp, 54);
        // Failed to obtain transactId of setPowerSaveMode under android.os.IPowerManager: No field TRANSACTION_setPowerSaveMode in class Landroid/os/IPowerManager$Stub; (declaration of 'android.os.IPowerManager$Stub' appears in /system/framework/framework.jar!classes2.dex)
        transactIds.put(setDynamicPowerSaveHint, 22);
        transactIds.put(setBatteryDischargePrediction, 26);
        mDescriptorTransacts.put(POWER_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(notifySystemEvent, 19);
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
        transactIds.put(setRingtonePlayer, 69);
        transactIds.put(getRingtonePlayer, 70);
        transactIds.put(isAudioServerRunning, 103);
        transactIds.put(forceRemoteSubmixFullVolume, 15);
        transactIds.put(getDeviceVolumeBehavior, 121);
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
        transactIds.put(generateCinematicEffect, 1);
        mDescriptorTransacts.put(WALLPAPER_EFFECTS_GENERATION_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(setWallpaper, 1);
        transactIds.put(setWallpaperComponent, 3);
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
        // Failed to obtain transactId of showAccessNotificationLocked under android.content.IClipboard: No field TRANSACTION_showAccessNotificationLocked in class Landroid/content/IClipboard$Stub; (declaration of 'android.content.IClipboard$Stub' appears in /system/framework/framework.jar)
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
        transactIds.put(getActiveNotifications, 64);
        transactIds.put(getZenRules, 128);
        transactIds.put(isNotificationPolicyAccessGrantedForPackage, 124);
        transactIds.put(getEnabledNotificationListeners, 110);
        transactIds.put(setToastRateLimitingEnabled, 147);
        mDescriptorTransacts.put(NOTIFICATION_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(movePackage, 111);
        transactIds.put(installExistingPackageAsUser, 115);
        transactIds.put(makeUidVisible, 199);
        transactIds.put(setKeepUninstalledPackages, 204);
        transactIds.put(getRuntimePermissionsVersion, 180);
        // Failed to obtain transactId of addOnPermissionsChangeListener under android.content.pm.IPackageManager: No field TRANSACTION_addOnPermissionsChangeListener in class Landroid/content/pm/IPackageManager$Stub; (declaration of 'android.content.pm.IPackageManager$Stub' appears in /system/framework/framework.jar)
        transactIds.put(getHarmfulAppWarning, 163);
        transactIds.put(isPackageDeviceAdminOnAnyUser, 151);
        transactIds.put(isPackageStateProtected, 176);
        transactIds.put(getMoveStatus, 108);
        transactIds.put(resetApplicationPreferences, 53);
        mDescriptorTransacts.put(PACKAGE_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getSubscriptionPlans, 20);
        transactIds.put(isUidNetworkingBlocked, 25);
        transactIds.put(getUidPolicy, 4);
        transactIds.put(registerListener, 6);
        mDescriptorTransacts.put(NET_POLICY_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(queryServiceStatus, 3);
        mDescriptorTransacts.put(AMBIENT_CONTEXT_MANAGER_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(set, 1);
        transactIds.put(setTimeZone, 3);
        transactIds.put(setTime, 2);
        mDescriptorTransacts.put(ALARM_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getActiveProjectionInfo, 4);
        mDescriptorTransacts.put(MEDIA_PROJECTION_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(dismissKeyguard, 52);
        mDescriptorTransacts.put(ACTIVITY_CLIENT_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getFrontActivityScreenCompatMode, 19);
        transactIds.put(getTaskDescription, 28);
        transactIds.put(getAssistContextExtras, 58);
        transactIds.put(setFrontActivityScreenCompatMode, 20);
        transactIds.put(getWindowOrganizerController, 65);
        transactIds.put(startActivityFromGameSession, 12);
        transactIds.put(startActivityAsCaller, 15);
        // Failed to obtain transactId of dismissKeyguard under android.app.IActivityTaskManager: No field TRANSACTION_dismissKeyguard in class Landroid/app/IActivityTaskManager$Stub; (declaration of 'android.app.IActivityTaskManager$Stub' appears in /system/framework/framework.jar)
        transactIds.put(getActivityClientController, 18);
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
        transactIds.put(dispatchMessage, 13);
        mDescriptorTransacts.put(COMPANION_DEVICE_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getCacheSizeBytes, 77);
        // Failed to obtain transactId of getEncryptionState under android.os.storage.IStorageManager: No field TRANSACTION_getEncryptionState in class Landroid/os/storage/IStorageManager$Stub; (declaration of 'android.os.storage.IStorageManager$Stub' appears in /system/framework/framework.jar!classes2.dex)
        transactIds.put(benchmark, 60);
        // Failed to obtain transactId of isConvertibleToFBE under android.os.storage.IStorageManager: No field TRANSACTION_isConvertibleToFBE in class Landroid/os/storage/IStorageManager$Stub; (declaration of 'android.os.storage.IStorageManager$Stub' appears in /system/framework/framework.jar!classes2.dex)
        mDescriptorTransacts.put(MOUNT_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(suggestExternalTime, 3);
        transactIds.put(getCapabilitiesAndConfig, 1);
        mDescriptorTransacts.put(TIME_DETECTOR_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(removeRequestRebootReadinessStatusListener, 5);
        mDescriptorTransacts.put(REBOOT_READINESS_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of cancelRequest under android.hardware.devicestate.IDeviceStateManager: No field TRANSACTION_cancelRequest in class Landroid/hardware/devicestate/IDeviceStateManager$Stub; (declaration of 'android.hardware.devicestate.IDeviceStateManager$Stub' appears in /system/framework/framework.jar)
        transactIds.put(cancelStateRequest, 4);
        mDescriptorTransacts.put(DEVICE_STATE_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(setDataSaverModeEnabled, 41);
        mDescriptorTransacts.put(NETWORK_MANAGEMENT_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(search, 1);
        mDescriptorTransacts.put(CLOUDSEARCH_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(resetAppErrors, 217);
        transactIds.put(bindBackupAgent, 84);
        transactIds.put(performIdleMaintenance, 158);
        transactIds.put(setDumpHeapDebugLimit, 175);
        transactIds.put(updateLockTaskPackages, 177);
        transactIds.put(stopAppForUser, 73);
        // Failed to obtain transactId of getGrantedUriPermissions under android.app.IActivityManager: No field TRANSACTION_getGrantedUriPermissions in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        transactIds.put(setHasTopUi, 196);
        // Failed to obtain transactId of dismissKeyguard under android.app.IActivityManager: No field TRANSACTION_dismissKeyguard in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        transactIds.put(resumeAppSwitches, 83);
        transactIds.put(getContentProviderExternal, 120);
        transactIds.put(getIntentForIntentSender, 148);
        // Failed to obtain transactId of getTaskDescription under android.app.IActivityManager: No field TRANSACTION_getTaskDescription in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of getAssistContextExtras under android.app.IActivityManager: No field TRANSACTION_getAssistContextExtras in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        transactIds.put(unhandledBack, 10);
        // Failed to obtain transactId of inputDispatchingTimedOut under android.app.IActivityManager: No field TRANSACTION_inputDispatchingTimedOut in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        // Failed to obtain transactId of setFrontActivityScreenCompatMode under android.app.IActivityManager: No field TRANSACTION_setFrontActivityScreenCompatMode in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        transactIds.put(setAlwaysFinish, 36);
        transactIds.put(startActivityFromRecents, 165);
        // Failed to obtain transactId of releasePersistableUriPermission under android.app.IActivityManager: No field TRANSACTION_releasePersistableUriPermission in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        transactIds.put(requestBugReport, 138);
        // Failed to obtain transactId of getFrontActivityScreenCompatMode under android.app.IActivityManager: No field TRANSACTION_getFrontActivityScreenCompatMode in class Landroid/app/IActivityManager$Stub; (declaration of 'android.app.IActivityManager$Stub' appears in /system/framework/framework.jar)
        transactIds.put(setProcessLimit, 44);
        transactIds.put(signalPersistentProcesses, 52);
        transactIds.put(updateConfiguration, 41);
        transactIds.put(appNotRespondingViaProvider, 159);
        transactIds.put(shutdown, 81);
        mDescriptorTransacts.put(ACTIVITY_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(generateChallenge, 19);
        mDescriptorTransacts.put(FACE_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(createVirtualDevice, 1);
        mDescriptorTransacts.put(VIRTUAL_DEVICE_MANAGER_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(awaken, 2);
        transactIds.put(isDreaming, 7);
        mDescriptorTransacts.put(DREAMS_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(setRefreshRateSwitchingType, 46);
        transactIds.put(setTemporaryAutoBrightnessAdjustment, 37);
        transactIds.put(startWifiDisplayScan, 6);
        transactIds.put(setUserPreferredDisplayMode, 41);
        transactIds.put(shouldAlwaysRespectAppRequestedMode, 45);
        transactIds.put(requestColorMode, 19);
        mDescriptorTransacts.put(DISPLAY_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(requestProjection, 18);
        transactIds.put(getActiveProjectionTypes, 23);
        mDescriptorTransacts.put(UI_MODE_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(createInputConsumer, 83);
        transactIds.put(registerTaskFpsCallback, 130);
        // Failed to obtain transactId of requestUserActivityNotification under android.view.IWindowManager: No field TRANSACTION_requestUserActivityNotification in class Landroid/view/IWindowManager$Stub; (declaration of 'android.view.IWindowManager$Stub' appears in /system/framework/framework.jar!classes3.dex)
        transactIds.put(clearWindowContentFrameStats, 75);
        transactIds.put(addKeyguardLockedStateListener, 33);
        transactIds.put(setRecentsVisibility, 67);
        transactIds.put(stopFreezingScreen, 26);
        transactIds.put(setInTouchMode, 42);
        transactIds.put(dismissKeyguard, 32);
        transactIds.put(screenshotWallpaper, 59);
        transactIds.put(setAnimationScale, 39);
        transactIds.put(removeWindowToken, 18);
        transactIds.put(thawRotation, 52);
        // Failed to obtain transactId of getInTouchMode under android.view.IWindowManager: No field TRANSACTION_getInTouchMode in class Landroid/view/IWindowManager$Stub; (declaration of 'android.view.IWindowManager$Stub' appears in /system/framework/framework.jar!classes3.dex)
        transactIds.put(registerShortcutKey, 82);
        transactIds.put(overridePendingAppTransitionRemote, 23);
        mDescriptorTransacts.put(WINDOW_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(clearHistory, 26);
        transactIds.put(setUserRestriction, 34);
        transactIds.put(getHistoricalOps, 19);
        transactIds.put(getUidOps, 28);
        transactIds.put(noteOperation, 2);
        mDescriptorTransacts.put(APP_OPS_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getSafetyCenterConfig, 6);
        transactIds.put(getSafetySourceData, 3);
        transactIds.put(isSafetyCenterEnabled, 1);
        mDescriptorTransacts.put(SAFETY_CENTER_MANAGER_MANAGER_DESCRIPTOR, transactIds);

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
        transactIds.put(reportEnabledTrustAgentsChanged, 5);
        transactIds.put(unregisterTrustListener, 7);
        mDescriptorTransacts.put(TRUST_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getFontConfig, 1);
        mDescriptorTransacts.put(FONT_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getSpecificationInfo, 6);
        mDescriptorTransacts.put(UWB_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(verifyToken, 2);
        transactIds.put(verifyAttestation, 1);
        mDescriptorTransacts.put(ATTESTATION_VERIFICATION_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(setSensorPrivacy, 9);
        transactIds.put(isSensorPrivacyEnabled, 6);
        mDescriptorTransacts.put(SENSOR_PRIVACY_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getWifiApConfiguration, 75);
        transactIds.put(stopSoftAp, 67);
        transactIds.put(setCoexUnsafeChannels, 62);
        transactIds.put(setOverrideCountryCode, 42);
        transactIds.put(unregisterCoexCallback, 64);
        transactIds.put(setWifiEnabled, 37);
        transactIds.put(restartWifiSubsystem, 140);
        mDescriptorTransacts.put(WIFI_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getControlFd, 34);
        mDescriptorTransacts.put(USB_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(setTemporaryComponent, 2);
        mDescriptorTransacts.put(SPEECH_RECOGNITION_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(isAutoRevokeExempted, 26);
        transactIds.put(addOnPermissionsChangeListener, 10);
        // Failed to obtain transactId of isAutoRevokeWhitelisted under android.permission.IPermissionManager: No field TRANSACTION_isAutoRevokeWhitelisted in class Landroid/permission/IPermissionManager$Stub; (declaration of 'android.permission.IPermissionManager$Stub' appears in /system/framework/framework.jar!classes2.dex)
        mDescriptorTransacts.put(PERMISSION_MANAGER_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getModuleProperties, 13);
        mDescriptorTransacts.put(SOUND_TRIGGER_SESSION_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getPermittedInputMethodsAsUser, 142);
        transactIds.put(installCaCert, 100);
        transactIds.put(getNearbyNotificationStreamingPolicy, 58);
        // Failed to obtain transactId of markProfileOwnerOnOrganizationOwnedDevice under android.app.admin.IDevicePolicyManager: No field TRANSACTION_markProfileOwnerOnOrganizationOwnedDevice in class Landroid/app/admin/IDevicePolicyManager$Stub; (declaration of 'android.app.admin.IDevicePolicyManager$Stub' appears in /system/framework/framework.jar)
        transactIds.put(setStrings, 359);
        transactIds.put(getWifiSsidPolicy, 352);
        transactIds.put(setProfileOwnerOnOrganizationOwnedDevice, 306);
        transactIds.put(forceSecurityLogs, 260);
        transactIds.put(getDoNotAskCredentialsOnBoot, 220);
        transactIds.put(clearSystemUpdatePolicyFreezePeriodRecord, 217);
        transactIds.put(getString, 361);
        transactIds.put(setDeviceOwner, 78);
        mDescriptorTransacts.put(DEVICE_POLICY_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(getAvailableExtensionInterfaceNames, 5);
        transactIds.put(getCurrentTunedInfos, 39);
        mDescriptorTransacts.put(TV_INPUT_DESCRIPTOR, transactIds);

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
        transactIds.put(getAlwaysOnVpnPackage, 16);
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
        transactIds.put(setTouchCalibrationForInputDevice, 13);
        transactIds.put(isInTabletMode, 23);
        transactIds.put(monitorGestureInput, 38);
        transactIds.put(addKeyboardLayoutForInputDevice, 20);
        transactIds.put(enableInputDevice, 4);
        transactIds.put(tryPointerSpeed, 8);
        transactIds.put(removePortAssociation, 40);
        mDescriptorTransacts.put(INPUT_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        transactIds.put(reloadPersistedData, 5);
        mDescriptorTransacts.put(ROLLBACK_DESCRIPTOR, transactIds);

        transactIds = new HashMap<>();
        // Failed to obtain transactId of hideBiometricDialog under com.android.internal.statusbar.IStatusBarService: No field TRANSACTION_hideBiometricDialog in class Lcom/android/internal/statusbar/IStatusBarService$Stub; (declaration of 'com.android.internal.statusbar.IStatusBarService$Stub' appears in /system/framework/framework.jar!classes4.dex)
        transactIds.put(onBiometricHelp, 48);
        mDescriptorTransacts.put(STATUS_BAR_DESCRIPTOR, transactIds);
    }
}
