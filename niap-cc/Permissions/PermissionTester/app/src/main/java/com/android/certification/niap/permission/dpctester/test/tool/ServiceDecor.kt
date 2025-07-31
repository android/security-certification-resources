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
import android.content.Context
import java.util.Objects

class ServiceDecorImpl(context: Context) : ServiceDecor {
    override fun <T> getService(serviceClass: Class<T>): T? {
        TODO("Not yet implemented")
    }

    override fun <T> systemService(clazz: Class<T>): T {
        return Objects.requireNonNull(getService(clazz), "[npe_system_service]" + clazz.simpleName)!!
    }

}

interface ServiceDecor {
    fun <T> getService(serviceClass: Class<T>): T?
    //fun systemService(serviceClass: Class<*>): Any?
    fun <T> systemService(clazz: Class<T>): T;
}


//
//    {
//        return Objects.requireNonNull(getService(clazz), "[npe_system_service]" + clazz.simpleName)
//    }