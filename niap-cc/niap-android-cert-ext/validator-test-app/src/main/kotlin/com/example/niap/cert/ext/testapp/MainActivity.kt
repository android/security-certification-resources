/*
 * Copyright 2026 The Android Open Source Project
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

package com.example.niap.cert.ext.testapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.android.niap.cert.validator.NiapCertValidator

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContentView(android.R.layout.simple_list_item_1)
        
        val textView = findViewById<android.widget.TextView>(android.R.id.text1)

        val url = intent.getStringExtra("openurl") ?: "https://www.google.com"
        val type = intent.getStringExtra("type") ?: "http"
        val buildTime = "2026-05-15 16:19"
        Log.d("TestApp", "Build Time: $buildTime")
        Log.d("TestApp", "URL to test: $url with type: $type")
        
        textView.text = "Build Time: $buildTime\nTesting $url using $type..."

        val data = Data.Builder()
            .putString("url", url)
            .putString("type", type)
            .build()

        val workRequest = OneTimeWorkRequest.Builder(NetworkWorker::class.java)
            .setInputData(data)
            .build()

        Log.d("TestApp", "Enqueuing NetworkWorker...")
        val workManager = WorkManager.getInstance(applicationContext)
        workManager.cancelAllWork()
        workManager.enqueue(workRequest)
        
        workManager.getWorkInfoByIdLiveData(workRequest.id)
            .observe(this) { workInfo ->
                if (workInfo != null) {
                    if (workInfo.state.isFinished) {
                        val status = workInfo.state.name
                        val error = workInfo.outputData.getString("error")
                        if (error != null) {
                            textView.text = "Result: $status\nError: $error"
                        } else {
                            textView.text = "Result: $status"
                        }
                    }
                }
            }
    }
}
