/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler.util;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.generic.Instruction;
/*    */ 
/*    */ class OutlineableChunkEnd extends MarkerInstruction
/*    */ {
/* 36 */   public static final Instruction OUTLINEABLECHUNKEND = new OutlineableChunkEnd();
/*    */ 
/*    */   public String getName()
/*    */   {
/* 52 */     return OutlineableChunkEnd.class.getName();
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 60 */     return getName();
/*    */   }
/*    */ 
/*    */   public String toString(boolean verbose)
/*    */   {
/* 68 */     return getName();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.util.OutlineableChunkEnd
 * JD-Core Version:    0.6.2
 */