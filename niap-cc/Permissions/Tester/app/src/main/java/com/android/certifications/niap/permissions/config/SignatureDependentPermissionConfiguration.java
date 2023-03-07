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

import android.os.Build;
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
        permissions.add(permission.RADIO_SCAN_WITHOUT_LOCATION);
        // Starting in Android 12 the NETWORK_SCAN permission behaves similar to
        // RADIO_SCAN_WITHOUT_LOCATION in that it allows a network scan without a location
        // permission.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(permission.NETWORK_SCAN);
        }
        return Optional.of(permissions);
    }

    //Add all signature permission tests for SDK31 
    public static void addPermissionsFor31(List<String> permissions)
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
        //The test crashes the system with TP1A.221105.002 9080065 dev-keys
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
    public static void addPermissionsFor32(List<String> permissions)
    {
        permissions.add(permission.TRIGGER_SHELL_PROFCOLLECT_UPLOAD);
    }

    //Add all signature permission tests for SDK33 
    public static void addPermissionsFor33(List<String> permissions)
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
