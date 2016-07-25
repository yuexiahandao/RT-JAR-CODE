/*    */ package com.sun.org.apache.xml.internal.security.algorithms;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*    */ import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
/*    */ import org.w3c.dom.Document;
/*    */ import org.w3c.dom.Element;
/*    */ 
/*    */ public abstract class Algorithm extends SignatureElementProxy
/*    */ {
/*    */   public Algorithm(Document paramDocument, String paramString)
/*    */   {
/* 45 */     super(paramDocument);
/*    */ 
/* 47 */     setAlgorithmURI(paramString);
/*    */   }
/*    */ 
/*    */   public Algorithm(Element paramElement, String paramString)
/*    */     throws XMLSecurityException
/*    */   {
/* 59 */     super(paramElement, paramString);
/*    */   }
/*    */ 
/*    */   public String getAlgorithmURI()
/*    */   {
/* 68 */     return this._constructionElement.getAttributeNS(null, "Algorithm");
/*    */   }
/*    */ 
/*    */   protected void setAlgorithmURI(String paramString)
/*    */   {
/* 78 */     if (paramString != null)
/* 79 */       this._constructionElement.setAttributeNS(null, "Algorithm", paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.algorithms.Algorithm
 * JD-Core Version:    0.6.2
 */