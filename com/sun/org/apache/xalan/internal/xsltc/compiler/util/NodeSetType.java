/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler.util;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ALOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.ASTORE;
/*     */ import com.sun.org.apache.bcel.internal.generic.BranchHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.GOTO;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFLT;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
/*     */ import com.sun.org.apache.bcel.internal.generic.Instruction;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.FlowList;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.Parser;
/*     */ 
/*     */ public final class NodeSetType extends Type
/*     */ {
/*     */   public String toString()
/*     */   {
/*  48 */     return "node-set";
/*     */   }
/*     */ 
/*     */   public boolean identicalTo(Type other) {
/*  52 */     return this == other;
/*     */   }
/*     */ 
/*     */   public String toSignature() {
/*  56 */     return "Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;";
/*     */   }
/*     */ 
/*     */   public com.sun.org.apache.bcel.internal.generic.Type toJCType() {
/*  60 */     return new com.sun.org.apache.bcel.internal.generic.ObjectType("com.sun.org.apache.xml.internal.dtm.DTMAxisIterator");
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Type type)
/*     */   {
/*  73 */     if (type == Type.String) {
/*  74 */       translateTo(classGen, methodGen, (StringType)type);
/*     */     }
/*  76 */     else if (type == Type.Boolean) {
/*  77 */       translateTo(classGen, methodGen, (BooleanType)type);
/*     */     }
/*  79 */     else if (type == Type.Real) {
/*  80 */       translateTo(classGen, methodGen, (RealType)type);
/*     */     }
/*  82 */     else if (type == Type.Node) {
/*  83 */       translateTo(classGen, methodGen, (NodeType)type);
/*     */     }
/*  85 */     else if (type == Type.Reference) {
/*  86 */       translateTo(classGen, methodGen, (ReferenceType)type);
/*     */     }
/*  88 */     else if (type == Type.Object) {
/*  89 */       translateTo(classGen, methodGen, (ObjectType)type);
/*     */     }
/*     */     else {
/*  92 */       ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), type.toString());
/*     */ 
/*  94 */       classGen.getParser().reportError(2, err);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void translateFrom(ClassGenerator classGen, MethodGenerator methodGen, Class clazz)
/*     */   {
/* 106 */     InstructionList il = methodGen.getInstructionList();
/* 107 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 108 */     if (clazz.getName().equals("org.w3c.dom.NodeList"))
/*     */     {
/* 112 */       il.append(classGen.loadTranslet());
/* 113 */       il.append(methodGen.loadDOM());
/* 114 */       int convert = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "nodeList2Iterator", "(Lorg/w3c/dom/NodeList;Lcom/sun/org/apache/xalan/internal/xsltc/Translet;Lcom/sun/org/apache/xalan/internal/xsltc/DOM;)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*     */ 
/* 121 */       il.append(new INVOKESTATIC(convert));
/*     */     }
/* 123 */     else if (clazz.getName().equals("org.w3c.dom.Node"))
/*     */     {
/* 127 */       il.append(classGen.loadTranslet());
/* 128 */       il.append(methodGen.loadDOM());
/* 129 */       int convert = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "node2Iterator", "(Lorg/w3c/dom/Node;Lcom/sun/org/apache/xalan/internal/xsltc/Translet;Lcom/sun/org/apache/xalan/internal/xsltc/DOM;)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*     */ 
/* 136 */       il.append(new INVOKESTATIC(convert));
/*     */     }
/*     */     else {
/* 139 */       ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), clazz.getName());
/*     */ 
/* 141 */       classGen.getParser().reportError(2, err);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type)
/*     */   {
/* 156 */     InstructionList il = methodGen.getInstructionList();
/* 157 */     FlowList falsel = translateToDesynthesized(classGen, methodGen, type);
/* 158 */     il.append(ICONST_1);
/* 159 */     BranchHandle truec = il.append(new GOTO(null));
/* 160 */     falsel.backPatch(il.append(ICONST_0));
/* 161 */     truec.setTarget(il.append(NOP));
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, StringType type)
/*     */   {
/* 172 */     InstructionList il = methodGen.getInstructionList();
/* 173 */     getFirstNode(classGen, methodGen);
/* 174 */     il.append(DUP);
/* 175 */     BranchHandle falsec = il.append(new IFLT(null));
/* 176 */     Type.Node.translateTo(classGen, methodGen, type);
/* 177 */     BranchHandle truec = il.append(new GOTO(null));
/* 178 */     falsec.setTarget(il.append(POP));
/* 179 */     il.append(new PUSH(classGen.getConstantPool(), ""));
/* 180 */     truec.setTarget(il.append(NOP));
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, RealType type)
/*     */   {
/* 191 */     translateTo(classGen, methodGen, Type.String);
/* 192 */     Type.String.translateTo(classGen, methodGen, Type.Real);
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, NodeType type)
/*     */   {
/* 202 */     getFirstNode(classGen, methodGen);
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ObjectType type)
/*     */   {
/* 212 */     methodGen.getInstructionList().append(NOP);
/*     */   }
/*     */ 
/*     */   public FlowList translateToDesynthesized(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type)
/*     */   {
/* 225 */     InstructionList il = methodGen.getInstructionList();
/* 226 */     getFirstNode(classGen, methodGen);
/* 227 */     return new FlowList(il.append(new IFLT(null)));
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ReferenceType type)
/*     */   {
/* 238 */     methodGen.getInstructionList().append(NOP);
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Class clazz)
/*     */   {
/* 248 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 249 */     InstructionList il = methodGen.getInstructionList();
/* 250 */     String className = clazz.getName();
/*     */ 
/* 252 */     il.append(methodGen.loadDOM());
/* 253 */     il.append(SWAP);
/*     */ 
/* 255 */     if (className.equals("org.w3c.dom.Node")) {
/* 256 */       int index = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "makeNode", "(Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;)Lorg/w3c/dom/Node;");
/*     */ 
/* 259 */       il.append(new INVOKEINTERFACE(index, 2));
/*     */     }
/* 261 */     else if ((className.equals("org.w3c.dom.NodeList")) || (className.equals("java.lang.Object")))
/*     */     {
/* 263 */       int index = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "makeNodeList", "(Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;)Lorg/w3c/dom/NodeList;");
/*     */ 
/* 266 */       il.append(new INVOKEINTERFACE(index, 2));
/*     */     }
/* 268 */     else if (className.equals("java.lang.String")) {
/* 269 */       int next = cpg.addInterfaceMethodref("com.sun.org.apache.xml.internal.dtm.DTMAxisIterator", "next", "()I");
/*     */ 
/* 271 */       int index = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getStringValueX", "(I)Ljava/lang/String;");
/*     */ 
/* 276 */       il.append(new INVOKEINTERFACE(next, 1));
/*     */ 
/* 278 */       il.append(new INVOKEINTERFACE(index, 2));
/*     */     }
/*     */     else
/*     */     {
/* 282 */       ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), className);
/*     */ 
/* 284 */       classGen.getParser().reportError(2, err);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void getFirstNode(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 293 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 294 */     InstructionList il = methodGen.getInstructionList();
/* 295 */     il.append(new INVOKEINTERFACE(cpg.addInterfaceMethodref("com.sun.org.apache.xml.internal.dtm.DTMAxisIterator", "next", "()I"), 1));
/*     */   }
/*     */ 
/*     */   public void translateBox(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 305 */     translateTo(classGen, methodGen, Type.Reference);
/*     */   }
/*     */ 
/*     */   public void translateUnBox(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 313 */     methodGen.getInstructionList().append(NOP);
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 320 */     return "com.sun.org.apache.xml.internal.dtm.DTMAxisIterator";
/*     */   }
/*     */ 
/*     */   public Instruction LOAD(int slot)
/*     */   {
/* 325 */     return new ALOAD(slot);
/*     */   }
/*     */ 
/*     */   public Instruction STORE(int slot) {
/* 329 */     return new ASTORE(slot);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeSetType
 * JD-Core Version:    0.6.2
 */