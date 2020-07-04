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

package com.android.certifications.niap.mdfppfcssrvext1;

import android.os.Bundle;

import com.android.certifications.niap.mdfpp_fcs_srv_ext_1tool.R;
import com.android.certifications.niap.mdfppfcssrvext1.tests.AESTestWorker;
import com.android.certifications.niap.mdfppfcssrvext1.tests.ECDSATestWorker;
import com.android.certifications.niap.mdfppfcssrvext1.tests.HMACTestWorker;
import com.android.certifications.niap.mdfppfcssrvext1.tests.RSATestWorker;
import com.android.certifications.niap.mdfppfcssrvext1.tests.SHATestWorker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.android.certifications.niap.mdfppfcssrvext1.tests.PBKDF2Worker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.work.WorkManager;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

/**
 * Sample Tool OEMs can use to generate and use keys, optionally printing the keys to the console
 * to aid the testing lab in verifying the output. In a normal production scenario, you should
 * reveal your keys.
 *
 * Algorithms supported for testing include AES, ECDSA, HMAC, PBKDF2, RSA, and SHA.
 *
 * This tool also has a basic test for BLE.
 */
public class MainActivity extends AppCompatActivity {

    private static final List<Class> TESTS_TO_RUN = new ArrayList<Class>() {{
      add(AESTestWorker.class);
      add(ECDSATestWorker.class);
      add(HMACTestWorker.class);
      add(PBKDF2Worker.class);
      add(RSATestWorker.class);
      add(SHATestWorker.class);
    }};

    private Switch switchCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        switchCompat = findViewById(R.id.rawKeyToggle);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(
                        view,
                        "Running FCS_SRC_EXT_1 Tests... Please check logcat.",
                        Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                TestUtil.printRawKeys = switchCompat.isChecked();
                startTests();
            }
        });
    }

    private void startTests() {
        WorkManager.getInstance(getApplicationContext())
                .beginWith(TestUtil.createWorkRequests(TESTS_TO_RUN))
                .enqueue();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
