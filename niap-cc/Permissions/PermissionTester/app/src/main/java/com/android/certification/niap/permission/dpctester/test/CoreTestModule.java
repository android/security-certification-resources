package com.android.certification.niap.permission.dpctester.test;
/*
 * Copyright (C) 2025 The Android Open Source Project
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
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_MEDIA_LOCATION;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.ANSWER_PHONE_CALLS;
import static android.Manifest.permission.BLUETOOTH;
import static android.Manifest.permission.BLUETOOTH_ADVERTISE;
import static android.Manifest.permission.BLUETOOTH_CONNECT;
import static android.Manifest.permission.BLUETOOTH_PRIVILEGED;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.MANAGE_OWN_CALLS;
import static android.Manifest.permission.NFC;
import static android.Manifest.permission.READ_CALENDAR;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_MEDIA_AUDIO;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.REQUEST_PASSWORD_COMPLEXITY;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.USE_BIOMETRIC;
import static android.Manifest.permission.WRITE_CALENDAR;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.net.ConnectivityManager;
import android.net.VpnService;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Build;
import android.provider.CallLog;
import android.provider.VoicemailContract;
import android.service.vr.VrListenerService;
import android.telecom.TelecomManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import com.android.certification.niap.permission.dpctester.R;
import com.android.certification.niap.permission.dpctester.common.Constants;
import com.android.certification.niap.permission.dpctester.common.ReflectionUtil;
import com.android.certification.niap.permission.dpctester.test.exception.BypassTestException;
import com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException;
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestRunner;
import com.android.certification.niap.permission.dpctester.test.runner.SignaturePermissionTestModuleBase;
import com.android.certification.niap.permission.dpctester.test.tool.BinderTransaction;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTest;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestModule;
import com.android.certification.niap.permission.dpctester.test.tool.PreferenceBool;
import com.android.certification.niap.permission.dpctester.test.tool.ReflectionTool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * The test case is based on the suggestion from
 * lab for picking up core permission test case
 */
@PermissionTestModule(name="Core Permission Test Cases",label = "Run Core Tests")
public class CoreTestModule extends SignaturePermissionTestModuleBase {

    @PreferenceBool(label="Enable Runtime Confirmation",
            prflabel = "core_enable_runtime", defaultValue=false)
    public boolean enableRuntimeConfirmation=false;

    //Test Modules
    SignatureTestModule signatureTestModule;
    SignatureTestModuleQ signatureTestModuleQ;
    SignatureTestModuleR signatureTestModuleR;
    SignatureTestModuleS signatureTestModuleS;
    SignatureTestModuleT signatureTestModuleT;
    SignatureTestModuleU signatureTestModuleU;
    SignatureTestModuleV signatureTestModuleV;
    SignatureTestModuleBinder signatureTestModuleBinder;// = new SignatureTestModule(mActivity);

    RuntimeTestModule   runtimeTestModule;// = new SignatureTestModuleR(mActivity);
    InstallTestModule   installTestModule;
    public CoreTestModule(@NonNull Activity activity) {
        super(activity);
        //Delegation
        signatureTestModule = new SignatureTestModule(activity);
        signatureTestModuleQ  = new SignatureTestModuleQ(activity);
        signatureTestModuleR  = new SignatureTestModuleR(activity);
        signatureTestModuleS  = new SignatureTestModuleS(activity);
        signatureTestModuleT  = new SignatureTestModuleT(activity);
        signatureTestModuleU  = new SignatureTestModuleU(activity);
        signatureTestModuleV  = new SignatureTestModuleV(activity);

        signatureTestModuleBinder  = new SignatureTestModuleBinder(activity);

        runtimeTestModule   = new RuntimeTestModule(activity);
        installTestModule   = new InstallTestModule(activity);
    }
    BluetoothAdapter mBluetoothAdapter;
    CountDownLatch mCountDownLatch  = null;

    @NonNull
    @Override
    public PrepareInfo prepare(Consumer<PermissionTestRunner.Result> callback){

        try {
            mBluetoothAdapter = systemService(BluetoothManager.class).getAdapter();
        } catch (NullPointerException e) { /*Leave bluetoothAdapter as null, if manager isn't available*/ }


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        enableRuntimeConfirmation = sp.getBoolean("core_enable_runtime",false);

        if(!mContext.getResources().getBoolean(R.bool.inverse_test_result) && enableRuntimeConfirmation) {
            //1.List requested permissions
            List<PermissionTestRunner.Data> datas =
                    ReflectionTool.Companion.checkPermissionTestMethod(this);
            Set<String> requested = new HashSet<>();
            for (PermissionTestRunner.Data d : datas) {
                requested.addAll(Arrays.asList(d.getRequestedPermissions()));
            }
            //logger.system("Requesting:"+requested);
            //2. Check the requested permissions are granted
            List<String> requestList = new ArrayList<>();
            for (String permission : requested) {
                if (mActivity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    requestList.add(permission);
                }
            }
            if (!requestList.isEmpty()) {
                logger.debug("Requesting Permission:" + requestList.toString());
                ActivityCompat.requestPermissions(
                        mActivity, requestList.toArray(new String[]{}),
                        Constants.PERMISSION_CODE_RUNTIME_DEPENDENT_PERMISSIONS
                );
                mCountDownLatch = new CountDownLatch(1);
                try {
                    // Wait for the countdown on the latch; this will block the thread attempting to
                    // run the permission test while the user is prompted for consent to the required
                    // permissions.
                    mCountDownLatch.await(10, TimeUnit.SECONDS);
                    // If the user has not granted the required permissions then throw a bypass
                    // exception to notify the user of this requirement.
                    boolean allGranted = true;
                    for (String permission : requested) {
                        if (mActivity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                            allGranted = false;
                        }
                    }
                    if (!allGranted) {
                        logger.system("Some Runtime Permissions are not granted. Try again and grant all permissions");
                    }
                } catch (InterruptedException e) {
                    throw new BypassTestException("Caught an Interruption");
                }
            }
        }
        //


        return super.prepare(callback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           int[] grantResults) {
        logger.system("onRequestPermissionResult event caught:"+requestCode);
        boolean permissionGranted = true;
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                permissionGranted = false;
            }
        }
        //
        switch (requestCode) {
            case Constants.PERMISSION_CODE_RUNTIME_DEPENDENT_PERMISSIONS:
                if (!permissionGranted) {
                    logger.system("Runtime Permission : The required permissions are not granted.");
                    throw new RuntimeException("The required permissions (" + String.join(", ", permissions)
                            + ") were not granted");
                }
                break;
            default:
                throw new RuntimeException("An unexpected request code of " + requestCode + " with permissions "
                        + String.join(", ", permissions) + " + was received");
        }
        mCountDownLatch.countDown();
    }

    private <T> T systemService(Class<T> clazz) {
        T service = getService(clazz);
        if(service == null){
            throw new NullPointerException("[npe_system_service]" + clazz.getSimpleName());
        }
        return service;
    }

    //1. Camera
    //	 Normal: Android does not provide any authorization at this level to access the Camera system service
    //	 Dangerous: android.permission.CAMERA
    //	 Platform: android.permission.CAPTURE_VIDEO_OUTPUT or android.permission.CAPTURE_SECURE_VIDEO_OUTPUT
    @PermissionTest(permission=CAMERA,
            requestedPermissions = {"android.permission.CAMERA"})
    public void testCamera(){
        runtimeTestModule.testCamera();
    }
    @PermissionTest(permission = "CAPTURE_SECURE_VIDEO_OUTPUT")
    public void testCaptureSecureVideoOutput() {
        signatureTestModule.testCaptureSecureVideoOutput();
    }


    @PermissionTest(permission = "CAPTURE_VIDEO_OUTPUT")
    public void testCaptureVideoOutput() {
        signatureTestModule.testCaptureVideoOutput();
    }

    //2. Microphone
    //	Normal: android.permission.MODIFY_AUDIO_SETTINGS
    //	Dangerous: android.permission.RECORD_AUDIO
    //	Platform: 	android.permission.BIND_SOUND_TRIGGER_DETECTION_SERVICE
    @PermissionTest(permission = "MODIFY_AUDIO_SETTINGS")
    public void testModifyAudioSettings() {
        installTestModule.testModifyAudioSettings();
    }
    @PermissionTest(permission = "RECORD_AUDIO",
            requestedPermissions = {"android.permission.RECORD_AUDIO"})
    public void testRecordAudio() {
        runtimeTestModule.testRecordAudio();
    }

//    @PermissionTest(permission="BIND_SOUND_TRIGGER_DETECTION_SERVICE")
//    public void testBindSoundTriggerDetectionService(){
//        runBindRunnable("BIND_SOUND_TRIGGER_DETECTION_SERVICE");
//    }

    @PermissionTest(permission="MODIFY_AUDIO_SETTINGS_PRIVILEGED", sdkMin=34)
    public void testModifyAudioSettingsPrivileged(){
        signatureTestModuleU.testModifyAudioSettingsPrivileged();
    }

    //3. Location / GPS
    //	Normal: Android does not provide any authorization at this level to access the Location / GPS system service
    //	Dangerous: android.permission.ACCESS_FINE_LOCATION or android.permission.ACCESS_COARSE_LOCATION or android.permission.ACCESS_BACKGROUND_LOCATION
    //	Platform: android.permission.CONTROL_LOCATION_UPDATES or android.permission.LOCATION_HARDWARE
    @PermissionTest(permission=ACCESS_COARSE_LOCATION,
            requestedPermissions = {"android.permission.ACCESS_COARSE_LOCATION"})
    public void testAccessCoarseLocation(){
        runtimeTestModule.testAccessCoarseLocation();
    }
    @PermissionTest(permission=ACCESS_FINE_LOCATION,
            requestedPermissions = {"android.permission.ACCESS_FINE_LOCATION"})
    public void testAccessFineLocation(){
        runtimeTestModule.testAccessFineLocation();
    }
    @PermissionTest(permission = "CONTROL_LOCATION_UPDATES")
    public void testControlLocationUpdates() {
        signatureTestModule.testControlLocationUpdates();
    }
    @PermissionTest(permission = "LOCATION_HARDWARE")
    public void testLocationHardware() {
        signatureTestModuleT.testLocationHardware();
    }

    //4. Contacts / Address Book
    //	Normal: Android does not provide any authorization at this level to access the Contacts / Address Book system service
    //	Dangerous: android.permission.READ_CONTACTS
    //	Platform: Android does not provide any authorization at this level to access the Contacts / Address Book system service
    @PermissionTest(permission=READ_CONTACTS,
            requestedPermissions = {"android.permission.READ_CONTACTS"})
    public void testReadContacts(){
        runtimeTestModule.testReadContacts();
    }
    //5. Calendar
    //	Normal: Android does not provide any authorization at this level to access the Calendar system service
    //	Dangerous: android.permission.READ/WRITE_CALENDAR
    //	Platform: Android does not provide any authorization at this level to access the Calendar system service
    @PermissionTest(permission=READ_CALENDAR,
            requestedPermissions = {"android.permission.READ_CALENDAR"})
    public void testReadCalendar(){
        runtimeTestModule.testReadCalendar();
    }
    @PermissionTest(permission=WRITE_CALENDAR,
            requestedPermissions = {"android.permission.WRITE_CALENDAR"})
    public void testWriteCalendar(){
       runtimeTestModule.testWriteCalendar();
    }
    //6. File / Storage Access - There were several changes built into Android 11.  We likely can assume all evaluated devices are now 11+
    //	Normal: Android does not provide any authorization at this level to access the File / Storage Access system service
    //	Dangerous: android.permission.READ_EXTERNAL_STORAGE or android.permission.READ_LOGS or android.permission.INTERNAL_DELETE_CACHE_FILES or android.permission.ACCESS_MEDIA_LOCATION
    //	Platform: android.permission.STORAGE_INTERNAL or android.permission.MANAGE_EXTERNAL_STORAGE or android.permission.ACCESS_CACHE_FILESYSTEM or android.permission.MOUNT_UNMOUNT_FILESYSTEMS

    //READ_EXTERNAL_STORAGE is deprecated, READ_LOGS is not working actually.

    //Access media location can not be test with access_media_image permission
    //And I think it's not a storage access permission I guess.
    //As of Android 13, there's no runtime level storage permissions. currently we can apporove it by media type.

    @PermissionTest(permission=READ_MEDIA_AUDIO, sdkMin=33,
            requestedPermissions = {"android.permission.READ_MEDIA_AUDIO"})
    public void testReadMediaAudio(){
        runtimeTestModule.testReadMediaAudio();
    }

    //INTERNAL_DELETE_CACHE_FILES is a signature level permission
    @PermissionTest(permission = "INTERNAL_DELETE_CACHE_FILES")
    public void testInternalDeleteCacheFiles() {
        signatureTestModule.testInternalDeleteCacheFiles();
    }

    //7. Photo Access - Maybe this is more appropriately combined with File access? The SFR calls this out which is why we mention it.
    //	Normal: Android does not provide any authorization at this level to access the Photos Access system service
    //	Dangerous: android.permission.READ_MEDIA_IMAGES
    //	Platform: Android does not provide any authorization at this level to access the Photos Access system service
    @PermissionTest(permission=READ_MEDIA_IMAGES, sdkMin=33,
            requestedPermissions = {"android.permission.READ_MEDIA_IMAGES"})
    public void testReadMediaImages(){
        runtimeTestModule.testReadMediaImages();
    }
    //8. Device Identifier Information
    //	Normal: Android does not provide any authorization at this level to access the Device Identifier Information system service
    //	Dangerous: Android does not provide any authorization at this level to access the Device Identifier Information system service
    //	Platform: android.permision.READ_DEVICE_CONFIG or android.permission.MANAGE_USER_OEM_UNLOCK_STATE

    //read deivice config is disabled after sdk35
    //@PermissionTest(permission="READ_DEVICE_CONFIG", sdkMin=29)
    //public void testReadDeviceConfig(){
    //signatureTestModuleQ.testReadDeviceConfig();
    //}
    //Need test for it? I think the permission is irrelevant to the device identifier information
    //@PermissionTest(permission="MANAGE_USER_OEM_UNLOCK_STATE")
    //READ_PRIVILEGED_PHONE_STATE?
    @PermissionTest(permission = "READ_PRIVILEGED_PHONE_STATE")
    public void testReadPrivilegedPhoneState() {
       signatureTestModule.testReadPrivilegedPhoneState();
    }

    //9. Text Messages - If possible to run without active SIM card, that would be great
    //	Normal: Android does not provide any authorization at this level to access the Text Messages system service
    //	Dangerous: android.permission.READ_SMS or android.permission.SEND_SMS
    //	Platform: android.permission.BIND_CARRIER_MESSAGING_SERVICE or android.permission.BIND_CARRIER_MESSAGING_CLIENT_SERVICE
    @PermissionTest(permission=READ_SMS,
            requestedPermissions = {"android.permission.READ_SMS"})
    public void testReadSms(){
        runtimeTestModule.testReadSms();
    }

    @PermissionTest(permission=SEND_SMS,
            requestedPermissions = {"android.permission.SEND_SMS"})
    public void testSendSms(){
        runtimeTestModule.testSendSms();
    }

    @PermissionTest(permission="ACCESS_MESSAGES_ON_ICC", sdkMin=30,requestedPermissions = "android.permission.RECEIVE_SMS")
    public void testAccessMessagesOnIcc(){
        signatureTestModuleR.testAccessMessagesOnIcc();
    }

//    @PermissionTest(permission="BIND_CARRIER_MESSAGING_SERVICE")
//    public void testBindCarrierMessagingService(){
//        signatureTestModuleBinder.testBindCarrierMessagingService();
//    }

//    @PermissionTest(permission="BIND_CARRIER_MESSAGING_CLIENT_SERVICE", sdkMin=29)
//    public void testBindCarrierMessagingClientService(){
//        signatureTestModuleBinder.testBindCarrierMessagingClientService();
//    }


    //10. Telephony - If possible to run without active SIM card, that would be great
    //	Normal: android.permission.MANAGE_OWN_CALLS
    //	Dangerous: android.permission.ANSWER_PHONE_CALLS or android.permission.CALL_PHONE or android.permission.READ/WRITE_CALL_LOG or android.permission.READ_PHONE_NUMBERS or android.permission.READ_PHONE_STATE or com.android.voicemail.permission.ADD_VOICEMAIL
    //	Platform: com.android.voicemail.permission.READ/WRITE_VOICEMAIL or android.permission.MODIFY_PHONE_STATE or android.permission.READ_PRECISE_PHONE_STATE or android.permission.READ/WRITE_BLOCKED_NUMBERS or android.permission.BIND_CARRIER_SERVICES/_TELECOM_CONNECTION_SERVICE/_TEXTCLASSIFIER_SERVICE/_TEXT_SERVICE/_VISUAL_VOICEMAIL_SERVICE
    @PermissionTest(permission=MANAGE_OWN_CALLS)
    public void testManageOwnCalls(){
        installTestModule.testManageOwnCalls();
    }
    @PermissionTest(permission=ANSWER_PHONE_CALLS,
            requestedPermissions = {"android.permission.ANSWER_PHONE_CALLS"})
    public void testAnswerPhoneCalls(){
        runtimeTestModule.testAnswerPhoneCalls();
    }

    @PermissionTest(permission="com.android.voicemail.permission.READ_VOICEMAIL",requestedPermissions = "android.permission.READ_CALL_LOG")
    public void testReadVoicemail(){
        signatureTestModule.testReadVoicemail();
    }

    @PermissionTest(permission="com.android.voicemail.permission.WRITE_VOICEMAIL",
            requestedPermissions = "com.android.voicemail.permission.ADD_VOICEMAIL")
    public void testWriteVoicemail(){
        signatureTestModule.testWriteVoicemail();
    }
    //11. Cellular - If possible to run without active SIM card, that would be great
    //	Normal: Android does not provide any authorization at this level to access the Cellular system service
    //	Dangerous: Android does not provide any authorization at this level to access the Cellular system service
    //	Platform: android.permission.BIND_TELEPHONY_DATA_SERVICE or android.permission.BIND_TELEPHONY_NETWORK_SERVICE or android.permission.BIND_CELL_BROADCAST_SERVICE
    //@PermissionTest(permission="BIND_TELEPHONY_DATA_SERVICE")
    //public void testBindTelephonyDataService(){
    //    runBindRunnable("BIND_TELEPHONY_DATA_SERVICE");
    //}

    @PermissionTest(permission="ACCESS_LAST_KNOWN_CELL_ID", sdkMin=35)
    public void testAccessLastKnownCellId(){
        signatureTestModuleV.testAccessLastKnownCellId();
    }


    //12. Bluetooth - There were several changes built into Android 12.  We likely can assume all evaluated devices are now 12+
    //	Normal: android.permission.BLUETOOTH or android.permission.BLUETOOTH_ADMIN
    //	Dangerous: android.permission.BLUETOOTH_ADVERTISE or android.permission.BLUETOOTH_SCAN
    //	Platform: android.permission.BLUETOOTH_PRIVILEGED

    //I think there's no normal test case for bluetooth after sdk 30
    @PermissionTest(permission=BLUETOOTH, sdkMin=28, sdkMax=30 ,requiredPermissions = {BLUETOOTH_CONNECT})
    public void testBluetooth(){
        installTestModule.testBluetooth();
    }

    @PermissionTest(permission=BLUETOOTH_ADVERTISE, sdkMin=31,
            requestedPermissions = {"android.permission.BLUETOOTH_ADVERTISE"})
    public void testBluetoothAdvertise(){
        runtimeTestModule.testBluetoothAdvertise();
    }

    @PermissionTest(permission=BLUETOOTH_PRIVILEGED, sdkMin=28)
    public void testBluetoothPrivileged(){
        ReflectionUtil.invoke(mBluetoothAdapter,"clearBluetooth");
        //signatureTestModule.testBluetoothPrivileged();
    }


    //13.NFC
    //	Normal: android.permission.NFC or android.permission.NFC_PREFERRED_PAYMENT_INFO
    //	Dangerous: Android does not provide any authorization at this level to access the NFC system service
    //	Platform: android.permission.BIND_NFC_SERVICE
    @PermissionTest(permission=NFC)
    public void testNfc(){
        installTestModule.testNfc();
    }

    //@PermissionTest(permission="BIND_NFC_SERVICE")
    //public void testBindNfcService(){
    //    runBindRunnable("BIND_NFC_SERVICE");
    //}
    @PermissionTest(permission="NFC_SET_CONTROLLER_ALWAYS_ON", sdkMin=31)
    public void testNfcSetControllerAlwaysOn(){
        signatureTestModuleS.testNfcSetControllerAlwaysOn();
    }

    //14. Network Access
    //	Normal: android.permission.ACCESS_NETWORK_STATE or android.permission.ACCESS/CHANGE_WIFI_STATE or android.permission.CHANGE_NETWORK_STATE or android.permission.CHANGE_WIFI_MULTICAST_STATE or android.permission.INTERNET
    //
    //	Dangerous: Android does not provide any authorization at this level to access the Network Access system service
    //	Platform: android.permission.NETWORK_AIRPLANE_MODE or android.permission.READ_NETWORK_USAGE_HISTORY or android.permission.MANAGE_NETWORK_POLICY or android.permission.CONFIGURE_WIFI_DISPLAY or android.permission.NETWORK_FACTORY or android.permission.NETWORK_MANAGED_PROVISIONING or android.permission.NETWORK_SCAN or android.permission.NETWORK_SETTINGS or android.permission.NETWORK_STACK or android.permission.NETWORK_STATS_PROVIDER or android.permission.OBSERVE_NETWORK_POLICY or android.permission.OVERRIDE_WIFI_CONFIG or android.permission.WIFI_SET_DEVICE_MOBILITY_STATE or android.permission.WIFI_UPDATE_USABILITY_STATS_SCORE or android.permission.RADIO_SCAN_WITHOUT_LOCAION
    //@PermissionTest(permission=ACCESS_NETWORK_STATE)
    //public void testAccessNetworkState(){
    //    installTestModule.testAccessNetworkState();
    //
    @PermissionTest(permission=INTERNET)
    public void testAccessNetworkState(){
        installTestModule.testInternet();
    }

    @PermissionTest(permission="NETWORK_AIRPLANE_MODE", sdkMin=30)
    public void testNetworkAirplaneMode(){
        signatureTestModuleR.testNetworkAirplaneMode();
    }

    //15. VPN ??TYPO??
    //	Normal: Android does not provide any authorization at this level to access the VPN system service
    //	Dangerous: Android does not provide any authorization at this level to access the VPN system service
    //	Platform: android.permission.BIND_NFC_SERVICE
    //  CONTROL_VPN,CONTROL_ALWAYS_ON_VPN,BIND_VPN_SERVICE
    @PermissionTest(permission = "CONTROL_VPN")
    public void testControlVpn() {
        signatureTestModule.testControlVpn();
    }


    //16. Body Sensors
    //	Normal: Android does not provide any authorization at this level to access the Body Sensors system service
    //	Dangerous: Android does not provide any authorization at this level to access the Body Sensors system service
    //	Platform: android.permission.MANAGE_SENSORS or android.permission.MANAGE_SENSOR_PRIVACY
    @PermissionTest(permission = "MANAGE_SENSORS")
    public void testManageSensors() {
        signatureTestModule.testManageSensors();
    }
    //17. Fingerprint / Biometrics
    //	Normal: android.permission.USE_BIOMETRIC
    //	Dangerous: Android does not provide any authorization at this level to access the Fingerprint / Biometrics system service
    //	Platform: android.permission.USE_BIOMETRIC_INTERNAL	or android.permission.MANAGE_BIOMETRIC_DIALOG
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @PermissionTest(permission=USE_BIOMETRIC,sdkMin = 29)
    public void testUseBiometricQ() {
        installTestModule.testUseBiometricQ();
    }
    @PermissionTest(permission="USE_BIOMETRIC_INTERNAL", sdkMin=29)
    public void testUseBiometricInternal(){
        signatureTestModuleQ.testUseBiometricInternal();
    }

    //18. Credential Access
    //	Normal: android.permission.REQUEST_PASSWORD_COMPLEXITY
    //	Dangerous: Android does not provide any authorization at this level to access the Credential Access system service
    //	Platform: android.permission.RESET_PASSWORD or android.permission.READ_WIFI_CREDENTIAL or android.permission.QUERY_DO_NOT_ASK_CREDENTIALS_ON_BOOT
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @PermissionTest(permission=REQUEST_PASSWORD_COMPLEXITY, sdkMin=29)
    public void testRequestPasswordComplexity(){
        installTestModule.testRequestPasswordComplexity();
    }
    @PermissionTest(permission="RESET_PASSWORD", sdkMin=29)
    public void testResetPassword(){
        signatureTestModuleQ.testResetPassword();
    }

    //19. Email - The SFR calls this out which is why we mention it
    //	Android has no built-in Email content provider, unlike contacts and calendar.
    //	As a result, there are no stock Android permissions for accessing email as email content is stored and maintained by the email application itself.

}
