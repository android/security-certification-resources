# Permission Test Tool - Transact IDs
This sample app is a tool which aids OEMs in testing their devices for
evaluation of the Common Criteria certificate through
[NIAP](https://www.niap-ccevs.org/).

## Introduction
The Permission Test Tool under the `Tester/` directory performs tests to verify
the platform declared permissions properly guard their respective APIs,
resources, etc. However a number of signature permissions guard methods that are
only accessible via direct binder transacts. In order to invoke these the
transact ID must be known at compile time. Since these IDs can vary by device
this app performs the necessary queries of the transact IDs and writes a java
source file with these transact IDs that can be copied to the Permission Test
Tool to verify the permissions that guard these methods.

## Additional Details
This app uses reflection to query the service descriptor classes for the
transaction ID used to invoke the direct binder transacts. Since these fields
are protected by the reflection deny-list this app must be signed with the same
key used to sign the platform on the device under test.

This app will write a java source file to the app's data directory on the
device, typically at
/data/data/com.android.certifications.niap.permissions.transactids/files/ with a
file name of the form `<Device>ApiLevel<SDK_INT>Transacts.java`. This file can
be copied to the Permission Test Tool project under `Tester/`; for more details
see the README for that project.

## Pre-requisites
* Android SDK 28+

## Getting Started
This sample uses the Gradle build system. To build this project, use the
`gradlew build` command or use `Import Project` in Android Studio.

## Support
If you've found an error in this sample, please file an issue on the github bug
tracker for this project.

Please see `Contributing.md` in the project root for more details.
