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

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ADD_VOICEMAIL;
import static android.Manifest.permission.ANSWER_PHONE_CALLS;
import static android.Manifest.permission.BLUETOOTH_ADMIN;
import static android.Manifest.permission.BLUETOOTH_CONNECT;
import static android.Manifest.permission.BODY_SENSORS;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CALENDAR;
import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.WRITE_CALENDAR;
import static android.Manifest.permission.WRITE_CALL_LOG;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.WallpaperManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.AdvertisingSetCallback;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.media.MediaRecorder;
import android.media.MediaRecorder.AudioEncoder;
import android.media.MediaRecorder.OutputFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.CalendarContract.CalendarAlerts;
import android.provider.CalendarContract.Events;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.provider.VoicemailContract.Voicemails;
import android.telecom.TelecomManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import com.android.certifications.niap.permissions.config.TestConfiguration;
import com.android.certifications.niap.permissions.log.Logger;
import com.android.certifications.niap.permissions.log.LoggerFactory;
import com.android.certifications.niap.permissions.log.StatusLogger;
import com.android.certifications.niap.permissions.utils.SignaturePermissions;
import com.android.certifications.niap.permissions.utils.Transacts;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Permission tester to verify all runtime permissions properly guard their API, resources, etc.
 * Runtime permissions require consent from the user, either with the -g flag when installing via
 * adb or else through runtime prompts. This app only prompts the user for permission consent in
 * special cases, so to test the path where these permissions are granted the APK should be
 * installed with the -g flag.
 */
public class RuntimePermissionTester extends BasePermissionTester {
    private static final String TAG = "RuntimePermissionTester";
    private final Logger mLogger = LoggerFactory.createDefaultLogger(TAG);

    protected final WallpaperManager mWallpaperManager;
    protected final SensorManager mSensorManager;
    protected final TelephonyManager mTelephonyManager;
    protected final TelecomManager mTelecomManager;
    protected final AccountManager mAccountManager;
    protected final LocationManager mLocationManager;
    protected final CameraManager mCameraManager;

    private final Map<String, PermissionTest> mPermissionTasks;

    public RuntimePermissionTester(TestConfiguration configuration, Activity activity) {
        super(configuration, activity);

        mWallpaperManager = (WallpaperManager) mContext.getSystemService(Context.WALLPAPER_SERVICE);
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mTelephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        mTelecomManager = (TelecomManager) mContext.getSystemService(Context.TELECOM_SERVICE);
        mAccountManager = (AccountManager) mContext.getSystemService(Context.ACCOUNT_SERVICE);
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mCameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);

        mPermissionTasks = new HashMap<>();

        // android.permission.ACCEPT_HANDOVER - requires an active call which is then handed over
        // to the app.

        mPermissionTasks.put(BODY_SENSORS, new PermissionTest(false, () -> {
            if (!mPackageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_HEART_RATE)) {
                throw new BypassTestException(
                        "A heard rate monitor is not available to run this test");
            }
            Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
            if (sensor == null) {
                throw new SecurityException(
                        "The heart rate sensor feature is available, but a null sensor was "
                                + "returned");
            }
        }));

        mPermissionTasks.put(CALL_PHONE, new PermissionTest(false, () -> {
            mTelecomManager.endCall();
        }));

        // android.permission.GET_ACCOUNTS - is no longer used.

        // android.permission.PROCESS_OUTGOING_CALLS requires a call to be placed to redirect /
        // abort the call.

        mPermissionTasks.put(READ_CALENDAR, new PermissionTest(false, () -> {
            mContentResolver.query(CalendarAlerts.CONTENT_URI, null, null, null, null);
        }));

        mPermissionTasks.put(READ_CALL_LOG, new PermissionTest(false, () -> {
            CallLog.Calls.getLastOutgoingCall(mContext);
        }));

        // android.permission.READ_CELL_BROADCASTS is a hidden runtime permission, but even with
        // permission granted an app must also be on a whitelist to access the provider.

        mPermissionTasks.put(READ_CONTACTS, new PermissionTest(false, () -> {
            mContentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        }));

        mPermissionTasks.put(READ_EXTERNAL_STORAGE, new PermissionTest(false, () -> {
            mWallpaperManager.getWallpaperFile(WallpaperManager.FLAG_SYSTEM);
        }));

        mPermissionTasks.put(READ_PHONE_STATE, new PermissionTest(false, () -> {
            mTelephonyManager.getCarrierConfig();
        }));

        mPermissionTasks.put(READ_SMS, new PermissionTest(false, () -> {
            mContentResolver.query(Telephony.Sms.Inbox.CONTENT_URI, null, null, null, null);
        }));

        // android.permission.RECEIVE_MMS requires receipt of an MMS.

        // android.permission.RECEIVE_SMS requires receipt of an SMS.

        // android.permission.RECEIVE_WAP_PUSH requires receipt of WAP push messages.

        mPermissionTasks.put(SEND_SMS, new PermissionTest(false, () -> {
            String deviceNumber = null;
            if (!isPermissionGranted(READ_PHONE_NUMBERS)) {
                throw new BypassTestException(
                        "This test requires the READ_PHONE_NUMBERS permission to obtain the "
                                + "device number");
            }
            try {
                deviceNumber = mTelephonyManager.getLine1Number();
            } catch (SecurityException e) {
                // A SecurityException caught here indicates this app does not have the proper
                // permission to obtain the device number; since the test requires the device
                // number it should be skipped below.
            }
            if (deviceNumber == null) {
                throw new BypassTestException(
                        "The device number could not be obtained to verify SEND_SMS");
            }
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(deviceNumber, null, "Test message to verify SEND_SMS", null,
                    null);
        }));

        // android.permission.USE_SIP only guards methods that return null / false if the permission
        // is not granted; no way to distinguish between permission being granted or not.

        mPermissionTasks.put(WRITE_CALENDAR, new PermissionTest(false, () -> {
            ContentValues values = new ContentValues();
            values.put(Events.DTSTART, System.currentTimeMillis());
            values.put(Events.DTEND, System.currentTimeMillis());
            values.put(Events.TITLE, "Test Calendar Entry");
            values.put(Events.EVENT_TIMEZONE, "America/Los_Angeles");
            values.put(Events.CALENDAR_ID, 1);
            mContentResolver.insert(Events.CONTENT_URI, values);
        }));

        mPermissionTasks.put(WRITE_CALL_LOG, new PermissionTest(false, () -> {
            ContentValues values = new ContentValues();
            values.put(CallLog.Calls.NUMBER, "520-555-1234");
            values.put(CallLog.Calls.DATE, System.currentTimeMillis());
            values.put(CallLog.Calls.DURATION, 0);
            values.put(CallLog.Calls.TYPE, Calls.OUTGOING_TYPE);
            mContentResolver.insert(Calls.CONTENT_URI, values);
        }));

        mPermissionTasks.put(WRITE_CONTACTS, new PermissionTest(false, () -> {
            Account[] accounts = mAccountManager.getAccounts();
            if (accounts.length == 0) {
                throw new BypassTestException(
                        "This permission requires an account with contacts");
            } else {
                Account account = accounts[0];
                ContentValues values = new ContentValues();
                values.put(RawContacts.ACCOUNT_TYPE, account.type);
                values.put(RawContacts.ACCOUNT_NAME, account.name);
                Uri rawContactUri = mContentResolver.insert(RawContacts.CONTENT_URI, values);
                long rawContactId = ContentUris.parseId(rawContactUri);
                values.clear();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
                values.put(StructuredName.DISPLAY_NAME, "Test Contact");
                mContentResolver.insert(Data.CONTENT_URI, values);
            }
        }));

        mPermissionTasks.put(WRITE_EXTERNAL_STORAGE,
                new PermissionTest(false, Build.VERSION_CODES.P, Build.VERSION_CODES.Q, () -> {
                    // Due to scoped storage apps targeting API level 30+ no longer get any
                    // additional access with the WRITE_EXTERNAL_STORAGE permission; for details see
                    // https://developer.android.com/preview/privacy/storage#permissions-target-11.
                    try {
                        File documentsDirectory = Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DOCUMENTS);
                        if (!documentsDirectory.exists() && !documentsDirectory.mkdir()) {
                            throw new SecurityException(
                                    "Could not create the directory "
                                            + documentsDirectory.getAbsolutePath());
                        }
                        File file = new File(documentsDirectory, "test_file.out");
                        if (!file.createNewFile()) {
                            throw new SecurityException(
                                    "Could not create the temporary file "
                                            + file.getAbsolutePath());
                        }
                        ;
                        file.delete();
                    } catch (IOException e) {
                        // If the permission is not granted then this could fail with a
                        // 'Permission denied' IOException instead of a SecurityException.
                        String message = e.getMessage();
                        if (message != null && message.contains("Permission denied")) {
                            throw new SecurityException(e);
                        } else {
                            throw new UnexpectedPermissionTestFailureException(e);
                        }
                    }
                }));

        mPermissionTasks.put(ADD_VOICEMAIL, new PermissionTest(false, () -> {
            ContentValues values = new ContentValues();
            values.put(Voicemails.NUMBER, "520-555-1234");
            values.put(Voicemails.DATE, System.currentTimeMillis());
            values.put(Voicemails.DURATION, 10);
            values.put(Voicemails.NEW, 0);
            values.put(Voicemails.TRANSCRIPTION, "Testing ADD_VOICEMAIL");
            values.put(Voicemails.IS_READ, 0);
            values.put(Voicemails.HAS_CONTENT, 0);
            values.put(Voicemails.SOURCE_DATA, "1234");
            values.put(Voicemails.BACKED_UP, 0);
            values.put(Voicemails.RESTORED, 0);
            values.put(Voicemails.ARCHIVED, 0);
            values.put(Voicemails.IS_OMTP_VOICEMAIL, 0);
            values.put(Voicemails.SOURCE_PACKAGE, mPackageName);
            Uri voicemailUri = Uri.parse(
                    Voicemails.CONTENT_URI + "?source_package=" + mPackageName);
            mContentResolver.insert(voicemailUri, values);
        }));

        mPermissionTasks.put(ACCESS_COARSE_LOCATION, new PermissionTest(false, () -> {
            mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }));

        mPermissionTasks.put(ACCESS_FINE_LOCATION, new PermissionTest(false, () -> {
            mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }));

        mPermissionTasks.put(CAMERA, new PermissionTest(false, () -> {
            try {
                String[] cameras = mCameraManager.getCameraIdList();
                if (cameras.length == 0) {
                    throw new BypassTestException(
                            "No cameras were found on this device to perform this test");
                }
                CameraDevice.StateCallback cameraCallback = new CameraDevice.StateCallback() {
                    @Override
                    public void onDisconnected(CameraDevice camera) {
                        mLogger.logDebug("onDisconnected: camera = " + camera);
                    }

                    @Override
                    public void onError(CameraDevice camera, int error) {
                        mLogger.logDebug("onError: camera = " + camera + ", error = " + error);
                    }

                    @Override
                    public void onOpened(CameraDevice camera) {
                        mLogger.logDebug("onOpened: camera = " + camera);
                        camera.close();
                    }
                };
                mCameraManager.openCamera(cameras[0], cameraCallback,
                        new Handler(Looper.getMainLooper()));
            } catch (CameraAccessException e) {
                throw new UnexpectedPermissionTestFailureException(e);
            }
        }));

        mPermissionTasks.put(READ_PHONE_NUMBERS, new PermissionTest(false, () -> {
            mTelephonyManager.getLine1Number();
        }));

        mPermissionTasks.put(RECORD_AUDIO, new PermissionTest(false, () -> {
            try {
                MediaRecorder recorder = new MediaRecorder();
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                String fileName = mContext.getFilesDir() + "/test_record_audio.out";
                recorder.setOutputFile(new File(fileName));
                recorder.setOutputFormat(OutputFormat.THREE_GPP);
                recorder.setAudioEncoder(AudioEncoder.AMR_NB);
                recorder.prepare();
            } catch (RuntimeException e) {
                // The MediaRecorder framework throws a RuntimeException when the RECORD_AUDIO
                // permission is not granted to the calling app.
                throw new SecurityException(e);
            } catch (IOException ioe) {
                throw new UnexpectedPermissionTestFailureException(ioe);
            }
        }));

        mPermissionTasks.put(ANSWER_PHONE_CALLS, new PermissionTest(false, () -> {
            mTelecomManager.endCall();
        }));

        // New permissions for Q
        mPermissionTasks.put(Manifest.permission.ACTIVITY_RECOGNITION,
                new PermissionTest(false, Build.VERSION_CODES.Q, () -> {
                    Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
                    if (sensor == null) {
                        throw new BypassTestException(
                                "The step counter sensor is not available to execute this test");
                    }
                    SensorEventListener listener = new SensorEventListener() {
                        @Override
                        public void onAccuracyChanged(Sensor sensor, int accuracy) {
                            mLogger.logDebug(
                                    "onAccuracyChanged: sensor = " + sensor + ", accuracy = "
                                            + accuracy);
                        }

                        @Override
                        public void onSensorChanged(SensorEvent event) {
                            mLogger.logDebug("onSensorChanged: event = " + event);
                        }
                    };
                    boolean listenerRegistered = mSensorManager.registerListener(listener,
                            sensor,
                            SensorManager.SENSOR_DELAY_NORMAL);
                    if (!listenerRegistered) {
                        throw new SecurityException(
                                "Failed to register a listener for the STEP_COUNTER sensor");
                    }
                    mSensorManager.unregisterListener(listener);
                }));

        mPermissionTasks.put(Manifest.permission.ACCESS_MEDIA_LOCATION,
                new PermissionTest(false, Build.VERSION_CODES.Q, () -> {
                    String selection = MediaStore.Images.Media.MIME_TYPE + "='image/jpeg'" + " OR "
                            + MediaStore.Images.Media.MIME_TYPE + "='image/jpg'";
                    Cursor cursor = mContentResolver.query(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            new String[]{MediaStore.Images.Media.DESCRIPTION,
                                    MediaStore.Images.Media._ID}, selection, null, null);
                    if (cursor == null) {
                        throw new UnexpectedPermissionTestFailureException(
                                "Unable to obtain an image to test ACCESS_MEDIA_LOCATION");
                    }
                    if (cursor.getCount() == 0) {
                        if (isPermissionGranted(READ_EXTERNAL_STORAGE)) {
                            throw new UnexpectedPermissionTestFailureException(
                                    "Unable to obtain an image with location data to verify "
                                            + "ACCESS_MEDIA_LOCATION");
                        } else {
                            throw new BypassTestException(
                                    "READ_EXTERNAL_STORAGE not granted; unable to obtain "
                                            + "images");
                        }
                    }
                    while (cursor.moveToNext()) {
                        int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                        String elementId = cursor.getString(columnIndex);
                        Uri elementUri = Uri.parse(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI + "/" + elementId);
                        try (InputStream inputStream = mContentResolver.openInputStream(
                                elementUri)) {
                            ExifInterface exif = new ExifInterface(inputStream);
                            float[] latLong = new float[2];
                            exif.getLatLong(latLong);
                            // Not all images will have location data, ensure all images are
                            // tested before reporting an error.
                            if (latLong[0] == 0.0f && latLong[1] == 0.0f) {
                                continue;
                            } else {
                                return;
                            }
                        } catch (IOException e) {
                            mLogger.logError("Caught an IOException reading the image: ", e);
                        }
                    }
                    throw new SecurityException(
                            "Unable to obtain the location data from any image");
                }));

        // New permissions added to Android 12.
        // The following are the new runtime permissions for Android 12.
        mPermissionTasks.put(Manifest.permission.BLUETOOTH_ADVERTISE,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (!enableBluetoothAdapter(bluetoothAdapter)) {
                        throw new BypassTestException(
                                "The bluetooth adapter must be enabled for this test");
                    }
                    AdvertiseSettings settings = new AdvertiseSettings.Builder()
                            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                            .setConnectable(false)
                            .build();
                    AdvertiseData data = new AdvertiseData.Builder()
                            .setIncludeDeviceName(true)
                            .build();
                    AdvertiseCallback callback = new AdvertiseCallback() {
                        @Override
                        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                            super.onStartSuccess(settingsInEffect);
                            mLogger.logDebug(
                                    "onStartSuccess: settingsInEffect = " + settingsInEffect);
                        }

                        @Override
                        public void onStartFailure(int errorCode) {
                            super.onStartFailure(errorCode);
                            mLogger.logDebug("onStartFailure: errorCode = " + errorCode);
                        }
                    };
                    BluetoothLeAdvertiser advertiser = bluetoothAdapter.getBluetoothLeAdvertiser();
                    advertiser.startAdvertising(settings, data, callback);
                    advertiser.stopAdvertising(callback);
                }));

        mPermissionTasks.put(Manifest.permission.BLUETOOTH_CONNECT,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    BluetoothAdapter.getDefaultAdapter().getName();
                }));

        mPermissionTasks.put(Manifest.permission.BLUETOOTH_SCAN,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (!enableBluetoothAdapter(bluetoothAdapter)) {
                        throw new BypassTestException(
                                "The bluetooth adapter must be enabled for this test");
                    }
                    BluetoothAdapter.getDefaultAdapter().getScanMode();
                }));

        mPermissionTasks.put(Manifest.permission.UWB_RANGING,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    // The API guarded by this permission is also guarded by the signature
                    // permission UWB_PRIVILEGED, so if this signature permission is not granted
                    // then skip this test.
                    if (!isPermissionGranted(SignaturePermissions.permission.UWB_PRIVILEGED)) {
                        throw new BypassTestException(
                                "The UWB_PRIVILEGED permission must be granted for this test");
                    }
                    // The UwbManager with the API guarded by this permission is hidden, so a
                    // direct transact is required.
                    mTransacts.invokeTransact(Transacts.UWB_SERVICE, Transacts.UWB_DESCRIPTOR,
                            Transacts.getSpecificationInfo);
                }));
    }

    @Override
    public boolean runPermissionTests() {
        boolean allTestsPassed = true;
        List<String> permissions = mConfiguration.getRuntimePermissions().orElse(
                new ArrayList<>(mPermissionTasks.keySet()));
        for (String permission : permissions) {
            if (!runPermissionTest(permission, mPermissionTasks.get(permission))) {
                allTestsPassed = false;
            }
        }
        if (allTestsPassed) {
            StatusLogger.logInfo(
                    "*** PASSED - all runtime permission tests completed successfully");
        } else {
            StatusLogger.logInfo("!!! FAILED - one or more runtime permission tests failed");
        }
        return allTestsPassed;
    }

    /**
     * Enables the specified {@code bluetoothAdapter} if the required permission is granted.
     *
     * @param bluetoothAdapter the adapter to be enabled
     * @return {@code true} if the adapter is successfully enabled
     */
    private boolean enableBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
        // If the bluetooth adapter is enabled then no further work is required.
        if (bluetoothAdapter.isEnabled()) {
            return true;
        }
        // Android 12+ requires the BLUETOOTH_CONNECT permission to enable a bluetooth adapter.
        boolean canEnable = mDeviceApiLevel < Build.VERSION_CODES.S
                ? isPermissionGranted(BLUETOOTH_ADMIN) : isPermissionGranted(BLUETOOTH_CONNECT);
        if (!canEnable) {
            return false;
        }
        mLogger.logDebug(
                "The bluetooth adapter is not enabled, but the permission required to enable it "
                        + "has been granted; enabling now");
        bluetoothAdapter.enable();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            mLogger.logDebug("Caught an InterruptedException waiting for the"
                    + " bluetooth adapter to be enabled");
        }
        return bluetoothAdapter.isEnabled();
    }
}
