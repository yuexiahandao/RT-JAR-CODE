/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.NEW;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ 
/*     */ final class Message extends Instruction
/*     */ {
/*  44 */   private boolean _terminate = false;
/*     */ 
/*     */   public void parseContents(Parser parser) {
/*  47 */     String termstr = getAttribute("terminate");
/*  48 */     if (termstr != null) {
/*  49 */       this._terminate = termstr.equals("yes");
/*     */     }
/*  51 */     parseChildren(parser);
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
/*  55 */     typeCheckContents(stable);
/*  56 */     return Type.Void;
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/*  60 */     ConstantPoolGen cpg = classGen.getConstantPool();
/*  61 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/*  64 */     il.append(classGen.loadTranslet());
/*     */ 
/*  66 */     switch (elementCount()) {
/*     */     case 0:
/*  68 */       il.append(new PUSH(cpg, ""));
/*  69 */       break;
/*     */     case 1:
/*  71 */       SyntaxTreeNode child = (SyntaxTreeNode)elementAt(0);
/*  72 */       if ((child instanceof Text))
/*  73 */         il.append(new PUSH(cpg, ((Text)child).getText()));
/*  74 */       break;
/*     */     }
/*     */ 
/*  79 */     il.append(methodGen.loadHandler());
/*     */ 
/*  82 */     il.append(new NEW(cpg.addClass("com.sun.org.apache.xml.internal.serializer.ToXMLStream")));
/*  83 */     il.append(methodGen.storeHandler());
/*     */ 
/*  86 */     il.append(new NEW(cpg.addClass("java.io.StringWriter")));
/*  87 */     il.append(DUP);
/*  88 */     il.append(DUP);
/*  89 */     il.append(new INVOKESPECIAL(cpg.addMethodref("java.io.StringWriter", "<init>", "()V")));
/*     */ 
/*  93 */     il.append(methodGen.loadHandler());
/*  94 */     il.append(new INVOKESPECIAL(cpg.addMethodref("com.sun.org.apache.xml.internal.serializer.ToXMLStream", "<init>", "()V")));
/*     */ 
/*  99 */     il.append(methodGen.loadHandler());
/* 100 */     il.append(SWAP);
/* 101 */     il.append(new INVOKEINTERFACE(cpg.addInterfaceMethodref("com.sun.org.apache.xml.internal.serializer.SerializationHandler", "setWriter", "(Ljava/io/Writer;)V"), 2));
/*     */ 
/* 107 */     il.append(methodGen.loadHandler());
/* 108 */     il.append(new PUSH(cpg, "UTF-8"));
/* 109 */     il.append(new INVOKEINTERFACE(cpg.addInterfaceMethodref("com.sun.org.apache.xml.internal.serializer.SerializationHandler", "setEncoding", "(Ljava/lang/String;)V"), 2));
/*     */ 
/* 115 */     il.append(methodGen.loadHandler());
/* 116 */     il.append(ICONST_1);
/* 117 */     il.append(new INVOKEINTERFACE(cpg.addInterfaceMethodref("com.sun.org.apache.xml.internal.serializer.SerializationHandler", "setOmitXMLDeclaration", "(Z)V"), 2));
/*     */ 
/* 122 */     il.append(methodGen.loadHandler());
/* 123 */     il.append(new INVOKEINTERFACE(cpg.addInterfaceMethodref("com.sun.org.apache.xml.internal.serializer.SerializationHandler", "startDocument", "()V"), 1));
/*     */ 
/* 129 */     translateContents(classGen, methodGen);
/*     */ 
/* 131 */     il.append(methodGen.loadHandler());
/* 132 */     il.append(new INVOKEINTERFACE(cpg.addInterfaceMethodref("com.sun.org.apache.xml.internal.serializer.SerializationHandler", "endDocument", "()V"), 1));
/*     */ 
/* 138 */     il.append(new INVOKEVIRTUAL(cpg.addMethodref("java.io.StringWriter", "toString", "()Ljava/lang/String;")));
/*     */ 
/* 143 */     il.append(SWAP);
/* 144 */     il.append(methodGen.storeHandler());
/*     */ 
/* 149 */     il.append(new INVOKEVIRTUAL(cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "displayMessage", "(Ljava/lang/String;)V")));
/*     */ 
/* 155 */     if (this._terminate == true)
/*     */     {
/* 157 */       int einit = cpg.addMethodref("java.lang.RuntimeException", "<init>", "(Ljava/lang/String;)V");
/*     */ 
/* 160 */       il.append(new NEW(cpg.addClass("java.lang.RuntimeException")));
/* 161 */       il.append(DUP);
/* 162 */       il.append(new PUSH(cpg, "Termination forced by an xsl:message instruction"));
/*     */ 
/* 164 */       il.append(new INVOKESPECIAL(einit));
/* 165 */       il.append(ATHROW);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.Message
 * JD-Core Version:    0.6.2
 */