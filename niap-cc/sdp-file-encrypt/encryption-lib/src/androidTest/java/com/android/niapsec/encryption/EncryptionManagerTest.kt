/*
* Copyright (C) 2026 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.android.niapsec.encryption

import android.app.KeyguardManager
import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.android.niapsec.encryption.api.EncryptionManager
import com.android.niapsec.encryption.api.KeyProviderType
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Assume.assumeTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.nio.charset.StandardCharsets
import java.security.GeneralSecurityException
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class EncryptionManagerTest {

    private lateinit var context: Context
    private lateinit var keyguardManager: KeyguardManager
    private val managersToDestroy = mutableListOf<EncryptionManager>()
    private val filesToClean = mutableListOf<File>()

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
    }

    @After
    fun tearDown() {
        managersToDestroy.forEach { it.destroy() }
        managersToDestroy.clear()

        filesToClean.forEach { if (it.exists()) it.delete() }
        filesToClean.clear()
    }

    private fun createManager(providerType: KeyProviderType, unlockedDeviceRequired: Boolean = false): EncryptionManager {
        val masterKeyUri = "android-keystore://test_key_${UUID.randomUUID()}"
        val manager = EncryptionManager(context, masterKeyUri, providerType, unlockedDeviceRequired)
        managersToDestroy.add(manager)
        return manager
    }

    private fun getTestFile(fileName: String): File {
        val file = File(context.cacheDir, fileName)
        filesToClean.add(file)
        return file
    }

    /**
     * Helper to generate a large string of approximately specific size (in bytes).
     */
    private fun generateLargeString(sizeInBytes: Int): String {
        val sb = StringBuilder(sizeInBytes)
        val chunk = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" // 62 chars

        while (sb.length < sizeInBytes) {
            sb.append(chunk)
        }
        return sb.substring(0, sizeInBytes)
    }

    // --- Stream API Tests (StreamingAead) ---

    @Test
    fun testRawHybridProvider_encryptAndDecryptStream_1MB_works() {
        // Test StreamingAead with ~1MB of data
        val encryptionManager = createManager(KeyProviderType.RAW_HYBRID)
        val testFile = getTestFile("stream_1mb_test.dat")

        // Generate 1MB String (1024 * 1024 bytes)
        val targetSize = 1024 * 1024
        val originalContent = generateLargeString(targetSize)

        // Encrypt using Stream API
        encryptionManager.encryptToFileStream(testFile).use { outputStream ->
            // Convert to bytes explicitly to ensure size match
            outputStream.write(originalContent.toByteArray(StandardCharsets.UTF_8))
        }

        // Verify file was created and has some size (overhead + content)
        assertTrue("File should exist", testFile.exists())
        assertTrue("File should be larger than plain content due to IV/AuthTag", testFile.length() > targetSize)

        // Decrypt using Stream API
        val decryptedContent = encryptionManager.decryptFromFileStream(testFile).use { inputStream ->
            inputStream.reader(StandardCharsets.UTF_8).readText()
        }

        assertEquals("Decrypted content length should match", originalContent.length, decryptedContent.length)
        assertEquals("Decrypted content should match original", originalContent, decryptedContent)
    }

    @Test
    fun testRawHybridProvider_encryptFile_deletesOriginalByDefault() {
        val encryptionManager = createManager(KeyProviderType.RAW_HYBRID)
        val sourceFile = getTestFile("source_to_delete.txt")
        val destFile = getTestFile("dest_encrypted.enc")
        
        val randomText = "Some random text for encryptFile. " + generateLargeString(1000)
        sourceFile.writeText(randomText)
        
        // Use the new encryptFile API (default deleteOriginal = true)
        encryptionManager.encryptFile(sourceFile, destFile)
        
        assertTrue("Destination file should exist", destFile.exists())
        assertTrue("Source file should be deleted", !sourceFile.exists())
        
        val decryptedText = encryptionManager.decryptFromFileStream(destFile).use { it.reader().readText() }
        assertEquals("Decrypted text should match", randomText, decryptedText)
    }

    @Test
    fun testRawHybridProvider_encryptFile_preservesOriginalWhenRequested() {
        val encryptionManager = createManager(KeyProviderType.RAW_HYBRID)
        val sourceFile = getTestFile("source_to_keep.txt")
        val destFile = getTestFile("dest_encrypted_keep.enc")
        
        val randomText = "Some random text that must be kept. " + generateLargeString(1000)
        sourceFile.writeText(randomText)
        
        // Use the new encryptFile API with deleteOriginal = false
        encryptionManager.encryptFile(sourceFile, destFile, deleteOriginal = false)
        
        assertTrue("Destination file should exist", destFile.exists())
        assertTrue("Source file should still exist", sourceFile.exists())
        
        val decryptedText = encryptionManager.decryptFromFileStream(destFile).use { it.reader().readText() }
        assertEquals(randomText, decryptedText)
    }

    @Test
    fun testRawProvider_encryptStream_throwsUnsupportedOperation() {
        // RAW provider (and others not updated) should NOT support streaming
        val encryptionManager = createManager(KeyProviderType.RAW)
        val testFile = getTestFile("raw_stream_fail_test.dat")

        assertThrows(UnsupportedOperationException::class.java) {
            encryptionManager.encryptToFileStream(testFile)
        }
    }

    // --- Legacy File API Tests (Aead / In-Memory) ---

    // --- String Encryption Tests ---

    @Test
    fun testStringEncryption_works_rawHybrid() {
        val encryptionManager = createManager(KeyProviderType.RAW_HYBRID)
        val originalText = "Hello World! String encryption test with RawHybrid."

        val ciphertext = encryptionManager.encryptToString(originalText)
        val decryptedText = encryptionManager.decryptFromString(ciphertext)

        assertEquals(originalText, decryptedText)
    }

    @Test
    fun testRawHybridProvider_encryptUsesSymmetricWhenUnlocked() {
        val encryptionManager = createManager(KeyProviderType.RAW_HYBRID)
        val originalText = "Asserting symmetric magic byte (Solution 1)"
        val ciphertextBase64 = encryptionManager.encryptToString(originalText)
        
        val ciphertextBytes = android.util.Base64.decode(ciphertextBase64, android.util.Base64.DEFAULT)
        
        // Assert first byte is 0x02 (MAGIC_BYTE_SYMMETRIC) after skipping the 4-byte provider header ("EHBR")
        assertEquals("Ciphertext should start with 0x02 representing symmetric encryption (no fallback)", 0x02.toByte(), ciphertextBytes[4])
    }
}