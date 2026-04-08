#!/bin/bash
# Script to build and install PermissionTester (Normal variant)
# Usage: ./install-normal.sh [device_id]

DEVICE=${1:-localhost:38189}

echo "Using DEVICE: $DEVICE"

# Build
./gradlew assembleNormalDebug

# Install with -t and -g options
adb -s $DEVICE install -t -g app/build/outputs/apk/normal/debug/Tester-normal-debug.apk
