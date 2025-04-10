package com.android.certification.niap.permission.dpctester.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.android.certification.niap.permission.dpctester.authenticator.MyAuthenticator;

public class AuthenticationService extends Service {

    private MyAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        super.onCreate();
        mAuthenticator =new MyAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}