/*     */ package java.lang;
/*     */ 
/*     */ class CharacterDataLatin1 extends CharacterData
/*     */ {
/*     */   static char[] sharpsMap;
/*     */   static final CharacterDataLatin1 instance;
/*     */   static final int[] A;
/*     */   static final String A_DATA = "";
/*     */   static final char[] B;
/*     */ 
/*     */   int getProperties(int paramInt)
/*     */   {
/*  71 */     int i = (char)paramInt;
/*  72 */     int j = A[i];
/*  73 */     return j;
/*     */   }
/*     */ 
/*     */   int getPropertiesEx(int paramInt) {
/*  77 */     int i = (char)paramInt;
/*  78 */     int j = B[i];
/*  79 */     return j;
/*     */   }
/*     */ 
/*     */   boolean isOtherLowercase(int paramInt) {
/*  83 */     int i = getPropertiesEx(paramInt);
/*  84 */     return (i & 0x1) != 0;
/*     */   }
/*     */ 
/*     */   boolean isOtherUppercase(int paramInt) {
/*  88 */     int i = getPropertiesEx(paramInt);
/*  89 */     return (i & 0x2) != 0;
/*     */   }
/*     */ 
/*     */   boolean isOtherAlphabetic(int paramInt) {
/*  93 */     int i = getPropertiesEx(paramInt);
/*  94 */     return (i & 0x4) != 0;
/*     */   }
/*     */ 
/*     */   boolean isIdeographic(int paramInt) {
/*  98 */     int i = getPropertiesEx(paramInt);
/*  99 */     return (i & 0x10) != 0;
/*     */   }
/*     */ 
/*     */   int getType(int paramInt) {
/* 103 */     int i = getProperties(paramInt);
/* 104 */     return i & 0x1F;
/*     */   }
/*     */ 
/*     */   boolean isJavaIdentifierStart(int paramInt) {
/* 108 */     int i = getProperties(paramInt);
/* 109 */     return (i & 0x7000) >= 20480;
/*     */   }
/*     */ 
/*     */   boolean isJavaIdentifierPart(int paramInt) {
/* 113 */     int i = getProperties(paramInt);
/* 114 */     return (i & 0x3000) != 0;
/*     */   }
/*     */ 
/*     */   boolean isUnicodeIdentifierStart(int paramInt) {
/* 118 */     int i = getProperties(paramInt);
/* 119 */     return (i & 0x7000) == 28672;
/*     */   }
/*     */ 
/*     */   boolean isUnicodeIdentifierPart(int paramInt) {
/* 123 */     int i = getProperties(paramInt);
/* 124 */     return (i & 0x1000) != 0;
/*     */   }
/*     */ 
/*     */   boolean isIdentifierIgnorable(int paramInt) {
/* 128 */     int i = getProperties(paramInt);
/* 129 */     return (i & 0x7000) == 4096;
/*     */   }
/*     */ 
/*     */   int toLowerCase(int paramInt) {
/* 133 */     int i = paramInt;
/* 134 */     int j = getProperties(paramInt);
/*     */ 
/* 136 */     if (((j & 0x20000) != 0) && ((j & 0x7FC0000) != 133955584))
/*     */     {
/* 138 */       int k = j << 5 >> 23;
/* 139 */       i = paramInt + k;
/*     */     }
/* 141 */     return i;
/*     */   }
/*     */ 
/*     */   int toUpperCase(int paramInt) {
/* 145 */     int i = paramInt;
/* 146 */     int j = getProperties(paramInt);
/*     */ 
/* 148 */     if ((j & 0x10000) != 0) {
/* 149 */       if ((j & 0x7FC0000) != 133955584) {
/* 150 */         int k = j << 5 >> 23;
/* 151 */         i = paramInt - k;
/* 152 */       } else if (paramInt == 181) {
/* 153 */         i = 924;
/*     */       }
/*     */     }
/* 156 */     return i;
/*     */   }
/*     */ 
/*     */   int toTitleCase(int paramInt) {
/* 160 */     return toUpperCase(paramInt);
/*     */   }
/*     */ 
/*     */   int digit(int paramInt1, int paramInt2) {
/* 164 */     int i = -1;
/* 165 */     if ((paramInt2 >= 2) && (paramInt2 <= 36)) {
/* 166 */       int j = getProperties(paramInt1);
/* 167 */       int k = j & 0x1F;
/* 168 */       if (k == 9) {
/* 169 */         i = paramInt1 + ((j & 0x3E0) >> 5) & 0x1F;
/*     */       }
/* 171 */       else if ((j & 0xC00) == 3072)
/*     */       {
/* 173 */         i = (paramInt1 + ((j & 0x3E0) >> 5) & 0x1F) + 10;
/*     */       }
/*     */     }
/* 176 */     return i < paramInt2 ? i : -1;
/*     */   }
/*     */ 
/*     */   int getNumericValue(int paramInt) {
/* 180 */     int i = getProperties(paramInt);
/* 181 */     int j = -1;
/*     */ 
/* 183 */     switch (i & 0xC00) {
/*     */     case 0:
/*     */     default:
/* 186 */       j = -1;
/* 187 */       break;
/*     */     case 1024:
/* 189 */       j = paramInt + ((i & 0x3E0) >> 5) & 0x1F;
/* 190 */       break;
/*     */     case 2048:
/* 192 */       j = -2;
/* 193 */       break;
/*     */     case 3072:
/* 195 */       j = (paramInt + ((i & 0x3E0) >> 5) & 0x1F) + 10;
/*     */     }
/*     */ 
/* 198 */     return j;
/*     */   }
/*     */ 
/*     */   boolean isWhitespace(int paramInt) {
/* 202 */     int i = getProperties(paramInt);
/* 203 */     return (i & 0x7000) == 16384;
/*     */   }
/*     */ 
/*     */   byte getDirectionality(int paramInt) {
/* 207 */     int i = getProperties(paramInt);
/* 208 */     byte b = (byte)((i & 0x78000000) >> 27);
/*     */ 
/* 210 */     if (b == 15) {
/* 211 */       b = -1;
/*     */     }
/* 213 */     return b;
/*     */   }
/*     */ 
/*     */   boolean isMirrored(int paramInt) {
/* 217 */     int i = getProperties(paramInt);
/* 218 */     return (i & 0x80000000) != 0;
/*     */   }
/*     */ 
/*     */   int toUpperCaseEx(int paramInt) {
/* 222 */     int i = paramInt;
/* 223 */     int j = getProperties(paramInt);
/*     */ 
/* 225 */     if ((j & 0x10000) != 0) {
/* 226 */       if ((j & 0x7FC0000) != 133955584) {
/* 227 */         int k = j << 5 >> 23;
/* 228 */         i = paramInt - k;
/*     */       }
/*     */       else {
/* 231 */         switch (paramInt) {
/*     */         case 181:
/* 233 */           i = 924; break;
/*     */         default:
/* 234 */           i = -1;
/*     */         }
/*     */       }
/*     */     }
/* 238 */     return i;
/*     */   }
/*     */ 
/*     */   char[] toUpperCaseCharArray(int paramInt)
/*     */   {
/* 244 */     char[] arrayOfChar = { (char)paramInt };
/* 245 */     if (paramInt == 223) {
/* 246 */       arrayOfChar = sharpsMap;
/*     */     }
/* 248 */     return arrayOfChar;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 241 */     sharpsMap = new char[] { 'S', 'S' };
/*     */ 
/* 251 */     instance = new CharacterDataLatin1();
/*     */ 
/* 258 */     A = new int[256];
/*     */ 
/* 300 */     B = "".toCharArray();
/*     */ 
/* 320 */     char[] arrayOfChar = "".toCharArray();
/* 321 */     assert (arrayOfChar.length == 512);
/* 322 */     int i = 0; int j = 0;
/* 323 */     while (i < 512) {
/* 324 */       int k = arrayOfChar[(i++)] << '\020';
/* 325 */       A[(j++)] = (k | arrayOfChar[(i++)]);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.CharacterDataLatin1
 * JD-Core Version:    0.6.2
 */