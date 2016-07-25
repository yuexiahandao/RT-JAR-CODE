/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class LAND extends ArithmeticInstruction
/*    */ {
/*    */   public LAND()
/*    */   {
/* 70 */     super((short)127);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 83 */     v.visitTypedInstruction(this);
/* 84 */     v.visitStackProducer(this);
/* 85 */     v.visitStackConsumer(this);
/* 86 */     v.visitArithmeticInstruction(this);
/* 87 */     v.visitLAND(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.LAND
 * JD-Core Version:    0.6.2
 */