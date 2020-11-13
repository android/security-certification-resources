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
import com.android.certifications.niap.permissions.NonPlatformPermissionTester;
import com.android.certifications.niap.permissions.R;

import java.util.Collections;
import java.util.List;

/**
 * Configuration designed to test the non-platform signature permissions declared by preloaded
 * packages on the device. Permissions to be tested can either be explicitly set here through {@link
 * TestConfiguration#getPermissions()}} or all non-platform signature permissions can be tested by
 * default with {@link NonPlatformPermissionTester}.
 */
class NonFrameworkPermissionConfiguration implements TestConfiguration {
    @Override
    public List<BasePermissionTester> getPermissionTesters(Activity activity) {
        return Collections.singletonList(new NonPlatformPermissionTester(this, activity));
    }

    @Override
    public int getButtonTextId() {
        return R.string.run_non_platform_tests;
    }
}
