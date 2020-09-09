These patches are based off of the android11-release branch
at this revision of platform/manifest:
9e79d8ab6fff81b5a62df67487661663219bb063
Here are the paths that the patches apply to:

`frameworks/base`:
  0001-Add-logging-for-SP800-derived-passwords-too.patch
  0001-DO-NOT-SUBMIT-log-personalized-keys.patch
  0001-Dump-synthetic-password-related-keys.patch

`system/vold`:
  0001-DO-NOT-SUBMIT-log-disk-encryption-keys.patch

`system/security`:
  0001-Dump-master-key-when-generated-and-read.patch

`external/wpa_supplicant_8`:
  0001-Dump-security-key.patch
