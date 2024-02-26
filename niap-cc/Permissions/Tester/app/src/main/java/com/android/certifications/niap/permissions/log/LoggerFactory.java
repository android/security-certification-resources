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

import com.android.certifications.niap.permissions.activities.LogListAdaptable;
import com.android.certifications.niap.permissions.activities.MainActivity;

/**
 * Factory to create {@link Logger} instances.
 */
public class LoggerFactory {
    /**
     * Creates and returns a new {@link Logger} instance that uses the provided {@code tag} when
     * logging.
     *
     * <p>This factory method creates a new instance of the default implementation used by this
     * app; currently this is a {@link LogcatLogger}.
     */
    public static Logger createDefaultLogger(String tag) {
        return new LogcatLogger(tag);
    }

    public static Logger createActivityLogger(String tag, LogListAdaptable adaptable) {
        return new UiLogger(tag,adaptable);
    }

}
