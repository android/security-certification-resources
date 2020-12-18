#!/bin/bash
# Copyright 2020 The Android Open Source Project
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
#

# Queries for all non-platform permissions on the device and outputs a
# list of uses-permissions tags that can be added to the AndroidManifest
# to be used during the non-platform permission tests.
#
# Input:
# [$1] - the serial of the device on which to run the command; this is only
#         required if multiple devices are connected to the system

serial=""
if [[ -n $1 ]]; then
    serial="-s $1"
fi
permission=""
adb $serial shell pm list permissions -f -g | while read line
do
    if echo $line | grep -q '+ permission'; then
        permission=$(echo $line | awk -F: '{print $NF}')
    fi
    if echo $line | grep -q 'package:'; then
        package=$(echo $line | awk -F: '{print $NF}')
        if [[ "$package" != "android" ]] && [[ -n $permission ]]; then
            echo "<uses-permission android:name=\"$permission\" />"
            permission=""
        fi
    fi
done
