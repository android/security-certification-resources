# Key Testing Patches for Android 16

We changed the patches and its styles from this version, and renewed according to the updates of
the systems and the KMD(Key Management Description) document. 

We placed the patch files in the corresponding directory.
So you can install them with overwriting, and then run patch commands one by one.

Base Version : 25Q2-release

## Patch Specs

### Patch for /system/vold/

  - Reference : Table.4 Device Encryption Keys
  - File : KeyStorage.cpp
  
  1.  Dump the key passed by the ‘dir’ arg.
  1.  (NEW) Dump appId
  1.  (NEW) Dump secdiscardable_hash

### Patch for /external/wpa_supplicant_8/src/utils/
   - Reference : Table 11 TSF WPA2 Keys
   -  File :wpa_debug.c

It’s a patch file for the debug line of the  WPA supplicant. Display WPA2 keys forcefully.

### Patch for /system/security/keystore2/

 - Reference : Table 8 - KeyStore Hierarchy Keys 
    - KeyStore Daemon Keys (KeystoreKeys not included in the KeyMint)

#### File : [crypto/zvec.rs]
The u8 vector with auto-zero-padding feature when unreferenced/destroyed.
For customize zvec toString to show containers.

#### File : [super_key.rs]

Hook, extract, create and encrypt,unlock operation to check the superkeys.

1. Add fmt::Display implementation to the SuperEncryptonAlogrithm enum.
1. pub fn extract_super_key_from_key_entry() -> Extract a super key.
    1. Read key value and algorithm
    1. (NEW) Read Key Parameters like salt,iv,aead_tag
1. pub fn encrypt_with_password() -> Encrypt super_key with password
    1. Encrypted Password
    1. (NEW) Key, and Password and other metadatas(iv,aead_tag,super_key)
1. (NEW)pub fn encrypt_with_aes_super_key() -> Encrypt super_key with aes
    1. super_key
    1. key_blob&iv
1. (NEW)pub fn encrypt_with_hybrid_super_key()->Encrypt super_key with ecdh-521 and ephem(public key)
1. pub fn create_super_key() -> Create a super_key
super_key
    1. (NEW)public_key (for asymmetric key)
    1. (NEW)encrypted_super_key
    1. (NEW)metadata iv, user_id, password etc
    1. (NEW) fn unlock_unlocked_device_required_keys() 
add a debug line(it calls create or read)
1. (NEW) fn lock_unlocked_device_required_keys()
biometric unlock key
1. fn try_unlock_user_with_biometric()

### Patch for /framework/base/services/…

 - Full Path : /framework/base/services/core/java/com/android/server/locksettings/
 - Reference : Table 3 - Lock Screen Key Factor Keys
 
#### File : [SyntheticPasswordCrypto.java]

 - personalizedHash to dump the keys below 
 - deriveSubkey: Derive from Synthetic Password(SP) below may be called
     - PERSONALIZATION_KEY_STORE_PASSWORD
     - PERSONALIZATION_FBE_KEY
     - PERSONALIZATION_AUTHSECRET_KEY
     - PERSONALIZATION_PASSWORD_HASH
     - PERSONALIZATION_PASSWORD_METRICS
     - PERSONALIZATION_PERSONALIZATION_AUTHSECRET_ENCRYPTION_KEY
 - recreate/PERSONALIZATION_SP_SPLIT
 - transformUnderWeaverSecret/PERSONALIZATION_WEAVER_PASSWORD
 - transformUnderSecdiscardable/PERSONALIZATION_SECDISCARDABLE
 - stretchedLskfToGkPassword/PERSONALIZATION_USER_GK_AUTH
 - stretchedLskfToWeaverKey/PERSONALIZATION_WEAVER_KEY

#### File : [SyntheticPasswordManager.java]

1. deriveSubKey/V3 synthetic password
2. createLskfBasedProtector/pwdToken
3. unwrapSyntheticPasswordBlob/protectorSecret&SyntheticPassword

#### File : (NEW)[LockSettingsService.java] 
1. setCeStorageProtection() 
    - FBE KEK, It used by encrypt the CE Storage 
