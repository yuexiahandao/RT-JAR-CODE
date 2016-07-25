/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class D2L extends ConversionInstruction
/*    */ {
/*    */   public D2L()
/*    */   {
/* 71 */     super((short)143);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 84 */     v.visitTypedInstruction(this);
/* 85 */     v.visitStackProducer(this);
/* 86 */     v.visitStackConsumer(this);
/* 87 */     v.visitConversionInstruction(this);
/* 88 */     v.visitD2L(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.D2L
 * JD-Core Version:    0.6.2
 */