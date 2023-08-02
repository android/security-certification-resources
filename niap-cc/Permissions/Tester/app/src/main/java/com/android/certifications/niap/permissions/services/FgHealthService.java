package com.android.certifications.niap.permissions.services;

import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE;
import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH;

public class FgHealthService extends FgServiceTypeService{
    static
    {
       mServiceType = FOREGROUND_SERVICE_TYPE_HEALTH;
       mId = mServiceType+1;
    }
}
