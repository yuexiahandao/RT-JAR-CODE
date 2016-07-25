/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler.util;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.BranchHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.CHECKCAST;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.GETFIELD;
/*     */ import com.sun.org.apache.bcel.internal.generic.GOTO;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFEQ;
/*     */ import com.sun.org.apache.bcel.internal.generic.ILOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.ISTORE;
/*     */ import com.sun.org.apache.bcel.internal.generic.Instruction;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.NEW;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.FlowList;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.Parser;
/*     */ 
/*     */ public final class NodeType extends Type
/*     */ {
/*     */   private final int _type;
/*     */ 
/*     */   protected NodeType()
/*     */   {
/*  52 */     this(-1);
/*     */   }
/*     */ 
/*     */   protected NodeType(int type) {
/*  56 */     this._type = type;
/*     */   }
/*     */ 
/*     */   public int getType() {
/*  60 */     return this._type;
/*     */   }
/*     */ 
/*     */   public String toString() {
/*  64 */     return "node-type";
/*     */   }
/*     */ 
/*     */   public boolean identicalTo(Type other) {
/*  68 */     return other instanceof NodeType;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/*  72 */     return this._type;
/*     */   }
/*     */ 
/*     */   public String toSignature() {
/*  76 */     return "I";
/*     */   }
/*     */ 
/*     */   public com.sun.org.apache.bcel.internal.generic.Type toJCType() {
/*  80 */     return com.sun.org.apache.bcel.internal.generic.Type.INT;
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Type type)
/*     */   {
/*  92 */     if (type == Type.String) {
/*  93 */       translateTo(classGen, methodGen, (StringType)type);
/*     */     }
/*  95 */     else if (type == Type.Boolean) {
/*  96 */       translateTo(classGen, methodGen, (BooleanType)type);
/*     */     }
/*  98 */     else if (type == Type.Real) {
/*  99 */       translateTo(classGen, methodGen, (RealType)type);
/*     */     }
/* 101 */     else if (type == Type.NodeSet) {
/* 102 */       translateTo(classGen, methodGen, (NodeSetType)type);
/*     */     }
/* 104 */     else if (type == Type.Reference) {
/* 105 */       translateTo(classGen, methodGen, (ReferenceType)type);
/*     */     }
/* 107 */     else if (type == Type.Object) {
/* 108 */       translateTo(classGen, methodGen, (ObjectType)type);
/*     */     }
/*     */     else {
/* 111 */       ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), type.toString());
/*     */ 
/* 113 */       classGen.getParser().reportError(2, err);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, StringType type)
/*     */   {
/* 124 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 125 */     InstructionList il = methodGen.getInstructionList();
/*     */     int index;
/* 127 */     switch (this._type) {
/*     */     case 1:
/*     */     case 9:
/* 130 */       il.append(methodGen.loadDOM());
/* 131 */       il.append(SWAP);
/* 132 */       index = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getElementValue", "(I)Ljava/lang/String;");
/*     */ 
/* 135 */       il.append(new INVOKEINTERFACE(index, 2));
/* 136 */       break;
/*     */     case -1:
/*     */     case 2:
/*     */     case 7:
/*     */     case 8:
/* 142 */       il.append(methodGen.loadDOM());
/* 143 */       il.append(SWAP);
/* 144 */       index = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getStringValueX", "(I)Ljava/lang/String;");
/*     */ 
/* 147 */       il.append(new INVOKEINTERFACE(index, 2));
/* 148 */       break;
/*     */     case 0:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     default:
/* 151 */       ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), type.toString());
/*     */ 
/* 153 */       classGen.getParser().reportError(2, err);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type)
/*     */   {
/* 168 */     InstructionList il = methodGen.getInstructionList();
/* 169 */     FlowList falsel = translateToDesynthesized(classGen, methodGen, type);
/* 170 */     il.append(ICONST_1);
/* 171 */     BranchHandle truec = il.append(new GOTO(null));
/* 172 */     falsel.backPatch(il.append(ICONST_0));
/* 173 */     truec.setTarget(il.append(NOP));
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, RealType type)
/*     */   {
/* 184 */     translateTo(classGen, methodGen, Type.String);
/* 185 */     Type.String.translateTo(classGen, methodGen, Type.Real);
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, NodeSetType type)
/*     */   {
/* 196 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 197 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 200 */     il.append(new NEW(cpg.addClass("com.sun.org.apache.xalan.internal.xsltc.dom.SingletonIterator")));
/* 201 */     il.append(DUP_X1);
/* 202 */     il.append(SWAP);
/* 203 */     int init = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.dom.SingletonIterator", "<init>", "(I)V");
/*     */ 
/* 205 */     il.append(new INVOKESPECIAL(init));
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ObjectType type)
/*     */   {
/* 215 */     methodGen.getInstructionList().append(NOP);
/*     */   }
/*     */ 
/*     */   public FlowList translateToDesynthesized(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type)
/*     */   {
/* 228 */     InstructionList il = methodGen.getInstructionList();
/* 229 */     return new FlowList(il.append(new IFEQ(null)));
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ReferenceType type)
/*     */   {
/* 240 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 241 */     InstructionList il = methodGen.getInstructionList();
/* 242 */     il.append(new NEW(cpg.addClass("com.sun.org.apache.xalan.internal.xsltc.runtime.Node")));
/* 243 */     il.append(DUP_X1);
/* 244 */     il.append(SWAP);
/* 245 */     il.append(new PUSH(cpg, this._type));
/* 246 */     il.append(new INVOKESPECIAL(cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.Node", "<init>", "(II)V")));
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Class clazz)
/*     */   {
/* 257 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 258 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 260 */     String className = clazz.getName();
/* 261 */     if (className.equals("java.lang.String")) {
/* 262 */       translateTo(classGen, methodGen, Type.String);
/* 263 */       return;
/*     */     }
/*     */ 
/* 266 */     il.append(methodGen.loadDOM());
/* 267 */     il.append(SWAP);
/*     */ 
/* 269 */     if ((className.equals("org.w3c.dom.Node")) || (className.equals("java.lang.Object")))
/*     */     {
/* 271 */       int index = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "makeNode", "(I)Lorg/w3c/dom/Node;");
/*     */ 
/* 274 */       il.append(new INVOKEINTERFACE(index, 2));
/*     */     }
/* 276 */     else if (className.equals("org.w3c.dom.NodeList")) {
/* 277 */       int index = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "makeNodeList", "(I)Lorg/w3c/dom/NodeList;");
/*     */ 
/* 280 */       il.append(new INVOKEINTERFACE(index, 2));
/*     */     }
/*     */     else {
/* 283 */       ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), className);
/*     */ 
/* 285 */       classGen.getParser().reportError(2, err);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void translateBox(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 294 */     translateTo(classGen, methodGen, Type.Reference);
/*     */   }
/*     */ 
/*     */   public void translateUnBox(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 302 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 303 */     InstructionList il = methodGen.getInstructionList();
/* 304 */     il.append(new CHECKCAST(cpg.addClass("com.sun.org.apache.xalan.internal.xsltc.runtime.Node")));
/* 305 */     il.append(new GETFIELD(cpg.addFieldref("com.sun.org.apache.xalan.internal.xsltc.runtime.Node", "node", "I")));
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 314 */     return "com.sun.org.apache.xalan.internal.xsltc.runtime.Node";
/*     */   }
/*     */ 
/*     */   public Instruction LOAD(int slot) {
/* 318 */     return new ILOAD(slot);
/*     */   }
/*     */ 
/*     */   public Instruction STORE(int slot) {
/* 322 */     return new ISTORE(slot);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeType
 * JD-Core Version:    0.6.2
 */