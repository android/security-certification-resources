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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.UserManager
import android.util.Log
import com.android.niapsec.encryption.api.EncryptionManager
import com.android.niapsec.encryption.api.KeyProviderType

/**
 * Diagnostic Receiver for validating Direct Boot and Lock State behavior.
 * This component mimics a background service attempting to access cryptographic keys
 * during the "Before First Unlock" (BFU) or "After First Unlock" (AFU) states.
 */
class DirectBootTestReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_TEST_CRYPTO = "com.android.niapsec.ACTION_TEST_CRYPTO"
        private const val TAG = "DirectBootTest"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_TEST_CRYPTO) {
            Log.i(TAG, "Received test broadcast. Starting Direct Boot verification...")

            // 1. Check System Environment
            val userManager = context.getSystemService(Context.USER_SERVICE) as UserManager
            val isLocked = !userManager.isUserUnlocked
            val isDeviceProtected = context.isDeviceProtectedStorage

            Log.d(TAG, "--- Environment Check ---")
            Log.d(TAG, "User State: ${if (isLocked) "LOCKED (BFU State)" else "UNLOCKED (AFU State)"}")
            Log.d(TAG, "Storage Context: ${if (isDeviceProtected) "Device Protected (DE)" else "Credential Encrypted (CE)"}")

            // 2. Test RawHybridKeyProvider (JCA Implementation)
            runTest(context, KeyProviderType.RAW_HYBRID, "RawHybrid_JCA", isLocked)

            // 3. Test HybridKeyProvider (Tink Implementation)
            runTest(context, KeyProviderType.HYBRID, "Hybrid_Tink", isLocked)
        }
    }

    /**
     * Executes the encryption/decryption cycle using EncryptionManager.
     * * @param context The application context (automatically handled by EncryptionManager)
     * @param type The provider type to test (RAW_HYBRID or HYBRID)
     * @param label A label for logging purposes
     * @param isLocked Current lock state of the device
     */
    private fun runTest(context: Context, type: KeyProviderType, label: String, isLocked: Boolean) {
        Log.d(TAG, "--- Starting Test Sequence: $label ---")
        try {
            // Initialize EncryptionManager with a unique test alias.
            // IMPORTANT: We set 'unlockedDeviceRequired = true' to enforce strict authentication binding (FIA_UAU_EXT.1).
            val manager = EncryptionManager(
                context = context,
                masterKeyUri = "android-keystore://diagnostic_test_${label.lowercase()}",
                providerType = type,
                unlockedDeviceRequired = true
            )

            val testPayload = "Confidential Data Verification for $label - ${System.currentTimeMillis()}"

            // Step A: Encryption Verification (FDP_DAR_EXT.2)
            // Requirement: Must SUCCEED even if the device is LOCKED.
            // Mechanism: Uses the Public Key stored in Device Protected (DE) storage.
            Log.d(TAG, "[$label] Attempting Encryption...")
            val ciphertext = manager.encryptToString(testPayload)
            Log.i(TAG, "[$label] SUCCESS: Encryption completed. Ciphertext length: ${ciphertext.length}")

            // Step B: Decryption Verification (FIA_UAU_EXT.1)
            // Requirement: Must FAIL if LOCKED. Must SUCCEED if UNLOCKED.
            // Mechanism: Access to the Private Key in Android Keystore is gated by user authentication.
            Log.d(TAG, "[$label] Attempting Decryption...")
            try {
                val plaintext = manager.decryptFromString(ciphertext)

                if (isLocked) {
                    // SECURITY VIOLATION: We accessed the key without unlocking!
                    Log.e(TAG, "[$label] FAILURE: Decryption succeeded but user is LOCKED! This violates FIA_UAU_EXT.1.")
                } else {
                    // Normal Operation
                    if (plaintext == testPayload) {
                        Log.i(TAG, "[$label] SUCCESS: Decryption succeeded and data matches.")
                    } else {
                        Log.e(TAG, "[$label] FAILURE: Decryption succeeded but data mismatch (Integrity Error).")
                    }
                }
            } catch (e: Exception) {
                if (isLocked) {
                    // EXPECTED BEHAVIOR: Access denied during lock
                    Log.i(TAG, "[$label] SUCCESS: Decryption failed as expected during lock. Exception: ${e.javaClass.simpleName}")
                } else {
                    // UNEXPECTED ERROR: Should have worked
                    Log.e(TAG, "[$label] FAILURE: Decryption failed but user is UNLOCKED. Error: ${e.message}")
                    e.printStackTrace()
                }
            }

            // Clean up test keys
            // manager.destroy() // Optional: Keep keys to test across reboots if needed
            Log.d(TAG, "--- End Test Sequence: $label ---")

        } catch (e: Exception) {
            Log.e(TAG, "[$label] CRITICAL ERROR during initialization or encryption: ${e.message}", e)
        }
    }
}