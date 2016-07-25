/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class FADD extends ArithmeticInstruction
/*    */ {
/*    */   public FADD()
/*    */   {
/* 71 */     super((short)98);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 84 */     v.visitTypedInstruction(this);
/* 85 */     v.visitStackProducer(this);
/* 86 */     v.visitStackConsumer(this);
/* 87 */     v.visitArithmeticInstruction(this);
/* 88 */     v.visitFADD(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.FADD
 * JD-Core Version:    0.6.2
 */