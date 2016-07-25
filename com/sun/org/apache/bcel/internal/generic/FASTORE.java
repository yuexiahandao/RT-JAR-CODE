/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class FASTORE extends ArrayInstruction
/*    */   implements StackConsumer
/*    */ {
/*    */   public FASTORE()
/*    */   {
/* 71 */     super((short)81);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 84 */     v.visitStackConsumer(this);
/* 85 */     v.visitExceptionThrower(this);
/* 86 */     v.visitTypedInstruction(this);
/* 87 */     v.visitArrayInstruction(this);
/* 88 */     v.visitFASTORE(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.FASTORE
 * JD-Core Version:    0.6.2
 */