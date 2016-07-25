/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler.util;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ALOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.ILOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.ISTORE;
/*     */ import com.sun.org.apache.bcel.internal.generic.Instruction;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.Type;
/*     */ 
/*     */ public final class MatchGenerator extends MethodGenerator
/*     */ {
/*  39 */   private static int CURRENT_INDEX = 1;
/*     */ 
/*  41 */   private int _iteratorIndex = -1;
/*     */   private final Instruction _iloadCurrent;
/*     */   private final Instruction _istoreCurrent;
/*     */   private Instruction _aloadDom;
/*     */ 
/*     */   public MatchGenerator(int access_flags, Type return_type, Type[] arg_types, String[] arg_names, String method_name, String class_name, InstructionList il, ConstantPoolGen cp)
/*     */   {
/*  51 */     super(access_flags, return_type, arg_types, arg_names, method_name, class_name, il, cp);
/*     */ 
/*  54 */     this._iloadCurrent = new ILOAD(CURRENT_INDEX);
/*  55 */     this._istoreCurrent = new ISTORE(CURRENT_INDEX);
/*     */   }
/*     */ 
/*     */   public Instruction loadCurrentNode() {
/*  59 */     return this._iloadCurrent;
/*     */   }
/*     */ 
/*     */   public Instruction storeCurrentNode() {
/*  63 */     return this._istoreCurrent;
/*     */   }
/*     */ 
/*     */   public int getHandlerIndex() {
/*  67 */     return -1;
/*     */   }
/*     */ 
/*     */   public Instruction loadDOM()
/*     */   {
/*  74 */     return this._aloadDom;
/*     */   }
/*     */ 
/*     */   public void setDomIndex(int domIndex)
/*     */   {
/*  81 */     this._aloadDom = new ALOAD(domIndex);
/*     */   }
/*     */ 
/*     */   public int getIteratorIndex()
/*     */   {
/*  88 */     return this._iteratorIndex;
/*     */   }
/*     */ 
/*     */   public void setIteratorIndex(int iteratorIndex)
/*     */   {
/*  95 */     this._iteratorIndex = iteratorIndex;
/*     */   }
/*     */ 
/*     */   public int getLocalIndex(String name) {
/*  99 */     if (name.equals("current")) {
/* 100 */       return CURRENT_INDEX;
/*     */     }
/* 102 */     return super.getLocalIndex(name);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.util.MatchGenerator
 * JD-Core Version:    0.6.2
 */