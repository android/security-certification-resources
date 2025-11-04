/*
 * Copyright 2020 The Android Open Source Project
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

package com.android.certifications.niap.niapsec.net;

import com.android.certifications.niap.niapsec.SecureConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import static com.android.certifications.niap.niapsec.SecureConfig.SSL_TLS;

import android.util.Log;


/**
 * Custom SSLSocketFactory that uses ValidatableSSLSocket to handle automatic cert revocation
 * checking and forces TLS use.
 * <p>
 * Internal only
 */
class ValidatableSSLSocketFactory extends SSLSocketFactory {

    final private static String TAG = "ValidatableSSLSocketFactory";

    private final SSLSocketFactory sslSocketFactory;
    private final SecureURL secureURL;
    private final SecureConfig secureConfig;
    private final String[] supportedSecureCipherSuites;

    private Socket socket;

    public ValidatableSSLSocketFactory(SecureURL secureURL,
                                       SSLSocketFactory sslSocketFactory,
                                       SecureConfig secureConfig)  {
        SSLSocketFactory sslSocketFactory1;
        this.secureURL = secureURL;

        if(CertificateValidation.enforceTlsV1_2){
            try {
                SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
                sslContext.init(null,null,null);
                sslSocketFactory1 = sslContext.getSocketFactory();
            } catch (NoSuchAlgorithmException | KeyManagementException ex){
                Log.i(TAG,"Couldn't find TLSv1.2 algo.");
                sslSocketFactory1 = sslSocketFactory;
            }
        } else {
            sslSocketFactory1 = sslSocketFactory;
        }

        this.sslSocketFactory = sslSocketFactory1;
        this.secureConfig = secureConfig;
        this.supportedSecureCipherSuites = getSecureSupportedCipherSuites();
    }

    public ValidatableSSLSocketFactory(SecureURL secureURL, SSLSocketFactory sslSocketFactory) {
        this(secureURL, sslSocketFactory, SecureConfig.getStrongConfig());
    }

    public ValidatableSSLSocketFactory(SecureURL secureURL) {
        this(secureURL,
                (SSLSocketFactory) SSLSocketFactory.getDefault(),
                SecureConfig.getStrongConfig());
    }

    public ValidatableSSLSocketFactory(SecureURL secureURL,
                                       Map<String, InputStream> trustedCAs,
                                       SecureConfig secureConfig) {
        this(secureURL,
                createUserTrustSSLSocketFactory(trustedCAs, secureConfig, secureURL),
                secureConfig);
    }

    private String[] getSecureSupportedCipherSuites() {
        List<String> supportedCipherSuites = Arrays.asList(getSupportedCipherSuites());
        List<String> supportedSecureCipherSuites = new ArrayList<>();
        for(String cipherSuite : this.secureConfig.getStrongSSLCiphers()) {
            if(supportedCipherSuites.contains(cipherSuite)) {
                supportedSecureCipherSuites.add(cipherSuite);
            }
        }
        return supportedSecureCipherSuites.toArray(new String[0]);
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return sslSocketFactory.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return sslSocketFactory.getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket(Socket s, String host, int port, boolean autoClose)
            throws IOException {
        if (socket == null) {
            socket = new ValidatableSSLSocket(secureURL,
                    sslSocketFactory.createSocket(s, host, port, autoClose),
                    secureConfig,
                    supportedSecureCipherSuites);
        }
        return socket;
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        if (socket == null) {
            socket = new ValidatableSSLSocket(secureURL,
                    sslSocketFactory.createSocket(host, port),
                    secureConfig,
                    supportedSecureCipherSuites);
        }
        return socket;
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort)
            throws IOException {
        if (socket == null) {
            socket = new ValidatableSSLSocket(secureURL,
                    sslSocketFactory.createSocket(host, port, localHost, localPort),
                    secureConfig,
                    supportedSecureCipherSuites);
        }
        return socket;
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        if (socket == null) {
            socket = new ValidatableSSLSocket(secureURL,
                    sslSocketFactory.createSocket(host, port),
                    secureConfig,
                    supportedSecureCipherSuites);
        }
        return socket;
    }

    @Override
    public Socket createSocket(InetAddress address,
                               int port,
                               InetAddress localAddress,
                               int localPort) throws IOException {
        if (socket == null) {
            socket = new ValidatableSSLSocket(secureURL,
                    sslSocketFactory.createSocket(address, port, localAddress, localPort),
                    secureConfig,
                    supportedSecureCipherSuites);
        }
        return socket;
    }

    private static SSLSocketFactory createUserTrustSSLSocketFactory(
            Map<String, InputStream> trustAnchors,
            SecureConfig secureConfig,
            SecureURL secureURL) {
        try {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());

            KeyStore clientStore = KeyStore.getInstance(secureConfig.getKeystoreType());
            clientStore.load(null, null);

            KeyStore trustStore = null;
            switch (secureConfig.getTrustAnchorOptions()) {
                case USER_ONLY:
                case USER_SYSTEM:
                case LIMITED_SYSTEM:
                    trustStore = KeyStore.getInstance(secureConfig.getKeystoreType());
                    trustStore.load(null, null);
                    break;
            }

            switch (secureConfig.getTrustAnchorOptions()) {
                case USER_SYSTEM:
                    KeyStore caStore = KeyStore.getInstance(secureConfig.getAndroidCAStore());
                    caStore.load(null, null);
                    Enumeration<String> caAliases = caStore.aliases();
                    while (caAliases.hasMoreElements()) {
                        String alias = caAliases.nextElement();
                        assert trustStore != null;
                        trustStore.setCertificateEntry(alias, caStore.getCertificate(alias));
                    }
                    break;
                case USER_ONLY:
                case LIMITED_SYSTEM:
                    for (Map.Entry<String, InputStream> ca : trustAnchors.entrySet()) {
                        CertificateFactory cf = CertificateFactory.getInstance(
                                secureConfig.getCertPath());
                        Certificate userCert = cf.generateCertificate(ca.getValue());
                        assert trustStore != null;
                        trustStore.setCertificateEntry(ca.getKey(), userCert);
                    }
                    break;
            }

            tmf.init(trustStore);
            SSLContext sslContext = SSLContext.getInstance(SSL_TLS);

            KeyManager[] keyManagersArray = new KeyManager[1];
            keyManagersArray[0] = SecureKeyManager.getDefault(
                    secureURL.getClientCertAlias(),
                    secureConfig);
            sslContext.init(keyManagersArray, tmf.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (GeneralSecurityException | IOException ex) {
            throw new SecurityException("Issue creating User SSLSocketFactory.");
        }
    }

}
