# Agent Context - Permission Test Tool Update

This file provides context for AI agents working on this project.

## Project Goal
Update the Permission Test Tooling for Android 17 (SDK 37).

## Workspace Information
- **Root Directory**: `/usr/local/google/home/wkouki/AndroidStudioProjects/security-certification-resources/niap-cc/Permissions`
- **Active Branch**: `niap-permission-update-sdk37` (Ensure all work is done here)
- **Android 17 Source Directory**: `~/Android17`
- **Preliminary Investigation Data**: Look in `xpermission`.

## Component Summary
- `PermissionTester`: The legacy tester body. **Do not update.**
- `Tester`: Appears in directory listing, likely related to `PermissionTester`.
- `Companion`: Provides service stubs for calling services via Transaction ID and handles configuration. Needs review/update for Android 17.
- `TransactIds`: Tool to extract Transaction IDs from actual devices. Needs review/update for Android 17.

## Guidance for Agents
- This project is part of a security certification resource update.
- Focus on updating `Companion` and `TransactIds` or creating a new tester mechanism as needed for Android 17.
- Reference the research in `xpermission` to understand the changes in Android 17 permissions.

## Permissions Checklist Legend

The `permissions_checklist.csv` file uses the following status codes:
- `-`: 実装不可 (Not implementable)
- `0`: 未実装 (Not implemented)
- `1`: プレースホルダの準備 (Placeholder ready)
- `2`: 仮実装 (Tentative implementation)
- `3`: 実装 (Implemented)
- `4`: 動作確認済み (Verified)
- `5`: 実装済み (Completed)
