/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class D2I extends ConversionInstruction
/*    */ {
/*    */   public D2I()
/*    */   {
/* 71 */     super((short)142);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 84 */     v.visitTypedInstruction(this);
/* 85 */     v.visitStackProducer(this);
/* 86 */     v.visitStackConsumer(this);
/* 87 */     v.visitConversionInstruction(this);
/* 88 */     v.visitD2I(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.D2I
 * JD-Core Version:    0.6.2
 */