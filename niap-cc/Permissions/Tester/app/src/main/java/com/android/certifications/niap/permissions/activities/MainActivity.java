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
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;

import com.android.certifications.niap.permissions.BasePermissionTester;
import com.android.certifications.niap.permissions.Constants;
import com.android.certifications.niap.permissions.GmsPermissionTester;
import com.android.certifications.niap.permissions.InstallPermissionTester;
import com.android.certifications.niap.permissions.InternalPermissionTester;
import com.android.certifications.niap.permissions.R;
import com.android.certifications.niap.permissions.RuntimePermissionTester;
import com.android.certifications.niap.permissions.SignaturePermissionTester;
import com.android.certifications.niap.permissions.TesterApplication;
import com.android.certifications.niap.permissions.config.BypassConfigException;
import com.android.certifications.niap.permissions.config.ConfigurationFactory;
import com.android.certifications.niap.permissions.config.TestConfiguration;
import com.android.certifications.niap.permissions.log.Logger;
import com.android.certifications.niap.permissions.log.LoggerFactory;
import com.android.certifications.niap.permissions.utils.LayoutUtils;
import com.android.certifications.niap.permissions.utils.gson.Test;
import com.android.certifications.niap.permissions.utils.gson.TestCategory;
import com.android.certifications.niap.permissions.utils.gson.TestSuites;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Activity to drive permission test configurations. This activity obtains the configuration(s) to
 * be run from the {@link ConfigurationFactory} and displays buttons that the user can tap to invoke
 * each individual permission test configuration. Upon completion the activity's status text is
 * updated with the final status of the test execution.
 */
public class MainActivity extends AppCompatActivity implements LogListAdaptable {
    private static final String TAG = "PermissionTesterActivity";
    private final Logger sLogger = LoggerFactory.createActivityLogger(TAG, this);

    private static final int ADMIN_INTENT = 1;

    private TextView mStatusTextView;
    private final List<Button> mTestButtons = new ArrayList<>();
    private Context mContext;
    private TestConfiguration mConfiguration;
    private ListView mStatusListView;
    List<String> mStatusData = new ArrayList<>();
    ArrayAdapter<String> mStatusAdapter = null;
    public void setLogAdapter(){
        mStatusAdapter = new ArrayAdapter<String>(
                (Context) this,
                android.R.layout.simple_list_item_1,
                mStatusData);
        mStatusListView = findViewById(R.id.statusTextView);
        mStatusListView.setAdapter(mStatusAdapter);
    }

    @Override
    public void addLogLine(String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mStatusAdapter != null)
                    mStatusAdapter.add(msg);

                assert mStatusAdapter != null;
                //mStatusAdapter.notifyDataSetChanged();
            }
        });
    }
    public void notifyUpdate(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStatusAdapter.notifyDataSetChanged();
                //getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
            }
        });
    }


    public ActivityResultLauncher<Intent> launhDeviceManagerTest = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                }
            });

    AtomicBoolean runningTest= new AtomicBoolean(false);
    void postTestersFinished(String message){
        //StatusTextView.setText(statusMessage);
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
                        .setContentText(message)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build();
        notificationManager.notify(0, notification);
        runningTest.set(false);
        runOnUiThread(()->{
            for (Button button : mTestButtons) {
                button.setEnabled(true);
            }
        });
    }


    BottomSheetBehavior<LinearLayout> mBottomSheet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mContext = this;
        LinearLayout layout = findViewById(R.id.mainLayout);

        mStatusTextView = new TextView(this);
        mStatusTextView.setText(R.string.tap_to_run);

        //layout.addView(mStatusTextView);
        setLogAdapter();
        addLogLine("Welcome!");
        // Obtain the list of configurations from the ConfigurationFactory and create a separate
        // button to allow the user to invoke each.
        List<TestConfiguration> configurations = ConfigurationFactory.getConfigurations(this);
        for (TestConfiguration configuration : configurations) {
            Button testButton = new Button(this);

            testButton.setText(configuration.getButtonTextId());
            mConfiguration = configuration;//activeConfiguration

            //it's difficult to pass the configuration object to workManager,
            //so I would like to choose ExecutorService instead of it
            testButton.setOnClickListener((view) -> {
                //Clear log
                mStatusListView.setAdapter(null);
                setLogAdapter();
                //disable all buttons//
                runningTest.set(true);
                for (Button button : mTestButtons) {
                    button.setEnabled(false);
                }
                //
                ExecutorService
                        executor = ((TesterApplication)getApplication()).executorService;

                AtomicInteger errorCnt= new AtomicInteger(0);
                AtomicInteger finishedTesters= new AtomicInteger(0);
                List<String> errorPermissions = new ArrayList<>();
                Future<Boolean> future = executor.submit(() -> {
                    try {
                        Handler handler = ((TesterApplication)getApplication()).mainThreadHandler;
                        handler.post(()->{
                            sLogger.logDebug("Call config->"+configuration.toString());
                            Activity activity = MainActivity.this;
                            mStatusData.clear();
                            mStatusAdapter.notifyDataSetChanged();
                            try {
                                configuration.preRunSetup();
                                int config_length = configuration.getPermissionTesters(activity).size();
                                for (BasePermissionTester permissionTester : configuration.getPermissionTesters(
                                        activity)) {
                                    String block = permissionTester.getClass().getSimpleName();
                                    sLogger.logSystem("Start Tester Block...:"+block);
                                    //if(!block.equals("SignaturePermissionTester")) continue;;
                                    permissionTester.runPermissionTestsByThreads((result)->{
                                        notifyUpdate();
                                        if(result.getResult()) {
                                            sLogger.logInfo("Test Passed:"+result);
                                        } else {
                                            sLogger.logError("Failure result for test:"+result);
                                            errorPermissions.add(result.getName());
                                            errorCnt.incrementAndGet();
                                        }
                                        if(result.getTotal() == result.getNo()){
                                            sLogger.logSystem("the test has done. error count="+errorCnt.get()+"/"+result.getTotal()+"/Failure Test Cases:");
                                            if(config_length == finishedTesters.incrementAndGet()){
                                                postTestersFinished("All test has been finished. Found "+errorCnt.get()+" errors.");
                                                for(String s : errorPermissions){
                                                    sLogger.logError(s);
                                                }
                                            }
                                        }
                                    });
                                }
                            } catch (BypassConfigException e) {
                                sLogger.logDebug("Bypassing test for current configuration: " + e);
                            }
                        });
                        return true;
                    } catch(Exception ex) {
                         sLogger.logError("SecureURL Failure: " + ex.getMessage());
                        return false;
                    }
                });

                try {
                    while(!future.isDone() && !future.isCancelled()) {
                        notifyUpdate();
                        System.out.println("Waiting for task completion...");
                    }

                    Boolean result = future.get();
                    //sLogger.logSystem(String.format("All test cases have been finished : resp=%s",result.toString()));
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });

            layout.addView(testButton);
            mTestButtons.add(testButton);
        }


        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        mBottomSheet = BottomSheetBehavior.from(layout);
        mBottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);

        mStatusTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (mBottomSheet.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if (mBottomSheet.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
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
        }

        return super.onOptionsItemSelected(item);
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
            sLogger.logInfo(">The application contains "+totalCounter+" test suites.");
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
