/*
 * Copyright (C) 2026 The Android Open Source Project
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
package com.android.certification.niap.permission.dpctester;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.WindowManager;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

// This test verifies CAPTURE_KEYBOARD permission enforcement using UI and key injection.
// It is executed as an instrumentation test.
@RunWith(AndroidJUnit4.class)
public class CaptureKeyboardTest {
    private Instrumentation mInstrumentation;
    private Context mContext;
    private MainActivity mActivity;

    @Before
    public void setUp() {
        mInstrumentation = InstrumentationRegistry.getInstrumentation();
        mContext = mInstrumentation.getTargetContext();
    }

    private MainActivity getActivity() {
        Instrumentation.ActivityMonitor monitor = mInstrumentation.addMonitor(
                MainActivity.class.getName(), null, false);
        Intent intent = mContext.getPackageManager()
                .getLaunchIntentForPackage("com.android.certification.niap.permission.dpctester");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(intent);
        return (MainActivity) monitor.waitForActivityWithTimeout(5000);
    }

    @Test
    public void testCaptureKeyboard_withoutPermission_shouldNotCapture() throws Exception {
        mActivity = getActivity();
        assertTrue("Activity should not be null", mActivity != null);

        // Clear received keys
        mActivity.getReceivedKeyCodes().clear();

        // Try to set keyboard capture enabled
        mActivity.runOnUiThread(() -> {
            WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
            try {
                // Use reflection to avoid compilation errors if SDK doesn't have it
                java.lang.reflect.Method method = WindowManager.LayoutParams.class.getMethod("setKeyboardCaptureEnabled", boolean.class);
                method.invoke(lp, true);
                mActivity.getWindow().setAttributes(lp);
                mActivity.addLogLine("Called setKeyboardCaptureEnabled(true)");
                android.util.Log.i("CaptureKeyboardTest", "Called setKeyboardCaptureEnabled(true)");
            } catch (Exception e) {
                mActivity.addLogLine("Failed to call setKeyboardCaptureEnabled: " + e.getMessage());
                android.util.Log.e("CaptureKeyboardTest", "Failed to call setKeyboardCaptureEnabled", e);
            }
        });

        mInstrumentation.waitForIdleSync();
        Thread.sleep(1000); // Wait for layout and focus

        // Inject KEYCODE_META_LEFT
        mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_META_LEFT);
        mInstrumentation.waitForIdleSync();

        // Check if we received it
        boolean received = mActivity.getReceivedKeyCodes().contains(KeyEvent.KEYCODE_META_LEFT);
        mActivity.addLogLine("Received KEYCODE_META_LEFT: " + received);

        // Check if we have the permission
        boolean hasPermission = mContext.checkSelfPermission("android.permission.CAPTURE_KEYBOARD") == android.content.pm.PackageManager.PERMISSION_GRANTED;
        mActivity.addLogLine("Has CAPTURE_KEYBOARD permission: " + hasPermission);
        android.util.Log.i("CaptureKeyboardTest", "Has permission: " + hasPermission + ", received: " + received);

        if (hasPermission) {
            // With permission, capture should work, so the application should receive the key.
            assertTrue("With permission, we should receive the key because capture succeeded", received);
        } else {
            // Without permission, it should fallback to default behavior (system might consume it).
            mActivity.addLogLine("Without permission, received status: " + received);
            assertFalse("Without permission, we should NOT receive the key", received);
        }
    }
}
