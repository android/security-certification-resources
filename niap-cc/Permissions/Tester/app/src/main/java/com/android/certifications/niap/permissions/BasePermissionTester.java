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

package com.android.certifications.niap.permissions;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.NonNull;

import com.android.certifications.niap.permissions.config.TestConfiguration;
import com.android.certifications.niap.permissions.log.Logger;
import com.android.certifications.niap.permissions.log.LoggerFactory;
import com.android.certifications.niap.permissions.log.StatusLogger;
import com.android.certifications.niap.permissions.utils.PermissionUtils;
import com.android.certifications.niap.permissions.utils.SignatureUtils;
import com.android.certifications.niap.permissions.utils.Transacts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Base class that provides standard functionality for performing permission tests. Permission
 * tester classes should extend this class and implement the {@link #runPermissionTests()} method to
 * test all permissions to be verified by the class.
 */
public abstract class BasePermissionTester {
    private static final String TAG = "PermissionTester";

    protected final Logger mLogger;
    protected final TestConfiguration mConfiguration;
    protected final Activity mActivity;
    protected final Context mContext;
    protected final ContentResolver mContentResolver;
    protected final PackageManager mPackageManager;

    protected final ApplicationInfo mAppInfo;
    protected final String mPackageName;
    protected final int mUid;
    protected final Signature mAppSignature;
    protected final boolean mPlatformSignatureMatch;
    protected final int mDeviceApiLevel;
    protected final Set<String> mPlatformPermissions;
    protected final Transacts mTransacts;

    protected static String TEST_STATUS_PASSED;
    protected static String TEST_STATUS_FAILED;
    protected static String TEST_STATUS_ERROR;
    protected static String TEST_STATUS_SKIPPED;

    /**
     * Constructs a new instance with the provided {@code configuration} and {@code activity}.
     */
    public BasePermissionTester(TestConfiguration configuration, Activity activity) {
        mConfiguration = configuration;
        mActivity = activity;
        mContext = activity.getApplicationContext();
        mContentResolver = mContext.getContentResolver();
        mAppInfo = mContext.getApplicationInfo();
        mPackageName = mContext.getPackageName();
        mUid = mAppInfo.uid;
        mPackageManager = mContext.getPackageManager();
        mLogger = LoggerFactory.createDefaultLogger(TAG);
        mDeviceApiLevel = Build.VERSION.SDK_INT;
        mTransacts = Transacts.createTransactsForApiLevel(mDeviceApiLevel);

        // Use this app's signing certificate to determine if the platform is signed with the same
        // key.
        mAppSignature = SignatureUtils.getTestAppSigningCertificate(mContext);
        mPlatformSignatureMatch = mPackageManager.hasSigningCertificate(Constants.PLATFORM_PACKAGE,
                mAppSignature.toByteArray(), PackageManager.CERT_INPUT_RAW_X509);
        mLogger.logDebug(
                "Device API: " + mDeviceApiLevel + ", build fingerprint: " + Build.FINGERPRINT
                        + ", matches platform signature: " + mPlatformSignatureMatch + ", uid: "
                        + mUid);

        // Obtain all platform declared permissions to ensure tests are only attempted against
        // those declared on the device.
        mPlatformPermissions = new HashSet<>();
        for (PermissionInfo permission : PermissionUtils.getAllDeclaredPlatformPermissions(mContext)) {
            mPlatformPermissions.add(permission.name);
        }

        Resources resources = activity.getResources();
        TEST_STATUS_PASSED = resources.getString(R.string.test_passed);
        TEST_STATUS_FAILED = resources.getString(R.string.test_failed);
        TEST_STATUS_ERROR = resources.getString(R.string.test_error);
        TEST_STATUS_SKIPPED = resources.getString(R.string.test_skipped);
    }

    /**
     * Returns whether the specified {@code permission} has been granted to this app.
     */
    public boolean isPermissionGranted(String permission) {
        return mContext.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Runs all of the permission tests defined by the current tester and returns a {@code boolean}
     * indicating whether all tests completed successfully.
     */
    public abstract boolean runPermissionTests();

    public abstract Map<String,PermissionTest> getRegisteredPermissions();

    /**
     * @see #runPermissionTest(String, PermissionTest, boolean)
     */
    public boolean runPermissionTest(String permission, PermissionTest test) {
        return runPermissionTest(permission, test, false);
    }

    /**
     * Runs the provided {@code test} for the specified {@code permission}, and returns whether the
     * test completed successfully; if {@code exceptionAllowed} is set to true then the API will
     * be considered successful even if an {@code Exception} other than a {@code SecurityException}
     * is caught (this can be used for permissions that are typically not exposed to external apps).
     *
     * <p>This method provides a basic framework for running permission tests using the {@link
     * PermissionTest} class by performing the following:
     * <ul>
     *     <li>If the permission is not declared by the platform then the test is skipped.
     *     <li>If the device under test does not meet the requirements for the minimum or maximum
     *     API level for this permission then the test is skipped.
     *     <li>If the provided {@code test} contains a custom permission test then the test is run
     *     as is outside the framework for the test. Note this will always result in a {@code true}
     *     value being returned; it is up to the custom test to log its own completion status.
     * </ul>
     *
     * <p>If a custom test is not provided then the {@code test}'s {@link PermissionTest#runTest()}}
     * method is invoked; this typically involves executing the {@link Runnable} for the test. If
     * this completes without any exceptions then the API for the permission is marked as
     * successful, if a {@link SecurityException} is caught then the API is marked as failed. The
     * permission test is considered passed if the API is successful and the permission has been
     * granted or if the API fails and the permission is not granted.
     *
     * <p>If a {@link BypassTestException} is caught then the permission test is marked as skipped;
     * this can be expected if the device does not meet some requirement for the permission.
     *
     * <p>If any other {@link Throwable} is caught then the test is marked as an error, and the
     * resulting {@code Throwable} is logged.
     *
     * @return true if the test is skipped, if the test is a custom test, or if the test is passed
     */
    public boolean runPermissionTest(String permission, PermissionTest test,
            boolean exceptionAllowed) {
        boolean testPassed = true;
        // if the permission does not exist then skp the test and return immediately.
        if (!mPlatformPermissions.contains(permission)) {
            mLogger.logDebug("The permission " + permission
                    + " is not declared by the platform on this device");
            return true;
        }
        if (mDeviceApiLevel < test.mMinApiLevel) {
            mLogger.logDebug(
                    "permission " + permission + " is targeted for min API " + test.mMinApiLevel
                            + "; device API is " + mDeviceApiLevel);
            return true;
        } else if (mDeviceApiLevel > test.mMaxApiLevel) {
            mLogger.logDebug(
                    "permission " + permission + " is targeted for max API " + test.mMaxApiLevel
                            + "; device API is " + mDeviceApiLevel);
            return true;
        }

        if (test.mIsCustom) {
            test.runTest();
        } else {
            boolean permissionGranted = isPermissionGranted(permission);
            try {
                test.runTest();
                // If the permission was granted then a SecurityException should not have been
                // thrown so the result of the test should match whether the permission was granted.
                testPassed = getAndLogTestStatus(permission, permissionGranted, true);
            } catch (BypassTestException bte) {
                StatusLogger.logTestSkipped(permission, permissionGranted, bte.getMessage());
            } catch (SecurityException e) {
                // Similar to above if the permission was not granted then a SecurityException
                // should have been thrown so the result of the test should be the opposite of
                // whether the permission was granted.
                mLogger.logDebug("Caught a SecurityException for permission " + permission + ": ",
                        e);
                if (e.getCause() != null) {
                    mLogger.logDebug("SecurityException cause: ", e.getCause());
                }
                testPassed = getAndLogTestStatus(permission, permissionGranted, false);
            } catch (UnexpectedPermissionTestFailureException e) {
                testPassed = false;
                StatusLogger.logTestError(permission, e);
            } catch (Throwable t) {
                // Any other Throwable indicates the test did not fail due to a SecurityException;
                // treat the API as successful if the caller specified exceptions are allowed.
                if (exceptionAllowed) {
                    mLogger.logDebug("Caught a Throwable for permission " + permission + ": ", t);
                    testPassed = getAndLogTestStatus(permission, permissionGranted, true);
                } else {
                    // else an Exception was not expected; treat the test as failed and log the
                    // error status.
                    testPassed = false;
                    StatusLogger.logTestError(permission, t);
                }
            }
        }
        return testPassed;
    }

    /**
     * Logs and returns the status of the test for the provided {@code permission} given whether
     * {@code permissionGranted} and {@code apiSuccessful}.
     *
     * <p>This method should be overridden by subclasses that test permissions that have additional
     * requirements to be granted.
     */
    protected boolean getAndLogTestStatus(String permission, boolean permissionGranted,
            boolean apiSuccessful) {
        // If the permission was granted then the API should have been successful.
        boolean testPassed = permissionGranted == apiSuccessful;
        StatusLogger.logTestStatus(permission, permissionGranted, apiSuccessful);
        return testPassed;
    }

    protected static HashMap<String, ArrayList<String>> mInvokedTransacts = new HashMap<>();



    /**
     * Encapsulates components required for a permission test. The main component is the {@link
     * Runnable} containing a snippet of code to be run to test a permission; in its simplest form
     * this is just an API that is guarded by the permission. Some permissions guard APIs that are
     * not part of the public API; for these tests the {@code Runnable} may contain code to invoke a
     * reflective call for hidden APIs or a direct transact to directly interface with a method
     * guarded by a permission exposed by a service.
     * <p>
     * Tests can also have minimum / maximum API levels defined; these are intended to represent
     * when the permission was first introduced or when it was no longer used, respectively. The
     * {@link #runTest()} method will first verify that the API level of the device under test falls
     * within the range of this test's min / max API levels.
     * <p>
     * A custom test can be used when a permission cannot be tested with an API, reflective call,
     * direct transact, etc., but instead requires additional setup. For instance a custom test may
     * be required for a permission that first requires starting a service; while the service is
     * able to invoke an API to test the permission the service will no longer be within the
     * framework of the permission test. Any permission that requires a custom test must ensure the
     * appropriate method from {@link StatusLogger} is invoked to log the status of the test.
     */
    public static class PermissionTest {
        public boolean mIsCustom;
        public Runnable mTestRunnable;
        public int mMinApiLevel;
        public int mMaxApiLevel;

        /**
         * Constructs a new instance that will run on all API levels supported by this app.
         *
         * @param isCustomTest {@code true} if the provided {@code test} should be treated as a
         *                     custom test
         * @param test         {@link Runnable} to test the permission
         */
        public PermissionTest(boolean isCustomTest, Runnable test) {
            this(isCustomTest, Build.VERSION_CODES.P, test);
        }

        /**
         * Constructs a new instance that will run on all API levels starting with the provided
         * {@code minApiLevel}.
         *
         * @param isCustomTest {@code true} if the provided {@code test} should be treated as a
         *                     custom test
         * @param minApiLevel  the minimum API level supported by this test
         * @param test         {@link Runnable} to test the permission
         */
        public PermissionTest(boolean isCustomTest, int minApiLevel, Runnable test) {
            this(isCustomTest, minApiLevel, Integer.MAX_VALUE, test);
        }

        /**
         * Constructs a new instance that will run on all platform levels between the provided
         * {@code minApiLevel} and {@code maxApiLevel} inclusive.
         *
         * @param isCustomTest {@code true} if the provided {@code test} should be treated as a
         *                     custom test
         * @param minApiLevel  the minimum API level supported by this test
         * @param maxApiLevel  the maximum API level supported by this test
         * @param test         {@link Runnable} to test the permission
         */
        public PermissionTest(boolean isCustomTest, int minApiLevel, int maxApiLevel,
                Runnable test) {
            this.mIsCustom = isCustomTest;
            this.mTestRunnable = test;
            this.mMinApiLevel = minApiLevel;
            this.mMaxApiLevel = maxApiLevel;
        }

        /**
         * Invokes the {@link Runnable}'s run method for this test.
         */
        public void runTest() {
            mTestRunnable.run();
        }

        @NonNull
        @Override
        public String toString() {
            return "PermissionTest{" +
                    "mIsCustom=" + mIsCustom +
                    ", mTestRunnable=" + mTestRunnable +
                    ", mMinApiLevel=" + mMinApiLevel +
                    ", mMaxApiLevel=" + mMaxApiLevel +
                    '}';
        }
    }

    /**
     * Thrown when the device under test does not meet the requirements for the current permission
     * test indicating that the test should be considered as skipped on the device.
     */
    public static class BypassTestException extends RuntimeException {
        /**
         * Constructs a new instance with the provided {@code message}.
         */
        public BypassTestException(String message) {
            super(message);
        }
    }

    /**
     * Thrown when an unexpected failure is encountered during a permission test indicating the
     * current permission test should be treated as an error.
     */
    public static class UnexpectedPermissionTestFailureException extends RuntimeException {
        /**
         * Constructs a new instance with the provided {@code message}.
         */
        public UnexpectedPermissionTestFailureException(String message) {
            super(message);
        }

        /**
         * Constructs a new instance with the provided {@code cause}.
         */
        public UnexpectedPermissionTestFailureException(Throwable cause) {
            super(cause);
        }
    }
}
