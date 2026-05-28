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

package org.example.plugin.fiax509

import org.example.plugin.utils.ADSRPTestWatcher
import org.example.plugin.utils.TestAssertLogger
import org.example.plugin.utils.SFR
import org.example.plugin.utils.SFRCheckList
import org.example.plugin.utils.*
import org.example.project.JUnitBridge
import com.malinskiy.adam.request.shell.v1.ShellCommandRequest
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ErrorCollector
import org.junit.rules.TestName
import org.junit.rules.TestWatcher
import org.example.project.adb.rules.AdbDeviceRule
import com.malinskiy.adam.AndroidDebugBridgeClient
import java.io.File
import org.hamcrest.core.IsEqual

@SFR("FIA_X509_EXT", "Verify NiapCertValidator functionality", category = "network")
class NiapValidatorTest {

    companion object {
        @BeforeClass
        @JvmStatic
        fun setupCheckList() {
            SFRCheckList.register("FIA_X509_EXT.1.1", "Verify algorithm constraints (SHA-384)")
        }
    }

    @get:Rule
    val adb = AdbDeviceRule()
    private val client: AndroidDebugBridgeClient by lazy { adb.adb }

    @get:Rule
    val watcher: TestWatcher by lazy { ADSRPTestWatcher(adb) }
    @get:Rule
    val testname: TestName = TestName()

    private val a: TestAssertLogger by lazy { TestAssertLogger(testname) }
    @get:Rule
    val errs: ErrorCollector = ErrorCollector()

    @Test
    fun testGoogleCertRejected() {
        log("Starting testGoogleCertRejected...")
        val hostName = "https://www.google.com"
        val resp = runTest(hostName)
        
        log("Logs: ${resp.workerLogs}")

        // We expect failure because Google uses SHA-256
        errs.checkThat(a.msg("Connection should fail for Google cert due to SHA-256"), resp.workerLogs.contains("CertificateException: Signature algorithm must use SHA-384"), IsEqual(true))
        
        SFRCheckList.pass("FIA_X509_EXT.1.1")
    }

    @Test
    fun testGoogleCertRejectedOkHttp() {
        log("Starting testGoogleCertRejectedOkHttp...")
        val hostName = "https://www.google.com"
        val resp = runTest(hostName, "okhttp3")
        
        log("Logs: ${resp.workerLogs}")

        // We expect failure because Google uses SHA-256
        errs.checkThat(a.msg("Connection should fail for Google cert due to SHA-256 (OkHttp)"), resp.workerLogs.contains("CertificateException: Signature algorithm must use SHA-384"), IsEqual(true))
        
        SFRCheckList.pass("FIA_X509_EXT.1.1")
    }

    @Test
    fun testGoogleCertAcceptedInRelaxedMode() {
        log("Starting testGoogleCertAcceptedInRelaxedMode...")
        val hostName = "https://www.google.com"
        val resp = runTest(hostName, "http", "relaxed")
        
        log("Logs: ${resp.workerLogs}")

        // We expect success because we disabled strict algorithm checks in relaxed flavor
        errs.checkThat(a.msg("Connection should succeed in relaxed mode"), resp.workerLogs.contains("Response Code: 200"), IsEqual(true))
    }

    private fun runTest(testurl: String, type: String = "http", flavor: String = "strict"): TestResult {
        var workerLogsStr: String = ""
        val serial = adb.deviceSerial

        runBlocking {
            val apkName = "validator-test-app-$flavor-debug.apk"
            val appApk = File(JUnitBridge.resourceDir, apkName)
            val ret = AdamUtils.installApk(client, serial, appApk, true)
            Assert.assertTrue("Failed to install test app: ${ret}", ret.startsWith("Success"))

            client.execute(ShellCommandRequest("am force-stop com.example.niap.cert.ext.testapp"), serial)
            Thread.sleep(2000)
            
            val cmd = "am start -a android.intent.action.MAIN -n com.example.niap.cert.ext.testapp/.MainActivity -e openurl '$testurl' -e type $type"
            client.execute(ShellCommandRequest(cmd), serial)

            // Wait for logs
            Thread.sleep(5000) // Simple wait instead of waitLogcatLine for robustness in this scratch test
            
            // Read logs to find the result
            val logcatResult = client.execute(ShellCommandRequest("su 0 logcat -d"), serial)
            val logcatOutput = String(logcatResult.stdout)
            workerLogsStr = logcatOutput.split("\n").filter { it.contains("NetworkWorker") }.joinToString("\n")
        }
        return TestResult(workerLogsStr)
    }

    data class TestResult(val workerLogs: String)
}
