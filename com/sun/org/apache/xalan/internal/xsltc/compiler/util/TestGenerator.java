/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler.util;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ALOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.ASTORE;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.ILOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.ISTORE;
/*     */ import com.sun.org.apache.bcel.internal.generic.Instruction;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.Type;
/*     */ 
/*     */ public final class TestGenerator extends MethodGenerator
/*     */ {
/*  41 */   private static int CONTEXT_NODE_INDEX = 1;
/*  42 */   private static int CURRENT_NODE_INDEX = 4;
/*  43 */   private static int ITERATOR_INDEX = 6;
/*     */   private Instruction _aloadDom;
/*     */   private final Instruction _iloadCurrent;
/*     */   private final Instruction _iloadContext;
/*     */   private final Instruction _istoreCurrent;
/*     */   private final Instruction _istoreContext;
/*     */   private final Instruction _astoreIterator;
/*     */   private final Instruction _aloadIterator;
/*     */ 
/*     */   public TestGenerator(int access_flags, Type return_type, Type[] arg_types, String[] arg_names, String method_name, String class_name, InstructionList il, ConstantPoolGen cp)
/*     */   {
/*  57 */     super(access_flags, return_type, arg_types, arg_names, method_name, class_name, il, cp);
/*     */ 
/*  60 */     this._iloadCurrent = new ILOAD(CURRENT_NODE_INDEX);
/*  61 */     this._istoreCurrent = new ISTORE(CURRENT_NODE_INDEX);
/*  62 */     this._iloadContext = new ILOAD(CONTEXT_NODE_INDEX);
/*  63 */     this._istoreContext = new ILOAD(CONTEXT_NODE_INDEX);
/*  64 */     this._astoreIterator = new ASTORE(ITERATOR_INDEX);
/*  65 */     this._aloadIterator = new ALOAD(ITERATOR_INDEX);
/*     */   }
/*     */ 
/*     */   public int getHandlerIndex() {
/*  69 */     return -1;
/*     */   }
/*     */ 
/*     */   public int getIteratorIndex() {
/*  73 */     return ITERATOR_INDEX;
/*     */   }
/*     */ 
/*     */   public void setDomIndex(int domIndex) {
/*  77 */     this._aloadDom = new ALOAD(domIndex);
/*     */   }
/*     */ 
/*     */   public Instruction loadDOM() {
/*  81 */     return this._aloadDom;
/*     */   }
/*     */ 
/*     */   public Instruction loadCurrentNode() {
/*  85 */     return this._iloadCurrent;
/*     */   }
/*     */ 
/*     */   public Instruction loadContextNode()
/*     */   {
/*  90 */     return this._iloadContext;
/*     */   }
/*     */ 
/*     */   public Instruction storeContextNode() {
/*  94 */     return this._istoreContext;
/*     */   }
/*     */ 
/*     */   public Instruction storeCurrentNode() {
/*  98 */     return this._istoreCurrent;
/*     */   }
/*     */ 
/*     */   public Instruction storeIterator() {
/* 102 */     return this._astoreIterator;
/*     */   }
/*     */ 
/*     */   public Instruction loadIterator() {
/* 106 */     return this._aloadIterator;
/*     */   }
/*     */ 
/*     */   public int getLocalIndex(String name) {
/* 110 */     if (name.equals("current")) {
/* 111 */       return CURRENT_NODE_INDEX;
/*     */     }
/*     */ 
/* 114 */     return super.getLocalIndex(name);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.util.TestGenerator
 * JD-Core Version:    0.6.2
 */