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

import android.app.KeyguardManager;
import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;

import com.android.certifications.niap.niapsec.SecureConfig;
import com.android.certifications.niap.niapsec.biometric.BiometricSupport;
import com.android.certifications.niap.niapsec.context.SecureContextCompat;
import com.android.certifications.niap.niapsec.crypto.EphemeralSecretKey;
import com.android.certifications.niap.niapsec.crypto.SecureCipher;
import com.android.certifications.niap.niapsec.crypto.SecureKeyGenerator;
import com.android.certifications.niap.niapsec.crypto.SecureKeyStore;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Helper class to encrypt and decrypt data
 */
public class EncryptionManager {

    public static String TAG = "EncryptionManager";

    private Context context;
    private BiometricSupport biometricSupport;

    public EncryptionManager(Context context, BiometricSupport biometricSupport) {
        this.context = context;
        this.biometricSupport = biometricSupport;
    }

    public void createSensitiveDataSymmetricKey(final String keyAlias) {
        Log.i(TAG, "Generating Key...");
        SecureKeyGenerator keyGenerator = SecureKeyGenerator.getInstance(SecureConfig.getDefault());
        boolean created = keyGenerator.generateKey(keyAlias);
        SecureKeyStore secureKeyStore = SecureKeyStore.getDefault(SecureConfig.getDefault());
        final boolean keyInHardware = secureKeyStore.checkKeyInsideSecureHardware(keyAlias);
        if (UpdateViewModel.updateStatus != null) {
            UpdateViewModel.updateStatus.postValue("Generated Key Stored in Hardware: " +
                    keyAlias + " - " + (keyInHardware ? "Yes" : "No"));
        }
    }

    public void createSensitiveDataAsymmetricKeyPair(final String keyPairAlias) {
        Log.i(TAG, "Generating KeyPair (RSA)...");
        SecureKeyGenerator keyGenerator = SecureKeyGenerator.getInstance(SecureConfig.getDefault());
        boolean createdAsym = keyGenerator.generateAsymmetricKeyPair(keyPairAlias);
        Log.i(TAG, "KeyPair Generated: " + createdAsym);
        SecureKeyStore secureKeyStore = SecureKeyStore.getDefault(SecureConfig.getDefault());
        final boolean keyInHardwareAsym =
                secureKeyStore.checkKeyInsideSecureHardwareAsymmetric(keyPairAlias);
        if (UpdateViewModel.updateStatus != null) {
            UpdateViewModel.updateStatus.postValue("Generated KeyPair Stored in Hardware: " +
                    keyPairAlias + " - " + (keyInHardwareAsym ? "Yes" : "No"));
        }
    }

    public EphemeralSecretKey createEphemeralKey() {
        SecureKeyGenerator secureKeyGenerator = SecureKeyGenerator.getInstance(SecureConfig.getDefault());
        EphemeralSecretKey secretKey = secureKeyGenerator.generateEphemeralDataKey();
        Log.i("SDPTestWorker", "Ephemeral AES Key Base64:\n" +
                Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT));
        return secretKey;
    }

    public void encryptEphemeralKeyAsymmetric(
            EphemeralSecretKey secretKey,
            String keyPairAlias,
            SecureCipher.SecureAsymmetricEncryptionCallback callback) {
        SecureCipher secureCipher = SecureCipher.getDefault(SecureConfig.getDefault(biometricSupport));
        secureCipher.encryptSensitiveDataAsymmetric(keyPairAlias, secretKey.getEncoded(), callback);
    }

    // Returns the locked status of the device
    public boolean deviceLocked() {
        KeyguardManager keyGuardManager =
                (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        Log.i(TAG, "DEVICE STATUS: " + (keyGuardManager.isDeviceLocked() ?
                "Locked" : "Unlocked"));
        return keyGuardManager.isDeviceLocked();
    }

    // Encrypt Data, device lock aware
    // If the device is locked...
    // Encrypts with an Ephemeral Symmetric key, then encrypts the key with the public
    // Sensitive Asymmetric public key.
    // If the device is unlocked...
    // Encrypts with the Sensitive Symmetric key if the device is unlocked
    //
    // Returns encrypted data with key and iv packed in.
    public void encryptData(String symKeyAlias,
                            String asymKeyPairAlias,
                            byte[] data,
                            SecureCipher.SecureAsymmetricEncryptionCallback
                                    asymmetricEncryptionCallback) {
        if (deviceLocked()) {
            // Asymmetric Sensitive Data Protection
            Log.i(TAG, "Device Locked: Encrypted Data using Asymmetric Key");
            EphemeralSecretKey secretKey = createEphemeralKey();
            SecureCipher secureCipher = SecureCipher.getDefault(SecureConfig.getDefault(biometricSupport));
            Pair<byte[], byte[]> encryptedData = secureCipher.encryptEphemeralData(secretKey, data,"default_encryption_key");
            encryptEphemeralKeyAsymmetric(secretKey, asymKeyPairAlias,
                    (byte[] encryptedEphemeralKey) -> {
                byte[] encodedData = secureCipher.encodeEphemeralData(
                        asymKeyPairAlias.getBytes(),
                        encryptedEphemeralKey,
                        encryptedData.first,
                        encryptedData.second);
                secretKey.destroy();
                asymmetricEncryptionCallback.encryptionComplete(encodedData);
            });
        } else {
            SecureCipher secureCipher = SecureCipher.getDefault(SecureConfig.getDefault(biometricSupport));
            secureCipher.encryptSensitiveData(
                    symKeyAlias,
                    data,
                    (byte[] cipherText, byte[] iv) -> {
                if (cipherText != null) {
                    byte[] encodedData = secureCipher.encodeSymmetricData(
                            symKeyAlias.getBytes(), cipherText, iv);
                    asymmetricEncryptionCallback.encryptionComplete(encodedData);
                }
            });
        }
    }

    // Encrypts and encodes data while also writing to a file.
    public boolean encryptData(String fileName, String symKeyAlias,
                               String asymKeyPairAlias,
                               byte[] data,
                               SecureCipher.SecureAsymmetricEncryptionCallback callback) {
        final AtomicBoolean saved = new AtomicBoolean(false);
        encryptData(symKeyAlias, asymKeyPairAlias, data, (byte[] encryptedData) -> {
            try {
                SecureContextCompat secureContext = new SecureContextCompat(context,SecureConfig.getDefault());
                Log.i(TAG, "Keyname " + fileName.substring(0, fileName.indexOf(".")));
                FileOutputStream outputStream = secureContext.openEncryptedFileOutput(
                        fileName,
                        Context.MODE_PRIVATE,
                        fileName.substring(0, fileName.indexOf(".")),true);
                outputStream.write(encryptedData);
                UpdateViewModel.updateStatus.postValue(
                        "Saving " + encryptedData.length + " bytes to file " + fileName);
                outputStream.flush();
                outputStream.close();
                saved.set(true);
            } catch (IOException ex) {
                ex.printStackTrace();
                Log.e(TAG, "There was a problem writing to file... " + ex.getMessage());
            }
        });
        return saved.get();
    }

    public void convertEphemeralEncodedData(
            String keyPairAlias,
            byte[] encodedCipherText,
            SecureCipher.SecureAsymmetricEncryptionCallback callback) {
        if (!deviceLocked()) {
            SecureCipher secureCipher = SecureCipher.getDefault(SecureConfig.getDefault(biometricSupport));
            secureCipher.decryptEncodedData(
                    encodedCipherText,
                    (byte[] decryptedData) -> {
                secureCipher.encryptSensitiveDataAsymmetric(
                        keyPairAlias,
                        decryptedData,
                        (byte[] asymmetricCipherText) -> {
                    callback.encryptionComplete(
                            secureCipher.encodeAsymmetricData(
                                    keyPairAlias.getBytes(),
                                    asymmetricCipherText));
                });
            });

        }
    }

    public boolean convertEphemeralEncodedData(String fileName, String keyPairAlias) {
        AtomicBoolean converted = new AtomicBoolean(false);
        try {
            SecureContextCompat secureContext = new SecureContextCompat(context,SecureConfig.getDefault());
            secureContext.openEncryptedFileInput(
                    fileName,
                    Executors.newSingleThreadExecutor(),
                    true,
                    inputStream-> {
                try {

                    byte[] fileData = new byte[inputStream.available()];
                    inputStream.read(fileData);
                    UpdateViewModel.updateStatus.postValue(
                            "Read " + fileData.length + " bytes from " + fileName);
                    convertEphemeralEncodedData(keyPairAlias, fileData, (byte[] convertedData) -> {
                        if (convertedData != null) {
                            try {
                                inputStream.close();
                                FileOutputStream outputStream = secureContext
                                        .openEncryptedFileOutput(
                                                fileName,
                                                Context.MODE_PRIVATE,
                                                fileName.substring(0, fileName.indexOf(".")),true);
                                outputStream.write(convertedData);
                                outputStream.flush();
                                outputStream.close();
                                converted.set(true);
                            } catch (IOException ex) {
                                Log.e(TAG, "Could not open/write file... " + ex.getMessage());
                            }
                        } else {
                            Log.i(TAG, "Nothing to convert.");
                        }
                    });
                } catch (IOException ex) {
                    Log.e(TAG, "Could not open/write file... " + ex.getMessage());
                }
            });
        } catch (IOException ex) {
            Log.e(TAG, "Could not open/write file... " + ex.getMessage());
        }
        return converted.get();
    }

    public void decryptData(String fileName, SecureCipher.SecureDecryptionCallback callback) {
        try {
            SecureContextCompat secureContext = new SecureContextCompat(context,SecureConfig.getDefault());
            secureContext.openEncryptedFileInput(
                    fileName,
                    Executors.newSingleThreadExecutor(),
                    true,
                    inputStream -> {
                try {

                    byte[] encodedData = new byte[inputStream.available()];
                    inputStream.read(encodedData);
                    inputStream.close();
                    SecureCipher secureCipher = SecureCipher.getDefault(SecureConfig.getDefault(biometricSupport));
                    secureCipher.decryptEncodedData(encodedData, callback);
                } catch (IOException ex) {
                    Log.e(TAG, "There was a problem writing to file... " + ex.getMessage());
                }
            });
        } catch (IOException ex) {
            Log.e(TAG, "There was a problem writing to file... " + ex.getMessage());
        }
    }
}



