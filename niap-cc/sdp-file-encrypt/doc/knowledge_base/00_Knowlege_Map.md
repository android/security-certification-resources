# Security Evaluation Knowledge Base Map

This directory contains the essential documentation required for the Common Criteria (CC) evaluation of Android Security components.
AI assistants and human evaluators should refer to this map to understand the structure and precedence of the documents.

## 1. Common Criteria (CC) - Core Standards
* **CCPART1V3.1R5.md**: Introduction and General Model. Refer to this for definitions of standard terminology (e.g., TOE, TSF).
* **CCPART2V3.1R5.md**: Security Functional Requirements (SFR). The catalog of functional components (e.g., `FCS_CKM.1`).
* **CCPART3V3.1R5.md**: Security Assurance Requirements (SAR). The criteria for assurance depth and deliverables (e.g., `ADV_FSP`).

## 2. Methodology - Evaluation Procedures
* **CEMV3.1R5.md** (ISO/IEC 18045): Common Evaluation Methodology. Contains specific inspection procedures and pass/fail criteria for evaluators.

## 3. Requirements for Mobile - Specific Rules
* **PP_MDF_V3.3.md**: Mobile Device Fundamentals Protection Profile.
    * **CRITICAL:** This document defines the specific security requirements for Android devices.
    * **Precedence:** When checking for implementation requirements, the definitions in this PP **override** or refine the generic definitions in CC Part 2.

## 4. Security Targets (ST) - Implementation Examples
* **st-samsung-android15.md**: Security Target for Samsung devices (Reference).
* **st-google-android15.md**: Security Target for Google Pixel devices (Reference).
* *Note: Use these to understand how specific requirements are typically implemented and documented by major vendors.*

---

## AI System Instructions (Rules of Precedence)

When generating responses or code based on this knowledge base, strictly adhere to the following order of precedence:

1.  **Requirement Specifics:** Always prioritize **`PP_MDF_V3.3`**. If the PP defines a specific parameter for an algorithm (e.g., "AES-GCM must use a 96-bit IV"), this takes precedence over generic CC documents.
2.  **Definitions:** For general understanding of requirement IDs (e.g., what `FCS_CKM.1` generally means), refer to **`CCPART2V3.1R5md`**.
3.  **Testing & Assurance:** For questions regarding test depth, coverage analysis, or document detail levels, refer to **`CCPART3V3.1R5.md`** and **`CEMV3.1R5.md`**.