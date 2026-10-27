# NIAP Android Certificate Extension & Lifecycle Validator

A comprehensive Android security framework and service designed to enforce strict **NIAP (National Information Assurance Partnership)** Common Criteria requirements for X.509 certificate validation and secure certificate lifecycle management.

---

## 📖 Background & Problem

By default, Android's core security provider (**Conscrypt**) is optimized for general web traffic and does not enforce the stringent constraints defined in the [NIAP X.509 Functional Package (FIA_X509_EXT.1 / FIA_XCU_EXT.1)](https://www.niap-ccevs.org/static_html/protection-profile/511/Functional%20Package%20for%20X.509_v1.0.html). For example:
* It permits signature algorithms and key lengths weaker than SHA-384 / 3072-bit RSA.
* It does not mandate essential extensions like Authority Key Identifier (AKID), Subject Key Identifier (SKID), or specific Key Usages.
* It does not strictly restrict wildcard SAN alignments on critical Top-Level Domains (TLDs).

This framework addresses these gaps by providing:
1. **A strict validation layer** (`cert-lib`) that seamlessly overrides standard SSL trust behaviors.
2. **A secure, hardware-backed certificate manager service** (`cert-manager`) supporting standardized protocols like **EST (Enrollment over Secure Transport)** for acquiring and managing enterprise/government-issued credentials while keeping private key material safely isolated in secure hardware.

---

## 🏛️ System Architecture & Module Layout

The project is structured into modular units, separating core validator logic, stateful lifecycle services, interactive test applications, and automated device-level test runners.

```
niap-android-cert-ext/
├── cert-lib/             # Core Android library (Validator & Client Manager API)
├── cert-manager/         # Foreground Service & signing oracle (EST client & KeyStore engine)
├── validator-test-app/   # WorkManager-based network testing application (strict/relaxed flavors)
├── cert-test-app/        # Interactive Jetpack Compose application for manual verification
├── agent-test/           # JUnit automated test suites designed for TestBed Core
└── est-server/           # Pre-configured local EST test environment (Cisco libest & NGINX)
```

### 1. Core Validation (`cert-lib`)
The foundation of the project, providing strict validation engines:
* **`NiapCertValidator`**: The core inspector that processes a certificate chain and verifies signature algorithms, key strengths, certificate basic constraints, required EKUs, mandatory extensions, and wildcard SAN constraints.
* **`NiapX509TrustManager`**: A custom `X509TrustManager` that wraps existing platform managers and runs active chain inspections on top of them.
* **`NiapCertHelper`**: A high-level orchestrator that parses policy configuration from XML resources and configures networking clients (`OkHttpClient`, `HttpsURLConnection`) with the proper socket factories and verifiers.
* **`NiapCertManager` & `NiapKeyManager`**: Client-side integration API that enables consumer applications to seamlessly fetch hardware-backed cryptographic identities from the `cert-manager` service via standard JCA interfaces without handling raw private keys.

### 2. Lifecycle Service (`cert-manager`)
A background service package configured as a Foreground Service (`NiapCertService`) that acts as a secure certificate repository and remote signing oracle:
* **Protocol-Agnostic Acquisition**: Implements key generation (ECDSA P-384 / RSA 3072) inside the secure **Android KeyStore** and requests certificates from external servers via **EST (RFC 7030)**.
* **Remote Signing Provider**: Exposes a custom `RemoteSigningProvider` and standard AIDL service interface (`INiapCertManager.aidl`). When a client app makes mTLS calls, cryptographic signatures are delegated to the service via a secure Binder IPC channel. **Private key material never leaves the secure service context.**
* **Validation & Storage**: Validates incoming certificates against local policies before writing them into the secure KeyStore engine.

### 3. Interactive Client (`cert-test-app`)
A Jetpack Compose-based user application created for developers and evaluators to:
* Configure connection settings (EST endpoints, target mTLS servers, CA credentials).
* Trigger interactive certificate enrollment, view real-time state machines, inspect cryptographic metadata (Subject, Issuer, Serial, Signature Algorithm, Validity).
* Test live mTLS endpoints and view detailed client logs immediately.

### 4. Automated Testbed (`agent-test`)
A JUnit test suite designed to register and execute device-level test cases in collaboration with the **TestBed Core** automation system:
* **`FiaX509ValidatorTest`** (`NiapValidatorTest` class): Installs strict/relaxed flavors of `validator-test-app` and tests web targets to verify that non-compliant SHA-256 endpoints are successfully blocked in strict mode and accepted in relaxed mode.
* **`CertManagerTest`**: Automates full-cycle local EST enrollment, setups ADB reverse tunnels, validates state transitions, and conducts mutual TLS authentications against local mock services.

---

## ⚙️ Declarative Security Policy Configuration

Validation rules are loaded declaratively from an XML resource (typically `res/xml/niap_security_config.xml`). This design allows administrators to configure strict security policies without altering any source code.

```xml
<?xml version="1.0" encoding="utf-8"?>
<niap-security-config>
    <!-- Enforce strict signature algorithm constraints (SHA-384 or stronger) as per RFC 8603 (CNSA). -->
    <niap-rfc8603-strict-sigalg>true</niap-rfc8603-strict-sigalg>
    
    <!-- List of TLDs for which wildcard SANs are prohibited. -->
    <prohibited-tld-wildcards>
        <tld>*.COM</tld>
        <tld>*.NET</tld>
        <tld>*.ORG</tld>
        <tld>*.GOV</tld>
        <tld>*.EDU</tld>
    </prohibited-tld-wildcards>
    
    <!-- List of allowed cipher suites. If empty, platform defaults are used. -->
    <allowed-cipher-suites>
        <cipher>TLS_AES_128_GCM_SHA256</cipher>
        <cipher>TLS_AES_256_GCM_SHA384</cipher>
        <cipher>TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256</cipher>
        <cipher>TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256</cipher>
        <cipher>TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384</cipher>
        <cipher>TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384</cipher>
    </allowed-cipher-suites>
    
    <!-- Trust anchor policy (SYSTEM_CA_ONLY, USER_CA_ONLY, BOTH_CA). -->
    <trust-anchor-policy>BOTH_CA</trust-anchor-policy>
    
    <!-- Enforce TLS 1.2 or higher. -->
    <enforce-tls-1-2>true</enforce-tls-1-2>
    
    <!-- Enforce checks for mandatory extensions (AKID, SKID, KeyUsage). Required by FIA_X509_EXT.1.2. -->
    <enforce-mandatory-extensions>true</enforce-mandatory-extensions>
    
    <!-- Enforce check for Extended Key Usage (EKU) in the leaf. -->
    <enforce-eku>true</enforce-eku>
    
    <!-- Required Extended Key Usage (EKU) aliases ('serverAuth', 'clientAuth') or raw OIDs. -->
    <required-eku>serverAuth</required-eku>
    
    <!-- Enforce CA constraints (End-entity must not be a CA). -->
    <enforce-ca-constraints>true</enforce-ca-constraints>
</niap-security-config>
```

---

## 🚀 Getting Started

### Prerequisites
* **JDK 17** configured in your development environment.
* **Android SDK** with Platform-34 tools.
* **Docker** & **Docker Compose** (if running the local mock EST server).

### 🛠️ 1. Building the Modules

To build the core library AAR:
```bash
./gradlew :cert-lib:assembleRelease
```

To build the background service APK:
```bash
./gradlew :cert-manager:assembleDebug
```

To build the automation test modules:
```bash
# Build the test app (strict & relaxed flavors)
./gradlew :validator-test-app:assembleDebug

# Compile the automated JUnit test plugin
./gradlew :agent-test:jar
```
*Note: The `assembleDebug` tasks for `cert-manager`, `validator-test-app`, and `cert-test-app` are pre-configured to automatically copy their target debug APK outputs into the TestBed Core resources workspace (`../testbed-core/composeApp/resources/`) for automated installer stages.*

---

## 💻 Integration Guide

### 1. Custom HTTPS Client Connections (using the library directly)

Initialize the custom helper and apply it to network connections in your app code:

#### For OkHttp Clients:
```kotlin
val helper = NiapCertHelper.getInstance(context)
val okHttpClient = helper.configureOkHttp(OkHttpClient.Builder()).build()
```

#### For Standard HttpsURLConnection:
```kotlin
val url = URL("https://example.gov")
val helper = NiapCertHelper.getInstance(context)
val connection = helper.openConnection(url)

connection.hostnameVerifier = HostnameVerifier { hostname, session ->
    helper.checkHostname(hostname, session)
    true
}
connection.connect()
```

### 2. Consuming Hardware-Backed Credentials (using the service)

Connect to the background lifecycle service to retrieve standard JCA network artifacts:

```kotlin
// Initialize manager proxy (lazily binds background IPC service)
val certManager = NiapCertManager(context)

// Verify enrollment states
if (certManager.hasEnrollment("my_secure_identity")) {
    // Retrieve a standard X509KeyManager which delegates signing operations
    val keyManager = certManager.getKeyManager("my_secure_identity")
    
    // Create an SSLContext loaded with the secure key manager
    val sslContext = certManager.getSslContext("my_secure_identity", trustedCaPem = serverRootPem)
    
    // Wire standard clients directly (OkHttp, HttpsURLConnection, etc.)
    val client = OkHttpClient.Builder()
        .sslSocketFactory(sslContext.socketFactory, trustManager)
        .build()
}
```

---

## 🔬 Mock EST & Testbed Verification

For comprehensive local testing or official Common Criteria evaluations, setup the environment using the provided local tools.

### 1. Start the Local Compliance EST Server
The `est-server` directory contains a pre-packaged environment executing **Cisco libest** and **NGINX**. It generates compliant ECDSA P-384 certificates signed with SHA-384.

```bash
cd est-server
# Configure EST_CERT_PROFILE=niap in docker-compose.yml to enable full compliance mode
docker compose up -d
```

### 2. Configure Port Forwarding for Testing
When testing on standard physical hardware or external systems, run the ADB reverse utility to forward network requests:
```bash
adb reverse tcp:8443 tcp:8443   # EST HTTPS Endpoint (libest/NGINX)
adb reverse tcp:8081 tcp:8081   # Strict mTLS test endpoint
adb reverse tcp:8080 tcp:8080   # HTTP admin / CA cert download endpoint
```

### 3. Run Automated TestBed Suite
Ensure the **TestBed Core** desktop application and MCP server is launched on `localhost:11452`, then run the JUnit test plugins:
* Execute `NiapValidatorTest` in the `agent-test` module to verify that non-compliant and compliant signature contexts are correctly categorized on actual devices.
* Execute `CertManagerTest` to launch an automated flow: installing test apps, establishing adb reversals, performing EST registrations, validating KeyStore operations, and testing mTLS endpoints recursively.

---

## 📄 License

This project is licensed under the Apache License, Version 2.0 - see the [LICENSE](LICENSE) file for details.

