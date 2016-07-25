/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class LXOR extends ArithmeticInstruction
/*    */ {
/*    */   public LXOR()
/*    */   {
/* 69 */     super((short)131);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 82 */     v.visitTypedInstruction(this);
/* 83 */     v.visitStackProducer(this);
/* 84 */     v.visitStackConsumer(this);
/* 85 */     v.visitArithmeticInstruction(this);
/* 86 */     v.visitLXOR(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.LXOR
 * JD-Core Version:    0.6.2
 */