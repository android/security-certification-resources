# パーミッション調査スキル (Permission Research Skill)

## 概要
Android パーミッションの網羅的調査を手順通りに実行するためのスキルです。
`agent_and_report_plan.md` を正とし、手抜きを排除し、証拠に基づいたレポートを作成します。

## 利用可能ツール
- `run_command` (zoekt 検索の実行)
- `read_url_content` (zoekt web 検索の実行 - 推奨)
- `view_file` (コードの確認)
- `write_to_file` (レポートの作成)
- `send_message` (進捗報告)

## 🚫 禁止事項と行動規範（手抜き・サボり防止）

あなたはサブエージェント（ワーカー）として行動します。以下の行為は厳格に禁止されており、違反した場合はタスクが破棄されます。

1. **`run_command` による `grep` や `find` の実行**: AOSPなどの巨大なコードベースではパフォーマンスを悪化させるため禁止です。必ず `read_url_content` で Zoekt Web サーバー（`http://localhost:6070/search?q=...`）を利用してください。
2. **サブエージェントの起動 (`invoke_subagent`)**: タスクを外部に丸投げしてはいけません。必ずあなた自身がすべてのファイルを確認してください。
3. **ユーザーへの質問・承認待ち**: 調査中に迷った際、ユーザーに質問を投げて作業を止めてはいけません。見つからない場合は「見つからない」として、論理的に推論して評価を決定し、レポートを作成しきってください。
4. **自分が親エージェント（統括役）であると錯覚すること**: ユーザーや親エージェントに指示を出したり、作業を要求してはいけません。


## ワークフローと手順

あなたは以下の手順を **順番に** 実行しなければなりません。

### 手順 1: 調査対象の特定
指定されたCSVファイルやリストから、調査対象のパーミッション文字列（例: `android.permission.READ_CONTACTS`）を特定します。

### 手順 2: 調査開始の報告 (死活確認用)
調査対象が特定できたら、本格的な検索（Zoekt）を始める前に親エージェントに「調査開始」を報告します。
- **ファイル読み込み**: `xpermssion/active_parent_id.txt` から親の会話 ID を取得します。
- **メッセージ送信**: `send_message` を用いて、その ID に「調査開始: [Permission名]」と送信します。
- **目的**: 大量バッチ処理時にエージェントが生存しているか（ハングしていないか）を追跡するためです。

### 手順 3: Zoektによる超高速検索
対象のパーミッション文字列を `zoekt` を用いて検索します。
- 検索キーワード例: `android.permission.READ_CONTACTS` または `READ_CONTACTS`
- 目的: 定義箇所（`AndroidManifest.xml`）と、使用箇所（`checkPermission` や `RequiresPermission` 等）を特定する。
- **重要**: タイムアウトを防ぐため、`find` や標準の `grep` ではなく `zoekt` を使用してください。

### 手順 4: 9カテゴリーの分類とスコアリング
見つかったコードを元に、以下の9カテゴリーに分類します。
1. **未実装 (WIP) の除外**
2. **Phone以外 (Wear, Auto, TV 等) 制限の分離**
3. **Feature Flag 制御 (Default Disabled) の識別**
4. **`BIND_*` 系パーミッションの識別**
5. **DPC (Device Policy Controller) 系パーミッションの識別**
6. **Google Playなど framework外に適用されるPermissionかどうか?**
7. **対応すると思われるCTSテスト**
   - 対応するテストがある場合は、テスト名、テストの概要、ユーザーレベル環境での再現可能性、再現パスを記載する。
8. **再現パスの確認 (Javaコード、Intent、Shell)**
9. **許可時の危険度の判定 (Level 0-5, スコア 100-0)**
   - **Level 0 (スコア 100)**: 危険すぎて未定義、または運用で既にブロックされている（例: AccessibilityServiceなど）
   - **Level 1 (スコア 10)**: パスワード、トークン、金融情報
   - **Level 2 (スコア 7.5)**: プライバシー（連絡先、位置情報、写真等）
   - **Level 3 (スコア 5)**: PII（カレンダー、健康情報等）
   - **Level 4 (スコア 2.5)**: 比較的無害（アプリ一覧、設定等）
   - **Level 5 (スコア 0)**: 完全無害
   - スコア判定は `agent_and_report_plan.md` の表の **最後の数値** を使用すること。

### 手順 5: レポートの作成
`resources/report_template.md` を読み込み、内容を埋めてレポートを作成します。
- 出力先: `xpermssion/permission-research/<Permission名>.md`
- **証拠の添付**: 独自の「実証データ」セクションは廃止し、すべてのファイルリンク（file:///絶対パス#L行番号）、スニペット、再現方法を **「8. 再現パスの確認」** に集約してください。
- 利用頻度が多いと判断される場合、実際のソースコード（Java/C++等）上で `if` 文や明示的なチェック関数（`checkCallingOrSelfPermission` 等）によって権限がチェックされている箇所を **少なくとも5箇所** リストアップしてください。設定プロパティやManifestの定義のみではなく、実行コードを優先します。
- 再現方法には、**公開API**、**非公開/システムAPI**、**ServiceManager / Binder IPC 呼び出し**、**Shellコマンド** のどれを用いて検証可能かを具体的に明記してください。

### 手順 6: セルフバリデーションの実行
作成したレポートが基準を満たしているか、以下のコマンドで検証します。
```bash
python3 .agent/skills/permission-chk/scripts/validate_report.py xpermssion/permission-research/<Permission名>.md
```
- 検証が失敗した場合は、レポートを修正してください。

### 手順 7: 親エージェントへの報告 (🚨 極めて重要)

> [!IMPORTANT]
> この報告（`send_message`）が行われないと、大量のパーミッションを同時にバッチ処理する際に親エージェントがタスク完了を把握できず、システム全体が破綻します。迷いや人間の手動操作待ちを挟むことは許されません。

1. `xpermssion/active_parent_id.txt` の内容を読み込み、宛先となる親の会話 ID を取得します。
2. `send_message` ツールを用いて、取得した ID に調査完了を報告します。
   - **Recipient**: `active_parent_id.txt` で読み取った ID
   - **Message**: 「調査完了: android.permission.XXXX (スコア: 7.5)」などの概要

迷わずにツール（コマンド）を実行して送信を完了させてください。

---

## リソース
- テンプレート: `resources/report_template.md`
- バリデーションスクリプト: `scripts/validate_report.py`
- サンプル: `examples/gold_sample.md`
