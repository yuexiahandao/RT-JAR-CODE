/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*    */ import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
/*    */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*    */ import java.util.Vector;
/*    */ 
/*    */ final class GenerateIdCall extends FunctionCall
/*    */ {
/*    */   public GenerateIdCall(QName fname, Vector arguments)
/*    */   {
/* 40 */     super(fname, arguments);
/*    */   }
/*    */ 
/*    */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/* 44 */     InstructionList il = methodGen.getInstructionList();
/* 45 */     if (argumentCount() == 0) {
/* 46 */       il.append(methodGen.loadContextNode());
/*    */     }
/*    */     else {
/* 49 */       argument().translate(classGen, methodGen);
/*    */     }
/* 51 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 52 */     il.append(new INVOKESTATIC(cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "generate_idF", "(I)Ljava/lang/String;")));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.GenerateIdCall
 * JD-Core Version:    0.6.2
 */