package com.google.android.satellite.aidl;

// Copies consumer pattern for an operation that requires a boolean result from another process to
// finish.
oneway interface IBooleanConsumer {
    void accept(boolean result);
}