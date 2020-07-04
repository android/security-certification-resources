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
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Worker class which defines all required key sizes, and algorithms for use with the
 * AndroidKeyStore and Conscript providers for ECDSA (Elliptical Curve Digital Signature Algorithm).
 *
 * Signing key pair sizes (bits):
 * 256, 384, 521
 *
 * Signature algorithms:
 * SHA256withECDSA (secp256r1)
 * SHA384withECDSA (secp384r1)
 * SHA512withECDSA/PSS (secp521r1)
 *
 * For more information on available Signature options, please see the
 * <a href="https://developer.android.com/reference/java/security/Signature">Signature</a>
 * documentation.
 */
public class ECDSATestWorker extends Worker {

    private static final Map<Integer, Pair<String, String>> SIGNING_ALGORITHMS =
            new LinkedHashMap() {{
        put(256, new Pair("SHA256withECDSA", "secp256r1"));
        put(384, new Pair("SHA384withECDSA", "secp384r1"));
        put(521, new Pair("SHA512withECDSA", "secp521r1"));
    }};

    private static final String KEYSTORE_ALIAS = "ecdsa_sign_keystore_key";

    public ECDSATestWorker(Context context, WorkerParameters params) {
        super(context, params);
        TestUtil.testStarted(getClass());
    }

    public Pair<ECPublicKey, ECPrivateKey> generateECDSAKeyPairConscript(String spec)
            throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(
                "EC",
                "AndroidOpenSSL");
        ECGenParameterSpec ecParams = new ECGenParameterSpec(spec);
        keyGen.initialize(ecParams);
        KeyPair keyPair = keyGen.generateKeyPair();
        ECPublicKey pubKey = (ECPublicKey) keyPair.getPublic();
        TestUtil.logKey(
                getClass(),
                "Public ECDSA:"+spec,
                pubKey.getEncoded());
        ECPrivateKey privKey = (ECPrivateKey) keyPair.getPrivate();
        TestUtil.logKey(
                getClass(),
                "Private ECDSA:"+spec,
                privKey.getEncoded());
        return new Pair(pubKey, privKey);
    }

    public Pair<ECPublicKey, PrivateKey> generateECDSAKeyPairAndroidKeystore(String nistSpec)
            throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
                "EC", "AndroidKeyStore");
        keyPairGenerator.initialize(
                new KeyGenParameterSpec.Builder(
                        KEYSTORE_ALIAS + nistSpec,
                        KeyProperties.PURPOSE_SIGN | KeyProperties.PURPOSE_VERIFY)
                        .setAlgorithmParameterSpec(new ECGenParameterSpec(nistSpec))
                        .setDigests(KeyProperties.DIGEST_SHA256,
                                KeyProperties.DIGEST_SHA384,
                                KeyProperties.DIGEST_SHA512).build());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey priv = (PrivateKey) keyPair.getPrivate();
        ECPublicKey pub = (ECPublicKey) keyPair.getPublic();
        return new Pair<>(pub, priv);
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
            // Conscript Signing tests
            for (Map.Entry<Integer, Pair<String, String>> signatureConstants :
                    SIGNING_ALGORITHMS.entrySet()) {
                int keySize = signatureConstants.getKey();
                String signatureAlgorithm = signatureConstants.getValue().first;
                String nistSpec = signatureConstants.getValue().second;

                TestUtil.logSuccess(
                        getClass(),
                        "generated Conscript ECDSA"
                                + keySize + " key pair with spec " + nistSpec,
                        KeyPairGenerator.class);
                Pair<ECPublicKey, ECPrivateKey> keyPair = generateECDSAKeyPairConscript(nistSpec);
                TestUtil.logSuccess(
                        getClass(),
                        "signing with " + signatureAlgorithm + " " + nistSpec,
                        Signature.class);
                byte[] signature = signData(TestUtil.TEST_DATA, signatureAlgorithm, keyPair.second);
                TestUtil.logSuccess(
                        getClass(),
                        "verifying with " + signatureAlgorithm + " " + nistSpec,
                        Signature.class);
                boolean verified = verifyData(
                        TestUtil.TEST_DATA,
                        signature,
                        signatureAlgorithm,
                        keyPair.first);
                if (verified) {
                    TestUtil.logSuccess(getClass(), "Data verified.");
                } else {
                    TestUtil.logFailure(
                            getClass(),
                            "Sign Verification Failed, please check the logs.");
                    return Result.failure();
                }
            }

            // AndroidKeyStore Signing tests
            for (Map.Entry<Integer, Pair<String, String>> signatureConstants :
                    SIGNING_ALGORITHMS.entrySet()) {
                int keySize = signatureConstants.getKey();
                String signatureAlgorithm = signatureConstants.getValue().first;
                String nistSpec = signatureConstants.getValue().second;

                TestUtil.logSuccess(
                        getClass(),
                        "generated AndroidKeyStore ECDSA"
                                + keySize + " key pair with spec " + nistSpec,
                        KeyPairGenerator.class);
                Pair<ECPublicKey, PrivateKey> keyPair =
                        generateECDSAKeyPairAndroidKeystore(nistSpec);
                TestUtil.logSuccess(
                        getClass(),
                        "signing with " + signatureAlgorithm + " " + nistSpec,
                        Signature.class);
                byte[] signature = signData(TestUtil.TEST_DATA, signatureAlgorithm, keyPair.second);
                TestUtil.logSuccess(
                        getClass(),
                        "verifying with " + signatureAlgorithm + " " + nistSpec,
                        Signature.class);
                boolean verified = verifyData(
                        TestUtil.TEST_DATA,
                        signature,
                        signatureAlgorithm,
                        keyPair.first);
                if (verified) {
                    TestUtil.logSuccess(getClass(), "Data verified.");
                } else {
                    TestUtil.logFailure(
                            getClass(),
                            "Sign Verification Failed, please check the logs.");
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
