/*    */ package com.sun.jmx.snmp.IPAcl;
/*    */ 
/*    */ class JDMTrapCommunity extends SimpleNode
/*    */ {
/* 32 */   protected String community = "";
/*    */ 
/* 34 */   JDMTrapCommunity(int paramInt) { super(paramInt); }
/*    */ 
/*    */   JDMTrapCommunity(Parser paramParser, int paramInt)
/*    */   {
/* 38 */     super(paramParser, paramInt);
/*    */   }
/*    */ 
/*    */   public static Node jjtCreate(int paramInt) {
/* 42 */     return new JDMTrapCommunity(paramInt);
/*    */   }
/*    */ 
/*    */   public static Node jjtCreate(Parser paramParser, int paramInt) {
/* 46 */     return new JDMTrapCommunity(paramParser, paramInt);
/*    */   }
/*    */ 
/*    */   public String getCommunity() {
/* 50 */     return this.community;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.JDMTrapCommunity
 * JD-Core Version:    0.6.2
 */