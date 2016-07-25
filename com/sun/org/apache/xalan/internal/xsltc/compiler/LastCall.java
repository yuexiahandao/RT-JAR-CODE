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
/*    */ final class LastCall extends FunctionCall
/*    */ {
/*    */   public LastCall(QName fname)
/*    */   {
/* 42 */     super(fname);
/*    */   }
/*    */ 
/*    */   public boolean hasPositionCall() {
/* 46 */     return true;
/*    */   }
/*    */ 
/*    */   public boolean hasLastCall() {
/* 50 */     return true;
/*    */   }
/*    */ 
/*    */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/* 54 */     InstructionList il = methodGen.getInstructionList();
/*    */ 
/* 56 */     if ((methodGen instanceof CompareGenerator)) {
/* 57 */       il.append(((CompareGenerator)methodGen).loadLastNode());
/*    */     }
/* 59 */     else if ((methodGen instanceof TestGenerator)) {
/* 60 */       il.append(new ILOAD(3));
/*    */     }
/*    */     else {
/* 63 */       ConstantPoolGen cpg = classGen.getConstantPool();
/* 64 */       int getLast = cpg.addInterfaceMethodref("com.sun.org.apache.xml.internal.dtm.DTMAxisIterator", "getLast", "()I");
/*    */ 
/* 67 */       il.append(methodGen.loadIterator());
/* 68 */       il.append(new INVOKEINTERFACE(getLast, 1));
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.LastCall
 * JD-Core Version:    0.6.2
 */