/*     */ package sun.security.provider.certpath;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.AccessController;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.cert.CertStore;
/*     */ import java.security.cert.CertStoreException;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CollectionCertStoreParameters;
/*     */ import java.security.cert.PKIXBuilderParameters;
/*     */ import java.security.cert.X509CertSelector;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import sun.security.action.GetBooleanAction;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.x509.GeneralName;
/*     */ import sun.security.x509.GeneralNameInterface;
/*     */ import sun.security.x509.GeneralNames;
/*     */ import sun.security.x509.GeneralSubtree;
/*     */ import sun.security.x509.GeneralSubtrees;
/*     */ import sun.security.x509.NameConstraintsExtension;
/*     */ import sun.security.x509.SubjectAlternativeNameExtension;
/*     */ import sun.security.x509.X500Name;
/*     */ import sun.security.x509.X509CertImpl;
/*     */ 
/*     */ public abstract class Builder
/*     */ {
/*  57 */   private static final Debug debug = Debug.getInstance("certpath");
/*     */   private Set<String> matchingPolicies;
/*     */   final PKIXBuilderParameters buildParams;
/*     */   final X500Principal targetSubjectDN;
/*     */   final Date date;
/*     */   final X509CertSelector targetCertConstraints;
/*  69 */   static final boolean USE_AIA = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("com.sun.security.enableAIAcaIssuers"))).booleanValue();
/*     */ 
/*     */   Builder(PKIXBuilderParameters paramPKIXBuilderParameters, X500Principal paramX500Principal)
/*     */   {
/*  78 */     this.buildParams = paramPKIXBuilderParameters;
/*  79 */     this.targetSubjectDN = paramX500Principal;
/*     */ 
/*  81 */     Date localDate = paramPKIXBuilderParameters.getDate();
/*  82 */     this.date = (localDate != null ? localDate : new Date());
/*  83 */     this.targetCertConstraints = ((X509CertSelector)paramPKIXBuilderParameters.getTargetCertConstraints());
/*     */   }
/*     */ 
/*     */   abstract Collection<X509Certificate> getMatchingCerts(State paramState, List<CertStore> paramList)
/*     */     throws CertStoreException, CertificateException, IOException;
/*     */ 
/*     */   abstract void verifyCert(X509Certificate paramX509Certificate, State paramState, List<X509Certificate> paramList)
/*     */     throws GeneralSecurityException;
/*     */ 
/*     */   abstract boolean isPathCompleted(X509Certificate paramX509Certificate);
/*     */ 
/*     */   abstract void addCertToPath(X509Certificate paramX509Certificate, LinkedList<X509Certificate> paramLinkedList);
/*     */ 
/*     */   abstract void removeFinalCertFromPath(LinkedList<X509Certificate> paramLinkedList);
/*     */ 
/*     */   static int distance(GeneralNameInterface paramGeneralNameInterface1, GeneralNameInterface paramGeneralNameInterface2, int paramInt)
/*     */   {
/* 151 */     switch (paramGeneralNameInterface1.constrains(paramGeneralNameInterface2)) {
/*     */     case -1:
/* 153 */       if (debug != null) {
/* 154 */         debug.println("Builder.distance(): Names are different types");
/*     */       }
/*     */     case 3:
/* 157 */       if (debug != null) {
/* 158 */         debug.println("Builder.distance(): Names are same type but in different subtrees");
/*     */       }
/*     */ 
/* 161 */       return paramInt;
/*     */     case 0:
/* 163 */       return 0;
/*     */     case 2:
/* 165 */       break;
/*     */     case 1:
/* 167 */       break;
/*     */     }
/* 169 */     return paramInt;
/*     */ 
/* 173 */     return paramGeneralNameInterface2.subtreeDepth() - paramGeneralNameInterface1.subtreeDepth();
/*     */   }
/*     */ 
/*     */   static int hops(GeneralNameInterface paramGeneralNameInterface1, GeneralNameInterface paramGeneralNameInterface2, int paramInt)
/*     */   {
/* 195 */     int i = paramGeneralNameInterface1.constrains(paramGeneralNameInterface2);
/* 196 */     switch (i) {
/*     */     case -1:
/* 198 */       if (debug != null) {
/* 199 */         debug.println("Builder.hops(): Names are different types");
/*     */       }
/* 201 */       return paramInt;
/*     */     case 3:
/* 204 */       break;
/*     */     case 0:
/* 207 */       return 0;
/*     */     case 2:
/* 210 */       return paramGeneralNameInterface2.subtreeDepth() - paramGeneralNameInterface1.subtreeDepth();
/*     */     case 1:
/* 213 */       return paramGeneralNameInterface2.subtreeDepth() - paramGeneralNameInterface1.subtreeDepth();
/*     */     default:
/* 215 */       return paramInt;
/*     */     }
/*     */ 
/* 219 */     if (paramGeneralNameInterface1.getType() != 4) {
/* 220 */       if (debug != null) {
/* 221 */         debug.println("Builder.hops(): hopDistance not implemented for this name type");
/*     */       }
/*     */ 
/* 224 */       return paramInt;
/*     */     }
/* 226 */     X500Name localX500Name1 = (X500Name)paramGeneralNameInterface1;
/* 227 */     X500Name localX500Name2 = (X500Name)paramGeneralNameInterface2;
/* 228 */     X500Name localX500Name3 = localX500Name1.commonAncestor(localX500Name2);
/* 229 */     if (localX500Name3 == null) {
/* 230 */       if (debug != null) {
/* 231 */         debug.println("Builder.hops(): Names are in different namespaces");
/*     */       }
/*     */ 
/* 234 */       return paramInt;
/*     */     }
/* 236 */     int j = localX500Name3.subtreeDepth();
/* 237 */     int k = localX500Name1.subtreeDepth();
/* 238 */     int m = localX500Name2.subtreeDepth();
/* 239 */     return k + m - 2 * j;
/*     */   }
/*     */ 
/*     */   static int targetDistance(NameConstraintsExtension paramNameConstraintsExtension, X509Certificate paramX509Certificate, GeneralNameInterface paramGeneralNameInterface)
/*     */     throws IOException
/*     */   {
/* 288 */     if ((paramNameConstraintsExtension != null) && (!paramNameConstraintsExtension.verify(paramX509Certificate))) {
/* 289 */       throw new IOException("certificate does not satisfy existing name constraints");
/*     */     }
/*     */ 
/*     */     X509CertImpl localX509CertImpl;
/*     */     try
/*     */     {
/* 295 */       localX509CertImpl = X509CertImpl.toImpl(paramX509Certificate);
/*     */     } catch (CertificateException localCertificateException) {
/* 297 */       throw ((IOException)new IOException("Invalid certificate").initCause(localCertificateException));
/*     */     }
/*     */ 
/* 300 */     X500Name localX500Name = X500Name.asX500Name(localX509CertImpl.getSubjectX500Principal());
/* 301 */     if (localX500Name.equals(paramGeneralNameInterface))
/*     */     {
/* 303 */       return 0;
/*     */     }
/*     */ 
/* 306 */     SubjectAlternativeNameExtension localSubjectAlternativeNameExtension = localX509CertImpl.getSubjectAlternativeNameExtension();
/*     */ 
/* 308 */     if (localSubjectAlternativeNameExtension != null) {
/* 309 */       localObject = (GeneralNames)localSubjectAlternativeNameExtension.get("subject_name");
/*     */ 
/* 312 */       if (localObject != null) {
/* 313 */         int i = 0; for (int j = ((GeneralNames)localObject).size(); i < j; i++) {
/* 314 */           GeneralNameInterface localGeneralNameInterface1 = ((GeneralNames)localObject).get(i).getName();
/* 315 */           if (localGeneralNameInterface1.equals(paramGeneralNameInterface)) {
/* 316 */             return 0;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 326 */     Object localObject = localX509CertImpl.getNameConstraintsExtension();
/* 327 */     if (localObject == null) {
/* 328 */       return -1;
/*     */     }
/*     */ 
/* 332 */     if (paramNameConstraintsExtension != null) {
/* 333 */       paramNameConstraintsExtension.merge((NameConstraintsExtension)localObject);
/*     */     }
/*     */     else
/*     */     {
/* 338 */       paramNameConstraintsExtension = (NameConstraintsExtension)((NameConstraintsExtension)localObject).clone();
/*     */     }
/*     */ 
/* 341 */     if (debug != null) {
/* 342 */       debug.println("Builder.targetDistance() merged constraints: " + String.valueOf(paramNameConstraintsExtension));
/*     */     }
/*     */ 
/* 346 */     GeneralSubtrees localGeneralSubtrees1 = (GeneralSubtrees)paramNameConstraintsExtension.get("permitted_subtrees");
/*     */ 
/* 348 */     GeneralSubtrees localGeneralSubtrees2 = (GeneralSubtrees)paramNameConstraintsExtension.get("excluded_subtrees");
/*     */ 
/* 350 */     if (localGeneralSubtrees1 != null) {
/* 351 */       localGeneralSubtrees1.reduce(localGeneralSubtrees2);
/*     */     }
/* 353 */     if (debug != null) {
/* 354 */       debug.println("Builder.targetDistance() reduced constraints: " + localGeneralSubtrees1);
/*     */     }
/*     */ 
/* 358 */     if (!paramNameConstraintsExtension.verify(paramGeneralNameInterface)) {
/* 359 */       throw new IOException("New certificate not allowed to sign certificate for target");
/*     */     }
/*     */ 
/* 363 */     if (localGeneralSubtrees1 == null)
/*     */     {
/* 365 */       return -1;
/*     */     }
/* 367 */     int k = 0; for (int m = localGeneralSubtrees1.size(); k < m; k++) {
/* 368 */       GeneralNameInterface localGeneralNameInterface2 = localGeneralSubtrees1.get(k).getName().getName();
/* 369 */       int n = distance(localGeneralNameInterface2, paramGeneralNameInterface, -1);
/* 370 */       if (n >= 0) {
/* 371 */         return n + 1;
/*     */       }
/*     */     }
/*     */ 
/* 375 */     return -1;
/*     */   }
/*     */ 
/*     */   Set<String> getMatchingPolicies()
/*     */   {
/* 399 */     if (this.matchingPolicies != null) {
/* 400 */       Set localSet = this.buildParams.getInitialPolicies();
/* 401 */       if ((!localSet.isEmpty()) && (!localSet.contains("2.5.29.32.0")) && (this.buildParams.isPolicyMappingInhibited()))
/*     */       {
/* 405 */         localSet.add("2.5.29.32.0");
/* 406 */         this.matchingPolicies = localSet;
/*     */       }
/*     */       else
/*     */       {
/* 410 */         this.matchingPolicies = Collections.emptySet();
/*     */       }
/*     */     }
/* 413 */     return this.matchingPolicies;
/*     */   }
/*     */ 
/*     */   boolean addMatchingCerts(X509CertSelector paramX509CertSelector, Collection<CertStore> paramCollection, Collection<X509Certificate> paramCollection1, boolean paramBoolean)
/*     */   {
/* 433 */     X509Certificate localX509Certificate = paramX509CertSelector.getCertificate();
/* 434 */     if (localX509Certificate != null)
/*     */     {
/* 436 */       if ((paramX509CertSelector.match(localX509Certificate)) && (!X509CertImpl.isSelfSigned(localX509Certificate, this.buildParams.getSigProvider())))
/*     */       {
/* 438 */         if (debug != null) {
/* 439 */           debug.println("Builder.addMatchingCerts: adding target cert");
/*     */         }
/* 441 */         return paramCollection1.add(localX509Certificate);
/*     */       }
/* 443 */       return false;
/*     */     }
/* 445 */     boolean bool = false;
/* 446 */     for (CertStore localCertStore : paramCollection) {
/*     */       try {
/* 448 */         Collection localCollection = localCertStore.getCertificates(paramX509CertSelector);
/*     */ 
/* 450 */         for (Certificate localCertificate : localCollection) {
/* 451 */           if (!X509CertImpl.isSelfSigned((X509Certificate)localCertificate, this.buildParams.getSigProvider()))
/*     */           {
/* 453 */             if (paramCollection1.add((X509Certificate)localCertificate)) {
/* 454 */               bool = true;
/*     */             }
/*     */           }
/*     */         }
/* 458 */         if ((!paramBoolean) && (bool)) {
/* 459 */           return true;
/*     */         }
/*     */       }
/*     */       catch (CertStoreException localCertStoreException)
/*     */       {
/* 464 */         if (debug != null) {
/* 465 */           debug.println("Builder.addMatchingCerts, non-fatal exception retrieving certs: " + localCertStoreException);
/*     */ 
/* 467 */           localCertStoreException.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/* 471 */     return bool;
/*     */   }
/*     */ 
/*     */   static boolean isLocalCertStore(CertStore paramCertStore)
/*     */   {
/* 481 */     return (paramCertStore.getType().equals("Collection")) || ((paramCertStore.getCertStoreParameters() instanceof CollectionCertStoreParameters));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.Builder
 * JD-Core Version:    0.6.2
 */