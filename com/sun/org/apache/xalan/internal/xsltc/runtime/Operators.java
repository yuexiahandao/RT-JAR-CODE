/*    */ package com.sun.org.apache.xalan.internal.xsltc.runtime;
/*    */ 
/*    */ public final class Operators
/*    */ {
/*    */   public static final int EQ = 0;
/*    */   public static final int NE = 1;
/*    */   public static final int GT = 2;
/*    */   public static final int LT = 3;
/*    */   public static final int GE = 4;
/*    */   public static final int LE = 5;
/* 38 */   private static final String[] names = { "=", "!=", ">", "<", ">=", "<=" };
/*    */ 
/* 47 */   private static final int[] swapOpArray = { 0, 1, 3, 2, 5, 4 };
/*    */ 
/*    */   public static final String getOpNames(int operator)
/*    */   {
/* 43 */     return names[operator];
/*    */   }
/*    */ 
/*    */   public static final int swapOp(int operator)
/*    */   {
/* 57 */     return swapOpArray[operator];
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.runtime.Operators
 * JD-Core Version:    0.6.2
 */