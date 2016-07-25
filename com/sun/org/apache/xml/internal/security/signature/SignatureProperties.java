/*     */ package com.sun.org.apache.xml.internal.security.signature;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public class SignatureProperties extends SignatureElementProxy
/*     */ {
/*     */   public SignatureProperties(Document paramDocument)
/*     */   {
/*  51 */     super(paramDocument);
/*     */ 
/*  53 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */   }
/*     */ 
/*     */   public SignatureProperties(Element paramElement, String paramString)
/*     */     throws XMLSecurityException
/*     */   {
/*  64 */     super(paramElement, paramString);
/*     */ 
/*  66 */     Attr localAttr1 = paramElement.getAttributeNodeNS(null, "Id");
/*  67 */     if (localAttr1 != null) {
/*  68 */       paramElement.setIdAttributeNode(localAttr1, true);
/*     */     }
/*     */ 
/*  71 */     int i = getLength();
/*  72 */     for (int j = 0; j < i; j++) {
/*  73 */       Element localElement = XMLUtils.selectDsNode(getElement(), "SignatureProperty", j);
/*     */ 
/*  75 */       Attr localAttr2 = localElement.getAttributeNodeNS(null, "Id");
/*  76 */       if (localAttr2 != null)
/*  77 */         localElement.setIdAttributeNode(localAttr2, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/*  89 */     Element[] arrayOfElement = XMLUtils.selectDsNodes(this._constructionElement, "SignatureProperty");
/*     */ 
/*  94 */     return arrayOfElement.length;
/*     */   }
/*     */ 
/*     */   public SignatureProperty item(int paramInt)
/*     */     throws XMLSignatureException
/*     */   {
/*     */     try
/*     */     {
/* 107 */       Element localElement = XMLUtils.selectDsNode(this._constructionElement, "SignatureProperty", paramInt);
/*     */ 
/* 112 */       if (localElement == null) {
/* 113 */         return null;
/*     */       }
/* 115 */       return new SignatureProperty(localElement, this._baseURI);
/*     */     } catch (XMLSecurityException localXMLSecurityException) {
/* 117 */       throw new XMLSignatureException("empty", localXMLSecurityException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setId(String paramString)
/*     */   {
/* 128 */     if (paramString != null)
/* 129 */       setLocalIdAttribute("Id", paramString);
/*     */   }
/*     */ 
/*     */   public String getId()
/*     */   {
/* 139 */     return this._constructionElement.getAttributeNS(null, "Id");
/*     */   }
/*     */ 
/*     */   public void addSignatureProperty(SignatureProperty paramSignatureProperty)
/*     */   {
/* 148 */     this._constructionElement.appendChild(paramSignatureProperty.getElement());
/* 149 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */   }
/*     */ 
/*     */   public String getBaseLocalName()
/*     */   {
/* 154 */     return "SignatureProperties";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.signature.SignatureProperties
 * JD-Core Version:    0.6.2
 */