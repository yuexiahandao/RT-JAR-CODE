/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*    */ import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
/*    */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*    */ import java.util.Vector;
/*    */ 
/*    */ final class CeilingCall extends FunctionCall
/*    */ {
/*    */   public CeilingCall(QName fname, Vector arguments)
/*    */   {
/* 40 */     super(fname, arguments);
/*    */   }
/*    */ 
/*    */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/* 44 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 45 */     InstructionList il = methodGen.getInstructionList();
/* 46 */     argument(0).translate(classGen, methodGen);
/* 47 */     il.append(new INVOKESTATIC(cpg.addMethodref("java.lang.Math", "ceil", "(D)D")));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.CeilingCall
 * JD-Core Version:    0.6.2
 */