/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.BranchHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.GOTO;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFEQ;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFGE;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFGT;
/*     */ import com.sun.org.apache.bcel.internal.generic.ILOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.ISTORE;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeSetType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.StringType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ import com.sun.org.apache.xml.internal.utils.XML11Char;
/*     */ 
/*     */ final class Key extends TopLevelElement
/*     */ {
/*     */   private QName _name;
/*     */   private Pattern _match;
/*     */   private Expression _use;
/*     */   private Type _useType;
/*     */ 
/*     */   public void parseContents(Parser parser)
/*     */   {
/*  86 */     String name = getAttribute("name");
/*  87 */     if (!XML11Char.isXML11ValidQName(name)) {
/*  88 */       ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", name, this);
/*  89 */       parser.reportError(3, err);
/*     */     }
/*     */ 
/*  93 */     this._name = parser.getQNameIgnoreDefaultNs(name);
/*  94 */     getSymbolTable().addKey(this._name, this);
/*     */ 
/*  96 */     this._match = parser.parsePattern(this, "match", null);
/*  97 */     this._use = parser.parseExpression(this, "use", null);
/*     */ 
/* 100 */     if (this._name == null) {
/* 101 */       reportError(this, parser, "REQUIRED_ATTR_ERR", "name");
/* 102 */       return;
/*     */     }
/* 104 */     if (this._match.isDummy()) {
/* 105 */       reportError(this, parser, "REQUIRED_ATTR_ERR", "match");
/* 106 */       return;
/*     */     }
/* 108 */     if (this._use.isDummy()) {
/* 109 */       reportError(this, parser, "REQUIRED_ATTR_ERR", "use");
/* 110 */       return;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 119 */     return this._name.toString();
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable) throws TypeCheckError
/*     */   {
/* 124 */     this._match.typeCheck(stable);
/*     */ 
/* 127 */     this._useType = this._use.typeCheck(stable);
/* 128 */     if ((!(this._useType instanceof StringType)) && (!(this._useType instanceof NodeSetType)))
/*     */     {
/* 131 */       this._use = new CastExpr(this._use, Type.String);
/*     */     }
/*     */ 
/* 134 */     return Type.Void;
/*     */   }
/*     */ 
/*     */   public void traverseNodeSet(ClassGenerator classGen, MethodGenerator methodGen, int buildKeyIndex)
/*     */   {
/* 145 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 146 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 149 */     int getNodeValue = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getStringValueX", "(I)Ljava/lang/String;");
/*     */ 
/* 153 */     int getNodeIdent = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getNodeIdent", "(I)I");
/*     */ 
/* 158 */     int keyDom = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "setKeyIndexDom", "(Ljava/lang/String;Lcom/sun/org/apache/xalan/internal/xsltc/DOM;)V");
/*     */ 
/* 166 */     LocalVariableGen parentNode = methodGen.addLocalVariable("parentNode", Util.getJCRefType("I"), null, null);
/*     */ 
/* 172 */     parentNode.setStart(il.append(new ISTORE(parentNode.getIndex())));
/*     */ 
/* 175 */     il.append(methodGen.loadCurrentNode());
/* 176 */     il.append(methodGen.loadIterator());
/*     */ 
/* 179 */     this._use.translate(classGen, methodGen);
/* 180 */     this._use.startIterator(classGen, methodGen);
/* 181 */     il.append(methodGen.storeIterator());
/*     */ 
/* 183 */     BranchHandle nextNode = il.append(new GOTO(null));
/* 184 */     InstructionHandle loop = il.append(NOP);
/*     */ 
/* 187 */     il.append(classGen.loadTranslet());
/* 188 */     il.append(new PUSH(cpg, this._name.toString()));
/* 189 */     parentNode.setEnd(il.append(new ILOAD(parentNode.getIndex())));
/*     */ 
/* 192 */     il.append(methodGen.loadDOM());
/* 193 */     il.append(methodGen.loadCurrentNode());
/* 194 */     il.append(new INVOKEINTERFACE(getNodeValue, 2));
/*     */ 
/* 197 */     il.append(new INVOKEVIRTUAL(buildKeyIndex));
/*     */ 
/* 199 */     il.append(classGen.loadTranslet());
/* 200 */     il.append(new PUSH(cpg, getName()));
/* 201 */     il.append(methodGen.loadDOM());
/* 202 */     il.append(new INVOKEVIRTUAL(keyDom));
/*     */ 
/* 204 */     nextNode.setTarget(il.append(methodGen.loadIterator()));
/* 205 */     il.append(methodGen.nextNode());
/*     */ 
/* 207 */     il.append(DUP);
/* 208 */     il.append(methodGen.storeCurrentNode());
/* 209 */     il.append(new IFGE(loop));
/*     */ 
/* 212 */     il.append(methodGen.storeIterator());
/* 213 */     il.append(methodGen.storeCurrentNode());
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 222 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 223 */     InstructionList il = methodGen.getInstructionList();
/* 224 */     int current = methodGen.getLocalIndex("current");
/*     */ 
/* 227 */     int key = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "buildKeyIndex", "(Ljava/lang/String;ILjava/lang/Object;)V");
/*     */ 
/* 232 */     int keyDom = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "setKeyIndexDom", "(Ljava/lang/String;Lcom/sun/org/apache/xalan/internal/xsltc/DOM;)V");
/*     */ 
/* 236 */     int getNodeIdent = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getNodeIdent", "(I)I");
/*     */ 
/* 241 */     int git = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getAxisIterator", "(I)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*     */ 
/* 245 */     il.append(methodGen.loadCurrentNode());
/* 246 */     il.append(methodGen.loadIterator());
/*     */ 
/* 249 */     il.append(methodGen.loadDOM());
/* 250 */     il.append(new PUSH(cpg, 4));
/* 251 */     il.append(new INVOKEINTERFACE(git, 2));
/*     */ 
/* 254 */     il.append(methodGen.loadCurrentNode());
/* 255 */     il.append(methodGen.setStartNode());
/* 256 */     il.append(methodGen.storeIterator());
/*     */ 
/* 259 */     BranchHandle nextNode = il.append(new GOTO(null));
/* 260 */     InstructionHandle loop = il.append(NOP);
/*     */ 
/* 263 */     il.append(methodGen.loadCurrentNode());
/* 264 */     this._match.translate(classGen, methodGen);
/* 265 */     this._match.synthesize(classGen, methodGen);
/* 266 */     BranchHandle skipNode = il.append(new IFEQ(null));
/*     */ 
/* 269 */     if ((this._useType instanceof NodeSetType))
/*     */     {
/* 271 */       il.append(methodGen.loadCurrentNode());
/* 272 */       traverseNodeSet(classGen, methodGen, key);
/*     */     }
/*     */     else {
/* 275 */       il.append(classGen.loadTranslet());
/* 276 */       il.append(DUP);
/* 277 */       il.append(new PUSH(cpg, this._name.toString()));
/* 278 */       il.append(DUP_X1);
/* 279 */       il.append(methodGen.loadCurrentNode());
/* 280 */       this._use.translate(classGen, methodGen);
/* 281 */       il.append(new INVOKEVIRTUAL(key));
/*     */ 
/* 283 */       il.append(methodGen.loadDOM());
/* 284 */       il.append(new INVOKEVIRTUAL(keyDom));
/*     */     }
/*     */ 
/* 288 */     InstructionHandle skip = il.append(NOP);
/*     */ 
/* 290 */     il.append(methodGen.loadIterator());
/* 291 */     il.append(methodGen.nextNode());
/* 292 */     il.append(DUP);
/* 293 */     il.append(methodGen.storeCurrentNode());
/* 294 */     il.append(new IFGT(loop));
/*     */ 
/* 297 */     il.append(methodGen.storeIterator());
/* 298 */     il.append(methodGen.storeCurrentNode());
/*     */ 
/* 300 */     nextNode.setTarget(skip);
/* 301 */     skipNode.setTarget(skip);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.Key
 * JD-Core Version:    0.6.2
 */