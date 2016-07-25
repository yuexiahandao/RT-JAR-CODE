/*     */ package com.sun.java_cup.internal.runtime;
/*     */ 
/*     */ public class Symbol
/*     */ {
/*     */   public int sym;
/*     */   public int parse_state;
/* 116 */   boolean used_by_parser = false;
/*     */   public int left;
/*     */   public int right;
/*     */   public Object value;
/*     */ 
/*     */   public Symbol(int id, int l, int r, Object o)
/*     */   {
/*  54 */     this(id);
/*  55 */     this.left = l;
/*  56 */     this.right = r;
/*  57 */     this.value = o;
/*     */   }
/*     */ 
/*     */   public Symbol(int id, Object o)
/*     */   {
/*  65 */     this(id);
/*  66 */     this.left = -1;
/*  67 */     this.right = -1;
/*  68 */     this.value = o;
/*     */   }
/*     */ 
/*     */   public Symbol(int sym_num, int l, int r)
/*     */   {
/*  76 */     this.sym = sym_num;
/*  77 */     this.left = l;
/*  78 */     this.right = r;
/*  79 */     this.value = null;
/*     */   }
/*     */ 
/*     */   public Symbol(int sym_num)
/*     */   {
/*  87 */     this(sym_num, -1);
/*  88 */     this.left = -1;
/*  89 */     this.right = -1;
/*  90 */     this.value = null;
/*     */   }
/*     */ 
/*     */   public Symbol(int sym_num, int state)
/*     */   {
/*  98 */     this.sym = sym_num;
/*  99 */     this.parse_state = state;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 128 */     return "#" + this.sym;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java_cup.internal.runtime.Symbol
 * JD-Core Version:    0.6.2
 */