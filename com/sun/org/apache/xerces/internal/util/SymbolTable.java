/*     */ package com.sun.org.apache.xerces.internal.util;
/*     */ 
/*     */ public class SymbolTable
/*     */ {
/*     */   protected static final int TABLE_SIZE = 173;
/*  62 */   protected Entry[] fBuckets = null;
/*     */   protected int fTableSize;
/*     */ 
/*     */   public SymbolTable()
/*     */   {
/*  73 */     this(173);
/*     */   }
/*     */ 
/*     */   public SymbolTable(int tableSize)
/*     */   {
/*  78 */     this.fTableSize = tableSize;
/*  79 */     this.fBuckets = new Entry[this.fTableSize];
/*     */   }
/*     */ 
/*     */   public String addSymbol(String symbol)
/*     */   {
/*  97 */     int hash = hash(symbol);
/*  98 */     int bucket = hash % this.fTableSize;
/*  99 */     int length = symbol.length();
/* 100 */     for (Entry entry = this.fBuckets[bucket]; entry != null; entry = entry.next) {
/* 101 */       if ((length == entry.characters.length) && (hash == entry.hashCode) && 
/* 102 */         (symbol.regionMatches(0, entry.symbol, 0, length))) {
/* 103 */         return entry.symbol;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 121 */     Entry entry = new Entry(symbol, this.fBuckets[bucket]);
/* 122 */     entry.hashCode = hash;
/* 123 */     this.fBuckets[bucket] = entry;
/* 124 */     return entry.symbol;
/*     */   }
/*     */ 
/*     */   public String addSymbol(char[] buffer, int offset, int length)
/*     */   {
/* 140 */     int hash = hash(buffer, offset, length);
/* 141 */     int bucket = hash % this.fTableSize;
/* 142 */     label93: for (Entry entry = this.fBuckets[bucket]; entry != null; entry = entry.next) {
/* 143 */       if ((length == entry.characters.length) && (hash == entry.hashCode)) {
/* 144 */         for (int i = 0; i < length; i++) {
/* 145 */           if (buffer[(offset + i)] != entry.characters[i]) {
/*     */             break label93;
/*     */           }
/*     */         }
/* 149 */         return entry.symbol;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 154 */     Entry entry = new Entry(buffer, offset, length, this.fBuckets[bucket]);
/* 155 */     this.fBuckets[bucket] = entry;
/* 156 */     entry.hashCode = hash;
/* 157 */     return entry.symbol;
/*     */   }
/*     */ 
/*     */   public int hash(String symbol)
/*     */   {
/* 171 */     int code = 0;
/* 172 */     int length = symbol.length();
/* 173 */     for (int i = 0; i < length; i++) {
/* 174 */       code = code * 37 + symbol.charAt(i);
/*     */     }
/* 176 */     return code & 0x7FFFFFFF;
/*     */   }
/*     */ 
/*     */   public int hash(char[] buffer, int offset, int length)
/*     */   {
/* 193 */     int code = 0;
/* 194 */     for (int i = 0; i < length; i++) {
/* 195 */       code = code * 37 + buffer[(offset + i)];
/*     */     }
/* 197 */     return code & 0x7FFFFFFF;
/*     */   }
/*     */ 
/*     */   public boolean containsSymbol(String symbol)
/*     */   {
/* 210 */     int hash = hash(symbol);
/* 211 */     int bucket = hash % this.fTableSize;
/* 212 */     int length = symbol.length();
/* 213 */     for (Entry entry = this.fBuckets[bucket]; entry != null; entry = entry.next) {
/* 214 */       if ((length == entry.characters.length) && (hash == entry.hashCode) && 
/* 215 */         (symbol.regionMatches(0, entry.symbol, 0, length))) {
/* 216 */         return true;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 232 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean containsSymbol(char[] buffer, int offset, int length)
/*     */   {
/* 247 */     int hash = hash(buffer, offset, length);
/* 248 */     int bucket = hash % this.fTableSize;
/* 249 */     label89: for (Entry entry = this.fBuckets[bucket]; entry != null; entry = entry.next) {
/* 250 */       if ((length == entry.characters.length) && (hash == entry.hashCode)) {
/* 251 */         for (int i = 0; i < length; i++) {
/* 252 */           if (buffer[(offset + i)] != entry.characters[i]) {
/*     */             break label89;
/*     */           }
/*     */         }
/* 256 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 260 */     return false;
/*     */   }
/*     */ 
/*     */   protected static final class Entry
/*     */   {
/*     */     public String symbol;
/* 281 */     int hashCode = 0;
/*     */     public char[] characters;
/*     */     public Entry next;
/*     */ 
/*     */     public Entry(String symbol, Entry next)
/*     */     {
/* 301 */       this.symbol = symbol.intern();
/* 302 */       this.characters = new char[symbol.length()];
/* 303 */       symbol.getChars(0, this.characters.length, this.characters, 0);
/* 304 */       this.next = next;
/*     */     }
/*     */ 
/*     */     public Entry(char[] ch, int offset, int length, Entry next)
/*     */     {
/* 312 */       this.characters = new char[length];
/* 313 */       System.arraycopy(ch, offset, this.characters, 0, length);
/* 314 */       this.symbol = new String(this.characters).intern();
/* 315 */       this.next = next;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.SymbolTable
 * JD-Core Version:    0.6.2
 */