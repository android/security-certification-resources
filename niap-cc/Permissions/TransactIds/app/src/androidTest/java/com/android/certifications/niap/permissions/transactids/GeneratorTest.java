/*
 * Copyright 2023 The Android Open Source Project
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

package com.android.certifications.niap.permissions.transactids;

import android.app.UiAutomation;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class GeneratorTest {

    private UiAutomation mUiAutomation;
    @Before
    public void setUp() {
        mUiAutomation = InstrumentationRegistry.getInstrumentation().getUiAutomation();


    }

    @After
    public void tearDown() {
        mUiAutomation.dropShellPermissionIdentity();


    }

    @Test
    public void runTest(){

        mUiAutomation.adoptShellPermissionIdentity();
        //gpu
        ProxyChecker.check("android.graphicsenv.IGpuService","toggleAngleAsSystemDriver");
        //ProxyChecker.check("com.android.server.gpu.GpuService","toggleAngleAsSystemDriver");

    }
}
