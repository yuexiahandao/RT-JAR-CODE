/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class CALOAD extends ArrayInstruction
/*    */   implements StackProducer
/*    */ {
/*    */   public CALOAD()
/*    */   {
/* 71 */     super((short)52);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 84 */     v.visitStackProducer(this);
/* 85 */     v.visitExceptionThrower(this);
/* 86 */     v.visitTypedInstruction(this);
/* 87 */     v.visitArrayInstruction(this);
/* 88 */     v.visitCALOAD(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.CALOAD
 * JD-Core Version:    0.6.2
 */