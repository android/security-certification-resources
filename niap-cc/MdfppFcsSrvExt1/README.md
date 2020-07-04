Encryption Test Tool - MDFPP_FCS_SRC_EXT_1 (Java)
===========================================

This sample app is a tool which aids OEMs in testing their devices for evaluation of the Common
Criteria certification through [NIAP](https://www.niap-ccevs.org/).

This app is designed to address requirements for MDFPP_FCS_SRV_EXT_1


Introduction
------------

This sample app uses both the AndroidKeyStore, and AndroidOpenSSL (BoringSSL) to run various crypto
operations to ensure that they are working properly. WorkManager is used to run several worker test
classes that provide the necessary output for the certification process.

Security Providers Used:
AndroidKeyStore, AndroidOpenSSL

Tested:
Cipher: AES, RSA - Encryption
Signature: ECDSA, RSA - Signatures
Message Digest: SHA - Digests
Mac: HMACSHA - Hashed Secure Message Authentication Codes
Password based Encrytption: PBKDF2 - Password Derivation Function via SecretKeyFactory

Pre-requisites
--------------

- Android SDK 28+
- Android Build Tools v29.0.3

Getting Started
---------------

This sample uses the Gradle build system. To build this project, use the
"gradlew build" command or use "Import Project" in Android Studio.

Support
-------

If you've found an error in this sample, please file an issue on the github bug tracker for this
project.

Please see CONTRIBUTING.md in the project root for more details.
