/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class NOP extends Instruction
/*    */ {
/*    */   public NOP()
/*    */   {
/* 68 */     super((short)0, (short)1);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 81 */     v.visitNOP(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.NOP
 * JD-Core Version:    0.6.2
 */