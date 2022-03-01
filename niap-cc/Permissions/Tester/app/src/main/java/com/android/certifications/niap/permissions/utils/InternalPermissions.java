/*
 * Copyright 2021 The Android Open Source Project
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

package com.android.certifications.niap.permissions.utils;

/**
 * Android 12 introduced a new protection level for permissions: permissions with the new internal
 * protection level are no longer automatically granted to requesting apps that are signed with the
 * platform's key, but instead are only granted when an app meets other requirements as declared
 * by the permission's protection flags. Similar to signature permissions, the platform declared
 * internal protection level permissions are hidden from apps. This class provides definitions for
 * all of the platform internal permissions.
 */
public class InternalPermissions {
    /**
     * Definition of all of the internal protection level permissions declared by the platform.
     */
    public static class permission {
        public static final String ACCESS_RCS_USER_CAPABILITY_EXCHANGE =
                "android.permission.ACCESS_RCS_USER_CAPABILITY_EXCHANGE";
        public static final String ASSOCIATE_COMPANION_DEVICES =
                "android.permission.ASSOCIATE_COMPANION_DEVICES";
        public static final String BACKGROUND_CAMERA = "android.permission.BACKGROUND_CAMERA";
        public static final String BYPASS_ROLE_QUALIFICATION =
                "android.permission.BYPASS_ROLE_QUALIFICATION";
        public static final String DOMAIN_VERIFICATION_AGENT =
                "android.permission.DOMAIN_VERIFICATION_AGENT";
        public static final String MANAGE_HOTWORD_DETECTION =
                "android.permission.MANAGE_HOTWORD_DETECTION";
        public static final String OBSERVE_SENSOR_PRIVACY =
                "android.permission.OBSERVE_SENSOR_PRIVACY";
        public static final String PERFORM_IMS_SINGLE_REGISTRATION =
                "android.permission.PERFORM_IMS_SINGLE_REGISTRATION";
        public static final String RECORD_BACKGROUND_AUDIO =
                "android.permission.RECORD_BACKGROUND_AUDIO";
    }
}
