/*    */ package com.sun.tracing.dtrace;
/*    */ 
/*    */ public enum StabilityLevel
/*    */ {
/* 39 */   INTERNAL(0), 
/*    */ 
/* 44 */   PRIVATE(1), 
/*    */ 
/* 49 */   OBSOLETE(2), 
/*    */ 
/* 53 */   EXTERNAL(3), 
/*    */ 
/* 60 */   UNSTABLE(4), 
/*    */ 
/* 65 */   EVOLVING(5), 
/*    */ 
/* 69 */   STABLE(6), 
/*    */ 
/* 73 */   STANDARD(7);
/*    */ 
/*    */   private int encoding;
/*    */ 
/* 76 */   String toDisplayString() { return toString().substring(0, 1) + toString().substring(1).toLowerCase(); }
/*    */ 
/*    */   public int getEncoding()
/*    */   {
/* 80 */     return this.encoding;
/*    */   }
/*    */ 
/*    */   private StabilityLevel(int paramInt)
/*    */   {
/* 85 */     this.encoding = paramInt;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.tracing.dtrace.StabilityLevel
 * JD-Core Version:    0.6.2
 */