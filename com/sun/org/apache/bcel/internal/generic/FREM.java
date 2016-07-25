/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class FREM extends ArithmeticInstruction
/*    */ {
/*    */   public FREM()
/*    */   {
/* 71 */     super((short)114);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 84 */     v.visitTypedInstruction(this);
/* 85 */     v.visitStackProducer(this);
/* 86 */     v.visitStackConsumer(this);
/* 87 */     v.visitArithmeticInstruction(this);
/* 88 */     v.visitFREM(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.FREM
 * JD-Core Version:    0.6.2
 */