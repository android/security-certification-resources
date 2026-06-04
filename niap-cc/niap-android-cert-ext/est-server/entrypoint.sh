#!/bin/bash
# =============================================================================
# EST Validation Server — Entrypoint Script
#
# Certificate profile selection:
#   EST_CERT_PROFILE=general  (default) — basic protocol verification
#   EST_CERT_PROFILE=niap     — passes all NIAP/CC validator checks
#
# Directory layout (follows libest ESTcommon.sh conventions):
#   /opt/estserver/
#     estExampleCA.cnf                  ← CA config (used by openssl ca)
#     trustedcerts.crt                  ← EST_TRUSTED_CERTS (client cert verification)
#     estCA/
#       cacert.crt                      ← EST_CACERTS_RESP (CA cert returned by /cacerts)
#       index.txt, serial, newcerts/    ← OpenSSL CA database
#       private/
#         cakey.pem                     ← CA private key
#         estserver.key                 ← Server private key
#         estserver.crt                 ← Server certificate
#         estservercertandkey.pem       ← cert+key combined (used for -c/-k args)
#         estserver-chain.pem           ← cert+CACert chain (used by NGINX ssl_certificate)
#
# NIAP requirements (NiapCertValidator.kt):
#   - Signature: SHA-384 or stronger
#   - Key size: RSA-3072+ or ECDSA P-384+
#   - Mandatory extensions: AKID(2.5.29.35), SKID(2.5.29.14), KeyUsage(2.5.29.15)
#   - EKU: serverAuth (1.3.6.1.5.5.7.3.1)
#   - basicConstraints: CA:FALSE (leaf cert)
#   - EST-specific (FIA_XCU_EXT.1.md §5): cmcRA OID (1.3.6.1.5.5.7.3.28)
# =============================================================================
set -euo pipefail

WORK_DIR="/opt/estserver"
CA_DIR="${WORK_DIR}/estCA"
PRIV_DIR="${CA_DIR}/private"
PROFILE="${EST_CERT_PROFILE:-general}"
LOG_TAG="[est-entrypoint]"

log() { echo "${LOG_TAG} $*"; }
die() { echo "${LOG_TAG} ERROR: $*" >&2; exit 1; }

log "=================================================="
log " EST Validation Server starting"
log " Certificate profile: ${PROFILE}"
log "=================================================="

# =============================================================================
# Step 1: Write CA config file used by libest
#         (directory layout follows ESTcommon.sh variable conventions)
# =============================================================================
log "Writing estExampleCA.cnf..."
cat > "${WORK_DIR}/estExampleCA.cnf" << 'OPENSSL_CA_CNF_EOF'
# EST Sample CA config (NIAP/CC compliant)
# Path conventions follow ESTcommon.sh:
#   EST_OPENSSL_CAPRIVKEY = estCA/private/cakey.pem
#   EST_OPENSSL_CACERT    = estCA/cacert.crt

[ ca ]
default_ca = CA_default

[ CA_default ]
dir              = ./estCA
certs            = $dir/certs
crl_dir          = $dir/crl
database         = $dir/index.txt
new_certs_dir    = $dir/newcerts
certificate      = $dir/cacert.crt
serial           = $dir/serial
private_key      = $dir/private/cakey.pem
x509_extensions  = usr_cert
default_days     = 365
default_crl_days = 30
# NIAP requirement: SHA-384 or stronger (FIA_X509_EXT.1.1 / RFC 8603 CNSA)
default_md       = sha384
preserve         = no
policy           = policy_match
# Allow re-enrollment with the same subject name (required for EST /reenroll)
unique_subject   = no

[ policy_match ]
countryName            = optional
stateOrProvinceName    = optional
organizationName       = optional
organizationalUnitName = optional
commonName             = supplied
emailAddress           = optional

[ req ]
default_bits       = 3072
default_md         = sha384
distinguished_name = req_distinguished_name
attributes         = req_attributes

[ req_distinguished_name ]
countryName            = Country Name (2 letter code)
stateOrProvinceName    = State or Province Name
organizationName       = Organization Name
commonName             = Common Name

[ req_attributes ]

# ---------------------------------------------------------------------------
# [ usr_cert ] — extensions for client certs issued by libest via /simpleenroll
# Compliant with NIAP FIA_X509_EXT.1.2:
#   - basicConstraints: CA:FALSE (end-entity)
#   - AKID, SKID: required (2.5.29.35, 2.5.29.14)
#   - KeyUsage: required (2.5.29.15)
#   - EKU: clientAuth (for TLS client authentication)
# ---------------------------------------------------------------------------
[ usr_cert ]
basicConstraints       = CA:FALSE
subjectKeyIdentifier   = hash
authorityKeyIdentifier = keyid,issuer
keyUsage               = critical,digitalSignature
extendedKeyUsage       = clientAuth

[ v3_ca ]
basicConstraints       = critical,CA:true,pathlen:0
subjectKeyIdentifier   = hash
authorityKeyIdentifier = keyid:always,issuer
keyUsage               = critical,keyCertSign,cRLSign
OPENSSL_CA_CNF_EOF

log "estExampleCA.cnf written."

# =============================================================================
# Step 2: Initialize CA (first boot only)
# =============================================================================
if [ -f "${PRIV_DIR}/estservercertandkey.pem" ]; then
    log "CA already initialized. Skipping."
else
    log "=================================================="
    log " Initializing Certificate Authority [${PROFILE}]"
    log "=================================================="

    # Create directory layout expected by libest
    mkdir -p "${CA_DIR}/newcerts" "${CA_DIR}/certs" "${CA_DIR}/crl" "${PRIV_DIR}"
    chmod 700 "${PRIV_DIR}"
    touch    "${CA_DIR}/index.txt"
    echo "unique_subject = no" > "${CA_DIR}/index.txt.attr"
    echo "01" > "${CA_DIR}/serial"

    cd "${WORK_DIR}"

    if [ "${PROFILE}" = "niap" ]; then
        # ------------------------------------------------------------------
        # NIAP profile
        # CA: ECDSA P-384, SHA-384
        # ------------------------------------------------------------------
        log "[NIAP] Generating Root CA (ECDSA P-384, SHA-384)..."

        openssl ecparam -name secp384r1 -genkey -noout \
            -out "${PRIV_DIR}/cakey.pem"
        chmod 600 "${PRIV_DIR}/cakey.pem"

        openssl req -new -x509 \
            -sha384 \
            -days 3650 \
            -key "${PRIV_DIR}/cakey.pem" \
            -out "${CA_DIR}/cacert.crt" \
            -subj "/C=JP/ST=Tokyo/O=NIAP Test Lab/CN=EST Validation Root CA" \
            -extensions v3_ca \
            -config "${WORK_DIR}/estExampleCA.cnf"

        log "[NIAP] Generating server certificate (ECDSA P-384, SHA-384, full NIAP extensions)..."

        openssl ecparam -name secp384r1 -genkey -noout \
            -out "${PRIV_DIR}/estserver.key"
        chmod 600 "${PRIV_DIR}/estserver.key"

        # Server certificate extensions (mandatory NIAP extensions + EST-specific EKU)
        cat > "${WORK_DIR}/server_ext.cnf" << 'SERVER_EXT_EOF'
[ server_cert ]
# FIA_X509_EXT.1.2: AKID, SKID, KeyUsage required
basicConstraints        = CA:FALSE
subjectKeyIdentifier    = hash
authorityKeyIdentifier  = keyid,issuer
keyUsage                = critical,digitalSignature
# EKU:
#   serverAuth  (1.3.6.1.5.5.7.3.1)  — TLS server authentication (NiapCertValidator.kt)
#   id-kp-cmcRA (1.3.6.1.5.5.7.3.28) — EST-specific requirement (FIA_XCU_EXT.1.md §5)
extendedKeyUsage        = serverAuth,1.3.6.1.5.5.7.3.28
# SAN: Android emulator access (10.0.2.2 = host machine) and real device via host IP
subjectAltName          = @san_names

[ san_names ]
DNS.1 = est-server
DNS.2 = localhost
IP.1  = 127.0.0.1
IP.2  = 10.0.2.2

[ req ]
distinguished_name = req_dn
[ req_dn ]
SERVER_EXT_EOF

        openssl req -new \
            -sha384 \
            -key "${PRIV_DIR}/estserver.key" \
            -out "${CA_DIR}/estserver.csr" \
            -subj "/C=JP/ST=Tokyo/O=NIAP Test Lab/CN=est-server"

        openssl x509 -req \
            -sha384 \
            -days 397 \
            -in  "${CA_DIR}/estserver.csr" \
            -CA  "${CA_DIR}/cacert.crt" \
            -CAkey "${PRIV_DIR}/cakey.pem" \
            -CAcreateserial \
            -out "${PRIV_DIR}/estserver.crt" \
            -extfile "${WORK_DIR}/server_ext.cnf" \
            -extensions server_cert

        log "[NIAP] Server certificate extensions:"
        openssl x509 -in "${PRIV_DIR}/estserver.crt" -noout -text \
            | grep -A 20 "X509v3 extensions:"

    else
        # ------------------------------------------------------------------
        # General profile (basic protocol verification)
        # CA: RSA-4096, SHA-256 / Server: RSA-2048, SHA-256
        # ------------------------------------------------------------------
        log "[General] Generating Root CA (RSA-4096, SHA-256)..."

        openssl req -x509 \
            -newkey rsa:4096 \
            -sha256 \
            -days 3650 \
            -nodes \
            -keyout "${PRIV_DIR}/cakey.pem" \
            -out    "${CA_DIR}/cacert.crt" \
            -subj   "/C=JP/ST=Tokyo/O=NIAP Test Lab/CN=EST Validation Root CA" \
            -addext "basicConstraints=critical,CA:true" \
            -addext "subjectKeyIdentifier=hash" \
            -addext "keyUsage=critical,keyCertSign,cRLSign"
        chmod 600 "${PRIV_DIR}/cakey.pem"

        log "[General] Generating server certificate (RSA-2048, SHA-256)..."

        openssl req -newkey rsa:2048 \
            -nodes \
            -keyout "${PRIV_DIR}/estserver.key" \
            -out    "${CA_DIR}/estserver.csr" \
            -subj   "/C=JP/ST=Tokyo/O=NIAP Test Lab/CN=est-server"
        chmod 600 "${PRIV_DIR}/estserver.key"

        # General profile does not need NIAP validator compliance;
        # a simple cert without extensions is sufficient for EST protocol verification.
        openssl x509 -req \
            -sha256 \
            -days 365 \
            -in  "${CA_DIR}/estserver.csr" \
            -CA  "${CA_DIR}/cacert.crt" \
            -CAkey "${PRIV_DIR}/cakey.pem" \
            -CAcreateserial \
            -out "${PRIV_DIR}/estserver.crt"
    fi

    # ------------------------------------------------------------------
    # Create combined PEM files required by estserver
    # (cert+key → passed to both -c and -k arguments)
    # ------------------------------------------------------------------
    cat "${PRIV_DIR}/estserver.crt" "${PRIV_DIR}/estserver.key" \
        > "${PRIV_DIR}/estservercertandkey.pem"
    chmod 600 "${PRIV_DIR}/estservercertandkey.pem"

    # Certificate chain for NGINX (server cert + CA cert)
    cat "${PRIV_DIR}/estserver.crt" "${CA_DIR}/cacert.crt" \
        > "${PRIV_DIR}/estserver-chain.pem"

    # EST_TRUSTED_CERTS: trust store for verifying client certificates
    cp "${CA_DIR}/cacert.crt" "${WORK_DIR}/trustedcerts.crt"

    log "CA initialization complete. CA certificate info:"
    openssl x509 -in "${CA_DIR}/cacert.crt" -noout \
        -subject -issuer -dates -fingerprint -sha256
    log "libest commit: $(cat ${WORK_DIR}/LIBEST_COMMIT)"
fi

# =============================================================================
# Step 3: Expose CA cert via HTTP for Android TrustStore setup
# =============================================================================
mkdir -p /var/www/html
cp "${CA_DIR}/cacert.crt" /var/www/html/cacert.pem
log "CA cert available at: http://localhost:8080/cacert.pem"

# =============================================================================
# Step 4: Start libest EST server (port 8085, TLS)
# =============================================================================
log "=================================================="
log " Starting libest EST server (port 8085, TLS)"
log "=================================================="

# Environment variables used by libest (same naming as ESTcommon.sh)
export EST_TRUSTED_CERTS="${WORK_DIR}/trustedcerts.crt"
export EST_CACERTS_RESP="${CA_DIR}/cacert.crt"
export EST_OPENSSL_CACONFIG="${WORK_DIR}/estExampleCA.cnf"

cd "${WORK_DIR}"

# estserver arguments (per runserver.sh):
#   -c/-k: combined cert+key PEM (same file for both)
#   -r: HTTP Basic auth realm
#   -v: verbose logging
#   -p: listen port (default 8085)
estserver \
    -p 8085 \
    -c "estCA/private/estservercertandkey.pem" \
    -k "estCA/private/estservercertandkey.pem" \
    -r "estrealm" \
    -v &

EST_PID=$!
log "EST server PID: ${EST_PID}"

# Wait up to 15 seconds for startup confirmation
for i in $(seq 1 15); do
    sleep 1
    if ! kill -0 "${EST_PID}" 2>/dev/null; then
        die "EST server exited immediately. Check 'docker compose logs' for details."
    fi
done
log "EST server startup confirmed."

# =============================================================================
# Step 5: Start NGINX
#         NGINX terminates TLS/mTLS on 8443 and reverse-proxies to estserver on 8085
# =============================================================================
log "=================================================="
log " Starting NGINX [profile: ${PROFILE}]"
log "  EST  (HTTPS + mTLS optional): https://localhost:8443/.well-known/est/"
log "  mTLS test (HTTPS + mTLS req): https://localhost:8081/protected/"
log "  Admin (HTTP):                 http://localhost:8080/"
log "  CA cert:                      http://localhost:8080/cacert.pem"
log "  Health check:                 http://localhost:8080/health"
log "=================================================="

exec nginx -g 'daemon off;'
