#!/usr/bin/env sh

adb shell dpm remove-active-admin com.android.certification.niap.permission.dpctester/.receiver.DeviceAdminReceiver
adb shell cmd role remove-role-holder android.app.role.DEVICE_POLICY_MANAGEMENT com.android.certification.niap.permission.dpctester
adb uninstall com.android.certification.niap.permission.dpctester
# Unregister and Uninstall