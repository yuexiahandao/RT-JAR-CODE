/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class RETURN extends ReturnInstruction
/*    */ {
/*    */   public RETURN()
/*    */   {
/* 69 */     super((short)177);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 82 */     v.visitExceptionThrower(this);
/* 83 */     v.visitTypedInstruction(this);
/* 84 */     v.visitStackConsumer(this);
/* 85 */     v.visitReturnInstruction(this);
/* 86 */     v.visitRETURN(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.RETURN
 * JD-Core Version:    0.6.2
 */