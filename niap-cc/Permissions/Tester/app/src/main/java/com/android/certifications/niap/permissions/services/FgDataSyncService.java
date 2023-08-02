package com.android.certifications.niap.permissions.services;

import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE;
import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC;

public class FgDataSyncService extends FgServiceTypeService{
    static
    {
       mServiceType = FOREGROUND_SERVICE_TYPE_DATA_SYNC;
       mId = mServiceType+1;
    }
}
