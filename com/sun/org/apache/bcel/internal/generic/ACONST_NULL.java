/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class ACONST_NULL extends Instruction
/*    */   implements PushInstruction, TypedInstruction
/*    */ {
/*    */   public ACONST_NULL()
/*    */   {
/* 73 */     super((short)1, (short)1);
/*    */   }
/*    */ 
/*    */   public Type getType(ConstantPoolGen cp)
/*    */   {
/* 79 */     return Type.NULL;
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 92 */     v.visitStackProducer(this);
/* 93 */     v.visitPushInstruction(this);
/* 94 */     v.visitTypedInstruction(this);
/* 95 */     v.visitACONST_NULL(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.ACONST_NULL
 * JD-Core Version:    0.6.2
 */