/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*    */ 
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*    */ 
/*    */ abstract class Instruction extends SyntaxTreeNode
/*    */ {
/*    */   public Type typeCheck(SymbolTable stable)
/*    */     throws TypeCheckError
/*    */   {
/* 41 */     return typeCheckContents(stable);
/*    */   }
/*    */ 
/*    */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*    */   {
/* 48 */     ErrorMsg msg = new ErrorMsg("NOT_IMPLEMENTED_ERR", getClass(), this);
/*    */ 
/* 50 */     getParser().reportError(2, msg);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction
 * JD-Core Version:    0.6.2
 */