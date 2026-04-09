# Testbed Automation MCP Reference for LLMs

このドキュメントは、Testbed Automation環境においてLLMエージェントがデバイスを制御・観測するためのMCP（Model Context Protocol）ツールの詳細なリファレンスです。
`mcp-spec.md` から更新され、最新のツール（ストリーミングやpingなど）の追加、正確なパラメータ、および**実行例**を含んでいます。

---

## 1. Sensing (状況把握)

デバイスの画面状態やシステム情報を取得するツールです。

### `get_ui_dump`
* **説明**: 現在の画面のUI階層(JSON)と、スクリーンショット画像(Base64)を取得します。
* **パラメータ**:
  * `include_image` (Boolean / 任意 / デフォルト:`false`) : 画像データをレスポンスに含めるかどうか。
  * `image_quality` (Int / 任意 / デフォルト:`2`) : 1=100%, 2=50%, 3=33%, 4=25%
* **戻り値**: `json_dump` (UI階層) および `screenshot` (Base64、要求された場合) を含むJSON
* **実行例**:
  * **リクエスト**:
    ```json
    {
      "include_image": false
    }
    ```
  * **レスポンス**:
    ```json
    {
      "json_dump": "{\"className\":\"android.widget.FrameLayout\",\"bounds\":\"[0,0][1080,2400]\",\"children\":[...]}"
    }
    ```

### `get_device_state`
* **説明**: 画面のON/OFF、ロック状態、フォアグラウンドのアプリを取得します。
* **パラメータ**: なし
* **戻り値**: 状態を示すJSON
* **実行例**:
  * **リクエスト**: `{}`
  * **レスポンス**:
    ```json
    {
      "is_screen_on": true,
      "is_locked": false,
      "foreground_package": "com.google.android.apps.nexuslauncher"
    }
    ```

### `get_device_info`
* **説明**: 端末のハードウェア・OS情報を取得します（`getprop` のラッパー）。
* **パラメータ**: なし
* **戻り値**: 端末情報を含むJSON
* **実行例**:
  * **リクエスト**: `{}`
  * **レスポンス**:
    ```json
    {
      "model": "Pixel 8",
      "os_version": "37",
      "abi": "arm64-v8a",
      "screen_size": "1080x2400"
    }
    ```

### `start_stream` (追加)
* **説明**: スクリーンショットのストリーミングを開始します。
* **パラメータ**:
  * `fps` (Float / 任意 / デフォルト:`1.0`) : 1秒あたりのフレーム数。
  * `image_quality` (Int / 任意 / デフォルト:`2`) : 画質（1〜4）。
* **戻り値**: 成功メッセージ
* **実行例**:
  * **リクエスト**:
    ```json
    {
      "fps": 2.0,
      "image_quality": 3
    }
    ```
  * **レスポンス**: `"Stream started"` (または同様の成功メッセージ)

### `stop_stream` (追加)
* **説明**: スクリーンショットのストリーミングを停止します。
* **パラメータ**: なし
* **戻り値**: 成功メッセージ
* **実行例**:
  * **リクエスト**: `{}`
  * **レスポンス**: `"Stream stopped"`

---

## 2. Action (UI操作)

デバイスに対してタッチや入力などの操作を行うツールです。これらは実行後、自動的に画面のアイドルを待ち、最新のUIダンプを返します。

### `tap`
* **説明**: 指定した(x, y)座標をタップします。
* **パラメータ**:
  * `x` (Int / 必須) : X座標
  * `y` (Int / 必須) : Y座標
* **戻り値**: 最新の `get_ui_dump` の結果
* **実行例**:
  * **リクエスト**:
    ```json
    {
      "x": 500,
      "y": 1000
    }
    ```
  * **レスポンス**: (最新のUIダンプJSON)

### `input_text`
* **説明**: フォーカスされている入力フィールドにテキストを入力します。
* **パラメータ**:
  * `text` (String / 必須) : 入力する文字列。スペースは自動的にエスケープされる場合があります。
* **戻り値**: 最新の `get_ui_dump` の結果
* **実行例**:
  * **リクエスト**:
    ```json
    {
      "text": "Hello World"
    }
    ```
  * **レスポンス**: (最新のUIダンプJSON)

### `swipe`
* **説明**: 画面を指定した座標間でスワイプします。
* **パラメータ**:
  * `start_x` (Int / 必須)
  * `start_y` (Int / 必須)
  * `end_x` (Int / 必須)
  * `end_y` (Int / 必須)
* **戻り値**: 最新の `get_ui_dump` の結果
* **実行例**:
  * **リクエスト**:
    ```json
    {
      "start_x": 500,
      "start_y": 1500,
      "end_x": 500,
      "end_y": 500
    }
    ```
  * **レスポンス**: (最新のUIダンプJSON)

### `press_key`
* **説明**: 物理キーまたはシステムキーのイベントを送信します。
* **パラメータ**:
  * `keycode` (String / 必須) : キー名 (例: `"BACK"`, `"HOME"`, `"ENTER"`)
* **戻り値**: 最新の `get_ui_dump` の結果
* **実行例**:
  * **リクエスト**:
    ```json
    {
      "keycode": "BACK"
    }
    ```
  * **レスポンス**: (最新のUIダンプJSON)

---

## 3. System (システム・ADB・アプリ管理)

### `execute_adb_shell`
* **説明**: 任意のADBシェルコマンドを実行します。
* **パラメータ**:
  * `command` (String / 必須) : 実行するコマンド
* **戻り値**: `stdout` および `stderr`
* **実行例**:
  * **リクエスト**:
    ```json
    {
      "command": "ls -l /sdcard"
    }
    ```
  * **レスポンス**:
    ```json
    "total 0\ndrwxrwxr-x 2 root sdcard_rw 4096 ...\n"
    ```
    *(注意: MCPのラップの仕方によってはJSONオブジェクトで返る場合もありますが、通常は文字列またはstdout/stderrのペアです。現在の実装ではプレーンテキストまたはJSON文字列として返されます。)*

### `open_settings`
* **説明**: 特定の設定画面をIntentで直接開きます。
* **パラメータ**:
  * `panel` (String / 必須) : `ROOT`, `SECURITY`, `WIFI`, `APP_DETAILS`, `DEVELOPER` など。
* **戻り値**: 最新のUIダンプ
* **実行例**:
  * **リクエスト**:
    ```json
    {
      "panel": "SECURITY"
    }
    ```
  * **レスポンス**: (設定画面のUIダンプJSON)

### `push_file`
* **説明**: ホストPCのファイルをデバイスに送信します。
* **パラメータ**:
  * `host_path` (String / 必須) : ホスト側のパス
  * `device_path` (String / 必須) : デバイス側のパス
* **戻り値**: 成功/失敗メッセージ
* **実行例**:
  * **リクエスト**:
    ```json
    {
      "host_path": "/tmp/test.apk",
      "device_path": "/data/local/tmp/test.apk"
    }
    ```
  * **レスポンス**: `"File pushed successfully"`

### `pull_file`
* **説明**: デバイスのファイルをホストPCに取得します。
* **パラメータ**:
  * `device_path` (String / 必須)
  * `host_path` (String / 必須)
* **戻り値**: 成功/失敗メッセージ
* **実行例**:
  * **リクエスト**:
    ```json
    {
      "device_path": "/sdcard/screenshot.png",
      "host_path": "/tmp/screenshot.png"
    }
    ```
  * **レスポンス**: `"File pulled successfully"`

### `install_app`
* **説明**: APKをインストールします。
* **パラメータ**:
  * `apk_path` (String / 必須) : ホスト側のAPKパス
* **戻り値**: 結果文字列
* **実行例**:
  * **リクエスト**:
    ```json
    {
      "apk_path": "/tmp/app.apk"
    }
    ```
  * **レスポンス**: `"Success"`

### `uninstall_app`
* **説明**: アプリをアンインストールします。
* **パラメータ**:
  * `package_name` (String / 必須)
* **戻り値**: 結果文字列
* **実行例**:
  * **リクエスト**:
    ```json
    {
      "package_name": "com.example.app"
    }
    ```
  * **レスポンス**: `"Success"`

---

## 4. Observe (ログ・状態観測)

### `get_logcat`
* **説明**: フィルタリングされたLogcatログを取得します。トークン節約のためにフィルタの使用を強く推奨します。
* **パラメータ**:
  * `tags` (String[] / 任意 / デフォルト:`[]`) : ログタグのリスト
  * `level` (String / 任意 / デフォルト:`"I"`) : 最小ログレベル (`V`, `D`, `I`, `W`, `E`, `F`)
  * `grep_pattern` (String / 任意 / デフォルト:`""`) : 絞り込み正規表現
  * `max_lines` (Int / 任意 / デフォルト:`100`) : 最大行数
* **戻り値**: ログのプレーンテキスト
* **実行例**:
  * **リクエスト**:
    ```json
    {
      "tags": ["ActivityManager"],
      "level": "I",
      "max_lines": 5
    }
    ```
  * **レスポンス**:
    ```text
    04-09 02:00:00.000  1000  1000 I ActivityManager: Start proc ...
    04-09 02:00:01.000  1000  1000 I ActivityManager: Activity paused ...
    ```

### `clear_logcat`
* **説明**: Logcatバッファをクリアします。
* **パラメータ**: なし
* **戻り値**: 成功メッセージ
* **実行例**:
  * **リクエスト**: `{}`
  * **レスポンス**: `"Logcat cleared"`

### `ping` (追加)
* **説明**: Mutton Agent（デバイス側エージェント）との疎通を確認します。
* **パラメータ**: なし
* **戻り値**: 応答メッセージ
* **実行例**:
  * **リクエスト**: `{}`
  * **レスポンス**: `"pong"`

### `get_agent_version`
* **説明**: デバイス上で動作しているMutton Agentのバージョンを取得します。
* **パラメータ**: なし
* **戻り値**: バージョン文字列
* **実行例**:
  * **リクエスト**: `{}`
  * **レスポンス**: `"1.0.0"`

---

## 5. Test Control (テスト制御)

### `junit_test_reload`
* **説明**: テストJarをリロードします。
* **パラメータ**: なし
* **戻り値**: ステータス情報

### `junit_test_list`
* **説明**: 実行可能なテストの一覧を取得します。
* **戻り値**: テストのリスト(JSON)
* **実行例**:
  * **リクエスト**: `{}`
  * **レスポンス**: `["com.example.TestClass#testMethod1", "..."]`

### `junit_test_execute`
* **説明**: テストの実行を開始します。
* **パラメータ**:
  * `class_name` (String / 必須) : テストクラス名
  * `method_name` (String / 任意) : メソッド名
* **戻り値**: 開始ステータス

### `junit_test_receive`
* **説明**: テストの実行結果を取得します。
* **戻り値**: 結果のJSON (Pass/Fail, Stacktrace等)

---

## 6. Health (環境・ヘルス)

### `check_testbed_health`
* **説明**: ADB接続やエージェントの状態を一括で診断します。
* **戻り値**: 診断結果のJSON
* **実行例**:
  * **レスポンス**:
    ```json
    {
      "adbIsValid": true,
      "deviceSerial": "localhost:38189",
      "isRunning": false,
      "deviceInfo": "..."
    }
    ```

### `cleanup_agent`
* **説明**: エージェントプロセスを強制終了してクリーンアップします（UiAutomationエラー時の復旧用）。
* **戻り値**: 成功メッセージ

---

## LLM向けTips & ベストプラクティス

1. **トークン節約**: `get_ui_dump` や `get_logcat` は情報量が多くなりがちです。`get_ui_dump` では `include_image` を必要な時だけ `true` にし、`get_logcat` では必ず `tags` や `max_lines` で絞り込んでください。
2. **自動待機**: `tap` や `input_text` などの操作ツールは、実行後に画面が落ち着く（アイドル状態になる）のを待ってから最新のUIダンプを返します。そのため、操作の直後に連続して `get_ui_dump` を呼ぶ必要はありません。
3. **復旧**: UI操作や取得でエラー（`rootInActiveWindow returned null` など）が発生した場合、`cleanup_agent` を実行してエージェントを再起動すると解決する場合があります。
4. **コマンドの注意**: `execute_adb_shell` でスペースを含むテキストを扱う場合（例: `input text "hello world"`）、Adamライブラリの仕様上、スペースを `%s` に置換する必要がある場合があります（`input text hello%sworld`）。MCPツール（`input_text`）側で処理されている場合は不要ですが、直接シェルを叩く場合は注意してください。
