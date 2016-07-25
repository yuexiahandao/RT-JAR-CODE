/*     */ package sun.text.normalizer;
/*     */ 
/*     */ public final class Utility
/*     */ {
/*  85 */   private static final char[] UNESCAPE_MAP = { 'a', '\007', 'b', '\b', 'e', '\033', 'f', '\f', 'n', '\n', 'r', '\r', 't', '\t', 'v', '\013' };
/*     */ 
/* 264 */   static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
/*     */ 
/*     */   public static final boolean arrayRegionMatches(char[] paramArrayOfChar1, int paramInt1, char[] paramArrayOfChar2, int paramInt2, int paramInt3)
/*     */   {
/*  51 */     int i = paramInt1 + paramInt3;
/*  52 */     int j = paramInt2 - paramInt1;
/*  53 */     for (int k = paramInt1; k < i; k++) {
/*  54 */       if (paramArrayOfChar1[k] != paramArrayOfChar2[(k + j)])
/*  55 */         return false;
/*     */     }
/*  57 */     return true;
/*     */   }
/*     */ 
/*     */   public static final String escape(String paramString)
/*     */   {
/*  65 */     StringBuffer localStringBuffer = new StringBuffer();
/*  66 */     for (int i = 0; i < paramString.length(); ) {
/*  67 */       int j = UTF16.charAt(paramString, i);
/*  68 */       i += UTF16.getCharCount(j);
/*  69 */       if ((j >= 32) && (j <= 127)) {
/*  70 */         if (j == 92)
/*  71 */           localStringBuffer.append("\\\\");
/*     */         else
/*  73 */           localStringBuffer.append((char)j);
/*     */       }
/*     */       else {
/*  76 */         int k = j <= 65535 ? 1 : 0;
/*  77 */         localStringBuffer.append(k != 0 ? "\\u" : "\\U");
/*  78 */         hex(j, k != 0 ? 4 : 8, localStringBuffer);
/*     */       }
/*     */     }
/*  81 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public static int unescapeAt(String paramString, int[] paramArrayOfInt)
/*     */   {
/* 110 */     int j = 0;
/* 111 */     int k = 0;
/* 112 */     int m = 0;
/* 113 */     int n = 0;
/* 114 */     int i1 = 4;
/*     */ 
/* 117 */     int i4 = 0;
/*     */ 
/* 120 */     int i5 = paramArrayOfInt[0];
/* 121 */     int i6 = paramString.length();
/* 122 */     if ((i5 < 0) || (i5 >= i6)) {
/* 123 */       return -1;
/*     */     }
/*     */ 
/* 127 */     int i = UTF16.charAt(paramString, i5);
/* 128 */     i5 += UTF16.getCharCount(i);
/*     */     int i2;
/* 131 */     switch (i) {
/*     */     case 117:
/* 133 */       m = n = 4;
/* 134 */       break;
/*     */     case 85:
/* 136 */       m = n = 8;
/* 137 */       break;
/*     */     case 120:
/* 139 */       m = 1;
/* 140 */       if ((i5 < i6) && (UTF16.charAt(paramString, i5) == 123)) {
/* 141 */         i5++;
/* 142 */         i4 = 1;
/* 143 */         n = 8;
/*     */       } else {
/* 145 */         n = 2;
/*     */       }
/* 147 */       break;
/*     */     default:
/* 149 */       i2 = UCharacter.digit(i, 8);
/* 150 */       if (i2 >= 0) {
/* 151 */         m = 1;
/* 152 */         n = 3;
/* 153 */         k = 1;
/* 154 */         i1 = 3;
/* 155 */         j = i2;
/*     */       }
/*     */       break;
/*     */     }
/* 159 */     if (m != 0) {
/* 160 */       while ((i5 < i6) && (k < n)) {
/* 161 */         i = UTF16.charAt(paramString, i5);
/* 162 */         i2 = UCharacter.digit(i, i1 == 3 ? 8 : 16);
/* 163 */         if (i2 < 0) {
/*     */           break;
/*     */         }
/* 166 */         j = j << i1 | i2;
/* 167 */         i5 += UTF16.getCharCount(i);
/* 168 */         k++;
/*     */       }
/* 170 */       if (k < m) {
/* 171 */         return -1;
/*     */       }
/* 173 */       if (i4 != 0) {
/* 174 */         if (i != 125) {
/* 175 */           return -1;
/*     */         }
/* 177 */         i5++;
/*     */       }
/* 179 */       if ((j < 0) || (j >= 1114112)) {
/* 180 */         return -1;
/*     */       }
/*     */ 
/* 186 */       if ((i5 < i6) && (UTF16.isLeadSurrogate((char)j)))
/*     */       {
/* 188 */         int i7 = i5 + 1;
/* 189 */         i = paramString.charAt(i5);
/* 190 */         if ((i == 92) && (i7 < i6)) {
/* 191 */           int[] arrayOfInt = { i7 };
/* 192 */           i = unescapeAt(paramString, arrayOfInt);
/* 193 */           i7 = arrayOfInt[0];
/*     */         }
/* 195 */         if (UTF16.isTrailSurrogate((char)i)) {
/* 196 */           i5 = i7;
/* 197 */           j = UCharacterProperty.getRawSupplementary((char)j, (char)i);
/*     */         }
/*     */       }
/*     */ 
/* 201 */       paramArrayOfInt[0] = i5;
/* 202 */       return j;
/*     */     }
/*     */ 
/* 206 */     for (int i3 = 0; i3 < UNESCAPE_MAP.length; i3 += 2) {
/* 207 */       if (i == UNESCAPE_MAP[i3]) {
/* 208 */         paramArrayOfInt[0] = i5;
/* 209 */         return UNESCAPE_MAP[(i3 + 1)];
/* 210 */       }if (i < UNESCAPE_MAP[i3])
/*     */       {
/*     */         break;
/*     */       }
/*     */     }
/*     */ 
/* 216 */     if ((i == 99) && (i5 < i6)) {
/* 217 */       i = UTF16.charAt(paramString, i5);
/* 218 */       paramArrayOfInt[0] = (i5 + UTF16.getCharCount(i));
/* 219 */       return 0x1F & i;
/*     */     }
/*     */ 
/* 224 */     paramArrayOfInt[0] = i5;
/* 225 */     return i;
/*     */   }
/*     */ 
/*     */   public static StringBuffer hex(int paramInt1, int paramInt2, StringBuffer paramStringBuffer)
/*     */   {
/* 235 */     return appendNumber(paramStringBuffer, paramInt1, 16, paramInt2);
/*     */   }
/*     */ 
/*     */   public static String hex(int paramInt1, int paramInt2)
/*     */   {
/* 244 */     StringBuffer localStringBuffer = new StringBuffer();
/* 245 */     return appendNumber(localStringBuffer, paramInt1, 16, paramInt2).toString();
/*     */   }
/*     */ 
/*     */   public static int skipWhitespace(String paramString, int paramInt)
/*     */   {
/* 254 */     while (paramInt < paramString.length()) {
/* 255 */       int i = UTF16.charAt(paramString, paramInt);
/* 256 */       if (!UCharacterProperty.isRuleWhiteSpace(i)) {
/*     */         break;
/*     */       }
/* 259 */       paramInt += UTF16.getCharCount(i);
/*     */     }
/* 261 */     return paramInt;
/*     */   }
/*     */ 
/*     */   private static void recursiveAppendNumber(StringBuffer paramStringBuffer, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 285 */     int i = paramInt1 % paramInt2;
/*     */ 
/* 287 */     if ((paramInt1 >= paramInt2) || (paramInt3 > 1)) {
/* 288 */       recursiveAppendNumber(paramStringBuffer, paramInt1 / paramInt2, paramInt2, paramInt3 - 1);
/*     */     }
/*     */ 
/* 291 */     paramStringBuffer.append(DIGITS[i]);
/*     */   }
/*     */ 
/*     */   public static StringBuffer appendNumber(StringBuffer paramStringBuffer, int paramInt1, int paramInt2, int paramInt3)
/*     */     throws IllegalArgumentException
/*     */   {
/* 311 */     if ((paramInt2 < 2) || (paramInt2 > 36)) {
/* 312 */       throw new IllegalArgumentException("Illegal radix " + paramInt2);
/*     */     }
/*     */ 
/* 316 */     int i = paramInt1;
/*     */ 
/* 318 */     if (paramInt1 < 0) {
/* 319 */       i = -paramInt1;
/* 320 */       paramStringBuffer.append("-");
/*     */     }
/*     */ 
/* 323 */     recursiveAppendNumber(paramStringBuffer, i, paramInt2, paramInt3);
/*     */ 
/* 325 */     return paramStringBuffer;
/*     */   }
/*     */ 
/*     */   public static boolean isUnprintable(int paramInt)
/*     */   {
/* 333 */     return (paramInt < 32) || (paramInt > 126);
/*     */   }
/*     */ 
/*     */   public static boolean escapeUnprintable(StringBuffer paramStringBuffer, int paramInt)
/*     */   {
/* 344 */     if (isUnprintable(paramInt)) {
/* 345 */       paramStringBuffer.append('\\');
/* 346 */       if ((paramInt & 0xFFFF0000) != 0) {
/* 347 */         paramStringBuffer.append('U');
/* 348 */         paramStringBuffer.append(DIGITS[(0xF & paramInt >> 28)]);
/* 349 */         paramStringBuffer.append(DIGITS[(0xF & paramInt >> 24)]);
/* 350 */         paramStringBuffer.append(DIGITS[(0xF & paramInt >> 20)]);
/* 351 */         paramStringBuffer.append(DIGITS[(0xF & paramInt >> 16)]);
/*     */       } else {
/* 353 */         paramStringBuffer.append('u');
/*     */       }
/* 355 */       paramStringBuffer.append(DIGITS[(0xF & paramInt >> 12)]);
/* 356 */       paramStringBuffer.append(DIGITS[(0xF & paramInt >> 8)]);
/* 357 */       paramStringBuffer.append(DIGITS[(0xF & paramInt >> 4)]);
/* 358 */       paramStringBuffer.append(DIGITS[(0xF & paramInt)]);
/* 359 */       return true;
/*     */     }
/* 361 */     return false;
/*     */   }
/*     */ 
/*     */   public static void getChars(StringBuffer paramStringBuffer, int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3)
/*     */   {
/* 379 */     if (paramInt1 == paramInt2) {
/* 380 */       return;
/*     */     }
/* 382 */     paramStringBuffer.getChars(paramInt1, paramInt2, paramArrayOfChar, paramInt3);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.normalizer.Utility
 * JD-Core Version:    0.6.2
 */