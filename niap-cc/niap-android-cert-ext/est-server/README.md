# EST Validation Server — Setup Guide

> **Purpose**: EST (Enrollment over Secure Transport) server for testing NIAP / Common Criteria
> Android certificate enrollment. Cisco libest + NGINX in a single container.
>
> **Deployment options:**
>
> | Option | Environment | EST enroll | mTLS test | Docker required |
> |--------|------------|-----------|-----------|-----------------|
> | **Cloud Run** | Company device, any machine | ✅ | ❌ | No |
> | **Cloud Run + LB** | Company GCP with budget | ✅ | ✅ | No |
> | **Local Docker** | Personal dev machine only | ✅ | ✅ | Yes |
>
> `gcloud run deploy --source .` builds on Google Cloud Build —
> no local Docker installation required.

---

## Architecture Overview

```
Android device / emulator
    │
    │ HTTPS (mTLS optional) :8443   ← EST protocol
    │ HTTPS (mTLS required) :8081   ← mTLS test endpoint
    ▼
┌─────────────────────────────────────────┐
│ Docker container                        │
│                                         │
│  NGINX :8443 (HTTPS + mTLS optional)    │
│    └─ proxy ──▶ libest :8085            │
│                   └─ issues certs (CA)  │
│                                         │
│  NGINX :8081 (HTTPS + mTLS required)    │
│    └─ /protected/  ← mTLS test          │
│                                         │
│  NGINX :8080 (HTTP)                     │
│    └─ /cacert.pem  ← CA cert download   │
│    └─ /health      ← health check       │
└─────────────────────────────────────────┘
    │
    │ HTTP :8080
    ▼
Developer browser (CA cert download)
```

### CA Certificate Role

A throwaway Root CA is auto-generated on first container start. This CA:
1. Signs the server's TLS certificate (used by NGINX)
2. Signs CSRs from Android clients (used by libest)

---

## Prerequisites

### Cloud Run deployment (no Docker needed)

| Tool | Check |
|------|-------|
| gcloud CLI | `gcloud version` |
| GCP project with billing enabled | — |

```bash
# Install gcloud CLI (example for macOS via Homebrew)
brew install --cask google-cloud-sdk
# Add to profile (e.g., ~/.zshrc): export PATH=/opt/homebrew/share/google-cloud-sdk/bin:"$PATH"
gcloud auth login
gcloud config set project <YOUR_PROJECT_ID>
```

### Local Docker (personal dev machine only)

| Tool | Version | Check |
|------|---------|-------|
| Docker | 20.x+ | `docker --version` |
| Docker Compose | v2+ | `docker compose version` |
| curl | any | `curl --version` |

Internet access is required at image build time (to clone libest). After startup the server runs fully offline.

---

## Certificate Profiles

Set `EST_CERT_PROFILE` in `docker-compose.yml`:

| Profile | Purpose | CA key | Server key | Signature | NIAP validator |
|---------|---------|--------|-----------|-----------|----------------|
| `general` | Basic EST verification | RSA-4096 | RSA-2048 | SHA-256 | ❌ not compliant |
| `niap` | NIAP/CC validation | ECDSA P-384 | ECDSA P-384 | SHA-384 | ✅ all checks pass |

**NIAP profile server certificate extensions:**
- AKID (2.5.29.35), SKID (2.5.29.14), KeyUsage (2.5.29.15) — FIA_X509_EXT.1.2
- EKU: `serverAuth` (1.3.6.1.5.5.7.3.1) — NiapCertValidator.kt
- EKU: `id-kp-cmcRA` (1.3.6.1.5.5.7.3.28) — FIA_XCU_EXT.1.md §5 EST-specific requirement
- basicConstraints: CA:FALSE

**Client certificates issued by libest (both profiles):**
- SHA-384 signature, AKID, SKID, KeyUsage, EKU (clientAuth)

---

## Setup

### 1. Check out the repository

```bash
git clone <this-repo>
cd niap-android-cert-ext/est-server
```

### 2. Build the Docker image

Clones and compiles libest from source. **First build takes 5–10 minutes.**

```bash
docker compose build
```

Verify the build log output:
```
=== SHA setting verification ===
default_md     = sha384        ← NIAP SHA-384 requirement in place
=== Build artifact ===
estserver: ELF 64-bit LSB executable  ← binary built successfully
```

> **Reproducibility**: After startup, check the libest commit used with:
> `docker exec est-validation-server cat /opt/estserver/LIBEST_COMMIT`
> Record this in your test report.

### 3. Start the server

```bash
docker compose up -d
```

Wait for the health check to pass:
```bash
docker compose ps
# est-validation-server ... healthy
```

Watch the CA initialization log:
```bash
docker compose logs -f
```

Expected output:
```
[est-entrypoint] CA initialization complete. CA certificate info:
[est-entrypoint] EST server startup confirmed.
[est-entrypoint] Starting NGINX [profile: general]
[est-entrypoint]   EST  (HTTPS + mTLS): https://localhost:8443/.well-known/est/
[est-entrypoint]   CA cert:             http://localhost:8080/cacert.pem
```

### 4. Smoke test (curl)

```bash
# Health check
curl http://localhost:8080/health
# → OK

# CA cert info
curl -s http://localhost:8080/cacert.pem | openssl x509 -noout -subject -dates
# → subject=C=JP, ST=Tokyo, O=NIAP Test Lab, CN=EST Validation Root CA

# EST /cacerts endpoint
curl -ks --cacert <(curl -s http://localhost:8080/cacert.pem) \
    https://localhost:8443/.well-known/est/cacerts \
    | openssl base64 -d | openssl pkcs7 -inform DER -print_certs -noout

# EST /simpleenroll
openssl req -newkey rsa:2048 -nodes -keyout /tmp/client.key \
    -subj "/CN=test-client" -out /tmp/client.csr 2>/dev/null
curl -sk \
    --cacert <(curl -s http://localhost:8080/cacert.pem) \
    -u "estuser:estpwd" \
    -H "Content-Type: application/pkcs10" \
    --data-binary @/tmp/client.csr \
    https://localhost:8443/.well-known/est/simpleenroll \
    | openssl base64 -d | openssl pkcs7 -inform DER -print_certs -noout
# → subject=CN=test-client
```

### 4.5. Test the mTLS endpoint manually (curl)

Once you have successfully enrolled a client certificate in the previous step, you can verify the mTLS enforcement endpoint on port `8081` using standard command line tools.

1. **Extract the client certificate to a PEM file**:
   ```bash
   # Convert the returned PKCS#7 bundle from simpleenroll into standard PEM format
   curl -sk \
       --cacert <(curl -s http://localhost:8080/cacert.pem) \
       -u "estuser:estpwd" \
       -H "Content-Type: application/pkcs10" \
       --data-binary @/tmp/client.csr \
       https://localhost:8443/.well-known/est/simpleenroll \
       | openssl base64 -d | openssl pkcs7 -inform DER -print_certs -out /tmp/client.crt
   ```

2. **Verify the issued certificate meets NIAP requirements (SHA-384 / ECDSA P-384 / clientAuth)**:
   ```bash
   openssl x509 -in /tmp/client.crt -noout -text | grep -E "Signature Algorithm|Public Key Algorithm|ASN1 OID|Extended Key Usage" -A 1
   # Expected Output:
   #   Signature Algorithm: ecdsa-with-SHA384
   #   Public Key Algorithm: id-ecPublicKey
   #       ASN1 OID: secp384r1
   #   Extended Key Usage:
   #       TLS Web Client Authentication
   ```

3. **Establish an mTLS session using the issued client key and cert**:
   ```bash
   curl -v -k --cacert <(curl -s http://localhost:8080/cacert.pem) \
       --key /tmp/client.key \
       --cert /tmp/client.crt \
       https://localhost:8081/protected/
   
   # Expected HTTP Response: 200 OK
   # Body output should contain:
   #   mTLS OK
   #   Client: C=JP, ST=Tokyo, O=NIAP Test Lab, CN=test-client
   ```

---


## Switching to NIAP Profile

```bash
# Edit docker-compose.yml: set EST_CERT_PROFILE=niap, then restart
docker compose down
docker compose up -d
docker compose logs -f  # watch NIAP cert generation

# Verify server cert NIAP extensions
docker exec est-validation-server \
    openssl x509 -in /opt/estserver/estCA/private/estserver.crt -noout -text \
    | grep -A 5 "Extended Key Usage"
# → Server Authentication (1.3.6.1.5.5.7.3.1) ✓
# → 1.3.6.1.5.5.7.3.28 (cmcRA) ✓
```

---

## Android App Integration

### 5. Download and deploy the CA cert

The Android emulator reaches the host machine at `10.0.2.2`. For a real device on USB,
use the host machine's LAN IP or set up ADB port forwarding.

```bash
# Download CA cert
curl -o cacert.pem http://localhost:8080/cacert.pem

# Place in app's raw resources
cp cacert.pem \
    ../cert-test-app/src/main/res/raw/est_validation_ca.pem
```

### 6. Configure the Android app

| Setting | Value |
|---------|-------|
| EST server URL (emulator) | `https://10.0.2.2:8443/.well-known/est/` |
| EST server URL (real device) | `https://<host-LAN-IP>:8443/.well-known/est/` |
| Trust anchor | downloaded `cacert.pem` |
| Auth | `estuser:estpwd` |

### 7. Connectivity check from the device

```bash
# Emulator
adb shell nc -w 2 10.0.2.2 8443 < /dev/null; echo $?   # 0 = connected

# Real device via ADB port forward
adb reverse tcp:8443 tcp:8443
adb reverse tcp:8080 tcp:8080
# then use https://localhost:8443/.well-known/est/ in the app
```

### 8. Manual test with the cert-test-app (real device)

Set up ADB port forwarding first (run on the host machine each time you reconnect the device):

```bash
adb reverse tcp:8443 tcp:8443   # EST/HTTPS (NGINX → Host)
adb reverse tcp:8081 tcp:8081   # mTLS test endpoint (NGINX → Host)
adb reverse tcp:8080 tcp:8080   # HTTP admin / CA cert download (NGINX → Host)
```

App settings to use:

| Field | Value |
|-------|-------|
| EST Server URL | `https://localhost:8443/.well-known/est/` |
| Auth Token | `estuser:estpwd` |
| CA PEM URL | `http://localhost:8080/cacert.pem` |
| mTLS Endpoint | `https://localhost:8081/protected/` |

The app downloads the CA PEM automatically from the CA PEM URL on each Enroll attempt.
Leave CA PEM URL blank only if you have embedded the CA cert as a raw resource (`est_validation_ca`) in the app.

> **Note**: `adb reverse` must be re-run after every device reconnect (USB unplug/replug or `adb kill-server`).

---

## Troubleshooting

### Build fails

**Symptom**: errors during `autogen.sh` or `make`

- Docker memory allocation must be ≥ 4 GB (Docker Desktop → Settings → Resources)
- Internet access required for libest clone

```bash
# Rebuild without cache
docker compose build --no-cache
```

### `estserver` exits immediately

**Symptom**: log shows "EST server exited immediately"

**Known root cause (ARM64 / Apple Silicon)**: libest's `estserver.c` declares `char c` for the
getopt return value. On ARM64 Linux, `char` is unsigned by default, so getopt's end-of-options
sentinel (-1) becomes 255, falls through to `default:`, and calls `show_usage_and_exit()`.
Fixed by building with `CFLAGS="-fsigned-char"` (already in the Dockerfile).

Other causes:
- `libest.so` link error → `ldd /usr/local/bin/estserver`
- CA directory permission issue → `ls -la /opt/estserver/estCA/`

```bash
# Manual test inside container
docker exec -it est-validation-server bash
cd /opt/estserver
estserver -p 8085 \
    -c estCA/private/estservercertandkey.pem \
    -k estCA/private/estservercertandkey.pem \
    -r estrealm -v
```

### Cannot connect from Android

**Symptom**: TLS handshake error or timeout

1. Check TCP connectivity: `adb shell nc -w 2 10.0.2.2 8443 < /dev/null`
2. Test with curl: `curl -v --cacert cacert.pem https://10.0.2.2:8443/.well-known/est/cacerts`
3. Verify the `cacert.pem` in the app matches the current container CA

> **Note**: `docker compose down` → `up` generates a **new CA**. Re-download `cacert.pem`
> and update the app's trust anchor each time you recreate the container.

### Persist CA across restarts

```bash
docker run -d \
    -v est-ca-data:/opt/estserver/estCA \
    -p 8443:8443 -p 8080:8080 \
    est-validation-server:local
```

---

## Stop and Clean Up

```bash
docker compose down

# Remove image too (requires rebuild)
docker compose down --rmi local
```

---

## Cloud Run Deployment (optional)

> **Note on TLS trust**: Cloud Run presents a Google-signed certificate. When using this endpoint
> from the Android app, leave CA PEM URL blank or do not apply the downloaded CA cert as the TLS
> trust anchor — use the system trust store instead. The CA PEM is still needed to validate issued
> client certificates.

---

### Architecture and limitations

Cloud Run terminates TLS at Google's infrastructure before the request reaches the container.
As a result:

| Feature | Cloud Run only | Cloud Run + LB |
|---------|---------------|----------------|
| EST enroll (`/simpleenroll`) | ✅ | ✅ |
| CA cert download (`/cacert.pem`) | ✅ | ✅ |
| mTLS test (`/protected/`) | ❌ | ✅ |

**Why mTLS does not work on Cloud Run alone:**
The client certificate is stripped at the Cloud Run proxy — nginx inside the container never
receives it, so `ssl_verify_client` has no effect.

---

### Option A: Cloud Run only (EST enroll, no mTLS test)

This is the current setup. **Local Docker is not required** — `gcloud run deploy --source .`
builds the container on Google Cloud Build, so deployment works from any machine with the
gcloud CLI installed (including company devices where Docker is unavailable).

For Android app configuration when using Cloud Run:
- EST Server URL: `https://<service-url>/.well-known/est/`
- CA PEM URL: leave blank (Cloud Run presents a Google-signed cert; system trust store is used)
- mTLS endpoint: requires local Docker or Option B (Cloud LB)

---

### Option B: Cloud Run + Cloud Load Balancer (full mTLS on cloud) — requires budget approval

> This option is documented for reference when deploying to a company GCP environment with
> an approved budget. The LB forwarding rule incurs a fixed charge (~$18/month) regardless
> of traffic volume — not suitable for personal accounts or short-term validation.

Add a Cloud Load Balancer in front of Cloud Run to perform mTLS termination.
The LB verifies the client certificate and forwards cert info as an HTTP header
(`X-Forwarded-Client-Cert`) to Cloud Run, where nginx reads it.

```
Android device
    │ HTTPS + client cert (mTLS)
    ▼
Cloud Load Balancer
    │ - Verifies client cert against EST CA
    │ - Adds X-Forwarded-Client-Cert header
    ▼
Cloud Run :8080
    │
    ▼
nginx /protected/
    - Checks X-Forwarded-Client-Cert header
    - 200 if present, 401 if absent
```

#### nginx change for `/protected/` on port 8080

```nginx
location /protected/ {
    if ($http_x_forwarded_client_cert = "") {
        return 401 "mTLS required\n";
    }
    add_header Content-Type "text/plain";
    return 200 "mTLS OK\nClient-Cert: $http_x_forwarded_client_cert\n";
}
```

#### Cloud LB setup

```bash
# 1. Create a serverless NEG for Cloud Run
gcloud compute network-endpoint-groups create est-neg \
    --region=asia-northeast1 \
    --network-endpoint-type=serverless \
    --cloud-run-service=est-validation-server

# 2. Create backend service
gcloud compute backend-services create est-backend \
    --load-balancing-scheme=EXTERNAL_MANAGED \
    --global

gcloud compute backend-services add-backend est-backend \
    --network-endpoint-group=est-neg \
    --network-endpoint-group-region=asia-northeast1 \
    --global

# 3. Create URL map, HTTPS proxy, forwarding rule
gcloud compute url-maps create est-url-map --default-service=est-backend
gcloud compute ssl-certificates create est-cert --domains=<YOUR_DOMAIN>
gcloud compute target-https-proxies create est-https-proxy \
    --url-map=est-url-map \
    --ssl-certificates=est-cert
gcloud compute forwarding-rules create est-forwarding-rule \
    --load-balancing-scheme=EXTERNAL_MANAGED \
    --target-https-proxy=est-https-proxy \
    --global --ports=443

# 4. Enable mTLS: upload EST CA cert as trust config
gcloud certificate-manager trust-configs create est-trust-config \
    --location=global \
    --ca-certs=<(curl -s https://<service-url>/cacert.pem)

gcloud compute target-https-proxies update est-https-proxy \
    --server-tls-policy=... # attach trust config via server TLS policy
```

> Once approved and deployed, this setup enables full cloud-based mTLS testing without
> requiring Docker on the test device.

---

### Prerequisites (first-time setup)

1. Create a GCP project and enable billing
2. Install and configure gcloud CLI:
   ```bash
   # Example for macOS via Homebrew; adjust for other OS platforms:
   brew install --cask google-cloud-sdk
   # Add to profile configuration:
   # export PATH=/opt/homebrew/share/google-cloud-sdk/bin:"$PATH"
   source ~/.zshrc
   gcloud auth login
   gcloud config set project <YOUR_PROJECT_ID>
   ```
3. Grant required IAM permissions to the default Compute Engine service account
   (needed once per project — Cloud Run source deploy requires these):
   ```bash
   SA="<PROJECT_NUMBER>-compute@developer.gserviceaccount.com"
   gcloud projects add-iam-policy-binding <YOUR_PROJECT_ID> --member="serviceAccount:$SA" --role="roles/cloudbuild.builds.builder"
   gcloud projects add-iam-policy-binding <YOUR_PROJECT_ID> --member="serviceAccount:$SA" --role="roles/storage.objectAdmin"
   gcloud projects add-iam-policy-binding <YOUR_PROJECT_ID> --member="serviceAccount:$SA" --role="roles/artifactregistry.writer"
   ```
   > Find your project number: `gcloud projects describe <YOUR_PROJECT_ID> --format="value(projectNumber)"`

### Deploy

```bash
cd est-server
gcloud run deploy est-validation-server \
    --source . \
    --region asia-northeast1 \
    --allow-unauthenticated \
    --max-instances 1 \
    --memory 512Mi \
    --port 8080
```

After deployment:
- CA cert: `https://<service-url>/cacert.pem`
- Health: `https://<service-url>/health`
- EST enroll: `https://<service-url>/.well-known/est/`
- mTLS test (`/protected/`): requires Cloud LB (Option B) or local Docker

---

## Packet Capture (PCAP) for NIAP/CC Verification

To provide cryptographic verification of the TLS and mTLS handshakes (e.g., confirming TLS 1.3 usage, client cert submission, and SHA-384 algorithms in TLS 1.2 `CertificateVerify`), evaluators can record traffic within the container using `tcpdump`.

1. **Start packet capture on target interfaces inside the container**:
   ```bash
   docker exec -it est-validation-server tcpdump -i any -w /opt/estserver/handshake.pcap "port 8443 or port 8081"
   ```

2. **Execute your test case** (either through `curl` or the Android client app).

3. **Stop the capture** with `Ctrl+C`.

4. **Copy the PCAP file to the host machine for Wireshark analysis**:
   ```bash
   docker cp est-validation-server:/opt/estserver/handshake.pcap ./handshake.pcap
   ```

---

## File Layout


```
est-server/
├── Dockerfile          Multi-stage build: compiles Cisco libest from source
├── docker-compose.yml  Local dev/test Compose definition
├── nginx.conf          mTLS reverse proxy (ports 8443/8080)
├── entrypoint.sh       CA init → estserver start → NGINX start
└── README.md           This guide
```

---

## Version Info

| Component | Version |
|-----------|---------|
| Base image | ubuntu:22.04 |
| libest | main branch (commit pinned at build time) |
| NGINX | Ubuntu 22.04 package |
| OpenSSL | Ubuntu 22.04 package (libssl3) |
| Signature algorithm | SHA-384 (NIAP requirement) |
| ARM64 fix | `CFLAGS=-fsigned-char` (unsigned char getopt bug) |
