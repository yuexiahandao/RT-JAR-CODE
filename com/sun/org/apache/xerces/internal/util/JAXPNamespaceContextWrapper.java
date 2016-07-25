/*     */ package com.sun.org.apache.xerces.internal.util;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.TreeSet;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public final class JAXPNamespaceContextWrapper
/*     */   implements com.sun.org.apache.xerces.internal.xni.NamespaceContext
/*     */ {
/*     */   private javax.xml.namespace.NamespaceContext fNamespaceContext;
/*     */   private SymbolTable fSymbolTable;
/*     */   private List fPrefixes;
/*  47 */   private final Vector fAllPrefixes = new Vector();
/*     */ 
/*  49 */   private int[] fContext = new int[8];
/*     */   private int fCurrentContext;
/*     */ 
/*     */   public JAXPNamespaceContextWrapper(SymbolTable symbolTable)
/*     */   {
/*  53 */     setSymbolTable(symbolTable);
/*     */   }
/*     */ 
/*     */   public void setNamespaceContext(javax.xml.namespace.NamespaceContext context) {
/*  57 */     this.fNamespaceContext = context;
/*     */   }
/*     */ 
/*     */   public javax.xml.namespace.NamespaceContext getNamespaceContext() {
/*  61 */     return this.fNamespaceContext;
/*     */   }
/*     */ 
/*     */   public void setSymbolTable(SymbolTable symbolTable) {
/*  65 */     this.fSymbolTable = symbolTable;
/*     */   }
/*     */ 
/*     */   public SymbolTable getSymbolTable() {
/*  69 */     return this.fSymbolTable;
/*     */   }
/*     */ 
/*     */   public void setDeclaredPrefixes(List prefixes) {
/*  73 */     this.fPrefixes = prefixes;
/*     */   }
/*     */ 
/*     */   public List getDeclaredPrefixes() {
/*  77 */     return this.fPrefixes;
/*     */   }
/*     */ 
/*     */   public String getURI(String prefix)
/*     */   {
/*  85 */     if (this.fNamespaceContext != null) {
/*  86 */       String uri = this.fNamespaceContext.getNamespaceURI(prefix);
/*  87 */       if ((uri != null) && (!"".equals(uri))) {
/*  88 */         return this.fSymbolTable != null ? this.fSymbolTable.addSymbol(uri) : uri.intern();
/*     */       }
/*     */     }
/*  91 */     return null;
/*     */   }
/*     */ 
/*     */   public String getPrefix(String uri) {
/*  95 */     if (this.fNamespaceContext != null) {
/*  96 */       if (uri == null) {
/*  97 */         uri = "";
/*     */       }
/*  99 */       String prefix = this.fNamespaceContext.getPrefix(uri);
/* 100 */       if (prefix == null) {
/* 101 */         prefix = "";
/*     */       }
/* 103 */       return this.fSymbolTable != null ? this.fSymbolTable.addSymbol(prefix) : prefix.intern();
/*     */     }
/* 105 */     return null;
/*     */   }
/*     */ 
/*     */   public Enumeration getAllPrefixes()
/*     */   {
/* 111 */     return Collections.enumeration(new TreeSet(this.fAllPrefixes));
/*     */   }
/*     */ 
/*     */   public void pushContext()
/*     */   {
/* 116 */     if (this.fCurrentContext + 1 == this.fContext.length) {
/* 117 */       int[] contextarray = new int[this.fContext.length * 2];
/* 118 */       System.arraycopy(this.fContext, 0, contextarray, 0, this.fContext.length);
/* 119 */       this.fContext = contextarray;
/*     */     }
/*     */ 
/* 122 */     this.fContext[(++this.fCurrentContext)] = this.fAllPrefixes.size();
/* 123 */     if (this.fPrefixes != null)
/* 124 */       this.fAllPrefixes.addAll(this.fPrefixes);
/*     */   }
/*     */ 
/*     */   public void popContext()
/*     */   {
/* 129 */     this.fAllPrefixes.setSize(this.fContext[(this.fCurrentContext--)]);
/*     */   }
/*     */ 
/*     */   public boolean declarePrefix(String prefix, String uri) {
/* 133 */     return true;
/*     */   }
/*     */ 
/*     */   public int getDeclaredPrefixCount() {
/* 137 */     return this.fPrefixes != null ? this.fPrefixes.size() : 0;
/*     */   }
/*     */ 
/*     */   public String getDeclaredPrefixAt(int index) {
/* 141 */     return (String)this.fPrefixes.get(index);
/*     */   }
/*     */ 
/*     */   public void reset() {
/* 145 */     this.fCurrentContext = 0;
/* 146 */     this.fContext[this.fCurrentContext] = 0;
/* 147 */     this.fAllPrefixes.clear();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.JAXPNamespaceContextWrapper
 * JD-Core Version:    0.6.2
 */