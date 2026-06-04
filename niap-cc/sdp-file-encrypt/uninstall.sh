#!/bin/sh
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
SDK_DIR=$(grep '^sdk.dir=' "$SCRIPT_DIR/local.properties" | cut -d'=' -f2)
ADB="$SDK_DIR/platform-tools/adb"

"$ADB" shell am broadcast -a com.android.niapsec.demo.ACTION_REMOVE_ADMIN -n com.android.niapsec/.demo.RemoveAdminReceiver
sleep 1
"$ADB" uninstall com.android.niapsec
