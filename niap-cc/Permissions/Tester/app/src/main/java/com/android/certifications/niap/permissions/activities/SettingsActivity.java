package com.android.certifications.niap.permissions.activities;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.certifications.niap.permissions.R;

public class SettingsActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActionBar actionBar = getSupportActionBar();
        if( actionBar != null )
        {
            actionBar.setDisplayHomeAsUpEnabled( true );
        }
    }
}
