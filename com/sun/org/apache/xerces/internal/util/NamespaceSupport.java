/*     */ package com.sun.org.apache.xerces.internal.util;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class NamespaceSupport
/*     */   implements NamespaceContext
/*     */ {
/*  95 */   protected String[] fNamespace = new String[32];
/*     */   protected int fNamespaceSize;
/* 111 */   protected int[] fContext = new int[8];
/*     */   protected int fCurrentContext;
/* 116 */   protected String[] fPrefixes = new String[16];
/*     */ 
/*     */   public NamespaceSupport()
/*     */   {
/*     */   }
/*     */ 
/*     */   public NamespaceSupport(NamespaceContext context)
/*     */   {
/* 131 */     pushContext();
/*     */ 
/* 133 */     Enumeration prefixes = context.getAllPrefixes();
/* 134 */     while (prefixes.hasMoreElements()) {
/* 135 */       String prefix = (String)prefixes.nextElement();
/* 136 */       String uri = context.getURI(prefix);
/* 137 */       declarePrefix(prefix, uri);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 152 */     this.fNamespaceSize = 0;
/* 153 */     this.fCurrentContext = 0;
/*     */ 
/* 157 */     this.fNamespace[(this.fNamespaceSize++)] = XMLSymbols.PREFIX_XML;
/* 158 */     this.fNamespace[(this.fNamespaceSize++)] = NamespaceContext.XML_URI;
/*     */ 
/* 160 */     this.fNamespace[(this.fNamespaceSize++)] = XMLSymbols.PREFIX_XMLNS;
/* 161 */     this.fNamespace[(this.fNamespaceSize++)] = NamespaceContext.XMLNS_URI;
/*     */ 
/* 163 */     this.fContext[this.fCurrentContext] = this.fNamespaceSize;
/*     */   }
/*     */ 
/*     */   public void pushContext()
/*     */   {
/* 175 */     if (this.fCurrentContext + 1 == this.fContext.length) {
/* 176 */       int[] contextarray = new int[this.fContext.length * 2];
/* 177 */       System.arraycopy(this.fContext, 0, contextarray, 0, this.fContext.length);
/* 178 */       this.fContext = contextarray;
/*     */     }
/*     */ 
/* 182 */     this.fContext[(++this.fCurrentContext)] = this.fNamespaceSize;
/*     */   }
/*     */ 
/*     */   public void popContext()
/*     */   {
/* 191 */     this.fNamespaceSize = this.fContext[(this.fCurrentContext--)];
/*     */   }
/*     */ 
/*     */   public boolean declarePrefix(String prefix, String uri)
/*     */   {
/* 200 */     if ((prefix == XMLSymbols.PREFIX_XML) || (prefix == XMLSymbols.PREFIX_XMLNS)) {
/* 201 */       return false;
/*     */     }
/*     */ 
/* 205 */     for (int i = this.fNamespaceSize; i > this.fContext[this.fCurrentContext]; i -= 2) {
/* 206 */       if (this.fNamespace[(i - 2)] == prefix)
/*     */       {
/* 213 */         this.fNamespace[(i - 1)] = uri;
/* 214 */         return true;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 219 */     if (this.fNamespaceSize == this.fNamespace.length) {
/* 220 */       String[] namespacearray = new String[this.fNamespaceSize * 2];
/* 221 */       System.arraycopy(this.fNamespace, 0, namespacearray, 0, this.fNamespaceSize);
/* 222 */       this.fNamespace = namespacearray;
/*     */     }
/*     */ 
/* 226 */     this.fNamespace[(this.fNamespaceSize++)] = prefix;
/* 227 */     this.fNamespace[(this.fNamespaceSize++)] = uri;
/*     */ 
/* 229 */     return true;
/*     */   }
/*     */ 
/*     */   public String getURI(String prefix)
/*     */   {
/* 239 */     for (int i = this.fNamespaceSize; i > 0; i -= 2) {
/* 240 */       if (this.fNamespace[(i - 2)] == prefix) {
/* 241 */         return this.fNamespace[(i - 1)];
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 246 */     return null;
/*     */   }
/*     */ 
/*     */   public String getPrefix(String uri)
/*     */   {
/* 257 */     for (int i = this.fNamespaceSize; i > 0; i -= 2) {
/* 258 */       if ((this.fNamespace[(i - 1)] == uri) && 
/* 259 */         (getURI(this.fNamespace[(i - 2)]) == uri)) {
/* 260 */         return this.fNamespace[(i - 2)];
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 265 */     return null;
/*     */   }
/*     */ 
/*     */   public int getDeclaredPrefixCount()
/*     */   {
/* 273 */     return (this.fNamespaceSize - this.fContext[this.fCurrentContext]) / 2;
/*     */   }
/*     */ 
/*     */   public String getDeclaredPrefixAt(int index)
/*     */   {
/* 280 */     return this.fNamespace[(this.fContext[this.fCurrentContext] + index * 2)];
/*     */   }
/*     */ 
/*     */   public Iterator getPrefixes() {
/* 284 */     int count = 0;
/* 285 */     if (this.fPrefixes.length < this.fNamespace.length / 2)
/*     */     {
/* 287 */       String[] prefixes = new String[this.fNamespaceSize];
/* 288 */       this.fPrefixes = prefixes;
/*     */     }
/* 290 */     String prefix = null;
/* 291 */     boolean unique = true;
/* 292 */     for (int i = 2; i < this.fNamespaceSize - 2; i += 2) {
/* 293 */       prefix = this.fNamespace[(i + 2)];
/* 294 */       for (int k = 0; k < count; k++) {
/* 295 */         if (this.fPrefixes[k] == prefix) {
/* 296 */           unique = false;
/* 297 */           break;
/*     */         }
/*     */       }
/* 300 */       if (unique) {
/* 301 */         this.fPrefixes[(count++)] = prefix;
/*     */       }
/* 303 */       unique = true;
/*     */     }
/* 305 */     return new IteratorPrefixes(this.fPrefixes, count);
/*     */   }
/*     */ 
/*     */   public Enumeration getAllPrefixes()
/*     */   {
/* 311 */     int count = 0;
/* 312 */     if (this.fPrefixes.length < this.fNamespace.length / 2)
/*     */     {
/* 314 */       String[] prefixes = new String[this.fNamespaceSize];
/* 315 */       this.fPrefixes = prefixes;
/*     */     }
/* 317 */     String prefix = null;
/* 318 */     boolean unique = true;
/* 319 */     for (int i = 2; i < this.fNamespaceSize - 2; i += 2) {
/* 320 */       prefix = this.fNamespace[(i + 2)];
/* 321 */       for (int k = 0; k < count; k++) {
/* 322 */         if (this.fPrefixes[k] == prefix) {
/* 323 */           unique = false;
/* 324 */           break;
/*     */         }
/*     */       }
/* 327 */       if (unique) {
/* 328 */         this.fPrefixes[(count++)] = prefix;
/*     */       }
/* 330 */       unique = true;
/*     */     }
/* 332 */     return new Prefixes(this.fPrefixes, count);
/*     */   }
/*     */ 
/*     */   public Vector getPrefixes(String uri) {
/* 336 */     int count = 0;
/* 337 */     String prefix = null;
/* 338 */     boolean unique = true;
/* 339 */     Vector prefixList = new Vector();
/* 340 */     for (int i = this.fNamespaceSize; i > 0; i -= 2) {
/* 341 */       if ((this.fNamespace[(i - 1)] == uri) && 
/* 342 */         (!prefixList.contains(this.fNamespace[(i - 2)]))) {
/* 343 */         prefixList.add(this.fNamespace[(i - 2)]);
/*     */       }
/*     */     }
/* 346 */     return prefixList;
/*     */   }
/*     */ 
/*     */   public boolean containsPrefix(String prefix)
/*     */   {
/* 364 */     for (int i = this.fNamespaceSize; i > 0; i -= 2) {
/* 365 */       if (this.fNamespace[(i - 2)] == prefix) {
/* 366 */         return true;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 371 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean containsPrefixInCurrentContext(String prefix)
/*     */   {
/* 385 */     for (int i = this.fContext[this.fCurrentContext]; i < this.fNamespaceSize; i += 2) {
/* 386 */       if (this.fNamespace[i] == prefix) {
/* 387 */         return true;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 392 */     return false;
/*     */   }
/*     */ 
/*     */   protected final class IteratorPrefixes
/*     */     implements Iterator
/*     */   {
/*     */     private String[] prefixes;
/* 397 */     private int counter = 0;
/* 398 */     private int size = 0;
/*     */ 
/*     */     public IteratorPrefixes(String[] prefixes, int size)
/*     */     {
/* 404 */       this.prefixes = prefixes;
/* 405 */       this.size = size;
/*     */     }
/*     */ 
/*     */     public boolean hasNext()
/*     */     {
/* 412 */       return this.counter < this.size;
/*     */     }
/*     */ 
/*     */     public Object next()
/*     */     {
/* 419 */       if (this.counter < this.size) {
/* 420 */         return NamespaceSupport.this.fPrefixes[(this.counter++)];
/*     */       }
/* 422 */       throw new NoSuchElementException("Illegal access to Namespace prefixes enumeration.");
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 426 */       StringBuffer buf = new StringBuffer();
/* 427 */       for (int i = 0; i < this.size; i++) {
/* 428 */         buf.append(this.prefixes[i]);
/* 429 */         buf.append(" ");
/*     */       }
/*     */ 
/* 432 */       return buf.toString();
/*     */     }
/*     */ 
/*     */     public void remove() {
/* 436 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final class Prefixes
/*     */     implements Enumeration
/*     */   {
/*     */     private String[] prefixes;
/* 443 */     private int counter = 0;
/* 444 */     private int size = 0;
/*     */ 
/*     */     public Prefixes(String[] prefixes, int size)
/*     */     {
/* 450 */       this.prefixes = prefixes;
/* 451 */       this.size = size;
/*     */     }
/*     */ 
/*     */     public boolean hasMoreElements()
/*     */     {
/* 458 */       return this.counter < this.size;
/*     */     }
/*     */ 
/*     */     public Object nextElement()
/*     */     {
/* 465 */       if (this.counter < this.size) {
/* 466 */         return NamespaceSupport.this.fPrefixes[(this.counter++)];
/*     */       }
/* 468 */       throw new NoSuchElementException("Illegal access to Namespace prefixes enumeration.");
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 472 */       StringBuffer buf = new StringBuffer();
/* 473 */       for (int i = 0; i < this.size; i++) {
/* 474 */         buf.append(this.prefixes[i]);
/* 475 */         buf.append(" ");
/*     */       }
/*     */ 
/* 478 */       return buf.toString();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.NamespaceSupport
 * JD-Core Version:    0.6.2
 */