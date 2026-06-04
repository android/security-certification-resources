# Security Traceability Matrix (STM)

This document maps the Security Functional Requirements (SFRs) from the Protection Profile (PP_MDF_V3.3) to the specific implementation details within the application source code.

## 1. Traceability Table

| SFR ID | Requirement Name | Implementation Class | Location / Method | Rationale & Evidence |
| :--- | :--- | :--- | :--- | :--- |
| **FDP_DAR_EXT.2** | Sensitive Data Encryption | `HybridKeyProvider` | `encrypt()` | **Compliant:** Uses an asymmetric key scheme (ECIES-AEAD-HKDF via Tink) to allow data encryption even when the device is locked (B/F/U states). |
| **FDP_DAR_EXT.2** | Sensitive Data Encryption | `RawHybridKeyProvider` | `encrypt()` | **Compliant:** Implements JCA-based asymmetric encryption to support data ingestion in the Locked State without requiring private key access. |
| **FCS_STG_EXT.2** | Encrypted Key Storage | `HybridKeyProvider` | `getAead()` / `AndroidKeysetManager` | **Compliant:** Private keysets are stored in SharedPreferences wrapped by a Master Key held in the Android Keystore. |
| **FCS_STG_EXT.2** | Encrypted Key Storage | `RawHybridKeyProvider` | `generateAndStoreKeyPairIfNeeded()` | **Compliant:** Private keys are generated and stored directly within the `AndroidKeyStore` provider, ensuring hardware-backed protection. |
| **FCS_CKM_EXT.4** | Key Destruction (Volatile) | `RawHybridKeyProvider` | `encrypt()`, `decrypt()` / `finally` block | **Compliant:** Explicitly zeroes out (`fill(0)`) DEK, KEK, and Shared Secret byte arrays immediately after use to prevent memory remanence. |
| **FCS_CKM_EXT.4** | Key Destruction (Storage) | `RawHybridKeyProvider` | `destroy()` | **Compliant:** Invokes `KeyStore.deleteEntry()` and `SharedPreferences.Editor.clear()` to remove persistent key material. |
| **FIA_UAU_EXT.1** | Auth for Crypto Operation | `HybridKeyProvider` | `createMasterKeyIfNeeded()` | **Compliant:** Configures the Master Key with `.setUnlockedDeviceRequired(true)`, enforcing user authentication for keyset unwrapping. |
| **FIA_UAU_EXT.1** | Auth for Crypto Operation | `RawHybridKeyProvider` | `generateAndStoreKeyPairIfNeeded()` | **Compliant:** Sets `.setUnlockedDeviceRequired(true)` on the private key, ensuring the OS rejects decryption attempts without user authentication. |
| **FCS_CKM.2/LOCKED** | Key Establishment (Locked) | `HybridKeyProvider` | `getAead()` / `AndroidKeysetManager` | **Compliant:** Sets Uses standard ECIES-AEAD-HKDF schemes provided by Google Tink, which allow key establishment for encryption while the device is locked. |

## 2. Implementation Notes

### HybridKeyProvider (Tink-based)
* **Role**: Primary encryption provider using Google Tink library.
* **Key Management**: Relies on Tink's validated schemes for key establishment.
* **Memory Safety**: Volatile memory clearing relies on Tink's internal implementation and JVM Garbage Collection.

### RawHybridKeyProvider (JCA-based)
* **Role**: Alternative implementation for high-assurance memory handling.
* **Key Management**: Manually composes JCA primitives.
* **Memory Safety**: **Explicitly implements volatile memory zeroization** to satisfy strict interpretations of FCS_CKM_EXT.4.