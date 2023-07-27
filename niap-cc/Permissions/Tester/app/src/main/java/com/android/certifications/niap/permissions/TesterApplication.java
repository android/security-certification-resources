package com.android.certifications.niap.permissions;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TesterApplication extends Application {
    public ExecutorService executorService = Executors.newFixedThreadPool(5);
    public Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
