/*     */ package com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509SubjectName;
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
/*     */ public class X509SubjectNameResolver extends KeyResolverSpi
/*     */ {
/*  46 */   static Logger log = Logger.getLogger(X509SubjectNameResolver.class.getName());
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
/*  86 */     if (log.isLoggable(Level.FINE))
/*  87 */       log.log(Level.FINE, "Can I resolve " + paramElement.getTagName() + "?");
/*  88 */     Element[] arrayOfElement = null;
/*  89 */     XMLX509SubjectName[] arrayOfXMLX509SubjectName = null;
/*     */ 
/*  91 */     if (!XMLUtils.elementIsInSignatureSpace(paramElement, "X509Data"))
/*     */     {
/*  93 */       log.log(Level.FINE, "I can't");
/*  94 */       return null;
/*     */     }
/*  96 */     arrayOfElement = XMLUtils.selectDsNodes(paramElement.getFirstChild(), "X509SubjectName");
/*     */ 
/*  99 */     if ((arrayOfElement == null) || (arrayOfElement.length <= 0))
/*     */     {
/* 101 */       log.log(Level.FINE, "I can't");
/* 102 */       return null;
/*     */     }
/*     */     try
/*     */     {
/*     */       Object localObject;
/* 106 */       if (paramStorageResolver == null) {
/* 107 */         Object[] arrayOfObject = { "X509SubjectName" };
/* 108 */         localObject = new KeyResolverException("KeyResolver.needStorageResolver", arrayOfObject);
/*     */ 
/* 112 */         log.log(Level.INFO, "", (Throwable)localObject);
/*     */ 
/* 114 */         throw ((Throwable)localObject);
/*     */       }
/*     */ 
/* 117 */       arrayOfXMLX509SubjectName = new XMLX509SubjectName[arrayOfElement.length];
/*     */ 
/* 120 */       for (int i = 0; i < arrayOfElement.length; i++) {
/* 121 */         arrayOfXMLX509SubjectName[i] = new XMLX509SubjectName(arrayOfElement[i], paramString);
/*     */       }
/*     */ 
/* 126 */       while (paramStorageResolver.hasNext()) {
/* 127 */         X509Certificate localX509Certificate = paramStorageResolver.next();
/* 128 */         localObject = new XMLX509SubjectName(paramElement.getOwnerDocument(), localX509Certificate);
/*     */ 
/* 131 */         log.log(Level.FINE, "Found Certificate SN: " + ((XMLX509SubjectName)localObject).getSubjectName());
/*     */ 
/* 133 */         for (int j = 0; j < arrayOfXMLX509SubjectName.length; j++) {
/* 134 */           log.log(Level.FINE, "Found Element SN:     " + arrayOfXMLX509SubjectName[j].getSubjectName());
/*     */ 
/* 137 */           if (((XMLX509SubjectName)localObject).equals(arrayOfXMLX509SubjectName[j])) {
/* 138 */             log.log(Level.FINE, "match !!! ");
/*     */ 
/* 140 */             return localX509Certificate;
/*     */           }
/* 142 */           log.log(Level.FINE, "no match...");
/*     */         }
/*     */       }
/*     */ 
/* 146 */       return null;
/*     */     } catch (XMLSecurityException localXMLSecurityException) {
/* 148 */       log.log(Level.FINE, "XMLSecurityException", localXMLSecurityException);
/*     */ 
/* 150 */       throw new KeyResolverException("generic.EmptyMessage", localXMLSecurityException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public SecretKey engineLookupAndResolveSecretKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*     */   {
/* 165 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.X509SubjectNameResolver
 * JD-Core Version:    0.6.2
 */