package com.android.certifications.niap.permissions.worker;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.certifications.niap.permissions.BasePermissionTester;
import com.android.certifications.niap.permissions.log.Logger;
import com.android.certifications.niap.permissions.log.LoggerFactory;

public class BackgroundTestWorker extends Worker {
    private static final String TAG = "PermissionTester";
    protected final SensorManager mSensorManager;
    protected final PackageManager mPackageManager;
    Logger mLogger;
    final Context mContext;
    public BackgroundTestWorker(Context context, WorkerParameters parameters) {

        super(context, parameters);
        mLogger = LoggerFactory.createDefaultLogger(TAG);
        mContext = context;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mPackageManager = (PackageManager) context.getPackageManager();
    }

    @NonNull
    @Override
    public Result doWork() {

        if (!mPackageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_HEART_RATE)) {
            mLogger.logDebug("Bypass : A hearrt rate monitor is not available to run this test");
            return Result.success();
        }
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        if (sensor == null) {
            mLogger.logDebug("The heart rate sensor feature is available, but a null sensor was returned");
            return Result.failure();
        }
        return Result.success();
    }
}
