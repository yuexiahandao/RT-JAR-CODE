/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ALOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.ASTORE;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.GETFIELD;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ import com.sun.org.apache.xml.internal.utils.XML11Char;
/*     */ 
/*     */ final class ProcessingInstruction extends Instruction
/*     */ {
/*     */   private AttributeValue _name;
/*  50 */   private boolean _isLiteral = false;
/*     */ 
/*     */   public void parseContents(Parser parser) {
/*  53 */     String name = getAttribute("name");
/*     */ 
/*  55 */     if (name.length() > 0) {
/*  56 */       this._isLiteral = Util.isLiteral(name);
/*  57 */       if ((this._isLiteral) && 
/*  58 */         (!XML11Char.isXML11ValidNCName(name))) {
/*  59 */         ErrorMsg err = new ErrorMsg("INVALID_NCNAME_ERR", name, this);
/*  60 */         parser.reportError(3, err);
/*     */       }
/*     */ 
/*  63 */       this._name = AttributeValue.create(this, name, parser);
/*     */     }
/*     */     else {
/*  66 */       reportError(this, parser, "REQUIRED_ATTR_ERR", "name");
/*     */     }
/*  68 */     if (name.equals("xml")) {
/*  69 */       reportError(this, parser, "ILLEGAL_PI_ERR", "xml");
/*     */     }
/*  71 */     parseChildren(parser);
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
/*  75 */     this._name.typeCheck(stable);
/*  76 */     typeCheckContents(stable);
/*  77 */     return Type.Void;
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/*  81 */     ConstantPoolGen cpg = classGen.getConstantPool();
/*  82 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/*  84 */     if (!this._isLiteral)
/*     */     {
/*  86 */       LocalVariableGen nameValue = methodGen.addLocalVariable2("nameValue", Util.getJCRefType("Ljava/lang/String;"), null);
/*     */ 
/*  92 */       this._name.translate(classGen, methodGen);
/*  93 */       nameValue.setStart(il.append(new ASTORE(nameValue.getIndex())));
/*  94 */       il.append(new ALOAD(nameValue.getIndex()));
/*     */ 
/*  97 */       int check = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "checkNCName", "(Ljava/lang/String;)V");
/*     */ 
/* 101 */       il.append(new INVOKESTATIC(check));
/*     */ 
/* 104 */       il.append(methodGen.loadHandler());
/* 105 */       il.append(DUP);
/*     */ 
/* 108 */       nameValue.setEnd(il.append(new ALOAD(nameValue.getIndex())));
/*     */     }
/*     */     else {
/* 111 */       il.append(methodGen.loadHandler());
/* 112 */       il.append(DUP);
/*     */ 
/* 115 */       this._name.translate(classGen, methodGen);
/*     */     }
/*     */ 
/* 119 */     il.append(classGen.loadTranslet());
/* 120 */     il.append(new GETFIELD(cpg.addFieldref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "stringValueHandler", "Lcom/sun/org/apache/xalan/internal/xsltc/runtime/StringValueHandler;")));
/*     */ 
/* 123 */     il.append(DUP);
/* 124 */     il.append(methodGen.storeHandler());
/*     */ 
/* 127 */     translateContents(classGen, methodGen);
/*     */ 
/* 130 */     il.append(new INVOKEVIRTUAL(cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.StringValueHandler", "getValueOfPI", "()Ljava/lang/String;")));
/*     */ 
/* 134 */     int processingInstruction = cpg.addInterfaceMethodref("com.sun.org.apache.xml.internal.serializer.SerializationHandler", "processingInstruction", "(Ljava/lang/String;Ljava/lang/String;)V");
/*     */ 
/* 138 */     il.append(new INVOKEINTERFACE(processingInstruction, 3));
/*     */ 
/* 140 */     il.append(methodGen.storeHandler());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.ProcessingInstruction
 * JD-Core Version:    0.6.2
 */