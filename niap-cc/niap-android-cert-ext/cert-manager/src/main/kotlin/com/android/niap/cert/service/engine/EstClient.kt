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

package com.android.niap.cert.service.engine

import android.content.Context
import android.util.Base64
import android.util.Log
import com.android.niap.cert.validator.NiapCertHelper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager
import javax.net.ssl.SSLSocketFactory

class EstClient(private val context: Context) {

    private val client: OkHttpClient = OkHttpClient.Builder().build()

    var lastError: String? = null

    /**
     * Acquires a certificate from an EST server (RFC 7030) using /simpleenroll endpoint.
     */
    fun acquireCertificate(csr: ByteArray, serverUrl: String, authToken: String, trustedCaPem: String): ByteArray {
        lastError = null
        val base64Csr = Base64.encodeToString(csr, Base64.DEFAULT)
        val requestBody = base64Csr.toRequestBody("application/pkcs10".toMediaType())

        // Ensure URL ends correctly without duplicate paths
        val endpoint = if (serverUrl.endsWith("/simpleenroll")) {
            serverUrl
        } else if (serverUrl.contains("/est/")) {
            // If user explicitly specified an est path (like /est/simpleenroll)
            val base = serverUrl.removeSuffix("/")
            if (base.endsWith("simpleenroll")) base else "$base/simpleenroll"
        } else if (serverUrl.contains("/.well-known/est")) {
            val base = serverUrl.removeSuffix("/").removeSuffix("/.well-known/est")
            "$base/.well-known/est/simpleenroll"
        } else {
            "${serverUrl.removeSuffix("/")}/.well-known/est/simpleenroll"
        }

        Log.d("EstClient", "POSTing CSR to EST endpoint: $endpoint")

        val requestBuilder = Request.Builder()
            .url(endpoint)
            .post(requestBody)
            .header("Content-Type", "application/pkcs10")

        if (authToken.isNotEmpty()) {
            val authHeader = if (authToken.startsWith("Basic ") || authToken.startsWith("Bearer ")) {
                authToken
            } else if (authToken.contains(":")) {
                val encoded = Base64.encodeToString(authToken.toByteArray(), Base64.NO_WRAP)
                "Basic $encoded"
            } else {
                "Basic $authToken"
            }
            requestBuilder.header("Authorization", authHeader)
            Log.d("EstClient", "Configured Authorization header style: ${if (authHeader.startsWith("Basic")) "Basic [Base64]" else "Custom"}")
        }

        val request = requestBuilder.build()

        val activeClient = if (trustedCaPem.isNotEmpty()) {
            try {
                Log.d("EstClient", "Configuring custom TLS trust anchor from PEM data")
                val (sslSocketFactory, trustManager) = createSslSocketFactory(trustedCaPem)
                client.newBuilder()
                    .sslSocketFactory(sslSocketFactory, trustManager)
                    .hostnameVerifier { _, _ -> true }
                    .build()
            } catch (e: Exception) {
                Log.e("EstClient", "Failed to configure custom trust manager, falling back to system defaults", e)
                client
            }
        } else {
            client
        }

        try {
            activeClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val msg = "HTTP ${response.code}: ${response.message}"
                    lastError = msg
                    Log.e("EstClient", "EST enrollment failed with $msg")
                    throw IOException(msg)
                }
                
                val responseBody = response.body?.bytes() ?: throw IOException("Empty response body from EST server")
                Log.d("EstClient", "EST enrollment successful. Received ${responseBody.size} bytes")
                Log.d("EstClient", "Response body string:\n" + String(responseBody))
                return responseBody
            }
        } catch (e: Exception) {
            lastError = lastError ?: e.message ?: e.javaClass.simpleName
            Log.e("EstClient", "Exception during EST acquisition: ${e.message}", e)
            return ByteArray(0)
        }
    }

    private fun createSslSocketFactory(caPem: String): Pair<SSLSocketFactory, X509TrustManager> {
        val cf = CertificateFactory.getInstance("X.509")
        val cert = cf.generateCertificate(caPem.byteInputStream())

        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
            load(null, null)
            setCertificateEntry("ca", cert)
        }

        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
            init(keyStore)
        }

        val trustManagers = tmf.trustManagers
        val x509TrustManager = trustManagers[0] as X509TrustManager

        val sslContext = SSLContext.getInstance("TLS").apply {
            init(null, arrayOf(x509TrustManager), null)
        }

        return Pair(sslContext.socketFactory, x509TrustManager)
    }

    fun revokeCertificate(alias: String): Boolean {
        Log.d("EstClient", "Revoking certificate for $alias")
        return true
    }
}
