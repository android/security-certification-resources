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

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.AttributionSource;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.media.MediaRecorder;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.provider.CalendarContract;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.VoicemailContract.Voicemails;

import android.provider.Telephony;
import android.ranging.RangingCapabilities;
import android.ranging.RangingData;
import android.ranging.RangingDevice;
import android.ranging.RangingManager;
import android.ranging.RangingSession;
import android.service.notification.StatusBarNotification;
import android.telecom.TelecomManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.content.ContentValues;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.android.certification.niap.permission.dpctester.MainActivity;
import com.android.certification.niap.permission.dpctester.R;
import com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException;
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestModuleBase;
import com.android.certification.niap.permission.dpctester.test.tool.BinderTransaction;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTest;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestModule;
import com.android.certification.niap.permission.dpctester.test.exception.BypassTestException;
import com.android.certification.niap.permission.dpctester.test.tool.TesterUtils;
import com.google.common.util.concurrent.MoreExecutors;

import android.provider.CalendarContract.Events;

import static android.Manifest.permission.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

@PermissionTestModule(name="Runtime Test Cases",prflabel = "Runtime Permissions")
public class RuntimeTestModule extends PermissionTestModuleBase {
	public RuntimeTestModule(@NonNull Activity activity){ super(activity);}

	private <T> T systemService(Class<T> clazz){
		return Objects.requireNonNull(getService(clazz),"[npe_system_service]"+clazz.getSimpleName());
	}

	@PermissionTest(permission=BODY_SENSORS)
	public void testBodySensors(){
		if (!mPackageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_HEART_RATE)) {
			throw new BypassTestException(
					"A heart rate monitor is not available to run this test");
		}
		Sensor sensor = systemService(SensorManager.class).getDefaultSensor(Sensor.TYPE_HEART_RATE);
		if (sensor == null) {
			throw new SecurityException(
					"The heart rate sensor feature is available, but a null sensor was "
							+ "returned");
		}
	}

	@SuppressLint("MissingPermission")
	@PermissionTest(permission=CALL_PHONE)
	public void testCallPhone(){
		systemService(TelecomManager.class).endCall();
	}

	@PermissionTest(permission=READ_CALENDAR)
	public void testReadCalendar(){
		mContentResolver.query(
				CalendarContract.CalendarAlerts.CONTENT_URI, null, null, null, null);
	}

	@PermissionTest(permission=READ_CALL_LOG)
	public void testReadCallLog(){
		CallLog.Calls.getLastOutgoingCall(mContext);
	}

	@PermissionTest(permission=READ_CONTACTS)
	public void testReadContacts(){
		mContentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
	}

	@PermissionTest(permission=READ_EXTERNAL_STORAGE, sdkMin=28, sdkMax=32)
	public void testReadExternalStorage(){
		systemService(WallpaperManager.class).getWallpaperFile(WallpaperManager.FLAG_SYSTEM);
	}

	@SuppressLint("MissingPermission")
	@PermissionTest(permission=READ_PHONE_STATE)
	public void testReadPhoneState(){
		systemService(TelephonyManager.class).getCarrierConfig();
	}

	@PermissionTest(permission=READ_SMS)
	public void testReadSms(){
		mContentResolver.query(Telephony.Sms.Inbox.CONTENT_URI, null, null, null, null);
	}

	@SuppressLint("HardwareIds")
	@PermissionTest(permission=SEND_SMS)
	public void testSendSms(){
		String deviceNumber = null;

		boolean permissionGranted =
				mContext.checkSelfPermission(READ_PHONE_NUMBERS) == PackageManager.PERMISSION_GRANTED;

		if (!permissionGranted) {
			throw new BypassTestException(
					"This test requires the READ_PHONE_NUMBERS permission to obtain the "
							+ "device number");
		}
		try {
			deviceNumber = systemService(TelephonyManager.class).getLine1Number();
		} catch (SecurityException e) {
			// A SecurityException caught here indicates this app does not have the proper
			// permission to obtain the device number; since the test requires the device
			// number it should be skipped below.
		}
		if (deviceNumber == null) {
			throw new BypassTestException(
					"The device number could not be obtained to verify SEND_SMS (Check SIM)");
		}
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(deviceNumber, null, "Test message to verify SEND_SMS", null,
				null);
	}

	@PermissionTest(permission=WRITE_CALENDAR)
	public void testWriteCalendar(){
		ContentValues values = new ContentValues();
		values.put(Events.DTSTART, System.currentTimeMillis());
		values.put(Events.DTEND, System.currentTimeMillis());
		values.put(Events.TITLE, "Test Calendar Entry");
		values.put(Events.EVENT_TIMEZONE, "America/Los_Angeles");
		values.put(Events.CALENDAR_ID, 1);
		mContentResolver.insert(Events.CONTENT_URI, values);
	}

	@PermissionTest(permission=WRITE_CALL_LOG)
	public void testWriteCallLog(){
		ContentValues values = new ContentValues();
		values.put(Calls.NUMBER, "520-555-1234");
		values.put(Calls.DATE, System.currentTimeMillis());
		values.put(Calls.DURATION, 0);
		values.put(Calls.TYPE, Calls.OUTGOING_TYPE);
		mContentResolver.insert(Calls.CONTENT_URI, values);
	}

	@PermissionTest(permission=WRITE_CONTACTS)
	public void testWriteContacts(){
		Account[] accounts = systemService(AccountManager.class).getAccounts();
		if (accounts.length == 0) {
			throw new BypassTestException(
					"This permission requires an account with contacts");
		} else {
			Account account = accounts[0];
			ContentValues values = new ContentValues();
			values.put(RawContacts.ACCOUNT_TYPE, account.type);
			values.put(RawContacts.ACCOUNT_NAME, account.name);
			Uri rawContactUri = mContentResolver.insert(RawContacts.CONTENT_URI, values);
			long rawContactId = ContentUris.parseId(rawContactUri);
			values.clear();
			values.put(Data.RAW_CONTACT_ID, rawContactId);
			values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
			values.put(StructuredName.DISPLAY_NAME, "Test Contact");
			mContentResolver.insert(Data.CONTENT_URI, values);
		}
	}

	@PermissionTest(permission=WRITE_EXTERNAL_STORAGE, sdkMin=28, sdkMax=29)
	public void testWriteExternalStorage(){
		// Due to scoped storage apps targeting API level 30+ no longer get any
		// additional access with the WRITE_EXTERNAL_STORAGE permission; for details see
		// https://developer.android.com/preview/privacy/storage#permissions-target-11.
		try {
			File documentsDirectory = Environment.getExternalStoragePublicDirectory(
					Environment.DIRECTORY_DOCUMENTS);
			if (!documentsDirectory.exists() && !documentsDirectory.mkdir()) {
				throw new SecurityException(
						"Could not create the directory "
								+ documentsDirectory.getAbsolutePath());
			}
			File file = new File(documentsDirectory, "test_file.out");
			if (!file.createNewFile()) {
				throw new SecurityException(
						"Could not create the temporary file "
								+ file.getAbsolutePath());
			}
			file.delete();
		} catch (IOException e) {
			// If the permission is not granted then this could fail with a
			// 'Permission denied' IOException instead of a SecurityException.
			String message = e.getMessage();
			if (message != null && message.contains("Permission denied")) {
				throw new SecurityException(e);
			} else {
				throw new UnexpectedTestFailureException(e);
			}
		}
	}

	@PermissionTest(permission=ADD_VOICEMAIL)
	public void testAddVoicemail(){
		ContentValues values = new ContentValues();
		values.put(Voicemails.NUMBER, "520-555-1234");
		values.put(Voicemails.DATE, System.currentTimeMillis());
		values.put(Voicemails.DURATION, 10);
		values.put(Voicemails.NEW, 0);
		values.put(Voicemails.TRANSCRIPTION, "Testing ADD_VOICEMAIL");
		values.put(Voicemails.IS_READ, 0);
		values.put(Voicemails.HAS_CONTENT, 0);
		values.put(Voicemails.SOURCE_DATA, "1234");
		values.put(Voicemails.BACKED_UP, 0);
		values.put(Voicemails.RESTORED, 0);
		values.put(Voicemails.ARCHIVED, 0);
		values.put(Voicemails.IS_OMTP_VOICEMAIL, 0);
		values.put(Voicemails.SOURCE_PACKAGE, mContext.getPackageName());
		Uri voicemailUri = Uri.parse(
				Voicemails.CONTENT_URI + "?source_package=" + mContext.getPackageName());
		mContentResolver.insert(voicemailUri, values);
	}

	@SuppressLint("MissingPermission")
	@PermissionTest(permission=ACCESS_COARSE_LOCATION)
	public void testAccessCoarseLocation(){
		systemService(LocationManager.class).getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	}
	@SuppressLint("MissingPermission")
	@PermissionTest(permission=ACCESS_FINE_LOCATION)
	public void testAccessFineLocation(){
		systemService(LocationManager.class).getLastKnownLocation(LocationManager.GPS_PROVIDER);
	}

	@SuppressLint("MissingPermission")
	@PermissionTest(permission=CAMERA)
	public void testCamera(){
		try {
			CameraManager mCameraManager = systemService(CameraManager.class);
			String[] cameras = mCameraManager.getCameraIdList();
			if (cameras.length == 0) {
				throw new BypassTestException(
						"No cameras were found on this device to perform this test");
			}
			CameraDevice.StateCallback cameraCallback = new CameraDevice.StateCallback() {
				@Override
				public void onDisconnected(CameraDevice camera) {
					logger.debug("onDisconnected: camera = " + camera);
				}

				@Override
				public void onError(CameraDevice camera, int error) {
					logger.debug("onError: camera = " + camera + ", error = " + error);
				}

				@Override
				public void onOpened(CameraDevice camera) {
					logger.debug("onOpened: camera = " + camera);
					camera.close();
				}
			};
			systemService(CameraManager.class).openCamera(cameras[0], cameraCallback,
					new Handler(Looper.getMainLooper()));
		} catch (CameraAccessException e) {
			throw new UnexpectedTestFailureException(e);
		}
	}

	@PermissionTest(permission=READ_PHONE_NUMBERS)
	public void testReadPhoneNumbers(){
		if(!mNopermMode) {
			if (ActivityCompat.checkSelfPermission(mContext, READ_SMS) != PackageManager.PERMISSION_GRANTED &&
					ActivityCompat.checkSelfPermission(mContext, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
				throw new BypassTestException(
						"READ_SMS & READ_PHONE_STATE permission is required for this test case as prerequisite");
			}
		}
		systemService(TelephonyManager.class).getLine1Number();
	}

	@PermissionTest(permission=RECORD_AUDIO)
	public void testRecordAudio(){
		try {
			MediaRecorder recorder = new MediaRecorder();
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			String fileName = mContext.getFilesDir() + "/test_record_audio.out";
			recorder.setOutputFile(new File(fileName));
			recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			recorder.prepare();
		} catch (RuntimeException e) {
			// The MediaRecorder framework throws a RuntimeException when the RECORD_AUDIO
			// permission is not granted to the calling app.
			throw new SecurityException(e);
		} catch (IOException ioe) {
			throw new UnexpectedTestFailureException(ioe);
		}
	}

	@SuppressLint("MissingPermission")
	@PermissionTest(permission=ANSWER_PHONE_CALLS)
	public void testAnswerPhoneCalls(){
		systemService(TelecomManager.class).endCall();
	}

	@PermissionTest(permission=ACTIVITY_RECOGNITION, sdkMin=29)
	public void testActivityRecognition(){
		Sensor sensor = systemService(SensorManager.class).getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
		if (sensor == null) {
			throw new BypassTestException(
					"The step counter sensor is not available to execute this test");
		}
		SensorEventListener listener = new SensorEventListener() {
			@Override
			public void onSensorChanged(SensorEvent sensorEvent) {
				logger.debug("onSensorChanged: event = " + sensorEvent);
			}

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				logger.debug(
						"onAccuracyChanged: sensor = " + sensor + ", accuracy = "
								+ accuracy);
			}
		};
		boolean listenerRegistered = systemService(SensorManager.class).registerListener(listener,
				sensor,
				SensorManager.SENSOR_DELAY_NORMAL);
		if (!listenerRegistered) {
			throw new SecurityException(
					"Failed to register a listener for the STEP_COUNTER sensor");
		}
		systemService(SensorManager.class).unregisterListener(listener);
	}

	@PermissionTest(permission=ACCESS_MEDIA_LOCATION, sdkMin=29,
			requiredPermissions = {"android.permission.READ_MEDIA_IMAGES",READ_EXTERNAL_STORAGE})
	public void testAccessMediaLocation(){
		String selection = MediaStore.Images.Media.MIME_TYPE + "='image/jpeg'" + " OR "
				+ MediaStore.Images.Media.MIME_TYPE + "='image/jpg'";
		Cursor cursor = mContentResolver.query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				new String[]{MediaStore.Images.Media.DESCRIPTION,
						MediaStore.Images.Media._ID}, selection, null, null);
		if (cursor == null) {
			//logger.system("cursor media location");
			throw new UnexpectedTestFailureException(
					"Unable to obtain an image to test ACCESS_MEDIA_LOCATION");
		}
		if (cursor.getCount() == 0) {
			if (mContext.checkSelfPermission(READ_EXTERNAL_STORAGE)  != PackageManager.PERMISSION_GRANTED) {
				throw new UnexpectedTestFailureException(
						"Unable to obtain an image with location data to verify "
								+ "ACCESS_MEDIA_LOCATION");
			} else {
				throw new BypassTestException(
						"READ_EXTERNAL_STORAGE not granted; unable to obtain "
								+ "images");
			}
		}
		while (cursor.moveToNext()) {
			int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
			String elementId = cursor.getString(columnIndex);
			Uri elementUri = Uri.parse(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI + "/" + elementId);
			try (InputStream inputStream = mContentResolver.openInputStream(
					elementUri)) {
				ExifInterface exif = new ExifInterface(inputStream);
				float[] latLong = new float[2];//{0.0f,0.0f};
				exif.getLatLong(latLong);
				//logger.system(">"+latLong[0]+":"+latLong[1]);
				// Not all images will have location data, ensure all images are
				// tested before reporting an error.
				if (!(latLong[0] == 0.0f && latLong[1] == 0.0f)) {
					//logger.system("passed!!");
					return;
				}
			} catch (IOException e) {
				logger.error("Caught an IOException reading the image: ", e);
			}
		}
		throw new SecurityException(
				"Unable to obtain the location data from any image");
	}

	@SuppressLint("MissingPermission")
	@PermissionTest(permission=BLUETOOTH_ADVERTISE, sdkMin=31)
	public void testBluetoothAdvertise(){
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (!TesterUtils.enableBluetoothAdapter(mContext,bluetoothAdapter)) {
			throw new BypassTestException(
					"The bluetooth adapter must be enabled for this test");
		}
		AdvertiseSettings settings = new AdvertiseSettings.Builder()
				.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
				.setConnectable(false)
				.build();
		AdvertiseData data = new AdvertiseData.Builder()
				.setIncludeDeviceName(true)
				.build();
		AdvertiseCallback callback = new AdvertiseCallback() {
			@Override
			public void onStartSuccess(AdvertiseSettings settingsInEffect) {
				super.onStartSuccess(settingsInEffect);
				logger.debug(
						"onStartSuccess: settingsInEffect = " + settingsInEffect);
			}

			@Override
			public void onStartFailure(int errorCode) {
				super.onStartFailure(errorCode);
				logger.debug("onStartFailure: errorCode = " + errorCode);
			}
		};
		BluetoothLeAdvertiser advertiser = bluetoothAdapter.getBluetoothLeAdvertiser();
		advertiser.startAdvertising(settings, data, callback);
		advertiser.stopAdvertising(callback);
	}

	@PermissionTest(permission=BLUETOOTH_CONNECT, sdkMin=31)
	public void testBluetoothConnect(){
		if(!mNopermMode) {
			if (ActivityCompat.checkSelfPermission(mContext, BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
				throw new BypassTestException(
						"BLUETOOTH_SCAN permission is required for this test case");
			}
		}
		BluetoothAdapter.getDefaultAdapter().getName();
	}

	@SuppressLint("MissingPermission")
	@PermissionTest(permission=BLUETOOTH_SCAN, sdkMin=31)
	public void testBluetoothScan(){
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (!TesterUtils.enableBluetoothAdapter(mContext,bluetoothAdapter)) {
			throw new BypassTestException(
					"The bluetooth adapter must be enabled for this test");
		}
		BluetoothAdapter.getDefaultAdapter().getScanMode();
	}

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
		if(!mNopermMode) {
			if (mContext.checkSelfPermission("android.permission.UWB_PRIVILEGED") != PackageManager.PERMISSION_GRANTED) {
				throw new BypassTestException(
						"The UWB_PRIVILEGED permission must be granted for this test");
			}
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

	@PermissionTest(permission=UWB_RANGING, sdkMax=30)
	public void testUwbRangingLegacy(){
		BinderTransaction.getInstance().invoke(Transacts.UWB_SERVICE,
				Transacts.UWB_DESCRIPTOR,
				"getSpecificationInfo"
		);
	}
	@PermissionTest(permission=READ_MEDIA_AUDIO, sdkMin=33)
	public void testReadMediaAudio(){
		ContentResolver contentResolver = mContext.getContentResolver();
		@SuppressLint("Recycle") Cursor cursor = contentResolver.query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				null, null, null, null);

		if (cursor == null) {
			throw new UnexpectedTestFailureException("Unable to obtain an sound to test READ_MEDIA_AUDIO");
		} else if (!cursor.moveToFirst()) {
			throw new SecurityException("Failed to load media files:READ_MEDIA_AUDIO." +
					"Pleaes ensure to execute the companion app before testing.");
		} else {
			int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
			//those file can be read after android udc without permissions...
			String[] acceptables = new String[]{"default_ringtone",
					"default_alarm_alert","default_notification_sound"};
			List<String> result = new ArrayList<String>();

			do{
				String fname = cursor.getString(index);
				boolean record=true;
				for(String a: acceptables){
					if(fname.startsWith(a)){
						record=false;
					}
				}
				if(record){
					result.add(fname);
				}
			}while(cursor.moveToNext());
			if (result.size() == 0){
				throw new SecurityException
						("Failed to read any audio medias (they should be setup by the companion app)");
			}
		}

	}

	@PermissionTest(permission=READ_MEDIA_IMAGES, sdkMin=33)
	public void testReadMediaImages(){

		ContentResolver contentResolver = mContext.getContentResolver();
		String[] PROJECTION_BUCKET = new String[] { "bucket_id", "bucket_display_name", "datetaken", "_data" };

		String[] thumbColumns = {MediaStore.Images.Thumbnails.DATA, MediaStore.Images.Thumbnails.IMAGE_ID};
		String[] columns = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_TAKEN};
		String[] whereArgs = {"image/jpeg", "image/jpg"};

		String orderBy = MediaStore.Images.Media.DATE_TAKEN+ " DESC";
		String  where = MediaStore.Images.Media.MIME_TYPE + "=? or "
				+ MediaStore.Images.Media.MIME_TYPE + "=?";


		@SuppressLint("Recycle") Cursor cursor = contentResolver.query
				(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,columns, where, whereArgs, orderBy);

		if (cursor == null) {
			throw new UnexpectedTestFailureException(
					"Unable to obtain an image to test READ_MEDIA_IMAGES");
		}
		if (!cursor.moveToFirst()) {
			throw new SecurityException("Failed to load media files:READ_MEDIA_IMAGES." +
					"If the permission is allowed, " +
					"Please ensure to execute the companion app before testing.");
		}
		int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
		Long id = cursor.getLong(fieldIndex);
		//String path = cursor.get(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));

		Uri bmpUri = ContentUris.withAppendedId(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
		try {
			Bitmap bmp = MediaStore.Images.Media
					.getBitmap(mContentResolver,bmpUri);// bmpUri);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		cursor.close();
	}

	@PermissionTest(permission=READ_MEDIA_VIDEO, sdkMin=33)
	public void testReadMediaVideo(){
		ContentResolver contentResolver = mContext.getContentResolver();
		@SuppressLint("Recycle") Cursor cursor = contentResolver.query(
				MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
				null, null, null, null);
		if (cursor == null) {
			throw new UnexpectedTestFailureException(
					"Unable to obtain an image to test READ_MEDIA_VIDEO");
		} else if (!cursor.moveToFirst()) {
			throw new SecurityException("Failed to load media files:READ_MEDIA_VIDEO." +
					"Pleaes ensure to execute the companion app before testing.");
		}
	}

	@PermissionTest(permission=POST_NOTIFICATIONS, sdkMin=33)
	public void testPostNotifications(){


		Resources resources = mContext.getResources();
		CharSequence channelName = resources.getString(R.string.tester_channel_name);
		NotificationChannel channel = new NotificationChannel(getTAG(), channelName,
				NotificationManager.IMPORTANCE_DEFAULT);
		NotificationManager notificationManager = mContext.getSystemService(
				NotificationManager.class);
		notificationManager.createNotificationChannel(channel);

		Intent notificationIntent = new Intent(mContext, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
				notificationIntent, PendingIntent.FLAG_IMMUTABLE);

		Notification notification =
				new NotificationCompat.Builder(mContext, getTAG())
						.setContentTitle(resources.getText(
								R.string.notificaton_title))
						.setContentText(resources.getText(
								R.string.intent_notification_message))
						.setSmallIcon(R.drawable.ic_launcher_foreground)
						.setPriority(NotificationCompat.PRIORITY_DEFAULT)
						.setContentIntent(pendingIntent)
						.build();

		notificationManager.notify(1573, notification);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		StatusBarNotification[] notifications = notificationManager.getActiveNotifications();
		boolean found = false;
		for (StatusBarNotification nn : notifications) {
			if (nn.getId() == 1573) {
				logger.info("notification found");
				found = true;
			}
		}
		if(!found){
			throw new SecurityException("Expected notification is not shown");
		}

	}



	//testNearbyWifiDevices implementations
	private final Object mLOHSLock = new Object();//Test for Local only hotspot
	public static class TestLocalOnlyHotspotCallback extends WifiManager.LocalOnlyHotspotCallback {
		final Object hotspotLock;
		WifiManager.LocalOnlyHotspotReservation reservation = null;
		boolean onStartedCalled = false;
		boolean onStoppedCalled = false;
		boolean onFailedCalled = false;
		int failureReason = -1;

		TestLocalOnlyHotspotCallback(Object lock) {
			hotspotLock = lock;
		}

		@Override
		public void onStarted(WifiManager.LocalOnlyHotspotReservation r) {
			synchronized (hotspotLock) {
				reservation = r;
				onStartedCalled = true;
				hotspotLock.notify();
			}
		}

		@Override
		public void onStopped() {
			synchronized (hotspotLock) {
				onStoppedCalled = true;
				hotspotLock.notify();
			}
		}

		@Override
		public void onFailed(int reason) {
			synchronized (hotspotLock) {
				onFailedCalled = true;
				failureReason = reason;
				hotspotLock.notify();
			}
		}
	}

	@SuppressLint("MissingPermission")
	@PermissionTest(permission=NEARBY_WIFI_DEVICES, sdkMin=33)
	public void testNearbyWifiDevices(){
		TestLocalOnlyHotspotCallback callback = new TestLocalOnlyHotspotCallback(mLOHSLock);
		synchronized (mLOHSLock) {
			try {
				//WifiManager.startLocalOnlyHotSpot requires NEARBY_WIFI_DEVICES from android T,
				//and also it's a public api
				systemService(WifiManager.class).startLocalOnlyHotspot(callback, null);
				mLOHSLock.wait(60);
			} catch (InterruptedException | IllegalStateException e) {
				logger.info("Intended exception : " + e.getMessage() + ". Ignored.");
			}
		}
	}

	//**** method template for target runtime SDK36
	@RequiresApi(35)
	@PermissionTest(permission="RANGING",sdkMin=36)
	public void testRanging(){

		//adb shell aflags list
		//@RequiresFlagsEnabled("com.android.ranging.flags.ranging_stack_enabled")
		RangingSession session = systemService(RangingManager.class).createRangingSession(mContext.getMainExecutor(),
				new RangingSession.Callback() {
			@Override
			public void onClosed(int i) {}
			@Override
			public void onOpenFailed(int i) {}
			@Override
			public void onOpened() {}
			@Override
			public void onResults(@NonNull RangingDevice rangingDevice, @NonNull RangingData rangingData) {}
			@Override
			public void onStarted(@NonNull RangingDevice rangingDevice, int i) {}
			@Override
			public void onStopped(@NonNull RangingDevice rangingDevice, int i) {}
		});
		session.stop();
		//reconfigureRangingInterval(100); <= this method crashes device
	}
	/* Could not find implementations...
	@PermissionTest(permission="EYE_TRACKING_COARSE",sdkMin=36)
	public void testEyeTrackingCoarse(){
		logger.debug("The test for android.permission.EYE_TRACKING_COARSE is not implemented yet");
	}
	@PermissionTest(permission="FACE_TRACKING",sdkMin=36)
	public void testFaceTracking(){
		logger.debug("The test for android.permission.FACE_TRACKING is not implemented yet");
	}
	@PermissionTest(permission="HAND_TRACKING",sdkMin=36)
	public void testHandTracking(){
		logger.debug("The test for android.permission.HAND_TRACKING is not implemented yet");
	}
	@PermissionTest(permission="SCENE_UNDERSTANDING_COARSE",sdkMin=36)
	public void testSceneUnderstandingCoarse(){
		logger.debug("The test for android.permission.SCENE_UNDERSTANDING_COARSE is not implemented yet");
	}
	@PermissionTest(permission="EYE_TRACKING_FINE",sdkMin=36)
	public void testEyeTrackingFine(){
		logger.debug("The test for android.permission.EYE_TRACKING_FINE is not implemented yet");
	}
	@PermissionTest(permission="HEAD_TRACKING",sdkMin=36)
	public void testHeadTracking(){
		logger.debug("The test for android.permission.HEAD_TRACKING is not implemented yet");
	}
	@PermissionTest(permission="SCENE_UNDERSTANDING_FINE",sdkMin=36)
	public void testSceneUnderstandingFine(){
		logger.debug("The test for android.permission.SCENE_UNDERSTANDING_FINE is not implemented yet");
	}
	*/
}
