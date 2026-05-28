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

package com.android.niap.cert.manager

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import java.security.KeyStore
import java.security.Security
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

/**
 * Public entry point for apps. Hides the AIDL Service behind a standard,
 * Future-based API and provides JCA components ([getKeyManager], [getSslContext])
 * so apps can use enrolled certificates with any HTTPS stack — without needing
 * to know that a separate Service exists.
 *
 * Threading: methods are safe to call from any thread. The first call lazily
 * binds the Service; binding is held for the lifetime of this [NiapCertManager]
 * instance until [close] is called.
 */
class NiapCertManager(private val context: Context) {
    private var service: INiapCertManager? = null
    private var latch = CountDownLatch(1)
    private var isBinding = false

    /**
     * Pending enrollment futures. We keep a strong reference to each callback's
     * future here so that if the Service dies mid-enrollment, the death recipient
     * can fail every outstanding future deterministically.
     */
    private val pendingFutures = CopyOnWriteArraySet<CompletableFuture<*>>()

    private val deathRecipient = IBinder.DeathRecipient {
        Log.w("NiapCertManager", "Service binder died; failing ${pendingFutures.size} pending future(s)")
        service = null
        val err = IllegalStateException("cert-manager service died during operation")
        pendingFutures.forEach { it.completeExceptionally(err) }
        pendingFutures.clear()
    }

    init {
        if (Security.getProvider("NiapRemoteSigning") == null) {
            Security.insertProviderAt(RemoteSigningProvider(), 1)
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            Log.d("NiapCertManager", "Service connected")
            val proxy = INiapCertManager.Stub.asInterface(binder)
            service = proxy
            try {
                binder?.linkToDeath(deathRecipient, 0)
            } catch (e: Exception) {
                Log.w("NiapCertManager", "linkToDeath failed: ${e.message}")
            }
            isBinding = false
            latch.countDown()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("NiapCertManager", "Service disconnected")
            service = null
            isBinding = false
        }
    }

    internal fun bindService(): Boolean {
        if (service != null || isBinding) return true
        isBinding = true
        if (latch.count == 0L) latch = CountDownLatch(1)
        val intent = Intent("com.android.niap.cert.service.BIND").apply {
            setPackage("com.example.niap.cert.ext.manager")
        }
        Log.d("NiapCertManager", "Initiating bindService to com.example.niap.cert.ext.manager")
        return context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    /** Release the Service binding; further calls will rebind lazily. */
    fun close() {
        if (service != null) {
            try { context.unbindService(connection) } catch (e: Exception) {
                Log.w("NiapCertManager", "unbindService failed: ${e.message}")
            }
            service = null
            isBinding = false
        }
    }

    /**
     * Async enrollment. The returned [CompletableFuture] completes with the
     * enrolled DER-encoded leaf certificate on success, or completes
     * exceptionally on validation/network errors or Service death.
     */
    fun enroll(request: EnrollmentRequest): CompletableFuture<ByteArray> {
        val future = CompletableFuture<ByteArray>()
        pendingFutures.add(future)
        try {
            ensureServiceConnected()
            val svc = service ?: throw IllegalStateException("Service not connected")
            val configBundle = android.os.Bundle().apply {
                putBoolean("strictSigAlg", request.validatorConfig.strictSigAlg)
                putBoolean("enforceCaConstraints", request.validatorConfig.enforceCaConstraints)
                putBoolean("enforceMandatoryExtensions", request.validatorConfig.enforceMandatoryExtensions)
                putBoolean("enforceEku", request.validatorConfig.enforceEku)
            }
            val cb = object : IEnrollmentCallback.Stub() {
                override fun onSuccess(certificateData: ByteArray) {
                    pendingFutures.remove(future)
                    future.complete(certificateData)
                }
                override fun onError(errorMessage: String?) {
                    pendingFutures.remove(future)
                    future.completeExceptionally(EnrollmentException(errorMessage ?: "Unknown error"))
                }
            }
            svc.enroll(
                request.alias, request.estServerUrl, request.authToken, request.subjectDn,
                request.sans, request.csrSpec.keyType.name, request.csrSpec.sigAlg.name,
                request.trustedCaPem, configBundle, cb
            )
        } catch (e: Exception) {
            pendingFutures.remove(future)
            future.completeExceptionally(e)
        }
        return future
    }

    /** Async revocation. Future completes with Unit on success. */
    fun revoke(alias: String): CompletableFuture<Unit> {
        val future = CompletableFuture<Unit>()
        pendingFutures.add(future)
        try {
            ensureServiceConnected()
            val svc = service ?: throw IllegalStateException("Service not connected")
            val cb = object : IRevocationCallback.Stub() {
                override fun onSuccess() {
                    pendingFutures.remove(future)
                    future.complete(Unit)
                }
                override fun onError(errorMessage: String?) {
                    pendingFutures.remove(future)
                    future.completeExceptionally(RevocationException(errorMessage ?: "Unknown error"))
                }
            }
            svc.revoke(alias, cb)
        } catch (e: Exception) {
            pendingFutures.remove(future)
            future.completeExceptionally(e)
        }
        return future
    }

    /** True if an enrolled certificate for [alias] is available. */
    fun hasEnrollment(alias: String): Boolean {
        ensureServiceConnected()
        return service?.hasEnrollment(alias) ?: false
    }

    /** Returns the DER-encoded leaf certificate, or null if no enrollment exists. */
    fun getCertificate(alias: String): X509Certificate? {
        ensureServiceConnected()
        val bytes = service?.getCertificateData(alias) ?: return null
        if (bytes.isEmpty()) return null
        return CertificateFactory.getInstance("X.509")
            .generateCertificate(java.io.ByteArrayInputStream(bytes)) as? X509Certificate
    }

    /**
     * Returns a standard [javax.net.ssl.X509KeyManager] for the enrolled certificate.
     *
     * The returned KeyManager can be plugged directly into [SSLContext.init] and used
     * with any HTTP/TLS stack (OkHttp, HttpsURLConnection, …). The private key material
     * never leaves the cert-manager service — signing is delegated transparently via
     * the AIDL `sign()` oracle, hidden behind the standard JCA interface.
     */
    fun getKeyManager(alias: String): javax.net.ssl.X509KeyManager {
        ensureServiceConnected()
        val svc = service ?: throw IllegalStateException("Service not connected")
        val certBytes = svc.getCertificateData(alias)
        require(certBytes.isNotEmpty()) { "No certificate found for alias '$alias'" }
        val cert = java.security.cert.CertificateFactory.getInstance("X.509")
            .generateCertificate(java.io.ByteArrayInputStream(certBytes)) as X509Certificate
        return NiapKeyManager(alias, arrayOf(cert)) { digest -> svc.sign(alias, digest) }
    }

    /**
     * Convenience: builds an [SSLContext] initialised with the enrolled cert's
     * KeyManager and (optionally) a TrustManager derived from [trustedCaPem].
     * If [trustedCaPem] is null, system trust anchors are used.
     */
    fun getSslContext(alias: String, trustedCaPem: String? = null): SSLContext {
        val km = getKeyManager(alias)
        val tms = if (trustedCaPem.isNullOrBlank()) null else arrayOf(buildTrustManager(trustedCaPem))
        return SSLContext.getInstance("TLS").apply { init(arrayOf(km), tms, null) }
    }

    /**
     * Performs an mTLS GET request to [protectedUrl] using the enrolled certificate.
     *
     * Kept as a thin convenience wrapper around [getSslContext]; new callers should
     * use [getKeyManager] / [getSslContext] directly with their preferred HTTP stack.
     * Returns "HTTP <code>\n<body>" or "ERROR\n<message>".
     */
    fun verifyMtls(alias: String, protectedUrl: String, trustedCaPem: String): String {
        return try {
            val sslCtx = getSslContext(alias, trustedCaPem)
            val tm = buildTrustManager(trustedCaPem)
            val client = OkHttpClient.Builder()
                .sslSocketFactory(sslCtx.socketFactory, tm)
                .hostnameVerifier { _, _ -> true }
                .build()
            val response = client.newCall(Request.Builder().url(protectedUrl).get().build()).execute()
            val code = response.code
            val body = response.body?.string()?.trim() ?: ""
            Log.d("NiapCertManager", "verifyMtls result: HTTP $code")
            "HTTP $code\n$body"
        } catch (e: Exception) {
            Log.e("NiapCertManager", "verifyMtls error: ${e.message}", e)
            "ERROR\n${e.message ?: e.javaClass.simpleName}"
        }
    }

    // -------------------------------------------------------------------------
    // Internal helpers
    // -------------------------------------------------------------------------

    private fun buildTrustManager(caPem: String): X509TrustManager {
        val cf = CertificateFactory.getInstance("X.509")
        val caCert = cf.generateCertificate(caPem.byteInputStream()) as X509Certificate
        val trustStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
            load(null, null)
            setCertificateEntry("ca", caCert)
        }
        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            .apply { init(trustStore) }
        return tmf.trustManagers[0] as X509TrustManager
    }

    private fun ensureServiceConnected() {
        if (service == null) {
            bindService()
            val connected = latch.await(10, TimeUnit.SECONDS)
            if (!connected) {
                Log.e("NiapCertManager", "Timeout waiting for service binding")
                isBinding = false
            }
        }
    }
}
