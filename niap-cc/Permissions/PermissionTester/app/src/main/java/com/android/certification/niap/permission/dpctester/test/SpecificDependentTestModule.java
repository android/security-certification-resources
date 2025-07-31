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
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.QUERY_ALL_PACKAGES;
import static android.Manifest.permission.USE_EXACT_ALARM;
import static android.Manifest.permission.UWB_RANGING;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.LocaleConfig;
import android.app.PendingIntent;
import android.content.AttributionSource;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.LocaleList;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.telephony.AccessNetworkConstants;
import android.telephony.CellInfo;
import android.telephony.NetworkScanRequest;
import android.telephony.RadioAccessSpecifier;
import android.telephony.TelephonyManager;
import android.telephony.TelephonyScanManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.android.certification.niap.permission.dpctester.MainActivity;
import com.android.certification.niap.permission.dpctester.common.ReflectionUtil;
import com.android.certification.niap.permission.dpctester.test.exception.BypassTestException;
import com.android.certification.niap.permission.dpctester.test.runner.SignaturePermissionTestModuleBase;
import com.android.certification.niap.permission.dpctester.test.tool.BinderTransaction;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTest;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * The test will be provided on 'specific-deps' variant module,
 * and it's a only test for that variant.
 * Some permissions require specific permissions to be granted in order to work.
 * So if we want to check whether it is working or not, we should provide a manifest file
 * which has corresponding preliminary permission.
 * This module is intended to test those test case, and running with such a manifest file.
 * So basically these test cases should be failed, like no-perm variant test cases.
 */
@PermissionTestModule(name="Specific Permission Dependency Test Cases",
        prflabel = "Specific Dependent",label = "Run Specific Dependency Tests",sync = true)
public class SpecificDependentTestModule extends SignaturePermissionTestModuleBase {
    RuntimeTestModule baseModule;
    public SpecificDependentTestModule(@NonNull Activity activity) {
        super(activity);
        baseModule = new RuntimeTestModule(activity);
    }
    private <T> T systemService(Class<T> clazz) {
        return Objects.requireNonNull(getService(clazz), "[npe_system_service]" + clazz.getSimpleName());
    }

    //The test requires UWB_PRIVILEGED permission for running,
    //Also the test only runs only on uwb supported devices.
    @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @PermissionTest(permission=UWB_RANGING, sdkMin=31)
    public void testUwbRanging(){
        if (!mPackageManager.hasSystemFeature("android.hardware.uwb")) {
            throw new BypassTestException(
                    "This permission requires the android.hardware.uwb feature");
        }
        // The API guarded by this permission is also guarded by the signature
        // permission UWB_PRIVILEGED, so if this signature permission is not granted
        // then skip this test. UWB_PRIVILEGED is a signature level
        if (mContext.checkSelfPermission("android.permission.UWB_PRIVILEGED")
                != PackageManager.PERMISSION_GRANTED) {
            throw new BypassTestException(
                    "The UWB_PRIVILEGED permission must be granted for this test");
        }
        // The UwbManager with the API guarded by this permission is hidden, so a
        // direct transact is required.
        final AttributionSource attributionSource =
                AttributionSource.myAttributionSource();
        Parcelable sessionHandle = new Parcelable() {
            @Override
            public int describeContents() {
                return 0;
            }
            @Override
            public void writeToParcel(@NonNull Parcel parcel, int i) {
                parcel.writeInt(1);
                parcel.writeString(attributionSource.getPackageName());
                parcel.writeInt(attributionSource.getUid());
                parcel.writeInt(attributionSource.getPid());
            }
        };

        BinderTransaction.getInstance().invoke(Transacts.UWB_SERVICE,
                Transacts.UWB_DESCRIPTOR,"openRanging",
                attributionSource, sessionHandle,
                (IBinder) null,
                new PersistableBundle(), ""
        );
    }
    //The test requires ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION permission for running,
    @PermissionTest(permission="RADIO_SCAN_WITHOUT_LOCATION", sdkMin=30)
    public void testRadioScanWithoutLocation(){
        // if the app has been granted a location permission then skip this test as this
        // permission is intended to allow scans without location.
        if (checkPermissionGranted(ACCESS_COARSE_LOCATION)||checkPermissionGranted(ACCESS_FINE_LOCATION)){
            throw new BypassTestException(
                    "Radio scan without location permission is intended to allow scans without location. " +
                            "This app grants a location permission. so skip this test.");
        }
        boolean scanStartedSuccessfully = systemService(WifiManager.class).startScan();
        if(!scanStartedSuccessfully) {
            throw new SecurityException(
                    "Wifi scan could not be started during "
                            + "RADIO_SCAN_WITHOUT_LOCATION test");
        }
    }

    @SuppressLint("MissingPermission")
    //@PermissionTest(permission="NETWORK_SCAN", sdkMin=29)
    public void testNetworkScan(){
        // Starting in Android 12 attempting a network scan with both this permission
        // as well as a location permission can cause a RuntimeException.
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (checkPermissionGranted("android.permission.NETWORK_SCAN")
                    && (checkPermissionGranted(ACCESS_COARSE_LOCATION)
                    || checkPermissionGranted(ACCESS_FINE_LOCATION))) {
                throw new BypassTestException("This test should only run when the "
                        + "location permissions are not granted");
            }
        }*/

        int bands[]={0};
        ArrayList<String> PLMNIds = new ArrayList<String>(Arrays.asList("42501"));

        RadioAccessSpecifier radioAccessSpecifiers[];

        bands[0] = AccessNetworkConstants.UtranBand.BAND_1;
        radioAccessSpecifiers = new RadioAccessSpecifier[1];
        radioAccessSpecifiers[0] = new RadioAccessSpecifier(
                AccessNetworkConstants.AccessNetworkType.UTRAN,
                bands,
                null);

        NetworkScanRequest request = new NetworkScanRequest(
                NetworkScanRequest.SCAN_TYPE_ONE_SHOT,
                radioAccessSpecifiers, 1, 60,
                true, 1, PLMNIds);
        TelephonyScanManager.NetworkScanCallback callback =
                new TelephonyScanManager.NetworkScanCallback() {
                    @Override
                    public void onResults(List<CellInfo> results) {
                        logger.debug("onResults: " + results);
                    }

                    @Override
                    public void onComplete() {
                        logger.debug("onComplete");
                    }

                    @Override
                    public void onError(int error) {
                        logger.debug("onError: " + error);
                    }
                };
        //SecurityException due to uncertain reason
        //Can be test with shell identity?
        systemService(TelephonyManager.class).requestNetworkScan(request,
                AsyncTask.SERIAL_EXECUTOR,
                callback);
    }

    //Block if the QUERY_ALL_PACKAGES permission is not granted
    @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @PermissionTest(permission="LIST_ENABLED_CREDENTIAL_PROVIDERS", sdkMin=34)
    public void testListEnabledCredentialProviders(){
        if(checkPermissionGranted(QUERY_ALL_PACKAGES)){
            throw new BypassTestException(
                    "This test works only when QUERY_ALL_PACKAGES permission is not granted.");
        }
        BinderTransaction.getInstance().invoke(
                Context.CREDENTIAL_SERVICE,
                Transacts.CREDENTIAL_DESCRIPTOR,
                "getCredentialProviderServices",
                appUid, Binder.getCallingPid());

    }
    @PermissionTest(permission="MANAGE_SMARTSPACE", sdkMin=31)
    public void testManageSmartspace(){
        // Note this is fragile since the implementation of SmartspaceSessionId can
        // change in the future, but since there is no way to construct an instance
        // of SmartspaceSessionId this at least allows the test to proceed.
        if(checkPermissionGranted("android.permission.ACCESS_SMARTSPACE") && !isPlatformSignatureMatch){
            //when access smart space permission is granted this test would be passed with ordinal signature
            throw new BypassTestException("Cannot test this case when ACCESS_SMARTSPACE is granted.");
        }
        Parcelable smartspaceId = new Parcelable() {
            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {
                parcel.writeString("test-smartspace-id");
                parcel.writeTypedObject(UserHandle.getUserHandleForUid(appUid), 0);
            }
        };
        BinderTransaction.getInstance().invoke(Transacts.SMART_SPACE_SERVICE,
                Transacts.SMART_SPACE_DESCRIPTOR, "destroySmartspaceSession", smartspaceId);
    }

    //Need to disable PACKAGE_USAGE_STATS to RUN Test
    @PermissionTest(permission="GET_BINDING_UID_IMPORTANCE", sdkMin=35)
    public void testGetBindingUidImportance(){
        if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.PACKAGE_USAGE_STATS)
                == PackageManager.PERMISSION_GRANTED) {
            throw new BypassTestException(
                    "This permission is not evlauated when PACKAGE_USAGE_STATS is allowed");
        }
        //logger.system(ReflectionTool.Companion.checkDeclaredMethod(am,"getBindingUidImportance").toString());
        ReflectionUtil.invoke(systemService(ActivityManager.class),
                "getBindingUidImportance",
                new Class<?>[] {int.class},appUid);
    }

    @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @PermissionTest(permission="SET_APP_SPECIFIC_LOCALECONFIG", sdkMin=34)
    public void testSetAppSpecificLocaleconfig(){
        if(!checkPermissionGranted(QUERY_ALL_PACKAGES)){
            throw new BypassTestException(
                    "This test works only when QUERY_ALL_PACKAGES permission is granted.");
        }

        LocaleList OVERRIDE_LOCALES =
                LocaleList.forLanguageTags("en-US,fr-FR,zh-Hant-TW");

        //for checking it need to set other application locale config
        BinderTransaction.getInstance().invoke(
                Context.LOCALE_SERVICE,
                Transacts.LOCALE_DESCRIPTOR,
                "setOverrideLocaleConfig",
                "com.android.certifications.niap.permissions.companion",0,
                new LocaleConfig(OVERRIDE_LOCALES));

    }


    @PermissionTest(permission="SCHEDULE_EXACT_ALARM", sdkMin=34)
    public void testScheduleExactAlarm(){

        if (ActivityCompat.checkSelfPermission(mContext, USE_EXACT_ALARM)
                == PackageManager.PERMISSION_GRANTED){
            throw new BypassTestException(
                    "If the USE_EXACT_ALARM permission is granted, the test will be passed. let's skip it");
        }

        Intent intent = new Intent(mContext, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent,
                PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = mContext.getSystemService(AlarmManager.class);
        alarmManager.setExact(AlarmManager.RTC, System.currentTimeMillis() + 60 * 1000,
                pendingIntent);
        alarmManager.cancel(pendingIntent);
    }


}
