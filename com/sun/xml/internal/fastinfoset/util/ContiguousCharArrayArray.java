/*     */ package com.sun.xml.internal.fastinfoset.util;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
/*     */ 
/*     */ public class ContiguousCharArrayArray extends ValueArray
/*     */ {
/*     */   public static final int INITIAL_CHARACTER_SIZE = 512;
/*     */   public static final int MAXIMUM_CHARACTER_SIZE = 2147483647;
/*     */   protected int _maximumCharacterSize;
/*     */   public int[] _offset;
/*     */   public int[] _length;
/*     */   public char[] _array;
/*     */   public int _arrayIndex;
/*     */   public int _readOnlyArrayIndex;
/*     */   private String[] _cachedStrings;
/*     */   public int _cachedIndex;
/*     */   private ContiguousCharArrayArray _readOnlyArray;
/*     */ 
/*     */   public ContiguousCharArrayArray(int initialCapacity, int maximumCapacity, int initialCharacterSize, int maximumCharacterSize)
/*     */   {
/*  52 */     this._offset = new int[initialCapacity];
/*  53 */     this._length = new int[initialCapacity];
/*  54 */     this._array = new char[initialCharacterSize];
/*  55 */     this._maximumCapacity = maximumCapacity;
/*  56 */     this._maximumCharacterSize = maximumCharacterSize;
/*     */   }
/*     */ 
/*     */   public ContiguousCharArrayArray() {
/*  60 */     this(10, 2147483647, 512, 2147483647);
/*     */   }
/*     */ 
/*     */   public final void clear()
/*     */   {
/*  65 */     this._arrayIndex = this._readOnlyArrayIndex;
/*  66 */     this._size = this._readOnlyArraySize;
/*     */ 
/*  68 */     if (this._cachedStrings != null)
/*  69 */       for (int i = this._readOnlyArraySize; i < this._cachedStrings.length; i++)
/*  70 */         this._cachedStrings[i] = null;
/*     */   }
/*     */ 
/*     */   public final int getArrayIndex()
/*     */   {
/*  76 */     return this._arrayIndex;
/*     */   }
/*     */ 
/*     */   public final void setReadOnlyArray(ValueArray readOnlyArray, boolean clear) {
/*  80 */     if (!(readOnlyArray instanceof ContiguousCharArrayArray)) {
/*  81 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.illegalClass", new Object[] { readOnlyArray }));
/*     */     }
/*     */ 
/*  84 */     setReadOnlyArray((ContiguousCharArrayArray)readOnlyArray, clear);
/*     */   }
/*     */ 
/*     */   public final void setReadOnlyArray(ContiguousCharArrayArray readOnlyArray, boolean clear) {
/*  88 */     if (readOnlyArray != null) {
/*  89 */       this._readOnlyArray = readOnlyArray;
/*  90 */       this._readOnlyArraySize = readOnlyArray.getSize();
/*  91 */       this._readOnlyArrayIndex = readOnlyArray.getArrayIndex();
/*     */ 
/*  93 */       if (clear) {
/*  94 */         clear();
/*     */       }
/*     */ 
/*  97 */       this._array = getCompleteCharArray();
/*  98 */       this._offset = getCompleteOffsetArray();
/*  99 */       this._length = getCompleteLengthArray();
/* 100 */       this._size = this._readOnlyArraySize;
/* 101 */       this._arrayIndex = this._readOnlyArrayIndex;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final char[] getCompleteCharArray() {
/* 106 */     if (this._readOnlyArray == null) {
/* 107 */       return this._array;
/*     */     }
/* 109 */     char[] ra = this._readOnlyArray.getCompleteCharArray();
/* 110 */     char[] a = new char[this._readOnlyArrayIndex + this._array.length];
/* 111 */     System.arraycopy(ra, 0, a, 0, this._readOnlyArrayIndex);
/* 112 */     return a;
/*     */   }
/*     */ 
/*     */   public final int[] getCompleteOffsetArray()
/*     */   {
/* 117 */     if (this._readOnlyArray == null) {
/* 118 */       return this._offset;
/*     */     }
/* 120 */     int[] ra = this._readOnlyArray.getCompleteOffsetArray();
/* 121 */     int[] a = new int[this._readOnlyArraySize + this._offset.length];
/* 122 */     System.arraycopy(ra, 0, a, 0, this._readOnlyArraySize);
/* 123 */     return a;
/*     */   }
/*     */ 
/*     */   public final int[] getCompleteLengthArray()
/*     */   {
/* 128 */     if (this._readOnlyArray == null) {
/* 129 */       return this._length;
/*     */     }
/* 131 */     int[] ra = this._readOnlyArray.getCompleteLengthArray();
/* 132 */     int[] a = new int[this._readOnlyArraySize + this._length.length];
/* 133 */     System.arraycopy(ra, 0, a, 0, this._readOnlyArraySize);
/* 134 */     return a;
/*     */   }
/*     */ 
/*     */   public final String getString(int i)
/*     */   {
/* 139 */     if ((this._cachedStrings != null) && (i < this._cachedStrings.length)) {
/* 140 */       String s = this._cachedStrings[i];
/* 141 */       return this._cachedStrings[i] = (s != null ? s : ) = new String(this._array, this._offset[i], this._length[i]);
/*     */     }
/*     */ 
/* 144 */     String[] newCachedStrings = new String[this._offset.length];
/* 145 */     if ((this._cachedStrings != null) && (i >= this._cachedStrings.length)) {
/* 146 */       System.arraycopy(this._cachedStrings, 0, newCachedStrings, 0, this._cachedStrings.length);
/*     */     }
/* 148 */     this._cachedStrings = newCachedStrings;
/*     */ 
/* 150 */     return this._cachedStrings[i] =  = new String(this._array, this._offset[i], this._length[i]);
/*     */   }
/*     */ 
/*     */   public final void ensureSize(int l) {
/* 154 */     if (this._arrayIndex + l >= this._array.length)
/* 155 */       resizeArray(this._arrayIndex + l);
/*     */   }
/*     */ 
/*     */   public final void add(int l)
/*     */   {
/* 160 */     if (this._size == this._offset.length) {
/* 161 */       resize();
/*     */     }
/*     */ 
/* 164 */     this._cachedIndex = this._size;
/* 165 */     this._offset[this._size] = this._arrayIndex;
/* 166 */     this._length[(this._size++)] = l;
/*     */ 
/* 168 */     this._arrayIndex += l;
/*     */   }
/*     */ 
/*     */   public final int add(char[] c, int l) {
/* 172 */     if (this._size == this._offset.length) {
/* 173 */       resize();
/*     */     }
/*     */ 
/* 176 */     int oldArrayIndex = this._arrayIndex;
/* 177 */     int arrayIndex = oldArrayIndex + l;
/*     */ 
/* 179 */     this._cachedIndex = this._size;
/* 180 */     this._offset[this._size] = oldArrayIndex;
/* 181 */     this._length[(this._size++)] = l;
/*     */ 
/* 183 */     if (arrayIndex >= this._array.length) {
/* 184 */       resizeArray(arrayIndex);
/*     */     }
/*     */ 
/* 187 */     System.arraycopy(c, 0, this._array, oldArrayIndex, l);
/*     */ 
/* 189 */     this._arrayIndex = arrayIndex;
/* 190 */     return oldArrayIndex;
/*     */   }
/*     */ 
/*     */   protected final void resize() {
/* 194 */     if (this._size == this._maximumCapacity) {
/* 195 */       throw new ValueArrayResourceException(CommonResourceBundle.getInstance().getString("message.arrayMaxCapacity"));
/*     */     }
/*     */ 
/* 198 */     int newSize = this._size * 3 / 2 + 1;
/* 199 */     if (newSize > this._maximumCapacity) {
/* 200 */       newSize = this._maximumCapacity;
/*     */     }
/*     */ 
/* 203 */     int[] offset = new int[newSize];
/* 204 */     System.arraycopy(this._offset, 0, offset, 0, this._size);
/* 205 */     this._offset = offset;
/*     */ 
/* 207 */     int[] length = new int[newSize];
/* 208 */     System.arraycopy(this._length, 0, length, 0, this._size);
/* 209 */     this._length = length;
/*     */   }
/*     */ 
/*     */   protected final void resizeArray(int requestedSize) {
/* 213 */     if (this._arrayIndex == this._maximumCharacterSize) {
/* 214 */       throw new ValueArrayResourceException(CommonResourceBundle.getInstance().getString("message.maxNumberOfCharacters"));
/*     */     }
/*     */ 
/* 217 */     int newSize = requestedSize * 3 / 2 + 1;
/* 218 */     if (newSize > this._maximumCharacterSize) {
/* 219 */       newSize = this._maximumCharacterSize;
/*     */     }
/*     */ 
/* 222 */     char[] array = new char[newSize];
/* 223 */     System.arraycopy(this._array, 0, array, 0, this._arrayIndex);
/* 224 */     this._array = array;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.util.ContiguousCharArrayArray
 * JD-Core Version:    0.6.2
 */