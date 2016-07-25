/*     */ package sun.security.provider.certpath;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.PublicKey;
/*     */ import java.security.cert.CertPathValidatorException;
/*     */ import java.security.cert.CertStore;
/*     */ import java.security.cert.CertStoreException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.PKIXBuilderParameters;
/*     */ import java.security.cert.PKIXCertPathChecker;
/*     */ import java.security.cert.PKIXReason;
/*     */ import java.security.cert.TrustAnchor;
/*     */ import java.security.cert.X509CertSelector;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ import sun.security.x509.AccessDescription;
/*     */ import sun.security.x509.AuthorityInfoAccessExtension;
/*     */ import sun.security.x509.AuthorityKeyIdentifierExtension;
/*     */ import sun.security.x509.PKIXExtensions;
/*     */ import sun.security.x509.X500Name;
/*     */ import sun.security.x509.X509CertImpl;
/*     */ 
/*     */ class ForwardBuilder extends Builder
/*     */ {
/*  75 */   private static final Debug debug = Debug.getInstance("certpath");
/*     */   private final Set<X509Certificate> trustedCerts;
/*     */   private final Set<X500Principal> trustedSubjectDNs;
/*     */   private final Set<TrustAnchor> trustAnchors;
/*     */   private X509CertSelector eeSelector;
/*     */   private AdaptableX509CertSelector caSelector;
/*     */   private X509CertSelector caTargetSelector;
/*     */   TrustAnchor trustAnchor;
/*     */   private Comparator<X509Certificate> comparator;
/*  84 */   private boolean searchAllCertStores = true;
/*  85 */   private boolean onlyEECert = false;
/*     */ 
/*     */   ForwardBuilder(PKIXBuilderParameters paramPKIXBuilderParameters, X500Principal paramX500Principal, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/*  96 */     super(paramPKIXBuilderParameters, paramX500Principal);
/*     */ 
/*  99 */     this.trustAnchors = paramPKIXBuilderParameters.getTrustAnchors();
/* 100 */     this.trustedCerts = new HashSet(this.trustAnchors.size());
/* 101 */     this.trustedSubjectDNs = new HashSet(this.trustAnchors.size());
/* 102 */     for (TrustAnchor localTrustAnchor : this.trustAnchors) {
/* 103 */       X509Certificate localX509Certificate = localTrustAnchor.getTrustedCert();
/* 104 */       if (localX509Certificate != null) {
/* 105 */         this.trustedCerts.add(localX509Certificate);
/* 106 */         this.trustedSubjectDNs.add(localX509Certificate.getSubjectX500Principal());
/*     */       } else {
/* 108 */         this.trustedSubjectDNs.add(localTrustAnchor.getCA());
/*     */       }
/*     */     }
/* 111 */     this.comparator = new PKIXCertComparator(this.trustedSubjectDNs);
/* 112 */     this.searchAllCertStores = paramBoolean1;
/* 113 */     this.onlyEECert = paramBoolean2;
/*     */   }
/*     */ 
/*     */   Collection<X509Certificate> getMatchingCerts(State paramState, List<CertStore> paramList)
/*     */     throws CertStoreException, CertificateException, IOException
/*     */   {
/* 129 */     if (debug != null) {
/* 130 */       debug.println("ForwardBuilder.getMatchingCerts()...");
/*     */     }
/*     */ 
/* 133 */     ForwardState localForwardState = (ForwardState)paramState;
/*     */ 
/* 140 */     TreeSet localTreeSet = new TreeSet(this.comparator);
/*     */ 
/* 145 */     if (localForwardState.isInitial()) {
/* 146 */       getMatchingEECerts(localForwardState, paramList, localTreeSet);
/*     */     }
/* 148 */     getMatchingCACerts(localForwardState, paramList, localTreeSet);
/*     */ 
/* 150 */     return localTreeSet;
/*     */   }
/*     */ 
/*     */   private void getMatchingEECerts(ForwardState paramForwardState, List<CertStore> paramList, Collection<X509Certificate> paramCollection)
/*     */     throws IOException
/*     */   {
/* 161 */     if (debug != null) {
/* 162 */       debug.println("ForwardBuilder.getMatchingEECerts()...");
/*     */     }
/*     */ 
/* 172 */     if (this.eeSelector == null) {
/* 173 */       this.eeSelector = ((X509CertSelector)this.targetCertConstraints.clone());
/*     */ 
/* 178 */       this.eeSelector.setCertificateValid(this.date);
/*     */ 
/* 183 */       if (this.buildParams.isExplicitPolicyRequired()) {
/* 184 */         this.eeSelector.setPolicy(getMatchingPolicies());
/*     */       }
/*     */ 
/* 189 */       this.eeSelector.setBasicConstraints(-2);
/*     */     }
/*     */ 
/* 193 */     addMatchingCerts(this.eeSelector, paramList, paramCollection, this.searchAllCertStores);
/*     */   }
/*     */ 
/*     */   private void getMatchingCACerts(ForwardState paramForwardState, List<CertStore> paramList, Collection<X509Certificate> paramCollection)
/*     */     throws IOException
/*     */   {
/* 204 */     if (debug != null) {
/* 205 */       debug.println("ForwardBuilder.getMatchingCACerts()...");
/*     */     }
/* 207 */     int i = paramCollection.size();
/*     */ 
/* 213 */     Object localObject1 = null;
/*     */ 
/* 215 */     if (paramForwardState.isInitial()) {
/* 216 */       if (this.targetCertConstraints.getBasicConstraints() == -2)
/*     */       {
/* 218 */         return;
/*     */       }
/*     */ 
/* 224 */       if (debug != null) {
/* 225 */         debug.println("ForwardBuilder.getMatchingCACerts(): ca is target");
/*     */       }
/*     */ 
/* 228 */       if (this.caTargetSelector == null) {
/* 229 */         this.caTargetSelector = ((X509CertSelector)this.targetCertConstraints.clone());
/*     */ 
/* 242 */         if (this.buildParams.isExplicitPolicyRequired()) {
/* 243 */           this.caTargetSelector.setPolicy(getMatchingPolicies());
/*     */         }
/*     */       }
/* 246 */       localObject1 = this.caTargetSelector;
/*     */     }
/*     */     else {
/* 249 */       if (this.caSelector == null) {
/* 250 */         this.caSelector = new AdaptableX509CertSelector();
/*     */ 
/* 262 */         if (this.buildParams.isExplicitPolicyRequired()) {
/* 263 */           this.caSelector.setPolicy(getMatchingPolicies());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 269 */       this.caSelector.setSubject(paramForwardState.issuerDN);
/*     */ 
/* 276 */       CertPathHelper.setPathToNames(this.caSelector, paramForwardState.subjectNamesTraversed);
/*     */ 
/* 283 */       localObject2 = paramForwardState.cert.getAuthorityKeyIdentifierExtension();
/*     */ 
/* 285 */       this.caSelector.parseAuthorityKeyIdentifierExtension((AuthorityKeyIdentifierExtension)localObject2);
/*     */ 
/* 290 */       this.caSelector.setValidityPeriod(paramForwardState.cert.getNotBefore(), paramForwardState.cert.getNotAfter());
/*     */ 
/* 293 */       localObject1 = this.caSelector;
/*     */     }
/*     */ 
/* 302 */     ((X509CertSelector)localObject1).setBasicConstraints(-1);
/*     */ 
/* 304 */     for (Object localObject2 = this.trustedCerts.iterator(); ((Iterator)localObject2).hasNext(); ) { X509Certificate localX509Certificate = (X509Certificate)((Iterator)localObject2).next();
/* 305 */       if (((X509CertSelector)localObject1).match(localX509Certificate)) {
/* 306 */         if (debug != null) {
/* 307 */           debug.println("ForwardBuilder.getMatchingCACerts: found matching trust anchor");
/*     */         }
/*     */ 
/* 310 */         if ((paramCollection.add(localX509Certificate)) && (!this.searchAllCertStores)) {
/* 311 */           return;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 320 */     ((X509CertSelector)localObject1).setCertificateValid(this.date);
/*     */ 
/* 326 */     ((X509CertSelector)localObject1).setBasicConstraints(paramForwardState.traversedCACerts);
/*     */ 
/* 335 */     if ((paramForwardState.isInitial()) || (this.buildParams.getMaxPathLength() == -1) || (this.buildParams.getMaxPathLength() > paramForwardState.traversedCACerts))
/*     */     {
/* 339 */       if ((addMatchingCerts((X509CertSelector)localObject1, paramList, paramCollection, this.searchAllCertStores)) && (!this.searchAllCertStores))
/*     */       {
/* 341 */         return;
/*     */       }
/*     */     }
/*     */ 
/* 345 */     if ((!paramForwardState.isInitial()) && (Builder.USE_AIA))
/*     */     {
/* 347 */       localObject2 = paramForwardState.cert.getAuthorityInfoAccessExtension();
/*     */ 
/* 349 */       if (localObject2 != null) {
/* 350 */         getCerts((AuthorityInfoAccessExtension)localObject2, paramCollection);
/*     */       }
/*     */     }
/*     */ 
/* 354 */     if (debug != null) {
/* 355 */       int j = paramCollection.size() - i;
/* 356 */       debug.println("ForwardBuilder.getMatchingCACerts: found " + j + " CA certs");
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean getCerts(AuthorityInfoAccessExtension paramAuthorityInfoAccessExtension, Collection<X509Certificate> paramCollection)
/*     */   {
/* 367 */     if (!Builder.USE_AIA) {
/* 368 */       return false;
/*     */     }
/* 370 */     List localList = paramAuthorityInfoAccessExtension.getAccessDescriptions();
/* 371 */     if ((localList == null) || (localList.isEmpty())) {
/* 372 */       return false;
/*     */     }
/*     */ 
/* 375 */     boolean bool = false;
/* 376 */     for (AccessDescription localAccessDescription : localList) {
/* 377 */       CertStore localCertStore = URICertStore.getInstance(localAccessDescription);
/* 378 */       if (localCertStore != null) {
/*     */         try {
/* 380 */           if (paramCollection.addAll(localCertStore.getCertificates(this.caSelector)))
/*     */           {
/* 382 */             bool = true;
/* 383 */             if (!this.searchAllCertStores)
/* 384 */               return true;
/*     */           }
/*     */         }
/*     */         catch (CertStoreException localCertStoreException) {
/* 388 */           if (debug != null) {
/* 389 */             debug.println("exception getting certs from CertStore:");
/* 390 */             localCertStoreException.printStackTrace();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 396 */     return bool;
/*     */   }
/*     */ 
/*     */   void verifyCert(X509Certificate paramX509Certificate, State paramState, List<X509Certificate> paramList)
/*     */     throws GeneralSecurityException
/*     */   {
/* 666 */     if (debug != null) {
/* 667 */       debug.println("ForwardBuilder.verifyCert(SN: " + Debug.toHexString(paramX509Certificate.getSerialNumber()) + "\n  Issuer: " + paramX509Certificate.getIssuerX500Principal() + ")" + "\n  Subject: " + paramX509Certificate.getSubjectX500Principal() + ")");
/*     */     }
/*     */ 
/* 673 */     ForwardState localForwardState = (ForwardState)paramState;
/*     */ 
/* 676 */     localForwardState.untrustedChecker.check(paramX509Certificate, Collections.emptySet());
/*     */     Object localObject1;
/* 685 */     if (paramList != null) {
/* 686 */       bool = false;
/* 687 */       for (localObject1 = paramList.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (X509Certificate)((Iterator)localObject1).next();
/* 688 */         localObject3 = X509CertImpl.toImpl((X509Certificate)localObject2);
/* 689 */         localObject4 = ((X509CertImpl)localObject3).getPolicyMappingsExtension();
/*     */ 
/* 691 */         if (localObject4 != null) {
/* 692 */           bool = true;
/*     */         }
/* 694 */         if (debug != null) {
/* 695 */           debug.println("policyMappingFound = " + bool);
/*     */         }
/* 697 */         if ((paramX509Certificate.equals(localObject2)) && (
/* 698 */           (this.buildParams.isPolicyMappingInhibited()) || (!bool)))
/*     */         {
/* 700 */           if (debug != null) {
/* 701 */             debug.println("loop detected!!");
/*     */           }
/* 703 */           throw new CertPathValidatorException("loop detected");
/*     */         }
/*     */       }
/*     */     }
/*     */     Object localObject2;
/*     */     Object localObject3;
/*     */     Object localObject4;
/* 710 */     boolean bool = this.trustedCerts.contains(paramX509Certificate);
/*     */ 
/* 713 */     if (!bool)
/*     */     {
/* 719 */       localObject1 = paramX509Certificate.getCriticalExtensionOIDs();
/* 720 */       if (localObject1 == null) {
/* 721 */         localObject1 = Collections.emptySet();
/*     */       }
/* 723 */       for (localObject2 = localForwardState.forwardCheckers.iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (PKIXCertPathChecker)((Iterator)localObject2).next();
/* 724 */         ((PKIXCertPathChecker)localObject3).check(paramX509Certificate, (Collection)localObject1);
/*     */       }
/*     */ 
/* 733 */       for (localObject2 = this.buildParams.getCertPathCheckers().iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (PKIXCertPathChecker)((Iterator)localObject2).next();
/* 734 */         if (!((PKIXCertPathChecker)localObject3).isForwardCheckingSupported()) {
/* 735 */           localObject4 = ((PKIXCertPathChecker)localObject3).getSupportedExtensions();
/* 736 */           if (localObject4 != null) {
/* 737 */             ((Set)localObject1).removeAll((Collection)localObject4);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 746 */       if (!((Set)localObject1).isEmpty()) {
/* 747 */         ((Set)localObject1).remove(PKIXExtensions.BasicConstraints_Id.toString());
/*     */ 
/* 749 */         ((Set)localObject1).remove(PKIXExtensions.NameConstraints_Id.toString());
/*     */ 
/* 751 */         ((Set)localObject1).remove(PKIXExtensions.CertificatePolicies_Id.toString());
/*     */ 
/* 753 */         ((Set)localObject1).remove(PKIXExtensions.PolicyMappings_Id.toString());
/*     */ 
/* 755 */         ((Set)localObject1).remove(PKIXExtensions.PolicyConstraints_Id.toString());
/*     */ 
/* 757 */         ((Set)localObject1).remove(PKIXExtensions.InhibitAnyPolicy_Id.toString());
/*     */ 
/* 759 */         ((Set)localObject1).remove(PKIXExtensions.SubjectAlternativeName_Id.toString());
/*     */ 
/* 761 */         ((Set)localObject1).remove(PKIXExtensions.KeyUsage_Id.toString());
/* 762 */         ((Set)localObject1).remove(PKIXExtensions.ExtendedKeyUsage_Id.toString());
/*     */ 
/* 765 */         if (!((Set)localObject1).isEmpty()) {
/* 766 */           throw new CertPathValidatorException("Unrecognized critical extension(s)", null, null, -1, PKIXReason.UNRECOGNIZED_CRIT_EXT);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 776 */     if (localForwardState.isInitial()) {
/* 777 */       return;
/*     */     }
/*     */ 
/* 781 */     if (!bool)
/*     */     {
/* 783 */       if (paramX509Certificate.getBasicConstraints() == -1) {
/* 784 */         throw new CertificateException("cert is NOT a CA cert");
/*     */       }
/*     */ 
/* 790 */       KeyChecker.verifyCAKeyUsage(paramX509Certificate);
/*     */     }
/*     */ 
/* 803 */     if (this.buildParams.isRevocationEnabled())
/*     */     {
/* 806 */       if (CrlRevocationChecker.certCanSignCrl(paramX509Certificate))
/*     */       {
/* 809 */         if (!localForwardState.keyParamsNeeded())
/*     */         {
/* 813 */           localForwardState.crlChecker.check(localForwardState.cert, paramX509Certificate.getPublicKey(), true);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 823 */     if (!localForwardState.keyParamsNeeded())
/* 824 */       localForwardState.cert.verify(paramX509Certificate.getPublicKey(), this.buildParams.getSigProvider());
/*     */   }
/*     */ 
/*     */   boolean isPathCompleted(X509Certificate paramX509Certificate)
/*     */   {
/* 842 */     for (TrustAnchor localTrustAnchor : this.trustAnchors)
/* 843 */       if (localTrustAnchor.getTrustedCert() != null) {
/* 844 */         if (paramX509Certificate.equals(localTrustAnchor.getTrustedCert())) {
/* 845 */           this.trustAnchor = localTrustAnchor;
/* 846 */           return true;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 851 */         Object localObject = localTrustAnchor.getCA();
/* 852 */         PublicKey localPublicKey = localTrustAnchor.getCAPublicKey();
/*     */ 
/* 854 */         if ((localObject != null) && (localPublicKey != null) && (((X500Principal)localObject).equals(paramX509Certificate.getSubjectX500Principal())))
/*     */         {
/* 856 */           if (localPublicKey.equals(paramX509Certificate.getPublicKey()))
/*     */           {
/* 858 */             this.trustAnchor = localTrustAnchor;
/* 859 */             return true;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 865 */         if ((localObject != null) && (((X500Principal)localObject).equals(paramX509Certificate.getIssuerX500Principal())))
/*     */         {
/* 872 */           if (this.buildParams.isRevocationEnabled()) {
/*     */             try {
/* 874 */               localObject = new CrlRevocationChecker(localTrustAnchor, this.buildParams, null, this.onlyEECert);
/*     */ 
/* 876 */               ((CrlRevocationChecker)localObject).check(paramX509Certificate, localTrustAnchor.getCAPublicKey(), true);
/*     */             } catch (CertPathValidatorException localCertPathValidatorException) {
/* 878 */               if (debug != null) {
/* 879 */                 debug.println("ForwardBuilder.isPathCompleted() cpve");
/* 880 */                 localCertPathValidatorException.printStackTrace();
/*     */               }
/*     */ 
/*     */             }
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/*     */             try
/*     */             {
/* 894 */               paramX509Certificate.verify(localTrustAnchor.getCAPublicKey(), this.buildParams.getSigProvider());
/*     */             }
/*     */             catch (InvalidKeyException localInvalidKeyException) {
/* 897 */               if (debug != null) {
/* 898 */                 debug.println("ForwardBuilder.isPathCompleted() invalid DSA key found");
/*     */               }
/*     */ 
/* 901 */               continue;
/*     */             } catch (Exception localException) {
/* 903 */               if (debug != null) {
/* 904 */                 debug.println("ForwardBuilder.isPathCompleted() unexpected exception");
/*     */ 
/* 906 */                 localException.printStackTrace();
/*     */               }
/*     */             }
/* 908 */             continue;
/*     */ 
/* 911 */             this.trustAnchor = localTrustAnchor;
/* 912 */             return true;
/*     */           }
/*     */         }
/*     */       }
/* 915 */     return false;
/*     */   }
/*     */ 
/*     */   void addCertToPath(X509Certificate paramX509Certificate, LinkedList<X509Certificate> paramLinkedList)
/*     */   {
/* 925 */     paramLinkedList.addFirst(paramX509Certificate);
/*     */   }
/*     */ 
/*     */   void removeFinalCertFromPath(LinkedList<X509Certificate> paramLinkedList)
/*     */   {
/* 933 */     paramLinkedList.removeFirst();
/*     */   }
/*     */ 
/*     */   static class PKIXCertComparator
/*     */     implements Comparator<X509Certificate>
/*     */   {
/*     */     static final String METHOD_NME = "PKIXCertComparator.compare()";
/*     */     private final Set<X500Principal> trustedSubjectDNs;
/*     */ 
/*     */     PKIXCertComparator(Set<X500Principal> paramSet)
/*     */     {
/* 443 */       this.trustedSubjectDNs = paramSet;
/*     */     }
/*     */ 
/*     */     public int compare(X509Certificate paramX509Certificate1, X509Certificate paramX509Certificate2)
/*     */     {
/* 464 */       if (paramX509Certificate1.equals(paramX509Certificate2)) return 0;
/*     */ 
/* 466 */       X500Principal localX500Principal1 = paramX509Certificate1.getIssuerX500Principal();
/* 467 */       X500Principal localX500Principal2 = paramX509Certificate2.getIssuerX500Principal();
/* 468 */       X500Name localX500Name1 = X500Name.asX500Name(localX500Principal1);
/* 469 */       X500Name localX500Name2 = X500Name.asX500Name(localX500Principal2);
/*     */ 
/* 471 */       if (ForwardBuilder.debug != null) {
/* 472 */         ForwardBuilder.debug.println("PKIXCertComparator.compare() o1 Issuer:  " + localX500Principal1);
/* 473 */         ForwardBuilder.debug.println("PKIXCertComparator.compare() o2 Issuer:  " + localX500Principal2);
/*     */       }
/*     */ 
/* 479 */       if (ForwardBuilder.debug != null) {
/* 480 */         ForwardBuilder.debug.println("PKIXCertComparator.compare() MATCH TRUSTED SUBJECT TEST...");
/*     */       }
/*     */ 
/* 483 */       boolean bool1 = this.trustedSubjectDNs.contains(localX500Principal1);
/* 484 */       boolean bool2 = this.trustedSubjectDNs.contains(localX500Principal2);
/* 485 */       if (ForwardBuilder.debug != null) {
/* 486 */         ForwardBuilder.debug.println("PKIXCertComparator.compare() m1: " + bool1);
/* 487 */         ForwardBuilder.debug.println("PKIXCertComparator.compare() m2: " + bool2);
/*     */       }
/* 489 */       if ((bool1) && (bool2))
/* 490 */         return -1;
/* 491 */       if (bool1)
/* 492 */         return -1;
/* 493 */       if (bool2) {
/* 494 */         return 1;
/*     */       }
/*     */ 
/* 500 */       if (ForwardBuilder.debug != null) {
/* 501 */         ForwardBuilder.debug.println("PKIXCertComparator.compare() NAMING DESCENDANT TEST...");
/*     */       }
/* 503 */       for (Object localObject = this.trustedSubjectDNs.iterator(); ((Iterator)localObject).hasNext(); ) { localX500Principal3 = (X500Principal)((Iterator)localObject).next();
/* 504 */         localX500Name3 = X500Name.asX500Name(localX500Principal3);
/* 505 */         i = Builder.distance(localX500Name3, localX500Name1, -1);
/*     */ 
/* 507 */         j = Builder.distance(localX500Name3, localX500Name2, -1);
/*     */ 
/* 509 */         if (ForwardBuilder.debug != null) {
/* 510 */           ForwardBuilder.debug.println("PKIXCertComparator.compare() distanceTto1: " + i);
/* 511 */           ForwardBuilder.debug.println("PKIXCertComparator.compare() distanceTto2: " + j);
/*     */         }
/* 513 */         if ((i > 0) || (j > 0)) {
/* 514 */           if (i == j)
/* 515 */             return -1;
/* 516 */           if ((i > 0) && (j <= 0))
/* 517 */             return -1;
/* 518 */           if ((i <= 0) && (j > 0))
/* 519 */             return 1;
/* 520 */           if (i < j) {
/* 521 */             return -1;
/*     */           }
/* 523 */           return 1;
/*     */         }
/*     */       }
/*     */       int i;
/*     */       int j;
/* 531 */       if (ForwardBuilder.debug != null) {
/* 532 */         ForwardBuilder.debug.println("PKIXCertComparator.compare() NAMING ANCESTOR TEST...");
/*     */       }
/* 534 */       for (localObject = this.trustedSubjectDNs.iterator(); ((Iterator)localObject).hasNext(); ) { localX500Principal3 = (X500Principal)((Iterator)localObject).next();
/* 535 */         localX500Name3 = X500Name.asX500Name(localX500Principal3);
/*     */ 
/* 537 */         i = Builder.distance(localX500Name3, localX500Name1, 2147483647);
/*     */ 
/* 539 */         j = Builder.distance(localX500Name3, localX500Name2, 2147483647);
/*     */ 
/* 541 */         if (ForwardBuilder.debug != null) {
/* 542 */           ForwardBuilder.debug.println("PKIXCertComparator.compare() distanceTto1: " + i);
/* 543 */           ForwardBuilder.debug.println("PKIXCertComparator.compare() distanceTto2: " + j);
/*     */         }
/* 545 */         if ((i < 0) || (j < 0)) {
/* 546 */           if (i == j)
/* 547 */             return -1;
/* 548 */           if ((i < 0) && (j >= 0))
/* 549 */             return -1;
/* 550 */           if ((i >= 0) && (j < 0))
/* 551 */             return 1;
/* 552 */           if (i > j) {
/* 553 */             return -1;
/*     */           }
/* 555 */           return 1;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 564 */       if (ForwardBuilder.debug != null) {
/* 565 */         ForwardBuilder.debug.println("PKIXCertComparator.compare() SAME NAMESPACE AS TRUSTED TEST...");
/*     */       }
/* 567 */       for (localObject = this.trustedSubjectDNs.iterator(); ((Iterator)localObject).hasNext(); ) { localX500Principal3 = (X500Principal)((Iterator)localObject).next();
/* 568 */         localX500Name3 = X500Name.asX500Name(localX500Principal3);
/* 569 */         localX500Name4 = localX500Name3.commonAncestor(localX500Name1);
/* 570 */         X500Name localX500Name5 = localX500Name3.commonAncestor(localX500Name2);
/* 571 */         if (ForwardBuilder.debug != null) {
/* 572 */           ForwardBuilder.debug.println("PKIXCertComparator.compare() tAo1: " + String.valueOf(localX500Name4));
/* 573 */           ForwardBuilder.debug.println("PKIXCertComparator.compare() tAo2: " + String.valueOf(localX500Name5));
/*     */         }
/* 575 */         if ((localX500Name4 != null) || (localX500Name5 != null)) {
/* 576 */           if ((localX500Name4 != null) && (localX500Name5 != null)) {
/* 577 */             m = Builder.hops(localX500Name3, localX500Name1, 2147483647);
/*     */ 
/* 579 */             int n = Builder.hops(localX500Name3, localX500Name2, 2147483647);
/*     */ 
/* 581 */             if (ForwardBuilder.debug != null) {
/* 582 */               ForwardBuilder.debug.println("PKIXCertComparator.compare() hopsTto1: " + m);
/* 583 */               ForwardBuilder.debug.println("PKIXCertComparator.compare() hopsTto2: " + n);
/*     */             }
/* 585 */             if (m != n) {
/* 586 */               if (m > n) {
/* 587 */                 return 1;
/*     */               }
/* 589 */               return -1;
/*     */             }
/*     */           } else { if (localX500Name4 == null) {
/* 592 */               return 1;
/*     */             }
/* 594 */             return -1;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 603 */       if (ForwardBuilder.debug != null) {
/* 604 */         ForwardBuilder.debug.println("PKIXCertComparator.compare() CERT ISSUER/SUBJECT COMPARISON TEST...");
/*     */       }
/* 606 */       localObject = paramX509Certificate1.getSubjectX500Principal();
/* 607 */       X500Principal localX500Principal3 = paramX509Certificate2.getSubjectX500Principal();
/* 608 */       X500Name localX500Name3 = X500Name.asX500Name((X500Principal)localObject);
/* 609 */       X500Name localX500Name4 = X500Name.asX500Name(localX500Principal3);
/*     */ 
/* 611 */       if (ForwardBuilder.debug != null) {
/* 612 */         ForwardBuilder.debug.println("PKIXCertComparator.compare() o1 Subject: " + localObject);
/* 613 */         ForwardBuilder.debug.println("PKIXCertComparator.compare() o2 Subject: " + localX500Principal3);
/*     */       }
/* 615 */       int k = Builder.distance(localX500Name3, localX500Name1, 2147483647);
/*     */ 
/* 617 */       int m = Builder.distance(localX500Name4, localX500Name2, 2147483647);
/*     */ 
/* 619 */       if (ForwardBuilder.debug != null) {
/* 620 */         ForwardBuilder.debug.println("PKIXCertComparator.compare() distanceStoI1: " + k);
/* 621 */         ForwardBuilder.debug.println("PKIXCertComparator.compare() distanceStoI2: " + m);
/*     */       }
/* 623 */       if (m > k)
/* 624 */         return -1;
/* 625 */       if (m < k) {
/* 626 */         return 1;
/*     */       }
/*     */ 
/* 631 */       if (ForwardBuilder.debug != null) {
/* 632 */         ForwardBuilder.debug.println("PKIXCertComparator.compare() no tests matched; RETURN 0");
/*     */       }
/* 634 */       return -1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.ForwardBuilder
 * JD-Core Version:    0.6.2
 */