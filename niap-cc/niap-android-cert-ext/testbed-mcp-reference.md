# Testbed Automation MCP Reference for LLMs

This document is a comprehensive Model Context Protocol (MCP) tool reference for LLM agents to control and observe physical or emulated devices within the Testbed Automation environment. 

It is updated from the baseline specifications and incorporates recent additions (such as streaming and ping utilities), precise parameter definitions, and concrete **execution examples**.

---

## 1. Sensing (Device State and Screen Analysis)

Tools in this category allow agents to inspect screen layouts, query power configurations, and check hardware parameters.

### `get_ui_dump`
* **Description**: Captures the current screen's UI layout hierarchy. By default, it yields a token-efficient flat list format (~1KB) optimized for LLMs.
* **Parameters**:
  * `format` (String / Optional / Default: `"summary"`): Output style. `"summary"` = compact flat list, `"json"` = traditional full-tree JSON.
  * `include_image` (Boolean / Optional / Default: `false`): Whether to include base64-encoded screenshot data (valid only when `format="json"`).
  * `image_quality` (Int / Optional / Default: `2`): Screen compression level (1 = 100%, 2 = 50%, 3 = 33%, 4 = 25%).
* **Returns**:
  * If `format="summary"`: A plain-text flat list where each line represents a node containing its index, class identifier, human-readable label, action coordinates, and state indicators.
  * If `format="json"`: A structured JSON payload containing the hierarchical `json_dump` string and optional base64 `screenshot` data.
* **Examples**:
  * **Request (Default: summary format)**:
    ```json
    {}
    ```
  * **Response (summary format)**:
    ```text
    Screen: 1080x2400 | App: com.example.app
    ───────────────────────────────────────────────
    [0] ImageButton "Back" tap(73,205) clickable
    [1] TextView "Settings" at(540,205)
    [2] CheckBox "Option A" tap(540,484) clickable checked=true ★
    [3] CheckBox "Option B" tap(540,625) clickable checked=false
    ──────────────────── scrollable: RecyclerView ─
    ```
  * **Request (JSON format)**:
    ```json
    {
      "format": "json",
      "include_image": true
    }
    ```
  * **Response (JSON format)**:
    ```json
    {
      "json_dump": "{\"className\":\"android.widget.FrameLayout\",\"bounds\":\"[0,0][1080,2400]\",\"children\":[...]}"
    }
    ```

### `get_device_state`
* **Description**: Checks screen power state (ON/OFF), screen lock status, and current active foreground package details.
* **Parameters**: None
* **Returns**: A JSON state map.
* **Examples**:
  * **Request**: `{}`
  * **Response**:
    ```json
    {
      "is_screen_on": true,
      "is_locked": false,
      "foreground_package": "com.google.android.apps.nexuslauncher"
    }
    ```

### `get_device_info`
* **Description**: Queries hardware and operating system parameters from the system (wrapper around `getprop` entries).
* **Parameters**: None
* **Returns**: A JSON dictionary containing hardware specifications.
* **Examples**:
  * **Request**: `{}`
  * **Response**:
    ```json
    {
      "model": "Pixel 8",
      "os_version": "37",
      "abi": "arm64-v8a",
      "screen_size": "1080x2400"
    }
    ```

### `start_stream` (New)
* **Description**: Initiates automatic streaming of base64-encoded device screenshots at standard regular intervals.
* **Parameters**:
  * `fps` (Float / Optional / Default: `1.0`): Target frames per second.
  * `image_quality` (Int / Optional / Default: `2`): Screen compression level (1 to 4).
* **Returns**: Operation status success message.
* **Examples**:
  * **Request**:
    ```json
    {
      "fps": 2.0,
      "image_quality": 3
    }
    ```
  * **Response**: `"Stream started"`

### `stop_stream` (New)
* **Description**: Terminates active screenshot streaming operations.
* **Parameters**: None
* **Returns**: Operation status success message.
* **Examples**:
  * **Request**: `{}`
  * **Response**: `"Stream stopped"`

---

## 2. Action (UI Interactions and Gestures)

These tools execute physical interactions on the device. Following command invocation, they automatically wait for the screen to settle into an idle state and **return a summary of interactable elements only** (a layout list containing only clickable, checkable, or scrollable nodes).

### `tap`
* **Description**: Simulates a tap action at the specified (x, y) screen coordinates.
* **Parameters**:
  * `x` (Int / Required): Target X coordinate.
  * `y` (Int / Required): Target Y coordinate.
* **Returns**: An interactable UI elements summary layout list.
* **Examples**:
  * **Request**:
    ```json
    {
      "x": 500,
      "y": 1000
    }
    ```
  * **Response**: (Latest interactable UI layout summary)

### `input_text`
* **Description**: Sends a raw text string to the currently focused text input control.
* **Parameters**:
  * `text` (String / Required): The text string to send. Spaces and special characters are managed and escaped automatically.
* **Returns**: An interactable UI elements summary layout list.
* **Examples**:
  * **Request**:
    ```json
    {
      "text": "Hello World"
    }
    ```
  * **Response**: (Latest interactable UI layout summary)

### `swipe`
* **Description**: Initiates a linear drag gesture between start and end screen coordinates.
* **Parameters**:
  * `start_x` (Int / Required)
  * `start_y` (Int / Required)
  * `end_x` (Int / Required)
  * `end_y` (Int / Required)
* **Returns**: An interactable UI elements summary layout list.
* **Examples**:
  * **Request**:
    ```json
    {
      "start_x": 500,
      "start_y": 1500,
      "end_x": 500,
      "end_y": 500
    }
    ```
  * **Response**: (Latest interactable UI layout summary)

### `press_key`
* **Description**: Dispatches a standard hardware or system physical key button press.
* **Parameters**:
  * `keycode` (String / Required): Key event identifier (e.g., `"BACK"`, `"HOME"`, `"ENTER"`).
* **Returns**: An interactable UI elements summary layout list.
* **Examples**:
  * **Request**:
    ```json
    {
      "keycode": "BACK"
    }
    ```
  * **Response**: (Latest interactable UI layout summary)

---

## 3. System (ADB Commands and Package Administration)

### `execute_adb_shell`
* **Description**: Executes an arbitrary ADB shell command on the target system.
* **Parameters**:
  * `command` (String / Required): Terminal command to run.
* **Returns**: Combined plain text streams of `stdout` and `stderr`.
* **Examples**:
  * **Request**:
    ```json
    {
      "command": "ls -l /sdcard"
    }
    ```
  * **Response**:
    ```text
    total 0
    drwxrwxr-x 2 root sdcard_rw 4096 ...
    ```
    *(Note: Depending on individual tool wrapper specifications, stdout/stderr streams might sometimes be delivered inside a structured JSON response object, but they are typically returned as simple text values.)*

### `open_settings`
* **Description**: Direct-launches specific system settings pages using target system Intents.
* **Parameters**:
  * `panel` (String / Required): Settings partition code. Valid keys: `ROOT`, `SECURITY`, `WIFI`, `APP_DETAILS`, `DEVELOPER`.
* **Returns**: A layout representation string of the opened settings layout.
* **Examples**:
  * **Request**:
    ```json
    {
      "panel": "SECURITY"
    }
    ```
  * **Response**: (Settings page UI layout representation)

### `push_file`
* **Description**: Transports local host files onto target paths of the connected device.
* **Parameters**:
  * `host_path` (String / Required): Source file path on the host computer.
  * `device_path` (String / Required): Destination file path on the target device.
* **Returns**: Operation status success or error message.
* **Examples**:
  * **Request**:
    ```json
    {
      "host_path": "/tmp/test.apk",
      "device_path": "/data/local/tmp/test.apk"
    }
    ```
  * **Response**: `"File pushed successfully"`

### `pull_file`
* **Description**: Downloads files from target locations on the device to the host system.
* **Parameters**:
  * `device_path` (String / Required): Target file path on the device.
  * `host_path` (String / Required): Destination path on the host computer.
* **Returns**: Operation status success or error message.
* **Examples**:
  * **Request**:
    ```json
    {
      "device_path": "/sdcard/screenshot.png",
      "host_path": "/tmp/screenshot.png"
    }
    ```
  * **Response**: `"File pulled successfully"`

### `install_app`
* **Description**: Installs a target application APK package from the host system.
* **Parameters**:
  * `apk_path` (String / Required): Absolute file path to the source APK on the host.
* **Returns**: Installation terminal output.
* **Examples**:
  * **Request**:
    ```json
    {
      "apk_path": "/tmp/app.apk"
    }
    ```
  * **Response**: `"Success"`

### `uninstall_app`
* **Description**: Removes an application package from the target device system.
* **Parameters**:
  * `package_name` (String / Required): Target package unique identifier (e.g., `com.example.app`).
* **Returns**: Operation status confirmation.
* **Examples**:
  * **Request**:
    ```json
    {
      "package_name": "com.example.app"
    }
    ```
  * **Response**: `"Success"`

---

## 4. Observe (Logging and Connection Diagnostics)

### `get_logcat`
* **Description**: Captures memory-buffered system and application logs via Logcat. Applying specific query filters is highly recommended to protect token budgets.
* **Parameters**:
  * `tags` (String[] / Optional / Default: `[]`): Filter logs matching target tag names.
  * `level` (String / Optional / Default: `"V"`): Filter logs exceeding minimum priority levels (`V`, `D`, `I`, `W`, `E`, `F`).
  * `grep_pattern` (String / Optional / Default: `""`): Filter entries matching regular expressions.
  * `max_lines` (Int / Optional / Default: `100`): Maximum lines output boundary.
  * `process` (String / Optional / Default: `""`): Filter logs matching specific processes. Supports package names (e.g., `"com.android.settings"`) or active PIDs (e.g., `"1234"`). Package strings are resolved to PIDs dynamically (first checking cached structures from ProcessNameResolver, then using `pidof` commands as fallback).
* **Returns**: A plain-text block of raw logs.
* **Examples**:
  * **Targeting Tag Names**:
    ```json
    {
      "tags": ["ActivityManager"],
      "level": "I",
      "max_lines": 5
    }
    ```
  * **Targeting Package Processes**:
    ```json
    {
      "process": "com.android.settings",
      "level": "D",
      "max_lines": 50
    }
    ```
  * **Response Output**:
    ```text
    04-09 02:00:00.000  1000  1000 I ActivityManager: Start proc ...
    04-09 02:00:01.000  1000  1000 I ActivityManager: Activity paused ...
    ```

### `clear_logcat`
* **Description**: Purges all log entries currently present inside the system Logcat buffers.
* **Parameters**: None
* **Returns**: Operation status success message.
* **Examples**:
  * **Request**: `{}`
  * **Response**: `"Logcat cleared"`

### `ping` (New)
* **Description**: Verifies continuous message connectivity with the on-device Mutton Agent.
* **Parameters**: None
* **Returns**: Latency response confirmation message.
* **Examples**:
  * **Request**: `{}`
  * **Response**: `"pong"`

### `get_agent_version`
* **Description**: Queries the build version number of the Mutton Agent current running on the device.
* **Parameters**: None
* **Returns**: Build version tag.
* **Examples**:
  * **Request**: `{}`
  * **Response**: `"1.0.0"`

---

## 5. Test Control (JUnit Test Orchestration)

These test services are executed by compiling and deploying specialized test JAR plugins directly onto the TestBed Core architecture. Because these programs run inside dedicated processes independent from the targeted systems, they escape common target app sandboxes or Android Instrumentation execution limitations, offering high runtime flexibility.

Management and development of these modules are conducted within the test suite projects (such as `niap-android-cert-ext`). If necessary, compile the plugin targets (including any shared modules like `common-utils`), build the final JAR, and register it inside the `plugins` folder of TestBed Core.

### `junit_test_reload`
* **Description**: Triggers a hot-reload of registered test JAR assemblies inside TestBed Core.
* **Parameters**: None
* **Returns**: Operation reload status metrics.

### `junit_test_list`
* **Description**: Queries a list of all current runnable test methods registered on the environment.
* **Parameters**: None
* **Returns**: A JSON array string of target class and method pointers.
* **Examples**:
  * **Request**: `{}`
  * **Response**: `["com.example.TestClass#testMethod1", "..."]`

### `junit_test_execute`
* **Description**: Initiates async execution of specified JUnit tests.
* **Parameters**:
  * `class_name` (String / Required): Absolute canonical identifier of the target class.
  * `method_name` (String / Optional): Target test case method identifier.
* **Returns**: Initialization success message.

### `junit_test_receive`
* **Description**: Captures current logs or structural exit results of target JUnit tests.
* **Parameters**: None
* **Returns**: A JSON payload containing execution statuses (`Running`, `Finished`), step traces, pass/fail state indicators, or exceptions stack traces.

---

## 6. Health (Connection Auditing and Recovery)

### `check_testbed_health`
* **Description**: Performs a holistic pipeline assessment, monitoring ADB connectivity status, physical devices links, and agent daemon processes.
* **Parameters**: None
* **Returns**: Diagnostic metrics JSON.
* **Examples**:
  * **Response**:
    ```json
    {
      "adbIsValid": true,
      "deviceSerial": "localhost:38189",
      "isRunning": false,
      "deviceInfo": "..."
    }
    ```

### `cleanup_agent`
* **Description**: Forcefully kills and restarts the on-device automation agent daemon process (essential for recovering from standard system UiAutomation lockouts).
* **Parameters**: None
* **Returns**: Status confirmation message.

---

## LLM Tips & Best Practices

1. **Token Conservation**: `get_ui_dump` returns the highly efficient `summary` flat list by default (~1KB). Use `format="json"` only when full coordinate systems or specific layout attributes are strictly necessary. Always target `get_logcat` queries with restrictive `tags`, levels, or `max_lines` scopes.
2. **Automatic Wait Cycles**: UI action tools (such as `tap` or `input_text`) automatically wait for the screen layout to settle into an idle state before resolving their interactable element summaries. Making separate `get_ui_dump` calls immediately after these actions is redundant.
3. **Reading Flat Summaries**: Lines are structured using the format: `[Index] ClassName "Label" tap(x,y) flags`. The coordinate map returned by `tap(x,y)` can be passed directly to touch tools. Nodes featuring `at(x,y)` represent non-interactable layout positions.
4. **Layout Crash Recovery**: If you encounter recurring target acquisition errors (e.g., `rootInActiveWindow returned null` exception messages), issue a `cleanup_agent` command to re-initialize the target context and recover the channel connection.
5. **Space Escapes in Terminal Commands**: When executing raw terminal commands via `execute_adb_shell` that pass arguments containing space strings (e.g., `input text "hello world"`), be aware of the underlying parser limitations (some environments might require replacing spaces with escape strings like `input text hello%sworld`). High-level tools (such as `input_text`) manage these encodings automatically, but this is an important consideration when using raw terminal execution scripts.
