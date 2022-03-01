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

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.certifications.niap.permissions.BasePermissionTester;
import com.android.certifications.niap.permissions.R;
import com.android.certifications.niap.permissions.config.BypassConfigException;
import com.android.certifications.niap.permissions.config.ConfigurationFactory;
import com.android.certifications.niap.permissions.config.TestConfiguration;
import com.android.certifications.niap.permissions.log.Logger;
import com.android.certifications.niap.permissions.log.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity to drive permission test configurations. This activity obtains the configuration(s) to
 * be run from the {@link ConfigurationFactory} and displays buttons that the user can tap to invoke
 * each individual permission test configuration. Upon completion the activity's status text is
 * updated with the final status of the test execution.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "PermissionTesterActivity";
    private static Logger sLogger = LoggerFactory.createDefaultLogger(TAG);

    private TextView mStatusTextView;
    private List<Button> mTestButtons = new ArrayList<>();
    private Context mContext;
    private TestConfiguration mConfiguration;

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
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
            int[] grantResults) {
        // Delegate handling of the permission request results to the active configuration.
        mConfiguration.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
