/*     */ package com.sun.xml.internal.fastinfoset.util;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
/*     */ import com.sun.xml.internal.fastinfoset.QualifiedName;
/*     */ 
/*     */ public class LocalNameQualifiedNamesMap extends KeyIntMap
/*     */ {
/*     */   private LocalNameQualifiedNamesMap _readOnlyMap;
/*     */   private int _index;
/*     */   private Entry[] _table;
/*     */ 
/*     */   public LocalNameQualifiedNamesMap(int initialCapacity, float loadFactor)
/*     */   {
/*  68 */     super(initialCapacity, loadFactor);
/*     */ 
/*  70 */     this._table = new Entry[this._capacity];
/*     */   }
/*     */ 
/*     */   public LocalNameQualifiedNamesMap(int initialCapacity) {
/*  74 */     this(initialCapacity, 0.75F);
/*     */   }
/*     */ 
/*     */   public LocalNameQualifiedNamesMap() {
/*  78 */     this(16, 0.75F);
/*     */   }
/*     */ 
/*     */   public final void clear() {
/*  82 */     for (int i = 0; i < this._table.length; i++) {
/*  83 */       this._table[i] = null;
/*     */     }
/*  85 */     this._size = 0;
/*     */ 
/*  87 */     if (this._readOnlyMap != null)
/*  88 */       this._index = this._readOnlyMap.getIndex();
/*     */     else
/*  90 */       this._index = 0;
/*     */   }
/*     */ 
/*     */   public final void setReadOnlyMap(KeyIntMap readOnlyMap, boolean clear)
/*     */   {
/*  95 */     if (!(readOnlyMap instanceof LocalNameQualifiedNamesMap)) {
/*  96 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.illegalClass", new Object[] { readOnlyMap }));
/*     */     }
/*     */ 
/* 100 */     setReadOnlyMap((LocalNameQualifiedNamesMap)readOnlyMap, clear);
/*     */   }
/*     */ 
/*     */   public final void setReadOnlyMap(LocalNameQualifiedNamesMap readOnlyMap, boolean clear) {
/* 104 */     this._readOnlyMap = readOnlyMap;
/* 105 */     if (this._readOnlyMap != null) {
/* 106 */       this._readOnlyMapSize = this._readOnlyMap.size();
/* 107 */       this._index = this._readOnlyMap.getIndex();
/* 108 */       if (clear)
/* 109 */         clear();
/*     */     }
/*     */     else {
/* 112 */       this._readOnlyMapSize = 0;
/* 113 */       this._index = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final boolean isQNameFromReadOnlyMap(QualifiedName name) {
/* 118 */     return (this._readOnlyMap != null) && (name.index <= this._readOnlyMap.getIndex());
/*     */   }
/*     */ 
/*     */   public final int getNextIndex() {
/* 122 */     return this._index++;
/*     */   }
/*     */ 
/*     */   public final int getIndex() {
/* 126 */     return this._index;
/*     */   }
/*     */ 
/*     */   public final Entry obtainEntry(String key) {
/* 130 */     int hash = hashHash(key.hashCode());
/*     */ 
/* 132 */     if (this._readOnlyMap != null) {
/* 133 */       Entry entry = this._readOnlyMap.getEntry(key, hash);
/* 134 */       if (entry != null) {
/* 135 */         return entry;
/*     */       }
/*     */     }
/*     */ 
/* 139 */     int tableIndex = indexFor(hash, this._table.length);
/* 140 */     for (Entry e = this._table[tableIndex]; e != null; e = e._next) {
/* 141 */       if ((e._hash == hash) && (eq(key, e._key))) {
/* 142 */         return e;
/*     */       }
/*     */     }
/*     */ 
/* 146 */     return addEntry(key, hash, tableIndex);
/*     */   }
/*     */ 
/*     */   public final Entry obtainDynamicEntry(String key) {
/* 150 */     int hash = hashHash(key.hashCode());
/*     */ 
/* 152 */     int tableIndex = indexFor(hash, this._table.length);
/* 153 */     for (Entry e = this._table[tableIndex]; e != null; e = e._next) {
/* 154 */       if ((e._hash == hash) && (eq(key, e._key))) {
/* 155 */         return e;
/*     */       }
/*     */     }
/*     */ 
/* 159 */     return addEntry(key, hash, tableIndex);
/*     */   }
/*     */ 
/*     */   private final Entry getEntry(String key, int hash) {
/* 163 */     if (this._readOnlyMap != null) {
/* 164 */       Entry entry = this._readOnlyMap.getEntry(key, hash);
/* 165 */       if (entry != null) {
/* 166 */         return entry;
/*     */       }
/*     */     }
/*     */ 
/* 170 */     int tableIndex = indexFor(hash, this._table.length);
/* 171 */     for (Entry e = this._table[tableIndex]; e != null; e = e._next) {
/* 172 */       if ((e._hash == hash) && (eq(key, e._key))) {
/* 173 */         return e;
/*     */       }
/*     */     }
/*     */ 
/* 177 */     return null;
/*     */   }
/*     */ 
/*     */   private final Entry addEntry(String key, int hash, int bucketIndex)
/*     */   {
/* 182 */     Entry e = this._table[bucketIndex];
/* 183 */     this._table[bucketIndex] = new Entry(key, hash, e);
/* 184 */     e = this._table[bucketIndex];
/* 185 */     if (this._size++ >= this._threshold) {
/* 186 */       resize(2 * this._table.length);
/*     */     }
/*     */ 
/* 189 */     return e;
/*     */   }
/*     */ 
/*     */   private final void resize(int newCapacity) {
/* 193 */     this._capacity = newCapacity;
/* 194 */     Entry[] oldTable = this._table;
/* 195 */     int oldCapacity = oldTable.length;
/* 196 */     if (oldCapacity == 1048576) {
/* 197 */       this._threshold = 2147483647;
/* 198 */       return;
/*     */     }
/*     */ 
/* 201 */     Entry[] newTable = new Entry[this._capacity];
/* 202 */     transfer(newTable);
/* 203 */     this._table = newTable;
/* 204 */     this._threshold = ((int)(this._capacity * this._loadFactor));
/*     */   }
/*     */ 
/*     */   private final void transfer(Entry[] newTable) {
/* 208 */     Entry[] src = this._table;
/* 209 */     int newCapacity = newTable.length;
/* 210 */     for (int j = 0; j < src.length; j++) {
/* 211 */       Entry e = src[j];
/* 212 */       if (e != null) {
/* 213 */         src[j] = null;
/*     */         do {
/* 215 */           Entry next = e._next;
/* 216 */           int i = indexFor(e._hash, newCapacity);
/* 217 */           e._next = newTable[i];
/* 218 */           newTable[i] = e;
/* 219 */           e = next;
/* 220 */         }while (e != null);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private final boolean eq(String x, String y) {
/* 226 */     return (x == y) || (x.equals(y));
/*     */   }
/*     */ 
/*     */   public static class Entry
/*     */   {
/*     */     final String _key;
/*     */     final int _hash;
/*     */     public QualifiedName[] _value;
/*     */     public int _valueIndex;
/*     */     Entry _next;
/*     */ 
/*     */     public Entry(String key, int hash, Entry next)
/*     */     {
/*  47 */       this._key = key;
/*  48 */       this._hash = hash;
/*  49 */       this._next = next;
/*  50 */       this._value = new QualifiedName[1];
/*     */     }
/*     */ 
/*     */     public void addQualifiedName(QualifiedName name) {
/*  54 */       if (this._valueIndex < this._value.length) {
/*  55 */         this._value[(this._valueIndex++)] = name;
/*  56 */       } else if (this._valueIndex == this._value.length) {
/*  57 */         QualifiedName[] newValue = new QualifiedName[this._valueIndex * 3 / 2 + 1];
/*  58 */         System.arraycopy(this._value, 0, newValue, 0, this._valueIndex);
/*  59 */         this._value = newValue;
/*  60 */         this._value[(this._valueIndex++)] = name;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.util.LocalNameQualifiedNamesMap
 * JD-Core Version:    0.6.2
 */