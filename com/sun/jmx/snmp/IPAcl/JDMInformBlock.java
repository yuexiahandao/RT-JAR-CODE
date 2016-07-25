/*    */ package com.sun.jmx.snmp.IPAcl;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ 
/*    */ class JDMInformBlock extends SimpleNode
/*    */ {
/*    */   JDMInformBlock(int paramInt)
/*    */   {
/* 34 */     super(paramInt);
/*    */   }
/*    */ 
/*    */   JDMInformBlock(Parser paramParser, int paramInt) {
/* 38 */     super(paramParser, paramInt);
/*    */   }
/*    */ 
/*    */   public static Node jjtCreate(int paramInt) {
/* 42 */     return new JDMInformBlock(paramInt);
/*    */   }
/*    */ 
/*    */   public static Node jjtCreate(Parser paramParser, int paramInt) {
/* 46 */     return new JDMInformBlock(paramParser, paramInt);
/*    */   }
/*    */ 
/*    */   public void buildAclEntries(PrincipalImpl paramPrincipalImpl, AclImpl paramAclImpl)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void buildTrapEntries(Hashtable paramHashtable)
/*    */   {
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.JDMInformBlock
 * JD-Core Version:    0.6.2
 */