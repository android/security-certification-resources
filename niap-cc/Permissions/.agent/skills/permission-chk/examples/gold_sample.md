# 🔍 Investigated Permission: android.permission.WRITE_CONTACTS
Risk Score: 7.5

## 📖 Overview
This permission allows an application to modify, add, or delete the user's contact data (RawContacts, Data, etc.). It is a critical permission concerning user privacy.

## ⚖️ Investigation Items (9 Categories)

1. **Exclusion of Work-in-Progress (WIP)**: N/A
   - Implementation exists and is actively used.

2. **Isolation of Non-Phone Limits (Wear, Auto, TV, etc.)**: N/A
   - This is a standard contact feature and serves as a fundamental capability across all form factors.

3. **Identification of Feature Flag Control (Default Disabled)**: N/A
   - Basic feature; no settings to disable via flags were found.

4. **Identification of `BIND_*` Permissions**: N/A

5. **Identification of DPC (Device Policy Controller) Permissions**: N/A

6. **Whether It Applies Outside the Framework (e.g., Google Play)**: No

7. **Corresponding CTS Tests**:
   - **Test Name**: `CtsContactsProviderTestCases` (or `CtsProviderTestCases`)
   - **Test Summary**: Verifies that a `SecurityException` is thrown when an application without the permission attempts to write to the contacts provider.
   - **User-Level Reproducibility**: Yes
   - **Reproduction Path**: Execute `ContentResolver.insert()` from an app that does not hold the specific permission and confirm that it fails.

8. **Reproduction Path Verification**:
   - **Usage Frequency and Location**: Extremely high. Used in telephony, dialers, contacts apps, account managers, etc.
   - **Source Code Locations and Evidence Snippets (Top 5 Representatives)**:

      1. **[IccPhoneBookInterfaceManager.java](file:///~/android17/frameworks/opt/telephony/src/java/com/android/internal/telephony/IccPhoneBookInterfaceManager.java#L203)**
         - **Summary**: Permission check when updating the SIM phonebook.
         - **Snippet**:
           ```java
           if (mContext.checkCallingOrSelfPermission(android.Manifest.permission.WRITE_CONTACTS)
                   != PackageManager.PERMISSION_GRANTED) {
               throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
           }
           ```

      2. **[PreferredSimFallbackProvider.java](file:///~/android17/packages/apps/Dialer/java/com/android/dialer/preferredsim/impl/PreferredSimFallbackProvider.java#L169)**
         - **Summary**: Permission check in the dialer's preferred SIM fallback provider.
         - **Snippet**:
           ```java
           if (getContext().checkCallingOrSelfPermission(permission.WRITE_CONTACTS)
                   != PackageManager.PERMISSION_GRANTED) {
               throw new SecurityException("WRITE_CONTACTS required");
           }
           ```

      3. **[ContactSaveService.java](file:///~/android17/packages/apps/Contacts/src/com/android/contacts/ContactSaveService.java#L316)**
         - **Summary**: Permission check before save operations in the contacts app.
         - **Snippet**:
           ```java
           if (!PermissionsUtil.hasPermission(this, WRITE_CONTACTS)) {
               Log.w(TAG, "No WRITE_CONTACTS permission, unable to write to CP2");
               return;
           }
           ```

      4. **[UndemoteOutgoingCallReceiver.java](file:///~/android17/packages/apps/Dialer/java/com/android/dialer/interactions/UndemoteOutgoingCallReceiver.java#L45)**
         - **Summary**: Permission check when updating contacts on outgoing calls.
         - **Snippet**:
           ```java
           if (!PermissionsUtil.hasPermission(context, WRITE_CONTACTS)) {
               return;
           }
           ```

      5. **[AccountManagerService.java](file:///~/android17/frameworks/base/services/core/java/com/android/server/accounts/AccountManagerService.java#L6004)**
         - **Summary**: Permission check (via helper) in the account authentication manager.
         - **Snippet**:
           ```java
           if (accountType.equals(serviceInfo.type.type)) {
               return isPermittedForPackage(serviceInfo.type.packageName, userId,
                   Manifest.permission.WRITE_CONTACTS);
           }
           ```

    - **Reproduction Method (Verification Techniques)**:
      *Prioritize validation using public APIs or shell commands. For details, see [advanced_verification_techniques.md](file:///~/AndroidStudioProjects/security-certification-resources/niap-cc/Permissions/.agent/skills/permission-chk/resources/advanced_verification_techniques.md).*
      - **Public API**: Execute `ContentResolver.insert(ContactsContract.RawContacts.CONTENT_URI, values)` (this is the most standard method).
      - **Non-Public/System API**: Call the AIDL interface of `IccPhoneBookInterfaceManager` (may require copying AIDL files).
      - **ServiceManager / Binder IPC**: Call Binder transactions like `service call telephony.phonebook ...` (requires identifying the transaction ID).
      - **Shell Command**: `adb shell content insert --uri content://com.android.contacts/raw_contacts ...`

9. **Risk Level Assessment on Permission Granted**:
   - **Score**: 7.5
   - **Remarks and Attack Scenarios**: Allows tampering and unintended deletion of contact information. Since this can lead to secondary damage such as phishing, it is classified as a Privacy Risk (Level 2).
