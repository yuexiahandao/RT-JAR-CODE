/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler.util;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ACONST_NULL;
/*     */ import com.sun.org.apache.bcel.internal.generic.ALOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.ASTORE;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.ILOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.ISTORE;
/*     */ import com.sun.org.apache.bcel.internal.generic.Instruction;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.Type;
/*     */ 
/*     */ public final class CompareGenerator extends MethodGenerator
/*     */ {
/*  44 */   private static int DOM_INDEX = 1;
/*  45 */   private static int CURRENT_INDEX = 2;
/*  46 */   private static int LEVEL_INDEX = 3;
/*  47 */   private static int TRANSLET_INDEX = 4;
/*  48 */   private static int LAST_INDEX = 5;
/*  49 */   private int ITERATOR_INDEX = 6;
/*     */   private final Instruction _iloadCurrent;
/*     */   private final Instruction _istoreCurrent;
/*     */   private final Instruction _aloadDom;
/*     */   private final Instruction _iloadLast;
/*     */   private final Instruction _aloadIterator;
/*     */   private final Instruction _astoreIterator;
/*     */ 
/*     */   public CompareGenerator(int access_flags, Type return_type, Type[] arg_types, String[] arg_names, String method_name, String class_name, InstructionList il, ConstantPoolGen cp)
/*     */   {
/*  62 */     super(access_flags, return_type, arg_types, arg_names, method_name, class_name, il, cp);
/*     */ 
/*  65 */     this._iloadCurrent = new ILOAD(CURRENT_INDEX);
/*  66 */     this._istoreCurrent = new ISTORE(CURRENT_INDEX);
/*  67 */     this._aloadDom = new ALOAD(DOM_INDEX);
/*  68 */     this._iloadLast = new ILOAD(LAST_INDEX);
/*     */ 
/*  70 */     LocalVariableGen iterator = addLocalVariable("iterator", Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), null, null);
/*     */ 
/*  74 */     this.ITERATOR_INDEX = iterator.getIndex();
/*  75 */     this._aloadIterator = new ALOAD(this.ITERATOR_INDEX);
/*  76 */     this._astoreIterator = new ASTORE(this.ITERATOR_INDEX);
/*  77 */     il.append(new ACONST_NULL());
/*  78 */     il.append(storeIterator());
/*     */   }
/*     */ 
/*     */   public Instruction loadLastNode() {
/*  82 */     return this._iloadLast;
/*     */   }
/*     */ 
/*     */   public Instruction loadCurrentNode() {
/*  86 */     return this._iloadCurrent;
/*     */   }
/*     */ 
/*     */   public Instruction storeCurrentNode() {
/*  90 */     return this._istoreCurrent;
/*     */   }
/*     */ 
/*     */   public Instruction loadDOM() {
/*  94 */     return this._aloadDom;
/*     */   }
/*     */ 
/*     */   public int getHandlerIndex() {
/*  98 */     return -1;
/*     */   }
/*     */ 
/*     */   public int getIteratorIndex() {
/* 102 */     return -1;
/*     */   }
/*     */ 
/*     */   public Instruction storeIterator() {
/* 106 */     return this._astoreIterator;
/*     */   }
/*     */ 
/*     */   public Instruction loadIterator() {
/* 110 */     return this._aloadIterator;
/*     */   }
/*     */ 
/*     */   public int getLocalIndex(String name)
/*     */   {
/* 115 */     if (name.equals("current")) {
/* 116 */       return CURRENT_INDEX;
/*     */     }
/* 118 */     return super.getLocalIndex(name);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.util.CompareGenerator
 * JD-Core Version:    0.6.2
 */