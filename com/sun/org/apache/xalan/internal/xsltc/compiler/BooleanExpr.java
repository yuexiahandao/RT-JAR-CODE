/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*    */ import com.sun.org.apache.bcel.internal.generic.GOTO;
/*    */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*    */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*    */ 
/*    */ final class BooleanExpr extends Expression
/*    */ {
/*    */   private boolean _value;
/*    */ 
/*    */   public BooleanExpr(boolean value)
/*    */   {
/* 45 */     this._value = value;
/*    */   }
/*    */ 
/*    */   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
/* 49 */     this._type = Type.Boolean;
/* 50 */     return this._type;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 54 */     return this._value ? "true()" : "false()";
/*    */   }
/*    */ 
/*    */   public boolean getValue() {
/* 58 */     return this._value;
/*    */   }
/*    */ 
/*    */   public boolean contextDependent() {
/* 62 */     return false;
/*    */   }
/*    */ 
/*    */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/* 66 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 67 */     InstructionList il = methodGen.getInstructionList();
/* 68 */     il.append(new PUSH(cpg, this._value));
/*    */   }
/*    */ 
/*    */   public void translateDesynthesized(ClassGenerator classGen, MethodGenerator methodGen)
/*    */   {
/* 73 */     InstructionList il = methodGen.getInstructionList();
/* 74 */     if (this._value) {
/* 75 */       il.append(NOP);
/*    */     }
/*    */     else
/* 78 */       this._falseList.add(il.append(new GOTO(null)));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.BooleanExpr
 * JD-Core Version:    0.6.2
 */