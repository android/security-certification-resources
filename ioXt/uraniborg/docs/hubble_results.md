# Hubble Results
After successful invocation of Hubble app, 7 text files should be produced. For
convenience, the content of the text file are essetentially JSON. Therefore, you
can also use your favorite JSON parsing or viewing tools to navigate the results.

All result files have the following format. It has a version that allows the
reader to identify which version of Hubble that produced the result (in case of
changes in the future). It then has a total field describing the number of
elements of the type of result. This should help detect very basic transmission
errors that leads to truncation of file, for example.

## Components
### Binaries (binaries.txt)
This file enumerates the executable binaries accessible to an untrusted app on
the system. The information included for each binary is:

- hash: This is the SHA256 digest of the binary.
- installPath: The directory the binary is located at.
- name: The filename of the binary.

### Build Information (build.txt)
This file provides a basic enumeration of the build information of the target
device. The information included is:

- apiLevel: The API level of this build (This usually also corresponds to the
OS level).
- bootloaderVersion: The bootloader version of the device at the time the
observation is made. Note that sometimes this may not be available on certain
devices.
- fingerprint: The build fingerprint string that the device identifies as.
- kernelVersion: The kernel version that this device currently runs. Note that
this sometimes include richer informations like the architecture and the build
timestamp of the kernel.
- locale: The locale that this device is set up as. This field is collected to
try to differentiate the case that with everything else constant, there may be
other changes to the device due to the locale variation alone.
- radioVersion: The baseband version. Note that sometimes this may not be
available on certain devices.
- securityPatchLevel: The security patch level as claimed by the OEM.

### Signing Certificates (certificates.txt)
This file enumerates all the certificates that are used to sign the packages
that were found to be installed at the time of observation. The listed fields
are:

- hash: The SHA256 digest of the certificate file.
- encodedCert: The base64 encoded string representation of the individual X509
certificate in DER format. You can decode this string and use your favorite tool
to parse and further analyze the certificates. For instance, after decoding back
to bytes and writing it to a file:<br/>
`openssl x509 -inform der -in <path_to_cert>`

### Device Properties
These are some of the properties that are specific to the device/SKU. This is
essentially information that is obtainable via `adb shell getprop`

- encodedDevProps: The base64 encoded string of the result from executing `adb
shell getprop`.

### Hardware Information
This file includes information about the hardware properties itself. Fields
include:

- boardName: The board name of the device
- brand: The brand of the device
- deviceName: The OEM-given name of the device
- hardwareName: The OEM-given hardware name of the device
- hash: The SHA256 digest of the other properties sorted by alphabetical order
- modelName: The OEM-given model name of the device
- oem: The manufacturer of the device
- productName: The OEM-given product name of the device

### System Libraries (libraries.txt)
This file enumerates all the libraries that are visible to Hubble. The properties
we capture include:

- bits: Whether the library is a 32 or 64 bit library.
- hash: The SHA256 digest of the library.
- installPath: The location where the library is installed at.
- name: The filename of the library.

### Installed Packages (packages.txt)
This file enumerates all installed packages on the system at the time of
observation.

- activities: A list of `activity`s that the package contains (can be empty).
- certIds: SHA256 digest of certificate(s) used to sign this package.
- description: The description of the application (if available).
- firstInstallTime: The recorded time (in ms) of the first install time of this
package.
- hasCode: A boolean [flag](https://developer.android.com/reference/android/content/pm/ApplicationInfo.html#FLAG_HAS_CODE)
indicating app developer's declaration of whether this package contains code or
is purely data/resource APK.
- hash: The SHA256 digest of the package/APK.
- installLocation: The location where the APK is installed on the system.
- isApex: A boolean flag indicating if this package is an [APEX](https://source.android.com/devices/tech/ota/apex) or not.
- isEnabled: A boolean [flag](https://developer.android.com/reference/android/content/pm/ApplicationInfo.html#enabled)
telling whether at the time of observation, this package is "active" or in the
"disabled" or not running state.
- isFactoryTest: A boolean flag indicating whether the APK is one used for
factory test purposes or not.
- isHidden: A boolean flag indicating whether the APK is visible to the user
from the UI or not at the time the observation is done.
- isSuspended: A boolean [flag](https://developer.android.com/reference/android/content/pm/ApplicationInfo.html#FLAG_SUSPENDED) indicating whether or not the APK is suspended or not at the time of observation.
- isTestOnly: A boolean [flag](https://developer.android.com/reference/android/content/pm/ApplicationInfo.html#FLAG_TEST_ONLY) indicating whether or not the APK is intended for
test only.
- kernelGids: the [kernel group IDs](https://developer.android.com/reference/android/content/pm/PackageInfo.html#gids)
of this package.
- label: the [application label](https://developer.android.com/reference/android/content/pm/PackageManager#getApplicationLabel(android.content.pm.ApplicationInfo)) of this package. This is the field known to
the user as the APK's name.
- name: the package name of this APK (usually in reverse domain format).
- permissionsDeclared: a list of **custom** permissions declared by the app.
- permissionsGranted: a list of permissions that is currently granted to the
package. If observation is done after a factory reset, this represents the state
of pre-granted permissions to this package by the OEM.
- permissionsNotGranted: a list of permissions that is **not** granted to the
package at the time of observation.
- providers: a list of `provider`s in the package.
- receivers: a list of `receiver`s in the package.
- services: a list of `service`s in the package.
- sharedUserId: a string representing the [shared user ID](https://developer.android.com/reference/android/content/pm/PackageInfo.html#sharedUserId) of this package.
- sharedUserLabel: an integer representing the [shared user ID label](https://developer.android.com/reference/android/content/pm/PackageInfo.html#sharedUserLabel) of this
package.
- splitNames: any names of installed [split APKs](https://developer.android.com/reference/android/content/pm/PackageInfo#splitNames)
of this package.
- usesCleartextTraffic: a boolean [flag](https://developer.android.com/reference/android/content/pm/ApplicationInfo.html#FLAG_USES_CLEARTEXT_TRAFFIC)indicating whether or not this
package would use cleartext network traffic.
- versionCode: An integer indicating this package's version code.
- versionName: A string representing this package's version.

For each of the components, there are further details that are collected:
#### Activity
- name: the name of the activity.
- isEnabled: whether this activity is turned on or not.
- isExported: whether this activity is exported (able to be called externally)
or not.
- labels: the labels (user readable names) of the activity.
- desc: the descriptions of the activity.
- permission: the permission that protects this activity.

#### Provider
- name: the name of the provider.
- isEnabled: whether this provider can be instantiated by the system.
- isExported: whether this provider is available for other apps to use.
- labels: the labels (user readable names) of this provider.
- desc: the description of the provider.
- authority: an URI authority that identifies data offered by the content
provider.
- grantUriPermissions: whether or not those who ordinarily would not have
permission to access the content provider's data can be granted permission to do
so.
- permissionRead: The permission that clients must have to query the content
provider.
- permissionWrite: The permission that clients must have to modify the data
controlled by the content provider.

#### Receiver
- name: the name of the receiver.
- isEnabled: whether this receiver is turned on or not.
- isExported: whether this receiver is exported (can be called externally) or
not.
- labels: the labels (user readable names) of this receiver.
- desc: the descriptions of this receiver.
- permission: the permission that protects this receiver.

#### Service
- name: the name of the service.
- isEnabled: whether this service is turned on or not.
- isExported: whether this service is exported (can be called externally) or
not.
- labels: the labels (user readable names) of this service.
- desc: the descriptions of this service.
- permission: the permission that protects this service.

## Interpretation
If you launched Hubble on a brand new device (out of the box) or one that has
just gone through factory data reset (FDR), you will be able to observe the
state of the device that is configured by the OEM.

If you launched Hubble on your current device without going through FDR, you
are essentially creating a snapshot of your device based on the installed
packages (including those that are installed post device setup).