/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ 
/*     */ public abstract class LocationPathPattern extends Pattern
/*     */ {
/*     */   private Template _template;
/*     */   private int _importPrecedence;
/*  40 */   private double _priority = (0.0D / 0.0D);
/*  41 */   private int _position = 0;
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
/*  44 */     return Type.Void;
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setTemplate(Template template) {
/*  52 */     this._template = template;
/*  53 */     this._priority = template.getPriority();
/*  54 */     this._importPrecedence = template.getImportPrecedence();
/*  55 */     this._position = template.getPosition();
/*     */   }
/*     */ 
/*     */   public Template getTemplate() {
/*  59 */     return this._template;
/*     */   }
/*     */ 
/*     */   public final double getPriority() {
/*  63 */     return Double.isNaN(this._priority) ? getDefaultPriority() : this._priority;
/*     */   }
/*     */ 
/*     */   public double getDefaultPriority() {
/*  67 */     return 0.5D;
/*     */   }
/*     */ 
/*     */   public boolean noSmallerThan(LocationPathPattern other)
/*     */   {
/*  79 */     if (this._importPrecedence > other._importPrecedence) {
/*  80 */       return true;
/*     */     }
/*  82 */     if (this._importPrecedence == other._importPrecedence) {
/*  83 */       if (this._priority > other._priority) {
/*  84 */         return true;
/*     */       }
/*  86 */       if ((this._priority == other._priority) && 
/*  87 */         (this._position > other._position)) {
/*  88 */         return true;
/*     */       }
/*     */     }
/*     */ 
/*  92 */     return false;
/*     */   }
/*     */ 
/*     */   public abstract StepPattern getKernelPattern();
/*     */ 
/*     */   public abstract void reduceKernelPattern();
/*     */ 
/*     */   public abstract boolean isWildcard();
/*     */ 
/*     */   public int getAxis() {
/* 102 */     StepPattern sp = getKernelPattern();
/* 103 */     return sp != null ? sp.getAxis() : 3;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 107 */     return "root()";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.LocationPathPattern
 * JD-Core Version:    0.6.2
 */