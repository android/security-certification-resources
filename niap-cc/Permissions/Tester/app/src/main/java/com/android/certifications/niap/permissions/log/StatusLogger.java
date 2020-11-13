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

package com.android.certifications.niap.permissions.log;

import com.android.certifications.niap.permissions.BasePermissionTester;

/**
 * Logger to log the status of permission tests and testers such as the success / failure of
 * individual permission tests, overall status messages upon the completion of a {@link
 * BasePermissionTester}, or unexpected test errors.
 */
public class StatusLogger {
    private static final String TAG = "PermissionTesterStatus";
    private static final Logger sLogger = LoggerFactory.createDefaultLogger(TAG);

    private static final String PASSED = "PASSED";
    private static final String FAILED = "FAILED";
    private static final String ERROR = "TEST ERROR";

    /**
     * Logs the status of an individual {@code permission} using the provided {@code
     * permissionGranted} and {@code apiSuccessful} boolean flags indicating whether the permission
     * was granted to the app and whether the API for the test was successful, respectively.
     *
     * <p>The test is considered successful if the {@code permissionGranted} flag equals the {@code
     * apiSuccessful} flag; an API guarded by a permission should only be successful when that
     * permission has been granted.
     */
    public static void logTestStatus(String permission, boolean permissionGranted,
            boolean apiSuccessful) {
        String testStatus = permissionGranted == apiSuccessful ? PASSED : "FAILED";
        sLogger.logInfo(permission + ": " + testStatus + " (granted = " + permissionGranted
                + ", api successful = " + apiSuccessful + ")");
    }

    /**
     * Logs the provided {@code permission} as being skipped with the specified {@code message}
     * noting whether the permission was granted based on the {@code permissionGranted} flag.
     *
     * <p>A test should be skipped in cases where a required service, etc., for the test is not
     * available on a device.
     */
    public static void logTestSkipped(String permission, boolean permissionGranted,
            String message) {
        sLogger.logInfo(
                permission + ": SKIPPED - " + message + " (granted = " + permissionGranted + ")");
    }

    /**
     * Logs an unexpected {@code throwable} encountered while testing the provided {@code
     * permission}.
     */
    public static void logTestError(String permission, Throwable throwable) {
        sLogger.logError(permission + ": TEST FAILURE", throwable);
    }

    /**
     * Logs the status of the provided signature {@code permission} based on the specified {@code
     * permissionGranted}, {@code signatureMatch}, and {@code platformSignatureMatch} indciating
     * whether the permission has been granted, the app is signed with the same signing key as the
     * app declaring the permission, and the app is signed with the same signing key as the
     * platform, respectively.
     */
    public static void logSignaturePermissionStatus(String permission,
            boolean permissionGranted, boolean signatureMatch, boolean platformSignatureMatch) {
        boolean testPassed = true;
        // The permission should only be granted if the the app is signed with the same signing key
        // as the declaring package or the platform.
        if (permissionGranted != (signatureMatch || platformSignatureMatch)) {
            testPassed = false;
        }
        sLogger.logInfo(permission + ": " + (testPassed ? PASSED : "FAILED") + " (granted = "
                + permissionGranted + ", signature match = " + signatureMatch
                + ", platform signature match = " + platformSignatureMatch + ")");
    }

    /**
     * Logs the provided {@code message} at the info level.
     *
     * <p>This is typically used to report the status of a {@link
     * BasePermissionTester}, or other status messages that don't
     * apply to a single permission.
     */
    public static void logInfo(String message) {
        sLogger.logInfo(message);
    }

    /**
     * Logs the provided {@code message} at the error level.
     *
     * <p>This is typically used to report an error encountered during a test that does not apply
     * to a single permission.
     */
    public static void logError(String message) {
        sLogger.logError(message);
    }

    /**
     * Logs the provided {@code message} and {@code throwable} at the error level.
     *
     * <p>This is typically used to report a {@link Throwable} caught during a test that does not
     * apply to a single permission.
     *
     * @param message
     * @param throwable
     */
    public static void logError(String message, Throwable throwable) {
        sLogger.logError(message, throwable);
    }
}
