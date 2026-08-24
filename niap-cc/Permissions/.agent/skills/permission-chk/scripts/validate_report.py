#!/usr/bin/env python3
import sys
import re

def validate_report(file_path):
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()

    errors = []

    # Check for required sections
    required_sections = [
        r"# 🔍 Investigated Permission:",
        r"## ⚖️ Investigation Items \(9 Categories\)",
        r"1. \*\*Exclusion of Work-in-Progress \(WIP\)\*\*",
        r"2. \*\*Isolation of Non-Phone Limits",
        r"3. \*\*Identification of Feature Flag Control",
        r"4. \*\*Identification of \`BIND_\*",
        r"5. \*\*Identification of DPC",
        r"6. \*\*Whether It Applies Outside the Framework",
        r"7. \*\*Corresponding CTS Tests",
        r"8. \*\*Reproduction Path",
        r"9. \*\*Risk Level Assessment"
    ]

    for section in required_sections:
        if not re.search(section, content):
            errors.append(f"Missing required section or pattern: {section}")

    # Check for risk score (100, 10, 7.5, 5, 2.5, 0)
    score_match = re.search(r"Risk Score:\s*(100|10|7\.5|5|2\.5|0)", content)
    if not score_match:
        errors.append("Risk score not found or invalid format. Must be 'Risk Score: [100, 10, 7.5, 5, 2.5, 0]'.")

    # Check for Zoekt links (file:///)
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
