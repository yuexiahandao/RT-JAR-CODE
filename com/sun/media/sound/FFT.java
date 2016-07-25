/*     */ package com.sun.media.sound;
/*     */ 
/*     */ public final class FFT
/*     */ {
/*     */   private final double[] w;
/*     */   private final int fftFrameSize;
/*     */   private final int sign;
/*     */   private final int[] bitm_array;
/*     */   private final int fftFrameSize2;
/*     */ 
/*     */   public FFT(int paramInt1, int paramInt2)
/*     */   {
/*  45 */     this.w = computeTwiddleFactors(paramInt1, paramInt2);
/*     */ 
/*  47 */     this.fftFrameSize = paramInt1;
/*  48 */     this.sign = paramInt2;
/*  49 */     this.fftFrameSize2 = (paramInt1 << 1);
/*     */ 
/*  52 */     this.bitm_array = new int[this.fftFrameSize2];
/*  53 */     for (int i = 2; i < this.fftFrameSize2; i += 2)
/*     */     {
/*  56 */       int k = 2; for (int j = 0; k < this.fftFrameSize2; k <<= 1) {
/*  57 */         if ((i & k) != 0)
/*  58 */           j++;
/*  59 */         j <<= 1;
/*     */       }
/*  61 */       this.bitm_array[i] = j;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void transform(double[] paramArrayOfDouble)
/*     */   {
/*  67 */     bitreversal(paramArrayOfDouble);
/*  68 */     calc(this.fftFrameSize, paramArrayOfDouble, this.sign, this.w);
/*     */   }
/*     */ 
/*     */   private static final double[] computeTwiddleFactors(int paramInt1, int paramInt2)
/*     */   {
/*  74 */     int i = (int)(Math.log(paramInt1) / Math.log(2.0D));
/*     */ 
/*  76 */     double[] arrayOfDouble = new double[(paramInt1 - 1) * 4];
/*  77 */     double d1 = 0;
/*     */ 
/*  79 */     int j = 0;
/*     */     double d4;
/*  79 */     for (double d2 = 2; j < i; j++) {
/*  80 */       d3 = d2;
/*  81 */       d2 <<= 1;
/*     */ 
/*  83 */       d4 = 1.0D;
/*  84 */       double d5 = 0.0D;
/*     */ 
/*  86 */       double d8 = 3.141592653589793D / (d3 >> 1);
/*  87 */       double d10 = Math.cos(d8);
/*  88 */       double d12 = paramInt2 * Math.sin(d8);
/*     */ 
/*  90 */       for (int m = 0; m < d3; m += 2) {
/*  91 */         arrayOfDouble[(d1++)] = d4;
/*  92 */         arrayOfDouble[(d1++)] = d5;
/*     */ 
/*  94 */         double d14 = d4;
/*  95 */         d4 = d14 * d10 - d5 * d12;
/*  96 */         d5 = d14 * d12 + d5 * d10;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 103 */     d1 = 0;
/* 104 */     j = arrayOfDouble.length >> 1;
/* 105 */     d2 = 0; for (double d3 = 2; d2 < i - 1; d2++) {
/* 106 */       d4 = d3;
/* 107 */       d3 *= 2;
/*     */ 
/* 109 */       int k = d1 + d4;
/* 110 */       for (double d6 = 0; d6 < d4; d6 += 2) {
/* 111 */         double d7 = arrayOfDouble[(d1++)];
/* 112 */         double d9 = arrayOfDouble[(d1++)];
/* 113 */         double d11 = arrayOfDouble[(k++)];
/* 114 */         double d13 = arrayOfDouble[(k++)];
/* 115 */         arrayOfDouble[(j++)] = (d7 * d11 - d9 * d13);
/* 116 */         arrayOfDouble[(j++)] = (d7 * d13 + d9 * d11);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 122 */     return arrayOfDouble;
/*     */   }
/*     */ 
/*     */   private static final void calc(int paramInt1, double[] paramArrayOfDouble1, int paramInt2, double[] paramArrayOfDouble2)
/*     */   {
/* 128 */     int i = paramInt1 << 1;
/*     */ 
/* 130 */     int j = 2;
/*     */ 
/* 132 */     if (j >= i)
/* 133 */       return;
/* 134 */     int k = j - 2;
/* 135 */     if (paramInt2 == -1)
/* 136 */       calcF4F(paramInt1, paramArrayOfDouble1, k, j, paramArrayOfDouble2);
/*     */     else
/* 138 */       calcF4I(paramInt1, paramArrayOfDouble1, k, j, paramArrayOfDouble2);
/*     */   }
/*     */ 
/*     */   private static final void calcF2E(int paramInt1, double[] paramArrayOfDouble1, int paramInt2, int paramInt3, double[] paramArrayOfDouble2)
/*     */   {
/* 144 */     int i = paramInt3;
/* 145 */     for (int j = 0; j < i; j += 2) {
/* 146 */       double d1 = paramArrayOfDouble2[(paramInt2++)];
/* 147 */       double d2 = paramArrayOfDouble2[(paramInt2++)];
/* 148 */       int k = j + i;
/* 149 */       double d3 = paramArrayOfDouble1[k];
/* 150 */       double d4 = paramArrayOfDouble1[(k + 1)];
/* 151 */       double d5 = paramArrayOfDouble1[j];
/* 152 */       double d6 = paramArrayOfDouble1[(j + 1)];
/* 153 */       double d7 = d3 * d1 - d4 * d2;
/* 154 */       double d8 = d3 * d2 + d4 * d1;
/* 155 */       paramArrayOfDouble1[k] = (d5 - d7);
/* 156 */       paramArrayOfDouble1[(k + 1)] = (d6 - d8);
/* 157 */       paramArrayOfDouble1[j] = (d5 + d7);
/* 158 */       paramArrayOfDouble1[(j + 1)] = (d6 + d8);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final void calcF4F(int paramInt1, double[] paramArrayOfDouble1, int paramInt2, int paramInt3, double[] paramArrayOfDouble2)
/*     */   {
/* 168 */     int i = paramInt1 << 1;
/*     */ 
/* 171 */     int j = paramArrayOfDouble2.length >> 1;
/* 172 */     while (paramInt3 < i)
/*     */     {
/* 174 */       if (paramInt3 << 2 == i)
/*     */       {
/* 177 */         calcF4FE(paramInt1, paramArrayOfDouble1, paramInt2, paramInt3, paramArrayOfDouble2);
/* 178 */         return;
/*     */       }
/* 180 */       int k = paramInt3;
/* 181 */       int m = paramInt3 << 1;
/* 182 */       if (m == i)
/*     */       {
/* 184 */         calcF2E(paramInt1, paramArrayOfDouble1, paramInt2, paramInt3, paramArrayOfDouble2);
/* 185 */         return;
/*     */       }
/* 187 */       paramInt3 <<= 2;
/* 188 */       int n = paramInt2 + k;
/* 189 */       int i1 = paramInt2 + j;
/*     */ 
/* 192 */       paramInt2 += 2;
/* 193 */       n += 2;
/* 194 */       i1 += 2;
/*     */ 
/* 196 */       for (int i2 = 0; i2 < i; i2 += paramInt3) {
/* 197 */         int i3 = i2 + k;
/*     */ 
/* 199 */         double d2 = paramArrayOfDouble1[i3];
/* 200 */         double d4 = paramArrayOfDouble1[(i3 + 1)];
/* 201 */         double d6 = paramArrayOfDouble1[i2];
/* 202 */         double d8 = paramArrayOfDouble1[(i2 + 1)];
/*     */ 
/* 204 */         i2 += m;
/* 205 */         i3 += m;
/* 206 */         double d10 = paramArrayOfDouble1[i3];
/* 207 */         double d12 = paramArrayOfDouble1[(i3 + 1)];
/* 208 */         double d13 = paramArrayOfDouble1[i2];
/* 209 */         double d15 = paramArrayOfDouble1[(i2 + 1)];
/*     */ 
/* 211 */         double d17 = d2;
/* 212 */         double d19 = d4;
/*     */ 
/* 214 */         d2 = d6 - d17;
/* 215 */         d4 = d8 - d19;
/* 216 */         d6 += d17;
/* 217 */         d8 += d19;
/*     */ 
/* 219 */         double d21 = d13;
/* 220 */         double d23 = d15;
/* 221 */         double d25 = d10;
/* 222 */         double d27 = d12;
/*     */ 
/* 224 */         d17 = d25 - d21;
/* 225 */         d19 = d27 - d23;
/*     */ 
/* 227 */         d10 = d2 + d19;
/* 228 */         d12 = d4 - d17;
/* 229 */         d2 -= d19;
/* 230 */         d4 += d17;
/*     */ 
/* 232 */         d17 = d21 + d25;
/* 233 */         d19 = d23 + d27;
/*     */ 
/* 235 */         d13 = d6 - d17;
/* 236 */         d15 = d8 - d19;
/* 237 */         d6 += d17;
/* 238 */         d8 += d19;
/*     */ 
/* 240 */         paramArrayOfDouble1[i3] = d10;
/* 241 */         paramArrayOfDouble1[(i3 + 1)] = d12;
/* 242 */         paramArrayOfDouble1[i2] = d13;
/* 243 */         paramArrayOfDouble1[(i2 + 1)] = d15;
/*     */ 
/* 245 */         i2 -= m;
/* 246 */         i3 -= m;
/* 247 */         paramArrayOfDouble1[i3] = d2;
/* 248 */         paramArrayOfDouble1[(i3 + 1)] = d4;
/* 249 */         paramArrayOfDouble1[i2] = d6;
/* 250 */         paramArrayOfDouble1[(i2 + 1)] = d8;
/*     */       }
/*     */ 
/* 255 */       for (i2 = 2; i2 < k; i2 += 2) {
/* 256 */         double d1 = paramArrayOfDouble2[(paramInt2++)];
/* 257 */         double d3 = paramArrayOfDouble2[(paramInt2++)];
/* 258 */         double d5 = paramArrayOfDouble2[(n++)];
/* 259 */         double d7 = paramArrayOfDouble2[(n++)];
/* 260 */         double d9 = paramArrayOfDouble2[(i1++)];
/* 261 */         double d11 = paramArrayOfDouble2[(i1++)];
/*     */ 
/* 266 */         for (int i4 = i2; i4 < i; i4 += paramInt3) {
/* 267 */           int i5 = i4 + k;
/*     */ 
/* 269 */           double d14 = paramArrayOfDouble1[i5];
/* 270 */           double d16 = paramArrayOfDouble1[(i5 + 1)];
/* 271 */           double d18 = paramArrayOfDouble1[i4];
/* 272 */           double d20 = paramArrayOfDouble1[(i4 + 1)];
/*     */ 
/* 274 */           i4 += m;
/* 275 */           i5 += m;
/* 276 */           double d22 = paramArrayOfDouble1[i5];
/* 277 */           double d24 = paramArrayOfDouble1[(i5 + 1)];
/* 278 */           double d26 = paramArrayOfDouble1[i4];
/* 279 */           double d28 = paramArrayOfDouble1[(i4 + 1)];
/*     */ 
/* 281 */           double d29 = d14 * d1 - d16 * d3;
/* 282 */           double d30 = d14 * d3 + d16 * d1;
/*     */ 
/* 284 */           d14 = d18 - d29;
/* 285 */           d16 = d20 - d30;
/* 286 */           d18 += d29;
/* 287 */           d20 += d30;
/*     */ 
/* 289 */           double d31 = d26 * d5 - d28 * d7;
/* 290 */           double d32 = d26 * d7 + d28 * d5;
/* 291 */           double d33 = d22 * d9 - d24 * d11;
/* 292 */           double d34 = d22 * d11 + d24 * d9;
/*     */ 
/* 294 */           d29 = d33 - d31;
/* 295 */           d30 = d34 - d32;
/*     */ 
/* 297 */           d22 = d14 + d30;
/* 298 */           d24 = d16 - d29;
/* 299 */           d14 -= d30;
/* 300 */           d16 += d29;
/*     */ 
/* 302 */           d29 = d31 + d33;
/* 303 */           d30 = d32 + d34;
/*     */ 
/* 305 */           d26 = d18 - d29;
/* 306 */           d28 = d20 - d30;
/* 307 */           d18 += d29;
/* 308 */           d20 += d30;
/*     */ 
/* 310 */           paramArrayOfDouble1[i5] = d22;
/* 311 */           paramArrayOfDouble1[(i5 + 1)] = d24;
/* 312 */           paramArrayOfDouble1[i4] = d26;
/* 313 */           paramArrayOfDouble1[(i4 + 1)] = d28;
/*     */ 
/* 315 */           i4 -= m;
/* 316 */           i5 -= m;
/* 317 */           paramArrayOfDouble1[i5] = d14;
/* 318 */           paramArrayOfDouble1[(i5 + 1)] = d16;
/* 319 */           paramArrayOfDouble1[i4] = d18;
/* 320 */           paramArrayOfDouble1[(i4 + 1)] = d20;
/*     */         }
/*     */       }
/*     */ 
/* 324 */       paramInt2 += (k << 1);
/*     */     }
/*     */ 
/* 328 */     calcF2E(paramInt1, paramArrayOfDouble1, paramInt2, paramInt3, paramArrayOfDouble2);
/*     */   }
/*     */ 
/*     */   private static final void calcF4I(int paramInt1, double[] paramArrayOfDouble1, int paramInt2, int paramInt3, double[] paramArrayOfDouble2)
/*     */   {
/* 336 */     int i = paramInt1 << 1;
/*     */ 
/* 339 */     int j = paramArrayOfDouble2.length >> 1;
/* 340 */     while (paramInt3 < i)
/*     */     {
/* 342 */       if (paramInt3 << 2 == i)
/*     */       {
/* 345 */         calcF4IE(paramInt1, paramArrayOfDouble1, paramInt2, paramInt3, paramArrayOfDouble2);
/* 346 */         return;
/*     */       }
/* 348 */       int k = paramInt3;
/* 349 */       int m = paramInt3 << 1;
/* 350 */       if (m == i)
/*     */       {
/* 352 */         calcF2E(paramInt1, paramArrayOfDouble1, paramInt2, paramInt3, paramArrayOfDouble2);
/* 353 */         return;
/*     */       }
/* 355 */       paramInt3 <<= 2;
/* 356 */       int n = paramInt2 + k;
/* 357 */       int i1 = paramInt2 + j;
/*     */ 
/* 359 */       paramInt2 += 2;
/* 360 */       n += 2;
/* 361 */       i1 += 2;
/*     */ 
/* 363 */       for (int i2 = 0; i2 < i; i2 += paramInt3) {
/* 364 */         int i3 = i2 + k;
/*     */ 
/* 366 */         double d2 = paramArrayOfDouble1[i3];
/* 367 */         double d4 = paramArrayOfDouble1[(i3 + 1)];
/* 368 */         double d6 = paramArrayOfDouble1[i2];
/* 369 */         double d8 = paramArrayOfDouble1[(i2 + 1)];
/*     */ 
/* 371 */         i2 += m;
/* 372 */         i3 += m;
/* 373 */         double d10 = paramArrayOfDouble1[i3];
/* 374 */         double d12 = paramArrayOfDouble1[(i3 + 1)];
/* 375 */         double d13 = paramArrayOfDouble1[i2];
/* 376 */         double d15 = paramArrayOfDouble1[(i2 + 1)];
/*     */ 
/* 378 */         double d17 = d2;
/* 379 */         double d19 = d4;
/*     */ 
/* 381 */         d2 = d6 - d17;
/* 382 */         d4 = d8 - d19;
/* 383 */         d6 += d17;
/* 384 */         d8 += d19;
/*     */ 
/* 386 */         double d21 = d13;
/* 387 */         double d23 = d15;
/* 388 */         double d25 = d10;
/* 389 */         double d27 = d12;
/*     */ 
/* 391 */         d17 = d21 - d25;
/* 392 */         d19 = d23 - d27;
/*     */ 
/* 394 */         d10 = d2 + d19;
/* 395 */         d12 = d4 - d17;
/* 396 */         d2 -= d19;
/* 397 */         d4 += d17;
/*     */ 
/* 399 */         d17 = d21 + d25;
/* 400 */         d19 = d23 + d27;
/*     */ 
/* 402 */         d13 = d6 - d17;
/* 403 */         d15 = d8 - d19;
/* 404 */         d6 += d17;
/* 405 */         d8 += d19;
/*     */ 
/* 407 */         paramArrayOfDouble1[i3] = d10;
/* 408 */         paramArrayOfDouble1[(i3 + 1)] = d12;
/* 409 */         paramArrayOfDouble1[i2] = d13;
/* 410 */         paramArrayOfDouble1[(i2 + 1)] = d15;
/*     */ 
/* 412 */         i2 -= m;
/* 413 */         i3 -= m;
/* 414 */         paramArrayOfDouble1[i3] = d2;
/* 415 */         paramArrayOfDouble1[(i3 + 1)] = d4;
/* 416 */         paramArrayOfDouble1[i2] = d6;
/* 417 */         paramArrayOfDouble1[(i2 + 1)] = d8;
/*     */       }
/*     */ 
/* 422 */       for (i2 = 2; i2 < k; i2 += 2) {
/* 423 */         double d1 = paramArrayOfDouble2[(paramInt2++)];
/* 424 */         double d3 = paramArrayOfDouble2[(paramInt2++)];
/* 425 */         double d5 = paramArrayOfDouble2[(n++)];
/* 426 */         double d7 = paramArrayOfDouble2[(n++)];
/* 427 */         double d9 = paramArrayOfDouble2[(i1++)];
/* 428 */         double d11 = paramArrayOfDouble2[(i1++)];
/*     */ 
/* 433 */         for (int i4 = i2; i4 < i; i4 += paramInt3) {
/* 434 */           int i5 = i4 + k;
/*     */ 
/* 436 */           double d14 = paramArrayOfDouble1[i5];
/* 437 */           double d16 = paramArrayOfDouble1[(i5 + 1)];
/* 438 */           double d18 = paramArrayOfDouble1[i4];
/* 439 */           double d20 = paramArrayOfDouble1[(i4 + 1)];
/*     */ 
/* 441 */           i4 += m;
/* 442 */           i5 += m;
/* 443 */           double d22 = paramArrayOfDouble1[i5];
/* 444 */           double d24 = paramArrayOfDouble1[(i5 + 1)];
/* 445 */           double d26 = paramArrayOfDouble1[i4];
/* 446 */           double d28 = paramArrayOfDouble1[(i4 + 1)];
/*     */ 
/* 448 */           double d29 = d14 * d1 - d16 * d3;
/* 449 */           double d30 = d14 * d3 + d16 * d1;
/*     */ 
/* 451 */           d14 = d18 - d29;
/* 452 */           d16 = d20 - d30;
/* 453 */           d18 += d29;
/* 454 */           d20 += d30;
/*     */ 
/* 456 */           double d31 = d26 * d5 - d28 * d7;
/* 457 */           double d32 = d26 * d7 + d28 * d5;
/* 458 */           double d33 = d22 * d9 - d24 * d11;
/* 459 */           double d34 = d22 * d11 + d24 * d9;
/*     */ 
/* 461 */           d29 = d31 - d33;
/* 462 */           d30 = d32 - d34;
/*     */ 
/* 464 */           d22 = d14 + d30;
/* 465 */           d24 = d16 - d29;
/* 466 */           d14 -= d30;
/* 467 */           d16 += d29;
/*     */ 
/* 469 */           d29 = d31 + d33;
/* 470 */           d30 = d32 + d34;
/*     */ 
/* 472 */           d26 = d18 - d29;
/* 473 */           d28 = d20 - d30;
/* 474 */           d18 += d29;
/* 475 */           d20 += d30;
/*     */ 
/* 477 */           paramArrayOfDouble1[i5] = d22;
/* 478 */           paramArrayOfDouble1[(i5 + 1)] = d24;
/* 479 */           paramArrayOfDouble1[i4] = d26;
/* 480 */           paramArrayOfDouble1[(i4 + 1)] = d28;
/*     */ 
/* 482 */           i4 -= m;
/* 483 */           i5 -= m;
/* 484 */           paramArrayOfDouble1[i5] = d14;
/* 485 */           paramArrayOfDouble1[(i5 + 1)] = d16;
/* 486 */           paramArrayOfDouble1[i4] = d18;
/* 487 */           paramArrayOfDouble1[(i4 + 1)] = d20;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 492 */       paramInt2 += (k << 1);
/*     */     }
/*     */ 
/* 496 */     calcF2E(paramInt1, paramArrayOfDouble1, paramInt2, paramInt3, paramArrayOfDouble2);
/*     */   }
/*     */ 
/*     */   private static final void calcF4FE(int paramInt1, double[] paramArrayOfDouble1, int paramInt2, int paramInt3, double[] paramArrayOfDouble2)
/*     */   {
/* 504 */     int i = paramInt1 << 1;
/*     */ 
/* 507 */     int j = paramArrayOfDouble2.length >> 1;
/* 508 */     while (paramInt3 < i)
/*     */     {
/* 510 */       int k = paramInt3;
/* 511 */       int m = paramInt3 << 1;
/* 512 */       if (m == i)
/*     */       {
/* 514 */         calcF2E(paramInt1, paramArrayOfDouble1, paramInt2, paramInt3, paramArrayOfDouble2);
/* 515 */         return;
/*     */       }
/* 517 */       paramInt3 <<= 2;
/* 518 */       int n = paramInt2 + k;
/* 519 */       int i1 = paramInt2 + j;
/* 520 */       for (int i2 = 0; i2 < k; i2 += 2) {
/* 521 */         double d1 = paramArrayOfDouble2[(paramInt2++)];
/* 522 */         double d2 = paramArrayOfDouble2[(paramInt2++)];
/* 523 */         double d3 = paramArrayOfDouble2[(n++)];
/* 524 */         double d4 = paramArrayOfDouble2[(n++)];
/* 525 */         double d5 = paramArrayOfDouble2[(i1++)];
/* 526 */         double d6 = paramArrayOfDouble2[(i1++)];
/*     */ 
/* 531 */         int i3 = i2 + k;
/*     */ 
/* 533 */         double d7 = paramArrayOfDouble1[i3];
/* 534 */         double d8 = paramArrayOfDouble1[(i3 + 1)];
/* 535 */         double d9 = paramArrayOfDouble1[i2];
/* 536 */         double d10 = paramArrayOfDouble1[(i2 + 1)];
/*     */ 
/* 538 */         i2 += m;
/* 539 */         i3 += m;
/* 540 */         double d11 = paramArrayOfDouble1[i3];
/* 541 */         double d12 = paramArrayOfDouble1[(i3 + 1)];
/* 542 */         double d13 = paramArrayOfDouble1[i2];
/* 543 */         double d14 = paramArrayOfDouble1[(i2 + 1)];
/*     */ 
/* 545 */         double d15 = d7 * d1 - d8 * d2;
/* 546 */         double d16 = d7 * d2 + d8 * d1;
/*     */ 
/* 548 */         d7 = d9 - d15;
/* 549 */         d8 = d10 - d16;
/* 550 */         d9 += d15;
/* 551 */         d10 += d16;
/*     */ 
/* 553 */         double d17 = d13 * d3 - d14 * d4;
/* 554 */         double d18 = d13 * d4 + d14 * d3;
/* 555 */         double d19 = d11 * d5 - d12 * d6;
/* 556 */         double d20 = d11 * d6 + d12 * d5;
/*     */ 
/* 558 */         d15 = d19 - d17;
/* 559 */         d16 = d20 - d18;
/*     */ 
/* 561 */         d11 = d7 + d16;
/* 562 */         d12 = d8 - d15;
/* 563 */         d7 -= d16;
/* 564 */         d8 += d15;
/*     */ 
/* 566 */         d15 = d17 + d19;
/* 567 */         d16 = d18 + d20;
/*     */ 
/* 569 */         d13 = d9 - d15;
/* 570 */         d14 = d10 - d16;
/* 571 */         d9 += d15;
/* 572 */         d10 += d16;
/*     */ 
/* 574 */         paramArrayOfDouble1[i3] = d11;
/* 575 */         paramArrayOfDouble1[(i3 + 1)] = d12;
/* 576 */         paramArrayOfDouble1[i2] = d13;
/* 577 */         paramArrayOfDouble1[(i2 + 1)] = d14;
/*     */ 
/* 579 */         i2 -= m;
/* 580 */         i3 -= m;
/* 581 */         paramArrayOfDouble1[i3] = d7;
/* 582 */         paramArrayOfDouble1[(i3 + 1)] = d8;
/* 583 */         paramArrayOfDouble1[i2] = d9;
/* 584 */         paramArrayOfDouble1[(i2 + 1)] = d10;
/*     */       }
/*     */ 
/* 588 */       paramInt2 += (k << 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final void calcF4IE(int paramInt1, double[] paramArrayOfDouble1, int paramInt2, int paramInt3, double[] paramArrayOfDouble2)
/*     */   {
/* 598 */     int i = paramInt1 << 1;
/*     */ 
/* 601 */     int j = paramArrayOfDouble2.length >> 1;
/* 602 */     while (paramInt3 < i)
/*     */     {
/* 604 */       int k = paramInt3;
/* 605 */       int m = paramInt3 << 1;
/* 606 */       if (m == i)
/*     */       {
/* 608 */         calcF2E(paramInt1, paramArrayOfDouble1, paramInt2, paramInt3, paramArrayOfDouble2);
/* 609 */         return;
/*     */       }
/* 611 */       paramInt3 <<= 2;
/* 612 */       int n = paramInt2 + k;
/* 613 */       int i1 = paramInt2 + j;
/* 614 */       for (int i2 = 0; i2 < k; i2 += 2) {
/* 615 */         double d1 = paramArrayOfDouble2[(paramInt2++)];
/* 616 */         double d2 = paramArrayOfDouble2[(paramInt2++)];
/* 617 */         double d3 = paramArrayOfDouble2[(n++)];
/* 618 */         double d4 = paramArrayOfDouble2[(n++)];
/* 619 */         double d5 = paramArrayOfDouble2[(i1++)];
/* 620 */         double d6 = paramArrayOfDouble2[(i1++)];
/*     */ 
/* 625 */         int i3 = i2 + k;
/*     */ 
/* 627 */         double d7 = paramArrayOfDouble1[i3];
/* 628 */         double d8 = paramArrayOfDouble1[(i3 + 1)];
/* 629 */         double d9 = paramArrayOfDouble1[i2];
/* 630 */         double d10 = paramArrayOfDouble1[(i2 + 1)];
/*     */ 
/* 632 */         i2 += m;
/* 633 */         i3 += m;
/* 634 */         double d11 = paramArrayOfDouble1[i3];
/* 635 */         double d12 = paramArrayOfDouble1[(i3 + 1)];
/* 636 */         double d13 = paramArrayOfDouble1[i2];
/* 637 */         double d14 = paramArrayOfDouble1[(i2 + 1)];
/*     */ 
/* 639 */         double d15 = d7 * d1 - d8 * d2;
/* 640 */         double d16 = d7 * d2 + d8 * d1;
/*     */ 
/* 642 */         d7 = d9 - d15;
/* 643 */         d8 = d10 - d16;
/* 644 */         d9 += d15;
/* 645 */         d10 += d16;
/*     */ 
/* 647 */         double d17 = d13 * d3 - d14 * d4;
/* 648 */         double d18 = d13 * d4 + d14 * d3;
/* 649 */         double d19 = d11 * d5 - d12 * d6;
/* 650 */         double d20 = d11 * d6 + d12 * d5;
/*     */ 
/* 652 */         d15 = d17 - d19;
/* 653 */         d16 = d18 - d20;
/*     */ 
/* 655 */         d11 = d7 + d16;
/* 656 */         d12 = d8 - d15;
/* 657 */         d7 -= d16;
/* 658 */         d8 += d15;
/*     */ 
/* 660 */         d15 = d17 + d19;
/* 661 */         d16 = d18 + d20;
/*     */ 
/* 663 */         d13 = d9 - d15;
/* 664 */         d14 = d10 - d16;
/* 665 */         d9 += d15;
/* 666 */         d10 += d16;
/*     */ 
/* 668 */         paramArrayOfDouble1[i3] = d11;
/* 669 */         paramArrayOfDouble1[(i3 + 1)] = d12;
/* 670 */         paramArrayOfDouble1[i2] = d13;
/* 671 */         paramArrayOfDouble1[(i2 + 1)] = d14;
/*     */ 
/* 673 */         i2 -= m;
/* 674 */         i3 -= m;
/* 675 */         paramArrayOfDouble1[i3] = d7;
/* 676 */         paramArrayOfDouble1[(i3 + 1)] = d8;
/* 677 */         paramArrayOfDouble1[i2] = d9;
/* 678 */         paramArrayOfDouble1[(i2 + 1)] = d10;
/*     */       }
/*     */ 
/* 682 */       paramInt2 += (k << 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   private final void bitreversal(double[] paramArrayOfDouble)
/*     */   {
/* 689 */     if (this.fftFrameSize < 4) {
/* 690 */       return;
/*     */     }
/* 692 */     int i = this.fftFrameSize2 - 2;
/* 693 */     for (int j = 0; j < this.fftFrameSize; j += 4) {
/* 694 */       int k = this.bitm_array[j];
/*     */ 
/* 697 */       if (j < k)
/*     */       {
/* 699 */         m = j;
/* 700 */         n = k;
/*     */ 
/* 704 */         d1 = paramArrayOfDouble[m];
/* 705 */         paramArrayOfDouble[m] = paramArrayOfDouble[n];
/* 706 */         paramArrayOfDouble[n] = d1;
/*     */ 
/* 708 */         m++;
/* 709 */         n++;
/* 710 */         d2 = paramArrayOfDouble[m];
/* 711 */         paramArrayOfDouble[m] = paramArrayOfDouble[n];
/* 712 */         paramArrayOfDouble[n] = d2;
/*     */ 
/* 714 */         m = i - j;
/* 715 */         n = i - k;
/*     */ 
/* 719 */         d1 = paramArrayOfDouble[m];
/* 720 */         paramArrayOfDouble[m] = paramArrayOfDouble[n];
/* 721 */         paramArrayOfDouble[n] = d1;
/*     */ 
/* 723 */         m++;
/* 724 */         n++;
/* 725 */         d2 = paramArrayOfDouble[m];
/* 726 */         paramArrayOfDouble[m] = paramArrayOfDouble[n];
/* 727 */         paramArrayOfDouble[n] = d2;
/*     */       }
/*     */ 
/* 732 */       int m = k + this.fftFrameSize;
/*     */ 
/* 735 */       int n = j + 2;
/* 736 */       double d1 = paramArrayOfDouble[n];
/* 737 */       paramArrayOfDouble[n] = paramArrayOfDouble[m];
/* 738 */       paramArrayOfDouble[m] = d1;
/*     */ 
/* 740 */       n++;
/* 741 */       m++;
/* 742 */       double d2 = paramArrayOfDouble[n];
/* 743 */       paramArrayOfDouble[n] = paramArrayOfDouble[m];
/* 744 */       paramArrayOfDouble[m] = d2;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.FFT
 * JD-Core Version:    0.6.2
 */