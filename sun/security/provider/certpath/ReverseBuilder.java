/*     */ package sun.security.provider.certpath;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.cert.CertPathValidatorException;
/*     */ import java.security.cert.CertStore;
/*     */ import java.security.cert.CertStoreException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.PKIXBuilderParameters;
/*     */ import java.security.cert.PKIXCertPathChecker;
/*     */ import java.security.cert.PKIXReason;
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
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ import sun.security.x509.NameConstraintsExtension;
/*     */ import sun.security.x509.PKIXExtensions;
/*     */ import sun.security.x509.X500Name;
/*     */ import sun.security.x509.X509CertImpl;
/*     */ 
/*     */ class ReverseBuilder extends Builder
/*     */ {
/*  73 */   private Debug debug = Debug.getInstance("certpath");
/*     */   Set<String> initPolicies;
/*     */ 
/*     */   ReverseBuilder(PKIXBuilderParameters paramPKIXBuilderParameters, X500Principal paramX500Principal)
/*     */   {
/*  85 */     super(paramPKIXBuilderParameters, paramX500Principal);
/*     */ 
/*  87 */     Set localSet = paramPKIXBuilderParameters.getInitialPolicies();
/*  88 */     this.initPolicies = new HashSet();
/*  89 */     if (localSet.isEmpty())
/*     */     {
/*  92 */       this.initPolicies.add("2.5.29.32.0");
/*     */     }
/*  94 */     else for (String str : localSet)
/*  95 */         this.initPolicies.add(str);
/*     */   }
/*     */ 
/*     */   Collection<X509Certificate> getMatchingCerts(State paramState, List<CertStore> paramList)
/*     */     throws CertStoreException, CertificateException, IOException
/*     */   {
/* 113 */     ReverseState localReverseState = (ReverseState)paramState;
/*     */ 
/* 115 */     if (this.debug != null) {
/* 116 */       this.debug.println("In ReverseBuilder.getMatchingCerts.");
/*     */     }
/*     */ 
/* 126 */     Collection localCollection = getMatchingEECerts(localReverseState, paramList);
/*     */ 
/* 128 */     localCollection.addAll(getMatchingCACerts(localReverseState, paramList));
/*     */ 
/* 130 */     return localCollection;
/*     */   }
/*     */ 
/*     */   private Collection<X509Certificate> getMatchingEECerts(ReverseState paramReverseState, List<CertStore> paramList)
/*     */     throws CertStoreException, CertificateException, IOException
/*     */   {
/* 148 */     X509CertSelector localX509CertSelector = (X509CertSelector)this.targetCertConstraints.clone();
/*     */ 
/* 153 */     localX509CertSelector.setIssuer(paramReverseState.subjectDN);
/*     */ 
/* 158 */     localX509CertSelector.setCertificateValid(this.date);
/*     */ 
/* 163 */     if (paramReverseState.explicitPolicy == 0) {
/* 164 */       localX509CertSelector.setPolicy(getMatchingPolicies());
/*     */     }
/*     */ 
/* 180 */     localX509CertSelector.setBasicConstraints(-2);
/*     */ 
/* 183 */     HashSet localHashSet = new HashSet();
/* 184 */     addMatchingCerts(localX509CertSelector, paramList, localHashSet, true);
/*     */ 
/* 186 */     if (this.debug != null) {
/* 187 */       this.debug.println("ReverseBuilder.getMatchingEECerts got " + localHashSet.size() + " certs.");
/*     */     }
/*     */ 
/* 190 */     return localHashSet;
/*     */   }
/*     */ 
/*     */   private Collection<X509Certificate> getMatchingCACerts(ReverseState paramReverseState, List<CertStore> paramList)
/*     */     throws CertificateException, CertStoreException, IOException
/*     */   {
/* 205 */     X509CertSelector localX509CertSelector = new X509CertSelector();
/*     */ 
/* 210 */     localX509CertSelector.setIssuer(paramReverseState.subjectDN);
/*     */ 
/* 215 */     localX509CertSelector.setCertificateValid(this.date);
/*     */ 
/* 222 */     localX509CertSelector.addPathToName(4, this.targetCertConstraints.getSubjectAsBytes());
/*     */ 
/* 227 */     if (paramReverseState.explicitPolicy == 0) {
/* 228 */       localX509CertSelector.setPolicy(getMatchingPolicies());
/*     */     }
/*     */ 
/* 244 */     localX509CertSelector.setBasicConstraints(0);
/*     */ 
/* 247 */     ArrayList localArrayList = new ArrayList();
/*     */ 
/* 249 */     addMatchingCerts(localX509CertSelector, paramList, localArrayList, true);
/*     */ 
/* 252 */     Collections.sort(localArrayList, new PKIXCertComparator());
/*     */ 
/* 254 */     if (this.debug != null) {
/* 255 */       this.debug.println("ReverseBuilder.getMatchingCACerts got " + localArrayList.size() + " certs.");
/*     */     }
/* 257 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   void verifyCert(X509Certificate paramX509Certificate, State paramState, List<X509Certificate> paramList)
/*     */     throws GeneralSecurityException
/*     */   {
/* 337 */     if (this.debug != null) {
/* 338 */       this.debug.println("ReverseBuilder.verifyCert(SN: " + Debug.toHexString(paramX509Certificate.getSerialNumber()) + "\n  Subject: " + paramX509Certificate.getSubjectX500Principal() + ")");
/*     */     }
/*     */ 
/* 343 */     ReverseState localReverseState = (ReverseState)paramState;
/*     */ 
/* 346 */     if (localReverseState.isInitial()) {
/* 347 */       return;
/*     */     }
/*     */ 
/* 351 */     localReverseState.untrustedChecker.check(paramX509Certificate, Collections.emptySet());
/*     */     Object localObject1;
/* 364 */     if ((paramList != null) && (!paramList.isEmpty())) {
/* 365 */       ArrayList localArrayList = new ArrayList();
/*     */ 
/* 367 */       for (Iterator localIterator = paramList.iterator(); localIterator.hasNext(); ) { localObject1 = (X509Certificate)localIterator.next();
/* 368 */         localArrayList.add(0, localObject1);
/*     */       }
/*     */ 
/* 371 */       bool2 = false;
/* 372 */       for (localObject1 = localArrayList.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (X509Certificate)((Iterator)localObject1).next();
/* 373 */         localObject3 = X509CertImpl.toImpl((X509Certificate)localObject2);
/* 374 */         localObject4 = ((X509CertImpl)localObject3).getPolicyMappingsExtension();
/*     */ 
/* 376 */         if (localObject4 != null) {
/* 377 */           bool2 = true;
/*     */         }
/* 379 */         if (this.debug != null)
/* 380 */           this.debug.println("policyMappingFound = " + bool2);
/* 381 */         if ((paramX509Certificate.equals(localObject2)) && (
/* 382 */           (this.buildParams.isPolicyMappingInhibited()) || (!bool2)))
/*     */         {
/* 384 */           if (this.debug != null)
/* 385 */             this.debug.println("loop detected!!");
/* 386 */           throw new CertPathValidatorException("loop detected");
/*     */         }
/*     */       }
/*     */     }
/*     */     Object localObject4;
/* 393 */     boolean bool1 = paramX509Certificate.getSubjectX500Principal().equals(this.targetSubjectDN);
/*     */ 
/* 396 */     boolean bool2 = paramX509Certificate.getBasicConstraints() != -1;
/*     */ 
/* 399 */     if (!bool1)
/*     */     {
/* 402 */       if (!bool2) {
/* 403 */         throw new CertPathValidatorException("cert is NOT a CA cert");
/*     */       }
/*     */ 
/* 408 */       if ((localReverseState.remainingCACerts <= 0) && (!X509CertImpl.isSelfIssued(paramX509Certificate))) {
/* 409 */         throw new CertPathValidatorException("pathLenConstraint violated, path too long", null, null, -1, PKIXReason.PATH_TOO_LONG);
/*     */       }
/*     */ 
/* 417 */       KeyChecker.verifyCAKeyUsage(paramX509Certificate);
/*     */     }
/* 425 */     else if (!this.targetCertConstraints.match(paramX509Certificate)) {
/* 426 */       throw new CertPathValidatorException("target certificate constraints check failed");
/*     */     }
/*     */ 
/* 434 */     if (this.buildParams.isRevocationEnabled())
/*     */     {
/* 436 */       localReverseState.crlChecker.check(paramX509Certificate, localReverseState.pubKey, localReverseState.crlSign);
/*     */     }
/*     */ 
/* 442 */     if (((bool1) || (!X509CertImpl.isSelfIssued(paramX509Certificate))) && 
/* 443 */       (localReverseState.nc != null)) {
/*     */       try {
/* 445 */         if (!localReverseState.nc.verify(paramX509Certificate)) {
/* 446 */           throw new CertPathValidatorException("name constraints check failed", null, null, -1, PKIXReason.INVALID_NAME);
/*     */         }
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/* 451 */         throw new CertPathValidatorException(localIOException);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 459 */     X509CertImpl localX509CertImpl = X509CertImpl.toImpl(paramX509Certificate);
/* 460 */     localReverseState.rootNode = PolicyChecker.processPolicies(localReverseState.certIndex, this.initPolicies, localReverseState.explicitPolicy, localReverseState.policyMapping, localReverseState.inhibitAnyPolicy, this.buildParams.getPolicyQualifiersRejected(), localReverseState.rootNode, localX509CertImpl, bool1);
/*     */ 
/* 470 */     Object localObject2 = paramX509Certificate.getCriticalExtensionOIDs();
/* 471 */     if (localObject2 == null) {
/* 472 */       localObject2 = Collections.emptySet();
/*     */     }
/*     */ 
/* 478 */     localReverseState.algorithmChecker.check(paramX509Certificate, (Collection)localObject2);
/*     */ 
/* 480 */     for (Object localObject3 = localReverseState.userCheckers.iterator(); ((Iterator)localObject3).hasNext(); ) { localObject4 = (PKIXCertPathChecker)((Iterator)localObject3).next();
/* 481 */       ((PKIXCertPathChecker)localObject4).check(paramX509Certificate, (Collection)localObject2);
/*     */     }
/*     */ 
/* 488 */     if (!((Set)localObject2).isEmpty()) {
/* 489 */       ((Set)localObject2).remove(PKIXExtensions.BasicConstraints_Id.toString());
/* 490 */       ((Set)localObject2).remove(PKIXExtensions.NameConstraints_Id.toString());
/* 491 */       ((Set)localObject2).remove(PKIXExtensions.CertificatePolicies_Id.toString());
/* 492 */       ((Set)localObject2).remove(PKIXExtensions.PolicyMappings_Id.toString());
/* 493 */       ((Set)localObject2).remove(PKIXExtensions.PolicyConstraints_Id.toString());
/* 494 */       ((Set)localObject2).remove(PKIXExtensions.InhibitAnyPolicy_Id.toString());
/* 495 */       ((Set)localObject2).remove(PKIXExtensions.SubjectAlternativeName_Id.toString());
/* 496 */       ((Set)localObject2).remove(PKIXExtensions.KeyUsage_Id.toString());
/* 497 */       ((Set)localObject2).remove(PKIXExtensions.ExtendedKeyUsage_Id.toString());
/*     */ 
/* 499 */       if (!((Set)localObject2).isEmpty()) {
/* 500 */         throw new CertPathValidatorException("Unrecognized critical extension(s)", null, null, -1, PKIXReason.UNRECOGNIZED_CRIT_EXT);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 508 */     if (this.buildParams.getSigProvider() != null)
/* 509 */       paramX509Certificate.verify(localReverseState.pubKey, this.buildParams.getSigProvider());
/*     */     else
/* 511 */       paramX509Certificate.verify(localReverseState.pubKey);
/*     */   }
/*     */ 
/*     */   boolean isPathCompleted(X509Certificate paramX509Certificate)
/*     */   {
/* 523 */     return paramX509Certificate.getSubjectX500Principal().equals(this.targetSubjectDN);
/*     */   }
/*     */ 
/*     */   void addCertToPath(X509Certificate paramX509Certificate, LinkedList<X509Certificate> paramLinkedList)
/*     */   {
/* 533 */     paramLinkedList.addLast(paramX509Certificate);
/*     */   }
/*     */ 
/*     */   void removeFinalCertFromPath(LinkedList<X509Certificate> paramLinkedList)
/*     */   {
/* 541 */     paramLinkedList.removeLast();
/*     */   }
/*     */ 
/*     */   class PKIXCertComparator
/*     */     implements Comparator<X509Certificate>
/*     */   {
/* 270 */     private Debug debug = Debug.getInstance("certpath");
/*     */ 
/*     */     PKIXCertComparator()
/*     */     {
/*     */     }
/*     */ 
/*     */     public int compare(X509Certificate paramX509Certificate1, X509Certificate paramX509Certificate2)
/*     */     {
/* 278 */       if (paramX509Certificate1.getSubjectX500Principal().equals(ReverseBuilder.this.targetSubjectDN)) {
/* 279 */         return -1;
/*     */       }
/* 281 */       if (paramX509Certificate2.getSubjectX500Principal().equals(ReverseBuilder.this.targetSubjectDN))
/* 282 */         return 1;
/*     */       int i;
/*     */       int j;
/*     */       try
/*     */       {
/* 288 */         X500Name localX500Name = X500Name.asX500Name(ReverseBuilder.this.targetSubjectDN);
/* 289 */         i = Builder.targetDistance(null, paramX509Certificate1, localX500Name);
/*     */ 
/* 291 */         j = Builder.targetDistance(null, paramX509Certificate2, localX500Name);
/*     */       }
/*     */       catch (IOException localIOException) {
/* 294 */         if (this.debug != null) {
/* 295 */           this.debug.println("IOException in call to Builder.targetDistance");
/* 296 */           localIOException.printStackTrace();
/*     */         }
/* 298 */         throw new ClassCastException("Invalid target subject distinguished name");
/*     */       }
/*     */ 
/* 302 */       if (i == j) {
/* 303 */         return 0;
/*     */       }
/* 305 */       if (i == -1) {
/* 306 */         return 1;
/*     */       }
/* 308 */       if (i < j) {
/* 309 */         return -1;
/*     */       }
/* 311 */       return 1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.ReverseBuilder
 * JD-Core Version:    0.6.2
 */