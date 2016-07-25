/*     */ package com.sun.org.apache.xml.internal.security.keys.content.keyvalues;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ import com.sun.org.apache.xml.internal.security.utils.I18n;
/*     */ import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import java.math.BigInteger;
/*     */ import java.security.Key;
/*     */ import java.security.KeyFactory;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PublicKey;
/*     */ import java.security.interfaces.RSAPublicKey;
/*     */ import java.security.spec.InvalidKeySpecException;
/*     */ import java.security.spec.RSAPublicKeySpec;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public class RSAKeyValue extends SignatureElementProxy
/*     */   implements KeyValueContent
/*     */ {
/*     */   public RSAKeyValue(Element paramElement, String paramString)
/*     */     throws XMLSecurityException
/*     */   {
/*  56 */     super(paramElement, paramString);
/*     */   }
/*     */ 
/*     */   public RSAKeyValue(Document paramDocument, BigInteger paramBigInteger1, BigInteger paramBigInteger2)
/*     */   {
/*  68 */     super(paramDocument);
/*     */ 
/*  70 */     XMLUtils.addReturnToElement(this._constructionElement);
/*  71 */     addBigIntegerElement(paramBigInteger1, "Modulus");
/*  72 */     addBigIntegerElement(paramBigInteger2, "Exponent");
/*     */   }
/*     */ 
/*     */   public RSAKeyValue(Document paramDocument, Key paramKey)
/*     */     throws IllegalArgumentException
/*     */   {
/*  84 */     super(paramDocument);
/*     */ 
/*  86 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */ 
/*  88 */     if ((paramKey instanceof RSAPublicKey)) {
/*  89 */       addBigIntegerElement(((RSAPublicKey)paramKey).getModulus(), "Modulus");
/*     */ 
/*  91 */       addBigIntegerElement(((RSAPublicKey)paramKey).getPublicExponent(), "Exponent");
/*     */     }
/*     */     else {
/*  94 */       Object[] arrayOfObject = { "RSAKeyValue", paramKey.getClass().getName() };
/*     */ 
/*  97 */       throw new IllegalArgumentException(I18n.translate("KeyValue.IllegalArgument", arrayOfObject));
/*     */     }
/*     */   }
/*     */ 
/*     */   public PublicKey getPublicKey()
/*     */     throws XMLSecurityException
/*     */   {
/*     */     try
/*     */     {
/* 106 */       KeyFactory localKeyFactory = KeyFactory.getInstance("RSA");
/*     */ 
/* 109 */       RSAPublicKeySpec localRSAPublicKeySpec = new RSAPublicKeySpec(getBigIntegerFromChildElement("Modulus", "http://www.w3.org/2000/09/xmldsig#"), getBigIntegerFromChildElement("Exponent", "http://www.w3.org/2000/09/xmldsig#"));
/*     */ 
/* 115 */       return localKeyFactory.generatePublic(localRSAPublicKeySpec);
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
/*     */     {
/* 119 */       throw new XMLSecurityException("empty", localNoSuchAlgorithmException);
/*     */     } catch (InvalidKeySpecException localInvalidKeySpecException) {
/* 121 */       throw new XMLSecurityException("empty", localInvalidKeySpecException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getBaseLocalName()
/*     */   {
/* 127 */     return "RSAKeyValue";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.keys.content.keyvalues.RSAKeyValue
 * JD-Core Version:    0.6.2
 */