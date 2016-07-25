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
/*    */ final class SimpleAttributeValue extends AttributeValue
/*    */ {
/*    */   private String _value;
/*    */ 
/*    */   public SimpleAttributeValue(String value)
/*    */   {
/* 47 */     this._value = value;
/*    */   }
/*    */ 
/*    */   public Type typeCheck(SymbolTable stable)
/*    */     throws TypeCheckError
/*    */   {
/* 55 */     return this._type = Type.String;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 59 */     return this._value;
/*    */   }
/*    */ 
/*    */   protected boolean contextDependent() {
/* 63 */     return false;
/*    */   }
/*    */ 
/*    */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*    */   {
/* 73 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 74 */     InstructionList il = methodGen.getInstructionList();
/* 75 */     il.append(new PUSH(cpg, this._value));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.SimpleAttributeValue
 * JD-Core Version:    0.6.2
 */