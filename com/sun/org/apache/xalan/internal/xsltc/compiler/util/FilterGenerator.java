/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler.util;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.generic.ALOAD;
/*    */ import com.sun.org.apache.bcel.internal.generic.Instruction;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.Stylesheet;
/*    */ 
/*    */ public final class FilterGenerator extends ClassGenerator
/*    */ {
/* 38 */   private static int TRANSLET_INDEX = 5;
/*    */   private final Instruction _aloadTranslet;
/*    */ 
/*    */   public FilterGenerator(String className, String superClassName, String fileName, int accessFlags, String[] interfaces, Stylesheet stylesheet)
/*    */   {
/* 45 */     super(className, superClassName, fileName, accessFlags, interfaces, stylesheet);
/*    */ 
/* 48 */     this._aloadTranslet = new ALOAD(TRANSLET_INDEX);
/*    */   }
/*    */ 
/*    */   public final Instruction loadTranslet()
/*    */   {
/* 56 */     return this._aloadTranslet;
/*    */   }
/*    */ 
/*    */   public boolean isExternal()
/*    */   {
/* 64 */     return true;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.util.FilterGenerator
 * JD-Core Version:    0.6.2
 */