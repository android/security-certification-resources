package android.health.connect.aidl;

import android.health.connect.backuprestore.GetChangesForBackupResponse;
import android.health.connect.aidl.HealthConnectExceptionParcel;

/**
 * Callback for {@link HealthConnectManager#getChangesForBackup}
 * {@hide}
 */
interface IGetChangesForBackupResponseCallback {
    // Called on a successful operation
    oneway void onResult(in GetChangesForBackupResponse parcel);
    // Called when an error is hit
    oneway void onError(in HealthConnectExceptionParcel exception);
}