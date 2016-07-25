/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class DCMPL extends Instruction
/*    */   implements TypedInstruction, StackProducer, StackConsumer
/*    */ {
/*    */   public DCMPL()
/*    */   {
/* 71 */     super((short)151, (short)1);
/*    */   }
/*    */ 
/*    */   public Type getType(ConstantPoolGen cp)
/*    */   {
/* 77 */     return Type.DOUBLE;
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 90 */     v.visitTypedInstruction(this);
/* 91 */     v.visitStackProducer(this);
/* 92 */     v.visitStackConsumer(this);
/* 93 */     v.visitDCMPL(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.DCMPL
 * JD-Core Version:    0.6.2
 */