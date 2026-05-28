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

package com.android.niap.cert.service

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.niap.cert.manager.EnrollmentRequest
import com.android.niap.cert.manager.NiapCertManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope

class ManagerActivity : ComponentActivity() {
    private lateinit var certManager: NiapCertManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        certManager = NiapCertManager(applicationContext)
        handleIntent(intent)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ManagerAppScreen(certManager)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        when (intent.getStringExtra("action")) {
        "verifyMtls" -> {
            val alias = intent.getStringExtra("alias") ?: "test_client_cert"
            val protectedUrl = intent.getStringExtra("protectedUrl") ?: "https://localhost:8443/protected/"
            val caPemUrl = intent.getStringExtra("caPemUrl") ?: "http://localhost:8080/cacert.pem"
            lifecycleScope.launch(Dispatchers.IO) {
                val caPem = try {
                    java.net.URL(caPemUrl).readText()
                } catch (e: Exception) {
                    Log.w("ManagerActivity", "Could not download CA PEM: ${e.message}")
                    ""
                }
                Log.d("ManagerActivity", "Auto-verifyMtls: alias=$alias url=$protectedUrl")
                val result = certManager.verifyMtls(alias, protectedUrl, caPem)
                Log.d("ManagerActivity", "MTLS_RESULT: $result")
            }
        }
        "enroll" -> {
            val alias = intent.getStringExtra("alias") ?: "test_client_cert"
            val estUrl = intent.getStringExtra("estUrl") ?: "https://localhost:8443/.well-known/est/"
            val authToken = intent.getStringExtra("authToken") ?: "estuser:estpwd"
            val subjectDn = intent.getStringExtra("subjectDn") ?: "CN=TestUser"

            lifecycleScope.launch(Dispatchers.IO) {
                val caPemUrl = intent.getStringExtra("caPemUrl") ?: "http://localhost:8080/cacert.pem"
                val caPem = try {
                    java.net.URL(caPemUrl).readText().also {
                        Log.d("ManagerActivity", "Downloaded CA PEM from $caPemUrl (${it.length} bytes)")
                    }
                } catch (e: Exception) {
                    Log.w("ManagerActivity", "Could not download CA PEM from $caPemUrl: ${e.message}")
                    ""
                }

                Log.d("ManagerActivity", "Auto-enroll: alias=$alias url=$estUrl subject=$subjectDn")
                val request = EnrollmentRequest(alias, estUrl, authToken, subjectDn, trustedCaPem = caPem)
                try {
                    certManager.enroll(request).get(30, java.util.concurrent.TimeUnit.SECONDS)
                    Log.d("ManagerActivity", "Enrollment succeeded: READY")
                } catch (e: java.util.concurrent.ExecutionException) {
                    Log.e("ManagerActivity", "Enrollment failed: ${e.cause?.message ?: e.message}")
                } catch (e: Exception) {
                    Log.e("ManagerActivity", "Enrollment failed: ${e.message}")
                }
            }
        }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        certManager.close()
    }
}

@Composable
fun ManagerAppScreen(certManager: NiapCertManager) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("NIAP Cert Manager Service", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "このアプリは Intent 経由で制御します。",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "enroll: action=enroll, alias, estUrl, authToken, subjectDn, caPemUrl\n" +
            "verify: action=verifyMtls, alias, protectedUrl, caPemUrl",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
