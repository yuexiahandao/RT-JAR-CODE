/*    */ package com.sun.org.apache.bcel.internal.generic;
/*    */ 
/*    */ public abstract class IfInstruction extends BranchInstruction
/*    */   implements StackConsumer
/*    */ {
/*    */   IfInstruction()
/*    */   {
/*    */   }
/*    */ 
/*    */   protected IfInstruction(short opcode, InstructionHandle target)
/*    */   {
/* 77 */     super(opcode, target);
/*    */   }
/*    */ 
/*    */   public abstract IfInstruction negate();
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.IfInstruction
 * JD-Core Version:    0.6.2
 */