/*    */ package com.sun.org.apache.xml.internal.security.utils;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*    */ import org.w3c.dom.Document;
/*    */ import org.w3c.dom.Element;
/*    */ 
/*    */ public abstract class SignatureElementProxy extends ElementProxy
/*    */ {
/*    */   protected SignatureElementProxy()
/*    */   {
/*    */   }
/*    */ 
/*    */   public SignatureElementProxy(Document paramDocument)
/*    */   {
/* 45 */     if (paramDocument == null) {
/* 46 */       throw new RuntimeException("Document is null");
/*    */     }
/*    */ 
/* 49 */     this._doc = paramDocument;
/* 50 */     this._constructionElement = XMLUtils.createElementInSignatureSpace(this._doc, getBaseLocalName());
/*    */   }
/*    */ 
/*    */   public SignatureElementProxy(Element paramElement, String paramString)
/*    */     throws XMLSecurityException
/*    */   {
/* 63 */     super(paramElement, paramString);
/*    */   }
/*    */ 
/*    */   public String getBaseNamespace()
/*    */   {
/* 69 */     return "http://www.w3.org/2000/09/xmldsig#";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy
 * JD-Core Version:    0.6.2
 */