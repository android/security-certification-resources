/*
* Copyright (C) 2026 The Android Open Source Project
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
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.android.niapsec.encryption"
    compileSdk = 36

    defaultConfig {
        minSdk = 28
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures.buildConfig = true

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    defaultConfig {
        val dateFormat = SimpleDateFormat("yyMMdd")
        val buildTime = dateFormat.format(Date())
        buildConfigField("String", "BUILD_DATE", "\"${buildTime}\"")
    }
}

configurations.all {
    resolutionStrategy {
        force("com.google.crypto.tink:tink-android:1.20.0")
        force("com.google.crypto.tink:tink-core:1.20.0")
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation("com.google.crypto.tink:tink-android:1.20.0")
    implementation("org.bouncycastle:bcprov-jdk18on:1.77")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
