/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ 
/*     */ final class ApplyImports extends Instruction
/*     */ {
/*     */   private QName _modeName;
/*     */   private int _precedence;
/*     */ 
/*     */   public void display(int indent)
/*     */   {
/*  45 */     indent(indent);
/*  46 */     Util.println("ApplyTemplates");
/*  47 */     indent(indent + 4);
/*  48 */     if (this._modeName != null) {
/*  49 */       indent(indent + 4);
/*  50 */       Util.println("mode " + this._modeName);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean hasWithParams()
/*     */   {
/*  58 */     return hasContents();
/*     */   }
/*     */ 
/*     */   private int getMinPrecedence(int max)
/*     */   {
/*  70 */     Stylesheet includeRoot = getStylesheet();
/*  71 */     while (includeRoot._includedFrom != null) {
/*  72 */       includeRoot = includeRoot._includedFrom;
/*     */     }
/*     */ 
/*  75 */     return includeRoot.getMinimumDescendantPrecedence();
/*     */   }
/*     */ 
/*     */   public void parseContents(Parser parser)
/*     */   {
/*  84 */     Stylesheet stylesheet = getStylesheet();
/*  85 */     stylesheet.setTemplateInlining(false);
/*     */ 
/*  88 */     Template template = getTemplate();
/*  89 */     this._modeName = template.getModeName();
/*  90 */     this._precedence = template.getImportPrecedence();
/*     */ 
/*  93 */     stylesheet = parser.getTopLevelStylesheet();
/*     */ 
/*  95 */     parseChildren(parser);
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable)
/*     */     throws TypeCheckError
/*     */   {
/* 102 */     typeCheckContents(stable);
/* 103 */     return Type.Void;
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 111 */     Stylesheet stylesheet = classGen.getStylesheet();
/* 112 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 113 */     InstructionList il = methodGen.getInstructionList();
/* 114 */     int current = methodGen.getLocalIndex("current");
/*     */ 
/* 117 */     il.append(classGen.loadTranslet());
/* 118 */     il.append(methodGen.loadDOM());
/* 119 */     il.append(methodGen.loadIterator());
/* 120 */     il.append(methodGen.loadHandler());
/* 121 */     il.append(methodGen.loadCurrentNode());
/*     */ 
/* 125 */     if (stylesheet.hasLocalParams()) {
/* 126 */       il.append(classGen.loadTranslet());
/* 127 */       int pushFrame = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "pushParamFrame", "()V");
/*     */ 
/* 130 */       il.append(new INVOKEVIRTUAL(pushFrame));
/*     */     }
/*     */ 
/* 135 */     int maxPrecedence = this._precedence;
/* 136 */     int minPrecedence = getMinPrecedence(maxPrecedence);
/* 137 */     Mode mode = stylesheet.getMode(this._modeName);
/*     */ 
/* 141 */     String functionName = mode.functionName(minPrecedence, maxPrecedence);
/*     */ 
/* 144 */     String className = classGen.getStylesheet().getClassName();
/* 145 */     String signature = classGen.getApplyTemplatesSigForImport();
/* 146 */     int applyTemplates = cpg.addMethodref(className, functionName, signature);
/*     */ 
/* 149 */     il.append(new INVOKEVIRTUAL(applyTemplates));
/*     */ 
/* 152 */     if (stylesheet.hasLocalParams()) {
/* 153 */       il.append(classGen.loadTranslet());
/* 154 */       int pushFrame = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "popParamFrame", "()V");
/*     */ 
/* 157 */       il.append(new INVOKEVIRTUAL(pushFrame));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.ApplyImports
 * JD-Core Version:    0.6.2
 */