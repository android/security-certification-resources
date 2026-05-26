# 🔍 Investigated Permission: [Permission Name]
Risk Score: [100, 10, 7.5, 5, 2.5, 0]

## 📖 Overview
Briefly describe the role and purpose of this permission.

## ⚖️ Investigation Items (9 Categories)

1. **Exclusion of Work-in-Progress (WIP)**: [Applicable/Not Applicable]
   - Reason and evidence (e.g., @RequiresPermission checks are not found).

2. **Isolation of Non-Phone Limits (Wear, Auto, TV, etc.)**: [Applicable/Not Applicable]
   - Reason (e.g., whether it is confined to specific modules).

3. **Identification of Feature Flag Control (Default Disabled)**: [Applicable/Not Applicable]
   - Reason (e.g., presence/absence of flag checks).

4. **Identification of `BIND_*` Permissions**: [Applicable/Not Applicable]

5. **Identification of DPC (Device Policy Controller) Permissions**: [Applicable/Not Applicable]

6. **Whether It Applies Outside the Framework (e.g., Google Play)**: [Yes/No]

7. **Corresponding CTS Tests**:
   - **Test Name**: [Test class name or module name]
   - **Test Summary**: [What the test verifies]
   - **User-Level Reproducibility**: [Yes/No (with reasons)]
   - **Reproduction Path**: [Execution command (e.g., adb shell am instrument ...) or steps to reproduce]

8. **Reproduction Path Verification**:
   - **Usage Frequency and Location**: [High/Low. System services, providers, etc.]
   - **Source Code Locations**: [Filename](file:///absolute/path#Lline_number)
   - **Evidence Snippets**:
     ```java
     // Actual permission check or invocation code
     ```
   - **Reproduction Method (Verification Techniques)**:
     - **Public API**: [Classname.methodname, etc.]
     - **Non-Public/Hidden API (System API)**: [System services, non-public classes, etc.]
     - **ServiceManager / Binder IPC**: [AIDL interface calls, etc.]
     - **Shell Command**: [adb shell content / am / service, etc.]

9. **Risk Level Assessment on Permission Granted**:
   - **Score**: [100, 10, 7.5, 5, 2.5, 0]
   - **Remarks and Attack Scenarios**: 
