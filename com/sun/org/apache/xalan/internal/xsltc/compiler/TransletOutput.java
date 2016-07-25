/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.StringType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ 
/*     */ final class TransletOutput extends Instruction
/*     */ {
/*     */   private Expression _filename;
/*     */   private boolean _append;
/*     */ 
/*     */   public void display(int indent)
/*     */   {
/*  51 */     indent(indent);
/*  52 */     Util.println("TransletOutput: " + this._filename);
/*     */   }
/*     */ 
/*     */   public void parseContents(Parser parser)
/*     */   {
/*  61 */     String filename = getAttribute("file");
/*     */ 
/*  65 */     String append = getAttribute("append");
/*     */ 
/*  68 */     if ((filename == null) || (filename.equals(""))) {
/*  69 */       reportError(this, parser, "REQUIRED_ATTR_ERR", "file");
/*     */     }
/*     */ 
/*  73 */     this._filename = AttributeValue.create(this, filename, parser);
/*     */ 
/*  75 */     if ((append != null) && ((append.toLowerCase().equals("yes")) || (append.toLowerCase().equals("true"))))
/*     */     {
/*  77 */       this._append = true;
/*     */     }
/*     */     else {
/*  80 */       this._append = false;
/*     */     }
/*  82 */     parseChildren(parser);
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable)
/*     */     throws TypeCheckError
/*     */   {
/*  89 */     Type type = this._filename.typeCheck(stable);
/*  90 */     if (!(type instanceof StringType)) {
/*  91 */       this._filename = new CastExpr(this._filename, Type.String);
/*     */     }
/*  93 */     typeCheckContents(stable);
/*  94 */     return Type.Void;
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 102 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 103 */     InstructionList il = methodGen.getInstructionList();
/* 104 */     boolean isSecureProcessing = classGen.getParser().getXSLTC().isSecureProcessing();
/*     */ 
/* 107 */     if (isSecureProcessing) {
/* 108 */       int index = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "unallowed_extension_elementF", "(Ljava/lang/String;)V");
/*     */ 
/* 111 */       il.append(new PUSH(cpg, "redirect"));
/* 112 */       il.append(new INVOKESTATIC(index));
/* 113 */       return;
/*     */     }
/*     */ 
/* 117 */     il.append(methodGen.loadHandler());
/*     */ 
/* 119 */     int open = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "openOutputHandler", "(Ljava/lang/String;Z)Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;");
/*     */ 
/* 124 */     int close = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "closeOutputHandler", "(Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;)V");
/*     */ 
/* 129 */     il.append(classGen.loadTranslet());
/* 130 */     this._filename.translate(classGen, methodGen);
/* 131 */     il.append(new PUSH(cpg, this._append));
/* 132 */     il.append(new INVOKEVIRTUAL(open));
/*     */ 
/* 135 */     il.append(methodGen.storeHandler());
/*     */ 
/* 138 */     translateContents(classGen, methodGen);
/*     */ 
/* 141 */     il.append(classGen.loadTranslet());
/* 142 */     il.append(methodGen.loadHandler());
/* 143 */     il.append(new INVOKEVIRTUAL(close));
/*     */ 
/* 146 */     il.append(methodGen.storeHandler());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.TransletOutput
 * JD-Core Version:    0.6.2
 */