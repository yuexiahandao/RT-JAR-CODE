/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class IFGE extends IfInstruction
/*    */ {
/*    */   IFGE()
/*    */   {
/*    */   }
/*    */ 
/*    */   public IFGE(InstructionHandle target)
/*    */   {
/* 76 */     super((short)156, target);
/*    */   }
/*    */ 
/*    */   public IfInstruction negate()
/*    */   {
/* 83 */     return new IFLT(this.target);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 96 */     v.visitStackConsumer(this);
/* 97 */     v.visitBranchInstruction(this);
/* 98 */     v.visitIfInstruction(this);
/* 99 */     v.visitIFGE(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.IFGE
 * JD-Core Version:    0.6.2
 */