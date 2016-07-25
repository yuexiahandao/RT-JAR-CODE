/*    */ package com.sun.jmx.snmp.IPAcl;
/*    */ 
/*    */ class JDMInformCommunity extends SimpleNode
/*    */ {
/* 31 */   protected String community = "";
/*    */ 
/* 33 */   JDMInformCommunity(int paramInt) { super(paramInt); }
/*    */ 
/*    */   JDMInformCommunity(Parser paramParser, int paramInt)
/*    */   {
/* 37 */     super(paramParser, paramInt);
/*    */   }
/*    */ 
/*    */   public static Node jjtCreate(int paramInt) {
/* 41 */     return new JDMInformCommunity(paramInt);
/*    */   }
/*    */ 
/*    */   public static Node jjtCreate(Parser paramParser, int paramInt) {
/* 45 */     return new JDMInformCommunity(paramParser, paramInt);
/*    */   }
/*    */ 
/*    */   public String getCommunity() {
/* 49 */     return this.community;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.JDMInformCommunity
 * JD-Core Version:    0.6.2
 */