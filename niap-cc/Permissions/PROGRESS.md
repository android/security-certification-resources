# Progress Log - Permission Test Tool Update (Android 17)

This file tracks the progress of the Android 17 (SDK 37) permission update task.

## 2026-04-07

### Achievements
- **Environment Setup**:
    - Confirmed `JAVA_HOME` pointing to Java 21 (`/usr/lib/jvm/java-21-openjdk-amd64`).
    - Resolved SDK 37 directory naming mismatch (`android-37.0` -> `android-37`) and updated `package.xml` to allow Gradle to find SDK 37.
- **Companion App**:
    - Updated `compileSdk` and `targetSdkVersion` to 37.
    - Added 15 new `BIND_*` permissions to `SignatureTestModuleBinder.java` and registered them as services in `AndroidManifest.xml`.
    - Successfully compiled `Companion` app with SDK 37.
- **Security Keys**:
    - Extracted and placed security keys for `Companion` and `PermissionTester`.
- **Checklist**:
    - Generated `permissions_checklist.csv` with 110 permissions extracted from research backup.
    - Marked 15 implemented permissions as `[3]` and others as `[0]`.

### Next Steps
- Verify compilation on `shiba` and `cuttlefish`.
- Update `TransactIds` if possible (requires real device, so might be blocked or need simulation).
- Continue implementing and testing the remaining permissions in the checklist.

