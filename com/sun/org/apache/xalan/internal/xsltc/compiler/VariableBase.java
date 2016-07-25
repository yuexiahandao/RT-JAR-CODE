/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.Instruction;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.NEW;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeSetType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ import com.sun.org.apache.xml.internal.utils.XML11Char;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Vector;
/*     */ 
/*     */ class VariableBase extends TopLevelElement
/*     */ {
/*     */   protected QName _name;
/*     */   protected String _escapedName;
/*     */   protected com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type _type;
/*     */   protected boolean _isLocal;
/*     */   protected LocalVariableGen _local;
/*     */   protected Instruction _loadInstruction;
/*     */   protected Instruction _storeInstruction;
/*     */   protected Expression _select;
/*     */   protected String select;
/*  63 */   protected Vector _refs = new Vector(2);
/*     */ 
/*  66 */   protected Vector _dependencies = null;
/*     */ 
/*  69 */   protected boolean _ignore = false;
/*     */ 
/*     */   public void disable()
/*     */   {
/*  75 */     this._ignore = true;
/*     */   }
/*     */ 
/*     */   public void addReference(VariableRefBase vref)
/*     */   {
/*  83 */     this._refs.addElement(vref);
/*     */   }
/*     */ 
/*     */   public void copyReferences(VariableBase var)
/*     */   {
/*  93 */     int size = this._refs.size();
/*  94 */     for (int i = 0; i < size; i++)
/*  95 */       var.addReference((VariableRefBase)this._refs.get(i));
/*     */   }
/*     */ 
/*     */   public void mapRegister(MethodGenerator methodGen)
/*     */   {
/* 103 */     if (this._local == null) {
/* 104 */       InstructionList il = methodGen.getInstructionList();
/* 105 */       String name = getEscapedName();
/* 106 */       com.sun.org.apache.bcel.internal.generic.Type varType = this._type.toJCType();
/* 107 */       this._local = methodGen.addLocalVariable2(name, varType, il.getEnd());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void unmapRegister(MethodGenerator methodGen)
/*     */   {
/* 116 */     if (this._local != null) {
/* 117 */       this._local.setEnd(methodGen.getInstructionList().getEnd());
/* 118 */       methodGen.removeLocalVariable(this._local);
/* 119 */       this._refs = null;
/* 120 */       this._local = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Instruction loadInstruction()
/*     */   {
/* 129 */     Instruction instr = this._loadInstruction;
/* 130 */     if (this._loadInstruction == null) {
/* 131 */       this._loadInstruction = this._type.LOAD(this._local.getIndex());
/*     */     }
/* 133 */     return this._loadInstruction;
/*     */   }
/*     */ 
/*     */   public Instruction storeInstruction()
/*     */   {
/* 141 */     Instruction instr = this._storeInstruction;
/* 142 */     if (this._storeInstruction == null) {
/* 143 */       this._storeInstruction = this._type.STORE(this._local.getIndex());
/*     */     }
/* 145 */     return this._storeInstruction;
/*     */   }
/*     */ 
/*     */   public Expression getExpression()
/*     */   {
/* 152 */     return this._select;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 159 */     return "variable(" + this._name + ")";
/*     */   }
/*     */ 
/*     */   public void display(int indent)
/*     */   {
/* 166 */     indent(indent);
/* 167 */     System.out.println("Variable " + this._name);
/* 168 */     if (this._select != null) {
/* 169 */       indent(indent + 4);
/* 170 */       System.out.println("select " + this._select.toString());
/*     */     }
/* 172 */     displayContents(indent + 4);
/*     */   }
/*     */ 
/*     */   public com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type getType()
/*     */   {
/* 179 */     return this._type;
/*     */   }
/*     */ 
/*     */   public QName getName()
/*     */   {
/* 187 */     return this._name;
/*     */   }
/*     */ 
/*     */   public String getEscapedName()
/*     */   {
/* 194 */     return this._escapedName;
/*     */   }
/*     */ 
/*     */   public void setName(QName name)
/*     */   {
/* 201 */     this._name = name;
/* 202 */     this._escapedName = Util.escape(name.getStringRep());
/*     */   }
/*     */ 
/*     */   public boolean isLocal()
/*     */   {
/* 209 */     return this._isLocal;
/*     */   }
/*     */ 
/*     */   public void parseContents(Parser parser)
/*     */   {
/* 217 */     String name = getAttribute("name");
/*     */ 
/* 219 */     if (name.length() > 0) {
/* 220 */       if (!XML11Char.isXML11ValidQName(name)) {
/* 221 */         ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", name, this);
/* 222 */         parser.reportError(3, err);
/*     */       }
/* 224 */       setName(parser.getQNameIgnoreDefaultNs(name));
/*     */     }
/*     */     else {
/* 227 */       reportError(this, parser, "REQUIRED_ATTR_ERR", "name");
/*     */     }
/*     */ 
/* 230 */     VariableBase other = parser.lookupVariable(this._name);
/* 231 */     if ((other != null) && (other.getParent() == getParent())) {
/* 232 */       reportError(this, parser, "VARIABLE_REDEF_ERR", name);
/*     */     }
/*     */ 
/* 235 */     this.select = getAttribute("select");
/* 236 */     if (this.select.length() > 0) {
/* 237 */       this._select = getParser().parseExpression(this, "select", null);
/* 238 */       if (this._select.isDummy()) {
/* 239 */         reportError(this, parser, "REQUIRED_ATTR_ERR", "select");
/* 240 */         return;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 245 */     parseChildren(parser);
/*     */   }
/*     */ 
/*     */   public void translateValue(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 255 */     if (this._select != null) {
/* 256 */       this._select.translate(classGen, methodGen);
/*     */ 
/* 259 */       if ((this._select.getType() instanceof NodeSetType)) {
/* 260 */         ConstantPoolGen cpg = classGen.getConstantPool();
/* 261 */         InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 263 */         int initCNI = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.dom.CachedNodeListIterator", "<init>", "(Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;)V");
/*     */ 
/* 268 */         il.append(new NEW(cpg.addClass("com.sun.org.apache.xalan.internal.xsltc.dom.CachedNodeListIterator")));
/* 269 */         il.append(DUP_X1);
/* 270 */         il.append(SWAP);
/*     */ 
/* 272 */         il.append(new INVOKESPECIAL(initCNI));
/*     */       }
/* 274 */       this._select.startIterator(classGen, methodGen);
/*     */     }
/* 277 */     else if (hasContents()) {
/* 278 */       compileResultTree(classGen, methodGen);
/*     */     }
/*     */     else
/*     */     {
/* 282 */       ConstantPoolGen cpg = classGen.getConstantPool();
/* 283 */       InstructionList il = methodGen.getInstructionList();
/* 284 */       il.append(new PUSH(cpg, ""));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.VariableBase
 * JD-Core Version:    0.6.2
 */