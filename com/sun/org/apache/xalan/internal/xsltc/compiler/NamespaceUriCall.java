/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*    */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*    */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*    */ import java.util.Vector;
/*    */ 
/*    */ final class NamespaceUriCall extends NameBase
/*    */ {
/*    */   public NamespaceUriCall(QName fname)
/*    */   {
/* 43 */     super(fname);
/*    */   }
/*    */ 
/*    */   public NamespaceUriCall(QName fname, Vector arguments)
/*    */   {
/* 50 */     super(fname, arguments);
/*    */   }
/*    */ 
/*    */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*    */   {
/* 59 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 60 */     InstructionList il = methodGen.getInstructionList();
/*    */ 
/* 63 */     int getNamespace = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getNamespaceName", "(I)Ljava/lang/String;");
/*    */ 
/* 66 */     super.translate(classGen, methodGen);
/* 67 */     il.append(new INVOKEINTERFACE(getNamespace, 2));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.NamespaceUriCall
 * JD-Core Version:    0.6.2
 */