/*    */ package com.sun.org.apache.xml.internal.security.c14n.implementations;
/*    */ 
/*    */ public class Canonicalizer20010315ExclWithComments extends Canonicalizer20010315Excl
/*    */ {
/*    */   public Canonicalizer20010315ExclWithComments()
/*    */   {
/* 41 */     super(true);
/*    */   }
/*    */ 
/*    */   public final String engineGetURI()
/*    */   {
/* 46 */     return "http://www.w3.org/2001/10/xml-exc-c14n#WithComments";
/*    */   }
/*    */ 
/*    */   public final boolean engineGetIncludeComments()
/*    */   {
/* 51 */     return true;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer20010315ExclWithComments
 * JD-Core Version:    0.6.2
 */