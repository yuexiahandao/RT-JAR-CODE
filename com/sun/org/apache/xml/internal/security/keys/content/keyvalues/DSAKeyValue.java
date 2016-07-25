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
/*     */ import java.security.interfaces.DSAParams;
/*     */ import java.security.interfaces.DSAPublicKey;
/*     */ import java.security.spec.DSAPublicKeySpec;
/*     */ import java.security.spec.InvalidKeySpecException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public class DSAKeyValue extends SignatureElementProxy
/*     */   implements KeyValueContent
/*     */ {
/*     */   public DSAKeyValue(Element paramElement, String paramString)
/*     */     throws XMLSecurityException
/*     */   {
/*  56 */     super(paramElement, paramString);
/*     */   }
/*     */ 
/*     */   public DSAKeyValue(Document paramDocument, BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4)
/*     */   {
/*  71 */     super(paramDocument);
/*     */ 
/*  73 */     XMLUtils.addReturnToElement(this._constructionElement);
/*  74 */     addBigIntegerElement(paramBigInteger1, "P");
/*  75 */     addBigIntegerElement(paramBigInteger2, "Q");
/*  76 */     addBigIntegerElement(paramBigInteger3, "G");
/*  77 */     addBigIntegerElement(paramBigInteger4, "Y");
/*     */   }
/*     */ 
/*     */   public DSAKeyValue(Document paramDocument, Key paramKey)
/*     */     throws IllegalArgumentException
/*     */   {
/*  89 */     super(paramDocument);
/*     */ 
/*  91 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */ 
/*  93 */     if ((paramKey instanceof DSAPublicKey)) {
/*  94 */       addBigIntegerElement(((DSAPublicKey)paramKey).getParams().getP(), "P");
/*     */ 
/*  96 */       addBigIntegerElement(((DSAPublicKey)paramKey).getParams().getQ(), "Q");
/*     */ 
/*  98 */       addBigIntegerElement(((DSAPublicKey)paramKey).getParams().getG(), "G");
/*     */ 
/* 100 */       addBigIntegerElement(((DSAPublicKey)paramKey).getY(), "Y");
/*     */     }
/*     */     else {
/* 103 */       Object[] arrayOfObject = { "DSAKeyValue", paramKey.getClass().getName() };
/*     */ 
/* 106 */       throw new IllegalArgumentException(I18n.translate("KeyValue.IllegalArgument", arrayOfObject));
/*     */     }
/*     */   }
/*     */ 
/*     */   public PublicKey getPublicKey()
/*     */     throws XMLSecurityException
/*     */   {
/*     */     try
/*     */     {
/* 115 */       DSAPublicKeySpec localDSAPublicKeySpec = new DSAPublicKeySpec(getBigIntegerFromChildElement("Y", "http://www.w3.org/2000/09/xmldsig#"), getBigIntegerFromChildElement("P", "http://www.w3.org/2000/09/xmldsig#"), getBigIntegerFromChildElement("Q", "http://www.w3.org/2000/09/xmldsig#"), getBigIntegerFromChildElement("G", "http://www.w3.org/2000/09/xmldsig#"));
/*     */ 
/* 125 */       KeyFactory localKeyFactory = KeyFactory.getInstance("DSA");
/* 126 */       return localKeyFactory.generatePublic(localDSAPublicKeySpec);
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
/*     */     {
/* 130 */       throw new XMLSecurityException("empty", localNoSuchAlgorithmException);
/*     */     } catch (InvalidKeySpecException localInvalidKeySpecException) {
/* 132 */       throw new XMLSecurityException("empty", localInvalidKeySpecException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getBaseLocalName()
/*     */   {
/* 138 */     return "DSAKeyValue";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.keys.content.keyvalues.DSAKeyValue
 * JD-Core Version:    0.6.2
 */