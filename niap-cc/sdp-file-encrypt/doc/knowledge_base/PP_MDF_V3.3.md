Consider using the pymupdf_layout package for a greatly improved page layout analysis.
# **Mobile Device Fundamentals**

Version: 3.3

2022-09-12
**National Information Assurance Partnership**


**Revision History**


**Version** **Date** **Comment**



1.1 201401-12


2.0 201509-14


3.0 201509-17


3.1 201704-05





Typographical changes and additional clarifications in application notes. Removed
assignment from FCS_TLS_EXT.1 and limited testing to those ciphersuites in both
FCS_TLS_EXT.1 and FCS_TLS_EXT.2.


Included changes based on Technical Rapid Response Team Decisions. Clarified many
requirements and evaluation activities. Mandated objective requirements:

Application Access Control (FDP_ACF_EXT.1.2)
VPN Information Flow Control (FDP_IFC_EXT.1)

Added new objective requirements:

Suite B cryptography for IEEE 802.11
Certificate enrollment
Protection of additional key material types
Heap overflow protection
Bluetooth requirements
Cryptographic operation services for applications
Remote Attestation (FPT_NOT_EXT.1)

Added transition dates for some objective requirements.
Included hardware-isolated REK and key storage selections.
Allowed key derivation by REK.
Clarified FTP_ITC_EXT.1 and added FDP_UPC_EXT.1.
Mandated HTTPS and TLS for application use. (FDP_UPC_EXT.1)
Removed Dual_EC_DRBG as an approved DRBG.
Adopted new TLS requirements.
Mandated TSF Wipe upon authentication failure limit and required number of
authentication failures be maintained across reboot.
Clarified Management Class.
Included more domain isolation discussion and tests.
Updated Audit requirements and added Auditable Events table.
Added SFR Category Mapping Table.
Updated Use Case Templates.
Moved Glossary to Introduction.


Included changes based on Technical Rapid Response Team Decisions.
Clarified many requirements and evaluation activities.
Mandated objective requirements:

Generation of Audit Records (FAU_GEN.1)
Audit Storage Protection (FAU_STG.1)
Audit Storage Overwrite (FAU_STG.4)
Lock Screen DAR (FDP_DAR_EXT.2)
Discard Bluetooth Connection Attempts from Bluetooth Addresses with Existing
Connection (FIA_BLT_EXT.3)
JTAG Disablement (FPT_JTA)

Added new objective requirements:

Application Backup
Biometric Authentication Factor
Access Control
User Authentication
Bluetooth Encryption

WLAN client requirements moved to Extended Package for WLAN Client.
Added SFRs to support BYOD Use Case
BYOD Use Case
Updated key destruction SFR


Included changes based on Technical Rapid Response Team Decisions and incorporated
Technical Decisions.
Modified biometric requirements:

FIA_UAU.5 - Added iris, face, voice and vein as supported modalities, in addition to
fingerprint (allowed in version 3)
FIA_BMG_EXT.1.1 - Clarified AA to specify that vendor evidence is acceptable and
expectations of evidence provided.
FIA_BMG_EXT.1.2 - SAFAR was changed to an assignment of a SAFAR no greater
than 1:500.
FIA_AFL_EXT.1 - Updated to allow each biometric modality to utilize an individual or
shared counter.

FCS_TLSC_EXT.1.1 - Removed TLS ciphersuites that utilized SHA1 and updated optional
ciphersuites to be uniformed across PPs.
FCS_STG_EXT.2.2 - Modified to require long term trusted channel key material be
encrypted by an approved method.


3.2 202104-15


3.3 202209-12


**Contents**



FIA_UAU_EXT.1.1 - Modified to allow the long term trusted channel key material to be
available prior to password being entered at start-up.


Removed TLS SFRs and utilized TLS Functional Package
Removed Bluetooth SFRs and utilized Bluetooth Module. Bluetooth SFR moved to
Implementation Dependent.
FPT_TUD_EXT.4.2 renumbered to FPT_TUD_EXT.5.1


Integrated Biometrics cPP Module, Included changes based on Technical Rapid Response
Team Decisions and open issues from GitHub.

Removed biometric definitions from Tech Terms
Removed FDP_PBA
Removed FIA_BMG
Updated FIA_UAU.5 to support bio cPP module
Moved FTA_TAB.1 to mandatory
Moved FAU_SAR.1 to mandatory
Added ECD
Updated WLAN Client reference from Extended Package to Module
Removed Diffie-Hellman group 14 selection from FCS_CKM.1.1 and
FCS_CKM.2.1/UNLOCKED



1 Introduction
1.1 Objectives of Document
1.2 Terms
1.2.1 Common Criteria Terms
1.2.2 Technical Terms
1.3 Scope of Document
1.4 Intended Readership
1.5 TOE Overview
1.6 TOE Usage
2 Conformance Claims
3 Security Problem Description
3.1 Threats
3.2 Assumptions
3.3 Organizational Security Policies
4 Security Objectives
4.1 Security Objectives for the TOE
4.2 Security Objectives for the Operational Environment
4.3 Security Objectives Rationale
5 Security Requirements
5.1 Security Functional Requirements
5.1.1 Auditable Events for Mandatory SFRs
5.1.2 Class: Security Audit (FAU)
5.1.3 Class: Cryptographic Support (FCS)
5.1.4 Cryptographic Storage (FCS_STG_EXT)
5.1.5 Class: User Data Protection (FDP)
5.1.6 Class: Identification and Authentication (FIA)
5.1.7 Class: Security Management (FMT)
5.1.8 Class: Protection of the TSF (FPT)
5.1.9 Class: TOE Access (FTA)
5.1.10 Class: Trusted Path/Channels (FTP)
5.1.11 TOE Security Functional Requirements Rationale
5.2 Security Assurance Requirements
5.2.1 Class ASE: Security Target
5.2.2 Class ADV: Development
5.2.3 Class AGD: Guidance Documentation
5.2.4 Class ALC: Life-cycle Support
5.2.5 Class ATE: Tests
5.2.6 Class AVA: Vulnerability Assessment
Appendix A - Optional Requirements
A.1 Strictly Optional Requirements
A.1.1 Class: Identification and Authentication (FIA)
A.2 Objective Requirements
A.2.1 Class: Security Audit (FAU)
A.2.2 Class: Cryptographic Support (FCS)
A.2.3 Class: User Data Protection (FDP)
A.2.4 Class: Identification and Authentication (FIA)
A.2.5 Class: Security Management (FMT)
A.2.6 Class: Protection of the TSF (FPT)
A.3 Implementation-dependent Requirements
A.3.1 Bluetooth
A.3.1.1 Class: User Data Protection (FDP)
Appendix B - Selection-based Requirements
B.1 Class: Cryptographic Support (FCS)


B.2 Class: User Data Protection (FDP)
B.3 Class: Protection of the TSF (FPT)
Appendix C - Extended Component Definitions
C.1 Extended Components Table
C.2 Extended Component Definitions
C.2.1 Class: Cryptographic Support (FCS)
C.2.1.1 FCS_CKM_EXT Cryptographic Key Management
C.2.1.2 FCS_HTTPS_EXT HTTPS Protocol
C.2.1.3 FCS_IV_EXT Initialization Vector Generation
C.2.1.4 FCS_RBG_EXT Random Bit Generation
C.2.1.5 FCS_SRV_EXT Cryptographic Algorithm Services
C.2.1.6 FCS_STG_EXT Cryptographic Key Storage
C.2.2 Class: Identification and Authentication (FIA)
C.2.2.1 FIA_AFL_EXT Authentication Failures
C.2.2.2 FIA_PMG_EXT Password Management
C.2.2.3 FIA_TRT_EXT Authentication Throttling
C.2.2.4 FIA_UAU_EXT User Authentication
C.2.2.5 FIA_X509_EXT X.509 Certificates
C.2.3 Class: Protection of the TSF (FPT)
C.2.3.1 FPT_AEX_EXT Anti-Exploitation Capabilities
C.2.3.2 FPT_BBD_EXT Baseband Processing
C.2.3.3 FPT_BLT_EXT Limitation of Bluetooth Profile Support
C.2.3.4 FPT_JTA_EXT JTAG Disablement
C.2.3.5 FPT_KST_EXT Key Storage
C.2.3.6 FPT_NOT_EXT Self-Test Notification
C.2.3.7 FPT_TST_EXT TSF Self Test
C.2.3.8 FPT_TUD_EXT TSF Updates
C.2.4 Class: Security Management (FMT)
C.2.4.1 FMT_MOF_EXT Management of Functions in TSF
C.2.4.2 FMT_SMF_EXT Specification of Management Functions
C.2.5 Class: TOE Access (FTA)
C.2.5.1 FTA_SSL_EXT Session Locking and Termination
C.2.6 Class: Trusted Path/Channels (FTP)
C.2.6.1 FTP_ITC_EXT Inter-TSF Trusted Channel
C.2.7 Class: User Data Protection (FDP)
C.2.7.1 FDP_ACF_EXT Access Control Functions
C.2.7.2 FDP_BCK_EXT Application Backup
C.2.7.3 FDP_BLT_EXT Limitation of Bluetooth Device Access
C.2.7.4 FDP_DAR_EXT Data-at-Rest Encryption
C.2.7.5 FDP_IFC_EXT Subset Information Flow Control
C.2.7.6 FDP_STG_EXT User Data Storage
C.2.7.7 FDP_UPC_EXT Inter-TSF User Data Transfer Protection
Appendix D - Validation Guidelines
Appendix E - Implicitly Satisfied Requirements
Appendix F - Entropy Documentation And Assessment
F.1 Design Description
F.2 Entropy Justification
F.3 Operating Conditions
F.4 Health Testing
Appendix G - Initialization Vector Requirements for NIST-Approved Cipher Modes
Appendix H - Use Case Templates
H.1 Enterprise-owned device for general-purpose enterprise use and limited personal use
H.2 Enterprise-owned device for specialized, high security use
H.3 Personally-owned device for personal and enterprise use
H.4 Personally-owned device for personal and limited enterprise use
Appendix I - Acronyms
Appendix J - Bibliography
Appendix K - Acknowledgments


# **1 Introduction**

**1.1 Objectives of Document**


The scope of this Protection Profile (PP) is to describe the security functionality of mobile devices in terms of

[CC] and to define functional and assurance requirements for such devices.


**1.2 Terms**


The following sections list Common Criteria and technology terms used in this document.


**1.2.1 Common Criteria Terms**


Assurance Grounds for confidence that a TOE meets the SFRs [CC].



Base
Protection
Profile (BasePP)


Collaborative
Protection
Profile (cPP)


Common
Criteria (CC)


Common
Criteria
Testing
Laboratory


Common
Evaluation
Methodology
(CEM)


Distributed
TOE


Extended
Package (EP)


Functional
Package (FP)


Operational
Environment
(OE)


Protection
Profile (PP)


Protection
Profile
Configuration
(PPConfiguration)


Protection
Profile Module
(PP-Module)


Security
Assurance
Requirement
(SAR)


Security
Functional
Requirement
(SFR)


Security
Target (ST)



Protection Profile used as a basis to build a PP-Configuration.


A Protection Profile developed by international technical communities and approved by
multiple schemes.


Common Criteria for Information Technology Security Evaluation (International Standard
ISO/IEC 15408).


Within the context of the Common Criteria Evaluation and Validation Scheme (CCEVS), an
IT security evaluation facility accredited by the National Voluntary Laboratory
Accreditation Program (NVLAP) and approved by the NIAP Validation Body to conduct
Common Criteria-based evaluations.


Common Evaluation Methodology for Information Technology Security Evaluation.


A TOE composed of multiple components operating as a logical whole.


A deprecated document form for collecting SFRs that implement a particular protocol,
technology, or functionality. See Functional Packages.


A document that collects SFRs for a particular protocol, technology, or functionality.


Hardware and software that are outside the TOE boundary that support the TOE
functionality and security policy.


An implementation-independent set of security requirements for a category of products.


A comprehensive set of security requirements for a product type that consists of at least
one Base-PP and at least one PP-Module.


An implementation-independent statement of security needs for a TOE type complementary
to one or more Base-PPs.


A requirement to assure the security of the TOE.


A requirement for security enforcement by the TOE.


A set of implementation-dependent security requirements for a specific product.



Target of The product under evaluation.


Evaluation
(TOE)


TOE Security
Functionality
(TSF)


TOE Summary
Specification
(TSS)



The security functionality of the product under evaluation.


A description of how a TOE satisfies the SFRs in an ST.



**1.2.2 Technical Terms**



Address Space
Layout
Randomization
(ASLR)



An anti-exploitation feature, which loads memory mappings into unpredictable locations.
ASLR makes it more difficult for an attacker to redirect control to code that they have
introduced into the address space of a process or the kernel.



Administrator The Administrator is responsible for management activities, including setting the policy
that is applied by the enterprise on the Mobile Device. This administrator is likely to be
acting remotely and could be the Mobile Device Management (MDM) Administrator acting
through an MDM Agent. If the device is unenrolled, the user is the administrator.



Auxiliary Boot
Modes


Biometric
Authentication
Factor (BAF)


Common
Application
Developer


Critical
Security
Parameter
(CSP)



Auxiliary boot modes are states in which the device provides power to one or more
components to provide an interface that enables an unauthenticated user to interact with
either a specific component or several components that exist outside of the device’s fully
authenticated, operational state.


Authentication factor, which uses biometric sample, matched to a biometric authentication
template to help establish identity.


Application developers (or software companies) often produce many applications under the
same name. Mobile devices often allow shared resources by such applications where
otherwise resources would not be shared.


Security-related information whose disclosure or modification can compromise the security
of a cryptographic module or authentication system.



Data Program/application or data files that are stored or transmitted by a server or Mobile
Device (MD).



Data
Encryption
Key (DEK)


Developer
Modes


Encrypted
Software Keys



A key used to encrypt data-at-rest.


Developer modes are states in which additional services are available to a user in order to
provide enhanced system access for debugging of software.


These keys are stored in the main file system encrypted by another key and can be
changed and sanitized.



Enrolled State The state in which the Mobile Device is managed with active policy settings from the
administrator.



Enterprise
Data


Ephemeral
Keys


File
Encryption
Key (FEK)


HardwareIsolated Keys


Hybrid
Authentication


Immutable
Hardware Key



Enterprise data is any data residing in the enterprise servers, or temporarily stored on
Mobile Devices to which the Mobile Device user is allowed access according to security
policy defined by the enterprise and implemented by the administrator.


These keys are stored in volatile memory.


A DEK used to encrypt a file or a director when File Encryption is used. FEKs are unique to
each encrypted file or directory.


The OS can only access these keys by reference, if at all, during runtime.


A hybrid authentication factor is one where a user has to submit a combination of a
biometric sample and a PIN or password and both must pass. If either factor fails, the
entire attempt fails. The user shall not be made aware of which factor failed, if either fails.


These keys are stored as hardware-protected raw key and cannot be changed or sanitized.



Key Chaining The method of using multiple layers of encryption keys to protect data. A top layer key
encrypts a lower layer key, which encrypts the data; this method can have any number of


Key
Encryption
Key (KEK)



layers.


A key used to encrypt other keys, such as DEKs or storage that contains keys.



Locked State Powered on but most functionality is unavailable for use. User authentication is required to
access functionality.


MDM Agent The MDM Agent is installed on a Mobile Device as an application or is part of the Mobile
Device’s OS. The MDM Agent establishes a secure connection back to the MDM Server
controlled by the administrator.


Minutia Point Friction ridge characteristics that are used to individualize a fingerprint image. Minutia
are the points where friction ridges begin, terminate, or split into two or more ridges. In
many fingerprint systems, the minutia points are compared for recognition purposes.



Mobile Device
(MD)


Mobile Device
Management
(MDM)


Mobile Device
User (User)


Modality
(Biometrics)


Mutable
Hardware Key


Operating
System (OS)


PIN
Authentication
Factor


Password
Authentication
Factor


Powered Off
State


Protected
Data (PD)


Root
Encryption
Key (REK)



A device which is composed of a hardware platform and its system software. The device
typically provides wireless connectivity and may include software for functions like secure
messaging, email, web, VPN (Virtual Private Network) connection, and VoIP (Voice over
IP), for access to the protected enterprise network, enterprise data and applications, and
for communicating to other Mobile Devices.


Mobile device management (MDM) products allow enterprises to apply security policies to
mobile devices. This system consists of two primary components: the MDM Server and the
MDM Agent.


The individual authorized to physically control and operate the Mobile Device. Depending
on the use case, this can be the device owner or an individual authorized by the device
owner.


A type or class of biometric system, such as fingerprint recognition, facial recognition, iris
recognition, voice recognition, signature/sign, and others.


These keys are stored as hardware-protected raw key and can be changed or sanitized.


Software that runs at the highest privilege level and can directly control hardware
resources. Modern Mobile Devices typically have at least two primary operating systems:
one, which runs on the application processor and one, which runs on the cellular baseband
processor. The OS of the application processor handles most user interactions and provides
the execution environment for apps. The OS of the cellular baseband processor handles
communications with the cellular network and may control other peripherals. The term OS,
without context, may be assumed to refer to the OS of the application processor.


A PIN is a set of numeric or alphabetic characters that may be used in addition to a
biometric factor to provide a hybrid authentication factor. At this time it is not considered
as a stand-alone authentication mechanism. A PIN is distinct from a password in that the
allowed character set and required length of a PIN is typically smaller than that of a
password as it is designed to be input quickly.


A type of authentication factor requiring the user to provide a secret set of characters to
gain access.


The device has been shut down such that no TOE function can be performed.


Protected data is all non-TSF data, including all user or enterprise data. Some or all of this
data may be considered sensitive data as well.


A key tied to the device used to encrypt other keys.



Sensitive data Sensitive data shall be identified in the TSS section of the Security Target (ST) by the ST
author. Sensitive data is a subset or all of the Protected data. Sensitive data may include
all user or enterprise data or may be specific application data such as emails, messaging,
documents, calendar items, and contacts. Sensitive data is protected while in the locked
state (FDP_DAR_EXT.2).


Software Keys The OS access the raw bytes of these keys during runtime.


TSF Data Data for the operation of the TSF upon which the enforcement of the requirements relies.



Trust Anchor
Database


Unenrolled
State



A list of trusted root Certificate Authority certificates.


The state in which the Mobile Device is not managed.


Unlocked
State


Verification
(Biometrics)



Powered on and device functionality is available for use. Implies user authentication has
occurred (when so configured).


A task where the biometric system attempts to confirm an individual’s claimed identity by
comparing a submitted sample to one or more previously enrolled authentication
templates.



**1.3 Scope of Document**


The scope of the Protection Profile within the development and evaluation process is described in the
Common Criteria for Information Technology Security Evaluation [CC]. In particular, a PP defines the IT
security requirements of a generic type of TOE and specifies the functional and assurance security measures
to be offered by that TOE to meet stated requirements [CC].


**1.4 Intended Readership**


The target audiences of this PP are Mobile Device developers, CC consumers, evaluators and schemes.


**1.5 TOE Overview**


This assurance standard specifies information security requirements for Mobile Devices for use in an
enterprise. A Mobile Device in the context of this assurance standard is a device, which is composed of a
hardware platform and its system software. The device typically provides wireless connectivity and may
include software for functions like secure messaging, email, web, VPN connection, and VoIP (Voice over IP),
for access to the protected enterprise network, enterprise data and applications, and for communicating to
other Mobile Devices.


Figure 1 illustrates the network operating environment of the Mobile Device.


**Figure 1: Mobile Device Network Environment**


Examples of a "Mobile Device" that should claim conformance to this Protection Profile include smartphones,
tablet computers, and other Mobile Devices with similar capabilities.


The Mobile Device provides essential services, such as cryptographic services, data-at-rest protection, and
key storage services to support the secure operation of applications on the device. Additional security
features such as security policy enforcement, application mandatory access control, anti-exploitation features,
user authentication, and software integrity protection are implemented in order to address threats.


This assurance standard describes these essential security services provided by the Mobile Device and serves
[as a foundation for a secure mobile architecture. The wireless connectivity shall be validated against the PP-](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=386&id=386)
Module for Wireless LAN Clients, version 1.0. If the mobile device contains Bluetooth functionality (i.e., has
[Bluetooth hardware), the Bluetooth connectivity shall be evaluated against the PP-Module for Bluetooth,](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=425&id=425)
version 1.0. As illustrated in Figure 2, it is expected that a typical deployment would also include either thirdparty or bundled components. Whether these components are bundled as part of the Mobile Device by the
manufacturer or developed by a third-party, they must be separately validated against the related assurance
standards such as the PP-Module for MDM Agent, PP-Module for VPN Client, PP-Module for VVoIP, and cPPModule for Biometrics. It is the responsibility of the architect of the overall secure mobile architecture to
ensure validation of these components. Additional applications that may come pre-installed on the Mobile
Device that are not validated are considered to be potentially flawed, but not malicious. Examples include
email client and web browser.


**Figure 2: Optional Additional Mobile Device Components**


**1.6 TOE Usage**


The Mobile Device may be operated in a number of use cases. use-case-appendix provides use case templates
that list those selections, assignments, and objective requirements that best support the use cases identified
by this Protection Profile. In addition to providing essential security services, the Mobile Device includes the
necessary security functionality to support configurations for these various use cases. Each use case may
require additional configuration and applications to achieve the desired security. A selection of these use
cases is elaborated below.


Several of the use case templates include objective requirements that are strongly desired for the indicated
use cases. Readers can expect those requirements to be made mandatory in a future revision of this
protection profile, and industry should aim to include that security functionality in products in the near-term.


As of publication of this version of the Protection Profile, meeting the requirements in Section 5 Security
Requirements is necessary for all use cases.


**[USE CASE 1] Enterprise-owned device for general-purpose enterprise use and limited personal**
**use**


An enterprise-owned device for general-purpose business use is commonly called _Corporately Owned,_
_Personally Enabled (COPE)_ . This use case entails a significant degree of enterprise control over
configuration and, possibly, software inventory. The enterprise elects to provide users with Mobile
Devices and additional applications (such as VPN or email clients) in order to maintain control of their
Enterprise data and security of their networks. Users may use Internet connectivity to browse the web or
access corporate mail or run enterprise applications, but this connectivity may be under significant
control of the enterprise.
For changes to included SFRs, selections, and assignments required for this use case, see H.1
Enterprise-owned device for general-purpose enterprise use and limited personal use.


**[USE CASE 2] Enterprise-owned device for specialized, high security use**


An enterprise-owned device with intentionally limited network connectivity, tightly controlled
configuration, and limited software inventory is appropriate for specialized, high-security use cases. For
example, the device may not be permitted connectivity to any external peripherals. It may only be able to
communicate via its Wi-Fi or cellular radios with the enterprise-run network, which may not even permit
connectivity to the Internet. Use of the device may entail compliance with policies that are more
restrictive than those in any general-purpose use case, yet may mitigate risks to highly sensitive
information. As in the previous case, the enterprise will look for additional applications providing
enterprise connectivity and services to have a similar level of assurance as the platform.
For changes to included SFRs, selections, and assignments required for this use case, see H.2
Enterprise-owned device for specialized, high security use.


**[USE CASE 3] Personally-owned device for personal and enterprise use**


A personally-owned device that is used for both personal activities and enterprise data is commonly
called Bring Your Own Device (BYOD). The device may be provisioned for access to enterprise resources
after significant personal usage has occurred. Unlike in the enterprise-owned cases, the enterprise is
limited in what security policies it can enforce because the user purchased the device primarily for
personal use and is unlikely to accept policies that limit the functionality of the device. However, because
the enterprise allows the user full (or nearly full) access to the enterprise network, the enterprise will
require their own security controls to ensure that enterprise resources are protected from potential
threats posed by the personal activities on the device. These controls could potentially be enforced by a
separation mechanism built-in to the device itself to distinguish between enterprise and personal
activities, or by a third-party application that provides access to enterprise resources and leverages
security capabilities provided by the mobile device. Based upon the operational environment and the
acceptable risk level of the enterprise, those security functional requirements outlined in Section 5
Security Requirements of this PP along with the selections in the Use Case 3 template defined in
Appendix F - Use Case Templates are sufficient for the secure implementation of this BYOD use case.
For changes to included SFRs, selections, and assignments required for this use case, see H.3


Personally-owned device for personal and enterprise use.


**[USE CASE 4] Personally-owned device for personal and limited enterprise use**


A personally-owned device that is used for both personal activities and enterprise data is commonly
called Bring Your Own Device (BYOD). This device may be provisioned for limited access to enterprise
resources such as enterprise email. Because the user does not have full access to the enterprise or
enterprise data, the enterprise may not need to enforce any security policies on the device. However, the
enterprise may want secure email and web browsing with assurance that the services being provided to
those clients by the Mobile Device are not compromised. Based upon the operational environment and
the acceptable risk level of the enterprise, those security functional requirements outlined in Section 5
Security Requirements of this PP are sufficient for the secure implementation of this BYOD use case.


# **2 Conformance Claims**

**Conformance Statement**

An ST must claim exact conformance to this PP, as defined in the CC and CEM addenda for Exact
Conformance, Selection-based SFRs, and Optional SFRs (dated May 2017).


The following PP-Modules are allowed to be specified in a PP-Configuration with this PP.


[PP-Module for Virtual Private Network (VPN) Clients, version 2.4](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=467&id=467)
[PP-Module for Bluetooth, version 1.0](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=425&id=425)
[PP-Module for Mobile Device Management Agent, version 1.0](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=441&id=441)
[PP-Module for Wireless LAN Clients, version 1.0](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=386&id=386)
[Biometric Enrollment and Verification, version 1.1](https://github.com/biometricITC/cPP-biometrics/blob/master/Protection%20Profile/BiocPP.adoc)


**CC Conformance Claims**

This PP is conformant to Parts 2 (extended) and 3 (conformant) of Common Criteria Version 3.1, Revision
5.


**PP Claim**

This PP does not claim conformance to any Protection Profile.


**Package Claim**

This PP is Functional Package for Transport Layer Security (TLS), version 1.1 Conformant.


# **3 Security Problem Description**

**3.1 Threats**


Mobile devices are subject to the threats of traditional computer systems along with those entailed by their
mobile nature. The threats considered in this PP are those of network eavesdropping, network attacks,
physical access, malicious or flawed applications, persistent presence, and backup as detailed in the following
sections.


**T.NETWORK_EAVESDROP**

An attacker is positioned on a wireless communications channel or elsewhere on the network
infrastructure. Attackers may monitor and gain access to data exchanged between the Mobile Device and
other endpoints.


**T.NETWORK_ATTACK**

An attacker is positioned on a wireless communications channel or elsewhere on the network
infrastructure. Attackers may initiate communications with the Mobile Device or alter communications
between the Mobile Device and other endpoints in order to compromise the Mobile Device. These attacks
include malicious software update of any applications or system software on the device. These attacks
also include malicious web pages or email attachments, which are usually delivered to devices over the
network.


**T.PHYSICAL_ACCESS**

An attacker, with physical access, may attempt to access user data on the Mobile Device including
credentials. These physical access threats may involve attacks, which attempt to access the device
through external hardware ports, impersonate the user authentication mechanisms, through its user
interface, and also through direct and possibly destructive access to its storage media. Note: Defending
against device re-use after physical compromise is out of scope for this Protection Profile.


**T.MALICIOUS_APP**

Applications loaded onto the Mobile Device may include malicious or exploitable code. This code could
be included intentionally or unknowingly by the developer, perhaps as part of a software library.
Malicious apps may attempt to exfiltrate data to which they have access. They may also conduct attacks
against the platform’s system software, which will provide them with additional privileges and the ability
to conduct further malicious activities. Malicious applications may be able to control the device's sensors
(GPS, camera, microphone) to gather intelligence about the user's surroundings even when those
activities do not involve data resident or transmitted from the device. Flawed applications may give an
attacker access to perform network-based or physical attacks that otherwise would have been prevented


**T.PERSISTENT_PRESENCE**

Persistent presence on a device by an attacker implies that the device has lost integrity and cannot
regain it. The device has likely lost this integrity due to some other threat vector, yet the continued
access by an attacker constitutes an on-going threat in itself. In this case, the device and its data may be
controlled by an adversary as well as by its legitimate owner.


**3.2 Assumptions**


The specific conditions listed below are assumed to exist in the TOE’s Operational Environment. These
include both practical realities in the development of the TOE security requirements and the essential
environmental conditions on the use of the TOE.


**A.CONFIG**

It is assumed that the TOE’s security functions are configured correctly in a manner to ensure that the
TOE security policies will be enforced on all applicable network traffic flowing among the attached
networks.


**A.NOTIFY**

It is assumed that the mobile user will immediately notify the administrator if the Mobile Device is lost or
stolen.


**A.PRECAUTION**

It is assumed that the mobile user exercises precautions to reduce the risk of loss or theft of the Mobile
Device.


**A.PROPER_USER**

Mobile Device users are not willfully negligent or hostile, and use the device within compliance of a
reasonable Enterprise security policy.


**3.3 Organizational Security Policies**


This document does not define any additional OSPs.


# **4 Security Objectives**

**4.1 Security Objectives for the TOE**


**O.PROTECTED_COMMS**

To address the network eavesdropping (T.NETWORK_EAVESDROP) and network attack
(T.NETWORK_ATTACK) threats described in Section 3.1 Threats, concerning wireless transmission of
Enterprise and user data and configuration data between the TOE and remote network entities,
conformant TOEs will use a trusted communication path. The TOE must be capable of communicating
using mutually authenticated TLS, EAP-TLS, HTTPS, 802.1X, and 802.11-2012. The TOE may optionally
communicate using these standard protocols: IPsec, mutually-authenticated DTLS, or Bluetooth. These
protocols are specified by RFCs that offer a variety of implementation choices. Requirements have been
imposed on some of these choices (particularly those for cryptographic primitives) to provide
interoperability and resistance to cryptographic attack.


While conformant TOEs must support all of the choices specified in the ST including any optional SFRs
defined in this PP, they may support additional algorithms and protocols. If such additional mechanisms
are not evaluated, guidance must be given to the administrator to make clear the fact that they were not
evaluated.


**O.STORAGE**

To address the issue of loss of confidentiality of user data in the event of loss of a Mobile Device
(T.PHYSICAL_ACCESS), conformant TOEs will use data-at-rest protection. The TOE will be capable of
encrypting data and keys stored on the device and will prevent unauthorized access to encrypted data.


**O.CONFIG**

To ensure a Mobile Device protects user and enterprise data that it may store or process, conformant
TOEs will provide the capability to configure and apply security policies defined by the user and the
Enterprise Administrator. If Enterprise security policies are configured these must be applied in
precedence of user specified security policies.


**O.AUTH**

To address the issue of loss of confidentiality of user data in the event of loss of a Mobile Device
(T.PHYSICAL_ACCESS), users are required to enter an authentication factor to the device prior to
accessing protected functionality and data. Some non-sensitive functionality (e.g., emergency calling,
text notification) can be accessed prior to entering the authentication factor. The device will
automatically lock following a configured period of inactivity in an attempt to ensure authorization will
be required in the event of the device being lost or stolen.


Authentication of the endpoints of a trusted communication path is required for network access to
ensure attacks are unable to establish unauthorized network connections to undermine the integrity of
the device.


Repeated attempts by a user to authorize to the TSF will be limited or throttled to enforce a delay
between unsuccessful attempts.


**O.INTEGRITY**

To ensure the integrity of the Mobile Device is maintained conformant TOEs will perform self-tests to
ensure the integrity of critical functionality, software/firmware and data has been maintained. The user
shall be notified of any failure of these self-tests. This will protect against the threat T.PERSISTENT.


To address the issue of an application containing malicious or flawed code (T.MALICIOUS_APP), the
integrity of downloaded updates to software/firmware will be verified prior to installation/execution of
the object on the Mobile Device. In addition, the TOE will restrict applications to only have access to the
system services and data they are permitted to interact with. The TOE will further protect against
malicious applications from gaining access to data they are not authorized to access by randomizing the
memory layout.


**O.PRIVACY**

In a BYOD environment (use cases 3 and 4), a personally-owned mobile device is used for both personal
activities and enterprise data. Enterprise management solutions may have the technical capability to
monitor and enforce security policies on the device. However, the privacy of the personal activities and
data must be ensured. In addition, since there are limited controls that the enterprise can enforce on the
personal side, separation of personal and enterprise data is needed. This will protect against the
T.MALICIOUS_APP and T.PERSISTENT_PRESENCE threats.


**4.2 Security Objectives for the Operational Environment**


The following security objectives for the operational environment assist the OS in correctly providing its
security functionality. These track with the assumptions about the environment.


**OE.CONFIG**

TOE administrators will configure the Mobile Device security functions correctly to create the intended
security policy


**OE.NOTIFY**

The Mobile User will immediately notify the administrator if the Mobile Device is lost or stolen.


**OE.PRECAUTION**

The mobile device user exercises precautions to reduce the risk of loss or theft of the Mobile Device.


**OE.DATA_PROPER_USER**

Administrators take measures to ensure that mobile device users are adequately vetted against malicious
intent and are made aware of the expectations for appropriate use of the device.


**4.3 Security Objectives Rationale**


This section describes how the assumptions, threats, and organizational security policies map to the security
objectives.


**Table 1: Security Objectives Rationale**



**Threat,**
**Assumption,**
**or OSP**



**Security**
**Objectives**





**Rationale**













|T.NETWORK_​<br>EAVESDROP|O.PROTECTED_​ The threat T.NETWORK_EAVESDROP is countered by<br>COMMS O.PROTECTED_COMMS as this provides the capability to<br>communicate using one (or more) standard protocols as a means to<br>maintain the confidentiality of data that are transmitted outside of<br>the TOE.<br>O.CONFIG The threat T.NETWORK_EAVESDROP is countered by O.CONFIG as<br>this provides a secure configuration of the mobile device to protect<br>data that it processes.<br>O.AUTH The threat T.NETWORK_EAVESDROP is countered by O.AUTH as<br>this provides authentication of the endpoints of a trusted<br>communication path.|
|---|---|
|T.NETWORK_<br>ATTACK|O.PROTECTED_<br>COMMS<br>The threatT.NETWORK_ATTACK is countered by<br>O.PROTECTED_COMMS as this provides the capability to<br>communicate using one (or more) standard protocols as a means to<br>maintain the confidentiality of data that are transmitted outside of<br>the TOE.<br>O.CONFIG<br>The threatT.NETWORK_ATTACK is countered byO.CONFIG as this<br>provides a secure configuration of the mobile device to protect data<br>that it processes.<br>O.AUTH<br>The threatT.NETWORK_ATTACK is countered byO.AUTH as this<br>provides authentication of the endpoints of a trusted communication<br>path.|
|T.PHYSICAL_<br>ACCESS|O.STORAGE<br>The threatT.PHYSICAL_ACCESS is countered byO.STORAGE as this<br>provides the capability to encrypt all user and enterprise data and<br>authentication keys to ensure the confidentiality of data that it stores.<br>O.AUTH<br>The threatT.PHYSICAL_ACCESS is countered byO.AUTH as this<br>provides the capability to authenticate the user prior to accessing<br>protected functionality and data.|
|T.MALICIOUS_<br>APP|O.PROTECTED_<br>COMMS<br>The threatT.MALICIOUS_APP is countered by<br>O.PROTECTED_COMMS as this provides the capability to<br>communicate using one (or more) standard protocols as a means to<br>maintain the confidentiality of data that are transmitted outside of<br>the TOE.<br>O.CONFIG<br>The threatT.MALICIOUS_APP is countered byO.CONFIG as this<br>provides the capability to configure and apply security policies to<br>ensure the Mobile Device can protect user and enterprise data that it<br>may store or process.<br>O.AUTH<br>The threatT.MALICIOUS_APP is countered byO.AUTH as this<br>provides the capability to authenticate the user and endpoints of a<br>trusted path to ensure they are communicating with an authorized<br>entity with appropriate privileges.<br>O.INTEGRITY<br>The threatT.MALICIOUS_APP is countered byO.INTEGRITY as this<br>provides the capability to perform self-tests to ensure the integrity of<br>critical functionality, software/firmware and data has been<br>maintained.<br>O.PRIVACY<br>The threatT.MALICIOUS_APP is countered byO.PRIVACY as this<br>provides separation and privacy between user activities.|


T.PERSISTENT_​ O.INTEGRITY The threat T.PERSISTENT_PRESENCE is countered by O.INTEGRITY


|PRESENCE|as this provides the capability to perform self-tests to ensure the<br>integrity of critical functionality, software/firmware and data has<br>been maintained.<br>O.PRIVACY The threat T.PERSISTENT_PRESENCE is countered by O.PRIVACY<br>as this provides separation and privacy between user activities.|
|---|---|
|A.CONFIG|OE.CONFIG<br>The operational environment objectiveOE.CONFIG is realized<br>throughA.CONFIG.|
|A.NOTIFY|OE.NOTIFY<br>The operational environment objectiveOE.NOTIFY is realized<br>throughA.NOTIFY.|
|A.PRECAUTION|OE.PRECAUTION<br>The operational environment objectiveOE.PRECAUTION is realized<br>throughA.PRECAUTION.|
|A.PROPER_<br>USER|OE.DATA_<br>PROPER_USER<br>The operational environment objectiveOE.DATA_PROPER_USER is<br>realized throughA.PROPER_USER.|


# **5 Security Requirements**

This chapter describes the security requirements which have to be fulfilled by the product under evaluation.
Those requirements comprise functional components from Part 2 and assurance components from Part 3 of

[CC]. The following conventions are used for the completion of operations:

**Refinement** operation (denoted by **bold text** or ~~strikethrough text)~~ : Is used to add details to a
requirement (including replacing an assignment with a more restrictive selection) or to remove part of
the requirement that is made irrelevant through the completion of another operation, and thus further
restricts a requirement.
**Selection** (denoted by _italicized text_ ): Is used to select one or more options provided by the [CC] in
stating a requirement.
**Assignment** operation (denoted by _italicized text_ ): Is used to assign a specific value to an unspecified
parameter, such as the length of a password. Showing the value in square brackets indicates assignment.
**Iteration** operation: Is indicated by appending the SFR name with a slash and unique identifier
suggesting the purpose of the operation, e.g. "/EXAMPLE1."


**5.1 Security Functional Requirements**


**5.1.1 Auditable Events for Mandatory SFRs**


**Table 2: Auditable Events for Mandatory Requirements**


























|Requirement|Auditable Events|Additional Audit Record Contents|
|---|---|---|
|FAU_GEN.1|No events specified|N/A|
|FAU_SAR.1|No events specified|N/A|
|FAU_STG.1|No events specified|N/A|
|FAU_STG.4|No events specified|N/A|
|FCS_CKM.1|[**selection, choose one of**:<br>_Failure of key generation activity_<br>_for authentication keys_, _None_ ]|No additional information|
|FCS_CKM.2/UNLOCKED|No events specified|N/A|
|FCS_CKM.2/LOCKED|No events specified|N/A|
|FCS_CKM_EXT.1|[**selection, choose one of**:<br>_generation of a REK_, _none_ ]|No additional information|
|FCS_CKM_EXT.2|No events specified|N/A|
|FCS_CKM_EXT.3|No events specified|N/A|
|FCS_CKM_EXT.4|No events specified|N/A|
|FCS_CKM_EXT.5|[**selection, choose one of**:<br>_Failure of the wipe_, _none_ ]|No additional information|
|FCS_CKM_EXT.6|No events specified|N/A|
|FCS_COP.1/ENCRYPT|No events specified|N/A|
|FCS_COP.1/HASH|No events specified|N/A|
|FCS_COP.1/SIGN|No events specified|N/A|
|FCS_COP.1/KEYHMAC|No events specified|N/A|
|FCS_COP.1/CONDITION|No events specified|N/A|
|FCS_IV_EXT.1|No events specified|N/A|
|FCS_SRV_EXT.1|No events specified|N/A|
|FCS_STG_EXT.1|Import or destruction of key|Identity of key, role and identity of<br>requester|
|FCS_STG_EXT.1|[**selection, choose one of**:<br>_Exceptions to use and destruction_<br>_rules_, _none_ ]|Identity of key, role and identity of<br>requester|
|FCS_STG_EXT.2|No events specified|N/A|
|FCS_STG_EXT.2|||


|FCS_STG_EXT.3|Col2|Col3|
|---|---|---|
|FCS_STG_EXT.3|Failure to verify integrity of<br>stored key|Identity of key being verified|
|FDP_ACF_EXT.1|No events specified|N/A|
|FDP_DAR_EXT.1|[**selection, choose one of**:<br>_Failure to encrypt/decrypt data_,<br>_none_ ]|No additional information|
|FDP_DAR_EXT.2|[**selection, choose one of**:<br>_Failure to encrypt/decrypt data_,<br>_none_ ]|No additional information|
|FDP_IFC_EXT.1|No events specified|N/A|
|FDP_STG_EXT.1|Addition or removal of certificate<br>from Trust Anchor Database|Subject name of certificate.|
|FIA_PMG_EXT.1|No events specified|N/A|
|FIA_TRT_EXT.1|No events specified|N/A|
|FIA_UAU.5|No events specified|N/A|
|FIA_UAU.7|No events specified|N/A|
|FIA_UAU_EXT.1|No events specified|N/A|
|FIA_X509_EXT.1|Failure to validate X.509v3<br>certificate|Reason for failure of validation|
|FIA_X509_EXT.2|No events specified|N/A|
|FMT_MOF_EXT.1|No events specified|N/A|
|FPT_AEX_EXT.1|No events specified|N/A|
|FPT_AEX_EXT.2|No events specified|N/A|
|FPT_AEX_EXT.3|No events specified|N/A|
|FPT_JTA_EXT.1|No events specified|N/A|
|FPT_KST_EXT.1|No events specified|N/A|
|FPT_KST_EXT.2|No events specified|N/A|
|FPT_KST_EXT.3|No events specified|N/A|
|FPT_NOT_EXT.1|[**selection, choose one of**:<br>_Measurement of TSF software_,<br>_none_ ]|[**selection, choose one of**: _Integrity_<br>_verification value_, _No additional_<br>_information_ ]|
|FPT_STM.1|No events specified|N/A|
|FPT_TST_EXT.1|Initiation of self-test|No additional information|
|FPT_TST_EXT.1|Failure of self-test|[**selection, choose one of**: _Algorithm_<br>_that caused the failure_, _No additional_<br>_information_ ]|
|FPT_TST_EXT.2/PREKERNEL|Start-up of TOE|No additional information|
|FPT_TST_EXT.2/PREKERNEL|[**selection, choose one of**:<br>_Detected integrity violation_, _none_<br>]|[**selection, choose one of**: _The TSF_<br>_code file that caused the integrity_<br>_violation_, _No additional information_ ]|
|FPT_TUD_EXT.1|No events specified|N/A|
|FTA_SSL_EXT.1|No events specified|N/A|
|FTA_TAB.1|No events specified|N/A|


**Table 3: Additional Audit Events**


**Requirement** **Auditable Events** **Additional Audit Record Contents**



FAU_SEL.1 All modifications to the
audit configuration that
occur while the audit
collection functions are



No additional information


|Col1|operating|Col3|
|---|---|---|
|FCS_CKM_EXT.7|No events specified|N/A|
||||
|FCS_HTTPS_EXT.1|Failure of the certificate<br>validity check.|Issuer Name and Subject Name of<br>certificate<br>[**selection, choose one of**: _User’s_<br>_authorization decision_, _No additional_<br>_information_ ]|
|FCS_RBG_EXT.1|Failure of the<br>randomization process|No additional information|
|FCS_RBG_EXT.2|No events specified|N/A|
|FCS_RBG_EXT.3|No events specified|N/A|
|FCS_SRV_EXT.2|No events specified|N/A|
|FDP_ACF_EXT.1|No events specified|N/A|
|FDP_ACF_EXT.2|No events specified|N/A|
|FDP_ACF_EXT.3|No events specified|N/A|
|FDP_BCK_EXT.1|No events specified|N/A|
|FDP_UPC_EXT.1/APPS|Application initiation of<br>trusted channel|Name of application. Trusted channel<br>protocol. Non-TOE endpoint of connection|
|FDP_UPC_EXT.1/BLUETOOTH|Application initiation of<br>trusted channel|Name of application. Trusted channel<br>protocol. Non-TOE endpoint of connection|
|FIA_AFL_EXT.1|Excess of authentication<br>failure limit|Authentication factor used|
|FIA_UAU.6/LOCKED|User changes Password<br>Authentication Factor|No additional information|
|FIA_UAU_EXT.2|Action performed before<br>authentication.|No additional information|
|FIA_UAU_EXT.4|No events specified|N/A|
|FIA_X509_EXT.2|Failure to establish<br>connection to determine<br>revocation status|No additional information|
|FIA_X509_EXT.3|No events specified|N/A|
|FIA_X509_EXT.4|Generation of Certificate<br>Enrollment Request|Issuer and Subject name of EST Server.<br>Method of authentication. Issuer and Subject<br>name of certificate used to authenticate.<br>Content of Certificate Request Message|
|FIA_X509_EXT.4|Success or failure of<br>enrollment|Issuer and Subject name of added certificate<br>or reason for failure|
|FIA_X509_EXT.4|Update of EST Trust<br>Anchor Database|Subject name of added Root CA|
|FIA_X509_EXT.5|No events specified|N/A|
|FMT_SMF.1|Initiation of policy update|Policy name|
|FMT_SMF.1|Change of settings|Role of user that changed setting, Value of<br>new setting|
|FMT_SMF.1|Success or failure of<br>function|Role of user that performed function,<br>Function performed, Reason for failure|
|FMT_SMF.1|Initiation of software<br>update|Version of update|
|FMT_SMF.1|Initiation of application<br>installation or update|Name and version of application|
|FMT_SMF_EXT.2|Unenrollment, Initiation of<br>unenrollment|Identity of administrator Remediation action<br>performed, failure of accepting command to<br>unenroll|


|FMT_SMF_EXT.3|No events specified|N/A|
|---|---|---|
|FPT_AEX_EXT.4|No events specified|N/A|
|FPT_AEX_EXT.5|No events specified|N/A|
|FPT_AEX_EXT.6|No events specified|N/A|
|FPT_AEX_EXT.7|No events specified|N/A|
|FPT_BBD_EXT.1|No events specified|N/A|
|FPT_BLT_EXT.1|No events specified|N/A|
|FPT_NOT_EXT.2|No events specified|N/A|
|FPT_TST_EXT.2/POSTKERNEL|[**selection, choose one**<br>**of**: _Detected integrity_<br>_violation_, _None_ ]|[**selection, choose one of**: _The TSF code_<br>_file that cause the integrity violation_, _No_<br>_additional information_ ]|
|FPT_TST_EXT.3|No events specified|N/A|
|FPT_TUD_EXT.2|Success or failure of<br>signature verification for<br>software updates|No additional information|
|FPT_TUD_EXT.3|Success or failure of<br>signature verification for<br>applications|No additional information|
|FPT_TUD_EXT.4|No events specified|N/A|
|FPT_TUD_EXT.5|No events specified|N/A|
|FPT_TUD_EXT.6|No events specified|N/A|
|FTP_ITC_EXT.1|Initiation and termination<br>of trusted channel|Trusted channel protocol, non-TOE endpoint<br>of connection|


**5.1.2 Class: Security Audit (FAU)**


**FAU_GEN.1 Audit Data Generation**


FAU_GEN.1.1





The TSF shall be able to generate an audit record of the following auditable
events:


1. Start-up and shutdown of the audit functions
2. All auditable events for the [ **not selected** ] level of audit
3. **All administrative actions**
4. **Start-up and shutdown of the OS**
5. **Insertion or removal of removable media**
6. **Specifically defined auditable events in Table 2**
7. **[selection:** _**Audit records reaching [assignment: integer value less**_

_**than 100] percentage of audit capacity**_ **,** _**Specifically defined**_
_**auditable events in Table 3**_ **,** _**[assignment: other auditable events**_
_**derived from this Protection Profile]**_ **,** _**no additional auditable events**_
**]**


**Application Note:** Administrator actions are defined as functions labeled as
mandatory for FMT_MOF_EXT.1.2 (i.e. ‘M-MM’ in Table 7). If the TSF does not
support removable media, number 4 is implicitly met.


The TSF must generate an audit record for all events contained in Table 2.
Generating audit records for events in Table 3 is currently objective. It is
acceptable to include individual SFRs from Table 3 in the ST, without including
the entirety of Table 3.


**Table 2 Application Note:**
FPT_TST_EXT.1 – Audit of self-tests is required only at initial start-up. Since the
TOE "transitions to non-operational mode" upon failure of a self-test, per
FPT_NOT_EXT.1, this is considered equivalent evidence to an audit record for
the failure of a self-test.


FDP_DAR_EXT.1 - "None" must be selected, if the TOE utilizes whole volume
encryption for protected data, since it is not feasible to audit when the


FAU_GEN.1.2



encryption/decryption fails. If the TOE utilizes file-based encryption for
protected data and audits when this encryption/decryption fails, then that
auditable event should be selected.


**Table 3 Application Note:**
If the audit event for FMT_SMF.1 is included in the ST, it is acceptable for the
initiation of the software update to be audited without indicating the outcome
(success or failure) of the update.


Validation Guidelines:


**Rule #1**


The TSF shall record within each audit record at least the following information:


1. Date and time of the event
2. Type of event
3. Subject identity
4. The outcome (success or failure) of the event
**5. Additional information in Table 2**
**6. [selection:** _**Additional information in Table 3**_ **,** _**no additional**_

_**information**_ **]**


**Application Note:** The subject identity is usually the process name/ID. The
event type is often indicated by a severity level, for example, ‘info’, ‘warning’, or
‘error’.


If no additional auditable events is selected in the second selection of
FAU_GEN.1.1, then no additional information must be selected.


For each audit event selected from Table 3 in FAU_GEN.1.1 if additional
information is required to be recorded within the audit record, it should be
included in this selection.


Validation Guidelines:


**Rule #1**



**Evaluation Activities**


**FAU_SAR.1 Audit Review**


FAU_SAR.1.1

The TSF shall provide [ _the administrator_ ] with the capability to read [ _all audited_
_events and record contents_ ] from the audit records.


**Application Note:** The administrator must have access to read the audit record,
perhaps through an API or via an MDM Agent, which transfers the local records
stored on the TOE to the MDM Server where the enterprise administrator may
view them. If this requirement is included in the ST, function 32 must be
included in the selection of FMT_SMF.1.


FAU_SAR.1.2

The TSF shall provide the audit records in a manner suitable for the user to
interpret the information.


**Evaluation Activities**





**FAU_STG.1 Audit Storage Protection**


FAU_STG.1.1

The TSF shall protect the stored audit records in the audit trail from
unauthorized deletion.


FAU_STG.1.2

The TSF shall be able to [ _prevent_ ] unauthorized modifications to the stored audit
records in the audit trail.


**Evaluation Activities**





**FAU_STG.4 Prevention of Audit Data Loss**


FAU_STG.4.1

The TSF shall [ _overwrite the oldest stored audit records_ ] ~~and [assignment: other~~
~~actions to be taken in case of audit storage failure]~~ if the audit trail is full.


**Evaluation Activities**


_FAU_STG.4_
_**TSS**_
_The evaluator shall examine the TSS to ensure that it describes the size limits on the audit_
_records, the detection of a full audit trail, and the actions taken by the TSF when the audit trail_
_is full. The evaluator shall ensure that the actions results in the deletion or overwrite of the_
_oldest stored record._


_**Guidance**_
_There are no guidance evaluation activities for this component._


_**Tests**_
_There are no test evaluation activities for this component._


**5.1.3 Class: Cryptographic Support (FCS)**
This section describes how keys are generated, derived, combined, released and destroyed. There are two
major types of keys: DEKs and KEKs. (A REK is considered a KEK.) DEKs are used to protect data (as in the
DAR protection described in FDP_DAR_EXT.1 and FDP_DAR_EXT.2). KEKs are used to protect other keys –
DEKs, other KEKs, and other types of keys stored by the user or applications. The following diagram shows an
example key hierarchy to illustrate the concepts of this profile. This example is not meant as an approved
design, but ST authors will be expected to provide a diagram illustrating their key hierarchy in order to
demonstrate that they meet the requirements of this profile. Please note if biometric in accordance with the
[Biometric Enrollment and Verification, version 1.1 is selected in FIA_UAU.5.1, each BAF claimed in](https://github.com/biometricITC/cPP-biometrics/blob/master/Protection%20Profile/BiocPP.adoc)
[FIA_MBV_EXT.1.1 in the Biometric Enrollment and Verification, version 1.1 shall be illustrated in the key](https://github.com/biometricITC/cPP-biometrics/blob/master/Protection%20Profile/BiocPP.adoc)
hierarchy diagram, to include a description of when and how the BAF is used to release keys. If hybrid is
selected in FIA_UAU.5.1, meaning that a PIN or password must be used in conjunction with the BAF, this
interaction shall be included.


**Figure 3: An Illustrative Key Hierarchy**


**FCS_CKM.1 Cryptographic Key Generation**


FCS_CKM.1.1

The TSF shall generate **asymmetric** cryptographic keys in accordance with a
specified cryptographic key generation algorithm [ **selection** :

_**RSA schemes using**_ _cryptographic key sizes of [_ _**assignment**_ _: 2048-bit or_
_greater] that meet [FIPS PUB 186-4, "Digital Signature Standard (DSS)",_
_Appendix B.3]_
_**ECC schemes using:**_ _[_ _**selection**_ _:_

_**"NIST curves" P-384 and**_ _[_ _**selection**_ _:_ _**P-256**_ _,_ _**P-521**_ _,_ _**no other**_
_**curves**_ _] that meet the following: [FIPS PUB 186-4, "Digital Signature_
_Standard (DSS)", Appendix B.4]_
_**Curve25519 schemes**_ _that meet the following: [RFC 7748]_

_]_
_**FFC schemes using:**_ _[_ _**selection**_ _:_

_cryptographic key sizes of_ _**2048-bit or greater**_ _that meet the_
_following [FIPS PUB 186-4, "Digital Signature Standard (DSS)",_
_Appendix B.1]_
_**"safe-prime" groups**_ _that meet the following: [NIST Special_
_Publication 800-56A Revision 3, "Recommendation for Pair-Wise Key_
_Establishment Schemes Using Discrete Logarithm Cryptography"]_

_]_

].


**Application Note:** The ST author must select all key generation schemes used
for key establishment and entity authentication. When key generation is used for


key establishment, the schemes in FCS_CKM.2/UNLOCKED and selected
cryptographic protocols must match the selection. When key generation is used
for entity authentication, the public key may be associated with an X.509v3
certificate.


If the TOE acts as a receiver in the RSA key establishment scheme, the TOE does
not need to implement RSA key generation.


Curve25519 can only be used for ECDH and in conjunction with
FDP_DAR_EXT.2.2.


**Evaluation Activities**


_FCS_CKM.1_
_**TSS**_
_The evaluator shall ensure that the TSS identifies the key sizes supported by the TOE. If the ST_
_specifies more than one scheme, the evaluator shall examine the TSS to verify that it identifies_
_the usage for each scheme._


_**Guidance**_
_The evaluator shall verify that the AGD guidance instructs the administrator how to configure_
_the TOE to use the selected key generation schemes and key sizes for all uses defined in this PP._


_**Tests**_
_**Evaluation Activity Note:**_ _The following tests require the developer to provide access to a test_
_platform that provides the evaluator with tools that are typically not found on factory products._


_**Key Generation for FIPS PUB 186-4 RSA Schemes**_


_The evaluator shall verify the implementation of RSA Key Generation by the TOE using the Key_
_Generation test. This test verifies the ability of the TSF to correctly produce values for the key_
_components including the public verification exponent e, the private prime factors p and q, the_
_public modulus n and the calculation of the private signature exponent d._


_Key Pair generation specifies 5 ways (or methods) to generate the primes p and q. These_
_include:_


_1. Random Primes:_

_Provable primes_
_Probable primes_

_2. Primes with Conditions:_

_Primes p1, p2, q1,q2, p and q shall all be provable primes_
_Primes p1, p2, q1, and q2 shall be provable primes and p and q shall be probable_
_primes_
_Primes p1, p2, q1,q2, p and q shall all be probable primes_


_To test the key generation method for the Random Provable primes method and for all the_
_Primes with Conditions methods, the evaluator must seed the TSF key generation routine with_
_sufficient data to deterministically generate the RSA key pair. This includes the random seeds,_
_the public exponent of the RSA key, and the desired key length. For each key length supported,_
_the evaluator shall have the TSF generate 25 key pairs. The evaluator shall verify the_
_correctness of the TSF’s implementation by comparing values generated by the TSF with those_
_generated from a known good implementation._


_If possible, the Random Probable primes method should also be verified against a known good_
_implementation as described above. Otherwise, the evaluator shall have the TSF generate 10_
_keys pairs for each supported key length nlen and verify:_

_n = p*q_
_p and q are probably prime according to Miller-Rabin tests_
_GCD(p-1,e) = 1_
_GCD(q-1,e) = 1_
_2^16 < e < 2^256 and e is an odd integer_
_|p-q| > 2^(nlen/2 – 100)_
_p >= squareroot(2)*( 2^(nlen/2 -1) )_
_q >= squareroot(2)*( 2^(nlen/2 -1) )_
_2^(nlen/2) < d < LCM(p-1,q-1)_
_e*d = 1 mod LCM(p-1,q-1)_


_**Key Generation for FIPS 186-4 Elliptic Curve Cryptography (ECC)**_
_FIPS 186-4 ECC Key Generation Test_


_For each supported NIST curve, i.e. P-256, P-384 and P-521, the evaluator shall require the_
_implementation under test (IUT) to generate 10 private/public key pairs. The private key shall be_
_generated using an approved random bit generator (RBG). To determine correctness, the_


_evaluator shall submit the generated key pairs to the public key verification (PKV) function of a_
_known good implementation._


_FIPS 186-4 Public Key Verification (PKV) Test_


_For each supported NIST curve, i.e. P-256, P-384 and P-521, the evaluator shall generate 10_
_private/public key pairs using the key generation function of a known good implementation and_
_modify five of the public key values so that they are incorrect, leaving five values unchanged (i.e._
_correct). The evaluator shall obtain in response a set of 10 PASS/FAIL values._


_**Key Generation for Curve25519**_
_The evaluator shall require the implementation under test (IUT) to generate 10 private/public_
_key pairs. The private key shall be generated as specified in RFC 7748 using an approved_
_random bit generator (RBG) and shall be written in little-endian order (least significant byte_
_first). To determine correctness, the evaluator shall submit the generated key pairs to the public_
_key verification (PKV) function of a known good implementation._


_Note: Assuming the PKV function of the good implementation will (using little-endian order):_


_a. Confirm the private and public keys are 32-byte values_
_b. Confirm the three least significant bits of the first byte of the private key are zero_

_c. Confirm the most significant bit of the last byte is zero_
_d. Confirm the second most significant bit of the last byte is one_
_e. Calculate the expected public key from the private key and confirm it matches the supplied_

_public key_


_The evaluator shall generate 10 private/public key pairs using the key generation function of a_
_known good implementation and modify 5 of the public key values so that they are incorrect,_
_leaving five values unchanged (i.e. correct). The evaluator shall obtain in response a set of 10_
_PASS/FAIL values._


_**Key Generation for Finite-Field Cryptography (FFC)**_
_The evaluator shall verify the implementation of the Parameters Generation and the Key_
_Generation for FFC by the TOE using the Parameter Generation and Key Generation test. This_
_test verifies the ability of the TSF to correctly produce values for the field prime p, the_
_cryptographic prime q (dividing p-1), the cryptographic group generator g, and the calculation of_
_the private key x and public key y._
_The Parameter generation specifies 2 ways (or methods) to generate the cryptographic prime q_
_and the field prime p:_


_Cryptographic and Field Primes:_


_Primes q and p shall both be provable primes_
_Primes q and field prime p shall both be probable primes_

_and two ways to generate the cryptographic group generator g:_


_Cryptographic Group Generator:_


_Generator g constructed through a verifiable process_
_Generator g constructed through an unverifiable process_

_The Key generation specifies 2 ways to generate the private key x:_


_Private Key:_


_len(q) bit output of RBG where 1 <= x <= q-1_
_len(q) + 64 bit output of RBG, followed by a mod q-1 operation where 1<= x<=q-1_

_The security strength of the RBG must be at least that of the security offered by the FFC_
_parameter set._


_To test the cryptographic and field prime generation method for the provable primes method or_
_the group generator g for a verifiable process, the evaluator must seed the TSF parameter_
_generation routine with sufficient data to deterministically generate the parameter set._


_For each key length supported, the evaluator shall have the TSF generate 25 parameter sets and_
_key pairs. The evaluator shall verify the correctness of the TSF’s implementation by comparing_
_values generated by the TSF with those generated from a known good implementation._
_Verification must also confirm_

_g != 0,1_
_q divides p-1_
_g^q mod p = 1_
_g^x mod p = y_


_for each FFC parameter set and key pair._


**FCS_CKM.2/UNLOCKED Cryptographic Key Establishment**


FCS_CKM.2.1/UNLOCKED

The TSF shall **perform** cryptographic **key establishment** in accordance with a
specified cryptographic key **establishment** method [ **selection** :

_[RSA-based key establishment schemes] that meet the following [_ _**selection**_ _:_

_NIST Special Publication 800-56B, “Recommendation for Pair-Wise Key_
_Establishment Schemes Using Integer Factorization Cryptography”_
_RSAES-PKCS1-v1_5 as specified in Section 7.2 of RFC 8017, "Public-_
_Key Cryptography Standards (PKCS) #1:RSA Cryptography_
_Specifications Version 2.2"_

_]_

_[Elliptic curve-based key establishment schemes] that meet the following:_

_[NIST Special Publication 800-56A Revision 3, "Recommendation for Pair-_
_Wise Key Establishment Schemes Using Discrete Logarithm Cryptography"]_

_[Finite field-based key establishment schemes] that meet the following:_

_[NIST Special Publication 800-56A Revision 3, "Recommendation for Pair-_
_Wise Key Establishment Schemes Using Discrete Logarithm Cryptography"]_

].


**Application Note:** The ST author must select all key establishment schemes
used for the selected cryptographic protocols and any RSA-based key
establishment schemes that may be used to satisfy FDP_DAR or FCS_STG. Also,
FCS_TLSC_EXT.1 requires ciphersuites that use RSA-based key establishment
schemes.


The RSA-based key establishment schemes are described in Section 9 of NIST
SP 800-56B; however, Section 9 relies on implementation of other sections in SP
800-56B. If the TOE only acts as a receiver in the RSA key establishment
scheme, the TOE does not need to implement RSA key generation.


The elliptic curves used for the key establishment scheme must correlate with
the curves specified in FCS_CKM.1.1.


The domain parameters used for the finite field-based key establishment scheme
are specified by the key generation according to FCS_CKM.1.1. The finite fieldbased key establishment schemes that conform to NIST SP 800-56A Revision 3
correspond to the "safe-prime" groups selection in FCS_CKM.1.1.


**Evaluation Activities**


_FCS_CKM.2/UNLOCKED_
_**TSS**_
_The evaluator shall ensure that the supported key establishment schemes correspond to the key_
_generation schemes identified in FCS_CKM.1.1. If the ST specifies more than one scheme, the_
_evaluator shall examine the TSS to verify that it identifies the usage for each scheme._


_**Guidance**_
_The evaluator shall verify that the AGD guidance instructs the administrator how to configure_
_the TOE to use the selected key establishment schemes._


_**Tests**_
_**Evaluation Activity Note:**_ _The following tests require the developer to provide access to a test_
_platform that provides the evaluator with tools that are typically not found on factory products._


_The evaluator shall verify the implementation of the key establishment schemes supported by_
_the TOE using the applicable tests below._


_**SP800-56A Revision 3 Key Establishment Schemes**_

_The evaluator shall verify a TOE's implementation of SP800-56A Revision 3 key establishment_
_schemes using the following Function and Validity tests. These validation tests for each key_
_agreement scheme verify that a TOE has implemented the components of the key agreement_
_scheme according to the specifications in the Recommendation. These components include the_
_calculation of the DLC primitives (the shared secret value Z) and the calculation of the derived_
_keying material (DKM) via the Key Derivation Function (KDF). If key confirmation is supported,_
_the evaluator shall also verify that the components of key confirmation have been implemented_
_correctly, using the test procedures described below. This includes the parsing of the DKM, the_
_generation of MACdata and the calculation of MacTag._


_**Function Test**_


_The Function test verifies the ability of the TOE to implement the key agreement schemes_


_correctly. To conduct this test the evaluator shall generate or obtain test vectors from a known_
_good implementation of the TOE supported schemes. For each supported key agreement_
_scheme-key agreement role combination, KDF type, and, if supported, key confirmation role- key_
_confirmation type combination, the tester shall generate 10 sets of test vectors. The data set_
_consists of one set of domain parameter values (FFC) or the NIST approved curve (ECC) per 10_
_sets of public keys. These keys are static, ephemeral or both depending on the scheme being_
_tested._


_The evaluator shall obtain the DKM, the corresponding TOE’s public keys (static or ephemeral),_
_the MAC tags, and any inputs used in the KDF, such as the Other Information field OI and TOE_
_id fields._


_If the TOE does not use a KDF defined in SP 800-56A Revision 3, the evaluator shall obtain only_
_the public keys and the hashed value of the shared secret._


_The evaluator shall verify the correctness of the TSF’s implementation of a given scheme by_
_using a known good implementation to calculate the shared secret value, derive the keying_
_material DKM, and compare hashes or MAC tags generated from these values._


_If key confirmation is supported, the TSF shall perform the above for each implemented_
_approved MAC algorithm._


_**Validity Test**_


_The Validity test verifies the ability of the TOE to recognize another party’s valid and invalid key_
_agreement results with or without key confirmation. To conduct this test, the evaluator shall_
_obtain a list of the supporting cryptographic functions included in the SP800-56A Revision 3 key_
_agreement implementation to determine which errors the TOE should be able to recognize. The_
_evaluator generates a set of 24 (FFC) or 30 (ECC) test vectors consisting of data sets including_
_domain parameter values or NIST approved curves, the evaluator’s public keys, the TOE’s_
_public/private key pairs, MacTag, and any inputs used in the KDF, such as the other info and_
_TOE id fields._


_The evaluator shall inject an error in some of the test vectors to test that the TOE recognizes_
_invalid key agreement results caused by the following fields being incorrect: the shared secret_
_value Z, the DKM, the other information field OI, the data to be MACed, or the generated_
_MacTag. If the TOE contains the full or partial (only ECC) public key validation, the evaluator_
_will also individually inject errors in both parties’ static public keys, both parties’ ephemeral_
_public keys and the TOE’s static private key to assure the TOE detects errors in the public key_
_validation function or the partial key validation function (in ECC only). At least two of the test_
_vectors shall remain unmodified and therefore should result in valid key agreement results (they_
_should pass)._


_The TOE shall use these modified test vectors to emulate the key agreement scheme using the_
_corresponding parameters. The evaluator shall compare the TOE’s results with the results using_
_a known good implementation verifying that the TOE detects these errors._


_**SP800-56B Key Establishment Schemes**_

_The evaluator shall verify that the TSS describes whether the TOE acts as a sender, a recipient,_
_or both for RSA-based key establishment schemes._


_If the TOE acts as a sender, the following evaluation activity shall be performed to ensure the_
_proper operation of every TOE supported combination of RSA-based key establishment scheme:_
_To conduct this test the evaluator shall generate or obtain test vectors from a known good_
_implementation of the TOE supported schemes. For each combination of supported key_
_establishment scheme and its options (with or without key confirmation if supported, for each_
_supported key confirmation MAC function if key confirmation is supported, and for each_
_supported mask generation function if KTS-OAEP is supported), the tester shall generate 10 sets_
_of test vectors. Each test vector shall include the RSA public key, the plaintext keying material,_
_any additional input parameters if applicable, the MacKey and MacTag if key confirmation is_
_incorporated, and the outputted ciphertext. For each test vector, the evaluator shall perform a_
_key establishment encryption operation on the TOE with the same inputs (in cases where key_
_confirmation is incorporated, the test shall use the MacKey from the test vector instead of the_
_randomly generated MacKey used in normal operation) and ensure that the outputted ciphertext_
_is equivalent to the ciphertext in the test vector._


_If the TOE acts as a receiver, the following evaluation activities shall be performed to ensure the_
_proper operation of every TOE supported combination of RSA-based key establishment scheme:_
_To conduct this test the evaluator shall generate or obtain test vectors FCS_CKM.2.1/LOCKED_
_from a known good implementation of the TOE supported schemes. For each combination of_
_supported key establishment scheme and its options (with our without key confirmation if_
_supported, for each supported key confirmation MAC function if key confirmation is supported,_
_and for each supported mask generation function if KTS-OAEP is supported), the tester shall_
_generate 10 sets of test vectors. Each test vector shall include the RSA private key, the plaintext_
_keying material (KeyData), any additional input parameters if applicable, the MacTag in cases_
_where key confirmation is incorporated, and the outputted ciphertext. For each test vector, the_


_evaluator shall perform the key establishment decryption operation on the TOE and ensure that_
_the outputted plaintext keying material (KeyData) is equivalent to the plaintext keying material_
_in the test vector. In cases where key confirmation is incorporated, the evaluator shall perform_
_the key confirmation steps and ensure that the outputted MacTag is equivalent to the MacTag in_
_the test vector._


_The evaluator shall ensure that the TSS describes how the TOE handles decryption errors. In_
_accordance with NIST Special Publication 800-56B, the TOE must not reveal the particular error_
_that occurred, either through the contents of any outputted or logged error message or through_
_timing variations. If KTS-OAEP is supported, the evaluator shall create separate contrived_
_ciphertext values that trigger each of the three decryption error checks described in NIST_
_Special Publication 800-56B section 7.2.2.3, ensure that each decryption attempt results in an_
_error, and ensure that any outputted or logged error message is identical for each. If KTS-KEM-_
_KWS is supported, the evaluator shall create separate contrived ciphertext values that trigger_
_each of the three decryption error checks described in NIST Special Publication 800-56B section_
_7.2.3.3, ensure that each decryption attempt results in an error, and ensure that any outputted_
_or logged error message is identical for each._


_**RSAES-PKCS1-v1_5 Key Establishment Schemes**_

_The evaluator shall verify the correctness of the TSF's implementation of RSAES-PKCS1-v1_5 by_
_using a known good implementation for each protocol selected in FTP_ITC_EXT.1 that uses_
_RSAES-PKCS1-v1_5._


_**FFC Schemes using "safe-prime" groups**_

_The evaluator shall verify the correctness of the TSF's implementation of "safe-prime" groups by_
_using a known good implementation for each protocol selected in FTP_ITC_EXT.1 that uses_
_"safe-prime" groups. This test must be performed for each "safe-prime" group that each protocol_
_uses._


**FCS_CKM.2/LOCKED Cryptographic Key Establishment**


FCS_CKM.2.1/LOCKED

The TSF shall **perform** cryptographic **key establishment** in accordance with a
specified cryptographic key **establishment** method: [ **selection** :

_[RSA-based key establishment schemes] that meet the following: [NIST_
_Special Publication 800-56B, “Recommendation for Pair-Wise Key_
_Establishment Schemes Using Integer Factorization Cryptography”]_

_[Elliptic curve-based key establishment schemes] that meet the following:_

_[_ _**selection**_ _:_

_**NIST Special Publication 800-56A Revision 3, "Recommendation**_
_**for Pair-Wise Key Establishment Schemes Using Discrete**_
_**Logarithm Cryptography"**_
_**RFC 7748, "Elliptic Curves for Security"**_

_]_

_[Finite field-based key establishment schemes] that meet the following:_

_[NIST Special Publication 800-56A Revision 3, "Recommendation for Pair-_
_Wise Key Establishment Schemes Using Discrete Logarithm Cryptography"]_

] **for the purposes of encrypting sensitive data received while the device**
**is locked.**


**Application Note:** The RSA-based key establishment schemes are described in
Section 9 of NIST SP 800-56B; however, Section 9 relies on implementation of
other sections in SP 800-56B. If the TOE acts as a receiver in the RSA key
establishment scheme, the TOE does not need to implement RSA key generation.


The elliptic curves used for the key establishment scheme must correlate with
the curves specified in FCS_CKM.1.1.


The domain parameters used for the finite field-based key establishment scheme
are specified by the key generation according to FCS_CKM.1.1.


**Evaluation Activities**


_FCS_CKM.2/LOCKED_
_**TSS**_
_There are no TSS evaluation activities for this component._


_**Guidance**_
_There are no guidance evaluation activities for this component._


_**Tests**_


_The test for SP800-56A Revision 3 and SP800-56B key establishment schemes is performed in_
_association with FCS_CKM.2/UNLOCKED._


_**Curve25519 Key Establishment Schemes**_


_The evaluator shall verify a TOE's implementation of the key agreement scheme using the_
_following Function and Validity tests. These validation tests for each key agreement scheme_
_verify that a TOE has implemented the components of the key agreement scheme according to_
_the specification. These components include the calculation of the shared secret K and the hash_
_of K._


_**Function Test**_


_The Function test verifies the ability of the TOE to implement the key agreement schemes_
_correctly. To conduct this test the evaluator shall generate or obtain test vectors from a known_
_good implementation of the TOE supported schemes. For each supported key agreement role_
_and hash function combination, the tester shall generate 10 sets of public keys. These keys are_
_static, ephemeral or both depending on the scheme being tested._


_The evaluator shall obtain the shared secret value K, and the hash of K._


_The evaluator shall verify the correctness of the TSF’s implementation of a given scheme by_
_using a known good implementation to calculate the shared secret value K and compare the_
_hash generated from this value._


_**Validity Test**_


_The Validity test verifies the ability of the TOE to recognize another party’s valid and invalid key_
_agreement results. To conduct this test, the evaluator generates a set of 30 test vectors_
_consisting of data sets including the evaluator’s public keys and the TOE’s public/private key_
_pairs._


_The evaluator shall inject an error in some of the test vectors to test that the TOE recognizes_
_invalid key agreement results caused by the following fields being incorrect: the shared secret_
_value K or the hash of K. At least two of the test vectors shall remain unmodified and therefore_
_should result in valid key agreement results (they should pass)._


_The TOE shall use these modified test vectors to emulate the key agreement scheme using the_
_corresponding parameters. The evaluator shall compare the TOE’s results with the results using_
_a known good implementation verifying that the TOE detects these errors._


**FCS_CKM_EXT.1 Cryptographic Key Support**


FCS_CKM_EXT.1.1

The TSF shall support [ **selection** : _immutable hardware_, _mutable hardware_ ]
REKs with a [ **selection** : _symmetric_, _asymmetric_ ] key of strength [ **selection** :
_112 bits_, _128 bits_, _192 bits_, _256 bits_ ].


FCS_CKM_EXT.1.2

Each REK shall be hardware-isolated from the OS on the TSF in runtime.


FCS_CKM_EXT.1.3

Each REK shall be generated by an RBG in accordance with FCS_RBG_EXT.1.


**Application Note:** Either asymmetric or symmetric keys are allowed; the ST
author makes the selection appropriate for the device. Symmetric keys must be
of size 128 or 256 bits in order to correspond with FCS_COP.1/ENCRYPT.
Asymmetric keys may be of any strength corresponding to FCS_CKM.1.


The raw key material of "immutable hardware" REKs is computationally
processed by hardware and software cannot access the raw key material. Thus if
immutable hardware is selected in FCS_CKM_EXT.1.1 it implicitly meets
FCS_CKM_EXT.7. If mutable hardware is selected in FCS_CKM_EXT.1.1,
FCS_CKM_EXT.7 must be included in the ST.


The lack of a public/documented API for importing or exporting the REK, when a
private/undocumented API exists, is not sufficient to meet this requirement.


The RBG used to generate a REK may be an RBG native to the hardware key
container or may be an off-device RBG. If performed by an off-device RBG, the
device manufacturer must not be able to access a REK after the manufacturing
process has been completed. The Evaluation Activities for these two cases differ.


**Evaluation Activities**


**FCS_CKM_EXT.2 Cryptographic Key Random Generation**


FCS_CKM_EXT.2.1

All DEKs shall be [ **selection** :

_randomly generated_
_from the combination of a randomly generated DEK with another DEK or_
_salt in a way that preserves the effective entropy of each factor by_

_[_ _**selection**_ _: using an XOR operation, concatenating the keys and using a_
_KDF (as described in SP 800-108), concatenating the keys and using a KDF_
_(as described in SP 800-56C) ]_

] with entropy corresponding to the security strength of AES key sizes of

[ **selection** : _128_, _256_ ] bits.


**Application Note:** The intent of this requirement is to ensure that the DEK
cannot be recovered with less work than a full exhaust of the key space for AES.
The key generation capability of the TOE uses an RBG implemented on the TOE
device (FCS_RBG_EXT.1). Either 128-bit or 256-bit (or both) are allowed; the ST
author makes the selection appropriate for the device. A DEK is used in addition
to the KEK so that authentication factors can be changed without having to reencrypt all of the user data on the device.


The ST author selects all applicable DEK generation types implemented by the
TOE.


SP 800-56C specifies a two-step key derivation procedure that employs an
extraction-then-expansion technique for deriving keying material from a shared
secret generated during a key establishment scheme. The Randomness
Extraction step as described in Section 5 of SP 800-56C is followed by Key
Expansion using the key derivation functions defined in SP 800-108 (as
described in Section 6 of SP 800-56C).


**Evaluation Activities**


_FCS_CKM_EXT.2_
_**TSS**_
_The evaluator shall ensure that the documentation of the product's encryption key management_
_is detailed enough that, after reading, the product's key management hierarchy is clear and that_
_it meets the requirements to ensure the keys are adequately protected. The evaluator shall_
_ensure that the documentation includes both an essay and one or more diagrams. Note that this_
_may also be documented as separate proprietary evidence rather than being included in the TSS._


_The evaluator shall also examine the key hierarchy section of the TSS to ensure that the_
_formation of all DEKs is described and that the key sizes match that described by the ST author._
_The evaluator shall examine the key hierarchy section of the TSS to ensure that each DEK is_
_generated or combined from keys of equal or greater security strength using one of the selected_
_methods._

_If the symmetric DEK is generated by an RBG, the evaluator shall review the TSS to_
_determine that it describes how the functionality described by FCS_RBG_EXT.1 is invoked._
_The evaluator uses the description of the RBG functionality in FCS_RBG_EXT.1 or_
_documentation available for the operational environment to determine that the key size_
_being requested is greater than or equal to the key size and mode to be used for the_
_encryption/decryption of the data._
_If the DEK is formed from a combination, the evaluator shall verify that the TSS describes_
_the method of combination and that this method is either an XOR or a KDF to justify that_
_the effective entropy of each factor is preserved. The evaluator shall also verify that each_
_combined value was originally generated from an Approved DRBG described in_
_FCS_RBG_EXT.1._
_If concatenating the keys and using a KDF (as described in SP 800-56C) is selected, the_
_evaluator shall ensure the TSS includes a description of the randomness extraction step._

_The description must include how an approved untruncated MAC function is being used for the_
_randomness extraction step and the evaluator must verify the TSS describes that the output_
_length (in bits) of the MAC function is at least as large as the targeted security strength (in bits)_
_of the parameter set employed by the key establishment scheme (see Tables 1-3 of SP 800-56C)._


_The description must include how the MAC function being used for the randomness extraction_
_step is related to the PRF used in the key expansion and verify the TSS description includes the_
_correct MAC function:_

_If an HMAC-hash is used in the randomness extraction step, then the same HMAC-hash_
_(with the same hash function hash) is used as the PRF in the key expansion step._
_If an AES-CMAC (with key length 128, 192, or 256 bits) is used in the randomness_
_extraction step, then AES-CMAC with a 128-bit key is used as the PRF in the key expansion_
_step._
_The description must include the lengths of the salt values being used in the randomness_
_extraction step and the evaluator shall verify the TSS description includes correct salt_
_lengths:_
_If an HMAC-hash is being used as the MAC, the salt length can be any value up to the_
_maximum bit length permitted for input to the hash function hash._
_If an AES-CMAC is being used as the MAC, the salt length shall be the same length as the_
_AES key (i.e. 128, 192, or 256 bits)._

_(conditional) If a KDF is used, the evaluator shall ensure that the TSS includes a description of_
_the key derivation function and shall verify the key derivation uses an approved derivation mode_
_and key expansion algorithm according to SP 800-108 or SP 800-56C._


_**Guidance**_
_The evaluator uses the description of the RBG functionality in FCS_RBG_EXT.1 or_
_documentation available for the operational environment to determine that the key size being_
_generated or combined is identical to the key size and mode to be used for the_
_encryption/decryption of the data._


_**Tests**_
_If a KDF is used, the evaluator shall perform one or more of the following tests to verify the_
_correctness of the key derivation function, depending on the modes that are supported. Table 4_
_maps the data fields to the notations used in SP 800-108 and SP 800-56C._


_**Table 4: Notations used in SP 800-108 and SP 800-56C**_


_**Data Fields**_ _**Notations**_


_Pseudorandom function_ _PRF_ _PRF_


_Counter length_ _r_ _r_


_Length of output of PRF_ _h_ _h_


_Length of derived keying material_ _L_ _L_


_Length of input values_ _l length_ _l length_


_Pseudorandom input values I_ _K1 (key derivation key)_ _Z (shared secret)_


_Pseudorandom salt values_ _n/a_ _s_


_Randomness extraction MAC_ _n/a_ _MAC_


_**Counter Mode Tests:**_


_The evaluator shall determine the following characteristics of the key derivation function:_

_One or more pseudorandom functions that are supported by the implementation (PRF)._
_One or more of the values {8, 16, 24, 32} that equal the length of the binary representation_
_of the counter (r)._
_The length (in bits) of the output of the PRF (h)._
_Minimum and maximum values for the length (in bits) of the derived keying material (L)._
_These values can be equal if only one value of L is supported. These must be evenly divisible_
_by h._
_Up to two values of L that are NOT evenly divisible by h._
_Location of the counter relative to fixed input data: before, after, or in the middle._

_Counter before fixed input data: fixed input data string length (in bytes), fixed input_
_data string value._
_Counter after fixed input data: fixed input data string length (in bytes), fixed input data_
_string value._
_Counter in the middle of fixed input data: length of data before counter (in bytes),_
_length of data after counter (in bytes), value of string input before counter, value of_
_string input after counter._

_The length (I_length) of the input values I._

_For each supported combination of I_length, MAC, salt, PRF, counter location, value of r, and_
_value of L, the evaluator shall generate 10 test vectors that include pseudorandom input values_
_I, and pseudorandom salt values. If there is only one value of L that is evenly divisible by h, the_
_evaluator shall generate 20 test vectors for it. For each test vector, the evaluator shall supply_
_this data to the TOE in order to produce the keying material output._


_The results from each test may either be obtained by the evaluator directly or by supplying the_
_inputs to the implementer and receiving the results in response. To determine correctness, the_
_evaluator shall compare the resulting values to those obtained by submitting the same inputs to_
_a known good implementation._


_**Feedback Mode Tests:**_


_The evaluator shall determine the following characteristics of the key derivation function:_

_One or more pseudorandom functions that are supported by the implementation (PRF)._
_The length (in bits) of the output of the PRF (h)._
_Minimum and maximum values for the length (in bits) of the derived keying material (L)._
_These values can be equal if only one value of L is supported. These must be evenly divisible_
_by h._
_Up to two values of L that are NOT evenly divisible by h._
_Whether or not zero-length IVs are supported._
_Whether or not a counter is used, and if so:_

_One or more of the values {8, 16, 24, 32} that equal the length of the binary_
_representation of the counter (r)._
_Location of the counter relative to fixed input data: before, after, or in the middle._

_Counter before fixed input data: fixed input data string length (in bytes), fixed_
_input data string value._
_Counter after fixed input data: fixed input data string length (in bytes), fixed input_
_data string value._
_Counter in the middle of fixed input data: length of data before counter (in bytes),_
_length of data after counter (in bytes), value of string input before counter, value_
_of string input after counter._

_The length (I_length) of the input values I._

_For each supported combination of I_length, MAC, salt, PRF, counter location (if a counter is_
_used), value of r (if a counter is used), and value of L, the evaluator shall generate 10 test_
_vectors that include pseudorandom input values I and pseudorandom salt values. If the KDF_
_supports zero-length IVs, five of these test vectors will be accompanied by pseudorandom IVs_
_and the other five will use zero-length IVs. If zero-length IVs are not supported, each test vector_
_will be accompanied by an pseudorandom IV. If there is only one value of L that is evenly_
_divisible by h, the evaluator shall generate 20 test vectors for it._


_For each test vector, the evaluator shall supply this data to the TOE in order to produce the_
_keying material output. The results from each test may either be obtained by the evaluator_
_directly or by supplying the inputs to the implementer and receiving the results in response. To_
_determine correctness, the evaluator shall compare the resulting values to those obtained by_
_submitting the same inputs to a known good implementation._


_**Double Pipeline Iteration Mode Tests:**_


_The evaluator shall determine the following characteristics of the key derivation function:_

_One or more pseudorandom functions that are supported by the implementation (PRF)._
_The length (in bits) of the output of the PRF (h)._
_Minimum and maximum values for the length (in bits) of the derived keying material (L)._
_These values can be equal if only one value of L is supported. These must be evenly divisible_
_by h._
_Up to two values of L that are NOT evenly divisible by h._
_Whether or not a counter is used, and if so:_

_One or more of the values {8, 16, 24, 32} that equal the length of the binary_
_representation of the counter (r)._
_Location of the counter relative to fixed input data: before, after, or in the middle._

_Counter before fixed input data: fixed input data string length (in bytes), fixed_
_input data string value._
_Counter after fixed input data: fixed input data string length (in bytes), fixed input_
_data string value._
_Counter in the middle of fixed input data: length of data before counter (in bytes),_
_length of data after counter (in bytes), value of string input before counter, value_
_of string input after counter._

_The length (I_length) of the input values I._

_For each supported combination of I_length, MAC, salt, PRF, counter location (if a counter is_
_used), value of r (if a counter is used), and value of L, the evaluator shall generate 10 test_
_vectors that include pseudorandom input values I, and pseudorandom salt values. If there is only_
_one value of L that is evenly divisible by h, the evaluator shall generate 20 test vectors for it._


_For each test vector, the evaluator shall supply this data to the TOE in order to produce the_
_keying material output. The results from each test may either be obtained by the evaluator_
_directly or by supplying the inputs to the implementer and receiving the results in response. To_
_determine correctness, the evaluator shall compare the resulting values to those obtained by_
_submitting the same inputs to a known good implementation._


**FCS_CKM_EXT.3 Cryptographic Key Generation**


FCS_CKM_EXT.3.1

The TSF shall use [ **selection** :

_asymmetric KEKs of [_ _**assignment**_ _: security strength greater than or equal_
_to 112 bits] security strength_
_symmetric KEKs of [_ _**selection**_ _: 128-bit, 256-bit ] security strength_
_corresponding to at least the security strength of the keys encrypted by the_
_KEK_

].


**Application Note:** The ST author selects all applicable KEK types implemented
by the TOE.


FCS_CKM_EXT.3.2

The TSF shall generate all KEKs using one of the following methods:

Derive the KEK from a Password Authentication Factor according to
FCS_COP.1.1 **/CONDITION** and

[ **selection** :

_Generate the KEK using an RBG that meets this profile (as specified in_
_FCS_RBG_EXT.1)_
_Generate the KEK using a key generation scheme that meets this profile (as_
_specified in FCS_CKM.1)_
_Combine the KEK from other KEKs in a way that preserves the effective_
_entropy of each factor by [_ _**selection**_ _: using an XOR operation,_
_concatenating the keys and using a KDF (as described in SP 800-108),_
_concatenating the keys and using a KDF (as described in SP 800-56C),_
_encrypting one key with another ]_

].


**Application Note:** The conditioning of passwords is performed in accordance
with FCS_COP.1/CONDITION.


It is expected that key generation derived from conditioning, using an RBG or
generation scheme, and through combination, will each be necessary to meet the
requirements set out in this document. In particular, Figure 3 has KEKs of each
type: KEK_3 is generated, KEK_1 is derived from a Password Authentication
Factor, and KEK_2 is combined from two KEKs. In Figure 3, KEK_3 may either
be a symmetric key generated from an RBG or an asymmetric key generated


using a key generation scheme according to FCS_CKM.1.


If combined, the ST author should describe which method of combination is used
in order to justify that the effective entropy of each factor is preserved.


SP 800-56C specifies a two-step key derivation procedure that employs an
extraction-then-expansion technique for deriving keying material from a shared
secret generated during a key establishment scheme. The Randomness
Extraction step as described in Section 5 of SP 800-56C is followed by Key
Expansion using the key derivation functions defined in SP 800-108 (as
described in Section 6 of SP 800-56C).


**Evaluation Activities**


_FCS_CKM_EXT.3_
_**TSS**_
_The evaluator shall examine the key hierarchy section of the TSS to ensure that the formation of_
_all KEKs are described and that the key sizes match that described by the ST author. The_
_evaluator shall examine the key hierarchy section of the TSS to ensure that each key (DEKs,_
_software-based key storage, and KEKs) is encrypted by keys of equal or greater security strength_
_using one of the selected methods._


_The evaluator shall review the TSS to verify that it contains a description of the conditioning_
_used to derive KEKs. This description must include the size and storage location of salts. This_
_activity may be performed in combination with that for FCS_COP.1/CONDITION._


_(conditional) If the symmetric KEK is generated by an RBG, the evaluator shall review the TSS to_
_determine that it describes how the functionality described by FCS_RBG_EXT.1 is invoked. The_
_evaluator uses the description of the RBG functionality in FCS_RBG_EXT.1 or documentation_
_available for the operational environment to determine that the key size being requested is_
_greater than or equal to the key size and mode to be used for the encryption/decryption of the_
_data._


_(conditional) If the KEK is generated according to an asymmetric key scheme, the evaluator shall_
_review the TSS to determine that it describes how the functionality described by FCS_CKM.1 is_
_invoked. The evaluator uses the description of the key generation functionality in FCS_CKM.1 or_
_documentation available for the operational environment to determine that the key strength_
_being requested is greater than or equal to 112 bits._


_(conditional) If the KEK is formed from a combination, the evaluator shall verify that the TSS_
_describes the method of combination and that this method is either an XOR, a KDF, or_
_encryption._


_(conditional) If a KDF is used, the evaluator shall ensure that the TSS includes a description of_
_the key derivation function and shall verify the key derivation uses an approved derivation mode_
_and key expansion algorithm according to SP 800-108._


_(conditional) If concatenating the keys and using a KDF (as described in SP 800-56C) is selected,_
_the evaluator shall ensure the TSS includes a description of the randomness extraction step. The_
_description must include_

_How an approved untruncated MAC function is being used for the randomness extraction_
_step and the evaluator must verify the TSS describes that the output length (in bits) of the_
_MAC function is at least as large as the targeted security strength (in bits) of the parameter_
_set employed by the key establishment scheme (see Tables 1-3 of SP 800-56C)._
_How the MAC function being used for the randomness extraction step is related to the PRF_
_used in the key expansion and verify the TSS description includes the correct MAC_
_function:_

_If an HMAC-hash is used in the randomness extraction step, then the same HMAC-_
_hash (with the same hash function hash) is used as the PRF in the key expansion step._
_If an AES-CMAC (with key length 128, 192, or 256 bits) is used in the randomness_
_extraction step, then AES-CMAC with a 128-bit key is used as the PRF in the key_
_expansion step._

_The lengths of the salt values being used in the randomness extraction step and the_
_evaluator shall verify the TSS description includes correct salt lengths:_

_If an HMAC-hash is being used as the MAC, the salt length can be any value up to the_
_maximum bit length permitted for input to the hash function hash._
_If an AES-CMAC is being used as the MAC, the salt length shall be the same length as_
_the AES key (i.e. 128, 192, or 256 bits)._


_The evaluator shall also ensure that the documentation of the product's encryption key_
_management is detailed enough that, after reading, the product's key management hierarchy is_
_clear and that it meets the requirements to ensure the keys are adequately protected. The_
_evaluator shall ensure that the documentation includes both an essay and one or more diagrams._
_Note that this may also be documented as separate proprietary evidence rather than being_
_included in the TSS._


_**Guidance**_
_There are no guidance evaluation activities for this component._


_**Tests**_
_If a KDF is used, the evaluator shall perform one or more of the following tests to verify the_
_correctness of the key derivation function, depending on the modes that are supported. Table 5_
_maps the data fields to the notations used in SP 800-108 and SP 800-56C._


_**Table 5: Notations used in SP 800-108 and SP 800-56C**_


_**Data Fields**_ _**Notations**_


_Pseudorandom function_ _PRF_ _PRF_


_Counter length_ _r_ _r_


_Length of output of PRF_ _h_ _h_


_Length of derived keying material_ _L_ _L_


_Length of input values_ _I_length_ _I_length_


_Pseudorandom input values I_ _K1 (key derivation key)_ _Z (shared secret)_


_Pseudorandom salt values_ _n/a_ _s_


_Randomness extraction MAC_ _n/a_ _MAC_


_**Counter Mode Tests:**_


_The evaluator shall determine the following characteristics of the key derivation function:_

_One or more pseudorandom functions that are supported by the implementation (PRF)._
_One or more of the values {8, 16, 24, 32} that equal the length of the binary representation_
_of the counter (r)._
_The length (in bits) of the output of the PRF (h)._
_Minimum and maximum values for the length (in bits) of the derived keying material (L)._
_These values can be equal if only one value of L is supported. These must be evenly divisible_
_by h._
_Up to two values of L that are NOT evenly divisible by h._
_Location of the counter relative to fixed input data: before, after, or in the middle._

_Counter before fixed input data: fixed input data string length (in bytes), fixed input_
_data string value._
_Counter after fixed input data: fixed input data string length (in bytes), fixed input data_
_string value._
_Counter in the middle of fixed input data: length of data before counter (in bytes),_
_length of data after counter (in bytes), value of string input before counter, value of_
_string input after counter._

_The length (I_length) of the input values I._


_For each supported combination of I_length, MAC, salt, PRF, counter location, value of r, and_
_value of L, the evaluator shall generate 10 test vectors that include pseudorandom input values_
_I, and pseudorandom salt values. If there is only one value of L that is evenly divisible by h, the_
_evaluator shall generate 20 test vectors for it. For each test vector, the evaluator shall supply_
_this data to the TOE in order to produce the keying material output._


_The results from each test may either be obtained by the evaluator directly or by supplying the_
_inputs to the implementer and receiving the results in response. To determine correctness, the_
_evaluator shall compare the resulting values to those obtained by submitting the same inputs to_
_a known good implementation._


_**Feedback Mode Tests:**_
_The evaluator shall determine the following characteristics of the key derivation function:_

_One or more pseudorandom functions that are supported by the implementation (PRF)._
_The length (in bits) of the output of the PRF (h)._
_Minimum and maximum values for the length (in bits) of the derived keying material (L)._
_These values can be equal if only one value of L is supported. These must be evenly divisible_
_by h._
_Up to two values of L that are NOT evenly divisible by h._
_Whether or not zero-length IVs are supported._
_Whether or not a counter is used, and if so:_

_One or more of the values {8, 16, 24, 32} that equal the length of the binary_


_representation of the counter (r)._
_Location of the counter relative to fixed input data: before, after, or in the middle._

_Counter before fixed input data: fixed input data string length (in bytes), fixed_
_input data string value._
_Counter after fixed input data: fixed input data string length (in bytes), fixed input_
_data string value._
_Counter in the middle of fixed input data: length of data before counter (in bytes),_
_length of data after counter (in bytes), value of string input before counter, value_
_of string input after counter._

_The length (I_length) of the input values I._


_For each supported combination of I_length, MAC, salt, PRF, counter location (if a counter is_
_used), value of r (if a counter is used), and value of L, the evaluator shall generate 10 test_
_vectors that include pseudorandom input values I and pseudorandom salt values. If the KDF_
_supports zero-length IVs, five of these test vectors will be accompanied by pseudorandom IVs_
_and the other five will use zero-length IVs. If zero-length IVs are not supported, each test vector_
_will be accompanied by an pseudorandom IV. If there is only one value of L that is evenly_
_divisible by h, the evaluator shall generate 20 test vectors for it._


_For each test vector, the evaluator shall supply this data to the TOE in order to produce the_
_keying material output. The results from each test may either be obtained by the evaluator_
_directly or by supplying the inputs to the implementer and receiving the results in response. To_
_determine correctness, the evaluator shall compare the resulting values to those obtained by_
_submitting the same inputs to a known good implementation._


_**Double Pipeline Iteration Mode Tests:**_
_The evaluator shall determine the following characteristics of the key derivation function:_

_One or more pseudorandom functions that are supported by the implementation (PRF)._
_The length (in bits) of the output of the PRF (h)._
_Minimum and maximum values for the length (in bits) of the derived keying material (L)._
_These values can be equal if only one value of L is supported. These must be evenly divisible_
_by h._
_Up to two values of L that are NOT evenly divisible by h._
_Whether or not a counter is used, and if so:_

_One or more of the values {8, 16, 24, 32} that equal the length of the binary_
_representation of the counter (r)._
_Location of the counter relative to fixed input data: before, after, or in the middle._

_Counter before fixed input data: fixed input data string length (in bytes), fixed_
_input data string value._
_Counter after fixed input data: fixed input data string length (in bytes), fixed input_
_data string value._
_Counter in the middle of fixed input data: length of data before counter (in bytes),_
_length of data after counter (in bytes), value of string input before counter, value_
_of string input after counter._

_The length (I_length) of the input values I._


_For each supported combination of I_length, MAC, salt, PRF, counter location (if a counter is_
_used), value of r (if a counter is used), and value of L, the evaluator shall generate 10 test_
_vectors that include pseudorandom input values I, and pseudorandom salt values. If there is only_
_one value of L that is evenly divisible by h, the evaluator shall generate 20 test vectors for it._


_For each test vector, the evaluator shall supply this data to the TOE in order to produce the_
_keying material output. The results from each test may either be obtained by the evaluator_
_directly or by supplying the inputs to the implementer and receiving the results in response. To_
_determine correctness, the evaluator shall compare the resulting values to those obtained by_
_submitting the same inputs to a known good implementation._


**FCS_CKM_EXT.4 Key Destruction**


FCS_CKM_EXT.4.1

The TSF shall destroy cryptographic keys in accordance with the specified
cryptographic key destruction methods:

By clearing the KEK encrypting the target key
In accordance with the following rules

For volatile memory, the destruction shall be executed by a single
direct overwrite [ **selection** : _consisting of a pseudorandom pattern_
_using the TSF’s RBG_, _consisting of zeros_ ].
For non-volatile EEPROM, the destruction shall be executed by a single
direct overwrite consisting of a pseudo random pattern using the TSF’s
RBG (as specified in FCS_RBG_EXT.1), followed by a read-verify.
For non-volatile flash memory, that is not wear-leveled, the destruction
shall be executed [ **selection** : _by a single direct overwrite consisting of_
_zeros followed by a read-verify_, _by a block erase that erases the_
_reference to memory that stores data as well as the data itself_ ].


FCS_CKM_EXT.4.2



For non-volatile flash memory, that is wear-leveled, the destruction
shall be executed [ **selection** : _by a single direct overwrite consisting of_
_zeros_, _by a block erase_ ].
For non-volatile memory other than EEPROM and flash, the
destruction shall be executed by a single direct overwrite with a
random pattern that is changed before each write.


**Application Note:** The clearing indicated above applies to each intermediate
storage area for plaintext key or cryptographic critical security parameter (i.e.
any storage, such as memory buffers, that is included in the path of such data)
upon the transfer of the key or cryptographic critical security parameter to
another location.


Because plaintext key material is not allowed to be written to non-volatile
memory (FPT_KST_EXT.1), the second selection only applies to key material
written to volatile memory.


The TSF shall destroy all plaintext keying material and critical security
parameters when no longer needed.


**Application Note:** For the purposes of this requirement, plaintext keying
material refers to authentication data, passwords, secret/private symmetric keys,
private asymmetric keys, data used to derive keys, values derived from
passwords, etc.


Key destruction procedures are performed in accordance with
FCS_CKM_EXT.4.1.


There are multiple situations in which plaintext keying material is no longer
necessary, including when the TOE is powered off, when the wipe function is
performed, when trusted channels are disconnected, when keying material is no
longer needed by the trusted channel per the protocol, and when transitioning to
the locked state (for those values derived from the Password Authentication
Factor or that key material which is protected by the password-derived or
biometric-unlocked KEK according to FCS_STG_EXT.2 – see Figure 3). For keys
(or key material used to derive those keys) protecting sensitive data received in
the locked state, "no longer needed" includes "while in the locked state."


Trusted channels may include TLS, HTTPS, DTLS, IPsec VPNs, Bluetooth
BR/EDR, and Bluetooth LE. The plaintext keying material for these channels
includes (but is not limited to) master secrets, and Security Associations (SAs).


If REKs are processed in a separate execution environment on the same
Application Processor as the OS, REK key material must be cleared from RAM
immediately after use, and at least, must be wiped when the device is locked, as
the REK is part of the key hierarchy protecting sensitive data.



**Evaluation Activities**


_FCS_CKM_EXT.4_
_**TSS**_
_The evaluator shall check to ensure the TSS lists each type of plaintext key material (DEKs,_
_software-based key storage, KEKs, trusted channel keys, passwords, etc.) and its generation and_
_storage location._


_The evaluator shall verify that the TSS describes when each type of key material is cleared (for_
_example, on system power off, on wipe function, on disconnection of trusted channels, when no_
_longer needed by the trusted channel per the protocol, when transitioning to the locked state,_
_and possibly including immediately after use, while in the locked state, etc.)._


_The evaluator shall also verify that, for each type of key, the type of clearing procedure that is_
_performed (cryptographic erase, overwrite with zeros, overwrite with random pattern, or block_
_erase) is listed. If different types of memory are used to store the materials to be protected, the_
_evaluator shall check to ensure that the TSS describes the clearing procedure in terms of the_
_memory in which the data are stored._


_**Guidance**_
_There are no guidance evaluation activities for this component._


_**Tests**_
_**Evaluation Activity Note:**_ _The following tests require the developer to provide access to a test_
_platform that provides the evaluator with tools that are typically not found on factory products._


_For each software and firmware key clearing situation (including on system power off, on wipe_
_function, on disconnection of trusted channels, when no longer needed by the trusted channel_


_per the protocol, when transitioning to the locked state, and possibly including immediately after_
_use, while in the locked state) the evaluator shall repeat the following tests._


_For these tests the evaluator shall utilize appropriate development environment (e.g. a Virtual_
_Machine) and development tools (debuggers, simulators, etc.) to test that keys are cleared,_
_including all copies of the key that may have been created internally by the TOE during normal_
_cryptographic processing with that key._


_Test 3: Applied to each key held as plaintext in volatile memory and subject to destruction_
_by overwrite by the TOE (whether or not the plaintext value is subsequently encrypted for_
_storage in volatile or non-volatile memory). In the case where the only selection made for_
_the destruction method key was removal of power, then this test is unnecessary. The_
_evaluator shall:_

_1. Record the value of the key in the TOE subject to clearing._
_2. Cause the TOE to perform a normal cryptographic processing with the key from Step_

_#1._
_3. Cause the TOE to clear the key._
_4. Cause the TOE to stop the execution but not exit._
_5. Cause the TOE to dump the entire memory of the TOE into a binary file._
_6. Search the content of the binary file created in Step #5 for instances of the known key_

_value from Step #1._
_7. Break the key value from Step #1 into 3 similar sized pieces and perform a search_

_using each piece._


_Steps 1-6 ensure that the complete key does not exist anywhere in volatile memory. If a_
_copy is found, then the test fails._


_Step 7 ensures that partial key fragments do not remain in memory. If a fragment is found,_
_there is a minuscule chance that it is not within the context of a key (e.g., some random bits_
_that happen to match). If this is the case the test should be repeated with a different key in_
_Step #1. If a fragment is found the test fails._


_Test 4: Applied to each key held in non-volatile memory and subject to destruction by_
_overwrite by the TOE. The evaluator shall use special tools (as needed), provided by the_
_TOE developer if necessary, to view the key storage location:_

_1. Record the value of the key in the TOE subject to clearing._
_2. Cause the TOE to perform a normal cryptographic processing with the key from Step_

_#1._
_3. Cause the TOE to clear the key._
_4. Search the non-volatile memory the key was stored in for instances of the known key_

_value from Step #1. If a copy is found, then the test fails._
_5. Break the key value from Step #1 into 3 similar sized pieces and perform a search_

_using each piece. If a fragment is found then the test is repeated (as described for test_
_1 above), and if a fragment is found in the repeated test then the test fails._


_Test 5: Applied to each key held as non-volatile memory and subject to destruction by_
_overwrite by the TOE. The evaluator shall use special tools (as needed), provided by the_
_TOE developer if necessary, to view the key storage location:_

_1. Record the storage location of the key in the TOE subject to clearing._
_2. Cause the TOE to perform a normal cryptographic processing with the key from Step_

_#1._
_3. Cause the TOE to clear the key._
_4. Read the storage location in Step #1 of non-volatile memory to ensure the appropriate_

_pattern is utilized._


_The test succeeds if correct pattern is used to overwrite the key in the memory location. If_
_the pattern is not found the test fails._


**FCS_CKM_EXT.5 TSF Wipe**


FCS_CKM_EXT.5.1

The TSF shall wipe all protected data by [ **selection** :

_Cryptographically erasing the encrypted DEKs or the KEKs in non-volatile_
_memory by following the requirements in FCS_CKM_EXT.4.1_
_Overwriting all PD according to the following rules:_

_For EEPROM, the destruction shall be executed by a single direct_
_overwrite consisting of a pseudo random pattern using the TSF’s RBG_
_(as specified in FCS_RBG_EXT.1, followed by a read-verify._
_For flash memory, that is not wear-leveled, the destruction shall be_
_executed [_ _**selection**_ _: by a single direct overwrite consisting of zeros_
_followed by a read-verify, by a block erase that erases the reference to_
_memory that stores data as well as the data itself ]._
_For flash memory, that is wear-leveled, the destruction shall be_
_executed [_ _**selection**_ _: by a single direct overwrite consisting of zeros,_


FCS_CKM_EXT.5.2



_by a block erase ]._
_For non-volatile memory other than EEPROM and flash, the_
_destruction shall be executed by a single direct overwrite with a_
_random pattern that is changed before each write._

].


**Application Note:** Protected data is all non-TSF data, including all user or
enterprise data. Some or all of this data may be considered sensitive data as
well.


The TSF shall perform a power cycle on conclusion of the wipe procedure.



**Evaluation Activities**


**FCS_CKM_EXT.6 Salt Generation**


FCS_CKM_EXT.6.1

The TSF shall generate all salts using an RBG that meets FCS_RBG_EXT.1.


**Application Note:** This requirement refers only to salt generation. In the
examples given, a salt may be used as part of the scheme/algorithm.
Requirements on nonces or ephemeral keys are provided elsewhere, if needed.
The list below is provided for clarity, in order to give examples of where the TSF


may be generating cryptographic salts; it is not exhaustive nor is it intended to
mandate implementation of all of these schemes/algorithms. Cryptographic salts
are generated for various uses including:

RSASSA-PSS signature generation
DSA signature generation
ECDSA signature generation
DH static key agreement scheme
PBKDF
Key Agreement Scheme in NIST SP 800-56B
AES GCM


**Evaluation Activities**





**FCS_COP.1/ENCRYPT Cryptographic Operation**


FCS_COP.1.1/ENCRYPT

The TSF shall perform [ _encryption/decryption_ ] in accordance with a specified
cryptographic algorithm: [

_AES-CBC (as defined in FIPS PUB 197, and NIST SP 800-38A) mode_
_AES-CCMP (as defined in FIPS PUB 197, NIST SP 800-38C and IEEE_
_802.11-2012), and_

_[_ _**selection**_ _:_

_AES Key Wrap (KW) (as defined in NIST SP 800-38F)_
_AES Key Wrap with Padding (KWP) (as defined in NIST SP 800-38F)_
_AES-GCM (as defined in NIST SP 800-38D)_
_AES-CCM (as defined in NIST SP 800-38C)_
_AES-XTS (as defined in NIST SP 800-38E) mode_
_AES-CCMP-256 (as defined in NIST SP800-38C and IEEE 802.11ac-_
_2013)_
_AES-GCMP-256 (as defined in NIST SP800-38D and IEEE 802.11ac-_
_2013)_
_no other modes_

_]_

] and cryptographic key sizes [ _128-bit key sizes and [_ _**selection**_ _: 256-bit key_
_sizes, no other key sizes ]_ ].


**Application Note:** For the first selection, the ST author should choose the mode
or modes in which AES operates. For the second selection, the ST author should
choose the key sizes that are supported by this functionality. 128-bit CBC and
[CCMP are required in order to comply with the PP-Module for Wireless LAN](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=386&id=386)
Clients, version 1.0.


[Note that to comply with the PP-Module for Wireless LAN Clients, version 1.0,](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=386&id=386)
AES CCMP (which uses AES in CCM as specified in SP 800-38C) with
cryptographic key size of 128 bits must be implemented. If CCM is only
implemented to support CCMP for WLAN, AES-CCM does not need be selected.
Optionally, AES-CCMP-256 or AES-GCMP-256 with cryptographic key size of 256
bits may be implemented.


**Evaluation Activities**


_FCS_COP.1/ENCRYPT_
_**TSS**_
_There are no TSS evaluation activities for this component._


_**Guidance**_
_There are no guidance evaluation activities for this component._


_**Tests**_
_**Evaluation Activity Note:**_ _The following tests require the developer to provide access to a test_
_platform that provides the evaluator with tools that are typically not found on factory products._


_**AES-CBC Tests**_
_Test 8: AES-CBC Known Answer Tests_


_There are four Known Answer Tests (KATs), described below. In all KATs, the plaintext,_
_ciphertext, and IV values shall be 128-bit blocks. The results from each test may either be_
_obtained by the evaluator directly or by supplying the inputs to the implementer and_
_receiving the results in response. To determine correctness, the evaluator shall compare_
_the resulting values to those obtained by submitting the same inputs to a known good_
_implementation._


_Test 8.1: KAT-1. To test the encrypt functionality of AES-CBC, the evaluator shall_
_supply a set of 10 plaintext values and obtain the ciphertext value that results from_
_AES-CBC encryption of the given plaintext using a key value of all zeros and an IV of_
_all zeros. Five plaintext values shall be encrypted with a 128-bit all-zeros key, and the_
_other five shall be encrypted with a 256-bit all-zeros key._


_To test the decrypt functionality of AES-CBC, the evaluator shall perform the same test_
_as for encrypt, using 10 ciphertext values as input and AES-CBC decryption._


_Test 8.2: KAT-2. To test the encrypt functionality of AES-CBC, the evaluator shall_
_supply a set of 10 key values and obtain the ciphertext value that results from AES-_
_CBC encryption of an all-zeros plaintext using the given key value and an IV of all_
_zeros. Five of the keys shall be 128-bit keys, and the other five shall be 256-bit keys._


_To test the decrypt functionality of AES-CBC, the evaluator shall perform the same test_
_as for encrypt, using an all-zero ciphertext value as input and AES-CBC decryption._


_Test 8.3: KAT-3. To test the encrypt functionality of AES-CBC, the evaluator shall_
_supply the two sets of key values described below and obtain the ciphertext value that_
_results from AES encryption of an all-zeros plaintext using the given key value and an_
_IV of all zeros. The first set of keys shall have 128 128-bit keys, and the second set_
_shall have 256 256-bit keys. Key i in each set shall have the leftmost i bits be ones and_
_the rightmost N-i bits be zeros, for i in [1,N]._


_To test the decrypt functionality of AES-CBC, the evaluator shall supply the two sets of_
_key and ciphertext value pairs described below and obtain the plaintext value that_
_results from AES-CBC decryption of the given ciphertext using the given key and an IV_
_of all zeros. The first set of key or ciphertext pairs shall have 128 128-bit key or_
_ciphertext pairs, and the second set of key or ciphertext pairs shall have 256 256-bit_
_key or ciphertext pairs. Key i in each set shall have the leftmost i bits be ones and the_
_rightmost N-i bits be zeros, for i in [1,N]. The ciphertext value in each pair shall be the_
_value that results in an all-zeros plaintext when decrypted with its corresponding key._


_Test 8.4: KAT-4. To test the encrypt functionality of AES-CBC, the evaluator shall_
_supply the set of 128 plaintext values described below and obtain the two ciphertext_
_values that result from AES-CBC encryption of the given plaintext using a 128-bit key_
_value of all zeros with an IV of all zeros and using a 256-bit key value of all zeros with_
_an IV of all zeros, respectively. Plaintext value i in each set shall have the leftmost i_
_bits be ones and the rightmost 128-i bits be zeros, for i in [1,128]._


_To test the decrypt functionality of AES-CBC, the evaluator shall perform the same test_
_as for encrypt, using ciphertext values of the same form as the plaintext in the encrypt_
_test as input and AES-CBC decryption._


_Test 9: AES-CBC Multi-Block Message Test_


_The evaluator shall test the encrypt functionality by encrypting an i-block message where 1_
_< i <= 10. The evaluator shall choose a key, an IV and plaintext message of length i blocks_
_and encrypt the message, using the mode to be tested, with the chosen key and IV. The_
_ciphertext shall be compared to the result of encrypting the same plaintext message with_
_the same key and IV using a known good implementation._


_The evaluator shall also test the decrypt functionality for each mode by decrypting an i-_
_block message where 1 < i <= 10. The evaluator shall choose a key, an IV and a ciphertext_
_message of length i blocks and decrypt the message, using the mode to be tested, with the_
_chosen key and IV. The plaintext shall be compared to the result of decrypting the same_
_ciphertext message with the same key and IV using a known good implementation._


_Test 10: AES-CBC Monte Carlo Tests_


_The evaluator shall test the encrypt functionality using a set of 200 plaintext, IV, and key 3-_
_tuples. 100 of these shall use 128 bit keys, and 100 shall use 256 bit keys. The plaintext and_
_IV values shall be 128-bit blocks. For each 3-tuple, 1000 iterations shall be run as follows:_


_# Input: PT, IV, Key for i = 1 to 1000: if i == 1: CT[1] =_
_AES-CBC-Encrypt(Key, IV, PT) PT = IV else: CT[i] = AES-CBC-Encrypt(Key, PT) PT_
_= CT[i-1]_


_The ciphertext computed in the 1000_ _[th]_ _iteration (i.e. CT[1000]) is the result for that trial._
_This result shall be compared to the result of running 1000 iterations with the same values_
_using a known good implementation._


_The evaluator shall test the decrypt functionality using the same test as for encrypt,_
_exchanging CT and PT and replacing AES-CBC-Encrypt with AES-CBC-Decrypt._


_**AES-CCM Tests**_
_Test 11: The evaluator shall test the generation-encryption and decryption-verification_
_functionality of AES-CCM for the following input parameter and tag lengths:_


_**128 bit and 256 bit keys**_


_**Two payload lengths.**_ _One payload length shall be the shortest supported payload_
_length, greater than or equal to zero bytes. The other payload length shall be the_
_longest supported payload length, less than or equal to 32 bytes (256 bits)._


_**Two or three associated data lengths.**_ _One associated data length shall be 0, if_
_supported. One associated data length shall be the shortest supported payload length,_
_greater than or equal to zero bytes. One associated data length shall be the longest_
_supported payload length, less than or equal to 32 bytes (256 bits). If the_
_implementation supports an associated data length of 2_ _[16]_ _bytes, an associated data_
_length of 2_ _[16]_ _bytes shall be tested._


_**Nonce lengths.**_ _All supported nonce lengths between 7 and 13 bytes, inclusive, shall_
_be tested._


_**Tag lengths.**_ _All supported tag lengths of 4, 6, 8, 10, 12, 14 and 16 bytes shall be_
_tested._

_To test the generation-encryption functionality of AES-CCM, the evaluator shall perform the_
_following four tests:_

_Test 11.1: For EACH supported key and associated data length and ANY supported_
_payload, nonce and tag length, the evaluator shall supply one key value, one nonce_
_value and 10 pairs of associated data and payload values and obtain the resulting_
_ciphertext._


_Test 11.2: For EACH supported key and payload length and ANY supported associated_
_data, nonce and tag length, the evaluator shall supply one key value, one nonce value_
_and 10 pairs of associated data and payload values and obtain the resulting ciphertext._


_Test 11.3: For EACH supported key and nonce length and ANY supported associated_
_data, payload and tag length, the evaluator shall supply one key value and 10_
_associated data, payload and nonce value 3-tuples and obtain the resulting ciphertext._


_Test 11.4: For EACH supported key and tag length and ANY supported associated_
_data, payload and nonce length, the evaluator shall supply one key value, one nonce_
_value and 10 pairs of associated data and payload values and obtain the resulting_
_ciphertext._

_To determine correctness in each of the above tests, the evaluator shall compare the_
_ciphertext with the result of generation-encryption of the same inputs with a known good_
_implementation._


_To test the decryption-verification functionality of AES-CCM, for EACH combination of_
_supported associated data length, payload length, nonce length and tag length, the_
_evaluator shall supply a key value and 15 nonce, associated data and ciphertext 3-tuples_
_and obtain either a FAIL result or a PASS result with the decrypted payload. The evaluator_
_shall supply 10 tuples that should FAIL and 5 that should PASS per set of 15._


_**AES-GCM Test**_
_The evaluator shall test the authenticated encrypt functionality of AES-GCM for each_
_combination of the following input parameter lengths:_


_**128 bit and 256 bit keys**_


_**Two plaintext lengths.**_ _One of the plaintext lengths shall be a non-zero integer_
_multiple of 128 bits, if supported. The other plaintext length shall not be an integer_
_multiple of 128 bits, if supported._


_**Three AAD lengths.**_ _One AAD length shall be 0, if supported. One AAD length shall be_


_a non-zero integer multiple of 128 bits, if supported. One AAD length shall not be an_
_integer multiple of 128 bits, if supported._


_**Two IV lengths.**_ _If 96 bit IV is supported, 96 bits shall be one of the two IV lengths_
_tested._

_Test 12: The evaluator shall test the encrypt functionality using a set of 10 key, plaintext,_
_AAD, and IV tuples for each combination of parameter lengths above and obtain the_
_ciphertext value and tag that results from AES-GCM authenticated encrypt. Each supported_
_tag length shall be tested at least once per set of 10. The IV value may be supplied by the_
_evaluator or the implementation being tested, as long as it is known._


_Test 13: The evaluator shall test the decrypt functionality using a set of 10 key, ciphertext,_
_tag, AAD, and IV 5-tuples for each combination of parameter lengths above and obtain a_
_Pass/Fail result on authentication and the decrypted plaintext if Pass. The set shall include_
_five tuples that Pass and five that Fail._


_The results from each test may either be obtained by the evaluator directly or by supplying_
_the inputs to the implementer and receiving the results in response. To determine_
_correctness, the evaluator shall compare the resulting values to those obtained by_
_submitting the same inputs to a known good implementation._


_**XTS-AES Test**_
_Test 14: The evaluator shall test the encrypt functionality of XTS-AES for each combination_
_of the following input parameter lengths:_


_**256 bit (for AES-128) and 512 bit (for AES-256) keys**_


_**Three data unit (i.e. plaintext) lengths.**_ _One of the data unit lengths shall be a non-_
_zero integer multiple of 128 bits, if supported. One of the data unit lengths shall be an_
_integer multiple of 128 bits, if supported. The third data unit length shall be either the_
_longest supported data unit length or 216 bits, whichever is smaller._


_using a set of 100 (key, plaintext and 128-bit random tweak value) 3-tuples and obtain the_
_ciphertext that results from XTS-AES encrypt._


_The evaluator may supply a data unit sequence number instead of the tweak value if the_
_implementation supports it. The data unit sequence number is a base-10 number ranging_
_between 0 and 255 that implementations convert to a tweak value internally._


_Test 15: The evaluator shall test the decrypt functionality of XTS-AES using the same test as_
_for encrypt, replacing plaintext values with ciphertext values and XTS-AES encrypt with_
_XTS-AES decrypt._


_**AES Key Wrap (AES-KW) and Key Wrap with Padding (AES-KWP) Test**_
_Test 16: The evaluator shall test the authenticated encryption functionality of AES-KW for_
_EACH combination of the following input parameter lengths:_


_**128 and 256 bit key encryption keys (KEKs)**_


_**Three plaintext lengths.**_ _One of the plaintext lengths shall be two semi-blocks (128_
_bits). One of the plaintext lengths shall be three semi-blocks (192 bits). The third data_
_unit length shall be the longest supported plaintext length less than or equal to 64_
_semi-blocks (4096 bits)._

_using a set of 100 key and plaintext pairs and obtain the ciphertext that results from AES-_
_KW authenticated encryption. To determine correctness, the evaluator shall use the AES-_
_KW authenticated-encryption function of a known good implementation._


_Test 17: The evaluator shall test the authenticated-decryption functionality of AES-KW_
_using the same test as for authenticated-encryption, replacing plaintext values with_
_ciphertext values and AES-KW authenticated-encryption with AES-KW authenticated-_
_decryption._


_Test 18: The evaluator shall test the authenticated-encryption functionality of AES-KWP_
_using the same test as for AES-KW authenticated-encryption with the following change in_
_the three plaintext lengths:_

_One plaintext length shall be one octet. One plaintext length shall be 20 octets (160_
_bits)._


_One plaintext length shall be the longest supported plaintext length less than or equal_
_to 512 octets (4096 bits)._

_Test 19: The evaluator shall test the authenticated-decryption functionality of AES-KWP_
_using the same test as for AES-KWP authenticated-encryption, replacing plaintext values_
_with ciphertext values and AES-KWP authenticated-encryption with AES-KWP_
_authenticated-decryption._


**FCS_COP.1/HASH Cryptographic Operation**


FCS_COP.1.1/HASH

The TSF shall perform [ _cryptographic hashing_ ] in accordance with a specified
cryptographic algorithm [ _SHA-1 and [_ _**selection**_ _: SHA-256, SHA-384, SHA-512,_
_no other algorithms ]_ ] and **message digest** sizes [ _160 and [_ _**selection**_ _: 256 bits,_
_384 bits, 512 bits, no other message digest sizes ]_ ] that meet the following: [ _FIPS_
_Pub 180-4_ ].


**Application Note:** Per NIST SP 800-131A, SHA-1 for generating digital
signatures is no longer allowed, and SHA-1 for verification of digital signatures
is strongly discouraged as there may be risk in accepting these signatures. It is
expected that vendors will implement SHA-2 algorithms in accordance with SP
800-131A.


[SHA-1 is currently required in order to comply with the PP-Module for Wireless](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=386&id=386)
LAN Clients, version 1.0. Vendors are strongly encouraged to implement
updated protocols that support the SHA-2 family; until updated protocols are
supported, this PP allows support for SHA-1 implementations in compliance with
SP 800-131A.


The intent of this requirement is to specify the hashing function. The hash
selection must support the message digest size selection. The hash selection
should be consistent with the overall strength of the algorithm used (for
example, SHA 256 for 128-bit keys).


The TSF hashing functions can be implemented in one of two modes. The first
mode is the byte​oriented mode. In this mode the TSF only hashes messages that
are an integral number of bytes in length; i.e. the length (in bits) of the message
to be hashed is divisible by 8. The second mode is the bit​oriented mode. In this
mode the TSF hashes messages of arbitrary length. The TSF may implement
either bit-oriented or byte-oriented; both implementations are not required.


Validation Guidelines:


**Rule #2**


**Rule #3**


**Rule #4**


**Evaluation Activities**


_FCS_COP.1/HASH_
_**TSS**_
_The evaluator shall check that the association of the hash function with other TSF cryptographic_
_functions (for example, the digital signature verification function) is documented in the TSS. The_
_evaluator shall check that the TSS indicates if the hashing function is implemented in bit-_
_oriented or byte-oriented mode._


_**Guidance**_
_The evaluator checks the AGD documents to determine that any configuration that is required to_
_be done to configure the functionality for the required hash sizes is present._


_**Tests**_
_**Evaluation Activity Note:**_ _The following tests require the developer to provide access to a test_
_platform that provides the evaluator with tools that are typically not found on factory products._


_The evaluator shall perform all of the following tests for each hash algorithm implemented by_
_the TSF and used to satisfy the requirements of this PP. As there are different tests for each_
_mode, an indication is given in the following sections for the bit​oriented vs. the byte​oriented_
_tests._


_Test 20: Short Messages Test: Bit-oriented Mode_
_The evaluators devise an input set consisting of m+1 messages, where m is the block length_
_of the hash algorithm. The length of the messages ranges sequentially from 0 to m bits. The_
_message text shall be pseudorandomly generated. The evaluators compute the message_
_digest for each of the messages and ensure that the correct result is produced when the_
_messages are provided to the TSF._


_Test 21: Short Messages Test: Byte-oriented Mode_
_The evaluators devise an input set consisting of m/8+1 messages, where m is the block_
_length of the hash algorithm. The length of the messages range sequentially from 0 to m/8_
_bytes, with each message being an integral number of bytes. The message text shall be_
_pseudorandomly generated. The evaluators compute the message digest for each of the_


_messages and ensure that the correct result is produced when the messages are provided_
_to the TSF._


_Test 22: Selected Long Messages Test: Bit-oriented Mode_
_The evaluators devise an input set consisting of m messages, where m is the block length of_
_the hash algorithm. The length of the i_ _[th]_ _message is 512 + 99*i, where 1 ≤ i ≤ m. The_
_message text shall be pseudorandomly generated. The evaluators compute the message_
_digest for each of the messages and ensure that the correct result is produced when the_
_messages are provided to the TSF._


_Test 23: Selected Long Messages Test: Byte-oriented Mode_
_The evaluators devise an input set consisting of m/8 messages, where m is the block length_
_of the hash algorithm. The length of the i_ _[th]_ _message is 512 + 8*99*i, where 1 ≤ i ≤ m/8. The_
_message text shall be pseudorandomly generated. The evaluators compute the message_
_digest for each of the messages and ensure that the correct result is produced when the_
_messages are provided to the TSF._


_Test 24: Pseudorandomly Generated Messages Test: Byte-oriented Mode_
_This test is for byte​oriented implementations only. The evaluators randomly generate a seed_
_that is n bits long, where n is the length of the message digest produced by the hash_
_function to be tested. The evaluators then formulate a set of 100 messages and associated_
_digests by following the algorithm provided in Figure 1 of SHAVS. The evaluators then_
_ensure that the correct result is produced when the messages are provided to the TSF._


**FCS_COP.1/SIGN Cryptographic Operation**


FCS_COP.1.1/SIGN

The TSF shall perform [ _cryptographic signature services (generation and_
_verification)_ ] in accordance with a specified cryptographic algorithm [ **selection** :

_[RSA schemes]_ _**using**_ _cryptographic key sizes of [2048-bit or greater] that_
_meet the following: [FIPS PUB 186-4, "Digital Signature Standard (DSS)",_
_Section 4]_

_[ECDSA schemes]_ _**using**_ _["NIST curves" P-384 and [_ _**selection**_ _: P-256, P-_
_521, no other curves ]] that meet the following: [FIPS PUB 186-4, "Digital_
_Signature Standard (DSS)", Section 5]_

].


**Application Note:** The ST author should choose the algorithm implemented to
perform digital signatures; if more than one algorithm is available, this
requirement should be iterated to specify the functionality. For the algorithm
chosen, the ST author should make the appropriate assignments/selections to
specify the parameters that are implemented for that algorithm.


**Evaluation Activities**


_FCS_COP.1/SIGN_
_**TSS**_
_There are no TSS evaluation activities for this component._


_**Guidance**_
_There are no guidance evaluation activities for this component._


_**Tests**_
_**Evaluation Activity Note:**_ _The following tests require the developer to provide access to a test_
_platform that provides the evaluator with tools that are typically not found on factory products._


_Test 25:_ _**[conditional] If ECDSA schemes is selected in FCS_COP.1.1/SIGN**_

_Test 25.1:_ _**ECDSA FIPS 186-4 Signature Generation Test**_
_For each supported NIST curve (i.e. P-256, P-384 and P-521) and SHA function pair,_
_the evaluator shall generate 10 1024-bit long messages and obtain for each message a_
_public key and the resulting signature values R and S. To determine correctness, the_
_evaluator shall use the signature verification function of a known good_
_implementation._


_Test 25.2:_ _**ECDSA FIPS 186-4 Signature Verification Test**_
_For each supported NIST curve (i.e. P-256, P-384 and P-521) and SHA function pair,_
_the evaluator shall generate a set of 10 1024-bit message, public key and signature_
_tuples and modify one of the values (message, public key or signature) in five of the 10_
_tuples. The evaluator shall obtain in response a set of 10 PASS/FAIL values._

_Test 26:_ _**[conditional] If RSA schemes is selected in FCS_COP.1.1/SIGN**_

_Test 26.1:_ _**Signature Generation Test**_


_The evaluator shall verify the implementation of RSA Signature Generation by the TOE_
_using the Signature Generation Test. To conduct this test the evaluator must generate_
_or obtain 10 messages from a trusted reference implementation for each modulus_
_size/SHA combination supported by the TSF. The evaluator shall have the TOE use_
_their private key and modulus value to sign these messages._


_The evaluator shall verify the correctness of the TSF’s signature using a known good_
_implementation and the associated public keys to verify the signatures._


_Test 26.2:_ _**Signature Verification Test**_
_The evaluator shall perform the Signature Verification test to verify the ability of the_
_TOE to recognize another party’s valid and invalid signatures. The evaluator shall_
_inject errors into the test vectors produced during the Signature Verification Test by_
_introducing errors in some of the public keys e, messages, IR format, or signatures._
_The TOE attempts to verify the signatures and returns success or failure._


_The evaluator shall use these test vectors to emulate the signature verification test_
_using the corresponding parameters and verify that the TOE detects these errors._


**FCS_COP.1/KEYHMAC Cryptographic Operation**


FCS_COP.1.1/KEYHMAC

The TSF shall perform [ _keyed-hash message authentication_ ] in accordance with
a specified cryptographic algorithm [ _HMAC-SHA-1 and [_ _**selection**_ _: HMAC-SHA-_
_256, HMAC-SHA-384, HMAC-SHA-512, no other algorithms ]_ ] and cryptographic
key sizes [ **assignment** : _key size (in bits) used in HMAC_ ] **and message digest**
**sizes 160 and [selection:** _**256**_ **,** _**384**_ **,** _**512**_ **,** _**no other**_ **] bits** that meet the
following: [ _FIPS Pub 198-1, "The Keyed-Hash Message Authentication Code",_
_and FIPS Pub 180-4, "Secure Hash Standard"_ ].


**Application Note:** The selection in this requirement must be consistent with
the key size specified for the size of the keys used in conjunction with the keyedhash message authentication. HMAC-SHA-1 is currently required in order to
[comply with the PP-Module for Wireless LAN Clients, version 1.0.](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=386&id=386)


**Evaluation Activities**





**FCS_COP.1/CONDITION Cryptographic Operation**


FCS_COP.1.1/CONDITION

The TSF shall perform **conditioning** in accordance with a specified
cryptographic algorithm **HMAC-[selection:** _**SHA-256**_ **,** _**SHA-384**_ **,** _**SHA-512**_ **]**
**using a salt, and [selection:** _**PBKDF2 with [assignment: number of**_
_**iterations] iterations**_ **,** _**[assignment: key stretching function]**_ **,** _**no other**_
_**function**_ **] and output** cryptographic key sizes **[selection:** _**128**_ **,** _**256**_ **]** that
meet the following: **[selection:** _**NIST SP 800-132**_ **,** _**no standard**_ **].**


**Application Note:** The key cryptographic key sizes in the third selection should
be made to correspond to the KEK key sizes selected in FCS_CKM_EXT.3.


This password must be conditioned into a string of bits that forms the submask
to be used as input into the KEK. Conditioning can be performed using one of the
identified hash functions and may include a key stretching function; the method
used is selected by the ST author. If selected, NIST SP 800-132 requires the use
of a pseudorandom function (PRF) consisting of HMAC with an approved hash


function. The ST author selects the hash function used, also includes the
appropriate requirements for HMAC and the hash function.


Appendix A of NIST SP 800-132 recommends setting the iteration count in order
to increase the computation needed to derive a key from a password and,
therefore, increase the workload of performing a dictionary attack.


**Evaluation Activities**





**FCS_HTTPS_EXT.1 HTTPS Protocol**


FCS_HTTPS_EXT.1.1

The TSF shall implement the HTTPS protocol that complies with RFC 2818.


FCS_HTTPS_EXT.1.2

[The TSF shall implement HTTPS using TLS as defined in [](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=439&id=439) _the Functional_
_Package for Transport Layer Security (TLS), version 1.1_ ].


**Application Note:** The Functional Package for Transport Layer Security (TLS),
[version 1.1 must be included in the ST, with the following selections made:](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=439&id=439)

FCS_TLS_EXT.1:

TLS must be selected
Client must be selected


FCS_HTTPS_EXT.1.3

The TSF shall notify the application and [ **selection** : _not establish the connection_,
_request application authorization to establish the connection_, _no other action_ ] if
the peer certificate is deemed invalid.


**Application Note:** Validity is determined by the certificate path, the expiration
date, and the revocation status in accordance with RFC 5280.


If not establish the connection is selected then "with no exceptions" must be
[selected for FCS_TLSC_EXT.1.3 in the Functional Package for Transport Layer](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=439&id=439)
Security (TLS), version 1.1. If request application authorization to establish the
connection is selected then "except when override is authorized" must be
selected for FCS_TLSC_EXT.1.3 in the Package for Transport Layer Security. If
no other action is selected either selection can be made in FCS_TLSC_EXT.1.3.


FMT_SMF.1 Function 23 configures whether to allow or disallow the
establishment of a trusted channel if the peer certificate is deemed invalid.


Validation Guidelines:


**Rule #5**


**Rule #6**


**Evaluation Activities**


_FCS_HTTPS_EXT.1_
_**TSS**_
_There are no TSS evaluation activities for this component._


_**Guidance**_
_There are no guidance evaluation activities for this component._


_**Tests**_

_Test 27: The evaluator shall attempt to establish an HTTPS connection with a webserver,_
_observe the traffic with a packet analyzer, and verify that the connection succeeds and that_
_the traffic is identified as TLS or HTTPS._


_Other tests are performed in conjunction with testing in the Functional Package for_
_Transport Layer Security (TLS), version 1.1._


_Certificate validity shall be tested in accordance with testing performed for_
_FIA_X509_EXT.1, and the evaluator shall perform the following test:_


_Test 28: The evaluator shall demonstrate that using a certificate without a valid_
_certification path results in an application notification. Using the administrative guidance,_
_the evaluator shall then load a certificate or certificates to the Trust Anchor Database_
_needed to validate the certificate to be used in the function, and demonstrate that the_
_function succeeds. The evaluator then shall delete one of the certificates, and show that the_
_application is notified of the validation failure._


**FCS_IV_EXT.1 Initialization Vector Generation**


FCS_IV_EXT.1.1

The TSF shall generate IVs in accordance with [ _Table 11: References and IV_
_Requirements for NIST-approved Cipher Modes_ ].


**Application Note:** Table 11 lists the requirements for composition of IVs
according to the NIST Special Publications for each cipher mode. The
composition of IVs generated for encryption according to a cryptographic
protocol is addressed by the protocol. Thus, this requirement addresses only IVs
generated for key storage and data storage encryption.


**Evaluation Activities**


**FCS_RBG_EXT.1 Random Bit Generation**


FCS_RBG_EXT.1.1

The TSF shall perform all deterministic random bit generation services in
accordance with NIST Special Publication 800-90A using [ **selection** :
_Hash_DRBG (any)_, _HMAC_DRBG (any)_, _CTR_DRBG (AES)_ ].


FCS_RBG_EXT.1.2

The deterministic RBG shall be seeded by an entropy source that accumulates
entropy from [ **selection** : _a software-based noise source_, _TSF-hardware-based_
_noise source_ ] with a minimum of [ **selection** : _128 bits_, _256 bits_ ] of entropy at
least equal to the greatest security strength (according to NIST SP 800-57) of
the keys and hashes that it will generate.


FCS_RBG_EXT.1.3

The TSF shall be capable of providing output of the RBG to applications running
on the TSF that request random bits.


**Application Note:** SP 800-90A contains three different methods of generating
random numbers; each of these, in turn, depends on underlying cryptographic
primitives (hash functions/ciphers). The ST author will select the function used,
and include the specific underlying cryptographic primitives used in the
requirement or in the TSS. While any of the identified hash functions (SHA-224,
SHA-256, SHA-384, SHA-512) are allowed for Hash_DRBG or HMAC_DRBG, only


AES-based implementations for CTR_DRBG are allowed.


The ST author must also ensure that any underlying functions are included in the
baseline requirements for the TOE.


Health testing of the DRBGs is performed in conjunction with the self-tests
required in FPT_TST_EXT.1.1.


For the selection in FCS_RBG_EXT.1.2, the ST author selects the appropriate
number of bits of entropy that corresponds to the greatest security strength of
the algorithms included in the ST. Security strength is defined in Tables 2 and 3
of NIST SP 800-57A. For example, if the implementation includes 2048-bit RSA
(security strength of 112 bits), AES 128 (security strength 128 bits), and HMACSHA-256 (security strength 256 bits), then the ST author would select 256 bits.


The ST author may select either software or hardware noise sources. A hardware
noise source is a component that produces data that cannot be explained by a
deterministic rule, due to its physical nature. In other words, a hardware based
noise source generates sequences of random numbers from a physical process
that cannot be predicted. For example, a sampled ring oscillator consists of an
odd number of inverter gates chained into a loop, with an electrical pulse
traveling from inverter to inverter around the loop. The inverters are not
clocked, so the precise time required for a complete circuit around the loop
varies slightly as various physical effects modify the small delay time at each
inverter on the line to the next inverter. This variance results in an approximate
natural frequency that contains drift and jitter over time. The output of the ring
oscillator consists of the oscillating binary value sampled at a constant rate from
one of the inverters – a rate that is significantly slower than the oscillator’s
natural frequency.


**Evaluation Activities**


_FCS_RBG_EXT.1_
_**Entropy Documentation**_
_Documentation shall be produced and the evaluator shall perform the activities in accordance_
_with Appendix F - Entropy Documentation And Assessment, the "Clarification to the Entropy_
_Documentation and Assessment"._


_**API Documentation**_
_The evaluator shall verify that the API documentation provided according to Section 5.2.2 Class_
_ADV: Development, includes the security functions described in FCS_RBG_EXT.1.3._


_**TSS**_
_There are no TSS evaluation activities for this component._


_**Guidance**_
_The evaluator shall also confirm that the operational guidance contains appropriate instructions_
_for configuring the RNG functionality._


_**Tests**_
_**Evaluation Activity Note:**_ _The following tests require the developer to provide access to a test_
_platform that provides the evaluator with tools that are typically not found on factory products._


_The evaluator shall perform 15 trials for the RNG implementation. If the RNG is configurable,_
_the evaluator shall perform 15 trials for each configuration._


_If the RNG has prediction resistance enabled, each trial consists of (1) instantiate DRBG, (2)_
_generate the first block of random bits (3) generate a second block of random bits (4)_
_uninstantiate. The evaluator verifies that the second block of random bits is the expected value._
_The evaluator shall generate eight input values for each trial. The first is a count (0 – 14). The_
_next three are entropy input, nonce, and personalization string for the instantiate operation. The_
_next two are additional input and entropy input for the first call to generate. The final two are_
_additional input and entropy input for the second call to generate. These values are randomly_
_generated. "generate one block of random bits" means to generate random bits with number of_
_returned bits equal to the Output Block Length (as defined in NIST SP800-90A)._


_If the RNG does not have prediction resistance, each trial consists of (1) instantiate DRBG, (2)_
_generate the first block of random bits (3) reseed, (4) generate a second block of random bits (5)_
_uninstantiate. The evaluator verifies that the second block of random bits is the expected value._
_The evaluator shall generate eight input values for each trial. The first is a count (0 – 14). The_
_next three are entropy input, nonce, and personalization string for the instantiate operation. The_
_fifth value is additional input to the first call to generate. The sixth and seventh are additional_
_input and entropy input to the call to reseed. The final value is additional input to the second_
_generate call._


_The following paragraphs contain more information on some of the input values to be_


_generated/selected by the evaluator._


_**Entropy input:**_ _the length of the entropy input value must equal the seed length._
_**Nonce:**_ _If a nonce is supported (CTR_DRBG with no Derivation Function does not use a_
_nonce), the nonce bit length is one-half the seed length._
_**Personalization string:**_ _The length of the personalization string must be � seed length. If_
_the implementation only supports one personalization string length, then the same length_
_can be used for both values. If more than one string length is support, the evaluator shall_
_use personalization strings of two different lengths. If the implementation does not use a_
_personalization string, no value needs to be supplied._
_**Additional input:**_ _the additional input bit lengths have the same defaults and restrictions_
_as the personalization string lengths._


**FCS_SRV_EXT.1 Cryptographic Algorithm Services**


FCS_SRV_EXT.1.1

The TSF shall provide a mechanism for applications to request the TSF to
perform the following cryptographic operations: [

_All mandatory and [_ _**selection**_ _: selected algorithms, selected algorithms_
_with the exception of ECC over curve 25519-based algorithms ] in_
_FCS_CKM.2/LOCKED_
_The following algorithms in FCS_COP.1/ENCRYPT: AES-CBC, [_ _**selection**_ _:_
_AES Key Wrap, AES Key Wrap with Padding, AES-GCM, AES-CCM, no other_
_modes ]_
_All selected algorithms in FCS_COP.1/SIGN_
_All mandatory and selected algorithms in FCS_COP.1/HASH_
_All mandatory and selected algorithms in FCS_COP.1/KEYHMAC_

_[_ _**selection**_ _:_

_All mandatory and [_ _**selection**_ _: selected algorithms, selected algorithms_
_with the exception of ECC over curve 25519-based algorithms ] in_
_FCS_CKM.1_
_The selected algorithms in FCS_COP.1_ _**/CONDITION**_
_No other cryptographic operations_

_]_

].


**Application Note:** For each of the listed FCS components in the bulleted list,
the intent is that the TOE will make available all algorithms specified for that
component in the ST. For example, if for FCS_COP.1/HASH the ST author selects
SHA-256, then the TOE would have to make available an interface to perform
SHA-1 (the "mandatory" portion of FCS_COP.1/HASH) and SHA-256 (the
"selected" portion of FCS_COP.1/HASH).


The exception is for FCS_COP.1/ENCRYPT. The TOE is not required to make
available AES_CCMP, AES_XTS, AES_GCMP-256, or AES_CCMP_256 even
though they may be implemented to perform TSF-related functions. It is
acceptable for the platform to not provide AES Key Wrap (KW) and AES Key
Wrap with Padding (KWP) to applications even if selected in
FCS_COP.1/ENCRYPT. However, the ST author is expected to select AES-GCM
or AES-CCM if it is selected in the ST for the FCS_COP.1/ENCRYPT component.


**Evaluation Activities**




**5.1.4 Cryptographic Storage (FCS_STG_EXT)**

The following requirements describe how keys are protected. All keys must ultimately be protected by a REK,
and may optionally be protected by the user’s authentication factor. Each key’s confidentiality and integrity
must be protected. This section also describes the secure key storage services to be provided by the Mobile
Device for use by applications and users, applying the same level of protection for these keys as keys internal
to the OS.


**FCS_STG_EXT.1 Cryptographic Key Storage**


FCS_STG_EXT.1.1

The TSF shall provide [ **selection** : _mutable hardware_, _software-based_ ] secure
key storage for asymmetric private keys and [ **selection** : _symmetric keys_,
_persistent secrets_, _no other keys_ ].


**Application Note:** A hardware keystore can be exposed to the TSF through a
variety of interfaces, including embedded on the motherboard, USB, microSD,
and Bluetooth.


Immutable hardware is considered outside of this requirement and will be
covered elsewhere.


If the secure key storage is implemented in software that is protected as
required by FCS_STG_EXT.2, the ST author must select software-based. If
software-based is selected, the ST author must select all software-based key
storage in FCS_STG_EXT.2.1.


Support for secure key storage for all symmetric keys and persistent secrets will
be required in future revisions.


Validation Guidelines:


**Rule #7**


FCS_STG_EXT.1.2

The TSF shall be capable of importing keys or secrets into the secure key
storage upon request of [ **selection** : _the user_, _the administrator_ ] and [ **selection** :
_applications running on the TSF_, _no other subjects_ ].


**Application Note:** If the ST selects only the user, the ST author must select
function 9 in FMT_MOF_EXT.1.1.


FCS_STG_EXT.1.3

The TSF shall be capable of destroying keys or secrets in the secure key storage
upon request of [ **selection** : _the user_, _the administrator_ ].


**Application Note:** If the ST selects the user, the ST author must select function
10 in FMT_MOF_EXT.1.1.


FCS_STG_EXT.1.4

The TSF shall have the capability to allow only the application that imported the
key or secret the use of the key or secret. Exceptions may only be explicitly
authorized by [ **selection** : _the user_, _the administrator_, _a common application_
_developer_ ].


**Application Note:** If the ST selects the user or the administrator, the ST author
must also select 34 in FMT_SMF.1.1. If the ST selects the user, the ST author
must select function 34 in FMT_MOF_EXT.1.1.


FCS_STG_EXT.1.5

The TSF shall allow only the application that imported the key or secret to
request that the key or secret be destroyed. Exceptions may only be explicitly
authorized by [ **selection** : _the user_, _the administrator_, _a common application_
_developer_ ].


**Application Note:** If the ST selects the user or the administrator, the ST author
must also select function 35 in FMT_SMF.1.1. If the ST selects only the user, the
ST author must select function 35 in FMT_MOF_EXT.1.1.


Validation Guidelines:


**Rule #10**


**Rule #11**


**Evaluation Activities**


_FCS_STG_EXT.1_
_The evaluator shall verify that the API documentation provided according to Section 5.2.2 Class_
_ADV: Development includes the security functions (import, use, and destruction) described in_
_these requirements. The API documentation shall include the method by which applications_


_restrict access to their keys or secrets in order to meet FCS_STG_EXT.1.4._


_**TSS**_
_The evaluator shall review the TSS to determine that the TOE implements the required secure_
_key storage. The evaluator shall ensure that the TSS contains a description of the key storage_
_mechanism that justifies the selection of "mutable hardware" or "software-based"._


_**Guidance**_
_The evaluator shall review the AGD guidance to determine that it describes the steps needed to_
_import or destroy keys or secrets._


_**Tests**_
_The evaluator shall test the functionality of each security function:_

_Test 29: The evaluator shall import keys or secrets of each supported type according to the_
_AGD guidance. The evaluator shall write, or the developer shall provide access to, an_
_application that generates a key or secret of each supported type and calls the import_
_functions. The evaluator shall verify that no errors occur during import._


_Test 30: The evaluator shall write, or the developer shall provide access to, an application_
_that uses an imported key or secret:_

_For RSA, the secret shall be used to sign data._
_For ECDSA, the secret shall be used to sign data_


_In the future additional types will be required to be tested:_

_For symmetric algorithms, the secret shall be used to encrypt data._
_For persistent secrets, the secret shall be compared to the imported secret._


_The evaluator shall repeat this test with the application-imported keys or secrets and a_
_different application’s imported keys or secrets. The evaluator shall verify that the TOE_
_requires approval before allowing the application to use the key or secret imported by the_
_user or by a different application:_

_The evaluator shall deny the approvals to verify that the application is not able to use_
_the key or secret as described._
_The evaluator shall repeat the test, allowing the approvals to verify that the application_
_is able to use the key or secret as described._


_If the ST author has selected "common application developer", this test is performed by_
_either using applications from different developers or appropriately (according to API_
_documentation) not authorizing sharing._


_Test 31: The evaluator shall destroy keys or secrets of each supported type according to the_
_AGD guidance. The evaluator shall write, or the developer shall provide access to, an_
_application that destroys an imported key or secret._


_The evaluator shall repeat this test with the application-imported keys or secrets and a_
_different application’s imported keys or secrets. The evaluator shall verify that the TOE_
_requires approval before allowing the application to destroy the key or secret imported by_
_the administrator or by a different application:_


_The evaluator shall deny the approvals and verify that the application is still able to_
_use the key or secret as described._
_The evaluator shall repeat the test, allowing the approvals and verifying that the_
_application is no longer able to use the key or secret as described._


_If the ST author has selected "common application developer", this test is performed by_
_either using applications from different developers or appropriately (according to API_
_documentation) not authorizing sharing._


**FCS_STG_EXT.2 Encrypted Cryptographic Key Storage**


FCS_STG_EXT.2.1

The TSF shall encrypt all DEKs, KEKs, [ **assignment** : _any long-term trusted_
_channel key material_ ] and [ **selection** : _all software-based key storage_, _no other_
_keys_ ] by KEKs that are [ **selection** :

_Protected by the REK with [_ _**selection**_ _:_

_encryption by a REK_
_encryption by a KEK chaining from a REK_
_encryption by a KEK that is derived from a REK_

_]_
_Protected by the REK and the password with [_ _**selection**_ _:_

_encryption by a REK and the password-derived KEK_


FCS_STG_EXT.2.2



_encryption by a KEK chaining to a REK and the password-derived or_
_biometric-unlocked KEK_
_encryption by a KEK that is derived from a REK and the password-_
_derived or biometric-unlocked KEK_

_]_

].


**Application Note:** The ST author must select all software-based key storage if
software-based is selected in FCS_STG_EXT.1.1. If the ST author selects mutable
hardware in FCS_STG_EXT.1.1, the secure key storage is not subject to this
requirement. REKs are not subject to this requirement.


A REK and the password-derived KEK may be combined to form a combined KEK
(as described in FCS_CKM_EXT.3) in order to meet this requirement.


Software-based key storage must be protected by the password or biometric and
REK.


All keys must ultimately be protected by a REK. In particular, Figure 3 has KEKs
protected according to these requirements: DEK_1 meets the "encryption by a
REK and the password-derived KEK" case and would be appropriate for sensitive
data, DEK_2 meets the "encryption by a KEK chaining from a REK" case and
would not be appropriate for sensitive data, K_1 meets the "encryption by a
REK" case and is not considered a sensitive key, and K_2 meets the "encryption
by a KEK chaining to a REK and the password-derived or biometric-unlocked
KEK" case and is considered a sensitive key.


Long-term trusted channel key material includes Wi-Fi (PSKs), IPsec (PSKs and
client certificates) and Bluetooth keys. These keys must not be protected by the
password, as they may be necessary in the locked state. For clarity, the ST
author must assign any Long-term trusted channel key material supported by the
TOE . At a minimum, a TOE must support at least Wi-Fi and Bluetooth keys.


Validation Guidelines:


**Rule #7**


DEKs, KEKs, [ **assignment** : _any long-term trusted channel key material_ ] and

[ **selection** : _all software-based key storage_, _no other keys_ ] shall be encrypted
using one of the following methods: [ **selection** :

_using a SP800-56B key establishment scheme_
_using AES in the [_ _**selection**_ _: Key Wrap (KW) mode, Key Wrap with Padding_
_(KWP) mode, GCM, CCM, CBC mode ]_

].


**Application Note:** The ST author selects which key encryption schemes are
used by the TOE. This requirement refers only to KEKs as defined this PP and
does not refer to those KEKs specified in other standards. The ST author must
assign the same Long-term trusted channel key material assigned in
FCS_STG_EXT.2.1.



**Evaluation Activities**


_FCS_STG_EXT.2_
_**TSS**_
_The evaluator shall review the TSS to determine that the TSS includes key hierarchy description_
_of the protection of each DEK for data-at-rest, of software-based key storage, of long-term_
_trusted channel keys, and of KEK related to the protection of the DEKs, long-term trusted_
_channel keys, and software-based key storage. This description must include a diagram_
_illustrating the key hierarchy implemented by the TOE in order to demonstrate that the_
_implementation meets FCS_STG_EXT.2. The description shall indicate how the functionality_
_described by FCS_RBG_EXT.1 is invoked to generate DEKs (FCS_CKM_EXT.2), the key size_
_(FCS_CKM_EXT.2 and FCS_CKM_EXT.3) for each key, how each KEK is formed (generated,_
_derived, or combined according to FCS_CKM_EXT.3), the integrity protection method for each_
_encrypted key (FCS_STG_EXT.3), and the IV generation for each key encrypted by the same KEK_
_(FCS_IV_EXT.1). More detail for each task follows the corresponding requirement._


_The evaluator shall also ensure that the documentation of the product's encryption key_
_management is detailed enough that, after reading, the product's key management hierarchy is_
_clear and that it meets the requirements to ensure the keys are adequately protected. The_
_evaluator shall ensure that the documentation includes both an essay and one or more diagrams._
_Note that this may also be documented as separate proprietary evidence rather than being_
_included in the TSS._


_The evaluator shall examine the key hierarchy description in the TSS section to verify that each_


_DEK and software-stored key is encrypted according to FCS_STG_EXT.2._


_**Guidance**_
_There are no guidance evaluation activities for this component._


_**Tests**_
_There are no test evaluation activities for this component._


**FCS_STG_EXT.3 Integrity of Encrypted Key Storage**


FCS_STG_EXT.3.1

The TSF shall protect the integrity of any encrypted DEKs and KEKs and

[ **selection** : _long-term trusted channel key material_, _all software-based key_
_storage_, _no other keys_ ] by [ **selection** :

_[_ _**selection**_ _: GCM, CCM, Key Wrap, Key Wrap with Padding ] cipher mode_
_for encryption according to FCS_STG_EXT.2_
_a hash (FCS_COP.1_ _**/HASH**_ _) of the stored key that is encrypted by a key_
_protected by FCS_STG_EXT.2_
_a keyed hash (FCS_COP.1_ _**/KEYHMAC**_ _) using a key protected by a key_
_protected by FCS_STG_EXT.2_
_a digital signature of the stored key using an asymmetric key protected_
_according to FCS_STG_EXT.2_
_an immediate application of the key for decrypting the protected data_
_followed by a successful verification of the decrypted data with previously_
_known information_

].


**Application Note:** The ST author must assign the same Long-term trusted
channel key material assigned in FCS_STG_EXT.2.1.


FCS_STG_EXT.3.2

The TSF shall verify the integrity of the [ **selection** : _hash_, _digital signature_, _MAC_
] of the stored key prior to use of the key.


**Application Note:** This requirement is not applicable to derived keys that are
not stored. It is not expected that a single key will be protected from corruption
by multiple of these methods; however, a product may use one integrityprotection method for one type of key and a different method for other types of
keys. The explicit Evaluation Activities for each of the options will be addressed
in each of the requirements (FCS_COP.1.1/HASH, FCS_COP.1.1/KEYHMAC).


Key Wrapping must be implemented per SP800-38F.


**Evaluation Activities**



**5.1.5 Class: User Data Protection (FDP)**
A subset of the User Data Protection focuses on protecting Data-At-Rest, namely FDP_DAR_EXT.1 and
FDP_DAR_EXT.2. Three levels of data-at-rest protection are addressed: TSF data, Protected Data (and keys),
and sensitive data. Table 6 addresses the level of protection required for each level of data-at-rest.


**Table 6: Protection of Data Levels**


**Data Level** **Protection Required**



Protected
Data


Sensitive
Data



Protected data is encrypted while powered off. (FDP_DAR_EXT.1)


Sensitive data is encrypted while in the locked state, in addition to while powered off.
(FDP_DAR_EXT.2)



All keys, protected data, and sensitive data must ultimately be protected by the REK. Sensitive data must be
protected by the password in addition to the REK. In particular, Figure 3 has KEKs protected according to
these requirements: DEK_1 would be appropriate for sensitive data, DEK_2 would not be appropriate for
sensitive data, K_1 is not considered a sensitive key, and K_2 is considered a sensitive key.


These requirements include a capability for encrypting sensitive data received while in the locked state,
which may be considered a separate sub-category of sensitive data. This capability may be met by a key
transport scheme (RSA) by using a public key to encrypt the DEK while protecting the corresponding private
key with a password-derived or biometric-unlocked KEK.


This capability may also be met by a key agreement scheme. To do so, the device generates a device-wide
sensitive data asymmetric pair (the private key of which is protected by a password-derived or biometricunlocked KEK) and an asymmetric pair for the received sensitive data to be stored. In order to store the
sensitive data, the device-wide public key and data private key are used to generate a shared secret, which
can be used as a KEK or a DEK. The data private key and shared secret are cleared after the data is encrypted
and the data public key stored. Thus, no key material is available in the locked state to decrypt the newly
stored data. Upon unlock, the device-wide private key is decrypted and is used with each data public key to
regenerate the shared secret and decrypt the stored data. Figure 4, below, illustrates this scheme.


**Figure 4: Key Agreement Scheme for Encrypting Received Sensitive Data in the Locked State**


**FDP_ACF_EXT.1 Access Control for System Services**


FDP_ACF_EXT.1.1

The TSF shall provide a mechanism to restrict the system services that are
accessible to an application.


**Application Note:** Examples of system services to which this requirement
applies include:


Obtain data from camera and microphone input devices
Obtain current device location
Retrieve credentials from system-wide credential store
Retrieve contacts list / address book


FDP_ACF_EXT.1.2



Retrieve stored pictures
Retrieve text messages
Retrieve emails
Retrieve device identifier information
Obtain network access


The TSF shall provide an access control policy that prevents [ **selection** :
_application_, _groups of applications_ ] from accessing [ **selection** : _all_, _private_ ] data
stored by other [ **selection** : _application_, _groups of applications_ ]. Exceptions may
only be explicitly authorized for such sharing by [ **selection** : _the user_, _the_
_administrator_, _a common application developer_, _no one_ ].


**Application Note:** Application groups may be designated Enterprise or
Personal. Applications installed by the user default to being in the Personal
application group unless otherwise designated by the administrator in function
43 of FMT_SMF.1.1. Applications installed by the administrator default to being
in the Enterprise application group (this category includes applications that the
user requests the administrator install, for instance by selecting the application
for installation through an enterprise application catalog) unless otherwise
designated by the administrator in function 43 of FMT_SMF.1.1. It is acceptable
for the same application to have multiple instances installed, each in different
application groups. Private data is defined as data that is accessible only by the
application that wrote it. Private data is distinguished from data that an
application may, by design, write to shared storage areas.


If groups of applications is selected, FDP_ACF_EXT.2 must be included in the ST.



**Evaluation Activities**


_FDP_ACF_EXT.1.1_
_**TSS**_
_The evaluator shall ensure the TSS lists all system services available for use by an application._
_The evaluator shall also ensure that the TSS describes how applications interface with these_
_system services, and means by which these system services are protected by the TSF._


_The TSS shall describe which of the following categories each system service falls in:_


_1. No applications are allowed access_
_2. Privileged applications are allowed access_
_3. Applications are allowed access by user authorization_
_4. All applications are allowed access_


_Privileged applications include any applications developed by the TSF developer. The TSS shall_
_describe how privileges are granted to third-party applications. For both types of privileged_
_applications, the TSS shall describe how and when the privileges are verified and how the TSF_
_prevents unprivileged applications from accessing those services._


_For any services for which the user may grant access, the evaluator shall ensure that the TSS_
_identifies whether the user is prompted for authorization when the application is installed, or_
_during runtime. The evaluator shall ensure that the operational user guidance contains_
_instructions for restricting application access to system services._


_**Guidance**_
_There are no guidance evaluation activities for this element._


_**Tests**_
_**Evaluation Activity Note:**_ _The following tests require the vendor to provide access to a test_
_platform that provides the evaluator with tools that are typically not found on consumer Mobile_
_Device products._


_The evaluator shall write, or the developer shall provide, applications for the purposes of the_
_following tests._


_Test 32: For each system service to which no applications are allowed access, the evaluator_
_shall attempt to access the system service with a test application and verify that the_
_application is not able to access that system service._
_Test 33: For each system service to which only privileged applications are allowed access,_
_the evaluator shall attempt to access the system service with an unprivileged application_
_and verify that the application is not able to access that system service. The evaluator shall_
_attempt to access the system service with a privileged application and verify that the_
_application can access the service._
_Test 34: For each system service to which the user may grant access, the evaluator shall_
_attempt to access the system service with a test application. The evaluator shall ensure that_
_either the system blocks such accesses or prompts for user authorization. The prompt for_
_user authorization may occur at runtime or at installation time, and should be consistent_


_with the behavior described in the TSS._
_Test 35: For each system service listed in the TSS that is accessible by all applications, the_
_evaluator shall test that an application can access that system service._


_FDP_ACF_EXT.1.2_
_**TSS**_
_The evaluator shall examine the TSS to verify that it describes which data sharing is permitted_
_between applications, which data sharing is not permitted, and how disallowed sharing is_
_prevented. It is possible to select both "applications" and "groups of applications", in which case_
_the TSS is expected to describe the data sharing policies that would be applied in each case._


_**Guidance**_
_There are no guidance evaluation activities for this element._


_**Tests**_

_Test 36: The evaluator shall write, or the developer shall provide, two applications, one that_
_saves data containing a unique string and the other, which attempts to access that data. If_
_groups of applications is selected, the applications shall be placed into different groups. If_
_application is selected, the evaluator shall install the two applications. If private is selected,_
_the application shall not write to a designated shared storage area. The evaluator shall_
_verify that the second application is unable to access the stored unique string._


_If the user is selected, the evaluator shall grant access as the user and verify that the_
_second application is able to access the stored unique string._


_If the administrator is selected, the evaluator shall grant access as the administrator and_
_verify that the second application is able to access the stored unique string._


_If a common application developer is selected, the evaluator shall grant access to an,_
_application with a common application developer to the first, and verify that the application_
_is able to access the stored unique string._


**FDP_DAR_EXT.1 Protected Data Encryption**


FDP_DAR_EXT.1.1

Encryption shall cover all protected data.


**Application Note:** Protected data is all non-TSF data, including all user or
enterprise data. Some or all of this data may be considered sensitive data as
well.


FDP_DAR_EXT.1.2

Encryption shall be performed using DEKs with AES in the [ **selection** : _XTS_,
_CBC_, _GCM_ ] mode with key size [ **selection** : _128_, _256_ ] bits.


**Application Note:** IVs must be generated in accordance with FCS_IV_EXT.1.1.


**Evaluation Activities**


**FDP_DAR_EXT.2 Sensitive Data Encryption**


FDP_DAR_EXT.2.1

The TSF shall provide a mechanism for applications to mark data and keys as
sensitive.


**Application Note:** Data and keys that have been marked as sensitive will be
subject to certain restrictions (through other requirements) in both the locked
and unlocked states of the Mobile Device. This mechanism allows an application
to choose those data and keys under its control to be subject to those
requirements.


In the future, this PP may require that all data and key created by applications
will default to the "sensitive" marking, requiring an explicit "non-sensitive"
marking rather than an explicit "sensitive" marking.


FDP_DAR_EXT.2.2

The TSF shall use an asymmetric key scheme to encrypt and store sensitive data
received while the product is locked.


**Application Note:** Sensitive data is encrypted according to FDP_DAR_EXT.1.2.
The asymmetric key scheme must be performed in accordance with
FCS_CKM.2/LOCKED.


The intent of this requirement is to allow the device to receive sensitive data
while locked and to store the received data in such a way as to prevent
unauthorized parties from decrypting it while in the locked state. If only a subset
of sensitive data may be received in the locked state, this subset must be
described in the TSS.


Key material must be cleared when no longer needed according to
FCS_CKM_EXT.4. For keys (or key material used to derive those keys) protecting
sensitive data received in the locked state, "no longer needed" includes "while in
the locked state." For example, in the first key scheme, this includes the DEK
protecting the received data as soon as the data is encrypted. In the second key
scheme this includes the private key for the data asymmetric pair, the generated
shared secret, and any generated DEKs. Of course, both schemes require that a
private key of an asymmetric pair (the RSA private key and the device-wide
private key, respectively) be cleared when transitioning to the locked state.


FDP_DAR_EXT.2.3

The TSF shall encrypt any stored symmetric key and any stored private key of
the asymmetric keys used for the protection of sensitive data according to

[ _FCS_STG_EXT.2.1 selection 2_ ].


**Application Note:** Symmetric keys used to encrypt sensitive data while the TSF
is in the unlocked state must be encrypted with (or chain to a KEK encrypted
with) the REK and password-derived or biometric-unlocked KEK. A stored
private key of the asymmetric key scheme for encrypting data in the locked state
must be encrypted with (or chain to a KEK encrypted with) the REK and
password-derived or biometric-unlocked KEK.


FDP_DAR_EXT.2.4

The TSF shall decrypt the sensitive data that was received while in the locked
state upon transitioning to the unlocked state using the asymmetric key scheme
and shall re-encrypt that sensitive data using the symmetric key scheme.


**Evaluation Activities**


_FDP_DAR_EXT.2.1_
_**TSS**_
_The evaluator shall verify that the TSS includes a description of which data stored by the TSF_
_(such as by native applications) is treated as sensitive. This data may include all or some user or_
_enterprise data and must be specific regarding the level of protection of email, contacts,_
_calendar appointments, messages, and documents._


_The evaluator shall examine the TSS to determine that it describes the mechanism that is_
_provided for applications to use to mark data and keys as sensitive. This description shall also_
_contain information reflecting how data and keys marked in this manner are distinguished from_
_data and keys that are not (for instance, tagging, segregation in a "special" area of memory or_
_container, etc.)._


_**Guidance**_
_There are no guidance evaluation activities for this element._


_**Tests**_
_The evaluator shall enable encryption of sensitive data and require user authentication_
_according to the AGD guidance. The evaluator shall try to access and create sensitive data (as_
_defined in the ST and either by creating a file or using an application to generate sensitive data)_
_in order to verify that no other user interaction is required._


_FDP_DAR_EXT.2.2_
_**TSS**_
_The evaluator shall review the TSS section of the ST to determine that the TSS includes a_
_description of the process of receiving sensitive data while the device is in a locked state. The_
_evaluator shall also verify that the description indicates if sensitive data that may be received in_
_the locked state is treated differently than sensitive data that cannot be received in the locked_
_state. The description shall include the key scheme for encrypting and storing the received data,_
_which must involve an asymmetric key and must prevent the sensitive data-at-rest from being_
_decrypted by wiping all key material used to derive or encrypt the data (as described in the_
_application note). The introduction to this section provides two different schemes that meet the_
_requirements, but other solutions may address this requirement._


_**Guidance**_
_There are no guidance evaluation activities for this element._


_**Tests**_
_The evaluator shall perform the tests in FCS_CKM_EXT.4 for all key material no longer needed_
_while in the locked state and shall ensure that keys for the asymmetric scheme are addressed in_
_the tests performed when transitioning to the locked state._


_FDP_DAR_EXT.2.3_
_**TSS**_
_The evaluator shall verify that the key hierarchy section of the TSS required for_
_FCS_STG_EXT.2.1 includes the symmetric encryption keys (DEKs) used to encrypt sensitive_
_data. The evaluator shall ensure that these DEKs are encrypted by a key encrypted with (or_
_chain to a KEK encrypted with) the REK and password-derived or biometric-unlocked KEK._


_The evaluator shall verify that the TSS section of the ST that describes the asymmetric key_
_scheme includes the protection of any private keys of the asymmetric pairs. The evaluator shall_
_ensure that any private keys that are not wiped and are stored by the TSF are stored encrypted_
_by a key encrypted with (or chain to a KEK encrypted with) the REK and password-derived or_
_biometric-unlocked KEK._


_The evaluator shall also ensure that the documentation of the product's encryption key_
_management is detailed enough that, after reading, the product's key management hierarchy is_
_clear and that it meets the requirements to ensure the keys are adequately protected. The_
_evaluator shall ensure that the documentation includes both an essay and one or more diagrams._
_Note that this may also be documented as separate proprietary evidence rather than being_
_included in the TSS._


_**Guidance**_
_There are no guidance evaluation activities for this element._


_**Tests**_
_There are no test evaluation activities for this element._


_FDP_DAR_EXT.2.4_
_**TSS**_
_The evaluator shall verify that the TSS section of the ST that describes the asymmetric key_
_scheme includes a description of the actions taken by the TSF for the purposes of DAR upon_
_transitioning to the unlocked state. These actions shall minimally include decrypting all received_
_data using the asymmetric key scheme and re-encrypting with the symmetric key scheme used_
_to store data while the device is unlocked._


_**Guidance**_
_There are no guidance evaluation activities for this element._


_**Tests**_
_There are no test evaluation activities for this element._


**FDP_IFC_EXT.1 Subset Information Flow Control**


FDP_IFC_EXT.1.1

The TSF shall [ **selection** :


_provide an interface which allows a VPN client to protect all IP traffic using_
_IPsec_
_provide a VPN client which can protect all IP traffic using IPsec_ _**as defined**_
_**in the PP-Module for Virtual Private Network (VPN) Clients, version**_
_**2.4**_

] with the exception of IP traffic needed to manage the VPN connection, and

[ **selection** : _[_ _**assignment**_ _: traffic needed for correct functioning of the TOE]_, _no_
_other traffic_ ], when the VPN is enabled.


**Application Note:** Typically, the traffic needed to manage the VPN connection
is referred to as "Control Plane" traffic; whereas, the IP traffic protected by the
IPsec VPN is referred to as "Data Plane" traffic. All "Data Plane" traffic must flow
through the VPN connection and the VPN must not split-tunnel. “IP traffic
needed for correct functioning of the TOE” comprises traffic that would prevent
the TOE from proper operation if it was either blocked by or routed through the
VPN. Enabling the VPN means that the VPN client has been activated by the
user. If the VPN tunnel gets interrupted, then no “Data Plane” traffic should be
sent without the VPN tunnel being re-established or the user disabling the VPN
client.


If no native IPsec client is validated or third-party VPN clients may also
implement the required Information Flow Control, the first option must be
selected. In these cases, the TOE provides an API to third-party VPN clients that
allow them to configure the TOE’s network stack to perform the required
Information Flow Control.


The ST author must select the second option if the TSF implements a native VPN
client (IPsec is selected in FTP_ITC_EXT.1.1). Thus the TSF must be validated
[against the PP-Module for Virtual Private Network (VPN) Clients, version 2.4 and](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=467&id=467)
[the ST author must also include FDP_IFC_EXT.1 from the PP-Module for Virtual](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=467&id=467)
Private Network (VPN) Clients, version 2.4.


It is optional for the VPN client to be configured to be always-on per FMT_SMF.1
Function 45. Always-on means the establishment of an IPsec trusted channel to
allow any communication by the TSF.


**Evaluation Activities**


_FDP_IFC_EXT.1_
_**TSS**_
_The evaluator shall verify that the TSS section of the ST describes the routing of IP traffic_
_through processes on the TSF when a VPN client is enabled. The evaluator shall ensure that the_
_description indicates which traffic does not go through the VPN and which traffic does. The_
_evaluator shall verify that a configuration exists for each baseband protocol in which only the_
_traffic identified by the ST author as necessary for establishing the VPN connection (IKE traffic_
_and perhaps HTTPS or DNS traffic) or needed for the correct functioning of the TOE is not_
_encapsulated by the VPN protocol (IPsec). The evaluator shall verify that the TSS section_
_describes any differences in the routing of IP traffic when using any supported baseband_
_protocols (e.g. Wi-Fi or, LTE)._


_**Guidance**_
_The evaluator shall verify that one (or more) of the following options is addressed by the_
_documentation:_

_The description above indicates that if a VPN client is enabled, all configurations route all_
_Data Plane traffic through the tunnel interface established by the VPN client._
_The AGD guidance describes how the user or administrator can configure the TSF to meet_
_this requirement._
_The API documentation includes a security function that allows a VPN client to specify this_
_routing._


_**Tests**_

_Test 38: If the ST author identifies any differences in the routing between Wi-Fi and cellular_
_protocols, the evaluator shall repeat this test with a base station implementing one of the_
_identified cellular protocols._


_Step 1: The evaluator shall enable a Wi-Fi configuration as described in the AGD guidance_
_(as required by FTP_ITC_EXT.1). The evaluator shall use a packet sniffing tool between the_
_wireless access point and an Internet-connected network. The evaluator shall turn on the_
_sniffing tool and perform actions with the device such as navigating to websites, using_
_provided applications, and accessing other Internet resources. The evaluator shall verify_
_that the sniffing tool captures the traffic generated by these actions, turn off the sniffing_
_tool, and save the session data._


_Step 2: The evaluator shall configure an IPsec VPN client that supports the routing_


_specified in this requirement, and if necessary, configure the device to perform the routing_
_specified as described in the AGD guidance. The evaluator shall ensure the test network is_
_capable of sending any traffic identified as exceptions. The evaluator shall turn on the_
_sniffing tool, establish the VPN connection, and perform the same actions with the device as_
_performed in the first step, as well as ensuring that all exception traffic is generated. The_
_evaluator shall verify that the sniffing tool captures traffic generated by these actions, turn_
_off the sniffing tool, and save the session data._


_Step 3: The evaluator shall examine the traffic from both step one and step two to verify_
_that all Data Plane traffic is encapsulated by IPsec, modulo the exceptions identified in the_
_SFR (if applicable). For each exception listed in the SFR, the evaluator shall verify that that_
_traffic is allowed outside of the VPN tunnel. The evaluator shall examine the Security_
_Parameter Index (SPI) value present in the encapsulated packets captured in Step two from_
_the TOE to the Gateway and shall verify this value is the same for all actions used to_
_generate traffic through the VPN. Note that it is expected that the SPI value for packets_
_from the Gateway to the TOE is different than the SPI value for packets from the TOE to the_
_Gateway. The evaluator shall be aware that IP traffic on the cellular baseband outside of_
_the IPsec tunnel may be emanating from the baseband processor and shall verify with the_
_manufacturer that any identified traffic is not emanating from the application processor._


_Step 4: (Conditional: If ICMP is not listed as part of the IP traffic needed for the correct_
_functioning of the TOE) The evaluator shall perform an ICMP echo from the TOE to the IP_
_address of another device on the local wireless network and shall verify that no packets are_
_sent using the sniffing tool. The evaluator shall attempt to send packets to the TOE outside_
_the VPN tunnel (i.e. not through the VPN gateway), including from the local wireless_
_network, and shall verify that the TOE discards them._


**FDP_STG_EXT.1 User Data Storage**


FDP_STG_EXT.1.1

The TSF shall provide protected storage for the Trust Anchor Database.


**Evaluation Activities**





**FDP_UPC_EXT.1/APPS Inter-TSF User Data Transfer Protection (Applications)**


FDP_UPC_EXT.1.1/APPS

The TSF shall provide a means for non-TSF applications executing on the TOE to
use [

_Mutually authenticated TLS as defined in the Functional Package for_
_Transport Layer Security (TLS), version 1.1,_
_HTTPS,_

_and [_ _**selection**_ _:_

_[mutually authenticated DTLS as defined in the Functional Package for](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=439&id=439)_
_Transport Layer Security (TLS), version 1.1_
_[IPsec as defined in the PP-Module for Virtual Private Network (VPN)](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=467&id=467)_
_Clients, version 2.4_
_no other protocol_

_]_ ] to provide a protected communication channel between the non-TSF
application and another IT product that is logically distinct from other
communication channels, provides assured identification of its end points,
protects channel data from disclosure, and detects modification of the channel
data.


**Application Note:** The intent of this requirement is that one of the selected
protocols is available for use by user applications running on the device for use
in connecting to distant-end services that are not necessarily part of the


FDP_UPC_EXT.1.2/APPS



enterprise infrastructure. It should be noted that the FTP_ITC_EXT.1 requires
that all TSF communications be protected using the protocols indicated in that
requirement, so the protocols required by this component ride "on top of" those
listed in FTP_ITC_EXT.1.


It should also be noted that some applications are part of the TSF, and
FTP_ITC_EXT.1 requires that TSF applications be protected by at least one of
the protocols in first selection in FTP_ITC_EXT.1. It is not required to have two
different implementations of a protocol, or two different protocols, to satisfy
both this requirement (for non-TSF apps) and FTP_ITC_EXT.1 (for TSF apps), as
long as the services specified are provided.


The ST author must list which trusted channel protocols are implemented by the
Mobile Device for use by non-TSF apps.


The TSF must be validated against requirements from the Functional Package
for Transport Layer Security (TLS), version 1.1, with the following selections
made:

FCS_TLS_EXT.1:

TLS is selected
Client is selected

FCS_TLSC_EXT.1.1:

The cipher suites selected must correspond with the algorithms and
hash functions allowed in FCS_COP.1.
Mutual authentication must be selected

FCS_TLSC_EXT.1.3

With no exceptions is selected.


[If mutually authenticated DTLS as defined in the Functional Package for](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=439&id=439)
Transport Layer Security (TLS), version 1.1 is selected, the TSF must be
[validated against requirements from the Functional Package for Transport Layer](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=439&id=439)
Security (TLS), version 1.1, with the following selections made:

FCS_TLS_EXT.1:

DTLS is selected
Client is selected

FCS_DTLSC_EXT.1.1:

The cipher suites selected must correspond with the algorithms and
hash functions allowed in FCS_COP.1.
Mutual authentication must be selected

FCS_DTLSC_EXT.1.3

With no exceptions is selected.


If the ST author selects IPsec as defined in the PP-Module for Virtual Private
[Network (VPN) Clients, version 2.4, the TSF must be validated against the PP-](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=467&id=467)
Module for Virtual Private Network (VPN) Clients.


The TSF shall permit the non-TSF applications to initiate communication via the
trusted channel.



**Evaluation Activities**


_FDP_UPC_EXT.1/APPS_
_The evaluator shall verify that the API documentation provided according to Section 5.2.2 Class_
_ADV: Development includes the security functions (protection channel) described in these_
_requirements, and verify that the APIs implemented to support this requirement include the_
_appropriate settings/parameters so that the application can both provide and obtain the_
_information needed to assure mutual identification of the endpoints of the communication as_
_required by this component._


_**TSS**_
_The evaluator shall examine the TSS to determine that it describes that all protocols listed in the_
_TSS are specified and included in the requirements in the ST._


_**Guidance**_
_The evaluator shall confirm that the operational guidance contains instructions necessary for_
_configuring the protocols selected for use by the applications._


_**Tests**_
_**Evaluation Activity Note:**_ _The following test requires the developer to provide access to a test_
_platform that provides the evaluator with tools that are typically not found on consumer Mobile_


_Device products._


_The evaluator shall write, or the developer shall provide access to, an application that requests_
_protected channel services by the TSF. The evaluator shall verify that the results from the_
_protected channel match the expected results according to the API documentation. This_
_application may be used to assist in verifying the protected channel Evaluation Activities for the_
_protocol requirements. The evaluator shall also perform the following tests:_

_Test 39: The evaluators shall ensure that the application is able to initiate communications_
_with an external IT entity using each protocol specified in the requirement, setting up the_
_connections as described in the operational guidance and ensuring that communication is_
_successful._
_Test 40: The evaluator shall ensure, for each communication channel with an authorized IT_
_entity, the channel data are not sent in plaintext._


**5.1.6 Class: Identification and Authentication (FIA)**


**FIA_AFL_EXT.1 Authentication Failure Handling**


FIA_AFL_EXT.1.1

The TSF shall consider password and [ **selection** : _biometric in accordance with_
_[the Biometric Enrollment and Verification, version 1.1](https://github.com/biometricITC/cPP-biometrics/blob/master/Protection%20Profile/BiocPP.adoc)_, _hybrid_, _no other_
_mechanism_ ] as critical authentication mechanisms.


**Application Note:** A critical authentication mechanism is one in which
countermeasures are triggered (i.e. wipe of the device) when the maximum
number of unsuccessful authentication attempts is exceeded, rendering the
other factors unavailable.


If no additional authentication mechanisms are selected in FIA_UAU.5.1, then no
other mechanism must be selected. If an additional authentication mechanism is
selected in FIA_UAU.5.1, then it must only be selected in FIA_AFL_EXT.1.1 if
surpassing the authentication failure threshold for biometric data causes a
countermeasure to be triggered regardless of the failure status of the other
authentication mechanisms.


If the TOE implements multiple Authentication Factor interfaces (for example, a
DAR decryption interface, a lock screen interface, an auxiliary boot mode
interface), this component applies to all available interfaces. For example, a
password is a critical authentication mechanism regardless of if it is being
entered at the DAR decryption interface or at a lock screen interface.


FIA_AFL_EXT.1.2

The TSF shall detect when a configurable positive integer within [ **assignment** :
_range of acceptable values for each authentication mechanism_ ] of [ **selection** :
_unique_, _non-unique_ ] unsuccessful authentication attempts occur related to last
successful authentication for each authentication mechanism.


**Application Note:** The positive integers is configured according to
FMT_SMF.1.1 function 2.


An unique authentication attempt is defined as any attempt to verify a password
or biometric sample, in which the input is different from a previous attempt.
"unique" must be selected if the authentication system increments the counter
only for unique unsuccessful authentication attempts. For example, if the same
incorrect password is attempted twice the authentication system increments the
counter once. "non-unique" must be selected if the authentication system
increments the counter for each unsuccessful authentication attempt, regardless
of if the input is unique. For example, if the same incorrect password is
attempted twice the authentication system increments the counter twice.


If hybrid authentication (i.e. a combination of biometric and pin/password) is
supported, a failed authentication attempt can be counted as a single attempt,
even if both the biometric and pin/password were incorrect.


If the TOE supports multiple authentication mechanisms per FIA_UAU.5.1, this
component applies to all authentication mechanisms. It is acceptable for each
authentication mechanism to utilize an independent counter or for multiple
authentication mechanisms to utilize a shared counter. The interaction between
the authentication factors in regards to the authentication counter must be in
accordance with FIA_UAU.5.2.


If the TOE implements multiple Authentication Factor interfaces (for example, a
DAR decryption interface, a lock screen interface, an auxiliary boot mode
interface), this component applies to all available interfaces. However, it is
acceptable for each Authentication Factor interface to be configurable with a
different number of unsuccessful authentication attempts.


FIA_AFL_EXT.1.3


FIA_AFL_EXT.1.4


FIA_AFL_EXT.1.5


FIA_AFL_EXT.1.6



The TSF shall maintain the number of unsuccessful authentication attempts that
have occurred upon power off.


**Application Note:** The TOE may implement an Authentication Factor interface
that precedes another Authentication Factor interface in the boot sequence (for
example, a volume DAR decryption interface which precedes the lock screen
interface) before the user can access the device. In this situation, because the
user must successfully authenticate to the first interface to access the second,
the number of unsuccessful authentication attempts need not be maintained for
the second interface.


When the defined number of unsuccessful authentication attempts has exceeded
the maximum allowed for a given authentication mechanism, all future
authentication attempts will be limited to other available authentication
mechanisms, unless the given mechanism is designated as a critical
authentication mechanism.


**Application Note:** In accordance with FIA_AFL_EXT.1.3, this requirement also
applies after the TOE is powered off and powered back on.


When the defined number of unsuccessful authentication attempts for the last
available authentication mechanism or single critical authentication mechanism
has been surpassed, the TSF shall perform a wipe of all protected data.


**Application Note:** Wipe is performed in accordance with FCS_CKM_EXT.5.
Protected data is all non-TSF data, including all user or enterprise data. Some or
all of this data may be considered sensitive data as well.


If the TOE implements multiple Authentication Factor interfaces (for example, a
DAR decryption interface, a lock screen interface, an auxiliary boot mode
interface), this component applies to all available interfaces.


The TSF shall increment the number of unsuccessful authentication attempts
prior to notifying the user that the authentication was unsuccessful.


**Application Note:** This requirement is to ensure that if power is cut to the
device directly after an authentication attempt, the counter will be incremented
to reflect that attempt.



**Evaluation Activities**


_FIA_AFL_EXT.1_
_**TSS**_
_The evaluator shall ensure that the TSS describes that a value corresponding to the number of_
_unsuccessful authentication attempts since the last successful authentication is kept for each_
_Authentication Factor interface. The evaluator shall ensure that this description also includes if_
_and how this value is maintained when the TOE loses power, either through a graceful powered_
_off or an ungraceful loss of power. The evaluator shall ensure that if the value is not maintained,_
_the interface is after another interface in the boot sequence for which the value is maintained._


_If the TOE supports multiple authentication mechanisms, the evaluator shall ensure that this_
_description also includes how the unsuccessful authentication attempts for each mechanism_
_selected in FIA_UAU.5.1 is handled. The evaluator shall verify that the TSS describes if each_
_authentication mechanism utilizes its own counter or if multiple authentication mechanisms_
_utilize a shared counter. If multiple authentication mechanisms utilize a shared counter, the_
_evaluator shall verify that the TSS describes this interaction._


_The evaluator shall confirm that the TSS describes how the process used to determine if the_
_authentication attempt was successful. The evaluator shall ensure that the counter would be_
_updated even if power to the device is cut immediately following notifying the TOE user if the_
_authentication attempt was successful or not._


_**Guidance**_
_The evaluator shall verify that the AGD guidance describes how the administrator configures the_
_maximum number of unique unsuccessful authentication attempts._


_**Tests**_

_Test 41: The evaluator shall configure the device with all authentication mechanisms_
_selected in FIA_UAU.5.1. The evaluator shall perform the following tests for each available_
_authentication interface:_


_Test 1a: The evaluator shall configure the TOE, according to the AGD guidance, with a_


_maximum number of unsuccessful authentication attempts. The evaluator shall enter the_
_locked state and enter incorrect passwords until the wipe occurs. The evaluator shall verify_
_that the number of password entries corresponds to the configured maximum and that the_
_wipe is implemented._


_Test 1b: [conditional] If the TOE supports multiple authentication mechanisms the previous_
_test shall be repeated using a combination of authentication mechanisms confirming that_
_the critical authentication mechanisms will cause the device to wipe and that when the_
_maximum number of unsuccessful authentication attempts for a non-critical authentication_
_mechanism is exceeded, the device limits authentication attempts to other available_
_authentication mechanisms. If multiple authentication mechanisms utilize a shared counter,_
_then the evaluator shall verify that the maximum number of unsuccessful authentication_
_attempts can be reached by using each individual authentication mechanism and a_
_combination of all authentication mechanisms that share the counter._


_Test 42: The evaluator shall repeat test one, but shall power off (by removing the battery, if_
_possible) the TOE between unsuccessful authentication attempts. The evaluator shall verify_
_that the total number of unsuccessful authentication attempts for each authentication_
_mechanism corresponds to the configured maximum and that the critical authentication_
_mechanisms cause the device to wipe. Alternatively, if the number of authentication failures_
_is not maintained for the interface under test, the evaluator shall verify that upon booting_
_the TOE between unsuccessful authentication attempts another authentication factor_
_interface is presented before the interface under test._


**FIA_PMG_EXT.1 Password Management**


FIA_PMG_EXT.1.1

The TSF shall support the following for the Password Authentication Factor:


1. Passwords shall be able to be composed of any combination of [ **selection** :

_upper and lower case letters_, _[_ _**assignment**_ _: a character set of at least 52_
_characters]_ ], numbers, and special characters: [ **selection** : _"!"_, _"@"_, _"#"_,
_"$"_, _"%"_, _"^"_, _"&"_, _"*"_, _"("_, _")"_, _[_ _**assignment**_ _: other characters]_ ];
2. Password length up to [ **assignment** : _an integer greater than or equal to 14_ ]

characters shall be supported.


**Application Note:** While some corporate policies require passwords of 14
characters or better, the use of a REK for DAR protection and key storage
protection and the anti-hammer requirement (FIA_TRT_EXT.1) addresses the
threat of attackers with physical access using much smaller and less complex
passwords.


The ST author selects the character set: either the upper and lower case Basic
Latin letters or another assigned character set containing at least 52 characters.
The assigned character set must be well defined: either according to an
international encoding standard (such as Unicode) or defined in the assignment
by the ST author. The ST author also selects the special characters that are
supported by the TOE; they may optionally list additional special characters
supported using the assignment.


**Evaluation Activities**





**FIA_TRT_EXT.1 Authentication Throttling**


FIA_TRT_EXT.1.1

The TSF shall limit automated user authentication attempts by [ **selection** :
_preventing authentication via an external port_, _enforcing a delay between_
_incorrect authentication attempts_ ] for all authentication mechanisms selected in
FIA_UAU.5.1. The minimum delay shall be such that no more than 10 attempts
can be attempted per 500 milliseconds.


**Application Note:** The authentication throttling applies to all authentication
mechanisms selected in FIA_UAU.5.1. The user authentication attempts in this
requirement are attempts to guess the Authentication Factor. The developer can
implement the timing of the delays in the requirements using unequal or equal
timing of delays. The minimum delay specified in this requirement provides
defense against brute forcing.


**Evaluation Activities**





**FIA_UAU.5 Multiple Authentication Mechanisms**


FIA_UAU.5.1

The TSF shall provide **password and [selection:** _**biometric in accordance**_
_**[with the Biometric Enrollment and Verification, version 1.1](https://github.com/biometricITC/cPP-biometrics/blob/master/Protection%20Profile/BiocPP.adoc)**_ **,** _**hybrid**_ **,** _**no**_
_**other mechanism**_ **]** to support user authentication.


**Application Note:** The TSF must support a Password Authentication Factor and
may optionally implement a BAF. A hybrid authentication factor is where a user
has to submit a combination of PIN/password and biometric sample where both
have to pass and if either fails the user is not made aware of which factor failed.


[If biometric in accordance with the Biometric Enrollment and Verification,](https://github.com/biometricITC/cPP-biometrics/blob/master/Protection%20Profile/BiocPP.adoc)
version 1.1 or hybrid is selected, then the TSF must be validated against
[requirements from the Biometric Enrollment and Verification, version 1.1.](https://github.com/biometricITC/cPP-biometrics/blob/master/Protection%20Profile/BiocPP.adoc)


[If hybrid is selected, biometric in accordance with the Biometric Enrollment and](https://github.com/biometricITC/cPP-biometrics/blob/master/Protection%20Profile/BiocPP.adoc)
Verification, version 1.1 does not need to be selected, but should be selected if
the biometric authentication can be used independent of the hybrid
authentication, i.e. without having to enter a PIN/password.


The Password Authentication Factor is configured according to FIA_PMG_EXT.1.


FIA_UAU.5.2

The TSF shall authenticate any user's claimed identity according to the

[ **assignment** : _rules describing how each authentication mechanism selected in_
_FIA_UAU.5.1 provides authentication_ ].


**Application Note:** Rules regarding how the authentication factors interact in
terms of unsuccessful authentication are covered in FIA_AFL_EXT.1.


**Evaluation Activities**


_FIA_UAU.5_
_**TSS**_
_The evaluator shall ensure that the TSS describes each mechanism provided to support user_
_authentication and the rules describing how the authentication mechanisms provide_
_authentication._


_Specifically, for all authentication mechanisms specified in FIA_UAU.5.1, the evaluator shall_
_ensure that the TSS describes the rules as to how each authentication mechanism is used._
_Example rules are how the authentication mechanism authenticates the user (i.e. how does the_


_TSF verify that the correct password or biometric sample was entered), the result of a successful_
_authentication (i.e. is the user input used to derive or unlock a key) and which authentication_
_mechanism can be used at which authentication factor interfaces (i.e. if there are times, for_
_example, after a reboot, that only specific authentication mechanisms can be used). If multiple_
_[BAFs are claimed in FIA_MBV_EXT.1.1 in the Biometric Enrollment and Verification, version 1.1,](https://github.com/biometricITC/cPP-biometrics/blob/master/Protection%20Profile/BiocPP.adoc)_
_the interaction between the BAFs must be described. For example, whether the multiple BAFs_
_can be enabled at the same time._


_**Guidance**_
_The evaluator shall verify that configuration guidance for each authentication mechanism is_
_addressed in the AGD guidance._


_**Tests**_

_Test 44: For each authentication mechanism selected in FIA_UAU.5.1, the evaluator shall_
_enable that mechanism and verify that it can be used to authenticate the user at the_
_specified authentication factor interfaces._
_Test 45: For each authentication mechanism rule, the evaluator shall ensure that the_
_authentication mechanisms behave accordingly._


**FIA_UAU.6/CREDENTIAL Re-Authenticating (Credential Change)**


FIA_UAU.6.1/CREDENTIAL

The TSF shall re-authenticate the user **via the Password Authentication**
**Factor** under the conditions [ _attempted change to any supported authentication_
_mechanisms_ ].


**Application Note:** The password authentication factor must be entered before
either the password or biometric authentication factor, if selected in
FIA_UAU.5.1, can be changed.


**Evaluation Activities**





**FIA_UAU.6/LOCKED Re-Authenticating (TSF Lock)**


FIA_UAU.6.1/LOCKED

The TSF shall re-authenticate the user **via an authentication factor defined**
**in FIA_UAU.5.1** under the conditions **TSF-initiated lock, user-initiated lock,**

**[assignment:** _**other conditions**_ **]** .


**Application Note:** Depending on the selections made in FIA_UAU.5.1, either
the password (at a minimum), biometric authentication or hybrid authentication
mechanisms can be used to unlock the device. TSF-initiated and user-initiated
locking is described in FTA_SSL_EXT.1.


**Evaluation Activities**


**FIA_UAU.7 Protected Authentication Feedback**


FIA_UAU.7.1

The TSF shall provide only [ _obscured feedback to the device’s display_ ] to the
user while the authentication is in progress.


**Application Note:** This applies to all authentication methods specified in
FIA_UAU.5.1. The TSF may briefly (1 second or less) display each character or
provide an option to allow the user to unmask the password; however, the
password must be obscured by default.


If biometric in accordance with the Biometric Enrollment and Verification,
[version 1.1 is selected in FIA_UAU.5.1, the TSF must not display sensitive](https://github.com/biometricITC/cPP-biometrics/blob/master/Protection%20Profile/BiocPP.adoc)
information regarding any BAF that could aid an adversary in identifying or
spoofing the respective biometric characteristics of a given human user. While it
is true that biometric samples, by themselves, are not secret, the analysis
performed by the respective biometric algorithms, as well as output data from
these biometric algorithms, is considered sensitive and must be kept secret.
Where applicable, the TSF must not reveal or make public the reasons for
authentication failure.


**Evaluation Activities**


_FIA_UAU.7_
_**TSS**_
_The evaluator shall ensure that the TSS describes the means of obscuring the authentication_
_entry, for all authentication methods specified in FIA_UAU.5.1._


_**Guidance**_
_The evaluator shall verify that any configuration of this requirement is addressed in the AGD_
_guidance and that the password is obscured by default._


_**Tests**_

_Test 55: The evaluator shall enter passwords on the device, including at least the Password_
_Authentication Factor at lock screen, and verify that the password is not displayed on the_
_device._
_[Test 56: [conditional] If biometric in accordance with the Biometric Enrollment and](https://github.com/biometricITC/cPP-biometrics/blob/master/Protection%20Profile/BiocPP.adoc)_
_Verification, version 1.1 is selected in FIA_UAU.5.1, for each BAF claimed in_
_[FIA_MBV_EXT.1.1 in the Biometric Enrollment and Verification, version 1.1 the evaluator](https://github.com/biometricITC/cPP-biometrics/blob/master/Protection%20Profile/BiocPP.adoc)_
_shall authenticate by producing a biometric sample at lock screen. As the biometric_


_algorithms are performed, the evaluator shall verify that sensitive images, audio, or other_
_information identifying the user are kept secret and are not revealed to the user._
_Additionally, the evaluator shall produce a biometric sample that fails to authenticate and_
_verify that the reasons for authentication failure (user mismatch, low sample quality, etc.)_
_are not revealed to the user. It is acceptable for the BAF to state that it was unable to_
_physically read the biometric sample, for example, if the sensor is unclean or the biometric_
_sample was removed too quickly. However, specifics regarding why the presented biometric_
_sample failed authentication shall not be revealed to the user._


**FIA_UAU_EXT.1 Authentication for Cryptographic Operation**


FIA_UAU_EXT.1.1

The TSF shall require the user to present the Password Authentication Factor
prior to decryption of protected data and encrypted DEKs, KEKs and [ **selection** :
_long-term trusted channel key material_, _all software-based key storage_, _no other_
_keys_ ] at startup.


**Application Note:** The intent of this requirement is to prevent decryption of
protected data before the user has authorized to the device using the Password
Authentication Factor. The Password Authentication Factor is also required in
order derive the key used to decrypt sensitive data, which includes softwarebased secure key storage.


**Evaluation Activities**


**FIA_UAU_EXT.2 Timing of Authentication**


FIA_UAU_EXT.2.1

The TSF shall allow [ **selection** : _[_ _**assignment**_ _: list of actions]_, _no actions_ ] on
behalf of the user to be performed before the user is authenticated.


FIA_UAU_EXT.2.2

The TSF shall require each user to be successfully authenticated before allowing
any other TSF-mediated actions on behalf of that user.


**Application Note:** The security relevant actions allowed by unauthorized users
in locked state must be listed. At a minimum the actions that correspond to the
functions available to the user in FMT_SMF.1 and are allowed by unauthorized
users in locked state should be listed. For example, if the user can enable/disable
the camera per function 5 of FMT_SMF.1 and unauthorized users can take a
picture when the device is in locked state, this action must be listed.


**Evaluation Activities**





**FIA_X509_EXT.1 X.509 Validation of Certificates**


FIA_X509_EXT.1.1

The TSF shall validate certificates in accordance with the following rules:

RFC 5280 certificate validation and certificate path validation.
The certificate path must terminate with a certificate in the Trust Anchor
Database.
The TSF shall validate a certificate path by ensuring the presence of the
basicConstraints extension, that the CA flag is set to TRUE for all CA
certificates, and that any path constraints are met.
The TSF shall validate that any CA certificate includes caSigning purpose in
the key usage field.
The TSF shall validate the revocation status of the certificate using

[ **selection** : _OCSP as specified in RFC 6960_, _CRL as specified in RFC 5759_,
_an OCSP TLS Status Request Extension (OCSP stapling) as specified in RFC_
_6066_, _OCSP TLS Multi-Certificate Status Request Extension (i.e., OCSP_
_Multi-Stapling) as specified in RFC 6961_ ].
The TSF shall validate the extendedKeyUsage field according to the
following rules:

Certificates used for trusted updates and executable code integrity
verification shall have the Code Signing Purpose (id-kp 3 with OID
1.3.6.1.5.5.7.3.3) in the extendedKeyUsage field.
Server certificates presented for TLS shall have the Server
Authentication purpose (id-kp 1 with OID 1.3.6.1.5.5.7.3.1) in the
extendedKeyUsage field.
Server certificates presented for EST shall have the CMC Registration
Authority (RA) purpose (id-kp-cmcRA with OID 1.3.6.1.5.5.7.3.28) in
the extendedKeyUsage field. [conditional]
Client certificates presented for TLS shall have the Client
Authentication purpose (id-kp 2 with OID 1.3.6.1.5.5.7.3.2) in the
extendedKeyUsage field.
OCSP certificates presented for OCSP responses shall have the OCSP
Signing purpose (id-kp 9 with OID 1.3.6.1.5.5.7.3.9) in the
extendedKeyUsage field. [conditional]


**Application Note:** FIA_X509_EXT.1.1 lists the rules for validating certificates.
The ST author must select whether revocation status is verified using OCSP or
CRLs. OCSP stapling and OCSP multi-stapling only support TLS server
certificate validation. If other certificate types are validated, either OCSP or CRL
[should be claimed. The PP-Module for Wireless LAN Clients, version 1.0, to](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=386&id=386)
which a MDF TOE must also conform, requires that certificates are used for
EAP-TLS; this use requires that the extendedKeyUsage rules are verified.


FIA_X509_EXT.1.2



Certificates may optionally be used for trusted updates of system software and
applications (FPT_TUD_EXT.2) and for integrity verification (FPT_TST_EXT.2(1))
and, if implemented, must be validated to contain the Code Signing purpose
extendedKeyUsage.


While FIA_X509_EXT.1.1 requires that the TOE perform certain checks on the
certificate presented by a TLS server, there are corresponding checks that the
authentication server will have to perform on the certificate presented by the
client; namely that the extendedKeyUsage field of the client certificate includes
“Client Authentication” and that the key agreement bit (for the Diffie-Hellman
ciphersuites) or the key encipherment bit (for RSA ciphersuites) be set.
Certificates obtained for use by the TOE will have to conform to these
requirements in order to be used in the enterprise. This check is required to
[support EAP-TLS for the PP-Module for Wireless LAN Clients, version 1.0.](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=386&id=386)


The TSF shall only treat a certificate as a CA certificate if the basicConstraints
extension is present and the CA flag is set to TRUE.


**Application Note:** This requirement applies to certificates that are used and
processed by the TSF and restricts the certificates that may be added to the
Trust Anchor Database.



**Evaluation Activities**


_FIA_X509_EXT.1_
_**TSS**_
_The evaluator shall ensure the TSS describes where the check of validity of the certificates takes_
_place. The evaluator ensures the TSS also provides a description of the certificate path_
_validation algorithm._


_**Guidance**_
_There are no guidance evaluation activities for this component._


_**Tests**_
_The tests described must be performed in conjunction with the other Certificate Services_
_evaluation activities, including the use cases in FIA_X509_EXT.2.1 and FIA_X509_EXT.3. The_
_tests for the extendedKeyUsage rules are performed in conjunction with the uses that require_
_those rules. The evaluator shall create a chain of at least four certificates: the node certificate to_
_be tested, two Intermediate CAs, and the self-signed Root CA._

_Test 60: The evaluator shall demonstrate that validating a certificate without a valid_
_certification path results in the function failing, for each of the following reasons, in turn:_

_By establishing a certificate path in which one of the issuing certificates is not a CA_
_certificate,_
_By omitting the basicConstraints field in one of the issuing certificates,_
_By setting the basicConstraints field in an issuing certificate to have CA=False,_
_By omitting the CA signing bit of the key usage field in an issuing certificate, and_
_By setting the path length field of a valid CA field to a value strictly less than the_
_certificate path._

_The evaluator shall then establish a valid certificate path consisting of valid CA certificates,_
_and demonstrate that the function succeeds. The evaluator shall then remove trust in one of_
_the CA certificates, and show that the function fails._


_Test 61: The evaluator shall demonstrate that validating an expired certificate results in the_
_function failing._


_Test 62: The evaluator shall test that the TOE can properly handle revoked certificates-_
_conditional on whether CRL, OCSP, OSCP stapling, or OCSP multi-stapling is selected; if_
_multiple methods are selected, then the following tests shall be performed for each method:_


_The evaluator shall test revocation of the node certificate._


_The evaluator shall also test revocation of the intermediate CA certificate (i.e. the_
_intermediate CA certificate should be revoked by the root CA). For the test of the WLAN_
_use case, only pre-stored CRLs are used. If OCSP stapling per RFC 6066 is the only_
_supported revocation method, this test is omitted._


_The evaluator shall ensure that a valid certificate is used, and that the validation function_
_succeeds. The evaluator then attempts the test with a certificate that has been revoked (for_
_each method chosen in the selection) to ensure when the certificate is no longer valid that_
_the validation function fails._


_Test 63: If any OCSP option is selected, the evaluator shall configure the OCSP server or_
_use a man-in-the-middle tool to present a certificate that does not have the OCSP signing_
_purpose and verify that validation of the OCSP response fails. If CRL as specified in RFC_


_5759 is selected, the evaluator shall configure the CA to sign a CRL with a certificate that_
_does not have the cRLsign key usage bit set, and verify that validation of the CRL fails._


_Test 64: The evaluator shall modify any byte in the first eight bytes of the certificate and_
_demonstrate that the certificate fails to validate (the certificate will fail to parse correctly)._


_Test 65: The evaluator shall modify any bit in the last byte of the signature algorithm of the_
_certificate and demonstrate that the certificate fails to validate (the signature on the_
_certificate will not validate)._


_Test 66: The evaluator shall modify any byte in the public key of the certificate and_
_demonstrate that the certificate fails to validate (the signature on the certificate will not_
_validate)._


_Test 67:_

_Test 67.1: (Conditional on support for EC certificates as indicated in FCS_COP.1(3))._
_The evaluator shall establish a valid, trusted certificate chain consisting of an EC leaf_
_certificate, an EC Intermediate CA certificate not designated as a trust anchor, and an_
_EC certificate designated as a trusted anchor, where the elliptic curve parameters are_
_specified as a named curve. The evaluator shall confirm that the TOE validates the_
_certificate chain._


_Test 67.2: (Conditional on support for EC certificates as indicated in FCS_COP.1(3))._
_The evaluator shall replace the intermediate certificate in the certificate chain for Test_
_8a with a modified certificate, where the modified intermediate CA has a public key_
_information field where the EC parameters uses an explicit format version of the_
_Elliptic Curve parameters in the public key information field of the intermediate CA_
_certificate from Test 8a, and the modified Intermediate CA certificate is signed by the_
_trusted EC root CA, but having no other changes. The evaluator shall confirm the TOE_
_treats the certificate as invalid._


**FIA_X509_EXT.2 X.509 Certificate Authentication**


FIA_X509_EXT.2.1

The TSF shall use X.509v3 certificates as defined by RFC 5280 to support
authentication for [ _mutually authenticated TLS as defined in the Functional_
_Package for Transport Layer Security (TLS), version 1.1, HTTPS, [_ _**selection**_ _:_
_IPsec in accordance with the PP-Module for Virtual Private Network (VPN)_
_[Clients, version 2.4, mutually authenticated DTLS as defined in the Functional](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=467&id=467)_
_Package for Transport Layer Security (TLS), version 1.1, no other protocol ]_ ] and

[ **selection** : _code signing for system software updates_, _code signing for mobile_
_applications_, _code signing for integrity verification_, _[_ _**assignment**_ _: other uses]_,
_no additional uses_ ].


**Application Note:** The ST author’s first selection must match the selection of
FDP_UPC_EXT.1.1/APPS and FTP_ITC_EXT.1.1.


Certificates may optionally be used for trusted updates of system software
(FPT_TUD_EXT.2.3) and mobile applications (FPT_TUD_EXT.6.1) and for
integrity verification (FPT_TST_EXT.2.1/PREKERNEL and FPT_TST_EXT.3.1). If
code signing for system software updates or code signing for mobile applications
is selected FPT_TUD_EXT.4.1 must be included in the ST.


If code signing for integrity verification is selected FPT_TST_EXT.3.1 must be
included in the ST.


If FPT_TUD_EXT.5.1 is included in the ST, code signing for mobile applications
must be included in the selection.


FIA_X509_EXT.2.2

When the TSF cannot establish a connection to determine the revocation status
of a certificate, the TSF shall [ **selection** : _allow the administrator to choose_
_whether to accept the certificate in these cases_, _allow the user to choose_
_whether to accept the certificate in these cases_, _accept the certificate_, _not_
_accept the certificate_ ].


**Application Note:** The TOE must not accept the certificate if it fails any of the
other validation rules in FIA_X509_EXT.1. However, often a connection must be
established to perform a verification of the revocation status of a certificate either to download a CRL or to perform OCSP. The selection is used to describe
the behavior in the event that such a connection cannot be established (for
example, due to a network error). If the TOE has determined the certificate is
valid according to all other rules in FIA_X509_EXT.1, the behavior indicated in
the selection must determine the validity. If allow the administrator to choose or
allow the user to choose the administrator-configured or user-configured option


is selected, the ST author must also select function 30 in FMT_SMF.1.


The TOE may behave differently depending on the trusted channel; for example,
in the case of WLAN where connections are unlikely to be established, the TOE
may accept the certificate even though certificates are not accepted for other
channels. The ST author should select all applicable behaviors.


Validation Guidelines:


**Rule #8**


**Rule #9**


**Evaluation Activities**





**FIA_X509_EXT.3 Request Validation of Certificates**


FIA_X509_EXT.3.1

The TSF shall provide a certificate validation service to applications.


FIA_X509_EXT.3.2

The TSF shall respond to the requesting application with the success or failure
of the validation.


**Application Note:** In order to comply with all of the rules in FIA_X509_EXT.1,
multiple API calls may be required; all of these calls should be clearly
documented


**Evaluation Activities**


_FIA_X509_EXT.3_
_The evaluator shall verify that the API documentation provided according to Section 5.2.2 Class_
_ADV: Development includes the security function (certificate validation) described in this_
_requirement. This documentation shall be clear as to which results indicate success and failure._


_**TSS**_
_There are no TSS evaluation activities for this component._


_**Guidance**_
_There are no guidance evaluation activities for this component._


_**Tests**_
_The evaluator shall write, or the developer shall provide access to, an application that requests_
_certificate validation by the TSF. The evaluator shall verify that the results from the validation_
_match the expected results according to the API documentation. This application may be used to_
_verify that import, removal, modification, and validation are performed correctly according to_


_the tests required by FDP_STG_EXT.1, FTP_ITC_EXT.1, FMT_SMF.1, and FIA_X509_EXT.1._


**5.1.7 Class: Security Management (FMT)**
Both the user and the administrator may manage the TOE. This administrator is likely to be acting remotely
and could be the Mobile Device Management (MDM) Administrator acting through an MDM Agent.


The Administrator is responsible for management activities, including setting the policy that is applied by the
enterprise on the Mobile Device. These management functions are likely to be a different set than those
management functions provided to the user. Management functions that are provided to the user and not the
administrator are listed in FMT_MOF_EXT.1.1. Management functions for which the administrator may adopt
a policy that restricts the user from performing that function are listed in FMT_MOF_EXT.1.2.


Table 7 compares the management functions required by this Protection Profile in the following three
requirements (FMT_MOF_EXT.1.1, FMT_MOF_EXT.1.2, and FMT_SMF.1).


**FMT_MOF_EXT.1 Management of Security Functions Behavior**


FMT_MOF_EXT.1.1

The TSF shall restrict the ability to perform the functions [ _in column 4 of Table_
_7_ ] to the user.


**Application Note:** The functions that have an "M" in the fourth column are
mandatory for this component, thus are restricted to the user, meaning that the
administrator cannot manage those functions. The functions that have an "O" in
the fourth column are optional and may be selected; and those functions with a
"-" are not applicable and may not be selected. The ST author should select those
security management functions that only the user may perform (i.e. the ones the
administrator may not perform).


The ST author may not select the same function in both FMT_MOF_EXT.1.1 and
FMT_MOF_EXT.1.2. A function cannot contain an "M" in both column 4 and
column 6.


The ST author may use a table in the ST, indicating with clear demarcations (to
be accompanied by an index) those functions that are restricted to the user
(column 4). The ST author should iterate a row to indicate any variations in the
selectable sub-functions or assigned values with respect to the values in the
columns.


For functions that are mandatory, any sub-functions not in a selection are also
mandatory and any assignments must contain at least one assigned value. For
non-selectable sub-functions in an optional function, all sub-functions outside a
selection must be implemented in order for the function to be listed.


FMT_MOF_EXT.1.2

The TSF shall restrict the ability to perform the functions [ _in column 6 of Table_
_7_ ] to the administrator when the device is enrolled and according to the
administrator-configured policy.


**Application Note:** As long as the device is enrolled in management, the
administrator (of the enterprise) must be guaranteed that minimum security
functions of the enterprise policy are enforced. Further restrictive policies can
be applied at any time by the user on behalf of the user or other administrators.


The functions that have an "M" in the sixth column are mandatory for this
component; the functions that have an "O" in the sixth column are optional and
may be selected; and those functions with a "-" in the sixth are not applicable
and may not be selected.


The ST author may not select the same function in both FMT_MOF_EXT.1.1 and
FMT_MOF_EXT.1.2.


The ST author should select those security management functions that the
administrator may restrict. The ST author may use a table in the ST, indicating
with clear demarcations (to be accompanied by an index) those functions that
are and are not implemented with APIs for the administrator (as in column 5).
Additionally, the ST author should demarcate which functions the user is
prevented from accessing or performing (as in column 6). The ST author should
iterate a row to indicate any variations in the selectable sub-functions or
assigned values with respect to the values in the columns.


For functions that are mandatory, any sub-functions not in a selection are also
mandatory and any assignments must contain at least one assigned value. For
non-selectable sub-functions in an optional function, all sub-functions outside the
selection must be implemented in order for the function to be listed.


**Evaluation Activities**


**FMT_SMF.1 Specification of Management Functions**


FMT_SMF.1.1

The TSF shall be capable of performing the following management functions:


**Table 7: Management Functions**


Status Markers:
M - Mandatory
O - Optional/Objective



**#** **Management Function** **Impl.** **User**
**Only**



**Admin** **Admin**

**Only**







2 configure session locking policy:

Screen-lock enabled/disabled
Screen lock timeout
Number of authentication failures


3 enable/disable the VPN protection:

Across device

[ **selection** :

_on a per-app basis_
_on a per-group of applications_
_processes basis_



M - M M


M O O O


_no other method_

]


4 enable/disable [ **assignment** : _list of all_
_radios_ ]





6 transition to the locked state


8 configure application installation policy
by

[ **selection** :

_restricting the sources of_
_applications_
_specifying a set of allowed_
_applications based on_

_[_ _**assignment**_ _: application_
_characteristics] (an application_
_allowlist)_
_denying installation of_
_applications_

]


10 destroy imported keys or secrets and

[ **selection** : _no other keys or secrets_,

_[_ _**assignment**_ _: list of other categories of_
_keys or secrets]_ ] in the secure key
storage


12 remove imported X.509v3 certificates
and [ **selection** : _no other X.509v3_
_certificates_, _[_ _**assignment**_ _: list of other_
_categories of X.509v3 certificates]_ ] in
the Trust Anchor Database


14 remove applications


16 install applications


18 enable/disable display notification in the
locked state of: [ **selection** :

_email notifications_
_calendar appointments_
_contact associated with phone call_
_notification_
_text message notification_

_[_ _**assignment**_ _: other application-_
_based notifications]_
_all notifications_

]


20 enable removable media’s data-at-rest



M O O O


M - M 

M - M M


M O O 

M O O 

M - M O


M - M O


M O O O


M O O O


protection





22 enable/disable the use of [ **selection** :
_Biometric Authentication Factor_, _Hybrid_
_Authentication Factor_ ]


24 enable/disable all data signaling over

[ **assignment** : _list of externally accessible_
_hardware ports_ ]


26 enable/disable developer modes


28 wipe Enterprise data


30 configure whether to allow or disallow
establishment of a trusted channel if the
TSF cannot establish a connection to
determine the validity of a certificate


32 read audit logs kept by the TSF


34 approve exceptions for shared use of
keys or secrets by multiple applications


36 configure the unlock banner


38 retrieve TSF-software integrity
verification values



O O O O


O O O O


O O O O


O O O 

O O O O


O O O 

O O O O


M - O O


O O O O






40 enable/disable backup of [ **selection** : _all_
_applications_, _selected applications_,
_selected groups of applications_,
_configuration data_ ] to [ **selection** : _locally_
_connected system_, _remote system_ ]







42 approve exceptions for sharing data
between [ **selection** : _applications_, _groups_
_of applications_ ]


44 unenroll the TOE from management



O O O O


O O O O


O O O O


O O O O







46 revoke Biometric template



**Application Note:** Table 7 compares the management functions required by
this Protection Profile.


The first column lists the management functions identified in the PP.


In the following columns:

‘M’ means Mandatory
‘O’ means Optional/Objective
'-' means that no value (M or O) can be assigned


The third column ("Impl.") indicates whether the function is to be implemented.
The ST author should select which Optional functions are implemented.


The fourth column ("User Only") indicates functions that are to be restricted to
the user (i.e. not available to the administrator).


The fifth column ("Admin") indicates functions that are available to the
administrator. The functions restricted to the user (column 4) cannot also be
available to the administrator. Functions available to the administrator can still
be available to the user, as long as the function is not restricted to the
administrator (column 6). Thus, if the TOE must offer these functions to the
administrator to perform, the fifth column must be selected.


The sixth column (FMT_MOF_EXT.1.2) indicates whether the function is to be
restricted to the administrator when the device is enrolled and the administrator
applies the indicated policy. If the function is restricted to the administrator the
function is not available to the user. This does not prevent the user from
modifying a setting to make the function stricter, but the user cannot undo the
configuration enforced by the administrator.


The ST author may use a table in the ST, listing only those functions that are
implemented. For functions that are mandatory, any sub-functions not in a
selection are also mandatory and any assignments must contain at least one
assigned value. For functions that are optional and contain an assignment or


selection, at least one value must be assigned/selected to be included in the ST.
For non-selectable sub-functions in an optional function, all sub-functions must
be implemented in order for the function to be included. For functions with a
"per-app basis" sub function and an assignment, the ST author must indicate
which assigned features are manageable on a per-app basis and which are not by
iterating the row.


**Function-specific Application Notes:**


Functions 3, 5, and 21 must be implemented on a device-wide basis but may
also be implemented on a per-app basis or on a per-group of applications basis in
which the configuration includes the list of applications or groups of applications
to which the enable/disable applies.


Function 3 addresses enabling and disabling the IPsec VPN only. The
configuration of the VPN Client itself (with information such as VPN Gateway,
[certificates, and algorithms) is addressed by the PP-Module for Virtual Private](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=467&id=467)
Network (VPN) Clients, version 2.4. The administrator options should only be
listed if the administrator can remotely enable/disable the VPN connection.


Function 3 optionally allows the VPN to be configured per-app or per-groups of
apps. If this configuration is selected, it does not void FDP_IFC_EXT.1. Instead
FDP_IFC_EXT.1 is applied to the application or group of applications the VPN is
applied to. In other words, all traffic destined for the VPN-enabled application or
group of applications, must travel through the VPN, but traffic not destined for
that application or group of applications can travel outside the VPN. When the
VPN is configured across the device FDP_IFC_EXT.1 applies to all traffic and the
VPN must not split tunnel.


The assignment in function 4 consists of all radios present on the TSF, such as
Wi-Fi, cellular, NFC, Bluetooth BR/EDR, and Bluetooth LE, which can be enabled
and disabled. In the future, if both Bluetooth BR/EDR and Bluetooth LE are
supported, they will be required to be enabled and disabled separately.
Disablement of the cellular radio does not imply that the radio may not be
enabled in order to place emergency phone calls; however, it is not expected
that a device in "airplane mode", where all radios are disabled, will automatically
(without authorization) turn on the cellular radio to place emergency calls.


The assignment in function 5 consists of at least one audio or visual device, such
as camera and microphone, which can be enabled and disabled by either the
user or administrator. Disablement of the microphone does not imply that the
microphone may not be enabled in order to place emergency phone calls. If
certain devices are able to be restricted to the enterprise (either device-wide,
per-app or per-group of applications) and others are able to be restricted to
users, then this function should be iterated in the table with the appropriate
table entries.


Regarding functions 4 and 5, disablement of a particular radio or audio/visual
device must be effective as soon as the TOE has power. Disablement must also
apply when the TOE is booted into auxiliary boot modes, for example, associated
with updates or backup. If the TOE supports states in which security
management policy is inaccessible, for example, due to data-at-rest protection, it
is acceptable to meet this requirement by ensuring that these devices are
disabled by default while in these states. That these devices are disabled during
auxiliary boot modes does not imply that the device (particularly the cellular
radio) may not be enabled in order to perform emergency phone calls.


Wipe of the TSF (function 7) is performed according to FCS_CKM_EXT.5.
Protected data is all non-TSF data, including all user or enterprise data. Some or
all of this data may be considered sensitive data as well.


The selection in function 8 allows the ST author to select which mechanisms are
available to the administrator through the MDM Agent to restrict the
applications which the user may install. The ST author must state if application
allowlist is applied device-wide or if it can be specified to apply to either the
Enterprise or Personal applications.


If the administrator can restrict the sources from which applications can be
installed, the ST author selects "restricting the sources of applications".
If the administrator can specify a allowlist of allowed applications, the ST
author selects "application allowlist". The ST author should list any
application characteristics (e.g. name, version, or developer) based on
which the allowlist can be formed.
If the administrator can prevent the user from installing additional
applications, the ST author selects "denying installation of applications".


In the future, function 12 may require destruction or disabling of any default
trusted CA certificates, excepting those CA certificates necessary for continued
operation of the TSF, such as the developer’s certificate. At this time, the ST
author must indicate in the assignment whether pre-installed or any other


category of X.509v3 certificates may be removed from the Trust Anchor
Database.


For function 13, the enrollment function may be installing an MDM agent and
includes the policies to be applied to the device. It is acceptable for the user
approval notice to require the user to intentionally opt to view the policies (for
example, by "tapping" on a "View" icon) rather than listing the policies in full in
the notice.


For function 15, the administrator capability to update the system software may
be limited to causing a prompt to the user to update rather than the ability to
initiate the update itself. As the administrator is likely to be acting remotely,
he/she would be unaware of inopportune situations, such as low power, which
may cause the update to fail and the device to become inoperable. The user can
refuse to accept the update in such situations. It is expected that system
architects will be cognizant of this limitation and will enforce network access
controls in order to enforce enterprise-critical updates.


Function 16 addresses both installation and update. This protection profile does
not distinguish between installation and update of applications because mobile
devices typically completely overwrite the previous installation with a new
installation during an application update.


For function 17, "Enterprise applications" are those applications that belong to
the Enterprise application group. Applications installed by the enterprise
administrator (including automatic installation by the administrator after being
requested by the user from a catalog of enterprise applications) are by default
placed in the Enterprise application group unless an exception has been made in
function 43 of FMT_SMF.1.1.


If the display of notifications in the locked state is supported, the configuration
of these notifications (function 18) must be included in the selection.


Function 19 must be included in the selection if data-at-rest protection is not
natively enabled.


Function 20 is implicitly met if the TSF does not support removable media.


For function 21, location services include location information gathered from
GPS, cellular, and Wi-Fi.


Function 22 must be included in the ST if the TOE contains a BAF. This selection
must correspond with the selection made in FIA_UAU.5.1. If biometric in
[accordance with the Biometric Enrollment and Verification, version 1.1 is](https://github.com/biometricITC/cPP-biometrics/blob/master/Protection%20Profile/BiocPP.adoc)
selected in FIA_UAU.5.1, "Biometric Authentication Factor" must be selected
and the user or admin must have the option to disable the use of it. If multiple
[BAFs are claimed in FIA_MBV_EXT.1.1 in the Biometric Enrollment and](https://github.com/biometricITC/cPP-biometrics/blob/master/Protection%20Profile/BiocPP.adoc)
Verification, version 1.1, this applies to all different modalities. If hybrid is
selected in FIA_UAU.5.1 it must be selected and the user or admin must have
the option to disable the use of it.


Function 23 must be included in the ST if the function is configurable on the
TOE for any of the trusted channels either mandated or selected in
FTP_ITC_EXT.1.1 or FDP_UPC_EXT.1.1/APPS. The configuration can be different
depending on the specific trusted channel(s) and they must be filled in for the
assignment.


The assignment in function 24 consists of all externally accessible hardware
ports, such as USB, the SD card, and HDMI, whose data transfer capabilities can
be enabled and disabled by either the user or administrator. Disablement of data
transfer over an external port must be effective during and after boot into the
normal operative mode of the device. If the TOE supports states in which
configured security management policy is inaccessible, for example, due to dataat-rest protection, it is acceptable to meet this requirement by ensuring that
data transfer is disabled by default while in these states. Each of the ports may
be enabled or disabled separately. The configuration policy need not disable all
ports together. In the case of USB, charging is still allowed if data transfer
capabilities have been disabled.


The assignment in function 25 consists of all protocols where the TSF acts as a
server, which can be enabled and disabled by either the user or administrator.


Function 26 must be included in the selection if developer modes are supported
by the TSF.


Function 27 must be included in the selection if bypass of local user
authentication, such as a "Forgot Password", password hint, or remote
authentication feature, is supported.


Function 29 must be included in the selection if the TSF allows applications,
other than the MDM Agents, to import or remove X.509v3 certificates from the
Trust Anchor Database. The MDM Agent is considered the administrator. This
function does not apply to applications trusting a certificate for its own
validations. The function only applies to situations where the application
modifies the device-wide Trust Anchor Database, affecting the validations


performed by the TSF for other applications. The user or administrator may be
provided the ability to globally allow or deny any application requests in order to
meet this requirement.


Function 30 must be included in the ST if "administrator-configured option" is
selection in FIA_X509_EXT.2.2.


Function 33 should be included in the selection if FPT_TUD_EXT.5.1 is included
in the ST and the configurable option is selected.


Function 34 should be included in the selection if user or administrator is
selected in FCS_STG_EXT.1.4.


Function 35 should be included in the selection if the user or the administrator is
selected in FCS_STG_EXT.1.5.


Function 37 must be included in the selection if FAU_SEL.1 is included in the ST.


For function 41, hotspot functionality refers to the condition in which the mobile
device is serving as an access point to other devices, not the connection of the
TOE to external hotspots.


Functions 42 and 43 correspond to FDP_ACF_EXT.1.2.


For function 44, FMT_SMF_EXT.2.1 specifies actions to be performed when the
TOE is unenrolled from management.


Function 45 must be included in the ST if IPsec is selected in FTP_ITC_EXT.1
and the native IPsec VPN client can be configured to be Always-On. Always-On is
defined as when the TOE has a network connection the VPN attempts to
connect, all data leaving the device uses the VPN when the VPN is connected
and no data leaves that device when the VPN is disconnected. The configuration
of the VPN Client itself (with information such as VPN Gateway, certificates, and
[algorithms) is addressed by the PP-Module for Virtual Private Network (VPN)](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=467&id=467)
Clients, version 2.4.


Validation Guidelines:


**Rule #8**


**Rule #9**


**Rule #10**


**Rule #11**


**Evaluation Activities**


_FMT_SMF.1_
_**TSS**_
_The evaluator shall verify that the TSS describes all management functions, what roles can_
_perform each function, and how these functions are (or can be) restricted to the roles identified_
_by FMT_MOF_EXT.1._


_The following activities are organized according to the function number in the table. These_
_activities include TSS Evaluation Activities, AGD Evaluation Activities, and test activities._


_Test activities specified below shall take place in the test environment described in the_
_evaluation activity for FPT_TUD_EXT.1._


_**Guidance**_
_The evaluator shall consult the AGD guidance to perform each of the specified tests, iterating_
_each test as necessary if both the user and administrator may perform the function. The_
_evaluator shall verify that the AGD guidance describes how to perform each management_
_function, including any configuration details. For each specified management function tested,_
_the evaluator shall confirm that the underlying mechanism exhibits the configured setting._


_The following EAs correspond to specific management functions._
_**Function 1**_
_**TSS**_
_The evaluator shall verify the TSS defines the allowable policy options: the range of values for_
_both password length and lifetime, and a description of complexity to include character set and_
_complexity policies (e.g., configuration and enforcement of number of uppercase, lowercase, and_
_special characters per password)._


_**Tests**_
_The evaluator shall exercise the TSF configuration as the administrator and perform positive and_
_negative tests, with at least two values set for each variable setting, for each of the following:_

_Minimum password length_
_Minimum password complexity_
_Maximum password lifetime_


_**Function 2**_
_**TSS**_
_The evaluator shall verify the TSS defines the range of values for both timeout period and_
_number of authentication failures for all supported authentication mechanisms._


_**Tests**_
_The evaluator shall exercise the TSF configuration as the administrator. The evaluator shall_
_perform positive and negative tests, with at least two values set for each variable setting, for_
_each of the following:_

_Screen-lock enabled/disabled_
_Screen lock timeout_
_Number of authentication failures (may be combined with test for FIA_AFL_EXT.1)_


_**Function 3**_
_**Tests**_
_The evaluator shall perform the following tests:_

_Test 72: The evaluator shall exercise the TSF configuration to enable the VPN protection._
_These configuration actions must be used for the testing of the FDP_IFC_EXT.1.1_
_requirement._
_Test 73: [conditional] If "on a per-app basis" is selected, the evaluator shall create two_
_applications and enable one to use the VPN and the other to not use the VPN. The evaluator_
_shall exercise each application (attempting to access network resources; for example, by_
_browsing different websites) individually while capturing packets from the TOE. The_
_evaluator shall verify from the packet capture that the traffic from the VPN-enabled_
_application is encapsulated in IPsec and that the traffic from the VPN-disabled application_
_is not encapsulated in IPsec._
_Test 74: [conditional] If "on a per-group of applications processes basis" is selected, the_
_evaluator shall create two applications and the applications shall be placed into different_
_groups. Enable one application group to use the VPN and the other to not use the VPN. The_
_evaluator shall exercise each application (attempting to access network resources; for_
_example, by browsing different websites) individually while capturing packets from the_
_TOE. The evaluator shall verify from the packet capture that the traffic from the application_
_in the VPN-enabled group is encapsulated in IPsec and that the traffic from the application_
_in the VPN-disabled group is not encapsulated in IPsec._


_**Function 4**_
_**TSS**_
_The evaluator shall verify that the TSS includes a description of each radio and an indication of if_
_the radio can be enabled/disabled along with what role can do so. In addition the evaluator shall_
_verify that the frequency ranges at which each radio operates is included in the TSS. The_
_evaluator shall verify that the TSS includes at what point in the boot sequence the radios are_
_powered on and indicates if the radios are used as part of the initialization of the device._


_**Guidance**_
_The evaluator shall confirm that the AGD guidance describes how to perform the enable/disable_
_function for each radio._


_**Tests**_
_The evaluator shall ensure that minimal signal leakage enters the RF shielded enclosure (i.e._
_Faraday bag, Faraday box, RF shielded room) by performing the following steps:_


_Step 1: Place the antenna of the spectrum analyzer inside the RF shielded enclosure._


_Step 2: Enable "Max Hold" on the spectrum analyzer and perform a spectrum sweep of the_
_frequency range between 300 MHz – 6000 MHz, in I kHz steps (this range should encompass_
_802.11, 802.15, GSM, UMTS, and LTE). This range will not address NFC 13.56.MHz, another_
_test should be set up with similar constraints to address NFC._


_If power above -90 dBm is observed, the Faraday box has too great of signal leakage and shall_
_not be used to complete the test for Function 4. The evaluator shall exercise the TSF_
_configuration as the administrator and, if not restricted to the administrator, the user, to enable_
_and disable the state of each radio (e.g. Wi-Fi, cellular, NFC, Bluetooth). Additionally, the_
_evaluator shall repeat the steps below, booting into any auxiliary boot mode supported by the_
_device. For each radio, the evaluator shall:_


_Step 1: Place the antenna of the spectrum analyzer inside the RF shielded enclosure. Configure_
_the spectrum analyzer to sweep desired frequency range for the radio to be tested (based on_
_range provided in the TSS)). The ambient noise floor shall be set to -110 dBm. Place the TOE_
_into the RF shielded enclosure to isolate them from all other RF traffic._


_Step 2: The evaluator shall create a baseline of the expected behavior of RF signals. The_
_evaluator shall power on the device, ensure the radio in question is enabled, power off the_
_device, enable "Max Hold" on the spectrum analyzer and power on the device. The evaluator_
_shall wait 2 minutes at each Authentication Factor interface prior to entering the necessary_


_password to complete the boot process, waiting 5 minutes after the device is fully booted. The_
_evaluator shall observe that RF spikes are present at the expected uplink channel frequency. The_
_evaluator shall clear the "Max Hold" on the spectrum analyzer._


_Step 3: The evaluator shall verify the absence of RF activity for the uplink channel when the_
_radio in question is disabled. The evaluator shall complete the following test five times. The_
_evaluator shall power on the device, ensure the radio in question is disabled, power off the_
_device, enable "Max Hold" on the spectrum analyzer and power on the device. The evaluator_
_shall wait 2 minutes at each Authentication Factor interface prior to entering the necessary_
_password to complete the boot process, waiting 5 minutes after the device is fully booted. The_
_evaluator shall clear the "Max Hold" on the spectrum analyzer. If the radios are used for device_
_initialization, then a spike of RF activity for the uplink channel can be observed initially at device_
_boot. However, if a spike of RF activity for the uplink channel of the specific radio frequency_
_band is observed after the device is fully booted or at an Authentication Factor interface it is_
_deemed that the radio is enabled._


_**Function 5**_
_**TSS**_
_The evaluator shall verify that the TSS includes a description of each collection device and an_
_indication of if it can be enabled/disabled along with what role can do so. The evaluator shall_
_confirm that the AGD guidance describes how to perform the enable/disable function._


_**Tests**_
_The evaluator shall perform the following tests:_

_Test 75: The evaluator shall exercise the TSF configuration as the administrator and, if not_
_restricted to the administrator, the user, to enable and disable the state of each audio or_
_visual collection devices (e.g. camera, microphone) listed by the ST author. For each_
_collection device, the evaluator shall disable the device and then attempt to use its_
_functionality. The evaluator shall reboot the TOE and verify that disabled collection devices_
_may not be used during or early in the boot process. Additionally, the evaluator shall boot_
_the device into each available auxiliary boot mode and verify that the collection device_
_cannot be used._
_Test 76: [conditional] If "on a per-app basis" is selected, the evaluator shall create two_
_applications and enable one to use access the A/V device and the other to not access the_
_A/V device. The evaluator shall exercise each application attempting to access the A/V_
_device individually. The evaluator shall verify that the enabled application is able to access_
_the A/V device and the disabled application is not able to access the A/V device._
_Test 77: [conditional] If "on a per-group of applications processes basis" is selected, the_
_evaluator shall create two applications and the applications shall be placed into different_
_groups. Enable one group to access the A/V device and the other to not access the A/V_
_device. The evaluator shall exercise each application attempting to access the A/V device_
_individually. The evaluator shall verify that the application in the enabled group is able to_
_access the A/V device and the application in the disabled group is not able to access the A/V_
_device._


_**Function 6**_
_**Tests**_
_The evaluator shall use the test environment to instruct the TSF, both as a user and as the_
_administrator, to command the device to transition to a locked state, and verify that the device_
_transitions to the locked state upon command._


_**Function 7**_
_**Tests**_
_The evaluator shall use the test environment to instruct the TSF, both as a user and as the_
_administrator, to command the device to perform a wipe of protected data. The evaluator must_
_ensure that this management setup is used when conducting the Evaluation Activities in_
_FCS_CKM_EXT.5._


_**Function 8**_
_**TSS**_
_The evaluator shall verify the TSS describes the allowable application installation policy options_
_based on the selection included in the ST. If the application allowlist is selected, the evaluator_
_shall verify that the TSS includes a description of each application characteristic upon which the_
_allowlist may be based._


_**Tests**_
_The evaluator shall exercise the TSF configuration as the administrator to restrict particular_
_applications, sources of applications, or application installation according to the AGD guidance._
_The evaluator shall attempt to install unauthorized applications and ensure that this is not_
_possible. The evaluator shall, in conjunction, perform the following specific tests:_

_Test 78: [conditional] The evaluator shall attempt to connect to an unauthorized repository_
_in order to install applications._
_Test 79: [conditional] The evaluator shall attempt to install two applications (one_
_allowlisted, and one not) from a known allowed repository and verify that the application_
_not on the allowlist is rejected. The evaluator shall also attempt to side-load executables or_


_installation packages via USB connections to determine that the white list is still adhered to_


_**Functions 9/10**_
_**TSS**_
_The evaluator shall verify that the TSS describes each category of keys or secrets that can be_
_imported into the TSF’s secure key storage._


_**Tests**_
_The test of these functions is performed in association with FCS_STG_EXT.1._


_**Function 11**_
_**Guidance**_
_The evaluator shall review the AGD guidance to determine that it describes the steps needed to_
_import, modify, or remove certificates in the Trust Anchor database, and that the users that have_
_authority to import those certificates (e.g., only administrator, or both administrators and users)_
_are identified._


_**Tests**_
_The evaluator shall import certificates according to the AGD guidance as the user or as the_
_administrator, as determined by the administrative guidance. The evaluator shall verify that no_
_errors occur during import. The evaluator should perform an action requiring use of the X.509v3_
_certificate to provide assurance that installation was completed properly._


_**Function 12**_
_**TSS**_
_The evaluator shall verify that the TSS describes each additional category of X.509 certificates_
_and their use within the TSF._


_**Tests**_
_The evaluator shall remove an administrator-imported certificate and any other categories of_
_certificates included in the assignment of function 14 from the Trust Anchor Database according_
_to the AGD guidance as the user and as the administrator._


_**Function 13**_
_**TSS**_
_The evaluator shall examine the TSS to ensure that it contains a description of each_
_management function that will be enforced by the enterprise once the device is enrolled. The_
_evaluator shall examine the AGD guidance to determine that this same information is present._


_**Tests**_
_The evaluator shall verify that user approval is required to enroll the device into management._


_**Function 14**_
_**TSS**_
_The evaluator shall verify that the TSS includes an indication of what applications (e.g., user-_
_installed applications, Administrator-installed applications, or Enterprise applications) can be_
_removed along with what role can do so. The evaluator shall examine the AGD guidance to_
_determine that it details, for each type of application that can be removed, the procedures_
_necessary to remove those applications and their associated data. For the purposes of this_
_Evaluation Activity, "associated data" refers to data that are created by the app during its_
_operation that do not exist independent of the app's existence, for instance, configuration data,_
_or e-mail information that’s part of an e-mail client. It does not, on the other hand, refer to data_
_such as word processing documents (for a word processing app) or photos (for a photo or_
_camera app)._


_**Tests**_
_The evaluator shall attempt to remove applications according to the AGD guidance and verify_
_that the TOE no longer permits users to access those applications or their associated data._


_**Function 15**_
_**Tests**_
_The evaluator shall attempt to update the TSF system software following the procedures in the_
_AGD guidance and verify that updates correctly install and that the version numbers of the_
_system software increase._


_**Function 16**_
_**Tests**_
_The evaluator shall attempt to install an application following the procedures in the AGD_
_guidance and verify that the application is installed and available on the TOE._


_**Function 17**_
_**Tests**_
_The evaluator shall attempt to remove any Enterprise applications from the device by following_
_the administrator guidance. The evaluator shall verify that the TOE no longer permits users to_


_access those applications or their associated data._


_**Function 18**_
_**Guidance**_
_The evaluator shall examine the AGD Guidance to determine that it specifies, for at least each_
_category of information selected for Function 18, how to enable and disable display information_
_for that type of information in the locked state._


_**Tests**_
_For each category of information listed in the AGD guidance, the evaluator shall verify that when_
_that TSF is configured to limit the information according to the AGD, the information is no_
_longer displayed in the locked state._


_**Function 19**_
_**Tests**_
_The evaluator shall exercise the TSF configuration as the administrator and, if not restricted to_
_the administrator, the user, to enable system-wide data-at-rest protection according to the AGD_
_guidance. The evaluator shall ensure that all Evaluation Activities for DAR (FDP_DAR) are_
_conducted with the device in this configuration._


_**Function 20**_
_**Tests**_
_The evaluator shall exercise the TSF configuration as the administrator and, if not restricted to_
_the administrator, the user, to enable removable media’s data-at-rest protection according to the_
_AGD guidance. The evaluator shall ensure that all Evaluation Activities for DAR (FDP_DAR) are_
_conducted with the device in this configuration._


_**Function 21**_
_**Tests**_
_The evaluator shall perform the following tests._

_Test 80: The evaluator shall enable location services device-wide and shall verify that an_
_application (such as a mapping application) is able to access the TOE’s location information._
_The evaluator shall disable location services device-wide and shall verify that an application_
_(such as a mapping application) is unable to access the TOE’s location information._
_Test 81: [conditional] If on a per-app basis is selected, the evaluator shall create two_
_applications and enable one to use access the location services and the other to not access_
_the location services. The evaluator shall exercise each application attempting to access_
_location services individually. The evaluator shall verify that the enabled application is able_
_to access the location services and the disabled application is not able to access the location_
_services._


_**Function 22 [CONDITIONAL]**_
_**Tests**_
_The evaluator shall verify that the TSS states if the TOE supports a BAF or hybrid_
_authentication. If the TOE does not include a BAF or hybrid authentication this test is implicitly_
_met._

_Test 82: [conditional] If biometric in accordance with the Biometric Enrollment and_
_[Verification, version 1.1 is selected in FIA_UAU.5.1, for each BAF claimed in](https://github.com/biometricITC/cPP-biometrics/blob/master/Protection%20Profile/BiocPP.adoc)_
_[FIA_MBV_EXT.1.1 in the Biometric Enrollment and Verification, version 1.1 the evaluator](https://github.com/biometricITC/cPP-biometrics/blob/master/Protection%20Profile/BiocPP.adoc)_
_shall verify that the TSS describes the procedure to enable/disable the BAF. The evaluator_
_shall configure the TOE to allow each supported BAF to authenticate and verify that_
_successful authentication can be achieved using the BAF. The evaluator shall configure the_
_TOE to disable the use of each supported BAF for authentication and confirm that the BAF_
_cannot be used to authenticate._
_Test 83: [conditional] If hybrid is selected the evaluator shall verify that the TSS describes_
_the procedure to enable/disable the hybrid (biometric credential and PIN/password)_
_authentication. The evaluator shall configure the TOE to allow hybrid authentication to_
_authenticate and confirm that successful authentication can be achieved using the hybrid_
_authentication. The evaluator shall configure the TOE to disable the use of hybrid_
_authentication and confirm that the hybrid authentication cannot be used to authenticate._


_**Function 23 [CONDITIONAL]**_
_**Tests**_
_The test of this function is performed in conjunction with FIA_X509_EXT.2.2, FCS_TLSC_EXT.1.3_
_[in the Functional Package for Transport Layer Security (TLS), version 1.1.](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=439&id=439)_


_**Function 24 [CONDITIONAL]**_
_**TSS**_
_The evaluator shall verify that the TSS includes a list of each externally accessible hardware port_
_and an indication of if data transfer over that port can be enabled/disabled. AGD guidance will_
_describe how to perform the enable/disable function._


_**Tests**_


_The evaluator shall exercise the TSF configuration to enable and disable data transfer_
_capabilities over each externally accessible hardware ports (e.g. USB, SD card, HDMI) listed by_
_the ST author. The evaluator shall use test equipment for the particular interface to ensure that_
_while the TOE may continue to receive data on the RX pins, it is not responding on TX pins used_
_for data transfer when they are disabled. For each disabled data transfer capability, the_
_evaluator shall repeat this test by rebooting the device into the normal operational mode and_
_verifying that the capability is disabled throughout the boot and early execution stage of the_
_device._


_**Function 25 [CONDITIONAL]**_
_**TSS**_
_The evaluator shall verify that the TSS describes how the TSF acts as a server in each of the_
_protocols listed in the ST, and the reason for acting as a server._


_**Tests**_
_The evaluator shall attempt to disable each listed protocol in the assignment. The evaluator shall_
_verify that remote devices can no longer access the TOE or TOE resources using any disabled_
_protocols._


_**Function 26 [CONDITIONAL]**_
_**Tests**_
_The evaluator shall exercise the TSF configuration as the administrator and, if not restricted to_
_the administrator, the user, to enable and disable any developer mode. The evaluator shall test_
_that developer mode access is not available when its configuration is disabled. The evaluator_
_shall verify the developer mode remains disabled during device reboot._


_**Function 27 [CONDITIONAL]**_
_**Guidance**_
_The evaluator shall examine the AGD guidance to determine that it describes how to enable and_
_disable any "Forgot Password", password hint, or remote authentication (to bypass local_
_authentication mechanisms) capability._


_**Tests**_
_For each mechanism listed in the AGD guidance that provides a "Forgot Password" feature or_
_other means where the local authentication process can be bypassed, the evaluator shall disable_
_the feature and ensure that they are not able to bypass the local authentication process._


_**Function 28 [CONDITIONAL]**_
_**Tests**_
_The evaluator shall attempt to wipe Enterprise data resident on the device according to the_
_administrator guidance. The evaluator shall verify that the data is no longer accessible by the_
_user._


_**Function 29 [CONDITIONAL]**_
_**TSS**_
_The evaluator shall verify that the TSS describes how approval for an application to perform the_
_selected action (import, removal) with respect to certificates in the Trust Anchor Database is_
_accomplished (e.g., a pop-up, policy setting, etc.)._


_The evaluator shall also verify that the API documentation provided according to Section 5.2.2_
_Class ADV: Development includes any security functions (import, modification, or destruction of_
_the Trust Anchor Database) allowed by applications._


_**Tests**_
_The evaluator shall perform one of the following tests:_

_Test 84: [conditional] If applications may import certificates to the Trust Anchor Database,_
_the evaluator shall write, or the developer shall provide access to, an application that_
_imports a certificate into the Trust Anchor Database. The evaluator shall verify that the TOE_
_requires approval before allowing the application to import the certificate:_

_The evaluator shall deny the approvals to verify that the application is not able to_
_import the certificate. Failure of import shall be tested by attempting to validate a_
_certificate that chains to the certificate whose import was attempted (as described in_
_the evaluation activity for FIA_X509_EXT.1)._
_The evaluator shall repeat the test, allowing the approval to verify that the application_
_is able to import the certificate and that validation occurs._

_Test 85: [conditional] If applications may remove certificates in the Trust Anchor Database,_
_the evaluator shall write, or the developer shall provide access to, an application that_
_removes certificates from the Trust Anchor Database. The evaluator shall verify that the_
_TOE requires approval before allowing the application to remove the certificate:_

_The evaluator shall deny the approvals to verify that the application is not able to_
_remove the certificate. Failure of removal shall be tested by attempting to validate a_
_certificate that chains to the certificate whose removal was attempted (as described in_
_the evaluation activity for FIA_X509_EXT.1)._

_The evaluator shall repeat the test, allowing the approval to verify that the application is able to_


_remove/modify the certificate and that validation no longer occurs._


_**Function 30 [CONDITIONAL]**_
_**Tests**_
_The test of this function is performed in conjunction with FIA_X509_EXT.2.2._


_**Function 31 [CONDITIONAL]**_
_**TSS**_
_The evaluator shall ensure that the TSS describes which cellular protocols can be disabled._


_**Guidance**_
_The evaluator shall confirm that the AGD guidance describes the procedure for disabling each_
_cellular protocol identified in the TSS._


_**Tests**_
_The evaluator shall attempt to disable each cellular protocol according to the administrator_
_guidance. The evaluator shall attempt to connect the device to a cellular network and, using_
_network analysis tools, verify that the device does not allow negotiation of the disabled_
_protocols._


_**Function 32 [CONDITIONAL]**_
_**Tests**_
_The evaluator shall attempt to read any device audit logs according to the administrator_
_guidance and verify that the logs may be read. This test may be performed in conjunction with_
_the evaluation activity of FAU_GEN.1._


_**Function 33 [CONDITIONAL]**_
_**Tests**_
_The test of this function is performed in conjunction with FPT_TUD_EXT.5.1._


_**Function 34 [CONDITIONAL]**_
_**TSS**_
_The evaluator shall verify that the TSS describes how the approval for exceptions for shared use_
_of keys or secrets by multiple applications is accomplished (e.g., a pop-up, policy setting, etc.)._


_**Tests**_
_The test of this function is performed in conjunction with FCS_STG_EXT.1._


_**Function 35 [CONDITIONAL]**_
_**TSS**_
_The evaluator shall verify that the TSS describes how the approval for exceptions for destruction_
_of keys or secrets by applications that did not import the key or secret is accomplished (e.g., a_
_pop-up, policy setting, etc.)._


_**Tests**_
_The test of this function is performed in conjunction with FCS_STG_EXT.1._


_**Function 36**_
_**TSS**_
_The evaluator shall verify that the TSS describes any restrictions in banner settings (e.g.,_
_character limitations)._


_**Tests**_
_The test of this function is performed in conjunction with FTA_TAB.1._


_**Function 37 [CONDITIONAL]**_
_**Tests**_
_The test of this function is performed in conjunction with FAU_SEL.1._


_**Function 38 [CONDITIONAL]**_
_**Tests**_
_The test of this function is performed in conjunction with FPT_NOT_EXT.2.1._


_**Function 39 [CONDITIONAL]**_
_**TSS**_
_The evaluator shall verify that the TSS includes a description of how data transfers can be_
_managed over USB._


_**Tests**_
_The evaluator shall perform the following tests based on the selections made in the table:_

_Test 86: [conditional] The evaluator shall disable USB mass storage mode, attach the device_
_to a computer, and verify that the computer cannot mount the TOE as a drive. The_
_evaluator shall reboot the TOE and repeat this test with other supported auxiliary boot_
_modes._


_Test 87: [conditional] The evaluator shall disable USB data transfer without user_
_authentication, attach the device to a computer, and verify that the TOE requires user_
_authentication before the computer can access TOE data. The evaluator shall reboot the_
_TOE and repeat this test with other supported auxiliary boot modes._
_Test 88: [conditional] The evaluator shall disable USB data transfer without connecting_
_system authentication, attach the device to a computer, and verify that the TOE requires_
_connecting system authentication before the computer can access TOE data. The evaluator_
_shall then connect the TOE to another computer and verify that the computer cannot access_
_TOE data. The evaluator shall then connect the TOE to the original computer and verify that_
_the computer can access TOE data._


_**Function 40 [CONDITIONAL]**_
_**TSS**_
_The evaluator shall verify that the TSS includes a description of available backup methods that_
_can be enabled/disabled. If "selected applications" or "selected groups of applications" are_
_selected the TSS shall include which applications of groups of applications backup can be_
_enabled/disabled._


_**Tests**_
_If all applications is selected, the evaluator shall disable each selected backup location in turn_
_and verify that the TOE cannot complete a backup. The evaluator shall then enable each selected_
_backup location in turn and verify that the TOE can perform a backup._


_If selected applications is selected, the evaluator shall disable each selected backup location in_
_turn and verify that for the selected application the TOE prevents backup from occurring. The_
_evaluator shall then enable each selected backup location in turn and verify that for the selected_
_application the TOE can perform a backup._


_If selected groups of applications is selected, the evaluator shall disable each selected backup_
_location in turn and verify that for a group of applications the TOE prevents the backup from_
_occurring. The evaluator shall then enable each selected backup location in turn and verify for_
_the group of application the TOE can perform a backup._


_If configuration data is selected, the evaluator shall disable each selected backup location in_
_turn and verify that the TOE prevents the backup of configuration data from occurring. The_
_evaluator shall then enable each selected backup location in turn and verify that the TOE can_
_perform a backup of configuration data._


_**Function 41 [CONDITIONAL]**_
_**TSS**_
_The evaluator shall verify that the TSS includes a description of Hotspot functionality and USB_
_tethering to include any authentication for these._


_**Tests**_
_The evaluator shall perform the following tests based on the selections in Function 41._

_Test 89: [conditional] The evaluator shall enable hotspot functionality with each of the of_
_the support authentication methods. The evaluator shall connect to the hotspot with_
_another device and verify that the hotspot functionality requires the configured_
_authentication method._
_Test 90: [conditional] The evaluator shall enable USB tethering functionality with each of_
_the of the support authentication methods. The evaluator shall connect to the TOE over_
_USB with another device and verify that the tethering functionality requires the configured_
_authentication method._


_**Function 42 [CONDITIONAL]**_
_**Tests**_
_The test of this function is performed in conjunction with FDP_ACF_EXT.1.2._


_**Function 43 [CONDITIONAL]**_
_**Tests**_
_The evaluator shall set a policy to cause a designated application to be placed into a particular_
_application group. The evaluator shall then install the designated application and verify that it_
_was placed into the correct group._


_**Function 44 [CONDITIONAL]**_
_**Tests**_
_The evaluator shall attempt to unenroll the device from management and verify that the steps_
_described in FMT_SMF_EXT.2.1 are performed. This test should be performed in conjunction_
_with the FMT_SMF_EXT.2.1 evaluation activity._


_**Function 45 [CONDITIONAL]**_
_**TSS**_
_The evaluator shall verify that the TSS contains guidance to configure the VPN as Always-On._


_**Tests**_
_The evaluator shall configure the VPN as Always-On and perform the following tests:_

_Test 91: The evaluator shall verify that when the VPN is connected all traffic is routed_
_through the VPN. This test is performed in conjunction with FDP_IFC_EXT.1.1._
_Test 92: The evaluator shall verify that when the VPN is not established, that no traffic_
_leaves the device. The evaluator shall ensure that the TOE has network connectivity and_
_that the VPN is established. The evaluator shall use a packet sniffing tool to capture the_
_traffic leaving the TOE. The evaluator shall disable the VPN connection on the server side._
_The evaluator shall perform actions with the device such as navigating to websites, using_
_provided applications, and accessing other Internet resources and verify that no traffic_
_leaves the device._
_Test 93: The evaluator shall verify that the TOE has network connectivity and that the VPN_
_is established. The evaluator shall disable network connectivity (i.e. Airplane Mode) and_
_verify that the VPN disconnects. The evaluator shall re-establish network connectivity and_
_verify that the VPN automatically reconnects._


_**Function 46 [CONDITIONAL]**_
_**TSS**_
_The evaluator shall verify that the TSS describes the procedure to revoke a biometric credential_
_stored on the TOE._


_**Tests**_
_The evaluator shall configure the TOE to use BAF and confirm that the biometric can be used to_
_authenticate to the device. The evaluator shall revoke the biometric credential’s ability to_
_authenticate to the TOE and confirm that the same BAF cannot be used to authenticate to the_
_device._


_**Function 47 [CONDITIONAL]**_
_**TSS**_
_The evaluator shall verify that the TSS describes all assigned security management functions_
_and their intended behavior._


_**Tests**_
_The evaluator shall design and perform tests to demonstrate that the function may be configured_
_and that the intended behavior of the function is enacted by the TOE._


**FMT_SMF_EXT.2 Specification of Remediation Actions**


FMT_SMF_EXT.2.1

The TSF shall offer [ **selection** : _wipe of protected data_, _wipe of sensitive data_,
_remove Enterprise applications_, _remove all device-stored Enterprise resource_
_data_, _remove Enterprise secondary authentication data_, _[_ _**assignment**_ _: list other_
_available remediation actions]_ ] upon unenrollment and [ **selection** :

_[_ _**assignment**_ _: other administrator-configured triggers]_, _no other triggers_ ].


**Application Note:** Unenrollment may consist of removing the MDM agent or
removing the administrator’s policies. The functions in the selection are
remediation actions that TOE may provide (perhaps via APIs) to the
administrator (perhaps via an MDM agent) that may be performed upon
unenrollment. "Enterprise applications" refers to applications that are in the
Enterprise application group. "Enterprise resource data" refers to all stored
Enterprise data and the separate resources that are available to the Enterprise
application group, per FDP_ACF_EXT.2.1. If FDP_ACF_EXT.2.1 is included in the
ST, then "remove all device-stored Enterprise resource data" must be selected,
and is defined to be all resources selected in FDP_ACF_EXT.2.1. If
FIA_UAU_EXT.4.1 is included in the ST, then "remove Enterprise secondary
authentication data" must be selected. If FIA_UAU_EXT.4.1 is not included in the
ST, then "remove Enterprise secondary authentication data" cannot be selected.
Enterprise secondary authentication data only refers to any data stored on the
TOE that is specifically used as part of a secondary authentication mechanism to
authenticate access to Enterprise applications and shared resources. Material
that is used for the TOE’s primary authentication mechanism or other purposes
not related to authentication to or protection of Enterprise applications or
shared resources should not be removed.


Protected data is all non-TSF data, including all user or enterprise data. Some or
all of this data may be considered sensitive data as well. If wipe of protected
data is selected it is assumed that the sensitive data is wiped as well. However, if
wipe of sensitive data is selected, it does not imply that all non-TSF data
(protected data) is wiped. If wipe of protected data or wipe of sensitive data is
selected the wipe must be in accordance with FCS_CKM_EXT.5.1. Thus
cryptographically wiping the device is an acceptable remediation action.


**Evaluation Activities**


**5.1.8 Class: Protection of the TSF (FPT)**


**FPT_AEX_EXT.1 Application Address Space Layout Randomization**


FPT_AEX_EXT.1.1

The TSF shall provide address space layout randomization ASLR to applications.


FPT_AEX_EXT.1.2

The base address of any user-space memory mapping will consist of at least 8
unpredictable bits.


**Application Note:** The 8 unpredictable bits may be provided by the TSF RBG
(as specified in FCS_RBG_EXT.1) but is not required.


**Evaluation Activities**





**FPT_AEX_EXT.2 Memory Page Permissions**


FPT_AEX_EXT.2.1

The TSF shall be able to enforce read, write, and execute permissions on every
page of physical memory.


**Evaluation Activities**


_FPT_AEX_EXT.2_
_**TSS**_
_The evaluator shall ensure that the TSS describes of the memory management unit (MMU), and_
_ensures that this description documents the ability of the MMU to enforce read, write, and_
_execute permissions on all pages of virtual memory._


_**Guidance**_
_There are no guidance evaluation activities for this component._


_**Tests**_
_There are no test evaluation activities for this component._


**FPT_AEX_EXT.3 Stack Overflow Protection**


FPT_AEX_EXT.3.1

TSF processes that execute in a non-privileged execution domain on the
application processor shall implement stack-based buffer overflow protection.


**Application Note:** A "non-privileged execution domain" refers to the user mode
(as opposed to kernel mode, for instance) of the processor. While not all TSF
processes must implement such protection, it is expected that most of the
processes (to include libraries used by TSF processes) do implement buffer
overflow protections.


**Evaluation Activities**





**FPT_AEX_EXT.4 Domain Isolation**


FPT_AEX_EXT.4.1

The TSF shall protect itself from modification by untrusted subjects.


FPT_AEX_EXT.4.2

The TSF shall enforce isolation of address space between applications.


**Application Note:** In addition to the TSF software (e.g., kernel image, device
drivers, trusted applications) that resides in storage, the execution context (e.g.,
address space, processor registers, per-process environment variables) of the
software operating in a privileged mode of the processor (e.g., kernel), as well as
the context of the trusted applications is to be protected. In addition to the
software, any configuration information that controls or influences the behavior
of the TSF is also to be protected from modification by untrusted subjects.


Configuration information includes, but is not limited to, user and administrative
management function settings, WLAN profiles, and Bluetooth data such as the
service-level security requirements database.


Untrusted subjects include untrusted applications; unauthorized users who have
access to the device while powered off, in a screen-locked state, or when booted
into auxiliary boot modes; and, unauthorized users or untrusted software or
hardware which may have access to the device over a wired interface, either
when the device is in a screen-locked state or booted into auxiliary boot modes.


**Evaluation Activities**


_FPT_AEX_EXT.4_
_**TSS**_
_The evaluator shall ensure that the TSS describes the mechanisms that are in place that_
_prevents non-TSF software from modifying the TSF software or TSF data that governs the_
_behavior of the TSF. These mechanisms could range from hardware-based means (e.g._
_"execution rings" and memory management functionality); to software-based means (e.g._
_boundary checking of inputs to APIs). The evaluator determines that the described mechanisms_
_appear reasonable to protect the TSF from modification._


_The evaluator shall ensure the TSS describes how the TSF ensures that the address spaces of_
_applications are kept separate from one another._


_The evaluator shall ensure the TSS details the USSD and MMI codes available from the dialer at_
_the locked state or during auxiliary boot modes that may alter the behavior of the TSF. The_
_evaluator shall ensure that this description includes the code, the action performed by the TSF,_
_and a justification that the actions performed do not modify user or TSF data. If no USSD or_
_MMI codes are available, the evaluator shall ensure that the TSS provides a description of the_
_method by which actions prescribed by these codes are prevented._


_The evaluator shall ensure the TSS documents any TSF data (including software, execution_
_context, configuration information, and audit logs) which may be accessed and modified over a_
_wired interface in auxiliary boot modes. The evaluator shall ensure that the description includes_
_data, which is modified in support of update or restore of the device. The evaluator shall ensure_
_that this documentation includes the auxiliary boot modes in which the data may be modified,_
_the methods for entering the auxiliary boot modes, the location of the data, the manner in which_
_data may be modified, the data format and packaging necessary to support modification, and_
_software or hardware tools, if any, which are necessary for modifying the data._


_The evaluator shall ensure that the TSS provides a description of the means by which_
_unauthorized and undetected modification (that is, excluding cryptographically verified updates_
_per FPT_TUD_EXT.2) of the TSF data over the wired interface in auxiliary boots modes is_
_prevented. The lack of publicly available tools is not sufficient justification. Examples of_
_sufficient justification include auditing of changes, cryptographic verification in the form of a_
_digital signature or hash, disabling the auxiliary boot modes, and access control mechanisms_
_that prevent writing to files or flashing partitions._


_**Guidance**_
_There are no guidance evaluation activities for this component._


_**Tests**_
_**Evaluation Activity Note:**_ _The following tests require the vendor to provide access to a test_
_platform that provides the evaluator with tools that are typically not found on consumer Mobile_
_Device products. In addition, the vendor provides a list of files (e.g., system files, libraries,_
_configuration files, audit logs) that make up the TSF data. This list could be organized by_
_folders/directories (e.g., /usr/sbin, /etc), as well as individual files that may exist outside of the_
_identified directories._

_Test 95: The evaluator shall create and load an app onto the Mobile Device. This app shall_
_attempt to traverse over all file systems and report any locations to which data can be_
_written or overwritten. The evaluator must ensure that none of these locations are part of_
_the OS software, device drivers, system and security configuration files, key material, or_
_another untrusted application’s image/data. For example, it is acceptable for a trusted_
_photo editor app to have access to the data created by the camera app, but a calculator_
_application shall not have access to the pictures._


_Test 96: For each available auxiliary boot mode, the evaluator shall attempt to modify a TSF_
_file of their choosing using the software or hardware tools described in the TSS. The_
_evaluator shall verify that the modification fails._


**FPT_JTA_EXT.1 JTAG Disablement**


FPT_JTA_EXT.1.1

The TSF shall [ **selection** : _disable access through hardware_, _control access by a_
_signing key_ ] to JTAG.


**Application Note:** This requirement means that access to JTAG must be
disabled either through hardware or restricted through the use of a signing key.


**Evaluation Activities**


_FPT_JTA_EXT.1_
_**TSS**_
_If disable access through hardware is selected:_
_The evaluator shall examine the TSS to determine the location of the JTAG ports on the TSF, to_
_include the order of the ports (i.e. Data In, Data Out, Clock, etc.)._


_If control access by a signing key is selected:_
_The evaluator shall examine the TSS to determine how access to the JTAG is controlled by a_
_signing key. The evaluator shall examine the TSS to determine when the JTAG can be accessed,_
_i.e. what has the access to the signing key._


_**Guidance**_
_There are no guidance evaluation activities for this component._


_**Tests**_
_**Evaluation Activity Note:**_ _The following test requires the developer to provide access to a test_
_platform that provides the evaluator with chip level access._


_If disable access through hardware is selected:_
_The evaluator shall connect a packet analyzer to the JTAG ports. The evaluator shall query the_
_JTAG port for its device ID and confirm that the device ID cannot be retrieved._


**FPT_KST_EXT.1 Key Storage**


FPT_KST_EXT.1.1

The TSF shall not store any plaintext key material in readable non-volatile
memory.


**Application Note:** The intention of this requirement is that the TOE will not
write plaintext keying material to persistent storage. For the purposes of this
requirement, keying material refers to authentication data, passwords,
secret/private symmetric keys, private asymmetric keys, data used to derive
keys, etc. These values must be stored encrypted.


This requirement also applies to any value derived from passwords. Thus, the
TOE cannot store plaintext password hashes for comparison purposes before
protected data is decrypted, and the TOE should use key derivation and
decryption to verify the Password Authentication Factor.


If hybrid is selected in FIA_UAU.5.1 keying material also refers to the
PIN/password used as part of the hybrid authentication.


**Evaluation Activities**


**FPT_KST_EXT.2 No Key Transmission**


FPT_KST_EXT.2.1

The TSF shall not transmit any plaintext key material outside the security
boundary of the TOE.


**Application Note:** The intention of this requirement is to prevent the logging of
plaintext key information to a service that transmits the information off-device.
For the purposes of this requirement, key material refers to keys, passwords,
and other material that is used to derive keys.


If hybrid is selected in FIA_UAU.5.1 keying material also refers to the


PIN/password used as part of the hybrid authentication.


In the future, this requirement will apply to symmetric and asymmetric private
keys stored in the TOE secure key storage where applications are outside the
boundary of the TOE. Thus, the TSF will be required to provide cryptographic
key operations (signature, encryption, and decryption) on behalf of applications
(FCS_SRV_EXT.2.1) that have access to those keys.


**Evaluation Activities**





**FPT_KST_EXT.3 No Plaintext Key Export**


FPT_KST_EXT.3.1

The TSF shall ensure it is not possible for the TOE users to export plaintext keys.


**Application Note:** Plaintext keys include DEKs, KEKs, and all keys stored in the
secure key storage (FCS_STG_EXT.1). The intent of this requirement is to
prevent the plaintext keys from being exported during a backup authorized by
the TOE user or administrator.


**Evaluation Activities**







**FPT_NOT_EXT.1 Self-Test Notification**


FPT_NOT_EXT.1.1

The TSF shall transition to non-operational mode and [ **selection** : _log failures in_
_the audit record_, _notify the administrator_, _[_ _**assignment**_ _: other actions]_, _no other_
_actions_ ] when the following types of failures occur:

failures of the self-tests
TSF software integrity verification failures

[ **selection** : _no other failures_, _[_ _**assignment**_ _: other failures]_ ]


**Evaluation Activities**


**FPT_STM.1 Reliable Time Stamps**


FPT_STM.1.1

The TSF shall be able to provide reliable time stamps **for its own use** .


**Evaluation Activities**







**FPT_TST_EXT.1 TSF Cryptographic Functionality Testing**


FPT_TST_EXT.1.1

The TSF shall run a suite of self-tests during initial start-up (on power on) to
demonstrate the correct operation of all cryptographic functionality.


**Application Note:** This requirement may be met by performing known answer
tests or pair-wise consistency tests. The self-tests must be performed before the
cryptographic functionality is exercised (for example, during the initialization of
a process that utilizes the functionality).


The cryptographic functionality includes the cryptographic operations in
FCS_COP, the key generation functions in FCS_CKM, and the random bit
generation in FCS_RBG_EXT.


**Evaluation Activities**


_FPT_TST_EXT.1_
_**TSS**_
_The evaluator shall examine the TSS to ensure that it specifies the self-tests that are performed_
_at start-up. This description must include an outline of the test procedures conducted by the TSF_
_(e.g., rather than saying "memory is tested", a description similar to "memory is tested by_
_writing a value to each memory location and reading it back to ensure it is identical to what was_
_written" shall be used). The TSS must include any error states that they TSF may enter when_
_self-tests fail, and the conditions and actions necessary to exit the error states and resume_
_normal operation. The evaluator shall verify that the TSS indicates these self-tests are run at_
_start-up automatically, and do not involve any inputs from or actions by the user or operator._


_The evaluator shall inspect the list of self-tests in the TSS and verify that it includes algorithm_
_self-tests. The algorithm self-tests will typically be conducted using known answer tests._


_**Guidance**_
_There are no guidance evaluation activities for this component._


_**Tests**_
_There are no test evaluation activities for this component._


**FPT_TST_EXT.2/PREKERNEL TSF Integrity Checking (Pre-Kernel)**


FPT_TST_EXT.2.1/PREKERNEL

The TSF shall verify the integrity of [ _the bootchain up through the Application_
_Processor OS kernel_ ] stored in mutable media prior to its execution through the
use of [ **selection** : _a digital signature using an immutable hardware asymmetric_
_key_, _an immutable hardware hash of an asymmetric key_, _an immutable hardware_
_hash_, _a digital signature using a hardware-protected asymmetric key_ ].


**Application Note:** The bootchain of the TSF is the sequence of firmware and
software, including ROM, bootloaders, and kernel, which ultimately result in
loading the kernel on the Application Processor, regardless of which processor
executes that code. Executable code that would be loaded after the kernel is
covered in FPT_TST_EXT.2/POSTKERNEL.


In order to meet this requirement, the hardware protection may be transitive in
nature: a hardware-protected public key, hash of an asymmetric key, or hash
may be used to verify the mutable bootloader code which contains a key or hash
used by the bootloader to verify the mutable OS kernel code, which contains a
key or hash to verify the next layer of executable code, and so on.


The cryptographic mechanism used to verify the (initial) mutable executable
code must be protected, such as being implemented in hardware or in read-only
memory (ROM).


**Evaluation Activities**


_FPT_TST_EXT.2/PREKERNEL_
_**TSS**_
_The evaluator shall verify that the TSS section of the ST includes a description of the boot_
_procedures, including a description of the entire bootchain, of the software for the TSF’s_
_Application Processor. The evaluator shall ensure that before loading the bootloaders for the_
_operating system and the kernel, all bootloaders and the kernel software itself is_
_cryptographically verified. For each additional category of executable code verified before_
_execution, the evaluator shall verify that the description in the TSS describes how that software_
_is cryptographically verified._


_The evaluator shall verify that the TSS contains a justification for the protection of the_
_cryptographic key or hash, preventing it from being modified by unverified or unauthenticated_
_software. The evaluator shall verify that the TSS contains a description of the protection_
_afforded to the mechanism performing the cryptographic verification._


_The evaluator shall verify that the TSS describes each auxiliary boot mode available on the TOE_
_during the boot procedures. The evaluator shall verify that, for each auxiliary boot mode, a_
_description of the cryptographic integrity of the executed code through the kernel is verified_
_before each execution._


_**Guidance**_
_There are no guidance evaluation activities for this component._


_**Tests**_
_**Evaluation Activity Note:**_ _The following tests require the vendor to provide access to a test_
_platform that provides the evaluator with tools that are typically not found on consumer Mobile_
_Device products._


_The evaluator shall perform the following tests:_

_Test 99: The evaluator shall perform actions to cause TSF software to load and observe that_
_the integrity mechanism does not flag any executables as containing integrity errors and_
_that the TOE properly boots._


_Test 100: The evaluator shall modify a TSF executable that is integrity protected and cause_
_that executable to be successfully loaded by the TSF. The evaluator observes that an_
_integrity violation is triggered and the TOE does not boot. (Care must be taken so that the_
_integrity violation is determined to be the cause of the failure to load the module, and not_
_the fact that the module was modified so that it was rendered unable to run because its_
_format was corrupt)._


_Test 101: [conditional] If the ST author indicates that the integrity verification is performed_
_using a public key, the evaluator shall verify that the update mechanism includes a_
_certificate validation according to FIA_X509_EXT.1. The evaluator shall digitally sign the_
_TSF executable with a certificate that does not have the Code Signing purpose in the_
_extendedKeyUsage field and verify that an integrity violation is triggered. The evaluator_
_shall repeat the test using a certificate that contains the Code Signing purpose and verify_
_that the integrity verification succeeds. Ideally, the two certificates should be identical_
_except for the extendedKeyUsage field._


**FPT_TUD_EXT.1 TSF Version Query**


FPT_TUD_EXT.1.1

The TSF shall provide authorized users the ability to query the current version of
the TOE firmware/software.


FPT_TUD_EXT.1.2

The TSF shall provide authorized users the ability to query the current version of
the hardware model of the device.


**Application Note:** The current version of the hardware model of the device is
an identifier that is sufficient to indicate (in tandem with manufacturer
documentation) the hardware which comprises the device.


FPT_TUD_EXT.1.3

The TSF shall provide authorized users the ability to query the current version of
installed mobile applications.


**Application Note:** The current version of mobile applications is the name and
published version number of each installed mobile application.


**Evaluation Activities**





**FPT_TUD_EXT.2 TSF Update Verification**


FPT_TUD_EXT.2.1

The TSF shall verify software updates to the Application Processor system
software and [ **selection** : _[_ _**assignment**_ _: other processor system software]_, _no_
_other processor system software_ ] using a digital signature verified by the
manufacturer trusted key prior to installing those updates.


**Application Note:** The digital signature mechanism is implemented in
accordance with FCS_COP.1.1/SIGN.


At this time, this requirement does not require verification of software updates
to the software operating outside the Application Processor.


Any change, via a supported mechanism, to software residing in non-volatile
storage is deemed a software update. Thus, this requirement applies to TSF
software updates regardless of how the software arrives or is delivered to the


FPT_TUD_EXT.2.2


FPT_TUD_EXT.2.3



device. This includes over-the-air (OTA) updates as well as partition images
containing software which may be delivered to the device over a wired interface.


The TSF shall [ **selection** : _never update_, _update only by verified software_ ] the
TSF boot integrity [ **selection** : _key_, _hash_ ].


**Application Note:** The key or hash updated via this requirement is used for
verifying software before execution in FPT_TST_EXT.2/PREKERNEL. The key or
hash is verified as a part of the digital signature on an update, and the software
which performs the update of the key or hash is verified by
FPT_TST_EXT.2/PREKERNEL.


The TSF shall verify that the digital signature verification key used for TSF
updates [ **selection** : _is validated to a public key in the Trust Anchor Database_,
_matches an immutable hardware public key_ ].


**Application Note:** The ST author must indicate the method by which the
signing key for system software updates is limited and, if selected in
FPT_TUD_EXT.2.3, must indicate how this signing key is protected by the
hardware.


If certificates are used, certificates are validated for the purpose of software
updates in accordance with FIA_X509_EXT.1 and should be selected in
FIA_X509_EXT.2.1. Additionally, FPT_TUD_EXT.4 must be included in the ST.



**Evaluation Activities**




**FPT_TUD_EXT.3 Application Signing**


FPT_TUD_EXT.3.1

The TSF shall verify mobile application software using a digital signature
mechanism prior to installation.


**Application Note:** This requirement does not necessitate an X.509v3 certificate
or certificate validation. X.509v3 certificates and certificate validation are
addressed in FPT_TUD_EXT.5.1.


**Evaluation Activities**


**5.1.9 Class: TOE Access (FTA)**


**FTA_SSL_EXT.1 TSF- and User-initiated Locked State**


FTA_SSL_EXT.1.1

The TSF shall transition to a locked state after a time interval of inactivity.


FTA_SSL_EXT.1.2

The TSF shall transition to a locked state after initiation by either the user or the
administrator.


FTA_SSL_EXT.1.3

The TSF shall, upon transitioning to the locked state, perform the following
operations:

Clearing or overwriting display devices, obscuring the previous contents;

[ **assignment** : _Other actions performed upon transitioning to the locked_
_state_ ].


**Application Note:** The time interval of inactivity is configured using
FMT_SMF.1 function 2. The user/administrator-initiated lock is specified in
FMT_SMF.1 function 6.


**Evaluation Activities**


_FTA_SSL_EXT.1_
_**TSS**_
_The evaluator shall verify the TSS describes the actions performed upon transitioning to the_
_locked state._


_**Guidance**_
_The evaluation shall verify that the AGD guidance describes the method of setting the inactivity_
_interval and of commanding a lock. The evaluator shall verify that the TSS describes the_
_information allowed to be displayed to unauthorized users._


_**Tests**_

_Test 108: The evaluator shall configure the TSF to transition to the locked state after a time_
_of inactivity (FMT_SMF.1) according to the AGD guidance. The evaluator shall wait until the_
_TSF locks and verify that the display is cleared or overwritten and that the only actions_
_allowed in the locked state are unlocking the session and those actions specified in_
_FIA_UAU_EXT.2._


_Test 109: The evaluator shall command the TSF to transition to the locked state according_
_to the AGD guidance as both the user and the administrator. The evaluator shall wait until_
_the TSF locks and verify that the display is cleared or overwritten and that the only actions_
_allowed in the locked state are unlocking the session and those actions specified in_
_FIA_UAU_EXT.2._


**FTA_TAB.1 Default TOE Access Banners**


FTA_TAB.1.1

Before establishing a user session, the TSF shall display an advisory warning
message regarding unauthorized use of the TOE.


**Application Note:** This requirement may be met with the configuration of
either text or an image containing the text of the desired message. The TSF must
minimally display this information at startup, but may also display the
information at every unlock. The banner is configured according to FMT_SMF.1
function 36.


**Evaluation Activities**





**5.1.10 Class: Trusted Path/Channels (FTP)**


**FTP_ITC_EXT.1 Trusted Channel Communication**


FTP_ITC_EXT.1.1

The TSF shall use

802.11-2012 in accordance with the [ _PP-Module for Wireless LAN Clients,_
_version 1.0_ ],
802.1X in accordance with the [ _PP-Module for Wireless LAN Clients,_
_version 1.0_ ],
EAP-TLS in accordance with the [ _PP-Module for Wireless LAN Clients,_
_version 1.0_ ],
Mutually authenticated TLS in accordance with [ _the Functional Package for_
_Transport Layer Security (TLS), version 1.1_ ]

and [ **selection** :

_IPsec in accordance with the PP-Module for Virtual Private Network (VPN)_
_Clients, version 2.4_
_mutually authenticated DTLS as defined in the Functional Package for_
_[Transport Layer Security (TLS), version 1.1](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=439&id=439)_
_HTTPS_

] protocols to provide a communication channel between itself and another
trusted IT product that is logically distinct from other communication channels,
provides assured identification of its end points, protects channel data from
disclosure, and detects modification of the channel data.


**Application Note:** The intent of the mandatory portion of the above
requirement is to use the cryptographic protocols identified in the requirement
to establish and maintain a trusted channel between the TOE and an access
point, VPN Gateway, or other trusted IT product.


The ST author must list which trusted channel protocols are implemented by the
Mobile Device.


The TSF must be validated against the PP-Module for Wireless LAN Clients,
version 1.0 to satisfy the mandatory trusted channels of 802.11-2012, 802.1X,
and EAP-TLS.


FTP_ITC_EXT.1.2


FTP_ITC_EXT.1.3



To satisfy the mandatory trusted channel of TLS and if mutually authenticated
DTLS is selected, the TSF must be validated against the Functional Package for
Transport Layer Security (TLS), version 1.1, with the following selections made:

FCS_TLS_EXT.1:

Either TLS or DTLS is selected as appropriate
Client is selected

FCS_TLSC_EXT.1.1 or FCS_DTLSC_EXT.1.1 (as appropriate):

The cipher suites selected must correspond with the algorithms and
hash functions allowed in FCS_COP.1.
Mutual authentication must be selected

FCS_TLSC_EXT.1.3 or FCS_DTLSC_EXT.1.3 (as appropriate):

With no exceptions is selected.


[If the ST author selects IPsec, the TSF must be validated against the PP-Module](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=467&id=467)
for Virtual Private Network (VPN) Clients, version 2.4.


Appendix B - Selection-based Requirements contains the requirements for
implementing each of the other optional trusted channel protocols. The ST
author must include the security functional requirements for the trusted channel
protocol selected in FTP_ITC_EXT.1 in the main body of the ST.


Assured identification of endpoints is performed according to the authentication
mechanisms used by the listed trusted channel protocols.


Validation Guidelines:


**Rule #13**


**Rule #14**


The TSF shall permit the TSF to initiate communication via the trusted channel.


The TSF shall initiate communication via the trusted channel for wireless access
point connections, administrative communication, configured enterprise
connections, and [ **selection** : _OTA updates_, _no other connections_ ].



**Evaluation Activities**




**5.1.11 TOE Security Functional Requirements Rationale**
The following rationale provides justification for each security objective for the TOE, showing that the SFRs
are suitable to meet and achieve the security objectives:


**Table 8: SFR Rationale**

**Objective** **Addressed by** **Rationale**
























FCS_CKM_EXT.7 (Sel-Based) FCS_CKM_EXT.7 supports the objective by ensuring that


the TOE's root encryption key cannot be disclosed.


FMT_SMF_EXT.2 FMT_SMF_EXT.2 supports the objective by defining the


configuration actions that the TSF performs
automatically upon unenrollment from mobile device
management.


FAU_STG.4 FAU_STG.4 supports the objective by ensuring the


availability of audit records.








FPT_TUD_EXT.1 FPT_TUD_EXT.1 supports the objective by allowing
users to determine the version of the TOE's hardware,
software/firmware, and installed applications.


**5.2 Security Assurance Requirements**


The Security Objectives in Section 4 Security Objectives were constructed to address threats identified in
Section 3 Security Problem Description. The Security Functional Requirements (SFRs) in Section 5.1 Security
Functional Requirements are a formal instantiation of the Security Objectives. The PP identifies the Security
Assurance Requirements (SARs) to frame the extent to which the evaluator assesses the documentation
applicable for the evaluation and performs independent testing.


This section lists the set of SARs from CC part 3 that are required in evaluations against this PP. Individual
Evaluation Activities to be performed are specified both in Section 5.1 Security Functional Requirements as
well as in this section.


The general model for evaluation of TOEs against STs written to conform to this PP is as follows:


After the ST has been approved for evaluation, the ITSEF will obtain the TOE, supporting environmental IT,
and the administrative/user guides for the TOE. The ITSEF is expected to perform actions mandated by the
Common Evaluation Methodology (CEM) for the ASE and ALC SARs. The ITSEF also performs the Evaluation
Activities contained within Section 5.1 Security Functional Requirements, which are intended to be an
interpretation of the other CEM evaluation requirements as they apply to the specific technology instantiated
in the TOE. The Evaluation Activities that are captured in Section 5.1 Security Functional Requirements also
provide clarification as to what the developer needs to provide to demonstrate the TOE is compliant with the
PP.


The TOE Security Assurance Requirements are identified in Table 9.


Table 9: Security Assurance Requirements


**Assurance Class** **Assurance Components**


Security Target (ASE) Conformance Claims (ASE_CCL.1)


Extended Components Definition (ASE_ECD.1)


ST Introduction (ASE_INT.1)


Security Objectives for the Operational Environment (ASE_OBJ.1)


Stated Security Requirements (ASE_REQ.1)


Security Problem Definition (ASE_SPD.1)


TOE Summary Specification (ASE_TSS.1)


Development (ADV) Basic Functional Specification (ADV_FSP.1)





Tests (ATE) Independent Testing – Sample (ATE_IND.1)


Vulnerability Assessment (AVA) Vulnerability Survey (AVA_VAN.1)


**5.2.1 Class ASE: Security Target**
The ST is evaluated as per ASE activities defined in the CEM for ASE_CCL.1, ASE_ECD.1, ASE_INT.1,
ASE_OBJ.1, ASE_REQ.1, ASE_SPD.1, and ASE_TSS.1. In addition, there may be Evaluation Activities specified
within Section 5.1 Security Functional Requirements that call for necessary descriptions to be included in the
TSS that are specific to the TOE technology type.


**5.2.2 Class ADV: Development**

The design information about the TOE is contained in the guidance documentation available to the end user
as well as the TSS portion of the ST, and any additional information required by this PP that is not to be made
public.


**ADV_FSP.1 Basic Functional Specification**


The functional specification describes the TOE Security Functions Interface (TSFIs). It is not
necessary to have a formal or complete specification of these interfaces. Additionally, because TOEs
conforming to this PP will necessarily have interfaces to the Operational Environment that are not
directly invokable by TOE users, there is little point specifying that such interfaces be described in
and of themselves since only indirect testing of such interfaces may be possible. For this PP, the
activities for this family should focus on understanding the interfaces presented in the TSS in
response to the functional requirements and the interfaces presented in the AGD documentation. No
additional "functional specification" documentation is necessary to satisfy the evaluation activities
specified.


The interfaces that need to be evaluated are characterized through the information needed to
perform the evaluation activities listed, rather than as an independent, abstract list.


**Developer action elements:**


ADV_FSP.1.1D

The developer shall provide a functional specification.


ADV_FSP.1.2D

The developer shall provide a tracing from the functional specification to the
SFRs.


**Application Note:** As indicated in the introduction to this section, the functional
specification is comprised of the information contained in the AGD_OPE,
AGD_PRE, and the API information that is provided to application developers,
including the APIs that require privilege to invoke.


The developer may reference a website accessible to application developers and
the evaluator. The API documentation must include those interfaces required in
this profile. The API documentation must clearly indicate to which products and
versions each available function applies.


The evaluation activities in the functional requirements point to evidence that
should exist in the documentation and TSS section; since these are directly
associated with the SFRs, the tracing in element ADV_FSP.1.2D is implicitly
already done and no additional documentation is necessary.


**Content and presentation elements:**


ADV_FSP.1.1C


ADV_FSP.1.2C


ADV_FSP.1.3C


ADV_FSP.1.4C



The functional specification shall describe the purpose and method of use for
each SFR-enforcing and SFR-supporting TSFI.


The functional specification shall identify all parameters associated with each
SFR-enforcing and SFR-supporting TSFI.


The functional specification shall provide rationale for the implicit categorization
of interfaces as SFR-non-interfering.


The tracing shall demonstrate that the SFRs trace to TSFIs in the functional
specification.



**Evaluator action elements:**


ADV_FSP.1.1E

The evaluator shall confirm that the information provided meets all requirements
for content and presentation of evidence.


ADV_FSP.1.2E

The evaluator shall determine that the functional specification is an accurate and
complete instantiation of the SFRs.


**Evaluation Activities**





**5.2.3 Class AGD: Guidance Documentation**
The guidance documents will be provided with the ST. Guidance must include a description of how the IT
personnel verifies that the Operational Environment can fulfill its role for the security functionality. The
documentation should be in an informal style and readable by the IT personnel.


Guidance must be provided for every operational environment that the product supports as claimed in the ST.
This guidance includes:

Instructions to successfully install the TSF in that environment
Instructions to manage the security of the TSF as a product and as a component of the larger operational
environment
Instructions to provide a protected administrative capability


Guidance pertaining to particular security functionality is also provided; requirements on such guidance are
contained in the evaluation activities specified with each requirement.


**AGD_OPE.1 Operational User Guidance**


**Developer action elements:**


AGD_OPE.1.1D

The developer shall provide operational user guidance.


**Application Note:** The operational user guidance does not have to be contained
in a single document. Guidance to users, administrators and application
developers can be spread among documents or web pages. Where appropriate,
the guidance documentation is expressed in the eXtensible Configuration
Checklist Description Format (XCCDF) to support security automation.


Rather than repeat information here, the developer should review the evaluation
activities for this component to ascertain the specifics of the guidance that the
evaluator will be checking for. This will provide the necessary information for
the preparation of acceptable guidance.


**Content and presentation elements:**


AGD_OPE.1.1C

The operational user guidance shall describe, for each user role, the useraccessible functions and privileges that should be controlled in a secure
processing environment, including appropriate warnings.


AGD_OPE.1.2C


AGD_OPE.1.3C


AGD_OPE.1.4C


AGD_OPE.1.5C


AGD_OPE.1.6C


AGD_OPE.1.7C



**Application Note:** User and administrator (e.g., MDM agent), and application
developer are to be considered in the definition of user role.


The operational user guidance shall describe, for each user role, how to use the
available interfaces provided by the TOE in a secure manner.


The operational user guidance shall describe, for each user role, the available
functions and interfaces, in particular all security parameters under the control
of the user, indicating secure values as appropriate.


The operational user guidance shall, for each user role, clearly present each type
of security-relevant event relative to the user-accessible functions that need to
be performed, including changing the security characteristics of entities under
the control of the TSF.


The operational user guidance shall identify all possible modes of operation of
the OS (including operation following failure or operational error), their
consequences, and implications for maintaining secure operation.


The operational user guidance shall, for each user role, describe the security
measures to be followed in order to fulfill the security objectives for the
operational environment as described in the ST.


The operational user guidance shall be clear and reasonable.



**Evaluator action elements:**


AGD_OPE.1.1E

The evaluator shall confirm that the information provided meets all requirements
for content and presentation of evidence.


**Evaluation Activities**





**AGD_PRE.1 Preparative Procedures**


**Developer action elements:**


AGD_PRE.1.1D

The developer shall provide the TOE, including its preparative procedures.


**Application Note:** As with the operational guidance, the developer should look
to the evaluation activities to determine the required content with respect to
preparative procedures.


**Content and presentation elements:**


AGD_PRE.1.1C

The preparative procedures shall describe all the steps necessary for secure


AGD_PRE.1.2C



acceptance of the delivered TOE in accordance with the developer's delivery
procedures.


The preparative procedures shall describe all the steps necessary for secure
installation of the TOE and for the secure preparation of the operational
environment in accordance with the security objectives for the operational
environment as described in the ST.



**Evaluator action elements:**


AGD_PRE.1.1E

The evaluator shall confirm that the information provided meets all requirements
for content and presentation of evidence.


AGD_PRE.1.2E

The evaluator shall apply the preparative procedures to confirm that the OS can
be prepared securely for operation.


**Evaluation Activities**





**5.2.4 Class ALC: Life-cycle Support**

At the assurance level provided for TOEs conformant to this PP, life-cycle support is limited to end-uservisible aspects of the life-cycle, rather than an examination of the TOE vendor’s development and
configuration management process. This is not meant to diminish the critical role that a developer’s practices
play in contributing to the overall trustworthiness of a product; rather, it is a reflection on the information to
be made available for evaluation at this assurance level.


**ALC_CMC.1 Labeling of the TOE**


This component is targeted at identifying the TOE such that it can be distinguished from other
products or versions from the same vendor and can be easily specified when being procured by an
end user.


**Developer action elements:**


ALC_CMC.1.1D

The developer shall provide the TOE and a reference for the TOE.


**Content and presentation elements:**


ALC_CMC.1.1C

The TOE shall be labeled with a unique reference.


**Evaluator action elements:**


ALC_CMC.1.1E

The evaluator shall confirm that the information provided meets all requirements
for content and presentation of evidence.


**Evaluation Activities**





**ALC_CMS.1 TOE CM Coverage**


Given the scope of the TOE and its associated evaluation evidence requirements, this component’s
evaluation activities are covered by the evaluation activities listed for ALC_CMC.1.


**Developer action elements:**


ALC_CMS.1.1D

The developer shall provide a configuration list for the TOE.


**Content and presentation elements:**


ALC_CMS.1.1C

The configuration list shall include the following: the TOE itself; and the
evaluation evidence required by the SARs.


ALC_CMS.1.2C

The configuration list shall uniquely identify the configuration items.


**Evaluator action elements:**


ALC_CMS.1.1E

The evaluator shall confirm that the information provided meets all requirements
for content and presentation of evidence.


**Application Note:** The "evaluation evidence required by the SARs" in this PP is
limited to the information in the ST coupled with the guidance provided to
administrators and users under the AGD requirements. By ensuring that the TOE
is specifically identified and that this identification is consistent in the ST and in
the AGD guidance (as done in the evaluation activity for ALC_CMC.1), the
evaluator implicitly confirms the information required by this component.


Life-cycle support is targeted aspects of the developer’s life-cycle and
instructions to providers of applications for the developer’s devices, rather than
an in-depth examination of the TSF manufacturer’s development and
configuration management process. This is not meant to diminish the critical
role that a developer’s practices play in contributing to the overall
trustworthiness of a product; rather, it’s a reflection on the information to be
made available for evaluation.


**Evaluation Activities**


**ALC_TSU_EXT.1 Timely Security Updates**


This component requires the TOE developer, in conjunction with any other necessary parties, to
provide information as to how the end-user devices are updated to address security issues in a timely
manner. The documentation describes the process of providing updates to the public from the time a
security flaw is reported/discovered, to the time an update is released. This description includes the
parties involved (e.g., the developer, cellular carriers) and the steps that are performed (e.g.,
developer testing, carrier testing), including worst-case time periods, before an update is made
available to the public.


**Developer action elements:**


ALC_TSU_EXT.1.1D

The developer shall provide a description in the TSS of how timely security
updates are made to the TOE.


**Content and presentation elements:**


ALC_TSU_EXT.1.1C

The description shall include the process for creating and deploying security
updates for the TOE software.


**Note:** The software to be described includes the operating systems of the
application processor and the baseband processor, as well as any firmware and
applications. The process description includes the TOE developer processes as
well as any third-party (carrier) processes. The process description includes each
deployment mechanism (e.g., over-the-air updates, per-carrier updates,
downloaded updates).


ALC_TSU_EXT.1.2C

The description shall express the time window as the length of time, in days,
between public disclosure of a vulnerability and the public availability of security
updates to the TOE.


ALC_TSU_EXT.1.3C


ALC_TSU_EXT.1.4C



**Note:** The total length of time may be presented as a summation of the periods
of time that each party (e.g., TOE developer, mobile carrier) on the critical path
consumes. The time period until public availability per deployment mechanism
may differ; each is described.


The description shall include the mechanisms publicly available for reporting
security issues pertaining to the TOE.


**Note:** The reporting mechanism could include web sites, email addresses, as
well as a means to protect the sensitive nature of the report (e.g., public keys
that could be used to encrypt the details of a proof-of-concept exploit).


The description shall include where users can seek information about the
availability of new updates including details (e.g. CVE identifiers) of the specific
public vulnerabilities corrected by each update.


**Note:** The purpose of providing this information is so that users and enterprises
can determine which devices are susceptible to publicly known vulnerabilities so
that they can make appropriate risk decisions, such as limiting access to
enterprise resources until updates are installed.



**Evaluator action elements:**


ALC_TSU_EXT.1.1E

The evaluator shall confirm that the information provided meets all requirements
for content and presentation of evidence.


**Evaluation Activities**





**5.2.5 Class ATE: Tests**
Testing is specified for functional aspects of the system as well as aspects that take advantage of design or
implementation weaknesses. The former is done through the ATE_IND family, while the latter is through the
AVA_VAN family. At the assurance level specified in this PP, testing is based on advertised functionality and
interfaces with dependency on the availability of design information. One of the primary outputs of the
evaluation process is the test report as specified in the following requirements.


Since many of the APIs are not exposed at the user interface (e.g., touch screen), the ability to stimulate the
necessary interfaces requires a developer’s test environment. This test environment will allow the evaluator,
for example, to access APIs and view file system information that is not available on consumer Mobile
Devices.


**ATE_IND.1 Independent Testing – Conformance**


Testing is performed to confirm the functionality described in the TSS as well as the administrative
(including configuration and operational) documentation provided. The focus of the testing is to
confirm that the requirements specified in Section 5.1 Security Functional Requirements being met,
although some additional testing is specified for SARs in Section 5.2 Security Assurance
Requirements. The Evaluation Activities identify the additional testing activities associated with


these components. The evaluator produces a test report documenting the plan for and results of
testing, as well as coverage arguments focused on the platform/TOE combinations that are claiming
conformance to this PP.


**Developer action elements:**


ATE_IND.1.1D

The developer shall provide the TOE for testing.


**Content and presentation elements:**


ATE_IND.1.1C

The TOE shall be suitable for testing.


**Evaluator action elements:**


ATE_IND.1.1E

The evaluator shall confirm that the information provided meets all requirements
for content and presentation of evidence.


ATE_IND.1.2E

The evaluator shall test a subset of the TSF to confirm that the TSF operates as
specified.


**Evaluation Activities**


**5.2.6 Class AVA: Vulnerability Assessment**

For the current generation of this protection profile, the evaluation lab is expected to survey open sources to
discover what vulnerabilities have been discovered in these types of products. In most cases, these
vulnerabilities will require sophistication beyond that of a basic attacker. Until penetration tools are created
and uniformly distributed to the evaluation labs, the evaluator will not be expected to test for these
vulnerabilities in the TOE. The labs will be expected to comment on the likelihood of these vulnerabilities
given the documentation provided by the vendor. This information will be used in the development of
penetration testing tools and for the development of future protection profiles.


**AVA_VAN.1 Vulnerability Survey**


**Developer action elements:**


AVA_VAN.1.1D

The developer shall provide the TOE for testing.


**Content and presentation elements:**


AVA_VAN.1.1C


The TOE shall be suitable for testing.


**Evaluator action elements:**


AVA_VAN.1.1E

The evaluator shall confirm that the information provided meets all requirements
for content and presentation of evidence.


AVA_VAN.1.2E

The evaluator shall perform a search of public domain sources to identify
potential vulnerabilities in the TOE.


**Application Note:** Public domain sources include the Common Vulnerabilities
and Exposures (CVE) dictionary for publicly-known vulnerabilities.


AVA_VAN.1.3E

The evaluator shall conduct penetration testing, based on the identified potential
vulnerabilities, to determine that the TOE is resistant to attacks performed by an
attacker possessing Basic attack potential.


**Evaluation Activities**


# **Appendix A - Optional Requirements**

As indicated in the introduction to this PP, the baseline requirements (those that must be performed by the
TOE) are contained in the body of this PP. This appendix contains three other types of optional requirements
that may be included in the ST, but are not required in order to conform to this PP. However, applied
modules, packages and/or use cases may refine specific requirements as mandatory.


The first type (A.1 Strictly Optional Requirements) are strictly optional requirements that are independent of
the TOE implementing any function. If the TOE fulfills any of these requirements or supports a certain
functionality, the vendor is encouraged to include the SFRs in the ST, but are not required in order to
conform to this PP.


The second type (A.2 Objective Requirements) are objective requirements that describe security functionality
not yet widely available in commercial technology. The requirements are not currently mandated in the body
of this PP, but will be included in the baseline requirements in future versions of this PP. Adoption by vendors
is encouraged and expected as soon as possible.


The third type (A.3 Implementation-dependent Requirements) are dependent on the TOE implementing a
particular function. If the TOE fulfills any of these requirements, the vendor must either add the related SFR
or disable the functionality for the evaluated configuration.


**A.1 Strictly Optional Requirements**


**A.1.1 Class: Identification and Authentication (FIA)**


**FIA_UAU_EXT.4 Secondary User Authentication**


FIA_UAU_EXT.4.1

The TSF shall provide a secondary authentication mechanism for accessing
Enterprise applications and resources. The secondary authentication mechanism
shall control access to the Enterprise application and shared resources and shall
be incorporated into the encryption of protected and sensitive data belonging to
Enterprise applications and shared resources.


**Application Note:** For the BYOD use case, Enterprise applications and data
must be protected using a different password than the user authentication to
gain access to the personal applications and data, if configured.


This requirement must be included in the ST if the TOE implements a container
solution, in which there is a separate authentication, to separate user and
Enterprise applications and resources.


FIA_UAU_EXT.4.2

The TSF shall require the user to present the secondary authentication factor
prior to decryption of Enterprise application data and Enterprise shared
resource data.


**Application Note:** The intent of this requirement is to prevent decryption of
protected Enterprise application data and Enterprise shared resource data
before the user has authenticated to the device using the secondary
authentication factor. Enterprise shared resource data consists of the
FDP_ACF_EXT.2.1 selections.


**Evaluation Activities**


_FIA_UAU_EXT.4.1_
_**TSS**_
_There are no TSS evaluation activities for this element._


_**Guidance**_
_There are no guidance evaluation activities for this element._


_**Tests**_
_The Evaluation Activities for any selected requirements related to device authentication must be_
_separately performed for the secondary authentication mechanism (in addition to activities_
_performed for the primary authentication mechanism). The requirements are:_
_FIA_UAU.6/CREDENTIAL, FIA_UAU.6/LOCKED, FIA_PMG_EXT.1, FIA_TRT_EXT.1, FIA_UAU.7,_
_FIA_UAU_EXT.2, FTA_SSL_EXT.1, FCS_STG_EXT.2, FMT_SMF.1/FMT_MOF_EXT.1 #1, #2, #8,_
_#21, and #36._


_Additionally, FIA_AFL_EXT.1 must be met, except that in FIA_AFL_EXT.1.2 the separate test is_
_performed with the text "wipe of all protected data" changed to "wipe of all Enterprise_
_application data and all Enterprise shared resource data."_


_FIA_UAU_EXT.4.2_
_**TSS**_
_The evaluator shall verify that the TSS section of the ST describes the process for decrypting_
_Enterprise application data and shared resource data. The evaluator shall ensure that this_
_process requires the user to enter an Authentication Factor and, in accordance with_
_FCS_CKM_EXT.3, derives a KEK which is used to protect the software-based secure key storage_
_and (optionally) DEKs for sensitive data, in accordance with FCS_STG_EXT.2._


_**Guidance**_
_There are no guidance evaluation activities for this element._


_**Tests**_
_There are no test evaluation activities for this element._


**A.2 Objective Requirements**


**A.2.1 Class: Security Audit (FAU)**


**FAU_SEL.1 Selective Audit**


FAU_SEL.1.1

The TSF shall be able to select the set of events to be audited from the set of all
auditable events based on the following attributes: [ **selection** :

_[event type]_

_[success of auditable security events_
_failure of auditable security events_

_[_ _**assignment**_ _: other attributes]]_

].


**Application Note:** The intent of this requirement is to identify all criteria that
can be selected to trigger an audit event. This can be configured through an
interface on the TSF for a user or administrator to invoke. For the ST author, the
assignment is used to list any additional criteria or "none".


**Evaluation Activities**


**A.2.2 Class: Cryptographic Support (FCS)**


**FCS_RBG_EXT.2 Random Bit Generator State Preservation**


FCS_RBG_EXT.2.1

The TSF shall save the state of the deterministic RBG at power-off, and shall use
this state as input to the deterministic RBG at startup.


**Application Note:** The capability to add the state saved at power-off as input to
the RBG prevents an RBG that is slow to gather entropy from producing the
same output regularly and across reboots. Since there is no guarantee of the
protections provided when the state is stored (or a requirement for any such
protection), it is assumed that the state is 'known', and therefore cannot
contribute entropy to the RBG, but can introduce enough variation that the
initial RBG values are not predictable and exploitable.


**Evaluation Activities**





**FCS_RBG_EXT.3 Support for Personalization String**


FCS_RBG_EXT.3.1

The TSF shall allow applications to add data to the deterministic RBG using the
Personalization String as defined in SP 800-90A.


**Application Note:** As specified in SP 800-90A, the TSF must not count data
input from an application towards the entropy required by FCS_RBG_EXT.1.
Thus, the TSF must not allow the only input to the RBG seed to be from an
application.


**Evaluation Activities**







**FCS_SRV_EXT.2 Cryptographic Key Storage Services**


FCS_SRV_EXT.2.1

The TSF shall provide a mechanism for applications to request the TSF to
perform the following cryptographic operations: [

_Algorithms in FCS_COP.1/ENCRYPT_
_Algorithms in FCS_COP.1/SIGN_

] by keys stored in the secure key storage.


**Application Note:** The TOE will, therefore, be required to perform
cryptographic operations on behalf of applications using the keys stored in the
TOE’s secure key storage.


**Evaluation Activities**


**A.2.3 Class: User Data Protection (FDP)**


**FDP_ACF_EXT.3 Security Attribute Based Access Control**


FDP_ACF_EXT.3.1

The TSF shall enforce an access control policy that prohibits an application from
granting both write and execute permission to a file on the device except for

[ **selection** : _files stored in the application's private data folder_, _no exceptions_ ].


**Evaluation Activities**







**FDP_BCK_EXT.1 Application Backup**


FDP_BCK_EXT.1.1

The TSF shall provide a mechanism for applications to mark [ **selection** : _all_
_application data_, _selected application data_ ] to be excluded from device backups.


**Application Note:** Device backups include any mechanism built into the TOE
that allows stored application data to be extracted over a physical port or sent
over the network, but does not include any functionality implemented by a
specific application itself if the application is not included in the TOE. The lack of
a public/documented API for performing backups, when a private/undocumented
API exists, is not sufficient to meet this requirement.


**Evaluation Activities**


_FDP_BCK_EXT.1_
_**TSS**_
_There are no TSS evaluation activities for this component._


_**Guidance**_
_There are no guidance evaluation activities for this component._


_**Tests**_
_If all application data is selected, the evaluator shall install an application that has marked all of_
_its application data to be excluded from backups. The evaluator shall cause data to be placed_
_into the application’s storage area. The evaluator shall attempt to back up the application data_
_and verify that the backup fails or that the application’s data was not included in the backup._


_If selected application data is selected, the evaluator shall install an application that has marked_
_selected application data to be excluded from backups. The evaluator shall cause data covered_
_by "selected application data" to be placed into the application’s storage area. The evaluator_
_shall attempt to backup that selected application data and verify that either the backup fails or_
_that the selected data is excluded from the backup._


**FDP_BLT_EXT.1 Limitation of Bluetooth Device Access**


FDP_BLT_EXT.1.1

The TSF shall limit the applications that may communicate with a particular
paired Bluetooth device.


**Application Note:** Not every application with privileges to use Bluetooth should
be permitted to communicate with every paired Bluetooth device. For example,
the TSF may choose to require that only the application that initiated the current
connection may communicate with the device, or it may strictly tie the paired
device to the first application that makes a socket connection to the device
following initial pairing. Additionally, for more flexibility, the TSF may choose to
provide the user with a way to select which applications on the device may
communicate with or observe communications with each paired Bluetooth
device.


**Evaluation Activities**





**A.2.4 Class: Identification and Authentication (FIA)**


**FIA_X509_EXT.4 X509 Certificate Enrollment**


FIA_X509_EXT.4.1

The TSF shall use the Enrollment over Secure Transport (EST) protocol as
specified in RFC 7030 to request certificate enrollment using the simple
enrollment method described in RFC 7030 Section 4.2.


FIA_X509_EXT.4.2

The TSF shall be capable of authenticating EST requests using an existing
certificate and corresponding private key as specified by RFC 7030 Section
3.3.2.


FIA_X509_EXT.4.3

The TSF shall be capable of authenticating EST requests using HTTP Basic
Authentication with a username and password as specified by RFC 7030 Section
3.2.3.


FIA_X509_EXT.4.4

The TSF shall perform authentication of the EST server using an Explicit Trust


FIA_X509_EXT.4.5


FIA_X509_EXT.4.6


FIA_X509_EXT.4.7


FIA_X509_EXT.4.8



Anchor following the rules described in RFC 7030, section 3.6.1.


**Application Note:** EST also uses HTTPS as specified in FCS_HTTPS_EXT.1 to
establish a secure connection to an EST server. The separate Trust Anchor
Database dedicated to EST operations is described as Explicit Trust Anchors in
RFC 7030.


The TSF shall be capable of requesting server-provided private keys as specified
in RFC 7030 Section 4.4.


The TSF shall be capable of updating its EST-specific Trust Anchor Database
using the "Root CA Key Update" process described in RFC 7030 Section 4.1.3.


The TSF shall generate a Certificate Request Message for EST as specified in
RFC 2986 and be able to provide the following information in the request: public
key and [ **selection** : _device-specific information_, _Common Name_, _Organization_,
_Organizational Unit_, _Country_ ].


**Application Note:** The public key referenced is the public key portion of the
public-private key pair generated by the TOE as specified in FCS_CKM.1.


The TSF shall validate the chain of certificates from the Root CA certificate in
the Trust Anchor Database to the EST Server CA certificate upon receiving a CA
Certificates Response.



**Evaluation Activities**


_FIA_X509_EXT.4_
_**TSS**_
_There are no TSS evaluation activities for this component._


_**Guidance**_
_The evaluator shall check to ensure that the operational guidance contains instructions on_
_requesting certificates from an EST server, including generating a Certificate Request Message._


_**Tests**_
_The evaluator shall also perform the following tests. Other tests are performed in conjunction_
_with the evaluation activity listed in the Package for Transport Layer Security._

_Test 120: The evaluator shall use the operational guidance to cause the TOE to request_
_certificate enrollment from an EST server using the simple enrollment method described in_
_RFC 7030 Section 4.2, authenticating the certificate request to the server using an existing_
_certificate and private key as described by RFC 7030 Section 3.3.2. The evaluator shall_
_confirm that the resulting certificate is successfully obtained and installed in the TOE key_
_store._


_Test 121: The evaluator shall use the operational guidance to cause the TOE to request_
_certificate enrollment from an EST server using the simple enrollment method described in_
_RFC 7030 Section 4.2, authenticating the certificate request to the server using a username_
_and password as described by RFC 7030 Section 3.2.3. The evaluator shall confirm that the_
_resulting certificate is successfully obtained and installed in the TOE key store._


_Test 122: The evaluator shall modify the EST server to return a certificate containing a_
_different public key than the key included in the TOE’s certificate request. The evaluator_
_shall use the operational guidance to cause the TOE to request certificate enrollment from_
_an EST server. The evaluator shall confirm that the TOE does not accept the resulting_
_certificate since the public key in the issued certificate does not match the public key in the_
_certificate request._


_Test 123: The evaluator shall configure the EST server or use a man-in-the-middle tool to_
_present a server certificate to the TOE that is present in the TOE general Trust Anchor_
_Database but not its EST-specific Trust Anchor Database. The evaluator shall cause the TOE_
_to request certificate enrollment from the EST server. The evaluator shall verify that the_
_request is not successful._


_Test 124: The evaluator shall configure the EST server or use a man-in-the-middle tool to_
_present an invalid certificate. The evaluator shall cause the TOE to request certificate_
_enrollment from the EST server. The evaluator shall verify that the request is not successful_
_The evaluator shall configure the EST server or use a man-in-the-middle tool to present a_
_certificate that does not have the CMC RA purpose and verify that requests to the EST_
_server fail. The tester shall repeat the test using a valid certificate and a certificate that_
_contains the CMC RA purpose and verify that the certificate enrollment requests succeed._


_Test 125: The evaluator shall use a packet sniffing tool between the TOE and an EST server._
_The evaluator shall turn on the sniffing tool and cause the TOE to request certificate_
_enrollment from an EST server. The evaluator shall verify that the EST protocol interaction_
_occurs over a Transport Layer Security (TLS) protected connection. The evaluator is not_
_expected to decrypt the connection but rather observe that the packets conform to the TLS_
_protocol format._


_Test 126: The evaluator shall use the operational guidance to cause the TOE to request a_
_server-provided private key and certificate from an EST server. The evaluator shall confirm_
_that the resulting private key and certificate are successfully obtained and installed in the_
_TOE key store._


_Test 127: The evaluator shall modify the EST server to, in response to a server-provided_
_private key and certificate request, return a private key that does not correspond with the_
_public key in the returned certificate. The evaluator shall use the operational guidance to_
_cause the TOE to request a server-provided private key and certificate. The evaluator shall_
_confirm that the TOE does not accept the resulting private key and certificate since the_
_private key and public key do not correspond._


_Test 128: The evaluator shall configure the EST server to provide a "Root CA Key Update"_
_as described in RFC 7030 Section 4.1.3. The evaluator shall cause the TOE to request CA_
_certificates from the EST server and shall confirm that the EST-specific Trust Anchor_
_Database is updated with the new trust anchor._


_Test 129: The evaluator shall configure the EST server to provide a "Root CA Key Update"_
_as described in RFC 7030 Section 4.1.3, but shall modify part of the NewWithOld_
_certificate’s generated signature. The evaluator shall cause the TOE to request CA_
_certificates from the EST server and shall confirm that the EST-specific Trust Anchor_
_Database is not updated with the new trust anchor since the signature did not verify._


_Test 130: The evaluator shall use the operational guidance to cause the TOE to generate a_
_certificate request message. The evaluator shall capture the generated message and ensure_
_that it conforms to the format specified by RFC 2986. The evaluator shall confirm that the_
_certificate request provides the public key and other required information, including any_
_necessary user-input information._


**FIA_X509_EXT.5 X.509 Certificate Requests**


FIA_X509_EXT.5.1

The TSF shall generate a Certificate Request Message as specified in RFC 2986
and be able to provide the following information in the request: public key and

[ **selection** : _device-specific information_, _Common Name_, _Organization_,
_Organizational Unit_, _Country_ ].


**Application Note:** The public key referenced in FIA_X509_EXT.5.1 is the public
key portion of the public-private key pair generated by the TOE as specified in
FCS_CKM.1. The trusted channel requirements do not apply to communication
with the CA for the certificate request/response messages.


As Enrollment over Secure Transport (EST) is a new standard that has not yet
been widely adopted, this requirement is included as an interim objective
requirement in order to allow developers to distinguish those products which
have do have the ability to generate Certificate Request Messages but do not yet
implement EST.


FIA_X509_EXT.5.2

The TSF shall validate the chain of certificates from the Root CA upon receiving
the CA Certificate Response.


**Evaluation Activities**


_FIA_X509_EXT.5_
_**TSS**_
_If the ST author selects "device-specific information", the evaluator shall verify that the TSS_
_contains a description of the device-specific fields used in certificate requests._


_**Guidance**_
_The evaluator shall check to ensure that the operational guidance contains instructions on_
_generating a Certificate Request Message. If the ST author selects "Common Name",_
_"Organization", "Organizational Unit", or "Country", the evaluator shall ensure that this guidance_
_includes instructions for establishing these fields before creating the certificate request_
_message._


_**Tests**_


_The evaluator shall also perform the following tests:_

_Test 131: The evaluator shall use the operational guidance to cause the TOE to generate a_
_certificate request message. The evaluator shall capture the generated message and ensure_
_that it conforms to the format specified. The evaluator shall confirm that the certificate_
_request provides the public key and other required information, including any necessary_
_user-input information._


_Test 132: The evaluator shall demonstrate that validating a certificate response message_
_without a valid certification path results in the function failing. The evaluator shall then_
_load a certificate or certificates as trusted CAs needed to validate the certificate response_
_message, and demonstrate that the function succeeds. The evaluator shall then delete one_
_of the certificates, and show that the function fails._


**A.2.5 Class: Security Management (FMT)**


**FMT_SMF_EXT.3 Current Administrator**


FMT_SMF_EXT.3.1

The TSF shall provide a mechanism that allows users to view a list of currently
authorized administrators and the management functions that each
administrator is authorized to perform.


**Evaluation Activities**





**A.2.6 Class: Protection of the TSF (FPT)**


**FPT_AEX_EXT.5 Kernel Address Space Layout Randomization**


FPT_AEX_EXT.5.1

The TSF shall provide address space layout randomization (ASLR) to the kernel.


FPT_AEX_EXT.5.2

The base address of any kernel-space memory mapping will consist of

[ **assignment** : _number greater than or equal to 4_ ] unpredictable bits.


**Application Note:** The unpredictable bits may be provided by the TSF RBG (as
specified in FCS_RBG_EXT.1).


**Evaluation Activities**






**FPT_AEX_EXT.6 Write or Execute Memory Page Permissions**


FPT_AEX_EXT.6.1

The TSF shall prevent write and execute permissions from being simultaneously
granted to any page of physical memory [ **selection** : _with no exceptions_,

_[_ _**assignment**_ _: specific exceptions]_ ].


**Application Note:** Memory used for just-in-time (JIT) compilation is anticipated
as an exception in this requirement; if so, the ST author must address how this
exception is permitted. It is expected that the memory management unit will
transition the system to a non-operational state if any violation is detected in
kernel memory space.


**Evaluation Activities**





**FPT_AEX_EXT.7 Heap Overflow Protection**


FPT_AEX_EXT.7.1

The TSF shall include heap-based buffer overflow protections in the runtime
environment it provides to processes that execute on the application processor.


**Application Note:** These heap-based buffer overflow protections are expected
to ensure the integrity of heap metadata such as memory addresses or offsets
recorded by the heap implementation to manage memory blocks. This includes
chunk headers, look-aside lists, and other data structures used to track the state
and location of memory blocks managed by the heap.


**Evaluation Activities**







**FPT_BBD_EXT.1 Application Processor Mediation**


FPT_BBD_EXT.1.1

The TSF shall prevent code executing on any baseband processor (BP) from
accessing application processor (AP) resources except when mediated by the AP.


**Application Note:** These resources include:

Volatile and non-volatile memory


Control of and data from integrated and non-integrated peripherals (e.g.
USB controllers, touch screen controllers, LCD controller, codecs)
Control of and data from integrated and non-integrated I/O sensors (e.g.
camera, light, microphone, GPS, accelerometers, geomagnetic field
sensors)


Mobile devices are becoming increasingly complex having an application
processor that runs an operating system and user applications and separate
baseband processors that handle cellular and other wireless network
connectivity.


The application processor within most modern Mobile Devices is a system
on a chip (SoC) that integrates, for example, CPU/GPU cores and memory
interface electronics into a single, power-efficient package.
Baseband processors are becoming increasingly complex themselves
delivering voice encoding alongside multiple independent radios (LTE, WiFi, Bluetooth, FM, GPS) in a single package containing multiple CPUs and
DSPs.


Thus, the baseband processors in these requirements include such integrated
SoCs and include any radio processors (integrated or not) on the Mobile Device.


All other requirements mostly, except where noted, apply to firmware/software
on the application processor, but future requirements (notably, all Integrity,
Access Control, and Anti-Exploitation requirements) will apply to application
processors and baseband processors.


**Evaluation Activities**





**FPT_BLT_EXT.1 Limitation of Bluetooth Profile Support**


FPT_BLT_EXT.1.1

The TSF shall disable support for [ **assignment** : _list of Bluetooth profiles_ ]
Bluetooth profiles when they are not currently being used by an application on
the Mobile Device, and shall require explicit user action to enable them.


**Application Note:** Some Bluetooth services incur more serious consequences if
unauthorized remote devices gain access to them. Such services should be
protected by measures like disabling support for the associated Bluetooth profile
unless it is actively being used by an application on the Mobile Device (in order
to prevent discovery by a Service Discovery Protocol search), and then requiring
explicit user action to enable those profiles in order to use the services. It may
be further appropriate to require additional user action before granting a remote
device access to that service.


For example, it may be appropriate to disable the OBEX Push Profile until a user
on the Mobile Device pushes a button in an application indicating readiness to
transfer an object. After completion of the object transfer, support for the OBEX
profile should be suspended until the next time the user requests its use.


**Evaluation Activities**


_FPT_BLT_EXT.1_
_**TSS**_
_The evaluator shall ensure that the TSS lists all Bluetooth profiles that are disabled while not in_
_use by an application and which need explicit user action in order to become enabled._


_**Guidance**_
_There are no guidance evaluation activities for this component._


_**Tests**_
_The evaluator shall perform the following tests:_

_Test 134: While the service is not in active use by an application on the TOE, the evaluator_
_shall attempt to discover a service associated with a "protected" Bluetooth profile (as_
_specified by the requirement) on the TOE via a Service Discovery Protocol search. The_
_evaluator shall verify that the service does not appear in the Service Discovery Protocol_
_search results. Next, the evaluator shall attempt to gain remote access to the service from a_
_device that does not currently have a trusted device relationship with the TOE. The_
_evaluator shall verify that this attempt fails due to the unavailability of the service and_
_profile._


_Test 135: The evaluator shall repeat Test 1 with a device that currently has a trusted device_
_relationship with the TOE and verify that the same behavior is exhibited._


**FPT_NOT_EXT.2 Software Integrity Verification**


FPT_NOT_EXT.2.1

The TSF shall [ **selection** : _audit_, _provide the administrator with_ ] TSF-software
integrity verification values.


**Application Note:** These notifications are typically called remote attestation
and these integrity values are typically called measurements. The integrity
values are calculated from hashes of critical memory and values, including
executable code. The ST author must select whether these values are logged as
a part of FAU_GEN.1.1 or are provided to the administrator.


FPT_NOT_EXT.2.2

The TSF shall cryptographically sign all integrity verification values.


**Application Note:** The intent of this requirement is to provide assurance to the
administrator that the responses provided are from the TOE and have not been
modified or spoofed by a man-in-the-middle such as a network-based adversary
or a malicious MDM Agent.


**Evaluation Activities**


_FPT_NOT_EXT.2.1_
_**TSS**_
_The evaluator shall verify that the TSS describes which critical memory is measured for these_
_integrity values and how the measurement is performed (including which TOE software_
_performs these generates these values, how that software accesses the critical memory, and_
_which algorithms are used)._


_**Guidance**_
_If the integrity values are provided to the administrator, the evaluator shall verify that the AGD_
_guidance contains instructions for retrieving these values and information for interpreting them._
_For example, if multiple measurements are taken, what those measurements are and how_
_changes to those values relate to changes in the device state._


_**Tests**_
_**Evaluation Activity Note:**_ _The following test may require the developer to provide access to a_
_test platform that provides the evaluator with tools that are typically not found on consumer_
_Mobile Device products._


_The evaluator shall repeat the following test for each measurement:_

_Test 136: The evaluator shall boot the device in an approved state and record the_
_measurement taken (either from the log or by using the administrative guidance to retrieve_
_the value via an MDM Agent). The evaluator shall modify the critical memory or value that_
_is measured. The evaluator shall boot the device and verify that the measurement changed._


_FPT_NOT_EXT.2.2_
_**TSS**_
_The evaluator shall verify that the TSS describes which key the TSF uses to sign the responses to_
_queries and the certificate used to prove ownership of the key, and the method of associating the_
_certificate with a particular device manufacturer and model._


_**Guidance**_
_There are no guidance evaluation activities for this component._


_**Tests**_
_The evaluator shall perform the following test:_

_Test 137: The evaluator shall write, or the developer shall provide, a management_
_application that queries either the audit logs or the measurements. The evaluator shall_
_verify that the responses to these queries are signed and verify the signatures against the_
_TOE’s certificate._


**FPT_TST_EXT.2/POSTKERNEL TSF Integrity Checking (Post-Kernel)**


FPT_TST_EXT.2.1/POSTKERNEL

The TSF shall verify the integrity of [ **selection** : _all executable code_,

_[_ _**assignment**_ _: subset of executable code]_ ] stored in mutable media prior to its
execution through the use of [ **selection** : _a digital signature using an immutable_
_hardware asymmetric key_, _an immutable hardware hash of an asymmetric key_,
_an immutable hardware hash_, _a digital signature using a hardware-protected_
_asymmetric key_, _hardware-protected hash_ ].


**Application Note:** All executable code covered in this requirement is executed
after the kernel is loaded.


If "all executable code in mutable media" is verified, implementation in hardware
or in read-only memory is a natural logical consequence.


At this time, the verification of software executed on other processors stored in
mutable media is not required; however, it may be added in the first assignment.
If all executable code (including bootloaders, kernel, device drivers, pre-loaded
applications, user-loaded applications, and libraries) is verified, "all executable
code stored in mutable media" should be selected.


**Evaluation Activities**





**FPT_TUD_EXT.5 Application Verification**


FPT_TUD_EXT.5.1

The TSF shall by default only install mobile applications cryptographically
verified by [ **selection** : _a built-in X.509v3 certificate_, _a configured X.509v3_
_certificate_ ].


**Application Note:** The built-in certificate is installed by the manufacturer
either at time of manufacture or as a part of system updates. The configured
certificate used to verify the signature is set according to FMT_SMF.1 function
33.


**Evaluation Activities**


_FPT_TUD_EXT.5_
_**TSS**_
_The evaluator shall verify that the TSS describes how mobile application software is verified at_
_installation. The evaluator shall ensure that this method uses a digital signature by a code_
_signing certificate._


_**Guidance**_
_There are no guidance evaluation activities for this component._


_**Tests**_

_Test 138: The evaluator shall write, or the developer shall provide access to, an application._
_The evaluator shall try to install this application without a digitally signature and shall_


_verify that installation fails. The evaluator shall attempt to install an application digitally_
_signed with an appropriate certificate, and verify that installation succeeds._


_Test 139: The evaluator shall digitally sign the application with an invalid certificate and_
_verify that application installation fails. The evaluator shall digitally sign the application_
_with a certificate that does not have the Code Signing purpose and verify that application_
_installation fails. This test may be performed in conjunction with the Evaluation Activities_
_for FIA_X509_EXT.1._


_Test 140: If necessary, the evaluator shall configure the device to limit the public keys that_
_can sign application software according to the AGD guidance. The evaluator shall digitally_
_sign the application with a certificate disallowed by the device or configuration and verify_
_that application installation fails. The evaluator shall attempt to install an application_
_digitally signed with an authorized certificate and verify that application installation_
_succeeds._


**FPT_TUD_EXT.6 Trusted Update Verification**


FPT_TUD_EXT.6.1

The TSF shall verify that software updates to the TSF are a current or later
version than the current version of the TSF.


**Application Note:** A later version has a larger version number. The method for
distinguishing newer software versions from older versions is determined by the
manufacturer.


**Evaluation Activities**





**A.3 Implementation-dependent Requirements**


**A.3.1 Bluetooth**
If the TOE includes Bluetooth hardware, the following SFRs must be claimed:
If this is implemented by the TOE, the following requirements must be included in the ST:


**FDP_UPC_EXT.1/BLUETOOTH**


**A.3.1.1 Class: User Data Protection (FDP)**


**FDP_UPC_EXT.1/BLUETOOTH Inter-TSF User Data Transfer Protection (Bluetooth)**


FDP_UPC_EXT.1.1/BLUETOOTH

The TSF shall provide a means for non-TSF applications executing on the TOE to
use [

_Bluetooth BR/EDR in accordance with the PP-Module for Bluetooth, version_
_1.0,_

_and [_ _**selection**_ _:_

_[Bluetooth LE in accordance with the PP-Module for Bluetooth, version 1.0](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=425&id=425)_
_no other protocol_


_]_ ] to provide a protected communication channel between the non-TSF
application and another IT product that is logically distinct from other
communication channels, provides assured identification of its end points,
protects channel data from disclosure, and detects modification of the channel
data.


**Application Note:** If the TOE includes Bluetooth hardware, this requirement
must be included in the ST. The intent of this requirement is that Bluetooth
BR/EDR and optionally Bluetooth LE is available for use by user applications
running on the device for use in connecting to distant-end services that are not
necessarily part of the enterprise infrastructure. The ST author must list which
trusted channel protocols are implemented by the Mobile Device for use by nonTSF apps.


[The TSF must be validated against requirements from the PP-Module for](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=425&id=425)
Bluetooth, version 1.0. It should be noted that the FTP_ITC_EXT.1 requires that
all TSF communications be protected using the protocols indicated in that
requirement, so the protocols required by this component ride "on top of" those
listed in FTP_ITC_EXT.1.


FDP_UPC_EXT.1.2/BLUETOOTH

The TSF shall permit the non-TSF applications to initiate communication via the
trusted channel.


**Evaluation Activities**




# **Appendix B - Selection-based Requirements**

As indicated in the introduction to this PP, the baseline requirements (those that must be performed by the
TOE or its underlying platform) are contained in the body of this PP. There are additional requirements based
on selections in the body of the PP: if certain selections are made, then additional requirements below must
be included.


**B.1 Class: Cryptographic Support (FCS)**


**FCS_CKM_EXT.7 Cryptographic Key Support (REK)**


_**The inclusion of this selection-based component depends upon selection in**_
_**FCS_CKM_EXT.1.1.**_


FCS_CKM_EXT.7.1

A REK shall not be able to be read from or exported from the hardware.


**Application Note:** If mutable hardware is selected in FCS_CKM_EXT.1.1,
FCS_CKM_EXT.7 must be included in the ST. Note that if immutable hardware is
selected in FCS_CKM_EXT.1.1 it implicitly meets FCS_CKM_EXT.7.


The lack of a public/documented API for importing or exporting, when a
private/undocumented API exists, is not sufficient to meet this requirement.


**Evaluation Activities**





**B.2 Class: User Data Protection (FDP)**


**FDP_ACF_EXT.2 Access Control for System Resources**


_**The inclusion of this selection-based component depends upon selection in**_
_**FDP_ACF_EXT.1.2.**_


FDP_ACF_EXT.2.1

The TSF shall provide a separate [ **selection** : _address book_, _calendar_, _keystore_,
_account credential database, [_ _**assignment**_ _: list of additional resources]_ ] for
each application group and only allow applications within that process group to
access the resource. Exceptions may only be explicitly authorized for such
sharing by [ **selection** : _the user_, _the administrator_, _no one_ ].


**Application Note:** If groups of applications is selected in FDP_ACF_EXT.1.2,
FDP_ACF_EXT.2 must be included in the ST.


**Evaluation Activities**


_FDP_ACF_EXT.2_
_**TSS**_
_There are no TSS evaluation activities for this component._


_**Guidance**_
_There are no guidance evaluation activities for this component._


_**Tests**_
_For each selected resource, the evaluator shall cause data to be placed into the Enterprise_


_group’s instance of that shared resource. The evaluator shall install an application into the_
_Personal group that attempts to access the shared resource information and verify that it cannot_
_access the information._


**B.3 Class: Protection of the TSF (FPT)**


**FPT_TST_EXT.3 TSF Integrity Testing**


_**The inclusion of this selection-based component depends upon selection in**_
_**FIA_X509_EXT.2.1.**_


FPT_TST_EXT.3.1

The TSF shall not execute code if the code signing certificate is deemed invalid.


**Application Note:** Certificates may optionally be used for code signing for
integrity verification (FPT_TST_EXT.2.1/PREKERNEL). If code signing for
integrity verification is selected in FIA_X509_EXT.2.1, FPT_TST_EXT.3 must be
included in the ST.


Validity is determined by the certificate path, the expiration date, and the
revocation status in accordance with RFC 5280.


**Evaluation Activities**





**FPT_TUD_EXT.4 Trusted Update Verification**


_**The inclusion of this selection-based component depends upon selection in**_
_**FIA_X509_EXT.2.1.**_


FPT_TUD_EXT.4.1

The TSF shall not install code if the code signing certificate is deemed invalid.


**Application Note:** Certificates may optionally be used for code signing of
system software updates (FPT_TUD_EXT.2.3) and of mobile applications
(FPT_TUD_EXT.5.1). This element must be included in the ST if certificates are
used for either update element. If either code signing for system software
updates or code signing for mobile applications is selected in FIA_X509_EXT.2.1,
FPT_TUD_EXT.4 must be included in the ST.


Validity is determined by the certificate path, the expiration date, and the
revocation status in accordance with RFC 5280.


**Evaluation Activities**






# **Appendix C - Extended Component Definitions**

This appendix contains the definitions for all extended requirements specified in the PP.


**C.1 Extended Components Table**


All extended components specified in the PP are listed in this table:


**Table 10: Extended Component Definitions**

**Functional Class** **Functional Components**


Class: Cryptographic Support (FCS) FCS_CKM_EXT Cryptographic Key Management
FCS_HTTPS_EXT HTTPS Protocol
FCS_IV_EXT Initialization Vector Generation
FCS_RBG_EXT Random Bit Generation
FCS_SRV_EXT Cryptographic Algorithm Services
FCS_STG_EXT Cryptographic Key Storage


Class: Identification and Authentication (FIA) FIA_AFL_EXT Authentication Failures
FIA_PMG_EXT Password Management
FIA_TRT_EXT Authentication Throttling
FIA_UAU_EXT User Authentication
FIA_X509_EXT X.509 Certificates


Class: Protection of the TSF (FPT) FPT_AEX_EXT Anti-Exploitation Capabilities
FPT_BBD_EXT Baseband Processing
FPT_BLT_EXT Limitation of Bluetooth Profile Support
FPT_JTA_EXT JTAG Disablement
FPT_KST_EXT Key Storage
FPT_NOT_EXT Self-Test Notification
FPT_TST_EXT TSF Self Test
FPT_TUD_EXT TSF Updates


Class: Security Management (FMT) FMT_MOF_EXT Management of Functions in TSF
FMT_SMF_EXT Specification of Management Functions


Class: TOE Access (FTA) FTA_SSL_EXT Session Locking and Termination


Class: Trusted Path/Channels (FTP) FTP_ITC_EXT Inter-TSF Trusted Channel


Class: User Data Protection (FDP) FDP_ACF_EXT Access Control Functions
FDP_BCK_EXT Application Backup
FDP_BLT_EXT Limitation of Bluetooth Device Access
FDP_DAR_EXT Data-at-Rest Encryption
FDP_IFC_EXT Subset Information Flow Control
FDP_STG_EXT User Data Storage
FDP_UPC_EXT Inter-TSF User Data Transfer Protection


**C.2 Extended Component Definitions**


**C.2.1 Class: Cryptographic Support (FCS)**
This PP defines the following extended components as part of the FCS class originally defined by CC Part 2:


**C.2.1.1 FCS_CKM_EXT Cryptographic Key Management**


**Family Behavior**


This family defines requirements for management of cryptographic keys that are not addressed by
FCS_CKM in CC Part 2.


**Component Leveling**






|FCS_CKM_EXT|1|
|---|---|
|FCS_CKM_EXT|2|
|FCS_CKM_EXT|3|
|FCS_CKM_EXT|4|
|FCS_CKM_EXT|5|
|FCS_CKM_EXT|6|
|FCS_CKM_EXT|7|



FCS_CKM_EXT.1, Cryptographic Key Support, requires the TSF to implement a Root Encryption Key (REK).


FCS_CKM_EXT.2, Cryptographic Key Random Generation, requires the TSF to specify the mechanism it
uses to generate Data Encryption Keys (DEKs).


FCS_CKM_EXT.3, Cryptographic Key Generation, requires the TSF to generate and manage the strength of
Key Encryption Keys (KEKs).


FCS_CKM_EXT.4, Key Destruction, requires the TSF to be able to follow specified rules to destroy plaintext
keying material and cryptographic keys when no longer needed.


FCS_CKM_EXT.5, TSF Wipe, requires the TSF to implement a cryptographic or other mechanism to make
TSF data unreadable.


FCS_CKM_EXT.6, Salt Generation, requires the TSF to generate salts in a specified manner.


FCS_CKM_EXT.7, Cryptographic Key Support (REK), requires the TSF to prevent the reading or exporting
of REKs.


**Management: FCS_CKM_EXT.1**


There are no management activities foreseen.


**Audit: FCS_CKM_EXT.1**


The following actions should be auditable if FAU_GEN Security audit data generation is included in the
PP/ST:


Generation of a REK.


**FCS_CKM_EXT.1 Cryptographic Key Support**


Hierarchical to: No other components.


Dependencies to: FCS_RBG_EXT.1 Random Bit Generation


**FCS_CKM_EXT.1.1**


The TSF shall support [ **assignment** : _description of REKs_ ]


**FCS_CKM_EXT.1.2**


Each REK shall be hardware-isolated from the OS on the TSF in runtime.


**FCS_CKM_EXT.1.3**


Each REK shall be generated by an RBG in accordance with FCS_RBG_EXT.1.


**Management: FCS_CKM_EXT.2**


There are no management activities foreseen.


**Audit: FCS_CKM_EXT.2**


There are no auditable events foreseen.


**FCS_CKM_EXT.2 Cryptographic Key Random Generation**


Hierarchical to: No other components.


Dependencies to: FCS_RBG_EXT.1 Random Bit Generation


**FCS_CKM_EXT.2.1**


All DEKs shall be [ **assignment** : _generation mechanism_ ] with entropy corresponding to the security
strength of AES key sizes of [ **assignment** : _number greater than 128_ ] bits.


**Management: FCS_CKM_EXT.3**


There are no management activities foreseen.


**Audit: FCS_CKM_EXT.3**


There are no auditable events foreseen.


**FCS_CKM_EXT.3 Cryptographic Key Generation**


Hierarchical to: No other components.


Dependencies to: FCS_CKM.1 Cryptographic Key Generation
FCS_COP.1 Cryptographic Operation
FCS_RBG_EXT.1 Random Bit Generation


**FCS_CKM_EXT.3.1**


The TSF shall use [ **assignment** : _description of KEKs_ ].


**FCS_CKM_EXT.3.2**


The TSF shall generate all KEKs using one of the following methods:


Derive the KEK from a Password Authentication Factor according to FCS_COP.1.1 and

[ **selection** :

_Generate the KEK using an RBG that meets this profile (as specified in FCS_RBG_EXT.1)_
_Generate the KEK using a key generation scheme that meets this profile (as specified in FCS_CKM.1)_
_Combine the KEK from other KEKs in a way that preserves the effective entropy of each factor by_

_[_ _**selection**_ _: using an XOR operation, concatenating the keys and using a KDF (as described in SP_
_800-108), concatenating the keys and using a KDF (as described in SP 800-56C), encrypting one key_
_with another ]_

].


**Management: FCS_CKM_EXT.4**


There are no management activities foreseen.


**Audit: FCS_CKM_EXT.4**


There are no auditable events foreseen.


**FCS_CKM_EXT.4 Key Destruction**


Hierarchical to: No other components.


Dependencies to: FCS_RBG_EXT.1 Random Bit Generation


**FCS_CKM_EXT.4.1**


The TSF shall destroy cryptographic keys in accordance with the specified cryptographic key destruction
methods:

By clearing the KEK encrypting the target key
In accordance with the following rules

For volatile memory, the destruction shall be executed by a single direct overwrite [ **selection** :
_consisting of a pseudorandom pattern using the TSF’s RBG_, _consisting of zeros_ ].
For non-volatile EEPROM, the destruction shall be executed by a single direct overwrite
consisting of a pseudo random pattern using the TSF’s RBG (as specified in FCS_RBG_EXT.1),
followed by a read-verify.
For non-volatile flash memory, that is not wear-leveled, the destruction shall be executed

[ **selection** : _by a single direct overwrite consisting of zeros followed by a read-verify_, _by a block_
_erase that erases the reference to memory that stores data as well as the data itself_ ].
For non-volatile flash memory, that is wear-leveled, the destruction shall be executed

[ **selection** : _by a single direct overwrite consisting of zeros_, _by a block erase_ ].
For non-volatile memory other than EEPROM and flash, the destruction shall be executed by a
single direct overwrite with a random pattern that is changed before each write.


**FCS_CKM_EXT.4.2**


The TSF shall destroy all plaintext keying material and critical security parameters when no longer
needed.


**Management: FCS_CKM_EXT.5**


The following actions could be considered for the management functions in FMT:


TSF wipe of protected data.
TSF wipe of enterprise data.


**Audit: FCS_CKM_EXT.5**


The following actions should be auditable if FAU_GEN Security audit data generation is included in the
PP/ST:


Failure of the wipe.


**FCS_CKM_EXT.5 TSF Wipe**


Hierarchical to: No other components.


Dependencies to: FCS_RBG_EXT.1 Random Bit Generation


**FCS_CKM_EXT.5.1**


The TSF shall wipe all protected data by [ **selection** :

_Cryptographically erasing the encrypted DEKs or the KEKs in non-volatile memory by following the_
_requirements in FCS_CKM_EXT.4.1_
_Overwriting all PD according to the following rules:_

_For EEPROM, the destruction shall be executed by a single direct overwrite consisting of a_
_pseudo random pattern using the TSF’s RBG (as specified in FCS_RBG_EXT.1, followed by a_
_read-verify._
_For flash memory, that is not wear-leveled, the destruction shall be executed [_ _**selection**_ _: by a_
_single direct overwrite consisting of zeros followed by a read-verify, by a block erase that erases_
_the reference to memory that stores data as well as the data itself ]._


_For flash memory, that is wear-leveled, the destruction shall be executed [_ _**selection**_ _: by a single_
_direct overwrite consisting of zeros, by a block erase ]._
_For non-volatile memory other than EEPROM and flash, the destruction shall be executed by a_
_single direct overwrite with a random pattern that is changed before each write._

].


**FCS_CKM_EXT.5.2**


The TSF shall perform a power cycle on conclusion of the wipe procedure.


**Management: FCS_CKM_EXT.6**


There are no management activities foreseen.


**Audit: FCS_CKM_EXT.6**


There are no auditable events foreseen.


**FCS_CKM_EXT.6 Salt Generation**


Hierarchical to: No other components.


Dependencies to: FCS_RBG_EXT.1 Random Bit Generation


**FCS_CKM_EXT.6.1**


The TSF shall generate all salts using an RBG that meets FCS_RBG_EXT.1.


**Management: FCS_CKM_EXT.7**


There are no management activities foreseen.


**Audit: FCS_CKM_EXT.7**


There are no auditable events foreseen.


**FCS_CKM_EXT.7 Cryptographic Key Support (REK)**


Hierarchical to: No other components.


Dependencies to: FCS_CKM_EXT.1 Cryptographic Key Support


**FCS_CKM_EXT.7.1**


A REK shall not be able to be read from or exported from the hardware.


**C.2.1.2 FCS_HTTPS_EXT HTTPS Protocol**


**Family Behavior**


This family defines requirements for implementation of the HTTPS protocol.


**Component Leveling**

|FCS_HTTPS_EXT|Col2|1|
|---|---|---|
|FCS_HTTPS_EXT|||



FCS_HTTPS_EXT.1, HTTPS Protocol, requires the TSF to implement the HTTPS protocol in accordance with
the specified standard, using TLS, and notifying the application if invalid.


**Management: FCS_HTTPS_EXT.1**


The following actions could be considered for the management functions in FMT:


Configuring whether to allow or disallow establishment of a trusted channel if the peer or server
certificate is deemed invalid.


**Audit: FCS_HTTPS_EXT.1**


The following actions should be auditable if FAU_GEN Security audit data generation is included in the
PP/ST:


Failure of the certificate validity check.


**FCS_HTTPS_EXT.1 HTTPS Protocol**


Hierarchical to: No other components.


Dependencies to: FIA_X509_EXT.1 X.509 Validation of Certificates
FMT_SMF.1 Specification of Management Functions


**FCS_HTTPS_EXT.1.1**


The TSF shall implement the HTTPS protocol that complies with RFC 2818.


**FCS_HTTPS_EXT.1.2**


The TSF shall implement HTTPS using TLS as defined in [ **assignment** : _specification that defines TLS_
_implementation requirements_ ].


**FCS_HTTPS_EXT.1.3**


The TSF shall notify the application and [ **assignment** : _a response_ ] if the peer certificate is deemed
invalid.


**C.2.1.3 FCS_IV_EXT Initialization Vector Generation**


**Family Behavior**


This family defines requirements for initialization vector generation in support of key generation.


**Component Leveling**

|FCS_IV_EXT|Col2|1|
|---|---|---|
|FCS_IV_EXT|||



FCS_IV_EXT.1, Initialization Vector Generation, requires the TSF to generate IVs in accordance with a set
of approved modes.


**Management: FCS_IV_EXT.1**


There are no management activities foreseen.


**Audit: FCS_IV_EXT.1**


There are no auditable events foreseen.


**FCS_IV_EXT.1 Initialization Vector Generation**


Hierarchical to: No other components.


Dependencies to: No dependencies.


**FCS_IV_EXT.1.1**


The TSF shall generate IVs in accordance with [ **assignment** : _standard or specification for IV generation_ ].


**C.2.1.4 FCS_RBG_EXT Random Bit Generation**


**Family Behavior**


This family defines requirements for the generation of random bits.


**Component Leveling**






|FCS_RBG_EXT|1|
|---|---|
|FCS_RBG_EXT|2|
|FCS_RBG_EXT|3|



FCS_RBG_EXT.1, Random Bit Generation, requires the TSF to generate random data with a certain amount
of entropy and in accordance with applicable standards.


FCS_RBG_EXT.2, Random Bit Generator State Preservation, requires the TSF to save and restore the state
of the RBG when powering off and starting up.


FCS_RBG_EXT.3, Support for Personalization String, requires the TSF to support a personalization string as
a DRBG input parameter.


**Management: FCS_RBG_EXT.1**


There are no management activities foreseen.


**Audit: FCS_RBG_EXT.1**


The following actions should be auditable if FAU_GEN Security audit data generation is included in the
PP/ST:


Failure of the randomization process.


**FCS_RBG_EXT.1 Random Bit Generation**


Hierarchical to: No other components.


Dependencies to: No dependencies.


**FCS_RBG_EXT.1.1**


The TSF shall perform all deterministic random bit generation services in accordance with NIST Special
Publication 800-90A using [ **assignment** : _list of DRBG algorithms_ ].


**FCS_RBG_EXT.1.2**


The deterministic RBG shall be seeded by an entropy source that accumulates entropy from [ **assignment** :
_list of sources of random_ ] with a minimum of [ **assignment** : _number of bits_ ] of entropy at least equal to
the greatest security strength (according to NIST SP 800-57) of the keys and hashes that it will generate.


**FCS_RBG_EXT.1.3**


The TSF shall be capable of providing output of the RBG to applications running on the TSF that request
random bits.


**Management: FCS_RBG_EXT.2**


There are no management activities foreseen.


**Audit: FCS_RBG_EXT.2**


There are no auditable events foreseen.


**FCS_RBG_EXT.2 Random Bit Generator State Preservation**


Hierarchical to: No other components.


Dependencies to: FCS_RBG_EXT.1 Random Bit Generation


**FCS_RBG_EXT.2.1**


The TSF shall save the state of the deterministic RBG at power-off, and shall use this state as input to the
deterministic RBG at startup.


**Management: FCS_RBG_EXT.3**


There are no management activities foreseen.


**Audit: FCS_RBG_EXT.3**


There are no auditable events foreseen.


**FCS_RBG_EXT.3 Support for Personalization String**


Hierarchical to: No other components.


Dependencies to: FCS_RBG_EXT.1 Random Bit Generation


**FCS_RBG_EXT.3.1**


The TSF shall allow applications to add data to the deterministic RBG using the Personalization String as
defined in SP 800-90A.


**C.2.1.5 FCS_SRV_EXT Cryptographic Algorithm Services**


**Family Behavior**


This family defines requirements for the ability of the TOE to make its cryptographic operations available to
non-TSF components.


**Component Leveling**






|FCS_SRV_EXT|Col2|1|
|---|---|---|
|FCS_SRV_EXT|FCS_SRV_EXT|2|
|FCS_SRV_EXT|||



FCS_SRV_EXT.1, Cryptographic Algorithm Services, requires the TSF to have externally-accessible
cryptographic services for making algorithm functions available to applications.


FCS_SRV_EXT.2, Cryptographic Key Storage Services, requires the TSF to support its stored keys being
usable by external applications through cryptographic algorithm services.


**Management: FCS_SRV_EXT.1**


There are no management activities foreseen.


**Audit: FCS_SRV_EXT.1**


There are no auditable events foreseen.


**FCS_SRV_EXT.1 Cryptographic Algorithm Services**


Hierarchical to: No other components.


Dependencies to: FCS_CKM.1 Cryptographic Key Generation
FCS_COP.1 Cryptographic Operation


**FCS_SRV_EXT.1.1**


The TSF shall provide a mechanism for applications to request the TSF to perform the following
cryptographic operations: [ **assignment** : _cryptographic operations defined by the TSF in FCS_CKM.1 or_
_FCS_COP.1_ ]


**Management: FCS_SRV_EXT.2**


There are no management activities foreseen.


**Audit: FCS_SRV_EXT.2**


There are no auditable events foreseen.


**FCS_SRV_EXT.2 Cryptographic Key Storage Services**


Hierarchical to: No other components.


Dependencies to: FCS_COP.1 Cryptographic Operation


**FCS_SRV_EXT.2.1**


The TSF shall provide a mechanism for applications to request the TSF to perform the following
cryptographic operations: [ **assignment** : _cryptographic operations defined by the TSF in FCS_COP.1_ ] by
keys stored in the secure key storage.


**C.2.1.6 FCS_STG_EXT Cryptographic Key Storage**


**Family Behavior**


This family defines requirements for the implementation of secure key storage with access control,
confidentiality, and integrity protections.


**Component Leveling**






|FCS_STG_EXT|1|
|---|---|
|FCS_STG_EXT|2|
|FCS_STG_EXT|3|



FCS_STG_EXT.1, Cryptographic Key Storage, requires the TSF to implement a secure key storage and
defines the access restrictions to be enforced on this.


FCS_STG_EXT.2, Encrypted Cryptographic Key Storage, requires the TSF to implement confidentiality
measures to protect the key storage.


FCS_STG_EXT.3, Integrity of Encrypted Key Storage, requires the TSF to implement integrity measures to
protect the key storage.


**Management: FCS_STG_EXT.1**


The following actions could be considered for the management functions in FMT:


Importing keys or secrets into the secure key storage.
Destroying imported keys or secrets in the secure key storage.
Approving exceptions for shared use of keys or secrets by multiple applications.
Approving exceptions for destruction of keys or secrets by applications that did not import the key or
secret


**Audit: FCS_STG_EXT.1**


The following actions should be auditable if FAU_GEN Security audit data generation is included in the
PP/ST:


Import or destruction of key.
Exceptions to use and destruction rules.


**FCS_STG_EXT.1 Cryptographic Key Storage**


Hierarchical to: No other components.


Dependencies to: [FCS_CKM.1 Cryptographic Key Generation, or
FDP_ITC.1 Import of User Data without Security Attributes, or
FDP_ITC.2 Import of User Data with Security Attributes]
FMT_SMR.1 Security Roles


**FCS_STG_EXT.1.1**


The TSF shall provide [ **assignment** : _storage medium_ ] secure key storage for asymmetric private keys and

[ **assignment** : _list of secrets_ ] .


**FCS_STG_EXT.1.2**


The TSF shall be capable of importing keys or secrets into the secure key storage upon request of

[ **assignment** : _list of users_ ] and [ **assignment** : _list of other subjects_ ].


**FCS_STG_EXT.1.3**


The TSF shall be capable of destroying keys or secrets in the secure key storage upon request of

[ **selection** : _the user_, _the administrator_ ].


**FCS_STG_EXT.1.4**


The TSF shall have the capability to allow only the application that imported the key or secret the use of
the key or secret. Exceptions may only be explicitly authorized by [ **selection** : _the user_, _the administrator_,
_a common application developer_ ].


**FCS_STG_EXT.1.5**


The TSF shall allow only the application that imported the key or secret to request that the key or secret
be destroyed. Exceptions may only be explicitly authorized by [ **assignment** : _list of subjects_ ].


**Management: FCS_STG_EXT.2**


There are no management activities foreseen.


**Audit: FCS_STG_EXT.2**


There are no auditable events foreseen.


**FCS_STG_EXT.2 Encrypted Cryptographic Key Storage**


Hierarchical to: No other components.


Dependencies to: FCS_COP.1 Cryptographic Operation
FCS_STG_EXT.1 Cryptographic Key Storage


**FCS_STG_EXT.2.1**


The TSF shall encrypt all DEKs, KEKs, [ **assignment** : _any long-term trusted channel key material_ ] and

[ **assignment** : _other secrets_ ] by KEKs that are [ **assignment** : _protection mechanism_ ].


**FCS_STG_EXT.2.2**


DEKs, KEKs, [ **assignment** : _any long-term trusted channel key material_ ] and [ **selection** : _all software-_
_based key storage_, _no other keys_ ] shall be encrypted using one of the following methods: [ **selection** :

_using a SP800-56B key establishment scheme_
_using AES in the [_ _**selection**_ _: Key Wrap (KW) mode, Key Wrap with Padding (KWP) mode, GCM,_
_CCM, CBC mode ]_

].


**Management: FCS_STG_EXT.3**


There are no management activities foreseen.


**Audit: FCS_STG_EXT.3**


The following actions should be auditable if FAU_GEN Security audit data generation is included in the
PP/ST:


Failure to verify the integrity of stored key.


**FCS_STG_EXT.3 Integrity of Encrypted Key Storage**


Hierarchical to: No other components.


Dependencies to: FCS_COP.1 Cryptographic Operation
FCS_STG_EXT.2 Encrypted Cryptographic Key Storage


**FCS_STG_EXT.3.1**


The TSF shall protect the integrity of any encrypted DEKs and KEKs and [ **selection** : _long-term trusted_
_channel key material_, _all software-based key storage_, _no other keys_ ] by [ **selection** :

_[_ _**selection**_ _: GCM, CCM, Key Wrap, Key Wrap with Padding ] cipher mode for encryption according to_
_FCS_STG_EXT.2_
_a hash (FCS_COP.1) of the stored key that is encrypted by a key protected by FCS_STG_EXT.2_
_a keyed hash (FCS_COP.1) using a key protected by a key protected by FCS_STG_EXT.2_
_a digital signature of the stored key using an asymmetric key protected according to FCS_STG_EXT.2_
_an immediate application of the key for decrypting the protected data followed by a successful_
_verification of the decrypted data with previously known information_

].


**FCS_STG_EXT.3.2**


The TSF shall verify the integrity of the [ **selection** : _hash_, _digital signature_, _MAC_ ] of the stored key prior
to use of the key.


**C.2.2 Class: Identification and Authentication (FIA)**
This PP defines the following extended components as part of the FIA class originally defined by CC Part 2:


**C.2.2.1 FIA_AFL_EXT Authentication Failures**


**Family Behavior**


This family defines requirements for authentication failure handling that are not addressed by the FIA_AFL
family in CC Part 2.


**Component Leveling**

|FIA_AFL_EXT|Col2|1|
|---|---|---|
|FIA_AFL_EXT|||



FIA_AFL_EXT.1, Authentication Failure Handling, requires the TSF be able to manage unsuccessful
authentication attempts and limit the number of attempts for each method.


**Management: FIA_AFL_EXT.1**


The following actions could be considered for the management functions in FMT:


Configuration of authentication failure limit.


**Audit: FIA_AFL_EXT.1**


The following actions should be auditable if FAU_GEN Security audit data generation is included in the
PP/ST:


Exceeding configured authentication failure limit.


**FIA_AFL_EXT.1 Authentication Failure Handling**


Hierarchical to: No other components.


Dependencies to: FCS_CKM_EXT.5 TSF Wipe
FIA_UAU.1 Timing of Authentication


**FIA_AFL_EXT.1.1**


The TSF shall consider password and [ **assignment** : _list of acceptable authentication mechanisms_ ] as
critical authentication mechanisms.


**FIA_AFL_EXT.1.2**


The TSF shall detect when a configurable positive integer within [ **assignment** : _range of acceptable_
_values for each authentication mechanism_ ] of [ **selection** : _unique_, _non-unique_ ] unsuccessful
authentication attempts occur related to last successful authentication for each authentication
mechanism.


**FIA_AFL_EXT.1.3**


The TSF shall maintain the number of unsuccessful authentication attempts that have occurred upon
power off.


**FIA_AFL_EXT.1.4**


When the defined number of unsuccessful authentication attempts has exceeded the maximum allowed for
a given authentication mechanism, all future authentication attempts will be limited to other available
authentication mechanisms, unless the given mechanism is designated as a critical authentication
mechanism.


**FIA_AFL_EXT.1.5**


When the defined number of unsuccessful authentication attempts for the last available authentication
mechanism or single critical authentication mechanism has been surpassed, the TSF shall perform a wipe
of all protected data.


**FIA_AFL_EXT.1.6**


The TSF shall increment the number of unsuccessful authentication attempts prior to notifying the user
that the authentication was unsuccessful.


**C.2.2.2 FIA_PMG_EXT Password Management**


**Family Behavior**


This family defines requirements for the composition of password credentials.


**Component Leveling**


|FIA_PMG_EXT|Col2|1|
|---|---|---|
|FIA_PMG_EXT|||


FIA_PMG_EXT.1, Password Management, requires the TSF to enforce character length and composition
requirements for password credentials.


**Management: FIA_PMG_EXT.1**


The following actions could be considered for the management functions in FMT:


Configuring password policy.


**Audit: FIA_PMG_EXT.1**


There are no auditable events foreseen.


**FIA_PMG_EXT.1 Password Management**


Hierarchical to: No other components.


Dependencies to: FIA_UAU.1 Timing of Authentication


**FIA_PMG_EXT.1.1**


The TSF shall support the following for the Password Authentication Factor:


1. Passwords shall be able to be composed of any combination of [ **selection** : _upper and lower case_

_letters_, _[_ _**assignment**_ _: a character set of at least 52 characters]_ ], numbers, and special characters:

[ **selection** : _"!"_, _"@"_, _"#"_, _"$"_, _"%"_, _"^"_, _"&"_, _"*"_, _"("_, _")"_, _[_ _**assignment**_ _: other characters]_ ];
2. Password length up to [ **assignment** : _an integer greater than or equal to 14_ ] characters shall be

supported.


**C.2.2.3 FIA_TRT_EXT Authentication Throttling**


**Family Behavior**


This family defines requirements for prevention of brute-force authentication attempts.


**Component Leveling**

|FIA_TRT_EXT|Col2|1|
|---|---|---|
|FIA_TRT_EXT|||



FIA_TRT_EXT.1, Authentication Throttling, requires the TSF to limit authentication attempts by number of
attempts in a set amount of time.


**Management: FIA_TRT_EXT.1**


There are no management activities foreseen.


**Audit: FIA_TRT_EXT.1**


There are no auditable events foreseen.


**FIA_TRT_EXT.1 Authentication Throttling**


Hierarchical to: No other components.


Dependencies to: FIA_UAU.5 Multiple Authentication Mechanisms


**FIA_TRT_EXT.1.1**


The TSF shall limit automated user authentication attempts by [ **selection** : _preventing authentication via_
_an external port_, _enforcing a delay between incorrect authentication attempts_ ] for all authentication
mechanisms selected in FIA_UAU.5.1. The minimum delay shall be such that no more than 10 attempts
can be attempted per 500 milliseconds.


**C.2.2.4 FIA_UAU_EXT User Authentication**


**Family Behavior**


This family defines requirements for user authentication that are not addressed by FIA_UAU in CC Part 2.


**Component Leveling**






|FIA_UAU_EXT|1|
|---|---|
|FIA_UAU_EXT|2|
|FIA_UAU_EXT|4|



FIA_UAU_EXT.1, Authentication for Cryptographic Operation, requires the TSF enforce data-at-rest
protection until successful authentication has occurred.


FIA_UAU_EXT.2, Timing of Authentication, requires the TSF to prevent a subject’s use of TOE until the user
is authenticated.


FIA_UAU_EXT.4, Secondary User Authentication, requires the TSF to enforce the use of a secondary
authentication factor to access certain user data.


**Management: FIA_UAU_EXT.1**


There are no management activities foreseen.


**Audit: FIA_UAU_EXT.1**


There are no auditable events foreseen.


**FIA_UAU_EXT.1 Authentication for Cryptographic Operation**


Hierarchical to: No other components.


Dependencies to: FDP_DAR_EXT.1 Protected Data Encryption
FDP_DAR_EXT.2 Sensitive Data Encryption


**FIA_UAU_EXT.1.1**


The TSF shall require the user to present the Password Authentication Factor prior to decryption of
protected data and encrypted DEKs, KEKs and [ **selection** : _long-term trusted channel key material_, _all_
_software-based key storage_, _no other keys_ ] at startup.


**Management: FIA_UAU_EXT.2**


The following actions could be considered for the management functions in FMT:


Enabling/disabling display TSF notifications while in the locked state.
Enabling/disabling bypass of local user authentication.


**Audit: FIA_UAU_EXT.2**


The following actions should be auditable if FAU_GEN Security audit data generation is included in the
PP/ST:


Action performed before authentication.


**FIA_UAU_EXT.2 Timing of Authentication**


Hierarchical to: No other components.


Dependencies to: No dependencies.


**FIA_UAU_EXT.2.1**


The TSF shall allow [ **selection** : _[_ _**assignment**_ _: list of actions]_, _no actions_ ] on behalf of the user to be
performed before the user is authenticated.


**FIA_UAU_EXT.2.2**


The TSF shall require each user to be successfully authenticated before allowing any other TSF-mediated
actions on behalf of that user.


**Management: FIA_UAU_EXT.4**


There are no management activities foreseen.


**Audit: FIA_UAU_EXT.4**


There are no auditable events foreseen.


**FIA_UAU_EXT.4 Secondary User Authentication**


Hierarchical to: No other components.


Dependencies to: FDP_ACF_EXT.2 Access Control for System Resources
FIA_UAU.5 Multiple Authentication Mechanisms


**FIA_UAU_EXT.4.1**


The TSF shall provide a secondary authentication mechanism for accessing Enterprise applications and
resources. The secondary authentication mechanism shall control access to the Enterprise application and
shared resources and shall be incorporated into the encryption of protected and sensitive data belonging
to Enterprise applications and shared resources.


**FIA_UAU_EXT.4.2**


The TSF shall require the user to present the secondary authentication factor prior to decryption of
Enterprise application data and Enterprise shared resource data.


**C.2.2.5 FIA_X509_EXT X.509 Certificates**


**Family Behavior**


This family defines requirements for the management and use of X.509 certificates.


**Component Leveling**




|FIA_X509_EXT|1|
|---|---|
|FIA_X509_EXT|2|
|FIA_X509_EXT|3|
|FIA_X509_EXT|4|
|FIA_X509_EXT|5|



FIA_X509_EXT.1, X.509 Validation of Certificates, specifies the rules the TSF must follow to determine if a
particular X.509 certificate is valid.


FIA_X509_EXT.2, X.509 Certificate Authentication, defines the TSF’s usage of X.509 certificates and how it
reacts to certificates with undetermined revocation status.


FIA_X509_EXT.3, Request Validation of Certificates, requires the TSF to make a certificate validation
service available to environmental components.


FIA_X509_EXT.4, X509 Certificate Enrollment, requires the TSF to implement Enrollment over Secure
Transport (EST) as a mechanism to obtain X.509 certificates.


FIA_X509_EXT.5, X.509 Certificate Requests, requires the TSF to generate X.509 certificate requests and
validate the responses.


**Management: FIA_X509_EXT.1**


There are no management activities foreseen.


**Audit: FIA_X509_EXT.1**


The following action is be auditable:


Failure to validate X.509v3 certificate.


**FIA_X509_EXT.1 X.509 Validation of Certificates**


Hierarchical to: No other components.


Dependencies to: FCS_COP.1 Cryptographic Operation


**FIA_X509_EXT.1.1**


The TSF shall validate certificates in accordance with the following rules: [ **assignment** : _list of rules_ ].


**FIA_X509_EXT.1.2**


The TSF shall only treat a certificate as a CA certificate if the basicConstraints extension is present and
the CA flag is set to TRUE.


**Management: FIA_X509_EXT.2**


The following actions could be considered for the management functions in FMT:


Configuring whether to allow or disallow establishment of a trusted channel if the TSF cannot establish
a connection to determine the validity of a certificate.


**Audit: FIA_X509_EXT.2**


The following actions should be auditable if FAU_GEN Security audit data generation is included in the
PP/ST:


Failure to establish connection to determine revocation status.


**FIA_X509_EXT.2 X.509 Certificate Authentication**


Hierarchical to: No other components.


Dependencies to: FIA_X509_EXT.1 X.509 Validation of Certificates
FTP_ITC_EXT.1 Trusted Channel Communication


**FIA_X509_EXT.2.1**


The TSF shall use X.509v3 certificates as defined by RFC 5280 to support authentication for

[ **assignment** : _trusted channel protocol_ ] and [ **selection** : _code signing for system software updates_, _code_
_signing for mobile applications_, _code signing for integrity verification_, _[_ _**assignment**_ _: other uses]_, _no_
_additional uses_ ].


**FIA_X509_EXT.2.2**


When the TSF cannot establish a connection to determine the revocation status of a certificate, the TSF
shall [ **assignment** : _list of acceptable actions_ ].


**Management: FIA_X509_EXT.3**


There are no management activities foreseen.


**Audit: FIA_X509_EXT.3**


There are no auditable events foreseen.


**FIA_X509_EXT.3 Request Validation of Certificates**


Hierarchical to: No other components.


Dependencies to: FIA_X509_EXT.1 X.509 Validation of Certificates


**FIA_X509_EXT.3.1**


The TSF shall provide a certificate validation service to applications.


**FIA_X509_EXT.3.2**


The TSF shall respond to the requesting application with the success or failure of the validation.


**Management: FIA_X509_EXT.4**


There are no management activities foreseen.


**Audit: FIA_X509_EXT.4**


The following actions should be auditable if FAU_GEN Security audit data generation is included in the
PP/ST:


Generation of Certificate Enrollment Request.
Success or failure of enrollment.
Update of EST Trust Anchor Database


**FIA_X509_EXT.4 X509 Certificate Enrollment**


Hierarchical to: No other components.


Dependencies to: FCS_CKM.1 Cryptographic Key Generation
FIA_X509_EXT.1 X.509 Validation of Certificates


**FIA_X509_EXT.4.1**


The TSF shall use the Enrollment over Secure Transport (EST) protocol as specified in RFC 7030 to
request certificate enrollment using the simple enrollment method described in RFC 7030 Section 4.2.


**FIA_X509_EXT.4.2**


The TSF shall be capable of authenticating EST requests using an existing certificate and corresponding
private key as specified by RFC 7030 Section 3.3.2.


**FIA_X509_EXT.4.3**


The TSF shall be capable of authenticating EST requests using HTTP Basic Authentication with a
username and password as specified by RFC 7030 Section 3.2.3.


**FIA_X509_EXT.4.4**


The TSF shall perform authentication of the EST server using an Explicit Trust Anchor following the rules
described in RFC 7030, section 3.6.1.


**FIA_X509_EXT.4.5**


The TSF shall be capable of requesting server-provided private keys as specified in RFC 7030 Section 4.4.


**FIA_X509_EXT.4.6**


The TSF shall be capable of updating its EST-specific Trust Anchor Database using the "Root CA Key
Update" process described in RFC 7030 Section 4.1.3.


**FIA_X509_EXT.4.7**


The TSF shall generate a Certificate Request Message for EST as specified in RFC 2986 and be able to
provide the following information in the request: public key and [ **selection** : _device-specific information_,
_Common Name_, _Organization_, _Organizational Unit_, _Country_ ].


**FIA_X509_EXT.4.8**


The TSF shall validate the chain of certificates from the Root CA certificate in the Trust Anchor Database
to the EST Server CA certificate upon receiving a CA Certificates Response.


**Management: FIA_X509_EXT.5**


There are no management activities foreseen.


**Audit: FIA_X509_EXT.5**


There are no auditable events foreseen.


**FIA_X509_EXT.5 X.509 Certificate Requests**


Hierarchical to: No other components.


Dependencies to: FCS_CKM.1 Cryptographic Key Generation
FIA_X509_EXT.1 X.509 Validation of Certificates


**FIA_X509_EXT.5.1**


The TSF shall generate a Certificate Request Message as specified in RFC 2986 and be able to provide
the following information in the request: public key and [ **selection** : _device-specific information_, _Common_
_Name_, _Organization_, _Organizational Unit_, _Country_ ].


**FIA_X509_EXT.5.2**


The TSF shall validate the chain of certificates from the Root CA upon receiving the CA Certificate
Response.


**C.2.3 Class: Protection of the TSF (FPT)**

This PP defines the following extended components as part of the FPT class originally defined by CC Part 2:


**C.2.3.1 FPT_AEX_EXT Anti-Exploitation Capabilities**


**Family Behavior**


This family defines requirements for protecting against common types of software exploitation techniques.


**Component Leveling**






|FPT_AEX_EXT|1|
|---|---|
|FPT_AEX_EXT|2|
|FPT_AEX_EXT|3|
|FPT_AEX_EXT|4|
|FPT_AEX_EXT|5|
|FPT_AEX_EXT|6|
|FPT_AEX_EXT|7|



FPT_AEX_EXT.1, Application Address Space Layout Randomization, requires the TSF to support address
space layout randomization (ASLR).


FPT_AEX_EXT.2, Memory Page Permissions, requires the TSF to enforce access permissions on physical
memory.


FPT_AEX_EXT.3, Stack Overflow Protection, requires the TSF to implement stack overflow protection.


FPT_AEX_EXT.4, Domain Isolation, requires the TSF to protect itself from untrusted subjects and enforce
address space isolation.


FPT_AEX_EXT.5, Kernel Address Space Layout Randomization, requires the TSF to provide ASLR to the
kernel.


FPT_AEX_EXT.6, Write or Execute Memory Page Permissions, requires the TSF to prevent physical memory
from being both writable and executable.


FPT_AEX_EXT.7, Heap Overflow Protection, requires the TSF to support heap-based buffer overflow
protection.


**Management: FPT_AEX_EXT.1**


There are no management activities foreseen.


**Audit: FPT_AEX_EXT.1**


There are no auditable events foreseen.


**FPT_AEX_EXT.1 Application Address Space Layout Randomization**


Hierarchical to: No other components.


Dependencies to: FCS_RBG_EXT.1 Random Bit Generation


**FPT_AEX_EXT.1.1**


The TSF shall provide address space layout randomization ASLR to applications.


**FPT_AEX_EXT.1.2**


The base address of any user-space memory mapping will consist of at least 8 unpredictable bits.


**Management: FPT_AEX_EXT.2**


There are no management activities foreseen.


**Audit: FPT_AEX_EXT.2**


There are no auditable events foreseen.


**FPT_AEX_EXT.2 Memory Page Permissions**


Hierarchical to: No other components.


Dependencies to: No dependencies.


**FPT_AEX_EXT.2.1**


The TSF shall be able to enforce read, write, and execute permissions on every page of physical memory.


**Management: FPT_AEX_EXT.3**


There are no management activities foreseen.


**Audit: FPT_AEX_EXT.3**


There are no auditable events foreseen.


**FPT_AEX_EXT.3 Stack Overflow Protection**


Hierarchical to: No other components.


Dependencies to: No dependencies.


**FPT_AEX_EXT.3.1**


TSF processes that execute in a non-privileged execution domain on the application processor shall
implement stack-based buffer overflow protection.


**Management: FPT_AEX_EXT.4**


There are no management activities foreseen.


**Audit: FPT_AEX_EXT.4**


There are no auditable events foreseen.


**FPT_AEX_EXT.4 Domain Isolation**


Hierarchical to: No other components.


Dependencies to: No dependencies.


**FPT_AEX_EXT.4.1**


The TSF shall protect itself from modification by untrusted subjects.


**FPT_AEX_EXT.4.2**


The TSF shall enforce isolation of address space between applications.


**Management: FPT_AEX_EXT.5**


There are no management activities foreseen.


**Audit: FPT_AEX_EXT.5**


There are no auditable events foreseen.


**FPT_AEX_EXT.5 Kernel Address Space Layout Randomization**


Hierarchical to: No other components.


Dependencies to: No dependencies.


**FPT_AEX_EXT.5.1**


The TSF shall provide address space layout randomization (ASLR) to the kernel.


**FPT_AEX_EXT.5.2**


The base address of any kernel-space memory mapping will consist of [ **assignment** : _number greater than_
_or equal to 4_ ] unpredictable bits.


**Management: FPT_AEX_EXT.6**


There are no management activities foreseen.


**Audit: FPT_AEX_EXT.6**


There are no auditable events foreseen.


**FPT_AEX_EXT.6 Write or Execute Memory Page Permissions**


Hierarchical to: No other components.


Dependencies to: No dependencies.


**FPT_AEX_EXT.6.1**


The TSF shall prevent write and execute permissions from being simultaneously granted to any page of
physical memory [ **selection** : _with no exceptions_, _[_ _**assignment**_ _: specific exceptions]_ ].


**Management: FPT_AEX_EXT.7**


There are no management activities foreseen.


**Audit: FPT_AEX_EXT.7**


There are no auditable events foreseen.


**FPT_AEX_EXT.7 Heap Overflow Protection**


Hierarchical to: No other components.


Dependencies to: No dependencies.


**FPT_AEX_EXT.7.1**


The TSF shall include heap-based buffer overflow protections in the runtime environment it provides to
processes that execute on the application processor.


**C.2.3.2 FPT_BBD_EXT Baseband Processing**


**Family Behavior**


This family defines requirements for separation of baseband and application processor execution.


**Component Leveling**

|FPT_BBD_EXT|Col2|1|
|---|---|---|
|FPT_BBD_EXT|||



FPT_BBD_EXT.1, Application Processor Mediation, requires the TSF to enforce separation between
baseband and application processor execution except through application processor mechanisms.


**Management: FPT_BBD_EXT.1**


There are no management activities foreseen.


**Audit: FPT_BBD_EXT.1**


There are no auditable events foreseen.


**FPT_BBD_EXT.1 Application Processor Mediation**


Hierarchical to: No other components.


Dependencies to: No dependencies.


**FPT_BBD_EXT.1.1**


The TSF shall prevent code executing on any baseband processor (BP) from accessing application
processor (AP) resources except when mediated by the AP.


**C.2.3.3 FPT_BLT_EXT Limitation of Bluetooth Profile Support**


**Family Behavior**


This family defines requirements for limiting Bluetooth capabilities without user action.


**Component Leveling**

|FPT_BLT_EXT|Col2|1|
|---|---|---|
|FPT_BLT_EXT|||



FPT_BLT_EXT.1, Limitation of Bluetooth Profile Support, requires the TSF to maintain a disabled by default
posture for Bluetooth profiles.


**Management: FPT_BLT_EXT.1**


There are no management activities foreseen.


**Audit: FPT_BLT_EXT.1**


There are no auditable events foreseen.


**FPT_BLT_EXT.1 Limitation of Bluetooth Profile Support**


Hierarchical to: No other components.


Dependencies to: No dependencies.


**FPT_BLT_EXT.1.1**


The TSF shall disable support for [ **assignment** : _list of Bluetooth profiles_ ] Bluetooth profiles when they
are not currently being used by an application on the Mobile Device, and shall require explicit user action
to enable them.


**C.2.3.4 FPT_JTA_EXT JTAG Disablement**


**Family Behavior**


This family defines requirements for JTAG interface access limitations.


**Component Leveling**

|FPT_JTA_EXT|Col2|1|
|---|---|---|
|FPT_JTA_EXT|||



FPT_JTA_EXT.1, JTAG Disablement, requires the TSF to specify the mechanism used to restrict access to its
JTAG interface.


**Management: FPT_JTA_EXT.1**


There are no management activities foreseen.


**Audit: FPT_JTA_EXT.1**


There are no auditable events foreseen.


**FPT_JTA_EXT.1 JTAG Disablement**


Hierarchical to: No other components.


Dependencies to: No dependencies.


**FPT_JTA_EXT.1.1**


The TSF shall [ **assignment** : _list access control mechanisms_ ] to JTAG.


**C.2.3.5 FPT_KST_EXT Key Storage**


**Family Behavior**


This family defines requirements for protecting plaintext keys.


**Component Leveling**






|FPT_KST_EXT|1|
|---|---|
|FPT_KST_EXT|2|
|FPT_KST_EXT|3|



FPT_KST_EXT.1, Key Storage, requires the TSF to avoid storage of plaintext keys in readable memory.


FPT_KST_EXT.2, No Key Transmission, requires the TSF to prevent transmitting plaintext key material to
the operational environment.


FPT_KST_EXT.3, No Plaintext Key Export, requires the TSF to prevent the export of plaintext keys.


**Management: FPT_KST_EXT.1**


There are no management activities foreseen.


**Audit: FPT_KST_EXT.1**


There are no auditable events foreseen.


**FPT_KST_EXT.1 Key Storage**


Hierarchical to: No other components.


Dependencies to: No dependencies.


**FPT_KST_EXT.1.1**


The TSF shall not store any plaintext key material in readable non-volatile memory.


**Management: FPT_KST_EXT.2**


There are no management activities foreseen.


**Audit: FPT_KST_EXT.2**


There are no auditable events foreseen.


**FPT_KST_EXT.2 No Key Transmission**


Hierarchical to: No other components.


Dependencies to: No dependencies.


**FPT_KST_EXT.2.1**


The TSF shall not transmit any plaintext key material outside the security boundary of the TOE.


**Management: FPT_KST_EXT.3**


There are no management activities foreseen.


**Audit: FPT_KST_EXT.3**


There are no auditable events foreseen.


**FPT_KST_EXT.3 No Plaintext Key Export**


Hierarchical to: No other components.


Dependencies to: No dependencies.


**FPT_KST_EXT.3.1**


The TSF shall ensure it is not possible for the TOE users to export plaintext keys.


**C.2.3.6 FPT_NOT_EXT Self-Test Notification**


**Family Behavior**


This family defines requirements for generation of notifications in response to completed self-tests.


**Component Leveling**






|FPT_NOT_EXT|Col2|1|
|---|---|---|
|FPT_NOT_EXT|FPT_NOT_EXT|2|
|FPT_NOT_EXT|||



FPT_NOT_EXT.1, Self-Test Notification, requires the TSF to become non-operational when certain failures
occur.


FPT_NOT_EXT.2, Software Integrity Verification, requires the TSF to generate and sign software integrity
verification values.


**Management: FPT_NOT_EXT.1**


There are no management activities foreseen.


**Audit: FPT_NOT_EXT.1**


The following actions should be auditable if FAU_GEN Security audit data generation is included in the
PP/ST:


Measurement of TSF software.


**FPT_NOT_EXT.1 Self-Test Notification**


Hierarchical to: No other components.


Dependencies to: FPT_TST_EXT.1 TSF Cryptographic Functionality Testing
FPT_TST_EXT.2 TSF Integrity Checking


**FPT_NOT_EXT.1.1**


The TSF shall transition to non-operational mode and [ **selection** : _log failures in the audit record_, _notify_
_the administrator_, _[_ _**assignment**_ _: other actions]_, _no other actions_ ] when the following types of failures
occur:

failures of the self-tests
TSF software integrity verification failures

[ **selection** : _no other failures_, _[_ _**assignment**_ _: other failures]_ ]


**Management: FPT_NOT_EXT.2**


The following actions could be considered for the management functions in FMT:


Retrieval of TSF software integrity verification values.


**Audit: FPT_NOT_EXT.2**


There are no auditable events foreseen.


**FPT_NOT_EXT.2 Software Integrity Verification**


Hierarchical to: No other components.


Dependencies to: FCS_COP.1 Cryptographic Operation


**FPT_NOT_EXT.2.1**


The TSF shall [ **selection** : _audit_, _provide the administrator with_ ] TSF-software integrity verification
values.


**FPT_NOT_EXT.2.2**


The TSF shall cryptographically sign all integrity verification values.


**C.2.3.7 FPT_TST_EXT TSF Self Test**


**Family Behavior**


This family defines requirements for execution of self-tests that are not addressed by FPT_TST in CC Part 2.


**Component Leveling**






|FPT_TST_EXT|Col2|1|
|---|---|---|
|FPT_TST_EXT|FPT_TST_EXT|3|
|FPT_TST_EXT|||



FPT_TST_EXT.1, TSF Cryptographic Functionality Testing, requires the TSF to run self-test at start-up to
verify correct operation.


FPT_TST_EXT.3, TSF Integrity Testing, requires the TSF to validate a code signing certificate before the
associated code is executed.


**Management: FPT_TST_EXT.1**


There are no management activities foreseen.


**Audit: FPT_TST_EXT.1**


The following actions should be auditable if FAU_GEN Security audit data generation is included in the
PP/ST:


Initiation of self-test.
Failure of self-test.


**FPT_TST_EXT.1 TSF Cryptographic Functionality Testing**


Hierarchical to: No other components.


Dependencies to: FCS_COP.1 Cryptographic Operation


**FPT_TST_EXT.1.1**


The TSF shall run a suite of self-tests during initial start-up (on power on) to demonstrate the correct
operation of all cryptographic functionality.


**Management: FPT_TST_EXT.3**


There are no management activities foreseen.


**Audit: FPT_TST_EXT.3**


There are no auditable events foreseen.


**FPT_TST_EXT.3 TSF Integrity Testing**


Hierarchical to: No other components.


Dependencies to: FPT_TST_EXT.2 TSF Integrity Checking
FIA_X509_EXT.1 X.509 Validation of Certificates
FIA_X509_EXT.2 X.509 Certificate Authentication


**FPT_TST_EXT.3.1**


The TSF shall not execute code if the code signing certificate is deemed invalid.


**C.2.3.8 FPT_TUD_EXT TSF Updates**


**Family Behavior**


This family defines requirements for trusted updates.


**Component Leveling**




|FPT_TUD_EXT|1|
|---|---|
|FPT_TUD_EXT|2|
|FPT_TUD_EXT|3|
|FPT_TUD_EXT|4|
|FPT_TUD_EXT|5|
|FPT_TUD_EXT|6|



FPT_TUD_EXT.1, TSF Version Query, requires the TSF to provide authorized users the ability to query the
version of the TOE hardware, TOE software, and installed applications.


FPT_TUD_EXT.2, TSF Update Verification, requires the TSF to ensure that system software updates are
digitally signed prior to installation.


FPT_TUD_EXT.3, Application Signing, requires the TSF to ensure that application software updates are
digitally signed prior to installation.


FPT_TUD_EXT.4, Trusted Update Verification, requires the TSF to enforce validity of system software’s
code signing certificate prior to installation.


FPT_TUD_EXT.5, Application Verification, requires the TSF to enforce validity of application software’s
code signing certificate prior to installation.


FPT_TUD_EXT.6, Trusted Update Verification, requires the TSF to prevent the intentional rollback of
software updates.


**Management: FPT_TUD_EXT.1**


There are no management activities foreseen.


**Audit: FPT_TUD_EXT.1**


There are no auditable events foreseen.


**FPT_TUD_EXT.1 TSF Version Query**


Hierarchical to: No other components.


Dependencies to: No dependencies.


**FPT_TUD_EXT.1.1**


The TSF shall provide authorized users the ability to query the current version of the TOE
firmware/software.


**FPT_TUD_EXT.1.2**


The TSF shall provide authorized users the ability to query the current version of the hardware model of
the device.


**FPT_TUD_EXT.1.3**


The TSF shall provide authorized users the ability to query the current version of installed mobile
applications.


**Management: FPT_TUD_EXT.2**


The following actions could be considered for the management functions in FMT:


Updating of system software.


**Audit: FPT_TUD_EXT.2**


The following actions should be auditable if FAU_GEN Security audit data generation is included in the
PP/ST:


Success or failure of signature verification for applications.


**FPT_TUD_EXT.2 TSF Update Verification**


Hierarchical to: No other components.


Dependencies to: FCS_COP.1 Cryptographic Operation


**FPT_TUD_EXT.2.1**


The TSF shall verify software updates to the Application Processor system software and [ **selection** :

_[_ _**assignment**_ _: other processor system software]_, _no other processor system software_ ] using a digital
signature verified by the manufacturer trusted key prior to installing those updates.


**FPT_TUD_EXT.2.2**


The TSF shall [ **selection** : _never update_, _update only by verified software_ ] the TSF boot integrity

[ **selection** : _key_, _hash_ ].


**FPT_TUD_EXT.2.3**


The TSF shall verify that the digital signature verification key used for TSF updates [ **selection** : _is_
_validated to a public key in the Trust Anchor Database_, _matches an immutable hardware public key_ ].


**Management: FPT_TUD_EXT.3**


There are no management activities foreseen.


**Audit: FPT_TUD_EXT.3**


The following actions should be auditable if FAU_GEN Security audit data generation is included in the
PP/ST:


Success or failure of signature verification for applications.


**FPT_TUD_EXT.3 Application Signing**


Hierarchical to: No other components.


Dependencies to: FIA_X509_EXT.1 X.509 Validation of Certificates
FIA_X509_EXT.2 X.509 Certificate Authentication


**FPT_TUD_EXT.3.1**


The TSF shall verify mobile application software using a digital signature mechanism prior to installation.


**Management: FPT_TUD_EXT.4**


There are no management activities foreseen.


**Audit: FPT_TUD_EXT.4**


There are no auditable events foreseen.


**FPT_TUD_EXT.4 Trusted Update Verification**


Hierarchical to: No other components.


Dependencies to: FIA_X509_EXT.1 X.509 Validation of Certificates
FIA_X509_EXT.2 X.509 Certificate Authentication


**FPT_TUD_EXT.4.1**


The TSF shall not install code if the code signing certificate is deemed invalid.


**Management: FPT_TUD_EXT.5**


The following actions should be auditable if FAU_GEN Security audit data generation is included in the
PP/ST:


Configure certificate or public key used to validate digital signature on applications.


**Audit: FPT_TUD_EXT.5**


There are no auditable events foreseen.


**FPT_TUD_EXT.5 Application Verification**


Hierarchical to: No other components.


Dependencies to: FIA_X509_EXT.1 X.509 Validation of Certificates
FIA_X509_EXT.2 X.509 Certificate Authentication


**FPT_TUD_EXT.5.1**


The TSF shall by default only install mobile applications cryptographically verified by [ **selection** : _a built-_
_in X.509v3 certificate_, _a configured X.509v3 certificate_ ].


**Management: FPT_TUD_EXT.6**


There are no management activities foreseen.


**Audit: FPT_TUD_EXT.6**


There are no auditable events foreseen.


**FPT_TUD_EXT.6 Trusted Update Verification**


Hierarchical to: No other components.


Dependencies to: No dependencies.


**FPT_TUD_EXT.6.1**


The TSF shall verify that software updates to the TSF are a current or later version than the current
version of the TSF.


**C.2.4 Class: Security Management (FMT)**
This PP defines the following extended components as part of the FMT class originally defined by CC Part 2:


**C.2.4.1 FMT_MOF_EXT Management of Functions in TSF**


**Family Behavior**


This family defines requirements for authorization to manage the behavior of the TSF that are not
addressed by FMT_MOF in CC Part 2.


**Component Leveling**

|FMT_MOF_EXT|Col2|1|
|---|---|---|
|FMT_MOF_EXT|||



FMT_MOF_EXT.1, Management of Security Functions Behavior, requires the TSF to apply restrictions to
access its management functions to the authorized roles.


**Management: FMT_MOF_EXT.1**


The following actions could be considered for the management functions in FMT:


Managing the group of roles that can interact with the functions in the TSF.


**Audit: FMT_MOF_EXT.1**


There are no auditable events foreseen.


**FMT_MOF_EXT.1 Management of Security Functions Behavior**


Hierarchical to: No other components.


Dependencies to: FMT_SMF.1 Specification of Management Functions


**FMT_MOF_EXT.1.1**


The TSF shall restrict the ability to perform the functions [ **assignment** : _reference to list of management_
_functions_ ] to the user.


**FMT_MOF_EXT.1.2**


The TSF shall restrict the ability to perform the functions [ **assignment** : _reference to list of management_
_functions_ ] to the administrator when the device is enrolled and according to the administrator-configured
policy.


**C.2.4.2 FMT_SMF_EXT Specification of Management Functions**


**Family Behavior**


This family defines requirements for security-relevant management functions that are not addressed by
FMT_SMF in CC Part 2.


**Component Leveling**






|FMT_SMF_EXT|Col2|2|
|---|---|---|
|FMT_SMF_EXT|FMT_SMF_EXT|3|
|FMT_SMF_EXT|||



FMT_SMF_EXT.2, Specification of Remediation Actions, requires the TSF to automatically perform specific
management functions in response to a specific event.


FMT_SMF_EXT.3, Current Administrator, requires the TSF to provide users with a list of administrators and
their specified functions.


**Management: FMT_SMF_EXT.2**


The following actions could be considered for the management functions in FMT:


Configuration of the functions that are performed in response to unenrollment event.


**Audit: FMT_SMF_EXT.2**


The following actions should be auditable if FAU_GEN Security audit data generation is included in the
PP/ST:


Initiation of unenrollment.
Completion of unenrollment.


**FMT_SMF_EXT.2 Specification of Remediation Actions**


Hierarchical to: No other components.


Dependencies to: No dependencies.


**FMT_SMF_EXT.2.1**


The TSF shall offer [ **assignment** : _list of remediation actions_ ] upon unenrollment and [ **assignment** : _list of_
_triggers_ ].


**Management: FMT_SMF_EXT.3**


There are no management activities foreseen.


**Audit: FMT_SMF_EXT.3**


There are no auditable events foreseen.


**FMT_SMF_EXT.3 Current Administrator**


Hierarchical to: No other components.


Dependencies to: FMT_SMR.1 Security Roles


**FMT_SMF_EXT.3.1**


The TSF shall provide a mechanism that allows users to view a list of currently authorized administrators
and the management functions that each administrator is authorized to perform.


**C.2.5 Class: TOE Access (FTA)**
This PP defines the following extended components as part of the FTA class originally defined by CC Part 2:


**C.2.5.1 FTA_SSL_EXT Session Locking and Termination**


**Family Behavior**


This family defines requirements for session locking capabilities that are not addressed by FTA_SSL in CC
Part 2.


**Component Leveling**

|FTA_SSL_EXT|Col2|1|
|---|---|---|
|FTA_SSL_EXT|||



FTA_SSL_EXT.1, TSF- and User-initiated Locked State, requires the TSF to manage the transition to a
locked state and what operations can be performed.


**Management: FTA_SSL_EXT.1**


The following actions could be considered for the management functions in FMT:


Configuring session locking policy.
Transitioning to the locked state.


**Audit: FTA_SSL_EXT.1**


There are no auditable events foreseen.


**FTA_SSL_EXT.1 TSF- and User-initiated Locked State**


Hierarchical to: No other components.


Dependencies to: No dependencies.


**FTA_SSL_EXT.1.1**


The TSF shall transition to a locked state after a time interval of inactivity.


**FTA_SSL_EXT.1.2**


The TSF shall transition to a locked state after initiation by either the user or the administrator.


**FTA_SSL_EXT.1.3**


The TSF shall, upon transitioning to the locked state, perform the following operations:

Clearing or overwriting display devices, obscuring the previous contents;

[ **assignment** : _Other actions performed upon transitioning to the locked state_ ].


**C.2.6 Class: Trusted Path/Channels (FTP)**
This PP defines the following extended components as part of the FTP class originally defined by CC Part 2:


**C.2.6.1 FTP_ITC_EXT Inter-TSF Trusted Channel**


**Family Behavior**


This family defines requirements for trusted channels that are not addressed by FTP_ITC in CC Part 2


because they apply specifically to channels required by a mobile device.


**Component Leveling**

|FTP_ITC_EXT|Col2|1|
|---|---|---|
|FTP_ITC_EXT|||



FTP_ITC_EXT.1, Trusted Channel Communication, requires the TSF to manage the communication channel
between itself and other trusted products.


**Management: FTP_ITC_EXT.1**


The following actions could be considered for the management functions in FMT:


Configuring the actions that require trusted channel, if applicable.
Enabling/disabling communications protocols where the TSF acts as a server.


**Audit: FTP_ITC_EXT.1**


The following actions should be auditable if FAU_GEN Security audit data generation is included in the
PP/ST:


Initiation and termination of trusted channel.


**FTP_ITC_EXT.1 Trusted Channel Communication**


Hierarchical to: No other components.


Dependencies to: No dependencies.


**FTP_ITC_EXT.1.1**


The TSF shall use

802.11-2012 in accordance with [ **assignment** : _requirements or standards defining implementation_
_of this protocol_ ],
802.1X in accordance with [ **assignment** : _requirements or standards defining implementation of this_
_protocol_ ],
EAP-TLS in accordance with [ **assignment** : _requirements or standards defining implementation of_
_this protocol_ ],
Mutually authenticated TLS in accordance with [ **assignment** : _requirements or standards defining_
_implementation of this protocol_ ]

and [ **assignment** : _other protocols_ ] protocols to provide a communication channel between itself and
another trusted IT product that is logically distinct from other communication channels, provides assured
identification of its end points, protects channel data from disclosure, and detects modification of the
channel data.


**FTP_ITC_EXT.1.2**


The TSF shall permit the TSF to initiate communication via the trusted channel.


**FTP_ITC_EXT.1.3**


The TSF shall initiate communication via the trusted channel for wireless access point connections,
administrative communication, configured enterprise connections, and [ **selection** : _OTA updates_, _no other_
_connections_ ].


**C.2.7 Class: User Data Protection (FDP)**

This PP defines the following extended components as part of the FDP class originally defined by CC Part 2:


**C.2.7.1 FDP_ACF_EXT Access Control Functions**


**Family Behavior**


This family defines the rules for access control functions that are not addressed by the FDP_ACF family in
CC Part 2.


**Component Leveling**






|FDP_ACF_EXT|1|
|---|---|
|FDP_ACF_EXT|2|
|FDP_ACF_EXT|3|



FDP_ACF_EXT.1, Access Control for System Services, requires the TSF to be able to control access to its
own services.


FDP_ACF_EXT.2, Access Control for System Resources, requires the TSF to be able to provide separate
copies of system resources for different application groups.


FDP_ACF_EXT.3, Security Attribute Based Access Control, requires the TSF to enforce policies on
applications that prohibit write and execute permissions from being granted simultaneously.


**Management: FDP_ACF_EXT.1**


The following actions could be considered for the management functions in FMT:


Placing applications into application groups based on enterprise configuration settings.
Enabling/disabling location services.
Enabling/disabling data signaling over externally-accessible hardware ports.


**Audit: FDP_ACF_EXT.1**


There are no auditable events foreseen.


**FDP_ACF_EXT.1 Access Control for System Services**


Hierarchical to: No other components.


Dependencies to: FMT_SMR.1 Security Roles


**FDP_ACF_EXT.1.1**


The TSF shall provide a mechanism to restrict the system services that are accessible to an application.


**FDP_ACF_EXT.1.2**


The TSF shall provide an access control policy that prevents [ **assignment** : _list of subjects_ ] from accessing

[ **selection** : _all_, _private_ ] data stored by other [ **assignment** : _list of subjects_ ]. Exceptions may only be
explicitly authorized for such sharing by [ **assignment** : _list of authorized subjects_ ].


**Management: FDP_ACF_EXT.2**


The following actions could be considered for the management functions in FMT:


Approving exceptions for sharing data between applications or groups of applications.


**Audit: FDP_ACF_EXT.2**


There are no auditable events foreseen.


**FDP_ACF_EXT.2 Access Control for System Resources**


Hierarchical to: No other components.


Dependencies to: FDP_ACF_EXT.1 Access Control for System Services
FMT_SMR.1 Security Roles


**FDP_ACF_EXT.2.1**


The TSF shall provide a separate [ **selection** : _address book_, _calendar_, _keystore_, _account credential_
_database, [_ _**assignment**_ _: list of additional resources]_ ] for each application group and only allow
applications within that process group to access the resource. Exceptions may only be explicitly
authorized for such sharing by [ **selection** : _the user_, _the administrator_, _no one_ ].


**Management: FDP_ACF_EXT.3**


There are no management activities foreseen.


**Audit: FDP_ACF_EXT.3**


There are no auditable events foreseen.


**FDP_ACF_EXT.3 Security Attribute Based Access Control**


Hierarchical to: No other components.


Dependencies to: No dependencies.


**FDP_ACF_EXT.3.1**


The TSF shall enforce an access control policy that prohibits an application from granting both write and
execute permission to a file on the device except for [ **selection** : _files stored in the application's private_
_data folder_, _no exceptions_ ].


**C.2.7.2 FDP_BCK_EXT Application Backup**


**Family Behavior**


This family defines requirements for managing device backups.


**Component Leveling**

|FDP_BCK_EXT|Col2|1|
|---|---|---|
|FDP_BCK_EXT|||



FDP_BCK_EXT.1, Application Backup, requires the TSF to be able to determine which data to include in
backup operations.


**Management: FDP_BCK_EXT.1**


The following actions could be considered for the management functions in FMT:


Enable/disable backup of certain applications to a local or remote system.


**Audit: FDP_BCK_EXT.1**


There are no auditable events foreseen.


**FDP_BCK_EXT.1 Application Backup**


Hierarchical to: No other components.


Dependencies to: No dependencies.


**FDP_BCK_EXT.1.1**


The TSF shall provide a mechanism for applications to mark [ **assignment** : _list of data categories_ ] to be
excluded from device backups.


**C.2.7.3 FDP_BLT_EXT Limitation of Bluetooth Device Access**


**Family Behavior**


This family defines requirements for managing Bluetooth devices.


**Component Leveling**

|FDP_BLT_EXT|Col2|1|
|---|---|---|
|FDP_BLT_EXT|||



FDP_BLT_EXT.1, Limitation of Bluetooth Device Access, requires the TSF to manage which applications
communicate with Bluetooth devices.


**Management: FDP_BLT_EXT.1**


There are no management activities foreseen.


**Audit: FDP_BLT_EXT.1**


There are no auditable events foreseen.


**FDP_BLT_EXT.1 Limitation of Bluetooth Device Access**


Hierarchical to: No other components.


Dependencies to: No dependencies.


**FDP_BLT_EXT.1.1**


The TSF shall limit the applications that may communicate with a particular paired Bluetooth device.


**C.2.7.4 FDP_DAR_EXT Data-at-Rest Encryption**


**Family Behavior**


This family defines requirements for implementation of data-at-rest protection.


**Component Leveling**






|FDP_DAR_EXT|Col2|1|
|---|---|---|
|FDP_DAR_EXT|FDP_DAR_EXT|2|
|FDP_DAR_EXT|||



FDP_DAR_EXT.1, Protected Data Encryption, requires the TSF to be able to protect all data with a chosen
method of encryption.


FDP_DAR_EXT.2, Sensitive Data Encryption, requires the TSF to protect the Trust Anchor Database.


**Management: FDP_DAR_EXT.1**


The following actions could be considered for the management functions in FMT:


Enabling data-at-rest protection.
Enabling removable media’s data-at-rest protection.


**Audit: FDP_DAR_EXT.1**


The following actions should be auditable if FAU_GEN Security audit data generation is included in the
PP/ST:


Failure to encrypt/decrypt data.


**FDP_DAR_EXT.1 Protected Data Encryption**


Hierarchical to: No other components.


Dependencies to: FCS_COP.1 Cryptographic Operation


**FDP_DAR_EXT.1.1**


Encryption shall cover all protected data.


**FDP_DAR_EXT.1.2**


Encryption shall be performed using DEKs with AES in the [ **assignment** : _list of AES modes_ ] mode with
key size [ **assignment** : _list of acceptable key sizes_ ] bits.


**Management: FDP_DAR_EXT.2**


The following actions could be considered for the management functions in FMT:


Importing X.509v3 certificates into the Trust Anchor Database.
Removing imported X.509v3 certificates from the Trust Anchor Database.
Approving import and removal by applications of X.509v3 certificates in the Trust Anchor Database.


**Audit: FDP_DAR_EXT.2**


The following actions should be auditable if FAU_GEN Security audit data generation is included in the
PP/ST:


Addition or removal of certificate from Trust Anchor Database


**FDP_DAR_EXT.2 Sensitive Data Encryption**


Hierarchical to: No other components.


Dependencies to: FCS_COP.1 Cryptographic Operation
FCS_CKM.2 Cryptographic Key Establishment
FCS_STG_EXT.2 Encrypted Cryptographic Key Storage


**FDP_DAR_EXT.2.1**


The TSF shall provide a mechanism for applications to mark data and keys as sensitive.


**FDP_DAR_EXT.2.2**


The TSF shall use an asymmetric key scheme to encrypt and store sensitive data received while the
product is locked.


**FDP_DAR_EXT.2.3**


The TSF shall encrypt any stored symmetric key and any stored private key of the asymmetric keys used
for the protection of sensitive data according to [ **assignment** : _mechanism for encrypted key storage_ ].


**FDP_DAR_EXT.2.4**


The TSF shall decrypt the sensitive data that was received while in the locked state upon transitioning to
the unlocked state using the asymmetric key scheme and shall re-encrypt that sensitive data using the
symmetric key scheme.


**C.2.7.5 FDP_IFC_EXT Subset Information Flow Control**


**Family Behavior**


This family defines requirements for handling of information flows that are not addressed by FDP_IFC in CC
Part 2.


**Component Leveling**

|FDP_IFC_EXT|Col2|1|
|---|---|---|
|FDP_IFC_EXT|||



FDP_IFC_EXT.1, Subset Information Flow Control, requires the TSF to be able to support the use of an
IPsec VPN to protect data in transit.


**Management: FDP_IFC_EXT.1**


The following actions could be considered for the management functions in FMT:


Enabling/disabling VPN protection.
Enabling/disabling Always On VPN protection.


**Audit: FDP_IFC_EXT.1**


There are no auditable events foreseen.


**FDP_IFC_EXT.1 Subset Information Flow Control**


Hierarchical to: No other components.


Dependencies to: FTP_ITC_EXT.1 Trusted Channel Communication


**FDP_IFC_EXT.1.1**


The TSF shall [ **selection** :

_provide an interface which allows a VPN client to protect all IP traffic using IPsec_
_[provide a VPN client which can protect all IP traffic using IPsec](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=467&id=467)_ _**as defined in the PP-Module for**_
_**Virtual Private Network (VPN) Clients, version 2.4**_

] with the exception of IP traffic needed to manage the VPN connection, and [ **selection** : _[_ _**assignment**_ _:_
_traffic needed for correct functioning of the TOE]_, _no other traffic_ ], when the VPN is enabled.


**C.2.7.6 FDP_STG_EXT User Data Storage**


**Family Behavior**


This family defines requirements for managing data storage.


**Component Leveling**

|FDP_STG_EXT|Col2|1|
|---|---|---|
|FDP_STG_EXT|||



FDP_STG_EXT.1, User Data Storage, requires the TSF to be able to label, encrypt, store, and decrypt
sensitive data and keys.


**Management: FDP_STG_EXT.1**


There are no management activities foreseen.


**Audit: FDP_STG_EXT.1**


The following actions should be auditable if FAU_GEN Security audit data generation is included in the
PP/ST:


Failure to encrypt/decrypt data.


**FDP_STG_EXT.1 User Data Storage**


Hierarchical to: No other components.


Dependencies to: FCS_COP.1 Cryptographic Operation
FCS_CKM.2 Cryptographic Key Establishment
FCS_STG_EXT.2 Encrypted Cryptographic Key Storage


**FDP_STG_EXT.1.1**


The TSF shall provide protected storage for the Trust Anchor Database.


**C.2.7.7 FDP_UPC_EXT Inter-TSF User Data Transfer Protection**


**Family Behavior**


This family defines requirements for the use of trusted channel protocols to protect user data.


**Component Leveling**

|FDP_UPC_EXT|Col2|1|
|---|---|---|
|FDP_UPC_EXT|||



FDP_UPC_EXT.1, Inter-TSF User Data Transfer Protection, requires the TSF to be able to protect
communication channels between products using a chosen secure method.


**Management: FDP_UPC_EXT.1**


There are no management activities foreseen.


**Audit: FDP_UPC_EXT.1**


The following actions should be auditable if FAU_GEN Security audit data generation is included in the
PP/ST:


Application initiation of trusted channel.


**FDP_UPC_EXT.1 Inter-TSF User Data Transfer Protection**


Hierarchical to: No other components.


Dependencies to: FTP_ITC_EXT.1 Trusted Channel Communication


**FDP_UPC_EXT.1.1**


The TSF shall provide a means for non-TSF applications executing on the TOE to use [ **assignment** : _data_
_transfer protocol_ ] to provide a protected communication channel between the non-TSF application and
another IT product that is logically distinct from other communication channels, provides assured


identification of its end points, protects channel data from disclosure, and detects modification of the
channel data.


**FDP_UPC_EXT.1.2**


The TSF shall permit the non-TSF applications to initiate communication via the trusted channel.


# **Appendix D - Validation Guidelines**

This appendix contains "rules" specified by the PP Authors that indicate whether certain selections require
the making of other selections in order for a Security Target to be valid. For example, selecting "HMAC-SHA3-384" as a supported keyed-hash algorithm would require that "SHA-3-384" be selected as a hash algorithm.


This appendix contains only such "rules" as have been defined by the PP Authors, and does not necessarily
represent all such dependencies in the document.


**Rule #1**











|Rule #2|Col2|
|---|---|
|DECISION J|**CHOICE J1**<br>FromFCS_COP.1.1/HASH:<br>* selectSHA-256<br>* select256 bits|
|DECISION J|**CHOICE J2**<br>FromFCS_COP.1.1/HASH:<br>Do not choose:<br>* SHA-256<br>Do not choose:<br>* 256 bits|


|Rule #3|Col2|
|---|---|
|DECISION K|**CHOICE K1**<br>FromFCS_COP.1.1/HASH:<br>* selectSHA-384<br>* select384 bits|
|DECISION K|**CHOICE K2**<br>FromFCS_COP.1.1/HASH:<br>Do not choose:<br>* SHA-384<br>Do not choose:<br>* 384 bits|


|Rule #4|Col2|
|---|---|
|DECISION L|**CHOICE L1**<br>FromFCS_COP.1.1/HASH:<br>* selectSHA-512<br>* select512 bits|
|DECISION L|**CHOICE L2**<br>FromFCS_COP.1.1/HASH:<br>Do not choose:<br>* SHA-512<br>Do not choose:<br>* 512 bits|


**Rule #5**








[From the Functional Package for Transport Layer Security (TLS):](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=439&id=439)
THEN
From FCS_TLSC_EXT.1.3:

      - select with no exceptions


**Rule #6**











**Rule #7**











**Rule #8**











**Rule #9**











**Rule #10**











**Rule #11**











**Rule #12**


[From the Functional Package for Transport Layer Security (TLS):](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=439&id=439)
From FCS_TLS_EXT.1.1:

 - select TLS as a client


From FCS_TLSC_EXT.1.1:

 - select mutual authentication
From FCS_TLSC_EXT.1.3:

 - select with no exceptions


**Rule #13**













**Rule #14**







THEN [Include the PP-Module for Virtual Private Network (VPN) Clients, version 2.4 module in the ST](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=467&id=467)


# **Appendix E - Implicitly Satisfied Requirements**

This appendix lists requirements that should be considered satisfied by products successfully evaluated
against this PP. These requirements are not featured explicitly as SFRs and should not be included in the ST.
They are not included as standalone SFRs because it would increase the time, cost, and complexity of
evaluation. This approach is permitted by [CC] Part 1, 8.2 Dependencies between components.


This information benefits systems engineering activities which call for inclusion of particular security
controls. Evaluation against the PP provides evidence that these controls are present and have been
evaluated.


**Requirement** **Rationale for Satisfaction**



FAU_SEL.1 Selective Audit


FCS_CKM.1 Cryptographic
Key Generation


FCS_CKM.1 Cryptographic
Key Generation


FCS_CKM.2 Cryptographic
Key
Establishment


FCS_COP.1 Cryptographic
Operation


FCS_STG_EXT.1

- Cryptographic
Key Storage


FDP_ACF_EXT.1

- Access Control
for System
Services


FDP_ACF_EXT.2

- Access Control
for System
Resources


FIA_AFL_EXT.1 Authentication
Failure Handling


FIA_PMG_EXT.1

- Password
Management


FIA_UAU.7 Protected
Authentication
Feedback


FMT_SMF_EXT.3

- Current
Administrator



FAU_SEL.1 has a dependency on FMT_MTD.1 since configuration of audit data is a
subset of managing TSF data. This dependency is met by FMT_SMF.1, which defines
"configure the auditable items" as a management function and specifies the roles that
may perform this, consistent with how FMT_MTD.1 would typically satisfy the
dependency.


FCS_CKM.1 has a dependency on FCS_CKM.4 for the subsequent destruction of any
keys that the TSF generates. This dependency is met by the extended SFR
FCS_CKM_EXT.4, which serves the same purpose.


FCS_CKM.1 has a dependency on FCS_CKM.4 for the subsequent destruction of any
keys that the TSF generates. This dependency is met by the extended SFR
FCS_CKM_EXT.4, which serves the same purpose as its CC Part 2 equivalent.


Both iterations of FCS_CKM.2 have a dependency on FCS_CKM.4 for the subsequent
destruction of any keys that the TSF establishes. This dependency is met by the
extended SFR FCS_CKM_EXT.4, which serves the same purpose as its CC Part 2
equivalent.


All iterations of FCS_COP.1 have a dependency on FCS_CKM.4 for the subsequent
destruction of any residual key material the TSF creates as part of the operation. This
dependency is met by the extended SFR FCS_CKM_EXT.4, which serves the same
purpose as its CC Part 2 equivalent.


FCS_STG_EXT.1 has a dependency on FMT_SMR.1 for the management roles that are
authorized to manage the functionality defined by the requirement. This dependency is
met by FMT_SMF.1, which implicitly defines separate management roles for the TSF.


FDP_ACF_EXT.1 has a dependency on FMT_SMR.1 for the management roles that are
authorized to manage the functionality defined by the requirement. This dependency is
met by FMT_SMF.1, which implicitly defines separate management roles for the TSF.


FDP_ACF_EXT.2 has a dependency on FMT_SMR.1 for the management roles that are
authorized to manage the functionality defined by the requirement. This dependency is
met by FMT_SMF.1, which implicitly defines separate management roles for the TSF.


FIA_AFL_EXT.1 has a dependency on FIA_UAU.1 since handling of authentication
failures is not possible without an authentication mechanism. This dependency is met by
the extended SFR FIA_UAU_EXT.1, which serves the same purpose as its CC Part 2
equivalent.


FIA_PMG_EXT.1 has a dependency on FIA_UAU.1 since composition of authentication
credentials is not possible without an authentication mechanism. This dependency is met
by the extended SFR FIA_UAU_EXT.1, which serves the same purpose as its CC Part 2
equivalent.


FIA_UAU.7 has a dependency on FIA_UAU.1 since protected authentication feedback is
not possible without an authentication mechanism. This dependency is met by the
extended SFR FIA_UAU_EXT.1, which serves the same purpose as its CC Part 2
equivalent.


FMT_SMF_EXT.3 has a dependency on FMT_SMR.1 through its reference to
management roles in the requirement text. This dependency is met by FMT_SMF.1,
which implicitly defines separate management roles for the TSF.


# **Appendix F - Entropy Documentation And** **Assessment**

The documentation of the entropy source should be detailed enough that, after reading, the evaluator will
thoroughly understand the entropy source and why it can be relied upon to provide entropy. This
documentation should include multiple detailed sections: design description, entropy justification, operating
conditions, and health testing. This documentation is not required to be part of the TSS.


**F.1 Design Description**


Documentation shall include the design of the entropy source as a whole, including the interaction of all
entropy source components. It will describe the operation of the entropy source to include how it works, how
entropy is produced, and how unprocessed (raw) data can be obtained from within the entropy source for
testing purposes. The documentation should walk through the entropy source design indicating where the
random comes from, where it is passed next, any post-processing of the raw outputs (hash, XOR, etc.),
if/where it is stored, and finally, how it is output from the entropy source. Any conditions placed on the
process (e.g., blocking) should also be described in the entropy source design. Diagrams and examples are
encouraged.


This design must also include a description of the content of the security boundary of the entropy source and
a description of how the security boundary ensures that an adversary outside the boundary cannot affect the
entropy rate.


If implemented, the design description shall include a description of how third-party applications can add
entropy to the RBG. A description of any RBG state saving between power-off and power-on shall be included.


**F.2 Entropy Justification**


There should be a technical argument for where the unpredictability in the source comes from and why there
is confidence in the entropy source exhibiting probabilistic behavior (an explanation of the probability
distribution and justification for that distribution given the particular source is one way to describe this). This
argument will include a description of the expected entropy rate and explain how you ensure that sufficient
entropy is going into the TOE randomizer seeding process. This discussion will be part of a justification for
why the entropy source can be relied upon to produce bits with entropy.


The entropy justification shall not include any data added from any third-party application or from any state
saving between restarts.


**F.3 Operating Conditions**


Documentation will also include the range of operating conditions under which the entropy source is expected
to generate random data. It will clearly describe the measures that have been taken in the system design to
ensure the entropy source continues to operate under those conditions. Similarly, documentation shall
describe the conditions under which the entropy source is known to malfunction or become inconsistent.
Methods used to detect failure or degradation of the source shall be included.


**F.4 Health Testing**


More specifically, all entropy source health tests and their rationale will be documented. This will include a
description of the health tests, the rate and conditions under which each health test is performed (e.g., at
startup, continuously, or on-demand), the expected results for each health test, and rationale indicating why
each test is believed to be appropriate for detecting one or more failures in the entropy source.


# **Appendix G - Initialization Vector Requirements** **for NIST-Approved Cipher Modes**

Table 11: References and IV Requirements for NIST-approved Cipher Modes


**Cipher Mode** **Reference** **IV Requirements**



Counter (CTR) SP 80038A


Cipher Block Chaining (CBC) SP 80038A


Output Feedback (OFB) SP 80038A


Cipher Feedback (CFB) SP 80038A





XEX (XOR Encrypt XOR)
Tweakable Block Cipher with
Ciphertext Stealing (XTS)


Cipher-based Message
Authentication Code (CMAC)


Key Wrap and Key Wrap with
Padding


Counter with CBC-Message
Authentication Code (CCM)



SP 80038E


SP 80038B


SP 80038F


SP 80038C



"Initial Counter" shall be non-repeating. No counter value shall
be repeated across multiple messages with the same secret
key.


IVs shall be unpredictable. Repeating IVs leak information
about whether the first one or more blocks are shared between
two messages, so IVs should be non-repeating in such
situations.


IVs shall be non-repeating and shall not be generated by
invoking the cipher on another IV.


IVs should be non-repeating as repeating IVs leak information
about the first plaintext block and about common shared
prefixes in messages.


No IV. Tweak values shall be non-negative integers, assigned
consecutively, and starting at an arbitrary non-negative
integer.


No IV


No IV


No IV. Nonces shall be non-repeating.


IV shall be non-repeating. The number of invocations of GCM

implementation only uses 96-bit IVs (default length).



Galois Counter Mode (GCM) SP 80038D


# **Appendix H - Use Case Templates**

**H.1 Enterprise-owned device for general-purpose enterprise use and limited**
**personal use**


The configuration for _[USE CASE 1] Enterprise-owned device for general-purpose enterprise use and limited_
_personal use_ modifies the base requirements as follows:
From FCS_STG_EXT.1.2:

Do not choose:

  - the user
From FMT_SMF.1.1:

Include 21 in the ST
Include 25 in the ST and :

 - for the 1 [st] assignment, Include personal Hotspot connections in the assignment
Include 36 in the ST
Include 39 in the ST and :

 - select USB mass storage mode
Include 41 in the ST and :

 - select USB tethering
Include FPT_BBD_EXT.1 in the ST
Include FPT_TST_EXT.2/POSTKERNEL in ST.
From FPT_TST_EXT.2.1/POSTKERNEL:

 - select all executable code

Include FPT_TUD_EXT.5 in the ST
Include FTA_TAB.1 in the ST


**H.2 Enterprise-owned device for specialized, high security use**


The configuration for _[USE CASE 2] Enterprise-owned device for specialized, high security use_ modifies the
base requirements as follows:






|DECISION B|CHOICE B1<br>From FCS_CKM_EXT.1.1:<br>* select symmetric<br>* select 256 bits|
|---|---|
|DECISION B|**CHOICE B2**<br>FromFCS_CKM_EXT.1.1:<br>* selectasymmetric<br>* select128 bits|


|DECISION C|CHOICE C1<br>From FCS_CKM_EXT.3.1:<br>* select symmetric KEKs<br>* select 256-bit|
|---|---|
|DECISION C|**CHOICE C2**<br>FromFCS_CKM_EXT.3.1:<br>* selectasymmetric KEKs<br>* for the1st assignment, 128|



DECISION A



**CHOICE A2**
From FCS_CKM.1.1:

 - select ECC schemes






|DECISION D|CHOICE D1<br>From FCS_CKM_EXT.1.1:<br>* select symmetric<br>* select 256 bits|
|---|---|
|DECISION D|**CHOICE D2**<br>FromFCS_CKM_EXT.1.1:<br>* selectasymmetric<br>* select192 bits|


|DECISION E|CHOICE E1<br>From FCS_CKM_EXT.3.1:<br>* select symmetric KEKs<br>* select 256-bit|
|---|---|
|DECISION E|**CHOICE E2**<br>FromFCS_CKM_EXT.3.1:<br>* selectasymmetric KEKs<br>* for the1st assignment, 128|





[From the Functional Package for Transport Layer Security (TLS):](https://www.niap-ccevs.org/Profile/Info.cfm?PPID=439&id=439)
From FCS_TLSS_EXT.1.3:

 - select ECDHE parameters using elliptic curves [ **selection** : _secp256r1_, _secp384r1_,
_secp521r1_ ] and no other curves

 - select secp384r1


|DECISION F|CHOICE F1<br>From FCS_CKM.2.1/LOCKED:<br>* select Elliptic curve-based key establishment schemes<br>* select Pair-Wise Key Establishment Schemes Using Discrete Logarithm Cryptography|
|---|---|
|DECISION F|**CHOICE F2**<br>FromFCS_CKM.2.1/LOCKED:<br>* selectRSA-based key establishment schemes|



From FCS_COP.1.1/ENCRYPT:

 - select 256-bit key sizes
From FCS_RBG_EXT.1.2:

 - select 256 bits
From FDP_DAR_EXT.1.2:

 - select 256






|DECISION G|CHOICE G1<br>From FIA_X509_EXT.2.2:<br>* select allow the administrator to choose|
|---|---|
|DECISION G|**CHOICE G2**<br>FromFIA_X509_EXT.2.2:<br>* selectnot accept the certificate|



From FMT_SMF.1.1:

Include 3 in the ST
From 4:

 - for the 1 [st] assignment, assign all radios on TSF
From 5:

 - for the 1 [st] assignment, assign all audio or visual collection devices on TSF
Include 19 in the ST
Include 21 in the ST
Include 44 in the ST


**H.3 Personally-owned device for personal and enterprise use**


The configuration for _[USE CASE 3] Personally-owned device for personal and enterprise use_ modifies the
base requirements as follows:






|DECISION H|CHOICE H1<br>From FMT_SMF.1.1:<br>From 3:<br>* select on a per-app basis|
|---|---|
|DECISION H|**CHOICE H2**<br>FromFMT_SMF.1.1:<br>From3:<br>* selecton a per-group of applications processes basis|
|DECISION H|**CHOICE H3**<br>FromFMT_SMF.1.1:<br>From3:<br>* selecton a per-app basis<br>* selecton a per-group of applications processes basis|




|Col1|CHOICE I2<br>From FMT_SMF.1.1:<br>From 5:<br>* select on a per-group of applications processes basis|
|---|---|
|||
||**CHOICE I3**<br>FromFMT_SMF.1.1:<br>From5:<br>* selecton a per-app basis<br>* selecton a per-group of applications processes basis|



From FMT_SMF.1.1:

Include 17 in the ST
Include 28 in the ST
Include 44 in the ST
From FMT_SMF_EXT.2.1:

 - select remove Enterprise applications
From FDP_ACF_EXT.1.2:

 - select groups of applications

Include FDP_ACF_EXT.2 in the ST


**H.4 Personally-owned device for personal and limited enterprise use**


The use case _[USE CASE 4] Personally-owned device for personal and limited enterprise use_ makes no
changes to the base requirements.


# **Appendix I - Acronyms**

**Acronym** **Meaning**


AEAD Authenticated Encryption with Associated Data


AES Advanced Encryption Standard


ANSI American National Standards Institute


AP Application Processor


API Application Programming Interface


ASLR Address Space Layout Randomization


BAF Biometric Authentication Factor


Base-PP Base Protection Profile


BP Baseband Processor


BR/EDR (Bluetooth) Basic Rate/Enhanced Data Rate


BYOD Bring Your Own Device


CA Certificate Authority


CBC Cipher Block Chaining


CC Common Criteria


CCM Counter with CBC-Message Authentication Code


CCMP CCM Protocol


CEM Common Evaluation Methodology


CMC Certificate Management over Cryptographic Message Syntax (CMS)


cPP Collaborative Protection Profile


CPU Central Processing Unit


CRL Certificate Revocation List


CSP Critical Security Parameter


DAR Data At Rest


DEK Data Encryption Key


DEK Data Encryption Key


DEP Data Execution Prevention


DH Diffie-Hellman


DNS Domain Name System


DSA Digital Signature Algorithm


DTLS Datagram Transport Layer Security


EAP Extensible Authentication Protocol


EAPOL EAP Over LAN


ECDH Elliptic Curve Diffie Hellman


ECDSA Elliptic Curve Digital Signature Algorithm


EEPROM Electrically Erasable Programmable Read-Only Memory


EP Extended Package


EST Enrollment over Secure Transport


FEK File Encryption Key


FFC Finite Field Cryptography


FIPS Federal Information Processing Standards


FM Frequency Modulation


FP Functional Package


FQDN Fully Qualified Domain Name


GCM Galois Counter Mode


GPS Global Positioning System


GPU Graphics Processing Unit


HDMI High Definition Multimedia Interface


HMAC Keyed-Hash Message Authentication Code


HTTPS HyperText Transfer Protocol Secure


IEEE Institute of Electrical and Electronics Engineers


IP Internet Protocol


IPC Inter-Process Communication


IPsec Internet Protocol Security


KAT Known Answer Test


KDF Key Derivation Function


KEK Key Encryption Key


LE (Bluetooth) Low Energy


LTE Long Term Evolution


MD Mobile Device


MDM Mobile Device Management


MMI Man-Machine Interface


MMS Multimedia Messaging Service


MMU Memory Management Unit


NFC Near Field Communication


NIST National Institute of Standards and Technology


NTP Network Time Protocol


NX Never Execute


OCSP Online Certificate Status Protocol


OE Operational Environment


OID Object Identifier


OS Operating System


OTA Over the Air


PAE Port Access Entity


PBKDF Password-Based Key Derivation Function


PD Protected Data


PIV Personal Identity Verification


PMK Pairwise Master Key


PP Protection Profile


PP-Configuration Protection Profile Configuration


PP-Module Protection Profile Module


PRF Pseudorandom Function


PSK Pre-Shared Key


PTK Pairwise Temporal Key


RA Registration Authority


RBG Random Bit Generator


REK Root Encryption Key


ROM Read-only memory


RSA Rivest Shamir Adleman Algorithm


SAFAR System Authentication False Accept Rate


SAR Security Assurance Requirement


SFR Security Functional Requirement


SHA Secure Hash Algorithm


SMS Short Messaging Service


SoC System On a Chip


SPI Security Parameter Index


SSH Secure Shell


SSID Service Set Identifier


ST Security Target


TLS Transport Layer Security


TOE Target of Evaluation


TSF TOE Security Functionality


TSFI TSF Interface


TSS TOE Summary Specification


URI Uniform Resource Identifier


USB Universal Serial Bus


USSD Unstructured Supplementary Service Data


VPN Virtual Private Network


XCCDF eXtensible Configuration Checklist Description Format


XTS XEX (XOR Encrypt XOR) Tweakable Block Cipher with Ciphertext Stealing


# **Appendix J - Bibliography**

**Identifier** **Title**






[CEM] Common Evaluation Methodology for Information Technology Security - Evaluation
[Methodology, CCMB-2012-09-004, Version 3.1, Revision 5, April 2017.](http://www.commoncriteriaportal.org/files/ccfiles/CEMV3.1R5.pdf)


# **Appendix K - Acknowledgments**

This protection profile was developed by the Mobility Technical Community with representatives from
industry, U.S. Government agencies, Common Criteria Test Laboratories, and international Common Criteria
schemes. The National Information Assurance Partnership wishes to acknowledge and thank the members of
this group whose dedicated efforts contributed significantly to the publication. These organizations include:


**U.S. Government**
Defense Information Systems Agency (DISA)
CyberSecurity Directorate (CSD)
National Information Assurance Partnership (NIAP)
National Institute of Standards and Technology (NIST)


**International Common Criteria Schemes**
Australian Information Security Evaluation Program (AISEP)
Canadian Common Criteria Evaluation and Certification Scheme (CSEC)
Information-technology Promotion Agency, Japan (IPA)
UK IT Security Evaluation and Certificate Scheme (NCSC)


**Industry**
Apple, Inc.
BlackBerry
LG Electronics, Inc.
Microsoft Corporation
Motorola Solutions
Samsung Electronics Co., Ltd.
Other Members of the Mobility Technical Community


**Common Criteria Test Laboratories**
EWA-Canada, Ltd.
Gossamer Security Solutions



