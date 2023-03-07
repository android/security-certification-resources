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

import android.util.Log;

/**
 * Aids in printing test case output to the console in a uniform way.
 *
 * This output can be used in your submission for certification.
 */
public class TestUtil {

    public static final String DATA = "SUPER Secret Test Data!";

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
}

