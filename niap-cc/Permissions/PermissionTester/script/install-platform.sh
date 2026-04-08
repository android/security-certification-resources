#!/bin/bash
# Script to build and install PermissionTester (Platform variant)
# Usage: ./install-platform.sh [device_id]

DEVICE=${1:-localhost:38189}

echo "Using DEVICE: $DEVICE"

# Build
./gradlew assemblePlatformDebug

# Install with -t and -g options
adb -s $DEVICE install -t -g app/build/outputs/apk/platform/debug/Tester-platform-debug.apk
