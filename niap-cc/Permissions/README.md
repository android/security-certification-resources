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