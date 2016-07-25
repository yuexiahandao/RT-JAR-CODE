/*     */ package com.sun.xml.internal.fastinfoset.util;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
/*     */ 
/*     */ public class CharArrayIntMap extends KeyIntMap
/*     */ {
/*     */   private CharArrayIntMap _readOnlyMap;
/*     */   protected int _totalCharacterCount;
/*     */   private Entry[] _table;
/*     */ 
/*     */   public CharArrayIntMap(int initialCapacity, float loadFactor)
/*     */   {
/*  73 */     super(initialCapacity, loadFactor);
/*     */ 
/*  75 */     this._table = new Entry[this._capacity];
/*     */   }
/*     */ 
/*     */   public CharArrayIntMap(int initialCapacity) {
/*  79 */     this(initialCapacity, 0.75F);
/*     */   }
/*     */ 
/*     */   public CharArrayIntMap() {
/*  83 */     this(16, 0.75F);
/*     */   }
/*     */ 
/*     */   public final void clear() {
/*  87 */     for (int i = 0; i < this._table.length; i++) {
/*  88 */       this._table[i] = null;
/*     */     }
/*  90 */     this._size = 0;
/*  91 */     this._totalCharacterCount = 0;
/*     */   }
/*     */ 
/*     */   public final void setReadOnlyMap(KeyIntMap readOnlyMap, boolean clear) {
/*  95 */     if (!(readOnlyMap instanceof CharArrayIntMap)) {
/*  96 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.illegalClass", new Object[] { readOnlyMap }));
/*     */     }
/*     */ 
/* 100 */     setReadOnlyMap((CharArrayIntMap)readOnlyMap, clear);
/*     */   }
/*     */ 
/*     */   public final void setReadOnlyMap(CharArrayIntMap readOnlyMap, boolean clear) {
/* 104 */     this._readOnlyMap = readOnlyMap;
/* 105 */     if (this._readOnlyMap != null) {
/* 106 */       this._readOnlyMapSize = this._readOnlyMap.size();
/*     */ 
/* 108 */       if (clear)
/* 109 */         clear();
/*     */     }
/*     */     else {
/* 112 */       this._readOnlyMapSize = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final int get(char[] ch, int start, int length)
/*     */   {
/* 124 */     int hash = hashHash(CharArray.hashCode(ch, start, length));
/* 125 */     return get(ch, start, length, hash);
/*     */   }
/*     */ 
/*     */   public final int obtainIndex(char[] ch, int start, int length, boolean clone)
/*     */   {
/* 137 */     int hash = hashHash(CharArray.hashCode(ch, start, length));
/*     */ 
/* 139 */     if (this._readOnlyMap != null) {
/* 140 */       int index = this._readOnlyMap.get(ch, start, length, hash);
/* 141 */       if (index != -1) {
/* 142 */         return index;
/*     */       }
/*     */     }
/*     */ 
/* 146 */     int tableIndex = indexFor(hash, this._table.length);
/* 147 */     for (Entry e = this._table[tableIndex]; e != null; e = e._next) {
/* 148 */       if ((e._hash == hash) && (e.equalsCharArray(ch, start, length))) {
/* 149 */         return e._value;
/*     */       }
/*     */     }
/*     */ 
/* 153 */     if (clone) {
/* 154 */       char[] chClone = new char[length];
/* 155 */       System.arraycopy(ch, start, chClone, 0, length);
/*     */ 
/* 157 */       ch = chClone;
/* 158 */       start = 0;
/*     */     }
/*     */ 
/* 161 */     addEntry(ch, start, length, hash, this._size + this._readOnlyMapSize, tableIndex);
/* 162 */     return -1;
/*     */   }
/*     */ 
/*     */   public final int getTotalCharacterCount() {
/* 166 */     return this._totalCharacterCount;
/*     */   }
/*     */ 
/*     */   private final int get(char[] ch, int start, int length, int hash) {
/* 170 */     if (this._readOnlyMap != null) {
/* 171 */       int i = this._readOnlyMap.get(ch, start, length, hash);
/* 172 */       if (i != -1) {
/* 173 */         return i;
/*     */       }
/*     */     }
/*     */ 
/* 177 */     int tableIndex = indexFor(hash, this._table.length);
/* 178 */     for (Entry e = this._table[tableIndex]; e != null; e = e._next) {
/* 179 */       if ((e._hash == hash) && (e.equalsCharArray(ch, start, length))) {
/* 180 */         return e._value;
/*     */       }
/*     */     }
/*     */ 
/* 184 */     return -1;
/*     */   }
/*     */ 
/*     */   private final void addEntry(char[] ch, int start, int length, int hash, int value, int bucketIndex) {
/* 188 */     Entry e = this._table[bucketIndex];
/* 189 */     this._table[bucketIndex] = new Entry(ch, start, length, hash, value, e);
/* 190 */     this._totalCharacterCount += length;
/* 191 */     if (this._size++ >= this._threshold)
/* 192 */       resize(2 * this._table.length);
/*     */   }
/*     */ 
/*     */   private final void resize(int newCapacity)
/*     */   {
/* 197 */     this._capacity = newCapacity;
/* 198 */     Entry[] oldTable = this._table;
/* 199 */     int oldCapacity = oldTable.length;
/* 200 */     if (oldCapacity == 1048576) {
/* 201 */       this._threshold = 2147483647;
/* 202 */       return;
/*     */     }
/*     */ 
/* 205 */     Entry[] newTable = new Entry[this._capacity];
/* 206 */     transfer(newTable);
/* 207 */     this._table = newTable;
/* 208 */     this._threshold = ((int)(this._capacity * this._loadFactor));
/*     */   }
/*     */ 
/*     */   private final void transfer(Entry[] newTable) {
/* 212 */     Entry[] src = this._table;
/* 213 */     int newCapacity = newTable.length;
/* 214 */     for (int j = 0; j < src.length; j++) {
/* 215 */       Entry e = src[j];
/* 216 */       if (e != null) {
/* 217 */         src[j] = null;
/*     */         do {
/* 219 */           Entry next = e._next;
/* 220 */           int i = indexFor(e._hash, newCapacity);
/* 221 */           e._next = newTable[i];
/* 222 */           newTable[i] = e;
/* 223 */           e = next;
/* 224 */         }while (e != null);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Entry extends KeyIntMap.BaseEntry
/*     */   {
/*     */     final char[] _ch;
/*     */     final int _start;
/*     */     final int _length;
/*     */     Entry _next;
/*     */ 
/*     */     public Entry(char[] ch, int start, int length, int hash, int value, Entry next)
/*     */     {
/*  46 */       super(value);
/*  47 */       this._ch = ch;
/*  48 */       this._start = start;
/*  49 */       this._length = length;
/*  50 */       this._next = next;
/*     */     }
/*     */ 
/*     */     public final boolean equalsCharArray(char[] ch, int start, int length) {
/*  54 */       if (this._length == length) {
/*  55 */         int n = this._length;
/*  56 */         int i = this._start;
/*  57 */         int j = start;
/*  58 */         while (n-- != 0) {
/*  59 */           if (this._ch[(i++)] != ch[(j++)])
/*  60 */             return false;
/*     */         }
/*  62 */         return true;
/*     */       }
/*     */ 
/*  65 */       return false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.util.CharArrayIntMap
 * JD-Core Version:    0.6.2
 */