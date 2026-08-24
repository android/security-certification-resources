# 🛠️ Advanced Permission Verification Techniques

When investigating Android permissions, if verification via standard public APIs or shell commands is not possible, you can attempt reproduction and verification using the following advanced techniques.

These techniques are referenced to specify which method is applicable in the "Reproduction Path Verification" section of the report.

---

## 🔝 0. Priority of Verification Paths (Principles)

When identifying verification paths, prioritize simple and standard methods in the following order:

1.  **Public API (SDK)**: APIs usable in standard application development.
2.  **Shell Commands**: Using commands like `am`, `pm`, or `content` via `adb shell`.
3.  **Test API**: APIs used in CTS, etc., which can only be called via instrumentation.
4.  **Non-Public/System API**: Hidden features that require reflection or AIDL duplication (detailed below).

---

## 🛠️ Advanced Techniques

### 1. Direct Binder Transactions (`IBinder.transact`)

If system services are not public in the SDK or Java-layer helpers do not exist, it may be possible to invoke them by issuing a Binder transaction directly.

*   **Overview**: Retrieve the `IBinder` of the service using `ServiceManager.getService("service_name")` and directly invoke `transact(code, data, reply, flags)`.
*   **Prerequisites**:
    *   Requires identifying the **Transaction ID (code)**. This must be read from the definition of the corresponding `BnInterface` or `Stub` class (`Stub.TRANSACTION_xxxx`) in AOSP, or retrieved via reflection.
    *   The structure of the arguments (`Parcel`) must be accurately recreated.
*   **Report Description Example**:
    *   "To call the non-public service `iphonesubinfo`'s `getDeviceId`, a direct Binder Call using transaction ID `1` is required."

### 2. AIDL / System Interface Duplication

If a non-public system service is defined via AIDL, replicating that interface on the test app side may allow writing it like a normal RPC call.

*   **Overview**: Copy the corresponding `.aidl` file (and its dependent AIDL files) from the AOSP source code into the test project under `src/main/aidl/` with the **exact same package structure**. The build system will generate the Stub class, which you can then use for invocation.
*   **Prerequisites**:
    *   If the copied AIDL file references hidden classes in the SDK, stubs for those may also be required.
*   **Report Description Example**:
    *   "To utilize the `IBluetooth` interface, `IBluetooth.aidl` must be copied to the app project and built."

### 3. Shadowing / Stubbing (Creating Stub Classes)

When compiling test code, referencing hidden classes (`@hide`) or constants not included in the SDK results in compilation errors. This technique avoids those errors.

*   **Overview**: Create an "empty class (stub)" with the exact same package name, class name, and signatures of required methods or constants within the test project.
*   **Prerequisites**:
    *   This is purely to pass compilation; at runtime on an actual device, the real class on the framework side will be loaded.
*   **Report Description Example**:
    *   "To utilize hidden classes such as `android.os.ServiceManager`, a stub class of the same name and package must be created on the app side to pass compilation."

---

## 📝 Important Notes for Report Creation
The agent must read from the code whether these methods are **physically possible** (e.g., interface is too large to copy easily, transaction ID changes dynamically so it cannot be hardcoded, etc.) and document this in the "Reproduction Path Verification" section of the report.
