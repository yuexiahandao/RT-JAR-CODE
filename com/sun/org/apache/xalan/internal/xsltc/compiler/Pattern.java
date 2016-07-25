/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*    */ 
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*    */ 
/*    */ public abstract class Pattern extends Expression
/*    */ {
/*    */   public abstract Type typeCheck(SymbolTable paramSymbolTable)
/*    */     throws TypeCheckError;
/*    */ 
/*    */   public abstract void translate(ClassGenerator paramClassGenerator, MethodGenerator paramMethodGenerator);
/*    */ 
/*    */   public abstract double getPriority();
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern
 * JD-Core Version:    0.6.2
 */