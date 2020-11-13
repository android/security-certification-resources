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

package com.android.certifications.niap.permissions.config;

/**
 * Thrown to indicate that the current test configuration should be bypassed, typically due to the
 * the prereqs for a configuration not being met.
 *
 * <p>This exception should typically be used in cases where the user did not grant a required
 * permission or the component to be tested is not available on the device. The message should be
 * user consumable so that it can be displayed in a toast or dialog.
 */
public class BypassConfigException extends Exception {
    public BypassConfigException(String message) {
        super(message);
    }
}
