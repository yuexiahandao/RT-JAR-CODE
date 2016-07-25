/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class IF_ACMPEQ extends IfInstruction
/*    */ {
/*    */   IF_ACMPEQ()
/*    */   {
/*    */   }
/*    */ 
/*    */   public IF_ACMPEQ(InstructionHandle target)
/*    */   {
/* 76 */     super((short)165, target);
/*    */   }
/*    */ 
/*    */   public IfInstruction negate()
/*    */   {
/* 83 */     return new IF_ACMPNE(this.target);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 95 */     v.visitStackConsumer(this);
/* 96 */     v.visitBranchInstruction(this);
/* 97 */     v.visitIfInstruction(this);
/* 98 */     v.visitIF_ACMPEQ(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ
 * JD-Core Version:    0.6.2
 */