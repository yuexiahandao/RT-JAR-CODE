/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*    */ import com.sun.org.apache.bcel.internal.generic.GETFIELD;
/*    */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*    */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*    */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*    */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*    */ 
/*    */ final class Comment extends Instruction
/*    */ {
/*    */   public void parseContents(Parser parser)
/*    */   {
/* 45 */     parseChildren(parser);
/*    */   }
/*    */ 
/*    */   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
/* 49 */     typeCheckContents(stable);
/* 50 */     return Type.String;
/*    */   }
/*    */ 
/*    */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/* 54 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 55 */     InstructionList il = methodGen.getInstructionList();
/*    */ 
/* 58 */     Text rawText = null;
/* 59 */     if (elementCount() == 1) {
/* 60 */       Object content = elementAt(0);
/* 61 */       if ((content instanceof Text)) {
/* 62 */         rawText = (Text)content;
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 70 */     if (rawText != null) {
/* 71 */       il.append(methodGen.loadHandler());
/*    */ 
/* 73 */       if (rawText.canLoadAsArrayOffsetLength()) {
/* 74 */         rawText.loadAsArrayOffsetLength(classGen, methodGen);
/* 75 */         int comment = cpg.addInterfaceMethodref("com.sun.org.apache.xml.internal.serializer.SerializationHandler", "comment", "([CII)V");
/*    */ 
/* 79 */         il.append(new INVOKEINTERFACE(comment, 4));
/*    */       } else {
/* 81 */         il.append(new PUSH(cpg, rawText.getText()));
/* 82 */         int comment = cpg.addInterfaceMethodref("com.sun.org.apache.xml.internal.serializer.SerializationHandler", "comment", "(Ljava/lang/String;)V");
/*    */ 
/* 86 */         il.append(new INVOKEINTERFACE(comment, 2));
/*    */       }
/*    */     }
/*    */     else {
/* 90 */       il.append(methodGen.loadHandler());
/* 91 */       il.append(DUP);
/*    */ 
/* 94 */       il.append(classGen.loadTranslet());
/* 95 */       il.append(new GETFIELD(cpg.addFieldref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "stringValueHandler", "Lcom/sun/org/apache/xalan/internal/xsltc/runtime/StringValueHandler;")));
/*    */ 
/* 98 */       il.append(DUP);
/* 99 */       il.append(methodGen.storeHandler());
/*    */ 
/* 102 */       translateContents(classGen, methodGen);
/*    */ 
/* 105 */       il.append(new INVOKEVIRTUAL(cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.StringValueHandler", "getValue", "()Ljava/lang/String;")));
/*    */ 
/* 109 */       int comment = cpg.addInterfaceMethodref("com.sun.org.apache.xml.internal.serializer.SerializationHandler", "comment", "(Ljava/lang/String;)V");
/*    */ 
/* 113 */       il.append(new INVOKEINTERFACE(comment, 2));
/*    */ 
/* 115 */       il.append(methodGen.storeHandler());
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.Comment
 * JD-Core Version:    0.6.2
 */