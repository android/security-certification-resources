These patches are based off of the android16-dev branch
There are few changes for wpa and keystore2 patches.

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
