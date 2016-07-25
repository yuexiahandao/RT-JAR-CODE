/*     */ package com.sun.security.auth.module;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ 
/*     */ class Crypt
/*     */ {
/*  60 */   private static final byte[] IP = { 58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7 };
/*     */ 
/*  71 */   private static final byte[] FP = { 40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25 };
/*     */ 
/*  82 */   private static final byte[] PC1_C = { 57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52, 44, 36 };
/*     */ 
/*  89 */   private static final byte[] PC1_D = { 63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4 };
/*     */ 
/*  96 */   private static final byte[] shifts = { 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1 };
/*     */ 
/*  98 */   private static final byte[] PC2_C = { 14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2 };
/*     */ 
/* 105 */   private static final byte[] PC2_D = { 41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32 };
/*     */ 
/* 112 */   private byte[] C = new byte[28];
/* 113 */   private byte[] D = new byte[28];
/*     */   private byte[] KS;
/* 117 */   private byte[] E = new byte[48];
/*     */ 
/* 119 */   private static final byte[] e2 = { 32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17, 16, 17, 18, 19, 20, 21, 20, 21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1 };
/*     */ 
/* 165 */   private static final byte[][] S = { { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7, 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8, 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0, 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 }, { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10, 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5, 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15, 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 }, { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8, 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1, 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7, 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 }, { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15, 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9, 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4, 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 }, { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9, 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6, 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14, 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 }, { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11, 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8, 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6, 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 }, { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1, 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6, 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2, 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 }, { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7, 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2, 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8, 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 } };
/*     */ 
/* 208 */   private static final byte[] P = { 16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6, 22, 11, 4, 25 };
/*     */ 
/* 219 */   private byte[] L = new byte[64];
/* 220 */   private byte[] tempL = new byte[32];
/* 221 */   private byte[] f = new byte[32];
/* 222 */   private byte[] preS = new byte[48];
/*     */ 
/*     */   private void setkey(byte[] paramArrayOfByte)
/*     */   {
/* 134 */     if (this.KS == null) {
/* 135 */       this.KS = new byte[768];
/*     */     }
/*     */ 
/* 138 */     for (int i = 0; i < 28; i++) {
/* 139 */       this.C[i] = paramArrayOfByte[(PC1_C[i] - 1)];
/* 140 */       this.D[i] = paramArrayOfByte[(PC1_D[i] - 1)];
/*     */     }
/* 142 */     for (i = 0; i < 16; i++) {
/* 143 */       for (int k = 0; k < shifts[i]; k++) {
/* 144 */         int m = this.C[0];
/* 145 */         for (j = 0; j < 27; j++)
/* 146 */           this.C[j] = this.C[(j + 1)];
/* 147 */         this.C[27] = m;
/* 148 */         m = this.D[0];
/* 149 */         for (j = 0; j < 27; j++)
/* 150 */           this.D[j] = this.D[(j + 1)];
/* 151 */         this.D[27] = m;
/*     */       }
/* 153 */       for (int j = 0; j < 24; j++) {
/* 154 */         int n = i * 48;
/*     */ 
/* 156 */         this.KS[(n + j)] = this.C[(PC2_C[j] - 1)];
/* 157 */         this.KS[(n + j + 24)] = this.D[(PC2_D[j] - 28 - 1)];
/*     */       }
/*     */     }
/* 160 */     for (i = 0; i < 48; i++)
/* 161 */       this.E[i] = e2[i];
/*     */   }
/*     */ 
/*     */   private void encrypt(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 228 */     int n = 32;
/*     */ 
/* 230 */     if (this.KS == null) {
/* 231 */       this.KS = new byte[768];
/*     */     }
/*     */ 
/* 234 */     for (int k = 0; k < 64; k++)
/* 235 */       this.L[k] = paramArrayOfByte[(IP[k] - 1)];
/*     */     int j;
/* 237 */     for (int i = 0; i < 16; i++) {
/* 238 */       int i1 = i * 48;
/*     */ 
/* 240 */       for (k = 0; k < 32; k++) {
/* 241 */         this.tempL[k] = this.L[(n + k)];
/*     */       }
/* 243 */       for (k = 0; k < 48; k++) {
/* 244 */         this.preS[k] = ((byte)(this.L[(n + this.E[k] - 1)] ^ this.KS[(i1 + k)]));
/*     */       }
/* 246 */       for (k = 0; k < 8; k++) {
/* 247 */         j = 6 * k;
/* 248 */         int m = S[k][((this.preS[(j + 0)] << 5) + (this.preS[(j + 1)] << 3) + (this.preS[(j + 2)] << 2) + (this.preS[(j + 3)] << 1) + (this.preS[(j + 4)] << 0) + (this.preS[(j + 5)] << 4))];
/*     */ 
/* 254 */         j = 4 * k;
/* 255 */         this.f[(j + 0)] = ((byte)(m >> 3 & 0x1));
/* 256 */         this.f[(j + 1)] = ((byte)(m >> 2 & 0x1));
/* 257 */         this.f[(j + 2)] = ((byte)(m >> 1 & 0x1));
/* 258 */         this.f[(j + 3)] = ((byte)(m >> 0 & 0x1));
/*     */       }
/* 260 */       for (k = 0; k < 32; k++) {
/* 261 */         this.L[(n + k)] = ((byte)(this.L[k] ^ this.f[(P[k] - 1)]));
/*     */       }
/* 263 */       for (k = 0; k < 32; k++) {
/* 264 */         this.L[k] = this.tempL[k];
/*     */       }
/*     */     }
/* 267 */     for (k = 0; k < 32; k++) {
/* 268 */       j = this.L[k];
/* 269 */       this.L[k] = this.L[(n + k)];
/* 270 */       this.L[(n + k)] = ((byte)j);
/*     */     }
/* 272 */     for (k = 0; k < 64; k++)
/* 273 */       paramArrayOfByte[k] = this.L[(FP[k] - 1)];
/*     */   }
/*     */ 
/*     */   public synchronized byte[] crypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */   {
/* 304 */     byte[] arrayOfByte1 = new byte[66];
/* 305 */     byte[] arrayOfByte2 = new byte[13];
/*     */ 
/* 309 */     int m = 0;
/*     */     int i;
/*     */     int k;
/* 311 */     for (int j = 0; (m < paramArrayOfByte1.length) && (j < 64); m++) {
/* 312 */       i = paramArrayOfByte1[m];
/* 313 */       for (k = 0; k < 7; j++) {
/* 314 */         arrayOfByte1[j] = ((byte)(i >> 6 - k & 0x1));
/*     */ 
/* 313 */         k++;
/*     */       }
/*     */ 
/* 316 */       j++;
/*     */     }
/*     */ 
/* 319 */     setkey(arrayOfByte1);
/*     */ 
/* 321 */     for (j = 0; j < 66; j++) {
/* 322 */       arrayOfByte1[j] = 0;
/*     */     }
/*     */ 
/* 325 */     for (j = 0; j < 2; j++) {
/* 326 */       i = paramArrayOfByte2[j];
/* 327 */       arrayOfByte2[j] = ((byte)i);
/* 328 */       if (i > 90)
/* 329 */         i -= 6;
/* 330 */       if (i > 57)
/* 331 */         i -= 7;
/* 332 */       i -= 46;
/* 333 */       for (k = 0; k < 6; k++) {
/* 334 */         if ((i >> k & 0x1) != 0) {
/* 335 */           int n = this.E[(6 * j + k)];
/* 336 */           this.E[(6 * j + k)] = this.E[(6 * j + k + 24)];
/* 337 */           this.E[(6 * j + k + 24)] = n;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 342 */     for (j = 0; j < 25; j++) {
/* 343 */       encrypt(arrayOfByte1, 0);
/*     */     }
/*     */ 
/* 346 */     for (j = 0; j < 11; j++) {
/* 347 */       i = 0;
/* 348 */       for (k = 0; k < 6; k++) {
/* 349 */         i <<= 1;
/* 350 */         i |= arrayOfByte1[(6 * j + k)];
/*     */       }
/* 352 */       i += 46;
/* 353 */       if (i > 57) {
/* 354 */         i += 7;
/*     */       }
/* 356 */       if (i > 90) {
/* 357 */         i += 6;
/*     */       }
/* 359 */       arrayOfByte2[(j + 2)] = ((byte)i);
/*     */     }
/*     */ 
/* 362 */     if (arrayOfByte2[1] == 0) {
/* 363 */       arrayOfByte2[1] = arrayOfByte2[0];
/*     */     }
/*     */ 
/* 366 */     return arrayOfByte2;
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/* 382 */     if (paramArrayOfString.length != 2) {
/* 383 */       System.err.println("usage: Crypt password salt");
/* 384 */       System.exit(1);
/*     */     }
/*     */ 
/* 387 */     Crypt localCrypt = new Crypt();
/*     */     try {
/* 389 */       byte[] arrayOfByte = localCrypt.crypt(paramArrayOfString[0].getBytes("ISO-8859-1"), paramArrayOfString[1].getBytes("ISO-8859-1"));
/*     */ 
/* 391 */       for (int i = 0; i < arrayOfByte.length; i++)
/* 392 */         System.out.println(" " + i + " " + (char)arrayOfByte[i]);
/*     */     }
/*     */     catch (UnsupportedEncodingException localUnsupportedEncodingException)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.module.Crypt
 * JD-Core Version:    0.6.2
 */