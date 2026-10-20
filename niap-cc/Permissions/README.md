# Permission Test Tool

This project contains tools for testing Android permissions, updated for Android 17 (SDK 37).

## Project Structure

- **PermissionTester**: The main permission and DPC test application supporting Android OS and DPC permission verification.
- **Companion**: Helper application providing exported service stubs for `BIND_*` permission tests and Binder transaction configuration.
- **TransactIds**: Helper application to extract Binder Transaction IDs from target devices.
- **Tester**: Legacy permission test project kept for reference.

## Module Installation and Verification

When installing the test modules, the `-g` option is **ALWAYS required** to grant all runtime permissions automatically. Also, the `-t` option is needed as it behaves as a test app.

Example command:
```bash
adb install -t -g app-normal-debug.apk
```

### Target Variants
Verification covers the following three primary variants:
- **platform**: Signed by platform key, contains all permissions.
- **noperm**: No permissions in manifest.
- **normal**: Contains all permissions, signed by ordinary key.

Helper scripts in `PermissionTester/script/` can be used to build and install these variants:
- `install-normal.sh`
- `install-noperm.sh`
- `install-platform.sh`
- `uninstall.sh`