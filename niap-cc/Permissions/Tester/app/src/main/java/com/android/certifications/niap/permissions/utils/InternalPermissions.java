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

        //Android T(13)
        public static final String SET_DEFAULT_ACCOUNT_FOR_CONTACTS =
                "android.permission.SET_DEFAULT_ACCOUNT_FOR_CONTACTS";
        public static final String REQUEST_COMPANION_PROFILE_AUTOMOTIVE_PROJECTION =
                "android.permission.REQUEST_COMPANION_PROFILE_AUTOMOTIVE_PROJECTION";
        public static final String BIND_TRACE_REPORT_SERVICE =
                "android.permission.BIND_TRACE_REPORT_SERVICE";
        public static final String SUBSCRIBE_TO_KEYGUARD_LOCKED_STATE =
                "android.permission.SUBSCRIBE_TO_KEYGUARD_LOCKED_STATE";
        public static final String READ_ASSISTANT_APP_SEARCH_DATA =
                "android.permission.READ_ASSISTANT_APP_SEARCH_DATA";
        public static final String READ_HOME_APP_SEARCH_DATA =
                "android.permission.READ_HOME_APP_SEARCH_DATA";
        public static final String SEND_SAFETY_CENTER_UPDATE =
            "android.permission.SEND_SAFETY_CENTER_UPDATE";
        public static final String CREATE_VIRTUAL_DEVICE =
                "android.permission.CREATE_VIRTUAL_DEVICE";
        public static final String ACCESS_AMBIENT_CONTEXT_EVENT =
                "android.permission.ACCESS_AMBIENT_CONTEXT_EVENT";
        public static final String MANAGE_SAFETY_CENTER =
                "android.permission.MANAGE_SAFETY_CENTER";

        // New internal permissions as of Android 14
        public static final String CAPTURE_CONSENTLESS_BUGREPORT_ON_USERDEBUG_BUILD =
                "android.permission.CAPTURE_CONSENTLESS_BUGREPORT_ON_USERDEBUG_BUILD";
        public static final String EXECUTE_APP_ACTION =
                "android.permission.EXECUTE_APP_ACTION";
        public static final String LAUNCH_CAPTURE_CONTENT_ACTIVITY_FOR_NOTE =
                "android.permission.LAUNCH_CAPTURE_CONTENT_ACTIVITY_FOR_NOTE";
        public static final String MANAGE_DEVICE_LOCK_STATE =
                "android.permission.MANAGE_DEVICE_LOCK_STATE";
        public static final String MANAGE_DEVICE_POLICY_ACCESSIBILITY =
                "android.permission.MANAGE_DEVICE_POLICY_ACCESSIBILITY";
        public static final String MANAGE_DEVICE_POLICY_ACCOUNT_MANAGEMENT =
                "android.permission.MANAGE_DEVICE_POLICY_ACCOUNT_MANAGEMENT";
        public static final String MANAGE_DEVICE_POLICY_ACROSS_USERS =
                "android.permission.MANAGE_DEVICE_POLICY_ACROSS_USERS";
        public static final String MANAGE_DEVICE_POLICY_ACROSS_USERS_FULL =
                "android.permission.MANAGE_DEVICE_POLICY_ACROSS_USERS_FULL";
        public static final String MANAGE_DEVICE_POLICY_ACROSS_USERS_SECURITY_CRITICAL =
                "android.permission.MANAGE_DEVICE_POLICY_ACROSS_USERS_SECURITY_CRITICAL";
        public static final String MANAGE_DEVICE_POLICY_AIRPLANE_MODE =
                "android.permission.MANAGE_DEVICE_POLICY_AIRPLANE_MODE";
        public static final String MANAGE_DEVICE_POLICY_APP_EXEMPTIONS =
                "android.permission.MANAGE_DEVICE_POLICY_APP_EXEMPTIONS";
        public static final String MANAGE_DEVICE_POLICY_APP_RESTRICTIONS =
                "android.permission.MANAGE_DEVICE_POLICY_APP_RESTRICTIONS";
        public static final String MANAGE_DEVICE_POLICY_APP_USER_DATA =
                "android.permission.MANAGE_DEVICE_POLICY_APP_USER_DATA";
        public static final String MANAGE_DEVICE_POLICY_APPS_CONTROL =
                "android.permission.MANAGE_DEVICE_POLICY_APPS_CONTROL";
        public static final String MANAGE_DEVICE_POLICY_AUDIO_OUTPUT =
                "android.permission.MANAGE_DEVICE_POLICY_AUDIO_OUTPUT";
        public static final String MANAGE_DEVICE_POLICY_AUTOFILL =
                "android.permission.MANAGE_DEVICE_POLICY_AUTOFILL";
        public static final String MANAGE_DEVICE_POLICY_BACKUP_SERVICE =
                "android.permission.MANAGE_DEVICE_POLICY_BACKUP_SERVICE";
        public static final String MANAGE_DEVICE_POLICY_BLUETOOTH =
                "android.permission.MANAGE_DEVICE_POLICY_BLUETOOTH";
        public static final String MANAGE_DEVICE_POLICY_BUGREPORT =
                "android.permission.MANAGE_DEVICE_POLICY_BUGREPORT";
        public static final String MANAGE_DEVICE_POLICY_CALLS =
                "android.permission.MANAGE_DEVICE_POLICY_CALLS";
        public static final String MANAGE_DEVICE_POLICY_CAMERA =
                "android.permission.MANAGE_DEVICE_POLICY_CAMERA";
        public static final String MANAGE_DEVICE_POLICY_CERTIFICATES =
                "android.permission.MANAGE_DEVICE_POLICY_CERTIFICATES";
        public static final String MANAGE_DEVICE_POLICY_COMMON_CRITERIA_MODE =
                "android.permission.MANAGE_DEVICE_POLICY_COMMON_CRITERIA_MODE";
        public static final String MANAGE_DEVICE_POLICY_DEBUGGING_FEATURES =
                "android.permission.MANAGE_DEVICE_POLICY_DEBUGGING_FEATURES";
        public static final String MANAGE_DEVICE_POLICY_DEFAULT_SMS =
                "android.permission.MANAGE_DEVICE_POLICY_DEFAULT_SMS";
        public static final String MANAGE_DEVICE_POLICY_DEVICE_IDENTIFIERS =
                "android.permission.MANAGE_DEVICE_POLICY_DEVICE_IDENTIFIERS";
        public static final String MANAGE_DEVICE_POLICY_DISPLAY =
                "android.permission.MANAGE_DEVICE_POLICY_DISPLAY";
        public static final String MANAGE_DEVICE_POLICY_FACTORY_RESET =
                "android.permission.MANAGE_DEVICE_POLICY_FACTORY_RESET";
        public static final String MANAGE_DEVICE_POLICY_FUN =
                "android.permission.MANAGE_DEVICE_POLICY_FUN";
        public static final String MANAGE_DEVICE_POLICY_INPUT_METHODS =
                "android.permission.MANAGE_DEVICE_POLICY_INPUT_METHODS";
        public static final String MANAGE_DEVICE_POLICY_INSTALL_UNKNOWN_SOURCES =
                "android.permission.MANAGE_DEVICE_POLICY_INSTALL_UNKNOWN_SOURCES";
        public static final String MANAGE_DEVICE_POLICY_KEEP_UNINSTALLED_PACKAGES =
                "android.permission.MANAGE_DEVICE_POLICY_KEEP_UNINSTALLED_PACKAGES";
        public static final String MANAGE_DEVICE_POLICY_KEYGUARD =
                "android.permission.MANAGE_DEVICE_POLICY_KEYGUARD";
        public static final String MANAGE_DEVICE_POLICY_LOCALE =
                "android.permission.MANAGE_DEVICE_POLICY_LOCALE";
        public static final String MANAGE_DEVICE_POLICY_LOCATION =
                "android.permission.MANAGE_DEVICE_POLICY_LOCATION";
        public static final String MANAGE_DEVICE_POLICY_LOCK =
                "android.permission.MANAGE_DEVICE_POLICY_LOCK";
        public static final String MANAGE_DEVICE_POLICY_LOCK_CREDENTIALS =
                "android.permission.MANAGE_DEVICE_POLICY_LOCK_CREDENTIALS";
        public static final String MANAGE_DEVICE_POLICY_LOCK_TASK =
                "android.permission.MANAGE_DEVICE_POLICY_LOCK_TASK";
        public static final String MANAGE_DEVICE_POLICY_METERED_DATA =
                "android.permission.MANAGE_DEVICE_POLICY_METERED_DATA";
        public static final String MANAGE_DEVICE_POLICY_MICROPHONE =
                "android.permission.MANAGE_DEVICE_POLICY_MICROPHONE";
        public static final String MANAGE_DEVICE_POLICY_MOBILE_NETWORK =
                "android.permission.MANAGE_DEVICE_POLICY_MOBILE_NETWORK";
        public static final String MANAGE_DEVICE_POLICY_MODIFY_USERS =
                "android.permission.MANAGE_DEVICE_POLICY_MODIFY_USERS";
        public static final String MANAGE_DEVICE_POLICY_MTE =
                "android.permission.MANAGE_DEVICE_POLICY_MTE";
        public static final String MANAGE_DEVICE_POLICY_NEARBY_COMMUNICATION =
                "android.permission.MANAGE_DEVICE_POLICY_NEARBY_COMMUNICATION";
        public static final String MANAGE_DEVICE_POLICY_NETWORK_LOGGING =
                "android.permission.MANAGE_DEVICE_POLICY_NETWORK_LOGGING";
        public static final String MANAGE_DEVICE_POLICY_ORGANIZATION_IDENTITY =
                "android.permission.MANAGE_DEVICE_POLICY_ORGANIZATION_IDENTITY";
        public static final String MANAGE_DEVICE_POLICY_OVERRIDE_APN =
                "android.permission.MANAGE_DEVICE_POLICY_OVERRIDE_APN";
        public static final String MANAGE_DEVICE_POLICY_PACKAGE_STATE =
                "android.permission.MANAGE_DEVICE_POLICY_PACKAGE_STATE";
        public static final String MANAGE_DEVICE_POLICY_PHYSICAL_MEDIA =
                "android.permission.MANAGE_DEVICE_POLICY_PHYSICAL_MEDIA";
        public static final String MANAGE_DEVICE_POLICY_PRINTING =
                "android.permission.MANAGE_DEVICE_POLICY_PRINTING";
        public static final String MANAGE_DEVICE_POLICY_PRIVATE_DNS =
                "android.permission.MANAGE_DEVICE_POLICY_PRIVATE_DNS";
        public static final String MANAGE_DEVICE_POLICY_PROFILE_INTERACTION =
                "android.permission.MANAGE_DEVICE_POLICY_PROFILE_INTERACTION";
        public static final String MANAGE_DEVICE_POLICY_PROFILES =
                "android.permission.MANAGE_DEVICE_POLICY_PROFILES";
        public static final String MANAGE_DEVICE_POLICY_PROXY =
                "android.permission.MANAGE_DEVICE_POLICY_PROXY";
        public static final String MANAGE_DEVICE_POLICY_QUERY_SYSTEM_UPDATES =
                "android.permission.MANAGE_DEVICE_POLICY_QUERY_SYSTEM_UPDATES";
        public static final String MANAGE_DEVICE_POLICY_RESET_PASSWORD =
                "android.permission.MANAGE_DEVICE_POLICY_RESET_PASSWORD";
        public static final String MANAGE_DEVICE_POLICY_RESTRICT_PRIVATE_DNS =
                "android.permission.MANAGE_DEVICE_POLICY_RESTRICT_PRIVATE_DNS";
        public static final String MANAGE_DEVICE_POLICY_RUN_IN_BACKGROUND =
                "android.permission.MANAGE_DEVICE_POLICY_RUN_IN_BACKGROUND";
        public static final String MANAGE_DEVICE_POLICY_RUNTIME_PERMISSIONS =
                "android.permission.MANAGE_DEVICE_POLICY_RUNTIME_PERMISSIONS";
        public static final String MANAGE_DEVICE_POLICY_SAFE_BOOT =
                "android.permission.MANAGE_DEVICE_POLICY_SAFE_BOOT";
        public static final String MANAGE_DEVICE_POLICY_SCREEN_CAPTURE =
                "android.permission.MANAGE_DEVICE_POLICY_SCREEN_CAPTURE";
        public static final String MANAGE_DEVICE_POLICY_SCREEN_CONTENT =
                "android.permission.MANAGE_DEVICE_POLICY_SCREEN_CONTENT";
        public static final String MANAGE_DEVICE_POLICY_SECURITY_LOGGING =
                "android.permission.MANAGE_DEVICE_POLICY_SECURITY_LOGGING";
        public static final String MANAGE_DEVICE_POLICY_SETTINGS =
                "android.permission.MANAGE_DEVICE_POLICY_SETTINGS";
        public static final String MANAGE_DEVICE_POLICY_SMS =
                "android.permission.MANAGE_DEVICE_POLICY_SMS";
        public static final String MANAGE_DEVICE_POLICY_STATUS_BAR =
                "android.permission.MANAGE_DEVICE_POLICY_STATUS_BAR";
        public static final String MANAGE_DEVICE_POLICY_SUPPORT_MESSAGE =
                "android.permission.MANAGE_DEVICE_POLICY_SUPPORT_MESSAGE";
        public static final String MANAGE_DEVICE_POLICY_SUSPEND_PERSONAL_APPS =
                "android.permission.MANAGE_DEVICE_POLICY_SUSPEND_PERSONAL_APPS";
        public static final String MANAGE_DEVICE_POLICY_SYSTEM_APPS =
                "android.permission.MANAGE_DEVICE_POLICY_SYSTEM_APPS";
        public static final String MANAGE_DEVICE_POLICY_SYSTEM_DIALOGS =
                "android.permission.MANAGE_DEVICE_POLICY_SYSTEM_DIALOGS";
        public static final String MANAGE_DEVICE_POLICY_SYSTEM_UPDATES =
                "android.permission.MANAGE_DEVICE_POLICY_SYSTEM_UPDATES";
        public static final String MANAGE_DEVICE_POLICY_TIME =
                "android.permission.MANAGE_DEVICE_POLICY_TIME";
        public static final String MANAGE_DEVICE_POLICY_USB_DATA_SIGNALLING =
                "android.permission.MANAGE_DEVICE_POLICY_USB_DATA_SIGNALLING";
        public static final String MANAGE_DEVICE_POLICY_USB_FILE_TRANSFER =
                "android.permission.MANAGE_DEVICE_POLICY_USB_FILE_TRANSFER";
        public static final String MANAGE_DEVICE_POLICY_USERS =
                "android.permission.MANAGE_DEVICE_POLICY_USERS";
        public static final String MANAGE_DEVICE_POLICY_VPN =
                "android.permission.MANAGE_DEVICE_POLICY_VPN";
        public static final String MANAGE_DEVICE_POLICY_WALLPAPER =
                "android.permission.MANAGE_DEVICE_POLICY_WALLPAPER";
        public static final String MANAGE_DEVICE_POLICY_WIFI =
                "android.permission.MANAGE_DEVICE_POLICY_WIFI";
        public static final String MANAGE_DEVICE_POLICY_WINDOWS =
                "android.permission.MANAGE_DEVICE_POLICY_WINDOWS";
        public static final String MANAGE_DEVICE_POLICY_WIPE_DATA =
                "android.permission.MANAGE_DEVICE_POLICY_WIPE_DATA";
        public static final String PROVIDE_OWN_AUTOFILL_SUGGESTIONS =
                "android.permission.PROVIDE_OWN_AUTOFILL_SUGGESTIONS";
        public static final String READ_RESTRICTED_STATS =
                "android.permission.READ_RESTRICTED_STATS";

        // New internal permissions as of Android 15
        public static final String MANAGE_DEVICE_POLICY_THREAD_NETWORK =
                "android.permission.MANAGE_DEVICE_POLICY_THREAD_NETWORK";
        public static final String MANAGE_DEVICE_POLICY_ASSIST_CONTENT =
                "android.permission.MANAGE_DEVICE_POLICY_ASSIST_CONTENT";
        public static final String MANAGE_DEVICE_POLICY_AUDIT_LOGGING =
                "android.permission.MANAGE_DEVICE_POLICY_AUDIT_LOGGING";
        public static final String QUERY_DEVICE_STOLEN_STATE =
                "android.permission.QUERY_DEVICE_STOLEN_STATE";
        public static final String MANAGE_DEVICE_POLICY_CONTENT_PROTECTION =
                "android.permission.MANAGE_DEVICE_POLICY_CONTENT_PROTECTION";
        public static final String MANAGE_DEVICE_POLICY_MANAGED_SUBSCRIPTIONS =
                "android.permission.MANAGE_DEVICE_POLICY_MANAGED_SUBSCRIPTIONS";
        public static final String MANAGE_DEVICE_POLICY_BLOCK_UNINSTALL =
                "android.permission.MANAGE_DEVICE_POLICY_BLOCK_UNINSTALL";
        public static final String MANAGE_DEVICE_POLICY_CAMERA_TOGGLE =
                "android.permission.MANAGE_DEVICE_POLICY_CAMERA_TOGGLE";
        public static final String MANAGE_DEVICE_POLICY_MICROPHONE_TOGGLE =
                "android.permission.MANAGE_DEVICE_POLICY_MICROPHONE_TOGGLE";
        public static final String MANAGE_DEVICE_POLICY_STORAGE_LIMIT =
                "android.permission.MANAGE_DEVICE_POLICY_STORAGE_LIMIT";
        public static final String EMBED_ANY_APP_IN_UNTRUSTED_MODE =
                "android.permission.EMBED_ANY_APP_IN_UNTRUSTED_MODE";
        public static final String ALWAYS_UPDATE_WALLPAPER =
                "android.permission.ALWAYS_UPDATE_WALLPAPER";

    }
}
