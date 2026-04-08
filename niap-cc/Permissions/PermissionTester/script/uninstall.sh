#!/bin/bash
# Script to uninstall PermissionTester and clean up roles
# Usage: ./uninstall.sh [device_id]

DEVICE=${1:-localhost:38189}

echo "Using DEVICE: $DEVICE"

# Remove active admin and role holder (ignore errors if not set)
adb -s $DEVICE shell dpm remove-active-admin com.android.certification.niap.permission.dpctester/.receiver.DeviceAdminReceiver || true
adb -s $DEVICE shell cmd role remove-role-holder android.app.role.DEVICE_POLICY_MANAGEMENT com.android.certification.niap.permission.dpctester || true

# Uninstall
adb -s $DEVICE uninstall com.android.certification.niap.permission.dpctester
