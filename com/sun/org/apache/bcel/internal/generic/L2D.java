/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class L2D extends ConversionInstruction
/*    */ {
/*    */   public L2D()
/*    */   {
/* 69 */     super((short)138);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 82 */     v.visitTypedInstruction(this);
/* 83 */     v.visitStackProducer(this);
/* 84 */     v.visitStackConsumer(this);
/* 85 */     v.visitConversionInstruction(this);
/* 86 */     v.visitL2D(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.L2D
 * JD-Core Version:    0.6.2
 */