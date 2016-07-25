/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public class IF_ICMPEQ extends IfInstruction
/*    */ {
/*    */   IF_ICMPEQ()
/*    */   {
/*    */   }
/*    */ 
/*    */   public IF_ICMPEQ(InstructionHandle target)
/*    */   {
/* 76 */     super((short)159, target);
/*    */   }
/*    */ 
/*    */   public IfInstruction negate()
/*    */   {
/* 83 */     return new IF_ICMPNE(this.target);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/* 96 */     v.visitStackConsumer(this);
/* 97 */     v.visitBranchInstruction(this);
/* 98 */     v.visitIfInstruction(this);
/* 99 */     v.visitIF_ICMPEQ(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.IF_ICMPEQ
 * JD-Core Version:    0.6.2
 */