/*
 * Copyright (C) 2023 The Android Open Source Project
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

import org.gradle.api.*
import com.google.common.base.CaseFormat//Guava
import java.nio.file.Files

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.0")
        classpath("com.google.guava:guava:24.1-jre")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

// Command to call this plugin
// ./gradlew bindServiceCodeGen
class BindServiceCodeGenPlugin:Plugin<Project> {

    val PERMISSIONS = arrayListOf<String>(
        "BIND_CALL_STREAMING_SERVICE",
        "BIND_CREDENTIAL_PROVIDER_SERVICE",
        "BIND_FIELD_CLASSIFICATION_SERVICE",
        "BIND_REMOTE_LOCKSCREEN_VALIDATION_SERVICE",
        "BIND_SATELLITE_GATEWAY_SERVICE",
        "BIND_SATELLITE_SERVICE",
        "BIND_VISUAL_QUERY_DETECTION_SERVICE",
        "BIND_WEARABLE_SENSING_SERVICE"
    )

    override fun apply(project:Project){
        project.task("bindServiceCodeGen"){
            doLast {
                val manifestTags:MutableList<String> = arrayListOf()

                for(pm in PERMISSIONS){
                    val cased_pm = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, pm)
                    val className =  "Test${cased_pm}Service"
                    val outPath = "${project.projectDir}/app/src/main/java/com/android/certifications/niap/permissions/companion/services/"
                    //println(outPath+className+".java")
                    val f = File(outPath+className+".java")
                    if(f.exists()) Files.delete(f.toPath())
                    val generatedCode = """
/*
 * Copyright 2023 The Android Open Source Project
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
package com.android.certifications.niap.permissions.companion.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Exported service used to test the $pm permission.
 *
 * This service requires clients are granted the $pm
 * permission to bind to it. The Permission Test Tool can attempt to bind to this service
 * and invoke the {@link ${className}Impl#testMethod()} method
 * to verify that the platform properly enforces this permission requirement.
 */
public class $className extends Service {
    private static final String TAG = "$className";
    private ${className}Impl bindService;

    @Override
    public void onCreate() {
        super.onCreate();
        bindService = new ${className}Impl();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return bindService;
    }

    static class ${className}Impl extends TestBindService.Stub {
        public void testMethod() {
            Log.d(TAG, "The caller successfully invoked the test method on service "
                    + "${className}Service");
        }
    }
}       """.trim()
                    f.writeText(generatedCode);

                    val tag = """<service android:name=".services.$className"
                         android:permission="android.permission.$pm" 
                         android:enabled="true" android:exported="true" />""".trimIndent()

                    //
                    manifestTags.add(tag)
                }
                println(manifestTags.joinToString(separator = "\n"))
            }
        }

    }
}
apply<BindServiceCodeGenPlugin>()