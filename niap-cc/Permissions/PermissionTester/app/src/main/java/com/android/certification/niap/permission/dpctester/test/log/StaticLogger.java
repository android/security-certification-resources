package com.android.certification.niap.permission.dpctester.test.log;
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
import androidx.annotation.NonNull;

public class StaticLogger {
    private static Logger logger = new LogcatLogger("Dpm Tester Static Log");

    public static void debug(@NonNull String message) {
        logger.debug(message);
    }

    public static void debug(@NonNull String message, @NonNull Throwable throwable) {
        logger.debug(message,throwable);
    }

    public static  void info(@NonNull String message) {
        logger.info(message);
    }

    public static void info(@NonNull String message, @NonNull Throwable throwable) {
        logger.info(message,throwable);
    }

    public static void error(@NonNull String message) {
        logger.error(message);
    }

    public static void error(@NonNull String message, @NonNull Throwable throwable) {
        logger.error(message, throwable);
    }

    public static void system(@NonNull String message) {
        logger.system(message);
    }

    public void warn(@NonNull String message) {
        logger.warn(message);
    }
}
