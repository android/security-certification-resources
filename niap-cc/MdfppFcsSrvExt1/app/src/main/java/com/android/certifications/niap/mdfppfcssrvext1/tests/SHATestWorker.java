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
import java.security.MessageDigest;

/**
 * Worker class which defines all required key sizes, and algorithms to create
 * Message Digests using the SHA (Secure Hash Algorithm).
 *
 * SHA key sizes (bits):
 * 1, 256, 384, 512
 *
 * MessageDigest algorithms:
 * SHA-1
 * SHA-256
 * SHA-384
 * SHA-512
 *
 * For more information on available MessageDigest options, please see the
 * <a href="https://developer.android.com/reference/java/security/MessageDigest">MessageDigest</a>
 * documentation.
 */
public class SHATestWorker extends Worker {

    private static final String[] ALGORITHMS = new String[]{
            "SHA-1", "SHA-256", "SHA-384", "SHA-512"
    };

    public SHATestWorker(Context context, WorkerParameters params) {
        super(context, params);
        TestUtil.testStarted(getClass());
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            for (String algorithm : ALGORITHMS) {
                MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
                messageDigest.update(TestUtil.TEST_DATA.getBytes(StandardCharsets.UTF_8));
                TestUtil.logSuccess(
                        getClass(),
                        "Creating MessageDigest using " + algorithm,
                        MessageDigest.class);
                messageDigest.digest();
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
