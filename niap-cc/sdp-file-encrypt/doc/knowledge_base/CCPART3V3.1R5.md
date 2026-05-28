Consider using the pymupdf_layout package for a greatly improved page layout analysis.
# **Foreword**

This version of the Common Criteria for Information Technology Security Evaluation (CC
v3.1) is the first major revision since being published as CC v2.3 in 2005.

CC v3.1 aims to: eliminate redundant evaluation activities; reduce/eliminate activities that
contribute little to the final assurance of a product; clarify CC terminology to reduce
misunderstanding; restructure and refocus the evaluation activities to those areas where
security assurance is gained; and add new CC requirements if needed.

CC version 3.1 consists of the following parts:


 Part 1: Introduction and general model


 Part 2: Security functional components


 Part 3: Security assurance components


_**Trademarks:**_


 UNIX is a registered trademark of The Open Group in the United States and other
countries


 Windows is a registered trademark of Microsoft Corporation in the United States
and other countries


Page 2 of 247 Version 3.1 April 2017


_**Legal Notice:**_

_The governmental organisations listed below contributed to the development of this version_
_of the Common Criteria for Information Technology Security Evaluation. As the joint_
_holders of the copyright in the Common Criteria for Information Technology Security_
_Evaluation, version_ 3.1 _Parts 1 through 3 (called “CC_ 3.1 _”), they hereby grant non-_
_exclusive license to ISO/IEC to use CC_ 3.1 _in the continued development/maintenance of the_
_ISO/IEC 15408 international standard. However, these governmental organisations retain_
_the right to use, copy, distribute, translate or modify CC_ 3.1 _as they see fit._

_Australia:_ _The Australian Signals Directorate;_
_Canada:_ _Communications Security Establishment;_
_France:_ _Agence Nationale de la Sécurité des Systèmes d'Information;_
_Germany:_ _Bundesamt für Sicherheit in der Informationstechnik;_
_Japan:_ _Information Technology Promotion Agency;_
_Netherlands:_ _Netherlands National Communications Security Agency;_
_New Zealand:_ _Government Communications Security Bureau;_
_Republic of Korea:_ _National Security Research Institute;_
_Spain:_ _Ministerio de Administraciones Públicas and_
_Centro Criptológico Nacional;_
_Sweden:_ _Swedish Defence Materiel Administration;_
_United Kingdom:_ _National Cyber Security Centre;_
_United States:_ _The National Security Agency and the_
_National Institute of Standards and Technology._


April 2017 Version 3.1 Page 3 of 247


**Table of contents**

# **Table of Contents**


**1** **INTRODUCTION ............................................................................................. 11**


**2** **SCOPE ........................................................................................................... 12**


**3** **NORMATIVE REFERENCES ......................................................................... 13**


**4** **TERMS AND DEFINITIONS, SYMBOLS AND ABBREVIATED TERMS ...... 14**


**5** **OVERVIEW ..................................................................................................... 15**


**5.1** **Organisation of CC Part 3 ..................................................................................................................... 15**


**6** **ASSURANCE PARADIGM ............................................................................. 16**


**6.1** **CC philosophy ........................................................................................................................................ 16**


**6.2** **Assurance approach ............................................................................................................................... 16**
6.2.1 Significance of vulnerabilities ........................................................................................................ 16
6.2.2 Cause of vulnerabilities .................................................................................................................. 17
6.2.3 CC assurance .................................................................................................................................. 17
6.2.4 Assurance through evaluation......................................................................................................... 17


**6.3** **The CC evaluation assurance scale ....................................................................................................... 18**


**7** **SECURITY ASSURANCE COMPONENTS .................................................... 19**


**7.1** **Security assurance classes, families and components structure ......................................................... 19**
7.1.1 Assurance class structure ................................................................................................................ 19
7.1.2 Assurance family structure ............................................................................................................. 20
7.1.3 Assurance component structure ...................................................................................................... 21
7.1.4 Assurance elements ........................................................................................................................ 24
7.1.5 Component taxonomy..................................................................................................................... 24


**7.2** **EAL structure ......................................................................................................................................... 24**
7.2.1 EAL name ....................................................................................................................................... 25
7.2.2 Objectives ....................................................................................................................................... 25
7.2.3 Application notes ............................................................................................................................ 25
7.2.4 Assurance components ................................................................................................................... 26
7.2.5 Relationship between assurances and assurance levels .................................................................. 26


**7.3** **CAP structure ......................................................................................................................................... 27**
7.3.1 CAP name ....................................................................................................................................... 28
7.3.2 Objectives ....................................................................................................................................... 28
7.3.3 Application notes ............................................................................................................................ 28
7.3.4 Assurance components ................................................................................................................... 28
7.3.5 Relationship between assurances and assurance levels .................................................................. 29


**8** **EVALUATION ASSURANCE LEVELS .......................................................... 31**


**8.1** **Evaluation assurance level (EAL) overview ........................................................................................ 31**


Page 4 of 247 Version 3.1 April 2017


**Table of contents**


**8.2** **Evaluation assurance level details ......................................................................................................... 32**


**8.3** **Evaluation assurance level 1 (EAL1) - functionally tested ................................................................. 33**


**8.4** **Evaluation assurance level 2 (EAL2) - structurally tested .................................................................. 35**


**8.5** **Evaluation assurance level 3 (EAL3) - methodically tested and checked .......................................... 37**


**8.6** **Evaluation assurance level 4 (EAL4) - methodically designed, tested, and reviewed ...................... 39**


**8.7** **Evaluation assurance level 5 (EAL5) - semiformally designed and tested ........................................ 41**


**8.8** **Evaluation assurance level 6 (EAL6) - semiformally verified design and tested .............................. 43**


**8.9** **Evaluation assurance level 7 (EAL7) - formally verified design and tested ...................................... 45**


**9** **COMPOSED ASSURANCE PACKAGES ...................................................... 47**


**9.1** **Composed assurance package (CAP) overview ................................................................................... 47**


**9.2** **Composed assurance package details ................................................................................................... 49**


**9.3** **Composition assurance level A (CAP-A) - Structurally composed .................................................... 50**


**9.4** **Composition assurance level B (CAP-B) - Methodically composed ................................................... 52**


**9.5** **Composition assurance level C (CAP-C) - Methodically composed, tested and reviewed ............... 54**


**10** **CLASS APE: PROTECTION PROFILE EVALUATION ................................. 56**


**10.1** **PP introduction (APE_INT) ............................................................................................................. 58**


**10.2** **Conformance claims (APE_CCL) .................................................................................................... 59**


**10.3** **Security problem definition (APE_SPD) ......................................................................................... 61**


**10.4** **Security objectives (APE_OBJ) ........................................................................................................ 62**


**10.5** **Extended components definition (APE_ECD) ................................................................................ 64**


**10.6** **Security requirements (APE_REQ) ................................................................................................. 65**


**11** **CLASS ACE: PROTECTION PROFILE CONFIGURATION EVALUATION .. 67**


**11.1** **PP-Module introduction (ACE_INT) .............................................................................................. 69**


**11.2** **PP-Module conformance claims (ACE_CCL) ................................................................................ 70**


**11.3** **PP-Module Security problem definition (ACE_SPD) .................................................................... 71**


**11.4** **PP-Module Security objectives (ACE_OBJ) ................................................................................... 72**


**11.5** **PP-Module extended components definition (ACE_ECD) ............................................................ 73**


**11.6** **PP-Module security requirements (ACE_REQ) ............................................................................. 74**


**11.7** **PP-Module consistency (ACE_MCO) .............................................................................................. 75**


April 2017 Version 3.1 Page 5 of 247


**Table of contents**


**11.8** **PP-Configuration consistency (ACE_CCO) ................................................................................... 76**


**12** **CLASS ASE: SECURITY TARGET EVALUATION ....................................... 78**


**12.1** **ST introduction (ASE_INT) ............................................................................................................. 79**


**12.2** **Conformance claims (ASE_CCL) .................................................................................................... 80**


**12.3** **Security problem definition (ASE_SPD) ......................................................................................... 82**


**12.4** **Security objectives (ASE_OBJ) ........................................................................................................ 83**


**12.5** **Extended components definition (ASE_ECD) ................................................................................ 85**


**12.6** **Security requirements (ASE_REQ) ................................................................................................. 86**


**12.7** **TOE summary specification (ASE_TSS) ......................................................................................... 88**


**13** **CLASS ADV: DEVELOPMENT ...................................................................... 90**


**13.1** **Security Architecture (ADV_ARC) ................................................................................................. 97**


**13.2** **Functional specification (ADV_FSP) ............................................................................................... 99**
13.2.1 Detail about the Interfaces ............................................................................................................ 101
13.2.2 Components of this Family ........................................................................................................... 102


**13.3** **Implementation representation (ADV_IMP) ................................................................................ 109**


**13.4** **TSF internals (ADV_INT) .............................................................................................................. 113**


**13.5** **Security policy modelling (ADV_SPM) ......................................................................................... 117**


**13.6** **TOE design (ADV_TDS) ................................................................................................................. 120**
13.6.1 Detail about the Subsystems and Modules ................................................................................... 121


**14** **CLASS AGD: GUIDANCE DOCUMENTS .................................................... 129**


**14.1** **Operational user guidance (AGD_OPE) ....................................................................................... 130**


**14.2** **Preparative procedures (AGD_PRE) ............................................................................................ 133**


**15** **CLASS ALC: LIFE-CYCLE SUPPORT ........................................................ 135**


**15.1** **CM capabilities (ALC_CMC) ........................................................................................................ 137**


**15.2** **CM scope (ALC_CMS) ................................................................................................................... 146**


**15.3** **Delivery (ALC_DEL) ...................................................................................................................... 151**


**15.4** **Development security (ALC_DVS) ................................................................................................ 153**


**15.5** **Flaw remediation (ALC_FLR) ....................................................................................................... 155**


**15.6** **Life-cycle definition (ALC_LCD) .................................................................................................. 160**


**15.7** **Tools and techniques (ALC_TAT) ................................................................................................. 163**


Page 6 of 247 Version 3.1 April 2017


**Table of contents**


**16** **CLASS ATE: TESTS .................................................................................... 166**


**16.1** **Coverage (ATE_COV) .................................................................................................................... 167**


**16.2** **Depth (ATE_DPT) ........................................................................................................................... 170**


**16.3** **Functional tests (ATE_FUN) .......................................................................................................... 174**


**16.4** **Independent testing (ATE_IND) .................................................................................................... 177**


**17** **CLASS AVA: VULNERABILITY ASSESSMENT ......................................... 182**


**17.1** **Vulnerability analysis (AVA_VAN) ............................................................................................... 184**


**18** **CLASS ACO: COMPOSITION ..................................................................... 189**


**18.1** **Composition rationale (ACO_COR) .............................................................................................. 193**


**18.2** **Development evidence (ACO_DEV) .............................................................................................. 194**


**18.3** **Reliance of dependent component (ACO_REL) ........................................................................... 198**


**18.4** **Composed TOE testing (ACO_CTT) ............................................................................................. 201**


**18.5** **Composition vulnerability analysis (ACO_VUL) ......................................................................... 204**


**A** **DEVELOPMENT (ADV) ................................................................................ 207**


**A.1** **ADV_ARC: Supplementary material on security architectures ................................................. 207**
A.1.1 Security architecture properties .................................................................................................... 207
A.1.2 Security architecture descriptions ................................................................................................. 208


**A.2** **ADV_FSP: Supplementary material on TSFIs ............................................................................. 211**
A.2.1 Determining the TSFI ................................................................................................................... 212
A.2.2 Example: A complex DBMS ........................................................................................................ 215
A.2.3 Example Functional Specification ................................................................................................ 216


**A.3** **ADV_INT: Supplementary material on TSF internals ................................................................ 218**
A.3.1 Structure of procedural software ................................................................................................... 219
A.3.2 Complexity of procedural software .............................................................................................. 221


**A.4** **ADV_TDS: Subsystems and Modules ............................................................................................ 222**
A.4.1 Subsystems ................................................................................................................................... 222
A.4.2 Modules ........................................................................................................................................ 223
A.4.3 Levelling Approach ...................................................................................................................... 226


**A.5** **Supplementary material on formal methods................................................................................. 228**


**B** **COMPOSITION (ACO) ................................................................................. 230**


**B.1** **Necessity for composed TOE evaluations ...................................................................................... 230**


**B.2** **Performing Security Target evaluation for a composed TOE ..................................................... 232**


**B.3** **Interactions between composed IT entities ................................................................................... 233**


April 2017 Version 3.1 Page 7 of 247


**Table of contents**


**C** **CROSS REFERENCE OF ASSURANCE COMPONENT DEPENDENCIES 240**


**D** **CROSS REFERENCE OF PPS AND ASSURANCE COMPONENTS ......... 245**


**E** **CROSS REFERENCE OF EALS AND ASSURANCE COMPONENTS ....... 246**


**F** **CROSS REFERENCE OF CAPS AND ASSURANCE COMPONENTS ...... 247**


Page 8 of 247 Version 3.1 April 2017


**List of figures**

# **List of figures**


Figure 1 - Assurance class/family/component/element hierarchy ......................................... 20
Figure 2 - Assurance component structure ............................................................................ 22
Figure 3 - Sample class decomposition diagram ................................................................... 24
Figure 4 - EAL structure ........................................................................................................ 25
Figure 5 - Assurance and assurance level association ........................................................... 27
Figure 6 - CAP structure ........................................................................................................ 28
Figure 7 - Assurance and composed assurance package association .................................... 30
Figure 8 - APE: Protection Profile evaluation class decomposition ..................................... 57
Figure 9 - ACE: Protection Profile Configuration evaluation class decomposition ............. 67
Figure 10 - ASE: Security Target evaluation class decomposition ....................................... 78
Figure 11 - Relationships of ADV constructs to one another and to other families ............. 92
Figure 12 - ADV: Development class decomposition ........................................................... 96
Figure 13 - AGD: Guidance documents class decomposition ............................................. 129
Figure 14 - ALC: Life-cycle support class decomposition ................................................. 136
Figure 15 - ATE: Tests class decomposition ....................................................................... 166
Figure 16 - AVA: Vulnerability assessment class decomposition ...................................... 182
Figure 17 - Relationship between ACO families and interactions between components ... 190
Figure 18 - Relationship between ACO families ................................................................ 191
Figure 19 - ACO: Composition class decomposition .......................................................... 192
Figure 20 - Wrappers ........................................................................................................... 213
Figure 21 - Interfaces in a DBMS system ........................................................................... 215
Figure 22 - Subsystems and Modules .................................................................................. 222
Figure 23 - Base component abstraction ............................................................................. 234
Figure 24 - Dependent component abstraction .................................................................... 235
Figure 25 - Composed TOE abstraction .............................................................................. 236
Figure 26 - Composed component interfaces ...................................................................... 236


April 2017 Version 3.1 Page 9 of 247


**List of tables**

# **List of tables**


Table 1 - Evaluation assurance level summary ..................................................................... 32
Table 2 - EAL1 ..................................................................................................................... 34
Table 3 - EAL2 ..................................................................................................................... 36
Table 4 - EAL3 ..................................................................................................................... 38
Table 5 - EAL4 ..................................................................................................................... 40
Table 6 - EAL5 ..................................................................................................................... 42
Table 7 - EAL6 ..................................................................................................................... 44
Table 8 - EAL7 ..................................................................................................................... 46
Table 9 - Composition assurance level summary ................................................................. 48
Table 10 - CAP-A ................................................................................................................. 51
Table 11 - CAP-B ................................................................................................................. 53
Table 12 - CAP-C ................................................................................................................. 55
Table 13 - PP assurance packages ........................................................................................ 56
Table 14 Description Detail Levelling ................................................................................ 228
Table 15 Dependency table for Class ACO: Composition ................................................. 240
Table 16 Dependency table for Class ADV: Development ................................................ 241
Table 17 Dependency table for Class AGD: Guidance documents .................................... 241
Table 18 Dependency table for Class ALC: Life-cycle support ......................................... 242
Table 19 Dependency table for Class APE: Protection Profile evaluation ......................... 242
Table 20 Dependency table for Class ACE: Protection Profile Configuration evaluation . 243
Table 21 Dependency table for Class ASE: Security Target evaluation ............................ 243
Table 22 Dependency table for Class ATE: Tests .............................................................. 244
Table 23 Dependency table for Class AVA: Vulnerability assessment ............................. 244
Table 24 PP assurance level summary ................................................................................ 245
Table 25 Evaluation assurance level summary ................................................................... 246
Table 26 Composition assurance level summary................................................................ 247


Page 10 of 247 Version 3.1 April 2017


**Introduction**

# **1 Introduction**


1 Security assurance components, as defined in this CC Part 3, are the basis for
the security assurance requirements expressed in a Protection Profile (PP) or
a Security Target (ST).


2 These requirements establish a standard way of expressing the assurance
requirements for TOEs. This CC Part 3 catalogues the set of assurance
components, families and classes. This CC Part 3 also defines evaluation
criteria for PPs and STs and presents evaluation assurance levels that define
the predefined CC scale for rating assurance for TOEs, which is called the
Evaluation Assurance Levels (EALs).


3 The audience for this CC Part 3 includes consumers, developers, and
evaluators of secure IT products. CC Part 1 Chapter 7 provides additional
information on the target audience of the CC, and on the use of the CC by the
groups that comprise the target audience. These groups may use this part of
the CC as follows:


a) Consumers, who use this CC Part 3 when selecting components to
express assurance requirements to satisfy the security objectives
expressed in a PP or ST, determining required levels of security
assurance of the TOE.


b) Developers, who respond to actual or perceived consumer security
requirements in constructing a TOE, reference this CC Part 3 when
interpreting statements of assurance requirements and determining
assurance approaches of TOEs.


c) Evaluators, who use the assurance requirements defined in this part of
the CC as mandatory statement of evaluation criteria when
determining the assurance of TOEs and when evaluating PPs and STs.


April 2017 Version 3.1 Page 11 of 247


**Scope**

# **2 Scope**


4 This CC Part 3 defines the assurance requirements of the CC. It includes the
evaluation assurance levels (EALs) that define a scale for measuring
assurance for component TOEs, the composed assurance packages (CAPs)
that define a scale for measuring assurance for composed TOEs, the
individual assurance components from which the assurance levels and
packages are composed, and the criteria for evaluation of PPs and STs.


Page 12 of 247 Version 3.1 April 2017


**Normative references**

# **3 Normative references**


5 The following referenced documents are indispensable for the application of
this document. For dated references, only the edition cited applies. For
undated references, the latest edition of the referenced document (including
any amendments) applies.


[CC-1] Common Criteria for Information Technology
Security Evaluation, Version 3.1, revision 5, April
2017. Part 1: Introduction and general model.


[CC-2] Common Criteria for Information Technology
Security Evaluation, Version 3.1, revision 5, April
2017. Part 2: Functional security components.


April 2017 Version 3.1 Page 13 of 247


**Terms and definitions, symbols and abbreviated terms**

# **4 Terms and definitions, symbols and** **abbreviated terms**


6 For the purposes of this document, the terms, definitions, symbols and
abbreviated terms given in CC Part 1 apply.


Page 14 of 247 Version 3.1 April 2017


**Overview**

# **5 Overview**

## **5.1 Organisation of CC Part 3**


7 Chapter 6 describes the paradigm used in the security assurance requirements
of CC Part 3.


8 Chapter 7 describes the presentation structure of the assurance classes,
families, components, evaluation assurance levels along with their
relationships, and the structure of the composed assurance packages. It also
characterises the assurance classes and families found in Chapters 10 through
18.


9 Chapter 8 provides detailed definitions of the EALs.


10 Chapter 9 provides detailed definitions of the CAPs.


11 Chapters 10 through 18 provide the detailed definitions of the CC Part 3
assurance classes.


12 Annex A provides further explanations and examples of the concepts behind
the Development class.


13 Annex B provides an explanation of the concepts behind composed TOE
evaluations and the Composition class.


14 Annex C provides a summary of the dependencies between the assurance
components.


15 Annex D provides a cross reference between PPs and the families and
components of the APE class.


16 Annex E provides a cross reference between the EALs and the assurance
components.


17 Annex F provides a cross reference between the CAPs and the assurance
components.


April 2017 Version 3.1 Page 15 of 247


**Assurance paradigm**

# **6 Assurance paradigm**


18 The purpose of this Chapter is to document the philosophy that underpins the
CC approach to assurance. An understanding of this Chapter will permit the
reader to understand the rationale behind the CC Part 3 assurance
requirements.

## **6.1 CC philosophy**


19 The CC philosophy is that the threats to security and organisational security
policy commitments should be clearly articulated and the proposed security
measures be demonstrably sufficient for their intended purpose.


20 Furthermore, measures should be adopted that reduce the likelihood of
vulnerabilities, the ability to exercise (i.e. intentionally exploit or
unintentionally trigger) a vulnerability, and the extent of the damage that
could occur from a vulnerability being exercised. Additionally, measures
should be adopted that facilitate the subsequent identification of
vulnerabilities and the elimination, mitigation, and/or notification that a
vulnerability has been exploited or triggered.

## **6.2 Assurance approach**


21 The CC philosophy is to provide assurance based upon an evaluation (active
investigation) of the IT product that is to be trusted. Evaluation has been the
traditional means of providing assurance and is the basis for prior evaluation
criteria documents. In aligning the existing approaches, the CC adopts the
same philosophy. The CC proposes measuring the validity of the
documentation and of the resulting IT product by expert evaluators with
increasing emphasis on scope, depth, and rigour.


22 The CC does not exclude, nor does it comment upon, the relative merits of
other means of gaining assurance. Research continues with respect to
alternative ways of gaining assurance. As mature alternative approaches
emerge from these research activities, they will be considered for inclusion
in the CC, which is so structured as to allow their future introduction.


**6.2.1** **Significance of vulnerabilities**


23 It is assumed that there are threat agents that will actively seek to exploit
opportunities to violate security policies both for illicit gains and for wellintentioned, but nonetheless insecure actions. Threat agents may also
accidentally trigger security vulnerabilities, causing harm to the organisation.
Due to the need to process sensitive information and the lack of availability
of sufficiently trusted products, there is significant risk due to failures of IT.
It is, therefore, likely that IT security breaches could lead to significant loss.


24 IT security breaches arise through the intentional exploitation or the
unintentional triggering of vulnerabilities in the application of IT within
business concerns.


Page 16 of 247 Version 3.1 April 2017


**Assurance paradigm**


25 Steps should be taken to prevent vulnerabilities arising in IT products. To the
extent feasible, vulnerabilities should be:


a) eliminated -- that is, active steps should be taken to expose, and
remove or neutralise, all exercisable vulnerabilities;


b) minimised -- that is, active steps should be taken to reduce, to an
acceptable residual level, the potential impact of any exercise of a
vulnerability;


c) monitored -- that is, active steps should be taken to ensure that any
attempt to exercise a residual vulnerability will be detected so that
steps can be taken to limit the damage.


**6.2.2** **Cause of vulnerabilities**


26 Vulnerabilities can arise through failures in:


a) requirements -- that is, an IT product may possess all the functions
and features required of it and still contain vulnerabilities that render
it unsuitable or ineffective with respect to security;


b) development -- that is, an IT product does not meet its specifications
and/or vulnerabilities have been introduced as a result of poor
development standards or incorrect design choices;


c) operation -- that is, an IT product has been constructed correctly to a
correct specification but vulnerabilities have been introduced as a
result of inadequate controls upon the operation.


**6.2.3** **CC assurance**


27 Assurance is grounds for confidence that an IT product meets its security
objectives. Assurance can be derived from reference to sources such as
unsubstantiated assertions, prior relevant experience, or specific experience.
However, the CC provides assurance through active investigation. Active
investigation is an evaluation of the IT product in order to determine its
security properties.


**6.2.4** **Assurance through evaluation**


28 Evaluation has been the traditional means of gaining assurance, and is the
basis of the CC approach. Evaluation techniques can include, but are not
limited to:


a) analysis and checking of process(es) and procedure(s);


b) checking that process(es) and procedure(s) are being applied;


c) analysis of the correspondence between TOE design representations;


d) analysis of the TOE design representation against the requirements;


April 2017 Version 3.1 Page 17 of 247


**Assurance paradigm**


e) verification of proofs;


f) analysis of guidance documents;


g) analysis of functional tests developed and the results provided;


h) independent functional testing;


i) analysis for vulnerabilities (including flaw hypothesis);


j) penetration testing.

## **6.3 The CC evaluation assurance scale**


29 The CC philosophy asserts that greater assurance results from the application
of greater evaluation effort, and that the goal is to apply the minimum effort
required to provide the necessary level of assurance. The increasing level of
effort is based upon:


a) scope -- that is, the effort is greater because a larger portion of the IT
product is included;


b) depth -- that is, the effort is greater because it is deployed to a finer
level of design and implementation detail;


c) rigour -- that is, the effort is greater because it is applied in a more
structured, formal manner.


Page 18 of 247 Version 3.1 April 2017


**Security assurance components**

# **7 Security assurance components**

## **7.1 Security assurance classes, families and components** **structure**


30 The following Sections describe the constructs used in representing the
assurance classes, families, and components.


31 Figure 1 illustrates the SARs defined in this CC Part 3. Note that the most
abstract collection of SARs is referred to as a class. Each class contains
assurance families, which then contain assurance components, which in turn
contain assurance elements. Classes and families are used to provide a
taxonomy for classifying SARs, while components are used to specify SARs
in a PP/ST.


**7.1.1** **Assurance class structure**


32 Figure 1 illustrates the assurance class structure.


7.1.1.1 Class name


33 Each assurance class is assigned a unique name. The name indicates the
topics covered by the assurance class.


34 A unique short form of the assurance class name is also provided. This is the
primary means for referencing the assurance class. The convention adopted
is an “A” followed by two letters related to the class name.


7.1.1.2 Class introduction


35 Each assurance class has an introductory Section that describes the
composition of the class and contains supportive text covering the intent of
the class.


7.1.1.3 Assurance families


36 Each assurance class contains at least one assurance family. The structure of
the assurance families is described in the following Section.


April 2017 Version 3.1 Page 19 of 247


**Security assurance components**


**Figure 1 - Assurance class/family/component/element hierarchy**


**7.1.2** **Assurance family structure**


37 Figure 1 illustrates the assurance family structure.


7.1.2.1 Family name


38 Every assurance family is assigned a unique name. The name provides
descriptive information about the topics covered by the assurance family.
Each assurance family is placed within the assurance class that contains other
families with the same intent.


39 A unique short form of the assurance family name is also provided. This is
the primary means used to reference the assurance family. The convention
adopted is that the short form of the class name is used, followed by an
underscore, and then three letters related to the family name.


Page 20 of 247 Version 3.1 April 2017


**Security assurance components**


7.1.2.2 Objectives


40 The objectives Section of the assurance family presents the intent of the
assurance family.


41 This Section describes the objectives, particularly those related to the CC
assurance paradigm, that the family is intended to address. The description
for the assurance family is kept at a general level. Any specific details
required for objectives are incorporated in the particular assurance
component.


7.1.2.3 Component levelling


42 Each assurance family contains one or more assurance components. This
Section of the assurance family describes the components available and
explains the distinctions between them. Its main purpose is to differentiate
between the assurance components once it has been determined that the
assurance family is a necessary or useful part of the SARs for a PP/ST.


43 Assurance families containing more than one component are levelled and
rationale is provided as to how the components are levelled. This rationale is
in terms of scope, depth, and/or rigour.


7.1.2.4 Application notes


44 The application notes Section of the assurance family, if present, contains
additional information for the assurance family. This information should be
of particular interest to users of the assurance family (e.g. PP and ST authors,
designers of TOEs, evaluators). The presentation is informal and covers, for
example, warnings about limitations of use and areas where specific attention
may be required.


7.1.2.5 Assurance components


45 Each assurance family has at least one assurance component. The structure
of the assurance components is provided in the following Section.


**7.1.3** **Assurance component structure**


46 Figure 2 illustrates the assurance component structure.


April 2017 Version 3.1 Page 21 of 247


**Security assurance components**


**Figure 2 - Assurance component structure**


47 The relationship between components within a family is highlighted using a
bolding convention. Those parts of the requirements that are new, enhanced
or modified beyond the requirements of the previous component within a
hierarchy are bolded.


7.1.3.1 Component identification


48 The component identification Section provides descriptive information
necessary to identify, categorise, register, and reference a component.


49 Every assurance component is assigned a unique name. The name provides
descriptive information about the topics covered by the assurance component.
Each assurance component is placed within the assurance family that shares
its security objective.


50 A unique short form of the assurance component name is also provided. This
is the primary means used to reference the assurance component. The
convention used is that the short form of the family name is used, followed
by a period, and then a numeric character. The numeric characters for the
components within each family are assigned sequentially, starting from 1.


7.1.3.2 Objectives


51 The objectives Section of the assurance component, if present, contains
specific objectives for the particular assurance component. For those
assurance components that have this Section, it presents the specific intent of
the component and a more detailed explanation of the objectives.


7.1.3.3 Application notes


52 The application notes Section of an assurance component, if present,
contains additional information to facilitate the use of the component.


Page 22 of 247 Version 3.1 April 2017


**Security assurance components**


7.1.3.4 Dependencies


53 Dependencies among assurance components arise when a component is not
self-sufficient, and relies upon the presence of another component.


54 Each assurance component provides a complete list of dependencies to other
assurance components. Some components may list “No dependencies”, to
indicate that no dependencies have been identified. The components
depended upon may have dependencies on other components.


55 The dependency list identifies the minimum set of assurance components
which are relied upon. Components which are hierarchical to a component in
the dependency list may also be used to satisfy the dependency.


56 In specific situations the indicated dependencies might not be applicable. The
PP/ST author, by providing rationale for why a given dependency is not
applicable, may elect not to satisfy that dependency.


7.1.3.5 Assurance elements


57 A set of assurance elements is provided for each assurance component. An
assurance element is a security requirement which, if further divided, would
not yield a meaningful evaluation result. It is the smallest security
requirement recognised in the CC.


58 Each assurance element is identified as belonging to one of the three sets of
assurance elements:


a) Developer action elements: the activities that shall be performed by
the developer. This set of actions is further qualified by evidential
material referenced in the following set of elements. Requirements
for developer actions are identified by appending the letter “D” to the
element number.


b) Content and presentation of evidence elements: the evidence required,
what the evidence shall demonstrate, and what information the
evidence shall convey. Requirements for content and presentation of
evidence are identified by appending the letter “C” to the element
number.


c) Evaluator action elements: the activities that shall be performed by
the evaluator. This set of actions explicitly includes confirmation that
the requirements prescribed in the content and presentation of
evidence elements have been met. It also includes explicit actions and
analysis that shall be performed in addition to that already performed
by the developer. Implicit evaluator actions are also to be performed
as a result of developer action elements which are not covered by
content and presentation of evidence requirements. Requirements for
evaluator actions are identified by appending the letter “E” to the
element number.


April 2017 Version 3.1 Page 23 of 247


**Security assurance components**


59 The developer actions and content and presentation of evidence define the
assurance requirements that are used to represent a developer's
responsibilities in demonstrating assurance in the TOE meeting the SFRs of
a PP or ST.


60 The evaluator actions define the evaluator's responsibilities in the two
aspects of evaluation. The first aspect is validation of the PP/ST, in
accordance with the classes APE and ASE in Chapters APE: Protection
Profile evaluation and ASE: Security Target evaluation. The second aspect is
verification of the TOE's conformance with its SFRs and SARs. By
demonstrating that the PP/ST is valid and that the requirements are met by
the TOE, the evaluator can provide a basis for confidence that the TOE in its
operational environment solves the defined security problem.


61 The developer action elements, content and presentation of evidence
elements, and explicit evaluator action elements, identify the evaluator effort
that shall be expended in verifying the security claims made in the ST of the
TOE.


**7.1.4** **Assurance elements**


62 Each element represents a requirement to be met. These statements of
requirements are intended to be clear, concise, and unambiguous. Therefore,
there are no compound sentences: each separable requirement is stated as an
individual element.


**7.1.5** **Component taxonomy**


63 This CC Part 3 contains classes of families and components that are grouped
on the basis of related assurance. At the start of each class is a diagram that
indicates the families in the class and the components in each family.


**Figure 3 - Sample class decomposition diagram**


64 In Figure 3, above, the class as shown contains a single family. The family
contains three components that are linearly hierarchical (i.e. component 2
requires more than component 1, in terms of specific actions, specific
evidence, or rigour of the actions or evidence). The assurance families in this
CC Part 3 are all linearly hierarchical, although linearity is not a mandatory
criterion for assurance families that may be added in the future.

## **7.2 EAL structure**


65 Figure 4 illustrates the EALs and associated structure defined in this CC Part
3. Note that while the figure shows the contents of the assurance components,
it is intended that this information would be included in an EAL by reference
to the actual components defined in the CC.


Page 24 of 247 Version 3.1 April 2017


**Security assurance components**


**7.2.1** **EAL name**



**Figure 4 - EAL structure**



66 Each EAL is assigned a unique name. The name provides descriptive
information about the intent of the EAL.


67 A unique short form of the EAL name is also provided. This is the primary
means used to reference the EAL.


**7.2.2** **Objectives**


68 The objectives Section of the EAL presents the intent of the EAL.


**7.2.3** **Application notes**


69 The application notes Section of the EAL, if present, contains information of
particular interest to users of the EAL (e.g. PP and ST authors, designers of
TOEs targeting this EAL, evaluators). The presentation is informal and
covers, for example, warnings about limitations of use and areas where
specific attention may be required.


April 2017 Version 3.1 Page 25 of 247


**Security assurance components**


**7.2.4** **Assurance components**


70 A set of assurance components have been chosen for each EAL.


71 A higher level of assurance than that provided by a given EAL can be
achieved by:


a) including additional assurance components from other assurance
families; or


b) replacing an assurance component with a higher level assurance
component from the same assurance family.


**7.2.5** **Relationship between assurances and assurance levels**


72 Figure 5 illustrates the relationship between the SARs and the assurance
levels defined in the CC. While assurance components further decompose
into assurance elements, assurance elements cannot be individually
referenced by assurance levels. Note that the arrow in the figure represents a
reference from an EAL to an assurance component within the class where it
is defined.


Page 26 of 247 Version 3.1 April 2017


**Security assurance components**


**Figure 5 - Assurance and assurance level association**

## **7.3 CAP structure**


73 The structure of the CAPs is similar to that of the EALs. The main difference
between these two types of package is the type of TOE they apply to; the
EALs applying to component TOEs and the CAPs applying to composed
TOEs.


74 Figure 6 illustrates the CAPs and associated structure defined in this CC Part
3. Note that while the figure shows the contents of the assurance components,
it is intended that this information would be included in a CAP by reference
to the actual components defined in the CC.


April 2017 Version 3.1 Page 27 of 247


**Security assurance components**


**Figure 6 - CAP structure**


**7.3.1** **CAP name**


75 Each CAP is assigned a unique name. The name provides descriptive
information about the intent of the CAP.


76 A unique short form of the CAP name is also provided. This is the primary
means used to reference the CAP.


**7.3.2** **Objectives**


77 The objectives Section of the CAP presents the intent of the CAP.


**7.3.3** **Application notes**


78 The application notes Section of the CAP, if present, contains information of
particular interest to users of the CAP (e.g. PP and ST authors, integrators of
composed TOEs targeting this CAP, evaluators). The presentation is
informal and covers, for example, warnings about limitations of use and
areas where specific attention may be required.


**7.3.4** **Assurance components**


79 A set of assurance components have been chosen for each CAP.


Page 28 of 247 Version 3.1 April 2017


**Security assurance components**


80 Some dependencies identify the activities performed during the evaluation of
the dependent component on which the composed TOE activity relies. Where
it is not explicitly identified that the dependency is on a dependent
component activity, the dependency is to another evaluation activity of the
composed TOE.


81 A higher level of assurance than that provided by a given CAP can be
achieved by:


a) including additional assurance components from other assurance
families; or


b) replacing an assurance component with a higher level assurance
component from the same assurance family.


82 The ACO: Composition components included in the CAP assurance
packages should not be used as augmentations for component TOE
evaluations, as this would provide no meaningful assurance for the
component.


**7.3.5** **Relationship between assurances and assurance levels**


83 Figure 7 illustrates the relationship between the SARs and the composed
assurance packages defined in the CC. While assurance components further
decompose into assurance elements, assurance elements cannot be
individually referenced by assurance packages. Note that the arrow in the
figure represents a reference from a CAP to an assurance component within
the class where it is defined.


April 2017 Version 3.1 Page 29 of 247


**Security assurance components**


**Figure 7 - Assurance and composed assurance package association**


Page 30 of 247 Version 3.1 April 2017


**Evaluation assurance levels**

# **8 Evaluation assurance levels**


84 The Evaluation Assurance Levels (EALs) provide an increasing scale that
balances the level of assurance obtained with the cost and feasibility of
acquiring that degree of assurance. The CC approach identifies the separate
concepts of assurance in a TOE at the end of the evaluation, and of
maintenance of that assurance during the operational use of the TOE.


85 It is important to note that not all families and components from CC Part 3
are included in the EALs. This is not to say that these do not provide
meaningful and desirable assurances. Instead, it is expected that these
families and components will be considered for augmentation of an EAL in
those PPs and STs for which they provide utility.

## **8.1 Evaluation assurance level (EAL) overview**


86 Table 1 represents a summary of the EALs. The columns represent a
hierarchically ordered set of EALs, while the rows represent assurance
families. Each number in the resulting matrix identifies a specific assurance
component where applicable.


87 As outlined in the next Section, seven hierarchically ordered evaluation
assurance levels are defined in the CC for the rating of a TOE's assurance.
They are hierarchically ordered inasmuch as each EAL represents more
assurance than all lower EALs. The increase in assurance from EAL to EAL
is accomplished by substitution of a hierarchically higher assurance
component from the same assurance family (i.e. increasing rigour, scope,
and/or depth) and from the addition of assurance components from other
assurance families (i.e. adding new requirements).


88 These EALs consist of an appropriate combination of assurance components
as described in Chapter 7 of this CC Part 3. More precisely, each EAL
includes no more than one component of each assurance family and all
assurance dependencies of every component are addressed.


89 While the EALs are defined in the CC, it is possible to represent other
combinations of assurance. Specifically, the notion of “augmentation” allows
the addition of assurance components (from assurance families not already
included in the EAL) or the substitution of assurance components (with
another hierarchically higher assurance component in the same assurance
family) to an EAL. Of the assurance constructs defined in the CC, only EALs
may be augmented. The notion of an “EAL minus a constituent assurance
component” is not recognised by the standard as a valid claim. Augmentation
carries with it the obligation on the part of the claimant to justify the utility
and added value of the added assurance component to the EAL. An EAL
may also be augmented with extended assurance requirements.


April 2017 Version 3.1 Page 31 of 247


**Evaluation assurance levels**
















|Assurance<br>class|Assurance<br>Family|Assurance Components by Evaluation<br>Assurance Level|Col4|Col5|Col6|Col7|Col8|Col9|
|---|---|---|---|---|---|---|---|---|
|Assurance<br>class|Assurance<br>Family|EAL1|EAL2|EAL3|EAL4|EAL5|EAL6|EAL7|
|Development|ADV_ARC||**1**|1|1|1|1|1|
|Development|ADV_FSP|**1**|**2**|**3**|**4**|**5**|5|**6**|
|Development|ADV_IMP||||**1**|1|**2**|2|
|Development|ADV_INT|||||**2**|**3**|3|
|Development|ADV_SPM||||||**1**|1|
|Development|ADV_TDS||**1**|**2**|**3**|**4**|**5**|**6**|
|Guidance<br>documents|AGD_OPE|**1**|1|1|1|1|1|1|
|Guidance<br>documents|AGD_PRE|**1**|1|1|1|1|1|1|
|Life-cycle<br>support|ALC_CMC|**1**|**2**|**3**|**4**|4|**5**|5|
|Life-cycle<br>support|ALC_CMS|**1**|**2**|**3**|**4**|**5**|5|5|
|Life-cycle<br>support|ALC_DEL||**1**|1|1|1|1|1|
|Life-cycle<br>support|ALC_DVS|||**1**|1|1|**2**|2|
|Life-cycle<br>support|ALC_FLR||||||||
|Life-cycle<br>support|ALC_LCD|||**1**|1|1|1|**2**|
|Life-cycle<br>support|ALC_TAT||||**1**|**2**|**3**|3|
|Security<br>Target<br>evaluation|ASE_CCL|**1**|1|1|1|1|1|1|
|Security<br>Target<br>evaluation|ASE_ECD|**1**|1|1|1|1|1|1|
|Security<br>Target<br>evaluation|ASE_INT|**1**|1|1|1|1|1|1|
|Security<br>Target<br>evaluation|ASE_OBJ|**1**|**2**|2|2|2|2|2|
|Security<br>Target<br>evaluation|ASE_REQ|**1**|**2**|2|2|2|2|2|
|Security<br>Target<br>evaluation|ASE_SPD||**1**|1|1|1|1|1|
|Security<br>Target<br>evaluation|ASE_TSS|**1**|1|1|1|1|1|1|
|Tests|ATE_COV||**1**|**2**|2|2|**3**|3|
|Tests|ATE_DPT|||**1**|1|**3**|3|**4**|
|Tests|ATE_FUN||**1**|1|1|1|**2**|2|
|Tests|ATE_IND|**1**|**2**|2|2|2|2|**3**|
|Vulnerability<br>assessment|AVA_VAN|**1**|**2**|2|**3**|**4**|**5**|5|



**Table 1 - Evaluation assurance level summary**

## **8.2 Evaluation assurance level details**


90 The following Sections provide definitions of the EALs, highlighting
differences between the specific requirements and the prose characterisations
of those requirements using bold type.


Page 32 of 247 Version 3.1 April 2017


**Evaluation assurance levels**

## **8.3 Evaluation assurance level 1 (EAL1) - functionally** **tested**


Objectives


91 EAL1 is applicable where some confidence in correct operation is required,
but the threats to security are not viewed as serious. It will be of value where
independent assurance is required to support the contention that due care has
been exercised with respect to the protection of personal or similar
information.


92 EAL1 requires only a limited security target. It is sufficient to simply state
the SFRs that the TOE must meet, rather than deriving them from threats,
OSPs and assumptions through security objectives.


93 EAL1 provides an evaluation of the TOE as made available to the customer,
including independent testing against a specification, and an examination of
the guidance documentation provided. It is intended that an EAL1 evaluation
could be successfully conducted without assistance from the developer of the
TOE, and for minimal outlay.


94 An evaluation at this level should provide evidence that the TOE functions in
a manner consistent with its documentation.


Assurance components


95 **EAL1 provides a basic level of assurance by a limited security target and**
**an analysis of the SFRs in that ST using a functional and interface**
**specification and guidance documentation, to understand the security**
**behaviour.**


96 **The analysis is supported by a search for potential vulnerabilities in the**
**public domain and independent testing (functional and penetration) of**
**the TSF.**


97 **EAL1 also provides assurance through unique identification of the TOE**
**and of the relevant evaluation documents.**


98 **This EAL provides a meaningful increase in assurance over unevaluated**
**IT.**


April 2017 Version 3.1 Page 33 of 247


**Evaluation assurance levels**

|Assurance Class|Assurance components|
|---|---|
|ADV: Development|ADV_FSP.1 Basic functional specification|
|AGD: Guidance documents|AGD_OPE.1 Operational user guidance|
|AGD: Guidance documents|AGD_PRE.1 Preparative procedures|
|ALC: Life-cycle support|ALC_CMC.1 Labelling of the TOE|
|ALC: Life-cycle support|ALC_CMS.1 TOE CM coverage|
|ASE: Security Target evaluation|ASE_CCL.1 Conformance claims|
|ASE: Security Target evaluation|ASE_ECD.1 Extended components definition|
|ASE: Security Target evaluation|ASE_INT.1 ST introduction|
|ASE: Security Target evaluation|ASE_OBJ.1 Security objectives for the<br>operational environment|
|ASE: Security Target evaluation|ASE_REQ.1 Stated security requirements|
|ASE: Security Target evaluation|ASE_TSS.1 TOE summary specification|
|ATE: Tests|ATE_IND.1 Independent testing - conformance|
|AVA: Vulnerability assessment|AVA_VAN.1 Vulnerability survey|



**Table 2 - EAL1**


Page 34 of 247 Version 3.1 April 2017


**Evaluation assurance levels**

## **8.4 Evaluation assurance level 2 (EAL2) - structurally** **tested**


Objectives


99 EAL2 requires the co-operation of the developer in terms of the delivery of
design information and test results, but should not demand more effort on the
part of the developer than is consistent with good commercial practise. As
such it should not require a substantially increased investment of cost or time.


100 EAL2 is therefore applicable in those circumstances where developers or
users require a low to moderate level of independently assured security in the
absence of ready availability of the complete development record. Such a
situation may arise when securing legacy systems, or where access to the
developer may be limited.


Assurance components


101 **EAL2** provides assurance by a **full** security target and an analysis of the
SFRs in that ST, using a functional and interface specification, guidance
documentation **and** **a** **basic** **description** **of** **the** **architecture** **of** **the** **TOE,** to
understand the security behaviour.


102 The analysis is supported by independent testing of the TSF, **evidence** **of**
**developer** **testing** **based** **on** **the** **functional** **specification,** **selective**
**independent** **confirmation** **of** **the** **developer** **test** **results,** **and** **a**
**vulnerability** **analysis** **(based** **upon** **the** **functional** **specification,** **TOE**
**design,** **security** **architecture** **description** **and** **guidance** **evidence**
**provided)** **demonstrating** **resistance** **to** **penetration** **attackers** **with** **a** **basic**
**attack** **potential.**


103 **EAL2** also provides assurance through **use** of **a** **configuration** **management**
**system** and **evidence** of **secure** **delivery** **procedures.**


104 This EAL **represents** a meaningful increase in assurance **from** **EAL1** **by**
**requiring** **developer** **testing,** **a** **vulnerability** **analysis** **(in** **addition** **to** **the**
**search** **of** **the** **public** **domain),** **and** **independent** **testing** **based** **upon** **more**
**detailed** **TOE** **specifications.**


April 2017 Version 3.1 Page 35 of 247


**Evaluation assurance levels**













|Assurance Class|Assurance components|
|---|---|
|ADV: Development|ADV_ARC.1 Security architecture description|
|ADV: Development|ADV_FSP.2 Security-enforcing functional<br>specification|
|ADV: Development|ADV_TDS.1 Basic design|
|AGD: Guidance documents|AGD_OPE.1 Operational user guidance|
|AGD: Guidance documents|AGD_PRE.1 Preparative procedures|
|ALC: Life-cycle support|ALC_CMC.2 Use of a CM system|
|ALC: Life-cycle support|ALC_CMS.2 Parts of the TOE CM coverage|
|ALC: Life-cycle support|ALC_DEL.1 Delivery procedures|
|ASE: Security Target evaluation|ASE_CCL.1 Conformance claims|
|ASE: Security Target evaluation|ASE_ECD.1 Extended components definition|
|ASE: Security Target evaluation|ASE_INT.1 ST introduction|
|ASE: Security Target evaluation|ASE_OBJ.2 Security objectives|
|ASE: Security Target evaluation|ASE_REQ.2 Derived security requirements|
|ASE: Security Target evaluation|ASE_SPD.1 Security problem definition|
|ASE: Security Target evaluation|ASE_TSS.1 TOE summary specification|
|ATE: Tests|ATE_COV.1 Evidence of coverage|
|ATE: Tests|ATE_FUN.1 Functional testing|
|ATE: Tests|ATE_IND.2 Independent testing - sample|
|AVA: Vulnerability assessment|AVA_VAN.2 Vulnerability analysis|


**Table 3 - EAL2**



Page 36 of 247 Version 3.1 April 2017


**Evaluation assurance levels**

## **8.5 Evaluation assurance level 3 (EAL3) - methodically** **tested and checked**


Objectives


105 EAL3 permits a conscientious developer to gain maximum assurance from
positive security engineering at the design stage without substantial alteration
of existing sound development practises.


106 EAL3 is applicable in those circumstances where developers or users require
a moderate level of independently assured security, and require a thorough
investigation of the TOE and its development without substantial reengineering.


Assurance components


107 **EAL3** provides assurance by a full security target and an analysis of the
SFRs in that ST, using a functional and interface specification, guidance
documentation, and **an** **architectural** description of the **design** of the TOE,
to understand the security behaviour.


108 The analysis is supported by independent testing of the TSF, evidence of
developer testing based on the functional specification **and** **TOE** **design,**
selective independent confirmation of the developer test results, and a
vulnerability analysis (based upon the functional specification, TOE design,
security architecture description and guidance evidence provided)
demonstrating resistance to penetration attackers with a basic attack potential.


109 **EAL3** also provides assurance through **the** use of **development**
**environment** **controls,** **TOE** configuration management, and evidence of
secure delivery procedures.


110 This EAL represents a meaningful increase in assurance from **EAL2** by
requiring **more** **complete** testing **coverage** of the **security** **functionality** and
**mechanisms** **and/or** **procedures** **that** **provide** **some** **confidence** **that** **the**
TOE **will** **not** **be** **tampered** **with** **during** **development.**


April 2017 Version 3.1 Page 37 of 247


**Evaluation assurance levels**













|Assurance Class|Assurance components|
|---|---|
|ADV: Development|ADV_ARC.1 Security architecture description|
|ADV: Development|ADV_FSP.3 Functional specification with<br>complete summary|
|ADV: Development|ADV_TDS.2 Architectural design|
|AGD: Guidance documents|AGD_OPE.1 Operational user guidance|
|AGD: Guidance documents|AGD_PRE.1 Preparative procedures|
|ALC: Life-cycle support|ALC_CMC.3 Authorisation controls|
|ALC: Life-cycle support|ALC_CMS.3 Implementation representation<br>CM coverage|
|ALC: Life-cycle support|ALC_DEL.1 Delivery procedures|
|ALC: Life-cycle support|ALC_DVS.1 Identification of security<br>measures|
|ALC: Life-cycle support|ALC_LCD.1 Developer defined life-cycle<br>model|
|ASE: Security Target evaluation|ASE_CCL.1 Conformance claims|
|ASE: Security Target evaluation|ASE_ECD.1 Extended components definition|
|ASE: Security Target evaluation|ASE_INT.1 ST introduction|
|ASE: Security Target evaluation|ASE_OBJ.2 Security objectives|
|ASE: Security Target evaluation|ASE_REQ.2 Derived security requirements|
|ASE: Security Target evaluation|ASE_SPD.1 Security problem definition|
|ASE: Security Target evaluation|ASE_TSS.1 TOE summary specification|
|ATE: Tests|ATE_COV.2 Analysis of coverage|
|ATE: Tests|ATE_DPT.1 Testing: basic design|
|ATE: Tests|ATE_FUN.1 Functional testing|
|ATE: Tests|ATE_IND.2 Independent testing - sample|
|AVA: Vulnerability assessment|AVA_VAN.2 Vulnerability analysis|


**Table 4 - EAL3**



Page 38 of 247 Version 3.1 April 2017


**Evaluation assurance levels**

## **8.6 Evaluation assurance level 4 (EAL4) - methodically** **designed, tested, and reviewed**


Objectives


111 EAL4 permits a developer to gain maximum assurance from positive
security engineering based on good commercial development practises which,
though rigorous, do not require substantial specialist knowledge, skills, and
other resources. EAL4 is the highest level at which it is likely to be
economically feasible to retrofit to an existing product line.


112 EAL4 is therefore applicable in those circumstances where developers or
users require a moderate to high level of independently assured security in
conventional commodity TOEs and are prepared to incur additional securityspecific engineering costs.


Assurance components


113 **EAL4** provides assurance by a full security target and an analysis of the
SFRs in that ST, using a functional and **complete** interface specification,
guidance documentation, **a** description of the **basic** **modular** design of the
TOE, **and** **a** **subset** **of** **the** **implementation,** to understand the security
behaviour.


114 The analysis is supported by independent testing of the TSF, evidence of
developer testing based on the functional specification and TOE design,
selective independent confirmation of the developer test results, and a
vulnerability analysis (based upon the functional specification, TOE design,
**implementation** **representation,** security architecture description and
guidance evidence provided) demonstrating resistance to penetration
attackers with **an** **Enhanced-Basic** attack potential.


115 **EAL4** also provides assurance through the use of development environment
controls **and** **additional** TOE configuration management **including**
**automation,** and evidence of secure delivery procedures.


116 This EAL represents a meaningful increase in assurance from **EAL3** by
requiring more **design** **description,** the **implementation** **representation** **for**
**the** **entire** **TSF,** and **improved** mechanisms and/or procedures that provide
confidence that the TOE will not be tampered with during development.


April 2017 Version 3.1 Page 39 of 247


**Evaluation assurance levels**













|Assurance Class|Assurance components|
|---|---|
|ADV: Development|ADV_ARC.1 Security architecture description|
|ADV: Development|ADV_FSP.4 Complete functional specification|
|ADV: Development|ADV_IMP.1 Implementation representation of<br>the TSF|
|ADV: Development|ADV_TDS.3 Basic modular design|
|AGD: Guidance documents|AGD_OPE.1 Operational user guidance|
|AGD: Guidance documents|AGD_PRE.1 Preparative procedures|
|ALC: Life-cycle support|ALC_CMC.4 Production support, acceptance<br>procedures and automation|
|ALC: Life-cycle support|ALC_CMS.4 Problem tracking CM coverage|
|ALC: Life-cycle support|ALC_DEL.1 Delivery procedures|
|ALC: Life-cycle support|ALC_DVS.1 Identification of security<br>measures|
|ALC: Life-cycle support|ALC_LCD.1 Developer defined life-cycle<br>model|
|ALC: Life-cycle support|ALC_TAT.1 Well-defined development tools|
|ASE: Security Target evaluation|ASE_CCL.1 Conformance claims|
|ASE: Security Target evaluation|ASE_ECD.1 Extended components definition|
|ASE: Security Target evaluation|ASE_INT.1 ST introduction|
|ASE: Security Target evaluation|ASE_OBJ.2 Security objectives|
|ASE: Security Target evaluation|ASE_REQ.2 Derived security requirements|
|ASE: Security Target evaluation|ASE_SPD.1 Security problem definition|
|ASE: Security Target evaluation|ASE_TSS.1 TOE summary specification|
|ATE: Tests|ATE_COV.2 Analysis of coverage|
|ATE: Tests|ATE_DPT.1 Testing: basic design|
|ATE: Tests|ATE_FUN.1 Functional testing|
|ATE: Tests|ATE_IND.2 Independent testing - sample|
|AVA: Vulnerability assessment|AVA_VAN.3 Focused vulnerability analysis|


**Table 5 - EAL4**



Page 40 of 247 Version 3.1 April 2017


**Evaluation assurance levels**

## **8.7 Evaluation assurance level 5 (EAL5) - semiformally** **designed and tested**


Objectives


117 EAL5 permits a developer to gain maximum assurance from security
engineering based upon rigorous commercial development practises
supported by moderate application of specialist security engineering
techniques. Such a TOE will probably be designed and developed with the
intent of achieving EAL5 assurance. It is likely that the additional costs
attributable to the EAL5 requirements, relative to rigorous development
without the application of specialised techniques, will not be large.


118 EAL5 is therefore applicable in those circumstances where developers or
users require a high level of independently assured security in a planned
development and require a rigorous development approach without incurring
unreasonable costs attributable to specialist security engineering techniques.


Assurance components


119 **EAL5** provides assurance by a full security target and an analysis of the
SFRs in that ST, using a functional and complete interface specification,
guidance documentation, a description of the design of the TOE, and the
implementation, to understand the security behaviour. **A** **modular** **TSF**
**design** **is** **also** **required.**


120 The analysis is supported by independent testing of the TSF, evidence of
developer testing based on the functional specification, TOE design,
selective independent confirmation of the developer test results, and **an**
**independent** vulnerability analysis demonstrating resistance to penetration
attackers with **a** **moderate** attack potential.


121 **EAL5** also provides assurance through the use of **a** development
environment controls, and **comprehensive** TOE configuration management
including automation, and evidence of secure delivery procedures.


122 This EAL represents a meaningful increase in assurance from **EAL4** by
requiring **semiformal** **design** **descriptions,** **a** more **structured** **(and** **hence**
**analysable)** **architecture,** and improved mechanisms and/or procedures that
provide confidence that the TOE will not be tampered with during
development.


April 2017 Version 3.1 Page 41 of 247


**Evaluation assurance levels**













|Assurance Class|Assurance components|
|---|---|
|ADV: Development|ADV_ARC.1 Security architecture description|
|ADV: Development|ADV_FSP.5 Complete semi-formal functional<br>specification with additional error information|
|ADV: Development|ADV_IMP.1 Implementation representation of<br>the TSF|
|ADV: Development|ADV_INT.2 Well-structured internals|
|ADV: Development|ADV_TDS.4 Semiformal modular design|
|AGD: Guidance documents|AGD_OPE.1 Operational user guidance|
|AGD: Guidance documents|AGD_PRE.1 Preparative procedures|
|ALC: Life-cycle support|ALC_CMC.4 Production support, acceptance<br>procedures and automation|
|ALC: Life-cycle support|ALC_CMS.5 Development tools CM coverage|
|ALC: Life-cycle support|ALC_DEL.1 Delivery procedures|
|ALC: Life-cycle support|ALC_DVS.1 Identification of security<br>measures|
|ALC: Life-cycle support|ALC_LCD.1 Developer defined life-cycle<br>model|
|ALC: Life-cycle support|ALC_TAT.2 Compliance with implementation<br>standards|
|ASE: Security Target evaluation|ASE_CCL.1 Conformance claims|
|ASE: Security Target evaluation|ASE_ECD.1 Extended components definition|
|ASE: Security Target evaluation|ASE_INT.1 ST introduction|
|ASE: Security Target evaluation|ASE_OBJ.2 Security objectives|
|ASE: Security Target evaluation|ASE_REQ.2 Derived security requirements|
|ASE: Security Target evaluation|ASE_SPD.1 Security problem definition|
|ASE: Security Target evaluation|ASE_TSS.1 TOE summary specification|
|ATE: Tests|ATE_COV.2 Analysis of coverage|
|ATE: Tests|ATE_DPT.3 Testing: modular design|
|ATE: Tests|ATE_FUN.1 Functional testing|
|ATE: Tests|ATE_IND.2 Independent testing - sample|
|AVA: Vulnerability assessment|AVA_VAN.4 Methodical vulnerability analysis|


**Table 6 - EAL5**



Page 42 of 247 Version 3.1 April 2017


**Evaluation assurance levels**

## **8.8 Evaluation assurance level 6 (EAL6) - semiformally** **verified design and tested**


Objectives


123 EAL6 permits developers to gain high assurance from application of security
engineering techniques to a rigorous development environment in order to
produce a premium TOE for protecting high value assets against significant
risks.


124 EAL6 is therefore applicable to the development of security TOEs for
application in high risk situations where the value of the protected assets
justifies the additional costs.


Assurance components


125 **EAL6** provides assurance by a full security target and an analysis of the
SFRs in that ST, using a functional and complete interface specification,
guidance documentation, the design of the TOE, and the implementation to
understand the security behaviour. **Assurance** **is** **additionally** **gained**
**through** **a** **formal** **model** **of** **select** **TOE** **security** **policies** **and** **a** **semiformal**
**presentation** **of** **the** **functional** **specification** **and** **TOE** **design.** A modular,
**layered** **and** **simple** TSF design is also required.


126 The analysis is supported by independent testing of the TSF, evidence of
developer testing based on the functional specification, TOE design,
selective independent confirmation of the developer test results, and an
independent vulnerability analysis demonstrating resistance to penetration
attackers with a **high** attack potential.


127 **EAL6** also provides assurance through the use of a **structured** development
**process,** **development** environment controls, and comprehensive TOE
configuration management including **complete** automation, and evidence of
secure delivery procedures.


128 This EAL represents a meaningful increase in assurance from **EAL5** by
requiring **more** **comprehensive** **analysis,** a **structured** **representation** **of**
**the** **implementation,** more **architectural** **structure** **(e.g.** **layering),** **more**
**comprehensive** **independent** **vulnerability** **analysis,** and improved
**configuration** **management** **and** development **environment** **controls.**


April 2017 Version 3.1 Page 43 of 247


**Evaluation assurance levels**













|Assurance Class|Assurance components|
|---|---|
|ADV: Development|ADV_ARC.1 Security architecture description|
|ADV: Development|ADV_FSP.5 Complete semi-formal functional<br>specification with additional error information|
|ADV: Development|ADV_IMP.2 Complete mapping of the<br>implementation representation of the TSF|
|ADV: Development|ADV_INT.3 Minimally complex internals|
|ADV: Development|ADV_SPM.1 Formal TOE security policy<br>model|
|ADV: Development|ADV_TDS.5 Complete semiformal modular<br>design|
|AGD: Guidance documents|AGD_OPE.1 Operational user guidance|
|AGD: Guidance documents|AGD_PRE.1 Preparative procedures|
|ALC: Life-cycle support|ALC_CMC.5 Advanced support|
|ALC: Life-cycle support|ALC_CMS.5 Development tools CM coverage|
|ALC: Life-cycle support|ALC_DEL.1 Delivery procedures|
|ALC: Life-cycle support|ALC_DVS.2 Sufficiency of security measures|
|ALC: Life-cycle support|ALC_LCD.1 Developer defined life-cycle<br>model|
|ALC: Life-cycle support|ALC_TAT.3 Compliance with implementation<br>standards - all parts|
|ASE: Security Target evaluation|ASE_CCL.1 Conformance claims|
|ASE: Security Target evaluation|ASE_ECD.1 Extended components definition|
|ASE: Security Target evaluation|ASE_INT.1 ST introduction|
|ASE: Security Target evaluation|ASE_OBJ.2 Security objectives|
|ASE: Security Target evaluation|ASE_REQ.2 Derived security requirements|
|ASE: Security Target evaluation|ASE_SPD.1 Security problem definition|
|ASE: Security Target evaluation|ASE_TSS.1 TOE summary specification|
|ATE: Tests|ATE_COV.3 Rigorous analysis of coverage|
|ATE: Tests|ATE_DPT.3 Testing: modular design|
|ATE: Tests|ATE_FUN.2 Ordered functional testing|
|ATE: Tests|ATE_IND.2 Independent testing - sample|
|AVA: Vulnerability assessment|AVA_VAN.5 Advanced methodical<br>vulnerability analysis|


**Table 7 - EAL6**


Page 44 of 247 Version 3.1 April 2017


**Evaluation assurance levels**

## **8.9 Evaluation assurance level 7 (EAL7) - formally verified** **design and tested**


Objectives


129 EAL7 is applicable to the development of security TOEs for application in
extremely high risk situations and/or where the high value of the assets
justifies the higher costs. Practical application of EAL7 is currently limited
to TOEs with tightly focused security functionality that is amenable to
extensive formal analysis.


Assurance components


130 **EAL7** provides assurance by a full security target and an analysis of the
SFRs in that ST, using a functional and complete interface specification,
guidance documentation, the design of the TOE, and **a** **structured**
**presentation** **of** the implementation to understand the security behaviour.
Assurance is additionally gained through a formal model of select TOE
security policies and a semiformal presentation of the functional
specification and TOE design. A modular, layered and simple TSF design is
also required.


131 The analysis is supported by independent testing of the TSF, evidence of
developer testing based on the functional specification, TOE design **and**
**implementation** **representation,** **complete** independent confirmation of the
developer test results, and an independent vulnerability analysis
demonstrating resistance to penetration attackers with a high attack potential.


132 **EAL7** also provides assurance through the use of a structured development
process, development environment controls, and comprehensive TOE
configuration management including complete automation, and evidence of
secure delivery procedures.


133 This EAL represents a meaningful increase in assurance from **EAL6** by
requiring more comprehensive analysis **using** **formal** **representations** and
**formal** **correspondence,** and **comprehensive** **testing.**


April 2017 Version 3.1 Page 45 of 247


**Evaluation assurance levels**













|Assurance Class|Assurance components|
|---|---|
|ADV: Development|ADV_ARC.1 Security architecture description|
|ADV: Development|ADV_FSP.6 Complete semi-formal functional<br>specification with additional formal<br>specification|
|ADV: Development|ADV_IMP.2 Complete mapping of the<br>implementation representation of the TSF|
|ADV: Development|ADV_INT.3 Minimally complex internals|
|ADV: Development|ADV_SPM.1 Formal TOE security policy<br>model|
|ADV: Development|ADV_TDS.6 Complete semiformal modular<br>design with formal high-level design<br>presentation|
|AGD: Guidance documents|AGD_OPE.1 Operational user guidance|
|AGD: Guidance documents|AGD_PRE.1 Preparative procedures|
|ALC: Life-cycle support|ALC_CMC.5 Advanced support|
|ALC: Life-cycle support|ALC_CMS.5 Development tools CM coverage|
|ALC: Life-cycle support|ALC_DEL.1 Delivery procedures|
|ALC: Life-cycle support|ALC_DVS.2 Sufficiency of security measures|
|ALC: Life-cycle support|ALC_LCD.2 Measurable life-cycle model|
|ALC: Life-cycle support|ALC_TAT.3 Compliance with implementation<br>standards - all parts|
|ASE: Security Target evaluation|ASE_CCL.1 Conformance claims|
|ASE: Security Target evaluation|ASE_ECD.1 Extended components definition|
|ASE: Security Target evaluation|ASE_INT.1 ST introduction|
|ASE: Security Target evaluation|ASE_OBJ.2 Security objectives|
|ASE: Security Target evaluation|ASE_REQ.2 Derived security requirements|
|ASE: Security Target evaluation|ASE_SPD.1 Security problem definition|
|ASE: Security Target evaluation|ASE_TSS.1 TOE summary specification|
|ATE: Tests|ATE_COV.3 Rigorous analysis of coverage|
|ATE: Tests|ATE_DPT.4 Testing: implementation<br>representation|
|ATE: Tests|ATE_FUN.2 Ordered functional testing|
|ATE: Tests|ATE_IND.3 Independent testing - complete|
|AVA: Vulnerability assessment|AVA_VAN.5 Advanced methodical<br>vulnerability analysis|


**Table 8 - EAL7**


Page 46 of 247 Version 3.1 April 2017


**Composed assurance packages**

# **9 Composed assurance packages**


134 The Composed Assurance Packages (CAPs) provide an increasing scale that
balances the level of assurance obtained with the cost and feasibility of
acquiring that degree of assurance for composed TOEs.


135 It is important to note that there are only a small number of families and
components from CC Part 3 included in the CAPs. This is due to their nature
of building upon evaluation results of previously evaluated entities (base
components and dependent components), and is not to say that these do not
provide meaningful and desirable assurances.

## **9.1 Composed assurance package (CAP) overview**


136 CAPs are to be applied to composed TOEs, which are comprised of
components that have been (are going through) component TOE evaluation
(see Annex B). The individual components will have been certified to an
EAL or another assurance package specified in the ST. It is expected that a
basic level of assurance in a composed TOE will be gained through
application of EAL1, which can be achieved with information about the
components that is generally available in the public domain. (EAL1 can be
applied as specified within to both component and composed TOEs.) CAPs
provide an alternative approach to obtaining higher levels of assurance for a
composed TOE than application of the EALs above EAL1.


137 While a dependent component can be evaluated using a previously evaluated
and certified base component to satisfy the IT platform requirements in the
environment, this does not provide any formal assurance of the interactions
between the components or the possible introduction of vulnerabilities
resulting from the composition. Composed assurance packages consider
these interactions and, at higher levels of assurance, ensure that the interface
between the components has itself been the subject of testing. A vulnerability
analysis of the composed TOE is also performed to consider the possible
introduction of vulnerabilities as a result of composing the components.


138 Table 9 represents a summary of the CAPs. The columns represent a
hierarchically ordered set of CAPs, while the rows represent assurance
families. Each number in the resulting matrix identifies a specific assurance
component where applicable.


139 As outlined in the next Section, three hierarchically ordered composed
assurance packages are defined in the CC for the rating of a composed TOE's
assurance. They are hierarchically ordered inasmuch as each CAP represents
more assurance than all lower CAPs. The increase in assurance from CAP to
CAP is accomplished by substitution of a hierarchically higher assurance
component from the same assurance family (i.e. increasing rigour, scope,
and/or depth) and from the addition of assurance components from other
assurance families (i.e. adding new requirements). These increases result in


April 2017 Version 3.1 Page 47 of 247


**Composed assurance packages**


greater analysis of the composition to identify the impact on the evaluation
results gained for the individual component TOEs.


140 These CAPs consist of an appropriate combination of assurance components
as described in Chapter 7 of this CC Part 3. More precisely, each CAP
includes no more than one component of each assurance family and all
assurance dependencies of every component are addressed.


141 The CAPs only consider resistance against an attacker with an attack
potential up to Enhanced-Basic. This is due to the level of design information
that can be provided through the ACO_DEV, limiting some of the factors
associated with attack potential (knowledge of the composed TOE) and
subsequently affecting the rigour of vulnerability analysis that can be
performed by the evaluator. Therefore, the level of assurance in the
composed TOE is limited, although the assurance in the individual
components within the composed TOE may be much higher.














|Assurance class|Assurance<br>Family|Assurance Components by<br>Composition Assurance<br>Package|Col4|Col5|
|---|---|---|---|---|
|Assurance class|Assurance<br>Family|CAP-A|CAP-B|CAP-C|
|Composition|ACO_COR|**1**|1|1|
|Composition|ACO_CTT|**1**|**2**|2|
|Composition|ACO_DEV|**1**|**2**|**3**|
|Composition|ACO_REL|**1**|1|**2**|
|Composition|ACO_VUL|**1**|**2**|**3**|
|Guidance<br>documents|AGD_OPE|**1**|1|1|
|Guidance<br>documents|AGD_PRE|**1**|1|1|
|Life-cycle<br>support|ALC_CMC|**1**|1|1|
|Life-cycle<br>support|ALC_CMS|**2**|2|2|
|Life-cycle<br>support|ALC_DEL||||
|Life-cycle<br>support|ALC_DVS||||
|Life-cycle<br>support|ALC_FLR||||
|Life-cycle<br>support|ALC_LCD||||
|Life-cycle<br>support|ALC_TAT||||
|Security Target<br>evaluation|ASE_CCL|**1**|1|1|
|Security Target<br>evaluation|ASE_ECD|**1**|1|1|
|Security Target<br>evaluation|ASE_INT|**1**|1|1|
|Security Target<br>evaluation|ASE_OBJ|**1**|**2**|2|
|Security Target<br>evaluation|ASE_REQ|**1**|**2**|2|
|Security Target<br>evaluation|ASE_SPD||**1**|1|
|Security Target<br>evaluation|ASE_TSS|**1**|1|1|



**Table 9 - Composition assurance level summary**


Page 48 of 247 Version 3.1 April 2017


**Composed assurance packages**

## **9.2 Composed assurance package details**


142 The following Sections provide definitions of the CAPs, highlighting
differences between the specific requirements and the prose characterisations
of those requirements using bold type.


April 2017 Version 3.1 Page 49 of 247


**Composed assurance packages**

## **9.3 Composition assurance level A (CAP-A) - Structurally** **composed**


Objectives


143 CAP-A is applicable when a composed TOE is integrated and confidence in
the correct security operation of the resulting composite is required. This
requires the cooperation of the developer of the dependent component in
terms of delivery of design information and test results from the dependent
component certification, without requiring the involvement of the base
component developer.


144 CAP-A is therefore applicable in those circumstances where developers or
users require a low to moderate level of independently assured security in the
absence of ready availability of the complete development record.


Assurance components


145 **CAP-A provides assurance by analysis of a security target for the**
**composed TOE. The SFRs in the composed TOE ST are analysed using**
**the outputs from the evaluations of the component TOEs (e.g. ST,**
**guidance documentation) and a specification for the interfaces between**
**the component TOEs in the composed TOE to understand the security**
**behaviour.**


146 **The analysis is supported by independent testing of the interfaces of the**
**base component that are relied upon by the dependent component, as**
**described in the reliance information, evidence of developer testing**
**based on the reliance information, development information and**
**composition rationale, and selective independent confirmation of the**
**developer test results. The analysis is also supported by a vulnerability**
**review of the composed TOE by the evaluator.**


147 **CAP-A also provides assurance through unique identification of the**
**composed TOE (i.e. IT TOE and guidance documentation).**


Page 50 of 247 Version 3.1 April 2017


**Composed assurance packages**












|Assurance Class|Assurance components|
|---|---|
|ACO: Composition|ACO_COR.1 Composition rationale|
|ACO: Composition|ACO_CTT.1 Interface testing|
|ACO: Composition|ACO_DEV.1 Functional Description|
|ACO: Composition|ACO_REL.1 Basic reliance information|
|ACO: Composition|ACO_VUL.1 Composition vulnerability review|
|AGD: Guidance documents|AGD_OPE.1 Operational user guidance|
|AGD: Guidance documents|AGD_PRE.1 Preparative procedures|
|ALC: Life-cycle support|ALC_CMC.1 Labelling of the TOE|
|ALC: Life-cycle support|ALC_CMS.2 Parts of the TOE CM coverage|
|ASE: Security Target evaluation|ASE_CCL.1 Conformance claims|
|ASE: Security Target evaluation|ASE_ECD.1 Extended components definition|
|ASE: Security Target evaluation|ASE_INT.1 ST introduction|
|ASE: Security Target evaluation|ASE_OBJ.1 Security objectives for the<br>operational environment|
|ASE: Security Target evaluation|ASE_REQ.1 Stated security requirements|
|ASE: Security Target evaluation|ASE_TSS.1 TOE summary specification|



**Table 10 - CAP-A**


April 2017 Version 3.1 Page 51 of 247


**Composed assurance packages**

## **9.4 Composition assurance level B (CAP-B) - Methodically** **composed**


Objectives


148 CAP-B permits a conscientious developer to gain maximum assurance from
understanding, at a subsystem level, the affects of interactions between
component TOEs integrated in the composed TOE, whilst minimising the
demand of involvement of the base component developer.


149 CAP-B is applicable in those circumstances where developers or users
require a moderate level of independently assured security, and require a
thorough investigation of the composed TOE and its development without
substantial re-engineering.


Assurance components


150 **CAP-B** provides assurance by analysis of a **full** security target for the
composed TOE. The SFRs in the composed TOE ST are analysed using the
outputs from the evaluations of the component TOEs (e.g. ST, guidance
documentation), a specification for the interfaces between the component
TOEs **and** **the** **TOE** **design** **(describing** **TSF** **subsystems)** **contained** in the
composed **development** **information** to understand the security behaviour.


151 The analysis is supported by independent testing of the interfaces of the base
component that are relied upon by the dependent component, as described in
the reliance information **(now** **also** **including** **TOE** **design),** evidence of
developer testing based on the reliance information, development
information and composition rationale, and selective independent
confirmation of the developer test results. The analysis is also supported by a
vulnerability **analysis** of the composed TOE by the evaluator **demonstrating**
**resistance** **to** **attackers** **with** **basic** **attack** **potential.**


152 **This** **CAP** **represents** **a** **meaningful** **increase** **in** **assurance** **from** CAP-A **by**
**requiring** **more** **complete** **testing** **coverage** of the **security** **functionality.**


Page 52 of 247 Version 3.1 April 2017


**Composed assurance packages**












|Assurance Class|Assurance components|
|---|---|
|ACO: Composition|ACO_COR.1 Composition rationale|
|ACO: Composition|ACO_CTT.2 Rigorous interface testing|
|ACO: Composition|ACO_DEV.2 Basic evidence of design|
|ACO: Composition|ACO_REL.1 Basic reliance information|
|ACO: Composition|ACO_VUL.2 Composition vulnerability<br>analysis|
|AGD: Guidance documents|AGD_OPE.1 Operational user guidance|
|AGD: Guidance documents|AGD_PRE.1 Preparative procedures|
|ALC: Life-cycle support|ALC_CMC.1 Labelling of the TOE|
|ALC: Life-cycle support|ALC_CMS.2 Parts of the TOE CM coverage|
|ASE: Security Target evaluation|ASE_CCL.1 Conformance claims|
|ASE: Security Target evaluation|ASE_ECD.1 Extended components definition|
|ASE: Security Target evaluation|ASE_INT.1 ST introduction|
|ASE: Security Target evaluation|ASE_OBJ.2 Security objectives|
|ASE: Security Target evaluation|ASE_REQ.2 Derived security requirements|
|ASE: Security Target evaluation|ASE_SPD.1 Security problem definition|
|ASE: Security Target evaluation|ASE_TSS.1 TOE summary specification|



**Table 11 - CAP-B**


April 2017 Version 3.1 Page 53 of 247


**Composed assurance packages**

## **9.5 Composition assurance level C (CAP-C) - Methodically** **composed, tested and reviewed**


Objectives


153 CAP-C permits a developer to gain maximum assurance from positive
analysis of the interactions between the components of the composed TOE,
which, though rigorous, do not require full access to all evaluation evidence
of the base component.


154 CAP-C is therefore applicable in those circumstances where developers or
users require a moderate to high level of independently assured security in
conventional commodity composed TOEs and are prepared to incur
additional security-specific engineering costs.


Assurance components


155 **CAP-C** provides assurance by analysis of a full security target for the
composed TOE. The SFRs in the composed TOE ST are analysed using the
outputs from the evaluations of the component TOEs (e.g. ST, guidance
documentation), a specification for the interfaces between the component
TOEs and the TOE design (describing TSF **modules)** contained in the
composed development information to understand the security behaviour.


156 The analysis is supported by independent testing of the interfaces of the base
component that are relied upon by the dependent component, as described in
the reliance information (now including TOE design), evidence of developer
testing based on the reliance information, development information and
composition rationale, and selective independent confirmation of the
developer test results. The analysis is also supported by a vulnerability
analysis of the composed TOE by the evaluator demonstrating resistance to
attackers with **Enhanced-Basic** attack potential.


157 This CAP represents a meaningful increase in assurance from **CAP-B** by
requiring more **design** **description** **and** **demonstration** of **resistance** **to** **a**
**higher** **attack** **potential.**


Page 54 of 247 Version 3.1 April 2017


**Composed assurance packages**












|Assurance Class|Assurance components|
|---|---|
|ACO: Composition|ACO_COR.1 Composition rationale|
|ACO: Composition|ACO_CTT.2 Rigorous interface testing|
|ACO: Composition|ACO_DEV.3 Detailed evidence of design|
|ACO: Composition|ACO_REL.2 Reliance information|
|ACO: Composition|ACO_VUL.3 Enhanced-Basic Composition<br>vulnerability analysis|
|AGD: Guidance documents|AGD_OPE.1 Operational user guidance|
|AGD: Guidance documents|AGD_PRE.1 Preparative procedures|
|ALC: Life-cycle support|ALC_CMC.1 Labelling of the TOE|
|ALC: Life-cycle support|ALC_CMS.2 Parts of the TOE CM coverage|
|ASE: Security Target evaluation|ASE_CCL.1 Conformance claims|
|ASE: Security Target evaluation|ASE_ECD.1 Extended components definition|
|ASE: Security Target evaluation|ASE_INT.1 ST introduction|
|ASE: Security Target evaluation|ASE_OBJ.2 Security objectives|
|ASE: Security Target evaluation|ASE_REQ.2 Derived security requirements|
|ASE: Security Target evaluation|ASE_SPD.1 Security problem definition|
|ASE: Security Target evaluation|ASE_TSS.1 TOE summary specification|



**Table 12 - CAP-C**


April 2017 Version 3.1 Page 55 of 247


**Class APE: Protection Profile evaluation**

# **10 Class APE: Protection Profile evaluation**


158 Evaluating a PP is required to demonstrate that the PP is sound and internally
consistent, and, if the PP is based on one or more other PPs or on packages,
that the PP is a correct instantiation of these PPs and packages. These
properties are necessary for the PP to be suitable for use as the basis for
writing an ST or another PP.


159 This Chapter should be used in conjunction with Annexes A, B and C in CC
Part 1, as these Annexes clarify the concepts here and provide many
examples.


160 This standard defines two assurance packages for PP evaluation as follows:


a) Low assurance PP evaluation package;


b) (Standard) PP evaluation package.


161 The assurance components for these packages are defined by table 13.










|Assurance class|Assurance<br>family|Assurance component|Col4|
|---|---|---|---|
|Assurance class|Assurance<br>family|Low Assurance<br>PP|PP|
|Protection Profile<br>evaluation|APE_CCL|**1**|1|
|Protection Profile<br>evaluation|APE_ECD|**1**|1|
|Protection Profile<br>evaluation|APE_INT|**1**|1|
|Protection Profile<br>evaluation|APE_OBJ|**1**|**2**|
|Protection Profile<br>evaluation|APE_REQ|**1**|**2**|
|Protection Profile<br>evaluation|APE_SPD||**1**|



**Table 13 - PP assurance packages**


162 Figure 8 shows the families within this class, and the hierarchy of
components within the families.


Page 56 of 247 Version 3.1 April 2017


**Class APE: Protection Profile evaluation**


**Figure 8 - APE: Protection Profile evaluation class decomposition**


April 2017 Version 3.1 Page 57 of 247


**Class APE: Protection Profile evaluation**

## **10.1 PP introduction (APE_INT)**


Objectives


163 The objective of this family is to describe the TOE in a narrative way.


164 Evaluation of the PP introduction is required to demonstrate that the PP is
correctly identified, and that the PP reference and TOE overview are
consistent with each other.


**APE_INT.1** **PP introduction**


Dependencies: No dependencies.


Developer action elements:


**APE_INT.1.1D** **The developer shall provide a PP introduction.**


Content and presentation elements:


**APE_INT.1.1C** **The PP introduction shall contain a PP reference and a TOE overview.**


**APE_INT.1.2C** **The PP reference shall uniquely identify the PP.**


**APE_INT.1.3C** **The TOE overview shall summarise the usage and major security**
**features of the TOE.**


**APE_INT.1.4C** **The TOE overview shall identify the TOE type.**


**APE_INT.1.5C** **The** **TOE** **overview** **shall** **identify** **any** **non-TOE**
**hardware/software/firmware available to the TOE.**


Evaluator action elements:


**APE_INT.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**
**requirements for content and presentation of evidence.**


Page 58 of 247 Version 3.1 April 2017


**Class APE: Protection Profile evaluation**

## **10.2 Conformance claims (APE_CCL)**


Objectives


165 The objective of this family is to determine the validity of the conformance
claim. In addition, this family specifies how STs and other PPs are to claim
conformance with the PP.


**APE_CCL.1** **Conformance claims**


Dependencies: APE_INT.1 PP introduction
APE_ECD.1 Extended components definition
APE_REQ.1 Stated security requirements


Developer action elements:


**APE_CCL.1.1D** **The developer shall provide a conformance claim.**


**APE_CCL.1.2D** **The developer shall provide a conformance claim rationale.**


**APE_CCL.1.3D** **The developer shall provide a conformance statement.**


Content and presentation elements:


**APE_CCL.1.1C** **The conformance claim shall contain a CC conformance claim that**

**identifies the version of the CC to which the PP claims conformance.**


**APE_CCL.1.2C** **The CC conformance claim shall describe the conformance of the PP to**

**CC Part 2 as either CC Part 2 conformant or CC Part 2 extended.**


**APE_CCL.1.3C** **The CC conformance claim shall describe the conformance of the PP to**

**CC Part 3 as either CC Part 3 conformant or CC Part 3 extended.**


**APE_CCL.1.4C** **The CC conformance claim shall be consistent with the extended**

**components definition.**


**APE_CCL.1.5C** **The conformance claim shall identify all PPs and security requirement**

**packages to which the PP claims conformance.**


**APE_CCL.1.6C** **The conformance claim shall describe any conformance of the PP to a**

**package as either package-conformant or package-augmented.**


**APE_CCL.1.7C** **The conformance claim rationale shall demonstrate that the TOE type is**

**consistent with the TOE type in the PPs for which conformance is being**
**claimed.**


**APE_CCL.1.8C** **The conformance claim rationale shall demonstrate that the statement**

**of the security problem definition is consistent with the statement of the**
**security problem definition in the PPs for which conformance is being**
**claimed.**


April 2017 Version 3.1 Page 59 of 247


**Class APE: Protection Profile evaluation**


**APE_CCL.1.9C** **The conformance claim rationale shall demonstrate that the statement**

**of security objectives is consistent with the statement of security**
**objectives in the PPs for which conformance is being claimed.**


**APE_CCL.1.10C** **The conformance claim rationale shall demonstrate that the statement**

**of security requirements is consistent with the statement of security**
**requirements in the PPs for which conformance is being claimed.**


**APE_CCL.1.11C** **The conformance statement shall describe the conformance required of**

**any PPs/STs to the PP as strict-PP or demonstrable-PP conformance.**


Evaluator action elements:


**APE_CCL.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


Page 60 of 247 Version 3.1 April 2017


**Class APE: Protection Profile evaluation**

## **10.3 Security problem definition (APE_SPD)**


Objectives


166 This part of the PP defines the security problem to be addressed by the TOE
and the operational environment of the TOE.


167 Evaluation of the security problem definition is required to demonstrate that
the security problem intended to be addressed by the TOE and its operational
environment, is clearly defined.


**APE_SPD.1** **Security problem definition**


Dependencies: No dependencies.


Developer action elements:


**APE_SPD.1.1D** **The developer shall provide a security problem definition.**


Content and presentation elements:


**APE_SPD.1.1C** **The security problem definition shall describe the threats.**


**APE_SPD.1.2C** **All threats shall be described in terms of a threat agent, an asset, and an**

**adverse action.**


**APE_SPD.1.3C** **The security problem definition shall describe the OSPs.**


**APE_SPD.1.4C** **The security problem definition shall describe the assumptions about the**

**operational environment of the TOE.**


Evaluator action elements:


**APE_SPD.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**
**requirements for content and presentation of evidence.**


April 2017 Version 3.1 Page 61 of 247


**Class APE: Protection Profile evaluation**

## **10.4 Security objectives (APE_OBJ)**


Objectives


168 The security objectives are a concise statement of the intended response to
the security problem defined through the Security problem definition
(APE_SPD) family.


169 Evaluation of the security objectives is required to demonstrate that the
security objectives adequately and completely address the security problem
definition and that the division of this problem between the TOE and its
operational environment is clearly defined.


Component levelling


170 The components in this family are levelled on whether they prescribe only
security objectives for the operational environment, or also security
objectives for the TOE.


**APE_OBJ.1** **Security objectives for the operational environment**


Dependencies: No dependencies.


Developer action elements:


**APE_OBJ.1.1D** **The developer shall provide a statement of security objectives.**


Content and presentation elements:


**APE_OBJ.1.1C** **The statement of security objectives shall describe the security objectives**

**for the operational environment.**


Evaluator action elements:


**APE_OBJ.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


**APE_OBJ.2** **Security objectives**


Dependencies: APE_SPD.1 Security problem definition


Developer action elements:


**APE_OBJ.2.1D** The developer shall provide a statement of security objectives.


**APE_OBJ.2.2D** **The developer shall provide a security objectives rationale.**


Content and presentation elements:


**APE_OBJ.2.1C** The statement of security objectives shall describe the security objectives for

the **TOE** **and** **the** **security** **objectives** **for** **the** operational environment.


Page 62 of 247 Version 3.1 April 2017


**Class APE: Protection Profile evaluation**


**APE_OBJ.2.2C** **The security objectives rationale shall trace each security objective for**

**the TOE back to threats countered by that security objective and OSPs**
**enforced by that security objective.**


**APE_OBJ.2.3C** **The security objectives rationale shall trace each security objective for**

**the operational environment back to threats countered by that security**
**objective, OSPs enforced by that security objective, and assumptions**
**upheld by that security objective.**


**APE_OBJ.2.4C** **The security objectives rationale shall demonstrate that the security**

**objectives counter all threats.**


**APE_OBJ.2.5C** **The security objectives rationale shall demonstrate that the security**

**objectives enforce all OSPs.**


**APE_OBJ.2.6C** **The security objectives rationale shall demonstrate that the security**

**objectives for the operational environment uphold all assumptions.**


Evaluator action elements:


**APE_OBJ.2.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


April 2017 Version 3.1 Page 63 of 247


**Class APE: Protection Profile evaluation**

## **10.5 Extended components definition (APE_ECD)**


Objectives


171 Extended security requirements are requirements that are not based on
components from CC Part 2 or CC Part 3, but are based on extended
components: components defined by the PP author.


172 Evaluation of the definition of extended components is necessary to
determine that they are clear and unambiguous, and that they are necessary,
i.e. they may not be clearly expressed using existing CC Part 2 or CC Part 3
components.


**APE_ECD.1** **Extended components definition**


Dependencies: No dependencies.


Developer action elements:


**APE_ECD.1.1D** **The developer shall provide a statement of security requirements.**


**APE_ECD.1.2D** **The developer shall provide an extended components definition.**


Content and presentation elements:


**APE_ECD.1.1C** **The statement of security requirements shall identify all extended**

**security requirements.**


**APE_ECD.1.2C** **The extended components definition shall define an extended component**

**for each extended security requirement.**


**APE_ECD.1.3C** **The extended components definition shall describe how each extended**

**component is related to the existing CC components, families, and**
**classes.**


**APE_ECD.1.4C** **The extended components definition shall use the existing CC**

**components, families, classes, and methodology as a model for**
**presentation.**


**APE_ECD.1.5C** **The extended components shall consist of measurable and objective**

**elements such that conformance or nonconformance to these elements**
**can be demonstrated.**


Evaluator action elements:


**APE_ECD.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


**APE_ECD.1.2E** **The evaluator** _**shall confirm**_ **that no extended component may be clearly**

**expressed using existing components.**


Page 64 of 247 Version 3.1 April 2017


**Class APE: Protection Profile evaluation**

## **10.6 Security requirements (APE_REQ)**


Objectives


173 The SFRs form a clear, unambiguous and well-defined description of the
expected security behaviour of the TOE. The SARs form a clear,
unambiguous and well-defined description of the expected activities that will
be undertaken to gain assurance in the TOE.


174 Evaluation of the security requirements is required to ensure that they are
clear, unambiguous and well-defined.


Component levelling


175 The components in this family are levelled on whether they are stated as is,
or whether the SFRs are derived from security objectives for the TOE.


**APE_REQ.1** **Stated security requirements**


Dependencies: APE_ECD.1 Extended components definition


Developer action elements:


**APE_REQ.1.1D** **The developer shall provide a statement of security requirements.**


**APE_REQ.1.2D** **The developer shall provide a security requirements rationale.**


Content and presentation elements:


**APE_REQ.1.1C** **The statement of security requirements shall describe the SFRs and the**

**SARs.**


**APE_REQ.1.2C** **All subjects, objects, operations, security attributes, external entities and**

**other terms that are used in the SFRs and the SARs shall be defined.**


**APE_REQ.1.3C** **The statement of security requirements shall identify all operations on**

**the security requirements.**


**APE_REQ.1.4C** **All operations shall be performed correctly.**


**APE_REQ.1.5C** **Each dependency of the security requirements shall either be satisfied,**

**or the security requirements rationale shall justify the dependency not**
**being satisfied.**


**APE_REQ.1.6C** **The statement of security requirements shall be internally consistent.**


Evaluator action elements:


**APE_REQ.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


April 2017 Version 3.1 Page 65 of 247


**Class APE: Protection Profile evaluation**


**APE_REQ.2** **Derived security requirements**


Dependencies: APE_OBJ.2 Security objectives
APE_ECD.1 Extended components definition


Developer action elements:


**APE_REQ.2.1D** The developer shall provide a statement of security requirements.


**APE_REQ.2.2D** The developer shall provide a security requirements rationale.


Content and presentation elements:


**APE_REQ.2.1C** The statement of security requirements shall describe the SFRs and the SARs.


**APE_REQ.2.2C** All subjects, objects, operations, security attributes, external entities and

other terms that are used in the SFRs and the SARs shall be defined.


**APE_REQ.2.3C** The statement of security requirements shall identify all operations on the

security requirements.


**APE_REQ.2.4C** All operations shall be performed correctly.


**APE_REQ.2.5C** Each dependency of the security requirements shall either be satisfied, or the

security requirements rationale shall justify the dependency not being
satisfied.


**APE_REQ.2.6C** **The security requirements rationale shall trace each SFR back to the**

**security objectives for the TOE.**


**APE_REQ.2.7C** **The security requirements rationale shall demonstrate that the SFRs**

**meet all security objectives for the TOE.**


**APE_REQ.2.8C** **The security requirements rationale shall explain why the SARs were**

**chosen.**


**APE_REQ.2.9C** The statement of security requirements shall be internally consistent.


Evaluator action elements:


**APE_REQ.2.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


Page 66 of 247 Version 3.1 April 2017


**Class ACE: Protection Profile Configuration evaluation**

# **11 Class ACE: Protection Profile** **Configuration evaluation**


176 Evaluating a PP-Configuration is required to demonstrate that the PPConfiguration is sound and consistent. These properties are necessary for the
PP-Configuration to be suitable for use as the basis for writing an ST or
another PP or PP-Configuration.


177 The class ACE is defined for the evaluation of a PP-Configuration composed
of one or more PPs and one PP-Module.


178 This Chapter should be used in conjunction with Annexes B and C in CC
Part 1, as these Annexes clarify the concepts here and provide many
examples.


179 This standard does not define low assurance PP-Configuration evaluation
package. There is only one assurance package for PP-Configuration
evaluation, equivalent to Standard PP evaluation package.


180 Figure 9 shows the families within this class, and the hierarchy of
components within the families.


**Figure 9 - ACE: Protection Profile Configuration evaluation class**

**decomposition**


April 2017 Version 3.1 Page 67 of 247


**Class ACE: Protection Profile Configuration evaluation**


181 The ACE class is based on APE.


Page 68 of 247 Version 3.1 April 2017


**Class ACE: Protection Profile Configuration evaluation**

## **11.1 PP-Module introduction (ACE_INT)**


Objectives


182 The objective of this family is to describe the TOE in a narrative way.


183 The objective of this sub-activity is to determine whether the PP-Module is
correctly identified, and whether the PP-Module reference and TOE
overview are consistent with each other.


**ACE_INT.1** **PP-Module introduction**


Dependencies: No dependencies.


Application notes


184 _All content and presentation elements of APE_INT.1 hold with PP-Module_
_instead of PP._


Developer action elements:


**ACE_INT.1.1D** **The developer shall provide a PP-Module introduction.**


Content and presentation elements:


**ACE_INT.1.1C** **The PP-Module introduction shall uniquely identify all the Base-PPs on**

**which the PP-Module relies, including their logical structuring and**
**relationship to the PP-Module according to CC Part 1, section B.14.3.2.**


**ACE_INT.1.2C** **The TOE overview shall identify the differences introduced by the PP-**

**Module with respect to the TOE overview of its Base-PP(s).**


Evaluator action elements:


**ACE_INT.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**
**requirements for content and presentation of evidence.**


April 2017 Version 3.1 Page 69 of 247


**Class ACE: Protection Profile Configuration evaluation**

## **11.2 PP-Module conformance claims (ACE_CCL)**


Objectives


185 The objective of this family is to determine the validity of the conformance
claim. Unlike standard Protection Profiles, a PP-Module cannot claim
conformance to another PP or PP-Module, nor to CC part 3 or any SAR
package.


**ACE_CCL.1** **PP-Module conformance claims**


Dependencies: ACE_INT.1 PP-Module introduction
ACE_ECD.1 PP-Module extended components
definition
ACE_REQ.1 PP-Module security requirements


Developer action elements:


**ACE_CCL.1.1D** **The developer shall provide a conformance claim.**


Content and presentation elements:


**ACE_CCL.1.1C** **The conformance claim shall contain a CC conformance claim that**

**identifies the version of the CC to which the PP-Module claims**
**conformance.**


**ACE_CCL.1.2C** **The CC conformance claim shall describe the conformance of the PP-**

**Module to CC Part 2 as either CC Part 2 conformant or CC Part 2**
**extended.**


**ACE_CCL.1.3C** **The conformance claim shall identify all security functional requirement**

**packages to which the PP-Module claims conformance**


**ACE_CCL.1.4C** **The CC conformance claim shall be consistent with the extended**

**components definition.**


Evaluator action elements:


**ACE_CCL.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


Page 70 of 247 Version 3.1 April 2017


**Class ACE: Protection Profile Configuration evaluation**

## **11.3 PP-Module Security problem definition (ACE_SPD)**


**ACE_SPD.1** **PP-Module Security problem definition**


Dependencies: No dependencies.


Application notes


186 _All content and presentation elements of APE_SPD.1 hold._


April 2017 Version 3.1 Page 71 of 247


**Class ACE: Protection Profile Configuration evaluation**

## **11.4 PP-Module Security objectives (ACE_OBJ)**


**ACE_OBJ.1** **PP-Module Security objectives**


Dependencies: No dependencies.


Application notes


187 _All content and presentation elements of APE_OBJ.2 hold._


Page 72 of 247 Version 3.1 April 2017


**Class ACE: Protection Profile Configuration evaluation**

## **11.5 PP-Module extended components definition (ACE_ECD)**


Objectives


188 Extended security functional requirements are requirements that are not
based on components from CC Part 2, but are based on extended
components: components defined by the PP-Module author.


189 Evaluation of the definition of extended functional components is necessary
to determine that they are clear and unambiguous, and that they are necessary,
i.e. they may not be clearly expressed using existing CC Part 2 components.


**ACE_ECD.1** **PP-Module extended components definition**


Dependencies: No dependencies.


Developer action elements:


**ACE_ECD.1.1D** **The developer shall provide a statement of security functional**

**requirements.**


**ACE_ECD.1.2D** **The developer shall provide an extended functional components**

**definition.**


Content and presentation elements:


**ACE_ECD.1.1C** **The statement of security functional requirements shall identify all**

**extended security functional requirements.**


**ACE_ECD.1.2C** **The extended functional components definition shall define an extended**

**functional component for each extended security functional requirement.**


**ACE_ECD.1.3C** **The extended functional components definition shall describe how each**

**extended functional component is related to the existing CC Part 2**
**components, families, and classes.**


**ACE_ECD.1.4C** **The extended functional components definition shall use the existing CC**

**Part 2 components, families, classes, and methodology as a model for**
**presentation.**


**ACE_ECD.1.5C** **The extended functional components shall consist of measurable and**

**objective elements such that conformance or nonconformance to these**
**elements can be demonstrated.**


Evaluator action elements:


**ACE_ECD.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


**ACE_ECD.1.2E** **The evaluator** _**shall confirm**_ **that no extended functional component may**

**be clearly expressed using existing components.**


April 2017 Version 3.1 Page 73 of 247


**Class ACE: Protection Profile Configuration evaluation**

## **11.6 PP-Module security requirements (ACE_REQ)**


Objectives


190 The SFRs form a clear, unambiguous and well-defined description of the
expected security behaviour of the TOE.


191 Evaluation of the security functional requirements is required to ensure that
they are clear, unambiguous and well-defined.


**ACE_REQ.1** **PP-Module security requirements**


Dependencies: ACE_ECD.1 PP-Module extended components
definition
ACE_OBJ.1 PP-Module Security objectives


Developer action elements:


**ACE_REQ.1.1D** **The developer shall provide a statement of security functional**

**requirements.**


**ACE_REQ.1.2D** **The developer shall provide a security requirements rationale.**


Content and presentation elements:


**ACE_REQ.1.1C** **The statement of security requirements shall describe the SFRs that**

**hold on the TOE.**


**ACE_REQ.1.2C** **All subjects, objects, operations, security attributes, external entities and**

**other terms that are used in the SFRs shall be defined.**


**ACE_REQ.1.3C** **The statement of security functional requirements shall identify all**

**operations on the security functional requirements.**


**ACE_REQ.1.4C** **All operations shall be performed correctly.**


**ACE_REQ.1.5C** **Each dependency of the security functional requirements shall either be**

**satisfied, or the security functional requirements rationale shall justify**
**the dependency not being satisfied.**


**ACE_REQ.1.6C** **The security functional requirements rationale shall trace each SFR**

**back to the security objectives for the TOE.**


**ACE_REQ.1.7C** **The security functional requirements rationale shall demonstrate that**

**the SFRs meet all security objectives for the TOE.**


Evaluator action elements:


**ACE_REQ.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


Page 74 of 247 Version 3.1 April 2017


**Class ACE: Protection Profile Configuration evaluation**

## **11.7 PP-Module consistency (ACE_MCO)**


Objectives


192 The objective of this family is to determine the validity of the PP-Module.


**ACE_MCO.1** **PP-Module consistency**


Dependencies: ACE_INT.1 PP-Module introduction
ACE_SPD.1 PP-Module Security problem definition
ACE_OBJ.1 PP-Module Security objectives
ACE_REQ.1 PP-Module security requirements


Developer action elements:


**ACE_MCO.1.1D** **The developer shall provide a consistency rationale of the PP-Module**

**with respect to its Base-PP(s) identified in the PP-Module introduction.**
**If the PP-Module specifies alternate sets of Base-PPs, the developer shall**
**provide as many consistency rationales as the number of alternate set of**
**Base-PPs.**


Content and presentation elements:


**ACE_MCO.1.1C** **The consistency rationale shall demonstrate that the TOE type of the**

**PP-Module is consistent with the TOE type(s) in the Base-PPs identified**
**in the PP-Module introduction.**


**ACE_MCO.1.2C** **The consistency rationale shall demonstrate that the statement of the**

**security problem definition is consistent with the statement of the**
**security problem definition in the Base-PPs identified in the PP-Module**
**introduction.**


**ACE_MCO.1.3C** **The consistency rationale shall demonstrate that the statement of**

**security objectives is consistent with the statement of security objectives**
**in the Base-PPs identified in the PP-Module introduction.**


**ACE_MCO.1.4C** **The consistency rationale shall demonstrate that the statement of**

**security requirements is consistent with the statement of security**
**requirements in the Base-PPs identified in the PP-Module introduction.**


Evaluator action elements:


**ACE_MCO.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence. If the PP-Module**
**specifies alternate sets of Base-PPs, the evaluator** _**shall perform**_ **this**
**action for each consistency rationale with its related Base-PPs in the**
**alternate set of Base-PPs of the PP-Module.**


April 2017 Version 3.1 Page 75 of 247


**Class ACE: Protection Profile Configuration evaluation**

## **11.8 PP-Configuration consistency (ACE_CCO)**


Objectives


193 The objective of this family is to determine the well-formedness and the
consistency of the PP-Configuration.


**ACE_CCO.1** **PP-Configuration consistency**


Dependencies: ACE_INT.1 PP-Module introduction
ACE_REQ.1 PP-Module security requirements
ACE_MCO.1 PP-Module consistency


Developer action elements:


**ACE_CCO.1.1D** **The developer shall provide the reference of the PP-Configuration.**


**ACE_CCO.1.2D** **The developer shall provide a components statement.**


**ACE_CCO.1.3D** **The developer shall provide a conformance statement and a**

**conformance claim.**


**ACE_CCO.1.4D** **The developer shall provide a SAR statement.**


Content and presentation elements:


**ACE_CCO.1.1C** **The PP-Configuration reference shall uniquely identify the PP-**

**Configuration.**


**ACE_CCO.1.2C** **The components statements shall uniquely identify the Protection**

**Profiles and the PP-Modules that compose the PP-Configuration.**


**ACE_CCO.1.3C** **The conformance statement shall specify the kind of conformity of the**

**PP-Configuration, either strict or demonstrable. The conformance claim**
**shall contain a CC conformance claim that identifies the version of the**
**CC to which the PP-Configuration and its underlying Base-PP(s) and**
**PP-Module claim conformance.**


**ACE_CCO.1.4C** **The SAR statement shall specify the set of SAR or predefined EAL that**

**applies to this PP-Configuration.**


**ACE_CCO.1.5C** **The Base-PP(s) on which the PP-Modules relies shall belong the**

**Protection Profiles identified in the components statement of the PP-**
**Configuration.**


Evaluator action elements:


**ACE_CCO.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


Page 76 of 247 Version 3.1 April 2017


**Class ACE: Protection Profile Configuration evaluation**


**ACE_CCO.1.2E** **The evaluator** _**shall check**_ **that the PP-Configuration made up of all the**

**Protection Profiles and PP-Modules identified in the components**
**statement of the PP-Configuration is consistent.**


April 2017 Version 3.1 Page 77 of 247


**Class ASE: Security Target evaluation**

# **12 Class ASE: Security Target evaluation**


194 Evaluating an ST is required to demonstrate that the ST is sound and
internally consistent, and, if the ST is based on one or more PPs or packages,
that the ST is a correct instantiation of these PPs and packages. These
properties are necessary for the ST to be suitable for use as the basis for a
TOE evaluation.


195 This Chapter should be used in conjunction with Annexes A, B and C in CC
Part 1, as these Annexes clarify the concepts here and provide many
examples.


196 Figure 10 shows the families within this class, and the hierarchy of
components within the families.


**Figure 10 - ASE: Security Target evaluation class decomposition**


Page 78 of 247 Version 3.1 April 2017


**Class ASE: Security Target evaluation**

## **12.1 ST introduction (ASE_INT)**


Objectives


197 The objective of this family is to describe the TOE in a narrative way on
three levels of abstraction: TOE reference, TOE overview and TOE
description.


198 Evaluation of the ST introduction is required to demonstrate that the ST and
the TOE are correctly identified, that the TOE is correctly described at three
levels of abstraction and that these three descriptions are consistent with each
other.


**ASE_INT.1** **ST introduction**


Dependencies: No dependencies.


Developer action elements:


**ASE_INT.1.1D** **The developer shall provide an ST introduction.**


Content and presentation elements:


**ASE_INT.1.1C** **The ST introduction shall contain an ST reference, a TOE reference, a**
**TOE overview and a TOE description.**


**ASE_INT.1.2C** **The ST reference shall uniquely identify the ST.**


**ASE_INT.1.3C** **The TOE reference shall uniquely identify the TOE.**


**ASE_INT.1.4C** **The TOE overview shall summarise the usage and major security**
**features of the TOE.**


**ASE_INT.1.5C** **The TOE overview shall identify the TOE type.**


**ASE_INT.1.6C** **The** **TOE** **overview** **shall** **identify** **any** **non-TOE**
**hardware/software/firmware required by the TOE.**


**ASE_INT.1.7C** **The TOE description shall describe the physical scope of the TOE.**


**ASE_INT.1.8C** **The TOE description shall describe the logical scope of the TOE.**


Evaluator action elements:


**ASE_INT.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**
**requirements for content and presentation of evidence.**


**ASE_INT.1.2E** **The evaluator** _**shall confirm**_ **that the TOE reference, the TOE overview,**
**and the TOE description are consistent with each other.**


April 2017 Version 3.1 Page 79 of 247


**Class ASE: Security Target evaluation**

## **12.2 Conformance claims (ASE_CCL)**


Objectives


199 The objective of this family is to determine the validity of the conformance
claim. In addition, this family specifies how STs are to claim conformance
with the PP.


**ASE_CCL.1** **Conformance claims**


Dependencies: ASE_INT.1 ST introduction
ASE_ECD.1 Extended components definition
ASE_REQ.1 Stated security requirements


Developer action elements:


**ASE_CCL.1.1D** **The developer shall provide a conformance claim.**


**ASE_CCL.1.2D** **The developer shall provide a conformance claim rationale.**


Content and presentation elements:


**ASE_CCL.1.1C** **The conformance claim shall contain a CC conformance claim that**

**identifies the version of the CC to which the ST and the TOE claim**
**conformance.**


**ASE_CCL.1.2C** **The CC conformance claim shall describe the conformance of the ST to**

**CC Part 2 as either CC Part 2 conformant or CC Part 2 extended.**


**ASE_CCL.1.3C** **The CC conformance claim shall describe the conformance of the ST to**

**CC Part 3 as either CC Part 3 conformant or CC Part 3 extended.**


**ASE_CCL.1.4C** **The CC conformance claim shall be consistent with the extended**

**components definition.**


**ASE_CCL.1.5C** **The conformance claim shall identify all PPs and security requirement**

**packages to which the ST claims conformance.**


**ASE_CCL.1.6C** **The conformance claim shall describe any conformance of the ST to a**

**package as either package-conformant or package-augmented.**


**ASE_CCL.1.7C** **The conformance claim rationale shall demonstrate that the TOE type is**

**consistent with the TOE type in the PPs for which conformance is being**
**claimed.**


**ASE_CCL.1.8C** **The conformance claim rationale shall demonstrate that the statement**

**of the security problem definition is consistent with the statement of the**
**security problem definition in the PPs for which conformance is being**
**claimed.**


Page 80 of 247 Version 3.1 April 2017


**Class ASE: Security Target evaluation**


**ASE_CCL.1.9C** **The conformance claim rationale shall demonstrate that the statement**

**of security objectives is consistent with the statement of security**
**objectives in the PPs for which conformance is being claimed.**


**ASE_CCL.1.10C** **The conformance claim rationale shall demonstrate that the statement**

**of security requirements is consistent with the statement of security**
**requirements in the PPs for which conformance is being claimed.**


Evaluator action elements:


**ASE_CCL.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


April 2017 Version 3.1 Page 81 of 247


**Class ASE: Security Target evaluation**

## **12.3 Security problem definition (ASE_SPD)**


Objectives


200 This part of the ST defines the security problem to be addressed by the TOE
and the operational environment of the TOE.


201 Evaluation of the security problem definition is required to demonstrate that
the security problem intended to be addressed by the TOE and its operational
environment, is clearly defined.


**ASE_SPD.1** **Security problem definition**


Dependencies: No dependencies.


Developer action elements:


**ASE_SPD.1.1D** **The developer shall provide a security problem definition.**


Content and presentation elements:


**ASE_SPD.1.1C** **The security problem definition shall describe the threats.**


**ASE_SPD.1.2C** **All threats shall be described in terms of a threat agent, an asset, and an**
**adverse action.**


**ASE_SPD.1.3C** **The security problem definition shall describe the OSPs.**


**ASE_SPD.1.4C** **The security problem definition shall describe the assumptions about the**
**operational environment of the TOE.**


Evaluator action elements:


**ASE_SPD.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**
**requirements for content and presentation of evidence.**


Page 82 of 247 Version 3.1 April 2017


**Class ASE: Security Target evaluation**

## **12.4 Security objectives (ASE_OBJ)**


Objectives


202 The security objectives are a concise statement of the intended response to
the security problem defined through the Security problem definition
(ASE_SPD) family.


203 Evaluation of the security objectives is required to demonstrate that the
security objectives adequately and completely address the security problem
definition, that the division of this problem between the TOE and its
operational environment is clearly defined.


Component levelling


204 The components in this family are levelled on whether they prescribe only
security objectives for the operational environment, or also security
objectives for the TOE.


**ASE_OBJ.1** **Security objectives for the operational environment**


Dependencies: No dependencies.


Developer action elements:


**ASE_OBJ.1.1D** **The developer shall provide a statement of security objectives.**


Content and presentation elements:


**ASE_OBJ.1.1C** **The statement of security objectives shall describe the security objectives**

**for the operational environment.**


Evaluator action elements:


**ASE_OBJ.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**
**requirements for content and presentation of evidence.**


**ASE_OBJ.2** **Security objectives**


Dependencies: ASE_SPD.1 Security problem definition


Developer action elements:


**ASE_OBJ.2.1D** The developer shall provide a statement of security objectives.


**ASE_OBJ.2.2D** **The developer shall provide a security objectives rationale.**


Content and presentation elements:


**ASE_OBJ.2.1C** **The statement of security objectives shall describe the security objectives**

**for the TOE and the security objectives for the operational environment.**


April 2017 Version 3.1 Page 83 of 247


**Class ASE: Security Target evaluation**


**ASE_OBJ.2.2C** **The security objectives rationale shall trace each security objective for**

**the TOE back to threats countered by that security objective and OSPs**
**enforced by that security objective.**


**ASE_OBJ.2.3C** **The security objectives rationale shall trace each security objective for**

**the operational environment back to threats countered by that security**
**objective, OSPs enforced by that security objective, and assumptions**
**upheld by that security objective.**


**ASE_OBJ.2.4C** The security objectives **rationale** shall **demonstrate** **that** the security

objectives **counter** **all** **threats.**


**ASE_OBJ.2.5C** **The security objectives rationale shall demonstrate that the security**

**objectives enforce all OSPs.**


**ASE_OBJ.2.6C** **The security objectives rationale shall demonstrate that the security**

**objectives for the operational environment uphold all assumptions.**


Evaluator action elements:


**ASE_OBJ.2.1E** The evaluator _**shall confirm**_ that the information provided meets all
requirements for content and presentation of evidence.


Page 84 of 247 Version 3.1 April 2017


**Class ASE: Security Target evaluation**

## **12.5 Extended components definition (ASE_ECD)**


Objectives


205 Extended security requirements are requirements that are not based on
components from CC Part 2 or CC Part 3, but are based on extended
components: components defined by the ST author.


206 Evaluation of the definition of extended components is necessary to
determine that they are clear and unambiguous, and that they are necessary,
i.e. they may not be clearly expressed using existing CC Part 2 or CC Part 3
components.


**ASE_ECD.1** **Extended components definition**


Dependencies: No dependencies.


Developer action elements:


**ASE_ECD.1.1D** **The developer shall provide a statement of security requirements.**


**ASE_ECD.1.2D** **The developer shall provide an extended components definition.**


Content and presentation elements:


**ASE_ECD.1.1C** **The statement of security requirements shall identify all extended**

**security requirements.**


**ASE_ECD.1.2C** **The extended components definition shall define an extended component**

**for each extended security requirement.**


**ASE_ECD.1.3C** **The extended components definition shall describe how each extended**

**component is related to the existing CC components, families, and**
**classes.**


**ASE_ECD.1.4C** **The extended components definition shall use the existing CC**

**components, families, classes, and methodology as a model for**
**presentation.**


**ASE_ECD.1.5C** **The extended components shall consist of measurable and objective**

**elements such that conformance or nonconformance to these elements**
**can be demonstrated.**


Evaluator action elements:


**ASE_ECD.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


**ASE_ECD.1.2E** **The evaluator** _**shall confirm**_ **that no extended component can be clearly**

**expressed using existing components.**


April 2017 Version 3.1 Page 85 of 247


**Class ASE: Security Target evaluation**

## **12.6 Security requirements (ASE_REQ)**


Objectives


207 The SFRs form a clear, unambiguous and well-defined description of the
expected security behaviour of the TOE. The SARs form a clear,
unambiguous and canonical description of the expected activities that will be
undertaken to gain assurance in the TOE.


208 Evaluation of the security requirements is required to ensure that they are
clear, unambiguous and well-defined.


Component levelling


209 The components in this family are levelled on whether they are stated as is.


**ASE_REQ.1** **Stated security requirements**


Dependencies: ASE_ECD.1 Extended components definition


Developer action elements:


**ASE_REQ.1.1D** **The developer shall provide a statement of security requirements.**


**ASE_REQ.1.2D** **The developer shall provide a security requirements rationale.**


Content and presentation elements:


**ASE_REQ.1.1C** **The statement of security requirements shall describe the SFRs and the**

**SARs.**


**ASE_REQ.1.2C** **All subjects, objects, operations, security attributes, external entities and**

**other terms that are used in the SFRs and the SARs shall be defined.**


**ASE_REQ.1.3C** **The statement of security requirements shall identify all operations on**

**the security requirements.**


**ASE_REQ.1.4C** **All operations shall be performed correctly.**


**ASE_REQ.1.5C** **Each dependency of the security requirements shall either be satisfied,**

**or the security requirements rationale shall justify the dependency not**
**being satisfied.**


**ASE_REQ.1.6C** **The statement of security requirements shall be internally consistent.**


Evaluator action elements:


**ASE_REQ.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


Page 86 of 247 Version 3.1 April 2017


**Class ASE: Security Target evaluation**


**ASE_REQ.2** **Derived security requirements**


Dependencies: ASE_OBJ.2 Security objectives
ASE_ECD.1 Extended components definition


Developer action elements:


**ASE_REQ.2.1D** The developer shall provide a statement of security requirements.


**ASE_REQ.2.2D** The developer shall provide a security requirements rationale.


Content and presentation elements:


**ASE_REQ.2.1C** The statement of security requirements shall describe the SFRs and the SARs.


**ASE_REQ.2.2C** All subjects, objects, operations, security attributes, external entities and

other terms that are used in the SFRs and the SARs shall be defined.


**ASE_REQ.2.3C** The statement of security requirements shall identify all operations on the

security requirements.


**ASE_REQ.2.4C** All operations shall be performed correctly.


**ASE_REQ.2.5C** Each dependency of the security requirements shall either be satisfied, or the

security requirements rationale shall justify the dependency not being
satisfied.


**ASE_REQ.2.6C** **The security requirements rationale shall trace each SFR back to the**

**security objectives for the TOE.**


**ASE_REQ.2.7C** **The security requirements rationale shall demonstrate that the SFRs**

**meet all security objectives for the TOE.**


**ASE_REQ.2.8C** **The security requirements rationale shall explain why the SARs were**

**chosen.**


**ASE_REQ.2.9C** The statement of security requirements shall be internally consistent.


Evaluator action elements:


**ASE_REQ.2.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


April 2017 Version 3.1 Page 87 of 247


**Class ASE: Security Target evaluation**

## **12.7 TOE summary specification (ASE_TSS)**


Objectives


210 The TOE summary specification enables evaluators and potential consumers
to gain a general understanding of how the TOE is implemented.


211 Evaluation of the TOE summary specification is necessary to determine
whether it is adequately described how the TOE:


 meets its SFRs;


 protects itself against interference, logical tampering and bypass.


and whether the TOE summary specification is consistent with other
narrative descriptions of the TOE.


Component levelling


212 The components in this family are levelled on whether the TOE summary
specification only needs to describe how the TOE meets the SFRs, or
whether the TOE summary specification also needs to describe how the TOE
protects itself against logical tampering and bypass. This additional
description may be used in special circumstances where there might be a
specific concern regarding the TOE security architecture.


**ASE_TSS.1** **TOE summary specification**


Dependencies: ASE_INT.1 ST introduction
ASE_REQ.1 Stated security requirements
ADV_FSP.1 Basic functional specification


Developer action elements:


**ASE_TSS.1.1D** **The developer shall provide a TOE summary specification.**


Content and presentation elements:


**ASE_TSS.1.1C** **The TOE summary specification shall describe how the TOE meets each**
**SFR.**


Evaluator action elements:


**ASE_TSS.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**
**requirements for content and presentation of evidence.**


**ASE_TSS.1.2E** **The evaluator** _**shall confirm**_ **that the TOE summary specification is**
**consistent with the TOE overview and the TOE description.**


Page 88 of 247 Version 3.1 April 2017


**Class ASE: Security Target evaluation**


**ASE_TSS.2** **TOE summary specification with architectural design summary**


Dependencies: ASE_INT.1 ST introduction
ASE_REQ.1 Stated security requirements
ADV_ARC.1 Security architecture description


Developer action elements:


**ASE_TSS.2.1D** The developer shall provide a TOE summary specification.


Content and presentation elements:


**ASE_TSS.2.1C** The TOE summary specification shall describe how the TOE meets each
SFR.


**ASE_TSS.2.2C** **The TOE summary specification shall describe how the TOE protects**
**itself against interference and logical tampering.**


**ASE_TSS.2.3C** **The TOE summary specification shall describe how the TOE protects**
**itself against bypass.**


Evaluator action elements:


**ASE_TSS.2.1E** The evaluator _**shall confirm**_ that the information provided meets all
requirements for content and presentation of evidence.


**ASE_TSS.2.2E** The evaluator _**shall confirm**_ that the TOE summary specification is
consistent with the TOE overview and the TOE description.


April 2017 Version 3.1 Page 89 of 247


**Class ADV: Development**

# **13 Class ADV: Development**


213 The requirements of the Development class provide information about the
TOE. The knowledge obtained by this information is used as the basis for
conducting vulnerability analysis and testing upon the TOE, as described in
the AVA and ATE classes.


214 The Development class encompasses six families of requirements for
structuring and representing the TSF at various levels and varying forms of
abstraction. These families include:


 requirements for the description (at the various levels of abstraction)
of the design and implementation of the SFRs (ADV_FSP,
ADV_TDS, ADV_IMP)


 requirements for the description of the architecture-oriented features
of domain separation, TSF self-protection and non-bypassability of
the security functionality (ADV_ARC)


 requirements for a security policy model and for correspondence
mappings between security policy model and the functional
specification (ADV_SPM)


 requirements on the internal structure of the TSF, which covers
aspects such as modularity, layering, and minimisation of complexity
(ADV_INT)


215 When documenting the security functionality of a TOE, there are two
properties that need to be demonstrated. The first property is that the security
functionality works correctly; that is, it performs as specified. The second
property, and one that is arguably harder to demonstrate, is that the TOE
cannot be used in a way such that the security functionality can be corrupted
or bypassed. These two properties require somewhat different approaches in
analysis, and so the families in ADV are structured to support these different
approaches. The families Functional specification (ADV_FSP), TOE design
(ADV_TDS), Implementation representation (ADV_IMP), and Security
policy modelling (ADV_SPM) deal with the first property: the specification
of the security functionality. The families Security Architecture
(ADV_ARC) and TSF internals (ADV_INT) deal with the second property:
the specification of the design of the TOE demonstrating the security
functionality cannot be corrupted or bypassed. It should be noted that both
properties need to be realised: the more confidence one has that the
properties are satisfied, the more trustworthy the TOE is. The components in
the families are designed so that more assurance can be gained as the
components hierarchically increase.


216 The paradigm for the families targeted at the first property is one of design
decomposition. At the highest level, there is a functional specification of the
TSF in terms of its interfaces (describing _what_ the TSF does in terms of


Page 90 of 247 Version 3.1 April 2017


**Class ADV: Development**


requests to the TSF for services and resulting responses), decomposing the
TSF into smaller units (dependent on the assurance desired and the
complexity of the TOE) and describing _how_ the TSF accomplishes its
functions (to a level of detail commensurate with the assurance level), and
showing the implementation of the TSF. A formal model of the security
behaviour also may be given. All levels of decomposition are used in
determining the completeness and accuracy of all other levels, ensuring that
the levels are mutually supportive. The requirements for the various TSF
representations are separated into different families, to allow the PP/ST
author to specify which TSF representations are required. The level chosen
will dictate the assurance desired/gained.


217 Figure 11 indicates the relationships among the various TSF representations
of the ADV class, as well as their relationships with other classes. As the
figure indicates, the APE and ASE classes define the requirements for the
correspondence between the SFRs and the security objectives for the TOE.
Class ASE also defines requirements for the correspondence between both
the security objectives and SFRs, and for the TOE summary specification
which explains how the TOE meets its SFRs. The activities of
ALC_CMC.5.2E include the verification that the TSF that is tested under the
ATE and AVA classes is in fact the one described by all of the ADV
decomposition levels.


April 2017 Version 3.1 Page 91 of 247


**Class ADV: Development**


**Figure 11 - Relationships of ADV constructs to one another and to other**

**families**


218 The requirements for all other correspondence shown in Figure 11 are
defined in the ADV class. The Security policy modelling (ADV_SPM)
family defines the requirements for formally modelling selected SFRs, and
providing correspondence between the functional specification and the
formal model. Each assurance family specific to a TSF representation (i.e.,
Functional specification (ADV_FSP), TOE design (ADV_TDS) and
Implementation representation (ADV_IMP)) defines requirements relating
that TSF representation to the SFRs. All decompositions must accurately
reflect all other decompositions (i.e., be mutually supportive); the developer
supplies the tracings in the last .C elements of the components. Assurance
relating to this factor is obtained during the analysis for each of the levels of
decomposition by referring to other levels of decomposition (in a recursive
fashion) while the analysis of a particular level of decomposition is being
performed; the evaluator verifies the correspondence as part of the second E


Page 92 of 247 Version 3.1 April 2017


**Class ADV: Development**


element. The understanding gained from these levels of decomposition form
the basis of the functional and penetration testing efforts.


219 The ADV_INT family is not represented in this figure, as it is related to the
internal structure of the TSF, and is only indirectly related to the process of
refinement of the TSF representations. Similarly, the ADV_ARC family is
not represented in the figure because it relates to the architectural soundness,
rather than representation, of the TSF. Both ADV_INT and ADV_ARC
relate to the analysis of the property that the TOE cannot be made to
circumvent or corrupt its security functionality.


220 The TOE security functionality (TSF) consists of all parts of the TOE that
have to be relied upon for enforcement of the SFRs. The TSF includes both
functionality that directly enforces the SFRs, as well as functionality that,
while not directly enforcing the SFRs, contributes to their enforcement in a
more indirect manner, including functionality with the capability to cause the
SFRs to be violated. This includes portions of the TOE that are invoked on
start-up that are responsible for putting the TSF into its initial secure state.


221 Several important concepts were used in the development of the components
of the ADV families. These concepts, while introduced briefly here, are
explained more fully in the application notes for the families.


222 One over-riding notion is that, as more information becomes available,
greater assurance can be obtained that the security functionality 1) is
correctly implemented; 2) cannot be corrupted; and 3) cannot be bypassed.
This is done through the verification that the documentation is correct and
consistent with other documentation, and by providing information that can
be used to ensure that the testing activities (both functional and penetration
testing) are comprehensive. This is reflected in the levelling of the
components of the families. In general, components are levelled based on the
amount of information that is to be provided (and subsequently analysed).


223 While not true for all TOEs, it is generally the case that the TSF is
sufficiently complex that there are portions of the TSF that deserve more
intense examination than other portions of the TSF. Determining those
portions is unfortunately somewhat subjective, thus terminology and
components have been defined such that as the level of assurance increases,
the responsibility for determining what portions of the TSF need to be
examined in detail shifts from the developer to the evaluator. To aid in
expressing this concept, the following terminology is introduced. It should be
noted that in the families of the class, this terminology is used when
expressing SFR-related portions of the TOE (that is, elements and work units
embodied in the Functional specification (ADV_FSP), TOE design
(ADV_TDS), and Implementation representation (ADV_IMP) families).
While the general concept (that some portions of the TOE are more
_interesting_ than others) applies to other families, the criteria are expressed
differently in order to obtain the assurance required.


224 All portions of the TSF are _security relevant_, meaning that they must
preserve the security of the TOE as expressed by the SFRs and requirements


April 2017 Version 3.1 Page 93 of 247


**Class ADV: Development**


for domain separation and non-bypassability. One aspect of security
relevance is the degree to which a portion of the TSF enforces a security
requirement. Since different portions of the TOE play different roles (or no
apparent role at all) in enforcing security requirements, this creates a
continuum of SFR relevance: at one end of this continuum are portions of the
TOE that are termed _SFR-enforcing_ . Such portions play a direct role in
implementing any SFR on the TOE. Such SFRs refer to any functionality
provided by one of the SFRs contained in the ST. It should be noted that the
definition of _plays a role in_ for SFR-enforcing functionality is impossible to
express quantitatively. For example, in the implementation of a Discretionary
Access Control (DAC) mechanism, a very narrow view of _SFR-enforcing_
might be the several lines of code that actually perform the check of a
subject's attributes against the object's attributes. A broader view would
include the software entity (e.g., C function) that contained the several lines
of code. A broader view still would include callers of the C function, since
they would be responsible for enforcing the decision returned by the attribute
check. A still broader view would include any code in the call tree (or
programming equivalent for the implementation language used) for that C
function (e.g., a sort function that sorted access control list entries in a firstmatch algorithm implementation). At some point, the component is not so
much _enforcing_ the security policy but rather plays a _supporting_ role; such
components are termed _SFR supporting_ .


225 One of the characteristics of SFR-supporting functionality is that it is trusted
to preserve the correctness of the SFR implementation by operating without
error. Such functionality may be depended on by SFR-enforcing
functionality, but the dependence is generally at a functional level; for
example, memory management, buffer management, etc. Further down on
the security relevance continuum is functionality termed _SFR non-interfering_ .
Such functionality has no role in implementing the SFRs, and is likely part of
the TSF because of its environment; for example, any code running in a
privileged hardware mode on an operating system. It needs to be considered
part of the TSF because, if compromised (or replaced by malicious code), it
could compromise the correct operation of an SFR by virtue of its operating
in the privileged hardware mode. An example of SFR non-interfering
functionality might be a set of mathematical floating point operations
implemented in kernel mode for speed considerations.


226 The architecture family (Security Architecture (ADV_ARC)) provides for
requirements and analysis of the TOE based on properties of domain
separation, self-protection, and non-bypassability. These properties relate to
the SFRs in that, if these properties are not present, it will likely lead to the
failure of mechanisms implementing SFRs. Functionality and design relating
to these properties _is not_ considered a part of the continuum described above,
but instead is treated separately due to its fundamentally different nature and
analysis requirements.


227 The difference in analysis of the implementation of SFRs (SFR-enforcing
and SFR-supporting functionality) and the implementation of somewhat
fundamental security properties of the TOE, which include the initialisation,


Page 94 of 247 Version 3.1 April 2017


**Class ADV: Development**


self-protection, and non-bypassability concerns, is that the SFR-related
functionality is more or less directly visible and relatively easy to test, while
the above-mentioned properties require varying degrees of analysis on a
much broader set of functionality. Further, the depth of analysis for such
properties will vary depending on the design of the TOE. The ADV families
are constructed to address this by a separate family (Security Architecture
(ADV_ARC)) devoted to analysis of the initialisation, self-protection, and
non-bypassability requirements, while the other families are concerned with
analysis of the functionality supporting SFRs.


228 Even in cases where different descriptions are necessary for the multiple
levels of abstraction, it is not absolutely necessary for each and every TSF
representation to be in a separate document. Indeed, it may be the case that a
single document meets the documentation requirements for more than one
TSF representation, since it is the information about each of these TSF
representations that is required, rather than the resulting document structure.
In cases where multiple TSF representations are combined within a single
document, the developer should indicate which portions of the documents
meet which requirements.


229 Three types of specification style are mandated by this class: informal,
semiformal and formal. The functional specification and TOE design
documentation are always written in either informal or semiformal style. A
semiformal style reduces the ambiguity in these documents over an informal
presentation. A formal specification may also be required _in addition to_ the
semi-formal presentation; the value is that a description of the TSF in more
than one way will add increased assurance that the TSF has been completely
and accurately specified.


230 An informal specification is written as prose in natural language. Natural
language is used here as meaning communication in any commonly spoken
tongue (e.g. Spanish, German, French, English, Dutch). An informal
specification is not subject to any notational or special restrictions other than
those required as ordinary conventions for that language (e.g. grammar and
syntax). While no notational restrictions apply, the informal specification is
also required to provide defined meanings for terms that are used in a context
other than that accepted by normal usage.


231 The difference between semiformal and informal documents is only a matter
of formatting or presentation: a semiformal notation includes such things as
an explicit glossary of terms, a standardised presentation format, etc. A
semiformal specification is written to a standard presentation template. The
presentation should use terms consistently if written in a natural language.
The presentation may also use more structured languages/diagrams (e.g.
data-flow diagrams, state transition diagrams, entity-relationship diagrams,
data structure diagrams, and process or program structure diagrams).
Whether based on diagrams or natural language, a set of conventions must be
used in the presentation. The glossary explicitly identifies the words that are
being used in a precise and constant manner; similarly, the standardised
format implies that extreme care has been taken in methodically preparing
the document in a manner that maximises clarity. It should be noted that


April 2017 Version 3.1 Page 95 of 247


**Class ADV: Development**


fundamentally different portions of the TSF may have different semiformal
notation conventions and presentation styles (as long as the number of
different “semiformal notations” is small); this still conforms to the concept
of a _semiformal presentation_ .


232 A formal specification is written in a notation based upon well-established
mathematical concepts, and is typically accompanied by supporting
explanatory (informal) prose. These mathematical concepts are used to
define the syntax and semantics of the notation and the proof rules that
support logical reasoning. The syntactic and semantic rules supporting a
formal notation should define how to recognise constructs unambiguously
and determine their meaning. There needs to be evidence that it is impossible
to derive contradictions, and all rules supporting the notation need to be
defined or referenced.


233 Figure 12 shows the families within this class, and the hierarchy of
components within the families.


**Figure 12 - ADV: Development class decomposition**


Page 96 of 247 Version 3.1 April 2017


**Class ADV: Development**

## **13.1 Security Architecture (ADV_ARC)**


Objectives


234 The objective of this family is for the developer to provide a description of
the security architecture of the TSF. This will allow analysis of the
information that, when coupled with the other evidence presented for the
TSF, will confirm the TSF achieves the desired properties. The security
architecture descriptions supports the implicit claim that security analysis of
the TOE can be achieved by examining the TSF; without a sound
architecture, the entire TOE functionality would have to be examined.


Component levelling


235 This family contains only one component.


Application notes


236 The properties of self-protection, domain separation, and non-bypassability
are distinct from security functionality expressed by Part 2 SFRs because
self-protection and non-bypassability largely have no directly observable
interface at the TSF. Rather, they are properties of the TSF that are achieved
through the design of the TOE and TSF, and enforced by the correct
implementation of that design.


237 The approach used in this family is for the developer to design and provide a
TSF that exhibits the above-mentioned properties, and to provide evidence
(in the form of documentation) that explains these properties of the TSF.
This explanation is provided at the same level of detail as the description of
the SFR-enforcing elements of the TOE in the TOE design document. The
evaluator has the responsibility for looking at the evidence and, coupled with
other evidence delivered for the TOE and TSF, determining that the
properties are achieved.


238 Specification of security functionality implementing the SFRs (in the
Functional specification (ADV_FSP) and TOE design (ADV_TDS)) will not
necessarily describe mechanisms employed in implementing self-protection
and non-bypassability (e.g. memory management mechanisms). Therefore,
the material needed to provide the assurance that these requirements are
being achieved is better suited to a presentation separate from the design
decomposition of the TSF as embodied in ADV_FSP and ADV_TDS. This is
not to imply that the security architecture description called for by this
component cannot reference or make use of the design decomposition
material; but it is likely that much of the detail present in the decomposition
documentation will not be relevant to the argument being provided for the
security architecture description document.


239 The description of architectural soundness can be thought of as a developer's
vulnerability analysis, in that it provides the justification for why the TSF is
sound and enforces all of its SFRs. Where the soundness is achieved through
specific security mechanisms, these will be tested as part of the Depth


April 2017 Version 3.1 Page 97 of 247


**Class ADV: Development**


(ATE_DPT) requirements; where the soundness is achieved solely through
the architecture, the behaviour will be tested as part of the AVA:
Vulnerability assessment requirements.


240 This family consists of requirements for a security architecture description
that describes the self-protection, domain separation, non-bypassability
principles, including a description of how these principles are supported by
the parts of the TOE that are used for TSF initialisation.


241 Additional information on the security architecture properties of selfprotection, domain separation, and non-bypassability can be found in Annex
A.1, ADV_ARC: Supplementary material on security architectures.


**ADV_ARC.1** **Security architecture description**


Dependencies: ADV_FSP.1 Basic functional specification
ADV_TDS.1 Basic design


Developer action elements:


**ADV_ARC.1.1D** **The developer shall design and implement the TOE so that the security**

**features of the TSF cannot be bypassed.**


**ADV_ARC.1.2D** **The developer shall design and implement the TSF so that it is able to**

**protect itself from tampering by untrusted active entities.**


**ADV_ARC.1.3D** **The developer shall provide a security architecture description of the**

**TSF.**


Content and presentation elements:


**ADV_ARC.1.1C** **The security architecture description shall be at a level of detail**

**commensurate with the description of the SFR-enforcing abstractions**
**described in the TOE design document.**


**ADV_ARC.1.2C** **The security architecture description shall describe the security domains**

**maintained by the TSF consistently with the SFRs.**


**ADV_ARC.1.3C** **The security architecture description shall describe how the TSF**

**initialisation process is secure.**


**ADV_ARC.1.4C** **The security architecture description shall demonstrate that the TSF**

**protects itself from tampering.**


**ADV_ARC.1.5C** **The security architecture description shall demonstrate that the TSF**

**prevents bypass of the SFR-enforcing functionality.**


Evaluator action elements:


**ADV_ARC.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


Page 98 of 247 Version 3.1 April 2017


**Class ADV: Development**

## **13.2 Functional specification (ADV_FSP)**


Objectives


242 This family levies requirements upon the functional specification, which
describes the TSF interfaces (TSFIs). The TSFI consists of all means by
which external entities (or subjects in the TOE but outside of the TSF)
supply data to the TSF, receive data from the TSF and invoke services from
the TSF. It does _not_ describe how the TSF processes those service requests,
nor does it describe the communication when the TSF invokes services from
its operational environment; this information is addressed by the TOE design
(ADV_TDS) and Reliance of dependent component (ACO_REL) families,
respectively.


243 This family provides assurance directly by allowing the evaluator to
understand how the TSF meets the claimed SFRs. It also provides assurance
indirectly, as input to other assurance families and classes:


 ADV_ARC, where the description of the TSFIs may be used to gain
better understanding of how the TSF is protected against corruption
(i.e. subversion of self-protection or domain separation) and/or
bypass;


 ATE, where the description of the TSFIs is an important input for
both developer and evaluator testing;


 AVA, where the description of the TSFIs is used to search for
vulnerabilities.


Component levelling


244 The components in this family are levelled on the degree of detail required of
the description of the TSFIs, and the degree of formalism required of the
description of the TSFIs.


Application notes


245 Once the TSFIs are determined (see A.2.1, Determining the TSFI for
guidance and examples of determining TSFI), they are described. At lowerlevel components, developers focus their documentation (and evaluators
focus their analysis) on the more security-relevant aspects of the TOE. Three
categories of TSFIs are defined, based upon the relevance the services
available through them have to the SFRs being claimed:


 If a service available through an interface can be traced to one of the
SFRs levied on the TSF, then that interface is termed _SFR-enforcing_ .
Note that it is possible that an interface may have various services
and results, some of which may be SFR-enforcing and some of which
may not.


April 2017 Version 3.1 Page 99 of 247


**Class ADV: Development**


 interfaces to (or services available through an interface relating to)
services that SFR-enforcing functionality depends upon, but need
only to function correctly in order for the security policies of the TOE
to be preserved, are termed _SFR-supporting_ .


 Interfaces to services on which SFR-enforcing functionality has no
dependence are termed _SFR non-interfering_ .


246 It should be noted that in order for an interface to be SFR-supporting or SFR
non-interfering it must have _no_ SFR-enforcing services or results. In contrast,
an SFR-enforcing interface may have SFR-supporting services (for example,
the ability to set the system clock may be an SFR-enforcing service of an
interface, but if that same interface is used to display the system date that
service may be only SFR-supporting). An example of a purely SFRsupporting interface is a system call interface that is used both by users and
by a portion of the TSF that is running on behalf of users.


247 As more information about the TSFIs becomes available, the greater the
assurance that can be gained that the interfaces are correctly
categorised/analysed. The requirements are structured such that, at the lowest
level, the information required for SFR non-interfering interfaces is the
minimum necessary in order for the evaluator to make this determination in
an effective manner. At higher levels, more information becomes available
so that the evaluator has greater confidence in the designation.


248 The purpose in defining these labels (SFR-enforcing, SFR-supporting, and
SFR-non-interfering) and for levying different requirements upon each (at
the lower assurance components) is to provide a first approximation of where
to focus the analysis and the evidence upon which that analysis is performed.
If the developer's documentation of the TSF interfaces describes all of the
interfaces to the degree specified in the requirements for the SFR-enforcing
interfaces (that is, if the documentation exceeds the requirements), there is no
need for the developer to create new evidence to match the requirements.
Similarly, because the labels are merely a means of differentiating the
interface types within the requirements, there is no need for the developer to
update the evidence solely to label the interfaces as SFR-enforcing, SFRsupporting, and SFR-non-interfering. The primary purpose of this labelling is
to allow developers with less mature development methodologies (and
associated artifacts, such as detailed interface and design documentation) to
provide only the necessary evidence without undue cost.


249 The last C element of each component within this family provides a direct
correspondence between the SFRs and the functional specification; that is, an
indication of which interfaces are used to invoke each of the claimed SFRs.
In the cases where the ST contains such functional requirements as Residual
information protection (FDP_RIP), whose functionality may not manifest
itself at the TSFIs, the functional specification and/or the tracing is expected
to identify these SFRs; including them in the functional specification helps to
ensure that they are not lost at lower levels of decomposition, where they
will be relevant.


Page 100 of 247 Version 3.1 April 2017


**Class ADV: Development**


**13.2.1** **Detail about the Interfaces**


250 The requirements define collections of details about TSFI to be provided. For
the purposes of the requirements, interfaces are specified (in varying degrees
of detail) in terms of their purpose, method of use, parameters, parameter
descriptions, and error messages.


251 The _purpose_ of an interface is a high-level description of the general goal of
the interface (e.g. process GUI commands, receive network packets, provide
printer output, etc.)


252 The interface's _method of use_ describes how the interface is supposed to be
used. This description should be built around the various interactions
available at that interface. For instance, if the interface were a Unix
command shell, _ls_, _mv_ and _cp_ would be interactions for that interface. For
each interaction the method of use describes what the interaction does, both
for behaviour seen at the interface (e.g. the programmer calling the API, the
Windows users changing a setting in the registry, etc.) as well as behaviour
at other interfaces (e.g. generating an audit record).


253 _Parameters_ are explicit inputs to and outputs from an interface that control
the behaviour of that interface. For example, parameters are the arguments
supplied to an API; the various fields in a packet for a given network
protocol; the individual key values in the Windows Registry; the signals
across a set of pins on a chip; the flags that can be set for the _ls_, etc. The
parameters are “identified” with a simple list of what they are.


254 A _parameter description_ tells what the parameter is in some meaningful way.
For instance, an acceptable parameter description for interface _foo(i)_ would
be “parameter i is an integer that indicates the number of users currently
logged in to the system”. A description such as “parameter i is an integer” is
not an acceptable.


255 The description of an interface's _actions_ describes what the interface does.
This is more detailed than the purpose in that, while the “purpose” reveals
why one might want to use it, the “actions” reveals everything that it does.
These actions might be related to the SFRs or not. In cases where the
interface's action is not related to SFRs, its description is said to be
_summarised_, meaning the description merely makes clear that it is indeed not
SFR-related.


256 The _error message description_ identifies the condition that generated it, what
the message is, and the meaning of any error codes. An error message is
generated by the TSF to signify that a problem or irregularity of some degree
has been encountered. The requirements in this family refer to different kinds
of error messages:


 a “direct” error message is a security-relevant response through a
specific TSFI invocation.


April 2017 Version 3.1 Page 101 of 247


**Class ADV: Development**


 an “indirect” error cannot be tied to a specific TSFI invocation
because it results from system-wide conditions (e.g. resource
exhaustion, connectivity interruptions, etc.). Error messages that are
not security-relevant are also considered “indirect”.


 “remaining” errors are any other errors, such as those that might be
referenced within the code. For example, the use of conditionchecking code that checks for conditions that would not logically
occur (e.g. a final “else” after a list of “case” statements), would
provide for generating a catch-all error message; in an operational
TOE, these error messages should never be seen.


257 An example functional specification is provided in A.2.3.


**13.2.2** **Components of this Family**


258 Increasing assurance through increased completeness and accuracy in the
interface specification is reflected in the documentation required from the
developer as detailed in the various hierarchical components of this family.


259 At ADV_FSP.1 Basic functional specification, the only documentation
required is a characterisation of all TSFIs and a high level description of
SFR-enforcing and SFR-supporting TSFIs. To provide some assurance that
the “important” aspects of the TSF have been correctly characterised at the
TSFIs, the developer is required to provide the purpose and method of use,
parameters for the SFR-enforcing and SFR-supporting TSFIs.


260 At ADV_FSP.2 Security-enforcing functional specification, the developer is
required to provide the purpose, method of use, parameters, and parameter
descriptions for all TSFIs. Additionally, for the SFR-enforcing TSFIs the
developer has to describe the SFR-enforcing actions and direct error
messages.


261 At ADV_FSP.3 Functional specification with complete summary, the
developer must now, in addition to the information required at ADV_FSP.2,
provide enough information about the SFR-supporting and SFR-noninterfering actions to show that they are not SFR-enforcing. Further, the
developer must now document all of the direct error messages resulting from
the invocation of SFR-enforcing TSFIs.


262 At ADV_FSP.4 Complete functional specification, all TSFIs - whether SFRenforcing, SFR-supporting, SFR-non-interfering - must be described to the
same degree, including all of the direct error messages.


263 At ADV_FSP.5 Complete semi-formal functional specification with
additional error information, the TSFIs descriptions also include error
messages that do not result from an invocation of a TSFI.


264 At ADV_FSP.6 Complete semi-formal functional specification with
additional formal specification, in addition to the information required by
ADV_FSP.5, all remaining error messages are included. The developer must


Page 102 of 247 Version 3.1 April 2017


**Class ADV: Development**


also provide a formal description of the TSFI. This provides an alternative
view of the TSFI that may expose inconsistencies or incomplete specification.


**ADV_FSP.1** **Basic functional specification**


Dependencies: No dependencies.


Developer action elements:


**ADV_FSP.1.1D** **The developer shall provide a functional specification.**


**ADV_FSP.1.2D** **The developer shall provide a tracing from the functional specification**

**to the SFRs.**


Content and presentation elements:


**ADV_FSP.1.1C** **The functional specification shall describe the purpose and method of**

**use for each SFR-enforcing and SFR-supporting TSFI.**


**ADV_FSP.1.2C** **The functional specification shall identify all parameters associated with**

**each SFR-enforcing and SFR-supporting TSFI.**


**ADV_FSP.1.3C** **The functional specification shall provide rationale for the implicit**

**categorisation of interfaces as SFR-non-interfering.**


**ADV_FSP.1.4C** **The tracing shall demonstrate that the SFRs trace to TSFIs in the**

**functional specification.**


Evaluator action elements:


**ADV_FSP.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


**ADV_FSP.1.2E** **The evaluator** _**shall determine**_ **that the functional specification is an**

**accurate and complete instantiation of the SFRs.**


**ADV_FSP.2** **Security-enforcing functional specification**


Dependencies: ADV_TDS.1 Basic design


Developer action elements:


**ADV_FSP.2.1D** The developer shall provide a functional specification.


**ADV_FSP.2.2D** The developer shall provide a tracing from the functional specification to the

SFRs.


Content and presentation elements:


**ADV_FSP.2.1C** **The functional specification shall completely represent the TSF.**


**ADV_FSP.2.2C** The functional specification shall describe the purpose and method of use for

**all** TSFI.


April 2017 Version 3.1 Page 103 of 247


**Class ADV: Development**


**ADV_FSP.2.3C** The functional specification shall identify **and** **describe** all parameters

associated with each TSFI.


**ADV_FSP.2.4C** **For** **each** **SFR-enforcing** **TSFI,** **the** functional specification shall **describe**

**the** **SFR-enforcing** **actions** associated with **the** TSFI.


**ADV_FSP.2.5C** **For each SFR-enforcing TSFI, the functional specification shall describe**

**direct error messages resulting from processing associated with the**
**SFR-enforcing actions.**


**ADV_FSP.2.6C** The tracing shall demonstrate that the SFRs trace to TSFIs in the functional

specification.


Evaluator action elements:


**ADV_FSP.2.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


**ADV_FSP.2.2E** The evaluator _**shall determine**_ that the functional specification is an accurate

and complete instantiation of the SFRs.


**ADV_FSP.3** **Functional specification with complete summary**


Dependencies: ADV_TDS.1 Basic design


Developer action elements:


**ADV_FSP.3.1D** The developer shall provide a functional specification.


**ADV_FSP.3.2D** The developer shall provide a tracing from the functional specification to the

SFRs.


Content and presentation elements:


**ADV_FSP.3.1C** The functional specification shall completely represent the TSF.


**ADV_FSP.3.2C** The functional specification shall describe the purpose and method of use for

all TSFI.


**ADV_FSP.3.3C** The functional specification shall identify and describe all parameters

associated with each TSFI.


**ADV_FSP.3.4C** For each SFR-enforcing TSFI, the functional specification shall describe the

SFR-enforcing actions associated with the TSFI.


**ADV_FSP.3.5C** For each SFR-enforcing TSFI, the functional specification shall describe

direct error messages resulting from **SFR-enforcing** **actions** **and** **exceptions**
associated with **invocation** **of** the **TSFI.**


**ADV_FSP.3.6C** **The functional specification shall summarise the SFR-supporting and**

**SFR-non-interfering actions associated with each TSFI.**


Page 104 of 247 Version 3.1 April 2017


**Class ADV: Development**


**ADV_FSP.3.7C** The tracing shall demonstrate that the SFRs trace to TSFIs in the functional

specification.


Evaluator action elements:


**ADV_FSP.3.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


**ADV_FSP.3.2E** The evaluator _**shall determine**_ that the functional specification is an accurate

and complete instantiation of the SFRs.


**ADV_FSP.4** **Complete functional specification**


Dependencies: ADV_TDS.1 Basic design


Developer action elements:


**ADV_FSP.4.1D** The developer shall provide a functional specification.


**ADV_FSP.4.2D** The developer shall provide a tracing from the functional specification to the

SFRs.


Content and presentation elements:


**ADV_FSP.4.1C** The functional specification shall completely represent the TSF.


**ADV_FSP.4.2C** The functional specification shall describe the purpose and method of use for

all TSFI.


**ADV_FSP.4.3C** The functional specification shall identify and describe all parameters

associated with each TSFI.


**ADV_FSP.4.4C** The functional specification shall **describe** **all** actions associated with each

TSFI.


**ADV_FSP.4.5C** **The functional specification shall describe all direct error messages that**

**may result from an invocation of each TSFI.**


**ADV_FSP.4.6C** The tracing shall demonstrate that the SFRs trace to TSFIs in the functional

specification.


Evaluator action elements:


**ADV_FSP.4.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


**ADV_FSP.4.2E** The evaluator _**shall determine**_ that the functional specification is an accurate

and complete instantiation of the SFRs.


April 2017 Version 3.1 Page 105 of 247


**Class ADV: Development**


**ADV_FSP.5** **Complete semi-formal functional specification with additional**

**error information**


Dependencies: ADV_TDS.1 Basic design
ADV_IMP.1 Implementation representation of the
TSF


Developer action elements:


**ADV_FSP.5.1D** The developer shall provide a functional specification.


**ADV_FSP.5.2D** The developer shall provide a tracing from the functional specification to the

SFRs.


Content and presentation elements:


**ADV_FSP.5.1C** The functional specification shall completely represent the TSF.


**ADV_FSP.5.2C** **The functional specification shall describe the TSFI using a semi-formal**

**style.**


**ADV_FSP.5.3C** The functional specification shall describe the purpose and method of use for

all TSFI.


**ADV_FSP.5.4C** The functional specification shall identify and describe all parameters

associated with each TSFI.


**ADV_FSP.5.5C** The functional specification shall describe all actions associated with each

TSFI.


**ADV_FSP.5.6C** The functional specification shall describe all direct error messages that may

result from an invocation of each TSFI.


**ADV_FSP.5.7C** **The functional specification shall describe all error messages that do not**

**result from an invocation of a TSFI.**


**ADV_FSP.5.8C** **The functional specification shall provide a rationale for each error**

**message contained in the TSF implementation yet does not result from**
**an invocation of a TSFI.**


**ADV_FSP.5.9C** The tracing shall demonstrate that the SFRs trace to TSFIs in the functional

specification.


Evaluator action elements:


**ADV_FSP.5.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


**ADV_FSP.5.2E** The evaluator _**shall determine**_ that the functional specification is an accurate

and complete instantiation of the SFRs.


Page 106 of 247 Version 3.1 April 2017


**Class ADV: Development**


**ADV_FSP.6** **Complete semi-formal functional specification with additional**

**formal specification**


Dependencies: ADV_TDS.1 Basic design
ADV_IMP.1 Implementation representation of the
TSF


Developer action elements:


**ADV_FSP.6.1D** The developer shall provide a functional specification.


**ADV_FSP.6.2D** **The developer shall provide a formal presentation of the functional**

**specification of the TSF.**


**ADV_FSP.6.3D** The developer shall provide a tracing from the functional specification to the

SFRs.


Content and presentation elements:


**ADV_FSP.6.1C** The functional specification shall completely represent the TSF.


**ADV_FSP.6.2C** The functional specification shall describe the TSFI using a **formal** style.


**ADV_FSP.6.3C** The functional specification shall describe the purpose and method of use for

all TSFI.


**ADV_FSP.6.4C** The functional specification shall identify and describe all parameters

associated with each TSFI.


**ADV_FSP.6.5C** The functional specification shall describe all actions associated with each

TSFI.


**ADV_FSP.6.6C** The functional specification shall describe all direct error messages that may

result from an invocation of each TSFI.


**ADV_FSP.6.7C** The functional specification shall describe all error messages **contained** **in**

**the** **TSF** **implementation** **representation.**


**ADV_FSP.6.8C** The functional specification shall provide a rationale for each error message

contained in the TSF implementation **that** **is** not **otherwise** **described** **in** **the**
**functional** **specification** **justifying** **why** **it** **is** **not** **associated** **with** a TSFI.


**ADV_FSP.6.9C** **The formal presentation of the functional specification of the TSF shall**

**describe the TSFI using a formal style, supported by informal,**
**explanatory text where appropriate.**


**ADV_FSP.6.10C** The tracing shall demonstrate that the SFRs trace to TSFIs in the functional

specification.


April 2017 Version 3.1 Page 107 of 247


**Class ADV: Development**


Evaluator action elements:


**ADV_FSP.6.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


**ADV_FSP.6.2E** The evaluator _**shall determine**_ that the functional specification is an accurate

and complete instantiation of the SFRs.


Page 108 of 247 Version 3.1 April 2017


**Class ADV: Development**

## **13.3 Implementation representation (ADV_IMP)**


Objectives


265 The function of the Implementation representation (ADV_IMP) family is for
the developer to make available the implementation representation (and, at
higher levels, the implementation itself) of the TOE in a form that can be
analysed by the evaluator. The implementation representation is used in
analysis activities for other families (analysing the TOE design, for instance)
to demonstrate that the TOE conforms its design and to provide a basis for
analysis in other areas of the evaluation (e.g., the search for vulnerabilities).
The implementation representation is expected to be in a form that captures
the detailed internal workings of the TSF. This may be software source code,
firmware source code, hardware diagrams and/or IC hardware design
language code or layout data.


Component levelling


266 The components in this family are levelled on the amount of implementation
that is mapped to the TOE design description.


Application notes


267 Source code or hardware diagrams and/or IC hardware design language code
or layout data that are used to build the actual hardware are examples of parts
of an implementation representation. It is important to note that while the
implementation representation must be made available to the evaluator, this
does not imply that the evaluator needs to possess that representation. For
instance, the developer may require that the evaluator review the
implementation representation at a site of the developer's choosing.


268 The entire implementation representation is made available to ensure that
analysis activities are not curtailed due to lack of information. This does not,
however, imply that all of the representation is examined when the analysis
activities are being performed. This is likely impractical in almost all cases,
in addition to the fact that it most likely will not result in a higher-assurance
TOE vs. targeted sampling of the implementation representation. The
implementation representation is made available to allow analysis of other
TOE design decompositions (e.g., functional specification, TOE design), and
to gain confidence that the security functionality described at a higher level
in the design actually appear to be implemented in the TOE. Conventions in
some forms of the implementation representation may make it difficult or
impossible to determine from just the implementation representation itself
what the actual result of the compilation or run-time interpretation will be.
For example, compiler directives for C language compilers will cause the
compiler to exclude or include entire portions of the code. For this reason, it
is important that such “extra” information or related tools (scripts, compilers,
etc.) be provided so that the implementation representation can be accurately
determined.


April 2017 Version 3.1 Page 109 of 247


**Class ADV: Development**


269 The purpose of the mapping between the implementation representation and
the TOE design description is to aid the evaluator's analysis. The internal
workings of the TOE may be better understood when the TOE design is
analysed with corresponding portions of the implementation representation.
The mapping serves as an index into the implementation representation. At
the lower component, only a subset of the implementation representation is
mapped to the TOE design description. Because of the uncertainty of which
portions of the implementation representation will need such a mapping, the
developer may choose either to map the entire implementation representation
beforehand, or to wait to see which portions of the implementation
representation the evaluator requires to be mapped.


270 The implementation representation is manipulated by the developer in a form
that is suitable for transformation to the actual implementation. For instance,
the developer may work with files containing source code, which is
eventually compiled to become part of the TSF. The developer makes
available the implementation representation in the form used by the
developer, so that the evaluator may use automated techniques in the analysis.
This also increases the confidence that the implementation representation
examined is actually the one used in the production of the TSF (as opposed
to the case where it is supplied in an alternate presentation format, such as a
word processor document). It should be noted that other forms of the
implementation representation may also be used by the developer; these
forms are supplied as well. The overall goal is to supply the evaluator with
the information that will maximise the effectiveness of the evaluator's
analysis efforts.


271 Some forms of the implementation representation may require additional
information because they introduce significant barriers to understanding and
analysis. Examples include “shrouded” source code or source code that has
been obfuscated in other ways such that it prevents understanding and/or
analysis. These forms of implementation representation typically result from
the TOE developer taking a version of the implementation representation and
running a shrouding or obfuscation program on it. While the shrouded
representation is what is compiled and may be closer to the implementation
(in terms of structure) than the original, un-shrouded representation,
supplying such obfuscated code may cause significantly more time to be
spent in analysis tasks involving the representation. When such forms of
representation are created, the components require details on the shrouding
tools/algorithms used so that the un-shrouded representation can be supplied,
and the additional information can be used to gain confidence that the
shrouding process does not compromise any security functionality.


**ADV_IMP.1** **Implementation representation of the TSF**


Dependencies: ADV_TDS.3 Basic modular design
ALC_TAT.1 Well-defined development tools


Page 110 of 247 Version 3.1 April 2017


**Class ADV: Development**


Developer action elements:


**ADV_IMP.1.1D** **The developer shall make available the implementation representation**

**for the entire TSF.**


**ADV_IMP.1.2D** **The developer shall provide a mapping between the TOE design**

**description and the sample of the implementation representation.**


Content and presentation elements:


**ADV_IMP.1.1C** **The implementation representation shall define the TSF to a level of**

**detail such that the TSF can be generated without further design**
**decisions.**


**ADV_IMP.1.2C** **The implementation representation shall be in the form used by the**

**development personnel.**


**ADV_IMP.1.3C** **The mapping between the TOE design description and the sample of the**

**implementation representation shall demonstrate their correspondence.**


Evaluator action elements:


**ADV_IMP.1.1E** **The evaluator** _**shall confirm**_ **that, for the selected sample of the**

**implementation representation, the information provided meets all**
**requirements for content and presentation of evidence.**


**ADV_IMP.2** **Complete mapping of the implementation representation of the**

**TSF**


Dependencies: ADV_TDS.3 Basic modular design
ALC_TAT.1 Well-defined development tools
ALC_CMC.5 Advanced support


Developer action elements:


**ADV_IMP.2.1D** The developer shall make available the implementation representation for the

entire TSF.


**ADV_IMP.2.2D** The developer shall provide a mapping between the TOE design description

and the **entire** implementation representation.


Content and presentation elements:


**ADV_IMP.2.1C** The implementation representation shall define the TSF to a level of detail

such that the TSF can be generated without further design decisions.


**ADV_IMP.2.2C** The implementation representation shall be in the form used by the

development personnel.


**ADV_IMP.2.3C** The mapping between the TOE design description and the **entire**

implementation representation shall demonstrate their correspondence.


April 2017 Version 3.1 Page 111 of 247


**Class ADV: Development**


Evaluator action elements:


**ADV_IMP.2.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


Page 112 of 247 Version 3.1 April 2017


**Class ADV: Development**

## **13.4 TSF internals (ADV_INT)**


Objectives


272 This family addresses the assessment of the internal structure of the TSF. A
TSF whose internals are well-structured is easier to implement and less likely
to contain flaws that could lead to vulnerabilities; it is also easier to maintain
without the introduction of flaws.


Component levelling


273 The components in this family are levelled on the basis of the amount of
structure and minimisation of complexity required. ADV_INT.1 Wellstructured subset of TSF internals places requirements for well-structured
internals on only selected parts of the TSF. This component is not included
in an EAL because this component is viewed for use in special circumstances
(e.g., the sponsor has a specific concern regarding a cryptographic module,
which is isolated from the rest of the TSF) and would not be widely
applicable.


274 At the next level, the requirements for well-structured internals are placed on
the entire TSF. Finally, minimisation of complexity is introduced in the
highest component.


Application notes


275 These requirements, when applied to the internal structure of the TSF,
typically result in improvements that aid both the developer and the evaluator
in understanding the TSF, and also provide the basis for designing and
evaluating test suites. Further, improving understandability of the TSF
should assist the developer in simplifying its maintainability.


276 The requirements in this family are presented at a fairly abstract level. The
wide variety of TOEs makes it impossible to codify anything more specific
than “well-structured” or “minimum complexity”. Judgements on structure
and complexity are expected to be derived from the specific technologies
used in the TOE. For example, software is likely to be considered wellstructured if it exhibits the characteristics cited in the software engineering
disciplines. The components within this family call for identifying the
standards for measuring the characteristic of being well-structured and not
overly-complex.


**ADV_INT.1** **Well-structured subset of TSF internals**


Dependencies: ADV_IMP.1 Implementation representation of the
TSF
ADV_TDS.3 Basic modular design
ALC_TAT.1 Well-defined development tools


April 2017 Version 3.1 Page 113 of 247


**Class ADV: Development**


Objectives


277 The objective of this component is to provide a means for requiring specific
portions of the TSF to be well-structured. The intent is that the entire TSF
has been designed and implemented using sound engineering principles, but
the analysis is performed upon only a specific subset.


Application notes


278 This component requires the PP or ST author to fill in an assignment with the
subset of the TSF. This subset may be identified in terms of the internals of
the TSF at any layer of abstraction. For example:


a) the structural elements of the TSF as identified in the TOE design
(e.g. “The developer shall design and implement _the audit subsystem_
such that it has well-structured internals.”)


b) the implementation (e.g. “The developer shall design and implement
_the encrypt.c and decrypt.c files_ such that it has well-structured
internals.” or “The developer shall design and implement _the 6227 IC_
_chip_ such that it has well-structured internals.”)


279 It is likely this would not be readily accomplished by referencing the claimed
SFRs (e.g. “The developer shall design and implement _the portion of the TSF_
_that provide anonymity as defined in FPR_ANO.2_ such that it has wellstructured internals.”) because this does not indicate where to focus the
analysis.


280 This component has limited value and would be suitable in cases where
potentially-malicious users/subjects have limited or strictly controlled access
to the TSFIs or where there is another means of protection (e.g., domain
separation) that ensures the chosen subset of the TSF cannot be adversely
affected by the rest of the TSF (e.g., the cryptographic functionality, which is
isolated from the rest of the TSF, is well-structured).


Developer action elements:


**ADV_INT.1.1D** **The developer shall design and implement [assignment:** _**subset of the**_

_**TSF**_ **] such that it has well-structured internals.**


**ADV_INT.1.2D** **The developer shall provide an internals description and justification.**


Content and presentation elements:


**ADV_INT.1.1C** **The justification shall explain the characteristics used to judge the**

**meaning of “well-structured”.**


**ADV_INT.1.2C** **The TSF internals description shall demonstrate that the assigned subset**

**of the TSF is well-structured.**


Page 114 of 247 Version 3.1 April 2017


**Class ADV: Development**


Evaluator action elements:


**ADV_INT.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


**ADV_INT.1.2E** **The evaluator** _**shall perform**_ **an internals analysis on the assigned subset**

**of the TSF.**


**ADV_INT.2** **Well-structured internals**


Dependencies: ADV_IMP.1 Implementation representation of the
TSF
ADV_TDS.3 Basic modular design
ALC_TAT.1 Well-defined development tools


Objectives


281 The objective of this component is to provide a means for requiring the TSF
to be well-structured. The intent is that the entire TSF has been designed and
implemented using sound engineering principles.


Application notes


282 Judgements on the adequacy of the structure are expected to be derived from
the specific technologies used in the TOE. This component calls for
identifying the standards for measuring the characteristic of being wellstructured.


Developer action elements:


**ADV_INT.2.1D** The developer shall design and implement the **entire** **TSF** such that it has

well-structured internals.


**ADV_INT.2.2D** The developer shall provide an internals description and justification.


Content and presentation elements:


**ADV_INT.2.1C** The justification shall **describe** the characteristics used to judge the meaning

of “well-structured”.


**ADV_INT.2.2C** The TSF internals description shall demonstrate that the **entire** TSF is well
structured.


Evaluator action elements:


**ADV_INT.2.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


**ADV_INT.2.2E** The evaluator _**shall perform**_ an internals analysis on the TSF.


April 2017 Version 3.1 Page 115 of 247


**Class ADV: Development**


**ADV_INT.3** **Minimally complex internals**


Dependencies: ADV_IMP.1 Implementation representation of the
TSF
ADV_TDS.3 Basic modular design
ALC_TAT.1 Well-defined development tools


Objectives


283 The objective of this component is to provide a means for requiring the TSF
to be well-structured and of minimal complexity. The intent is that the entire
TSF has been designed and implemented using sound engineering principles.


Application notes


284 Judgements on the adequacy of the structure and complexity are expected to
be derived from the specific technologies used in the TOE. This component
calls for identifying the standards for measuring the structure and complexity.


Developer action elements:


**ADV_INT.3.1D** The developer shall design and implement the entire TSF such that it has

well-structured internals.


**ADV_INT.3.2D** The developer shall provide an internals description and justification.


Content and presentation elements:


**ADV_INT.3.1C** The justification shall describe the characteristics used to judge the meaning

of “well-structured” **and** **“complex”.**


**ADV_INT.3.2C** The TSF internals description shall demonstrate that the entire TSF is well
structured **and** **is** **not** **overly** **complex.**


Evaluator action elements:


**ADV_INT.3.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


**ADV_INT.3.2E** The evaluator _**shall perform**_ an internals analysis on the **entire** TSF.


Page 116 of 247 Version 3.1 April 2017


**Class ADV: Development**

## **13.5 Security policy modelling (ADV_SPM)**


Objectives


285 It is the objective of this family to provide additional assurance from the
development of a formal _security policy model_ of the TSF, and establishing a
correspondence between the functional specification and this security policy
model. Preserving internal consistency the security policy model is expected
to formally establish the security principles from its characteristics by means
of a mathematical proof.


Component levelling


286 This family contains only one component.


Application notes


287 Inadequacies in a TOE can result either from a failure in understanding the
security requirements or from a flawed implementation of those security
requirements. Defining the security requirements adequately to ensure their
understanding may be problematic because the definition must be
sufficiently precise to prevent undesired results or subtle flaws during
implementation of the TOE. Throughout the design, implementation, and
review processes, the modelled security requirements may be used as precise
design and implementation guidance, thereby providing increased assurance
that the modelled security requirements are satisfied by the TOE. The
precision of the model and resulting guidance is significantly improved by
casting the model in a formal language and verifying the security
requirements by formal proof.


288 The creation of a formal security policy model helps to identify and
eliminate ambiguous, inconsistent, contradictory, or unenforceable security
policy elements. Once the TOE has been built, the formal model serves the
evaluation effort by contributing to the evaluator's judgement of how well
the developer has understood the security functionality being implemented
and whether there are inconsistencies between the security requirements and
the TOE design. The confidence in the model is accompanied by a proof that
it contains no inconsistencies.


289 A formal security model is a precise formal presentation of the important
aspects of security and their relationship to the behaviour of the TOE; it
identifies the set of rules and practises that regulates how the TSF manages,
protects, and otherwise controls the system resources. The model includes
the set of restrictions and properties that specify how information and
computing resources are prevented from being used to violate the SFRs,
accompanied by a persuasive set of engineering arguments showing that
these restrictions and properties play a key role in the enforcement of the
SFRs. It consists both of the formalisms that express the security
functionality, as well as ancillary text to explain the model and to provide it
with context. The security behaviour of the TSF is modelled both in terms of


April 2017 Version 3.1 Page 117 of 247


**Class ADV: Development**


external behaviour (i.e. how the TSF interacts with the rest of the TOE and
with its operational environment), as well as its internal behaviour.


290 The Security Policy Model of the TOE is informally abstracted from its
realisation by considering the proposed security requirements of the ST. The
informal abstraction is taken to be successful if the TOE's principles (also
termed “invariants”) turn out to be enforced by its characteristics. The
purpose of formal methods lies within the enhancement of the rigour of
enforcement. Informal arguments are always prone to fallacies; especially if
relationships among subjects, objects and operations get more and more
involved. In order to minimise the risk of insecure state arrivals the rules and
characteristics of the security policy model are mapped to respective
properties and features within some formal system, whose rigour and
strength can afterwards be used to obtain the security properties by means of
theorems and formal proof.


291 While the term “formal security policy model” is used in academic circles,
the CC's approach has no fixed definition of “security”; it would equate to
whatever SFRs are being claimed. Therefore, the formal security policy
model is merely a formal representation of the set of SFRs being claimed.


292 The term _security policy_ has traditionally been associated with only access
control policies, whether label-based (mandatory access control) or userbased (discretionary access control). However, a security policy is not
limited to access control; there are also audit policies, identification policies,
authentication policies, encryption policies, management policies, and any
other security policies that are enforced by the TOE, as described in the
PP/ST. **ADV_SPM.1.1D** contains an assignment for identifying these policies
that are formally modelled.


**ADV_SPM.1** **Formal TOE security policy model**


Dependencies: ADV_FSP.4 Complete functional specification


Developer action elements:


**ADV_SPM.1.1D** **The developer shall provide a formal security policy model for the**

**[assignment:** _**list of policies that are formally modelled**_ **].**


**ADV_SPM.1.2D** **For each policy covered by the formal security policy model, the model**

**shall identify the relevant portions of the statement of SFRs that make**
**up that policy.**


**ADV_SPM.1.3D** **The developer shall provide a formal proof of correspondence between**

**the model and any formal functional specification.**


**ADV_SPM.1.4D** **The developer shall provide a demonstration of correspondence between**

**the model and the functional specification.**


Page 118 of 247 Version 3.1 April 2017


**Class ADV: Development**


Content and presentation elements:


**ADV_SPM.1.1C** **The model shall be in a formal style, supported by explanatory text as**

**required, and identify the security policies of the TSF that are modelled.**


**ADV_SPM.1.2C** **For all policies that are modelled, the model shall define security for the**

**TOE and provide a formal proof that the TOE cannot reach a state that**
**is not secure.**


**ADV_SPM.1.3C** **The correspondence between the model and the functional specification**

**shall be at the correct level of formality.**


**ADV_SPM.1.4C** **The correspondence shall show that the functional specification is**

**consistent and complete with respect to the model.**


Evaluator action elements:


**ADV_SPM.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


April 2017 Version 3.1 Page 119 of 247


**Class ADV: Development**

## **13.6 TOE design (ADV_TDS)**


Objectives


293 The design description of a TOE provides both context for a description of
the TSF, and a thorough description of the TSF. As assurance needs increase,
the level of detail provided in the description also increases. As the size and
complexity of the TSF increase, multiple levels of decomposition are
appropriate. The design requirements are intended to provide information
(commensurate with the given assurance level) so that a determination can be
made that the security functional requirements are realised.


Component levelling


294 The components in this family are levelled on the basis of the amount of
information that is required to be presented with respect to the TSF, and on
the degree of formalism required of the design description.


Application notes


295 The goal of design documentation is to provide sufficient information to
determine the TSF boundary, and to describe _how_ the TSF implements the
Security Functional Requirements. The amount and structure of the design
documentation will depend on the complexity of the TOE and the number of
SFRs; in general, a very complex TOE with a large number of SFRs will
require more design documentation than a very simple TOE implementing
only a few SFRs. Very complex TOEs will benefit (in terms of the assurance
provided) from the production of differing levels of decomposition in
describing the design, while very simple TOEs do not require both high-level
and low-level descriptions of its implementation.


296 This family uses two levels of decomposition: the _subsystem_ and the _module_ .
A module is the most specific description of functionality: it is a description
of the implementation. A developer should be able to implement the part of
the TOE described by the module with no further design decisions. A
subsystem is a description of the design of the TOE; it helps to provide a
high-level description of what a portion of the TOE is doing and how. As
such, a subsystem may be further divided into lower-level subsystems, or
into modules. Very complex TOEs might require several levels of
subsystems in order to adequately convey a useful description of how the
TOE works. Very simple TOEs, in contrast, might not require a subsystem
level of description; the module might clearly describe how the TOE works.


297 The general approach adopted for design documentation is that, as the level
of assurance increases, the emphasis of description shifts from the general
(subsystem level) to more (module level) detail. In cases where a modulelevel of abstraction is appropriate because the TOE is simple enough to be
described at the module level, yet the level of assurance calls for a subsystem
level of description, the module-level description alone will suffice. For
complex TOEs, however, this is not the case: an enormous amount of


Page 120 of 247 Version 3.1 April 2017


**Class ADV: Development**


(module-level) detail would be incomprehensible without an accompanying
subsystem level of description.


298 This approach follows the general paradigm that providing additional detail
about the implementation of the TSF will result in greater assurance that the
SFRs are implemented correctly, and provide information that can be used to
demonstrate this in testing (ATE: Tests).


299 In the requirements for this family, the term _interface_ is used as the means of
communication (between two modules). It describes how the communication
is invoked; this is similar to the details of TSFI (see Functional specification
(ADV_FSP)). The term _interaction_ is used to identify the purpose for
communication; it identifies why two subsystems or modules are
communicating.


**13.6.1** **Detail about the Subsystems and Modules**


300 The requirements define collections of details about subsystems and modules
to be provided:


a) The subsystems and modules are _identified_ with a simple list of what
they are.


b) Subsystems and modules may be _categorised_ (either implicitly or
explicitly) as “SFR-enforcing”, “SFR-supporting”, or “SFR-noninterfering”; these terms are used the same as they are used in
Functional specification (ADV_FSP).


c) A subsystem's _behaviour_ is what it does. The behaviour may also be
categorised as SFR-enforcing, SFR-supporting, or SFR-noninterfering. The behaviour of the subsystem is never categorised as
more SFR-relevant than the category of the subsystem itself. For
example, an SFR-enforcing subsystem can have SFR-enforcing
behaviour as well as SFR-supporting or SFR-non-interfering
behaviour.


d) A _behaviour summary_ of a subsystem is an overview of the actions it
performs (e.g. “The TCP subsystem assembles IP datagrams into
reliable byte streams”).


e) A _behaviour description_ of a subsystem is an explanation of
everything it does. This description should be at a level of detail that
one can readily determine whether the behaviour has any relevance to
the enforcement of the SFRs.


f) A _description of interactions_ among or between subsystems or
modules identifies the reason that subsystems or modules
communicate, and characterises the information that is passed. It need
not define the information to the same level of detail as an interface
specification. For example, it would be sufficient to say “subsystem


April 2017 Version 3.1 Page 121 of 247


**Class ADV: Development**


X requests a block of memory from the memory manager, which
responds with the location of the allocated memory.


g) A _description of interfaces_ provides the details of how the
interactions among modules are achieved. Rather than describing the
reason the modules are communicating or the purpose of their
communication (that is, the description of interactions), the
description of interfaces describes the details of how that
communication is accomplished, in terms of the structure and
contents of the messages, semaphores, internal process
communications, etc.


h) The _purpose_ describes how a module provides their functionality. It
provides sufficient detail that no further design decisions are needed.
The correspondence between the implementation representation that
implements the module, and the purpose of the module should be
readily apparent.


i) A module is otherwise _described_ in terms of whatever is identified in
the element.


Subsystems and modules, and “SFR-enforcing”, etc. are all further explained
in greater detail in A.4, ADV_TDS: Subsystems and Modules.


**ADV_TDS.1** **Basic design**


Dependencies: ADV_FSP.2 Security-enforcing functional
specification


Developer action elements:


**ADV_TDS.1.1D** **The developer shall provide the design of the TOE.**


**ADV_TDS.1.2D** **The developer shall provide a mapping from the TSFI of the functional**

**specification to the lowest level of decomposition available in the TOE**
**design.**


Content and presentation elements:


**ADV_TDS.1.1C** **The design shall describe the structure of the TOE in terms of**

**subsystems.**


**ADV_TDS.1.2C** **The design shall identify all subsystems of the TSF.**


**ADV_TDS.1.3C** **The design shall provide the behaviour summary of each SFR-**

**supporting or SFR-non-interfering TSF subsystem.**


**ADV_TDS.1.4C** **The design shall summarise the SFR-enforcing behaviour of the SFR-**

**enforcing subsystems.**


Page 122 of 247 Version 3.1 April 2017


**Class ADV: Development**


**ADV_TDS.1.5C** **The design shall provide a description of the interactions among SFR-**

**enforcing subsystems of the TSF, and between the SFR-enforcing**
**subsystems of the TSF and other subsystems of the TSF.**


**ADV_TDS.1.6C** **The mapping shall demonstrate that all TSFIs trace to the behaviour**

**described in the TOE design that they invoke.**


Evaluator action elements:


**ADV_TDS.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


**ADV_TDS.1.2E** **The evaluator** _**shall determine**_ **that the design is an accurate and**

**complete instantiation of all security functional requirements.**


**ADV_TDS.2** **Architectural design**


Dependencies: ADV_FSP.3 Functional specification with complete
summary


Developer action elements:


**ADV_TDS.2.1D** The developer shall provide the design of the TOE.


**ADV_TDS.2.2D** The developer shall provide a mapping from the TSFI of the functional

specification to the lowest level of decomposition available in the TOE
design.


Content and presentation elements:


**ADV_TDS.2.1C** The design shall describe the structure of the TOE in terms of subsystems.


**ADV_TDS.2.2C** The design shall identify all subsystems of the TSF.


**ADV_TDS.2.3C** **The design shall provide the behaviour summary of each SFR non-**

**interfering subsystem of the TSF.**


**ADV_TDS.2.4C** The design shall **describe** the SFR-enforcing behaviour of the SFR
enforcing subsystems.


**ADV_TDS.2.5C** The design shall summarise the **SFR-supporting** **and** **SFR-non-interfering**

behaviour of the SFR-enforcing subsystems.


**ADV_TDS.2.6C** The design shall summarise the behaviour of the **SFR-supporting**

subsystems.


**ADV_TDS.2.7C** **The design shall provide a description of the interactions among all**

**subsystems of the TSF.**


**ADV_TDS.2.8C** The mapping shall demonstrate that all TSFIs trace to the behaviour

described in the TOE design that they invoke.


April 2017 Version 3.1 Page 123 of 247


**Class ADV: Development**


Evaluator action elements:


**ADV_TDS.2.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


**ADV_TDS.2.2E** The evaluator _**shall determine**_ that the design is an accurate and complete

instantiation of all security functional requirements.


**ADV_TDS.3** **Basic modular design**


Dependencies: ADV_FSP.4 Complete functional specification


Developer action elements:


**ADV_TDS.3.1D** The developer shall provide the design of the TOE.


**ADV_TDS.3.2D** The developer shall provide a mapping from the TSFI of the functional

specification to the lowest level of decomposition available in the TOE
design.


Content and presentation elements:


**ADV_TDS.3.1C** The design shall describe the structure of the TOE in terms of subsystems.


**ADV_TDS.3.2C** **The design shall describe the TSF in terms of modules.**


**ADV_TDS.3.3C** The design shall identify all subsystems of the TSF.


**ADV_TDS.3.4C** The design shall **provide** **a** **description** of **each** **subsystem** **of** the **TSF.**


**ADV_TDS.3.5C** The design shall provide a description of the interactions among all

subsystems of the TSF.


**ADV_TDS.3.6C** **The design shall provide a mapping from the subsystems of the TSF to**

**the modules of the TSF.**


**ADV_TDS.3.7C** The design shall describe **each** SFR-enforcing **module** **in** **terms** of **its**

**purpose** **and** **relationship** **with** **other** **modules.**


**ADV_TDS.3.8C** **The design shall describe each SFR-enforcing module in terms of its**

**SFR-related interfaces, return values from those interfaces, interaction**
**with other modules and called SFR-related interfaces to other SFR-**
**enforcing modules.**


**ADV_TDS.3.9C** The design shall **describe** each **SFR-supporting** **or** **SFR-non-interfering**

**module** **in** **terms** of **its** **purpose** **and** **interaction** **with** **other** **modules.**


**ADV_TDS.3.10C** The mapping shall demonstrate that all TSFIs trace to the behaviour

described in the TOE design that they invoke.


Page 124 of 247 Version 3.1 April 2017


**Class ADV: Development**


Evaluator action elements:


**ADV_TDS.3.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


**ADV_TDS.3.2E** The evaluator _**shall determine**_ that the design is an accurate and complete

instantiation of all security functional requirements.


**ADV_TDS.4** **Semiformal modular design**


Dependencies: ADV_FSP.5 Complete semi-formal functional
specification with additional error information


Developer action elements:


**ADV_TDS.4.1D** The developer shall provide the design of the TOE.


**ADV_TDS.4.2D** The developer shall provide a mapping from the TSFI of the functional

specification to the lowest level of decomposition available in the TOE
design.


Content and presentation elements:


**ADV_TDS.4.1C** The design shall describe the structure of the TOE in terms of subsystems.


**ADV_TDS.4.2C** The design shall describe the TSF in terms of modules, **designating** **each**

**module** **as** **SFR-enforcing,** **SFR-supporting,** **or** **SFR-non-interfering.**


**ADV_TDS.4.3C** The design shall identify all subsystems of the TSF.


**ADV_TDS.4.4C** The design shall provide a **semiformal** description of each subsystem of the

TSF, **supported** **by** **informal,** **explanatory** **text** **where** **appropriate.**


**ADV_TDS.4.5C** The design shall provide a description of the interactions among all

subsystems of the TSF.


**ADV_TDS.4.6C** The design shall provide a mapping from the subsystems of the TSF to the

modules of the TSF.


**ADV_TDS.4.7C** The design shall describe each SFR-enforcing **and** **SFR-supporting** module

in terms of its purpose and relationship with other modules.


**ADV_TDS.4.8C** The design shall describe each SFR-enforcing **and** **SFR-supporting** module

in terms of its SFR-related interfaces, return values from those interfaces,
interaction with other modules and called SFR-related interfaces to other
SFR-enforcing **or** **SFR-supporting** modules.


**ADV_TDS.4.9C** The design shall describe each SFR-non-interfering module in terms of its

purpose and interaction with other modules.


**ADV_TDS.4.10C** The mapping shall demonstrate that all TSFIs trace to the behaviour

described in the TOE design that they invoke.


April 2017 Version 3.1 Page 125 of 247


**Class ADV: Development**


Evaluator action elements:


**ADV_TDS.4.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


**ADV_TDS.4.2E** The evaluator _**shall determine**_ that the design is an accurate and complete

instantiation of all security functional requirements.


**ADV_TDS.5** **Complete semiformal modular design**


Dependencies: ADV_FSP.5 Complete semi-formal functional
specification with additional error information


Developer action elements:


**ADV_TDS.5.1D** The developer shall provide the design of the TOE.


**ADV_TDS.5.2D** The developer shall provide a mapping from the TSFI of the functional

specification to the lowest level of decomposition available in the TOE
design.


Content and presentation elements:


**ADV_TDS.5.1C** The design shall describe the structure of the TOE in terms of subsystems.


**ADV_TDS.5.2C** The design shall describe the TSF in terms of modules, designating each

module as SFR-enforcing, SFR-supporting, or SFR-non-interfering.


**ADV_TDS.5.3C** The design shall identify all subsystems of the TSF.


**ADV_TDS.5.4C** The design shall provide a semiformal description of each subsystem of the

TSF, supported by informal, explanatory text where appropriate.


**ADV_TDS.5.5C** The design shall provide a description of the interactions among all

subsystems of the TSF.


**ADV_TDS.5.6C** The design shall provide a mapping from the subsystems of the TSF to the

modules of the TSF.


**ADV_TDS.5.7C** The design shall **provide** **a** **semiformal** **description** **of** each module in terms

of its **purpose,** **interaction,** interfaces, return values from those interfaces,
and called interfaces to other modules, **supported** **by** **informal,** **explanatory**
**text** **where** **appropriate.**


**ADV_TDS.5.8C** The mapping shall demonstrate that all TSFIs trace to the behaviour

described in the TOE design that they invoke.


Evaluator action elements:


**ADV_TDS.5.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


Page 126 of 247 Version 3.1 April 2017


**Class ADV: Development**


**ADV_TDS.5.2E** The evaluator _**shall determine**_ that the design is an accurate and complete

instantiation of all security functional requirements.


**ADV_TDS.6** **Complete semiformal modular design with formal high-level**

**design presentation**


Dependencies: ADV_FSP.6 Complete semi-formal functional
specification with additional formal specification


Developer action elements:


**ADV_TDS.6.1D** The developer shall provide the design of the TOE.


**ADV_TDS.6.2D** The developer shall provide a mapping from the TSFI of the functional

specification to the lowest level of decomposition available in the TOE
design.


**ADV_TDS.6.3D** **The developer shall provide a formal specification of the TSF**

**subsystems.**


**ADV_TDS.6.4D** **The developer shall provide a proof of correspondence between the**

**formal specifications of the TSF subsystems and of the functional**
**specification.**


Content and presentation elements:


**ADV_TDS.6.1C** The design shall describe the structure of the TOE in terms of subsystems.


**ADV_TDS.6.2C** The design shall describe the TSF in terms of modules, designating each

module as SFR-enforcing, SFR-supporting, or SFR-non-interfering.


**ADV_TDS.6.3C** The design shall identify all subsystems of the TSF.


**ADV_TDS.6.4C** The design shall provide a semiformal description of each subsystem of the

TSF, supported by informal, explanatory text where appropriate.


**ADV_TDS.6.5C** The design shall provide a description of the interactions among all

subsystems of the TSF.


**ADV_TDS.6.6C** The design shall provide a mapping from the subsystems of the TSF to the

modules of the TSF.


**ADV_TDS.6.7C** The design shall **describe** each module in **semiformal** **style** **in** terms of its

purpose, interaction, interfaces, return values from those interfaces, and
called interfaces to other modules, supported by informal, explanatory text
where appropriate.


**ADV_TDS.6.8C** **The formal specification of the TSF subsystems shall describe the TSF**

**using a formal style, supported by informal, explanatory text where**
**appropriate.**


April 2017 Version 3.1 Page 127 of 247


**Class ADV: Development**


**ADV_TDS.6.9C** The mapping shall demonstrate that all TSFIs trace to the behaviour

described in the TOE design that they invoke.


**ADV_TDS.6.10C** **The proof of correspondence between the formal specifications of the**

**TSF subsystems and of the functional specification shall demonstrate**
**that all behaviour described in the TOE design is a correct and complete**
**refinement of the TSFI that invoked it.**


Evaluator action elements:


**ADV_TDS.6.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


**ADV_TDS.6.2E** The evaluator _**shall determine**_ that the design is an accurate and complete

instantiation of all security functional requirements.


Page 128 of 247 Version 3.1 April 2017


**Class AGD: Guidance documents**

# **14 Class AGD: Guidance documents**


301 The guidance documents class provides the requirements for guidance
documentation for all user roles. For the secure preparation and operation of
the TOE it is necessary to describe all relevant aspects for the secure
handling of the TOE. The class also addresses the possibility of unintended
incorrect configuration or handling of the TOE.


302 In many cases it may be appropriate that guidance is provided in separate
documents for preparation and operation of the TOE, or even separate for
different user roles as end-users, administrators, application programmers
using software or hardware interfaces, etc.


303 The guidance documents class is subdivided into two families which are
concerned with the preparative user guidance (what has to be done to
transform the delivered TOE into its evaluated configuration in the
operational environment as described in the ST) and with the operational
user guidance (what has to be done during the operation of the TOE in its
evaluated configuration).


304 Figure 13 shows the families within this class, and the hierarchy of
components within the families.


**Figure 13 - AGD: Guidance documents class decomposition**


April 2017 Version 3.1 Page 129 of 247


**Class AGD: Guidance documents**

## **14.1 Operational user guidance (AGD_OPE)**


Objectives


305 Operational user guidance refers to written material that is intended to be
used by all types of users of the TOE in its evaluated configuration: endusers, persons responsible for maintaining and administering the TOE in a
correct manner for maximum security, and by others (e.g. programmers)
using the TOE's external interfaces. Operational user guidance describes the
security functionality provided by the TSF, provides instructions and
guidelines (including warnings), helps to understand the TSF and includes
the security-critical information, and the security-critical actions required, for
its secure use. Misleading and unreasonable guidance should be absent from
the guidance documentation, and secure procedures for all modes of
operation should be addressed. Insecure states should be easy to detect.


306 The operational user guidance provides a measure of confidence that nonmalicious users, administrators, application providers and others exercising
the external interfaces of the TOE will understand the secure operation of the
TOE and will use it as intended. The evaluation of the user guidance includes
investigating whether the TOE can be used in a manner that is insecure but
that the user of the TOE would reasonably believe to be secure. The
objective is to minimise the risk of human or other errors in operation that
may deactivate, disable, or fail to activate security functionality, resulting in
an undetected insecure state.


Component levelling


307 This family contains only one component.


Application notes


308 There may be different user roles or groups that are recognised by the TOE
and that can interact with the TSF. These user roles and groups should be
taken into consideration by the operational user guidance. They may be
roughly grouped into administrators and non-administrative users, or more
specifically grouped into persons responsible for receiving, accepting,
installing and maintaining the TOE, application programmers, revisors,
auditors, daily-management, end-users. Each role can encompass an
extensive set of capabilities, or can be a single one.


309 The requirement **AGD_OPE.1.1C** encompasses the aspect that any warnings to
the users during operation of a TOE with regard to the security problem
definition and the security objectives for the operational environment
described in the PP/ST are appropriately covered in the user guidance.


310 The concept of secure values, as employed in **AGD_OPE.1.3C**, has relevance
where a user has control over security parameters. Guidance needs to be
provided on secure and insecure settings for such parameters.


Page 130 of 247 Version 3.1 April 2017


**Class AGD: Guidance documents**


311 **AGD_OPE.1.4C** requires that the user guidance describes the appropriate
reactions to all security-relevant events. Although many security-relevant
events are the result of performing functions, this need not always be the
case (e.g. the audit log fills up, an intrusion is detected). Furthermore, a
security-relevant event may happen as a result of a specific chain of
functions or, conversely, several security-relevant events may be triggered by
one function.


312 **AGD_OPE.1.7C** requires that the user guidance is clear and reasonable.
Misleading or unreasonable guidance may result in a user of the TOE
believing that the TOE is secure when it is not.


313 An example of misleading guidance would be the description of a single
guidance instruction that could be parsed in more than one way, one of
which may result in an insecure state.


314 An example of unreasonable guidance would be a recommendation to follow
a procedure that is so complicated that it cannot reasonably be expected that
users will follow this guidance.


**AGD_OPE.1** **Operational user guidance**


Dependencies: ADV_FSP.1 Basic functional specification


Developer action elements:


**AGD_OPE.1.1D** **The developer shall provide operational user guidance.**


Content and presentation elements:


**AGD_OPE.1.1C** **The operational user guidance shall describe, for each user role, the**

**user-accessible functions and privileges that should be controlled in a**
**secure processing environment, including appropriate warnings.**


**AGD_OPE.1.2C** **The operational user guidance shall describe, for each user role, how to**

**use the available interfaces provided by the TOE in a secure manner.**


**AGD_OPE.1.3C** **The operational user guidance shall describe, for each user role, the**

**available functions and interfaces, in particular all security parameters**
**under the control of the user, indicating secure values as appropriate.**


**AGD_OPE.1.4C** **The operational user guidance shall, for each user role, clearly present**

**each type of security-relevant event relative to the user-accessible**
**functions that need to be performed, including changing the security**
**characteristics of entities under the control of the TSF.**


**AGD_OPE.1.5C** **The operational user guidance shall identify all possible modes of**

**operation of the TOE (including operation following failure or**
**operational error), their consequences and implications for maintaining**
**secure operation.**


April 2017 Version 3.1 Page 131 of 247


**Class AGD: Guidance documents**


**AGD_OPE.1.6C** **The operational user guidance shall, for each user role, describe the**

**security measures to be followed in order to fulfil the security objectives**
**for the operational environment as described in the ST.**


**AGD_OPE.1.7C** **The operational user guidance shall be clear and reasonable.**


Evaluator action elements:


**AGD_OPE.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


Page 132 of 247 Version 3.1 April 2017


**Class AGD: Guidance documents**

## **14.2 Preparative procedures (AGD_PRE)**


Objectives


315 Preparative procedures are useful for ensuring that the TOE has been
received and installed in a secure manner as intended by the developer. The
requirements for preparation call for a secure transition from the delivered
TOE to its initial operational environment. This includes investigating
whether the TOE can be configured or installed in a manner that is insecure
but that the user of the TOE would reasonably believe to be secure.


Component levelling


316 This family contains only one component.


Application notes


317 It is recognised that the application of these requirements will vary
depending on aspects such as whether the TOE is delivered in an operational
state, or whether it has to be installed at the TOE owner's site, etc.


318 The first process covered by the preparative procedures is the consumer's
secure acceptance of the received TOE in accordance with the developer's
delivery procedures. If the developer has not defined delivery procedures,
security of the acceptance has to be ensured otherwise.


319 Installation of the TOE includes transforming its operational environment
into a state that conforms to the security objectives for the operational
environment provided in the ST.


320 It might also be the case that no installation is necessary, for example a smart
card. In this case it may be inappropriate to require and analyse installation
procedures.


321 The requirements in this assurance family are presented separately from
those in the Operational user guidance (AGD_OPE) family, due to the
infrequent, possibly one-time use of the preparative procedures.


**AGD_PRE.1** **Preparative procedures**


Dependencies: No dependencies.


Developer action elements:


**AGD_PRE.1.1D** **The developer shall provide the TOE including its preparative**

**procedures.**


Content and presentation elements:


**AGD_PRE.1.1C** **The preparative procedures shall describe all the steps necessary for**

**secure acceptance of the delivered TOE in accordance with the**
**developer's delivery procedures.**


April 2017 Version 3.1 Page 133 of 247


**Class AGD: Guidance documents**


**AGD_PRE.1.2C** **The preparative procedures shall describe all the steps necessary for**

**secure installation of the TOE and for the secure preparation of the**
**operational environment in accordance with the security objectives for**
**the operational environment as described in the ST.**


Evaluator action elements:


**AGD_PRE.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


**AGD_PRE.1.2E** **The evaluator shall apply the preparative procedures to confirm that the**

**TOE can be prepared securely for operation.**


Page 134 of 247 Version 3.1 April 2017


**Class ALC: Life-cycle support**

# **15 Class ALC: Life-cycle support**


322 Life-cycle support is an aspect of establishing discipline and control in the
processes of refinement of the TOE during its development and maintenance.
Confidence in the correspondence between the TOE security requirements
and the TOE is greater if security analysis and the production of the evidence
are done on a regular basis as an integral part of the development and
maintenance activities.


323 In the product life-cycle it is distinguished whether the TOE is under the
responsibility of the developer or the user rather than whether it is located in
the development or user environment. The point of transition is the moment
where the TOE is handed over to the user. This is also the point of transition
from the ALC to the AGD class.


324 The ALC class consists of seven families. Life-cycle definition (ALC_LCD)
is the high-level description of the TOE life-cycle; CM capabilities
(ALC_CMC) a more detailed description of the management of the
configuration items. CM scope (ALC_CMS) requires a minimum set of
configuration items to be managed in the defined way. Development security
(ALC_DVS) is concerned with the developer's physical, procedural,
personnel, and other security measures; Tools and techniques (ALC_TAT)
with the development tools and implementation standards used by the
developer; Flaw remediation (ALC_FLR) with the handling of security flaws.
Delivery (ALC_DEL) defines the procedures used for the delivery of the
TOE to the consumer. Delivery processes occurring during the development
of the TOE are denoted rather as transportations, and are handled in the
context of integration and acceptance procedures in other families of this
class.


325 Throughout this class, development and related terms (developer, develop)
are meant in the more general sense to comprise development _and_
_production_, whereas production specifically means the process of
transforming the implementation representation into the final TOE.


326 Figure 14 shows the families within this class, and the hierarchy of
components within the families.


April 2017 Version 3.1 Page 135 of 247


**Class ALC: Life-cycle support**


**Figure 14 - ALC: Life-cycle support class decomposition**


Page 136 of 247 Version 3.1 April 2017


**Class ALC: Life-cycle support**

## **15.1 CM capabilities (ALC_CMC)**


Objectives


327 Configuration management (CM) is one means for increasing assurance that
the TOE meets the SFRs. CM establishes this by requiring discipline and
control in the processes of refinement and modification of the TOE and the
related information. CM systems are put in place to ensure the integrity of
the portions of the TOE that they control, by providing a method of tracking
any changes, and by ensuring that all changes are authorised.


328 The objective of this family is to require the developer's CM system to have
certain capabilities. These are meant to reduce the likelihood that accidental
or unauthorised modifications of the configuration items will occur. The CM
system should ensure the integrity of the TOE from the early design stages
through all subsequent maintenance efforts.


329 The objective of introducing automated CM tools is to increase the
effectiveness of the CM system. While both automated and manual CM
systems can be bypassed, ignored, or proven insufficient to prevent
unauthorised modification, automated systems are less susceptible to human
error or negligence.


330 The objectives of this family include the following:


a) ensuring that the TOE is correct and complete before it is sent to the
consumer;


b) ensuring that no configuration items are missed during evaluation;


c) preventing unauthorised modification, addition, or deletion of TOE
configuration items.


Component levelling


331 The components in this family are levelled on the basis of the CM system
capabilities, the scope of the CM documentation and the evidence provided
by the developer.


Application notes


332 While it is desired that CM be applied from the early design stages and
continue into the future, this family requires that CM be in place and in use
prior to the end of the evaluation.


333 In the case where the TOE is a subset of a product, the requirements of this
family apply only to the TOE configuration items, not to the product as a
whole.


334 For developers that have separate CM systems for different life-cycle phases
(for example development, production and/or the final product), it is required
to document all of them. For evaluation purposes, the separate CM systems


April 2017 Version 3.1 Page 137 of 247


**Class ALC: Life-cycle support**


should be regarded as parts of an overall CM system which is addressed in
the criteria.


335 Similarly, if parts of the TOE are produced by different developers or at
different sites, the CM systems being in use at the different places should be
regarded as parts of an overall CM system which is addressed in the criteria.
In this situation, integration aspects have also to be taken into account.


336 Several elements of this family refer to configuration items. These elements
identify CM requirements to be imposed on all items identified in the
configuration list, but leave the contents of the list to the discretion of the
developer. CM scope (ALC_CMS) can be used to narrow this discretion by
identifying specific items that must be included in the configuration list, and
hence covered by CM.


337 **ALC_CMC.2.3C** introduces a requirement that the CM system uniquely identify
all configuration items. This also requires that modifications to configuration
items result in a new, unique identifier being assigned to the configuration
item.


338 **ALC_CMC.3.8C** introduces the requirement that the evidence shall demonstrate
that the CM system operates in accordance with the CM plan. Examples of
such evidence might be documentation such as screen snapshots or audit trail
output from the CM system, or a detailed demonstration of the CM system
by the developer. The evaluator is responsible for determining that this
evidence is sufficient to show that the CM system operates in accordance
with the CM plan.


339 **ALC_CMC.4.5C** introduces a requirement that the CM system provide an
automated means to support the production of the TOE. This requires that the
CM system provide an automated means to assist in determining that the
correct configuration items are used in generating the TOE.


340 **ALC_CMC.5.10C** introduces a requirement that the CM system provide an
automated means to ascertain the changes between the TOE and its
preceding version. If no previous version of the TOE exists, the developer
still needs to provide an automated means to ascertain the changes between
the TOE and a future version of the TOE.


**ALC_CMC.1** **Labelling of the TOE**


Dependencies: ALC_CMS.1 TOE CM coverage


Objectives


341 A unique reference is required to ensure that there is no ambiguity in terms
of which instance of the TOE is being evaluated. Labelling the TOE with its
reference ensures that users of the TOE can be aware of which instance of
the TOE they are using.


Page 138 of 247 Version 3.1 April 2017


**Class ALC: Life-cycle support**


Developer action elements:


**ALC_CMC.1.1D** **The developer shall provide the TOE and a reference for the TOE.**


Content and presentation elements:


**ALC_CMC.1.1C** **The TOE shall be labelled with its unique reference.**


Evaluator action elements:


**ALC_CMC.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


**ALC_CMC.2** **Use of a CM system**


Dependencies: ALC_CMS.1 TOE CM coverage


Objectives


342 A unique reference is required to ensure that there is no ambiguity in terms
of which instance of the TOE is being evaluated. Labelling the TOE with its
reference ensures that users of the TOE can be aware of which instance of
the TOE they are using.


343 Unique identification of the configuration items leads to a clearer
understanding of the composition of the TOE, which in turn helps to
determine those items which are subject to the evaluation requirements for
the TOE.


344 The use of a CM system increases assurance that the configuration items are
maintained in a controlled manner.


Developer action elements:


**ALC_CMC.2.1D** The developer shall provide the TOE and a reference for the TOE.


**ALC_CMC.2.2D** **The developer shall provide the CM documentation.**


**ALC_CMC.2.3D** **The developer shall use a CM system.**


Content and presentation elements:


**ALC_CMC.2.1C** The TOE shall be labelled with its unique reference.


**ALC_CMC.2.2C** **The CM documentation shall describe the method used to uniquely**

**identify the configuration items.**


**ALC_CMC.2.3C** **The CM system shall uniquely identify all configuration items.**


Evaluator action elements:


**ALC_CMC.2.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


April 2017 Version 3.1 Page 139 of 247


**Class ALC: Life-cycle support**


**ALC_CMC.3** **Authorisation controls**


Dependencies: ALC_CMS.1 TOE CM coverage
ALC_DVS.1 Identification of security measures
ALC_LCD.1 Developer defined life-cycle model


Objectives


345 A unique reference is required to ensure that there is no ambiguity in terms
of which instance of the TOE is being evaluated. Labelling the TOE with its
reference ensures that users of the TOE can be aware of which instance of
the TOE they are using.


346 Unique identification of the configuration items leads to a clearer
understanding of the composition of the TOE, which in turn helps to
determine those items which are subject to the evaluation requirements for
the TOE.


347 The use of a CM system increases assurance that the configuration items are
maintained in a controlled manner.


348 Providing controls to ensure that unauthorised modifications are not made to
the TOE (“CM access control”), and ensuring proper functionality and use of
the CM system, helps to maintain the integrity of the TOE.


Developer action elements:


**ALC_CMC.3.1D** The developer shall provide the TOE and a reference for the TOE.


**ALC_CMC.3.2D** The developer shall provide the CM documentation.


**ALC_CMC.3.3D** The developer shall use a CM system.


Content and presentation elements:


**ALC_CMC.3.1C** The TOE shall be labelled with its unique reference.


**ALC_CMC.3.2C** The CM documentation shall describe the method used to uniquely identify

the configuration items.


**ALC_CMC.3.3C** The CM system shall uniquely identify all configuration items.


**ALC_CMC.3.4C** **The CM system shall provide measures such that only authorised**

**changes are made to the configuration items.**


**ALC_CMC.3.5C** **The CM documentation shall include a CM plan.**


**ALC_CMC.3.6C** **The CM plan shall describe how the CM system is used for the**

**development of the TOE.**


**ALC_CMC.3.7C** **The evidence shall demonstrate that all configuration items are being**

**maintained under the CM system.**


Page 140 of 247 Version 3.1 April 2017


**Class ALC: Life-cycle support**


**ALC_CMC.3.8C** **The evidence shall demonstrate that the CM system is being operated in**

**accordance with the CM plan.**


Evaluator action elements:


**ALC_CMC.3.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


**ALC_CMC.4** **Production support, acceptance procedures and automation**


Dependencies: ALC_CMS.1 TOE CM coverage
ALC_DVS.1 Identification of security measures
ALC_LCD.1 Developer defined life-cycle model


Objectives


349 A unique reference is required to ensure that there is no ambiguity in terms
of which instance of the TOE is being evaluated. Labelling the TOE with its
reference ensures that users of the TOE can be aware of which instance of
the TOE they are using.


350 Unique identification of the configuration items leads to a clearer
understanding of the composition of the TOE, which in turn helps to
determine those items which are subject to the evaluation requirements for
the TOE.


351 The use of a CM system increases assurance that the configuration items are
maintained in a controlled manner.


352 Providing controls to ensure that unauthorised modifications are not made to
the TOE (“CM access control”), and ensuring proper functionality and use of
the CM system, helps to maintain the integrity of the TOE.


353 The purpose of the acceptance procedures is to ensure that the parts of the
TOE are of adequate quality and to confirm that any creation or modification
of configuration items is authorised. Acceptance procedures are an essential
element in integration processes and in the life-cycle management of the
TOE.


354 In development environments where the configuration items are complex, it
is difficult to control changes without the support of automated tools. In
particular, these automated tools need to be able to support the numerous
changes that occur during development and ensure that those changes are
authorised. It is an objective of this component to ensure that the
configuration items are controlled through automated means. If the TOE is
developed by multiple developers, i.e. integration has to take place, the use
of automatic tools is adequate.


355 Production support procedures help to ensure that the generation of the TOE
from a managed set of configuration items is correctly performed in an
authorised manner, particularly in the case when different developers are
involved and integration processes have to be carried out.


April 2017 Version 3.1 Page 141 of 247


**Class ALC: Life-cycle support**


Developer action elements:


**ALC_CMC.4.1D** The developer shall provide the TOE and a reference for the TOE.


**ALC_CMC.4.2D** The developer shall provide the CM documentation.


**ALC_CMC.4.3D** The developer shall use a CM system.


Content and presentation elements:


**ALC_CMC.4.1C** The TOE shall be labelled with its unique reference.


**ALC_CMC.4.2C** The CM documentation shall describe the method used to uniquely identify

the configuration items.


**ALC_CMC.4.3C** The CM system shall uniquely identify all configuration items.


**ALC_CMC.4.4C** The CM system shall provide **automated** measures such that only authorised

changes are made to the configuration items.


**ALC_CMC.4.5C** **The CM system shall support the production of the TOE by automated**

**means.**


**ALC_CMC.4.6C** The CM documentation shall include a CM plan.


**ALC_CMC.4.7C** The CM plan shall describe how the CM system is used for the development

of the TOE.


**ALC_CMC.4.8C** **The CM plan shall describe the procedures used to accept modified or**

**newly created configuration items as part of the TOE.**


**ALC_CMC.4.9C** The evidence shall demonstrate that all configuration items are being

maintained under the CM system.


**ALC_CMC.4.10C** The evidence shall demonstrate that the CM system is being operated in

accordance with the CM plan.


Evaluator action elements:


**ALC_CMC.4.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


**ALC_CMC.5** **Advanced support**


Dependencies: ALC_CMS.1 TOE CM coverage
ALC_DVS.2 Sufficiency of security measures
ALC_LCD.1 Developer defined life-cycle model


Objectives


356 A unique reference is required to ensure that there is no ambiguity in terms
of which instance of the TOE is being evaluated. Labelling the TOE with its


Page 142 of 247 Version 3.1 April 2017


**Class ALC: Life-cycle support**


reference ensures that users of the TOE can be aware of which instance of
the TOE they are using.


357 Unique identification of the configuration items leads to a clearer
understanding of the composition of the TOE, which in turn helps to
determine those items which are subject to the evaluation requirements for
the TOE.


358 The use of a CM system increases assurance that the configuration items are
maintained in a controlled manner.


359 Providing controls to ensure that unauthorised modifications are not made to
the TOE (“CM access control”), and ensuring proper functionality and use of
the CM system, helps to maintain the integrity of the TOE.


360 The purpose of the acceptance procedures is to ensure that the parts of the
TOE are of adequate quality and to confirm that any creation or modification
of configuration items is authorised. Acceptance procedures are an essential
element in integration processes and in the life-cycle management of the
TOE.


361 In development environments where the configuration items are complex, it
is difficult to control changes without the support of automated tools. In
particular, these automated tools need to be able to support the numerous
changes that occur during development and ensure that those changes are
authorised. It is an objective of this component to ensure that the
configuration items are controlled through automated means. If the TOE is
developed by multiple developers, i.e. integration has to take place, the use
of automatic tools is adequate.


362 Production support procedures help to ensure that the generation of the TOE
from a managed set of configuration items is correctly performed in an
authorised manner, particularly in the case when different developers are
involved and integration processes have to be carried out.


363 Requiring that the CM system be able to identify the version of the
implementation representation from which the TOE is generated helps to
ensure that the integrity of this material is preserved by the appropriate
technical, physical and procedural safeguards.


364 Providing an automated means of ascertaining changes between versions of
the TOE and identifying which configuration items are affected by
modifications to other configuration items assists in determining the impact
of the changes between successive versions of the TOE. This in turn can
provide valuable information in determining whether changes to the TOE
result in all configuration items being consistent with one another.


Developer action elements:


**ALC_CMC.5.1D** The developer shall provide the TOE and a reference for the TOE.


April 2017 Version 3.1 Page 143 of 247


**Class ALC: Life-cycle support**


**ALC_CMC.5.2D** The developer shall provide the CM documentation.


**ALC_CMC.5.3D** The developer shall use a CM system.


Content and presentation elements:


**ALC_CMC.5.1C** The TOE shall be labelled with its unique reference.


**ALC_CMC.5.2C** The CM documentation shall describe the method used to uniquely identify

the configuration items.


**ALC_CMC.5.3C** **The CM documentation shall justify that the acceptance procedures**

**provide for an adequate and appropriate review of changes to all**
**configuration items.**


**ALC_CMC.5.4C** The CM system shall uniquely identify all configuration items.


**ALC_CMC.5.5C** The CM system shall provide automated measures such that only authorised

changes are made to the configuration items.


**ALC_CMC.5.6C** The CM system shall support the production of the TOE by automated

means.


**ALC_CMC.5.7C** **The CM system shall ensure that the person responsible for accepting a**

**configuration item into CM is not the person who developed it.**


**ALC_CMC.5.8C** **The CM system shall identify the configuration items that comprise the**

**TSF.**


**ALC_CMC.5.9C** **The CM system shall support the audit of all changes to the TOE by**

**automated means, including the originator, date, and time in the audit**
**trail.**


**ALC_CMC.5.10C** **The CM system shall provide an automated means to identify all other**

**configuration items that are affected by the change of a given**
**configuration item.**


**ALC_CMC.5.11C** **The CM system shall be able to identify the version of the**

**implementation representation from which the TOE is generated.**


**ALC_CMC.5.12C** The CM documentation shall include a CM plan.


**ALC_CMC.5.13C** The CM plan shall describe how the CM system is used for the development

of the TOE.


**ALC_CMC.5.14C** The CM plan shall describe the procedures used to accept modified or newly

created configuration items as part of the TOE.


**ALC_CMC.5.15C** The evidence shall demonstrate that all configuration items are being

maintained under the CM system.


Page 144 of 247 Version 3.1 April 2017


**Class ALC: Life-cycle support**


**ALC_CMC.5.16C** The evidence shall demonstrate that the CM system is being operated in

accordance with the CM plan.


Evaluator action elements:


**ALC_CMC.5.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


**ALC_CMC.5.2E** **The evaluator** _**shall determine**_ **that the application of the production**

**support procedures results in a TOE as provided by the developer for**
**testing activities.**


April 2017 Version 3.1 Page 145 of 247


**Class ALC: Life-cycle support**

## **15.2 CM scope (ALC_CMS)**


Objectives


365 The objective of this family is to identify items to be included as
configuration items and hence placed under the CM requirements of CM
capabilities (ALC_CMC). Applying configuration management to these
additional items provides additional assurance that the integrity of TOE is
maintained.


Component levelling


366 The components in this family are levelled on the basis of which of the
following are required to be included as configuration items: the TOE and
the evaluation evidence required by the SARs; the parts of the TOE; the
implementation representation; security flaws; and development tools and
related information.


Application notes


367 While CM scope (ALC_CMS) mandates a list of configuration items and
that each item on this list be under CM, CM capabilities (ALC_CMC) leaves
the contents of the configuration list to the discretion of the developer. CM
scope (ALC_CMS) narrows this discretion by identifying items that must be
included in the configuration list, and hence come under the CM
requirements of CM capabilities (ALC_CMC).


**ALC_CMS.1** **TOE CM coverage**


Dependencies: No dependencies.


Objectives


368 A CM system can control changes only to those items that have been placed
under CM (i.e., the configuration items identified in the configuration list).
Placing the TOE itself and the evaluation evidence required by the other
SARs in the ST under CM provides assurance that they have been modified
in a controlled manner with proper authorisations.


Application notes


369 **ALC_CMS.1.1C** introduces the requirement that the TOE itself and the
evaluation evidence required by the other SARs in the ST be included in the
configuration list and hence be subject to the CM requirements of CM
capabilities (ALC_CMC).


Developer action elements:


**ALC_CMS.1.1D** **The developer shall provide a configuration list for the TOE.**


Page 146 of 247 Version 3.1 April 2017


**Class ALC: Life-cycle support**


Content and presentation elements:


**ALC_CMS.1.1C** **The configuration list shall include the following: the TOE itself; and the**

**evaluation evidence required by the SARs.**


**ALC_CMS.1.2C** **The configuration list shall uniquely identify the configuration items.**


Evaluator action elements:


**ALC_CMS.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


**ALC_CMS.2** **Parts of the TOE CM coverage**


Dependencies: No dependencies.


Objectives


370 A CM system can control changes only to those items that have been placed
under CM (i.e., the configuration items identified in the configuration list).
Placing the TOE itself, the parts that comprise the TOE, and the evaluation
evidence required by the other SARs under CM provides assurance that they
have been modified in a controlled manner with proper authorisations.


Application notes


371 **ALC_CMS.2.1C** introduces the requirement that the parts that comprise the TOE
(all parts that are delivered to the consumer, for example hardware parts or
executable files) be included in the configuration list and hence be subject to
the CM requirements of CM capabilities (ALC_CMC).


372 **ALC_CMS.2.3C** introduces the requirement that the configuration list indicate
the developer of each TSF relevant configuration item. “Developer” here
does not refer to a person, but to the organisation responsible for the
development of the item.


Developer action elements:


**ALC_CMS.2.1D** The developer shall provide a configuration list for the TOE.


Content and presentation elements:


**ALC_CMS.2.1C** The configuration list shall include the following: the TOE itself; the

evaluation evidence required by the SARs; **and** **the** **parts** **that** **comprise** **the**
**TOE.**


**ALC_CMS.2.2C** The configuration list shall uniquely identify the configuration items.


**ALC_CMS.2.3C** **For each TSF relevant configuration item, the configuration list shall**

**indicate the developer of the item.**


April 2017 Version 3.1 Page 147 of 247


**Class ALC: Life-cycle support**


Evaluator action elements:


**ALC_CMS.2.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


**ALC_CMS.3** **Implementation representation CM coverage**


Dependencies: No dependencies.


Objectives


373 A CM system can control changes only to those items that have been placed
under CM (i.e., the configuration items identified in the configuration list).
Placing the TOE itself, the parts that comprise the TOE, the TOE
implementation representation and the evaluation evidence required by the
other SARs under CM provides assurance that they have been modified in a
controlled manner with proper authorisations.


Application notes


374 **ALC_CMS.3.1C** introduces the requirement that the TOE implementation
representation be included in the list of configuration items and hence be
subject to the CM requirements of CM capabilities (ALC_CMC).


Developer action elements:


**ALC_CMS.3.1D** The developer shall provide a configuration list for the TOE.


Content and presentation elements:


**ALC_CMS.3.1C** The configuration list shall include the following: the TOE itself; the

evaluation evidence required by the SARs; the parts that comprise the TOE;
**and** **the** **implementation** **representation.**


**ALC_CMS.3.2C** The configuration list shall uniquely identify the configuration items.


**ALC_CMS.3.3C** For each TSF relevant configuration item, the configuration list shall indicate

the developer of the item.


Evaluator action elements:


**ALC_CMS.3.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


**ALC_CMS.4** **Problem tracking CM coverage**


Dependencies: No dependencies.


Objectives


375 A CM system can control changes only to those items that have been placed
under CM (i.e., the configuration items identified in the configuration list).
Placing the TOE itself, the parts that comprise the TOE, the TOE


Page 148 of 247 Version 3.1 April 2017


**Class ALC: Life-cycle support**


implementation representation and the evaluation evidence required by the
other SARs under CM provides assurance that they have been modified in a
controlled manner with proper authorisations.


376 Placing security flaws under CM ensures that security flaw reports are not
lost or forgotten, and allows a developer to track security flaws to their
resolution.


Application notes


377 **ALC_CMS.4.1C** introduces the requirement that security flaws be included in
the configuration list and hence be subject to the CM requirements of CM
capabilities (ALC_CMC). This requires that information regarding previous
security flaws and their resolution be maintained, as well as details regarding
current security flaws.


Developer action elements:


**ALC_CMS.4.1D** The developer shall provide a configuration list for the TOE.


Content and presentation elements:


**ALC_CMS.4.1C** The configuration list shall include the following: the TOE itself; the

evaluation evidence required by the SARs; the parts that comprise the TOE;
the implementation representation; **and** **security** **flaw** **reports** **and**
**resolution** **status.**


**ALC_CMS.4.2C** The configuration list shall uniquely identify the configuration items.


**ALC_CMS.4.3C** For each TSF relevant configuration item, the configuration list shall indicate

the developer of the item.


Evaluator action elements:


**ALC_CMS.4.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


**ALC_CMS.5** **Development tools CM coverage**


Dependencies: No dependencies.


Objectives


378 A CM system can control changes only to those items that have been placed
under CM (i.e., the configuration items identified in the configuration list).
Placing the TOE itself, the parts that comprise the TOE, the TOE
implementation representation and the evaluation evidence required by the
other SARs under CM provides assurance that they have been modified in a
controlled manner with proper authorisations.


April 2017 Version 3.1 Page 149 of 247


**Class ALC: Life-cycle support**


379 Placing security flaws under CM ensures that security flaw reports are not
lost or forgotten, and allows a developer to track security flaws to their
resolution.


380 Development tools play an important role in ensuring the production of a
quality version of the TOE. Therefore, it is important to control
modifications to these tools.


Application notes


381 **ALC_CMS.5.1C** introduces the requirement that development tools and other
related information be included in the list of configuration items and hence
be subject to the CM requirements of CM capabilities (ALC_CMC).
Examples of development tools are programming languages and compilers.
Information pertaining to TOE generation items (such as compiler options,
generation options, and build options) is an example of information relating
to development tools.


Developer action elements:


**ALC_CMS.5.1D** The developer shall provide a configuration list for the TOE.


Content and presentation elements:


**ALC_CMS.5.1C** The configuration list shall include the following: the TOE itself; the

evaluation evidence required by the SARs; the parts that comprise the TOE;
the implementation representation; security flaw reports and resolution
status; **and** **development** **tools** **and** **related** **information.**


**ALC_CMS.5.2C** The configuration list shall uniquely identify the configuration items.


**ALC_CMS.5.3C** For each TSF relevant configuration item, the configuration list shall indicate

the developer of the item.


Evaluator action elements:


**ALC_CMS.5.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


Page 150 of 247 Version 3.1 April 2017


**Class ALC: Life-cycle support**

## **15.3 Delivery (ALC_DEL)**


Objectives


382 The concern of this family is the secure transfer of the finished TOE from the
development environment into the responsibility of the user.


383 The requirements for delivery call for system control and distribution
facilities and procedures that detail the measures necessary to provide
assurance that the security of the TOE is maintained during distribution of
the TOE to the user. For a valid distribution of the TOE, the procedures used
for the distribution of the TOE address the objectives identified in the PP/ST
relating to the security of the TOE during delivery.


Component levelling


384 This family contains only one component. An increasing level of protection
is established by requiring commensurability of the delivery procedures with
the assumed attack potential in the family Vulnerability analysis
(AVA_VAN).


Application notes


385 Transportations from subcontractors to the developer or between different
development sites are not considered here, but in the family Development
security (ALC_DVS).


386 The end of the delivery phase is marked by the transfer of the TOE into the
responsibility of the user. This does not necessarily coincide with the arrival
of the TOE at the user's location.


387 The delivery procedures should consider, if applicable, issues such as:


a) ensuring that the TOE received by the consumer corresponds
precisely to the evaluated version of the TOE;


b) avoiding or detecting any tampering with the actual version of the
TOE;


c) preventing submission of a false version of the TOE;


d) avoiding unwanted knowledge of distribution of the TOE to the
consumer: there might be cases where potential attackers should not
know when and how it is delivered;


e) avoiding or detecting the TOE being intercepted during delivery; and


f) avoiding the TOE being delayed or stopped during distribution.


388 The delivery procedures should include the recipient's actions implied by
these issues. The consistent description of these implied actions is examined
in the Preparative procedures (AGD_PRE) family, if present.


April 2017 Version 3.1 Page 151 of 247


**Class ALC: Life-cycle support**


**ALC_DEL.1** **Delivery procedures**


Dependencies: No dependencies.


Developer action elements:


**ALC_DEL.1.1D** **The developer shall document and provide procedures for delivery of**

**the TOE or parts of it to the consumer.**


**ALC_DEL.1.2D** **The developer shall use the delivery procedures.**


Content and presentation elements:


**ALC_DEL.1.1C** **The delivery documentation shall describe all procedures that are**

**necessary to maintain security when distributing versions of the TOE to**
**the consumer.**


Evaluator action elements:


**ALC_DEL.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


Page 152 of 247 Version 3.1 April 2017


**Class ALC: Life-cycle support**

## **15.4 Development security (ALC_DVS)**


Objectives


389 Development security is concerned with physical, procedural, personnel, and
other security measures that may be used in the development environment to
protect the TOE and its parts. It includes the physical security of the
development location and any procedures used to select development staff.


Component levelling


390 The components in this family are levelled on the basis of whether
justification of the sufficiency of the security measures is required.


Application notes


391 This family deals with measures to remove or reduce threats existing at the
developer's site.


392 The evaluator should visit the site(s) in order to assess evidence for
development security. This may include sites of subcontractors involved in
the TOE development and production. Any decision not to visit shall be
agreed with the evaluation authority.


393 Although development security deals with the maintenance of the TOE and
hence with aspects becoming relevant after the completion of the evaluation,
the Development security (ALC_DVS) requirements specify only that the
development security measures be in place at the time of evaluation.
Furthermore, Development security (ALC_DVS) does not contain any
requirements related to the sponsor's intention to apply the development
security measures in the future, after completion of the evaluation.


394 It is recognised that confidentiality may not always be an issue for the
protection of the TOE in its development environment. The use of the word
“necessary” allows for the selection of appropriate safeguards.


**ALC_DVS.1** **Identification of security measures**


Dependencies: No dependencies.


Developer action elements:


**ALC_DVS.1.1D** **The developer shall produce and provide development security**

**documentation.**


Content and presentation elements:


**ALC_DVS.1.1C** **The development security documentation shall describe all the physical,**

**procedural, personnel, and other security measures that are necessary to**
**protect the confidentiality and integrity of the TOE design and**
**implementation in its development environment.**


April 2017 Version 3.1 Page 153 of 247


**Class ALC: Life-cycle support**


Evaluator action elements:


**ALC_DVS.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


**ALC_DVS.1.2E** **The evaluator** _**shall confirm**_ **that the security measures are being applied.**


**ALC_DVS.2** **Sufficiency of security measures**


Dependencies: No dependencies.


Developer action elements:


**ALC_DVS.2.1D** The developer shall produce and provide development security
documentation.


Content and presentation elements:


**ALC_DVS.2.1C** The development security documentation shall describe all the physical,

procedural, personnel, and other security measures that are necessary to
protect the confidentiality and integrity of the TOE design and
implementation in its development environment.


**ALC_DVS.2.2C** **The development security documentation shall justify that the security**

**measures provide the necessary level of protection to maintain the**
**confidentiality and integrity of the TOE.**


Evaluator action elements:


**ALC_DVS.2.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


**ALC_DVS.2.2E** The evaluator _**shall confirm**_ that the security measures are being applied.


Page 154 of 247 Version 3.1 April 2017


**Class ALC: Life-cycle support**

## **15.5 Flaw remediation (ALC_FLR)**


Objectives


395 Flaw remediation requires that discovered security flaws be tracked and
corrected by the developer. Although future compliance with flaw
remediation procedures cannot be determined at the time of the TOE
evaluation, it is possible to evaluate the policies and procedures that a
developer has in place to track and correct flaws, and to distribute the flaw
information and corrections.


Component levelling


396 The components in this family are levelled on the basis of the increasing
extent in scope of the flaw remediation procedures and the rigour of the flaw
remediation policies.


Application notes


397 This family provides assurance that the TOE will be maintained and
supported in the future, requiring the TOE developer to track and correct
flaws in the TOE. Additionally, requirements are included for the
distribution of flaw corrections. However, this family does not impose
evaluation requirements beyond the current evaluation.


398 The TOE user is considered to be the focal point in the user organisation that
is responsible for receiving and implementing fixes to security flaws. This is
not necessarily an individual user, but may be an organisational
representative who is responsible for the handling of security flaws. The use
of the term TOE user recognises that different organisations have different
procedures for handling flaw reporting, which may be done either by an
individual user, or by a central administrative body.


399 The flaw remediation procedures should describe the methods for dealing
with all types of flaws encountered. These flaws may be reported by the
developer, by users of the TOE, or by other parties with familiarity with the
TOE. Some flaws may not be reparable immediately. There may be some
occasions where a flaw cannot be fixed and other (e.g. procedural) measures
must be taken. The documentation provided should cover the procedures for
providing the operational sites with fixes, and providing information on
flaws where fixes are delayed (and what to do in the interim) or when fixes
are not possible.


400 Changes applied to a TOE after its release render it unevaluated; although
some information from the original evaluation may still apply. The phrase
“release of the TOE” used in this family therefore refers to a version of a
product that is a release of a certified TOE, to which changes have been
applied.


April 2017 Version 3.1 Page 155 of 247


**Class ALC: Life-cycle support**


**ALC_FLR.1** **Basic flaw remediation**


Dependencies: No dependencies.


Developer action elements:


**ALC_FLR.1.1D** **The developer shall document and provide flaw remediation procedures**

**addressed to TOE developers.**


Content and presentation elements:


**ALC_FLR.1.1C** **The flaw remediation procedures documentation shall describe the**

**procedures used to track all reported security flaws in each release of**
**the TOE.**


**ALC_FLR.1.2C** **The flaw remediation procedures shall require that a description of the**

**nature and effect of each security flaw be provided, as well as the status**
**of finding a correction to that flaw.**


**ALC_FLR.1.3C** **The flaw remediation procedures shall require that corrective actions be**

**identified for each of the security flaws.**


**ALC_FLR.1.4C** **The flaw remediation procedures documentation shall describe the**

**methods used to provide flaw information, corrections and guidance on**
**corrective actions to TOE users.**


Evaluator action elements:


**ALC_FLR.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


**ALC_FLR.2** **Flaw reporting procedures**


Dependencies: No dependencies.


Objectives


401 In order for the developer to be able to act appropriately upon security flaw
reports from TOE users, and to know to whom to send corrective fixes, TOE
users need to understand how to submit security flaw reports to the developer.
Flaw remediation guidance from the developer to the TOE user ensures that
TOE users are aware of this important information.


Developer action elements:


**ALC_FLR.2.1D** The developer shall document and provide flaw remediation procedures

addressed to TOE developers.


**ALC_FLR.2.2D** **The developer shall establish a procedure for accepting and acting upon**

**all reports of security flaws and requests for corrections to those flaws.**


Page 156 of 247 Version 3.1 April 2017


**Class ALC: Life-cycle support**


**ALC_FLR.2.3D** **The developer shall provide flaw remediation guidance addressed to**

**TOE users.**


Content and presentation elements:


**ALC_FLR.2.1C** The flaw remediation procedures documentation shall describe the

procedures used to track all reported security flaws in each release of the
TOE.


**ALC_FLR.2.2C** The flaw remediation procedures shall require that a description of the nature

and effect of each security flaw be provided, as well as the status of finding a
correction to that flaw.


**ALC_FLR.2.3C** The flaw remediation procedures shall require that corrective actions be

identified for each of the security flaws.


**ALC_FLR.2.4C** The flaw remediation procedures documentation shall describe the methods

used to provide flaw information, corrections and guidance on corrective
actions to TOE users.


**ALC_FLR.2.5C** **The flaw remediation procedures shall describe a means by which the**

**developer receives from TOE users reports and enquiries of suspected**
**security flaws in the TOE.**


**ALC_FLR.2.6C** **The procedures for processing reported security flaws shall ensure that**

**any reported flaws are remediated and the remediation procedures**
**issued to TOE users.**


**ALC_FLR.2.7C** **The procedures for processing reported security flaws shall provide**

**safeguards that any corrections to these security flaws do not introduce**
**any new flaws.**


**ALC_FLR.2.8C** **The flaw remediation guidance shall describe a means by which TOE**

**users report to the developer any suspected security flaws in the TOE.**


Evaluator action elements:


**ALC_FLR.2.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


**ALC_FLR.3** **Systematic flaw remediation**


Dependencies: No dependencies.


Objectives


402 In order for the developer to be able to act appropriately upon security flaw
reports from TOE users, and to know to whom to send corrective fixes, TOE
users need to understand how to submit security flaw reports to the developer,
and how to register themselves with the developer so that they may receive
these corrective fixes. Flaw remediation guidance from the developer to the
TOE user ensures that TOE users are aware of this important information.


April 2017 Version 3.1 Page 157 of 247


**Class ALC: Life-cycle support**


Developer action elements:


**ALC_FLR.3.1D** The developer shall document and provide flaw remediation procedures

addressed to TOE developers.


**ALC_FLR.3.2D** The developer shall establish a procedure for accepting and acting upon all

reports of security flaws and requests for corrections to those flaws.


**ALC_FLR.3.3D** The developer shall provide flaw remediation guidance addressed to TOE

users.


Content and presentation elements:


**ALC_FLR.3.1C** The flaw remediation procedures documentation shall describe the

procedures used to track all reported security flaws in each release of the
TOE.


**ALC_FLR.3.2C** The flaw remediation procedures shall require that a description of the nature

and effect of each security flaw be provided, as well as the status of finding a
correction to that flaw.


**ALC_FLR.3.3C** The flaw remediation procedures shall require that corrective actions be

identified for each of the security flaws.


**ALC_FLR.3.4C** The flaw remediation procedures documentation shall describe the methods

used to provide flaw information, corrections and guidance on corrective
actions to TOE users.


**ALC_FLR.3.5C** The flaw remediation procedures shall describe a means by which the

developer receives from TOE users reports and enquiries of suspected
security flaws in the TOE.


**ALC_FLR.3.6C** **The flaw remediation procedures shall include a procedure requiring**

**timely response and the automatic distribution of security flaw reports**
**and the associated corrections to registered users who might be affected**
**by the security flaw.**


**ALC_FLR.3.7C** The procedures for processing reported security flaws shall ensure that any

reported flaws are remediated and the remediation procedures issued to TOE
users.


**ALC_FLR.3.8C** The procedures for processing reported security flaws shall provide

safeguards that any corrections to these security flaws do not introduce any
new flaws.


**ALC_FLR.3.9C** The flaw remediation guidance shall describe a means by which TOE users

report to the developer any suspected security flaws in the TOE.


**ALC_FLR.3.10C** **The flaw remediation guidance shall describe a means by which TOE**

**users may register with the developer, to be eligible to receive security**
**flaw reports and corrections.**


Page 158 of 247 Version 3.1 April 2017


**Class ALC: Life-cycle support**


**ALC_FLR.3.11C** **The flaw remediation guidance shall identify the specific points of**

**contact for all reports and enquiries about security issues involving the**
**TOE.**


Evaluator action elements:


**ALC_FLR.3.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


April 2017 Version 3.1 Page 159 of 247


**Class ALC: Life-cycle support**

## **15.6 Life-cycle definition (ALC_LCD)**


Objectives


403 Poorly controlled development and maintenance of the TOE can result in a
TOE that does not meet all of its SFRs. Therefore, it is important that a
model for the development and maintenance of a TOE be established as early
as possible in the TOE's life-cycle.


404 Using a model for the development and maintenance of a TOE does not
guarantee that the TOE meets all of its SFRs. It is possible that the model
chosen will be insufficient or inadequate and therefore no benefits in the
quality of the TOE can be observed. Using a life-cycle model that has been
approved by a group of experts (e.g. academic experts, standards bodies)
improves the chances that the development and maintenance models will
contribute to the TOE meeting its SFRs. The use of a life-cycle model
including some quantitative valuation adds further assurance in the overall
quality of the TOE development process.


Component levelling


405 The components in this family are levelled on the basis of increasing
requirements for measurability of the life-cycle model, and for compliance
with that model.


Application notes


406 A life-cycle model encompasses the procedures, tools and techniques used to
develop and maintain the TOE. Aspects of the process that may be covered
by such a model include design methods, review procedures, project
management controls, change control procedures, test methods and
acceptance procedures. An effective life-cycle model will address these
aspects of the development and maintenance process within an overall
management structure that assigns responsibilities and monitors progress.


407 There are different types of acceptance situations that are dealt with at
different locations in the criteria: acceptance of parts delivered by
subcontractors (“integration”) should be treated in this family Life-cycle
definition (ALC_LCD), acceptance subsequent to internal transportations in
Development security (ALC_DVS), acceptance of parts into the CM system
in CM capabilities (ALC_CMC), and acceptance of the delivered TOE by
the consumer in Delivery (ALC_DEL). The first three types may overlap.


408 Although life-cycle definition deals with the maintenance of the TOE and
hence with aspects becoming relevant after the completion of the evaluation,
its evaluation adds assurance through an analysis of the life-cycle
information for the TOE provided at the time of the evaluation.


409 A life-cycle model provides for the necessary control over the development
and maintenance of the TOE, if the model enables sufficient minimisation of
the danger that the TOE will not meet its security requirement.


Page 160 of 247 Version 3.1 April 2017


**Class ALC: Life-cycle support**


410 A measurable life-cycle model is a model using some quantitative valuation
(arithmetic parameters and/or metrics) of the managed product in order to
measure development properties of the product. Typical metrics are source
code complexity metrics, defect density (errors per size of code) or mean
time to failure. For the security evaluation all those metrics are of relevance,
which are used to increase quality by decreasing the probability of faults and
thereby in turn increasing assurance in the security of the TOE.


411 One should take into account that there exist standardised life cycle models
on the one hand (like the waterfall model) and standardised metrics on the
other hand (like error density), which may be combined. The CC does not
require the life cycle to follow exactly one standard defining both aspects.


**ALC_LCD.1** **Developer defined life-cycle model**


Dependencies: No dependencies.


Developer action elements:


**ALC_LCD.1.1D** **The developer shall establish a life-cycle model to be used in the**

**development and maintenance of the TOE.**


**ALC_LCD.1.2D** **The developer shall provide life-cycle definition documentation.**


Content and presentation elements:


**ALC_LCD.1.1C** **The life-cycle definition documentation shall describe the model used to**

**develop and maintain the TOE.**


**ALC_LCD.1.2C** **The life-cycle model shall provide for the necessary control over the**

**development and maintenance of the TOE.**


Evaluator action elements:


**ALC_LCD.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


**ALC_LCD.2** **Measurable life-cycle model**


Dependencies: No dependencies.


Developer action elements:


**ALC_LCD.2.1D** The developer shall establish a life-cycle model to be used in the

development and maintenance of the TOE, **that** **is** **based** **on** **a** **measurable**
**life-cycle** **model.**


**ALC_LCD.2.2D** The developer shall provide life-cycle definition documentation.


**ALC_LCD.2.3D** **The developer shall measure the TOE development using the**

**measurable life-cycle model.**


April 2017 Version 3.1 Page 161 of 247


**Class ALC: Life-cycle support**


**ALC_LCD.2.4D** **The developer shall provide life-cycle output documentation.**


Content and presentation elements:


**ALC_LCD.2.1C** The life-cycle definition documentation shall describe the model used to

develop and maintain the TOE, **including** **the** **details** **of** **its** **arithmetic**
**parameters** **and/or** **metrics** **used** **to** **measure** **the** **quality** **of** **the** **TOE**
**and/or** **its** **development.**


**ALC_LCD.2.2C** The life-cycle model shall provide for the necessary control over the

development and maintenance of the TOE.


**ALC_LCD.2.3C** **The life-cycle output documentation shall provide the results of the**

**measurements of the TOE development using the measurable life-cycle**
**model.**


Evaluator action elements:


**ALC_LCD.2.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


Page 162 of 247 Version 3.1 April 2017


**Class ALC: Life-cycle support**

## **15.7 Tools and techniques (ALC_TAT)**


Objectives


412 Tools and techniques is an aspect of selecting tools that are used to develop,
analyse and implement the TOE. It includes requirements to prevent illdefined, inconsistent or incorrect development tools from being used to
develop the TOE. This includes, but is not limited to, programming
languages, documentation, implementation standards, and other parts of the
TOE such as supporting runtime libraries.


Component levelling


413 The components in this family are levelled on the basis of increasing
requirements on the description and scope of the implementation standards
and the documentation of implementation-dependent options.


Application notes


414 There is a requirement for well-defined development tools. These are tools
that are clearly and completely described. For example, programming
languages and computer aided design (CAD) systems that are based on a
standard published by standards bodies are considered to be well-defined.
Self-made tools would need further investigation to clarify whether they are
well-defined.


415 The requirement in **ALC_TAT.1.2C** is especially applicable to programming
languages so as to ensure that all statements in the source code have an
unambiguous meaning.


416 In ALC_TAT.2 and ALC_TAT.3, implementation guidelines may be
accepted as an implementation standard if they have been approved by some
group of experts (e.g. academic experts, standards bodies). Implementation
standards are normally public, well accepted and common practise in a
specific industry, but developer-specific implementation guidelines may also
be accepted as a standard; the emphasis is on the expertise.


417 Tools and techniques distinguishes between the implementation standards
applied by the developer ( **ALC_TAT.2.3D** ) and the implementation standards
for “all parts of the TOE” ( **ALC_TAT.3.3D** ) which include third party software,
hardware, or firmware. The configuration list introduced in CM scope
(ALC_CMS) requires that for each TSF relevant configuration item to
indicate if it has been generated by the TOE developer or by third party
developers.


April 2017 Version 3.1 Page 163 of 247


**Class ALC: Life-cycle support**


**ALC_TAT.1** **Well-defined development tools**


Dependencies: ADV_IMP.1 Implementation representation of the
TSF


Developer action elements:


**ALC_TAT.1.1D** **The developer shall provide the documentation identifying each**

**development tool being used for the TOE.**


**ALC_TAT.1.2D** **The developer shall document and provide the selected implementation-**

**dependent options of each development tool.**


Content and presentation elements:


**ALC_TAT.1.1C** **Each development tool used for implementation shall be well-defined.**


**ALC_TAT.1.2C** **The documentation of each development tool shall unambiguously**

**define the meaning of all statements as well as all conventions and**
**directives used in the implementation.**


**ALC_TAT.1.3C** **The documentation of each development tool shall unambiguously**

**define the meaning of all implementation-dependent options.**


Evaluator action elements:


**ALC_TAT.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


**ALC_TAT.2** **Compliance with implementation standards**


Dependencies: ADV_IMP.1 Implementation representation of the
TSF


Developer action elements:


**ALC_TAT.2.1D** The developer shall provide the documentation identifying each development

tool being used for the TOE.


**ALC_TAT.2.2D** The developer shall document and provide the selected implementation
dependent options of each development tool.


**ALC_TAT.2.3D** **The developer shall describe and provide the implementation standards**

**that are being applied by the developer.**


Content and presentation elements:


**ALC_TAT.2.1C** Each development tool used for implementation shall be well-defined.


**ALC_TAT.2.2C** The documentation of each development tool shall unambiguously define the

meaning of all statements as well as all conventions and directives used in
the implementation.


Page 164 of 247 Version 3.1 April 2017


**Class ALC: Life-cycle support**


**ALC_TAT.2.3C** The documentation of each development tool shall unambiguously define the

meaning of all implementation-dependent options.


Evaluator action elements:


**ALC_TAT.2.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


**ALC_TAT.2.2E** **The evaluator** _**shall confirm**_ **that the implementation standards have**

**been applied.**


**ALC_TAT.3** **Compliance with implementation standards - all parts**


Dependencies: ADV_IMP.1 Implementation representation of the
TSF


Developer action elements:


**ALC_TAT.3.1D** The developer shall provide the documentation identifying each development

tool being used for the TOE.


**ALC_TAT.3.2D** The developer shall document and provide the selected implementation
dependent options of each development tool.


**ALC_TAT.3.3D** The developer shall describe and provide the implementation standards that

are being applied by the developer **and** **by** **any** **third-party** **providers** **for**
**all** **parts** **of** **the** **TOE.**


Content and presentation elements:


**ALC_TAT.3.1C** Each development tool used for implementation shall be well-defined.


**ALC_TAT.3.2C** The documentation of each development tool shall unambiguously define the

meaning of all statements as well as all conventions and directives used in
the implementation.


**ALC_TAT.3.3C** The documentation of each development tool shall unambiguously define the

meaning of all implementation-dependent options.


Evaluator action elements:


**ALC_TAT.3.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


**ALC_TAT.3.2E** The evaluator _**shall confirm**_ that the implementation standards have been

applied.


April 2017 Version 3.1 Page 165 of 247


**Class ATE: Tests**

# **16 Class ATE: Tests**


418 The class “Tests” encompasses four families: Coverage (ATE_COV), Depth
(ATE_DPT), Independent testing (ATE_IND) (i.e. functional testing
performed by evaluators), and Functional tests (ATE_FUN). Testing
provides assurance that the TSF behaves as described (in the functional
specification, TOE design, and implementation representation).


419 The emphasis in this class is on confirmation that the TSF operates according
to its design descriptions. This class does not address penetration testing,
which is based upon an analysis of the TSF that specifically seeks to identify
vulnerabilities in the design and implementation of the TSF. Penetration
testing is addressed separately as an aspect of vulnerability assessment in the
AVA: Vulnerability assessment class.


420 The ATE: Tests class separates testing into developer testing and evaluator
testing. The Coverage (ATE_COV) and Depth (ATE_DPT) families address
the completeness of developer testing. Coverage (ATE_COV) addresses the
rigour with which the functional specification is tested; Depth (ATE_DPT)
addresses whether testing against other design descriptions (security
architecture, TOE design, implementation representation) is required.


421 Functional tests (ATE_FUN) addresses the performing of the tests by the
developer and how this testing should be documented. Finally, Independent
testing (ATE_IND) then addresses evaluator testing: whether the evaluator
should repeat part or all of the developer testing and how much independent
testing the evaluator should do.


422 Figure 15 shows the families within this class, and the hierarchy of
components within the families.


**Figure 15 - ATE: Tests class decomposition**


Page 166 of 247 Version 3.1 April 2017


**Class ATE: Tests**

## **16.1 Coverage (ATE_COV)**


Objectives


423 This family establishes that the TSF has been tested against its functional
specification. This is achieved through an examination of developer evidence
of correspondence.


Component levelling


424 The components in this family are levelled on the basis of specification.


Application notes


**ATE_COV.1** **Evidence of coverage**


Dependencies: ADV_FSP.2 Security-enforcing functional
specification
ATE_FUN.1 Functional testing


Objectives


425 The objective of this component is to establish that some of the TSFIs have
been tested.


Application notes


426 In this component the developer shows how tests in the test documentation
correspond to TSFIs in the functional specification. This can be achieved by
a statement of correspondence, perhaps using a table.


Developer action elements:


**ATE_COV.1.1D** **The developer shall provide evidence of the test coverage.**


Content and presentation elements:


**ATE_COV.1.1C** **The evidence of the test coverage shall show the correspondence between**

**the tests in the test documentation and the TSFIs in the functional**
**specification.**


Evaluator action elements:


**ATE_COV.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


**ATE_COV.2** **Analysis of coverage**


Dependencies: ADV_FSP.2 Security-enforcing functional
specification
ATE_FUN.1 Functional testing


April 2017 Version 3.1 Page 167 of 247


**Class ATE: Tests**


Objectives


427 The objective of this component is to confirm that all of the TSFIs have been
tested.


Application notes


428 In this component the developer confirms that tests in the test documentation
correspond to all of the TSFIs in the functional specification. This can be
achieved by a statement of correspondence, perhaps using a table, but the
developer also provides an analysis of the test coverage.


Developer action elements:


**ATE_COV.2.1D** The developer shall provide **an** **analysis** of the test coverage.


Content and presentation elements:


**ATE_COV.2.1C** The **analysis** of the test coverage shall **demonstrate** the correspondence

between the tests in the test documentation and the TSFIs in the functional
specification.


**ATE_COV.2.2C** **The analysis of the test coverage shall demonstrate that all TSFIs in the**

**functional specification have been tested.**


Evaluator action elements:


**ATE_COV.2.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


**ATE_COV.3** **Rigorous analysis of coverage**


Dependencies: ADV_FSP.2 Security-enforcing functional
specification
ATE_FUN.1 Functional testing


Objectives


429 In this component, the objective is to confirm that the developer performed
exhaustive tests of all interfaces in the functional specification.


430 The objective of this component is to confirm that all parameters of all of the
TSFIs have been tested.


Application notes


431 In this component the developer is required to show how tests in the test
documentation correspond to all of the TSFIs in the functional specification.
This can be achieved by a statement of correspondence, perhaps using a table,
but in addition the developer is required to demonstrate that the tests exercise
all of the parameters of all TSFIs. This additional requirement includes
bounds testing (i.e. verifying that errors are generated when stated limits are


Page 168 of 247 Version 3.1 April 2017


**Class ATE: Tests**


exceeded) and negative testing (e.g. when access is given to User A,
verifying not only that User A now has access, but also that User B did not
suddenly gain access). This kind of testing is not, strictly speaking,
_exhaustive_ because not every possible value of the parameters is expected to
be checked.


Developer action elements:


**ATE_COV.3.1D** The developer shall provide an analysis of the test coverage.


Content and presentation elements:


**ATE_COV.3.1C** The analysis of the test coverage shall demonstrate the correspondence

between the tests in the test documentation and the TSFIs in the functional
specification.


**ATE_COV.3.2C** The analysis of the test coverage shall demonstrate that all TSFIs in the

functional specification have been **completely** tested.


Evaluator action elements:


**ATE_COV.3.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


April 2017 Version 3.1 Page 169 of 247


**Class ATE: Tests**

## **16.2 Depth (ATE_DPT)**


Objectives


432 The components in this family deal with the level of detail to which the TSF
is tested by the developer. Testing of the TSF is based upon increasing depth
of information derived from additional design representations and
descriptions (TOE design, implementation representation, and security
architecture description).


433 The objective is to counter the risk of missing an error in the development of
the TOE. Testing that exercises specific internal interfaces can provide
assurance not only that the TSF exhibits the desired external security
behaviour, but also that this behaviour stems from correctly operating
internal functionality.


Component levelling


434 The components in this family are levelled on the basis of increasing detail
provided in the TSF representations, from the TOE design to the
implementation representation. This levelling reflects the TSF
representations presented in the ADV class.


Application notes


435 The TOE design describes the internal components (e.g. subsystems) and,
perhaps, modules of the TSF, together with a description of the interfaces
among these components and modules. Evidence of testing of this TOE
design must show that the internal interfaces have been exercised and seen to
behave as described. This may be achieved through testing via the external
interfaces of the TSF, or by testing of the TOE subsystem or module
interfaces in isolation, perhaps employing a test harness. In cases where
some aspects of an internal interface cannot be tested via the external
interfaces, there should either be justification that these aspects need not be
tested, or the internal interface needs to be tested directly. In the latter case
the TOE design needs to be sufficiently detailed in order to facilitate direct
testing.


436 In cases where the description of the TSF's architectural soundness (in
Security Architecture (ADV_ARC)) cites specific mechanisms, the tests
performed by the developer must show that the mechanisms have been
exercised and seen to behave as described.


437 At the highest component of this family, the testing is performed not only
against the TOE design, but also against the implementation representation.


**ATE_DPT.1** **Testing: basic design**


Dependencies: ADV_ARC.1 Security architecture description
ADV_TDS.2 Architectural design
ATE_FUN.1 Functional testing


Page 170 of 247 Version 3.1 April 2017


**Class ATE: Tests**


Objectives


438 The subsystem descriptions of the TSF provide a high-level description of
the internal workings of the TSF. Testing at the level of the TOE subsystems
provides assurance that the TSF subsystems behave and interact as described
in the TOE design and the security architecture description.


Developer action elements:


**ATE_DPT.1.1D** **The developer shall provide the analysis of the depth of testing.**


Content and presentation elements:


**ATE_DPT.1.1C** **The analysis of the depth of testing shall demonstrate the**

**correspondence between the tests in the test documentation and the TSF**
**subsystems in the TOE design.**


**ATE_DPT.1.2C** **The analysis of the depth of testing shall demonstrate that all TSF**

**subsystems in the TOE design have been tested.**


Evaluator action elements:


**ATE_DPT.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


**ATE_DPT.2** **Testing: security enforcing modules**


Dependencies: ADV_ARC.1 Security architecture description
ADV_TDS.3 Basic modular design
ATE_FUN.1 Functional testing


Objectives


439 The subsystem and module descriptions of the TSF provide a high-level
description of the internal workings, and a description of the interfaces of the
SFR-enforcing modules, of the TSF. Testing at this level of TOE description
provides assurance that the TSF subsystems and SFR-enforcing modules
behave and interact as described in the TOE design and the security
architecture description.


Developer action elements:


**ATE_DPT.2.1D** The developer shall provide the analysis of the depth of testing.


Content and presentation elements:


**ATE_DPT.2.1C** The analysis of the depth of testing shall demonstrate the correspondence

between the tests in the test documentation and the TSF subsystems **and**
**SFR-enforcing** **modules** in the TOE design.


**ATE_DPT.2.2C** The analysis of the depth of testing shall demonstrate that all TSF

subsystems in the TOE design have been tested.


April 2017 Version 3.1 Page 171 of 247


**Class ATE: Tests**


**ATE_DPT.2.3C** **The analysis of the depth of testing shall demonstrate that the SFR-**

**enforcing modules in the TOE design have been tested.**


Evaluator action elements:


**ATE_DPT.2.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


**ATE_DPT.3** **Testing: modular design**


Dependencies: ADV_ARC.1 Security architecture description
ADV_TDS.4 Semiformal modular design
ATE_FUN.1 Functional testing


Objectives


440 The subsystem and module descriptions of the TSF provide a high-level
description of the internal workings, and a description of the interfaces of the
modules, of the TSF. Testing at this level of TOE description provides
assurance that the TSF subsystems and modules behave and interact as
described in the TOE design and the security architecture description.


Developer action elements:


**ATE_DPT.3.1D** The developer shall provide the analysis of the depth of testing.


Content and presentation elements:


**ATE_DPT.3.1C** The analysis of the depth of testing shall demonstrate the correspondence

between the tests in the test documentation and the TSF subsystems and
modules in the TOE design.


**ATE_DPT.3.2C** The analysis of the depth of testing shall demonstrate that all TSF

subsystems in the TOE design have been tested.


**ATE_DPT.3.3C** The analysis of the depth of testing shall demonstrate that **all** **TSF** modules

in the TOE design have been tested.


Evaluator action elements:


**ATE_DPT.3.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


**ATE_DPT.4** **Testing: implementation representation**


Dependencies: ADV_ARC.1 Security architecture description
ADV_TDS.4 Semiformal modular design
ADV_IMP.1 Implementation representation of the
TSF
ATE_FUN.1 Functional testing


Page 172 of 247 Version 3.1 April 2017


**Class ATE: Tests**


Objectives


441 The subsystem and module descriptions of the TSF provide a high-level
description of the internal workings, and a description of the interfaces of the
modules, of the TSF. Testing at this level of TOE description provides
assurance that the TSF subsystems and modules behave and interact as
described in the TOE design and the security architecture description, and in
accordance with the implementation representation.


Developer action elements:


**ATE_DPT.4.1D** The developer shall provide the analysis of the depth of testing.


Content and presentation elements:


**ATE_DPT.4.1C** The analysis of the depth of testing shall demonstrate the correspondence

between the tests in the test documentation and the TSF subsystems and
modules in the TOE design.


**ATE_DPT.4.2C** The analysis of the depth of testing shall demonstrate that all TSF

subsystems in the TOE design have been tested.


**ATE_DPT.4.3C** The analysis of the depth of testing shall demonstrate that all modules in the

TOE design have been tested.


**ATE_DPT.4.4C** **The analysis of the depth of testing shall demonstrate that the TSF**

**operates in accordance with its implementation representation.**


Evaluator action elements:


**ATE_DPT.4.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


April 2017 Version 3.1 Page 173 of 247


**Class ATE: Tests**

## **16.3 Functional tests (ATE_FUN)**


Objectives


442 Functional testing performed by the developer provides assurance that the
tests in the test documentation are performed and documented correctly. The
correspondence of these tests to the design descriptions of the TSF is
achieved through the Coverage (ATE_COV) and Depth (ATE_DPT)
families.


443 This family contributes to providing assurance that the likelihood of
undiscovered flaws is relatively small.


444 The families Coverage (ATE_COV), Depth (ATE_DPT) and Functional tests
(ATE_FUN) are used in combination to define the evidence of testing to be
supplied by a developer. Independent functional testing by the evaluator is
specified by Independent testing (ATE_IND).


Component levelling


445 This family contains two components, the higher requiring that ordering
dependencies are analysed.


Application notes


446 Procedures for performing tests are expected to provide instructions for using
test programs and test suites, including the test environment, test conditions,
test data parameters and values. The test procedures should also show how
the test results are derived from the test inputs.


447 Ordering dependencies are relevant when the successful execution of a
particular test depends upon the existence of a particular state. For example,
this might require that test A be executed immediately before test B, since
the state resulting from the successful execution of test A is a prerequisite for
the successful execution of test B. Thus, failure of test B could be related to a
problem with the ordering dependencies. In the above example, test B could
fail because test C (rather than test A) was executed immediately before it, or
the failure of test B could be related to a failure of test A.


**ATE_FUN.1** **Functional testing**


Dependencies: ATE_COV.1 Evidence of coverage


Objectives


448 The objective is for the developer to demonstrate that the tests in the test
documentation are performed and documented correctly.


Developer action elements:


**ATE_FUN.1.1D** **The developer shall test the TSF and document the results.**


Page 174 of 247 Version 3.1 April 2017


**Class ATE: Tests**


**ATE_FUN.1.2D** **The developer shall provide test documentation.**


Content and presentation elements:


**ATE_FUN.1.1C** **The test documentation shall consist of test plans, expected test results**

**and actual test results.**


**ATE_FUN.1.2C** **The test plans shall identify the tests to be performed and describe the**

**scenarios for performing each test. These scenarios shall include any**
**ordering dependencies on the results of other tests.**


**ATE_FUN.1.3C** **The expected test results shall show the anticipated outputs from a**

**successful execution of the tests.**


**ATE_FUN.1.4C** **The actual test results shall be consistent with the expected test results.**


Evaluator action elements:


**ATE_FUN.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


**ATE_FUN.2** **Ordered functional testing**


Dependencies: ATE_COV.1 Evidence of coverage


Objectives


449 The objectives are for the developer to demonstrate that the tests in the test
documentation are performed and documented correctly, and to ensure that
testing is structured such as to avoid circular arguments about the correctness
of the interfaces being tested.


Application notes


450 Although the test procedures may state pre-requisite initial test conditions in
terms of ordering of tests, they may not provide a rationale for the ordering.
An analysis of test ordering is an important factor in determining the
adequacy of testing, as there is a possibility of faults being concealed by the
ordering of tests.


Developer action elements:


**ATE_FUN.2.1D** The developer shall test the TSF and document the results.


**ATE_FUN.2.2D** The developer shall provide test documentation.


Content and presentation elements:


**ATE_FUN.2.1C** The test documentation shall consist of test plans, expected test results and

actual test results.


April 2017 Version 3.1 Page 175 of 247


**Class ATE: Tests**


**ATE_FUN.2.2C** The test plans shall identify the tests to be performed and describe the

scenarios for performing each test. These scenarios shall include any
ordering dependencies on the results of other tests.


**ATE_FUN.2.3C** The expected test results shall show the anticipated outputs from a successful

execution of the tests.


**ATE_FUN.2.4C** The actual test results shall be consistent with the expected test results.


**ATE_FUN.2.5C** **The test documentation shall include an analysis of the test procedure**

**ordering dependencies.**


Evaluator action elements:


**ATE_FUN.2.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


Page 176 of 247 Version 3.1 April 2017


**Class ATE: Tests**

## **16.4 Independent testing (ATE_IND)**


Objectives


451 The objectives of this family are built upon the assurances achieved in the
ATE_FUN, ATE_COV, and ATE_DPT families by verifying the developer
testing and performing additional tests by the evaluator.


Component levelling


452 Levelling is based upon the amount of developer test documentation and test
support and the amount of evaluator testing.


Application notes


453 This family deals with the degree to which there is independent functional
testing of the TSF. Independent functional testing may take the form of
repeating the developer's functional tests (in whole or in part) or of extending
the scope or the depth of the developer's tests. These activities are
complementary, and an appropriate mix must be planned for each TOE,
which takes into account the availability and coverage of test results, and the
functional complexity of the TSF.


454 Sampling of developer tests is intended to provide confirmation that the
developer has carried out his planned test programme on the TSF, and has
correctly recorded the results. The size of sample selected will be influenced
by the detail and quality of the developer's functional test results. The
evaluator will also need to consider the scope for devising additional tests,
and the relative benefit that may be gained from effort in these two areas. It
is recognised that repetition of all developer tests may be feasible and
desirable in some cases, but may be very arduous and less productive in
others. The highest component in this family should therefore be used with
caution. Sampling will address the whole range of test results available,
including those supplied to meet the requirements of both Coverage
(ATE_COV) and Depth (ATE_DPT).


455 There is also a need to consider the different configurations of the TOE that
are included within the evaluation. The evaluator will need to assess the
applicability of the results provided, and to plan his own testing accordingly.


456 The suitability of the TOE for testing is based on the access to the TOE, and
the supporting documentation and information required (including any test
software or tools) to run tests. The need for such support is addressed by the
dependencies to other assurance families.


457 Additionally, suitability of the TOE for testing may be based on other
considerations. For example, the version of the TOE submitted by the
developer may not be the final version.


458 The term _interfaces_ refers to interfaces described in the functional
specification and TOE design, and parameters passed through invocations


April 2017 Version 3.1 Page 177 of 247


**Class ATE: Tests**


identified in the implementation representation. The exact set of interfaces to
be used is selected through Coverage (ATE_COV) and the Depth
(ATE_DPT) components.


459 References to a subset of the interfaces are intended to allow the evaluator to
design an appropriate set of tests which is consistent with the objectives of
the evaluation being conducted.


**ATE_IND.1** **Independent testing - conformance**


Dependencies: ADV_FSP.1 Basic functional specification
AGD_OPE.1 Operational user guidance
AGD_PRE.1 Preparative procedures


Objectives


460 In this component, the objective is to demonstrate that the TOE operates in
accordance with its design representations and guidance documents.


Application notes


461 This component does not address the use of developer test results. It is
applicable where such results are not available, and also in cases where the
developer's testing is accepted without validation. The evaluator is required
to devise and conduct tests with the objective of confirming that the TOE
operates in accordance with its design representations, including but not
limited to the functional specification. The approach is to gain confidence in
correct operation through representative testing, rather than to conduct every
possible test. The extent of testing to be planned for this purpose is a
methodology issue, and needs to be considered in the context of a particular
TOE and the balance of other evaluation activities.


Developer action elements:


**ATE_IND.1.1D** **The developer shall provide the TOE for testing.**


Content and presentation elements:


**ATE_IND.1.1C** **The TOE shall be suitable for testing.**


Evaluator action elements:


**ATE_IND.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**
**requirements for content and presentation of evidence.**


**ATE_IND.1.2E** **The evaluator** _**shall test**_ **a subset of the TSF to confirm that the TSF**
**operates as specified.**


Page 178 of 247 Version 3.1 April 2017


**Class ATE: Tests**


**ATE_IND.2** **Independent testing - sample**


Dependencies: ADV_FSP.2 Security-enforcing functional
specification
AGD_OPE.1 Operational user guidance
AGD_PRE.1 Preparative procedures
ATE_COV.1 Evidence of coverage
ATE_FUN.1 Functional testing


Objectives


462 In this component, the objective is to demonstrate that the TOE operates in
accordance with its design representations and guidance documents.
Evaluator testing confirms that the developer performed some tests of some
interfaces in the functional specification.


Application notes


463 The intent is that the developer should provide the evaluator with materials
necessary for the efficient reproduction of developer tests. This may include
such things as machine-readable test documentation, test programs, etc.


464 This component contains a requirement that the evaluator has available test
results from the developer to supplement the programme of testing. The
evaluator will repeat a sample of the developer's tests to gain confidence in
the results obtained. Having established such confidence the evaluator will
build upon the developer's testing by conducting additional tests that exercise
the TOE in a different manner. By using a platform of validated developer
test results the evaluator is able to gain confidence that the TOE operates
correctly in a wider range of conditions than would be possible purely using
the developer's own efforts, given a fixed level of resource. Having gained
confidence that the developer has tested the TOE, the evaluator will also
have more freedom, where appropriate, to concentrate testing in areas where
examination of documentation or specialist knowledge has raised particular
concerns.


Developer action elements:


**ATE_IND.2.1D** The developer shall provide the TOE for testing.


Content and presentation elements:


**ATE_IND.2.1C** The TOE shall be suitable for testing.


**ATE_IND.2.2C** **The developer shall provide an equivalent set of resources to those that**

**were used in the developer's functional testing of the TSF.**


Evaluator action elements:


**ATE_IND.2.1E** The evaluator _**shall confirm**_ that the information provided meets all
requirements for content and presentation of evidence.


April 2017 Version 3.1 Page 179 of 247


**Class ATE: Tests**


**ATE_IND.2.2E** **The evaluator** _**shall execute**_ **a sample of tests in the test documentation to**
**verify the developer test results.**


**ATE_IND.2.3E** The evaluator _**shall test**_ a subset of the TSF to confirm that the TSF operates
as specified.


**ATE_IND.3** **Independent testing - complete**


Dependencies: ADV_FSP.4 Complete functional specification
AGD_OPE.1 Operational user guidance
AGD_PRE.1 Preparative procedures
ATE_COV.1 Evidence of coverage
ATE_FUN.1 Functional testing


Objectives


465 In this component, the objective is to demonstrate that the TOE operates in
accordance with its design representations and guidance documents.
Evaluator testing includes repeating all of the developer tests.


Application notes


466 The intent is that the developer should provide the evaluator with materials
necessary for the efficient reproduction of developer tests. This may include
such things as machine-readable test documentation, test programs, etc.


467 In this component the evaluator must repeat all of the developer's tests as
part of the programme of testing. As in the previous component the evaluator
will also conduct tests that aim to exercise the TSF in a different manner
from that achieved by the developer. In cases where developer testing has
been exhaustive, there may remain little scope for this.


Developer action elements:


**ATE_IND.3.1D** The developer shall provide the TOE for testing.


Content and presentation elements:


**ATE_IND.3.1C** The TOE shall be suitable for testing.


**ATE_IND.3.2C** The developer shall provide an equivalent set of resources to those that were

used in the developer's functional testing of the TSF.


Evaluator action elements:


**ATE_IND.3.1E** The evaluator _**shall confirm**_ that the information provided meets all
requirements for content and presentation of evidence.


**ATE_IND.3.2E** The evaluator _**shall execute**_ **all** tests in the test documentation to verify the
developer test results.


Page 180 of 247 Version 3.1 April 2017


**Class ATE: Tests**


**ATE_IND.3.3E** The evaluator _**shall test**_ the TSF to confirm that the **entire** TSF operates as
specified.


April 2017 Version 3.1 Page 181 of 247


**Class AVA: Vulnerability assessment**

# **17 Class AVA: Vulnerability assessment**


468 The AVA: Vulnerability assessment class addresses the possibility of
exploitable vulnerabilities introduced in the development or the operation of
the TOE.


469 Figure 16 shows the families within this class, and the hierarchy of
components within the families.


**Figure 16 - AVA: Vulnerability assessment class decomposition**


Application notes


470 Generally, the vulnerability assessment activity covers various vulnerabilities
in the development and operation of the TOE. Development vulnerabilities
take advantage of some property of the TOE which was introduced during its
development, e.g. defeating the TSF self protection through tampering, direct
attack or monitoring of the TSF, defeating the TSF domain separation
through monitoring or direct attack the TSF, or defeating non-bypassability
through circumventing (bypassing) the TSF. Operational vulnerabilities take
advantage of weaknesses in non-technical countermeasures to violate the
TOE SFRs, e.g. misuse or incorrect configuration. Misuse investigates
whether the TOE can be configured or used in a manner that is insecure, but
that an administrator or user of the TOE would reasonably believe to be
secure.


471 Assessment of development vulnerabilities is covered by the assurance
family AVA_VAN. Basically, all development vulnerabilities can be
considered in the context of AVA_VAN due to the fact, that this family
allows application of a wide range of assessment methodologies being
unspecific to the kind of an attack scenario. These unspecific assessment
methodologies comprise, among other, also the specific methodologies for
those TSF where covert channels are to be considered (a channel capacity
estimation can be done using informal engineering measurements, as well as
actual test measurements) or can be overcome by the use of sufficient
resources in the form of a direct attack (underlying technical concept of those
TSF is based on probabilistic or permutational mechanisms; a qualification
of their security behaviour and the effort required to overcome them can be
made using a quantitative or statistical analysis).


472 If there are security objectives specified in the ST to either to prevent one
user of the TOE from observing activity associated with another user of the
TOE, or to ensure that information flows cannot be used to achieve enforced
illicit data signals, covert channel analysis should be considered during the
conduct of the vulnerability analysis. This is often reflected by the inclusion
of Unobservability (FPR_UNO) and multilevel access control policies


Page 182 of 247 Version 3.1 April 2017


**Class AVA: Vulnerability assessment**


specified through Access control policy (FDP_ACC) and/or Information
flow control policy (FDP_IFC) requirements in the ST.


April 2017 Version 3.1 Page 183 of 247


**Class AVA: Vulnerability assessment**

## **17.1 Vulnerability analysis (AVA_VAN)**


Objectives


473 Vulnerability analysis is an assessment to determine whether potential
vulnerabilities identified, during the evaluation of the development and
anticipated operation of the TOE or by other methods (e.g. by flaw
hypotheses or quantitative or statistical analysis of the security behaviour of
the underlying security mechanisms), could allow attackers to violate the
SFRs.


474 Vulnerability analysis deals with the threats that an attacker will be able to
discover flaws that will allow unauthorised access to data and functionality,
allow the ability to interfere with or alter the TSF, or interfere with the
authorised capabilities of other users.


Component levelling


475 Levelling is based on an increasing rigour of vulnerability analysis by the
evaluator and increased levels of attack potential required by an attacker to
identify and exploit the potential vulnerabilities.


**AVA_VAN.1** **Vulnerability survey**


Dependencies: ADV_FSP.1 Basic functional specification
AGD_OPE.1 Operational user guidance
AGD_PRE.1 Preparative procedures


Objectives


476 A vulnerability survey of information available in the public domain is
performed by the evaluator to ascertain potential vulnerabilities that may be
easily found by an attacker.


477 The evaluator performs penetration testing, to confirm that the potential
vulnerabilities cannot be exploited in the operational environment for the
TOE. Penetration testing is performed by the evaluator assuming an attack
potential of Basic.


Developer action elements:


**AVA_VAN.1.1D** **The developer shall provide the TOE for testing.**


Content and presentation elements:


**AVA_VAN.1.1C** **The TOE shall be suitable for testing.**


Evaluator action elements:


**AVA_VAN.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


Page 184 of 247 Version 3.1 April 2017


**Class AVA: Vulnerability assessment**


**AVA_VAN.1.2E** **The evaluator** _**shall perform**_ **a search of public domain sources to**

**identify potential vulnerabilities in the TOE.**


**AVA_VAN.1.3E** **The evaluator** _**shall conduct**_ **penetration testing, based on the identified**

**potential vulnerabilities, to determine that the TOE is resistant to**
**attacks performed by an attacker possessing Basic attack potential.**


**AVA_VAN.2** **Vulnerability analysis**


Dependencies: ADV_ARC.1 Security architecture description
ADV_FSP.2 Security-enforcing functional
specification
ADV_TDS.1 Basic design
AGD_OPE.1 Operational user guidance
AGD_PRE.1 Preparative procedures


Objectives


478 A vulnerability analysis is performed by the evaluator to ascertain the
presence of potential vulnerabilities.


479 The evaluator performs penetration testing, to confirm that the potential
vulnerabilities cannot be exploited in the operational environment for the
TOE. Penetration testing is performed by the evaluator assuming an attack
potential of Basic.


Developer action elements:


**AVA_VAN.2.1D** The developer shall provide the TOE for testing.


Content and presentation elements:


**AVA_VAN.2.1C** The TOE shall be suitable for testing.


Evaluator action elements:


**AVA_VAN.2.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


**AVA_VAN.2.2E** The evaluator _**shall perform**_ a search of public domain sources to identify

potential vulnerabilities in the TOE.


**AVA_VAN.2.3E** **The evaluator** _**shall perform**_ **an independent vulnerability analysis of the**

**TOE using the guidance documentation, functional specification, TOE**
**design and security architecture description to identify potential**
**vulnerabilities in the TOE.**


**AVA_VAN.2.4E** The evaluator _**shall conduct**_ penetration testing, based on the identified

potential vulnerabilities, to determine that the TOE is resistant to attacks
performed by an attacker possessing Basic attack potential.


April 2017 Version 3.1 Page 185 of 247


**Class AVA: Vulnerability assessment**


**AVA_VAN.3** **Focused vulnerability analysis**


Dependencies: ADV_ARC.1 Security architecture description
ADV_FSP.4 Complete functional specification
ADV_TDS.3 Basic modular design
ADV_IMP.1 Implementation representation of the
TSF
AGD_OPE.1 Operational user guidance
AGD_PRE.1 Preparative procedures
ATE_DPT.1 Testing: basic design


Objectives


480 A vulnerability analysis is performed by the evaluator to ascertain the
presence of potential vulnerabilities.


481 The evaluator performs penetration testing, to confirm that the potential
vulnerabilities cannot be exploited in the operational environment for the
TOE. Penetration testing is performed by the evaluator assuming an attack
potential of Enhanced-Basic.


Developer action elements:


**AVA_VAN.3.1D** The developer shall provide the TOE for testing.


Content and presentation elements:


**AVA_VAN.3.1C** The TOE shall be suitable for testing.


Evaluator action elements:


**AVA_VAN.3.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


**AVA_VAN.3.2E** The evaluator _**shall perform**_ a search of public domain sources to identify

potential vulnerabilities in the TOE.


**AVA_VAN.3.3E** The evaluator _**shall perform**_ an independent, **focused** vulnerability analysis

of the TOE using the guidance documentation, functional specification, TOE
design, security architecture description **and** **implementation**
**representation** to identify potential vulnerabilities in the TOE.


**AVA_VAN.3.4E** The evaluator _**shall conduct**_ penetration testing, based on the identified

potential vulnerabilities, to determine that the TOE is resistant to attacks
performed by an attacker possessing **Enhanced-Basic** attack potential.


**AVA_VAN.4** **Methodical vulnerability analysis**


Dependencies: ADV_ARC.1 Security architecture description
ADV_FSP.4 Complete functional specification
ADV_TDS.3 Basic modular design


Page 186 of 247 Version 3.1 April 2017


**Class AVA: Vulnerability assessment**


ADV_IMP.1 Implementation representation of the
TSF
AGD_OPE.1 Operational user guidance
AGD_PRE.1 Preparative procedures
ATE_DPT.1 Testing: basic design


Objectives


482 A methodical vulnerability analysis is performed by the evaluator to
ascertain the presence of potential vulnerabilities.


483 The evaluator performs penetration testing, to confirm that the potential
vulnerabilities cannot be exploited in the operational environment for the
TOE. Penetration testing is performed by the evaluator assuming an attack
potential of Moderate.


Developer action elements:


**AVA_VAN.4.1D** The developer shall provide the TOE for testing.


Content and presentation elements:


**AVA_VAN.4.1C** The TOE shall be suitable for testing.


Evaluator action elements:


**AVA_VAN.4.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


**AVA_VAN.4.2E** The evaluator _**shall perform**_ a search of public domain sources to identify

potential vulnerabilities in the TOE.


**AVA_VAN.4.3E** The evaluator _**shall perform**_ an independent, **methodical** vulnerability

analysis of the TOE using the guidance documentation, functional
specification, TOE design, security architecture description and
implementation representation to identify potential vulnerabilities in the TOE.


**AVA_VAN.4.4E** The evaluator _**shall conduct**_ penetration testing based on the identified

potential vulnerabilities to determine that the TOE is resistant to attacks
performed by an attacker possessing **Moderate** attack potential.


**AVA_VAN.5** **Advanced methodical vulnerability analysis**


Dependencies: ADV_ARC.1 Security architecture description
ADV_FSP.4 Complete functional specification
ADV_TDS.3 Basic modular design
ADV_IMP.1 Implementation representation of the
TSF
AGD_OPE.1 Operational user guidance
AGD_PRE.1 Preparative procedures
ATE_DPT.1 Testing: basic design


April 2017 Version 3.1 Page 187 of 247


**Class AVA: Vulnerability assessment**


Objectives


484 A methodical vulnerability analysis is performed by the evaluator to
ascertain the presence of potential vulnerabilities.


485 The evaluator performs penetration testing, to confirm that the potential
vulnerabilities cannot be exploited in the operational environment for the
TOE. Penetration testing is performed by the evaluator assuming an attack
potential of High.


Developer action elements:


**AVA_VAN.5.1D** The developer shall provide the TOE for testing.


Content and presentation elements:


**AVA_VAN.5.1C** The TOE shall be suitable for testing.


Evaluator action elements:


**AVA_VAN.5.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


**AVA_VAN.5.2E** The evaluator _**shall perform**_ a search of public domain sources to identify

potential vulnerabilities in the TOE.


**AVA_VAN.5.3E** The evaluator _**shall perform**_ an independent, methodical vulnerability

analysis of the TOE using the guidance documentation, functional
specification, TOE design, security architecture description and
implementation representation to identify potential vulnerabilities in the TOE.


**AVA_VAN.5.4E** The evaluator _**shall conduct**_ penetration testing based on the identified

potential vulnerabilities to determine that the TOE is resistant to attacks
performed by an attacker possessing **High** attack potential.


Page 188 of 247 Version 3.1 April 2017


**Class ACO: Composition**

# **18 Class ACO: Composition**


486 The class ACO: Composition encompasses five families. These families
specify assurance requirements that are designed to provide confidence that a
composed TOE will operate securely when relying upon security
functionality provided by previously evaluated software, firmware or
hardware components.


487 Composition involves taking two or more IT entities successfully evaluated
against CC security assurance requirements packages (base components and
dependent components, see Annex B) and combining them for use, with no
further development of either IT entity. The development of additional IT
entities is not included (entities that have not previously been the subject of a
component evaluation). The composed TOE forms a new product that can be
installed and integrated into any specific environment instance that meets the
objectives for the environment.


488 This approach does not provide an alternative approach for the evaluation of
components. Composition under ACO provides a composed TOE integrator
a method, which can be used as an alternative to other assurance levels
specified in the CC, to gain confidence in a TOE that is the combination of
two or more successfully evaluated components without having to reevaluate the composite TSF. (The composed TOE integrator is referred to as
“developer” throughout the ACO class, with any references to the developer
of the base or dependent components clarified as such.)


489 Composed Assurance Packages, as defined in Chapters 9 and 7.3, is an
assurance scale for composed TOEs. This assurance scale is required in
addition to EALs because to combine components evaluated against EALs
and gain a resulting EAL assurance, all SARs in the EAL have to be applied
to the composed TOE. Although reuse can be made of the component TOE
evaluation results, there are often additional aspects of the components that
have to be considered in the composed TOE, as described in Annex B.3. Due
to the different parties involved in a composed TOE evaluation activity it is
generally not possible to gain all necessary evidence about these additional
aspects of the components to apply the appropriate EAL. Hence, CAPs have
been defined to address the issue of combining evaluated components and
gaining a meaningful result. This is discussed further in Annex B.


April 2017 Version 3.1 Page 189 of 247


**Class ACO: Composition**


**Figure 17 - Relationship between ACO families and interactions between**

**components**


490 In a composed TOE it is generally the case that one component relies on the
services provided by another component. The component requiring services
is termed the dependent component and the component providing the
services is termed the base component. This interaction and distinct is
discussed further in Annex B. It is assumed to be the case that the developer
of the dependent component is supporting the composed TOE evaluation in
some manner (as developer, sponsor, or just cooperating and providing the
necessary evaluation evidence from the dependent component evaluation)
The ACO components included in the CAP assurance packages should not
be used as augmentations for component TOE evaluations, as this would
provide no meaningful assurance for the component.


491 The families within the ACO class interact in a similar manner to the ADV,
ATE and AVA classes in a component TOE evaluation and hence leverage
from the specification of requirements from those classes where applicable.
There are however a few items specific to composed TOE evaluations. To
determine how the components interact and identify any deviations from the
evaluations of the components, the dependencies that the dependent
component has upon the underlying base component are identified
(ACO_REL). This reliance on the base component is specified in terms of
the interfaces through which the dependent component makes calls for
services in support of the dependent component SFRs. The interfaces, and at
higher levels the supporting behaviour, provided by the base component in
response to those service requests are analysed in ACO_DEV. The
ACO_DEV family is based on the ADV_TDS family, as at the simplest level
the TSF of each component can be viewed as a subsystem of the composed
TOE, with additional portions of each component seen as additional
subsystems. Therefore, the interfaces between the components are seen as
interactions between subsystems in a component TOE evaluation.


Page 190 of 247 Version 3.1 April 2017


**Class ACO: Composition**


492 It is possible that the interfaces and supporting behaviour descriptions
provided for ACO_DEV are incomplete. This is determined during the
conduct of ACO_COR. The ACO_COR family takes the outputs of
ACO_REL and ACO_DEV and determines whether the components are
being used in their evaluated configuration and identifies where any
specifications are incomplete, which are then identified as inputs into testing
(ACO_CTT) and vulnerability analysis (ACO_VUL) activities of the
composed TOE.


493 Testing of the composed TOE is performed to determine that the composed
TOE exhibits the expected behaviour as determined by the composed TOE
SFRs, and at higher levels demonstrates the compatibility of the interfaces
between the components of the composed TOE.


494 The vulnerability analysis of the composed TOE leverages from the outputs
of the vulnerability analysis of the component evaluations. The composed
TOE vulnerability analysis considers any residual vulnerabilities from the
component evaluations to determine that the residual vulnerabilities are not
applicable to the composed TOE. A search of publicly available information
relating to the components is also performed to identify any issues reported
in the components since the completion of the respective evaluations.


495 The interaction between the ACO families is depicted in Figure 18 below.
This shows by solid arrowed lines where the evidence and understanding
gained in one family feeds into the next activity and the dashed arrows
identify where an activity explicitly traces back to the composed TOE SFRs,
as described above.


**Figure 18 - Relationship between ACO families**


496 Further discussion of the definition and interactions within composed TOEs
is provided in Annex B.


497 Figure 19 shows the families within this class, and the hierarchy of
components within the families.


April 2017 Version 3.1 Page 191 of 247


**Class ACO: Composition**


**Figure 19 - ACO: Composition class decomposition**


Page 192 of 247 Version 3.1 April 2017


**Class ACO: Composition**

## **18.1 Composition rationale (ACO_COR)**


Objectives


498 This family addresses the requirement to demonstrate that the base
component can provide an appropriate level of assurance for use in
composition.


Component levelling


499 There is only a single component in this family.


**ACO_COR.1** **Composition rationale**


Dependencies: ACO_DEV.1 Functional Description
ALC_CMC.1 Labelling of the TOE
ACO_REL.1 Basic reliance information


Developer action elements:


**ACO_COR.1.1D** **The developer shall provide composition rationale for the base**

**component.**


Content and presentation elements:


**ACO_COR.1.1C** **The composition rationale shall demonstrate that a level of assurance at**

**least as high as that of the dependent component has been obtained for**
**the support functionality of the base component, when the base**
**component is configured as required to support the TSF of the**
**dependent component.**


Evaluator action elements:


**ACO_COR.1.1E** **The evaluator** _**shall confirm**_ **that the information meets all requirements**

**for content and presentation of evidence.**


April 2017 Version 3.1 Page 193 of 247


**Class ACO: Composition**

## **18.2 Development evidence (ACO_DEV)**


Objectives


500 This family sets out requirements for a specification of the base component
in increasing levels of detail. Such information is required to gain confidence
that the appropriate security functionality is provided to support the
requirements of the dependent component (as identified in the reliance
information).


Component levelling


501 The components are levelled on the basis of increasing amounts of detail
about the interfaces provided, and how they are implemented.


Application notes


502 The TSF of the base component is often defined without knowledge of the
dependencies of the possible applications with which it may by composed.
The TSF of this base component is defined to include all parts of the base
component that have to be relied upon for enforcement of the base
component SFRs. This will include all parts of the base component required
to implement the base component SFRs.


503 The functional specification of the base component will describe the TSFI in
terms of the interfaces the base component provides to allow an external
entity to invoke operations of the TSF. This includes interfaces to the human
user to permit interaction with the operation of the TSF invoking SFRs and
also interfaces allowing an external IT entity to make calls into the TSF.


504 The functional specification only provides a description of what the TSF
provides at its interface and the means by which that TSF functionality are
invoked. Therefore, the functional specification does not necessarily provide
a complete interface specification of all possible interfaces available between
an external entity and the base component. It does not include what the TSF
expects/requires from the operational environment. The description of what a
dependent component TSF relies upon of a base component is considered in
Reliance of dependent component (ACO_REL) and the development
information evidence provides a response to the interfaces specified.


505 The development information evidence includes a specification of the base
component. This may be the evidence used during evaluation of the base
component to satisfy the ADV requirements, or may be another form of
evidence produced by either the base component developer or the composed
TOE developer. This specification of the base component is used during
Development evidence (ACO_DEV) to gain confidence that the appropriate
security functionality is provided to support the requirements of the
dependent component. The level of detail required of this evidence increases
to reflect the level of required assurance in the composed TOE. This is
expected to broadly reflect the increasing confidence gained from the
application of the assurance packages to the components. The evaluator


Page 194 of 247 Version 3.1 April 2017


**Class ACO: Composition**


determines that this description of the base component is consistent with the
reliance information provided for the dependent component.


**ACO_DEV.1** **Functional Description**


Dependencies: ACO_REL.1 Basic reliance information


Objectives


506 A description of the interfaces in the base component, on which the
dependent component relies, is required. This is examined to determine
whether or not it is consistent with the description of interfaces on which the
dependent component relies, as provided in the reliance information.


Developer action elements:


**ACO_DEV.1.1D** **The developer shall provide development information for the base**

**component.**


Content and presentation elements:


**ACO_DEV.1.1C** **The development information shall describe the purpose of each**

**interface of the base component used in the composed TOE.**


**ACO_DEV.1.2C** **The development information shall show correspondence between the**

**interfaces, used in the composed TOE, of the base component and the**
**dependent component to support the TSF of the dependent component.**


Evaluator action elements:


**ACO_DEV.1.1E** **The evaluator** _**shall confirm**_ **that the information meets all requirements**

**for content and presentation of evidence.**


**ACO_DEV.1.2E** **The evaluator** _**shall determine**_ **that the interface description provided is**

**consistent with the reliance information provided for the dependent**
**component.**


**ACO_DEV.2** **Basic evidence of design**


Dependencies: ACO_REL.1 Basic reliance information


Objectives


507 A description of the interfaces in the base component, on which the
dependent component relies, is required. This is examined to determine
whether or not it is consistent with the description of interfaces on which the
dependent component relies, as provided in the reliance information.


508 In addition, the security behaviour of the base component that supports the
dependent component TSF is described.


April 2017 Version 3.1 Page 195 of 247


**Class ACO: Composition**


Developer action elements:


**ACO_DEV.2.1D** The developer shall provide development information for the base

component.


Content and presentation elements:


**ACO_DEV.2.1C** The development information shall describe the purpose **and** **method** of **use**

**of** each interface of the base component used in the composed TOE.


**ACO_DEV.2.2C** **The development information shall provide a high-level description of**

**the behaviour of the base component, which supports the enforcement of**
**the dependent component SFRs.**


**ACO_DEV.2.3C** The development information shall show correspondence between the

interfaces, used in the composed TOE, of the base component and the
dependent component to support the TSF of the dependent component.


Evaluator action elements:


**ACO_DEV.2.1E** The evaluator _**shall confirm**_ that the information meets all requirements for

content and presentation of evidence.


**ACO_DEV.2.2E** The evaluator _**shall determine**_ that the interface description provided is

consistent with the reliance information provided for the dependent
component.


**ACO_DEV.3** **Detailed evidence of design**


Dependencies: ACO_REL.2 Reliance information


Objectives


509 A description of the interfaces in the base component, on which the
dependent component relies, is required. This is examined to determine
whether or not it is consistent with the description of interfaces on which the
dependent component relies, as provided in the reliance information.


510 The interface description of the architecture of the base component is
provided to enable the evaluator to determine whether or not that interface
formed part of the TSF of the base component.


Developer action elements:


**ACO_DEV.3.1D** The developer shall provide development information for the base

component.


Content and presentation elements:


**ACO_DEV.3.1C** The development information shall describe the purpose and method of use

of each interface of the base component used in the composed TOE.


Page 196 of 247 Version 3.1 April 2017


**Class ACO: Composition**


**ACO_DEV.3.2C** **The development information shall identify the subsystems of the base**

**component that provide interfaces of the base component used in the**
**composed TOE.**


**ACO_DEV.3.3C** The development information shall provide a high-level description of the

behaviour of the base component **subsystems,** which **support** the
enforcement of the dependent component SFRs.


**ACO_DEV.3.4C** **The development information shall provide a mapping from the**

**interfaces to the subsystems of the base component.**


**ACO_DEV.3.5C** The development information shall show correspondence between the

interfaces, used in the composed TOE, of the base component and the
dependent component to support the TSF of the dependent component.


Evaluator action elements:


**ACO_DEV.3.1E** The evaluator _**shall confirm**_ that the information meets all requirements for

content and presentation of evidence.


**ACO_DEV.3.2E** The evaluator _**shall determine**_ that the interface description provided is

consistent with the reliance information provided for the dependent
component.


April 2017 Version 3.1 Page 197 of 247


**Class ACO: Composition**

## **18.3 Reliance of dependent component (ACO_REL)**


Objectives


511 The purpose of this family is to provide evidence that describes the reliance
that a dependent component has upon the base component. This information
is useful to persons responsible for integrating the component with other
evaluated IT components to form the composed TOE, and for providing
insight into the security properties of the resulting composition.


512 This provides a description of the interface between the dependent and base
components of the composed TOE that may not have been analysed during
evaluation of the individual components, as the interfaces were not TSFIs of
the individual component TOEs.


Component levelling


513 The components in this family are levelled according to the amount of detail
provided in the description of the reliance by the dependent component upon
the base component.


Application notes


514 The Reliance of dependent component (ACO_REL) family considers the
interactions between the components where the dependent component relies
upon a service from the base component to support the operation of security
functionality of the dependent component. The interfaces into these services
of the base component may not have been considered during evaluation of
the base component because the service in the base component was not
considered security-relevant in the component evaluation, either because of
the inherent purpose of the service (e.g., adjust type font) or because
associated CC SFRs are not being claimed in the base component's ST (e.g.
the login interface when no FIA: Identification and authentication SFRs are
claimed). These interfaces into the base component are often viewed as
functional interfaces in the evaluation of the base component, and are in
addition to the security interfaces (TSFI) considered in the functional
specification.


515 In summary, the TSFIs described in the functional specification only include
the calls made into a TSF by external entities and responses to those calls.
Calls made by a TSF, which were not explicitly considered during evaluation
of the components, are described by the reliance information provided to
satisfy Reliance of dependent component (ACO_REL).


Page 198 of 247 Version 3.1 April 2017


**Class ACO: Composition**


**ACO_REL.1** **Basic reliance information**


Dependencies: No dependencies.


Developer action elements:


**ACO_REL.1.1D** **The developer shall provide reliance information of the dependent**

**component.**


Content and presentation elements:


**ACO_REL.1.1C** **The reliance information shall describe the functionality of the base**

**component hardware, firmware and/or software that is relied upon by**
**the dependent component TSF.**


**ACO_REL.1.2C** **The reliance information shall describe all interactions through which**

**the dependent component TSF requests services from the base**
**component.**


**ACO_REL.1.3C** **The reliance information shall describe how the dependent TSF protects**

**itself from interference and tampering by the base component.**


Evaluator action elements:


**ACO_REL.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


**ACO_REL.2** **Reliance information**


Dependencies: No dependencies.


Developer action elements:


**ACO_REL.2.1D** The developer shall provide reliance information of the dependent

component.


Content and presentation elements:


**ACO_REL.2.1C** The reliance information shall describe the functionality of the base

component hardware, firmware and/or software that is relied upon by the
dependent component TSF.


**ACO_REL.2.2C** The reliance information shall describe all interactions through which the

dependent component TSF requests services from the base component.


**ACO_REL.2.3C** **The reliance information shall describe each interaction in terms of the**

**interface used and the return values from those interfaces.**


**ACO_REL.2.4C** The reliance information shall describe how the dependent TSF protects

itself from interference and tampering by the base component.


April 2017 Version 3.1 Page 199 of 247


**Class ACO: Composition**


Evaluator action elements:


**ACO_REL.2.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


Page 200 of 247 Version 3.1 April 2017


**Class ACO: Composition**

## **18.4 Composed TOE testing (ACO_CTT)**


Objectives


516 This family requires that testing of composed TOE and testing of the base
component, as used in the composed TOE, is performed.


Component levelling


517 The components in this family are levelled on the basis of increasing rigour
of interface testing and increasing rigour of the analysis of the sufficiency of
the tests to demonstrate that the composed TSF operates in accordance with
the reliance information and the composed TOE SFRs.


Application notes


518 There are two distinct aspects of testing associated with this family:


a) testing of the interfaces between the base component and the
dependent component, which the dependent component rely upon for
enforcement of security functionality, to demonstrate their
compatibility;


b) testing of the composed TOE to demonstrate that the TOE behaves in
accordance with the SFRs for the composed TOE.


519 If the test configurations used during evaluation of the dependent component
included use of the base component as a “platform” and the test analysis
sufficiently demonstrates that the TSF behaves in accordance with the SFRs,
the developer need perform no further testing of the composed TOE
functionality. However, if the base component was not used in the testing of
the dependent component, or the configuration of either component varied,
then the developer is to perform testing of the composed TOE. This may take
the form of repeating the dependent component developer testing of the
dependent component, provided this adequately demonstrates the composed
TOE TSF behaves in accordance with the SFRs.


520 The developer is to provide evidence of testing the base component
interfaces used in the composition. The operation of base component TSFIs
would have been tested as part of the ATE: Tests activities during evaluation
of the base component. Therefore, provided the appropriate interfaces were
included within the test sample of the base component evaluation and it was
determined in Composition rationale (ACO_COR) that the base component
is operating in accordance with the base component evaluated configuration,
with all security functionality required by the dependent component included
in the TSF, the evaluator action **ACO_CTT.1.1E** may be met through reuse of
the base component ATE: Tests verdicts.


521 If this is not the case, the base component interfaces used relevant to the
composition that are affected by any variations to the evaluated configuration
and any additional security functionally will be tested to ensure they


April 2017 Version 3.1 Page 201 of 247


**Class ACO: Composition**


demonstrate the expected behaviour. The expected behaviour to be tested is
that described in the reliance information (Reliance of dependent component
(ACO_REL) evidence).


**ACO_CTT.1** **Interface testing**


Dependencies: ACO_REL.1 Basic reliance information
ACO_DEV.1 Functional Description


Objectives


522 The objective of this component is to ensure that each interface of the base
component, on which the dependent component relies, is tested.


Developer action elements:


**ACO_CTT.1.1D** **The developer shall provide composed TOE test documentation.**


**ACO_CTT.1.2D** **The** **developer** **shall** **provide** **base** **component** **interface** **test**
**documentation.**


**ACO_CTT.1.3D** **The developer shall provide the composed TOE for testing.**


**ACO_CTT.1.4D** **The developer shall provide an equivalent set of resources to those that**

**were used in the base component developer's functional testing of the**
**base component.**


Content and presentation elements:


**ACO_CTT.1.1C** **The composed TOE and base component interface test documentation**

**shall consist of test plans, expected test results and actual test results.**


**ACO_CTT.1.2C** **The test documentation from the developer execution of the composed**

**TOE tests shall demonstrate that the TSF behaves as specified.**


**ACO_CTT.1.3C** **The test documentation from the developer execution of the base**

**component interface tests shall demonstrate that the base component**
**interface relied upon by the dependent component behaves as specified.**


**ACO_CTT.1.4C** **The base component shall be suitable for testing.**


Evaluator action elements:


**ACO_CTT.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


**ACO_CTT.1.2E** **The evaluator** _**shall execute**_ **a sample of test in the test documentation to**

**verify the developer test results.**


**ACO_CTT.1.3E** **The evaluator** _**shall test**_ **a subset of the TSF interfaces of the composed**

**TOE to confirm that the composed TSF operates as specified.**


Page 202 of 247 Version 3.1 April 2017


**Class ACO: Composition**


**ACO_CTT.2** **Rigorous interface testing**


Dependencies: ACO_REL.2 Reliance information
ACO_DEV.2 Basic evidence of design


Objectives


523 The objective of this component is to ensure that each interface of the base
component, on which the dependent component relies, is tested.


Developer action elements:


**ACO_CTT.2.1D** The developer shall provide composed TOE test documentation.


**ACO_CTT.2.2D** The developer shall provide base component interface test documentation.


**ACO_CTT.2.3D** The developer shall provide the composed TOE for testing.


**ACO_CTT.2.4D** The developer shall provide an equivalent set of resources to those that were

used in the base component developer's functional testing of the base
component.


Content and presentation elements:


**ACO_CTT.2.1C** The composed TOE and base component interface test documentation shall

consist of test plans, expected test results and actual test results.


**ACO_CTT.2.2C** The test documentation from the developer execution of the composed TOE

tests shall demonstrate that the TSF behaves as specified **and** **is** **complete.**


**ACO_CTT.2.3C** The test documentation from the developer execution of the base component

interface tests shall demonstrate that the base component interface relied
upon by the dependent component behaves as specified **and** **is** **complete.**


**ACO_CTT.2.4C** The base component shall be suitable for testing.


Evaluator action elements:


**ACO_CTT.2.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


**ACO_CTT.2.2E** The evaluator _**shall execute**_ a sample of test in the test documentation to

verify the developer test results.


**ACO_CTT.2.3E** The evaluator _**shall test**_ a subset of the TSF interfaces of the composed TOE

to confirm that the composed TSF operates as specified.


April 2017 Version 3.1 Page 203 of 247


**Class ACO: Composition**

## **18.5 Composition vulnerability analysis (ACO_VUL)**


Objectives


524 This family calls for an analysis of vulnerability information available in the
public domain and of vulnerabilities that may be introduced as a result of the
composition.


Component levelling


525 The components in this family are levelled on the basis of increasing scrutiny
of vulnerability information from the public domain and independent
vulnerability analysis.


Application notes


526 The developer will provide details of any residual vulnerabilities reported
during evaluation of the components. These may be gained from the
component developers or evaluation reports for the components. These will
be used as inputs into the evaluator's vulnerability analysis of the composed
TOE in the operational environment.


527 The operational environment of the composed TOE is examined to ensure
that the assumptions and objectives for the component operational
environment (specified in each component ST) are satisfied in the composed
TOE. An initial analysis of the consistency of assumptions and objectives
between the components and the composed TOE STs will have been
performed during the conduct of the ASE activities for the composed TOE.
However, this analysis is revisited with the knowledge acquired during the
ACO_REL, ACO_DEV and the ACO_COR activities to ensure that, for
example, assumptions of the dependent component that were addressed by
the environment in the dependent component ST are not reintroduced as a
result of composition (i.e. that the base component adequately addresses the
assumptions of the dependent component ST in the composed TOE).


528 A search by the evaluator for issues in each component will identify potential
vulnerabilities reported in the public domain since completion of the
evaluation of the components. Any potential vulnerabilities will then be
subject to testing.


529 If the base component used in the composed TOE has been the subject of
assurance continuity activities since certification, the evaluator will consider
during the composed TOE vulnerability analysis activities the changes made
in base component.


**ACO_VUL.1** **Composition vulnerability review**


Dependencies: ACO_DEV.1 Functional Description


Developer action elements:


**ACO_VUL.1.1D** **The developer shall provide the composed TOE for testing.**


Page 204 of 247 Version 3.1 April 2017


**Class ACO: Composition**


Content and presentation elements:


**ACO_VUL.1.1C** **The composed TOE shall be suitable for testing.**


Evaluator action elements:


**ACO_VUL.1.1E** **The evaluator** _**shall confirm**_ **that the information provided meets all**

**requirements for content and presentation of evidence.**


**ACO_VUL.1.2E** **The evaluator** _**shall perform**_ **an analysis to determine that any residual**

**vulnerabilities identified for the base and dependent components are not**
**exploitable in the composed TOE in its operational environment.**


**ACO_VUL.1.3E** **The evaluator** _**shall perform**_ **a search of public domain sources to**

**identify possible vulnerabilities arising from use of the base and**
**dependent components in the composed TOE operational environment.**


**ACO_VUL.1.4E** **The evaluator** _**shall conduct**_ **penetration testing, based on the identified**

**vulnerabilities, to demonstrate that the composed TOE is resistant to**
**attacks by an attacker with basic attack potential.**


**ACO_VUL.2** **Composition vulnerability analysis**


Dependencies: ACO_DEV.2 Basic evidence of design


Developer action elements:


**ACO_VUL.2.1D** The developer shall provide the composed TOE for testing.


Content and presentation elements:


**ACO_VUL.2.1C** The composed TOE shall be suitable for testing.


Evaluator action elements:


**ACO_VUL.2.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


**ACO_VUL.2.2E** The evaluator _**shall perform**_ an analysis to determine that any residual

vulnerabilities identified for the base and dependent components are not
exploitable in the composed TOE in its operational environment.


**ACO_VUL.2.3E** The evaluator _**shall perform**_ a search of public domain sources to identify

possible vulnerabilities arising from use of the base and dependent
components in the composed TOE operational environment.


**ACO_VUL.2.4E** **The evaluator** _**shall perform**_ **an independent vulnerability analysis of the**

**composed TOE, using the guidance documentation, reliance information**
**and composition rationale to identify potential vulnerabilities in the**
**composed TOE.**


April 2017 Version 3.1 Page 205 of 247


**Class ACO: Composition**


**ACO_VUL.2.5E** The evaluator _**shall conduct**_ penetration testing, based on the identified

vulnerabilities, to demonstrate that the composed TOE is resistant to attacks
by an attacker with basic attack potential.


**ACO_VUL.3** **Enhanced-Basic Composition vulnerability analysis**


Dependencies: ACO_DEV.3 Detailed evidence of design


Developer action elements:


**ACO_VUL.3.1D** The developer shall provide the composed TOE for testing.


Content and presentation elements:


**ACO_VUL.3.1C** The composed TOE shall be suitable for testing.


Evaluator action elements:


**ACO_VUL.3.1E** The evaluator _**shall confirm**_ that the information provided meets all

requirements for content and presentation of evidence.


**ACO_VUL.3.2E** The evaluator _**shall perform**_ an analysis to determine that any residual

vulnerabilities identified for the base and dependent components are not
exploitable in the composed TOE in its operational environment.


**ACO_VUL.3.3E** The evaluator _**shall perform**_ a search of public domain sources to identify

possible vulnerabilities arising from use of the base and dependent
components in the composed TOE operational environment.


**ACO_VUL.3.4E** The evaluator _**shall perform**_ an independent vulnerability analysis of the

composed TOE, using the guidance documentation, reliance information and
composition rationale to identify potential vulnerabilities in the composed
TOE.


**ACO_VUL.3.5E** The evaluator _**shall conduct**_ penetration testing, based on the identified

vulnerabilities, to demonstrate that the composed TOE is resistant to attacks
by an attacker with **Enhanced-Basic** attack potential.


Page 206 of 247 Version 3.1 April 2017


**Development (ADV)**

# **A Development (ADV)** **(informative)**


530 This annex contains ancillary material to further explain and provide
additional examples for the topics brought up in families of the ADV:
Development class.

## **A.1 ADV_ARC: Supplementary material on security** **architectures**


531 A security architecture is a set of properties that the TSF exhibits; these
properties include self-protection, domain separation, and non-bypassability.
Having these properties provides a basis of confidence that the TSF is
providing its security services. This annex provides additional material on
these properties, as well as discussion on contents of a security architecture
description.


532 The remainder of this section first explains these properties, then discusses
the kinds of information that are needed to describe how the TSF exhibits
those properties.


**A.1.1** **Security architecture properties**


533 _Self-protection_ refers to the ability of the TSF to protect itself from
manipulation from external entities that may result in changes to the TSF.
Without these properties, the TSF might be disabled from performing its
security services.


534 It is oftentimes the case that a TOE uses services or resources supplied by
other IT entities in order to perform its functions (e.g. an application that
relies upon its underlying operating system). In these cases, the TSF does not
protect itself entirely on its own, because it depends on the other IT entities
to protect the services it uses.


535 _Domain separation_ is a property whereby the TSF creates separate _security_
_domains_ for each untrusted active entity to operate on its resources, and then
keeps those domains separated from one another so that no entity can run in
the domain of any other. For example, an operating system TOE supplies a
domain (address space, per-process environment variables) for each process
associated with untrusted entities.


536 For some TOEs such domains do not exist because all of the actions of the
untrusted entities are brokered by the TSF. A packet-filter firewall is an
example of such a TOE, where there are no untrusted entity domains; there
are only data structures maintained by the TSF. The existence of domains,
then, is dependant upon 1) the type of TOE and 2) the SFRs levied on the
TOE. In the cases where the TOE does provide domains for untrusted entities,
this family requires that those domains are isolated from one another such


April 2017 Version 3.1 Page 207 of 247


**Development (ADV)**


that untrusted entities in one domain are prevented from tampering (affecting
without brokering by the TSF) from another untrusted entity's domain.


537 _Non-bypassability_ is a property that the security functionality of the TSF (as
specified by the SFRs) is always invoked and cannot be circumvented when
appropriate for that specific mechanism. For example, if access control to
files is specified as a capability of the TSF via an SFR, there must be no
interfaces through which files can be accessed without invoking the TSF's
access control mechanism (an interface through which a raw disk access
takes place might be an example of such an interface).


538 As is the case with self-protection, the very nature of some TOEs might
depend upon their environments to play a role in non-bypassability of the
TSF. For example, a security application TOE requires that it be invoked by
the underlying operating system. Similarly, a firewall depends upon the fact
that there are no direct connections between the internal and external
networks and that all traffic between them must go through the firewall.


**A.1.2** **Security architecture descriptions**


539 The security architecture description explains how the properties described
above are exhibited by the TSF. It describes how domains are defined and
how the TSF keeps them separate. It describes what prevents untrusted
processes from getting to the TSF and modifying it. It describes what ensures
that all resources under the TSF's control are adequately protected and that
all actions related to the SFRs are mediated by the TSF. It explains any role
the environment plays in any of these (e.g. presuming it gets correctly
invoked by its underlying environment, how are its security functions
invoked?).


540 The security architecture description presents the TSF's properties of selfprotection, domain separation, and non-bypassability in terms of the
decomposition descriptions. The level of this description is commensurate
with the TSF description required by the ADV_FSP, ADV_TDS and
ADV_IMP requirements that are being claimed. For example, if ADV_FSP
is the only TSF description available, it would be difficult to provide any
meaningful security architecture description because none of the details of
any internal workings of the TSF would be available.


541 However, if the TOE design were also available, even at the most basic level
(ADV_TDS.1), there would be some information available concerning the
subsystems that make up the TSF, and there would be a description of how
they work to implement self-protection, domain separation, and nonbypassability. For example, perhaps all user interaction with the TOE is
constrained through a process that acts on that user's behalf, adopting all of
the user's security attributes; the security architecture description would
describe how such a process comes into being, how the process's behaviour
is constrained by the TSF (so it cannot corrupt the TSF), how all actions of
that process are mediated by the TSF (thereby explaining why the TSF
cannot be bypassed), etc.


Page 208 of 247 Version 3.1 April 2017


**Development (ADV)**


542 If the available TOE design is more detailed (e.g. at the modular level), or
the implementation representation is also available, then the security
architecture description would be correspondingly more detailed, explaining
how the user's process communicate with the TSF processes, how different
requests are processed by the TSF, what parameters are passed, what
programmatic protections (buffer overflow prevention, parameter bounds
checking, time of check/time of use checking, etc.) are in place. Similarly, a
TOE whose ST claimed the ADV_IMP component would go into
implementation-specific detail.


543 The explanations provided in the security architecture description are
expected to be of sufficient detail that one would be able to test their
accuracy. That is, simple assertions (e.g. "The TSF keeps domains separate”)
provide no useful information to convince the reader that the TSF does
indeed create and separate domains.


**A.1.2.1** Domain Separation


544 In cases where the TOE exhibits domain separation entirely on its own, there
would be a straightforward description of how this is attained. The security
architecture description would explain the different kinds of domains that are
defined by the TSF, how they are defined (i.e. what resources are allocated to
each domain), how no resources are left unprotected, and how the domains
are kept separated so that active entities in one domain cannot tamper with
resources in another domain.


545 For cases where the TOE depends upon other IT entities to play a role in
domain separation, that sharing of roles must be made clear. For example, a
TOE that is solely application software relies upon the underlying operating
system to correctly instantiate the domains that the TOE defines; if the TOE
defines separate processing space, memory space, etc, for each domain, it
depends upon the underlying operating system to operate correctly and
benignly (e.g. allow the process to execute only in the execution space that is
requested by the TOE software).


546 For example, mechanisms that implement domain separation (e.g., memory
management, protected processing modes provided by the hardware, etc.)
would be identified and described. Or, the TSF might implement software
protection constructs or coding conventions that contribute to implementing
separation of software domains, perhaps by delineating user address space
from system address space.


547 The vulnerability analysis and testing (see AVA_VAN) activities will likely
include attempts to defeat the described TSF domain separation through the
use of monitoring or direct attack the TSF.


**A.1.2.2** TSF Self-protection


548 In cases where the TOE exhibits self-protection entirely on its own, there
would be a straightforward description of how this self-protection is attained.


April 2017 Version 3.1 Page 209 of 247


**Development (ADV)**


Mechanisms that provide domain separation to define a TSF domain that is
protected from other (user) domains would be identified and described.


549 For cases where the TOE depends upon other IT entities to play a role in
protecting itself, that sharing of roles must be made clear. For example, a
TOE that is solely application software relies upon the underlying operating
system to operate correctly and benignly; the application cannot protect itself
against a malicious operating system that subverts it (for example, by
overwriting its executable code or TSF data).


550 The security architecture description also covers how user input is handled
by the TSF in such a way that the TSF does not subject itself to being
corrupted by that user input. For example, the TSF might implement the
notion of privilege and protect itself by using privileged-mode routines to
handle user data. The TSF might make use of processor-based separation
mechanisms (e.g. privilege levels or rings) to separate TSF code and data
from user code and data. The TSF might implement software protection
constructs or coding conventions that contribute to implementing separation
of software, perhaps by delineating user address space from system address
space.


551 For TOEs that start up in a low-function mode (for example, a single-user
mode accessible only to installers or administrators) and then transition to the
evaluated secure configuration (a mode whereby untrusted users are able to
login and use the services and resources of the TOE), the security
architecture description also includes an explanation of how the TSF is
protected against this initialisation code that does not run in the evaluated
configuration. For such TOEs, the security architecture description would
explain what prevents those services that should be available only during
initialisation (e.g. direct access to resources) from being accessible in the
evaluated configuration. It would also explain what prevents initialisation
code from running while the TOE is in the evaluated configuration.


552 There must also be an explanation of how the trusted initialisation code will
maintain the integrity of the TSF (and of its initialisation process) such that
the initialisation process is able to detect any modification that would result
in the TSF being spoofed into believe it was in an initial secure state.


553 The vulnerability analysis and testing (see AVA_VAN) activities will likely
include attempts to defeat the described TSF self protection through the use
of tampering, direct attack, or monitoring of the TSF.


**A.1.2.3** TSF Non-Bypassability


554 The property of non-bypassability is concerned with interfaces that permit
the bypass of the enforcement mechanisms. In most cases this is a
consequence of the implementation, where if a programmer is writing an
interface that accesses or manipulates an object, it is that programmer's
responsibility to use interfaces that are part of the SFR enforcement
mechanism for the object and not to try to circumvent those interfaces. For


Page 210 of 247 Version 3.1 April 2017


**Development (ADV)**


the description pertaining to non-bypassability, then, there are two broad
areas that have to be covered.


555 The first consists of those interfaces to the SFR-enforcement. The property
for these interfaces is that they contain no operations or modes that allow
them to be used to bypass the TSF. It is likely that the evidence for
ADV_FSP and ADV_TDS can be used in large part to make this
determination. Because non-bypassability is the concern, if only certain
operations available through these TSFIs are documented (because they are
SFR-enforcing) and others are not, the developer should consider whether
additional information (to that presented in ADV_FSP and ADV_TDS) is
necessary to make a determination that the SFR-supporting and SFR-noninterfering operations of the TSFI do not afford an untrusted entity the ability
to bypass the policy being enforced. If such information is necessary, it is
included in the security architecture description.


556 The second area of non-bypassability is concerned with those interfaces
whose interactions are not associated with SFR-enforcement. Depending on
the ADV_FSP and ADV_TDS components claimed, some information about
these interfaces may or may not exist in the functional specification and TOE
design documentation. The information presented for such interfaces (or
groups of interfaces) should be sufficient so that a reader can make a
determination (at the level of detail commensurate with the rest of the
evidence supplied in the ADV: Development class) that the enforcement
mechanisms cannot be bypassed.


557 The property that the security functionality cannot be bypassed applies to all
security functionality equally. That is, the design description should cover
objects that are protected under the SFRs (e.g. FDP_* components) and
functionality (e.g., audit) that is provided by the TSF. The description should
also identify the interfaces that are associated with security functionality; this
might make use of the information in the functional specification. This
description should also describe any design constructs, such as object
managers, and their method of use. For instance, if routines are to use a
standard macro to produce an audit record, this convention is a part of the
design that contributes to the non-bypassability of the audit mechanism. It is
important to note that _non-bypassability_ in this context is not an attempt to
answer the question “could a part of the TSF implementation, if malicious,
bypass the security functionality”, but rather to document how the
implementation does not bypass the security functionality.


558 The vulnerability analysis and testing (see AVA_VAN) activities will likely
include attempts to defeat the described non-bypassability by circumventing
the TSF.

## **A.2 ADV_FSP: Supplementary material on TSFIs**


559 The purpose in specifying the TSFIs is to provide the necessary information
to conduct testing; without knowing the possible means interact with the TSF,
one cannot adequately test the behaviour of the TSF.


April 2017 Version 3.1 Page 211 of 247


**Development (ADV)**


560 There are two parts to specifying the TSFIs: identifying them and describing
them. Because of the diversity of possible TOEs, and of different TSFs
therein, there is no standard set of interfaces that constitute “TSFIs”. This
annex provides guidance on the factors that determine which interfaces are
TSFIs.


**A.2.1** **Determining the TSFI**


561 In order to identify the interfaces to the TSF, the parts of the TOE that make
up the TSF must first be identified. This identification is actually a part of
the TOE design (ADV_TDS) analysis, but is also performed implicitly
(through identification and description of the TSFI) by the developer in cases
where TOE design (ADV_TDS) is not included in the assurance package. In
this analysis, a portion of the TOE must be considered to be in the TSF if it
contributes to the satisfaction of an SFR in the ST (in whole or in part). This
includes, for example, everything in the TOE that contributes to TSF runtime initialisation, such as software that runs prior to the TSF being able to
protect itself because enforcement of the SFRs has not yet begun (e.g., while
booting up). Also included in the TSF are all parts of the TOE that contribute
to the architectural principles of TSF self-protection, domain separation, and
non-bypassability (see Security Architecture (ADV_ARC)).


562 Once the TSF has been defined, the TSFI are identified. The TSFI consists of
all means by which external entities (or subjects in the TOE but outside of
the TSF) supply data to the TSF, receive data from the TSF and invoke
services from the TSF. These service invocations and responses are the
means of crossing the TSF boundary. While many of these are readily
apparent, others might not be as obvious. The question that should be asked
when determining the TSFIs is: “How can a potential attacker interact with
the TSF in an attempt to subvert the SFRs?” The following discussions
illustrate the application of the TSFI definition in different contexts.


**A.2.1.1** Electrical interfaces


563 In TOEs such as smart cards, where the adversary has not only logical access
to the TOE, but also complete physical access to the TOE, the TSF boundary
is the physical boundary. Therefore, the exposed electrical interfaces are
considered TSFI because their manipulation could affect the behaviour of the
TSF. As such, all these interfaces (electrical contacts) need to be described:
various voltages that might be applied, etc.


**A.2.1.2** Network protocol stack


564 The TSFIs of a TOE that performs protocol processing would be those
protocol layers to which a potential attacker has direct access. This need not
be the entire protocol stack, but it might be.


565 For example, if the TOE were some sort of a network appliance that allowed
potential attackers to affect every level of the protocol stack (i.e. to send
arbitrary signals, arbitrary voltages, arbitrary packets, arbitrary datagrams,
etc.), then the TSF boundary exists at each layer of the stack. Therefore, the


Page 212 of 247 Version 3.1 April 2017


**Development (ADV)**


functional specification would have to address every protocol at every layer
of the stack.


566 If, however, the TOE were a firewall that protects an internal network from
the Internet, a potential attacker would have no means of directly
manipulating the voltages that enter the TOE; any extreme voltages would
simply not be passed though the Internet. That is, the attacker would have
access only to those protocols at the Internet layer or above. The TSF
boundary exists at each layer of the stack. Therefore, the functional
specification would have to address only those protocols at or above the
Internet layer: it would describe each of the different communication layers
at which the firewall is exposed in terms of what constitutes well-formed
input for what might appear on the line, and the result of both well-formed
and malformed inputs. For example, the description of the Internet protocol
layer would describe what constitutes a well-formed IP packet and what
happens when both correctly-formed and malformed packets are received.
Likewise, the description of the TCP layer would describe a successful TCP
connection and what happens both when successful connections are
established and when connections cannot be established or are inadvertently
dropped. Presuming the firewall's purpose is to filter application-level
commands (like FTP or telnet), the description of the application layer would
describe the application-level commands that are recognised and filtered by
the firewall, as well as the results of encountering unknown commands.


567 The descriptions of these layers would likely reference published
communication standards (telnet, FTP, TCP, etc.) that are used, noting which
user-defined options are chosen.


**A.2.1.3** Wrappers


**Figure 20 - Wrappers**


April 2017 Version 3.1 Page 213 of 247


**Development (ADV)**


568 “Wrappers” translate complex series of interactions into simplified common
services, such as when Operating Systems create APIs for use by
applications (as shown in Figure 20). Whether the TSFIs would be the
system calls or the APIs depends upon what is available to the application: if
the application can use the system calls directly, then the system calls are the
TSFIs. If, however, there were something that prohibits their direct use and
requires all communication through the APIs, then the APIs would be the
TSFIs.


569 A Graphical User interface is similar: it translates between machineunderstandable commands and user-friendly graphics. Similarly, the TSFIs
would be the commands if users have access to them, or the graphics (pulldown menus, check-boxes, text fields) if the users are constrained to using
them.


570 It is worth noting that, in both of these examples, if the user is prohibited
from using the more primitive interfaces (i.e. the system calls or the
commands), the description of this restriction and of its enforcement would
be included in the Security Architecture Description (see A.1). Also, the
wrapper would be part of the TSF.


**A.2.1.4** Inaccessible interfaces


571 For a given TOE, not all of the interfaces may be _accessible_ . That is, the
security objectives for the operational environment (in the Security Target)
may prevent access to these interfaces or limit access in such a way that they
are practically inaccessible. Such interfaces would not be considered TSFIs.
Some examples:


a) If the security objectives for the operational environment for the
stand-alone firewall state that “the firewall will be operational in a
server room environment to which only trusted and trained personnel
will have access, and which will be equipped with an interruptible
power supply (against power failure)”, physical and power interfaces
will not be accessible, since trusted and trained personnel will not
attempt to dismantle the firewall and/or disable its power supply.


b) If the security objectives for the operational environment for the
software firewall (application) state that “the OS and the hardware
will provide a security domain for the application free from
tampering by other programs”, the interfaces through which the
firewall can be accessed by other applications on the OS (e.g.
deleting or modifying the firewall executable, direct reading or
writing to the memory space of the firewall) will not be accessible,
since the OS/hardware part of the operational environment makes this
interface inaccessible.


c) If the security objectives for the operational environment for the
software firewall additionally state that the OS and hardware will
faithfully execute the commands of the TOE, and will not tamper
with the TOE in any manner, interfaces through which the firewall


Page 214 of 247 Version 3.1 April 2017


**Development (ADV)**


obtains primitive functionality from the OS and hardware (executing
machine code instructions, OS APIs, such as creating, reading,
writing or deleting files, graphical APIs etc.) will not be accessible,
since the OS/hardware are the only entities that can access that
interface, and they are completely trusted.


For all of these examples, these inaccessible interfaces would not be TSFIs.


**A.2.2** **Example: A complex DBMS**


572 Figure 21 illustrates a complex TOE: a database management system that
relies on hardware and software that is outside the TOE boundary (referred
to as the _IT environment_ in the rest of this discussion). To simplify this
example, the TOE is identical to the TSF. The shaded boxes represent the
TSF, while the unshaded boxes represent IT entities in the environment. The
TSF comprises the database engine and management GUIs (represented by
the box labelled _DB_ ) and a kernel module that runs as part of the OS that
performs some security function (represented by the box labelled _PLG_ ). The
TSF kernel module has entry points defined by the OS specification that the
OS will call to invoke some function (this could be a device driver, or an
authentication module, etc.). The key is that this pluggable kernel module is
providing security services specified by functional requirements in the ST.


**Figure 21 - Interfaces in a DBMS system**


573 The IT environment consists of the operating system itself (represented by
the box labelled _OS_ ), as well as an external server (labelled _SRV_ ). This
external server, like the OS, provides a service that the TSF depends on, and
thus needs to be in the IT environment. Interfaces in the figure are labelled
_Ax_ for TSFI, and _Bx_ for other interfaces that would be documented in ACO:
Composition. Each of these groups of interfaces is now discussed.


574 Interface group A1 represents the most obvious set of TSFI. These are
interfaces used by users to directly access the database and its security
functionality and resources.


April 2017 Version 3.1 Page 215 of 247


**Development (ADV)**


575 Interface group A2 represent the TSFI that the OS invokes to obtain the
functionality provided by the pluggable module. These are contrasted with
interface group B3, which represent calls that the pluggable module makes to
obtain services from the IT environment.


576 Interface group A3 represent TSFI that pass through the IT environment. In
this case, the DBMS communicates over the network using a proprietary
application-level protocol. While the IT environment is responsible for
providing various supporting protocols (e.g., Ethernet, IP, TCP), the
application layer protocol that is used to obtain services from the DBMS is a
TSFI and must be documented as such. The dotted line indicates return
values/services from the TSF over the network connection.


577 The interfaces labelled _Bx_ represent interfaces to functionality in the IT
Environment. These interfaces are not TSFI and need only be discussed and
analysed when the TOE is being used in a composite evaluation as part of the
activities associated with the ACO class.


**A.2.3** **Example Functional Specification**


578 The Example firewall is used between an internal network and an external
network. It verifies the source address of data received (to ensure that
external data is not attempting to masquerade as originating from the internal
data); if it detects any such attempts, it saves the offending attempt to the
audit log. The administrator connects to the firewall by establishing a telnet
connection to the firewall from the internal network. Administrator actions
consist of authenticating, changing passwords, reviewing the audit log, and
setting or changing the addresses of the internal and external networks.


579 The Example firewall presents the following interfaces to the internal
network:


a) IP datagrams


b) Administrator Commands


and the following interfaces to the external network:


a) IP datagrams


580 **Interfaces Descriptions: IP Datagrams**


581 The datagrams are in the format specified by RFC 791.


 Purpose - to transmit blocks of data (“datagrams”) from source hosts
to destination hosts identified by fixed length addresses; also
provides for fragmentation and reassembly of long datagrams, if
necessary, for transmission through small-packet networks.


 Method of Use - they arrive from the lower-level (e.g. data link)
protocol.


Page 216 of 247 Version 3.1 April 2017


**Development (ADV)**


 Parameters - the following fields of the IP datagram header: source
address, destination address, don't-fragment flag.


 Parameter description - [As defined by RFC 791, section 3.1
(“Internet Header Format”)]


 Actions - Transmits datagrams that are not masquerading; fragments
large datagrams if necessary; reassembles fragments into datagrams.


 Error messages - (none). No reliability guaranteed (reliability to be
provided by upper-level protocols) Undeliverable datagrams (e.g.
must be fragmented for transmission, but don't-fragment flag is set)
dropped.


582 **Interfaces Descriptions: Administrator Commands**


583 The administrator commands provide a means for the administrator to
interact with the firewall. These commands and responses ride atop a telnet
(RFC 854) connection established from any host on the internal network.
Available commands are:


 **Passwd**


 Purpose - sets administrator password


 Method of Use - **Passwd** < _password_         

 Parameters - password


 Parameter description - value of new password


 Actions - changes password to new value supplied. There are
no restrictions.


 Error messages - none.


 **Readaudit**


 Purpose - presents the audit log to the administrator


 Method of Use - **Readaudit**


 Parameters - none


 Parameter description - none


 Actions - provides the text of the audit log


 Error messages - none.


 **Setintaddr**


April 2017 Version 3.1 Page 217 of 247


**Development (ADV)**


 Purpose - sets the address of the internal address.


 Method of Use - **Setintaddr** < _address_         

 Parameters - address


 Parameter description - first three fields of an IP address (as
defined in RFC 791). For example: 123.123.123.


 Actions - changes the internal value of the variable defining
the internal network, the value of which is used to judge
attempted masquerades.


 Error messages - “address in use”: indicates the identified
internal network is the same as the external network.


 **Setextaddr**


 Purpose - sets the address of the external address


 Method of Use - **Setextaddr** < _address_         

 Parameters - address


 Parameter description - first three fields of an IP address (as
defined in RFC 791). For example: 123.123.123.


 Actions - changes the internal value of the variable defining
the external network.


 Error messages - “address in use”: indicates the identified
external network is the same as the internal network.

## **A.3 ADV_INT: Supplementary material on TSF internals**


584 The wide variety of TOEs makes it impossible to codify anything more
specific than “well-structured” or “minimum complexity”. Judgements on
structure and complexity are expected to be derived from the specific
technologies used in the TOE. For example, software is likely to be
considered well-structured if it exhibits the characteristics cited in the
software engineering disciplines.


585 This annex provides supplementary material on assessing the structure and
complexity of procedure-based software portions of the TSF. This material is
based on information readily available in software engineering literature. For
other kinds of internals (e.g. hardware, non-procedural software such as
object-oriented code, etc.), corresponding literature on good practises should
be consulted.


Page 218 of 247 Version 3.1 April 2017


**Development (ADV)**


**A.3.1** **Structure of procedural software**


586 The structure of procedural software is traditionally assessed according to its
_modularity_ . Software written with a modular design aids in achieving
understandability by clarifying what dependencies a module has on other
modules ( _coupling_ ) and by including in a module only tasks that are strongly
related to each other ( _cohesion_ ). The use of modular design reduces the
interdependence between elements of the TSF and thus reduces the risk that
a change or error in one module will have effects throughout the TOE. Its use
enhances clarity of design and provides for increased assurance that
unexpected effects do not occur. Additional desirable properties of modular
decomposition are a reduction in the amount of redundant or unneeded code.


587 Minimising the amount of functionality in the TSF allows the evaluator as
well as the developer to focus only on that functionality which is necessary
for SFR enforcement, contributing further to understandability and further
lowering the likelihood of design or implementation errors.


588 The incorporation of modular decomposition, layering and minimisation into
the design and implementation process must be accompanied by sound
software engineering considerations. A practical, useful software system will
usually entail some undesirable coupling among modules, some modules that
include loosely-related functions, and some subtlety or complexity in a
module's design. These deviations from the ideals of modular decomposition
are often deemed necessary to achieve some goal or constraint, be it related
to performance, compatibility, future planned functionality, or some other
factors, and may be acceptable, based on the developer's justification for
them. In applying the requirements of this class, due consideration must be
given to sound software engineering principles; however, the overall
objective of achieving understandability must be achieved.


**A.3.1.1** Cohesion


589 Cohesion is the manner and degree to which the tasks performed by a single
software module are related to one another; types of cohesion include
coincidental, communicational, functional, logical, sequential, and temporal.
These types of cohesion are characterised below, listed in the order of
decreasing desirability.


a) _functional_ cohesion - a module with functional cohesion performs
activities related to a single purpose. A functionally cohesive module
transforms a single type of input into a single type of output, such as
a stack manager or a queue manager.


b) _sequential_ cohesion - a module with sequential cohesion contains
functions each of whose output is input for the following function in
the module. An example of a sequentially cohesive module is one
that contains the functions to write audit records and to maintain a
running count of the accumulated number of audit violations of a
specified type.


April 2017 Version 3.1 Page 219 of 247


**Development (ADV)**


c) _communicational_ cohesion - a module with communicational
cohesion contains functions that produce output for, or use output
from, other functions within the module. An example of a
communicationally cohesive module is an access check module that
includes mandatory, discretionary, and capability checks.


d) _temporal_ cohesion - a module with temporal cohesion contains
functions that need to be executed at about the same time. Examples
of temporally cohesive modules include initialisation, recovery, and
shutdown modules.


e) _logical_ (or _procedural_ ) cohesion - a module with logical cohesion
performs similar activities on different data structures. A module
exhibits logical cohesion if its functions perform related, but different,
operations on different inputs.


f) _coincidental_ cohesion - a module with coincidental cohesion
performs unrelated, or loosely related, activities.


**A.3.1.2** Coupling


590 Coupling is the manner and degree of interdependence between software
modules; types of coupling include call, common and content coupling.
These types of coupling are characterised below, listed in the order of
decreasing desirability:


a) call: two modules are call coupled if they communicate strictly
through the use of their documented function calls; examples of call
coupling are data, stamp, and control, which are defined below.


1. _data_ : two modules are data coupled if they communicate
strictly through the use of call parameters that represent single
data items.


2. _stamp_ : two modules are stamp coupled if they communicate
through the use of call parameters that comprise multiple
fields or that have meaningful internal structures.


3. _control_ : two modules are control coupled if one passes
information that is intended to influence the internal logic of
the other.


b) _common_ : two modules are common coupled if they share a common
data area or a common system resource. Global variables indicate that
modules using those global variables are common coupled. Common
coupling through global variables is generally allowed, but only to a
limited degree. For example, variables that are placed into a global
area, but are used by only a single module, are inappropriately placed,
and should be removed. Other factors that need to be considered in
assessing the suitability of global variables are:


Page 220 of 247 Version 3.1 April 2017


**Development (ADV)**


1. The number of modules that modify a global variable: In
general, only a single module should be allocated the
responsibility for controlling the contents of a global variable,
but there may be situations in which a second module may
share that responsibility; in such a case, sufficient justification
must be provided. It is unacceptable for this responsibility to
be shared by more than two modules. (In making this
assessment, care should be given to determining the module
actually responsible for the contents of the variable; for
example, if a single routine is used to modify the variable, but
that routine simply performs the modification requested by its
caller, it is the calling module that is responsible, and there
may be more than one such module). Further, as part of the
complexity determination, if two modules are responsible for
the contents of a global variable, there should be clear
indications of how the modifications are coordinated between
them.


2. The number of modules that reference a global variable:
Although there is generally no limit on the number of
modules that reference a global variable, cases in which many
modules make such a reference should be examined for
validity and necessity.


c) _content_ : two modules are content coupled if one can make direct
reference to the internals of the other (e.g. modifying code of, or
referencing labels internal to, the other module). The result is that
some or all of the content of one module are effectively included in
the other. Content coupling can be thought of as using unadvertised
module interfaces; this is in contrast to call coupling, which uses only
advertised module interfaces.


**A.3.2** **Complexity of procedural software**


591 Complexity is the measure of the decision points and logical paths of
execution that code takes. Software engineering literature cites complexity as
a negative characteristic of software because it impedes understanding of the
logic and flow of the code. Another impediment to the understanding of code
is the presence of code that is unnecessary, in that it is unused or redundant.


592 The use of layering to separate levels of abstraction and minimise circular
dependencies further enables a better understanding of the TSF, providing
more assurance that the TOE security functional requirements are accurately
and completely instantiated in the implementation.


593 Reducing complexity also includes reducing or eliminating mutual
dependencies, which pertains both to modules in a single layer and to those
in separate layers. Modules that are mutually dependent may rely on one
another to formulate a single result, which could result in a deadlock
condition, or worse yet, a race condition (e.g., time of check vs. time of use


April 2017 Version 3.1 Page 221 of 247


**Development (ADV)**


concern), where the ultimate conclusion could be indeterminate and subject
to the computing environment at the given instant in time.


594 Design complexity minimisation is a key characteristic of a reference
validation mechanism, the purpose of which is to arrive at a TSF that is
easily understood so that it can be completely analysed. (There are other
important characteristics of a reference validation mechanism, such as TSF
self-protection and non-bypassability; these other characteristics are covered
by requirements in the ADV_ARC family.)

## **A.4 ADV_TDS: Subsystems and Modules**


595 This Section provides additional guidance on the TDS family, and its use of
the terms “subsystem” and “module”. This is followed by a discussion of
how, as more-detailed becomes available, the requirement for the lessdetailed is reduced.


**A.4.1** **Subsystems**


596 Figure 22 shows that, depending on the complexity of the TSF, the design
may be described in terms of subsystems _and_ modules (where subsystems
are at a higher level of abstraction than modules); or it may just be described
in terms of one level of abstraction (e.g., _subsystems_ at lower assurance
levels, _modules_ at higher levels). In cases where a lower level of abstraction
(modules) is presented, requirements levied on higher-level abstractions
(subsystems) are essentially met by default. This concept is further
elaborated in the discussion on subsystems and modules below.


**Figure 22 - Subsystems and Modules**


597 The developer is expected to describe the design of the TOE in terms of
_subsystems_ . The term “subsystem” was chosen to be specifically vague so
that it could refer to units appropriate to the TOE (e.g., subsystems, modules).
subsystems can even be uneven in scope, as long as the requirements for
description of subsystems are met.


Page 222 of 247 Version 3.1 April 2017


**Development (ADV)**


598 The first use of subsystems is to distinguish the TSF boundary; that is, the
portions of the TOE that comprise the TSF. In general, a subsystem is part of
the TSF if it has the capability (whether by design or implementation) to
affect the correct operation of any of the SFRs. For example, for software
that depends on different hardware execution modes to provide domain
separation (see A.1) where SFR-enforcing code is executed in one domain,
then all subsystems that execute in that domain would be considered part of
the TSF. Likewise, if a server outside that domain implemented an SFR (e.g.
enforced an access control policy over objects it managed), then it too would
be considered part of the TSF.


599 The second use of subsystems is to provide a structure for describing the
TSF at a level of description that, while describing how the TSF works, does
not necessarily contain low-level implementation detail found in module
descriptions (discussed later). subsystems are described at either a high level
(lacking an abundance of implementation detail) or a detailed level
(providing more insight into the implementation). The level of description
provided for a subsystem is determined by the degree to which that
subsystem is responsible for implementing an SFR.


600 An _SFR-enforcing_ subsystem is a subsystem that provides mechanisms for
enforcing an element of any SFR, or directly supports a subsystem that is
responsible for enforcing an SFR. If a subsystem provides (implements) an
SFR-enforcing TSFI, then the subsystem is SFR-enforcing.


601 Subsystems can also be identified as _SFR-supporting_ and _SFR-non-_
_interfering_ . An SFR-supporting subsystem is one that is depended on by an
SFR-enforcing subsystem in order to implement an SFR, but does not play as
direct a role as an SFR-enforcing subsystem. An SFR-non-interfering
subsystem is one that is not depended upon, in either a supporting or
enforcing role, to implement an SFR.


**A.4.2** **Modules**


602 A module is generally a relatively small architectural unit that can be
characterised in terms of the properties discussed in TSF internals
(ADV_INT). When both ADV_TDS.3 Basic modular design (or above)
requirements and TSF internals (ADV_INT) requirements are present in a PP
or ST, a “module” in terms of the TOE design (ADV_TDS) requirements
refers to the same entity as a “module” for the TSF internals (ADV_INT)
requirements. Unlike subsystems, modules describe the implementation in a
level of detail that can serve as a guide to reviewing the implementation
representation.


603 It is important to note that, depending on the TOE, modules and subsystems
may refer to the same abstraction. For ADV_TDS.1 Basic design and
ADV_TDS.2 Architectural design (which do not require description at the
module level) the subsystem description provides the lowest level detail
available about the TSF. For ADV_TDS.3 Basic modular design (which
require module descriptions) these descriptions provide the lowest level of
detail, while the subsystem descriptions (if they exist as separate entities)


April 2017 Version 3.1 Page 223 of 247


**Development (ADV)**


merely serve to put to the module descriptions in context. That is, it is not
necessary to provide detailed subsystem descriptions if module descriptions
exist. In TOEs that are sufficiently simple, a separate “subsystem
description” is not necessary; the requirements can be met through
documentation provided by modules. For complex TOEs, the purpose of the
subsystem description (with respect to the TSF) is to provide the reader
context so they can focus their analysis appropriately. This difference is
illustrated in Figure 22.


604 An SFR-enforcing module is a module that completely or partially
implements a security functional requirement (SFR) in the ST. Such modules
may implement an SFR-enforcing TSFI, but some functionality expressed in
an SFR (for example, audit and object re-use functionality) may not be
directly tied to a single TSFI. As was the case with subsystems, SFRsupporting modules are those modules that are depended upon by an SFRenforcing module, but are not responsible for directly implementing an SFR.
SFR-non-interfering modules are those modules that do not deal, directly or
indirectly, with the enforcement of SFRs.


605 It is important to note that the determination of what “directly implements”
means is somewhat subjective. In the narrowest sense of the term, it could be
interpreted to mean the one or two lines of code that actually perform a
comparison, zeroing operation, etc. that implements a requirement. A
broader interpretation might be that it includes the module that is invoked in
response to a SFR-enforcing TSFI, and all modules that may be invoked in
turn by that module (and so on until the completion of the call). Neither of
these interpretations is particularly satisfying, since the narrowness of the
first interpretation may lead to important modules being incorrectly
categorised as SFR supporting, while the second leads to modules that are
actually not SFR-enforcing being classified as such.


606 A description of a module should be such that one could create an
implementation of the module from the description, and the resulting
implementation would be 1) identical to the actual TSF implementation in
terms of the interfaces presented, 2) identical in the use of interfaces that are
mentioned in the design, and 3) functionally equivalent to the description of
the purpose of the TSF module. For instance, RFC 793 provides a high-level
description of the TCP protocol. It is necessarily implementation
independent. While it provides a wealth of detail, it is _**not**_ a suitable design
description because it is not specific to an implementation. An actual
implementation can add to the protocol specified in the RFC, and
implementation choices (for example, the use of global data vs. local data in
various parts of the implementation) may have an impact on the analysis that
is performed. The design description of the TCP module would list the
interfaces presented by the implementation (rather than just those defined in
RFC 793), as well as an algorithm description of the processing associated
with the modules implementing TCP (assuming they were part of the TSF).


607 In the design, modules are described in detail in terms of the function they
provide (the purpose); the interfaces they present (when required by the
criteria); the return values from such interfaces; the interfaces (presented by


Page 224 of 247 Version 3.1 April 2017


**Development (ADV)**


other modules) they use (provided those interfaces are required to be also
described); and a description of how they provide their functionality using a
technique appropriate to the method used to implement the module.


608 The purpose of a module should be described indicating what function the
module is providing. It should be sufficient so that the reader could get a
general idea of what the module's function is in the architecture.


609 The interfaces presented by a module are those interfaces used by other
modules to invoke the functionality provided. Interfaces include both _explicit_
interfaces (e.g., a calling sequence invoked by other modules) as well as
_implicit_ interfaces (e.g., global data manipulated by the module). Interfaces
are described in terms of how they are invoked, and any values that are
returned. This description would include a list of parameters, and
descriptions of these parameters. If a parameter were expected to take on a
set of values (e.g., a “flag” parameter), the complete set of values the
parameter could take on that would have an effect on module processing
would be specified. Likewise, parameters representing data structures are
described such that each field of the data structure is identified and described.
Global data should be described to the extent required to understand their
purpose. The level of description required for a global data structure needs to
be identical to the one for module interfaces, where the input parameter and
return values correspond to the individual fields and their possible values in
the data structure. Global data structures may be described separate from the
modules that manipulate or read them as long as the design of the modules
contain sufficient information about the global data structures updated or the
information extracted from global data structures.


610 Note that different programming languages may have additional “interfaces”
that would be non-obvious; an example would be operator/function
overloading in C++. This “implicit interface” in the class description would
also be described as part of the module design. Note that although a module
could present only one interface, it is more common that a module presents a
small set of related interfaces.


611 When it is required to describe the interfaces used by a module, it must be
clear from either the design description of the module or the purpose of the
module called, what service is expected from the module called. For example
if Module A is being described, and it uses Module B's bubble sort routine,
the description of the interaction between modules must allow to identify
why Module B's bubble sort routine is called and what this call contributes to
the implementation of the SFRs. The interface and purpose of Module B's
bubble sort routine must be described as part of the interfaces of Module B
(provided the level of ADV_TDS and the classification of Module B require
a description its interfaces) and so Module A just needs to identify what data
it needs to have sorted using this routine. An adequate description would be:
"Module A invokes Module B's interface _double_bubble()_ to sort the
usernames in alphabetical order".


612 Note that if this sorting of the user names is not important for the
enforcement of any SFR (e. g. it is just done to speed up things and an


April 2017 Version 3.1 Page 225 of 247


**Development (ADV)**


algorithmically identical implementation of Module A could also avoid to
have the usernames sorted), the use of Module B's bubble sort routine is not
SFR-enforcing and it is suffcient to explain in the description of Module A
that the usernames are sorted in alphabetical order to enhance performance.
Module B may be classified as "SFR-supporting" only and the level of
ADV_TDS chosen indicates if the interfaces of SFR-supporting modules
need to be described or if its is sufficient to just describe the purpose of
Module B.


613 As discussed previously, the algorithmic description of the module should
describe in an algorithmic fashion the implementation of the module. This
can be done in pseudo-code, through flow charts, or (at ADV_TDS.3 Basic
modular design) informal text. It discusses how the module inputs and called
functions are used to accomplish the module's function. It notes changes to
global data, system state, and return values produced by the module. It is at
the level of detail that an implementation could be derived that would be
very similar to the actual implementation of the TOE.


614 It should be noted that source code does not meet the module documentation
requirements. Although the module design describes the implementation, it
is _not_ the implementation. The comments surrounding the source code might
be sufficient documentation if they provide an explanation of the intent of
the source code. In-line comments that merely state what each line of code is
doing are useless because they provide no explanation of what the module is
meant to accomplish.


615 In the elements below, the labels (SFR-enforcing, SFR-supporting, and SFRnon-interfering) discussed for subsystems and modules are used to describe
the amount and type of information that needs to be made available by the
developer. The elements have been structured so that there is no expectation
that the developer provide _only_ the information specified. That is, if the
developer's documentation of the TSF provides the information in the
requirements below, there is no expectation that the developer update their
documentation and label subsystems and modules as SFR-enforcing, SFRsupporting, and SFR-non-interfering. The primary purpose of this labelling is
to allow developers with less mature development methodologies (and
associated artifacts, such as detailed interface and design documentation) to
provide the necessary evidence without undue cost.


**A.4.3** **Levelling Approach**


616 Because there is subjectivity in determining what is SFR-enforcing vs. SFRsupporting (and in some cases, even determining what is SFR-noninterfering) the following paradigm has been adopted in this family. In early
components of the family, the developer makes a determination about the
classification of the subsystems into SFR-enforcing, etc., supplying the
appropriate information, and there is little additional evidence for the
evaluator to examine to support this claim. As the level of desired assurance
increases, while the developer still makes a classification determination, the
evaluator obtains more and more evidence that is used to confirm the
developer's classification.


Page 226 of 247 Version 3.1 April 2017


**Development (ADV)**


617 In order to focus the evaluator's analysis on the SFR-related portions of the
TOE, especially at lower levels of assurance, the components of the family
are levelled such that initially detailed information is required only for SFRenforcing architectural entities. As the level of assurance increases, more
information is required for SFR-supporting and (eventually) SFR-noninterfering entities. It should be noted that even when complete information
is required, it is not required that all of this information be analysed in the
same level of detail. The focus should be in all cases on whether the
_necessary_ information has been provided and analysed.


618 Table 14 summarises the information required at each of the family
components for the architectural entities to be described.




















|Col1|TSF subsystem|Col3|Col4|TSF Module|Col6|Col7|
|---|---|---|---|---|---|---|
||SFR Enforce|SFR<br>Support|SFR NI|SFR<br>Enforce|SFR<br>Support|SFR NI|
|ADV_TDS.1<br>Basic design <br>(informal<br>presentation)|structure,<br>summary of<br>SFR-Enf.<br>behaviour,<br>interactions|designation<br>support(1)|designation<br>support||||
|ADV_TDS.2<br>Architectural<br>design (informal<br>presentation)|structure,<br>detailed<br>description of<br>SFR-Enf.<br>behaviour,<br>summary of<br>other<br>behaviour,<br>interactions|structure,<br>summary of<br>other<br>behaviour,<br>interactions|designation<br>support,<br>interactions||||
|ADV_TDS.3<br>Basic modular<br>design (informal<br>presentation)|description,<br>interactions|description,<br>interactions|description,<br>interactions|purpose,<br>SFR<br>interfaces(2)|interaction,<br>purpose|interaction,<br>purpose|
|ADV_TDS.4<br>Semiformal<br>modular design <br>(semiformal<br>presentation)|description,<br>interactions|description,<br>interactions|description,<br>interactions|purpose,<br>SFR<br>interfaces|purpose,<br>SFR<br>interfaces|interaction,<br>purpose|
|ADV_TDS.5<br>Complete<br>semiformal<br>modular design <br>(semiformal<br>presentation)|description,<br>interactions|description,<br>interactions|description,<br>interactions|purpose, all<br>interfaces(3)|purpose, all<br>interfaces|purpose,<br>all<br>interfaces|
|ADV_TDS.6<br>Complete<br>semiformal<br>modular design<br>with formal<br>high-level<br>design<br>presentation <br>(semiformal|description,<br>interactions|description,<br>interactions|description,<br>interactions|purpose, all<br>interfaces|purpose, all<br>interfaces|purpose,<br>all<br>interfaces|



April 2017 Version 3.1 Page 227 of 247


**Development (ADV)**







|Col1|TSF subsystem|Col3|Col4|TSF Module|Col6|Col7|
|---|---|---|---|---|---|---|
||SFR Enforce|SFR<br>Support|SFR NI|SFR<br>Enforce|SFR<br>Support|SFR NI|
|presentation;<br>additional formal<br>presentation)<br>|||||||


/ module is needed.
(2) _SFR interfaces_ means that the module description contains, for each SFR-related interface, the returned
values and the called interfaces to other modules.
(3) _All interfaces_ means that the module description contains, for each interface, the returned values and the
called interfaces to other modules.


**Table 14 Description Detail Levelling**

## **A.5 Supplementary material on formal methods**


619 Formal methods provide a mathematical representation of the TSF and its
behaviour and are required by the ADV_FSP.6 Complete semi-formal
functional specification with additional formal specification, ADV_SPM.1
Formal TOE security policy model, and ADV_TDS.6 Complete semiformal
modular design with formal high-level design presentation components.
There are two aspects of formal methods: the _specification language_ that is
used for formal expression, and the _theorem prover_ that mathematically
proves the completeness and correctness of the formal specification.


620 A formal specification is expressed within a formal system based upon wellestablished mathematical concepts. These mathematical concepts are used to
define well-defined semantics, syntax and rules of inference. A formal
system is an abstract system of identities and relations that can be described
by specifying a formal alphabet, a formal language over that alphabet which
is based on a formal syntax, and a set of formal rules of inference for
constructing derivations of sentences in the formal language.


621 The evaluator should examine the identified formal systems to make sure
that:


 The semantics, syntax and inference rules of the formal system are
defined or a definition is referenced.


 Each formal system is accompanied by explanatory text that provides
defined **semantics** so that:


1. the explanatory text provides defined meanings of terms,
abbreviations and acronyms that are used in a context other
than that accepted by normal usage,


2. the use of a formal system and semiformal notation use is
accompanied by supporting explanatory text in informal style
appropriate for unambiguous meaning,


3. the formal system is able to express rules and characteristics
of applicable SFPs, security functionality and interfaces


Page 228 of 247 Version 3.1 April 2017


**Development (ADV)**


(providing details of effects, exceptions and error messages)
of TSF, their subsystems or modules to be specified for the
assurance family for which the notations are used.


4. the notation provides rules to determine the meaning of
syntactical valid constructs.


 Each formal system uses a formal syntax that provides rules to
unambiguously recognise constructs.


 Each formal system provides proof rules which


1. support logical reasoning of well-established mathematical
concepts,


2. help to prevent derivation of contradictions


622 If the developer uses a formal system which is already accepted by the
evaluation authority the evaluator can rely on the level of formality and
strength of the system and focus on the instantiation of the formal system to
the TOE specifications and correspondence proofs.


623 The formal style supports mathematical proofs of the security properties
based on the security features, the consistency of refinements and the
correspondence of the representations. Formal tool support seems adequate
whenever manual derivations would otherwise become long winded and
incomprehensible. Formal tools are also apt to reduce the error probability
inherent in manual derivations.


624 Examples of formal systems:


 The **Z specification language** is highly expressive, and supports
many different methods or styles of formal specification. The use of
Z has been predominantly for model-oriented specification, using
_schemas_ [to formally specify operations. See http://vl.zuser.org/ for](http://vl.zuser.org/)
more information.


 **ACL2** is an open-source formal system comprising a LISP-based
specification language and a theorem prover. See
[http://www.cs.utexas.edu/users/moore/acl2/ for further information.](http://www.cs.utexas.edu/users/moore/acl2/)


 **Isabelle** is a popular generic theorem proving environment that
allows mathematical formulae to be expressed in a formal language
and provides tools for proving those formulae within a logical
[calculus (see e.g. http://www.cl.cam.ac.uk/Research/HVG/Isabelle/](http://www.cl.cam.ac.uk/Research/HVG/Isabelle/)
for additional information)


 The **B method** is a formal system based on the propositional calculus,
the first order predicate calculus with inference rules and set theory
[(see e.g. http://vl.fmnet.info/b/ for further information).](http://vl.fmnet.info/b/)


April 2017 Version 3.1 Page 229 of 247


**Composition (ACO)**

# **B Composition (ACO)** **(informative)**


625 The goal of this annex is to explain the concepts behind composition
evaluations and the ACO criteria. This annex does not define the ASE
criteria; this definition can be found in chapter 12.

## **B.1 Necessity for composed TOE evaluations**


626 The IT market is, on the whole, made up of vendors offering a particular type
of product/technology. Although there is some overlap, where a PC hardware
vendor may also offer application software and/or operating systems or a
chip manufacturer may also develop a dedicated operating system for their
own chipset, it is often the case that an IT solution is implemented by a
variety of vendors.


627 There is sometimes a need for assurance in the combination (composition) of
components in addition to the assurance of the individual components.
Although there is cooperation between these vendors, in the dissemination of
certain material required for the technical integration of the components, the
agreements rarely stretch to the extent of providing detailed design
information and development process/procedure evidence. This lack of
information from the developer of a component on which another component
relies means that the dependent component developer does not have access to
the type of information necessary to perform an evaluation of both the
dependent and base components at EAL2 or above. Therefore, while an
evaluation of the dependent component can still be performed at any
assurance level, to compose components with assurance at EAL2 or above it
is necessary to reuse the evaluation evidence and results of evaluations
performed for the component developer.


628 It is intended that the ACO criteria are applicable in the situation where one
IT entity is dependent on another for the provision of security services. The
entity providing the services is termed the “base component”, and that
receiving the services is termed the “dependent component”. This
relationship may exist in a number of contexts. For example, an application
(dependent component) may use services provided by an operating system
(base component). Alternatively, the relationship may be peer-to-peer, in the
sense of two linked applications, either running in a common operating
system environment, or on separate hardware platforms. If there is a
dominant peer providing the services to the minor peer, the dominant peer is
considered to be the base component and the minor peer the dependent
component. If the peers provide services to each other in a mutual manner,
each peer will be considered to be the base component for the services
offered and dependent component for the services required. This will require
iterations of the ACO components applying all requirements to each type of
component peer.


Page 230 of 247 Version 3.1 April 2017


**Composition (ACO)**


629 The criteria are also intended to be more broadly applicable, stepwise (where
a composed TOE comprised of a dependent component and a base
component itself becomes the base component of another composed TOE),
in more complex relationships, but this may require further interpretation.


630 It is still required for composed TOE evaluations that the individual
components are evaluated independently, as the composition evaluation
builds on the results of the individual component evaluations. The evaluation
of the dependent component may still be in progress when the composed
TOE evaluation commences. However, the dependent component evaluation
must complete before the composed TOE evaluation completes.


631 The composed evaluation activities may take place at the same time as the
dependent component evaluation. This is due to two factors:


a) Economic/business drivers - the dependent component developer will
either be sponsoring the composition evaluation activities or
supporting these activities as the evaluation deliverables from the
dependent component evaluation are required for composed
evaluation activities.


b) Technical drivers - the components consider whether the requisite
assurance is provided by the base component (e.g. considering the
changes to the base component since completion of the component
evaluation) with the understanding that the dependent component has
recently undergone (is undergoing) component evaluation and all
evaluation deliverables associated with the evaluation are available.
Therefore, there are no activities during composition requesting the
dependent component evaluation activities to be re-verified. Also, it
is verified that the base component forms (one of) the test
configurations for the testing of the dependent component during the
dependent component evaluation, leaving ACO_CTT to consider the
base component in this configuration.


632 The evaluation evidence from the evaluation of the dependent component is
required input into the composed TOE evaluation activities. The only
evaluation material from the evaluation of the base component that is
required as input into the composed TOE evaluation activities:


a) Residual vulnerabilities in the base component, as reported during the
base component evaluation. This is required for the ACO_VUL
activities.


633 No other evaluation evidence from the base component activities should be
required for the composed TOE evaluation, as the evaluation results from the
component evaluation of the base component should be reused. Additional
information about the base component may be required if the composed TOE
TSF includes more of the base component than was considered to be TSF
during component evaluation of the base component.


April 2017 Version 3.1 Page 231 of 247


**Composition (ACO)**


634 The component evaluation of the base and dependent components are
assumed to be complete by the time final verdicts are assigned for the ACO
components.


635 The ACO_VUL components only consider resistance against an attacker
with an attack potential up to Enhanced-Basic. This is due to the level of
design information that can be provided of how the base component provides
the services on which the dependent component relies through application of
the ACO_DEV activities. Therefore, the confidence arising from composed
TOE evaluations using CAPs is limited to a level similar to that obtained
from EAL4 component TOE evaluations. Although assurance in the
components that comprise the composed TOE may be higher than EAL4.

## **B.2 Performing Security Target evaluation for a composed** **TOE**


636 An ST will be submitted by the developer for the evaluation of the composed
(base component + dependent component) TOE. This ST will identify the
assurance package to be applied to the composed TOE, providing assurance
in the composed entity by drawing upon the assurance gained in the
component evaluations.


637 The purpose of considering the composition of components within an ST is
to validate the compatibility of the components from the point of view of
both the environment and the requirements, and also to assess that the
composed TOE ST is consistent with the component STs and the security
policies expressed within them. This includes determining that the
component STs and the security policies expressed within them are
compatible.


638 The composed TOE ST may refer out to the content of the component STs,
or the ST author may chose to reiterate the material of the component STs
within the composed TOE ST providing a rationale of how the component
STs are represented in the composed TOE ST.


639 During the conduct of the ASE_CCL evaluation activities for a composed
TOE ST the evaluator determines that the component STs are accurately
represented in the composed TOE ST. This is achieved through determining
that the composed TOE ST demonstrably conforms to the component TOE
STs. Also, the evaluator will need to determine that the dependencies of the
dependent component on the operational environment are adequately
fulfilled in the composed TOE.


640 The composed TOE description will describe the composed solution. The
logical and physical scope and boundary of the composed solution will be
described, and the logical boundary(ies) between the components will also be
identified. The description will identify the security functionality to be
provided by each component.


641 The statement of SFRs for the composed TOE will identify which
component is to satisfy an SFR. If an SFR is met by both components, then


Page 232 of 247 Version 3.1 April 2017


**Composition (ACO)**


the statement will identify which component meets the different aspects of
the SFR. Similarly the composed TOE Summary Specification will identify
which component provides the security functionality described.


642 The package of ASE: Security Target evaluation requirements applied to the
composed TOE ST should be consistent with the package of ASE: Security
Target evaluation requirements used in the component evaluations.


643 Reuse of evaluation results from the evaluation of component STs can be
made in the instances that the composed TOE ST directly refers to the
component STs. e.g. if the composed TOE ST refers to a component ST for
part of its statement of SFRs, the evaluator can understand that the
requirement for the completion of all assignment and selection operations (as
stated in ASE_REQ.*.3C has been satisfied in the component evaluations.

## **B.3 Interactions between composed IT entities**


644 The TSF of the base component is often defined without knowledge of the
dependencies of the possible applications with which it may by composed.
The TSF of this base component is defined to include all parts of the base
component that have to be relied upon for enforcement of the base
component SFRs. This will include all parts of the base component required
to implement the base component SFRs.


645 The TSFI of this base component represents the interfaces provided by the
TSF to the external entities defined in the statement of SFRs to invoke a
service of the TSF. This includes interfaces to the human user and also
interfaces to external IT entities. However, the TSFI only includes those
interfaces to the TSF, and therefore is not necessarily an exhaustive interface
specification of all possible interfaces available between an external entity
and the base component. The base component may present interfaces to
services that were not considered security-relevant, either because of the
inherent purpose of the service (e.g., adjust type font) or because associated
CC SFRs are not being claimed in the base component's ST (e.g. the login
interface when no FIA: Identification and authentication SFRs are claimed).


646 The functional interfaces provided by the base component are in addition to
the security interfaces (TSFIs), and are not required to be considered during
the base component evaluation. These often include interfaces that are used
by a dependent component to invoke a service provided by the base
component.


647 The base component may include some indirect interfaces through which
TSFIs may be called, e.g. APIs that can be used to invoke a service of the
TSF, which were not considered during the evaluation of the base component.


April 2017 Version 3.1 Page 233 of 247


**Composition (ACO)**


**Figure 23 - Base component abstraction**


648 The dependent component, which relies on the base component, is similarly
defined: interfaces to external entities defined in the SFRs of the component
ST are categorised as TSFI and are examined in ADV_FSP.


649 Any call out from the dependent TSF to the environment in support of an
SFR will indicate that the dependent TSF requires some service from the
environment in order to satisfy the enforcement of the stated dependent
component SFRs. Such a service is outside the dependent component
boundary and the base component is unlikely to be defined in the dependent
ST as an external entity. Hence, the calls for services made out by the
dependent TSF to its underlying platform (the base component) will not be
analysed as part of the Functional specification (ADV_FSP) activities. These
dependencies on the base component are expressed in the dependent
component ST as security objectives for the environment.


650 This abstraction of the dependent component and the interfaces is shown in
Figure 24 below.


Page 234 of 247 Version 3.1 April 2017


**Composition (ACO)**


**Figure 24 - Dependent component abstraction**


651 When considering the composition of the base component and the dependent
component, if the dependent component's TSF requires services from the
base component to support the implementation of the SFR, the interface to
the service will need to be defined. If that service is provided by the base
component's TSF, then that interface should be a TSFI of the base
component and will therefore already be defined within the functional
specification of the base component.


652 If, however, the service called by the dependent component's TSF is not
provided by the TSF of the base component (i.e., it is implemented in the
non-TSF portion of the base component or possibly even in the non-TOE
portion of the base component (not illustrated in Figure 25), there is unlikely
to be a TSFI of the base component relating to the service, unless the service
is mediated by the TSF of the base component. The interfaces to these
services from the dependent component to the operational environment are
considered in the family Reliance of dependent component (ACO_REL).


653 The non-TSF portion of the base component is drawn into the TSF of the
composed TOE due to the dependencies the dependent component has on the
base component to support the SFRs of the dependent component. Therefore,
in such cases, the TSF of the composed TOE would be larger than simply the
sum of the components' TSFs.


April 2017 Version 3.1 Page 235 of 247


**Composition (ACO)**


**Figure 25 - Composed TOE abstraction**


654 It may be the case that the base component TSFI is being called in a manner
that was unforeseen in the base component evaluation. Hence there would be
a requirement for further testing of the base component TSFI.


655 The possible interfaces are further described in the following diagram
(Figure 26) and supporting text.


**Figure 26 - Composed component interfaces**


a) Arrows going _into_ 'dependent component-a' (A and B) = where the
component expects the environment to respond to a service request
(responding to calls out from dependent component to the
environment);


Page 236 of 247 Version 3.1 April 2017


**Composition (ACO)**


b) Arrows coming _out_ of 'base component-b' (C and D) = interfaces of
services provided by the base component to the environment;


c) Broken lines between components = types of communication
between pairs of interfaces;


d) The other (grey) arrows = interfaces that are described by the given
criteria.


656 The following is a simplification, but explains the considerations that need to
be made.


657 There are components a ('dependent component-a') and b ('base componentb'): the arrows coming _out_ of TSF-a are services provided by TSF-a and are
therefore TSFIs(a); likewise, the arrows coming _out_ of TSF-b (“C”) are
TSFIs(b). These are each detailed in their respective functional specs.
component-a is such that it requires services from its environment: those
needed by the TSF(a) are labelled “A”; the other (not related to TSF-a)
services are labelled “B”.


658 When component-a and component-b are combined, there are four possible
combinations of {services needed by component-a} and {services provided
by component-b}, shown as broken lines (types of communication between
pairs of interfaces). Any set of these might exist for a particular composition:


a) TSF-a needs those services that are provided by TSF-b ("A" is
connected to "C"): this is straightforward: the details about "C" are in
the FSP for component-b. In this instance the interfaces should all be
defined in the functional specifications for the component-b.


b) Non-TSF-a needs those services that are provided by TSF-b (“B” is
connected to “C”): this is straightforward (again, the details about
“C” are in the FSP for component-b), but unimportant: security-wise.


c) Non-TSF-a needs those services that are provided by non-TSF-b (“B”
is connected to “D”): we have no details about D, but there are no
security implications about the use of these interfaces, so they do not
need to be considered in the evaluation, although they are likely to be
an integration issue for the developer.


d) TSF-a needs those services that are provided by non-TSF-b (“A” is
connected to “D”): this would arise when component-a and
component-b have different senses of what a “security service” is.
Perhaps component-b is making no claims about I&A (has no FIA
SFRs in its ST), but component-a needs authentication provided by
its environment. There are no details about the “D” interfaces
available (they are not TSFI (b), so they are not in component-b's
FSP).


April 2017 Version 3.1 Page 237 of 247


**Composition (ACO)**


659 Note: if the kind of interaction described in case d above exists, then the TSF
of the composed TOE would be TSF-a + TSF-b + Non-TSF-b. Otherwise,
the TSF of the composed TOE would be TSF-a + TSF-b.


660 Interfaces types 2 and 4 of Figure 26 are not directly relevant to the
evaluation of the composed TOE. Interfaces 1 and 3 will be considered
during the application of different families:


a) Functional specification (ADV_FSP) (for component-b) will describe
the C interfaces.


b) Reliance of dependent component (ACO_REL) will describe the A
interfaces.


c) Development evidence (ACO_DEV) will describe the C interfaces
for connection type 1 and the D interfaces for connection type 3.


661 A typical example where composition may be applied is a database
management system (DBMS) that relies upon its underlying operating
system (OS). During the evaluation of the DBMS component, there will be
an assessment made of the security properties of that DBMS (to whatever
degree of rigour is dictated by the assurance components used in the
evaluation): its TSF boundary will be identified, its functional specification
will be assessed to determine whether it describes the interfaces to the
security services provided by the TSF, perhaps additional information about
the TSF (its design, architecture, internal structure) will be provided, the TSF
will be tested, aspects of its life-cycle and its guidance documentation will be
assessed, etc.


662 However, the DBMS evaluation will not call for any evidence concerning the
dependency the DBMS has on the OS. The ST of the DBMS will most likely
state assumptions about the OS in its Assumptions section and state security
objectives for the OS in its Environment section. The DBMS ST may even
instantiate those objectives for the environment in terms of SFRs for the OS.
However, there will be no specification for the OS that mirrors the detail in
the functional specification, architecture description, or other ADV evidence
as for the DBMS. Reliance of dependent component (ACO_REL) will fulfil
that need.


663 Reliance of dependent component (ACO_REL) describes the interfaces of
the dependent TOE that make the calls to the base component for the
provision of services. These are the interfaces to which the base component
is to respond. The interface descriptions are provided from the dependent
component's viewpoint.


664 Development evidence (ACO_DEV) describes the interfaces provided by the
base component, which respond to the dependent component service requests.
These interfaces are mapped to the relevant dependent component interfaces
that are identified in the reliance information. (The completeness of this
mapping, whether the base component interfaces described represent all
dependent component interfaces, is not verified here, but in Composition


Page 238 of 247 Version 3.1 April 2017


**Composition (ACO)**


rationale (ACO_COR)). At the higher levels of ACO_DEV the subsystems
providing the interfaces are described.


665 Any interfaces required by the dependent component that have not been
described for the base component are reported in the rationale for
Composition rationale (ACO_COR). The rationale also reports whether the
interfaces of the base component on which the dependent component relies
were considered within the base component evaluation. For any interfaces
that were not considered in the base component evaluation, a rationale is
provided of the impact of using the interface on the base component TSF.


April 2017 Version 3.1 Page 239 of 247


**Cross reference of assurance component dependencies**

# **C Cross reference of assurance component** **dependencies** **(informative)**


666 The dependencies documented in the components of Chapters 10 and 12-18
are the direct dependencies between the assurance components.


667 The following dependency tables for assurance components show their direct,
indirect and optional dependencies. Each of the components that is a
dependency of some assurance component is allocated a column. Each
assurance component is allocated a row. The value in the table cell indicate
whether the column label component is directly required (indicated by a
cross “X”) or indirectly required (indicated by a dash “-”), by the row label
component. If no character is presented, the component is not dependent
upon another component.

|Col1|ACO_DEV.1|ACO_DEV.2|ACO_DEV.3|ACO_REL.1|ACO_REL.2|ALC_CMC.1|ALC_CMS.1|
|---|---|---|---|---|---|---|---|
|ACO_COR.1|X|||X||X|-|
|ACO_CTT.1|X|||X||||
|ACO_CTT.2||X||-|X|||
|ACO_DEV.1||||X||||
|ACO_DEV.2||||X||||
|ACO_DEV.3|||||X|||
|ACO_REL.1||||||||
|ACO_REL.2||||||||
|ACO_VUL.1|X|||-||||
|ACO_VUL.2||X||-||||
|ACO_VUL.3|||X||-|||



**Table 15 Dependency table for Class ACO: Composition**


Page 240 of 247 Version 3.1 April 2017


**Cross reference of assurance component dependencies**

|Col1|ADV_FSP.1|ADV_FSP.2|ADV_FSP.3|ADV_FSP.4|ADV_FSP.5|ADV_FSP.6|ADV_IMP.1|ADV_TDS.1|ADV_TDS.3|ALC_CMC.5|ALC_CMS.1|ALC_DVS.2|ALC_LCD.1|ALC_TAT.1|
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
|ADV_ARC.1|X|-||||||X|||||||
|ADV_FSP.1|||||||||||||||
|ADV_FSP.2||-||||||X|||||||
|ADV_FSP.3||-||||||X|||||||
|ADV_FSP.4||-||||||X|||||||
|ADV_FSP.5||-||-|||X|X|-|||||-|
|ADV_FSP.6||-||-|||X|X|-|||||-|
|ADV_IMP.1||-||-|||-|-|X|||||X|
|ADV_IMP.2||-||-|||-|-|X|X|-|-|-|X|
|ADV_INT.1||-||-|||X|-|X|||||X|
|ADV_INT.2||-||-|||X|-|X|||||X|
|ADV_INT.3||-||-|||X|-|X|||||X|
|ADV_SPM.1||-||X||||-|||||||
|ADV_TDS.1||X||||||-|||||||
|ADV_TDS.2||-|X|||||-|||||||
|ADV_TDS.3||-||X||||-|||||||
|ADV_TDS.4||-||-|X||-|-|-|||||-|
|ADV_TDS.5||-||-|X||-|-|-|||||-|
|ADV_TDS.6||-||-||X|-|-|-|||||-|



**Table 16 Dependency table for Class ADV: Development**

|Col1|ADV_FSP.1|
|---|---|
|AGD_OPE.1|X|
|AGD_PRE.1||



**Table 17 Dependency table for Class AGD: Guidance documents**


April 2017 Version 3.1 Page 241 of 247


**Cross reference of assurance component dependencies**

|Col1|ADV_FSP.2|ADV_FSP.4|ADV_IMP.1|ADV_TDS.1|ADV_TDS.3|ALC_CMS.1|ALC_DVS.1|ALC_DVS.2|ALC_LCD.1|ALC_TAT.1|
|---|---|---|---|---|---|---|---|---|---|---|
|ALC_CMC.1||||||X|||||
|ALC_CMC.2||||||X|||||
|ALC_CMC.3||||||X|X||X||
|ALC_CMC.4||||||X|X||X||
|ALC_CMC.5||||||X||X|X||
|ALC_CMS.1|||||||||||
|ALC_CMS.2|||||||||||
|ALC_CMS.3|||||||||||
|ALC_CMS.4|||||||||||
|ALC_CMS.5|||||||||||
|ALC_DEL.1|||||||||||
|ALC_DVS.1|||||||||||
|ALC_DVS.2|||||||||||
|ALC_FLR.1|||||||||||
|ALC_FLR.2|||||||||||
|ALC_FLR.3|||||||||||
|ALC_LCD.1|||||||||||
|ALC_LCD.2|||||||||||
|ALC_TAT.1|-|-|X|-|-|||||-|
|ALC_TAT.2|-|-|X|-|-|||||-|
|ALC_TAT.3|-|-|X|-|-|||||-|



**Table 18 Dependency table for Class ALC: Life-cycle support**

|Col1|APE_ECD.1|APE_INT.1|APE_OBJ.2|APE_REQ.1|APE_SPD.1|
|---|---|---|---|---|---|
|APE_CCL.1|X|X||X||
|APE_ECD.1||||||
|APE_INT.1||||||
|APE_OBJ.1||||||
|APE_OBJ.2|||||X|
|APE_REQ.1|X|||||
|APE_REQ.2|X||X||-|
|APE_SPD.1||||||



**Table 19 Dependency table for Class APE: Protection Profile evaluation**


Page 242 of 247 Version 3.1 April 2017


**Cross reference of assurance component dependencies**

|Col1|ACE_ECD.1|ACE_INT.1|ACE_MCO.1|ACE_OBJ.1|ACE_REQ.1|ACE_SPD.1|
|---|---|---|---|---|---|---|
|ACE_CCL.1|X|X||-|X||
|ACE_CCO.1|-|X|X|-|X|-|
|ACE_ECD.1|||||||
|ACE_INT.1|||||||
|ACE_MCO.1|-|X||X|X|X|
|ACE_OBJ.1|||||||
|ACE_REQ.1|X|||X|||
|ACE_SPD.1|||||||



**Table 20 Dependency table for Class ACE: Protection Profile Configuration evaluation**

|Col1|ADV_ARC.1|ADV_FSP.1|ADV_FSP.2|ADV_TDS.1|ASE_ECD.1|ASE_INT.1|ASE_OBJ.2|ASE_REQ.1|ASE_SPD.1|
|---|---|---|---|---|---|---|---|---|---|
|ASE_CCL.1|||||X|X||X||
|ASE_ECD.1||||||||||
|ASE_INT.1||||||||||
|ASE_OBJ.1||||||||||
|ASE_OBJ.2|||||||||X|
|ASE_REQ.1|||||X|||||
|ASE_REQ.2|||||X||X||-|
|ASE_SPD.1||||||||||
|ASE_TSS.1||X|||-|X||X||
|ASE_TSS.2|X|-|-|-|-|X||X||



**Table 21 Dependency table for Class ASE: Security Target evaluation**


April 2017 Version 3.1 Page 243 of 247


**Cross reference of assurance component dependencies**

|Col1|ADV_ARC.1|ADV_FSP.1|ADV_FSP.2|ADV_FSP.3|ADV_FSP.4|ADV_FSP.5|ADV_IMP.1|ADV_TDS.1|ADV_TDS.2|ADV_TDS.3|ADV_TDS.4|AGD_OPE.1|AGD_PRE.1|ALC_TAT.1|ATE_COV.1|ATE_FUN.1|
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
|ATE_COV.1|||X|||||-|||||||-|X|
|ATE_COV.2|||X|||||-|||||||-|X|
|ATE_COV.3|||X|||||-|||||||-|X|
|ATE_DPT.1|X|-|-|-||||-|X||||||-|X|
|ATE_DPT.2|X|-|-||-|||-||X|||||-|X|
|ATE_DPT.3|X|-|-||-|-|-|-||-|X|||-|-|X|
|ATE_DPT.4|X|-|-||-|-|X|-||-|X|||-|-|X|
|ATE_FUN.1|||-|||||-|||||||X|-|
|ATE_FUN.2|||-|||||-|||||||X|-|
|ATE_IND.1||X||||||||||X|X||||
|ATE_IND.2||-|X|||||-||||X|X||X|X|
|ATE_IND.3||-|-||X|||-||||X|X||X|X|



**Table 22 Dependency table for Class ATE: Tests**

|Col1|ADV_ARC.1|ADV_FSP.1|ADV_FSP.2|ADV_FSP.3|ADV_FSP.4|ADV_IMP.1|ADV_TDS.1|ADV_TDS.2|ADV_TDS.3|AGD_OPE.1|AGD_PRE.1|ALC_TAT.1|ATE_COV.1|ATE_DPT.1|ATE_FUN.1|
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
|AVA_VAN.1||X||||||||X|X|||||
|AVA_VAN.2|X|-|X||||X|||X|X|||||
|AVA_VAN.3|X|-|-|-|X|X|-|-|X|X|X|-|-|X|-|
|AVA_VAN.4|X|-|-|-|X|X|-|-|X|X|X|-|-|X|-|
|AVA_VAN.5|X|-|-|-|X|X|-|-|X|X|X|-|-|X|-|



**Table 23 Dependency table for Class AVA: Vulnerability assessment**


Page 244 of 247 Version 3.1 April 2017


**Cross reference of PPs and assurance components**

# **D Cross reference of PPs and assurance** **components** **(informative)**


668 Table 24 describes the relationship between PPs and the families and










|components of the APE class.|Col2|Col3|Col4|
|---|---|---|---|
|Assurance class|Assurance<br>family|Assurance component|Assurance component|
|Assurance class|Assurance<br>family|Low Assurance<br>PP|PP|
|Protection Profile<br>evaluation|APE_CCL|**1**|1|
|Protection Profile<br>evaluation|APE_ECD|**1**|1|
|Protection Profile<br>evaluation|APE_INT|**1**|1|
|Protection Profile<br>evaluation|APE_OBJ|**1**|**2**|
|Protection Profile<br>evaluation|APE_REQ|**1**|**2**|
|Protection Profile<br>evaluation|APE_SPD||**1**|



**Table 24 PP assurance level summary**


April 2017 Version 3.1 Page 245 of 247


**Cross reference of EALs and assurance components**

# **E Cross reference of EALs and assurance** **components** **(informative)**


669 Table 25 describes the relationship between the evaluation assurance levels
and the assurance classes, families and components.






















|Assurance<br>class|Assurance<br>Family|Assurance Components by Evaluation<br>Assurance Level|Col4|Col5|Col6|Col7|Col8|Col9|
|---|---|---|---|---|---|---|---|---|
|Assurance<br>class|Assurance<br>Family|EAL1|EAL2|EAL3|EAL4|EAL5|EAL6|EAL7|
|Development|ADV_ARC||**1**|1|1|1|1|1|
|Development|ADV_FSP|**1**|**2**|**3**|**4**|**5**|5|**6**|
|Development|ADV_IMP||||**1**|1|**2**|2|
|Development|ADV_INT|||||**2**|**3**|3|
|Development|ADV_SPM||||||**1**|1|
|Development|ADV_TDS||**1**|**2**|**3**|**4**|**5**|**6**|
|Guidance<br>documents|AGD_OPE|**1**|1|1|1|1|1|1|
|Guidance<br>documents|AGD_PRE|**1**|1|1|1|1|1|1|
|Life-cycle<br>support|ALC_CMC|**1**|**2**|**3**|**4**|4|**5**|5|
|Life-cycle<br>support|ALC_CMS|**1**|**2**|**3**|**4**|**5**|5|5|
|Life-cycle<br>support|ALC_DEL||**1**|1|1|1|1|1|
|Life-cycle<br>support|ALC_DVS|||**1**|1|1|**2**|2|
|Life-cycle<br>support|ALC_FLR||||||||
|Life-cycle<br>support|ALC_LCD|||**1**|1|1|1|**2**|
|Life-cycle<br>support|ALC_TAT||||**1**|**2**|**3**|3|
|Security<br>Target<br>evaluation|ASE_CCL|**1**|1|1|1|1|1|1|
|Security<br>Target<br>evaluation|ASE_ECD|**1**|1|1|1|1|1|1|
|Security<br>Target<br>evaluation|ASE_INT|**1**|1|1|1|1|1|1|
|Security<br>Target<br>evaluation|ASE_OBJ|**1**|**2**|2|2|2|2|2|
|Security<br>Target<br>evaluation|ASE_REQ|**1**|**2**|2|2|2|2|2|
|Security<br>Target<br>evaluation|ASE_SPD||**1**|1|1|1|1|1|
|Security<br>Target<br>evaluation|ASE_TSS|**1**|1|1|1|1|1|1|
|Tests|ATE_COV||**1**|**2**|2|2|**3**|3|
|Tests|ATE_DPT|||**1**|1|**3**|3|**4**|
|Tests|ATE_FUN||**1**|1|1|1|**2**|2|
|Tests|ATE_IND|**1**|**2**|2|2|2|2|**3**|
|Vulnerability<br>assessment|AVA_VAN|**1**|**2**|2|**3**|**4**|**5**|5|



**Table 25 Evaluation assurance level summary**


Page 246 of 247 Version 3.1 April 2017


**Cross reference of CAPs and assurance components**

# **F Cross reference of CAPs and assurance** **components** **(informative)**


670 Table 26 describes the relationship between the composition assurance levels
and the assurance classes, families and components.














|Assurance class|Assurance<br>Family|Assurance Components by<br>Composition Assurance<br>Package|Col4|Col5|
|---|---|---|---|---|
|Assurance class|Assurance<br>Family|CAP-A|CAP-B|CAP-C|
|Composition|ACO_COR|**1**|1|1|
|Composition|ACO_CTT|**1**|**2**|2|
|Composition|ACO_DEV|**1**|**2**|**3**|
|Composition|ACO_REL|**1**|1|**2**|
|Composition|ACO_VUL|**1**|**2**|**3**|
|Guidance<br>documents|AGD_OPE|**1**|1|1|
|Guidance<br>documents|AGD_PRE|**1**|1|1|
|Life-cycle<br>support|ALC_CMC|**1**|1|1|
|Life-cycle<br>support|ALC_CMS|**2**|2|2|
|Life-cycle<br>support|ALC_DEL||||
|Life-cycle<br>support|ALC_DVS||||
|Life-cycle<br>support|ALC_FLR||||
|Life-cycle<br>support|ALC_LCD||||
|Life-cycle<br>support|ALC_TAT||||
|Security Target<br>evaluation|ASE_CCL|**1**|1|1|
|Security Target<br>evaluation|ASE_ECD|**1**|1|1|
|Security Target<br>evaluation|ASE_INT|**1**|1|1|
|Security Target<br>evaluation|ASE_OBJ|**1**|**2**|2|
|Security Target<br>evaluation|ASE_REQ|**1**|**2**|2|
|Security Target<br>evaluation|ASE_SPD||**1**|1|
|Security Target<br>evaluation|ASE_TSS|**1**|1|1|



**Table 26 Composition assurance level summary**


April 2017 Version 3.1 Page 247 of 247



