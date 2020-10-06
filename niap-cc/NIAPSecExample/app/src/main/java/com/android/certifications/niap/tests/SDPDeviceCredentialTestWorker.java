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

package com.android.certifications.niap.tests;

import android.content.Context;
import android.util.Log;

import com.android.certifications.niap.MainActivity;
import com.android.certifications.niap.TestUtil;
import com.android.certifications.niap.niapsec.SecureConfig;
import com.android.certifications.niap.niapsec.biometric.BiometricSupport;
import com.android.certifications.niap.niapsec.biometric.BiometricSupportImpl;
import com.android.certifications.niap.niapsec.context.SecureContextCompat;
import com.android.certifications.niap.niapsec.crypto.SecureKeyGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyPairGenerator;

import javax.crypto.Cipher;

import androidx.core.content.ContextCompat;

/**
 * Worker class that runs a test to encrypt and decrypt data with the NIAPSEC library.
 * <p>
 * This test should be successful after running the application. Please note that, this test
 * does require that the user authorize using the devices default unlock implementation which can
 * be a device pin or password, fingerprint, or face identification.
 */
public class SDPDeviceCredentialTestWorker implements TestWorker {

    private static final String FILE_NAME = "test_file";
    private static final String KEY_PAIR_ALIAS = "key_pair_alias";

    private static final String TAG = "SDPDeviceCredentialTestWorker";
    private Context context;

    public SDPDeviceCredentialTestWorker(Context context) {
        this.context = context;
        try {
            Log.i(TAG, "Deleting previous run file.");
            new File(context.getFilesDir(), FILE_NAME).delete();
        } catch (Exception ex) {
        }
    }

    public boolean doWork() {
        try {
            // Write SDP File
            BiometricSupport biometricSupport = new BiometricSupportImpl(
                    MainActivity.thisActivity,
                    context, true) {
                @Override
                public void onAuthenticationSucceeded() {
                    TestUtil.logSuccess(getClass(), "SDP Device Credential Unlock " +
                            "Succeeded, private key available for decryption through the " +
                            "AndroidKeyStore.");
                }

                @Override
                public void onAuthenticationFailed() {
                    TestUtil.logFailure(getClass(),
                            "SDP Device Credential Unlock failed, " +
                            "file not available for decryption.");
                }

                @Override
                public void onMessage(String message) {
                    TestUtil.logSuccess(getClass(), message);
                }
            };

            SecureConfig secureConfig =
                    SecureConfig.getStrongDeviceCredentialConfig(biometricSupport);
            secureConfig.setDebugLoggingEnabled(true);
            SecureKeyGenerator keyGenerator = SecureKeyGenerator.getInstance(secureConfig);
            TestUtil.logSuccess(getClass(), "Generated RSA with provider AndroidKeyStore.",
                    KeyPairGenerator.class);
            keyGenerator.generateAsymmetricKeyPair(KEY_PAIR_ALIAS);

            SecureContextCompat secureContext = new SecureContextCompat(
                    context,
                    secureConfig);

            TestUtil.logSuccess(
                    getClass(),
                    "Opening encrypted stream to SDP " + FILE_NAME,
                    FileOutputStream.class);
            TestUtil.logSuccess(
                    getClass(),
                    "Writing Data: " + TestUtil.DATA,
                    FileOutputStream.class);
            FileOutputStream outputStream = secureContext.openEncryptedFileOutput(
                    FILE_NAME,
                    Context.MODE_PRIVATE, KEY_PAIR_ALIAS, true);
            TestUtil.logSuccess(
                    getClass(),
                    "Writing SDP file encrypted contents to " + FILE_NAME,
                    Cipher.class);
            outputStream.write(TestUtil.DATA.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            outputStream.close();

            FileInputStream rawInputStream = context.openFileInput(FILE_NAME);
            byte[] fileContents = new byte[rawInputStream.available()];
            rawInputStream.read(fileContents);
            rawInputStream.close();
            TestUtil.logSuccess(getClass(), "SDP File Contents: " +
                    new String(fileContents, StandardCharsets.UTF_8), FileInputStream.class);


            // Read file
            secureContext.openEncryptedFileInput(
                    FILE_NAME,
                    ContextCompat.getMainExecutor(context),
                    true,
                    inputStream -> {
                        try {
                            byte[] encodedData = new byte[inputStream.available()];
                            TestUtil.logSuccess(
                                    getClass(),
                                    "decrypting " + FILE_NAME + " data ",
                                    Cipher.class);
                            inputStream.read(encodedData);
                            inputStream.close();
                            String plainText = new String(encodedData, StandardCharsets.UTF_8);
                            TestUtil.logSuccess(getClass(), "File Contents= " + plainText);
                            if (TestUtil.DATA.equals(plainText)) {
                                TestUtil.logSuccess(getClass(), "Data matches...");
                            } else {
                                TestUtil.logFailure(getClass(), "Decryption failed");
                            }


                        } catch (Exception ex) {
                            TestUtil.logFailure(
                                    getClass(),
                                    "Failed on file decryption " + ex.getMessage());
                        }
                    });

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            TestUtil.logFailure(getClass(), ex.getMessage());
        }
        TestUtil.logFailure(getClass(), "Unknown Error, please check the logs.");
        return false;
    }


}
