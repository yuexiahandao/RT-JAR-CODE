/*     */ package sun.text.normalizer;
/*     */ 
/*     */ public final class UTF16
/*     */ {
/*     */   public static final int CODEPOINT_MIN_VALUE = 0;
/*     */   public static final int CODEPOINT_MAX_VALUE = 1114111;
/*     */   public static final int SUPPLEMENTARY_MIN_VALUE = 65536;
/*     */   public static final int LEAD_SURROGATE_MIN_VALUE = 55296;
/*     */   public static final int TRAIL_SURROGATE_MIN_VALUE = 56320;
/*     */   public static final int LEAD_SURROGATE_MAX_VALUE = 56319;
/*     */   public static final int TRAIL_SURROGATE_MAX_VALUE = 57343;
/*     */   public static final int SURROGATE_MIN_VALUE = 55296;
/*     */   private static final int LEAD_SURROGATE_SHIFT_ = 10;
/*     */   private static final int TRAIL_SURROGATE_MASK_ = 1023;
/*     */   private static final int LEAD_SURROGATE_OFFSET_ = 55232;
/*     */ 
/*     */   public static int charAt(String paramString, int paramInt)
/*     */   {
/* 187 */     int i = paramString.charAt(paramInt);
/* 188 */     if (i < 55296) {
/* 189 */       return i;
/*     */     }
/* 191 */     return _charAt(paramString, paramInt, i);
/*     */   }
/*     */ 
/*     */   private static int _charAt(String paramString, int paramInt, char paramChar) {
/* 195 */     if (paramChar > 57343) {
/* 196 */       return paramChar;
/*     */     }
/*     */ 
/* 203 */     if (paramChar <= 56319) {
/* 204 */       paramInt++;
/* 205 */       if (paramString.length() != paramInt) {
/* 206 */         int i = paramString.charAt(paramInt);
/* 207 */         if ((i >= 56320) && (i <= 57343))
/* 208 */           return UCharacterProperty.getRawSupplementary(paramChar, i);
/*     */       }
/*     */     }
/*     */     else {
/* 212 */       paramInt--;
/* 213 */       if (paramInt >= 0)
/*     */       {
/* 215 */         int j = paramString.charAt(paramInt);
/* 216 */         if ((j >= 55296) && (j <= 56319)) {
/* 217 */           return UCharacterProperty.getRawSupplementary(j, paramChar);
/*     */         }
/*     */       }
/*     */     }
/* 221 */     return paramChar;
/*     */   }
/*     */ 
/*     */   public static int charAt(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 248 */     paramInt3 += paramInt1;
/* 249 */     if ((paramInt3 < paramInt1) || (paramInt3 >= paramInt2)) {
/* 250 */       throw new ArrayIndexOutOfBoundsException(paramInt3);
/*     */     }
/*     */ 
/* 253 */     int i = paramArrayOfChar[paramInt3];
/* 254 */     if (!isSurrogate(i))
/* 255 */       return i;
/*     */     char c;
/* 261 */     if (i <= 56319) {
/* 262 */       paramInt3++;
/* 263 */       if (paramInt3 >= paramInt2) {
/* 264 */         return i;
/*     */       }
/* 266 */       c = paramArrayOfChar[paramInt3];
/* 267 */       if (isTrailSurrogate(c))
/* 268 */         return UCharacterProperty.getRawSupplementary(i, c);
/*     */     }
/*     */     else
/*     */     {
/* 272 */       if (paramInt3 == paramInt1) {
/* 273 */         return i;
/*     */       }
/* 275 */       paramInt3--;
/* 276 */       c = paramArrayOfChar[paramInt3];
/* 277 */       if (isLeadSurrogate(c))
/* 278 */         return UCharacterProperty.getRawSupplementary(c, i);
/*     */     }
/* 280 */     return i;
/*     */   }
/*     */ 
/*     */   public static int getCharCount(int paramInt)
/*     */   {
/* 294 */     if (paramInt < 65536) {
/* 295 */       return 1;
/*     */     }
/* 297 */     return 2;
/*     */   }
/*     */ 
/*     */   public static boolean isSurrogate(char paramChar)
/*     */   {
/* 308 */     return (55296 <= paramChar) && (paramChar <= 57343);
/*     */   }
/*     */ 
/*     */   public static boolean isTrailSurrogate(char paramChar)
/*     */   {
/* 320 */     return (56320 <= paramChar) && (paramChar <= 57343);
/*     */   }
/*     */ 
/*     */   public static boolean isLeadSurrogate(char paramChar)
/*     */   {
/* 332 */     return (55296 <= paramChar) && (paramChar <= 56319);
/*     */   }
/*     */ 
/*     */   public static char getLeadSurrogate(int paramInt)
/*     */   {
/* 348 */     if (paramInt >= 65536) {
/* 349 */       return (char)(55232 + (paramInt >> 10));
/*     */     }
/*     */ 
/* 353 */     return '\000';
/*     */   }
/*     */ 
/*     */   public static char getTrailSurrogate(int paramInt)
/*     */   {
/* 368 */     if (paramInt >= 65536) {
/* 369 */       return (char)(56320 + (paramInt & 0x3FF));
/*     */     }
/*     */ 
/* 373 */     return (char)paramInt;
/*     */   }
/*     */ 
/*     */   public static String valueOf(int paramInt)
/*     */   {
/* 390 */     if ((paramInt < 0) || (paramInt > 1114111)) {
/* 391 */       throw new IllegalArgumentException("Illegal codepoint");
/*     */     }
/* 393 */     return toString(paramInt);
/*     */   }
/*     */ 
/*     */   public static StringBuffer append(StringBuffer paramStringBuffer, int paramInt)
/*     */   {
/* 411 */     if ((paramInt < 0) || (paramInt > 1114111)) {
/* 412 */       throw new IllegalArgumentException("Illegal codepoint: " + Integer.toHexString(paramInt));
/*     */     }
/*     */ 
/* 416 */     if (paramInt >= 65536)
/*     */     {
/* 418 */       paramStringBuffer.append(getLeadSurrogate(paramInt));
/* 419 */       paramStringBuffer.append(getTrailSurrogate(paramInt));
/*     */     }
/*     */     else {
/* 422 */       paramStringBuffer.append((char)paramInt);
/*     */     }
/* 424 */     return paramStringBuffer;
/*     */   }
/*     */ 
/*     */   public static int moveCodePointOffset(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 444 */     int i = paramArrayOfChar.length;
/*     */ 
/* 447 */     int k = paramInt3 + paramInt1;
/* 448 */     if ((paramInt1 < 0) || (paramInt2 < paramInt1)) {
/* 449 */       throw new StringIndexOutOfBoundsException(paramInt1);
/*     */     }
/* 451 */     if (paramInt2 > i) {
/* 452 */       throw new StringIndexOutOfBoundsException(paramInt2);
/*     */     }
/* 454 */     if ((paramInt3 < 0) || (k > paramInt2))
/* 455 */       throw new StringIndexOutOfBoundsException(paramInt3);
/*     */     char c;
/* 457 */     if (paramInt4 > 0) {
/* 458 */       if (paramInt4 + k > i) {
/* 459 */         throw new StringIndexOutOfBoundsException(k);
/*     */       }
/* 461 */       j = paramInt4;
/* 462 */       while ((k < paramInt2) && (j > 0))
/*     */       {
/* 464 */         c = paramArrayOfChar[k];
/* 465 */         if ((isLeadSurrogate(c)) && (k + 1 < paramInt2) && (isTrailSurrogate(paramArrayOfChar[(k + 1)])))
/*     */         {
/* 467 */           k++;
/*     */         }
/* 469 */         j--;
/* 470 */         k++;
/*     */       }
/*     */     }
/* 473 */     if (k + paramInt4 < paramInt1) {
/* 474 */       throw new StringIndexOutOfBoundsException(k);
/*     */     }
/* 476 */     for (int j = -paramInt4; j > 0; j--) {
/* 477 */       k--;
/* 478 */       if (k < paramInt1) {
/*     */         break;
/*     */       }
/* 481 */       c = paramArrayOfChar[k];
/* 482 */       if ((isTrailSurrogate(c)) && (k > paramInt1) && (isLeadSurrogate(paramArrayOfChar[(k - 1)]))) {
/* 483 */         k--;
/*     */       }
/*     */     }
/*     */ 
/* 487 */     if (j != 0) {
/* 488 */       throw new StringIndexOutOfBoundsException(paramInt4);
/*     */     }
/* 490 */     k -= paramInt1;
/* 491 */     return k;
/*     */   }
/*     */ 
/*     */   private static String toString(int paramInt)
/*     */   {
/* 529 */     if (paramInt < 65536) {
/* 530 */       return String.valueOf((char)paramInt);
/*     */     }
/*     */ 
/* 533 */     StringBuffer localStringBuffer = new StringBuffer();
/* 534 */     localStringBuffer.append(getLeadSurrogate(paramInt));
/* 535 */     localStringBuffer.append(getTrailSurrogate(paramInt));
/* 536 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.normalizer.UTF16
 * JD-Core Version:    0.6.2
 */