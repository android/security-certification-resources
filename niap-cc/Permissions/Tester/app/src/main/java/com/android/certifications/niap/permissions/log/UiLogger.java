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
import com.android.certifications.niap.permissions.activities.LogListAdaptable;

/**
 * {@link Logger} that uses Android's logcat for logging.
 */
public class UiLogger implements Logger {
    private final String mTag;
    public final LogListAdaptable mFrontEnd;

    public final int LEVEL_DEBUG = 0;
    public final int LEVEL_INFO = 1;
    public final int LEVEL_WARN = 2;
    public final int LEVEL_ERROR = 3;
    public final int LEVEL_SYSTEM = 4;
    private int ui_level = LEVEL_ERROR;


    /**
     * Public constructor that uses the provide {@code tag} when writing to logcat.
     */
    public UiLogger(String tag,LogListAdaptable frontEnd) {
        mTag = tag;
        mFrontEnd = frontEnd;
    }

    @Override
    public void logDebug(String message) {
        if (Constants.DEBUG) {
            Log.d(mTag, message);
            if(ui_level<=LEVEL_DEBUG)
                mFrontEnd.addLogLine("🪲"+message);
        }
    }

    @Override
    public void logDebug(String message, Throwable throwable) {
        if (Constants.DEBUG) {
            Log.d(mTag, message, throwable);
            if(ui_level<=LEVEL_DEBUG)
                mFrontEnd.addLogLine("🪲"+message+"/"+throwable);
        }
    }

    @Override
    public void logInfo(String message) {
        Log.i(mTag, message);
        if(ui_level<=LEVEL_INFO)
            mFrontEnd.addLogLine("🟢"+message);
    }

    @Override
    public void logWarn(String message) {
        Log.w(mTag, message);
        if(ui_level<=LEVEL_WARN)
            mFrontEnd.addLogLine("🔴"+message);
    }
    @Override
    public void logError(String message) {
        Log.e(mTag, message);
        if(ui_level<=LEVEL_ERROR)
            mFrontEnd.addLogLine("🔴"+message);
    }


    @Override
    public void logError(String message, Throwable throwable) {
        Log.e(mTag, message, throwable);
        if(ui_level<=LEVEL_ERROR)
            mFrontEnd.addLogLine("🔴"+message+"/"+throwable);
    }

    @Override
    public void logSystem(String message) {
        //Show System important message like result/un-implmented method and so on
        Log.i(mTag, message);
        if(ui_level<=LEVEL_SYSTEM)
            mFrontEnd.addLogLine("❗"+message);
    }
}
