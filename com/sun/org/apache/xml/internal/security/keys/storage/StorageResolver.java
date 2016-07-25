/*     */ package com.sun.org.apache.xml.internal.security.keys.storage;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.keys.storage.implementations.KeyStoreResolver;
/*     */ import com.sun.org.apache.xml.internal.security.keys.storage.implementations.SingleCertificateResolver;
/*     */ import java.security.KeyStore;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public class StorageResolver
/*     */ {
/*  41 */   static Logger log = Logger.getLogger(StorageResolver.class.getName());
/*     */ 
/*  45 */   List _storageResolvers = null;
/*     */ 
/*  48 */   Iterator _iterator = null;
/*     */ 
/*     */   public StorageResolver()
/*     */   {
/*     */   }
/*     */ 
/*     */   public StorageResolver(StorageResolverSpi paramStorageResolverSpi)
/*     */   {
/*  62 */     add(paramStorageResolverSpi);
/*     */   }
/*     */ 
/*     */   public void add(StorageResolverSpi paramStorageResolverSpi)
/*     */   {
/*  71 */     if (this._storageResolvers == null)
/*  72 */       this._storageResolvers = new ArrayList();
/*  73 */     this._storageResolvers.add(paramStorageResolverSpi);
/*     */ 
/*  75 */     this._iterator = null;
/*     */   }
/*     */ 
/*     */   public StorageResolver(KeyStore paramKeyStore)
/*     */   {
/*  84 */     add(paramKeyStore);
/*     */   }
/*     */ 
/*     */   public void add(KeyStore paramKeyStore)
/*     */   {
/*     */     try
/*     */     {
/*  95 */       add(new KeyStoreResolver(paramKeyStore));
/*     */     } catch (StorageResolverException localStorageResolverException) {
/*  97 */       log.log(Level.SEVERE, "Could not add KeyStore because of: ", localStorageResolverException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public StorageResolver(X509Certificate paramX509Certificate)
/*     */   {
/* 107 */     add(paramX509Certificate);
/*     */   }
/*     */ 
/*     */   public void add(X509Certificate paramX509Certificate)
/*     */   {
/* 116 */     add(new SingleCertificateResolver(paramX509Certificate));
/*     */   }
/*     */ 
/*     */   public Iterator getIterator()
/*     */   {
/* 126 */     if (this._iterator == null) {
/* 127 */       if (this._storageResolvers == null)
/* 128 */         this._storageResolvers = new ArrayList();
/* 129 */       this._iterator = new StorageResolverIterator(this._storageResolvers.iterator());
/*     */     }
/*     */ 
/* 132 */     return this._iterator;
/*     */   }
/*     */ 
/*     */   public boolean hasNext()
/*     */   {
/* 142 */     if (this._iterator == null) {
/* 143 */       if (this._storageResolvers == null)
/* 144 */         this._storageResolvers = new ArrayList();
/* 145 */       this._iterator = new StorageResolverIterator(this._storageResolvers.iterator());
/*     */     }
/*     */ 
/* 148 */     return this._iterator.hasNext();
/*     */   }
/*     */ 
/*     */   public X509Certificate next()
/*     */   {
/* 157 */     return (X509Certificate)this._iterator.next();
/*     */   }
/*     */ 
/*     */   static class StorageResolverIterator
/*     */     implements Iterator
/*     */   {
/* 169 */     Iterator _resolvers = null;
/*     */ 
/*     */     public StorageResolverIterator(Iterator paramIterator)
/*     */     {
/* 177 */       this._resolvers = paramIterator;
/*     */     }
/*     */ 
/*     */     public boolean hasNext()
/*     */     {
/* 182 */       return this._resolvers.hasNext();
/*     */     }
/*     */ 
/*     */     public Object next()
/*     */     {
/* 187 */       return this._resolvers.next();
/*     */     }
/*     */ 
/*     */     public void remove()
/*     */     {
/* 194 */       throw new UnsupportedOperationException("Can't remove keys from KeyStore");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.keys.storage.StorageResolver
 * JD-Core Version:    0.6.2
 */