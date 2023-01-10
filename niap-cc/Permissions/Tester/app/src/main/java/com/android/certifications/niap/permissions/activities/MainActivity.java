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

package com.android.certifications.niap.permissions.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.android.certifications.niap.permissions.BasePermissionTester;
import com.android.certifications.niap.permissions.Constants;
import com.android.certifications.niap.permissions.GmsPermissionTester;
import com.android.certifications.niap.permissions.InstallPermissionTester;
import com.android.certifications.niap.permissions.InternalPermissionTester;
import com.android.certifications.niap.permissions.R;
import com.android.certifications.niap.permissions.RuntimePermissionTester;
import com.android.certifications.niap.permissions.SignaturePermissionTester;
import com.android.certifications.niap.permissions.config.BypassConfigException;
import com.android.certifications.niap.permissions.config.ConfigurationFactory;
import com.android.certifications.niap.permissions.config.TestConfiguration;
import com.android.certifications.niap.permissions.log.Logger;
import com.android.certifications.niap.permissions.log.LoggerFactory;
import com.android.certifications.niap.permissions.utils.gson.Test;
import com.android.certifications.niap.permissions.utils.gson.TestCategory;
import com.android.certifications.niap.permissions.utils.gson.TestSuites;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Activity to drive permission test configurations. This activity obtains the configuration(s) to
 * be run from the {@link ConfigurationFactory} and displays buttons that the user can tap to invoke
 * each individual permission test configuration. Upon completion the activity's status text is
 * updated with the final status of the test execution.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "PermissionTesterActivity";
    private static final Logger sLogger = LoggerFactory.createDefaultLogger(TAG);

    private static final int ADMIN_INTENT = 1;

    private TextView mStatusTextView;
    private final List<Button> mTestButtons = new ArrayList<>();
    private Context mContext;
    private TestConfiguration mConfiguration;

    public ActivityResultLauncher<Intent> launhDeviceManagerTest = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mContext = this;
        LinearLayout layout = findViewById(R.id.mainLayout);

        mStatusTextView = new TextView(this);
        mStatusTextView.setText(R.string.tap_to_run);
        mStatusTextView.setGravity(Gravity.CENTER);
        mStatusTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18f);
        layout.addView(mStatusTextView);

        // Obtain the list of configurations from the ConfigurationFactory and create a separate
        // button to allow the user to invoke each.
        List<TestConfiguration> configurations = ConfigurationFactory.getConfigurations(this);
        for (TestConfiguration configuration : configurations) {
            Button testButton = new Button(this);

            testButton.setText(configuration.getButtonTextId());
            testButton.setOnClickListener((view) -> {
                new PermissionTesterAsyncTask().execute(configuration);
            });
            layout.addView(testButton);
            mTestButtons.add(testButton);
        }
       
        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

    }
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        //menu.findItem(R.id.action_force_chrome).setChecked(viewModel.isForceChrome.get());
        return true;
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            /*
             * Create a json file for checking whole testing items
             */
            case R.id.action_output_tester_json:
                writeAllTesterDetailsToJson();
                break;
            case R.id.action_request_runtime_permissions:
                requestRuntimePermissionsForSignatureTests();
                break;
            /*case R.id.action_device_admin_test:
                if (!mDevicePolicyManager.isAdminActive(mComponentName)) {
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Administrator description");
                    startActivityForResult(intent, ADMIN_INTENT);
                }
            break;*/
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * {@link AsyncTask} used to drive the permission test execution using the hooks provided by the
     * {@link TestConfiguration}.
     */
    private class PermissionTesterAsyncTask extends AsyncTask<TestConfiguration, Void, String> {
        @Override
        protected void onPreExecute() {
            for (Button button : mTestButtons) {
                button.setEnabled(false);
            }
            mStatusTextView.setText(R.string.test_in_progress);
        }

        /**
         * Executes the permission test defined by the provided {@code configuration} in the
         * background, returning a {@code String} representing the final status that should be
         * displayed in the status text of the activity.
         */
        @Override
        protected String doInBackground(TestConfiguration... configurations) {
            boolean mAllTestsPassed = true;
            Activity activity = MainActivity.this;

            mConfiguration = configurations[0];
            try {
                configurations[0].preRunSetup();
            } catch (BypassConfigException e) {
                sLogger.logDebug("Bypassing test for current configuration: " + e);
                return e.getMessage();
            }

            for (BasePermissionTester permissionTester : mConfiguration.getPermissionTesters(
                    activity)) {

                //PermissionUtils.checkTester(permissionTester);
                if (!permissionTester.runPermissionTests()) {
                    mAllTestsPassed = false;
                }
            }
            int statusId = mAllTestsPassed ? R.string.test_passed : R.string.test_failed;
            return activity.getResources().getString(statusId);
        }

        @Override
        protected void onPostExecute(String statusMessage) {
            // Update the Activity's status text with the output from doInBackground and add a
            // notification with the same text.
            mStatusTextView.setText(statusMessage);
            Resources resources = getResources();
            CharSequence channelName = resources.getString(R.string.tester_channel_name);
            NotificationChannel channel = new NotificationChannel(TAG, channelName,
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            Intent notificationIntent = new Intent(mContext, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE);
            Notification notification =
                    new Notification.Builder(mContext, TAG)
                            .setContentTitle(resources.getText(R.string.status_notification_title))
                            .setContentText(statusMessage)
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .build();
            notificationManager.notify(0, notification);

            for (Button button : mTestButtons) {
                button.setEnabled(true);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADMIN_INTENT) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Registered As Admin", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to register as Admin", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Delegate handling of the permission request results to the active configuration.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(mConfiguration != null)
            mConfiguration.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    final String[] RUNTIME_PERMS_FOR_SIG = new String[]{
        "android.permission.BLUETOOTH_CONNECT",
        "android.permission.READ_CALL_LOG",
        "android.permission.RECEIVE_SMS",
        "com.android.voicemail.permission.ADD_VOICEMAIL",
        "android.permission.RECORD_AUDIO"
    };

    private void requestRuntimePermissionsForSignatureTests()
    {
        List<String> not_granted = new ArrayList<String>();
        for(String p:RUNTIME_PERMS_FOR_SIG){
            if (checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED) {
                not_granted.add(p);
            }
        }
        String[] perms = not_granted.toArray(new String[0]);
        ActivityCompat.requestPermissions(this,perms,
                Constants.PERMISSION_CODE_RUNTIME_DEPENDENT_PERMISSIONS);
    }

    private void writeAllTesterDetailsToJson() {
        try {

            final String fileName = "all-tester.json";
            OutputStreamWriter writer = new OutputStreamWriter(mContext.openFileOutput(
                    fileName, Context.MODE_PRIVATE));
            //Prepare all defined tester
            List<TestConfiguration> configurations =
                    ConfigurationFactory.getConfigurations(this);
            if(configurations.size() == 0){
                sLogger.logError("[error] configuration not found!");
                return;
            }
            List<BasePermissionTester> sources = new ArrayList<>();
            TestConfiguration c = configurations.get(0);
            sources.add(new InternalPermissionTester(c,this));
            sources.add(new InstallPermissionTester(c,this));
            sources.add(new RuntimePermissionTester(c,this));
            sources.add(new SignaturePermissionTester(c,this));
            sources.add(new GmsPermissionTester(c,this));

            //Note : Output all tests suites into below structure for checking project health
            //Test Category ... :
            //    Name: [Name,Category,minVersion,maxVersion] ...
            //Analysis
            //  Naming Conflicts:[Name ....]
            //  Test Counts : [Category:Name ....]
            Set<String> dupecheck = new HashSet<>();
            List<String> dupe = new ArrayList<>();
            Map<String,Integer> testCounter = new HashMap<>();
            int totalCounter = 0;
            TestSuites gsonTestSuites = new TestSuites();
            for(BasePermissionTester s:sources){
                Integer testCount = 0;
                Map<String,BasePermissionTester.PermissionTest> tests = s.getRegisteredPermissions();
                String testCategory = s.getClass().getSimpleName().
                        replaceAll("PermissionTester$","").toLowerCase();
                TestCategory gsonCategory = new TestCategory(testCategory);
                sLogger.logInfo(" >"+testCategory+"");

                for(Map.Entry<String,BasePermissionTester.PermissionTest> entry:tests.entrySet()){
                    String key = entry.getKey();
                    //register dupe
                    if(dupecheck.contains(key)) dupe.add(testCategory+"$"+key); else dupecheck.add(key);
                    BasePermissionTester.PermissionTest test = entry.getValue();
                    gsonCategory.tests.add(new Test(key,test.mMinApiLevel,test.mMaxApiLevel));
                    testCount++;
                    totalCounter++;
                }
                sLogger.logInfo(" >"+testCategory+" contains "+testCount+" test suites.");
                gsonTestSuites.testCategories.add(gsonCategory);
                testCounter.put(testCategory,testCount);
            }
            sLogger.logInfo(">The applicatin contains "+totalCounter+" test suites.");
            sLogger.logInfo(">Test Conflicts :"+dupe.toString());

            Gson gson = new Gson();
            writer.write(gson.toJson(gsonTestSuites));

            writer.close();

            sLogger.logInfo( "Completed to output all tester details to json: " + mContext.getFilesDir() + "/"+fileName);

        } catch (IOException e) {
            sLogger.logError( "Caught an IOException writing the file: ", e);
        }
    }
}
