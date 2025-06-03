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
interface Logger {
    val tag: String

    companion object DebugLevel {
        val LEVEL_DEBUG: Int = 0
        val LEVEL_INFO: Int = 1
        val LEVEL_WARN: Int = 2
        val LEVEL_ERROR: Int = 3
        val LEVEL_SYSTEM: Int = 4
    }
    /**
     * Logs the provided `message` at the debug level.
     */
    fun debug(message: String)
    fun debug(message: String, throwable: Throwable)

    /**
     * Logs the provided `message` at the info level.
     */
    fun info(message: String)
    fun info(message: String, throwable: Throwable)

    /**
     * Logs the provided `message` at the error level.
     */
    fun error(message: String)
    fun error(message: String, throwable: Throwable)

    fun system(message: String)
    fun warn(message: String)
}