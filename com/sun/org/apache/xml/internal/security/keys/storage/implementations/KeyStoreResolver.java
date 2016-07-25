/*     */ package com.sun.org.apache.xml.internal.security.keys.storage.implementations;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509SKI;
/*     */ import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolverException;
/*     */ import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolverSpi;
/*     */ import com.sun.org.apache.xml.internal.security.utils.Base64;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class KeyStoreResolver extends StorageResolverSpi
/*     */ {
/*  42 */   KeyStore _keyStore = null;
/*     */ 
/*  45 */   Iterator _iterator = null;
/*     */ 
/*     */   public KeyStoreResolver(KeyStore paramKeyStore)
/*     */     throws StorageResolverException
/*     */   {
/*  54 */     this._keyStore = paramKeyStore;
/*  55 */     this._iterator = new KeyStoreIterator(this._keyStore);
/*     */   }
/*     */ 
/*     */   public Iterator getIterator()
/*     */   {
/*  60 */     return this._iterator;
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */     throws Exception
/*     */   {
/* 129 */     KeyStore localKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
/*     */ 
/* 131 */     localKeyStore.load(new FileInputStream("data/com/sun/org/apache/xml/internal/security/samples/input/keystore.jks"), "xmlsecurity".toCharArray());
/*     */ 
/* 136 */     KeyStoreResolver localKeyStoreResolver = new KeyStoreResolver(localKeyStore);
/*     */ 
/* 138 */     for (Iterator localIterator = localKeyStoreResolver.getIterator(); localIterator.hasNext(); ) {
/* 139 */       X509Certificate localX509Certificate = (X509Certificate)localIterator.next();
/* 140 */       byte[] arrayOfByte = XMLX509SKI.getSKIBytesFromCert(localX509Certificate);
/*     */ 
/* 144 */       System.out.println(Base64.encode(arrayOfByte));
/*     */     }
/*     */   }
/*     */ 
/*     */   static class KeyStoreIterator
/*     */     implements Iterator
/*     */   {
/*  72 */     KeyStore _keyStore = null;
/*     */ 
/*  75 */     Enumeration _aliases = null;
/*     */ 
/*     */     public KeyStoreIterator(KeyStore paramKeyStore)
/*     */       throws StorageResolverException
/*     */     {
/*     */       try
/*     */       {
/*  87 */         this._keyStore = paramKeyStore;
/*  88 */         this._aliases = this._keyStore.aliases();
/*     */       } catch (KeyStoreException localKeyStoreException) {
/*  90 */         throw new StorageResolverException("generic.EmptyMessage", localKeyStoreException);
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean hasNext()
/*     */     {
/*  96 */       return this._aliases.hasMoreElements();
/*     */     }
/*     */ 
/*     */     public Object next()
/*     */     {
/* 102 */       String str = (String)this._aliases.nextElement();
/*     */       try
/*     */       {
/* 105 */         return this._keyStore.getCertificate(str); } catch (KeyStoreException localKeyStoreException) {
/*     */       }
/* 107 */       return null;
/*     */     }
/*     */ 
/*     */     public void remove()
/*     */     {
/* 116 */       throw new UnsupportedOperationException("Can't remove keys from KeyStore");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.keys.storage.implementations.KeyStoreResolver
 * JD-Core Version:    0.6.2
 */