package com.android.certification.niap.permission.dpctester.test
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
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.MANAGE_DEVICE_POLICY_ACCOUNT_MANAGEMENT
import android.Manifest.permission.MANAGE_DEVICE_POLICY_AIRPLANE_MODE
import android.Manifest.permission.MANAGE_DEVICE_POLICY_APPS_CONTROL
import android.Manifest.permission.MANAGE_DEVICE_POLICY_APP_RESTRICTIONS
import android.Manifest.permission.MANAGE_DEVICE_POLICY_ASSIST_CONTENT
import android.Manifest.permission.MANAGE_DEVICE_POLICY_AUDIO_OUTPUT
import android.Manifest.permission.MANAGE_DEVICE_POLICY_AUTOFILL
import android.Manifest.permission.MANAGE_DEVICE_POLICY_BLOCK_UNINSTALL
import android.Manifest.permission.MANAGE_DEVICE_POLICY_BLUETOOTH
import android.Manifest.permission.MANAGE_DEVICE_POLICY_CALLS
import android.Manifest.permission.MANAGE_DEVICE_POLICY_CAMERA
import android.Manifest.permission.MANAGE_DEVICE_POLICY_CAMERA_TOGGLE
import android.Manifest.permission.MANAGE_DEVICE_POLICY_CERTIFICATES
import android.Manifest.permission.MANAGE_DEVICE_POLICY_COMMON_CRITERIA_MODE
import android.Manifest.permission.MANAGE_DEVICE_POLICY_CONTENT_PROTECTION
import android.Manifest.permission.MANAGE_DEVICE_POLICY_DEBUGGING_FEATURES
import android.Manifest.permission.MANAGE_DEVICE_POLICY_DEFAULT_SMS
import android.Manifest.permission.MANAGE_DEVICE_POLICY_DISPLAY
import android.Manifest.permission.MANAGE_DEVICE_POLICY_FACTORY_RESET
import android.Manifest.permission.MANAGE_DEVICE_POLICY_FUN
import android.Manifest.permission.MANAGE_DEVICE_POLICY_INPUT_METHODS
import android.Manifest.permission.MANAGE_DEVICE_POLICY_INSTALL_UNKNOWN_SOURCES
import android.Manifest.permission.MANAGE_DEVICE_POLICY_KEYGUARD
import android.Manifest.permission.MANAGE_DEVICE_POLICY_LOCALE
import android.Manifest.permission.MANAGE_DEVICE_POLICY_LOCATION
import android.Manifest.permission.MANAGE_DEVICE_POLICY_LOCK
import android.Manifest.permission.MANAGE_DEVICE_POLICY_LOCK_CREDENTIALS
import android.Manifest.permission.MANAGE_DEVICE_POLICY_LOCK_TASK
import android.Manifest.permission.MANAGE_DEVICE_POLICY_MANAGED_SUBSCRIPTIONS
import android.Manifest.permission.MANAGE_DEVICE_POLICY_MICROPHONE
import android.Manifest.permission.MANAGE_DEVICE_POLICY_MICROPHONE_TOGGLE
import android.Manifest.permission.MANAGE_DEVICE_POLICY_MOBILE_NETWORK
import android.Manifest.permission.MANAGE_DEVICE_POLICY_MODIFY_USERS
import android.Manifest.permission.MANAGE_DEVICE_POLICY_MTE
import android.Manifest.permission.MANAGE_DEVICE_POLICY_NEARBY_COMMUNICATION
import android.Manifest.permission.MANAGE_DEVICE_POLICY_ORGANIZATION_IDENTITY
import android.Manifest.permission.MANAGE_DEVICE_POLICY_PACKAGE_STATE
import android.Manifest.permission.MANAGE_DEVICE_POLICY_PHYSICAL_MEDIA
import android.Manifest.permission.MANAGE_DEVICE_POLICY_PRINTING
import android.Manifest.permission.MANAGE_DEVICE_POLICY_PROFILES
import android.Manifest.permission.MANAGE_DEVICE_POLICY_PROFILE_INTERACTION
import android.Manifest.permission.MANAGE_DEVICE_POLICY_QUERY_SYSTEM_UPDATES
import android.Manifest.permission.MANAGE_DEVICE_POLICY_RESET_PASSWORD
import android.Manifest.permission.MANAGE_DEVICE_POLICY_RESTRICT_PRIVATE_DNS
import android.Manifest.permission.MANAGE_DEVICE_POLICY_RUNTIME_PERMISSIONS
import android.Manifest.permission.MANAGE_DEVICE_POLICY_RUN_IN_BACKGROUND
import android.Manifest.permission.MANAGE_DEVICE_POLICY_SAFE_BOOT
import android.Manifest.permission.MANAGE_DEVICE_POLICY_SCREEN_CAPTURE
import android.Manifest.permission.MANAGE_DEVICE_POLICY_SCREEN_CONTENT
import android.Manifest.permission.MANAGE_DEVICE_POLICY_SECURITY_LOGGING
import android.Manifest.permission.MANAGE_DEVICE_POLICY_SMS
import android.Manifest.permission.MANAGE_DEVICE_POLICY_STATUS_BAR
import android.Manifest.permission.MANAGE_DEVICE_POLICY_SUPPORT_MESSAGE
import android.Manifest.permission.MANAGE_DEVICE_POLICY_SYSTEM_DIALOGS
import android.Manifest.permission.MANAGE_DEVICE_POLICY_SYSTEM_UPDATES
import android.Manifest.permission.MANAGE_DEVICE_POLICY_TIME
import android.Manifest.permission.MANAGE_DEVICE_POLICY_USB_DATA_SIGNALLING
import android.Manifest.permission.MANAGE_DEVICE_POLICY_USB_FILE_TRANSFER
import android.Manifest.permission.MANAGE_DEVICE_POLICY_VPN
import android.Manifest.permission.MANAGE_DEVICE_POLICY_WALLPAPER
import android.Manifest.permission.MANAGE_DEVICE_POLICY_WIFI
import android.Manifest.permission.MANAGE_DEVICE_POLICY_WINDOWS
import android.Manifest.permission.MANAGE_DEVICE_POLICY_WIPE_DATA
import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.app.admin.FactoryResetProtectionPolicy
import android.app.admin.SystemUpdatePolicy
import android.content.ComponentName
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.os.UserManager
import androidx.annotation.RequiresApi
import com.android.certification.niap.permission.dpctester.R
import com.android.certification.niap.permission.dpctester.common.DevicePolicyManagerGateway.DeviceOwnerLevel
import com.android.certification.niap.permission.dpctester.common.DevicePolicyManagerGatewayImpl
import com.android.certification.niap.permission.dpctester.test.exception.BypassTestException
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestModuleBase
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestRunner
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTest
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestModule
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTool
import java.lang.UnsupportedOperationException


@PermissionTestModule("DPC Test Cases", label = "Run DPC Test Cases")
class DPCTestModule(val ctx: Activity): PermissionTestModuleBase(ctx){
    override var TAG: String = DPCTestModule::class.java.simpleName
    val dpm = DevicePolicyManagerGatewayImpl(ctx)
    val pm  =ctx.packageManager
    val dpsLevel:DeviceOwnerLevel = PermissionTool.getDeviceOwnerLevel(dpm)
    val nopermMode = ctx.resources.getBoolean(R.bool.inverse_test_result)

    init {
        //Enable Permission Check Flag
        //"enable_permission_based_access"
        //DeviceConfigTool.setProperty("device_policy_manager", "enable_permission_based_access","true",false)
        //logger.system("enable_permission_based_access"+DeviceConfigTool.getProperty("device_policy_manager", "enable_permission_based_access")!!)
        inverseForPlatformTesting=false
    }


    override fun resultHook(result:PermissionTestRunner.Result):PermissionTestRunner.Result{

        if(pm.checkPermission(ctx.packageName,result.source.permission)
            == android.content.pm.PackageManager.PERMISSION_DENIED
            && result.success==false
            && nopermMode==false
            && dpsLevel == DeviceOwnerLevel.DPS_ACTIVE_ADMIN_APP
        ){
            result.bypassed = true
            logger.debug(
                "Package Manager does not recognize the permission `${result.source.permission}`.")
        }

        return result;
    }


    //Permission Test Cases for SDK34

    /*@PermissionTest(MANAGE_DEVICE_POLICY_ACCESSIBILITY,34,35)
    fun testAccessibility(){
        println("The test for MANAGE_DEVICE_POLICY_ACCESSIBILITY is not implemented yet")

        //val permission = PermissionTool.getCurrentPermission(javaClass.enclosingMethod!!.name)
        //throw YetImplementedException();
    }*/
    @PermissionTest(MANAGE_DEVICE_POLICY_ACCOUNT_MANAGEMENT,34,35)
    fun testAccountManagement(){
        dpm.setAccountManagementDisabled(false,"accountName")
    }
    /* ACROSS_USERS Permissions
    @PermissionTest(MANAGE_DEVICE_POLICY_ACROSS_USERS,34,35)
    fun testAcrossUsers(){
        println("The test for MANAGE_DEVICE_POLICY_ACROSS_USERS is not implemented yet")
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_ACROSS_USERS_FULL,34,35)
    fun testAcrossUsersFull(){
        println("The test for MANAGE_DEVICE_POLICY_ACROSS_USERS_FULL is not implemented yet")
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_ACROSS_USERS_SECURITY_CRITICAL,34,35)
    fun testAcrossUsersSecurityCritical(){
        println("The test for MANAGE_DEVICE_POLICY_ACROSS_USERS_SECURITY_CRITICAL is not implemented yet")
    }
     */
    @PermissionTest(MANAGE_DEVICE_POLICY_AIRPLANE_MODE,34,35)
    fun testAirplaneMode(){
        //println("The test for MANAGE_DEVICE_POLICY_AIRPLANE_MODE is not implemented yet")
        checkUserRestriction(UserManager.DISALLOW_AIRPLANE_MODE)
    }
    @PermissionTest("MANAGE_DEVICE_POLICY_APP_EXEMPTIONS",34,35)
    fun testAppExemptions(){
        //println("The test for MANAGE_DEVICE_POLICY_APP_EXEMPTIONS is not implemented yet")
        dpm.setApplicationExemptions(ctx.packageName, hashSetOf(0,1))
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_APP_RESTRICTIONS,34,35)
    fun testAppRestrictions(){
        dpm.setApplicationRestrictions(ctx.packageName, Bundle.EMPTY,{},{e->throw e})
    }
    /*@PermissionTest(MANAGE_DEVICE_POLICY_APP_USER_DATA,34,35)
    fun testAppUserData(){
        println("The test for MANAGE_DEVICE_POLICY_APP_USER_DATA is not implemented yet")
    }*/
    @PermissionTest(MANAGE_DEVICE_POLICY_AUTOFILL,34,35)
    fun testAutofill(){
        checkUserRestriction(UserManager.DISALLOW_AUTOFILL)
    }
    /*@PermissionTest(MANAGE_DEVICE_POLICY_BACKUP_SERVICE,34,35)
    fun testBackupService(){
        println("The test for MANAGE_DEVICE_POLICY_BACKUP_SERVICE is not implemented yet")
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_BUGREPORT,34,35)
    fun testBugreport(){
        println("The test for MANAGE_DEVICE_POLICY_BUGREPORT is not implemented yet")
    }*/

    @PermissionTest(MANAGE_DEVICE_POLICY_COMMON_CRITERIA_MODE,34,35)
    fun testCommonCriteriaMode(){
        dpm.setCommonCriteriaModeEnabled(true)
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_DEFAULT_SMS,34,35)
    fun testDefaultSms(){
        dpm.setDefaultSmsApplication("sms.packagename")
    }
    /*@PermissionTest(MANAGE_DEVICE_POLICY_DEVICE_IDENTIFIERS,34,35)
    fun testDeviceIdentifiers(){
        println("The test for MANAGE_DEVICE_POLICY_DEVICE_IDENTIFIERS is not implemented yet")
    }*/

    @RequiresApi(Build.VERSION_CODES.R)
    @PermissionTest(MANAGE_DEVICE_POLICY_FACTORY_RESET,34,35)
    fun testFactoryReset(){
        dpm.setFactoryResetProtectionPolicy(
            FactoryResetProtectionPolicy.Builder()
                .setFactoryResetProtectionAccounts(listOf("account1","account2"))
                .setFactoryResetProtectionEnabled(false).build())
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_INPUT_METHODS,34,35)
    fun testInputMethods(){
        dpm.setPermittedInputMethods(mutableListOf("com.package","com.package2"),{},{ e->throw e})
        //println("The test for MANAGE_DEVICE_POLICY_INPUT_METHODS is not implemented yet")
    }
    /*@PermissionTest(MANAGE_DEVICE_POLICY_KEEP_UNINSTALLED_PACKAGES,34,35)
    fun testKeepUninstalledPackages(){
        println("The test for MANAGE_DEVICE_POLICY_KEEP_UNINSTALLED_PACKAGES is not implemented yet")
    }*/


    @PermissionTest(MANAGE_DEVICE_POLICY_KEYGUARD,34,35)
    fun testKeyguard(){
        val configuration = PersistableBundle.EMPTY
        dpm.setTrustAgentConfiguration(
            ComponentName("com.trustagent","com.trustagent.xxx"),configuration)
    }


    @PermissionTest(MANAGE_DEVICE_POLICY_LOCK,34,35)
    fun testLock(){
        throw BypassTestException(
            "The test for MANAGE_DEVICE_POLICY_LOCK is currently infeasible to test. API is not restricted.")
        //isPermissionCheckFlagEnabled() = true? dpm.setMaximumTimeToLock(1000*30)
        //!isUnicornFlagEnabled dpm.lockNow({},{e->throw e})
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_LOCK_CREDENTIALS,34,35)
    fun testLockCredentials(){
        dpm.setPasswordExpirationTimeOut(30)
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_LOCK_TASK,34,35)
    fun testLockTask(){
        dpm.setLockTaskPackages(arrayOf("com.package","com.package2"),{},{e->throw e});
    }
    /*@PermissionTest(MANAGE_DEVICE_POLICY_METERED_DATA,34,35)
    fun testMeteredData(){
        println("The test for MANAGE_DEVICE_POLICY_METERED_DATA is not implemented yet")
    }*/
    @PermissionTest(MANAGE_DEVICE_POLICY_MTE,34,35)
    fun testMte(){
        try {
            dpm.setMtePolicy(DevicePolicyManager.MTE_NOT_CONTROLLED_BY_POLICY)
        } catch (ex:UnsupportedOperationException){
            throw BypassTestException("The test for MANAGE_DEVICE_POLICY_MTE is failed because mte is not supported on this device")
        }
    }
    /*@PermissionTest(MANAGE_DEVICE_POLICY_NETWORK_LOGGING,34,35)
    fun testNetworkLogging(){
        println("The test for MANAGE_DEVICE_POLICY_NETWORK_LOGGING is not implemented yet")
    }*/
    @PermissionTest(MANAGE_DEVICE_POLICY_ORGANIZATION_IDENTITY,34,35)
    fun testOrganizationIdentity(){
        dpm.setOrganizationName("Testers' Organization Name",{},{ e-> throw e})
    }
    /*@PermissionTest(MANAGE_DEVICE_POLICY_OVERRIDE_APN,34,35)
    fun testOverrideApn(){
        println("The test for MANAGE_DEVICE_POLICY_OVERRIDE_APN is not implemented yet")
    }*/
    @PermissionTest(MANAGE_DEVICE_POLICY_PACKAGE_STATE,34,35)
    fun testPackageState(){
        dpm.isPackageSuspended("com.google.android.youtube")
    }
    /*@PermissionTest(MANAGE_DEVICE_POLICY_PRIVATE_DNS,34,35)
    fun testPrivateDns(){
        println("The test for MANAGE_DEVICE_POLICY_PRIVATE_DNS is not implemented yet")
    }*/
    @PermissionTest(MANAGE_DEVICE_POLICY_PROFILE_INTERACTION,34,35)
    fun testProfileInteraction(){
        dpm.clearCrossProfileIntentFilters()
    }
    /*@PermissionTest(MANAGE_DEVICE_POLICY_PROXY,34,35)
    fun testProxy(){
        println("The test for MANAGE_DEVICE_POLICY_PROXY is not implemented yet")
    }*/
    @PermissionTest(MANAGE_DEVICE_POLICY_QUERY_SYSTEM_UPDATES,34,35)
    fun testQuerySystemUpdates(){
        //Check Flags.permissionMigrationForZeroTrustImplEnabled())
        dpm.getPendingSystemUpdate()

    }
    @PermissionTest(MANAGE_DEVICE_POLICY_RESET_PASSWORD,34,35)
    fun testResetPassword(){
        //byte array describes 'password'x4.
        dpm.setResetPasswordToken(byteArrayOf(
            0x70,0x61,0x73,0x73,0x77,0x6f,0x72,0x64,
            0x70,0x61,0x73,0x73,0x77,0x6f,0x72,0x64,
            0x70,0x61,0x73,0x73,0x77,0x6f,0x72,0x64,
            0x70,0x61,0x73,0x73,0x77,0x6f,0x72,0x64));
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_RUNTIME_PERMISSIONS,34,35)
    fun testRuntimePermissions(){
        dpm.setPermissionGrantState(ctx.packageName,ACCESS_FINE_LOCATION,
            DevicePolicyManager.PERMISSION_GRANT_STATE_DEFAULT,{},{e->throw e})
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_SCREEN_CAPTURE,34,35)
    fun testScreenCapture(){
        dpm.setScreenCaptureDisabled(false)
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_SECURITY_LOGGING,34,35)
    fun testSecurityLogging(){
        dpm.setSecurityLoggingEnabled(false,{},{e->throw e})
    }
    /*@PermissionTest(MANAGE_DEVICE_POLICY_SETTINGS,34,35)
    fun testSettings(){
        println("The test for MANAGE_DEVICE_POLICY_SETTINGS is not implemented yet")
    }*/
    @PermissionTest(MANAGE_DEVICE_POLICY_SMS,34,35)
    fun testSms(){
        checkUserRestriction(UserManager.DISALLOW_SMS);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_STATUS_BAR,34,35)
    fun testStatusBar(){
        dpm.setStatusBarDisabled(false,{},{throw it})
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_SUPPORT_MESSAGE,34,35)
    fun testSupportMessage(){
        //And Should isPermissionCheckFlagEnabled() be true?
        dpm.setShortSupportMessage("Hello Short Support Message!")
    }
    /*@PermissionTest(MANAGE_DEVICE_POLICY_SUSPEND_PERSONAL_APPS,34,35)
    fun testSuspendPersonalApps(){
        println("The test for MANAGE_DEVICE_POLICY_SUSPEND_PERSONAL_APPS is not implemented yet")
    }*/
    /*@PermissionTest(MANAGE_DEVICE_POLICY_SYSTEM_APPS,34,35)
    fun testSystemApps(){
        println("The test for MANAGE_DEVICE_POLICY_SYSTEM_APPS is not implemented yet")
    }*/
    @PermissionTest(MANAGE_DEVICE_POLICY_SYSTEM_UPDATES,34,35)
    fun testSystemUpdates(){
        dpm.setSystemUpdatePolicy(SystemUpdatePolicy.createAutomaticInstallPolicy())
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_USB_DATA_SIGNALLING,34,35)
    fun testUsbDataSignalling(){
        dpm.setUsbDataSignalingEnabled(true,{},{throw it})
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_USB_FILE_TRANSFER,34,35)
    fun testUsbFileTransfer(){
        //println("The test for MANAGE_DEVICE_POLICY_USB_FILE_TRANSFER is not implemented yet")
        checkUserRestriction(UserManager.DISALLOW_USB_FILE_TRANSFER);
    }
    /*@PermissionTest(MANAGE_DEVICE_POLICY_USERS,34,35)
    fun testUsers(){
        println("The test for MANAGE_DEVICE_POLICY_USERS is not implemented yet")
    }*/
    @PermissionTest(MANAGE_DEVICE_POLICY_VPN,34,35)
    fun testVpn(){
        checkUserRestriction(UserManager.DISALLOW_CONFIG_VPN);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_WIFI,34,35)
    fun testWifi(){
        dpm.hasLockDownAdminConfigureNetworks()
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_WIPE_DATA,34,35)
    fun testWipeData(){
        //And Should isPermissionCheckFlagEnabled() be true
        dpm.setMaximumFailedPasswordsForWipe(3000,{},{e->throw e})
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_CERTIFICATES,34,35)
    fun testCertificates() {
        //it is require to call from transaction and flag should be enabled
        // DeviceConfig.getBoolean(
        //                NAMESPACE_DEVICE_POLICY_MANAGER,
        //                PERMISSION_BASED_ACCESS_EXPERIMENT_FLAG,
        //                DEFAULT_VALUE_PERMISSION_BASED_ACCESS_FLAG);

        //Flags.permissionMigrationForZeroTrustImplEnabled<=prerequist
        dpm.getEnrollmentSpecifiedId()
        //dpm.installKeyValuePair(null)?
    }
    //
    @PermissionTest(MANAGE_DEVICE_POLICY_APPS_CONTROL,34,35)
    fun testAppsControl() {
        dpm.getUserControlDisabledPackages()
    }

    @PermissionTest(MANAGE_DEVICE_POLICY_CAMERA,34)
    fun testCamera() {
        checkUserRestriction("no_camera")
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_AUDIO_OUTPUT,34)
    fun testAudioOutput(){
        checkUserRestriction(UserManager.DISALLOW_ADJUST_VOLUME);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_BLUETOOTH,34)
    fun testBluetooth(){
        checkUserRestriction(UserManager.DISALLOW_BLUETOOTH);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_CALLS,34)
    fun testCalls(){
        checkUserRestriction(UserManager.DISALLOW_OUTGOING_CALLS);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_DEBUGGING_FEATURES,34)
    fun testDebuggingFeatures(){
        //should always false for running test cases
        clearUserRestriction(UserManager.DISALLOW_DEBUGGING_FEATURES)
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_DISPLAY,34)
    fun testDisplay(){
        checkUserRestriction(UserManager.DISALLOW_AMBIENT_DISPLAY);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_INSTALL_UNKNOWN_SOURCES,34)
    fun testInstallUnknownSources(){
        checkUserRestriction(UserManager.DISALLOW_INSTALL_UNKNOWN_SOURCES);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_LOCALE,34)
    fun testLocale(){
        checkUserRestriction(UserManager.DISALLOW_CONFIG_LOCALE);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_LOCATION,34)
    fun testLocation(){
        checkUserRestriction(UserManager.DISALLOW_CONFIG_LOCATION);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_MOBILE_NETWORK,34)
    fun testMobileNetwork(){
        checkUserRestriction(UserManager.DISALLOW_CONFIG_MOBILE_NETWORKS);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_MODIFY_USERS,34)
    fun testModifyUsers(){
        checkUserRestriction(UserManager.DISALLOW_ADD_USER);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_NEARBY_COMMUNICATION,34)
    fun testNearbyCommunication(){
        checkUserRestriction(UserManager.DISALLOW_OUTGOING_BEAM);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_PHYSICAL_MEDIA,34)
    fun testPhysicalMedia(){
        checkUserRestriction(UserManager.DISALLOW_MOUNT_PHYSICAL_MEDIA);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_PRINTING,34)
    fun testPrinting(){
        checkUserRestriction(UserManager.DISALLOW_PRINTING);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_PROFILES,34)
    fun testProfiles(){
        checkUserRestriction(UserManager.ALLOW_PARENT_PROFILE_APP_LINKING);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_RESTRICT_PRIVATE_DNS,34)
    fun testRestrictPrivateDns(){
        checkUserRestriction(UserManager.DISALLOW_CONFIG_PRIVATE_DNS);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_RUN_IN_BACKGROUND,34)
    fun testRunInBackground(){
        checkUserRestriction("no_run_in_background");
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_SAFE_BOOT,34)
    fun testSafeBoot(){
        checkUserRestriction(UserManager.DISALLOW_SAFE_BOOT);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_SCREEN_CONTENT,34)
    fun testScreenContent(){
        checkUserRestriction(UserManager.DISALLOW_CONTENT_CAPTURE);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_MICROPHONE,34)
    fun testMicrophone(){
        checkUserRestriction(UserManager.DISALLOW_MICROPHONE_TOGGLE);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_SMS,34)
    fun testSMS(){
        checkUserRestriction(UserManager.DISALLOW_SMS);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_FUN,34)
    fun testFun(){
        checkUserRestriction(UserManager.DISALLOW_FUN);
    }

    //Clear User Transaction Tests
    @PermissionTest(MANAGE_DEVICE_POLICY_SYSTEM_DIALOGS,34)
    fun testSystemDialogs(){
        clearUserRestriction(UserManager.DISALLOW_SYSTEM_ERROR_DIALOGS);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_TIME,34)
    fun testTime(){
        clearUserRestriction(UserManager.DISALLOW_CONFIG_DATE_TIME);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_VPN,34)
    fun testVPN(){
        clearUserRestriction(UserManager.DISALLOW_CONFIG_VPN);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_WALLPAPER,34)
    fun testWallpaper(){
        clearUserRestriction(UserManager.DISALLOW_SET_WALLPAPER);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_WINDOWS,34)
    fun testWindows(){
        checkUserRestriction(UserManager.DISALLOW_CREATE_WINDOWS);
    }

    //The test cases For Android 15

    //UserManger + DevicePolicyService related Permissions
    @PermissionTest(MANAGE_DEVICE_POLICY_CAMERA_TOGGLE,35)
    fun testCameraToggle(){
        checkUserRestriction(UserManager.DISALLOW_CAMERA_TOGGLE);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_MICROPHONE_TOGGLE,35)
    fun testMicrophoneToggle(){
        checkUserRestriction(UserManager.DISALLOW_MICROPHONE_TOGGLE);
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_ASSIST_CONTENT,35)
    fun testAssistContent(){
        //Also blocked by system flag?
        checkUserRestriction(UserManager.DISALLOW_ASSIST_CONTENT);
    }
    //Normal Test Cases
    @PermissionTest(MANAGE_DEVICE_POLICY_BLOCK_UNINSTALL,35)
    fun testUninstallBlocked(){
        dpm.setUninstallBlocked(
            "test.package.name",false,{},{e->throw e})
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_CONTENT_PROTECTION,35)
    fun testContentProtectionPolicy(){
        val flag = 1 shl 7 //See enterprisepolicy.java
        dpm.setContentProtectionPolicy(flag)
    }
    @PermissionTest(MANAGE_DEVICE_POLICY_MANAGED_SUBSCRIPTIONS,35)
    fun testManagedSubscriptions(){
        dpm.getSubscriptionIds()
    }
    @PermissionTest("MANAGE_DEVICE_POLICY_STORAGE_LIMIT",35)
    fun testStorageLimit(){
        dpm.policySizeForAdmin
    }
    @PermissionTest("QUERY_DEVICE_STOLEN_STATE",35)
    fun testQueryDeviceStolenState(){
        //Check Admin Flag and if not supported, throw bypass exception
        if(!PermissionTool.getAdminFlag("deviceTheftImplEnabled")){
            throw BypassTestException("The test for QUERY_DEVICE_STOLEN_STATE is failed because device theft feature is not enabled on this device")
        }
        dpm.isDevicePotentiallyStolen()
    }
    @PermissionTest("MANAGE_DEVICE_POLICY_AUDIT_LOGGING",35)
    fun testAuditLogEnabled(){
        dpm.setAuditLogEnabled(false)
    }
    /* ?
    @PermissionTest(MANAGE_DEVICE_POLICY_THREAD_NETWORK,35)
    fun testThreadNetwork(){
        checkUserRestriction(UserManager.DISALLOW_THREAD_NETWORK);
    }*/

    @PermissionTest("MANAGE_DEVICE_POLICY_APP_FUNCTIONS",36)
    fun testAppFunctions(){
        dpm.setAppFunctionPolicy(0)//not controlled
    }

    ////////////////////////////////////////////////////////////
    // Local Scope Tools Section
    private fun clearUserRestriction(aRestriction:String){
        //Make sure to always set testing value as false
        dpm.setUserRestriction(aRestriction, false,{},{e->throw e})
    }
    //UserManager + DevicePolicyService related Permissions
    private fun checkUserRestriction(aRestriction:String){
        val value = dpm.userRestrictions.contains(aRestriction)
        //Make sure to reset the testing value
        dpm.setUserRestriction(aRestriction, value,{},{e->throw e})
        dpm.setUserRestriction(aRestriction, !value,{},{e->throw e})
    }
}