/*
 * Copyright 2026 The Android Open Source Project
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

package com.android.certifications.niap.permissions.companion.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Exported service used to test the BIND_DATA_MIGRATION_FOR_PRIVATECOMPUTE permission.
 */
public class TestBindDataMigrationForPrivatecomputeService extends Service {
    private static final String TAG = "PermissionTesterBindService";
    private TestBindDataMigrationForPrivatecomputeServiceImpl bindService;

    @Override
    public void onCreate() {
        super.onCreate();
        bindService = new TestBindDataMigrationForPrivatecomputeServiceImpl();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return bindService;
    }

    static class TestBindDataMigrationForPrivatecomputeServiceImpl extends TestBindService.Stub {
        public void testMethod() {
            Log.d(TAG, "The caller successfully invoked the test method on service "
                    + "TestBindDataMigrationForPrivatecomputeService");
        }
    }
}
