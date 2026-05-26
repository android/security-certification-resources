# Permission Research Skill (SKILL.md)

## Overview
This skill is designed to execute a comprehensive investigation of Android permissions according to the defined procedure.
It strictly follows `agent_and_report_plan.md` to avoid shortcut actions and creates evidence-based reports.

## Available Tools
- `run_command` (to execute zoekt searches)
- `read_url_content` (to execute zoekt web searches - Recommended)
- `view_file` (to inspect code)
- `write_to_file` (to generate reports)
- `send_message` (to report progress)

## 🚫 Prohibited Actions & Code of Conduct (To Prevent Cutting Corners)

You will act as a subagent (worker). The following actions are strictly prohibited, and failure to comply will result in the task being discarded:

1. **Running `grep` or `find` via `run_command`**: Using these commands is prohibited in huge codebases like AOSP as it degrades performance. You MUST use `read_url_content` with the Zoekt Web Server (`http://localhost:6070/search?q=...`).
2. **Invoking Subagents (`invoke_subagent`)**: Do not outsource the task to another agent. You must inspect all files yourself.
3. **Asking Users for Questions/Approvals**: Do not block the task by asking the user questions when you are unsure during the investigation. If something is not found, record it as "not found", logically deduce and determine the evaluation, and complete the report.
4. **Acting as the Host/Orchestrator Agent**: Do not issue instructions or demand actions from the user or the parent agent.

## Workflow & Steps

You must execute the following steps **in order**:

### Step 1: Identify the Target
Identify the target permission string (e.g., `android.permission.READ_CONTACTS`) from the specified CSV file or list.

### Step 2: Report Start of Investigation (For Liveness Checks)
Once the target permission is identified, report "Start of Investigation" to the parent agent before beginning intensive searches (Zoekt).
- **Read File**: Retrieve the parent's Conversation ID from `xpermssion/active_parent_id.txt`.
- **Send Message**: Use `send_message` to send "Start: [Permission Name]" to that ID.
- **Purpose**: To track whether the agent is alive (not hung) during large batch processing.

### Step 3: Fast Search via Zoekt
Search the target permission string using `zoekt`.
- Search Keyword Example: `android.permission.READ_CONTACTS` or `READ_CONTACTS`
- Purpose: Identify the definition location (`AndroidManifest.xml`) and usage locations (such as `checkPermission` or `RequiresPermission`).
- **Important**: To prevent timeouts, use `zoekt` instead of `find` or standard `grep`.

### Step 4: Classify and Score Across 9 Categories
Based on the found code, classify the permission into the following 9 categories:
1. **Exclusion of Work-in-Progress (WIP)**
2. **Isolation of Non-Phone Limits (Wear, Auto, TV, etc.)**
3. **Identification of Feature Flag Control (Default Disabled)**
4. **Identification of `BIND_*` Permissions**
5. **Identification of DPC (Device Policy Controller) Permissions**
6. **Whether It Applies Outside the Framework (e.g., Google Play)**
7. **Corresponding CTS Tests**
   - If corresponding tests exist, document the test name, test summary, user-level reproducibility, and the path to reproduce.
8. **Reproduction Path Verification (Java code, Intent, Shell)**
9. **Risk Level Assessment on Permission Granted (Level 0-5, Score 100-0)**
   - **Level 0 (Score 100)**: Too dangerous to define, or already blocked in operation (e.g., AccessibilityService)
   - **Level 1 (Score 10)**: Passwords, tokens, financial data
   - **Level 2 (Score 7.5)**: Privacy (contacts, location, photos, etc.)
   - **Level 3 (Score 5)**: PII (calendar, health data, etc.)
   - **Level 4 (Score 2.5)**: Relatively harmless (app list, settings, etc.)
   - **Level 5 (Score 0)**: Completely harmless
   - The score must be determined using the **last number** from the table in `agent_and_report_plan.md`.

### Step 5: Create Report
Read `resources/report_template.md` and fill in the details to generate a report.
- Destination Path: `xpermssion/permission-research/<Permission_Name>.md`
- **Attach Evidence**: The unique "Verification Data" section is deprecated; consolidate all file links (file:///absolute_path#Lline_number), snippets, and reproduction methods inside **"8. Reproduction Path Verification"**.
- If the permission is deemed highly utilized, list **at least 5 locations** where the permission is explicitly checked (e.g., via `if` conditions or explicit checking functions like `checkCallingOrSelfPermission`) in the actual source code (Java/C++, etc.). Prioritize execution code over configuration properties or manifest definitions.
- For the reproduction method, clearly specify whether verification is possible using a **Public API**, **Non-Public/System API**, **ServiceManager / Binder IPC Call**, or **Shell Command**.

### Step 6: Execute Self-Validation
Verify if the generated report meets the criteria using the following command:
```bash
python3 .agent/skills/permission-chk/scripts/validate_report.py xpermssion/permission-research/<Permission_Name>.md
```
- If the validation fails, fix the report.

### Step 7: Report to Parent Agent (🚨 Extremely Important)

> [!IMPORTANT]
> If this report (`send_message`) is not sent, the parent agent cannot recognize the completion of tasks when batch-processing a large number of permissions simultaneously, causing the entire system to collapse. Do not delay or wait for manual human interaction.

1. Read the parent's Conversation ID from `xpermssion/active_parent_id.txt`.
2. Use the `send_message` tool to report completion to the retrieved ID.
   - **Recipient**: The ID read from `active_parent_id.txt`
   - **Message**: Summary message like "Completed: android.permission.XXXX (Score: 7.5)"

Execute the tools/commands without hesitation to complete the transmission.

---

## Resources
- Template: `resources/report_template.md`
- Validation Script: `scripts/validate_report.py`
- Example: `examples/gold_sample.md`
