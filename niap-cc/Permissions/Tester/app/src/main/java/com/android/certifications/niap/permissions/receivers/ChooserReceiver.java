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
package com.android.certifications.niap.permissions.receivers;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ChooserReceiver extends BroadcastReceiver {

    public static final int TEST_LAUNCH_DEVICE_MANAGER_SETUP = 1000;

    private static final String TAG = "ChooserReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        String test = intent.getStringExtra("test");
        int test_id = intent.getIntExtra("test_id",0);
        if(test_id == TEST_LAUNCH_DEVICE_MANAGER_SETUP){
            Object componentName = bundle.get(Intent.EXTRA_CHOSEN_COMPONENT);
            Log.d(TAG,"*** PASSED "+test+" test case. success.("+componentName+")");
        }
    }
}
