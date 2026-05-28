## Identification and Authentication (FIA)

### FIA\_XCU\_EXT.1 Implementation of X.509 Functions

FIA\_XCU\_EXT.1.1  
The TSF shall \[**selection**: *verify, assert*\] identities included in X.509 certificates.

### FIA\_X509\_EXT.1 X.509 Certificate Validation

FIA\_X509\_EXT.1.1  
The TSF shall \[**selection**: *invoke platform-provided functionality, implement functionality*\] to validate certificates in accordance with the following rules:

* Certification path validation meets requirements of RFC 5280 for certificate paths of \[**selection**: *unlimited path length, maximum path length of \[**assignment**: 102\] certificates*\] and certificate paths exceeding the maximum path length are invalid.  
* The current time is within the notBefore and notAfter values of all certificates in the certification path.  
* The certification path shall terminate at a trust anchor element appropriate for the supported function.  
* Certificates containing subjectUniqueID or issuerUniqueID fields are considered invalid.  
* Certificates are signed using cryptographic signatures and hashes in accordance with RFC 8603, and \[**selection**:  
  * *\[**assignment**: list of supported cryptographic algorithms\]*  
  * *no other algorithms*

  \] and certificates signed using other cryptographic algorithms are considered invalid.

* \[**selection**:  
  * *CRLs are signed using cryptographic signatures and hashes in accordance with RFC 8603 and \[**selection**:*  
    * *\[**assignment**: list of supported cryptographic algorithms\]*  
    * *no other algorithms*

    *\] and CRLs signed using other cryptographic algorithms are considered invalid;*

  * *OCSP responses are signed using \[**selection**:*  
    * *sha384WithRSAEncryption with key size of 3072 bits or greater,*  
    * *ecdsa-with-SHA384 using \[**selection**: secp384r1, secp521r1\],*  
    * *ecdsa-with-SHA512 using \[**selection**: secp384r1, secp521r1\],*

    *\] and \[**selection**:*

    * *\[**assignment**: list of other supported algorithms\]*  
    * *no other algorithms*

    *\] requested using the preferredSignatureAlgorithm extension and OCSP responses are considered invalid if using other algorithms;*

  * *No other algorithm constraints*

  \].


FIA\_X509\_EXT.1.2  
The TSF shall \[**selection**: *invoke platform-provided functionality, implement*\] processing of the extensions indicated in RFC 5280, section 4.2,

* Authority Key Identifier,  
* Subject Key Identifier  
* keyUsage  
* and \[**selection**:  
  * *basicConstraints*  
  * *authorityInformationAccess*  
  * *cRLDistributionPoints*  
  * *certificatePolicies*  
  * *policyMapping*  
  * *Subject alternate name containing any of the following name types \[**selection**:*  
    * *rfc822Name*  
    * *dNSName*  
    * *directoryName*  
    * *uniformResourceIdentifier*  
    * *iPAddress*  
    * *\[**assignment**: other name types\]*

  *\]*

  * *extendedKeyUsage*  
  * *nameConstraints*  
  * *\[**assignment**: other extensions\]*  
  * *no other extensions*

  \].


FIA\_X509\_EXT.1.3  
The TSF shall \[**selection**: *invoke platform-provided functionality, implement functionality*\] to validate revocation status of the certificate using \[**selection**:

* *The Online Certificate Status Protocol (OCSP) as specified in RFC 6960*  
* *Certificate Revocation Lists (CRL) as specified in RFC 5280 and refined by RFC 8603*  
* *Certificate Revocation Lists as specified in RFC 5280*  
* *Based on validity period: Certificates expiring within \[**assignment**: time less than 24 hours\] of the current time are considered valid when no other valid revocation status information is available*  
* *Administrative notification of revocation: \[**assignment**: administrative action upon notification\] using \[**assignment**: method to invalidate use of certificates in supported functions\] when the certificate is revoked.*  
* *Direct association with Certification Authority: \[**assignment**: direct revocation status information implementations\]*

\].

FIA\_X509\_EXT.1.4  
The TSF shall \[**selection**:

* *not obtain revocation status information by the TSF due to \[**selection**: determining that the certificate expires within \[**assignment**: time less than 24 hours\], determining that the \[assignment: supported function\] validates revocation status using \[**assignment**: methods supported by the function\]\]*  
* *\[**selection**: invoke platform-provided functionality, implement functionality\] to obtain supported revocation status information via \[**selection**:*  
* *Network connection to \[**selection**: CA, CRL distribution point, OCSP responder, \[**assignment**: alternate sources \]\]*  
* *Local revocation status information from \[**selection**: cached CRL, embedded CA repository, local OCSP responder, administrator configuration\]*  
* *An OCSP TLS Status Request Extension (OCSP stapling) as specified in RFC 6066,*  
* *An OCSP TLS Multiple Certificate Status Request Extension (OCSP multi-stapling) as specified in RFC 6961*  
  *\]*

\].

FIA\_X509\_EXT.1.5  
The TSF shall \[**selection**: *invoke platform-provided functionality, implement functionality, pass context information to the supported function*\] to validate that the context of the certificate path and trust store element is consistent with the supported function use via \[**selection**:

* *processing \[**selection**:*  
  * *\[**assignment**: trust store context rules\]*  
  * *extendedKeyUsage field constraints in the leaf certificate including: \[**selection**:*  
    * *\[**assignment**: trust store context rules\],*  
    * *extendedKeyUsage (EKU) field constraints in the leaf certificate including: \[**selection**:*  
      * *Certificates used for trusted updates and executable code integrity verification shall have the Code Signing Purpose (id-kp 3 with OID 1.3.6.1.5.5.7.3.3),*  
      * *Client certificates presented for TLS shall have the Client Authentication purpose (id-kp 2 with OID 1.3.6.1.5.5.7.3.2),*  
      * *Server certificates presented for TLS shall have the Server Authentication purpose (id-kp 1 with OID 1.3.6.1.5.5.7.3.1),*  
      * *Delegated OCSP signer’s certificates presented for OCSP responses shall have the OCSP Signing purpose (id-kp 9 with OID 1.3.6.1.5.5.7.3.9),*  
      * *Server certificates presented for EST shall have the CMC Registration Authority (RA) purpose (id-kp-cmcRA with OID 1.3.6.1.5.5.7.3.28),*  
      * *Certificates representing a Registration Authority have the CMC Registration Authority Purpose (id-kp-cmcRA with OID 1.3.6.1.5.5.7.3.28),*  
      * *Certificates representing a Registration Authority have the v3 CMP Registration Authority Purpose (id-kp-cmcRA with OID 1.3.6.1.5.5.7.3.28)*  
      * *SMIME certificates presented to protect email have the email protection purpose (id-kp-emailProtection with OID 1.3.6.1.5.5.7.3.4),*  
      * *IPsec and IKE certificates used in conjunction with other functions requiring an explicit EKU also have the IPsec-IKE purpose (id-kp-ipsecIKE with OID 1.3.6.1.5.5.7.3.17) in accordance with RFC 4945, section 5.1.3.12,*  
      * *\[**assignment**: other EKU values required by supported functions\]*

      *\]*

    *\] and rejects the certificate if the context requirements are not met,*

  *\]*

* *passing \[**selection**: certification path, \[**assignment**: context from certification path processing\] passed to the supported function\]*

\].

FIA\_X509\_EXT.1.6  
The TSF shall \[**selection**: *manage trust stores, use platform-managed trust stores*\] used for certification path validation.

### FIA\_X509\_EXT.2 X.509 Certificate Support for Functions

FIA\_X509\_EXT.2.1  
The TSF shall \[**selection**: *invoke platform-provided functionality to validate, validate*\] X.509v3 certificates in accordance with FIA\_X509\_EXT.1 to support \[**assignment**: *supported functions*\] using \[**selection**:

* *\[**selection**: TLS, DTLS, IPsec or IKE , SMIME, SSH, \[**assignment**: other authenticated communications protocol\]\]*  
* *\[**selection**: code signing for system software updates, code signing for software integrity testing, integrity verification for TSF protected data, administrator authentication, user authentication , \[**assignment**: other uses\]\]*

\].

FIA\_X509\_EXT.2.2  
For each function indicated in FIA\_X509\_EXT.2.1, the TSF shall \[**selection**: *invoke the TOE platform to determine, determine*\] whether the \[**selection**: *administrator is allowed to configure certificate acceptance, supported function determines acceptance via \[**assignment**: method of determining acceptance\], certificate is accepted, certificate is not accepted*\] when valid certificate revocation status information cannot be obtained from a source indicated in FIA\_X509\_EXT.1.3.

### FIA\_X509\_EXT.3 X.509 Certificate Requests

FIA\_X509\_EXT.3.1  
The TSF shall \[**selection**: *invoke the TOE platform to generate, generate*\] Certificate Requests as specified by \[**selection**:

* *RFC 2986 (PKCS-10)*  
* *RFC 7030 as updated by RFC 8996 (EST)*  
* *RFC 5272 as updated by RFC 6402 (CMC)*  
* *RFC 5272 as updated by RFC 8756 (CNSA CMC)*  
* *RFC 4210 as updated by RFC 6712 and RFC 9481 (v2 CMP)*  
* *RFC 4210 as updated by RFC 6712 and RFC 9480 (v3 CMP)*

\] and be able to provide the following information in the request: public key, \[**selection**:

* *Subject DN consisting of values for \[**selection**:*  
  * *U*  
  * *O*  
  * *OU*  
  * *CN*  
  * *\[**assignment**: other subject attributes\]*

  *\]*

* *one or more of the following SAN types \[**selection**:*  
  * *rfc822Name*  
  * *dNSName*  
  * *directoryName*  
  * *uniformResourceIdentifier*  
  * *iPAddress*  
  * *\[**assignment**: other SAN types\]*

  *\]*

\] and \[**selection**:

* *\[**assignment**: list of other certificate field and extension values\]*  
* *\[**assignment**: list of identifying information\]*  
* *no other information.*

\].

FIA\_X509\_EXT.3.2  
The TSF shall \[**selection**: *invoke platform-provided functionality, provide functionality*\] to validate the certificate path in accordance with FIA\_X509\_EXT.1 upon receiving the CA Certificate Response, and validate that the certificate path of the response is consistent with the intended usage.

### FIA\_XCU\_EXT.2 X.509 Certificate Acquisition

FIA\_XCU\_EXT.2.1  
The TSF shall \[**selection**:

* *request certificates from an \[**selection**: external, embedded \] CA,*  
* *obtain certificates from an embedded CA*

\] to represent \[**assignment**: *TOE functions*\] for \[**selection**:

* *\[**selection**: TLS, DTLS, IPsec or IKE , SMIME, SSH, \[**assignment**: other authenticated communications protocol\]\]*  
* *\[**selection**: code signing for system software updates, code signing for software integrity testing, integrity verification for TSF protected data, administrator authentication, user authentication, \[**assignment**: other uses\]\]*

\].
