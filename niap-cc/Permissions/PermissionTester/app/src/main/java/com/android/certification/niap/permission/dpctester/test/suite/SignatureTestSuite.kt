package com.android.certification.niap.permission.dpctester.test.suite
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
import android.app.Activity
import com.android.certification.niap.permission.dpctester.test.RuntimeTestModule
import com.android.certification.niap.permission.dpctester.test.SignatureTestModule
import com.android.certification.niap.permission.dpctester.test.SignatureTestModuleBaklava
import com.android.certification.niap.permission.dpctester.test.SignatureTestModuleBinder
import com.android.certification.niap.permission.dpctester.test.SignatureTestModuleP
import com.android.certification.niap.permission.dpctester.test.SignatureTestModuleQ
import com.android.certification.niap.permission.dpctester.test.SignatureTestModuleR
import com.android.certification.niap.permission.dpctester.test.SignatureTestModuleS
import com.android.certification.niap.permission.dpctester.test.SignatureTestModuleT
import com.android.certification.niap.permission.dpctester.test.SignatureTestModuleU
import com.android.certification.niap.permission.dpctester.test.SignatureTestModuleV
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestSuiteBase
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestSuite

@PermissionTestSuite("Signature Tests","Run Signature/Runtime Test", details = "details")
class SignatureTestSuite(activity: Activity): PermissionTestSuiteBase(
    async = false,
    activity = activity,
    values = arrayOf(
    RuntimeTestModule(activity),
    SignatureTestModule(activity),
    SignatureTestModuleP(activity),
    SignatureTestModuleQ(activity),
    SignatureTestModuleR(activity),
    SignatureTestModuleS(activity),
    SignatureTestModuleT(activity),
    SignatureTestModuleU(activity),
    SignatureTestModuleV(activity),
    SignatureTestModuleBaklava(activity),
    SignatureTestModuleBinder(activity)
        )
){

}
