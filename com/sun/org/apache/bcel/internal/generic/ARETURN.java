/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class ARETURN extends ReturnInstruction
/*    */ {
/*    */   public ARETURN()
/*    */   {
/* 72 */     super((short)176);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 85 */     v.visitExceptionThrower(this);
/* 86 */     v.visitTypedInstruction(this);
/* 87 */     v.visitStackConsumer(this);
/* 88 */     v.visitReturnInstruction(this);
/* 89 */     v.visitARETURN(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.ARETURN
 * JD-Core Version:    0.6.2
 */