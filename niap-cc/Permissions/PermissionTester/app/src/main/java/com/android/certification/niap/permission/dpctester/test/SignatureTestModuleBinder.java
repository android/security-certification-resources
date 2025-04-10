package com.android.certification.niap.permission.dpctester.test;
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
import android.app.Activity;

import androidx.annotation.NonNull;

import com.android.certification.niap.permission.dpctester.test.runner.SignaturePermissionTestModuleBase;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTest;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestModule;

@PermissionTestModule(name="Binder Test Cases",label = "Run Signature Binder Test",sync =true,prflabel = "BIND_* Permissions")
public class SignatureTestModuleBinder extends SignaturePermissionTestModuleBase {
    public SignatureTestModuleBinder(@NonNull Activity activity) {
        super(activity);
    }

    @PermissionTest(permission="BIND_INCALL_SERVICE")
    public void testBindIncallService(){
        runBindRunnable("BIND_INCALL_SERVICE");
    }

    @PermissionTest(permission="BIND_PRINT_RECOMMENDATION_SERVICE")
    public void testBindPrintRecommendationService(){
        runBindRunnable("BIND_PRINT_RECOMMENDATION_SERVICE");
    }

    @PermissionTest(permission="BIND_KEYGUARD_APPWIDGET")
    public void testBindKeyguardAppwidget(){
        runBindRunnable("BIND_KEYGUARD_APPWIDGET");
    }

    @PermissionTest(permission="BIND_DEVICE_ADMIN")
    public void testBindDeviceAdmin(){
        runBindRunnable("BIND_DEVICE_ADMIN");
    }

    @PermissionTest(permission="BIND_PRINT_SERVICE")
    public void testBindPrintService(){
        runBindRunnable("BIND_PRINT_SERVICE");
    }

    @PermissionTest(permission="BIND_RUNTIME_PERMISSION_PRESENTER_SERVICE")
    public void testBindRuntimePermissionPresenterService(){
        runBindRunnable("BIND_RUNTIME_PERMISSION_PRESENTER_SERVICE");
    }

    @PermissionTest(permission="BIND_VR_LISTENER_SERVICE")
    public void testBindVrListenerService(){
        runBindRunnable("BIND_VR_LISTENER_SERVICE");
    }

    @PermissionTest(permission="BIND_DREAM_SERVICE")
    public void testBindDreamService(){
        runBindRunnable("BIND_DREAM_SERVICE");
    }

    @PermissionTest(permission="BIND_CARRIER_SERVICES")
    public void testBindCarrierServices(){
        runBindRunnable("BIND_CARRIER_SERVICES");
    }

    @PermissionTest(permission="BIND_QUICK_SETTINGS_TILE")
    public void testBindQuickSettingsTile(){
        runBindRunnable("BIND_QUICK_SETTINGS_TILE");
    }

    @PermissionTest(permission="BIND_TV_INPUT")
    public void testBindTvInput(){
        runBindRunnable("BIND_TV_INPUT");
    }

    @PermissionTest(permission="BIND_AUTOFILL")
    public void testBindAutofill(){
        runBindRunnable("BIND_AUTOFILL");
    }

    @PermissionTest(permission="BIND_WALLPAPER")
    public void testBindWallpaper(){
        runBindRunnable("BIND_WALLPAPER");
    }

    @PermissionTest(permission="BIND_INTENT_FILTER_VERIFIER")
    public void testBindIntentFilterVerifier(){
        runBindRunnable("BIND_INTENT_FILTER_VERIFIER");
    }

    @PermissionTest(permission="BIND_TELECOM_CONNECTION_SERVICE")
    public void testBindTelecomConnectionService(){
        runBindRunnable("BIND_TELECOM_CONNECTION_SERVICE");
    }

    @PermissionTest(permission="BIND_VOICE_INTERACTION")
    public void testBindVoiceInteraction(){
        runBindRunnable("BIND_VOICE_INTERACTION");
    }

    @PermissionTest(permission="BIND_CACHE_QUOTA_SERVICE")
    public void testBindCacheQuotaService(){
        runBindRunnable("BIND_CACHE_QUOTA_SERVICE");
    }

    @PermissionTest(permission="BIND_RESOLVER_RANKER_SERVICE")
    public void testBindResolverRankerService(){
        runBindRunnable("BIND_RESOLVER_RANKER_SERVICE");
    }

    @PermissionTest(permission="BIND_CONNECTION_SERVICE")
    public void testBindConnectionService(){
        runBindRunnable("BIND_CONNECTION_SERVICE");
    }

    @PermissionTest(permission="BIND_VPN_SERVICE")
    public void testBindVpnService(){
        runBindRunnable("BIND_VPN_SERVICE");
    }

    @PermissionTest(permission="BIND_APPWIDGET")
    public void testBindAppwidget(){
        runBindRunnable("BIND_APPWIDGET");
    }

    @PermissionTest(permission="BIND_NOTIFICATION_LISTENER_SERVICE")
    public void testBindNotificationListenerService(){
        runBindRunnable("BIND_NOTIFICATION_LISTENER_SERVICE");
    }

    @PermissionTest(permission="BIND_SCREENING_SERVICE")
    public void testBindScreeningService(){
        runBindRunnable("BIND_SCREENING_SERVICE");
    }

    @PermissionTest(permission="BIND_MIDI_DEVICE_SERVICE")
    public void testBindMidiDeviceService(){
        runBindRunnable("BIND_MIDI_DEVICE_SERVICE");
    }

    @PermissionTest(permission="BIND_REMOTE_DISPLAY")
    public void testBindRemoteDisplay(){
        runBindRunnable("BIND_REMOTE_DISPLAY");
    }

    @PermissionTest(permission="BIND_AUTOFILL_SERVICE")
    public void testBindAutofillService(){
        runBindRunnable("BIND_AUTOFILL_SERVICE");
    }

    @PermissionTest(permission="BIND_JOB_SERVICE")
    public void testBindJobService(){
        runBindRunnable("BIND_JOB_SERVICE");
    }

    @PermissionTest(permission="BIND_COMPANION_DEVICE_MANAGER_SERVICE")
    public void testBindCompanionDeviceManagerService(){
        runBindRunnable("BIND_COMPANION_DEVICE_MANAGER_SERVICE");
    }

    @PermissionTest(permission="BIND_PACKAGE_VERIFIER")
    public void testBindPackageVerifier(){
        runBindRunnable("BIND_PACKAGE_VERIFIER");
    }

    @PermissionTest(permission="BIND_ROUTE_PROVIDER")
    public void testBindRouteProvider(){
        runBindRunnable("BIND_ROUTE_PROVIDER");
    }

    @PermissionTest(permission="BIND_CARRIER_MESSAGING_SERVICE")
    public void testBindCarrierMessagingService(){
        runBindRunnable("BIND_CARRIER_MESSAGING_SERVICE");
    }

    @PermissionTest(permission="BIND_EUICC_SERVICE")
    public void testBindEuiccService(){
        runBindRunnable("BIND_EUICC_SERVICE");
    }

    @PermissionTest(permission="BIND_VISUAL_VOICEMAIL_SERVICE")
    public void testBindVisualVoicemailService(){
        runBindRunnable("BIND_VISUAL_VOICEMAIL_SERVICE");
    }

    @PermissionTest(permission="BIND_TV_REMOTE_SERVICE")
    public void testBindTvRemoteService(){
        runBindRunnable("BIND_TV_REMOTE_SERVICE");
    }

    @PermissionTest(permission="BIND_CONDITION_PROVIDER_SERVICE")
    public void testBindConditionProviderService(){
        runBindRunnable("BIND_CONDITION_PROVIDER_SERVICE");
    }

    @PermissionTest(permission="BIND_AUTOFILL_FIELD_CLASSIFICATION_SERVICE")
    public void testBindAutofillFieldClassificationService(){
        runBindRunnable("BIND_AUTOFILL_FIELD_CLASSIFICATION_SERVICE");
    }

    @PermissionTest(permission="BIND_NOTIFICATION_ASSISTANT_SERVICE")
    public void testBindNotificationAssistantService(){
        runBindRunnable("BIND_NOTIFICATION_ASSISTANT_SERVICE");
    }

    @PermissionTest(permission="BIND_SOUND_TRIGGER_DETECTION_SERVICE")
    public void testBindSoundTriggerDetectionService(){
        runBindRunnable("BIND_SOUND_TRIGGER_DETECTION_SERVICE");
    }

    @PermissionTest(permission="BIND_PRINT_SPOOLER_SERVICE")
    public void testBindPrintSpoolerService(){
        runBindRunnable("BIND_PRINT_SPOOLER_SERVICE");
    }

    @PermissionTest(permission="BIND_DIRECTORY_SEARCH")
    public void testBindDirectorySearch(){
        runBindRunnable("BIND_DIRECTORY_SEARCH");
    }

    @PermissionTest(permission="BIND_TELEPHONY_NETWORK_SERVICE")
    public void testBindTelephonyNetworkService(){
        runBindRunnable("BIND_TELEPHONY_NETWORK_SERVICE");
    }

    @PermissionTest(permission="BIND_SETTINGS_SUGGESTIONS_SERVICE")
    public void testBindSettingsSuggestionsService(){
        runBindRunnable("BIND_SETTINGS_SUGGESTIONS_SERVICE");
    }

    @PermissionTest(permission="BIND_TRUST_AGENT")
    public void testBindTrustAgent(){
        runBindRunnable("BIND_TRUST_AGENT");
    }

    @PermissionTest(permission="BIND_REMOTEVIEWS")
    public void testBindRemoteviews(){
        runBindRunnable("BIND_REMOTEVIEWS");
    }

    @PermissionTest(permission="BIND_TELEPHONY_DATA_SERVICE")
    public void testBindTelephonyDataService(){
        runBindRunnable("BIND_TELEPHONY_DATA_SERVICE");
    }

    @PermissionTest(permission="BIND_ACCESSIBILITY_SERVICE")
    public void testBindAccessibilityService(){
        runBindRunnable("BIND_ACCESSIBILITY_SERVICE");
    }

    @PermissionTest(permission="BIND_INPUT_METHOD")
    public void testBindInputMethod(){
        runBindRunnable("BIND_INPUT_METHOD");
    }

    @PermissionTest(permission="BIND_TEXTCLASSIFIER_SERVICE")
    public void testBindTextclassifierService(){
        runBindRunnable("BIND_TEXTCLASSIFIER_SERVICE");
    }

    @PermissionTest(permission="BIND_NFC_SERVICE")
    public void testBindNfcService(){
        runBindRunnable("BIND_NFC_SERVICE");
    }

    @PermissionTest(permission="BIND_IMS_SERVICE")
    public void testBindImsService(){
        runBindRunnable("BIND_IMS_SERVICE");
    }

    @PermissionTest(permission="BIND_TEXT_SERVICE")
    public void testBindTextService(){
        runBindRunnable("BIND_TEXT_SERVICE");
    }

    @PermissionTest(permission="BIND_NETWORK_RECOMMENDATION_SERVICE")
    public void testBindNetworkRecommendationService(){
        runBindRunnable("BIND_NETWORK_RECOMMENDATION_SERVICE");
    }

    @PermissionTest(permission="BIND_CHOOSER_TARGET_SERVICE")
    public void testBindChooserTargetService(){
        runBindRunnable("BIND_CHOOSER_TARGET_SERVICE");
    }


    @PermissionTest(permission="BIND_ATTENTION_SERVICE", sdkMin=29)
    public void testBindAttentionService(){
        runBindRunnable("BIND_ATTENTION_SERVICE");
    }

    @PermissionTest(permission="BIND_CALL_REDIRECTION_SERVICE", sdkMin=29)
    public void testBindCallRedirectionService(){
        runBindRunnable("BIND_CALL_REDIRECTION_SERVICE");
    }

    @PermissionTest(permission="BIND_CARRIER_MESSAGING_CLIENT_SERVICE", sdkMin=29)
    public void testBindCarrierMessagingClientService(){
        runBindRunnable("BIND_CARRIER_MESSAGING_CLIENT_SERVICE");
    }

    @PermissionTest(permission="BIND_CONTENT_SUGGESTIONS_SERVICE", sdkMin=29)
    public void testBindContentSuggestionsService(){
        runBindRunnable("BIND_CONTENT_SUGGESTIONS_SERVICE");
    }

    @PermissionTest(permission="BIND_PHONE_ACCOUNT_SUGGESTION_SERVICE", sdkMin=29)
    public void testBindPhoneAccountSuggestionService(){
        runBindRunnable("BIND_PHONE_ACCOUNT_SUGGESTION_SERVICE");
    }

    @PermissionTest(permission="BIND_EXPLICIT_HEALTH_CHECK_SERVICE", sdkMin=29)
    public void testBindExplicitHealthCheckService(){
        runBindRunnable("BIND_EXPLICIT_HEALTH_CHECK_SERVICE");
    }

    @PermissionTest(permission="BIND_CONTENT_CAPTURE_SERVICE", sdkMin=29)
    public void testBindContentCaptureService(){
        runBindRunnable("BIND_CONTENT_CAPTURE_SERVICE");
    }

    @PermissionTest(permission="BIND_AUGMENTED_AUTOFILL_SERVICE", sdkMin=29)
    public void testBindAugmentedAutofillService(){
        runBindRunnable("BIND_AUGMENTED_AUTOFILL_SERVICE");
    }


    @PermissionTest(permission="BIND_QUICK_ACCESS_WALLET_SERVICE", sdkMin=30)
    public void testBindQuickAccessWalletService(){
        runBindRunnable("BIND_QUICK_ACCESS_WALLET_SERVICE");
    }

    @PermissionTest(permission="BIND_CONTROLS", sdkMin=30)
    public void testBindControls(){
        runBindRunnable("BIND_CONTROLS");
    }

    @PermissionTest(permission="BIND_CELL_BROADCAST_SERVICE", sdkMin=30)
    public void testBindCellBroadcastService(){
        runBindRunnable("BIND_CELL_BROADCAST_SERVICE");
    }

    @PermissionTest(permission="BIND_EXTERNAL_STORAGE_SERVICE", sdkMin=30)
    public void testBindExternalStorageService(){
        runBindRunnable("BIND_EXTERNAL_STORAGE_SERVICE");
    }

    @PermissionTest(permission="BIND_INLINE_SUGGESTION_RENDER_SERVICE", sdkMin=30)
    public void testBindInlineSuggestionRenderService(){
        runBindRunnable("BIND_INLINE_SUGGESTION_RENDER_SERVICE");
    }


    @PermissionTest(permission="BIND_CALL_DIAGNOSTIC_SERVICE", sdkMin=31)
    public void testBindCallDiagnosticService(){
        runBindRunnable("BIND_CALL_DIAGNOSTIC_SERVICE");
    }

    @PermissionTest(permission="BIND_COMPANION_DEVICE_SERVICE", sdkMin=31)
    public void testBindCompanionDeviceService(){
        runBindRunnable("BIND_COMPANION_DEVICE_SERVICE");
    }

    @PermissionTest(permission="BIND_DISPLAY_HASHING_SERVICE", sdkMin=31)
    public void testBindDisplayHashingService(){
        runBindRunnable("BIND_DISPLAY_HASHING_SERVICE");
    }

    @PermissionTest(permission="BIND_DOMAIN_VERIFICATION_AGENT", sdkMin=31)
    public void testBindDomainVerificationAgent(){
        runBindRunnable("BIND_DOMAIN_VERIFICATION_AGENT");
    }

    @PermissionTest(permission="BIND_GBA_SERVICE", sdkMin=31)
    public void testBindGbaService(){
        runBindRunnable("BIND_GBA_SERVICE");
    }

    @PermissionTest(permission="BIND_HOTWORD_DETECTION_SERVICE", sdkMin=31)
    public void testBindHotwordDetectionService(){
        runBindRunnable("BIND_HOTWORD_DETECTION_SERVICE");
    }

    @PermissionTest(permission="BIND_MUSIC_RECOGNITION_SERVICE", sdkMin=31)
    public void testBindMusicRecognitionService(){
        runBindRunnable("BIND_MUSIC_RECOGNITION_SERVICE");
    }

    @PermissionTest(permission="BIND_RESUME_ON_REBOOT_SERVICE", sdkMin=31)
    public void testBindResumeOnRebootService(){
        runBindRunnable("BIND_RESUME_ON_REBOOT_SERVICE");
    }

    @PermissionTest(permission="BIND_ROTATION_RESOLVER_SERVICE", sdkMin=31)
    public void testBindRotationResolverService(){
        runBindRunnable("BIND_ROTATION_RESOLVER_SERVICE");
    }

    @PermissionTest(permission="BIND_TIME_ZONE_PROVIDER_SERVICE", sdkMin=31)
    public void testBindTimeZoneProviderService(){
        runBindRunnable("BIND_TIME_ZONE_PROVIDER_SERVICE");
    }

    @PermissionTest(permission="BIND_TRANSLATION_SERVICE", sdkMin=31)
    public void testBindTranslationService(){
        runBindRunnable("BIND_TRANSLATION_SERVICE");
    }


    @PermissionTest(permission="BIND_ATTESTATION_VERIFICATION_SERVICE", sdkMin=33)
    public void testBindAttestationVerificationService(){
        runBindRunnable("BIND_ATTESTATION_VERIFICATION_SERVICE");
    }

    @PermissionTest(permission="BIND_TRACE_REPORT_SERVICE", sdkMin=33)
    public void testBindTraceReportService(){
        runBindRunnable("BIND_TRACE_REPORT_SERVICE");
    }

    @PermissionTest(permission="BIND_GAME_SERVICE", sdkMin=33)
    public void testBindGameService(){
        runBindRunnable("BIND_GAME_SERVICE");
    }

    @PermissionTest(permission="BIND_SELECTION_TOOLBAR_RENDER_SERVICE", sdkMin=33)
    public void testBindSelectionToolbarRenderService(){
        runBindRunnable("BIND_SELECTION_TOOLBAR_RENDER_SERVICE");
    }

    @PermissionTest(permission="BIND_WALLPAPER_EFFECTS_GENERATION_SERVICE", sdkMin=33)
    public void testBindWallpaperEffectsGenerationService(){
        runBindRunnable("BIND_WALLPAPER_EFFECTS_GENERATION_SERVICE");
    }

    @PermissionTest(permission="BIND_TV_INTERACTIVE_APP", sdkMin=33)
    public void testBindTvInteractiveApp(){
        runBindRunnable("BIND_TV_INTERACTIVE_APP");
    }

    @PermissionTest(permission="BIND_AMBIENT_CONTEXT_DETECTION_SERVICE", sdkMin=33)
    public void testBindAmbientContextDetectionService(){
        runBindRunnable("BIND_AMBIENT_CONTEXT_DETECTION_SERVICE");
    }

    @PermissionTest(permission="BIND_CALL_STREAMING_SERVICE", sdkMin=34)
    public void testBindCallStreamingService(){
        runBindRunnable("BIND_CALL_STREAMING_SERVICE");
    }

    @PermissionTest(permission="BIND_CREDENTIAL_PROVIDER_SERVICE", sdkMin=34)
    public void testBindCredentialProviderService(){
        runBindRunnable("BIND_CREDENTIAL_PROVIDER_SERVICE");
    }

    @PermissionTest(permission="BIND_FIELD_CLASSIFICATION_SERVICE", sdkMin=34)
    public void testBindFieldClassificationService(){
        runBindRunnable("BIND_FIELD_CLASSIFICATION_SERVICE");
    }

    @PermissionTest(permission="BIND_REMOTE_LOCKSCREEN_VALIDATION_SERVICE", sdkMin=34)
    public void testBindRemoteLockscreenValidationService(){
        runBindRunnable("BIND_REMOTE_LOCKSCREEN_VALIDATION_SERVICE");
    }

    @PermissionTest(permission="BIND_SATELLITE_GATEWAY_SERVICE", sdkMin=34)
    public void testBindSatelliteGatewayService() {
        runBindRunnable("BIND_SATELLITE_GATEWAY_SERVICE");
    }
    @PermissionTest(permission="BIND_SATELLITE_SERVICE", sdkMin=34)
    public void testBindSatelliteService(){
        runBindRunnable("BIND_SATELLITE_SERVICE");
    }

    @PermissionTest(permission="BIND_VISUAL_QUERY_DETECTION_SERVICE", sdkMin=34)
    public void testBindVisualQueryDetectionService(){
        runBindRunnable("BIND_VISUAL_QUERY_DETECTION_SERVICE");
    }

    @PermissionTest(permission="BIND_WEARABLE_SENSING_SERVICE", sdkMin=34,sdkMax = 35)
    public void testBindWearableSensingService(){
        runBindRunnable("BIND_WEARABLE_SENSING_SERVICE");
    }

    @PermissionTest(permission="BIND_ON_DEVICE_INTELLIGENCE_SERVICE", sdkMin=35)
    public void testBindOnDeviceIntelligenceService(){
        runBindRunnable("BIND_ON_DEVICE_INTELLIGENCE_SERVICE");
    }

    @PermissionTest(permission="BIND_ON_DEVICE_SANDBOXED_INFERENCE_SERVICE", sdkMin=35,sdkMax = 35)
    public void testBindOnDeviceSandboxedInferenceService(){
        runBindRunnable("BIND_ON_DEVICE_SANDBOXED_INFERENCE_SERVICE");
    }

    @PermissionTest(permission="BIND_TV_AD_SERVICE", sdkMin=35)
    public void testBindTvAdService(){
        runBindRunnable("BIND_TV_AD_SERVICE");
    }

    @PermissionTest(permission="BIND_DOMAIN_SELECTION_SERVICE", sdkMin=35)
    public void testBindDomainSelectionService(){
        runBindRunnable("BIND_DOMAIN_SELECTION_SERVICE");
    }

    @PermissionTest(permission="BIND_POPULATION_DENSITY_PROVIDER_SERVICE",sdkMin=36)
    public void testBindPopulationDensityProviderService(){
        runBindRunnable("BIND_POPULATION_DENSITY_PROVIDER_SERVICE");
        //logger.debug("The test for android.permission.BIND_POPULATION_DENSITY_PROVIDER_SERVICE is not implemented yet");
    }
    @PermissionTest(permission="BIND_INTRUSION_DETECTION_EVENT_TRANSPORT_SERVICE",sdkMin=36)
    public void testBindIntrusionDetectionEventTransportService(){
        runBindRunnable("BIND_INTRUSION_DETECTION_EVENT_TRANSPORT_SERVICE");
        //logger.debug("The test for android.permission.BIND_INTRUSION_DETECTION_EVENT_TRANSPORT_SERVICE is not implemented yet");
    }
    @PermissionTest(permission="BIND_RKP_SERVICE",sdkMin=36)
    public void testBindRkpService(){
        runBindRunnable("BIND_RKP_SERVICE");
        //logger.debug("The test for android.permission.BIND_RKP_SERVICE is not implemented yet");
    }
    @PermissionTest(permission="BIND_DEPENDENCY_INSTALLER",sdkMin=36)
    public void testBindDependencyInstaller(){
        runBindRunnable("BIND_DEPENDENCY_INSTALLER");
        //logger.debug("The test for android.permission.BIND_DEPENDENCY_INSTALLER is not implemented yet");
    }
    @PermissionTest(permission="BIND_APP_FUNCTION_SERVICE",sdkMin=36)
    public void testBindAppFunctionService(){
        runBindRunnable("BIND_APP_FUNCTION_SERVICE");
        //logger.debug("The test for android.permission.BIND_APP_FUNCTION_SERVICE is not implemented yet");
    }
}
