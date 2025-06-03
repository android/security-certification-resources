package com.android.certification.niap.permission.dpctester.test.runner
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
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestSuite
import com.google.common.base.CaseFormat
import java.util.function.Consumer

open class PermissionTestSuiteBase(val activity: Activity, val async: Boolean, vararg values:PermissionTestModuleBase) {
    var TAG: String = PermissionTestSuiteBase::class.java.simpleName
    var title: String? = javaClass.getAnnotation(PermissionTestSuite::class.java)?.name
    var label: String? = javaClass.getAnnotation(PermissionTestSuite::class.java)?.label
    var details:String? = javaClass.getAnnotation(PermissionTestSuite::class.java)?.details
    var key:String? = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, javaClass.simpleName)
    val info:Info = Info()
    val modules:MutableList<PermissionTestModuleBase> = mutableListOf()
    var methodCallback:Consumer<PermissionTestRunner.Result>?=null;
    //@JvmField
    val testCount:Int
        get() {
            var count = 0
            modules.forEach { m ->
                count += m.testSize + m.additionalTestSize
            }
            //Log.d(TAG, "testCount=$count")
            return count;
        }
    init {
       values.forEach { m ->
           modules.add(m)
           //testCount+=m.testSize
       }
        info.count_modules = modules.size
    }
    var cbSuiteStart: Consumer<Info>?=null;
    var cbSuiteFinish: Consumer<Info>?=null;
    var cbModuleControl:Consumer<PermissionTestModuleBase.Info>?=null
    var cbTestControl:Consumer<PermissionTestModuleBase.Info>?=null
    var cbModuleStart: Consumer<PermissionTestModuleBase.Info>?=null;
    var cbModuleFinish: Consumer<PermissionTestModuleBase.Info>?=null;

    open fun start(callback: Consumer<PermissionTestRunner.Result>?,
                   cbSuiteStart_: Consumer<Info>?,
                   cbSuiteFinish_: Consumer<Info>?,
                   cbModuleStart_: Consumer<PermissionTestModuleBase.Info>?,
                   cbModuleFinish_: Consumer<PermissionTestModuleBase.Info>?,
                   cbModuleControl_:Consumer<PermissionTestModuleBase.Info>?=null,
                   cbTestControl_:Consumer<PermissionTestModuleBase.Info>?=null
    ){
        info.title = title
        info.details = details
        info.ellapsed_time = 0
        //
        cbSuiteFinish = cbSuiteFinish_
        cbSuiteStart = cbSuiteStart_
        cbModuleStart = cbModuleStart_
        cbModuleFinish = cbModuleFinish_
        cbModuleControl = cbModuleControl_
        cbTestControl = cbTestControl_

        //
        PermissionTestRunner.getInstance().start(this,callback)
    }

    open class Info {
        var title: String? = null
        var details:String? = null
        var count_modules: Int = 0
        var count_errors: Int = 0
        var count_bypassed: Int =0
        var ellapsed_time: Long = 0
        var start_time: Long=0
        override fun toString(): String {
            return "title=$title details=$details count_modules=$count_modules count_errors=$count_errors count_bypassed=$count_bypassed ellapsedtime=$ellapsed_time"
        }
    }

}