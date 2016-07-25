/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class SWAP extends StackInstruction
/*    */   implements StackConsumer, StackProducer
/*    */ {
/*    */   public SWAP()
/*    */   {
/* 69 */     super((short)95);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 82 */     v.visitStackConsumer(this);
/* 83 */     v.visitStackProducer(this);
/* 84 */     v.visitStackInstruction(this);
/* 85 */     v.visitSWAP(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.SWAP
 * JD-Core Version:    0.6.2
 */