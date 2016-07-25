/*    */ package com.sun.jmx.snmp.IPAcl;
/*    */ 
/*    */ class JDMEnterprise extends SimpleNode
/*    */ {
/* 32 */   protected String enterprise = "";
/*    */ 
/*    */   JDMEnterprise(int paramInt) {
/* 35 */     super(paramInt);
/*    */   }
/*    */ 
/*    */   JDMEnterprise(Parser paramParser, int paramInt) {
/* 39 */     super(paramParser, paramInt);
/*    */   }
/*    */ 
/*    */   public static Node jjtCreate(int paramInt) {
/* 43 */     return new JDMEnterprise(paramInt);
/*    */   }
/*    */ 
/*    */   public static Node jjtCreate(Parser paramParser, int paramInt) {
/* 47 */     return new JDMEnterprise(paramParser, paramInt);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.JDMEnterprise
 * JD-Core Version:    0.6.2
 */