/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class LCMP extends Instruction
/*    */   implements TypedInstruction, StackProducer, StackConsumer
/*    */ {
/*    */   public LCMP()
/*    */   {
/* 72 */     super((short)148, (short)1);
/*    */   }
/*    */ 
/*    */   public Type getType(ConstantPoolGen cp)
/*    */   {
/* 78 */     return Type.LONG;
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 90 */     v.visitTypedInstruction(this);
/* 91 */     v.visitStackProducer(this);
/* 92 */     v.visitStackConsumer(this);
/* 93 */     v.visitLCMP(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.LCMP
 * JD-Core Version:    0.6.2
 */