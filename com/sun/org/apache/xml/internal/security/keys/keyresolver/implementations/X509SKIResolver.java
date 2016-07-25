/*     */ package com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509SKI;
/*     */ import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverException;
/*     */ import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi;
/*     */ import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolver;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import java.security.Principal;
/*     */ import java.security.PublicKey;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.crypto.SecretKey;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public class X509SKIResolver extends KeyResolverSpi
/*     */ {
/*  47 */   static Logger log = Logger.getLogger(X509SKIResolver.class.getName());
/*     */ 
/*     */   public PublicKey engineLookupAndResolvePublicKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*     */     throws KeyResolverException
/*     */   {
/*  64 */     X509Certificate localX509Certificate = engineLookupResolveX509Certificate(paramElement, paramString, paramStorageResolver);
/*     */ 
/*  67 */     if (localX509Certificate != null) {
/*  68 */       return localX509Certificate.getPublicKey();
/*     */     }
/*     */ 
/*  71 */     return null;
/*     */   }
/*     */ 
/*     */   public X509Certificate engineLookupResolveX509Certificate(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*     */     throws KeyResolverException
/*     */   {
/*  86 */     if (log.isLoggable(Level.FINE)) {
/*  87 */       log.log(Level.FINE, "Can I resolve " + paramElement.getTagName() + "?");
/*     */     }
/*  89 */     if (!XMLUtils.elementIsInSignatureSpace(paramElement, "X509Data"))
/*     */     {
/*  91 */       log.log(Level.FINE, "I can't");
/*  92 */       return null;
/*     */     }
/*     */ 
/*  95 */     XMLX509SKI[] arrayOfXMLX509SKI = null;
/*     */ 
/*  97 */     Element[] arrayOfElement = null;
/*  98 */     arrayOfElement = XMLUtils.selectDsNodes(paramElement.getFirstChild(), "X509SKI");
/*     */ 
/* 101 */     if ((arrayOfElement == null) || (arrayOfElement.length <= 0))
/*     */     {
/* 103 */       log.log(Level.FINE, "I can't");
/* 104 */       return null;
/*     */     }
/*     */     try
/*     */     {
/*     */       Object localObject;
/* 107 */       if (paramStorageResolver == null) {
/* 108 */         Object[] arrayOfObject = { "X509SKI" };
/* 109 */         localObject = new KeyResolverException("KeyResolver.needStorageResolver", arrayOfObject);
/*     */ 
/* 113 */         log.log(Level.INFO, "", (Throwable)localObject);
/*     */ 
/* 115 */         throw ((Throwable)localObject);
/*     */       }
/*     */ 
/* 118 */       arrayOfXMLX509SKI = new XMLX509SKI[arrayOfElement.length];
/*     */ 
/* 120 */       for (int i = 0; i < arrayOfElement.length; i++) {
/* 121 */         arrayOfXMLX509SKI[i] = new XMLX509SKI(arrayOfElement[i], paramString);
/*     */       }
/*     */ 
/* 125 */       while (paramStorageResolver.hasNext()) {
/* 126 */         X509Certificate localX509Certificate = paramStorageResolver.next();
/* 127 */         localObject = new XMLX509SKI(paramElement.getOwnerDocument(), localX509Certificate);
/*     */ 
/* 129 */         for (int j = 0; j < arrayOfXMLX509SKI.length; j++)
/* 130 */           if (((XMLX509SKI)localObject).equals(arrayOfXMLX509SKI[j])) {
/* 131 */             log.log(Level.FINE, "Return PublicKey from " + localX509Certificate.getSubjectDN().getName());
/*     */ 
/* 134 */             return localX509Certificate;
/*     */           }
/*     */       }
/*     */     }
/*     */     catch (XMLSecurityException localXMLSecurityException) {
/* 139 */       throw new KeyResolverException("empty", localXMLSecurityException);
/*     */     }
/*     */ 
/* 142 */     return null;
/*     */   }
/*     */ 
/*     */   public SecretKey engineLookupAndResolveSecretKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*     */   {
/* 156 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.X509SKIResolver
 * JD-Core Version:    0.6.2
 */