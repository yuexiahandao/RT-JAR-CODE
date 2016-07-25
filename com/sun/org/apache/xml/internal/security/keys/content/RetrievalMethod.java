/*     */ package com.sun.org.apache.xml.internal.security.keys.content;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureException;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.Transforms;
/*     */ import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public class RetrievalMethod extends SignatureElementProxy
/*     */   implements KeyInfoContent
/*     */ {
/*     */   public static final String TYPE_DSA = "http://www.w3.org/2000/09/xmldsig#DSAKeyValue";
/*     */   public static final String TYPE_RSA = "http://www.w3.org/2000/09/xmldsig#RSAKeyValue";
/*     */   public static final String TYPE_PGP = "http://www.w3.org/2000/09/xmldsig#PGPData";
/*     */   public static final String TYPE_SPKI = "http://www.w3.org/2000/09/xmldsig#SPKIData";
/*     */   public static final String TYPE_MGMT = "http://www.w3.org/2000/09/xmldsig#MgmtData";
/*     */   public static final String TYPE_X509 = "http://www.w3.org/2000/09/xmldsig#X509Data";
/*     */   public static final String TYPE_RAWX509 = "http://www.w3.org/2000/09/xmldsig#rawX509Certificate";
/*     */ 
/*     */   public RetrievalMethod(Element paramElement, String paramString)
/*     */     throws XMLSecurityException
/*     */   {
/*  66 */     super(paramElement, paramString);
/*     */   }
/*     */ 
/*     */   public RetrievalMethod(Document paramDocument, String paramString1, Transforms paramTransforms, String paramString2)
/*     */   {
/*  80 */     super(paramDocument);
/*     */ 
/*  82 */     this._constructionElement.setAttributeNS(null, "URI", paramString1);
/*     */ 
/*  84 */     if (paramString2 != null) {
/*  85 */       this._constructionElement.setAttributeNS(null, "Type", paramString2);
/*     */     }
/*     */ 
/*  88 */     if (paramTransforms != null) {
/*  89 */       this._constructionElement.appendChild(paramTransforms.getElement());
/*  90 */       XMLUtils.addReturnToElement(this._constructionElement);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Attr getURIAttr()
/*     */   {
/* 100 */     return this._constructionElement.getAttributeNodeNS(null, "URI");
/*     */   }
/*     */ 
/*     */   public String getURI()
/*     */   {
/* 110 */     return getURIAttr().getNodeValue();
/*     */   }
/*     */ 
/*     */   public String getType()
/*     */   {
/* 115 */     return this._constructionElement.getAttributeNS(null, "Type");
/*     */   }
/*     */ 
/*     */   public Transforms getTransforms()
/*     */     throws XMLSecurityException
/*     */   {
/*     */     try
/*     */     {
/* 128 */       Element localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "Transforms", 0);
/*     */ 
/* 133 */       if (localElement != null) {
/* 134 */         return new Transforms(localElement, this._baseURI);
/*     */       }
/*     */ 
/* 137 */       return null;
/*     */     } catch (XMLSignatureException localXMLSignatureException) {
/* 139 */       throw new XMLSecurityException("empty", localXMLSignatureException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getBaseLocalName()
/*     */   {
/* 145 */     return "RetrievalMethod";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.keys.content.RetrievalMethod
 * JD-Core Version:    0.6.2
 */