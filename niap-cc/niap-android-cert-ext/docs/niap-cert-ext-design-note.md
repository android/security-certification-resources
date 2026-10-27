# NIAP Certificate Extension Library Design Note

**Agent / Author**: Antigravity (Draft)

**1. Introduction**

* **1.1 Goal and Scope:** The primary goal of this project is to implement strict X.509 certificate validation and management functions required for NIAP compliance on Android. This library will act as an extension to the Android platform's native capabilities to satisfy specific `FIA_X509_EXT` and `FIA_XCU_EXT` requirements.
* **1.2 Audience:** Internal developers and evaluation authorities.
* **1.3 Definitions and Acronyms:**
  * **TOE**: Target of Evaluation (The system/app running on the Android device being evaluated).
  * **NIAP**: National Information Assurance Partnership.
  * **CSR**: Certificate Signing Request.
  * **EST**: Enrollment over Secure Transport (RFC 7030).
  * **AKID/SKID**: Authority Key Identifier / Subject Key Identifier.

**2. Background and Motivation**

* **2.1 Problem Statement:** 
  * The standard Android security provider (Conscrypt) is lenient regarding some X.509 extensions and does not strictly enforce all validation rules required by NIAP (e.g., presence of specific extensions, Name Constraints).
  * Additionally, NIAP requires the TOE to support CSR issuance and certificate acquisition, but native Android devices do not provide these functions by default. Therefore, these features must be added to the TOE.
  * *(Japanese reference: また、CSRの発行や証明書の取得がNIAPではTOEに要求されるがAndroidデバイスはこの機能を持たないため追加の必要がある)*
* **2.2 Justification:** 
  * To achieve NIAP certification, we need to complement Conscrypt's behavior with a library that performs strict post-handshake checks.
  * We need to provide standard-compliant mechanisms for certificate enrollment (CSR and EST) within the TOE.
* **2.3 Requirements:** 
  * **OS**: Android OS (Targeting TOE environment).
  * **Standards**: RFC 8603 (CNSA) algorithm constraints, RFC 9580 (relevant if OpenPGP style headers are used, but primarily standard X.509/TLS focus here).
  * Support for custom TrustAnchor and isolated TrustStore.
* **2.4 Schedule:** 
  * *TBD (To be defined after design finalization)*

**3. High-Level Design and Architecture**

* **3.1 System Context:** 
  * The library will be integrated into the TOE (Android application). It will be used in conjunction with HTTP clients like OkHttp or direct `SSLSocket` usage to enforce strict security policies.
* **3.2 Architectural Overview:** 
  * **Module 1: Validation Core (Android Archive - AAR)**
    * **NiapCertValidator**: Core logic for strict X.509 checks. Specific restrictions to be enforced include:
      * **Algorithm Constraints**: Restrict to RFC 8603 (CNSA) profiles (e.g., enforcing SHA-384 with ECDSA P-384).
      * **Mandatory Extension Checks**: Verify presence of AKID (Authority Key Identifier), SKID (Subject Key Identifier), and KeyUsage.
      * **Field Enforcement**: Strict checking of Basic Constraints, EKU (Extended Key Usage), and Name Constraints (which Conscrypt may ignore).
      * **Revocation Checks**: Enforce OCSP Stapling and support fallback to Out-of-Band CRL fetching.
    * **Network Security Features** (Carried over from NIAPSEC/SecureURL):
      * Enforce HTTPS (blocking non-secure connections).
      * Hostname and TLD validation.
    * **Config Support**: Ability to read these strict validation rules and policies from an **XML file placed in resources** (e.g., `res/xml/niap_security_config.xml`).
    * **Helpers / Integration**: Provide easy integration for:
      * **OkHttp3**: Via `Interceptor` or custom `X509TrustManager`.
      * **Ktor**: Via engine-specific TLS configuration using custom `TrustManager`.
      * **java.net**: Via custom `SSLSocketFactory` applied to `HttpsURLConnection`.
  * **Module 2: Certificate Management (Execution Module)**
    * **CSR Generator**: Generates PKCS#10 CSRs using Bouncy Castle.
    * **EST Client**: Handles certificate acquisition via EST.
    * **Form Factor Options**:
      * *Option A (Library)*: Included in the AAR, called directly by the app.
      * *Option B (Service)*: An Android Service in a dedicated application (APK) to satisfy the "executable" requirement and allow IPC (Inter-Process Communication).
* **3.3 Key Design Decisions:** 
  * **Language**: Java / Kotlin for Android SDK.
  * **Libraries**: Bouncy Castle (for CSR generation and advanced crypto operations not supported by default Android providers).
  * **Integration**: Transport-agnostic validator to allow use in different layers (Socket, HTTP).
