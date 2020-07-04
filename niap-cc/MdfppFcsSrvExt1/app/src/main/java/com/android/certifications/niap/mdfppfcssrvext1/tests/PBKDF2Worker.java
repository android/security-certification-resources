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

import com.android.certifications.niap.mdfppfcssrvext1.TestUtil;

import java.security.Provider;
import java.security.Security;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


/**
 * Worker class which creates and demonstrates password based encryption (PBE) algorithms.
 *
 * Key length (bits):
 * 256
 *
 * PBE algorithms:
 * PBKDF2withHmacSHA1
 *
 * For certification and testing purposes only.
 *
 * For more information on available algorithm options, please see the
 * <a href=https://developer.android.com/reference/javax/crypto/SecretKeyFactory">
 * SecretKeyFactory</a> documentation.
 */
public class PBKDF2Worker extends Worker {

    private static final String PASSWORD = "S3cur1ty!";
    private static final String SALT = "nacl!";

    public PBKDF2Worker(Context context, WorkerParameters params) {
        super(context, params);
        TestUtil.testStarted(getClass());
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2withHmacSHA1");
            PBEKeySpec keySpec = new PBEKeySpec(
                    PASSWORD.toCharArray(),
                    SALT.getBytes(),
                    65500,
                    256);
            SecretKey key = keyFactory.generateSecret(keySpec);

            TestUtil.logKey(
                    getClass(),
                    "PBKDF2 - Provider: " + keyFactory.getProvider().toString(),
                    key.getEncoded());

            return Result.success();
        } catch (Exception ex) {
            ex.printStackTrace();
            TestUtil.logFailure(getClass(), ex.getMessage());
        }
        TestUtil.logFailure(getClass(), "Unknown Error, please check the logs.");
        return Result.failure();
    }

}
