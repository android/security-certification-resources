Consider using the pymupdf_layout package for a greatly improved page layout analysis.
# Google Pixel Devices on Android 15 – Security Target

## **Google LLC**

1600 Amphitheatre Parkway
Mountain View, CA 94043
USA



Version: 1.0
April 4, 2025


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025

## Table of Contents


**1** **Security Target Introduction .......................................................................................................5**
1.1 Security Target Reference ............................................................................................................. 6
1.2 TOE Reference ............................................................................................................................... 6
1.3 TOE Overview ................................................................................................................................ 6
1.4 TOE Description............................................................................................................................. 7
1.4.1 TOE Architecture ................................................................................................................... 8
1.4.2 TOE Documentation ............................................................................................................ 11
**2** **Conformance Claims ................................................................................................................. 12**
2.1 Conformance Rationale .............................................................................................................. 13
**3** **Security Objectives ................................................................................................................... 14**
3.1 Security Objectives for the Operational Environment ................................................................ 14
**4** **Extended Components Definition.............................................................................................. 16**
**5** **Security Requirements.............................................................................................................. 19**
5.1 TOE Security Functional Requirements ...................................................................................... 19
5.1.1 Security Audit (FAU) ............................................................................................................ 22
5.1.2 Cryptographic Support (FCS) ............................................................................................... 28
5.1.3 User Data Protection (FDP) ................................................................................................. 37
5.1.4 Identification and Authentication (FIA) .............................................................................. 39
5.1.5 Security management (FMT) ............................................................................................... 46
5.1.6 Protection of the TSF (FPT) ................................................................................................. 55
5.1.7 TOE Access (FTA) ................................................................................................................. 58
5.1.8 Trusted Path/Channels (FTP) .............................................................................................. 59
5.2 TOE Security Assurance Requirements ....................................................................................... 61
5.2.1 Development (ADV) ............................................................................................................ 61
5.2.2 Guidance Documents (AGD) ............................................................................................... 62
5.2.3 Life-cycle support (ALC) ...................................................................................................... 63
5.2.4 Tests (ATE) ........................................................................................................................... 64
5.2.5 Vulnerability assessment (AVA) .......................................................................................... 64
**6** **TOE Summary Specification ...................................................................................................... 66**
6.1 Security audit .............................................................................................................................. 66
6.2 Cryptographic support ................................................................................................................ 68
6.3 User data protection ................................................................................................................... 79
6.4 Identification and authentication ............................................................................................... 85
6.5 Security management ................................................................................................................. 91
6.6 Protection of the TSF .................................................................................................................. 92
6.7 TOE access ................................................................................................................................... 98
6.8 Trusted path/channels ................................................................................................................ 99
6.9 Live Cycle ................................................................................................................................... 100


2 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025

## List of Tables


Table 1 - TOE Common Attributes ................................................................................................................ 6
Table 2 - Evaluated Devices .......................................................................................................................... 7
Table 3 - Technical Decisions ...................................................................................................................... 13
Table 4 - PP_MDF_V3.3 Extended Components ......................................................................................... 17
Table 5 - MOD_BT_V1.0 Extended Components ........................................................................................ 17
Table 6 - MOD_WLANC_V1.0 Extended Components ................................................................................ 17
Table 7 - MOD_BIO_V1.1 Extended Components ...................................................................................... 18
Table 8 - PKG_TLS_V1.1 Extended Components......................................................................................... 18
Table 9 - MOD_MDM_AGENT_V1.0 Extended Components ...................................................................... 18
Table 10 - PP_MDF_V3.3 Extended Assurance Components ..................................................................... 18
Table 11 - TOE Security Functional Components ........................................................................................ 22
Table 12 - PP_MDF_V3.3 Audit Events ....................................................................................................... 24
Table 13 - MOD_BT_V1.0 Audit Events ...................................................................................................... 25
Table 14 - MOD_WLANC_V1.0 Audit Events .............................................................................................. 26
Table 15 - MOD_MDM_AGENT_V1.0 Audit Events .................................................................................... 27
Table 16 - Security Management Functions ............................................................................................... 53
Table 17 - Bluetooth Security Management Functions .............................................................................. 53
Table 18 - WLAN Security Management Functions .................................................................................... 54
Table 19 - Assurance Components ............................................................................................................. 61
Table 20 - Audit Event Table References .................................................................................................... 67
Table 21 - Asymmetric Key Generation ...................................................................................................... 68
Table 22 - Wi-Fi Alliance Certificates .......................................................................................................... 69
Table 23 - Salt Nonces ................................................................................................................................. 71
Table 24 - BoringSSL Cryptographic Algorithms ......................................................................................... 72
Table 25 - LockSettings Service KDF Cryptographic Algorithms ................................................................. 72
Table 26 - Titan Security Chipsets ............................................................................................................... 72
Table 27 - Titan M2 with v1.5.1 Firmware Cryptographic Algorithms ........................................................ 73
Table 28 - Titan M2 with v1.3.10 Firmware Cryptographic Algorithms ...................................................... 73
Table 29 - Titan M2 with v1.2.10 Firmware Cryptographic Algorithms ...................................................... 73
Table 30 - Wi-Fi Chipsets ............................................................................................................................. 74
Table 31 - Google Tensor G4 Hardware Cryptographic Algorithms ........................................................... 74
Table 32 - Google Tensor G3 Hardware Cryptographic Algorithms ........................................................... 74
Table 33 - Google Tensor G2 Hardware Cryptographic Algorithms ........................................................... 75
Table 34 - Google Tensor Hardware Cryptographic Algorithms ................................................................. 75
Table 35 - Functional Categories ................................................................................................................. 82
Table 36 - Supported Biometric Modalities ................................................................................................ 85
Table 37 - Fingerprint False Accept/Reject Rates ....................................................................................... 87
Table 38 - Power-up Cryptographic Algorithm Known Answer Tests ......................................................... 97


3 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


Table 39 - Security Update Period ............................................................................................................ 100

## List of Figures


Figure 1 - Password Conditioning ............................................................................................................... 76


4 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025

## 1 Security Target Introduction


This section identifies the Security Target (ST) and Target of Evaluation (TOE) identification, ST
conventions, ST conformance claims, and the ST organization. The TOE consists of the Pixel Devices on
Android 15 provided by Google LLC. The TOE is being evaluated as a Mobile Device.


The Security Target contains the following additional sections:

  - Conformance Claims (Section 2)

  - Security Objectives (Section 3)

  - Extended Components Definition (Section 4)

  - Security Requirements (Section 5)

  - TOE Summary Specification (Section 6)


_**Acronyms and Terminology**_


AA Assurance Activity


BAF Biometric Authentication Factor


CC Common Criteria


CCEVS Common Criteria Evaluation and Validation Scheme


MDM Mobile Device Management


NFC Near Field Communication


PBFPS Power-Button Fingerprint Sensor


PP Protection Profile


SAR Security Assurance Requirement


SEE Separate Execution Environment


SFR Security Functional Requirement


ST Security Target


TEE Trusted Execution Environment


TOE Target of Evaluation


UDFPS Under-Display Fingerprint Sensor


UI User Interface


_**Conventions**_


The following conventions have been applied in this document:

  - Security Functional Requirements – Part 2 of the CC defines the approved set of operations that
may be applied to functional requirements: iteration, assignment, selection, and refinement.


5 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


`o` Iteration: allows a component to be used more than once with varying operations. In

the ST, iteration is indicated by a parenthetical number placed at the end of the
component. For example FDP_ACC.1(1) and FDP_ACC.1(2) indicate that the ST includes
two iterations of the FDP_ACC.1 requirement.

`o` Assignment: allows the specification of an identified parameter. Assignments are

indicated using bold and are surrounded by brackets (e.g., [ **assignment** ]). Note that an
assignment within a selection would be identified in italics and with embedded bold
brackets (e.g., [ _**[selected-assignment]**_ ]).

`o` Selection: allows the specification of one or more elements from a list. Selections are

indicated using bold italics and are surrounded by brackets (e.g., [ _**selection**_ ]).

`o` Refinement: allows the addition of details. Refinements are indicated using bold, for

additions, and strike-through, for deletions (e.g., “… all objects …” or “… some big things
…”).

  - Other sections of the ST – Other sections of the ST use bolding to highlight text of special
interest, such as captions.

### 1.1 Security Target Reference


**ST Title** Google Pixel Devices on Android 15 – Security Target
**ST Version** 1.0
**ST Date** April 4, 2025

### 1.2 TOE Reference


**TOE Identification** Google Pixel Devices on Android 15
**TOE Developer** Google LLC
**Evaluation Sponsor** Google LLC

### 1.3 TOE Overview


The Target of Evaluation (TOE) is Google Pixel Devices on Android 15. All the included phones have the
following information in common:

|Android OS Version|Device Policy Version|Architecture|Security Patch Level|
|---|---|---|---|
|Android 15|128|ARMv8|December 2024|



_**Table 1 - TOE Common Attributes**_


The TOE consists of the following devices:

|Google Product|Col2|Model #|SoC|Kernel|
|---|---|---|---|---|
|Pixel 9 Pro XL|GZC4K, GQ57S, GGX8B|GZC4K, GQ57S, GGX8B|Google Tensor G4|6.1|
|Pixel 9 Pro|GEC77, GWVK6, GR83Y|GEC77, GWVK6, GR83Y|Google Tensor G4|6.1|
|Pixel 9|GUR25, G1B60, G2YBB|GUR25, G1B60, G2YBB|Google Tensor G4|6.1|
|Pixel 9 Pro Fold|GGH2X, GC15S|GGH2X, GC15S|Google Tensor G4|6.1|
|Pixel 9a|GXQ96, GTF7P, G3Y12|GXQ96, GTF7P, G3Y12|Google Tensor G4|6.1|
|Pixel 8 Pro|G1NMW, GC3VE|G1NMW, GC3VE|Google Tensor G3|6.1|



6 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025

|Google Product|Col2|Model #|SoC|Kernel|
|---|---|---|---|---|
|Pixel 8|GKWS6, G9BQD|GKWS6, G9BQD|Google Tensor G3|6.1|
|Pixel 8a|G5760D, G6GPR, G8HHN|G5760D, G6GPR, G8HHN|Google Tensor G3|6.1|
|Pixel Tablet|GTU8P|GTU8P|Google Tensor G2|6.1|
|Pixel Fold|G9FPL, G0B96|G9FPL, G0B96|Google Tensor G2|6.1|
|Pixel 7 Pro|GVU6C, G03Z5, GQML3|GVU6C, G03Z5, GQML3|Google Tensor G2|6.1|
|Pixel 7|GE2AE, GFE4J, GP4BC|GE2AE, GFE4J, GP4BC|Google Tensor G2|6.1|
|Pixel 7a|GWKK3, GHL1X, G82U8,<br>G0DZQ|GWKK3, GHL1X, G82U8,<br>G0DZQ|Google Tensor G2|6.1|
|Pixel 6 Pro|GF5KQ, G8V0U, GLU0G|GF5KQ, G8V0U, GLU0G|Google Tensor|6.1|
|Pixel 6|GR1YH, GB7N6, G9S9B|GR1YH, GB7N6, G9S9B|Google Tensor|6.1|
|Pixel 6a|GX7AS, GB62Z, G1AZG, GB17L|GX7AS, GB62Z, G1AZG, GB17L|Google Tensor|6.1|



_**Table 2 - Evaluated Devices**_


Google manufacturers some of the phones in multiple variants, differing in size (the designations vary,
from the “base” device not having any, and other models having something like “a” or “Pro”) or build
materials (entry and premium). The only differences between variants of a given device are build
materials, screen type and size, battery capacity, cameras, RAM and Flash storage. The Pro phones are
normally physically larger and have larger screen sizes, battery capacity and possibly RAM, while the “a”
phones may have a smaller screen or different build materials. Storage options vary with each release
and may be selectable by the customer of the device at purchase.


The TOE allows basic telephony features (make and receive phone calls, send and receive SMS/MMS
messages) as well as advanced network connectivity (allowing connections to both 802.11 Wi-Fi and
5G/4G LTE/3G/2G mobile data networks). The TOE supports using client certificates to connect to access
points offering WPA2/WPA3 networks with 802.1x/EAP-TLS, or alternatively connecting to cellular base
stations when utilizing mobile data.


The TOE offers mobile applications an Application Programming Interface (API) including that provided
by the Android framework and supports API calls to the Android Management APIs.

### 1.4 TOE Description


The TOE is a mobile device to support enterprises and individual users alike.


Some features and settings must be enabled for the TOE to operate in its evaluated configuration. The
following features and settings must be enabled:


1. Enable a password screen lock
2. Do not use Smart Lock
3. Enable encryption of Wi-Fi and Bluetooth secrets (NIAP mode DPM API)
4. Do not use USB debugging
5. Do not allow installation of applications from unknown sources
6. Enable security logging
7. Disable ‘Usage & Diagnostic’ settings
8. Disable Captive Portal Checking
9. Loaded applications must be implemented utilizing the NIAPSEC library


7 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


Doing this ensures that the phone complies with the PP_MDF_V3.3 requirements. Please refer to the
Admin Guide on how to configure these settings and features.


1.4.1 TOE Architecture


The TOE provides a rich API to mobile applications and provides users installing an application the
option to either approve or reject an application based upon the API access that the application requires
(or to grant applications access at runtime).


The TOE also provides users with the ability to protect Data-At-Rest with AES encryption, including all
user and mobile application data stored in the user’s data partition. The TOE uses a key hierarchy that
combines a REK with the user’s password to provide protection to all user and application cryptographic
keys stored in the TOE.


The TOE includes an additional hardware security chip (the Titan M2) that provides dedicated key
storage [1] . The TOE makes this secure, hardware key storage available to mobile applications through the
StrongBox extensions to the Android Keystore. Currently, the StrongBox extension is not used for any
system keys, but remains an option for applications to use should they desire the protections it
provides.


Finally, the TOE can interact with a Mobile Device Management (MDM) system to allow enterprise
control of the configuration and operation of the device so as to ensure adherence to enterprise-wide
policies (for example, restricting use of a corporate provided device’s camera, forced configuration of
maximum login attempts, pulling of audit logs off the TOE, etc.) as well as policies governing enterprise
applications and data. An MDM is made up of two parts: the MDM Agent and MDM Server. The MDM
Agent is installed on the phone as an administrator with elevated permissions (allowing it to change the
relevant settings on the phone) while the MDM Server is used to issue the commands to the MDM
Agent. The TOE includes an MDM Agent as part of the evaluated configuration. A user may choose to
install a third party MDM Agent, which is out-of-scope for this evaluation.


The TOE includes several different levels of execution including (from lowest to highest): hardware, a
Trusted Execution Environment, Android’s bootloader, and Android’s user space, which provides APIs
allowing applications to leverage the cryptographic functionality of the device.


_1.4.1.1_ _Physical Boundaries_


The TOE’s physical boundary is the physical perimeter of its enclosure. The TOE runs Android as its
software/OS, executing on the Google Tensor processors. The TOE does not include the user
applications that run on top of the operating system, but does include controls that limit application
behavior. Further, the device provides a built-in MDM Agent (downloadable MDM Agents are not
considered in-scope) to be installed to limit or permit different functionality of the device.


The TOE communicates and interacts with 802.11-2012 Access Points and mobile data networks to
establish network connectivity, and through that connectivity interacts with MDM servers that allow
administrative control of the TOE.


1 Does not apply to the Pixel 6 Pro/6/6a

8 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


_1.4.1.2_ _Logical Boundaries_


This section summarizes the security functions provided by the Pixel phones:


- Security audit


- Cryptographic support


- User data protection


- Identification and authentication


- Security management


- Protection of the TSF


- TOE access


- Trusted path/channels


_1.4.1.2.1_ _Security audit_


The TOE implements the SecurityLog and logcat that are stored in a circular memory buffers. An MDM
agent can read/fetch the logs (both the SecurityLog and logcat) and then handle appropriately
(potentially storing the log to Flash or transmitting its contents to the MDM server). These log methods
meet the logging requirements outlined by FAU_GEN.1 in PP_MDF_V3.3. Please see the Security audit
section for further information and specifics.


_1.4.1.2.2_ _Cryptographic support_


The TOE includes multiple cryptographic libraries with CAVP certified algorithms for a wide range of
cryptographic functions including the following: asymmetric key generation and establishment,
symmetric key generation, encryption/decryption, cryptographic hashing and keyed-hash message
authentication. These functions are supported with suitable random bit generation, key derivation, salt
generation, initialization vector generation, secure key storage, and key and protected data destruction.
These primitive cryptographic functions are used to implement security protocols such as TLS, EAP-TLS,
and HTTPS and to encrypt the media (including the generation and protection of data and key
encryption keys) used by the TOE. Many of these cryptographic functions are also accessible as services
to applications running on the TOE allowing application developers to ensure their application meets the
required criteria to remain compliant to PP_MDF_V3.3 standards.


_1.4.1.2.3_ _User data protection_


The TOE controls access to system services by hosted applications, including protection of the Trust
Anchor Database. Additionally, the TOE protects user and other sensitive data using encryption so that
even if a device is physically lost, the data remains protected. The TOE’s evaluated configuration
supports Android Enterprise profiles to provide additional separation between application and
application data belonging to the Enterprise profile. Please see the Admin Guide for additional details
regarding how to set up and use Enterprise profiles.


9 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


_1.4.1.2.4_ _Identification and authentication_


The TOE supports a number of features related to identification and authentication. From a user
perspective, except for FCC mandated (making phone calls to an emergency number) or non-sensitive
functions (e.g., choosing the keyboard input method or taking screen shots), a password (i.e., Password
Authentication Factor) must be correctly entered to unlock the TOE. Also, even when unlocked, the TOE
requires the user re-enter the password to change the password. Passwords are obscured when entered
so they cannot be read from the TOE's display and the frequency of entering passwords is limited and
when a configured number of failures occurs, the TOE will be wiped to protect its contents. Passwords
can be constructed using upper and lower cases characters, numbers, and special characters and
passwords up to 16 characters are supported. The TOE can also be configured to utilize a biometric
authentication factor (fingerprints), to unlock the device (this only works after the password has been
entered after the device powers on).


The TOE can also serve as an 802.1X supplicant and can both use and validate X.509v3 certificates for
EAP-TLS, TLS, and HTTPS exchanges.


_1.4.1.2.5_ _Security management_


The TOE provides all the interfaces necessary to manage the security functions identified throughout
this Security Target as well as other functions commonly found in mobile devices. Many of the available
functions are available to users of the TOE while many are restricted to administrators operating
through a Mobile Device Management solution once the TOE has been enrolled. Once the TOE has been
enrolled and then un-enrolled, it will remove Enterprise applications and remove MDM policies.


_1.4.1.2.6_ _Protection of the TSF_


The TOE implements a number of features to protect itself to ensure the reliability and integrity of its
security features. It protects particularly sensitive data such as cryptographic keys so that they are not
accessible or exportable through the use of the application processor’s hardware. The TOE disallows all
read access to the Root Encryption Key (REK) and retains all keys derived from the REK within its Trusted
Execution Environment (TEE). Application software can only use keys derived from the REK by reference
and receive the result. The TEE is a Separate Execution Environment (SEE), running outside the Android
operating system on the device.


The TOE also provides its own timing mechanism to ensure that reliable time information is available
(e.g., for log accountability). It enforces read, write, and execute memory page protections, uses address
space layout randomization, and stack-based buffer overflow protections to minimize the potential to
exploit application flaws. It also protects itself from modification by applications as well as to isolate the
address spaces of applications from one another to protect those applications.


The TOE includes functions to perform self-tests and software/firmware integrity checking so that it
might detect when it is failing or may be corrupt. If any self-tests fail, the TOE will not go into an
operational mode. It also includes mechanisms (i.e., verification of the digital signature of each new
image) so that the TOE itself can be updated while ensuring that the updates will not introduce
malicious or other unexpected changes in the TOE. Digital signature checking also extends to verifying
applications prior to their installation as all applications must have signatures (even if self-signed).


10 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


_1.4.1.2.7_ _TOE access_


The TOE can be locked, obscuring its display, by the user or after a configured interval of inactivity. The
TOE also has the capability to display an administrator specified (using the TOE’s MDM API) advisory
message (banner) when the user unlocks the TOE for the first use after reboot.


The TOE is also able to attempt to connect to wireless networks as configured.


_1.4.1.2.8_ _Trusted path/channels_


The TOE supports the use of IEEE 802.11-2012, 802.1X, and EAP-TLS and TLS, HTTPS to secure
communications channels between itself and other trusted network devices.


1.4.2 TOE Documentation


Google Pixel Phones on Android 15 Administrator Guidance Documentation, Version 1.0, April 4, 2025

**[Admin Guide]**


11 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025

## 2 Conformance Claims


This TOE is conformant to the following CC specifications:

  - Common Criteria for Information Technology Security Evaluation Part 2: Security functional
components, Version 3.1, Revision 5, April 2017.

`o` Part 2 Extended

  - Common Criteria for Information Technology Security Evaluation Part 3: Security assurance
components, Version 3.1, Revision 5, April 2017.

`o` Part 3 Extended

  - PP-Configuration for Mobile Device Fundamentals, Biometric enrolment and verification – for
unlocking the device, Bluetooth, MDM Agents, and WLAN Clients, Version 1.0, 16 August 2023
(CFG_MDF-BIO-BT-MDMA-WLANC_V1.0)

`o` The PP-Configuration includes the following components:

         - Base-PP: Protection Profile for Mobile Device Fundamentals, Version 3.3, 12
September 2022 (PP_MDF_V3.3)

         - PP-Module: collaborative PP-Module for Biometric enrolment and verification for unlocking the device - [BIOPP-Module], Version 1.1, September 12, 2022
(MOD_BIO_V1.1)

         - PP-Module: PP-Module for Bluetooth, Version 1.0, 15 April 2021
(MOD_BT_V1.0)

         - PP-Module: PP-Module for MDM Agents, Version 1.0, 25 April 2019
(MOD_MDM_AGENT_V1.0)

         - PP-Module: PP-Module for WLAN Clients, Version 1.0, 31 March 2022
(MOD_WLANC_V1.0)

  - Package Claims:

`o` Functional Package for Transport Layer Security (TLS), Version 1.1, 1 March 2019

(PKG_TLS_V1.1)

  - Technical Decisions as of March 11, 2025:

|TD Number|Applied|Rationale|
|---|---|---|
|TD0442 – PKG_TLS_V1.1|Yes||
|TD0469 – PKG_TLS_V1.1|No|Product does not have a TLS server|
|TD0497 – MOD_MDM_AGENT_V1.0|Yes||
|TD0499 – PKG_TLS_V1.1|Yes||
|TD0513 – PKG_TLS_V1.1|Yes||
|TD0600 – MOD_BT_V1.0 &<br>MOD_MDM_AGENT_V1.0|No|Using later PP-Configuration|
|TD0640 – MOD_BT_V1.0|Yes||
|TD0645 – MOD_BT_V1.0|Yes||
|TD0650 – MOD_BT_V1.0 &<br>MOD_MDM_AGENT_V1.0|Yes||
|TD0660 – MOD_MDM_AGENT_V1.0|Yes||



12 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025

|TD Number|Applied|Rationale|
|---|---|---|
|TD0667 – MOD_WLANC_V1.0|Yes||
|TD0671 – MOD_BT_V1.0|Yes||
|TD0673 – MOD_MDM_AGENT_V1.0|Yes||
|TD0677 – PP_MDF_V3.3|Yes||
|TD0685 – MOD_BT_V1.0|Yes||
|TD0689 – PP_MDF_V3.3|Yes||
|TD0700 – MOD_BIO_V1.1|Yes||
|TD0703 – MOD_WLANC_V1.0|Yes||
|TD0704 – PP_MDF_V3.3|Yes||
|TD0707 – MOD_BT_V1.0|Yes||
|TD0710 – MOD_WLANC_V1.0|Yes||
|TD0714 – MOD_BIO_V1.1|Yes||
|TD0724 – PP_MDF_V3.3|Yes||
|TD0726 – PKG_TLS_V1.1|No|Product does not have a TLS server|
|TD0739 – PKG_TLS_V1.1|Yes||
|TD0755 – MOD_MDM_AGENT_V1.0|Yes||
|TD0770 – PKG_TLS_V1.1|No|Product does not have a TLS Server|
|TD0779 – PKG_TLS_V1.1|No|Product does not have a TLS Server|
|TD0797 – MOD_WLANC_V1.0|Yes||
|TD0837 – MOD_WLANC_V1.0|Yes||
|TD0844 – PP_MDF_V3.3|Yes||
|TD0871 – PP_MDF_V3.3|Yes||
|TD0892 – MOD_BIO_V1.1|Yes||



_**Table 3 - Technical Decisions**_

### 2.1 Conformance Rationale


The ST conforms to
PP_MDF_V3.3/MOD_BT_V1.0/MOD_WLANC_V1.0/MOD_BIO_V1.1/PKG_TLS_V1.1/MOD_MDM_AGENT
_V1.0. For simplicity, this shall be referenced as MDF/BT/WLANC/BIO/TLS/MDMA. As explained
previously, the security problem definition, security objectives, and security requirements have been
drawn from the PP.


The ST claims conformance with the following Use Cases in the PP_MDF_V3.3:

  - USE CASE 1 with the following exceptions:

`o` For FCS_STC_EXT.1.2, “the user” is selected as Android will provide separate storage

between the enterprise and personal space via the work profile, so “the user” can
manage keys for the personal space while only the administrator can manage keys in
the work profile

`o` FPT_TUD_EXT.5 is not applicable for Android as applications are not signed in this

manner

  - USE CASE 2 with no exceptions


13 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025

## 3 Security Objectives


The Security Problem Definition may be found in the MDF/BT/WLANC/BIO/TLS/MDMA and this section
reproduces only the corresponding Security Objectives for operational environment for reader
convenience. The MDF/BT/WLANC/BIO/TLS/MDMA offers additional information about the identified
security objectives, but that has not been reproduced here and the MDF/BT/WLANC/BIO/TLS/MDMA
should be consulted if there is interest in that material.


In general, the MDF/BT/WLANC/BIO/TLS/MDMA has defined Security Objectives appropriate for mobile
device and as such are applicable to the Google Pixel Devices on Android 15 TOE.

### 3.1 Security Objectives for the Operational Environment


**PP_MDF_V3.3**
**OE.CONFIG** TOE administrators will configure the Mobile Device security functions correctly to create
the intended security policy.


**OE.NOTIFY** The Mobile User will immediately notify the administrator if the Mobile Device is lost or
stolen.


**OE.PRECAUTION** The Mobile User exercises precautions to reduce the risk of loss or theft of the Mobile
Device.


**OE.DATA_PROPER_USER** Administrators take measures to ensure that mobile device users are
adequately vetted against malicious intent and are made aware of the expectations for appropriate use
of the device.


**MOD_WLANC_V1.0**
**OE.NO_TOE_BYPASS** Information cannot flow between external and internal networks located in
different enclaves without passing through the TOE.


**OE.TRUSTED_ADMIN** TOE Administrators are trusted to follow and apply all administrator guidance in a
trusted manner.


**MOD_BIO_V1.1**
**OE.Protection** The TOE environment shall provide the SEE to protect the TOE, the TOE configuration and
biometric data during runtime and storage.


**MOD_MDM_AGENT_V1.0**
**OE.DATA_PROPER_ADMIN** TOE Administrators are trusted to follow and apply all administrator
guidance in a trusted manner.


**OE.DATA_PROPER_USER** Users of the mobile device are trained to securely use the mobile device and
apply all guidance in a trusted manner.


**OE.IT_ENTERPRISE** The Enterprise IT infrastructure provides security for a network that is available to
the TOE and mobile devices that prevents unauthorized access.


**OE.MOBILE_DEVICE_PLATFORM** The MDM Agent relies upon the trustworthy mobile platform and
hardware to provide policy enforcement as well as cryptographic services and data protection. The
mobile platform provides trusted updates and software integrity verification of the MDM Agent.


14 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


**OE.WIRELESS_NETWORK** A wireless network will be available to the mobile devices.


15 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025

## 4 Extended Components Definition


All of the extended requirements in this ST are drawn from the MDF/BT/WLANC/BIO/TLS/MDMA. The
MDF/BT/WLANC/BIO/TLS/MDMA defines the following extended requirements and since they are not
redefined in this ST, the MDF/BT/WLANC/BIO/TLS/MDMA should be consulted for more information
about those CC extensions.

|Extended SFR|Name|
|---|---|
|FCS_CKM_EXT.1|Cryptographic Key Support|
|FCS_CKM_EXT.2|Cryptographic Key Random Generation|
|FCS_CKM_EXT.3|Cryptographic Key Generation|
|FCS_CKM_EXT.4|Key Destruction|
|FCS_CKM_EXT.5|TSF Wipe|
|FCS_CKM_EXT.6|Salt Generation|
|FCS_HTTPS_EXT.1|HTTPS Protocol|
|FCS_IV_EXT.1|Initialization Vector Generation|
|FCS_RBG_EXT.1|Random Bit Generation|
|FCS_SRV_EXT.1|Cryptographic Algorithm Services|
|FCS_SRV_EXT.2|Cryptographic Algorithm Services|
|FCS_STG_EXT.1|Cryptographic Key Storage|
|FCS_STG_EXT.2|Encrypted Cryptographic Key Storage|
|FCS_STG_EXT.3|Integrity of Encrypted Key Storage|
|FDP_ACF_EXT.1|Security Access Control for System Services|
|FDP_ACF_EXT.2|Security Access Control for System Resources|
|FDP_DAR_EXT.1|Protected Data Encryption|
|FDP_DAR_EXT.2|Sensitive Data Encryption|
|FDP_IFC_EXT.1|Subset Information Flow Control|
|FDP_STG_EXT.1|User Data Storage|
|FDP_UPC_EXT.1/APPS|Inter-TSF User Data Transfer Protection (Applications)|
|FDP_UPC_EXT.1/BLUETOOTH|Inter-TSF User Data Transfer Protection (Bluetooth)|
|FIA_AFL_EXT.1|Authentication Failure Handling|
|FIA_PMG_EXT.1|Password Management|
|FIA_TRT_EXT.1|Authentication Throttling|
|FIA_UAU_EXT.1|Authentication for Cryptographic Operation|
|FIA_UAU_EXT.2|Timing of Authentication|
|FIA_X509_EXT.1|Validation of Certificates|
|FIA_X509_EXT.2|X509 Certificate Authentication|
|FIA_X509_EXT.3|Request Validation of Certificates|
|FMT_MOF_EXT.1|Management of Security Functions Behavior|
|FMT_SMF_EXT.2|Specification of Remediation Actions|
|FMT_SMF_EXT.3|Current Administrator|
|FPT_AEX_EXT.1|Application Address Space Layout Randomization|
|FPT_AEX_EXT.2|Memory Page Permissions|
|FPT_AEX_EXT.3|Stack Overflow Protection|
|FPT_AEX_EXT.4|Domain Isolation|
|FPT_AEX_EXT.5|Kernel Address Space Layout Randomization|
|FPT_BBD_EXT.1|Application Processor Mediation|



16 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025

|Extended SFR|Name|
|---|---|
|FPT_JTA_EXT.1|JTAG Disablement|
|FPT_KST_EXT.1|Key Storage|
|FPT_KST_EXT.2|No Key Transmission|
|FPT_KST_EXT.3|No Plaintext Key Export|
|FPT_NOT_EXT.1|Self-Test Notification|
|FPT_TST_EXT.1|TSF Cryptographic Functionality Testing|
|FPT_TST_EXT.2/PREKERNEL|TSF Integrity Checking (Pre-Kernel)|
|FPT_TST_EXT.2/POSTKERNEL|TSF Integrity Checking (Post-Kernel)|
|FPT_TUD_EXT.1|Trusted Update: TSF Version Query|
|FPT_TUD_EXT.2|TSF Update Verification|
|FPT_TUD_EXT.3|Application Signing|
|FPT_TUD_EXT.6|Trusted Update Verification|
|FTA_SSL_EXT.1|TSF- and User-initiated Locked State|
|FTP_ITC_EXT.1|Trusted Channel Communication|



_**Table 4 - PP_MDF_V3.3 Extended Components**_

|Extended SFR|Name|
|---|---|
|FCS_CKM_EXT.8|Bluetooth Key Generation|
|FIA_BLT_EXT.1|Bluetooth User Authorization|
|FIA_BLT_EXT.2|Bluetooth Mutual Authentication|
|FIA_BLT_EXT.3|Rejection of Duplicate Bluetooth Connections|
|FIA_BLT_EXT.4|Secure Simple Pairing|
|FIA_BLT_EXT.6|Trusted Bluetooth User Authorization|
|FIA_BLT_EXT.7|Untrusted Bluetooth User Authorization|
|FMT_SMF.1 [modified]|Specification of Management Functions|
|FMT_SMF_EXT.1/BT|Specification of Management Functions|
|FTP_BLT_EXT.1|Bluetooth Encryption|
|FTP_BLT_EXT.2|Persistence of Bluetooth Encryption|
|FTP_BLT_EXT.3/BR|Bluetooth Encryption Parameters (BR/EDR)|
|FTP_BLT_EXT.3/LE|Bluetooth Encryption Parameters (LE)|



_**Table 5 - MOD_BT_V1.0 Extended Components**_

|Extended SFR|Name|
|---|---|
|FCS_TLSC_EXT.1/WLAN|TLS Client Protocol (EAP-TLS for WLAN)|
|FCS_TLSC_EXT.2/WLAN|TLS Client Support for Supported Groups Extension (EAP-TLS<br>for WLAN)|
|FCS_WPA_EXT.1|Supported WPA Versions|
|FIA_PAE_EXT.1|Port Access Entity Authentication|
|FIA_X509_EXT.1/WLAN|X.509 Certificate Validation|
|FIA_X509_EXT.2/WLAN|X.509 Certificate Authentication (EAP-TLS for WLAN)|
|FIA_X509_EXT.6|X.509 Certificate Storage and Management|
|FPT_TST_EXT.3/WLAN|TSF Cryptographic Functionality Testing (WLAN Client)|
|FTA_WSE_EXT.1|Wireless Network Access|



_**Table 6 - MOD_WLANC_V1.0 Extended Components**_

|Extended SFR|Name|
|---|---|
|FIA_MBE_EXT.1|Biometric enrolment|
|FIA_MBE_EXT.2|Quality of biometric templates for biometric enrolment|



17 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025

|Extended SFR|Name|
|---|---|
|FIA_MBV_EXT.1/PBFPS|Biometric verification|
|FIA_MBV_EXT.1/UDFPS|Biometric verification|
|FIA_MBV_EXT.1/USFPS|Biometric verification|
|FIA_MBV_EXT.2|Quality of biometric samples for biometric verification|
|FIA_MBV_EXT.3|Presentation attack detection for biometric verification|
|FPT_BDP_EXT.1|Biometric data processing|
|FPT_KST_EXT.1 [modified]|Key Storage|
|FPT_KST_EXT.2 [modified]|No Key Transmission|
|FPT_PBT_EXT.1|Protection of biometric template|



_**Table 7 - MOD_BIO_V1.1 Extended Components**_

|Extended SFR|Name|
|---|---|
|FCS_TLS_EXT.1|TLS Protocol|
|FCS_TLSC_EXT.1|TLS Client Protocol|
|FCS_TLSC_EXT.2|TLS Client Support for Mutual Authentication|
|FCS_TLSC_EXT.4|TLS Client Support for Renegotiation|
|FCS_TLSC_EXT.5|TLS Client Support for Supported Groups Extension|



_**Table 8 - PKG_TLS_V1.1 Extended Components**_

|Extended SFR|Name|
|---|---|
|FAU_ALT_EXT.2|Alert Agents|
|FCS_STG_EXT.4|Cryptographic Key Storage|
|FIA_ENR_EXT.2|Agent Enrollment of Mobile Device into Management|
|FMT_POL_EXT.2|Agent Trusted Policy Update|
|FMT_SMF_EXT.4|Specification of Management Functions|
|FMT_UNR_EXT.1|User Unenrollment Prevention|
|FTP_ITC_EXT.1(2)|Trusted Channel Communication|



_**Table 9 - MOD_MDM_AGENT_V1.0 Extended Components**_

|Extended SAR|Name|
|---|---|
|ALC_TSU_EXT.1|Timely Security Updates|



_**Table 10 - PP_MDF_V3.3 Extended Assurance Components**_


18 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025

## 5 Security Requirements


This section defines the Security Functional Requirements (SFRs) and Security Assurance Requirements
(SARs) that serve to represent the security functional claims for the Target of Evaluation (TOE) and to
scope the evaluation effort.


The SFRs are from the MDF/BT/WLANC/BIO/TLS/MDMA documents. The refinements and operations
already performed in the MDF/BT/WLANC/BIO/TLS/MDMA are not identified (e.g., highlighted) here,
rather the requirements have been copied from the MDF/BT/WLANC/BIO/TLS/MDMA and any residual
operations have been completed herein. Of particular note, the MDF/BT/WLANC/BIO/TLS/MDMA made
a number of refinements and completed some of the SFR operations defined in the Common Criteria
(CC) and that PP should be consulted to identify those changes if necessary.


The SARs are from the MDF/BT/WLANC/BIO/TLS/MDMA documents, and includes all the relevant SARs.
The SARs are effectively refined since requirement-specific 'Evaluation Activities' are defined in the
MDF/BT/WLANC/BIO/TLS/MDMA that serve to ensure corresponding evaluations will yield more
practical and consistent assurance. The MDF/BT/WLANC/BIO/TLS/MDMA should be consulted for the
assurance activity definitions.

### 5.1 TOE Security Functional Requirements


The following table identifies the SFRs that are satisfied by Google Pixel Devices on Android 15 TOE.





|Requirement Class|PP|Requirement Component|
|---|---|---|
|FAU: Security Audit|MOD_MDM_AGENT_V1.0|FAU_ALT_EXT.2 Alert Agents|
|FAU: Security Audit|PP_MDF_V3.3|FAU_GEN.1 Audit Data Generation|
|FAU: Security Audit|MOD_BT_V1.0|FAU_GEN.1/BT Audit Data Generation (Bluetooth)|
|FAU: Security Audit|MOD_WLANC_V1.0|FAU_GEN.1/WLAN Audit Data Generation (Wireless LAN)|
|FAU: Security Audit|MOD_MDM_AGENT_V1.0|FAU_GEN.1(2) Audit Data Generation|
|FAU: Security Audit|PP_MDF_V3.3|FAU_SAR.1 Audit Review|
|FAU: Security Audit|MOD_MDM_AGENT_V1.0|FAU_SEL.1(2) Security Audit Event Selection|
|FAU: Security Audit|PP_MDF_V3.3|FAU_STG.4 Prevention of Audit Data Loss|
|FAU: Security Audit|PP_MDF_V3.3|FAU_STG.1 Audit Storage Protection|
|FCS: Cryptographic<br>Support|PP_MDF_V3.3|FCS_CKM.1 Cryptographic Key Generation|
|FCS: Cryptographic<br>Support|MOD_WLANC_V1.0|FCS_CKM.1/WPA Cryptographic Key Generation<br>(Symmetric Keys for WPA2/WPA3 Connections)|
|FCS: Cryptographic<br>Support|PP_MDF_V3.3|FCS_CKM.2/UNLOCKED Cryptographic Key Establishment|
|FCS: Cryptographic<br>Support|PP_MDF_V3.3|FCS_CKM.2/LOCKED Cryptographic Key Establishment|
|FCS: Cryptographic<br>Support|MOD_WLANC_V1.0|FCS_CKM.2/WLAN Cryptographic Key Distribution (Group<br>Temporal Key for WLAN)|
|FCS: Cryptographic<br>Support|PP_MDF_V3.3|FCS_CKM_EXT.1 Cryptographic Key Support|
|FCS: Cryptographic<br>Support|PP_MDF_V3.3|FCS_CKM_EXT.2 Cryptographic Key Random Generation|
|FCS: Cryptographic<br>Support|PP_MDF_V3.3|FCS_CKM_EXT.3 Cryptographic Key Generation|
|FCS: Cryptographic<br>Support|PP_MDF_V3.3|FCS_CKM_EXT.4 Key Destruction|
|FCS: Cryptographic<br>Support|PP_MDF_V3.3|FCS_CKM_EXT.5 TSF Wipe|
|FCS: Cryptographic<br>Support|PP_MDF_V3.3|FCS_CKM_EXT.6 Salt Generation|
|FCS: Cryptographic<br>Support|MOD_BT_V1.0|FCS_CKM_EXT.8 Bluetooth Key Generation|
|FCS: Cryptographic<br>Support|PP_MDF_V3.3|FCS_COP.1/ENCRYPT Cryptographic operation|


19 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025





|Requirement Class|PP|Requirement Component|
|---|---|---|
||PP_MDF_V3.3|FCS_COP.1/HASH Cryptographic operation|
||PP_MDF_V3.3|FCS_COP.1/SIGN Cryptographic operation|
||PP_MDF_V3.3|FCS_COP.1/KEYHMAC Cryptographic operation|
||PP_MDF_V3.3|FCS_COP.1/CONDITION Cryptographic operation|
||PP_MDF_V3.3|FCS_HTTPS_EXT.1 HTTPS Protocol|
||PP_MDF_V3.3|FCS_IV_EXT.1 Initialization Vector Generation|
||PP_MDF_V3.3|FCS_RBG_EXT.1 Random Bit Generation|
||PP_MDF_V3.3|FCS_SRV_EXT.1 Cryptographic Algorithm Services|
||PP_MDF_V3.3|FCS_SRV_EXT.2 Cryptographic Algorithm Services|
||PP_MDF_V3.3|FCS_STG_EXT.1 Cryptographic Key Storage|
||PP_MDF_V3.3|FCS_STG_EXT.2 Encrypted Cryptographic Key Storage|
||PP_MDF_V3.3|FCS_STG_EXT.3 Integrity of Encrypted Key Storage|
||MOD_MDM_AGENT_V1.0|FCS_STG_EXT.4 Cryptographic Key Storage|
||PKG_TLS_V1.1|FCS_TLS_EXT.1 TLS Protocol|
||PKG_TLS_V1.1|FCS_TLSC_EXT.1 TLS Client Protocol|
||PKG_TLS_V1.1|FCS_TLSC_EXT.4 TLS Client Support for Renegotiation|
||PKG_TLS_V1.1|FCS_TLSC_EXT.2 TLS Client Support for Mutual<br>Authentication|
||PKG_TLS_V1.1|FCS_TLSC_EXT.5 TLS Client Support for Supported Groups<br>Extension|
||MOD_WLANC_V1.0|FCS_TLSC_EXT.1/WLAN TLS Client Protocol (EAP-TLS for<br>WLAN)|
||MOD_WLANC_V1.0|FCS_TLSC_EXT.2/WLAN TLS Client Support for Supported<br>Groups Extension (EAP-TLS for WLAN)|
||MOD_WLANC_V1.0|FCS_WPA_EXT.1 Supported WPA Versions|
|FDP: User Data<br>Protection|PP_MDF_V3.3|FDP_ACF_EXT.1 Security Access Control for System Services|
|FDP: User Data<br>Protection|PP_MDF_V3.3|FDP_ACF_EXT.2 Security Access Control for System<br>Resources|
|FDP: User Data<br>Protection|PP_MDF_V3.3|FDP_DAR_EXT.1 Protected Data Encryption|
|FDP: User Data<br>Protection|PP_MDF_V3.3|FDP_DAR_EXT.2 Sensitive Data Encryption|
|FDP: User Data<br>Protection|PP_MDF_V3.3|FDP_IFC_EXT.1 Subset Information Flow Control|
|FDP: User Data<br>Protection|PP_MDF_V3.3|FDP_STG_EXT.1 User Data Storage|
|FDP: User Data<br>Protection|PP_MDF_V3.3|FDP_UPC_EXT.1/APPS Inter-TSF User Data Transfer<br>Protection (Applications)|
|FDP: User Data<br>Protection|PP_MDF_V3.3|FDP_UPC_EXT.1/BLUETOOTH Inter-TSF User Data Transfer<br>Protection (Bluetooth)|
|FIA: Identification &<br>Authentication|PP_MDF_V3.3|FIA_AFL_EXT.1 Authentication Failure Handling|
|FIA: Identification &<br>Authentication|MOD_BT_V1.0|FIA_BLT_EXT.1 Bluetooth User Authorization|
|FIA: Identification &<br>Authentication|MOD_BT_V1.0|FIA_BLT_EXT.2 Bluetooth Mutual Authentication|
|FIA: Identification &<br>Authentication|MOD_BT_V1.0|FIA_BLT_EXT.3 Rejection of Duplicate Bluetooth<br>Connections|
|FIA: Identification &<br>Authentication|MOD_BT_V1.0|FIA_BLT_EXT.4 Secure Simple Pairing|
|FIA: Identification &<br>Authentication|MOD_BT_V1.0|FIA_BLT_EXT.6 Trusted Bluetooth User Authorization|
|FIA: Identification &<br>Authentication|MOD_BT_V1.0|FIA_BLT_EXT.7 Untrusted Bluetooth User Authorization|
|FIA: Identification &<br>Authentication|MOD_MDM_AGENT_V1.0|FIA_ENR_EXT.2 Agent Enrollment of Mobile Device into<br>Management|
|FIA: Identification &<br>Authentication|MOD_BIO_V1.1|FIA_MBE_EXT.1 Biometric enrolment|


20 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025





|Requirement Class|PP|Requirement Component|
|---|---|---|
||MOD_BIO_V1.1|FIA_MBE_EXT.2 Quality of biometric templates for<br>biometric enrolment|
||MOD_BIO_V1.1|FIA_MBV_EXT.1/PBFPS Biometric verification|
||MOD_BIO_V1.1|FIA_MBV_EXT.1/UDFPS Biometric verification|
||MOD_BIO_V1.1|FIA_MBV_EXT.1/USFPS Biometric verification|
||MOD_BIO_V1.1|FIA_MBV_EXT.2 Quality of biometric samples for biometric<br>verification|
||MOD_BIO_V1.1|FIA_MBV_EXT.3 Presentation attack detection for<br>biometric verification|
||MOD_WLANC_V1.0|FIA_PAE_EXT.1 Port Access Entity Authentication|
||PP_MDF_V3.3|FIA_PMG_EXT.1 Password Management|
||PP_MDF_V3.3|FIA_TRT_EXT.1 Authentication Throttling|
||PP_MDF_V3.3|FIA_UAU.5 Multiple Authentication Mechanisms|
||PP_MDF_V3.3|FIA_UAU.6/CREDENTIAL Re-Authentication (Credential<br>Change)|
||PP_MDF_V3.3|FIA_UAU.6/LOCKED Re-Authentication (TSF Lock)|
||PP_MDF_V3.3|FIA_UAU.7 Protected Authentication Feedback|
||PP_MDF_V3.3|FIA_UAU_EXT.1 Authentication for Cryptographic<br>Operation|
||PP_MDF_V3.3|FIA_UAU_EXT.2 Timing of Authentication|
||PP_MDF_V3.3|FIA_X509_EXT.1 Validation of Certificates|
||PP_MDF_V3.3|FIA_X509_EXT.3 Request Validation of Certificates|
||PP_MDF_V3.3|FIA_X509_EXT.2 X509 Certificate Authentication|
||MOD_WLANC_V1.0|FIA_X509_EXT.1/WLAN X.509 Certificate Validation|
||MOD_WLANC_V1.0|FIA_X509_EXT.2/WLAN X.509 Certificate Authentication<br>(EAP-TLS for WLAN)|
||MOD_WLANC_V1.0|FIA_X509_EXT.6 X.509 Certificate Storage and<br>Management|
|FMT: Security<br>Management|PP_MDF_V3.3|FMT_MOF_EXT.1 Management of Security Functions<br>Behavior|
|FMT: Security<br>Management|MOD_MDM_AGENT_V1.0|FMT_POL_EXT.2 Agent Trusted Policy Update|
|FMT: Security<br>Management|PP_MDF_V3.3|FMT_SMF.1 Specification of Management Functions|
|FMT: Security<br>Management|MOD_BT_V1.0|FMT_SMF_EXT.1/BT Specification of Management<br>Functions|
|FMT: Security<br>Management|MOD_WLANC_V1.0|FMT_SMF.1/WLAN Specification of Management Functions<br>(WLAN Client)|
|FMT: Security<br>Management|PP_MDF_V3.3|FMT_SMF_EXT.2 Specification of Remediation Actions|
|FMT: Security<br>Management|PP_MDF_V3.3|FMT_SMF_EXT.3 Current Administrator|
|FMT: Security<br>Management|MOD_MDM_AGENT_V1.0|FMT_SMF_EXT.4 Specification of Management Functions|
|FMT: Security<br>Management|MOD_MDM_AGENT_V1.0|FMT_UNR_EXT.1 User Unenrollment Prevention|
|FPT: Protection of<br>the TSF|PP_MDF_V3.3|FPT_AEX_EXT.1 Application Address Space Layout<br>Randomization|
|FPT: Protection of<br>the TSF|PP_MDF_V3.3|FPT_AEX_EXT.2 Memory Page Permissions|
|FPT: Protection of<br>the TSF|PP_MDF_V3.3|FPT_AEX_EXT.3 Stack Overflow Protection|
|FPT: Protection of<br>the TSF|PP_MDF_V3.3|FPT_AEX_EXT.4 Domain Isolation|
|FPT: Protection of<br>the TSF|PP_MDF_V3.3|FPT_AEX_EXT.5 Kernel Address Space Layout<br>Randomization|


21 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025





|Requirement Class|PP|Requirement Component|
|---|---|---|
||PP_MDF_V3.3|FPT_BBD_EXT.1 Application Processor Mediation|
||MOD_BIO_V1.1|FPT_BDP_EXT.1 Biometric data processing|
||PP_MDF_V3.3|FPT_JTA_EXT.1 JTAG Disablement|
||PP_MDF_V3.3 &<br>MOD_BIO_V1.1|FPT_KST_EXT.1 Key Storage|
||PP_MDF_V3.3 &<br>MOD_BIO_V1.1|FPT_KST_EXT.2 No Key Transmission|
||PP_MDF_V3.3|FPT_KST_EXT.3 No Plaintext Key Export|
||PP_MDF_V3.3|FPT_NOT_EXT.1 Self-Test Notification|
||MOD_BIO_V1.1|FPT_PBT_EXT.1 Protection of biometric template|
||PP_MDF_V3.3|FPT_STM.1 Reliable time stamps|
||PP_MDF_V3.3|FPT_TST_EXT.1 TSF Cryptographic Functionality Testing|
||PP_MDF_V3.3|FPT_TST_EXT.2/PREKERNEL TSF Integrity Checking (Pre-<br>Kernel)|
||PP_MDF_V3.3|FPT_TST_EXT.2/POSTKERNEL TSF Integrity Checking (Post-<br>Kernel)|
||MOD_WLANC_V1.0|FPT_TST_EXT.3/WLAN TSF Cryptographic Functionality<br>Testing (WLAN Client)|
||PP_MDF_V3.3|FPT_TUD_EXT.1 Trusted Update: TSF Version Query|
||PP_MDF_V3.3|FPT_TUD_EXT.2 TSF Update Verification|
||PP_MDF_V3.3|FPT_TUD_EXT.3 Application Signing|
||PP_MDF_V3.3|FPT_TUD_EXT.6 Trusted Update Verification|
|FTA: TOE Access|PP_MDF_V3.3|FTA_SSL_EXT.1 TSF- and User-initiated Locked State|
|FTA: TOE Access|PP_MDF_V3.3|FTA_TAB.1 Default TOE Access Banners|
|FTA: TOE Access|MOD_WLANC_V1.0|FTA_WSE_EXT.1 Wireless Network Access|
|FTP: Trusted<br>Path/Channels|MOD_BT_V1.0|FTP_BLT_EXT.1 Bluetooth Encryption|
|FTP: Trusted<br>Path/Channels|MOD_BT_V1.0|FTP_BLT_EXT.2 Persistence of Bluetooth Encryption|
|FTP: Trusted<br>Path/Channels|MOD_BT_V1.0|FTP_BLT_EXT.3/BR Bluetooth Encryption Parameters<br>(BR/EDR)|
|FTP: Trusted<br>Path/Channels|MOD_BT_V1.0|FTP_BLT_EXT.3/LE Bluetooth Encryption Parameters (LE)|
|FTP: Trusted<br>Path/Channels|MOD_WLANC_V1.0|FTP_ITC.1/WLAN Trusted Channel Communication<br>(Wireless LAN)|
|FTP: Trusted<br>Path/Channels|PP_MDF_V3.3|FTP_ITC_EXT.1 Trusted Channel Communication|
|FTP: Trusted<br>Path/Channels|MOD_MDM_AGENT_V1.0|FTP_ITC_EXT.1(2) Trusted Channel Communication|
|FTP: Trusted<br>Path/Channels|MOD_MDM_AGENT_V1.0|FTP_TRP.1(2) Trusted Path (for Enrollment)|


_**Table 11 - TOE Security Functional Components**_



5.1.1 Security Audit (FAU)


_5.1.1.1_ _MOD_MDM_AGENT_V1.0:FAU_ALT_EXT.2 Agent Alerts_


**FAU_ALT_EXT.2.1**

The MDM Agent shall provide an alert via the trusted channel to the MDM Server in the
event of any of the following audit events:

       - successful application of policies to a mobile device,

       - [ _**receiving**_ ] periodic reachability events,


22 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


       - [ _**no other events**_ ].
**FAU_ALT_EXT.2.2**

The MDM Agent shall queue alerts if the trusted channel is not available.


_5.1.1.2_ _PP_MDF_V3.3:FAU_GEN.1 Audit Data Generation_


**FAU_GEN.1.1**

The TSF shall be able to generate an audit record of the following auditable events:
1. Start-up and shutdown of the audit functions
2. All auditable events for the [ _not selected_ ] level of audit
_3._ [ _All administrative actions_
_4._ _Start-up and shutdown of the OS_
_5._ _Insertion or removal of removable media_
_6._ _Specifically defined auditable events in Table 2 of the PP_MDF_V3.3_
7. [ _**no additional auditable events**_ ]







|Requirement|Audit Event|Content|
|---|---|---|
|FAU_GEN.1|Start-up and shutdown of the<br>audit functions||
|FAU_GEN.1|All administrative actions||
|FAU_GEN.1|Start-up and shutdown of the Rich<br>OS||
|FAU_GEN.1|||
|FAU_SAR.1|||
|FAU_STG.1|||
|FAU_STG.4|||
|FCS_CKM.1|[**_None_**].||
|FCS_CKM.2/UNLOCKED|||
|FCS_CKM.2/LOCKED|||
|FCS_CKM_EXT.1|[**_None_**]||
|FCS_CKM_EXT.2|||
|FCS_CKM_EXT.3|||
|FCS_CKM_EXT.4|||
|FCS_CKM_EXT.5|[**_None_**]||
|FCS_CKM_EXT.6|||
|FCS_COP.1/ENCRYPT|||
|FCS_COP.1/HASH|||
|FCS_COP.1/SIGN|||
|FCS_COP.1/KEYHMAC|||
|FCS_COP.1/CONDITION|||
|FCS_IV_EXT.1|||
|FCS_SRV_EXT.1|||
|FCS_STG_EXT.1|Import or destruction of key.|Identity of key. Role and identity<br>of requestor.|
|FCS_STG_EXT.1|[**_None_**]||
|FCS_STG_EXT.2|||


23 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025

|Requirement|Audit Event|Content|
|---|---|---|
|FCS_STG_EXT.3|Failure to verify integrity of stored<br>key.|Identity of key being verified.|
|FDP_ACF_EXT.1|||
|FDP_DAR_EXT.1|[**_None_**]||
|FDP_DAR_EXT.2|[**_None_**]||
|FDP_IFC_EXT.1|||
|FDP_STG_EXT.1|Addition or removal of certificate<br>from Trust Anchor Database.|Subject name of certificate.|
|FIA_PMG_EXT.1|||
|FIA_TRT_EXT.1|||
|FIA_UAU.5|||
|FIA_UAU.7|||
|FIA_UAU_EXT.1|||
|FIA_X509_EXT.1|Failure to validate X.509v3<br>certificate.|Reason for failure of validation.|
|FIA_X509_EXT.2|||
|FMT_MOF_EXT.1|||
|FPT_AEX_EXT.1|||
|FPT_AEX_EXT.2|||
|FPT_AEX_EXT.3|||
|FPT_JTA_EXT.1|||
|FPT_KST_EXT.1|||
|FPT_KST_EXT.2|||
|FPT_KST_EXT.3|||
|FPT_NOT_EXT.1|[**_None_**]|[**_No additional information_**]|
|FPT_STM.1|||
|FPT_TST_EXT.1|Initiation of self-test.||
|FPT_TST_EXT.1|Failure of self-test.|[**_No additional information_**]|
|FPT_TST_EXT.2/PREKERNEL|Start-up of TOE.||
|FPT_TST_EXT.2/PREKERNEL|[**_None_**]|[**_No additional information_**]|
|FPT_TUD_EXT.1|||
|FTA_SSL_EXT.1|||
|FTA_TAB.1|||



_**Table 12 - PP_MDF_V3.3 Audit Events**_


**FAU_GEN.1.2**

The TSF shall record within each audit record at least the following information:
1. Date and time of the event
2. Type of event
3. Subject identity
4. The outcome (success or failure) of the event
5. Additional information in Table 12 - PP_MDF_V3.3 Audit Events from Table 2 (of the

PP_MDF_V3.3)
6. [ _**no additional information**_ ]
(TD0724 applied)


24 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


_5.1.1.3_ _MOD_BT_V1.0:FAU_GEN.1/BT Audit Data Generation (Bluetooth)_


**FAU_GEN.1.1/BT**

The TSF shall be able to generate an audit record of the following auditable events:
a. Start-up and shutdown of the audit functions
b. All auditable events for the [ _not specified_ ] level of audit
c. [ _Specifically defined auditable events in the Auditable Events table (of the_

_MOD_BT_V1.0 shown in Table 13 - MOD_BT_V1.0 Audit Events)_ ]









|Requirement|Audit Event|Content|
|---|---|---|
|FCS_CKM_EXT.8|||
|FIA_BLT_EXT.1|Failed user authorization of<br>Bluetooth device.|User authorization decision (e.g.,<br>user rejected connection,<br>incorrect pin entry).|
|FIA_BLT_EXT.1|Failed user authorization for local<br>Bluetooth Service.|[_last [2] octets of the_] BD_ADDR<br>and [**_no other information_**].<br> <br>Bluetooth profile. Identity of local<br>service with [**_service ID_**].|
|FIA_BLT_EXT.2|Initiation of Bluetooth connection.|[_last [2] octets of the_] BD_ADDR<br>and [**_no other information_**].|
|FIA_BLT_EXT.2|Failure of Bluetooth connection.|Reason for failure.|
|FIA_BLT_EXT.4|||
|FIA_BLT_EXT.6|||
|FIA_BLT_EXT.7|||
|FTP_BLT_EXT.1|||
|FTP_BLT_EXT.2|||
|FTP_BLT_EXT.3/BR|||
|FTP_BLT_EXT.3/LE|||


_**Table 13 - MOD_BT_V1.0 Audit Events**_


**FAU_GEN.1.2/BT**

The TSF shall record within each audit record at least the following information:
a. Date and time of the event
b. Type of event
c. Subject identity
d. The outcome (success or failure) of the event
e. For each audit event type, based on the auditable event definitions of the functional

components included in the PP/ST, [ _Additional information in the Auditable Events_
_table (of the MOD_BT_V1.0)_ ]
(TD0707 & TD0645 applied)


_5.1.1.4_ _MOD_WLANC_V1.0:FAU_GEN.1/WLAN Audit Data Generation (Wireless LAN)_


**FAU_GEN.1.1/WLAN**

The TSF shall [ _**invoke platform-provided functionality**_ ] to generate an audit record of
the following auditable events:


25 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


a. Startup and shutdown of the audit functions;
b. All auditable events for the [ _not specified_ ] level of audit; and
c. [ _all auditable events for mandatory SFRs specified in Table 2 and selected SFRs in_

_Table 5 (of the MOD_WLANC_V1.0 shown in Table 14 - MOD_WLANC_V1.0 Audit_
_Events)_ ]

















|Requirement|Audit Event|Content|
|---|---|---|
|FAU_GEN.1/WLAN|||
|FCS_CKM.1/WPA|||
|FCS_CKM.2/WLAN|||
|FCS_TLSC_EXT.1/WLAN|Failure to establish an EAP-TLS<br>session.|Reason for failure.<br>Non-TOE endpoint of connection.|
|FCS_TLSC_EXT.1/WLAN|Establishment/termination of an<br>EAP-TLS session.|Non-TOE endpoint of connection.|
|FCS_TLSC_EXT.2/WLAN|||
|FCS_WPA_EXT.1|||
|FIA_PAE_EXT.1|||
|FIA_X509_EXT.1/WLAN|Failure to validate X.509v3<br>certificate.|Reason for failure of validation.|
|FIA_X509_EXT.2/WLAN|||
|FIA_X509_EXT.6|Attempts to load certificates.||
|FIA_X509_EXT.6|Attempts to revoke certificates.||
|FMT_SMF.1/WLAN|||
|FPT_TST_EXT.3/WLAN|Execution of this set of TSF self-<br>tests.|(Done as part of FPT_TST_EXT.1)|
|FPT_TST_EXT.3/WLAN|[**_None_**].|[**_None_**].|
|FTA_WSE_EXT.1|All attempts to connect to access<br>points.|For each access point record the<br>[**_Complete SSID and MAC_**] of the<br>MAC Address<br> <br>Success and failures (including<br>reason for failure).|
|FTP_ITC.1/WLAN|All attempts to establish a trusted<br>channel.|Identification of the non-TOE<br>endpoint of the channel.|


_**Table 14 - MOD_WLANC_V1.0 Audit Events**_


**FAU_GEN.1.2/WLAN**

The [ _**TOE Platform**_ ] shall record within each audit record at least the following
information:
a. Date and time of the event, type of event, subject identity, (if relevant) the outcome

(success or failure) of the event; and
b. For each audit event type, based on the auditable event definitions of the functional

components included in the PP-Module/ST, [ _Additional Audit Record Contents as_
_specified in Table 2 and Table 5 (of the MOD_WLANC_V1.0 shown in Table 14 -_
_MOD_WLANC_V1.0 Audit Events)_ ]


26 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


_5.1.1.5_ _MOD_MDM_AGENT_V1.0:FAU_GEN.1(2) Audit Data Generation (MDM Agent)_


**FAU_GEN.1.1(2)**

**Refinement:** The MDM Agent shall [ _**invoke platform-provided functionality**_ ] to
generate an MDM Agent audit record of the following auditable events:
a. Startup and shutdown of the MDM Agent;
b. All auditable events for [ _not specified_ ] level of audit; and
c. [ _MDM policy updated, any modification commanded by the MDM Server, specifically_

_defined auditable events listed in Table 1 (of the MOD_MDM_AGENT_V1.0 shown in_
_Table 15 - MOD_MDM_AGENT_V1.0 Audit Events, and [_ _**no other events**_ _]_ ].















|Requirement|Auditable Events|Additional Audit Record Contents|
|---|---|---|
|FAU_ALT_EXT.2|Success/failure of sending alert.|No additional information.|
|FAU_GEN.1(2)|None.|N/A|
|FAU_SEL.1(2)|All modifications to the audit<br>configuration that occur while the<br>audit collection functions are<br>operating.|No additional information.|
|FCS_STG_EXT.4/<br>FCS_STG_EXT.1(2)|None.||
|FCS_TLSC_EXT.1|Failure to establish a TLS session.|Reason for failure.|
|FCS_TLSC_EXT.1|Failure to verify presented<br>identifier.|Presented identifier and reference<br>identifier.|
|FCS_TLSC_EXT.1|Establishment/termination of a<br>TLS session.|Non-TOE endpoint of connection.|
|FIA_ENR_EXT.2|Enrollment in management.|Reference identifier of MDM<br>Server.|
|FMT_POL_EXT.2|Failure of policy validation.|Reason for failure of validation.|
|FMT_SMF_EXT.4|Outcome (Success/failure) of<br>function.|No additional information.|
|FMT_UNR_EXT.1.1|[**_Attempt to unenroll_**]|No additional information.|
|FTP_ITC_EXT.1(2)|Initiation and termination of<br>trusted channel.|Trusted channel protocol. Non-<br>TOE endpoint of connection.|


_**Table 15 - MOD_MDM_AGENT_V1.0 Audit Events**_


**FAU_GEN.1.2(2)**

**Refinement:** The [ _**TOE platform**_ ] shall record within each MDM Agent audit record at
least the following information:
a. Date and time of the event, type of event, subject identity, (if relevant) the outcome

(success or failure) of the event, and additional information in Table 1 ( _of the_
_MOD_MDM_AGENT_V1.0 shown in Table 15 - MOD_MDM_AGENT_V1.0 Audit_
_Events)_ ; and
b. For each audit event type, based on the auditable event definitions of the functional

components included in the PP-Module/ST, [ _no other information_ ].
(TD0660 applied)


27 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


_5.1.1.6_ _PP_MDF_V3.3:FAU_SAR.1 Audit Review_


**FAU_SAR.1.1**

The TSF shall provide [the administrator] with the capability to read [all audited events
and record contents] from the audit records.
**FAU_SAR.1.2**

The TSF shall provide the audit records in a manner suitable for the user to interpret the
information.


_5.1.1.7_ _MOD_MDM_AGENT_V1.0:FAU_SEL.1(2) Security Audit Event Selection_


**FAU_SEL.1.1(2)**

**Refinement:** The TSF shall [ _**invoke platform-provided functionality**_ ] to select the set of
events to be audited from the set of all auditable events based on the following
attributes:
a. [ _event type_ ]
b. [ _success of auditable security events, failure of auditable security events, [no other_

_attributes]_ ].


_5.1.1.8_ _PP_MDF_V3.3:FAU_STG.1 Audit Storage Protection_


**FAU_STG.1.1**

The TSF shall protect the stored audit records in the audit trail from unauthorized
deletion.
**FAU_STG.1.2**

The TSF shall be able to [prevent] unauthorized modifications to the stored audit
records in the audit trail.


_5.1.1.9_ _PP_MDF_V3.3:FAU_STG.4 Prevention of Audit Data Loss_


**FAU_STG.4.1**

The TSF shall [overwrite the oldest stored audit records] if the audit trail is full.


5.1.2 Cryptographic Support (FCS)


_5.1.2.1_ _PP_MDF_V3.3:FCS_CKM.1 Cryptographic Key Generation_


**FCS_CKM.1.1**

The TSF shall generate asymmetric cryptographic keys in accordance with a specified
cryptographic key generation algorithm [

       - _**RSA schemes using cryptographic key sizes of [2048, 3072 or 4096 bits] that meet**_

_**[FIPS PUB 186-5, “Digital Signature Standard (DSS)”, Appendix B.3],**_

       - _**ECC schemes using [“NIST curves” P-384 and [P-256, P-521] that meet the**_
_**following: [FIPS PUB 186-5, “Digital Signature Standard (DSS)”, Appendix B.4]]**_
].
(TD0871 applied)


28 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


_5.1.2.2_ _MOD_WLANC_V1.0:FCS_CKM.1/WPA Cryptographic Key Generation (Symmetric Keys_
_for WPA2/WPA3 Connections)_


**FCS_CKM.1.1/WPA**

The TSF shall generate symmetric cryptographic keys in accordance with a specified
cryptographic key generation algorithm [ _**PRF-384 and [PRF-512, PRF-704] (as defined in**_
_**IEEE 802.11-2012)**_ ] and specified key sizes [ _**256 bits and [128 bits, 192 bits]**_ ] using a
Random Bit Generator as specified in FCS_RBG_EXT.1.


_5.1.2.3_ _PP_MDF_V3.3:FCS_CKM.2/UNLOCKED Cryptographic Key Establishment_


**FCS_CKM.2.1/UNLOCKED**

The TSF shall perform cryptographic key establishment in accordance with a specified
cryptographic key establishment method [

       - _**[RSA-based key establishment schemes] that meet the following [**_

`o` _**NIST Special Publication 800-56B, “Recommendation for Pair-Wise Key**_

_**Establishment Schemes Using Integer Factorization Cryptography”]**_

       - _**[Elliptic curve-based key establishment schemes] that meet the following: [NIST**_
_**Special Publication 800-56A Revision 3, “Recommendation for Pair-Wise Key**_
_**Establishment Schemes Using Discrete Logarithm Cryptography”]**_
].


_5.1.2.4_ _PP_MDF_V3.3:FCS_CKM.2/LOCKED Cryptographic Key Establishment_


**FCS_CKM.2.1/LOCKED**

The TSF shall perform cryptographic key establishment in accordance with a specified
cryptographic key establishment method: [

       - _**[RSA-based key establishment schemes] that meet the following: [NIST Special**_
_**Publication 800-56B, “Recommendation for Pair-Wise Key Establishment Schemes**_
_**Using Integer Factorization Cryptography”]**_
] for the purposes of encrypting sensitive data received while the device is locked.


_5.1.2.5_ _MOD_WLANC_V1.0:FCS_CKM.2/WLAN Cryptographic Key Distribution (Group_
_Temporal Key for WLAN)_


**FCS_CKM.2.1/WLAN**


The TSF shall decrypt Group Temporal Key in accordance with a specified cryptographic
key distribution method [ _AES Key Wrap (as defined in RFC 3394) in an EAPOL-Key frame_
_(as defined in IEEE 802.11-2012 for the packet format and timing considerations_ ] and
does not expose the cryptographic keys.


_5.1.2.6_ _PP_MDF_V3.3:FCS_CKM_EXT.1 Cryptographic Key Support_


**FCS_CKM_EXT.1.1**

The TSF shall support [ _**immutable hardware**_ ] REKs with a [ _**symmetric**_ ] key of strength

[ _**256 bits**_ ].


29 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


**FCS_CKM_EXT.1.2**

Each REK shall be hardware-isolated from the OS on the TSF in runtime.
**FCS_CKM_EXT.1.3**

Each REK shall be generated by an RBG in accordance with FCS_RBG_EXT.1.


_5.1.2.7_ _PP_MDF_V3.3:FCS_CKM_EXT.2 Cryptographic Key Random Generation_


**FCS_CKM_EXT.2.1**

All DEKs shall be [

       - _**randomly generated**_
] with entropy corresponding to the security strength of AES key sizes of [ _**256**_ ] bits.


_5.1.2.8_ _PP_MDF_V3.3:FCS_CKM_EXT.3 Cryptographic Key Generation_


**FCS_CKM_EXT.3.1**

The TSF shall use [

       - _**asymmetric KEKs of [128 bits] security strength,**_

       - _**symmetric KEKs of [256-bit] security strength corresponding to at least the security**_
_**strength of the keys encrypted by the KEK**_
].
**FCS_CKM_EXT.3.2**

The TSF shall generate all KEKs using one of the following methods:

       - Derive the KEK from a Password Authentication Factor according to
FCS_COP.1.1/CONDITION and

[

       - _**Generate the KEK using an RBG that meets this profile (as specified in**_
_**FCS_RBG_EXT.1)**_

       - _**Generate the KEK using a key generation scheme that meets this profile (as**_
_**specified in FCS_CKM.1)**_

       - _**Combine the KEK from other KEKs in a way that preserves the effective entropy of**_
_**each factor by [concatenating the keys and using a KDF (as described in SP 800-**_
_**108), encrypting one key with another]**_
].


_5.1.2.9_ _PP_MDF_V3.3:FCS_CKM_EXT.4 Key Destruction_


**FCS_CKM_EXT.4.1**

The TSF shall destroy cryptographic keys in accordance with the specified cryptographic
key destruction methods:

       - by clearing the KEK encrypting the target key

       - in accordance with the following rules

`o` For volatile memory, the destruction shall be executed by a single direct

overwrite [ _**consisting of zeroes**_ ].


30 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


`o` For non-volatile EEPROM, the destruction shall be executed by a single direct

overwrite consisting of a pseudo random pattern using the TSF's RBG (as
specified in FCS_RBG_EXT.1), followed by a read-verify.

`o` For non-volatile flash memory, that is not wear-leveled, the destruction shall be

executed [ _**by a block erase that erases the reference to memory that stores**_
_**data as well as the data itself**_ ].

`o` For non-volatile flash memory, that is wear-leveled, the destruction shall be

executed [ _**by a block erase**_ ].

`o` For non-volatile memory other than EEPROM and flash, the destruction shall be

executed by a single direct overwrite with a random pattern that is changed
before each write.
**FCS_CKM_EXT.4.2**

The TSF shall destroy all plaintext keying material and critical security parameters when
no longer needed.


_5.1.2.10_ _PP_MDF_V3.3:FCS_CKM_EXT.5 TSF Wipe_


**FCS_CKM_EXT.5.1**

The TSF shall wipe all protected data by [

       - _**Cryptographically erasing the encrypted DEKs or the KEKs in non-volatile memory**_
_**by following the requirements in FCS_CKM_EXT.4.1**_

       - _**Overwriting all PD according to the following rules:**_

`o` _**For EEPROM, the destruction shall be executed by a single direct overwrite**_

_**consisting of a pseudo random pattern using the TSF's RBG (as specified in**_
_**FCS_RBG_EXT.1), followed by a read-verify.**_

`o` _**For flash memory, that is not wear-leveled, the destruction shall be executed**_

_**[by a block erase that erases the reference to memory that stores data as well**_
_**as the data itself].**_

`o` _**For flash memory, that is wear-leveled, the destruction shall be executed [by a**_

_**block erase].**_

`o` _**For non-volatile memory other than EEPROM and flash, the destruction shall**_

_**be executed by a single direct overwrite with a random pattern that is**_
_**changed before each write.**_ ].
**FCS_CKM_EXT.5.2**

The TSF shall perform a power cycle on conclusion of the wipe procedure.


_5.1.2.11_ _PP_MDF_V3.3:FCS_CKM_EXT.6 Salt Generation_


**FCS_CKM_EXT.6.1**

The TSF shall generate all salts using an RBG that meets FCS_RBG_EXT.1.


_5.1.2.12_ _MOD_BT_V1.0:FCS_CKM_EXT.8 Bluetooth Key Generation_


**FCS_CKM_EXT.8.1**

The TSF shall generate public/private ECDH key pairs every [ **time a connection between**
**devices is established** ].


31 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


_5.1.2.13_ _PP_MDF_V3.3:FCS_COP.1/ENCRYPT Cryptographic operation_


**FCS_COP.1.1/ENCRYPT**

The TSF shall perform [encryption/decryption] in accordance with a specified
cryptographic algorithm:

       - AES-CBC (as defined in FIPS PUB 197, and NIST SP 800-38A) mode

       - AES-CCMP (as defined in FIPS PUB 197, NIST SP 800-38C and IEEE 802.11-2012), and

       - [

`o` _**AES Key Wrap (KW) (as defined in NIST SP 800-38F)**_

`o` _**AES-GCM (as defined in NIST SP 800-38D)**_

`o` _**AES-XTS (as defined in NIST SP 800-38E) mode**_

`o` _**AES-GCMP-256 (as defined in NIST SP800-38D and IEEE 802.11ac-2013)**_
]
and cryptographic key sizes [128-bit key sizes and [ _**256-bit key sizes**_ ]].


_5.1.2.14_ _PP_MDF_V3.3:FCS_COP.1/HASH Cryptographic operation_


**FCS_COP.1.1/HASH**

The TSF shall perform [cryptographic hashing] in accordance with a specified
cryptographic algorithm [SHA-1 and [ _**SHA-256, SHA-384, SHA-512**_ ]] and message digest
sizes [160 and [ _**256 bits, 384 bits, 512 bits**_ ]] that meet the following: [FIPS Pub 180-4].


_5.1.2.15_ _PP_MDF_V3.3:FCS_COP.1/SIGN Cryptographic operation_


**FCS_COP.1.1/SIGN**

The TSF shall perform [cryptographic signature services (generation and verification)] in
accordance with a specified cryptographic algorithm [

       - _**[RSA schemes] using cryptographic key sizes of [2048-bit or greater] that meet the**_
_**following: [FIPS PUB 186-5, “Digital Signature Standard (DSS)”, Section 4]**_

       - _**[ECDSA schemes] using [“NIST curves” P-384 and [P-256, P-521]] that meet the**_
_**following: [FIPS PUB 186-5, “Digital Signature Standard (DSS)”, Section 5]**_
].
(TD0871 applied)


_5.1.2.16_ _PP_MDF_V3.3:FCS_COP.1/KEYHMAC Cryptographic operation_


**FCS_COP.1.1/KEYHMAC**

The TSF shall perform [keyed-hash message authentication] in accordance with a
specified cryptographic algorithm [HMAC-SHA-1 and [ _**HMAC-SHA-256, HMAC-SHA-384,**_
_**HMAC-SHA-512**_ ]] and cryptographic key sizes [ **160, 256, 384, 512** ] and message digest
sizes 160 and [ _**256, 384, 512**_ ] bits that meet the following: [FIPS Pub 198-1, “The KeyedHash Message Authentication Code”, and FIPS Pub 180-4, “Secure Hash Standard”].


_5.1.2.17_ _PP_MDF_V3.3:FCS_COP.1/CONDITION Cryptographic operation_


**FCS_COP.1.1/CONDITION**


32 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


The TSF shall perform conditioning in accordance with a specified cryptographic
algorithm HMAC-[ _**SHA-256**_ ] using a salt, and [ _**[key stretching with scrypt]**_ ] and output
cryptographic key sizes [ _**256**_ ] that meet the following: [ _**no standard**_ ].


_5.1.2.18_ _PP_MDF_V3.3:FCS_HTTPS_EXT.1 HTTPS Protocol_


**FCS_HTTPS_EXT.1.1**

The TSF shall implement the HTTPS protocol that complies with RFC 2818.
**FCS_HTTPS_EXT.1.2**

The TSF shall implement HTTPS using TLS as defined in [the Functional Package for
Transport Layer Security (TLS), version 1.1].
**FCS_HTTPS_EXT.1.3**

The TSF shall notify the application and [ _**not establish the connection**_ ] if the peer
certificate is deemed invalid.


_5.1.2.19_ _PP_MDF_V3.3:FCS_IV_EXT.1 Initialization Vector Generation_


**FCS_IV_EXT.1.1**

The TSF shall generate IVs in accordance with [Table 11: References and IV
Requirements for NIST-approved Cipher Modes].


_5.1.2.20_ _PP_MDF_V3.3:FCS_RBG_EXT.1 Random Bit Generation_


**FCS_RBG_EXT.1.1**

The TSF shall perform all deterministic random bit generation services in accordance
with NIST Special Publication 800-90A using [ _**HMAC_DRBG (any), CTR_DRBG (AES)**_ ].
**FCS_RBG_EXT.1.2**

The deterministic RBG shall be seeded by an entropy source that accumulates entropy
from [ _**TSF-hardware-based noise source**_ ] with a minimum of [ _**256 bits**_ ] of entropy at
least equal to the greatest security strength (according to NIST SP 800-57) of the keys
and hashes that it will generate.
**FCS_RBG_EXT.1.3**

The TSF shall be capable of providing output of the RBG to applications running on the
TSF that request random bits.


_5.1.2.21_ _PP_MDF_V3.3:FCS_SRV_EXT.1 Cryptographic Algorithm Services_


**FCS_SRV_EXT.1.1**

The TSF shall provide a mechanism for applications to request the TSF to perform the
following cryptographic operations: [

       - All mandatory and [ _**selected algorithms**_ ] in FCS_CKM.2/LOCKED

       - The following algorithms in FCS_COP.1/ENCRYPT: AES-CBC, [ _**AES-GCM**_ ]

       - All selected algorithms in FCS_COP.1/SIGN

       - All mandatory and selected algorithms in FCS_COP.1/HASH

       - All mandatory and selected algorithms in FCS_COP.1/KEYHMAC

       - [ _**All mandatory and [selected algorithms] in FCS_CKM.1**_ ]


33 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


].


_5.1.2.22_ _PP_MDF_V3.3:FCS_SRV_EXT.2 Cryptographic Algorithm Services_


**FCS_SRV_EXT.2.1**

The TSF shall provide a mechanism for applications to request the TSF to perform the
following cryptographic operations: [

       - Algorithms in FCS_COP.1/ENCRYPT

       - Algorithms in FCS_COP.1/SIGN
] by keys stored in the secure key storage.


_5.1.2.23_ _PP_MDF_V3.3:FCS_STG_EXT.1 Cryptographic Key Storage_


**FCS_STG_EXT.1.1**

The TSF shall provide [ _**mutable hardware**_ _**[2]**_ _**, software-based**_ ] secure key storage for
asymmetric private keys and [ _**symmetric keys, persistent secrets**_ ].
**FCS_STG_EXT.1.2**

The TSF shall be capable of importing keys or secrets into the secure key storage upon
request of [ _**the user, the administrator**_ ] and [ _**applications running on the TSF**_ ].
**FCS_STG_EXT.1.3**

The TSF shall be capable of destroying keys or secrets in the secure key storage upon
request of [ _**the user, the administrator**_ ].
**FCS_STG_EXT.1.4**

The TSF shall have the capability to allow only the application that imported the key or
secret the use of the key or secret. Exceptions may only be explicitly authorized by [ _**a**_
_**common application developer**_ ].
**FCS_STG_EXT.1.5**

The TSF shall allow only the application that imported the key or secret to request that
the key or secret be destroyed. Exceptions may only be explicitly authorized by [ _**a**_
_**common application developer**_ ].


_5.1.2.24_ _PP_MDF_V3.3:FCS_STG_EXT.2 Encrypted Cryptographic Key Storage_


**FCS_STG_EXT.2.1**

The TSF shall encrypt all DEKs, KEKs, [ **WPA2/WPA3 PSK, Bluetooth Keys** ] and [ _**all**_
_**software-based key storage**_ ] by KEKs that are [

       - _**Protected by the REK with [**_

`o` _**encryption by a KEK chaining from a REK**_

`o` _**encryption by a KEK that is derived from a REK]**_

       - _**Protected by the REK and the password with [**_

`o` _**encryption by a KEK chaining to a REK and the password-derived or biometric-**_

_**unlocked KEK**_


2 Does not apply to the Pixel 6 Pro/6/6a

34 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


`o` _**encryption by a KEK that is derived from a REK and the password-derived or**_

_**biometric-unlocked KEK]**_
].
**FCS_STG_EXT.2.2**

DEKs, KEKs, [ **WPA2/WPA3 PSK, Bluetooth Keys** ] and [ _**all software-based key storage**_ ]
shall be encrypted using one of the following methods: [

       - _**using a SP800-56B key establishment scheme**_

       - _**using AES in the [GCM, CCM mode]**_
].


_5.1.2.25_ _PP_MDF_V3.3:FCS_STG_EXT.3 Integrity of Encrypted Key Storage_


**FCS_STG_EXT.3.1**

The TSF shall protect the integrity of any encrypted DEKs and KEKs and [ _**long-term**_
_**trusted channel key material, all software-based key storage**_ ] by [

       - _**[GCM, CCM] cipher mode for encryption according to FCS_STG_EXT.2**_
].
**FCS_STG_EXT.3.2**

The TSF shall verify the integrity of the [ _**MAC**_ ] of the stored key prior to use of the key.


_5.1.2.26_ _MOD_MDM_AGENT_V1.0:FCS_STG_EXT.4 Cryptographic Key Storage_


**FCS_STG_EXT.4.1**

The MDM Agent shall use the platform provided key storage for all persistent secret and
private keys.


_5.1.2.27_ _PKG_TLS_V1.1:FCS_TLS_EXT.1 TLS Protocol_


**FCS_TLS_EXT.1.1**

The product shall implement [

       - _**TLS as a client**_
].


_5.1.2.28_ _PKG_TLS_V1.1:FCS_TLSC_EXT.1 TLS Client Protocol_


**FCS_TLSC_EXT.1.1**

The product shall implement TLS 1.2 (RFC 5246) and [ _**no earlier TLS versions**_ ] as a client
that supports the cipher suites [

       - _**TLS_RSA_WITH_AES_128_GCM_SHA256 as defined in RFC 5288,**_

       - _**TLS_RSA_WITH_AES_256_GCM_SHA384 as defined in RFC 5288,**_

       - _**TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256 as defined in RFC 5289,**_

       - _**TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384 as defined in RFC 5289,**_

       - _**TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256 as defined in RFC 5289,**_

       - _**TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384 as defined in RFC 5289**_
] and also supports functionality for [


35 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


       - _**mutual authentication**_

       - _**session renegotiation**_
].
**FCS_TLSC_EXT.1.2**

The TSF shall verify that the presented identifier matches the reference identifier
according to RFC 6125.
**FCS_TLSC_EXT.1.3**

The TSF shall not establish a trusted channel if the server certificate is invalid [

       - _**with no exceptions**_
].
(TD0442 applied)


_5.1.2.29_ _PKG_TLS_V1.1:FCS_TLSC_EXT.2 TLS Client Support for Mutual Authentication_


**FCS_TLSC_EXT.2.1**

The product shall support mutual authentication using X.509v3 certificates.


_5.1.2.30_ _PKG_TLS_V1.1:FCS_TLSC_EXT.4 TLS Client Support for Renegotiation_


**FCS_TLSC_EXT.4.1**

The product shall support secure renegotiation through use of the “renegotiation_info”
TLS extension in accordance with RFC 5746.


_5.1.2.31_ _PKG_TLS_V1.1:FCS_TLSC_EXT.5 TLS Client Support for Supported Groups Extension_


**FCS_TLSC_EXT.5.1**

The product shall present the Supported Groups Extension in the Client Hello with the
supported groups [

       - _**secp256r1,**_

       - _**secp384r1**_
].


_5.1.2.32_ _MOD_WLANC_V1.0:FCS_TLSC_EXT.1/WLAN TLS Client Protocol (EAP-TLS for WLAN)_


**FCS_TLSC_EXT.1.1/WLAN**

The product shall implement TLS 1.2 (RFC 5246) and [ _**TLS 1.1 (RFC 4346)**_ ] in support of
the EAP-TLS protocol as specified in RFC 5216 supporting the following cipher suites: [

       - _**TLS_RSA_WITH_AES_128_CBC_SHA as defined in RFC 5246,**_

       - _**TLS_RSA_WITH_AES_256_GCM_SHA384 as defined in RFC 5288,**_

       - _**TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256 as defined in RFC 5289,**_

       - _**TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384 as defined in RFC 5289,**_

       - _**TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256 as defined in RFC 5289,**_

       - _**TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384 as defined in RFC 5289**_
].


36 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


**FCS_TLSC_EXT.1.2/WLAN**

The TSF shall generate random values used in the EAP-TLS exchange using the RBG
specified in FCS_RBG_EXT.1.
**FCS_TLSC_EXT.1.3/WLAN**

The TSF shall use X509 v3 certificates as specified in FIA_X509_EXT.1/WLAN.
**FCS_TLSC_EXT.1.4/WLAN**

The TSF shall verify that the server certificate presented includes the Server
Authentication purpose (id-kp 1 with OID 1.3.6.1.5.5.7.3.1) in the extendedKeyUsage
field.
**FCS_TLSC_EXT.1.5/WLAN**

The TSF shall allow an authorized administrator to configure the list of CAs that are
allowed to sign authentication server certificates that are accepted by the TOE.


_5.1.2.33_ _MOD_WLANC_V1.0:FCS_TLSC_EXT.2/WLAN TLS Client Support for Supported Groups_
_Extension (EAP-TLS for WLAN)_


**FCS_TLSC_EXT.2.1/WLAN**

The TSF shall present the Supported Groups Extension in the Client Hello with the
following NIST curves: [ _**secp256r1, secp384r1**_ ].


_5.1.2.34_ _MOD_WLANC_V1.0:FCS_WPA_EXT.1 Supported WPA Versions_


**FCS_WPA_EXT.1.1**

The TSF shall support WPA3 and [ _**WPA2**_ ] security type.


5.1.3 User Data Protection (FDP)


_5.1.3.1_ _PP_MDF_V3.3:FDP_ACF_EXT.1 Security Access Control for System Services_


**FDP_ACF_EXT.1.1**

The TSF shall provide a mechanism to restrict the system services that are accessible to
an application.
**FDP_ACF_EXT.1.2**

The TSF shall provide an access control policy that prevents [ _**application, groups of**_
_**applications**_ ] from accessing [ _**all**_ ] data stored by other [ _**application, groups of**_
_**applications**_ ]. Exceptions may only be explicitly authorized for such sharing by [ _**a**_
_**common application developer (for sharing between applications), no one (for sharing**_
_**between personal and enterprise profiles)**_ ].


_5.1.3.2_ _PP_MDF_V3.3:FDP_ACF_EXT.2 Security Access Control for System Resources_


**FDP_ACF_EXT.2.1**

The TSF shall provide a separate [ _**address book, calendar, [keychain]**_ ] for each
application group and only allow applications within that process group to access the
resource. Exceptions may only be explicitly authorized for such sharing by [ _**the**_
_**administrator (for address book), no one (for calendar, keychain)**_ ].


37 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


_5.1.3.3_ _PP_MDF_V3.3:FDP_DAR_EXT.1 Protected Data Encryption_


**FDP_DAR_EXT.1.1**

Encryption shall cover all protected data.
**FDP_DAR_EXT.1.2**

Encryption shall be performed using DEKs with AES in the [ _**XTS**_ ] mode with key size [ _**256**_ ]
bits.


_5.1.3.4_ _PP_MDF_V3.3:FDP_DAR_EXT.2 Sensitive Data Encryption_


**FDP_DAR_EXT.2.1**

The TSF shall provide a mechanism for applications to mark data and keys as sensitive.
**FDP_DAR_EXT.2.2**

The TSF shall use an asymmetric key scheme to encrypt and store sensitive data
received while the product is locked.
**FDP_DAR_EXT.2.3**

The TSF shall encrypt any stored symmetric key and any stored private key of the
asymmetric keys used for the protection of sensitive data according to

[FCS_STG_EXT.2.1 selection 2].
**FDP_DAR_EXT.2.4**

The TSF shall decrypt the sensitive data that was received while in the locked state upon
transitioning to the unlocked state using the asymmetric key scheme and shall reencrypt that sensitive data using the symmetric key scheme.


_5.1.3.5_ _PP_MDF_V3.3:FDP_IFC_EXT.1 Subset Information Flow Control_


**FDP_IFC_EXT.1.1**

The TSF shall [ _**provide an interface which allows a VPN client to protect all IP traffic**_
_**using IPsec**_ ] with the exception of IP traffic needed to manage the VPN connection, and

[ _**traffic needed to determine if the network connection has connectivity to the internet**_
_**and responses to local ICMP echo requests on the local subnet**_ ], when the VPN is
enabled.


_5.1.3.6_ _PP_MDF_V3.3:FDP_STG_EXT.1 User Data Storage_


**FDP_STG_EXT.1.1**

The TSF shall provide protected storage for the Trust Anchor Database.


_5.1.3.7_ _PP_MDF_V3.3:FDP_UPC_EXT.1/APPS Inter-TSF User Data Transfer Protection_
_(Applications)_


**FDP_UPC_EXT.1.1/APPS**

The TSF shall provide a means for non-TSF applications executing on the TOE to use [

       - Mutually authenticated TLS as defined in the Functional Package for Transport Layer
Security (TLS), version 1.1,

       - HTTPS
and [


38 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


       - _**no other protocol**_
]] to provide a protected communication channel between the non-TSF application and
another IT product that is logically distinct from other communication channels,
provides assured identification of its end points, protects channel data from disclosure,
and detects modification of the channel data.
**FDP_UPC_EXT.1.2/APPS**

The TSF shall permit the non-TSF applications to initiate communication via the trusted
channel.


_5.1.3.8_ _PP_MDF_V3.3:FDP_UPC_EXT.1/BLUETOOTH Inter-TSF User Data Transfer Protection_
_(Bluetooth)_


**FDP_UPC_EXT.1.1/BLUETOOTH**

The TSF shall provide a means for non-TSF applications executing on the TOE to use [

       - Bluetooth BR/EDR in accordance with the PP-Module for Bluetooth, version 1.0,
and [

       - _**Bluetooth LE in accordance with the PP-Module for Bluetooth, version 1.0**_
]] to provide a protected communication channel between the non-TSF application and
another IT product that is logically distinct from other communication channels,
provides assured identification of its end points, protects channel data from disclosure,
and detects modification of the channel data.
**FDP_UPC_EXT.1.2/BLUETOOTH**

The TSF shall permit the non-TSF applications to initiate communication via the trusted
channel.


5.1.4 Identification and Authentication (FIA)


_5.1.4.1_ _PP_MDF_V3.3:FIA_AFL_EXT.1 Authentication Failure Handling_


**FIA_AFL_EXT.1.1**

The TSF shall consider password and [ _**no other mechanism**_ ] as critical authentication
mechanisms.
**FIA_AFL_EXT.1.2**

The TSF shall detect when a configurable positive integer within [ **0 and 50** ] of [ _**non-**_
_**unique**_ ] unsuccessful authentication attempts occur related to last successful
authentication for each authentication mechanism.
**FIA_AFL_EXT.1.3**

The TSF shall maintain the number of unsuccessful authentication attempts that have
occurred upon power off.
**FIA_AFL_EXT.1.4**

When the defined number of unsuccessful authentication attempts has exceeded the
maximum allowed for a given authentication mechanism, all future authentication
attempts will be limited to other available authentication mechanisms, unless the given
mechanism is designated as a critical authentication mechanism.
**FIA_AFL_EXT.1.5**


39 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


When the defined number of unsuccessful authentication attempts for the last available
authentication mechanism or single critical authentication mechanism has been
surpassed, the TSF shall perform a wipe of all protected data.
**FIA_AFL_EXT.1.6**

The TSF shall increment the number of unsuccessful authentication attempts prior to
notifying the user that the authentication was unsuccessful.


_5.1.4.2_ _MOD_BT_V1.0:FIA_BLT_EXT.1 Bluetooth User Authorization_


**FIA_BLT_EXT.1.1**

The TSF shall require explicit user authorization before pairing with a remote Bluetooth
device.


_5.1.4.3_ _MOD_BT_V1.0:FIA_BLT_EXT.2 Bluetooth Mutual Authentication_


**FIA_BLT_EXT.2.1**

The TSF shall require Bluetooth mutual authentication between devices prior to any
data transfer over the Bluetooth link.


_5.1.4.4_ _MOD_BT_V1.0:FIA_BLT_EXT.3 Rejection of Duplicate Bluetooth Connections_


**FIA_BLT_EXT.3.1**

The TSF shall discard pairing and session initialization attempts from a Bluetooth device
address (BD_ADDR) to which an active session already exists.


_5.1.4.5_ _MOD_BT_V1.0:FIA_BLT_EXT.4 Secure Simple Pairing_


**FIA_BLT_EXT.4.1**

The TOE shall support Bluetooth Secure Simple Pairing, both in the host and the
controller.
**FIA_BLT_EXT.4.2**

The TOE shall support Secure Simple Pairing during the pairing process.


_5.1.4.6_ _MOD_BT_V1.0:FIA_BLT_EXT.6 Trusted Bluetooth User Authorization_


**FIA_BLT_EXT.6.1**

The TSF shall require explicit user authorization before granting trusted remote devices
access to services associated with the following Bluetooth profiles: [ _**OPP, MAP**_ ].


_5.1.4.7_ _MOD_BT_V1.0:FIA_BLT_EXT.7 Untrusted Bluetooth User Authorization_


**FIA_BLT_EXT.7.1**

The TSF shall require explicit user authorization before granting untrusted remote
devices access to services associated with the following Bluetooth profiles: [ _**OPP, MAP**_ ].


_5.1.4.8_ _MOD_MDM_AGENT_V1.0:FIA_ENR_EXT.2 Agent Enrollment of Mobile Device into_
_Management_


**FIA_ENR_EXT.2.1**


40 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


The MDM Agent shall record the reference identifier of the MDM Server during the
enrollment process.


_5.1.4.9_ _MOD_BIO_V1.1:FIA_MBE_EXT.1 Biometric enrolment_


**FIA_MBE_EXT.1.1**

The TSF shall provide a mechanism to enrol an authenticated user to the biometric
system.
(TD0714 applied)


_5.1.4.10_ _MOD_BIO_V1.1:FIA_MBE_EXT.2 Quality of biometric templates for biometric_
_enrolment_


**FIA_MBE_EXT.2.1**

The TSF shall only use biometric samples of sufficient quality for enrolment. Sufficiency
of sample data shall be determined by measuring sample with [ _**the ability to meet the**_
_**Android 15 CDD Class 3 biometric requirements**_ ].


_5.1.4.11_ _MOD_BIO_V1.1:FIA_MBV_EXT.1/PBFPS Biometric verification_


This applies to the Pixel 9 Pro Fold, Tablet, Fold devices.
**FIA_MBV_EXT.1.1/PBFPS**

The TSF shall provide a biometric verification mechanism using [ _**fingerprint**_ ].
**FIA_MBV_EXT.1.2/PBFPS**

The TSF shall provide a biometric verification mechanism with the [ _**FAR**_ ] not exceeding

[ **1:50,000** ] for the upper bound of [ **95%** ] confidence interval and, [ _**FRR**_ ] not exceeding

[ **3%** ] for the upper bound of [ **95%** ] confidence interval.


_5.1.4.12_ _MOD_BIO_V1.1:FIA_MBV_EXT.1/UDFPS Biometric verification_


This applies to the Pixel 8 Pro/8/8a, 7 Pro/7/7a, 6 Pro/6/6a devices.
**FIA_MBV_EXT.1.1/UDFPS**

The TSF shall provide a biometric verification mechanism using [ _**fingerprint**_ ].
**FIA_MBV_EXT.1.2/UDFPS**

The TSF shall provide a biometric verification mechanism with the [ _**FAR**_ ] not exceeding

[ **1:50,000** ] for the upper bound of [ **95%** ] confidence interval and, [ _**FRR**_ ] not exceeding

[ **3%** ] for the upper bound of [ **95%** ] confidence interval.


_5.1.4.13_ _MOD_BIO_V1.1:FIA_MBV_EXT.1/USFPS Biometric verification_


This applies to the Pixel 9 Pro XL/9 Pro/9 devices.
**FIA_MBV_EXT.1.1/USFPS**

The TSF shall provide a biometric verification mechanism using [ _**fingerprint**_ ].
**FIA_MBV_EXT.1.2/USFPS**

The TSF shall provide a biometric verification mechanism with the [ _**FAR**_ ] not exceeding

[ **1:50,000** ] for the upper bound of [ **95%** ] confidence interval and, [ _**FRR**_ ] not exceeding

[ **3%** ] for the upper bound of [ **95%** ] confidence interval.


41 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


_5.1.4.14_ _MOD_BIO_V1.1:FIA_MBV_EXT.2 Quality of biometric samples for biometric_
_verification_


**FIA_MBV_EXT.2.1**

The TSF shall only use biometric samples of sufficient quality for verification. Sufficiency
of sample data shall be determined by measuring sample with [ _**the ability to meet**_ _**the**_
_**Android 15 CDD Class 3 biometric requirements**_ ].


_5.1.4.15_ _MOD_BIO_V1.1:FIA_MBV_EXT.3 Presentation attack detection for biometric_
_verification_


**FIA_MBV_EXT.3.1**

The TSF shall provide a biometric verification mechanism with the IAPAR not exceeding

[ **7%** ] to prevent use of artificial presentation attack instruments from being successfully
verified.


_5.1.4.16_ _MOD_WLANC_V1.0:FIA_PAE_EXT.1 Port Access Entity Authentication_


**FIA_PAE_EXT.1.1**

The TSF shall conform to IEEE Standard 802.1X for a Port Access Entity (PAE) in the
“Supplicant” role.


_5.1.4.17_ _PP_MDF_V3.3:FIA_PMG_EXT.1 Password Management_


**FIA_PMG_EXT.1.1**

The TSF shall support the following for the Password Authentication Factor:
1. Passwords shall be able to be composed of any combination of _**[upper and lower**_

_**case letters**_ ], numbers, and special characters: [ _**! @ # $ % ^ & * ( ) [= + - _ ` ~ \ | ] } [**_
_**{ ‘ “ ; : / ? . >, < ]**_ ]
2. Password length up to [ _**16**_ ] characters shall be supported.


_5.1.4.18_ _PP_MDF_V3.3:FIA_TRT_EXT.1 Authentication Throttling_


**FIA_TRT_EXT.1.1**

The TSF shall limit automated user authentication attempts by [ _**enforcing a delay**_
_**between incorrect authentication attempts**_ ] for all authentication mechanisms selected
in FIA_UAU.5.1. The minimum delay shall be such that no more than 10 attempts can be
attempted per 500 milliseconds.


_5.1.4.19_ _PP_MDF_V3.3:FIA_UAU.5 Multiple Authentication Mechanisms_


**FIA_UAU.5.1**

The TSF shall provide password and [ _**biometric in accordance with the Biometric**_
_**Enrollment and Verification, version 1.1**_ ] to support user authentication.
**FIA_UAU.5.2**

The TSF shall authenticate any user's claimed identity according to the [ _**following rules:**_
_**To authenticate unlocking the device immediately after boot (first unlock after**_
_**reboot):**_


42 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


       - _**User passwords are required after reboot to unlock the user's Credential encrypted**_
_**(CE files) and keystore keys. Biometric authentication is disabled immediately after**_
_**boot.**_
_**To authenticate unlocking the device after device lock (not following a reboot):**_

       - _**The TOE verifies user credentials (password or fingerprint) via the gatekeeper or**_
_**fingerprint trusted application (running inside the Trusted Execution Environment,**_
_**TEE), which compares the entered credential to a derived value or template.**_
_**To change protected settings or issue certain commands:**_

       - _**The TOE requires password after a reboot, when changing settings (Screen lock,**_
_**Fingerprint, and Smart Lock settings), and when factory resetting.**_
].


_5.1.4.20_ _PP_MDF_V3.3:FIA_UAU.6/CREDENTIAL Re-Authentication (Credential Change)_


**FIA_UAU.6.1/CREDENTIAL**

The TSF shall re-authenticate the user via the Password Authentication Factor under the
conditions [attempted change to any supported authentication mechanisms].


_5.1.4.21_ _PP_MDF_V3.3:FIA_UAU.6/LOCKED Re-Authentication (TSF Lock)_


**FIA_UAU.6.1/LOCKED**

The TSF shall re-authenticate the user via an authentication factor defined in
FIA_UAU.5.1 under the conditions TSF-initiated lock, user-initiated lock, [ **no other**
**conditions** ].


_5.1.4.22_ _PP_MDF_V3.3:FIA_UAU.7 Protected Authentication Feedback_


**FIA_UAU.7.1**

The TSF shall provide only [obscured feedback to the device's display] to the user while
the authentication is in progress.


_5.1.4.23_ _PP_MDF_V3.3:FIA_UAU_EXT.1 Authentication for Cryptographic Operation_


**FIA_UAU_EXT.1.1**

The TSF shall require the user to present the Password Authentication Factor prior to
decryption of protected data and encrypted DEKs, KEKs and [ _**all software-based key**_
_**storage**_ ] at startup.


_5.1.4.24_ _PP_MDF_V3.3:FIA_UAU_EXT.2 Timing of Authentication_


**FIA_UAU_EXT.2.1**

The TSF shall allow **[** _**[**_

       - _**Take screen shots (stored internally)**_

       - _**Make emergency calls**_

       - _**Receive calls**_

       - _**Take pictures (stored internally) - unless the camera was disabled**_

       - _**Turn the TOE off**_


43 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


       - _**Restart the TOE**_

       - _**Place TOE into lockdown mode**_

       - _**Adjust screen brightness**_

       - _**See notifications (note that some notifications identify actions, for example to**_
_**view a screenshot; however, selecting those notifications highlights the password**_
_**prompt and require the password to access that data)**_

       - _**Configure sound, vibrate, or mute**_

       - _**Change keyboard input method**_

       - _**Access widgets (without authentication):**_

`o` _**Internet toggle (Wi-Fi and Mobile/Cellular data)**_

`o` _**Bluetooth toggle**_

`o` _**Airplane Mode toggle**_

`o` _**Flashlight toggle**_

`o` _**Do not disturb toggle**_

`o` _**Auto rotate toggle**_

`o` _**Sound (on, mute, vibrate)**_

`o` _**Night light filter toggle**_

`o` _**Live captions toggle**_

`o` _**Battery Saver toggle**_

`o` _**Hotspot toggle (using what has already been configured)**_

`o` _**Color inversion toggle**_

`o` _**Data Saver toggle**_

`o` _**Dark Theme toggle**_

`o` _**One-handed mode toggle**_

`o` _**Extra dim toggle**_

`o` _**Font Size toggle**_

`o` _**Screen saver toggle**_

`o` _**Color correction toggle**_

`o` _**Calculator app**_
_**]**_ ] on behalf of the user to be performed before the user is authenticated.
**FIA_UAU_EXT.2.2**

The TSF shall require each user to be successfully authenticated before allowing any
other TSF-mediated actions on behalf of that user.


_5.1.4.25_ _PP_MDF_V3.3:FIA_X509_EXT.1 Validation of Certificates_


**FIA_X509_EXT.1.1**

The TSF shall validate certificates in accordance with the following rules:

       - RFC 5280 certificate validation and certificate path validation.

       - The certificate path must terminate with a certificate in the Trust Anchor Database.


44 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


       - The TSF shall validate a certificate path by ensuring the presence of the
basicConstraints extension, that the CA flag is set to TRUE for all CA certificates, and
that any path constraints are met.

       - The TSF shall validate that any CA certificate includes caSigning purpose in the key
usage field.

       - The TSF shall validate the revocation status of the certificate using [ _**OCSP as**_
_**specified in RFC 6960**_ ].

       - The TSF shall validate the extendedKeyUsage field according to the following rules:

`o` Certificates used for trusted updates and executable code integrity verification

shall have the Code Signing purpose (id-kp 3 with OID 1.3.6.1.5.5.7.3.3) in the
extendedKeyUsage field

`o` Server certificates presented for TLS shall have the Server Authentication

purpose (id-kp 1 with OID 1.3.6.1.5.5.7.3.1) in the extendedKeyUsage field.

`o` Server certificates presented for EST shall have the CMC Registration Authority

(RA) purpose (id-kp-cmcRA with OID 1.3.6.1.5.5.7.3.28) in the
extendedKeyUsage field. [conditional]

`o` Client certificates presented for TLS shall have the Client Authentication purpose

(id-kp 2 with OID 1.3.6.1.5.5.7.3.2) in the extendedKeyUsage field.

`o` OCSP certificates presented for OCSP responses shall have the OCSP Signing

purpose (id-dp 9 with OID 1.3.6.1.5.5.7.3.9) in the extendedKeyUsage field.

[conditional]
**FIA_X509_EXT.1.2**

The TSF shall only treat a certificate as a CA certificate if the basicConstraints extension
is present and the CA flag is set to TRUE.


_5.1.4.26_ _PP_MDF_V3.3:FIA_X509_EXT.2 X509 Certificate Authentication_


**FIA_X509_EXT.2.1**

The TSF shall use X.509v3 certificates as defined by RFC 5280 to support authentication
for [mutually authenticated TLS as defined in the Package for Transport Layer Security
(TLS), version 1.1, HTTPS, [ _**no other protocol**_ ]] and [ _**no additional uses**_ ].
**FIA_X509_EXT.2.2**

When the TSF cannot establish a connection to determine the revocation status of a
certificate, the TSF shall [ _**not accept the certificate**_ ].


_5.1.4.27_ _PP_MDF_V3.3:FIA_X509_EXT.3 Request Validation of Certificates_


**FIA_X509_EXT.3.1**

The TSF shall provide a certificate validation service to applications.
**FIA_X509_EXT.3.2**

The TSF shall respond to the requesting application with the success or failure of the
validation.


_5.1.4.28_ _MOD_WLANC_V1.0:FIA_X509_EXT.1/WLAN X.509 Certificate Validation_


**FIA_X509_EXT.1.1/WLAN**

45 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


The TSF shall validate certificates for EAP-TLS in accordance with the following rules:

       - RFC 5280 certificate validation and certificate path validation

       - The certificate path must terminate with a certificate in the Trust Anchor Database

       - The TSF shall validate a certificate path by ensuring the presence of the
basicConstraints extension and that the CA flag is set to TRUE for all CA certificates

       - The TSF shall validate the extendedKeyUsage field according to the following rules:

`o` Server certificates presented for TLS shall have the Server Authentication

purpose (id-kp 1 with OID 1.3.6.1.5.5.7.3.1) in the extendedKeyUsage field

`o` Client certificates presented for TLS shall have the Client Authentication purpose

(id-kp 2 with OID 1.3.6.1.5.5.7.3.2) in the extendedKeyUsage field.
**FIA_X509_EXT.1.2/WLAN**

The TSF shall only treat a certificate as a CA certificate if the basicConstraints extension
is present and the CA flag is set to TRUE.


_5.1.4.29_ _MOD_WLANC_V1.0:FIA_X509_EXT.2/WLAN X.509 Certificate Authentication (EAP-TLS_
_for WLAN)_


**FIA_X509_EXT.2.1/WLAN**

The TSF shall use X.509v3 certificates as defined by RFC 5280 to support [ _[authentication_
_for EAP-TLS exchanges]_ ].


_5.1.4.30_ _MOD_WLANC_V1.0:FIA_X509_EXT.6 Certificate Storage and Management_


**FIA_X509_EXT.6.1**

The TSF shall [ _**invoke [software-based key storage] to store and protect**_ ] certificate(s)
from unauthorized deletion and modification.
**FIA_X509_EXT.6.2**

The TSF shall [ _**rely on [the TOE certificate management system] to load X.509v3**_
_**certificates into [software-based key storage]**_ ] for use by the TSF.


5.1.5 Security management (FMT)


_5.1.5.1_ _PP_MDF_V3.3:FMT_MOF_EXT.1 Management of Security Functions Behavior_


**FMT_MOF_EXT.1.1**

The TSF shall restrict the ability to perform the functions in [column 4 of Table 16 Security Management Functions] to the user.
**FMT_MOF_EXT.1.2**

The TSF shall restrict the ability to perform the functions [in column 6 of Table 16 Security Management Functions] to the administrator when the device is enrolled and
according to the administrator-configured policy.


_5.1.5.2_ _MOD_MDM_AGENT_V1.0:FMT_POL_EXT.2 Agent Trusted Policy Update_


**FMT_POL_EXT.2.1**

The MDM Agent shall only accept policies and policy updates that are digitally signed by
a private key that has been authorized for policy updates by the MDM Server.

46 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


**FMT_POL_EXT.2.2**

The MDM Agent shall not install policies if the signature check fails.
(TD0755 applied)


_5.1.5.3_ _PP_MDF_V3.3:FMT_SMF.1 Specification of Management Functions_


**FMT_SMF.1.1**

The TSF shall be capable of performing the following management functions:

(In the last four columns, M = Mandatory and I = Implemented, to denote which options are available
for any management function.)





|#|Management Function|Implemented|User Only|Admin|Admin Only|
|---|---|---|---|---|---|
|1.|configure password policy:<br>• <br>Minimum password length<br>• <br>Minimum password complexity<br>• <br>Maximum password lifetime<br>The administrator can configure the required password characteristics (minimum<br>length, complexity, and lifetime) using the Android MDM APIs.<br>Length: an integer value of characters (password range is 4-16 characters, values<br>below that range are treated as 4, values above that range are treated as 16)<br>Complexity:<br>• <br>Unspecified, Something – no specific requirements for the password<br>• <br>Numeric – numbers are required in the password (can be all numbers, or<br>anything that has numbers in it)<br>• <br>Numeric_Complex – same as Numeric, but no repeating or sequential<br>numeric characters above 3 are allowed (e.g. 4444, 1234, 2468)<br>• <br>Alphabetic – letters or symbols are required in the password (can be all<br>letters, or anything with letters/symbols in it)<br>• <br>Alphanumeric – letters/symbols and numbers are required in the password<br>• <br>Complex – enables the admin to set specific constraints on the password<br>requirements (minimum/maximum letters, symbols, numbers and length)<br>Lifetime: an integer value of seconds (0 = no maximum).|M|-|M|M|


47 of 100






Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


















|#|Management Function|Implemented|User Only|Admin|Admin Only|
|---|---|---|---|---|---|
|2.|configure session locking policy:<br>• <br>Screen-lock enabled/disabled<br>• <br>Screen lock timeout<br>• <br>Number of authentication failures<br>The administrator can configure the session locking policy using the Android MDM<br>APIs.<br>Screen lock timeout: an integer number of milliseconds before the TOE locks. An<br>integer number (-9,223,372,036,854,775,808 to 9,223,372,036,854,775,807 [negative<br>integers and zero means no lockout]).<br>Authentication failures: an integer number (-2,147,483,648 to 2,147,483,648<br>[negative integers and zero means no limit]) of failures before a wipe action is<br>initiated. This only applies to password authentication, biometric attempts do not<br>increment this counter.|M|-|M|M|
|3.|enable/disable the VPN protection:<br>• <br>Across device<br>• <br>[**_on a per-group of applications processes basis_**] <br>Both users (using the TOE’s settings UI) and administrator (using the TOE’s MDM<br>APIs) can configure a third-party VPN client and then enable the VPN client to protect<br>traffic. The User can set up VPN protection, but if an admin enables VPN protection,<br>the user cannot disable it.<br>The administrator (using the TOE’s MDM APIs) can configure a VPN client for a group<br>of applications through the creation of a work profile. The VPN client for the work<br>profile will be associated with all the applications included in the work profile.|M|-|I|I|
|4.|enable/disable [**Bluetooth, NFC**] <br>enable/disable [**Wi-Fi, cellular**] <br>The administrator (using the TOE’s MDM APIs) can manage Bluetooth radio. The user<br>cannot override the administrator setting.<br>The NFC, Wi-Fi and cellular radios can be disabled by the administrator (using the<br>TOE’s MDM APIs), but the user (using the TOE’s settings UI) is able to override this<br>and turn the radios back on.<br>The TOE’s radios operate at frequencies of 2.4 GHz (NFC/Bluetooth), 2.4/5 GHz (Wi-<br>Fi) and 850, 900, 1800, 1900 MHz (4G/LTE).<br>The radios are initialized during the initial power-up sequence. If the radio is<br>supposed to be off (by setting), it will be turned off after the initial check.|M <br>M|- <br>I|I <br>-|I <br>-|



48 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025













|#|Management Function|Implemented|User Only|Admin|Admin Only|
|---|---|---|---|---|---|
|5.|enable/disable [**microphone, camera**]:<br>• <br>Across device,<br>• <br>[**_on a per-app basis_**] <br>An administrator can enable/disable the device’s microphone via an MDM API. Once<br>the microphone has been disabled, the user cannot re-enable it until the<br>administrator enables it.<br>In the user’s settings, a user can view a permission by type (i.e. camera, microphone).<br>The user can access this by going to the settings UI (`Settings -> Privacy ->`<br>`Permission manager -> <camera/microphone>`) and revoking any<br>applications.|<br>M <br>M|<br>- <br>-|<br>I <br>-|<br>I <br>-|
|6.|transition to the locked state<br>Both users (using the TOE’s settings UI) and administrators (using the TOE’s MDM<br>APIs) can transition the TOE into a locked state.|M|-|M|-|
|7.|TSF wipe of protected data<br>Both users (using the TOE’s settings UI) and administrators (using the TOE’s MDM<br>APIs) can force the TOE to perform a full wipe (factory reset) of data.|M|-|M|-|
|8.|configure application installation policy by: [<br>• <br>**_restricting the sources of applications,_**<br>• <br>**_denying installation of applications_**] <br>The administrator (using the TOE’s MDM APIs) can configure the TOE so that<br>applications cannot be installed and can also block the use of the Google Market<br>Place.|M|-|M|M|
|9.|import keys or secrets into the secure key storage<br>Both users (using the TOE’s settings UI) and administrators (using the TOE’s MDM<br>APIs) can import secret keys into the secure key storage.|M|-|I|-|
|10.|destroy imported keys or secrets and [**_no other keys or secrets_**] in the secure key<br>storage<br>Both users and administrators (using the TOE’s MDM APIs) can destroy secret keys in<br>the secure key storage.|M|-|I|-|
|11.|import X.509v3 certificates into the Trust Anchor Database<br>Both users (using the TOE’s settings UI) and administrators (using the TOE’s MDM<br>APIs) can import X.509v3 certificates into the Trust Anchor Database.|M|-|M|-|


49 of 100






Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025





|#|Management Function|Implemented|User Only|Admin|Admin Only|
|---|---|---|---|---|---|
|12.|remove imported X.509v3 certificates and [**_no other X.509v3 certificates_**] in the Trust<br>Anchor Database<br>Both users (using the TOE’s settings UI) and administrators (using the TOE’s MDM<br>APIs) can remove imported X.509v3 certificates from the Trust Anchor Database as<br>well as disable any of the TOE’s default Root CA certificates (in the latter case, the CA<br>certificate still resides in the TOE’s read-only system partition; however, the TOE will<br>treat that Root CA certificate and any certificate chaining to it as untrusted).|M|-|I|-|
|13.|enroll the TOE in management<br>TOE users can enroll the TOE in management according to the instructions specific to<br>a given MDM. Presumably any enrollment would involve at least some user functions<br>(e.g., install an MDM agent application) on the TOE prior to enrollment.|M|-|-|-|
|14.|remove applications<br>Both users (using the TOE’s settings UI) and administrators (using the TOE’s MDM<br>APIs) can uninstall user and administrator installed applications (and the associated<br>application data) on the TOE.|M|-|M|-|
|15.|update system software<br>Users can check for updates and cause the device to update if an update is available.<br>An administrator can use MDM APIs to query the version of the TOE and query the<br>installed applications and an MDM agent on the TOE could issue pop-ups, initiate<br>updates, block communication, etc. until any necessary updates are completed.|M|-|M|-|
|16.|install applications<br>Both users and administrators (using the TOE’s MDM APIs) can install applications on<br>the TOE.|M|-|M|-|
|17.|remove Enterprise applications<br>An administrator (using the TOE’s MDM APIs) can uninstall Enterprise installed<br>applications on the TOE.|M|-|M|-|
|18.|enable/disable display notification in the locked state of: [<br>• <br>**_all notifications_**] <br>Notifications can be configured to display in the following formats:<br>• <br>Users & administrators: show all notification content<br>• <br>Users: hide sensitive content<br>• <br>Users & administrators: hide notifications entirely<br>If the administrator sets any of the above settings, the user cannot change it.|M|-|I|I|
|19.|enable data-at rest protection<br>The TOE always encrypts its user data storage.|M|-|-|-|


50 of 100






Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025









|#|Management Function|Implemented|User Only|Admin|Admin Only|
|---|---|---|---|---|---|
|20.|enable removable media’s data-at-rest protection<br>The device does not support internally installed removable media.|M|-|-|-|
|21.|enable/disable location services:<br>• <br>Across device<br>• <br>[**_no other method_**] <br>The administrator (using the TOE’s MDM APIs) can enable or disable location services.<br>An additional MDM API can prohibit TOE users’ ability to enable and disable location<br>services.|M|-|I|I|
|22.|enable/disable the use of [**_Biometric Authentication Factor_**] <br>The administrator (using the TOE’s MDM APIs) can enable or disable the biometric<br>services (individually for each possible supported modality).|I|-|I|I|
|23.|configure whether to allow or disallow establishment of [**assignment**: _configurable_<br>_trusted channel in FTP_ITC_EXT.1.1 or FDP_UPC_EXT.1.1/APPS_] if the peer or server<br>certificate is deemed invalid.|||||
|24.|enable/disable all data signaling over [**assignment:**_list of externally accessible_<br>_hardware ports_]|||||
|25.|enable/disable [**Bluetooth tethering**] <br>The administrator (using the TOE’s MDM APIs) can enable/disable all tethering<br>methods (i.e. all or none disabled).<br>The TOE acts as a server (acting as an access point, a USB Ethernet adapter, and as a<br>Bluetooth Ethernet adapter respectively) in order to share its network connection<br>with another device.|I|-|I|I|
|26.|enable/disable developer modes<br>The administrator (using the TOE’s MDM APIs) can disable Developer Mode.<br>Unless disabled by the administrator, TOE users can enable and disable Developer<br>Mode.|I|-|I|I|
|27.|enable/disable bypass of local user authentication<br>N/A – It is not possible to bypass local user auth for this TOE|||||
|28.|wipe Enterprise data<br>An administrator (using the TOE’s MDM APIs) can remove Enterprise applications and<br>their data. The user can factory reset the device to remove all Enterprise applications<br>and associated data.|I|-|I|-|
|29.|approve [**selection**: _import, removal_] by applications of X.509v3 certificates in the<br>Trust Anchor Database|||||
|30.|configure whether to allow or disallow establishment of a trusted channel if the TSF<br>cannot establish a connection to determine the validity of a certificate|||||


51 of 100








Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025



















|#|Management Function|Implemented|User Only|Admin|Admin Only|
|---|---|---|---|---|---|
|31.|enable/disable the cellular protocols used to connect to cellular network base<br>stations<br>An administrator or user is able to block the ability to use 2G networks.|I|-|I|-|
|32.|read audit logs kept by the TSF<br>Only the administrator is able to read the SecurityLog, but events tracked through<br>logcat may be read by a user (based on the setting in #26).|I|-|I|-|
|33.|configure [**selection**: _certificate, public-key_] used to validate digital signature on<br>applications|||||
|34.|approve exceptions for shared use of keys or secrets by multiple applications|||||
|35.|approve exceptions for destruction of keys or secrets by applications that did not<br>import the key or secret|||||
|36.|configure the unlock banner<br>The administrator (using the TOE’s MDM APIs) can specify text to always be shown on<br>the lock screen (up to 120 characters can be displayed).|M|-|I|-|
|37.|configure the auditable items|||||
|38.|retrieve TSF-software integrity verification values|||||
|39.|enable/disable [<br>• <br>**_USB mass storage mode_**] <br>The administrator (using the TOE’s MDM APIs) can specify whether the device can<br>have its storage mounted as USB storage available for read/write (when the device is<br>unlocked) to another device (such as a computer). The user can also specify to block<br>access to USB storage in the TOE’s settings UI.|I|-|I|-|
|40.|enable/disable backup to [selection: all applications, selected applications, selected<br>groups of applications, configuration data] to [selection: locally connected system,<br>remote system]|||||
|41.|enable/disable [<br>• <br>**_Hotspot functionality authenticated by [pre-shared key],_**<br>• <br>**_USB tethering authenticated by [no authentication]]_** <br>The administrator (using the TOE’s MDM APIs) can disable the Wi-Fi hotspot and USB<br>tethering.<br>Unless disabled by the administrator, TOE users can configure the Wi-Fi hotspot with<br>a pre-shared key and can configure USB tethering (with no authentication, though the<br>device must be unlocked to establish the initial tethering connection).|I|-|I|I|
|42.|approve exceptions for sharing data between [**_groups of application_**] <br>The administrator (using the TOE’s MDM APIs) can specify grouping of applications to<br>restrict sharing data between the groups.|I|-|I|I|
|43.|place applications into application process groups based on [**assignment**: _enterprise_<br>_configuration settings_]|||||


52 of 100




Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025











|#|Management Function|Implemented|User Only|Admin|Admin Only|
|---|---|---|---|---|---|
|44.|unenroll the TOE from management<br>The administrator (using the TOE’s MDM APIs) or the user (using the TOE’s settings<br>UI) can choose to remove the TOE from management.|I|-|I|-|
|45.|enable/disable the Always On VPN protection<br>• <br>Across device<br>• <br>[**_no other method_**] <br>The administrator (using the TOE’s MDM APIs) can specify whether a VPN connection<br>is required for the device to access any network services. The configuration would<br>specify the VPN connection(s) required.|I|-|I|I|
|46.|revoke Biometric template|||||
|47.|[**assignment**: _list of other management functions to be provided by the TSF_]|||||


_**Table 16 - Security Management Functions**_


_5.1.5.4_ _MOD_BT_V1.0:FMT_SMF_EXT.1/BT Specification of Management Functions_


**FMT_SMF_EXT.1.1/BT**

The TSF shall be capable of performing the following **Bluetooth** management functions:











|#|Management Function|Implemented|User Only|Admin|Admin Only|
|---|---|---|---|---|---|
|BT-1.|Configure the Bluetooth trusted channel.<br>• <br>Disable/enable the Discoverable (for BR/EDR) and Advertising (for LE)<br>modes;|M|I|||
|BT-2.|Change the Bluetooth device name (separately for BR/EDR and LE);|||||
|BT-3.|Provide separate controls for turning the BR/EDR and LE radios on and off;|||||
|BT-4.|Allow/disallow the following additional wireless technologies to be used with<br>Bluetooth: [**selection**: _Wi-Fi, NFC, [_**_assignment_**_: other wireless technologies]_];|||||
|BT-5.|Configure allowable methods of Out of Band pairing (for BR/EDR and LE);|||||
|BT-6.|Disable/enable the Discoverable (for BR/EDR) and Advertising (for LE) modes<br>separately;|||||
|BT-7.|Disable/enable the Connectable mode (for BR/EDR and LE);|||||
|BT-8.|Disable/enable the Bluetooth [**assignment**: list of Bluetooth service and/or profiles<br>available on the OS (for BR/EDR and LE)]:|||||
|BT-9.|Specify minimum level of security for each pairing (for BR/EDR and LE);|||||


_**Table 17 - Bluetooth Security Management Functions**_



53 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


_5.1.5.5_ _MOD_WLANC_V1.0:FMT_SMF.1/WLAN Specification of Management Functions_
_(WLAN Client)_


**FMT_SMF_EXT.1.1/WLAN**

The TSF shall be capable of performing the following management functions:


















|#|Management Function|Implemented|Admin|User|
|---|---|---|---|---|
|WL-1.|configure security policy for each wireless network:<br>• <br>[**_specify the CA(s) from which the TSF will accept WLAN authentication_**<br>**_server certificate(s)_**] <br>• <br>security type<br>• <br>authentication protocol<br>• <br>client credentials to be used for authentication|M|M||
|WL-2.|specify wireless networks (SSIDs) to which the TSF may connect;<br>An administrator can specify a list of wireless networks to which the TOE may<br>connect and can restrict the TOE to only allow a connection to the specified<br>networks.|M|M||
|WL-3.|enable/disable disable wireless network bridging capability (for example, bridging a<br>connection between the WLAN and cellular radios to function as a hotspot)<br>authenticated by [**_pre-shared key_**]|M|M||
|WL-4.|enable/disable certificate revocation list checking;||||
|WL-5.|disable ad hoc wireless client-to-client connection capability||||
|WL-6.|disable roaming capability;||||
|WL-7.|enable/disable IEEE 802.1X pre-authentication;||||
|WL-8.|loading X.509 certificates into the TOE||||
|WL-9.|revoke X.509 certificates loaded into the TOE||||
|WL-10.|<br>enable/disable and configure PMK caching:<br>• <br>set the amount of time (in minutes) PMK entries are cached; <br>• <br>set the maximum number of PMK entries that can be cached.||||
|WL-11.|configure security policy for each wireless network: set wireless frequency band to<br>   [**selection**: _2.4 GHz, 5 GHz, 6 GHz_]||||



_**Table 18 - WLAN Security Management Functions**_


(TD0667 applied)


_5.1.5.6_ _PP_MDF_V3.3:FMT_SMF_EXT.2 Specification of Remediation Actions_


**FMT_SMF_EXT.2.1**

The TSF shall offer [ _**wipe of protected data, wipe of sensitive data, remove Enterprise**_
_**applications, remove all device-stored Enterprise resource data**_ ] upon un-enrollment
and [ _**factory reset**_ ].


_5.1.5.7_ _PP_MDF_V3.3:FMT_SMF_EXT.3 Current Administrator_


**FMT_SMF_EXT.3.1**


54 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


The TSF shall provide a mechanism that allows users to view a list of currently
authorized administrators and the management functions that each administrator is
authorized to perform.


_5.1.5.8_ _MOD_MDM_AGENT_V1.0:FMT_SMF_EXT.4 Specification of Management Functions_


**FMT_SMF_EXT.4.1**

The MDM Agent shall be capable of interacting with the platform to perform the
following functions:

       - [ _**import the server public key**_ ],

       - [ _**administrator-provided management functions in MDF PP**_ ],

       - [ _**no additional functions**_ ].
**FMT_SMF_EXT.4.2**

The MDM Agent shall be capable of performing the following functions:

       - Enroll in management

       - Configure whether users can unenroll from management

       - [ _**no other functions**_ ].
(TD0755 applied)


_5.1.5.9_ _MOD_MDM_AGENT_V1.0:FMT_UNR_EXT.1 User Unenrollment Prevention_


**FMT_UNR_EXT.1.1**

The MDM Agent shall provide a mechanism to enforce the following behavior upon an
attempt to unenroll the mobile device from management: [ _**prevent the unenrollment**_
_**from occurring,**_ _**apply remediation actions**_ ].


5.1.6 Protection of the TSF (FPT)


_5.1.6.1_ _PP_MDF_V3.3:FPT_AEX_EXT.1 Application Address Space Layout Randomization_


**FPT_AEX_EXT.1.1**

The TSF shall provide address space layout randomization ASLR to applications.
**FPT_AEX_EXT.1.2**

The base address of any user-space memory mapping will consist of at least 8
unpredictable bits.


_5.1.6.2_ _PP_MDF_V3.3:FPT_AEX_EXT.2 Memory Page Permissions_


**FPT_AEX_EXT.2.1**

The TSF shall be able to enforce read, write, and execute permissions on every page of
physical memory.


_5.1.6.3_ _PP_MDF_V3.3:FPT_AEX_EXT.3 Stack Overflow Protection_


**FPT_AEX_EXT.3.1**

TSF processes that execute in a non-privileged execution domain on the application
processor shall implement stack-based buffer overflow protection.


55 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


_5.1.6.4_ _PP_MDF_V3.3:FPT_AEX_EXT.4 Domain Isolation_


**FPT_AEX_EXT.4.1**

The TSF shall protect itself from modification by untrusted subjects.
**FPT_AEX_EXT.4.2**

The TSF shall enforce isolation of address space between applications.


_5.1.6.5_ _PP_MDF_V3.3:FPT_AEX_EXT.5 Kernel Address Space Layout Randomization_


**FPT_AEX_EXT.5.1**

The TSF shall provide address space layout randomization (ASLR) to the kernel.
**FPT_AEX_EXT.5.2**

The base address of any kernel-space memory mapping will consist of [ **13-25** ]
unpredictable bits.


_5.1.6.6_ _PP_MDF_V3.3:FPT_BBD_EXT.1 Application Processor Mediation_


**FPT_BBD_EXT.1.1**

The TSF shall prevent code executing on any baseband processor (BP) from accessing
application processor (AP) resources except when mediated by the AP.


_5.1.6.7_ _MOD_BIO_V1.1:FPT_BDP_EXT.1 Biometric data processing_


**FPT_BDP_EXT.1.1**

Processing of plaintext biometric data shall be inside the SEE in runtime.
**FPT_BDP_EXT.1.2**

Transmission of plaintext biometric data between the capture sensor and the SEE shall
be isolated from the main computer operating system on the TSF in runtime.


_5.1.6.8_ _PP_MDF_V3.3:FPT_JTA_EXT.1 JTAG Disablement_


**FPT_JTA_EXT.1.1**

The TSF shall [ _**control access by a signing key**_ ] to JTAG.


_5.1.6.9_ _PP_MDF_V3.3 & MOD_BIO_V1.1:FPT_KST_EXT.1 Key Storage_


**FPT_KST_EXT.1.1**

The TSF shall not store any plaintext key material **or biometric data** in readable nonvolatile memory.


_5.1.6.10_ _PP_MDF_V3.3 & MOD_BIO_V1.1:FPT_KST_EXT.2 No Key Transmission_


**FPT_KST_EXT.2.1**

The TSF shall not transmit any plaintext key material **or biometric data** outside the
security boundary of the TOE.


_5.1.6.11_ _PP_MDF_V3.3:FPT_KST_EXT.3 No Plaintext Key Export_


**FPT_KST_EXT.3.1**

The TSF shall ensure it is not possible for the TOE users to export plaintext keys.


56 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


_5.1.6.12_ _PP_MDF_V3.3:FPT_NOT_EXT.1 Self-Test Notification_


**FPT_NOT_EXT.1.1**

The TSF shall transition to non-operational mode and [ _**no other actions**_ ] when the
following types of failures occur:

       - failures of the self-tests

       - TSF software integrity verification failures

       - [ _**no other failures**_ ]


_5.1.6.13_ _MOD_BIO_V1.1:FPT_PBT_EXT.1 Protection of biometric template_


**FPT_PBT_EXT.1.1**

The TSF shall protect the biometric template [ _**using a password as an additional factor**_ ].
(TD0714 applied)


_5.1.6.14_ _PP_MDF_V3.3:FPT_STM.1 Reliable time stamps_


**FPT_STM.1.1**

The TSF shall be able to provide reliable time stamps for its own use.


_5.1.6.15_ _PP_MDF_V3.3:FPT_TST_EXT.1 TSF Cryptographic Functionality Testing_


**FPT_TST_EXT.1.1**

The TSF shall run a suite of self-tests during initial start-up (on power on) to
demonstrate the correct operation of all cryptographic functionality.


_5.1.6.16_ _PP_MDF_V3.3:FPT_TST_EXT.2/PREKERNEL TSF Integrity Checking (Pre-Kernel)_


**FPT_TST_EXT.2.1/PREKERNEL**

The TSF shall verify the integrity of [the bootchain up through the Application Processor
OS kernel] stored in mutable media prior to its execution through the use of [ _**an**_
_**immutable hardware hash of an asymmetric key**_ ].


_5.1.6.17_ _PP_MDF_V3.3:FPT_TST_EXT.2/POSTKERNEL TSF Integrity Checking (Post-Kernel)_


**FPT_TST_EXT.2.1/POSTKERNEL**

The TSF shall verify the integrity of [ _**[executable code stored in the /system and /vendor**_
_**partitions]**_ ], stored in mutable media prior to its execution through the use of [ _**an**_
_**immutable hardware hash of an asymmetric key**_ ].


_5.1.6.18_ _MOD_WLANC_V1.0:FPT_TST_EXT.3/WLAN TSF Cryptographic Functionality Testing_
_(WLAN Client)_


**FPT_TST_EXT.3.1/WLAN**

The [ _**TOE platform**_ ] shall run a suite of self-tests during initial start-up (on power on) to
demonstrate the correct operation of the TSF.
**FPT_TST_EXT.3.2/WLAN**


57 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


The [ _**TOE platform**_ ] shall provide the capability to verify the integrity of stored TSF
executable code when it is loaded for execution through the use of the TSF-provided
cryptographic services.


_5.1.6.19_ _PP_MDF_V3.3:FPT_TUD_EXT.1 Trusted Update: TSF Version Query_


**FPT_TUD_EXT.1.1**

The TSF shall provide authorized users the ability to query the current version of the
TOE firmware/software.
**FPT_TUD_EXT.1.2**

The TSF shall provide authorized users the ability to query the current version of the
hardware model of the device.
**FPT_TUD_EXT.1.3**

The TSF shall provide authorized users the ability to query the current version of
installed mobile applications.


_5.1.6.20_ _PP_MDF_V3.3:FPT_TUD_EXT.2 TSF Update Verification_


**FPT_TUD_EXT.2.1**

The TSF shall verify software updates to the Application Processor system software and

[ _**[baseband processor]**_ ] using a digital signature verified by the manufacturer trusted
key prior to installing those updates.
**FPT_TUD_EXT.2.2**

The TSF shall [ _**update only by verified software**_ ] the TSF boot integrity [ _**key**_ ].
**FPT_TUD_EXT.2.3**

The TSF shall verify that the digital signature verification key used for TSF updates

[ _**matches an immutable hardware public key**_ ].


_5.1.6.21_ _PP_MDF_V3.3:FPT_TUD_EXT.3 Application Signing_


**FPT_TUD_EXT.3.1**

The TSF shall verify mobile application software using a digital signature mechanism
prior to installation.


_5.1.6.22_ _PP_MDF_V3.3:FPT_TUD_EXT.6 Trusted Update Verification_


**FPT_TUD_EXT.6.1**

The TSF shall verify that software updates to the TSF are a current or later version than
the current version of the TSF.


5.1.7 TOE Access (FTA)


_5.1.7.1_ _PP_MDF_V3.3:FTA_SSL_EXT.1 TSF- and User-initiated Locked State_


**FTA_SSL_EXT.1.1**

The TSF shall transition to a locked state after a time interval of inactivity.
**FTA_SSL_EXT.1.2**


58 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


The TSF shall transition to a locked state after initiation by either the user or the
administrator.
**FTA_SSL_EXT.1.3**

The TSF shall, upon transitioning to the locked state, perform the following operations:

       - Clearing or overwriting display devices, obscuring the previous contents;

       - [ _**no other actions**_ ].


_5.1.7.2_ _PP_MDF_V3.3:FTA_TAB.1 Default TOE Access Banners_


**FTA_TAB.1.1**

Before establishing a user session, the TSF shall display an advisory warning message
regarding unauthorized use of the TOE.


_5.1.7.3_ _MOD_WLANC_V1.0:FTA_WSE_EXT.1 Wireless Network Access_


**FTA_WSE_EXT.1.1**

The TSF shall be able to attempt connections only to wireless networks specified as
acceptable networks as configured by the administrator in FMT_SMF.1.1/WLAN.


5.1.8 Trusted Path/Channels (FTP)


_5.1.8.1_ _MOD_BT_V1.0:FTP_BLT_EXT.1 Bluetooth Encryption_


**FTP_BLT_EXT.1.1**

The TSF shall enforce the use of encryption when transmitting data over the Bluetooth
trusted channel for BR/EDR and [ _**LE**_ ].
**FTP_BLT_EXT.1.2**

The TSF shall use key pairs per FCS_CKM_EXT.8 for Bluetooth encryption.


_5.1.8.2_ _MOD_BT_V1.0:FTP_BLT_EXT.2 Persistence of Bluetooth Encryption_


**FTP_BLT_EXT.2.1**

The TSF shall [ _**terminate the connection**_ ] if the remote device stops encryption while
connected to the TOE.


_5.1.8.3_ _MOD_BT_V1.0:FTP_BLT_EXT.3/BR Bluetooth Encryption Parameters (BR/EDR)_


**FTP_BLT_EXT.3.1/BR**

The TSF shall set the minimum encryption key size to [ **128 bits** ] for [ _BR/EDR_ ] and not
negotiate encryption key sizes smaller than the minimum size.


_5.1.8.4_ _MOD_BT_V1.0:FTP_BLT_EXT.3/LE Bluetooth Encryption Parameters (LE)_


**FTP_BLT_EXT.3.1/LE**

The TSF shall set the minimum encryption key size to [ **128 bits** ] for [ _LE_ ] and not
negotiate encryption key sizes smaller than the minimum size.


_5.1.8.5_ _MOD_WLANC_V1.0:FTP_ITC.1/WLAN Trusted Channel Communication (Wireless LAN)_


**FTP_ITC.1.1/WLAN**


59 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


The TSF shall use 802.11-2012, 802.1X, and EAP-TLS to provide a trusted communication
channel between itself and a wireless access point that is logically distinct from other
communication channels and provides assured identification of its end points and
protection of the channel data from modification or disclosure.
**FTP_ITC.1.2/WLAN**

The TSF shall permit [the TSF] to initiate communication via the trusted channel.
**FTP_ITC.1.3/WLAN**

The TSF shall initiate communication via the trusted channel for [wireless access point
connections].


_5.1.8.6_ _PP_MDF_V3.3:FTP_ITC_EXT.1 Trusted Channel Communication_


**FTP_ITC_EXT.1.1**

The TSF shall use

       - 802.11-2012 in accordance with the [PP-Module for Wireless LAN Clients, version
1.0],

       - 802.1X in accordance with the [PP-Module for Wireless LAN Clients, version 1.0],

       - EAP-TLS in accordance with the [PP-Module for Wireless LAN Clients, version 1.0],

       - mutually authenticated TLS in accordance with [the Functional Package for
Transport Layer Security (TLS), version 1.1]
and [

       - _**HTTPS**_
] protocols to provide a communication channel between itself and another trusted IT
product that is logically distinct from other communication channels, provides assured
identification of its end points, protects channel data from disclosure, and detects
modification of the channel data.
**FTP_ITC_EXT.1.2**

The TSF shall permit the TSF to initiate communication via the trusted channel.
**FTP_ITC_EXT.1.3**

The TSF shall initiate communication via the trusted channel for wireless access point
connections, administrative communication, configured enterprise connections, and

[ _**OTA updates**_ ].


_5.1.8.7_ _MOD_MDM_AGENT_V1.0:FTP_ITC_EXT.1(2) Trusted Channel Communication_


**FTP_ITC_EXT.1.1(2)**

**Refinement:** The TSF shall use [ _**HTTPS**_ ] to provide a communication channel between
itself and another trusted IT product that is logically distinct from other communication
channels, provides assured identification of its end points, protects channel data from
disclosure, and detects modification of the channel data.
**FTP_ITC_EXT.1.2(2)**

**Refinement:** The TSF shall permit the TSF and the MDM Server and [ _**no other IT entities**_ ]
to initiate communication via the trusted channel
**FTP_ITC_EXT.1.3(2)**


60 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


**Refinement:** The TSF shall initiate communication via the trusted channel for all
communication between the MDM Agent and the MDM Server and [ _**no other**_
_**communication**_ ].


_5.1.8.8_ _MOD_MDM_AGENT_V1.0:FTP_TRP.1(2) Trusted Path (for Enrollment)_


**FTP_TRP.1.1(2)**

**Refinement:** The TSF shall use [ _**HTTPS**_ ] to provide a trusted communication path
between itself and another trusted IT product that is logically distinct from other
communication paths and provides assured identification of its endpoints and
protection of the communicated data from disclosure and detection of modification of
the communicated data from [ _modification, disclosure_ ].
**FTP_TRP.1.2(2)**

**Refinement:** The TSF shall permit MD users to initiate communication via the trusted
path.
**FTP_TRP.1.3(2)**

**Refinement:** The TSF shall require the use of the trusted path for [ _[all MD user actions]_ ].

### 5.2 TOE Security Assurance Requirements


The SARs for the TOE are the components as specified in Part 3 of the Common Criteria. Note that the
SARs have effectively been refined with the assurance activities explicitly defined in association with
both the SFRs and SARs.

|Requirement Class|Requirement Component|
|---|---|
|ADV: Development|ADV_FSP.1: Basic Functional Specification|
|AGD: Guidance documents|AGD_OPE.1: Operational User Guidance|
|AGD: Guidance documents|AGD_PRE.1: Preparative Procedures|
|ALC: Life-cycle support|ALC_CMC.1: Labelling of the TOE|
|ALC: Life-cycle support|ALC_CMS.1: TOE CM Coverage|
|ALC: Life-cycle support|ALC_TSU_EXT.1: Timely Security Updates|
|ATE: Tests|ATE_IND.1: Independent Testing - Conformance|
|AVA: Vulnerability assessment|AVA_VAN.1: Vulnerability Survey|



_**Table 19 - Assurance Components**_


5.2.1 Development (ADV)


_5.2.1.1_ _ADV_FSP.1 Basic Functional Specification_


**ADV_FSP.1.1D**

The developer shall provide a functional specification.
**ADV_FSP.1.2D**

The developer shall provide a tracing from the functional specification to the SFRs.
**ADV_FSP.1.1C**

The functional specification shall describe the purpose and method of use for each SFRenforcing and SFR-supporting TSFI.
**ADV_FSP.1.2C**


61 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


The functional specification shall identify all parameters associated with each SFRenforcing and SFR-supporting TSFI.
**ADV_FSP.1.3C**

The functional specification shall provide rationale for the implicit categorization of
interfaces as SFR-non-interfering.
**ADV_FSP.1.4C**

The tracing shall demonstrate that the SFRs trace to TSFIs in the functional specification.
**ADV_FSP.1.1E**

The evaluator shall confirm that the information provided meets all requirements for
content and presentation of evidence.
**ADV_FSP.1.2E**

The evaluator shall determine that the functional specification is an accurate and
complete instantiation of the SFRs.


5.2.2 Guidance Documents (AGD)


_5.2.2.1_ _AGD_OPE.1 Operational User Guidance_


**AGD_OPE.1.1D**

The developer shall provide operational user guidance.
**AGD_OPE.1.1C**

The operational user guidance shall describe, for each user role, the user-accessible
functions and privileges that should be controlled in a secure processing environment,
including appropriate warnings.
**AGD_OPE.1.2C**

The operational user guidance shall describe, for each user role, how to use the
available interfaces provided by the TOE in a secure manner.
**AGD_OPE.1.3C**

The operational user guidance shall describe, for each user role, the available functions
and interfaces, in particular all security parameters under the control of the user,
indicating secure values as appropriate.
**AGD_OPE.1.4C**

The operational user guidance shall, for each user role, clearly present each type of
security-relevant event relative to the user-accessible functions that need to be
performed, including changing the security characteristics of entities under the control
of the TSF.
**AGD_OPE.1.5C**

The operational user guidance shall identify all possible modes of operation of the TOE
(including operation following failure or operational error), their consequences, and
implications for maintaining secure operation.
**AGD_OPE.1.6C**

The operational user guidance shall, for each user role, describe the security measures
to be followed in order to fulfill the security objectives for the operational environment
as described in the ST.


62 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


**AGD_OPE.1.7C**

The operational user guidance shall be clear and reasonable.
**AGD_OPE.1.1E**

The evaluator shall confirm that the information provided meets all requirements for
content and presentation of evidence.


_5.2.2.2_ _AGD_PRE.1 Preparative Procedures_


**AGD_PRE.1.1D**

The developer shall provide the TOE, including its preparative procedures.
**AGD_PRE.1.1C**

The preparative procedures shall describe all the steps necessary for secure acceptance
of the delivered TOE in accordance with the developer's delivery procedures.
**AGD_PRE.1.2C**

The preparative procedures shall describe all the steps necessary for secure installation
of the TOE and for the secure preparation of the operational environment in accordance
with the security objectives for the operational environment as described in the ST.
**AGD_PRE.1.1E**

The evaluator shall confirm that the information provided meets all requirements for
content and presentation of evidence.
**AGD_PRE.1.2E**

The evaluator shall apply the preparative procedures to confirm that the TOE can be
prepared securely for operation.


5.2.3 Life-cycle support (ALC)


_5.2.3.1_ _ALC_CMC.1 Labeling of the TOE_


**ALC_CMC.1.1D**

The developer shall provide the TOE and a reference for the TOE.
**ALC_CMC.1.1C**

The TOE shall be labelled with its unique reference.
**ALC_CMC.1.1E**

The evaluator shall confirm that the information provided meets all requirements for
content and presentation of evidence.


_5.2.3.2_ _ALC_CMS.1 TOE CM Coverage_


**ALC_CMS.1.1D**

The developer shall provide a configuration list for the TOE.
**ALC_CMS.1.1C**

The configuration list shall include the following: the TOE itself; and the evaluation
evidence required by the SARs.
**ALC_CMS.1.2C**

The configuration list shall uniquely identify the configuration items.
**ALC_CMS.1.1E**


63 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


The evaluator shall confirm that the information provided meets all requirements for
content and presentation of evidence.


_5.2.3.3_ _ALC_TSU_EXT.1 Timely Security Updates_


**ALC_TSU_EXT.1.1D**

The developer shall provide a description in the TSS of how timely security updates are
made to the TOE.
**ALC_TSU_EXT.1.1C**

The description shall include the process for creating and deploying security updates for
the TOE software.
**ALC_TSU_EXT.1.2C**

The description shall express the time window as the length of time, in days, between
public disclosure of a vulnerability and the public availability of security updates to the
TOE.
**ALC_TSU_EXT.1.3C**

The description shall include the mechanisms publicly available for reporting security
issues pertaining to the TOE.
**ALC_TSU_EXT.1.4C**

The description shall include where users can seek information about the availability of
new updates including details (e.g. CVE identifiers) of the specific public vulnerabilities
corrected by each update.
**ALC_TSU_EXT.1.1E**

The evaluator shall confirm that the information provided meets all requirements for
content and presentation of evidence.


5.2.4 Tests (ATE)


_5.2.4.1_ _ATE_IND.1 Independent Testing - Conformance_


**ATE_IND.1.1D**

The developer shall provide the TOE for testing.
**ATE_IND.1.1C**

The TOE shall be suitable for testing.
**ATE_IND.1.1E**

The evaluator shall confirm that the information provided meets all requirements for
content and presentation of evidence.
**ATE_IND.1.2E**

The evaluator shall test a subset of the TSF to confirm that the TSF operates as specified.


5.2.5 Vulnerability assessment (AVA)


_5.2.5.1_ _AVA_VAN.1 Vulnerability Survey_


**AVA_VAN.1.1D**

The developer shall provide the TOE for testing.


64 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


**AVA_VAN.1.1C**

The TOE shall be suitable for testing.
**AVA_VAN.1.1E**

The evaluator shall confirm that the information provided meets all requirements for
content and presentation of evidence.
**AVA_VAN.1.2E**

The evaluator shall perform a search of public domain sources to identify potential
vulnerabilities in the TOE.
**AVA_VAN.1.3E**

The evaluator shall conduct penetration testing, based on the identified potential
vulnerabilities, to determine that the TOE is resistant to attacks performed by an
attacker possessing Basic attack potential.


65 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025

## 6 TOE Summary Specification


This chapter describes the security functions:

  - Security audit

  - Cryptographic support

  - User data protection

  - Identification and authentication

  - Security management

  - Protection of the TSF

  - TOE access

  - Trusted path/channels

### 6.1 Security audit


**MOD_MDM_AGENT_V1.0:FAU_ALT_EXT.2:**
The TOE utilizes a server-initiated communications mechanism for communications to the MDM Server.
Once the TOE starts, the Device Policy application (the MDM Agent service) will connect to the server to
register its availability and will then only connect again based on notifications from the MDM Server.
When a policy is retrieved, the Device Policy application will send a notification to be delivered back to
the MDM Server to register the successful (or unsuccessful) application of the policy.


If the connection to the MDM Server is not available when the Device Policy application attempts to
send a policy status update, the notification will be queued until connectivity is restored at which time
the notification will be delivered. A policy status update includes the current status of all policies that
are applied (so a single update would include the current status of all the settings included in the policy).
If the current status of a policy changes (for example, a password was not in compliance at the time the
policy was applied but then the user later changes the password to bring it into compliance), the policy
status update will be changed to note that the current status is now compliant.


The policy status update does not have any specific storage constraints as there is only a single policy
status update that maintains the current status of all policies. The queue on a per-policy basis is one,
such that only the current status of the policy is maintained (so if a policy was listed as compliant, then
non-compliant and then again compliant, only the current state, in this case compliant, would be
reported in the queue).


All connections are made over a trusted channel. If the trusted channel is not available, notifications will
be cached until it is available (such as going offline after downloading a policy).


**PP_MDF_V3.3:FAU_GEN.1:**
**MOD_BT_V1.0:FAU_GEN.1/BT:**
**MOD_WLANC_V1.0:FAU_GEN.1/WLAN:**
**MOD_MDM_AGENT_V1.0:FAU_GEN.1(2):**
The TOE uses different forms of logs to meet all the required management logging events specified in
Table 2 and Table 3 of the PP_MDF_V3.3, Table 2 of the MOD_BT_V1.0 and Table 2 of the
MOD_WLANC_V1.0:

66 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


1. SecurityLog events
2. Logcat events
Each of the above logging methods are described below.

  - _SecurityLog events_ : A full list of all auditable events can be found here:
[https://developer.android.com/reference/android/app/admin/SecurityLog#constants_1. Values](https://developer.android.com/reference/android/app/admin/SecurityLog#constants_1)
found in this list represent SecurityLog keywords used in this logging function along with a
description of what the log means and any additional information/variables present in the audit
record. Additionally, the following link provides the additional information that can be grabbed
when an MDM requests a copy of the logs:
[https://developer.android.com/reference/android/app/admin/SecurityLog.SecurityEvent. Each](https://developer.android.com/reference/android/app/admin/SecurityLog.SecurityEvent)
log contains a keyword or phrase describing the event, the date and time of the event, and
further event-specific values that provide success, failure, and other information relevant to the
event.

  - _Logcat events:_ Similar to SecurityLog events, logcat events contain date, time, and further evenspecific values within the logs. In addition, logcat events provide a value that maps to a user ID
to identify which user caused the event that generated the log. Finally, logcat events are
descriptive and do not require the administrator to know the template of the log to understand
its values. Logcat events cannot be exported but can be viewed by an administrator via an
MDM agent.
The logs, when full, wrap around and overwrite the oldest log (as the start of the buffer).


The Device Policy application (MDM Agent service) has sufficient permissions to allow it to write events
to Logcat. The WLAN client components are integrated into the operating system and write directly to
the SecurityLog and Logcat (as needed).


The following tables enumerate the events that the TOE audits:

|Protection Profile|Table|
|---|---|
|PP_MDF_V3.3|Mandatory - Table 12 - PP_MDF_V3.3 Audit Events|
|MOD_BT_V1.0|Table 13 - MOD_BT_V1.0 Audit Events|
|MOD_WLANC_V1.0|Table 14 - MOD_WLANC_V1.0 Audit Events|
|MOD_MDM_AGENT_V1.0|Table 15 - MOD_MDM_AGENT_V1.0 Audit Events|



_**Table 20 - Audit Event Table References**_


The details of the events audited are included in section 9 of the Admin Guide.


Some audit records, while logged, are unavailable to the administrator due to a number of reasons. Such
audits and their explanations are identified below:

  - (ALL) FAU_GEN.1 – Shutdown of the audit functions: Upon log shutdown, the security log buffer
is deallocated and no longer available to be read, rendering the viewing of such an audit
unavailable for the administrator to view.

  - PP_MDF_V3.3:FAU_GEN.1 – Shutdown of the Rich OS: Since security logs are stored in memory,
a shutdown of the system clears the audit record that is generated stating that the system is
shutting down.

  - PP_MDF_V3.3:FPT_TST_EXT.1 – Failure of self-test: Self-tests take place prior to the initialization
of audit records. While the self-test success/failure audit is queued up to be logged upon

67 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


security logs being initialized, when a self-test failure occurs the boot process is halted prior to
security logs being initialized.
**PP_MDF_V3.3:FAU_SAR.1:**
The TOE provides an MDM API to allow a Device-Owner MDM agent to read the security logs.


**MOD_MDM_AGENT_V1.0:FAU_SEL.1(2):**
The TOE always logs all events into the available services, the SecurityLog or Logcat as specified in
FAU_GEN.1. This ensures that events are always available for review where they can be filtered by the
logging service of the MDM Server.


**PP_MDF_V3.3:FAU_STG.1:**
For security logs, the TOE stores all audit records in memory, making it only accessible to the logd
daemon, and only device owner applications can call the MDM API to retrieve a copy of the logs.
Additionally, only new logs can be added. There is no designated method allowing for the deletion or
modification of logs already present in memory, but reading the security logs clears the buffer at the
time of the read.


The TOE stores logcat events in memory and only allows access by an administrator via an MDM Agent.
The TOE prevents deletion of these logs by any method other than USB debugging (and enabling USB
Debugging takes the phone out of the evaluated configuration).


**PP_MDF_V3.3:FAU_STG.4:**
The SecurityLog and logcat are stored in memory in a circular log buffer of 10KB/64KB, respectively.
Logcat storage is configurable, able to be set by an MDM API. There is no limit to the size that the logcat
buffer can be configured to and it is limited to the size of the system’s memory. Once the log is full, it
begins overwriting the oldest message in its respective buffer and continues overwriting the oldest
message with each new auditable event. These logs persist until either they are overwritten or the
device is restarted.

### 6.2 Cryptographic support


**PP_MDF_V3.3:FCS_CKM.1:**
The TOE provides generation of asymmetric keys including:

|Algorithm|Key/Curve Sizes|Usage|
|---|---|---|
|RSA, FIPS 186-5|2048/3072/4096|API/Application & Sensitive Data Protection<br>(FDP_DAR_EXT.2)|
|ECDSA, FIPS 186-5|P-256/384/521|API/Application|
|ECDHE keys (not domain parameters)|P-256/384|TLS KeyEx (WPA2/WPA3 w/ EAP-TLS & HTTPS)|



_**Table 21 - Asymmetric Key Generation**_


The TOE’s cryptographic algorithm implementations have received NIST algorithm certificates (see the
tables in FCS_COP.1 for all of the TOE’S algorithm certificates). The TOE itself does not generate any
RSA/ECDSA authentication key pairs for TOE functionality (the user or administrator must load
certificates for use with WPA2/WPA3 with EAP-TLS authentication); however, the TOE provides key
generation APIs to mobile applications to allow them to generate RSA/ECDSA key pairs. The TOE
generates only ECDH key pairs (as BoringSSL does not support DH/DHE cipher suites) and does not
generate domain parameters (curves) for use in TLS Key Exchange.


68 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


The TOE will provide a library for application developers to use for Sensitive Data Protection (SDP). This
library (class) generates asymmetric RSA keys for use to encrypt and decrypt data that comes to the
device while in a locked state. Any data received for a specified application (that opts into SDP via this
library), is encrypted using the public key and stored until the device is unlocked. The public key stays in
memory no matter the state of the device (locked or unlocked). However, when the device is locked, the
private key is evicted from memory and unavailable for use until the device is unlocked. Upon unlock,
the private key is re-derived and used to decrypt data received and encrypted while locked.


**MOD_WLANC_V1.0:FCS_CKM.1/WPA:**
The TOE adheres to IEEE 802.11-2012 for key generation. The TOE’s wpa_supplicant provides PRF384,
PRF512 and PRF704 for derivation of 128-bit, 192-bit and 256-bit AES Temporal Keys (using the HMAC
implementation provided by BoringSSL) and employs its BoringSSL AES-256 DRBG when generating
random values used in the EAP-TLS and 802.11 4-way handshake. The TOE supports the AES-128 CCMP
and AES-192/AES-256 GCMP encryption modes. The TOE has successfully completed certification
(including WPA2/WPA3 Enterprise) and received Wi-Fi CERTIFIED Interoperability Certificates from the
Wi-Fi Alliance. The Wi-Fi Alliance maintains a website providing further information about the testing
[program: http://www.wi-fi.org/certification.](http://www.wi-fi.org/certification)

|Device Name|Model Number|Wi-Fi Alliance Certificate Numbers|
|---|---|---|
|Pixel 9 Pro XL|GZC4K, GQ57S, GGX8B|WFA131513|
|Pixel 9 Pro|GEC77, GWVK6, GR83Y|WFA131514|
|Pixel 9|GUR25, G1B60, G2YBB|WFA129269|
|Pixel 9 Pro Fold|GGH2X, GC15S|WFA131515|
|Pixel 9a|GXQ96, GTF7P, G3Y12|WFA132581|
|Pixel 8 Pro|G1NMW, GC3VE|WFA125104|
|Pixel 8|GKWS6, G9BQD|WFA127396|
|Pixel 8a|G5760D, G6GPR, G8HHN|WFA127250|
|Pixel Tablet|GTU8P|WFA117213|
|Pixel Fold|G9FPL, G0B96|WFA124381|
|Pixel 7 Pro|GVU6C, G03Z5, GQML3|WFA119877, WFA119869|
|Pixel 7|GE2AE, GFE4J, GP4BC|WFA119878, WFA119753|
|Pixel 7a|GWKK3, GHL1X, G82U8, G0DZQ|WFA120585, WFA124403|
|Pixel 6 Pro|GF5KQ, G8V0U, GLU0G|WFA113888, WFA113887|
|Pixel 6|GR1YH, GB7N6, G9S9B|WFA113889, WFA111718|
|Pixel 6a|GX7AS, GB62Z, G1AZG, GB17L|WFA117809, WFA117592|



_**Table 22 - Wi-Fi Alliance Certificates**_


**PP_MDF_V3.3:FCS_CKM.2/UNLOCKED:**
The TOE performs key establishment as a client during EAP-TLS and TLS session establishment. Table 21 Asymmetric Key Generation enumerates the TOE’s supported key establishment implementations
(RSA/ECDH for TLS/EAP-TLS). The RSA key exchange mechanism used in the TLS handshake process
undergoes TLS compatibility testing during TOE development to ensure correct implementation.


**PP_MDF_V3.3:FCS_CKM.2.1/LOCKED:**
The TOE provides an SDP library for applications that uses a hybrid crypto scheme based on 4096-bit
RSA based key establishment. Applications can utilize this library to implement SDP that encrypts
incoming data received while the phone is locked in a manner compliant with this requirement.


**MOD_WLANC_V1.0:FCS_CKM.2/WLAN:**

69 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


The TOE adheres to RFC 3394 and 802.11-2012 standards and unwraps the GTK (sent encrypted with the
WPA2/WPA3 KEK using AES Key Wrap in an EAPOL-Key frame). The TOE, upon receiving an EAPOL
frame, will subject the frame to a number of checks (frame length, EAPOL version, frame payload size,
EAPOL-Key type, key data length, EAPOL-Key CCMP descriptor version, and replay counter) to ensure a
proper EAPOL message and then decrypt the GTK using the KEK, thus ensuring that it does not expose
the Group Temporal Key (GTK).


**PP_MDF_V3.3:FCS_CKM_EXT.1:**
The TOE includes a Root Encryption Key (REK) stored in a 256-bit fuse bank within the application
processor. The TOE generates the REK/fuse value during manufacturing using its hardware DRBG. The
application processor protects the REK by preventing any direct observation of the value and prohibiting
any ability to modify or update the value. The application processor loads the fuse value into an internal
hardware crypto register and the Trusted Execution Environment (TEE) provides trusted applications the
ability to derive KEKs from the REK (using an SP 800-108 KDF to combine the REK with a salt).
Additionally, the when the REK is loaded, the fuses for the REK become locked, preventing any further
changing or loading of the REK value. The TEE does not allow trusted applications to use the REK for
encryption or decryption, only the ability to derive a KEK from the REK. The TOE includes a TEE
application that calls into the TEE in order to derive a KEK from the 256-bit REK/fuse value and then only
permits use of the derived KEK for encryption and decryption as part of the TOE key hierarchy. More
[information regarding Trusted Execution Environments may be found at the GlobalPlatform website.](https://globalplatform.org/)


**PP_MDF_V3.3:FCS_CKM_EXT.2:**
The TOE utilizes its approved RBGs to generate DEKs. When generating AES keys for itself (for example,
the TOE’s sensitive data encryption keys or for the Secure Key Storage), the TOE utilizes the
RAND_bytes() API call from its BoringSSL AES-256 CTR_DRBG to generate a 256-bit AES key. The TOE
also utilizes that same DRBG when servicing API requests from mobile applications wishing to generate
AES keys (either 128 or 256-bit).


In all cases, the TOE generates DEKs using a compliant RBG seeded with sufficient entropy so as to
ensure that the generated key cannot be recovered with less work than a full exhaustive search of the
key space.


**PP_MDF_V3.3:FCS_CKM_EXT.3:**
The TOE takes the user-entered password and conditions/stretches this value before combining the
factor with other KEK.


The TOE generates all non-derived KEKs using the RAND_bytes() API call from its BoringSSL AES-256
CTR_DRBG to ensure a full 128/256-bits of strength for asymmetric/symmetric keys, respectively. And
the TOE combines KEKs by encrypting one KEK with the other so as to preserve entropy.


**PP_MDF_V3.3:FCS_CKM_EXT.4:**
The TOE clears sensitive cryptographic material (plaintext keys, authentication and biometric data, and
other security parameters) from memory when no longer needed or when transitioning to the device’s
locked state (in the case of the Sensitive Data Protection keys). Public keys (such as the one used for
Sensitive Data Protection) can remain in memory when the phone is locked, but all crypto-related
private keys are evicted from memory upon device lock. No plaintext cryptographic material resides in
the TOE’s Flash as the TOE encrypts all keys stored in Flash. When performing a full wipe of protected
data, the TOE cryptographically erases the protected data by clearing the Data-At-Rest DEK. Because the


70 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


Android Keystore of the TOE resides within the user data partition, the TOE effectively cryptographically
erases those keys when clearing the Data-At-Rest DEK. In turn, the TOE clears the Data-At-Rest DEK and
Secure Key Storage SEK through a secure direct overwrite (BLKSECDISCARD ioctl) of the wear-leveled
Flash memory containing the key followed by a read-verify.


**PP_MDF_V3.3:FCS_CKM_EXT.5:**
The TOE stores all protected data in encrypted form within the user data partition (either protected data
or sensitive data). Upon request, the TOE cryptographically erases the Data-At-Rest DEK protecting the
user data partition and the SDP Primary KEK protecting sensitive data files in the user data partition,
clears those keys from memory, reformats the partition, and then reboots. The TOE’s clearing of the
keys follows the requirements of FCS_CKM_EXT.4.


**PP_MDF_V3.3:FCS_CKM_EXT.6:**
The TOE generates salt nonces (which are just salt values used in WPA2/WPA3) using its /dev/urandom.

|Salt value and size|RBG origin|Salt storage location|
|---|---|---|
|User password salt (128-bit)|BoringSSL’s AES-256 CTR_DRBG|Flash filesystem|
|TLS client_random (256-bit)|BoringSSL’s AES-256 CTR_DRBG|N/A (ephemeral)|
|TLS pre_master_secret (384-bit)|BoringSSL’s AES-256 CTR_DRBG|N/A (ephemeral)|
|WPA2/WPA3 4-way handshake supplicant nonce<br>(SNonce)|BoringSSL’s AES-256 CTR_DRBG|N/A (ephemeral)|



_**Table 23 - Salt Nonces**_


**MOD_BT_V1.0:FCS_CKM_EXT.8**
The TOE generates new ECDH key pairs every time a connection with a Bluetooth device is established.


**PP_MDF_V3.3:FCS_COP.1/ENCRYPT:**
**PP_MDF_V3.3:FCS_COP.1/HASH:**
**PP_MDF_V3.3:FCS_COP.1/SIGN:**
**PP_MDF_V3.3:FCS_COP.1/KEYHMAC:**
**PP_MDF_V3.3:FCS_COP.1/CONDITION:**
The TOE implements cryptographic algorithms in accordance with the following NIST standards and has
received the following CAVP algorithm certificates. These algorithms are in software and hardware,
depending on the implementation.


The TOE’s BoringSSL Library (version 20240805 with both Processor Algorithm Accelerators (PAA) and
without PAA) provides the following algorithms as validated on Android 15:






















|SFR|Algorithm|Keys|NIST Standard|Cert#|
|---|---|---|---|---|
|FCS_CKM.1|RSA IFC Key Generation|2048,<br>3072,<br>4096|FIPS 186-5, RSA|A6134|
|FCS_CKM.1|ECDSA ECC Key Generation|P256,<br>P384,<br>P521|FIPS 186-5, ECDSA|FIPS 186-5, ECDSA|
|FCS_CKM.2|RSA-based Key Exchange||Tested with known good<br>implementation.|Tested with known good<br>implementation.|
|FCS_CKM.2|ECC-based Key Exchange|P256,<br>P384,<br>P521|SP 800-56A, CVL KAS ECC|SP 800-56A, CVL KAS ECC|



71 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025











|SFR|Algorithm|Keys|NIST Standard|Cert#|
|---|---|---|---|---|
|FCS_COP.1/ENCRYPT|AES CBC, GCM, KW|128/256|FIPS 197, SP 800-38A/D/F||
|FCS_COP.1/HASH|SHA Hashing|1/256/<br>384/512|FIPS 180-4|FIPS 180-4|
|FCS_COP.1/SIGN|RSA Sign/Verify|2048,<br>3072,<br>4096|FIPS 186-5, RSA|FIPS 186-5, RSA|
|FCS_COP.1/SIGN|ECDSA Sign/Verify|P256,<br>P384,<br>P521|FIPS 186-5, ECDSA|FIPS 186-5, ECDSA|
|FCS_COP.1/KEYHMAC|HMAC-SHA 1/256/384/512|1/256/<br>384/512|FIPS 198-1 & 180-4|FIPS 198-1 & 180-4|
|FCS_RBG_EXT.1|DRBG Bit Generation|256|SP 800-90A (Counter)|SP 800-90A (Counter)|


_**Table 24 - BoringSSL Cryptographic Algorithms**_


The TOE’s BoringSSL library has been tested on supported processors for both 64-bit and 32-bit
applications, based on support that is enabled on the Pixel device. The Pixel 8 and 9 series devices (all
Tensor G3 and Tensor G4 devices) only support 64-bit applications while all other devices support both
64-bit and 32-bit applications.


Android’s LockSettings service (version 77561fc30db9aedc1f50f5b07504aa65b4268b88) as validated on
Android 15 provides the TOE’s SP 800-108 key based key derivation function for deriving KEKs.

|SFR|Algorithm|Keys|NIST Standard|Cert#|
|---|---|---|---|---|
|FCS_CKM_EXT.2 &<br>FCS_CKM_EXT.3|LockSettings service KBKDF|256|SP 800-108|A2168|



_**Table 25 - LockSettings Service KDF Cryptographic Algorithms**_


The following algorithms used in the TOE are provided by hardware components of the device. As these
algorithms are implemented solely in hardware, they do not utilize Android 15 as their operating
environment, but provide lower-level services upon which some of the security functionality rests.


The Pixel devices in Table 26 include a Titan security chip, which provides cryptographic algorithm
implementations within a secure microprocessor supporting the Android Keystore StrongBox HAL. Table
26 provides a list of the supported chips in the devices (devices not listed do not support the StrongBox
HAL). Titan security chips support the Android Keystore StrongBox hardware abstraction layer, and as
such, provides secure key generation, digital signatures, and other cryptographic functions in a mutual
hardware Keystore.

|Device|Chip|Hardware|Firmware|
|---|---|---|---|
|Pixel 9 Pro XL/9<br>Pro/9/9 Pro Fold/9a|Titan M2|H1D3M|1.5.1|
|Pixel 8 Pro/8/8a|Titan M2|H1D3M|1.3.10|
|Pixel Tablet/Fold/<br>7 Pro/7/7a|Titan M2|H1D3M|1.2.10|



_**Table 26 - Titan Security Chipsets**_

|SFR|Algorithm|Keys|NIST Standard|Cert#|
|---|---|---|---|---|
|FCS_CKM.1|RSA IFC Key Generation|2048|FIPS 186-5, RSA|A6054|
|FCS_CKM.1|ECDSA ECC Key Generation|P256|FIPS 186-5, ECDSA|FIPS 186-5, ECDSA|



72 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025

|SFR|Algorithm|Keys|NIST Standard|Cert#|
|---|---|---|---|---|
|FCS_COP.1/ENCRYPT|AES CBC, GCM|128/256|FIPS 197, SP 800-38A/D||
|FCS_COP.1/HASH|SHA Hashing|256|FIPS 180-4|FIPS 180-4|
|FCS_COP.1/SIGN|RSA Sign/Verify|2048|FIPS 186-5, RSA|FIPS 186-5, RSA|
|FCS_COP.1/SIGN|ECDSA Sign/Verify|P256|FIPS 186-5, ECDSA|FIPS 186-5, ECDSA|
|FCS_COP.1/KEYHMAC|HMAC-SHA|256|FIPS 198-1 & 180-4|FIPS 198-1 & 180-4|
|FCS_RBG_EXT.1|DRBG Bit Generation|256|SP 800-90A (HMAC)|SP 800-90A (HMAC)|
|FCS_CKM_EXT.2 &<br>FCS_CKM_EXT.3|KBKDF|256|SP 800-108|SP 800-108|



_**Table 27 - Titan M2 with v1.5.1 Firmware Cryptographic Algorithms**_



|SFR|Algorithm|Keys|NIST Standard|Cert#|
|---|---|---|---|---|
|FCS_CKM.1|RSA IFC Key Generation|2048|FIPS 186-5, RSA|A4707|
|FCS_CKM.1|ECDSA ECC Key Generation|P256|FIPS 186-5, ECDSA|FIPS 186-5, ECDSA|
|FCS_COP.1/ENCRYPT|AES CBC, GCM|128/256|FIPS 197, SP 800-38A/D|FIPS 197, SP 800-38A/D|
|FCS_COP.1/HASH|SHA Hashing|256|FIPS 180-4|FIPS 180-4|
|FCS_COP.1/SIGN|RSA Sign/Verify|2048|FIPS 186-5, RSA|FIPS 186-5, RSA|
|FCS_COP.1/SIGN|ECDSA Sign/Verify|P256|FIPS 186-5, ECDSA|FIPS 186-5, ECDSA|
|FCS_COP.1/KEYHMAC|HMAC-SHA|256|FIPS 198-1 & 180-4|FIPS 198-1 & 180-4|
|FCS_RBG_EXT.1|DRBG Bit Generation|256|SP 800-90A (HMAC)|SP 800-90A (HMAC)|
|FCS_CKM_EXT.2 &<br>FCS_CKM_EXT.3|KBKDF|256|SP 800-108|SP 800-108|


_**Table 28 - Titan M2 with v1.3.10 Firmware Cryptographic Algorithms**_







|SFR|Algorithm|Keys|NIST Standard|Cert#|
|---|---|---|---|---|
|FCS_CKM.1|RSA IFC Key Generation|2048|FIPS 186-5, RSA|A2951|
|FCS_CKM.1|ECDSA ECC Key Generation|P256|FIPS 186-5, ECDSA|FIPS 186-5, ECDSA|
|FCS_COP.1/ENCRYPT|AES CBC, GCM|128/256|FIPS 197, SP 800-38A/D|FIPS 197, SP 800-38A/D|
|FCS_COP.1/HASH|SHA Hashing|256|FIPS 180-4|FIPS 180-4|
|FCS_COP.1/SIGN|RSA Sign/Verify|2048|FIPS 186-5, RSA|FIPS 186-5, RSA|
|FCS_COP.1/SIGN|ECDSA Sign/Verify|P256|FIPS 186-5, ECDSA|FIPS 186-5, ECDSA|
|FCS_COP.1/KEYHMAC|HMAC-SHA|256|FIPS 198-1 & 180-4|FIPS 198-1 & 180-4|
|FCS_RBG_EXT.1|DRBG Bit Generation|256|SP 800-90A (HMAC)|SP 800-90A (HMAC)|
|FCS_CKM_EXT.2 &<br>FCS_CKM_EXT.3|KBKDF|256|SP 800-108|SP 800-108|


_**Table 29 - Titan M2 with v1.2.10 Firmware Cryptographic Algorithms**_


The devices have unique Wi-Fi chipsets. All Wi-Fi chipsets provide encryption to meet
FCS_COP.1/ENCRYPT.














|Device|Wi-Fi Chipset|Algorithm|Keys|NIST Standard|Cert#|
|---|---|---|---|---|---|
|Pixel 9 Pro XL/9<br>Pro/9/9 Pro Fold/9a|BCM4390|AES-CCMP|128/256|FIPS 197, SP<br>800-38C|A4158, A4159|
|Pixel 8 Pro/8/8a|BCM4398|BCM4398|BCM4398|BCM4398|A2442, A2509|
|<br>Pixel Tablet|<br>BCM4389|<br>BCM4389|<br>BCM4389|<br>BCM4389|<br>AES 5926, AES<br>5927, AES<br>5952, AES<br>5953|
|<br>Pixel Fold|<br>Pixel Fold|<br>Pixel Fold|<br>Pixel Fold|<br>Pixel Fold|<br>Pixel Fold|
|<br>Pixel 7 Pro/7|<br>Pixel 7 Pro/7|<br>Pixel 7 Pro/7|<br>Pixel 7 Pro/7|<br>Pixel 7 Pro/7|<br>Pixel 7 Pro/7|
|<br>Pixel 6 Pro/6/6a|<br>Pixel 6 Pro/6/6a|<br>Pixel 6 Pro/6/6a|<br>Pixel 6 Pro/6/6a|<br>Pixel 6 Pro/6/6a|<br>Pixel 6 Pro/6/6a|
|<br>Pixel 7a|QC6740|QC6740|QC6740|QC6740|AES 5663|



73 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


_**Table 30 - Wi-Fi Chipsets**_


The Pixel 9 Pro XL/9 Pro/9/9 Pro Fold/9a application processor (Google Tensor G4) provides
cryptographic algorithms (marked as SoC). The Google Tensor UFS Inline Storage Engine is version 1.2.1
with hardware sf_crypt_fmp_fx8_v4.2.1 (4.2.1). The Google Trusty TEE is version 12128783 (marked as
TEE).

|SFR|Component|Algorithm|Keys|NIST Standard|Cert#|
|---|---|---|---|---|---|
|FCS_COP.1/ENCRYPT|Storage|AES XTS|256|FIPS 197, SP 800-38E|A4645|
|FCS_COP.1/HASH|Storage|SHA Hashing|256|FIPS 180-4|A6282|
|FCS_COP.1/KEYHMAC|Storage|HMAC-SHA|256|FIPS 198-1 & 180-4|FIPS 198-1 & 180-4|
|FCS_COP.1/ENCRYPT|SoC|AES CBC|128/256|FIPS 197, SP 800-38A|A5649|
|FCS_COP.1/HASH|SoC|SHA Hashing|256|FIPS 180-4|FIPS 180-4|
|FCS_COP.1/KEYHMAC|SoC|HMAC-SHA|256|FIPS 198-1 & 180-4|FIPS 198-1 & 180-4|
|FCS_CKM_EXT.2 &<br>FCS_CKM_EXT.3|TEE|KBKDF|256|SP 800-108|A5792|
|FCS_COP.1/ENCRYPT|TEE|AES GCM|128/256|FIPS 197, SP 800-38D|FIPS 197, SP 800-38D|
|FCS_COP.1/HASH|TEE|SHA Hashing|256|FIPS 180-4|FIPS 180-4|
|FCS_COP.1/KEYHMAC|TEE|HMAC-SHA|256|FIPS 198-1 & 180-4|FIPS 198-1 & 180-4|



_**Table 31 - Google Tensor G4 Hardware Cryptographic Algorithms**_


The Pixel 8 Pro/8 application processor (Google Tensor G3) provides cryptographic algorithms (marked
as SoC). The Google Tensor UFS Inline Storage Engine is version 1.2.0 with hardware
sf_crypt_fmp_fx8_v4.2.1 (4.2.1). The Google Trusty TEE is version 10588524 (marked as TEE).

|SFR|Component|Algorithm|Keys|NIST Standard|Cert#|
|---|---|---|---|---|---|
|FCS_COP.1/ENCRYPT|Storage|AES XTS|256|FIPS 197, SP 800-38E|A4645|
|FCS_COP.1/HASH|Storage|SHA Hashing|256|FIPS 180-4|A4644|
|FCS_COP.1/KEYHMAC|Storage|HMAC-SHA|256|FIPS 198-1 & 180-4|FIPS 198-1 & 180-4|
|FCS_COP.1/ENCRYPT|SoC|AES CBC|128/256|FIPS 197, SP 800-38A|A4656|
|FCS_COP.1/HASH|SoC|SHA Hashing|256|FIPS 180-4|FIPS 180-4|
|FCS_COP.1/KEYHMAC|SoC|HMAC-SHA|256|FIPS 198-1 & 180-4|FIPS 198-1 & 180-4|
|FCS_CKM_EXT.2 &<br>FCS_CKM_EXT.3|TEE|KBKDF|256|SP 800-108|A4402|
|FCS_COP.1/ENCRYPT|TEE|AES GCM|128/256|FIPS 197, SP 800-38D|FIPS 197, SP 800-38D|
|FCS_COP.1/HASH|TEE|SHA Hashing|256|FIPS 180-4|FIPS 180-4|
|FCS_COP.1/KEYHMAC|TEE|HMAC-SHA|256|FIPS 198-1 & 180-4|FIPS 198-1 & 180-4|



_**Table 32 - Google Tensor G3 Hardware Cryptographic Algorithms**_


The Pixel Tablet, Fold and 7 Pro/7/7a application processor (Google Tensor G2) provides cryptographic
algorithms (marked as SoC). The Google Tensor UFS Inline Storage Engine is version 1.0.0 with hardware
sf_crypt_fmp_fx8_v4.1.0 (4.1.0). The Google Trusty TEE is version 9004426 (marked as TEE).

|SFR|Component|Algorithm|Keys|NIST Standard|Cert#|
|---|---|---|---|---|---|
|FCS_COP.1/ENCRYPT|Storage|AES XTS|128/256|FIPS 197, SP 800-38E|A2937|
|FCS_COP.1/HASH|Storage|SHA Hashing|256|FIPS 180-4|A2938|
|FCS_COP.1/KEYHMAC|Storage|HMAC-SHA|256|FIPS 198-1 & 180-4|FIPS 198-1 & 180-4|
|FCS_COP.1/ENCRYPT|SoC|AES CBC|128/256|FIPS 197, SP 800-38A|A2923|
|FCS_COP.1/HASH|SoC|SHA Hashing|256|FIPS 180-4|FIPS 180-4|
|FCS_COP.1/KEYHMAC|SoC|HMAC-SHA|256|FIPS 198-1 & 180-4|FIPS 198-1 & 180-4|



74 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025

|SFR|Component|Algorithm|Keys|NIST Standard|Cert#|
|---|---|---|---|---|---|
|FCS_CKM_EXT.2 &<br>FCS_CKM_EXT.3|TEE|KBKDF|256|SP 800-108|A2928|
|FCS_COP.1/ENCRYPT|TEE|AES GCM|128/256|FIPS 197, SP 800-38D|FIPS 197, SP 800-38D|
|FCS_COP.1/HASH|TEE|SHA Hashing|256|FIPS 180-4|FIPS 180-4|
|FCS_COP.1/KEYHMAC|TEE|HMAC-SHA|256|FIPS 198-1 & 180-4|FIPS 198-1 & 180-4|



_**Table 33 - Google Tensor G2 Hardware Cryptographic Algorithms**_


The Pixel 6 Pro/6/6a application processor (Google Tensor) provides cryptographic algorithms (marked
as SoC). The Google Tensor UFS Inline Storage Engine is version de8b6c8621; the Hash and Keyed Hash
functions are implemented in software, not hardware (marked as Storage). The Google Trusty TEE is
version 7623683 (marked as TEE).

|SFR|Component|Algorithm|Keys|NIST Standard|Cert#|
|---|---|---|---|---|---|
|FCS_COP.1/ENCRYPT|Storage|AES XTS|128/256|FIPS 197, SP 800-38E|A1981|
|FCS_COP.1/HASH|Storage|SHA Hashing|256|FIPS 180-4|FIPS 180-4|
|FCS_COP.1/KEYHMAC|Storage|HMAC-SHA|256|FIPS 198-1 & 180-4|FIPS 198-1 & 180-4|
|FCS_COP.1/ENCRYPT|SoC|AES CBC|128/256|FIPS 197, SP 800-38A|A1980|
|FCS_COP.1/HASH|SoC|SHA Hashing|256|FIPS 180-4|FIPS 180-4|
|FCS_COP.1/KEYHMAC|SoC|HMAC-SHA|256|FIPS 198-1 & 180-4|FIPS 198-1 & 180-4|
|FCS_RBG_EXT.1|SoC|DRBG Bit Generation|256|SP 800-90A (Counter)|SP 800-90A (Counter)|
|FCS_CKM_EXT.2 &<br>FCS_CKM_EXT.3|TEE|KBKDF|256|SP 800-108|A1982|
|FCS_COP.1/ENCRYPT|TEE|AES GCM|128/256|FIPS 197, SP 800-38D|FIPS 197, SP 800-38D|
|FCS_COP.1/HASH|TEE|SHA Hashing|256|FIPS 180-4|FIPS 180-4|
|FCS_COP.1/KEYHMAC|TEE|HMAC-SHA|256|FIPS 198-1 & 180-4|FIPS 198-1 & 180-4|



_**Table 34 - Google Tensor Hardware Cryptographic Algorithms**_


The TOE’s application processor includes a source of hardware entropy that the TOE distributes
throughout, and the TOE’s RBGs make use of that entropy when seeding/instantiating themselves.


The TOE’s BoringSSL library supports the TOE’s cryptographic Android Runtime (ART) methods (through
Android's conscrypt JNI provider) afforded to mobile applications and supports Android user-space
processes and daemons (e.g., wpa_supplicant). The TOE’s Application Processor provides hardware
accelerated cryptography utilized in Data-At-Rest (DAR) encryption of the user data partition.


The TOE stretches the user’s password to create a password-derived key. The TOE stretching function
uses a series of steps to increase the memory required for key derivation (thus thwarting GPUacceleration, off-line brute force, and precomputed dictionary attacks) and ensure proper conditioning
and stretching of the user’s password.


The TOE conditions the user’s password using two iterations of PBKDFv2 w HMAC-SHA-256 in addition
to some ROMix operations in an algorithm named scrypt. Scrypt consists of one iteration of PBKDFv2,
followed by a series of ROMix operations, and finished with a final iteration of PBKDFv2. The ROMix
operations increase the memory required for key derivation, thus thwarting GPU-acceleration (which
can greatly decrease the time needed to brute force PBKDFv2 alone) and other custom hardware-based
brute force attacks.


The password-derived key is combined with the hardware REK in storage, preventing the ability to
perform offline attacks, and online attacks are limited due to the TOE’s configuration of the maximum


75 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


no more than 50 incorrect password attempts (with at least 4 character passwords). The use of the
password derivation function in combination with the device configuration forces the attacker to only
be able to perform an exhaustive key search to unlock the device without access to the password, and
then subject to the configured limit of 50 or less attempts before the device is wiped.


The following scrypt diagram shows how the password and salt are used with PBKDFv2 and ROMix to
fulfil the requirements for password conditioning.


_**Figure 1 - Password Conditioning**_


The resulting derived key from this operation is used to decrypt the FBE and to derive the User Keystore
Daemon Value.


As part of the TLS, the TOE uses SHA with ciphersuites and digital signatures. The TLS ciphersuites
support using SHA-1, SHA-256 and SHA-384. SHA functionality is also provided to mobile applications
and can also be used as part of HMAC generation. For mobile applications generating a MAC, the HMAC
operations in a byte-oriented mode and can use SHA-1 (with a 160-bit key) to generate a 160-bit MAC,
SHA-256 (with a 256-bit key) to generate a 256-bit MAC, SHA-384 (with a 384-bit key) to generate a 384bit MAC and SHA-512 (with a 512-bit key) to generate a 512-bit MAC. FIPS 198-1 & 180-4 dictate the
block size used, and they specify block sizes/output MAC lengths of 512/160, 512/160, 1024/384, and
1024/512-bits for HMAC-SHA-1, HMAC-SHA-256, HMAC-SHA-384, and HMAC-SHA-512 respectively.


**PP_MDF_V3.3:FCS_HTTPS_EXT.1:**
The TOE supports the HTTPS protocol (compliant with RFC 2818) so that (mobile and system)
applications executing on the TOE can act as HTTPS clients and securely connect to external servers
using HTTPS. Administrators have no credentials and cannot use HTTPS or TLS to establish
administrative sessions with the TOE as the TOE does not provide any such capabilities.


**PP_MDF_V3.3:FCS_IV_EXT.1:**
The TOE generates IVs by reading from /dev/urandom for use with all keys. In all cases, the TOE uses
/dev/urandom and generates the IVs in compliance with the requirements of table 11 of PP_MDF_V3.3.


**PP_MDF_V3.3:FCS_RBG_EXT.1:**


76 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


The TOE provides a number of different RBGs including:


1. An AES-256 CTR_DRBG in Google Tensor processors hardware
2. An AES-256 CTR_DRBG provided by BoringSSL. This is the only accredited and supported DRBG

present in the system and available to independently developed applications. As such, the TOE
provides mobile applications access (through an Android Java API) to random data drawn from
its AES-256 CTR_DRBG
3. An SHA-256 HMAC_DRBG provided by the Titan security chip
The TOE initializes its AP DRBG with enough data from its AP hardware noise source to ensure at least
256-bits of entropy. The TOE then uses its AP DRBG to seed an entropy daemon that uses the BoringSSL
AES-256 CTR_DRBG to provide random bits for user space. The entropy daemon starts early in the boot
process to ensure availability to the rest of the system.


The TOE seeds its BoringSSL AES-256 CTR_DRBG using 384-bits of data from the entropy daemon, thus
ensuring at least 256-bits of entropy. The TOE uses its BoringSSL DRBG for all random generation
including salts.


The TOE seeds the Titan security chip SHA-256 HMAC_DRBG with entropy from its hardware noise and
then uses the DRBG when generating keys and cryptographic random values.


**PP_MDF_V3.3:FCS_SRV_EXT.1:**
The TOE provides applications access to the cryptographic operations including encryption (AES),
hashing (SHA), signing and verification (RSA & ECDSA), key hashing (HMAC), keyed message digests
(HMAC-SHA-256), generation of asymmetric keys for key establishment (RSA and ECDH), and generation
of asymmetric keys for signature generation and verification (RSA, ECDSA). The TOE provides access
through the Android operating system’s Java API, through the native BoringSSL API, and through the
application processor module (user and kernel) APIs.


**PP_MDF_V3.3:FCS_SRV_EXT.2:**
The TOE provides applications with APIs to perform the functions referenced in FCS_COP.1/ENCRYPT
and FCS_COP.1/SIGN.


**PP_MDF_V3.3:FCS_STG_EXT.1:**
The TOE provides the user, administrator and mobile applications the ability to import and use
asymmetric public and private keys into the TOE’s software-based Secure Key Storage. Certificates are
stored in files using UID-based permissions and an API virtualizes the access. Additionally, the user and
administrator can request the TOE to destroy the keys stored in the Secure Key Storage. While normally
mobile applications cannot use or destroy the keys of another application, applications that share a
common application developer (and are thus signed by the same developer key) may do so. In other
words, applications with a common developer (and which explicitly declare a shared UUID in their
application manifest) may use and destroy each other’s keys located within the Secure Key Storage.


The TOE provides additional protections on keys beyond including key attestation, to allow enterprises
and application developers the ability to ensure which keys have been generated securely within the
phone.


77 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


The TOE also provides an extension to Android Keystore, StrongBox, which allows mobile applications to
specify that keys be stored in the Pixel’s hardware-based key storage (provided by the Titan M security
chip [3] ).


**PP_MDF_V3.3:FCS_STG_EXT.2:**
The TOE employs a key hierarchy that protects all DEKs and KEKs by encryption with either the REK or by
the REK and password derived KEK.


The TOE encrypts Long-term Trusted channel Key Material (LTTCKM, i.e., Bluetooth and Wi-Fi keys)
values using AES-256 GCM encryption and stores the encrypted values within their respective
configuration files.


All keys are 256-bits in size. The TOE generates keys using its BoringSSL AES-256 CTR_DRBG (for the Java
and native layer), the Titan series security chips SHA-256 HMAC_DRBG (for StrongBox) or the Google
Tensor series processors AES-256 CTR_DRBG (for Trusted Applications in TrustZone). By utilizing only
256-bit KEKs, the TOE ensures that all keys are encrypted by an equal or larger sized key.


In the case of Wi-Fi, the TOE utilizes the 802.11-2012 KCK and KEK keys to unwrap (decrypt) the
WPA2/WPA3 Group Temporal Key received from the access point. The TOE protects persistent Wi-Fi
keys (user certificates and private keys) by storing them in the Android Key Store. The Wi-Fi connection
uses AES-CCMP (CCM) to encrypt the wireless traffic.


**PP_MDF_V3.3:FCS_STG_EXT.3:**
The TOE protects the integrity of all DEKs and KEKs (including LTTCKM keys) stored in Flash by using
authenticated encryption/decryption methods (CCM, GCM).


**MOD_MDM_AGENT_V1.0:FCS_STG_EXT.4:**
The private key used by the TOE to verify policy integrity is stored in the Android Keystore.


**PKG_TLS_V1.1:FCS_TLS_EXT.1:**
**PKG_TLS_V1.1:FCS_TLSC_EXT.1:**
**PKG_TLS_V1.1:FCS_TLSC_EXT.2:**
The TOE provides mobile applications (through its Android API) the use of TLS version 1.2 as a client,
including support for the selections chosen in section 5 for FCS_TLSC_EXT.1 (and the TOE requires no
configuration other than using the appropriate library APIs as described in the Admin Guidance).


When an application uses the combined APIs provided in the Admin Guide to attempt to establish a
trusted channel connection based on TLS or HTTPS, the TOE supports only Subject Alternative Name
(SAN) (DNS and IP address) as reference identifiers (the TOE does not accept reference identifiers in the
Common Name[CN]). The TOE supports client (mutual) authentication (only a certificate is required to
provide support for mutual authentication).


No additional configuration is needed to allow the device to use the supported cipher suites, as only the
claimed cipher suites are supported in the aforementioned library as each of the aforementioned
ciphersuites are supported on the TOE by default or through the use of the TLS library.


3 StrongBox is not available on the Pixel 6 Pro/6/6a

78 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


While the TOE supports the use of wildcards in X.509 reference identifiers (SAN only), the TOE does not
support certificate pinning. If the TOE cannot determine the revocation status of a peer certificate, the
TOE rejects the certificate and rejects the connection.


**PKG_TLS_V1.1:FCS_TLSC_EXT. 4:**
The TOE includes the ‘renegotiation_info’ TLS extension in its TLS client hello message.


**PKG_TLS_V1.1:FCS_TLSC_EXT.5:**
The TOE in its evaluated configuration and, by design, supports elliptic curves for TLS (P-256 and P-384)
and has a fixed set of supported curves (thus the admin cannot and need not configure any curves).


**MOD_WLANC_V1.0:FCS_TLSC_EXT.1/WLAN:**
**MOD_WLANC_V1.0:FCS_TLSC_EXT.2/WLAN:**
The TSF supports TLS versions 1.1, and 1.2 and also supports the selected ciphersuites utilizing SHA-1,
SHA-256, and SHA-384 (see the selections in section 5 for FCS_TLSC_EXT.1/WLAN) for use with EAP-TLS
as part of WPA2/WPA3. The TOE in its evaluated configuration and, by design, supports only evaluated
elliptic curves (P-256 & P-384 and no others) and has a fixed set of supported curves (thus the admin
cannot and need not configure any curves).


The TOE allows the user to load and utilize authentication certificates for EAP-TLS used with
WPA3/WPA2. The Android UI

```
  Settings -> Security -> Advanced settings -> Encryption &
  credentials -> Install a certificate -> Wi-Fi certificate

```

allows the user to import an RSA or ECDSA certificate for use with Wi-Fi.


**MOD_WLANC_V1.0:FCS_WPA_EXT.1:**
The TSF support WPA2 and WPA3 security types for Wi-Fi networks.

### 6.3 User data protection


**PP_MDF_V3.3:FDP_ACF_EXT.1:**
The TOE provides a mechanism based on the use of assigned permissions to specify the level of access
any application may have to any system service. A system service may have multiple permissions
associated with it, depending on the functionality of the service (for example read and write access may
be separate controls on one service while both may be combined into a single control on another
service). When an application wants to access the system service in question, the calling application
must be granted access to the permission by the user.


Some permissions are granted automatically for applications that are installed by Google (these are only
for Google applications and are not provided for any third party applications) while all the user of the
device must authorize other permissions. Applications using API Level 23 (Android 6.0) or higher (the
current API Level is 35) will prompt the user to grant the permission the first time the permission is
requested by the application. Applications written to older API Levels will prompt the user for all
permissions the first time the application runs. If the user has approved the permission persistently, it
will be allowed every time the application runs, but if the user only approved the permission for one
time use, the user will be prompted to approve access every time the permission is requested by the
application.


79 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


[Permissions in API Level 33 are assigned a protectionLevel](https://developer.android.com/reference/android/R.attr#protectionLevel) based on the implied potential risk to
accessing data protected by the permission. The protectionLevel is divided into two types: base
permissions and protection flags. Base permissions are associated with the level of risk while the flags
are modifiers that may provide context or refinement of the base permission.


The TOE provides the following base permissions to applications (for API Level 35):


1. Normal - A lower-risk permission that gives an application access to isolated application-level

features, with minimal risk to other applications, the system, or the user. The system
automatically grants this type of permission to a requesting application at installation, without
asking for the user's explicit approval (though the user always has the option to review these
permissions before installing).
2. Dangerous - A higher-risk permission that would give a requesting application access to private

user data or control over the device that can negatively impact the user. Because this type of
permission introduces potential risk, the system cannot automatically grant it to the requesting
application. For example, any dangerous permissions requested by an application will be
displayed to the user and require confirmation before proceeding or some other approach can
be taken to avoid the user automatically allowing the use of such facilities.
3. Signature - A permission that the system is to grant only if the requesting application is signed

with the same certificate as the application that declared the permission. If the certificates
match, the system automatically grants the permission without notifying the user or asking for
the user's explicit approval.
4. Internal - a permission that is managed internally by the system and only granted according to

the protection flags.
An example of a normal permission is the ability to vibrate the device: android.permission.VIBRATE. This
permission allows an application to make the device vibrate, and an application that does not request
(or declare) this permission would have its vibration requests ignored.


An example of a dangerous privilege would be access to location services to determine the location of
the mobile device: android.permission.ACCESS_FINE_LOCATION. The TOE controls access to Dangerous
permissions during the running of the application. The TOE prompts the user to review the application’s
requested permissions (by displaying a description of each permission group, into which individual
permissions map, that an application requested access to). If the user approves, then the application is
allowed to continue running. If the user disapproves, the device continues to run, but cannot use the
services protected by the denied permissions. Thereafter, the mobile device grants that application
during execution access to the set of permissions declared in its Manifest file.


An example of a signature permission is the android.permission.BIND_VPN_SERVICE that an application
must declare in order to utilize the VpnService APIs of the device. Because the permission is a Signature
permission, the mobile device only grants this permission to an application (2nd installed app) that
requests this permission and that has been signed with the same developer key used to sign the
application (1st installed app) declaring the permission (in the case of the example, the Android
Framework itself).


An example of an internal permission is the
android.permission.SET_DEFAULT_ACCOUNT_FOR_CONTACTS, which is only granted to system
applications fulfilling the Contacts app role to allow the default account for new contacts to be set.


80 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


Additionally, Android includes the following flags that layer atop the base categories (details can be
[found here https://developer.android.com/reference/android/R.attr#protectionLevel):](https://developer.android.com/reference/android/R.attr#protectionLevel)


1. appop - this permission is closely associated with an app op for controlling access.
2. companion - this permission can be automatically granted to the system companion device

manager service.
3. configurator - this permission automatically granted to device configurator.
4. development - this permission can also (optionally) be granted to development applications

(e.g., to allow additional location reporting during beta testing).
5. incidentReportApprover - this permission designates the app that will approve the sharing of

incident reports.
6. installer - this permission can be automatically granted to system apps that install packages.
7. instant - this permission can be granted to instant apps.
8. knownSigner - this permission can also be granted if the requesting application is signed by, or

has in its signing lineage, any of the certificate digests declared in knownCerts (this allows for
signature changes such as when an application has been changed to a new organization to
maintain access during updates).
9. module - this permission can also be granted if the requesting application is included in the

mainline module.
10. oem - this permission can be granted only if its protection level is signature, the requesting app

resides on the OEM partition, and the OEM has allowlisted the app to receive this permission by
the OEM.
11. pre23 - this permission can be automatically granted to apps that target API levels below API

level 23 (Android 6.0).
12. preinstalled - this permission can be automatically granted to any application pre-installed on

the system image (not just privileged apps) (the TOE does not prompt the user to approve the
permission).
13. privileged - this permission can also be granted to any applications installed as privileged apps

on the system image. Please avoid using this option, as the signature protection level should be
sufficient for most needs and works regardless of exactly where applications are installed. This
permission flag is used for certain special situations where multiple vendors have applications
built in to a system image which need to share specific features explicitly because they are being
built together.
14. recents - this permission will be granted to the recents app.
15. role - this permission is managed by role.
16. runtime - this permission can only be granted to apps that target runtime permissions API level

23 (Android 6.0) and above.
17. setup - this permission can be automatically granted to the setup wizard app.
18. vendorPrivileged - this permission can be granted to privileged apps in vendor partition.
19. verifier - this permission can be automatically granted to system apps that verify packages.


81 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


[The Android 15 (Level 35) API (details found here https://developer.android.com/reference/packages)](https://developer.android.com/reference/packages)
provides services to mobile applications.


While Android provides a large number of individual permissions, they are grouped into categories or
features that provide similar functionality for the simplicity of the user interaction. These groupings do
not affect the permissions themselves; it is only a way to group them together for the user presentation.
Table 35 shows a series of functional categories centered on common functionality.

|Service Features|Description|
|---|---|
|Sensitive I/O Devices & Sensors|Location services, Audio & Video capture, Body sensors|
|User Personal Information & Credentials|Contacts, Calendar, Call logs, SMS|
|Metadata & Device ID Information|IMEI, Phone Number|
|Data Storage Protection|App data, App cache|
|System Settings & Application Management|Date time, Reboot/Shutdown, Sleep, Force-close<br>application, Administrator Enrollment|
|Wi-Fi, Bluetooth, USB Access|Wi-Fi, Bluetooth, USB tethering, debugging and file transfer|
|Mobile Device Management & Administration|MDM APIs|
|Peripheral Hardware|NFC, Camera, Headphones|
|Security & Encryption|Certificate/Key Management, Password, Revocation rules|



_**Table 35 - Functional Categories**_


**PP_MDF_V3.3:FDP_ACF_EXT.1.2:**
Applications with a common developer have the ability to allow sharing of data between their
applications. A common application developer can sign their generated APK with a common certificate
or key and set the permissions of their application to allow data sharing. When the different
applications’ signatures match and the proper permissions are enabled, information can then be shared
as needed.


The TOE supports Enterprise profiles to provide additional separation between application and
application data belonging to the Enterprise profile. Applications installed into the Enterprise versus
Personal profiles cannot access each other’s secure data, applications, and can have separate device
administrators/managers. This functionality is built into the device by default and does not require an
application download. The Enterprise administrative app (an MDM agent application installed into the
Enterprise Profile) may enable cross-profile contacts search, in which case, the device owner can search
the address book of the enterprise profile. Please see the Admin Guide for additional details regarding
how to set up and use Enterprise profiles. Ultimately, the enterprise profile is under control of the
personal profile. The personal profile can decide to remove the enterprise profile, thus deleting all
information and applications stored within the enterprise profile. However, despite the “control” of the
personal profile, the personal profile cannot dictate the enterprise profile to share applications or data
with the personal profile; the enterprise profile MDM must allow for sharing of contacts before any
information can be shared.


**PP_MDF_V3.3:FDP_ACF_EXT.2:**
The TOE allows an administrator to allow sharing of the enterprise profile address book with the normal
profile. Each application group (profile) has its own calendar as well as keychain (keychain is the
collection of user [not application] keys, and only the user can grant the user’s applications access to use
a given key in the user’s keychain), thus Android’s personal and work profiles do not share calendar
appointments nor keys.


82 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


**PP_MDF_V3.3:FDP_DAR_EXT.1:**
The TOE provides Data-At-Rest AES-256 XTS hardware encryption for all data stored on the TOE in the
user data partition (which includes both user data and TSF data). The TOE also has TSF data relating to
key storage for TSF keys not stored in the system’s Android Key Store. The TOE separately encrypts
those TSF keys and data. Additionally, the TOE includes a read-only file system in which the TOE’s
system executables, libraries, and their configuration data reside. For its Data-At-Rest encryption of the
data partition on the internal Flash (where the TOE stores all user data and all application data), the TOE
uses an AES-256 bit DEK with XTS feedback mode to encrypt each file in the data partition using
dedicated application processor hardware.


**PP_MDF_V3.3:FDP_DAR_EXT.2:**
The vendor provides the NIAPSEC library for Sensitive Data Protection (SDP) that application developers
must use to opt-in for sensitive data protection. This library calls into the TOE to generate an RSA key
that acts as a primary KEK for the SDP encryption process. When an application that has opted-in for
SDP receives incoming data while the device is locked, an AES symmetric DEK is generated to encrypt
that data. The public key from the primary RSA KEK is then used to encrypt the AES DEK. Once the device
is unlocked, the RSA KEK private key is re-derived and can be used to decrypt the AES DEK for each piece
of information that was stored while the device was locked. For performance reasons SDP-protected
data is usually decrypted and re-encrypted according to FDP_DAR_EXT.1 at the next successful login and
access of the application, though this is a choice of the application developer (who may have a reason to
maintain the SDP-status of the data).


The keys for SDP are stored in the keystore (FCS_STG_EXT.1) with the settings
[setUnlockedDeviceRequired and setUserAuthenticationRequired](https://developer.android.com/reference/android/security/keystore/KeyGenParameterSpec.Builder#setUnlockedDeviceRequired(boolean)) to enable. These settings ensure that
sensitive data cannot be unlocked except once the user is authenticated to the TOE.


Application data marked as sensitive will have header information about how the data is encrypted that
will specify whether the data can only be read through the NIAPSEC library (utilizing the appropriate
primary SDP KEK). To the system as a whole, there is no difference between an SDP file and a non-SDP
file to avoid calling out where sensitive data is located; this is specifically limited to the header data of
the file which would mark how the DEK is encrypted. Application data is segregated from other
applications as per FDP_ACF_EXT.1.2.


**PP_MDF_V3.3:FDP_IFC_EXT.1:**
The TOE will route all traffic other than traffic necessary to establish the VPN connection to the VPN
gateway (when the gateway’s configuration specifies so) when the Always-On-VPN is enabled. The TOE
includes an interceptor kernel module that controls inbound and output packets. When a VPN is active,
the interceptor will route all incoming packets to the VPN and conversely route all outbound packets to
the VPN before they are output.


Note that when the TOE tries to connect to a Wi-Fi network, it performs a standard captive portal check
which sends traffic that bypasses the full tunnel VPN configuration in order to detect whether the Wi-Fi
network restricts Internet access until one has authenticated or agreed to usage terms through a captive
portal. If the administrator wishes to deactivate the captive portal check (in order to prevent the
plaintext traffic), they may do this by following the instructions in the Admin Guide.


The only exception to all traffic being routed to the VPN is in the instance of ICMP echo requests. The
TOE uses ICMP echo responses on the local subnet to facilitate network troubleshooting and categorizes


83 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


it as a part of ARP. As such, if an ICMP echo request is issued on the subnet the TOE is part of, it will
respond with an ICMP echo response, but no other instances of traffic will be routed outside of the VPN.


**PP_MDF_V3.3:FDP_STG_EXT.1:**
The TOE’s Trusted Anchor Database consists of the built-in certs and any additional user or admin/MDM
loaded certificates. The built-in certs are individually stored in the device’s read-only system image in
the /system/etc/security/cacerts directory, and the user can individually disable certs through the
Android user interface:

```
  Settings -> Security -> Advanced settings -> Encryption &
  credentials -> Trusted Credentials

```

Because the built-in CA certificates reside on the read-only system partition, the TOE places a copy of
any disabled built-in certificate into the /data/misc/user/X/cacerts-removed/ directory, where 'X'
represents the user’s number (which starts at 0). The TOE stores added CA certificates in the
corresponding /data/misc/user/X/cacerts-added/ directory and also stores a copy of the CA certificate in
the user’s Secure Key Storage (residing in the /data/misc/keystore/user_X/ directory). The TOE uses
Linux file permissions that prevent any mobile application or entity other than the TSF from modifying
these files. Only applications registered as an administrator (such as an MDM Agent Application) have
the ability to access these files, staying in accordance to the permissions established in FMT_SMF.1 and
FMT_MOF_EXT.1.


**PP_MDF_V3.3:FDP_UPC_EXT.1/APPS:**
**PP_MDF_V3.3:FDP_UPC_EXT.1/BLUETOOTH:**
The TOE provides APIs allowing non-TSF applications (mobile applications) the ability to establish a
secure channel using TLS, HTTPS, and Bluetooth BR/EDR and LE. Additionally, the vendor provides the
NIAPSEC library for application developers to use for Hostname Checking, Revocation Checking, and TLS
Ciphersuite restriction. Application developers must utilize this library to ensure the device behaves in
the evaluated configuration. Mobile applications can use the following Android APIs for TLS, HTTPS, and
Bluetooth respectively:


SSL:


javax.net.ssl.SSLContext:


[https://developer.android.com/reference/javax/net/ssl/SSLSocket](https://developer.android.com/reference/javax/net/ssl/SSLSocket)


Developers then need to swap SocketFactory for SecureSocketFactory, part of a private library
provided by Google.


[Developers can request this library by emailing: niapsec@google.com](mailto:niapsec@google.com)


HTTPS:


javax.net.ssl.HttpsURLConnection:


[https://developer.android.com/reference/javax/net/ssl/HttpsURLConnection](https://developer.android.com/reference/javax/net/ssl/HttpsURLConnection)


Developers then need to swap HTTPSUrlConnections for SecureUrl part of a private library
provided by Google.


[Developers can request this library by emailing: niapsec@google.com](mailto:niapsec@google.com)


Bluetooth:

84 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


android.bluetooth:


[http://developer.android.com/reference/android/bluetooth/package-summary.html](http://developer.android.com/reference/android/bluetooth/package-summary.html)

### 6.4 Identification and authentication


**PP_MDF_V3.3:FIA_AFL_EXT.1:**
The TOE maintains in persistent storage, for each user, the number of failed password logins since the
last successful login and upon reaching the maximum number of incorrect logins, the TOE performs a full
wipe of all protected data (and in fact, wipes all user data). The maximum number of failed attempts is
limited to only counting password attempts, as biometric attempts are not considered critical attempts
that can trigger a wipe.


The administrator can adjust the number of failed login attempts that are allowed for the password
unlock screen through an MDM. The possible values range from the default of ten failed logins to a
value between 0 (deactivate wiping) and 50. When an authentication attempt occurs, the TOE first
increments the failed login counter, and then checks the validity of the password by providing it to
Android’s Gatekeeper (which runs in the Trusted Execution Environment).


Any visual error to the user about a failed entry is displayed after the validation check. Android’s
Gatekeeper keeps this password counter in persistent secure storage and increments the counter before
validating the password. Upon successful validation of the password, this counter is reset back to zero. If
the login attempt is a failure and the counter is equal or greater than the specified value the device will
be wiped. By storing the counter persistently, and by incrementing the counter prior to validating it, the
TOE ensures a correct tally of failed attempts even if it loses power.


Table 36 lists the supported biometric fingerprint sensors for each device.









|Device:|Ultrasonic|Under<br>Display|Power<br>Button|
|---|---|---|---|
|Pixel 9 Pro XL/9<br>Pro/9/9a|X|||
|Pixel 8 Pro/8/8a||X||
|Pixel Tablet|||X|
|Pixel 9 Pro Fold/Fold|||X|
|Pixel 7 Pro/7/7a||X||
|Pixel 6 Pro/6/6a||X||


_**Table 36 - Supported Biometric Modalities**_


Additionally, the phone allows the user to unlock the device using their fingerprint. The TOE (through a
separate counter) allows users up to 5 attempts to unlock the device via fingerprint before the TOE
completely disables the fingerprint sensor. Once the TOE has disabled the fingerprint unlock entirely, it
remains disabled until the user enters their password to unlock the device. Note that restarting the
phone at any point disables the fingerprint sensor automatically until the user enters a correct password
and unlocks the phone, and therefore TOE restart disruptions are not applicable for biometric
authentication mechanisms.


**MOD_BT_V1.0:FIA_BLT_EXT.1:**
The TOE requires explicit user authorization before it will pair with a remote Bluetooth device. When
pairing with another device, the TOE requires that the user either confirm that a displayed numeric

85 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


passcode matches between the two devices or that the user enter (or choose) a numeric passcode that
the peer device generates (or must enter). The TOE requires this authorization (via manual input) for
mobile application use of the Bluetooth trusted channel and in situations where temporary (nonbonded) connections are formed.


**MOD_BT_V1.0:FIA_BLT_EXT.2:**
The TOE does not allow any data transfers with remote devices that have not been paired or authorized
by the user of the TOE. All Bluetooth connections require initial approval by the user in the user
interface and cannot be done programmatically. Bluetooth pairing (RFCOMM connections) is completed
by confirming/entering a displayed passcode in the user interface. TOE support for OBEX (OBject
EXchange) through L2CAP (Logical Link Control and Adaptation Protocol) requires the user to explicitly
authorize the transfer via a popup that will be displayed to the user.


**MOD_BT_V1.0:FIA_BLT_EXT.3:**
The TOE rejects duplicate Bluetooth connections by only allowing a single session per paired device. This
ensures that when the TOE receives a duplicate session attempt while the TOE already has an active
session with that device, then the TOE ignores the duplicate session.


**MOD_BT_V1.0:FIA_BLT_EXT.4:**
The TOE’s Bluetooth host and controller supports Bluetooth Secure Simple Pairing and the TOE utilizes
this pairing method when the remote host also supports it.


**MOD_BT_V1.0:FIA_BLT_EXT.6:**
The TOE requires explicit user authorization before granting trusted (paired) remote devices access to
services associated with the OPP and MAP Bluetooth profiles.


**MOD_BT_V1.0:FIA_BLT_EXT.7:**
The TOE requires explicit user authorization before granting untrusted (unpaired) remote devices access
to services associated with OPP and MAP Bluetooth profiles.


**MOD_MDM_AGENT_V1.0:FIA_ENR_EXT.2:**
[The MDM Agent is configured to connect to https://m.google.com](https://m.google.com/) for all communications with the
MDM Server. All communications between the MDM Server and the MDM Agent are handled through
the public server at this address and this address cannot be changed.


**MOD_BIO_V1.1:FIA_MBE_EXT.1:**
The TOE provides a mechanism to enroll user biometrics for authentication. The enrollment process
requires the user to have a password set and to be authenticated successfully prior to being able to start
the enrollment process. The user is able to enroll multiple fingerprints individually for use on supported
devices.


**MOD_BIO_V1.1:FIA_MBE_EXT.2:**
**MOD_BIO_V1.1:FIA_MBV_EXT.2:**
The TSF determines the quality of a sample before using it to enroll or verify a user. The fingerprint
systems utilize different capture sensors, but the data analysis of the quality is common. Fingerprint
[sample quality is determined using three measures. While the Android 15 CDD](https://source.android.com/docs/compatibility/15/android-15-cdd#7310_biometric_sensors) does not specify exactly
how the quality measure must be performed, [Class 3](https://source.android.com/docs/security/features/biometric/measure#biometric-classes) provides strict requirements in terms of
performance, leading to the measures here.


86 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


The first measure is the completeness of the sample. For example, if the finger is offset from the sensor
and only half the sensor acquires an image of the fingerprint, this partial print would be considered
insufficient quality, as there is not enough data to make a match. This can also be triggered by events
like a too light or too hard touch on the sensor (either of which can cause parts of the sensor to not
sense the minutia of the fingerprint).


The second measure is the clarity of the sample. Clarity is determined primarily by how clear the
fingerprint minutia are in the sample image. Areas with no minutia (that is not a partial image), such as
when a finger is dirty, or if the sensor is blocked, will be considered as insufficient quality. Repeated
attempts with similar results will trigger a user notification that the sensor or finger may be dirty so the
user can consider remediation (such as wiping the sensor or washing the finger).


The third measure is a sufficient number of minutia within the sample. If the sample is complete and
clear, then it is checked for a sufficient number of minutia to be used. The system looks for a large
number of data points from which to build the template for use. While there is not a specific number,
the check looks not only at the number but the distribution across the sample. For example if 50 points
are needed but 40 points are found in only half the image this would be rejected, the distribution of the
minutia would still cause the sample to fail.


Each of these measures is used independently to determine whether the sample has sufficient quality.


These measures are used on any sample that is collected, regardless of the purpose of the sample
(enrollment or verification).


**MOD_BIO_V1.1:FIA_MBV_EXT.1/PBFPS:**
**MOD_BIO_V1.1:FIA_MBV_EXT.1/UDFPS:**
**MOD_BIO_V1.1:FIA_MBV_EXT.1/USFPS:**
The TOE’s fingerprint sensor provides FAR and FRR rates as shown in Table 37. The FAR and FRR rates
provide a rating as to the protection provided against “live” attacks where-in a live fingerprint is
presented to the sensor with regards to ensuring a proper match to the enrolled fingerprint(s). Each
phone provides a FRR of shown in the table below, along with a rounded up (for the worse) and mapped
ratio. Prior to the rounded rate, the FRR meets the requirements for FIA_BMG_EXT in all cases.


Users have up to 5 attempts to unlock the phone using fingerprint before the fingerprint unlock method
is disabled for 30 seconds. After the 4th unsuccessful round of unlock attempts (a total of 20 fingerprint
attempts), the fingerprint sensor is disabled entirely and the user is prompted for their password. The
fingerprint unlock remains disabled until the user enters their password.









|Device|Sensor Type|False Accept Rate<br>(FAR)|False Reject Rate<br>(FRR)|Imposter Attack<br>Presentation<br>Accept Rate<br>(IAPAR)|
|---|---|---|---|---|
|Pixel 9 Pro XL/9<br>Pro/9/9a|USFPS|1:50,000|2.5%|7%|
|Pixel 8 Pro/8/8a|UDFPS|UDFPS|UDFPS|UDFPS|
|Pixel Tablet|PBFPS|PBFPS|PBFPS|PBFPS|
|Pixel 9 Pro Fold/Fold|PBFPS|PBFPS|PBFPS|PBFPS|
|Pixel 7 Pro/7/7a|UDFPS|UDFPS|UDFPS|UDFPS|
|Pixel 6 Pro/6/6a|UDFPS|UDFPS|UDFPS|UDFPS|


_**Table 37 - Fingerprint False Accept/Reject Rates**_



87 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


**MOD_BIO_V1.1:FIA_MBV_EXT.3:**
To provide protection against artefacts (beyond just properly matching a live fingerprint correctly), the
TOE’s fingerprint sensor provides an IAPAR rate not exceeding 7% based on a biometric transaction
using a built-in presentation attack detection (PAD) mechanism. An artefact is a non-live item (i.e. not a
live person attempting to use their own actual fingerprint directly) that is presented to the sensor as a
valid attempt at authentication.


The PAD mechanism, in addition to the normal quality checks of the biometric sample, checks for
additional information in the sample that can be used to determine if the sample is from an imposter.
The IAPAR is measured as the Spoof Acceptance Rate as shown in the Android biometric testing
[requirements here at Measuring Biometric Unlock Security.](https://source.android.com/docs/security/features/biometric/measure#fingerprint-authentication)


**MOD_WLANC_V1.0:FIA_PAE_EXT.1:**
The TOE can join WPA2-802.1X (802.11i) and WPA3-Enterprise wireless networks requiring EAP-TLS
authentication, acting as a client/supplicant (and in that role connect to the 802.11 access point and
communicate with the 802.1X authentication server).


**PP_MDF_V3.3:FIA_PMG_EXT.1:**
The TOE authenticates the user through a password consisting of basic Latin characters (upper and
lower case, numbers, and the special characters noted in the selection (see the selections in section 5
for FIA_PMG_EXT.1)). The TOE defaults to requiring passwords to have a minimum of four characters
but no more than sixteen, contain at least one letter; however, an MDM application can change these
defaults. The Smart Lock feature is not allowed in the evaluated configuration as this feature
circumvents the requirements for FIA_PMG_EXT.1 and many others.


**PP_MDF_V3.3:FIA_TRT_EXT.1:**
Android’s GateKeeper throttling is used to prevent brute-force attacks. After a user enters an incorrect
password or a failed biometric, GateKeeper APIs return a value in milliseconds (500ms default) in which
the user must wait before another authentication attempt. Any attempts before the defined amount of
time has passed will be ignored by GateKeeper. Gatekeeper also keeps a count of the number of failed
authentication attempts since the last successful attempt. These two values together are used to
prevent brute-force attacks of the TOE.


**PP_MDF_V3.3:FIA_UAU.5:**
The TOE, in its evaluated configuration, allows the user to authenticate using either a password or
biometric (see Table 36). Upon boot, the first unlock screen presented requires the user to enter their
password to unlock the device. The biometric sensors are disabled until the user enters their password
for the first time.


Upon device lock during normal use of the device, the user has the ability to unlock the phone either by
entering their password or by using a biometric authentication. Throttling of these inputs can be read
about in the FIA_TRT_EXT.1 section. The entered password is compared to a value derived as described
in the key hierarchy and key table above (FCS_STG_EXT.2 and FCS_CKM_EXT.3, respectively).
FIA_MBV_EXT.1 describes the biometric authentication process and its security measures.


Some security related user settings (e.g. changing the password, modifying, deleting, or adding stored
fingerprint templates, Smart Lock settings, etc.) and actions (e.g. factory reset) require the user to enter


88 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


their password before modifying these settings or executing these actions. In these instances, biometric
authentication is not accepted to permit the referenced functions.


The TOE’s evaluated configuration disallows other authentication mechanisms, such as pattern, PIN, or
Smart Lock mechanisms (on-body detection, trusted places, trusted devices, and trusted voice).


**PP_MDF_V3.3:FIA_UAU.6/CREDENTIAL:**
**PP_MDF_V3.3:FIA_UAU.6/LOCKED:**
The TOE requires the user to enter their password or supply their biometric in order to unlock the TOE.
Additionally the TOE requires the user to confirm their current password when accessing the

```
  Settings -> Security -> Screen lock

```

menu in the TOE’s user interface. The TOE can disable Smart Lock through management controls. Only
after entering their current user password can the user then elect to change their password.


**PP_MDF_V3.3:FIA_UAU.7:**
The TOE allows the user to enter the user's password from the lock screen. The TOE will, by default,
display the most recently entered character of the password briefly or until the user enters the next
character in the password, at which point the TOE obscures the character by replacing the character
with a dot symbol. Further, the TOE provides no feedback other than whether the fingerprint unlock
attempt succeeded or failed.


**PP_MDF_V3.3:FIA_UAU_EXT.1:**
As described in FCS_STG_EXT.2, the TOE’s key hierarchy requires the user's password in order to derive
the KEK_* keys in order to decrypt other KEKs and DEKs. Thus, until it has the user's password, the TOE
cannot decrypt the DEK utilized for Data-At-Rest encryption, and thus cannot decrypt the user’s
protected data.


**PP_MDF_V3.3:FIA_UAU_EXT.2:**
The TOE, when configured to require a user password, allows a user to perform the actions assigned in
FIA_UAU_EXT.2.1 (see selections in section 5 for FIA_UAU_EXT.2) without first successfully
authenticating. Choosing the input method allows the user to select between different keyboard devices
(say, for example, if the user has installed additional keyboards). Note that the TOE automatically names
and saves (to the internal Flash) any screen shots or photos taken from the lock screen, and the TOE
provides the user no opportunity to name them or change where they are stored.


When configured, the user can also launch Google Assistant to initiate some features of the phone.
However, if the command requires access to the user’s data (e.g. contacts for calls or messages), the
phone requires the user to manually unlock the phone before the action can be completed.


Beyond those actions, a user cannot perform any other actions other than observing notifications
displayed on the lock screen until after successfully authenticating. Additionally, the TOE provides the
user the ability to hide the contents of notifications once a password (or any other locking
authentication method) is enabled.


**PP_MDF_V3.3:FIA_X509_EXT.1:**
The TOE checks the validity of all imported CA certificates by checking for the presence of the
basicConstraints extension and that the CA flag is set to TRUE as the TOE imports the certificate. In
addition to the check during import, the checks will be done again at use of the CA certificate.


89 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


The TOE’s certificate validation algorithm examines each certificate in the path (starting with the peer’s
certificate) and first checks for validity of that certificate (e.g., has the certificate expired; or if not yet
valid, whether the certificate contains the appropriate X.509 extensions [e.g., the CA flag in the basic
constraints extension for a CA certificate, or that a server certificate contains the Server Authentication
purpose in the extendedKeyUsage field]), then verifies each certificate in the chain (applying the same
rules as above, but also ensuring that the Issuer of each certificate matches the Subject in the next rung
“up” in the chain and that the chain ends in a self-signed certificate present in either the TOE’s trusted
anchor database or matches a specified Root CA), and finally the TOE performs revocation checking for
all certificates in the chain.


For certificates imported into the TOE (not CA certificates), the checks on the validity of the certificate
are performed on use, not on import. An imported certificate that does not meet the requirements for
basicConstraints will fail.


**MOD_WLANC_V1.0:FIA_X509_EXT.1/WLAN:**
In addition to the checks performed as part of PP_MDF_V3.3:FIA_X509_EXT.1, the TOE verifies the
extendedKeyUsage Server Authentication purpose during WPA2/EAP-TLS negotiation.


**PP_MDF_V3.3:FIA_X509_EXT.2:**
**MOD_WLANC_V1.0:FIA_X509_EXT.2/WLAN:**
**MOD_WLANC_V1.0:FIA_X509_EXT.6:**
The TOE uses X.509v3 certificates during EAP-TLS, TLS, and HTTPS. The TOE comes with a built-in set of
default Trusted Credentials (Android's set of trusted CA certificates), and while the user cannot remove
any of the built-in default CA certificates, the user can disable any of those certificates through the user
interface so that certificates issued by disabled CA’s cannot validate successfully. In addition, a user and
an administrator/MDM can import a new trusted CA certificate into the Trust Anchor Database (the TOE
stores the new CA certificate in the Security Key Store).


The certificates that will be used to establish EAP-TLS, TLS or HTTPS connections are stored in the key
store specified in FCS_STG_EXT.1.


The TOE does not establish TLS connections itself (beyond EAP-TLS used for WPA2/WPA3 Wi-Fi
connections), but provides a series of APIs that mobile applications can use to check the validity of a
peer certificate. The mobile application, after correctly using the specified APIs, can be assured as to the
validity of the peer certificate and be assured that the TOE will not establish the trusted connection if
the peer certificate cannot be verified (including validity, certification path, and revocation [through
OCSP]). If, during the process of certificate verification, the TOE cannot establish a connection with the
server acting as the OCSP Responder, the TOE will not deem the server’s certificate as valid and will not
establish a TLS connection with the server.


For mobile applications, the application developer will specify whether the TOE should use the Android
system Trusted CAs, use application-specified trusted CAs, or a combination of the two. In this way, the
TOE always knows which trusted CAs to use.


The user or administrator explicitly specifies the trusted CA that the TOE will use for EAP-TLS
authentication of the server’s certificate when creating a new EAP-TLS connection. The certificates are
all checked at use.


90 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


The TOE, when acting as a WPA2/WPA3 supplicant uses X.509 certificates for EAP-TLS authentication.
Because the TOE may not have network connectivity to a revocation server prior to being admitted to
the WPA2/WPA3 network and because the TOE cannot determine the IP address or hostname of the
authentication server (the Wi-Fi access point proxies the supplicant’s authentication request to the
server), the TOE will accept the certificate of the server.


**PP_MDF_V3.3:FIA_X509_EXT.3:**
Applications needing compliant revocation checking must utilize the NIAPSEC library. The NIAPSEC
library created by the vendor provides the following functions to allow for certificate path validation and
revocation checking:

  - public boolean isValid(List<Certificate> certs)

  - public Boolean isValid(Certificate cert)
The first function allows for validation and revocation checking against a list of certificates, while the
second checks a singular certificate. Revocation checking is completed using OCSP. Please see the
FIA_X509_EXT.2/WLAN section for a further explanation on how the TOE handles revocation checking.

### 6.5 Security management


**MOD_MDM_AGENT_V1.0:FMT_POL_EXT.2**
The TOE only accepts policies which have been signed with the private key of the MDM Server. The
public key (downloaded as part of the enrollment process) is used to verify the integrity of the policy
file. If the signature of the file is confirmed, the policy is applied and the MDM Server is notified of the
successful application. If the policy signature check fails, the policy file is discarded and the MDM Server
is notified of the failure.


**PP_MDF_V3.3:FMT_MOF_EXT.1:**
**PP_MDF_V3.3:FMT_SMF.1:**
The TOE provides the management functions described in Table 16 - Security Management Functions in
section 5. The table includes annotations describing the roles that have access to each service and how
to access the service. The TOE enforces administrative configured restrictions by rejecting user
configuration (through the UI) when attempted. It is worth noting that the TOE’s ability to specify
authorized application repositories takes the form of allowing enterprise applications (i.e., restricting
applications to only those applications installed by an MDM Agent).


**MOD_BT_V1.0:FMT_SMF_EXT.1/BT:**
The TOE provides the management functions described in Table 17 - Bluetooth Security Management
Functions in section 5. The TOE enforces administrative configured restrictions by rejecting user
configuration (through the UI) when attempted.


**MOD_WLANC_V1.0:FMT_SMF_EXT.1/WLAN:**
The TOE provides the management functions described in Table 18 - WLAN Security Management
Functions in section 5. As with Table 16 - Security Management Functions, the table includes
annotations describing the roles that have access to each service and how to access the service. The TOE
enforces administrative configured restrictions by rejecting user configuration (through the UI) when
attempted.


**PP_MDF_V3.3:FMT_SMF_EXT.2:**


91 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


The TOE offers MDM agents the ability to wipe protected data, wipe sensitive data, remove Enterprise
applications, and remove all device stored Enterprise resource data upon un-enrollment. The TOE offers
MDM agents the ability to wipe protected data (effectively wiping the device) at any time. Similarly, the
TOE also offers the ability to remove Enterprise applications and a full wipe of managed profile data of
the TOE’s Enterprise data/applications at any time. These capabilities are available as APIs that can be
set through the MDM and then passed to the MDM agent to apply (and start the action as specified).


**PP_MDF_V3.3:FMT_SMF_EXT.3:**
The TOE offers MDM agents and the user the

```
  Settings -> Security -> Advanced settings -> Device admin apps

```

menu to view each application that has been granted admin rights, and further to see what operations
each admin app has been granted.


**MOD_MDM_AGENT_V1.0:FMT_SMF_EXT.4:**
During the enrollment process, the TOE will import the MDM Server public key to be used for verifying
the policy signature. This is handled automatically as part of the process and not as a separate function.
The certificates necessary for connecting to the MDM Server are already present on the device.


The TOE supports the management functions of the PP_MDF_V3.3 as specified in Table 16 - Security
Management Functions, Table 17 - Bluetooth Security Management Functions and Table 18 - WLAN
Security Management Functions. The TOE itself supports the enrollment into a managed configuration
with an MDM Server but does not provide other self-management capabilities.


**MOD_MDM_AGENT_V1.0:FMT_UNR_EXT.1:**
When the TOE is unenrolled from management via the MDM Server, the TOE will perform remediation
actions to the managed applications and data on the device. The user cannot initiate unenrollment (the
user can perform a factory reset, but cannot unenroll from management). The admin can block the
ability of the user to perform the factory reset. The remediation action is a wipe of all data on the device
(a factory reset).

### 6.6 Protection of the TSF


**PP_MDF_V3.3:FPT_AEX_EXT.1:**
The Linux kernel of the TOE’s Android operating system provides address space layout randomization
utilizing the get_random_int(void) kernel random function to provide eight unpredictable bits to the
base address of any user-space memory mapping. The random function, though not cryptographic,
ensures that one cannot predict the value of the bits.


**PP_MDF_V3.3:FPT_AEX_EXT.2:**
[The TOE utilizes the 6.1 Linux kernel (https://source.android.com/devices/architecture/kernel/modular-](https://source.android.com/devices/architecture/kernel/modular-kernels#core-kernel-requirements)
[kernels#core-kernel-requirements), whose memory management unit (MMU) enforces read, write, and](https://source.android.com/devices/architecture/kernel/modular-kernels#core-kernel-requirements)
execute permissions on all pages of virtual memory and ensures that write and execute permissions are
not simultaneously granted on all memory. The Android operating system sets the ARM No eXecute (XN)
bit on memory pages and the TOE’s ARMv8 Application Processor’s Memory Management Unit (MMU)
circuitry enforces the XN bits. From Android’s documentation
(https://source.android.com/devices/tech/security/index.html), Android supports 'Hardware-based No
eXecute (NX) to prevent code execution on the stack and heap. Section D.5 of the ARMv8 Architecture


92 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


Reference Manual contains additional details about the MMU of ARM-based processors:
[http://infocenter.arm.com/help/index.jsp?topic=/com.arm.doc.ddi0487a.f/index.html.](http://infocenter.arm.com/help/index.jsp?topic=/com.arm.doc.ddi0487a.f/index.html)


**PP_MDF_V3.3:FPT_AEX_EXT.3:**
The TOE’s Android operating system provides explicit mechanisms to prevent stack buffer overruns in
addition to taking advantage of hardware-based No eXecute to prevent code execution on the stack and
heap. Specifically, the vendor builds the TOE (Android and support libraries) using gcc-fstack-protector
compile option to enable stack overflow protection and Android takes advantage of hardware-based
eXecute-Never to make the stack and heap non-executable. The vendor applies these protections to all
TSF executable binaries and libraries.


**PP_MDF_V3.3:FPT_AEX_EXT.4:**
The TOE protects itself from modification by untrusted subjects using a variety of methods. The first
protection employed by the TOE is a Secure Boot process that uses cryptographic signatures to ensure
the authenticity and integrity of the bootloader and kernels using data fused into the device processor.


The TOE protects its REK by limiting access to only trusted applications within the TEE (Trusted Execution
Environment). The TOE key manager includes a TEE module that utilizes the REK to protect all other keys
in the key hierarchy. All TEE applications are cryptographically signed, and when invoked at runtime (at
the behest of an untrusted application), the TEE will only load the trusted application after successfully
verifying its cryptographic signature.


The TOE protects biometric data by separating it from the Android operating system. The biometric
sensor is tied to the TEE such that it cannot be accessed directly from Android but can only be done
through the biometric software inside the TEE. All biometric data is maintained within the TEE such that
Android is only able to know the result of a biometric process (such as enrollment or verification), and
not any of the data used in that process itself.


Additionally, the TOE’s Android operating system provides 'sandboxing' that ensures that each thirdparty mobile application executes with the file permissions of a unique Linux user ID, in a different
virtual memory space. This ensures that applications cannot access each other’s memory space or files
and cannot access the memory space or files of other applications (notwithstanding access between
applications with a common application developer).


While the TOE supports USSD and MMI codes, they are only available once the user has authenticated
to the TOE through the dialer. Attempting to access these codes through the emergency dialer will be
rejected as a non-emergency number.


The TOE, in its evaluated configuration has its bootloader in the locked state. This prevents a user from
installing a new software image via another method than Google’s proscribed OTA methods. The TOE
allows an operator to download and install an OTA update through the system settings

```
  Settings -> System -> System update -> Check for update

```

while the phone is running, or by separately downloading an OTA image, and then “sideloading” the
OTA update from Android’s recovery mode. In both cases, the TOE will verify the digital signature of the
new OTA before applying the new firmware.


For the first install of the Common Criteria compliant build, the administrator must unlock the device’s
bootloader via the fastboot interface, “sideload” the correct build, reboot the phone back to the
fastboot interface, re-lock the bootloader, and finally start the phone normally. For both the locking and


93 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


unlocking of the bootloader, the device is factory reset as part of the process. This prevents an attacker
from modifying or switching the image running on the device to allow access to sensitive data. After this
first install of the official build, further updates can be done via normal OTA updates.


**PP_MDF_V3.3:FPT_AEX_EXT.5:**
The TOE models provide Kernel Address Space Layout Randomization (KASLR) as a hardening feature to
randomize the location of kernel data structures at each boot, including the core kernel as a random
physical address, mapping the core kernel at a random virtual address in the vmalloc area, loading
kernel modules at a random virtual address in the vmalloc area, and mapping system memory at a
random virtual address in the linear area. The entropy used to dictate the randomization is based on the
hardware present within the phone. For ARM devices, such as the TOE, 13-25 bits of entropy are
generated on boot from the DRBG in the Application Processor, from which the starting memory
address is generated.


**PP_MDF_V3.3:FPT_BBD_EXT.1:**
The TOE’s hardware and software architecture ensures separation of the application processor (AP)
from the baseband or communications processor (CP) through internal controls of the TOE’s SoC, which
contains both the AP and the CP. The AP restricts hardware access control through a protection unit that
restricts software access from the baseband processor through a dedicated 'modem interface'. The
protection unit combines the functionality of the Memory Protection Unit (MPU), the Register
Protection Unit (RPU), and the Address Protection Unit (APU) into a single function that conditionally
grants access by a master to a software defined area of memory, to registers, or to a pre-decoded
address region, respectively. The modem interface provides a set of APIs (grouped into five categories)
to enable a high-level OS to send messages to a service defined on the modem/baseband processor. The
combination of hardware and software restrictions ensures that the TOE’s AP prevents software
executing on the modem or baseband processor from accessing the resources of the application
processor (outside of the defined methods, mediated by the application processor).


**MOD_BIO_V1.1:FPT_BDP_EXT.1:**
The complete biometric authentication process happens inside the TEE (including image capture, all
processing and match determination). All software in the biometric system is inside the TEE boundary,
while the sensors are accessible from within Android. The TEE handles calls for authentication made
from Android with only the success or failure of the match provided back to Android (and when
applicable, to the calling app). The image taken by the capture sensor is processed by the biometric
service to check the enrolled templates for a match to the captured image.


**PP_MDF_V3.3:FPT_JTA_EXT.1:**
The TOE prevents access to its processor’s JTAG interface by requiring use of a signing key to
authenticate prior to gaining JTAG access. Only a JTAG image with the accompanying device serial
number (which is different for each mobile device) that has been signed by Google’s private key can be
used to access a device’s JTAG interface. The Google private key corresponds to the Google RSA 2048-bit
public key (a SHA-256 hash of which is fused into the TOE’s application processor).


**PP_MDF_V3.3 & MOD_BIO_V1.1:FPT_KST_EXT.1:**
The TOE does not store any plaintext key or biometrics material in its internal Flash; the TOE encrypts all
keys and biometric data before storing them. This ensures that irrespective of how the TOE powers
down (e.g., a user commands the TOE to power down, the TOE reboots itself, or battery depletes or is
removed), all keys and biometric data stored in the internal Flash are wrapped with a KEK. Please refer


94 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


to section 6.2 of the TSS for further information (including the KEK used) regarding the encryption of
keys stored in the internal Flash. As the TOE encrypts all keys stored in Flash, upon boot-up, the TOE
must first decrypt any keys in order to utilize them.


**PP_MDF_V3.3 & MOD_BIO_V1.1:FPT_KST_EXT.2:**
The TOE itself (i.e., the mobile device) comprises a cryptographic module that utilizes cryptographic
libraries including BoringSSL, application processor cryptography (which leverages AP hardware), and
the following system-level executables that utilize KEKs: vold, wpa_supplicant, and the Android Key
Store.


1. vold and application processor hardware provides Data-At-Rest encryption of the user data

partition in Flash using the Google Tensor Inline Storage Encryption (ISE)
2. wpa_supplicant provides WPA2/WPA3 services
3. the Android Key Store application provides key generation, storage, deletion services to mobile

applications and to user through the UI
The TOE ensures that plaintext key material is not exported by not allowing the REK to be exported and
by ensuring that only authenticated entities can request utilization of the REK. Furthermore, the TOE
only allows the system-level executables access to plaintext DEK values needed for their operation. The
TSF software (the system-level executables) protects those plaintext DEK values in memory both by not
providing any access to these values and by clearing them when no longer needed (in compliance with
FCS_CKM_EXT.4). Note that the TOE does not use the biometric template to encrypt/protect key
material (and instead only relies upon the user’s password).


The TOE also ensures that biometric data used for enrolling and authenticating users can not be
exported. During authentication or enrollment, the calling program (the TSF or an app) is able to request
biometric actions, but the data resulting from that action is not provided back to the calling program.
The calling program only receives a notice of success of failure about the process.


**PP_MDF_V3.3:FPT_KST_EXT.3:**
The TOE does not provide any way to export plaintext DEKs or KEKs (including all keys stored in the
Android Key Store) as the TOE chains or directly encrypts all KEKs to the REK.


Furthermore, the components of the device are designed to prevent transmission of key material
outside the device. Each internal system component requiring access to a plaintext key (for example the
Wi-Fi driver) must have the necessary precursor(s), whether that be a password from the user or file
access to key in Flash (for example the encrypted AES key used for encryption of the Flash data
partition). With those appropriate precursors, the internal system-level component may call directly to
the system-level library to obtain the plaintext key value. The system library in turn requests decryption
from a component executing inside the trusted execution environment and then directly returns the
plaintext key value (assuming that it can successfully decrypt the requested key, as confirmed by the
CCM/GCM verification) to the calling system component. That system component will then utilize that
key (in the example, the kernel which holds the key in order to encrypt and decrypt reads and writes to
the encrypted user data partition files in Flash). In this way, only the internal system components
responsible for a given activity have access to the plaintext key needed for the activity, and that
component receives the plaintext key value directly from the system library.


For a user’s mobile applications, those applications do not have any access to any system-level
components and only have access to keys that the application has imported into the Android Key Store.


95 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


Upon requesting access to a key, the mobile application receives the plaintext key value back from the
system library through the Android API. Mobile applications do not have access to the memory space of
any other mobile application so it is not possible for a malicious application to intercept the plaintext
key value to then log or transmit the value off the device.


**PP_MDF_V3.3:FPT_NOT_EXT.1:**
When the TOE encounters a critical failure (either a self-test failure or TOE software integrity verification
failure), the TOE attempts to reboot. If the failure persists between boots, the user may attempt to boot
to the recovery mode/kernel to wipe data and perform a factory reset in order to recover the device.


**MOD_BIO_V1.1:FPT_PBT_EXT.1:**
The TOE requires the user to enter their password to enroll, re-enroll or unenroll any biometric
templates. When the user attempts biometric authentication to the TOE, the biometric sensor takes an
image of the presented biometric for comparison to the enrolled templates. The biometric system
compares the captured image to all the stored templates on the device to determine if there is a match.


**PP_MDF_V3.3:FPT_STM.1:**
The TOE requires time for the Package Manager (which installs and verifies APK signatures and
certificates), image verifier, wpa_supplicant, and Android Key Store applications. These TOE components
obtain time from the TOE using system API calls [e.g., time() or gettimeofday()]. An application (unless a
system application is residing in /system/priv-app or signed by the vendor) cannot modify the system
time as mobile applications need the Android 'SET_TIME' permission to do so. Likewise, only a process
with root privileges can directly modify the system time using system-level APIs. Further, this stored
time is used both for the time/date tags in audit logs and is used to track inactivity timeouts that force
the TOE into a locked state.


By default, the TOE uses the Cellular Carrier time (obtained through the Carrier’s network time server)
as the trusted time source. The admin can decide to not use cellular time as the trusted source but
instead use a NTP server to set the trusted time. The default NTP server is a Google-hosted server
source, but this can be changed by the admin to point to another trusted server. It is also possible to let
the user set the date and time through the TOE’s user interface and use the internal clock to maintain a
local (as opposed to externally checked) trusted time.


**PP_MDF_V3.3:FPT_TST_EXT.1:**
The TOE automatically performs known answer power on self-tests (POST) on its cryptographic
algorithms to ensure that they are functioning correctly. Each component providing cryptography
performs known answer tests on their cryptographic algorithms to ensure they are working correctly.
Should any of the tests fail, the TOE displays an error message stating “Boot Failure” and halts the boot
process, displays an error to the screen, and forces a reboot of the device.









|Algorithm|Implemented in|Description|
|---|---|---|
|AES encryption/decryption|BoringSSL<br>Kernel<br>Storage<br>Application Processor<br>Titan chip|Comparison of known answer to calculated value|
|ECDH key agreement|BoringSSL|Comparison of known answer to calculated value|


96 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025
























|Algorithm|Implemented in|Description|
|---|---|---|
|DRBG random bit generation|BoringSSL<br>Application Processor<br>Titan chip|Comparison of known answer to calculated value|
|ECDSA sign/verify|BoringSSL|Comparison of known answer to calculated value|
|HMAC-SHA|BoringSSL<br>Kernel<br>Storage<br>Application Processor<br>Titan chip|Comparison of known answer to calculated value|
|RSA sign/verify|BoringSSL|Comparison of known answer to calculated value|
|SHA hashing|BoringSSL<br>Kernel<br>Storage<br>Application Processor<br>Titan chip|Comparison of known answer to calculated value|



_**Table 38 - Power-up Cryptographic Algorithm Known Answer Tests**_


**PP_MDF_V3.3:FPT_TST_EXT.2/PREKERNEL:**
**PP_MDF_V3.3:FPT_TST_EXT.2/POSTKERNEL:**
**MOD_WLANC_V1.0:FPT_TST_EXT.3/WLAN:**
The TOE ensures a secure boot process in which the TOE verifies the digital signature of the bootloader
software for the Application Processor (using a public key whose hash resides in the processor’s internal
fuses) before transferring control. The bootloader, in turn, verifies the signature of the Linux kernel it
loads. This series of checks occur for all boot modes (normal, recovery and fastboot). The recovery and
fastboot modes utilize the same alternative boot mode but expose different software to the user once
the boot is complete.


For any boot mode, the TOE performs checking of the entire /system and /vendor partitions through use
of Android’s dm-verity mechanism (and while the TOE will still operate, it will log any blocks/executables
that have been modified). Some limited failures (changes under a block size, depending on the location
of the failure) can be automatically self-corrected as part of the check process.


dm-verity is a hash table of the block device used for storage (in this case the /system and /vendor
partitions) where every 4k block has a SHA256. These hashes are then concatenated and every 4k of
that hash is again hashed. This is repeated until a 4k root hash is generated (this is normally 4 total
layers of hashes), and this root hash is signed with the keys used to verify the signature of the Linux
kernel. The Wi-Fi components are included in the /system partition and are verified as part of the dmverity check of that partition as part of the platform checks.


**PP_MDF_V3.3:FPT_TUD_EXT.1:**
The TOE’s user interface provides a method to query the current version of the TOE software/firmware
(Android version, baseband version, kernel version, build number, and software version) and hardware
(model and version). Additionally, the TOE provides users the ability to review the currently installed
apps (including 3rd party 'built-in' applications) and their version.


**PP_MDF_V3.3:FPT_TUD_EXT.2:**
The TOE verifies all OTA (over-the-air) updates to the TOE software (which includes baseband processor
updates) using a public key chaining ultimately to the Root Public Key, a hardware protected key whose


97 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


SHA-256 hash resides inside the application processor. Should this verification fail, the software update
will fail and the update will not be installed.


The application processor verifies the bootloader’s authenticity and integrity (thus tying the bootloader
and subsequent stages to a hardware root of trust: the SHA-256 hash of the Root Public Key, which
cannot be reprogrammed after the “write-enable” fuse has been blown).


**PP_MDF_V3.3:FPT_TUD_EXT.3:**
The Android OS on the TOE requires that all applications bear a valid signature before Android will install
the application.


Additionally, Android allows updates through Google Play updates, including both APK and APEX files.
Both file types use Android APK signature format and the TOE verifies the accompanying signature prior
to installing the file (additionally, Android ensures that updates to existing files use the same signing
certificate). APEX files are used to update low level modules within Android that are not traditional
applications and as such are not easily able to be updated using a traditional APK. These files both follow
the same format, structure and signature requirements.


**PP_MDF_V3.3:FPT_TUD_EXT.6:**
The TOE maintains a monotonic anti-rollback counter used to set a minimum version for the TOE
software. Before a new update can be installed, the version of the new software is compared to the
counter version. The update is allowed only if the version of the new software is equal or greater than
the counter. APEX files, which update the TOE software also follow the anti-rollback counter of the
device preventing downgrades.

### 6.7 TOE access


**PP_MDF_V3.3:FTA_SSL_EXT.1:**
The TOE transitions to its locked state either immediately after a User initiates a lock by pressing the
power button (if configured) or after a (also configurable) period of inactivity, and as part of that
transition, the TOE will display a lock screen (the KeyGuard lock screen) to obscure the previous
contents and play a “lock sound” to indicate the phone’s transition; however, the TOE’s lock screen still
displays email notifications, calendar appointments, user configured widgets, text message notifications,
the time, date, call notifications, battery life, signal strength, and carrier network. But without
authenticating first, a user cannot perform any related actions based upon these notifications (they
cannot respond to emails, calendar appointments, or text messages) other than the actions assigned in
Timing of Authentication (PP_MDF_V3.3:FIA_UAU_EXT.2).


The administrator can also force the device into the locked state through the use of an MDM.


Note that during power up, the TOE presents the user with an unlock screen stating “unlock for all
features and data”. While at this screen, the TOE has already decrypted Device Encrypted (DE) files
within the userdata partition, but cannot yet decrypt the user’s Credential Encrypted (CE) files. The user
can only access a subset of device functionality before authenticating (e.g. the user can making an
emergency call, receive incoming calls, receiving alarms, and any other “direct boot” functionality). After
the user enters their password, the TOE decrypts the user’s CE files within the user data partition and
the user has unlocked the full functionality of the phone. After this initial authentication, upon
(re)locking the phone, the TOE presents the user with the previously mentioned KeyGuard lock screen.
While locked, the actions described in FIA_UAU_EXT.2.1 are available for the user to utilize.

98 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025


**PP_MDF_V3.3:FTA_TAB.1:**
The TOE can be configured to display a user-specified message on the Lock screen, and additionally an
administrator can also set a Lock screen message using an MDM.


**MOD_WLANC_V1.0:FTA_WSE_EXT.1:**
The TOE allows an administrator to specify (through the use of an MDM) a list of wireless networks
(SSIDs) to which the user may direct the TOE to connect to, the security type, authentication protocol,
and the client credentials to be used for authentication. When not enrolled with an MDM, the TOE
allows the user to control to which wireless networks the TOE should connect, but does not provide an
explicit list of such networks, rather the user may scan for available wireless network (or directly enter a
specific wireless network), and then connect. Once a user has connected to a wireless network, the TOE
will automatically reconnect to that network when in range and the user has enabled the TOE’s Wi-Fi
radio.

### 6.8 Trusted path/channels


**MOD_BT_V1.0:FTP_BLT_EXT.1:**
**MOD_BT_V1.0:FTP_BLT_EXT.3/BR:**
**MOD_BT_V1.0:FTP_BLT_EXT.3/LE:**
The TSF enforces the use of encryption by default, over Bluetooth BD/EDR and LE connections using at
least 128-bit AES encryption keys and does not allow the key length to be renegotiated below the length
set at the pairing (the request to change the size will be rejected, and the connection terminated if this
is not accepted). ECDH is used to generate key pairs for the devices to exchange symmetric keys. The
admin cannot configure key sizes.


**MOD_BT_V1.0:FTP_BLT_EXT.2:**
The TSF will terminate a connection with a remote device if the remote device requests to terminate
encryption.


**PP_MDF_V3.3:FTP_ITC_EXT.1:**
The TOE provides secured (encrypted and mutually authenticated) communication channels between
itself and other trusted IT products through the use of TLS and HTTPS. The TOE provides mobile
applications and MDM agent applications access to HTTPS and TLS via published APIs, thus facilitating
administrative communication and configured enterprise connections. These APIs are accessible to any
application that needs an encrypted end-to-end trusted channel.


The TOE also uses TLS connections to download OTA updates for the device.


**MOD_WLANC_V1.0:FTP_ITC.1/WLAN:**
The TOE provides secured (encrypted and mutually authenticated) communication channels between
itself and other trusted IT products through the use of IEEE 802.11-2012, 802.1X, and EAP-TLS. The TOE
permits itself and applications to initiate communicate via the trusted channel, and the TOE initiates
communications via the WPA2/WPA3 (IEEE 802.11-2012, 802.1X with EAP-TLS) trusted channel for
connection to a wireless access point.


**MOD_MDM_AGENT_V1.0:FTP_ITC_EXT.1(2):**
**MOD_MDM_AGENT_V1.0:FTP_TRP.1(2):**
The TSF uses HTTPS connections for both enrollment with the MDM Server as well as for the
communication channel for normal operations.

99 of 100


Google Pixel Devices on Android 15 – Security Target Version: 1.0
Date: April 4, 2025

### 6.9 Live Cycle


**PP_MDF_V3.3:ALC_TSU_EXT.1:**
Google supports a bug filing system for the Android OS outlined here:
[https://source.android.com/setup/contribute/report-bugs. This allows developers or users to search for,](https://source.android.com/setup/contribute/report-bugs)
file, and vote on bugs that need to be fixed. This helps to ensure that all bugs that affect large numbers
of people get pushed up in priority to be fixed. The method outlined above requires the user to submit
their bug to Android’s website. As such, the user of the device needs to establish a trusted channel web
connection to securely file the bug by following the set-up steps to establish a secure HTTPS/TLS/EAPTLS connection from the TOE, then visiting the above web portal to submit the report.


Google also commits to pushing out monthly security updates for the Android operating system
(including the Java layer and kernel, not including applications). Google provides security updates for at
least three years from the device launch. The latest information about this can be found at
[https://support.google.com/nexus/answer/4457705 (for smartphones) and](https://support.google.com/nexus/answer/4457705)
[https://support.google.com/googlepixeltablet/answer/13399216](https://support.google.com/googlepixeltablet/answer/13399216) (for tablets), summarized in Table 39.

|Device|Android<br>updates to|Security<br>patched to|
|---|---|---|
|Pixel 9 Pro XL/9<br>Pro/9/9 Pro Fold/9a|Aug 2031|Aug 2031|
|Pixel 8a|May 2031|May 2031|
|Pixel 8 Pro/8|Oct 2030|Oct 2030|
|Pixel Tablet|Jun 2026|Jun 2028|
|Pixel Fold|Jun 2028|Jun 2028|
|Pixel 7 Pro/7|Oct 2027|Oct 2027|
|Pixel 7a|May 2028|May 2028|
|Pixel 6 Pro/6|Oct 2026|Oct 2026|
|Pixel 6a|Jul 2027|Jul 2027|



_**Table 39 - Security Update Period**_


These systematic updates are designed to address the highest issue problems as quickly as possible and
allows Google to ensure their Pixel products remain as safe as possible and any issues are addressed
promptly. Google posts Android Security Bulletins with each release showing the patches that are
[included https://source.android.com/docs/security/bulletin.](https://source.android.com/docs/security/bulletin)


Google creates updates and patches to resolve reported issues as quickly as possible. The delivery time
for resolving an issue depends on the severity, and can be as rapid as a few days before the update can
be deployed for high priority cases. Google maintains a security blog (https://android[developers.googleblog.com/) to disseminate information directly to the public.](https://android-developers.googleblog.com/)


100 of 100



