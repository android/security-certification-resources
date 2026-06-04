# =============================================================================
# EST Validation Server — Multi-stage Docker Build
# Based on Cisco libest (https://github.com/cisco/libest)
# Purpose: NIAP / Common Criteria Android certificate enrollment testing
# =============================================================================

# -----------------------------------------------------------------------------
# Stage 1: builder
# Compile libest from source on Ubuntu 22.04
# -----------------------------------------------------------------------------
FROM ubuntu:22.04 AS builder

ENV DEBIAN_FRONTEND=noninteractive

# Install build dependencies
RUN apt-get update && apt-get install -y --no-install-recommends \
    git \
    build-essential \
    autoconf \
    automake \
    libtool \
    libssl-dev \
    pkg-config \
    ca-certificates \
    && rm -rf /var/lib/apt/lists/*

# Clone libest (pin branch/tag for reproducibility)
# To update: run `git ls-remote https://github.com/cisco/libest HEAD`
# and set LIBEST_REF to the new commit hash.
ARG LIBEST_REF=main

WORKDIR /build
RUN git clone --depth 1 --branch ${LIBEST_REF} \
        https://github.com/cisco/libest.git libest \
    && cd libest \
    && git rev-parse HEAD | tee /LIBEST_COMMIT \
    && echo "Cloned libest at commit: $(cat /LIBEST_COMMIT)"

# NIAP requirement: use SHA-384 for certificate signing (default is SHA-256)
# Compliance with FIA_X509_EXT and related PP requirements
RUN sed -i 's/^default_md[[:space:]]*=.*/default_md     = sha384/' \
        /build/libest/example/server/estExampleCA.cnf \
    && echo "=== SHA setting verification ===" \
    && grep -n "default_md" /build/libest/example/server/estExampleCA.cnf

# Build libest core library
WORKDIR /build/libest
# Two known issues fixed here:
#   1. configure.ac has duplicate AM_INIT_AUTOMAKE (fatal error on Ubuntu 22.04 autotools)
#   2. estclient.c references FIPS_mode/FIPS_mode_set removed in OpenSSL 3.x
#      → estclient is not needed; build only the core library (src/)
#
# ARM64 note: libest's estserver.c uses `char c` for the getopt return value.
# On ARM64 Linux, char is unsigned by default, so getopt's end-of-options (-1)
# becomes 255 and never terminates the option loop → show_usage_and_exit() called.
# Fix: CFLAGS="-fsigned-char" forces signed char behavior.
RUN awk 'BEGIN{n=0} /AM_INIT_AUTOMAKE/{n++; if(n==2)next} {print}' \
        configure.ac > /tmp/configure.ac.patched \
    && mv /tmp/configure.ac.patched configure.ac \
    && echo "=== AM_INIT_AUTOMAKE count (must be 1) ===" \
    && grep -c "AM_INIT_AUTOMAKE" configure.ac \
    && autoreconf --force --install --verbose 2>&1 | tail -5 \
    && ./configure --disable-safec CFLAGS="-fsigned-char" \
    # Build order:
    #   1. safe_c_stub → produces libsafe_lib.a
    #   2. Inject FIPS compat stubs into libsafe_lib.a (OpenSSL 3.x support)
    #   3. src         → produces libest.so
    && make -j"$(nproc)" -C safe_c_stub \
    # OpenSSL 3.x compatibility:
    #   FIPS_mode / FIPS_mode_set were removed in OpenSSL 3.0.
    #   libest was written for OpenSSL 1.x, so we provide stub implementations.
    #   Injecting into libsafe_lib.a resolves the symbols for both libest.so
    #   and the estserver binary at link time.
    && printf 'int FIPS_mode_set(int r){(void)r;return 0;}\nint FIPS_mode(void){return 0;}\n' \
        > /tmp/fips_compat.c \
    && gcc -c -o /tmp/fips_compat.o /tmp/fips_compat.c \
    && ar rcs /build/libest/safe_c_stub/lib/libsafe_lib.a /tmp/fips_compat.o \
    && echo "=== FIPS compat stubs injected into libsafe_lib.a ===" \
    && make -j"$(nproc)" -C src

# Build EST sample server binary only (skip estclient which has FIPS references)
WORKDIR /build/libest/example/server
RUN make -j"$(nproc)" \
    && echo "=== Build artifact ===" \
    && ls -lh estserver \
    && file estserver

# -----------------------------------------------------------------------------
# Stage 2: runtime
# Minimal runtime: NGINX + OpenSSL + compiled estserver binary
# -----------------------------------------------------------------------------
FROM ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive

# Install runtime dependencies only
RUN apt-get update && apt-get install -y --no-install-recommends \
    nginx \
    openssl \
    libssl3 \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Copy compiled estserver binary
# Note: example/server/estserver is a libtool wrapper script.
#       The actual binary is at .libs/estserver
COPY --from=builder /build/libest/example/server/.libs/estserver /usr/local/bin/estserver

# Copy libest shared library and update linker cache
COPY --from=builder /build/libest/src/est/.libs/libest-*.so /usr/local/lib/
RUN ln -sf /usr/local/lib/libest-*.so /usr/local/lib/libest.so && ldconfig

# Copy sample server working directory (contains CA config files)
COPY --from=builder /build/libest/example/server/ /opt/estserver/

# Record the libest commit used in this image for reproducibility
COPY --from=builder /LIBEST_COMMIT /opt/estserver/LIBEST_COMMIT

# Copy runtime config files
COPY nginx.conf /etc/nginx/nginx.conf
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

# Port definitions:
#   8443 = EST endpoints (HTTPS + mTLS)
#   8080 = Admin HTTP (CA cert download, health check)
EXPOSE 8080 8443

CMD ["/entrypoint.sh"]
