#!/usr/bin/env sh

# Unregister and Uninstall first
adb shell dpm remove-active-admin com.android.certification.niap.permission.dpctester/.receiver.DeviceAdminReceiver
adb shell cmd role remove-role-holder android.app.role.DEVICE_POLICY_MANAGEMENT com.android.certification.niap.permission.dpctester
adb uninstall com.android.certification.niap.permission.dpctester

#assemble and install
./gradlew assembleDpc-normalDebug
./gradlew installDpc-normalDebug

#enable priviledges
#adb shell dpm set-active-admin com.android.certification.niap.permission.dpctester/.receiver.DeviceAdminReceiver
#adb shell dpm set-profile-owner com.android.certification.niap.permission.dpctester/.receiver.DeviceAdminReceiver
adb shell settings put global hidden_api_policy  1
adb shell dpm set-device-owner com.android.certification.niap.permission.dpctester/.receiver.DeviceAdminReceiver
adb shell cmd role set-bypassing-role-qualification true
adb shell cmd role add-role-holder android.app.role.DEVICE_POLICY_MANAGEMENT com.android.certification.niap.permission.dpctester
#launch
adb shell am start -n "com.android.certification.niap.permission.dpctester/.MainActivity" -a android.intent.action.MAIN
# MANAGE_DEVICE_POLICY_RESET_PASSWORD
# Need a QR code that is described below
#https://developers.google.com/android/work/play/emm-api/prov-devices#create_a_qr_code