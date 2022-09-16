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

import static com.android.certifications.niap.permissions.utils.SignaturePermissions.permission;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import com.android.certifications.niap.permissions.BasePermissionTester;
import com.android.certifications.niap.permissions.R;
import com.android.certifications.niap.permissions.RuntimePermissionTester;
import com.android.certifications.niap.permissions.SignaturePermissionTester;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Configuration designed to test signature permissions that are dependent on other permissions. In
 * this case the signature permission under test is RADIO_SCAN_WITHOUT_LOCATION, intended to allow a
 * radio scan to be performed without a location permission granted.
 */
public class SignatureDependentPermissionConfiguration implements TestConfiguration {
    private final Activity mActivity;

    private static final String[] DEPENDENT_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.UWB_RANGING,
    };

    public SignatureDependentPermissionConfiguration(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void preRunSetup() throws BypassConfigException {
        if (areDependentPermissionsGranted(mActivity)) {
            throw new BypassConfigException(
                    mActivity.getResources().getString(R.string.permissions_must_not_be_granted,
                            String.join(", ", DEPENDENT_PERMISSIONS)));
        }
    }

    /**
     * Returns whether the dependent permissions have been granted; in order to run the test in this
     * configuration these permissions must not be granted.
     */
    public static boolean areDependentPermissionsGranted(Activity activity) {
        for (String permission : DEPENDENT_PERMISSIONS) {
            if (activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<BasePermissionTester> getPermissionTesters(Activity activity) {

        List<BasePermissionTester> permissionTesters = new ArrayList<>();
        permissionTesters.add(new RuntimePermissionTester(this, activity));
        permissionTesters.add(new SignaturePermissionTester(this, activity));
        return permissionTesters;
    }

    @Override
    public Optional<List<String>> getRuntimePermissions() {
        // The API guarded by UWB_RANGING is also guarded by UWB_PRIVILEGED which is checked first.
        // In this configuration UWB_PRIVILEGED should be granted while UWB_RANGING is not, so this
        // allows verification that this permission is checked and the API fails as expected when
        // it is not granted.
        return Optional.of(Collections.singletonList(Manifest.permission.UWB_RANGING));
    }

    @Override
    public Optional<List<String>> getSignaturePermissions() {

        List<String> permissions = new ArrayList<>();

        // Starting in Android 12 the NETWORK_SCAN permission behaves similar to
        // RADIO_SCAN_WITHOUT_LOCATION in that it allows a network scan without a location
        // permission.
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        //    permissions.add(permission.RADIO_SCAN_WITHOUT_LOCATION);
        //    permissions.add(permission.NETWORK_SCAN);
        //}

//        addPermissionsFor28(permissions);
//        addPermissionsFor29(permissions);
//        addPermissionsFor30(permissions);
//        addPermissionsFor31(permissions);
//        addPermissionsFor32(permissions);
//        addPermissionsFor33(permissions);

        //permissions.add(permission.CAPTURE_AUDIO_OUTPUT);
        //permissions.add(permission.CRYPT_KEEPER);
        //permissions.add(permission.MARK_DEVICE_ORGANIZATION_OWNED);
        //permissions.add(permission.PEEK_DROPBOX_DATA);
        //permissions.add(permission.CONTROL_DEVICE_STATE);
        //permissions.add(permission.RESET_PASSWORD);
        //permissions.add(permission.MOUNT_FORMAT_FILESYSTEMS);
        //addPermissionsNeedPreReq(permissions);
        //addPermissionsWithError(permissions);

        permissions.add(permission.CONNECTIVITY_USE_RESTRICTED_NETWORKS);

        return Optional.of(permissions);
    }

    //Those test suites require some runtime permissions to execute.
    private void addPermissionsNeedPreReq(List<String> permissions)
    {
        permissions.add(permission.LOCAL_MAC_ADDRESS);
        permissions.add(permission.MANAGE_SOUND_TRIGGER);
        permissions.add(permission.READ_VOICEMAIL);
        permissions.add(permission.WRITE_VOICEMAIL);
        permissions.add(permission.ACCESS_MESSAGES_ON_ICC);
    }

    private void addPermissionsWithError(List<String> permissions)
    {
        permissions.add(permission.CHANGE_COMPONENT_ENABLED_STATE);
        permissions.add(permission.CONNECTIVITY_USE_RESTRICTED_NETWORKS);
        permissions.add(permission.CONTROL_REMOTE_APP_TRANSITION_ANIMATIONS);
        permissions.add(permission.DELETE_PACKAGES);
        permissions.add(permission.FORCE_PERSISTABLE_URI_PERMISSIONS);
        permissions.add(permission.HARDWARE_TEST);
        permissions.add(permission.MANAGE_DOCUMENTS);
        permissions.add(permission.MANAGE_PROFILE_AND_DEVICE_OWNERS);
        permissions.add(permission.OBSERVE_GRANT_REVOKE_PERMISSIONS);
        permissions.add(permission.PACKET_KEEPALIVE_OFFLOAD);
        permissions.add(permission.READ_SEARCH_INDEXABLES);
        permissions.add(permission.SET_INPUT_CALIBRATION);
        permissions.add(permission.SET_KEYBOARD_LAYOUT);
        permissions.add(permission.START_TASKS_FROM_RECENTS);
        permissions.add(permission.KEYPHRASE_ENROLLMENT_APPLICATION);
        permissions.add(permission.NETWORK_STATS_PROVIDER);
        permissions.add(permission.FORCE_DEVICE_POLICY_MANAGER_LOGS);
        permissions.add(permission.GET_PEOPLE_TILE_PREVIEW);
        permissions.add(permission.MANAGE_MUSIC_RECOGNITION);
        permissions.add(permission.MANAGE_WIFI_COUNTRY_CODE);
        permissions.add(permission.SET_AND_VERIFY_LOCKSCREEN_CREDENTIALS);
        permissions.add(permission.SIGNAL_REBOOT_READINESS);
        permissions.add(permission.SOUNDTRIGGER_DELEGATE_IDENTITY);
        permissions.add(permission.SUGGEST_EXTERNAL_TIME);
        permissions.add(permission.WIFI_ACCESS_COEX_UNSAFE_CHANNELS);
    }
    
    //Add all signature permission tests for SDK28 
    private void addPermissionsFor28(List<String> permissions)
    {
        permissions.add(permission.ACCESS_AMBIENT_LIGHT_STATS);
        permissions.add(permission.ACCESS_CACHE_FILESYSTEM);
        permissions.add(permission.ACCESS_CONTENT_PROVIDERS_EXTERNALLY);
        permissions.add(permission.ACCESS_INSTANT_APPS);
        permissions.add(permission.ACCESS_KEYGUARD_SECURE_STORAGE);
        permissions.add(permission.ACCESS_MTP);
        permissions.add(permission.ACCESS_NOTIFICATIONS);
        permissions.add(permission.ACCESS_SHORTCUTS);
        permissions.add(permission.ACCESS_SURFACE_FLINGER);
        permissions.add(permission.ACCESS_VOICE_INTERACTION_SERVICE);
        permissions.add(permission.ACCESS_VR_MANAGER);
        permissions.add(permission.ACCESS_VR_STATE);
        permissions.add(permission.ALLOCATE_AGGRESSIVE);
        permissions.add(permission.BACKUP);
        permissions.add(permission.BATTERY_STATS);
        permissions.add(permission.BIND_ACCESSIBILITY_SERVICE);
        permissions.add(permission.BIND_APPWIDGET);
        permissions.add(permission.BIND_AUTOFILL);
        permissions.add(permission.BIND_AUTOFILL_FIELD_CLASSIFICATION_SERVICE);
        permissions.add(permission.BIND_AUTOFILL_SERVICE);
        permissions.add(permission.BIND_CACHE_QUOTA_SERVICE);
        permissions.add(permission.BIND_CARRIER_MESSAGING_SERVICE);
        permissions.add(permission.BIND_CARRIER_SERVICES);
        permissions.add(permission.BIND_CHOOSER_TARGET_SERVICE);
        permissions.add(permission.BIND_COMPANION_DEVICE_MANAGER_SERVICE);
        permissions.add(permission.BIND_CONDITION_PROVIDER_SERVICE);
        permissions.add(permission.BIND_CONNECTION_SERVICE);
        permissions.add(permission.BIND_DEVICE_ADMIN);
        permissions.add(permission.BIND_DIRECTORY_SEARCH);
        permissions.add(permission.BIND_DREAM_SERVICE);
        permissions.add(permission.BIND_EUICC_SERVICE);
        permissions.add(permission.BIND_IMS_SERVICE);
        permissions.add(permission.BIND_INCALL_SERVICE);
        permissions.add(permission.BIND_INPUT_METHOD);
        permissions.add(permission.BIND_INTENT_FILTER_VERIFIER);
        permissions.add(permission.BIND_JOB_SERVICE);
        permissions.add(permission.BIND_KEYGUARD_APPWIDGET);
        permissions.add(permission.BIND_MIDI_DEVICE_SERVICE);
        permissions.add(permission.BIND_NETWORK_RECOMMENDATION_SERVICE);
        permissions.add(permission.BIND_NFC_SERVICE);
        permissions.add(permission.BIND_NOTIFICATION_ASSISTANT_SERVICE);
        permissions.add(permission.BIND_NOTIFICATION_LISTENER_SERVICE);
        permissions.add(permission.BIND_PACKAGE_VERIFIER);
        permissions.add(permission.BIND_PRINT_RECOMMENDATION_SERVICE);
        permissions.add(permission.BIND_PRINT_SERVICE);
        permissions.add(permission.BIND_PRINT_SPOOLER_SERVICE);
        permissions.add(permission.BIND_QUICK_SETTINGS_TILE);
        permissions.add(permission.BIND_REMOTE_DISPLAY);
        permissions.add(permission.BIND_REMOTEVIEWS);
        permissions.add(permission.BIND_RESOLVER_RANKER_SERVICE);
        permissions.add(permission.BIND_ROUTE_PROVIDER);
        permissions.add(permission.BIND_RUNTIME_PERMISSION_PRESENTER_SERVICE);
        permissions.add(permission.BIND_SCREENING_SERVICE);
        permissions.add(permission.BIND_SETTINGS_SUGGESTIONS_SERVICE);
        permissions.add(permission.BIND_SOUND_TRIGGER_DETECTION_SERVICE);
        permissions.add(permission.BIND_TELECOM_CONNECTION_SERVICE);
        permissions.add(permission.BIND_TELEPHONY_DATA_SERVICE);
        permissions.add(permission.BIND_TELEPHONY_NETWORK_SERVICE);
        permissions.add(permission.BIND_TEXT_SERVICE);
        permissions.add(permission.BIND_TEXTCLASSIFIER_SERVICE);
        permissions.add(permission.BIND_TRUST_AGENT);
        permissions.add(permission.BIND_TV_INPUT);
        permissions.add(permission.BIND_TV_REMOTE_SERVICE);
        permissions.add(permission.BIND_VISUAL_VOICEMAIL_SERVICE);
        permissions.add(permission.BIND_VOICE_INTERACTION);
        permissions.add(permission.BIND_VPN_SERVICE);
        permissions.add(permission.BIND_VR_LISTENER_SERVICE);
        permissions.add(permission.BIND_WALLPAPER);
        permissions.add(permission.BRIGHTNESS_SLIDER_USAGE);
        permissions.add(permission.CAMERA_SEND_SYSTEM_EVENTS);
        permissions.add(permission.CAPTURE_AUDIO_OUTPUT);//FAILED  to call setAudioSource(Runtime Exectiopn)
        permissions.add(permission.CAPTURE_SECURE_VIDEO_OUTPUT);
        permissions.add(permission.CAPTURE_VIDEO_OUTPUT);
        permissions.add(permission.CHANGE_APP_IDLE_STATE);
        permissions.add(permission.CHANGE_COMPONENT_ENABLED_STATE);//Illegal Argument
        permissions.add(permission.CHANGE_CONFIGURATION);
        permissions.add(permission.CLEAR_APP_CACHE);
        permissions.add(permission.CLEAR_APP_USER_DATA);
        permissions.add(permission.CONFIGURE_DISPLAY_BRIGHTNESS);
        permissions.add(permission.CONFIGURE_DISPLAY_COLOR_MODE);
        permissions.add(permission.CONFIGURE_WIFI_DISPLAY);
        permissions.add(permission.CONFIRM_FULL_BACKUP);
        permissions.add(permission.CONNECTIVITY_INTERNAL);
        permissions.add(permission.CONNECTIVITY_USE_RESTRICTED_NETWORKS);//NPE
        permissions.add(permission.CONTROL_DISPLAY_BRIGHTNESS);
        permissions.add(permission.CONTROL_DISPLAY_SATURATION);
        permissions.add(permission.CONTROL_KEYGUARD);
        permissions.add(permission.CONTROL_LOCATION_UPDATES);
        permissions.add(permission.CONTROL_REMOTE_APP_TRANSITION_ANIMATIONS);//NPE
        permissions.add(permission.CONTROL_VPN);
        permissions.add(permission.CREATE_USERS);
        permissions.add(permission.CRYPT_KEEPER);//Transaction ID error
        permissions.add(permission.DELETE_PACKAGES);//Illegal arguments(package not found)
        permissions.add(permission.DEVICE_POWER);
        permissions.add(permission.DISABLE_INPUT_DEVICE);
        permissions.add(permission.DUMP);
        permissions.add(permission.FILTER_EVENTS);
        permissions.add(permission.FORCE_BACK);
        permissions.add(permission.FORCE_PERSISTABLE_URI_PERMISSIONS);//NPE
        permissions.add(permission.FORCE_STOP_PACKAGES);
        permissions.add(permission.FRAME_STATS);
        permissions.add(permission.FREEZE_SCREEN);
        permissions.add(permission.GET_ACCOUNTS_PRIVILEGED);
        permissions.add(permission.GET_APP_GRANTED_URI_PERMISSIONS);
        permissions.add(permission.GET_APP_OPS_STATS);
        permissions.add(permission.GET_INTENT_SENDER_INTENT);
        permissions.add(permission.GET_TOP_ACTIVITY_INFO);
        permissions.add(permission.HARDWARE_TEST);//NoSuchElementException?
        permissions.add(permission.INSTALL_GRANT_RUNTIME_PERMISSIONS);
        permissions.add(permission.INSTALL_PACKAGES);
        permissions.add(permission.INTENT_FILTER_VERIFICATION_AGENT);
        permissions.add(permission.INTERACT_ACROSS_USERS);
        permissions.add(permission.INTERACT_ACROSS_USERS_FULL);
        permissions.add(permission.INTERNAL_DELETE_CACHE_FILES);
        permissions.add(permission.INTERNAL_SYSTEM_WINDOW);
        permissions.add(permission.KILL_UID);
        permissions.add(permission.LOCAL_MAC_ADDRESS);//FAILED getAddress:Need android.permission.BLUETOOTH_CONNECT
        permissions.add(permission.LOCATION_HARDWARE);
        permissions.add(permission.MANAGE_ACTIVITY_STACKS);
        permissions.add(permission.MANAGE_APP_OPS_MODES);
        permissions.add(permission.MANAGE_APP_OPS_RESTRICTIONS);
        permissions.add(permission.MANAGE_APP_TOKENS);
        permissions.add(permission.MANAGE_AUTO_FILL);
        permissions.add(permission.MANAGE_CA_CERTIFICATES);
        permissions.add(permission.MANAGE_COMPANION_DEVICES);
        permissions.add(permission.MANAGE_DEVICE_ADMINS);
        permissions.add(permission.MANAGE_DOCUMENTS);//java.lang.UnsupportedOperationException
        permissions.add(permission.MANAGE_FINGERPRINT);
        permissions.add(permission.MANAGE_MEDIA_PROJECTION);
        permissions.add(permission.MANAGE_NETWORK_POLICY);
        permissions.add(permission.MANAGE_NOTIFICATIONS);
        permissions.add(permission.MANAGE_PROFILE_AND_DEVICE_OWNERS);//java.lang.IllegalStateException
        permissions.add(permission.MANAGE_SENSORS);
        permissions.add(permission.MANAGE_SLICE_PERMISSIONS);
        permissions.add(permission.MANAGE_SOUND_TRIGGER);//Failed to obtain permission android.permission.RECORD_AUDIO
        permissions.add(permission.MANAGE_SUBSCRIPTION_PLANS);
        permissions.add(permission.MANAGE_USB);
        permissions.add(permission.MANAGE_USERS);
        permissions.add(permission.MASTER_CLEAR);
        permissions.add(permission.MEDIA_CONTENT_CONTROL);
        permissions.add(permission.MODIFY_ACCESSIBILITY_DATA);
        permissions.add(permission.MODIFY_APPWIDGET_BIND_PERMISSIONS);
        permissions.add(permission.MODIFY_AUDIO_ROUTING);
        permissions.add(permission.MODIFY_PHONE_STATE);
        permissions.add(permission.MODIFY_QUIET_MODE);
        permissions.add(permission.MOUNT_FORMAT_FILESYSTEMS);//.ServiceSpecificException: Failed to find volume test (code 61)
        permissions.add(permission.MOUNT_UNMOUNT_FILESYSTEMS);
        permissions.add(permission.MOVE_PACKAGE);
        permissions.add(permission.NETWORK_SETTINGS);
        permissions.add(permission.NETWORK_STACK);
        permissions.add(permission.NOTIFY_PENDING_SYSTEM_UPDATE);
        permissions.add(permission.OBSERVE_APP_USAGE);
        permissions.add(permission.OBSERVE_GRANT_REVOKE_PERMISSIONS);//NPE
        permissions.add(permission.OVERRIDE_WIFI_CONFIG);
        permissions.add(permission.PACKAGE_USAGE_STATS);
        permissions.add(permission.PACKAGE_VERIFICATION_AGENT);
        permissions.add(permission.PACKET_KEEPALIVE_OFFLOAD);//NPE
        permissions.add(permission.QUERY_DO_NOT_ASK_CREDENTIALS_ON_BOOT);
        permissions.add(permission.READ_BLOCKED_NUMBERS);
        permissions.add(permission.READ_DREAM_STATE);
        permissions.add(permission.READ_FRAME_BUFFER);
        permissions.add(permission.READ_LOGS);
        permissions.add(permission.READ_NETWORK_USAGE_HISTORY);
        permissions.add(permission.READ_PRECISE_PHONE_STATE);
        permissions.add(permission.READ_PRINT_SERVICE_RECOMMENDATIONS);
        permissions.add(permission.READ_PRINT_SERVICES);
        permissions.add(permission.READ_PRIVILEGED_PHONE_STATE);
        permissions.add(permission.READ_SEARCH_INDEXABLES);//UnsupportedOperationException
        permissions.add(permission.READ_WIFI_CREDENTIAL);
        permissions.add(permission.REBOOT);
        permissions.add(permission.REGISTER_SIM_SUBSCRIPTION);
        permissions.add(permission.REGISTER_WINDOW_MANAGER_LISTENERS);
        permissions.add(permission.REMOTE_AUDIO_PLAYBACK);
        permissions.add(permission.REMOVE_TASKS);
        permissions.add(permission.RESET_FINGERPRINT_LOCKOUT);
        permissions.add(permission.RESET_SHORTCUT_MANAGER_THROTTLING);
        permissions.add(permission.RESTRICTED_VR_ACCESS);
        permissions.add(permission.RETRIEVE_WINDOW_CONTENT);
        permissions.add(permission.RETRIEVE_WINDOW_TOKEN);
        permissions.add(permission.REVOKE_RUNTIME_PERMISSIONS);
        permissions.add(permission.SET_ACTIVITY_WATCHER);
        permissions.add(permission.SET_ALWAYS_FINISH);
        permissions.add(permission.SET_ANIMATION_SCALE);
        permissions.add(permission.SET_DEBUG_APP);
        permissions.add(permission.SET_HARMFUL_APP_WARNINGS);
        permissions.add(permission.SET_INPUT_CALIBRATION);//NPE
        permissions.add(permission.SET_KEYBOARD_LAYOUT);//NPE
        permissions.add(permission.SET_MEDIA_KEY_LISTENER);
        permissions.add(permission.SET_ORIENTATION);
        permissions.add(permission.SET_POINTER_SPEED);
        permissions.add(permission.SET_PREFERRED_APPLICATIONS);
        permissions.add(permission.SET_PROCESS_LIMIT);
        permissions.add(permission.SET_SCREEN_COMPATIBILITY);
        permissions.add(permission.SET_TIME);
        permissions.add(permission.SET_TIME_ZONE);
        permissions.add(permission.SET_VOLUME_KEY_LONG_PRESS_LISTENER);
        permissions.add(permission.SET_WALLPAPER_COMPONENT);
        permissions.add(permission.SHOW_KEYGUARD_MESSAGE);
        permissions.add(permission.SHUTDOWN);
        permissions.add(permission.SIGNAL_PERSISTENT_PROCESSES);
//        permissions.add(permission.START_ANY_ACTIVITY);//The test shows bottom sheet dialogue
        permissions.add(permission.START_TASKS_FROM_RECENTS);//Illegal Argument Exception
        permissions.add(permission.STATUS_BAR);
        permissions.add(permission.STATUS_BAR_SERVICE);
        permissions.add(permission.STOP_APP_SWITCHES);
        permissions.add(permission.STORAGE_INTERNAL);
        permissions.add(permission.SUSPEND_APPS);
        permissions.add(permission.TABLET_MODE);
        permissions.add(permission.TEMPORARY_ENABLE_ACCESSIBILITY);
        permissions.add(permission.TETHER_PRIVILEGED);
        permissions.add(permission.TRUST_LISTENER);
        permissions.add(permission.UPDATE_APP_OPS_STATS);
        permissions.add(permission.UPDATE_DEVICE_STATS);
        permissions.add(permission.UPDATE_LOCK_TASK_PACKAGES);
        permissions.add(permission.USER_ACTIVITY);
        permissions.add(permission.WRITE_APN_SETTINGS);
        permissions.add(permission.WRITE_BLOCKED_NUMBERS);
        permissions.add(permission.WRITE_DREAM_STATE);
        permissions.add(permission.WRITE_EMBEDDED_SUBSCRIPTIONS);
        permissions.add(permission.WRITE_SECURE_SETTINGS);
        permissions.add(permission.READ_VOICEMAIL);//SecruityException
        permissions.add(permission.WRITE_VOICEMAIL);//SecurityException
    }

    //Add all signature permission tests for SDK29 
    private void addPermissionsFor29(List<String> permissions)
    {
        permissions.add(permission.ACCESS_SHARED_LIBRARIES);
        permissions.add(permission.ADJUST_RUNTIME_PERMISSIONS_POLICY);
        permissions.add(permission.BIND_ATTENTION_SERVICE);
        permissions.add(permission.BIND_AUGMENTED_AUTOFILL_SERVICE);
        permissions.add(permission.BIND_CALL_REDIRECTION_SERVICE);
        permissions.add(permission.BIND_CARRIER_MESSAGING_CLIENT_SERVICE);
        permissions.add(permission.BIND_CONTENT_CAPTURE_SERVICE);
        permissions.add(permission.BIND_CONTENT_SUGGESTIONS_SERVICE);
        permissions.add(permission.BIND_EXPLICIT_HEALTH_CHECK_SERVICE);
        permissions.add(permission.BIND_PHONE_ACCOUNT_SUGGESTION_SERVICE);
        permissions.add(permission.CONTROL_ALWAYS_ON_VPN);
        permissions.add(permission.CONTROL_KEYGUARD_SECURE_NOTIFICATIONS);
        permissions.add(permission.GET_RUNTIME_PERMISSIONS);
        permissions.add(permission.GRANT_PROFILE_OWNER_DEVICE_IDS_ACCESS);//Test Faiure:Reflection
        //permissions.add(permission.LOCK_DEVICE);//The test locks screen
        permissions.add(permission.MANAGE_ACCESSIBILITY);
        permissions.add(permission.MANAGE_APP_PREDICTIONS);
        permissions.add(permission.MANAGE_APPOPS);
        permissions.add(permission.MANAGE_BIOMETRIC);
        permissions.add(permission.MANAGE_BIOMETRIC_DIALOG);
        permissions.add(permission.MANAGE_CONTENT_CAPTURE);
        permissions.add(permission.MANAGE_ROLE_HOLDERS);
        permissions.add(permission.MANAGE_ROLLBACKS);
        permissions.add(permission.MANAGE_SENSOR_PRIVACY);
        permissions.add(permission.MANAGE_TEST_NETWORKS);
        permissions.add(permission.MONITOR_INPUT);
        permissions.add(permission.NETWORK_MANAGED_PROVISIONING);
        permissions.add(permission.NETWORK_SCAN);
        permissions.add(permission.OBSERVE_ROLE_HOLDERS);
        permissions.add(permission.OPEN_APP_OPEN_BY_DEFAULT_SETTINGS);
        permissions.add(permission.POWER_SAVER);
        permissions.add(permission.READ_DEVICE_CONFIG);
        permissions.add(permission.RESET_PASSWORD);//Failed to RESET_PASSWORD
        //permissions.add(permission.REVIEW_ACCESSIBILITY_SERVICES);//The test displays alertbox
        permissions.add(permission.SEND_DEVICE_CUSTOMIZATION_READY);
        //permissions.add(permission.START_VIEW_PERMISSION_USAGE);//The test displays help page
        permissions.add(permission.TEST_MANAGE_ROLLBACKS);
        permissions.add(permission.USE_BIOMETRIC_INTERNAL);
        permissions.add(permission.WHITELIST_RESTRICTED_PERMISSIONS);
        permissions.add(permission.WIFI_SET_DEVICE_MOBILITY_STATE);
        permissions.add(permission.WIFI_UPDATE_USABILITY_STATS_SCORE);
        permissions.add(permission.WRITE_DEVICE_CONFIG);
        permissions.add(permission.WRITE_SETTINGS_HOMEPAGE_DATA);
        permissions.add(permission.INSTALL_EXISTING_PACKAGES);
    }

    //Add all signature permission tests for SDK30 
    private void addPermissionsFor30(List<String> permissions)
    {
        permissions.add(permission.ACCESS_CONTEXT_HUB);
        permissions.add(permission.ACCESS_LOCUS_ID_USAGE_STATS);
        permissions.add(permission.ACCESS_MESSAGES_ON_ICC);//FAILED:Secuirty Exception(RECEIVE_SMS)
        permissions.add(permission.ACCESS_VIBRATOR_STATE);
        permissions.add(permission.ADD_TRUSTED_DISPLAY);
        permissions.add(permission.ASSOCIATE_INPUT_DEVICE_TO_DISPLAY_BY_PORT);
        permissions.add(permission.BIND_CELL_BROADCAST_SERVICE);
        permissions.add(permission.BIND_CONTROLS);
        permissions.add(permission.BIND_EXTERNAL_STORAGE_SERVICE);
        permissions.add(permission.BIND_INLINE_SUGGESTION_RENDER_SERVICE);
        permissions.add(permission.BIND_QUICK_ACCESS_WALLET_SERVICE);
        permissions.add(permission.COMPANION_APPROVE_WIFI_CONNECTIONS);
        permissions.add(permission.CONFIGURE_INTERACT_ACROSS_PROFILES);
        permissions.add(permission.CONTROL_DEVICE_LIGHTS);
        permissions.add(permission.ENTER_CAR_MODE_PRIORITIZED);
        permissions.add(permission.KEYPHRASE_ENROLLMENT_APPLICATION);//Illegal Arguments
        permissions.add(permission.LISTEN_ALWAYS_REPORTED_SIGNAL_STRENGTH);
        permissions.add(permission.LOG_COMPAT_CHANGE);
        permissions.add(permission.MANAGE_EXTERNAL_STORAGE);
        permissions.add(permission.MARK_DEVICE_ORGANIZATION_OWNED);//Transaction ID - markProfileOwnerOnOrganizationOwnedDevice
        permissions.add(permission.MEDIA_RESOURCE_OVERRIDE_PID);
        permissions.add(permission.MODIFY_SETTINGS_OVERRIDEABLE_BY_RESTORE);
        permissions.add(permission.MONITOR_DEVICE_CONFIG_ACCESS);
        permissions.add(permission.NETWORK_AIRPLANE_MODE);
        permissions.add(permission.NETWORK_FACTORY);
        permissions.add(permission.NETWORK_STATS_PROVIDER);//NPE
        permissions.add(permission.OBSERVE_NETWORK_POLICY);
        permissions.add(permission.OVERRIDE_COMPAT_CHANGE_CONFIG);
        permissions.add(permission.PEEK_DROPBOX_DATA);//FAILED:PEEK_DROPBOX_DATA
        permissions.add(permission.RADIO_SCAN_WITHOUT_LOCATION);
        permissions.add(permission.READ_COMPAT_CHANGE_CONFIG);
        permissions.add(permission.RESTORE_RUNTIME_PERMISSIONS);
        permissions.add(permission.SECURE_ELEMENT_PRIVILEGED_OPERATION);
        permissions.add(permission.SET_INITIAL_LOCK);
        permissions.add(permission.SYSTEM_CAMERA);
        permissions.add(permission.UPGRADE_RUNTIME_PERMISSIONS);
        permissions.add(permission.VIBRATE_ALWAYS_ON);
        permissions.add(permission.WHITELIST_AUTO_REVOKE_PERMISSIONS);
        permissions.add(permission.USE_INSTALLER_V2);
    }

    //Add all signature permission tests for SDK31 
    private void addPermissionsFor31(List<String> permissions)
    {
        permissions.add(permission.ACCESS_TUNED_INFO);
        permissions.add(permission.ASSOCIATE_INPUT_DEVICE_TO_DISPLAY);
        permissions.add(permission.BATTERY_PREDICTION);//need to be unpluged for testing?
        permissions.add(permission.BIND_CALL_DIAGNOSTIC_SERVICE);
        permissions.add(permission.BIND_COMPANION_DEVICE_SERVICE);
        permissions.add(permission.BIND_DISPLAY_HASHING_SERVICE);
        permissions.add(permission.BIND_DOMAIN_VERIFICATION_AGENT);
        permissions.add(permission.BIND_GBA_SERVICE);
        permissions.add(permission.BIND_HOTWORD_DETECTION_SERVICE);
        permissions.add(permission.BIND_MUSIC_RECOGNITION_SERVICE);
        permissions.add(permission.BIND_RESUME_ON_REBOOT_SERVICE);
        permissions.add(permission.BIND_ROTATION_RESOLVER_SERVICE);
        permissions.add(permission.BIND_TIME_ZONE_PROVIDER_SERVICE);
        permissions.add(permission.BIND_TRANSLATION_SERVICE);
        permissions.add(permission.BROADCAST_CLOSE_SYSTEM_DIALOGS);
        permissions.add(permission.CAMERA_INJECT_EXTERNAL_CAMERA);
        permissions.add(permission.CLEAR_FREEZE_PERIOD);
        permissions.add(permission.CONTROL_DEVICE_STATE);//CONTROL_DEVICE_STATE FAILURE
        permissions.add(permission.FORCE_DEVICE_POLICY_MANAGER_LOGS);//Illegas State
        permissions.add(permission.GET_PEOPLE_TILE_PREVIEW);//NPE
        permissions.add(permission.INPUT_CONSUMER);
        permissions.add(permission.KEEP_UNINSTALLED_PACKAGES);
        permissions.add(permission.MANAGE_ACTIVITY_TASKS);
        permissions.add(permission.MANAGE_CREDENTIAL_MANAGEMENT_APP);
        permissions.add(permission.MANAGE_GAME_MODE);
        permissions.add(permission.MANAGE_MUSIC_RECOGNITION);//NPE
        permissions.add(permission.MANAGE_NOTIFICATION_LISTENERS);
        permissions.add(permission.MANAGE_ONGOING_CALLS);
        permissions.add(permission.MANAGE_SMARTSPACE);
        permissions.add(permission.MANAGE_SPEECH_RECOGNITION);
        permissions.add(permission.MANAGE_TIME_AND_ZONE_DETECTION);
        permissions.add(permission.MANAGE_TOAST_RATE_LIMITING);
        permissions.add(permission.MANAGE_UI_TRANSLATION);
        permissions.add(permission.MANAGE_WIFI_COUNTRY_CODE);//Illegal argument exception
        permissions.add(permission.MODIFY_REFRESH_RATE_SWITCHING_TYPE);
        permissions.add(permission.NFC_SET_CONTROLLER_ALWAYS_ON);
        permissions.add(permission.OVERRIDE_COMPAT_CHANGE_CONFIG_ON_RELEASE_BUILD);
        permissions.add(permission.OVERRIDE_DISPLAY_MODE_REQUESTS);
        permissions.add(permission.QUERY_AUDIO_STATE);
        permissions.add(permission.READ_DREAM_SUPPRESSION);
        permissions.add(permission.READ_NEARBY_STREAMING_POLICY);
        permissions.add(permission.READ_PEOPLE_DATA);
        permissions.add(permission.READ_PROJECTION_STATE);
        permissions.add(permission.REGISTER_MEDIA_RESOURCE_OBSERVER);//Binder Transaction error(-22)
        permissions.add(permission.RESET_APP_ERRORS);
        permissions.add(permission.RESTART_WIFI_SUBSYSTEM);
        permissions.add(permission.SCHEDULE_PRIORITIZED_ALARM);
        permissions.add(permission.SEND_CATEGORY_CAR_NOTIFICATIONS);
        permissions.add(permission.SET_AND_VERIFY_LOCKSCREEN_CREDENTIALS);//Illegal arg
        permissions.add(permission.SET_CLIP_SOURCE);
        permissions.add(permission.SIGNAL_REBOOT_READINESS);//NPE
        permissions.add(permission.SOUNDTRIGGER_DELEGATE_IDENTITY);//NPE
        permissions.add(permission.SUGGEST_EXTERNAL_TIME);//NPE
        permissions.add(permission.TEST_BIOMETRIC);
        permissions.add(permission.TOGGLE_AUTOMOTIVE_PROJECTION);
        permissions.add(permission.UPDATE_DOMAIN_VERIFICATION_USER_SELECTION);
        permissions.add(permission.UPDATE_FONTS);
        permissions.add(permission.USE_ICC_AUTH_WITH_DEVICE_IDENTIFIER);
        permissions.add(permission.UWB_PRIVILEGED);
        permissions.add(permission.WIFI_ACCESS_COEX_UNSAFE_CHANNELS);//Illegal Argument
        permissions.add(permission.WIFI_UPDATE_COEX_UNSAFE_CHANNELS);
    }

    //Add all signature permission tests for SDK32 
    private void addPermissionsFor32(List<String> permissions)
    {
        permissions.add(permission.ALLOW_SLIPPERY_TOUCHES);
        permissions.add(permission.TRIGGER_SHELL_PROFCOLLECT_UPLOAD);
    }

    //Add all signature permission tests for SDK33 
    private void addPermissionsFor33(List<String> permissions)
    {
        permissions.add(permission.ACCESS_BROADCAST_RESPONSE_STATS);
        permissions.add(permission.ACCESS_FPS_COUNTER);
        permissions.add(permission.ACCESS_ULTRASOUND);
        permissions.add(permission.BIND_AMBIENT_CONTEXT_DETECTION_SERVICE);
        permissions.add(permission.BIND_ATTESTATION_VERIFICATION_SERVICE);
        permissions.add(permission.BIND_GAME_SERVICE);
        permissions.add(permission.BIND_SELECTION_TOOLBAR_RENDER_SERVICE);
        permissions.add(permission.BIND_TRACE_REPORT_SERVICE);
        permissions.add(permission.BIND_TV_INTERACTIVE_APP);
        permissions.add(permission.BIND_WALLPAPER_EFFECTS_GENERATION_SERVICE);
        permissions.add(permission.CALL_AUDIO_INTERCEPTION);
        permissions.add(permission.CHANGE_APP_LAUNCH_TIME_ESTIMATE);
        permissions.add(permission.CONTROL_AUTOMOTIVE_GNSS);
        permissions.add(permission.LAUNCH_DEVICE_MANAGER_SETUP);//UI:Call an activity on support tool
        permissions.add(permission.LOCATION_BYPASS);
        permissions.add(permission.MAKE_UID_VISIBLE);
        permissions.add(permission.MANAGE_CLOUDSEARCH);
        permissions.add(permission.MANAGE_GAME_ACTIVITY);
        permissions.add(permission.MANAGE_LOW_POWER_STANDBY);
        permissions.add(permission.MANAGE_WALLPAPER_EFFECTS_GENERATION);
        permissions.add(permission.MANAGE_WEAK_ESCROW_TOKEN);
        permissions.add(permission.MANAGE_WIFI_INTERFACES);
        permissions.add(permission.MANAGE_WIFI_NETWORK_SELECTION);
        permissions.add(permission.MODIFY_USER_PREFERRED_DISPLAY_MODE);
        permissions.add(permission.PROVISION_DEMO_DEVICE);
        permissions.add(permission.QUERY_ADMIN_POLICY);
        permissions.add(permission.QUERY_USERS);
        permissions.add(permission.READ_APP_SPECIFIC_LOCALES);
        permissions.add(permission.READ_SAFETY_CENTER_STATUS);
        permissions.add(permission.REQUEST_COMPANION_PROFILE_APP_STREAMING);
        permissions.add(permission.REQUEST_COMPANION_PROFILE_COMPUTER);
        permissions.add(permission.REQUEST_COMPANION_SELF_MANAGED);
        permissions.add(permission.REQUEST_UNIQUE_ID_ATTESTATION);
        permissions.add(permission.REVOKE_POST_NOTIFICATIONS_WITHOUT_KILL);
        permissions.add(permission.SET_GAME_SERVICE);
        permissions.add(permission.SET_SYSTEM_AUDIO_CAPTION);
        permissions.add(permission.SET_WALLPAPER_DIM_AMOUNT);
        permissions.add(permission.START_REVIEW_PERMISSION_DECISIONS);
        permissions.add(permission.TIS_EXTENSION_INTERFACE);
        permissions.add(permission.TRIGGER_LOST_MODE);
        permissions.add(permission.UPDATE_DEVICE_MANAGEMENT_RESOURCES);
        permissions.add(permission.USE_ATTESTATION_VERIFICATION_SERVICE);
    }

    @Override
    public int getButtonTextId() {
        return R.string.run_permission_dependent_tests;
    }
}
