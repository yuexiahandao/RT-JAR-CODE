/*     */ package sun.font;
/*     */ 
/*     */ import java.text.Bidi;
/*     */ 
/*     */ public final class BidiUtils
/*     */ {
/*     */   static final char NUMLEVELS = '>';
/*     */ 
/*     */   public static void getLevels(Bidi paramBidi, byte[] paramArrayOfByte, int paramInt)
/*     */   {
/*  55 */     int i = paramInt + paramBidi.getLength();
/*     */ 
/*  57 */     if ((paramInt < 0) || (i > paramArrayOfByte.length)) {
/*  58 */       throw new IndexOutOfBoundsException("levels.length = " + paramArrayOfByte.length + " start: " + paramInt + " limit: " + i);
/*     */     }
/*     */ 
/*  62 */     int j = paramBidi.getRunCount();
/*  63 */     int k = paramInt;
/*  64 */     for (int m = 0; m < j; m++) {
/*  65 */       int n = paramInt + paramBidi.getRunLimit(m);
/*  66 */       int i1 = (byte)paramBidi.getRunLevel(m);
/*     */ 
/*  68 */       while (k < n)
/*  69 */         paramArrayOfByte[(k++)] = i1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static byte[] getLevels(Bidi paramBidi)
/*     */   {
/*  79 */     byte[] arrayOfByte = new byte[paramBidi.getLength()];
/*  80 */     getLevels(paramBidi, arrayOfByte, 0);
/*  81 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public static int[] createVisualToLogicalMap(byte[] paramArrayOfByte)
/*     */   {
/*  95 */     int i = paramArrayOfByte.length;
/*  96 */     int[] arrayOfInt = new int[i];
/*     */ 
/*  98 */     int j = 63;
/*  99 */     int k = 0;
/*     */     int n;
/* 103 */     for (int m = 0; m < i; m++) {
/* 104 */       arrayOfInt[m] = m;
/*     */ 
/* 106 */       n = paramArrayOfByte[m];
/* 107 */       if (n > k) {
/* 108 */         k = n;
/*     */       }
/*     */ 
/* 111 */       if (((n & 0x1) != 0) && (n < j)) {
/* 112 */         j = n;
/*     */       }
/*     */     }
/*     */ 
/* 116 */     while (k >= j) {
/* 117 */       m = 0;
/*     */       while (true) {
/* 119 */         if ((m < i) && (paramArrayOfByte[m] < k)) {
/* 120 */           m++;
/*     */         } else {
/* 122 */           n = m++;
/*     */ 
/* 124 */           if (n == paramArrayOfByte.length)
/*     */           {
/*     */             break;
/*     */           }
/* 128 */           while ((m < i) && (paramArrayOfByte[m] >= k)) {
/* 129 */             m++;
/*     */           }
/* 131 */           int i1 = m - 1;
/*     */ 
/* 133 */           while (n < i1) {
/* 134 */             int i2 = arrayOfInt[n];
/* 135 */             arrayOfInt[n] = arrayOfInt[i1];
/* 136 */             arrayOfInt[i1] = i2;
/* 137 */             n++;
/* 138 */             i1--;
/*     */           }
/*     */         }
/*     */       }
/* 142 */       k = (byte)(k - 1);
/*     */     }
/*     */ 
/* 145 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   public static int[] createInverseMap(int[] paramArrayOfInt)
/*     */   {
/* 156 */     if (paramArrayOfInt == null) {
/* 157 */       return null;
/*     */     }
/*     */ 
/* 160 */     int[] arrayOfInt = new int[paramArrayOfInt.length];
/* 161 */     for (int i = 0; i < paramArrayOfInt.length; i++) {
/* 162 */       arrayOfInt[paramArrayOfInt[i]] = i;
/*     */     }
/*     */ 
/* 165 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   public static int[] createContiguousOrder(int[] paramArrayOfInt)
/*     */   {
/* 179 */     if (paramArrayOfInt != null) {
/* 180 */       return computeContiguousOrder(paramArrayOfInt, 0, paramArrayOfInt.length);
/*     */     }
/*     */ 
/* 183 */     return null;
/*     */   }
/*     */ 
/*     */   private static int[] computeContiguousOrder(int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*     */   {
/* 192 */     int[] arrayOfInt = new int[paramInt2 - paramInt1];
/* 193 */     for (int i = 0; i < arrayOfInt.length; i++) {
/* 194 */       arrayOfInt[i] = (i + paramInt1);
/*     */     }
/*     */ 
/* 201 */     for (i = 0; i < arrayOfInt.length - 1; i++) {
/* 202 */       int j = i;
/* 203 */       int k = paramArrayOfInt[arrayOfInt[j]];
/* 204 */       for (int m = i; m < arrayOfInt.length; m++) {
/* 205 */         if (paramArrayOfInt[arrayOfInt[m]] < k) {
/* 206 */           j = m;
/* 207 */           k = paramArrayOfInt[arrayOfInt[j]];
/*     */         }
/*     */       }
/* 210 */       m = arrayOfInt[i];
/* 211 */       arrayOfInt[i] = arrayOfInt[j];
/* 212 */       arrayOfInt[j] = m;
/*     */     }
/*     */ 
/* 216 */     if (paramInt1 != 0) {
/* 217 */       for (i = 0; i < arrayOfInt.length; i++) {
/* 218 */         arrayOfInt[i] -= paramInt1;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 224 */     for (i = 0; (i < arrayOfInt.length) && 
/* 225 */       (arrayOfInt[i] == i); i++);
/* 230 */     if (i == arrayOfInt.length) {
/* 231 */       return null;
/*     */     }
/*     */ 
/* 235 */     return createInverseMap(arrayOfInt);
/*     */   }
/*     */ 
/*     */   public static int[] createNormalizedMap(int[] paramArrayOfInt, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 253 */     if (paramArrayOfInt != null) {
/* 254 */       if ((paramInt1 != 0) || (paramInt2 != paramArrayOfInt.length))
/*     */       {
/*     */         int k;
/*     */         int i;
/*     */         int j;
/* 259 */         if (paramArrayOfByte == null) {
/* 260 */           k = 0;
/* 261 */           i = 1;
/* 262 */           j = 1;
/*     */         }
/* 265 */         else if (paramArrayOfByte[paramInt1] == paramArrayOfByte[(paramInt2 - 1)]) {
/* 266 */           k = paramArrayOfByte[paramInt1];
/* 267 */           j = (k & 0x1) == 0 ? 1 : 0;
/*     */ 
/* 271 */           for (int m = paramInt1; (m < paramInt2) && 
/* 272 */             (paramArrayOfByte[m] >= k); m++)
/*     */           {
/* 275 */             if (j != 0) {
/* 276 */               j = paramArrayOfByte[m] == k ? 1 : 0;
/*     */             }
/*     */           }
/*     */ 
/* 280 */           i = m == paramInt2 ? 1 : 0;
/*     */         }
/*     */         else {
/* 283 */           i = 0;
/*     */ 
/* 286 */           k = 0;
/* 287 */           j = 0;
/*     */         }
/*     */ 
/* 291 */         if (i != 0) {
/* 292 */           if (j != 0) {
/* 293 */             return null;
/*     */           }
/*     */ 
/* 296 */           int[] arrayOfInt = new int[paramInt2 - paramInt1];
/*     */           int n;
/* 299 */           if ((k & 0x1) != 0)
/* 300 */             n = paramArrayOfInt[(paramInt2 - 1)];
/*     */           else {
/* 302 */             n = paramArrayOfInt[paramInt1];
/*     */           }
/*     */ 
/* 305 */           if (n == 0) {
/* 306 */             System.arraycopy(paramArrayOfInt, paramInt1, arrayOfInt, 0, paramInt2 - paramInt1);
/*     */           }
/*     */           else {
/* 309 */             for (int i1 = 0; i1 < arrayOfInt.length; i1++) {
/* 310 */               arrayOfInt[i1] = (paramArrayOfInt[(i1 + paramInt1)] - n);
/*     */             }
/*     */           }
/*     */ 
/* 314 */           return arrayOfInt;
/*     */         }
/*     */ 
/* 317 */         return computeContiguousOrder(paramArrayOfInt, paramInt1, paramInt2);
/*     */       }
/*     */ 
/* 321 */       return paramArrayOfInt;
/*     */     }
/*     */ 
/* 325 */     return null;
/*     */   }
/*     */ 
/*     */   public static void reorderVisually(byte[] paramArrayOfByte, Object[] paramArrayOfObject)
/*     */   {
/* 339 */     int i = paramArrayOfByte.length;
/*     */ 
/* 341 */     int j = 63;
/* 342 */     int k = 0;
/*     */     int n;
/* 346 */     for (int m = 0; m < i; m++) {
/* 347 */       n = paramArrayOfByte[m];
/* 348 */       if (n > k) {
/* 349 */         k = n;
/*     */       }
/*     */ 
/* 352 */       if (((n & 0x1) != 0) && (n < j)) {
/* 353 */         j = n;
/*     */       }
/*     */     }
/*     */ 
/* 357 */     while (k >= j) {
/* 358 */       m = 0;
/*     */       while (true) {
/* 360 */         if ((m < i) && (paramArrayOfByte[m] < k)) {
/* 361 */           m++;
/*     */         } else {
/* 363 */           n = m++;
/*     */ 
/* 365 */           if (n == paramArrayOfByte.length)
/*     */           {
/*     */             break;
/*     */           }
/* 369 */           while ((m < i) && (paramArrayOfByte[m] >= k)) {
/* 370 */             m++;
/*     */           }
/* 372 */           int i1 = m - 1;
/*     */ 
/* 374 */           while (n < i1) {
/* 375 */             Object localObject = paramArrayOfObject[n];
/* 376 */             paramArrayOfObject[n] = paramArrayOfObject[i1];
/* 377 */             paramArrayOfObject[i1] = localObject;
/* 378 */             n++;
/* 379 */             i1--;
/*     */           }
/*     */         }
/*     */       }
/* 383 */       k = (byte)(k - 1);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.BidiUtils
 * JD-Core Version:    0.6.2
 */