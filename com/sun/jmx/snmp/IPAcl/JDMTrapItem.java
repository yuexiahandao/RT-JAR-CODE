/*    */ package com.sun.jmx.snmp.IPAcl;
/*    */ 
/*    */ class JDMTrapItem extends SimpleNode
/*    */ {
/* 32 */   protected JDMTrapCommunity comm = null;
/*    */ 
/*    */   JDMTrapItem(int paramInt) {
/* 35 */     super(paramInt);
/*    */   }
/*    */ 
/*    */   JDMTrapItem(Parser paramParser, int paramInt) {
/* 39 */     super(paramParser, paramInt);
/*    */   }
/*    */ 
/*    */   public static Node jjtCreate(int paramInt) {
/* 43 */     return new JDMTrapItem(paramInt);
/*    */   }
/*    */ 
/*    */   public static Node jjtCreate(Parser paramParser, int paramInt) {
/* 47 */     return new JDMTrapItem(paramParser, paramInt);
/*    */   }
/*    */ 
/*    */   public JDMTrapCommunity getCommunity() {
/* 51 */     return this.comm;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.JDMTrapItem
 * JD-Core Version:    0.6.2
 */