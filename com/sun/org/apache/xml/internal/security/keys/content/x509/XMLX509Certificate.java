/*     */ package com.sun.org.apache.xml.internal.security.keys.content.x509;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.PublicKey;
/*     */ import java.security.cert.CertificateEncodingException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.X509Certificate;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public class XMLX509Certificate extends SignatureElementProxy
/*     */   implements XMLX509DataContent
/*     */ {
/*     */   public static final String JCA_CERT_ID = "X.509";
/*     */ 
/*     */   public XMLX509Certificate(Element paramElement, String paramString)
/*     */     throws XMLSecurityException
/*     */   {
/*  54 */     super(paramElement, paramString);
/*     */   }
/*     */ 
/*     */   public XMLX509Certificate(Document paramDocument, byte[] paramArrayOfByte)
/*     */   {
/*  65 */     super(paramDocument);
/*     */ 
/*  67 */     addBase64Text(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public XMLX509Certificate(Document paramDocument, X509Certificate paramX509Certificate)
/*     */     throws XMLSecurityException
/*     */   {
/*  80 */     super(paramDocument);
/*     */     try
/*     */     {
/*  83 */       addBase64Text(paramX509Certificate.getEncoded());
/*     */     } catch (CertificateEncodingException localCertificateEncodingException) {
/*  85 */       throw new XMLSecurityException("empty", localCertificateEncodingException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] getCertificateBytes()
/*     */     throws XMLSecurityException
/*     */   {
/*  96 */     return getBytesFromTextChild();
/*     */   }
/*     */ 
/*     */   public X509Certificate getX509Certificate()
/*     */     throws XMLSecurityException
/*     */   {
/*     */     try
/*     */     {
/* 108 */       byte[] arrayOfByte = getCertificateBytes();
/* 109 */       CertificateFactory localCertificateFactory = CertificateFactory.getInstance("X.509");
/*     */ 
/* 111 */       X509Certificate localX509Certificate = (X509Certificate)localCertificateFactory.generateCertificate(new ByteArrayInputStream(arrayOfByte));
/*     */ 
/* 115 */       if (localX509Certificate != null) {
/* 116 */         return localX509Certificate;
/*     */       }
/*     */ 
/* 119 */       return null;
/*     */     } catch (CertificateException localCertificateException) {
/* 121 */       throw new XMLSecurityException("empty", localCertificateException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public PublicKey getPublicKey()
/*     */     throws XMLSecurityException
/*     */   {
/* 133 */     X509Certificate localX509Certificate = getX509Certificate();
/*     */ 
/* 135 */     if (localX509Certificate != null) {
/* 136 */       return localX509Certificate.getPublicKey();
/*     */     }
/*     */ 
/* 139 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 145 */     if (paramObject == null) {
/* 146 */       return false;
/*     */     }
/* 148 */     if (!getClass().getName().equals(paramObject.getClass().getName())) {
/* 149 */       return false;
/*     */     }
/* 151 */     XMLX509Certificate localXMLX509Certificate = (XMLX509Certificate)paramObject;
/*     */     try
/*     */     {
/* 155 */       return MessageDigest.isEqual(localXMLX509Certificate.getCertificateBytes(), getCertificateBytes());
/*     */     } catch (XMLSecurityException localXMLSecurityException) {
/*     */     }
/* 158 */     return false;
/*     */   }
/*     */ 
/*     */   public String getBaseLocalName()
/*     */   {
/* 164 */     return "X509Certificate";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate
 * JD-Core Version:    0.6.2
 */