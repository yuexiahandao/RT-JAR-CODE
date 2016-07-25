/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ReferenceType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ import com.sun.org.apache.xml.internal.utils.XML11Char;
/*     */ 
/*     */ final class WithParam extends Instruction
/*     */ {
/*     */   private QName _name;
/*     */   protected String _escapedName;
/*     */   private Expression _select;
/*  68 */   private boolean _doParameterOptimization = false;
/*     */ 
/*     */   public void display(int indent)
/*     */   {
/*  74 */     indent(indent);
/*  75 */     Util.println("with-param " + this._name);
/*  76 */     if (this._select != null) {
/*  77 */       indent(indent + 4);
/*  78 */       Util.println("select " + this._select.toString());
/*     */     }
/*  80 */     displayContents(indent + 4);
/*     */   }
/*     */ 
/*     */   public String getEscapedName()
/*     */   {
/*  87 */     return this._escapedName;
/*     */   }
/*     */ 
/*     */   public QName getName()
/*     */   {
/*  94 */     return this._name;
/*     */   }
/*     */ 
/*     */   public void setName(QName name)
/*     */   {
/* 101 */     this._name = name;
/* 102 */     this._escapedName = Util.escape(name.getStringRep());
/*     */   }
/*     */ 
/*     */   public void setDoParameterOptimization(boolean flag)
/*     */   {
/* 109 */     this._doParameterOptimization = flag;
/*     */   }
/*     */ 
/*     */   public void parseContents(Parser parser)
/*     */   {
/* 117 */     String name = getAttribute("name");
/* 118 */     if (name.length() > 0) {
/* 119 */       if (!XML11Char.isXML11ValidQName(name)) {
/* 120 */         ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", name, this);
/*     */ 
/* 122 */         parser.reportError(3, err);
/*     */       }
/* 124 */       setName(parser.getQNameIgnoreDefaultNs(name));
/*     */     }
/*     */     else {
/* 127 */       reportError(this, parser, "REQUIRED_ATTR_ERR", "name");
/*     */     }
/*     */ 
/* 130 */     String select = getAttribute("select");
/* 131 */     if (select.length() > 0) {
/* 132 */       this._select = parser.parseExpression(this, "select", null);
/*     */     }
/*     */ 
/* 135 */     parseChildren(parser);
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable)
/*     */     throws TypeCheckError
/*     */   {
/* 143 */     if (this._select != null) {
/* 144 */       Type tselect = this._select.typeCheck(stable);
/* 145 */       if (!(tselect instanceof ReferenceType))
/* 146 */         this._select = new CastExpr(this._select, Type.Reference);
/*     */     }
/*     */     else
/*     */     {
/* 150 */       typeCheckContents(stable);
/*     */     }
/* 152 */     return Type.Void;
/*     */   }
/*     */ 
/*     */   public void translateValue(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 162 */     if (this._select != null) {
/* 163 */       this._select.translate(classGen, methodGen);
/* 164 */       this._select.startIterator(classGen, methodGen);
/*     */     }
/* 167 */     else if (hasContents()) {
/* 168 */       compileResultTree(classGen, methodGen);
/*     */     }
/*     */     else
/*     */     {
/* 172 */       ConstantPoolGen cpg = classGen.getConstantPool();
/* 173 */       InstructionList il = methodGen.getInstructionList();
/* 174 */       il.append(new PUSH(cpg, ""));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 184 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 185 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 188 */     if (this._doParameterOptimization) {
/* 189 */       translateValue(classGen, methodGen);
/* 190 */       return;
/*     */     }
/*     */ 
/* 194 */     String name = Util.escape(getEscapedName());
/*     */ 
/* 197 */     il.append(classGen.loadTranslet());
/*     */ 
/* 200 */     il.append(new PUSH(cpg, name));
/*     */ 
/* 202 */     translateValue(classGen, methodGen);
/*     */ 
/* 204 */     il.append(new PUSH(cpg, false));
/*     */ 
/* 206 */     il.append(new INVOKEVIRTUAL(cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "addParameter", "(Ljava/lang/String;Ljava/lang/Object;Z)Ljava/lang/Object;")));
/*     */ 
/* 209 */     il.append(POP);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.WithParam
 * JD-Core Version:    0.6.2
 */