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
package com.android.niapsec.demo

import android.app.admin.DevicePolicyManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.niapsec.demo.R
import com.android.niapsec.demo.ui.theme.FileEncryptionLibTheme
import com.android.niapsec.encryption.api.EncryptionManager
import com.android.niapsec.encryption.api.KeyProviderType
import com.android.niapsec.encryption.tools.SecurityAuditLogger
import kotlinx.coroutines.launch
import java.io.File


class MainActivity : ComponentActivity() {

    companion object {
        private const val PREFS_NAME = "demo_settings"
        private const val KEY_SWEEP_ENABLED = "sweep_enabled"
        private const val KEY_LOG_ENABLED = "log_enabled"
    }

    private val prefs by lazy { getSharedPreferences(PREFS_NAME, MODE_PRIVATE) }
    private val niapSecPrefs by lazy { getSharedPreferences("niap_sec_prefs", MODE_PRIVATE) }
    private val sweepEnabled = mutableStateOf(true)
    private val logEnabled = mutableStateOf(true)
    private val flushIterationsCount = mutableStateOf(64)

    private val hybridFileKeyUri = "android-keystore://hybrid_file_key"
    private val rawFileKeyUri = "android-keystore://raw_file_key"
    private val rawHybridFileKeyUri = "android-keystore://raw_hybrid_file_key"
    private lateinit var testRunner: EncryptionTestRunner

    private lateinit var devicePolicyManager: DevicePolicyManager
    private lateinit var compName: ComponentName

    private val unlockReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_USER_PRESENT) {
                Log.d("MainActivity", "Device unlocked. Triggering auto-sweep...")
                sweepAndRewrap()
            }
        }
    }

    private var _hybridManager: EncryptionManager? = null
    private val hybridManager: EncryptionManager
        get() {
            if (_hybridManager == null) {
                _hybridManager = EncryptionManager(this, hybridFileKeyUri, providerType = KeyProviderType.HYBRID, unlockedDeviceRequired = true)
            }
            return _hybridManager!!
        }

    private var _rawManager: EncryptionManager? = null
    private val rawManager: EncryptionManager
        get() {
            if (_rawManager == null) {
                _rawManager = EncryptionManager(this, rawFileKeyUri, providerType = KeyProviderType.RAW, unlockedDeviceRequired = true)
            }
            return _rawManager!!
        }

    private var _rawHybridManager: EncryptionManager? = null
    private val rawHybridManager: EncryptionManager
        get() {
            if (_rawHybridManager == null) {
                _rawHybridManager = EncryptionManager(this, rawHybridFileKeyUri, providerType = KeyProviderType.RAW_HYBRID, unlockedDeviceRequired = true)
            }
            return _rawHybridManager!!
        }

    private val testResults = mutableStateOf<List<TestResult>>(emptyList())
    private val fileStatusResults = mutableStateOf<List<TestResult>>(emptyList())

    private val requestAdminLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode != RESULT_OK) {
            Log.e("DeviceAdmin", "Failed to get device admin permission.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        testRunner = EncryptionTestRunner(this)
        devicePolicyManager = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
        compName = ComponentName(this, DeviceAdminReceiver::class.java)

        sweepEnabled.value = prefs.getBoolean(KEY_SWEEP_ENABLED, true)
        logEnabled.value = prefs.getBoolean(KEY_LOG_ENABLED, true)
        flushIterationsCount.value = niapSecPrefs.getInt("keystore_flush_iterations", 64)
        SecurityAuditLogger.isAuditLogEnabled = logEnabled.value

        registerReceiver(unlockReceiver, IntentFilter(Intent.ACTION_USER_PRESENT))
        sweepAndRewrap()

        setContent {
            FileEncryptionLibTheme {
                TestScreen()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sweepAndRewrap()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(unlockReceiver)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun TestScreen() {
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        val showDeleteConfirmation = remember { mutableStateOf(false) }
        var expanded by remember { mutableStateOf(false) }
        var sweepChecked by sweepEnabled
        var loggingChecked by logEnabled
        val showFlushIterationsDialog = remember { mutableStateOf(false) }
        var flushIterationsInput by remember(flushIterationsCount.value) { mutableStateOf(flushIterationsCount.value.toString()) }

        if (showDeleteConfirmation.value) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmation.value = false },
                title = { Text("Delete All Keys & Data?") },
                text = { Text("This will permanently destroy all cryptographic keys and encrypted files. This action cannot be undone.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteConfirmation.value = false
                            clearAll()
                            scope.launch {
                                snackbarHostState.showSnackbar("All keys and data destroyed")
                            }
                        }
                    ) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteConfirmation.value = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        if (showFlushIterationsDialog.value) {
            AlertDialog(
                onDismissRequest = { showFlushIterationsDialog.value = false },
                title = { Text("Set IPC Flush Iterations") },
                text = {
                    Column {
                        Text("Configure how many times the Keystore IPC dummy flush request operation is repeated.")
                        Spacer(modifier = Modifier.height(8.dp))
                        androidx.compose.material3.OutlinedTextField(
                            value = flushIterationsInput,
                            onValueChange = { flushIterationsInput = it },
                            label = { Text("Iterations") },
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number)
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        val newCount = flushIterationsInput.toIntOrNull() ?: 64
                        flushIterationsCount.value = newCount
                        niapSecPrefs.edit().putInt("keystore_flush_iterations", newCount).apply()
                        showFlushIterationsDialog.value = false
                    }) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showFlushIterationsDialog.value = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.VpnKey, contentDescription = "Encryption Key")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("File Encryption Demo")
                        }
                    },
                    actions = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More",
                            )
                            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                DropdownMenuItem(text = { Text(
                                    stringResource(R.string.enable_sweep)
                                ) }, leadingIcon = {
                                    Checkbox(checked = sweepChecked, onCheckedChange = { toggleSweep(!sweepChecked) })
                                }, onClick = {
                                    toggleSweep(!sweepChecked)
                                })
                                DropdownMenuItem(text = { Text(
                                    stringResource(R.string.enable_logging)
                                )}, leadingIcon = {
                                    Checkbox(checked = loggingChecked, onCheckedChange = { toggleLog(!loggingChecked) })
                                },onClick = {
                                    toggleLog(!loggingChecked)
                                })
                                DropdownMenuItem(text = { Text("Set Flush Iterations") }, onClick = {
                                    showFlushIterationsDialog.value = true
                                    expanded = false
                                })
                            }
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
                ) {
                    item {
                        /* *** Temporarily removed for readability. *** LLM must not modify here ***
                        ProviderTestGroup(
                            title = "Tink HYBRID",
                            onTestClick = { runHybridFileTest() },
                            onLockAndTestClick = { lockAndTest(KeyProviderType.HYBRID) }
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        ProviderTestGroup(
                            title = "JCA RAW",
                            onTestClick = { runRawFileTest() },
                            onLockAndTestClick = { lockAndTest(KeyProviderType.RAW) }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        */
                        ProviderTestGroup(
                            title = "JCA RAW HYBRID",
                            onTestClick = { runRawHybridFileTest() },
                            onLockAndTestClick = { lockAndTest(KeyProviderType.RAW_HYBRID) }
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Button(onClick = { checkFileStatus() }) { Text("Check Status") }
                            Button(onClick = { encryptFileManual() }) { Text("Encrypt File") }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { showDeleteConfirmation.value = true },
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) { 
                            Text("Clear All Keys & Data") 
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(modifier = Modifier.fillMaxWidth())
                    }

                    if (testResults.value.isNotEmpty()) {
                        item {
                            Text(
                                "Test Results", 
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(testResults.value) { result ->
                            TestResultRow(result, isFile = false)
                        }
                        item { HorizontalDivider(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)) }
                    }

                    if (fileStatusResults.value.isNotEmpty()) {
                        item {
                            Text(
                                "File Storage Status", 
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(fileStatusResults.value) { result ->
                            TestResultRow(result, isFile = true) {
                                result.file?.let { file ->
                                    scope.launch {
                                        try {
                                            val header = readHeader(file)
                                            val manager = when (header) {
                                                "ERAW" -> rawManager
                                                "EHBT" -> hybridManager
                                                "EHBR" -> rawHybridManager
                                                else -> rawHybridManager
                                            }
                                            val plaintext = manager.decryptFromFile(file).use { it.reader().readText() }
                                            snackbarHostState.showSnackbar("Decrypted: $plaintext")
                                        } catch (e: Exception) {
                                            snackbarHostState.showSnackbar("Decryption failed: ${e.message}")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun readHeader(file: File): String {
        return try {
            val input = file.inputStream()
            val headerBytes = ByteArray(4)
            val read = input.read(headerBytes)
            input.close()
            if (read == 4) String(headerBytes) else ""
        } catch (e: Exception) { "" }
    }

    private fun sweepAndRewrap() {
        if (!sweepEnabled.value) return
        try {
            rawHybridManager.sweepAndRewrapPendingFiles()
            checkFileStatus()
        } catch (e: Exception) {
            Log.e("MainActivity", "Sweep failed", e)
        }
    }

    private fun encryptFileManual() {
        val file = File(filesDir, "Manual-Encrypt-test_file.enc")
        try {
            val text = "This is a manual encrypted message."
            rawHybridManager.encryptToFile(file).use { it.write(text.toByteArray()) }
            checkFileStatus()
        } catch (e: Exception) {
            Log.e("MainActivity", "Manual encryption failed", e)
        }
    }

    private fun toggleSweep(enabled: Boolean) {
        sweepEnabled.value = enabled
        prefs.edit().putBoolean(KEY_SWEEP_ENABLED, enabled).apply()
    }

    private fun toggleLog(enabled: Boolean) {
        logEnabled.value = enabled
        SecurityAuditLogger.isAuditLogEnabled = enabled
        prefs.edit().putBoolean(KEY_LOG_ENABLED, enabled).apply()
    }

    private fun readHeaderAndMagic(file: File): Pair<String, Int> {
        return try {
            file.inputStream().use { input ->
                val buffer = ByteArray(5)
                val read = input.read(buffer)
                if (read >= 4) {
                    val header = String(buffer.sliceArray(0..3))
                    val magic = if (read == 5) buffer[4].toInt() else 0
                    Pair(header, magic)
                } else Pair("", 0)
            }
        } catch (e: Exception) { Pair("", 0) }
    }
    private fun checkFileStatus() {
        val results = mutableListOf<TestResult>()
        val files = filesDir.listFiles()?.filter { it.name.endsWith(".enc") } ?: emptyList()

        files.forEach { file ->
            val (header, magic) = readHeaderAndMagic(file) // Completely removed readBytes()
            val status = when (header) {
                "EHBT" -> "Tink Hybrid"
                "ERAW" -> "JCA Raw"
                "EHBR" -> when (magic) {
                    0x01 -> "JCA RawHybrid (Asymmetric 0x01)"
                    0x02 -> "JCA RawHybrid (Symmetric 0x02)"
                    else -> "JCA RawHybrid (Unknown)"
                }
                else -> "Unknown Header: $header"
            }
            results.add(TestResult(file.name, true, status, file))
        }
        fileStatusResults.value = results
    }

    @Composable
    private fun TestResultRow(result: TestResult, isFile: Boolean, onClick: () -> Unit = {}) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable(enabled = isFile) { onClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icon: ImageVector = if (isFile) {
                when {
                    result.message.contains("0x01") -> Icons.Default.LockOpen
                    result.message.contains("0x02") -> Icons.Default.Lock
                    else -> Icons.Default.Description
                }
            } else {
                if (result.passed) Icons.Default.CheckCircle else Icons.Default.Warning
            }

            val iconColor = if (isFile) {
                if (result.message.contains("0x02")) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary
            } else {
                if (result.passed) Color.Green else Color.Red
            }

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = result.testName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (result.passed || isFile) Color.Unspecified else Color.Red
                )
                Text(
                    text = result.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (result.passed || isFile) Color.Unspecified else Color.Red
                )
                if (isFile) {
                    Text(
                        text = "Tap to verify decryption",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }

    @Composable
    private fun ProviderTestGroup(title: String, onTestClick: () -> Unit, onLockAndTestClick: () -> Unit) {
        var isProcessing by remember { mutableStateOf(false) }
        
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = {
                        if (!isProcessing) {
                            isProcessing = true
                            onTestClick()
                            Handler(Looper.getMainLooper()).postDelayed({ isProcessing = false }, 1000)
                        }
                    },
                    enabled = !isProcessing
                ) { Text("Test File") }
                Button(
                    onClick = {
                        if (!isProcessing) {
                            isProcessing = true
                            onLockAndTestClick()
                            Handler(Looper.getMainLooper()).postDelayed({ isProcessing = false }, 1000)
                        }
                    },
                    enabled = !isProcessing
                ) { Text("Lock & Test") }
            }
        }
    }

    private fun runHybridFileTest() {
        testResults.value = testRunner.runFullTest(hybridManager, "TinkHybridFileTest")
    }

    private fun runRawFileTest() {
        testResults.value = testRunner.runFullTest(rawManager, "JcaRawFileTest")
    }

    private fun runRawHybridFileTest() {
        testResults.value = testRunner.runFullTest(rawHybridManager, "JcaRawHybridFileTest")
    }

    private fun lockAndTest(providerType: KeyProviderType) {
        if (!devicePolicyManager.isAdminActive(compName)) {
            requestDeviceAdmin()
            return
        }

        val manager = when (providerType) {
            KeyProviderType.HYBRID -> hybridManager
            KeyProviderType.RAW -> rawManager
            KeyProviderType.RAW_HYBRID -> rawHybridManager
            else -> throw IllegalArgumentException("Unsupported provider type: $providerType")
        }

        val keyguardManager = getSystemService(KEYGUARD_SERVICE) as android.app.KeyguardManager
        Log.d("LockAndTest", "Locking screen to test $providerType provider...")
        devicePolicyManager.lockNow()

        val handler = Handler(Looper.getMainLooper())
        val startTime = System.currentTimeMillis()
        val timeoutMs = 5000L

        val checkStateRunnable = object : Runnable {
            override fun run() {
                val isLocked = keyguardManager.isDeviceLocked
                val elapsed = System.currentTimeMillis() - startTime
                
                if (isLocked) {
                    Log.d("LockAndTest", "Device is LOCKED after ${elapsed}ms. Proceeding with test.")
                    val shouldFail = true
                    testResults.value = testRunner.runFullTest(manager, "${providerType}AfterLock", reverseDecryptionResult = shouldFail)
                } else if (elapsed < timeoutMs) {
                    handler.postDelayed(this, 200)
                } else {
                    Log.e("LockAndTest", "Timeout waiting for device to lock. Proceeding anyway.")
                    val shouldFail = true
                    testResults.value = testRunner.runFullTest(manager, "${providerType}AfterLock", reverseDecryptionResult = shouldFail)
                }
            }
        }
        handler.postDelayed(checkStateRunnable, 200)
    }

    private fun requestDeviceAdmin() {
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
            putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName)
            putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "This app needs permission to lock the screen for testing.")
        }
        requestAdminLauncher.launch(intent)
    }

    private fun clearAll() {
        hybridManager.destroy()
        _hybridManager = null
        rawManager.destroy()
        _rawManager = null
        rawHybridManager.destroy()
        _rawHybridManager = null
        testResults.value = emptyList()
        fileStatusResults.value = emptyList()
        Log.d("ClearData", "All keys and data have been destroyed.")
    }
}
