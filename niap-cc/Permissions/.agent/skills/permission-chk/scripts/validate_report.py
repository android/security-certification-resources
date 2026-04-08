#!/usr/bin/env python3
import sys
import re

def validate_report(file_path):
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()

    errors = []

    # 必須セクションのチェック
    required_sections = [
        r"# 🔍 調査パーミッション:",
        r"## ⚖️ 調査項目（9カテゴリー）",
        r"1. \*\*未実装 \(WIP\) の除外\*\*",
        r"2. \*\*Phone以外",
        r"3. \*\*Feature Flag",
        r"4. \*\*\`BIND_\*",
        r"5. \*\*DPC",
        r"6. \*\*Google Play",
        r"7. \*\*対応すると思われるCTSテスト",
        r"8. \*\*再現パスの確認",
        r"9. \*\*許可時の危険度の判定"
    ]

    for section in required_sections:
        if not re.search(section, content):
            errors.append(f"Missing required section or pattern: {section}")

    # スコアのチェック (100, 10, 7.5, 5, 2.5, 0)
    score_match = re.search(r"リスクスコア:\s*(100|10|7\.5|5|2\.5|0)", content)
    if not score_match:
        errors.append("Risk score not found or invalid format. Must be 'リスクスコア: [100, 10, 7.5, 5, 2.5, 0]'.")

    # Zoektリンク (file:///) のチェック
    if "file:///" not in content:
        errors.append("No file:/// links found. Proof is required.")

    if errors:
        print("❌ Validation Failed:")
        for error in errors:
            print(f"  - {error}")
        return False
    else:
        print("✅ Validation Passed!")
        return True

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: validate_report.py <path_to_report.md>")
        sys.exit(1)
    
    success = validate_report(sys.argv[1])
    sys.exit(0 if success else 1)
