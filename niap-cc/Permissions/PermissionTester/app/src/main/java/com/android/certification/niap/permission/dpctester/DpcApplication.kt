package com.android.certification.niap.permission.dpctester
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
import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.core.os.HandlerCompat
import com.android.certification.niap.permission.dpctester.test.tool.BinderTransaction
import com.android.certification.niap.permission.dpctester.test.tool.BinderTransactsDict
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DpcApplication : Application() {
    var executorService: ExecutorService = Executors.newFixedThreadPool(5)
    var mainThreadHandler: Handler = HandlerCompat.createAsync(Looper.getMainLooper())
    override fun onCreate() {
        super.onCreate()

        /*invokeReflectionCall(statusBarManager.getClass(), "expandNotificationsPanel",
                statusBarManager, null);

        mTransacts.invokeTransact(Transacts.DEVICE_POLICY_SERVICE,
                Transacts.DEVICE_POLICY_DESCRIPTOR,
                Transacts.getNearbyNotificationStreamingPolicy, 0);*/
        //call it in more suitable place

        BinderTransaction.Builder(applicationContext).build()
        BinderTransactsDict.Builder(applicationContext).build()
    }

}