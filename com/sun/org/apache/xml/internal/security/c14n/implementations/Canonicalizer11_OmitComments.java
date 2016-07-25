/*    */ package com.sun.org.apache.xml.internal.security.c14n.implementations;
/*    */ 
/*    */ public class Canonicalizer11_OmitComments extends Canonicalizer11
/*    */ {
/*    */   public Canonicalizer11_OmitComments()
/*    */   {
/* 31 */     super(false);
/*    */   }
/*    */ 
/*    */   public final String engineGetURI() {
/* 35 */     return "http://www.w3.org/2006/12/xml-c14n11";
/*    */   }
/*    */ 
/*    */   public final boolean engineGetIncludeComments() {
/* 39 */     return false;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer11_OmitComments
 * JD-Core Version:    0.6.2
 */