/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class I2S extends ConversionInstruction
/*    */ {
/*    */   public I2S()
/*    */   {
/* 69 */     super((short)147);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 82 */     v.visitTypedInstruction(this);
/* 83 */     v.visitStackProducer(this);
/* 84 */     v.visitStackConsumer(this);
/* 85 */     v.visitConversionInstruction(this);
/* 86 */     v.visitI2S(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.I2S
 * JD-Core Version:    0.6.2
 */