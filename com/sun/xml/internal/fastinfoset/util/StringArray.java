/*     */ package com.sun.xml.internal.fastinfoset.util;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
/*     */ 
/*     */ public class StringArray extends ValueArray
/*     */ {
/*     */   public String[] _array;
/*     */   private StringArray _readOnlyArray;
/*     */   private boolean _clear;
/*     */ 
/*     */   public StringArray(int initialCapacity, int maximumCapacity, boolean clear)
/*     */   {
/*  40 */     this._array = new String[initialCapacity];
/*  41 */     this._maximumCapacity = maximumCapacity;
/*  42 */     this._clear = clear;
/*     */   }
/*     */ 
/*     */   public StringArray() {
/*  46 */     this(10, 2147483647, false);
/*     */   }
/*     */ 
/*     */   public final void clear() {
/*  50 */     if (this._clear) for (int i = this._readOnlyArraySize; i < this._size; i++) {
/*  51 */         this._array[i] = null;
/*     */       }
/*  53 */     this._size = this._readOnlyArraySize;
/*     */   }
/*     */ 
/*     */   public final String[] getArray() {
/*  57 */     return this._array;
/*     */   }
/*     */ 
/*     */   public final void setReadOnlyArray(ValueArray readOnlyArray, boolean clear) {
/*  61 */     if (!(readOnlyArray instanceof StringArray)) {
/*  62 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.illegalClass", new Object[] { readOnlyArray }));
/*     */     }
/*     */ 
/*  66 */     setReadOnlyArray((StringArray)readOnlyArray, clear);
/*     */   }
/*     */ 
/*     */   public final void setReadOnlyArray(StringArray readOnlyArray, boolean clear) {
/*  70 */     if (readOnlyArray != null) {
/*  71 */       this._readOnlyArray = readOnlyArray;
/*  72 */       this._readOnlyArraySize = readOnlyArray.getSize();
/*     */ 
/*  74 */       if (clear) {
/*  75 */         clear();
/*     */       }
/*     */ 
/*  78 */       this._array = getCompleteArray();
/*  79 */       this._size = this._readOnlyArraySize;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final String[] getCompleteArray() {
/*  84 */     if (this._readOnlyArray == null) {
/*  85 */       return this._array;
/*     */     }
/*  87 */     String[] ra = this._readOnlyArray.getCompleteArray();
/*  88 */     String[] a = new String[this._readOnlyArraySize + this._array.length];
/*  89 */     System.arraycopy(ra, 0, a, 0, this._readOnlyArraySize);
/*  90 */     return a;
/*     */   }
/*     */ 
/*     */   public final String get(int i)
/*     */   {
/*  95 */     return this._array[i];
/*     */   }
/*     */ 
/*     */   public final int add(String s) {
/*  99 */     if (this._size == this._array.length) {
/* 100 */       resize();
/*     */     }
/*     */ 
/* 103 */     this._array[(this._size++)] = s;
/* 104 */     return this._size;
/*     */   }
/*     */ 
/*     */   protected final void resize() {
/* 108 */     if (this._size == this._maximumCapacity) {
/* 109 */       throw new ValueArrayResourceException(CommonResourceBundle.getInstance().getString("message.arrayMaxCapacity"));
/*     */     }
/*     */ 
/* 112 */     int newSize = this._size * 3 / 2 + 1;
/* 113 */     if (newSize > this._maximumCapacity) {
/* 114 */       newSize = this._maximumCapacity;
/*     */     }
/*     */ 
/* 117 */     String[] newArray = new String[newSize];
/* 118 */     System.arraycopy(this._array, 0, newArray, 0, this._size);
/* 119 */     this._array = newArray;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.util.StringArray
 * JD-Core Version:    0.6.2
 */