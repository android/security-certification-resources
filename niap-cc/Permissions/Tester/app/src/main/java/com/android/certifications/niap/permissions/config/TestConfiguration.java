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

package com.android.certifications.niap.permissions.config;

import android.app.Activity;
import android.util.ArraySet;

import com.android.certifications.niap.permissions.BasePermissionTester;
import com.android.certifications.niap.permissions.Constants;
import com.android.certifications.niap.permissions.InstallPermissionTester;
import com.android.certifications.niap.permissions.InternalPermissionTester;
import com.android.certifications.niap.permissions.PrivilegedPermissionTester;
import com.android.certifications.niap.permissions.R;
import com.android.certifications.niap.permissions.RuntimePermissionTester;
import com.android.certifications.niap.permissions.SignaturePermissionTester;
import com.android.certifications.niap.permissions.utils.SignaturePermissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Allows the execution of the tests to be configured for specific use cases.
 *
 * <p>The default implementation provides the configuration that can be used to test the platform
 * permissions.
 */
public interface TestConfiguration {
    /**
     * Runs any necessary setup before the permission tests are started.
     */
    default void preRunSetup() throws BypassConfigException {
    }

    /**
     * Returns a {@link List} of {@link BasePermissionTester} instances that should be run.
     */
    default List<BasePermissionTester> getPermissionTesters(Activity activity) {
        List<BasePermissionTester> permissionTesters = new ArrayList<>();
        permissionTesters.add(new RuntimePermissionTester(this, activity));
        permissionTesters.add(new SignaturePermissionTester(this, activity));
        if (Constants.USE_PRIVILEGED_PERMISSION_TESTER) {
            permissionTesters.add(new PrivilegedPermissionTester(this, activity));
        }
        permissionTesters.add(new InternalPermissionTester(this, activity));
        permissionTesters.add(new InstallPermissionTester(this, activity));
        return permissionTesters;
    }

    /**
     * Callback from the Activity after a result is received from the {@link
     * androidx.core.app.ActivityCompat#requestPermissions(Activity, String[], int)} invocation.
     */
    default void onRequestPermissionsResult(int requestCode, String[] permissions,
            int[] grantResults) {
    }

    /**
     * Returns an {@link Optional} containing a {@link List} of install permissions to test, or
     * {@link Optional#empty()} if all install permissions should be tested.
     */
    default Optional<List<String>> getInstallPermissions() {
        return Optional.empty();
    }

    /**
     * Returns an {@link Optional} containing a {@link List} of runtime permissions to test, or
     * {@link Optional#empty()} if all runtime permissions should be tested.
     */
    default Optional<List<String>> getRuntimePermissions() {
        return Optional.empty();
    }

    /**
     * Returns an {@link Optional} containing a {@link List} of signature permissions to test, or
     * {@link Optional#empty()} if all signature permissions should be tested.
     */
    default Optional<List<String>> getSignaturePermissions() {
       //List<String> list = new ArrayList<>();
       //SignatureDependentPermissionConfiguration.addPermissionsFor28(list);
       // SignatureDependentPermissionConfiguration.addPermissionsFor29(list);
       // SignatureDependentPermissionConfiguration.addPermissionsFor30(list);
       // SignatureDependentPermissionConfiguration.addPermissionsFor31(list);
        //SignatureDependentPermissionConfiguration.addPermissionsFor32(list);
        //SignatureDependentPermissionConfiguration.addPermissionsFor33(list);
        //return Optional.of(list);//list;
        return Optional.empty();
    }

    /**
     * Returns an {@link Optional} containing a {@link List} of privileged permissions to test, or
     * {@link Optional#empty()} if all privileged permissions should be tested.
     */
    default Optional<List<String>> getPrivilegedPermissions() {
        return Optional.empty();
    }

    /**
     * Returns an {@link Optional} containing a {@link List} of internal permissions to test, or
     * {@link Optional#empty()}} if all internal permissions should be tested.
     */
    default Optional<List<String>> getInternalPermissions() {
        return Optional.empty();
    }

    /**
     * Returns an {@link Optional} containing a {@link List} of permissions to test, or {@link
     * Optional#empty()} if all permissions should be tested.
     *
     * <p>Note, this is a generic version of the various protection levels of permissions. This can
     * be used for a configuration that doesn't necessarily fit a single protection level / flag.
     */
    default Optional<List<String>> getPermissions() {
        return Optional.empty();
    }

    /**
     * Returns an {@link Optional} containing a {@link Set} of signature permissions to be skipped,
     * or {@link Optional#empty()} if no signature permissions should be skipped.
     */
    default Optional<Set<String>> getSkippedSignaturePermissions() {
        //Set<String> list = new ArraySet<>();
        //list.add(SignaturePermissions.permission.RESTART_WIFI_SUBSYSTEM);
        //return Optional.of(list);
        return Optional.empty();
    }

    /**
     * Returns an {@link Optional} containing a {@link Set} of internal permissions to be skipped,
     * or {@link Optional#empty()} if no internal permissions should be skipped.
     */
    default Optional<Set<String>> getSkippedInternalPermissions() {
        return Optional.empty();
    }

    /**
     * Returns an int representing the String resource to be used as the button test for the current
     * configuration.
     */
    default int getButtonTextId() {
        return R.string.run_platform_tests;
    }
}
