/*     */ package com.sun.org.apache.xml.internal.security.keys.content;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ import com.sun.org.apache.xml.internal.security.keys.content.keyvalues.DSAKeyValue;
/*     */ import com.sun.org.apache.xml.internal.security.keys.content.keyvalues.RSAKeyValue;
/*     */ import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import java.security.PublicKey;
/*     */ import java.security.interfaces.DSAPublicKey;
/*     */ import java.security.interfaces.RSAPublicKey;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public class KeyValue extends SignatureElementProxy
/*     */   implements KeyInfoContent
/*     */ {
/*     */   public KeyValue(Document paramDocument, DSAKeyValue paramDSAKeyValue)
/*     */   {
/*  54 */     super(paramDocument);
/*     */ 
/*  56 */     XMLUtils.addReturnToElement(this._constructionElement);
/*  57 */     this._constructionElement.appendChild(paramDSAKeyValue.getElement());
/*  58 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */   }
/*     */ 
/*     */   public KeyValue(Document paramDocument, RSAKeyValue paramRSAKeyValue)
/*     */   {
/*  69 */     super(paramDocument);
/*     */ 
/*  71 */     XMLUtils.addReturnToElement(this._constructionElement);
/*  72 */     this._constructionElement.appendChild(paramRSAKeyValue.getElement());
/*  73 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */   }
/*     */ 
/*     */   public KeyValue(Document paramDocument, Element paramElement)
/*     */   {
/*  84 */     super(paramDocument);
/*     */ 
/*  86 */     XMLUtils.addReturnToElement(this._constructionElement);
/*  87 */     this._constructionElement.appendChild(paramElement);
/*  88 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */   }
/*     */ 
/*     */   public KeyValue(Document paramDocument, PublicKey paramPublicKey)
/*     */   {
/*  99 */     super(paramDocument);
/*     */ 
/* 101 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */     Object localObject;
/* 103 */     if ((paramPublicKey instanceof DSAPublicKey)) {
/* 104 */       localObject = new DSAKeyValue(this._doc, paramPublicKey);
/*     */ 
/* 106 */       this._constructionElement.appendChild(((DSAKeyValue)localObject).getElement());
/* 107 */       XMLUtils.addReturnToElement(this._constructionElement);
/* 108 */     } else if ((paramPublicKey instanceof RSAPublicKey)) {
/* 109 */       localObject = new RSAKeyValue(this._doc, paramPublicKey);
/*     */ 
/* 111 */       this._constructionElement.appendChild(((RSAKeyValue)localObject).getElement());
/* 112 */       XMLUtils.addReturnToElement(this._constructionElement);
/*     */     }
/*     */   }
/*     */ 
/*     */   public KeyValue(Element paramElement, String paramString)
/*     */     throws XMLSecurityException
/*     */   {
/* 125 */     super(paramElement, paramString);
/*     */   }
/*     */ 
/*     */   public PublicKey getPublicKey()
/*     */     throws XMLSecurityException
/*     */   {
/* 136 */     Element localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "RSAKeyValue", 0);
/*     */ 
/* 140 */     if (localElement != null) {
/* 141 */       localObject = new RSAKeyValue(localElement, this._baseURI);
/* 142 */       return ((RSAKeyValue)localObject).getPublicKey();
/*     */     }
/*     */ 
/* 145 */     Object localObject = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "DSAKeyValue", 0);
/*     */ 
/* 149 */     if (localObject != null) {
/* 150 */       DSAKeyValue localDSAKeyValue = new DSAKeyValue((Element)localObject, this._baseURI);
/* 151 */       return localDSAKeyValue.getPublicKey();
/*     */     }
/*     */ 
/* 154 */     return null;
/*     */   }
/*     */ 
/*     */   public String getBaseLocalName()
/*     */   {
/* 159 */     return "KeyValue";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.keys.content.KeyValue
 * JD-Core Version:    0.6.2
 */