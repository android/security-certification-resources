package com.android.certifications.niap.permissions.receivers;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ChooserReceiver extends BroadcastReceiver {
    private static final String TAG = "ChooserReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();

        Log.d(TAG, "bundle = null"+bundle+","+intent.getPackage()+""+intent.getType());
        if(bundle == null) {
            Log.d(TAG, "bundle = null");
            return;
        }

        Object componentName = bundle.get(Intent.EXTRA_CHOSEN_COMPONENT);
        if (componentName instanceof ComponentName) {

            Log.d(TAG, ((ComponentName)componentName).getPackageName());

        }
    }
}
