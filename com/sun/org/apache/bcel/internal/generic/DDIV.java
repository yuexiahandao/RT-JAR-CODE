/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class DDIV extends ArithmeticInstruction
/*    */ {
/*    */   public DDIV()
/*    */   {
/* 72 */     super((short)111);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 85 */     v.visitTypedInstruction(this);
/* 86 */     v.visitStackProducer(this);
/* 87 */     v.visitStackConsumer(this);
/* 88 */     v.visitArithmeticInstruction(this);
/* 89 */     v.visitDDIV(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.DDIV
 * JD-Core Version:    0.6.2
 */