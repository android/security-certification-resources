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


import static android.Manifest.permission.ACCESS_BACKGROUND_LOCATION;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACTIVITY_RECOGNITION;
import static android.Manifest.permission.CAMERA;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.android.certification.niap.permission.dpctester.common.Constants;
import com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException;
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestModuleBase;
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestRunner;
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestRunner.Result;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTest;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestModule;


import com.android.certification.niap.permission.dpctester.test.tool.PermissionTool;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.ActivityTransitionResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@PermissionTestModule(name="Gms Permission Test Cases",label = "Run Gms Tests")
public class GmsTestModule extends PermissionTestModuleBase {
	public GmsTestModule(@NonNull Activity activity) {
		super(activity);
	}

	/**
	 * List of all Google Play Services declared signature permissions used to verify that an app
	 * not signed with the same key as GMS or the platform is not granted these permissions.
	 */
	private static final List<String> GMS_SIGNATURE_PERMISSIONS=new ArrayList<>();
	static {
		GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.auth.api.phone.permission.SEND");
		GMS_SIGNATURE_PERMISSIONS.add(
				"com.google.android.gms.trustagent.framework.model.DATA_CHANGE_NOTIFICATION");
		GMS_SIGNATURE_PERMISSIONS.add(
				"com.google.android.gms.matchstick.permission.BROADCAST_LIGHTER_WEB_INFO");
		GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.APPINDEXING");
		GMS_SIGNATURE_PERMISSIONS.add(
				"com.google.android.gms.permission.BIND_NETWORK_TASK_SERVICE");
		GMS_SIGNATURE_PERMISSIONS.add(
				"com.google.android.gms.auth.authzen.permission.DEVICE_SYNC_FINISHED");
		GMS_SIGNATURE_PERMISSIONS.add(
				"com.google.android.gms.chromesync.permission.CONTENT_PROVIDER_ACCESS");
		GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.CHECKIN_NOW");
		GMS_SIGNATURE_PERMISSIONS.add(
				"com.google.android.gms.learning.permission.LAUNCH_IN_APP_PROXY");
		GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.SEND_ANDROID_PAY_DATA");
		GMS_SIGNATURE_PERMISSIONS.add(
				"com.google.android.gms.auth.authzen.permission.GCM_DEVICE_PROXIMITY");
		GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.NEARBY_START_DISCOVERER");
		GMS_SIGNATURE_PERMISSIONS.add(
				"com.google.android.gms.auth.permission.POST_SIGN_IN_ACCOUNT");
		GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.REPORT_TAP");
		GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.C2D_MESSAGE");
		GMS_SIGNATURE_PERMISSIONS.add(
				"com.google.firebase.auth.api.gms.permission.LAUNCH_FEDERATED_SIGN_IN");
		GMS_SIGNATURE_PERMISSIONS.add(
				"com.google.android.gms.nearby.exposurenotification.EXPOSURE_CALLBACK");
		GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.PHENOTYPE_OVERRIDE_FLAGS");
		GMS_SIGNATURE_PERMISSIONS.add(
				"com.google.android.gms.auth.api.signin.permission.REVOCATION_NOTIFICATION");
		GMS_SIGNATURE_PERMISSIONS.add(
				"com.google.android.gms.magictether.permission.CONNECTED_HOST_CHANGED");
		GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.CONTACTS_SYNC_DELEGATION");
		//GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.GAMES_DEBUG_SETTINGS");//Removed as of 13
		GMS_SIGNATURE_PERMISSIONS.add(
				"com.google.android.gms.trustagent.permission.TRUSTAGENT_STATE");
		GMS_SIGNATURE_PERMISSIONS.add(
				"com.google.android.gms.permission.PHENOTYPE_UPDATE_BROADCAST");
		GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.INTERNAL_BROADCAST");
		GMS_SIGNATURE_PERMISSIONS.add(
				"com.google.android.gms.auth.proximity.permission.SMS_CONNECT_SETUP_REQUESTED");
		GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.GOOGLE_PAY");
		GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.chimera.permission.QUERY_MODULES");
		GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.CAR");
		GMS_SIGNATURE_PERMISSIONS.add(
				"com.google.android.gms.contextmanager.CONTEXT_MANAGER_RESTARTED_BROADCAST");
		GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.carsetup.DRIVING_MODE_MANAGER");
		GMS_SIGNATURE_PERMISSIONS.add(
				"com.google.android.gms.permission.SHOW_PAYMENT_CARD_DETAILS");
		GMS_SIGNATURE_PERMISSIONS.add(
				"com.google.android.gms.magictether.permission.SCANNED_DEVICE");
		GMS_SIGNATURE_PERMISSIONS.add(
				"com.google.android.gms.permission.BIND_PAYMENTS_CALLBACK_SERVICE");
		GMS_SIGNATURE_PERMISSIONS.add(
				"com.google.android.gms.permission.GRANT_WALLPAPER_PERMISSIONS");
		GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.chimera.permission.CONFIG_CHANGE");
		GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.auth.permission.FACE_UNLOCK");
		GMS_SIGNATURE_PERMISSIONS.add(
				"com.google.android.gms.auth.permission.GOOGLE_ACCOUNT_CHANGE");
		GMS_SIGNATURE_PERMISSIONS.add(
				"com.google.android.gms.trustagent.framework.model.DATA_ACCESS");
		GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.SHOW_TRANSACTION_RECEIPT");
		GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.SAFETY_NET");
		GMS_SIGNATURE_PERMISSIONS.add(
				"com.google.android.gms.common.internal.SHARED_PREFERENCES_PERMISSION");
		GMS_SIGNATURE_PERMISSIONS.add(
				"com.google.android.gms.permission.SHOW_WARM_WELCOME_TAPANDPAY_APP");
		GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.BROADCAST_TO_GOOGLEHELP");
		GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.READ_VALUABLES_IMAGES");
		GMS_SIGNATURE_PERMISSIONS.add(
				"com.google.android.gms.auth.authzen.permission.KEY_REGISTRATION_FINISHED");
		GMS_SIGNATURE_PERMISSIONS.add(
				"com.google.android.gms.magictether.permission"
						+ ".CLIENT_TETHERING_PREFERENCE_CHANGED");
		GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.DRIVE");
		GMS_SIGNATURE_PERMISSIONS.add(
				"com.google.android.gms.auth.cryptauth.permission.KEY_CHANGE");
		GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.permission.GROWTH");
		GMS_SIGNATURE_PERMISSIONS.add(
				"com.google.android.gms.magictether.permission.DISABLE_SOFT_AP");
		GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.cloudsave.BIND_EVENT_BROADCAST");
		GMS_SIGNATURE_PERMISSIONS.add("com.google.android.gms.WRITE_VERIFY_APPS_CONSENT");
		GMS_SIGNATURE_PERMISSIONS.add(
				"com.google.android.gms.googlehelp.LAUNCH_SUPPORT_SCREENSHARE");
		GMS_SIGNATURE_PERMISSIONS.add("com.android.vending.APP_ERRORS_SERVICE");
		GMS_SIGNATURE_PERMISSIONS.add(
				"com.google.android.gms.auth.cryptauth.permission.CABLEV2_SERVER_LINK");
		GMS_SIGNATURE_PERMISSIONS.add(
				"com.google.android.gms.vehicle.permission.SHARED_AUTO_SENSOR_DATA");
	}

	Boolean gmsSignatureMatch = false;
	@NonNull
	@Override
	public PrepareInfo prepare(Consumer<Result> callback) {

		var qs = super.prepare(callback);

		// An app should only have access to the GMS signature permissions if it is signed with the
		// GMS signing key or the platform signing key.


		//evaluate gms permissions separately
		List<String> gmsDeclaredPermissions = getAllGmsDeclaredSignaturePermissions();
		setAdditionalTestSize(gmsDeclaredPermissions.size());
		gmsDeclaredPermissions.forEach(gmsp->{
			boolean permissionGranted = mContext.checkSelfPermission(gmsp)
					== PackageManager.PERMISSION_GRANTED;

			if (permissionGranted != (gmsSignatureMatch || isPlatformSignatureMatch)) {
				//always ignore size
				callback.accept(
						new Result(false,null,
								new PermissionTestRunner.Data(gmsp),false,
								false,
								gmsSignatureMatch,
								true,
								isPlatformSignatureMatch,false,false,"not granted"));
				qs.setCount_errors(qs.getCount_errors()+1);
			} else {
				callback.accept(
						new Result(true,null,
								new PermissionTestRunner.Data(gmsp),false,
								true,
								gmsSignatureMatch,
								true,
								isPlatformSignatureMatch,false,false,"granted"));
				qs.setCount_passed(qs.getCount_passed()+1);
			}
		});
		return qs;
	}

	@NonNull
	@Override
	public Result resultHook(@NonNull Result result) {
		result.setGms_signature_match(gmsSignatureMatch);
		return super.resultHook(result);
	}

	/**
	 * Returns all of the signature protection level permissions declared by GMS.
	 */
	private List<String> getAllGmsDeclaredSignaturePermissions() {
		List<PermissionInfo> permissions = PermissionTool.Companion.getAllDeclaredPermissions(mContext);
		return permissions
				.stream()
				.filter(permission ->
						Constants.GMS_PACKAGE_NAME.equals(permission.packageName)
								&& permission.getProtection()
								== PermissionInfo.PROTECTION_SIGNATURE)
				.map(permission -> permission.name)
				.collect(Collectors.toList());

	}

	private <T> T systemService(Class<T> clazz){
		return Objects.requireNonNull(getService(clazz),"[npe_system_service]"+clazz.getSimpleName());
	}

	@SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @PermissionTest(permission=ACCESS_COARSE_LOCATION)
	public void testAccessCoarseLocation(){
		if(Build.VERSION.SDK_INT<=Build.VERSION_CODES.TIRAMISU) {
			final String ACCESS_COARSE_LOCATION_TEST = "ACCESS_COARSE_LOCATION_TEST";
			CountDownLatch[] latch = new CountDownLatch[1];
			latch[0] = new CountDownLatch(1);

			BroadcastReceiver receiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					LocationResult result = LocationResult.extractResult(intent);
					// The countdown on the latch should only occur if the provided
					// intent includes a valid location since additional updates can be sent
					// without a location.
					if (result != null) {
						Location location = result.getLastLocation();
						if (location != null) {
							logger.debug(
									"Received a lat,long of " + location.getLatitude()
											+ ", "
											+ location.getLongitude());
							latch[0].countDown();
						}
					}
				}
			};
			mContext.registerReceiver(receiver,
					new IntentFilter(ACCESS_COARSE_LOCATION_TEST));
			Intent intent = new Intent(ACCESS_COARSE_LOCATION_TEST);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent,
					PendingIntent.FLAG_IMMUTABLE);
			LocationRequest locationRequest = new LocationRequest.Builder(5000)
					.setDurationMillis(5000).setPriority(Priority.PRIORITY_HIGH_ACCURACY).build();


			FusedLocationProviderClient locationClient =
					LocationServices.getFusedLocationProviderClient(mContext);
			locationClient.requestLocationUpdates(locationRequest, pendingIntent);
			boolean locationReceived = false;
			try {
				locationReceived = latch[0].await(30, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				logger.error("Caught an InterruptedException: ", e);
			}
			locationClient.removeLocationUpdates(pendingIntent);
			mContext.unregisterReceiver(receiver);
			if (!locationReceived) {
				throw new SecurityException("A location update was not received");
			}
		} else {
			CountDownLatch[] latch = new CountDownLatch[1];
			latch[0] = new CountDownLatch(1);
			LocationCallback locationCallback = new LocationCallback() {
				@Override
				public void onLocationResult(LocationResult locationResult) {
					if (locationResult == null) {
						return;
					}
					for (Location location : locationResult.getLocations()) {
						logger.debug("updated location=>"+location.toString());
						latch[0].countDown();
					}
				}
			};
			LocationRequest locationRequest = new LocationRequest.Builder(5000)
					.setDurationMillis(5000).setPriority(Priority.PRIORITY_HIGH_ACCURACY).build();

			FusedLocationProviderClient locationClient =
					LocationServices.getFusedLocationProviderClient(mContext);

			locationClient.requestLocationUpdates(locationRequest,
					locationCallback,
					Looper.getMainLooper());

			boolean locationReceived = false;
			try {
				locationReceived = latch[0].await(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				logger.error("Caught an InterruptedException: ", e);
			}
			locationClient.removeLocationUpdates(locationCallback);
			if (!locationReceived) {
				throw new SecurityException("A location update was not received");
			}
		}
	}

	@SuppressLint("MissingPermission")
	@PermissionTest(permission=ACCESS_BACKGROUND_LOCATION)
	public void testAccessBackgroundLocation(){
		final String ACCESS_BACKGROUND_LOCATION_TEST =
				"ACCESS_BACKGROUND_LOCATION_TEST";
		CountDownLatch[] latch = new CountDownLatch[1];
		latch[0] = new CountDownLatch(1);
		BroadcastReceiver receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				LocationResult result = LocationResult.extractResult(intent);
				// The countdown on the latch should only occur if the provided intent
				// includes a valid location since additional updates can be sent
				// without a location.
				if (result != null) {
					Location location = result.getLastLocation();
					if (location != null) {
						logger.debug(
								"Received a lat,long of " + location.getLatitude()
										+ ", "
										+ location.getLongitude());
						latch[0].countDown();
					}
				}
			}
		};
		if(Build.VERSION.SDK_INT<Build.VERSION_CODES.TIRAMISU) {
			mContext.registerReceiver(receiver,
					new IntentFilter(ACCESS_BACKGROUND_LOCATION_TEST));
		} else {
			mContext.registerReceiver(receiver,
					new IntentFilter(ACCESS_BACKGROUND_LOCATION_TEST), Context.RECEIVER_NOT_EXPORTED);
		}

		Intent intent = new Intent(ACCESS_BACKGROUND_LOCATION_TEST);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent,
				PendingIntent.FLAG_IMMUTABLE);

		GeofencingClient geofencingClient = LocationServices.getGeofencingClient(
				mContext);
		Geofence geofence = new Geofence.Builder().setRequestId(
						"ACCESS_FINE_LOCATION_GEOFENCE_TEST").setCircularRegion(37.422136,
						-122.084068,
						1000)
				.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
				.setExpirationDuration(5000)
				.build();
		GeofencingRequest geofencingRequest =
				new GeofencingRequest.Builder().setInitialTrigger(
						GeofencingRequest.INITIAL_TRIGGER_ENTER).addGeofence(
						geofence).build();
		Task<Void> geofenceTask = geofencingClient.addGeofences(geofencingRequest,
				pendingIntent);
		try {
			Tasks.await(geofenceTask);
			geofenceTask.getResult();
		} catch (ExecutionException e) {
			// While the Geofencing Binder invocation will fail due to a
			// SecurityException the ExecutionException does not contain the
			// SecurityException as a cause, but it does contain the text from the
			// SecurityException in the message.
			if (e.getMessage().indexOf("SecurityException") != -1) {
				throw new SecurityException(e);
			}
			throw new UnexpectedTestFailureException(e);
		} catch (Exception e) {
			throw new UnexpectedTestFailureException(e);
		}
		try {
			latch[0].await(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			throw new UnexpectedTestFailureException(e);
		}

	}

	@SuppressLint("MissingPermission")
	@PermissionTest(permission=CAMERA)
	public void testCamera(){
		CountDownLatch[] latch = new CountDownLatch[1];
		latch[0] = new CountDownLatch(1);

		Detector<Integer> detector = new Detector() {
			@Override
			public SparseArray<Integer> detect(Frame frame) {
				logger.debug("detect: frame = " + frame);
				latch[0].countDown();
				return null;
			}
		};
		Detector.Processor<Integer> processor = new Detector.Processor() {
			@Override
			public void receiveDetections(Detector.Detections detections) {
				logger.debug("Camera Permission : detections = " + detections);
			}

			@Override
			public void release() {
				logger.debug("Camera Permission : release deteced");
			}
		};
		detector.setProcessor(processor);
		CameraSource cameraSource = new CameraSource.Builder(mContext, detector).setFacing(
				CameraSource.CAMERA_FACING_BACK).setAutoFocusEnabled(true).setRequestedFps(
				1).setRequestedPreviewSize(1024, 768).build();
		try {
			cameraSource.start();
		} catch (IOException e) {
			throw new UnexpectedTestFailureException(e);
		} catch (RuntimeException e) {
			if (e.getMessage().contains("Fail to connect to camera service")) {
				// A RuntimeException occurs when the CAMERA permission check fails; wrap the
				// exception in a SecurityException to fail the API call for this test.
				throw new SecurityException(e);
			}
			throw e;
		} finally {
			cameraSource.release();
		}
	}

	@SuppressLint("MissingPermission")
	@PermissionTest(permission=ACTIVITY_RECOGNITION, sdkMin=29)
	public void testActivityRecognition(){
		final String ACTIVITY_RECOGNITION_TEST = "ACTIVITY_RECOGNITION_TEST";
		CountDownLatch[] latch = new CountDownLatch[1];
		latch[0] = new CountDownLatch(1);

		BroadcastReceiver receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				logger.debug("onReceive invoked with intent: " + intent);
				ActivityTransitionResult result =
						ActivityTransitionResult.extractResult(intent);
				ActivityRecognitionResult result2 =
						ActivityRecognitionResult.extractResult(
								intent);

				// The countdown on the latch should only occur if the provided
				// intent includes a valid activity update.
				if (result != null) {
					logger.debug("Received a broadcast with the activity result: "
							+ result.toString());
					latch[0].countDown();
				} else {
					logger.debug(
							"Received a broadcast with a null activity result");
				}
			}
		};

		if(Build.VERSION.SDK_INT< Build.VERSION_CODES.TIRAMISU) {
			mContext.registerReceiver(receiver,
					new IntentFilter(ACTIVITY_RECOGNITION_TEST));
		} else {
			mContext.registerReceiver(receiver,
					new IntentFilter(ACTIVITY_RECOGNITION_TEST), Context.RECEIVER_NOT_EXPORTED);
		}

		Intent intent = new Intent(ACTIVITY_RECOGNITION_TEST);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent,
				PendingIntent.FLAG_IMMUTABLE);

		// The ActivityRecognitionClient will throw a SecurityException when waiting for
		// the Task from the #requestActivityUpdates to complete.
		ActivityRecognitionClient activityClient = ActivityRecognition.getClient(
				mContext);
		Task<Void> activityRecognitionTask =
				activityClient.requestActivityUpdates(0, pendingIntent);
		try {
			Tasks.await(activityRecognitionTask, 10, TimeUnit.SECONDS);
		} catch (ExecutionException | TimeoutException | InterruptedException e) {
			// The ExecutionException typically wraps an ApiException with the message
			// from a SecurityException if the permission is not granted. If the
			// SecurityException text is in the message then treat the API call as
			// having failed due to the permission not being granted.
			if (e.getMessage().contains("SecurityException")) {
				throw new SecurityException(e);
			} else {
				throw new UnexpectedTestFailureException(e);
			}
		} finally {
			activityClient.removeActivityTransitionUpdates(pendingIntent);
			activityClient.removeActivityUpdates(pendingIntent);
			mContext.unregisterReceiver(receiver);
		}
	}

	@SuppressLint("MissingPermission")
	@PermissionTest(permission=ACCESS_FINE_LOCATION, sdkMin=29)
	public void testAccessFineLocation(){
		Task<Location> locationTask = LocationServices.getFusedLocationProviderClient(
				mContext).getLastLocation();
		try {
			Tasks.await(locationTask, 5, TimeUnit.SECONDS);
			Location location = locationTask.getResult();
			if (location == null) {
				throw new SecurityException("Unable to obtain the last location");
			} else {
				logger.debug(
						"Received a lat,long of " + location.getLatitude() + ", "
								+ location.getLongitude());
			}
		} catch (ExecutionException e) {
			if (e.getCause() instanceof SecurityException) {
				throw (SecurityException) e.getCause();
			}
		} catch (TimeoutException | InterruptedException e) {
			throw new UnexpectedTestFailureException(e);
		}
	}
}









