package com.android.certification.niap.permission.dpctester.receiver;
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
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import com.android.certification.niap.permission.dpctester.common.NotificationUtil;
import com.android.certification.niap.permission.dpctester.R;

public class PackageMonitorReceiver extends BroadcastReceiver {
    private static final String TAG = "PackageMonitorReceiver";
    private static final int PACKAGE_CHANGED_NOTIIFICATION_ID = 34857;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (!Intent.ACTION_PACKAGE_ADDED.equals(action)
                && !Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
            return;
        }
        String packageName = getPackageNameFromIntent(intent);
        if (TextUtils.isEmpty(packageName)) {
            return;
        }
        boolean replacing = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false);
        if (replacing) {
            return;
        }
        String notificationBody = buildNotificationText(context, packageName, action);
        Notification notification =
                NotificationUtil.getNotificationBuilder(context)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(context.getString(R.string.package_changed_notification_title))
                        .setContentText(notificationBody)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationBody))
                        .setDefaults(Notification.DEFAULT_LIGHTS)
                        .setOnlyAlertOnce(true)
                        .build();
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(PACKAGE_CHANGED_NOTIIFICATION_ID, notification);
    }

    private String getPackageNameFromIntent(Intent intent) {
        if (intent.getData() == null) {
            return null;
        }
        return intent.getData().getSchemeSpecificPart();
    }

    private String buildNotificationText(Context context, String pkgName, String action) {
        int res =
                Intent.ACTION_PACKAGE_ADDED.equals(action)
                        ? R.string.package_added_notification_text
                        : R.string.package_removed_notification_text;
        return context.getString(res, pkgName);
    }
}
