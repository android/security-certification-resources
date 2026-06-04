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

import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.MockResponse
import okio.Buffer
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import java.math.BigInteger
import java.util.Date
import javax.security.auth.x500.X500Principal
import java.security.KeyPairGenerator
import java.security.spec.ECGenParameterSpec

@SFR("FIA_XCU_EXT", "Verify NiapCertManager EST enrollment workflow", category = "network")
class CertManagerTest {

    companion object {
        @BeforeClass
        @JvmStatic
        fun setupCheckList() {
            SFRCheckList.register("FIA_XCU_EXT.2.1", "Verify certificate acquisition via EST")
            SFRCheckList.register("FIA_XCU_EXT.2.2", "Verify mTLS client authentication with enrolled certificate")
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
    fun testEstEnrollmentFlow() {
        log("Starting testEstEnrollmentFlow targeting local Cisco libest native server on port 8085...")
        val resp = runEnrollTest()
        log("Logs: ${resp.workerLogs}")

        // Expect complete workflow success ending in READY state
        errs.checkThat(a.msg("EST enrollment workflow should complete and enter READY state"), resp.workerLogs.contains("READY"), IsEqual(true))
        SFRCheckList.pass("FIA_XCU_EXT.2.1")
    }

    @Test
    fun testEnrollAndVerifyMtls() {
        log("Starting testEnrollAndVerifyMtls: enroll then verify mTLS with enrolled certificate")
        val serial = adb.deviceSerial
        var mtlsLogs = ""

        runBlocking {
            // 1. Install latest APK
            val serviceApk = File(JUnitBridge.resourceDir, "cert-manager-debug.apk")
            val retService = AdamUtils.installApk(client, serial, serviceApk, true)
            Assert.assertTrue("Failed to install service app: $retService", retService.startsWith("Success"))

            // 2. ADB reverse port forwarding
            try {
                Runtime.getRuntime().exec(arrayOf("adb", "-s", serial, "reverse", "tcp:8443", "tcp:8443")).waitFor()
                Runtime.getRuntime().exec(arrayOf("adb", "-s", serial, "reverse", "tcp:8080", "tcp:8080")).waitFor()
                log("ADB reverse configured")
            } catch (e: Exception) {
                log("Warning: adb reverse failed: ${e.message}")
            }

            // 3. Clear logcat and enroll
            client.execute(ShellCommandRequest("logcat -c"), serial)
            val enrollCmd = "am start -a android.intent.action.MAIN " +
                "-n com.example.niap.cert.ext.manager/com.android.niap.cert.service.ManagerActivity " +
                "-e action enroll -e alias test_client_cert " +
                "-e estUrl https://localhost:8443/.well-known/est/ " +
                "-e authToken estuser:estpwd -e subjectDn CN=TestUser"
            client.execute(ShellCommandRequest(enrollCmd), serial)
            Thread.sleep(15000)

            val enrollLogs = String(client.execute(ShellCommandRequest("logcat -d"), serial).stdout)
            val enrolled = enrollLogs.contains("Enrollment succeeded: READY")
            Assert.assertTrue("Enrollment must succeed before mTLS test", enrolled)
            log("Enrollment confirmed: READY")

            // 4. Clear logcat and run mTLS verification
            client.execute(ShellCommandRequest("logcat -c"), serial)
            val mtlsCmd = "am start -a android.intent.action.MAIN --activity-clear-task " +
                "-n com.example.niap.cert.ext.manager/com.android.niap.cert.service.ManagerActivity " +
                "-e action verifyMtls -e alias test_client_cert " +
                "-e protectedUrl https://localhost:8443/protected/"
            client.execute(ShellCommandRequest(mtlsCmd), serial)
            Thread.sleep(10000)

            val logcatResult = client.execute(ShellCommandRequest("logcat -d"), serial)
            mtlsLogs = String(logcatResult.stdout).split("\n")
                .filter { it.contains("ManagerActivity") || it.contains("NiapCertManager") }
                .joinToString("\n")
        }

        log("mTLS logs: $mtlsLogs")
        errs.checkThat(
            a.msg("mTLS should return HTTP 200 with enrolled certificate"),
            mtlsLogs.contains("MTLS_RESULT: HTTP 200"),
            IsEqual(true)
        )
        SFRCheckList.pass("FIA_XCU_EXT.2.2")
    }

    private fun runEnrollTest(): TestResult {
        var workerLogsStr: String = ""
        val serial = adb.deviceSerial

        runBlocking {
            // 1. Install Service APK
            val serviceApk = File(JUnitBridge.resourceDir, "cert-manager-debug.apk")
            val retService = AdamUtils.installApk(client, serial, serviceApk, true)
            Assert.assertTrue("Failed to install service app: $retService", retService.startsWith("Success"))

            // 2. Setup ADB reverse port forwarding for NGINX EST server (8443) and admin HTTP (8080)
            try {
                Runtime.getRuntime().exec(arrayOf("adb", "-s", serial, "reverse", "tcp:8443", "tcp:8443")).waitFor()
                Runtime.getRuntime().exec(arrayOf("adb", "-s", serial, "reverse", "tcp:8080", "tcp:8080")).waitFor()
                log("ADB reverse configured: tcp:8443 (EST/HTTPS), tcp:8080 (admin/HTTP)")
            } catch (e: Exception) {
                log("Warning: Failed to configure adb reverse: ${e.message}")
            }

            // 3. CA cert is fetched by ManagerActivity directly from http://localhost:8080/cacert.pem
            //    (port 8080 is reverse-forwarded above, no push needed)
            log("CA cert will be downloaded on-device from http://localhost:8080/cacert.pem")

            // 4. Clear logcat before test
            client.execute(ShellCommandRequest("logcat -c"), serial)

            // 5. Launch ManagerActivity in automation mode (clear-task ensures a fresh onCreate)
            val cmd = "am start -a android.intent.action.MAIN --activity-clear-task " +
                "-n com.example.niap.cert.ext.manager/com.android.niap.cert.service.ManagerActivity " +
                "-e action enroll " +
                "-e alias test_client_cert " +
                "-e estUrl https://localhost:8443/.well-known/est/ " +
                "-e authToken estuser:estpwd " +
                "-e subjectDn CN=TestUser"
            client.execute(ShellCommandRequest(cmd), serial)

            // 6. Wait for enrollment to complete (service bind up to 10s + CA download + EST)
            Thread.sleep(25000)

            // 7. Read relevant log lines
            val logcatResult = client.execute(ShellCommandRequest("logcat -d"), serial)
            val logcatOutput = String(logcatResult.stdout)
            workerLogsStr = logcatOutput.split("\n")
                .filter {
                    it.contains("ManagerActivity") || it.contains("EstClient") ||
                    it.contains("NiapCertOrchestrator") || it.contains("NiapCertService") ||
                    it.contains("NiapCertManager")
                }
                .joinToString("\n")
        }
        return TestResult(workerLogsStr)
    }

    data class TestResult(val workerLogs: String)
}
