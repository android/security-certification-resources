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

import com.android.certifications.niap.permissions.BasePermissionTester;
import com.android.certifications.niap.permissions.R;
import com.android.certifications.niap.permissions.SignaturePermissionTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Debug implementation of {@link TestConfiguration} intended to be used during developer to test
 * individual permissions or {@link BasePermissionTester} instances. Typically {@link
 * TestConfiguration#getPermissionTesters(Activity)} is overridden to return the tester to be
 * debugged, along with one or more of {@link TestConfiguration#getInstallPermissions()}, {@link
 * TestConfiguration#getRuntimePermissions()}, {@link TestConfiguration#getSignaturePermissions()},
 * and {@link TestConfiguration#getPrivilegedPermissions()} when testing platform permissions, or
 * just {@link TestConfiguration#getPermissionTesters(Activity)} when testing non-platform
 * permissions.
 */
class DebugConfiguration implements TestConfiguration {
    @Override
    public List<BasePermissionTester> getPermissionTesters(Activity activity) {
        // This will return a single SignaturePermissionTester; this could either be used to debug
        // all of the permissions tested by the SignaturePermissionTester, or
        // getSignaturePermissions could be overridden to return one or more permissions to be
        // tested.
        List<BasePermissionTester> permissionTesters = new ArrayList<>();
        permissionTesters.add(new SignaturePermissionTester(this, activity));
        return permissionTesters;
    }

    @Override
    public Optional<List<String>> getInstallPermissions() {
        return Optional.empty();
    }

    @Override
    public Optional<List<String>> getRuntimePermissions() {
        return Optional.empty();
    }

    @Override
    public Optional<List<String>> getSignaturePermissions() {
        // Returning a single signature permission will allow debug of the test for that one
        // permission; this is useful during development when creating tests for new permissions
        // declared in a release.
        return Optional.empty();
    }

    @Override
    public Optional<List<String>> getInternalPermissions() {
        return Optional.empty();
    }

    @Override
    public Optional<List<String>> getPermissions() {
        return Optional.empty();
    }

    @Override
    public int getButtonTextId() {
        return R.string.run_debug_tests;
    }
}
