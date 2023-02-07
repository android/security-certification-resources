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

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import com.android.certifications.niap.niapsec.SecureConfig;
import com.android.certifications.niap.niapsec.biometric.BiometricSupport;
import com.android.certifications.niap.niapsec.crypto.SecureCipher;
import com.android.certifications.niap.niapsec.net.SecureURL;
import com.android.certifications.niap.niapsecexample.R;
import com.android.certifications.niap.tests.SDPAuthFailureTestWorker;
import com.android.certifications.niap.tests.SDPTestWorker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Sample Tool for OEMs to run Sensitive Data Protection tests using the NIAPSEC library.
 *
 * To run tests, please configure the URL for TLS and OCSP tests via the text box.
 *
 * The checkbox to delay running of the tests is to give the user time to lock the device.
 *
 * Some labs may ask that you test while the device is locked to ensure that the keys are not
 * available for decryption (encryption in this case is OK).
 *
 */
public class MainActivity extends FragmentActivity {

    private static String TAG = "MainActivity";
    public static FragmentActivity thisActivity;
    private TextView textView;
    private CheckBox runInBackgroundCheckBox;
    private boolean serviceRunning = false;
    public static UpdateViewModel viewModel;
    private BiometricSupport biometricSupport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        thisActivity = this;
        textView = (TextView) findViewById(R.id.output_textview);
        runInBackgroundCheckBox = findViewById(R.id.run_in_background);
        viewModel = ViewModelProviders.of(this).get(UpdateViewModel.class);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runTest();
                showMessage("Please see logcat for details!");
            }
        });

        viewModel.updateStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(final @Nullable String s) {
                thisActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        textView.append(s + System.getProperty("line.separator"));
                        Log.i(TAG, s);
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(
                Context.WIFI_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, wifi.getConfiguredNetworks().toString());
            return;
        }

        Log.i(TAG, wifi.getConnectionInfo().toString());
    }

    private void runTest() {
        /*
            Runs in background if the delay checkbox is checked.
            This provides an additional test to check and ensure that data cannot be decrypted.
            The key should not be available for decryption while the device is locked.
        */
        int initialDelay = 1;
        if(runInBackgroundCheckBox.isChecked()) {
            Log.i(TAG, "LOCK DEVICE NOW...");
            Log.i(TAG, "!!!!!!!!!!LOCK DEVICE NOW...!!!!");
            initialDelay = 6;
        }
        OneTimeWorkRequest sdpTestWorker = new OneTimeWorkRequest.Builder(SDPTestWorker.class)
                .setInitialDelay(initialDelay, TimeUnit.SECONDS)
                .build();
        OneTimeWorkRequest sdpFailureTestWorker =
                new OneTimeWorkRequest.Builder(SDPAuthFailureTestWorker.class)
                        .build();

        WorkManager.getInstance()
                .beginWith(sdpTestWorker)
                .then(sdpFailureTestWorker)
                .enqueue();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    SecureConfig secureConfig = SecureConfig.getStrongConfig();
                    SecureURL secureURL = new SecureURL("https://www.google.com", null);
                    URLConnection conn = secureURL.openConnection();
                    conn.connect();

                } catch(Exception ex) {
                    Log.e(TAG, "SecureURL Failure: " + ex.getMessage());
                    Log.e(TAG, ex.getStackTrace().toString());
                }
            }
        });
        executor.shutdown();
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


    }

    private void showMessage(String message) {
        Snackbar.make(textView, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void testOAEP() {
        EncryptionManager encryptionManager = new EncryptionManager(this, biometricSupport);
        Log.i(TAG, "Creating Keypair OAEP_TESTING_RSA");
        encryptionManager.createSensitiveDataAsymmetricKeyPair("OAEP_TESTING_RSA");
        Log.i(TAG, "Created Keypair OAEP_TESTING_RSA");
        byte[] clearText = new String("SO MUCH TESTING!").getBytes();

        Log.i(TAG, "Encrypting " + new String(clearText));

        SecureCipher secureCipher = SecureCipher.getDefault(SecureConfig.getDefault(biometricSupport));
        secureCipher.encryptSensitiveDataAsymmetric(
                "OAEP_TESTING_RSA",
                clearText,
                (byte[] cipherText) -> {
            Log.i(TAG, "Encrypted Text = " + new String(cipherText));
            secureCipher.decryptSensitiveDataAsymmetric(
                    "OAEP_TESTING_RSA",
                    cipherText,
                    (byte[] unencryptedText) -> {
                Log.i(TAG, "Unencrypted Text = " + new String(unencryptedText));
            });
        });
    }
}
