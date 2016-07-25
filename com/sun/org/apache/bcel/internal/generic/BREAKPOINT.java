/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class BREAKPOINT extends Instruction
/*    */ {
/*    */   public BREAKPOINT()
/*    */   {
/* 68 */     super((short)202, (short)1);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 80 */     v.visitBREAKPOINT(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.BREAKPOINT
 * JD-Core Version:    0.6.2
 */