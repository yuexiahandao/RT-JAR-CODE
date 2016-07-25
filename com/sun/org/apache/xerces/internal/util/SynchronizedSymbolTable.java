/*     */ package com.sun.org.apache.xerces.internal.util;
/*     */ 
/*     */ public final class SynchronizedSymbolTable extends SymbolTable
/*     */ {
/*     */   protected SymbolTable fSymbolTable;
/*     */ 
/*     */   public SynchronizedSymbolTable(SymbolTable symbolTable)
/*     */   {
/*  48 */     this.fSymbolTable = symbolTable;
/*     */   }
/*     */ 
/*     */   public SynchronizedSymbolTable()
/*     */   {
/*  53 */     this.fSymbolTable = new SymbolTable();
/*     */   }
/*     */ 
/*     */   public SynchronizedSymbolTable(int size)
/*     */   {
/*  58 */     this.fSymbolTable = new SymbolTable(size);
/*     */   }
/*     */ 
/*     */   public String addSymbol(String symbol)
/*     */   {
/*  75 */     synchronized (this.fSymbolTable) {
/*  76 */       return this.fSymbolTable.addSymbol(symbol);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String addSymbol(char[] buffer, int offset, int length)
/*     */   {
/*  93 */     synchronized (this.fSymbolTable) {
/*  94 */       return this.fSymbolTable.addSymbol(buffer, offset, length);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean containsSymbol(String symbol)
/*     */   {
/* 107 */     synchronized (this.fSymbolTable) {
/* 108 */       return this.fSymbolTable.containsSymbol(symbol);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean containsSymbol(char[] buffer, int offset, int length)
/*     */   {
/* 123 */     synchronized (this.fSymbolTable) {
/* 124 */       return this.fSymbolTable.containsSymbol(buffer, offset, length);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable
 * JD-Core Version:    0.6.2
 */