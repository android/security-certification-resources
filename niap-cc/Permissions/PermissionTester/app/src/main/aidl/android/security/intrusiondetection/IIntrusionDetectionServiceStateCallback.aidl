package android.security.intrusiondetection;

/**
 * @hide
 */
 oneway interface IIntrusionDetectionServiceStateCallback {
    enum State{
        UNKNOWN = 0,
        DISABLED = 1,
        ENABLED = 2,
    }
    void onStateChange(State state);
 }