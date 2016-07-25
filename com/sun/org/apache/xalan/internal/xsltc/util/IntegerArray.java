/*     */ package com.sun.org.apache.xalan.internal.xsltc.util;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public final class IntegerArray
/*     */ {
/*     */   private static final int InitialSize = 32;
/*     */   private int[] _array;
/*     */   private int _size;
/*  34 */   private int _free = 0;
/*     */ 
/*     */   public IntegerArray() {
/*  37 */     this(32);
/*     */   }
/*     */ 
/*     */   public IntegerArray(int size) {
/*  41 */     this._array = new int[this._size = size];
/*     */   }
/*     */ 
/*     */   public IntegerArray(int[] array) {
/*  45 */     this(array.length);
/*  46 */     System.arraycopy(array, 0, this._array, 0, this._free = this._size);
/*     */   }
/*     */ 
/*     */   public void clear() {
/*  50 */     this._free = 0;
/*     */   }
/*     */ 
/*     */   public Object clone() {
/*  54 */     IntegerArray clone = new IntegerArray(this._free > 0 ? this._free : 1);
/*  55 */     System.arraycopy(this._array, 0, clone._array, 0, this._free);
/*  56 */     clone._free = this._free;
/*  57 */     return clone;
/*     */   }
/*     */ 
/*     */   public int[] toIntArray() {
/*  61 */     int[] result = new int[cardinality()];
/*  62 */     System.arraycopy(this._array, 0, result, 0, cardinality());
/*  63 */     return result;
/*     */   }
/*     */ 
/*     */   public final int at(int index) {
/*  67 */     return this._array[index];
/*     */   }
/*     */ 
/*     */   public final void set(int index, int value) {
/*  71 */     this._array[index] = value;
/*     */   }
/*     */ 
/*     */   public int indexOf(int n) {
/*  75 */     for (int i = 0; i < this._free; i++) {
/*  76 */       if (n == this._array[i]) return i;
/*     */     }
/*  78 */     return -1;
/*     */   }
/*     */ 
/*     */   public final void add(int value) {
/*  82 */     if (this._free == this._size) {
/*  83 */       growArray(this._size * 2);
/*     */     }
/*  85 */     this._array[(this._free++)] = value;
/*     */   }
/*     */ 
/*     */   public void addNew(int value)
/*     */   {
/*  92 */     for (int i = 0; i < this._free; i++) {
/*  93 */       if (this._array[i] == value) return;
/*     */     }
/*  95 */     add(value);
/*     */   }
/*     */ 
/*     */   public void reverse() {
/*  99 */     int left = 0;
/* 100 */     int right = this._free - 1;
/*     */ 
/* 102 */     while (left < right) {
/* 103 */       int temp = this._array[left];
/* 104 */       this._array[(left++)] = this._array[right];
/* 105 */       this._array[(right--)] = temp;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void merge(IntegerArray other)
/*     */   {
/* 114 */     int newSize = this._free + other._free;
/*     */ 
/* 116 */     int[] newArray = new int[newSize];
/*     */ 
/* 119 */     int i = 0; int j = 0;
/* 120 */     for (int k = 0; (i < this._free) && (j < other._free); k++) {
/* 121 */       int x = this._array[i];
/* 122 */       int y = other._array[j];
/*     */ 
/* 124 */       if (x < y) {
/* 125 */         newArray[k] = x;
/* 126 */         i++;
/*     */       }
/* 128 */       else if (x > y) {
/* 129 */         newArray[k] = y;
/* 130 */         j++;
/*     */       }
/*     */       else {
/* 133 */         newArray[k] = x;
/* 134 */         i++; j++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 139 */     if (i >= this._free) {
/* 140 */       while (j < other._free) {
/* 141 */         newArray[(k++)] = other._array[(j++)];
/*     */       }
/*     */     }
/*     */ 
/* 145 */     while (i < this._free) {
/* 146 */       newArray[(k++)] = this._array[(i++)];
/*     */     }
/*     */ 
/* 151 */     this._array = newArray;
/* 152 */     this._free = (this._size = newSize);
/*     */   }
/*     */ 
/*     */   public void sort()
/*     */   {
/* 157 */     quicksort(this._array, 0, this._free - 1);
/*     */   }
/*     */ 
/*     */   private static void quicksort(int[] array, int p, int r) {
/* 161 */     if (p < r) {
/* 162 */       int q = partition(array, p, r);
/* 163 */       quicksort(array, p, q);
/* 164 */       quicksort(array, q + 1, r);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static int partition(int[] array, int p, int r) {
/* 169 */     int x = array[(p + r >>> 1)];
/* 170 */     int i = p - 1; int j = r + 1;
/*     */     while (true)
/*     */     {
/* 173 */       if (x >= array[(--j)]) {
/* 174 */         while (x > array[(++i)]);
/* 175 */         if (i >= j) break;
/* 176 */         int temp = array[i];
/* 177 */         array[i] = array[j];
/* 178 */         array[j] = temp;
/*     */       }
/*     */     }
/* 181 */     return j;
/*     */   }
/*     */ 
/*     */   private void growArray(int size)
/*     */   {
/* 187 */     int[] newArray = new int[this._size = size];
/* 188 */     System.arraycopy(this._array, 0, newArray, 0, this._free);
/* 189 */     this._array = newArray;
/*     */   }
/*     */ 
/*     */   public int popLast() {
/* 193 */     return this._array[(--this._free)];
/*     */   }
/*     */ 
/*     */   public int last() {
/* 197 */     return this._array[(this._free - 1)];
/*     */   }
/*     */ 
/*     */   public void setLast(int n) {
/* 201 */     this._array[(this._free - 1)] = n;
/*     */   }
/*     */ 
/*     */   public void pop() {
/* 205 */     this._free -= 1;
/*     */   }
/*     */ 
/*     */   public void pop(int n) {
/* 209 */     this._free -= n;
/*     */   }
/*     */ 
/*     */   public final int cardinality() {
/* 213 */     return this._free;
/*     */   }
/*     */ 
/*     */   public void print(PrintStream out) {
/* 217 */     if (this._free > 0) {
/* 218 */       for (int i = 0; i < this._free - 1; i++) {
/* 219 */         out.print(this._array[i]);
/* 220 */         out.print(' ');
/*     */       }
/* 222 */       out.println(this._array[(this._free - 1)]);
/*     */     }
/*     */     else {
/* 225 */       out.println("IntegerArray: empty");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray
 * JD-Core Version:    0.6.2
 */