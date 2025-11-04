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

package com.android.certifications.niap.niapsec.biometric;

import android.content.Context;


import android.util.Log;

import com.android.certifications.niap.niapsec.crypto.SecureCipher;

import java.util.concurrent.Executor;

import javax.crypto.Cipher;

import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

/**
 * Implementation of Biometric support that handles callbacks and showing the UI elements for
 * key use authorization for the AndroidKeyStore.
 */
public abstract class BiometricSupportImpl extends BiometricPrompt.AuthenticationCallback
        implements BiometricSupport {

    private static final String TAG = "BiometricSupportImpl";

    private FragmentActivity activity;
    private Context context;
    private boolean useDeviceCredential = false;
    private SecureCipher.SecureAuthCallback secureAuthCallback;
    private static final String keyName = "biometric_key";
    private BiometricPrompt biometricPrompt;
    private Executor executor;

    /**
     * Create a Biometric support object with settings from the calling app
     *
     * @param activity            The activity of the calling app
     * @param context             The context of the calling app
     * @param useDeviceCredential true if testing on a device without biometrics and fingerprint
     *                            support. false to test with the biometrics.
     *                            <p>
     *                            NOTE: If useDeviceCredential is true, you must set your config
     *                            to allow for a time-bound key approach in the android keystore.
     *                            userValiditySeconds must be > 0
     */
    public BiometricSupportImpl(FragmentActivity activity,
                                Context context,
                                boolean useDeviceCredential) {
        this.activity = activity;
        this.context = context;
        this.useDeviceCredential = useDeviceCredential;
        this.executor = ContextCompat.getMainExecutor(activity);
        this.biometricPrompt = new BiometricPrompt(activity, executor, this);
    }

    @Override
    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        onAuthenticationSucceeded();
        Log.i(TAG, "SDP Unlock Succeeded, private key available for " +
                "decryption through the AndroidKeyStore.\"");
        try {
            secureAuthCallback.authComplete(BiometricStatus.SUCCESS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        super.onAuthenticationError(errorCode, errString);
        Log.i(TAG, "SDP Unlock Error: " + errorCode + ": " + errString);
        try {
            onMessage(String.valueOf(errString));
            onAuthenticationFailed();
            secureAuthCallback.authComplete(BiometricStatus.FAILED);
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        Log.i(TAG, "SDP Unlock Failed");
        try {
            onAuthenticationFailed();
            secureAuthCallback.authComplete(BiometricStatus.FAILED);
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Show the auth dialog
     *
     * @param title              The title of the prompt
     * @param subtitle           subtitle of the prompt
     * @param description        description of the prompt
     * @param negativeButtonText cancel button text (
     * @param callback           The callback to handle auth complete, failures, etc
     * @param cryptoObject       The crypto object to authenticate (Cipher, Signature, etc)
     */
    public void showAuthDialog(String title, String subtitle,
                               String description,
                               String negativeButtonText,
                               SecureCipher.SecureAuthCallback callback,
                               BiometricPrompt.CryptoObject cryptoObject) {
        this.secureAuthCallback = callback;

        //activity.runOnUiThread(() -> {
        BiometricPrompt.PromptInfo.Builder promptInfoBuilder =
                new BiometricPrompt.PromptInfo.Builder()
                        .setTitle(title)
                        .setSubtitle(subtitle)
                        .setDescription(description);
        if (useDeviceCredential) {
            promptInfoBuilder.setDeviceCredentialAllowed(true);
        } else {
            promptInfoBuilder.setNegativeButtonText(negativeButtonText);
        }
        BiometricPrompt.PromptInfo promptInfo = promptInfoBuilder.build();
        try {
            if (useDeviceCredential) {
                Log.i(TAG, "Calling BiometricPrompt Authenticate for device credential.");
                biometricPrompt.authenticate(promptInfo);
            } else {
                Log.i(TAG, "Calling BiometricPrompt Authenticate for biometrics.");
                biometricPrompt.authenticate(promptInfo, cryptoObject);
            }
        } catch (IllegalArgumentException ex) {
            Log.i(TAG, "Could not authenticate, make sure you have biometrics setup " +
                    "properly, if you set useDeviceCredential to true, you must not have " +
                    "biometrics available on the device");
        }
        //});
    }

    /**
     * Authenticate a cipher using Biometrics to unlock the AndroidKeyStore key
     *
     * @param cipher   The cipher to authenticate
     * @param callback The callback to call when auth is complete with the appropriate event
     */
    public void authenticate(final Cipher cipher, SecureCipher.SecureAuthCallback callback) {
        final BiometricPrompt.CryptoObject cryptoObject = new BiometricPrompt.CryptoObject(cipher);
        showAuthDialog("Please Auth for key usage.",
                "Key used for encrypting files",
                "User authentication required to access key.",
                "Cancel",
                callback,
                cryptoObject);
    }

    /**
     * Authenticate a device using Device Credential to unlock the time-bound AndroidKeyStore keys
     * <p>
     * Use this when testing devices that do not have physical biometric or fingerprint hardware.
     *
     * @param callback The callback to call when auth is complete with the appropriate event
     */
    public void authenticateDeviceCredential(SecureCipher.SecureAuthCallback callback) {
        showAuthDialog("Please Auth for key usage.",
                "Key used for encrypting files",
                "User authentication required to access key.",
                null,
                callback,
                null);
    }

}
