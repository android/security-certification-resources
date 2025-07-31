package com.android.certification.niap.permission.dpctester.test.tool
/*
 * Copyright (C) 2024 The Android Open Source Project
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
import android.net.netstats.provider.INetworkStatsProvider

/**
 * A shim class that allows [TestableNetworkStatsProviderBinder] to be built against
 * different SDK versions.
 */
open class NetworkStatsProviderStubCompat : INetworkStatsProvider.Stub() {
    override fun onRequestStatsUpdate(token: Int) {}

    // Removed and won't be called in S+.
    fun onSetLimit(iface: String?, quotaBytes: Long) {}

    override fun onSetAlert(bytes: Long) {}

    // Added in S.
    override fun onSetWarningAndLimit(iface: String?, warningBytes: Long, limitBytes: Long) {}
}

