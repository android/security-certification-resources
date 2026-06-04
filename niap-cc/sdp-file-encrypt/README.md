# Android SDP File Encryption Demo

## 1. Project Overview

This project provides an Android file encryption core library (`encryption-lib`) alongside a demonstration application (`locked-device-demo`). It is designed to demonstrate and compare different file encryption strategies, focusing on compliance with the **NIAP Mobile Device Fundamentals Protection Profile (MDF PP) Version 3.3**. Note that while the project is structured as a generic library, implementations of manual or unsafe patterns (such as `RawKeyProvider`) are kept for demonstration and educational purposes.

The primary goal is to compare distinct `EncryptionProvider` implementations:
- **`RawHybridKeyProvider`**: The **NIAP-compliant** reference implementation. It uses raw JCA primitives to strictly control key memory lifecycles (zeroization) and supports secure lock-state operations.
- **`HybridKeyProvider`**: A standard implementation using **Google Tink's Hybrid Encryption** (ECIES). While it is theoretically capable of meeting most requirements, strict memory zeroization (`FCS_CKM_EXT.4`) depends entirely on the Tink library's internals. Therefore, a completely compliant implementation is not provided.
- **`RawKeyProvider`**: A manual implementation using raw Android JCA APIs. It was an earlier candidate, but due to the difficulty of satisfying the strict NIAP requirements, a complete implementation is not provided.

## 2. Security Architecture & Compliance

This library is designed to meet the strict security requirements of the **NIAP Mobile Device Fundamentals Protection Profile (MDF PP) Version 3.3**.

### Key Implementation: `RawHybridKeyProvider`
Unlike standard high-level wrappers, our `RawHybridKeyProvider` uses raw JCA primitives to ensure full control over the key lifecycle, specifically addressing memory zeroization requirements.

* **Algorithm**: Hybrid Encryption Scheme (EC-DH + HKDF + AES-GCM).
* **Compliance Features**:
    * **FCS_CKM_EXT.4 (Key Destruction)**: Explicitly zeroes out (overwrites) plain-text DEKs and shared secrets in volatile memory immediately after use via `finally` blocks. Standard high-level libraries often rely on Garbage Collection, which is insufficient for PP compliance.
    * **FDP_DAR_EXT.2 (Locked State Operation / Data at Rest)**: Fully and correctly implemented. Supports encryption even when the device is locked (AFU/BFU) by leveraging a cached public key configuration, while keeping the private key securely hardware-backed in the TEE. It also includes an automatic `FDP_DAR_EXT.2.4` sweep-and-rewrap mechanism that transitions files encrypted via asymmetric keys back to a more efficient symmetric encryption as soon as the device is unlocked.
    * **FCS_CKM_EXT.2 (Key Generation)**: Uses `SecureRandom` for generating ephemeral Data Encryption Keys (DEKs).
    * **FCS_STG_EXT.2 (Key Storage)**: Implements Envelope Encryption where DEKs are wrapped by a TEE-backed Key Encryption Key (KEK).

## 3. Key Components

### `EncryptionProvider` Implementations

#### `RawHybridKeyProvider` (Compliant)
- **Strategy**: A robust two-tier envelope encryption designed for Common Criteria evaluation.
- **KEK (Key-Encrypting Key)**: An `EC` (Elliptic Curve) key pair stored securely in the `AndroidKeyStore` with `unlockedDeviceRequired`.
- **DEK (Data-Encapsulating Key)**: An ephemeral `AES-256-GCM` key generated via `SecureRandom` for each file.
- **Lock-State Behavior**:
    - **Encryption**: Supported in locked state. Uses a pre-shared/cached Public Key to wrap the ephemeral DEK without accessing the Keystore.
    - **Decryption**: Fails securely when locked. Requires user authentication to access the Private Key in the TEE for unwrapping.
- **Memory Safety**: Implements explicit zeroization of sensitive byte arrays immediately after use.

#### `HybridKeyProvider` (Tink Standard)
- **Strategy**: Two-tier envelope encryption using the **Google Tink** library.
- **KEK**: An `AES` key stored in the `AndroidKeyStore`.
- **DEK**: A Tink-managed ECIES keyset.
- **Note**: This implementation demonstrates the standard usage of Tink's Hybrid primitives. While secure for general use, it abstracts memory management, making it difficult to prove strict compliance with NIAP's volatile memory zeroization requirements (`FCS_CKM_EXT.4`). Since explicit zeroization would require modifying Tink itself, a fully compliant implementation is not provided.

#### `RawKeyProvider` (JCA Adapter)
- **Strategy**: A manual implementation wrapping standard Android JCA APIs (`Cipher`, `AndroidKeyStore`) into the Tink `Aead` interface.
- **Algorithm**: `AES/CBC/PKCS7Padding`.
- **Behavior**: Generates software-backed keys and imports them into the Keystore.
- **Purpose**: Demonstrates how to adapt raw platform APIs to a common interface without using the full Tink library features. It was an earlier candidate for the final implementation, but structural challenges in meeting strict compliance requirements mean a complete, NIAP-compliant version is not provided.

### Core Library (`encryption-lib`)
- Contains all core encryption logic, including the `EncryptionManager` (the facade API) and the various `EncryptionProvider` implementations. 
- Some providers, like `RawKeyProvider`, represent specific historic or anti-pattern strategies and are deliberately retained to serve as test cases and comparative examples.

### Demo Application (`locked-device-demo`)
- Contains a `MainActivity` that provides a UI to run encryption/decryption tests for each provider, both in unlocked and locked states, and to display the results.
- Includes a `DeviceAdminReceiver` required to programmatically lock the screen for the "Lock & Test" feature. The user must grant Device Administrator permissions to the app for this functionality to work.

## 4. How to Build and Run

1. Clone the repository.
2. Open the project in a recent version of Android Studio.
3. Build and run the app on an emulator or a physical device.
4. Use the buttons on the screen to test each encryption provider.
5. To use the "Lock & Test" feature, you may need to grant Device Administrator permissions to the app via the device's settings.

## 5. Security Verification Procedure (Direct Boot & Authentication)

This section describes how to verify the application's compliance with **FDP_DAR_EXT.2** (Encryption in Locked State) and **FIA_UAU_EXT.1** (Authentication for Decryption) using the included Test Harness.

### Prerequisites
* Android Device running Android 8.0+ (Pixel device recommended).
* Screen Lock (PIN/Pattern/Password) **MUST** be set up.
* ADB tool installed and authorized.

### Verification Steps

**Step 1: Install and Initialize**
1.  Install the application (`app-debug.apk`) on the target device.
2.  Launch the app once and ensure the screen is unlocked to allow initial key generation.
3.  Force stop the app to clear memory state:
    ```bash
    adb shell am force-stop com.android.niapsec
    ```

**Step 2: Reboot into "Before First Unlock" (BFU) State**
1.  Reboot the device using ADB or the power button.
    ```bash
    adb reboot
    ```
2.  **CRITICAL:** When the device boots up, **DO NOT enter your PIN/Password**.
    * The device must remain in the "Locked" state.
    * Verify the state using:
      ```bash
      adb shell dumpsys activity | grep "mUserUnlocked"
      ```
      *Expected Output:* `mUserUnlocked=false`

**Step 3: Execute the Test Harness**
Trigger the diagnostic receiver via ADB. This component is marked as `directBootAware` and will run even when the user is locked.

1.  Start log monitoring in a terminal:
    ```bash
    adb logcat -c
    adb logcat -s DirectBootTest
    ```

2.  In a separate terminal, send the test broadcast:
    ```bash
    adb shell am broadcast -a com.android.niapsec.ACTION_TEST_CRYPTO -n com.android.niapsec/.demo.DirectBootTestReceiver
    ```

**Step 4: Verify Pass/Fail Criteria (Log Analysis)**

Check the log output for the following verification points:

#### A. Environment Validation
* `User State: LOCKED (BFU State)` -> **MUST** be Locked.
* `Storage Context: Device Protected (DE)` -> **MUST** use DE storage.

#### B. RawHybridKeyProvider (JCA Implementation)
* `[RawHybrid_JCA] Attempting Encryption...`
* `[RawHybrid_JCA] SUCCESS: Encryption completed.` -> **PASS (FDP_DAR_EXT.2 Satisfied)**
    * *Rationale:* Public Key was successfully retrieved from DE storage and used for encryption.
* `[RawHybrid_JCA] Attempting Decryption...`
* `[RawHybrid_JCA] SUCCESS: Decryption failed as expected during lock.` -> **PASS (FIA_UAU_EXT.1 Satisfied)**
    * *Rationale:* Access to the Private Key (Android Keystore) was correctly blocked by the OS because the user is not authenticated.

#### C. HybridKeyProvider (Tink Implementation)
* `[Hybrid_Tink] Attempting Encryption...`
* `[Hybrid_Tink] SUCCESS: Encryption completed.` -> **PASS (FDP_DAR_EXT.2 Satisfied)**
* `[Hybrid_Tink] Attempting Decryption...`
* `[Hybrid_Tink] SUCCESS: Decryption failed as expected during lock.` -> **PASS (FIA_UAU_EXT.1 Satisfied)**

---

### Step 5: Verify "After First Unlock" (AFU) Behavior (Optional)
1.  Unlock the screen (enter PIN).
2.  Run the broadcast command again:
    ```bash
    adb shell am broadcast -a com.android.niapsec.ACTION_TEST_CRYPTO -n com.android.niapsec/.demo.DirectBootTestReceiver
    ```
3.  Verify logs:
    * `User State: UNLOCKED (AFU State)`
    * `SUCCESS: Decryption succeeded and data matches.` -> **PASS** (Normal operation restored).


## 6.Testing Guide

### 1. Symmetric Encrypt → Symmetric Decrypt

Verifies normal encryption and decryption when the device is unlocked.

1. Tap **Test File**
2. Tap **Check Status** — file should show `Symmetric 0x02`
3. Tap the file entry to decrypt and verify the plaintext

### 2. Asymmetric Encrypt → Sweep → Symmetric Decrypt

Verifies that files encrypted while locked are re-wrapped to symmetric after unlock.

1. Tap **Lock & Test** (device will lock and encrypt with asymmetric key)
2. Unlock the device and return to the app
3. Tap **Check Status** — file should show `Symmetric 0x02` (sweep ran automatically on unlock)
4. Tap the file entry to decrypt and verify the plaintext

### 3. Asymmetric Encrypt → Asymmetric Decrypt (Sweep Disabled)

Verifies that asymmetric-encrypted files remain unchanged and can still be decrypted without sweep.

1. Open the 3-dot menu in the top bar
2. Uncheck **Enable DAR_EXT.2.4 sweep**
3. Tap **Lock & Test** (device will lock and encrypt with asymmetric key)
4. Unlock the device and return to the app
5. Tap **Check Status** — file should show `Asymmetric 0x01` (sweep is disabled)
6. Tap the file entry to decrypt and verify the plaintext


## 7. License

Copyright 2026 The Android Open Source Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.