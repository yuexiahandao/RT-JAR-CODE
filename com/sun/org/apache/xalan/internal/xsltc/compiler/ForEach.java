/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.BranchHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.GOTO;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFGT;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeSetType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ReferenceType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ResultTreeType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class ForEach extends Instruction
/*     */ {
/*     */   private Expression _select;
/*     */   private Type _type;
/*     */ 
/*     */   public void display(int indent)
/*     */   {
/*  57 */     indent(indent);
/*  58 */     Util.println("ForEach");
/*  59 */     indent(indent + 4);
/*  60 */     Util.println("select " + this._select.toString());
/*  61 */     displayContents(indent + 4);
/*     */   }
/*     */ 
/*     */   public void parseContents(Parser parser) {
/*  65 */     this._select = parser.parseExpression(this, "select", null);
/*     */ 
/*  67 */     parseChildren(parser);
/*     */ 
/*  70 */     if (this._select.isDummy())
/*  71 */       reportError(this, parser, "REQUIRED_ATTR_ERR", "select");
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable) throws TypeCheckError
/*     */   {
/*  76 */     this._type = this._select.typeCheck(stable);
/*     */ 
/*  78 */     if (((this._type instanceof ReferenceType)) || ((this._type instanceof NodeType))) {
/*  79 */       this._select = new CastExpr(this._select, Type.NodeSet);
/*  80 */       typeCheckContents(stable);
/*  81 */       return Type.Void;
/*     */     }
/*  83 */     if (((this._type instanceof NodeSetType)) || ((this._type instanceof ResultTreeType))) {
/*  84 */       typeCheckContents(stable);
/*  85 */       return Type.Void;
/*     */     }
/*  87 */     throw new TypeCheckError(this);
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/*  91 */     ConstantPoolGen cpg = classGen.getConstantPool();
/*  92 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/*  95 */     il.append(methodGen.loadCurrentNode());
/*  96 */     il.append(methodGen.loadIterator());
/*     */ 
/*  99 */     Vector sortObjects = new Vector();
/* 100 */     Enumeration children = elements();
/* 101 */     while (children.hasMoreElements()) {
/* 102 */       Object child = children.nextElement();
/* 103 */       if ((child instanceof Sort)) {
/* 104 */         sortObjects.addElement(child);
/*     */       }
/*     */     }
/*     */ 
/* 108 */     if ((this._type != null) && ((this._type instanceof ResultTreeType)))
/*     */     {
/* 110 */       il.append(methodGen.loadDOM());
/*     */ 
/* 113 */       if (sortObjects.size() > 0) {
/* 114 */         ErrorMsg msg = new ErrorMsg("RESULT_TREE_SORT_ERR", this);
/* 115 */         getParser().reportError(4, msg);
/*     */       }
/*     */ 
/* 119 */       this._select.translate(classGen, methodGen);
/*     */ 
/* 121 */       this._type.translateTo(classGen, methodGen, Type.NodeSet);
/*     */ 
/* 123 */       il.append(SWAP);
/* 124 */       il.append(methodGen.storeDOM());
/*     */     }
/*     */     else
/*     */     {
/* 128 */       if (sortObjects.size() > 0) {
/* 129 */         Sort.translateSortIterator(classGen, methodGen, this._select, sortObjects);
/*     */       }
/*     */       else
/*     */       {
/* 133 */         this._select.translate(classGen, methodGen);
/*     */       }
/*     */ 
/* 136 */       if (!(this._type instanceof ReferenceType)) {
/* 137 */         il.append(methodGen.loadContextNode());
/* 138 */         il.append(methodGen.setStartNode());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 144 */     il.append(methodGen.storeIterator());
/*     */ 
/* 147 */     initializeVariables(classGen, methodGen);
/*     */ 
/* 149 */     BranchHandle nextNode = il.append(new GOTO(null));
/* 150 */     InstructionHandle loop = il.append(NOP);
/*     */ 
/* 152 */     translateContents(classGen, methodGen);
/*     */ 
/* 154 */     nextNode.setTarget(il.append(methodGen.loadIterator()));
/* 155 */     il.append(methodGen.nextNode());
/* 156 */     il.append(DUP);
/* 157 */     il.append(methodGen.storeCurrentNode());
/* 158 */     il.append(new IFGT(loop));
/*     */ 
/* 161 */     if ((this._type != null) && ((this._type instanceof ResultTreeType))) {
/* 162 */       il.append(methodGen.storeDOM());
/*     */     }
/*     */ 
/* 166 */     il.append(methodGen.storeIterator());
/* 167 */     il.append(methodGen.storeCurrentNode());
/*     */   }
/*     */ 
/*     */   public void initializeVariables(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 188 */     int n = elementCount();
/* 189 */     for (int i = 0; i < n; i++) {
/* 190 */       Object child = getContents().elementAt(i);
/* 191 */       if ((child instanceof Variable)) {
/* 192 */         Variable var = (Variable)child;
/* 193 */         var.initialize(classGen, methodGen);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.ForEach
 * JD-Core Version:    0.6.2
 */