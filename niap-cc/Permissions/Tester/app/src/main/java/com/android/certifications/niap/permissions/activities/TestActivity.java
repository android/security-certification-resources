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

package com.android.certifications.niap.permissions.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.certifications.niap.permissions.R;
import com.android.certifications.niap.permissions.log.Logger;
import com.android.certifications.niap.permissions.log.LoggerFactory;

/**
 * Activity that can be used as the target of an {@link android.content.Intent} when required
 * for permission tests.
 */
public class TestActivity extends AppCompatActivity {
    private static final String TAG = "PermissionTesterTestActivity";
    private static final Logger sLogger = LoggerFactory.createDefaultLogger(TAG);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        sLogger.logDebug("onCreate invoked");
        finish();
    }
}
