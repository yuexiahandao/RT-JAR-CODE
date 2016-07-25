/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class ISTORE extends StoreInstruction
/*    */ {
/*    */   ISTORE()
/*    */   {
/* 73 */     super((short)54, (short)59);
/*    */   }
/*    */ 
/*    */   public ISTORE(int n)
/*    */   {
/* 80 */     super((short)54, (short)59, n);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 92 */     super.accept(v);
/* 93 */     v.visitISTORE(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.ISTORE
 * JD-Core Version:    0.6.2
 */