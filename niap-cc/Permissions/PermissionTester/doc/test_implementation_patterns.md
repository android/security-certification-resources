# Permission Test Implementation Patterns

This document summarizes the patterns for implementing permission tests in the `PermissionTester` project, based on the investigation of existing test modules and tools.

## 1. API Invocation Patterns

The primary goal is to invoke the API protected by the target permission and observe if it succeeds or throws a `SecurityException`.

### A. Simple API Call
Call a public SDK method directly.
*   **Example**: `DisplayManager.createVirtualDisplay` for `ADD_MIRROR_DISPLAY`.
*   **Characteristics**: Easiest to implement, uses standard SDK.

### B. Manager/Service Method Call
Call a method on a system manager or service.
*   **Example**: `RoleManager.getRoleHolders` for `GET_ROLE_HOLDERS`.
*   **Characteristics**: Preferred if available, often requires `@SystemApi` or reflection if not in public SDK.

### C. Hidden Method Invocation
Call a hidden method of a Manager class using reflection.
*   **Characteristics**: Used when the API is not exposed in the SDK or System API but exists in the implementation.

### D. Binder IPC via Transaction ID
Directly invoke a binder transaction bypassing the Manager class.
*   **Example**: `BinderTransaction.getInstance().invoke(...)`
*   **Characteristics**: Used when Manager APIs are not available or to make calls that are normally impossible via high-level APIs. Requires service name, descriptor, and method name/transaction ID.

---

## 2. Component Activation Patterns

### A. Specific Intent or BroadcastReceiver
Start an activity with a specific intent or send a broadcast to see if it is allowed.
*   **Example**: `ACTION_LAUNCH_CAPTURE_CONTENT_ACTIVITY_FOR_NOTE`.

### B. BIND_* Specific Patterns
Fixed patterns for binding to specific services that require `BIND_*` permissions.

---

## 3. Shell and Instrumented Test Patterns

Some tests cannot be performed by a normal application and require running as an instrumented test (`androidTest`).

### A. UIAutomation and Shell Identity
Adopt the shell UID's permission identity to call APIs that require permissions granted to shell but not to normal apps.
*   **Example**: `mUiAutomation.adoptShellPermissionIdentity()` in `BeforeClass` and `dropShellPermissionIdentity()` in `AfterClass`.
*   **File**: `InternalPermissionBaklavaTest.java`, `DangerousPermissionJUnitTest.java`.

### B. Shell Command Execution
Execute a specific shell command using `Runtime.getRuntime().exec()` and check the return code or output.
*   **Example**: `runShellCommand("cmd wifi help")`.

### C. Destructive Test Cases
Tests that severely affect the UI or reboot the device (e.g., `REBOOT` permission).
*   **Example**: `DangerousPermissionJUnitTest.java` handles these cases, often requiring explicit flags like `acceptDangerousApi = true` to run.

---

## 4. Best Practices and Guidelines

*   **Reference CTS**: CTS (Compatibility Test Suite) tests are often the best reference for how to verify a permission.
*   **Prefer Simplicity**: If multiple candidates exist, choose the one that is easiest to implement and does not affect the UI.
*   **Avoid Interference**: When multiple permissions are involved, choose an API with minimal interference or one that doesn't exist to isolate the target permission.
*   **Silent Pass Handling**: If a test passes (no exception) even when the permission is NOT granted, but logs show warnings or errors (e.g., AppOps blocking), you may manually throw an exception to fail the test if the failure is obvious.
*   **Asynchronous Handling**: Use `CountDownLatch` to wait for async results (e.g., `NsdManager` callbacks) to prevent premature test completion.
