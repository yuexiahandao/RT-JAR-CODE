/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class IFLT extends IfInstruction
/*    */ {
/*    */   IFLT()
/*    */   {
/*    */   }
/*    */ 
/*    */   public IFLT(InstructionHandle target)
/*    */   {
/* 76 */     super((short)155, target);
/*    */   }
/*    */ 
/*    */   public IfInstruction negate()
/*    */   {
/* 83 */     return new IFGE(this.target);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 96 */     v.visitStackConsumer(this);
/* 97 */     v.visitBranchInstruction(this);
/* 98 */     v.visitIfInstruction(this);
/* 99 */     v.visitIFLT(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.IFLT
 * JD-Core Version:    0.6.2
 */