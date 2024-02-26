/*
 * Copyright 2023 The Android Open Source Project
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

import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.android.certifications.niap.permissions.R;
import com.android.certifications.niap.permissions.log.Logger;
import com.android.certifications.niap.permissions.log.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Started service that can be used to test permissions that require APIs invoked from a {link
 * Service}.
 */

abstract class FgServiceTypeService extends Service {
    protected static final String TAG = "FgServiceType";
    protected AtomicBoolean mRunning = new AtomicBoolean(false);
    private final IBinder mBinder = new LocalBinder();

    static int mServiceType = 0;
    static int mId = 0;

    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationManager m = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        String channel_id = getClass().getSimpleName()+"_tester";
        //System.out.println("starting fgservice =>"+channel_id);
        if(m.getNotificationChannel(channel_id ) == null){
            NotificationChannel channel  =
                    new NotificationChannel(channel_id , channel_id
                            , NotificationManager.IMPORTANCE_HIGH);

            channel.setDescription("description");
            m.createNotificationChannel(channel);
        }
        Notification nc = new NotificationCompat.Builder(this,channel_id)
                .setContentText("content_text")
                .setContentTitle(channel_id)
                .setSmallIcon(R.drawable.ic_launcher_background).build();
        //live only 5 second
        Thread th = new Thread(()->{
            for(int i=0;i<5;i++){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                stopForeground(Service.STOP_FOREGROUND_DETACH);
                mRunning.set(false);
            }
        });
        th.start();
        //System.out.println("started fgservice =>"+channel_id);
        mRunning.set(true);
        try {
            startForeground(mId, nc, mServiceType);
        } catch (Exception ex){
            ex.printStackTrace();
            //call main thread?
            mRunning.set(false);
        }
        return Service.START_NOT_STICKY;
    }


    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        FgServiceTypeService getService() {
            return FgServiceTypeService.this;
        }
    }
}
