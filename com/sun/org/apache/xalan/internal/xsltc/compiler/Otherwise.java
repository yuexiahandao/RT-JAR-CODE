/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*    */ 
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*    */ 
/*    */ final class Otherwise extends Instruction
/*    */ {
/*    */   public void display(int indent)
/*    */   {
/* 39 */     indent(indent);
/* 40 */     Util.println("Otherwise");
/* 41 */     indent(indent + 4);
/* 42 */     displayContents(indent + 4);
/*    */   }
/*    */ 
/*    */   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
/* 46 */     typeCheckContents(stable);
/* 47 */     return Type.Void;
/*    */   }
/*    */ 
/*    */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/* 51 */     Parser parser = getParser();
/* 52 */     ErrorMsg err = new ErrorMsg("STRAY_OTHERWISE_ERR", this);
/* 53 */     parser.reportError(3, err);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.Otherwise
 * JD-Core Version:    0.6.2
 */