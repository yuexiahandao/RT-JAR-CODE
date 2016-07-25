/*     */ package com.sun.xml.internal.fastinfoset.util;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
/*     */ import com.sun.xml.internal.fastinfoset.QualifiedName;
/*     */ 
/*     */ public class QualifiedNameArray extends ValueArray
/*     */ {
/*     */   public QualifiedName[] _array;
/*     */   private QualifiedNameArray _readOnlyArray;
/*     */ 
/*     */   public QualifiedNameArray(int initialCapacity, int maximumCapacity)
/*     */   {
/*  40 */     this._array = new QualifiedName[initialCapacity];
/*  41 */     this._maximumCapacity = maximumCapacity;
/*     */   }
/*     */ 
/*     */   public QualifiedNameArray() {
/*  45 */     this(10, 2147483647);
/*     */   }
/*     */ 
/*     */   public final void clear() {
/*  49 */     this._size = this._readOnlyArraySize;
/*     */   }
/*     */ 
/*     */   public final QualifiedName[] getArray() {
/*  53 */     return this._array;
/*     */   }
/*     */ 
/*     */   public final void setReadOnlyArray(ValueArray readOnlyArray, boolean clear) {
/*  57 */     if (!(readOnlyArray instanceof QualifiedNameArray)) {
/*  58 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.illegalClass", new Object[] { readOnlyArray }));
/*     */     }
/*     */ 
/*  62 */     setReadOnlyArray((QualifiedNameArray)readOnlyArray, clear);
/*     */   }
/*     */ 
/*     */   public final void setReadOnlyArray(QualifiedNameArray readOnlyArray, boolean clear) {
/*  66 */     if (readOnlyArray != null) {
/*  67 */       this._readOnlyArray = readOnlyArray;
/*  68 */       this._readOnlyArraySize = readOnlyArray.getSize();
/*     */ 
/*  70 */       if (clear) {
/*  71 */         clear();
/*     */       }
/*     */ 
/*  74 */       this._array = getCompleteArray();
/*  75 */       this._size = this._readOnlyArraySize;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final QualifiedName[] getCompleteArray() {
/*  80 */     if (this._readOnlyArray == null) {
/*  81 */       return this._array;
/*     */     }
/*  83 */     QualifiedName[] ra = this._readOnlyArray.getCompleteArray();
/*  84 */     QualifiedName[] a = new QualifiedName[this._readOnlyArraySize + this._array.length];
/*  85 */     System.arraycopy(ra, 0, a, 0, this._readOnlyArraySize);
/*  86 */     return a;
/*     */   }
/*     */ 
/*     */   public final QualifiedName getNext()
/*     */   {
/*  91 */     return this._size == this._array.length ? null : this._array[this._size];
/*     */   }
/*     */ 
/*     */   public final void add(QualifiedName s) {
/*  95 */     if (this._size == this._array.length) {
/*  96 */       resize();
/*     */     }
/*     */ 
/*  99 */     this._array[(this._size++)] = s;
/*     */   }
/*     */ 
/*     */   protected final void resize() {
/* 103 */     if (this._size == this._maximumCapacity) {
/* 104 */       throw new ValueArrayResourceException(CommonResourceBundle.getInstance().getString("message.arrayMaxCapacity"));
/*     */     }
/*     */ 
/* 107 */     int newSize = this._size * 3 / 2 + 1;
/* 108 */     if (newSize > this._maximumCapacity) {
/* 109 */       newSize = this._maximumCapacity;
/*     */     }
/*     */ 
/* 112 */     QualifiedName[] newArray = new QualifiedName[newSize];
/* 113 */     System.arraycopy(this._array, 0, newArray, 0, this._size);
/* 114 */     this._array = newArray;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.util.QualifiedNameArray
 * JD-Core Version:    0.6.2
 */