# Permission Test Tool

This project contains tools for testing Android permissions, currently being updated for Android 17 (SDK 37).

## Project Structure

- **PermissionTester**: The main body of the tester. This is an older version and is no longer updated.
- **Companion**: A tool that provides service stubs to call services via Transaction ID, and handles some settings.
- **TransactIds**: A tool to retrieve actual Transaction IDs from a device.
- **Tester**: (Appears in directory listing, likely related to PermissionTester or legacy code).

## Current Status & Context

- **Target**: Android 17 (SDK 37)
- **Branch**: `niap-permission-update-sdk37`
- **Android 17 Source/Reference**: Located at `~/Android17`
- **Preliminary Investigation**: Conducted in `xpermission`.

## Module Installation and Verification

When installing the test modules, the `-g` option is **ALWAYS required** to grant all runtime permissions automatically. Also, the `-t` option is needed as it behaves as a test app.

Example command:
```bash
adb install -t -g Tester-normal-debug.apk
```

### Target Variants
Basically, we only check the following three variants:
- **platform**: Signed by platform key, contains all permissions.
- **noperm**: No permissions in manifest.
- **normal**: Contains all permissions, signed by ordinary key.

We have prepared scripts in `PermissionTester/script/` to build and install these variants:
- `install-normal.sh`
- `install-noperm.sh`
- `install-platform.sh`
- `uninstall.sh`

Use these scripts for verification.

## Permissions Checklist Legend

The `permissions_checklist.csv` file uses the following status codes:
- `-`: 実装不可 (Not implementable)
- `0`: 未実装 (Not implemented)
- `1`: プレースホルダの準備 (Placeholder ready)
- `2`: 仮次走 (Tentative run)
- `3`: 実装 (Implemented)
- `4`: 動作確認済み (Verified)
- `5`: 実装済み (Completed)


## Switch cf and out

export OUT_DIR=cfout
source build/envsetup.sh
lunch cf_x86_64_phone-trunk_staging-userdebug
# この時点で ANDROID_PRODUCT_OUT は cfout/target/product/... を指しているため、そのまま実行可能
acloud create --local-image

I shouldn't use cuttlefish, because I have to lunch it on cloudtop, and it's very slow to use it over double vpn. I should use my Pixel 8 device via ponits instead.