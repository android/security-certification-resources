package com.android.certifications.niap.permissions.services;

import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA;

import android.app.job.JobParameters;
import android.app.job.JobService;

import androidx.work.Configuration;

import com.android.certifications.niap.permissions.log.Logger;
import com.android.certifications.niap.permissions.log.LoggerFactory;

public class TestJobService extends JobService {

    private static final Logger mLogger = LoggerFactory.createDefaultLogger("TestJobService");

    public TestJobService(){
        Configuration.Builder builder = new Configuration.Builder();
        builder.setJobSchedulerJobIdRange(0, 2000).build();
    }
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        mLogger.logDebug("Test Job Service: onStartJob");
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
