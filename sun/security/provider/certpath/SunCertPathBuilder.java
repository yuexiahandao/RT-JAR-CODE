/*     */ package sun.security.provider.certpath;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.AccessController;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.PublicKey;
/*     */ import java.security.cert.CertPathBuilderException;
/*     */ import java.security.cert.CertPathBuilderResult;
/*     */ import java.security.cert.CertPathBuilderSpi;
/*     */ import java.security.cert.CertPathParameters;
/*     */ import java.security.cert.CertPathValidatorException;
/*     */ import java.security.cert.CertStore;
/*     */ import java.security.cert.CertStoreException;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.PKIXBuilderParameters;
/*     */ import java.security.cert.PKIXCertPathChecker;
/*     */ import java.security.cert.PKIXReason;
/*     */ import java.security.cert.PolicyNode;
/*     */ import java.security.cert.TrustAnchor;
/*     */ import java.security.cert.X509CertSelector;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.security.interfaces.DSAPublicKey;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import sun.security.action.GetBooleanSecurityPropertyAction;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ import sun.security.x509.PKIXExtensions;
/*     */ 
/*     */ public final class SunCertPathBuilder extends CertPathBuilderSpi
/*     */ {
/*  76 */   private static final Debug debug = Debug.getInstance("certpath");
/*     */   private PKIXBuilderParameters buildParams;
/*     */   private CertificateFactory cf;
/*  83 */   private boolean pathCompleted = false;
/*     */   private X500Principal targetSubjectDN;
/*     */   private PolicyNode policyTreeResult;
/*     */   private TrustAnchor trustAnchor;
/*     */   private PublicKey finalPublicKey;
/*     */   private X509CertSelector targetSel;
/*     */   private List<CertStore> orderedCertStores;
/*  90 */   private boolean onlyEECert = false;
/*     */ 
/*     */   public SunCertPathBuilder()
/*     */     throws CertPathBuilderException
/*     */   {
/*     */     try
/*     */     {
/*  99 */       this.cf = CertificateFactory.getInstance("X.509");
/*     */     } catch (CertificateException localCertificateException) {
/* 101 */       throw new CertPathBuilderException(localCertificateException);
/*     */     }
/* 103 */     this.onlyEECert = ((Boolean)AccessController.doPrivileged(new GetBooleanSecurityPropertyAction("com.sun.security.onlyCheckRevocationOfEECert"))).booleanValue();
/*     */   }
/*     */ 
/*     */   public CertPathBuilderResult engineBuild(CertPathParameters paramCertPathParameters)
/*     */     throws CertPathBuilderException, InvalidAlgorithmParameterException
/*     */   {
/* 131 */     if (debug != null) {
/* 132 */       debug.println("SunCertPathBuilder.engineBuild(" + paramCertPathParameters + ")");
/*     */     }
/*     */ 
/* 135 */     if (!(paramCertPathParameters instanceof PKIXBuilderParameters)) {
/* 136 */       throw new InvalidAlgorithmParameterException("inappropriate parameter type, must be an instance of PKIXBuilderParameters");
/*     */     }
/*     */ 
/* 140 */     boolean bool = true;
/* 141 */     if ((paramCertPathParameters instanceof SunCertPathBuilderParameters)) {
/* 142 */       bool = ((SunCertPathBuilderParameters)paramCertPathParameters).getBuildForward();
/*     */     }
/*     */ 
/* 146 */     this.buildParams = ((PKIXBuilderParameters)paramCertPathParameters);
/*     */ 
/* 152 */     for (Object localObject1 = this.buildParams.getTrustAnchors().iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (TrustAnchor)((Iterator)localObject1).next();
/* 153 */       if (((TrustAnchor)localObject2).getNameConstraints() != null) {
/* 154 */         throw new InvalidAlgorithmParameterException("name constraints in trust anchor not supported");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 159 */     localObject1 = this.buildParams.getTargetCertConstraints();
/* 160 */     if (!(localObject1 instanceof X509CertSelector)) {
/* 161 */       throw new InvalidAlgorithmParameterException("the targetCertConstraints parameter must be an X509CertSelector");
/*     */     }
/*     */ 
/* 165 */     this.targetSel = ((X509CertSelector)localObject1);
/* 166 */     this.targetSubjectDN = this.targetSel.getSubject();
/* 167 */     if (this.targetSubjectDN == null) {
/* 168 */       localObject2 = this.targetSel.getCertificate();
/* 169 */       if (localObject2 != null) {
/* 170 */         this.targetSubjectDN = ((X509Certificate)localObject2).getSubjectX500Principal();
/*     */       }
/*     */     }
/*     */ 
/* 174 */     this.orderedCertStores = new ArrayList(this.buildParams.getCertStores());
/*     */ 
/* 176 */     Collections.sort(this.orderedCertStores, new CertStoreComparator(null));
/* 177 */     if (this.targetSubjectDN == null) {
/* 178 */       this.targetSubjectDN = getTargetSubjectDN(this.orderedCertStores, this.targetSel);
/*     */     }
/* 180 */     if (this.targetSubjectDN == null) {
/* 181 */       throw new InvalidAlgorithmParameterException("Could not determine unique target subject");
/*     */     }
/*     */ 
/* 185 */     Object localObject2 = new ArrayList();
/* 186 */     CertPathBuilderResult localCertPathBuilderResult = buildCertPath(bool, false, (List)localObject2);
/*     */ 
/* 188 */     if (localCertPathBuilderResult == null) {
/* 189 */       if (debug != null) {
/* 190 */         debug.println("SunCertPathBuilder.engineBuild: 2nd pass");
/*     */       }
/*     */ 
/* 193 */       ((List)localObject2).clear();
/* 194 */       localCertPathBuilderResult = buildCertPath(bool, true, (List)localObject2);
/* 195 */       if (localCertPathBuilderResult == null) {
/* 196 */         throw new SunCertPathBuilderException("unable to find valid certification path to requested target", new AdjacencyList((List)localObject2));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 201 */     return localCertPathBuilderResult;
/*     */   }
/*     */ 
/*     */   private CertPathBuilderResult buildCertPath(boolean paramBoolean1, boolean paramBoolean2, List<List<Vertex>> paramList)
/*     */     throws CertPathBuilderException
/*     */   {
/* 209 */     this.pathCompleted = false;
/* 210 */     this.trustAnchor = null;
/* 211 */     this.finalPublicKey = null;
/* 212 */     this.policyTreeResult = null;
/* 213 */     LinkedList localLinkedList = new LinkedList();
/*     */     try
/*     */     {
/* 216 */       if (paramBoolean1)
/* 217 */         buildForward(paramList, localLinkedList, paramBoolean2);
/*     */       else
/* 219 */         buildReverse(paramList, localLinkedList);
/*     */     }
/*     */     catch (Exception localException1) {
/* 222 */       if (debug != null) {
/* 223 */         debug.println("SunCertPathBuilder.engineBuild() exception in build");
/*     */ 
/* 225 */         localException1.printStackTrace();
/*     */       }
/* 227 */       throw new SunCertPathBuilderException("unable to find valid certification path to requested target", localException1, new AdjacencyList(paramList));
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 234 */       if (this.pathCompleted) {
/* 235 */         if (debug != null) {
/* 236 */           debug.println("SunCertPathBuilder.engineBuild() pathCompleted");
/*     */         }
/*     */ 
/* 242 */         Collections.reverse(localLinkedList);
/*     */ 
/* 244 */         return new SunCertPathBuilderResult(this.cf.generateCertPath(localLinkedList), this.trustAnchor, this.policyTreeResult, this.finalPublicKey, new AdjacencyList(paramList));
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (Exception localException2)
/*     */     {
/* 250 */       if (debug != null) {
/* 251 */         debug.println("SunCertPathBuilder.engineBuild() exception in wrap-up");
/*     */ 
/* 253 */         localException2.printStackTrace();
/*     */       }
/* 255 */       throw new SunCertPathBuilderException("unable to find valid certification path to requested target", localException2, new AdjacencyList(paramList));
/*     */     }
/*     */ 
/* 260 */     return null;
/*     */   }
/*     */ 
/*     */   private void buildReverse(List<List<Vertex>> paramList, LinkedList<X509Certificate> paramLinkedList)
/*     */     throws Exception
/*     */   {
/* 269 */     if (debug != null) {
/* 270 */       debug.println("SunCertPathBuilder.buildReverse()...");
/* 271 */       debug.println("SunCertPathBuilder.buildReverse() InitialPolicies: " + this.buildParams.getInitialPolicies());
/*     */     }
/*     */ 
/* 275 */     ReverseState localReverseState = new ReverseState();
/*     */ 
/* 277 */     paramList.clear();
/* 278 */     paramList.add(new LinkedList());
/*     */ 
/* 284 */     Iterator localIterator = this.buildParams.getTrustAnchors().iterator();
/* 285 */     while (localIterator.hasNext()) {
/* 286 */       TrustAnchor localTrustAnchor = (TrustAnchor)localIterator.next();
/*     */ 
/* 289 */       if (anchorIsTarget(localTrustAnchor, this.targetSel)) {
/* 290 */         this.trustAnchor = localTrustAnchor;
/* 291 */         this.pathCompleted = true;
/* 292 */         this.finalPublicKey = localTrustAnchor.getTrustedCert().getPublicKey();
/*     */       }
/*     */       else
/*     */       {
/* 297 */         localReverseState.initState(this.buildParams.getMaxPathLength(), this.buildParams.isExplicitPolicyRequired(), this.buildParams.isPolicyMappingInhibited(), this.buildParams.isAnyPolicyInhibited(), this.buildParams.getCertPathCheckers());
/*     */ 
/* 302 */         localReverseState.updateState(localTrustAnchor);
/*     */ 
/* 304 */         localReverseState.crlChecker = new CrlRevocationChecker(null, this.buildParams, null, this.onlyEECert);
/*     */ 
/* 306 */         localReverseState.algorithmChecker = new AlgorithmChecker(localTrustAnchor);
/* 307 */         localReverseState.untrustedChecker = new UntrustedChecker();
/*     */         try {
/* 309 */           depthFirstSearchReverse(null, localReverseState, new ReverseBuilder(this.buildParams, this.targetSubjectDN), paramList, paramLinkedList);
/*     */         }
/*     */         catch (Exception localException)
/*     */         {
/*     */         }
/* 314 */         if (!localIterator.hasNext())
/*     */         {
/* 317 */           throw localException;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 324 */     if (debug != null) {
/* 325 */       debug.println("SunCertPathBuilder.buildReverse() returned from depthFirstSearchReverse()");
/*     */ 
/* 327 */       debug.println("SunCertPathBuilder.buildReverse() certPathList.size: " + paramLinkedList.size());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void buildForward(List<List<Vertex>> paramList, LinkedList<X509Certificate> paramLinkedList, boolean paramBoolean)
/*     */     throws GeneralSecurityException, IOException
/*     */   {
/* 339 */     if (debug != null) {
/* 340 */       debug.println("SunCertPathBuilder.buildForward()...");
/*     */     }
/*     */ 
/* 344 */     ForwardState localForwardState = new ForwardState();
/* 345 */     localForwardState.initState(this.buildParams.getCertPathCheckers());
/*     */ 
/* 348 */     paramList.clear();
/* 349 */     paramList.add(new LinkedList());
/*     */ 
/* 352 */     localForwardState.crlChecker = new CrlRevocationChecker(null, this.buildParams, null, this.onlyEECert);
/*     */ 
/* 354 */     localForwardState.untrustedChecker = new UntrustedChecker();
/*     */ 
/* 356 */     depthFirstSearchForward(this.targetSubjectDN, localForwardState, new ForwardBuilder(this.buildParams, this.targetSubjectDN, paramBoolean, this.onlyEECert), paramList, paramLinkedList);
/*     */   }
/*     */ 
/*     */   void depthFirstSearchForward(X500Principal paramX500Principal, ForwardState paramForwardState, ForwardBuilder paramForwardBuilder, List<List<Vertex>> paramList, LinkedList<X509Certificate> paramLinkedList)
/*     */     throws GeneralSecurityException, IOException
/*     */   {
/* 384 */     if (debug != null) {
/* 385 */       debug.println("SunCertPathBuilder.depthFirstSearchForward(" + paramX500Principal + ", " + paramForwardState.toString() + ")");
/*     */     }
/*     */ 
/* 393 */     List localList = addVertices(paramForwardBuilder.getMatchingCerts(paramForwardState, this.orderedCertStores), paramList);
/*     */ 
/* 395 */     if (debug != null) {
/* 396 */       debug.println("SunCertPathBuilder.depthFirstSearchForward(): certs.size=" + localList.size());
/*     */     }
/*     */ 
/* 408 */     for (Vertex localVertex : localList)
/*     */     {
/* 416 */       ForwardState localForwardState = (ForwardState)paramForwardState.clone();
/* 417 */       X509Certificate localX509Certificate = (X509Certificate)localVertex.getCertificate();
/*     */       try
/*     */       {
/* 420 */         paramForwardBuilder.verifyCert(localX509Certificate, localForwardState, paramLinkedList);
/*     */       } catch (GeneralSecurityException localGeneralSecurityException) {
/* 422 */         if (debug != null) {
/* 423 */           debug.println("SunCertPathBuilder.depthFirstSearchForward(): validation failed: " + localGeneralSecurityException);
/*     */ 
/* 425 */           localGeneralSecurityException.printStackTrace();
/*     */         }
/* 427 */         localVertex.setThrowable(localGeneralSecurityException);
/* 428 */       }continue;
/*     */ 
/* 440 */       if (paramForwardBuilder.isPathCompleted(localX509Certificate))
/*     */       {
/* 442 */         BasicChecker localBasicChecker = null;
/* 443 */         if (debug != null) {
/* 444 */           debug.println("SunCertPathBuilder.depthFirstSearchForward(): commencing final verification");
/*     */         }
/*     */ 
/* 447 */         ArrayList localArrayList1 = new ArrayList(paramLinkedList);
/*     */ 
/* 456 */         if (paramForwardBuilder.trustAnchor.getTrustedCert() == null) {
/* 457 */           localArrayList1.add(0, localX509Certificate);
/*     */         }
/*     */ 
/* 460 */         HashSet localHashSet = new HashSet(1);
/* 461 */         localHashSet.add("2.5.29.32.0");
/*     */ 
/* 463 */         PolicyNodeImpl localPolicyNodeImpl = new PolicyNodeImpl(null, "2.5.29.32.0", null, false, localHashSet, false);
/*     */ 
/* 466 */         PolicyChecker localPolicyChecker = new PolicyChecker(this.buildParams.getInitialPolicies(), localArrayList1.size(), this.buildParams.isExplicitPolicyRequired(), this.buildParams.isPolicyMappingInhibited(), this.buildParams.isAnyPolicyInhibited(), this.buildParams.getPolicyQualifiersRejected(), localPolicyNodeImpl);
/*     */ 
/* 475 */         ArrayList localArrayList2 = new ArrayList(this.buildParams.getCertPathCheckers());
/*     */ 
/* 478 */         int i = 0;
/* 479 */         localArrayList2.add(i, localPolicyChecker);
/* 480 */         i++;
/*     */ 
/* 483 */         localArrayList2.add(i, new AlgorithmChecker(paramForwardBuilder.trustAnchor));
/*     */ 
/* 485 */         i++;
/*     */         Object localObject2;
/* 487 */         if (localForwardState.keyParamsNeeded()) {
/* 488 */           PublicKey localPublicKey = localX509Certificate.getPublicKey();
/* 489 */           if (paramForwardBuilder.trustAnchor.getTrustedCert() == null) {
/* 490 */             localPublicKey = paramForwardBuilder.trustAnchor.getCAPublicKey();
/* 491 */             if (debug != null) {
/* 492 */               debug.println("SunCertPathBuilder.depthFirstSearchForward using buildParams public key: " + localPublicKey.toString());
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 497 */           localObject2 = new TrustAnchor(localX509Certificate.getSubjectX500Principal(), localPublicKey, null);
/*     */ 
/* 501 */           localBasicChecker = new BasicChecker((TrustAnchor)localObject2, paramForwardBuilder.date, this.buildParams.getSigProvider(), true);
/*     */ 
/* 505 */           localArrayList2.add(i, localBasicChecker);
/* 506 */           i++;
/*     */ 
/* 509 */           if (this.buildParams.isRevocationEnabled()) {
/* 510 */             localArrayList2.add(i, new CrlRevocationChecker((TrustAnchor)localObject2, this.buildParams, null, this.onlyEECert));
/*     */ 
/* 512 */             i++;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 518 */         for (int j = 0; ; j++) { if (j >= localArrayList1.size()) break label1032;
/* 519 */           localObject2 = (X509Certificate)localArrayList1.get(j);
/* 520 */           if (debug != null) {
/* 521 */             debug.println("current subject = " + ((X509Certificate)localObject2).getSubjectX500Principal());
/*     */           }
/* 523 */           Set localSet1 = ((X509Certificate)localObject2).getCriticalExtensionOIDs();
/*     */ 
/* 525 */           if (localSet1 == null)
/* 526 */             localSet1 = Collections.emptySet();
/* 529 */           PKIXCertPathChecker localPKIXCertPathChecker;
/* 529 */           for (int k = 0; ; k++) { if (k >= localArrayList2.size()) break label795;
/* 530 */             localPKIXCertPathChecker = (PKIXCertPathChecker)localArrayList2.get(k);
/* 531 */             if ((k < i) || (!localPKIXCertPathChecker.isForwardCheckingSupported()))
/*     */             {
/* 533 */               if (j == 0) {
/* 534 */                 localPKIXCertPathChecker.init(false);
/*     */ 
/* 539 */                 if ((k >= i) && ((localPKIXCertPathChecker instanceof AlgorithmChecker)))
/*     */                 {
/* 541 */                   ((AlgorithmChecker)localPKIXCertPathChecker).trySetTrustAnchor(paramForwardBuilder.trustAnchor);
/*     */                 }
/*     */               }
/*     */ 
/*     */               try
/*     */               {
/* 547 */                 localPKIXCertPathChecker.check((Certificate)localObject2, localSet1);
/*     */               } catch (CertPathValidatorException localCertPathValidatorException) {
/* 549 */                 if (debug != null) {
/* 550 */                   debug.println("SunCertPathBuilder.depthFirstSearchForward(): final verification failed: " + localCertPathValidatorException);
/*     */                 }
/*     */ 
/* 553 */                 localVertex.setThrowable(localCertPathValidatorException);
/* 554 */               }break;
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 566 */           for (Iterator localIterator2 = this.buildParams.getCertPathCheckers().iterator(); localIterator2.hasNext(); ) { localPKIXCertPathChecker = (PKIXCertPathChecker)localIterator2.next();
/*     */ 
/* 568 */             if (localPKIXCertPathChecker.isForwardCheckingSupported()) {
/* 569 */               Set localSet2 = localPKIXCertPathChecker.getSupportedExtensions();
/*     */ 
/* 571 */               if (localSet2 != null) {
/* 572 */                 localSet1.removeAll(localSet2);
/*     */               }
/*     */             }
/*     */           }
/*     */ 
/* 577 */           if (!localSet1.isEmpty()) {
/* 578 */             localSet1.remove(PKIXExtensions.BasicConstraints_Id.toString());
/*     */ 
/* 580 */             localSet1.remove(PKIXExtensions.NameConstraints_Id.toString());
/*     */ 
/* 582 */             localSet1.remove(PKIXExtensions.CertificatePolicies_Id.toString());
/*     */ 
/* 584 */             localSet1.remove(PKIXExtensions.PolicyMappings_Id.toString());
/*     */ 
/* 586 */             localSet1.remove(PKIXExtensions.PolicyConstraints_Id.toString());
/*     */ 
/* 588 */             localSet1.remove(PKIXExtensions.InhibitAnyPolicy_Id.toString());
/*     */ 
/* 590 */             localSet1.remove(PKIXExtensions.SubjectAlternativeName_Id.toString());
/*     */ 
/* 592 */             localSet1.remove(PKIXExtensions.KeyUsage_Id.toString());
/*     */ 
/* 594 */             localSet1.remove(PKIXExtensions.ExtendedKeyUsage_Id.toString());
/*     */ 
/* 597 */             if (!localSet1.isEmpty()) {
/* 598 */               throw new CertPathValidatorException("unrecognized critical extension(s)", null, null, -1, PKIXReason.UNRECOGNIZED_CRIT_EXT);
/*     */             }
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 604 */         if (debug != null) {
/* 605 */           debug.println("SunCertPathBuilder.depthFirstSearchForward(): final verification succeeded - path completed!");
/*     */         }
/* 607 */         this.pathCompleted = true;
/*     */ 
/* 614 */         if (paramForwardBuilder.trustAnchor.getTrustedCert() == null) {
/* 615 */           paramForwardBuilder.addCertToPath(localX509Certificate, paramLinkedList);
/*     */         }
/* 617 */         this.trustAnchor = paramForwardBuilder.trustAnchor;
/*     */ 
/* 622 */         if (localBasicChecker != null) {
/* 623 */           this.finalPublicKey = localBasicChecker.getPublicKey();
/*     */         }
/*     */         else
/*     */         {
/*     */           Object localObject1;
/* 626 */           if (paramLinkedList.size() == 0)
/* 627 */             localObject1 = paramForwardBuilder.trustAnchor.getTrustedCert();
/*     */           else {
/* 629 */             localObject1 = (Certificate)paramLinkedList.get(paramLinkedList.size() - 1);
/*     */           }
/* 631 */           this.finalPublicKey = ((Certificate)localObject1).getPublicKey();
/*     */         }
/*     */ 
/* 634 */         this.policyTreeResult = localPolicyChecker.getPolicyTree();
/* 635 */         return;
/*     */       }
/* 637 */       paramForwardBuilder.addCertToPath(localX509Certificate, paramLinkedList);
/*     */ 
/* 641 */       localForwardState.updateState(localX509Certificate);
/*     */ 
/* 647 */       paramList.add(new LinkedList());
/* 648 */       localVertex.setIndex(paramList.size() - 1);
/*     */ 
/* 651 */       depthFirstSearchForward(localX509Certificate.getIssuerX500Principal(), localForwardState, paramForwardBuilder, paramList, paramLinkedList);
/*     */ 
/* 657 */       if (this.pathCompleted) {
/* 658 */         return;
/*     */       }
/*     */ 
/* 666 */       if (debug != null) {
/* 667 */         debug.println("SunCertPathBuilder.depthFirstSearchForward(): backtracking");
/*     */       }
/* 669 */       paramForwardBuilder.removeFinalCertFromPath(paramLinkedList);
/*     */     }
/*     */ 
/* 672 */     label795:
/*     */   }
/*     */ 
/*     */   void depthFirstSearchReverse(X500Principal paramX500Principal, ReverseState paramReverseState, ReverseBuilder paramReverseBuilder, List<List<Vertex>> paramList, LinkedList<X509Certificate> paramLinkedList)
/*     */     throws GeneralSecurityException, IOException
/*     */   {
/* 694 */     if (debug != null) {
/* 695 */       debug.println("SunCertPathBuilder.depthFirstSearchReverse(" + paramX500Principal + ", " + paramReverseState.toString() + ")");
/*     */     }
/*     */ 
/* 702 */     List localList = addVertices(paramReverseBuilder.getMatchingCerts(paramReverseState, this.orderedCertStores), paramList);
/*     */ 
/* 704 */     if (debug != null) {
/* 705 */       debug.println("SunCertPathBuilder.depthFirstSearchReverse(): certs.size=" + localList.size());
/*     */     }
/*     */ 
/* 714 */     for (Vertex localVertex : localList)
/*     */     {
/* 722 */       ReverseState localReverseState = (ReverseState)paramReverseState.clone();
/* 723 */       X509Certificate localX509Certificate = (X509Certificate)localVertex.getCertificate();
/*     */       try {
/* 725 */         paramReverseBuilder.verifyCert(localX509Certificate, localReverseState, paramLinkedList);
/*     */       } catch (GeneralSecurityException localGeneralSecurityException) {
/* 727 */         if (debug != null) {
/* 728 */           debug.println("SunCertPathBuilder.depthFirstSearchReverse(): validation failed: " + localGeneralSecurityException);
/*     */         }
/* 730 */         localVertex.setThrowable(localGeneralSecurityException);
/* 731 */       }continue;
/*     */ 
/* 738 */       if (!paramReverseState.isInitial()) {
/* 739 */         paramReverseBuilder.addCertToPath(localX509Certificate, paramLinkedList);
/*     */       }
/* 741 */       this.trustAnchor = paramReverseState.trustAnchor;
/*     */ 
/* 746 */       if (paramReverseBuilder.isPathCompleted(localX509Certificate)) {
/* 747 */         if (debug != null) {
/* 748 */           debug.println("SunCertPathBuilder.depthFirstSearchReverse(): path completed!");
/*     */         }
/* 750 */         this.pathCompleted = true;
/*     */ 
/* 752 */         PolicyNodeImpl localPolicyNodeImpl = localReverseState.rootNode;
/*     */ 
/* 754 */         if (localPolicyNodeImpl == null) {
/* 755 */           this.policyTreeResult = null;
/*     */         } else {
/* 757 */           this.policyTreeResult = localPolicyNodeImpl.copyTree();
/* 758 */           ((PolicyNodeImpl)this.policyTreeResult).setImmutable();
/*     */         }
/*     */ 
/* 764 */         this.finalPublicKey = localX509Certificate.getPublicKey();
/* 765 */         if (((this.finalPublicKey instanceof DSAPublicKey)) && (((DSAPublicKey)this.finalPublicKey).getParams() == null))
/*     */         {
/* 768 */           this.finalPublicKey = BasicChecker.makeInheritedParamsKey(this.finalPublicKey, paramReverseState.pubKey);
/*     */         }
/*     */ 
/* 773 */         return;
/*     */       }
/*     */ 
/* 777 */       localReverseState.updateState(localX509Certificate);
/*     */ 
/* 783 */       paramList.add(new LinkedList());
/* 784 */       localVertex.setIndex(paramList.size() - 1);
/*     */ 
/* 787 */       depthFirstSearchReverse(localX509Certificate.getSubjectX500Principal(), localReverseState, paramReverseBuilder, paramList, paramLinkedList);
/*     */ 
/* 793 */       if (this.pathCompleted) {
/* 794 */         return;
/*     */       }
/*     */ 
/* 802 */       if (debug != null) {
/* 803 */         debug.println("SunCertPathBuilder.depthFirstSearchReverse(): backtracking");
/*     */       }
/* 805 */       if (!paramReverseState.isInitial()) {
/* 806 */         paramReverseBuilder.removeFinalCertFromPath(paramLinkedList);
/*     */       }
/*     */     }
/* 809 */     if (debug != null)
/* 810 */       debug.println("SunCertPathBuilder.depthFirstSearchReverse() all certs in this adjacency list checked");
/*     */   }
/*     */ 
/*     */   private List<Vertex> addVertices(Collection<X509Certificate> paramCollection, List<List<Vertex>> paramList)
/*     */   {
/* 820 */     List localList = (List)paramList.get(paramList.size() - 1);
/*     */ 
/* 822 */     for (X509Certificate localX509Certificate : paramCollection) {
/* 823 */       Vertex localVertex = new Vertex(localX509Certificate);
/* 824 */       localList.add(localVertex);
/*     */     }
/*     */ 
/* 827 */     return localList;
/*     */   }
/*     */ 
/*     */   private boolean anchorIsTarget(TrustAnchor paramTrustAnchor, X509CertSelector paramX509CertSelector)
/*     */   {
/* 835 */     X509Certificate localX509Certificate = paramTrustAnchor.getTrustedCert();
/* 836 */     if (localX509Certificate != null) {
/* 837 */       return paramX509CertSelector.match(localX509Certificate);
/*     */     }
/* 839 */     return false;
/*     */   }
/*     */ 
/*     */   private X500Principal getTargetSubjectDN(List<CertStore> paramList, X509CertSelector paramX509CertSelector)
/*     */   {
/* 862 */     for (CertStore localCertStore : paramList) {
/*     */       try {
/* 864 */         Collection localCollection = localCertStore.getCertificates(paramX509CertSelector);
/*     */ 
/* 867 */         if (!localCollection.isEmpty()) {
/* 868 */           X509Certificate localX509Certificate = (X509Certificate)localCollection.iterator().next();
/*     */ 
/* 870 */           return localX509Certificate.getSubjectX500Principal();
/*     */         }
/*     */       }
/*     */       catch (CertStoreException localCertStoreException) {
/* 874 */         if (debug != null) {
/* 875 */           debug.println("SunCertPathBuilder.getTargetSubjectDN: non-fatal exception retrieving certs: " + localCertStoreException);
/*     */ 
/* 877 */           localCertStoreException.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/* 881 */     return null;
/*     */   }
/*     */ 
/*     */   private static class CertStoreComparator
/*     */     implements Comparator<CertStore>
/*     */   {
/*     */     public int compare(CertStore paramCertStore1, CertStore paramCertStore2)
/*     */     {
/* 848 */       if (Builder.isLocalCertStore(paramCertStore1)) {
/* 849 */         return -1;
/*     */       }
/* 851 */       return 1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.SunCertPathBuilder
 * JD-Core Version:    0.6.2
 */