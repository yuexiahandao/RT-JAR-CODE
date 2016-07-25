/*     */ package com.sun.org.apache.xml.internal.security.keys.content.x509;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ import com.sun.org.apache.xml.internal.security.utils.Base64;
/*     */ import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public class XMLX509SKI extends SignatureElementProxy
/*     */   implements XMLX509DataContent
/*     */ {
/*  47 */   static Logger log = Logger.getLogger(XMLX509SKI.class.getName());
/*     */   public static final String SKI_OID = "2.5.29.14";
/*     */ 
/*     */   public XMLX509SKI(Document paramDocument, byte[] paramArrayOfByte)
/*     */   {
/*  68 */     super(paramDocument);
/*  69 */     addBase64Text(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public XMLX509SKI(Document paramDocument, X509Certificate paramX509Certificate)
/*     */     throws XMLSecurityException
/*     */   {
/*  81 */     super(paramDocument);
/*  82 */     addBase64Text(getSKIBytesFromCert(paramX509Certificate));
/*     */   }
/*     */ 
/*     */   public XMLX509SKI(Element paramElement, String paramString)
/*     */     throws XMLSecurityException
/*     */   {
/*  94 */     super(paramElement, paramString);
/*     */   }
/*     */ 
/*     */   public byte[] getSKIBytes()
/*     */     throws XMLSecurityException
/*     */   {
/* 104 */     return getBytesFromTextChild();
/*     */   }
/*     */ 
/*     */   public static byte[] getSKIBytesFromCert(X509Certificate paramX509Certificate)
/*     */     throws XMLSecurityException
/*     */   {
/* 119 */     if (paramX509Certificate.getVersion() < 3) {
/* 120 */       localObject = new Object[] { new Integer(paramX509Certificate.getVersion()) };
/* 121 */       throw new XMLSecurityException("certificate.noSki.lowVersion", (Object[])localObject);
/*     */     }
/*     */ 
/* 131 */     Object localObject = paramX509Certificate.getExtensionValue("2.5.29.14");
/* 132 */     if (localObject == null) {
/* 133 */       throw new XMLSecurityException("certificate.noSki.null");
/*     */     }
/*     */ 
/* 142 */     byte[] arrayOfByte = new byte[localObject.length - 4];
/*     */ 
/* 144 */     System.arraycopy(localObject, 4, arrayOfByte, 0, arrayOfByte.length);
/*     */ 
/* 146 */     if (log.isLoggable(Level.FINE)) {
/* 147 */       log.log(Level.FINE, "Base64 of SKI is " + Base64.encode(arrayOfByte));
/*     */     }
/*     */ 
/* 150 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 155 */     if (paramObject == null) {
/* 156 */       return false;
/*     */     }
/* 158 */     if (!getClass().getName().equals(paramObject.getClass().getName())) {
/* 159 */       return false;
/*     */     }
/*     */ 
/* 162 */     XMLX509SKI localXMLX509SKI = (XMLX509SKI)paramObject;
/*     */     try
/*     */     {
/* 165 */       return MessageDigest.isEqual(localXMLX509SKI.getSKIBytes(), getSKIBytes());
/*     */     } catch (XMLSecurityException localXMLSecurityException) {
/*     */     }
/* 168 */     return false;
/*     */   }
/*     */ 
/*     */   public String getBaseLocalName()
/*     */   {
/* 174 */     return "X509SKI";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509SKI
 * JD-Core Version:    0.6.2
 */