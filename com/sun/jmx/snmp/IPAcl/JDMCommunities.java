/*    */ package com.sun.jmx.snmp.IPAcl;
/*    */ 
/*    */ class JDMCommunities extends SimpleNode
/*    */ {
/*    */   JDMCommunities(int paramInt)
/*    */   {
/* 34 */     super(paramInt);
/*    */   }
/*    */ 
/*    */   JDMCommunities(Parser paramParser, int paramInt) {
/* 38 */     super(paramParser, paramInt);
/*    */   }
/*    */ 
/*    */   public static Node jjtCreate(int paramInt) {
/* 42 */     return new JDMCommunities(paramInt);
/*    */   }
/*    */ 
/*    */   public static Node jjtCreate(Parser paramParser, int paramInt) {
/* 46 */     return new JDMCommunities(paramParser, paramInt);
/*    */   }
/*    */ 
/*    */   public void buildCommunities(AclEntryImpl paramAclEntryImpl) {
/* 50 */     for (int i = 0; i < this.children.length; i++)
/* 51 */       paramAclEntryImpl.addCommunity(((JDMCommunity)this.children[i]).getCommunity());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.JDMCommunities
 * JD-Core Version:    0.6.2
 */