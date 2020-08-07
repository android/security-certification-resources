# How to use Hubble

## Prerequisites
Make sure that you have [ADB](https://developer.android.com/studio/command-line/adb)
installed on your system. It can be downloaded as a standalone tool from the
[official website](https://developer.android.com/studio/releases/platform-tools.html).

Additionally, make sure that the device that you would like to probe on has gone
through a [Factory Data Reset (FDR)](https://support.google.com/android/answer/6088915?hl=en)
process to ensure that the information you collected accurately reflects the
out-of-box experience. As an extra precaution, also ensure that the device has
not been set-up with Internet collection or have an active SIM card inserted.

## Install & Launch
Locate the Hubble APK you built. For build instructions, please refer to the
other [doc](hubble_setup.md).

Install the APK via [ADB](https://developer.android.com/studio/command-line/adb#move).

Assuming that you have not changed the package name, Hubble can be launched via
ADB with the following command:<br/>
`adb shell am start -n com.uraniborg.hubble/com.uraniborg.hubble.MainActivity`

It will take a while for all the computations to be completed, and results be
written to files on disk on device. When you see the UI on your device screen,
that means that all computations are completed.

Then, check logcat to confirm where the results are stored on disk on device.
Usually, it is stored at `/storage/emulated/0/Android/data/com.uraniborg.hubble/files/results`.

Do an adb pull to collect your results:<br/>
`adb pull /storage/emulated/0/Android/data/com.uraniborg.hubble/files/results`

You should find 7 files within the `results` folder that you've just pulled
from the target device:<br/>
1. binaries.txt<br/>
2. build.txt<br/>
3. certificates.txt<br/>
4. device_properties.txt<br/>
5. hardware.txt<br/>
6. libraries.txt<br/>
7. packages.txt<br/>

Interpretation of the results is explained in the [results doc](hubble_results.md).