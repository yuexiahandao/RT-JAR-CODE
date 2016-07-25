/*    */ package com.sun.jmx.snmp.IPAcl;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ 
/*    */ class JDMTrapBlock extends SimpleNode
/*    */ {
/*    */   JDMTrapBlock(int paramInt)
/*    */   {
/* 35 */     super(paramInt);
/*    */   }
/*    */ 
/*    */   JDMTrapBlock(Parser paramParser, int paramInt) {
/* 39 */     super(paramParser, paramInt);
/*    */   }
/*    */ 
/*    */   public static Node jjtCreate(int paramInt) {
/* 43 */     return new JDMTrapBlock(paramInt);
/*    */   }
/*    */ 
/*    */   public static Node jjtCreate(Parser paramParser, int paramInt) {
/* 47 */     return new JDMTrapBlock(paramParser, paramInt);
/*    */   }
/*    */ 
/*    */   public void buildAclEntries(PrincipalImpl paramPrincipalImpl, AclImpl paramAclImpl)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void buildInformEntries(Hashtable paramHashtable)
/*    */   {
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.JDMTrapBlock
 * JD-Core Version:    0.6.2
 */