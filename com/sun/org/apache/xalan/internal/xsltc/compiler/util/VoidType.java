/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler.util;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.generic.Instruction;
/*    */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*    */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.Parser;
/*    */ 
/*    */ public final class VoidType extends Type
/*    */ {
/*    */   public String toString()
/*    */   {
/* 39 */     return "void";
/*    */   }
/*    */ 
/*    */   public boolean identicalTo(Type other) {
/* 43 */     return this == other;
/*    */   }
/*    */ 
/*    */   public String toSignature() {
/* 47 */     return "V";
/*    */   }
/*    */ 
/*    */   public com.sun.org.apache.bcel.internal.generic.Type toJCType() {
/* 51 */     return null;
/*    */   }
/*    */ 
/*    */   public Instruction POP() {
/* 55 */     return NOP;
/*    */   }
/*    */ 
/*    */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Type type)
/*    */   {
/* 67 */     if (type == Type.String) {
/* 68 */       translateTo(classGen, methodGen, (StringType)type);
/*    */     }
/*    */     else {
/* 71 */       ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), type.toString());
/*    */ 
/* 73 */       classGen.getParser().reportError(2, err);
/*    */     }
/*    */   }
/*    */ 
/*    */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, StringType type)
/*    */   {
/* 84 */     InstructionList il = methodGen.getInstructionList();
/* 85 */     il.append(new PUSH(classGen.getConstantPool(), ""));
/*    */   }
/*    */ 
/*    */   public void translateFrom(ClassGenerator classGen, MethodGenerator methodGen, Class clazz)
/*    */   {
/* 94 */     if (!clazz.getName().equals("void")) {
/* 95 */       ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), clazz.getName());
/*    */ 
/* 97 */       classGen.getParser().reportError(2, err);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.util.VoidType
 * JD-Core Version:    0.6.2
 */