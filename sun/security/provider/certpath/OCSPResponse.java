/*     */ package sun.security.provider.certpath;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import java.security.AccessController;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.Signature;
/*     */ import java.security.SignatureException;
/*     */ import java.security.cert.CRLReason;
/*     */ import java.security.cert.CertPathValidatorException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateParsingException;
/*     */ import java.security.cert.TrustAnchor;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import sun.misc.HexDumpEncoder;
/*     */ import sun.security.action.GetIntegerAction;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ import sun.security.x509.AlgorithmId;
/*     */ import sun.security.x509.PKIXExtensions;
/*     */ import sun.security.x509.X500Name;
/*     */ import sun.security.x509.X509CertImpl;
/*     */ 
/*     */ public final class OCSPResponse
/*     */ {
/* 129 */   private static ResponseStatus[] rsvalues = ResponseStatus.values();
/*     */ 
/* 131 */   private static final Debug DEBUG = Debug.getInstance("certpath");
/* 132 */   private static final boolean dump = Debug.isOn("ocsp");
/* 133 */   private static final ObjectIdentifier OCSP_BASIC_RESPONSE_OID = ObjectIdentifier.newInternal(new int[] { 1, 3, 6, 1, 5, 5, 7, 48, 1, 1 });
/*     */ 
/* 135 */   private static final ObjectIdentifier OCSP_NONCE_EXTENSION_OID = ObjectIdentifier.newInternal(new int[] { 1, 3, 6, 1, 5, 5, 7, 48, 1, 2 });
/*     */   private static final int CERT_STATUS_GOOD = 0;
/*     */   private static final int CERT_STATUS_REVOKED = 1;
/*     */   private static final int CERT_STATUS_UNKNOWN = 2;
/*     */   private static final int NAME_TAG = 1;
/*     */   private static final int KEY_TAG = 2;
/*     */   private static final String KP_OCSP_SIGNING_OID = "1.3.6.1.5.5.7.3.9";
/*     */   private final ResponseStatus responseStatus;
/*     */   private final Map<CertId, SingleResponse> singleResponseMap;
/*     */   private static final int DEFAULT_MAX_CLOCK_SKEW = 900000;
/* 160 */   private static final int MAX_CLOCK_SKEW = initializeClockSkew();
/*     */ 
/* 179 */   private static CRLReason[] values = CRLReason.values();
/*     */ 
/*     */   private static int initializeClockSkew()
/*     */   {
/* 168 */     Integer localInteger = (Integer)AccessController.doPrivileged(new GetIntegerAction("com.sun.security.ocsp.clockSkew"));
/*     */ 
/* 170 */     if ((localInteger == null) || (localInteger.intValue() < 0)) {
/* 171 */       return 900000;
/*     */     }
/*     */ 
/* 175 */     return localInteger.intValue() * 1000;
/*     */   }
/*     */ 
/*     */   OCSPResponse(byte[] paramArrayOfByte, Date paramDate, List<X509Certificate> paramList)
/*     */     throws IOException, CertPathValidatorException
/*     */   {
/* 189 */     if (dump) {
/* 190 */       localObject1 = new HexDumpEncoder();
/* 191 */       DEBUG.println("\nOCSPResponse bytes...");
/* 192 */       DEBUG.println(((HexDumpEncoder)localObject1).encode(paramArrayOfByte) + "\n");
/*     */     }
/* 194 */     Object localObject1 = new DerValue(paramArrayOfByte);
/* 195 */     if (((DerValue)localObject1).tag != 48) {
/* 196 */       throw new IOException("Bad encoding in OCSP response: expected ASN.1 SEQUENCE tag.");
/*     */     }
/*     */ 
/* 199 */     DerInputStream localDerInputStream1 = ((DerValue)localObject1).getData();
/*     */ 
/* 202 */     int i = localDerInputStream1.getEnumerated();
/* 203 */     if ((i >= 0) && (i < rsvalues.length)) {
/* 204 */       this.responseStatus = rsvalues[i];
/*     */     }
/*     */     else {
/* 207 */       throw new IOException("Unknown OCSPResponse status: " + i);
/*     */     }
/* 209 */     if (DEBUG != null) {
/* 210 */       DEBUG.println("OCSP response status: " + this.responseStatus);
/*     */     }
/* 212 */     if (this.responseStatus != ResponseStatus.SUCCESSFUL)
/*     */     {
/* 214 */       this.singleResponseMap = Collections.emptyMap();
/* 215 */       return;
/*     */     }
/*     */ 
/* 219 */     localObject1 = localDerInputStream1.getDerValue();
/* 220 */     if (!((DerValue)localObject1).isContextSpecific((byte)0)) {
/* 221 */       throw new IOException("Bad encoding in responseBytes element of OCSP response: expected ASN.1 context specific tag 0.");
/*     */     }
/*     */ 
/* 224 */     DerValue localDerValue1 = ((DerValue)localObject1).data.getDerValue();
/* 225 */     if (localDerValue1.tag != 48) {
/* 226 */       throw new IOException("Bad encoding in responseBytes element of OCSP response: expected ASN.1 SEQUENCE tag.");
/*     */     }
/*     */ 
/* 231 */     localDerInputStream1 = localDerValue1.data;
/* 232 */     ObjectIdentifier localObjectIdentifier = localDerInputStream1.getOID();
/* 233 */     if (localObjectIdentifier.equals(OCSP_BASIC_RESPONSE_OID)) {
/* 234 */       if (DEBUG != null)
/* 235 */         DEBUG.println("OCSP response type: basic");
/*     */     }
/*     */     else {
/* 238 */       if (DEBUG != null) {
/* 239 */         DEBUG.println("OCSP response type: " + localObjectIdentifier);
/*     */       }
/* 241 */       throw new IOException("Unsupported OCSP response type: " + localObjectIdentifier);
/*     */     }
/*     */ 
/* 246 */     DerInputStream localDerInputStream2 = new DerInputStream(localDerInputStream1.getOctetString());
/*     */ 
/* 249 */     DerValue[] arrayOfDerValue1 = localDerInputStream2.getSequence(2);
/* 250 */     if (arrayOfDerValue1.length < 3) {
/* 251 */       throw new IOException("Unexpected BasicOCSPResponse value");
/*     */     }
/*     */ 
/* 254 */     DerValue localDerValue2 = arrayOfDerValue1[0];
/*     */ 
/* 257 */     byte[] arrayOfByte1 = arrayOfDerValue1[0].toByteArray();
/*     */ 
/* 260 */     if (localDerValue2.tag != 48) {
/* 261 */       throw new IOException("Bad encoding in tbsResponseData element of OCSP response: expected ASN.1 SEQUENCE tag.");
/*     */     }
/*     */ 
/* 264 */     DerInputStream localDerInputStream3 = localDerValue2.data;
/* 265 */     DerValue localDerValue3 = localDerInputStream3.getDerValue();
/*     */ 
/* 268 */     if (localDerValue3.isContextSpecific((byte)0))
/*     */     {
/* 270 */       if ((localDerValue3.isConstructed()) && (localDerValue3.isContextSpecific()))
/*     */       {
/* 272 */         localDerValue3 = localDerValue3.data.getDerValue();
/* 273 */         j = localDerValue3.getInteger();
/* 274 */         if (localDerValue3.data.available() != 0) {
/* 275 */           throw new IOException("Bad encoding in version  element of OCSP response: bad format");
/*     */         }
/*     */ 
/* 278 */         localDerValue3 = localDerInputStream3.getDerValue();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 283 */     int j = (short)(byte)(localDerValue3.tag & 0x1F);
/* 284 */     if (j == 1) {
/* 285 */       if (DEBUG != null) {
/* 286 */         localObject2 = new X500Name(localDerValue3.getData());
/* 287 */         DEBUG.println("OCSP Responder name: " + localObject2);
/*     */       }
/* 289 */     } else if (j == 2) {
/* 290 */       localDerValue3 = localDerValue3.data.getDerValue();
/* 291 */       if (DEBUG != null) {
/* 292 */         localObject2 = localDerValue3.getOctetString();
/* 293 */         DEBUG.println("OCSP Responder key ID: " + String.format(new StringBuilder().append("0x%0").append(localObject2.length * 2).append("x").toString(), new Object[] { new BigInteger(1, (byte[])localObject2) }));
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 299 */       throw new IOException("Bad encoding in responderID element of OCSP response: expected ASN.1 context specific tag 1 or 2");
/*     */     }
/*     */ 
/* 304 */     localDerValue3 = localDerInputStream3.getDerValue();
/* 305 */     if (DEBUG != null) {
/* 306 */       localObject2 = localDerValue3.getGeneralizedTime();
/* 307 */       DEBUG.println("OCSP response produced at: " + localObject2);
/*     */     }
/*     */ 
/* 311 */     Object localObject2 = localDerInputStream3.getSequence(1);
/* 312 */     this.singleResponseMap = new HashMap(localObject2.length);
/*     */ 
/* 314 */     if (DEBUG != null) {
/* 315 */       DEBUG.println("OCSP number of SingleResponses: " + localObject2.length);
/*     */     }
/*     */ 
/* 318 */     for (int k = 0; k < localObject2.length; k++) {
/* 319 */       SingleResponse localSingleResponse = new SingleResponse(localObject2[k], paramDate, null);
/*     */ 
/* 321 */       this.singleResponseMap.put(localSingleResponse.getCertId(), localSingleResponse);
/*     */     }
/*     */ 
/* 325 */     if (localDerInputStream3.available() > 0) {
/* 326 */       localDerValue3 = localDerInputStream3.getDerValue();
/* 327 */       if (localDerValue3.isContextSpecific((byte)1)) {
/* 328 */         localObject3 = localDerValue3.data.getSequence(3);
/* 329 */         for (int m = 0; m < localObject3.length; m++) {
/* 330 */           localObject4 = new sun.security.x509.Extension(localObject3[m]);
/*     */ 
/* 332 */           if (DEBUG != null) {
/* 333 */             DEBUG.println("OCSP extension: " + localObject4);
/*     */           }
/* 335 */           if (!((sun.security.x509.Extension)localObject4).getExtensionId().equals(OCSP_NONCE_EXTENSION_OID))
/*     */           {
/* 341 */             if (((sun.security.x509.Extension)localObject4).isCritical()) {
/* 342 */               throw new IOException("Unsupported OCSP critical extension: " + ((sun.security.x509.Extension)localObject4).getExtensionId());
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 351 */     Object localObject3 = AlgorithmId.parse(arrayOfDerValue1[1]);
/*     */ 
/* 354 */     byte[] arrayOfByte2 = arrayOfDerValue1[2].getBitString();
/* 355 */     Object localObject4 = null;
/*     */     DerValue[] arrayOfDerValue2;
/* 358 */     if (arrayOfDerValue1.length > 3)
/*     */     {
/* 360 */       localObject5 = arrayOfDerValue1[3];
/* 361 */       if (!((DerValue)localObject5).isContextSpecific((byte)0)) {
/* 362 */         throw new IOException("Bad encoding in certs element of OCSP response: expected ASN.1 context specific tag 0.");
/*     */       }
/*     */ 
/* 365 */       arrayOfDerValue2 = ((DerValue)localObject5).getData().getSequence(3);
/* 366 */       localObject4 = new X509CertImpl[arrayOfDerValue2.length];
/*     */       try {
/* 368 */         for (int n = 0; n < arrayOfDerValue2.length; n++)
/* 369 */           localObject4[n] = new X509CertImpl(arrayOfDerValue2[n].toByteArray());
/*     */       }
/*     */       catch (CertificateException localCertificateException) {
/* 372 */         throw new IOException("Bad encoding in X509 Certificate", localCertificateException);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 378 */     Object localObject5 = (X509Certificate)paramList.get(0);
/*     */     byte[] arrayOfByte3;
/* 381 */     if ((localObject4 != null) && (localObject4[0] != null)) {
/* 382 */       arrayOfDerValue2 = localObject4[0];
/*     */ 
/* 384 */       if (DEBUG != null) {
/* 385 */         DEBUG.println("Signer certificate name: " + arrayOfDerValue2.getSubjectX500Principal());
/*     */ 
/* 388 */         arrayOfByte3 = arrayOfDerValue2.getSubjectKeyIdentifier();
/* 389 */         if (arrayOfByte3 != null) {
/* 390 */           DEBUG.println("Signer certificate key ID: " + String.format(new StringBuilder().append("0x%0").append(arrayOfByte3.length * 2).append("x").toString(), new Object[] { new BigInteger(1, arrayOfByte3) }));
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 396 */       arrayOfByte3 = null;
/*     */ 
/* 398 */       for (X509Certificate localX509Certificate : paramList)
/*     */       {
/* 401 */         if (arrayOfDerValue2.equals(localX509Certificate))
/*     */         {
/* 404 */           localObject5 = localX509Certificate;
/* 405 */           if (DEBUG == null) break;
/* 406 */           DEBUG.println("Signer certificate is a trusted responder"); break;
/*     */         }
/*     */ 
/* 413 */         if (arrayOfDerValue2.getIssuerX500Principal().equals(localX509Certificate.getSubjectX500Principal()))
/*     */         {
/* 417 */           if (arrayOfByte3 == null) {
/* 418 */             arrayOfByte3 = arrayOfDerValue2.getIssuerKeyIdentifier();
/* 419 */             if ((arrayOfByte3 == null) && 
/* 420 */               (DEBUG != null)) {
/* 421 */               DEBUG.println("No issuer key identifier (AKID) in the signer certificate");
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 428 */           byte[] arrayOfByte4 = null;
/* 429 */           if ((arrayOfByte3 != null) && ((arrayOfByte4 = OCSPChecker.getKeyId(localX509Certificate)) != null))
/*     */           {
/* 432 */             if (Arrays.equals(arrayOfByte3, arrayOfByte4))
/*     */             {
/* 436 */               if (DEBUG != null) {
/* 437 */                 DEBUG.println("Issuer certificate key ID: " + String.format(new StringBuilder().append("0x%0").append(arrayOfByte3.length * 2).append("x").toString(), new Object[] { new BigInteger(1, arrayOfByte3) }));
/*     */               }
/*     */             }
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/*     */             try
/*     */             {
/* 446 */               List localList = arrayOfDerValue2.getExtendedKeyUsage();
/*     */ 
/* 448 */               if ((localList == null) || (!localList.contains("1.3.6.1.5.5.7.3.9")))
/*     */               {
/* 451 */                 continue;
/*     */               }
/*     */             } catch (CertificateParsingException localCertificateParsingException) {
/*     */             }
/* 455 */             continue;
/*     */ 
/* 460 */             AlgorithmChecker localAlgorithmChecker = new AlgorithmChecker(new TrustAnchor(localX509Certificate, null));
/*     */ 
/* 462 */             localAlgorithmChecker.init(false);
/* 463 */             localAlgorithmChecker.check(arrayOfDerValue2, Collections.emptySet());
/*     */             try
/*     */             {
/* 468 */               if (paramDate == null)
/* 469 */                 arrayOfDerValue2.checkValidity();
/*     */               else
/* 471 */                 arrayOfDerValue2.checkValidity(paramDate);
/*     */             }
/*     */             catch (GeneralSecurityException localGeneralSecurityException1) {
/* 474 */               if (DEBUG != null) {
/* 475 */                 DEBUG.println("Responder's certificate not within the validity period " + localGeneralSecurityException1);
/*     */               }
/*     */             }
/* 478 */             continue;
/*     */ 
/* 488 */             sun.security.x509.Extension localExtension = arrayOfDerValue2.getExtension(PKIXExtensions.OCSPNoCheck_Id);
/*     */ 
/* 490 */             if ((localExtension != null) && 
/* 491 */               (DEBUG != null)) {
/* 492 */               DEBUG.println("Responder's certificate includes the extension id-pkix-ocsp-nocheck.");
/*     */             }
/*     */ 
/*     */             try
/*     */             {
/* 502 */               arrayOfDerValue2.verify(localX509Certificate.getPublicKey());
/* 503 */               localObject5 = arrayOfDerValue2;
/*     */ 
/* 505 */               if (DEBUG != null) {
/* 506 */                 DEBUG.println("Signer certificate was issued by a trusted responder");
/*     */               }
/*     */ 
/*     */             }
/*     */             catch (GeneralSecurityException localGeneralSecurityException2)
/*     */             {
/* 512 */               localObject5 = null;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 520 */     if (localObject5 != null)
/*     */     {
/* 523 */       AlgorithmChecker.check(((X509Certificate)localObject5).getPublicKey(), (AlgorithmId)localObject3);
/*     */ 
/* 526 */       if (!verifyResponse(arrayOfByte1, (X509Certificate)localObject5, (AlgorithmId)localObject3, arrayOfByte2))
/*     */       {
/* 528 */         throw new CertPathValidatorException("Error verifying OCSP Responder's signature");
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 533 */       throw new CertPathValidatorException("Responder's certificate is not trusted for signing OCSP responses");
/*     */     }
/*     */   }
/*     */ 
/*     */   ResponseStatus getResponseStatus()
/*     */   {
/* 543 */     return this.responseStatus;
/*     */   }
/*     */ 
/*     */   private boolean verifyResponse(byte[] paramArrayOfByte1, X509Certificate paramX509Certificate, AlgorithmId paramAlgorithmId, byte[] paramArrayOfByte2)
/*     */     throws CertPathValidatorException
/*     */   {
/*     */     try
/*     */     {
/* 555 */       Signature localSignature = Signature.getInstance(paramAlgorithmId.getName());
/* 556 */       localSignature.initVerify(paramX509Certificate.getPublicKey());
/* 557 */       localSignature.update(paramArrayOfByte1);
/*     */ 
/* 559 */       if (localSignature.verify(paramArrayOfByte2)) {
/* 560 */         if (DEBUG != null) {
/* 561 */           DEBUG.println("Verified signature of OCSP Responder");
/*     */         }
/* 563 */         return true;
/*     */       }
/*     */ 
/* 566 */       if (DEBUG != null) {
/* 567 */         DEBUG.println("Error verifying signature of OCSP Responder");
/*     */       }
/*     */ 
/* 570 */       return false;
/*     */     }
/*     */     catch (InvalidKeyException localInvalidKeyException) {
/* 573 */       throw new CertPathValidatorException(localInvalidKeyException);
/*     */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 575 */       throw new CertPathValidatorException(localNoSuchAlgorithmException);
/*     */     } catch (SignatureException localSignatureException) {
/* 577 */       throw new CertPathValidatorException(localSignatureException);
/*     */     }
/*     */   }
/*     */ 
/*     */   SingleResponse getSingleResponse(CertId paramCertId)
/*     */   {
/* 586 */     return (SingleResponse)this.singleResponseMap.get(paramCertId);
/*     */   }
/*     */ 
/*     */   public static enum ResponseStatus
/*     */   {
/* 121 */     SUCCESSFUL, 
/* 122 */     MALFORMED_REQUEST, 
/* 123 */     INTERNAL_ERROR, 
/* 124 */     TRY_LATER, 
/* 125 */     UNUSED, 
/* 126 */     SIG_REQUIRED, 
/* 127 */     UNAUTHORIZED;
/*     */   }
/*     */ 
/*     */   static final class SingleResponse
/*     */     implements OCSP.RevocationStatus
/*     */   {
/*     */     private final CertId certId;
/*     */     private final OCSP.RevocationStatus.CertStatus certStatus;
/*     */     private final Date thisUpdate;
/*     */     private final Date nextUpdate;
/*     */     private final Date revocationTime;
/*     */     private final CRLReason revocationReason;
/*     */     private final Map<String, java.security.cert.Extension> singleExtensions;
/*     */ 
/*     */     private SingleResponse(DerValue paramDerValue)
/*     */       throws IOException
/*     */     {
/* 602 */       this(paramDerValue, null);
/*     */     }
/*     */ 
/*     */     private SingleResponse(DerValue paramDerValue, Date paramDate) throws IOException
/*     */     {
/* 607 */       if (paramDerValue.tag != 48) {
/* 608 */         throw new IOException("Bad ASN.1 encoding in SingleResponse");
/*     */       }
/* 610 */       DerInputStream localDerInputStream = paramDerValue.data;
/*     */ 
/* 612 */       this.certId = new CertId(localDerInputStream.getDerValue().data);
/* 613 */       DerValue localDerValue = localDerInputStream.getDerValue();
/* 614 */       int i = (short)(byte)(localDerValue.tag & 0x1F);
/*     */       Object localObject1;
/*     */       int j;
/* 615 */       if (i == 1) {
/* 616 */         this.certStatus = OCSP.RevocationStatus.CertStatus.REVOKED;
/* 617 */         this.revocationTime = localDerValue.data.getGeneralizedTime();
/* 618 */         if (localDerValue.data.available() != 0) {
/* 619 */           localObject1 = localDerValue.data.getDerValue();
/* 620 */           i = (short)(byte)(((DerValue)localObject1).tag & 0x1F);
/* 621 */           if (i == 0) {
/* 622 */             j = ((DerValue)localObject1).data.getEnumerated();
/*     */ 
/* 624 */             if ((j >= 0) && (j < OCSPResponse.values.length))
/* 625 */               this.revocationReason = OCSPResponse.values[j];
/*     */             else
/* 627 */               this.revocationReason = CRLReason.UNSPECIFIED;
/*     */           }
/*     */           else {
/* 630 */             this.revocationReason = CRLReason.UNSPECIFIED;
/*     */           }
/*     */         } else {
/* 633 */           this.revocationReason = CRLReason.UNSPECIFIED;
/*     */         }
/*     */ 
/* 636 */         if (OCSPResponse.DEBUG != null) {
/* 637 */           OCSPResponse.DEBUG.println("Revocation time: " + this.revocationTime);
/* 638 */           OCSPResponse.DEBUG.println("Revocation reason: " + this.revocationReason);
/*     */         }
/*     */       } else {
/* 641 */         this.revocationTime = null;
/* 642 */         this.revocationReason = CRLReason.UNSPECIFIED;
/* 643 */         if (i == 0)
/* 644 */           this.certStatus = OCSP.RevocationStatus.CertStatus.GOOD;
/* 645 */         else if (i == 2)
/* 646 */           this.certStatus = OCSP.RevocationStatus.CertStatus.UNKNOWN;
/*     */         else {
/* 648 */           throw new IOException("Invalid certificate status");
/*     */         }
/*     */       }
/*     */ 
/* 652 */       this.thisUpdate = localDerInputStream.getGeneralizedTime();
/*     */ 
/* 654 */       if (localDerInputStream.available() == 0)
/*     */       {
/* 656 */         this.nextUpdate = null;
/*     */       } else {
/* 658 */         localDerValue = localDerInputStream.getDerValue();
/* 659 */         i = (short)(byte)(localDerValue.tag & 0x1F);
/* 660 */         if (i == 0)
/*     */         {
/* 662 */           this.nextUpdate = localDerValue.data.getGeneralizedTime();
/*     */ 
/* 664 */           if (localDerInputStream.available() != 0)
/*     */           {
/* 667 */             localDerValue = localDerInputStream.getDerValue();
/* 668 */             i = (short)(byte)(localDerValue.tag & 0x1F);
/*     */           }
/*     */         } else {
/* 671 */           this.nextUpdate = null;
/*     */         }
/*     */       }
/*     */ 
/* 675 */       if (localDerInputStream.available() > 0) {
/* 676 */         localDerValue = localDerInputStream.getDerValue();
/* 677 */         if (localDerValue.isContextSpecific((byte)1)) {
/* 678 */           localObject1 = localDerValue.data.getSequence(3);
/* 679 */           this.singleExtensions = new HashMap(localObject1.length);
/*     */ 
/* 682 */           for (j = 0; j < localObject1.length; j++) {
/* 683 */             localObject2 = new sun.security.x509.Extension(localObject1[j]);
/* 684 */             if (OCSPResponse.DEBUG != null) {
/* 685 */               OCSPResponse.DEBUG.println("OCSP single extension: " + localObject2);
/*     */             }
/*     */ 
/* 690 */             if (((sun.security.x509.Extension)localObject2).isCritical()) {
/* 691 */               throw new IOException("Unsupported OCSP critical extension: " + ((sun.security.x509.Extension)localObject2).getExtensionId());
/*     */             }
/*     */ 
/* 695 */             this.singleExtensions.put(((sun.security.x509.Extension)localObject2).getId(), localObject2);
/*     */           }
/*     */         } else {
/* 698 */           this.singleExtensions = Collections.emptyMap();
/*     */         }
/*     */       } else {
/* 701 */         this.singleExtensions = Collections.emptyMap();
/*     */       }
/*     */ 
/* 704 */       long l = System.currentTimeMillis();
/* 705 */       Object localObject2 = new Date(l + OCSPResponse.MAX_CLOCK_SKEW);
/* 706 */       Date localDate = new Date(l - OCSPResponse.MAX_CLOCK_SKEW);
/* 707 */       if (OCSPResponse.DEBUG != null) {
/* 708 */         String str = "";
/* 709 */         if (this.nextUpdate != null) {
/* 710 */           str = " until " + this.nextUpdate;
/*     */         }
/* 712 */         OCSPResponse.DEBUG.println("Response's validity interval is from " + this.thisUpdate + str);
/*     */       }
/*     */ 
/* 716 */       if (((this.thisUpdate != null) && (((Date)localObject2).before(this.thisUpdate))) || ((this.nextUpdate != null) && (localDate.after(this.nextUpdate))))
/*     */       {
/* 719 */         if (OCSPResponse.DEBUG != null) {
/* 720 */           OCSPResponse.DEBUG.println("Response is unreliable: its validity interval is out-of-date");
/*     */         }
/*     */ 
/* 723 */         throw new IOException("Response is unreliable: its validity interval is out-of-date");
/*     */       }
/*     */     }
/*     */ 
/*     */     public OCSP.RevocationStatus.CertStatus getCertStatus()
/*     */     {
/* 732 */       return this.certStatus;
/*     */     }
/*     */ 
/*     */     private CertId getCertId() {
/* 736 */       return this.certId;
/*     */     }
/*     */ 
/*     */     public Date getRevocationTime() {
/* 740 */       return (Date)this.revocationTime.clone();
/*     */     }
/*     */ 
/*     */     public CRLReason getRevocationReason() {
/* 744 */       return this.revocationReason;
/*     */     }
/*     */ 
/*     */     public Map<String, java.security.cert.Extension> getSingleExtensions()
/*     */     {
/* 749 */       return Collections.unmodifiableMap(this.singleExtensions);
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 756 */       StringBuilder localStringBuilder = new StringBuilder();
/* 757 */       localStringBuilder.append("SingleResponse:  \n");
/* 758 */       localStringBuilder.append(this.certId);
/* 759 */       localStringBuilder.append("\nCertStatus: " + this.certStatus + "\n");
/* 760 */       if (this.certStatus == OCSP.RevocationStatus.CertStatus.REVOKED) {
/* 761 */         localStringBuilder.append("revocationTime is " + this.revocationTime + "\n");
/* 762 */         localStringBuilder.append("revocationReason is " + this.revocationReason + "\n");
/*     */       }
/* 764 */       localStringBuilder.append("thisUpdate is " + this.thisUpdate + "\n");
/* 765 */       if (this.nextUpdate != null) {
/* 766 */         localStringBuilder.append("nextUpdate is " + this.nextUpdate + "\n");
/*     */       }
/* 768 */       return localStringBuilder.toString();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.OCSPResponse
 * JD-Core Version:    0.6.2
 */