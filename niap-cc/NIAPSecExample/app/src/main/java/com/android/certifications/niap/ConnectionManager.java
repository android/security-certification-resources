/*
 * Copyright 2022 The Android Open Source Project
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

package com.android.certifications.niap;

import android.content.res.Resources;
import android.util.Log;

import com.android.certifications.niap.niapsec.net.SecureURL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;


/**
 * Tests TLS and OCSP network connections.
 *
 * Provides helper methods to verify the validity of a connection and provides an example
 * of how to use SecureURL, which adds TLS and OCSP checks automatically to network calls.
 */
public class ConnectionManager {

    public static final String TAG = "ConnectionManager";
    private final String HTTP_GET = "GET";
    private final String CONNECTION_ERROR_MSG = "Encountered an error connecting... ";

    private String domain = "";
    private int port;
    private Resources resources;
    private String keystorePassword;

    public ConnectionManager(String url, int port) {
        this.domain = url;
        this.port = port;
    }

    public ConnectionManager(String url, int port, Resources resources) {
        this(url, port);
        this.resources = resources;
    }

    public ConnectionManager(String url, int port, Resources resources, String keystorePassword) {
        this(url, port);
        this.resources = resources;
        this.keystorePassword = keystorePassword;
    }

    public Map<String, InputStream> loadCertsFromResource(Resources resources,
        Map<String, Integer> resourceIds) {
        Map<String, InputStream> certs = new HashMap<>();
        for (Map.Entry<String, Integer> resourceId : resourceIds.entrySet()) {
            certs.put(resourceId.getKey(), resources.openRawResource(resourceId.getValue()));
        }
        return certs;
    }

    public void printDefaultSLLCiphers(HttpsURLConnection urlConnection) {
        SSLSocketFactory sslSocketFactory = (SSLSocketFactory)
            urlConnection.getSSLSocketFactory();
        Log.i(TAG, "Default Cipher Suites: ");
        for (String defaultCipher : sslSocketFactory.getDefaultCipherSuites()) {
            Log.i(TAG, defaultCipher);
        }
        Log.i(TAG, "Supported Cipher Suites: ");
        for (String cipher : sslSocketFactory.getSupportedCipherSuites()) {
            Log.i(TAG, cipher);
        }
    }

    public boolean checkConnectionValid(String certAlias) {
        boolean valid = false;
        try {
            SecureURL url = new SecureURL(domain, certAlias);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod(HTTP_GET);
            conn.setDoInput(true);
            conn.connect();

            valid = url.isValid(conn);
            Log.i(TAG, "HttpsUrlConnection Cert Revocation Check - Cert valid: " + valid);
            UpdateViewModel.updateStatus.postValue("HttpsUrlConnection - Cert valid: " +
                (valid ? "Passed" : "Failed"));


            int status = conn.getResponseCode();
            Log.i(TAG, "HTTP STATUS: " + status);
            BufferedReader in = null;
            if (status >= 400)
                in = new BufferedReader(
                    new InputStreamReader(conn.getErrorStream()));
            else
                in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            Log.i(TAG, "HTTP GET RESPONSE...\n" + response.toString());

            conn.disconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.e(TAG, CONNECTION_ERROR_MSG + ex.getMessage());
        }
        return valid;
    }

    public boolean checkSSLSocketValid() {
        boolean valid = false;
        try {
            SecureURL url = new SecureURL(domain, "test client alias");
            SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket ssl = (SSLSocket) sf.createSocket(url.getHostname(), port);
            ssl.startHandshake();
            valid = url.isValid(url.getHostname(), ssl);
            Log.i(TAG, "TLS Socket Revocation Certs/Hostname Valid: " + valid);
            UpdateViewModel.updateStatus.postValue("TLS Socket/Hostname Valid: " +
                (valid ? "Passed" : "Failed"));
        } catch (IOException ex) {
            Log.e(TAG, CONNECTION_ERROR_MSG + ex.getMessage());
            ex.printStackTrace();
        }
        return valid;
    }
}
