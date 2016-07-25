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
/*    */ final class LiteralExpr extends Expression
/*    */ {
/*    */   private final String _value;
/*    */   private final String _namespace;
/*    */ 
/*    */   public LiteralExpr(String value)
/*    */   {
/* 48 */     this._value = value;
/* 49 */     this._namespace = null;
/*    */   }
/*    */ 
/*    */   public LiteralExpr(String value, String namespace)
/*    */   {
/* 58 */     this._value = value;
/* 59 */     this._namespace = (namespace.equals("") ? null : namespace);
/*    */   }
/*    */ 
/*    */   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
/* 63 */     return this._type = Type.String;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 67 */     return "literal-expr(" + this._value + ')';
/*    */   }
/*    */ 
/*    */   protected boolean contextDependent() {
/* 71 */     return false;
/*    */   }
/*    */ 
/*    */   protected String getValue() {
/* 75 */     return this._value;
/*    */   }
/*    */ 
/*    */   protected String getNamespace() {
/* 79 */     return this._namespace;
/*    */   }
/*    */ 
/*    */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/* 83 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 84 */     InstructionList il = methodGen.getInstructionList();
/* 85 */     il.append(new PUSH(cpg, this._value));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.LiteralExpr
 * JD-Core Version:    0.6.2
 */