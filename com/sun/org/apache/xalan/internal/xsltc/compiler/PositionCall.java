/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*    */ import com.sun.org.apache.bcel.internal.generic.ILOAD;
/*    */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*    */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.CompareGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TestGenerator;
/*    */ 
/*    */ final class PositionCall extends FunctionCall
/*    */ {
/*    */   public PositionCall(QName fname)
/*    */   {
/* 43 */     super(fname);
/*    */   }
/*    */ 
/*    */   public boolean hasPositionCall() {
/* 47 */     return true;
/*    */   }
/*    */ 
/*    */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/* 51 */     InstructionList il = methodGen.getInstructionList();
/*    */ 
/* 53 */     if ((methodGen instanceof CompareGenerator)) {
/* 54 */       il.append(((CompareGenerator)methodGen).loadCurrentNode());
/*    */     }
/* 56 */     else if ((methodGen instanceof TestGenerator)) {
/* 57 */       il.append(new ILOAD(2));
/*    */     }
/*    */     else {
/* 60 */       ConstantPoolGen cpg = classGen.getConstantPool();
/* 61 */       int index = cpg.addInterfaceMethodref("com.sun.org.apache.xml.internal.dtm.DTMAxisIterator", "getPosition", "()I");
/*    */ 
/* 65 */       il.append(methodGen.loadIterator());
/* 66 */       il.append(new INVOKEINTERFACE(index, 1));
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.PositionCall
 * JD-Core Version:    0.6.2
 */