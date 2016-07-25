/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class SALOAD extends ArrayInstruction
/*    */   implements StackProducer
/*    */ {
/*    */   public SALOAD()
/*    */   {
/* 69 */     super((short)53);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 82 */     v.visitStackProducer(this);
/* 83 */     v.visitExceptionThrower(this);
/* 84 */     v.visitTypedInstruction(this);
/* 85 */     v.visitArrayInstruction(this);
/* 86 */     v.visitSALOAD(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.SALOAD
 * JD-Core Version:    0.6.2
 */