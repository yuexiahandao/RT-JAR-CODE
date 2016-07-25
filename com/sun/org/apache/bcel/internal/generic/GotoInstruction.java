/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public abstract class GotoInstruction extends BranchInstruction
/*    */   implements UnconditionalBranch
/*    */ {
/*    */   GotoInstruction(short opcode, InstructionHandle target)
/*    */   {
/* 70 */     super(opcode, target);
/*    */   }
/*    */ 
/*    */   GotoInstruction()
/*    */   {
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.GotoInstruction
 * JD-Core Version:    0.6.2
 */