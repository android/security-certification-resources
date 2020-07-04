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

package com.android.certifications.niap.mdfppfcssrvext1;

import android.util.Base64;
import android.util.Log;

import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import androidx.work.OneTimeWorkRequest;

/**
 * Aids in printing test case output to the console in a uniform way.
 *
 * This output can be used in your submission for certification.
 */
public class TestUtil {

    public static boolean printRawKeys = false;
    public static final String TEST_DATA = "SUPER Secret Test Data!";

    public static void testStarted(Class clazz) {
        Log.i(clazz.getSimpleName(), "Starting " + clazz.getSimpleName() + "...");
    }

    public static void logSuccess(Class clazz, String message, Class opClazz) {
        Log.i(
                clazz.getSimpleName(),
                "Calling: " + opClazz.getName() + " " + message + " ...Success");
    }

    public static void logSuccess(Class clazz, String message) {
        Log.i(clazz.getSimpleName(), message + " ...Success");
    }

    public static void logFailure(Class clazz, String message) {
        Log.i(clazz.getSimpleName(), "Failed: " + message);
    }

    public static void logKey(Class clazz, String keyInfo, byte[] key) {
        if(printRawKeys) {
            Log.i(
                    clazz.getSimpleName(),
                    "Raw " + keyInfo + " key - Base64:\n" +
                            Base64.encodeToString(key, Base64.DEFAULT));
        }
    }

    public static List<OneTimeWorkRequest> createWorkRequests(List<Class> testWorkerClasses) {
        List<OneTimeWorkRequest> workers = new ArrayList<>();
        for(Class clazz: testWorkerClasses) {
            workers.add(new OneTimeWorkRequest.Builder(clazz).build());
        }
        return workers;
    }

    public static void findAvailableSecurityProviders() {
        for(Provider provider : Security.getProviders()) {
            TestUtil.logSuccess(
                    TestUtil.class,
                    "Provider - " + provider.getName());
            for(Provider.Service s : provider.getServices()) {
                TestUtil.logSuccess(
                        TestUtil.class,
                        "Service - " + s.toString());
            }
        }
    }

}
