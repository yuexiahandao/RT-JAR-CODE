/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*    */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*    */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*    */ 
/*    */ final class RealExpr extends Expression
/*    */ {
/*    */   private double _value;
/*    */ 
/*    */   public RealExpr(double value)
/*    */   {
/* 42 */     this._value = value;
/*    */   }
/*    */ 
/*    */   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
/* 46 */     return this._type = Type.Real;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 50 */     return "real-expr(" + this._value + ')';
/*    */   }
/*    */ 
/*    */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/* 54 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 55 */     InstructionList il = methodGen.getInstructionList();
/* 56 */     il.append(new PUSH(cpg, this._value));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.RealExpr
 * JD-Core Version:    0.6.2
 */