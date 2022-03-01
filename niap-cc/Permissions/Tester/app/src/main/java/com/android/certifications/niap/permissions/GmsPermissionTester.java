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

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.location.Location;
import android.util.SparseArray;

import com.android.certifications.niap.permissions.config.TestConfiguration;
import com.android.certifications.niap.permissions.log.Logger;
import com.android.certifications.niap.permissions.log.LoggerFactory;
import com.android.certifications.niap.permissions.log.StatusLogger;
import com.android.certifications.niap.permissions.utils.PermissionUtils;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.location.ActivityTransitionRequest;
import com.google.android.gms.location.ActivityTransitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;

import java.io.IOException;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * Google Play Services / GMS allows app developers to take advantage of Google-powered features
 * through client side libraries. This permission tester verifies client libraries that require a
 * platform permission respond as expected both when an app has not been granted the required
 * permission and when the app has been granted the permission.
 */
public class GmsPermissionTester extends BasePermissionTester {
    private static final String TAG = "GmsPermissionTester";
    private final Logger mLogger = LoggerFactory.createDefaultLogger(TAG);

    /**
     * Map of permission to a Runnable that can be used to verify the client library API that is
     * guarded by the permission.
     */
    private final Map<String, PermissionTest> mPermissionTasks;

    /**
     * List of all Google Play Services declared signature permissions used to verify that an app
     * not signed with the same key as GMS or the platform is not granted these permissions.
     */
    private static final List<String> GMS_SIGNATURE_PERMISSIONS;
    static {
        GMS_SIGNATURE_PERMISSIONS = new ArrayList<>();
        GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.auth.api.phone.permission.SEND");
        GMS_SIGNATURE_PERMISSIONS.add(
                "com.google.android.gms.trustagent.framework.model.DATA_CHANGE_NOTIFICATION");
        GMS_SIGNATURE_PERMISSIONS.add(
                "com.google.android.gms.matchstick.permission.BROADCAST_LIGHTER_WEB_INFO");
        GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.APPINDEXING");
        GMS_SIGNATURE_PERMISSIONS.add(
                "com.google.android.gms.permission.BIND_NETWORK_TASK_SERVICE");
        GMS_SIGNATURE_PERMISSIONS.add(
                "com.google.android.gms.auth.authzen.permission.DEVICE_SYNC_FINISHED");
        GMS_SIGNATURE_PERMISSIONS.add(
                "com.google.android.gms.chromesync.permission.CONTENT_PROVIDER_ACCESS");
        GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.CHECKIN_NOW");
        GMS_SIGNATURE_PERMISSIONS.add(
                "com.google.android.gms.learning.permission.LAUNCH_IN_APP_PROXY");
        GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.SEND_ANDROID_PAY_DATA");
        GMS_SIGNATURE_PERMISSIONS.add(
                "com.google.android.gms.auth.authzen.permission.GCM_DEVICE_PROXIMITY");
        GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.NEARBY_START_DISCOVERER");
        GMS_SIGNATURE_PERMISSIONS.add(
                "com.google.android.gms.auth.permission.POST_SIGN_IN_ACCOUNT");
        GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.REPORT_TAP");
        GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.C2D_MESSAGE");
        GMS_SIGNATURE_PERMISSIONS.add(
                "com.google.firebase.auth.api.gms.permission.LAUNCH_FEDERATED_SIGN_IN");
        GMS_SIGNATURE_PERMISSIONS.add(
                "com.google.android.gms.nearby.exposurenotification.EXPOSURE_CALLBACK");
        GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.PHENOTYPE_OVERRIDE_FLAGS");
        GMS_SIGNATURE_PERMISSIONS.add(
                "com.google.android.gms.auth.api.signin.permission.REVOCATION_NOTIFICATION");
        GMS_SIGNATURE_PERMISSIONS.add(
                "com.google.android.gms.magictether.permission.CONNECTED_HOST_CHANGED");
        GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.CONTACTS_SYNC_DELEGATION");
        GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.GAMES_DEBUG_SETTINGS");
        GMS_SIGNATURE_PERMISSIONS.add(
                "com.google.android.gms.trustagent.permission.TRUSTAGENT_STATE");
        GMS_SIGNATURE_PERMISSIONS.add(
                "com.google.android.gms.permission.PHENOTYPE_UPDATE_BROADCAST");
        GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.INTERNAL_BROADCAST");
        GMS_SIGNATURE_PERMISSIONS.add(
                "com.google.android.gms.auth.proximity.permission.SMS_CONNECT_SETUP_REQUESTED");
        GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.GOOGLE_PAY");
        GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.chimera.permission.QUERY_MODULES");
        GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.CAR");
        GMS_SIGNATURE_PERMISSIONS.add(
                "com.google.android.gms.contextmanager.CONTEXT_MANAGER_RESTARTED_BROADCAST");
        GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.carsetup.DRIVING_MODE_MANAGER");
        GMS_SIGNATURE_PERMISSIONS.add(
                "com.google.android.gms.permission.SHOW_PAYMENT_CARD_DETAILS");
        GMS_SIGNATURE_PERMISSIONS.add(
                "com.google.android.gms.magictether.permission.SCANNED_DEVICE");
        GMS_SIGNATURE_PERMISSIONS.add(
                "com.google.android.gms.permission.BIND_PAYMENTS_CALLBACK_SERVICE");
        GMS_SIGNATURE_PERMISSIONS.add(
                "com.google.android.gms.permission.GRANT_WALLPAPER_PERMISSIONS");
        GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.chimera.permission.CONFIG_CHANGE");
        GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.auth.permission.FACE_UNLOCK");
        GMS_SIGNATURE_PERMISSIONS.add(
                "com.google.android.gms.auth.permission.GOOGLE_ACCOUNT_CHANGE");
        GMS_SIGNATURE_PERMISSIONS.add(
                "com.google.android.gms.trustagent.framework.model.DATA_ACCESS");
        GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.SHOW_TRANSACTION_RECEIPT");
        GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.SAFETY_NET");
        GMS_SIGNATURE_PERMISSIONS.add(
                "com.google.android.gms.common.internal.SHARED_PREFERENCES_PERMISSION");
        GMS_SIGNATURE_PERMISSIONS.add(
                "com.google.android.gms.permission.SHOW_WARM_WELCOME_TAPANDPAY_APP");
        GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.BROADCAST_TO_GOOGLEHELP");
        GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.READ_VALUABLES_IMAGES");
        GMS_SIGNATURE_PERMISSIONS.add(
                "com.google.android.gms.auth.authzen.permission.KEY_REGISTRATION_FINISHED");
        GMS_SIGNATURE_PERMISSIONS.add(
                "com.google.android.gms.magictether.permission"
                        + ".CLIENT_TETHERING_PREFERENCE_CHANGED");
        GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.DRIVE");
        GMS_SIGNATURE_PERMISSIONS.add(
                "com.google.android.gms.auth.cryptauth.permission.KEY_CHANGE");
        GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.GROWTH");
        GMS_SIGNATURE_PERMISSIONS.add(
                "com.google.android.gms.magictether.permission.DISABLE_SOFT_AP");
        GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.cloudsave.BIND_EVENT_BROADCAST");
        GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.WRITE_VERIFY_APPS_CONSENT");
        GMS_SIGNATURE_PERMISSIONS.add(
                "com.google.android.gms.googlehelp.LAUNCH_SUPPORT_SCREENSHARE");
        GMS_SIGNATURE_PERMISSIONS.add("com.android.vending.APP_ERRORS_SERVICE");
        GMS_SIGNATURE_PERMISSIONS.add(
                "com.google.android.gms.auth.cryptauth.permission.CABLEV2_SERVER_LINK");
        GMS_SIGNATURE_PERMISSIONS.add(
                "com.google.android.gms.vehicle.permission.SHARED_AUTO_SENSOR_DATA");
    }

    public GmsPermissionTester(TestConfiguration configuration, Activity activity) {
        super(configuration, activity);
        mPermissionTasks = new HashMap<>();

        mPermissionTasks.put(Manifest.permission.ACTIVITY_RECOGNITION,
                new PermissionTest(false, () -> {
                    final String ACTIVITY_RECOGNITION_TEST = "ACTIVITY_RECOGNITION_TEST";
                    CountDownLatch[] latch = new CountDownLatch[1];
                    latch[0] = new CountDownLatch(1);

                    BroadcastReceiver receiver = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            mLogger.logDebug("onReceive invoked with intent: " + intent);
                            ActivityTransitionResult result =
                                    ActivityTransitionResult.extractResult(intent);
                            ActivityRecognitionResult result2 =
                                    ActivityRecognitionResult.extractResult(
                                            intent);

                            // The countdown on the latch should only occur if the provided
                            // intent includes a valid activity update.
                            if (result != null) {
                                mLogger.logDebug("Received a broadcast with the activity result: "
                                        + result.toString());
                                latch[0].countDown();
                            } else {
                                mLogger.logDebug(
                                        "Received a broadcast with a null activity result");
                            }
                        }
                    };
                    mContext.registerReceiver(receiver,
                            new IntentFilter(ACTIVITY_RECOGNITION_TEST));
                    Intent intent = new Intent(ACTIVITY_RECOGNITION_TEST);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent,
                            PendingIntent.FLAG_MUTABLE);

                    // The ActivityRecognitionClient will throw a SecurityException when waiting for
                    // the Task from the #requestActivityUpdates to complete.
                    ActivityRecognitionClient activityClient = ActivityRecognition.getClient(
                            mContext);
                    Task<Void> activityRecognitionTask = activityClient.requestActivityUpdates(0, pendingIntent);
                    try {
                        Tasks.await(activityRecognitionTask, 10, TimeUnit.SECONDS);
                    } catch (ExecutionException | TimeoutException | InterruptedException e) {
                        // The ExecutionException typically wraps an ApiException with the message
                        // from a SecurityException if the permission is not granted. If the
                        // SecurityException text is in the message then treat the API call as
                        // having failed due to the permission not being granted.
                        if (e.getMessage().contains("SecurityException")) {
                            throw new SecurityException(e);
                        } else {
                            throw new UnexpectedPermissionTestFailureException(e);
                        }
                    } finally {
                        activityClient.removeActivityTransitionUpdates(pendingIntent);
                        activityClient.removeActivityUpdates(pendingIntent);
                        mContext.unregisterReceiver(receiver);
                    }
                }));

        mPermissionTasks.put(Manifest.permission.ACCESS_COARSE_LOCATION,
                new PermissionTest(false, () -> {
                    final String ACCESS_COARSE_LOCATION_TEST = "ACCESS_COARSE_LOCATION_TEST";
                    CountDownLatch[] latch = new CountDownLatch[1];
                    latch[0] = new CountDownLatch(1);

                    BroadcastReceiver receiver = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            LocationResult result = LocationResult.extractResult(intent);
                            // The countdown on the latch should only occur if the provided
                            // intent includes a valid location since additional updates can be sent
                            // without a location.
                            if (result != null) {
                                Location location = result.getLastLocation();
                                if (location != null) {
                                    mLogger.logDebug(
                                            "Received a lat,long of " + location.getLatitude()
                                                    + ", "
                                                    + location.getLongitude());
                                    latch[0].countDown();
                                }
                            }
                        }
                    };
                    mContext.registerReceiver(receiver,
                            new IntentFilter(ACCESS_COARSE_LOCATION_TEST));
                    Intent intent = new Intent(ACCESS_COARSE_LOCATION_TEST);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent,
                            PendingIntent.FLAG_MUTABLE);

                    LocationRequest locationRequest = LocationRequest.create().setPriority(
                            LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY).setInterval(1000);
                    FusedLocationProviderClient locationClient =
                            LocationServices.getFusedLocationProviderClient(mContext);
                    locationClient.requestLocationUpdates(locationRequest, pendingIntent);
                    boolean locationReceived = false;
                    try {
                        locationReceived = latch[0].await(30, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        mLogger.logError("Caught an InterruptedException: ", e);
                    }
                    locationClient.removeLocationUpdates(pendingIntent);
                    mContext.unregisterReceiver(receiver);
                    if (!locationReceived) {
                        throw new SecurityException("A location update was not received");
                    }
                }));

        mPermissionTasks.put(Manifest.permission.ACCESS_FINE_LOCATION,
                new PermissionTest(false, () -> {
                    Task<Location> locationTask = LocationServices.getFusedLocationProviderClient(
                            mContext).getLastLocation();
                    try {
                        Tasks.await(locationTask, 5, TimeUnit.SECONDS);
                        Location location = locationTask.getResult();
                        if (location == null) {
                            throw new SecurityException("Unable to obtain the last location");
                        } else {
                            mLogger.logDebug(
                                    "Received a lat,long of " + location.getLatitude() + ", "
                                            + location.getLongitude());
                        }
                    } catch (ExecutionException e) {
                        if (e.getCause() instanceof SecurityException) {
                            throw (SecurityException) e.getCause();
                        }
                    } catch (TimeoutException | InterruptedException e) {
                        throw new UnexpectedPermissionTestFailureException(e);
                    }
                }));

        mPermissionTasks.put(Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                new PermissionTest(false, () -> {
                    final String ACCESS_BACKGROUND_LOCATION_TEST =
                            "ACCESS_BACKGROUND_LOCATION_TEST";
                    CountDownLatch[] latch = new CountDownLatch[1];
                    latch[0] = new CountDownLatch(1);
                    BroadcastReceiver receiver = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            String action = intent.getAction();
                            LocationResult result = LocationResult.extractResult(intent);
                            // The countdown on the latch should only occur if the provided intent
                            // includes a valid location since additional updates can be sent
                            // without a location.
                            if (result != null) {
                                Location location = result.getLastLocation();
                                if (location != null) {
                                    mLogger.logDebug(
                                            "Received a lat,long of " + location.getLatitude()
                                                    + ", "
                                                    + location.getLongitude());
                                    latch[0].countDown();
                                }
                            }
                        }
                    };
                    mContext.registerReceiver(receiver,
                            new IntentFilter(ACCESS_BACKGROUND_LOCATION_TEST));
                    Intent intent = new Intent(ACCESS_BACKGROUND_LOCATION_TEST);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent,
                            PendingIntent.FLAG_IMMUTABLE);

                    GeofencingClient geofencingClient = LocationServices.getGeofencingClient(
                            mContext);
                    Geofence geofence = new Geofence.Builder().setRequestId(
                            "ACCESS_FINE_LOCATION_GEOFENCE_TEST").setCircularRegion(37.422136,
                            -122.084068,
                            1000)
                            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                            .setExpirationDuration(5000)
                            .build();
                    GeofencingRequest geofencingRequest =
                            new GeofencingRequest.Builder().setInitialTrigger(
                                    GeofencingRequest.INITIAL_TRIGGER_ENTER).addGeofence(
                                    geofence).build();
                    Task<Void> geofenceTask = geofencingClient.addGeofences(geofencingRequest,
                            pendingIntent);
                    try {
                        Tasks.await(geofenceTask);
                        geofenceTask.getResult();
                    } catch (ExecutionException e) {
                        // While the Geofencing Binder invocation will fail due to a
                        // SecurityException the ExecutionException does not contain the
                        // SecurityException as a cause, but it does contain the text from the
                        // SecurityException in the message.
                        if (e.getMessage().indexOf("SecurityException") != -1) {
                            throw new SecurityException(e);
                        }
                        throw new UnexpectedPermissionTestFailureException(e);
                    } catch (Exception e) {
                        throw new UnexpectedPermissionTestFailureException(e);
                    }
                    try {
                        latch[0].await(5, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        throw new UnexpectedPermissionTestFailureException(e);
                    }

                }));

        mPermissionTasks.put(Manifest.permission.CAMERA, new PermissionTest(false, () -> {
            CountDownLatch[] latch = new CountDownLatch[1];
            latch[0] = new CountDownLatch(1);

            Detector<Integer> detector = new Detector() {
                @Override
                public SparseArray<Integer> detect(Frame frame) {
                    mLogger.logDebug("detect: frame = " + frame);
                    latch[0].countDown();
                    return null;
                }
            };
            Detector.Processor<Integer> processor = new Detector.Processor() {
                @Override
                public void receiveDetections(Detector.Detections detections) {
                    mLogger.logDebug("receiveDetections: detections = " + detections);
                }

                @Override
                public void release() {
                    mLogger.logDebug("release");
                }
            };
            detector.setProcessor(processor);
            CameraSource cameraSource = new CameraSource.Builder(mContext, detector).setFacing(
                    CameraSource.CAMERA_FACING_BACK).setAutoFocusEnabled(true).setRequestedFps(
                    1).setRequestedPreviewSize(1024, 768).build();
            try {
                cameraSource.start();
            } catch (IOException e) {
                throw new UnexpectedPermissionTestFailureException(e);
            } catch (RuntimeException e) {
                if (e.getMessage().contains("Fail to connect to camera service")) {
                    // A RuntimeException occurs when the CAMERA permission check fails; wrap the
                    // exception in a SecurityException to fail the API call for this test.
                    throw new SecurityException(e);
                }
                throw e;
            } finally {
                cameraSource.release();
            }
        }));
    }

    @Override
    public boolean runPermissionTests() {
        boolean allTestsPassed = true;
        List<String> gmsDeclaredPermissions = getAllGmsDeclaredSignaturePermissions();
        List<String> defaultPermissions = new ArrayList<>(mPermissionTasks.keySet());
        defaultPermissions.addAll(GMS_SIGNATURE_PERMISSIONS);
        List<String> permissions = mConfiguration.getPermissions().orElse(defaultPermissions);
        // An app should only have access to the GMS signature permissions if it is signed with the
        // GMS signing key or the platform signing key.
        boolean signatureMatch = mPackageManager.hasSigningCertificate(Constants.GMS_PACKAGE_NAME,
                mAppSignature.toByteArray(), PackageManager.CERT_INPUT_RAW_X509);
        for (String permission : permissions) {
            // If the permission has a corresponding task then run it.
            if (mPermissionTasks.containsKey(permission)) {
                if (!runPermissionTest(permission, mPermissionTasks.get(permission))) {
                    allTestsPassed = false;
                }
            } else {
                if (!gmsDeclaredPermissions.contains(permission)) {
                    mLogger.logDebug("Permission " + permission
                            + " is not declared as a signature permission on this version of GMS");
                    continue;
                }
                boolean permissionGranted = mContext.checkSelfPermission(permission)
                        == PackageManager.PERMISSION_GRANTED;
                if (permissionGranted != (signatureMatch || mPlatformSignatureMatch)) {
                    allTestsPassed = false;
                }
                StatusLogger.logSignaturePermissionStatus(permission, permissionGranted,
                        signatureMatch, mPlatformSignatureMatch);
            }
        }
        if (allTestsPassed) {
            StatusLogger.logInfo(
                    "*** PASSED - all GMS permission tests completed successfully");
        } else {
            StatusLogger.logInfo(
                    "!!! FAILED - one or more GMS permission tests failed");
        }
        return allTestsPassed;
    }

    /**
     * Returns all of the signature protection level permissions declared by GMS.
     */
    private List<String> getAllGmsDeclaredSignaturePermissions() {
        List<PermissionInfo> permissions = PermissionUtils.getAllDeclaredPermissions(mContext);
        return permissions
                .stream()
                .filter(permission ->
                        Constants.GMS_PACKAGE_NAME.equals(permission.packageName)
                                && permission.getProtection()
                                == PermissionInfo.PROTECTION_SIGNATURE)
                .map(permission -> permission.name)
                .collect(Collectors.toList());

    }
}
