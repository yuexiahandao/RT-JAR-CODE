/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*    */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*    */ import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
/*    */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*    */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeSetType;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeType;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ReferenceType;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ResultTreeType;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*    */ 
/*    */ final class CopyOf extends Instruction
/*    */ {
/*    */   private Expression _select;
/*    */ 
/*    */   public void display(int indent)
/*    */   {
/* 50 */     indent(indent);
/* 51 */     Util.println("CopyOf");
/* 52 */     indent(indent + 4);
/* 53 */     Util.println("select " + this._select.toString());
/*    */   }
/*    */ 
/*    */   public void parseContents(Parser parser) {
/* 57 */     this._select = parser.parseExpression(this, "select", null);
/*    */ 
/* 59 */     if (this._select.isDummy()) {
/* 60 */       reportError(this, parser, "REQUIRED_ATTR_ERR", "select");
/* 61 */       return;
/*    */     }
/*    */   }
/*    */ 
/*    */   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
/* 66 */     Type tselect = this._select.typeCheck(stable);
/* 67 */     if ((!(tselect instanceof NodeType)) && (!(tselect instanceof NodeSetType)) && (!(tselect instanceof ReferenceType)) && (!(tselect instanceof ResultTreeType)))
/*    */     {
/* 74 */       this._select = new CastExpr(this._select, Type.String);
/*    */     }
/* 76 */     return Type.Void;
/*    */   }
/*    */ 
/*    */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/* 80 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 81 */     InstructionList il = methodGen.getInstructionList();
/* 82 */     Type tselect = this._select.getType();
/*    */ 
/* 84 */     String CPY1_SIG = "(Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;)V";
/* 85 */     int cpy1 = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "copy", "(Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;)V");
/*    */ 
/* 87 */     String CPY2_SIG = "(ILcom/sun/org/apache/xml/internal/serializer/SerializationHandler;)V";
/* 88 */     int cpy2 = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "copy", "(ILcom/sun/org/apache/xml/internal/serializer/SerializationHandler;)V");
/*    */ 
/* 90 */     String getDoc_SIG = "()I";
/* 91 */     int getDoc = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getDocument", "()I");
/*    */ 
/* 94 */     if ((tselect instanceof NodeSetType)) {
/* 95 */       il.append(methodGen.loadDOM());
/*    */ 
/* 98 */       this._select.translate(classGen, methodGen);
/* 99 */       this._select.startIterator(classGen, methodGen);
/*    */ 
/* 102 */       il.append(methodGen.loadHandler());
/* 103 */       il.append(new INVOKEINTERFACE(cpy1, 3));
/*    */     }
/* 105 */     else if ((tselect instanceof NodeType)) {
/* 106 */       il.append(methodGen.loadDOM());
/* 107 */       this._select.translate(classGen, methodGen);
/* 108 */       il.append(methodGen.loadHandler());
/* 109 */       il.append(new INVOKEINTERFACE(cpy2, 3));
/*    */     }
/* 111 */     else if ((tselect instanceof ResultTreeType)) {
/* 112 */       this._select.translate(classGen, methodGen);
/*    */ 
/* 114 */       il.append(DUP);
/* 115 */       il.append(new INVOKEINTERFACE(getDoc, 1));
/* 116 */       il.append(methodGen.loadHandler());
/* 117 */       il.append(new INVOKEINTERFACE(cpy2, 3));
/*    */     }
/* 119 */     else if ((tselect instanceof ReferenceType)) {
/* 120 */       this._select.translate(classGen, methodGen);
/* 121 */       il.append(methodGen.loadHandler());
/* 122 */       il.append(methodGen.loadCurrentNode());
/* 123 */       il.append(methodGen.loadDOM());
/* 124 */       int copy = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "copy", "(Ljava/lang/Object;Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;ILcom/sun/org/apache/xalan/internal/xsltc/DOM;)V");
/*    */ 
/* 131 */       il.append(new INVOKESTATIC(copy));
/*    */     }
/*    */     else {
/* 134 */       il.append(classGen.loadTranslet());
/* 135 */       this._select.translate(classGen, methodGen);
/* 136 */       il.append(methodGen.loadHandler());
/* 137 */       il.append(new INVOKEVIRTUAL(cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "characters", "(Ljava/lang/String;Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;)V")));
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.CopyOf
 * JD-Core Version:    0.6.2
 */