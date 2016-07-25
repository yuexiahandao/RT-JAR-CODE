/*     */ package com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.encryption.EncryptedKey;
/*     */ import com.sun.org.apache.xml.internal.security.encryption.XMLCipher;
/*     */ import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi;
/*     */ import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolver;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import java.security.Key;
/*     */ import java.security.PublicKey;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.crypto.SecretKey;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public class EncryptedKeyResolver extends KeyResolverSpi
/*     */ {
/*  54 */   static Logger log = Logger.getLogger(RSAKeyValueResolver.class.getName());
/*     */   Key _kek;
/*     */   String _algorithm;
/*     */ 
/*     */   public EncryptedKeyResolver(String paramString)
/*     */   {
/*  68 */     this._kek = null;
/*  69 */     this._algorithm = paramString;
/*     */   }
/*     */ 
/*     */   public EncryptedKeyResolver(String paramString, Key paramKey)
/*     */   {
/*  79 */     this._algorithm = paramString;
/*  80 */     this._kek = paramKey;
/*     */   }
/*     */ 
/*     */   public PublicKey engineLookupAndResolvePublicKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*     */   {
/*  88 */     return null;
/*     */   }
/*     */ 
/*     */   public X509Certificate engineLookupResolveX509Certificate(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*     */   {
/*  94 */     return null;
/*     */   }
/*     */ 
/*     */   public SecretKey engineLookupAndResolveSecretKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*     */   {
/* 100 */     SecretKey localSecretKey = null;
/* 101 */     if (log.isLoggable(Level.FINE)) {
/* 102 */       log.log(Level.FINE, "EncryptedKeyResolver - Can I resolve " + paramElement.getTagName());
/*     */     }
/* 104 */     if (paramElement == null) {
/* 105 */       return null;
/*     */     }
/*     */ 
/* 108 */     boolean bool = XMLUtils.elementIsInEncryptionSpace(paramElement, "EncryptedKey");
/*     */ 
/* 111 */     if (bool) {
/* 112 */       log.log(Level.FINE, "Passed an Encrypted Key");
/*     */       try {
/* 114 */         XMLCipher localXMLCipher = XMLCipher.getInstance();
/* 115 */         localXMLCipher.init(4, this._kek);
/* 116 */         EncryptedKey localEncryptedKey = localXMLCipher.loadEncryptedKey(paramElement);
/* 117 */         localSecretKey = (SecretKey)localXMLCipher.decryptKey(localEncryptedKey, this._algorithm);
/*     */       }
/*     */       catch (Exception localException) {
/*     */       }
/*     */     }
/* 122 */     return localSecretKey;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.EncryptedKeyResolver
 * JD-Core Version:    0.6.2
 */