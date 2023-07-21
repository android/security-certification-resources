package com.android.certifications.niap.permissions.worker;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.certifications.niap.permissions.BasePermissionTester;
import com.android.certifications.niap.permissions.R;
import com.android.certifications.niap.permissions.activities.MainActivity;
import com.android.certifications.niap.permissions.config.BypassConfigException;
import com.android.certifications.niap.permissions.config.TestConfiguration;
import com.google.common.util.concurrent.ListenableFuture;

public class PermissionTesterWorker extends Worker {
    public PermissionTesterWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {


        return Result.success();
    }


    //public ListenableFuture<Result> startWork() {

        /*
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
        */
        //return null;
    //}
}

/*
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
     * displayed in the status text of the activity
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
}*/