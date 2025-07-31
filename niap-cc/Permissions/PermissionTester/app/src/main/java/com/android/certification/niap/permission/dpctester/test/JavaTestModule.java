package com.android.certification.niap.permission.dpctester.test;
/*
 * Copyright (C) 2024 The Android Open Source Project
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
import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestModuleBase;
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestRunner;
import com.android.certification.niap.permission.dpctester.test.tool.BinderTransaction;
import com.android.certification.niap.permission.dpctester.test.tool.BinderTransactsDict;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTest;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestModule;

import java.lang.reflect.ParameterizedType;
import java.util.function.Consumer;



@PermissionTestModule(name = "Java Test")
public class JavaTestModule extends PermissionTestModuleBase {
    public JavaTestModule(@NonNull Activity ctx) {
        super(ctx);
    }

    @PermissionTest(permission = ".Java Fun",sdkMin = 34,sdkMax = 35)
    public void checkJavaIsWorking() {
        logger.system("message from java!");
        //.invoke(Context.DEVICE_PO
        BinderTransaction.getInstance().invoke(
                Context.DEVICE_POLICY_SERVICE,
                "android.app.admin.IDevicePolicyManager",
                "getNearbyNotificationStreamingPolicy",0
                );

    }
    @PermissionTest(permission = ".Protected Java",sdkMin = 34,sdkMax = 35)
    void checkProtectedMember() {
        logger.system("message from protected member!");
    }
}
