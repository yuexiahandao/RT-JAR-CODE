/*    */ package com.sun.jmx.snmp.IPAcl;
/*    */ 
/*    */ class JDMTrapNum extends SimpleNode
/*    */ {
/* 32 */   protected int low = 0;
/* 33 */   protected int high = 0;
/*    */ 
/*    */   JDMTrapNum(int paramInt) {
/* 36 */     super(paramInt);
/*    */   }
/*    */ 
/*    */   JDMTrapNum(Parser paramParser, int paramInt) {
/* 40 */     super(paramParser, paramInt);
/*    */   }
/*    */ 
/*    */   public static Node jjtCreate(int paramInt) {
/* 44 */     return new JDMTrapNum(paramInt);
/*    */   }
/*    */ 
/*    */   public static Node jjtCreate(Parser paramParser, int paramInt) {
/* 48 */     return new JDMTrapNum(paramParser, paramInt);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.JDMTrapNum
 * JD-Core Version:    0.6.2
 */