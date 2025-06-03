import org.jetbrains.kotlin.config.JvmTarget
/*
 * Copyright 2024 The Android Open Source Project
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
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}
val homePath = System.getenv("user.home")
android {
    signingConfigs {
        // TODO :
        // Prepare your own normal.jks and platform.jks as settings and put these into
        // assigned path. It helps to build the application variants automatically.
        // For details see the instructions on the SIGNING.md
        create("norm"){
            storeFile = File(rootProject.projectDir,"./security/normal/normal.jks")
            storePassword = "android"
            keyPassword = "android"
            keyAlias = "normal"
        }
        create("platform") {
            storeFile = File(rootProject.projectDir,"./security/platform/platform.jks")
            storePassword = "android"
            keyPassword = "android"
            keyAlias = "platform"
        }
        create("debugkeystore") {
            storeFile = File(homePath,"./.android/debug.keystore")
            storePassword = "android"
            keyPassword = "androiddebugkey"
            keyAlias = "android"
        }
    }
    val applicationName = "Tester"
    val publish = project.tasks.create("publishAll")
    testVariants.all {
        val variant = this

        if(variant.buildType.name.equals("debug")){
            val task = project.tasks.create("publish${variant.name.replaceFirstChar{it.uppercase()}}Test", Copy::class)
            mkdir("$rootDir/package")
            variant.outputs.forEach { item ->
                task.from(item.outputFile.absolutePath)
            }
            task.into("$rootDir/package")
            task.dependsOn(variant.assembleProvider)
            publish.dependsOn(task)
        }
    }
    applicationVariants.all {
        val variant = this
        variant.outputs
            .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
            .forEach { output ->
                val flavor = variant.productFlavors[0].name
                val outputFileName = "${applicationName}-${flavor}-${variant.buildType.name}.apk"
                println("OutputFileName: $outputFileName")
                output.outputFileName = outputFileName
            }

        if(variant.buildType.name.equals("debug")){
            val task = project.tasks.create("publish${variant.name.replaceFirstChar{it.uppercase()}}Apk", Copy::class)
            mkdir("$rootDir/package")
            variant.outputs.forEach { it->
                task.from(it.outputFile.absolutePath)
            }
            task.into("$rootDir/package")
            task.dependsOn(variant.assembleProvider)
            publish.dependsOn(task)
        }
    }


    buildTypes {
        release {
            //set null and later set it with productFlavors
            signingConfig = null
            isDebuggable = false
            isMinifyEnabled = false
            //proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
        debug {
            signingConfig = null
            isDebuggable = true
            isMinifyEnabled = false
        }
    }
    flavorDimensions.add("settings")
    productFlavors {
        create("normal"){
            dimension = "settings"
            signingConfig = signingConfigs.getByName("norm")
        }
        create("noperm"){
            dimension = "settings"
            signingConfig = signingConfigs.getByName("norm")
        }
        create("specperm"){
            dimension = "settings"
            signingConfig = signingConfigs.getByName("platform")
        }
        create("platform"){
            dimension = "settings"
            signingConfig = signingConfigs.getByName("platform")
        }
        create("dpc-normal"){
            dimension = "settings"
            signingConfig = signingConfigs.getByName("platform")
        }
        create("dpc-noperm"){
            dimension = "settings"
            signingConfig = signingConfigs.getByName("platform")
        }
        create("coretest-platform"){
            dimension = "settings"
            signingConfig = signingConfigs.getByName("platform")
        }
        create("coretest-normal"){
            dimension = "settings"
            signingConfig = signingConfigs.getByName("norm")
        }
        create("coretest-noperm"){
            dimension = "settings"
            signingConfig = signingConfigs.getByName("norm")
        }
    }


    namespace = "com.android.certification.niap.permission.dpctester"
    buildFeatures {
        aidl = true
    }
    //compileSdkPreview = "Baklava"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.android.certification.niap.permission.dpctester"
        minSdk = 28
        targetSdk = 36
        //targetSdkPreview = "Baklava"
        versionCode = 1
        versionName = "1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

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
    buildFeatures {
        viewBinding = true
    }
    sourceSets {
        getByName("main") {
            aidl {
                srcDirs("src/main/aidl")
            }
            assets {
                srcDirs("src/main/assets")
            }
        }
    }
}

dependencies {
    // Java language implementation
    implementation(libs.androidx.preference)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.runner)
    implementation(libs.androidx.credentials)
    implementation(libs.robolectric)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.play.services.location)
    implementation(libs.play.services.vision)
    implementation(libs.play.services.base)
    implementation(libs.androidx.preference)
    implementation(libs.guava)
    implementation(libs.material)

    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.work.runtime)

    implementation(libs.androidx.fragment)
    implementation(libs.androidx.fragment.ktx)

    implementation(libs.jackson.core)
    implementation(libs.jackson.databind)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.core)
    androidTestImplementation(libs.androidx.runner.v140)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.rules)
    androidTestImplementation(libs.androidx.uiautomator)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}