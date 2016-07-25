/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class FSTORE extends StoreInstruction
/*    */ {
/*    */   FSTORE()
/*    */   {
/* 73 */     super((short)56, (short)67);
/*    */   }
/*    */ 
/*    */   public FSTORE(int n)
/*    */   {
/* 80 */     super((short)56, (short)67, n);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 92 */     super.accept(v);
/* 93 */     v.visitFSTORE(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.FSTORE
 * JD-Core Version:    0.6.2
 */