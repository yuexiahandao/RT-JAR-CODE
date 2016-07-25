/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class DSTORE extends StoreInstruction
/*    */ {
/*    */   DSTORE()
/*    */   {
/* 73 */     super((short)57, (short)71);
/*    */   }
/*    */ 
/*    */   public DSTORE(int n)
/*    */   {
/* 80 */     super((short)57, (short)71, n);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 92 */     super.accept(v);
/* 93 */     v.visitDSTORE(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.DSTORE
 * JD-Core Version:    0.6.2
 */