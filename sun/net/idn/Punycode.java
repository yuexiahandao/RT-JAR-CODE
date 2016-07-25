/*     */ package sun.net.idn;
/*     */ 
/*     */ import java.text.ParseException;
/*     */ import sun.text.normalizer.UCharacter;
/*     */ import sun.text.normalizer.UTF16;
/*     */ 
/*     */ public final class Punycode
/*     */ {
/*     */   private static final int BASE = 36;
/*     */   private static final int TMIN = 1;
/*     */   private static final int TMAX = 26;
/*     */   private static final int SKEW = 38;
/*     */   private static final int DAMP = 700;
/*     */   private static final int INITIAL_BIAS = 72;
/*     */   private static final int INITIAL_N = 128;
/*     */   private static final int HYPHEN = 45;
/*     */   private static final int DELIMITER = 45;
/*     */   private static final int ZERO = 48;
/*     */   private static final int NINE = 57;
/*     */   private static final int SMALL_A = 97;
/*     */   private static final int SMALL_Z = 122;
/*     */   private static final int CAPITAL_A = 65;
/*     */   private static final int CAPITAL_Z = 90;
/*     */   private static final int MAX_CP_COUNT = 256;
/*     */   private static final int UINT_MAGIC = -2147483648;
/*     */   private static final long ULONG_MAGIC = -9223372036854775808L;
/* 102 */   static final int[] basicToDigit = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
/*     */ 
/*     */   private static int adaptBias(int paramInt1, int paramInt2, boolean paramBoolean)
/*     */   {
/*  82 */     if (paramBoolean)
/*  83 */       paramInt1 /= 700;
/*     */     else {
/*  85 */       paramInt1 /= 2;
/*     */     }
/*  87 */     paramInt1 += paramInt1 / paramInt2;
/*     */ 
/*  89 */     for (int i = 0; 
/*  90 */       paramInt1 > 455; i += 36) {
/*  91 */       paramInt1 /= 35;
/*     */     }
/*     */ 
/*  94 */     return i + 36 * paramInt1 / (paramInt1 + 38);
/*     */   }
/*     */ 
/*     */   private static char asciiCaseMap(char paramChar, boolean paramBoolean)
/*     */   {
/* 129 */     if (paramBoolean) {
/* 130 */       if (('a' <= paramChar) && (paramChar <= 'z')) {
/* 131 */         paramChar = (char)(paramChar - ' ');
/*     */       }
/*     */     }
/* 134 */     else if (('A' <= paramChar) && (paramChar <= 'Z')) {
/* 135 */       paramChar = (char)(paramChar + ' ');
/*     */     }
/*     */ 
/* 138 */     return paramChar;
/*     */   }
/*     */ 
/*     */   private static char digitToBasic(int paramInt, boolean paramBoolean)
/*     */   {
/* 150 */     if (paramInt < 26) {
/* 151 */       if (paramBoolean) {
/* 152 */         return (char)(65 + paramInt);
/*     */       }
/* 154 */       return (char)(97 + paramInt);
/*     */     }
/*     */ 
/* 157 */     return (char)(22 + paramInt);
/*     */   }
/*     */ 
/*     */   public static StringBuffer encode(StringBuffer paramStringBuffer, boolean[] paramArrayOfBoolean)
/*     */     throws ParseException
/*     */   {
/* 172 */     int[] arrayOfInt = new int[256];
/*     */ 
/* 175 */     int i10 = paramStringBuffer.length();
/* 176 */     int i11 = 256;
/* 177 */     char[] arrayOfChar = new char[i11];
/* 178 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */     int i1;
/* 183 */     int i8 = i1 = 0;
/*     */ 
/* 185 */     for (int i3 = 0; i3 < i10; i3++) {
/* 186 */       if (i8 == 256)
/*     */       {
/* 188 */         throw new IndexOutOfBoundsException();
/*     */       }
/* 190 */       int i9 = paramStringBuffer.charAt(i3);
/* 191 */       if (isBasic(i9)) {
/* 192 */         if (i1 < i11) {
/* 193 */           arrayOfInt[(i8++)] = 0;
/* 194 */           arrayOfChar[i1] = (paramArrayOfBoolean != null ? asciiCaseMap(i9, paramArrayOfBoolean[i3]) : i9);
/*     */         }
/*     */ 
/* 199 */         i1++;
/*     */       } else {
/* 201 */         int i = ((paramArrayOfBoolean != null) && (paramArrayOfBoolean[i3] != 0) ? 1 : 0) << 31;
/* 202 */         if (!UTF16.isSurrogate(i9)) {
/* 203 */           i |= i9;
/*     */         }
/*     */         else
/*     */         {
/*     */           char c;
/* 204 */           if ((UTF16.isLeadSurrogate(i9)) && (i3 + 1 < i10) && (UTF16.isTrailSurrogate(c = paramStringBuffer.charAt(i3 + 1)))) {
/* 205 */             i3++;
/*     */ 
/* 207 */             i |= UCharacter.getCodePoint(i9, c);
/*     */           }
/*     */           else {
/* 210 */             throw new ParseException("Illegal char found", -1);
/*     */           }
/*     */         }
/* 212 */         arrayOfInt[(i8++)] = j;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 217 */     int n = i1;
/* 218 */     if (n > 0) {
/* 219 */       if (i1 < i11) {
/* 220 */         arrayOfChar[i1] = '-';
/*     */       }
/* 222 */       i1++;
/*     */     }
/*     */ 
/* 232 */     int j = 128;
/* 233 */     int k = 0;
/* 234 */     int i2 = 72;
/*     */ 
/* 237 */     for (int m = n; m < i8; )
/*     */     {
/* 242 */       int i4 = 2147483647;
/*     */       int i5;
/* 242 */       for (i3 = 0; i3 < i8; i3++) {
/* 243 */         i5 = arrayOfInt[i3] & 0x7FFFFFFF;
/* 244 */         if ((j <= i5) && (i5 < i4)) {
/* 245 */           i4 = i5;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 253 */       if (i4 - j > (2147483391 - k) / (m + 1)) {
/* 254 */         throw new RuntimeException("Internal program error");
/*     */       }
/* 256 */       k += (i4 - j) * (m + 1);
/* 257 */       j = i4;
/*     */ 
/* 260 */       for (i3 = 0; i3 < i8; i3++) {
/* 261 */         i5 = arrayOfInt[i3] & 0x7FFFFFFF;
/* 262 */         if (i5 < j) {
/* 263 */           k++;
/* 264 */         } else if (i5 == j)
/*     */         {
/* 266 */           i5 = k; for (int i6 = 36; ; i6 += 36)
/*     */           {
/* 278 */             int i7 = i6 - i2;
/* 279 */             if (i7 < 1)
/* 280 */               i7 = 1;
/* 281 */             else if (i6 >= i2 + 26) {
/* 282 */               i7 = 26;
/*     */             }
/*     */ 
/* 285 */             if (i5 < i7)
/*     */             {
/*     */               break;
/*     */             }
/* 289 */             if (i1 < i11) {
/* 290 */               arrayOfChar[(i1++)] = digitToBasic(i7 + (i5 - i7) % (36 - i7), false);
/*     */             }
/* 292 */             i5 = (i5 - i7) / (36 - i7);
/*     */           }
/*     */ 
/* 295 */           if (i1 < i11) {
/* 296 */             arrayOfChar[(i1++)] = digitToBasic(i5, arrayOfInt[i3] < 0 ? 1 : false);
/*     */           }
/* 298 */           i2 = adaptBias(k, m + 1, m == n);
/* 299 */           k = 0;
/* 300 */           m++;
/*     */         }
/*     */       }
/*     */ 
/* 304 */       k++;
/* 305 */       j++;
/*     */     }
/*     */ 
/* 308 */     return localStringBuffer.append(arrayOfChar, 0, i1);
/*     */   }
/*     */ 
/*     */   private static boolean isBasic(int paramInt) {
/* 312 */     return paramInt < 128;
/*     */   }
/*     */ 
/*     */   private static boolean isBasicUpperCase(int paramInt) {
/* 316 */     return (65 <= paramInt) && (paramInt <= 90);
/*     */   }
/*     */ 
/*     */   private static boolean isSurrogate(int paramInt) {
/* 320 */     return (paramInt & 0xFFFFF800) == 55296;
/*     */   }
/*     */ 
/*     */   public static StringBuffer decode(StringBuffer paramStringBuffer, boolean[] paramArrayOfBoolean)
/*     */     throws ParseException
/*     */   {
/* 333 */     int i = paramStringBuffer.length();
/* 334 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 338 */     int i13 = 256;
/* 339 */     char[] arrayOfChar = new char[i13];
/*     */ 
/* 349 */     for (int i2 = i; i2 > 0; )
/* 350 */       if (paramStringBuffer.charAt(--i2) == '-')
/* 351 */         break;
/*     */     int i9;
/*     */     int i1;
/* 354 */     int k = i1 = i9 = i2;
/*     */ 
/* 356 */     while (i2 > 0) {
/* 357 */       int i12 = paramStringBuffer.charAt(--i2);
/* 358 */       if (!isBasic(i12)) {
/* 359 */         throw new ParseException("Illegal char found", -1);
/*     */       }
/*     */ 
/* 362 */       if (i2 < i13) {
/* 363 */         arrayOfChar[i2] = i12;
/*     */ 
/* 365 */         if (paramArrayOfBoolean != null) {
/* 366 */           paramArrayOfBoolean[i2] = isBasicUpperCase(i12);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 372 */     int j = 128;
/* 373 */     int m = 0;
/* 374 */     int n = 72;
/* 375 */     int i10 = 1000000000;
/*     */ 
/* 382 */     for (int i3 = i1 > 0 ? i1 + 1 : 0; i3 < i; )
/*     */     {
/* 392 */       int i4 = m; int i5 = 1; for (int i6 = 36; ; i6 += 36) {
/* 393 */         if (i3 >= i) {
/* 394 */           throw new ParseException("Illegal char found", -1);
/*     */         }
/*     */ 
/* 397 */         int i7 = basicToDigit[((byte)paramStringBuffer.charAt(i3++))];
/* 398 */         if (i7 < 0) {
/* 399 */           throw new ParseException("Invalid char found", -1);
/*     */         }
/* 401 */         if (i7 > (2147483647 - m) / i5)
/*     */         {
/* 403 */           throw new ParseException("Illegal char found", -1);
/*     */         }
/*     */ 
/* 406 */         m += i7 * i5;
/* 407 */         int i8 = i6 - n;
/* 408 */         if (i8 < 1)
/* 409 */           i8 = 1;
/* 410 */         else if (i6 >= n + 26) {
/* 411 */           i8 = 26;
/*     */         }
/* 413 */         if (i7 < i8)
/*     */         {
/*     */           break;
/*     */         }
/* 417 */         if (i5 > 2147483647 / (36 - i8))
/*     */         {
/* 419 */           throw new ParseException("Illegal char found", -1);
/*     */         }
/* 421 */         i5 *= (36 - i8);
/*     */       }
/*     */ 
/* 429 */       i9++;
/* 430 */       n = adaptBias(m - i4, i9, i4 == 0);
/*     */ 
/* 436 */       if (m / i9 > 2147483647 - j)
/*     */       {
/* 438 */         throw new ParseException("Illegal char found", -1);
/*     */       }
/*     */ 
/* 441 */       j += m / i9;
/* 442 */       m %= i9;
/*     */ 
/* 446 */       if ((j > 1114111) || (isSurrogate(j)))
/*     */       {
/* 448 */         throw new ParseException("Illegal char found", -1);
/*     */       }
/*     */ 
/* 452 */       int i11 = UTF16.getCharCount(j);
/* 453 */       if (k + i11 < i13)
/*     */       {
/*     */         int i14;
/* 466 */         if (m <= i10) {
/* 467 */           i14 = m;
/* 468 */           if (i11 > 1)
/* 469 */             i10 = i14;
/*     */           else
/* 471 */             i10++;
/*     */         }
/*     */         else {
/* 474 */           i14 = i10;
/* 475 */           i14 = UTF16.moveCodePointOffset(arrayOfChar, 0, k, i14, m - i14);
/*     */         }
/*     */ 
/* 479 */         if (i14 < k) {
/* 480 */           System.arraycopy(arrayOfChar, i14, arrayOfChar, i14 + i11, k - i14);
/*     */ 
/* 483 */           if (paramArrayOfBoolean != null) {
/* 484 */             System.arraycopy(paramArrayOfBoolean, i14, paramArrayOfBoolean, i14 + i11, k - i14);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 489 */         if (i11 == 1)
/*     */         {
/* 491 */           arrayOfChar[i14] = ((char)j);
/*     */         }
/*     */         else {
/* 494 */           arrayOfChar[i14] = UTF16.getLeadSurrogate(j);
/* 495 */           arrayOfChar[(i14 + 1)] = UTF16.getTrailSurrogate(j);
/*     */         }
/* 497 */         if (paramArrayOfBoolean != null)
/*     */         {
/* 499 */           paramArrayOfBoolean[i14] = isBasicUpperCase(paramStringBuffer.charAt(i3 - 1));
/* 500 */           if (i11 == 2) {
/* 501 */             paramArrayOfBoolean[(i14 + 1)] = false;
/*     */           }
/*     */         }
/*     */       }
/* 505 */       k += i11;
/* 506 */       m++;
/*     */     }
/* 508 */     localStringBuffer.append(arrayOfChar, 0, k);
/* 509 */     return localStringBuffer;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.idn.Punycode
 * JD-Core Version:    0.6.2
 */