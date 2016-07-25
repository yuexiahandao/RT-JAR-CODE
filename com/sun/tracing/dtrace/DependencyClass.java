/*    */ package com.sun.tracing.dtrace;
/*    */ 
/*    */ public enum DependencyClass
/*    */ {
/* 38 */   UNKNOWN(0), 
/*    */ 
/* 42 */   CPU(1), 
/*    */ 
/* 47 */   PLATFORM(2), 
/*    */ 
/* 52 */   GROUP(3), 
/*    */ 
/* 57 */   ISA(4), 
/*    */ 
/* 62 */   COMMON(5);
/*    */ 
/*    */   private int encoding;
/*    */ 
/* 65 */   public String toDisplayString() { return toString().substring(0, 1) + toString().substring(1).toLowerCase(); }
/*    */ 
/*    */   public int getEncoding()
/*    */   {
/* 69 */     return this.encoding;
/*    */   }
/*    */ 
/*    */   private DependencyClass(int paramInt)
/*    */   {
/* 74 */     this.encoding = paramInt;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.tracing.dtrace.DependencyClass
 * JD-Core Version:    0.6.2
 */