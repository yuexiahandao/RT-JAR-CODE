/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*    */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*    */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*    */ import java.util.Vector;
/*    */ 
/*    */ final class NameCall extends NameBase
/*    */ {
/*    */   public NameCall(QName fname)
/*    */   {
/* 45 */     super(fname);
/*    */   }
/*    */ 
/*    */   public NameCall(QName fname, Vector arguments)
/*    */   {
/* 52 */     super(fname, arguments);
/*    */   }
/*    */ 
/*    */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*    */   {
/* 59 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 60 */     InstructionList il = methodGen.getInstructionList();
/*    */ 
/* 62 */     int getName = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getNodeNameX", "(I)Ljava/lang/String;");
/*    */ 
/* 65 */     super.translate(classGen, methodGen);
/* 66 */     il.append(new INVOKEINTERFACE(getName, 2));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.NameCall
 * JD-Core Version:    0.6.2
 */