/*    */ package com.sun.jmx.snmp.IPAcl;
/*    */ 
/*    */ class JDMCommunity extends SimpleNode
/*    */ {
/* 32 */   protected String communityString = "";
/*    */ 
/*    */   JDMCommunity(int paramInt) {
/* 35 */     super(paramInt);
/*    */   }
/*    */ 
/*    */   JDMCommunity(Parser paramParser, int paramInt) {
/* 39 */     super(paramParser, paramInt);
/*    */   }
/*    */ 
/*    */   public static Node jjtCreate(int paramInt) {
/* 43 */     return new JDMCommunity(paramInt);
/*    */   }
/*    */ 
/*    */   public static Node jjtCreate(Parser paramParser, int paramInt) {
/* 47 */     return new JDMCommunity(paramParser, paramInt);
/*    */   }
/*    */ 
/*    */   public String getCommunity() {
/* 51 */     return this.communityString;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.JDMCommunity
 * JD-Core Version:    0.6.2
 */