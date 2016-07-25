/*    */ package com.sun.org.apache.xml.internal.security.c14n.implementations;
/*    */ 
/*    */ public class Canonicalizer20010315ExclOmitComments extends Canonicalizer20010315Excl
/*    */ {
/*    */   public Canonicalizer20010315ExclOmitComments()
/*    */   {
/* 37 */     super(false);
/*    */   }
/*    */ 
/*    */   public final String engineGetURI()
/*    */   {
/* 42 */     return "http://www.w3.org/2001/10/xml-exc-c14n#";
/*    */   }
/*    */ 
/*    */   public final boolean engineGetIncludeComments()
/*    */   {
/* 47 */     return false;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer20010315ExclOmitComments
 * JD-Core Version:    0.6.2
 */