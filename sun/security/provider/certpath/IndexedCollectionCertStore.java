/*     */ package sun.security.provider.certpath;
/*     */ 
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.cert.CRL;
/*     */ import java.security.cert.CRLSelector;
/*     */ import java.security.cert.CertSelector;
/*     */ import java.security.cert.CertStoreException;
/*     */ import java.security.cert.CertStoreParameters;
/*     */ import java.security.cert.CertStoreSpi;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CollectionCertStoreParameters;
/*     */ import java.security.cert.X509CRL;
/*     */ import java.security.cert.X509CRLSelector;
/*     */ import java.security.cert.X509CertSelector;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ 
/*     */ public class IndexedCollectionCertStore extends CertStoreSpi
/*     */ {
/*     */   private Map<X500Principal, Object> certSubjects;
/*     */   private Map<X500Principal, Object> crlIssuers;
/*     */   private Set<Certificate> otherCertificates;
/*     */   private Set<CRL> otherCRLs;
/*     */ 
/*     */   public IndexedCollectionCertStore(CertStoreParameters paramCertStoreParameters)
/*     */     throws InvalidAlgorithmParameterException
/*     */   {
/* 123 */     super(paramCertStoreParameters);
/* 124 */     if (!(paramCertStoreParameters instanceof CollectionCertStoreParameters)) {
/* 125 */       throw new InvalidAlgorithmParameterException("parameters must be CollectionCertStoreParameters");
/*     */     }
/*     */ 
/* 128 */     Collection localCollection = ((CollectionCertStoreParameters)paramCertStoreParameters).getCollection();
/* 129 */     if (localCollection == null) {
/* 130 */       throw new InvalidAlgorithmParameterException("Collection must not be null");
/*     */     }
/*     */ 
/* 133 */     buildIndex(localCollection);
/*     */   }
/*     */ 
/*     */   private void buildIndex(Collection<?> paramCollection)
/*     */   {
/* 141 */     this.certSubjects = new HashMap();
/* 142 */     this.crlIssuers = new HashMap();
/* 143 */     this.otherCertificates = null;
/* 144 */     this.otherCRLs = null;
/* 145 */     for (Iterator localIterator = paramCollection.iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 146 */       if ((localObject instanceof X509Certificate)) {
/* 147 */         indexCertificate((X509Certificate)localObject);
/* 148 */       } else if ((localObject instanceof X509CRL)) {
/* 149 */         indexCRL((X509CRL)localObject);
/* 150 */       } else if ((localObject instanceof Certificate)) {
/* 151 */         if (this.otherCertificates == null) {
/* 152 */           this.otherCertificates = new HashSet();
/*     */         }
/* 154 */         this.otherCertificates.add((Certificate)localObject);
/* 155 */       } else if ((localObject instanceof CRL)) {
/* 156 */         if (this.otherCRLs == null) {
/* 157 */           this.otherCRLs = new HashSet();
/*     */         }
/* 159 */         this.otherCRLs.add((CRL)localObject);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 164 */     if (this.otherCertificates == null) {
/* 165 */       this.otherCertificates = Collections.emptySet();
/*     */     }
/* 167 */     if (this.otherCRLs == null)
/* 168 */       this.otherCRLs = Collections.emptySet();
/*     */   }
/*     */ 
/*     */   private void indexCertificate(X509Certificate paramX509Certificate)
/*     */   {
/* 176 */     X500Principal localX500Principal = paramX509Certificate.getSubjectX500Principal();
/* 177 */     Object localObject1 = this.certSubjects.put(localX500Principal, paramX509Certificate);
/* 178 */     if (localObject1 != null)
/*     */     {
/*     */       Object localObject2;
/* 179 */       if ((localObject1 instanceof X509Certificate)) {
/* 180 */         if (paramX509Certificate.equals(localObject1)) {
/* 181 */           return;
/*     */         }
/* 183 */         localObject2 = new ArrayList(2);
/* 184 */         ((List)localObject2).add(paramX509Certificate);
/* 185 */         ((List)localObject2).add((X509Certificate)localObject1);
/* 186 */         this.certSubjects.put(localX500Principal, localObject2);
/*     */       } else {
/* 188 */         localObject2 = (List)localObject1;
/* 189 */         if (!((List)localObject2).contains(paramX509Certificate)) {
/* 190 */           ((List)localObject2).add(paramX509Certificate);
/*     */         }
/* 192 */         this.certSubjects.put(localX500Principal, localObject2);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void indexCRL(X509CRL paramX509CRL)
/*     */   {
/* 201 */     X500Principal localX500Principal = paramX509CRL.getIssuerX500Principal();
/* 202 */     Object localObject1 = this.crlIssuers.put(localX500Principal, paramX509CRL);
/* 203 */     if (localObject1 != null)
/*     */     {
/*     */       Object localObject2;
/* 204 */       if ((localObject1 instanceof X509CRL)) {
/* 205 */         if (paramX509CRL.equals(localObject1)) {
/* 206 */           return;
/*     */         }
/* 208 */         localObject2 = new ArrayList(2);
/* 209 */         ((List)localObject2).add(paramX509CRL);
/* 210 */         ((List)localObject2).add((X509CRL)localObject1);
/* 211 */         this.crlIssuers.put(localX500Principal, localObject2);
/*     */       } else {
/* 213 */         localObject2 = (List)localObject1;
/* 214 */         if (!((List)localObject2).contains(paramX509CRL)) {
/* 215 */           ((List)localObject2).add(paramX509CRL);
/*     */         }
/* 217 */         this.crlIssuers.put(localX500Principal, localObject2);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Collection<? extends Certificate> engineGetCertificates(CertSelector paramCertSelector)
/*     */     throws CertStoreException
/*     */   {
/* 238 */     if (paramCertSelector == null) {
/* 239 */       localObject1 = new HashSet();
/* 240 */       matchX509Certs(new X509CertSelector(), (Collection)localObject1);
/* 241 */       ((Set)localObject1).addAll(this.otherCertificates);
/* 242 */       return localObject1;
/*     */     }
/*     */     Object localObject2;
/* 245 */     if (!(paramCertSelector instanceof X509CertSelector)) {
/* 246 */       localObject1 = new HashSet();
/* 247 */       matchX509Certs(paramCertSelector, (Collection)localObject1);
/* 248 */       for (localObject2 = this.otherCertificates.iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (Certificate)((Iterator)localObject2).next();
/* 249 */         if (paramCertSelector.match((Certificate)localObject3)) {
/* 250 */           ((Set)localObject1).add(localObject3);
/*     */         }
/*     */       }
/* 253 */       return localObject1;
/*     */     }
/*     */ 
/* 256 */     if (this.certSubjects.isEmpty()) {
/* 257 */       return Collections.emptySet();
/*     */     }
/* 259 */     Object localObject1 = (X509CertSelector)paramCertSelector;
/*     */ 
/* 262 */     Object localObject3 = ((X509CertSelector)localObject1).getCertificate();
/* 263 */     if (localObject3 != null)
/* 264 */       localObject2 = ((X509Certificate)localObject3).getSubjectX500Principal();
/*     */     else {
/* 266 */       localObject2 = ((X509CertSelector)localObject1).getSubject();
/*     */     }
/* 268 */     if (localObject2 != null)
/*     */     {
/* 270 */       localObject4 = this.certSubjects.get(localObject2);
/* 271 */       if (localObject4 == null) {
/* 272 */         return Collections.emptySet();
/*     */       }
/* 274 */       if ((localObject4 instanceof X509Certificate)) {
/* 275 */         localObject5 = (X509Certificate)localObject4;
/* 276 */         if (((X509CertSelector)localObject1).match((Certificate)localObject5)) {
/* 277 */           return Collections.singleton(localObject5);
/*     */         }
/* 279 */         return Collections.emptySet();
/*     */       }
/*     */ 
/* 282 */       Object localObject5 = (List)localObject4;
/* 283 */       HashSet localHashSet = new HashSet(16);
/* 284 */       for (X509Certificate localX509Certificate : (List)localObject5) {
/* 285 */         if (((X509CertSelector)localObject1).match(localX509Certificate)) {
/* 286 */           localHashSet.add(localX509Certificate);
/*     */         }
/*     */       }
/* 289 */       return localHashSet;
/*     */     }
/*     */ 
/* 293 */     Object localObject4 = new HashSet(16);
/* 294 */     matchX509Certs((CertSelector)localObject1, (Collection)localObject4);
/* 295 */     return localObject4;
/*     */   }
/*     */ 
/*     */   private void matchX509Certs(CertSelector paramCertSelector, Collection<Certificate> paramCollection)
/*     */   {
/* 305 */     for (Iterator localIterator1 = this.certSubjects.values().iterator(); localIterator1.hasNext(); ) { Object localObject1 = localIterator1.next();
/*     */       Object localObject2;
/* 306 */       if ((localObject1 instanceof X509Certificate)) {
/* 307 */         localObject2 = (X509Certificate)localObject1;
/* 308 */         if (paramCertSelector.match((Certificate)localObject2))
/* 309 */           paramCollection.add(localObject2);
/*     */       }
/*     */       else {
/* 312 */         localObject2 = (List)localObject1;
/* 313 */         for (X509Certificate localX509Certificate : (List)localObject2)
/* 314 */           if (paramCertSelector.match(localX509Certificate))
/* 315 */             paramCollection.add(localX509Certificate);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Collection<CRL> engineGetCRLs(CRLSelector paramCRLSelector)
/*     */     throws CertStoreException
/*     */   {
/* 337 */     if (paramCRLSelector == null) {
/* 338 */       localObject1 = new HashSet();
/* 339 */       matchX509CRLs(new X509CRLSelector(), (Collection)localObject1);
/* 340 */       ((Set)localObject1).addAll(this.otherCRLs);
/* 341 */       return localObject1;
/*     */     }
/*     */ 
/* 344 */     if (!(paramCRLSelector instanceof X509CRLSelector)) {
/* 345 */       localObject1 = new HashSet();
/* 346 */       matchX509CRLs(paramCRLSelector, (Collection)localObject1);
/* 347 */       for (localObject2 = this.otherCRLs.iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (CRL)((Iterator)localObject2).next();
/* 348 */         if (paramCRLSelector.match((CRL)localObject3)) {
/* 349 */           ((Set)localObject1).add(localObject3);
/*     */         }
/*     */       }
/* 352 */       return localObject1;
/*     */     }
/*     */ 
/* 355 */     if (this.crlIssuers.isEmpty()) {
/* 356 */       return Collections.emptySet();
/*     */     }
/* 358 */     Object localObject1 = (X509CRLSelector)paramCRLSelector;
/*     */ 
/* 360 */     Object localObject2 = ((X509CRLSelector)localObject1).getIssuers();
/* 361 */     if (localObject2 != null) {
/* 362 */       localObject3 = new HashSet(16);
/* 363 */       for (X500Principal localX500Principal : (Collection)localObject2) {
/* 364 */         Object localObject4 = this.crlIssuers.get(localX500Principal);
/* 365 */         if (localObject4 != null)
/*     */         {
/*     */           Object localObject5;
/* 367 */           if ((localObject4 instanceof X509CRL)) {
/* 368 */             localObject5 = (X509CRL)localObject4;
/* 369 */             if (((X509CRLSelector)localObject1).match((CRL)localObject5))
/* 370 */               ((HashSet)localObject3).add(localObject5);
/*     */           }
/*     */           else {
/* 373 */             localObject5 = (List)localObject4;
/* 374 */             for (X509CRL localX509CRL : (List)localObject5) {
/* 375 */               if (((X509CRLSelector)localObject1).match(localX509CRL))
/* 376 */                 ((HashSet)localObject3).add(localX509CRL);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 381 */       return localObject3;
/*     */     }
/*     */ 
/* 384 */     Object localObject3 = new HashSet(16);
/* 385 */     matchX509CRLs((CRLSelector)localObject1, (Collection)localObject3);
/* 386 */     return localObject3;
/*     */   }
/*     */ 
/*     */   private void matchX509CRLs(CRLSelector paramCRLSelector, Collection<CRL> paramCollection)
/*     */   {
/* 394 */     for (Iterator localIterator1 = this.crlIssuers.values().iterator(); localIterator1.hasNext(); ) { Object localObject1 = localIterator1.next();
/*     */       Object localObject2;
/* 395 */       if ((localObject1 instanceof X509CRL)) {
/* 396 */         localObject2 = (X509CRL)localObject1;
/* 397 */         if (paramCRLSelector.match((CRL)localObject2))
/* 398 */           paramCollection.add(localObject2);
/*     */       }
/*     */       else {
/* 401 */         localObject2 = (List)localObject1;
/* 402 */         for (X509CRL localX509CRL : (List)localObject2)
/* 403 */           if (paramCRLSelector.match(localX509CRL))
/* 404 */             paramCollection.add(localX509CRL);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.IndexedCollectionCertStore
 * JD-Core Version:    0.6.2
 */