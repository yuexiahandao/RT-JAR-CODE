/*    */ package com.sun.jmx.snmp.IPAcl;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ 
/*    */ class JDMAclBlock extends SimpleNode
/*    */ {
/*    */   JDMAclBlock(int paramInt)
/*    */   {
/* 35 */     super(paramInt);
/*    */   }
/*    */ 
/*    */   JDMAclBlock(Parser paramParser, int paramInt) {
/* 39 */     super(paramParser, paramInt);
/*    */   }
/*    */ 
/*    */   public static Node jjtCreate(int paramInt) {
/* 43 */     return new JDMAclBlock(paramInt);
/*    */   }
/*    */ 
/*    */   public static Node jjtCreate(Parser paramParser, int paramInt) {
/* 47 */     return new JDMAclBlock(paramParser, paramInt);
/*    */   }
/*    */ 
/*    */   public void buildTrapEntries(Hashtable paramHashtable)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void buildInformEntries(Hashtable paramHashtable)
/*    */   {
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.JDMAclBlock
 * JD-Core Version:    0.6.2
 */