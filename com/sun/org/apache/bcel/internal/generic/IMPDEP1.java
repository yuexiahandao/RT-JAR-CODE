/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class IMPDEP1 extends Instruction
/*    */ {
/*    */   public IMPDEP1()
/*    */   {
/* 68 */     super((short)254, (short)1);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 81 */     v.visitIMPDEP1(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.IMPDEP1
 * JD-Core Version:    0.6.2
 */