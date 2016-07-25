/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class FCMPG extends Instruction
/*    */   implements TypedInstruction, StackProducer, StackConsumer
/*    */ {
/*    */   public FCMPG()
/*    */   {
/* 70 */     super((short)150, (short)1);
/*    */   }
/*    */ 
/*    */   public Type getType(ConstantPoolGen cp)
/*    */   {
/* 76 */     return Type.FLOAT;
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 89 */     v.visitTypedInstruction(this);
/* 90 */     v.visitStackProducer(this);
/* 91 */     v.visitStackConsumer(this);
/* 92 */     v.visitFCMPG(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.FCMPG
 * JD-Core Version:    0.6.2
 */