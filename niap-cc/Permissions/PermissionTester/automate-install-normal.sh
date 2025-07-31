#!/usr/bin/env sh

# Copyright (C) 2025 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# Unregister and Uninstall first
adb shell dpm remove-active-admin com.android.certification.niap.permission.dpctester/.receiver.DeviceAdminReceiver
adb shell cmd role remove-role-holder android.app.role.DEVICE_POLICY_MANAGEMENT com.android.certification.niap.permission.dpctester
adb uninstall com.android.certification.niap.permission.dpctester

#assemble and install
#./gradlew assembleNopermDebug
#./gradlew installNopermDebug
./gradlew assembleDpc-normalDebug
./gradlew installDpc-normalDebug

#enable priviledges
adb shell settings put global hidden_api_policy  1
adb shell dpm set-active-admin com.android.certification.niap.permission.dpctester/.receiver.DeviceAdminReceiver
#adb shell dpm set-profile-owner com.android.certification.niap.permission.dpctester/.receiver.DeviceAdminReceiver
#adb shell dpm set-device-owner com.android.certification.niap.permission.dpctester/.receiver.DeviceAdminReceiver
adb shell cmd role set-bypassing-role-qualification true
adb shell cmd role add-role-holder android.app.role.DEVICE_POLICY_MANAGEMENT com.android.certification.niap.permission.dpctester
#launch
adb shell am start -n "com.android.certification.niap.permission.dpctester/.MainActivity" -a android.intent.action.MAIN
