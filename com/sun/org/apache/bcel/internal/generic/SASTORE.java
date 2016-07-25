/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class SASTORE extends ArrayInstruction
/*    */   implements StackConsumer
/*    */ {
/*    */   public SASTORE()
/*    */   {
/* 69 */     super((short)86);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 82 */     v.visitStackConsumer(this);
/* 83 */     v.visitExceptionThrower(this);
/* 84 */     v.visitTypedInstruction(this);
/* 85 */     v.visitArrayInstruction(this);
/* 86 */     v.visitSASTORE(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.SASTORE
 * JD-Core Version:    0.6.2
 */