/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler.util;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*    */ import com.sun.org.apache.bcel.internal.generic.Instruction;
/*    */ import com.sun.org.apache.bcel.internal.generic.Visitor;
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ abstract class MarkerInstruction extends Instruction
/*    */ {
/*    */   public MarkerInstruction()
/*    */   {
/* 47 */     super((short)-1, (short)0);
/*    */   }
/*    */ 
/*    */   public void accept(Visitor v)
/*    */   {
/*    */   }
/*    */ 
/*    */   public final int consumeStack(ConstantPoolGen cpg)
/*    */   {
/* 67 */     return 0;
/*    */   }
/*    */ 
/*    */   public final int produceStack(ConstantPoolGen cpg)
/*    */   {
/* 78 */     return 0;
/*    */   }
/*    */ 
/*    */   public Instruction copy()
/*    */   {
/* 88 */     return this;
/*    */   }
/*    */ 
/*    */   public final void dump(DataOutputStream out)
/*    */     throws IOException
/*    */   {
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.util.MarkerInstruction
 * JD-Core Version:    0.6.2
 */