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

/**
 * Provides a generic interface for logging trace entries during test execution.
 */
public interface Logger {

    String PASSED = "PASSED";
    String FAILED = "FAILED";
    String ERROR = "TEST ERROR";

    /*
     * Verbose - Only when I would be "tracing" the code and trying to find one part of a function specifically.
     * Debug - Information that is diagnostically helpful to people more than just developers (IT, sysadmins, etc.).
     * Info - Generally useful information to log (service start/stop, configuration assumptions, etc).
     * Info I want to always have available but usually don't care about under normal circumstances.
     * This is my out-of-the-box config level.
     * Warn - Anything that can potentially cause application oddities, but for which I am automatically recovering.
     * (Such as switching from a primary to backup server, retrying an operation, missing secondary data, etc.)
     * Error - Any error which is fatal to the operation, but not the service or application (can't open a required file,
     * missing data, etc.). These errors will force user (administrator, or direct user) intervention.
     *
     * These are usually reserved (in my apps) for incorrect connection strings, missing services, etc.
     */


    /**
     * Logs the provided {@code message} at the debug level.
     */
    void logDebug(String message);

    /**
     * Logs the provided {@code message} and {@code throwable} at the debug level.
     */
    void logDebug(String message, Throwable throwable);

    /**
     * Logs the provided {@code message} at the info level.
     */
    void logInfo(String message);

    /**
     * Logs the provided {@code message} at the error level.
     */
    void logError(String message);
    void logSystem(String message);
    /**
     * Logs the provided {@code message} and {@code throwable} at the error level.
     */
    void logError(String message, Throwable throwable);
    void logWarn(String message);
    default void logTestStatus(String permission, boolean permissionGranted,
                              boolean apiSuccessful) {
        String testStatus = permissionGranted == apiSuccessful ? PASSED : "FAILED";
        String msg = permission + ": " + testStatus + " (granted = " + permissionGranted
                + ", api successful = " + apiSuccessful + ")";

        if(testStatus.equals(PASSED)){
            logInfo(msg);
        } else {
            logError(msg);
        }

    }

    /**
     * Logs the provided {@code permission} as being skipped with the specified {@code message}
     * noting whether the permission was granted based on the {@code permissionGranted} flag.
     *
     * <p>A test should be skipped in cases where a required service, etc., for the test is not
     * available on a device.
     */
    default void logTestSkipped(String permission, boolean permissionGranted,
                                       String message) {
        logInfo(
                permission + ": SKIPPED - " + message + " (granted = " + permissionGranted + ")");
    }

    /**
     * Logs an unexpected {@code throwable} encountered while testing the provided {@code
     * permission}.
     */
    default void logTestError(String permission, Throwable throwable) {
        logError(permission + ": ATEST FAILURE", throwable);
    }

    /**
     * Logs the status of the provided signature {@code permission} based on the specified {@code
     * permissionGranted}, {@code signatureMatch}, and {@code platformSignatureMatch} indciating
     * whether the permission has been granted, the app is signed with the same signing key as the
     * app declaring the permission, and the app is signed with the same signing key as the
     * platform, respectively.
     */
    default void logSignaturePermissionStatus(String permission,
                                                     boolean permissionGranted, boolean signatureMatch, boolean platformSignatureMatch) {
        boolean testPassed = true;
        // The permission should only be granted if the the app is signed with the same signing key
        // as the declaring package or the platform.
        if (permissionGranted != (signatureMatch || platformSignatureMatch)) {
            testPassed = false;
        }

        String msg = permission + ": " + (testPassed ? PASSED : "FAILED") + " (granted = "
                + permissionGranted + ", signature match = " + signatureMatch
                + ", platform signature match = " + platformSignatureMatch + ")";

        if(testPassed){
            logInfo(msg);
        } else {
            logError(msg);
        }
    }
}
