/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class IRETURN extends ReturnInstruction
/*    */ {
/*    */   public IRETURN()
/*    */   {
/* 71 */     super((short)172);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 84 */     v.visitExceptionThrower(this);
/* 85 */     v.visitTypedInstruction(this);
/* 86 */     v.visitStackConsumer(this);
/* 87 */     v.visitReturnInstruction(this);
/* 88 */     v.visitIRETURN(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.IRETURN
 * JD-Core Version:    0.6.2
 */