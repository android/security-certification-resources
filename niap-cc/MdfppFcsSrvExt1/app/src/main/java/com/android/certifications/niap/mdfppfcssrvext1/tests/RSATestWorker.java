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

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.MGF1ParameterSpec;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

/**
 * Worker class which defines all required key sizes, and algorithms for use with the
 * AndroidKeyStore and Conscript providers for the RSA cryptographic algorithm.
 *
 * RSA supports both encryption and signing.
 *
 * Signing key pair sizes (bits):
 * 2048, 3072
 *
 * Signature algorithms:
 * SHA256withRSA (PKCS1)
 * SHA384withRSA (PKCS1)
 * SHA256withRSA/PSS (PSS)
 * SHA384withRSA/PSS (PSS)
 *
 * Encryption key pair sizes (bits):
 * 2048, 3072, 4096
 *
 * Cipher transformations:
 * RSA/ECB/OAEPWithSHA-256AndMGF1Padding
 *
 * For more information on available Cipher transformations, please see the
 * <a href="https://developer.android.com/reference/javax/crypto/Cipher">Cipher</a> documentation.
 *
 * For more information on available Signature options, please see the
 * <a href="https://developer.android.com/reference/java/security/Signature">Signature</a>
 * documentation.
 */
public class RSATestWorker extends Worker {

    private static final int[] KEY_SIZES = new int[]{2048, 3072};
    private static final Map<String, String[]> SIGNING_ALGORITHMS = new LinkedHashMap() {{
        put("PKCS1", new String[]{"SHA256withRSA", "SHA384withRSA"});
        put("PSS", new String[]{"SHA256withRSA/PSS", "SHA384withRSA/PSS"});
    }};
    private static final int[] ENC_KEY_SIZES = new int[]{2048, 3072, 4096};

    private static final String OAEP_PADDING = KeyProperties.ENCRYPTION_PADDING_RSA_OAEP;

    private static final String KEYSTORE_ALIAS = "rsa_sign_keystore_key";

    public RSATestWorker(Context context, WorkerParameters params) {
        super(context, params);
        TestUtil.testStarted(getClass());
    }

    public Pair<RSAPublicKey, RSAPrivateCrtKey> generateRSAKeyPairConscript(int keySize)
            throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(
                "RSA",
                "AndroidOpenSSL");
        keyGen.initialize(keySize);
        KeyPair keyPair = keyGen.generateKeyPair();
        RSAPublicKey pub = (RSAPublicKey) keyPair.getPublic();
        TestUtil.logKey(getClass(), "Public RSA"+keySize, pub.getEncoded());
        RSAPrivateCrtKey priv = (RSAPrivateCrtKey) keyPair.getPrivate();
        TestUtil.logKey(getClass(), "Private RSA"+keySize, priv.getEncoded());
        return new Pair<>(pub, priv);
    }

    public Pair<RSAPublicKey, PrivateKey> generateRSAKeyPairAndroidKeyStore(
            int keySize,
            String padding)
            throws Exception {
        KeyPairGenerator keyGenerator;
        keyGenerator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
        KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(
                KEYSTORE_ALIAS + keySize,
                KeyProperties.PURPOSE_SIGN | KeyProperties.PURPOSE_VERIFY)
                .setSignaturePaddings(padding)
                .setDigests(
                        KeyProperties.DIGEST_SHA256,
                        KeyProperties.DIGEST_SHA384,
                        KeyProperties.DIGEST_SHA512)
                .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                .setKeySize(keySize);
        keyGenerator.initialize(builder.build());
        KeyPair keyPair = keyGenerator.generateKeyPair();
        PrivateKey priv = (PrivateKey) keyPair.getPrivate();
        RSAPublicKey pub = (RSAPublicKey) keyPair.getPublic();
        return new Pair<>(pub, priv);
    }

    public Pair<RSAPublicKey, PrivateKey> generateRSAKeyPairEncryptAndroidKeyStore(
            int keySize,
            String padding)
            throws Exception {
        KeyPairGenerator keyGenerator;
        keyGenerator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
        KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(
                KEYSTORE_ALIAS + "Encrypt" + keySize,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setEncryptionPaddings(padding)
                .setDigests(
                        KeyProperties.DIGEST_SHA256,
                        KeyProperties.DIGEST_SHA384,
                        KeyProperties.DIGEST_SHA512)
                .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                .setKeySize(keySize);
        keyGenerator.initialize(builder.build());
        KeyPair keyPair = keyGenerator.generateKeyPair();
        PrivateKey priv = (PrivateKey) keyPair.getPrivate();
        RSAPublicKey pub = (RSAPublicKey) keyPair.getPublic();
        return new Pair<>(pub, priv);
    }

    public byte[] encryptData(String data, PublicKey publicKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey, new OAEPParameterSpec(
                "SHA-256",
                "MGF1",
                new MGF1ParameterSpec("SHA-1"),
                PSource.PSpecified.DEFAULT));
        return cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }

    public byte[] decryptData(byte[] cipherText, PrivateKey privateKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey, new OAEPParameterSpec(
                "SHA-256",
                "MGF1",
                new MGF1ParameterSpec("SHA-1"),
                PSource.PSpecified.DEFAULT));
        return cipher.doFinal(cipherText);
    }

    public byte[] signData(String data, String algorithm, PrivateKey privateKey)
            throws Exception {
        Signature signature = Signature.getInstance(algorithm);
        signature.initSign(privateKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        return signature.sign();
    }

    public boolean verifyData(String data, byte[] sig, String algorithm, PublicKey publicKey)
            throws Exception {
        Signature signature = Signature.getInstance(algorithm);
        signature.initVerify(publicKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        return signature.verify(sig);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            String data = TestUtil.TEST_DATA;

            // Conscript Signing tests
            for (int keySize : KEY_SIZES) {
                for (Map.Entry<String, String[]> algorithmPair : SIGNING_ALGORITHMS.entrySet()) {
                    for (String algo : algorithmPair.getValue()) {
                        String padding = algorithmPair.getKey();
                        TestUtil.logSuccess(
                                getClass(),
                                "generated Conscript Signature RSA"
                                        + keySize + " key pair with padding " + padding,
                                KeyPairGenerator.class);
                        Pair<RSAPublicKey, RSAPrivateCrtKey> keyPair =
                                generateRSAKeyPairConscript(keySize);
                        TestUtil.logSuccess(
                                getClass(),
                                "signing with " + algo + " " + padding, Signature.class);
                        byte[] signature = signData(TestUtil.TEST_DATA, algo, keyPair.second);
                        TestUtil.logSuccess(
                                getClass(),
                                "verifying with " + algo + " " + padding,
                                Signature.class);
                        boolean verified = verifyData(data, signature, algo, keyPair.first);
                        if (verified) {
                            TestUtil.logSuccess(getClass(), "Data verified.");
                        } else {
                            TestUtil.logFailure(
                                    getClass(),
                                    "Sign Verification Failed, please check the logs.");
                            return Result.failure();
                        }
                    }
                }
            }


            // AndroidKeyStore signing tests
            for (int keySize : KEY_SIZES) {
                for (Map.Entry<String, String[]> algorithmPair : SIGNING_ALGORITHMS.entrySet()) {
                    for (String algo : algorithmPair.getValue()) {
                        String padding = algorithmPair.getKey();
                        TestUtil.logSuccess(
                                getClass(),
                                "generated AndroidKeyStore Signature RSA"
                                        + keySize + " key pair with padding " + padding,
                                KeyPairGenerator.class);
                        Pair<RSAPublicKey, PrivateKey> keyPair =
                                generateRSAKeyPairAndroidKeyStore(keySize, padding);
                        TestUtil.logSuccess(
                                getClass(),
                                "signing with " + algo + " " + padding,
                                Signature.class);
                        byte[] signature = signData(TestUtil.TEST_DATA, algo, keyPair.second);
                        TestUtil.logSuccess(
                                getClass(),
                                "verifying with " + algo + " " + padding,
                                Signature.class);
                        boolean verified = verifyData(data, signature, algo, keyPair.first);
                        if (verified) {
                            TestUtil.logSuccess(getClass(), "Data verified.");
                        } else {
                            TestUtil.logFailure(
                                    getClass(),
                                    "Sign Verification Failed, please check the logs.");
                            return Result.failure();
                        }
                    }
                }
            }

            // Conscript encryption tests
            for (int keySize : ENC_KEY_SIZES) {

                TestUtil.logSuccess(
                        getClass(),
                        "generated Conscript Encryption RSA"
                                + keySize + " key pair with padding " + OAEP_PADDING,
                        KeyPairGenerator.class);
                Pair<RSAPublicKey, RSAPrivateCrtKey> keyPair = generateRSAKeyPairConscript(keySize);
                TestUtil.logSuccess(
                        getClass(),
                        "encrypting with RSA " + keySize  + " " + OAEP_PADDING,
                        Cipher.class);
                byte[] cipherText = encryptData(TestUtil.TEST_DATA, keyPair.first);
                TestUtil.logSuccess(
                        getClass(),
                        "decrypting with RSA " + keySize  + " " + OAEP_PADDING,
                        Cipher.class);
                byte[] clearText = decryptData(cipherText, keyPair.second);
                String decrypted = new String(clearText, StandardCharsets.UTF_8);
                if (TestUtil.TEST_DATA.equals(decrypted)) {
                    TestUtil.logSuccess(getClass(), "Data matches.");
                } else {
                    TestUtil.logFailure(
                            getClass(),
                            "Encrypt Failed, please check the logs.");
                    return Result.failure();
                }
            }

            // AndroidKeyStore encryption tests
            for (int keySize : ENC_KEY_SIZES) {

                TestUtil.logSuccess(
                        getClass(),
                        "generated AndroidKeyStore Encryption RSA"
                                + keySize + " key pair with padding " + OAEP_PADDING,
                        KeyPairGenerator.class);
                Pair<RSAPublicKey, PrivateKey> keyPair =
                        generateRSAKeyPairEncryptAndroidKeyStore(keySize, OAEP_PADDING);
                TestUtil.logSuccess(
                        getClass(),
                        "encrypting with RSA " + keySize  + " " + OAEP_PADDING,
                        Cipher.class);
                byte[] cipherText = encryptData(TestUtil.TEST_DATA, keyPair.first);
                TestUtil.logSuccess(
                        getClass(),
                        "decrypting with RSA " + keySize  + " " + OAEP_PADDING,
                        Cipher.class);
                byte[] clearText = decryptData(cipherText, keyPair.second);
                String decrypted = new String(clearText, StandardCharsets.UTF_8);
                if (TestUtil.TEST_DATA.equals(decrypted)) {
                    TestUtil.logSuccess(getClass(), "Data matches.");
                } else {
                    TestUtil.logFailure(
                            getClass(),
                            "Encrypt Failed, please check the logs.");
                    return Result.failure();
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


