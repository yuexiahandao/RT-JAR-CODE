/*    */ package com.sun.org.apache.xerces.internal.impl.xpath;
/*    */ 
/*    */ public class XPathException extends Exception
/*    */ {
/*    */   static final long serialVersionUID = -948482312169512085L;
/*    */   private String fKey;
/*    */ 
/*    */   public XPathException()
/*    */   {
/* 48 */     this.fKey = "c-general-xpath";
/*    */   }
/*    */ 
/*    */   public XPathException(String key)
/*    */   {
/* 54 */     this.fKey = key;
/*    */   }
/*    */ 
/*    */   public String getKey() {
/* 58 */     return this.fKey;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xpath.XPathException
 * JD-Core Version:    0.6.2
 */