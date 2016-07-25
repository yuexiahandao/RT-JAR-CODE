/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class DALOAD extends ArrayInstruction
/*    */   implements StackProducer
/*    */ {
/*    */   public DALOAD()
/*    */   {
/* 71 */     super((short)49);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 84 */     v.visitStackProducer(this);
/* 85 */     v.visitExceptionThrower(this);
/* 86 */     v.visitTypedInstruction(this);
/* 87 */     v.visitArrayInstruction(this);
/* 88 */     v.visitDALOAD(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.DALOAD
 * JD-Core Version:    0.6.2
 */