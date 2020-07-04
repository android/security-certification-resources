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

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.certifications.niap.mdfppfcssrvext1.TestUtil;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;

/**
 * Worker class which defines all required key sizes, and algorithms for use with
 * Conscript to create HMACs (Hashed Message Authentication Codes)
 *
 * Mac key sizes (bits):
 * 160, 256, 384, 512
 *
 * Mac algorithms:
 * HmacSHA1
 * HmacSHA256
 * HmacSHA384
 * HmacSHA512
 *
 * For more information on available Mac options, please see the
 * <a href="https://developer.android.com/reference/javax/crypto/Mac">Mac</a> documentation.
 */
public class HMACTestWorker extends Worker {

    private static final Map<Integer, String> ALGORITHMS = new LinkedHashMap() {{
        put(160, "HmacSHA1");
        put(256, "HmacSHA256");
        put(384, "HmacSHA384");
        put(512, "HmacSHA512");
    }};

    public SecretKey generateHmacKeyConscript(int keySize, String algorithm) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(
                algorithm, "AndroidOpenSSL");
        keyGenerator.init(keySize);
        SecretKey key = keyGenerator.generateKey();
        TestUtil.logKey(getClass(), "HMAC"+keySize+":"+algorithm, key.getEncoded());
        return key;
    }

    public byte[] mac(String algorithm, SecretKey secretKey, String data) throws Exception {
        Mac mac = Mac.getInstance(algorithm);
        mac.init(secretKey);
        return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }

    public HMACTestWorker(Context context, WorkerParameters params) {
        super(context, params);
        TestUtil.testStarted(getClass());
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            for (Map.Entry<Integer, String> entry : ALGORITHMS.entrySet()) {
                int keySize = entry.getKey();
                String algorithm = entry.getValue();
                TestUtil.logSuccess(
                        getClass(),
                        "generated Conscript HMAC"
                                + keySize + " key with algorithm " + algorithm,
                        Mac.class);
                SecretKey key = generateHmacKeyConscript(keySize, algorithm);
                TestUtil.logSuccess(
                        getClass(),
                        "Creating HMAC using " + algorithm,
                        Mac.class);
                byte[] mac = mac(algorithm, key, TestUtil.TEST_DATA);
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
