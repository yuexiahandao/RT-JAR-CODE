/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler.util;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ALOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.ASTORE;
/*     */ import com.sun.org.apache.bcel.internal.generic.CHECKCAST;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.GETFIELD;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFEQ;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.Instruction;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.NEW;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.FlowList;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.Parser;
/*     */ 
/*     */ public final class ResultTreeType extends Type
/*     */ {
/*     */   private final String _methodName;
/*     */ 
/*     */   protected ResultTreeType()
/*     */   {
/*  52 */     this._methodName = null;
/*     */   }
/*     */ 
/*     */   public ResultTreeType(String methodName) {
/*  56 */     this._methodName = methodName;
/*     */   }
/*     */ 
/*     */   public String toString() {
/*  60 */     return "result-tree";
/*     */   }
/*     */ 
/*     */   public boolean identicalTo(Type other) {
/*  64 */     return other instanceof ResultTreeType;
/*     */   }
/*     */ 
/*     */   public String toSignature() {
/*  68 */     return "Lcom/sun/org/apache/xalan/internal/xsltc/DOM;";
/*     */   }
/*     */ 
/*     */   public com.sun.org.apache.bcel.internal.generic.Type toJCType() {
/*  72 */     return Util.getJCRefType(toSignature());
/*     */   }
/*     */ 
/*     */   public String getMethodName() {
/*  76 */     return this._methodName;
/*     */   }
/*     */ 
/*     */   public boolean implementedAsMethod() {
/*  80 */     return this._methodName != null;
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Type type)
/*     */   {
/*  95 */     if (type == Type.String) {
/*  96 */       translateTo(classGen, methodGen, (StringType)type);
/*     */     }
/*  98 */     else if (type == Type.Boolean) {
/*  99 */       translateTo(classGen, methodGen, (BooleanType)type);
/*     */     }
/* 101 */     else if (type == Type.Real) {
/* 102 */       translateTo(classGen, methodGen, (RealType)type);
/*     */     }
/* 104 */     else if (type == Type.NodeSet) {
/* 105 */       translateTo(classGen, methodGen, (NodeSetType)type);
/*     */     }
/* 107 */     else if (type == Type.Reference) {
/* 108 */       translateTo(classGen, methodGen, (ReferenceType)type);
/*     */     }
/* 110 */     else if (type == Type.Object) {
/* 111 */       translateTo(classGen, methodGen, (ObjectType)type);
/*     */     }
/*     */     else {
/* 114 */       ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), type.toString());
/*     */ 
/* 116 */       classGen.getParser().reportError(2, err);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type)
/*     */   {
/* 133 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 134 */     InstructionList il = methodGen.getInstructionList();
/* 135 */     il.append(POP);
/* 136 */     il.append(ICONST_1);
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, StringType type)
/*     */   {
/* 149 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 150 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 152 */     if (this._methodName == null) {
/* 153 */       int index = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getStringValue", "()Ljava/lang/String;");
/*     */ 
/* 156 */       il.append(new INVOKEINTERFACE(index, 1));
/*     */     }
/*     */     else {
/* 159 */       String className = classGen.getClassName();
/* 160 */       int current = methodGen.getLocalIndex("current");
/*     */ 
/* 163 */       il.append(classGen.loadTranslet());
/* 164 */       if (classGen.isExternal()) {
/* 165 */         il.append(new CHECKCAST(cpg.addClass(className)));
/*     */       }
/* 167 */       il.append(DUP);
/* 168 */       il.append(new GETFIELD(cpg.addFieldref(className, "_dom", "Lcom/sun/org/apache/xalan/internal/xsltc/DOM;")));
/*     */ 
/* 172 */       int index = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.StringValueHandler", "<init>", "()V");
/* 173 */       il.append(new NEW(cpg.addClass("com.sun.org.apache.xalan.internal.xsltc.runtime.StringValueHandler")));
/* 174 */       il.append(DUP);
/* 175 */       il.append(DUP);
/* 176 */       il.append(new INVOKESPECIAL(index));
/*     */ 
/* 179 */       LocalVariableGen handler = methodGen.addLocalVariable("rt_to_string_handler", Util.getJCRefType("Lcom/sun/org/apache/xalan/internal/xsltc/runtime/StringValueHandler;"), null, null);
/*     */ 
/* 183 */       handler.setStart(il.append(new ASTORE(handler.getIndex())));
/*     */ 
/* 186 */       index = cpg.addMethodref(className, this._methodName, "(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;)V");
/*     */ 
/* 188 */       il.append(new INVOKEVIRTUAL(index));
/*     */ 
/* 191 */       handler.setEnd(il.append(new ALOAD(handler.getIndex())));
/* 192 */       index = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.StringValueHandler", "getValue", "()Ljava/lang/String;");
/*     */ 
/* 195 */       il.append(new INVOKEVIRTUAL(index));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, RealType type)
/*     */   {
/* 210 */     translateTo(classGen, methodGen, Type.String);
/* 211 */     Type.String.translateTo(classGen, methodGen, Type.Real);
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ReferenceType type)
/*     */   {
/* 225 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 226 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 228 */     if (this._methodName == null) {
/* 229 */       il.append(NOP);
/*     */     }
/*     */     else
/*     */     {
/* 233 */       String className = classGen.getClassName();
/* 234 */       int current = methodGen.getLocalIndex("current");
/*     */ 
/* 237 */       il.append(classGen.loadTranslet());
/* 238 */       if (classGen.isExternal()) {
/* 239 */         il.append(new CHECKCAST(cpg.addClass(className)));
/*     */       }
/* 241 */       il.append(methodGen.loadDOM());
/*     */ 
/* 244 */       il.append(methodGen.loadDOM());
/* 245 */       int index = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getResultTreeFrag", "(IZ)Lcom/sun/org/apache/xalan/internal/xsltc/DOM;");
/*     */ 
/* 248 */       il.append(new PUSH(cpg, 32));
/* 249 */       il.append(new PUSH(cpg, false));
/* 250 */       il.append(new INVOKEINTERFACE(index, 3));
/* 251 */       il.append(DUP);
/*     */ 
/* 254 */       LocalVariableGen newDom = methodGen.addLocalVariable("rt_to_reference_dom", Util.getJCRefType("Lcom/sun/org/apache/xalan/internal/xsltc/DOM;"), null, null);
/*     */ 
/* 257 */       il.append(new CHECKCAST(cpg.addClass("Lcom/sun/org/apache/xalan/internal/xsltc/DOM;")));
/* 258 */       newDom.setStart(il.append(new ASTORE(newDom.getIndex())));
/*     */ 
/* 261 */       index = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getOutputDomBuilder", "()Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;");
/*     */ 
/* 265 */       il.append(new INVOKEINTERFACE(index, 1));
/*     */ 
/* 270 */       il.append(DUP);
/* 271 */       il.append(DUP);
/*     */ 
/* 274 */       LocalVariableGen domBuilder = methodGen.addLocalVariable("rt_to_reference_handler", Util.getJCRefType("Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;"), null, null);
/*     */ 
/* 278 */       domBuilder.setStart(il.append(new ASTORE(domBuilder.getIndex())));
/*     */ 
/* 281 */       index = cpg.addInterfaceMethodref("com.sun.org.apache.xml.internal.serializer.SerializationHandler", "startDocument", "()V");
/*     */ 
/* 283 */       il.append(new INVOKEINTERFACE(index, 1));
/*     */ 
/* 286 */       index = cpg.addMethodref(className, this._methodName, "(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;)V");
/*     */ 
/* 292 */       il.append(new INVOKEVIRTUAL(index));
/*     */ 
/* 295 */       domBuilder.setEnd(il.append(new ALOAD(domBuilder.getIndex())));
/* 296 */       index = cpg.addInterfaceMethodref("com.sun.org.apache.xml.internal.serializer.SerializationHandler", "endDocument", "()V");
/*     */ 
/* 298 */       il.append(new INVOKEINTERFACE(index, 1));
/*     */ 
/* 301 */       newDom.setEnd(il.append(new ALOAD(newDom.getIndex())));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, NodeSetType type)
/*     */   {
/* 320 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 321 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 324 */     il.append(DUP);
/*     */ 
/* 329 */     il.append(classGen.loadTranslet());
/* 330 */     il.append(new GETFIELD(cpg.addFieldref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "namesArray", "[Ljava/lang/String;")));
/*     */ 
/* 333 */     il.append(classGen.loadTranslet());
/* 334 */     il.append(new GETFIELD(cpg.addFieldref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "urisArray", "[Ljava/lang/String;")));
/*     */ 
/* 337 */     il.append(classGen.loadTranslet());
/* 338 */     il.append(new GETFIELD(cpg.addFieldref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "typesArray", "[I")));
/*     */ 
/* 341 */     il.append(classGen.loadTranslet());
/* 342 */     il.append(new GETFIELD(cpg.addFieldref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "namespaceArray", "[Ljava/lang/String;")));
/*     */ 
/* 346 */     int mapping = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "setupMapping", "([Ljava/lang/String;[Ljava/lang/String;[I[Ljava/lang/String;)V");
/*     */ 
/* 352 */     il.append(new INVOKEINTERFACE(mapping, 5));
/* 353 */     il.append(DUP);
/*     */ 
/* 356 */     int iter = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getIterator", "()Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*     */ 
/* 359 */     il.append(new INVOKEINTERFACE(iter, 1));
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ObjectType type)
/*     */   {
/* 369 */     methodGen.getInstructionList().append(NOP);
/*     */   }
/*     */ 
/*     */   public FlowList translateToDesynthesized(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type)
/*     */   {
/* 385 */     InstructionList il = methodGen.getInstructionList();
/* 386 */     translateTo(classGen, methodGen, Type.Boolean);
/* 387 */     return new FlowList(il.append(new IFEQ(null)));
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Class clazz)
/*     */   {
/* 404 */     String className = clazz.getName();
/* 405 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 406 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 408 */     if (className.equals("org.w3c.dom.Node")) {
/* 409 */       translateTo(classGen, methodGen, Type.NodeSet);
/* 410 */       int index = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "makeNode", "(Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;)Lorg/w3c/dom/Node;");
/*     */ 
/* 413 */       il.append(new INVOKEINTERFACE(index, 2));
/*     */     }
/* 415 */     else if (className.equals("org.w3c.dom.NodeList")) {
/* 416 */       translateTo(classGen, methodGen, Type.NodeSet);
/* 417 */       int index = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "makeNodeList", "(Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;)Lorg/w3c/dom/NodeList;");
/*     */ 
/* 420 */       il.append(new INVOKEINTERFACE(index, 2));
/*     */     }
/* 422 */     else if (className.equals("java.lang.Object")) {
/* 423 */       il.append(NOP);
/*     */     }
/* 425 */     else if (className.equals("java.lang.String")) {
/* 426 */       translateTo(classGen, methodGen, Type.String);
/*     */     }
/*     */     else {
/* 429 */       ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), className);
/*     */ 
/* 431 */       classGen.getParser().reportError(2, err);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void translateBox(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 440 */     translateTo(classGen, methodGen, Type.Reference);
/*     */   }
/*     */ 
/*     */   public void translateUnBox(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 448 */     methodGen.getInstructionList().append(NOP);
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 455 */     return "com.sun.org.apache.xalan.internal.xsltc.DOM";
/*     */   }
/*     */ 
/*     */   public Instruction LOAD(int slot) {
/* 459 */     return new ALOAD(slot);
/*     */   }
/*     */ 
/*     */   public Instruction STORE(int slot) {
/* 463 */     return new ASTORE(slot);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.util.ResultTreeType
 * JD-Core Version:    0.6.2
 */