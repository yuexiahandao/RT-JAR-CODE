/*     */ package java.lang;
/*     */ 
/*     */ class CharacterData01 extends CharacterData
/*     */ {
/*     */   static final CharacterData instance;
/*     */   static final char[] X;
/*     */   static final char[] Y;
/*     */   static final int[] A;
/*     */   static final String A_DATA = "";
/*     */   static final char[] B;
/*     */ 
/*     */   int getProperties(int paramInt)
/*     */   {
/*  71 */     int i = (char)paramInt;
/*  72 */     int j = A[(Y[(X[(i >> 5)] << '\004' | i >> 1 & 0xF)] << '\001' | i & 0x1)];
/*  73 */     return j;
/*     */   }
/*     */ 
/*     */   int getPropertiesEx(int paramInt) {
/*  77 */     int i = (char)paramInt;
/*  78 */     int j = B[(Y[(X[(i >> 5)] << '\004' | i >> 1 & 0xF)] << '\001' | i & 0x1)];
/*  79 */     return j;
/*     */   }
/*     */ 
/*     */   int getType(int paramInt) {
/*  83 */     int i = getProperties(paramInt);
/*  84 */     return i & 0x1F;
/*     */   }
/*     */ 
/*     */   boolean isOtherLowercase(int paramInt) {
/*  88 */     int i = getPropertiesEx(paramInt);
/*  89 */     return (i & 0x1) != 0;
/*     */   }
/*     */ 
/*     */   boolean isOtherUppercase(int paramInt) {
/*  93 */     int i = getPropertiesEx(paramInt);
/*  94 */     return (i & 0x2) != 0;
/*     */   }
/*     */ 
/*     */   boolean isOtherAlphabetic(int paramInt) {
/*  98 */     int i = getPropertiesEx(paramInt);
/*  99 */     return (i & 0x4) != 0;
/*     */   }
/*     */ 
/*     */   boolean isIdeographic(int paramInt) {
/* 103 */     int i = getPropertiesEx(paramInt);
/* 104 */     return (i & 0x10) != 0;
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
/* 136 */     if ((j & 0x20000) != 0) {
/* 137 */       int k = j << 5 >> 23;
/* 138 */       i = paramInt + k;
/*     */     }
/* 140 */     return i;
/*     */   }
/*     */ 
/*     */   int toUpperCase(int paramInt) {
/* 144 */     int i = paramInt;
/* 145 */     int j = getProperties(paramInt);
/*     */ 
/* 147 */     if ((j & 0x10000) != 0) {
/* 148 */       int k = j << 5 >> 23;
/* 149 */       i = paramInt - k;
/*     */     }
/* 151 */     return i;
/*     */   }
/*     */ 
/*     */   int toTitleCase(int paramInt) {
/* 155 */     int i = paramInt;
/* 156 */     int j = getProperties(paramInt);
/*     */ 
/* 158 */     if ((j & 0x8000) != 0)
/*     */     {
/* 160 */       if ((j & 0x10000) == 0)
/*     */       {
/* 163 */         i = paramInt + 1;
/*     */       }
/* 165 */       else if ((j & 0x20000) == 0)
/*     */       {
/* 168 */         i = paramInt - 1;
/*     */       }
/*     */ 
/*     */     }
/* 176 */     else if ((j & 0x10000) != 0)
/*     */     {
/* 179 */       i = toUpperCase(paramInt);
/*     */     }
/* 181 */     return i;
/*     */   }
/*     */ 
/*     */   int digit(int paramInt1, int paramInt2) {
/* 185 */     int i = -1;
/* 186 */     if ((paramInt2 >= 2) && (paramInt2 <= 36)) {
/* 187 */       int j = getProperties(paramInt1);
/* 188 */       int k = j & 0x1F;
/* 189 */       if (k == 9) {
/* 190 */         i = paramInt1 + ((j & 0x3E0) >> 5) & 0x1F;
/*     */       }
/* 192 */       else if ((j & 0xC00) == 3072)
/*     */       {
/* 194 */         i = (paramInt1 + ((j & 0x3E0) >> 5) & 0x1F) + 10;
/*     */       }
/*     */     }
/* 197 */     return i < paramInt2 ? i : -1;
/*     */   }
/*     */ 
/*     */   int getNumericValue(int paramInt) {
/* 201 */     int i = getProperties(paramInt);
/* 202 */     int j = -1;
/*     */ 
/* 204 */     switch (i & 0xC00) {
/*     */     case 0:
/*     */     default:
/* 207 */       j = -1;
/* 208 */       break;
/*     */     case 1024:
/* 210 */       j = paramInt + ((i & 0x3E0) >> 5) & 0x1F;
/* 211 */       break;
/*     */     case 2048:
/* 213 */       switch (paramInt) { case 65811:
/* 214 */         j = 40; break;
/*     */       case 65812:
/* 215 */         j = 50; break;
/*     */       case 65813:
/* 216 */         j = 60; break;
/*     */       case 65814:
/* 217 */         j = 70; break;
/*     */       case 65815:
/* 218 */         j = 80; break;
/*     */       case 65816:
/* 219 */         j = 90; break;
/*     */       case 65817:
/* 220 */         j = 100; break;
/*     */       case 65818:
/* 221 */         j = 200; break;
/*     */       case 65819:
/* 222 */         j = 300; break;
/*     */       case 65820:
/* 223 */         j = 400; break;
/*     */       case 65821:
/* 224 */         j = 500; break;
/*     */       case 65822:
/* 225 */         j = 600; break;
/*     */       case 65823:
/* 226 */         j = 700; break;
/*     */       case 65824:
/* 227 */         j = 800; break;
/*     */       case 65825:
/* 228 */         j = 900; break;
/*     */       case 65826:
/* 229 */         j = 1000; break;
/*     */       case 65827:
/* 230 */         j = 2000; break;
/*     */       case 65828:
/* 231 */         j = 3000; break;
/*     */       case 65829:
/* 232 */         j = 4000; break;
/*     */       case 65830:
/* 233 */         j = 5000; break;
/*     */       case 65831:
/* 234 */         j = 6000; break;
/*     */       case 65832:
/* 235 */         j = 7000; break;
/*     */       case 65833:
/* 236 */         j = 8000; break;
/*     */       case 65834:
/* 237 */         j = 9000; break;
/*     */       case 65835:
/* 238 */         j = 10000; break;
/*     */       case 65836:
/* 239 */         j = 20000; break;
/*     */       case 65837:
/* 240 */         j = 30000; break;
/*     */       case 65838:
/* 241 */         j = 40000; break;
/*     */       case 65839:
/* 242 */         j = 50000; break;
/*     */       case 65840:
/* 243 */         j = 60000; break;
/*     */       case 65841:
/* 244 */         j = 70000; break;
/*     */       case 65842:
/* 245 */         j = 80000; break;
/*     */       case 65843:
/* 246 */         j = 90000; break;
/*     */       case 66339:
/* 247 */         j = 50; break;
/*     */       case 65860:
/* 249 */         j = 50; break;
/*     */       case 65861:
/* 250 */         j = 500; break;
/*     */       case 65862:
/* 251 */         j = 5000; break;
/*     */       case 65863:
/* 252 */         j = 50000; break;
/*     */       case 65866:
/* 253 */         j = 50; break;
/*     */       case 65867:
/* 254 */         j = 100; break;
/*     */       case 65868:
/* 255 */         j = 500; break;
/*     */       case 65869:
/* 256 */         j = 1000; break;
/*     */       case 65870:
/* 257 */         j = 5000; break;
/*     */       case 65873:
/* 258 */         j = 50; break;
/*     */       case 65874:
/* 259 */         j = 100; break;
/*     */       case 65875:
/* 260 */         j = 500; break;
/*     */       case 65876:
/* 261 */         j = 1000; break;
/*     */       case 65877:
/* 262 */         j = 10000; break;
/*     */       case 65878:
/* 263 */         j = 50000; break;
/*     */       case 65894:
/* 264 */         j = 50; break;
/*     */       case 65895:
/* 265 */         j = 50; break;
/*     */       case 65896:
/* 266 */         j = 50; break;
/*     */       case 65897:
/* 267 */         j = 50; break;
/*     */       case 65898:
/* 268 */         j = 100; break;
/*     */       case 65899:
/* 269 */         j = 300; break;
/*     */       case 65900:
/* 270 */         j = 500; break;
/*     */       case 65901:
/* 271 */         j = 500; break;
/*     */       case 65902:
/* 272 */         j = 500; break;
/*     */       case 65903:
/* 273 */         j = 500; break;
/*     */       case 65904:
/* 274 */         j = 500; break;
/*     */       case 65905:
/* 275 */         j = 1000; break;
/*     */       case 65906:
/* 276 */         j = 5000; break;
/*     */       case 65908:
/* 277 */         j = 50; break;
/*     */       case 66369:
/* 278 */         j = 90; break;
/*     */       case 66378:
/* 279 */         j = 900; break;
/*     */       case 66517:
/* 280 */         j = 100; break;
/*     */       case 67677:
/* 281 */         j = 100; break;
/*     */       case 67678:
/* 282 */         j = 1000; break;
/*     */       case 67679:
/* 283 */         j = 10000; break;
/*     */       case 67865:
/* 284 */         j = 100; break;
/*     */       case 68166:
/* 285 */         j = 100; break;
/*     */       case 68167:
/* 286 */         j = 1000; break;
/*     */       case 68222:
/* 287 */         j = 50; break;
/*     */       case 68446:
/* 288 */         j = 100; break;
/*     */       case 68447:
/* 289 */         j = 1000; break;
/*     */       case 68478:
/* 290 */         j = 100; break;
/*     */       case 68479:
/* 291 */         j = 1000; break;
/*     */       case 69228:
/* 292 */         j = 40; break;
/*     */       case 69229:
/* 293 */         j = 50; break;
/*     */       case 69230:
/* 294 */         j = 60; break;
/*     */       case 69231:
/* 295 */         j = 70; break;
/*     */       case 69232:
/* 296 */         j = 80; break;
/*     */       case 69233:
/* 297 */         j = 90; break;
/*     */       case 69234:
/* 298 */         j = 100; break;
/*     */       case 69235:
/* 299 */         j = 200; break;
/*     */       case 69236:
/* 300 */         j = 300; break;
/*     */       case 69237:
/* 301 */         j = 400; break;
/*     */       case 69238:
/* 302 */         j = 500; break;
/*     */       case 69239:
/* 303 */         j = 600; break;
/*     */       case 69240:
/* 304 */         j = 700; break;
/*     */       case 69241:
/* 305 */         j = 800; break;
/*     */       case 69242:
/* 306 */         j = 900; break;
/*     */       case 69726:
/* 307 */         j = 40; break;
/*     */       case 69727:
/* 308 */         j = 50; break;
/*     */       case 69728:
/* 309 */         j = 60; break;
/*     */       case 69729:
/* 310 */         j = 70; break;
/*     */       case 69730:
/* 311 */         j = 80; break;
/*     */       case 69731:
/* 312 */         j = 90; break;
/*     */       case 69732:
/* 313 */         j = 100; break;
/*     */       case 69733:
/* 314 */         j = 1000; break;
/*     */       case 119660:
/* 315 */         j = 40; break;
/*     */       case 119661:
/* 316 */         j = 50; break;
/*     */       case 119662:
/* 317 */         j = 60; break;
/*     */       case 119663:
/* 318 */         j = 70; break;
/*     */       case 119664:
/* 319 */         j = 80; break;
/*     */       case 119665:
/* 320 */         j = 90; break;
/*     */       default:
/* 321 */         j = -2; } break;
/*     */     case 3072:
/* 326 */       j = (paramInt + ((i & 0x3E0) >> 5) & 0x1F) + 10;
/*     */     }
/*     */ 
/* 329 */     return j;
/*     */   }
/*     */ 
/*     */   boolean isWhitespace(int paramInt) {
/* 333 */     int i = getProperties(paramInt);
/* 334 */     return (i & 0x7000) == 16384;
/*     */   }
/*     */ 
/*     */   byte getDirectionality(int paramInt) {
/* 338 */     int i = getProperties(paramInt);
/* 339 */     byte b = (byte)((i & 0x78000000) >> 27);
/* 340 */     if (b == 15) {
/* 341 */       b = -1;
/*     */     }
/* 343 */     return b;
/*     */   }
/*     */ 
/*     */   boolean isMirrored(int paramInt) {
/* 347 */     int i = getProperties(paramInt);
/* 348 */     return (i & 0x80000000) != 0;
/*     */   }
/*     */   static {
/* 351 */     instance = new CharacterData01();
/*     */ 
/* 358 */     X = "".toCharArray();
/*     */ 
/* 470 */     Y = "".toCharArray();
/*     */ 
/* 576 */     A = new int[292];
/*     */ 
/* 622 */     B = "".toCharArray();
/*     */ 
/* 644 */     char[] arrayOfChar = "".toCharArray();
/* 645 */     assert (arrayOfChar.length == 584);
/* 646 */     int i = 0; int j = 0;
/* 647 */     while (i < 584) {
/* 648 */       int k = arrayOfChar[(i++)] << '\020';
/* 649 */       A[(j++)] = (k | arrayOfChar[(i++)]);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.CharacterData01
 * JD-Core Version:    0.6.2
 */