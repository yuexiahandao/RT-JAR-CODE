/*    */ package com.sun.org.apache.regexp.internal;
/*    */ 
/*    */ public class REUtil
/*    */ {
/*    */   private static final String complexPrefix = "complex:";
/*    */ 
/*    */   public static RE createRE(String expression, int matchFlags)
/*    */     throws RESyntaxException
/*    */   {
/* 43 */     if (expression.startsWith("complex:"))
/*    */     {
/* 45 */       return new RE(expression.substring("complex:".length()), matchFlags);
/*    */     }
/* 47 */     return new RE(RE.simplePatternToFullRegularExpression(expression), matchFlags);
/*    */   }
/*    */ 
/*    */   public static RE createRE(String expression)
/*    */     throws RESyntaxException
/*    */   {
/* 59 */     return createRE(expression, 0);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.regexp.internal.REUtil
 * JD-Core Version:    0.6.2
 */