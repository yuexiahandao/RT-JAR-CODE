/*     */ package com.sun.org.apache.xerces.internal.xinclude;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.util.NamespaceSupport;
/*     */ import com.sun.org.apache.xerces.internal.util.NamespaceSupport.Prefixes;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLSymbols;
/*     */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*     */ import java.util.Enumeration;
/*     */ 
/*     */ public class MultipleScopeNamespaceSupport extends NamespaceSupport
/*     */ {
/*  43 */   protected int[] fScope = new int[8];
/*     */   protected int fCurrentScope;
/*     */ 
/*     */   public MultipleScopeNamespaceSupport()
/*     */   {
/*  51 */     this.fCurrentScope = 0;
/*  52 */     this.fScope[0] = 0;
/*     */   }
/*     */ 
/*     */   public MultipleScopeNamespaceSupport(NamespaceContext context)
/*     */   {
/*  59 */     super(context);
/*  60 */     this.fCurrentScope = 0;
/*  61 */     this.fScope[0] = 0;
/*     */   }
/*     */ 
/*     */   public Enumeration getAllPrefixes()
/*     */   {
/*  68 */     int count = 0;
/*  69 */     if (this.fPrefixes.length < this.fNamespace.length / 2)
/*     */     {
/*  71 */       String[] prefixes = new String[this.fNamespaceSize];
/*  72 */       this.fPrefixes = prefixes;
/*     */     }
/*  74 */     String prefix = null;
/*  75 */     boolean unique = true;
/*  76 */     for (int i = this.fContext[this.fScope[this.fCurrentScope]]; 
/*  77 */       i <= this.fNamespaceSize - 2; 
/*  78 */       i += 2) {
/*  79 */       prefix = this.fNamespace[i];
/*  80 */       for (int k = 0; k < count; k++) {
/*  81 */         if (this.fPrefixes[k] == prefix) {
/*  82 */           unique = false;
/*  83 */           break;
/*     */         }
/*     */       }
/*  86 */       if (unique) {
/*  87 */         this.fPrefixes[(count++)] = prefix;
/*     */       }
/*  89 */       unique = true;
/*     */     }
/*  91 */     return new NamespaceSupport.Prefixes(this, this.fPrefixes, count);
/*     */   }
/*     */ 
/*     */   public int getScopeForContext(int context) {
/*  95 */     int scope = this.fCurrentScope;
/*  96 */     while (context < this.fScope[scope]) {
/*  97 */       scope--;
/*     */     }
/*  99 */     return scope;
/*     */   }
/*     */ 
/*     */   public String getPrefix(String uri)
/*     */   {
/* 106 */     return getPrefix(uri, this.fNamespaceSize, this.fContext[this.fScope[this.fCurrentScope]]);
/*     */   }
/*     */ 
/*     */   public String getURI(String prefix)
/*     */   {
/* 113 */     return getURI(prefix, this.fNamespaceSize, this.fContext[this.fScope[this.fCurrentScope]]);
/*     */   }
/*     */ 
/*     */   public String getPrefix(String uri, int context) {
/* 117 */     return getPrefix(uri, this.fContext[(context + 1)], this.fContext[this.fScope[getScopeForContext(context)]]);
/*     */   }
/*     */ 
/*     */   public String getURI(String prefix, int context) {
/* 121 */     return getURI(prefix, this.fContext[(context + 1)], this.fContext[this.fScope[getScopeForContext(context)]]);
/*     */   }
/*     */ 
/*     */   public String getPrefix(String uri, int start, int end)
/*     */   {
/* 126 */     if (uri == NamespaceContext.XML_URI) {
/* 127 */       return XMLSymbols.PREFIX_XML;
/*     */     }
/* 129 */     if (uri == NamespaceContext.XMLNS_URI) {
/* 130 */       return XMLSymbols.PREFIX_XMLNS;
/*     */     }
/*     */ 
/* 134 */     for (int i = start; i > end; i -= 2) {
/* 135 */       if ((this.fNamespace[(i - 1)] == uri) && 
/* 136 */         (getURI(this.fNamespace[(i - 2)]) == uri)) {
/* 137 */         return this.fNamespace[(i - 2)];
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 142 */     return null;
/*     */   }
/*     */ 
/*     */   public String getURI(String prefix, int start, int end)
/*     */   {
/* 147 */     if (prefix == XMLSymbols.PREFIX_XML) {
/* 148 */       return NamespaceContext.XML_URI;
/*     */     }
/* 150 */     if (prefix == XMLSymbols.PREFIX_XMLNS) {
/* 151 */       return NamespaceContext.XMLNS_URI;
/*     */     }
/*     */ 
/* 155 */     for (int i = start; i > end; i -= 2) {
/* 156 */       if (this.fNamespace[(i - 2)] == prefix) {
/* 157 */         return this.fNamespace[(i - 1)];
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 162 */     return null;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 170 */     this.fCurrentContext = this.fScope[this.fCurrentScope];
/* 171 */     this.fNamespaceSize = this.fContext[this.fCurrentContext];
/*     */   }
/*     */ 
/*     */   public void pushScope()
/*     */   {
/* 179 */     if (this.fCurrentScope + 1 == this.fScope.length) {
/* 180 */       int[] contextarray = new int[this.fScope.length * 2];
/* 181 */       System.arraycopy(this.fScope, 0, contextarray, 0, this.fScope.length);
/* 182 */       this.fScope = contextarray;
/*     */     }
/* 184 */     pushContext();
/* 185 */     this.fScope[(++this.fCurrentScope)] = this.fCurrentContext;
/*     */   }
/*     */ 
/*     */   public void popScope()
/*     */   {
/* 193 */     this.fCurrentContext = this.fScope[(this.fCurrentScope--)];
/* 194 */     popContext();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xinclude.MultipleScopeNamespaceSupport
 * JD-Core Version:    0.6.2
 */