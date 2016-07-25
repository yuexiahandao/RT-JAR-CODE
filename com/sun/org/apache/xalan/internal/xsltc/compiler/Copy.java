/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ALOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.ASTORE;
/*     */ import com.sun.org.apache.bcel.internal.generic.BranchHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFEQ;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFNULL;
/*     */ import com.sun.org.apache.bcel.internal.generic.ILOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.ISTORE;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ 
/*     */ final class Copy extends Instruction
/*     */ {
/*     */   private UseAttributeSets _useSets;
/*     */ 
/*     */   public void parseContents(Parser parser)
/*     */   {
/*  54 */     String useSets = getAttribute("use-attribute-sets");
/*  55 */     if (useSets.length() > 0) {
/*  56 */       if (!Util.isValidQNames(useSets)) {
/*  57 */         ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", useSets, this);
/*  58 */         parser.reportError(3, err);
/*     */       }
/*  60 */       this._useSets = new UseAttributeSets(useSets, parser);
/*     */     }
/*  62 */     parseChildren(parser);
/*     */   }
/*     */ 
/*     */   public void display(int indent) {
/*  66 */     indent(indent);
/*  67 */     Util.println("Copy");
/*  68 */     indent(indent + 4);
/*  69 */     displayContents(indent + 4);
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
/*  73 */     if (this._useSets != null) {
/*  74 */       this._useSets.typeCheck(stable);
/*     */     }
/*  76 */     typeCheckContents(stable);
/*  77 */     return Type.Void;
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/*  81 */     ConstantPoolGen cpg = classGen.getConstantPool();
/*  82 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/*  84 */     LocalVariableGen name = methodGen.addLocalVariable2("name", Util.getJCRefType("Ljava/lang/String;"), null);
/*     */ 
/*  88 */     LocalVariableGen length = methodGen.addLocalVariable2("length", Util.getJCRefType("I"), null);
/*     */ 
/*  94 */     il.append(methodGen.loadDOM());
/*  95 */     il.append(methodGen.loadCurrentNode());
/*  96 */     il.append(methodGen.loadHandler());
/*  97 */     int cpy = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "shallowCopy", "(ILcom/sun/org/apache/xml/internal/serializer/SerializationHandler;)Ljava/lang/String;");
/*     */ 
/* 103 */     il.append(new INVOKEINTERFACE(cpy, 3));
/* 104 */     il.append(DUP);
/* 105 */     name.setStart(il.append(new ASTORE(name.getIndex())));
/* 106 */     BranchHandle ifBlock1 = il.append(new IFNULL(null));
/*     */ 
/* 109 */     il.append(new ALOAD(name.getIndex()));
/* 110 */     int lengthMethod = cpg.addMethodref("java.lang.String", "length", "()I");
/* 111 */     il.append(new INVOKEVIRTUAL(lengthMethod));
/* 112 */     il.append(DUP);
/* 113 */     length.setStart(il.append(new ISTORE(length.getIndex())));
/*     */ 
/* 117 */     BranchHandle ifBlock4 = il.append(new IFEQ(null));
/*     */ 
/* 120 */     if (this._useSets != null)
/*     */     {
/* 123 */       SyntaxTreeNode parent = getParent();
/* 124 */       if (((parent instanceof LiteralElement)) || ((parent instanceof LiteralElement)))
/*     */       {
/* 126 */         this._useSets.translate(classGen, methodGen);
/*     */       }
/*     */       else
/*     */       {
/* 132 */         il.append(new ILOAD(length.getIndex()));
/* 133 */         BranchHandle ifBlock2 = il.append(new IFEQ(null));
/*     */ 
/* 135 */         this._useSets.translate(classGen, methodGen);
/*     */ 
/* 137 */         ifBlock2.setTarget(il.append(NOP));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 142 */     ifBlock4.setTarget(il.append(NOP));
/* 143 */     translateContents(classGen, methodGen);
/*     */ 
/* 147 */     length.setEnd(il.append(new ILOAD(length.getIndex())));
/* 148 */     BranchHandle ifBlock3 = il.append(new IFEQ(null));
/* 149 */     il.append(methodGen.loadHandler());
/* 150 */     name.setEnd(il.append(new ALOAD(name.getIndex())));
/* 151 */     il.append(methodGen.endElement());
/*     */ 
/* 153 */     InstructionHandle end = il.append(NOP);
/* 154 */     ifBlock1.setTarget(end);
/* 155 */     ifBlock3.setTarget(end);
/* 156 */     methodGen.removeLocalVariable(name);
/* 157 */     methodGen.removeLocalVariable(length);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.Copy
 * JD-Core Version:    0.6.2
 */