package android.security.intrusiondetection;

oneway interface IIntrusionDetectionServiceCommandCallback {
    enum ErrorCode{
        UNKNOWN = 0,
        PERMISSION_DENIED = 1,
        INVALID_STATE_TRANSITION = 2,
        TRANSPORT_UNAVAILABLE = 3,
        DATA_SOURCE_UNAVAILABLE = 4,
    }
    void onSuccess();
    void onFailure(ErrorCode error);
}
