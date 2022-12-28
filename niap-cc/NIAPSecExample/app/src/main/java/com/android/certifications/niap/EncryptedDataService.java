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

package com.android.certifications.niap;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.android.certifications.niap.niapsec.SecureConfig;
import com.android.certifications.niap.niapsec.biometric.BiometricSupport;
import com.android.certifications.niap.niapsec.biometric.BiometricSupportImpl;
import com.android.certifications.niap.niapsec.crypto.SecureCipher;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.File;

/**
 * Class to help developers test by executing encryption operations in the background
 * while the device is locked. This is important to ensure that the keys are only available when
 * the user has authenticated.
 */
public class EncryptedDataService extends IntentService {

    public static String START_FOREGROUND_ACTION = "start-encryption-foreground-service";
    public static String STOP_FOREGROUND_ACTION = "stop-encryption-foreground-service";
    public static int NOTIFICATION_ID = 110;
    public static String NOTIFICATION_CHANNEL_ID =
            "com.android.certifications.niap.EncryptedDataService";
    private static String BIOMETRIC_AUTH= "Biometric Auth";

    public static String TAG = "EncryptedDataService";
    EncryptionManager dataManager;
    private boolean deviceLocked = false;
    private UpdateViewModel viewModel;
    private BiometricSupport biometricSupport;

    private static byte[] encryptedData = null;

    private boolean serviceRunning = true;

    private String symmetricKeyAlias = "sensitive_key_sym";
    private String asymmetricKeyPairAlias = "sensitive_keypair_asym";
    private String testFileName = "test_data.txt";
    private String testDataString = "ALL THE THINGS...";

    public EncryptedDataService() {
        super("EncryptedDataService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.viewModel = MainActivity.viewModel;
        biometricSupport = new BiometricSupportImpl(MainActivity.thisActivity,
                getApplicationContext(),true) {
            @Override
            public void onAuthenticationSucceeded() {
                showMessage(BIOMETRIC_AUTH + " Succeeded!");
            }

            @Override
            public void onAuthenticationFailed() {
                onMessage(BIOMETRIC_AUTH + " Failed");
            }

            public void onMessage(String message) {
                showMessage(message);
            }
        };
        dataManager = new EncryptionManager(getApplicationContext(), biometricSupport);
        deviceLocked = dataManager.deviceLocked();
    }

    private void showMessage(String message) {
        Log.i(TAG, message);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals(START_FOREGROUND_ACTION)) {
                startForegroundService();
            } else if (intent.getAction().equals(STOP_FOREGROUND_ACTION)) {
                killService();
            }
        }
        return START_STICKY;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {

            File[] files = getApplicationContext().getFilesDir().listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
            createKeys(testFileName);
            serviceRunning = false;
            while (serviceRunning) {
                Log.i(TAG, "Running Encryption Scenario...");
            }
        } catch (Exception ex) {
            Log.i(TAG, "There was an error! " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void createKeys(String fileName) {
        dataManager.createSensitiveDataSymmetricKey(symmetricKeyAlias);
        dataManager.createSensitiveDataAsymmetricKeyPair(asymmetricKeyPairAlias);

        Log.i(TAG, "Encrypting..." + testDataString);
        dataManager.encryptData(
                symmetricKeyAlias,
                asymmetricKeyPairAlias,
                testDataString.getBytes(),
                (byte[] encryptedData) -> {
            SecureCipher secureCipher = SecureCipher.getDefault((SecureConfig) biometricSupport);
            secureCipher.decryptEncodedData(encryptedData, (byte[] decryptedData) -> {
                Log.i(TAG, "Decrypted... " + new String(decryptedData));
                boolean encrypted = encryptData(fileName, (byte[] cipherText) -> {
                    Log.i(TAG, "File saved");
                    decodeData(fileName, clearText -> {
                        Log.i(TAG, "unencrypted file. " + new String(clearText));
                    });
                });
                Log.i(TAG, "" + encrypted);
            });
        });
    }

    private boolean encryptData(
            String fileName,
            SecureCipher.SecureAsymmetricEncryptionCallback callback) {
        return dataManager.encryptData(
                fileName,
                symmetricKeyAlias,
                asymmetricKeyPairAlias,
                testDataString.getBytes(),
                callback);
    }

    private void decodeData(String fileName, SecureCipher.SecureDecryptionCallback callback) {
        dataManager.decryptData(fileName, callback);
    }

    public void killService() {
        Log.i(TAG, "Killing the service.");
        serviceRunning = false;
        stopForeground(true);
        stopSelf();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startForegroundService() {
        NotificationChannel chan = new NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Security Extensions",
                NotificationManager.IMPORTANCE_DEFAULT);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(chan);

        Intent notificationIntent = new Intent(this, EncryptedDataService.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification.Builder notificationBuilder = new Notification.Builder(
                this,
                NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setContentText("Encrypting Random Data...")
                .setCategory(Notification.CATEGORY_SERVICE)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(NOTIFICATION_ID, notification);
    }

}