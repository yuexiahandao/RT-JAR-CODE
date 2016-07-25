/*     */ package com.sun.xml.internal.fastinfoset.util;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
/*     */ 
/*     */ public class StringIntMap extends KeyIntMap
/*     */ {
/*  33 */   protected static final Entry NULL_ENTRY = new Entry(null, 0, -1, null);
/*     */   protected StringIntMap _readOnlyMap;
/*  48 */   protected Entry _lastEntry = NULL_ENTRY;
/*     */   protected Entry[] _table;
/*     */   protected int _index;
/*     */   protected int _totalCharacterCount;
/*     */ 
/*     */   public StringIntMap(int initialCapacity, float loadFactor)
/*     */   {
/*  58 */     super(initialCapacity, loadFactor);
/*     */ 
/*  60 */     this._table = new Entry[this._capacity];
/*     */   }
/*     */ 
/*     */   public StringIntMap(int initialCapacity) {
/*  64 */     this(initialCapacity, 0.75F);
/*     */   }
/*     */ 
/*     */   public StringIntMap() {
/*  68 */     this(16, 0.75F);
/*     */   }
/*     */ 
/*     */   public void clear() {
/*  72 */     for (int i = 0; i < this._table.length; i++) {
/*  73 */       this._table[i] = null;
/*     */     }
/*  75 */     this._lastEntry = NULL_ENTRY;
/*  76 */     this._size = 0;
/*  77 */     this._index = this._readOnlyMapSize;
/*  78 */     this._totalCharacterCount = 0;
/*     */   }
/*     */ 
/*     */   public void setReadOnlyMap(KeyIntMap readOnlyMap, boolean clear) {
/*  82 */     if (!(readOnlyMap instanceof StringIntMap)) {
/*  83 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.illegalClass", new Object[] { readOnlyMap }));
/*     */     }
/*     */ 
/*  87 */     setReadOnlyMap((StringIntMap)readOnlyMap, clear);
/*     */   }
/*     */ 
/*     */   public final void setReadOnlyMap(StringIntMap readOnlyMap, boolean clear) {
/*  91 */     this._readOnlyMap = readOnlyMap;
/*  92 */     if (this._readOnlyMap != null) {
/*  93 */       this._readOnlyMapSize = this._readOnlyMap.size();
/*  94 */       this._index = (this._size + this._readOnlyMapSize);
/*     */ 
/*  96 */       if (clear)
/*  97 */         clear();
/*     */     }
/*     */     else {
/* 100 */       this._readOnlyMapSize = 0;
/* 101 */       this._index = this._size;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final int getNextIndex() {
/* 106 */     return this._index++;
/*     */   }
/*     */ 
/*     */   public final int getIndex() {
/* 110 */     return this._index;
/*     */   }
/*     */ 
/*     */   public final int obtainIndex(String key) {
/* 114 */     int hash = hashHash(key.hashCode());
/*     */ 
/* 116 */     if (this._readOnlyMap != null) {
/* 117 */       int index = this._readOnlyMap.get(key, hash);
/* 118 */       if (index != -1) {
/* 119 */         return index;
/*     */       }
/*     */     }
/*     */ 
/* 123 */     int tableIndex = indexFor(hash, this._table.length);
/* 124 */     for (Entry e = this._table[tableIndex]; e != null; e = e._next) {
/* 125 */       if ((e._hash == hash) && (eq(key, e._key))) {
/* 126 */         return e._value;
/*     */       }
/*     */     }
/*     */ 
/* 130 */     addEntry(key, hash, tableIndex);
/* 131 */     return -1;
/*     */   }
/*     */ 
/*     */   public final void add(String key) {
/* 135 */     int hash = hashHash(key.hashCode());
/* 136 */     int tableIndex = indexFor(hash, this._table.length);
/* 137 */     addEntry(key, hash, tableIndex);
/*     */   }
/*     */ 
/*     */   public final int get(String key) {
/* 141 */     if (key == this._lastEntry._key) {
/* 142 */       return this._lastEntry._value;
/*     */     }
/* 144 */     return get(key, hashHash(key.hashCode()));
/*     */   }
/*     */ 
/*     */   public final int getTotalCharacterCount() {
/* 148 */     return this._totalCharacterCount;
/*     */   }
/*     */ 
/*     */   private final int get(String key, int hash) {
/* 152 */     if (this._readOnlyMap != null) {
/* 153 */       int i = this._readOnlyMap.get(key, hash);
/* 154 */       if (i != -1) {
/* 155 */         return i;
/*     */       }
/*     */     }
/*     */ 
/* 159 */     int tableIndex = indexFor(hash, this._table.length);
/* 160 */     for (Entry e = this._table[tableIndex]; e != null; e = e._next) {
/* 161 */       if ((e._hash == hash) && (eq(key, e._key))) {
/* 162 */         this._lastEntry = e;
/* 163 */         return e._value;
/*     */       }
/*     */     }
/*     */ 
/* 167 */     return -1;
/*     */   }
/*     */ 
/*     */   private final void addEntry(String key, int hash, int bucketIndex)
/*     */   {
/* 172 */     Entry e = this._table[bucketIndex];
/* 173 */     this._table[bucketIndex] = new Entry(key, hash, this._index++, e);
/* 174 */     this._totalCharacterCount += key.length();
/* 175 */     if (this._size++ >= this._threshold)
/* 176 */       resize(2 * this._table.length);
/*     */   }
/*     */ 
/*     */   protected final void resize(int newCapacity)
/*     */   {
/* 181 */     this._capacity = newCapacity;
/* 182 */     Entry[] oldTable = this._table;
/* 183 */     int oldCapacity = oldTable.length;
/* 184 */     if (oldCapacity == 1048576) {
/* 185 */       this._threshold = 2147483647;
/* 186 */       return;
/*     */     }
/*     */ 
/* 189 */     Entry[] newTable = new Entry[this._capacity];
/* 190 */     transfer(newTable);
/* 191 */     this._table = newTable;
/* 192 */     this._threshold = ((int)(this._capacity * this._loadFactor));
/*     */   }
/*     */ 
/*     */   private final void transfer(Entry[] newTable) {
/* 196 */     Entry[] src = this._table;
/* 197 */     int newCapacity = newTable.length;
/* 198 */     for (int j = 0; j < src.length; j++) {
/* 199 */       Entry e = src[j];
/* 200 */       if (e != null) {
/* 201 */         src[j] = null;
/*     */         do {
/* 203 */           Entry next = e._next;
/* 204 */           int i = indexFor(e._hash, newCapacity);
/* 205 */           e._next = newTable[i];
/* 206 */           newTable[i] = e;
/* 207 */           e = next;
/* 208 */         }while (e != null);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private final boolean eq(String x, String y) {
/* 214 */     return (x == y) || (x.equals(y));
/*     */   }
/*     */ 
/*     */   protected static class Entry extends KeyIntMap.BaseEntry
/*     */   {
/*     */     final String _key;
/*     */     Entry _next;
/*     */ 
/*     */     public Entry(String key, int hash, int value, Entry next)
/*     */     {
/*  42 */       super(value);
/*  43 */       this._key = key;
/*  44 */       this._next = next;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.util.StringIntMap
 * JD-Core Version:    0.6.2
 */