package com.google.android.satellite.aidl;

// Copies consumer pattern for an operation that requires an integer result from another process to
// finish.
oneway interface IIntegerConsumer {
    void accept(int result);
}
