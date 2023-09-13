package com.android.certifications.niap.permissions;

import android.util.Log;

import org.junit.rules.TestName;

public class TestAssertLogger {
    int inc = 0;

    TestName name;
    public TestAssertLogger(TestName name){
        this.name = name;
    }
    public String Msg(String desc){
        inc++;
        String line = name.getMethodName() + "(" + String.format("%03d",inc) +"):"+ desc;
        Log.d("tag",line);

        return line;
    }
}
