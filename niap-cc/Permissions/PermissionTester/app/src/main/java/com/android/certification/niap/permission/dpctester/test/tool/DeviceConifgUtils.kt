package com.android.certification.niap.permission.dpctester.test.tool
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
class DeviceConfigTool {
    companion object {
        fun getProperty(namespace:String,name:String):String? {
            return Class.forName("android.provider.DeviceConfig")!!.
                getMethod("getProperty", String::class.java,String::class.java).run {
                invoke(null, namespace, name) as String
            }
        }
        fun getBoolean(namespace:String,name:String):Boolean? {
            return Class.forName("android.provider.DeviceConfig")!!.
            getMethod("getBoolean", String::class.java,String::class.java).run {
                invoke(null, namespace, name) as Boolean
            }
        }
        fun setProperty(namespace:String,name:String,value:String,makeDefault:Boolean=false):Boolean {
            return Class.forName("android.provider.DeviceConfig")!!.
            getMethod("setProperty", String::class.java,String::class.java,String::class.java,Boolean::class.java).run {
                invoke(null, namespace, name,value,makeDefault) as Boolean
            }
        }
    }
}