/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.StringType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class KeyCall extends FunctionCall
/*     */ {
/*     */   private Expression _name;
/*     */   private Expression _value;
/*     */   private Type _valueType;
/*  73 */   private QName _resolvedQName = null;
/*     */ 
/*     */   public KeyCall(QName fname, Vector arguments)
/*     */   {
/*  88 */     super(fname, arguments);
/*  89 */     switch (argumentCount()) {
/*     */     case 1:
/*  91 */       this._name = null;
/*  92 */       this._value = argument(0);
/*  93 */       break;
/*     */     case 2:
/*  95 */       this._name = argument(0);
/*  96 */       this._value = argument(1);
/*  97 */       break;
/*     */     default:
/*  99 */       this._name = (this._value = null);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addParentDependency()
/*     */   {
/* 118 */     if (this._resolvedQName == null) return;
/*     */ 
/* 120 */     SyntaxTreeNode node = this;
/* 121 */     while ((node != null) && (!(node instanceof TopLevelElement))) {
/* 122 */       node = node.getParent();
/*     */     }
/*     */ 
/* 125 */     TopLevelElement parent = (TopLevelElement)node;
/* 126 */     if (parent != null)
/* 127 */       parent.addDependency(getSymbolTable().getKey(this._resolvedQName));
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable)
/*     */     throws TypeCheckError
/*     */   {
/* 139 */     Type returnType = super.typeCheck(stable);
/*     */ 
/* 143 */     if (this._name != null) {
/* 144 */       Type nameType = this._name.typeCheck(stable);
/*     */ 
/* 146 */       if ((this._name instanceof LiteralExpr)) {
/* 147 */         LiteralExpr literal = (LiteralExpr)this._name;
/* 148 */         this._resolvedQName = getParser().getQNameIgnoreDefaultNs(literal.getValue());
/*     */       }
/* 151 */       else if (!(nameType instanceof StringType)) {
/* 152 */         this._name = new CastExpr(this._name, Type.String);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 165 */     this._valueType = this._value.typeCheck(stable);
/*     */ 
/* 167 */     if ((this._valueType != Type.NodeSet) && (this._valueType != Type.Reference) && (this._valueType != Type.String))
/*     */     {
/* 170 */       this._value = new CastExpr(this._value, Type.String);
/* 171 */       this._valueType = this._value.typeCheck(stable);
/*     */     }
/*     */ 
/* 175 */     addParentDependency();
/*     */ 
/* 177 */     return returnType;
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 190 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 191 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 194 */     int getKeyIndex = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "getKeyIndex", "(Ljava/lang/String;)Lcom/sun/org/apache/xalan/internal/xsltc/dom/KeyIndex;");
/*     */ 
/* 200 */     int keyDom = cpg.addMethodref("com/sun/org/apache/xalan/internal/xsltc/dom/KeyIndex", "setDom", "(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;I)V");
/*     */ 
/* 205 */     int getKeyIterator = cpg.addMethodref("com/sun/org/apache/xalan/internal/xsltc/dom/KeyIndex", "getKeyIndexIterator", "(" + this._valueType.toSignature() + "Z)" + "Lcom/sun/org/apache/xalan/internal/xsltc/dom/KeyIndex$KeyIndexIterator;");
/*     */ 
/* 212 */     il.append(classGen.loadTranslet());
/* 213 */     if (this._name == null)
/* 214 */       il.append(new PUSH(cpg, "##id"));
/* 215 */     else if (this._resolvedQName != null)
/* 216 */       il.append(new PUSH(cpg, this._resolvedQName.toString()));
/*     */     else {
/* 218 */       this._name.translate(classGen, methodGen);
/*     */     }
/*     */ 
/* 228 */     il.append(new INVOKEVIRTUAL(getKeyIndex));
/* 229 */     il.append(DUP);
/* 230 */     il.append(methodGen.loadDOM());
/* 231 */     il.append(methodGen.loadCurrentNode());
/* 232 */     il.append(new INVOKEVIRTUAL(keyDom));
/*     */ 
/* 234 */     this._value.translate(classGen, methodGen);
/* 235 */     il.append(this._name != null ? ICONST_1 : ICONST_0);
/* 236 */     il.append(new INVOKEVIRTUAL(getKeyIterator));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.KeyCall
 * JD-Core Version:    0.6.2
 */