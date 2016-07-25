/*     */ package java.util.regex;
/*     */ 
/*     */ final class ASCII
/*     */ {
/*     */   static final int UPPER = 256;
/*     */   static final int LOWER = 512;
/*     */   static final int DIGIT = 1024;
/*     */   static final int SPACE = 2048;
/*     */   static final int PUNCT = 4096;
/*     */   static final int CNTRL = 8192;
/*     */   static final int BLANK = 16384;
/*     */   static final int HEX = 32768;
/*     */   static final int UNDER = 65536;
/*     */   static final int ASCII = 65280;
/*     */   static final int ALPHA = 768;
/*     */   static final int ALNUM = 1792;
/*     */   static final int GRAPH = 5888;
/*     */   static final int WORD = 67328;
/*     */   static final int XDIGIT = 32768;
/*  67 */   private static final int[] ctype = { 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 26624, 10240, 10240, 10240, 10240, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 18432, 4096, 4096, 4096, 4096, 4096, 4096, 4096, 4096, 4096, 4096, 4096, 4096, 4096, 4096, 4096, 33792, 33793, 33794, 33795, 33796, 33797, 33798, 33799, 33800, 33801, 4096, 4096, 4096, 4096, 4096, 4096, 4096, 33034, 33035, 33036, 33037, 33038, 33039, 272, 273, 274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284, 285, 286, 287, 288, 289, 290, 291, 4096, 4096, 4096, 4096, 69632, 4096, 33290, 33291, 33292, 33293, 33294, 33295, 528, 529, 530, 531, 532, 533, 534, 535, 536, 537, 538, 539, 540, 541, 542, 543, 544, 545, 546, 547, 4096, 4096, 4096, 4096, 8192 };
/*     */ 
/*     */   static int getType(int paramInt)
/*     */   {
/* 199 */     return (paramInt & 0xFFFFFF80) == 0 ? ctype[paramInt] : 0;
/*     */   }
/*     */ 
/*     */   static boolean isType(int paramInt1, int paramInt2) {
/* 203 */     return (getType(paramInt1) & paramInt2) != 0;
/*     */   }
/*     */ 
/*     */   static boolean isAscii(int paramInt) {
/* 207 */     return (paramInt & 0xFFFFFF80) == 0;
/*     */   }
/*     */ 
/*     */   static boolean isAlpha(int paramInt) {
/* 211 */     return isType(paramInt, 768);
/*     */   }
/*     */ 
/*     */   static boolean isDigit(int paramInt) {
/* 215 */     return (paramInt - 48 | 57 - paramInt) >= 0;
/*     */   }
/*     */ 
/*     */   static boolean isAlnum(int paramInt) {
/* 219 */     return isType(paramInt, 1792);
/*     */   }
/*     */ 
/*     */   static boolean isGraph(int paramInt) {
/* 223 */     return isType(paramInt, 5888);
/*     */   }
/*     */ 
/*     */   static boolean isPrint(int paramInt) {
/* 227 */     return (paramInt - 32 | 126 - paramInt) >= 0;
/*     */   }
/*     */ 
/*     */   static boolean isPunct(int paramInt) {
/* 231 */     return isType(paramInt, 4096);
/*     */   }
/*     */ 
/*     */   static boolean isSpace(int paramInt) {
/* 235 */     return isType(paramInt, 2048);
/*     */   }
/*     */ 
/*     */   static boolean isHexDigit(int paramInt) {
/* 239 */     return isType(paramInt, 32768);
/*     */   }
/*     */ 
/*     */   static boolean isOctDigit(int paramInt) {
/* 243 */     return (paramInt - 48 | 55 - paramInt) >= 0;
/*     */   }
/*     */ 
/*     */   static boolean isCntrl(int paramInt) {
/* 247 */     return isType(paramInt, 8192);
/*     */   }
/*     */ 
/*     */   static boolean isLower(int paramInt) {
/* 251 */     return (paramInt - 97 | 122 - paramInt) >= 0;
/*     */   }
/*     */ 
/*     */   static boolean isUpper(int paramInt) {
/* 255 */     return (paramInt - 65 | 90 - paramInt) >= 0;
/*     */   }
/*     */ 
/*     */   static boolean isWord(int paramInt) {
/* 259 */     return isType(paramInt, 67328);
/*     */   }
/*     */ 
/*     */   static int toDigit(int paramInt) {
/* 263 */     return ctype[(paramInt & 0x7F)] & 0x3F;
/*     */   }
/*     */ 
/*     */   static int toLower(int paramInt) {
/* 267 */     return isUpper(paramInt) ? paramInt + 32 : paramInt;
/*     */   }
/*     */ 
/*     */   static int toUpper(int paramInt) {
/* 271 */     return isLower(paramInt) ? paramInt - 32 : paramInt;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.regex.ASCII
 * JD-Core Version:    0.6.2
 */