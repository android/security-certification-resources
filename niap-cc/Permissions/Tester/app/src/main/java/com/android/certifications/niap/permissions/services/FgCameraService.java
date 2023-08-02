package com.android.certifications.niap.permissions.services;

import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA;

import android.content.Intent;

public class FgCameraService extends FgServiceTypeService{
    static
    {
       mServiceType = FOREGROUND_SERVICE_TYPE_CAMERA;
       mId = mServiceType+1;
    }
}
