package com.android.certifications.niap;

import android.app.Application;
import android.util.Log;

import java.security.Security;

import javax.net.ssl.SSLContext;

public class MyApplication extends Application {
    private static String TAG = "MyApplication";
    public static String DISABLED_ALGOR_TAG="jdk.tls.disabledAlgorithms";

    @Override
    public void onCreate() {
        super.onCreate();
        //ProviderInstaller.installIfNeeded(getApplicationContext());
        //SSLContext context = SSLContext.getInstance("TLSv1.2");
        final String ALGOR = "SSLv3, TLSv1, TLSv1.1, RC4, DES, SHA, SHA-1, SHA1, MD5withRSA, DH keySize < 1024, EC keySize < 224, 3DES_EDE_CBC, anon, NULL, include jdk.disabled.namedCurves";
        System.setProperty("https.protocols", "TLS");
        System.setProperty("javax.net.debug", "ssl:handshake");
        Security.setProperty(DISABLED_ALGOR_TAG,ALGOR);
        Log.i(TAG,Security.getProperty(DISABLED_ALGOR_TAG));
    }
}
