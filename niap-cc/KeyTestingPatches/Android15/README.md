These patches are based off of the android15-dev branch
This time, We can apply Android 14 patches except for keystore.

Here are the paths that the patches apply to:

`frameworks/base`:
 - 0001-SyntheticPasswordCrypto.java.patch
 - 0001-SyntheticPasswordManager.java.patch

`system/vold`:
 - 0001-DO-NOT-SUBMIT-log-disk-encryption-keys.patch

`external/wpa_supplicant_8`:
 - 0001-Dump-security-key.patch

`system/security/keystore2`:
 - DumpKeystore2/super_key.rs
 - DumpKeystore2/zvec.rs
