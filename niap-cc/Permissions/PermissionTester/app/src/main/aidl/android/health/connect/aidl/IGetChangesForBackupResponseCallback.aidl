/*
 * Copyright (C) 2025 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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