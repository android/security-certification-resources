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

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.android.niapsec.encryption.api.EncryptionManager
import com.android.niapsec.encryption.api.KeyProviderType
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class EncryptionProviderTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val filesToClean = mutableListOf<File>()

    @Before
    fun setup() {
        // Ensure we start from a clean state before each test
        clearAllData()
    }

    @After
    fun teardown() {
        // Clean up all generated keys and files after each test
        clearAllData()
        filesToClean.forEach { it.delete() }
        filesToClean.clear()
    }

    @Test
    fun testHybridProvider_EncryptDecrypt_Succeeds() {
        val manager = createManager(KeyProviderType.HYBRID)
        val testFile = createTestFile("hybrid_test.txt")
        val originalText = "This is a hybrid message."

        // Encrypt
        manager.encryptToFile(testFile).use { it.write(originalText.toByteArray()) }

        // Decrypt
        val decryptedText = manager.decryptFromFile(testFile).use { it.reader().readText() }
        assertEquals("Decrypted content should match original", originalText, decryptedText)
    }

    @Test
    fun testRawProvider_EncryptDecrypt_Succeeds() {
        val manager = createManager(KeyProviderType.RAW)
        val testFile = createTestFile("raw_test.txt")
        val originalText = "This is a raw message."

        // Encrypt
        manager.encryptToFile(testFile).use { it.write(originalText.toByteArray()) }

        // Decrypt
        val decryptedText = manager.decryptFromFile(testFile).use { it.reader().readText() }
        assertEquals("Decrypted content should match original", originalText, decryptedText)
    }

    @Test
    fun testHybridProvider_EncryptDecrypt_TwoFiles_Succeeds() {
        val manager = createManager(KeyProviderType.HYBRID)

        val testFile1 = createTestFile("hybrid_test_1.txt")
        val originalText1 = "This is the first hybrid message."
        manager.encryptToFile(testFile1).use { it.write(originalText1.toByteArray()) }

        val testFile2 = createTestFile("hybrid_test_2.txt")
        val originalText2 = "This is the second hybrid message."
        manager.encryptToFile(testFile2).use { it.write(originalText2.toByteArray()) }

        val decryptedText1 = manager.decryptFromFile(testFile1).use { it.reader().readText() }
        assertEquals("Decrypted content for file 1 should match original", originalText1, decryptedText1)

        val decryptedText2 = manager.decryptFromFile(testFile2).use { it.reader().readText() }
        assertEquals("Decrypted content for file 2 should match original", originalText2, decryptedText2)
    }

    @Test
    fun testRawProvider_EncryptDecrypt_TwoFiles_Succeeds() {
        val manager = createManager(KeyProviderType.RAW)

        val testFile1 = createTestFile("raw_test_1.txt")
        val originalText1 = "This is the first raw message."
        manager.encryptToFile(testFile1).use { it.write(originalText1.toByteArray()) }

        val testFile2 = createTestFile("raw_test_2.txt")
        val originalText2 = "This is the second raw message."
        manager.encryptToFile(testFile2).use { it.write(originalText2.toByteArray()) }

        val decryptedText1 = manager.decryptFromFile(testFile1).use { it.reader().readText() }
        assertEquals("Decrypted content for file 1 should match original", originalText1, decryptedText1)

        val decryptedText2 = manager.decryptFromFile(testFile2).use { it.reader().readText() }
        assertEquals("Decrypted content for file 2 should match original", originalText2, decryptedText2)
    }

    private fun createManager(providerType: KeyProviderType): EncryptionManager {
        val keyUri = "android-keystore://test_key_for_${providerType.name}"
        return EncryptionManager(
            context = context,
            masterKeyUri = keyUri,
            providerType = providerType,
            unlockedDeviceRequired = false // Use false for automated tests
        )
    }

    private fun createTestFile(filename: String): File {
        val file = File(context.filesDir, filename)
        filesToClean.add(file)
        return file
    }

    private fun clearAllData() {
        // A simple way to clear all provider keys and files
        for (providerType in KeyProviderType.values()) {
            try {
                val manager = createManager(providerType)
                manager.destroy()
            } catch (e: Exception) {
                // Ignore errors during cleanup
            }
        }
    }
}
