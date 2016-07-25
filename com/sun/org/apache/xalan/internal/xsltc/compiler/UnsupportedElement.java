/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class UnsupportedElement extends SyntaxTreeNode
/*     */ {
/*  45 */   private Vector _fallbacks = null;
/*  46 */   private ErrorMsg _message = null;
/*  47 */   private boolean _isExtension = false;
/*     */ 
/*     */   public UnsupportedElement(String uri, String prefix, String local, boolean isExtension)
/*     */   {
/*  53 */     super(uri, prefix, local);
/*  54 */     this._isExtension = isExtension;
/*     */   }
/*     */ 
/*     */   public void setErrorMessage(ErrorMsg message)
/*     */   {
/*  67 */     this._message = message;
/*     */   }
/*     */ 
/*     */   public void display(int indent)
/*     */   {
/*  74 */     indent(indent);
/*  75 */     Util.println("Unsupported element = " + this._qname.getNamespace() + ":" + this._qname.getLocalPart());
/*     */ 
/*  77 */     displayContents(indent + 4);
/*     */   }
/*     */ 
/*     */   private void processFallbacks(Parser parser)
/*     */   {
/*  86 */     Vector children = getContents();
/*  87 */     if (children != null) {
/*  88 */       int count = children.size();
/*  89 */       for (int i = 0; i < count; i++) {
/*  90 */         SyntaxTreeNode child = (SyntaxTreeNode)children.elementAt(i);
/*  91 */         if ((child instanceof Fallback)) {
/*  92 */           Fallback fallback = (Fallback)child;
/*  93 */           fallback.activate();
/*  94 */           fallback.parseContents(parser);
/*  95 */           if (this._fallbacks == null) {
/*  96 */             this._fallbacks = new Vector();
/*     */           }
/*  98 */           this._fallbacks.addElement(child);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void parseContents(Parser parser)
/*     */   {
/* 108 */     processFallbacks(parser);
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable)
/*     */     throws TypeCheckError
/*     */   {
/* 115 */     if (this._fallbacks != null) {
/* 116 */       int count = this._fallbacks.size();
/* 117 */       for (int i = 0; i < count; i++) {
/* 118 */         Fallback fallback = (Fallback)this._fallbacks.elementAt(i);
/* 119 */         fallback.typeCheck(stable);
/*     */       }
/*     */     }
/* 122 */     return Type.Void;
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 129 */     if (this._fallbacks != null) {
/* 130 */       int count = this._fallbacks.size();
/* 131 */       for (int i = 0; i < count; i++) {
/* 132 */         Fallback fallback = (Fallback)this._fallbacks.elementAt(i);
/* 133 */         fallback.translate(classGen, methodGen);
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 142 */       ConstantPoolGen cpg = classGen.getConstantPool();
/* 143 */       InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 145 */       int unsupportedElem = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "unsupported_ElementF", "(Ljava/lang/String;Z)V");
/*     */ 
/* 147 */       il.append(new PUSH(cpg, getQName().toString()));
/* 148 */       il.append(new PUSH(cpg, this._isExtension));
/* 149 */       il.append(new INVOKESTATIC(unsupportedElem));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.UnsupportedElement
 * JD-Core Version:    0.6.2
 */