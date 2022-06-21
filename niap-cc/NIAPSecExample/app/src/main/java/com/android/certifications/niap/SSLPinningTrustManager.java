package com.android.certifications.niap;

import android.content.Context;

import android.util.Log;
import androidx.annotation.RawRes;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class SSLPinningTrustManager implements X509TrustManager {

    private boolean sslPinningEnabled = true;
    private String publicKey;

    SSLPinningTrustManager(Context context, @RawRes int certificateResource, boolean sslPinningEnabled) {
        this.sslPinningEnabled = sslPinningEnabled;
        loadCRTFile(context.getResources().openRawResource(certificateResource));
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        if (!this.sslPinningEnabled) {
            Log.e("SslPinningTrustManager", "SSL pinning is disabled");
        } else if (chain == null) {
            throw new IllegalArgumentException("checkServerTrusted: X509Certificate array is null");
        } else if (chain.length <= 0) {
            throw new IllegalArgumentException("checkServerTrusted: X509Certificate is empty");
        } else if (authType == null || !authType.contains("RSA")) {
            throw new CertificateException("checkServerTrusted: AuthType is not RSA");
        } else {
            boolean expected = false;
            for (X509Certificate chainCertificate : chain) {
                String encoded = new BigInteger(1, chainCertificate.getPublicKey().getEncoded()).toString(16);
                expected = expected || this.publicKey.equalsIgnoreCase(encoded);
            }
            if (!expected) {
                throw new CertificateException("checkServerTrusted: Expected public key: " + this.publicKey);
            }
        }
    }

    private void loadCRTFile(InputStream certificate) {
        try {
            Certificate ca = CertificateFactory.getInstance("X.509").generateCertificate(certificate);
            this.publicKey = new BigInteger(1, ca.getPublicKey().getEncoded()).toString(16);
            certificate.close();
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkClientTrusted(X509Certificate[] xcs, String string) {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

}
