/*    */ package com.sun.jmx.snmp.IPAcl;
/*    */ 
/*    */ class JDMInformItem extends SimpleNode
/*    */ {
/* 31 */   protected JDMInformCommunity comm = null;
/*    */ 
/* 33 */   JDMInformItem(int paramInt) { super(paramInt); }
/*    */ 
/*    */   JDMInformItem(Parser paramParser, int paramInt)
/*    */   {
/* 37 */     super(paramParser, paramInt);
/*    */   }
/*    */ 
/*    */   public static Node jjtCreate(int paramInt) {
/* 41 */     return new JDMInformItem(paramInt);
/*    */   }
/*    */ 
/*    */   public static Node jjtCreate(Parser paramParser, int paramInt) {
/* 45 */     return new JDMInformItem(paramParser, paramInt);
/*    */   }
/*    */ 
/*    */   public JDMInformCommunity getCommunity() {
/* 49 */     return this.comm;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.JDMInformItem
 * JD-Core Version:    0.6.2
 */