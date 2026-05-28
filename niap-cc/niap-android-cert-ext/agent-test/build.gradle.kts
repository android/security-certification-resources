/*
 * Copyright 2026 The Android Open Source Project
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
    kotlin("jvm")
}

kotlin {
    jvmToolchain(21)
}



dependencies {
    // 1. 本体のコンパイル済みクラスを参照
    compileOnly(files("${rootProject.projectDir}/../testbed-core/composeApp/build/classes/kotlin/jvm/main"))
    implementation(project(":common-utils"))
    
    // 2. テスト実行に必要な最小限の依存関係
    implementation("junit:junit:4.13.2")
    implementation("com.malinskiy.adam:adam:0.5.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")

    // Packet analysis
    implementation("io.pkts:pkts-core:3.0.2")
    implementation("com.squareup.okhttp3:mockwebserver:4.12.0")
    implementation("org.bouncycastle:bcpkix-jdk18on:1.77")
}

configurations.all {
    resolutionStrategy {
        force("org.jetbrains.kotlin:kotlin-stdlib:2.2.0")
        force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.2.0")
        force("org.jetbrains.kotlin:kotlin-stdlib-jdk7:2.2.0")
        force("org.jetbrains.kotlin:kotlin-reflect:2.2.0")
    }
}

tasks.jar {
    val pluginName = project.name
    archiveFileName.set("niap-cert-validator-test.jar")

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }) {
        exclude("META-INF/*.SF")
        exclude("META-INF/*.DSA")
        exclude("META-INF/*.RSA")
    }
    destinationDirectory.set(file("${rootProject.projectDir}/../testbed-core/composeApp/plugins/$pluginName"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}

tasks.register("generateTestList") {
    dependsOn("compileKotlin")
    val srcDir = file("src/main/kotlin")
    val listFile = layout.buildDirectory.file("generated/testbed/META-INF/testbed-tests.list")

    inputs.dir(srcDir).withPropertyName("sourceDirectory")
    outputs.file(listFile).withPropertyName("outputListFile")

    doLast {
        val out = listFile.get().asFile
        out.parentFile.mkdirs()

        out.printWriter().use { writer ->
            srcDir.walk().filter { it.isFile && it.extension == "kt" }.forEach { file ->
                val text = file.readText()

                if (text.contains("@Test") && !text.contains("@Ignore")) {
                    val pkg = Regex("package\\s+([a-zA-Z0-9_.]+)").find(text)?.groupValues?.get(1) ?: ""
                    val cls = Regex("class\\s+([a-zA-Z0-9_]+)").find(text)?.groupValues?.get(1) ?: ""
                    if (cls.isNotEmpty()) {
                        writer.println("$pkg.$cls")
                    }
                }
            }
        }
    }
}

tasks.named<Jar>("jar") {
    dependsOn("generateTestList")
    from(layout.buildDirectory.dir("generated/testbed"))
}
