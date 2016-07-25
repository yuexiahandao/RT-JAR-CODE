/*     */ package com.sun.org.apache.xerces.internal.util;
/*     */ 
/*     */ public final class ShadowedSymbolTable extends SymbolTable
/*     */ {
/*     */   protected SymbolTable fSymbolTable;
/*     */ 
/*     */   public ShadowedSymbolTable(SymbolTable symbolTable)
/*     */   {
/*  51 */     this.fSymbolTable = symbolTable;
/*     */   }
/*     */ 
/*     */   public String addSymbol(String symbol)
/*     */   {
/*  68 */     if (this.fSymbolTable.containsSymbol(symbol)) {
/*  69 */       return this.fSymbolTable.addSymbol(symbol);
/*     */     }
/*  71 */     return super.addSymbol(symbol);
/*     */   }
/*     */ 
/*     */   public String addSymbol(char[] buffer, int offset, int length)
/*     */   {
/*  87 */     if (this.fSymbolTable.containsSymbol(buffer, offset, length)) {
/*  88 */       return this.fSymbolTable.addSymbol(buffer, offset, length);
/*     */     }
/*  90 */     return super.addSymbol(buffer, offset, length);
/*     */   }
/*     */ 
/*     */   public int hash(String symbol)
/*     */   {
/* 103 */     return this.fSymbolTable.hash(symbol);
/*     */   }
/*     */ 
/*     */   public int hash(char[] buffer, int offset, int length)
/*     */   {
/* 118 */     return this.fSymbolTable.hash(buffer, offset, length);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.ShadowedSymbolTable
 * JD-Core Version:    0.6.2
 */