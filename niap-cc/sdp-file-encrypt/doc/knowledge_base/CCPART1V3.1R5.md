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


Page 2 of 106 Version 3.1 April 2017


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


April 2017 Version 3.1 Page 3 of 106


**Table of contents**

# **Table of Contents**


**1** **INTRODUCTION ............................................................................................. 11**


**2** **SCOPE ........................................................................................................... 13**


**3** **NORMATIVE REFERENCES ......................................................................... 14**


**4** **TERMS AND DEFINITIONS ........................................................................... 15**


**4.1** **Terms and definitions common in the CC ........................................................................................... 15**


**4.2** **Terms and definitions related to the ADV class .................................................................................. 21**


**4.3** **Terms and definitions related to the AGD class .................................................................................. 26**


**4.4** **Terms and definitions related to the ALC class .................................................................................. 26**


**4.5** **Terms and definitions related to the AVA class .................................................................................. 30**


**4.6** **Terms and definitions related to the ACO class .................................................................................. 30**


**5** **SYMBOLS AND ABBREVIATED TERMS ..................................................... 32**


**6** **OVERVIEW ..................................................................................................... 34**


**6.1** **The TOE ................................................................................................................................................. 34**
6.1.1 Different representations of the TOE ............................................................................................. 35
6.1.2 Different configurations of the TOE ............................................................................................... 35


**6.2** **Target audience of the CC ..................................................................................................................... 36**
6.2.1 Consumers ...................................................................................................................................... 36
6.2.2 Developers ...................................................................................................................................... 36
6.2.3 Evaluators ....................................................................................................................................... 36
6.2.4 Others ............................................................................................................................................. 36


**6.3** **The different parts of the CC ................................................................................................................ 37**


**6.4** **Evaluation context .................................................................................................................................. 38**


**7** **GENERAL MODEL ......................................................................................... 40**


**7.1** **Assets and countermeasures .................................................................................................................. 40**
7.1.1 Sufficiency of the countermeasures ................................................................................................ 42
7.1.2 Correctness of the TOE .................................................................................................................. 44
7.1.3 Correctness of the Operational Environment .................................................................................. 44


**7.2** **Evaluation ............................................................................................................................................... 45**


**8** **TAILORING SECURITY REQUIREMENTS ................................................... 47**


**8.1** **Operations .............................................................................................................................................. 47**
8.1.1 The iteration operation.................................................................................................................... 48


Page 4 of 106 Version 3.1 April 2017


**Table of contents**


8.1.2 The assignment operation ............................................................................................................... 48
8.1.3 The selection operation ................................................................................................................... 49
8.1.4 The refinement operation ................................................................................................................ 49


**8.2** **Dependencies between components ...................................................................................................... 50**


**8.3** **Extended components ............................................................................................................................ 51**


**9** **PROTECTION PROFILES AND PACKAGES ................................................ 52**


**9.1** **Introduction ............................................................................................................................................ 52**


**9.2** **Packages .................................................................................................................................................. 52**


**9.3** **Protection Profiles .................................................................................................................................. 52**


**9.4** **Using PPs and packages ......................................................................................................................... 55**


**9.5** **Using Multiple Protection Profiles ........................................................................................................ 56**


**9.6** **Protection Profiles, PP-Modules and PP-Configurations ................................................................... 56**
9.6.1 Introduction .................................................................................................................................... 56
9.6.2 PP-Modules .................................................................................................................................... 56
9.6.3 PP-Configurations........................................................................................................................... 57
9.6.4 Using PP-Modules and PP-Configurations in security targets ....................................................... 57


**10** **EVALUATION RESULTS ............................................................................... 59**


**10.1** **Introduction ....................................................................................................................................... 59**


**10.2** **Results of a PP evaluation ................................................................................................................. 60**


**10.3** **Results of a PP-Configuration evaluation ....................................................................................... 60**


**10.4** **Results of an ST/TOE evaluation ..................................................................................................... 60**


**10.5** **Conformance claim ........................................................................................................................... 61**


**10.6** **Use of ST/TOE evaluation results .................................................................................................... 62**


**A** **SPECIFICATION OF SECURITY TARGETS ................................................. 64**


**A.1** **Goal and structure of this Annex ..................................................................................................... 64**


**A.2** **Mandatory contents of an ST ........................................................................................................... 64**


**A.3** **Using an ST ........................................................................................................................................ 66**
A.3.1 How an ST should be used ............................................................................................................. 66
A.3.2 How an ST should not be used ....................................................................................................... 66


**A.4** **ST Introduction (ASE_INT) ............................................................................................................. 66**
A.4.1 ST reference and TOE reference .................................................................................................... 67
A.4.2 TOE overview ................................................................................................................................. 67
A.4.3 TOE description .............................................................................................................................. 69


**A.5** **Conformance claims (ASE_CCL) .................................................................................................... 70**


April 2017 Version 3.1 Page 5 of 106


**Table of contents**


**A.6** **Security problem definition (ASE_SPD) ......................................................................................... 70**
A.6.1 Introduction .................................................................................................................................... 70
A.6.2 Threats ............................................................................................................................................ 71
A.6.3 Organisational security policies (OSPs) ......................................................................................... 71
A.6.4 Assumptions ................................................................................................................................... 72


**A.7** **Security objectives (ASE_OBJ) ........................................................................................................ 73**
A.7.1 High-level solution ......................................................................................................................... 73
A.7.2 Part wise solutions .......................................................................................................................... 73
A.7.3 Relation between security objectives and the security problem definition ..................................... 74
A.7.4 Security objectives: conclusion ...................................................................................................... 76


**A.8** **Extended Components Definition (ASE_ECD)............................................................................... 76**


**A.9** **Security requirements (ASE_REQ) ................................................................................................. 77**
A.9.1 Security functional requirements (SFRs) ........................................................................................ 77
A.9.2 Security assurance requirements (SARs) ........................................................................................ 79
A.9.3 SARs and the security requirement rationale ................................................................................. 79
A.9.4 Security requirements: conclusion .................................................................................................. 80


**A.10** **TOE summary specification (ASE_TSS) ......................................................................................... 80**


**A.11** **Questions that may be answered with an ST .................................................................................. 81**


**A.12** **Low assurance Security Targets ...................................................................................................... 82**


**A.13** **Referring to other standards in an ST ............................................................................................. 83**


**B** **SPECIFICATION OF PROTECTION PROFILES ........................................... 85**


**B.1** **Goal and structure of this Annex ..................................................................................................... 85**


**B.2** **Mandatory contents of a PP ............................................................................................................. 85**


**B.3** **Using the PP ....................................................................................................................................... 86**
B.3.1 How a PP should be used ............................................................................................................... 86
B.3.2 How a PP should not be used ......................................................................................................... 87


**B.4** **PP introduction (APE_INT) ............................................................................................................. 87**
B.4.1 PP reference .................................................................................................................................... 87
B.4.2 TOE overview ................................................................................................................................ 88


**B.5** **Conformance claims (APE_CCL) .................................................................................................... 89**


**B.6** **Security problem definition (APE_SPD) ......................................................................................... 89**


**B.7** **Security objectives (APE_OBJ)........................................................................................................ 89**


**B.8** **Extended components definition (APE_ECD) ................................................................................ 89**


**B.9** **Security requirements (APE_REQ) ................................................................................................. 89**


**B.10** **TOE summary specification ............................................................................................................. 89**


**B.11** **Low assurance Protection Profiles ................................................................................................... 89**


**B.12** **Referring to other standards in a PP ............................................................................................... 90**


**B.13** **Interpretation of PP-Configuration as a standard PP ................................................................... 91**


Page 6 of 106 Version 3.1 April 2017


**Table of contents**


B.13.1 TOE type .................................................................................................................................... 91
B.13.2 Conformance claims .................................................................................................................. 91
B.13.3 Security problem definition ....................................................................................................... 91
B.13.4 Security objectives ..................................................................................................................... 91
B.13.5 Extended functional components definition ............................................................................... 91
B.13.6 Security functional requirements ............................................................................................... 92


**B.14** **Specification of PP-Modules ............................................................................................................. 92**
B.14.1 Mandatory content of a PP-Module ........................................................................................... 92
B.14.2 Using the PP-Module ................................................................................................................. 93
B.14.3 PP-Module introduction ............................................................................................................. 93
B.14.4 Consistency rationale ................................................................................................................. 94
B.14.5 Conformance claims .................................................................................................................. 95
B.14.6 Security problem definition ....................................................................................................... 95
B.14.7 Security objectives ..................................................................................................................... 96
B.14.8 Extended functional components definition ............................................................................... 97
B.14.9 Security functional requirements ............................................................................................... 97
B.14.10 Guidance for inclusion of elements from Base-PP .................................................................... 97


**B.15** **Specification of PP-Configurations .................................................................................................. 98**
B.15.1 Mandatory content of a PP-Configuration ................................................................................. 98
B.15.2 Using the PP-Configuration ....................................................................................................... 98
B.15.3 PP-Configuration reference........................................................................................................ 98
B.15.4 PP-Configuration components statement ................................................................................... 99
B.15.5 PP-Configuration conformance statement ................................................................................. 99
B.15.6 PP-Configuration SAR statement .............................................................................................. 99
B.15.7 Evaluation of a PP-Configuration .............................................................................................. 99


**C** **GUIDANCE FOR OPERATIONS .................................................................. 100**


**C.1** **Introduction ..................................................................................................................................... 100**


**C.2** **Examples of operations ................................................................................................................... 100**
C.2.1 The iteration operation .................................................................................................................. 100
C.2.2 The assignment operation ............................................................................................................. 100
C.2.3 The selection operation ................................................................................................................. 101
C.2.4 The refinement operation .............................................................................................................. 101


**C.3** **Organisation of components ........................................................................................................... 102**
C.3.1 Class ............................................................................................................................................. 102
C.3.2 Family ........................................................................................................................................... 102
C.3.3 Component.................................................................................................................................... 102
C.3.4 Element ......................................................................................................................................... 102


**C.4** **Extended components ..................................................................................................................... 103**
C.4.1 How to define extended components ............................................................................................ 103


**D** **PP CONFORMANCE ................................................................................... 104**


**D.1** **Introduction ..................................................................................................................................... 104**


**D.2** **Strict conformance .......................................................................................................................... 104**


**D.3** **Demonstrable conformance ............................................................................................................ 105**


**E** **BIBLIOGRAPHY .......................................................................................... 106**


April 2017 Version 3.1 Page 7 of 106


**Table of contents**


**E.1** **ISO/IEC standards and guidance .................................................................................................. 106**


**E.2** **Other standards and guidance ....................................................................................................... 106**


Page 8 of 106 Version 3.1 April 2017


**List of figures**

# **List of figures**


Figure 1 - Terminology in CM and in the product life-cycle ................................................ 30
Figure 2 - Security concepts and relationships ...................................................................... 41
Figure 3 - Evaluation concepts and relationships .................................................................. 42
Figure 4 - Evaluation results .................................................................................................. 59
Figure 5 - Security Target contents ....................................................................................... 65
Figure 6 - Tracings between security objectives and security problem definition ................ 75
Figure 7 - Relations between the security problem definition, the security objectives and the
security requirements ............................................................................................................. 80
Figure 8 - Contents of a Low Assurance Security Target ..................................................... 83
Figure 9 - Protection Profile contents .................................................................................... 86
Figure 10 - Contents of a Low Assurance Protection Profile ................................................ 90
Figure 11 - PP-Module content ............................................................................................. 92


April 2017 Version 3.1 Page 9 of 106


**List of tables**

# **List of tables**


Table 1 - Road map to the Common Criteria ........................................................................ 38


Page 10 of 106 Version 3.1 April 2017


**Introduction**

# **1 Introduction**


1 The CC permits comparability between the results of independent security
evaluations. The CC does so by providing a common set of requirements for
the security functionality of IT products and for assurance measures applied
to these IT products during a security evaluation. These IT products may be
implemented in hardware, firmware or software.


2 The evaluation process establishes a level of confidence that the security
functionality of these IT products and the assurance measures applied to
these IT products meet these requirements. The evaluation results may help
consumers to determine whether these IT products fulfil their security needs.


3 The CC is useful as a guide for the development, evaluation and/or
procurement of IT products with security functionality.


4 The CC is intentionally flexible, enabling a range of evaluation methods to
be applied to a range of security properties of a range of IT products.
Therefore users of the standard are cautioned to exercise care that this
flexibility is not misused. For example, using the CC in conjunction with
unsuitable evaluation methods, irrelevant security properties, or
inappropriate IT products, may result in meaningless evaluation results.


5 Consequently, the fact that an IT product has been evaluated has meaning
only in the context of the security properties that were evaluated and the
evaluation methods that were used. Evaluation authorities are advised to
carefully check the products, properties and methods to determine that an
evaluation will provide meaningful results. Additionally, purchasers of
evaluated products are advised to carefully consider this context to determine
whether the evaluated product is useful and applicable to their specific
situation and needs.


6 The CC addresses protection of assets from unauthorised disclosure,
modification, or loss of use. The categories of protection relating to these
three types of failure of security are commonly called confidentiality,
integrity, and availability, respectively. The CC may also be applicable to
aspects of IT security outside of these three. The CC is applicable to risks
arising from human activities (malicious or otherwise) and to risks arising
from non-human activities. Apart from IT security, the CC may be applied in
other areas of IT, but makes no claim of applicability in these areas.


7 Certain topics, because they involve specialised techniques or because they
are somewhat peripheral to IT security, are considered to be outside the
scope of the CC. Some of these are identified below.


a) The CC does not contain security evaluation criteria pertaining to
administrative security measures not related directly to the IT security
functionality. However, it is recognised that significant security can
often be achieved through or supported by administrative measures
such as organisational, personnel, physical, and procedural controls.


April 2017 Version 3.1 Page 11 of 106


**Introduction**


b) The evaluation of some technical physical aspects of IT security such
as electromagnetic emanation control is not specifically covered,
although many of the concepts addressed will be applicable to that
area.


c) The CC does not address the evaluation methodology under which
the criteria should be applied. This methodology is given in the CEM.


d) The CC does not address the administrative and legal framework
under which the criteria may be applied by evaluation authorities.
However, it is expected that the CC will be used for evaluation
purposes in the context of such a framework.


e) The procedures for use of evaluation results in accreditation are
outside the scope of the CC. Accreditation is the administrative
process whereby authority is granted for the operation of an IT
product (or collection thereof) in its full operational environment
including all of its non-IT parts. The results of the evaluation process
are an input to the accreditation process. However, as other
techniques are more appropriate for the assessments of non-IT related
properties and their relationship to the IT security parts, accreditors
should make separate provisions for those aspects.


f) The subject of criteria for the assessment of the inherent qualities of
cryptographic algorithms is not covered in the CC. Should
independent assessment of mathematical properties of cryptography
be required, the evaluation scheme under which the CC is applied
must make provision for such assessments.


8 ISO terminology, such as "can", "informative", "may", "normative", "shall"
and "should" used throughout the document are defined in the ISO/IEC
Directives, Part 2. Note that the term "should" has an additional meaning
applicable when using this standard. See the note below. The following
definition is given for the use of “should” in the CC.


9 **should**  within normative text, “should” indicates “that among several
possibilities one is recommended as particularly suitable, without mentioning
or excluding others, or that a certain course of action is preferred but not
necessarily required.” (ISO/IEC Directives, Part 2).


The CC interprets “not necessarily required” to mean that the choice of
another possibility requires a justification of why the preferred option was
not chosen.


Page 12 of 106 Version 3.1 April 2017


**Scope**

# **2 Scope**


10 This part of the CC establishes the general concepts and principles of IT
security evaluation and specifies the general model of evaluation given by
various parts of the standard which in its entirety is meant to be used as the
basis for evaluation of security properties of IT products.


11 Part one provides an overview of all parts of the CC standard. It describes the
various parts of the standard; defines the terms and abbreviations to be used
in all parts of the standard; establishes the core concept of a Target of
Evaluation (TOE); the evaluation context and describes the audience to
which the evaluation criteria are addressed. An introduction to the basic
security concepts necessary for evaluation of IT products is given.


12 It defines the various operations by which the functional and assurance
components given in CC Part 2 and CC Part 3 may be tailored through the
use of permitted operations.


13 The key concepts of protection profiles (PP), packages of security
requirements and the topic of conformance are specified and the
consequences of evaluation, evaluation results are described. This part of the
CC gives guidelines for the specification of Security Targets (ST) and
provides a description of the organization of components throughout the
model. General information about the evaluation methodology are given in
the CEM and the scope of evaluation schemes is provided.


April 2017 Version 3.1 Page 13 of 106


**Normative references**

# **3 Normative references**


14 The following referenced documents are indispensable for the application of
this CC part 1. For dated references, only the edition cited applies. For
undated references, the latest edition of the referenced document (including
any amendments) applies.


[CC-2] Common Criteria for Information Technology
Security Evaluation, Version 3.1, revision 5, April
2017. Part 2: Functional security components.


[CC-3] Common Criteria for Information Technology
Security Evaluation, Version 3.1, revision 5, April
2017. Part 3: Assurance security components.


[CEM] Common Methodology for Information Technology
Security Evaluation, Version 3.1, revision 5, April
2017.


Page 14 of 106 Version 3.1 April 2017


**Terms and definitions**

# **4 Terms and definitions**


15 For the purpose of the CC, the following terms and definitions apply.


16 This Chapter 4 contains only those terms which are used in a specialised way
throughout the CC. Some combinations of common terms used in the CC,
while not meriting inclusion in this Chapter 4, are explained for clarity in the
context where they are used.

## **4.1 Terms and definitions common in the CC**


17 **adverse actions**  actions performed by a threat agent on an asset


18 **assets**  entities that the owner of the TOE presumably places value upon


19 **assignment**  the specification of an identified parameter in a component
(of the CC) or requirement


20 **assurance**  grounds for confidence that a TOE meets the SFRs


21 **attack potential**  measure of the effort to be expended in attacking a TOE,
expressed in terms of an attacker's expertise, resources and motivation


22 **augmentation**  addition of one or more requirement(s) to a package


23 **authentication data**  information used to verify the claimed identity of a
user


24 **authorised user**  TOE user who may, in accordance with the SFRs,
perform an operation


25 **Base Protection Profile**  Protection Profile used as a basis to build a
Protection Profile Configuration


26 **class**  set of CC families that share a common focus


27 **coherent**  logically ordered and having discernible meaning


For documentation, this addresses both the actual text and the structure of the
document, in terms of whether it is understandable by its target audience.


28 **complete**  property where all necessary parts of an entity have been
provided


In terms of documentation, this means that all relevant information is
covered in the documentation, at such a level of detail that no further
explanation is required at that level of abstraction.


29 **component**  smallest selectable set of elements on which requirements
may be based


April 2017 Version 3.1 Page 15 of 106


**Terms and definitions**


30 **composed assurance package**  assurance package consisting of
requirements drawn from CC Part 3 (predominately from the ACO class),
representing a point on the CC predefined composition assurance scale


31 **confirm**  declare that something has been reviewed in detail with an
independent determination of sufficiency


The level of rigour required depends on the nature of the subject matter. This
term is only applied to evaluator actions.


32 **connectivity**  property of the TOE allowing interaction with IT entities
external to the TOE


This includes exchange of data by wire or by wireless means, over any
distance in any environment or configuration.


33 **consistent**  relationship between two or more entities such that there are
no apparent contradictions between these entities


34 **counter, verb**  meet an attack where the impact of a particular threat is
mitigated but not necessarily eradicated


35 **demonstrable conformance**  relation between an ST and a PP, where the
ST provides a solution which solves the generic security problem in the PP


The PP and the ST may contain entirely different statements that discuss
different entities, use different concepts etc. Demonstrable conformance is
also suitable for a TOE type where several similar PPs already exist, thus
allowing the ST author to claim conformance to these PPs simultaneously,
thereby saving work.


36 **demonstrate**  provide a conclusion gained by an analysis which is less
rigorous than a “proof”


37 **dependency**  relationship between components such that if a requirement
based on the depending component is included in a PP, ST or package, a
requirement based on the component that is depended upon must normally
also be included in the PP, ST or package


38 **describe**  provide specific details of an entity


39 **determine**  affirm a particular conclusion based on independent analysis
with the objective of reaching a particular conclusion


The usage of this term implies a truly independent analysis, usually in the
absence of any previous analysis having been performed. Compare with the
terms “confirm” or “verify” which imply that an analysis has already been
performed which needs to be reviewed


40 **development environment**  environment in which the TOE is developed


Page 16 of 106 Version 3.1 April 2017


**Terms and definitions**


41 **element**  indivisible statement of a security need


42 **ensure**  guarantee a strong causal relationship between an action and its
consequences


When this term is preceded by the word “help” it indicates that the
consequence is not fully certain, on the basis of that action alone.


43 **evaluation**  assessment of a PP, an ST or a TOE, against defined criteria


44 **evaluation assurance level**  set of assurance requirements drawn from CC
Part 3, representing a point on the CC predefined assurance scale, that form
an assurance package


45 **evaluation authority**  body that sets the standards and monitors the
quality of evaluations conducted by bodies within a specific community and
implements the CC for that community by means of an evaluation scheme


46 **evaluation scheme**  administrative and regulatory framework under which
the CC is applied by an evaluation authority within a specific community


47 **exhaustive**  characteristic of a methodical approach taken to perform an
analysis or activity according to an unambiguous plan


This term is used in the CC with respect to conducting an analysis or other
activity. It is related to “systematic” but is considerably stronger, in that it
indicates not only that a methodical approach has been taken to perform the
analysis or activity according to an unambiguous plan, but that the plan that
was followed is sufficient to ensure that all possible avenues have been
exercised.


48 **explain**  give argument accounting for the reason for taking a course of
action


This term differs from both “describe” and “demonstrate”. It is intended to
answer the question “Why?” without actually attempting to argue that the
course of action that was taken was necessarily optimal.


49 **extension**  addition to an ST or PP of functional requirements not
contained in CC Part 2 and/or assurance requirements not contained in CC
Part 3


50 **external entity**  human or IT entity possibly interacting with the TOE
from outside of the TOE boundary


51 **family**  set of components that share a similar goal but differ in emphasis
or rigour


52 **formal**  expressed in a restricted syntax language with defined semantics
based on well-established mathematical concepts


April 2017 Version 3.1 Page 17 of 106


**Terms and definitions**


53 **guidance documentation**  documentation that describes the delivery,
preparation, operation, management and/or use of the TOE


54 **identity**  representation uniquely identifying entities (e.g. a user, a process
or a disk) within the context of the TOE


An example of such a representation is a string. For a human user, the
representation can be the full or abbreviated name or a (still unique)
pseudonym.


55 **informal**  expressed in natural language


56 **inter TSF transfers**  communicating data between the TOE and the
security functionality of other trusted IT products


57 **internal communication channel**  communication channel between
separated parts of the TOE


58 **internal TOE transfer**  communicating data between separated parts of
the TOE


59 **internally consistent**  no apparent contradictions exist between any
aspects of an entity


In terms of documentation, this means that there can be no statements within
the documentation that can be taken to contradict each other.


60 **iteration**  use of the same component to express two or more distinct
requirements


61 **justification**  analysis leading to a conclusion


“Justification” is more rigorous than a demonstration. This term requires
significant rigour in terms of very carefully and thoroughly explaining every
step of a logical argument.


62 **object**  passive entity in the TOE, that contains or receives information,
and upon which subjects perform operations


63 **operation (on a component of the CC)**  modification or repetition of a
component


Allowed operations on components are assignment, iteration, refinement and
selection.


64 **operation (on an object)**  specific type of action performed by a subject
on an object


65 **operational environment**  environment in which the TOE is operated


Page 18 of 106 Version 3.1 April 2017


**Terms and definitions**


66 **organisational security policy**  set of security rules, procedures, or
guidelines for an organisation


A policy may pertain to a specific operational environment.


67 **package**  named set of either security functional or security assurance
requirements


An example of a package is “EAL 3”.


68 **Protection Profile Configuration**  Protection Profile composed of Base
Protection Profiles and Protection Profile Module


69 **Protection Profile evaluation**  assessment of a PP against defined criteria


70 **Protection Profile**  implementation-independent statement of security
needs for a TOE type


71 **Protection Profile Module**  implementation-independent statement of
security needs for a TOE type complementary to one or more Base
Protection Profiles


72 **prove**  show correspondence by formal analysis in its mathematical sense


It is completely rigorous in all ways. Typically, “prove” is used when there is
a desire to show correspondence between two TSF representations at a high
level of rigour.


73 **refinement**  addition of details to a component


74 **role**  predefined set of rules establishing the allowed interactions between
a user and the TOE


75 **secret**  information that must be known only to authorised users and/or the
TSF in order to enforce a specific SFP


76 **secure state**  state in which the TSF data are consistent and the TSF
continues correct enforcement of the SFRs


77 **security attribute**  property of subjects, users (including external IT
products), objects, information, sessions and/or resources that is used in
defining the SFRs and whose values are used in enforcing the SFRs


78 **security function policy**  set of rules describing specific security
behaviour enforced by the TSF and expressible as a set of SFRs


79 **security objective**  statement of an intent to counter identified threats
and/or satisfy identified organisation security policies and/or assumptions


80 **security problem**  statement which in a formal manner defines the nature
and scope of the security that the TOE is intended to address


April 2017 Version 3.1 Page 19 of 106


**Terms and definitions**


This statement consists of a combination of:


 threats to be countered by the TOE and its operational environment,


 the OSPs enforced by the TOE and its operational environment, and


 the assumptions that are upheld for the operational environment of
the TOE.


81 **security requirement**  requirement, stated in a standardised language,
which is meant to contribute to achieving the security objectives for a TOE


82 **Security Target**  implementation-dependent statement of security needs
for a specific identified TOE


83 **selection**  specification of one or more items from a list in a component


84 **semiformal**  expressed in a restricted syntax language with defined
semantics


85 **specify**  provide specific details about an entity in a rigorous and precise
manner


86 **strict conformance**  hierarchical relationship between a PP and an ST
where all the requirements in the PP also exist in the ST


This relation can be roughly defined as “the ST shall contain all statements
that are in the PP, but may contain more”. Strict conformance is expected to
be used for stringent requirements that are to be adhered to in a single
manner.


87 **ST evaluation**  assessment of an ST against defined criteria


88 **subject**  active entity in the TOE that performs operations on objects


89 **target of evaluation**  set of software, firmware and/or hardware possibly
accompanied by guidance


90 **threat agent**  entity that can adversely act on assets


91 **TOE evaluation**  assessment of a TOE against defined criteria


92 **TOE resource**  anything useable or consumable in the TOE


93 **TOE security functionality**  combined functionality of all hardware,
software, and firmware of a TOE that must be relied upon for the correct
enforcement of the SFRs


94 **trace, verb**  perform an informal correspondence analysis between two
entities with only a minimal level of rigour


Page 20 of 106 Version 3.1 April 2017


**Terms and definitions**


95 **transfers outside of the TOE**  TSF mediated communication of data to
entities not under the control of the TSF


96 **translation**  describes the process of describing security requirements in a
standardised language.


use of the term translation in this context is not literal and does not imply
that every SFR expressed in standardised language can also be translated
back to the security objectives.


97 **trusted channel**  a means by which a TSF and another trusted IT product
can communicate with necessary confidence


98 **trusted IT product**  IT product, other than the TOE, which has its
security functional requirements administratively coordinated with the TOE
and which is assumed to enforce its security functional requirements
correctly


An example of a trusted IT product would be one that has been separately
evaluated.


99 **trusted path**  means by which a user and a TSF can communicate with the
necessary confidence


100 **TSF data**  data for the operation of the TOE upon which the enforcement
of the SFR relies


101 **TSF interface**  means by which external entities (or subjects in the TOE
but outside of the TSF) supply data to the TSF, receive data from the TSF
and invoke services from the TSF


102 **user**  see external entity


103 **user data**  data for the user, that does not affect the operation of the TSF


104 **verify**  rigorously review in detail with an independent determination of
sufficiency


Also see “confirm”. This term has more rigorous connotations. The term
“verify” is used in the context of evaluator actions where an independent
effort is required of the evaluator.

## **4.2 Terms and definitions related to the ADV class**


105 The following terms are used in the requirements for software internal
structuring. Some of these are derived from the [IEEE Std 610.121990] _IEEE Std 610.12-1990, Standard glossary of software engineering_
_terminology, Institute of Electrical and Electronics Engineers_ .


106 **administrator**  entity that has a level of trust with respect to all policies
implemented by the TSF


April 2017 Version 3.1 Page 21 of 106


**Terms and definitions**


Not all PPs or STs assume the same level of trust for administrators.
Typically administrators are assumed to adhere at all times to the policies in
the ST of the TOE. Some of these policies may be related to the functionality
of the TOE, others may be related to the operational environment.


107 **call tree**  identifies the modules in a system in diagrammatic form
showing which modules call one another


Adapted from [IEEE Std 610.12-1990]


108 **cohesion**  module strength manner and degree to which the tasks
performed by a single software module are related to one another


[IEEE Std 610.12-1990]


Types of cohesion include coincidental, communicational, functional, logical,
sequential, and temporal. These types of cohesion are described by the
relevant term entry.


109 **coincidental cohesion**  module with the characteristic of performing
unrelated, or loosely related, activities


[IEEE Std 610.12-1990]


See “cohesion”.


110 **communicational cohesion**  module containing functions that produce
output for, or use output from, other functions within the module


[IEEE Std 610.12-1990]


111 See “cohesion”.


112 An example of a communicationally cohesive module is an access check
module that includes mandatory, discretionary, and capability checks.


113 **complexity**  measure of how difficult software is to understand, and thus
to analyse, test, and maintain


[IEEE Std 610.12-1990]


114 Reducing complexity is the ultimate goal for using modular decomposition,
layering and minimisation. Controlling coupling and cohesion contributes
significantly to this goal.


115 A good deal of effort in the software engineering field has been expended in
attempting to develop metrics to measure the complexity of source code.
Most of these metrics use easily computed properties of the source code,
such as the number of operators and operands, the complexity of the control
flow graph (cyclomatic complexity), the number of lines of source code, the
ratio of comments to executable code, and similar measures. Coding


Page 22 of 106 Version 3.1 April 2017


**Terms and definitions**


standards have been found to be a useful tool in generating code that is more
readily understood.


116 The TSF internals (ADV_INT) family calls for a complexity analysis in all
components. It is expected that the developer will provide support for the
claims that there has been a sufficient reduction in complexity. This support
could include the developer's programming standards, and an indication that
all modules meet the standard (or that there are some exceptions that are
justified by software engineering arguments). It could include the results of
tools used to measure some of the properties of the source code, or it could
include other support that the developer finds appropriate.


117 **coupling**  manner and degree of interdependence between software
modules


[IEEE Std 610.12-1990]


Types of coupling include call, common and content coupling. These are
characterised below:


118 **call coupling**  relationship between two modules


Examples of call coupling are data, stamp, and control:


119 **call coupling (data)**  relationship between two modules communicating
strictly through the use of call parameters that represent single data items.


See “call coupling”


120 **call coupling (stamp)**  relationship between two modules through the use
of call parameters that comprise multiple fields or that have meaningful
internal structures.


See “call coupling”


121 **call coupling (control)**  relationship between two modules if one passes
information that is intended to influence the internal logic of the other.


See “call coupling”


122 **common coupling**  relationship between two modules sharing a common
data area or other common system resource


123 Global variables indicate that modules using those global variables are
common coupled. Common coupling through global variables is generally
allowed, but only to a limited degree.


124 For example, variables that are placed into a global area, but are used by only
a single module, are inappropriately placed, and should be removed. Other
factors that need to be considered in assessing the suitability of global
variables are:


April 2017 Version 3.1 Page 23 of 106


**Terms and definitions**


a) The number of modules that modify a global variable: In general,
only a single module should be allocated the responsibility for
controlling the contents of a global variable, but there may be
situations in which a second module may share that responsibility; in
such a case, sufficient justification must be provided. It is
unacceptable for this responsibility to be shared by more than two
modules. (In making this assessment, care should be given to
determining the module actually responsible for the contents of the
variable; for example, if a single routine is used to modify the
variable, but that routine simply performs the modification requested
by its caller, it is the calling module that is responsible, and there may
be more than one such module). Further, as part of the complexity
determination, if two modules are responsible for the contents of a
global variable, there should be clear indications of how the
modifications are coordinated between them.


b) The number of modules that reference a global variable: Although
there is generally no limit on the number of modules that reference a
global variable, cases in which many modules make such a reference
should be examined for validity and necessity.


125 **content coupling**  relationship between two modules where one makes
direct reference to the internals of the other


Examples include modifying code of, or referencing labels internal to, the
other module. The result is that some or all of the content of one module are
effectively included in the other. Content coupling can be thought of as using
unadvertised module interfaces; this is in contrast to call coupling, which
uses only advertised module interfaces.


126 **domain separation**  security architecture property whereby the TSF
defines separate security domains for each user and for the TSF and ensures
that no user process can affect the contents of a security domain of another
user or of the TSF


127 **functional cohesion**  functional property of a module which performs
activities related to a single purpose


[IEEE Std 610.12-1990]


A functionally cohesive module transforms a single type of input into a
single type of output, such as a stack manager or a queue manager. See also
“cohesion”.


128 **interaction**  general communication-based activity between entities


129 **interface**  means of communication with an entity


130 **layering**  design technique where separate groups of modules (the layers)
are hierarchically organised to have separate responsibilities such that one


Page 24 of 106 Version 3.1 April 2017


**Terms and definitions**


layer depends only on layers below it in the hierarchy for services, and
provides its services only to the layers above it


Strict layering adds the constraint that each layer receives services only from
the layer immediately beneath it, and provides services only to the layer
immediately above it.


131 **logical cohesion**  procedural cohesion characteristics of a module
performing similar activities on different data structures


A module exhibits logical cohesion if its functions perform related, but
different, operations on different inputs. See also “cohesion”.


132 **modular decomposition**  process of breaking a system into components
to facilitate design, development and evaluation


[IEEE Std 610.12-1990]


133 **non-bypassability (of the TSF)**  security architecture property whereby
all SFR-related actions are mediated by the TSF


134 **procedural cohesion**  See “logical cohesion”


135 **security domains**  environments provided by the TSF for the use by
untrusted entities in such a way that these environments are isolated and
protected from each other


136 **sequential cohesion**  module containing functions each of whose output is
input for the following function in the module


[IEEE Std 610.12-1990]


An example of a sequentially cohesive module is one that contains the
functions to write audit records and to maintain a running count of the
accumulated number of audit violations of a specified type.


137 **software engineering**  application of a systematic, disciplined,
quantifiable approach to the development and maintenance of software; that
is, the application of engineering to software


[IEEE Std 610.12-1990]


As with engineering practices in general, some amount of judgement must be
used in applying engineering principles. Many factors affect choices, not just
the application of measures of modular decomposition, layering, and
minimisation. For example, a developer may design a system with future
applications in mind that will not be implemented initially. The developer
may choose to include some logic to handle these future applications without
fully implementing them; further, the developer may include some calls to
as-yet unimplemented modules, leaving call stubs. The developer's
justification for such deviations from well-structured programs will have to


April 2017 Version 3.1 Page 25 of 106


**Terms and definitions**


be assessed using judgement, as well as the application of good software
engineering discipline.


138 **temporal cohesion**  characteristics of a module containing functions that
need to be executed at about the same time


Adapted from [IEEE Std 610.12-1990]. Examples of temporally cohesive
modules include initialisation, recovery, and shutdown modules.


139 **TSF self-protection**  security architecture property whereby the TSF
cannot be corrupted by non-TSF code or entities

## **4.3 Terms and definitions related to the AGD class**


140 **installation**  procedure performed by a human user embedding the TOE in
its operational environment and putting it into an operational state


This operation is performed normally only once, after receipt and acceptance
of the TOE. The TOE is expected to be progressed to a configuration
allowed by the ST. If similar processes have to be performed by the
developer they are denoted as “generation” throughout ALC: Life-cycle
support. If the TOE requires an initial start-up that does not need to be
repeated regularly, this process would be classified as installation.


141 **operation**  usage phase of the TOE including “normal usage”,
administration and maintenance of the TOE after delivery and preparation


142 **preparation**  activity in the life-cycle phase of a product, comprising the
customer's acceptance of the delivered TOE and its installation which may
include such things as booting, initialisation, start-up and progressing the
TOE to a state ready for operation

## **4.4 Terms and definitions related to the ALC class**


143 **acceptance criteria**  criteria to be applied when performing the
acceptance procedures (e.g. successful document review, or successful
testing in the case of software, firmware or hardware)


144 **acceptance procedures**  procedures followed in order to accept newly
created or modified configuration items as part of the TOE, or to move them
to the next step of the life-cycle


145 These procedures identify the roles or individuals responsible for the
acceptance and the criteria to be applied in order to decide on the acceptance.


146 There are several types of acceptance situations some of which may overlap:


a) acceptance of an item into the configuration management system for
the first time, in particular inclusion of software, firmware and
hardware components from other manufacturers into the TOE
(“integration”);


Page 26 of 106 Version 3.1 April 2017


**Terms and definitions**


b) progression of configuration items to the next life-cycle phase at each
stage of the construction of the TOE (e.g. module, subsystem, quality
control of the finished TOE);


c) subsequent to transports of configuration items (for example parts of
the TOE or preliminary products) between different development
sites;


d) subsequent to the delivery of the TOE to the consumer.


147 **configuration** **management**  discipline applying technical and
administrative direction and surveillance to: identify and document the
functional and physical characteristics of a configuration item, control
changes to those characteristics, record and report change processing and
implementation status, and verify compliance with specified requirements.


[IEEE Std 610.12-1990]


148 **CM documentation**  all CM documentation including CM output, CM list
(configuration list), CM system records, CM plan and CM usage
documentation


149 **configuration management evidence**  everything that may be used to
establish confidence in the correct operation of the CM system


For example, CM output, rationales provided by the developer, observations,
experiments or interviews made by the evaluator during a site visit.


150 **configuration item**  object managed by the CM system during the TOE
development


These may be either parts of the TOE or objects related to the development
of the TOE like evaluation documents or development tools. CM items may
be stored in the CM system directly (for example files) or by reference (for
example hardware parts) together with their version.


151 **configuration list**  configuration management output document listing all
configuration items for a specific product together with the exact version of
each configuration management item relevant for a specific version of the
complete product


This list allows distinguishing the items belonging to the evaluated version
of the product from other versions of these items belonging to other versions
of the product. The final configuration management list is a specific
document for a specific version of a specific product. (Of course the list can
be an electronic document inside of a configuration management tool. In that
case it can be seen as a specific view into the system or a part of the system
rather than an output of the system. However, for the practical use in an
evaluation the configuration list will probably be delivered as a part of the
evaluation documentation.) The configuration list defines the items that are
under the configuration management requirements of ALC_CMC.


April 2017 Version 3.1 Page 27 of 106


**Terms and definitions**


152 **configuration management output**  results, related to configuration
management, produced or enforced by the configuration management system


These configuration management related results could occur as documents
(for example filled paper forms, configuration management system records,
logging data, hard-copies and electronic output data) as well as actions (for
example manual measures to fulfil configuration management instructions).
Examples of such configuration management outputs are configuration lists,
configuration management plans and/or behaviours during the product lifecycle.


153 **configuration management plan**  description of how the configuration
management system is used for the TOE


The objective of issuing a configuration management plan is that staff
members can see clearly what they have to do. From the point of view of the
overall configuration management system this can be seen as an output
document (because it may be produced as part of the application of the
configuration management system). From the point of view of the concrete
project it is a usage document because members of the project team use it in
order to understand the steps that they have to perform during the project.
The configuration management plan defines the usage of the system for the
specific product; the same system may be used to a different extent for other
products. That means the configuration management plan defines and
describes the output of the configuration management system of a company
which is used during the TOE development.


154 **configuration management system**  set of procedures and tools
(including their documentation) used by a developer to develop and maintain
configurations of his products during their life-cycles


Configuration management systems may have varying degrees of rigour and
function. At higher levels, configuration management systems may be
automated, with flaw remediation, change controls, and other tracking
mechanisms.


155 **configuration management system records**  output produced during the
operation of the configuration management system documenting important
configuration management activities


Examples of configuration management system records are configuration
management item change control forms or configuration management item
access approval forms.


156 **configuration management tools**  manually operated or automated tools
realising or supporting a configuration management system


For example tools for the version management of the parts of the TOE.


157 **configuration management usage documentation**  part of the
configuration management system, which describes, how the configuration


Page 28 of 106 Version 3.1 April 2017


**Terms and definitions**


management system is defined and applied by using for example handbooks,
regulations and/or documentation of tools and procedures


158 **delivery**  transmission of the finished TOE from the production
environment into the hands of the customer


This product life-cycle phase may include packaging and storage at the
development site, but does not include transportations of the unfinished TOE
or parts of the TOE between different developers or different development
sites.


159 **developer**  organisation responsible for the development of the TOE


160 **development**  product life-cycle phase which is concerned with generating
the implementation representation of the TOE


Throughout the ALC: Life-cycle support requirements, development and
related terms (developer, develop) are meant in the more general sense to
comprise development and production.


161 **development tools**  tools (including test software, if applicable)
supporting the development and production of the TOE


For example for a software TOE, development tools are usually
programming languages, compilers, linkers and generating tools.


162 **implementation representation**  least abstract representation of the TSF,
specifically the one that is used to create the TSF itself without further design
refinement


Source code that is then compiled or a hardware drawing that is used to build
the actual hardware are examples of parts of an implementation
representation.


163 **life-cycle**  sequence of stages of existence of an object (for example a
product or a system) in time


164 **life-cycle definition**  definition of the life-cycle model


165 **life cycle model**  description of the stages and their relations to each other
that are used in the management of the life-cycle of a certain object, how the
sequence of stages looks like and which high level characteristics the stages
have


166 **production**  production life-cycle phase follows the development phase
and consists of transforming the implementation representation into the
implementation of the TOE, i.e. into a state acceptable for delivery to the
customer


This phase may comprise manufacturing, integration, generation, internal
transports, storage, and labelling of the TOE.


April 2017 Version 3.1 Page 29 of 106


**Terms and definitions**


**Figure 1 - Terminology in CM and in the product life-cycle**

## **4.5 Terms and definitions related to the AVA class**


167 **covert channel**  enforced, illicit signalling channel that allows a user to
surreptitiously contravene the multi-level separation policy and
unobservability requirements of the TOE


168 **encountered potential vulnerabilities**  potential weakness in the TOE
identified by the evaluator while performing evaluation activities that could
be used to violate the SFRs


169 **exploitable vulnerability**  weakness in the TOE that can be used to
violate the SFRs in the operational environment for the TOE


170 **monitoring attacks**  generic category of attack methods that includes
passive analysis techniques aiming at disclosure of sensitive internal data of
the TOE by operating the TOE in the way that corresponds to the guidance
documents


171 **potential vulnerability**  suspected, but not confirmed, weakness


Suspicion is by virtue of a postulated attack path to violate the SFRs.


172 **residual vulnerability**  weakness that cannot be exploited in the
operational environment for the TOE, but that could be used to violate the
SFRs by an attacker with greater attack potential than is anticipated in the
operational environment for the TOE


173 **vulnerability**  weakness in the TOE that can be used to violate the SFRs
in some environment

## **4.6 Terms and definitions related to the ACO class**


174 **base component**  entity in a composed TOE, which has itself been the
subject of an evaluation, providing services and resources to a dependent
component


Page 30 of 106 Version 3.1 April 2017


**Terms and definitions**


175 **compatible (components)**  property of a component able to provide the
services required by the other component, through the corresponding
interfaces of each component, in consistent operational environments


176 **component TOE**  successfully evaluated TOE that is part of another
composed TOE


177 **composed TOE**  TOE comprised solely of two or more components that
have been successfully evaluated


178 **dependent component**  entity in a composed TOE, which is itself the
subject of an evaluation, relying on the provision on services by a base
component


179 **functional interface**  external interface providing a user with access to
functionality of the TOE which is not directly involved in enforcing security
functional requirements


In a composed TOE these are the interfaces provided by the base component
that are required by the dependent component to support the operation of the
composed TOE.


April 2017 Version 3.1 Page 31 of 106


**Symbols and abbreviated terms**

# **5 Symbols and abbreviated terms**


180 The following abbreviations are used in one or more parts of the CC:


**API** Application Programming Interface

**CAP** Composed Assurance Package

**CC** Common Criteria

**CCRA** Arrangement on the Recognition of Common Criteria
Certificates in the field of IT Security

**CM** Configuration Management

**DAC** Discretionary Access Control

**EAL** Evaluation Assurance Level

**GHz** Gigahertz

**GUI** Graphical User Interface

**IC** Integrated Circuit

**IOCTL** Input Output Control

**IP** Internet Protocol

**IT** Information Technology

**MB** Mega Byte

**OS** Operating System

**OSP** Organisational Security Policy

**PC** Personal Computer

**PCI** Peripheral Component Interconnect

**PKI** Public Key Infrastructure

**PP** Protection Profile

**RAM** Random Access Memory

**RPC** Remote Procedure Call

**SAR** Security Assurance Requirement

**SFR** Security Functional Requirement

**SFP** Security Function Policy

**SPD** Security Problem Definition

**ST** Security Target

**TCP** Transmission Control Protocol

**TOE** Target of Evaluation

**TSF** TOE Security Functionality

**TSFI** TSF Interface


Page 32 of 106 Version 3.1 April 2017


**Symbols and abbreviated terms**


**VPN** Virtual Private Network


April 2017 Version 3.1 Page 33 of 106


**Overview**

# **6 Overview**


181 This Chapter introduces the main concepts of the CC. It identifies the
concept “TOE”, the target audience of the CC, and the approach taken to
present the material in the remainder of the CC.

## **6.1 The TOE**


182 The CC is flexible in what to evaluate and is therefore not tied to the
boundaries of IT products as commonly understood. Therefore in the context
of evaluation, the CC uses the term “TOE” (Target of Evaluation).


183 A TOE is defined as a set of software, firmware and/or hardware possibly
accompanied by guidance.


184 While there are cases where a TOE consists of an IT product, this need not
be the case. The TOE may be an IT product, a part of an IT product, a set of
IT products, a unique technology that may never be made into a product, or a
combination of these.


185 As far as the CC is concerned, the precise relation between the TOE and any
IT products is only important in one aspect: the evaluation of a TOE
containing only part of an IT product should not be misrepresented as the
evaluation of the entire IT product.


186 Examples of TOEs include:


 A software application;


 An operating system;


 A software application in combination with an operating system;


 A software application in combination with an operating system and
a workstation;


 An operating system in combination with a workstation;


 A smart card integrated circuit;


 The cryptographic co-processor of a smart card integrated circuit;


 A Local Area Network including all terminals, servers, network
equipment and software;


 A database application excluding the remote client software normally
associated with that database application.


Page 34 of 106 Version 3.1 April 2017


**Overview**


**6.1.1** **Different representations of the TOE**


187 In the CC, a TOE can occur in several representations, such as (for a
software TOE):


 a list of files in a configuration management system;


 a single master copy, that has just been compiled;


 a box containing a CD-ROM and a manual, ready to be shipped to a
customer;


 an installed and operational version.


188 All of these are considered to be a TOE: and wherever the term “TOE” is
used in the remainder of the CC, the context determines the representation
that is meant.


**6.1.2** **Different configurations of the TOE**


189 In general, IT products can be configured in many ways: installed in different
ways, with different options enabled or disabled. As, during a CC evaluation,
it will be determined whether a TOE meets certain requirements, this
flexibility in configuration may lead to problems, as all possible
configurations of the TOE must meet the requirements. For these reasons, it
is often the case that the guidance part of the TOE strongly constrains the
possible configurations of the TOE. That is: the guidance of the TOE may be
different from the general guidance of the IT product.


190 An example is an operating system IT product. This product can be
configured in many ways (e.g. types of users, number of users, types of
external connections allowed/disallowed, options enabled/disabled etc.).


191 If the same IT product is to be a TOE, and is evaluated against a reasonable
set of requirements, the configuration should be much more tightly
controlled, as many options (e.g. allow all types of external connections or
the system administrator does not need to be authenticated) will lead to a
TOE not meeting the requirements.


192 For this reason, there would normally be a difference between the guidance
of the IT product (allowing many configurations) and the guidance of the
TOE (allowing only one or only configurations that do not differ in securityrelevant ways).


193 Note that if the guidance of the TOE still allows more than one configuration,
these configurations are collectively called “the TOE” and each such
configuration must meet the requirements levied on the TOE.


April 2017 Version 3.1 Page 35 of 106


**Overview**

## **6.2 Target audience of the CC**


194 There are three groups with a general interest in evaluation of the security
properties of TOEs: consumers, developers and evaluators. The criteria
presented in this CC part 1 have been structured to support the needs of all
three groups. They are all considered to be the principal users of the CC. The
three groups can benefit from the criteria as explained in the following
paragraphs.


**6.2.1** **Consumers**


195 The CC is written to ensure that evaluation fulfils the needs of the consumers
as this is the fundamental purpose and justification for the evaluation process.


196 Consumers can use the results of evaluations to help decide whether a TOE
fulfils their security needs. These security needs are typically identified as a
result of both risk analysis and policy direction. Consumers can also use the
evaluation results to compare different TOEs.


197 The CC gives consumers, especially in consumer groups and communities of
interest, an implementation-independent structure, termed the Protection
Profile (PP), in which to express their security requirements in an
unambiguous manner.


**6.2.2** **Developers**


198 The CC is intended to support developers in preparing for and assisting in
the evaluation of their TOEs and in identifying security requirements to be
satisfied by those TOEs. These requirements are contained in an
implementation-dependent construct termed the Security Target (ST). This
ST may be based on one or more PPs to show that the ST conforms to the
security requirements from consumers as laid down in those PPs.


199 The CC can then be used to determine the responsibilities and actions to
provide evidence that is necessary to support the evaluation of the TOE
against these requirements. It also defines the content and presentation of
that evidence.


**6.2.3** **Evaluators**


200 The CC contains criteria to be used by evaluators when forming judgements
about the conformance of TOEs to their security requirements. The CC
describes the set of general actions the evaluator is to carry out. Note that the
CC does not specify procedures to be followed in carrying out those actions.
More information on these procedures may be found in Section 6.4.


**6.2.4** **Others**


201 While the CC is oriented towards specification and evaluation of the IT
security properties of TOEs, it may also be useful as reference material to all
parties with an interest in or responsibility for IT security. Some of the


Page 36 of 106 Version 3.1 April 2017


**Overview**


additional interest groups that can benefit from information contained in the
CC are:


a) system custodians and system security officers responsible for
determining and meeting organisational IT security policies and
requirements;


b) auditors, both internal and external, responsible for assessing the
adequacy of the security of an IT solution (which may consist of or
contain a TOE);


c) security architects and designers responsible for the specification of
security properties of IT products;


d) accreditors responsible for accepting an IT solution for use within a
particular environment;


e) sponsors of evaluation responsible for requesting and supporting an
evaluation; and


f) evaluation authorities responsible for the management and oversight
of IT security evaluation programmes.

## **6.3 The different parts of the CC**


202 The CC is presented as a set of distinct but related parts as identified below.
Terms used in the description of the parts are explained in Chapter 7.


a) **Part 1, Introduction and general model** is the introduction to the
CC. It defines the general concepts and principles of IT security
evaluation and presents a general model of evaluation.


b) **Part 2, Security functional components** establishes a set of
functional components that serve as standard templates upon which to
base functional requirements for TOEs. CC Part 2 catalogues the set
of functional components and organises them in families and classes.


c) **Part 3, Security assurance components** establishes a set of
assurance components that serve as standard templates upon which to
base assurance requirements for TOEs. CC Part 3 catalogues the set
of assurance components and organises them into families and classes.
CC Part 3 also defines evaluation criteria for PPs and STs and
presents seven pre-defined assurance packages which are called the
Evaluation Assurance Levels (EALs).


203 In support of the three parts of the CC listed above, other documents have
been published, the CEM provides the methodology for IT security
evaluation using the CC as a basis. It is anticipated that other documents will
be published, including technical rationale material and guidance documents.


204 The following table presents, for the three key target audience groupings,
how the parts of the CC will be of interest.


April 2017 Version 3.1 Page 37 of 106


**Overview**










|Col1|Consumers|Developers|Evaluators|
|---|---|---|---|
|Part<br>1|Use for background<br>information and are<br>obliged to use for<br>reference purposes.<br>Guidance structure<br>for PPs.|Use for background<br>information and reference<br>purposes. Are obliged to<br>use for the development<br>of security specifications<br>for TOEs.|Are obliged to use<br>for reference<br>purposes and for<br>guidance in the<br>structure for PPs and<br>STs.|
|Part<br>2|Use for guidance and<br>reference when<br>formulating<br>statements of<br>requirements for a<br>TOE.|Are obliged to use for<br>reference when<br>interpreting statements of<br>functional requirements<br>and formulating<br>functional specifications<br>for TOEs.|Are obliged to use<br>for reference when<br>interpreting<br>statements of<br>functional<br>requirements.|
|Part<br>3|Use for guidance<br>when determining<br>required levels of<br>assurance.|Use for reference when<br>interpreting statements of<br>assurance requirements<br>and determining<br>assurance approaches of<br>TOEs.|Use for reference<br>when interpreting<br>statements of<br>assurance<br>requirements.|



**Table 1 - Road map to the Common Criteria**

## **6.4 Evaluation context**


205 In order to achieve greater comparability between evaluation results,
evaluations should be performed within the framework of an authoritative
evaluation scheme that sets the standards, monitors the quality of the
evaluations and administers the regulations to which the evaluation facilities
and evaluators must conform.


206 The CC does not state requirements for the regulatory framework. However,
consistency between the regulatory frameworks of different evaluation
authorities will be necessary to achieve the goal of mutual recognition of the
results of such evaluations.


207 A second way of achieving greater comparability between evaluation results
is using a common methodology to achieve these results. For the CC, this
methodology is given in the CEM.


208 Use of a common evaluation methodology contributes to the repeatability
and objectivity of the results but is not by itself sufficient. Many of the
evaluation criteria require the application of expert judgement and
background knowledge for which consistency is more difficult to achieve. In
order to enhance the consistency of the evaluation findings, the final
evaluation results may be submitted to a certification process.


209 The certification process is the independent inspection of the results of the
evaluation leading to the production of the final certificate or approval,
which is normally publicly available. The certification process is a means of
gaining greater consistency in the application of IT security criteria.


Page 38 of 106 Version 3.1 April 2017


**Overview**


210 The evaluation schemes and certification processes are the responsibility of
the evaluation authorities that run such schemes and processes and are
outside the scope of the CC.


April 2017 Version 3.1 Page 39 of 106


**General model**

# **7 General model**


211 This chapter presents the general concepts used throughout the CC, including
the context in which the concepts are to be used and the CC approach for
applying the concepts. CC Part 2 and CC Part 3, which are obliged to be
consulted by users of the CC Part 1, expand on the use of these concepts and
assume that the approach described is used. Further, for users of the CC who
intend to perform evaluation activities the CEM is applicable. This chapter
assumes some knowledge of IT security and does not propose to act as a
tutorial in this area.


212 The CC discusses security using a set of security concepts and terminology.
An understanding of these concepts and the terminology is a prerequisite to
the effective use of the CC. However, the concepts themselves are quite
general and are not intended to restrict the class of IT security problems to
which the CC is applicable.

## **7.1 Assets and countermeasures**


213 Security is concerned with the protection of assets. Assets are entities that
someone places value upon. Examples of assets include:


 contents of a file or a server;


 the authenticity of votes cast in an election;


 the availability of an electronic commerce process;


 the ability to use an expensive printer;


 access to a classified facility.


but given that value is highly subjective, almost anything can be an asset.


214 The environment(s) in which these assets are located is called the operational
environment. Examples of (aspects of) operational environments are:


a) the computer room of a bank;


b) a computer network connected to the Internet;


c) a LAN;


d) a general office environment.


215 Many assets are in the form of information that is stored, processed and
transmitted by IT products to meet requirements laid down by owners of the
information. Information owners may require that availability, dissemination
and modification of any such information are strictly controlled and that the


Page 40 of 106 Version 3.1 April 2017


**General model**


assets are protected from threats by countermeasures. Figure 2 illustrates
these high level concepts and relationships.


**Figure 2 - Security concepts and relationships**


216 Safeguarding assets of interest is the responsibility of owners who place
value on those assets. Actual or presumed threat agents may also place value
on the assets and seek to abuse assets in a manner contrary to the interests of
the owner. Examples of threat agents include hackers, malicious users, nonmalicious users (who sometimes make errors), computer processes and
accidents.


217 The owners of the assets will perceive such threats as potential for
impairment of the assets such that the value of the assets to the owners would
be reduced. Security-specific impairment commonly includes, but is not
limited to: loss of asset confidentiality, loss of asset integrity and loss of
asset availability.


218 These threats therefore give rise to risks to the assets, based on the likelihood
of a threat being realised and the impact on the assets when that threat is
realised. Subsequently countermeasures are imposed to reduce the risks to
assets. These countermeasures may consist of IT countermeasures (such as
firewalls and smart cards) and non-IT countermeasures (such as guards and
procedures). See also ISO/IEC 27001 and ISO/IEC 27002 for a more general
discussion on security countermeasures (controls).


219 Owners of assets may be (held) responsible for those assets and therefore
should be able to defend the decision to accept the risks of exposing the
assets to the threats.


April 2017 Version 3.1 Page 41 of 106


**General model**


220 Two important elements in defending this decision are being able to
demonstrate that:


 the countermeasures are sufficient: if the countermeasures do what
they claim to do, the threats to the assets are countered;


 the countermeasures are correct: the countermeasures do what they
claim to do.


221 Many owners of assets lack the knowledge, expertise or resources necessary
to judge sufficiency and correctness of the countermeasures, and they may
not wish to rely solely on the assertions of the developers of the
countermeasures. These consumers may therefore choose to increase their
confidence in the sufficiency and correctness of some or all of their
countermeasures by ordering an evaluation of these countermeasures.


**Figure 3 - Evaluation concepts and relationships**


**7.1.1** **Sufficiency of the countermeasures**


222 In an evaluation, sufficiency of the countermeasures is analysed through a
construct called the Security Target. In this Section a simplified view on this
construct is provided: a more detailed and complete description may be
found in Annex A.


223 The Security Target begins with describing the assets and the threats to those
assets. The Security Target then describes the countermeasures (in the form
of Security Objectives) and demonstrates that these countermeasures are


Page 42 of 106 Version 3.1 April 2017


**General model**


sufficient to counter these threats: if the countermeasures do what they claim
to do, the threats are countered.


224 The Security Target then divides these countermeasures in two groups:


a) the security objectives for the TOE: these describe the
countermeasure(s) for which correctness will be determined in the
evaluation;


b) the security objectives for the Operational Environment: these
describe the countermeasures for which correctness will not be
determined in the evaluation.


225 The reasons for this division are:


 The CC is only suitable for assessing the correctness of IT
countermeasures. Therefore the non-IT countermeasures (e.g. human
security guards, procedures) are always in the Operational
Environment.


 Assessing correctness of countermeasures costs time and money,
possibly making it infeasible to assess the correctness of all IT
countermeasures.


 The correctness of some IT countermeasures may already have been
assessed in another evaluation. It is therefore not cost-effective to
assess this correctness again.


226 For the TOE (the IT countermeasures whose correctness will be assessed
during the evaluation), the Security Target requires a further detailing of the
security objectives for the TOE in Security Functional Requirements (SFRs).
These SFRs are formulated in a standardised language (described in CC Part
2) to ensure exactness and facilitate comparability.


227 In summary, the Security Target demonstrates that:


 The SFRs meet the security objectives for the TOE;


 The security objectives for the TOE and the security objectives for
the operational environment counter the threats;


 And therefore, the SFRs and the security objectives for the
operational environment counter the threats.


228 From this it follows that a correct TOE (meeting the SFRs) in combination
with a correct operational environment (meeting the security objectives for
the operational environment) will counter the threats. In the next two
sections correctness of the TOE and correctness of the operational
environment are discussed separately.


April 2017 Version 3.1 Page 43 of 106


**General model**


**7.1.2** **Correctness of the TOE**


229 A TOE may be incorrectly designed and implemented, and may therefore
contain errors that lead to vulnerabilities. By exploiting these vulnerabilities,
attackers may still damage and/or abuse the assets.


230 These vulnerabilities may arise from accidental errors made during
development, poor design, intentional addition of malicious code, poor
testing etc.


231 To determine correctness of the TOE, various activities can be performed
such as:


 testing the TOE;


 examining various design representations of the TOE;


 examining the physical security of the development environment of
the TOE.


232 The Security Target provides a structured description of these activities to
determine correctness in the form of Security Assurance Requirements
(SARs). These SARs are formulated in a standardised language (described in
CC Part 3) to ensure exactness and facilitate comparability.


233 If the SARs are met, there exists assurance in the correctness of the TOE and
the TOE is therefore less likely to contain vulnerabilities that can be
exploited by attackers. The amount of assurance that exists in the correctness
of the TOE is determined by the SARs themselves: a few “weak” SARs will
lead to a little assurance, a lot of “strong” SARs will lead to a lot of
assurance.


**7.1.3** **Correctness of the Operational Environment**


234 The operational environment may also be incorrectly designed and
implemented, and may therefore contain errors that lead to vulnerabilities.
By exploiting these vulnerabilities, attackers may still damage and/or abuse
the assets.


235 However, in the CC, no assurance is obtained regarding the correctness of
the operational environment. Or, in other words, the operational environment
is not evaluated (see the next Section).


236 As far as the evaluation is concerned, the operational environment is
assumed to be a 100% correct instantiation of the security objectives for the
operational environment.


237 This does not preclude a consumer of the TOE from using other methods to
determine the correctness of his operational environment, such as:


 If, for an OS TOE, the security objectives for the operational
environment state “The operational environment shall ensure that


Page 44 of 106 Version 3.1 April 2017


**General model**


entities from an untrusted network (e.g. the Internet) can only access
the TOE by ftp”, the consumer could select an evaluated firewall, and
configure it to only allow ftp access to the TOE;


 If the security objectives for the operational environment state “The
operational environment shall ensure that all administrative personnel
will not behave maliciously”, the consumer could adapt his contracts
with administrative personnel to include punitive sanctions for
malicious behaviour, but this determination is not part of a CC
evaluation.

## **7.2 Evaluation**


238 The CC recognises two types of evaluation: an ST/TOE evaluation, which is
described below, and an evaluation of PPs, which is defined in CC Part 3. In
many places, the CC uses the term evaluation (without qualifiers) to refer to
an ST/TOE evaluation.


239 In the CC an ST/TOE evaluation proceeds in two steps:


a) An ST evaluation: where the sufficiency of the TOE and the
operational environment are determined;


b) A TOE evaluation: where the correctness of the TOE is determined.
As said earlier, the TOE evaluation does not assess correctness of the
operational environment.


240 The ST evaluation is carried out by applying the Security Target evaluation
criteria (which are defined in CC Part 3) to the Security Target. The precise
method to apply the ASE criteria is determined by the evaluation
methodology that is used.


241 The TOE evaluation is more complex. The principal inputs to a TOE
evaluation are: the evaluation evidence, which includes the TOE and ST, but
will usually also include input from the development environment, such as
design documents or developer test results.


242 The TOE evaluation consists of applying the SARs (from the Security
Target) to the evaluation evidence. The precise method to apply a specific
SAR is determined by the evaluation methodology that is used.


243 How the results of applying the SARs are documented, and what reports
need to be generated and in what detail, is determined by both the evaluation
methodology that is used and the evaluation scheme under which the
evaluation is carried out.


244 The result of the TOE evaluation process is either:


 A statement that not all SARs have been met and that therefore there
is not the specified level of assurance that the TOE meets the SFRs as
stated in the ST;


April 2017 Version 3.1 Page 45 of 106


**General model**


 A statement that all SARs have been met, and that therefore there is
the specified level of assurance that the TOE meets the SFRs as
stated in the ST.


245 The TOE evaluation may be carried out after TOE development has finished,
or in parallel with TOE development.


246 The method of stating ST/TOE evaluation results is described in Chapter 10.
These results also identify the PP(s) and package(s) to which the TOE claims
conformance, and these constructs are described in the next Chapter.


Page 46 of 106 Version 3.1 April 2017


**Tailoring Security Requirements**

# **8 Tailoring Security Requirements**

## **8.1 Operations**


247 The CC functional and assurance components may be used exactly as
defined in CC Part 2 and CC Part 3, or they may be tailored through the use
of permitted operations. When using operations, the PP/ST author should be
careful that the dependency needs of other requirements that depend on this
requirement are satisfied. The permitted operations are selected from the
following set:


 Iteration: allows a component to be used more than once with varying
operations;


 Assignment: allows the specification of parameters;


 Selection: allows the specification of one or more items from a list;
and


 Refinement: allows the addition of details.


248 The assignment and selection operations are permitted only where
specifically indicated in a component. Iteration and refinement are permitted
for all components. The operations are described in more detail below.


249 The CC Part 2 Annexes provide the guidance on the valid completion of
selections and assignments. This guidance provides normative instructions
on how to complete operations, and those instructions shall be followed
unless the PP/ST author justifies the deviation:


a) “None” is only available as a choice for the completion of a selection
if explicitly provided.


The lists provided for the completion of selections must be nonempty. If a “None” option is chosen, no additional selection options
may be chosen. If “None” is not given as an option in a selection, it is
permissible to combine the choices in a selection with “and”s and
“or”s, unless the selection explicitly states “choose one of”.


Selection operations may be combined by iteration where needed. In
this case, the applicability of the option chosen for each iteration
should not overlap the subject of the other iterated selection, since
they are intended to be exclusive.


b) For the completion of assignments, the CC Part 2 Annexes shall be
consulted in order to determine when “None” would be a valid
completion.


April 2017 Version 3.1 Page 47 of 106


**Tailoring Security Requirements**


**8.1.1** **The iteration operation**


250 The iteration operation may be performed on every component. The PP/ST
author performs an iteration operation by including multiple requirements
based on the same component. Each iteration of a component shall be
different from all other iterations of that component, which is realised by
completing assignments and selections in a different way, or by applying
refinements to it in a different way.


251 Different iterations should be uniquely identified to allow clear rationales
and tracings to and from these requirements.


252 It is important to note that sometimes an iteration operation can be used with
components where could also be possible to perform an assignment
operation with a range or list of values instead of iterate them. In that case
the author can select the most appropriate alternative, considering if there is a
necessity of providing a whole rationale for the range of values or if it is
necessary to have a separate one for each of them. The author should also
keep in mind if individual traces are required for those values.


**8.1.2** **The assignment operation**


253 An assignment operation occurs where a given component contains an
element with a parameter that may be set by the PP/ST author. The
parameter may be an unrestricted variable, or a rule that narrows the variable
to a specific range of values.


254 Whenever an element in a PP contains an assignment, a PP author shall do
one of four things:


a) leave the assignment uncompleted. The PP author could include
FIA_AFL.1.2 “When the defined number of unsuccessful
authentication attempts has been met or surpassed, the TSF shall

**[assignment: list of actions]** .” in the PP.


b) complete the assignment. As an example, the PP author could include
FIA_AFL.1.2 “When the defined number of unsuccessful
authentication attempts has been met or surpassed, the TSF shall
**prevent that external entity from binding to any subject in the**
**future** .” in the PP.


c) narrow the assignment, to further limit the range of values that is
allowed. As an example, the PP author could include FIA_AFL.1.1
“The TSF shall **detect when [assignment: positive integer between**
**4 and 9] unsuccessful authentication attempts occur ...** ” in the PP.


d) transform the assignment to a selection, thereby narrowing the
assignment. As an example, the PP author could include
FIA_AFL.1.2 “When the defined number of unsuccessful
authentication attempts has been met or surpassed, the TSF shall


Page 48 of 106 Version 3.1 April 2017


**Tailoring Security Requirements**


**[selection: prevent that user from binding to any subject in the**
**future, notify the administrator]** .” in the PP.


255 Whenever an element in an ST contains an assignment, an ST author shall
complete that assignment, as indicated in b) above. Options a), c) and d) are
not allowed for STs.


256 The values chosen in options b), c) and d) shall conform to the indicated type
required by the assignment.


257 When an assignment is to be completed with a set (e.g. subjects), one may
list a set of subjects, but also some description of the set from which the
elements of the set can be derived such as:


 all subjects


 all subjects of type X


 all subjects except subject a


 as long as it is clear which subjects are meant.


**8.1.3** **The selection operation**


258 The selection operation occurs where a given component contains an element
where a choice from several items has to be made by the PP/ST author.


259 Whenever an element in a PP contains a selection, the PP author may do one
of three things:


a) leave the selection uncompleted.


b) complete the selection by choosing one or more items.


c) restrict the selection by removing some of the choices, but leaving
two or more.


260 Whenever an element in an ST contains a selection, an ST author shall
complete that selection, as indicated in b) above. Options a) and c) are not
allowed for STs.


261 The item or items chosen in b) and c) shall be taken from the items provided
in the selection.


**8.1.4** **The refinement operation**


262 The refinement operation can be performed on every requirement. The
PP/ST author performs a refinement by altering that requirement. The first
rule for a refinement is that a TOE meeting the refined requirement also
meets the unrefined requirement in the context of the PP/ST (i.e. a refined
requirement must be “stricter” than the original requirement). If a refinement


April 2017 Version 3.1 Page 49 of 106


**Tailoring Security Requirements**


does not meet this rule, the resulting refined requirement is considered to be
an extended requirement and shall be treated as such.


263 The first rule for a refinement is that a TOE meeting the refined requirement
also meets the unrefined requirement in the context of the PP/ST (i.e. a
refined requirement must be “stricter” than the original requirement)


264 The only exception to this rule is that a PP/ST author is allowed to refine a
SFR to apply to some but not all subjects, objects, operations, security
attributes and/or external entities.


265 However, this exception does not apply to refining SFRs that are taken from
PPs that compliance is being claimed to; these SFRs may not be refined to
apply to fewer subjects, objects, operations, security attributes and/or
external entities than the SFR in the PP.


266 The second rule for a refinement is that the refinement shall be related to the
original component.


267 A special case of refinement is an editorial refinement, where a small change
is made in a requirement, i.e. rephrasing a sentence due to adherence to
proper English grammar, or to make it more understandable to the reader.
This change is not allowed to modify the meaning of the requirement in any
way.

## **8.2 Dependencies between components**


268 Dependencies may exist between components. Dependencies arise when a
component is not self sufficient and relies upon the presence of another
component to provide security functionality or assurance.


269 The functional components in CC Part 2 typically have dependencies on
other functional components as do some of the assurance components in CC
Part 3 which may have dependencies on other CC Part 3 components. CC
Part 2 dependencies on CC Part 3 components may also be defined. However,
this does not preclude extended functional components having dependencies
on assurance components or vice versa.


270 Component dependency descriptions are determined by consulting the CC
Part 2 and CC Part 3 component definitions. In order to ensure completeness
of the TOE security requirements, dependencies should be satisfied when
requirements based on components with dependencies are incorporated into
PPs and STs. Dependencies should also be considered when constructing
packages.


271 In other words: if component A has a dependency on component B, this
means that whenever a PP/ST contains a security requirement based on
component A, the PP/ST shall also contain one of :


a) a security requirement based on component B, or


Page 50 of 106 Version 3.1 April 2017


**Tailoring Security Requirements**


b) a security requirement based on a component that is hierarchically
higher than B, or


c) a justification why the PP/ST does not contain a security requirement
based on component B.


272 In cases a) and b), when a security requirement is included because of a
dependency, it may be necessary to complete operations (assignment,
iteration, refinement, selection) on that security requirement in a particular
manner to make sure that it actually satisfies the dependency.


273 In case c), the justification that a security requirement is not included should
address either:


 why the dependency is not necessary or useful, or


 that the dependency has been addressed by the operational
environment of the TOE, in which case the justification should
describe how the security objectives for the operational environment
address this dependency, or


 that the dependency has been addressed by the other SFRs in some
other manner (extended SFRs, combinations of SFRs etc.)

## **8.3 Extended components**


274 In the CC it is mandatory to base requirements on components from CC Part
2 or CC Part 3 with two exceptions:


a) there are security objectives for the TOE that can not be translated to
Part 2 SFRs, or there are third party requirements (e.g., laws,
standards) that can not be translated to Part 3 SARs (e.g. regarding
evaluation of cryptography);


b) a security objective can be translated, but only with great difficulty
and/or complexity based on components in CC Part 2 and/or CC Part
3.


275 In both cases the PP/ST author is required to define his own components.
These newly defined components are called extended components. A
precisely defined extended component is needed to provide context and
meaning to the extended SFRs and SARs based on that component.


276 After the new components have been defined correctly, the PP/ST author can
then base one or more SFRs or SARs on these newly defined extended
components and use them in the same way as the other SFRs and SARs.
From this point on, there is no further distinction between SARs and SFRs
based on the CC and SARs and SFRs based on extended components. Refer
to CC Part 3 Extended components definition (APE_ECD) and Extended
components definition (ASE_ECD) for further requirements on extended
components.


April 2017 Version 3.1 Page 51 of 106


**Protection Profiles and Packages**

# **9 Protection Profiles and Packages**

## **9.1 Introduction**


277 To allow consumer groups and communities of interest to express their
security needs, and to facilitate writing STs, this part of the CC provides two
special constructs: packages and Protection Profiles (PPs). In the following
two sections these constructs are described in more detail, followed by a
section on how these constructs can be used.

## **9.2 Packages**


278 A package is a named set of security requirements. A package is either


 a functional package, containing only SFRs, or


 an assurance package, containing only SARs.


279 Mixed packages containing both SFRs and SARs are not allowed.


280 A package can be defined by any party and is intended to be re-usable. To
this goal it should contain requirements that are useful and effective in
combination. Packages can be used in the construction of larger packages,
PPs and STs. At present there are no criteria for the evaluation of packages,
therefore any set of SFRs or SARs can be a package.


281 Examples of assurance packages are the evaluation assurance levels (EALs)
that are defined in CC Part 3. At the time of writing there are no functional
packages for this version of the CC.

## **9.3 Protection Profiles**


282 Whereas an ST always describes a specific TOE (e.g. the MinuteGap v18.5
Firewall), a PP is intended to describe a TOE type (e.g. firewalls). The same
PP may therefore be used as a template for many different STs to be used in
different evaluations. A detailed description of PPs is given in Annex B.


283 In general an ST describes requirements for a TOE and is written by the
developer of that TOE, while a PP describes the general requirements for a
TOE type, and is therefore typically written by:


 A user community seeking to come to a consensus on the
requirements for a given TOE type;


 A developer of a TOE, or a group of developers of similar TOEs
wishing to establish a minimum baseline for that type of TOE;


 A government or large corporation specifying its requirements as part
of its acquisition process.


Page 52 of 106 Version 3.1 April 2017


**Protection Profiles and Packages**


284 The PP determines the allowed type of conformance of the ST to the PP.
That is, the PP states (in the PP conformance statement, see section B.5)
what the allowed types of conformance for the ST are:


 if the PP states that strict conformance is required, the ST shall
conform to the PP in a strict manner;


 if the PP states that demonstrable conformance is required, the ST
shall conform to the PP in a strict or demonstrable manner.


285 Restating this in other words, an ST is only allowed to conform in a PP in a
demonstrable manner, if the PP explicitly allows this.


286 If an ST claims conformance to multiple PPs, it shall conform (as described
above) to each PP in the manner ordained by that PP. This may mean that the
ST conforms strictly to some PPs and demonstrably to other PPs.


287 Note that either the ST conforms to the PP in question or it does not. The CC
does not recognise “partial” conformance. It is therefore the responsibility of
the PP author to ensure the PP is not overly onerous, prohibiting PP/ST
authors in claiming conformance to the PP.


288 An ST is equivalent or more restrictive than a PP if:


 all TOEs that meet the ST also meet the PP, and


 all operational environments that meet the PP also meet the ST.


or, informally, the ST shall levy the same or more, restrictions on the TOE
and the same or less restrictions on the operational environment of the TOE.


289 This general statement can be made more specific for various sections of the
ST:


a) **Security problem definition** : The conformance rationale in the ST
shall demonstrate that the security problem definition in the ST is
equivalent (or more restrictive) than the security problem definition
in the PP. This means that:


 all TOEs that would meet the security problem definition in
the ST also meet the security problem definition in the PP;


 all operational environments that would meet the security
problem definition in the PP would also meet the security
problem definition in the ST.


b) **Security objectives** : The conformance rationale in the ST shall
demonstrate that the security objectives in the ST is equivalent (or
more restrictive) than the security objectives in the PP. This means
that:


April 2017 Version 3.1 Page 53 of 106


**Protection Profiles and Packages**


 all TOEs that would meet the security objectives for the TOE
in the ST also meet the security objectives for the TOE in the
PP;


 all operational environments that would meet the security
objectives for the operational environment in the PP would
also meet the security objectives for the operational
environment in the ST.


290 If strict conformance for protection profiles is specified then the following
requirements apply:


a) **Security problem definition** :


 The ST shall contain the security problem definition of the PP
and may specify additional threats and OSPs; it shall contain
all assumptions as defined in the PP, with two possible
exceptions as explained in the next two bullets;


 an assumption (or a part of an assumption) specified in the PP
may be omitted from the ST, if all security objectives for the
operational environment defined in the PP addressing this
assumption (or this part of an assumption) are replaced by
security objectives for the TOE in the ST;


 a new assumption may be added in the ST to the set of
assumptions defined in the PP, if this new assumption does
not mitigate a threat (or part of a threat) meant to be addressed
by security objectives for the TOE in the PP and if this
assumption doesn't fulfil an OSP (or a part of an OSP) meant
to be addressed by security objectives for the TOE in the PP;


b) **Security objectives** : The ST:


 shall contain all security objectives for the TOE of the PP but
may specify additional security objectives for the TOE;


 shall contain all security objectives for the operational
environment as defined in the PP with two exceptions as
explained in the next two bullet points;


 may specify that certain objectives for the operational
environment in the PP are security objectives for the TOE in
the ST. This is called re-assigning a security objective. If a
security objective is re-assigned to the TOE the security
objectives justification has to make clear which assumption or
part of the assumption may not be necessary any more;


 may specify additional objectives for the operational
environment, if these new objectives do not mitigate a threat
(or part of a threat) meant to be addressed by security


Page 54 of 106 Version 3.1 April 2017


**Protection Profiles and Packages**


objectives of the TOE in the PP and if these new objectives do
not fulfil an OSP (or a part of an OSP) meant to be addressed
by security objectives of the TOE in the PP


c) **Security requirements** : The ST shall contain all SFRs and SARs in
the PP, but may claim additional or hierarchically stronger SFRs and
SARs. The completion of operations in the ST must be consistent
with that in the PP; either the same completion will be used in the ST
as that in the PP or one that makes the requirement more restrictive
(the rules of refinement apply).


291 If demonstrable conformance for protection profiles is specified then the
following requirements apply:


 the ST shall contain a rationale on why the ST is considered to be
“equivalent or more restrictive” than the PP.


 Demonstrable conformance allows a PP author to describe a common
security problem to be solved and provide generic guidelines to the
requirements necessary for its resolution, in the knowledge that there
is likely to be more than one way of specifying a resolution.


292 PP evaluation is optional. Evaluation is performed by applying the APE
criteria to them as listed in CC Part 3. The goal of such an evaluation is to
demonstrate that the PP is complete, consistent, and technically sound and
suitable for use as a template on which to build another PP or an ST.


293 Basing a PP/ST on an evaluated PP has two advantages:


 There is much less risk that there are errors, ambiguities or gaps in
the PP. If any problems with a PP (that would have been caught by
evaluating that PP) are found during the writing or evaluation of the
new ST, significant time may elapse before the PP is corrected.


 Evaluation of the new PP/ST may often re-use evaluation results of
the evaluated PP, resulting in less effort for evaluating the new PP/ST.

## **9.4 Using PPs and packages**


294 If an ST claims to be conformant to one or more packages and/or Protection
Profiles, the evaluation of that ST will (among other properties of that ST)
demonstrate that the ST actually conforms to these packages and/or PPs that
they claim conformance to. Details of this determination of conformance can
be found in Annex A.


295 This allows the following process:


a) An organisation seeking to acquire a particular type of IT security
product develops their security needs into a PP, then has this
evaluated and publishes it;


April 2017 Version 3.1 Page 55 of 106


**Protection Profiles and Packages**


b) A developer takes this PP, writes an ST that claims conformance to
the PP and has this ST evaluated;


c) The developer then builds a TOE (or uses an existing one) and has
this evaluated against the ST.


296 The result is that the developer can prove that his TOE is conformant to the
security needs of the organisation: the organisation can therefore acquire that
TOE. A similar line of reasoning applies to packages.

## **9.5 Using Multiple Protection Profiles**


297 The CC also allows PPs to conform to other PPs, allowing chains of PPs to
be constructed, each based on the previous one(s).


298 For instance, one could take a PP for an Integrated Circuit and a PP for a
Smart Card OS, and use these to construct a Smart Card PP (IC and OS) that
claims conformance to the other two. One could then write a PP on Smart
Cards for Public Transport based on the Smart Card PP and a PP on Applet
Loading. Finally, a developer could then construct an ST based on this Smart
Cards for Public Transport PP.

## **9.6 Protection Profiles, PP-Modules and PP-Configurations**


**9.6.1** **Introduction**


299 To allow the definition of modular Protection Profiles that address optional
TOE's security features, this chapter introduces two constructs: PP-Modules
and PP-Configurations, as well as the way they can be used to evaluate
compliant products.


**9.6.2** **PP-Modules**


300 A PP-Module is a consistent set of elements (threats, assumptions,
organisational policies, objectives and security requirements) with a unique
reference.


301 Unlike Protection Profiles, PP-Modules address optional security features of
a given type of TOE that cannot be required uniformly for all products of this
kind.


302 Each PP-Module refers to at least one Base Protection Profile (or Base-PP)
that provides the definition of the TOE type and the mandatory requirements
to fulfill. The PP-Module specifies the modified TOE type, complements
these requirements and has to be used with the Base-PPs: a PP-Module may
introduce new elements to the Base-PPs and may also refine or interpret
some of the elements of the Base-PPs.


303 If the PP-Module refers to several Base Protection Profiles, this set of BasePPs have to be used simultaneously for the evaluation and usage of the PPModule.


Page 56 of 106 Version 3.1 April 2017


**Protection Profiles and Packages**


304 The PP-Module can also refer to alternative sets of Base-PPs, in the case the
PP-Module could comply with alternative Base-PPs depending of the usage.


305 The evaluation of a PP-Module alone is meaningless. A PP-Module has to be
evaluated as part of a PP-Configuration, at least with its mandatory Base-PPs.


**9.6.3** **PP-Configurations**


306 A PP-Configuration results from the combination of at least one PP-Module
with its Base-PPs, without any additional content: a PP-Configuration is
much like a Protection Profile that would include all the elements from the
Base-PPs and the PP-Modules.


307 A PP-Configuration can select more PPs than the Base-PPs of the PPModules, but at least all of the Base-PPs of the referred PP-Modules must be
included in the PP-Configuration.


308 If the PP-Module defines alternative sets of Base-PPs, only one of these sets
must be used in the PP-Configuration.


309 A PP-Configuration holds a unique reference and identifies all the PP
components: selected Base-PPs and selected PP-Modules.


310 A PP-Configuration can only combine certified Base-PPs to PP-Modules.


311 Evaluation rules for PP-Configurations are similar to the ones for standard
PPs. These rules are described in Class ACE, in CC Part 3.


**9.6.4** **Using PP-Modules and PP-Configurations in security targets**


312 PP-Modules are used to build specific PP-Configurations on top of one or
more Base-PPs. PP-Modules are used in Security Targets only as part of
well-identified PP-Configurations.


313 PP-Configurations are used like Protection Profiles. A Security Target can
claim conformity to a PP-Configuration provided this PP-Configuration has
been evaluated. Henceforth, the evaluation of the ST can rely on the results
of the PP-Configuration evaluation results as usual.


314 Note that the evaluation of a PP-Configuration can arise in two situations,
with no impact on the evaluation methodology:


 Independently of any product evaluation, or


 As the first step of the evaluation of a Security Target that claims
conformity with the PP-Configuration. Otherwise the conformance
claim is meaningless and the ST evaluation would fail in this aspect.


315 In practice, a ST that claims conformance with a non-certified PPConfiguration can still be evaluated with a conformance claim against the
Base-PP of the PP-Configuration; the elements of the ST that meet the PP

April 2017 Version 3.1 Page 57 of 106


**Protection Profiles and Packages**


Modules of the PP-Configuration would be evaluated as standard additions
to the Base-PP, proper to the TOE.


Page 58 of 106 Version 3.1 April 2017


**Evaluation results**

# **10 Evaluation results**

## **10.1 Introduction**


316 This chapter presents the expected results from PP and ST/TOE evaluations
performed according to the CEM.


317 PP evaluations lead to catalogues of evaluated PPs.


318 An ST evaluation leads to intermediate results that are used in the frame of a
TOE evaluation.


319 ST/TOE evaluations lead to catalogues of evaluated TOEs. In many cases
these catalogues will refer to the IT products that the TOEs are derived from
rather than the specific TOE. Therefore, the existence of an IT product in a
catalogue should not be construed as meaning that the whole IT product has
been evaluated; instead the actual extent of the ST/TOE evaluation is defined
by the ST. Refer to the bibliography for examples of such catalogues.


**Figure 4 - Evaluation results**


320 STs may be based on packages, evaluated PPs or non-evaluated PPs however this is not mandatory, as STs do not have to be based on anything at
all.


321 Evaluation should lead to objective and repeatable results that can be cited as
evidence, even if there is no absolute objective scale for representing the
results of a security evaluation. The existence of a set of evaluation criteria is
a necessary pre-condition for evaluation to lead to a meaningful result and
provides a technical basis for mutual recognition of evaluation results
between evaluation authorities.


April 2017 Version 3.1 Page 59 of 106


**Evaluation results**


322 An evaluation result represents the findings of a specific type of investigation
of the security properties of a TOE. Such a result does not automatically
guarantee fitness for use in any particular application environment. The
decision to accept a TOE for use in a specific application environment is
based on consideration of many security issues including the evaluation
findings.

## **10.2 Results of a PP evaluation**


323 CC Part 3 contains the evaluation criteria that an evaluator is obliged to
consult in order to state whether a PP is complete, consistent, and technically
sound and hence suitable for use in developing an ST.


324 The results of the evaluation shall also include a “Conformance Claim” (see
Section 10.5)).

## **10.3 Results of a PP-Configuration evaluation**


325 This chapter presents the expected results from PP-Configuration evaluation
and ST/TOE evaluations according to the Class ACE (Protection Profile
Configuration Evaluation) presented in CEM class ACE.


326 The evaluated PP-Configurations integrate the catalogue of evaluated PPs,
linked to the Base-PPs of the PP-Configurations.


327 STs may be based on packages, evaluated PPs or non-evaluated PPs,
evaluated PP-Configurations or non-evaluated PP-Configurations, or built-in
independently.


328 CC Part 3 ACE contains the evaluation criteria that an evaluator is obliged to
follow in order to state whether a PP-Configuration is complete, consistent,
and technically sound and hence suitable for use in developing an ST.


329 The results of the evaluation shall also include a "Conformance Claim" (see
Section 10.5).

## **10.4 Results of an ST/TOE evaluation**


330 CC Part 3 contains the evaluation criteria that an evaluator is obliged to
consult in order to determine whether sufficient assurance exists that the
TOE satisfies the SFRs in the ST. Evaluation of the TOE shall therefore
result in a pass/fail statement for the ST. If both the ST and the TOE
evaluation have resulted in a pass statement, the underlying product is
eligible for inclusion in a registry. The results of evaluation shall also include
a “Conformance Claim” as defined in the next section.


331 It may be the case that the evaluation results are subsequently used in a
certification process, but this certification process is outside the scope of the
CC.


Page 60 of 106 Version 3.1 April 2017


**Evaluation results**

## **10.5 Conformance claim**


332 The conformance claim indicates the source of the collection of requirements
that is met by a PP or ST that passes its evaluation. This conformance claim
contains a CC conformance claim that:


a) describes the version of the CC to which the PP or ST claims
conformance.


b) describes the conformance to CC Part 2 (security functional
requirements) as either:


 **CC Part 2 conformant**         - A PP or ST is CC Part 2 conformant
if all SFRs in that PP or ST are based only upon functional
components in CC Part 2, or


 **CC Part 2 extended**         - A PP or ST is CC Part 2 extended if at
least one SFR in that PP or ST is not based upon functional
components in CC Part 2.


c) describes the conformance to CC Part 3 (security assurance
requirements) as either:


 **CC Part 3 conformant**         - A PP or ST is CC Part 3 conformant
if all SARs in that PP or ST are based only upon assurance
components in CC Part 3, or


 **CC Part 3 extended**         - A PP or ST is CC Part 3 extended if at
least one SAR in that PP or ST is not based upon assurance
components in CC Part 3.


333 Additionally, the conformance claim may include a statement made with
respect to packages, in which case it consists of one of the following:


 _Package name Conformant_      - A PP or ST is conformant to a predefined package (e.g. EAL) if:


 the SFRs of that PP or ST are identical to the SFRs in the
package, or


 the SARs of that PP or ST are identical to the SARs in the
package.


 _Package name Augmented_      - A PP or ST is an augmentation of a
predefined package if:


 the SFRs of that PP or ST contain all SFRs in the package, but
have at least one additional SFR or one SFR that is
hierarchically higher than an SFR in the package.


April 2017 Version 3.1 Page 61 of 106


**Evaluation results**


 the SARs of that PP or ST contain all SARs in the package,
but have at least one additional SAR or one SAR that is
hierarchically higher than an SAR in the package.


334 Note that when a TOE is successfully evaluated to a given ST, any
conformance claims of the ST also hold for the TOE. A TOE can therefore
also be e.g. CC Part 2 conformant.


335 Finally, the conformance claim may also include two statements with respect
to Protection Profiles:


a) _PP Conformant_         - A PP or TOE meets specific PP(s), which are listed
as part of the conformance result.


b) _Conformance Statement_ (Only for PPs) - This statement describes the
manner in which PPs or STs must conform to this PP: strict or
demonstrable. For more information on this Conformance Statement,
see Annex B.


336 Besides the standard CC conformance claim regarding the version of the CC,
the CC Part 2 and Part 3, the SFR and SAR packages, and the standard PP
claim,


 a PP-Configuration has to provide a conformance statement
applicable to the conformant STs, either _strict_ or _demonstrable_, that
meet the conformance statements of the Base-PP(s),


 a ST may claim conformity with one or more PP-Configurations.

## **10.6 Use of ST/TOE evaluation results**


337 Once an ST and a TOE have been evaluated, asset owners can have the
assurance (as defined in the ST) that the TOE, together with the operational
environment, counters the threats. The evaluation results may be used by the
asset owner in deciding whether to accept the risk of exposing the assets to
the threats.


338 However, the asset owner should carefully check whether:


a) the Security Problem Definition in the ST matches the security
problem of the asset owner;


b) the Operational Environment of the asset owner conforms (or can be
made to conform) to the security objectives for the Operational
Environment described in the ST.


339 If either of these is not the case, the TOE may not be suitable for the
purposes of the asset owner.


340 Additionally, once an evaluated TOE is in operation, it is still possible that
previously unknown errors or vulnerabilities in the TOE may surface. In that
case, the developer may correct the TOE (to repair the vulnerabilities) or


Page 62 of 106 Version 3.1 April 2017


**Evaluation results**


change the ST to exclude the vulnerabilities from the scope of the evaluation.
In either case, the old evaluation results may no longer be valid.


341 If it is deemed necessary that confidence is regained, re-evaluation is needed.
The CC may be used for this re-evaluation, but detailed procedures for reevaluation are outside the scope of this part of the CC.


April 2017 Version 3.1 Page 63 of 106


**Specification of Security Targets**

# **A Specification of Security Targets** **(informative)**

## **A.1 Goal and structure of this Annex**


342 The goal of this annex is to explain the Security Target (ST) concept. This
annex does not define the ASE criteria; this definition can be found in CC
Part 3 and is supported by the documents given in the bibliography.


343 This annex consists of four major parts:


a) _What an ST must contain_ . This is summarised in Section A.2, and
described in more detail in Sections A.4 - A.10. These sections
describe the mandatory contents of the ST, the interrelationships
between these contents, and provide examples.


b) _How an ST should be used_ . This is summarised in Section A.3, and
described in more detail in section A.11. These sections describe how
an ST should be used, and some of the questions that can be
answered with an ST.


c) _Low Assurance STs_ . Low Assurance STs are STs with reduced
content. They are described in detail in section A.12.


d) _Claiming compliance with standards_ . Section A.13 describes how an
ST writer can claim that the TOE meets a particular standard.

## **A.2 Mandatory contents of an ST**


344 Figure 5 portrays the mandatory contents of an ST that are given in CC Part
3. Figure 5 may also be used as a structural outline of the ST, though
alternative structures are allowed. For instance, if the security requirements
rationale is particularly bulky, it could be included in an appendix of the ST
instead of in the security requirements section. The separate sections of an
ST and the contents of those sections are briefly summarised below and
explained in much more detail in sections A.4 to A.10. An ST normally
contains:


a) _an ST introduction_ containing three narrative descriptions of the TOE
on different levels of abstraction;


b) _a conformance claim_, showing whether the ST claims conformance
to any PPs and/or packages, and if so, to which PPs and/or packages;


c) _a security problem definition_, showing threats, OSPs and
assumptions;


Page 64 of 106 Version 3.1 April 2017


**Specification of Security Targets**


d) _security objectives_, showing how the solution to the security problem
is divided between security objectives for the TOE and security
objectives for the operational environment of the TOE;


e) _extended components definition_ (optional), where new components
(i.e. those not included in CC Part 2 or CC Part 3) may be defined.
These new components are needed to define extended functional and
extended assurance requirements;


f) _security requirements_, where a translation of the security objectives
for the TOE into a standardised language is provided. This
standardised language is in the form of SFRs. Additionally this
section defines the SARs;


g) _a TOE summary specification_, showing how the SFRs are
implemented in the TOE.


345 There also exists low assurance STs which have reduced contents; these are
described in detail in section A.12. All other parts of this Annex assume an
ST with full contents.


**Figure 5 - Security Target contents**


April 2017 Version 3.1 Page 65 of 106


**Specification of Security Targets**

## **A.3 Using an ST**


**A.3.1** **How an ST should be used**


346 A typical ST fulfils two roles:


 Before and during the evaluation, the ST specifies “what is to be
evaluated”. In this role, the ST serves as a basis for agreement
between the developer and the evaluator on the exact security
properties of the TOE and the exact scope of the evaluation.
Technical correctness and completeness are major issues for this role.
Section A.7 describes how the ST should be used in this role.


 After the evaluation, the ST specifies “what was evaluated”. In this
role, the ST serves as a basis for agreement between the developer or
re-seller of the TOE and the potential consumer of the TOE. The ST
describes the exact security properties of the TOE in an abstract
manner, and the potential consumer can rely on this description
because the TOE has been evaluated to meet the ST. Ease of use and
understandability are major issues for this role. Section A.11
describes how the ST should be used in this role.


**A.3.2** **How an ST should not be used**


347 Two roles (among many) that an ST should not fulfil are:


 _a detailed specification_ : An ST is designed to be a security
specification on a relatively high level of abstraction. An ST should,
in general, not contain detailed protocol specifications, detailed
descriptions of algorithms and/or mechanisms, long description of
detailed operations etc.


 _a complete specification_ : An ST is designed to be a security
specification and not a general specification. Unless security-relevant,
properties such as interoperability, physical size and weight, required
voltage etc. should not be part of an ST. This means that in general an
ST may be a part of a complete specification, but not a complete
specification itself.

## **A.4 ST Introduction (ASE_INT)**


348 The ST introduction describes the TOE in a narrative way on three levels of
abstraction:


a) the ST reference and the TOE reference, which provide identification
material for the ST and the TOE that the ST refers to;


b) the TOE overview, which briefly describes the TOE;


c) the TOE description, which describes the TOE in more detail.


Page 66 of 106 Version 3.1 April 2017


**Specification of Security Targets**


**A.4.1** **ST reference and TOE reference**


349 An ST contains a clear ST reference that identifies that particular ST. A
typical ST reference consists of title, version, authors and publication date.
An example of an ST reference is “MauveRAM Database ST, version 1.3,
MauveCorp Specification Team, 11 October 2002”.


350 An ST also contains a TOE reference that identifies the TOE that claims
conformance to the ST. A typical TOE reference consists of developer name,
TOE name and TOE version number. An example of a TOE reference is
“MauveCorp MauveRAM Database v2.11”. As a single TOE may be
evaluated multiple times, for instance by different consumers of that TOE,
and therefore have multiple STs, this reference is not necessarily unique.


351 If the TOE is constructed from one or more well-known products, it is
allowed to reflect this in the TOE reference, by referring to the product
name(s). However, this should not be used to mislead consumers: situations
where major parts or security functionalities were not considered in the
evaluation, yet the TOE reference does not reflect this are not allowed.


352 The ST reference and the TOE reference facilitate indexing and referencing
the ST and TOE and their inclusion in summaries of lists of evaluated
TOEs/Products.


**A.4.2** **TOE overview**


353 The TOE overview is aimed at potential consumers of a TOE who are
looking through lists of evaluated TOEs/Products to find TOEs that may
meet their security needs, and are supported by their hardware, software and
firmware. The typical length of a TOE overview is several paragraphs.


354 To this end, the TOE overview briefly describes the usage of the TOE and its
major security features, identifies the TOE type and identifies any major
non-TOE hardware/software/firmware required by the TOE.


**A.4.2.1** Usage and major security features of a TOE


355 The description of the usage and major security features of the TOE is
intended to give a very general idea of what the TOE is capable of in terms
of security, and what it can be used for in a security context. This section
should be written for (potential) TOE consumers, describing TOE usage and
major security features in terms of business operations, using language that
TOE consumers understand.


356 An example of this is “The MauveCorp MauveRAM Database v2.11 is a
multi-user database intended to be used in a networked environment. It
allows 1024 users to be active simultaneously. It allows password/token and
biometric authentication, protects against accidental data corruption, and can
roll-back ten thousand transactions. Its audit features are highly configurable,
so as to allow detailed audit to be performed for some users and transactions,
while protecting the privacy of other users and transactions.”


April 2017 Version 3.1 Page 67 of 106


**Specification of Security Targets**


**A.4.2.2** TOE type


357 The TOE overview identifies the general type of TOE, such as: firewall,
VPN-firewall, smart card, crypto-modem, intranet, web server, database,
web server and database, LAN, LAN with web server and database, etc.


358 It may be the case that the TOE is not of a readily available type, in which
case “none” would be acceptable.


359 In some cases, a TOE type can mislead consumers. Examples include:


 certain functionality can be expected of the TOE because of its TOE
type, but the TOE does not have this functionality. Examples include:


 an ATM-card type TOE, which does not support any
identification/authentication functionality;


 a firewall type TOE, which does not support protocols that are
almost universally used;


 a PKI-type TOE, which has no certificate revocation
functionality.


 the TOE can be expected to operate in certain operational
environments because of its TOE type, but it cannot do so. Examples
include:


 a PC-operating system type TOE, which is unable to function
securely unless the PC has no network connection, floppy
drive, and CD/DVD-player;


 a firewall, which is unable to function securely unless all
users that can connect through that firewall are benign.


**A.4.2.3** Required non-TOE hardware/software/firmware


360 While some TOEs do not rely upon other IT, many TOEs (notably software
TOEs) rely on additional, non-TOE, hardware, software and/or firmware. In
the latter case, the TOE overview is required to identify such non-TOE
hardware,software and/or firmware . A complete and fully detailed
identification of the additional hardware, software and/or firmware is not
necessary, but the identification should be complete and detailed enough for
potential consumers to determine the major hardware,software and/or
firmware needed to use the TOE.


361 Example hardware/software/firmware identifications are:


 a standard PC with a 1GHz or faster processor and 512MB or more
RAM, running version 3.0 Update 6b, c, or 7, or version 4.0 of the
Yaiza operating system;


Page 68 of 106 Version 3.1 April 2017


**Specification of Security Targets**


 a standard PC with a 1GHz or faster version processor and 512MB or
more RAM, running version 3.0 Update 6d of the Yaiza operating
system and the WonderMagic 1.0 Graphics card with the 1.0 WM
Driver Set;


 a standard PC with version 3.0 of the Yaiza OS (or higher);


 a CleverCard SB2067 integrated circuit;


 a CleverCard SB2067 integrated circuit running v2.0 of the QuickOS
smart card operating system;


 the December 2002 installation of the LAN of the Director-General's
Office of the Department of Traffic.


**A.4.3** **TOE description**


362 A TOE description is a narrative description of the TOE, likely to run to
several pages. The TOE description should provide evaluators and potential
consumers with a general understanding of the security capabilities of the
TOE, in more detail than was provided in the TOE overview. The TOE
description may also be used to describe the wider application context into
which the TOE will fit.


363 The TOE description discusses the physical scope of the TOE: a list of all
hardware, firmware, software and guidance parts that constitute the TOE.
This list should be described at a level of detail that is sufficient to give the
reader a general understanding of those parts.


364 The TOE description should also discuss the logical scope of the TOE: the
logical security features offered by the TOE at a level of detail that is
sufficient to give the reader a general understanding of those features. This
description is expected to be in more detail than the major security features
described in the TOE overview.


365 An important property of the physical and logical scopes is that they describe
the TOE in such a way that there remains no doubt on whether a certain part
or feature is in the TOE or whether this part or feature is outside the TOE.
This is especially important when the TOE is intertwined with and cannot be
easily separated from non-TOE entities.


366 Examples where the TOE is intertwined with non-TOE entities are:


 the TOE is a cryptographic co-processor of a smart card IC, instead
of the entire IC;


 the TOE is a smart card IC, except for the cryptographic processor;


 the TOE is the Network Address Translation part of the MinuteGap
Firewall v18.5.


April 2017 Version 3.1 Page 69 of 106


**Specification of Security Targets**

## **A.5 Conformance claims (ASE_CCL)**


367 This section of an ST describes how the ST conforms with:


 Part 2 and Part 3 of this International Standard;


 Protection Profiles (if any);


 Packages (if any).


368 The description of how the ST conforms to the CC consists of two items: the
version of the CC that is used and whether the ST contains extended security
requirements or not (see Section A.8).


369 The description of conformance of the ST to Protection Profiles means that
the ST lists the packages that conformance is being claimed to. For an
explanation of this, see Section 10.5.


370 The description of conformance of the ST to packages means that the ST lists
the packages that conformance is being claimed to. For an explanation of this,
see Section 10.5.


371 A Security Target can use PP-Configurations in the same way as standard
Protection Profiles. That is, the _Conformance claim_ of a ST can contain a _PP_
_claim_ that identifies the PP-Configurations the ST is conformant with.

## **A.6 Security problem definition (ASE_SPD)**


**A.6.1** **Introduction**


372 The security problem definition defines the security problem that is to be
addressed. The security problem definition is, as far as the CC is concerned,
axiomatic. That is, the process of deriving the security problem definition
falls outside the scope of the CC.


373 However, it should be noted that the usefulness of the results of an
evaluation strongly depends on the ST, and the usefulness of the ST strongly
depends on the quality of the security problem definition. It is therefore often
worthwhile to spend significant resources and use well-defined processes and
analyses to derive a good security problem definition.


374 Note that according to CC Part 3 it is not mandatory to have statements in all
sections, an ST with threats does not need to have OSPs and vice versa. Also,
any ST may omit assumptions.


375 Also note that where the TOE is physically distributed, it may be better to
discuss the relevant threats, OSPs and assumptions separately for distinct
domains of the TOE operational environment.


Page 70 of 106 Version 3.1 April 2017


**Specification of Security Targets**


**A.6.2** **Threats**


376 This section of the security problem definition shows the threats that are to
be countered by the TOE, its operational environment, or a combination of
the two.


377 A threat consists of an adverse action performed by a threat agent on an asset.


378 Adverse actions are actions performed by a threat agent on an asset. These
actions influence one or more properties of an asset from which that asset
derives its value.


379 Threat agents may be described as individual entities, but in some cases it
may be better to describe them as types of entities, groups of entities etc.


380 Examples of threat agents are hackers, users, computer processes, and
accidents. Threat agents may be further described by aspects such as
expertise, resources, opportunity and motivation.


381 Examples of threats are:


 a hacker (with substantial expertise, standard equipment, and being
paid to do so) remotely copying confidential files from a company
network;


 a worm seriously degrading the performance of a wide-area network;


 a system administrator violating user privacy;


 someone on the Internet listening in on confidential electronic
communication.


**A.6.3** **Organisational security policies (OSPs)**


382 This section of the security problem definition shows the OSPs that are to be
enforced by the TOE, its operational environment, or a combination of the
two.


383 OSPs are security rules, procedures, or guidelines imposed (or presumed to
be imposed) now and/or in the future by an actual or hypothetical
organisation in the operational environment. OSPs may be laid down by an
organisation controlling the operational environment of the TOE, or they
may be laid down by legislative or regulatory bodies. OSPs can apply to the
TOE and/or the operational environment of the TOE.


384 Examples of OSPs are:


 All products that are used by the Government must conform to the
National Standard for password generation and encryption;


April 2017 Version 3.1 Page 71 of 106


**Specification of Security Targets**


 Only users with System Administrator privilege and clearance of
Department Secret shall be allowed to manage the Department
Fileserver.


**A.6.4** **Assumptions**


385 This section of the security problem definition shows the assumptions that
are made on the operational environment in order to be able to provide
security functionality. If the TOE is placed in an operational environment
that does not meet these assumptions, the TOE may not be able to provide all
of its security functionality anymore. Assumptions can be on physical,
personnel and connectivity of the operational environment.


386 Examples of assumptions are:


 Assumptions on physical aspects of the operational environment:


 It is assumed that the TOE will be placed in a room that is
designed to minimise electromagnetic emanations;


 It is assumed that the administrator consoles of the TOE will
be placed in a restricted access area.


 Assumptions on personnel aspects of the operational environment:


 It is assumed that users of the TOE will be trained sufficiently
in order to operate the TOE;


 It is assumed that users of the TOE are approved for
information that is classified as National Secret;


 It is assumed that users of the TOE will not write down their
passwords.


 Assumptions on connectivity aspects of the operational environment:


 It is assumed that a PC workstation with at least 10GB of disk
space is available to run the TOE on;


 It is assumed that the TOE is the only non-OS application
running on this workstation;


 It is assumed that the TOE will not be connected to an
untrusted network.


387 Note that during the evaluation these assumptions are considered to be true:
they are not tested in any way. For these reasons, assumptions can only be
made on the operational environment. Assumptions can never be made on
the behaviour of the TOE because an evaluation consists of evaluating
assertions made about the TOE and not by assuming that assertions on the
TOE are true.


Page 72 of 106 Version 3.1 April 2017


**Specification of Security Targets**

## **A.7 Security objectives (ASE_OBJ)**


388 The security objectives are a concise and abstract statement of the intended
solution to the problem defined by the security problem definition. The role
of the security objectives is threefold:


 provide a high-level, natural language solution of the problem;


 divide this solution into two part wise solutions, that reflect that
different entities each have to address a part of the problem;


 demonstrate that these part wise solutions form a complete solution to
the problem.


**A.7.1** **High-level solution**


389 The security objectives consist of a set of short and clear statements without
overly much detail that together form a high-level solution to the security
problem. The level of abstraction of the security objectives aims at being
clear and understandable to knowledgeable potential consumers of the TOE.
The security objectives are in natural language.


**A.7.2** **Part wise solutions**


390 In an ST the high-level security solution, as described by the security
objectives, is divided into two part wise solutions. These part wise solutions
are called the security objectives for the TOE and the security objectives for
the operational environment. This reflects that these part wise solutions are
to be provided by two different entities: the TOE, and the operational
environment.


**A.7.2.1** Security objectives for the TOE


391 The TOE provides security functionality to solve a certain part of the
problem defined by the security problem definition. This part wise solution is
called the security objectives for the TOE and consists of a set of objectives
that the TOE should achieve in order to solve its part of the problem.


392 Examples of security objectives for the TOE are:


 The TOE shall keep confidential the content of all files transmitted
between it and a Server;


 The TOE shall identify and authenticate all users before allowing
them access to the Transmission Service provided by the TOE;


 The TOE shall restrict user access to data according to the Data
Access policy described in Annex 3 of the ST.


April 2017 Version 3.1 Page 73 of 106


**Specification of Security Targets**


393 If the TOE is physically distributed, it may be better to subdivide the ST
section containing the security objectives for the TOE into several subsections to reflect this.


**A.7.2.2** Security objectives for the operational environment


394 The operational environment of the TOE implements technical and
procedural measures to assist the TOE in correctly providing its security
functionality (which is defined by the security objectives for the TOE). This
part wise solution is called the security objectives for the operational
environment and consists of a set of statements describing the goals that the
operational environment should achieve.


395 Examples of security objectives for the operational environment are:


 The operational environment shall provide a workstation with the OS
Inux version 3.01b to execute the TOE on;


 The operational environment shall ensure that all human TOE users
receive appropriate training before allowing them to work with the
TOE;


 The operational environment of the TOE shall restrict physical access
to the TOE to administrative personnel and maintenance personnel
accompanied by administrative personnel;


 The operational environment shall ensure the confidentiality of the
audit logs generated by the TOE before sending them to the central
Audit Server.


396 If the operational environment of the TOE consists of multiple sites, each
with different properties, it may be better to subdivide the ST section
containing the security objectives for the operational environment into
several sub-sections to reflect this.


**A.7.3** **Relation between security objectives and the security problem**
**definition**


397 The ST also contains a security objectives rationale containing two sections:


 a tracing that shows which security objectives address which threats,
OSPs and assumptions;


 a set of justifications that shows that all threats, OSPs, and
assumptions are effectively addressed by the security objectives.


**A.7.3.1** Tracing between security objectives and the security problem
definition


398 The tracing shows how the security objectives trace back to the threats, OSPs
and assumptions as described in the security problem definition.


Page 74 of 106 Version 3.1 April 2017


**Specification of Security Targets**


a) _No spurious objectives_ : Each security objective traces to at least one
threat, OSP or assumption.


b) _Complete with respect to the security problem definition_ : Each threat,
OSP and assumption has at least one security objective tracing to it.


c) _Correct tracing_ : Since assumptions are always made by the TOE on
the operational environment, security objectives for the TOE do not
trace back to assumptions. The tracings allowed by CC Part 3 are
depicted in Figure 6.


**Figure 6 - Tracings between security objectives and security problem definition**


399 Multiple security objectives may trace to the same threat, indicating that the
combination of those security objectives counters that threat. A similar
argument holds for OSPs and assumptions.


**A.7.3.2** Providing a justification for the tracing


400 The security objectives rationale also demonstrates that the tracing is
effective: All the given threats, OSPs and assumption are addressed (i.e.
countered, enforced and upheld respectively) if all security objectives tracing
to a particular threat, OSP or assumption are achieved.


401 This demonstration analyses the effect of achieving the relevant security
objectives on countering the threats, enforcing the OSPs and upholding the
assumptions and leads to the conclusion that this is indeed the case.


402 In some cases, where parts of the security problem definition very closely
resemble some security objectives, the demonstration can be very simple. An
example is: a threat “T17: Threat agent X reads the Confidential Information
in transit between A and B”, a security objective for the TOE: “OT12: The
TOE shall ensure that all information transmitted between A and B is kept
confidential”, and a demonstration “T17 is directly countered by OT12”.


**A.7.3.3** On countering threats


403 Countering a threat does not necessarily mean removing that threat, it can
also mean sufficiently diminishing that threat or sufficiently mitigating that
threat.


404 Examples of removing a threat are:


April 2017 Version 3.1 Page 75 of 106


**Specification of Security Targets**


 removing the ability to execute the adverse action from the threat
agent;


 moving, changing or protecting the asset in such a way that the
adverse action is no longer applicable to it;


 removing the threat agent (e.g. removing machines from a network
that frequently crash that network).


405 Examples of diminishing a threat are:


 restricting the ability of a threat agent to perform adverse actions;


 restricting the opportunity to execute an adverse action of a threat
agent;


 reducing the likelihood of an executed adverse action being
successful;


 reducing the motivation to execute an adverse action of a threat agent
by deterrence;


 requiring greater expertise or greater resources from the threat agent.


406 Examples of mitigating the effects of a threat are:


 making frequent back-ups of the asset;


 obtaining spare copies of an asset;


 insuring an asset;


 ensuring that successful adverse actions are always timely detected,
so that appropriate action can be taken.


**A.7.4** **Security objectives: conclusion**


407 Based on the security objectives and the security objectives rationale, the
following conclusion can be drawn: if all security objectives are achieved
then the security problem as defined in Security problem definition
(ASE_SPD) is solved: all threats are countered, all OSPs are enforced, and
all assumptions are upheld.

## **A.8 Extended Components Definition (ASE_ECD)**


408 In many cases the security requirements (see the next section) in an ST are
based on components in CC Part 2 or CC Part 3. However, in some cases,
there may be requirements in an ST that are not based on components in CC
Part 2 or CC Part 3. In this case, new components (extended components)
must be defined, and this definition should be done in the Extended
Components Definition. For more information on this, see Annex C.4.


Page 76 of 106 Version 3.1 April 2017


**Specification of Security Targets**


409 Note that this section is intended to contain only the extended components
and not the extended requirements (requirements based on extended
components). The extended requirements should be included in the security
requirements (see the next section) and are for all purposes the same as
requirements based on components in CC Part 2 or CC Part 3.

## **A.9 Security requirements (ASE_REQ)**


410 The security requirements consist of two groups of requirements:


a) _the security functional requirements_ (SFRs): a translation of the
security objectives for the TOE into a standardised language;


b) _the security assurance requirements_ (SARs): a description of how
assurance is to be gained that the TOE meets the SFRs.


411 These two groups are discussed in the following two sections:


**A.9.1** **Security functional requirements (SFRs)**


412 The SFRs are a translation of the security objectives for the TOE. They are
usually at a more detailed level of abstraction, but they have to be a complete
translation (the security objectives must be completely addressed) and be
independent of any specific technical solution (implementation). The CC
requires this translation into a standardised language for several reasons:


 to provide an exact description of what is to be evaluated. As security
objectives for the TOE are usually formulated in natural language,
translation into a standardised language enforces a more exact
description of the functionality of the TOE.


 to allow comparison between two STs. As different ST authors may
use different terminology in describing their security objectives, the
standardised language enforces using the same terminology and
concepts. This allows easy comparison.


413 There is no translation required in the CC for the security objectives for the
operational environment, because the operational environment is not
evaluated and does therefore not require a description aimed at its evaluation.
See the bibliography for items relevant to the security assessment of
operational systems.


414 It may be the case that parts of the operational environment are evaluated in
another evaluation, but this is out of scope for the current evaluation. For
example: an OS TOE may require a firewall to be present in its operational
environment. Another evaluation may subsequently evaluate the firewall, but
this evaluation has nothing to do with the evaluation of the OS TOE.


**A.9.1.1** How the CC supports this translation


415 The CC supports this translation in three ways:


April 2017 Version 3.1 Page 77 of 106


**Specification of Security Targets**


a) by providing a predefined precise “language” designed to describe
exactly what is to be evaluated. This language is defined as a set of
components defined in CC Part 2. The use of this language as a welldefined translation of the security objectives for the TOE to SFRs is
mandatory, though some exceptions exist (see Section 8.3).


b) by providing operations: mechanisms that allow the ST writer to
modify the SFRs to provide a more accurate translation of the
security objectives for the TOE. This part of the CC defines the four
allowed operations: assignment, selection, iteration, and refinement.
These are described further in Section 8.1.


c) by providing dependencies: a mechanism that supports a more
complete translation to SFRs. In the CC Part 2 language, an SFR can
have a dependency on other SFRs. This signifies that if an ST uses
that SFR, it generally needs to use those other SFRs as well. This
makes it much harder for the ST writer to overlook including
necessary SFRs and thereby improves the completeness of the ST.
Dependencies are described further in Section 8.2.


**A.9.1.2** Relation between SFRs and security objectives


416 The ST also contains a security requirements rationale, consisting of two
sections about SFRs:


 a tracing that shows which SFRs address which security objectives
for the TOE;


 a set of justifications that shows that all security objectives for the
TOE are effectively addressed by the SFRs.


**A.9.1.2.1** Tracing between SFRs and the security objectives for the TOE


417 The tracing shows how the SFRs trace back to the security objectives for the
TOE as follows:


a) _No spurious SFRs_ : Each SFR traces back to at least one security
objective.


b) _Complete with respect to the security objectives for the TOE_ : Each
security objective for the TOE has at least one SFR tracing to it.


418 Multiple SFRs may trace to the same security objective for the TOE,
indicating that the combination of those security requirements meets that
security objective for the TOE.


**A.9.1.2.2** Providing a justification for the tracing


419 The security requirements rationale demonstrates that the tracing is effective:
if all SFRs tracing to a particular security objective for the TOE are satisfied,
that security objective for the TOE is achieved.


Page 78 of 106 Version 3.1 April 2017


**Specification of Security Targets**


420 This demonstration should analyse the effects of satisfying the relevant SFRs
on achieving the security objective for the TOE and lead to the conclusion
that this is indeed the case.


421 In cases where SFRs very closely resemble security objectives for the TOE,
the demonstration can be very simple.


**A.9.2** **Security assurance requirements (SARs)**


422 The SARs are a description of how the TOE is to be evaluated. This
description uses a standardised language for two reasons:


 to provide an exact description of how the TOE is to be evaluated.
Using a standardised language assists in creating an exact description
and avoids ambiguity.


 to allow comparison between two STs. As different ST authors may
use different terminology in describing the evaluation, the
standardised language enforces using the same terminology and
concepts. This allows easy comparison.


423 This standardised language is defined as a set of components defined in CC
Part 3. The use of this language is mandatory, though some exceptions exist.
The CC enhances this language in two ways:


a) by providing operations: mechanisms that allow the ST writer to
modify the SARs. The CC has four operations: assignment, selection,
iteration, and refinement. These are described further in Section 8.1.


b) by providing dependencies: a mechanism that supports a more
complete translation to SARs. In CC Part 3 language, an SAR can
have a dependency on other SARs. This signifies that if an ST uses
that SAR, it generally needs to use those other SARs as well. This
makes it much harder for the ST writer to overlook including
necessary SARs and thereby improves the completeness of STs.
Dependencies are described further in Section 8.2.


**A.9.3** **SARs and the security requirement rationale**


424 The ST also contains a security requirements rationale that explains why this
particular set of SARs was deemed appropriate. There are no specific
requirements for this explanation. The goal for this explanation is to allow
the readers of the ST to understand the reasons why this particular set was
chosen.


425 An example of an inconsistency is if the security problem description
mentions threats where the threat agent is very capable, and a low (or no)
Vulnerability analysis (AVA_VAN) is included in the SARs.


April 2017 Version 3.1 Page 79 of 106


**Specification of Security Targets**


**A.9.4** **Security requirements: conclusion**


426 In the security problem definition of the ST, the security problem is defined
as consisting of threats, OSPs and assumptions. In the security objectives
section of the ST, the solution is provided in the form of two sub-solutions:


 security objectives for the TOE;


 security objectives for the operational environment.


427 Additionally, a security objectives rationale is provided showing that if all
security objectives are achieved, the security problem is solved: all threats
are countered, all OSPs are enforced, and all assumptions are upheld.


**Figure 7 - Relations between the security problem definition, the security**

**objectives and the security requirements**


428 In the security requirements section of the ST, the security objectives for the
TOE are translated to SFRs and a security requirements rationale is provided
showing that if all SFRs are satisfied, all security objectives for the TOE are
achieved.


429 Additionally, a set of SARs is provided to show how the TOE is evaluated,
together with an explanation for selecting these SARs.


430 All of the above can be combined into the statement: If all SFRs and SARs
are satisfied and all security objectives for the operational environment are
achieved, then there exists assurance that the security problem as defined in
ASE_SPD is solved: all threats are countered, all OSPs are enforced, and all
assumptions are upheld. This is illustrated in Figure 7.


431 The amount of assurance obtained is defined by the SARs, and whether this
amount of assurance is sufficient is defined by the explanation for choosing
these SARs.

## **A.10 TOE summary specification (ASE_TSS)**


432 The objective for the TOE summary specification is to provide potential
consumers of the TOE with a description of how the TOE satisfies all the


Page 80 of 106 Version 3.1 April 2017


**Specification of Security Targets**


SFRs. The TOE summary specification should provide the general technical
mechanisms that the TOE uses for this purpose. The level of detail of this
description should be enough to enable potential consumers to understand
the general form and implementation of the TOE.


433 For instance if the TOE is an Internet PC and the SFRs contain FIA_UAU.1
to specify authentication, the TOE summary specification should indicate
how this authentication is done: password, token, iris scanning etc. More
information, like applicable standards that the TOE uses to meet SFRs, or
more detailed descriptions may also be provided.

## **A.11 Questions that may be answered with an ST**


434 After the evaluation, the ST specifies “what was evaluated”. In this role, the
ST serves as a basis for agreement between the developer or re-seller of the
TOE and the potential consumer of the TOE. The ST can therefore answer
the following questions (and more):


a) _How can I find the ST/TOE that I need given the multitude of existing_
_STs/TOEs?_ This question is addressed by the TOE overview, which
gives a brief (several paragraphs) summary of the TOE;


b) _Does this TOE fit in with my existing IT-infrastructure?_ This question
is addressed by the TOE overview, which identifies the major
hardware/firmware/software elements needed to run the TOE;


c) _Does this TOE fit in with my existing operational environment?_ This
question is addressed by the security objectives for the operational
environment, which identifies all constraints the TOE places on the
operational environment in order to function;


d) _What does the TOE do (interested reader)?_ This question is
addressed by the TOE overview, which gives a brief (several
paragraphs) summary of the TOE;


e) _What does the TOE do (potential consumer)?_ This question is
addressed by the TOE description, which gives a less brief (several
pages) summary of the TOE;


f) _What does the TOE do (technical)?_ This question is addressed by the
TOE summary specification which provides a high-level description
of the mechanisms the TOE uses;


g) _What does the TOE do (expert)?_ This question is addressed by the
SFRs which provide an abstract highly technical description, and the
TOE summary specification which provide additional detail;


h) _Does the TOE address the problem as defined by my_
_government/organisation?_ If your government/organisation has
defined packages and/or PPs to define this solution, then the answer


April 2017 Version 3.1 Page 81 of 106


**Specification of Security Targets**


can be found in the Conformance Claims section of the ST, which
lists all packages and PPs that the ST conforms to


i) _Does the TOE address my security problem (expert)?_ What are the
threats countered by the TOE? What organisational security policies
does it enforce? What assumptions does it make about the operational
environment? These questions are addressed by the security problem
definition;


j) _How much trust can I place in the TOE?_ This can be found in the
SARs in the security requirements section, which provide the
assurance level that was used to evaluate the TOE, and hence the trust
that the evaluation provides in the correctness of the TOE.

## **A.12 Low assurance Security Targets**


435 Writing an ST is not a trivial task, and may, especially in low assurance
evaluations, be a major part of the total effort expended by the developer and
the evaluator in the whole of the evaluation. For this reason, it is also
possible to write a low assurance ST.


436 The CC allows the use of a low assurance ST for an EAL 1 evaluation, but
not for EAL 2 and up. A low-assurance ST may only claim conformance to a
low-assurance PP (see Annex B). A regular ST (i.e., one with full contents)
may claim conformance with a low assurance PP.


437 A low assurance ST has a significantly reduced content compared to a
regular ST:


 there is no need to describe the security problem definition;


 there is no need to describe the security objectives for the TOE. The
security objectives for the operational environment must still be
described;


 there is no need to describe the security objectives rationale as there
is no security problem definition in the ST;


 the security requirements rationale only needs to justify (any)
dependencies not being satisfied as there are no security objectives
for the TOE in the ST.


438 All that remains are:


a) the references to TOE and ST;


b) a conformance claim;


c) the various narrative descriptions;


1. the TOE overview;


Page 82 of 106 Version 3.1 April 2017


**Specification of Security Targets**


2. the TOE description;


3. the TOE summary specification.


d) security objectives for the operational environment;


e) the SFRs and the SARs (including the extended components
definition) and the security requirements rationale (only if the
dependencies are not satisfied).


439 The reduced content of a low assurance ST is shown in Figure 8.


**Figure 8 - Contents of a Low Assurance Security Target**

## **A.13 Referring to other standards in an ST**


440 In some cases, an ST writer may wish to refer to an external standard, such
as a particular cryptographic standard or protocol. The CC allows three ways
of doing this:


a) As an organisational security policy (or part of it).


If, for example, there exists a government standard defining how
passwords have to be chosen, this may be stated as an organisational
security policy in an ST. This may lead to an objective for the
environment (e. g. if users of the TOE need to choose passwords
accordingly), or it may lead to security objectives for the TOE and
then to appropriate SFRs (likely of the FIA class), if the TOE


April 2017 Version 3.1 Page 83 of 106


**Specification of Security Targets**


generates passwords. In both cases the rationale of the developer
needs to make plausible that the security objectives for the TOE and
the SFRs are suitable to fulfil the OSP. The evaluator will examine if
this is in fact plausible (and may decide to look into the standard for
this), if the OSP is implemented by SFRs, as explained below.


b) As a technical standard (for example a cryptographic standard) used
in a refinement of an SFR.


In this case conformance to the standard is part of the fulfilment of
the SFR by the TOE and is treated as if the full text of the standard is
part of the SFR. Conformance is subsequently determined like any
other conformance to SFRs: during ADV: Development and ATE:
Tests it is analysed, by design analysis and tests, that the SFR is
completely and fully implemented in the TOE. If reference to only a
certain part of a standard is desired, that part should be
unambiguously stated in the SFR refinement.


c) As a technical standard (for example a cryptographic standard)
mentioned in the TOE summary specification.


The TOE summary specification is only considered as an explanation
of how the SFRs are realised, and is not strictly used as a strict
implementation requirement like the SFRs or the documents
delivered for ADV: Development. So the evaluator may detect an
inconsistency if the TSS references a technical standard and this is
not reflected in ADV: Development documentation, but there is no
routine activity to test fulfilment of the standard.


Page 84 of 106 Version 3.1 April 2017


**Specification of Protection Profiles**

# **B Specification of Protection Profiles** **(informative)**

## **B.1 Goal and structure of this Annex**


441 The goal of this Annex is to explain the Protection Profile (PP) concept. This
Annex does not define the APE criteria; this definition can be found in CC
Part 3 and is supported by the documents given in the bibliography.


442 As PPs and STs have a significant overlap, this Annex focuses on the
differences between PPs and STs. The material that is identical between STs
and PPs is described in Annex A.


443 This annex consists of four major parts:


a) _What a PP must contain_ . This is summarised in Section B.2, and
described in more detail in Sections B.4-B.9. These chapters describe
the mandatory contents of the PP, the interrelationships between
these contents, and provide examples.


b) _How a PP should be used_ . This is summarised in Section B.3.


c) _Low Assurance PPs_ . Low Assurance PPs are PPs with reduced
content. They are described in detail in Section B.11.


d) _Claiming compliance with standards_ . Section B.12 describes how a
PP writer can claim that the TOE is to meet a particular standard.

## **B.2 Mandatory contents of a PP**


444 Figure 9 portrays the mandatory content for a PP that is given in CC Part 3.
Figure 9 may also be used as a structural outline of the PP, though alternative
structures are allowed. For instance, if the security requirements rationale is
particularly bulky, it could be included in an appendix of the PP instead of in
the security requirements section. The separate sections of a PP and the
contents of those sections are briefly summarised below and explained in
much more detail in Sections B.4 - B.9. A PP contains:


a) a PP _introduction_ containing a narrative description of the TOE type;


b) a _conformance claim_, showing whether the PP claims conformance to
any PPs and/or packages, and if so, to which PPs and/or packages;


c) a _security problem definition_, showing threats, OSPs and
assumptions;


d) _security objectives_, showing how the solution to the security problem
is divided between security objectives for the TOE and security
objectives for the operational environment of the TOE;


April 2017 Version 3.1 Page 85 of 106


**Specification of Protection Profiles**


e) _extended components definition_, where new components (i.e. those
not included in CC Part 2 or CC Part 3) may be defined. These new
components are needed to define extended functional and extended
assurance requirements;


f) _security requirements_, where a translation of the security objectives
for the TOE into a standardised language is provided. This
standardised language is in the form of SFRs. Additionally this
section defines the SARs;


445 There also exist low assurance PPs, which have reduced contents; these are
described in detail in Section B.11. With this exception, all other parts of this
Annex assume a PP with full contents.


**Figure 9 - Protection Profile contents**

## **B.3 Using the PP**


**B.3.1** **How a PP should be used**


446 A PP is typically a statement of need where a user community, a regulatory
entity, or a group of developers define a common set of security needs. A PP
gives consumers a means of referring to this set, and facilitates future
evaluation against these needs.


447 A PP is therefore typically used as:


 part of a requirement specification for a specific consumer or group
of consumers, who will only consider buying a specific type of IT if
it meets the PP;


Page 86 of 106 Version 3.1 April 2017


**Specification of Protection Profiles**


 part of a regulation from a specific regulatory entity, who will only
allow a specific type of IT to be used if it meets the PP;


 a baseline defined by a group of IT developers, who then agree that
all IT that they produce of this type will meet this baseline.


though this does not preclude other uses.


**B.3.2** **How a PP should not be used**


448 Three roles (among many) that a PP should not fulfil are:


 _a detailed specification_ : A PP is designed to be a security
specification on a relatively high level of abstraction. A PP should, in
general, not contain detailed protocol specifications, detailed
descriptions of algorithms and/or mechanisms, long description of
detailed operations etc.


 _a complete specification_ : A PP is designed to be a security
specification and not a general specification. Unless security-relevant,
properties such as interoperability, physical size and weight, required
voltage etc. should not be part of a PP. This means that in general a
PP is a part of a complete specification, but not a complete
specification itself.


 _a specification of a single product_ : Unlike an ST, a PP is designed to
describe a certain type of IT, and not a single product. When only a
single product is described, it is better to use an ST for this purpose.

## **B.4 PP introduction (APE_INT)**


449 The PP introduction describes the TOE in a narrative way on two levels of
abstraction:


a) the PP reference, which provides identification material for the PP;


b) the TOE overview, which briefly describes the TOE.


**B.4.1** **PP reference**


450 A PP contains a clear PP reference that identifies that particular PP. A typical
PP reference consists of title, version, authors and publication date. An
example of a PP reference is “Atlantean Navy CablePhone Encryptor PP,
version 2b, Atlantean Navy Procurement Office, April 7, 2003”. The
reference must be unique so that it is possible to tell different PPs and
different versions of the same PP apart.


451 The PP reference facilitates indexing and referencing the PP and its inclusion
in lists of PPs.


April 2017 Version 3.1 Page 87 of 106


**Specification of Protection Profiles**


**B.4.2** **TOE overview**


452 The TOE overview is aimed at potential consumers of a TOE who are
looking through lists of evaluated products to find TOEs that may meet their
security needs, and are supported by their hardware, software and firmware.


453 The TOE overview is also aimed at developers who may use the PP in
designing TOEs or in adapting existing products.


454 The typical length of a TOE overview is several paragraphs.


455 To this end, the TOE overview briefly describes the usage of the TOE and its
major security features, identifies the TOE type and identifies any major
non-TOE hardware/software/firmware available to the TOE.


**B.4.2.1** Usage and major security features of a TOE


456 The description of the usage and major security features of the TOE is
intended to give a very general idea of what the TOE should be capable of,
and what it can be used for. This section should be written for (potential)
TOE consumers, describing TOE usage and major security features in terms
of business operations, using language that TOE consumers understand.


457 An example of this is “The Atlantean Navy CablePhone Encryptor is an
encryption device that should allow confidential communication between
ships across the Atlantean Navy CablePhone system. To this end it should
allow at least 32 different users and support at least 100 Mbps encryption
speed. It should allow both bilateral communication between ships and
broadcast across the entire network.”


**B.4.2.2** TOE Type


458 The TOE overview identifies the general type of TOE, such as: firewall,
VPN-firewall, smart card, crypto-modem, intranet, web server, database,
web server and database, LAN, LAN with web server and database, etc.


**B.4.2.3** Available non-TOE hardware/software/firmware


459 While some TOEs do not rely upon other IT, many TOEs (notably software
TOEs) rely on additional, non-TOE, hardware, software and/or firmware. In
the latter case, the TOE overview is required to identify the non-TOE
hardware/software/firmware.


460 As a Protection Profile is not written for a specific product, in many cases
only a general idea can be given of the available hardware/software/firmware.
In some other cases, e.g. a requirements specification for a specific consumer
where the platform is already known, (much) more specific information may
be provided.


461 Examples of hardware/software/firmware identifications are:


 None. (for a completely stand-alone TOE);


Page 88 of 106 Version 3.1 April 2017


**Specification of Protection Profiles**


 The Yaiza 3.0 Operating System running on a general PC;


 a CleverCard SB2067 integrated circuit;


 a CleverCard SB2067 IC running v2.0 of the QuickOS smart card
operating system;


 the December 2002 installation of the LAN of the Director-General's
Office of the Department of Traffic.

## **B.5 Conformance claims (APE_CCL)**


462 This section of a PP describes how the PP conforms with other PPs and with
packages. It is identical to the conformance claims section for an ST (see
Section A.5), with one exception: the conformance statement.


463 The conformance statement in the PP states how STs and/or other PPs must
conform to that PP. The PP author selects whether “strict” or “demonstrable”
conformance is required. See Annex D for more details on this.

## **B.6 Security problem definition (APE_SPD)**


464 This section is identical to the security problem definition section of an ST as
explained in Section A.6.

## **B.7 Security objectives (APE_OBJ)**


465 This section is identical to the security objectives section of an ST as
explained in Section A.7.

## **B.8 Extended components definition (APE_ECD)**


466 This section is identical to the extended components section of an ST as
explained in Section A.8.

## **B.9 Security requirements (APE_REQ)**


467 This section is identical to the security requirements section of an ST as
explained in Section A.9. Note however that the rules for completing
operations in a PP are slightly different from the rules for completing
operations in an ST. This is explained in more detail in Section 8.1.

## **B.10 TOE summary specification**


468 A PP has no TOE summary specification.

## **B.11 Low assurance Protection Profiles**


469 A low assurance PP has the same relationship to a regular PP (i.e., one with
full contents), as a low assurance ST has to a regular ST. This means that a
low-assurance PP consists of


April 2017 Version 3.1 Page 89 of 106


**Specification of Protection Profiles**


a) a PP introduction, consisting of a PP reference and a TOE overview;


b) a conformance claim;


c) security objectives for the operational environment;


d) the SFRs and the SARs (including the extended components
definition) and the security requirements rationale (only if the
dependencies are not satisfied).


470 A low-assurance PP may only claim conformance to a low-assurance PP (see
B.5). A regular PP may claim conformance with a low assurance PP.


471 The reduced content of a low assurance PP is shown in Figure 10.


**Figure 10 - Contents of a Low Assurance Protection Profile**

## **B.12 Referring to other standards in a PP**


472 This section is identical to the section on standards for STs as described in
Section A.13, with one exception: as a PP has no TOE summary
specification, the third option is not valid for PPs.


473 The PP author is reminded that referring to a standard in SFRs may impose a
significant burden on a developer developing a TOE to meet that PP
(depending on the size and complexity of the standard and the assurance
level required), and that it may be more suitable to require alternative (nonCC related) ways to assess conformance to that standard.


Page 90 of 106 Version 3.1 April 2017


**Specification of Protection Profiles**

## **B.13 Interpretation of PP-Configuration as a standard PP**


474 Once evaluated, a PP-Configuration can be refined and used in the same way
as a standard Protection Profile. This chapter explains how to combine the
content of the Base-PP(s) and PP-Module(s) of a PP-Configuration so as to
interpret it as a standard PP.


**B.13.1** **TOE type**


475 The TOE type of a PP to interpret in the same way as the PP-Configuration
would be constituted of the TOE type of the Base-PP(s) with the additions
introduced in the PP-Module(s) TOE types. The evaluation of the PPConfiguration ensures that it forms a consistent TOE type.


**B.13.2** **Conformance claims**


476 The Conformance claims of a PP to interpret in the same way as the PPConfiguration would contain:


 The conformance to the PP(s) whose conformance is claimed in the
Base-PP(s).


 The conformance to SAR packages (including predefined EAL) from
the Base-PPs. The issue of ANDed Base-PPs with different EALs has
to be dealt with like in an ST conformant to all those PPs (meaning
that the ST has to claim the level of the minimum EAL of all the
Base-PPs).


 The conformance statement (strict or demonstrable) from the BasePPs. The issue of ANDed Base-PPs with different conformance
statements has to be dealt with like in an ST conformant to all those
PPs.


**B.13.3** **Security problem definition**


477 The SPD of a PP to interpret in the same way as the PP-Configuration would
contain the union of the elements from the Base-PP(s) and PP-Module(s) of
the PP-Configuration.


**B.13.4** **Security objectives**


478 The security objectives of a PP to interpret in the same way as the PPConfiguration would contain the union of the security objectives from the
Base-PP(s) and PP-Module(s) of the PP-Configuration.


**B.13.5** **Extended functional components definition**


479 The extended functional components of a PP to interpret in the same way as
the PP-Configuration would contain all extended functional components
from the Base-PP(s) and PP-Module(s) of the PP-Configuration.


April 2017 Version 3.1 Page 91 of 106


**Specification of Protection Profiles**


**B.13.6** **Security functional requirements**


480 The set of SFRs of a PP to interpret in the same way as the PP-Configuration
would contain:


 all the SFRs from the PP-Module(s) of the PP-Configuration.


 all the SFRs from the Base-PP(s) except those which are refined in
the PP-Module(s).


481 The consistency analysis performed on PP-Configuration during evaluation
shall ensure this set is valid.

## **B.14 Specification of PP-Modules**


**B.14.1** **Mandatory content of a PP-Module**


482 Figure 11 shows the mandatory content of a PP-Module.


**Figure 11 - PP-Module content**


483 The content of the PP-Module is summarized below and explained in detail
in sections from B.14.3 to B.14.10. A PP-Module contains:


 an _Introduction_ that identifies the PP-Module, identifies the BasePP(s) and states the correspondence rationale, and provides a
description of the TOE within its environment that meets the
descriptions underlying the Base-PPs,


Page 92 of 106 Version 3.1 April 2017


**Specification of Protection Profiles**


 a _Consistency rationale_ that states the correspondence between the
Module and its Base-PP(s),


 a _Conformance claim_ regarding the CC, with inherited EAL and
conformance statement,


 a _Security problem definition_ with threats, assumptions and
organisational security policies,


 a _Security objectives_ section presenting the solution to the security
problem in terms of objectives for the TOE and its operational
environment,


 an optional _Extended functional components definition_ where new
functional components not included in CC Part 2 are introduced,


 a _Security functional requirements_ section with a standardized
statement of the TOE security objectives.


**B.14.2** **Using the PP-Module**


484 A PP-Module is a security statement of a group of users or developers,
regulators, administration, or any other entity that meets specific consumer
needs. A PP-Module complements one or more Base-PPs and allows
consumers to refer to this statement, facilitates the evaluation against it and
the comparison of conformant evaluated TOEs.


**B.14.3** **PP-Module introduction**


**B.14.3.1** PP-Module reference


485 The PP-Module introduction provides a clear and unambiguous reference
that allows identifying the PP-Module. A typical reference is made of the
title of the PP-Module, its version, their authors and the publication date.


486 The PP-Module reference will be used to index the document in Protection
Profiles databases.


**B.14.3.2** Base-PP identification


487 The PP-Module introduction identifies the Base Protection Profile(s) the
Module relies on. The identification consists of a list of PP references.


488 The PP-Module may require to be used with a set of Base-PPs
simultaneously, say {PP1,..., PPn}; the identification list states:


489 The PP-Module may allow the use with alternative sets of Base-PPs, say
{S1,..., Sk}; the identification list states:


April 2017 Version 3.1 Page 93 of 106


**Specification of Protection Profiles**


490 The general form of the Base-PP identification is then


491 Note that a PP-Module that states a list with an "OR" can be replaced by as
many PP-Modules as elements in the list. That is, the list with an "OR" is a
means to avoid managing similar PP-Modules for different usages, which
does not introduce any complexity to the security specification itself.


**B.14.3.3** TOE overview


492 The _TOE overview_ of the PP-Module may complete the TOE overviews of
the Base-PPs, provided the supplements do not contradict the Base-PPs:


 The _TOE type_ of the PP-Module can be the same of the Base-PPs or
introduce specificities that meet the purpose of the PP-Module.


 The PP-Module can introduce additional _usage and major security_
_features_ to those stated in the Base-PPs.


 The PP-Module can specify particular _non-TOE hardware, software_
_and/or firmware_ compliant with the statement in the Base-PPs.


493 The possibility of supplementing the _TOE overview_ of one or more Base-PPs
in a PP-Module has the same meaning as the supplements of a ST regarding
the _TOE overview_ of a PP or the supplements of a PP that is conformant to
another PP.


494 The statement of the _TOE overview_ in a PP-Module is necessary whenever
the TOE overview of the Base-PPs present different characteristics that need
to be consolidated.


495 The PP-Module may provide as many specific TOE overviews as alternative
sets of Base-PPs.


**B.14.4** **Consistency rationale**


496 The PP-Module has to provide a consistency rationale with respect to its
Base-PPs.


497 If the PP-Module specifies alternative sets of Base-PPs, the PP-Module must
provide as many conformance claims as the number of alternative set of
Base-PPs.


498 If the PP-Module specifies alternative sets of Base-PPs, the PP-Module must
provide as many consistency rationales as the number of alternative set of
Base-PPs.


499 The consistency analysis must be performed on the TOE type, the SPD, the
objectives and the security functional requirements. At the end, the goal is to


Page 94 of 106 Version 3.1 April 2017


**Specification of Protection Profiles**


demonstrate that a TOE can meet the TOE type descriptions provided in the
Base-PP(s) and in the PP-Module and that can satisfy all the Base-PPs and
the PP-Module security functional requirements.


500 The consistency rationale must demonstrate that the unions of the SPD, the
objectives and the security functional requirements from the Base-PPs and
from the PP-Module do not lead to a contradiction.


501 The consistency rationale may use correspondence tables between
SPD/objectives/SFRs in the PP-Module and SPD/objectives/SFRs in the
Base-PPs together with textual justifications whenever needed.


502 Note that the consistency at the SFR level implies the consistency of the
union of objectives and the union of SPDs provided that the PP-Module does
not change the assumptions and objectives for the environment of the BasePP(s).


**B.14.5** **Conformance claims**


503 This section describes how the PP-Module conforms to:


 Part 2 of the Common Criteria: CC version and extended security
requirements,


 SFR packages.


504 A PP-Module cannot claim conformance to any PP, PP-Module or PPConfiguration.


505 A PP-Module inherits the conformity to SAR packages (including predefined
EAL) from the Base-PPs. The issue of ANDed Base-PPs with different
EALs has to be dealt with like in an ST conformant to all those PPs.


506 A PP-Module inherits the conformance statement ( _strict_ or _demonstrable_ )
from the Base-PPs. The issue of ANDed Base-PPs with different
conformance statements has to be dealt with like in an ST conformant to all
those PPs.


**B.14.6** **Security problem definition**


507 This section defines the security problem addressed by the PP-Module. It can
contain assumptions, threats and organisational security policies.


508 A PP-Module defines the security problem in relationship with the security
problem of the Base-PPs and the definition of the TOE and its environment
provided in the PP-Module's _Introduction_ .


509 Each element of the SPD may either come from a Base-PP or be entirely new.
Let E be an element of the SPD of a PP-Module, one of the following cases
holds:


April 2017 Version 3.1 Page 95 of 106


**Specification of Protection Profiles**


 E belongs to an identified Base-PP; the PP-Module may only contain
a reference to the element in the Base-PP,


 E results from the refinement of an element of a Base-PP,


 E is a new element introduced by the PP-Module, related to
additional features of the TOE or its environment.


510 Note that the interpreted / refined elements can be dealt with as new elements
without any impact on the meaning of the SPD.


511 Note that as for STs, a PP-Module can introduce assumptions provided they
cover aspects that are outside the scope of the Base-PPs.


**B.14.7** **Security objectives**


512 This section defines the security objectives for the TOE and for the TOE's
operational environment


513 A PP-Module defines the security objectives in relationship with its security
problem and with the security objectives of the Base-PPs.


514 Each security objective may either come from a Base-PP or be entirely new.
Let O be an objective of a PP-Module, one of the following cases holds:


 O belongs to an identified Base-PP; the PP-Module may only contain
a reference to the objective in the Base-PP


 O results from the refinement of an objective of the same kind (for
the TOE or for the TOE operational environment) of a Base-PP,


 O is a new objective introduced by the PP-Module.


515 Note that the refined objectives can be dealt with as new objectives without
any impact on the meaning of the whole set of objectives.


516 As for STs, a PP-Module can introduce new objectives for the TOE
operational environment only provided they address aspects that are outside
the scope of the Base-PPs.


517 In the opposite, if this is the purpose of the PP-Module, some security
objectives for the environment of the Base-PPs could become security
objectives for the TOE in the PP-Module.


518 This section also defines the rationale between the SPD and the security
objectives of the PP-Module, which consists of a mapping that traces the
SPD of the PP-Module to their security objectives as well as a justification
demonstrating that the tracing is effective, as specified in section B.7.
Moreover, the mapping has to show not only that all the assumptions, threats
and organisational security policies are covered but also that there is no
useless security objective.


Page 96 of 106 Version 3.1 April 2017


**Specification of Protection Profiles**


519 It may happen that some security objectives of the PP-Module cover also
elements of the SPD of the Base-PPs that do not belong to the SPD of the
PP-Module itself. This information is not required, but can be provided in
application notes.


**B.14.8** **Extended functional components definition**


520 This section is identical to the standard PP and ST extended components
section specified in section A.8, applied to functional components only.


**B.14.9** **Security functional requirements**


521 This section defines the security functional requirements for the TOE in
relationship with the set of TOE security objectives in the PP-Module and
with the security functional requirements of the Base-PPs.


522 Each security functional requirement may either come from a Base-PP or be
entirely new. Let R be a security functional requirement of a PP-Module, one
of the following cases holds:


 R belongs to an identified Base-PP; the PP-Module may only contain
a reference to the requirement in the Base-PP,


 R results from the refinement of a SFR of a Base-PPs,


 R is a new requirement introduced by the PP-Module.


523 Note that the refined requirements can be dealt with as new ones without any
impact on the meaning of the whole set of requirements.


524 This section also defines the rationale between the SFRs and the TOE
security objectives of the PP-Module, which consists of a mapping that
traces the TOE objectives of the PP-Module to one or more SFRs and a
justification demonstrating that the tracing is effective, as specified in section
B.9. Moreover, the mapping must fulfill the conditions specified in section
B.14.10 and has to show not only that all the objectives for the TOE are
covered but also that there is no useless security functional requirement.


525 It may happen that some SFRs of the PP-Module cover also TOE security
objectives of the Base-PPs that do not belong to the PP-Module itself. This
information is not required, but can be provided in application notes.


**B.14.10** **Guidance for inclusion of elements from Base-PP**


526 In order to limit the amount of information contained in the PP-Module, the
editor may apply the following rules.


527 Let E, O and R belong to the SPD, the security objectives and the security
functional requirements of a Protection Profile Q, respectively, with E
mapped to O and O mapped to R.


April 2017 Version 3.1 Page 97 of 106


**Specification of Protection Profiles**


528 Let P be a PP-Module with Q amongst its Base-PPs. P has to satisfy the
following condition:


529 E, O, R and the mappings between them may belong to P only if at least one
of these elements is linked to a new element in P, that is


 Either there is a new element E' in the SPD of P such that E' is
mapped to O, or


 There is a new objective O' in P such that E is mapped to O' or O' is
mapped to R, or


 There is a new requirement R' in P such that O is mapped to R'.


530 That is, a PP-Module would not contain portions of Base-PPs unless they are
required to fulfill new needs. Here, refined elements are considered new.

## **B.15 Specification of PP-Configurations**


**B.15.1** **Mandatory content of a PP-Configuration**


531 The content of the PP-Configuration is summarized below and explained in
detail in Annexes B.15.3, B.15.4, B.15.5 and B.15.6. A PP-Configuration
contains:


 a _Reference_ that identifies the PP-Configuration,


 a _Components statement_ that identifies the Base-PPs and the PPModules composing the PP-Configuration,


 a _Conformance statement_, that specifies whether the conformance to
this PP-Configuration has to be strict or demonstrable,


 a _SAR statement_, specifying the EAL, SAR package or list of the
selected assurance components applicable to the PP-Configuration.


**B.15.2** **Using the PP-Configuration**


532 PP-Configurations are security statements that cover specific needs of groups
of users, consumers, organisations, etc. Any PP-Configuration can be used
exactly as a standard Protection Profile, as explained in Section B.13.


**B.15.3** **PP-Configuration reference**


533 The PP-Configuration reference provides a clear and unambiguous
identification, usually made of a title, version number, sponsor and the
publication date.


534 The PP-Configuration reference will be used to index the document in
Protection Profiles databases.


Page 98 of 106 Version 3.1 April 2017


**Specification of Protection Profiles**


**B.15.4** **PP-Configuration components statement**


535 The _Components statement_ identifies the Base-PPs and the PP-Modules that
compose the PP-Configuration.


536 The _Components statement_ must include at least all Base-PPs referenced in
the PP-Modules. If the PP-Module specifies alternative sets of Base-PPs,
only one of these sets must be referred to in the PP-Configuration.


**B.15.5** **PP-Configuration conformance statement**


537 The _Conformance statement_ specifies whether the conformance to this PPConfiguration has to be strict or demonstrable.


538 Any ST that claims conformance to the PP-Configuration shall conform to
the kind of conformance claimed in the PP-Configuration.


**B.15.6** **PP-Configuration SAR statement**


539 The _SAR statement_ specifies the set of SAR (potentially predefined EAL)
applicable to any product evaluation with a ST that claims conformance to
this PP-Configuration.


**B.15.7** **Evaluation of a PP-Configuration**


540 The assurance components for PP-Configuration evaluation, defined in
Chapter 11: Class ACE of CC Part 3, are the following: ACE_INT.1,
ACE_CCL.1, ACE_SPD.1, ACE_ECD.1, ACE_OBJ.1, ACE_REQ.1,
ACE_MCO.1 and ACE_CCO.1.


April 2017 Version 3.1 Page 99 of 106


**Guidance for Operations**

# **C Guidance for Operations** **(informative)**

## **C.1 Introduction**


541 As described in this CC part 1, Protection Profiles and Security Targets
contain pre-defined security requirements, as well as providing PP and ST
authors the ability to extend the component lists in some circumstances.

## **C.2 Examples of operations**


542 The four types of operations are given in section 8.1. Examples of the
various operations are described below:


**C.2.1** **The iteration operation**


543 As described in section 8.1.1 the iteration operation may be performed on
every component. The PP/ST author performs an iteration operation by
including multiple requirements based on the same component. Each
iteration of a component is different from all other iterations of that
component, which is realised by completing assignments and selections in a
different way, or by applying refinements to it in a different way. Different
iterations should be uniquely identified to allow clear rationales and tracings
to and from these requirements.


544 A typical example of an iteration is FCS_COP.1 Cryptographic operation
being iterated twice in order to require the implementation of two different
cryptographic algorithms. An example of each iteration being uniquely
identified is:


 Cryptographic operation (RSA and DSA signatures) (FCS_COP.1(1))


 Cryptographic operation (TLS/SSL: symmetric operations)
(FCS_COP.1(2))


**C.2.2** **The assignment operation**


545 As described in section 8.1.2 an assignment operation occurs where a given
component contains an element with a parameter that may be set by the
PP/ST author. The parameter may be an unrestricted variable, or a rule that
narrows the variable to a specific range of values.


546 An example of an element with an assignment is: FIA_AFL.1.2 “When the
defined number of unsuccessful authentication attempts has been met or
surpassed, the TSF shall **[assignment: list of actions]** .”


Page 100 of 106 Version 3.1 April 2017


**Guidance for Operations**


**C.2.3** **The selection operation**


547 As described in section 8.1.3 the selection operation occurs where a given
component contains an element where a choice from several items has to be
made by the PP/ST author.


548 An example of an element with a selection is: FPT_TST.1.1 “The TSF shall
run a suite of self tests [selection: during initial start-up, periodically during
normal operation, at the request of the authorised user, at the conditions

[assignment: conditions under which self test should occur]] to demonstrate
the correct operation of ...”


**C.2.4** **The refinement operation**


549 As described in section 8.1.4 the refinement operation can be performed on
every requirement. The PP/ST author performs a refinement by altering that
requirement.


550 An example of a valid refinement is FIA_UAU.2.1 “The TSF shall require
each user to be successfully authenticated before allowing any other TSFmediated actions on behalf of that user.” being refined to “The TSF shall
require each user to be successfully authenticated **by username/password**
before allowing any other TSF-mediated actions on behalf of that user.”


551 The first rule for a refinement is that a TOE meeting the refined requirement
also meets the unrefined requirement in the context of the PP/ST (i.e. a
refined requirement must be “stricter” than the original requirement)


552 The only exception to this rule is that a PP/ST author is allowed to refine a
SFR to apply to some but not all subjects, objects, operations, security
attributes and/or external entities.


553 An example of a such an exception is FIA_UAU.2.1 “The TSF shall require
each user to be successfully authenticated before allowing any other TSFmediated actions on behalf of that user.” being refined to “The TSF shall
require each user **originating from the internet** to be successfully
authenticated before allowing any other TSF-mediated actions on behalf of
that user.”


554 The second rule for a refinement given is that the refinement shall be related
to the original component. For example, refining an audit component with an
extra element on prevention of electromagnetic radiation is not allowed.


555 A special case of refinement is an editorial refinement, where a small change
is made in a requirement, i.e. rephrasing a sentence due to adherence to
proper English grammar, or to make it more understandable to the reader.
This change is not allowed to modify the meaning of the requirement in any
way. Examples of editorial refinements include:


 the SFR FPT_FLS.1 “The TSF shall continue to preserve a secure
state when the following failures occur: **breakdown of one CPU** ”


April 2017 Version 3.1 Page 101 of 106


**Guidance for Operations**


could be refined to FPT_FLS.1 “The TSF shall continue to preserve a
secure state when the following failure occurs: **breakdown of one**
**CPU** ” or even FPT_FLS.1 “The TSF shall continue to preserve a
secure state when **one CPU breaks down** ”.

## **C.3 Organisation of components**


556 The CC has organised the components in CC Part 2 and CC Part 3 into
hierarchical structures:


 Classes, consisting of


 Families, consisting of


 Components, consisting of


 Elements.


557 This organisation into a hierarchy of class - family - component - element is
provided to assist consumers, developers and evaluators in locating specific
components.


558 The CC presents functional and assurance components in the same general
hierarchical style and use the same organisation and terminology for each.


**C.3.1** **Class**


559 An example of a class is the FIA: Identification and authentication class that
is focused at identification of users, authentication of users and binding of
users and subjects.


**C.3.2** **Family**


560 An example of a family is the User authentication (FIA_UAU) family which
is part of the FIA: Identification and authentication class. This family
concentrates on the authentication of users.


**C.3.3** **Component**


561 An example of a component is FIA_UAU.3 Unforgeable authentication
which concentrates on unforgeable authentication.


**C.3.4** **Element**


562 An example of an element is FIA_UAU.3.2 which concentrates on the
prevention of use of copied authentication data.


Page 102 of 106 Version 3.1 April 2017


**Guidance for Operations**

## **C.4 Extended components**


**C.4.1** **How to define extended components**


563 Whenever a PP/ST author defines an extended component, this has to be
done in a similar manner to the existing CC components: clear, unambiguous
and evaluatable (it is possible to systematically demonstrate whether a
requirement based on that component holds for a TOE). Extended
components must use similar labelling, manner of expression, and level of
detail as the existing CC components.


564 The PP/ST author also has to make to sure that all applicable dependencies
of an extended component are included in the definition of that extended
component. Examples of possible dependencies are:


a) if an extended component refers to auditing, dependencies to
components of the FAU: Security audit class may have to be
included;


b) if an extended component modifies or accesses data, dependencies to
components of the Access control policy (FDP_ACC) family may
have to be included;


c) if an extended component uses a particular design description a
dependency to the appropriate ADV: Development family (e.g.
Functional Specification) may have to be included.


565 In the case of an extended functional component, the PP/ST author also has
to include any applicable audit and associated operations information in the
definition of that component, similar to existing CC Part 2 components. In
the case of an extended assurance component, the PP/ST author also has to
provide suitable evaluation methodology for the component, similar to the
methodology provided in the CEM.


566 Extended components may be placed in existing families, in which case the
PP/ST writer has to show how these families change. If they do not fit into
an existing family, they shall be placed in a new family. New families have
to be defined similarly to the CC.


567 New families may be placed in existing classes in which case the PP/ST
writer has to show how these classes change. If they do not fit into an
existing class, they shall be placed in a new class. New classes have to be
defined similarly to the CC.


April 2017 Version 3.1 Page 103 of 106


**PP conformance**

# **D PP conformance** **(informative)**

## **D.1 Introduction**


568 A PP is intended to be used as a “template” for an ST. That is: the PP
describes a set of user needs, while an ST that conforms to that PP describes
a TOE that satisfies those needs.


569 Note that it is also possible for a PP to be used as a template for another PP.
That is PPs can claim conformance to other PPs. This case is completely
similar to that of an ST vs. a PP. For clarity this Annex describes only the
ST/PP case, but it holds also for the PP/PP case.


570 The CC does not allow any form of partial conformance, so if a PP is
claimed, the PP or ST must fully conform to the referenced PP or PPs. There
are however two types of conformance (“strict” and demonstrable”) and the
type of conformance allowed is determined by the PP. That is, the PP states
(in the PP conformance statement, see section B.5) what the allowed types of
conformance for the ST are. This distinction between strict and demonstrable
conformance is applicable to each PP to which an ST may claim
conformance on an individual basis. This may mean that the ST conforms
strictly to some PPs and demonstrably to other PPs. An ST is only allowed to
conform to a PP in a demonstrable manner, if the PP explicitly allows this,
whereas an ST can always conform with strict conformance to any PP.


571 Restating this in other words, an ST is only allowed to conform to a PP in a
demonstrable manner, if the PP explicitly allows this.


572 Conformance to a PP means that the PP or ST (and if an ST is of an
evaluated product, the product as well) meets all requirements of that PP.


573 Published PPs will normally require demonstrable conformance. This means
that STs claiming conformance with the PP must offer a solution to the
generic security problem described in the PP, but can do so in any way that is
equivalent or more restrictive to that described in the PP. “Equivalent but
more restrictive” is defined at length within the CC, but in principle it means
that the PP and ST may contain entirely different statements that discuss
different entities, use different concepts etc., provided that overall the ST
levies the same or more restrictions on the TOE, and the same or less
restrictions on the operational environment of the TOE.

## **D.2 Strict conformance**


574 Strict conformance is oriented to the PP-author who requires evidence that
the requirements in the PP are met, that the ST is an instantiation of the PP,
though the ST could be broader than the PP. In essence, the ST specifies that


Page 104 of 106 Version 3.1 April 2017


**PP conformance**


the TOE does at least the same as in the PP, while the operational
environment does at most the same as in the PP.


575 A typical example of the use of strict conformance is in selection based
purchasing where a product's security requirements are expected to exactly
match those specified in the PP.


576 An ST instantiating strict conformance to a PP can still introduce additional
restrictions to those given in the PP.

## **D.3 Demonstrable conformance**


577 Demonstrable conformance is orientated to the PP-author who requires
evidence that the ST is a suitable solution to the generic security problem
described in the PP.


578 Where there is a clear subset-superset type relation between PP and ST in the
case of strict conformance, the relation is less clear-cut in the case of
demonstrable conformance. STs claiming conformance with the PP must
offer a solution to the generic security problem described in the PP. but can
do so in any way that is equivalent or more restrictive to that described in the
PP.


April 2017 Version 3.1 Page 105 of 106


**Bibliography**

# **E Bibliography** **(informative)**


579 This bibliography contains references to further material and standards that
the reader of the CC may find useful. For undated references the reader is
recommended to refer to the latest edition of the referenced document.

## **E.1 ISO/IEC standards and guidance**


[ISO/IEC 15292] Information technology -- Security techniques -Protection Profile registration procedures


[ISO/IEC 15443] Information technology -- Security techniques -- A
framework for IT security assurance - all parts


[ISO/IEC 15446] Information technology -- Security techniques -Guide for the production of Protection Profiles and
Security Targets


[ISO/IEC 19790] Information technology -- Security techniques -Security requirements for cryptographic modules


[ISO/IEC 19791] Information technology -- Security techniques -Security assessment of operational systems


[ISO/IEC 27001] Information technology -- Security techniques -Information security management systems -Requirements


[ISO/IEC 27002] Information technology -- Security techniques -- Code
of practice for information security management

## **E.2 Other standards and guidance**


[IEEE Std 610.12-1990] Institute of Electrical and Electronics
Engineers, Standard Glossary of Software Engineering
Terminology


[CC portal] Common Criteria portal, February 2009. CCRA,
www.commoncriteriaportal.org


Page 106 of 106 Version 3.1 April 2017



