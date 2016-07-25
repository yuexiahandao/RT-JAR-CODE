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
/*     */ import java.util.Collection;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class CollectionCertStore extends CertStoreSpi
/*     */ {
/*     */   private Collection<?> coll;
/*     */ 
/*     */   public CollectionCertStore(CertStoreParameters paramCertStoreParameters)
/*     */     throws InvalidAlgorithmParameterException
/*     */   {
/*  98 */     super(paramCertStoreParameters);
/*  99 */     if (!(paramCertStoreParameters instanceof CollectionCertStoreParameters)) {
/* 100 */       throw new InvalidAlgorithmParameterException("parameters must be CollectionCertStoreParameters");
/*     */     }
/* 102 */     this.coll = ((CollectionCertStoreParameters)paramCertStoreParameters).getCollection();
/*     */   }
/*     */ 
/*     */   public Collection<Certificate> engineGetCertificates(CertSelector paramCertSelector)
/*     */     throws CertStoreException
/*     */   {
/* 119 */     if (this.coll == null) {
/* 120 */       throw new CertStoreException("Collection is null");
/*     */     }
/*     */ 
/* 123 */     for (int i = 0; i < 10; i++)
/*     */       try {
/* 125 */         HashSet localHashSet = new HashSet();
/* 126 */         Iterator localIterator = this.coll.iterator();
/*     */         Object localObject;
/* 127 */         if (paramCertSelector != null)
/* 128 */           while (localIterator.hasNext()) {
/* 129 */             localObject = localIterator.next();
/* 130 */             if (((localObject instanceof Certificate)) && (paramCertSelector.match((Certificate)localObject)))
/*     */             {
/* 132 */               localHashSet.add((Certificate)localObject);
/*     */             }
/*     */           }
/* 135 */         while (localIterator.hasNext()) {
/* 136 */           localObject = localIterator.next();
/* 137 */           if ((localObject instanceof Certificate)) {
/* 138 */             localHashSet.add((Certificate)localObject);
/*     */           }
/*     */         }
/* 141 */         return localHashSet;
/*     */       } catch (ConcurrentModificationException localConcurrentModificationException) {
/*     */       }
/* 144 */     throw new ConcurrentModificationException("Too many ConcurrentModificationExceptions");
/*     */   }
/*     */ 
/*     */   public Collection<CRL> engineGetCRLs(CRLSelector paramCRLSelector)
/*     */     throws CertStoreException
/*     */   {
/* 163 */     if (this.coll == null) {
/* 164 */       throw new CertStoreException("Collection is null");
/*     */     }
/*     */ 
/* 167 */     for (int i = 0; i < 10; i++)
/*     */       try {
/* 169 */         HashSet localHashSet = new HashSet();
/* 170 */         Iterator localIterator = this.coll.iterator();
/*     */         Object localObject;
/* 171 */         if (paramCRLSelector != null) {
/* 172 */           while (localIterator.hasNext()) {
/* 173 */             localObject = localIterator.next();
/* 174 */             if (((localObject instanceof CRL)) && (paramCRLSelector.match((CRL)localObject)))
/* 175 */               localHashSet.add((CRL)localObject);
/*     */           }
/*     */         }
/* 178 */         while (localIterator.hasNext()) {
/* 179 */           localObject = localIterator.next();
/* 180 */           if ((localObject instanceof CRL)) {
/* 181 */             localHashSet.add((CRL)localObject);
/*     */           }
/*     */         }
/* 184 */         return localHashSet;
/*     */       } catch (ConcurrentModificationException localConcurrentModificationException) {
/*     */       }
/* 187 */     throw new ConcurrentModificationException("Too many ConcurrentModificationExceptions");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.CollectionCertStore
 * JD-Core Version:    0.6.2
 */