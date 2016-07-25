/*     */ package com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ import com.sun.org.apache.xml.internal.security.keys.content.X509Data;
/*     */ import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509IssuerSerial;
/*     */ import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverException;
/*     */ import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi;
/*     */ import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolver;
/*     */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureException;
/*     */ import java.math.BigInteger;
/*     */ import java.security.PublicKey;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.crypto.SecretKey;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public class X509IssuerSerialResolver extends KeyResolverSpi
/*     */ {
/*  46 */   static Logger log = Logger.getLogger(X509IssuerSerialResolver.class.getName());
/*     */ 
/*     */   public PublicKey engineLookupAndResolvePublicKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*     */     throws KeyResolverException
/*     */   {
/*  56 */     X509Certificate localX509Certificate = engineLookupResolveX509Certificate(paramElement, paramString, paramStorageResolver);
/*     */ 
/*  59 */     if (localX509Certificate != null) {
/*  60 */       return localX509Certificate.getPublicKey();
/*     */     }
/*     */ 
/*  63 */     return null;
/*     */   }
/*     */ 
/*     */   public X509Certificate engineLookupResolveX509Certificate(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*     */     throws KeyResolverException
/*     */   {
/*  70 */     if (log.isLoggable(Level.FINE)) {
/*  71 */       log.log(Level.FINE, "Can I resolve " + paramElement.getTagName() + "?");
/*     */     }
/*  73 */     X509Data localX509Data = null;
/*     */     try {
/*  75 */       localX509Data = new X509Data(paramElement, paramString);
/*     */     } catch (XMLSignatureException localXMLSignatureException) {
/*  77 */       log.log(Level.FINE, "I can't");
/*  78 */       return null;
/*     */     } catch (XMLSecurityException localXMLSecurityException1) {
/*  80 */       log.log(Level.FINE, "I can't");
/*  81 */       return null;
/*     */     }
/*     */ 
/*  84 */     if (localX509Data == null) {
/*  85 */       log.log(Level.FINE, "I can't");
/*  86 */       return null;
/*     */     }
/*     */ 
/*  89 */     if (!localX509Data.containsIssuerSerial())
/*  90 */       return null;
/*     */     try
/*     */     {
/*     */       Object localObject;
/*  93 */       if (paramStorageResolver == null) {
/*  94 */         Object[] arrayOfObject = { "X509IssuerSerial" };
/*  95 */         localObject = new KeyResolverException("KeyResolver.needStorageResolver", arrayOfObject);
/*     */ 
/*  99 */         log.log(Level.INFO, "", (Throwable)localObject);
/* 100 */         throw ((Throwable)localObject);
/*     */       }
/*     */ 
/* 103 */       int i = localX509Data.lengthIssuerSerial();
/*     */ 
/* 105 */       while (paramStorageResolver.hasNext()) {
/* 106 */         localObject = paramStorageResolver.next();
/* 107 */         XMLX509IssuerSerial localXMLX509IssuerSerial1 = new XMLX509IssuerSerial(paramElement.getOwnerDocument(), (X509Certificate)localObject);
/*     */ 
/* 109 */         if (log.isLoggable(Level.FINE)) {
/* 110 */           log.log(Level.FINE, "Found Certificate Issuer: " + localXMLX509IssuerSerial1.getIssuerName());
/*     */ 
/* 112 */           log.log(Level.FINE, "Found Certificate Serial: " + localXMLX509IssuerSerial1.getSerialNumber().toString());
/*     */         }
/*     */ 
/* 116 */         for (int j = 0; j < i; j++) {
/* 117 */           XMLX509IssuerSerial localXMLX509IssuerSerial2 = localX509Data.itemIssuerSerial(j);
/*     */ 
/* 119 */           if (log.isLoggable(Level.FINE)) {
/* 120 */             log.log(Level.FINE, "Found Element Issuer:     " + localXMLX509IssuerSerial2.getIssuerName());
/*     */ 
/* 122 */             log.log(Level.FINE, "Found Element Serial:     " + localXMLX509IssuerSerial2.getSerialNumber().toString());
/*     */           }
/*     */ 
/* 126 */           if (localXMLX509IssuerSerial1.equals(localXMLX509IssuerSerial2)) {
/* 127 */             log.log(Level.FINE, "match !!! ");
/*     */ 
/* 129 */             return localObject;
/*     */           }
/* 131 */           log.log(Level.FINE, "no match...");
/*     */         }
/*     */       }
/*     */ 
/* 135 */       return null;
/*     */     } catch (XMLSecurityException localXMLSecurityException2) {
/* 137 */       log.log(Level.FINE, "XMLSecurityException", localXMLSecurityException2);
/*     */ 
/* 139 */       throw new KeyResolverException("generic.EmptyMessage", localXMLSecurityException2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public SecretKey engineLookupAndResolveSecretKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*     */   {
/* 146 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.X509IssuerSerialResolver
 * JD-Core Version:    0.6.2
 */