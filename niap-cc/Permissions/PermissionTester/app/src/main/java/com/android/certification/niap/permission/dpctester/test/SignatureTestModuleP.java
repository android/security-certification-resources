//Auto generated file InstallPermissionTestModule.java by CoderPorterPlugin
/*
 * Copyright 2024 The Android Open Source Project
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
import android.companion.CompanionDeviceManager;
import android.content.ComponentName;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.net.MacAddress;
import android.os.Build;
import android.os.IBinder;
import android.os.UserHandle;
import android.os.UserManager;

import androidx.annotation.NonNull;

import com.android.certification.niap.permission.dpctester.MainActivity;
import com.android.certification.niap.permission.dpctester.common.ReflectionUtil;
import com.android.certification.niap.permission.dpctester.test.exception.BypassTestException;
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestRunner;
import com.android.certification.niap.permission.dpctester.test.runner.SignaturePermissionTestModuleBase;
import com.android.certification.niap.permission.dpctester.test.tool.BinderTransaction;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTest;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestModule;

import java.util.Objects;
import java.util.function.Consumer;

@PermissionTestModule(name="Signature 28(P) Test Cases",prflabel = "Pie(9)")
public class SignatureTestModuleP extends SignaturePermissionTestModuleBase {
	public SignatureTestModuleP(@NonNull Activity activity) {
		super(activity);
	}

	@NonNull
	@Override
	public PrepareInfo prepare(Consumer<PermissionTestRunner.Result> callback){
		return super.prepare(callback);
	}
	private <T> T systemService(Class<T> clazz){
		return Objects.requireNonNull(getService(clazz),"[npe_system_service]"+clazz.getSimpleName());
	}
	
	@PermissionTest(permission="TEMPORARY_ENABLE_ACCESSIBILITY", sdkMin=28, sdkMax=33)
	public void testTemporaryEnableAccessibility(){

		ComponentName componentName = new ComponentName(mContext, MainActivity.class);
		BinderTransaction.getInstance().invoke(Transacts.ACCESSIBILITY_SERVICE,
				Transacts.ACCESSIBILITY_DESCRIPTOR,
				"temporaryEnableAccessibilityStateUntilKeyguardRemoved",
				componentName, true);
	}

	@PermissionTest(permission="CAMERA_SEND_SYSTEM_EVENTS", sdkMin=28, sdkMax=28)
	public void testCameraSendSystemEvents(){
		BinderTransaction.getInstance().invoke(Transacts.CAMERA_SERVICE, Transacts.CAMERA_DESCRIPTOR,
				"notifySystemEvent", 0, new int[]{});
	}

	@PermissionTest(permission="CONTROL_DISPLAY_SATURATION", sdkMin=28, sdkMax=28)
	public void testControlDisplaySaturation(){
		ReflectionUtil.invoke(systemService(DisplayManager.class), "setSaturationLevel",
				 new Class<?>[]{float.class}, 1);
	}

	@PermissionTest(permission="CRYPT_KEEPER", sdkMin=28, sdkMax=31)
	public void testCryptKeeper(){
		BinderTransaction.getInstance().invoke(Transacts.MOUNT_SERVICE, Transacts.MOUNT_DESCRIPTOR,
				"getEncryptionState");
	}

	@PermissionTest(permission="FILTER_EVENTS", sdkMin=28, sdkMax=28)
	public void testFilterEvents(){
		// This causes an ANR, so skip the test if the permission is granted
		if (checkPermissionGranted("android.permission.FILTER_EVENTS")) {
			throw new BypassTestException(
					"The API guarded by this permission will cause an ANR");
		}
		BinderTransaction.getInstance().invoke(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
				"inputDispatchingTimedOut", 1, false, "Test FILTER_EVENTS");
	}

	@PermissionTest(permission="GET_ACCOUNTS_PRIVILEGED", sdkMin=28)
	public void testGetAccountsPrivileged(){
		systemService(UserManager.class).getUserName();
	}

	@PermissionTest(permission="MANAGE_ACTIVITY_STACKS", sdkMin=28, sdkMax=30)
	public void testManageActivityStacks(){
		//MANAGE_ACTIVITY_STACKS is a deprecated permission, we should bypass this permisson from s

		String service = "activity_task";
		String descriptor = Transacts.ACTIVITY_TASK_DESCRIPTOR;
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P) {
			service = Context.ACTIVITY_SERVICE;
			descriptor = Transacts.ACTIVITY_DESCRIPTOR;
		}
		BinderTransaction.getInstance().invoke(service, descriptor, "getTaskDescription", 0);
	}

	@PermissionTest(permission="MODIFY_QUIET_MODE", sdkMin=28, sdkMax=29)
	public void testModifyQuietMode(){
		systemService(UserManager.class).requestQuietModeEnabled(true,
				UserHandle.getUserHandleForUid(appUid));
	}

	@PermissionTest(permission="SET_PROCESS_LIMIT", sdkMin=28, sdkMax=29)
	public void testSetProcessLimit(){
		BinderTransaction.getInstance().invoke(Transacts.ACTIVITY_SERVICE, Transacts.ACTIVITY_DESCRIPTOR,
				"setProcessLimit", 1000);
	}

	@PermissionTest(permission="USER_ACTIVITY", sdkMin=28, sdkMax=29)
	public void testUserActivity(){
		BinderTransaction.getInstance().invoke(Transacts.WINDOW_SERVICE, Transacts.WINDOW_DESCRIPTOR,
				"requestUserActivityNotification", (IBinder) null);
	}

	@PermissionTest(permission="MANAGE_COMPANION_DEVICES", sdkMin=28)
	public void testManageCompanionDevices(){
		// CompanionDeviceManager#isDeviceAssociatedForWifiConnection can be used for
		// both this permission as well as COMPANION_APPROVE_WIFI_CONNECTION as it first
		// checks for the MANAGE_COMPANION_DEVICES permission, then it checks if the
		// caller has the COMPANION_APPROVE_WIFI_CONNECTION permission to allow
		// connecting to a WiFi network without user consent.
		CompanionDeviceManager companionDeviceManager =
				(CompanionDeviceManager) mContext.getSystemService(
						Context.COMPANION_DEVICE_SERVICE);

		ReflectionUtil.invoke(companionDeviceManager,
				"isDeviceAssociatedForWifiConnection",
				new Class[]{String.class, MacAddress.class, UserHandle.class},
				mContext.getPackageName(), MacAddress.BROADCAST_ADDRESS,
				UserHandle.getUserHandleForUid(appUid));
	}

}









