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
import android.hardware.input.InputManager;
import android.view.KeyEvent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

// This test demonstrates the danger of INJECT_KEY_EVENTS permission.
// If granted, the app can create a virtual keyboard and inject arbitrary key events.
// We use reflection to access SystemApis to avoid compilation errors on standard SDKs.
@RunWith(AndroidJUnit4.class)
public class InjectKeyEventsTest {
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
    public void testInjectKeyEvents_dangerDemonstration() throws Exception {
        mActivity = getActivity();
        assertTrue("Activity should not be null", mActivity != null);

        // Clear received keys
        mActivity.getReceivedKeyCodes().clear();

        InputManager inputManager = mContext.getSystemService(InputManager.class);
        boolean hasPermission = mContext.checkSelfPermission("android.permission.INJECT_KEY_EVENTS") == android.content.pm.PackageManager.PERMISSION_GRANTED;
        
        Object virtualKeyboard = null;
        try {
            // Load classes via reflection
            Class<?> vkbBuilderClass = Class.forName("android.hardware.input.VirtualKeyboardConfig$Builder");
            Class<?> virtualKeyboardConfigClass = Class.forName("android.hardware.input.VirtualKeyboardConfig");
            
            Object vkbBuilder = vkbBuilderClass.getConstructor().newInstance();
            vkbBuilderClass.getMethod("setInputDeviceName", String.class).invoke(vkbBuilder, "testVirtualKeyboard");
            vkbBuilderClass.getMethod("setLanguageTag", String.class).invoke(vkbBuilder, "en-Latn-US");
            vkbBuilderClass.getMethod("setLayoutType", String.class).invoke(vkbBuilder, "qwerty");
            Object config = vkbBuilderClass.getMethod("build").invoke(vkbBuilder);
            
            java.lang.reflect.Method createVkbMethod = InputManager.class.getMethod("createVirtualKeyboard", virtualKeyboardConfigClass);
            virtualKeyboard = createVkbMethod.invoke(inputManager, config);
            
            android.util.Log.i("InjectKeyEventsTest", "Virtual keyboard created successfully!");
            mActivity.addLogLine("Virtual keyboard created successfully!");
            
            assertTrue("Should have permission if creation succeeded", hasPermission);
            
            // Load VirtualKeyEvent classes
            Class<?> vkeBuilderClass = Class.forName("android.hardware.input.VirtualKeyEvent$Builder");
            Class<?> virtualKeyEventClass = Class.forName("android.hardware.input.VirtualKeyEvent");
            
            // Helper to send key event
            java.lang.reflect.Method sendKeyEventMethod = virtualKeyboard.getClass().getMethod("sendKeyEvent", virtualKeyEventClass);
            
            // Down event
            Object vkeBuilderDown = vkeBuilderClass.getConstructor().newInstance();
            vkeBuilderClass.getMethod("setKeyCode", int.class).invoke(vkeBuilderDown, KeyEvent.KEYCODE_A);
            vkeBuilderClass.getMethod("setAction", int.class).invoke(vkeBuilderDown, KeyEvent.ACTION_DOWN);
            Object eventDown = vkeBuilderClass.getMethod("build").invoke(vkeBuilderDown);
            sendKeyEventMethod.invoke(virtualKeyboard, eventDown);
            
            // Up event
            Object vkeBuilderUp = vkeBuilderClass.getConstructor().newInstance();
            vkeBuilderClass.getMethod("setKeyCode", int.class).invoke(vkeBuilderUp, KeyEvent.KEYCODE_A);
            vkeBuilderClass.getMethod("setAction", int.class).invoke(vkeBuilderUp, KeyEvent.ACTION_UP);
            Object eventUp = vkeBuilderClass.getMethod("build").invoke(vkeBuilderUp);
            sendKeyEventMethod.invoke(virtualKeyboard, eventUp);
            
            mInstrumentation.waitForIdleSync();
            Thread.sleep(1000); // Wait for event processing
            
            boolean received = mActivity.getReceivedKeyCodes().contains(KeyEvent.KEYCODE_A);
            android.util.Log.i("InjectKeyEventsTest", "Received KEYCODE_A via virtual keyboard: " + received);
            mActivity.addLogLine("Received KEYCODE_A via virtual keyboard: " + received);
            
            assertTrue("Key should be received when injected via virtual keyboard", received);
            
        } catch (java.lang.reflect.InvocationTargetException e) {
            Throwable target = e.getTargetException();
            if (target instanceof SecurityException) {
                android.util.Log.i("InjectKeyEventsTest", "SecurityException caught as expected: " + target.getMessage());
                mActivity.addLogLine("SecurityException caught: " + target.getMessage());
                assertFalse("Should NOT have permission if SecurityException is thrown", hasPermission);
            } else {
                throw e;
            }
        } catch (ClassNotFoundException e) {
             // APIs might not be available on this SDK version or device
             android.util.Log.w("InjectKeyEventsTest", "APIs not found: " + e.getMessage());
             mActivity.addLogLine("APIs not found: " + e.getMessage());
        } finally {
            if (virtualKeyboard != null) {
                virtualKeyboard.getClass().getMethod("close").invoke(virtualKeyboard);
            }
        }
    }
}
