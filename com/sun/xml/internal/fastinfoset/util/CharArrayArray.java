/*     */ package com.sun.xml.internal.fastinfoset.util;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
/*     */ 
/*     */ public class CharArrayArray extends ValueArray
/*     */ {
/*     */   private CharArray[] _array;
/*     */   private CharArrayArray _readOnlyArray;
/*     */ 
/*     */   public CharArrayArray(int initialCapacity, int maximumCapacity)
/*     */   {
/*  39 */     this._array = new CharArray[initialCapacity];
/*  40 */     this._maximumCapacity = maximumCapacity;
/*     */   }
/*     */ 
/*     */   public CharArrayArray() {
/*  44 */     this(10, 2147483647);
/*     */   }
/*     */ 
/*     */   public final void clear() {
/*  48 */     for (int i = 0; i < this._size; i++) {
/*  49 */       this._array[i] = null;
/*     */     }
/*  51 */     this._size = 0;
/*     */   }
/*     */ 
/*     */   public final CharArray[] getArray() {
/*  55 */     return this._array;
/*     */   }
/*     */ 
/*     */   public final void setReadOnlyArray(ValueArray readOnlyArray, boolean clear) {
/*  59 */     if (!(readOnlyArray instanceof CharArrayArray)) {
/*  60 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.illegalClass", new Object[] { readOnlyArray }));
/*     */     }
/*     */ 
/*  63 */     setReadOnlyArray((CharArrayArray)readOnlyArray, clear);
/*     */   }
/*     */ 
/*     */   public final void setReadOnlyArray(CharArrayArray readOnlyArray, boolean clear) {
/*  67 */     if (readOnlyArray != null) {
/*  68 */       this._readOnlyArray = readOnlyArray;
/*  69 */       this._readOnlyArraySize = readOnlyArray.getSize();
/*     */ 
/*  71 */       if (clear)
/*  72 */         clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   public final CharArray get(int i)
/*     */   {
/*  78 */     if (this._readOnlyArray == null) {
/*  79 */       return this._array[i];
/*     */     }
/*  81 */     if (i < this._readOnlyArraySize) {
/*  82 */       return this._readOnlyArray.get(i);
/*     */     }
/*  84 */     return this._array[(i - this._readOnlyArraySize)];
/*     */   }
/*     */ 
/*     */   public final void add(CharArray s)
/*     */   {
/*  90 */     if (this._size == this._array.length) {
/*  91 */       resize();
/*     */     }
/*     */ 
/*  94 */     this._array[(this._size++)] = s;
/*     */   }
/*     */ 
/*     */   protected final void resize() {
/*  98 */     if (this._size == this._maximumCapacity) {
/*  99 */       throw new ValueArrayResourceException(CommonResourceBundle.getInstance().getString("message.arrayMaxCapacity"));
/*     */     }
/*     */ 
/* 102 */     int newSize = this._size * 3 / 2 + 1;
/* 103 */     if (newSize > this._maximumCapacity) {
/* 104 */       newSize = this._maximumCapacity;
/*     */     }
/*     */ 
/* 107 */     CharArray[] newArray = new CharArray[newSize];
/* 108 */     System.arraycopy(this._array, 0, newArray, 0, this._size);
/* 109 */     this._array = newArray;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.util.CharArrayArray
 * JD-Core Version:    0.6.2
 */