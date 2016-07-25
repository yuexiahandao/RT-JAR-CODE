/*     */ package com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
/*     */ import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverException;
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
/*     */ public class X509CertificateResolver extends KeyResolverSpi
/*     */ {
/*  48 */   static Logger log = Logger.getLogger(X509CertificateResolver.class.getName());
/*     */ 
/*     */   public PublicKey engineLookupAndResolvePublicKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*     */     throws KeyResolverException
/*     */   {
/*  66 */     X509Certificate localX509Certificate = engineLookupResolveX509Certificate(paramElement, paramString, paramStorageResolver);
/*     */ 
/*  69 */     if (localX509Certificate != null) {
/*  70 */       return localX509Certificate.getPublicKey();
/*     */     }
/*     */ 
/*  73 */     return null;
/*     */   }
/*     */ 
/*     */   public X509Certificate engineLookupResolveX509Certificate(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*     */     throws KeyResolverException
/*     */   {
/*     */     try
/*     */     {
/*  90 */       Element[] arrayOfElement = XMLUtils.selectDsNodes(paramElement.getFirstChild(), "X509Certificate");
/*     */ 
/*  92 */       if ((arrayOfElement == null) || (arrayOfElement.length == 0)) {
/*  93 */         Element localElement = XMLUtils.selectDsNode(paramElement.getFirstChild(), "X509Data", 0);
/*     */ 
/*  95 */         if (localElement != null) {
/*  96 */           return engineLookupResolveX509Certificate(localElement, paramString, paramStorageResolver);
/*     */         }
/*  98 */         return null;
/*     */       }
/*     */ 
/* 102 */       for (int i = 0; i < arrayOfElement.length; i++) {
/* 103 */         XMLX509Certificate localXMLX509Certificate = new XMLX509Certificate(arrayOfElement[i], paramString);
/* 104 */         X509Certificate localX509Certificate = localXMLX509Certificate.getX509Certificate();
/* 105 */         if (localX509Certificate != null) {
/* 106 */           return localX509Certificate;
/*     */         }
/*     */       }
/* 109 */       return null;
/*     */     } catch (XMLSecurityException localXMLSecurityException) {
/* 111 */       log.log(Level.FINE, "XMLSecurityException", localXMLSecurityException);
/*     */ 
/* 113 */       throw new KeyResolverException("generic.EmptyMessage", localXMLSecurityException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public SecretKey engineLookupAndResolveSecretKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*     */   {
/* 128 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.X509CertificateResolver
 * JD-Core Version:    0.6.2
 */