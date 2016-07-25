/*    */ package com.sun.jmx.snmp.IPAcl;
/*    */ 
/*    */ class JDMAclItem extends SimpleNode
/*    */ {
/* 32 */   protected JDMAccess access = null;
/* 33 */   protected JDMCommunities com = null;
/*    */ 
/*    */   JDMAclItem(int paramInt) {
/* 36 */     super(paramInt);
/*    */   }
/*    */ 
/*    */   JDMAclItem(Parser paramParser, int paramInt) {
/* 40 */     super(paramParser, paramInt);
/*    */   }
/*    */ 
/*    */   public static Node jjtCreate(int paramInt) {
/* 44 */     return new JDMAclItem(paramInt);
/*    */   }
/*    */ 
/*    */   public static Node jjtCreate(Parser paramParser, int paramInt) {
/* 48 */     return new JDMAclItem(paramParser, paramInt);
/*    */   }
/*    */ 
/*    */   public JDMAccess getAccess() {
/* 52 */     return this.access;
/*    */   }
/*    */ 
/*    */   public JDMCommunities getCommunities() {
/* 56 */     return this.com;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.JDMAclItem
 * JD-Core Version:    0.6.2
 */