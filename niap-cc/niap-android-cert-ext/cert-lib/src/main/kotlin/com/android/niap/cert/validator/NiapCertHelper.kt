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

package com.android.niap.cert.validator

import android.content.Context
import android.util.Log
import org.xmlpull.v1.XmlPullParser
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLSession
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager
import java.security.KeyStore
import okhttp3.OkHttpClient
import okhttp3.ConnectionSpec
import okhttp3.TlsVersion

/**
 * Helper class to create secure network objects using NiapCertValidator.
 */
class NiapCertHelper private constructor(
    private val validator: NiapCertValidator = NiapCertValidator(),
    private val allowedCipherSuites: List<String>? = null,
    private val trustAnchorPolicy: TrustAnchorOptions = TrustAnchorOptions.BOTH_CA,
    private val enforceTls12: Boolean = true
) {
    enum class TrustAnchorOptions {
        USER_CA_ONLY,
        BOTH_CA,
        SYSTEM_CA_ONLY
    }
    
    /**
     * Validates that the URL uses HTTPS.
     */
    fun validateUrl(url: String) {
        validator.validateUrl(url)
    }

    companion object {
        @Volatile
        private var instance: NiapCertHelper? = null

        /**
         * Resets the singleton instance. Used for testing.
         */
        fun resetInstance() {
            instance = null
        }

        /**
         * Gets the singleton instance of NiapCertHelper, initializing it from XML config if necessary.
         */
        fun getInstance(context: Context): NiapCertHelper {
            return instance ?: synchronized(this) {
                instance ?: fromConfig(context).also { instance = it }
            }
        }
        /**
         * Creates a NiapCertHelper by loading configuration from res/xml/niap_security_config.xml.
         */
        fun fromConfig(context: Context): NiapCertHelper {
            val resId = context.resources.getIdentifier("niap_security_config", "xml", context.packageName)
            val parser = if (resId != 0) {
                context.resources.getXml(resId)
            } else {
                val libResId = context.resources.getIdentifier("niap_security_config", "xml", "com.android.niap.cert.validator")
                if (libResId != 0) {
                    context.resources.getXml(libResId)
                } else {
                    throw IllegalStateException("niap_security_config.xml not found")
                }
            }
            
            var strictSigAlg = true
            val prohibitedTlds = mutableSetOf<String>()
            val allowedCiphers = mutableListOf<String>()
            var trustAnchorPolicy = TrustAnchorOptions.BOTH_CA
            var enforceMandatoryExtensions = true
            var enforceEku = true
            var enforceCaConstraints = true
            var enforceTls12 = true
            var requiredEkus = listOf("serverAuth")
            
            var eventType = parser.eventType
            var currentTag = ""
            
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        currentTag = parser.name
                    }
                    XmlPullParser.TEXT -> {
                        val text = parser.text.trim()
                        if (text.isNotEmpty()) {
                            when (currentTag) {
                                "niap-rfc8603-strict-sigalg" -> {
                                    strictSigAlg = text.toBoolean()
                                }
                                "tld" -> {
                                    prohibitedTlds.add(text)
                                }
                                "cipher" -> {
                                    allowedCiphers.add(text)
                                }
                                "trust-anchor-policy" -> {
                                    trustAnchorPolicy = TrustAnchorOptions.valueOf(text.uppercase())
                                }
                                "enforce-mandatory-extensions" -> {
                                    enforceMandatoryExtensions = text.toBoolean()
                                }
                                "enforce-eku" -> {
                                    enforceEku = text.toBoolean()
                                }
                                "enforce-ca-constraints" -> {
                                    enforceCaConstraints = text.toBoolean()
                                }
                                "enforce-tls-1-2" -> {
                                    enforceTls12 = text.toBoolean()
                                }
                                "required-eku" -> {
                                    requiredEkus = text.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                                }
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        currentTag = ""
                    }
                }
                eventType = parser.next()
            }
            
           
            val validator = NiapCertValidator(
                strictSigAlg = strictSigAlg,
                prohibitedTldWildcards = prohibitedTlds,
                enforceCaConstraints = enforceCaConstraints,
                enforceMandatoryExtensions = enforceMandatoryExtensions,
                enforceEku = enforceEku,
                requiredEkus = requiredEkus
            )
            val cipherSuites = if (allowedCiphers.isNotEmpty()) allowedCiphers else null
            return NiapCertHelper(validator, cipherSuites, trustAnchorPolicy, enforceTls12)
        }
    }

    /**
     * Creates a SSLSocketFactory that enforces TLS 1.2 and applies NIAP certificate validation.
     */
    private fun createTrustManager(
        options: TrustAnchorOptions,
        trustAnchors: Map<String, java.io.InputStream>? = null
    ): X509TrustManager {
        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        
        return when (options) {
            TrustAnchorOptions.SYSTEM_CA_ONLY -> {
                tmf.init(null as KeyStore?)
                tmf.trustManagers.first { it is X509TrustManager } as X509TrustManager
            }
            TrustAnchorOptions.USER_CA_ONLY -> {
                val store = KeyStore.getInstance(KeyStore.getDefaultType())
                store.load(null, null)
                if (trustAnchors != null) {
                    val cf = java.security.cert.CertificateFactory.getInstance("X.509")
                    for ((alias, stream) in trustAnchors) {
                        val cert = cf.generateCertificate(stream)
                        store.setCertificateEntry(alias, cert)
                    }
                }
                tmf.init(store)
                tmf.trustManagers.first { it is X509TrustManager } as X509TrustManager
            }
            TrustAnchorOptions.BOTH_CA -> {
                tmf.init(null as KeyStore?)
                val systemTm = tmf.trustManagers.first { it is X509TrustManager } as X509TrustManager
                
                val store = KeyStore.getInstance(KeyStore.getDefaultType())
                store.load(null, null)
                if (trustAnchors != null) {
                    val cf = java.security.cert.CertificateFactory.getInstance("X.509")
                    for ((alias, stream) in trustAnchors) {
                        val cert = cf.generateCertificate(stream)
                        store.setCertificateEntry(alias, cert)
                    }
                }
                val userTmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                userTmf.init(store)
                val userTm = userTmf.trustManagers.first { it is X509TrustManager } as X509TrustManager
                
                CombinedTrustManager(systemTm, userTm)
            }
        }
    }

    fun createSSLSocketFactory(): SSLSocketFactory {
        val trustManager = createTrustManager(trustAnchorPolicy)
        return createSSLSocketFactory(trustManager)
    }

    fun createSSLSocketFactory(
        trustAnchors: Map<String, java.io.InputStream>,
        options: TrustAnchorOptions = trustAnchorPolicy
    ): SSLSocketFactory {
        val trustManager = createTrustManager(options, trustAnchors)
        return createSSLSocketFactory(trustManager)
    }

    private fun createSSLSocketFactory(trustManager: X509TrustManager): SSLSocketFactory {
        val niapTrustManager = NiapX509TrustManager(trustManager, validator)
        
        val sslContext = if (enforceTls12) {
            SSLContext.getInstance("TLSv1.2")
        } else {
            SSLContext.getInstance("TLS")
        }
        sslContext.init(null, arrayOf(niapTrustManager), null)
        
        val baseFactory = sslContext.socketFactory
        
        return if (allowedCipherSuites != null) {
            CipherEnforcingSSLSocketFactory(baseFactory, allowedCipherSuites.toTypedArray())
        } else {
            baseFactory
        }
    }

    /**
     * Opens a HttpsURLConnection for the given URL and applies NIAP security settings.
     */
    fun openConnection(url: java.net.URL): javax.net.ssl.HttpsURLConnection {
        val connection = url.openConnection() as javax.net.ssl.HttpsURLConnection
        connection.sslSocketFactory = createSSLSocketFactory()
        return connection
    }    /**
     * Verifies the connection after it has been established.
     * Enforces hostname verification.
     */

    /**
     * Verifies the hostname against the SSL session.
     */
    fun checkHostname(hostname: String, session: SSLSession) {
        validator.checkHostname(hostname, session)
    }

    fun configureOkHttp(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        val trustManager = createTrustManager(trustAnchorPolicy)
        return configureOkHttp(builder, trustManager)
    }

    fun configureOkHttp(
        builder: OkHttpClient.Builder,
        trustAnchors: Map<String, java.io.InputStream>,
        options: TrustAnchorOptions = trustAnchorPolicy
    ): OkHttpClient.Builder {
        val trustManager = createTrustManager(options, trustAnchors)
        return configureOkHttp(builder, trustManager)
    }

    private fun configureOkHttp(builder: OkHttpClient.Builder, trustManager: X509TrustManager): OkHttpClient.Builder {
        val niapTrustManager = NiapX509TrustManager(trustManager, validator)
        
        val sslContext = if (enforceTls12) {
            SSLContext.getInstance("TLSv1.2")
        } else {
            SSLContext.getInstance("TLS")
        }
        sslContext.init(null, arrayOf(niapTrustManager), null)
        
        val connectionSpecBuilder = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_3)
            
        if (allowedCipherSuites != null) {
            connectionSpecBuilder.cipherSuites(*allowedCipherSuites.toTypedArray())
        }
        
        val connectionSpec = connectionSpecBuilder.build()
            
        return builder
            .sslSocketFactory(sslContext.socketFactory, niapTrustManager)
            .connectionSpecs(listOf(connectionSpec))
    }

    private class CombinedTrustManager(
        private val systemTm: X509TrustManager,
        private val customTm: X509TrustManager
    ) : X509TrustManager {
        override fun checkClientTrusted(chain: Array<out java.security.cert.X509Certificate>?, authType: String?) {
            try {
                customTm.checkClientTrusted(chain, authType)
            } catch (e: java.security.cert.CertificateException) {
                systemTm.checkClientTrusted(chain, authType)
            }
        }

        override fun checkServerTrusted(chain: Array<out java.security.cert.X509Certificate>?, authType: String?) {
            try {
                customTm.checkServerTrusted(chain, authType)
            } catch (e: java.security.cert.CertificateException) {
                systemTm.checkServerTrusted(chain, authType)
            }
        }

        override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
            return systemTm.acceptedIssuers + customTm.acceptedIssuers
        }
    }

    private class CipherEnforcingSSLSocketFactory(
        private val delegate: SSLSocketFactory,
        private val allowedCipherSuites: Array<String>
    ) : SSLSocketFactory() {

        override fun getDefaultCipherSuites(): Array<String> = allowedCipherSuites
        override fun getSupportedCipherSuites(): Array<String> = allowedCipherSuites

        override fun createSocket(s: java.net.Socket?, host: String?, port: Int, autoClose: Boolean): java.net.Socket {
            val socket = delegate.createSocket(s, host, port, autoClose) as javax.net.ssl.SSLSocket
            socket.enabledCipherSuites = allowedCipherSuites
            return socket
        }

        override fun createSocket(host: String?, port: Int): java.net.Socket {
            val socket = delegate.createSocket(host, port) as javax.net.ssl.SSLSocket
            socket.enabledCipherSuites = allowedCipherSuites
            return socket
        }

        override fun createSocket(host: String?, port: Int, localHost: java.net.InetAddress?, localPort: Int): java.net.Socket {
            val socket = delegate.createSocket(host, port, localHost, localPort) as javax.net.ssl.SSLSocket
            socket.enabledCipherSuites = allowedCipherSuites
            return socket
        }

        override fun createSocket(host: java.net.InetAddress?, port: Int): java.net.Socket {
            val socket = delegate.createSocket(host, port) as javax.net.ssl.SSLSocket
            socket.enabledCipherSuites = allowedCipherSuites
            return socket
        }

        override fun createSocket(address: java.net.InetAddress?, port: Int, localAddress: java.net.InetAddress?, localPort: Int): java.net.Socket {
            val socket = delegate.createSocket(address, port, localAddress, localPort) as javax.net.ssl.SSLSocket
            socket.enabledCipherSuites = allowedCipherSuites
            return socket
        }
    }
}
