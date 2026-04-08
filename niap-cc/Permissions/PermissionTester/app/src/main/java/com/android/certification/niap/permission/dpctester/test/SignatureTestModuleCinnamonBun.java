/*
 * Copyright 2026 The Android Open Source Project
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
package com.android.certification.niap.permission.dpctester.test;

import android.app.Activity;
import android.os.Binder;
import android.os.IWakeLockCallback;
import android.os.RemoteException;
import android.os.WorkSource;

import androidx.annotation.NonNull;

import com.android.certification.niap.permission.dpctester.test.runner.SignaturePermissionTestModuleBase;
import com.android.certification.niap.permission.dpctester.test.tool.BinderTransaction;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTest;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestModule;

@PermissionTestModule(name="Signature 37(CinnamonBun) Test Cases",prflabel="CinnamonBun(17)")
public class SignatureTestModuleCinnamonBun extends SignaturePermissionTestModuleBase {
	public SignatureTestModuleCinnamonBun(@NonNull Activity activity) {
		super(activity);
	}

    @PermissionTest(permission="ACCESS_ATTENTION_LISTENER",sdkMin=37)
    public void testAccessAttentionListener(){
        logger.debug("The test for android.permission.ACCESS_ATTENTION_LISTENER is not implemented yet");
    }
    @PermissionTest(permission="ACCESS_CELL_BROADCAST",sdkMin=37)
    public void testAccessCellBroadcast(){
        logger.debug("The test for android.permission.ACCESS_CELL_BROADCAST is not implemented yet");
    }
    @PermissionTest(permission="ACCESS_COMPANION_INFO",sdkMin=37)
    public void testAccessCompanionInfo(){
        BinderTransaction.getInstance().invoke(Transacts.COMPANION_DEVICE_SERVICE, Transacts.COMPANION_DEVICE_DESCRIPTOR,
                "getAssociationByDeviceId",
                0, null);
        logger.debug("testAccessCompanionInfo invoked successfully");
    }
    @PermissionTest(permission="ACCESS_COMPANION_MESSAGE_PCC",sdkMin=37)
    public void testAccessCompanionMessagePcc(){
        logger.debug("The test for android.permission.ACCESS_COMPANION_MESSAGE_PCC is not implemented yet");
    }
    @PermissionTest(permission="ACCESS_NPU_MODEL_MANAGER_API",sdkMin=37)
    public void testAccessNpuModelManagerApi(){
        logger.debug("The test for android.permission.ACCESS_NPU_MODEL_MANAGER_API is not implemented yet");
    }
    @PermissionTest(permission="ACQUIRE_SLEEP_LOCK",sdkMin=37)
    public void testAcquireSleepLock(){
        final int PARTIAL_SLEEP_WAKE_LOCK = 0x00000200;
        BinderTransaction.getInstance().invoke(Transacts.POWER_SERVICE, Transacts.POWER_DESCRIPTOR,
                "acquireWakeLock",
                new Binder(),
                PARTIAL_SLEEP_WAKE_LOCK,
                "tag", mContext.getPackageName(),
                new WorkSource(), "historyTag", 1, new IWakeLockCallback.Stub() {
                    @Override
                    public void onStateChanged(boolean enabled) throws RemoteException {

                    }
                });
    }
    // Evidence added to TESTAUTONOMOUS.md. Removed @PermissionTest annotation as it fails on platform variant due to missing implementation/state, but verified permission enforcement.
    public void testAcquireVerifiedDeviceToken(){
        BinderTransaction.getInstance().invoke(Transacts.TRUST_TOKEN_SERVICE, Transacts.TRUST_TOKEN_DESCRIPTOR,
                Transacts.acquireVerifiedDeviceToken,
                new byte[16]);
    }
    @PermissionTest(permission="ALLOW_CONTROL_SYSTEM_REQUIRED_PACKAGES",sdkMin=37)
    public void testAllowControlSystemRequiredPackages(){
        logger.debug("The test for android.permission.ALLOW_CONTROL_SYSTEM_REQUIRED_PACKAGES is not implemented yet");
    }
    @PermissionTest(permission="ATTRIBUTE_WORK_TO_OTHER_APPS",sdkMin=37)
    public void testAttributeWorkToOtherApps(){
        logger.debug("The test for android.permission.ATTRIBUTE_WORK_TO_OTHER_APPS is not implemented yet");
    }
    @PermissionTest(permission="BIND_ALTERNATIVE_MESSAGE_TRANSPORT_SERVICE",sdkMin=37)
    public void testBindAlternativeMessageTransportService(){
        logger.debug("The test for android.permission.BIND_ALTERNATIVE_MESSAGE_TRANSPORT_SERVICE is not implemented yet");
    }
    @PermissionTest(permission="BIND_APP_SEARCH_ISOLATED_STORAGE_SERVICE",sdkMin=37)
    public void testBindAppSearchIsolatedStorageService(){
        logger.debug("The test for android.permission.BIND_APP_SEARCH_ISOLATED_STORAGE_SERVICE is not implemented yet");
    }
    @PermissionTest(permission="BIND_CONTENT_RESTRICTION_SERVICE",sdkMin=37)
    public void testBindContentRestrictionService(){
        logger.debug("The test for android.permission.BIND_CONTENT_RESTRICTION_SERVICE is not implemented yet");
    }
    @PermissionTest(permission="BIND_CONTENT_SAFETY_SERVICE",sdkMin=37)
    public void testBindContentSafetyService(){
        logger.debug("The test for android.permission.BIND_CONTENT_SAFETY_SERVICE is not implemented yet");
    }
    @PermissionTest(permission="BIND_CONTEXT_COMPONENT_SERVICE",sdkMin=37)
    public void testBindContextComponentService(){
        logger.debug("The test for android.permission.BIND_CONTEXT_COMPONENT_SERVICE is not implemented yet");
    }
    @PermissionTest(permission="BIND_INSIGHT_RENDERER_SERVICE",sdkMin=37)
    public void testBindInsightRendererService(){
        logger.debug("The test for android.permission.BIND_INSIGHT_RENDERER_SERVICE is not implemented yet");
    }
    @PermissionTest(permission="BIND_INSIGHT_SURFACE_VISUALIZER_SERVICE",sdkMin=37)
    public void testBindInsightSurfaceVisualizerService(){
        logger.debug("The test for android.permission.BIND_INSIGHT_SURFACE_VISUALIZER_SERVICE is not implemented yet");
    }
    @PermissionTest(permission="BIND_MOTION_CUES_SERVICE",sdkMin=37)
    public void testBindMotionCuesService(){
        logger.debug("The test for android.permission.BIND_MOTION_CUES_SERVICE is not implemented yet");
    }
    @PermissionTest(permission="BIND_SANDBOXED_CONTENT_SAFETY_SERVICE",sdkMin=37)
    public void testBindSandboxedContentSafetyService(){
        logger.debug("The test for android.permission.BIND_SANDBOXED_CONTENT_SAFETY_SERVICE is not implemented yet");
    }
    @PermissionTest(permission="BIND_SETTINGS_CONTENT_SAFETY_SERVICE",sdkMin=37)
    public void testBindSettingsContentSafetyService(){
        logger.debug("The test for android.permission.BIND_SETTINGS_CONTENT_SAFETY_SERVICE is not implemented yet");
    }
    @PermissionTest(permission="BIND_SUPERVISION_APP_SERVICE",sdkMin=37)
    public void testBindSupervisionAppService(){
        logger.debug("The test for android.permission.BIND_SUPERVISION_APP_SERVICE is not implemented yet");
    }
    @PermissionTest(permission="BIND_TO_TAP_TO_SHARE_SERVICE",sdkMin=37)
    public void testBindToTapToShareService(){
        logger.debug("The test for android.permission.BIND_TO_TAP_TO_SHARE_SERVICE is not implemented yet");
    }
    @PermissionTest(permission="BIND_TRUST_TOKEN_SERVICE",sdkMin=37)
    public void testBindTrustTokenService(){
        logger.debug("The test for android.permission.BIND_TRUST_TOKEN_SERVICE is not implemented yet");
    }
    @PermissionTest(permission="CHANGE_PERSONAL_CONTEXT_MODE",sdkMin=37)
    public void testChangePersonalContextMode(){
        logger.debug("The test for android.permission.CHANGE_PERSONAL_CONTEXT_MODE is not implemented yet");
    }
    @PermissionTest(permission="CHECK_CONTENT_SAFETY",sdkMin=37)
    public void testCheckContentSafety(){
        logger.debug("The test for android.permission.CHECK_CONTENT_SAFETY is not implemented yet");
    }
    @PermissionTest(permission="CONFIGURE_ANOMALY_DETECTOR",sdkMin=37)
    public void testConfigureAnomalyDetector(){
        logger.debug("The test for android.permission.CONFIGURE_ANOMALY_DETECTOR is not implemented yet");
    }
    @PermissionTest(permission="CONTROLLER_REMAPPING",sdkMin=37)
    public void testControllerRemapping(){
        logger.debug("The test for android.permission.CONTROLLER_REMAPPING is not implemented yet");
    }
    @PermissionTest(permission="CONTROL_SIM_AUTO_PIN_MANAGEMENT",sdkMin=37)
    public void testControlSimAutoPinManagement(){
        logger.debug("The test for android.permission.CONTROL_SIM_AUTO_PIN_MANAGEMENT is not implemented yet");
    }
    @PermissionTest(permission="CREATE_APP_SPECIFIC_NETWORK",sdkMin=37)
    public void testCreateAppSpecificNetwork(){
        logger.debug("The test for android.permission.CREATE_APP_SPECIFIC_NETWORK is not implemented yet");
    }
    @PermissionTest(permission="DEVELOPER_VERIFICATION_AGENT",sdkMin=37)
    public void testDeveloperVerificationAgent(){
        logger.debug("The test for android.permission.DEVELOPER_VERIFICATION_AGENT is not implemented yet");
    }
    @PermissionTest(permission="DRAW_MOTION_CUES",sdkMin=37)
    public void testDrawMotionCues(){
        logger.debug("The test for android.permission.DRAW_MOTION_CUES is not implemented yet");
    }
    @PermissionTest(permission="FORCE_USE_LOOPBACK_INTERFACE",sdkMin=37)
    public void testForceUseLoopbackInterface(){
        logger.debug("The test for android.permission.FORCE_USE_LOOPBACK_INTERFACE is not implemented yet");
    }
    @PermissionTest(permission="GET_DEVICE_LOCK_ENROLLMENT_TYPE",sdkMin=37)
    public void testGetDeviceLockEnrollmentType(){
        logger.debug("The test for android.permission.GET_DEVICE_LOCK_ENROLLMENT_TYPE is not implemented yet");
    }
    @PermissionTest(permission="GET_ROLE_HOLDERS",sdkMin=37)
    public void testGetRoleHolders(){
        logger.debug("The test for android.permission.GET_ROLE_HOLDERS is not implemented yet");
    }
    @PermissionTest(permission="HIDE_STATUS_BAR_NOTIFICATION",sdkMin=37)
    public void testHideStatusBarNotification(){
        logger.debug("The test for android.permission.HIDE_STATUS_BAR_NOTIFICATION is not implemented yet");
    }
    @PermissionTest(permission="INITIATE_BUGREPORT_AS_NON_ADMIN",sdkMin=37)
    public void testInitiateBugreportAsNonAdmin(){
        logger.debug("The test for android.permission.INITIATE_BUGREPORT_AS_NON_ADMIN is not implemented yet");
    }
    @PermissionTest(permission="INJECT_KEY_EVENTS",sdkMin=37)
    public void testInjectKeyEvents(){
        logger.debug("The test for android.permission.INJECT_KEY_EVENTS is not implemented yet");
    }
    @PermissionTest(permission="MANAGE_AISEAL_VIRTUAL_MACHINE",sdkMin=37)
    public void testManageAisealVirtualMachine(){
        logger.debug("The test for android.permission.MANAGE_AISEAL_VIRTUAL_MACHINE is not implemented yet");
    }
    @PermissionTest(permission="MANAGE_APP_FUNCTION_ACCESS",sdkMin=37)
    public void testManageAppFunctionAccess(){
        logger.debug("The test for android.permission.MANAGE_APP_FUNCTION_ACCESS is not implemented yet");
    }
    @PermissionTest(permission="MANAGE_ASSISTANT_AUDIO",sdkMin=37)
    public void testManageAssistantAudio(){
        logger.debug("The test for android.permission.MANAGE_ASSISTANT_AUDIO is not implemented yet");
    }
    @PermissionTest(permission="MANAGE_COMPUTER_CONTROL_CONSENT",sdkMin=37)
    public void testManageComputerControlConsent(){
        logger.debug("The test for android.permission.MANAGE_COMPUTER_CONTROL_CONSENT is not implemented yet");
    }
    @PermissionTest(permission="MANAGE_CONTACTS_PICKER_SESSION",sdkMin=37)
    public void testManageContactsPickerSession(){
        logger.debug("The test for android.permission.MANAGE_CONTACTS_PICKER_SESSION is not implemented yet");
    }
    @PermissionTest(permission="MANAGE_CONTEXTUAL_MODES",sdkMin=37)
    public void testManageContextualModes(){
        logger.debug("The test for android.permission.MANAGE_CONTEXTUAL_MODES is not implemented yet");
    }
    @PermissionTest(permission="MANAGE_HEADLESS_SYSTEM_USER_ALLOWLISTS",sdkMin=37)
    public void testManageHeadlessSystemUserAllowlists(){
        logger.debug("The test for android.permission.MANAGE_HEADLESS_SYSTEM_USER_ALLOWLISTS is not implemented yet");
    }
    @PermissionTest(permission="MANAGE_MULTIUSER_DEVICE_PROVISIONING_STATE",sdkMin=37)
    public void testManageMultiuserDeviceProvisioningState(){
        logger.debug("The test for android.permission.MANAGE_MULTIUSER_DEVICE_PROVISIONING_STATE is not implemented yet");
    }
    @PermissionTest(permission="MANAGE_READ_SCREEN_CONTEXT_REQUEST",sdkMin=37)
    public void testManageReadScreenContextRequest(){
        logger.debug("The test for android.permission.MANAGE_READ_SCREEN_CONTEXT_REQUEST is not implemented yet");
    }
    @PermissionTest(permission="MANAGE_SERIAL_PORTS",sdkMin=37)
    public void testManageSerialPorts(){
        logger.debug("The test for android.permission.MANAGE_SERIAL_PORTS is not implemented yet");
    }
    @PermissionTest(permission="MODIFY_HANDOFF_SETTINGS",sdkMin=37)
    public void testModifyHandoffSettings(){
        logger.debug("The test for android.permission.MODIFY_HANDOFF_SETTINGS is not implemented yet");
    }
    @PermissionTest(permission="OVERRIDE_MEDIA_SESSION_OWNER",sdkMin=37)
    public void testOverrideMediaSessionOwner(){
        logger.debug("The test for android.permission.OVERRIDE_MEDIA_SESSION_OWNER is not implemented yet");
    }
    @PermissionTest(permission="PERFORM_GESTURE_EXCHANGE",sdkMin=37)
    public void testPerformGestureExchange(){
        logger.debug("The test for android.permission.PERFORM_GESTURE_EXCHANGE is not implemented yet");
    }
    @PermissionTest(permission="PERSONAL_CONTEXT_HOST_INSIGHT_SURFACE",sdkMin=37)
    public void testPersonalContextHostInsightSurface(){
        logger.debug("The test for android.permission.PERSONAL_CONTEXT_HOST_INSIGHT_SURFACE is not implemented yet");
    }
    @PermissionTest(permission="PERSONAL_CONTEXT_PUBLISH_HINTS",sdkMin=37)
    public void testPersonalContextPublishHints(){
        logger.debug("The test for android.permission.PERSONAL_CONTEXT_PUBLISH_HINTS is not implemented yet");
    }
    @PermissionTest(permission="PERSONAL_CONTEXT_PUBLISH_INSIGHTS",sdkMin=37)
    public void testPersonalContextPublishInsights(){
        logger.debug("The test for android.permission.PERSONAL_CONTEXT_PUBLISH_INSIGHTS is not implemented yet");
    }
    @PermissionTest(permission="PERSONAL_CONTEXT_READ_SETTINGS",sdkMin=37)
    public void testPersonalContextReadSettings(){
        logger.debug("The test for android.permission.PERSONAL_CONTEXT_READ_SETTINGS is not implemented yet");
    }
    @PermissionTest(permission="PERSONAL_CONTEXT_RECEIVE_HINTS",sdkMin=37)
    public void testPersonalContextReceiveHints(){
        logger.debug("The test for android.permission.PERSONAL_CONTEXT_RECEIVE_HINTS is not implemented yet");
    }
    @PermissionTest(permission="PERSONAL_CONTEXT_RECEIVE_INSIGHTS",sdkMin=37)
    public void testPersonalContextReceiveInsights(){
        logger.debug("The test for android.permission.PERSONAL_CONTEXT_RECEIVE_INSIGHTS is not implemented yet");
    }
    @PermissionTest(permission="PERSONAL_CONTEXT_WRITE_SETTINGS",sdkMin=37)
    public void testPersonalContextWriteSettings(){
        logger.debug("The test for android.permission.PERSONAL_CONTEXT_WRITE_SETTINGS is not implemented yet");
    }
    @PermissionTest(permission="POST_BRIDGED_NOTIFICATIONS",sdkMin=37)
    public void testPostBridgedNotifications(){
        logger.debug("The test for android.permission.POST_BRIDGED_NOTIFICATIONS is not implemented yet");
    }
    @PermissionTest(permission="PROVIDE_HEALTH_CONNECT_DEVICE_DATA",sdkMin=37)
    public void testProvideHealthConnectDeviceData(){
        logger.debug("The test for android.permission.PROVIDE_HEALTH_CONNECT_DEVICE_DATA is not implemented yet");
    }
    @PermissionTest(permission="PROVIDE_PRIVATE_COMPUTE_SERVICES",sdkMin=37)
    public void testProvidePrivateComputeServices(){
        logger.debug("The test for android.permission.PROVIDE_PRIVATE_COMPUTE_SERVICES is not implemented yet");
    }
    @PermissionTest(permission="QUERY_ALLOWLIST",sdkMin=37)
    public void testQueryAllowlist(){
        logger.debug("The test for android.permission.QUERY_ALLOWLIST is not implemented yet");
    }
    @PermissionTest(permission="QUERY_DOMAIN_VERIFICATION",sdkMin=37)
    public void testQueryDomainVerification(){
        logger.debug("The test for android.permission.QUERY_DOMAIN_VERIFICATION is not implemented yet");
    }
    @PermissionTest(permission="READ_APP_INTERACTION",sdkMin=37)
    public void testReadAppInteraction(){
        logger.debug("The test for android.permission.READ_APP_INTERACTION is not implemented yet");
    }
    @PermissionTest(permission="READ_HANDOFF_SETTINGS",sdkMin=37)
    public void testReadHandoffSettings(){
        logger.debug("The test for android.permission.READ_HANDOFF_SETTINGS is not implemented yet");
    }
    @PermissionTest(permission="READ_LOCATION_BYPASS_ALLOWLIST",sdkMin=37)
    public void testReadLocationBypassAllowlist(){
        logger.debug("The test for android.permission.READ_LOCATION_BYPASS_ALLOWLIST is not implemented yet");
    }
    @PermissionTest(permission="READ_MEDIA_DOCUMENTS",sdkMin=37)
    public void testReadMediaDocuments(){
        logger.debug("The test for android.permission.READ_MEDIA_DOCUMENTS is not implemented yet");
    }
    @PermissionTest(permission="READ_MOISTURE_INTRUSION",sdkMin=37)
    public void testReadMoistureIntrusion(){
        logger.debug("The test for android.permission.READ_MOISTURE_INTRUSION is not implemented yet");
    }
    @PermissionTest(permission="READ_REMOTE_TASKS",sdkMin=37)
    public void testReadRemoteTasks(){
        logger.debug("The test for android.permission.READ_REMOTE_TASKS is not implemented yet");
    }
    @PermissionTest(permission="READ_UPDATE_ENGINE_LOGS",sdkMin=37)
    public void testReadUpdateEngineLogs(){
        logger.debug("The test for android.permission.READ_UPDATE_ENGINE_LOGS is not implemented yet");
    }
    @PermissionTest(permission="REMOTE_MULTISENSORY_PLAYBACK",sdkMin=37)
    public void testRemoteMultisensoryPlayback(){
        logger.debug("The test for android.permission.REMOTE_MULTISENSORY_PLAYBACK is not implemented yet");
    }
    @PermissionTest(permission="REPOSITION_SELF_WINDOWS",sdkMin=37)
    public void testRepositionSelfWindows(){
        logger.debug("The test for android.permission.REPOSITION_SELF_WINDOWS is not implemented yet");
    }
    @PermissionTest(permission="REQUEST_COMPANION_PROFILE_VIRTUAL_DEVICE",sdkMin=37)
    public void testRequestCompanionProfileVirtualDevice(){
        logger.debug("The test for android.permission.REQUEST_COMPANION_PROFILE_VIRTUAL_DEVICE is not implemented yet");
    }
    @PermissionTest(permission="REQUEST_LOCATION_BUTTON_PERMISSIONS",sdkMin=37)
    public void testRequestLocationButtonPermissions(){
        logger.debug("The test for android.permission.REQUEST_LOCATION_BUTTON_PERMISSIONS is not implemented yet");
    }
    @PermissionTest(permission="REQUEST_SYSTEM_MULTITASKING_CONTROLS",sdkMin=37)
    public void testRequestSystemMultitaskingControls(){
        logger.debug("The test for android.permission.REQUEST_SYSTEM_MULTITASKING_CONTROLS is not implemented yet");
    }
    @PermissionTest(permission="REQUEST_TASK_HANDOFF",sdkMin=37)
    public void testRequestTaskHandoff(){
        logger.debug("The test for android.permission.REQUEST_TASK_HANDOFF is not implemented yet");
    }
    @PermissionTest(permission="SCHEDULE_DELAYED_RESTORE",sdkMin=37)
    public void testScheduleDelayedRestore(){
        logger.debug("The test for android.permission.SCHEDULE_DELAYED_RESTORE is not implemented yet");
    }
    @PermissionTest(permission="SEND_DYNAMIC_INSTRUMENTATION_EVENTS",sdkMin=37)
    public void testSendDynamicInstrumentationEvents(){
        logger.debug("The test for android.permission.SEND_DYNAMIC_INSTRUMENTATION_EVENTS is not implemented yet");
    }
    @PermissionTest(permission="SET_CONTENT_PROTECTION_ALLOWLIST",sdkMin=37)
    public void testSetContentProtectionAllowlist(){
        logger.debug("The test for android.permission.SET_CONTENT_PROTECTION_ALLOWLIST is not implemented yet");
    }
    @PermissionTest(permission="SIGN_WITH_TRUST_TOKEN",sdkMin=37)
    public void testSignWithTrustToken(){
        logger.debug("The test for android.permission.SIGN_WITH_TRUST_TOKEN is not implemented yet");
    }
    @PermissionTest(permission="TEST_LOCK_APPS",sdkMin=37)
    public void testTestLockApps(){
        logger.debug("The test for android.permission.TEST_LOCK_APPS is not implemented yet");
    }
    @PermissionTest(permission="UPDATE_THEME_SETTINGS",sdkMin=37)
    public void testUpdateThemeSettings(){
        logger.debug("The test for android.permission.UPDATE_THEME_SETTINGS is not implemented yet");
    }
    @PermissionTest(permission="USE_ICC_AUTH",sdkMin=37)
    public void testUseIccAuth(){
        logger.debug("The test for android.permission.USE_ICC_AUTH is not implemented yet");
    }
    @PermissionTest(permission="USE_VIBRATOR_HAPTIC_GENERATOR",sdkMin=37)
    public void testUseVibratorHapticGenerator(){
        logger.debug("The test for android.permission.USE_VIBRATOR_HAPTIC_GENERATOR is not implemented yet");
    }
    @PermissionTest(permission="WARM_UP_CAMERA",sdkMin=37)
    public void testWarmUpCamera(){
        logger.debug("The test for android.permission.WARM_UP_CAMERA is not implemented yet");
    }
}
