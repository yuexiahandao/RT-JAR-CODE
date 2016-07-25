/*     */ package com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ import com.sun.org.apache.xml.internal.security.keys.content.keyvalues.RSAKeyValue;
/*     */ import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi;
/*     */ import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolver;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import java.security.PublicKey;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.crypto.SecretKey;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public class RSAKeyValueResolver extends KeyResolverSpi
/*     */ {
/*  45 */   static Logger log = Logger.getLogger(RSAKeyValueResolver.class.getName());
/*     */ 
/*     */   public PublicKey engineLookupAndResolvePublicKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*     */   {
/*  55 */     if (log.isLoggable(Level.FINE))
/*  56 */       log.log(Level.FINE, "Can I resolve " + paramElement.getTagName());
/*  57 */     if (paramElement == null) {
/*  58 */       return null;
/*     */     }
/*     */ 
/*  61 */     boolean bool = XMLUtils.elementIsInSignatureSpace(paramElement, "KeyValue");
/*     */ 
/*  63 */     Element localElement = null;
/*  64 */     if (bool) {
/*  65 */       localElement = XMLUtils.selectDsNode(paramElement.getFirstChild(), "RSAKeyValue", 0);
/*     */     }
/*  67 */     else if (XMLUtils.elementIsInSignatureSpace(paramElement, "RSAKeyValue"))
/*     */     {
/*  71 */       localElement = paramElement;
/*     */     }
/*     */ 
/*  75 */     if (localElement == null) {
/*  76 */       return null;
/*     */     }
/*     */     try
/*     */     {
/*  80 */       RSAKeyValue localRSAKeyValue = new RSAKeyValue(localElement, paramString);
/*     */ 
/*  83 */       return localRSAKeyValue.getPublicKey();
/*     */     } catch (XMLSecurityException localXMLSecurityException) {
/*  85 */       log.log(Level.FINE, "XMLSecurityException", localXMLSecurityException);
/*     */     }
/*     */ 
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
/* 100 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.RSAKeyValueResolver
 * JD-Core Version:    0.6.2
 */