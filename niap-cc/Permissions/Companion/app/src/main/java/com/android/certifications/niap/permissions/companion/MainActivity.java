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

package com.android.certifications.niap.permissions.companion;

import static android.app.PendingIntent.FLAG_IMMUTABLE;
import static android.app.PendingIntent.FLAG_NO_CREATE;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.LocusId;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Activity to drive the configuration of the device under test to ensure all resources are in place
 * as required by the permission tests.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "PermissionTesterCompanion";

    private static final int REQUEST_LOCATION_SETTINGS = 100;
    private static final int REQUEST_LOCATION_PERMISSION = 101;

    private static final String[] LOCATION_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    private ListView mStatusListView;
    static List<String> mStatusData = new ArrayList<String>();
    static ArrayAdapter<String> mStatusAdapter;

    protected void setAdapters(){
        mStatusAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                mStatusData);
        mStatusListView.setAdapter(mStatusAdapter);
    }
    private Button mSetupButton;
    private boolean mGmsAvailable;

    /**
     * Used to ensure the GMS location settings are configured as required for the location tests.
     */
    private LocationRequest mLocationRequest;
    private void logdebug(String text)
    {
        Log.d(TAG, text);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mStatusAdapter != null){
                    mStatusAdapter.add("🟩"+text);
                }
            }
        });
    }
    private void logerror(String text)
    {
        Log.e(TAG, text);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mStatusAdapter != null){
                    mStatusAdapter.add("🔴"+text);

                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStatusListView = findViewById(R.id.statusTextView);
        setAdapters();

        logdebug("Running permission tester companion setup on build " + Build.FINGERPRINT);

        mGmsAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
                == ConnectionResult.SUCCESS;
        if (mGmsAvailable) {
            mLocationRequest = LocationRequest.create().setPriority(
                    LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY).setInterval(1000);
        } else {
            logdebug( "GMS is not available on this device");
        }

        mSetupButton = findViewById(R.id.setupButton);
        mSetupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SetupTestsAsyncTask().execute();
            }
        });

        long currTimeMs = System.currentTimeMillis();
        final DropBoxManager db = (DropBoxManager) getApplicationContext().getSystemService(Context.DROPBOX_SERVICE);
        logdebug("Put a data into the DropBox Manager");
        db.addText("test-companion-tag","Companion:PEEK_DROPBOX_DATA test at :"+currTimeMs);

    }

    /**
     * {@link AsyncTask} used to drive the setup for the permission tests.
     *
     * Fix: We shouldn't use AsyncTask anymore ... refine it later
     *
     */
    private class SetupTestsAsyncTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            //mStatusTextView.setText(R.string.setup_in_progress);
            logdebug("Setup in progress");
            mSetupButton.setEnabled(false);
        }

        @Override
        protected Boolean doInBackground(Void... noParams) {
            boolean result;
            setupLocusTest();
            result = setupMediaLocationTest();
            if (mGmsAvailable) {
                // if the location settings are not correct then prompt the user for correction
                // before attempting to set up the location tests.
                if (!verifyLocationSettings()) {
                    return false;
                }
                result = result && setupLocationTest();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean setupComplete) {
            if (!setupComplete) {
                logerror("Setup failed");
            } else {
                logdebug("☑️Setup complete with no error");
            }
            mSetupButton.setEnabled(true);
        }
    }

    /**
     * Verifies GMS is properly configured to fulfill a balanced power location query.
     *
     * <p>If the device is not properly configured and GMS indicates it is possible to request
     * consent from the user this method will prompt the user to configure GMS.
     */
    private boolean verifyLocationSettings() {
        LocationSettingsRequest locationSettingsRequest =
                new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest).build();
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(
                this).checkLocationSettings(locationSettingsRequest);
        try {
            Tasks.await(result, 10, TimeUnit.SECONDS);
            logdebug( "Location settings indicate location should be available");
            return true;
        } catch (ExecutionException e) {
            // An ExecutionException indicates that GMS is not properly configured to support this
            // location request. If the cause of the exception is a ResolveableApiException then
            // the resolution from the exception can be used to prompt the user to configure GMS.
            Throwable exceptionCause = e.getCause();
            if (exceptionCause instanceof ApiException) {
                ApiException apiException = (ApiException) exceptionCause;
                switch (apiException.getStatusCode()) {
                    case LocationSettingsStatusCodes
                            .RESOLUTION_REQUIRED:
                        if (apiException instanceof ResolvableApiException) {
                            ResolvableApiException resolvableApiException =
                                    (ResolvableApiException) apiException;
                            try {
                                resolvableApiException.startResolutionForResult(this,
                                        REQUEST_LOCATION_SETTINGS);
                            } catch (IntentSender.SendIntentException intentException) {
                                logerror(
                                        "Caught a SendIntentException attempting to launch the "
                                                + "Settings page");
                            }
                        } else {
                            logerror(
                                    "Settings resolution is required to access location, but "
                                            + "Exception is not an instanceof "
                                            + "ResolvableApiException");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        logerror(
                                "Settings resolution is required to access location, but the "
                                        + "change is unavailable from this app");
                        break;
                    default:
                        logerror("Received unknown status code " + apiException.getStatusCode()
                                + " from exception: "+e.getLocalizedMessage());
                }
            } else {
                logerror( "Caught an ExecutionException verifying the location settings: ");
            }
        } catch (TimeoutException | InterruptedException e) {
            logerror( "An exception was caught verifying the location settings: ");
        }
        return false;
    }


    /**
     * Sets up the device for location related tests.
     *
     * <p>The GMS location tests will query for the device's last known location as well as query
     * for the current location. This test ensures that the query for the current location will
     * complete as expected, and also seeds the last known location on the device in case the test
     * performing the location known location query is run first.
     */
    private boolean setupLocationTest() {
        final String ACCESS_LOCATION_ACTION = "ACCESS_LOCATION_ACTION";
        CountDownLatch[] latch = new CountDownLatch[1];
        latch[0] = new CountDownLatch(1);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            logerror( "Location permission has not been granted; prompting for it now");
            ActivityCompat.requestPermissions(this, LOCATION_PERMISSIONS,
                    REQUEST_LOCATION_PERMISSION);
            return false;
        } else {
            logdebug( "ACCESS_FINE_LOCATION permission has been granted");
        }

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                LocationResult result = LocationResult.extractResult(intent);
                // The countdown on the latch should only occur if the provided intent includes
                // a valid location since updates can be sent without location.
                if (result != null) {
                    Location location = result.getLastLocation();
                    if (location != null) {
                        logdebug( "Received a lat,long of " + location.getLatitude() + ", "
                                + location.getLongitude());
                        latch[0].countDown();
                    }
                }
            }
        };

        int flags = ContextCompat.RECEIVER_NOT_EXPORTED;//RECEIVER_EXPORTED
        ContextCompat.registerReceiver
                (this, receiver, new IntentFilter(ACCESS_LOCATION_ACTION), flags);

        //TODO: Bug? From U+ We can't create mutable Pending Intent directly ...
        //      So we can't utilize the LocationClient in this way
        //      We should investigate an alternative plan to verify the location service
        boolean locationReceived = false;
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            Intent intent = new Intent(ACCESS_LOCATION_ACTION);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
                    PendingIntent.FLAG_MUTABLE);
            //FLAG_NO_CREATE

            FusedLocationProviderClient locationClient =
                    LocationServices.getFusedLocationProviderClient(this);
            try {
                locationClient.requestLocationUpdates(mLocationRequest, pendingIntent);
            } catch (SecurityException e) {
                logerror("Caught a SecurityException requesting location updates: ");
                return false;
            }

            try {
                locationReceived = latch[0].await(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                logerror("Caught an InterruptedException: ");
            }
            locationClient.removeLocationUpdates(pendingIntent);
            unregisterReceiver(receiver);
            if (!locationReceived) {
                logerror("Location update not received within timeout window");
            }
        } else {
            logerror("We can not receive device location with pending intent after U+.");
        }
        return locationReceived;
    }

    /**
     * Sets up the device for the ACCESS_MEDIA_LOCATION test.
     *
     * <p>The ACCESS_MEDIA_LOCATION test requires a photo with location data in the EXIF metadata.
     * This method will copy the photo with location data from resources to the Pictures directory
     * to ensure the permission test has a photo from which to query the location.
     */
    private boolean setupMediaLocationTest() {
        // Scoped storage and the ACCESS_MEDIA_LOCATION permission were introduced in Android 10.
        boolean result = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //Image 1
            ContentResolver contentResolver = getApplication().getContentResolver();
            ContentValues photoContentValues = new ContentValues();
            photoContentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "TestImage");
            photoContentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            photoContentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/");
            photoContentValues.put(MediaStore.Images.Media.IS_PENDING, 1);
            Uri collectionUri = MediaStore.Images.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL_PRIMARY);
            Uri photoUri = contentResolver.insert(collectionUri, photoContentValues);

            try (InputStream inputStream = getResources().openRawResource(R.raw.test_image);
                 OutputStream outputStream = contentResolver.openOutputStream(photoUri)) {
                byte[] bytes = new byte[2048];
                while (inputStream.read(bytes) != -1) {
                    outputStream.write(bytes);
                }
                logdebug( "Successfully wrote file to pictures directory");

            } catch (IOException e) {
                logerror("Caught an exception copying file:");
                result = false;
            }
            photoContentValues.put(MediaStore.Images.Media.IS_PENDING, 0);
            contentResolver.update(photoUri, photoContentValues, null, null);

            //Image 1
            ContentValues photoContentValues2 = new ContentValues();
            photoContentValues2.put(MediaStore.Images.Media.DISPLAY_NAME, "PngImage");
            photoContentValues2.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            photoContentValues2.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/");
            photoContentValues2.put(MediaStore.Images.Media.IS_PENDING, 1);
            Uri collectionUri2 = MediaStore.Images.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL_PRIMARY);
            Uri photoUri2 = contentResolver.insert(collectionUri2, photoContentValues2);

            try (InputStream inputStream = getResources().openRawResource(R.raw.test_image2);
                 OutputStream outputStream = contentResolver.openOutputStream(photoUri2)) {
                byte[] bytes = new byte[2048];
                while (inputStream.read(bytes) != -1) {
                    outputStream.write(bytes);
                }
                logdebug( "Successfully wrote file to pictures directory");

            } catch (IOException e) {
                logerror("Caught an exception copying file:");
                result = false;
            }
            photoContentValues2.put(MediaStore.Images.Media.IS_PENDING, 0);
            contentResolver.update(photoUri2, photoContentValues2, null, null);

            //Audio
            ContentValues audioContentValues = new ContentValues();
            audioContentValues.put(MediaStore.Audio.Media.DISPLAY_NAME, "TestAudio");
            audioContentValues.put(MediaStore.Audio.Media.MIME_TYPE, "audio/mpeg");
            audioContentValues.put(MediaStore.Audio.Media.RELATIVE_PATH, "Music/");
            audioContentValues.put(MediaStore.Audio.Media.IS_PENDING, 1);
            collectionUri = MediaStore.Audio.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL_PRIMARY);
            Uri audioUri = contentResolver.insert(collectionUri, audioContentValues);

            try (InputStream inputStream = getResources().openRawResource(R.raw.test_audio);
                 OutputStream outputStream = contentResolver.openOutputStream(audioUri)) {
                byte[] bytes = new byte[2048];
                while (inputStream.read(bytes) != -1) {
                    outputStream.write(bytes);
                }
                logdebug( "Successfully wrote file to Music directory");
            } catch (IOException e) {
                logerror("Caught an exception copying file:");
                result = false;
            }
            audioContentValues.put(MediaStore.Audio.Media.IS_PENDING, 0);
            contentResolver.update(audioUri, audioContentValues, null, null);
            //Video
            ContentValues videoContentValues = new ContentValues();
            videoContentValues.put(MediaStore.Video.Media.DISPLAY_NAME, "TestVideo");
            videoContentValues.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
            videoContentValues.put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/");
            videoContentValues.put(MediaStore.Video.Media.IS_PENDING, 1);
            collectionUri = MediaStore.Video.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL_PRIMARY);
            Uri videoUri = contentResolver.insert(collectionUri, videoContentValues);

            try (InputStream inputStream = getResources().openRawResource(R.raw.test_video);
                 OutputStream outputStream = contentResolver.openOutputStream(videoUri)) {
                byte[] bytes = new byte[2048];
                while (inputStream.read(bytes) != -1) {
                    outputStream.write(bytes);
                }
                logdebug("Successfully wrote file to Movies directory");
            } catch (IOException e) {
                logerror("Caught an exception copying file:");
                result = false;
            }
            videoContentValues.put(MediaStore.Video.Media.IS_PENDING, 0);
            contentResolver.update(videoUri, videoContentValues, null, null);

        }
        return result;
    }

    /**
     * Sets up the device for the ACCESS_LOCUS_ID_USAGE_STATS test.
     *
     * <p>The ACCESS_LOCUS_ID_USAGE_STATS permission allows an app to query for locus events
     * created by other apps. This method generates a locus event to ensure there is an event
     * on the device for which the permission test can query. For additional details about locus
     * events see <a href="https://developer.android.com/reference/android/content/LocusId">
     * LocusId</a>.
     */
    private void setupLocusTest() {
        // While locus events were introduced in Android 10 the ACCESS_LOCUS_ID_USAGE_STATS
        // permission was introduced in Android 11.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            LocusId locusId = new LocusId("ACCESS_LOCUS_ID_USAGE_STATS_test");
            setLocusContext(locusId, null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
            int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean permissionGranted = true;
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                permissionGranted = false;
            }
        }
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (!permissionGranted) {
                    logerror( "The required permissions (" + String.join(", ", permissions)
                            + ") were not granted");
                    //runOnUiThread(
                    //        () -> mStatusAdapter.add(getResources().getString(R.string.location_permission_required));
                } else {
                    logdebug( "The location permission was granted; rerunning setup tasks");
                    new SetupTestsAsyncTask().execute();
                }
                break;
            default:
                logerror("An unexpected request code of " + requestCode + " with permissions "
                        + String.join(", ", permissions) + " + was received");

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_LOCATION_SETTINGS:
                LocationSettingsStates locationStates = LocationSettingsStates.fromIntent(data);
                logdebug(
                        "isLocationUsable: " + locationStates.isLocationUsable() + ", isGpsUsable: "
                                + locationStates.isGpsUsable() + ", isNetworkLocationUsable: "
                                + locationStates.isNetworkLocationUsable());
                if (resultCode == Activity.RESULT_OK) {
                    logdebug("Location settings have been updated; rerunning setup");
                    new SetupTestsAsyncTask().execute();
                } else {
                    logdebug("Location settings request completed with result code " + resultCode);
                }
                break;
            default:
                logerror("An unknown activity result was received: requestCode = " + requestCode
                        + ", resultCode = " + resultCode);
        }
    }
}
