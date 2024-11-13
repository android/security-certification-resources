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

import static com.android.certifications.niap.permissions.transactids.Transacts.ACTIVITY_DESCRIPTOR;
import static com.android.certifications.niap.permissions.transactids.Transacts.BACKGROUND_INSTALL_CONTROL_DESCRIPTOR;
import static com.android.certifications.niap.permissions.transactids.Transacts.COMPANION_DEVICE_DESCRIPTOR;
import static com.android.certifications.niap.permissions.transactids.Transacts.CONTEXTUAL_SEARCH_DESCRIPTOR;
import static com.android.certifications.niap.permissions.transactids.Transacts.DEVICE_POLICY_DESCRIPTOR;
import static com.android.certifications.niap.permissions.transactids.Transacts.DISPLAY_DESCRIPTOR;
import static com.android.certifications.niap.permissions.transactids.Transacts.EUICC_CONTROLLER_DESCRIPTOR;
import static com.android.certifications.niap.permissions.transactids.Transacts.FEATURE_FLAGS_DESCRIPTOR;
import static com.android.certifications.niap.permissions.transactids.Transacts.FILE_INTEGRITY_DESCRIPTOR;
import static com.android.certifications.niap.permissions.transactids.Transacts.GRAMMATICAL_INFLECTION_DESCRIPTOR;
import static com.android.certifications.niap.permissions.transactids.Transacts.INPUT_DESCRIPTOR;
import static com.android.certifications.niap.permissions.transactids.Transacts.MEDIA_ROUTER_DESCRIPTOR;
import static com.android.certifications.niap.permissions.transactids.Transacts.NSD_DESCRIPTOR;
import static com.android.certifications.niap.permissions.transactids.Transacts.ON_DEVICE_INTELLINGENCE_DESCRIPTOR;
import static com.android.certifications.niap.permissions.transactids.Transacts.PACKAGE_DESCRIPTOR;
import static com.android.certifications.niap.permissions.transactids.Transacts.POWER_DESCRIPTOR;
import static com.android.certifications.niap.permissions.transactids.Transacts.SYSTEM_CONFIG_DESCRIPTOR;
import static com.android.certifications.niap.permissions.transactids.Transacts.TELEPHONY_DESCRIPTOR;
import static com.android.certifications.niap.permissions.transactids.Transacts.TRANSACT_PREFIX;
import static com.android.certifications.niap.permissions.transactids.Transacts.USAGE_STATS_DESCRIPTOR;
import static com.android.certifications.niap.permissions.transactids.Transacts.WINDOW_DESCRIPTOR;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Activity to drive querying for the direct binder transact IDs for the device under test. These
 * binder transacts are required since some platform declared signature protection level permissions
 * can only be verified by directly interfacing with the services guarded by these permissions.
 * Since the transact IDs can vary between devices based on the AIDL sources this activity provides
 * a quick way of obtaining the necessary IDs for the Permission Test Tool.
 *
 * <p>Note, this activity uses reflection to obtain the IDs; since these are blocked by the
 * reflection deny-list the app must be signed by the same signing key used to sign the platform.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TransactIds";
    private static final String NL = System.lineSeparator();

    public static boolean isAtLeastV() {
        return Build.VERSION.SDK_INT >= 34 && isAtLeastPreReleaseCodename("VanillaIceCream", Build.VERSION.CODENAME);
    }

    protected static boolean isAtLeastPreReleaseCodename(@NonNull String codename, @NonNull String buildCodename) {
        if ("REL".equals(buildCodename)) {
            return false;
        } else {
            String buildCodenameUpper = buildCodename.toUpperCase(Locale.ROOT);
            String codenameUpper = codename.toUpperCase(Locale.ROOT);
            return buildCodenameUpper.compareTo(codenameUpper) >= 0;
        }
    }

    /**
     * Maps the descriptor value to the constant variable name in the Transacts class; this is used
     * when generating a class that extends Transacts to be used in the Permission Test Tool
     * source.
     */
    private static final Map<String, String> sDescriptorNames = Transacts.getDescriptorNames();

    private Context mContext;
    private TextView mStatusTextView;
    private Button mQueryButton;

    /**
     * Name of the class that will be generated by this activity; a .java file with this name will
     * be generated in the app's data directory and can be pulled from the device after execution
     * for use in the Permission Test Tool.
     */
    private static final String sClassName;
    private int ACTUAL_SDK_INT = Build.VERSION.SDK_INT;
    static {
        String deviceName = Build.DEVICE;

        if (TextUtils.isEmpty(deviceName)) {
            deviceName = "Device";
        } else {
            deviceName = deviceName.substring(0, 1).toUpperCase() + deviceName.substring(
                    1).toLowerCase();
        }

        if(isAtLeastV()){
            sClassName = "SdkV_Transacts";//deviceName + "ApiLevel35Transacts";
        } else {
            sClassName = "SdkV_Transacts";//deviceName + "ApiLevel35Transacts";
            //sClassName = deviceName + "ApiLevel" + Build.VERSION.SDK_INT + "Transacts";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isAtLeastV()){
            ACTUAL_SDK_INT = 35;
        }
        Log.d("tag","Launch SDK INT>"+ACTUAL_SDK_INT);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        mStatusTextView = findViewById(R.id.statusTextView);

        mQueryButton = findViewById(R.id.queryButton);
        mQueryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TransactIdQueryAsyncTask().execute();
            }
        });

        //Transaction APIs as of Android 34
        // ProxyChecker.check(WINDOW_DESCRIPTOR, "requestAppKeyboardShortcuts");
        //ProxyChecker.check(EUICC_CONTROLLER_DESCRIPTOR,"getSupportedCountries");

        ProxyChecker.check(WINDOW_DESCRIPTOR,Transacts.registerScreenRecordingCallback);
    }

    /*
     * {@link AsyncTask} used to drive the query of the transact IDs. This task will produce a java
     * source file containing the IDs for all direct binder transacts that could be invoked.
     */
    private class TransactIdQueryAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            mQueryButton.setEnabled(false);
            mStatusTextView.setText(R.string.query_in_progress);
        }

        @Override
        protected String doInBackground(Void... noParams) {
            Map<String, Map<String, String>> descriptorTransacts = new HashMap<>();
            // Special cases; the ISurfaceComposer cannot be directly queried through reflection,
            // but its two transacts have static values; bootFinished is the default starting
            // value for transact IDs, and showCpu is out of range for all valid transact IDs for
            // the service.
            Map<String, String> transactIds = new HashMap<>();
            transactIds.put(Transacts.bootFinished, "1");
            transactIds.put(Transacts.showCpu, "1000");
            descriptorTransacts.put(Transacts.SURFACE_FLINGER_DESCRIPTOR, transactIds);
            // Special case: IResourceObserverService is a native service that cannot be queried
            // directly through reflection, 2 is the transact ID for unregisterObserver.
            transactIds = new HashMap<>();
            transactIds.put(Transacts.unregisterObserver, "2");
            descriptorTransacts.put(Transacts.RESOURCE_OBSERVER_DESCRIPTOR, transactIds);

            // This is the full list of direct binder transacts invoked by the Permission Test Tool
            // for all supported API levels. Any new transacts should be added to this list to
            // ensure invocation on other devices can obtain the proper IDs.
            queryTransactId(Transacts.ACCESSIBILITY_DESCRIPTOR, Transacts.getWindowToken,
                    descriptorTransacts);
            queryTransactId(Transacts.ACCESSIBILITY_DESCRIPTOR,
                    Transacts.registerUiTestAutomationService, descriptorTransacts);
            queryTransactId(Transacts.ACCESSIBILITY_DESCRIPTOR,
                    Transacts.setPictureInPictureActionReplacingConnection, descriptorTransacts);
            queryTransactId(Transacts.ACCESSIBILITY_DESCRIPTOR,
                    Transacts.temporaryEnableAccessibilityStateUntilKeyguardRemoved,
                    descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_DESCRIPTOR, Transacts.appNotRespondingViaProvider,
                    descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_DESCRIPTOR, Transacts.bindBackupAgent,
                    descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_DESCRIPTOR, Transacts.dismissKeyguard,
                    descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_DESCRIPTOR, Transacts.getAssistContextExtras,
                    descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_DESCRIPTOR, Transacts.getContentProviderExternal,
                    descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_DESCRIPTOR,
                    Transacts.getFrontActivityScreenCompatMode, descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_DESCRIPTOR, Transacts.getGrantedUriPermissions,
                    descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_DESCRIPTOR, Transacts.getIntentForIntentSender,
                    descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_DESCRIPTOR, Transacts.getTaskDescription,
                    descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_DESCRIPTOR, Transacts.inputDispatchingTimedOut,
                    descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_DESCRIPTOR, Transacts.performIdleMaintenance,
                    descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_DESCRIPTOR,
                    Transacts.releasePersistableUriPermission, descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_DESCRIPTOR, Transacts.requestBugReport,
                    descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_DESCRIPTOR, Transacts.resumeAppSwitches,
                    descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_DESCRIPTOR, Transacts.setAlwaysFinish,
                    descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_DESCRIPTOR, Transacts.setDumpHeapDebugLimit,
                    descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_DESCRIPTOR,
                    Transacts.setFrontActivityScreenCompatMode, descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_DESCRIPTOR, Transacts.setHasTopUi,
                    descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_DESCRIPTOR, Transacts.setProcessLimit,
                    descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_DESCRIPTOR, Transacts.shutdown, descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_DESCRIPTOR, Transacts.signalPersistentProcesses,
                    descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_DESCRIPTOR, Transacts.startActivityFromRecents,
                    descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_DESCRIPTOR, Transacts.unhandledBack,
                    descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_DESCRIPTOR, Transacts.updateConfiguration,
                    descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_DESCRIPTOR, Transacts.updateLockTaskPackages,
                    descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_TASK_DESCRIPTOR, Transacts.dismissKeyguard,
                    descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_TASK_DESCRIPTOR, Transacts.getAssistContextExtras,
                    descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_TASK_DESCRIPTOR,
                    Transacts.getFrontActivityScreenCompatMode, descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_TASK_DESCRIPTOR, Transacts.getTaskDescription,
                    descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_TASK_DESCRIPTOR,
                    Transacts.setFrontActivityScreenCompatMode, descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_TASK_DESCRIPTOR, Transacts.startActivityAsCaller,
                    descriptorTransacts);
            queryTransactId(Transacts.ALARM_DESCRIPTOR, Transacts.setTime, descriptorTransacts);
            queryTransactId(Transacts.ALARM_DESCRIPTOR, Transacts.setTimeZone, descriptorTransacts);
            queryTransactId(Transacts.APPWIDGET_DESCRIPTOR, Transacts.setBindAppWidgetPermission,
                    descriptorTransacts);
            queryTransactId(Transacts.APP_OPS_DESCRIPTOR, Transacts.clearHistory,
                    descriptorTransacts);
            queryTransactId(Transacts.APP_OPS_DESCRIPTOR, Transacts.noteOperation,
                    descriptorTransacts);
            queryTransactId(Transacts.APP_OPS_DESCRIPTOR, Transacts.setUserRestriction,
                    descriptorTransacts);
            queryTransactId(Transacts.APP_OPS_DESCRIPTOR, Transacts.permissionToOpCode,
                    descriptorTransacts);

            queryTransactId(Transacts.AUDIO_DESCRIPTOR, Transacts.getRingtonePlayer,
                    descriptorTransacts);
            queryTransactId(Transacts.AUDIO_DESCRIPTOR, Transacts.isAudioServerRunning,
                    descriptorTransacts);
            queryTransactId(Transacts.AUDIO_DESCRIPTOR, Transacts.setRingtonePlayer,
                    descriptorTransacts);
            queryTransactId(Transacts.AUDIO_DESCRIPTOR, Transacts.forceRemoteSubmixFullVolume,
                    descriptorTransacts);
            queryTransactId(Transacts.BACKUP_DESCRIPTOR, Transacts.setBackupEnabled,
                    descriptorTransacts);
            queryTransactId(Transacts.BATTERY_STATS_DESCRIPTOR, Transacts.getAwakeTimeBattery,
                    descriptorTransacts);
            queryTransactId(Transacts.BATTERY_STATS_DESCRIPTOR, Transacts.noteStartAudio,
                    descriptorTransacts);
            queryTransactId(Transacts.BIOMETRIC_DESCRIPTOR, Transacts.hasEnrolledBiometrics,
                    descriptorTransacts);
            queryTransactId(Transacts.CAMERA_DESCRIPTOR, Transacts.notifySystemEvent,
                    descriptorTransacts);
            queryTransactId(Transacts.CONNECTIVITY_DESCRIPTOR, Transacts.getActiveNetworkForUid,
                    descriptorTransacts);
            queryTransactId(Transacts.CONNECTIVITY_DESCRIPTOR, Transacts.getAlwaysOnVpnPackage,
                    descriptorTransacts);
            queryTransactId(Transacts.CONNECTIVITY_DESCRIPTOR, Transacts.startNattKeepalive,
                    descriptorTransacts);
            queryTransactId(Transacts.CONNECTIVITY_DESCRIPTOR, Transacts.tether,
                    descriptorTransacts);
            queryTransactId(Transacts.CONNECTIVITY_DESCRIPTOR, Transacts.getActiveNetwork,
                    descriptorTransacts);
            queryTransactId(Transacts.CONNECTIVITY_DESCRIPTOR, Transacts.getActiveNetworkInfo,
                    descriptorTransacts);

            queryTransactId(Transacts.CROSS_PROFILE_APPS_DESCRIPTOR,
                    Transacts.clearInteractAcrossProfilesAppOps, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR,
                    Transacts.getDoNotAskCredentialsOnBoot, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.installCaCert,
                    descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR,
                    Transacts.markProfileOwnerOnOrganizationOwnedDevice, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.setDeviceOwner,
                    descriptorTransacts);
            queryTransactId(Transacts.DISPLAY_DESCRIPTOR, Transacts.requestColorMode,
                    descriptorTransacts);
            queryTransactId(Transacts.DISPLAY_DESCRIPTOR,
                    Transacts.setTemporaryAutoBrightnessAdjustment, descriptorTransacts);
            queryTransactId(Transacts.DISPLAY_DESCRIPTOR, Transacts.startWifiDisplayScan,
                    descriptorTransacts);
            queryTransactId(Transacts.DREAMS_DESCRIPTOR, Transacts.awaken, descriptorTransacts);
            queryTransactId(Transacts.DREAMS_DESCRIPTOR, Transacts.isDreaming, descriptorTransacts);
            queryTransactId(Transacts.DROPBOX_DESCRIPTOR, Transacts.getNextEntry,
                    descriptorTransacts);
            queryTransactId(Transacts.EUICC_CONTROLLER_DESCRIPTOR,
                    Transacts.retainSubscriptionsForFactoryReset, descriptorTransacts);
            queryTransactId(Transacts.FACE_DESCRIPTOR, Transacts.generateChallenge,
                    descriptorTransacts);
            queryTransactId(Transacts.FINGERPRINT_DESCRIPTOR,
                    Transacts.cancelAuthenticationFromService, descriptorTransacts);
            queryTransactId(Transacts.FINGERPRINT_DESCRIPTOR, Transacts.cancelEnrollment,
                    descriptorTransacts);
            queryTransactId(Transacts.FINGERPRINT_DESCRIPTOR, Transacts.resetTimeout,
                    descriptorTransacts);
            queryTransactId(Transacts.INPUT_DESCRIPTOR, Transacts.addKeyboardLayoutForInputDevice,
                    descriptorTransacts);
            queryTransactId(Transacts.INPUT_DESCRIPTOR, Transacts.setKeyboardLayoutForInputDevice,
                    descriptorTransacts);
            queryTransactId(Transacts.INPUT_DESCRIPTOR, Transacts.enableInputDevice,
                    descriptorTransacts);
            queryTransactId(Transacts.INPUT_DESCRIPTOR, Transacts.isInTabletMode,
                    descriptorTransacts);
            queryTransactId(Transacts.INPUT_DESCRIPTOR, Transacts.monitorGestureInput,
                    descriptorTransacts);
            queryTransactId(Transacts.INPUT_DESCRIPTOR, Transacts.removePortAssociation,
                    descriptorTransacts);
            queryTransactId(Transacts.INPUT_DESCRIPTOR, Transacts.setTouchCalibrationForInputDevice,
                    descriptorTransacts);
            queryTransactId(Transacts.INPUT_DESCRIPTOR, Transacts.tryPointerSpeed,
                    descriptorTransacts);
            queryTransactId(Transacts.ISUB_DESCRIPTOR,
                    Transacts.requestEmbeddedSubscriptionInfoListRefresh, descriptorTransacts);
            queryTransactId(Transacts.MEDIA_PROJECTION_DESCRIPTOR,
                    Transacts.getActiveProjectionInfo, descriptorTransacts);
            queryTransactId(Transacts.MEDIA_SESSION_DESCRIPTOR, Transacts.setOnMediaKeyListener,
                    descriptorTransacts);
            queryTransactId(Transacts.MEDIA_SESSION_DESCRIPTOR,
                    Transacts.setOnVolumeKeyLongPressListener, descriptorTransacts);
            queryTransactId(Transacts.MOUNT_DESCRIPTOR, Transacts.benchmark, descriptorTransacts);
            queryTransactId(Transacts.MOUNT_DESCRIPTOR, Transacts.getCacheSizeBytes,
                    descriptorTransacts);
            queryTransactId(Transacts.MOUNT_DESCRIPTOR, Transacts.getEncryptionState,
                    descriptorTransacts);
            queryTransactId(Transacts.NETWORK_MANAGEMENT_DESCRIPTOR,
                    Transacts.setDataSaverModeEnabled, descriptorTransacts);
            queryTransactId(Transacts.NETWORK_STATS_DESCRIPTOR, Transacts.forceUpdate,
                    descriptorTransacts);
            queryTransactId(Transacts.NETWORK_STATS_DESCRIPTOR,
                    Transacts.registerNetworkStatsProvider, descriptorTransacts);
            queryTransactId(Transacts.NET_POLICY_DESCRIPTOR, Transacts.getSubscriptionPlans,
                    descriptorTransacts);
            queryTransactId(Transacts.NET_POLICY_DESCRIPTOR, Transacts.getUidPolicy,
                    descriptorTransacts);
            queryTransactId(Transacts.NET_POLICY_DESCRIPTOR, Transacts.registerListener,
                    descriptorTransacts);
            queryTransactId(Transacts.NOTIFICATION_DESCRIPTOR, Transacts.getActiveNotifications,
                    descriptorTransacts);
            queryTransactId(Transacts.NOTIFICATION_DESCRIPTOR, Transacts.getZenRules,
                    descriptorTransacts);
            queryTransactId(Transacts.NOTIFICATION_DESCRIPTOR,
                    Transacts.isNotificationPolicyAccessGrantedForPackage, descriptorTransacts);
            queryTransactId(Transacts.PACKAGE_DESCRIPTOR, Transacts.addOnPermissionsChangeListener,
                    descriptorTransacts);
            queryTransactId(Transacts.PACKAGE_DESCRIPTOR, Transacts.getHarmfulAppWarning,
                    descriptorTransacts);
            queryTransactId(Transacts.PACKAGE_DESCRIPTOR, Transacts.getMoveStatus,
                    descriptorTransacts);
            queryTransactId(Transacts.PACKAGE_DESCRIPTOR, Transacts.getRuntimePermissionsVersion,
                    descriptorTransacts);
            queryTransactId(Transacts.PACKAGE_DESCRIPTOR, Transacts.installExistingPackageAsUser,
                    descriptorTransacts);
            queryTransactId(Transacts.PACKAGE_DESCRIPTOR, Transacts.installExistingPackageAsUser,
                    descriptorTransacts);
            queryTransactId(Transacts.PACKAGE_DESCRIPTOR, Transacts.isPackageDeviceAdminOnAnyUser,
                    descriptorTransacts);
            queryTransactId(Transacts.PACKAGE_DESCRIPTOR, Transacts.isPackageStateProtected,
                    descriptorTransacts);
            queryTransactId(Transacts.PACKAGE_DESCRIPTOR, Transacts.movePackage,
                    descriptorTransacts);
            queryTransactId(Transacts.PACKAGE_DESCRIPTOR, Transacts.resetApplicationPreferences,
                    descriptorTransacts);
            queryTransactId(Transacts.PERMISSION_MANAGER_DESCRIPTOR,
                    Transacts.addOnPermissionsChangeListener, descriptorTransacts);
            queryTransactId(Transacts.PERMISSION_MANAGER_DESCRIPTOR,
                    Transacts.isAutoRevokeWhitelisted, descriptorTransacts);
            queryTransactId(Transacts.PLATFORM_COMPAT_DESCRIPTOR, Transacts.clearOverridesForTest,
                    descriptorTransacts);
            queryTransactId(Transacts.PLATFORM_COMPAT_DESCRIPTOR, Transacts.reportChangeByUid,
                    descriptorTransacts);
            queryTransactId(Transacts.POWER_DESCRIPTOR, Transacts.reboot, descriptorTransacts);
            queryTransactId(Transacts.POWER_DESCRIPTOR, Transacts.setPowerSaveMode,
                    descriptorTransacts);
            queryTransactId(Transacts.RESOURCE_MANAGER_DESCRIPTOR, Transacts.overridePid,
                    descriptorTransacts);
            queryTransactId(Transacts.ROLE_DESCRIPTOR,
                    Transacts.addOnRoleHoldersChangedListenerAsUser, descriptorTransacts);
            queryTransactId(Transacts.ROLLBACK_DESCRIPTOR, Transacts.reloadPersistedData,
                    descriptorTransacts);
            queryTransactId(Transacts.SENSOR_PRIVACY_DESCRIPTOR, Transacts.setSensorPrivacy,
                    descriptorTransacts);
            queryTransactId(Transacts.SHORTCUT_DESCRIPTOR, Transacts.onApplicationActive,
                    descriptorTransacts);
            queryTransactId(Transacts.SLICE_DESCRIPTOR, Transacts.grantPermissionFromUser,
                    descriptorTransacts);
            queryTransactId(Transacts.SOUND_TRIGGER_DESCRIPTOR, Transacts.isRecognitionActive,
                    descriptorTransacts);
            queryTransactId(Transacts.STATUS_BAR_DESCRIPTOR, Transacts.hideBiometricDialog,
                    descriptorTransacts);
            queryTransactId(Transacts.STATUS_BAR_DESCRIPTOR, Transacts.onBiometricHelp,
                    descriptorTransacts);
            queryTransactId(Transacts.TELEPHONY_DESCRIPTOR, Transacts.enableLocationUpdates,
                    descriptorTransacts);
            queryTransactId(Transacts.TELEPHONY_DESCRIPTOR, Transacts.getVtDataUsage,
                    descriptorTransacts);
            queryTransactId(Transacts.TRUST_DESCRIPTOR, Transacts.reportEnabledTrustAgentsChanged,
                    descriptorTransacts);
            queryTransactId(Transacts.TRUST_DESCRIPTOR, Transacts.unregisterTrustListener,
                    descriptorTransacts);
            queryTransactId(Transacts.URI_GRANTS_DESCRIPTOR, Transacts.getGrantedUriPermissions,
                    descriptorTransacts);
            queryTransactId(Transacts.URI_GRANTS_DESCRIPTOR, Transacts.takePersistableUriPermission,
                    descriptorTransacts);
            queryTransactId(Transacts.USB_DESCRIPTOR, Transacts.getControlFd, descriptorTransacts);
            queryTransactId(Transacts.VIBRATOR_DESCRIPTOR, Transacts.isVibrating,
                    descriptorTransacts);
            queryTransactId(Transacts.VIBRATOR_DESCRIPTOR, Transacts.setAlwaysOnEffect,
                    descriptorTransacts);
            queryTransactId(Transacts.VOICE_INTERACTION_DESCRIPTOR,
                    Transacts.getActiveServiceComponentName, descriptorTransacts);
            queryTransactId(Transacts.VOICE_INTERACTION_DESCRIPTOR,
                    Transacts.updateKeyphraseSoundModel, descriptorTransacts);
            queryTransactId(Transacts.VR_DESCRIPTOR, Transacts.getVrModeState, descriptorTransacts);
            queryTransactId(Transacts.VR_DESCRIPTOR, Transacts.setPersistentVrModeEnabled,
                    descriptorTransacts);
            queryTransactId(Transacts.VR_DESCRIPTOR, Transacts.setStandbyEnabled,
                    descriptorTransacts);
            queryTransactId(Transacts.WALLPAPER_DESCRIPTOR, Transacts.setWallpaper,
                    descriptorTransacts);
            queryTransactId(Transacts.WALLPAPER_DESCRIPTOR, Transacts.setWallpaperComponent,
                    descriptorTransacts);
            queryTransactId(Transacts.WIFI_DESCRIPTOR, Transacts.getWifiApConfiguration,
                    descriptorTransacts);
            queryTransactId(Transacts.WIFI_DESCRIPTOR, Transacts.setWifiEnabled,
                    descriptorTransacts);
            queryTransactId(Transacts.WIFI_DESCRIPTOR, Transacts.stopSoftAp, descriptorTransacts);
            queryTransactId(Transacts.WINDOW_DESCRIPTOR, Transacts.clearWindowContentFrameStats,
                    descriptorTransacts);
            queryTransactId(Transacts.WINDOW_DESCRIPTOR, Transacts.dismissKeyguard,
                    descriptorTransacts);
            queryTransactId(Transacts.WINDOW_DESCRIPTOR,
                    Transacts.overridePendingAppTransitionRemote, descriptorTransacts);
            queryTransactId(Transacts.WINDOW_DESCRIPTOR, Transacts.registerShortcutKey,
                    descriptorTransacts);
            queryTransactId(Transacts.WINDOW_DESCRIPTOR, Transacts.removeWindowToken,
                    descriptorTransacts);
            queryTransactId(Transacts.WINDOW_DESCRIPTOR, Transacts.requestUserActivityNotification,
                    descriptorTransacts);
            queryTransactId(Transacts.WINDOW_DESCRIPTOR, Transacts.screenshotWallpaper,
                    descriptorTransacts);
            queryTransactId(Transacts.WINDOW_DESCRIPTOR, Transacts.setAnimationScale,
                    descriptorTransacts);
            queryTransactId(Transacts.WINDOW_DESCRIPTOR, Transacts.setRecentsVisibility,
                    descriptorTransacts);
            queryTransactId(Transacts.WINDOW_DESCRIPTOR, Transacts.stopFreezingScreen,
                    descriptorTransacts);
            queryTransactId(Transacts.WINDOW_DESCRIPTOR, Transacts.thawRotation,
                    descriptorTransacts);

            queryTransactId(Transacts.WINDOW_DESCRIPTOR, Transacts.addKeyguardLockedStateListener,
                    descriptorTransacts);

            // The following are the transacts required for new permissions in Android 12.
            queryTransactId(Transacts.CAMERA_DESCRIPTOR, Transacts.injectCamera,
                    descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR,
                    Transacts.clearSystemUpdatePolicyFreezePeriodRecord, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_STATE_DESCRIPTOR, Transacts.cancelRequest,
                    descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.forceSecurityLogs,
                    descriptorTransacts);
            queryTransactId(Transacts.WINDOW_DESCRIPTOR, Transacts.createInputConsumer,
                    descriptorTransacts);
            queryTransactId(Transacts.PACKAGE_DESCRIPTOR, Transacts.setKeepUninstalledPackages,
                    descriptorTransacts);
            queryTransactId(Transacts.KEY_CHAIN_DESCRIPTOR, Transacts.removeCredentialManagementApp,
                    descriptorTransacts);
            queryTransactId(Transacts.GAME_DESCRIPTOR, Transacts.getAvailableGameModes,
                    descriptorTransacts);
            queryTransactId(Transacts.SMART_SPACE_DESCRIPTOR, Transacts.destroySmartspaceSession,
                    descriptorTransacts);
            queryTransactId(Transacts.SPEECH_RECOGNITION_DESCRIPTOR,
                    Transacts.setTemporaryComponent, descriptorTransacts);
            queryTransactId(Transacts.NOTIFICATION_DESCRIPTOR,
                    Transacts.setToastRateLimitingEnabled, descriptorTransacts);
            queryTransactId(Transacts.WIFI_DESCRIPTOR, Transacts.setOverrideCountryCode,
                    descriptorTransacts);
            queryTransactId(Transacts.DISPLAY_DESCRIPTOR, Transacts.setRefreshRateSwitchingType,
                    descriptorTransacts);
            queryTransactId(Transacts.DISPLAY_DESCRIPTOR,
                    Transacts.shouldAlwaysRespectAppRequestedMode, descriptorTransacts);
            queryTransactId(Transacts.AUDIO_DESCRIPTOR, Transacts.getDeviceVolumeBehavior,
                    descriptorTransacts);
            queryTransactId(Transacts.POWER_DESCRIPTOR,
                    Transacts.isAmbientDisplaySuppressedForTokenByApp, descriptorTransacts);
            queryTransactId(Transacts.UI_MODE_DESCRIPTOR, Transacts.getActiveProjectionTypes,
                    descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_DESCRIPTOR, Transacts.resetAppErrors,
                    descriptorTransacts);
            queryTransactId(Transacts.LOCK_SETTINGS_DESCRIPTOR, Transacts.verifyCredential,
                    descriptorTransacts);
            queryTransactId(Transacts.AUTH_DESCRIPTOR, Transacts.getUiPackage, descriptorTransacts);
            queryTransactId(Transacts.DOMAIN_VERIFICATION_DESCRIPTOR,
                    Transacts.setDomainVerificationLinkHandlingAllowed, descriptorTransacts);
            queryTransactId(Transacts.NOTIFICATION_DESCRIPTOR,
                    Transacts.getEnabledNotificationListeners, descriptorTransacts);
            queryTransactId(Transacts.POWER_DESCRIPTOR, Transacts.setBatteryDischargePrediction,
                    descriptorTransacts);
            queryTransactId(Transacts.TIME_DETECTOR_DESCRIPTOR,
                    Transacts.getCapabilitiesAndConfig, descriptorTransacts);
            queryTransactId(Transacts.NFC_DESCRIPTOR, Transacts.isControllerAlwaysOnSupported,
                    descriptorTransacts);
            queryTransactId(Transacts.PLATFORM_COMPAT_DESCRIPTOR,
                    Transacts.removeOverridesOnReleaseBuilds, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR,
                    Transacts.getNearbyNotificationStreamingPolicy, descriptorTransacts);
            queryTransactId(Transacts.PERMISSION_CHECKER_DESCRIPTOR, Transacts.checkPermission,
                    descriptorTransacts);
            queryTransactId(Transacts.WIFI_DESCRIPTOR, Transacts.restartWifiSubsystem,
                    descriptorTransacts);
            queryTransactId(Transacts.ALARM_DESCRIPTOR, Transacts.set, descriptorTransacts);
            queryTransactId(Transacts.REBOOT_READINESS_DESCRIPTOR,
                    Transacts.removeRequestRebootReadinessStatusListener, descriptorTransacts);
            queryTransactId(Transacts.SOUND_TRIGGER_DESCRIPTOR, Transacts.attachAsMiddleman,
                    descriptorTransacts);
            queryTransactId(Transacts.TIME_DETECTOR_DESCRIPTOR, Transacts.suggestExternalTime,
                    descriptorTransacts);
            queryTransactId(Transacts.UI_MODE_DESCRIPTOR, Transacts.requestProjection,
                    descriptorTransacts);
            queryTransactId(Transacts.FONT_DESCRIPTOR, Transacts.getFontConfig,
                    descriptorTransacts);
            queryTransactId(Transacts.UWB_DESCRIPTOR, Transacts.getSpecificationInfo,
                    descriptorTransacts);
            queryTransactId(Transacts.MUSIC_RECOGNITION_DESCRIPTOR, Transacts.beginRecognition,
                    descriptorTransacts);
            queryTransactId(Transacts.TRANSLATION_DESCRIPTOR, Transacts.updateUiTranslationState,
                    descriptorTransacts);
            queryTransactId(Transacts.TV_INPUT_DESCRIPTOR, Transacts.getCurrentTunedInfos,
                    descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_TASK_DESCRIPTOR,
                    Transacts.getWindowOrganizerController, descriptorTransacts);
            queryTransactId(Transacts.CLIPBOARD_DESCRIPTOR, Transacts.getPrimaryClipSource,
                    descriptorTransacts);
            queryTransactId(Transacts.PEOPLE_DESCRIPTOR, Transacts.isConversation,
                    descriptorTransacts);
            queryTransactId(Transacts.WIFI_DESCRIPTOR, Transacts.unregisterCoexCallback,
                    descriptorTransacts);
            queryTransactId(Transacts.WIFI_DESCRIPTOR, Transacts.setCoexUnsafeChannels,
                    descriptorTransacts);
            queryTransactId(Transacts.VOICE_INTERACTION_DESCRIPTOR, Transacts.updateState,
                    descriptorTransacts);
            queryTransactId(Transacts.SENSOR_PRIVACY_DESCRIPTOR, Transacts.isSensorPrivacyEnabled,
                    descriptorTransacts);
            queryTransactId(Transacts.DOMAIN_VERIFICATION_DESCRIPTOR,
                    Transacts.queryValidVerificationPackageNames, descriptorTransacts);
            queryTransactId(Transacts.TELEPHONY_IMS_DESCRIPTOR, Transacts.requestAvailability,
                    descriptorTransacts);
            queryTransactId(Transacts.COMPANION_DEVICE_DESCRIPTOR, Transacts.createAssociation,
                    descriptorTransacts);
            queryTransactId(Transacts.ROLE_DESCRIPTOR, Transacts.setBypassingRoleQualification,
                    descriptorTransacts);
            queryTransactId(Transacts.TELEPHONY_IMS_DESCRIPTOR,
                    Transacts.triggerNetworkRegistration, descriptorTransacts);
            queryTransactId(Transacts.VPN_DESCRIPTOR, Transacts.getAlwaysOnVpnPackage,
                    descriptorTransacts);
            queryTransactId(Transacts.SOUND_TRIGGER_DESCRIPTOR, Transacts.attachAsOriginator,
                    descriptorTransacts);
            queryTransactId(Transacts.SOUND_TRIGGER_SESSION_DESCRIPTOR,
                    Transacts.getModuleProperties, descriptorTransacts);
            queryTransactId(Transacts.POWER_DESCRIPTOR, Transacts.setDynamicPowerSaveHint,
                    descriptorTransacts);
            queryTransactId(Transacts.FINGERPRINT_DESCRIPTOR, Transacts.resetLockout,
                    descriptorTransacts);
            queryTransactId(Transacts.PERMISSION_MANAGER_DESCRIPTOR, Transacts.isAutoRevokeExempted,
                    descriptorTransacts);
            queryTransactId(Transacts.NET_POLICY_DESCRIPTOR, Transacts.isUidNetworkingBlocked,
                    descriptorTransacts);
            queryTransactId(Transacts.VOICE_INTERACTION_DESCRIPTOR, Transacts.isSessionRunning,
                    descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_TASK_DESCRIPTOR,
                    Transacts.getActivityClientController, descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_CLIENT_DESCRIPTOR, Transacts.dismissKeyguard,
                    descriptorTransacts);
            queryTransactId(Transacts.CONNECTIVITY_DESCRIPTOR, Transacts.pendingRequestForNetwork,
                    descriptorTransacts);
            queryTransactId(Transacts.APP_OPS_DESCRIPTOR, Transacts.getUidOps, descriptorTransacts);
            //For Android T
            //   Internal Permissions
            queryTransactId(Transacts.AMBIENT_CONTEXT_MANAGER_DESCRIPTOR, Transacts.queryServiceStatus,
                    descriptorTransacts);
            queryTransactId(Transacts.SAFETY_CENTER_MANAGER_MANAGER_DESCRIPTOR, Transacts.isSafetyCenterEnabled,
                    descriptorTransacts);
            queryTransactId(Transacts.SAFETY_CENTER_MANAGER_MANAGER_DESCRIPTOR, Transacts.getSafetyCenterConfig,
                    descriptorTransacts);
            queryTransactId(Transacts.SAFETY_CENTER_MANAGER_MANAGER_DESCRIPTOR, Transacts.getSafetySourceData,
                    descriptorTransacts);
            queryTransactId(Transacts.VIRTUAL_DEVICE_MANAGER_DESCRIPTOR, Transacts.createVirtualDevice,
                    descriptorTransacts);

            //   Signature Permissions
            queryTransactId(Transacts.ATTESTATION_VERIFICATION_DESCRIPTOR, Transacts.verifyToken,
                    descriptorTransacts);
            queryTransactId(Transacts.ATTESTATION_VERIFICATION_DESCRIPTOR, Transacts.verifyAttestation,
                    descriptorTransacts);

            queryTransactId(Transacts.WINDOW_DESCRIPTOR, Transacts.setInTouchMode,
                    descriptorTransacts);
            queryTransactId(Transacts.WINDOW_DESCRIPTOR, Transacts.getInTouchMode,
                    descriptorTransacts);

            queryTransactId(Transacts.CLOUDSEARCH_DESCRIPTOR, Transacts.search,
                    descriptorTransacts);
            queryTransactId(Transacts.WALLPAPER_EFFECTS_GENERATION_DESCRIPTOR, Transacts.generateCinematicEffect,
                    descriptorTransacts);

            queryTransactId(Transacts.CLIPBOARD_DESCRIPTOR, Transacts.showAccessNotificationLocked,
                    descriptorTransacts);

            queryTransactId(Transacts.GAME_DESCRIPTOR, Transacts.setGameServiceProvider,
                    descriptorTransacts);

            queryTransactId(Transacts.WINDOW_DESCRIPTOR, Transacts.registerTaskFpsCallback,
                    descriptorTransacts);
            queryTransactId(Transacts.GAME_DESCRIPTOR, Transacts.createGameSession,
                    descriptorTransacts);

            queryTransactId(Transacts.TV_INPUT_DESCRIPTOR,
                    Transacts.getAvailableExtensionInterfaceNames,
                    descriptorTransacts);

            queryTransactId(Transacts.PACKAGE_DESCRIPTOR,
                    Transacts.makeUidVisible,
                    descriptorTransacts);
            queryTransactId(Transacts.APP_OPS_DESCRIPTOR,
                    Transacts.getHistoricalOps,
                    descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR,
                    Transacts.setStrings,
                    descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR,
                    Transacts.getString,
                    descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR,
                    Transacts.getPermittedInputMethodsAsUser,
                    descriptorTransacts);

            queryTransactId(Transacts.COMPANION_DEVICE_DESCRIPTOR,
                    Transacts.dispatchMessage,
                    descriptorTransacts);

            queryTransactId(Transacts.ACTIVITY_TASK_DESCRIPTOR,
                    Transacts.startActivityFromGameSession,
                    descriptorTransacts);

            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR,
                    Transacts.getWifiSsidPolicy,descriptorTransacts);

            queryTransactId(Transacts.MOUNT_DESCRIPTOR,
                    Transacts.isConvertibleToFBE,descriptorTransacts);

            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR,
                    Transacts.setProfileOwnerOnOrganizationOwnedDevice,descriptorTransacts);

            queryTransactId(Transacts.DEVICE_STATE_DESCRIPTOR,
                    Transacts.cancelStateRequest,descriptorTransacts);

            queryTransactId(Transacts.DISPLAY_DESCRIPTOR,
                    Transacts.setUserPreferredDisplayMode,descriptorTransacts);
            queryTransactId(Transacts.DISPLAY_DESCRIPTOR,
                    Transacts.getUserPreferredDisplayMode,descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_DESCRIPTOR,
                    Transacts.stopAppForUser,descriptorTransacts);

            //For Android 34
            queryTransactId(Transacts.ACTIVITY_DESCRIPTOR, Transacts.broadcastIntentWithFeature, descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_DESCRIPTOR, Transacts.killAllBackgroundProcesses, descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_DESCRIPTOR, Transacts.logFgsApiBegin, descriptorTransacts);
            queryTransactId(Transacts.AUDIO_DESCRIPTOR, Transacts.setVolumeGroupVolumeIndex, descriptorTransacts);
            queryTransactId(Transacts.CLIPBOARD_DESCRIPTOR, Transacts.areClipboardAccessNotificationsEnabledForUser, descriptorTransacts);
            queryTransactId(Transacts.CREDENTIAL_DESCRIPTOR, Transacts.getCredentialProviderServices, descriptorTransacts);
            queryTransactId(Transacts.DEVICELOCK_DESCRIPTOR, Transacts.isDeviceLocked, descriptorTransacts);
            queryTransactId(Transacts.DISPLAY_DESCRIPTOR, Transacts.setHdrConversionMode, descriptorTransacts);
            queryTransactId(Transacts.HEALTH_CONNECT_DESCRIPTOR, Transacts.deleteAllStagedRemoteData, descriptorTransacts);
            queryTransactId(Transacts.HEALTH_CONNECT_DESCRIPTOR, Transacts.startMigration, descriptorTransacts);
            queryTransactId(Transacts.HEALTH_CONNECT_DESCRIPTOR, Transacts.getHealthConnectDataState, descriptorTransacts);


            queryTransactId(Transacts.HEALTH_CONNECT_DESCRIPTOR, Transacts.updateDataDownloadState, descriptorTransacts);
            queryTransactId(Transacts.INPUT_DESCRIPTOR, Transacts.getModifierKeyRemapping, descriptorTransacts);
            queryTransactId(Transacts.INPUT_DESCRIPTOR, Transacts.registerKeyboardBacklightListener, descriptorTransacts);
            queryTransactId(Transacts.INPUTMETHOD_DESCRIPTOR, Transacts.isInputMethodPickerShownForTest, descriptorTransacts);
            queryTransactId(Transacts.LOCALE_DESCRIPTOR, Transacts.setOverrideLocaleConfig, descriptorTransacts);
            queryTransactId(Transacts.PACKAGE_DESCRIPTOR, Transacts.getAppMetadataFd, descriptorTransacts);
            queryTransactId(Transacts.POWER_DESCRIPTOR, Transacts.acquireWakeLock, descriptorTransacts);
            queryTransactId(Transacts.POWER_DESCRIPTOR, Transacts.releaseLowPowerStandbyPorts, descriptorTransacts);
            queryTransactId(Transacts.ROLE_DESCRIPTOR, Transacts.getDefaultApplicationAsUser, descriptorTransacts);
            queryTransactId(Transacts.STATS_DESCRIPTOR, Transacts.setRestrictedMetricsChangedOperation, descriptorTransacts);
            queryTransactId(Transacts.SUBSCRIPTION_DESCRIPTOR, Transacts.setSubscriptionUserHandle, descriptorTransacts);
            queryTransactId(Transacts.TELEPHONY_DESCRIPTOR, Transacts.requestIsSatelliteEnabled, descriptorTransacts);
            queryTransactId(Transacts.TELEPHONY_DESCRIPTOR, Transacts.requestSatelliteEnabled, descriptorTransacts);
            queryTransactId(Transacts.WEARABLES_DESCRIPTOR, Transacts.provideDataStream, descriptorTransacts);
            queryTransactId(Transacts.ACTIVITY_DESCRIPTOR, Transacts.getMimeTypeFilterAsync, descriptorTransacts);
            queryTransactId(Transacts.LOCK_SETTINGS_DESCRIPTOR, Transacts.startRemoteLockscreenValidation, descriptorTransacts);

            //DevicePolicyDescriptors
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.setCameraDisabled, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.addCrossProfileWidgetProvider, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.getCrossProfileWidgetProviders, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.hasLockdownAdminConfiguredNetworks, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.installKeyPair, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.installUpdateFromFile, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.isPackageSuspended, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.setAccountManagementDisabled, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.setApplicationExemptions, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.setApplicationRestrictions, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.setCommonCriteriaModeEnabled, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.setConfiguredNetworksLockdownState, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.setDefaultSmsApplication, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.setFactoryResetProtectionPolicy, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.setKeyguardDisabledFeatures, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.setLockTaskPackages, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.setMaximumFailedPasswordsForWipe, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.setMaximumTimeToLock, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.setMaximumTimeToLock, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.setMtePolicy, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.setOrganizationName, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.setPasswordExpirationTimeout, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.setPermissionGrantState, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.setPermittedInputMethods, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.setResetPasswordToken, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.setScreenCaptureDisabled, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.setSecurityLoggingEnabled, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.setShortSupportMessage, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.setStatusBarDisabled, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.setSystemUpdatePolicy, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.setUsbDataSignalingEnabled, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.setUserControlDisabledPackages, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.setUserRestriction, descriptorTransacts);
            queryTransactId(Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.setTrustAgentConfiguration, descriptorTransacts);
            queryTransactId(Transacts.STATS_DESCRIPTOR,Transacts.removeRestrictedMetricsChangedOperation,descriptorTransacts);
            queryTransactId(Transacts.SURFACE_FLINGER_DESCRIPTOR,Transacts.setTransactionState,descriptorTransacts);
            queryTransactId(Transacts.WINDOW_DESCRIPTOR, Transacts.requestAppKeyboardShortcuts, descriptorTransacts);
            queryTransactId(EUICC_CONTROLLER_DESCRIPTOR, Transacts.getSupportedCountries, descriptorTransacts);
            queryTransactId(Transacts.UWB_DESCRIPTOR, Transacts.openRanging, descriptorTransacts);

            //For Android 15
            queryTransactId(WINDOW_DESCRIPTOR,Transacts.registerScreenRecordingCallback,descriptorTransacts);
            queryTransactId(Transacts.PDB_DESCRIPTOR,Transacts.deactivateFactoryResetProtection,descriptorTransacts);
            queryTransactId(TELEPHONY_DESCRIPTOR,Transacts.getLastKnownCellIdentity,descriptorTransacts);
            queryTransactId(SYSTEM_CONFIG_DESCRIPTOR,Transacts.getEnhancedConfirmationTrustedPackages,descriptorTransacts);
            queryTransactId(COMPANION_DEVICE_DESCRIPTOR,Transacts.startObservingDevicePresence,descriptorTransacts);
            queryTransactId(COMPANION_DEVICE_DESCRIPTOR,Transacts.getAllAssociationsForUser,descriptorTransacts);
            queryTransactId(COMPANION_DEVICE_DESCRIPTOR,Transacts.addOnMessageReceivedListener,descriptorTransacts);
            queryTransactId(COMPANION_DEVICE_DESCRIPTOR,Transacts.removeOnTransportsChangedListener,descriptorTransacts);
            queryTransactId(COMPANION_DEVICE_DESCRIPTOR,Transacts.addOnTransportsChangedListener,descriptorTransacts);
            queryTransactId(COMPANION_DEVICE_DESCRIPTOR,Transacts.sendMessage,descriptorTransacts);
            queryTransactId(MEDIA_ROUTER_DESCRIPTOR,Transacts.registerManager,descriptorTransacts);
            queryTransactId(MEDIA_ROUTER_DESCRIPTOR,Transacts.registerProxyRouter,descriptorTransacts);
            queryTransactId(USAGE_STATS_DESCRIPTOR,Transacts.reportChooserSelection,descriptorTransacts);
            queryTransactId(CONTEXTUAL_SEARCH_DESCRIPTOR,Transacts.startContextualSearch,descriptorTransacts);
            queryTransactId(INPUT_DESCRIPTOR,Transacts.registerStickyModifierStateListener,descriptorTransacts);
            queryTransactId(INPUT_DESCRIPTOR,Transacts.unregisterStickyModifierStateListener,descriptorTransacts);
            queryTransactId(ON_DEVICE_INTELLINGENCE_DESCRIPTOR,Transacts.getVersion,descriptorTransacts);
            queryTransactId(ON_DEVICE_INTELLINGENCE_DESCRIPTOR,Transacts.getFeature,descriptorTransacts);
            queryTransactId(DISPLAY_DESCRIPTOR,Transacts.enableConnectedDisplay,descriptorTransacts);
            queryTransactId(BACKGROUND_INSTALL_CONTROL_DESCRIPTOR,Transacts.getBackgroundInstalledPackages,descriptorTransacts);
            queryTransactId(GRAMMATICAL_INFLECTION_DESCRIPTOR,Transacts.getSystemGrammaticalGender,descriptorTransacts);
            queryTransactId(DISPLAY_DESCRIPTOR,Transacts.requestDisplayModes,descriptorTransacts);
            queryTransactId(FILE_INTEGRITY_DESCRIPTOR,Transacts.setupFsverity,descriptorTransacts);
            queryTransactId(FILE_INTEGRITY_DESCRIPTOR,Transacts.createAuthToken,descriptorTransacts);
            queryTransactId(DEVICE_POLICY_DESCRIPTOR,Transacts.isDevicePotentiallyStolen,descriptorTransacts);
            queryTransactId(ACTIVITY_DESCRIPTOR,Transacts.getBindingUidProcessState,descriptorTransacts);
            queryTransactId(NSD_DESCRIPTOR,Transacts.connect,descriptorTransacts);
            queryTransactId(FEATURE_FLAGS_DESCRIPTOR,Transacts.resetFlag,descriptorTransacts);
            queryTransactId(FEATURE_FLAGS_DESCRIPTOR,Transacts.overrideFlag,descriptorTransacts);
            queryTransactId(POWER_DESCRIPTOR,Transacts.isWakeLockLevelSupported,descriptorTransacts);
            queryTransactId(PACKAGE_DESCRIPTOR,Transacts.setPackagesSuspendedAsUser,descriptorTransacts);

            writeTransactsJsonFile(descriptorTransacts);

            return writeTransactsSourceFile(descriptorTransacts);

        }

        /**
         * Uses reflection to query the {@code descriptor}'s Stub class for the specified {@code
         * transactName}; the {@code transactName} is mapped to the resulting ID in the {@link Map}
         * under the {@code descriptor} in the provided {@code descriptorTransacts}.
         */
        private void queryTransactId(String descriptor, String transactName,
                Map<String, Map<String, String>> descriptorTransacts) {
            Map<String, String> transactIds = descriptorTransacts.get(descriptor);
            if (transactIds == null) {
                transactIds = new HashMap<>();
            }
            try {
                Class serviceProxy = Class.forName(descriptor + "$Stub");
                Field transactField = serviceProxy.getDeclaredField(TRANSACT_PREFIX + transactName);
                transactField.setAccessible(true);
                int transactId = (int) transactField.get(null);
                transactIds.put(transactName, String.valueOf(transactId));
            } catch (ReflectiveOperationException e) {
                // Exceptions can be expected when this tool is run on a device at an API level that
                // does not support a service / transact method being queried; the Permission Test
                // Tool will use the appropriate transact based on the API level. However the
                // exception is included here so that it can be logged in the resulting java source
                // below for debugging purposes.
                transactIds.put(transactName, e.getMessage());
            }
            descriptorTransacts.put(descriptor, transactIds);
        }


        private String writeTransactsJsonFile(Map<String, Map<String, String>> descriptorTransact){

            Map<String,Object> source = new HashMap<>();

            try {
                OutputStreamWriter writer = new OutputStreamWriter(mContext.openFileOutput(
                        sClassName + ".json", Context.MODE_PRIVATE));

                Map<String, String> serviceNameMap = new HashMap<>();
                descriptorTransact.keySet().forEach(descriptor -> {
                    String serviceNameSynonym = sDescriptorNames.get(descriptor);
                    Log.d(TAG,">"+serviceNameSynonym+":"+descriptor);
                    Field f = null;
                    try {
                        f = Transacts.class.getField(serviceNameSynonym);
                    } catch (NoSuchFieldException ignored) {}
                    if(!Objects.isNull(f)){
                        try {
                            String item = (String)f.get(null);//static
                            serviceNameMap.put(item,item);
                            serviceNameMap.put(serviceNameSynonym,item);
                        } catch (IllegalAccessException e) {}
                    }
                });
                source.put("services",serviceNameMap);
                //construct method maps
                Map<String, Map<String,Integer>> methodMap = new HashMap<>();
                descriptorTransact.keySet().forEach(descriptor -> {
                    Map<String,Integer> mInner = new HashMap<>();
                    descriptorTransact.get(descriptor).keySet().forEach(key -> {
                        String id = descriptorTransact.get(descriptor).get(key);
                        try {
                            Integer integer = Integer.valueOf(id);
                            mInner.put(key,integer);
                        } catch (NumberFormatException ex){
                            Log.d("TAG","NumberFormatException:"+key+"+"+id);
                        }
                    });
                    methodMap.put(descriptor,mInner);
                });
                source.put("methods",methodMap);
                ObjectMapper mapper = new ObjectMapper();
                mapper.writerWithDefaultPrettyPrinter();
                mapper.writeValue(writer,source);
                //jacksonObjectMapper.writeValue(writer,source);
                //use jacksonObjectMapper to change the map to json


                //writer.write();
                writer.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return "";
        }

        /**
         * Writes the mapping of descriptor names to transact method names to IDs from the provided
         * {@code descriptorTransacts} to a java file in the app's data directory; this file can be
         * copied to the Permission Test Tool's source and used to verify the signature permissions
         * that require direct binder transacts.
         */
        private String writeTransactsSourceFile(
                Map<String, Map<String, String>> descriptorTransacts) {
            try {
                OutputStreamWriter writer = new OutputStreamWriter(mContext.openFileOutput(
                        sClassName + ".java", Context.MODE_PRIVATE));
                writer.write(
                        "package com.android.certifications.niap.permissions.utils;" + NL + NL);
                writer.write("import java.util.HashMap;" + NL);
                writer.write("import java.util.Map;" + NL + NL);
                writer.write("public class " + sClassName + " extends Transacts {" + NL);
                writer.write("    public " + sClassName + "() {" + NL);
                writer.write("        mDeviceApiLevel = " + ACTUAL_SDK_INT + ";" + NL);
                writer.write("        Map<String, Integer> transactIds;" + NL);

                Map<String, String> transactIds;
                for (String descriptor : descriptorTransacts.keySet()) {
                    transactIds = descriptorTransacts.get(descriptor);
                    writer.write(NL + "        transactIds = new HashMap<>();" + NL);
                    for (String transact : transactIds.keySet()) {
                        String transactId = transactIds.get(transact);
                        try {
                            writer.write(
                                    "        transactIds.put(" + transact + ", " + Integer.valueOf(
                                            transactId) + ");" + NL);
                        } catch (NumberFormatException e) {
                            writer.write("        // Failed to obtain transactId of " + transact
                                    + " under "
                                    + descriptor + ": " + transactId + NL);
                        }
                    }
                    writer.write(
                            "        mDescriptorTransacts.put(" + sDescriptorNames.get(descriptor)
                                    + ", transactIds);" + NL);
                }
                writer.write("    }" + NL);
                writer.write("}" + NL);
                writer.close();
                String fileName = mContext.getFilesDir() + "/" + sClassName + ".java";
                Log.d(TAG, "Completed write of java source to file: " + fileName);
                return fileName;
            } catch (IOException e) {
                Log.e(TAG, "Caught an IOException writing the file: ", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String fileName) {
            if (fileName != null) {
                mStatusTextView.setText(mContext.getString(R.string.query_complete, fileName));
            } else {
                mStatusTextView.setText(mContext.getString(R.string.query_failed));
            }
            mQueryButton.setEnabled(true);
        }
    }
}
