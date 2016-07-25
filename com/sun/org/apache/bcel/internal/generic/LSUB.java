/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class LSUB extends ArithmeticInstruction
/*    */ {
/*    */   public LSUB()
/*    */   {
/* 70 */     super((short)101);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 83 */     v.visitTypedInstruction(this);
/* 84 */     v.visitStackProducer(this);
/* 85 */     v.visitStackConsumer(this);
/* 86 */     v.visitArithmeticInstruction(this);
/* 87 */     v.visitLSUB(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.LSUB
 * JD-Core Version:    0.6.2
 */