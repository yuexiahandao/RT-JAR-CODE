/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class ASTORE extends StoreInstruction
/*    */ {
/*    */   ASTORE()
/*    */   {
/* 73 */     super((short)58, (short)75);
/*    */   }
/*    */ 
/*    */   public ASTORE(int n)
/*    */   {
/* 80 */     super((short)58, (short)75, n);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 92 */     super.accept(v);
/* 93 */     v.visitASTORE(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.ASTORE
 * JD-Core Version:    0.6.2
 */