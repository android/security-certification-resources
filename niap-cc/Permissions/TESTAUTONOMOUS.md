# 自動テストの状況整理 (TESTAUTONOMOUS.md)

## 【重要】テスト手順 (Test Procedure)
テストを実行する際は、以下の手順に従ってください。

1. **設定でターゲットのモジュールを絞る**:
   - `SharedPreferences` のファイルを事前準備し、`adb push` でデバイスに配置して、実行するテストモジュールを制御します。
2. **Intentから対象のテストを実行する**:
   - UI操作は不安定なため、以下のインテントコマンドを使用してテストを実行します。
   ```bash
   # 自動実行フラグを立てて起動する場合
   adb shell am start -S -n com.android.certification.niap.permission.dpctester/.MainActivity --ez auto_run true
   ```
   - ※ `-S` オプションでアプリを強制停止してから起動することを推奨します。
3. **Platform -> Noperm の順でテストして正常に動いてるか確認する**:
   - `Platform` variant で正常系（権限がある場合の動作）を確認します。
   - `Noperm` variant で異常系（権限がない場合の `SecurityException` やタイムアウト等）を確認します。

このファイルは、Android 17 (SDK 37) 向け Permission Tester の自動実行における成功例と課題、および学んだ事項をまとめたものです。

## うまくいったこと (成功した操作)

1. **アプリの起動**:
   - `adb shell monkey` コマンドを使用して、パッケージ名 `com.android.certification.niap.permission.dpctester` からランチャー画面を確実に起動することに成功しました。
2. **誤操作からの回復**:
   - 誤って通知画面（ステータスバー）を引き下ろしてしまった際、`adb shell input keyevent 4` (BACKキー) を送信することで、通知画面を閉じ、アプリ画面に復帰することに成功しました。
3. **UIの改善**:
   - ボトムシートの展開を容易にするため、`MainActivity.kt` を修正し、矢印アイコンだけでなく `mainLayout` 全体のタップで展開できるようにしました。
4. **インテントによるテスト起動**:
   - UI操作をスキップしてテストを自動実行できるよう、`MainActivity.kt` にインテント経由での自動実行機能 (`auto_run` boolean extra) を追加しました。
   - コマンド: `adb shell am start -n com.android.certification.niap.permission.dpctester/.MainActivity --ez auto_run true`

## ACCESS_LOCAL_NETWORK の検証結果

- **Platform Variant**: 権限がある状態でテストが成功することを確認。
- **Normal Variant (権限あり)**: インストール時に `-g` で権限を付与した場合、テストが成功することを確認。
- **Noperm Variant (権限なし)**: 権限を持たない `noperm` variant で実行した場合も、`NsdManager.discoverServices` は成功（`onDiscoveryStarted` が呼ばれる）してしまうケースがある。
  - ただし、AppOps で警告ログ（`Operation not started: ... op=ACCESS_LOCAL_NETWORK`）が出力されている。
  - 現状のテスト実装（開始の成否のみ確認）では権限の有無を正しく判定できない可能性がある。実際のサービス検出まで待つ必要があるが、テスト環境にサービスが存在しないため未検証。

## 期待通りのエラーが出ない（サイレントパス）場合の対処法

テストが成功（PASSED）しているにもかかわらず、AppOps等で警告が出ている場合や、権限がないはずなのに動いている場合は以下のステップで調査します。

1. **AppOpsログの確認**:
   - `adb logcat` で `AppOps` のログを検索し、権限チェックが走っているか、拒否されているかを確認します。
2. **非同期処理の確認**:
   - APIが非同期の場合、呼び出し自体は成功しても、後続の処理で失敗している可能性があります。コールバックやリスナーの中で例外やエラーコードが返されていないか確認します。
3. **環境の確認**:
   - デバイスの種別やOSのビルドによって挙動が異なる場合があります。特に新しいパーミッションは実装が不完全な場合があります。
4. **チェックリストの更新**:
   - このようなケースでは、ステータスを安易に `4` (Verified) にせず、`3` (Implemented) に留め、メモに状況を記載します。

## うまくいかなかったこと (課題)

1. **ボトムシートの展開 (三角ボタンの操作)**:
   - 画面下部にある `bsArrow` (▲) をタップしても、ボトムシートが展開されませんでした。（UI改善により解決済み）
2. **テストの実行とログの確認**:
   - 当初はUI操作の失敗によりテストが走っていませんでしたが、インテント起動やUI改善により実行可能になりました。
3. **トランザクションIDの不足**:
   - `SignatureTestModuleCinnamonBun` の実装において、一部のメソッド（`getAssociationByDeviceId` など）のトランザクションIDが `binderdb-37.json` に含まれておらず、`NullPointerException` が発生するケースがありました。

## 学んだ事項・Tips

1. **トランザクションIDの抽出**:
   - `TransactIds` ツール（Androidプロジェクト）を使用することで、実機からトランザクションIDを抽出したJavaソースファイルを生成できます。
   - 実行にはプラットフォームキーでの署名が必要です。
2. **AOSPソースコードの検索**:
   - `find` コマンドは検索ツールとして直接使用できません（ポリシーによる制限）。
   - Androidのソースコードを検索する際は **`zoekt`** が使用可能です。
   - **重要**: `zoekt` を利用する前には、プロセスを起動しておく必要があります。
3. **モジュールの無効化**:
   - `SettingsFragment` では、インテントで渡された情報などから動的に設定画面を生成しており、これを利用することでモジュール単位での無効化（テストのスキップ）が可能です。
4. **Play Protect の干渉**:
   - テストアプリのインストールや実行時に Play Protect が干渉することがあります。
   - 設定で無効にしても、しばらくすると自動的に有効に戻ってしまうことがあるため、テスト実行時にブロックされる場合は再度無効化を確認してください。
   - adb shell settings put global package_verifier_user_consent -1
5. **MCPのLogcatツールの活用**:
   - ログの確認には、通常の `adb logcat` よりも MCP の `mcp_testbed_get_logcat` ツールを使用する方が、トークン消費を抑えられ、フィルタリングも効くため推奨されます。動作確認済み。

## トランザクションIDが不足する場合のワークフロー

トランザクションIDが `binderdb-37.json` に不足している場合、以下の手順で `TransactIds` プロジェクトを更新して再生成します。

1. **`Transacts.java` への定数追加**:
   - `TransactIds/app/src/main/java/.../transactids/Transacts.java` に、対象のメソッド名（例: `getAssociationByDeviceId`）を定数として定義します。
2. **`MainActivity.java` へのクエリ追加**:
   - `TransactIds/app/src/main/java/.../transactids/MainActivity.java` の `doInBackground` 内で、`queryTransactId(Descriptor, Transacts.MethodName, descriptorTransacts);` を呼び出すコードを追加します。
3. **`TransactIds` のビルドと実行**:
   - プラットフォームキーで署名してアプリをビルドし、実機にインストールします。
   - アプリを実行して新しいトランザクションIDを抽出します。
4. **JSONの再発行**:
   - 生成されたファイルを回収し、`binderdb-37.json` を更新します。

> [!TIP]
> 不足しているメソッドが多い場合は、一つずつ行うのではなく、メモしておいてまとめて実施する方が効率的です。

## 調査結果 (TrustTokenManager)
 
 `ACQUIRE_VERIFIED_DEVICE_TOKEN` 権限のテストに関して以下のことが判明しました。
 - **対象メソッド**: `ITrustTokenManager.aidl` 内の `acquirePreparedIdentitySet(in byte[] challenge)`。
 - **サービス名**: `Context.TRUST_TOKEN_SERVICE` (文字列値は `"trust_token"`)。
 - **トランザクションID**: `2` (実機から抽出成功)。
 - **対応**: `binderdb-37.json` に `"android.security.trusttoken.ITrustTokenManager":{"acquirePreparedIdentitySet":2}` を追加しました。
 
  ## 調査結果 (SignatureTestModuleCinnamonBun の大量失敗について)

 `SignatureTestModuleCinnamonBun` で発生している大量の失敗（約78件）について調査した結果、以下のことが判明しました。
 - **原因**: 該当モジュールのテストメソッドのほとんどが、ログ出力のみで実際のアクションを行わない「未実装」状態（スタブ）となっています。
 - **判定ロジックとの不一致**: `normal` バリアントでの実行時、署名権限のテストは「アクセス拒否（`SecurityException`）」が発生することを期待します。しかし、スタブメソッドは例外を投げずに正常終了するため、テストランナー側で「権限チェックをバイパスできた（＝失敗）」と判定されてしまいます。
 - **対応案**: 実際に権限が必要なAPIを呼び出すようにテストを実装する必要があります。ただし、数が多いことと、ソース構造の変更を避ける意図から、現時点ではこれらのスタブの実装は見送るのが妥当と判断しました。

 ## 今後の方針
 
 - **動作確認**: 更新した `binderdb-37.json` を含む `PermissionTester` を実機にインストールし、`SignatureTestModuleCinnamonBun` を実行して `NullPointerException` が解消されたことを確認します。
 
 ## ユーザーへの支援・変更依頼項目
 
 エージェント（LLM）側で詰まっている点や、対応していただけると助かる項目をまとめました。
 
 1. **SharedPreferences の外部からの変更**:
    - ユーザーが仰る通り、`adb shell run-as com.android.certification.niap.permission.dpctester` を使用して、`/data/data/.../shared_prefs/` 内の XML を編集することで、モジュールの有効/無効を外部から制御可能です。自動実行スクリプト等でこれを制御いただけると助かります。
 2. **インテント経由でのモジュール指定機能（アプリ側変更の要望）**:
    - 現在は `auto_run` で全テスト（有効なもの）が走りますが、特定のモジュールのみを指定して実行するインテントパラメータ（例: `modules_to_run` にキーのリストを渡す）があると、自動テストの制御が格段に容易になります。
 3. **`TransactIds` の実行と JSON の更新**:
    - **完了**: `TransactIds` を使用して `TrustTokenManager` のトランザクションIDを抽出し、`binderdb-37.json` を更新しました。
 4. **サービス名の確定**:
    - **完了**: `"trust_token"` で動作することを確認しました。
 ## SharedPreferences の書き換えと実装手順のメモ

 ### 1. SharedPreferences の書き換え (モジュールの絞り込み)
 - **課題**: 実機上の `sed` コマンドが不正なパターンエラーとなり失敗した。
 - **解決策**: `adb pull` で設定ファイルをPC側に取得し、PC側で編集した後に `adb push` で実機に戻す方法で確実に書き換えを実施した。
 - **結果**: `signature_test_module_binder` を `false` に設定し、`CinnamonBun` モジュールに集中できる環境を構築した。

 ### 2. テストの実装と検証順序 (ユーザー指示)
 今後、未実装のテストを実装していく際の手順は以下の通りとする。
 1. **APIの特定**: 対象の権限が必要なAPI（Binderトランザクションなど）を特定する。
 2. **テストの実装**: `SignatureTestModuleCinnamonBun.java` にテストロジックを実装する。
 3. **Platform variant での検証 (正常系)**: **先に** プラットフォーム署名版をインストールし、APIが正常に呼び出せること（例外が発生しないこと）を確認する。
 4. **Normal variant での検証 (異常系)**: 通常署名版をインストールし、期待通り `SecurityException` が発生してテストが「PASSED」になることを確認する。

 ### 3. TrustTokenManager のテスト結果と考察
 - **`testAcquireVerifiedDeviceToken`**:
     - **Platform variant**: `RemoteException` が発生したが、スタックトレースから `TrustTokenSqliteDatabase`（システムサーバー側）で例外が発生していることが判明。これは権限チェック（`acquireVerifiedDeviceToken_enforcePermission()`）を通過したことを意味するため、**正常系としては成功**と判断。

 ### 4. システムダイアログへの対応について
 - **注意**: テスト実行中にシステムダイアログが大量に発生し、画面を占有してテストが阻害される可能性がある（ユーザーからの警告あり）。
 - **対応方針**: ダイアログが発生した場合は、認識して頑張って消す（例: `adb shell input keyevent 4` でBACKキーを送信するなど）ことを意識する。

