package com.android.certification.niap.permission.dpctester.test.log
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
import android.util.Log

class LogcatLogger(override val tag: String):Logger{

    override fun debug(message: String) {
        Log.d(tag,message)
    }

    override fun debug(message: String, throwable: Throwable) {
        Log.d(tag,message,throwable)
    }

    override fun info(message: String) {
        Log.d(tag,message)
    }

    override fun info(message: String, throwable: Throwable) {
        Log.i(tag,message,throwable)
    }

    override fun error(message: String) {
        Log.e(tag,message)
    }

    override fun error(message: String, throwable: Throwable) {
        Log.e(tag,message,throwable)
    }

    override fun system(message: String) {
        Log.e(tag,message)
    }

    override fun warn(message: String) {
        Log.w(tag,message)
    }
}