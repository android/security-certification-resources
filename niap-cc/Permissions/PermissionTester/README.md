## Permissions tester

The tool is for checking android os's permissions by vendors/labs.
Based on the permission tester tool below, we've added some improvements and modern framework to it.

https://github.com/android/security-certification-resources/tree/master/niap-cc/Permissions

This new framework was starting to develop the tester for DPC related permissions.
The tool still keep that feature, and also able to run thousands of test cases for os permissions.

## DPC Permissions tester

The tool is for checking android's dpc related permission which has 'MANAGE_DEVICE_POLICY_' prefixes.
These permissions are related to DPC app, so this test apk is working as a Device policy controller.

 - Run 'automate-install.sh to install' an apk file into the connected device.
 - And then you can execute the test cases from application.
 - If you want to disable permission remove corresponding permissions from Manifests.
 - if you want to run permissions which are disabled in active admin level, set application's privilege as the profile owner level.
 - If you'd like to uninstall app use 'remove-app.sh'

### Device Owner
```console
adb shell dpm set-device-owner com.android.certification.niap.permission.dpctester/.receiver.DeviceAdminReceiver
```
### Device Admin
```console
adb shell dpm set-active-admin com.android.certification.niap.permission.dpctester/.receiver.DeviceAdminReceiver
```
You should use 'set-device-admin' command, until android 14

### Profile Owner
```console
adb shell dpm set-profile-owner com.android.certification.niap.permission.dpctester/.receiver.DeviceAdminReceiver
```

### Apply DM Role to app

```console
adb shell cmd role set-bypassing-role-qualification true
adb shell cmd role add-role-holder android.app.role.DEVICE_POLICY_MANAGEMENT com.android.certification.niap.permission.dpctester
```

## TestDPC as DM role holder

Can be setup as Device Management Role Holder.

*   Running the following `adb` commands:

    ```console
    adb shell cmd role set-bypassing-role-qualification true
    adb shell cmd role add-role-holder android.app.role.DEVICE_POLICY_MANAGEMENT com.android.certification.niap.permission.dpctester
    ```

    Note: unlike DO/PO, this change is not persisted so TestDPC needs to be
    marked as role holder again if the device reboots.

## How to Uninstall Debug-Mode Device Owner app

If you once uninstall the app with release mode and set it to device owner there's no way.
Only if we install it as debug app

```console
adb shell dpm remove-active-admin com.android.certification.niap.permission.dpctester/.receiver.DeviceAdminReceiver
```
Go settings -> Secuirty -> More Secuirty & Privacy -> Device Admin apps -> Open app

Then uninstall it. If you can't uninstall it with some notifications. 
You should reboot the device and try unistall from shell.

```console
adb uninstall com.android.certification.niap.permission.dpctester
```
