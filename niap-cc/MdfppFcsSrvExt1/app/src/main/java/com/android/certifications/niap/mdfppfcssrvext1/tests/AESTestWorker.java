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

package com.android.certifications.niap.mdfppfcssrvext1.tests;

import android.content.Context;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.certifications.niap.mdfppfcssrvext1.TestUtil;

import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Worker class which defines all required key sizes, and algorithms for use with the
 * AndroidKeyStore and Conscript providers for the AES (Advanced Encryption Standard) cryptographic
 * algorithm.
 *
 * Encryption key sizes (bits):
 * 128
 * 256
 *
 * Cipher transformations:
 * AES/CBC/NoPadding
 * AES/GCM/NoPadding
 *
 * For more information on available Cipher transformations, please see the
 * <a href="https://developer.android.com/reference/javax/crypto/Cipher">Cipher</a> documentation.
 */
public class AESTestWorker extends Worker {

    private static final int[] KEY_SIZES = new int[]{128, 256};
    private static final Map<String, Function<byte[], AlgorithmParameterSpec>> TRANSFORMATIONS =
            new LinkedHashMap() {{
                Function<byte[], AlgorithmParameterSpec> gcm =
                        (iv) -> new GCMParameterSpec(128, iv);
                Function<byte[], AlgorithmParameterSpec> cbc = (iv) -> new IvParameterSpec(iv);
                put("AES/CBC/NoPadding", cbc);
                put("AES/GCM/NoPadding", gcm);
            }};

    private static final Map<String, KeyGenParameterSpec> KEY_GEN_PARAMETER_SPECS =
            new LinkedHashMap() {{
                try {
                    KeyGenerator keyGenerator = KeyGenerator.getInstance(
                            "AES",
                            "AndroidKeyStore");
                    put("AES128/GCM/NoPadding", new KeyGenParameterSpec.Builder(
                            KEYSTORE_ALIAS + "128GCM",
                            KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                            .setKeySize(128).build());
                    put("AES256/GCM/NoPadding", new KeyGenParameterSpec.Builder(
                            KEYSTORE_ALIAS + "256GCM",
                            KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_GCM).
                            setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE).
                            setKeySize(256).build());
                    put("AES128/CBC/NoPadding", new KeyGenParameterSpec.Builder(
                            KEYSTORE_ALIAS + "128CBC",
                            KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                            .setKeySize(128).build());
                    put("AES256/CBC/NoPadding", new KeyGenParameterSpec.Builder(
                            KEYSTORE_ALIAS + "256CBC",
                            KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                            .setKeySize(256).build());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }};

    private static final String KEYSTORE_ALIAS = "aes_keystore_key";

    public AESTestWorker(Context context, WorkerParameters params) {
        super(context, params);
        TestUtil.testStarted(getClass());
    }

    public SecretKey generateAESKeyConscript(int keySize) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(
                "AES",
                "AndroidOpenSSL");
        keyGenerator.init(keySize);
        SecretKey key = keyGenerator.generateKey();
        TestUtil.logKey(getClass(), "AES"+keySize, key.getEncoded());
        return key;
    }

    public SecretKey generateAESKeyAndroidKeyStore(KeyGenParameterSpec keyGenParameterSpec)
            throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(
                "AES",
                "AndroidKeyStore");
        keyGenerator.init(keyGenParameterSpec);
        return keyGenerator.generateKey();
    }

    public Pair<byte[], byte[]> encrypt(String data, SecretKey secretKey, String transformation)
            throws Exception {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] iv = cipher.getIV();
        byte[] clearData = data.getBytes(UTF_8);
        byte[] cipherText = cipher.doFinal(clearData);
        return new Pair<>(cipherText, iv);
    }

    public String decrypt(byte[] cipherText, SecretKey secretKey,
                          String transformation, AlgorithmParameterSpec spec)
            throws Exception {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
        return new String(cipher.doFinal(cipherText), UTF_8);
    }


    @NonNull
    @Override
    public Result doWork() {
        try {
            String clearText = TestUtil.TEST_DATA;

            // AndroidKeyStore Tests
            for (Map.Entry<String, KeyGenParameterSpec> entry :
                    KEY_GEN_PARAMETER_SPECS.entrySet()) {
                SecretKey secretKey = generateAESKeyAndroidKeyStore(entry.getValue());
                TestUtil.logSuccess(getClass(), "generated AndroidKeyStore AES" +
                        entry.getValue().getKeySize() + " key", KeyGenerator.class);
                String transformation;
                if(Arrays.asList(entry.getValue().getBlockModes()).contains("GCM")) {
                    transformation = "AES/GCM/NoPadding";
                } else {
                    transformation = "AES/CBC/NoPadding";
                }
                Pair<byte[], byte[]> encrypted = encrypt(clearText, secretKey, transformation);
                TestUtil.logSuccess(
                        getClass(),
                        "encrypting with " + transformation,
                        Cipher.class);
                AlgorithmParameterSpec spec = TRANSFORMATIONS
                        .get(transformation)
                        .apply(encrypted.second);
                String decrypted = decrypt(encrypted.first, secretKey, transformation, spec);
                TestUtil.logSuccess(
                        getClass(),
                        "decrypting with " + transformation,
                        Cipher.class);
                if (decrypted.equals(clearText)) {
                    TestUtil.logSuccess(getClass(), "Data matches...");
                } else {
                    TestUtil.logFailure(
                            getClass(),
                            "Data does not match found " + decrypted +
                                    " should have found " + clearText);
                    return Result.failure();
                }

            }

            // Conscript Tests - Verify these tests run on Android R Beta
            for (int keySize : KEY_SIZES) {
                SecretKey secretKey = generateAESKeyConscript(keySize);
                TestUtil.logSuccess(getClass(), "generated Conscript AES" +
                        keySize + " key", KeyGenerator.class);

                for (Map.Entry<String, Function<byte[], AlgorithmParameterSpec>> entry :
                        TRANSFORMATIONS.entrySet()) {
                    Pair<byte[], byte[]> encrypted = encrypt(clearText, secretKey, entry.getKey());
                    TestUtil.logSuccess(
                            getClass(),
                            "encrypting with " + entry.getKey(),
                            Cipher.class);
                    AlgorithmParameterSpec spec = entry.getValue().apply(encrypted.second);
                    String decrypted = decrypt(encrypted.first, secretKey, entry.getKey(), spec);
                    TestUtil.logSuccess(
                            getClass(),
                            "decrypting with " + entry.getKey(),
                            Cipher.class);
                    if (decrypted.equals(clearText)) {
                        TestUtil.logSuccess(getClass(), "Data matches...");
                    } else {
                        TestUtil.logFailure(
                                getClass(),
                                "Data does not match found " + decrypted +
                                        " should have found " + clearText);
                        return Result.failure();
                    }
                }
            }

            return Result.success();
        } catch (Exception ex) {
            ex.printStackTrace();
            TestUtil.logFailure(getClass(), ex.getMessage());
        }
        TestUtil.logFailure(getClass(), "Unknown Error, please check the logs.");
        return Result.failure();
    }
}
