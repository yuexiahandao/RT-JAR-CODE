/*     */ package com.sun.org.apache.xml.internal.security.keys.content.x509;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ import com.sun.org.apache.xml.internal.security.utils.RFC2253Parser;
/*     */ import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import java.math.BigInteger;
/*     */ import java.security.Principal;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public class XMLX509IssuerSerial extends SignatureElementProxy
/*     */   implements XMLX509DataContent
/*     */ {
/*  42 */   static Logger log = Logger.getLogger(XMLX509IssuerSerial.class.getName());
/*     */ 
/*     */   public XMLX509IssuerSerial(Element paramElement, String paramString)
/*     */     throws XMLSecurityException
/*     */   {
/*  55 */     super(paramElement, paramString);
/*     */   }
/*     */ 
/*     */   public XMLX509IssuerSerial(Document paramDocument, String paramString, BigInteger paramBigInteger)
/*     */   {
/*  68 */     super(paramDocument);
/*  69 */     XMLUtils.addReturnToElement(this._constructionElement);
/*  70 */     addTextElement(paramString, "X509IssuerName");
/*  71 */     addTextElement(paramBigInteger.toString(), "X509SerialNumber");
/*     */   }
/*     */ 
/*     */   public XMLX509IssuerSerial(Document paramDocument, String paramString1, String paramString2)
/*     */   {
/*  83 */     this(paramDocument, paramString1, new BigInteger(paramString2));
/*     */   }
/*     */ 
/*     */   public XMLX509IssuerSerial(Document paramDocument, String paramString, int paramInt)
/*     */   {
/*  95 */     this(paramDocument, paramString, new BigInteger(Integer.toString(paramInt)));
/*     */   }
/*     */ 
/*     */   public XMLX509IssuerSerial(Document paramDocument, X509Certificate paramX509Certificate)
/*     */   {
/* 107 */     this(paramDocument, RFC2253Parser.normalize(paramX509Certificate.getIssuerDN().getName()), paramX509Certificate.getSerialNumber());
/*     */   }
/*     */ 
/*     */   public BigInteger getSerialNumber()
/*     */   {
/* 119 */     String str = getTextFromChildElement("X509SerialNumber", "http://www.w3.org/2000/09/xmldsig#");
/*     */ 
/* 121 */     if (log.isLoggable(Level.FINE)) {
/* 122 */       log.log(Level.FINE, "X509SerialNumber text: " + str);
/*     */     }
/* 124 */     return new BigInteger(str);
/*     */   }
/*     */ 
/*     */   public int getSerialNumberInteger()
/*     */   {
/* 133 */     return getSerialNumber().intValue();
/*     */   }
/*     */ 
/*     */   public String getIssuerName()
/*     */   {
/* 143 */     return RFC2253Parser.normalize(getTextFromChildElement("X509IssuerName", "http://www.w3.org/2000/09/xmldsig#"));
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 152 */     if (paramObject == null) {
/* 153 */       return false;
/*     */     }
/* 155 */     if (!getClass().getName().equals(paramObject.getClass().getName())) {
/* 156 */       return false;
/*     */     }
/*     */ 
/* 159 */     XMLX509IssuerSerial localXMLX509IssuerSerial = (XMLX509IssuerSerial)paramObject;
/*     */ 
/* 161 */     return (getSerialNumber().equals(localXMLX509IssuerSerial.getSerialNumber())) && (getIssuerName().equals(localXMLX509IssuerSerial.getIssuerName()));
/*     */   }
/*     */ 
/*     */   public String getBaseLocalName()
/*     */   {
/* 167 */     return "X509IssuerSerial";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509IssuerSerial
 * JD-Core Version:    0.6.2
 */