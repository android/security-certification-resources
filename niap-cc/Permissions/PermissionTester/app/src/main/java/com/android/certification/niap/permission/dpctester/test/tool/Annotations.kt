package com.android.certification.niap.permission.dpctester.test.tool
/*
 * Copyright (C) 2025 The Android Open Source Project
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
annotation class PermissionTestSuite(
    val name:String="title",
    val label:String="suite label",
    val details:String="details",
    val prflabel:String="prflabel"
)

annotation class PermissionTestModule(
    val name:String="title",
    val label:String="module label",
    val prflabel:String="prflabel",
    val priority:Int=0,
    val sync:Boolean=false,
)

annotation class PreferenceBool(
    val label:String="module label",
    val prflabel:String="prflabel",
    val defaultValue:Boolean,
)

annotation class PermissionTest(
    val permission:String="title",
    val sdkMin:Int=0,
    val sdkMax:Int=100000,
    val priority:Int=-1,
    val customCase:Boolean=false,
    val requiredPermissions:Array<String> = emptyArray(),
    val requestedPermissions:Array<String> = emptyArray(),
    val developmentProtection: Boolean =false,
    val ignore:Boolean = false
    )