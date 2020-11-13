# Permission Test Tool

This sample app is a tool which aids OEMs in testing their devices for
evaluation of the Common Criteria certificate through
[NIAP](https://www.niap-ccevs.org/).

## Introduction
This sample app facilitates testing of all of the platform declared permissions
(protection levels normal, dangerous, and signature), signature protection level
permissions declared by apps preloaded on the device, and Google Play Services
(GMS) client libraries that require permissions. The extensible
`BasePermissionTester` can be extended to add additional permission tests for
OEM specific permissions, and a new `TestConfiguration` can be implemented to
run these tests.

## Implementation Details
The `BasePermissionTester` class is the base abstract class from which all other
permission testers derive. This class contains an inner `PermissionTester`
utility class that is comprised of a `Runnable` that can be used to verify the
API, resources, etc., guarded by the permission behave as expeced, along with a
minimum and maximum API level. Subclasses only need to override the
`runPermissionTests` method to iterate through all permissions to be tested,
utilizing the `BasePermissionTester#runPermissionTest(permission,
permissionTester)` to invoke individual permission tests. The `Runnable` for a
permission test is expected to throw a SecurityException when the permission is
not granted to the app and should exit without any `Throwable`s when the
permission is granted. For reference implementations see
`InstallPermissionTester`, `RuntimePermissionTester`, and
`GmsPermissionTester`.

Once a subclass of `BasePermissionTester` has been defined a new implementation
of `TestConfiguration` can be created for the tester. In most cases this
configuration just needs to return the new tester in `#getPermissionTesters` and
an appropriate button text ID from `getButtonTextId`. With the configuration
defined a new instance can be instantiated and returned in the `List` in
`ConfigurationFactory#getAdditionalConfigurations`. For reference see
`GmsPermissionConfiguration` and `DebugConfiguration`. During development
the `DebugConfiguration` can be used by returning the new tester / permission
under test and modifying `Constants.USE_DEBUG_CONFIG` to `true`.

## Companion Package
Under the `Companion/` directory exists the project for the tester companion
app. This app is a pre-requisite for the permission tester app as a number of
the platform permission tests require additional setup on the device before
being run; the companion app performs this necessary setup. The permission test
app and the companion app should be signed with different signing keys as some
permission tests may fail if the two apps have the same signing identity.

## Transact IDs
Some signature platform permissions guard methods that are only accessible via
direct binder transacts; these require the ID of the transaction to be known at
compile time. Since these IDs can vary by device the app under `TransactIds/`
can be used to query the device under test for these IDs. Once the java source
file has been obtained from this app it can be copied to the
`app/src/main/java/com/android/certifications/niap/permissions/utils/`
directory. The `createTransactsForApiLevel` method in `Transacts.java` in this
same directory can then be updated to return an instance of this class for the
appropriate API level.

## Pre-requisites
* Android SDK 28+
* The companion package must complete setup successfully on the device under
test

## Getting Started
This sample uses the Gradle build system. To build this project, use the
`gradlew build` command or use `Import Project` in Android Studio.

## Support
If you've found an error in this sample, please file an issue on the github bug
tracker for this project.

Please see `Contributing.md` in the project root for more details.
