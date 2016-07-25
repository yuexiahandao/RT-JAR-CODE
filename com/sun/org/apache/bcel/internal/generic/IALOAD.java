/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class IALOAD extends ArrayInstruction
/*    */   implements StackProducer
/*    */ {
/*    */   public IALOAD()
/*    */   {
/* 72 */     super((short)46);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 85 */     v.visitStackProducer(this);
/* 86 */     v.visitExceptionThrower(this);
/* 87 */     v.visitTypedInstruction(this);
/* 88 */     v.visitArrayInstruction(this);
/* 89 */     v.visitIALOAD(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.IALOAD
 * JD-Core Version:    0.6.2
 */