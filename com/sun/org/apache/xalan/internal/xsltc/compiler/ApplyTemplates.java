/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeSetType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ReferenceType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ResultTreeType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ import com.sun.org.apache.xml.internal.utils.XML11Char;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class ApplyTemplates extends Instruction
/*     */ {
/*     */   private Expression _select;
/*  51 */   private Type _type = null;
/*     */   private QName _modeName;
/*     */   private String _functionName;
/*     */ 
/*     */   public void display(int indent)
/*     */   {
/*  56 */     indent(indent);
/*  57 */     Util.println("ApplyTemplates");
/*  58 */     indent(indent + 4);
/*  59 */     Util.println("select " + this._select.toString());
/*  60 */     if (this._modeName != null) {
/*  61 */       indent(indent + 4);
/*  62 */       Util.println("mode " + this._modeName);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean hasWithParams() {
/*  67 */     return hasContents();
/*     */   }
/*     */ 
/*     */   public void parseContents(Parser parser) {
/*  71 */     String select = getAttribute("select");
/*  72 */     String mode = getAttribute("mode");
/*     */ 
/*  74 */     if (select.length() > 0) {
/*  75 */       this._select = parser.parseExpression(this, "select", null);
/*     */     }
/*     */ 
/*  79 */     if (mode.length() > 0) {
/*  80 */       if (!XML11Char.isXML11ValidQName(mode)) {
/*  81 */         ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", mode, this);
/*  82 */         parser.reportError(3, err);
/*     */       }
/*  84 */       this._modeName = parser.getQNameIgnoreDefaultNs(mode);
/*     */     }
/*     */ 
/*  88 */     this._functionName = parser.getTopLevelStylesheet().getMode(this._modeName).functionName();
/*     */ 
/*  90 */     parseChildren(parser);
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
/*  94 */     if (this._select != null) {
/*  95 */       this._type = this._select.typeCheck(stable);
/*  96 */       if (((this._type instanceof NodeType)) || ((this._type instanceof ReferenceType))) {
/*  97 */         this._select = new CastExpr(this._select, Type.NodeSet);
/*  98 */         this._type = Type.NodeSet;
/*     */       }
/* 100 */       if (((this._type instanceof NodeSetType)) || ((this._type instanceof ResultTreeType))) {
/* 101 */         typeCheckContents(stable);
/* 102 */         return Type.Void;
/*     */       }
/* 104 */       throw new TypeCheckError(this);
/*     */     }
/*     */ 
/* 107 */     typeCheckContents(stable);
/* 108 */     return Type.Void;
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 117 */     boolean setStartNodeCalled = false;
/* 118 */     Stylesheet stylesheet = classGen.getStylesheet();
/* 119 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 120 */     InstructionList il = methodGen.getInstructionList();
/* 121 */     int current = methodGen.getLocalIndex("current");
/*     */ 
/* 124 */     Vector sortObjects = new Vector();
/* 125 */     Enumeration children = elements();
/* 126 */     while (children.hasMoreElements()) {
/* 127 */       Object child = children.nextElement();
/* 128 */       if ((child instanceof Sort)) {
/* 129 */         sortObjects.addElement(child);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 134 */     if ((stylesheet.hasLocalParams()) || (hasContents())) {
/* 135 */       il.append(classGen.loadTranslet());
/* 136 */       int pushFrame = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "pushParamFrame", "()V");
/*     */ 
/* 139 */       il.append(new INVOKEVIRTUAL(pushFrame));
/*     */ 
/* 141 */       translateContents(classGen, methodGen);
/*     */     }
/*     */ 
/* 145 */     il.append(classGen.loadTranslet());
/*     */ 
/* 148 */     if ((this._type != null) && ((this._type instanceof ResultTreeType)))
/*     */     {
/* 150 */       if (sortObjects.size() > 0) {
/* 151 */         ErrorMsg err = new ErrorMsg("RESULT_TREE_SORT_ERR", this);
/* 152 */         getParser().reportError(4, err);
/*     */       }
/*     */ 
/* 155 */       this._select.translate(classGen, methodGen);
/*     */ 
/* 157 */       this._type.translateTo(classGen, methodGen, Type.NodeSet);
/*     */     }
/*     */     else {
/* 160 */       il.append(methodGen.loadDOM());
/*     */ 
/* 163 */       if (sortObjects.size() > 0) {
/* 164 */         Sort.translateSortIterator(classGen, methodGen, this._select, sortObjects);
/*     */ 
/* 166 */         int setStartNode = cpg.addInterfaceMethodref("com.sun.org.apache.xml.internal.dtm.DTMAxisIterator", "setStartNode", "(I)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*     */ 
/* 170 */         il.append(methodGen.loadCurrentNode());
/* 171 */         il.append(new INVOKEINTERFACE(setStartNode, 2));
/* 172 */         setStartNodeCalled = true;
/*     */       }
/* 175 */       else if (this._select == null) {
/* 176 */         Mode.compileGetChildren(classGen, methodGen, current);
/*     */       } else {
/* 178 */         this._select.translate(classGen, methodGen);
/*     */       }
/*     */     }
/*     */ 
/* 182 */     if ((this._select != null) && (!setStartNodeCalled)) {
/* 183 */       this._select.startIterator(classGen, methodGen);
/*     */     }
/*     */ 
/* 187 */     String className = classGen.getStylesheet().getClassName();
/* 188 */     il.append(methodGen.loadHandler());
/* 189 */     String applyTemplatesSig = classGen.getApplyTemplatesSig();
/* 190 */     int applyTemplates = cpg.addMethodref(className, this._functionName, applyTemplatesSig);
/*     */ 
/* 193 */     il.append(new INVOKEVIRTUAL(applyTemplates));
/*     */ 
/* 196 */     if ((stylesheet.hasLocalParams()) || (hasContents())) {
/* 197 */       il.append(classGen.loadTranslet());
/* 198 */       int popFrame = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "popParamFrame", "()V");
/*     */ 
/* 201 */       il.append(new INVOKEVIRTUAL(popFrame));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.ApplyTemplates
 * JD-Core Version:    0.6.2
 */