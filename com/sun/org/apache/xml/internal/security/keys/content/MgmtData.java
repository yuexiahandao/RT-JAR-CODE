/*    */ package com.sun.org.apache.xml.internal.security.keys.content;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*    */ import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
/*    */ import org.w3c.dom.Document;
/*    */ import org.w3c.dom.Element;
/*    */ 
/*    */ public class MgmtData extends SignatureElementProxy
/*    */   implements KeyInfoContent
/*    */ {
/*    */   public MgmtData(Element paramElement, String paramString)
/*    */     throws XMLSecurityException
/*    */   {
/* 44 */     super(paramElement, paramString);
/*    */   }
/*    */ 
/*    */   public MgmtData(Document paramDocument, String paramString)
/*    */   {
/* 55 */     super(paramDocument);
/*    */ 
/* 57 */     addText(paramString);
/*    */   }
/*    */ 
/*    */   public String getMgmtData()
/*    */   {
/* 66 */     return getTextFromTextChild();
/*    */   }
/*    */ 
/*    */   public String getBaseLocalName()
/*    */   {
/* 71 */     return "MgmtData";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.keys.content.MgmtData
 * JD-Core Version:    0.6.2
 */