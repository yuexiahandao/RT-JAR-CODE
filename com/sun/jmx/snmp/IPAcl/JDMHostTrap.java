/*    */ package com.sun.jmx.snmp.IPAcl;
/*    */ 
/*    */ class JDMHostTrap extends SimpleNode
/*    */ {
/* 32 */   protected String name = "";
/*    */ 
/*    */   JDMHostTrap(int paramInt) {
/* 35 */     super(paramInt);
/*    */   }
/*    */ 
/*    */   JDMHostTrap(Parser paramParser, int paramInt) {
/* 39 */     super(paramParser, paramInt);
/*    */   }
/*    */ 
/*    */   public static Node jjtCreate(int paramInt) {
/* 43 */     return new JDMHostTrap(paramInt);
/*    */   }
/*    */ 
/*    */   public static Node jjtCreate(Parser paramParser, int paramInt) {
/* 47 */     return new JDMHostTrap(paramParser, paramInt);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.JDMHostTrap
 * JD-Core Version:    0.6.2
 */