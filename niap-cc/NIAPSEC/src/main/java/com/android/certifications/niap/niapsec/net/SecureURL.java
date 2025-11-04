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

import android.util.Log;

import com.android.certifications.niap.niapsec.SecureConfig;
import com.android.certifications.niap.niapsec.config.TldConstants;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.PKIXParameters;
import java.security.cert.PKIXRevocationChecker;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;

/**
 * Wraps URL to provide automatic cert revocation checking through OCSP and enforces TLS use.
 */
public class SecureURL {

    private static final String TAG = "SecureURL";

    private final URL url;
    private final SecureConfig secureConfig;
    private final String clientCertAlias;

    /**
     * Creates a SecureURL
     *
     * @param spec The URL spec
     * @param clientCertAlias The cert alias of the client cert to be used (The parameter for KeyChain.choosePrivateKeyAlias), accept null
     * @throws MalformedURLException If the spec is malformed and not valid URL
     */
    public SecureURL(String spec, String clientCertAlias) throws MalformedURLException {
        this(spec, clientCertAlias, SecureConfig.getStrongConfig());
    }

    /**
     * Creates a SecureURL
     *
     * @param spec The URL spec
     * @param clientCertAlias The cert alias of the client cert to be used
     * @param secureConfig The provided settings to configure TLS version
     * @throws MalformedURLException If the spec is malformed and not valid URL
     */
    public SecureURL(String spec, String clientCertAlias, SecureConfig secureConfig)
            throws MalformedURLException {
        this.url = new URL(addProtocol(spec));
        this.clientCertAlias = clientCertAlias;
        this.secureConfig = secureConfig;
    }

    /**
     * Open the TLS connection to the host specified at construction
     *
     * @return The connection that has been established
     * @throws IOException when there is a bad host name or TLS configuration
     */
    public URLConnection openConnection() throws IOException {
        Log.i(TAG, SecureConfig.PACKAGE_NAME +
                " initiated a trusted channel to " + getHostname());
        HttpsURLConnection urlConnection = (HttpsURLConnection) this.url.openConnection();
        urlConnection.setSSLSocketFactory(new ValidatableSSLSocketFactory(this));
        Log.i(TAG, "TLS session established and validated to " + getHostInfo());
        return urlConnection;
    }

    /**
     * Checks the hostname against an open SSLSocket connect to the hostname for validity for certs
     * and hostname validity.
     *
     * @param trustedCAs List of trustedCA files to use
     * @return URLConnection if the SSLSocket has a valid cert and if the hostname is valid
     */
    public URLConnection openUserTrustedCertConnection(Map<String, InputStream> trustedCAs)
            throws IOException {
        Log.i(TAG, SecureConfig.PACKAGE_NAME + " initiated a trusted channel to " +
                getHostname());
        HttpsURLConnection urlConnection = (HttpsURLConnection) this.url.openConnection();
        urlConnection.setSSLSocketFactory(
                new ValidatableSSLSocketFactory(this, trustedCAs, secureConfig));
        Log.i(TAG, "TLS session established and validated to " + getHostInfo());
        return urlConnection;
    }

    /**
     * Checks the hostname against an open SSLSocket connect to the hostname for validity for certs
     * and hostname validity.
     *
     * @param hostname The host name to check
     * @param socket   The SSLSocket that is open to the URL of the host to check
     * @return true if the SSLSocket has a valid cert and if the hostname is valid, false otherwise.
     */
    public boolean isValid(String hostname, SSLSocket socket) {
        try {
            boolean valid = isValid(Arrays.asList(socket.getSession().getPeerCertificates()));
            boolean hostnameValid = HttpsURLConnection.getDefaultHostnameVerifier()
                    .verify(hostname, socket.getSession());
            if(!valid) {
                Log.i(TAG, "Failed to validate X509v3 certificates. "+ getHostInfo());
            } else if(!hostnameValid) {
                Log.i(TAG, "Failed to validate presented identifier. "+getHostInfo());
            }
            return hostnameValid
                    && valid
                    && validTldWildcards(Arrays.asList(socket.getSession().getPeerCertificates()));
        } catch (SSLPeerUnverifiedException e) {
            Log.i(TAG, "Validity Check failed: "+ getHostInfo() + " Ex:" + e.getMessage());
            //e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks the HttpsUrlConnection certificates for validity.
     * <p>
     * Example Code:
     * URL url = new URL("https://" + urlText);
     * conn = (HttpsURLConnection) url.openConnection();
     * boolean valid = SecureURL.isValid(conn);
     * </p>
     *
     * @param conn The connection to check the certificates for
     * @return true if the certificates for the HttpsUrlConnection are valid, false otherwise
     */
    public boolean isValid(HttpsURLConnection conn) {
        try {
            return isValid(Arrays.asList(conn.getServerCertificates())) &&
                    validTldWildcards(Arrays.asList(conn.getServerCertificates()));
        } catch (SSLPeerUnverifiedException e) {
            Log.i(TAG, "Valid Check failed: " + e.getMessage());
            e.fillInStackTrace();
            return false;
        }
    }

    /**
     * Method to check a certificate for validity.
     *
     * @param cert cert to check
     * @return true if the certs are valid, false otherwise
     */
    public boolean isValid(Certificate cert) {
        List<Certificate> certs = new ArrayList<>();
        certs.add(cert);
        return isValid(certs);
    }



    /**
     * Method to check a list of certificates for validity.
     * If certificate revocation check can only use OCSP verification, it works.
     * Otherwise PKIXRevocationChecker raise errors.
     *
     * @param certs list of certs to check
     * @return true if the certs are valid, false otherwise
     */
    public boolean isValid(List<? extends Certificate> certs) {
        try {
            List<Certificate> leafCerts = new ArrayList<>();
            for (Certificate cert : certs) {
                if (!isRootCA(cert)) {
                    leafCerts.add(cert);
                }
            }
            CertPath path = CertificateFactory.getInstance(secureConfig.getCertPath())
                    .generateCertPath(leafCerts);
            KeyStore ks = KeyStore.getInstance(secureConfig.getAndroidCAStore());
            try {
                ks.load(null, null);
            } catch (IOException e) {
                e.fillInStackTrace();
                throw new AssertionError(e);
            }
            CertPathValidator cpv = CertPathValidator.getInstance(
                    secureConfig.getCertPathValidator());//=PKIX by default
            PKIXParameters params = new PKIXParameters(ks);
            params.setRevocationEnabled(true);

            PKIXRevocationChecker checker = (PKIXRevocationChecker) cpv.getRevocationChecker();
            if(CertificateValidation.cpvCheckDoNotFallbackToCrl) {
                checker.setOptions(EnumSet.of
                        (PKIXRevocationChecker.Option.NO_FALLBACK, PKIXRevocationChecker.Option.SOFT_FAIL));
            }
            params.addCertPathChecker(checker);
            cpv.validate(path, params);

            return true;
        } catch (CertPathValidatorException e) {
            // If you see "Unable to determine revocation status due to network error"
            // Make sure your network security config allows for clear text access of the relevant
            // OCSP url.
            e.fillInStackTrace();
            return false;
        } catch (GeneralSecurityException e) {
            e.fillInStackTrace();
            return false;
        }
    }

    /**
     * Get the hostname of the URL specified at construction
     *
     * @return The hostname of the URL
     */
    public String getHostname() {
        return this.url.getHost();
    }

    /**
     * Get the host from the URL, and the package name of the app for logging purposes
     *
     * @return The host name concatenated with the package name.
     */
    public String getHostInfo() {
        return "Host: "+ this.url.getHost() + " App: " + SecureConfig.PACKAGE_NAME;
    }

    /**
     * Gets the client cert alias
     *
     * @return The client cert alias given at construction
     */
    public String getClientCertAlias() {
        return this.clientCertAlias;
    }

    private String addProtocol(String spec) {
        if (!spec.toLowerCase().startsWith("http://") &&
                !spec.toLowerCase().startsWith("https://")) {
            return "https://" + spec;
        }
        return spec;
    }

    private boolean isRootCA(Certificate cert) {
        boolean rootCA = false;
        if (cert instanceof X509Certificate) {
            X509Certificate x509Certificate = (X509Certificate) cert;
            if (x509Certificate.getSubjectDN().getName()
                    .equals(x509Certificate.getIssuerDN().getName())) {
                rootCA = true;
            }
        }
        return rootCA;
    }

    private boolean validTldWildcards(List<? extends Certificate> certs) {
        // For a more complete list https://publicsuffix.org/list/public_suffix_list.dat
        for (Certificate cert : certs) {
            if (cert instanceof X509Certificate) {
                X509Certificate x509Cert = (X509Certificate) cert;
                try {
                    Collection<List<?>> subAltNames = x509Cert.getSubjectAlternativeNames();
                    if (subAltNames != null) {
                        List<String> dnsNames = new ArrayList<>();
                        for (List<?> tldList : subAltNames) {
                            if (tldList.size() >= 2) {
                                dnsNames.add(tldList.get(1).toString().toUpperCase());
                            }
                        }
                        // Populate DNS NAMES, make sure they are lower case
                        for (String dnsName : dnsNames) {
                            if (TldConstants.VALID_TLDS.contains(dnsName)) {
                                Log.i(TAG, "FAILED WILDCARD TldConstants CHECK: " + dnsName);
                                return false;
                            }
                        }
                    }
                } catch (CertificateParsingException ex) {
                    Log.i(TAG, "Cert Parsing Issue: " + ex.getMessage());
                    return false;
                }
            }
        }
        return true;
    }

}
