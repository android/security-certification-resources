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
package com.android.certifications.niap.niapsec.crypto;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import com.android.certifications.niap.niapsec.SecureConfig;

import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;

/**
 * Class used for generating ephemeral keys
 */
public class SecureKeyGenerator {

    private static final String TAG = "SecureKeyGenerator";

    private SecureConfig secureConfig;

    /**
     * Create an instance of the key generator with custom settings
     *
     * @param secureConfig The config to use when building the key generator
     * @return The key generator
     */
    public static SecureKeyGenerator getInstance(SecureConfig secureConfig) {
        return new SecureKeyGenerator(secureConfig);
    }

    private SecureKeyGenerator(SecureConfig secureConfig) {
        this.secureConfig = secureConfig;
    }

    /**
     * <p>
     * Generates a sensitive data key and adds the SecretKey to the AndroidKeyStore.
     * Utilizes UnlockedDeviceProtection to ensure that the device must be unlocked in order to
     * use the generated key.
     * </p>
     *
     * @param keyAlias The name of the generated SecretKey to save into the AndroidKeyStore.
     * @return true if the key was generated, false otherwise
     */
    public boolean generateKey(String keyAlias) {
        boolean created = false;
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(
                    secureConfig.getSymmetricKeyAlgorithm(),
                    secureConfig.getAndroidKeyStore());
            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(
                    keyAlias, secureConfig.getSymmetricKeyPurposes()).
                    setBlockModes(secureConfig.getSymmetricBlockModes()).
                    setEncryptionPaddings(secureConfig.getSymmetricPaddings()).
                    setKeySize(secureConfig.getSymmetricKeySize());
            builder = builder.setUserAuthenticationRequired(
                    secureConfig.isSymmetricRequireUserAuthEnabled());
            builder = builder.setUserAuthenticationValidityDurationSeconds(
                    secureConfig.getSymmetricRequireUserValiditySeconds());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                builder = builder.setUnlockedDeviceRequired(
                        secureConfig.isSymmetricSensitiveDataProtectionEnabled());
            }
            keyGenerator.init(builder.build());
            keyGenerator.generateKey();
            created = true;
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                 NoSuchProviderException ex) {
            throw new SecurityException(ex);
        }
        return created;
    }

    /**
     * <p>
     * Generates a sensitive data public/private key pair and adds the KeyPair to the AndroidKeyStore.
     * Utilizes UnlockedDeviceProtection to ensure that the device must be unlocked in order to
     * use the generated key.
     * </p>
     * <p>
     * ANDROID P ONLY (API LEVEL 28>)
     * </p>
     *
     * @param keyPairAlias The name of the generated SecretKey to save into the AndroidKeyStore.
     * @return true if the key was generated, false otherwise
     */
    public boolean generateAsymmetricKeyPair(String keyPairAlias) {
        boolean created = false;
        try {
            KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(
                    secureConfig.getAsymmetricKeyPairAlgorithm(),
                    secureConfig.getAndroidKeyStore());
            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(
                    keyPairAlias, secureConfig.getAsymmetricKeyPurposes())
                    .setEncryptionPaddings(secureConfig.getAsymmetricPaddings())
                    .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                    .setBlockModes(secureConfig.getAsymmetricBlockModes())
                    .setKeySize(secureConfig.getAsymmetricKeySize());
            builder = builder.setUserAuthenticationRequired(
                    secureConfig.isAsymmetricRequireUserAuthEnabled());
            builder = builder.setUserAuthenticationValidityDurationSeconds(
                    secureConfig.getAsymmetricRequireUserValiditySeconds());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                builder = builder.setUnlockedDeviceRequired(
                        secureConfig.isAsymmetricSensitiveDataProtectionEnabled());
            }
            keyGenerator.initialize(builder.build());
            keyGenerator.generateKeyPair();
            created = true;
        } catch (NoSuchProviderException |
                InvalidAlgorithmParameterException |
                NoSuchAlgorithmException ex) {
            throw new SecurityException(ex);
        }
        return created;
    }

    /**
     * <p>
     * Generates an Ephemeral symmetric key that can be fully destroyed and removed from memory.
     * </p>
     *
     * @return The EphemeralSecretKey generated
     */
    public EphemeralSecretKey generateEphemeralDataKey() {
        try {
            SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            byte[] key = new byte[secureConfig.getSymmetricKeySize() / 8];
            secureRandom.nextBytes(key);
            return new EphemeralSecretKey(key,
                    secureConfig.getSymmetricKeyAlgorithm(),
                    secureConfig);
        } catch (GeneralSecurityException ex) {
            throw new SecurityException(ex);
        }
    }

}
