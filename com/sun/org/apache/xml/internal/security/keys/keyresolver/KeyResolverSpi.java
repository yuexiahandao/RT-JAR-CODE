/*     */ package com.sun.org.apache.xml.internal.security.keys.keyresolver;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolver;
/*     */ import java.security.PublicKey;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.crypto.SecretKey;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public abstract class KeyResolverSpi
/*     */ {
/* 180 */   protected Map _properties = null;
/*     */ 
/* 182 */   protected boolean globalResolver = false;
/*     */ 
/*     */   public boolean engineCanResolve(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*     */   {
/*  57 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public PublicKey engineResolvePublicKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*     */     throws KeyResolverException
/*     */   {
/*  73 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public PublicKey engineLookupAndResolvePublicKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*     */     throws KeyResolverException
/*     */   {
/*  89 */     KeyResolverSpi localKeyResolverSpi = cloneIfNeeded();
/*  90 */     if (!localKeyResolverSpi.engineCanResolve(paramElement, paramString, paramStorageResolver))
/*  91 */       return null;
/*  92 */     return localKeyResolverSpi.engineResolvePublicKey(paramElement, paramString, paramStorageResolver);
/*     */   }
/*     */ 
/*     */   private KeyResolverSpi cloneIfNeeded() throws KeyResolverException {
/*  96 */     KeyResolverSpi localKeyResolverSpi = this;
/*  97 */     if (this.globalResolver) {
/*     */       try {
/*  99 */         localKeyResolverSpi = (KeyResolverSpi)getClass().newInstance();
/*     */       } catch (InstantiationException localInstantiationException) {
/* 101 */         throw new KeyResolverException("", localInstantiationException);
/*     */       } catch (IllegalAccessException localIllegalAccessException) {
/* 103 */         throw new KeyResolverException("", localIllegalAccessException);
/*     */       }
/*     */     }
/* 106 */     return localKeyResolverSpi;
/*     */   }
/*     */ 
/*     */   public X509Certificate engineResolveX509Certificate(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*     */     throws KeyResolverException
/*     */   {
/* 122 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public X509Certificate engineLookupResolveX509Certificate(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*     */     throws KeyResolverException
/*     */   {
/* 138 */     KeyResolverSpi localKeyResolverSpi = cloneIfNeeded();
/* 139 */     if (!localKeyResolverSpi.engineCanResolve(paramElement, paramString, paramStorageResolver))
/* 140 */       return null;
/* 141 */     return localKeyResolverSpi.engineResolveX509Certificate(paramElement, paramString, paramStorageResolver);
/*     */   }
/*     */ 
/*     */   public SecretKey engineResolveSecretKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*     */     throws KeyResolverException
/*     */   {
/* 157 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public SecretKey engineLookupAndResolveSecretKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*     */     throws KeyResolverException
/*     */   {
/* 173 */     KeyResolverSpi localKeyResolverSpi = cloneIfNeeded();
/* 174 */     if (!localKeyResolverSpi.engineCanResolve(paramElement, paramString, paramStorageResolver))
/* 175 */       return null;
/* 176 */     return localKeyResolverSpi.engineResolveSecretKey(paramElement, paramString, paramStorageResolver);
/*     */   }
/*     */ 
/*     */   public void engineSetProperty(String paramString1, String paramString2)
/*     */   {
/* 191 */     if (this._properties == null)
/* 192 */       this._properties = new HashMap();
/* 193 */     this._properties.put(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public String engineGetProperty(String paramString)
/*     */   {
/* 203 */     if (this._properties == null) {
/* 204 */       return null;
/*     */     }
/* 206 */     return (String)this._properties.get(paramString);
/*     */   }
/*     */ 
/*     */   public boolean understandsProperty(String paramString)
/*     */   {
/* 216 */     if (this._properties == null) {
/* 217 */       return false;
/*     */     }
/* 219 */     return this._properties.get(paramString) != null;
/*     */   }
/*     */   public void setGlobalResolver(boolean paramBoolean) {
/* 222 */     this.globalResolver = paramBoolean;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi
 * JD-Core Version:    0.6.2
 */