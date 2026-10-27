# Project Knowledge for Agent Execution

This document contains architectural context, workflows, and guidelines for LLM agents to develop, run, and verify security test cases in this repository.

---

## 1. Starting and Stopping TestBed Core (MCP Server)
This project integrates with a desktop-based automation runner and MCP server hosted by the `testbed-core` application on port `11452` (`localhost:11452`). The server must be active for the agent to interface with physical devices or emulators.

### How to Start the Server
Open a separate terminal window, navigate to the `testbed-core` repository directory, and start the desktop program:

```bash
cd ../testbed-core
./gradlew :composeApp:run
```

### How to Stop (Kill) the Server
To prevent port collisions, dual-instance conflicts, or general socket errors (`Address already in use`), identify and force-kill any running processes holding port `11452`. It is recommended to perform this cleanup before launching automated runs:

```bash
# Force-kill processes listening on port 11452
lsof -t -i :11452 | xargs kill -9
```

---

## 2. Referencing Security Documents (MDFPP-CC)
To ensure test accuracy and compliance with security requirements, review the target criteria documented under the `docs/` folder (such as [FIA_XCU_EXT.1.md](docs/FIA_XCU_EXT.1.md) or related Common Criteria files). 

If the document references are empty or need to be synchronized with the remote requirements repository, run:

```bash
git submodule update --init --recursive
```

---

## 3. Document-Driven Test Development (Supreme Directive)
The core operating principle of this project is **strict adherence of test implementations to the security requirements documents**:
* Before generating or modifying any JUnit test suites, the LLM agent **must** read the relevant functional requirements and test case evaluation criteria from the target files in the `docs/` folder.
* The written JUnit test assertions must align perfectly with the Pass/Fail decision boundaries defined in the criteria.

---

## 4. Test Development and Execution Workflow
When developing and running security test suites, follow this structured workflow (refer to the main tool references for deeper API mechanics):

1. **Requirements Reference**: Retrieve target specifications from the target documents in `docs/`.
2. **Test Generation**: Author the Kotlin/Java test suites inside the `agent-test` module (e.g., under `org.example.plugin.fiax509`). Ensure all log entries adhere to the logging design patterns (using helpers like `logi` or `logp`).
3. **Compilation and Loading**:
   * Compile the JUnit plugin JAR:
     ```bash
     ./gradlew :agent-test:jar
     ```
   * Invoke the `junit_test_reload` MCP tool to notify the server and load the newly compiled test definitions.
4. **Execution and Monitoring**:
   * Trigger execution using the `junit_test_execute` MCP tool (passing target class and method arguments).
   * Poll `junit_test_receive` to stream log messages in real-time and capture structural exit states (Pass/Fail reports and stack traces).

---

## 5. Automation MCP Tools & Specifications
The following MCP tools are exposed by the `testbed-core` interface for automated workflow execution:

* **`junit_test_reload`**: Reloads and updates the JUnit plugin JAR context on the server.
* **`junit_test_list`**: Returns a list of all current tests loaded in the environment (as a JSON array).
* **`junit_test_execute`**: Accepts a class name and optional method identifier to begin execution.
* **`junit_test_receive`**: Retrieves current logs (`status: "Running"`) or finalized structural details (`status: "Finished"`, Pass/Fail indicator, exception details).

> [!NOTE]
> `junit_test_receive` supports active streaming. For long-running operations (e.g., processes taking over 2 minutes), safely poll `junit_test_receive` to track task execution progress.

---

## 6. Utilizing Timers for Passive Wait States
When the agent is required to enter a wait state (e.g. waiting for a compilation step, a background script, or multi-stage device deployments to complete), **always schedule a 60-second timer using the `schedule` tool** rather than generating repeated active prompt-and-response loops. This saves token budgets and resources.

---

## 7. Configuring the Local EST Test Server (libest)
For testing security certificate acquisitions (EST protocol) and client-side mutual TLS actions, run a local mock container based on **Cisco libest** and NGINX (configured under `est-server/`).
* See [est-server/README.md](est-server/README.md) for full docker deployment guidelines.
* Ensure the environment signature algorithm is configured to **SHA-384** (via `EST_CERT_PROFILE=niap`) to properly satisfy Common Criteria / NIAP verification criteria during automated runs.
