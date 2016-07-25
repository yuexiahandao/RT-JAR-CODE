/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler.util;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ALOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.ASTORE;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFEQ;
/*     */ import com.sun.org.apache.bcel.internal.generic.ILOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
/*     */ import com.sun.org.apache.bcel.internal.generic.Instruction;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.FlowList;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.Parser;
/*     */ 
/*     */ public final class ReferenceType extends Type
/*     */ {
/*     */   public String toString()
/*     */   {
/*  51 */     return "reference";
/*     */   }
/*     */ 
/*     */   public boolean identicalTo(Type other) {
/*  55 */     return this == other;
/*     */   }
/*     */ 
/*     */   public String toSignature() {
/*  59 */     return "Ljava/lang/Object;";
/*     */   }
/*     */ 
/*     */   public com.sun.org.apache.bcel.internal.generic.Type toJCType() {
/*  63 */     return com.sun.org.apache.bcel.internal.generic.Type.OBJECT;
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Type type)
/*     */   {
/*  75 */     if (type == Type.String) {
/*  76 */       translateTo(classGen, methodGen, (StringType)type);
/*     */     }
/*  78 */     else if (type == Type.Real) {
/*  79 */       translateTo(classGen, methodGen, (RealType)type);
/*     */     }
/*  81 */     else if (type == Type.Boolean) {
/*  82 */       translateTo(classGen, methodGen, (BooleanType)type);
/*     */     }
/*  84 */     else if (type == Type.NodeSet) {
/*  85 */       translateTo(classGen, methodGen, (NodeSetType)type);
/*     */     }
/*  87 */     else if (type == Type.Node) {
/*  88 */       translateTo(classGen, methodGen, (NodeType)type);
/*     */     }
/*  90 */     else if (type == Type.ResultTree) {
/*  91 */       translateTo(classGen, methodGen, (ResultTreeType)type);
/*     */     }
/*  93 */     else if (type == Type.Object) {
/*  94 */       translateTo(classGen, methodGen, (ObjectType)type);
/*     */     }
/*  96 */     else if (type != Type.Reference)
/*     */     {
/*  99 */       ErrorMsg err = new ErrorMsg("INTERNAL_ERR", type.toString());
/* 100 */       classGen.getParser().reportError(2, err);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, StringType type)
/*     */   {
/* 111 */     int current = methodGen.getLocalIndex("current");
/* 112 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 113 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 116 */     if (current < 0) {
/* 117 */       il.append(new PUSH(cpg, 0));
/*     */     }
/*     */     else {
/* 120 */       il.append(new ILOAD(current));
/*     */     }
/* 122 */     il.append(methodGen.loadDOM());
/* 123 */     int stringF = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "stringF", "(Ljava/lang/Object;ILcom/sun/org/apache/xalan/internal/xsltc/DOM;)Ljava/lang/String;");
/*     */ 
/* 130 */     il.append(new INVOKESTATIC(stringF));
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, RealType type)
/*     */   {
/* 140 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 141 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 143 */     il.append(methodGen.loadDOM());
/* 144 */     int index = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "numberF", "(Ljava/lang/Object;Lcom/sun/org/apache/xalan/internal/xsltc/DOM;)D");
/*     */ 
/* 149 */     il.append(new INVOKESTATIC(index));
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type)
/*     */   {
/* 159 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 160 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 162 */     int index = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "booleanF", "(Ljava/lang/Object;)Z");
/*     */ 
/* 166 */     il.append(new INVOKESTATIC(index));
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, NodeSetType type)
/*     */   {
/* 176 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 177 */     InstructionList il = methodGen.getInstructionList();
/* 178 */     int index = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "referenceToNodeSet", "(Ljava/lang/Object;)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*     */ 
/* 183 */     il.append(new INVOKESTATIC(index));
/*     */ 
/* 186 */     index = cpg.addInterfaceMethodref("com.sun.org.apache.xml.internal.dtm.DTMAxisIterator", "reset", "()Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/* 187 */     il.append(new INVOKEINTERFACE(index, 1));
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, NodeType type)
/*     */   {
/* 197 */     translateTo(classGen, methodGen, Type.NodeSet);
/* 198 */     Type.NodeSet.translateTo(classGen, methodGen, type);
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ResultTreeType type)
/*     */   {
/* 208 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 209 */     InstructionList il = methodGen.getInstructionList();
/* 210 */     int index = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "referenceToResultTree", "(Ljava/lang/Object;)Lcom/sun/org/apache/xalan/internal/xsltc/DOM;");
/*     */ 
/* 212 */     il.append(new INVOKESTATIC(index));
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ObjectType type)
/*     */   {
/* 222 */     methodGen.getInstructionList().append(NOP);
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Class clazz)
/*     */   {
/* 230 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 231 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 233 */     int referenceToLong = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "referenceToLong", "(Ljava/lang/Object;)J");
/*     */ 
/* 236 */     int referenceToDouble = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "referenceToDouble", "(Ljava/lang/Object;)D");
/*     */ 
/* 239 */     int referenceToBoolean = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "referenceToBoolean", "(Ljava/lang/Object;)Z");
/*     */ 
/* 243 */     if (clazz.getName().equals("java.lang.Object")) {
/* 244 */       il.append(NOP);
/*     */     }
/* 246 */     else if (clazz == Double.TYPE) {
/* 247 */       il.append(new INVOKESTATIC(referenceToDouble));
/*     */     }
/* 249 */     else if (clazz.getName().equals("java.lang.Double")) {
/* 250 */       il.append(new INVOKESTATIC(referenceToDouble));
/* 251 */       Type.Real.translateTo(classGen, methodGen, Type.Reference);
/*     */     }
/* 253 */     else if (clazz == Float.TYPE) {
/* 254 */       il.append(new INVOKESTATIC(referenceToDouble));
/* 255 */       il.append(D2F);
/*     */     }
/* 257 */     else if (clazz.getName().equals("java.lang.String")) {
/* 258 */       int index = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "referenceToString", "(Ljava/lang/Object;Lcom/sun/org/apache/xalan/internal/xsltc/DOM;)Ljava/lang/String;");
/*     */ 
/* 264 */       il.append(methodGen.loadDOM());
/* 265 */       il.append(new INVOKESTATIC(index));
/*     */     }
/* 267 */     else if (clazz.getName().equals("org.w3c.dom.Node")) {
/* 268 */       int index = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "referenceToNode", "(Ljava/lang/Object;Lcom/sun/org/apache/xalan/internal/xsltc/DOM;)Lorg/w3c/dom/Node;");
/*     */ 
/* 274 */       il.append(methodGen.loadDOM());
/* 275 */       il.append(new INVOKESTATIC(index));
/*     */     }
/* 277 */     else if (clazz.getName().equals("org.w3c.dom.NodeList")) {
/* 278 */       int index = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "referenceToNodeList", "(Ljava/lang/Object;Lcom/sun/org/apache/xalan/internal/xsltc/DOM;)Lorg/w3c/dom/NodeList;");
/*     */ 
/* 284 */       il.append(methodGen.loadDOM());
/* 285 */       il.append(new INVOKESTATIC(index));
/*     */     }
/* 287 */     else if (clazz.getName().equals("com.sun.org.apache.xalan.internal.xsltc.DOM")) {
/* 288 */       translateTo(classGen, methodGen, Type.ResultTree);
/*     */     }
/* 290 */     else if (clazz == Long.TYPE) {
/* 291 */       il.append(new INVOKESTATIC(referenceToLong));
/*     */     }
/* 293 */     else if (clazz == Integer.TYPE) {
/* 294 */       il.append(new INVOKESTATIC(referenceToLong));
/* 295 */       il.append(L2I);
/*     */     }
/* 297 */     else if (clazz == Short.TYPE) {
/* 298 */       il.append(new INVOKESTATIC(referenceToLong));
/* 299 */       il.append(L2I);
/* 300 */       il.append(I2S);
/*     */     }
/* 302 */     else if (clazz == Byte.TYPE) {
/* 303 */       il.append(new INVOKESTATIC(referenceToLong));
/* 304 */       il.append(L2I);
/* 305 */       il.append(I2B);
/*     */     }
/* 307 */     else if (clazz == Character.TYPE) {
/* 308 */       il.append(new INVOKESTATIC(referenceToLong));
/* 309 */       il.append(L2I);
/* 310 */       il.append(I2C);
/*     */     }
/* 312 */     else if (clazz == Boolean.TYPE) {
/* 313 */       il.append(new INVOKESTATIC(referenceToBoolean));
/*     */     }
/* 315 */     else if (clazz.getName().equals("java.lang.Boolean")) {
/* 316 */       il.append(new INVOKESTATIC(referenceToBoolean));
/* 317 */       Type.Boolean.translateTo(classGen, methodGen, Type.Reference);
/*     */     }
/*     */     else {
/* 320 */       ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), clazz.getName());
/*     */ 
/* 322 */       classGen.getParser().reportError(2, err);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void translateFrom(ClassGenerator classGen, MethodGenerator methodGen, Class clazz)
/*     */   {
/* 332 */     if (clazz.getName().equals("java.lang.Object")) {
/* 333 */       methodGen.getInstructionList().append(NOP);
/*     */     }
/*     */     else {
/* 336 */       ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), clazz.getName());
/*     */ 
/* 338 */       classGen.getParser().reportError(2, err);
/*     */     }
/*     */   }
/*     */ 
/*     */   public FlowList translateToDesynthesized(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type)
/*     */   {
/* 352 */     InstructionList il = methodGen.getInstructionList();
/* 353 */     translateTo(classGen, methodGen, type);
/* 354 */     return new FlowList(il.append(new IFEQ(null)));
/*     */   }
/*     */ 
/*     */   public void translateBox(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void translateUnBox(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/*     */   }
/*     */ 
/*     */   public Instruction LOAD(int slot)
/*     */   {
/* 373 */     return new ALOAD(slot);
/*     */   }
/*     */ 
/*     */   public Instruction STORE(int slot) {
/* 377 */     return new ASTORE(slot);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.util.ReferenceType
 * JD-Core Version:    0.6.2
 */