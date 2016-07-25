/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler.util;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.generic.ALOAD;
/*    */ import com.sun.org.apache.bcel.internal.generic.Instruction;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.Stylesheet;
/*    */ 
/*    */ public final class NodeSortRecordGenerator extends ClassGenerator
/*    */ {
/*    */   private static final int TRANSLET_INDEX = 4;
/*    */   private final Instruction _aloadTranslet;
/*    */ 
/*    */   public NodeSortRecordGenerator(String className, String superClassName, String fileName, int accessFlags, String[] interfaces, Stylesheet stylesheet)
/*    */   {
/* 44 */     super(className, superClassName, fileName, accessFlags, interfaces, stylesheet);
/*    */ 
/* 46 */     this._aloadTranslet = new ALOAD(4);
/*    */   }
/*    */ 
/*    */   public Instruction loadTranslet()
/*    */   {
/* 54 */     return this._aloadTranslet;
/*    */   }
/*    */ 
/*    */   public boolean isExternal()
/*    */   {
/* 62 */     return true;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeSortRecordGenerator
 * JD-Core Version:    0.6.2
 */