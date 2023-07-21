package com.android.certifications.niap.permissions.activities;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public interface LogListAdaptable {
    void setLogAdapter();
    void addLogLine(String msg);


}
