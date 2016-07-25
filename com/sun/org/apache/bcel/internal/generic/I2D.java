/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class I2D extends ConversionInstruction
/*    */ {
/*    */   public I2D()
/*    */   {
/* 71 */     super((short)135);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 84 */     v.visitTypedInstruction(this);
/* 85 */     v.visitStackProducer(this);
/* 86 */     v.visitStackConsumer(this);
/* 87 */     v.visitConversionInstruction(this);
/* 88 */     v.visitI2D(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.I2D
 * JD-Core Version:    0.6.2
 */