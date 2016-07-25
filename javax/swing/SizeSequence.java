/*     */ package javax.swing;
/*     */ 
/*     */ public class SizeSequence
/*     */ {
/* 126 */   private static int[] emptyArray = new int[0];
/*     */   private int[] a;
/*     */ 
/*     */   public SizeSequence()
/*     */   {
/* 138 */     this.a = emptyArray;
/*     */   }
/*     */ 
/*     */   public SizeSequence(int paramInt)
/*     */   {
/* 151 */     this(paramInt, 0);
/*     */   }
/*     */ 
/*     */   public SizeSequence(int paramInt1, int paramInt2)
/*     */   {
/* 163 */     this();
/* 164 */     insertEntries(0, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public SizeSequence(int[] paramArrayOfInt)
/*     */   {
/* 175 */     this();
/* 176 */     setSizes(paramArrayOfInt);
/*     */   }
/*     */ 
/*     */   void setSizes(int paramInt1, int paramInt2)
/*     */   {
/* 184 */     if (this.a.length != paramInt1) {
/* 185 */       this.a = new int[paramInt1];
/*     */     }
/* 187 */     setSizes(0, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   private int setSizes(int paramInt1, int paramInt2, int paramInt3) {
/* 191 */     if (paramInt2 <= paramInt1) {
/* 192 */       return 0;
/*     */     }
/* 194 */     int i = (paramInt1 + paramInt2) / 2;
/* 195 */     this.a[i] = (paramInt3 + setSizes(paramInt1, i, paramInt3));
/* 196 */     return this.a[i] + setSizes(i + 1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   public void setSizes(int[] paramArrayOfInt)
/*     */   {
/* 211 */     if (this.a.length != paramArrayOfInt.length) {
/* 212 */       this.a = new int[paramArrayOfInt.length];
/*     */     }
/* 214 */     setSizes(0, this.a.length, paramArrayOfInt);
/*     */   }
/*     */ 
/*     */   private int setSizes(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
/* 218 */     if (paramInt2 <= paramInt1) {
/* 219 */       return 0;
/*     */     }
/* 221 */     int i = (paramInt1 + paramInt2) / 2;
/* 222 */     this.a[i] = (paramArrayOfInt[i] + setSizes(paramInt1, i, paramArrayOfInt));
/* 223 */     return this.a[i] + setSizes(i + 1, paramInt2, paramArrayOfInt);
/*     */   }
/*     */ 
/*     */   public int[] getSizes()
/*     */   {
/* 232 */     int i = this.a.length;
/* 233 */     int[] arrayOfInt = new int[i];
/* 234 */     getSizes(0, i, arrayOfInt);
/* 235 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   private int getSizes(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
/* 239 */     if (paramInt2 <= paramInt1) {
/* 240 */       return 0;
/*     */     }
/* 242 */     int i = (paramInt1 + paramInt2) / 2;
/* 243 */     paramArrayOfInt[i] = (this.a[i] - getSizes(paramInt1, i, paramArrayOfInt));
/* 244 */     return this.a[i] + getSizes(i + 1, paramInt2, paramArrayOfInt);
/*     */   }
/*     */ 
/*     */   public int getPosition(int paramInt)
/*     */   {
/* 263 */     return getPosition(0, this.a.length, paramInt);
/*     */   }
/*     */ 
/*     */   private int getPosition(int paramInt1, int paramInt2, int paramInt3) {
/* 267 */     if (paramInt2 <= paramInt1) {
/* 268 */       return 0;
/*     */     }
/* 270 */     int i = (paramInt1 + paramInt2) / 2;
/* 271 */     if (paramInt3 <= i) {
/* 272 */       return getPosition(paramInt1, i, paramInt3);
/*     */     }
/*     */ 
/* 275 */     return this.a[i] + getPosition(i + 1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   public int getIndex(int paramInt)
/*     */   {
/* 289 */     return getIndex(0, this.a.length, paramInt);
/*     */   }
/*     */ 
/*     */   private int getIndex(int paramInt1, int paramInt2, int paramInt3) {
/* 293 */     if (paramInt2 <= paramInt1) {
/* 294 */       return paramInt1;
/*     */     }
/* 296 */     int i = (paramInt1 + paramInt2) / 2;
/* 297 */     int j = this.a[i];
/* 298 */     if (paramInt3 < j) {
/* 299 */       return getIndex(paramInt1, i, paramInt3);
/*     */     }
/*     */ 
/* 302 */     return getIndex(i + 1, paramInt2, paramInt3 - j);
/*     */   }
/*     */ 
/*     */   public int getSize(int paramInt)
/*     */   {
/* 316 */     return getPosition(paramInt + 1) - getPosition(paramInt);
/*     */   }
/*     */ 
/*     */   public void setSize(int paramInt1, int paramInt2)
/*     */   {
/* 330 */     changeSize(0, this.a.length, paramInt1, paramInt2 - getSize(paramInt1));
/*     */   }
/*     */ 
/*     */   private void changeSize(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 334 */     if (paramInt2 <= paramInt1) {
/* 335 */       return;
/*     */     }
/* 337 */     int i = (paramInt1 + paramInt2) / 2;
/* 338 */     if (paramInt3 <= i) {
/* 339 */       this.a[i] += paramInt4;
/* 340 */       changeSize(paramInt1, i, paramInt3, paramInt4);
/*     */     }
/*     */     else {
/* 343 */       changeSize(i + 1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void insertEntries(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 365 */     int[] arrayOfInt = getSizes();
/* 366 */     int i = paramInt1 + paramInt2;
/* 367 */     int j = this.a.length + paramInt2;
/* 368 */     this.a = new int[j];
/* 369 */     for (int k = 0; k < paramInt1; k++) {
/* 370 */       this.a[k] = arrayOfInt[k];
/*     */     }
/* 372 */     for (k = paramInt1; k < i; k++) {
/* 373 */       this.a[k] = paramInt3;
/*     */     }
/* 375 */     for (k = i; k < j; k++) {
/* 376 */       this.a[k] = arrayOfInt[(k - paramInt2)];
/*     */     }
/* 378 */     setSizes(this.a);
/*     */   }
/*     */ 
/*     */   public void removeEntries(int paramInt1, int paramInt2)
/*     */   {
/* 395 */     int[] arrayOfInt = getSizes();
/* 396 */     int i = paramInt1 + paramInt2;
/* 397 */     int j = this.a.length - paramInt2;
/* 398 */     this.a = new int[j];
/* 399 */     for (int k = 0; k < paramInt1; k++) {
/* 400 */       this.a[k] = arrayOfInt[k];
/*     */     }
/* 402 */     for (k = paramInt1; k < j; k++) {
/* 403 */       this.a[k] = arrayOfInt[(k + paramInt2)];
/*     */     }
/* 405 */     setSizes(this.a);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.SizeSequence
 * JD-Core Version:    0.6.2
 */