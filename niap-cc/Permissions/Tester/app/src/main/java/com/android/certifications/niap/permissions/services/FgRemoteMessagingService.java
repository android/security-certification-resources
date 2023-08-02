package com.android.certifications.niap.permissions.services;

import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE;
import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_REMOTE_MESSAGING;

public class FgRemoteMessagingService extends FgServiceTypeService{
    static
    {
       mServiceType = FOREGROUND_SERVICE_TYPE_REMOTE_MESSAGING;
       mId = mServiceType+1;
    }
}
