/*     */ package com.sun.corba.se.impl.orbutil;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ 
/*     */ public class CacheTable
/*     */ {
/*     */   private boolean noReverseMap;
/*     */   static final int INITIAL_SIZE = 16;
/*     */   static final int MAX_SIZE = 1073741824;
/*     */   int size;
/*     */   int entryCount;
/*     */   private Entry[] map;
/*     */   private Entry[] rmap;
/*     */   private ORB orb;
/*     */   private ORBUtilSystemException wrapper;
/*     */ 
/*     */   private CacheTable()
/*     */   {
/*     */   }
/*     */ 
/*     */   public CacheTable(ORB paramORB, boolean paramBoolean)
/*     */   {
/*  63 */     this.orb = paramORB;
/*  64 */     this.wrapper = ORBUtilSystemException.get(paramORB, "rpc.encoding");
/*     */ 
/*  66 */     this.noReverseMap = paramBoolean;
/*  67 */     this.size = 16;
/*  68 */     this.entryCount = 0;
/*  69 */     initTables();
/*     */   }
/*     */   private void initTables() {
/*  72 */     this.map = new Entry[this.size];
/*  73 */     this.rmap = (this.noReverseMap ? null : new Entry[this.size]);
/*     */   }
/*     */   private void grow() {
/*  76 */     if (this.size == 1073741824)
/*  77 */       return;
/*  78 */     Entry[] arrayOfEntry = this.map;
/*  79 */     int i = this.size;
/*  80 */     this.size <<= 1;
/*  81 */     initTables();
/*     */ 
/*  83 */     for (int j = 0; j < i; j++)
/*  84 */       for (Entry localEntry = arrayOfEntry[j]; localEntry != null; localEntry = localEntry.next)
/*  85 */         put_table(localEntry.key, localEntry.val);
/*     */   }
/*     */ 
/*     */   private int moduloTableSize(int paramInt)
/*     */   {
/*  91 */     paramInt += (paramInt << 9 ^ 0xFFFFFFFF);
/*  92 */     paramInt ^= paramInt >>> 14;
/*  93 */     paramInt += (paramInt << 4);
/*  94 */     paramInt ^= paramInt >>> 10;
/*  95 */     return paramInt & this.size - 1;
/*     */   }
/*     */   private int hash(Object paramObject) {
/*  98 */     return moduloTableSize(System.identityHashCode(paramObject));
/*     */   }
/*     */   private int hash(int paramInt) {
/* 101 */     return moduloTableSize(paramInt);
/*     */   }
/*     */   public final void put(Object paramObject, int paramInt) {
/* 104 */     if (put_table(paramObject, paramInt)) {
/* 105 */       this.entryCount += 1;
/* 106 */       if (this.entryCount > this.size * 3 / 4)
/* 107 */         grow(); 
/*     */     }
/*     */   }
/*     */ 
/* 111 */   private boolean put_table(Object paramObject, int paramInt) { int i = hash(paramObject);
/* 112 */     for (Entry localEntry = this.map[i]; localEntry != null; localEntry = localEntry.next) {
/* 113 */       if (localEntry.key == paramObject) {
/* 114 */         if (localEntry.val != paramInt) {
/* 115 */           throw this.wrapper.duplicateIndirectionOffset();
/*     */         }
/*     */ 
/* 119 */         return false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 124 */     localEntry = new Entry(paramObject, paramInt);
/* 125 */     localEntry.next = this.map[i];
/* 126 */     this.map[i] = localEntry;
/* 127 */     if (!this.noReverseMap) {
/* 128 */       int j = hash(paramInt);
/* 129 */       localEntry.rnext = this.rmap[j];
/* 130 */       this.rmap[j] = localEntry;
/*     */     }
/* 132 */     return true; }
/*     */ 
/*     */   public final boolean containsKey(Object paramObject) {
/* 135 */     return getVal(paramObject) != -1;
/*     */   }
/*     */   public final int getVal(Object paramObject) {
/* 138 */     int i = hash(paramObject);
/* 139 */     for (Entry localEntry = this.map[i]; localEntry != null; localEntry = localEntry.next) {
/* 140 */       if (localEntry.key == paramObject)
/* 141 */         return localEntry.val;
/*     */     }
/* 143 */     return -1;
/*     */   }
/*     */   public final boolean containsVal(int paramInt) {
/* 146 */     return getKey(paramInt) != null;
/*     */   }
/*     */   public final boolean containsOrderedVal(int paramInt) {
/* 149 */     return containsVal(paramInt);
/*     */   }
/*     */   public final Object getKey(int paramInt) {
/* 152 */     int i = hash(paramInt);
/* 153 */     for (Entry localEntry = this.rmap[i]; localEntry != null; localEntry = localEntry.rnext) {
/* 154 */       if (localEntry.val == paramInt)
/* 155 */         return localEntry.key;
/*     */     }
/* 157 */     return null;
/*     */   }
/*     */   public void done() {
/* 160 */     this.map = null;
/* 161 */     this.rmap = null;
/*     */   }
/*     */ 
/*     */   class Entry
/*     */   {
/*     */     Object key;
/*     */     int val;
/*     */     Entry next;
/*     */     Entry rnext;
/*     */ 
/*     */     public Entry(Object paramInt, int arg3)
/*     */     {
/*  42 */       this.key = paramInt;
/*     */       int i;
/*  43 */       this.val = i;
/*  44 */       this.next = null;
/*  45 */       this.rnext = null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orbutil.CacheTable
 * JD-Core Version:    0.6.2
 */