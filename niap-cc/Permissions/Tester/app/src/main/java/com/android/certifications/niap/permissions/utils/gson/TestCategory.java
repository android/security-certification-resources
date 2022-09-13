package com.android.certifications.niap.permissions.utils.gson;

import java.util.ArrayList;
import java.util.List;

public class TestCategory {
    public String name;
    public List<Test> tests;
    public TestCategory(String name){
        tests = new ArrayList<>();
        this.name = name;
    }
}
