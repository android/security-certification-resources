package com.android.certifications.niap.permissions.services;

import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE;
import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_PHONE_CALL;

public class FgPhoneCallService extends FgServiceTypeService{
    static
    {
       mServiceType = FOREGROUND_SERVICE_TYPE_PHONE_CALL;
       mId = mServiceType+1;
    }
}
