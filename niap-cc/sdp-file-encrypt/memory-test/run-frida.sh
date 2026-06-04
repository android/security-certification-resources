#!/bin/bash

# --- Configuration ---
# The package name of the app to test.
PACKAGE_NAME="com.android.niapsec"

# The name of the Frida hook script.
HOOK_SCRIPT="security-hook.js"

# The path to the frida-server on the device.
FRIDA_SERVER_PATH="/data/local/tmp/frida-server"

# --- Script Body ---

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}Starting Frida test script for package: ${YELLOW}${PACKAGE_NAME}${NC}"

# 1. Check if adb command exists
if ! command -v adb &> /dev/null; then
    echo -e "${RED}Error: 'adb' command not found. Please ensure Android SDK Platform-Tools are in your PATH.${NC}"
    exit 1
fi

# 2. Check if the hook script exists
if [ ! -f "$HOOK_SCRIPT" ]; then
    echo -e "${RED}Error: Hook script '$HOOK_SCRIPT' not found in the current directory.${NC}"
    exit 1
fi

source ~/frenv/bin/activate

# 3. Check if frida-server is running
echo -e "${YELLOW}Checking for running frida-server...${NC}"
# Use pgrep to find the process ID
FRIDA_PID=$(adb shell "pgrep -f $FRIDA_SERVER_PATH")

if [ -z "$FRIDA_PID" ]; then
    echo -e "${YELLOW}Frida server is not running. Attempting to start it...${NC}"
    # Start frida-server in the background as root
    adb shell "su -c '$FRIDA_SERVER_PATH &'"
    # Wait a moment for the server to initialize
    sleep 2

    # Check again
    FRIDA_PID=$(adb shell "pgrep -f $FRIDA_SERVER_PATH")
    if [ -z "$FRIDA_PID" ]; then
        echo -e "${RED}Error: Failed to start Frida server on the device. Please check if the device is rooted and if the server is at '$FRIDA_SERVER_PATH'.${NC}"
        exit 1
    else
        echo -e "${GREEN}Frida server started successfully (PID: $FRIDA_PID).${NC}"
    fi
else
    echo -e "${GREEN}Frida server is already running (PID: ${FRIDA_PID}).${NC}"
fi

# 4. Attach to the application using Frida
echo -e "\n${GREEN}Attaching to application... (Press Ctrl+C to exit)${NC}"
echo "--------------------------------------------------"

# -U: Use a USB-connected device
# -f: Spawn the specified package name
# -l: Load the specified script
# --no-pause: Resume the application immediately after attaching
frida -U -f $PACKAGE_NAME -l $HOOK_SCRIPT

echo "--------------------------------------------------"
echo -e "${GREEN}Frida session finished.${NC}"

