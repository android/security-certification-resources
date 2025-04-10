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
package com.android.certification.niap.permission.dpctester.test.runner;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.NonNull;

import com.android.certification.niap.permission.dpctester.DpcApplication;
import com.android.certification.niap.permission.dpctester.common.Constants;
import com.android.certification.niap.permission.dpctester.test.exception.BypassTestException;
import com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException;
import com.android.certifications.niap.permissions.companion.services.TestBindService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

//Base class for signature permission test modules
public abstract class SignaturePermissionTestModuleBase extends PermissionTestModuleBase {
	public SignaturePermissionTestModuleBase(@NonNull Activity activity){ super(activity);}

	@NonNull
	@Override
	public PrepareInfo prepare(Consumer<PermissionTestRunner.Result> callback){
		if(!isPlatformSignatureMatch){
			inverseForPlatformTesting = true;
		}
		return super.prepare(callback);
	}

	@Override
	public PermissionTestRunner.Result resultHook(PermissionTestRunner.Result result){
		if(result.getDevelopmentProtection()){
			// The development permission flag under signature permissions allows the permission to
			// be granted via adb, including when installed with the -g option. Since this app is
			// often installed with the -g flag signature development permissions are allowed to
			// be granted to the test app. However if a signature permission does not have the
			// development flag then its grant state should match whether the app is platform signed.
			boolean B_FAILURE = result.isInverse();
			//logger.debug(result.getSource().getPermission()+" :failure:=>?"+B_FAILURE+","+isPlatformSignatureMatch+","+result.getGranted());
			if(!isPlatformSignatureMatch && result.getGranted()){
				result.setSuccess(B_FAILURE);
			}
			if (isPlatformSignatureMatch && !result.getGranted()) {
				result.setSuccess(B_FAILURE);
			}
		}
		return result;
	}

	/**
	 * Set of BIND_ permissions that also require the UID to belong to the system for a bind to
	 * complete successful; even when these permissions are granted with a platform signed app
	 * the test must be skipped since the app cannot pass the UID check.
	 */
	private static final Set<String> SYSTEM_ONLY_BIND_PERMISSIONS;
	static {
		SYSTEM_ONLY_BIND_PERMISSIONS = new HashSet<>();
		SYSTEM_ONLY_BIND_PERMISSIONS.add("android.permission.BIND_HOTWORD_DETECTION_SERVICE");
		SYSTEM_ONLY_BIND_PERMISSIONS.add("android.permission.BIND_VISUAL_QUERY_DETECTION_SERVICE");
	}

	private final Object lock = new Object();

	private class LocalServiceConnection implements android.content.ServiceConnection {
		public final AtomicBoolean binderSuccess = new AtomicBoolean();
		private final AtomicBoolean mConnected = new AtomicBoolean(false);
		public String mComponentName = "";
		public void onServiceConnected(ComponentName name, IBinder binder) {
			synchronized (lock) {
				mConnected.set(true);
				binderSuccess.set(true);
				mComponentName = name.getShortClassName();
				TestBindService service = TestBindService.Stub.asInterface(binder);
				try {
					service.testMethod();
				} catch (RemoteException e) {
					binderSuccess.set(false);
					//e.printStackTrace();
					logger.error(name+" failure."+e.getMessage(),e);
				}
				lock.notify();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			//Unimplemented
		}
	}

	public Object descriptorAsInterface(String descriptor) {
		Class clazz = null;
		Constructor c = null;
		Object o = null;
		logger.debug("Checking :"+descriptor);
		try {
			clazz = Class.forName(descriptor + "$Stub");
			//c = clazz.getDeclaredConstructor();
			//o = c.newInstance();
		} catch (ClassNotFoundException  e) {
			e.printStackTrace();
			return null;
		}
		logger.debug("Found :"+clazz);
		try {
			Method transactMethod = clazz.getDeclaredMethod("asInterface",IBinder.class);
			transactMethod.setAccessible(true);
			return transactMethod.invoke(o,getActivityToken());

		} catch (NoSuchMethodException | IllegalAccessException |
				 InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * Invokes the provided shell {@code command} as a permission test; a non-zero return code
	 * is treated as a test failure.
	 */
	protected void runShellCommandTest(String command) {
		int returnCode = runShellCommand(command);
		if (returnCode != 0) {
			throw new SecurityException(command + " failed with return code " + returnCode);
		}
	}

	/**
	 * Invokes and logs the stdout / stderr of the provided shell {@code command}, returning the
	 * exit code from the command.
	 */
	protected int runShellCommand(String command) {
		try {
			logger.debug("Attempting to run command " + command);
			java.lang.Process process = Runtime.getRuntime().exec(command);
			int returnCode = process.waitFor();
			BufferedReader stdout = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			BufferedReader stderr = new BufferedReader(
					new InputStreamReader(process.getErrorStream()));
			StringBuilder stdoutBuilder = new StringBuilder();
			String line;
			while ((line = stdout.readLine()) != null) {
				stdoutBuilder.append(line + "\n");
			}

			StringBuilder stderrBuilder = new StringBuilder();
			while ((line = stderr.readLine()) != null) {
				stderrBuilder.append(line + "\n");
			}
			logger.debug("Process return code: " + returnCode);
			logger.debug("Process stdout: " + stdoutBuilder.toString());
			logger.debug("Process stderr: " + stderrBuilder.toString());
			return returnCode;
		} catch (Throwable e) {
			throw new UnexpectedTestFailureException(e);
		}
	}
	protected void runBindRunnable(final String permission_){
		getBindRunnable(permission_).run();
	}
    protected Runnable getBindRunnable(final String permission_) {
        return () -> {

			//logger.debug("Get Bind Runnable...");

			var permission="";
			if(!permission_.contains(".")){
				permission ="android.permission."+permission_;
			} else {
				permission = permission_;
			}
			if (SYSTEM_ONLY_BIND_PERMISSIONS.contains(permission)
					&& checkPermissionGranted(permission)) {
				throw new BypassTestException(
						"Only the system can bind to this service with this permission("+permission_+") granted");
			}
			StringBuilder serviceName = new StringBuilder();
			serviceName.append("Test");
			for (String element : permission.substring(permission.lastIndexOf('.') + 1).split(
					"_")) {
				serviceName.append(element.charAt(0)).append(
						element.substring(1).toLowerCase());
			}
			serviceName.append("Service");
			//
			ExecutorService executorService =
					((DpcApplication) mActivity.getApplication()).getExecutorService();
			Intent intent = new Intent();
			intent.setComponent(new ComponentName(Constants.COMPANION_PACKAGE,
					Constants.COMPANION_PACKAGE + ".services." + serviceName));

			LocalServiceConnection serviceConnection = new LocalServiceConnection();
			//Use this type of check instead of annotation To suppress inherited methods.
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
				mContext.bindService(intent, Context.BIND_AUTO_CREATE, executorService, serviceConnection);
			} else {
				throw new BypassTestException("Can not use bind service before SDK Q");
			}
			synchronized (lock) {
				try {
					//logger.debug("Get Bind Runnable...Sync");
					int i = 0;
					while (!serviceConnection.mConnected.get()) {
						lock.wait(10 + (i * i));
						//logger.debug("Waiting for service connection...");
						if (i++ >= 10) {
							throw new InterruptedException("Connection Timed Out");
						}
					}
					//logger.info("Connected To Service in the Companion app=" + serviceConnection.mComponentName +
					//			"," + serviceConnection.binderSuccess.get());
					if (!serviceConnection.binderSuccess.get()) {
						throw new SecurityException("Test for " + serviceConnection.mComponentName + " has been failed.");
					}
				} catch (Exception ex) {
					throw new UnexpectedTestFailureException(ex);
				} finally {
					mContext.unbindService(serviceConnection);
				}
			}
		};
	}
}









