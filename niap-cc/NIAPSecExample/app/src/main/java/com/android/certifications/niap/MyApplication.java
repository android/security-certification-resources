package com.android.certifications.niap;

import android.app.Application;
import android.util.Log;

import java.security.Security;

public class MyApplication extends Application {
    private static Application app;

    public static Application getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.app = this;
        Log.i("TAG", "?>" +  Security.getProperty("jdk.tls.disabledAlgorithms"));
        Security.setProperty("jdk.tls.disabledAlgorithms","SSLv3, RC4, DES, MD5withRSA," +
                "DH keySize < 1024, EC keySize < 224, 3DES_EDE_CBC, anon, NULL," +
                "include jdk.disabled.namedCurves");
        Log.i("TAG", "?>" +  Security.getProperty("jdk.tls.disabledAlgorithms"));
    }
}