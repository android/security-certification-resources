# 🔍 調査パーミッション: android.permission.WRITE_CONTACTS
リスクスコア: 7.5

## 📖 概要
このパーミッションは、アプリがユーザーの連絡先データ（RawContacts, Data など）を変更、追加、削除することを許可するものです。プライバシーに関わる重要な権限です。

## ⚖️ 調査項目（9カテゴリー）

1. **未実装 (WIP) の除外**: 非該当
   - 実装は存在し、使用されている。

2. **Phone以外 (Wear, Auto, TV 等) 制限の分離**: 非該当
   - 標準の連絡先機能であり、全フォームファクタで基本機能として動作。

3. **Feature Flag 制御 (Default Disabled) の識別**: 非該当
   - 基本機能であり、フラグによる無効化設定は見当たらない。

4. **`BIND_*` 系パーミッションの識別**: 非該当

5. **DPC (Device Policy Controller) 系パーミッションの識別**: 非該当

6. **Google Playなど framework外に適用されるPermissionかどうか?**: いいえ

7. **対応すると思われるCTSテスト**:
   - **テスト名**: `CtsContactsProviderTestCases` (または `CtsProviderTestCases`)
   - **テストの概要**: 権限を持たないアプリが連絡先プロバイダに書き込もうとした際に `SecurityException` が発生することを確認するテスト。
   - **ユーザーレベル再現可能性**: 可
   - **再現パス**: 特殊な権限を必要としないアプリから `ContentResolver.insert()` を実行し、失敗することを確認する。

8. **再現パスの確認**:
    - **利用頻度と配置箇所**: 非常に多い。テレフォニー、ダイヤラー、連絡先アプリ、アカウントマネージャーなど。
    - **ソースコードの場所と証拠コードスニペット (代表5箇所)**:

      1. **[IccPhoneBookInterfaceManager.java](file:///usr/local/google/home/wkouki/android17/frameworks/opt/telephony/src/java/com/android/internal/telephony/IccPhoneBookInterfaceManager.java#L203)**
         - **概要**: SIMの電話帳更新時の権限チェック。
         - **スニペット**:
           ```java
           if (mContext.checkCallingOrSelfPermission(android.Manifest.permission.WRITE_CONTACTS)
                   != PackageManager.PERMISSION_GRANTED) {
               throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
           }
           ```

      2. **[PreferredSimFallbackProvider.java](file:///usr/local/google/home/wkouki/android17/packages/apps/Dialer/java/com/android/dialer/preferredsim/impl/PreferredSimFallbackProvider.java#L169)**
         - **概要**: ダイヤラーの優先SIMフォールバックプロバイダでの権限チェック。
         - **スニペット**:
           ```java
           if (getContext().checkCallingOrSelfPermission(permission.WRITE_CONTACTS)
                   != PackageManager.PERMISSION_GRANTED) {
               throw new SecurityException("WRITE_CONTACTS required");
           }
           ```

      3. **[ContactSaveService.java](file:///usr/local/google/home/wkouki/android17/packages/apps/Contacts/src/com/android/contacts/ContactSaveService.java#L316)**
         - **概要**: 連絡先アプリでの保存処理前の権限チェック。
         - **スニペット**:
           ```java
           if (!PermissionsUtil.hasPermission(this, WRITE_CONTACTS)) {
               Log.w(TAG, "No WRITE_CONTACTS permission, unable to write to CP2");
               return;
           }
           ```

      4. **[UndemoteOutgoingCallReceiver.java](file:///usr/local/google/home/wkouki/android17/packages/apps/Dialer/java/com/android/dialer/interactions/UndemoteOutgoingCallReceiver.java#L45)**
         - **概要**: 発信時の連絡先更新時の権限チェック。
         - **スニペット**:
           ```java
           if (!PermissionsUtil.hasPermission(context, WRITE_CONTACTS)) {
               return;
           }
           ```

      5. **[AccountManagerService.java](file:///usr/local/google/home/wkouki/android17/frameworks/base/services/core/java/com/android/server/accounts/AccountManagerService.java#L6004)**
         - **概要**: アカウント認証マネージャーでの権限確認（ヘルパー経由）。
         - **スニペット**:
           ```java
           if (accountType.equals(serviceInfo.type.type)) {
               return isPermittedForPackage(serviceInfo.type.packageName, userId,
                   Manifest.permission.WRITE_CONTACTS);
           }
           ```

    - **再現方法 (検証手法)**:
      *基本的には公開APIやShellコマンドによる検証を優先してください。詳細は [advanced_verification_techniques.md](file:///usr/local/google/home/wkouki/android17/.agent/skills/permission-chk/resources/advanced_verification_techniques.md) を参照。*
      - **公開API**: `ContentResolver.insert(ContactsContract.RawContacts.CONTENT_URI, values)` を実行（これが最も標準的）。
      - **非公開/システムAPI**: `IccPhoneBookInterfaceManager` の AIDL インターフェース呼び出し（AIDLファイルの複製が必要な場合あり）。
      - **ServiceManager / Binder IPC**: `service call telephony.phonebook ...` のような Binder トランザクション（トランザクションIDの特定が必要）。
      - **Shellコマンド**: `adb shell content insert --uri content://com.android.contacts/raw_contacts ...`

9. **許可時の危険度の判定**:
   - **スコア**: 7.5
   - **所感とアタックシナリオ**: 連絡先情報の改ざん、意図しない削除が可能。フィッシングなどの二次被害に繋がる可能性があるため、プライバシーリスク（Level 2）と判定。
