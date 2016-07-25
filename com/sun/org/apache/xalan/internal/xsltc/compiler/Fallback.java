/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*    */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*    */ 
/*    */ final class Fallback extends Instruction
/*    */ {
/* 38 */   private boolean _active = false;
/*    */ 
/*    */   public Type typeCheck(SymbolTable stable)
/*    */     throws TypeCheckError
/*    */   {
/* 44 */     if (this._active) {
/* 45 */       return typeCheckContents(stable);
/*    */     }
/*    */ 
/* 48 */     return Type.Void;
/*    */   }
/*    */ 
/*    */   public void activate()
/*    */   {
/* 56 */     this._active = true;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 60 */     return "fallback";
/*    */   }
/*    */ 
/*    */   public void parseContents(Parser parser)
/*    */   {
/* 68 */     if (this._active) parseChildren(parser);
/*    */   }
/*    */ 
/*    */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*    */   {
/* 76 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 77 */     InstructionList il = methodGen.getInstructionList();
/*    */ 
/* 79 */     if (this._active) translateContents(classGen, methodGen);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.Fallback
 * JD-Core Version:    0.6.2
 */