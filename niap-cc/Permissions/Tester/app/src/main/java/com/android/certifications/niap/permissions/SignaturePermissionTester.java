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

package com.android.certifications.niap.permissions;

import static android.Manifest.permission.QUERY_ALL_PACKAGES;
import static android.Manifest.permission.REQUEST_COMPANION_PROFILE_NEARBY_DEVICE_STREAMING;
import static android.Manifest.permission.REQUEST_COMPANION_PROFILE_WATCH;
import static android.Manifest.permission.SCHEDULE_EXACT_ALARM;
import static android.Manifest.permission.USE_EXACT_ALARM;
import static android.os.PowerManager.FULL_WAKE_LOCK;
import static android.os.PowerManager.PARTIAL_WAKE_LOCK;
import static android.os.PowerManager.SCREEN_DIM_WAKE_LOCK;
import static android.security.keystore.KeyProperties.DIGEST_NONE;
import static android.security.keystore.KeyProperties.DIGEST_SHA256;
import static android.security.keystore.KeyProperties.DIGEST_SHA512;
import static android.security.keystore.KeyProperties.KEY_ALGORITHM_EC;
import static android.security.keystore.KeyProperties.PURPOSE_SIGN;
import static com.android.certifications.niap.permissions.utils.ReflectionUtils.invokeReflectionCall;
import static com.android.certifications.niap.permissions.utils.ReflectionUtils.stubFromInterface;
import static com.android.certifications.niap.permissions.utils.ReflectionUtils.stubRemoteCallback;
import static com.android.certifications.niap.permissions.utils.SignaturePermissions.permission;

import android.Manifest;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.BroadcastOptions;
import android.app.Instrumentation;
import android.app.KeyguardManager;
import android.app.LocaleConfig;
import android.app.LocaleManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.UiModeManager;
import android.app.WallpaperManager;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.app.ondeviceintelligence.Feature;
import android.app.ondeviceintelligence.IFeatureCallback;
import android.app.role.IOnRoleHoldersChangedListener;
import android.app.role.RoleManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.companion.AssociationRequest;
import android.companion.CompanionDeviceManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.IOnPermissionsChangeListener;
import android.content.pm.LauncherApps;
import android.content.pm.LauncherApps.ShortcutQuery;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageInstaller.SessionParams;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.flags.SyncableFlag;
import android.hardware.biometrics.BiometricPrompt;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.display.DisplayManager;
import android.hardware.display.HdrConversionMode;
import android.hardware.input.IStickyModifierStateListener;
import android.hardware.usb.UsbManager;
import android.health.connect.aidl.IMigrationCallback;
import android.health.connect.migration.MigrationException;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.IMediaRouter2Manager;
import android.media.MediaRecorder;
import android.media.MediaRoute2Info;
import android.media.RouteDiscoveryPreference;
import android.media.RouteListingPreference;
import android.media.RoutingSessionInfo;
import android.media.session.MediaSessionManager;
import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.IpConfiguration;
import android.net.MacAddress;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.Uri;
import android.net.VpnService;
import android.net.nsd.DiscoveryRequest;
import android.net.nsd.INsdManagerCallback;
import android.net.nsd.INsdServiceConnector;
import android.net.nsd.IOffloadEngine;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.nsd.OffloadEngine;
import android.net.nsd.OffloadServiceInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.DropBoxManager;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.IInterface;
import android.os.LocaleList;
import android.os.Looper;
import android.os.OutcomeReceiver;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.VibratorManager;
import android.os.WorkSource;
import android.os.storage.StorageManager;
import android.print.PrintManager;
import android.provider.BlockedNumberContract.BlockedNumbers;
import android.provider.CallLog.Calls;
import android.provider.E2eeContactKeysManager;
import android.provider.Settings;
import android.provider.Telephony;
import android.provider.Telephony.Carriers;
import android.provider.VoicemailContract.Voicemails;
import android.se.omapi.Reader;
import android.se.omapi.SEService;
import android.security.FileIntegrityManager;
import android.security.IKeyChainService;
import android.security.KeyChain;
import android.security.keystore.KeyGenParameterSpec;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.CellInfo;
import android.telephony.NetworkScanRequest;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.SignalStrengthUpdateRequest;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.TelephonyScanManager;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.SurfaceControl;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.CaptioningManager;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.credentials.CreateCredentialRequest;
import androidx.credentials.provider.ProviderCreateCredentialRequest;
import androidx.preference.PreferenceManager;
import androidx.test.platform.app.InstrumentationRegistry;

import com.android.certifications.niap.permissions.activities.LogListAdaptable;
import com.android.certifications.niap.permissions.activities.MainActivity;
import com.android.certifications.niap.permissions.activities.TestActivity;
import com.android.certifications.niap.permissions.companion.services.TestBindService;
import com.android.certifications.niap.permissions.config.TestConfiguration;
import com.android.certifications.niap.permissions.log.Logger;
import com.android.certifications.niap.permissions.log.LoggerFactory;
import com.android.certifications.niap.permissions.log.UiLogger;
import com.android.certifications.niap.permissions.services.TestService;
import com.android.certifications.niap.permissions.utils.InternalPermissions;
import com.android.certifications.niap.permissions.utils.ReflectionUtils;
import com.android.certifications.niap.permissions.utils.SignaturePermissions;
import com.android.certifications.niap.permissions.utils.TesterUtils;
import com.android.certifications.niap.permissions.utils.Transacts;
import com.android.internal.policy.IKeyguardDismissCallback;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.spec.ECGenParameterSpec;
import java.sql.Ref;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
import java.util.stream.Collectors;

/**
 * Permission tester to verify all platform declared signature permissions properly guard their API,
 * resources, etc. To be granted a platform declared signature permission an app must be signed with
 * the same signing key as the platform. There is also a protection flag of privileged for some of
 * the signature permissions; this privileged protection flag indicates that the permission can also
 * be granted to a priv-app preloaded on the device. This class declares tests for all platform
 * declared signature permissions, and, by default, will run all tests. However {@code
 * Constants#USE_PRIVILEGED_PERMISSION_TESTER} can be set to {@code true} to run the tests for
 * privileged permissions in the {@link PrivilegedPermissionTester} class which would require an
 * app be preloaded as a priv-app to verify the case where the permission is granted.
 */
public class SignaturePermissionTester extends BasePermissionTester {
    private static final String TAG = "SignaturePermissionTester";
    private final Logger mLogger = LoggerFactory.createActivityLogger(TAG,(LogListAdaptable) mActivity);

    protected final BluetoothAdapter mBluetoothAdapter;
    protected final BluetoothManager mBluetoothManager;
    protected final ConnectivityManager mConnectivityManager;
    protected final WifiManager mWifiManager;
    protected final KeyguardManager mKeyguardManager;
    protected final ActivityManager mActivityManager;
    protected final DevicePolicyManager mDevicePolicyManager;
    protected final PowerManager mPowerManager;
    protected final TelephonyManager mTelephonyManager;
    protected final TelecomManager mTelecomManager;
    protected final LocationManager mLocationManager;
    protected final CameraManager mCameraManager;
    protected final DisplayManager mDisplayManager;
    protected final UsbManager mUsbManager;
    protected final LauncherApps mLauncherApps;
    protected final StorageManager mStorageManager;
    protected final UsageStatsManager mUsageStatsManager;
    protected final UserManager mUserManager;
    protected final AppOpsManager mAppOpsManager;
    protected final PrintManager mPrintManager;
    protected final AccessibilityManager mAccessibilityManager;
    protected final LocaleManager mLocaleManager;
    protected CaptioningManager mCaptioningManager;
    protected final AudioManager mAudioManager;


    protected final List<String> mSignaturePermissions;
    protected final Set<String> mPrivilegedPermissions;
    protected final Set<String> mDevelopmentPermissions;



    /**
     * Map of permissions that can only be held by platform signed apps to their corresponding
     * PermissionTesters.
     */
    protected final Map<String, PermissionTest> mPermissionTasks;
    @SuppressLint({"WrongConstant", "SuspiciousIndentation"})
    public SignaturePermissionTester(TestConfiguration configuration, Activity activity) {
        super(configuration, activity);

        mSignaturePermissions = SignaturePermissions.getSignaturePermissions(mContext);
        mPrivilegedPermissions = SignaturePermissions.getPrivilegedPermissions(mContext);
        mDevelopmentPermissions = SignaturePermissions.getDevelopmentPermissions(mContext);

        mConnectivityManager = (ConnectivityManager) mContext.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        mKeyguardManager = (KeyguardManager) mContext.getSystemService(Context.KEYGUARD_SERVICE);
        mActivityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        mDevicePolicyManager = (DevicePolicyManager) mContext.getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        mPowerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        mTelephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        mTelecomManager = (TelecomManager) mContext.getSystemService(Context.TELECOM_SERVICE);
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mCameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        mDisplayManager = (DisplayManager) mContext.getSystemService(Context.DISPLAY_SERVICE);
        mUsbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        mLauncherApps = (LauncherApps) mContext.getSystemService(Context.LAUNCHER_APPS_SERVICE);
        mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        mUsageStatsManager = (UsageStatsManager) mContext.getSystemService(
                Context.USAGE_STATS_SERVICE);
        mUserManager = (UserManager) mContext.getSystemService(Context.USER_SERVICE);
        mAppOpsManager = (AppOpsManager) mContext.getSystemService(Context.APP_OPS_SERVICE);
        mPrintManager = (PrintManager) mContext.getSystemService(Context.PRINT_SERVICE);
        mAccessibilityManager = (AccessibilityManager) mContext.getSystemService(
                Context.ACCESSIBILITY_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mLocaleManager = (LocaleManager) mContext.getSystemService(Context.LOCALE_SERVICE);
            mCaptioningManager = (CaptioningManager) mContext.getSystemService(Context.CAPTIONING_SERVICE);
        } else {
            mLocaleManager = null;
        }
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

        mPermissionTasks = new HashMap<>();

        SharedPreferences sp =
                PreferenceManager.getDefaultSharedPreferences(mContext);

        if (sp.getBoolean("cb_sig01", false)) prepareTestBlock01();
        if (sp.getBoolean("cb_sig02", false)) prepareTestBlock02();
        if (sp.getBoolean("cb_sig10", false)) prepareTestBlockForQPR();
        if (sp.getBoolean("cb_sig12", false)) prepareTestBlockForS();
        if (sp.getBoolean("cb_sig13", false)) prepareTestBlockForT();
        if (sp.getBoolean("cb_sig14", false)) prepareTestBlockForU();
        if (sp.getBoolean("cb_sig15", false)) prepareTestBlockForV();

        if (sp.getBoolean("cb_sig_bind", false)) prepareTestBlockBind();

        //prepareTestBlockTemp();

    }


    private void prepareTestBlock01()
    {
        mPermissionTasks.put(permission.ACCESS_AMBIENT_LIGHT_STATS,
                new PermissionTest(false, () -> {
                    invokeReflectionCall(mDisplayManager.getClass(), "getAmbientBrightnessStats",
                            mDisplayManager, null);
                }));

        mPermissionTasks.put(permission.ACCESS_CACHE_FILESYSTEM,
                new PermissionTest(false, () -> {
                    try {
                        File f = new File(Environment.getDownloadCacheDirectory(),
                                "test_access_cache_filesystem.out");
                        if(f.createNewFile()){
                            mLogger.logInfo("test_access_cache_filesystem.out generated.");
                        }
                    } catch (IOException e) {
                        // If an app does not have this permission then an IOException will be
                        // thrown with "Permission denied" in the message.
                        if (Objects.requireNonNull(e.getMessage()).contains("Permission denied")) {
                            throw new SecurityException(e);
                        } else {
                            throw new UnexpectedPermissionTestFailureException(e);
                        }
                    }
                }));

        mPermissionTasks.put(permission.ACCESS_CONTENT_PROVIDERS_EXTERNALLY,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
                            Transacts.getContentProviderExternal, "settings", 0,
                            getActivityToken(), TAG);
                }));

        mPermissionTasks.put(permission.ACCESS_INSTANT_APPS,
                new PermissionTest(false, () -> {
                    invokeReflectionCall(mPackageManager.getClass(), "getInstantApps",
                            mPackageManager, null);
                }));

        mPermissionTasks.put(permission.ACCESS_KEYGUARD_SECURE_STORAGE,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.TRUST_SERVICE, Transacts.TRUST_DESCRIPTOR,
                            Transacts.reportEnabledTrustAgentsChanged, 0);
                }));

        // android.permissionACCESS_MOCK_LOCATION has a corresponding appop check that will fail
        // first before the the permission check is performed.

        mPermissionTasks.put(permission.ACCESS_MTP, new PermissionTest(false, () -> {
            mTransacts.invokeTransact(Transacts.USB_SERVICE, Transacts.USB_DESCRIPTOR,
                    Transacts.getControlFd, 0L);
        }));

        // android.permission.ACCESS_NETWORK_CONDITIONS guards network condition broadcasts

        mPermissionTasks.put(permission.ACCESS_NOTIFICATIONS,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.NOTIFICATION_SERVICE,
                            Transacts.NOTIFICATION_DESCRIPTOR, Transacts.getActiveNotifications,
                            mPackageName);
                }));

        // android.permission.ACCESS_PDB_STATE - SELinux policy blocks access to Persistent Data
        // Block service.

        mPermissionTasks.put(permission.ACCESS_SHORTCUTS, new PermissionTest(false, () -> {
            mLauncherApps.hasShortcutHostPermission();
            int queryFlags = ShortcutQuery.FLAG_MATCH_DYNAMIC
                    | ShortcutQuery.FLAG_MATCH_PINNED_BY_ANY_LAUNCHER;
            mLauncherApps.getShortcuts(new ShortcutQuery().setQueryFlags(queryFlags),
                    Process.myUserHandle());
        }));

        mPermissionTasks.put(permission.ACCESS_SURFACE_FLINGER,
                new PermissionTest(false, () -> {
                    // SurfaceFlinger.cpp CheckTransactCodeCredentials should check this;
                    // BOOT_FINISHED is set to FIRST_CALL_TRANSACTION since it is anticipated to be
                    // called from the ActivityManagerService.
                    mTransacts.invokeTransact(
                            Transacts.SURFACE_FLINGER_SERVICE,
                            Transacts.SURFACE_FLINGER_DESCRIPTOR,
                            Transacts.showCpu);
                }));

        mPermissionTasks.put(permission.ACCESS_VOICE_INTERACTION_SERVICE,
                new PermissionTest(false, () -> {
                    if (mDeviceApiLevel < Build.VERSION_CODES.S) {
                        // As of Android 12 this permission is no longer required to invoke this
                        // transact.
                        mTransacts.invokeTransact(Transacts.VOICE_INTERACTION_SERVICE,
                                Transacts.VOICE_INTERACTION_DESCRIPTOR,
                                Transacts.getActiveServiceComponentName);
                    } else {
                        mTransacts.invokeTransact(Transacts.VOICE_INTERACTION_SERVICE,
                                Transacts.VOICE_INTERACTION_DESCRIPTOR, Transacts.isSessionRunning);
                    }
                }));

        mPermissionTasks.put(permission.ACCESS_VR_MANAGER,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.VR_SERVICE, Transacts.VR_DESCRIPTOR,
                            Transacts.setStandbyEnabled, true);
                }));

        mPermissionTasks.put(permission.ACCESS_VR_STATE, new PermissionTest(false, () -> {
            mTransacts.invokeTransact(Transacts.VR_SERVICE, Transacts.VR_DESCRIPTOR,
                    Transacts.getVrModeState);
        }));

        mPermissionTasks.put(permission.ALLOCATE_AGGRESSIVE,
                new PermissionTest(false, () -> {
                    UUID storageUUID;
                    try {
                        storageUUID = mStorageManager.getUuidForPath(
                                Environment.getDataDirectory());
                    } catch (IOException e) {
                        throw new UnexpectedPermissionTestFailureException(e);
                    }
                    invokeReflectionCall(mStorageManager.getClass(), "getAllocatableBytes",
                            mStorageManager, new Class[]{UUID.class, int.class}, storageUUID, 1);
                }));

        mPermissionTasks.put(permission.BACKUP, new PermissionTest(false, () -> {
            // This test will be skipped if the backup service is disabled; if this happens check
            // for the following touch file on the device:
            // /data/backup/backup-suppress
            mTransacts.invokeTransact(Transacts.BACKUP_SERVICE, Transacts.BACKUP_DESCRIPTOR,
                    Transacts.setBackupEnabled, true);
        }));

        mPermissionTasks.put(permission.BATTERY_STATS, new PermissionTest(false, () -> {
            mTransacts.invokeTransact(Transacts.BATTERY_STATS_SERVICE, Transacts.BATTERY_STATS_DESCRIPTOR,
                    Transacts.getAwakeTimeBattery);
        }));

        mPermissionTasks.put(permission.BRIGHTNESS_SLIDER_USAGE,
                new PermissionTest(false, () -> {
                    invokeReflectionCall(mDisplayManager.getClass(), "getBrightnessEvents",
                            mDisplayManager, null);
                }));

        // android.permission.BROADCAST_PACKAGE_REMOVED - all broadcasts protected by a platform
        // signed signature permission first check if the caller is a system UID; if not the call
        // fails before the permission check is performed.

        // Starting in Android 10 a permission failure is logged but a SecurityException is not
        // thrown, so there is no way to verify the API.
        mPermissionTasks.put(permission.CAMERA_SEND_SYSTEM_EVENTS,
                new PermissionTest(false, Build.VERSION_CODES.P, Build.VERSION_CODES.P, () -> {
                    mTransacts.invokeTransact(Transacts.CAMERA_SERVICE, Transacts.CAMERA_DESCRIPTOR,
                            Transacts.notifySystemEvent, 0, new int[]{});
                }));

        /*OK1*/

        mPermissionTasks.put(permission.CAPTURE_AUDIO_OUTPUT, new PermissionTest(false, () -> {

            MediaRecorder recorder;
            try {
                recorder = new MediaRecorder();
                recorder.setAudioSource(MediaRecorder.AudioSource.REMOTE_SUBMIX);
                String fileName = mContext.getFilesDir() + "/test_capture_audio_output.3gpp";
                recorder.setOutputFile(new File(fileName));
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                recorder.prepare();
                recorder.start();
            } catch (RuntimeException e) {
                // The call to start was observed to fail with a RuntimeException with
                // the following:
                // ServiceManager: Permission failure:
                // android.permission.CAPTURE_AUDIO_OUTPUT from uid=10167 pid=24184
                throw new SecurityException(e);
            } catch (IOException ioe) {
                // An IOException indicates that the permission check was passed and the
                // API should be considered as being successfully invoked.
                mLogger.logDebug("Caught an IOException: ", ioe);
                return;
            }
            // If the call to start has been passed then the permission check was
            // successful and any failures in stop can still be treated as a successful
            // API invocation.
            try {
                Thread.sleep(1000);//need wait a while
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            recorder.stop();

        }));

        mPermissionTasks.put(permission.CAPTURE_SECURE_VIDEO_OUTPUT,
                new PermissionTest(false, () -> {
                    mDisplayManager.createVirtualDisplay(TAG, 10, 10, 1, null,
                            DisplayManager.VIRTUAL_DISPLAY_FLAG_SECURE);
                }));

        mPermissionTasks.put(permission.CAPTURE_VIDEO_OUTPUT,
                new PermissionTest(false, () -> {
                    mDisplayManager.createVirtualDisplay(TAG, 10, 10, 1, null,
                            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR);
                }));

        mPermissionTasks.put(permission.CHANGE_APP_IDLE_STATE,
                new PermissionTest(false, () -> {
                    invokeReflectionCall(mUsageStatsManager.getClass(), "setAppStandbyBucket",
                            mUsageStatsManager, new Class[]{String.class, int.class},
                            Constants.COMPANION_PACKAGE,
                            UsageStatsManager.STANDBY_BUCKET_ACTIVE);
                }));

        // android.permission.CHANGE_BACKGROUND_DATA_SETTING is no longer used; the method guarded
        // by this permission in ConnectivityManager is empty.

        mPermissionTasks.put(permission.CHANGE_COMPONENT_ENABLED_STATE,
                new PermissionTest(false, () -> {
                    ComponentName testComponent = new ComponentName("android",
                            "android.widget.RemoteViewsService");
                    mPackageManager.setComponentEnabledSetting(testComponent,
                            PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, 0);
                }));

        mPermissionTasks.put(permission.CHANGE_CONFIGURATION,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
                            Transacts.updateConfiguration, 0);
                }));

        // android.permission.CHANGE_OVERLAY_PACKAGES - the overlay service protected by SELinux
        // policy and can't be accessed to test this permission.

        mPermissionTasks.put(permission.CLEAR_APP_CACHE, new PermissionTest(false, () -> {
            invokeReflectionCall(mPackageManager.getClass(), "freeStorage", mPackageManager,
                    new Class<?>[]{long.class, IntentSender.class}, 100, null);
        }));

        mPermissionTasks.put(permission.CLEAR_APP_USER_DATA,
                new PermissionTest(false, () -> {

                    if(Constants.BYPASS_TESTS_AFFECTING_UI)
                        throw new BypassTestException("This test case affects to UI. skip to avoiding ui stuck.");

                    Class<?> packageDataObserverClass;
                    try {
                        packageDataObserverClass = Class
                                .forName("android.content.pm.IPackageDataObserver");
                    } catch (ReflectiveOperationException e) {
                        throw new UnexpectedPermissionTestFailureException(e);
                    }
                    invokeReflectionCall(mPackageManager.getClass(), "clearApplicationUserData",
                            mPackageManager, new Class<?>[]{String.class, packageDataObserverClass},
                            Constants.COMPANION_PACKAGE, null);
                    // After clearing the application user data sleep for a couple seconds to allow
                    // time for the operation to complete; if another operation is attempted against
                    // the companion package, such as attempting to bind to one of its test
                    // services, this could fail with the error "Package is currently frozen"
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        mLogger.logError(
                                "Caught an InterruptedException while sleeping after clearing app user data:",
                                e);
                    }
                }));

        mPermissionTasks.put(permission.CONFIGURE_DISPLAY_BRIGHTNESS,
                new PermissionTest(false, () -> {
                    invokeReflectionCall(mDisplayManager.getClass(), "getBrightnessConfiguration",
                            mDisplayManager, null);
                }));

        mPermissionTasks.put(permission.CONFIGURE_DISPLAY_COLOR_MODE,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.DISPLAY_SERVICE,
                            Transacts.DISPLAY_DESCRIPTOR,
                            Transacts.requestColorMode, 0, 0);
                }));

        mPermissionTasks.put(permission.CONFIGURE_WIFI_DISPLAY,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.DISPLAY_SERVICE,
                            Transacts.DISPLAY_DESCRIPTOR,
                            Transacts.startWifiDisplayScan);
                }));

        mPermissionTasks.put(permission.CONFIRM_FULL_BACKUP,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
                            Transacts.bindBackupAgent, mPackageName, 1, mUid);
                }));

        mPermissionTasks.put(permission.CONNECTIVITY_INTERNAL,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.CONNECTIVITY_SERVICE,
                            Transacts.CONNECTIVITY_DESCRIPTOR,
                            Transacts.getActiveNetworkForUid, mUid, false);
                }));

        // Note,
        mPermissionTasks.put(permission.CONNECTIVITY_USE_RESTRICTED_NETWORKS,
                new PermissionTest(false, () -> {
                    // Note, the platform will first check for CONNECTIVITY_USE_RESTRICTED_NETWORKS,
                    // and if that fails then it will check for CONNECTIVITY_INTERNAL which is also
                    // a signature permission. CONNECTIVITY_INTERNAL is being phased out, but this
                    // is why SecurityException messages may reference CONNECTIVITY_INTERNAL
                    // instead of the permission under test.

                    // Prior to Android 12 the NetworkRequest could be modified via reflection to
                    // request restricted networks.
                    if (mDeviceApiLevel < Build.VERSION_CODES.R) {
                        try {
                            NetworkRequest.Builder networkRequestBuilder =
                                    new NetworkRequest.Builder()
                                            .addCapability(
                                                    NetworkCapabilities.NET_CAPABILITY_INTERNET);
                            NetworkRequest networkRequest = networkRequestBuilder.build();
                            Field capabilitiesField = networkRequest.getClass().getField(
                                    "networkCapabilities");
                            NetworkCapabilities capabilities =
                                    (NetworkCapabilities) capabilitiesField
                                            .get(networkRequest);
                            //The field access throw exception as of SDK 33.
                            @SuppressLint("SoonBlockedPrivateApi") Field capabilitiesValueField =
                                    capabilities.getClass().getDeclaredField("mNetworkCapabilities");

                            capabilitiesValueField.setAccessible(true);
                            long capabilitiesValue = (long) capabilitiesValueField.get(
                                    capabilities);
                            capabilitiesValueField.set(capabilities, capabilitiesValue & ~(1
                                    << NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED));
                            mConnectivityManager.requestNetwork(networkRequest,
                                    new NetworkCallback() {
                                        @Override
                                        public void onAvailable(Network network) {
                                            mLogger.logDebug(
                                                    "onAvailable called with network " + network);
                                        }
                                    });
                        } catch (ReflectiveOperationException e) {
                            throw new UnexpectedPermissionTestFailureException(e);
                        }
                    } else  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        // The following are the default NetworkCapabilities without
                        // NET_CAPABILITY_NOT_RESTRICTED set.
                        Class<?> ncBuilderClazz = null;
                        try {
                            ncBuilderClazz = Class.forName("android.net.NetworkCapabilities$Builder");

                            Object ncBuilderObj = invokeReflectionCall(ncBuilderClazz,
                                    "withoutDefaultCapabilities", null,
                                    new Class<?>[]{});
                            ncBuilderObj = invokeReflectionCall(ncBuilderClazz,
                                    "addCapability", ncBuilderObj,
                                    new Class<?>[]{int.class},NetworkCapabilities.NET_CAPABILITY_TRUSTED);
                            ncBuilderObj = invokeReflectionCall(ncBuilderClazz,
                                    "addCapability", ncBuilderObj,
                                    new Class<?>[]{int.class},NetworkCapabilities.NET_CAPABILITY_NOT_VPN);

                            NetworkCapabilities nc  = (NetworkCapabilities) invokeReflectionCall(ncBuilderClazz,
                                    "build", ncBuilderObj,
                                    new Class<?>[]{});

                            Intent intent = new Intent(mContext, TestActivity.class);
                            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent,
                                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                            mTransacts.invokeTransact(Transacts.CONNECTIVITY_SERVICE,
                                    Transacts.CONNECTIVITY_DESCRIPTOR,
                                    Transacts.pendingRequestForNetwork,nc,
                                    pendingIntent, mPackageName, mContext.getAttributionTag());


                        } catch (ClassNotFoundException e) {
                            throw new UnexpectedPermissionTestFailureException(e);
                        }
                    }
                }));

        mPermissionTasks.put(permission.CONTROL_DISPLAY_BRIGHTNESS,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.DISPLAY_SERVICE,
                            Transacts.DISPLAY_DESCRIPTOR,
                            Transacts.setTemporaryAutoBrightnessAdjustment, 0.0f);

                }));

        // Starting in Android 10 the color_display service required for this test is guarded by
        // SELinux policy.
        mPermissionTasks.put(permission.CONTROL_DISPLAY_SATURATION,
                new PermissionTest(false, Build.VERSION_CODES.P, Build.VERSION_CODES.P, () -> {
                    invokeReflectionCall(mDisplayManager.getClass(), "setSaturationLevel",
                            mDisplayManager, new Class<?>[]{float.class}, 1);
                }));

        mPermissionTasks.put(permission.CONTROL_KEYGUARD, new PermissionTest(false, () -> {
            mTransacts.invokeTransact(Transacts.WINDOW_SERVICE, Transacts.WINDOW_DESCRIPTOR,
                    Transacts.dismissKeyguard, (IBinder) null);
        }));

        mPermissionTasks.put(permission.CONTROL_LOCATION_UPDATES,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.TELEPHONY_SERVICE,
                            Transacts.TELEPHONY_DESCRIPTOR,
                            Transacts.enableLocationUpdates);
                }));

        mPermissionTasks.put(permission.CONTROL_REMOTE_APP_TRANSITION_ANIMATIONS,
                new PermissionTest(false, () -> {

                    if(Constants.BYPASS_TESTS_AFFECTING_UI)
                        throw new BypassTestException("This test case affects to UI. skip to avoiding ui stuck.");


                    mTransacts.invokeTransact(Transacts.WINDOW_SERVICE, Transacts.WINDOW_DESCRIPTOR,
                            Transacts.overridePendingAppTransitionRemote, (IInterface) null);
                }));

        mPermissionTasks.put(permission.CONTROL_VPN, new PermissionTest(false, () -> {
            invokeReflectionCall(VpnService.class, "prepareAndAuthorize", null,
                    new Class<?>[]{Context.class}, mContext);
        }));

        mPermissionTasks.put(permission.CREATE_USERS, new PermissionTest(false, () -> {
            Object userInfo = invokeReflectionCall(mUserManager.getClass(), "createUser",
                    mUserManager, new Class<?>[]{String.class, int.class}, "test_user", 0);
            try {
                Field idField = userInfo.getClass().getField("id");
                int userId = (int) idField.get(userInfo);
                invokeReflectionCall(mUserManager.getClass(), "removeUser",
                        mUserManager, new Class[]{int.class}, userId);
            } catch (ReflectiveOperationException e) {
                mLogger.logDebug(
                        "Caught an exception attempting to remove the user for the CREATE_USERS "
                                + "test: ",
                        e);
            }
        }));

        mPermissionTasks.put(permission.CRYPT_KEEPER,
                new PermissionTest(false,Build.VERSION_CODES.P,Build.VERSION_CODES.S,() -> {
                    mTransacts.invokeTransact(Transacts.MOUNT_SERVICE, Transacts.MOUNT_DESCRIPTOR,
                            Transacts.getEncryptionState);
                }));

        // android.permission.DELETE_CACHE_FILES is no longer used and has been replaced by
        // android.permission.INTERNAL_DELETE_CACHE_FILES

        mPermissionTasks.put(permission.DELETE_PACKAGES, new PermissionTest(false, () -> {
            Class<?> packageDeleteObserverClass = null;
            try {
                packageDeleteObserverClass = Class
                        .forName("android.content.pm.IPackageDeleteObserver");
            } catch (ReflectiveOperationException e) {
                throw new UnexpectedPermissionTestFailureException(e);
            }
            try {
                invokeReflectionCall(mPackageManager.getClass(), "deletePackage", mPackageManager,
                        new Class<?>[]{String.class, packageDeleteObserverClass, int.class},
                        "com.example.test", null, 0);
            } catch (UnexpectedPermissionTestFailureException e) {
                // The only guaranteed package on the device that can be deleted is the companion
                // package, but that would break any subsequent tests that require connecting
                // to the services exported by the companion app. A ReflectiveOperationException
                // can be thrown due to an IllegalArgumentException since the specified package
                // does not exist on the device, but if that point is reached it indicates the
                // permission check was passed and the API invocation should be considered
                // successful.
                Throwable cause = e.getCause();
                while (cause != null) {
                    if (cause instanceof IllegalArgumentException) {
                        mLogger.logDebug("Permission check successful with following exception: ",
                                cause);
                        return;
                    }
                    cause = cause.getCause();
                }
                throw e;
            }
        }));

        mPermissionTasks.put(permission.DEVICE_POWER, new PermissionTest(false, () -> {
            if (mDeviceApiLevel == Build.VERSION_CODES.P) {
                mTransacts.invokeTransact(Transacts.POWER_SERVICE, Transacts.POWER_DESCRIPTOR,
                        Transacts.setPowerSaveMode, false);
            } else {
                invokeReflectionCall(mPowerManager.getClass(), "setPowerSaveModeEnabled",
                        mPowerManager, new Class<?>[]{boolean.class}, false);
            }

        }));

        mPermissionTasks.put(permission.DISABLE_INPUT_DEVICE,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.INPUT_SERVICE, Transacts.INPUT_DESCRIPTOR,
                            Transacts.enableInputDevice, 1);
                }));

        mPermissionTasks.put(permission.DUMP, new PermissionTest(false, () -> {
            // The stats service cannot be used for this test since it is guarded by SELinux
            // policy.
            mTransacts.invokeTransact(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
                    Transacts.requestBugReport, 0);
        }));

        // android.permission.DVB_DEVICE allows TvInputService to access DVB devices.

        // android.permission.FACTORY_TEST must be held by an app to allow it to run tests as
        // the root user when the device is running in manufacturing test mode.

        // In Android 10 the method guarded by this permission was moved to a method internal to
        // system_server, so this test should only be run on P.
        mPermissionTasks.put(permission.FILTER_EVENTS,
                new PermissionTest(false, Build.VERSION_CODES.P, Build.VERSION_CODES.P, () -> {
                    // This causes an ANR, so skip the test if the permission is granted
                    if (isPermissionGranted(permission.FILTER_EVENTS)) {
                        throw new BypassTestException(
                                "The API guarded by this permission will cause an ANR");
                    }
                    mTransacts.invokeTransact(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
                            Transacts.inputDispatchingTimedOut, 1, false, "Test FILTER_EVENTS");
                }));

        mPermissionTasks.put(permission.FORCE_BACK, new PermissionTest(false, () -> {
            // if the permission if granted then do not invoke this as it can interrupt the test
            if (isPermissionGranted(permission.FORCE_BACK)) {
                throw new BypassTestException(
                        "The API guarded by this permission will exit this activity");
            }
            mTransacts.invokeTransact(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
                    Transacts.unhandledBack);
        }));

        mPermissionTasks.put(permission.FORCE_PERSISTABLE_URI_PERMISSIONS,
                new PermissionTest(false, () -> {
                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P) {
                        mTransacts.invokeTransact(Transacts.ACTIVITY_SERVICE,
                                Transacts.ACTIVITY_DESCRIPTOR,
                                Transacts.releasePersistableUriPermission, 0,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION, mPackageName,
                                mUid);
                    } else {
                        mTransacts.invokeTransact(Transacts.URI_GRANTS_SERVICE,
                                Transacts.URI_GRANTS_DESCRIPTOR,
                                Transacts.takePersistableUriPermission, 0,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION, mPackageName,
                                mUid);
                    }
                }));

        mPermissionTasks.put(permission.FORCE_STOP_PACKAGES,
                new PermissionTest(false, () -> {
                    invokeReflectionCall(mActivityManager.getClass(), "forceStopPackage",
                            mActivityManager, new Class<?>[]{String.class}, "com.example.test");
                }));

        mPermissionTasks.put(permission.FRAME_STATS, new PermissionTest(false, () -> {
            try {
                Field tokenField = Activity.class.getDeclaredField("mToken");
                tokenField.setAccessible(true);
                IBinder token = (IBinder) tokenField.get(mActivity);
                mTransacts.invokeTransact(Transacts.WINDOW_SERVICE, Transacts.WINDOW_DESCRIPTOR,
                        Transacts.clearWindowContentFrameStats, token);

            } catch (ReflectiveOperationException e) {
                throw new UnexpectedPermissionTestFailureException(e);
            }
        }));

        mPermissionTasks.put(permission.FREEZE_SCREEN, new PermissionTest(false, () -> {
            mTransacts.invokeTransact(Transacts.WINDOW_SERVICE, Transacts.WINDOW_DESCRIPTOR,
                    Transacts.stopFreezingScreen);
        }));

        // In P this permission only limited the accounts returned from APIs, but starting in Q
        // other APIs under the UserManager were guarded by this permission.
        mPermissionTasks.put(permission.GET_ACCOUNTS_PRIVILEGED,
                new PermissionTest(false, Build.VERSION_CODES.P, () -> {
                    mUserManager.getUserName();
                }));

        mPermissionTasks.put(permission.GET_APP_GRANTED_URI_PERMISSIONS,
                new PermissionTest(false, () -> {
                    String service = Transacts.URI_GRANTS_SERVICE;
                    String descriptor = Transacts.URI_GRANTS_DESCRIPTOR;
                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P) {
                        service = Transacts.ACTIVITY_SERVICE;
                        descriptor = Transacts.ACTIVITY_DESCRIPTOR;
                    }
                    mTransacts.invokeTransact(service, descriptor, Transacts.getGrantedUriPermissions,
                            mPackageName, mUid);
                }));

        mPermissionTasks.put(permission.GET_APP_OPS_STATS,
                new PermissionTest(false, () -> {
                    if (mDeviceApiLevel < Build.VERSION_CODES.S) {
                        invokeReflectionCall(mAppOpsManager.getClass(), "getPackagesForOps",
                                mAppOpsManager, new Class<?>[]{int[].class}, new int[]{0});
                    } else {
                        // Starting in Android 12 getPackagesForOps can return the calling package
                        // without the permission.
                        mTransacts.invokeTransact(Transacts.APP_OPS_SERVICE,
                                Transacts.APP_OPS_DESCRIPTOR, Transacts.getUidOps, 1000,
                                new int[]{0});
                    }
                }));

        mPermissionTasks.put(permission.GET_INTENT_SENDER_INTENT,
                new PermissionTest(false, () -> {
                    IBinder token = getActivityToken();
                    mTransacts.invokeTransact(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
                            Transacts.getIntentForIntentSender, token);
                }));

        // android.permission.GET_PASSWORDS allows but does not guarantee access to the user
        // password at the conclusion of an add account; without that guarantee it cannot be
        // tested.

        // android.permission.GET_PROCESS_STATE_AND_OOM_SCORE guards methods within the processinfo
        // service, but this service is guarded by SELinux policy.

        mPermissionTasks.put(permission.GET_TOP_ACTIVITY_INFO,
                new PermissionTest(false, () -> {
                    String service = Transacts.ACTIVITY_TASK_SERVICE;
                    String descriptor = Transacts.ACTIVITY_TASK_DESCRIPTOR;
                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P) {
                        service = Transacts.ACTIVITY_SERVICE;
                        descriptor = Transacts.ACTIVITY_DESCRIPTOR;
                    }
                    mTransacts.invokeTransact(service, descriptor, Transacts.getAssistContextExtras, 0);
                }));

        mPermissionTasks.put(permission.HARDWARE_TEST, new PermissionTest(false, () -> {
            // The showCpu transaction should result in an invalid transaction ID; when the
            // SurfaceFlinger receives this UNKNOWN_TRANSACTION result it will check if the caller
            // has the HARDWARE_TEST permission before proceeding.
            mTransacts.invokeTransact(Transacts.SURFACE_FLINGER_SERVICE,
                    Transacts.SURFACE_FLINGER_DESCRIPTOR, Transacts.showCpu);
        }));

        // android.permission.HIDE_NON_SYSTEM_OVERLAY_WINDOWS can be used to prevent non-system
        // windows overlay windows (ie toasts) from being displayed, but if the permission is not
        // granted the flag is ignored.

        mPermissionTasks.put(permission.INSTALL_GRANT_RUNTIME_PERMISSIONS,
                new PermissionTest(false, () -> {
                    try {
                        PackageInstaller installer = mPackageManager.getPackageInstaller();
                        SessionParams params = new SessionParams(SessionParams.MODE_FULL_INSTALL);
                        invokeReflectionCall(params.getClass(), "setGrantedRuntimePermissions",
                                params,
                                new Class<?>[]{String[].class},
                                (Object) new String[]{Manifest.permission.CAMERA});
                        installer.createSession(params);
                    } catch (IOException e) {
                        throw new UnexpectedPermissionTestFailureException(e);
                    }
                }));

        mPermissionTasks.put(permission.INSTALL_PACKAGES,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.PACKAGE_SERVICE, Transacts.PACKAGE_DESCRIPTOR,
                            Transacts.installExistingPackageAsUser, "com.example.test", 0, 0, 0);
                }));

        // android.permission.INSTANT_APP_FOREGROUND_SERVICE requires an instant app.

        mPermissionTasks.put(permission.INTENT_FILTER_VERIFICATION_AGENT,
                new PermissionTest(false, () -> {
                    invokeReflectionCall(mPackageManager.getClass(), "verifyIntentFilter",
                            mPackageManager, new Class<?>[]{int.class, int.class, List.class}, 0, 0,
                            Collections.singletonList("test_intent_filter"));
                }));

        mPermissionTasks.put(permission.INTERACT_ACROSS_USERS,
                new PermissionTest(false, () -> {
                    invokeReflectionCall(mActivityManager.getClass(), "isUserRunning",
                            mActivityManager, new Class<?>[]{int.class}, 1);
                }));

        mPermissionTasks.put(permission.INTERACT_ACROSS_USERS_FULL,
                new PermissionTest(false, () -> {
                    invokeReflectionCall(mPackageManager.getClass(),
                            "getDefaultBrowserPackageNameAsUser", mPackageManager,
                            new Class<?>[]{int.class}, 1);
                }));

        mPermissionTasks.put(permission.INTERNAL_DELETE_CACHE_FILES,
                new PermissionTest(false, () -> {
                    try {
                        Class<?> packageDataObserverClass = Class
                                .forName("android.content.pm.IPackageDataObserver");
                        invokeReflectionCall(mPackageManager.getClass(),
                                "deleteApplicationCacheFiles",
                                mPackageManager,
                                new Class<?>[]{String.class, packageDataObserverClass},
                                Constants.COMPANION_PACKAGE, null);
                    } catch (ReflectiveOperationException e) {
                        throw new UnexpectedPermissionTestFailureException(e);
                    }
                }));

        mPermissionTasks.put(permission.INTERNAL_SYSTEM_WINDOW,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
                            Transacts.setHasTopUi, true);
                }));

        // android.permission.INVOKE_CARRIER_SETUP can be required by carrier setup apps to ensure
        // their setup flow can only be invoked by a system / priv-all

        mPermissionTasks.put(permission.KILL_UID, new PermissionTest(false, () -> {
            invokeReflectionCall(mActivityManager.getClass(), "killUid", mActivityManager,
                    new Class<?>[]{int.class, String.class}, 99999, "Test KILL_UID");
        }));
        /*OK2*/

        mPermissionTasks.put(permission.LOCAL_MAC_ADDRESS,
                new PermissionTest(false, () -> {
                    String macAddress = mBluetoothAdapter.getAddress();
                    // The BluetoothAdapter class indicates that the hidden DEFAULT_MAC_ADDRESS
                    // field's value will be returned to apps that do not have the LOCAL_MAC_ADDRESS
                    // permission.
                    if (macAddress.equals("02:00:00:00:00:00")) {
                        throw new SecurityException(
                                "Received the default MAC address for apps without the "
                                        + "LOCAL_MAC_ADDRESS permission");
                    }
                    mLogger.logDebug("Bluetooth address: " + macAddress);
                }));

        mPermissionTasks.put(permission.LOCATION_HARDWARE,
                new PermissionTest(false, () -> {
                    invokeReflectionCall(mLocationManager.getClass(), "flushGnssBatch",
                            mLocationManager, null);
                }));

        mPermissionTasks.put(permission.MANAGE_ACTIVITY_STACKS,
                new PermissionTest(false,Build.VERSION_CODES.P, VERSION_CODES.R, () -> {
                    //MANAGE_ACTIVITY_STACKS is a deprecated permission, we should bypass this permisson from s

                    String service = "activity_task";
                    String descriptor = Transacts.ACTIVITY_TASK_DESCRIPTOR;
                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P) {
                        service = Context.ACTIVITY_SERVICE;
                        descriptor = Transacts.ACTIVITY_DESCRIPTOR;
                    }
                    mTransacts.invokeTransact(service, descriptor, Transacts.getTaskDescription, 0);
                }));

        mPermissionTasks.put(permission.MANAGE_APP_OPS_MODES,
                new PermissionTest(false, () -> {
                    invokeReflectionCall(mAppOpsManager.getClass(), "setMode", mAppOpsManager,
                            new Class<?>[]{String.class, int.class, String.class, int.class},
                            AppOpsManager.OPSTR_CAMERA, mAppInfo.uid, mPackageName,
                            AppOpsManager.MODE_ALLOWED);

                }));

        mPermissionTasks.put(permission.MANAGE_APP_OPS_RESTRICTIONS,
                new PermissionTest(false, () -> {
                    IBinder token = getActivityToken();
                    mTransacts.invokeTransact(Transacts.APP_OPS_SERVICE,
                            Transacts.APP_OPS_DESCRIPTOR,
                            Transacts.setUserRestriction, 0, false, token, 0, 0);
                }));

        mPermissionTasks.put(permission.MANAGE_APP_TOKENS,
                new PermissionTest(false, () -> {
                    IBinder token = getActivityToken();
                    mTransacts.invokeTransact(Transacts.WINDOW_SERVICE, Transacts.WINDOW_DESCRIPTOR,
                            Transacts.removeWindowToken, token, 1);
                }));

        // android.permission.MANAGE_AUDIO_POLICY - SELinux policy reports an audioserver read
        // denied when attempting to test this permission.

        mPermissionTasks.put(permission.MANAGE_AUTO_FILL, new PermissionTest(false, () -> {
            runShellCommandTest("cmd autofill list sessions");
        }));

        // android.permission.MANAGE_BIND_INSTANT_SERVICE - all APIS guarded by this permission are
        // not accessible outside the system_server.

        mPermissionTasks.put(permission.MANAGE_CA_CERTIFICATES,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.DEVICE_POLICY_SERVICE,
                            Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.installCaCert, 0,
                            mPackageName, new byte[0]);
                }));

        // android.permission.MANAGE_CAMERA - SELinux policy reports a cameraserver read
        // denied when attempting to test this permission.

        // android.permission.MANAGE_CARRIER_OEM_UNLOCK_STATE - the oem_lock is guarded by SELinux
        // policy.

        mPermissionTasks.put(permission.MANAGE_DEVICE_ADMINS,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.PACKAGE_SERVICE, Transacts.PACKAGE_DESCRIPTOR,
                            Transacts.isPackageStateProtected, mPackageName, mUid);
                }));

        mPermissionTasks.put(permission.MANAGE_DOCUMENTS, new PermissionTest(false, () -> {
            // Without this permission this query fails with a SecurityException, but with it it
            // fails with an Unsupported Uri exception.
            mContentResolver.query(Uri.parse("content://com.android.externalstorage.documents/"),
                    null, null, null, null);
        }));

        mPermissionTasks.put(permission.MANAGE_FINGERPRINT,
                new PermissionTest(false, () -> {
                    IBinder token = getActivityToken();
                    mTransacts.invokeTransact(Transacts.FINGERPRINT_SERVICE,
                            Transacts.FINGERPRINT_DESCRIPTOR,
                            Transacts.cancelEnrollment, token);
                }));

        // android.permission.MANAGE_IPSEC_TUNNELS - the appop is checked first; this appop defaults
        // to MODE_ERROR for apps.

        mPermissionTasks.put(permission.MANAGE_MEDIA_PROJECTION,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.MEDIA_PROJECTION_SERVICE,
                            Transacts.MEDIA_PROJECTION_DESCRIPTOR,
                            Transacts.getActiveProjectionInfo);
                }));

        mPermissionTasks.put(permission.MANAGE_NETWORK_POLICY,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.NET_POLICY_SERVICE,
                            Transacts.NET_POLICY_DESCRIPTOR,
                            Transacts.getUidPolicy, mUid);
                }));

        mPermissionTasks.put(permission.MANAGE_NOTIFICATIONS,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.NOTIFICATION_SERVICE,
                            Transacts.NOTIFICATION_DESCRIPTOR,
                            Transacts.getZenRules);
                }));

        mPermissionTasks.put(permission.MANAGE_PROFILE_AND_DEVICE_OWNERS,
                new PermissionTest(false, () -> {
                    // Tests fails with a SecurityException without the permission but still
                    // fails with the permission with another exception since the device owner
                    // cannot be set if the device is already setup-up.
                    ComponentName componentName = new ComponentName(mContext, MainActivity.class);
                    if(mDeviceApiLevel>= VERSION_CODES.UPSIDE_DOWN_CAKE){
                        mTransacts.invokeTransact(Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.setDeviceOwner,
                                componentName,  mUid);

                    } else {
                        mTransacts.invokeTransact(Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.setDeviceOwner,
                                componentName, "test owner", 0);
                    }
                }));

        mPermissionTasks.put(permission.MANAGE_SENSORS, new PermissionTest(false, () -> {
            runShellCommandTest("cmd sensorservice reset-uid-state " + mPackageName);
        }));

        mPermissionTasks.put(permission.MANAGE_SLICE_PERMISSIONS,
                new PermissionTest(false, () -> {
                    Uri testUri = Uri.parse("content://" + mPackageName + "/test");
                    mTransacts.invokeTransact(Transacts.SLICE_SERVICE, Transacts.SLICE_DESCRIPTOR,
                            Transacts.grantPermissionFromUser, testUri, "android",
                            mPackageName, true);
                }));

        mPermissionTasks.put(permission.MANAGE_SOUND_TRIGGER,
                new PermissionTest(false, () -> {
                    ParcelUuid uuid = new ParcelUuid(UUID.randomUUID());
                    if (mDeviceApiLevel < Build.VERSION_CODES.S) {
                        mTransacts.invokeTransact(Transacts.SOUND_TRIGGER_SERVICE,
                                Transacts.SOUND_TRIGGER_DESCRIPTOR, Transacts.isRecognitionActive,
                                uuid);
                    } else {
                        Parcelable identity = new Parcelable() {
                            @Override
                            public int describeContents() {
                                return 0;
                            }

                            @Override
                            public void writeToParcel(Parcel parcel, int i) {
                                int start_pos = parcel.dataPosition();
                                parcel.writeInt(0);
                                parcel.writeInt(mUid); // uid
                                parcel.writeInt(Binder.getCallingPid()); // pid
                                parcel.writeString(mPackageName); // packageName
                                parcel.writeString("test-attribution"); // attributionTag
                                int end_pos = parcel.dataPosition();
                                parcel.setDataPosition(start_pos);
                                parcel.writeInt(end_pos - start_pos);
                                parcel.setDataPosition(end_pos);
                            }
                        };
                        Parcel result = mTransacts.invokeTransact(Transacts.SOUND_TRIGGER_SERVICE,
                                Transacts.SOUND_TRIGGER_DESCRIPTOR, Transacts.attachAsOriginator,
                                identity, new Binder());
                        IBinder binder = result.readStrongBinder();
                        mTransacts.invokeTransactWithCharSequence(binder,
                                Transacts.SOUND_TRIGGER_SESSION_DESCRIPTOR,
                                Transacts.getModuleProperties, false, uuid);
                    }
                }));

        mPermissionTasks.put(permission.MANAGE_SUBSCRIPTION_PLANS,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.NET_POLICY_SERVICE,
                            Transacts.NET_POLICY_DESCRIPTOR,
                            Transacts.getSubscriptionPlans, 0, mPackageName);
                }));

        mPermissionTasks.put(permission.MANAGE_USB, new PermissionTest(false, () -> {
            invokeReflectionCall(mUsbManager.getClass(), "getPorts", mUsbManager, null);
        }));

        // android.permission.MANAGE_USER_OEM_UNLOCK_STATE - also requires the oem_lock service
        // that is guarded by SELinux policy.

        mPermissionTasks.put(permission.MANAGE_USERS, new PermissionTest(false, () -> {
            mTransacts.invokeTransact(Transacts.PACKAGE_SERVICE, Transacts.PACKAGE_DESCRIPTOR,
                    Transacts.isPackageDeviceAdminOnAnyUser, mPackageName);
        }));

        mPermissionTasks.put(permission.MASTER_CLEAR, new PermissionTest(false, () -> {
            // Note, if this permission is granted this action could potentially interrupt the test
            // activity. If the test is interrupted consider skipping this test if the permission
            // is granted.
            Intent intent = new Intent(mContext, TestActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            // In Android 9 this API only accepted a PendingIntent
            if (mDeviceApiLevel == Build.VERSION_CODES.P) {
                mTransacts.invokeTransact(Transacts.EUICC_CONTROLLER_SERVICE,
                        Transacts.EUICC_CONTROLLER_DESCRIPTOR,
                        Transacts.retainSubscriptionsForFactoryReset, pendingIntent);
            } else {
                // Later Android platforms also required a cardId
                mTransacts.invokeTransact(Transacts.EUICC_CONTROLLER_SERVICE,
                        Transacts.EUICC_CONTROLLER_DESCRIPTOR,
                        Transacts.retainSubscriptionsForFactoryReset, 0, pendingIntent);
            }
        }));

        mPermissionTasks.put(permission.MEDIA_CONTENT_CONTROL,
                new PermissionTest(false, () -> {
                    MediaSessionManager mediaSessionManager =
                            (MediaSessionManager) mContext.getSystemService(
                                    Context.MEDIA_SESSION_SERVICE);
                    mediaSessionManager.getActiveSessions(
                            new ComponentName(mContext, MainActivity.class));
                }));

        mPermissionTasks.put(permission.MODIFY_ACCESSIBILITY_DATA,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.ACCESSIBILITY_SERVICE,
                            Transacts.ACCESSIBILITY_DESCRIPTOR,
                            Transacts.setPictureInPictureActionReplacingConnection, getActivityToken());
                }));

        mPermissionTasks.put(permission.MODIFY_APPWIDGET_BIND_PERMISSIONS,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.APPWIDGET_SERVICE,
                            Transacts.APPWIDGET_DESCRIPTOR,
                            Transacts.setBindAppWidgetPermission, mPackageName, 0,
                            true);
                }));

        mPermissionTasks.put(permission.MODIFY_AUDIO_ROUTING,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.AUDIO_SERVICE, Transacts.AUDIO_DESCRIPTOR,
                            Transacts.isAudioServerRunning);
                }));

        mPermissionTasks.put(permission.MODIFY_PHONE_STATE,
                new PermissionTest(false, () -> {
                    int phoneId = SubscriptionManager.getDefaultSubscriptionId();
                    if (mDeviceApiLevel < Build.VERSION_CODES.S) {
                        try {
                            invokeReflectionCall(Class.forName("com.android.ims.ImsManager"),
                                    "getInstance", null,
                                    new Class<?>[]{Context.class, int.class}, mContext, phoneId);
                        } catch (ReflectiveOperationException e) {
                            throw new UnexpectedPermissionTestFailureException(e);
                        }
                    } else {
                        if (phoneId == -1) {
                            throw new BypassTestException(
                                    "This device does not have a valid subscription on which to "
                                            + "run this test");
                        }
                        invokeReflectionCall(mTelephonyManager.getClass(), "nvReadItem",
                                mTelephonyManager, new Class[]{int.class}, 0);
                    }
                }));

        // Starting in Android 11 this test now requires a profile under the user before
        // checking the MODIFY_QUIET_MODE permission.
        mPermissionTasks.put(permission.MODIFY_QUIET_MODE,
                new PermissionTest(false, Build.VERSION_CODES.P, Build.VERSION_CODES.Q, () -> {
                    mUserManager.requestQuietModeEnabled(true,
                            UserHandle.getUserHandleForUid(mUid));
                }));

        mPermissionTasks.put(permission.MOUNT_FORMAT_FILESYSTEMS,
                new PermissionTest(false, () -> {
                    //
                    mTransacts.invokeTransact(Transacts.MOUNT_SERVICE, Transacts.MOUNT_DESCRIPTOR,
                            Transacts.benchmark, "private", (IBinder) null);
                }));
    }
    void prepareTestBlock02()
    {
        mPermissionTasks.put(permission.MOUNT_UNMOUNT_FILESYSTEMS,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.PACKAGE_SERVICE, Transacts.PACKAGE_DESCRIPTOR,
                            Transacts.getMoveStatus, 1);
                }));

        mPermissionTasks.put(permission.MOVE_PACKAGE, new PermissionTest(false, () -> {
            mTransacts.invokeTransact(Transacts.PACKAGE_SERVICE, Transacts.PACKAGE_DESCRIPTOR,
                    Transacts.movePackage,
                    "com.example.test", "test");
        }));

        mPermissionTasks.put(permission.NETWORK_SETTINGS, new PermissionTest(false, () -> {
            mTransacts.invokeTransact(Transacts.NETWORK_MANAGEMENT_SERVICE,
                    Transacts.NETWORK_MANAGEMENT_DESCRIPTOR,
                    Transacts.setDataSaverModeEnabled, false);
        }));

        mPermissionTasks.put(permission.NETWORK_STACK, new PermissionTest(false, () -> {
            mTransacts.invokeTransact(Transacts.WIFI_SERVICE, Transacts.WIFI_DESCRIPTOR,
                    Transacts.stopSoftAp);
        }));

        mPermissionTasks.put(permission.NOTIFY_PENDING_SYSTEM_UPDATE,
                new PermissionTest(false, () -> {
                    invokeReflectionCall(mDevicePolicyManager.getClass(),
                            "notifyPendingSystemUpdate", mDevicePolicyManager,
                            new Class<?>[]{long.class}, System.currentTimeMillis());
                }));

        mPermissionTasks.put(permission.OBSERVE_APP_USAGE,
                new PermissionTest(false, () -> {
                    invokeReflectionCall(mUsageStatsManager.getClass(),
                            "unregisterAppUsageObserver", mUsageStatsManager,
                            new Class<?>[]{int.class}, 1);
                }));

        mPermissionTasks.put(permission.OBSERVE_GRANT_REVOKE_PERMISSIONS,
                new PermissionTest(false, () -> {
                    IOnPermissionsChangeListener listener = new IOnPermissionsChangeListener() {
                        @Override
                        public void onPermissionsChange(int uid) {
                            mLogger.logDebug(
                                    "Received notification of a changed permission for UID " + uid);
                        }

                        @Override
                        public Binder asBinder() {
                            return null;
                        }
                    };
                    if (mDeviceApiLevel < Build.VERSION_CODES.R) {
                        mTransacts.invokeTransact(Transacts.PACKAGE_SERVICE, Transacts.PACKAGE_DESCRIPTOR,
                                Transacts.addOnPermissionsChangeListener, listener);
                    } else {
                        mTransacts.invokeTransact(Transacts.PERMISSION_MANAGER_SERVICE,
                                Transacts.PERMISSION_MANAGER_DESCRIPTOR,
                                Transacts.addOnPermissionsChangeListener, listener);
                    }
                }));

        // android.permission.OEM_UNLOCK_STATE - persistent_data_block service guarded by SELinux
        // policy.

        // android.permission.OPEN_APPLICATION_DETAILS_OPEN_BY_DEFAULT_PAGE - this permission is
        // not used, removed in Q.

        mPermissionTasks.put(permission.OVERRIDE_WIFI_CONFIG,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.WIFI_SERVICE, Transacts.WIFI_DESCRIPTOR,
                            Transacts.getWifiApConfiguration);
                }));

        mPermissionTasks.put(permission.PACKAGE_USAGE_STATS,
                new PermissionTest(false, () -> {
                    invokeReflectionCall(mActivityManager.getClass(), "getPackageImportance",
                            mActivityManager, new Class<?>[]{String.class},
                            mPackageName);
                }));

        mPermissionTasks.put(permission.PACKAGE_VERIFICATION_AGENT,
                new PermissionTest(false, () -> {
                    mPackageManager.verifyPendingInstall(0, 0);
                }));

        mPermissionTasks.put(permission.PACKET_KEEPALIVE_OFFLOAD,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.CONNECTIVITY_SERVICE,
                            Transacts.CONNECTIVITY_DESCRIPTOR,
                            Transacts.startNattKeepalive, 0, 0, 0, "127.0.0.1", 0, "127.0.0.1");
                }));

        // android.permission.PEERS_MAC_ADDRESS - method doesn't throw a SecurityException, instead
        // just returns an empty result.

        // android.permission.PROVIDE_RESOLVER_RANKER_SERVICE /
        // android.permission.PROVIDE_TRUST_AGENT - any service that does not hold either of these
        // permissions is ignored when invoking the relevant APIs.

        mPermissionTasks.put(permission.QUERY_DO_NOT_ASK_CREDENTIALS_ON_BOOT,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.DEVICE_POLICY_SERVICE,
                            Transacts.DEVICE_POLICY_DESCRIPTOR,
                            Transacts.getDoNotAskCredentialsOnBoot);
                }));

        // android.permission.QUERY_TIME_ZONE_RULES - service required for this permission is
        // guarded by SELinux policy.

        mPermissionTasks.put(permission.READ_BLOCKED_NUMBERS,
                new PermissionTest(false, () -> {
                    mContentResolver.query(BlockedNumbers.CONTENT_URI, null, null, null, null);
                }));

        mPermissionTasks.put(permission.READ_DREAM_STATE,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.DREAMS_SERVICE, Transacts.DREAMS_DESCRIPTOR,
                            Transacts.isDreaming);
                }));

        mPermissionTasks.put(permission.READ_FRAME_BUFFER,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.WINDOW_SERVICE, Transacts.WINDOW_DESCRIPTOR,
                            Transacts.screenshotWallpaper);
                }));

        // android.permission.READ_INPUT_STATE - the API that was guarded by this permission has
        // been removed.

        mPermissionTasks.put(permission.READ_LOGS, new PermissionTest(false, () -> {
            DropBoxManager manager = (DropBoxManager) mContext.getSystemService(
                    Context.DROPBOX_SERVICE);
            manager.getNextEntry(null, System.currentTimeMillis());
        }));

        mPermissionTasks.put(permission.READ_NETWORK_USAGE_HISTORY,
                new PermissionTest(false, () -> {
                    if (mDeviceApiLevel < Build.VERSION_CODES.R) {
                        mTransacts.invokeTransact(Transacts.TELEPHONY_SERVICE,
                                Transacts.TELEPHONY_DESCRIPTOR, Transacts.getVtDataUsage,
                                0, true);
                    } else {
                        mTransacts.invokeTransact(Transacts.NETWORK_STATS_SERVICE,
                                Transacts.NETWORK_STATS_DESCRIPTOR, Transacts.forceUpdate);
                    }
                }));

        // android.permission.READ_OEM_UNLOCK_STATE - oem_lock service guarded by SELinux policy.

        mPermissionTasks.put(permission.READ_PRECISE_PHONE_STATE,
                new PermissionTest(false, () -> {
                    if (Looper.myLooper() == null) {
                        Looper.prepare();
                    }
                    mTelephonyManager.listen(new PhoneStateListener(), 0x00000800);
                }));

        mPermissionTasks.put(permission.READ_PRINT_SERVICE_RECOMMENDATIONS,
                new PermissionTest(false, () -> {
                    invokeReflectionCall(mPrintManager.getClass(), "getPrintServiceRecommendations",
                            mPrintManager, null);
                }));

        mPermissionTasks.put(permission.READ_PRINT_SERVICES,
                new PermissionTest(false, () -> {
                    invokeReflectionCall(mPrintManager.getClass(), "getPrintServices",
                            mPrintManager, new Class<?>[]{int.class}, 3);
                }));

        mPermissionTasks.put(permission.READ_PRIVILEGED_PHONE_STATE,
                new PermissionTest(false, () -> {
                    invokeReflectionCall(mTelephonyManager.getClass(), "getUiccSlotsInfo",
                            mTelephonyManager, null);
                }));

        mPermissionTasks.put(permission.READ_SEARCH_INDEXABLES, new PermissionTest(false,
                () -> {
                    Cursor cursor =
                            mContentResolver.query(Uri.parse("content://com.android.settings/"), null, null,
                                    null, null);
                    cursor.close();
                }));

        // android.permission.READ_SYSTEM_UPDATE_INFO - the system_update service is guarded by
        // SELinux policy

        // android.permission.READ_WALLPAPER_INTERNAL grants access to platform signed or priv-apps
        // to read the wallpaper settings without requiring READ_EXTERNAL_STORAGE.

        mPermissionTasks.put(permission.READ_WIFI_CREDENTIAL,
                new PermissionTest(false, () -> {
                    invokeReflectionCall(mWifiManager.getClass(), "getPrivilegedConfiguredNetworks",
                            mWifiManager, null);
                }));

        //TODO : Reboot the device?
        mPermissionTasks.put(permission.REBOOT, new PermissionTest(false, () -> {
            if (isPermissionGranted(permission.REBOOT)) {
                throw new BypassTestException(
                        "Skipping this test to avoid rebooting the device and interrupting the "
                                + "rest of the tests");
            }
            mTransacts.invokeTransact(Transacts.POWER_SERVICE, Transacts.POWER_DESCRIPTOR,
                    Transacts.reboot, false,
                    "Test REBOOT permission", false);
        }));

        // android.permission.RECEIVE_DATA_ACTIVITY_CHANGE - protects broadcast sent by
        // ConnectivityServer.

        // android.permission.RECEIVE_EMERGENCY_BROADCAST - protects broadcast sent by
        // CellBroadcastHandler.

        // android.permission.RECEIVE_MEDIA_RESOURCE_USAGE - protects broadcast sent by
        // MediaResourceMonitorService.

        // android.permission.RECEIVE_WIFI_CREDENTIAL_CHANGE - protects broadcasts sent by
        // ClientModeImpl.

        // android.permission.RECOVER_KEYSTORE - lock_settings service is guarded by SELinux
        // policy.

        // android.permission.RECOVERY - recovery service is guarded by SELinux policy.

        mPermissionTasks.put(permission.REGISTER_SIM_SUBSCRIPTION,
                new PermissionTest(false, () -> {
                    PhoneAccountHandle handle = new PhoneAccountHandle(
                            new ComponentName(mContext, TestService.class), "test_provider");
                    PhoneAccount account = PhoneAccount.builder(
                                    handle, "ConnectionService")
                            .setAddress(Uri.parse("tel:555-TEST2"))
                            .setSubscriptionAddress(Uri.parse("tel:555-TEST2"))
                            .setCapabilities(PhoneAccount.CAPABILITY_SIM_SUBSCRIPTION)
                            .addSupportedUriScheme(PhoneAccount.SCHEME_TEL)
                            .addSupportedUriScheme(PhoneAccount.SCHEME_VOICEMAIL)
                            .build();
                    mTelecomManager.registerPhoneAccount(account);
                }));

        mPermissionTasks.put(permission.REGISTER_WINDOW_MANAGER_LISTENERS,
                new PermissionTest(false, () -> {

                    mTransacts.invokeTransact(Transacts.WINDOW_SERVICE, Transacts.WINDOW_DESCRIPTOR,
                            Transacts.registerShortcutKey, 1L, (IBinder)null);

                }));

        mPermissionTasks.put(permission.REMOTE_AUDIO_PLAYBACK,
                new PermissionTest(false, () -> {
                    Parcel reply = mTransacts.invokeTransact(Transacts.AUDIO_SERVICE,
                            Transacts.AUDIO_DESCRIPTOR, Transacts.getRingtonePlayer);
                    IBinder player = reply.readStrongBinder();
                    mTransacts.invokeTransact(Transacts.AUDIO_SERVICE, Transacts.AUDIO_DESCRIPTOR,
                            Transacts.setRingtonePlayer, player);
                }));

        mPermissionTasks.put(permission.REMOVE_TASKS, new PermissionTest(false, () -> {
            mTransacts.invokeTransact(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
                    Transacts.appNotRespondingViaProvider, (IBinder) null);
        }));

        // android.permission.REQUEST_INSTALL_PACKAGES requires both the permission and the appop
        // to be granted.

        // android.permission.REQUEST_NETWORK_SCORES - network_score service guarded by
        // SELinux policy.

        mPermissionTasks.put(permission.RESET_FINGERPRINT_LOCKOUT,
                new PermissionTest(false, () -> {
                    if (mDeviceApiLevel < Build.VERSION_CODES.S) {
                        mTransacts.invokeTransact(Transacts.FINGERPRINT_SERVICE,
                                Transacts.FINGERPRINT_DESCRIPTOR,
                                Transacts.resetTimeout);
                    } else {
                        // The byte array has 70 elements to avoid an IndexOutOfBoundException
                        // in HardwareAuthTokenUtils#toHardwareAuthToken.
                        mTransacts.invokeTransact(Transacts.FINGERPRINT_SERVICE,
                                Transacts.FINGERPRINT_DESCRIPTOR, Transacts.resetLockout,
                                getActivityToken(), 0, 0, new byte[70], mPackageName);

                    }
                }));

        mPermissionTasks.put(permission.RESET_SHORTCUT_MANAGER_THROTTLING,
                new PermissionTest(false, () -> {
                    if (mDeviceApiLevel < Build.VERSION_CODES.S) {
                        mTransacts.invokeTransact(Transacts.SHORTCUT_SERVICE,
                                Transacts.SHORTCUT_DESCRIPTOR,
                                Transacts.onApplicationActive, mPackageName, mUid);
                    } else  if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        Parcel reply = mTransacts.invokeTransact(Transacts.SHORTCUT_SERVICE,
                                Transacts.SHORTCUT_DESCRIPTOR,
                                Transacts.onApplicationActive, mPackageName, mUid);
                        // The first parameter in the reply is an int indicating whether or not
                        // an AndroidFuture was written to the Parcel.
                        if (reply.readInt() != 0) {
                            // The first field in the Future is a boolean indicating whether the
                            // Future completed.
                            if (reply.readBoolean()) {
                                // If the Future completed the second and third fields are booleans
                                // indicating whether it completed with a Throwable.
                                if (reply.readBoolean() && reply.readBoolean()) {
                                    // When the Future results in an Exception an additional boolean
                                    // is written indicating whether the exception is Parcelable.
                                    if (reply.readBoolean()) {
                                        Throwable throwable = reply.readParcelable(
                                                Parcelable.class.getClassLoader());
                                        mLogger.logDebug("AndroidFuture resulted in a Throwable",
                                                throwable);
                                        if (throwable instanceof SecurityException) {
                                            throw (SecurityException) throwable;
                                        }
                                    } else {
                                        mLogger.logDebug("The received Throwable is not parcelable");
                                        String className = reply.readString();
                                        String message = reply.readString();
                                        String stackTrace = reply.readString();
                                        if (className.contains("SecurityException")) {
                                            throw new SecurityException(message + ":" + stackTrace);
                                        } else {
                                            mLogger.logDebug(
                                                    "Caught the following exception class: "
                                                            + className
                                                            + " with message: " + message
                                                            + " and stackTrace: " + stackTrace);
                                        }
                                    }
                                } else {
                                    mLogger.logDebug("The Future completed without a Throwable");
                                }
                            } else {
                                mLogger.logDebug("The future is still running");
                            }
                        } else {
                            mLogger.logDebug(
                                    "An AndroidFuture was not written to the reply Parcel");
                        }
                    }
                }));

        mPermissionTasks.put(permission.RESTRICTED_VR_ACCESS,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.VR_SERVICE, Transacts.VR_DESCRIPTOR,
                            Transacts.setPersistentVrModeEnabled, true);
                }));

        mPermissionTasks.put(permission.RETRIEVE_WINDOW_CONTENT,
                new PermissionTest(false, () -> {
                    if (isPermissionGranted(permission.RETRIEVE_WINDOW_CONTENT)) {
                        return;
                    }
                    IBinder token = getActivityToken();
                    AccessibilityServiceInfo serviceInfo = new AccessibilityServiceInfo();
                    mTransacts.invokeTransact(Transacts.ACCESSIBILITY_SERVICE,
                            Transacts.ACCESSIBILITY_DESCRIPTOR,
                            Transacts.registerUiTestAutomationService, getActivityToken(), token,
                            serviceInfo, 0);
                }));
        // USER_CURRENT_OR_SELF=-3
        mPermissionTasks.put(permission.RETRIEVE_WINDOW_TOKEN,
                new PermissionTest(false, () -> {
                    if (isPermissionGranted(permission.RETRIEVE_WINDOW_TOKEN)) {
                        return;
                    }
                    mTransacts.invokeTransact(Transacts.ACCESSIBILITY_SERVICE,
                            Transacts.ACCESSIBILITY_DESCRIPTOR,
                            Transacts.getWindowToken,  -3, mUid);
                }));

        mPermissionTasks.put(permission.REVOKE_RUNTIME_PERMISSIONS,
                new PermissionTest(false, () -> {
                    invokeReflectionCall(mPackageManager.getClass(), "revokeRuntimePermission",
                            mPackageManager,
                            new Class<?>[]{String.class, String.class, UserHandle.class},
                            Constants.COMPANION_PACKAGE, Manifest.permission.CAMERA,
                            UserHandle.getUserHandleForUid(mUid));
                }));

//         android.permission.RUN_IN_BACKGROUND - deprecated by REQUEST_COMPANION_RUN_IN_BACKGROUND

//         android.permission.SCORE_NETWORKS - network_score service guarded by SELinux
//         policy.

//         android.permission.SEND_SMS_NO_CONFIRMATION - SMSDispatcher checks for this and returns
//         true without prompting the user if it is granted.

//         android.permission.SERIAL_PORT - serial service guarded by SELinux policy.

        mPermissionTasks.put(permission.SET_ACTIVITY_WATCHER,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
                            Transacts.performIdleMaintenance);
                }));

        mPermissionTasks.put(permission.SET_ALWAYS_FINISH,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
                            Transacts.setAlwaysFinish, false);
                }));

        mPermissionTasks.put(permission.SET_ANIMATION_SCALE,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.WINDOW_SERVICE, Transacts.WINDOW_DESCRIPTOR,
                            Transacts.setAnimationScale, 0, 0.1f);
                }));

        mPermissionTasks.put(permission.SET_DEBUG_APP, new PermissionTest(false, () -> {
            mTransacts.invokeTransact(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
                    Transacts.setDumpHeapDebugLimit, mPackageName, mUid,
                    1073741824L, mPackageName);
        }));

        // android.permission.SET_DISPLAY_OFFSET is only used on wear devices.

        mPermissionTasks.put(permission.SET_HARMFUL_APP_WARNINGS,
                new PermissionTest(false, () -> {
                    // This test can potentially interrupt the test app; if so the test may need to
                    // be bypassed when the permission is granted.
                    mTransacts.invokeTransact(Transacts.PACKAGE_SERVICE, Transacts.PACKAGE_DESCRIPTOR,
                            Transacts.getHarmfulAppWarning, Constants.COMPANION_PACKAGE, mUid);
                }));

        mPermissionTasks.put(permission.SET_INPUT_CALIBRATION,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.INPUT_SERVICE, Transacts.INPUT_DESCRIPTOR,
                            Transacts.setTouchCalibrationForInputDevice, "test_device", 0, 0);
                }));

        mPermissionTasks.put(permission.SET_KEYBOARD_LAYOUT,
                new PermissionTest(false, () -> {
                    if(mDeviceApiLevel >= 35){
                        //method name changed
                        mTransacts.invokeTransact(Transacts.INPUT_SERVICE, Transacts.INPUT_DESCRIPTOR,
                                Transacts.setKeyboardLayoutForInputDevice, 0, "test_descriptor");
                    } else {
                        mTransacts.invokeTransact(Transacts.INPUT_SERVICE, Transacts.INPUT_DESCRIPTOR,
                                Transacts.addKeyboardLayoutForInputDevice, 0, "test_descriptor");
                    }
                }));

        mPermissionTasks.put(permission.SET_MEDIA_KEY_LISTENER,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.MEDIA_SESSION_SERVICE,
                            Transacts.MEDIA_SESSION_DESCRIPTOR,
                            Transacts.setOnMediaKeyListener);
                }));

        mPermissionTasks.put(permission.SET_ORIENTATION, new PermissionTest(false, () -> {
            mTransacts.invokeTransact(Transacts.WINDOW_SERVICE, Transacts.WINDOW_DESCRIPTOR,
                    Transacts.thawRotation);
        }));

        mPermissionTasks.put(permission.SET_POINTER_SPEED,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.INPUT_SERVICE, Transacts.INPUT_DESCRIPTOR,
                            Transacts.tryPointerSpeed, 7);
                }));

        mPermissionTasks.put(permission.SET_PREFERRED_APPLICATIONS,
                new PermissionTest(false, () -> {
                    // If this permission is granted then do not invoke the method; the permissions
                    // will be revoked causing the test app to immediately exit.
                    if (isPermissionGranted(permission.SET_PREFERRED_APPLICATIONS)) {
                        throw new BypassTestException(
                                "Skipping test to prevent killing test app when permissions are "
                                        + "revoked");
                    }
                    mTransacts.invokeTransact(Transacts.PACKAGE_SERVICE, Transacts.PACKAGE_DESCRIPTOR,
                            Transacts.resetApplicationPreferences, 0);
                }));

        // Starting in Android 11 an additional check is added that 'android' belongs to the
        // invoking package's uid.
        mPermissionTasks.put(permission.SET_PROCESS_LIMIT,
                new PermissionTest(false, Build.VERSION_CODES.P, Build.VERSION_CODES.Q, () -> {
                    mTransacts.invokeTransact(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
                            Transacts.setProcessLimit, 1000);
                }));

        mPermissionTasks.put(permission.SET_SCREEN_COMPATIBILITY,
                new PermissionTest(false, () -> {
                    String service = "activity_task";
                    String descriptor = Transacts.ACTIVITY_TASK_DESCRIPTOR;
                    // The ActivityManagerService provided the screen compat actions in P.
                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P) {
                        service = Context.ACTIVITY_SERVICE;
                        descriptor = Transacts.ACTIVITY_DESCRIPTOR;
                    }
                    Parcel reply = mTransacts.invokeTransact(service, descriptor,
                            Transacts.getFrontActivityScreenCompatMode, 20);
                    int mode = reply.readInt();
                    mTransacts.invokeTransact(service, descriptor,
                            Transacts.setFrontActivityScreenCompatMode, mode);
                }));

        mPermissionTasks.put(permission.SET_TIME, new PermissionTest(false, () -> {
            mTransacts.invokeTransact(Transacts.ALARM_SERVICE, Transacts.ALARM_DESCRIPTOR,
                    Transacts.setTime,
                    System.currentTimeMillis());
        }));

        mPermissionTasks.put(permission.SET_TIME_ZONE, new PermissionTest(false, () -> {
            String timeZone = TimeZone.getDefault().getID();
            mTransacts.invokeTransact(Transacts.ALARM_SERVICE, Transacts.ALARM_DESCRIPTOR,
                    Transacts.setTimeZone, timeZone);
        }));

        mPermissionTasks.put(permission.SET_VOLUME_KEY_LONG_PRESS_LISTENER,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.MEDIA_SESSION_SERVICE,
                            Transacts.MEDIA_SESSION_DESCRIPTOR,
                            Transacts.setOnVolumeKeyLongPressListener, (IBinder) null);
                }));

        mPermissionTasks.put(permission.SET_WALLPAPER_COMPONENT,
                new PermissionTest(false, () -> {
                    ComponentName name = new ComponentName(mContext, MainActivity.class);
                    mTransacts.invokeTransact(Transacts.WALLPAPER_SERVICE, Transacts.WALLPAPER_DESCRIPTOR,
                            Transacts.setWallpaperComponent, name);
                }));

        mPermissionTasks.put(permission.SHOW_KEYGUARD_MESSAGE,
                new PermissionTest(false, () -> {
                    IBinder token = getActivityToken();
                    IKeyguardDismissCallback callback = new IKeyguardDismissCallback() {
                        @Override
                        public void onDismissError() {
                            mLogger.logDebug("onDismissError called");
                        }

                        @Override
                        public void onDismissSucceeded() {
                            mLogger.logDebug("onDismissSucceed called");
                        }

                        @Override
                        public void onDismissCancelled() {
                            mLogger.logDebug("onDismissCancelled called");
                        }

                        @Override
                        public IBinder asBinder() {
                            return null;
                        }
                    };
                    String service = Transacts.ACTIVITY_TASK_SERVICE;
                    String descriptor = Transacts.ACTIVITY_TASK_DESCRIPTOR;
                    if (mDeviceApiLevel < Build.VERSION_CODES.S) {
                        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P) {
                            service = Context.ACTIVITY_SERVICE;
                            descriptor = Transacts.ACTIVITY_DESCRIPTOR;
                        }
                        mTransacts.invokeTransactWithCharSequence(service, descriptor,
                                Transacts.dismissKeyguard, true, token, callback,
                                "Test SHOW_KEYGUARD_MESSAGE");
                    } else {
                        // In Android 12 this transact is now in the ActivityClientController which
                        // must first be obtained through a call to ActivityTaskManager.
                        Parcel reply = mTransacts.invokeTransact(Transacts.ACTIVITY_TASK_SERVICE,
                                Transacts.ACTIVITY_TASK_DESCRIPTOR,
                                Transacts.getActivityClientController);
                        IBinder activityClientController = reply.readStrongBinder();
                        mTransacts.invokeTransactWithCharSequence(activityClientController,
                                Transacts.ACTIVITY_CLIENT_DESCRIPTOR, Transacts.dismissKeyguard,
                                true, token, callback, "Test SHOW_KEYGUARD_MESSAGE");
                    }
                }));

        mPermissionTasks.put(permission.SHUTDOWN, new PermissionTest(false, () -> {
            // only invoke the action if the permission is not granted to avoid interrupting the
            // test
            if (mContext.checkSelfPermission(permission.SHUTDOWN)
                    == PackageManager.PERMISSION_GRANTED) {
                throw new BypassTestException(
                        "This test will shutdown the device and interrupt the test app");
            }
            mTransacts.invokeTransact(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
                    Transacts.shutdown, 1000);
        }));

        mPermissionTasks.put(permission.SIGNAL_PERSISTENT_PROCESSES,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
                            Transacts.signalPersistentProcesses, Process.SIGNAL_USR1);
                }));

        //TODO: It causes Screen White Out, It shows dialogue
        mPermissionTasks.put(permission.START_ANY_ACTIVITY,
                new PermissionTest(false, () -> {

                    if(Constants.BYPASS_TESTS_AFFECTING_UI)
                        throw new BypassTestException("This test case affects to UI. skip to avoiding ui stuck.");

                    ComponentName componentName;
                    if (mDeviceApiLevel < Build.VERSION_CODES.R) {
                        componentName = new ComponentName("android",
                                "com.android.internal.app.AccessibilityButtonChooserActivity");
                    } else {
                        componentName = new ComponentName("android",
                                "com.android.internal.accessibility.dialog"
                                        + ".AccessibilityButtonChooserActivity");
                    }
                    Intent intent = new Intent();
                    intent.setComponent(componentName);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);

                    //}

                }));

        mPermissionTasks.put(permission.START_TASKS_FROM_RECENTS,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
                            Transacts.startActivityFromRecents, 0, 0);
                }));

        // android.permission.STATSCOMPANION - statscompanion service is guarded by SELinux
        // policy.

        mPermissionTasks.put(permission.STATUS_BAR, new PermissionTest(false, () -> {
            mTransacts.invokeTransact(Transacts.WINDOW_SERVICE, Transacts.WINDOW_DESCRIPTOR,
                    Transacts.setRecentsVisibility, (IBinder) null);
        }));

        mPermissionTasks.put(permission.STATUS_BAR_SERVICE,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.NOTIFICATION_SERVICE,
                            Transacts.NOTIFICATION_DESCRIPTOR,
                            Transacts.isNotificationPolicyAccessGrantedForPackage, "android");
                }));

        mPermissionTasks.put(permission.STOP_APP_SWITCHES,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
                            Transacts.resumeAppSwitches);
                }));

        mPermissionTasks.put(permission.STORAGE_INTERNAL, new PermissionTest(false, () -> {
            UUID storageUUID = null;
            try {
                storageUUID = mStorageManager.getUuidForPath(Environment.getDataDirectory());
            } catch (IOException e) {
                throw new UnexpectedPermissionTestFailureException(e);
            }
            mTransacts.invokeTransact(Transacts.MOUNT_SERVICE, Transacts.MOUNT_DESCRIPTOR,
                    Transacts.getCacheSizeBytes, storageUUID.toString(),
                    1000);

        }));

        mPermissionTasks.put(permission.SUSPEND_APPS, new PermissionTest(false, () -> {
            invokeReflectionCall(mPackageManager.getClass(), "setPackagesSuspended",
                    mPackageManager,
                    new Class<?>[]{String[].class, boolean.class,
                            PersistableBundle.class, PersistableBundle.class, String.class},
                    new String[]{mPackageName}, false, null, null,
                    "Test SUSPEND_APPS");
        }));

        // android.permission.SYSTEM_ALERT_WINDOW - does not throw an Exception if the permission
        // is not granted, instead just returns without performing the request.

        mPermissionTasks.put(permission.TABLET_MODE, new PermissionTest(false, () -> {
            mTransacts.invokeTransact(Transacts.INPUT_SERVICE, Transacts.INPUT_DESCRIPTOR,
                    Transacts.isInTabletMode);
        }));

        // TemporaryEnableAccessibilityStateUntilKeyguardRemoved api has been removed from Android 14.
        mPermissionTasks.put(permission.TEMPORARY_ENABLE_ACCESSIBILITY,
                new PermissionTest(false, VERSION_CODES.M,VERSION_CODES.TIRAMISU, () -> {

                    ComponentName componentName = new ComponentName(mContext, MainActivity.class);
                    mTransacts.invokeTransact(Transacts.ACCESSIBILITY_SERVICE,
                            Transacts.ACCESSIBILITY_DESCRIPTOR,
                            Transacts.temporaryEnableAccessibilityStateUntilKeyguardRemoved,
                            componentName, true);
                }));

        mPermissionTasks.put(permission.TETHER_PRIVILEGED,
                new PermissionTest(false, () -> {
                    if (mDeviceApiLevel < Build.VERSION_CODES.R) {
                        mTransacts.invokeTransact(Transacts.CONNECTIVITY_SERVICE,
                                Transacts.CONNECTIVITY_DESCRIPTOR, Transacts.tether, "wlan0",
                                mPackageName);
                    } else {
                        invokeReflectionCall(mConnectivityManager.getClass(),
                                "isTetheringSupported", mConnectivityManager, null);
                    }

                }));

        mPermissionTasks.put(permission.TRUST_LISTENER, new PermissionTest(false, () -> {
            mTransacts.invokeTransact(Transacts.TRUST_SERVICE, Transacts.TRUST_DESCRIPTOR,
                    Transacts.unregisterTrustListener, getActivityToken());
        }));

        mPermissionTasks.put(permission.UPDATE_APP_OPS_STATS,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.APP_OPS_SERVICE,
                            Transacts.APP_OPS_DESCRIPTOR, Transacts.noteOperation, 0,
                            "android", 1000);
                }));

        mPermissionTasks.put(permission.UPDATE_DEVICE_STATS,
                new PermissionTest(false, () -> {

                    if (mDeviceApiLevel >= Build.VERSION_CODES.TIRAMISU) {
                        mTransacts.invokeTransact(Transacts.BATTERY_STATS_SERVICE,
                                Transacts.BATTERY_STATS_DESCRIPTOR,
                                Transacts.noteStartAudio,0);
                    } else {
                        mTransacts.invokeTransact(Transacts.BATTERY_STATS_SERVICE,
                                Transacts.BATTERY_STATS_DESCRIPTOR,
                                Transacts.noteStartAudio, 0, "test", mUid);
                    }

                }));

        // android.permission.UPDATE_LOCK - updatelock service guarded by SELinux policy.
        mPermissionTasks.put(permission.UPDATE_LOCK_TASK_PACKAGES,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
                            Transacts.updateLockTaskPackages, mUid,
                            new String[]{mPackageName});
                }));

        // android.permission.UPDATE_TIME_ZONE_RULES - service required for this permission
        // guarded by SELinux policy.

        // android.permission.USE_COLORIZED_NOTIFICATIONS - if the permission is not held the
        // colorization is ignored.

        // android.permission.USE_DATA_IN_BACKGROUND - deprecated for
        // REQUEST_COMPANION_USE_DATA_IN_BACKGROUND.

        // PowerManagerService#userActivity does not throw SecurityException to maintain backwards
        // compatibility, and the WindowManager transact that checks for this permission was removed
        // in Android 11.

        mPermissionTasks.put(permission.USER_ACTIVITY,
                new PermissionTest(false, Build.VERSION_CODES.P, Build.VERSION_CODES.Q, () -> {
                    mTransacts.invokeTransact(Transacts.WINDOW_SERVICE, Transacts.WINDOW_DESCRIPTOR,
                            Transacts.requestUserActivityNotification, (IBinder) null);
                }));

        // android.permission.VIEW_INSTANT_APPS - only filters instant apps from result, requires
        // instant app on the device for verification.

        // android.permission.WATCH_APPOPS - without this permission watching appops only targets
        // current UID and always returns false when checking if an operation is active.

        mPermissionTasks.put(permission.WRITE_APN_SETTINGS,
                new PermissionTest(false, () -> {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(Carriers.APN, "testApn");
                    contentValues.put(Carriers.NAME, "testName");
                    contentValues.put(Carriers.NUMERIC, "123456");
                    contentValues.put(Carriers.MCC, "123");
                    contentValues.put(Carriers.MNC, "456");
                    mContentResolver.insert(Carriers.CONTENT_URI, contentValues);
                }));

        mPermissionTasks.put(permission.WRITE_BLOCKED_NUMBERS,
                new PermissionTest(false, () -> {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(BlockedNumbers.COLUMN_ORIGINAL_NUMBER, "520-555-1234");
                    mContentResolver.insert(BlockedNumbers.CONTENT_URI, contentValues);
                }));

        mPermissionTasks.put(permission.WRITE_DREAM_STATE,
                new PermissionTest(false, () -> {
                    mTransacts.invokeTransact(Transacts.DREAMS_SERVICE, Transacts.DREAMS_DESCRIPTOR,
                            Transacts.awaken);
                }));

        mPermissionTasks.put(permission.WRITE_EMBEDDED_SUBSCRIPTIONS,
                new PermissionTest(false, () -> {

                    if (android.os.Build.VERSION.SDK_INT >= VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        mTransacts.invokeTransact(Transacts.EUICC_CONTROLLER_SERVICE,
                                Transacts.EUICC_CONTROLLER_DESCRIPTOR,
                                Transacts.getSupportedCountries, true);
                    } else {
                        mTransacts.invokeTransact(Transacts.ISUB_SERVICE, Transacts.ISUB_DESCRIPTOR,
                                        Transacts.requestEmbeddedSubscriptionInfoListRefresh, 0);
                    }
                }));

        mPermissionTasks.put(permission.WRITE_SECURE_SETTINGS,
                new PermissionTest(false, () -> {
                    Settings.Secure.putString(mContentResolver, "TEST_KEY", "TEST_VALUE");
                }));

        mPermissionTasks.put(permission.INSTALL_EXISTING_PACKAGES,
                new PermissionTest(false, Build.VERSION_CODES.Q ,() -> {
                    // installExistingPackageAsUser - checks both INSTALL_PACKAGES and
                    // INSTALL_EXISTING_PACKAGES, but SecurityException only reports
                    // INSTALL_PACKAGES.
                    mTransacts.invokeTransact(Transacts.PACKAGE_SERVICE, Transacts.PACKAGE_DESCRIPTOR,
                            Transacts.installExistingPackageAsUser, Constants.COMPANION_PACKAGE,
                            mUid, 0, 0);
                }));

        mPermissionTasks.put(permission.READ_VOICEMAIL, new PermissionTest(false, () -> {
            mContentResolver.query(Calls.CONTENT_URI_WITH_VOICEMAIL, null, null, null, null);
        }));

        mPermissionTasks.put(permission.WRITE_VOICEMAIL, new PermissionTest(false, () -> {
            ContentValues values = new ContentValues();
            values.put(Voicemails.SOURCE_PACKAGE, mPackageName);
            mContentResolver.insert(Voicemails.CONTENT_URI, values);
        }));


    }

    void prepareTestBlockForQPR()
    {
        // new permissions for Q
        mPermissionTasks.put(permission.MANAGE_APPOPS,
                new PermissionTest(false, Build.VERSION_CODES.Q, () -> {
                    // clearHistory was only exposed on Q devices
                    if (mDeviceApiLevel == Build.VERSION_CODES.Q) {
                        invokeReflectionCall(mAppOpsManager.getClass(), "clearHistory",
                                mAppOpsManager, null);
                    } else {
                        mTransacts.invokeTransact(Transacts.APP_OPS_SERVICE,
                                Transacts.APP_OPS_DESCRIPTOR,
                                Transacts.clearHistory);
                    }
                }));

        mPermissionTasks.put(permission.MANAGE_TEST_NETWORKS,
                new PermissionTest(false, Build.VERSION_CODES.Q, () -> {
                    Object test_network = mContext.getSystemService("test_network");
                }));

        // android.permission.CONTROL_DISPLAY_COLOR_TRANSFORMS - color_display service required to
        // test this permission is guarded by SELinux policy.

        // This permission was only used on Android 10; starting with Android 11 profile owners are
        // granted access to device identifiers using the MARK_DEVICE_ORGANIZATION_OWNED permission.
        mPermissionTasks.put(permission.GRANT_PROFILE_OWNER_DEVICE_IDS_ACCESS,
                new PermissionTest(false, Build.VERSION_CODES.Q,Build.VERSION_CODES.Q, () -> {
                    ComponentName componentName = new ComponentName(mContext, MainActivity.class);
                    // SecurityException message indicates the failure is due to the process not
                    // being run with the system UID, but a check is performed for this permission
                    // as well.
                    invokeReflectionCall(mDevicePolicyManager.getClass(),
                            "setProfileOwnerCanAccessDeviceIds", mDevicePolicyManager,
                            new Class[]{ComponentName.class}, componentName);
                }));

        mPermissionTasks.put(permission.CONTROL_ALWAYS_ON_VPN,
                new PermissionTest(false, Build.VERSION_CODES.Q, () -> {
                    if (mDeviceApiLevel < Build.VERSION_CODES.S) {
                        mTransacts.invokeTransact(Transacts.CONNECTIVITY_SERVICE,
                                Transacts.CONNECTIVITY_DESCRIPTOR,
                                Transacts.getAlwaysOnVpnPackage, mUid);
                    } else {
                        mTransacts.invokeTransact(Transacts.VPN_SERVICE, Transacts.VPN_DESCRIPTOR,
                                Transacts.getAlwaysOnVpnPackage, mUid);
                    }
                }));

        mPermissionTasks.put(permission.CONTROL_KEYGUARD_SECURE_NOTIFICATIONS,
                new PermissionTest(false, Build.VERSION_CODES.Q, () -> {
                    invokeReflectionCall(mKeyguardManager.getClass(),
                            "getPrivateNotificationsAllowed", mKeyguardManager, null);
                }));

        // android.permission.START_ACTIVITY_AS_CALLER requires both the permission and a permission
        // token.

        // android.permission.MONITOR_DEFAULT_SMS_PACKAGE only protects broadcasts when the default
        // SMS handler changes.

        mPermissionTasks.put(permission.ACCESS_SHARED_LIBRARIES,
                new PermissionTest(false, Build.VERSION_CODES.Q, () -> {
                    invokeReflectionCall(mPackageManager.getClass(), "getDeclaredSharedLibraries",
                            mPackageManager, new Class[]{String.class, int.class}, "android", 0);
                }));

        mPermissionTasks.put(permission.MONITOR_INPUT,
                new PermissionTest(false, Build.VERSION_CODES.Q, () -> {
                    mTransacts.invokeTransact(Transacts.INPUT_SERVICE, Transacts.INPUT_DESCRIPTOR,
                            Transacts.monitorGestureInput, "test", 0);
                }));

        mPermissionTasks.put(permission.POWER_SAVER,
                new PermissionTest(false, Build.VERSION_CODES.Q, () -> {
                    // Starting in Android 12 this permission is no longer required to get the
                    // battery saver control mode.
                    if (mDeviceApiLevel < Build.VERSION_CODES.S) {
                        invokeReflectionCall(mPowerManager.getClass(), "getPowerSaveModeTrigger",
                                mPowerManager, null);
                    } else {
                        mTransacts.invokeTransact(Transacts.POWER_SERVICE,
                                Transacts.POWER_DESCRIPTOR, Transacts.setDynamicPowerSaveHint,
                                false, 80);
                    }
                }));

        //TODO:WE SHOULD NOT LOCK SCREEN WHILE THE TEST....
        mPermissionTasks.put(permission.LOCK_DEVICE,
                new PermissionTest(false, Build.VERSION_CODES.Q,VERSION_CODES.UPSIDE_DOWN_CAKE,() -> {

                    if(Constants.BYPASS_TESTS_AFFECTING_UI)
                        throw new BypassTestException("This test case affects to UI. skip to avoiding ui stuck.");

                    mDevicePolicyManager.lockNow();
                }));

        mPermissionTasks.put(permission.NETWORK_SCAN,
                new PermissionTest(false, Build.VERSION_CODES.Q, () -> {
                    // Starting in Android 12 attempting a network scan with both this permission
                    // as well as a location permission can cause a RuntimeException.
                    if (mDeviceApiLevel >= Build.VERSION_CODES.S) {
                        if (isPermissionGranted(permission.NETWORK_SCAN)
                                && (isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
                                || isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION))) {
                            throw new BypassTestException("This test should only run when the "
                                    + "location permissions are not granted");
                        }
                    }
                    NetworkScanRequest request = new NetworkScanRequest(
                            NetworkScanRequest.SCAN_TYPE_ONE_SHOT, null, 5, 60, true, 5, null);
                    TelephonyScanManager.NetworkScanCallback callback =
                            new TelephonyScanManager.NetworkScanCallback() {
                                @Override
                                public void onResults(List<CellInfo> results) {
                                    mLogger.logDebug("onResults: " + results);
                                }

                                @Override
                                public void onComplete() {
                                    mLogger.logDebug("onComplete");
                                }

                                @Override
                                public void onError(int error) {
                                    mLogger.logDebug("onError: " + error);
                                }
                            };
                    mTelephonyManager.requestNetworkScan(request, AsyncTask.SERIAL_EXECUTOR,
                            callback);
                }));

        mPermissionTasks.put(permission.SEND_DEVICE_CUSTOMIZATION_READY,
                new PermissionTest(false, Build.VERSION_CODES.Q, () -> {
                    invokeReflectionCall(mPackageManager.getClass(),
                            "sendDeviceCustomizationReadyBroadcast", mPackageManager, null);
                }));

        // android.permission.MANAGE_DYNAMIC_SYSTEM - dynamic_system service is guarded by
        // SELinux policy.

        mPermissionTasks.put(permission.MANAGE_CONTENT_CAPTURE,
                new PermissionTest(false, Build.VERSION_CODES.Q, () -> {
                    runShellCommandTest("cmd content_capture get bind-instant-service-allowed");
                }));

        mPermissionTasks.put(permission.MANAGE_ROLE_HOLDERS,
                new PermissionTest(false, Build.VERSION_CODES.Q, () -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        RoleManager roleManager = null;
                        roleManager = (RoleManager) mContext.getSystemService(
                                Context.ROLE_SERVICE);
                        invokeReflectionCall(roleManager.getClass(), "getRoleHolders", roleManager,
                                new Class[]{String.class}, RoleManager.ROLE_SMS);
                    }
                }));

        //TODO : May need to skip if the permission is granted as opening the new activity may interrupt the test app.
        mPermissionTasks.put(permission.OPEN_APP_OPEN_BY_DEFAULT_SETTINGS,
                new PermissionTest(false, Build.VERSION_CODES.Q,
                        Build.VERSION_CODES.R, () -> {

                    if(Constants.BYPASS_TESTS_AFFECTING_UI)
                        throw new BypassTestException("This test case affects to UI. skip to avoiding ui stuck.");

                    // TOOD: May need to skip if the permission is granted as opening the new
                    // activity may interrupt the test app.
                    //DevicePolicyManager.ACTION_
                    Intent intent = new Intent("com.android.settings.APP_OPEN_BY_DEFAULT_SETTINGS");
                    intent.setData(Uri.parse("package:" + mPackageName));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }));

        // android.permission.SMS_FINANCIAL_TRANSACTIONS - both with and without the permission
        // granted a null value can be sent to the callback.
        mPermissionTasks.put(permission.RESET_PASSWORD,
                new PermissionTest(false, Build.VERSION_CODES.Q, () -> {
                    String newPassword = null;
                    // In Android 10 a password had to be specified. Since this test only allows a
                    // password to be set if it has not been previously set this password will need
                    // to be cleared before a subsequent test invocation when the test app is
                    // platform signed.
                    // https://cs.android.com/android/_/android/platform/frameworks/base/+/d952240979aea7d10b5f81dfa9199323c79b4363
                    if (mDeviceApiLevel == Build.VERSION_CODES.Q) {
                        newPassword = "1234";
                    }
                    KeyguardManager manager = (KeyguardManager)
                            mContext.getSystemService(Context.KEYGUARD_SERVICE);
                    if(manager.isDeviceSecure()){
                        //To avoid untrusted password reset, resetPassword() will either
                        //throw SecurityException, or fail silently, if once passowrd is specified.
                        //So we need to reset it before testing.
                        throw new BypassTestException("Skipped : " +
                                "To avoid untrusted password reset, resetPassword() will " +
                                "throw SecurityException, if once screen lock passowrd is specified."+
                                "Please reset it before testing this permission");
                    } else {
                        mDevicePolicyManager.resetPassword(newPassword, 0);
                    }
                }));

        mPermissionTasks.put(permission.WRITE_DEVICE_CONFIG,
                new PermissionTest(false, Build.VERSION_CODES.Q, () -> {
                    try {
                        invokeReflectionCall(Class.forName("android.provider.DeviceConfig"),
                                "setProperty", null,
                                new Class[]{String.class, String.class, String.class,
                                        boolean.class}, "privacy",
                                "device_identifier_access_restrictions_disabled", "false", false);
                    } catch (ClassNotFoundException e) {
                        throw new UnexpectedPermissionTestFailureException(e);
                    }
                }));

        mPermissionTasks.put(permission.NETWORK_MANAGED_PROVISIONING,
                new PermissionTest(false, VERSION_CODES.Q, () -> {
                    if (Build.VERSION.SDK_INT >= VERSION_CODES.Q) {
                        // IWifiManager#setWifiEnabled
                        Parcel result = mTransacts.invokeTransact(Transacts.WIFI_SERVICE,
                                Transacts.WIFI_DESCRIPTOR,
                                Transacts.setWifiEnabled, mPackageName, true);

                        if (result.readBoolean() == false) {
                            throw new SecurityException("Unable to set WIFI enabled");
                        }
                    }
                }));

        mPermissionTasks.put(permission.MANAGE_ACCESSIBILITY,
                new PermissionTest(false, VERSION_CODES.Q, () -> {
                    if (mDeviceApiLevel == VERSION_CODES.Q) {
                        invokeReflectionCall(mAccessibilityManager.getClass(),
                                "getAccessibilityShortcutService", mAccessibilityManager, null);
                    } else {
                        invokeReflectionCall(mAccessibilityManager.getClass(),
                                "performAccessibilityShortcut", mAccessibilityManager, null);
                        // Allow time for the service to be enabled before checking and disabling.
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            mLogger.logError(
                                    "Caught an InterruptedException while waiting for the "
                                            + "accessibility service to be enabled",e);
                        }
                        if (mAccessibilityManager.isTouchExplorationEnabled()) {
                            invokeReflectionCall(mAccessibilityManager.getClass(),
                                    "performAccessibilityShortcut", mAccessibilityManager, null);
                        }
                    }
                }));

        // android.permission.APPROVE_INCIDENT_REPORTS / REQUEST_INCIDENT_REPORT_APPROVAL - both
        // the incident and incidentcompanion services are blocked by SELinux policy.

        //TODO : This Test Shows Dialogue
        mPermissionTasks.put(permission.REVIEW_ACCESSIBILITY_SERVICES,
                new PermissionTest(false, VERSION_CODES.Q, () -> {

                    if(Constants.BYPASS_TESTS_AFFECTING_UI)
                        throw new BypassTestException("This test case affects to UI. skip to avoiding ui stuck.");

                    Intent intent = new Intent(
                            "android.intent.action.REVIEW_ACCESSIBILITY_SERVICES");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);

                }));

        // android.permission.NETWORK_SIGNAL_STRENGTH_WAKEUP - the ConnectivityService first checks
        // if the NetworkCapabilities are requestable, then fails due to the signal strength being
        // specified before the permission can be checked.

        mPermissionTasks.put(permission.WRITE_SETTINGS_HOMEPAGE_DATA,
                new PermissionTest(false, VERSION_CODES.Q, () -> {
                    mContentResolver.query(Uri.parse(
                                    "content://com.android.settings.homepage.CardContentProvider/cards"),
                            null, null, null);
                }));

        mPermissionTasks.put(permission.MANAGE_APP_PREDICTIONS,
                new PermissionTest(false, VERSION_CODES.Q, () -> {
                    invokeReflectionCall(ShortcutManager.class, "getShareTargets",
                            mContext.getSystemService(Context.SHORTCUT_SERVICE),
                            new Class[]{IntentFilter.class}, new IntentFilter());
                }));

        // android.permission.MANAGE_DEBUGGING - the adb service is guarded by SELinux policy.

        mPermissionTasks.put(permission.USE_BIOMETRIC_INTERNAL,
                new PermissionTest(false, VERSION_CODES.Q, () -> {
                    mTransacts.invokeTransact(Transacts.BIOMETRIC_SERVICE, Transacts.BIOMETRIC_DESCRIPTOR,
                            Transacts.hasEnrolledBiometrics, 0, mPackageName);
                }));

        // android.permission.INSTALL_DYNAMIC_SYSTEM - dynamic_system service is guarded by
        // SELinux policy.

        mPermissionTasks.put(permission.OBSERVE_ROLE_HOLDERS, new PermissionTest(false,
                VERSION_CODES.Q, () -> {
            IOnRoleHoldersChangedListener listener = new IOnRoleHoldersChangedListener() {
                @Override
                public void onRoleHoldersChanged(String roleName, int userId) {
                    mLogger.logDebug(
                            "onRoleHoldersChanged: roleName = " + roleName + ", userId = "
                                    + userId);
                }

                @Override
                public IBinder asBinder() {
                    return new Binder();
                }
            };
            mTransacts.invokeTransact(Transacts.ROLE_SERVICE, Transacts.ROLE_DESCRIPTOR,
                    Transacts.addOnRoleHoldersChangedListenerAsUser, listener, 0);
        }));


        mPermissionTasks.put(permission.MANAGE_BIOMETRIC,
                new PermissionTest(false, VERSION_CODES.Q, () -> {

                    try {
                        mTransacts.invokeTransact(Transacts.FACE_SERVICE, Transacts.FACE_DESCRIPTOR,
                                Transacts.generateChallenge, getActivityToken());
                    } catch (BypassTestException e) {
                        // For devices without the face service the fingerprint service can be
                        // used to test this permission. The lock_settings service also uses
                        // this permission, but it is guarded by SELinux policy.
                        if (mDeviceApiLevel >= VERSION_CODES.TIRAMISU) {
                            mTransacts.invokeTransact(Transacts.FINGERPRINT_SERVICE,
                                    Transacts.FINGERPRINT_DESCRIPTOR,
                                    Transacts.cancelAuthenticationFromService, 0, getActivityToken(),
                                    mPackageName, (long) Binder.getCallingPid());
                        } else {
                            mTransacts.invokeTransact(Transacts.FINGERPRINT_SERVICE,
                                    Transacts.FINGERPRINT_DESCRIPTOR,
                                    Transacts.cancelAuthenticationFromService, getActivityToken(),
                                    mPackageName, mUid, Binder.getCallingPid(), 0);
                        }
                    }
                }));

        // android.permission.MODIFY_DEFAULT_AUDIO_EFFECTS requires access to the constructor of
        // one of the AudioEffect classes which are all guarded behind the reflection greylist.

        mPermissionTasks.put(permission.MANAGE_ROLLBACKS,
                new PermissionTest(false, VERSION_CODES.Q, () -> {
                    Object manager = mContext.getSystemService("rollback");
                    invokeReflectionCall(manager.getClass(), "getAvailableRollbacks", manager,
                            null);
                }));

        mPermissionTasks.put(permission.TEST_MANAGE_ROLLBACKS,
                new PermissionTest(false, VERSION_CODES.Q, () -> {
                    // The reloadPersistedData API was only available via reflection in Q
                    if (mDeviceApiLevel == VERSION_CODES.Q) {
                        Object manager = mContext.getSystemService("rollback");
                        invokeReflectionCall(manager.getClass(), "reloadPersistedData", manager,
                                null);
                    } else {
                        mTransacts.invokeTransact(Transacts.ROLLBACK_SERVICE,
                                Transacts.ROLLBACK_DESCRIPTOR,
                                Transacts.reloadPersistedData);
                    }
                }));

        //Moved to internal permission as of 31
        mPermissionTasks.put(permission.MANAGE_SENSOR_PRIVACY,
                new PermissionTest(false, VERSION_CODES.Q,
                        VERSION_CODES.R,() -> {
                    // ISensorPrivacyManager#setSensorPrivacy
                    mTransacts.invokeTransact(Transacts.SENSOR_PRIVACY_SERVICE,
                            Transacts.SENSOR_PRIVACY_DESCRIPTOR,
                            Transacts.setSensorPrivacy, false);
                }));

        mPermissionTasks.put(permission.WIFI_SET_DEVICE_MOBILITY_STATE,
                new PermissionTest(false, VERSION_CODES.Q, () -> {
                    invokeReflectionCall(mWifiManager.getClass(), "setDeviceMobilityState",
                            mWifiManager, new Class[]{int.class}, 0);
                }));

        mPermissionTasks.put(permission.MANAGE_BIOMETRIC_DIALOG,
                new PermissionTest(false, VERSION_CODES.Q, () -> {
                    if (mDeviceApiLevel == VERSION_CODES.Q) {
                        mTransacts.invokeTransact(Transacts.STATUS_BAR_SERVICE,
                                Transacts.STATUS_BAR_DESCRIPTOR,
                                Transacts.hideBiometricDialog);
                    } else if (mDeviceApiLevel >= VERSION_CODES.TIRAMISU) {
                        mTransacts.invokeTransact(Transacts.STATUS_BAR_SERVICE,
                                Transacts.STATUS_BAR_DESCRIPTOR,
                                Transacts.onBiometricHelp, 0, "test");
                    }else {
                        mTransacts.invokeTransact(Transacts.STATUS_BAR_SERVICE,
                                Transacts.STATUS_BAR_DESCRIPTOR,
                                Transacts.onBiometricHelp, "test");
                    }
                }));

        mPermissionTasks.put(permission.START_VIEW_PERMISSION_USAGE,
                new PermissionTest(false, VERSION_CODES.Q, () -> {

                    if(Constants.BYPASS_TESTS_AFFECTING_UI)
                        throw new BypassTestException("This test case affects to UI. skip to avoiding ui stuck.");

                    try {
                        Intent intent = new Intent("android.intent.action.VIEW_PERMISSION_USAGE")
                                .putExtra("android.intent.extra.PERMISSION_GROUP_NAME",
                                        "android.permission-group.CAMERA");
                        if (Build.VERSION.SDK_INT >= VERSION_CODES.S) {
                            //Corresponding Activity will be missing since S, so try to test an activity on
                            //the companion app instead.
                            intent.setComponent(new
                                    ComponentName("com.android.certifications.niap.permissions.companion",
                                    "com.android.certifications.niap.permissions.companion.ViewPermissionUsageActivity"));
                        }

                        ResolveInfo resolveInfo =mPackageManager.resolveActivity(intent, 0);
                        if(resolveInfo == null){
                            throw new BypassTestException("the system does not have corresponding activity to" +
                                    " ROLE_HOLDER_PROVISION_MANAGED_PROFILE action. Let's skip it...");
                        }
                        //alternative plan but it did not works:android.intent.action.VIEW_PERMISSION_USAGE_FOR_PERIOD
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        throw new BypassTestException(
                                "No activity exists on the device to handle the "
                                        + "VIEW_PERMISSION_USAGE action");
                    }
                }));

        // android.permission.INTERACT_ACROSS_PROFILES requires a work profile under the primary
        // user.

        mPermissionTasks.put(permission.WIFI_UPDATE_USABILITY_STATS_SCORE,
                new PermissionTest(false, VERSION_CODES.Q, () -> {
                    invokeReflectionCall(mWifiManager.getClass(), "updateWifiUsabilityScore",
                            mWifiManager, new Class[]{int.class, int.class, int.class}, 0, 0, 0);
                }));

        mPermissionTasks.put(permission.WHITELIST_RESTRICTED_PERMISSIONS,
                new PermissionTest(false, VERSION_CODES.Q, () -> {
                    if (Build.VERSION.SDK_INT >= VERSION_CODES.Q) {
                        Set<String> permissions = mPackageManager.getWhitelistedRestrictedPermissions(
                                "android", PackageManager.FLAG_PERMISSION_WHITELIST_SYSTEM);
                    }
                }));

        // android.permission.ENABLE_TEST_HARNESS_MODE - testharness service protected by
        // SELinux policy.

        mPermissionTasks.put(permission.GET_RUNTIME_PERMISSIONS,
                new PermissionTest(false, VERSION_CODES.Q, () -> {
                    invokeReflectionCall(mPackageManager.getClass(), "getPermissionFlags",
                            mPackageManager,
                            new Class[]{String.class, String.class, UserHandle.class},
                            Manifest.permission.READ_PHONE_STATE, "android",
                            UserHandle.getUserHandleForUid(mUid));
                }));

        // android.permission.NETWORK_CARRIER_PROVISIONING requires a FQDN of the Passpoint
        // configuration.



        mPermissionTasks.put(permission.READ_DEVICE_CONFIG,
                new PermissionTest(false, VERSION_CODES.Q, () -> {
                    if (Build.VERSION.SDK_INT >= 35) {
                        throw new BypassTestException(
                                "READ_DEVICE_CONFIG permission check is disabled in SDK35 beta3. " +
                                        "Wating public release.");
                    }
                    try {
                        invokeReflectionCall(Class.forName("android.provider.DeviceConfig"),
                                "getProperty", null, new Class[]{String.class, String.class},
                                //"adservices", "sdksandbox_enforce_restrictions");
                                "privacy", "device_identifier_access_restrictions_disabled");
//                        invokeReflectionCall(Class.forName("android.provider.DeviceConfig"),
//                                "addOnPropertiesChangedListener", null,
//                                new Class[]{String.class, Executor.class,DeviceConfig.OnPropertiesChangedListener.class},
//                                //"adservices", "sdksandbox_enforce_restrictions");
//                                "privacy", "device_identifier_access_restrictions_disabled");


                    } catch (ReflectiveOperationException e) {
                        throw new UnexpectedPermissionTestFailureException(e);
                    }
                }));

        mPermissionTasks.put(permission.ADJUST_RUNTIME_PERMISSIONS_POLICY,
                new PermissionTest(false, VERSION_CODES.Q, () -> {
                    mTransacts.invokeTransact(Transacts.PACKAGE_SERVICE, Transacts.PACKAGE_DESCRIPTOR,
                            Transacts.getRuntimePermissionsVersion, 0);
                }));

        // Following are the new permissions for Android 11.
        mPermissionTasks.put(permission.ACCESS_CONTEXT_HUB,
                new PermissionTest(false, VERSION_CODES.R, () -> {
                    // As of Android 11 all ACCESS_CONTEXT_HUB guarded methods are also guarded by
                    // ACCESS_LOCATION_HARDWARE.
                    Object contextHubManager = mContext.getSystemService("contexthub");
                    invokeReflectionCall(contextHubManager.getClass(), "getContextHubs",
                            contextHubManager, null);
                }));

        mPermissionTasks.put(permission.ACCESS_LOCUS_ID_USAGE_STATS,
                new PermissionTest(false, VERSION_CODES.R, () -> {
                    // This permission guards access to locus events from other apps; the companion
                    // app should first be run to generate a locus event that can be queried by
                    // this test.
                    boolean locusEventFound = false;
                    UsageEvents usageEvents = mUsageStatsManager.queryEvents(0, Long.MAX_VALUE);
                    UsageEvents.Event event = new UsageEvents.Event();
                    while (usageEvents.hasNextEvent()) {
                        usageEvents.getNextEvent(event);
                        // 30 is the value of the hidden Event.LOCUS_ID_SET field indicating a
                        // locus event.
                        if (event.getEventType() == 30) {
                            mLogger.logDebug(
                                    "Time of locus event: " + event.getTimeStamp() + ", package: "
                                            + event.getPackageName());
                            locusEventFound = true;
                            break;
                        }
                    }
                    if (!locusEventFound) {
                        throw new SecurityException(
                                "No locus events returned from usage stats query");
                    }
                }));

        mPermissionTasks.put(permission.ACCESS_MESSAGES_ON_ICC,
                new PermissionTest(false, VERSION_CODES.R, () -> {
                    SmsManager smsManager = SmsManager.getSmsManagerForSubscriptionId(0);
                    invokeReflectionCall(smsManager.getClass(), "getAllMessagesFromIcc", smsManager,
                            null);
                }));

        mPermissionTasks.put(permission.ACCESS_VIBRATOR_STATE,
                new PermissionTest(false, VERSION_CODES.R, () -> {
                    mTransacts.invokeTransact(Transacts.VIBRATOR_SERVICE, Transacts.VIBRATOR_DESCRIPTOR,
                            Transacts.isVibrating);
                }));

        //The permission had been removed as of SDK 31
        mPermissionTasks.put(permission.ASSOCIATE_INPUT_DEVICE_TO_DISPLAY_BY_PORT,
                new PermissionTest(false,
                        VERSION_CODES.R, VERSION_CODES.R, () -> {
                    mTransacts.invokeTransact(Transacts.INPUT_SERVICE, Transacts.INPUT_DESCRIPTOR,
                            Transacts.removePortAssociation, "testPort");
                }));


        // android.permission.OPEN_CLOSE_CAMERA_LISTENER requires an ICameraServiceListener which
        // cannot be instantiated due to the reflection greylist denying access to the constructor.

        // android.permission.CAPTURE_VOICE_COMMUNICATION_OUTPUT - requires an AudioMix with a
        // policy registered with AudioMixingRule#voiceCommunicationCaptureAllowed. This method
        // and the AudioMix builder are not accessible to apps via reflection, so there is no
        // way to create an AudioMix for this test.

        mPermissionTasks.put(permission.COMPANION_APPROVE_WIFI_CONNECTIONS,
                new PermissionTest(false, VERSION_CODES.R, () -> {
                    // CompanionDeviceManager#isDeviceAssociatedForWifiConnection is the only
                    // method that is guarded by this permission. While it does check
                    // MANAGE_COMPANION_DEVICES first it will ultimately return true if all of
                    // the permission checks are successful, including the check for
                    // COMPANION_APPROVE_WIFI_CONNECTIONS.
                    CompanionDeviceManager companionDeviceManager =
                            (CompanionDeviceManager) mContext.getSystemService(
                                    Context.COMPANION_DEVICE_SERVICE);
                    Object result = invokeReflectionCall(companionDeviceManager.getClass(),
                            "isDeviceAssociatedForWifiConnection", companionDeviceManager,
                            new Class[]{String.class, MacAddress.class, UserHandle.class},
                            mPackageName, MacAddress.BROADCAST_ADDRESS,
                            UserHandle.getUserHandleForUid(mUid));
                    if (result instanceof Boolean && (Boolean) result != true) {
                        throw new SecurityException(
                                "isDeviceAssociatedForWifiConnection returned false indicating "
                                        + "permission is not granted");
                    }
                }));


        mPermissionTasks.put(permission.CONFIGURE_INTERACT_ACROSS_PROFILES,
                new PermissionTest(false, VERSION_CODES.R, () -> {
                    mTransacts.invokeTransact(Transacts.CROSS_PROFILE_APPS_SERVICE,
                            Transacts.CROSS_PROFILE_APPS_DESCRIPTOR,
                            Transacts.clearInteractAcrossProfilesAppOps);
                }));

        mPermissionTasks.put(permission.CONTROL_DEVICE_LIGHTS,
                new PermissionTest(false, VERSION_CODES.R, () -> {
                    @SuppressLint("WrongConstant") Object lightsManager = mContext.getSystemService("lights");
                    invokeReflectionCall(lightsManager.getClass(), "getLights", lightsManager,
                            null);
                }));

        //TODO: This Test Hide Activity and Show Home Page
        mPermissionTasks.put(permission.ENTER_CAR_MODE_PRIORITIZED,
                new PermissionTest(false, VERSION_CODES.R,VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if(Constants.BYPASS_TESTS_AFFECTING_UI)
                        throw new BypassTestException("This test case affects to UI. skip to avoiding ui stuck.");

                    UiModeManager uiModeManager = (UiModeManager) mContext.getSystemService(
                            Context.UI_MODE_SERVICE);
                    // The reflective call is required since a priority other than 0 must be
                    // specified to test this permission.
                    invokeReflectionCall(uiModeManager.getClass(), "enableCarMode", uiModeManager,
                            new Class[]{int.class, int.class}, 1, 0);
                    uiModeManager.disableCarMode(UiModeManager.DISABLE_CAR_MODE_GO_HOME);

                }));

        mPermissionTasks.put(permission.KEYPHRASE_ENROLLMENT_APPLICATION,
                new PermissionTest(false, VERSION_CODES.R, () -> {
                    mTransacts.invokeTransact(Transacts.VOICE_INTERACTION_SERVICE,
                            Transacts.VOICE_INTERACTION_DESCRIPTOR,
                            Transacts.updateKeyphraseSoundModel, (Object) null);
                }));

        mPermissionTasks.put(permission.LISTEN_ALWAYS_REPORTED_SIGNAL_STRENGTH,
                new PermissionTest(false, VERSION_CODES.R, () -> {
                    if (Build.VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
                        //The way to using the listner was obsolated after android T, so let me choose using this option instead
                        //https://cs.android.com/android/platform/superproject/+/master:cts/tests/tests/telephony/current/src/android/telephony/cts/PhoneStateListenerTest.java;l=328;drc=e64188140ba71c7b7424b044119b37af1dde6609?=4186
                        //problem : if the signature does not match it always fail,because the method also checks MODIFY_PHONE_STATE permission

                        SignalStrengthUpdateRequest.Builder builder = new SignalStrengthUpdateRequest.Builder()
                                .setSignalThresholdInfos(Collections.EMPTY_LIST);

                        builder = (SignalStrengthUpdateRequest.Builder)
                                invokeReflectionCall(SignalStrengthUpdateRequest.Builder.class,
                                        "setSystemThresholdReportingRequestedWhileIdle", builder,
                                        new Class<?>[]{boolean.class},true);
                        SignalStrengthUpdateRequest request = builder.build();
                        try {
                            mTelephonyManager.setSignalStrengthUpdateRequest(request);
                        } catch (IllegalStateException ex){
                            mLogger.logInfo("Expected:"+ex.getMessage());
                        }

                    } else {
                        HandlerThread handlerThread = new HandlerThread(TAG);
                        handlerThread.start();
                        Handler handler = new Handler(handlerThread.getLooper());
                        // An Exception array is used since local variables referenced from a lambda
                        // expression must be effectively final.
                        //https://cs.android.com/android/platform/superproject/+/master:cts/tests/tests/telephony/current/src/android/telephony/cts/PhoneStateListenerTest.java;l=328;drc=e64188140ba71c7b7424b044119b37af1dde6609?
                        Exception[] caughtException = new Exception[1];
                        CountDownLatch latch = new CountDownLatch(1);
                        handler.post(() -> {
                            PhoneStateListener listener = new PhoneStateListener() {
                                @Override
                                public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                                    mLogger.logDebug("onSignalStrengthChanged: signalStrength = "
                                            + signalStrength);
                                }
                            };
                            try {
                                mTelephonyManager.listen(listener, 0x00000200);
                            } catch (Exception e) {
                                caughtException[0] = e;
                                e.printStackTrace();
                            }
                            latch.countDown();
                        });
                        try {
                            latch.await(2000, TimeUnit.MILLISECONDS);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (caughtException[0] instanceof SecurityException) {
                            throw (SecurityException) caughtException[0];
                        }
                    }

                }));

        // android.permission.LOADER_USAGE_STATS - incremental service is guarded by SELinux
        // policy.

        mPermissionTasks.put(permission.LOG_COMPAT_CHANGE,
                new PermissionTest(false, VERSION_CODES.R, () -> {
                    mTransacts.invokeTransact(Transacts.PLATFORM_COMPAT_SERVICE,
                            Transacts.PLATFORM_COMPAT_DESCRIPTOR,
                            Transacts.reportChangeByUid, 0, mUid);
                }));

        mPermissionTasks.put(permission.MANAGE_COMPANION_DEVICES,
                new PermissionTest(false, VERSION_CODES.P, () -> {
                    // CompanionDeviceManager#isDeviceAssociatedForWifiConnection can be used for
                    // both this permission as well as COMPANION_APPROVE_WIFI_CONNECTION as it first
                    // checks for the MANAGE_COMPANION_DEVICES permission, then it checks if the
                    // caller has the COMPANION_APPROVE_WIFI_CONNECTION permission to allow
                    // connecting to a WiFi network without user consent.
                    CompanionDeviceManager companionDeviceManager =
                            (CompanionDeviceManager) mContext.getSystemService(
                                    Context.COMPANION_DEVICE_SERVICE);
                    invokeReflectionCall(companionDeviceManager.getClass(),
                            "isDeviceAssociatedForWifiConnection", companionDeviceManager,
                            new Class[]{String.class, MacAddress.class, UserHandle.class},
                            mPackageName, MacAddress.BROADCAST_ADDRESS,
                            UserHandle.getUserHandleForUid(mUid));
                }));

        //TODO : Clear App Cache Dialogue
        mPermissionTasks.put(permission.MANAGE_EXTERNAL_STORAGE,
                new PermissionTest(false, VERSION_CODES.R, () -> {
                    // Only an app granted this permission will receive a value of true back from
                    // this API.
                    if (Build.VERSION.SDK_INT >= VERSION_CODES.R) {

                        if(Constants.BYPASS_TESTS_AFFECTING_UI)
                            throw new BypassTestException("This test case affects to UI. skip to avoiding ui stuck.");

                        if (!Environment.isExternalStorageManager()) {
                            throw new SecurityException(
                                    "MANAGE_EXTERNAL_STORAGE: Test app is not the external storage "
                                            + "manager");
                        }
                        // This activity can then be started to verify that this app has been granted
                        // this permission; if the permission is not granted the following will be
                        // in logcat:
                        // CacheClearingActivity: Calling package com.android.permissions.tester
                        //   has no permission clear app caches
                        Intent intent = new Intent();
                        intent.setAction("android.os.storage.action.CLEAR_APP_CACHE");
                        intent.addCategory("android.intent.category.DEFAULT");
                        mActivity.startActivityForResult(intent, 0);
                    }
                }));

        mPermissionTasks.put(permission.MARK_DEVICE_ORGANIZATION_OWNED,
                new PermissionTest(false, VERSION_CODES.R, () -> {
                    ComponentName componentName = new ComponentName(mContext, MainActivity.class);
                    //
                    if (mDeviceApiLevel <= VERSION_CODES.S_V2) {
                        // If the permission is granted an active admin must first be granted to
                        // verify this test; since this is outside the scope of a permission test skip
                        // the test when the permission is granted.
                        if (mContext.checkSelfPermission(permission.MARK_DEVICE_ORGANIZATION_OWNED)
                                == PackageManager.PERMISSION_GRANTED) {
                            throw new BypassTestException(
                                    "This permission requires an active admin to be set");
                        }
                        mTransacts.invokeTransact(Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.markProfileOwnerOnOrganizationOwnedDevice, componentName);
                    } else {
                        //Above methods are removed from Android T
                        //The below api can also be granted to run with MANAGE_PROFILE_AND_DEVICE_OWNERS
                        //permission. For checking please remove both of the permisssions.
                        mTransacts.invokeTransact(Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.setProfileOwnerOnOrganizationOwnedDevice,
                                componentName,Process.myUid(),true);
                    }
                }));

        mPermissionTasks.put(permission.MEDIA_RESOURCE_OVERRIDE_PID,
                new PermissionTest(false, VERSION_CODES.R, () -> {
                    try {
                        mTransacts.invokeTransact(Transacts.RESOURCE_MANAGER_SERVICE,
                                Transacts.RESOURCE_MANAGER_DESCRIPTOR, Transacts.overridePid,
                                1000, 1001);
                    } catch (Exception e) {
                        // The ResourceManagerService fails with a ServiceSpecificException when the
                        // permission requirements are not met. Since the ServiceSpecificException
                        // class is hidden from apps check the string of the Exception message to
                        // confirm the proper Exception was thrown:
                        // ServiceManager: Permission failure:
                        //   android.permission.MEDIA_RESOURCE_OVERRIDE_PID from uid=10241 pid=18140
                        // ResourceManagerService: Permission Denial: can't access overridePid
                        //   method from pid=18140, self pid=1217
                        // SignaturePermissionTester: android.os.ServiceSpecificException:
                        //   (code -1)
                        String exceptionMessage = e.toString();
                        if (exceptionMessage.contains(("ServiceSpecificException"))) {
                            throw new SecurityException(exceptionMessage);
                        }
                    }
                }));

        mPermissionTasks.put(permission.MODIFY_SETTINGS_OVERRIDEABLE_BY_RESTORE,
                new PermissionTest(false, VERSION_CODES.R, () -> {
                    invokeReflectionCall(Settings.System.class, "putString", null,
                            new Class[]{ContentResolver.class, String.class, String.class,
                                    boolean.class}, mContext.getContentResolver(), "vibrate_on",
                            "1", true);
                }));

        mPermissionTasks.put(permission.MONITOR_DEVICE_CONFIG_ACCESS,
                new PermissionTest(false, VERSION_CODES.R, () -> {
                    try {

                        if (Build.VERSION.SDK_INT >= VERSION_CODES.R) {
                            Class<?> onResultListenerClass = Class.forName(
                                    "android.os.RemoteCallback$OnResultListener");
                            Object onResultListener = Proxy.newProxyInstance(
                                    onResultListenerClass.getClassLoader(),
                                    new Class[]{onResultListenerClass}, new InvocationHandler() {
                                        @Override
                                        public Object invoke(Object o, Method method, Object[] objects)
                                                throws Throwable {
                                            mLogger.logDebug("invoke: " + method);
                                            return null;
                                        }
                                    });

                            Class<?> remoteCallbackClass = Class.forName("android.os.RemoteCallback");
                            Constructor remoteCallbackConstructor = remoteCallbackClass.getConstructor(
                                    onResultListenerClass);
                            Object remoteCallback = remoteCallbackConstructor.newInstance(
                                    (Object) onResultListener);

                            Bundle bundle = new Bundle();
                            bundle.putInt("_user", 0);
                            bundle.putParcelable("_monitor_callback_key", (Parcelable) remoteCallback);

                            mContentResolver.call("settings", "REGISTER_MONITOR_CALLBACK_config", null,
                                    bundle);
                        }
                    } catch (ReflectiveOperationException e) {
                        throw new UnexpectedPermissionTestFailureException(e);
                    }
                }));

        mPermissionTasks.put(permission.NETWORK_AIRPLANE_MODE,
                new PermissionTest(false, VERSION_CODES.R, () -> {
                    invokeReflectionCall(mConnectivityManager.getClass(), "setAirplaneMode",
                            mConnectivityManager, new Class[]{boolean.class}, false);
                }));

        mPermissionTasks.put(permission.NETWORK_FACTORY,
                new PermissionTest(false, VERSION_CODES.R, () -> {
                    try {
                        HandlerThread handlerThread = new HandlerThread(TAG);
                        handlerThread.start();
                        Class<?> networkProviderClass = Class.forName("android.net.NetworkProvider");
                        Constructor networkProviderConstructor =
                                networkProviderClass.getConstructor(Context.class, Looper.class,
                                        String.class);
                        Object networkProvider = networkProviderConstructor.newInstance(mContext,
                                handlerThread.getLooper(), "test_network_provider");

                        NetworkRequest networkRequest = new NetworkRequest.Builder().addCapability(
                                NetworkCapabilities.NET_CAPABILITY_INTERNET).build();

                        invokeReflectionCall(mConnectivityManager.getClass(),
                                "unregisterNetworkProvider", mConnectivityManager,
                                new Class[]{Class.forName("android.net.NetworkProvider")},
                                (Object) networkProvider);
                    } catch (ReflectiveOperationException e) {
                        throw new UnexpectedPermissionTestFailureException(e);
                    }
                }));

        mPermissionTasks.put(permission.NETWORK_STATS_PROVIDER,
                new PermissionTest(false, VERSION_CODES.R, () -> {
                    mTransacts.invokeTransact(Transacts.NETWORK_STATS_SERVICE,
                            Transacts.NETWORK_STATS_DESCRIPTOR,
                            Transacts.registerNetworkStatsProvider, "testTag", null);
                }));

        mPermissionTasks.put(permission.OBSERVE_NETWORK_POLICY,
                new PermissionTest(false, VERSION_CODES.R, () -> {
                    if (mDeviceApiLevel < VERSION_CODES.S) {
                        mTransacts.invokeTransact(Transacts.NET_POLICY_SERVICE,
                                Transacts.NET_POLICY_DESCRIPTOR,
                                Transacts.registerListener, (Object) null);
                    } else {
                        mTransacts.invokeTransact(Transacts.NET_POLICY_SERVICE,
                                Transacts.NET_POLICY_DESCRIPTOR, Transacts.isUidNetworkingBlocked,
                                mUid, false);
                    }
                }));

        mPermissionTasks.put(permission.OVERRIDE_COMPAT_CHANGE_CONFIG,
                new PermissionTest(false, VERSION_CODES.R, () -> {
                    mTransacts.invokeTransact(Transacts.PLATFORM_COMPAT_SERVICE,
                            Transacts.PLATFORM_COMPAT_DESCRIPTOR,
                            Transacts.clearOverridesForTest, mPackageName);
                }));

        mPermissionTasks.put(permission.PEEK_DROPBOX_DATA,
                new PermissionTest(false, VERSION_CODES.R, () -> {

                    //Just to be safe, if the PEEK_DROPBOX_DATA permission is not granted
                    //but READ_LOGS and PACKAGE_USAGE_STATS are granted,
                    //I think we should skip the test since it looks like these development permissions
                    //that are granted with the -g flag will always allow the normal variant to access dropbox data.

                    if (mContext.checkSelfPermission(Manifest.permission.READ_LOGS)
                            == PackageManager.PERMISSION_GRANTED &&
                            mContext.checkSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS)
                                    == PackageManager.PERMISSION_GRANTED) {
                        if(mContext.checkSelfPermission(permission.PEEK_DROPBOX_DATA)
                                != PackageManager.PERMISSION_GRANTED)
                            throw new BypassTestException(
                                    "Bypass the check due to avoid unexpected behaviour. ");
                    }

                    long currTimeMs = System.currentTimeMillis();

                    //#add a line from companion app.=>need to run the companion app before testing
                    //final DropBoxManager db = (DropBoxManager) mContext.getSystemService(Context.DROPBOX_SERVICE);
                    //db.addText("test-tag","PEEK_DROPBOX_DATA test at :"+currTimeMs);

                    Parcel result= mTransacts.invokeTransact(Transacts.DROPBOX_SERVICE,
                            Transacts.DROPBOX_DESCRIPTOR,
                            Transacts.getNextEntry, "test-companion-tag", currTimeMs-(1000*60*60*8), mPackageName);

                    if (result.readInt() == 0) {
                        throw new SecurityException(
                                "Received DropBoxManager.Entry is null during PEEK_DROPBOX_DATA "
                                        + "test. Please check you surely executed companion app before testing.");
                    }
                    DropBoxManager.Entry entry = DropBoxManager.Entry.CREATOR.createFromParcel(
                            result);

                    mLogger.logDebug(
                            "Successfully parsed entry from parcel: " + entry.getText(100));
                }));

        mPermissionTasks.put(permission.RADIO_SCAN_WITHOUT_LOCATION,
                new PermissionTest(false, VERSION_CODES.R, () -> {
                    // if the app has been granted a location permission then skip this test as this
                    // permission is intended to allow scans without location.
                    if (mContext.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED ||
                            mContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                                    == PackageManager.PERMISSION_GRANTED) {
                        throw new BypassTestException(
                                "This app has been granted a location permission");
                    }
                    boolean scanStartedSuccessfully = mWifiManager.startScan();
                    if (!scanStartedSuccessfully) {
                        throw new SecurityException(
                                "Wifi scan could not be started during "
                                        + "RADIO_SCAN_WITHOUT_LOCATION test");
                    }
                }));

        // android.permission.REGISTER_STATS_PULL_ATOM / REGISTER_STATS_PULL_ATOM - the stats
        // service is guarded by SELinux policy.

        // android.permission.READ_CARRIER_APP_INFO - the system_config service is guarded by
        // SELinux policy.

        mPermissionTasks.put(permission.READ_COMPAT_CHANGE_CONFIG,
                new PermissionTest(false, VERSION_CODES.R, () -> {
                    try {
                        invokeReflectionCall(Class.forName("android.app.compat.CompatChanges"),
                                "isChangeEnabled", null,
                                new Class[]{long.class, String.class, UserHandle.class}, 0,
                                "android", UserHandle.getUserHandleForUid(mUid));
                    } catch (ReflectiveOperationException e) {
                        throw new UnexpectedPermissionTestFailureException(e);
                    }
                }));

        mPermissionTasks.put(permission.RESTORE_RUNTIME_PERMISSIONS,
                new PermissionTest(false, VERSION_CODES.R, () -> {
                    @SuppressLint("WrongConstant") Object permissionControllerManager = mContext.getSystemService(
                            "permission_controller");
                    invokeReflectionCall(permissionControllerManager.getClass(),
                            "stageAndApplyRuntimePermissionsBackup", permissionControllerManager,
                            new Class[]{byte[].class, UserHandle.class}, new byte[0],
                            UserHandle.getUserHandleForUid(mUid));
                }));

        mPermissionTasks.put(permission.SECURE_ELEMENT_PRIVILEGED_OPERATION,
                new PermissionTest(false, VERSION_CODES.R, () -> {
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    final CountDownLatch latch = new CountDownLatch(1);
                    SEService.OnConnectedListener listener = () -> {
                        mLogger.logDebug("SECURE_ELEMENT_PRIVILEGED_OPERATION: onConnect invoked");
                        latch.countDown();
                    };
                    SEService seService = new SEService(mContext, executor, listener);
                    try {
                        boolean connected = latch.await(2000, TimeUnit.MILLISECONDS);
                        if (!connected) {
                            throw new BypassTestException(
                                    "Unable to establish a connection to the SEService");
                        }
                    } catch (InterruptedException e) {
                        throw new UnexpectedPermissionTestFailureException(e);
                    }
                    Reader[] readers = seService.getReaders();
                    if (readers == null || readers.length == 0) {
                        throw new BypassTestException(
                                "No Secure Element Readers available on the device");
                    }
                    boolean resetSuccessful = false;
                    for (Reader reader : readers) {
                        mLogger.logDebug("About to invoke method on isSecureElement: "
                                + reader.isSecureElementPresent());
                        // If the HAL is not available to invoke the reset then a value of false
                        // will be returned before making it to the permission check.
                        Object result = invokeReflectionCall(reader.getClass(), "reset", reader,
                                null);
                        if (result instanceof Boolean
                                && ((Boolean) result).booleanValue() == true) {
                            // a result of true means the permission check was passed and the
                            // reset was successfully invoked.
                            resetSuccessful = true;
                            break;
                        }
                    }
                    if (!resetSuccessful) {
                        throw new BypassTestException(
                                "The SecureElement HAL v1.2 is not available for this test");
                    }
                }));

        mPermissionTasks.put(permission.SET_INITIAL_LOCK,
                new PermissionTest(false, VERSION_CODES.R, () -> {
                    invokeReflectionCall(mKeyguardManager.getClass(), "setLock", mKeyguardManager,
                            new Class[]{int.class, byte[].class, int.class}, 0, new byte[0], 0);
                }));

        mPermissionTasks.put(permission.SYSTEM_CAMERA,
                new PermissionTest(false, VERSION_CODES.R, () -> {
                    try {
                        String[] cameraIds = mCameraManager.getCameraIdList();
                        for (String cameraId : cameraIds) {
                            CameraCharacteristics cameraCharacteristics =
                                    mCameraManager.getCameraCharacteristics(cameraId);
                            for (int capability : cameraCharacteristics.get(
                                    CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES)) {
                                // If a system camera was found then this indicates the API was
                                // successful
                                if (capability
                                        == CameraCharacteristics
                                        .REQUEST_AVAILABLE_CAPABILITIES_SYSTEM_CAMERA) {
                                    mLogger.logDebug("Found a system camera with ID " + cameraId);
                                    return;
                                }
                            }
                        }
                    } catch (CameraAccessException e) {
                        throw new UnexpectedPermissionTestFailureException(e);
                    }
                    // If this point is reached there's no guarantee that the device has any
                    // system cameras, so report a bypass for the test.
                    throw new BypassTestException("No system cameras reported on the device");
                }));

        // android.permission.SUGGEST_MANUAL_TIME_AND_ZONE and SUGGEST_TELEPHONY_TIME_AND_SONE -
        // the time_zone_detector service required for these permissions is guarded by SELinux
        // policy.

        mPermissionTasks.put(permission.UPGRADE_RUNTIME_PERMISSIONS,
                new PermissionTest(false, VERSION_CODES.R, () -> {
                    Object permissionManager = mContext.getSystemService("permission");
                    invokeReflectionCall(permissionManager.getClass(),
                            "getRuntimePermissionsVersion", permissionManager, null);
                }));

        mPermissionTasks.put(permission.VIBRATE_ALWAYS_ON,
                new PermissionTest(false, VERSION_CODES.R, () -> {
                    mTransacts.invokeTransact(Transacts.VIBRATOR_SERVICE, Transacts.VIBRATOR_DESCRIPTOR,
                            Transacts.setAlwaysOnEffect, mUid, mPackageName, 0, null,
                            null);
                }));

        mPermissionTasks.put(permission.WHITELIST_AUTO_REVOKE_PERMISSIONS,
                new PermissionTest(false, VERSION_CODES.R, () -> {
                    if (mDeviceApiLevel < VERSION_CODES.S) {
                        mTransacts.invokeTransact(Transacts.PERMISSION_MANAGER_SERVICE,
                                Transacts.PERMISSION_MANAGER_DESCRIPTOR,
                                Transacts.isAutoRevokeWhitelisted, mPackageName, true,
                                0);
                    } else {
                        mTransacts.invokeTransact(Transacts.PERMISSION_MANAGER_SERVICE,
                                Transacts.PERMISSION_MANAGER_DESCRIPTOR,
                                Transacts.isAutoRevokeExempted, mPackageName, 0);
                    }
                }));

        mPermissionTasks.put(permission.USE_INSTALLER_V2,
                new PermissionTest(false, VERSION_CODES.R, () -> {
                    PackageInstaller packageInstaller = mPackageManager.getPackageInstaller();
                    try {
                        int sessionId = packageInstaller.createSession(
                                new SessionParams(
                                        SessionParams.MODE_FULL_INSTALL));
                        PackageInstaller.Session session = packageInstaller.openSession(sessionId);
                        invokeReflectionCall(session.getClass(), "getDataLoaderParams", session,
                                null);
                    } catch (IOException e) {
                        throw new UnexpectedPermissionTestFailureException(e);
                    }
                }));

        mPermissionTasks.put(permission.ADD_TRUSTED_DISPLAY,
                new PermissionTest(false, VERSION_CODES.R, () -> {
                    // DisplayManager#VIRTUAL_DISPLAY_FLAG_TRUSTED is set to 1 << 10, but the
                    // flag is hidden so use the constant value for this test.
                    mDisplayManager.createVirtualDisplay(TAG, 10, 10, 1, null,
                            1 << 10);
                }));

        //prepareTestBlock12();

    }
    void prepareTestBlockForS()
    {
        // The following are the new signature permissions for Android 12.
        mPermissionTasks.put(permission.ASSOCIATE_INPUT_DEVICE_TO_DISPLAY,
                new PermissionTest(false, VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.INPUT_SERVICE, Transacts.INPUT_DESCRIPTOR,
                            Transacts.removePortAssociation, "testPort");
                }));

        mPermissionTasks.put(permission.CAMERA_INJECT_EXTERNAL_CAMERA,
                new PermissionTest(false, VERSION_CODES.S, () -> {
                    Object injectionCallback;
                    Object injectionSession;
                    try {
                        Class<?> injectionCallbackClass = Class.forName(
                                "android.hardware.camera2.ICameraInjectionCallback");
                        injectionCallback = Proxy.newProxyInstance(
                                injectionCallbackClass.getClassLoader(),
                                new Class[]{injectionCallbackClass}, new InvocationHandler() {
                                    @Override
                                    public Object invoke(Object o, Method method, Object[] objects)
                                            throws Throwable {
                                        mLogger.logDebug("injectionCallback#invoke: " + method);
                                        if (method.toString().contains("asBinder")) {
                                            return new Binder();
                                        } else if (method.toString().contains("onInjectionError")) {
                                            return null;
                                        }
                                        return null;
                                    }
                                });
                        Class<?> injectionSessionClass = Class.forName(
                                "android.hardware.camera2.ICameraInjectionSession");
                        injectionSession = Proxy.newProxyInstance(
                                injectionSessionClass.getClassLoader(),
                                new Class[]{injectionSessionClass}, new InvocationHandler() {
                                    @Override
                                    public Object invoke(Object o, Method method, Object[] objects)
                                            throws Throwable {
                                        mLogger.logDebug("injectionSession#invoke: " + method);
                                        if (method.toString().contains("asBinder")) {
                                            return new Binder();
                                        }
                                        return null;
                                    }
                                });
                    } catch (ClassNotFoundException e) {
                        throw new UnexpectedPermissionTestFailureException(e);
                    }
                    try {
                        if(mDeviceApiLevel >= VERSION_CODES.UPSIDE_DOWN_CAKE){
                            mTransacts.invokeTransact(Context.CAMERA_SERVICE,
                                    Transacts.CAMERA_DESCRIPTOR,
                                    Transacts.injectCamera, mContext.getPackageName(),
                                    "",
                                    "", injectionCallback);
                        } else if(mDeviceApiLevel == VERSION_CODES.TIRAMISU){
                            mTransacts.invokeTransact(Transacts.CAMERA_SERVICE,
                                    Transacts.CAMERA_DESCRIPTOR,
                                    Transacts.injectCamera, mContext.getPackageName(),
                                    "test-internal-cam",
                                    "test-external-cam", injectionCallback);

                        } else {
                            mTransacts.invokeTransact(Transacts.CAMERA_SERVICE,
                                    Transacts.CAMERA_DESCRIPTOR,
                                    Transacts.injectCamera, mContext.getPackageName(),
                                    "test-internal-cam",
                                    "test-external-cam", injectionCallback, injectionSession);
                        }

                    } catch (SecurityException e) {
                        throw e;
                    } catch (Exception e) {
                        //e.printStackTrace();
                        // If the test fails due to this package not holding the required permission
                        // a ServiceSpecificException is thrown with the text "Permission Denial"
                        String emessage = e.getMessage();
                        if(emessage != null && emessage.contains("Permission Denial")){
                            mLogger.logSystem(emessage);
                            throw new SecurityException(e);
                        } else {
                            throw e;
                        }
                    }
                }));

        // CAPTURE_BLACKOUT_CONTENT requires another app with FLAG_SECURE set.

        mPermissionTasks.put(permission.CLEAR_FREEZE_PERIOD,
                new PermissionTest(false, VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.DEVICE_POLICY_SERVICE,
                            Transacts.DEVICE_POLICY_DESCRIPTOR,
                            Transacts.clearSystemUpdatePolicyFreezePeriodRecord);
                }));

        mPermissionTasks.put(permission.CONTROL_DEVICE_STATE,
                new PermissionTest(false, VERSION_CODES.S,
                        VERSION_CODES.S, () -> {
                    @SuppressLint("WrongConstant") Object deviceStateManager
                            = mContext.getSystemService("device_state");
                    Class<?> deviceStateRequestClass = null;
                    try {
                        deviceStateRequestClass = Class.forName(
                                "android.hardware.devicestate.DeviceStateRequest");
                    } catch (ClassNotFoundException e) {
                        throw new UnexpectedPermissionTestFailureException(e);
                    }
                    mTransacts.invokeTransact(Transacts.DEVICE_STATE_SERVICE,
                            Transacts.DEVICE_STATE_DESCRIPTOR, Transacts.cancelRequest,
                            deviceStateRequestClass.cast(null));

                }));

        // CONTROL_OEM_PAID_NETWORK_PREFERENCE requires a device that supports automotive.

        mPermissionTasks.put(permission.FORCE_DEVICE_POLICY_MANAGER_LOGS,
                new PermissionTest(false, VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.DEVICE_POLICY_SERVICE,
                            Transacts.DEVICE_POLICY_DESCRIPTOR, Transacts.forceSecurityLogs);
                }));

        mPermissionTasks.put(permission.INPUT_CONSUMER,
                new PermissionTest(false, VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.WINDOW_SERVICE, Transacts.WINDOW_DESCRIPTOR,
                            Transacts.createInputConsumer, getActivityToken(), "test", 1, null);
                }));

        // INSTALL_TEST_ONLY_PACKAGE requires a package marked testOnly as well as the
        // INSTALL_PACKAGES permission to install without a user prompt; test may not make it to
        // INSTALL_TEST_ONLY_PACKAGE check before failing.

        mPermissionTasks.put(permission.KEEP_UNINSTALLED_PACKAGES,
                new PermissionTest(false, VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.PACKAGE_SERVICE,
                            Transacts.PACKAGE_DESCRIPTOR, Transacts.setKeepUninstalledPackages,
                            List.of("com.example.app"));
                }));

        //The test launches Intent
        mPermissionTasks.put(permission.MANAGE_CREDENTIAL_MANAGEMENT_APP,
                new PermissionTest(false, VERSION_CODES.S, () -> {

                    Intent intent = new Intent();
                    intent.setAction("android.security.IKeyChainService");
                    intent.setComponent(new ComponentName(Constants.KEY_CHAIN_PACKAGE,
                            Constants.KEY_CHAIN_PACKAGE + ".KeyChainService"));

                    if(Build.VERSION.SDK_INT >= VERSION_CODES.UPSIDE_DOWN_CAKE){
                        //AtomicBoolean foundError = new AtomicBoolean(false);
                        Thread thread = new Thread(() -> {
                            boolean permissionGranted =
                                    isPermissionGranted(permission.MANAGE_CREDENTIAL_MANAGEMENT_APP);
                            mLogger.logSystem("Running MANAGE_CREDENTIAL_MANAGEMENT_APP test case.");
                            try {
                                KeyChain.removeCredentialManagementApp(mContext);
                                getAndLogTestStatus(permission.MANAGE_CREDENTIAL_MANAGEMENT_APP,
                                        permissionGranted, true);
                            } catch (Exception ex){
                                if(ex.getClass().getSimpleName().equals("SecurityException")){
                                    getAndLogTestStatus(permission.MANAGE_CREDENTIAL_MANAGEMENT_APP,
                                            permissionGranted, false);
                                }
                            }
                        });
                        thread.start();
                        try {
                            thread.join(500);
                            throw new BypassTestException("The test launch on the new thread, it will finish after other test cases.");
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        mTransacts.invokeTransactWithServiceFromIntent(mContext, intent,
                                Transacts.KEY_CHAIN_DESCRIPTOR,
                                Transacts.removeCredentialManagementApp);
                    }
                }));

        mPermissionTasks.put(permission.MANAGE_GAME_MODE,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.GAME_SERVICE, Transacts.GAME_DESCRIPTOR,
                            Transacts.getAvailableGameModes, mContext.getPackageName());
                }));

        mPermissionTasks.put(permission.MANAGE_SMARTSPACE,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
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
                            parcel.writeTypedObject(UserHandle.getUserHandleForUid(mUid), 0);
                        }
                    };
                    mTransacts.invokeTransact(Transacts.SMART_SPACE_SERVICE,
                            Transacts.SMART_SPACE_DESCRIPTOR, Transacts.destroySmartspaceSession, smartspaceId);
                }));

        mPermissionTasks.put(permission.MANAGE_SPEECH_RECOGNITION,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    ComponentName componentName = new ComponentName(mContext, MainActivity.class);
                    mTransacts.invokeTransact(Transacts.SPEECH_RECOGNITION_SERVICE,
                            Transacts.SPEECH_RECOGNITION_DESCRIPTOR,
                            Transacts.setTemporaryComponent, componentName);
                }));

        mPermissionTasks.put(permission.MANAGE_TOAST_RATE_LIMITING,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.NOTIFICATION_SERVICE,
                            Transacts.NOTIFICATION_DESCRIPTOR,
                            Transacts.setToastRateLimitingEnabled, false);
                }));

        mPermissionTasks.put(permission.MANAGE_WIFI_COUNTRY_CODE,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.WIFI_SERVICE, Transacts.WIFI_DESCRIPTOR,
                            Transacts.setOverrideCountryCode, "ja");
                }));

        mPermissionTasks.put(permission.MODIFY_REFRESH_RATE_SWITCHING_TYPE,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.DISPLAY_SERVICE,
                            Transacts.DISPLAY_DESCRIPTOR, Transacts.setRefreshRateSwitchingType, 0);
                }));

        mPermissionTasks.put(permission.OVERRIDE_DISPLAY_MODE_REQUESTS,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.DISPLAY_SERVICE,
                            Transacts.DISPLAY_DESCRIPTOR,
                            Transacts.shouldAlwaysRespectAppRequestedMode);
                }));

        mPermissionTasks.put(permission.QUERY_AUDIO_STATE,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    Parcelable audioDeviceAttributes = new Parcelable() {
                        @Override
                        public int describeContents() {
                            return 0;
                        }

                        @Override
                        public void writeToParcel(Parcel parcel, int i) {
                            parcel.writeInt(2); // Role - AudioPort#ROLE_SINK
                            parcel.writeInt(2); // Type - AudioDeviceInfo#TYPE_BUILTIN_SPEAKER
                            parcel.writeString("test-address"); // address
                            parcel.writeInt(0); // native-type
                        }
                    };
                    mTransacts.invokeTransact(Transacts.AUDIO_SERVICE, Transacts.AUDIO_DESCRIPTOR,
                            Transacts.getDeviceVolumeBehavior, audioDeviceAttributes);
                }));

        mPermissionTasks.put(permission.READ_DREAM_SUPPRESSION,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    // Note, this transact requires both this permission and READ_DREAM_STATE,
                    // but this is the only transact that checks for READ_DREAM_SUPPRESSION, so it
                    // is included since the granted path can verify that the transact behaves
                    // as expected when both permissions are granted.
                    mTransacts.invokeTransact(Transacts.POWER_SERVICE, Transacts.POWER_DESCRIPTOR,
                            Transacts.isAmbientDisplaySuppressedForTokenByApp, "test-token", mUid);
                }));

        mPermissionTasks.put(permission.READ_PROJECTION_STATE,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.UI_MODE_SERVICE,
                            Transacts.UI_MODE_DESCRIPTOR, Transacts.getActiveProjectionTypes);
                }));

        mPermissionTasks.put(permission.RESET_APP_ERRORS,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.ACTIVITY_SERVICE,
                            Transacts.ACTIVITY_DESCRIPTOR, Transacts.resetAppErrors);
                }));

        mPermissionTasks.put(permission.SET_AND_VERIFY_LOCKSCREEN_CREDENTIALS,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    if (!mPackageManager.hasSystemFeature(
                            PackageManager.FEATURE_SECURE_LOCK_SCREEN)) {
                        throw new BypassTestException("This permission requires feature "
                                + PackageManager.FEATURE_SECURE_LOCK_SCREEN);
                    }
                    Parcelable lockscreenCredential = new Parcelable() {
                        @Override
                        public int describeContents() {
                            return 0;
                        }

                        @Override
                        public void writeToParcel(Parcel parcel, int i) {
                            // @see LockscreenCredential#createNone
                            parcel.writeInt(-1); // Type - LockPatternUtils#CREDENTIAL_TYPE_NONE
                            parcel.writeByteArray(new byte[0]); // Credential - empty byte array to
                            // check for no credentials set.
                        }
                    };
                    mTransacts.invokeTransact(Transacts.LOCK_SETTINGS_SERVICE,
                            Transacts.LOCK_SETTINGS_DESCRIPTOR, Transacts.verifyCredential,
                            lockscreenCredential, 0, 0);
                }));

        mPermissionTasks.put(permission.TEST_BIOMETRIC,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.AUTH_SERVICE, Transacts.AUTH_DESCRIPTOR,
                            Transacts.getUiPackage);
                }));

        // UNLIMITED_TOASTS requires MANAGE_TOAST_RATE_LIMITING to enable toast rate limiting then
        // need to be able to count the number of displayed toasts.

        mPermissionTasks.put(permission.UPDATE_DOMAIN_VERIFICATION_USER_SELECTION,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.DOMAIN_VERIFICATION_SERVICE,
                            Transacts.DOMAIN_VERIFICATION_DESCRIPTOR,
                            Transacts.setDomainVerificationLinkHandlingAllowed, mPackageName, false,
                            0);
                }));

        // VIRTUAL_INPUT_DEVICE does not appear to be used in AOSP.

        // USE_SYSTEM_DATA_LOADERS requires both USE_INSTALLER_V2 to set the DataLoaderParams along
        // with access to the hidden DataLoaderParams class.

        mPermissionTasks.put(permission.MANAGE_ONGOING_CALLS,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    // Note, MANAGE_ONGOING_CALLS is only used to determine the UI type when
                    // the system binds to an in-call app, but this API at least performs a
                    // permission grant check.
                    boolean permissionGranted = false;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                        permissionGranted = mTelecomManager.hasManageOngoingCallsPermission();

                        if (!permissionGranted) {
                            throw new SecurityException(
                                    mPackageName + " has not been granted MANAGE_ONGOING_CALLS");
                        }
                    }
                }));

        mPermissionTasks.put(permission.USE_ICC_AUTH_WITH_DEVICE_IDENTIFIER,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    SubscriptionManager subscriptionManager = mContext.getSystemService(
                            SubscriptionManager.class);
                    List<SubscriptionInfo> subInfos;
                    try {
                        subInfos = subscriptionManager.getActiveSubscriptionInfoList();
                    } catch (SecurityException e) {
                        // A SecurityException indicates the app does not have permission to query
                        // the active SubscriptionInfo instances; without these the test cannot
                        // be run.
                        subInfos = null;
                    }
                    if (subInfos == null || subInfos.size() == 0) {
                        throw new BypassTestException(
                                "This permission requires that the app be able to query at least "
                                        + "one active subscription");
                    }
                    // The data parameter is supposed to be a base64 encoded value; the encoded
                    // value used for this invocation is "test".
                    mTelephonyManager.getIccAuthentication(TelephonyManager.APPTYPE_SIM,
                            TelephonyManager.AUTHTYPE_EAP_SIM, "dGVzdAo=");
                }));

        // MANAGE_MEDIA does not give read or write access directly but instead just controls
        // whether the user is prompted to confirm the write request.

        // MANAGE_APP_HIBERNATION requires that app hibernation be enabled via DeviceConfig;
        // this can be queried via
        // `adb shell device_config get app_hibernation app_hibernation_enabled`. This was disabled
        // on the test device, and this requires READ_DEVICE_CONFIG to read so it cannot be
        // queried as part of the test. Since the app hibernation enabled check occurs before the
        // permission check there is no way to determine whether the successful response is due to
        // the permission being reported as granted or that it is not enabled.

        mPermissionTasks.put(permission.MANAGE_NOTIFICATION_LISTENERS,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.NOTIFICATION_SERVICE,
                            Transacts.NOTIFICATION_DESCRIPTOR,
                            Transacts.getEnabledNotificationListeners, 0);
                }));

        mPermissionTasks.put(permission.BATTERY_PREDICTION,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    // Note, this is the only transact that checks for this permission, but it also
                    // checks for the DEVICE_POWER permission. First the permission under test is
                    // checked, then if that fails an enforcePermission call is invoked for
                    // DEVICE_POWER; this is why the SecurityException for this transact only
                    // reports the DEVICE_POWER permission even though both are checked.
                    Parcelable parcelDuration = new Parcelable() {
                        @Override
                        public int describeContents() {
                            return 0;
                        }

                        @Override
                        public void writeToParcel(Parcel parcel, int i) {
                            Duration duration = Duration.ofDays(1);
                            parcel.writeLong(duration.getSeconds());
                            parcel.writeInt(duration.getNano());
                        }
                    };
                    mTransacts.invokeTransact(Transacts.POWER_SERVICE, Transacts.POWER_DESCRIPTOR,
                            Transacts.setBatteryDischargePrediction, parcelDuration, false);
                }));

        // CAPTURE_TUNER_AUDIO_INPUT requires a tuner device, and no code in AOSP checks for this
        // permission.

        // DISABLE_SYSTEM_SOUND_EFFECTS can be used to disable system sound effects when the user
        // exits one of the app's activities, but only determines whether the platform plays a
        // sound with no APIs or signals to determine whether or not the sound played.

        // INSTALL_LOCATION_TIME_ZONE_PROVIDER_SERVICE must be granted to an app that wants to
        // extend TimeZoneProviderService to be installed as the time zone provider.

        mPermissionTasks.put(permission.MANAGE_TIME_AND_ZONE_DETECTION,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.TIME_DETECTOR_SERVICE,
                            Transacts.TIME_DETECTOR_DESCRIPTOR,
                            Transacts.getCapabilitiesAndConfig, 0);
                }));

        mPermissionTasks.put(permission.NFC_SET_CONTROLLER_ALWAYS_ON,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    if (!mPackageManager.hasSystemFeature(PackageManager.FEATURE_NFC)) {
                        throw new BypassTestException("This permission requires feature "
                                + PackageManager.FEATURE_NFC);
                    }
                    mTransacts.invokeTransact(Transacts.NFC_SERVICE, Transacts.NFC_DESCRIPTOR,
                            Transacts.isControllerAlwaysOnSupported);
                }));

        mPermissionTasks.put(permission.OVERRIDE_COMPAT_CHANGE_CONFIG_ON_RELEASE_BUILD,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    Parcelable compatOverridesToRemoveConfig = new Parcelable() {
                        @Override
                        public int describeContents() {
                            return 0;
                        }

                        @Override
                        public void writeToParcel(Parcel parcel, int i) {
                            parcel.writeInt(0); // Number of changeIds to be removed.
                        }
                    };
                    mTransacts.invokeTransact(Transacts.PLATFORM_COMPAT_SERVICE,
                            Transacts.PLATFORM_COMPAT_DESCRIPTOR,
                            Transacts.removeOverridesOnReleaseBuilds, compatOverridesToRemoveConfig,
                            mPackageName);
                }));

        //This permission's category has been moved to install after Android T
        //The test leave it as is for the previous releases.
        mPermissionTasks.put(permission.READ_NEARBY_STREAMING_POLICY,
                new PermissionTest(false, Build.VERSION_CODES.S, VERSION_CODES.S_V2, () -> {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                        // DevicePolicyManagerService first checks if this feature is available before
                        // performing the permission check.
                        if (!mPackageManager.hasSystemFeature(PackageManager.FEATURE_DEVICE_ADMIN)) {
                            throw new BypassTestException("This permission requires feature "
                                    + PackageManager.FEATURE_DEVICE_ADMIN);
                        }
                        mTransacts.invokeTransact(Transacts.DEVICE_POLICY_SERVICE,
                                Transacts.DEVICE_POLICY_DESCRIPTOR,
                                Transacts.getNearbyNotificationStreamingPolicy, 0);
                    }
                }));

        mPermissionTasks.put(permission.REGISTER_MEDIA_RESOURCE_OBSERVER,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    IBinder binder = new IBinder() {
                        @Nullable
                        @Override
                        public String getInterfaceDescriptor() throws RemoteException {
                            return "android.media.IResourceObserver";
                        }

                        @Override
                        public boolean pingBinder() {
                            return false;
                        }

                        @Override
                        public boolean isBinderAlive() {
                            return false;
                        }

                        @Nullable
                        @Override
                        public IInterface queryLocalInterface(@NonNull String s) {
                            return null;
                        }

                        @Override
                        public void dump(@NonNull FileDescriptor fileDescriptor,
                                         @Nullable String[] strings) throws RemoteException {

                        }

                        @Override
                        public void dumpAsync(@NonNull FileDescriptor fileDescriptor,
                                              @Nullable String[] strings) throws RemoteException {

                        }

                        @Override
                        public boolean transact(int i, @NonNull Parcel parcel,
                                                @Nullable Parcel parcel1, int i1) throws RemoteException {
                            return false;
                        }

                        @Override
                        public void linkToDeath(@NonNull DeathRecipient deathRecipient, int i)
                                throws RemoteException {

                        }

                        @Override
                        public boolean unlinkToDeath(@NonNull DeathRecipient deathRecipient,
                                                     int i) {
                            return false;
                        }
                    };
                    try {
                        mTransacts.invokeTransact(Transacts.RESOURCE_OBSERVER_SERVICE,
                                Transacts.RESOURCE_OBSERVER_DESCRIPTOR,
                                Transacts.unregisterObserver, binder);
                    } catch (Exception e) {
                        // The following was logged and caught when the permission was not granted;
                        // the code -1 signifies PERMISSION_DENIED.
                        // ServiceManager: Permission failure: android.permission
                        //   .REGISTER_MEDIA_RESOURCE_OBSERVER from uid=10430 pid=31259
                        // ResourceObserverService: Permission Denial: can't unregisterObserver
                        //   from pid=31259, uid=10430
                        // SignaturePermissionTester: android.os.ServiceSpecificException: (code -1)

                        // Note, the caught exception is a ServiceSpecificException which is hidden
                        // from external apps; these Exceptions have an errorCode that is not part
                        // of the message but is shown in #toString.
                        if (e.toString().contains("code -1")) {
                            throw new SecurityException("Transact failed with PERMISSION_DENIED",
                                    e);
                        } else {
                            // any other exceptions should be logged for debugging in case other
                            // devices use a different error code for PERMISSION_DENIED.

                            mLogger.logDebug("Intended Exception : Caught an exception invoking the transact: ", e);
                        }
                    }
                }));

        // RENOUNCE_PERMISSIONS requires an AttributionSource during the permission check that
        // contains a Set of permissions to renounce; if the permission being checked has been
        // granted but the Set contains this permissions and the RENOUNCE_PERMISSIONS permission
        // has been granted then it just reports that the permission is not granted.

        mPermissionTasks.put(permission.RESTART_WIFI_SUBSYSTEM,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    // This test may require some amount of sleep time after a successful run in
                    // case other tests rely on the wifi subsystem and it has not yet fully
                    // restarted.
                    mTransacts.invokeTransact(Transacts.WIFI_SERVICE, Transacts.WIFI_DESCRIPTOR,
                            Transacts.restartWifiSubsystem);

                }));

        mPermissionTasks.put(permission.SCHEDULE_PRIORITIZED_ALARM,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    final int FLAG_PRIORITIZE = 1 << 6; // As defined in AlarmManager
                    // Set the start time to be a day from now
                    long windowStartMs = System.currentTimeMillis() + 60 * 60 * 24 * 1000;
                    // null values are used to prevent an alarm from actually triggering.
                    mTransacts.invokeTransact(Transacts.ALARM_SERVICE, Transacts.ALARM_DESCRIPTOR,
                            Transacts.set, mPackageName, 0, windowStartMs, 0, 0, FLAG_PRIORITIZE,
                            null, null, null, null, null);
                }));

        mPermissionTasks.put(permission.SEND_CATEGORY_CAR_NOTIFICATIONS,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    if (!mPackageManager.hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE)) {
                        throw new BypassTestException("This permission requires the feature "
                                + PackageManager.FEATURE_AUTOMOTIVE);
                    }
                    Resources resources = mContext.getResources();
                    CharSequence channelName = resources.getString(R.string.tester_channel_name);
                    NotificationChannel channel = new NotificationChannel(TAG, channelName,
                            NotificationManager.IMPORTANCE_DEFAULT);
                    NotificationManager notificationManager = mContext.getSystemService(
                            NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);

                    Intent notificationIntent = new Intent(mContext, MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
                            notificationIntent,
                            PendingIntent.FLAG_IMMUTABLE);
                    Notification notification =
                            new Notification.Builder(mContext, TAG)
                                    .setContentTitle(
                                            resources.getText(R.string.status_notification_title))
                                    .setContentText(
                                            resources.getString(R.string.test_notification_message))
                                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                                    .setContentIntent(pendingIntent)
                                    .setAutoCancel(true)
                                    .setCategory("car_emergency")
                                    .build();
                    notificationManager.notify(0, notification);
                }));

        mPermissionTasks.put(permission.SIGNAL_REBOOT_READINESS,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.REBOOT_READINESS_SERVICE,
                            Transacts.REBOOT_READINESS_DESCRIPTOR,
                            Transacts.removeRequestRebootReadinessStatusListener, getActivityToken());
                }));

        mPermissionTasks.put(permission.SOUNDTRIGGER_DELEGATE_IDENTITY,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    Parcelable identity = new Parcelable() {
                        @Override
                        public int describeContents() {
                            return 0;
                        }

                        @Override
                        public void writeToParcel(Parcel parcel, int i) {
                            int start_pos = parcel.dataPosition();
                            parcel.writeInt(0);
                            parcel.writeInt(mUid); // uid
                            parcel.writeInt(Binder.getCallingPid()); // pid
                            parcel.writeString(mPackageName); // packageName
                            parcel.writeString("test-attribution"); // attributionTag
                            int end_pos = parcel.dataPosition();
                            parcel.setDataPosition(start_pos);
                            parcel.writeInt(end_pos - start_pos);
                            parcel.setDataPosition(end_pos);
                        }
                    };

                    mTransacts.invokeTransact(Transacts.SOUND_TRIGGER_SERVICE,
                            Transacts.SOUND_TRIGGER_DESCRIPTOR, Transacts.attachAsMiddleman,
                            identity, identity, getActivityToken());
                }));

        // SOUND_TRIGGER_RUN_IN_BATTERY_SAVER requires an initial transact to obtain an instance
        // of ISoundTriggerSession; this service exposes #startRecognition with parameter
        // runInBatterySaver which can be set to true to test this permission, but after obtaining
        // the instance there is no way to invoke the method since it is guarded by the reflection
        // deny-list.



        mPermissionTasks.put(permission.SUGGEST_EXTERNAL_TIME,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    //android.app.time.ExternalTimeSuggestion
                    mTransacts.invokeTransact(Transacts.TIME_DETECTOR_SERVICE,
                            Transacts.TIME_DETECTOR_DESCRIPTOR, Transacts.suggestExternalTime,
                            (Object) null);
                }));

        mPermissionTasks.put(permission.TOGGLE_AUTOMOTIVE_PROJECTION,
                new PermissionTest(false, Build.VERSION_CODES.S,
                        Build.VERSION_CODES.S_V2, () -> {
                    mTransacts.invokeTransact(Transacts.UI_MODE_SERVICE,
                            Transacts.UI_MODE_DESCRIPTOR, Transacts.requestProjection,
                            genericIBinder, 1, mPackageName);
                }));

        mPermissionTasks.put(permission.UPDATE_FONTS,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.FONT_SERVICE, Transacts.FONT_DESCRIPTOR,
                            Transacts.getFontConfig);
                }));

        mPermissionTasks.put(permission.UWB_PRIVILEGED,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.UWB_SERVICE, Transacts.UWB_DESCRIPTOR,
                            Transacts.getSpecificationInfo);
                }));

        // CONTROL_UI_TRACING - does not appear to be used in AOSP.

        // ACCESS_BLOBS_ACROSS_USERS requires setting up a new BlobHandle in a separate user
        // which cannot be automated without the rest of the signature permissions granted.

        mPermissionTasks.put(permission.BROADCAST_CLOSE_SYSTEM_DIALOGS,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    Intent intent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                    mContext.sendBroadcast(intent);
                }));

        mPermissionTasks.put(permission.MANAGE_MUSIC_RECOGNITION,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    //RecognitionRequest,IBinder
                    mTransacts.invokeTransact(Transacts.MUSIC_RECOGNITION_SERVICE,
                            Transacts.MUSIC_RECOGNITION_DESCRIPTOR, Transacts.beginRecognition,
                            null, null);
                }));

        mPermissionTasks.put(permission.MANAGE_UI_TRANSLATION,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.TRANSLATION_SERVICE,
                            Transacts.TRANSLATION_DESCRIPTOR, Transacts.updateUiTranslationState, 0,
                            null, null, null, new Binder(), 0, null, 0);
                }));

        mPermissionTasks.put(permission.ACCESS_TUNED_INFO,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.TV_INPUT_SERVICE,
                            Transacts.TV_INPUT_DESCRIPTOR, Transacts.getCurrentTunedInfos, 0);
                }));

        mPermissionTasks.put(permission.GET_PEOPLE_TILE_PREVIEW,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    Bundle param = new Bundle();
                    mContentResolver.call(
                            Uri.parse("content://com.android.systemui.people.PeopleProvider"),
                            "get_people_tile_preview", null, param);
                }));

        mPermissionTasks.put(permission.MANAGE_ACTIVITY_TASKS,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        //stopAppForUser(final String packageName, int userId)
                        mTransacts.invokeTransact(Transacts.ACTIVITY_SERVICE,
                                Transacts.ACTIVITY_DESCRIPTOR,
                                Transacts.stopAppForUser,"test.packagename",0);
                    } else {
                        mTransacts.invokeTransact(Transacts.ACTIVITY_TASK_SERVICE,
                                Transacts.ACTIVITY_TASK_DESCRIPTOR,
                                Transacts.getWindowOrganizerController);
                    }

                }));

        // ROTATE_SURFACE_FLINGER - does not appear to be a good way to reach this from
        // external apps.

        mPermissionTasks.put(permission.SET_CLIP_SOURCE,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.CLIPBOARD_SERVICE,
                            Transacts.CLIPBOARD_DESCRIPTOR, Transacts.getPrimaryClipSource,
                            mPackageName, 0);
                }));

        mPermissionTasks.put(permission.READ_PEOPLE_DATA,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.PEOPLE_SERVICE, Transacts.PEOPLE_DESCRIPTOR,
                            Transacts.isConversation, mPackageName, 0, "test-shortcut-id");
                }));

        // MANAGE_SEARCH_UI is a permission that must be required by a SearchUiService.

        mPermissionTasks.put(permission.WIFI_ACCESS_COEX_UNSAFE_CHANNELS,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    //WifiManager.CoexCallback()
                    mTransacts.invokeTransact(Transacts.WIFI_SERVICE, Transacts.WIFI_DESCRIPTOR,
                            Transacts.unregisterCoexCallback,(Object) null);

                }));

        mPermissionTasks.put(permission.WIFI_UPDATE_COEX_UNSAFE_CHANNELS,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.WIFI_SERVICE, Transacts.WIFI_DESCRIPTOR,
                            Transacts.setCoexUnsafeChannels, null, 0);
                }));

        ////////////////////////////////////////////////////////////////////////////////
        //New Signature permissions as of Android 12 SV2

        // # Skip ALLOW_SLIPPERY_TOUCHES
        // Reason : The permission is Infeasible to test

        mPermissionTasks.put(permission.TRIGGER_SHELL_PROFCOLLECT_UPLOAD,
                new PermissionTest(false, Build.VERSION_CODES.S_V2, () -> {

                    //behaviour changes?
                    runShellCommandTest(
                            "am broadcast --allow-background-activity-starts " +
                                    "-a com.android.shell.action.PROFCOLLECT_UPLOAD");
                }));

    }
    void prepareTestBlockForT(){
        ////////////////////////////////////////////////////////////////////////////////
        // New Signature permissions as of TIRAMISU(13/SDK33)

        mPermissionTasks.put(permission.LOCATION_BYPASS,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    invokeReflectionCall(mLocationManager.getClass(),
                            "setAdasGnssLocationEnabled",mLocationManager,
                            new Class<?>[]{boolean.class}, true);
                }));

        mPermissionTasks.put(permission.CONTROL_AUTOMOTIVE_GNSS,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    if (!mPackageManager.hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE)) {
                        throw new BypassTestException("This permission requires feature "
                                + PackageManager.FEATURE_AUTOMOTIVE);
                    }
                    invokeReflectionCall(mLocationManager.getClass(),
                            "isAutomotiveGnssSuspended",mLocationManager,null);
                }));

        mPermissionTasks.put(permission.MANAGE_WIFI_NETWORK_SELECTION,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    invokeReflectionCall(mWifiManager.getClass(),
                            "getSsidsAllowlist",mWifiManager,null);
                }));

        mPermissionTasks.put(permission.MANAGE_WIFI_INTERFACES,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        mWifiManager.reportCreateInterfaceImpact(WifiManager.WIFI_INTERFACE_TYPE_STA,
                                true, new Executor() {
                                    @Override
                                    public void execute(Runnable runnable) {}
                                }, new BiConsumer<Boolean, Set<WifiManager.InterfaceCreationImpact>>() {
                                    @Override
                                    public void accept(Boolean aBoolean, Set<WifiManager.InterfaceCreationImpact> interfaceCreationImpacts) {}
                                });
                    }
                }));

        mPermissionTasks.put(permission.TRIGGER_LOST_MODE,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    try {
                        invokeReflectionCall(mDevicePolicyManager.getClass(),
                                "sendLostModeLocationUpdate", mDevicePolicyManager,
                                new Class<?>[]{Executor.class, Consumer.class}, new Executor() {
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
                    } catch(UnexpectedPermissionTestFailureException e){
                        //The error is intended
                        //Lost mode location updates can only be sent on an organization-owned device.
                    }
                }));

        // # Skip START_CROSS_PROFILE_ACTIVITIES
        // Reason : CrossProfileAppService requires Android Enterprise Environment.

        mPermissionTasks.put(permission.QUERY_USERS,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    invokeReflectionCall(mUserManager.getClass(),
                            "getUserRestrictionSource", mUserManager,
                            new Class<?>[]{java.lang.String.class, android.os.UserHandle.class},
                            "Hello",UserHandle.getUserHandleForUid(0));
                }));

        mPermissionTasks.put(permission.QUERY_ADMIN_POLICY,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {

                    String devicePolicySettings="true";

                    if(Build.VERSION.SDK_INT>=VERSION_CODES.UPSIDE_DOWN_CAKE){
                        devicePolicySettings = ReflectionUtils.deviceConfigGetProperty("device_policy_manager", "enable_permission_based_access");
                        ReflectionUtils.deviceConfigSetProperty("device_policy_manager", "enable_permission_based_access","false",false);
                    }

                    invokeReflectionCall(mDevicePolicyManager.getClass(),
                            "getWifiSsidPolicy", mDevicePolicyManager,
                            new Class<?>[]{});

                    //Revert the settings
                    if(Build.VERSION.SDK_INT>=VERSION_CODES.UPSIDE_DOWN_CAKE){
                        ReflectionUtils.deviceConfigSetProperty("device_policy_manager", "enable_permission_based_access",devicePolicySettings,true);

                    }

                }));
        //The test can not execute after Android 15 beta 4
        mPermissionTasks.put(permission.PROVISION_DEMO_DEVICE,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {

                    Class<?> fmdpBuilderClazz = null;
                    Class<?> fmdpClazz = null;
                    try {
                        fmdpBuilderClazz = Class.forName("android.app.admin.FullyManagedDeviceProvisioningParams$Builder");
                        fmdpClazz = Class.forName("android.app.admin.FullyManagedDeviceProvisioningParams");

                        Constructor constructor = fmdpBuilderClazz.getConstructor(ComponentName.class,String.class);
                        Object fmdpBuilderObj =
                                constructor.newInstance(new ComponentName(mContext,MainActivity.class),"");
                        Object fmdpObj = invokeReflectionCall(fmdpBuilderClazz,
                                "build", fmdpBuilderObj,
                                new Class<?>[]{});
                        invokeReflectionCall(mDevicePolicyManager.getClass(),
                                "provisionFullyManagedDevice", mDevicePolicyManager,
                                new Class<?>[]{fmdpClazz},fmdpObj);
                    } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                        throw new UnexpectedPermissionTestFailureException(e);
                    } catch (UnexpectedPermissionTestFailureException e){
                        if(e.getMessage().equals("java.lang.reflect.InvocationTargetException")){
                            boolean foundExpected = ReflectionUtils.
                                    findCauseInStackTraceElement(false,e,
                                            "android.os.ServiceSpecificException");
                            if(!foundExpected) throw e;
                        } else {
                            throw e;
                        }
                    }
                }));

        ////////////////////////////////////////////////////////////
        //REQUEST_COMPANION_PROFILE_* methods

        mPermissionTasks.put(permission.REQUEST_COMPANION_PROFILE_APP_STREAMING,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    if (!mPackageManager.hasSystemFeature(
                            PackageManager.FEATURE_COMPANION_DEVICE_SETUP)) {
                        throw new BypassTestException(
                                "Device does not have the "
                                        + "PackageManager#FEATURE_COMPANION_DEVICE_SETUP feature "
                                        + "for this test");
                    }
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        AssociationRequest request = null;
                        request = new AssociationRequest.Builder().setDeviceProfile(
                                AssociationRequest.DEVICE_PROFILE_APP_STREAMING).build();

                        CompanionDeviceManager.Callback callback =
                                new CompanionDeviceManager.Callback() {
                                    @Override
                                    public void onDeviceFound(IntentSender intentSender) {
                                        mLogger.logDebug(
                                                "onDeviceFound: intentSender = " + intentSender);
                                    }

                                    @Override
                                    public void onFailure(CharSequence charSequence) {
                                        mLogger.logDebug("onFailure: charSequence = " + charSequence);
                                    }
                                };
                        CompanionDeviceManager companionDeviceManager = mActivity.getSystemService(
                                CompanionDeviceManager.class);
                        companionDeviceManager.associate(request, callback, null);
                    }
                }));

        mPermissionTasks.put(permission.REQUEST_COMPANION_PROFILE_COMPUTER,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {

                    if (!mPackageManager.hasSystemFeature(
                            PackageManager.FEATURE_COMPANION_DEVICE_SETUP)) {
                        throw new BypassTestException(
                                "Device does not have the "
                                        + "PackageManager#FEATURE_COMPANION_DEVICE_SETUP feature "
                                        + "for this test");
                    }
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        AssociationRequest request = null;
                        request = new AssociationRequest.Builder().setDeviceProfile(
                                AssociationRequest.DEVICE_PROFILE_COMPUTER).build();

                        CompanionDeviceManager.Callback callback =
                                new CompanionDeviceManager.Callback() {
                                    @Override
                                    public void onDeviceFound(IntentSender intentSender) {
                                        mLogger.logDebug(
                                                "onDeviceFound: intentSender = " + intentSender);
                                    }

                                    @Override
                                    public void onFailure(CharSequence charSequence) {
                                        mLogger.logDebug("onFailure: charSequence = " + charSequence);
                                    }
                                };
                        CompanionDeviceManager companionDeviceManager = mActivity.getSystemService(
                                CompanionDeviceManager.class);
                        companionDeviceManager.associate(request, callback, null);
                    }
                }));

        mPermissionTasks.put(permission.REQUEST_COMPANION_SELF_MANAGED,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    if (!mPackageManager.hasSystemFeature(
                            PackageManager.FEATURE_COMPANION_DEVICE_SETUP)) {
                        throw new BypassTestException(
                                "Device does not have the "
                                        + "PackageManager#FEATURE_COMPANION_DEVICE_SETUP feature "
                                        + "for this test");
                    }
                    AssociationRequest request = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        request = new AssociationRequest.Builder().setDisplayName("foo")
                                .setSelfManaged(true).setForceConfirmation(true).build();
                    } else {
                        throw new BypassTestException("REQUEST_COMPANION_SELF_MANAGED tests requires SDK 33");
                    }

                    CompanionDeviceManager.Callback callback =
                            new CompanionDeviceManager.Callback() {
                                @Override
                                public void onDeviceFound(IntentSender intentSender) {
                                    mLogger.logDebug(
                                            "onDeviceFound: intentSender = " + intentSender);
                                }
                                @Override
                                public void onFailure(CharSequence charSequence) {
                                    mLogger.logDebug("onFailure: charSequence = " + charSequence);
                                }
                            };
                    CompanionDeviceManager companionDeviceManager = mActivity.getSystemService(
                            CompanionDeviceManager.class);
                    companionDeviceManager.associate(request, callback, null);
                }));

        mPermissionTasks.put(permission.READ_APP_SPECIFIC_LOCALES,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    //if the caller is not an owner of the application, the api raise a security exception.
                    try {
                        //Check companion app
                        mLocaleManager.getApplicationLocales(
                                "com.android.certifications.niap.permissions.companion");
                    } catch (IllegalArgumentException ex){
                        //if signed signature is not a platform one, the api may not find the package.
                        throw new SecurityException(ex);
                    }
                }));

        mPermissionTasks.put(permission.USE_ATTESTATION_VERIFICATION_SERVICE,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    // in VerificationToken token,in ParcelDuration maximumTokenAge,in AndroidFuture resultCallback
                    // Intended NPE will be raised
                    mTransacts.invokeTransact(Transacts.ATTESTATION_VERIFICATION_SERVICE,
                            Transacts.ATTESTATION_VERIFICATION_DESCRIPTOR,
                            Transacts.verifyToken, null,null,null);
                }));

        // # Skip VERIFY_ATTESTATION
        // Reason : Found No corresponding API or implementation to test.

        mPermissionTasks.put(permission.REQUEST_UNIQUE_ID_ATTESTATION,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    String keystoreAlias = "test_key";
                    KeyGenParameterSpec.Builder builder =
                            new KeyGenParameterSpec.Builder(keystoreAlias, PURPOSE_SIGN)
                                    .setAlgorithmParameterSpec(new ECGenParameterSpec("secp256r1"))
                                    .setDigests(DIGEST_NONE, DIGEST_SHA256, DIGEST_SHA512)
                                    .setAttestationChallenge(new byte[128]);
                    //setUniqueIeIncluded is a hidden api
                    builder = (KeyGenParameterSpec.Builder)invokeReflectionCall(builder.getClass(),
                            "setUniqueIdIncluded",builder,
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
                    } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | NoSuchProviderException e) {
                        throw new UnexpectedPermissionTestFailureException(e);
                    }
                }));

        // # Skip GET_HISTORICAL_APP_OPS_STATS
        // Reason : The corresponding api hasn't raise any exception internally.(AppOpsService#getHistoricalOps)

        mPermissionTasks.put(permission.SET_SYSTEM_AUDIO_CAPTION,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    invokeReflectionCall(mCaptioningManager.getClass(),
                            "setSystemAudioCaptioningEnabled",mCaptioningManager,
                            new Class<?>[]{boolean.class}, true);
                }));


        // # Skip INSTALL_DPC_PACKAGES
        // Reason : Found no way to evaluate this permission without enterprise environment

        //The Permission required for CTS test - Notification test suite - need platform signature to run

        mPermissionTasks.put(permission.REVOKE_POST_NOTIFICATIONS_WITHOUT_KILL,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    @SuppressLint("WrongConstant") Object permissionManager = mContext.getSystemService("permission");
                    //revokePostNotificationPermissionWithoutKillForTest( java.lang.String int);
                    invokeReflectionCall(permissionManager.getClass(),
                            "revokePostNotificationPermissionWithoutKillForTest",permissionManager,
                            new Class<?>[]{String.class,int.class}, mContext.getPackageName(),0);
                }));


        // # Skip DELIVER_COMPANION_MESSAGES
        // Reason : Found No corresponding API or implementation to test.

        // # Skip MODIFY_TOUCH_MODE_STATE
        //Reason : setInTouchMode has not raise any exception. no way to evaluate this permissison.

        mPermissionTasks.put(permission.MODIFY_USER_PREFERRED_DISPLAY_MODE,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    Display.Mode mode = mDisplayManager.getDisplays()[0].getMode();

                    //The method may not find if the app is signing by the platform signing key
                    //mLogger.logInfo(ReflectionUtils.checkDeclaredMethod(mDisplayManager,"setGlobalUser").toString());
                    //invokeReflectionCall(mDisplayManager.getClass(),
                    //        "setGlobalUserPreferredDisplayMode", mDisplayManager,
                    //        new Class[]{Display.Mode.class},mode);

                    mTransacts.invokeTransact(Transacts.DISPLAY_SERVICE,Transacts.DISPLAY_DESCRIPTOR,
                            Transacts.setUserPreferredDisplayMode,0,mode);
                }));

        mPermissionTasks.put(permission.ACCESS_ULTRASOUND,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    invokeReflectionCall(mAudioManager.getClass(),
                            "isUltrasoundSupported", mAudioManager,
                            new Class[]{});
                }));

        mPermissionTasks.put(permission.CALL_AUDIO_INTERCEPTION,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    invokeReflectionCall(mAudioManager.getClass(),
                            "isPstnCallAudioInterceptable", mAudioManager,
                            new Class[]{});
                }));

        mPermissionTasks.put(permission.MANAGE_LOW_POWER_STANDBY,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    invokeReflectionCall(mPowerManager.getClass(),
                            "isLowPowerStandbySupported", mPowerManager,
                            new Class[]{});
                }));

        mPermissionTasks.put(permission.ACCESS_BROADCAST_RESPONSE_STATS,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    invokeReflectionCall(mUsageStatsManager.getClass(),
                            "queryBroadcastResponseStats", mUsageStatsManager,
                            new Class[]{String.class,long.class},mContext.getPackageName(),0);
                }));

        mPermissionTasks.put(permission.CHANGE_APP_LAUNCH_TIME_ESTIMATE,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    invokeReflectionCall(mUsageStatsManager.getClass(),
                            "setEstimatedLaunchTimeMillis", mUsageStatsManager,
                            new Class[]{String.class,long.class},mContext.getPackageName(),1000L);
                }));

        mPermissionTasks.put(permission.MANAGE_WEAK_ESCROW_TOKEN,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    if (!mPackageManager.hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE)) {
                        throw new BypassTestException("This permission requires feature "
                                + PackageManager.FEATURE_AUTOMOTIVE);
                    }
                    invokeReflectionCall(mKeyguardManager.getClass(),
                            "isWeakEscrowTokenActive", mKeyguardManager,
                            new Class[]{long.class, UserHandle.class},
                            100L, UserHandle.getUserHandleForUid(mUid));
                }));

        mPermissionTasks.put(permission.SET_WALLPAPER_DIM_AMOUNT,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    WallpaperManager wallpaperManager =
                            (WallpaperManager) mContext.getSystemService(
                                    Context.WALLPAPER_SERVICE);

                    invokeReflectionCall(wallpaperManager.getClass(),
                            "getWallpaperDimAmount", wallpaperManager,
                            new Class[]{});
                }));

        //TODO : Suspicious
        mPermissionTasks.put(permission.START_REVIEW_PERMISSION_DECISIONS,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
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

                }));

        // # Skip START_VIEW_APP_FEATURES
        //Reason :Found No Corresponding activity for the Intent
        //android.permission.action.ACTION_VIEW_APP_FEATURES.

        mPermissionTasks.put(permission.MANAGE_CLOUDSEARCH,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    //in SearchRequest request, in ICloudSearchManagerCallback.aidl callBack
                    Class<?> clazzSearchBuilder = null;
                    Object searchRequest = null;
                    try {
                        clazzSearchBuilder = Class.forName("android.app.cloudsearch.SearchRequest$Builder");
                        Constructor constructor = clazzSearchBuilder.getConstructor(String.class);
                        Object builderObj = constructor.newInstance("test");
                        searchRequest = invokeReflectionCall(clazzSearchBuilder,
                                "build", builderObj,
                                new Class[]{});

                        mTransacts.invokeTransact(Transacts.CLOUDSEARCH_SERVICE, Transacts.CLOUDSEARCH_DESCRIPTOR,
                                Transacts.search,searchRequest,null);
                    } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
                        throw new UnexpectedPermissionTestFailureException(e);
                    }
                }));

        mPermissionTasks.put(permission.MANAGE_WALLPAPER_EFFECTS_GENERATION,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    mTransacts.invokeTransact(Transacts.WALLPAPER_EFFECTS_GENERATION_SERVICE,
                            Transacts.WALLPAPER_EFFECTS_GENERATION_DESCRIPTOR,
                            Transacts.generateCinematicEffect,
                            null,null);
                }));


        // # Skip SUPPRESS_CLIPBOARD_ACCESS_NOTIFICATION
        //Reason : Found no proper way to check this permission.
        //The permission only suppress the toast for clipboard access.

        // # Skip ACCESS_TV_SHARED_FILTER
        //Reason : Need TV tuner on the device for testing.(Tuner#openSharedFilter)

        // # Skip ADD_ALWAYS_UNLOCKED_DISPLAY
        //Reason : The corresponding api
        //VirtualDeviceParams.Builder#setLockState hasn't raise an exception.

        mPermissionTasks.put(permission.SET_GAME_SERVICE,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    mTransacts.invokeTransact(Transacts.GAME_SERVICE, Transacts.GAME_DESCRIPTOR,
                            Transacts.setGameServiceProvider, mContext.getPackageName());
                }));
        mPermissionTasks.put(permission.ACCESS_FPS_COUNTER,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    mTransacts.invokeTransact(Transacts.WINDOW_SERVICE, Transacts.WINDOW_DESCRIPTOR,
                            Transacts.registerTaskFpsCallback, 0,null);
                }));

        //TODO: (Suspicous)
        mPermissionTasks.put(permission.MANAGE_GAME_ACTIVITY,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {

                    mTransacts.invokeTransact(Transacts.ACTIVITY_TASK_SERVICE, Transacts.ACTIVITY_TASK_DESCRIPTOR,
                            Transacts.startActivityFromGameSession,
                            getActivityToken(),mContext.getPackageName(),"",
                            0,0,new Intent().setClass(mContext, TestActivity.class),0,0);
                }));

        //TODO: (Suspicious) Start Activity
        mPermissionTasks.put(permission.LAUNCH_DEVICE_MANAGER_SETUP,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {

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
                }));

        mPermissionTasks.put(permission.UPDATE_DEVICE_MANAGEMENT_RESOURCES,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    List<Object> array = new ArrayList<Object>();
                    mTransacts.invokeTransact(Transacts.DEVICE_POLICY_SERVICE, Transacts.DEVICE_POLICY_DESCRIPTOR,
                            Transacts.setStrings, array);
                }));

        mPermissionTasks.put(permission.READ_SAFETY_CENTER_STATUS,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {

                    Class<?> clazzSaftyCenter = null;
                    try {
                        clazzSaftyCenter = Class.forName("android.safetycenter.SafetyCenterManager");
                        Object saftyCenter = mContext.getSystemService(clazzSaftyCenter);
                        invokeReflectionCall
                                (clazzSaftyCenter, "isSafetyCenterEnabled", saftyCenter, new Class[]{});
                    } catch (ClassNotFoundException e){// | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
                        throw new UnexpectedPermissionTestFailureException(e);
                    }
                }));


        // # Skip SET_UNRESTRICTED_KEEP_CLEAR_AREAS
        //Reason : The corresponding api
        //View.setUnrestrictedPreferKeepClearRects hasn't check this restriction.

        mPermissionTasks.put(permission.TIS_EXTENSION_INTERFACE,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    //The tv_input service guarded by this permission is not available on this device
                    mTransacts.invokeTransact(Transacts.TV_INPUT_SERVICE, Transacts.TV_INPUT_DESCRIPTOR,
                            Transacts.getAvailableExtensionInterfaceNames);
                }));

        // # Skip WRITE_SECURITY_LOG
        //Reason : The corresponding api
        // SecurityLog.writeEvent hasn't check this restriction/and couldn't execute propery from
        // User applications.

        mPermissionTasks.put(permission.MAKE_UID_VISIBLE,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    //mPackageManager.makeUidVisible(120000,130000);
                    mTransacts.invokeTransact(Transacts.PACKAGE_SERVICE, Transacts.PACKAGE_DESCRIPTOR,
                            Transacts.makeUidVisible,1200000,1300000);
                }));

        mPermissionTasks.put(permission.MANAGE_ETHERNET_NETWORKS,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU, () -> {
                    //disableInterface,enableInterface,updateConfiguration
                    //Those apis always raise UnsupportedOperationException
                    //if the system doesn't have automotive feature
                    if (!mPackageManager.hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE)) {
                        throw new BypassTestException("This permission requires the feature "
                                + PackageManager.FEATURE_AUTOMOTIVE);
                    }

                    Object ethernetManager =
                            mContext.getSystemService("ethernet");
                    Class<?> eurClazz = null;
                    Class<?> eurBuilderClazz = null;
                    try {
                        //To construct NetworkCapabilities as a parameter of EthernetNetworkUpdateRequest
                        Class<?> ncBuilderClazz = null;
                        ncBuilderClazz = Class.forName("android.net.NetworkCapabilities$Builder");
                        Object ncBuilderObj = invokeReflectionCall(ncBuilderClazz,
                                "withoutDefaultCapabilities", null,
                                new Class<?>[]{});
                        NetworkCapabilities nc  = (NetworkCapabilities) invokeReflectionCall(ncBuilderClazz,
                                "build", ncBuilderObj,
                                new Class<?>[]{});
                        //set up EthernetNetworkUpdateRequest
                        eurClazz = Class.forName("android.net.EthernetNetworkUpdateRequest");
                        eurBuilderClazz = Class.forName("android.net.EthernetNetworkUpdateRequest$Builder");
                        Constructor eurBuilderConstructor = eurBuilderClazz.getConstructor();
                        Object eurBuilderObj = eurBuilderConstructor.newInstance();
                        eurBuilderObj = invokeReflectionCall(eurBuilderClazz,
                                "setIpConfiguration", eurBuilderObj,
                                new Class<?>[]{IpConfiguration.class}, new IpConfiguration.Builder().build());

                        eurBuilderObj = invokeReflectionCall(eurBuilderClazz,
                                "setNetworkCapabilities", eurBuilderObj,
                                new Class<?>[]{NetworkCapabilities.class}, nc);

                        Object eurObj =  invokeReflectionCall(eurBuilderClazz,
                                "build", eurBuilderObj,
                                new Class<?>[]{});

                        invokeReflectionCall(ethernetManager.getClass(),
                                "updateConfiguration", ethernetManager,
                                new Class[]{String.class,eurClazz,Executor.class, OutcomeReceiver.class},
                                "test123abc789", eurObj, null, null
                        );
                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }));
    }
    void prepareTestBlockForU(){
        //For Android 14
        //Move From Internal Permission
        mPermissionTasks.put(InternalPermissions.permission.ACCESS_AMBIENT_CONTEXT_EVENT,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE,
                        VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {

                    @SuppressLint("WrongConstant") Object ambientContextManager
                            = mContext.getSystemService("ambient_context");
                    //Context.AMBIENT_CONTEXT_SERVICE);

                    int[] eventsArray = new int[] {-1};//AmbientContextEvent.EVENT_COUGH
                    Set<Integer> eventTypes = Arrays.stream(eventsArray).boxed().collect(
                            Collectors.toSet());

                    invokeReflectionCall(ambientContextManager.getClass(),
                            "queryAmbientContextServiceStatus",
                            ambientContextManager, new Class[]{Set.class, Executor.class,
                                    Consumer.class}, eventTypes, new Executor() {
                                @Override
                                public void execute(Runnable runnable) {

                                }
                            }, null);

                }));

        //Move from Internal Permission
        mPermissionTasks.put(InternalPermissions.permission.SUBSCRIBE_TO_KEYGUARD_LOCKED_STATE,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    //WindowManagerService#addKeyguardLockedStateListener.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        KeyguardManager.KeyguardLockedStateListener listener = new KeyguardManager.KeyguardLockedStateListener() {
                            @Override
                            public void onKeyguardLockedStateChanged(boolean b) {
                            }
                        };
                        mTransacts.invokeTransact(Transacts.WINDOW_SERVICE, Transacts.WINDOW_DESCRIPTOR,
                                Transacts.addKeyguardLockedStateListener, listener);
                    }
                }));

        //Move from Install Permission
        mPermissionTasks.put(SCHEDULE_EXACT_ALARM,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {

                    if (ActivityCompat.checkSelfPermission(mContext, USE_EXACT_ALARM)
                            == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(mContext, SCHEDULE_EXACT_ALARM)
                                    == PackageManager.PERMISSION_DENIED){
                        throw new BypassTestException(
                                "If the USE_EXACT_ALARM permission is granted, the test will be passed. let's skip it");
                    }

                    Intent intent = new Intent(mContext, MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent,
                            PendingIntent.FLAG_IMMUTABLE);
                    AlarmManager alarmManager = mContext.getSystemService(AlarmManager.class);
                    alarmManager.setExact(AlarmManager.RTC, System.currentTimeMillis() + 60 * 1000,
                            pendingIntent);
                    alarmManager.cancel(pendingIntent);
                }));


        mPermissionTasks.put(permission.DELETE_STAGED_HEALTH_CONNECT_REMOTE_DATA,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    mTransacts.invokeTransact(
                            Context.HEALTHCONNECT_SERVICE ,
                            Transacts.HEALTH_CONNECT_DESCRIPTOR,
                            Transacts.deleteAllStagedRemoteData,UserHandle.getUserHandleForUid(mUid));
                }));

        mPermissionTasks.put(permission.MIGRATE_HEALTH_CONNECT_DATA,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    CountDownLatch latch = new CountDownLatch(1);
                    AtomicBoolean success = new AtomicBoolean(true);
                    IMigrationCallback callback = new IMigrationCallback() {
                        @Override
                        public void onSuccess()
                                throws RemoteException {}
                        @Override
                        public void onError(MigrationException exception)
                                throws RemoteException {}
                        @Override
                        public IBinder asBinder() {
                            return new Stub() {
                                @Override
                                public void onSuccess() throws RemoteException {
                                    latch.countDown();
                                }
                                @Override
                                public void onError(MigrationException exception)
                                        throws RemoteException {
                                    //Hook Security Exception
                                    if(exception.getMessage() != null &&
                                            exception.getMessage().contains("java.lang.SecurityException")){
                                        success.set(false);
                                    }

                                    latch.countDown();
                                }
                            };
                        }
                    };
                    mTransacts.invokeTransact(
                            Context.HEALTHCONNECT_SERVICE,
                            Transacts.HEALTH_CONNECT_DESCRIPTOR,
                            Transacts.startMigration,
                            mContext.getPackageName(), callback);
                    try {
                        latch.await(2000, TimeUnit.MILLISECONDS);
                        if(!success.get()){
                            throw new SecurityException("Found security error in callback interface!");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }));

        mPermissionTasks.put(permission.STAGE_HEALTH_CONNECT_REMOTE_DATA,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    mTransacts.invokeTransact(
                            Context.HEALTHCONNECT_SERVICE ,
                            Transacts.HEALTH_CONNECT_DESCRIPTOR,
                            Transacts.updateDataDownloadState,
                            0);
                }));

        mPermissionTasks.put(permission.GET_APP_METADATA,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        mTransacts.invokeTransact(
                                Transacts.PACKAGE_SERVICE,
                                Transacts.PACKAGE_DESCRIPTOR,
                                Transacts.getAppMetadataFd,
                                mContext.getPackageName(), mUid);
                    }
                }));
        mPermissionTasks.put(permission.LIST_ENABLED_CREDENTIAL_PROVIDERS,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if(isPermissionGranted(QUERY_ALL_PACKAGES)){
                        throw new BypassTestException(
                                "This test works only when QUERY_ALL_PACKAGES permission is not granted.");
                    }
                    mTransacts.invokeTransact(
                            Context.CREDENTIAL_SERVICE,
                            Transacts.CREDENTIAL_DESCRIPTOR,
                            Transacts.getCredentialProviderServices,
                            mUid,Binder.getCallingPid());

                }));
        mPermissionTasks.put(permission.LOG_FOREGROUND_RESOURCE_USE,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    mTransacts.invokeTransact(
                            Transacts.ACTIVITY_SERVICE,
                            Transacts.ACTIVITY_DESCRIPTOR,
                            Transacts.logFgsApiBegin,
                            0,mUid,Binder.getCallingPid()
                            );
                }));
        mPermissionTasks.put(permission.MANAGE_CLIPBOARD_ACCESS_NOTIFICATION,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    mTransacts.invokeTransact(
                            Context.CLIPBOARD_SERVICE,
                            Transacts.CLIPBOARD_DESCRIPTOR,
                            Transacts.areClipboardAccessNotificationsEnabledForUser,
                            mUid
                    );

                }));
        mPermissionTasks.put(permission.MANAGE_SUBSCRIPTION_USER_ASSOCIATION,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    mTransacts.invokeTransact(
                            Context.TELEPHONY_SUBSCRIPTION_SERVICE,
                            Transacts.SUBSCRIPTION_DESCRIPTOR,
                            Transacts.setSubscriptionUserHandle,
                            UserHandle.getUserHandleForUid(mUid),0
                    );
                }));
        mPermissionTasks.put(permission.MANAGE_WEARABLE_SENSING_SERVICE,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    //Object callback = ReflectionUtils.stubRemoteCallback();
                    //ParcelFileDescriptor descriptor = ParcelFileDescriptor//
                    Consumer<Integer> statusCb = new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) {

                        }
                    };


                    try {
                        Object wearableSensing
                                = mContext.getSystemService("wearable_sensing");
                        ParcelFileDescriptor[] descriptors = ParcelFileDescriptor.createPipe();
                        //wearableSensing.
                        ReflectionUtils.invokeReflectionCall(wearableSensing.getClass(),
                                "provideDataStream", wearableSensing,
                                new Class[]{ParcelFileDescriptor.class, Executor.class, Consumer.class},
                                descriptors[0], mContext.getMainExecutor(), statusCb);
                    } catch (SecurityException e){
                        throw e;
                    } catch (Exception e) {
                        throw new UnexpectedPermissionTestFailureException(e);
                    }


                }));
        mPermissionTasks.put(permission.MODIFY_AUDIO_SETTINGS_PRIVILEGED,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    mTransacts.invokeTransact(
                            Context.AUDIO_SERVICE,
                            Transacts.AUDIO_DESCRIPTOR,
                            Transacts.setVolumeGroupVolumeIndex,
                            0,0,0
                    );

                }));
        mPermissionTasks.put(permission.MODIFY_HDR_CONVERSION_MODE,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        mTransacts.invokeTransact(
                                Context.DISPLAY_SERVICE,
                                Transacts.DISPLAY_DESCRIPTOR,
                                Transacts.setHdrConversionMode,
                                new HdrConversionMode(HdrConversionMode.HDR_CONVERSION_SYSTEM)
                        );
                    }
                }));
        mPermissionTasks.put(permission.MONITOR_KEYBOARD_BACKLIGHT,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    mTransacts.invokeTransact(
                            Context.INPUT_SERVICE,
                            Transacts.INPUT_DESCRIPTOR,
                            Transacts.registerKeyboardBacklightListener,
                            getActivityToken());
                }));
        mPermissionTasks.put(permission.REMAP_MODIFIER_KEYS,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    mTransacts.invokeTransact(
                            Context.INPUT_SERVICE,
                            Transacts.INPUT_DESCRIPTOR,
                            Transacts.getModifierKeyRemapping);

                }));

        mPermissionTasks.put(permission.SATELLITE_COMMUNICATION,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    mTransacts.invokeTransact(
                            Context.TELEPHONY_SERVICE,
                            Transacts.TELEPHONY_DESCRIPTOR,
                            Transacts.requestIsSatelliteEnabled);
                }));


        mPermissionTasks.put(permission.SET_APP_SPECIFIC_LOCALECONFIG,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        if(!isPermissionGranted(QUERY_ALL_PACKAGES)){
                            throw new BypassTestException(
                                    "This test works only when QUERY_ALL_PACKAGES permission is granted.");
                        }

                        LocaleList OVERRIDE_LOCALES =
                                LocaleList.forLanguageTags("en-US,fr-FR,zh-Hant-TW");

                        //for checking it need to set other application locale config
                        mTransacts.invokeTransact(
                                Context.LOCALE_SERVICE,
                                Transacts.LOCALE_DESCRIPTOR,
                                Transacts.setOverrideLocaleConfig,
                                "com.android.certifications.niap.permissions.companion",0,
                                new LocaleConfig(OVERRIDE_LOCALES));
                    }
        }));

        mPermissionTasks.put(permission.SET_LOW_POWER_STANDBY_PORTS,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    mTransacts.invokeTransact(
                            Context.POWER_SERVICE,
                            Transacts.POWER_DESCRIPTOR,
                            Transacts.releaseLowPowerStandbyPorts,
                            getActivityToken());

                }));

        mPermissionTasks.put(permission.TEST_INPUT_METHOD,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    mTransacts.invokeTransact(
                            Context.INPUT_METHOD_SERVICE,
                            Transacts.INPUTMETHOD_DESCRIPTOR,
                            Transacts.isInputMethodPickerShownForTest
                    );

                }));

        mPermissionTasks.put(permission.TURN_SCREEN_ON,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        //Hidden Options
                        final int SYSTEM_WAKELOCK = 0x80000000;
                        final int ACQUIRE_CAUSES_WAKEUP = 0x10000000;
                        mTransacts.invokeTransact(
                                Context.POWER_SERVICE,
                                Transacts.POWER_DESCRIPTOR,
                                Transacts.acquireWakeLock,
                                getActivityToken(),SCREEN_DIM_WAKE_LOCK|SYSTEM_WAKELOCK|ACQUIRE_CAUSES_WAKEUP,
                                "tag",mContext.getPackageName(),
                                new WorkSource(),"historyTag",0,null
                        );
                    }
                }));

        mPermissionTasks.put(permission.MANAGE_DEFAULT_APPLICATIONS,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    mTransacts.invokeTransact(
                            Context.ROLE_SERVICE,
                            Transacts.ROLE_DESCRIPTOR,
                            Transacts.getDefaultApplicationAsUser,
                            "dummy",mUid
                    );
                }));

        mPermissionTasks.put(permission.KILL_ALL_BACKGROUND_PROCESSES,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    mTransacts.invokeTransact(
                            Transacts.ACTIVITY_SERVICE,
                            Transacts.ACTIVITY_DESCRIPTOR,
                            Transacts.killAllBackgroundProcesses);

                }));



        mPermissionTasks.put(permission.CHECK_REMOTE_LOCKSCREEN,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    mTransacts.invokeTransact(
                            Transacts.LOCK_SETTINGS_SERVICE,
                            Transacts.LOCK_SETTINGS_DESCRIPTOR,
                            Transacts.startRemoteLockscreenValidation);
                }));

        mPermissionTasks.put(permission.REQUEST_COMPANION_PROFILE_NEARBY_DEVICE_STREAMING,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        CompletableFuture<AssociationRequest> associationRequest =
                                new CompletableFuture<AssociationRequest>().completeAsync(() ->
                                        new AssociationRequest.Builder().setDeviceProfile(
                                                AssociationRequest.DEVICE_PROFILE_NEARBY_DEVICE_STREAMING).build());
                        TesterUtils.tryBluetoothAssociationRequest
                                (mPackageManager, mActivity, associationRequest);
                    }
                }));


        mPermissionTasks.put(permission.LAUNCH_CREDENTIAL_SELECTOR,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        Intent featuresIntent = new Intent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        featuresIntent.setComponent(new
                                ComponentName("com.android.credentialmanager",
                                "com.android.credentialmanager.CredentialSelectorActivity"));
                        mActivity.startActivity(featuresIntent);
                    }
                }));

        mPermissionTasks.put(permission.WRITE_ALLOWLISTED_DEVICE_CONFIG,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (android.os.Build.VERSION.SDK_INT >= VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        //frameworks/base/core/java/android/provider/Settings.java
                        Uri CONTENT_URI = Uri.parse("content://settings/config");
                        //CALL_METHOD_SET_ALL_CONFIG = "SET_ALL_config";
                        mContentResolver.call(
                                CONTENT_URI,
                                "SET_ALL_config",null,null);
                    }
                }));

        mPermissionTasks.put(permission.READ_WRITE_SYNC_DISABLED_MODE_CONFIG,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    //commonize the tester routine with exposing the builder of AssociationRequest object
                    if (android.os.Build.VERSION.SDK_INT >= VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        //frameworks/base/core/java/android/provider/Settings.java
                        Uri CONTENT_URI = Uri.parse("content://settings/config");
                        String CALL_METHOD_SYNC_DISABLED_MODE_KEY = "_disabled_mode";
                        Bundle args = new Bundle();
                        args.putInt(CALL_METHOD_SYNC_DISABLED_MODE_KEY,1);
                        mContentResolver.call(
                                CONTENT_URI,
                                "SET_SYNC_DISABLED_MODE_config",null,args);
                    }
                }));

        //Skip permission check for GET_ANY_PROVIDER_TYPE//

        mPermissionTasks.put(permission.BROADCAST_OPTION_INTERACTIVE,
            new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                if (android.os.Build.VERSION.SDK_INT >= VERSION_CODES.UPSIDE_DOWN_CAKE) {

                    final String[] requiredPermissions = {permission.BROADCAST_OPTION_INTERACTIVE};
                    final String[] excludePermissions = {};
                    final String[] excludePackages = {};
                    final BroadcastOptions bOptions;
                    bOptions = BroadcastOptions.makeBasic();

                    //setInteractive true
                    ReflectionUtils.invokeReflectionCall(bOptions.getClass(),
                            "setInteractive",bOptions,
                            new Class<?>[]{boolean.class},true
                    );
                    //isAlarmBroadcast() should be true
                    ReflectionUtils.invokeReflectionCall(bOptions.getClass(),
                            "setAlarmBroadcast",bOptions,
                            new Class<?>[]{boolean.class},false
                    );
                    Intent[] intents = new Intent[]{new Intent(Intent.ACTION_VIEW)};
                    //getIApplicationThread()
                    Object activityThread = ReflectionUtils.invokeReflectionCall
                            (Activity.class,"getActivityThread",mActivity,new Class<?>[]{});
                    if(activityThread != null){
                        Class atClazz;
                        try {
                            atClazz = Class.forName("android.app.ActivityThread");
                            Object applicationThread = ReflectionUtils.invokeReflectionCall
                                    (atClazz,"getApplicationThread",activityThread,
                                            new Class<?>[]{});
                             for(Intent i:intents) {
                                try {
                                    mTransacts.invokeTransact(
                                            Context.ACTIVITY_SERVICE,
                                            Transacts.ACTIVITY_DESCRIPTOR,
                                            Transacts.broadcastIntentWithFeature,
                                            applicationThread, "callingFeatureId",
                                            i, "resolvedType", null, 0,
                                            "resultData", new Bundle(), requiredPermissions, excludePermissions,
                                            excludePackages, 0 /*appOp*/, bOptions.toBundle(), true, false, Binder.getCallingUid());

                                } catch (Exception ex){
                                    String name = ex.getClass().getSimpleName();
                                    if(name.equals("SecurityException")) {
                                        throw ex;
                                    }
                                }
                            }
                        } catch (ClassNotFoundException e) {
                            throw new UnexpectedPermissionTestFailureException(e);
                        }
                    }

                }
            }));

        //permission.ACCESS_GPU_SERVICE

        mPermissionTasks.put(permission.WAKEUP_SURFACE_FLINGER,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    //commonize the tester routine with exposing the builder of AssociationRequest object
                    if (android.os.Build.VERSION.SDK_INT >= VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        //mTransacts.invokeTransact(Transacts.SURFACE_FLINGER_SERVICE,
                        //        Transacts.SURFACE_FLINGER_DESCRIPTOR, Transacts.showCpu);
                        throw new BypassTestException("WAKEUP_SURFACE_FLINGER permission is bypassed");
                    }
                }));

        //permission.QUERY_CLONED_APPS

        //permission.HANDLE_QUERY_PACKAGE_RESTART

        mPermissionTasks.put(permission.MANAGE_FACE,
                new PermissionTest(false,VERSION_CODES.UPSIDE_DOWN_CAKE,  () -> {
                    if (android.os.Build.VERSION.SDK_INT >= VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        if (!mPackageManager.hasSystemFeature(PackageManager.FEATURE_FACE)) {
                            throw new BypassTestException("This permission requires the feature "
                                    + PackageManager.FEATURE_FACE);
                        }
                        runShellCommandTest("cmd face sync");
                    }
                }));
    }
    
    void prepareTestBlockForV()
    {
        // New signature permissions as of Android 15
        mPermissionTasks.put(permission.WRITE_VERIFICATION_STATE_E2EE_CONTACT_KEYS,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    //mLogger.logDebug("Test case for android.permission.WRITE_VERIFICATION_STATE_E2EE_CONTACT_KEYS not implemented yet");
                    if (mDeviceApiLevel >= 35) {
                        String LOOKUP_KEY = "0r1-423A2E4644502A2E50";
                        String DEVICE_ID = "device_id_value";
                        String ACCOUNT_ID = "+1 (555) 555-1234";

                        E2eeContactKeysManager e2m = (E2eeContactKeysManager) mContext.getSystemService
                                (Context.CONTACT_KEYS_SERVICE);
                        //Put dummy data for test
                        e2m.updateOrInsertE2eeContactKey(LOOKUP_KEY,DEVICE_ID,ACCOUNT_ID, new byte[]{0});
                        //WRITE_CONTACT & this permission
                        boolean b = e2m.updateE2eeContactKeyLocalVerificationState
                                (LOOKUP_KEY,DEVICE_ID,ACCOUNT_ID,
                                        E2eeContactKeysManager.VERIFICATION_STATE_VERIFIED);

                        mLogger.logDebug("updateE2eeContactKeyLocalVerificationState result: " + b);
                        if(!b){
                            throw new SecurityException("call updateE2eeContactKeyLocalVerificainoState failed");
                        }
                    }

                }));
        mPermissionTasks.put(permission.CAMERA_HEADLESS_SYSTEM_USER,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    //Permission for automotive
                    if(!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE)){
                        throw new BypassTestException(
                                "This permission requires feature "
                                        + PackageManager.FEATURE_AUTOMOTIVE);
                    }
                    //no implementation here
        }));
        mPermissionTasks.put(permission.CAMERA_PRIVACY_ALLOWLIST,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                //Permission for automotive
                if(!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE)){
                    throw new BypassTestException(
                            "This permission requires feature "
                                    + PackageManager.FEATURE_AUTOMOTIVE);
                }
                //no implementation here
        }));
        mPermissionTasks.put(permission.MANAGE_REMOTE_AUTH,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
            mLogger.logDebug("MANAGE_REMOTE_AUTH is not implemented yet");
            throw new BypassTestException("MANAGE_REMOTE_AUTH permission is not implemented yet");
            //mTransacts.invokeTransact(Transacts.SERVICE, Transacts.DESCRIPTOR,
            //       Transacts.unregisterCoexCallback, (Object) null);
        }));
        mPermissionTasks.put(permission.USE_REMOTE_AUTH,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
            mLogger.logDebug("USE_REMOTE_AUTH is not implemented yet");
            throw new BypassTestException("USE_REMOTE_AUTH permission is not implemented yet");
            //mTransacts.invokeTransact(Transacts.SERVICE, Transacts.DESCRIPTOR,
            //       Transacts.unregisterCoexCallback, (Object) null);
        }));

        mPermissionTasks.put(permission.THREAD_NETWORK_PRIVILEGED,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
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
                            System.out.println(ReflectionUtils.checkDeclaredMethod(threadNetworkCon,"set").toString());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
        }));

        INsdManagerCallback nsd_cb = new INsdManagerCallback() {
            @Override
            public void onDiscoverServicesStarted(int listenerKey, DiscoveryRequest discoveryRequest) throws RemoteException {

            }

            @Override
            public void onDiscoverServicesFailed(int listenerKey, int error) throws RemoteException {

            }

            @Override
            public void onServiceFound(int listenerKey, NsdServiceInfo info) throws RemoteException {

            }

            @Override
            public void onServiceLost(int listenerKey, NsdServiceInfo info) throws RemoteException {

            }

            @Override
            public void onStopDiscoveryFailed(int listenerKey, int error) throws RemoteException {

            }

            @Override
            public void onStopDiscoverySucceeded(int listenerKey) throws RemoteException {

            }

            @Override
            public void onRegisterServiceFailed(int listenerKey, int error) throws RemoteException {

            }

            @Override
            public void onRegisterServiceSucceeded(int listenerKey, NsdServiceInfo info) throws RemoteException {

            }

            @Override
            public void onUnregisterServiceFailed(int listenerKey, int error) throws RemoteException {

            }

            @Override
            public void onUnregisterServiceSucceeded(int listenerKey) throws RemoteException {

            }

            @Override
            public void onResolveServiceFailed(int listenerKey, int error) throws RemoteException {

            }

            @Override
            public void onResolveServiceSucceeded(int listenerKey, NsdServiceInfo info) throws RemoteException {

            }

            @Override
            public void onStopResolutionFailed(int listenerKey, int error) throws RemoteException {

            }

            @Override
            public void onStopResolutionSucceeded(int listenerKey) throws RemoteException {

            }

            @Override
            public void onServiceInfoCallbackRegistrationFailed(int listenerKey, int error) throws RemoteException {

            }

            @Override
            public void onServiceUpdated(int listenerKey, NsdServiceInfo info) throws RemoteException {

            }

            @Override
            public void onServiceUpdatedLost(int listenerKey) throws RemoteException {

            }

            @Override
            public void onServiceInfoCallbackUnregistered(int listenerKey) throws RemoteException {

            }

            @Override
            public IBinder asBinder() {
                return getActivityToken();
            }
        };
        mPermissionTasks.put(permission.REGISTER_NSD_OFFLOAD_ENGINE,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
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
                    invokeReflectionCall(NsdManager.class,
                            "registerOffloadEngine", manager,
                            new Class<?>[]{String.class,long.class,long.class,Executor.class,OffloadEngine.class},
                            "iface1",
                            OFFLOAD_TYPE_REPLY,
                            OFFLOAD_CAPABILITY_BYPASS_MULTICAST_LOCK,
                            mContext.getMainExecutor(),
                            mock);
                    //If succeed unregister it for in the case.
                    invokeReflectionCall(NsdManager.class,
                            "unregisterOffloadEngine", manager,
                            new Class<?>[]{OffloadEngine.class},
                            mock);
                }));
        mPermissionTasks.put(permission.QUARANTINE_APPS,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (mDeviceApiLevel >= 35) {
                        //final boolean quarantined = ((flags & PackageManager.FLAG_SUSPEND_QUARANTINED) != 0)
                        //        && Flags.quarantinedEnabled();
                        // Flags.quarantinedEnabled();
                        try {

                            Class<?> clazzDialogBuilder = null;
                            clazzDialogBuilder = Class.forName("android.content.pm.SuspendDialogInfo$Builder");
                            Constructor constructor = clazzDialogBuilder.getConstructor();
                            Object builderObj = constructor.newInstance();

                            Object dialogInfo =
                                    invokeReflectionCall(clazzDialogBuilder, "build", builderObj, new Class[]{});


                            int FLAG_SUSPEND_QUARANTINED = 0x00000001;
                            mTransacts.invokeTransact(
                                    Transacts.PACKAGE_SERVICE,
                                    Transacts.PACKAGE_DESCRIPTOR,
                                    Transacts.setPackagesSuspendedAsUser,
                                    new String[]{"dummy.package.suspending"}, false,
                                    new PersistableBundle(),new PersistableBundle(),dialogInfo,
                                    FLAG_SUSPEND_QUARANTINED,"dummy.package.suspending",mUid,mUid);

                        } catch (ClassNotFoundException | NoSuchMethodException ex){
                            mLogger.logError("class not found exception",ex);
                        } catch (InvocationTargetException | IllegalAccessException |
                                 InstantiationException ex) {
                            mLogger.logError("instance/invocation error",ex);
                        }
                    }


        }));
        //Infeasible to test
//        mPermissionTasks.put(permission.VIBRATE_SYSTEM_CONSTANTS,
//                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
//        mLogger.logDebug("Test case for android.permission.VIBRATE_SYSTEM_CONSTANTS not implemented yet");
//            if(Build.VERSION.SDK_INT>= VERSION_CODES.TIRAMISU){
//
//                VibratorManager vibratorManager = (VibratorManager)
//                        mContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
//
//                //ReflectionUtils.invokeReflectionCall(VibratorManager.class,
//                //        "performHapticFeedback",vibratorManager,
//                //        new Class<?>[]{int.class,boolean.class,String.class,boolean.class},0,true,"",true);
//                //Can we get system log here?
//            }
//
//        }));
        mPermissionTasks.put(permission.CONFIGURE_FACTORY_RESET_PROTECTION,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                mTransacts.invokeTransact(Transacts.PDB_SERVICE, Transacts.PDB_DESCRIPTOR,
                       Transacts.deactivateFactoryResetProtection, (Object) "dummy_bytes".getBytes());

        }));

        mPermissionTasks.put(permission.ACCESS_LAST_KNOWN_CELL_ID,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    mTransacts.invokeTransact(Transacts.TELEPHONY_SERVICE, Transacts.TELEPHONY_DESCRIPTOR,
                            Transacts.getLastKnownCellIdentity, 0,mPackageName,"callingFeatureId");

            }));
        mPermissionTasks.put(permission.ACCESS_HIDDEN_PROFILES_FULL,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    //same as access_hidden_profiles install permission
                    //those two permissions are treated as 'either is fine' condition.
                    LauncherApps launcherApps = (LauncherApps)
                            mContext.getSystemService(Context.LAUNCHER_APPS_SERVICE);
                    //If the caller cannot access hidden profiles the method returns null
                    Object intent = invokeReflectionCall
                            (launcherApps.getClass(),
                                    "getPrivateSpaceSettingsIntent",
                                    launcherApps,new Class[]{});
                    if(intent == null){
                        throw new SecurityException("Caller cannot access hidden profiles");
                    }

        }));
        mPermissionTasks.put(permission.MANAGE_ENHANCED_CONFIRMATION_STATES,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    //ReflectionUtils.checkDeclaredMethod("android.os.SystemConfigManager")
                    Object systemConfig = mContext.getSystemService("system_config");
                    //mLogger.logSystem(">"+ReflectionUtils.checkDeclaredMethod(systemConfig,"get").toString());
                    invokeReflectionCall(systemConfig.getClass(),
                            "getEnhancedConfirmationTrustedInstallers",systemConfig,new Class<?>[]{});
                    //getEnhancedConfirmationTrustedInstallers
                    //mLogger.logDebug("Test case for android.permission.MANAGE_ENHANCED_CONFIRMATION_STATES not implemented yet");
                    //mTransacts.invokeTransact(Transacts.SYSTEM_CONFIG_SERVICE, Transacts.SYSTEM_CONFIG_DESCRIPTOR,
                    //Transacts.getEnhancedConfirmationTrustedPackages, (Object) null);
        }));
        mPermissionTasks.put(permission.READ_DROPBOX_DATA,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    //Same as PEEK_DROPBOX_DATA
                    if (mContext.checkSelfPermission(Manifest.permission.READ_LOGS)
                            == PackageManager.PERMISSION_GRANTED &&
                            mContext.checkSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS)
                                    == PackageManager.PERMISSION_GRANTED) {
                        if(mContext.checkSelfPermission(permission.READ_DROPBOX_DATA)
                                != PackageManager.PERMISSION_GRANTED)
                            throw new BypassTestException(
                                    "Bypass the check due to avoid unexpected behaviour. ");
                    }

                    long currTimeMs = System.currentTimeMillis();

                    Parcel result= mTransacts.invokeTransact(Transacts.DROPBOX_SERVICE,
                            Transacts.DROPBOX_DESCRIPTOR,
                            Transacts.getNextEntry, "test-companion-tag", currTimeMs-(1000*60*60*8), mPackageName);

                    if (result.readInt() == 0) {
                        throw new SecurityException(
                                "Received DropBoxManager.Entry is null during PEEK_DROPBOX_DATA "
                                        + "test. Please check you surely executed companion app before testing.");
                    }
                    DropBoxManager.Entry entry = DropBoxManager.Entry.CREATOR.createFromParcel(
                            result);

                    mLogger.logDebug(
                            "Successfully parsed entry from parcel: " + entry.getText(100));
        }));
        //Infeasible to test
//        mPermissionTasks.put(permission.ACCESSIBILITY_MOTION_EVENT_OBSERVING,
//                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
//                //ProxyAccessibilityServiceConnection mProxyConnection;
//                //mProxyConnection.setInstalledAndEnabledServices(List<?> infos)
//                Object mProxyConnextion = ReflectionUtils.stubHiddenObject
//                        ("com.android.server.accessibility.ProxyAccessibilityServiceConnection");
//        }));

        mPermissionTasks.put(permission.LAUNCH_PERMISSION_SETTINGS,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                //The action may affect to ui
                String ACTION_MANAGE_APP_PERMISSIONS = "android.intent.action.MANAGE_APP_PERMISSIONS";
                Intent intent = new Intent(ACTION_MANAGE_APP_PERMISSIONS);
                intent.setAction("android.settings.APP_PERMISSIONS_SETTINGS");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);

        }));
        mPermissionTasks.put(permission.REQUEST_OBSERVE_DEVICE_UUID_PRESENCE,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {

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
                invokeReflectionCall(
                        builderObj.getClass(),
                        "setUuid",builderObj,new Class[]{ParcelUuid.class},
                        ParcelUuid.fromString("00000000-0000-1000-8000-00805F9B34FB"));

                Object odprParams =
                        invokeReflectionCall(clazzOdprBuilder, "build", builderObj, new Class[]{});

                mTransacts.invokeTransact(
                        Transacts.COMPANION_DEVICE_SERVICE, Transacts.COMPANION_DEVICE_DESCRIPTOR,
                        Transacts.startObservingDevicePresence, odprParams,mPackageName,mUid);
            } catch (SecurityException ex) {
                throw ex;
            } catch (Exception ex){
                throw new UnexpectedPermissionTestFailureException(ex);
            }

        }));
        mPermissionTasks.put(permission.USE_COMPANION_TRANSPORTS,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                final int MESSAGE_REQUEST_PING = 0x63807378;
                mTransacts.invokeTransact(
                        Transacts.COMPANION_DEVICE_SERVICE, Transacts.COMPANION_DEVICE_DESCRIPTOR,
                        Transacts.sendMessage,MESSAGE_REQUEST_PING, new byte[]{0}, new int[]{0});
        }));
        mPermissionTasks.put(permission.MEDIA_ROUTING_CONTROL,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
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
            mTransacts.invokeTransact(Transacts.MEDIA_ROUTER_SERVICE, Transacts.MEDIA_ROUTER_DESCRIPTOR,
                Transacts.registerManager,manager,mPackageName);
        }));
        mPermissionTasks.put(permission.REPORT_USAGE_STATS,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
            mTransacts.invokeTransact(Transacts.USAGE_STATS_SERVICE, Transacts.USAGE_STATS_DESCRIPTOR,
                   Transacts.reportChooserSelection, mPackageName,mUid,"text/html",
                    new String[]{"annotation-1"},"action");
        }));
        mPermissionTasks.put(permission.SET_BIOMETRIC_DIALOG_ADVANCED,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                //mLogger.logDebug("Test case for android.permission.SET_BIOMETRIC_DIALOG_ADVANCED not implemented yet");
                //NOTE :
                //Conflict to USE_BIOMETRIC permission
                //You should put below in noperm/AndroidManifest.xml to test
                //<uses-permission android:name="android.permission.USE_BIOMETRIC" />
                if(Build.VERSION.SDK_INT>28) {
                    BiometricPrompt.Builder bmBuilder = new BiometricPrompt.Builder(mContext)
                            .setTitle("a").setNegativeButton("text", mContext.getMainExecutor(), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });//.setLo
                    invokeReflectionCall(BiometricPrompt.Builder.class,
                            "setLogoDescription", bmBuilder, new Class<?>[]{String.class},
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
        }));
        mPermissionTasks.put(permission.RECORD_SENSITIVE_CONTENT,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {

            String ACTION_MANAGE_APP_PERMISSIONS = "android.intent.action.MANAGE_APP_PERMISSIONS";
            Intent intent = new Intent(ACTION_MANAGE_APP_PERMISSIONS);
            intent.setAction("android.settings.APP_PERMISSIONS_SETTINGS");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);


        }));
        mPermissionTasks.put(permission.ACCESS_SMARTSPACE,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
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
                            parcel.writeTypedObject(UserHandle.getUserHandleForUid(mUid), 0);
                        }
                    };
                    mTransacts.invokeTransact(Transacts.SMART_SPACE_SERVICE,
                            Transacts.SMART_SPACE_DESCRIPTOR, Transacts.destroySmartspaceSession, smartspaceId);
        }));
        mPermissionTasks.put(permission.ACCESS_CONTEXTUAL_SEARCH,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
            int ENTRYPOINT_LONG_PRESS_HOME = 2;
            mTransacts.invokeTransact(Transacts.CONTEXTUAL_SEARCH_SERVICE, Transacts.CONTEXTUAL_SEARCH_DESCRIPTOR,
                   Transacts.startContextualSearch, (Object) ENTRYPOINT_LONG_PRESS_HOME);
        }));
        /*
        mPermissionTasks.put(permission.RECEIVE_SANDBOX_TRIGGER_AUDIO,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
            mLogger.logDebug("Test case for android.permission.RECEIVE_SANDBOX_TRIGGER_AUDIO not implemented yet");
            String name = AppOpsManager.permissionToOp("android.permission.RECEIVE_SANDBOX_TRIGGER_AUDIO");
            if(name != null){

            }
            mLogger.logSystem("appop=>"+name);
            /*invokeReflectionCall(mAppOpsManager.getClass(), "setMode", mAppOpsManager,
                    new Class<?>[]{String.class, int.class, String.class, int.class},
                    AppOpsManager.OPSTR_CAMERA, mAppInfo.uid, mPackageName,
                    AppOpsManager.MODE_ALLOWED);
        }));*/

        mPermissionTasks.put(permission.SET_THEME_OVERLAY_CONTROLLER_READY,
                    new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    //Unreachable
                    if(!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE)){
                        throw new BypassTestException(
                                "This permission requires feature "
                                        + PackageManager.FEATURE_AUTOMOTIVE);
                    }
                    //Flags.setHomeDelay should be false to chek it and it is not allowed to automotive
                    //com.android.systemui.shared.Flags.enableHomeDelay;
                    invokeReflectionCall(ActivityManager.class,
                            "setThemeOverlayReady",mActivityManager,
                            new Class<?>[]{int.class},mUid);
        }));
        mPermissionTasks.put(permission.SHOW_CUSTOMIZED_RESOLVER,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
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
                    throw new UnexpectedPermissionTestFailureException("Corresponding receiver was not found");
                }
        }));
        mPermissionTasks.put(permission.MONITOR_STICKY_MODIFIER_STATE,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {

            IStickyModifierStateListener listener = new IStickyModifierStateListener(){
                @Override
                public IBinder asBinder() {
                    return new Binder();
                }
                @Override
                public void onStickyModifierStateChanged(int modifierState, int lockedModifierState) throws RemoteException {

                }
            };
            mTransacts.invokeTransact(Transacts.INPUT_SERVICE, Transacts.INPUT_DESCRIPTOR,
                       Transacts.registerStickyModifierStateListener, listener);
        }));

        //You can't enclose this callback into the closure routine.
        //For Transaciton method recgnizing it as IIterface object

        mPermissionTasks.put(permission.USE_ON_DEVICE_INTELLIGENCE,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    Object ond = mContext.getSystemService("on_device_intelligence");
                    //mLogger.logSystem(">"+ond.toString());
                    //mLogger.logSystem(">"+ReflectionUtils.checkDeclaredMethod(ond,"getVersion").toString());

                    //public void getFeature(
                    //            int featureId,
                    //            @NonNull @CallbackExecutor Executor callbackExecutor,
                    //            @NonNull OutcomeReceiver<Feature, OnDeviceIntelligenceException> featureReceiver) {
                    if (Build.VERSION.SDK_INT >= VERSION_CODES.S) {
                        try {
                            invokeReflectionCall(ond.getClass(),
                                    "getVersion", ond,
                                    new Class<?>[]{Executor.class, LongConsumer.class},
                                    mContext.getMainExecutor(), (LongConsumer) result -> {
                                    });
                        } catch (UnexpectedPermissionTestFailureException ex){
                            if(ex.getCause() != null  &&  ex.getCause() instanceof InvocationTargetException){
                                if(ex.getCause().getCause() != null && ex.getCause() instanceof IllegalStateException){
                                    //ok? : remote service is not configured
                                }
                            }
                        }
                        /*
                        invokeReflectionCall(ond.getClass(),
                                "getFeature",ond,
                                new Class<?>[]{int.class,Executor.class, OutcomeReceiver.class},
                                1,mContext.getMainExecutor(),(OutcomeReceiver) result->{});

                         */
                    }
//                    mTransacts.invokeTransact(Transacts.ON_DEVICE_INTELLIGENCE_SERVICE,
//                            Transacts.ON_DEVICE_INTELLINGENCE_DESCRIPTOR,
//                            Transacts.getFeature,ii_callback);
//                            invokeTransactWithCharSequence(Transacts.ON_DEVICE_INTELLIGENCE_SERVICE,
//                            Transacts.ON_DEVICE_INTELLINGENCE_DESCRIPTOR,
//                            Transacts.getFeature,false,ii_callback);
        }));


        //permission.START_ACTIVITIES_FROM_SDK_SANDBOX.
        //Implement this test as an insturmentaion test case.

        mPermissionTasks.put(permission.SYNC_FLAGS,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                  try {
                      Object featureFlags = invokeReflectionCall(
                            "android.flags.FeatureFlags","getInstance",null,
                            new Class<?>[]{});
                      invokeReflectionCall(Class.forName("android.flags.FeatureFlags"),
                              "booleanFlag",null,
                              new Class<?>[]{String.class,String.class,boolean.class},"dummy","dummy-boolean",true);
                      invokeReflectionCall(Class.forName("android.flags.FeatureFlags"),
                                "sync",featureFlags,
                                new Class<?>[]{});
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
        }));
        mPermissionTasks.put(permission.WRITE_FLAGS,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    mTransacts.invokeTransact(Transacts.FEATURE_FLAGS_SERVICE, Transacts.FEATURE_FLAGS_DESCRIPTOR,
                        Transacts.overrideFlag,new SyncableFlag("test","permission_tester_writeflag","dummy",true));
        }));
        mPermissionTasks.put(permission.GET_BINDING_UID_IMPORTANCE,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
            mLogger.logDebug("Test case for android.permission.GET_BINDING_UID_IMPORTANCE not implemented yet");
            mTransacts.invokeTransact(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
                   Transacts.getBindingUidProcessState, mUid, mPackageName);
        }));
        mPermissionTasks.put(permission.MANAGE_DISPLAYS,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
            //mLogger.logDebug("Test case for android.permission.MANAGE_DISPLAYS not implemented yet");
            mTransacts.invokeTransact(Transacts.DISPLAY_SERVICE,Transacts.DISPLAY_DESCRIPTOR,
                   Transacts.enableConnectedDisplay, 0);
        }));
        mPermissionTasks.put(permission.PREPARE_FACTORY_RESET,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    //String ACTION_SHOW_NFC_RESOLVER = "android.nfc.action.SHOW_NFC_RESOLVER";
                    String ACTION_PREPARE_FACTORY_RESET =
                            "com.android.settings.ACTION_PREPARE_FACTORY_RESET";
                    Intent intent = new Intent(ACTION_PREPARE_FACTORY_RESET);
                    //intent.putExtra(Intent.EXTRA_INTENT,new Intent());
                    //intent.putExtra(Intent.EXTRA_TITLE,"test-title");
                    //dintent.setAction("android.settings.APP_PERMISSIONS_SETTINGS");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ResolveInfo res = mPackageManager.
                            resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                    mLogger.logDebug(">"+res);
                    if(res != null && res.activityInfo != null) {
                        mContext.startActivity(intent);
                    } else {
                        throw new BypassTestException("Unable to resolve a Factory Reset Handler Activty");
                    }
            }));
        /*
        mPermissionTasks.put(permission.OVERRIDE_SYSTEM_KEY_BEHAVIOR_IN_FOCUSED_WINDOW,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    //internal permission is not exposed
        }));
        */
        /* Infeasible to test
        mPermissionTasks.put(permission.RECEIVE_SENSITIVE_NOTIFICATIONS,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
            mLogger.logDebug("Test case for android.permission.RECEIVE_SENSITIVE_NOTIFICATIONS not implemented yet");
            //mTransacts.invokeTransact(Transacts.SERVICE, Transacts.DESCRIPTOR,
            //       Transacts.unregisterCoexCallback, (Object) null);
        }));*/
        mPermissionTasks.put(permission.GET_BACKGROUND_INSTALLED_PACKAGES,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
            mTransacts.invokeTransact(Transacts.BACKGROUND_INSTALL_CONTROL_SERVICE,
                    Transacts.BACKGROUND_INSTALL_CONTROL_DESCRIPTOR,
                    Transacts.getBackgroundInstalledPackages, PackageManager.MATCH_ALL,mUid);
        }));
        mPermissionTasks.put(permission.READ_SYSTEM_GRAMMATICAL_GENDER,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    if (Build.VERSION.SDK_INT >= VERSION_CODES.S) {
                        mTransacts.invokeTransact(Transacts.GRAMMATICAL_INFLECTION_SERVICE, Transacts.GRAMMATICAL_INFLECTION_DESCRIPTOR,
                               Transacts.getSystemGrammaticalGender, mContext.getAttributionSource(),mUid);
                    }
                }));
        //Infeasible to test
//        mPermissionTasks.put(permission.EMERGENCY_INSTALL_PACKAGES,
//                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
//            //mLogger.logDebug("Test case for android.permission.EMERGENCY_INSTALL_PACKAGES not implemented yet");
//            //mTransacts.invokeTransact(Transacts.SERVICE, Transacts.DESCRIPTOR,
//            //Transacts.unregisterCoexCallback, (Object) null);
//            //PackageInstal
//            //PackageInstaller.Session session = mPackageInstaller.createSession(
//
//        }));
        mPermissionTasks.put(permission.RESTRICT_DISPLAY_MODES,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
            mTransacts.invokeTransact(Transacts.DISPLAY_SERVICE, Transacts.DISPLAY_DESCRIPTOR,
            Transacts.requestDisplayModes, getActivityToken(),0,null);
        }));
        //
        mPermissionTasks.put(permission.SCREEN_TIMEOUT_OVERRIDE,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {
                    //Check EarlyScreenTimeoutDetectorEnable Flag Here and skip if it is disabled
                    //test method : isWakeLockLevelSupported(int level) {(PowerManager.SCREEN_TIMEOUT_OVERRIDE_WAKE_LOCK)
                    final int SCREEN_TIMEOUT_OVERRIDE_WAKE_LOCK  =0x00000100;
                    Parcel isSupported = mTransacts.invokeTransact(Transacts.POWER_SERVICE, Transacts.POWER_DESCRIPTOR,
                            Transacts.isWakeLockLevelSupported, SCREEN_TIMEOUT_OVERRIDE_WAKE_LOCK);
                    if(!isSupported.readBoolean()){
                        throw new BypassTestException("WakeLock feature SCREEN_TIMEOUT_OVERRIDE_WAKE_LOCK is not supported on this device. Bypassed");
                    }

                    mTransacts.invokeTransact(Transacts.POWER_SERVICE, Transacts.POWER_DESCRIPTOR,
                    Transacts.acquireWakeLock,
                    getActivityToken(),SCREEN_TIMEOUT_OVERRIDE_WAKE_LOCK,
                    "tag",mContext.getPackageName(),
                    new WorkSource(),"historyTag",0,null);
        }));

        mPermissionTasks.put(permission.SETUP_FSVERITY,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE, () -> {

                if (Build.VERSION.SDK_INT >= 35) {
                    FileIntegrityManager fmg = (FileIntegrityManager) mContext.getSystemService(Context.FILE_INTEGRITY_SERVICE);
                    try {
                        File file = File.createTempFile("authfd", ".tmp",mContext.getFilesDir());
                        invokeReflectionCall
                                (fmg.getClass(),"setupFsVerity",fmg,
                                        new Class<?>[]{File.class},file);

                    } catch (SecurityException e) {
                        throw e;
                    } catch (Exception e){
                        //expected (may catch InvocationTargetException for null pointer exception)
                    }
                }
        }));


    }
    
    
    void prepareTestBlockBind()
    {
        // All of the BIND_* signature permissions are intended to be required by various services
        // to ensure only the platform can bind to them. The companion package defines a service
        // for each of these permissions, requiring the permission to bind to it. The
        // #getBindRunnable uses the permission name to determine the name of the service in the
        // companion package and returns a Runnable that will attempt to bind to the service
        // for each of the permissions.

        //TODO : Any Bind Services causes ANR with Activity swapping
        mPermissionTasks.put(permission.BIND_INCALL_SERVICE,
                new PermissionTest(false, getBindRunnable(permission.BIND_INCALL_SERVICE)));
        mPermissionTasks.put(permission.BIND_ATTENTION_SERVICE,
                new PermissionTest(false, Build.VERSION_CODES.Q, getBindRunnable(permission.BIND_ATTENTION_SERVICE)));
        mPermissionTasks.put(permission.BIND_PRINT_RECOMMENDATION_SERVICE,
                new PermissionTest(false,
                        getBindRunnable(permission.BIND_PRINT_RECOMMENDATION_SERVICE)));
        mPermissionTasks.put(permission.BIND_KEYGUARD_APPWIDGET,
                new PermissionTest(false, getBindRunnable(permission.BIND_KEYGUARD_APPWIDGET)));
        mPermissionTasks.put(permission.BIND_DEVICE_ADMIN,
                new PermissionTest(false, getBindRunnable(permission.BIND_DEVICE_ADMIN)));
        mPermissionTasks.put(permission.BIND_PRINT_SERVICE,
                new PermissionTest(false, getBindRunnable(permission.BIND_PRINT_SERVICE)));
        mPermissionTasks.put(permission.BIND_RUNTIME_PERMISSION_PRESENTER_SERVICE,
                new PermissionTest(false,
                        getBindRunnable(permission.BIND_RUNTIME_PERMISSION_PRESENTER_SERVICE)));
        mPermissionTasks.put(permission.BIND_VR_LISTENER_SERVICE,
                new PermissionTest(false, getBindRunnable(permission.BIND_VR_LISTENER_SERVICE)));
        mPermissionTasks.put(permission.BIND_DREAM_SERVICE,
                new PermissionTest(false, getBindRunnable(permission.BIND_DREAM_SERVICE)));
        mPermissionTasks.put(permission.BIND_CARRIER_SERVICES,
                new PermissionTest(false, getBindRunnable(permission.BIND_CARRIER_SERVICES)));
        mPermissionTasks.put(permission.BIND_QUICK_SETTINGS_TILE,
                new PermissionTest(false, getBindRunnable(permission.BIND_QUICK_SETTINGS_TILE)));
        mPermissionTasks.put(permission.BIND_TV_INPUT,
                new PermissionTest(false, getBindRunnable(permission.BIND_TV_INPUT)));
        mPermissionTasks.put(permission.BIND_AUTOFILL,
                new PermissionTest(false, getBindRunnable(permission.BIND_AUTOFILL)));
        mPermissionTasks.put(permission.BIND_WALLPAPER,
                new PermissionTest(false, getBindRunnable(permission.BIND_WALLPAPER)));
        mPermissionTasks.put(permission.BIND_INTENT_FILTER_VERIFIER,
                new PermissionTest(false, getBindRunnable(permission.BIND_INTENT_FILTER_VERIFIER)));
        mPermissionTasks.put(permission.BIND_TELECOM_CONNECTION_SERVICE,
                new PermissionTest(false,
                        getBindRunnable(permission.BIND_TELECOM_CONNECTION_SERVICE)));
        mPermissionTasks.put(permission.BIND_CALL_REDIRECTION_SERVICE,
                new PermissionTest(false,Build.VERSION_CODES.Q,
                        getBindRunnable(permission.BIND_CALL_REDIRECTION_SERVICE)));
        mPermissionTasks.put(permission.BIND_VOICE_INTERACTION,
                new PermissionTest(false, getBindRunnable(permission.BIND_VOICE_INTERACTION)));
        mPermissionTasks.put(permission.BIND_CACHE_QUOTA_SERVICE,
                new PermissionTest(false, getBindRunnable(permission.BIND_CACHE_QUOTA_SERVICE)));
        mPermissionTasks.put(permission.BIND_RESOLVER_RANKER_SERVICE,
                new PermissionTest(false,
                        getBindRunnable(permission.BIND_RESOLVER_RANKER_SERVICE)));
        mPermissionTasks.put(permission.BIND_CARRIER_MESSAGING_CLIENT_SERVICE,
                new PermissionTest(false,Build.VERSION_CODES.Q,
                        getBindRunnable(permission.BIND_CARRIER_MESSAGING_CLIENT_SERVICE)));
        mPermissionTasks.put(permission.BIND_CONNECTION_SERVICE,
                new PermissionTest(false, getBindRunnable(permission.BIND_CONNECTION_SERVICE)));
        mPermissionTasks.put(permission.BIND_QUICK_ACCESS_WALLET_SERVICE,
                new PermissionTest(false,Build.VERSION_CODES.R,
                        getBindRunnable(permission.BIND_QUICK_ACCESS_WALLET_SERVICE)));
        mPermissionTasks.put(permission.BIND_VPN_SERVICE,
                new PermissionTest(false, getBindRunnable(permission.BIND_VPN_SERVICE)));
        mPermissionTasks.put(permission.BIND_APPWIDGET,
                new PermissionTest(false, getBindRunnable(permission.BIND_APPWIDGET)));
        mPermissionTasks.put(permission.BIND_NOTIFICATION_LISTENER_SERVICE,
                new PermissionTest(false,
                        getBindRunnable(permission.BIND_NOTIFICATION_LISTENER_SERVICE)));
        mPermissionTasks.put(permission.BIND_SCREENING_SERVICE,
                new PermissionTest(false, getBindRunnable(permission.BIND_SCREENING_SERVICE)));
        mPermissionTasks.put(permission.BIND_MIDI_DEVICE_SERVICE,
                new PermissionTest(false, getBindRunnable(permission.BIND_MIDI_DEVICE_SERVICE)));
        mPermissionTasks.put(permission.BIND_REMOTE_DISPLAY,
                new PermissionTest(false, getBindRunnable(permission.BIND_REMOTE_DISPLAY)));
        mPermissionTasks.put(permission.BIND_AUTOFILL_SERVICE,
                new PermissionTest(false, getBindRunnable(permission.BIND_AUTOFILL_SERVICE)));
        mPermissionTasks.put(permission.BIND_JOB_SERVICE,
                new PermissionTest(false, getBindRunnable(permission.BIND_JOB_SERVICE)));
        mPermissionTasks.put(permission.BIND_COMPANION_DEVICE_MANAGER_SERVICE,
                new PermissionTest(false,
                        getBindRunnable(permission.BIND_COMPANION_DEVICE_MANAGER_SERVICE)));
        mPermissionTasks.put(permission.BIND_PACKAGE_VERIFIER,
                new PermissionTest(false, getBindRunnable(permission.BIND_PACKAGE_VERIFIER)));
        mPermissionTasks.put(permission.BIND_ROUTE_PROVIDER,
                new PermissionTest(false, getBindRunnable(permission.BIND_ROUTE_PROVIDER)));
        mPermissionTasks.put(permission.BIND_CARRIER_MESSAGING_SERVICE,
                new PermissionTest(false,
                        getBindRunnable(permission.BIND_CARRIER_MESSAGING_SERVICE)));
        mPermissionTasks.put(permission.BIND_EUICC_SERVICE,
                new PermissionTest(false, getBindRunnable(permission.BIND_EUICC_SERVICE)));
        mPermissionTasks.put(permission.BIND_VISUAL_VOICEMAIL_SERVICE,
                new PermissionTest(false,
                        getBindRunnable(permission.BIND_VISUAL_VOICEMAIL_SERVICE)));
        mPermissionTasks.put(permission.BIND_TV_REMOTE_SERVICE,
                new PermissionTest(false, getBindRunnable(permission.BIND_TV_REMOTE_SERVICE)));
        mPermissionTasks.put(permission.BIND_CONDITION_PROVIDER_SERVICE,
                new PermissionTest(false,
                        getBindRunnable(permission.BIND_CONDITION_PROVIDER_SERVICE)));
        mPermissionTasks.put(permission.BIND_AUTOFILL_FIELD_CLASSIFICATION_SERVICE,
                new PermissionTest(false,
                        getBindRunnable(permission.BIND_AUTOFILL_FIELD_CLASSIFICATION_SERVICE)));
        mPermissionTasks.put(permission.BIND_CONTENT_SUGGESTIONS_SERVICE,
                new PermissionTest(false,Build.VERSION_CODES.Q,
                        getBindRunnable(permission.BIND_CONTENT_SUGGESTIONS_SERVICE)));
        mPermissionTasks.put(permission.BIND_NOTIFICATION_ASSISTANT_SERVICE,
                new PermissionTest(false,
                        getBindRunnable(permission.BIND_NOTIFICATION_ASSISTANT_SERVICE)));
        mPermissionTasks.put(permission.BIND_SOUND_TRIGGER_DETECTION_SERVICE,
                new PermissionTest(false,
                        getBindRunnable(permission.BIND_SOUND_TRIGGER_DETECTION_SERVICE)));
        mPermissionTasks.put(permission.BIND_PRINT_SPOOLER_SERVICE,
                new PermissionTest(false, getBindRunnable(permission.BIND_PRINT_SPOOLER_SERVICE)));
        mPermissionTasks.put(permission.BIND_DIRECTORY_SEARCH,
                new PermissionTest(false, getBindRunnable(permission.BIND_DIRECTORY_SEARCH)));
        mPermissionTasks.put(permission.BIND_TELEPHONY_NETWORK_SERVICE,
                new PermissionTest(false,
                        getBindRunnable(permission.BIND_TELEPHONY_NETWORK_SERVICE)));
        mPermissionTasks.put(permission.BIND_CONTROLS,
                new PermissionTest(false, Build.VERSION_CODES.R,getBindRunnable(permission.BIND_CONTROLS)));
        mPermissionTasks.put(permission.BIND_SETTINGS_SUGGESTIONS_SERVICE,
                new PermissionTest(false,
                        getBindRunnable(permission.BIND_SETTINGS_SUGGESTIONS_SERVICE)));
        mPermissionTasks.put(permission.BIND_TRUST_AGENT,
                new PermissionTest(false, getBindRunnable(permission.BIND_TRUST_AGENT)));
        mPermissionTasks.put(permission.BIND_REMOTEVIEWS,
                new PermissionTest(false, getBindRunnable(permission.BIND_REMOTEVIEWS)));
        mPermissionTasks.put(permission.BIND_TELEPHONY_DATA_SERVICE,
                new PermissionTest(false, getBindRunnable(permission.BIND_TELEPHONY_DATA_SERVICE)));
        mPermissionTasks.put(permission.BIND_CELL_BROADCAST_SERVICE,
                new PermissionTest(false, Build.VERSION_CODES.R,
                        getBindRunnable(permission.BIND_CELL_BROADCAST_SERVICE)));
        mPermissionTasks.put(permission.BIND_ACCESSIBILITY_SERVICE,
                new PermissionTest(false, getBindRunnable(permission.BIND_ACCESSIBILITY_SERVICE)));
        mPermissionTasks.put(permission.BIND_INPUT_METHOD,
                new PermissionTest(false, getBindRunnable(permission.BIND_INPUT_METHOD)));
        mPermissionTasks.put(permission.BIND_EXTERNAL_STORAGE_SERVICE,
                new PermissionTest(false,Build.VERSION_CODES.R,
                        getBindRunnable(permission.BIND_EXTERNAL_STORAGE_SERVICE)));
        mPermissionTasks.put(permission.BIND_TEXTCLASSIFIER_SERVICE,
                new PermissionTest(false, getBindRunnable(permission.BIND_TEXTCLASSIFIER_SERVICE)));
        mPermissionTasks.put(permission.BIND_NFC_SERVICE,
                new PermissionTest(false, getBindRunnable(permission.BIND_NFC_SERVICE)));
        mPermissionTasks.put(permission.BIND_PHONE_ACCOUNT_SUGGESTION_SERVICE,
                new PermissionTest(false,Build.VERSION_CODES.Q,
                        getBindRunnable(permission.BIND_PHONE_ACCOUNT_SUGGESTION_SERVICE)));
        mPermissionTasks.put(permission.BIND_IMS_SERVICE,
                new PermissionTest(false, getBindRunnable(permission.BIND_IMS_SERVICE)));
        mPermissionTasks.put(permission.BIND_TEXT_SERVICE,
                new PermissionTest(false, getBindRunnable(permission.BIND_TEXT_SERVICE)));
        mPermissionTasks.put(permission.BIND_EXPLICIT_HEALTH_CHECK_SERVICE,
                new PermissionTest(false,Build.VERSION_CODES.Q,
                        getBindRunnable(permission.BIND_EXPLICIT_HEALTH_CHECK_SERVICE)));
        mPermissionTasks.put(permission.BIND_NETWORK_RECOMMENDATION_SERVICE,
                new PermissionTest(false,
                        getBindRunnable(permission.BIND_NETWORK_RECOMMENDATION_SERVICE)));
        mPermissionTasks.put(permission.BIND_CHOOSER_TARGET_SERVICE,
                new PermissionTest(false, getBindRunnable(permission.BIND_CHOOSER_TARGET_SERVICE)));
        mPermissionTasks.put(permission.BIND_INLINE_SUGGESTION_RENDER_SERVICE,
                new PermissionTest(false,Build.VERSION_CODES.R,
                        getBindRunnable(permission.BIND_INLINE_SUGGESTION_RENDER_SERVICE)));
        mPermissionTasks.put(permission.BIND_CONTENT_CAPTURE_SERVICE,
                new PermissionTest(false,Build.VERSION_CODES.Q,
                        getBindRunnable(permission.BIND_CONTENT_CAPTURE_SERVICE)));
        mPermissionTasks.put(permission.BIND_AUGMENTED_AUTOFILL_SERVICE,
                new PermissionTest(false,Build.VERSION_CODES.Q,
                        getBindRunnable(permission.BIND_AUGMENTED_AUTOFILL_SERVICE)));

        // The following are the new BIND_ permissions in Android 12.
        mPermissionTasks.put(permission.BIND_CALL_DIAGNOSTIC_SERVICE,
                new PermissionTest(false, Build.VERSION_CODES.S,
                        getBindRunnable(permission.BIND_CALL_DIAGNOSTIC_SERVICE)));
        mPermissionTasks.put(permission.BIND_COMPANION_DEVICE_SERVICE,
                new PermissionTest(false, Build.VERSION_CODES.S,
                        getBindRunnable(permission.BIND_COMPANION_DEVICE_SERVICE)));
        mPermissionTasks.put(permission.BIND_DISPLAY_HASHING_SERVICE,
                new PermissionTest(false, Build.VERSION_CODES.S,
                        getBindRunnable(permission.BIND_DISPLAY_HASHING_SERVICE)));
        mPermissionTasks.put(permission.BIND_DOMAIN_VERIFICATION_AGENT,
                new PermissionTest(false, Build.VERSION_CODES.S,
                        getBindRunnable(permission.BIND_DOMAIN_VERIFICATION_AGENT)));
        mPermissionTasks.put(permission.BIND_GBA_SERVICE,
                new PermissionTest(false, Build.VERSION_CODES.S,
                        getBindRunnable(permission.BIND_GBA_SERVICE)));
        mPermissionTasks.put(permission.BIND_HOTWORD_DETECTION_SERVICE,
                new PermissionTest(false, Build.VERSION_CODES.S,
                        getBindRunnable(permission.BIND_HOTWORD_DETECTION_SERVICE)));
        mPermissionTasks.put(permission.BIND_MUSIC_RECOGNITION_SERVICE,
                new PermissionTest(false, Build.VERSION_CODES.S,
                        getBindRunnable(permission.BIND_MUSIC_RECOGNITION_SERVICE)));
        mPermissionTasks.put(permission.BIND_RESUME_ON_REBOOT_SERVICE,
                new PermissionTest(false, Build.VERSION_CODES.S,
                        getBindRunnable(permission.BIND_RESUME_ON_REBOOT_SERVICE)));
        mPermissionTasks.put(permission.BIND_ROTATION_RESOLVER_SERVICE,
                new PermissionTest(false, Build.VERSION_CODES.S,
                        getBindRunnable(permission.BIND_ROTATION_RESOLVER_SERVICE)));
        mPermissionTasks.put(permission.BIND_TIME_ZONE_PROVIDER_SERVICE,
                new PermissionTest(false, Build.VERSION_CODES.S,
                        getBindRunnable(permission.BIND_TIME_ZONE_PROVIDER_SERVICE)));
        mPermissionTasks.put(permission.BIND_TRANSLATION_SERVICE,
                new PermissionTest(false, Build.VERSION_CODES.S,
                        getBindRunnable(permission.BIND_TRANSLATION_SERVICE)));

        //The following are the new BIND_ permissions in Android T
        mPermissionTasks.put(permission.BIND_ATTESTATION_VERIFICATION_SERVICE,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU,
                        getBindRunnable(permission.BIND_ATTESTATION_VERIFICATION_SERVICE)));
        mPermissionTasks.put(permission.BIND_TRACE_REPORT_SERVICE,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU,
                        getBindRunnable(permission.BIND_TRACE_REPORT_SERVICE)));
        mPermissionTasks.put(permission.BIND_GAME_SERVICE,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU,
                        getBindRunnable(permission.BIND_GAME_SERVICE)));
        mPermissionTasks.put(permission.BIND_SELECTION_TOOLBAR_RENDER_SERVICE,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU,
                        getBindRunnable(permission.BIND_SELECTION_TOOLBAR_RENDER_SERVICE)));
        mPermissionTasks.put(permission.BIND_WALLPAPER_EFFECTS_GENERATION_SERVICE,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU,
                        getBindRunnable(permission.BIND_WALLPAPER_EFFECTS_GENERATION_SERVICE)));
        mPermissionTasks.put(permission.BIND_TV_INTERACTIVE_APP,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU,
                        getBindRunnable(permission.BIND_TV_INTERACTIVE_APP)));
        mPermissionTasks.put(permission.BIND_AMBIENT_CONTEXT_DETECTION_SERVICE,
                new PermissionTest(false, Build.VERSION_CODES.TIRAMISU,
                        getBindRunnable(permission.BIND_AMBIENT_CONTEXT_DETECTION_SERVICE)));

        //The following are the new BIND_ permissions in Android U
        mPermissionTasks.put(permission.BIND_CALL_STREAMING_SERVICE,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE,
                        getBindRunnable(permission.BIND_CALL_STREAMING_SERVICE)));

        mPermissionTasks.put(permission.BIND_CREDENTIAL_PROVIDER_SERVICE,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE,
                        getBindRunnable(permission.BIND_CREDENTIAL_PROVIDER_SERVICE)));
        mPermissionTasks.put(permission.BIND_FIELD_CLASSIFICATION_SERVICE,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE,
                        getBindRunnable(permission.BIND_FIELD_CLASSIFICATION_SERVICE)));
        mPermissionTasks.put(permission.BIND_REMOTE_LOCKSCREEN_VALIDATION_SERVICE,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE,
                        getBindRunnable(permission.BIND_REMOTE_LOCKSCREEN_VALIDATION_SERVICE)));
        mPermissionTasks.put(permission.BIND_SATELLITE_GATEWAY_SERVICE,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE,
                        getBindRunnable(permission.BIND_SATELLITE_GATEWAY_SERVICE)));
        mPermissionTasks.put(permission.BIND_SATELLITE_SERVICE,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE,
                        getBindRunnable(permission.BIND_SATELLITE_SERVICE)));
        mPermissionTasks.put(permission.BIND_VISUAL_QUERY_DETECTION_SERVICE,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE,
                        getBindRunnable(permission.BIND_VISUAL_QUERY_DETECTION_SERVICE)));
        mPermissionTasks.put(permission.BIND_WEARABLE_SENSING_SERVICE,
                new PermissionTest(false, VERSION_CODES.UPSIDE_DOWN_CAKE,
                        getBindRunnable(permission.BIND_WEARABLE_SENSING_SERVICE)));

        //For Android 15 (Temporay allow runnning on UPSIDE_DOWN_CAKE)
        mPermissionTasks.put(permission.BIND_TV_AD_SERVICE,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE,
                        getBindRunnable(permission.BIND_TV_AD_SERVICE)));
        mPermissionTasks.put(permission.BIND_DOMAIN_SELECTION_SERVICE,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE,
                        getBindRunnable(permission.BIND_DOMAIN_SELECTION_SERVICE)));
        mPermissionTasks.put(permission.BIND_ON_DEVICE_INTELLIGENCE_SERVICE,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE,
                        getBindRunnable(permission.BIND_ON_DEVICE_INTELLIGENCE_SERVICE)));
        mPermissionTasks.put(permission.BIND_ON_DEVICE_SANDBOXED_INFERENCE_SERVICE,
                new PermissionTest(false, Build.VERSION_CODES.UPSIDE_DOWN_CAKE,
                        getBindRunnable(permission.BIND_ON_DEVICE_SANDBOXED_INFERENCE_SERVICE)));
    }





    /**
     * Set of BIND_ permissions that also require the UID to belong to the system for a bind to
     * complete successful; even when these permissions are granted with a platform signed app
     * the test must be skipped since the app cannot pass the UID check.
     */
    private static final Set<String> SYSTEM_ONLY_BIND_PERMISSIONS;
    static {
        SYSTEM_ONLY_BIND_PERMISSIONS = new HashSet<>();
        SYSTEM_ONLY_BIND_PERMISSIONS.add(permission.BIND_HOTWORD_DETECTION_SERVICE);
        SYSTEM_ONLY_BIND_PERMISSIONS.add(permission.BIND_VISUAL_QUERY_DETECTION_SERVICE);
    }

    /**
     * Creates a {@link Runnable} that can be used to test the the provided {@code permission} by
     * attempting to bind to the corresponding service in the companion app.
     */
    private Runnable getBindRunnable2(final String permission) {
        return () -> {
            if (SYSTEM_ONLY_BIND_PERMISSIONS.contains(permission) && isPermissionGranted(
                    permission)) {
                throw new BypassTestException(
                        "Only the system can bind to this service with this permission granted");
            }
            final CountDownLatch latch = new CountDownLatch(1);
            TestBindService[] service = new TestBindService[1];
            ServiceConnection serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName className, IBinder binder) {
                    mLogger.logDebug("onServiceConnected: className = " + className);
                    service[0] = TestBindService.Stub.asInterface(binder);
                    latch.countDown();
                }

                @Override
                public void onServiceDisconnected(ComponentName arg0) {
                    mLogger.logDebug("onServiceDisconnected: arg0 = " + arg0);
                }
            };
            // All of the services exported by the companion app app are named
            // Test<PermissionName>Service where <PermissionName> is an upper camel case conversion
            // of the snake case permission name. For instance the service for
            // BIND_ACCESSIBILITY_SERVICE is TestBindAccessibilityServiceService.
            String permissionName = permission.substring(permission.lastIndexOf('.') + 1);
            StringBuilder serviceName = new StringBuilder();
            serviceName.append("Test");
            for (String element : permission.substring(permission.lastIndexOf('.') + 1).split(
                    "_")) {
                serviceName.append(element.charAt(0)).append(
                        element.substring(1).toLowerCase());
            }
            serviceName.append("Service");
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(Constants.COMPANION_PACKAGE,
                    Constants.COMPANION_PACKAGE + ".services." + serviceName));
            mContext.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            boolean connectionSuccessful = false;
            try {
                mLogger.logDebug("Connecting "+intent);

                connectionSuccessful = latch.await(1000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                mLogger.logError(
                        "Caught an InterruptedException waiting for the service to connect: ",
                        e);
            }

            if (!connectionSuccessful) {
                throw new SecurityException(
                        "Unable to establish a connection to the service guarded by the "
                                + permissionName + " permission");
            }
            // Each service implements TestBindService which provides a single #testMethod
            // that is used to verify the service connection.
            try {
                service[0].testMethod();
            } catch (RemoteException e) {
                throw new UnexpectedPermissionTestFailureException(e);
            } finally {
                mContext.unbindService(serviceConnection);
            }
        };
    }


    private final Object lock = new Object();

    private class LocalServiceConnection implements android.content.ServiceConnection {
        public final AtomicBoolean binderSuccess = new AtomicBoolean();
        private final AtomicBoolean mConnected = new AtomicBoolean(false);
        public String mComponentName = "";
        public void onServiceConnected(ComponentName name, IBinder binder) {
            synchronized (lock) {
                mConnected.set(true);
                binderSuccess.set(true);
                mComponentName = name.getShortClassName();
                TestBindService service = TestBindService.Stub.asInterface(binder);
                try {
                    service.testMethod();
                } catch (RemoteException e) {
                    binderSuccess.set(false);
                    //e.printStackTrace();
                    mLogger.logError(name+" failure."+e.getMessage(),e);
                }
                lock.notify();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //Unimplemented
        }
    }

    public Object descriptorAsInterface(String descriptor) {
        Class clazz = null;
        Constructor c = null;
        Object o = null;
        Log.d(TAG,"Checking :"+descriptor);
        try {
            clazz = Class.forName(descriptor + "$Stub");
            //c = clazz.getDeclaredConstructor();
            //o = c.newInstance();
        } catch (ClassNotFoundException  e) {
            e.printStackTrace();
            return null;
        }
        Log.d(TAG,"Found :"+clazz);
        try {
            Method transactMethod = clazz.getDeclaredMethod("asInterface",IBinder.class);
            transactMethod.setAccessible(true);
            return transactMethod.invoke(o,getActivityToken());

        } catch (NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Runnable getBindRunnable(final String permission) {
        return () -> {
            if (SYSTEM_ONLY_BIND_PERMISSIONS.contains(permission) && isPermissionGranted(
                    permission)) {
                throw new BypassTestException(
                        "Only the system can bind to this service with this permission granted");
            }

            StringBuilder serviceName = new StringBuilder();
            serviceName.append("Test");
            for (String element : permission.substring(permission.lastIndexOf('.') + 1).split(
                    "_")) {
                serviceName.append(element.charAt(0)).append(
                        element.substring(1).toLowerCase());
            }
            serviceName.append("Service");
            //
            ExecutorService executorService = ((TesterApplication)mActivity.getApplication()).executorService;
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(Constants.COMPANION_PACKAGE,
                    Constants.COMPANION_PACKAGE + ".services." + serviceName));

            LocalServiceConnection serviceConnection = new LocalServiceConnection();
            mContext.bindService(intent, Context.BIND_AUTO_CREATE, executorService,serviceConnection);
            synchronized (lock) {
               try {
                   int i=0;
                   while (!serviceConnection.mConnected.get()) {
                       try {
                           //wait almost 1 sec along increasing waiting time
                           lock.wait(10+(i*i));
                           if(i++>=10){
                               throw new InterruptedException("Connection Timed Out");
                           }
                       } catch (InterruptedException e) {
                           throw new RuntimeException(e);
                       }
                   }
                   mLogger.logInfo("Connected To Service in the Companion app="+serviceConnection.mComponentName+
                           ","+serviceConnection.binderSuccess.get());
                   if(!serviceConnection.binderSuccess.get()){
                       throw new SecurityException("Test for "+serviceConnection.mComponentName+" has been failed.");
                   }
               } catch (Exception ex){
                  throw new UnexpectedPermissionTestFailureException(ex);
               } finally {
                 mContext.unbindService(serviceConnection);
               }
            }
        };
    }
    protected int modifyPermission(String permission,boolean bSwitch) {
        String flag = bSwitch?"grant":"revoke";
        String cmd = "am shell pm "+flag+" "+mContext.getPackageName()+" "+permission;
        return runShellCommand(cmd);
    }

    /**
     * Invokes the provided shell {@code command} as a permission test; a non-zero return code
     * is treated as a test failure.
     */
    protected void runShellCommandTest(String command) {
        int returnCode = runShellCommand(command);
        if (returnCode != 0) {
            throw new SecurityException(command + " failed with return code " + returnCode);
        }
    }

    /**
     * Invokes and logs the stdout / stderr of the provided shell {@code command}, returning the
     * exit code from the command.
     */
    protected int runShellCommand(String command) {
        try {
            mLogger.logDebug("Attempting to run command " + command);
            java.lang.Process process = Runtime.getRuntime().exec(command);
            int returnCode = process.waitFor();
            BufferedReader stdout = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            BufferedReader stderr = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()));
            StringBuilder stdoutBuilder = new StringBuilder();
            String line;
            while ((line = stdout.readLine()) != null) {
                stdoutBuilder.append(line + "\n");
            }

            StringBuilder stderrBuilder = new StringBuilder();
            while ((line = stderr.readLine()) != null) {
                stderrBuilder.append(line + "\n");
            }
            mLogger.logDebug("Process return code: " + returnCode);
            mLogger.logDebug("Process stdout: " + stdoutBuilder.toString());
            mLogger.logDebug("Process stderr: " + stderrBuilder.toString());
            return returnCode;
        } catch (Throwable e) {
            throw new UnexpectedPermissionTestFailureException(e);
        }
    }

    /**
     * Returns the {@link IBinder} token for the current activity.
     *
     * <p>This token can be used in any binder transaction that requires the activity's token.
     */
    public IBinder getActivityToken() {
        try {
            Field tokenField = Activity.class.getDeclaredField("mToken");
            tokenField.setAccessible(true);
            return (IBinder) tokenField.get(mActivity);
        } catch (ReflectiveOperationException e) {
            throw new UnexpectedPermissionTestFailureException(e);
        }
    }

    @Override
    public boolean runPermissionTests() {
        boolean allTestsPassed = true;
        List<String> permissions = mConfiguration.getSignaturePermissions().orElse(
                mSignaturePermissions);
        Set<String> permissionsToSkip = mConfiguration.getSkippedSignaturePermissions().orElse(
                Collections.emptySet());
        for (String permission : permissions) {
            if (permissionsToSkip.contains(permission)) {
                continue;
            }
            // if this is a signature permission with the privileged protection flag then skip it
            // if the app is configured to use the PrivilegedPermissionTester.
            if (mPrivilegedPermissions.contains(permission)
                    && Constants.USE_PRIVILEGED_PERMISSION_TESTER) {
                continue;
            }
            mLogger.logDebug("Starting test for permission: " + permission);
            boolean testPassed = true;
            // if there is a corresponding test for this permission then run it now.
            if (mPermissionTasks.containsKey(permission)) {
                testPassed = runPermissionTest(permission, mPermissionTasks.get(permission));

            } else {
                // else log whether the permission should be granted to this app
                testPassed = getAndLogTestStatus(permission);
            }
            if (!testPassed) {
                allTestsPassed = false;
            }
        }
        if (allTestsPassed) {
            mLogger.logInfo(
                    "*** PASSED - all signature permission tests completed successfully");
        } else {
            mLogger.logInfo(
                    "!!! FAILED - one or more signature permission tests failed");
        }
        return allTestsPassed;
    }
    public void runPermissionTestsByThreads(androidx.core.util.Consumer<Result> callback){
        Result.testerName = this.getClass().getSimpleName();

        List<String> permissions = mConfiguration.getSignaturePermissions().orElse(
                mSignaturePermissions);
        Set<String> permissionsToSkip = mConfiguration.getSkippedSignaturePermissions().orElse(
                Collections.emptySet());
        AtomicInteger cnt = new AtomicInteger(0);
        AtomicInteger err = new AtomicInteger(0);
        int cntmap = 0;
        final int total = permissions.size();
        for (String permission : permissions) {
            // If the permission has a corresponding task then run it.
            cntmap+=1;
            //mLogger.logDebug("Starting test for signature permission: "+String.format(Locale.US,
            //        "%d %d ",cntmap,total) + permission);

            Handler handler = ((TesterApplication) mActivity.getApplication()).mainThreadHandler;
            Thread thread = new Thread(() -> {
                // if this is a signature permission with the privileged protection flag then skip it
                // if the app is configured to use the PrivilegedPermissionTester.
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String tester = this.getClass().getSimpleName();
                        if (permissionsToSkip.contains(permission) || (mPrivilegedPermissions.contains(permission)
                                && Constants.USE_PRIVILEGED_PERMISSION_TESTER)) {
                            mLogger.logInfo(permission+" is skipped due to the configurations.");
                            callback.accept(new Result(true, permission, aiIncl(cnt), total,err.get(),tester).markNonTarget());
                        } else {
                            if (mPermissionTasks.containsKey(permission)) {
                                if (runPermissionTest(permission, mPermissionTasks.get(permission))) {
                                    callback.accept(new Result(true, permission, aiIncl(cnt), total,err.get(),tester));
                                } else {
                                    callback.accept(new Result(false, permission, aiIncl(cnt), total,aiIncl(err),tester));
                                }
                            } else {
                                //The task to skip.
                                if(!((MainActivity)mActivity).m_reduce_logs)
                                    mLogger.logInfo(permission+" is not a target permission.");

                                callback.accept(new Result(true, permission, aiIncl(cnt), total,err.get(),tester).markNonTarget());
                            }
                        }
                    }
                });
            });
            thread.setPriority(9);
            thread.start();
            try {
                thread.join(THREAD_JOIN_DELAY);
            } catch (InterruptedException e) {
                mLogger.logError(String.format(Locale.US,"%d %s failed due to the timeout.",cnt.get(),permission));
            }
        }
    }

    @Override
    public Map<String,PermissionTest> getRegisteredPermissions() {
        return mPermissionTasks;
    }

    @Override
    public boolean runPermissionTest(String permission, PermissionTest test) {
        boolean testPassed = true;
        if(!((MainActivity)mActivity).m_reduce_logs)
            mLogger.logInfo("Run Permission Test In "+permission);

        // if the permission does not exist then skp the test and return immediately
        if (!mPlatformPermissions.contains(permission)) {
            mLogger.logDebug("The permission " + permission
                    + " is not declared by the platform on this device");
            return true;
        }
        if (mDeviceApiLevel < test.mMinApiLevel) {
            mLogger.logDebug(
                    "permission " + permission + " is targeted for min API " + test.mMinApiLevel
                            + "; device API is " + mDeviceApiLevel);
            return true;
        } else if (mDeviceApiLevel > test.mMaxApiLevel) {
            mLogger.logDebug(
                    "permission " + permission + " is targeted for max API " + test.mMaxApiLevel
                            + "; device API is " + mDeviceApiLevel);
            return true;
        }
        //mLogger.logInfo(test.mIsCustom+"<=handler?");
        if (test.mIsCustom) {
            test.runTest();
        } else {
            boolean permissionGranted = isPermissionGranted(permission);
            try {
                test.runTest();
                // If the permission was granted then a SecurityException should not have been
                // thrown so the result of the test should match whether the permission was granted.
                testPassed = getAndLogTestStatus(permission, permissionGranted, true);
            } catch (SecurityException e) {
                // Similar to above if the permission was not granted then a SecurityException
                // should have been thrown so the result of the test should be the opposite of
                // whether the permission was granted.
                mLogger.logDebug(
                        "Caught a SecurityException for permission " + permission, e);
                if (e.getCause() != null) {
                    mLogger.logDebug("Cause of SecurityException: ", e.getCause());
                }
                testPassed = getAndLogTestStatus(permission, permissionGranted, false);
            } catch (BypassTestException bte) {
                mLogger.logTestSkipped(permission, permissionGranted, bte.getMessage());
            } catch (UnexpectedPermissionTestFailureException e) {
                mLogger.logDebug(
                        "Caught a UnexpectedPermissionTestFailureException for permission " + permission , e);
                testPassed = getAndLogTestStatus(permission, permissionGranted, false);;
            } catch (Throwable t) {
                // Some of the signature / privileged tests can fail for other reasons (primarily
                // due to the test app not having access to all necessary classes to invoke the
                // APIs guarded by signature permissions), but if they make it past the
                // SecurityException then the API should be considered successfully invoked.
                if (Constants.DEBUG) {
                    mLogger.logDebug("Caught a Throwable for permission " + permission , t);
                }
                testPassed = getAndLogTestStatus(permission, permissionGranted, true);
            }
        }
        return testPassed;
    }

    /**
     * Logs a status entry for the provided {@code permission}; the test app should only be granted
     * a signature level permission if it has been signed with the platform's signing key.
     *
     * @return boolean indicating whether the test for the {@code permission} was successful
     */
    protected boolean getAndLogTestStatus(String permission) {
        boolean testPassed = true;
        if (!mPlatformPermissions.contains(permission)) {
            mLogger.logDebug("The permission " + permission
                    + " is not declared by the platform on this device");
            return true;
        }
        boolean permissionGranted = isPermissionGranted(permission);
        // The development permission flag under signature permissions allows the permission to
        // be granted via adb, including when installed with the -g option. Since this app is
        // often installed with the -g flag signature development permissions are allowed to
        // be granted to the test app. However if a signature permission does not have the
        // development flag then its grant state should match whether the app is platform signed.
        if (!mPlatformSignatureMatch && permissionGranted && !mDevelopmentPermissions.contains(
                permission)) {
            testPassed = false;
        }
        if (mPlatformSignatureMatch && !permissionGranted) {
            testPassed = false;
        }
        mLogger.logInfo(
                permission + ": " + (testPassed ? "PASSED" : "FAILED") + " (granted = "
                        + permissionGranted + ", signature match = " + mPlatformSignatureMatch
                        + ")");
        return testPassed;
    }

    /**
     * Logs a status entry for the provided {@code permission} based on the specified {@code
     * permissionGranted} and {@code apiSuccessful} flags.
     *
     * <p>The test is considered successful if the grant state of the permission is the same as
     * both whether the app is platform signed and the API for the test is successful.
     *
     * @return boolean indicating whether the test for the {@code permission} was successful
     */
    @Override
    protected boolean getAndLogTestStatus(String permission, boolean permissionGranted,
            boolean apiSuccessful) {
        boolean testPassed = true;
        if (!mPlatformSignatureMatch && permissionGranted && !mDevelopmentPermissions.contains(
                permission)) {
            testPassed = false;
        }
        if (mPlatformSignatureMatch && !permissionGranted) {
            testPassed = false;
        }
        // the API should only be successful when the permission is granted.
        if (permissionGranted != apiSuccessful) {
            testPassed = false;
        }
        mLogger.logInfo(
                permission + ": " + (testPassed ? "PASSED" : "FAILED") + " (granted = "
                        + permissionGranted + ", api successful = " + apiSuccessful
                        + ", signature match = " + mPlatformSignatureMatch + ")");
        return testPassed;
    }

    private final IBinder genericIBinder = new IBinder() {
        @Nullable
        @Override
        public String getInterfaceDescriptor() throws RemoteException {
            return null;
        }

        @Override
        public boolean pingBinder() {
            return false;
        }

        @Override
        public boolean isBinderAlive() {
            return false;
        }

        @Nullable
        @Override
        public IInterface queryLocalInterface(@NonNull String s) {
            return null;
        }

        @Override
        public void dump(@NonNull FileDescriptor fileDescriptor,
                @Nullable String[] strings) throws RemoteException {

        }

        @Override
        public void dumpAsync(@NonNull FileDescriptor fileDescriptor,
                @Nullable String[] strings) throws RemoteException {

        }

        @Override
        public boolean transact(int i, @NonNull Parcel parcel,
                @Nullable Parcel parcel1, int i1) throws RemoteException {
            return false;
        }

        @Override
        public void linkToDeath(@NonNull DeathRecipient deathRecipient, int i)
                throws RemoteException {

        }

        @Override
        public boolean unlinkToDeath(@NonNull DeathRecipient deathRecipient,
                int i) {
            return false;
        }
    };
}
