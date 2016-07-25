/*     */ package sun.security.provider.certpath;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PublicKey;
/*     */ import java.security.cert.CRL;
/*     */ import java.security.cert.CRLException;
/*     */ import java.security.cert.CRLReason;
/*     */ import java.security.cert.CertPath;
/*     */ import java.security.cert.CertPathBuilder;
/*     */ import java.security.cert.CertPathBuilderException;
/*     */ import java.security.cert.CertPathValidatorException;
/*     */ import java.security.cert.CertPathValidatorException.BasicReason;
/*     */ import java.security.cert.CertStore;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateRevokedException;
/*     */ import java.security.cert.CollectionCertStoreParameters;
/*     */ import java.security.cert.PKIXBuilderParameters;
/*     */ import java.security.cert.PKIXCertPathBuilderResult;
/*     */ import java.security.cert.PKIXCertPathChecker;
/*     */ import java.security.cert.PKIXParameters;
/*     */ import java.security.cert.TrustAnchor;
/*     */ import java.security.cert.X509CRL;
/*     */ import java.security.cert.X509CRLEntry;
/*     */ import java.security.cert.X509CRLSelector;
/*     */ import java.security.cert.X509CertSelector;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.security.interfaces.DSAPublicKey;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ import sun.security.x509.AccessDescription;
/*     */ import sun.security.x509.AuthorityInfoAccessExtension;
/*     */ import sun.security.x509.CRLDistributionPointsExtension;
/*     */ import sun.security.x509.DistributionPoint;
/*     */ import sun.security.x509.GeneralName;
/*     */ import sun.security.x509.GeneralNameInterface;
/*     */ import sun.security.x509.GeneralNames;
/*     */ import sun.security.x509.PKIXExtensions;
/*     */ import sun.security.x509.X500Name;
/*     */ import sun.security.x509.X509CRLEntryImpl;
/*     */ import sun.security.x509.X509CertImpl;
/*     */ 
/*     */ class CrlRevocationChecker extends PKIXCertPathChecker
/*     */ {
/*  88 */   private static final Debug debug = Debug.getInstance("certpath");
/*     */   private final TrustAnchor mAnchor;
/*     */   private final List<CertStore> mStores;
/*     */   private final String mSigProvider;
/*     */   private final Date mCurrentTime;
/*     */   private PublicKey mPrevPubKey;
/*     */   private boolean mCRLSignFlag;
/*     */   private HashSet<X509CRL> mPossibleCRLs;
/*     */   private HashSet<X509CRL> mApprovedCRLs;
/*     */   private final PKIXParameters mParams;
/*  98 */   private static final boolean[] mCrlSignUsage = { false, false, false, false, false, false, true };
/*     */ 
/* 100 */   private static final boolean[] ALL_REASONS = { true, true, true, true, true, true, true, true, true };
/*     */ 
/* 102 */   private boolean mOnlyEECert = false;
/*     */   private static final long MAX_CLOCK_SKEW = 900000L;
/*     */ 
/*     */   CrlRevocationChecker(TrustAnchor paramTrustAnchor, PKIXParameters paramPKIXParameters)
/*     */     throws CertPathValidatorException
/*     */   {
/* 118 */     this(paramTrustAnchor, paramPKIXParameters, null);
/*     */   }
/*     */ 
/*     */   CrlRevocationChecker(TrustAnchor paramTrustAnchor, PKIXParameters paramPKIXParameters, Collection<X509Certificate> paramCollection)
/*     */     throws CertPathValidatorException
/*     */   {
/* 137 */     this(paramTrustAnchor, paramPKIXParameters, paramCollection, false);
/*     */   }
/*     */ 
/*     */   CrlRevocationChecker(TrustAnchor paramTrustAnchor, PKIXParameters paramPKIXParameters, Collection<X509Certificate> paramCollection, boolean paramBoolean)
/*     */     throws CertPathValidatorException
/*     */   {
/* 143 */     this.mAnchor = paramTrustAnchor;
/* 144 */     this.mParams = paramPKIXParameters;
/* 145 */     this.mStores = new ArrayList(paramPKIXParameters.getCertStores());
/* 146 */     this.mSigProvider = paramPKIXParameters.getSigProvider();
/* 147 */     if (paramCollection != null) {
/*     */       try {
/* 149 */         this.mStores.add(CertStore.getInstance("Collection", new CollectionCertStoreParameters(paramCollection)));
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/* 154 */         if (debug != null) {
/* 155 */           debug.println("CrlRevocationChecker: error creating Collection CertStore: " + localException);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 160 */     Date localDate = paramPKIXParameters.getDate();
/* 161 */     this.mCurrentTime = (localDate != null ? localDate : new Date());
/* 162 */     this.mOnlyEECert = paramBoolean;
/* 163 */     init(false);
/*     */   }
/*     */ 
/*     */   public void init(boolean paramBoolean)
/*     */     throws CertPathValidatorException
/*     */   {
/* 172 */     if (!paramBoolean) {
/* 173 */       if (this.mAnchor != null) {
/* 174 */         if (this.mAnchor.getCAPublicKey() != null)
/* 175 */           this.mPrevPubKey = this.mAnchor.getCAPublicKey();
/*     */         else
/* 177 */           this.mPrevPubKey = this.mAnchor.getTrustedCert().getPublicKey();
/*     */       }
/*     */       else {
/* 180 */         this.mPrevPubKey = null;
/*     */       }
/* 182 */       this.mCRLSignFlag = true;
/*     */     } else {
/* 184 */       throw new CertPathValidatorException("forward checking not supported");
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isForwardCheckingSupported()
/*     */   {
/* 190 */     return false;
/*     */   }
/*     */ 
/*     */   public Set<String> getSupportedExtensions() {
/* 194 */     return null;
/*     */   }
/*     */ 
/*     */   public void check(Certificate paramCertificate, Collection<String> paramCollection)
/*     */     throws CertPathValidatorException
/*     */   {
/* 210 */     X509Certificate localX509Certificate = (X509Certificate)paramCertificate;
/* 211 */     verifyRevocationStatus(localX509Certificate, this.mPrevPubKey, this.mCRLSignFlag, true);
/*     */ 
/* 214 */     PublicKey localPublicKey = localX509Certificate.getPublicKey();
/* 215 */     if (((localPublicKey instanceof DSAPublicKey)) && (((DSAPublicKey)localPublicKey).getParams() == null))
/*     */     {
/* 218 */       localPublicKey = BasicChecker.makeInheritedParamsKey(localPublicKey, this.mPrevPubKey);
/*     */     }
/* 220 */     this.mPrevPubKey = localPublicKey;
/* 221 */     this.mCRLSignFlag = certCanSignCrl(localX509Certificate);
/*     */   }
/*     */ 
/*     */   public boolean check(X509Certificate paramX509Certificate, PublicKey paramPublicKey, boolean paramBoolean)
/*     */     throws CertPathValidatorException
/*     */   {
/* 241 */     verifyRevocationStatus(paramX509Certificate, paramPublicKey, paramBoolean, true);
/* 242 */     return certCanSignCrl(paramX509Certificate);
/*     */   }
/*     */ 
/*     */   static boolean certCanSignCrl(X509Certificate paramX509Certificate)
/*     */   {
/* 256 */     boolean[] arrayOfBoolean = paramX509Certificate.getKeyUsage();
/* 257 */     if (arrayOfBoolean != null) {
/* 258 */       return arrayOfBoolean[6];
/*     */     }
/* 260 */     return false;
/*     */   }
/*     */ 
/*     */   private void verifyRevocationStatus(X509Certificate paramX509Certificate, PublicKey paramPublicKey, boolean paramBoolean1, boolean paramBoolean2)
/*     */     throws CertPathValidatorException
/*     */   {
/* 270 */     verifyRevocationStatus(paramX509Certificate, paramPublicKey, paramBoolean1, paramBoolean2, null, this.mParams.getTrustAnchors());
/*     */   }
/*     */ 
/*     */   private void verifyRevocationStatus(X509Certificate paramX509Certificate, PublicKey paramPublicKey, boolean paramBoolean1, boolean paramBoolean2, Set<X509Certificate> paramSet, Set<TrustAnchor> paramSet1)
/*     */     throws CertPathValidatorException
/*     */   {
/* 289 */     String str = "revocation status";
/* 290 */     if (debug != null) {
/* 291 */       debug.println("CrlRevocationChecker.verifyRevocationStatus() ---checking " + str + "...");
/*     */     }
/*     */ 
/* 295 */     if ((this.mOnlyEECert) && (paramX509Certificate.getBasicConstraints() != -1)) {
/* 296 */       if (debug != null) {
/* 297 */         debug.println("Skipping revocation check, not end entity cert");
/*     */       }
/* 299 */       return;
/*     */     }
/*     */ 
/* 305 */     if ((paramSet != null) && (paramSet.contains(paramX509Certificate))) {
/* 306 */       if (debug != null) {
/* 307 */         debug.println("CrlRevocationChecker.verifyRevocationStatus() circular dependency");
/*     */       }
/*     */ 
/* 310 */       throw new CertPathValidatorException("Could not determine revocation status", null, null, -1, CertPathValidatorException.BasicReason.UNDETERMINED_REVOCATION_STATUS);
/* 316 */     }
/*     */ this.mPossibleCRLs = new HashSet();
/* 317 */     this.mApprovedCRLs = new HashSet();
/* 318 */     boolean[] arrayOfBoolean = new boolean[9];
/*     */     Object localObject4;
/*     */     Object localObject5;
/*     */     try { X509CRLSelector localX509CRLSelector = new X509CRLSelector();
/* 322 */       localX509CRLSelector.setCertificateChecking(paramX509Certificate);
/* 323 */       CertPathHelper.setDateAndTime(localX509CRLSelector, this.mCurrentTime, 900000L);
/*     */ 
/* 325 */       for (localObject2 = this.mStores.iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (CertStore)((Iterator)localObject2).next();
/* 326 */         for (localObject4 = ((CertStore)localObject3).getCRLs(localX509CRLSelector).iterator(); ((Iterator)localObject4).hasNext(); ) { localObject5 = (CRL)((Iterator)localObject4).next();
/* 327 */           this.mPossibleCRLs.add((X509CRL)localObject5);
/*     */         }
/*     */       }
/*     */ 
/* 331 */       this.mApprovedCRLs.addAll(DistributionPointFetcher.getCRLs(localX509CRLSelector, paramBoolean1, paramPublicKey, this.mSigProvider, this.mStores, arrayOfBoolean, paramSet1, this.mParams.getDate()));
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 335 */       if (debug != null) {
/* 336 */         debug.println("CrlRevocationChecker.verifyRevocationStatus() unexpected exception: " + localException.getMessage());
/*     */       }
/*     */ 
/* 339 */       throw new CertPathValidatorException(localException);
/*     */     }
/*     */ 
/* 342 */     if (debug != null) {
/* 343 */       debug.println("CrlRevocationChecker.verifyRevocationStatus() crls.size() = " + this.mPossibleCRLs.size());
/*     */     }
/*     */ 
/* 346 */     if (!this.mPossibleCRLs.isEmpty())
/*     */     {
/* 349 */       this.mApprovedCRLs.addAll(verifyPossibleCRLs(this.mPossibleCRLs, paramX509Certificate, paramBoolean1, paramPublicKey, arrayOfBoolean, paramSet1));
/*     */     }
/*     */ 
/* 352 */     if (debug != null) {
/* 353 */       debug.println("CrlRevocationChecker.verifyRevocationStatus() approved crls.size() = " + this.mApprovedCRLs.size());
/*     */     }
/*     */ 
/* 359 */     if ((this.mApprovedCRLs.isEmpty()) || (!Arrays.equals(arrayOfBoolean, ALL_REASONS)))
/*     */     {
/* 361 */       if (paramBoolean2) {
/* 362 */         verifyWithSeparateSigningKey(paramX509Certificate, paramPublicKey, paramBoolean1, paramSet);
/*     */ 
/* 364 */         return;
/*     */       }
/* 366 */       throw new CertPathValidatorException("Could not determine revocation status", null, null, -1, CertPathValidatorException.BasicReason.UNDETERMINED_REVOCATION_STATUS);
/*     */     }
/*     */ 
/* 373 */     if (debug != null) {
/* 374 */       localObject1 = paramX509Certificate.getSerialNumber();
/* 375 */       debug.println("CrlRevocationChecker.verifyRevocationStatus() starting the final sweep...");
/*     */ 
/* 377 */       debug.println("CrlRevocationChecker.verifyRevocationStatus cert SN: " + ((BigInteger)localObject1).toString());
/*     */     }
/*     */ 
/* 381 */     Object localObject1 = CRLReason.UNSPECIFIED;
/* 382 */     Object localObject2 = null;
/* 383 */     for (Object localObject3 = this.mApprovedCRLs.iterator(); ((Iterator)localObject3).hasNext(); ) { localObject4 = (X509CRL)((Iterator)localObject3).next();
/* 384 */       localObject5 = ((X509CRL)localObject4).getRevokedCertificate(paramX509Certificate);
/* 385 */       if (localObject5 != null) {
/*     */         try {
/* 387 */           localObject2 = X509CRLEntryImpl.toImpl((X509CRLEntry)localObject5);
/*     */         } catch (CRLException localCRLException) {
/* 389 */           throw new CertPathValidatorException(localCRLException);
/*     */         }
/* 391 */         if (debug != null) {
/* 392 */           debug.println("CrlRevocationChecker.verifyRevocationStatus CRL entry: " + ((X509CRLEntryImpl)localObject2).toString());
/*     */         }
/*     */ 
/* 401 */         Set localSet = ((X509CRLEntryImpl)localObject2).getCriticalExtensionOIDs();
/* 402 */         if ((localSet != null) && (!localSet.isEmpty()))
/*     */         {
/* 404 */           localSet.remove(PKIXExtensions.ReasonCode_Id.toString());
/*     */ 
/* 406 */           localSet.remove(PKIXExtensions.CertificateIssuer_Id.toString());
/*     */ 
/* 408 */           if (!localSet.isEmpty()) {
/* 409 */             if (debug != null) {
/* 410 */               debug.println("Unrecognized critical extension(s) in revoked CRL entry: " + localSet);
/*     */             }
/*     */ 
/* 414 */             throw new CertPathValidatorException("Could not determine revocation status", null, null, -1, CertPathValidatorException.BasicReason.UNDETERMINED_REVOCATION_STATUS);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 420 */         localObject1 = ((X509CRLEntryImpl)localObject2).getRevocationReason();
/* 421 */         if (localObject1 == null) {
/* 422 */           localObject1 = CRLReason.UNSPECIFIED;
/*     */         }
/* 424 */         CertificateRevokedException localCertificateRevokedException = new CertificateRevokedException(((X509CRLEntryImpl)localObject2).getRevocationDate(), (CRLReason)localObject1, ((X509CRL)localObject4).getIssuerX500Principal(), ((X509CRLEntryImpl)localObject2).getExtensions());
/*     */ 
/* 427 */         throw new CertPathValidatorException(localCertificateRevokedException.getMessage(), localCertificateRevokedException, null, -1, CertPathValidatorException.BasicReason.REVOKED);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void verifyWithSeparateSigningKey(X509Certificate paramX509Certificate, PublicKey paramPublicKey, boolean paramBoolean, Set<X509Certificate> paramSet)
/*     */     throws CertPathValidatorException
/*     */   {
/* 458 */     String str = "revocation status";
/* 459 */     if (debug != null) {
/* 460 */       debug.println("CrlRevocationChecker.verifyWithSeparateSigningKey() ---checking " + str + "...");
/*     */     }
/*     */ 
/* 468 */     if ((paramSet != null) && (paramSet.contains(paramX509Certificate))) {
/* 469 */       if (debug != null) {
/* 470 */         debug.println("CrlRevocationChecker.verifyWithSeparateSigningKey() circular dependency");
/*     */       }
/*     */ 
/* 474 */       throw new CertPathValidatorException("Could not determine revocation status", null, null, -1, CertPathValidatorException.BasicReason.UNDETERMINED_REVOCATION_STATUS);
/*     */     }
/*     */ 
/* 481 */     if (!paramBoolean) {
/* 482 */       paramPublicKey = null;
/*     */     }
/*     */ 
/* 487 */     buildToNewKey(paramX509Certificate, paramPublicKey, paramSet);
/*     */   }
/*     */ 
/*     */   private void buildToNewKey(X509Certificate paramX509Certificate, PublicKey paramPublicKey, Set<X509Certificate> paramSet)
/*     */     throws CertPathValidatorException
/*     */   {
/* 508 */     if (debug != null) {
/* 509 */       debug.println("CrlRevocationChecker.buildToNewKey() starting work");
/*     */     }
/*     */ 
/* 512 */     HashSet localHashSet = new HashSet();
/* 513 */     if (paramPublicKey != null) {
/* 514 */       localHashSet.add(paramPublicKey);
/*     */     }
/* 516 */     RejectKeySelector localRejectKeySelector = new RejectKeySelector(localHashSet);
/* 517 */     localRejectKeySelector.setSubject(paramX509Certificate.getIssuerX500Principal());
/* 518 */     localRejectKeySelector.setKeyUsage(mCrlSignUsage);
/*     */ 
/* 520 */     Set localSet = this.mAnchor == null ? this.mParams.getTrustAnchors() : Collections.singleton(this.mAnchor);
/*     */     PKIXBuilderParameters localPKIXBuilderParameters;
/* 525 */     if ((this.mParams instanceof PKIXBuilderParameters)) {
/* 526 */       localPKIXBuilderParameters = (PKIXBuilderParameters)this.mParams.clone();
/* 527 */       localPKIXBuilderParameters.setTargetCertConstraints(localRejectKeySelector);
/*     */ 
/* 530 */       localPKIXBuilderParameters.setPolicyQualifiersRejected(true);
/*     */       try {
/* 532 */         localPKIXBuilderParameters.setTrustAnchors(localSet);
/*     */       } catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException1) {
/* 534 */         throw new RuntimeException(localInvalidAlgorithmParameterException1);
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */       try
/*     */       {
/* 543 */         localPKIXBuilderParameters = new PKIXBuilderParameters(localSet, localRejectKeySelector);
/*     */       } catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException2) {
/* 545 */         throw new RuntimeException(localInvalidAlgorithmParameterException2);
/*     */       }
/* 547 */       localPKIXBuilderParameters.setInitialPolicies(this.mParams.getInitialPolicies());
/* 548 */       localPKIXBuilderParameters.setCertStores(this.mStores);
/* 549 */       localPKIXBuilderParameters.setExplicitPolicyRequired(this.mParams.isExplicitPolicyRequired());
/*     */ 
/* 551 */       localPKIXBuilderParameters.setPolicyMappingInhibited(this.mParams.isPolicyMappingInhibited());
/*     */ 
/* 553 */       localPKIXBuilderParameters.setAnyPolicyInhibited(this.mParams.isAnyPolicyInhibited());
/*     */ 
/* 557 */       localPKIXBuilderParameters.setDate(this.mParams.getDate());
/* 558 */       localPKIXBuilderParameters.setCertPathCheckers(this.mParams.getCertPathCheckers());
/* 559 */       localPKIXBuilderParameters.setSigProvider(this.mParams.getSigProvider());
/*     */     }
/*     */ 
/* 565 */     localPKIXBuilderParameters.setRevocationEnabled(false);
/*     */     Object localObject2;
/*     */     Object localObject3;
/* 568 */     if (Builder.USE_AIA == true) {
/* 569 */       localObject1 = null;
/*     */       try {
/* 571 */         localObject1 = X509CertImpl.toImpl(paramX509Certificate);
/*     */       }
/*     */       catch (CertificateException localCertificateException) {
/* 574 */         if (debug != null) {
/* 575 */           debug.println("CrlRevocationChecker.buildToNewKey: error decoding cert: " + localCertificateException);
/*     */         }
/*     */       }
/*     */ 
/* 579 */       AuthorityInfoAccessExtension localAuthorityInfoAccessExtension = null;
/* 580 */       if (localObject1 != null) {
/* 581 */         localAuthorityInfoAccessExtension = ((X509CertImpl)localObject1).getAuthorityInfoAccessExtension();
/*     */       }
/* 583 */       if (localAuthorityInfoAccessExtension != null) {
/* 584 */         localObject2 = localAuthorityInfoAccessExtension.getAccessDescriptions();
/* 585 */         if (localObject2 != null)
/* 586 */           for (localObject3 = ((List)localObject2).iterator(); ((Iterator)localObject3).hasNext(); ) { AccessDescription localAccessDescription = (AccessDescription)((Iterator)localObject3).next();
/* 587 */             localObject4 = URICertStore.getInstance(localAccessDescription);
/* 588 */             if (localObject4 != null) {
/* 589 */               if (debug != null) {
/* 590 */                 debug.println("adding AIAext CertStore");
/*     */               }
/* 592 */               localPKIXBuilderParameters.addCertStore((CertStore)localObject4);
/*     */             }
/*     */           }
/*     */       }
/*     */     }
/*     */     Object localObject4;
/* 599 */     Object localObject1 = null;
/*     */     try {
/* 601 */       localObject1 = CertPathBuilder.getInstance("PKIX");
/*     */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 603 */       throw new CertPathValidatorException(localNoSuchAlgorithmException);
/*     */     }
/*     */     try {
/*     */       while (true) {
/* 607 */         if (debug != null) {
/* 608 */           debug.println("CrlRevocationChecker.buildToNewKey() about to try build ...");
/*     */         }
/*     */ 
/* 611 */         PKIXCertPathBuilderResult localPKIXCertPathBuilderResult = (PKIXCertPathBuilderResult)((CertPathBuilder)localObject1).build(localPKIXBuilderParameters);
/*     */ 
/* 614 */         if (debug != null) {
/* 615 */           debug.println("CrlRevocationChecker.buildToNewKey() about to check revocation ...");
/*     */         }
/*     */ 
/* 620 */         if (paramSet == null) {
/* 621 */           paramSet = new HashSet();
/*     */         }
/* 623 */         paramSet.add(paramX509Certificate);
/* 624 */         localObject2 = localPKIXCertPathBuilderResult.getTrustAnchor();
/* 625 */         localObject3 = ((TrustAnchor)localObject2).getCAPublicKey();
/* 626 */         if (localObject3 == null) {
/* 627 */           localObject3 = ((TrustAnchor)localObject2).getTrustedCert().getPublicKey();
/*     */         }
/* 629 */         boolean bool = true;
/* 630 */         localObject4 = localPKIXCertPathBuilderResult.getCertPath().getCertificates();
/*     */         try
/*     */         {
/* 633 */           for (int i = ((List)localObject4).size() - 1; i >= 0; i--) {
/* 634 */             X509Certificate localX509Certificate = (X509Certificate)((List)localObject4).get(i);
/*     */ 
/* 636 */             if (debug != null) {
/* 637 */               debug.println("CrlRevocationChecker.buildToNewKey() index " + i + " checking " + localX509Certificate);
/*     */             }
/*     */ 
/* 640 */             verifyRevocationStatus(localX509Certificate, (PublicKey)localObject3, bool, true, paramSet, localSet);
/*     */ 
/* 642 */             bool = certCanSignCrl(localX509Certificate);
/* 643 */             localObject3 = localX509Certificate.getPublicKey();
/*     */           }
/*     */         }
/*     */         catch (CertPathValidatorException localCertPathValidatorException1) {
/* 647 */           localHashSet.add(localPKIXCertPathBuilderResult.getPublicKey());
/* 648 */         }continue;
/*     */ 
/* 651 */         if (debug != null) {
/* 652 */           debug.println("CrlRevocationChecker.buildToNewKey() got key " + localPKIXCertPathBuilderResult.getPublicKey());
/*     */         }
/*     */ 
/* 658 */         PublicKey localPublicKey = localPKIXCertPathBuilderResult.getPublicKey();
/*     */         try {
/* 660 */           verifyRevocationStatus(paramX509Certificate, localPublicKey, true, false);
/*     */ 
/* 662 */           return;
/*     */         }
/*     */         catch (CertPathValidatorException localCertPathValidatorException2) {
/* 665 */           if (localCertPathValidatorException2.getReason() == CertPathValidatorException.BasicReason.REVOKED) {
/* 666 */             throw localCertPathValidatorException2;
/*     */           }
/*     */ 
/* 671 */           localHashSet.add(localPublicKey);
/*     */         }
/*     */       } } catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException3) { throw new CertPathValidatorException(localInvalidAlgorithmParameterException3); } catch (CertPathBuilderException localCertPathBuilderException) {
/*     */     }
/* 675 */     throw new CertPathValidatorException("Could not determine revocation status", null, null, -1, CertPathValidatorException.BasicReason.UNDETERMINED_REVOCATION_STATUS);
/*     */   }
/*     */ 
/*     */   private Collection<X509CRL> verifyPossibleCRLs(Set<X509CRL> paramSet, X509Certificate paramX509Certificate, boolean paramBoolean, PublicKey paramPublicKey, boolean[] paramArrayOfBoolean, Set<TrustAnchor> paramSet1)
/*     */     throws CertPathValidatorException
/*     */   {
/*     */     try
/*     */     {
/* 759 */       X509CertImpl localX509CertImpl = X509CertImpl.toImpl(paramX509Certificate);
/* 760 */       if (debug != null) {
/* 761 */         debug.println("CRLRevocationChecker.verifyPossibleCRLs: Checking CRLDPs for " + localX509CertImpl.getSubjectX500Principal());
/*     */       }
/*     */ 
/* 765 */       CRLDistributionPointsExtension localCRLDistributionPointsExtension = localX509CertImpl.getCRLDistributionPointsExtension();
/*     */ 
/* 767 */       List localList = null;
/* 768 */       if (localCRLDistributionPointsExtension == null)
/*     */       {
/* 772 */         localObject1 = (X500Name)localX509CertImpl.getIssuerDN();
/* 773 */         localObject2 = new DistributionPoint(new GeneralNames().add(new GeneralName((GeneralNameInterface)localObject1)), null, null);
/*     */ 
/* 776 */         localList = Collections.singletonList(localObject2);
/*     */       } else {
/* 778 */         localList = (List)localCRLDistributionPointsExtension.get("points");
/*     */       }
/*     */ 
/* 781 */       Object localObject1 = new HashSet();
/* 782 */       Object localObject2 = localList.iterator();
/*     */       DistributionPoint localDistributionPoint;
/* 783 */       while ((((Iterator)localObject2).hasNext()) && (!Arrays.equals(paramArrayOfBoolean, ALL_REASONS))) {
/* 784 */         localDistributionPoint = (DistributionPoint)((Iterator)localObject2).next();
/* 785 */         for (X509CRL localX509CRL : paramSet) {
/* 786 */           if (DistributionPointFetcher.verifyCRL(localX509CertImpl, localDistributionPoint, localX509CRL, paramArrayOfBoolean, paramBoolean, paramPublicKey, this.mSigProvider, paramSet1, this.mStores, this.mParams.getDate()))
/*     */           {
/* 789 */             ((Set)localObject1).add(localX509CRL);
/*     */           }
/*     */         }
/*     */       }
/* 793 */       return localObject1;
/*     */     } catch (Exception localException) {
/* 795 */       if (debug != null) {
/* 796 */         debug.println("Exception while verifying CRL: " + localException.getMessage());
/* 797 */         localException.printStackTrace();
/*     */       }
/*     */     }
/* 799 */     return Collections.emptySet();
/*     */   }
/*     */ 
/*     */   private static class RejectKeySelector extends X509CertSelector
/*     */   {
/*     */     private final Set<PublicKey> badKeySet;
/*     */ 
/*     */     RejectKeySelector(Set<PublicKey> paramSet)
/*     */     {
/* 700 */       this.badKeySet = paramSet;
/*     */     }
/*     */ 
/*     */     public boolean match(Certificate paramCertificate)
/*     */     {
/* 711 */       if (!super.match(paramCertificate)) {
/* 712 */         return false;
/*     */       }
/* 714 */       if (this.badKeySet.contains(paramCertificate.getPublicKey())) {
/* 715 */         if (CrlRevocationChecker.debug != null)
/* 716 */           CrlRevocationChecker.debug.println("RejectCertSelector.match: bad key");
/* 717 */         return false;
/*     */       }
/*     */ 
/* 720 */       if (CrlRevocationChecker.debug != null)
/* 721 */         CrlRevocationChecker.debug.println("RejectCertSelector.match: returning true");
/* 722 */       return true;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 732 */       StringBuilder localStringBuilder = new StringBuilder();
/* 733 */       localStringBuilder.append("RejectCertSelector: [\n");
/* 734 */       localStringBuilder.append(super.toString());
/* 735 */       localStringBuilder.append(this.badKeySet);
/* 736 */       localStringBuilder.append("]");
/* 737 */       return localStringBuilder.toString();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.CrlRevocationChecker
 * JD-Core Version:    0.6.2
 */