/*     */ package com.sun.java.util.jar.pack;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ class PopulationCoding
/*     */   implements CodingMethod
/*     */ {
/*     */   Histogram vHist;
/*     */   int[] fValues;
/*     */   int fVlen;
/*     */   long[] symtab;
/*     */   CodingMethod favoredCoding;
/*     */   CodingMethod tokenCoding;
/*     */   CodingMethod unfavoredCoding;
/*  53 */   int L = -1;
/*     */ 
/* 409 */   static final int[] LValuesCoded = { -1, 4, 8, 16, 32, 64, 128, 192, 224, 240, 248, 252 };
/*     */ 
/*     */   public void setFavoredValues(int[] paramArrayOfInt, int paramInt)
/*     */   {
/*  58 */     assert (paramArrayOfInt[0] == 0);
/*  59 */     assert (this.fValues == null);
/*  60 */     this.fValues = paramArrayOfInt;
/*  61 */     this.fVlen = paramInt;
/*  62 */     if (this.L >= 0)
/*  63 */       setL(this.L);
/*     */   }
/*     */ 
/*     */   public void setFavoredValues(int[] paramArrayOfInt) {
/*  67 */     int i = paramArrayOfInt.length - 1;
/*  68 */     setFavoredValues(paramArrayOfInt, i);
/*     */   }
/*     */   public void setHistogram(Histogram paramHistogram) {
/*  71 */     this.vHist = paramHistogram;
/*     */   }
/*     */   public void setL(int paramInt) {
/*  74 */     this.L = paramInt;
/*  75 */     if ((paramInt >= 0) && (this.fValues != null) && (this.tokenCoding == null)) {
/*  76 */       this.tokenCoding = fitTokenCoding(this.fVlen, paramInt);
/*  77 */       assert (this.tokenCoding != null);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Coding fitTokenCoding(int paramInt1, int paramInt2)
/*     */   {
/*  83 */     if (paramInt1 < 256)
/*     */     {
/*  85 */       return BandStructure.BYTE1;
/*  86 */     }Coding localCoding1 = BandStructure.UNSIGNED5.setL(paramInt2);
/*  87 */     if (!localCoding1.canRepresentUnsigned(paramInt1))
/*  88 */       return null;
/*  89 */     Object localObject = localCoding1;
/*  90 */     Coding localCoding2 = localCoding1;
/*     */     while (true) { localCoding2 = localCoding2.setB(localCoding2.B() - 1);
/*  92 */       if (localCoding2.umax() < paramInt1)
/*     */         break;
/*  94 */       localObject = localCoding2;
/*     */     }
/*  96 */     return localObject;
/*     */   }
/*     */ 
/*     */   public void setFavoredCoding(CodingMethod paramCodingMethod) {
/* 100 */     this.favoredCoding = paramCodingMethod;
/*     */   }
/*     */   public void setTokenCoding(CodingMethod paramCodingMethod) {
/* 103 */     this.tokenCoding = paramCodingMethod;
/* 104 */     this.L = -1;
/* 105 */     if (((paramCodingMethod instanceof Coding)) && (this.fValues != null)) {
/* 106 */       Coding localCoding = (Coding)paramCodingMethod;
/* 107 */       if (localCoding == fitTokenCoding(this.fVlen, localCoding.L()))
/* 108 */         this.L = localCoding.L();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setUnfavoredCoding(CodingMethod paramCodingMethod) {
/* 113 */     this.unfavoredCoding = paramCodingMethod;
/*     */   }
/*     */ 
/*     */   public int favoredValueMaxLength() {
/* 117 */     if (this.L == 0) {
/* 118 */       return 2147483647;
/*     */     }
/* 120 */     return BandStructure.UNSIGNED5.setL(this.L).umax();
/*     */   }
/*     */ 
/*     */   public void resortFavoredValues() {
/* 124 */     Coding localCoding = (Coding)this.tokenCoding;
/*     */ 
/* 126 */     this.fValues = BandStructure.realloc(this.fValues, 1 + this.fVlen);
/*     */ 
/* 128 */     int i = 1;
/* 129 */     for (int j = 1; j <= localCoding.B(); j++) {
/* 130 */       int k = localCoding.byteMax(j);
/* 131 */       if (k > this.fVlen)
/* 132 */         k = this.fVlen;
/* 133 */       if (k < localCoding.byteMin(j))
/*     */         break;
/* 135 */       int m = i;
/* 136 */       int n = k + 1;
/* 137 */       if (n != m)
/*     */       {
/* 139 */         assert (n > m) : (n + "!>" + m);
/*     */ 
/* 141 */         assert (localCoding.getLength(m) == j) : (j + " != len(" + m + ") == " + localCoding.getLength(m));
/*     */ 
/* 144 */         assert (localCoding.getLength(n - 1) == j) : (j + " != len(" + (n - 1) + ") == " + localCoding.getLength(n - 1));
/*     */ 
/* 146 */         int i1 = m + (n - m) / 2;
/* 147 */         int i2 = m;
/*     */ 
/* 149 */         int i3 = -1;
/* 150 */         int i4 = m;
/* 151 */         for (int i5 = m; i5 < n; i5++) {
/* 152 */           int i6 = this.fValues[i5];
/* 153 */           int i7 = this.vHist.getFrequency(i6);
/* 154 */           if (i3 != i7) {
/* 155 */             if (j == 1)
/*     */             {
/* 158 */               Arrays.sort(this.fValues, i4, i5);
/* 159 */             } else if (Math.abs(i2 - i1) > Math.abs(i5 - i1))
/*     */             {
/* 163 */               i2 = i5;
/*     */             }
/* 165 */             i3 = i7;
/* 166 */             i4 = i5;
/*     */           }
/*     */         }
/* 169 */         if (j == 1) {
/* 170 */           Arrays.sort(this.fValues, i4, n);
/*     */         }
/*     */         else {
/* 173 */           Arrays.sort(this.fValues, m, i2);
/* 174 */           Arrays.sort(this.fValues, i2, n);
/*     */         }
/* 176 */         assert (localCoding.getLength(m) == localCoding.getLength(i2));
/* 177 */         assert (localCoding.getLength(m) == localCoding.getLength(n - 1));
/* 178 */         i = k + 1;
/*     */       }
/*     */     }
/* 180 */     assert (i == this.fValues.length);
/*     */ 
/* 183 */     this.symtab = null;
/*     */   }
/*     */ 
/*     */   public int getToken(int paramInt) {
/* 187 */     if (this.symtab == null)
/* 188 */       this.symtab = makeSymtab();
/* 189 */     int i = Arrays.binarySearch(this.symtab, paramInt << 32);
/* 190 */     if (i < 0) i = -i - 1;
/* 191 */     if ((i < this.symtab.length) && (paramInt == (int)(this.symtab[i] >>> 32))) {
/* 192 */       return (int)this.symtab[i];
/*     */     }
/* 194 */     return 0;
/*     */   }
/*     */ 
/*     */   public int[][] encodeValues(int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*     */   {
/* 199 */     int[] arrayOfInt1 = new int[paramInt2 - paramInt1];
/* 200 */     int i = 0;
/*     */     int m;
/* 201 */     for (int j = 0; j < arrayOfInt1.length; j++) {
/* 202 */       k = paramArrayOfInt[(paramInt1 + j)];
/* 203 */       m = getToken(k);
/* 204 */       if (m != 0)
/* 205 */         arrayOfInt1[j] = m;
/*     */       else {
/* 207 */         i++;
/*     */       }
/*     */     }
/* 210 */     int[] arrayOfInt2 = new int[i];
/* 211 */     i = 0;
/* 212 */     for (int k = 0; k < arrayOfInt1.length; k++)
/* 213 */       if (arrayOfInt1[k] == 0) {
/* 214 */         m = paramArrayOfInt[(paramInt1 + k)];
/* 215 */         arrayOfInt2[(i++)] = m;
/*     */       }
/* 217 */     assert (i == arrayOfInt2.length);
/* 218 */     return new int[][] { arrayOfInt1, arrayOfInt2 };
/*     */   }
/*     */ 
/*     */   private long[] makeSymtab() {
/* 222 */     long[] arrayOfLong = new long[this.fVlen];
/* 223 */     for (int i = 1; i <= this.fVlen; i++) {
/* 224 */       arrayOfLong[(i - 1)] = (this.fValues[i] << 32 | i);
/*     */     }
/*     */ 
/* 227 */     Arrays.sort(arrayOfLong);
/* 228 */     return arrayOfLong;
/*     */   }
/*     */ 
/*     */   private Coding getTailCoding(CodingMethod paramCodingMethod) {
/* 232 */     while ((paramCodingMethod instanceof AdaptiveCoding))
/* 233 */       paramCodingMethod = ((AdaptiveCoding)paramCodingMethod).tailCoding;
/* 234 */     return (Coding)paramCodingMethod;
/*     */   }
/*     */ 
/*     */   public void writeArrayTo(OutputStream paramOutputStream, int[] paramArrayOfInt, int paramInt1, int paramInt2) throws IOException
/*     */   {
/* 239 */     int[][] arrayOfInt = encodeValues(paramArrayOfInt, paramInt1, paramInt2);
/* 240 */     writeSequencesTo(paramOutputStream, arrayOfInt[0], arrayOfInt[1]);
/*     */   }
/*     */   void writeSequencesTo(OutputStream paramOutputStream, int[] paramArrayOfInt1, int[] paramArrayOfInt2) throws IOException {
/* 243 */     this.favoredCoding.writeArrayTo(paramOutputStream, this.fValues, 1, 1 + this.fVlen);
/* 244 */     getTailCoding(this.favoredCoding).writeTo(paramOutputStream, computeSentinelValue());
/* 245 */     this.tokenCoding.writeArrayTo(paramOutputStream, paramArrayOfInt1, 0, paramArrayOfInt1.length);
/* 246 */     if (paramArrayOfInt2.length > 0)
/* 247 */       this.unfavoredCoding.writeArrayTo(paramOutputStream, paramArrayOfInt2, 0, paramArrayOfInt2.length);
/*     */   }
/*     */ 
/*     */   int computeSentinelValue() {
/* 251 */     Coding localCoding = getTailCoding(this.favoredCoding);
/* 252 */     if (localCoding.isDelta())
/*     */     {
/* 254 */       return 0;
/*     */     }
/*     */ 
/* 257 */     int i = this.fValues[1];
/* 258 */     int j = i;
/*     */ 
/* 260 */     for (int k = 2; k <= this.fVlen; k++) {
/* 261 */       j = this.fValues[k];
/* 262 */       i = moreCentral(i, j);
/*     */     }
/*     */ 
/* 265 */     if (localCoding.getLength(i) <= localCoding.getLength(j)) {
/* 266 */       return i;
/*     */     }
/* 268 */     return j;
/*     */   }
/*     */ 
/*     */   public void readArrayFrom(InputStream paramInputStream, int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 274 */     setFavoredValues(readFavoredValuesFrom(paramInputStream, paramInt2 - paramInt1));
/*     */ 
/* 276 */     this.tokenCoding.readArrayFrom(paramInputStream, paramArrayOfInt, paramInt1, paramInt2);
/*     */ 
/* 278 */     int i = 0; int j = -1;
/* 279 */     int k = 0;
/* 280 */     for (int m = paramInt1; m < paramInt2; m++) {
/* 281 */       n = paramArrayOfInt[m];
/* 282 */       if (n == 0)
/*     */       {
/* 284 */         if (j < 0)
/* 285 */           i = m;
/*     */         else {
/* 287 */           paramArrayOfInt[j] = m;
/*     */         }
/* 289 */         j = m;
/* 290 */         k++;
/*     */       } else {
/* 292 */         paramArrayOfInt[m] = this.fValues[n];
/*     */       }
/*     */     }
/*     */ 
/* 296 */     int[] arrayOfInt = new int[k];
/* 297 */     if (k > 0)
/* 298 */       this.unfavoredCoding.readArrayFrom(paramInputStream, arrayOfInt, 0, k);
/* 299 */     for (int n = 0; n < k; n++) {
/* 300 */       int i1 = paramArrayOfInt[i];
/* 301 */       paramArrayOfInt[i] = arrayOfInt[n];
/* 302 */       i = i1;
/*     */     }
/*     */   }
/*     */ 
/*     */   int[] readFavoredValuesFrom(InputStream paramInputStream, int paramInt) throws IOException {
/* 307 */     int[] arrayOfInt = new int[1000];
/*     */ 
/* 311 */     HashSet localHashSet = null;
/* 312 */     assert ((localHashSet = new HashSet()) != null);
/* 313 */     int i = 1;
/* 314 */     paramInt += i;
/* 315 */     int j = -2147483648;
/*     */ 
/* 317 */     int k = 0;
/* 318 */     CodingMethod localCodingMethod = this.favoredCoding;
/*     */     int i2;
/* 319 */     while ((localCodingMethod instanceof AdaptiveCoding)) {
/* 320 */       localObject = (AdaptiveCoding)localCodingMethod;
/* 321 */       int m = ((AdaptiveCoding)localObject).headLength;
/* 322 */       while (i + m > arrayOfInt.length) {
/* 323 */         arrayOfInt = BandStructure.realloc(arrayOfInt);
/*     */       }
/* 325 */       int i1 = i + m;
/* 326 */       ((AdaptiveCoding)localObject).headCoding.readArrayFrom(paramInputStream, arrayOfInt, i, i1);
/* 327 */       while (i < i1) {
/* 328 */         i2 = arrayOfInt[(i++)];
/* 329 */         assert (localHashSet.add(Integer.valueOf(i2)));
/* 330 */         assert (i <= paramInt);
/* 331 */         k = i2;
/* 332 */         j = moreCentral(j, i2);
/*     */       }
/*     */ 
/* 335 */       localCodingMethod = ((AdaptiveCoding)localObject).tailCoding;
/*     */     }
/* 337 */     Object localObject = (Coding)localCodingMethod;
/* 338 */     if (((Coding)localObject).isDelta()) {
/* 339 */       long l = 0L;
/*     */       while (true) {
/* 341 */         l += ((Coding)localObject).readFrom(paramInputStream);
/*     */ 
/* 343 */         if (((Coding)localObject).isSubrange())
/* 344 */           i2 = ((Coding)localObject).reduceToUnsignedRange(l);
/*     */         else
/* 346 */           i2 = (int)l;
/* 347 */         l = i2;
/* 348 */         if ((i > 1) && ((i2 == k) || (i2 == j)))
/*     */           break;
/* 350 */         if (i == arrayOfInt.length)
/* 351 */           arrayOfInt = BandStructure.realloc(arrayOfInt);
/* 352 */         arrayOfInt[(i++)] = i2;
/* 353 */         assert (localHashSet.add(Integer.valueOf(i2)));
/* 354 */         assert (i <= paramInt);
/* 355 */         k = i2;
/* 356 */         j = moreCentral(j, i2);
/*     */       }
/*     */     }
/*     */     else {
/*     */       while (true) {
/* 361 */         int n = ((Coding)localObject).readFrom(paramInputStream);
/* 362 */         if ((i > 1) && ((n == k) || (n == j)))
/*     */           break;
/* 364 */         if (i == arrayOfInt.length)
/* 365 */           arrayOfInt = BandStructure.realloc(arrayOfInt);
/* 366 */         arrayOfInt[(i++)] = n;
/* 367 */         assert (localHashSet.add(Integer.valueOf(n)));
/* 368 */         assert (i <= paramInt);
/* 369 */         k = n;
/* 370 */         j = moreCentral(j, n);
/*     */       }
/*     */     }
/*     */ 
/* 374 */     return BandStructure.realloc(arrayOfInt, i);
/*     */   }
/*     */ 
/*     */   private static int moreCentral(int paramInt1, int paramInt2) {
/* 378 */     int i = paramInt1 >> 31 ^ paramInt1 << 1;
/* 379 */     int j = paramInt2 >> 31 ^ paramInt2 << 1;
/*     */ 
/* 381 */     i -= -2147483648;
/* 382 */     j -= -2147483648;
/* 383 */     int k = i < j ? paramInt1 : paramInt2;
/*     */ 
/* 385 */     assert (k == moreCentralSlow(paramInt1, paramInt2));
/* 386 */     return k;
/*     */   }
/*     */ 
/*     */   private static int moreCentralSlow(int paramInt1, int paramInt2)
/*     */   {
/* 397 */     int i = paramInt1;
/* 398 */     if (i < 0) i = -i;
/* 399 */     if (i < 0) return paramInt2;
/* 400 */     int j = paramInt2;
/* 401 */     if (j < 0) j = -j;
/* 402 */     if (j < 0) return paramInt1;
/* 403 */     if (i < j) return paramInt1;
/* 404 */     if (i > j) return paramInt2;
/*     */ 
/* 406 */     return paramInt1 < paramInt2 ? paramInt1 : paramInt2;
/*     */   }
/*     */ 
/*     */   public byte[] getMetaCoding(Coding paramCoding)
/*     */   {
/* 413 */     int i = this.fVlen;
/* 414 */     int j = 0;
/* 415 */     if ((this.tokenCoding instanceof Coding)) {
/* 416 */       localObject = (Coding)this.tokenCoding;
/* 417 */       if (((Coding)localObject).B() == 1) {
/* 418 */         j = 1;
/* 419 */       } else if (this.L >= 0) {
/* 420 */         assert (this.L == ((Coding)localObject).L());
/* 421 */         for (k = 1; k < LValuesCoded.length; k++)
/* 422 */           if (LValuesCoded[k] == this.L) { j = k; break;
/*     */           }
/*     */       }
/*     */     }
/* 426 */     Object localObject = null;
/* 427 */     if ((j != 0) && (this.tokenCoding == fitTokenCoding(this.fVlen, this.L)))
/*     */     {
/* 429 */       localObject = this.tokenCoding;
/*     */     }
/* 431 */     int k = this.favoredCoding == paramCoding ? 1 : 0;
/* 432 */     int m = (this.unfavoredCoding == paramCoding) || (this.unfavoredCoding == null) ? 1 : 0;
/* 433 */     int n = this.tokenCoding == localObject ? 1 : 0;
/* 434 */     int i1 = n == 1 ? j : 0;
/* 435 */     if (!$assertionsDisabled) if (n != (i1 > 0 ? 1 : 0)) throw new AssertionError();
/* 436 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(10);
/* 437 */     localByteArrayOutputStream.write(141 + k + 2 * m + 4 * i1);
/*     */     try {
/* 439 */       if (k == 0) localByteArrayOutputStream.write(this.favoredCoding.getMetaCoding(paramCoding));
/* 440 */       if (n == 0) localByteArrayOutputStream.write(this.tokenCoding.getMetaCoding(paramCoding));
/* 441 */       if (m == 0) localByteArrayOutputStream.write(this.unfavoredCoding.getMetaCoding(paramCoding)); 
/*     */     }
/* 443 */     catch (IOException localIOException) { throw new RuntimeException(localIOException); }
/*     */ 
/* 445 */     return localByteArrayOutputStream.toByteArray();
/*     */   }
/*     */   public static int parseMetaCoding(byte[] paramArrayOfByte, int paramInt, Coding paramCoding, CodingMethod[] paramArrayOfCodingMethod) {
/* 448 */     int i = paramArrayOfByte[(paramInt++)] & 0xFF;
/* 449 */     if ((i < 141) || (i >= 189)) return paramInt - 1;
/* 450 */     i -= 141;
/* 451 */     int j = i % 2;
/* 452 */     int k = i / 2 % 2;
/* 453 */     int m = i / 4;
/* 454 */     int n = m > 0 ? 1 : 0;
/* 455 */     int i1 = LValuesCoded[m];
/* 456 */     CodingMethod[] arrayOfCodingMethod1 = { paramCoding }; CodingMethod[] arrayOfCodingMethod2 = { null }; CodingMethod[] arrayOfCodingMethod3 = { paramCoding };
/* 457 */     if (j == 0)
/* 458 */       paramInt = BandStructure.parseMetaCoding(paramArrayOfByte, paramInt, paramCoding, arrayOfCodingMethod1);
/* 459 */     if (n == 0)
/* 460 */       paramInt = BandStructure.parseMetaCoding(paramArrayOfByte, paramInt, paramCoding, arrayOfCodingMethod2);
/* 461 */     if (k == 0)
/* 462 */       paramInt = BandStructure.parseMetaCoding(paramArrayOfByte, paramInt, paramCoding, arrayOfCodingMethod3);
/* 463 */     PopulationCoding localPopulationCoding = new PopulationCoding();
/* 464 */     localPopulationCoding.L = i1;
/* 465 */     localPopulationCoding.favoredCoding = arrayOfCodingMethod1[0];
/* 466 */     localPopulationCoding.tokenCoding = arrayOfCodingMethod2[0];
/* 467 */     localPopulationCoding.unfavoredCoding = arrayOfCodingMethod3[0];
/* 468 */     paramArrayOfCodingMethod[0] = localPopulationCoding;
/* 469 */     return paramInt;
/*     */   }
/*     */ 
/*     */   private String keyString(CodingMethod paramCodingMethod) {
/* 473 */     if ((paramCodingMethod instanceof Coding))
/* 474 */       return ((Coding)paramCodingMethod).keyString();
/* 475 */     if (paramCodingMethod == null)
/* 476 */       return "none";
/* 477 */     return paramCodingMethod.toString();
/*     */   }
/*     */   public String toString() {
/* 480 */     PropMap localPropMap = Utils.currentPropMap();
/* 481 */     int i = (localPropMap != null) && (localPropMap.getBoolean("com.sun.java.util.jar.pack.verbose.pop")) ? 1 : 0;
/*     */ 
/* 484 */     StringBuilder localStringBuilder = new StringBuilder(100);
/* 485 */     localStringBuilder.append("pop(").append("fVlen=").append(this.fVlen);
/* 486 */     if ((i != 0) && (this.fValues != null)) {
/* 487 */       localStringBuilder.append(" fV=[");
/* 488 */       for (int j = 1; j <= this.fVlen; j++) {
/* 489 */         localStringBuilder.append(j == 1 ? "" : ",").append(this.fValues[j]);
/*     */       }
/* 491 */       localStringBuilder.append(";").append(computeSentinelValue());
/* 492 */       localStringBuilder.append("]");
/*     */     }
/* 494 */     localStringBuilder.append(" fc=").append(keyString(this.favoredCoding));
/* 495 */     localStringBuilder.append(" tc=").append(keyString(this.tokenCoding));
/* 496 */     localStringBuilder.append(" uc=").append(keyString(this.unfavoredCoding));
/* 497 */     localStringBuilder.append(")");
/* 498 */     return localStringBuilder.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.util.jar.pack.PopulationCoding
 * JD-Core Version:    0.6.2
 */