/*
 * Copyright 2020 The Android Open Source Project
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

package com.android.certifications.niap.permissions.log;

import android.util.Log;

import com.android.certifications.niap.permissions.Constants;

/**
 * {@link Logger} that uses Android's logcat for logging.
 */
public class LogcatLogger implements Logger {
    private final String mTag;

    /**
     * Public constructor that uses the provide {@code tag} when writing to logcat.
     */
    public LogcatLogger(String tag) {
        mTag = tag;
    }

    @Override
    public void logDebug(String message) {
        if (Constants.DEBUG) {
            Log.d(mTag, message);
        }
    }

    @Override
    public void logDebug(String message, Throwable throwable) {
        if (Constants.DEBUG) {
            Log.d(mTag, message, throwable);
        }
    }

    @Override
    public void logInfo(String message) {
        Log.i(mTag, message);
    }

    @Override
    public void logInfo(String message, Throwable throwable) {
        if (Constants.DEBUG) {
            Log.i(mTag, message, throwable);
        }
    }


    @Override
    public void logError(String message) {
        Log.e(mTag, message);
    }
    public void logWarn(String message) { Log.w(mTag,message);}
    @Override
    public void logError(String message, Throwable throwable) {
        Log.e(mTag, message, throwable);
    }

    @Override
    public void logSystem(String message) {
        Log.i(mTag, message);
    }

}
