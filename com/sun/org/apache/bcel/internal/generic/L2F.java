/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class L2F extends ConversionInstruction
/*    */ {
/*    */   public L2F()
/*    */   {
/* 69 */     super((short)137);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 82 */     v.visitTypedInstruction(this);
/* 83 */     v.visitStackProducer(this);
/* 84 */     v.visitStackConsumer(this);
/* 85 */     v.visitConversionInstruction(this);
/* 86 */     v.visitL2F(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.L2F
 * JD-Core Version:    0.6.2
 */