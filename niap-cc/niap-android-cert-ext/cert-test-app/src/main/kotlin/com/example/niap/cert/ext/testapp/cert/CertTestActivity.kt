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

package com.example.niap.cert.ext.testapp.cert

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.android.niap.cert.manager.EnrollmentRequest
import com.android.niap.cert.manager.NiapCertManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.ByteArrayInputStream
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class CertTestActivity : ComponentActivity() {

    private lateinit var certManager: NiapCertManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        certManager = NiapCertManager(applicationContext)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CertAppScreen(certManager)
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
fun CertAppScreen(certManager: NiapCertManager) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val prefs = remember { context.getSharedPreferences("cert_test_prefs", Context.MODE_PRIVATE) }

    var alias       by remember { mutableStateOf(prefs.getString("alias",        "test_client_cert")!!) }
    var estUrl      by remember { mutableStateOf(prefs.getString("estUrl",       "https://localhost:8443/.well-known/est/")!!) }
    var authToken   by remember { mutableStateOf(prefs.getString("authToken",    "estuser:estpwd")!!) }
    var subjectDn   by remember { mutableStateOf(prefs.getString("subjectDn",    "CN=TestUser")!!) }
    var caPemUrl    by remember { mutableStateOf(prefs.getString("caPemUrl",     "http://localhost:8080/cacert.pem")!!) }
    var mtlsEndpoint by remember { mutableStateOf(prefs.getString("mtlsEndpoint", "https://localhost:8081/protected/")!!) }

    fun savePrefs() {
        prefs.edit()
            .putString("alias",        alias)
            .putString("estUrl",       estUrl)
            .putString("authToken",    authToken)
            .putString("subjectDn",    subjectDn)
            .putString("caPemUrl",     caPemUrl)
            .putString("mtlsEndpoint", mtlsEndpoint)
            .apply()
    }

    // Load CA PEM: download from URL if set, otherwise fall back to raw resource
    suspend fun loadCaPem(): String {
        if (caPemUrl.isNotBlank()) {
            return try {
                java.net.URL(caPemUrl).readText().also {
                    Log.d("CertTestApp", "Downloaded CA PEM from $caPemUrl (${it.length} bytes)")
                }
            } catch (e: Exception) {
                Log.w("CertTestApp", "Failed to download CA PEM: ${e.message}")
                ""
            }
        }
        val resId = context.resources.getIdentifier("est_validation_ca", "raw", context.packageName)
        return if (resId != 0) {
            context.resources.openRawResource(resId).bufferedReader().use { it.readText() }.also {
                Log.d("CertTestApp", "Loaded CA PEM from raw resource (${it.length} bytes)")
            }
        } else {
            Log.w("CertTestApp", "No CA PEM available (URL not set, raw resource not found)")
            ""
        }
    }

    var status      by remember { mutableStateOf("IDLE") }
    var certSummary by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var logMessages by remember { mutableStateOf(listOf<String>()) }

    // Dialog: (httpCode, responseBody) — null means closed
    var responseDialog by remember { mutableStateOf<Pair<Int, String>?>(null) }

    fun appendLog(msg: String) {
        Log.d("CertTestApp", msg)
        logMessages = logMessages + msg
    }

    // Response dialog with WebView
    responseDialog?.let { (code, body) ->
        AlertDialog(
            onDismissRequest = { responseDialog = null },
            title = { Text("HTTP $code") },
            text = {
                AndroidView(
                    factory = { ctx ->
                        WebView(ctx).apply {
                            val html = "<html><body><pre>${body.replace("&", "&amp;").replace("<", "&lt;")}</pre></body></html>"
                            loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                )
            },
            confirmButton = {
                TextButton(onClick = { responseDialog = null }) { Text("Close") }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("NIAP EST Enrollment Client", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = alias,     onValueChange = { alias = it;     savePrefs() }, label = { Text("Key Alias") },              modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = estUrl,    onValueChange = { estUrl = it;    savePrefs() }, label = { Text("EST Server URL") },          modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = authToken, onValueChange = { authToken = it; savePrefs() }, label = { Text("Auth Token (user:pass)") },  modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = subjectDn, onValueChange = { subjectDn = it; savePrefs() }, label = { Text("Subject DN") },             modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = caPemUrl,  onValueChange = { caPemUrl = it;  savePrefs() }, label = { Text("CA PEM URL (blank = raw resource)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = mtlsEndpoint, onValueChange = { mtlsEndpoint = it; savePrefs() }, label = { Text("mTLS Endpoint URL") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    logMessages  = emptyList()
                    certSummary  = null
                    errorMessage = null
                    status       = "ENROLLING"
                    val caPem = loadCaPem()
                    appendLog("CA PEM: ${caPem.length} bytes")
                    // Use custom CA as TLS trust anchor only for self-signed servers (localhost / private IP).
                    // Public endpoints (Cloud Run etc.) use the system trust store.
                    val isPrivate = estUrl.contains("localhost") || estUrl.contains("10.0.2.2") || estUrl.contains("192.168.")
                    val trustAnchor = if (isPrivate) caPem else ""
                    if (!isPrivate) appendLog("Public EST URL — using system trust store")
                    val req = EnrollmentRequest(alias, estUrl, authToken, subjectDn, trustedCaPem = trustAnchor)
                    try {
                        val certBytes = certManager.enroll(req).get(30, java.util.concurrent.TimeUnit.SECONDS)
                        status = "READY"
                        val cert = CertificateFactory.getInstance("X.509")
                            .generateCertificate(ByteArrayInputStream(certBytes)) as X509Certificate
                        certSummary = "Subject: ${cert.subjectDN}\nIssuer: ${cert.issuerDN}\nSerial: ${cert.serialNumber}\nAlg: ${cert.sigAlgName}\nValid: ${cert.notBefore} – ${cert.notAfter}"
                        appendLog("Enrollment succeeded")
                    } catch (e: java.util.concurrent.ExecutionException) {
                        status = "FAILED"
                        val detail = e.cause?.message ?: e.message ?: "Unknown error"
                        appendLog("Enrollment failed: $detail")
                        errorMessage = detail
                    } catch (e: Exception) {
                        status = "FAILED"
                        appendLog("Enrollment error: ${e.message}")
                        errorMessage = e.message
                    }
                }
            }) { Text("Enroll (EST)") }

            Button(
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        appendLog("Revoking alias: $alias")
                        try {
                            certManager.revoke(alias).get(15, java.util.concurrent.TimeUnit.SECONDS)
                            status      = "REVOKED"
                            certSummary = null
                            errorMessage = null
                        } catch (e: Exception) {
                            appendLog("Revoke failed: ${e.message}")
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) { Text("Revoke") }

            Button(
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        appendLog("Opening mTLS endpoint: $mtlsEndpoint")
                        try {
                            val caPem = loadCaPem()
                            val sslCtx = certManager.getSslContext(alias, caPem.ifBlank { null })
                            val tm: X509TrustManager = if (caPem.isNotBlank()) {
                                val ks = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
                                    load(null, null)
                                    setCertificateEntry("ca", CertificateFactory.getInstance("X.509")
                                        .generateCertificate(caPem.byteInputStream()) as X509Certificate)
                                }
                                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                                    .apply { init(ks) }.trustManagers[0] as X509TrustManager
                            } else {
                                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                                    .apply { init(null as KeyStore?) }.trustManagers[0] as X509TrustManager
                            }
                            val client = OkHttpClient.Builder()
                                .sslSocketFactory(sslCtx.socketFactory, tm)
                                .hostnameVerifier { _, _ -> true }
                                .build()
                            val response = client.newCall(Request.Builder().url(mtlsEndpoint).get().build()).execute()
                            val code = response.code
                            val body = response.body?.string() ?: ""
                            appendLog("Response: HTTP $code")
                            responseDialog = code to body
                        } catch (e: Exception) {
                            appendLog("Error: ${e.message}")
                            responseDialog = 0 to (e.message ?: e.javaClass.simpleName)
                        }
                    }
                },
                enabled = mtlsEndpoint.isNotBlank()
            ) { Text("mTLS") }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Status: $status", style = MaterialTheme.typography.titleMedium)

        errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Enrollment Failed", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onErrorContainer)
                    Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onErrorContainer)
                }
            }
        }

        certSummary?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Certificate", style = MaterialTheme.typography.titleSmall)
                    Text(it, style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        // ── Logs ───────────────────────────────────────────────────────────
        Spacer(modifier = Modifier.height(16.dp))
        Text("Logs:", style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(4.dp))
        Card(
            modifier = Modifier.fillMaxWidth().height(180.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(8.dp).verticalScroll(rememberScrollState())) {
                logMessages.forEach { Text(it, style = MaterialTheme.typography.bodySmall) }
            }
        }
    }
}
