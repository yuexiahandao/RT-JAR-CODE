/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class IASTORE extends ArrayInstruction
/*    */   implements StackConsumer
/*    */ {
/*    */   public IASTORE()
/*    */   {
/* 72 */     super((short)79);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 85 */     v.visitStackConsumer(this);
/* 86 */     v.visitExceptionThrower(this);
/* 87 */     v.visitTypedInstruction(this);
/* 88 */     v.visitArrayInstruction(this);
/* 89 */     v.visitIASTORE(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.IASTORE
 * JD-Core Version:    0.6.2
 */