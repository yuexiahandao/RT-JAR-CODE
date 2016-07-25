/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ 
/*     */ final class ValueOf extends Instruction
/*     */ {
/*     */   private Expression _select;
/*  45 */   private boolean _escaping = true;
/*  46 */   private boolean _isString = false;
/*     */ 
/*     */   public void display(int indent) {
/*  49 */     indent(indent);
/*  50 */     Util.println("ValueOf");
/*  51 */     indent(indent + 4);
/*  52 */     Util.println("select " + this._select.toString());
/*     */   }
/*     */ 
/*     */   public void parseContents(Parser parser) {
/*  56 */     this._select = parser.parseExpression(this, "select", null);
/*     */ 
/*  59 */     if (this._select.isDummy()) {
/*  60 */       reportError(this, parser, "REQUIRED_ATTR_ERR", "select");
/*  61 */       return;
/*     */     }
/*  63 */     String str = getAttribute("disable-output-escaping");
/*  64 */     if ((str != null) && (str.equals("yes"))) this._escaping = false; 
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
/*  68 */     Type type = this._select.typeCheck(stable);
/*     */ 
/*  71 */     if ((type != null) && (!type.identicalTo(Type.Node)))
/*     */     {
/*  81 */       if (type.identicalTo(Type.NodeSet)) {
/*  82 */         this._select = new CastExpr(this._select, Type.Node);
/*     */       } else {
/*  84 */         this._isString = true;
/*  85 */         if (!type.identicalTo(Type.String)) {
/*  86 */           this._select = new CastExpr(this._select, Type.String);
/*     */         }
/*  88 */         this._isString = true;
/*     */       }
/*     */     }
/*  91 */     return Type.Void;
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/*  95 */     ConstantPoolGen cpg = classGen.getConstantPool();
/*  96 */     InstructionList il = methodGen.getInstructionList();
/*  97 */     int setEscaping = cpg.addInterfaceMethodref("com/sun/org/apache/xml/internal/serializer/SerializationHandler", "setEscaping", "(Z)Z");
/*     */ 
/* 101 */     if (!this._escaping) {
/* 102 */       il.append(methodGen.loadHandler());
/* 103 */       il.append(new PUSH(cpg, false));
/* 104 */       il.append(new INVOKEINTERFACE(setEscaping, 2));
/*     */     }
/*     */ 
/* 112 */     if (this._isString) {
/* 113 */       int characters = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "characters", "(Ljava/lang/String;Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;)V");
/*     */ 
/* 117 */       il.append(classGen.loadTranslet());
/* 118 */       this._select.translate(classGen, methodGen);
/* 119 */       il.append(methodGen.loadHandler());
/* 120 */       il.append(new INVOKEVIRTUAL(characters));
/*     */     } else {
/* 122 */       int characters = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "characters", "(ILcom/sun/org/apache/xml/internal/serializer/SerializationHandler;)V");
/*     */ 
/* 126 */       il.append(methodGen.loadDOM());
/* 127 */       this._select.translate(classGen, methodGen);
/* 128 */       il.append(methodGen.loadHandler());
/* 129 */       il.append(new INVOKEINTERFACE(characters, 3));
/*     */     }
/*     */ 
/* 133 */     if (!this._escaping) {
/* 134 */       il.append(methodGen.loadHandler());
/* 135 */       il.append(SWAP);
/* 136 */       il.append(new INVOKEINTERFACE(setEscaping, 2));
/* 137 */       il.append(POP);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.ValueOf
 * JD-Core Version:    0.6.2
 */