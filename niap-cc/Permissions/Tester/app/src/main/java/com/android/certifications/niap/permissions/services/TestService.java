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

package com.android.certifications.niap.permissions.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.android.certifications.niap.permissions.Constants;
import com.android.certifications.niap.permissions.activities.MainActivity;
import com.android.certifications.niap.permissions.R;
import com.android.certifications.niap.permissions.log.Logger;
import com.android.certifications.niap.permissions.log.LoggerFactory;
import com.android.certifications.niap.permissions.log.StatusLogger;

/**
 * Started service that can be used to test permissions that require APIs invoked from a {link
 * Service}.
 */
public class TestService extends Service {
    private static final String TAG = "PermissionTesterService";
    private static final Logger sLogger = LoggerFactory.createDefaultLogger(TAG);

    private static final int NOTIFICATION_ID = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String permission = intent.getStringExtra(Constants.EXTRA_PERMISSION_NAME);
        boolean permissionGranted = intent.getBooleanExtra(Constants.EXTRA_PERMISSION_GRANTED, false);
        try {
            switch (permission) {
                case Manifest.permission.FOREGROUND_SERVICE: {
                    Intent notificationIntent = new Intent(this, MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                            notificationIntent, PendingIntent.FLAG_IMMUTABLE);

                    // Create a NotificationChannel to be used to start the service in the
                    // foreground.
                    CharSequence channelName = getString(R.string.service_channel_name);
                    NotificationChannel channel = new NotificationChannel(TAG, channelName,
                            NotificationManager.IMPORTANCE_DEFAULT);
                    NotificationManager notificationManager = getSystemService(
                            NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);

                    Notification notification =
                            new Notification.Builder(this, TAG)
                                    .setContentTitle(getText(R.string.service_notification_title))
                                    .setContentText(getText(R.string.service_notification_message))
                                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                                    .setContentIntent(pendingIntent)
                                    .build();
                    startForeground(NOTIFICATION_ID, notification);
                    stopForeground(true);
                    StatusLogger.logTestStatus(permission, permissionGranted, true);
                    break;
                }
                default:
                    StatusLogger.logError(
                            "Unexpected permission provided to TestService: " + permission);
                    break;
            }
        } catch (SecurityException e) {
            sLogger.logDebug("Caught a SecurityException invoking the test: ", e);
            StatusLogger.logTestStatus(permission, permissionGranted, false);
        } catch (Throwable t) {
            StatusLogger.logTestError(permission, t);
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
