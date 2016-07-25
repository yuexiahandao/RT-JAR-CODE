/*     */ package sun.security.provider.certpath;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.security.AccessController;
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.PublicKey;
/*     */ import java.security.cert.CRL;
/*     */ import java.security.cert.CRLException;
/*     */ import java.security.cert.CertPathBuilder;
/*     */ import java.security.cert.CertPathParameters;
/*     */ import java.security.cert.CertPathValidatorException;
/*     */ import java.security.cert.CertStore;
/*     */ import java.security.cert.CertStoreException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.PKIXBuilderParameters;
/*     */ import java.security.cert.PKIXCertPathBuilderResult;
/*     */ import java.security.cert.TrustAnchor;
/*     */ import java.security.cert.X509CRL;
/*     */ import java.security.cert.X509CRLSelector;
/*     */ import java.security.cert.X509CertSelector;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import sun.security.action.GetBooleanAction;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ import sun.security.x509.AuthorityKeyIdentifierExtension;
/*     */ import sun.security.x509.CRLDistributionPointsExtension;
/*     */ import sun.security.x509.DistributionPoint;
/*     */ import sun.security.x509.DistributionPointName;
/*     */ import sun.security.x509.GeneralName;
/*     */ import sun.security.x509.GeneralNames;
/*     */ import sun.security.x509.IssuingDistributionPointExtension;
/*     */ import sun.security.x509.KeyIdentifier;
/*     */ import sun.security.x509.PKIXExtensions;
/*     */ import sun.security.x509.RDN;
/*     */ import sun.security.x509.ReasonFlags;
/*     */ import sun.security.x509.SerialNumber;
/*     */ import sun.security.x509.URIName;
/*     */ import sun.security.x509.X500Name;
/*     */ import sun.security.x509.X509CRLImpl;
/*     */ import sun.security.x509.X509CertImpl;
/*     */ 
/*     */ public class DistributionPointFetcher
/*     */ {
/*  55 */   private static final Debug debug = Debug.getInstance("certpath");
/*     */ 
/*  57 */   private static final boolean[] ALL_REASONS = { true, true, true, true, true, true, true, true, true };
/*     */ 
/*  65 */   private static final boolean USE_CRLDP = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("com.sun.security.enableCRLDP"))).booleanValue();
/*     */ 
/*     */   public static Collection<X509CRL> getCRLs(X509CRLSelector paramX509CRLSelector, boolean paramBoolean, PublicKey paramPublicKey, String paramString, List<CertStore> paramList, boolean[] paramArrayOfBoolean, Set<TrustAnchor> paramSet, Date paramDate)
/*     */     throws CertStoreException
/*     */   {
/*  90 */     if (!USE_CRLDP) {
/*  91 */       return Collections.emptySet();
/*     */     }
/*  93 */     X509Certificate localX509Certificate = paramX509CRLSelector.getCertificateChecking();
/*  94 */     if (localX509Certificate == null)
/*  95 */       return Collections.emptySet();
/*     */     try
/*     */     {
/*  98 */       X509CertImpl localX509CertImpl = X509CertImpl.toImpl(localX509Certificate);
/*  99 */       if (debug != null) {
/* 100 */         debug.println("DistributionPointFetcher.getCRLs: Checking CRLDPs for " + localX509CertImpl.getSubjectX500Principal());
/*     */       }
/*     */ 
/* 103 */       CRLDistributionPointsExtension localCRLDistributionPointsExtension = localX509CertImpl.getCRLDistributionPointsExtension();
/*     */ 
/* 105 */       if (localCRLDistributionPointsExtension == null) {
/* 106 */         if (debug != null) {
/* 107 */           debug.println("No CRLDP ext");
/*     */         }
/* 109 */         return Collections.emptySet();
/*     */       }
/* 111 */       List localList = (List)localCRLDistributionPointsExtension.get("points");
/*     */ 
/* 113 */       HashSet localHashSet = new HashSet();
/* 114 */       Iterator localIterator = localList.iterator();
/* 115 */       while ((localIterator.hasNext()) && (!Arrays.equals(paramArrayOfBoolean, ALL_REASONS))) {
/* 116 */         DistributionPoint localDistributionPoint = (DistributionPoint)localIterator.next();
/* 117 */         Collection localCollection = getCRLs(paramX509CRLSelector, localX509CertImpl, localDistributionPoint, paramArrayOfBoolean, paramBoolean, paramPublicKey, paramString, paramList, paramSet, paramDate);
/*     */ 
/* 120 */         localHashSet.addAll(localCollection);
/*     */       }
/* 122 */       if (debug != null) {
/* 123 */         debug.println("Returning " + localHashSet.size() + " CRLs");
/*     */       }
/* 125 */       return localHashSet;
/*     */     } catch (CertificateException localCertificateException) {
/* 127 */       return Collections.emptySet(); } catch (IOException localIOException) {
/*     */     }
/* 129 */     return Collections.emptySet();
/*     */   }
/*     */ 
/*     */   private static Collection<X509CRL> getCRLs(X509CRLSelector paramX509CRLSelector, X509CertImpl paramX509CertImpl, DistributionPoint paramDistributionPoint, boolean[] paramArrayOfBoolean, boolean paramBoolean, PublicKey paramPublicKey, String paramString, List<CertStore> paramList, Set<TrustAnchor> paramSet, Date paramDate)
/*     */   {
/* 144 */     GeneralNames localGeneralNames1 = paramDistributionPoint.getFullName();
/* 145 */     if (localGeneralNames1 == null)
/*     */     {
/* 147 */       localObject1 = paramDistributionPoint.getRelativeName();
/* 148 */       if (localObject1 == null)
/* 149 */         return Collections.emptySet();
/*     */       try
/*     */       {
/* 152 */         GeneralNames localGeneralNames2 = paramDistributionPoint.getCRLIssuer();
/* 153 */         if (localGeneralNames2 == null) {
/* 154 */           localGeneralNames1 = getFullNames((X500Name)paramX509CertImpl.getIssuerDN(), (RDN)localObject1);
/*     */         }
/*     */         else
/*     */         {
/* 158 */           if (localGeneralNames2.size() != 1) {
/* 159 */             return Collections.emptySet();
/*     */           }
/* 161 */           localGeneralNames1 = getFullNames((X500Name)localGeneralNames2.get(0).getName(), (RDN)localObject1);
/*     */         }
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/* 166 */         return Collections.emptySet();
/*     */       }
/*     */     }
/* 169 */     Object localObject1 = new ArrayList();
/* 170 */     ArrayList localArrayList = new ArrayList(2);
/* 171 */     for (Iterator localIterator = localGeneralNames1.iterator(); localIterator.hasNext(); ) {
/* 172 */       localObject2 = (GeneralName)localIterator.next();
/*     */       Object localObject3;
/* 173 */       if (((GeneralName)localObject2).getType() == 4) {
/* 174 */         localObject3 = (X500Name)((GeneralName)localObject2).getName();
/* 175 */         ((Collection)localObject1).addAll(getCRLs((X500Name)localObject3, paramX509CertImpl.getIssuerX500Principal(), paramList));
/*     */       }
/* 178 */       else if (((GeneralName)localObject2).getType() == 6) {
/* 179 */         localObject3 = (URIName)((GeneralName)localObject2).getName();
/* 180 */         X509CRL localX509CRL = getCRL((URIName)localObject3);
/* 181 */         if (localX509CRL != null)
/* 182 */           ((Collection)localObject1).add(localX509CRL);
/*     */       }
/*     */     }
/* 187 */     Object localObject2;
/* 187 */     for (localIterator = ((Collection)localObject1).iterator(); localIterator.hasNext(); ) { localObject2 = (X509CRL)localIterator.next();
/*     */       try
/*     */       {
/* 191 */         paramX509CRLSelector.setIssuerNames(null);
/* 192 */         if ((paramX509CRLSelector.match((CRL)localObject2)) && (verifyCRL(paramX509CertImpl, paramDistributionPoint, (X509CRL)localObject2, paramArrayOfBoolean, paramBoolean, paramPublicKey, paramString, paramSet, paramList, paramDate)))
/*     */         {
/* 195 */           localArrayList.add(localObject2);
/*     */         }
/*     */       }
/*     */       catch (Exception localException) {
/* 199 */         if (debug != null) {
/* 200 */           debug.println("Exception verifying CRL: " + localException.getMessage());
/* 201 */           localException.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/* 205 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   private static X509CRL getCRL(URIName paramURIName)
/*     */   {
/* 212 */     URI localURI = paramURIName.getURI();
/* 213 */     if (debug != null)
/* 214 */       debug.println("Trying to fetch CRL from DP " + localURI);
/*     */     try
/*     */     {
/* 217 */       CertStore localCertStore = URICertStore.getInstance(new URICertStore.URICertStoreParameters(localURI));
/*     */ 
/* 219 */       Collection localCollection = localCertStore.getCRLs(null);
/* 220 */       if (localCollection.isEmpty()) {
/* 221 */         return null;
/*     */       }
/* 223 */       return (X509CRL)localCollection.iterator().next();
/*     */     }
/*     */     catch (Exception localException) {
/* 226 */       if (debug != null) {
/* 227 */         debug.println("Exception getting CRL from CertStore: " + localException);
/* 228 */         localException.printStackTrace();
/*     */       }
/*     */     }
/* 231 */     return null;
/*     */   }
/*     */ 
/*     */   private static Collection<X509CRL> getCRLs(X500Name paramX500Name, X500Principal paramX500Principal, List<CertStore> paramList)
/*     */   {
/* 240 */     if (debug != null) {
/* 241 */       debug.println("Trying to fetch CRL from DP " + paramX500Name);
/*     */     }
/* 243 */     X509CRLSelector localX509CRLSelector = new X509CRLSelector();
/* 244 */     localX509CRLSelector.addIssuer(paramX500Name.asX500Principal());
/* 245 */     localX509CRLSelector.addIssuer(paramX500Principal);
/* 246 */     ArrayList localArrayList = new ArrayList();
/* 247 */     for (CertStore localCertStore : paramList) {
/*     */       try {
/* 249 */         for (CRL localCRL : localCertStore.getCRLs(localX509CRLSelector))
/* 250 */           localArrayList.add((X509CRL)localCRL);
/*     */       }
/*     */       catch (CertStoreException localCertStoreException)
/*     */       {
/* 254 */         if (debug != null) {
/* 255 */           debug.println("Non-fatal exception while retrieving CRLs: " + localCertStoreException);
/*     */ 
/* 257 */           localCertStoreException.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/* 261 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   static boolean verifyCRL(X509CertImpl paramX509CertImpl, DistributionPoint paramDistributionPoint, X509CRL paramX509CRL, boolean[] paramArrayOfBoolean, boolean paramBoolean, PublicKey paramPublicKey, String paramString, Set<TrustAnchor> paramSet, List<CertStore> paramList, Date paramDate)
/*     */     throws CRLException, IOException
/*     */   {
/* 288 */     int i = 0;
/* 289 */     X509CRLImpl localX509CRLImpl = X509CRLImpl.toImpl(paramX509CRL);
/* 290 */     IssuingDistributionPointExtension localIssuingDistributionPointExtension = localX509CRLImpl.getIssuingDistributionPointExtension();
/*     */ 
/* 292 */     X500Name localX500Name1 = (X500Name)paramX509CertImpl.getIssuerDN();
/* 293 */     X500Name localX500Name2 = (X500Name)localX509CRLImpl.getIssuerDN();
/*     */ 
/* 299 */     GeneralNames localGeneralNames = paramDistributionPoint.getCRLIssuer();
/* 300 */     X500Name localX500Name3 = null;
/*     */     Object localObject3;
/* 301 */     if (localGeneralNames != null) {
/* 302 */       if ((localIssuingDistributionPointExtension == null) || (((Boolean)localIssuingDistributionPointExtension.get("indirect_crl")).equals(Boolean.FALSE)))
/*     */       {
/* 306 */         return false;
/*     */       }
/* 308 */       int j = 0;
/* 309 */       localObject2 = localGeneralNames.iterator();
/* 310 */       while ((j == 0) && (((Iterator)localObject2).hasNext())) {
/* 311 */         localObject3 = ((GeneralName)((Iterator)localObject2).next()).getName();
/* 312 */         if (localX500Name2.equals(localObject3) == true) {
/* 313 */           localX500Name3 = (X500Name)localObject3;
/* 314 */           j = 1;
/*     */         }
/*     */       }
/* 317 */       if (j == 0) {
/* 318 */         return false;
/*     */       }
/*     */ 
/* 323 */       if (issues(paramX509CertImpl, localX509CRLImpl, paramString))
/*     */       {
/* 325 */         paramPublicKey = paramX509CertImpl.getPublicKey();
/*     */       }
/* 327 */       else i = 1; 
/*     */     }
/* 329 */     else { if (!localX500Name2.equals(localX500Name1)) {
/* 330 */         if (debug != null) {
/* 331 */           debug.println("crl issuer does not equal cert issuer");
/*     */         }
/* 333 */         return false;
/*     */       }
/*     */ 
/* 336 */       localObject1 = paramX509CertImpl.getExtensionValue(PKIXExtensions.AuthorityKey_Id.toString());
/*     */ 
/* 338 */       localObject2 = localX509CRLImpl.getExtensionValue(PKIXExtensions.AuthorityKey_Id.toString());
/*     */ 
/* 341 */       if ((localObject1 == null) || (localObject2 == null))
/*     */       {
/* 346 */         if (issues(paramX509CertImpl, localX509CRLImpl, paramString))
/*     */         {
/* 348 */           paramPublicKey = paramX509CertImpl.getPublicKey();
/*     */         }
/* 350 */       } else if (!Arrays.equals((byte[])localObject1, (byte[])localObject2))
/*     */       {
/* 353 */         if (issues(paramX509CertImpl, localX509CRLImpl, paramString))
/*     */         {
/* 355 */           paramPublicKey = paramX509CertImpl.getPublicKey();
/*     */         }
/* 357 */         else i = 1;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 362 */     if ((i == 0) && (!paramBoolean))
/*     */     {
/* 364 */       return false;
/*     */     }
/*     */     Object localObject6;
/*     */     Object localObject7;
/*     */     Object localObject8;
/*     */     Object localObject4;
/* 367 */     if (localIssuingDistributionPointExtension != null) {
/* 368 */       localObject1 = (DistributionPointName)localIssuingDistributionPointExtension.get("point");
/*     */ 
/* 370 */       if (localObject1 != null) {
/* 371 */         localObject2 = ((DistributionPointName)localObject1).getFullName();
/* 372 */         if (localObject2 == null) {
/* 373 */           localObject3 = ((DistributionPointName)localObject1).getRelativeName();
/* 374 */           if (localObject3 == null) {
/* 375 */             if (debug != null) {
/* 376 */               debug.println("IDP must be relative or full DN");
/*     */             }
/* 378 */             return false;
/*     */           }
/* 380 */           if (debug != null) {
/* 381 */             debug.println("IDP relativeName:" + localObject3);
/*     */           }
/* 383 */           localObject2 = getFullNames(localX500Name2, (RDN)localObject3);
/*     */         }
/*     */         Object localObject5;
/* 388 */         if ((paramDistributionPoint.getFullName() != null) || (paramDistributionPoint.getRelativeName() != null))
/*     */         {
/* 390 */           localObject3 = paramDistributionPoint.getFullName();
/* 391 */           if (localObject3 == null) {
/* 392 */             RDN localRDN = paramDistributionPoint.getRelativeName();
/* 393 */             if (localRDN == null) {
/* 394 */               if (debug != null) {
/* 395 */                 debug.println("DP must be relative or full DN");
/*     */               }
/* 397 */               return false;
/*     */             }
/* 399 */             if (debug != null) {
/* 400 */               debug.println("DP relativeName:" + localRDN);
/*     */             }
/* 402 */             if (i != 0) {
/* 403 */               if (localGeneralNames.size() != 1)
/*     */               {
/* 406 */                 if (debug != null) {
/* 407 */                   debug.println("must only be one CRL issuer when relative name present");
/*     */                 }
/*     */ 
/* 410 */                 return false;
/*     */               }
/* 412 */               localObject3 = getFullNames(localX500Name3, localRDN);
/*     */             }
/*     */             else {
/* 415 */               localObject3 = getFullNames(localX500Name1, localRDN);
/*     */             }
/*     */           }
/* 418 */           boolean bool2 = false;
/* 419 */           localObject5 = ((GeneralNames)localObject2).iterator();
/* 420 */           while ((!bool2) && (((Iterator)localObject5).hasNext())) {
/* 421 */             localObject6 = ((GeneralName)((Iterator)localObject5).next()).getName();
/* 422 */             if (debug != null) {
/* 423 */               debug.println("idpName: " + localObject6);
/*     */             }
/* 425 */             localObject7 = ((GeneralNames)localObject3).iterator();
/* 426 */             while ((!bool2) && (((Iterator)localObject7).hasNext())) {
/* 427 */               localObject8 = ((GeneralName)((Iterator)localObject7).next()).getName();
/* 428 */               if (debug != null) {
/* 429 */                 debug.println("pointName: " + localObject8);
/*     */               }
/* 431 */               bool2 = localObject6.equals(localObject8);
/*     */             }
/*     */           }
/* 434 */           if (!bool2) {
/* 435 */             if (debug != null) {
/* 436 */               debug.println("IDP name does not match DP name");
/*     */             }
/* 438 */             return false;
/*     */           }
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 447 */           boolean bool1 = false;
/* 448 */           localObject4 = localGeneralNames.iterator();
/* 449 */           while ((!bool1) && (((Iterator)localObject4).hasNext())) {
/* 450 */             localObject5 = ((GeneralName)((Iterator)localObject4).next()).getName();
/* 451 */             localObject6 = ((GeneralNames)localObject2).iterator();
/* 452 */             while ((!bool1) && (((Iterator)localObject6).hasNext())) {
/* 453 */               localObject7 = ((GeneralName)((Iterator)localObject6).next()).getName();
/* 454 */               bool1 = localObject5.equals(localObject7);
/*     */             }
/*     */           }
/* 457 */           if (!bool1) {
/* 458 */             return false;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 465 */       localObject2 = (Boolean)localIssuingDistributionPointExtension.get("only_user_certs");
/*     */ 
/* 467 */       if ((((Boolean)localObject2).equals(Boolean.TRUE)) && (paramX509CertImpl.getBasicConstraints() != -1)) {
/* 468 */         if (debug != null) {
/* 469 */           debug.println("cert must be a EE cert");
/*     */         }
/* 471 */         return false;
/*     */       }
/*     */ 
/* 476 */       localObject2 = (Boolean)localIssuingDistributionPointExtension.get("only_ca_certs");
/*     */ 
/* 478 */       if ((((Boolean)localObject2).equals(Boolean.TRUE)) && (paramX509CertImpl.getBasicConstraints() == -1)) {
/* 479 */         if (debug != null) {
/* 480 */           debug.println("cert must be a CA cert");
/*     */         }
/* 482 */         return false;
/*     */       }
/*     */ 
/* 487 */       localObject2 = (Boolean)localIssuingDistributionPointExtension.get("only_attribute_certs");
/*     */ 
/* 489 */       if (((Boolean)localObject2).equals(Boolean.TRUE)) {
/* 490 */         if (debug != null) {
/* 491 */           debug.println("cert must not be an AA cert");
/*     */         }
/* 493 */         return false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 498 */     Object localObject1 = new boolean[9];
/* 499 */     Object localObject2 = null;
/* 500 */     if (localIssuingDistributionPointExtension != null) {
/* 501 */       localObject2 = (ReasonFlags)localIssuingDistributionPointExtension.get("reasons");
/*     */     }
/*     */ 
/* 505 */     boolean[] arrayOfBoolean = paramDistributionPoint.getReasonFlags();
/* 506 */     if (localObject2 != null) {
/* 507 */       if (arrayOfBoolean != null)
/*     */       {
/* 510 */         localObject4 = ((ReasonFlags)localObject2).getFlags();
/* 511 */         for (m = 0; m < localObject1.length; m++) {
/* 512 */           localObject1[m] = ((m < localObject4.length) && (localObject4[m] != 0) && (m < arrayOfBoolean.length) && (arrayOfBoolean[m] != 0) ? 1 : 0);
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 520 */         localObject1 = (boolean[])((ReasonFlags)localObject2).getFlags().clone();
/*     */       }
/* 522 */     } else if ((localIssuingDistributionPointExtension == null) || (localObject2 == null)) {
/* 523 */       if (arrayOfBoolean != null)
/*     */       {
/* 525 */         localObject1 = (boolean[])arrayOfBoolean.clone();
/*     */       }
/*     */       else {
/* 528 */         Arrays.fill((boolean[])localObject1, true);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 534 */     int k = 0;
/* 535 */     for (int m = 0; (m < localObject1.length) && (k == 0); m++) {
/* 536 */       if ((localObject1[m] != 0) && ((m >= paramArrayOfBoolean.length) || (paramArrayOfBoolean[m] == 0)))
/*     */       {
/* 539 */         k = 1;
/*     */       }
/*     */     }
/* 542 */     if (k == 0) {
/* 543 */       return false;
/*     */     }
/*     */ 
/* 549 */     if (i != 0) {
/* 550 */       X509CertSelector localX509CertSelector = new X509CertSelector();
/* 551 */       localX509CertSelector.setSubject(localX500Name2.asX500Principal());
/* 552 */       localObject6 = new boolean[] { false, false, false, false, false, false, true };
/* 553 */       localX509CertSelector.setKeyUsage((boolean[])localObject6);
/*     */ 
/* 565 */       localObject7 = localX509CRLImpl.getAuthKeyIdExtension();
/*     */ 
/* 567 */       if (localObject7 != null) {
/* 568 */         localObject8 = (KeyIdentifier)((AuthorityKeyIdentifierExtension)localObject7).get("key_id");
/* 569 */         if (localObject8 != null) {
/* 570 */           localObject9 = new DerOutputStream();
/* 571 */           ((DerOutputStream)localObject9).putOctetString(((KeyIdentifier)localObject8).getIdentifier());
/* 572 */           localX509CertSelector.setSubjectKeyIdentifier(((DerOutputStream)localObject9).toByteArray());
/*     */         }
/*     */ 
/* 575 */         localObject9 = (SerialNumber)((AuthorityKeyIdentifierExtension)localObject7).get("serial_number");
/*     */ 
/* 577 */         if (localObject9 != null) {
/* 578 */           localX509CertSelector.setSerialNumber(((SerialNumber)localObject9).getNumber());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 588 */       localObject8 = new HashSet(paramSet);
/*     */ 
/* 590 */       if (paramPublicKey != null)
/*     */       {
/* 592 */         localObject9 = paramX509CertImpl.getIssuerX500Principal();
/* 593 */         TrustAnchor localTrustAnchor = new TrustAnchor((X500Principal)localObject9, paramPublicKey, null);
/*     */ 
/* 595 */         ((Set)localObject8).add(localTrustAnchor);
/*     */       }
/*     */ 
/* 598 */       Object localObject9 = null;
/*     */       try {
/* 600 */         localObject9 = new PKIXBuilderParameters((Set)localObject8, localX509CertSelector);
/*     */       } catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException) {
/* 602 */         throw new CRLException(localInvalidAlgorithmParameterException);
/*     */       }
/* 604 */       ((PKIXBuilderParameters)localObject9).setCertStores(paramList);
/* 605 */       ((PKIXBuilderParameters)localObject9).setSigProvider(paramString);
/* 606 */       ((PKIXBuilderParameters)localObject9).setDate(paramDate);
/*     */       try {
/* 608 */         CertPathBuilder localCertPathBuilder = CertPathBuilder.getInstance("PKIX");
/* 609 */         PKIXCertPathBuilderResult localPKIXCertPathBuilderResult = (PKIXCertPathBuilderResult)localCertPathBuilder.build((CertPathParameters)localObject9);
/*     */ 
/* 611 */         paramPublicKey = localPKIXCertPathBuilderResult.getPublicKey();
/*     */       } catch (Exception localException2) {
/* 613 */         throw new CRLException(localException2);
/*     */       }
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 619 */       AlgorithmChecker.check(paramPublicKey, paramX509CRL);
/*     */     } catch (CertPathValidatorException localCertPathValidatorException) {
/* 621 */       if (debug != null) {
/* 622 */         debug.println("CRL signature algorithm check failed: " + localCertPathValidatorException);
/*     */       }
/* 624 */       return false;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 629 */       paramX509CRL.verify(paramPublicKey, paramString);
/*     */     } catch (Exception localException1) {
/* 631 */       if (debug != null) {
/* 632 */         debug.println("CRL signature failed to verify");
/*     */       }
/* 634 */       return false;
/*     */     }
/*     */ 
/* 638 */     Set localSet = paramX509CRL.getCriticalExtensionOIDs();
/*     */ 
/* 640 */     if (localSet != null) {
/* 641 */       localSet.remove(PKIXExtensions.IssuingDistributionPoint_Id.toString());
/*     */ 
/* 643 */       if (!localSet.isEmpty()) {
/* 644 */         if (debug != null) {
/* 645 */           debug.println("Unrecognized critical extension(s) in CRL: " + localSet);
/*     */ 
/* 647 */           localObject6 = localSet.iterator();
/* 648 */           while (((Iterator)localObject6).hasNext())
/* 649 */             debug.println((String)((Iterator)localObject6).next());
/*     */         }
/* 651 */         return false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 656 */     for (int n = 0; n < localObject1.length; n++) {
/* 657 */       paramArrayOfBoolean[n] = ((paramArrayOfBoolean[n] != 0) || ((n < localObject1.length) && (localObject1[n] != 0)) ? 1 : false);
/*     */     }
/*     */ 
/* 661 */     return true;
/*     */   }
/*     */ 
/*     */   private static GeneralNames getFullNames(X500Name paramX500Name, RDN paramRDN)
/*     */     throws IOException
/*     */   {
/* 670 */     ArrayList localArrayList = new ArrayList(paramX500Name.rdns());
/* 671 */     localArrayList.add(paramRDN);
/* 672 */     X500Name localX500Name = new X500Name((RDN[])localArrayList.toArray(new RDN[0]));
/* 673 */     GeneralNames localGeneralNames = new GeneralNames();
/* 674 */     localGeneralNames.add(new GeneralName(localX500Name));
/* 675 */     return localGeneralNames;
/*     */   }
/*     */ 
/*     */   private static boolean issues(X509CertImpl paramX509CertImpl, X509CRLImpl paramX509CRLImpl, String paramString)
/*     */     throws IOException
/*     */   {
/* 687 */     boolean bool = false;
/*     */ 
/* 689 */     AdaptableX509CertSelector localAdaptableX509CertSelector = new AdaptableX509CertSelector();
/*     */ 
/* 693 */     boolean[] arrayOfBoolean = paramX509CertImpl.getKeyUsage();
/* 694 */     if (arrayOfBoolean != null) {
/* 695 */       arrayOfBoolean[6] = true;
/* 696 */       localAdaptableX509CertSelector.setKeyUsage(arrayOfBoolean);
/*     */     }
/*     */ 
/* 700 */     X500Principal localX500Principal = paramX509CRLImpl.getIssuerX500Principal();
/* 701 */     localAdaptableX509CertSelector.setSubject(localX500Principal);
/*     */ 
/* 711 */     AuthorityKeyIdentifierExtension localAuthorityKeyIdentifierExtension = paramX509CRLImpl.getAuthKeyIdExtension();
/* 712 */     if (localAuthorityKeyIdentifierExtension != null) {
/* 713 */       localAdaptableX509CertSelector.parseAuthorityKeyIdentifierExtension(localAuthorityKeyIdentifierExtension);
/*     */     }
/*     */ 
/* 716 */     bool = localAdaptableX509CertSelector.match(paramX509CertImpl);
/*     */ 
/* 719 */     if ((bool) && ((localAuthorityKeyIdentifierExtension == null) || (paramX509CertImpl.getAuthorityKeyIdentifierExtension() == null))) {
/*     */       try
/*     */       {
/* 722 */         paramX509CRLImpl.verify(paramX509CertImpl.getPublicKey(), paramString);
/* 723 */         bool = true;
/*     */       } catch (Exception localException) {
/* 725 */         bool = false;
/*     */       }
/*     */     }
/*     */ 
/* 729 */     return bool;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.DistributionPointFetcher
 * JD-Core Version:    0.6.2
 */