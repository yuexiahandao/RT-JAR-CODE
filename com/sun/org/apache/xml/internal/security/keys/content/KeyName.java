/*    */ package com.sun.org.apache.xml.internal.security.keys.content;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*    */ import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
/*    */ import org.w3c.dom.Document;
/*    */ import org.w3c.dom.Element;
/*    */ 
/*    */ public class KeyName extends SignatureElementProxy
/*    */   implements KeyInfoContent
/*    */ {
/*    */   public KeyName(Element paramElement, String paramString)
/*    */     throws XMLSecurityException
/*    */   {
/* 43 */     super(paramElement, paramString);
/*    */   }
/*    */ 
/*    */   public KeyName(Document paramDocument, String paramString)
/*    */   {
/* 54 */     super(paramDocument);
/*    */ 
/* 56 */     addText(paramString);
/*    */   }
/*    */ 
/*    */   public String getKeyName()
/*    */   {
/* 65 */     return getTextFromTextChild();
/*    */   }
/*    */ 
/*    */   public String getBaseLocalName()
/*    */   {
/* 70 */     return "KeyName";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.keys.content.KeyName
 * JD-Core Version:    0.6.2
 */