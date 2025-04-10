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
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestModuleBase
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestSuiteBase
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestModule
import com.google.common.base.CaseFormat

class SingleModuleTestSuite(activity: Activity, aModule: PermissionTestModuleBase): PermissionTestSuiteBase(
    async = false,
    activity = activity,
    values = arrayOf(aModule)
){
    init {
        title =  aModule.javaClass.getAnnotation(PermissionTestModule::class.java)?.name
        label =  aModule.javaClass.getAnnotation(PermissionTestModule::class.java)?.label
        key =  CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, aModule.javaClass.simpleName)
        //StaticLogger.debug("title="+title+" label="+label)
        details = "read from module"
    }

}
