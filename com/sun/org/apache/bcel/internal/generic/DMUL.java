/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class DMUL extends ArithmeticInstruction
/*    */ {
/*    */   public DMUL()
/*    */   {
/* 72 */     super((short)107);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 85 */     v.visitTypedInstruction(this);
/* 86 */     v.visitStackProducer(this);
/* 87 */     v.visitStackConsumer(this);
/* 88 */     v.visitArithmeticInstruction(this);
/* 89 */     v.visitDMUL(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.DMUL
 * JD-Core Version:    0.6.2
 */