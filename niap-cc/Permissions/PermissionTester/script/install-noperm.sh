#!/bin/bash
# Script to build and install PermissionTester (NoPerm variant)
# Usage: ./install-noperm.sh [device_id]

DEVICE=${1:-localhost:38189}

echo "Using DEVICE: $DEVICE"

# Build
./gradlew assembleNopermDebug

# Install with -t and -g options
adb -s $DEVICE install -t -g app/build/outputs/apk/noperm/debug/Tester-noperm-debug.apk
