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

package com.android.certifications.niap.permissions.utils;

import android.content.Context;
import android.content.pm.PermissionInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The platform declared signature protection level permissions are hidden from apps. This class
 * provides definitions for all of the platform signature permissions and methods for apps to obtain
 * either all of these permissions or those filtered by protection flags (ie privileged and
 * development permissions).
 */
public class SignaturePermissions {
    /**
     * Definition of all of the signature protection level permissions declared by the platform.
     */
    public static class permission {
        public static final String ACCESS_AMBIENT_LIGHT_STATS =
                "android.permission.ACCESS_AMBIENT_LIGHT_STATS";
        public static final String ACCESS_BROADCAST_RADIO =
                "android.permission.ACCESS_BROADCAST_RADIO";
        public static final String ACCESS_CACHE_FILESYSTEM =
                "android.permission.ACCESS_CACHE_FILESYSTEM";
        public static final String ACCESS_CHECKIN_PROPERTIES =
                "android.permission.ACCESS_CHECKIN_PROPERTIES";
        public static final String ACCESS_CONTENT_PROVIDERS_EXTERNALLY =
                "android.permission.ACCESS_CONTENT_PROVIDERS_EXTERNALLY";
        public static final String ACCESS_DRM_CERTIFICATES =
                "android.permission.ACCESS_DRM_CERTIFICATES";
        public static final String ACCESS_FM_RADIO = "android.permission.ACCESS_FM_RADIO";
        public static final String ACCESS_IMS_CALL_SERVICE =
                "android.permission.ACCESS_IMS_CALL_SERVICE";
        public static final String ACCESS_INPUT_FLINGER = "android.permission.ACCESS_INPUT_FLINGER";
        public static final String ACCESS_INSTANT_APPS = "android.permission.ACCESS_INSTANT_APPS";
        public static final String ACCESS_KEYGUARD_SECURE_STORAGE =
                "android.permission.ACCESS_KEYGUARD_SECURE_STORAGE";
        public static final String ACCESS_LOWPAN_STATE = "android.permission.ACCESS_LOWPAN_STATE";
        public static final String ACCESS_MOCK_LOCATION = "android.permission.ACCESS_MOCK_LOCATION";
        public static final String ACCESS_MTP = "android.permission.ACCESS_MTP";
        public static final String ACCESS_NETWORK_CONDITIONS =
                "android.permission.ACCESS_NETWORK_CONDITIONS";
        public static final String ACCESS_NOTIFICATIONS = "android.permission.ACCESS_NOTIFICATIONS";
        public static final String ACCESS_PDB_STATE = "android.permission.ACCESS_PDB_STATE";
        public static final String ACCESS_SHORTCUTS = "android.permission.ACCESS_SHORTCUTS";
        public static final String ACCESS_SURFACE_FLINGER =
                "android.permission.ACCESS_SURFACE_FLINGER";
        public static final String ACCESS_UCE_OPTIONS_SERVICE =
                "android.permission.ACCESS_UCE_OPTIONS_SERVICE";
        public static final String ACCESS_UCE_PRESENCE_SERVICE =
                "android.permission.ACCESS_UCE_PRESENCE_SERVICE";
        public static final String ACCESS_VOICE_INTERACTION_SERVICE =
                "android.permission.ACCESS_VOICE_INTERACTION_SERVICE";
        public static final String ACCESS_VR_MANAGER = "android.permission.ACCESS_VR_MANAGER";
        public static final String ACCESS_VR_STATE = "android.permission.ACCESS_VR_STATE";
        public static final String ACCOUNT_MANAGER = "android.permission.ACCOUNT_MANAGER";
        public static final String ACTIVITY_EMBEDDING = "android.permission.ACTIVITY_EMBEDDING";
        public static final String ALLOCATE_AGGRESSIVE = "android.permission.ALLOCATE_AGGRESSIVE";
        public static final String ALLOW_ANY_CODEC_FOR_PLAYBACK =
                "android.permission.ALLOW_ANY_CODEC_FOR_PLAYBACK";
        public static final String ASEC_ACCESS = "android.permission.ASEC_ACCESS";
        public static final String ASEC_CREATE = "android.permission.ASEC_CREATE";
        public static final String ASEC_DESTROY = "android.permission.ASEC_DESTROY";
        public static final String ASEC_MOUNT_UNMOUNT = "android.permission.ASEC_MOUNT_UNMOUNT";
        public static final String ASEC_RENAME = "android.permission.ASEC_RENAME";
        public static final String BACKUP = "android.permission.BACKUP";
        public static final String BATTERY_STATS = "android.permission.BATTERY_STATS";
        public static final String BIND_ACCESSIBILITY_SERVICE =
                "android.permission.BIND_ACCESSIBILITY_SERVICE";
        public static final String BIND_APPWIDGET = "android.permission.BIND_APPWIDGET";
        public static final String BIND_AUTOFILL = "android.permission.BIND_AUTOFILL";
        public static final String BIND_AUTOFILL_FIELD_CLASSIFICATION_SERVICE =
                "android.permission.BIND_AUTOFILL_FIELD_CLASSIFICATION_SERVICE";
        public static final String BIND_AUTOFILL_SERVICE =
                "android.permission.BIND_AUTOFILL_SERVICE";
        public static final String BIND_CACHE_QUOTA_SERVICE =
                "android.permission.BIND_CACHE_QUOTA_SERVICE";
        public static final String BIND_CARRIER_MESSAGING_SERVICE =
                "android.permission.BIND_CARRIER_MESSAGING_SERVICE";
        public static final String BIND_CARRIER_SERVICES =
                "android.permission.BIND_CARRIER_SERVICES";
        public static final String BIND_CHOOSER_TARGET_SERVICE =
                "android.permission.BIND_CHOOSER_TARGET_SERVICE";
        public static final String BIND_COMPANION_DEVICE_MANAGER_SERVICE =
                "android.permission.BIND_COMPANION_DEVICE_MANAGER_SERVICE";
        public static final String BIND_CONDITION_PROVIDER_SERVICE =
                "android.permission.BIND_CONDITION_PROVIDER_SERVICE";
        public static final String BIND_CONNECTION_SERVICE =
                "android.permission.BIND_CONNECTION_SERVICE";
        public static final String BIND_DEVICE_ADMIN = "android.permission.BIND_DEVICE_ADMIN";
        public static final String BIND_DIRECTORY_SEARCH =
                "android.permission.BIND_DIRECTORY_SEARCH";
        public static final String BIND_DREAM_SERVICE = "android.permission.BIND_DREAM_SERVICE";
        public static final String BIND_EUICC_SERVICE = "android.permission.BIND_EUICC_SERVICE";
        public static final String BIND_IMS_SERVICE = "android.permission.BIND_IMS_SERVICE";
        public static final String BIND_INCALL_SERVICE = "android.permission.BIND_INCALL_SERVICE";
        public static final String BIND_INPUT_METHOD = "android.permission.BIND_INPUT_METHOD";
        public static final String BIND_INTENT_FILTER_VERIFIER =
                "android.permission.BIND_INTENT_FILTER_VERIFIER";
        public static final String BIND_JOB_SERVICE = "android.permission.BIND_JOB_SERVICE";
        public static final String BIND_KEYGUARD_APPWIDGET =
                "android.permission.BIND_KEYGUARD_APPWIDGET";
        public static final String BIND_MIDI_DEVICE_SERVICE =
                "android.permission.BIND_MIDI_DEVICE_SERVICE";
        public static final String BIND_NETWORK_RECOMMENDATION_SERVICE =
                "android.permission.BIND_NETWORK_RECOMMENDATION_SERVICE";
        public static final String BIND_NFC_SERVICE = "android.permission.BIND_NFC_SERVICE";
        public static final String BIND_NOTIFICATION_ASSISTANT_SERVICE =
                "android.permission.BIND_NOTIFICATION_ASSISTANT_SERVICE";
        public static final String BIND_NOTIFICATION_LISTENER_SERVICE =
                "android.permission.BIND_NOTIFICATION_LISTENER_SERVICE";
        public static final String BIND_PACKAGE_VERIFIER =
                "android.permission.BIND_PACKAGE_VERIFIER";
        public static final String BIND_PRINT_RECOMMENDATION_SERVICE =
                "android.permission.BIND_PRINT_RECOMMENDATION_SERVICE";
        public static final String BIND_PRINT_SERVICE = "android.permission.BIND_PRINT_SERVICE";
        public static final String BIND_PRINT_SPOOLER_SERVICE =
                "android.permission.BIND_PRINT_SPOOLER_SERVICE";
        public static final String BIND_QUICK_SETTINGS_TILE =
                "android.permission.BIND_QUICK_SETTINGS_TILE";
        public static final String BIND_REMOTE_DISPLAY = "android.permission.BIND_REMOTE_DISPLAY";
        public static final String BIND_REMOTEVIEWS = "android.permission.BIND_REMOTEVIEWS";
        public static final String BIND_RESOLVER_RANKER_SERVICE =
                "android.permission.BIND_RESOLVER_RANKER_SERVICE";
        public static final String BIND_ROUTE_PROVIDER = "android.permission.BIND_ROUTE_PROVIDER";
        public static final String BIND_RUNTIME_PERMISSION_PRESENTER_SERVICE =
                "android.permission.BIND_RUNTIME_PERMISSION_PRESENTER_SERVICE";
        public static final String BIND_SCREENING_SERVICE =
                "android.permission.BIND_SCREENING_SERVICE";
        public static final String BIND_SETTINGS_SUGGESTIONS_SERVICE =
                "android.permission.BIND_SETTINGS_SUGGESTIONS_SERVICE";
        public static final String BIND_SOUND_TRIGGER_DETECTION_SERVICE =
                "android.permission.BIND_SOUND_TRIGGER_DETECTION_SERVICE";
        public static final String BIND_TELECOM_CONNECTION_SERVICE =
                "android.permission.BIND_TELECOM_CONNECTION_SERVICE";
        public static final String BIND_TELEPHONY_DATA_SERVICE =
                "android.permission.BIND_TELEPHONY_DATA_SERVICE";
        public static final String BIND_TELEPHONY_NETWORK_SERVICE =
                "android.permission.BIND_TELEPHONY_NETWORK_SERVICE";
        public static final String BIND_TEXTCLASSIFIER_SERVICE =
                "android.permission.BIND_TEXTCLASSIFIER_SERVICE";
        public static final String BIND_TEXT_SERVICE = "android.permission.BIND_TEXT_SERVICE";
        public static final String BIND_TRUST_AGENT = "android.permission.BIND_TRUST_AGENT";
        public static final String BIND_TV_INPUT = "android.permission.BIND_TV_INPUT";
        public static final String BIND_TV_REMOTE_SERVICE =
                "android.permission.BIND_TV_REMOTE_SERVICE";
        public static final String BIND_VISUAL_VOICEMAIL_SERVICE =
                "android.permission.BIND_VISUAL_VOICEMAIL_SERVICE";
        public static final String BIND_VOICE_INTERACTION =
                "android.permission.BIND_VOICE_INTERACTION";
        public static final String BIND_VPN_SERVICE = "android.permission.BIND_VPN_SERVICE";
        public static final String BIND_VR_LISTENER_SERVICE =
                "android.permission.BIND_VR_LISTENER_SERVICE";
        public static final String BIND_WALLPAPER = "android.permission.BIND_WALLPAPER";
        public static final String BLUETOOTH_MAP = "android.permission.BLUETOOTH_MAP";
        public static final String BLUETOOTH_PRIVILEGED = "android.permission.BLUETOOTH_PRIVILEGED";
        public static final String BLUETOOTH_STACK = "android.permission.BLUETOOTH_STACK";
        public static final String BRICK = "android.permission.BRICK";
        public static final String BRIGHTNESS_SLIDER_USAGE =
                "android.permission.BRIGHTNESS_SLIDER_USAGE";
        public static final String BROADCAST_NETWORK_PRIVILEGED =
                "android.permission.BROADCAST_NETWORK_PRIVILEGED";
        public static final String BROADCAST_PACKAGE_REMOVED =
                "android.permission.BROADCAST_PACKAGE_REMOVED";
        public static final String BROADCAST_SMS = "android.permission.BROADCAST_SMS";
        public static final String BROADCAST_WAP_PUSH = "android.permission.BROADCAST_WAP_PUSH";
        public static final String C2D_MESSAGE =
                "android.intent.category.MASTER_CLEAR.permission.C2D_MESSAGE";
        public static final String CACHE_CONTENT = "android.permission.CACHE_CONTENT";
        public static final String CALL_PRIVILEGED = "android.permission.CALL_PRIVILEGED";
        public static final String CAMERA_DISABLE_TRANSMIT_LED =
                "android.permission.CAMERA_DISABLE_TRANSMIT_LED";
        public static final String CAMERA_SEND_SYSTEM_EVENTS =
                "android.permission.CAMERA_SEND_SYSTEM_EVENTS";
        public static final String CAPTURE_AUDIO_HOTWORD =
                "android.permission.CAPTURE_AUDIO_HOTWORD";
        public static final String CAPTURE_AUDIO_OUTPUT = "android.permission.CAPTURE_AUDIO_OUTPUT";
        public static final String CAPTURE_SECURE_VIDEO_OUTPUT =
                "android.permission.CAPTURE_SECURE_VIDEO_OUTPUT";
        public static final String CAPTURE_TV_INPUT = "android.permission.CAPTURE_TV_INPUT";
        public static final String CAPTURE_VIDEO_OUTPUT = "android.permission.CAPTURE_VIDEO_OUTPUT";
        public static final String CARRIER_FILTER_SMS = "android.permission.CARRIER_FILTER_SMS";
        public static final String CHANGE_ACCESSIBILITY_VOLUME =
                "android.permission.CHANGE_ACCESSIBILITY_VOLUME";
        public static final String CHANGE_APP_IDLE_STATE =
                "android.permission.CHANGE_APP_IDLE_STATE";
        public static final String CHANGE_BACKGROUND_DATA_SETTING =
                "android.permission.CHANGE_BACKGROUND_DATA_SETTING";
        public static final String CHANGE_COMPONENT_ENABLED_STATE =
                "android.permission.CHANGE_COMPONENT_ENABLED_STATE";
        public static final String CHANGE_CONFIGURATION = "android.permission.CHANGE_CONFIGURATION";
        public static final String CHANGE_DEVICE_IDLE_TEMP_WHITELIST =
                "android.permission.CHANGE_DEVICE_IDLE_TEMP_WHITELIST";
        public static final String CHANGE_HDMI_CEC_ACTIVE_SOURCE =
                "android.permission.CHANGE_HDMI_CEC_ACTIVE_SOURCE";
        public static final String CHANGE_LOWPAN_STATE = "android.permission.CHANGE_LOWPAN_STATE";
        public static final String CHANGE_OVERLAY_PACKAGES =
                "android.permission.CHANGE_OVERLAY_PACKAGES";
        public static final String CLEAR_APP_CACHE = "android.permission.CLEAR_APP_CACHE";
        public static final String CLEAR_APP_GRANTED_URI_PERMISSIONS =
                "android.permission.CLEAR_APP_GRANTED_URI_PERMISSIONS";
        public static final String CLEAR_APP_USER_DATA = "android.permission.CLEAR_APP_USER_DATA";
        public static final String CONFIGURE_DISPLAY_BRIGHTNESS =
                "android.permission.CONFIGURE_DISPLAY_BRIGHTNESS";
        public static final String CONFIGURE_DISPLAY_COLOR_MODE =
                "android.permission.CONFIGURE_DISPLAY_COLOR_MODE";
        public static final String CONFIGURE_WIFI_DISPLAY =
                "android.permission.CONFIGURE_WIFI_DISPLAY";
        public static final String CONFIRM_FULL_BACKUP = "android.permission.CONFIRM_FULL_BACKUP";
        public static final String CONNECTIVITY_INTERNAL =
                "android.permission.CONNECTIVITY_INTERNAL";
        public static final String CONNECTIVITY_USE_RESTRICTED_NETWORKS =
                "android.permission.CONNECTIVITY_USE_RESTRICTED_NETWORKS";
        public static final String CONTROL_DISPLAY_BRIGHTNESS =
                "android.permission.CONTROL_DISPLAY_BRIGHTNESS";
        public static final String CONTROL_DISPLAY_SATURATION =
                "android.permission.CONTROL_DISPLAY_SATURATION";
        public static final String CONTROL_INCALL_EXPERIENCE =
                "android.permission.CONTROL_INCALL_EXPERIENCE";
        public static final String CONTROL_KEYGUARD = "android.permission.CONTROL_KEYGUARD";
        public static final String CONTROL_LOCATION_UPDATES =
                "android.permission.CONTROL_LOCATION_UPDATES";
        public static final String CONTROL_REMOTE_APP_TRANSITION_ANIMATIONS =
                "android.permission.CONTROL_REMOTE_APP_TRANSITION_ANIMATIONS";
        public static final String CONTROL_VPN = "android.permission.CONTROL_VPN";
        public static final String CONTROL_WIFI_DISPLAY = "android.permission.CONTROL_WIFI_DISPLAY";
        public static final String COPY_PROTECTED_DATA = "android.permission.COPY_PROTECTED_DATA";
        public static final String CREATE_USERS = "android.permission.CREATE_USERS";
        public static final String CRYPT_KEEPER = "android.permission.CRYPT_KEEPER";
        public static final String DELETE_CACHE_FILES = "android.permission.DELETE_CACHE_FILES";
        public static final String DELETE_PACKAGES = "android.permission.DELETE_PACKAGES";
        public static final String DEVICE_POWER = "android.permission.DEVICE_POWER";
        public static final String DIAGNOSTIC = "android.permission.DIAGNOSTIC";
        public static final String DISABLE_HIDDEN_API_CHECKS =
                "android.permission.DISABLE_HIDDEN_API_CHECKS";
        public static final String DISABLE_INPUT_DEVICE = "android.permission.DISABLE_INPUT_DEVICE";
        public static final String DISPATCH_NFC_MESSAGE = "android.permission.DISPATCH_NFC_MESSAGE";
        public static final String DISPATCH_PROVISIONING_MESSAGE =
                "android.permission.DISPATCH_PROVISIONING_MESSAGE";
        public static final String DUMP = "android.permission.DUMP";
        public static final String DVB_DEVICE = "android.permission.DVB_DEVICE";
        public static final String FACTORY_TEST = "android.permission.FACTORY_TEST";
        public static final String FILTER_EVENTS = "android.permission.FILTER_EVENTS";
        public static final String FORCE_BACK = "android.permission.FORCE_BACK";
        public static final String FORCE_PERSISTABLE_URI_PERMISSIONS =
                "android.permission.FORCE_PERSISTABLE_URI_PERMISSIONS";
        public static final String FORCE_STOP_PACKAGES = "android.permission.FORCE_STOP_PACKAGES";
        public static final String FRAME_STATS = "android.permission.FRAME_STATS";
        public static final String FREEZE_SCREEN = "android.permission.FREEZE_SCREEN";
        public static final String GET_ACCOUNTS_PRIVILEGED =
                "android.permission.GET_ACCOUNTS_PRIVILEGED";
        public static final String GET_APP_GRANTED_URI_PERMISSIONS =
                "android.permission.GET_APP_GRANTED_URI_PERMISSIONS";
        public static final String GET_APP_OPS_STATS = "android.permission.GET_APP_OPS_STATS";
        public static final String GET_DETAILED_TASKS = "android.permission.GET_DETAILED_TASKS";
        public static final String GET_INTENT_SENDER_INTENT =
                "android.permission.GET_INTENT_SENDER_INTENT";
        public static final String GET_PASSWORD = "android.permission.GET_PASSWORD";
        public static final String GET_PROCESS_STATE_AND_OOM_SCORE =
                "android.permission.GET_PROCESS_STATE_AND_OOM_SCORE";
        public static final String GET_TOP_ACTIVITY_INFO =
                "android.permission.GET_TOP_ACTIVITY_INFO";
        public static final String GLOBAL_SEARCH = "android.permission.GLOBAL_SEARCH";
        public static final String GLOBAL_SEARCH_CONTROL =
                "android.permission.GLOBAL_SEARCH_CONTROL";
        public static final String GRANT_RUNTIME_PERMISSIONS =
                "android.permission.GRANT_RUNTIME_PERMISSIONS";
        public static final String HARDWARE_TEST = "android.permission.HARDWARE_TEST";
        public static final String HDMI_CEC = "android.permission.HDMI_CEC";
        public static final String HIDE_NON_SYSTEM_OVERLAY_WINDOWS =
                "android.permission.HIDE_NON_SYSTEM_OVERLAY_WINDOWS";
        public static final String INJECT_EVENTS = "android.permission.INJECT_EVENTS";
        public static final String INSTALL_EXISTING_PACKAGES =
                "com.android.permission.INSTALL_EXISTING_PACKAGES";
        public static final String INSTALL_GRANT_RUNTIME_PERMISSIONS =
                "android.permission.INSTALL_GRANT_RUNTIME_PERMISSIONS";
        public static final String INSTALL_LOCATION_PROVIDER =
                "android.permission.INSTALL_LOCATION_PROVIDER";
        public static final String INSTALL_PACKAGES = "android.permission.INSTALL_PACKAGES";
        public static final String INSTALL_PACKAGE_UPDATES =
                "android.permission.INSTALL_PACKAGE_UPDATES";
        public static final String INSTALL_SELF_UPDATES = "android.permission.INSTALL_SELF_UPDATES";
        public static final String INSTANT_APP_FOREGROUND_SERVICE =
                "android.permission.INSTANT_APP_FOREGROUND_SERVICE";
        public static final String INTENT_FILTER_VERIFICATION_AGENT =
                "android.permission.INTENT_FILTER_VERIFICATION_AGENT";
        public static final String INTERACT_ACROSS_USERS =
                "android.permission.INTERACT_ACROSS_USERS";
        public static final String INTERACT_ACROSS_USERS_FULL =
                "android.permission.INTERACT_ACROSS_USERS_FULL";
        public static final String INTERNAL_DELETE_CACHE_FILES =
                "android.permission.INTERNAL_DELETE_CACHE_FILES";
        public static final String INTERNAL_SYSTEM_WINDOW =
                "android.permission.INTERNAL_SYSTEM_WINDOW";
        public static final String INVOKE_CARRIER_SETUP = "android.permission.INVOKE_CARRIER_SETUP";
        public static final String KILL_UID = "android.permission.KILL_UID";
        public static final String LAUNCH_TRUST_AGENT_SETTINGS =
                "android.permission.LAUNCH_TRUST_AGENT_SETTINGS";
        public static final String LOCAL_MAC_ADDRESS = "android.permission.LOCAL_MAC_ADDRESS";
        public static final String LOCATION_HARDWARE = "android.permission.LOCATION_HARDWARE";
        public static final String LOOP_RADIO = "android.permission.LOOP_RADIO";
        public static final String MANAGE_ACTIVITY_STACKS =
                "android.permission.MANAGE_ACTIVITY_STACKS";
        public static final String MANAGE_APP_OPS_MODES = "android.permission.MANAGE_APP_OPS_MODES";
        public static final String MANAGE_APP_OPS_RESTRICTIONS =
                "android.permission.MANAGE_APP_OPS_RESTRICTIONS";
        public static final String MANAGE_APP_TOKENS = "android.permission.MANAGE_APP_TOKENS";
        public static final String MANAGE_AUDIO_POLICY = "android.permission.MANAGE_AUDIO_POLICY";
        public static final String MANAGE_AUTO_FILL = "android.permission.MANAGE_AUTO_FILL";
        public static final String MANAGE_BIND_INSTANT_SERVICE =
                "android.permission.MANAGE_BIND_INSTANT_SERVICE";
        public static final String MANAGE_BLUETOOTH_WHEN_PERMISSION_REVIEW_REQUIRED =
                "android.permission.MANAGE_BLUETOOTH_WHEN_PERMISSION_REVIEW_REQUIRED";
        public static final String MANAGE_CA_CERTIFICATES =
                "android.permission.MANAGE_CA_CERTIFICATES";
        public static final String MANAGE_CAMERA = "android.permission.MANAGE_CAMERA";
        public static final String MANAGE_CARRIER_OEM_UNLOCK_STATE =
                "android.permission.MANAGE_CARRIER_OEM_UNLOCK_STATE";
        public static final String MANAGE_DEVICE_ADMINS = "android.permission.MANAGE_DEVICE_ADMINS";
        public static final String MANAGE_DOCUMENTS = "android.permission.MANAGE_DOCUMENTS";
        public static final String MANAGE_FINGERPRINT = "android.permission.MANAGE_FINGERPRINT";
        public static final String MANAGE_IPSEC_TUNNELS = "android.permission.MANAGE_IPSEC_TUNNELS";
        public static final String MANAGE_LOWPAN_INTERFACES =
                "android.permission.MANAGE_LOWPAN_INTERFACES";
        public static final String MANAGE_MEDIA_PROJECTION =
                "android.permission.MANAGE_MEDIA_PROJECTION";
        public static final String MANAGE_NETWORK_POLICY =
                "android.permission.MANAGE_NETWORK_POLICY";
        public static final String MANAGE_NOTIFICATIONS = "android.permission.MANAGE_NOTIFICATIONS";
        public static final String MANAGE_PROFILE_AND_DEVICE_OWNERS =
                "android.permission.MANAGE_PROFILE_AND_DEVICE_OWNERS";
        public static final String MANAGE_SCOPED_ACCESS_DIRECTORY_PERMISSIONS =
                "android.permission.MANAGE_SCOPED_ACCESS_DIRECTORY_PERMISSIONS";
        public static final String MANAGE_SENSORS = "android.permission.MANAGE_SENSORS";
        public static final String MANAGE_SLICE_PERMISSIONS =
                "android.permission.MANAGE_SLICE_PERMISSIONS";
        public static final String MANAGE_SOUND_TRIGGER = "android.permission.MANAGE_SOUND_TRIGGER";
        public static final String MANAGE_SUBSCRIPTION_PLANS =
                "android.permission.MANAGE_SUBSCRIPTION_PLANS";
        public static final String MANAGE_USB = "android.permission.MANAGE_USB";
        public static final String MANAGE_USER_OEM_UNLOCK_STATE =
                "android.permission.MANAGE_USER_OEM_UNLOCK_STATE";
        public static final String MANAGE_USERS = "android.permission.MANAGE_USERS";
        public static final String MANAGE_VOICE_KEYPHRASES =
                "android.permission.MANAGE_VOICE_KEYPHRASES";
        public static final String MANAGE_WIFI_WHEN_PERMISSION_REVIEW_REQUIRED =
                "android.permission.MANAGE_WIFI_WHEN_PERMISSION_REVIEW_REQUIRED";
        public static final String MASTER_CLEAR = "android.permission.MASTER_CLEAR";
        public static final String MEDIA_CONTENT_CONTROL =
                "android.permission.MEDIA_CONTENT_CONTROL";
        public static final String MODIFY_ACCESSIBILITY_DATA =
                "android.permission.MODIFY_ACCESSIBILITY_DATA";
        public static final String MODIFY_APPWIDGET_BIND_PERMISSIONS =
                "android.permission.MODIFY_APPWIDGET_BIND_PERMISSIONS";
        public static final String MODIFY_AUDIO_ROUTING = "android.permission.MODIFY_AUDIO_ROUTING";
        public static final String MODIFY_CELL_BROADCASTS =
                "android.permission.MODIFY_CELL_BROADCASTS";
        public static final String MODIFY_DAY_NIGHT_MODE =
                "android.permission.MODIFY_DAY_NIGHT_MODE";
        public static final String MODIFY_NETWORK_ACCOUNTING =
                "android.permission.MODIFY_NETWORK_ACCOUNTING";
        public static final String MODIFY_PARENTAL_CONTROLS =
                "android.permission.MODIFY_PARENTAL_CONTROLS";
        public static final String MODIFY_PHONE_STATE = "android.permission.MODIFY_PHONE_STATE";
        public static final String MODIFY_QUIET_MODE = "android.permission.MODIFY_QUIET_MODE";
        public static final String MODIFY_THEME_OVERLAY = "android.permission.MODIFY_THEME_OVERLAY";
        public static final String MOUNT_FORMAT_FILESYSTEMS =
                "android.permission.MOUNT_FORMAT_FILESYSTEMS";
        public static final String MOUNT_UNMOUNT_FILESYSTEMS =
                "android.permission.MOUNT_UNMOUNT_FILESYSTEMS";
        public static final String MOVE_PACKAGE = "android.permission.MOVE_PACKAGE";
        public static final String NET_ADMIN = "android.permission.NET_ADMIN";
        public static final String NET_TUNNELING = "android.permission.NET_TUNNELING";
        public static final String NETWORK_BYPASS_PRIVATE_DNS =
                "android.permission.NETWORK_BYPASS_PRIVATE_DNS";
        public static final String NETWORK_SETTINGS = "android.permission.NETWORK_SETTINGS";
        public static final String NETWORK_SETUP_WIZARD = "android.permission.NETWORK_SETUP_WIZARD";
        public static final String NETWORK_STACK = "android.permission.NETWORK_STACK";
        public static final String NFC_HANDOVER_STATUS = "android.permission.NFC_HANDOVER_STATUS";
        public static final String NOTIFICATION_DURING_SETUP =
                "android.permission.NOTIFICATION_DURING_SETUP";
        public static final String NOTIFY_PENDING_SYSTEM_UPDATE =
                "android.permission.NOTIFY_PENDING_SYSTEM_UPDATE";
        public static final String NOTIFY_TV_INPUTS = "android.permission.NOTIFY_TV_INPUTS";
        public static final String OBSERVE_APP_USAGE = "android.permission.OBSERVE_APP_USAGE";
        public static final String OBSERVE_GRANT_REVOKE_PERMISSIONS =
                "android.permission.OBSERVE_GRANT_REVOKE_PERMISSIONS";
        public static final String OEM_UNLOCK_STATE = "android.permission.OEM_UNLOCK_STATE";
        public static final String OPEN_APPLICATION_DETAILS_OPEN_BY_DEFAULT_PAGE =
                "android.permission.OPEN_APPLICATION_DETAILS_OPEN_BY_DEFAULT_PAGE";
        public static final String OVERRIDE_WIFI_CONFIG = "android.permission.OVERRIDE_WIFI_CONFIG";
        public static final String PACKAGE_USAGE_STATS = "android.permission.PACKAGE_USAGE_STATS";
        public static final String PACKAGE_VERIFICATION_AGENT =
                "android.permission.PACKAGE_VERIFICATION_AGENT";
        public static final String PACKET_KEEPALIVE_OFFLOAD =
                "android.permission.PACKET_KEEPALIVE_OFFLOAD";
        public static final String PEERS_MAC_ADDRESS = "android.permission.PEERS_MAC_ADDRESS";
        public static final String PERFORM_CDMA_PROVISIONING =
                "android.permission.PERFORM_CDMA_PROVISIONING";
        public static final String PERFORM_SIM_ACTIVATION =
                "android.permission.PERFORM_SIM_ACTIVATION";
        public static final String PROVIDE_RESOLVER_RANKER_SERVICE =
                "android.permission.PROVIDE_RESOLVER_RANKER_SERVICE";
        public static final String PROVIDE_TRUST_AGENT = "android.permission.PROVIDE_TRUST_AGENT";
        public static final String QUERY_DO_NOT_ASK_CREDENTIALS_ON_BOOT =
                "android.permission.QUERY_DO_NOT_ASK_CREDENTIALS_ON_BOOT";
        public static final String QUERY_TIME_ZONE_RULES =
                "android.permission.QUERY_TIME_ZONE_RULES";
        public static final String READ_BLOCKED_NUMBERS = "android.permission.READ_BLOCKED_NUMBERS";
        public static final String READ_CONTENT_RATING_SYSTEMS =
                "android.permission.READ_CONTENT_RATING_SYSTEMS";
        public static final String READ_DREAM_STATE = "android.permission.READ_DREAM_STATE";
        public static final String READ_FRAME_BUFFER = "android.permission.READ_FRAME_BUFFER";
        public static final String READ_INPUT_STATE = "android.permission.READ_INPUT_STATE";
        public static final String READ_LOGS = "android.permission.READ_LOGS";
        public static final String READ_LOWPAN_CREDENTIAL =
                "android.permission.READ_LOWPAN_CREDENTIAL";
        public static final String READ_NETWORK_USAGE_HISTORY =
                "android.permission.READ_NETWORK_USAGE_HISTORY";
        public static final String READ_OEM_UNLOCK_STATE =
                "android.permission.READ_OEM_UNLOCK_STATE";
        public static final String READ_PRECISE_PHONE_STATE =
                "android.permission.READ_PRECISE_PHONE_STATE";
        public static final String READ_PRINT_SERVICE_RECOMMENDATIONS =
                "android.permission.READ_PRINT_SERVICE_RECOMMENDATIONS";
        public static final String READ_PRINT_SERVICES = "android.permission.READ_PRINT_SERVICES";
        public static final String READ_PRIVILEGED_PHONE_STATE =
                "android.permission.READ_PRIVILEGED_PHONE_STATE";
        public static final String READ_RUNTIME_PROFILES =
                "android.permission.READ_RUNTIME_PROFILES";
        public static final String READ_SEARCH_INDEXABLES =
                "android.permission.READ_SEARCH_INDEXABLES";
        public static final String READ_SYSTEM_UPDATE_INFO =
                "android.permission.READ_SYSTEM_UPDATE_INFO";
        public static final String READ_VOICEMAIL =
                "com.android.voicemail.permission.READ_VOICEMAIL";
        public static final String READ_WALLPAPER_INTERNAL =
                "android.permission.READ_WALLPAPER_INTERNAL";
        public static final String READ_WIFI_CREDENTIAL = "android.permission.READ_WIFI_CREDENTIAL";
        public static final String REAL_GET_TASKS = "android.permission.REAL_GET_TASKS";
        public static final String REBOOT = "android.permission.REBOOT";
        public static final String RECEIVE_BLUETOOTH_MAP =
                "android.permission.RECEIVE_BLUETOOTH_MAP";
        public static final String RECEIVE_DATA_ACTIVITY_CHANGE =
                "android.permission.RECEIVE_DATA_ACTIVITY_CHANGE";
        public static final String RECEIVE_EMERGENCY_BROADCAST =
                "android.permission.RECEIVE_EMERGENCY_BROADCAST";
        public static final String RECEIVE_MEDIA_RESOURCE_USAGE =
                "android.permission.RECEIVE_MEDIA_RESOURCE_USAGE";
        public static final String RECEIVE_STK_COMMANDS = "android.permission.RECEIVE_STK_COMMANDS";
        public static final String RECEIVE_WIFI_CREDENTIAL_CHANGE =
                "android.permission.RECEIVE_WIFI_CREDENTIAL_CHANGE";
        public static final String RECOVER_KEYSTORE = "android.permission.RECOVER_KEYSTORE";
        public static final String RECOVERY = "android.permission.RECOVERY";
        public static final String REGISTER_CALL_PROVIDER =
                "android.permission.REGISTER_CALL_PROVIDER";
        public static final String REGISTER_CONNECTION_MANAGER =
                "android.permission.REGISTER_CONNECTION_MANAGER";
        public static final String REGISTER_SIM_SUBSCRIPTION =
                "android.permission.REGISTER_SIM_SUBSCRIPTION";
        public static final String REGISTER_WINDOW_MANAGER_LISTENERS =
                "android.permission.REGISTER_WINDOW_MANAGER_LISTENERS";
        public static final String REMOTE_AUDIO_PLAYBACK =
                "android.permission.REMOTE_AUDIO_PLAYBACK";
        public static final String REMOVE_DRM_CERTIFICATES =
                "android.permission.REMOVE_DRM_CERTIFICATES";
        public static final String REMOVE_TASKS = "android.permission.REMOVE_TASKS";
        public static final String REQUEST_INSTALL_PACKAGES =
                "android.permission.REQUEST_INSTALL_PACKAGES";
        public static final String REQUEST_NETWORK_SCORES =
                "android.permission.REQUEST_NETWORK_SCORES";
        public static final String RESET_FINGERPRINT_LOCKOUT =
                "android.permission.RESET_FINGERPRINT_LOCKOUT";
        public static final String RESET_SHORTCUT_MANAGER_THROTTLING =
                "android.permission.RESET_SHORTCUT_MANAGER_THROTTLING";
        public static final String RESTRICTED_VR_ACCESS = "android.permission.RESTRICTED_VR_ACCESS";
        public static final String RETRIEVE_WINDOW_CONTENT =
                "android.permission.RETRIEVE_WINDOW_CONTENT";
        public static final String RETRIEVE_WINDOW_TOKEN =
                "android.permission.RETRIEVE_WINDOW_TOKEN";
        public static final String REVOKE_RUNTIME_PERMISSIONS =
                "android.permission.REVOKE_RUNTIME_PERMISSIONS";
        public static final String RUN_IN_BACKGROUND = "android.permission.RUN_IN_BACKGROUND";
        public static final String SCORE_NETWORKS = "android.permission.SCORE_NETWORKS";
        public static final String SEND_EMBMS_INTENTS = "android.permission.SEND_EMBMS_INTENTS";
        public static final String SEND_RESPOND_VIA_MESSAGE =
                "android.permission.SEND_RESPOND_VIA_MESSAGE";
        public static final String SEND_SHOW_SUSPENDED_APP_DETAILS =
                "android.permission.SEND_SHOW_SUSPENDED_APP_DETAILS";
        public static final String SEND_SMS_NO_CONFIRMATION =
                "android.permission.SEND_SMS_NO_CONFIRMATION";
        public static final String SERIAL_PORT = "android.permission.SERIAL_PORT";
        public static final String SET_ACTIVITY_WATCHER = "android.permission.SET_ACTIVITY_WATCHER";
        public static final String SET_ALWAYS_FINISH = "android.permission.SET_ALWAYS_FINISH";
        public static final String SET_ANIMATION_SCALE = "android.permission.SET_ANIMATION_SCALE";
        public static final String SET_DEBUG_APP = "android.permission.SET_DEBUG_APP";
        public static final String SET_DISPLAY_OFFSET = "android.permission.SET_DISPLAY_OFFSET";
        public static final String SET_HARMFUL_APP_WARNINGS =
                "android.permission.SET_HARMFUL_APP_WARNINGS";
        public static final String SET_INPUT_CALIBRATION =
                "android.permission.SET_INPUT_CALIBRATION";
        public static final String SET_KEYBOARD_LAYOUT = "android.permission.SET_KEYBOARD_LAYOUT";
        public static final String SET_MEDIA_KEY_LISTENER =
                "android.permission.SET_MEDIA_KEY_LISTENER";
        public static final String SET_ORIENTATION = "android.permission.SET_ORIENTATION";
        public static final String SET_POINTER_SPEED = "android.permission.SET_POINTER_SPEED";
        public static final String SET_PREFERRED_APPLICATIONS =
                "android.permission.SET_PREFERRED_APPLICATIONS";
        public static final String SET_PROCESS_LIMIT = "android.permission.SET_PROCESS_LIMIT";
        public static final String SET_SCREEN_COMPATIBILITY =
                "android.permission.SET_SCREEN_COMPATIBILITY";
        public static final String SET_TIME = "android.permission.SET_TIME";
        public static final String SET_TIME_ZONE = "android.permission.SET_TIME_ZONE";
        public static final String SET_VOLUME_KEY_LONG_PRESS_LISTENER =
                "android.permission.SET_VOLUME_KEY_LONG_PRESS_LISTENER";
        public static final String SET_WALLPAPER_COMPONENT =
                "android.permission.SET_WALLPAPER_COMPONENT";
        public static final String SHOW_KEYGUARD_MESSAGE =
                "android.permission.SHOW_KEYGUARD_MESSAGE";
        public static final String SHUTDOWN = "android.permission.SHUTDOWN";
        public static final String SIGNAL_PERSISTENT_PROCESSES =
                "android.permission.SIGNAL_PERSISTENT_PROCESSES";
        public static final String START_ANY_ACTIVITY = "android.permission.START_ANY_ACTIVITY";
        public static final String START_TASKS_FROM_RECENTS =
                "android.permission.START_TASKS_FROM_RECENTS";
        public static final String STATSCOMPANION = "android.permission.STATSCOMPANION";
        public static final String STATUS_BAR = "android.permission.STATUS_BAR";
        public static final String STATUS_BAR_SERVICE = "android.permission.STATUS_BAR_SERVICE";
        public static final String STOP_APP_SWITCHES = "android.permission.STOP_APP_SWITCHES";
        public static final String STORAGE_INTERNAL = "android.permission.STORAGE_INTERNAL";
        public static final String SUBSTITUTE_NOTIFICATION_APP_NAME =
                "android.permission.SUBSTITUTE_NOTIFICATION_APP_NAME";
        public static final String SUSPEND_APPS = "android.permission.SUSPEND_APPS";
        public static final String SYSTEM_ALERT_WINDOW = "android.permission.SYSTEM_ALERT_WINDOW";
        public static final String TABLET_MODE = "android.permission.TABLET_MODE";
        public static final String TEMPORARY_ENABLE_ACCESSIBILITY =
                "android.permission.TEMPORARY_ENABLE_ACCESSIBILITY";
        public static final String TEST_BLACKLISTED_PASSWORD =
                "android.permission.TEST_BLACKLISTED_PASSWORD";
        public static final String TETHER_PRIVILEGED = "android.permission.TETHER_PRIVILEGED";
        public static final String TRIGGER_TIME_ZONE_RULES_CHECK =
                "android.permission.TRIGGER_TIME_ZONE_RULES_CHECK";
        public static final String TRUST_LISTENER = "android.permission.TRUST_LISTENER";
        public static final String TV_INPUT_HARDWARE = "android.permission.TV_INPUT_HARDWARE";
        public static final String TV_VIRTUAL_REMOTE_CONTROLLER =
                "android.permission.TV_VIRTUAL_REMOTE_CONTROLLER";
        public static final String UNLIMITED_SHORTCUTS_API_CALLS =
                "android.permission.UNLIMITED_SHORTCUTS_API_CALLS";
        public static final String UPDATE_APP_OPS_STATS = "android.permission.UPDATE_APP_OPS_STATS";
        public static final String UPDATE_CONFIG = "android.permission.UPDATE_CONFIG";
        public static final String UPDATE_DEVICE_STATS = "android.permission.UPDATE_DEVICE_STATS";
        public static final String UPDATE_LOCK = "android.permission.UPDATE_LOCK";
        public static final String UPDATE_LOCK_TASK_PACKAGES =
                "android.permission.UPDATE_LOCK_TASK_PACKAGES";
        public static final String UPDATE_TIME_ZONE_RULES =
                "android.permission.UPDATE_TIME_ZONE_RULES";
        public static final String USE_COLORIZED_NOTIFICATIONS =
                "android.permission.USE_COLORIZED_NOTIFICATIONS";
        public static final String USE_DATA_IN_BACKGROUND =
                "android.permission.USE_DATA_IN_BACKGROUND";
        public static final String USER_ACTIVITY = "android.permission.USER_ACTIVITY";
        public static final String USE_RESERVED_DISK = "android.permission.USE_RESERVED_DISK";
        public static final String VIEW_INSTANT_APPS = "android.permission.VIEW_INSTANT_APPS";
        public static final String WATCH_APPOPS = "android.permission.WATCH_APPOPS";
        public static final String WRITE_APN_SETTINGS = "android.permission.WRITE_APN_SETTINGS";
        public static final String WRITE_BLOCKED_NUMBERS =
                "android.permission.WRITE_BLOCKED_NUMBERS";
        public static final String WRITE_DREAM_STATE = "android.permission.WRITE_DREAM_STATE";
        public static final String WRITE_EMBEDDED_SUBSCRIPTIONS =
                "android.permission.WRITE_EMBEDDED_SUBSCRIPTIONS";
        public static final String WRITE_GSERVICES = "android.permission.WRITE_GSERVICES";
        public static final String WRITE_MEDIA_STORAGE = "android.permission.WRITE_MEDIA_STORAGE";
        public static final String WRITE_SECURE_SETTINGS =
                "android.permission.WRITE_SECURE_SETTINGS";
        public static final String WRITE_SETTINGS = "android.permission.WRITE_SETTINGS";
        public static final String WRITE_VOICEMAIL =
                "com.android.voicemail.permission.WRITE_VOICEMAIL";

        // New permissions for Q
        public static final String ACCESS_SHARED_LIBRARIES =
                "android.permission.ACCESS_SHARED_LIBRARIES";
        public static final String ADJUST_RUNTIME_PERMISSIONS_POLICY =
                "android.permission.ADJUST_RUNTIME_PERMISSIONS_POLICY";
        public static final String AMBIENT_WALLPAPER = "android.permission.AMBIENT_WALLPAPER";
        public static final String APPROVE_INCIDENT_REPORTS =
                "android.permission.APPROVE_INCIDENT_REPORTS";
        public static final String BIND_ATTENTION_SERVICE =
                "android.permission.BIND_ATTENTION_SERVICE";
        public static final String BIND_AUGMENTED_AUTOFILL_SERVICE =
                "android.permission.BIND_AUGMENTED_AUTOFILL_SERVICE";
        public static final String BIND_CALL_REDIRECTION_SERVICE =
                "android.permission.BIND_CALL_REDIRECTION_SERVICE";
        public static final String BIND_CARRIER_MESSAGING_CLIENT_SERVICE =
                "android.permission.BIND_CARRIER_MESSAGING_CLIENT_SERVICE";
        public static final String BIND_CONTENT_CAPTURE_SERVICE =
                "android.permission.BIND_CONTENT_CAPTURE_SERVICE";
        public static final String BIND_CONTENT_SUGGESTIONS_SERVICE =
                "android.permission.BIND_CONTENT_SUGGESTIONS_SERVICE";
        public static final String BIND_EXPLICIT_HEALTH_CHECK_SERVICE =
                "android.permission.BIND_EXPLICIT_HEALTH_CHECK_SERVICE";
        public static final String BIND_FINANCIAL_SMS_SERVICE =
                "android.permission.BIND_FINANCIAL_SMS_SERVICE";
        public static final String BIND_PHONE_ACCOUNT_SUGGESTION_SERVICE =
                "android.permission.BIND_PHONE_ACCOUNT_SUGGESTION_SERVICE";
        public static final String CAPTURE_MEDIA_OUTPUT = "android.permission.CAPTURE_MEDIA_OUTPUT";
        public static final String CONTROL_ALWAYS_ON_VPN =
                "android.permission.CONTROL_ALWAYS_ON_VPN";
        public static final String CONTROL_DISPLAY_COLOR_TRANSFORMS =
                "android.permission.CONTROL_DISPLAY_COLOR_TRANSFORMS";
        public static final String CONTROL_KEYGUARD_SECURE_NOTIFICATIONS =
                "android.permission.CONTROL_KEYGUARD_SECURE_NOTIFICATIONS";
        public static final String ENABLE_TEST_HARNESS_MODE =
                "android.permission.ENABLE_TEST_HARNESS_MODE";
        public static final String GET_RUNTIME_PERMISSIONS =
                "android.permission.GET_RUNTIME_PERMISSIONS";
        public static final String GRANT_PROFILE_OWNER_DEVICE_IDS_ACCESS =
                "android.permission.GRANT_PROFILE_OWNER_DEVICE_IDS_ACCESS";
        public static final String INSTALL_DYNAMIC_SYSTEM =
                "android.permission.INSTALL_DYNAMIC_SYSTEM";
        public static final String INTERACT_ACROSS_PROFILES =
                "android.permission.INTERACT_ACROSS_PROFILES";
        public static final String LOCK_DEVICE = "android.permission.LOCK_DEVICE";
        public static final String MANAGE_ACCESSIBILITY = "android.permission.MANAGE_ACCESSIBILITY";
        public static final String MANAGE_APPOPS = "android.permission.MANAGE_APPOPS";
        public static final String MANAGE_APP_PREDICTIONS =
                "android.permission.MANAGE_APP_PREDICTIONS";
        public static final String MANAGE_BIOMETRIC = "android.permission.MANAGE_BIOMETRIC";
        public static final String MANAGE_BIOMETRIC_DIALOG =
                "android.permission.MANAGE_BIOMETRIC_DIALOG";
        public static final String MANAGE_BLUETOOTH_WHEN_WIRELESS_CONSENT_REQUIRED =
                "android.permission.MANAGE_BLUETOOTH_WHEN_WIRELESS_CONSENT_REQUIRED";
        public static final String MANAGE_CONTENT_CAPTURE =
                "android.permission.MANAGE_CONTENT_CAPTURE";
        public static final String MANAGE_CONTENT_SUGGESTIONS =
                "android.permission.MANAGE_CONTENT_SUGGESTIONS";
        public static final String MANAGE_DEBUGGING = "android.permission.MANAGE_DEBUGGING";
        public static final String MANAGE_DYNAMIC_SYSTEM =
                "android.permission.MANAGE_DYNAMIC_SYSTEM";
        public static final String MANAGE_ROLE_HOLDERS = "android.permission.MANAGE_ROLE_HOLDERS";
        public static final String MANAGE_ROLLBACKS = "android.permission.MANAGE_ROLLBACKS";
        public static final String MANAGE_SENSOR_PRIVACY =
                "android.permission.MANAGE_SENSOR_PRIVACY";
        public static final String MANAGE_TEST_NETWORKS = "android.permission.MANAGE_TEST_NETWORKS";
        public static final String MANAGE_WIFI_WHEN_WIRELESS_CONSENT_REQUIRED =
                "android.permission.MANAGE_WIFI_WHEN_WIRELESS_CONSENT_REQUIRED";
        public static final String MODIFY_DEFAULT_AUDIO_EFFECTS =
                "android.permission.MODIFY_DEFAULT_AUDIO_EFFECTS";
        public static final String MONITOR_DEFAULT_SMS_PACKAGE =
                "android.permission.MONITOR_DEFAULT_SMS_PACKAGE";
        public static final String MONITOR_INPUT = "android.permission.MONITOR_INPUT";
        public static final String NETWORK_CARRIER_PROVISIONING =
                "android.permission.NETWORK_CARRIER_PROVISIONING";
        public static final String NETWORK_MANAGED_PROVISIONING =
                "android.permission.NETWORK_MANAGED_PROVISIONING";
        public static final String NETWORK_SCAN = "android.permission.NETWORK_SCAN";
        public static final String NETWORK_SIGNAL_STRENGTH_WAKEUP =
                "android.permission.NETWORK_SIGNAL_STRENGTH_WAKEUP";
        public static final String OBSERVE_ROLE_HOLDERS = "android.permission.OBSERVE_ROLE_HOLDERS";
        public static final String OPEN_ACCESSIBILITY_DETAILS_SETTINGS =
                "android.permission.OPEN_ACCESSIBILITY_DETAILS_SETTINGS";
        public static final String OPEN_APP_OPEN_BY_DEFAULT_SETTINGS =
                "android.permission.OPEN_APP_OPEN_BY_DEFAULT_SETTINGS";
        public static final String PACKAGE_ROLLBACK_AGENT =
                "android.permission.PACKAGE_ROLLBACK_AGENT";
        public static final String POWER_SAVER = "android.permission.POWER_SAVER";
        public static final String READ_CLIPBOARD_IN_BACKGROUND =
                "android.permission.READ_CLIPBOARD_IN_BACKGROUND";
        public static final String READ_DEVICE_CONFIG = "android.permission.READ_DEVICE_CONFIG";
        public static final String RECEIVE_DEVICE_CUSTOMIZATION_READY =
                "android.permission.RECEIVE_DEVICE_CUSTOMIZATION_READY";
        public static final String REMOTE_DISPLAY_PROVIDER =
                "android.permission.REMOTE_DISPLAY_PROVIDER";
        public static final String REQUEST_INCIDENT_REPORT_APPROVAL =
                "android.permission.REQUEST_INCIDENT_REPORT_APPROVAL";
        public static final String REQUEST_NOTIFICATION_ASSISTANT_SERVICE =
                "android.permission.REQUEST_NOTIFICATION_ASSISTANT_SERVICE";
        public static final String RESET_FACE_LOCKOUT = "android.permission.RESET_FACE_LOCKOUT";
        public static final String RESET_PASSWORD = "android.permission.RESET_PASSWORD";
        public static final String REVIEW_ACCESSIBILITY_SERVICES =
                "android.permission.REVIEW_ACCESSIBILITY_SERVICES";
        public static final String SEND_DEVICE_CUSTOMIZATION_READY =
                "android.permission.SEND_DEVICE_CUSTOMIZATION_READY";
        public static final String SMS_FINANCIAL_TRANSACTIONS =
                "android.permission.SMS_FINANCIAL_TRANSACTIONS";
        public static final String START_ACTIVITIES_FROM_BACKGROUND =
                "android.permission.START_ACTIVITIES_FROM_BACKGROUND";
        public static final String START_ACTIVITY_AS_CALLER =
                "android.permission.START_ACTIVITY_AS_CALLER";
        public static final String START_VIEW_PERMISSION_USAGE =
                "android.permission.START_VIEW_PERMISSION_USAGE";
        public static final String SUBSTITUTE_SHARE_TARGET_APP_NAME_AND_ICON =
                "android.permission.SUBSTITUTE_SHARE_TARGET_APP_NAME_AND_ICON";
        public static final String TEST_MANAGE_ROLLBACKS =
                "android.permission.TEST_MANAGE_ROLLBACKS";
        public static final String USE_BIOMETRIC_INTERNAL =
                "android.permission.USE_BIOMETRIC_INTERNAL";
        public static final String WHITELIST_RESTRICTED_PERMISSIONS =
                "android.permission.WHITELIST_RESTRICTED_PERMISSIONS";
        public static final String WIFI_SET_DEVICE_MOBILITY_STATE =
                "android.permission.WIFI_SET_DEVICE_MOBILITY_STATE";
        public static final String WIFI_UPDATE_USABILITY_STATS_SCORE =
                "android.permission.WIFI_UPDATE_USABILITY_STATS_SCORE";
        public static final String WRITE_DEVICE_CONFIG = "android.permission.WRITE_DEVICE_CONFIG";
        public static final String WRITE_OBB = "android.permission.WRITE_OBB";
        public static final String WRITE_SETTINGS_HOMEPAGE_DATA =
                "android.permission.WRITE_SETTINGS_HOMEPAGE_DATA";

        // New Permissions for Android 11
        public static final String ACCESS_CONTEXT_HUB = "android.permission.ACCESS_CONTEXT_HUB";
        public static final String ACCESS_LOCUS_ID_USAGE_STATS =
                "android.permission.ACCESS_LOCUS_ID_USAGE_STATS";
        public static final String ACCESS_MESSAGES_ON_ICC =
                "android.permission.ACCESS_MESSAGES_ON_ICC";
        public static final String ACCESS_TV_DESCRAMBLER =
                "android.permission.ACCESS_TV_DESCRAMBLER";
        public static final String ACCESS_TV_TUNER = "android.permission.ACCESS_TV_TUNER";
        public static final String ACCESS_VIBRATOR_STATE =
                "android.permission.ACCESS_VIBRATOR_STATE";
        public static final String ACT_AS_PACKAGE_FOR_ACCESSIBILITY =
                "android.permission.ACT_AS_PACKAGE_FOR_ACCESSIBILITY";
        public static final String ADD_TRUSTED_DISPLAY = "android.permission.ADD_TRUSTED_DISPLAY";
        public static final String ASSOCIATE_INPUT_DEVICE_TO_DISPLAY_BY_PORT =
                "android.permission.ASSOCIATE_INPUT_DEVICE_TO_DISPLAY_BY_PORT";
        public static final String BIND_CELL_BROADCAST_SERVICE =
                "android.permission.BIND_CELL_BROADCAST_SERVICE";
        public static final String BIND_CONTROLS = "android.permission.BIND_CONTROLS";
        public static final String BIND_EXTERNAL_STORAGE_SERVICE =
                "android.permission.BIND_EXTERNAL_STORAGE_SERVICE";
        public static final String BIND_INLINE_SUGGESTION_RENDER_SERVICE =
                "android.permission.BIND_INLINE_SUGGESTION_RENDER_SERVICE";
        public static final String BIND_QUICK_ACCESS_WALLET_SERVICE =
                "android.permission.BIND_QUICK_ACCESS_WALLET_SERVICE";
        public static final String CAMERA_OPEN_CLOSE_LISTENER =
                "android.permission.CAMERA_OPEN_CLOSE_LISTENER";
        public static final String CAPTURE_VOICE_COMMUNICATION_OUTPUT =
                "android.permission.CAPTURE_VOICE_COMMUNICATION_OUTPUT";
        public static final String COMPANION_APPROVE_WIFI_CONNECTIONS =
                "android.permission.COMPANION_APPROVE_WIFI_CONNECTIONS";
        public static final String CONFIGURE_INTERACT_ACROSS_PROFILES =
                "android.permission.CONFIGURE_INTERACT_ACROSS_PROFILES";
        public static final String CONTROL_DEVICE_LIGHTS =
                "android.permission.CONTROL_DEVICE_LIGHTS";
        public static final String ENTER_CAR_MODE_PRIORITIZED =
                "android.permission.ENTER_CAR_MODE_PRIORITIZED";
        public static final String EXEMPT_FROM_AUDIO_RECORD_RESTRICTIONS =
                "android.permission.EXEMPT_FROM_AUDIO_RECORD_RESTRICTIONS";
        public static final String GRANT_RUNTIME_PERMISSIONS_TO_TELEPHONY_DEFAULTS =
                "android.permission.GRANT_RUNTIME_PERMISSIONS_TO_TELEPHONY_DEFAULTS";
        public static final String HANDLE_CAR_MODE_CHANGES =
                "android.permission.HANDLE_CAR_MODE_CHANGES";
        public static final String KEYPHRASE_ENROLLMENT_APPLICATION =
                "android.permission.KEYPHRASE_ENROLLMENT_APPLICATION";
        public static final String LISTEN_ALWAYS_REPORTED_SIGNAL_STRENGTH =
                "android.permission.LISTEN_ALWAYS_REPORTED_SIGNAL_STRENGTH";
        public static final String LOADER_USAGE_STATS = "android.permission.LOADER_USAGE_STATS";
        public static final String LOG_COMPAT_CHANGE = "android.permission.LOG_COMPAT_CHANGE";
        public static final String MANAGE_COMPANION_DEVICES =
                "android.permission.MANAGE_COMPANION_DEVICES";
        public static final String MANAGE_CRATES = "android.permission.MANAGE_CRATES";
        public static final String MANAGE_EXTERNAL_STORAGE =
                "android.permission.MANAGE_EXTERNAL_STORAGE";
        public static final String MANAGE_FACTORY_RESET_PROTECTION =
                "android.permission.MANAGE_FACTORY_RESET_PROTECTION";
        public static final String MANAGE_ONE_TIME_PERMISSION_SESSIONS =
                "android.permission.MANAGE_ONE_TIME_PERMISSION_SESSIONS";
        public static final String MARK_DEVICE_ORGANIZATION_OWNED =
                "android.permission.MARK_DEVICE_ORGANIZATION_OWNED";
        public static final String MEDIA_RESOURCE_OVERRIDE_PID =
                "android.permission.MEDIA_RESOURCE_OVERRIDE_PID";
        public static final String MODIFY_SETTINGS_OVERRIDEABLE_BY_RESTORE =
                "android.permission.MODIFY_SETTINGS_OVERRIDEABLE_BY_RESTORE";
        public static final String MONITOR_DEVICE_CONFIG_ACCESS =
                "android.permission.MONITOR_DEVICE_CONFIG_ACCESS";
        public static final String NETWORK_AIRPLANE_MODE =
                "android.permission.NETWORK_AIRPLANE_MODE";
        public static final String NETWORK_FACTORY = "android.permission.NETWORK_FACTORY";
        public static final String NETWORK_STATS_PROVIDER =
                "android.permission.NETWORK_STATS_PROVIDER";
        public static final String OBSERVE_NETWORK_POLICY =
                "android.permission.OBSERVE_NETWORK_POLICY";
        public static final String OVERRIDE_COMPAT_CHANGE_CONFIG =
                "android.permission.OVERRIDE_COMPAT_CHANGE_CONFIG";
        public static final String PEEK_DROPBOX_DATA = "android.permission.PEEK_DROPBOX_DATA";
        public static final String RADIO_SCAN_WITHOUT_LOCATION =
                "android.permission.RADIO_SCAN_WITHOUT_LOCATION";
        public static final String READ_ACTIVE_EMERGENCY_SESSION =
                "android.permission.READ_ACTIVE_EMERGENCY_SESSION";
        public static final String READ_CARRIER_APP_INFO =
                "android.permission.READ_CARRIER_APP_INFO";
        public static final String READ_COMPAT_CHANGE_CONFIG =
                "android.permission.READ_COMPAT_CHANGE_CONFIG";
        public static final String REGISTER_STATS_PULL_ATOM =
                "android.permission.REGISTER_STATS_PULL_ATOM";
        public static final String RESTORE_RUNTIME_PERMISSIONS =
                "android.permission.RESTORE_RUNTIME_PERMISSIONS";
        public static final String SECURE_ELEMENT_PRIVILEGED_OPERATION =
                "android.permission.SECURE_ELEMENT_PRIVILEGED_OPERATION";
        public static final String SET_INITIAL_LOCK = "android.permission.SET_INITIAL_LOCK";
        public static final String SUGGEST_MANUAL_TIME_AND_ZONE =
                "android.permission.SUGGEST_MANUAL_TIME_AND_ZONE";
        public static final String SUGGEST_TELEPHONY_TIME_AND_ZONE =
                "android.permission.SUGGEST_TELEPHONY_TIME_AND_ZONE";
        public static final String SYSTEM_CAMERA = "android.permission.SYSTEM_CAMERA";
        public static final String TRIGGER_SHELL_BUGREPORT =
                "android.permission.TRIGGER_SHELL_BUGREPORT";
        public static final String TUNER_RESOURCE_ACCESS =
                "android.permission.TUNER_RESOURCE_ACCESS";
        public static final String UPGRADE_RUNTIME_PERMISSIONS =
                "android.permission.UPGRADE_RUNTIME_PERMISSIONS";
        public static final String VIBRATE_ALWAYS_ON = "android.permission.VIBRATE_ALWAYS_ON";
        public static final String WHITELIST_AUTO_REVOKE_PERMISSIONS =
                "android.permission.WHITELIST_AUTO_REVOKE_PERMISSIONS";
        public static final String USE_INSTALLER_V2 = "com.android.permission.USE_INSTALLER_V2";

        // The following are the new signature permissions for Android 12.
        public static final String ACCESS_BLOBS_ACROSS_USERS =
                "android.permission.ACCESS_BLOBS_ACROSS_USERS";
        public static final String ACCESS_TUNED_INFO = "android.permission.ACCESS_TUNED_INFO";
        public static final String ASSOCIATE_INPUT_DEVICE_TO_DISPLAY =
                "android.permission.ASSOCIATE_INPUT_DEVICE_TO_DISPLAY";
        public static final String BATTERY_PREDICTION = "android.permission.BATTERY_PREDICTION";
        public static final String BIND_CALL_DIAGNOSTIC_SERVICE =
                "android.permission.BIND_CALL_DIAGNOSTIC_SERVICE";
        public static final String BIND_COMPANION_DEVICE_SERVICE =
                "android.permission.BIND_COMPANION_DEVICE_SERVICE";
        public static final String BIND_DISPLAY_HASHING_SERVICE =
                "android.permission.BIND_DISPLAY_HASHING_SERVICE";
        public static final String BIND_DOMAIN_VERIFICATION_AGENT =
                "android.permission.BIND_DOMAIN_VERIFICATION_AGENT";
        public static final String BIND_GBA_SERVICE = "android.permission.BIND_GBA_SERVICE";
        public static final String BIND_HOTWORD_DETECTION_SERVICE =
                "android.permission.BIND_HOTWORD_DETECTION_SERVICE";
        public static final String BIND_MUSIC_RECOGNITION_SERVICE =
                "android.permission.BIND_MUSIC_RECOGNITION_SERVICE";
        public static final String BIND_RESUME_ON_REBOOT_SERVICE =
                "android.permission.BIND_RESUME_ON_REBOOT_SERVICE";
        public static final String BIND_ROTATION_RESOLVER_SERVICE =
                "android.permission.BIND_ROTATION_RESOLVER_SERVICE";
        public static final String BIND_TIME_ZONE_PROVIDER_SERVICE =
                "android.permission.BIND_TIME_ZONE_PROVIDER_SERVICE";
        public static final String BIND_TRANSLATION_SERVICE =
                "android.permission.BIND_TRANSLATION_SERVICE";
        public static final String BROADCAST_CLOSE_SYSTEM_DIALOGS =
                "android.permission.BROADCAST_CLOSE_SYSTEM_DIALOGS";
        public static final String CAMERA_INJECT_EXTERNAL_CAMERA =
                "android.permission.CAMERA_INJECT_EXTERNAL_CAMERA";
        public static final String CAPTURE_BLACKOUT_CONTENT =
                "android.permission.CAPTURE_BLACKOUT_CONTENT";
        public static final String CAPTURE_TUNER_AUDIO_INPUT =
                "android.permission.CAPTURE_TUNER_AUDIO_INPUT";
        public static final String CLEAR_FREEZE_PERIOD = "android.permission.CLEAR_FREEZE_PERIOD";
        public static final String CONTROL_DEVICE_STATE = "android.permission.CONTROL_DEVICE_STATE";
        public static final String CONTROL_OEM_PAID_NETWORK_PREFERENCE =
                "android.permission.CONTROL_OEM_PAID_NETWORK_PREFERENCE";
        public static final String CONTROL_UI_TRACING = "android.permission.CONTROL_UI_TRACING";
        public static final String DISABLE_SYSTEM_SOUND_EFFECTS =
                "android.permission.DISABLE_SYSTEM_SOUND_EFFECTS";
        public static final String FORCE_DEVICE_POLICY_MANAGER_LOGS =
                "android.permission.FORCE_DEVICE_POLICY_MANAGER_LOGS";
        public static final String GET_PEOPLE_TILE_PREVIEW =
                "android.permission.GET_PEOPLE_TILE_PREVIEW";
        public static final String GET_RUNTIME_PERMISSION_GROUP_MAPPING =
                "android.permission.GET_RUNTIME_PERMISSION_GROUP_MAPPING";
        public static final String INPUT_CONSUMER = "android.permission.INPUT_CONSUMER";
        public static final String INSTALL_LOCATION_TIME_ZONE_PROVIDER_SERVICE =
                "android.permission.INSTALL_LOCATION_TIME_ZONE_PROVIDER_SERVICE";
        public static final String INSTALL_TEST_ONLY_PACKAGE =
                "android.permission.INSTALL_TEST_ONLY_PACKAGE";
        public static final String KEEP_UNINSTALLED_PACKAGES =
                "android.permission.KEEP_UNINSTALLED_PACKAGES";
        public static final String MANAGE_ACTIVITY_TASKS =
                "android.permission.MANAGE_ACTIVITY_TASKS";
        public static final String MANAGE_APP_HIBERNATION =
                "android.permission.MANAGE_APP_HIBERNATION";
        public static final String MANAGE_CREDENTIAL_MANAGEMENT_APP =
                "android.permission.MANAGE_CREDENTIAL_MANAGEMENT_APP";
        public static final String MANAGE_GAME_MODE = "android.permission.MANAGE_GAME_MODE";
        public static final String MANAGE_MEDIA = "android.permission.MANAGE_MEDIA";
        public static final String MANAGE_MUSIC_RECOGNITION =
                "android.permission.MANAGE_MUSIC_RECOGNITION";
        public static final String MANAGE_NOTIFICATION_LISTENERS =
                "android.permission.MANAGE_NOTIFICATION_LISTENERS";
        public static final String MANAGE_ONGOING_CALLS = "android.permission.MANAGE_ONGOING_CALLS";
        public static final String MANAGE_ROTATION_RESOLVER =
                "android.permission.MANAGE_ROTATION_RESOLVER";
        public static final String MANAGE_SEARCH_UI = "android.permission.MANAGE_SEARCH_UI";
        public static final String MANAGE_SMARTSPACE = "android.permission.MANAGE_SMARTSPACE";
        public static final String MANAGE_SPEECH_RECOGNITION =
                "android.permission.MANAGE_SPEECH_RECOGNITION";
        public static final String MANAGE_TIME_AND_ZONE_DETECTION =
                "android.permission.MANAGE_TIME_AND_ZONE_DETECTION";
        public static final String MANAGE_TOAST_RATE_LIMITING =
                "android.permission.MANAGE_TOAST_RATE_LIMITING";
        public static final String MANAGE_UI_TRANSLATION =
                "android.permission.MANAGE_UI_TRANSLATION";
        public static final String MANAGE_WIFI_COUNTRY_CODE =
                "android.permission.MANAGE_WIFI_COUNTRY_CODE";
        public static final String MODIFY_REFRESH_RATE_SWITCHING_TYPE =
                "android.permission.MODIFY_REFRESH_RATE_SWITCHING_TYPE";
        public static final String NFC_SET_CONTROLLER_ALWAYS_ON =
                "android.permission.NFC_SET_CONTROLLER_ALWAYS_ON";
        public static final String OVERRIDE_COMPAT_CHANGE_CONFIG_ON_RELEASE_BUILD =
                "android.permission.OVERRIDE_COMPAT_CHANGE_CONFIG_ON_RELEASE_BUILD";
        public static final String OVERRIDE_DISPLAY_MODE_REQUESTS =
                "android.permission.OVERRIDE_DISPLAY_MODE_REQUESTS";
        public static final String QUERY_AUDIO_STATE = "android.permission.QUERY_AUDIO_STATE";
        public static final String READ_DREAM_SUPPRESSION =
                "android.permission.READ_DREAM_SUPPRESSION";
        public static final String READ_NEARBY_STREAMING_POLICY =
                "android.permission.READ_NEARBY_STREAMING_POLICY";
        public static final String READ_PEOPLE_DATA = "android.permission.READ_PEOPLE_DATA";
        public static final String READ_PROJECTION_STATE =
                "android.permission.READ_PROJECTION_STATE";
        public static final String REGISTER_MEDIA_RESOURCE_OBSERVER =
                "android.permission.REGISTER_MEDIA_RESOURCE_OBSERVER";
        public static final String RENOUNCE_PERMISSIONS = "android.permission.RENOUNCE_PERMISSIONS";
        public static final String RESET_APP_ERRORS = "android.permission.RESET_APP_ERRORS";
        public static final String RESTART_WIFI_SUBSYSTEM =
                "android.permission.RESTART_WIFI_SUBSYSTEM";
        public static final String ROTATE_SURFACE_FLINGER =
                "android.permission.ROTATE_SURFACE_FLINGER";
        public static final String SCHEDULE_PRIORITIZED_ALARM =
                "android.permission.SCHEDULE_PRIORITIZED_ALARM";
        public static final String SEND_CATEGORY_CAR_NOTIFICATIONS =
                "android.permission.SEND_CATEGORY_CAR_NOTIFICATIONS";
        public static final String SET_AND_VERIFY_LOCKSCREEN_CREDENTIALS =
                "android.permission.SET_AND_VERIFY_LOCKSCREEN_CREDENTIALS";
        public static final String SET_CLIP_SOURCE = "android.permission.SET_CLIP_SOURCE";
        public static final String SIGNAL_REBOOT_READINESS =
                "android.permission.SIGNAL_REBOOT_READINESS";
        public static final String SOUNDTRIGGER_DELEGATE_IDENTITY =
                "android.permission.SOUNDTRIGGER_DELEGATE_IDENTITY";
        public static final String SOUND_TRIGGER_RUN_IN_BATTERY_SAVER =
                "android.permission.SOUND_TRIGGER_RUN_IN_BATTERY_SAVER";
        public static final String START_FOREGROUND_SERVICES_FROM_BACKGROUND =
                "android.permission.START_FOREGROUND_SERVICES_FROM_BACKGROUND";
        public static final String SUGGEST_EXTERNAL_TIME =
                "android.permission.SUGGEST_EXTERNAL_TIME";
        public static final String SYSTEM_APPLICATION_OVERLAY =
                "android.permission.SYSTEM_APPLICATION_OVERLAY";
        public static final String TEST_BIOMETRIC = "android.permission.TEST_BIOMETRIC";
        public static final String TOGGLE_AUTOMOTIVE_PROJECTION =
                "android.permission.TOGGLE_AUTOMOTIVE_PROJECTION";
        public static final String UNLIMITED_TOASTS = "android.permission.UNLIMITED_TOASTS";
        public static final String UPDATE_DOMAIN_VERIFICATION_USER_SELECTION =
                "android.permission.UPDATE_DOMAIN_VERIFICATION_USER_SELECTION";
        public static final String UPDATE_FONTS = "android.permission.UPDATE_FONTS";
        public static final String USE_ICC_AUTH_WITH_DEVICE_IDENTIFIER =
                "android.permission.USE_ICC_AUTH_WITH_DEVICE_IDENTIFIER";
        public static final String UWB_PRIVILEGED = "android.permission.UWB_PRIVILEGED";
        public static final String VIRTUAL_INPUT_DEVICE = "android.permission.VIRTUAL_INPUT_DEVICE";
        public static final String WIFI_ACCESS_COEX_UNSAFE_CHANNELS =
                "android.permission.WIFI_ACCESS_COEX_UNSAFE_CHANNELS";
        public static final String WIFI_UPDATE_COEX_UNSAFE_CHANNELS =
                "android.permission.WIFI_UPDATE_COEX_UNSAFE_CHANNELS";
        public static final String USE_SYSTEM_DATA_LOADERS =
                "com.android.permission.USE_SYSTEM_DATA_LOADERS";
        // The following are the new signature permissions for Android SV2
        public static final String TRIGGER_SHELL_PROFCOLLECT_UPLOAD =
                "android.permission.TRIGGER_SHELL_PROFCOLLECT_UPLOAD";
        public static final String ALLOW_PLACE_IN_MULTI_PANE_SETTINGS =
                "android.permission.ALLOW_PLACE_IN_MULTI_PANE_SETTINGS";
        public static final String LAUNCH_MULTI_PANE_SETTINGS_DEEP_LINK =
                "android.permission.LAUNCH_MULTI_PANE_SETTINGS_DEEP_LINK";
        public static final String ALLOW_SLIPPERY_TOUCHES =
                "android.permission.ALLOW_SLIPPERY_TOUCHES";

        // New Signature permissions as of Android 13
        public static final String LOCATION_BYPASS =
                "android.permission.LOCATION_BYPASS";
        public static final String CONTROL_AUTOMOTIVE_GNSS =
                "android.permission.CONTROL_AUTOMOTIVE_GNSS";
        public static final String MANAGE_WIFI_NETWORK_SELECTION =
                "android.permission.MANAGE_WIFI_NETWORK_SELECTION";
        public static final String MANAGE_WIFI_INTERFACES =
                "android.permission.MANAGE_WIFI_INTERFACES";
        public static final String TRIGGER_LOST_MODE =
                "android.permission.TRIGGER_LOST_MODE";
        public static final String START_CROSS_PROFILE_ACTIVITIES =
                "android.permission.START_CROSS_PROFILE_ACTIVITIES";
        public static final String QUERY_USERS =
                "android.permission.QUERY_USERS";
        public static final String QUERY_ADMIN_POLICY =
                "android.permission.QUERY_ADMIN_POLICY";
        public static final String PROVISION_DEMO_DEVICE =
                "android.permission.PROVISION_DEMO_DEVICE";
        public static final String REQUEST_COMPANION_PROFILE_APP_STREAMING =
                "android.permission.REQUEST_COMPANION_PROFILE_APP_STREAMING";
        public static final String REQUEST_COMPANION_PROFILE_COMPUTER =
                "android.permission.REQUEST_COMPANION_PROFILE_COMPUTER";
        public static final String REQUEST_COMPANION_SELF_MANAGED =
                "android.permission.REQUEST_COMPANION_SELF_MANAGED";
        public static final String READ_APP_SPECIFIC_LOCALES =
                "android.permission.READ_APP_SPECIFIC_LOCALES";
        public static final String USE_ATTESTATION_VERIFICATION_SERVICE =
                "android.permission.USE_ATTESTATION_VERIFICATION_SERVICE";
        public static final String VERIFY_ATTESTATION =
                "android.permission.VERIFY_ATTESTATION";
        public static final String BIND_ATTESTATION_VERIFICATION_SERVICE =
                "android.permission.BIND_ATTESTATION_VERIFICATION_SERVICE";
        public static final String REQUEST_UNIQUE_ID_ATTESTATION =
                "android.permission.REQUEST_UNIQUE_ID_ATTESTATION";
        public static final String GET_HISTORICAL_APP_OPS_STATS =
                "android.permission.GET_HISTORICAL_APP_OPS_STATS";
        public static final String SET_SYSTEM_AUDIO_CAPTION =
                "android.permission.SET_SYSTEM_AUDIO_CAPTION";
        public static final String BIND_GAME_SERVICE =
                "android.permission.BIND_GAME_SERVICE";
        public static final String BIND_SELECTION_TOOLBAR_RENDER_SERVICE =
                "android.permission.BIND_SELECTION_TOOLBAR_RENDER_SERVICE";
        public static final String BIND_WALLPAPER_EFFECTS_GENERATION_SERVICE =
                "android.permission.BIND_WALLPAPER_EFFECTS_GENERATION_SERVICE";
        public static final String BIND_TV_INTERACTIVE_APP =
                "android.permission.BIND_TV_INTERACTIVE_APP";
        public static final String INSTALL_DPC_PACKAGES =
                "android.permission.INSTALL_DPC_PACKAGES";
        public static final String REVOKE_POST_NOTIFICATIONS_WITHOUT_KILL =
                "android.permission.REVOKE_POST_NOTIFICATIONS_WITHOUT_KILL";
        public static final String DELIVER_COMPANION_MESSAGES =
                "android.permission.DELIVER_COMPANION_MESSAGES";
        public static final String MODIFY_TOUCH_MODE_STATE =
                "android.permission.MODIFY_TOUCH_MODE_STATE";
        public static final String MODIFY_USER_PREFERRED_DISPLAY_MODE =
                "android.permission.MODIFY_USER_PREFERRED_DISPLAY_MODE";
        public static final String ACCESS_ULTRASOUND =
                "android.permission.ACCESS_ULTRASOUND";
        public static final String CALL_AUDIO_INTERCEPTION =
                "android.permission.CALL_AUDIO_INTERCEPTION";
        public static final String MANAGE_LOW_POWER_STANDBY =
                "android.permission.MANAGE_LOW_POWER_STANDBY";
        public static final String ACCESS_BROADCAST_RESPONSE_STATS =
                "android.permission.ACCESS_BROADCAST_RESPONSE_STATS";
        public static final String CHANGE_APP_LAUNCH_TIME_ESTIMATE =
                "android.permission.CHANGE_APP_LAUNCH_TIME_ESTIMATE";
        public static final String SET_WALLPAPER_DIM_AMOUNT =
                "android.permission.SET_WALLPAPER_DIM_AMOUNT";
        public static final String MANAGE_WEAK_ESCROW_TOKEN =
                "android.permission.MANAGE_WEAK_ESCROW_TOKEN";
        public static final String START_REVIEW_PERMISSION_DECISIONS =
                "android.permission.START_REVIEW_PERMISSION_DECISIONS";
        public static final String START_VIEW_APP_FEATURES =
                "android.permission.START_VIEW_APP_FEATURES";
        public static final String MANAGE_CLOUDSEARCH =
                "android.permission.MANAGE_CLOUDSEARCH";
        public static final String MANAGE_WALLPAPER_EFFECTS_GENERATION =
                "android.permission.MANAGE_WALLPAPER_EFFECTS_GENERATION";
        public static final String SUPPRESS_CLIPBOARD_ACCESS_NOTIFICATION =
                "android.permission.SUPPRESS_CLIPBOARD_ACCESS_NOTIFICATION";
        public static final String ACCESS_TV_SHARED_FILTER =
                "android.permission.ACCESS_TV_SHARED_FILTER";
        public static final String ADD_ALWAYS_UNLOCKED_DISPLAY =
                "android.permission.ADD_ALWAYS_UNLOCKED_DISPLAY";
        public static final String SET_GAME_SERVICE =
                "android.permission.SET_GAME_SERVICE";
        public static final String ACCESS_FPS_COUNTER =
                "android.permission.ACCESS_FPS_COUNTER";
        public static final String MANAGE_GAME_ACTIVITY =
                "android.permission.MANAGE_GAME_ACTIVITY";
        public static final String LAUNCH_DEVICE_MANAGER_SETUP =
                "android.permission.LAUNCH_DEVICE_MANAGER_SETUP";
        public static final String UPDATE_DEVICE_MANAGEMENT_RESOURCES =
                "android.permission.UPDATE_DEVICE_MANAGEMENT_RESOURCES";
        public static final String READ_SAFETY_CENTER_STATUS =
                "android.permission.READ_SAFETY_CENTER_STATUS";
        public static final String BIND_AMBIENT_CONTEXT_DETECTION_SERVICE =
                "android.permission.BIND_AMBIENT_CONTEXT_DETECTION_SERVICE";
        public static final String SET_UNRESTRICTED_KEEP_CLEAR_AREAS =
                "android.permission.SET_UNRESTRICTED_KEEP_CLEAR_AREAS";
        public static final String TIS_EXTENSION_INTERFACE =
                "android.permission.TIS_EXTENSION_INTERFACE";
        public static final String WRITE_SECURITY_LOG =
                "android.permission.WRITE_SECURITY_LOG";
        public static final String MAKE_UID_VISIBLE =
                "android.permission.MAKE_UID_VISIBLE";
        // New Internal permissions as of Android 13
        public static final String REQUEST_COMPANION_PROFILE_AUTOMOTIVE_PROJECTION =
                "android.permission.REQUEST_COMPANION_PROFILE_AUTOMOTIVE_PROJECTION";
        public static final String BIND_TRACE_REPORT_SERVICE =
                "android.permission.BIND_TRACE_REPORT_SERVICE";
        public static final String SUBSCRIBE_TO_KEYGUARD_LOCKED_STATE =
                "android.permission.SUBSCRIBE_TO_KEYGUARD_LOCKED_STATE";
        public static final String CREATE_VIRTUAL_DEVICE =
                "android.permission.CREATE_VIRTUAL_DEVICE";
        public static final String SEND_SAFETY_CENTER_UPDATE =
                "android.permission.SEND_SAFETY_CENTER_UPDATE";
        public static final String ACCESS_AMBIENT_CONTEXT_EVENT =
                "android.permission.ACCESS_AMBIENT_CONTEXT_EVENT";
        public static final String MANAGE_ETHERNET_NETWORKS =
                "android.permission.MANAGE_ETHERNET_NETWORKS";
    }

    /**
     * Signature protection level permissions declared by the platform.
     */
    private static final List<String> mSignaturePermissions = new ArrayList<>();
    private static final Object mSignaturePermissionsLock = new Object();

    /**
     * Signature protection level permissions declared by the platform with the privileged
     * protection flag.
     */
    private static final Set<String> mPrivilegedPermissions = new HashSet<>();

    /**
     * Signature protection level permissions declared by the platform with the development
     * protection flag.
     */
    private static final Set<String> mDevelopmentPermissions = new HashSet<>();

    /**
     * Returns a {@link List} of all signature protection level permissions declared by the
     * platform.
     */
    public static List<String> getSignaturePermissions(Context context) {
        if (mSignaturePermissions.size() == 0) {
            querySignaturePermissions(context);
        }
        return mSignaturePermissions;
    }

    /**
     * Returns a {@link Set} of all signature protection level permissions declared by the platform
     * with the privileged protection flag.
     */
    public static Set<String> getPrivilegedPermissions(Context context) {
        if (mPrivilegedPermissions.size() == 0) {
            querySignaturePermissions(context);
        }
        return mPrivilegedPermissions;
    }

    /**
     * Returns a {@link Set} of all signature protection level permissions declared by the platform
     * with the development protection flag.
     */
    public static Set<String> getDevelopmentPermissions(Context context) {
        if (mDevelopmentPermissions.size() == 0) {
            querySignaturePermissions(context);
        }
        return mDevelopmentPermissions;
    }

    /**
     * Queries the platform declared permissions for all signature protection level permissions and
     * updates the corresponding list / sets with the signature, privileged, and development
     * permissions.
     */
    private static void querySignaturePermissions(Context context) {
        synchronized (mSignaturePermissionsLock) {
            // if all of the permission list / sets have 0 elements then query for all platform
            // permissions and update the corresponding list / sets.
            if (mSignaturePermissions.size() == 0
                    && mPrivilegedPermissions.size() == 0
                    && mDevelopmentPermissions.size() == 0) {
                List<PermissionInfo> permissions =
                        PermissionUtils.getAllDeclaredPlatformPermissions(context);
                for (PermissionInfo permission : permissions) {
                    if (permission.getProtection() == PermissionInfo.PROTECTION_SIGNATURE) {
                        mSignaturePermissions.add(permission.name);
                        if ((permission.getProtectionFlags()
                                & PermissionInfo.PROTECTION_FLAG_PRIVILEGED) != 0) {
                            mPrivilegedPermissions.add(permission.name);
                        }
                        if ((permission.getProtectionFlags()
                                & PermissionInfo.PROTECTION_FLAG_DEVELOPMENT) != 0) {
                            mDevelopmentPermissions.add(permission.name);
                        }
                    }
                }
            }
        }
    }
}
