/*    */ package com.sun.xml.internal.fastinfoset.util;
/*    */ 
/*    */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
/*    */ 
/*    */ public class FixedEntryStringIntMap extends StringIntMap
/*    */ {
/*    */   private StringIntMap.Entry _fixedEntry;
/*    */ 
/*    */   public FixedEntryStringIntMap(String fixedEntry, int initialCapacity, float loadFactor)
/*    */   {
/* 37 */     super(initialCapacity, loadFactor);
/*    */ 
/* 40 */     int hash = hashHash(fixedEntry.hashCode());
/* 41 */     int tableIndex = indexFor(hash, this._table.length);
/*    */     void tmp56_53 = new StringIntMap.Entry(fixedEntry, hash, this._index++, null); this._fixedEntry = tmp56_53; this._table[tableIndex] = tmp56_53;
/* 43 */     if (this._size++ >= this._threshold)
/* 44 */       resize(2 * this._table.length);
/*    */   }
/*    */ 
/*    */   public FixedEntryStringIntMap(String fixedEntry, int initialCapacity)
/*    */   {
/* 49 */     this(fixedEntry, initialCapacity, 0.75F);
/*    */   }
/*    */ 
/*    */   public FixedEntryStringIntMap(String fixedEntry) {
/* 53 */     this(fixedEntry, 16, 0.75F);
/*    */   }
/*    */ 
/*    */   public final void clear() {
/* 57 */     for (int i = 0; i < this._table.length; i++) {
/* 58 */       this._table[i] = null;
/*    */     }
/* 60 */     this._lastEntry = NULL_ENTRY;
/*    */ 
/* 62 */     if (this._fixedEntry != null) {
/* 63 */       int tableIndex = indexFor(this._fixedEntry._hash, this._table.length);
/* 64 */       this._table[tableIndex] = this._fixedEntry;
/* 65 */       this._fixedEntry._next = null;
/* 66 */       this._size = 1;
/* 67 */       this._index = (this._readOnlyMapSize + 1);
/*    */     } else {
/* 69 */       this._size = 0;
/* 70 */       this._index = this._readOnlyMapSize;
/*    */     }
/*    */   }
/*    */ 
/*    */   public final void setReadOnlyMap(KeyIntMap readOnlyMap, boolean clear) {
/* 75 */     if (!(readOnlyMap instanceof FixedEntryStringIntMap)) {
/* 76 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.illegalClass", new Object[] { readOnlyMap }));
/*    */     }
/*    */ 
/* 80 */     setReadOnlyMap((FixedEntryStringIntMap)readOnlyMap, clear);
/*    */   }
/*    */ 
/*    */   public final void setReadOnlyMap(FixedEntryStringIntMap readOnlyMap, boolean clear) {
/* 84 */     this._readOnlyMap = readOnlyMap;
/* 85 */     if (this._readOnlyMap != null) {
/* 86 */       readOnlyMap.removeFixedEntry();
/* 87 */       this._readOnlyMapSize = readOnlyMap.size();
/* 88 */       this._index = (this._readOnlyMapSize + this._size);
/* 89 */       if (clear)
/* 90 */         clear();
/*    */     }
/*    */     else {
/* 93 */       this._readOnlyMapSize = 0;
/*    */     }
/*    */   }
/*    */ 
/*    */   private final void removeFixedEntry() {
/* 98 */     if (this._fixedEntry != null) {
/* 99 */       int tableIndex = indexFor(this._fixedEntry._hash, this._table.length);
/* 100 */       StringIntMap.Entry firstEntry = this._table[tableIndex];
/* 101 */       if (firstEntry == this._fixedEntry) {
/* 102 */         this._table[tableIndex] = this._fixedEntry._next;
/*    */       } else {
/* 104 */         StringIntMap.Entry previousEntry = firstEntry;
/* 105 */         while (previousEntry._next != this._fixedEntry) {
/* 106 */           previousEntry = previousEntry._next;
/*    */         }
/* 108 */         previousEntry._next = this._fixedEntry._next;
/*    */       }
/*    */ 
/* 111 */       this._fixedEntry = null;
/* 112 */       this._size -= 1;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.util.FixedEntryStringIntMap
 * JD-Core Version:    0.6.2
 */