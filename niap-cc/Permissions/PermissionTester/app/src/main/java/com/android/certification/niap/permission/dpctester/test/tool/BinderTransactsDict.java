package com.android.certification.niap.permission.dpctester.test.tool;
/*
 * Copyright (C) 2024 The Android Open Source Project
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
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.android.certification.niap.permission.dpctester.test.log.StaticLogger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BinderTransactsDict {
    static Map<String,String> serviceNameSynonyms = new HashMap<>();

    static Map<String, Map<String,Integer>> indexDb = new HashMap<>();

    Context mContext;
    private static BinderTransactsDict instance = null;
    private BinderTransactsDict(){

    }
    public static BinderTransactsDict getInstance(){
        if(instance == null){
            // block the multiple access from multiple thread
            synchronized (BinderTransactsDict.class) {
                if(instance == null){
                    instance = new BinderTransactsDict();
                }
            }
        }
        return instance;
    }

    private void build(BinderTransactsDict.Builder builder) {
        this.mContext = builder.mContext;
        //Initialize Database According to the Database File
        try {
            AssetManager am = mContext.getResources().getAssets();
            //Change suffix depends on system version

            var SDK_INT = Build.VERSION.SDK_INT;
            if(TesterUtils.isAtLeastBaklava()){
                SDK_INT = 36;
            }

            String filename = String.format(Locale.getDefault(),"binderdb-%d.json",
                    SDK_INT);
            StaticLogger.debug("Binder Transaction filename:"+filename);

            InputStream is = am.open(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String allText = br.lines().collect(Collectors.joining());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(allText);

            JsonNode services_= root.get("services");
            JsonNode methods_ = root.get("methods");

            serviceNameSynonyms = mapper.readValue(services_.toString(),serviceNameSynonyms.getClass());
            indexDb = mapper.readValue(methods_.toString(),indexDb.getClass());
            /*for(String key : serviceNameSynonyms.keySet()){
                String actual = serviceNameSynonyms.get(key);
                if(!actual.equals(key)){
                    StaticLogger.info("public static final String "+key+" = \""+actual+"\";");
                }

            }*/
            //Log.d("tag",indexDb.toString());
            //Log.d("tag",serviceNameSynonyms.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    public static class Builder {
        private final Context mContext; // Mandatory
        //private Builder(){}
        public Builder(Context context) {
            this.mContext = context;
        }
        public void build(){
            BinderTransactsDict.getInstance().build(this);
        }
    }

    public int getTransactId(String descriptor, String methodName){
        return indexDb.get(descriptor).get(methodName);
    }
}
