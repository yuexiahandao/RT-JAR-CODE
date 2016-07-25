/*     */ package sun.security.provider.certpath;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.cert.CertPathValidatorException;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.PKIXCertPathChecker;
/*     */ import java.security.cert.PKIXReason;
/*     */ import java.security.cert.PolicyNode;
/*     */ import java.security.cert.PolicyQualifierInfo;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ import sun.security.x509.CertificatePoliciesExtension;
/*     */ import sun.security.x509.CertificatePolicyId;
/*     */ import sun.security.x509.CertificatePolicyMap;
/*     */ import sun.security.x509.InhibitAnyPolicyExtension;
/*     */ import sun.security.x509.PKIXExtensions;
/*     */ import sun.security.x509.PolicyConstraintsExtension;
/*     */ import sun.security.x509.PolicyInformation;
/*     */ import sun.security.x509.PolicyMappingsExtension;
/*     */ import sun.security.x509.X509CertImpl;
/*     */ 
/*     */ class PolicyChecker extends PKIXCertPathChecker
/*     */ {
/*     */   private final Set<String> initPolicies;
/*     */   private final int certPathLen;
/*     */   private final boolean expPolicyRequired;
/*     */   private final boolean polMappingInhibited;
/*     */   private final boolean anyPolicyInhibited;
/*     */   private final boolean rejectPolicyQualifiers;
/*     */   private PolicyNodeImpl rootNode;
/*     */   private int explicitPolicy;
/*     */   private int policyMapping;
/*     */   private int inhibitAnyPolicy;
/*     */   private int certIndex;
/*     */   private Set<String> supportedExts;
/*  74 */   private static final Debug debug = Debug.getInstance("certpath");
/*     */   static final String ANY_POLICY = "2.5.29.32.0";
/*     */ 
/*     */   PolicyChecker(Set<String> paramSet, int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, PolicyNodeImpl paramPolicyNodeImpl)
/*     */     throws CertPathValidatorException
/*     */   {
/*  93 */     if (paramSet.isEmpty())
/*     */     {
/*  96 */       this.initPolicies = new HashSet(1);
/*  97 */       this.initPolicies.add("2.5.29.32.0");
/*     */     } else {
/*  99 */       this.initPolicies = new HashSet(paramSet);
/*     */     }
/* 101 */     this.certPathLen = paramInt;
/* 102 */     this.expPolicyRequired = paramBoolean1;
/* 103 */     this.polMappingInhibited = paramBoolean2;
/* 104 */     this.anyPolicyInhibited = paramBoolean3;
/* 105 */     this.rejectPolicyQualifiers = paramBoolean4;
/* 106 */     this.rootNode = paramPolicyNodeImpl;
/* 107 */     init(false);
/*     */   }
/*     */ 
/*     */   public void init(boolean paramBoolean)
/*     */     throws CertPathValidatorException
/*     */   {
/* 120 */     if (paramBoolean) {
/* 121 */       throw new CertPathValidatorException("forward checking not supported");
/*     */     }
/*     */ 
/* 125 */     this.certIndex = 1;
/* 126 */     this.explicitPolicy = (this.expPolicyRequired ? 0 : this.certPathLen + 1);
/* 127 */     this.policyMapping = (this.polMappingInhibited ? 0 : this.certPathLen + 1);
/* 128 */     this.inhibitAnyPolicy = (this.anyPolicyInhibited ? 0 : this.certPathLen + 1);
/*     */   }
/*     */ 
/*     */   public boolean isForwardCheckingSupported()
/*     */   {
/* 140 */     return false;
/*     */   }
/*     */ 
/*     */   public Set<String> getSupportedExtensions()
/*     */   {
/* 154 */     if (this.supportedExts == null) {
/* 155 */       this.supportedExts = new HashSet();
/* 156 */       this.supportedExts.add(PKIXExtensions.CertificatePolicies_Id.toString());
/* 157 */       this.supportedExts.add(PKIXExtensions.PolicyMappings_Id.toString());
/* 158 */       this.supportedExts.add(PKIXExtensions.PolicyConstraints_Id.toString());
/* 159 */       this.supportedExts.add(PKIXExtensions.InhibitAnyPolicy_Id.toString());
/* 160 */       this.supportedExts = Collections.unmodifiableSet(this.supportedExts);
/*     */     }
/* 162 */     return this.supportedExts;
/*     */   }
/*     */ 
/*     */   public void check(Certificate paramCertificate, Collection<String> paramCollection)
/*     */     throws CertPathValidatorException
/*     */   {
/* 178 */     checkPolicy((X509Certificate)paramCertificate);
/*     */ 
/* 180 */     if ((paramCollection != null) && (!paramCollection.isEmpty())) {
/* 181 */       paramCollection.remove(PKIXExtensions.CertificatePolicies_Id.toString());
/* 182 */       paramCollection.remove(PKIXExtensions.PolicyMappings_Id.toString());
/* 183 */       paramCollection.remove(PKIXExtensions.PolicyConstraints_Id.toString());
/* 184 */       paramCollection.remove(PKIXExtensions.InhibitAnyPolicy_Id.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void checkPolicy(X509Certificate paramX509Certificate)
/*     */     throws CertPathValidatorException
/*     */   {
/* 198 */     String str = "certificate policies";
/* 199 */     if (debug != null) {
/* 200 */       debug.println("PolicyChecker.checkPolicy() ---checking " + str + "...");
/*     */ 
/* 202 */       debug.println("PolicyChecker.checkPolicy() certIndex = " + this.certIndex);
/*     */ 
/* 204 */       debug.println("PolicyChecker.checkPolicy() BEFORE PROCESSING: explicitPolicy = " + this.explicitPolicy);
/*     */ 
/* 206 */       debug.println("PolicyChecker.checkPolicy() BEFORE PROCESSING: policyMapping = " + this.policyMapping);
/*     */ 
/* 208 */       debug.println("PolicyChecker.checkPolicy() BEFORE PROCESSING: inhibitAnyPolicy = " + this.inhibitAnyPolicy);
/*     */ 
/* 210 */       debug.println("PolicyChecker.checkPolicy() BEFORE PROCESSING: policyTree = " + this.rootNode);
/*     */     }
/*     */ 
/* 214 */     X509CertImpl localX509CertImpl = null;
/*     */     try {
/* 216 */       localX509CertImpl = X509CertImpl.toImpl(paramX509Certificate);
/*     */     } catch (CertificateException localCertificateException) {
/* 218 */       throw new CertPathValidatorException(localCertificateException);
/*     */     }
/*     */ 
/* 221 */     boolean bool = this.certIndex == this.certPathLen;
/*     */ 
/* 223 */     this.rootNode = processPolicies(this.certIndex, this.initPolicies, this.explicitPolicy, this.policyMapping, this.inhibitAnyPolicy, this.rejectPolicyQualifiers, this.rootNode, localX509CertImpl, bool);
/*     */ 
/* 227 */     if (!bool) {
/* 228 */       this.explicitPolicy = mergeExplicitPolicy(this.explicitPolicy, localX509CertImpl, bool);
/*     */ 
/* 230 */       this.policyMapping = mergePolicyMapping(this.policyMapping, localX509CertImpl);
/* 231 */       this.inhibitAnyPolicy = mergeInhibitAnyPolicy(this.inhibitAnyPolicy, localX509CertImpl);
/*     */     }
/*     */ 
/* 235 */     this.certIndex += 1;
/*     */ 
/* 237 */     if (debug != null) {
/* 238 */       debug.println("PolicyChecker.checkPolicy() AFTER PROCESSING: explicitPolicy = " + this.explicitPolicy);
/*     */ 
/* 240 */       debug.println("PolicyChecker.checkPolicy() AFTER PROCESSING: policyMapping = " + this.policyMapping);
/*     */ 
/* 242 */       debug.println("PolicyChecker.checkPolicy() AFTER PROCESSING: inhibitAnyPolicy = " + this.inhibitAnyPolicy);
/*     */ 
/* 244 */       debug.println("PolicyChecker.checkPolicy() AFTER PROCESSING: policyTree = " + this.rootNode);
/*     */ 
/* 246 */       debug.println("PolicyChecker.checkPolicy() " + str + " verified");
/*     */     }
/*     */   }
/*     */ 
/*     */   static int mergeExplicitPolicy(int paramInt, X509CertImpl paramX509CertImpl, boolean paramBoolean)
/*     */     throws CertPathValidatorException
/*     */   {
/* 268 */     if ((paramInt > 0) && (!X509CertImpl.isSelfIssued(paramX509CertImpl))) {
/* 269 */       paramInt--;
/*     */     }
/*     */     try
/*     */     {
/* 273 */       PolicyConstraintsExtension localPolicyConstraintsExtension = paramX509CertImpl.getPolicyConstraintsExtension();
/*     */ 
/* 275 */       if (localPolicyConstraintsExtension == null)
/* 276 */         return paramInt;
/* 277 */       int i = ((Integer)localPolicyConstraintsExtension.get("require")).intValue();
/*     */ 
/* 279 */       if (debug != null) {
/* 280 */         debug.println("PolicyChecker.mergeExplicitPolicy() require Index from cert = " + i);
/*     */       }
/*     */ 
/* 283 */       if (!paramBoolean) {
/* 284 */         if ((i != -1) && (
/* 285 */           (paramInt == -1) || (i < paramInt))) {
/* 286 */           paramInt = i;
/*     */         }
/*     */ 
/*     */       }
/* 290 */       else if (i == 0)
/* 291 */         paramInt = i;
/*     */     }
/*     */     catch (Exception localException) {
/* 294 */       if (debug != null) {
/* 295 */         debug.println("PolicyChecker.mergeExplicitPolicy unexpected exception");
/*     */ 
/* 297 */         localException.printStackTrace();
/*     */       }
/* 299 */       throw new CertPathValidatorException(localException);
/*     */     }
/*     */ 
/* 302 */     return paramInt;
/*     */   }
/*     */ 
/*     */   static int mergePolicyMapping(int paramInt, X509CertImpl paramX509CertImpl)
/*     */     throws CertPathValidatorException
/*     */   {
/* 321 */     if ((paramInt > 0) && (!X509CertImpl.isSelfIssued(paramX509CertImpl))) {
/* 322 */       paramInt--;
/*     */     }
/*     */     try
/*     */     {
/* 326 */       PolicyConstraintsExtension localPolicyConstraintsExtension = paramX509CertImpl.getPolicyConstraintsExtension();
/*     */ 
/* 328 */       if (localPolicyConstraintsExtension == null) {
/* 329 */         return paramInt;
/*     */       }
/* 331 */       int i = ((Integer)localPolicyConstraintsExtension.get("inhibit")).intValue();
/*     */ 
/* 333 */       if (debug != null) {
/* 334 */         debug.println("PolicyChecker.mergePolicyMapping() inhibit Index from cert = " + i);
/*     */       }
/*     */ 
/* 337 */       if ((i != -1) && (
/* 338 */         (paramInt == -1) || (i < paramInt)))
/* 339 */         paramInt = i;
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 343 */       if (debug != null) {
/* 344 */         debug.println("PolicyChecker.mergePolicyMapping unexpected exception");
/*     */ 
/* 346 */         localException.printStackTrace();
/*     */       }
/* 348 */       throw new CertPathValidatorException(localException);
/*     */     }
/*     */ 
/* 351 */     return paramInt;
/*     */   }
/*     */ 
/*     */   static int mergeInhibitAnyPolicy(int paramInt, X509CertImpl paramX509CertImpl)
/*     */     throws CertPathValidatorException
/*     */   {
/* 369 */     if ((paramInt > 0) && (!X509CertImpl.isSelfIssued(paramX509CertImpl))) {
/* 370 */       paramInt--;
/*     */     }
/*     */     try
/*     */     {
/* 374 */       InhibitAnyPolicyExtension localInhibitAnyPolicyExtension = (InhibitAnyPolicyExtension)paramX509CertImpl.getExtension(PKIXExtensions.InhibitAnyPolicy_Id);
/*     */ 
/* 376 */       if (localInhibitAnyPolicyExtension == null) {
/* 377 */         return paramInt;
/*     */       }
/* 379 */       int i = ((Integer)localInhibitAnyPolicyExtension.get("skip_certs")).intValue();
/*     */ 
/* 381 */       if (debug != null) {
/* 382 */         debug.println("PolicyChecker.mergeInhibitAnyPolicy() skipCerts Index from cert = " + i);
/*     */       }
/*     */ 
/* 385 */       if ((i != -1) && 
/* 386 */         (i < paramInt))
/* 387 */         paramInt = i;
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 391 */       if (debug != null) {
/* 392 */         debug.println("PolicyChecker.mergeInhibitAnyPolicy unexpected exception");
/*     */ 
/* 394 */         localException.printStackTrace();
/*     */       }
/* 396 */       throw new CertPathValidatorException(localException);
/*     */     }
/*     */ 
/* 399 */     return paramInt;
/*     */   }
/*     */ 
/*     */   static PolicyNodeImpl processPolicies(int paramInt1, Set<String> paramSet, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, PolicyNodeImpl paramPolicyNodeImpl, X509CertImpl paramX509CertImpl, boolean paramBoolean2)
/*     */     throws CertPathValidatorException
/*     */   {
/* 429 */     boolean bool1 = false;
/*     */ 
/* 431 */     PolicyNodeImpl localPolicyNodeImpl = null;
/* 432 */     Object localObject = new HashSet();
/*     */ 
/* 434 */     if (paramPolicyNodeImpl == null)
/* 435 */       localPolicyNodeImpl = null;
/*     */     else {
/* 437 */       localPolicyNodeImpl = paramPolicyNodeImpl.copyTree();
/*     */     }
/*     */ 
/* 440 */     CertificatePoliciesExtension localCertificatePoliciesExtension = paramX509CertImpl.getCertificatePoliciesExtension();
/*     */ 
/* 444 */     if ((localCertificatePoliciesExtension != null) && (localPolicyNodeImpl != null)) {
/* 445 */       bool1 = localCertificatePoliciesExtension.isCritical();
/* 446 */       if (debug != null)
/* 447 */         debug.println("PolicyChecker.processPolicies() policiesCritical = " + bool1);
/*     */       List localList;
/*     */       try
/*     */       {
/* 451 */         localList = (List)localCertificatePoliciesExtension.get("policies");
/*     */       }
/*     */       catch (IOException localIOException) {
/* 454 */         throw new CertPathValidatorException("Exception while retrieving policyOIDs", localIOException);
/*     */       }
/*     */ 
/* 458 */       if (debug != null) {
/* 459 */         debug.println("PolicyChecker.processPolicies() rejectPolicyQualifiers = " + paramBoolean1);
/*     */       }
/*     */ 
/* 462 */       int i = 0;
/*     */ 
/* 465 */       for (PolicyInformation localPolicyInformation : localList) {
/* 466 */         String str = localPolicyInformation.getPolicyIdentifier().getIdentifier().toString();
/*     */ 
/* 469 */         if (str.equals("2.5.29.32.0")) {
/* 470 */           i = 1;
/* 471 */           localObject = localPolicyInformation.getPolicyQualifiers();
/*     */         }
/*     */         else {
/* 474 */           if (debug != null) {
/* 475 */             debug.println("PolicyChecker.processPolicies() processing policy: " + str);
/*     */           }
/*     */ 
/* 479 */           Set localSet = localPolicyInformation.getPolicyQualifiers();
/*     */ 
/* 484 */           if ((!localSet.isEmpty()) && (paramBoolean1) && (bool1))
/*     */           {
/* 486 */             throw new CertPathValidatorException("critical policy qualifiers present in certificate", null, null, -1, PKIXReason.INVALID_POLICY);
/*     */           }
/*     */ 
/* 492 */           boolean bool2 = processParents(paramInt1, bool1, paramBoolean1, localPolicyNodeImpl, str, localSet, false);
/*     */ 
/* 496 */           if (!bool2)
/*     */           {
/* 498 */             processParents(paramInt1, bool1, paramBoolean1, localPolicyNodeImpl, str, localSet, true);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 506 */       if ((i != 0) && (
/* 507 */         (paramInt4 > 0) || ((!paramBoolean2) && (X509CertImpl.isSelfIssued(paramX509CertImpl)))))
/*     */       {
/* 509 */         if (debug != null) {
/* 510 */           debug.println("PolicyChecker.processPolicies() processing policy: 2.5.29.32.0");
/*     */         }
/*     */ 
/* 513 */         processParents(paramInt1, bool1, paramBoolean1, localPolicyNodeImpl, "2.5.29.32.0", (Set)localObject, true);
/*     */       }
/*     */ 
/* 520 */       localPolicyNodeImpl.prune(paramInt1);
/* 521 */       if (!localPolicyNodeImpl.getChildren().hasNext())
/* 522 */         localPolicyNodeImpl = null;
/*     */     }
/* 524 */     else if (localCertificatePoliciesExtension == null) {
/* 525 */       if (debug != null) {
/* 526 */         debug.println("PolicyChecker.processPolicies() no policies present in cert");
/*     */       }
/*     */ 
/* 529 */       localPolicyNodeImpl = null;
/*     */     }
/*     */ 
/* 535 */     if ((localPolicyNodeImpl != null) && 
/* 536 */       (!paramBoolean2))
/*     */     {
/* 538 */       localPolicyNodeImpl = processPolicyMappings(paramX509CertImpl, paramInt1, paramInt3, localPolicyNodeImpl, bool1, (Set)localObject);
/*     */     }
/*     */ 
/* 547 */     if ((localPolicyNodeImpl != null) && (!paramSet.contains("2.5.29.32.0")) && (localCertificatePoliciesExtension != null))
/*     */     {
/* 549 */       localPolicyNodeImpl = removeInvalidNodes(localPolicyNodeImpl, paramInt1, paramSet, localCertificatePoliciesExtension);
/*     */ 
/* 553 */       if ((localPolicyNodeImpl != null) && (paramBoolean2))
/*     */       {
/* 555 */         localPolicyNodeImpl = rewriteLeafNodes(paramInt1, paramSet, localPolicyNodeImpl);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 560 */     if (paramBoolean2)
/*     */     {
/* 562 */       paramInt2 = mergeExplicitPolicy(paramInt2, paramX509CertImpl, paramBoolean2);
/*     */     }
/*     */ 
/* 570 */     if ((paramInt2 == 0) && (localPolicyNodeImpl == null)) {
/* 571 */       throw new CertPathValidatorException("non-null policy tree required and policy tree is null", null, null, -1, PKIXReason.INVALID_POLICY);
/*     */     }
/*     */ 
/* 576 */     return localPolicyNodeImpl;
/*     */   }
/*     */ 
/*     */   private static PolicyNodeImpl rewriteLeafNodes(int paramInt, Set<String> paramSet, PolicyNodeImpl paramPolicyNodeImpl)
/*     */   {
/* 595 */     Set localSet1 = paramPolicyNodeImpl.getPolicyNodesValid(paramInt, "2.5.29.32.0");
/*     */ 
/* 597 */     if (localSet1.isEmpty()) {
/* 598 */       return paramPolicyNodeImpl;
/*     */     }
/* 600 */     PolicyNodeImpl localPolicyNodeImpl1 = (PolicyNodeImpl)localSet1.iterator().next();
/* 601 */     PolicyNodeImpl localPolicyNodeImpl2 = (PolicyNodeImpl)localPolicyNodeImpl1.getParent();
/* 602 */     localPolicyNodeImpl2.deleteChild(localPolicyNodeImpl1);
/*     */ 
/* 604 */     HashSet localHashSet = new HashSet(paramSet);
/* 605 */     for (Iterator localIterator1 = paramPolicyNodeImpl.getPolicyNodes(paramInt).iterator(); localIterator1.hasNext(); ) { localObject = (PolicyNodeImpl)localIterator1.next();
/* 606 */       localHashSet.remove(((PolicyNodeImpl)localObject).getValidPolicy());
/*     */     }
/*     */     Object localObject;
/*     */     boolean bool;
/* 608 */     if (localHashSet.isEmpty())
/*     */     {
/* 611 */       paramPolicyNodeImpl.prune(paramInt);
/* 612 */       if (!paramPolicyNodeImpl.getChildren().hasNext())
/* 613 */         paramPolicyNodeImpl = null;
/*     */     }
/*     */     else {
/* 616 */       bool = localPolicyNodeImpl1.isCritical();
/* 617 */       localObject = localPolicyNodeImpl1.getPolicyQualifiers();
/*     */ 
/* 619 */       for (String str : localHashSet) {
/* 620 */         Set localSet2 = Collections.singleton(str);
/* 621 */         PolicyNodeImpl localPolicyNodeImpl3 = new PolicyNodeImpl(localPolicyNodeImpl2, str, (Set)localObject, bool, localSet2, false);
/*     */       }
/*     */     }
/*     */ 
/* 625 */     return paramPolicyNodeImpl;
/*     */   }
/*     */ 
/*     */   private static boolean processParents(int paramInt, boolean paramBoolean1, boolean paramBoolean2, PolicyNodeImpl paramPolicyNodeImpl, String paramString, Set<PolicyQualifierInfo> paramSet, boolean paramBoolean3)
/*     */     throws CertPathValidatorException
/*     */   {
/* 658 */     boolean bool = false;
/*     */ 
/* 660 */     if (debug != null) {
/* 661 */       debug.println("PolicyChecker.processParents(): matchAny = " + paramBoolean3);
/*     */     }
/*     */ 
/* 665 */     Set localSet1 = paramPolicyNodeImpl.getPolicyNodesExpected(paramInt - 1, paramString, paramBoolean3);
/*     */ 
/* 670 */     for (PolicyNodeImpl localPolicyNodeImpl1 : localSet1) {
/* 671 */       if (debug != null) {
/* 672 */         debug.println("PolicyChecker.processParents() found parent:\n" + localPolicyNodeImpl1.asString());
/*     */       }
/*     */ 
/* 675 */       bool = true;
/* 676 */       String str1 = localPolicyNodeImpl1.getValidPolicy();
/*     */ 
/* 678 */       PolicyNodeImpl localPolicyNodeImpl2 = null;
/* 679 */       HashSet localHashSet = null;
/*     */ 
/* 681 */       if (paramString.equals("2.5.29.32.0"))
/*     */       {
/* 683 */         Set localSet2 = localPolicyNodeImpl1.getExpectedPolicies();
/*     */ 
/* 685 */         for (String str2 : localSet2)
/*     */         {
/* 687 */           Iterator localIterator3 = localPolicyNodeImpl1.getChildren();
/*     */           while (true) {
/* 689 */             if (!localIterator3.hasNext()) break label272;
/* 690 */             localObject = (PolicyNodeImpl)localIterator3.next();
/* 691 */             String str3 = ((PolicyNodeImpl)localObject).getValidPolicy();
/* 692 */             if (str2.equals(str3)) {
/* 693 */               if (debug == null) break;
/* 694 */               debug.println(str3 + " in parent's " + "expected policy set already appears in " + "child node"); break;
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 701 */           Object localObject = new HashSet();
/* 702 */           ((Set)localObject).add(str2);
/*     */ 
/* 704 */           localPolicyNodeImpl2 = new PolicyNodeImpl(localPolicyNodeImpl1, str2, paramSet, paramBoolean1, (Set)localObject, false);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 709 */         localHashSet = new HashSet();
/* 710 */         localHashSet.add(paramString);
/*     */ 
/* 712 */         localPolicyNodeImpl2 = new PolicyNodeImpl(localPolicyNodeImpl1, paramString, paramSet, paramBoolean1, localHashSet, false);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 718 */     label272: return bool;
/*     */   }
/*     */ 
/*     */   private static PolicyNodeImpl processPolicyMappings(X509CertImpl paramX509CertImpl, int paramInt1, int paramInt2, PolicyNodeImpl paramPolicyNodeImpl, boolean paramBoolean, Set<PolicyQualifierInfo> paramSet)
/*     */     throws CertPathValidatorException
/*     */   {
/* 742 */     PolicyMappingsExtension localPolicyMappingsExtension = paramX509CertImpl.getPolicyMappingsExtension();
/*     */ 
/* 745 */     if (localPolicyMappingsExtension == null) {
/* 746 */       return paramPolicyNodeImpl;
/*     */     }
/* 748 */     if (debug != null) {
/* 749 */       debug.println("PolicyChecker.processPolicyMappings() inside policyMapping check");
/*     */     }
/*     */ 
/* 752 */     List localList = null;
/*     */     try {
/* 754 */       localList = (List)localPolicyMappingsExtension.get("map");
/*     */     }
/*     */     catch (IOException localIOException) {
/* 757 */       if (debug != null) {
/* 758 */         debug.println("PolicyChecker.processPolicyMappings() mapping exception");
/*     */ 
/* 760 */         localIOException.printStackTrace();
/*     */       }
/* 762 */       throw new CertPathValidatorException("Exception while checking mapping", localIOException);
/*     */     }
/*     */ 
/* 766 */     int i = 0;
/*     */     String str1;
/*     */     String str2;
/*     */     Object localObject2;
/*     */     PolicyNodeImpl localPolicyNodeImpl1;
/* 767 */     for (int j = 0; j < localList.size(); j++) {
/* 768 */       CertificatePolicyMap localCertificatePolicyMap = (CertificatePolicyMap)localList.get(j);
/* 769 */       str1 = localCertificatePolicyMap.getIssuerIdentifier().getIdentifier().toString();
/*     */ 
/* 771 */       str2 = localCertificatePolicyMap.getSubjectIdentifier().getIdentifier().toString();
/*     */ 
/* 773 */       if (debug != null) {
/* 774 */         debug.println("PolicyChecker.processPolicyMappings() issuerDomain = " + str1);
/*     */ 
/* 776 */         debug.println("PolicyChecker.processPolicyMappings() subjectDomain = " + str2);
/*     */       }
/*     */ 
/* 780 */       if (str1.equals("2.5.29.32.0")) {
/* 781 */         throw new CertPathValidatorException("encountered an issuerDomainPolicy of ANY_POLICY", null, null, -1, PKIXReason.INVALID_POLICY);
/*     */       }
/*     */ 
/* 786 */       if (str2.equals("2.5.29.32.0")) {
/* 787 */         throw new CertPathValidatorException("encountered a subjectDomainPolicy of ANY_POLICY", null, null, -1, PKIXReason.INVALID_POLICY);
/*     */       }
/*     */ 
/* 792 */       Set localSet = paramPolicyNodeImpl.getPolicyNodesValid(paramInt1, str1);
/*     */       Object localObject1;
/* 794 */       if (!localSet.isEmpty()) {
/* 795 */         for (localObject1 = localSet.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (PolicyNodeImpl)((Iterator)localObject1).next();
/* 796 */           if ((paramInt2 > 0) || (paramInt2 == -1)) {
/* 797 */             ((PolicyNodeImpl)localObject2).addExpectedPolicy(str2);
/* 798 */           } else if (paramInt2 == 0) {
/* 799 */             localPolicyNodeImpl1 = (PolicyNodeImpl)((PolicyNodeImpl)localObject2).getParent();
/*     */ 
/* 801 */             if (debug != null) {
/* 802 */               debug.println("PolicyChecker.processPolicyMappings() before deleting: policy tree = " + paramPolicyNodeImpl);
/*     */             }
/*     */ 
/* 805 */             localPolicyNodeImpl1.deleteChild((PolicyNode)localObject2);
/* 806 */             i = 1;
/* 807 */             if (debug != null) {
/* 808 */               debug.println("PolicyChecker.processPolicyMappings() after deleting: policy tree = " + paramPolicyNodeImpl);
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/* 814 */       else if ((paramInt2 > 0) || (paramInt2 == -1)) {
/* 815 */         localObject1 = paramPolicyNodeImpl.getPolicyNodesValid(paramInt1, "2.5.29.32.0");
/*     */ 
/* 817 */         for (localObject2 = ((Set)localObject1).iterator(); ((Iterator)localObject2).hasNext(); ) { localPolicyNodeImpl1 = (PolicyNodeImpl)((Iterator)localObject2).next();
/* 818 */           PolicyNodeImpl localPolicyNodeImpl2 = (PolicyNodeImpl)localPolicyNodeImpl1.getParent();
/*     */ 
/* 821 */           HashSet localHashSet = new HashSet();
/* 822 */           localHashSet.add(str2);
/*     */ 
/* 824 */           PolicyNodeImpl localPolicyNodeImpl3 = new PolicyNodeImpl(localPolicyNodeImpl2, str1, paramSet, paramBoolean, localHashSet, true);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 832 */     if (i != 0) {
/* 833 */       paramPolicyNodeImpl.prune(paramInt1);
/* 834 */       if (!paramPolicyNodeImpl.getChildren().hasNext()) {
/* 835 */         if (debug != null)
/* 836 */           debug.println("setting rootNode to null");
/* 837 */         paramPolicyNodeImpl = null;
/*     */       }
/*     */     }
/*     */ 
/* 841 */     return paramPolicyNodeImpl;
/*     */   }
/*     */ 
/*     */   private static PolicyNodeImpl removeInvalidNodes(PolicyNodeImpl paramPolicyNodeImpl, int paramInt, Set<String> paramSet, CertificatePoliciesExtension paramCertificatePoliciesExtension)
/*     */     throws CertPathValidatorException
/*     */   {
/* 861 */     List localList = null;
/*     */     try {
/* 863 */       localList = (List)paramCertificatePoliciesExtension.get("policies");
/*     */     }
/*     */     catch (IOException localIOException) {
/* 866 */       throw new CertPathValidatorException("Exception while retrieving policyOIDs", localIOException);
/*     */     }
/*     */ 
/* 870 */     int i = 0;
/* 871 */     for (PolicyInformation localPolicyInformation : localList) {
/* 872 */       str = localPolicyInformation.getPolicyIdentifier().getIdentifier().toString();
/*     */ 
/* 875 */       if (debug != null) {
/* 876 */         debug.println("PolicyChecker.processPolicies() processing policy second time: " + str);
/*     */       }
/*     */ 
/* 879 */       Set localSet = paramPolicyNodeImpl.getPolicyNodesValid(paramInt, str);
/*     */ 
/* 881 */       for (PolicyNodeImpl localPolicyNodeImpl1 : localSet) {
/* 882 */         PolicyNodeImpl localPolicyNodeImpl2 = (PolicyNodeImpl)localPolicyNodeImpl1.getParent();
/* 883 */         if ((localPolicyNodeImpl2.getValidPolicy().equals("2.5.29.32.0")) && 
/* 884 */           (!paramSet.contains(str)) && (!str.equals("2.5.29.32.0")))
/*     */         {
/* 886 */           if (debug != null) {
/* 887 */             debug.println("PolicyChecker.processPolicies() before deleting: policy tree = " + paramPolicyNodeImpl);
/*     */           }
/* 889 */           localPolicyNodeImpl2.deleteChild(localPolicyNodeImpl1);
/* 890 */           i = 1;
/* 891 */           if (debug != null)
/* 892 */             debug.println("PolicyChecker.processPolicies() after deleting: policy tree = " + paramPolicyNodeImpl);
/*     */         }
/*     */       }
/*     */     }
/*     */     String str;
/* 899 */     if (i != 0) {
/* 900 */       paramPolicyNodeImpl.prune(paramInt);
/* 901 */       if (!paramPolicyNodeImpl.getChildren().hasNext()) {
/* 902 */         paramPolicyNodeImpl = null;
/*     */       }
/*     */     }
/*     */ 
/* 906 */     return paramPolicyNodeImpl;
/*     */   }
/*     */ 
/*     */   PolicyNode getPolicyTree()
/*     */   {
/* 918 */     if (this.rootNode == null) {
/* 919 */       return null;
/*     */     }
/* 921 */     PolicyNodeImpl localPolicyNodeImpl = this.rootNode.copyTree();
/* 922 */     localPolicyNodeImpl.setImmutable();
/* 923 */     return localPolicyNodeImpl;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.PolicyChecker
 * JD-Core Version:    0.6.2
 */