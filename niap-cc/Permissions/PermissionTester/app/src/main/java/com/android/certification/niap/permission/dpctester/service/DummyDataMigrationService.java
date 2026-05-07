package com.android.certification.niap.permission.dpctester.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DummyDataMigrationService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
