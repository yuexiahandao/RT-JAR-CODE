/*     */ package com.sun.org.apache.xerces.internal.util;
/*     */ 
/*     */ public class SymbolHash
/*     */ {
/*  40 */   protected int fTableSize = 101;
/*     */   protected Entry[] fBuckets;
/*  50 */   protected int fNum = 0;
/*     */ 
/*     */   public SymbolHash()
/*     */   {
/*  58 */     this.fBuckets = new Entry[this.fTableSize];
/*     */   }
/*     */ 
/*     */   public SymbolHash(int size)
/*     */   {
/*  67 */     this.fTableSize = size;
/*  68 */     this.fBuckets = new Entry[this.fTableSize];
/*     */   }
/*     */ 
/*     */   public void put(Object key, Object value)
/*     */   {
/*  84 */     int bucket = (key.hashCode() & 0x7FFFFFFF) % this.fTableSize;
/*  85 */     Entry entry = search(key, bucket);
/*     */ 
/*  88 */     if (entry != null) {
/*  89 */       entry.value = value;
/*     */     }
/*     */     else
/*     */     {
/*  93 */       entry = new Entry(key, value, this.fBuckets[bucket]);
/*  94 */       this.fBuckets[bucket] = entry;
/*  95 */       this.fNum += 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object get(Object key)
/*     */   {
/* 106 */     int bucket = (key.hashCode() & 0x7FFFFFFF) % this.fTableSize;
/* 107 */     Entry entry = search(key, bucket);
/* 108 */     if (entry != null) {
/* 109 */       return entry.value;
/*     */     }
/* 111 */     return null;
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 120 */     return this.fNum;
/*     */   }
/*     */ 
/*     */   public int getValues(Object[] elements, int from)
/*     */   {
/* 131 */     int i = 0; for (int j = 0; (i < this.fTableSize) && (j < this.fNum); i++) {
/* 132 */       for (Entry entry = this.fBuckets[i]; entry != null; entry = entry.next) {
/* 133 */         elements[(from + j)] = entry.value;
/* 134 */         j++;
/*     */       }
/*     */     }
/* 137 */     return this.fNum;
/*     */   }
/*     */ 
/*     */   public Object[] getEntries()
/*     */   {
/* 144 */     Object[] entries = new Object[this.fNum << 1];
/* 145 */     int i = 0; for (int j = 0; (i < this.fTableSize) && (j < this.fNum << 1); i++) {
/* 146 */       for (Entry entry = this.fBuckets[i]; entry != null; entry = entry.next) {
/* 147 */         entries[j] = entry.key;
/* 148 */         entries[(++j)] = entry.value;
/* 149 */         j++;
/*     */       }
/*     */     }
/* 152 */     return entries;
/*     */   }
/*     */ 
/*     */   public SymbolHash makeClone()
/*     */   {
/* 159 */     SymbolHash newTable = new SymbolHash(this.fTableSize);
/* 160 */     newTable.fNum = this.fNum;
/* 161 */     for (int i = 0; i < this.fTableSize; i++) {
/* 162 */       if (this.fBuckets[i] != null)
/* 163 */         newTable.fBuckets[i] = this.fBuckets[i].makeClone();
/*     */     }
/* 165 */     return newTable;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 173 */     for (int i = 0; i < this.fTableSize; i++) {
/* 174 */       this.fBuckets[i] = null;
/*     */     }
/* 176 */     this.fNum = 0;
/*     */   }
/*     */ 
/*     */   protected Entry search(Object key, int bucket)
/*     */   {
/* 181 */     for (Entry entry = this.fBuckets[bucket]; entry != null; entry = entry.next) {
/* 182 */       if (key.equals(entry.key))
/* 183 */         return entry;
/*     */     }
/* 185 */     return null;
/*     */   }
/*     */ 
/*     */   protected static final class Entry
/*     */   {
/*     */     public Object key;
/*     */     public Object value;
/*     */     public Entry next;
/*     */ 
/*     */     public Entry()
/*     */     {
/* 204 */       this.key = null;
/* 205 */       this.value = null;
/* 206 */       this.next = null;
/*     */     }
/*     */ 
/*     */     public Entry(Object key, Object value, Entry next) {
/* 210 */       this.key = key;
/* 211 */       this.value = value;
/* 212 */       this.next = next;
/*     */     }
/*     */ 
/*     */     public Entry makeClone() {
/* 216 */       Entry entry = new Entry();
/* 217 */       entry.key = this.key;
/* 218 */       entry.value = this.value;
/* 219 */       if (this.next != null)
/* 220 */         entry.next = this.next.makeClone();
/* 221 */       return entry;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.SymbolHash
 * JD-Core Version:    0.6.2
 */