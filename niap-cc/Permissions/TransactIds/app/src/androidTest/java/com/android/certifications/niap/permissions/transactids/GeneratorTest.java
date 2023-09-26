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
