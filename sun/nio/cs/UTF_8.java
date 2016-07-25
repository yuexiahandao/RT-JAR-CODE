/*     */ package sun.nio.cs;
/*     */ 
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ 
/*     */ class UTF_8 extends Unicode
/*     */ {
/*     */   public UTF_8()
/*     */   {
/*  60 */     super("UTF-8", StandardCharsets.aliases_UTF_8);
/*     */   }
/*     */ 
/*     */   public String historicalName() {
/*  64 */     return "UTF8";
/*     */   }
/*     */ 
/*     */   public CharsetDecoder newDecoder() {
/*  68 */     return new Decoder(this, null);
/*     */   }
/*     */ 
/*     */   public CharsetEncoder newEncoder() {
/*  72 */     return new Encoder(this, null);
/*     */   }
/*     */ 
/*     */   static final void updatePositions(Buffer paramBuffer1, int paramInt1, Buffer paramBuffer2, int paramInt2)
/*     */   {
/*  77 */     paramBuffer1.position(paramInt1 - paramBuffer1.arrayOffset());
/*  78 */     paramBuffer2.position(paramInt2 - paramBuffer2.arrayOffset());
/*     */   }
/*     */ 
/*     */   private static class Decoder extends CharsetDecoder implements ArrayDecoder
/*     */   {
/*     */     private Decoder(Charset paramCharset) {
/*  84 */       super(1.0F, 1.0F);
/*     */     }
/*     */ 
/*     */     private static boolean isNotContinuation(int paramInt) {
/*  88 */       return (paramInt & 0xC0) != 128;
/*     */     }
/*     */ 
/*     */     private static boolean isMalformed2(int paramInt1, int paramInt2)
/*     */     {
/*  93 */       return ((paramInt1 & 0x1E) == 0) || ((paramInt2 & 0xC0) != 128);
/*     */     }
/*     */ 
/*     */     private static boolean isMalformed3(int paramInt1, int paramInt2, int paramInt3)
/*     */     {
/*  99 */       return ((paramInt1 == -32) && ((paramInt2 & 0xE0) == 128)) || ((paramInt2 & 0xC0) != 128) || ((paramInt3 & 0xC0) != 128);
/*     */     }
/*     */ 
/*     */     private static boolean isMalformed4(int paramInt1, int paramInt2, int paramInt3)
/*     */     {
/* 109 */       return ((paramInt1 & 0xC0) != 128) || ((paramInt2 & 0xC0) != 128) || ((paramInt3 & 0xC0) != 128);
/*     */     }
/*     */ 
/*     */     private static CoderResult lookupN(ByteBuffer paramByteBuffer, int paramInt)
/*     */     {
/* 115 */       for (int i = 1; i < paramInt; i++) {
/* 116 */         if (isNotContinuation(paramByteBuffer.get()))
/* 117 */           return CoderResult.malformedForLength(i);
/*     */       }
/* 119 */       return CoderResult.malformedForLength(paramInt);
/*     */     }
/*     */ 
/*     */     private static CoderResult malformedN(ByteBuffer paramByteBuffer, int paramInt)
/*     */     {
/*     */       int i;
/*     */       int j;
/* 123 */       switch (paramInt) {
/*     */       case 1:
/* 125 */         i = paramByteBuffer.get();
/* 126 */         if (i >> 2 == -2)
/*     */         {
/* 128 */           if (paramByteBuffer.remaining() < 4)
/* 129 */             return CoderResult.UNDERFLOW;
/* 130 */           return lookupN(paramByteBuffer, 5);
/*     */         }
/* 132 */         if (i >> 1 == -2)
/*     */         {
/* 134 */           if (paramByteBuffer.remaining() < 5)
/* 135 */             return CoderResult.UNDERFLOW;
/* 136 */           return lookupN(paramByteBuffer, 6);
/*     */         }
/* 138 */         return CoderResult.malformedForLength(1);
/*     */       case 2:
/* 140 */         return CoderResult.malformedForLength(1);
/*     */       case 3:
/* 142 */         i = paramByteBuffer.get();
/* 143 */         j = paramByteBuffer.get();
/* 144 */         return CoderResult.malformedForLength(((i == -32) && ((j & 0xE0) == 128)) || (isNotContinuation(j)) ? 1 : 2);
/*     */       case 4:
/* 148 */         i = paramByteBuffer.get() & 0xFF;
/* 149 */         j = paramByteBuffer.get() & 0xFF;
/* 150 */         if ((i > 244) || ((i == 240) && ((j < 144) || (j > 191))) || ((i == 244) && ((j & 0xF0) != 128)) || (isNotContinuation(j)))
/*     */         {
/* 154 */           return CoderResult.malformedForLength(1);
/* 155 */         }if (isNotContinuation(paramByteBuffer.get()))
/* 156 */           return CoderResult.malformedForLength(2);
/* 157 */         return CoderResult.malformedForLength(3);
/*     */       }
/* 159 */       if (!$assertionsDisabled) throw new AssertionError();
/* 160 */       return null;
/*     */     }
/*     */ 
/*     */     private static CoderResult malformed(ByteBuffer paramByteBuffer, int paramInt1, CharBuffer paramCharBuffer, int paramInt2, int paramInt3)
/*     */     {
/* 168 */       paramByteBuffer.position(paramInt1 - paramByteBuffer.arrayOffset());
/* 169 */       CoderResult localCoderResult = malformedN(paramByteBuffer, paramInt3);
/* 170 */       UTF_8.updatePositions(paramByteBuffer, paramInt1, paramCharBuffer, paramInt2);
/* 171 */       return localCoderResult;
/*     */     }
/*     */ 
/*     */     private static CoderResult malformed(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2)
/*     */     {
/* 177 */       paramByteBuffer.position(paramInt1);
/* 178 */       CoderResult localCoderResult = malformedN(paramByteBuffer, paramInt2);
/* 179 */       paramByteBuffer.position(paramInt1);
/* 180 */       return localCoderResult;
/*     */     }
/*     */ 
/*     */     private static CoderResult xflow(Buffer paramBuffer1, int paramInt1, int paramInt2, Buffer paramBuffer2, int paramInt3, int paramInt4)
/*     */     {
/* 185 */       UTF_8.updatePositions(paramBuffer1, paramInt1, paramBuffer2, paramInt3);
/* 186 */       return (paramInt4 == 0) || (paramInt2 - paramInt1 < paramInt4) ? CoderResult.UNDERFLOW : CoderResult.OVERFLOW;
/*     */     }
/*     */ 
/*     */     private static CoderResult xflow(Buffer paramBuffer, int paramInt1, int paramInt2)
/*     */     {
/* 191 */       CoderResult localCoderResult = (paramInt2 == 0) || (paramBuffer.remaining() < paramInt2 - 1) ? CoderResult.UNDERFLOW : CoderResult.OVERFLOW;
/*     */ 
/* 193 */       paramBuffer.position(paramInt1);
/* 194 */       return localCoderResult;
/*     */     }
/*     */ 
/*     */     private CoderResult decodeArrayLoop(ByteBuffer paramByteBuffer, CharBuffer paramCharBuffer)
/*     */     {
/* 201 */       byte[] arrayOfByte = paramByteBuffer.array();
/* 202 */       int i = paramByteBuffer.arrayOffset() + paramByteBuffer.position();
/* 203 */       int j = paramByteBuffer.arrayOffset() + paramByteBuffer.limit();
/*     */ 
/* 205 */       char[] arrayOfChar = paramCharBuffer.array();
/* 206 */       int k = paramCharBuffer.arrayOffset() + paramCharBuffer.position();
/* 207 */       int m = paramCharBuffer.arrayOffset() + paramCharBuffer.limit();
/* 208 */       int n = k + Math.min(j - i, m - k);
/*     */ 
/* 211 */       while ((k < n) && (arrayOfByte[i] >= 0)) {
/* 212 */         arrayOfChar[(k++)] = ((char)arrayOfByte[(i++)]);
/*     */       }
/* 214 */       while (i < j) {
/* 215 */         int i1 = arrayOfByte[i];
/* 216 */         if (i1 >= 0)
/*     */         {
/* 218 */           if (k >= m)
/* 219 */             return xflow(paramByteBuffer, i, j, paramCharBuffer, k, 1);
/* 220 */           arrayOfChar[(k++)] = ((char)i1);
/* 221 */           i++;
/*     */         }
/*     */         else
/*     */         {
/*     */           int i2;
/* 222 */           if (i1 >> 5 == -2)
/*     */           {
/* 224 */             if ((j - i < 2) || (k >= m))
/* 225 */               return xflow(paramByteBuffer, i, j, paramCharBuffer, k, 2);
/* 226 */             i2 = arrayOfByte[(i + 1)];
/* 227 */             if (isMalformed2(i1, i2))
/* 228 */               return malformed(paramByteBuffer, i, paramCharBuffer, k, 2);
/* 229 */             arrayOfChar[(k++)] = ((char)(i1 << 6 ^ i2 ^ 0xF80));
/*     */ 
/* 233 */             i += 2;
/*     */           }
/*     */           else
/*     */           {
/*     */             int i3;
/* 234 */             if (i1 >> 4 == -2)
/*     */             {
/* 236 */               if ((j - i < 3) || (k >= m))
/* 237 */                 return xflow(paramByteBuffer, i, j, paramCharBuffer, k, 3);
/* 238 */               i2 = arrayOfByte[(i + 1)];
/* 239 */               i3 = arrayOfByte[(i + 2)];
/* 240 */               if (isMalformed3(i1, i2, i3))
/* 241 */                 return malformed(paramByteBuffer, i, paramCharBuffer, k, 3);
/* 242 */               arrayOfChar[(k++)] = ((char)(i1 << 12 ^ i2 << 6 ^ (i3 ^ 0xFFFE1F80)));
/*     */ 
/* 249 */               i += 3;
/* 250 */             } else if (i1 >> 3 == -2)
/*     */             {
/* 252 */               if ((j - i < 4) || (m - k < 2))
/* 253 */                 return xflow(paramByteBuffer, i, j, paramCharBuffer, k, 4);
/* 254 */               i2 = arrayOfByte[(i + 1)];
/* 255 */               i3 = arrayOfByte[(i + 2)];
/* 256 */               int i4 = arrayOfByte[(i + 3)];
/* 257 */               int i5 = i1 << 18 ^ i2 << 12 ^ i3 << 6 ^ (i4 ^ 0x381F80);
/*     */ 
/* 265 */               if ((isMalformed4(i2, i3, i4)) || (!Character.isSupplementaryCodePoint(i5)))
/*     */               {
/* 268 */                 return malformed(paramByteBuffer, i, paramCharBuffer, k, 4);
/*     */               }
/* 270 */               arrayOfChar[(k++)] = Character.highSurrogate(i5);
/* 271 */               arrayOfChar[(k++)] = Character.lowSurrogate(i5);
/* 272 */               i += 4;
/*     */             } else {
/* 274 */               return malformed(paramByteBuffer, i, paramCharBuffer, k, 1);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 276 */       return xflow(paramByteBuffer, i, j, paramCharBuffer, k, 0);
/*     */     }
/*     */ 
/*     */     private CoderResult decodeBufferLoop(ByteBuffer paramByteBuffer, CharBuffer paramCharBuffer)
/*     */     {
/* 282 */       int i = paramByteBuffer.position();
/* 283 */       int j = paramByteBuffer.limit();
/* 284 */       while (i < j) {
/* 285 */         int k = paramByteBuffer.get();
/* 286 */         if (k >= 0)
/*     */         {
/* 288 */           if (paramCharBuffer.remaining() < 1)
/* 289 */             return xflow(paramByteBuffer, i, 1);
/* 290 */           paramCharBuffer.put((char)k);
/* 291 */           i++;
/*     */         }
/*     */         else
/*     */         {
/*     */           int m;
/* 292 */           if (k >> 5 == -2)
/*     */           {
/* 294 */             if ((j - i < 2) || (paramCharBuffer.remaining() < 1))
/* 295 */               return xflow(paramByteBuffer, i, 2);
/* 296 */             m = paramByteBuffer.get();
/* 297 */             if (isMalformed2(k, m))
/* 298 */               return malformed(paramByteBuffer, i, 2);
/* 299 */             paramCharBuffer.put((char)(k << 6 ^ m ^ 0xF80));
/*     */ 
/* 303 */             i += 2;
/*     */           }
/*     */           else
/*     */           {
/*     */             int n;
/* 304 */             if (k >> 4 == -2)
/*     */             {
/* 306 */               if ((j - i < 3) || (paramCharBuffer.remaining() < 1))
/* 307 */                 return xflow(paramByteBuffer, i, 3);
/* 308 */               m = paramByteBuffer.get();
/* 309 */               n = paramByteBuffer.get();
/* 310 */               if (isMalformed3(k, m, n))
/* 311 */                 return malformed(paramByteBuffer, i, 3);
/* 312 */               paramCharBuffer.put((char)(k << 12 ^ m << 6 ^ (n ^ 0xFFFE1F80)));
/*     */ 
/* 319 */               i += 3;
/* 320 */             } else if (k >> 3 == -2)
/*     */             {
/* 322 */               if ((j - i < 4) || (paramCharBuffer.remaining() < 2))
/* 323 */                 return xflow(paramByteBuffer, i, 4);
/* 324 */               m = paramByteBuffer.get();
/* 325 */               n = paramByteBuffer.get();
/* 326 */               int i1 = paramByteBuffer.get();
/* 327 */               int i2 = k << 18 ^ m << 12 ^ n << 6 ^ (i1 ^ 0x381F80);
/*     */ 
/* 335 */               if ((isMalformed4(m, n, i1)) || (!Character.isSupplementaryCodePoint(i2)))
/*     */               {
/* 338 */                 return malformed(paramByteBuffer, i, 4);
/*     */               }
/* 340 */               paramCharBuffer.put(Character.highSurrogate(i2));
/* 341 */               paramCharBuffer.put(Character.lowSurrogate(i2));
/* 342 */               i += 4;
/*     */             } else {
/* 344 */               return malformed(paramByteBuffer, i, 1);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 347 */       return xflow(paramByteBuffer, i, 0);
/*     */     }
/*     */ 
/*     */     protected CoderResult decodeLoop(ByteBuffer paramByteBuffer, CharBuffer paramCharBuffer)
/*     */     {
/* 353 */       if ((paramByteBuffer.hasArray()) && (paramCharBuffer.hasArray())) {
/* 354 */         return decodeArrayLoop(paramByteBuffer, paramCharBuffer);
/*     */       }
/* 356 */       return decodeBufferLoop(paramByteBuffer, paramCharBuffer);
/*     */     }
/*     */ 
/*     */     private static ByteBuffer getByteBuffer(ByteBuffer paramByteBuffer, byte[] paramArrayOfByte, int paramInt)
/*     */     {
/* 361 */       if (paramByteBuffer == null)
/* 362 */         paramByteBuffer = ByteBuffer.wrap(paramArrayOfByte);
/* 363 */       paramByteBuffer.position(paramInt);
/* 364 */       return paramByteBuffer;
/*     */     }
/*     */ 
/*     */     public int decode(byte[] paramArrayOfByte, int paramInt1, int paramInt2, char[] paramArrayOfChar)
/*     */     {
/* 370 */       int i = paramInt1 + paramInt2;
/* 371 */       int j = 0;
/* 372 */       int k = Math.min(paramInt2, paramArrayOfChar.length);
/* 373 */       ByteBuffer localByteBuffer = null;
/*     */ 
/* 376 */       while ((j < k) && (paramArrayOfByte[paramInt1] >= 0)) {
/* 377 */         paramArrayOfChar[(j++)] = ((char)paramArrayOfByte[(paramInt1++)]);
/*     */       }
/* 379 */       while (paramInt1 < i) {
/* 380 */         int m = paramArrayOfByte[(paramInt1++)];
/* 381 */         if (m >= 0)
/*     */         {
/* 383 */           paramArrayOfChar[(j++)] = ((char)m);
/*     */         }
/*     */         else
/*     */         {
/*     */           int n;
/* 384 */           if (m >> 5 == -2)
/*     */           {
/* 386 */             if (paramInt1 < i) {
/* 387 */               n = paramArrayOfByte[(paramInt1++)];
/* 388 */               if (isMalformed2(m, n)) {
/* 389 */                 if (malformedInputAction() != CodingErrorAction.REPLACE)
/* 390 */                   return -1;
/* 391 */                 paramArrayOfChar[(j++)] = replacement().charAt(0);
/* 392 */                 paramInt1--;
/*     */               } else {
/* 394 */                 paramArrayOfChar[(j++)] = ((char)(m << 6 ^ n ^ 0xF80));
/*     */               }
/*     */ 
/*     */             }
/*     */             else
/*     */             {
/* 400 */               if (malformedInputAction() != CodingErrorAction.REPLACE)
/* 401 */                 return -1;
/* 402 */               paramArrayOfChar[(j++)] = replacement().charAt(0);
/* 403 */               return j;
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/*     */             int i1;
/* 404 */             if (m >> 4 == -2)
/*     */             {
/* 406 */               if (paramInt1 + 1 < i) {
/* 407 */                 n = paramArrayOfByte[(paramInt1++)];
/* 408 */                 i1 = paramArrayOfByte[(paramInt1++)];
/* 409 */                 if (isMalformed3(m, n, i1)) {
/* 410 */                   if (malformedInputAction() != CodingErrorAction.REPLACE)
/* 411 */                     return -1;
/* 412 */                   paramArrayOfChar[(j++)] = replacement().charAt(0);
/* 413 */                   paramInt1 -= 3;
/* 414 */                   localByteBuffer = getByteBuffer(localByteBuffer, paramArrayOfByte, paramInt1);
/* 415 */                   paramInt1 += malformedN(localByteBuffer, 3).length();
/*     */                 } else {
/* 417 */                   paramArrayOfChar[(j++)] = ((char)(m << 12 ^ n << 6 ^ (i1 ^ 0xFFFE1F80)));
/*     */                 }
/*     */ 
/*     */               }
/*     */               else
/*     */               {
/* 426 */                 if (malformedInputAction() != CodingErrorAction.REPLACE)
/* 427 */                   return -1;
/* 428 */                 paramArrayOfChar[(j++)] = replacement().charAt(0);
/* 429 */                 return j;
/*     */               } } else if (m >> 3 == -2)
/*     */             {
/* 432 */               if (paramInt1 + 2 < i) {
/* 433 */                 n = paramArrayOfByte[(paramInt1++)];
/* 434 */                 i1 = paramArrayOfByte[(paramInt1++)];
/* 435 */                 int i2 = paramArrayOfByte[(paramInt1++)];
/* 436 */                 int i3 = m << 18 ^ n << 12 ^ i1 << 6 ^ (i2 ^ 0x381F80);
/*     */ 
/* 444 */                 if ((isMalformed4(n, i1, i2)) || (!Character.isSupplementaryCodePoint(i3)))
/*     */                 {
/* 447 */                   if (malformedInputAction() != CodingErrorAction.REPLACE)
/* 448 */                     return -1;
/* 449 */                   paramArrayOfChar[(j++)] = replacement().charAt(0);
/* 450 */                   paramInt1 -= 4;
/* 451 */                   localByteBuffer = getByteBuffer(localByteBuffer, paramArrayOfByte, paramInt1);
/* 452 */                   paramInt1 += malformedN(localByteBuffer, 4).length();
/*     */                 } else {
/* 454 */                   paramArrayOfChar[(j++)] = Character.highSurrogate(i3);
/* 455 */                   paramArrayOfChar[(j++)] = Character.lowSurrogate(i3);
/*     */                 }
/*     */               }
/*     */               else {
/* 459 */                 if (malformedInputAction() != CodingErrorAction.REPLACE)
/* 460 */                   return -1;
/* 461 */                 paramArrayOfChar[(j++)] = replacement().charAt(0);
/* 462 */                 return j;
/*     */               }
/*     */             } else { if (malformedInputAction() != CodingErrorAction.REPLACE)
/* 465 */                 return -1;
/* 466 */               paramArrayOfChar[(j++)] = replacement().charAt(0);
/* 467 */               paramInt1--;
/* 468 */               localByteBuffer = getByteBuffer(localByteBuffer, paramArrayOfByte, paramInt1);
/* 469 */               CoderResult localCoderResult = malformedN(localByteBuffer, 1);
/* 470 */               if (!localCoderResult.isError())
/*     */               {
/* 473 */                 return j;
/*     */               }
/* 475 */               paramInt1 += localCoderResult.length(); } 
/*     */           }
/*     */         }
/*     */       }
/* 478 */       return j;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class Encoder extends CharsetEncoder implements ArrayEncoder {
/*     */     private Surrogate.Parser sgp;
/*     */ 
/*     */     private Encoder(Charset paramCharset) {
/* 486 */       super(1.1F, 3.0F);
/*     */     }
/*     */ 
/*     */     public boolean canEncode(char paramChar) {
/* 490 */       return !Character.isSurrogate(paramChar);
/*     */     }
/*     */ 
/*     */     public boolean isLegalReplacement(byte[] paramArrayOfByte) {
/* 494 */       return ((paramArrayOfByte.length == 1) && (paramArrayOfByte[0] >= 0)) || (super.isLegalReplacement(paramArrayOfByte));
/*     */     }
/*     */ 
/*     */     private static CoderResult overflow(CharBuffer paramCharBuffer, int paramInt1, ByteBuffer paramByteBuffer, int paramInt2)
/*     */     {
/* 500 */       UTF_8.updatePositions(paramCharBuffer, paramInt1, paramByteBuffer, paramInt2);
/* 501 */       return CoderResult.OVERFLOW;
/*     */     }
/*     */ 
/*     */     private static CoderResult overflow(CharBuffer paramCharBuffer, int paramInt) {
/* 505 */       paramCharBuffer.position(paramInt);
/* 506 */       return CoderResult.OVERFLOW;
/*     */     }
/*     */ 
/*     */     private CoderResult encodeArrayLoop(CharBuffer paramCharBuffer, ByteBuffer paramByteBuffer)
/*     */     {
/* 513 */       char[] arrayOfChar = paramCharBuffer.array();
/* 514 */       int i = paramCharBuffer.arrayOffset() + paramCharBuffer.position();
/* 515 */       int j = paramCharBuffer.arrayOffset() + paramCharBuffer.limit();
/*     */ 
/* 517 */       byte[] arrayOfByte = paramByteBuffer.array();
/* 518 */       int k = paramByteBuffer.arrayOffset() + paramByteBuffer.position();
/* 519 */       int m = paramByteBuffer.arrayOffset() + paramByteBuffer.limit();
/* 520 */       int n = k + Math.min(j - i, m - k);
/*     */ 
/* 523 */       while ((k < n) && (arrayOfChar[i] < ''))
/* 524 */         arrayOfByte[(k++)] = ((byte)arrayOfChar[(i++)]);
/* 525 */       while (i < j) {
/* 526 */         int i1 = arrayOfChar[i];
/* 527 */         if (i1 < 128)
/*     */         {
/* 529 */           if (k >= m)
/* 530 */             return overflow(paramCharBuffer, i, paramByteBuffer, k);
/* 531 */           arrayOfByte[(k++)] = ((byte)i1);
/* 532 */         } else if (i1 < 2048)
/*     */         {
/* 534 */           if (m - k < 2)
/* 535 */             return overflow(paramCharBuffer, i, paramByteBuffer, k);
/* 536 */           arrayOfByte[(k++)] = ((byte)(0xC0 | i1 >> 6));
/* 537 */           arrayOfByte[(k++)] = ((byte)(0x80 | i1 & 0x3F));
/* 538 */         } else if (Character.isSurrogate(i1))
/*     */         {
/* 540 */           if (this.sgp == null)
/* 541 */             this.sgp = new Surrogate.Parser();
/* 542 */           int i2 = this.sgp.parse(i1, arrayOfChar, i, j);
/* 543 */           if (i2 < 0) {
/* 544 */             UTF_8.updatePositions(paramCharBuffer, i, paramByteBuffer, k);
/* 545 */             return this.sgp.error();
/*     */           }
/* 547 */           if (m - k < 4)
/* 548 */             return overflow(paramCharBuffer, i, paramByteBuffer, k);
/* 549 */           arrayOfByte[(k++)] = ((byte)(0xF0 | i2 >> 18));
/* 550 */           arrayOfByte[(k++)] = ((byte)(0x80 | i2 >> 12 & 0x3F));
/* 551 */           arrayOfByte[(k++)] = ((byte)(0x80 | i2 >> 6 & 0x3F));
/* 552 */           arrayOfByte[(k++)] = ((byte)(0x80 | i2 & 0x3F));
/* 553 */           i++;
/*     */         }
/*     */         else {
/* 556 */           if (m - k < 3)
/* 557 */             return overflow(paramCharBuffer, i, paramByteBuffer, k);
/* 558 */           arrayOfByte[(k++)] = ((byte)(0xE0 | i1 >> 12));
/* 559 */           arrayOfByte[(k++)] = ((byte)(0x80 | i1 >> 6 & 0x3F));
/* 560 */           arrayOfByte[(k++)] = ((byte)(0x80 | i1 & 0x3F));
/*     */         }
/* 562 */         i++;
/*     */       }
/* 564 */       UTF_8.updatePositions(paramCharBuffer, i, paramByteBuffer, k);
/* 565 */       return CoderResult.UNDERFLOW;
/*     */     }
/*     */ 
/*     */     private CoderResult encodeBufferLoop(CharBuffer paramCharBuffer, ByteBuffer paramByteBuffer)
/*     */     {
/* 571 */       int i = paramCharBuffer.position();
/* 572 */       while (paramCharBuffer.hasRemaining()) {
/* 573 */         int j = paramCharBuffer.get();
/* 574 */         if (j < 128)
/*     */         {
/* 576 */           if (!paramByteBuffer.hasRemaining())
/* 577 */             return overflow(paramCharBuffer, i);
/* 578 */           paramByteBuffer.put((byte)j);
/* 579 */         } else if (j < 2048)
/*     */         {
/* 581 */           if (paramByteBuffer.remaining() < 2)
/* 582 */             return overflow(paramCharBuffer, i);
/* 583 */           paramByteBuffer.put((byte)(0xC0 | j >> 6));
/* 584 */           paramByteBuffer.put((byte)(0x80 | j & 0x3F));
/* 585 */         } else if (Character.isSurrogate(j))
/*     */         {
/* 587 */           if (this.sgp == null)
/* 588 */             this.sgp = new Surrogate.Parser();
/* 589 */           int k = this.sgp.parse(j, paramCharBuffer);
/* 590 */           if (k < 0) {
/* 591 */             paramCharBuffer.position(i);
/* 592 */             return this.sgp.error();
/*     */           }
/* 594 */           if (paramByteBuffer.remaining() < 4)
/* 595 */             return overflow(paramCharBuffer, i);
/* 596 */           paramByteBuffer.put((byte)(0xF0 | k >> 18));
/* 597 */           paramByteBuffer.put((byte)(0x80 | k >> 12 & 0x3F));
/* 598 */           paramByteBuffer.put((byte)(0x80 | k >> 6 & 0x3F));
/* 599 */           paramByteBuffer.put((byte)(0x80 | k & 0x3F));
/* 600 */           i++;
/*     */         }
/*     */         else {
/* 603 */           if (paramByteBuffer.remaining() < 3)
/* 604 */             return overflow(paramCharBuffer, i);
/* 605 */           paramByteBuffer.put((byte)(0xE0 | j >> 12));
/* 606 */           paramByteBuffer.put((byte)(0x80 | j >> 6 & 0x3F));
/* 607 */           paramByteBuffer.put((byte)(0x80 | j & 0x3F));
/*     */         }
/* 609 */         i++;
/*     */       }
/* 611 */       paramCharBuffer.position(i);
/* 612 */       return CoderResult.UNDERFLOW;
/*     */     }
/*     */ 
/*     */     protected final CoderResult encodeLoop(CharBuffer paramCharBuffer, ByteBuffer paramByteBuffer)
/*     */     {
/* 618 */       if ((paramCharBuffer.hasArray()) && (paramByteBuffer.hasArray())) {
/* 619 */         return encodeArrayLoop(paramCharBuffer, paramByteBuffer);
/*     */       }
/* 621 */       return encodeBufferLoop(paramCharBuffer, paramByteBuffer);
/*     */     }
/*     */ 
/*     */     public int encode(char[] paramArrayOfChar, int paramInt1, int paramInt2, byte[] paramArrayOfByte)
/*     */     {
/* 627 */       int i = paramInt1 + paramInt2;
/* 628 */       int j = 0;
/* 629 */       int k = j + Math.min(paramInt2, paramArrayOfByte.length);
/*     */ 
/* 632 */       while ((j < k) && (paramArrayOfChar[paramInt1] < '')) {
/* 633 */         paramArrayOfByte[(j++)] = ((byte)paramArrayOfChar[(paramInt1++)]);
/*     */       }
/* 635 */       while (paramInt1 < i) {
/* 636 */         int m = paramArrayOfChar[(paramInt1++)];
/* 637 */         if (m < 128)
/*     */         {
/* 639 */           paramArrayOfByte[(j++)] = ((byte)m);
/* 640 */         } else if (m < 2048)
/*     */         {
/* 642 */           paramArrayOfByte[(j++)] = ((byte)(0xC0 | m >> 6));
/* 643 */           paramArrayOfByte[(j++)] = ((byte)(0x80 | m & 0x3F));
/* 644 */         } else if (Character.isSurrogate(m)) {
/* 645 */           if (this.sgp == null)
/* 646 */             this.sgp = new Surrogate.Parser();
/* 647 */           int n = this.sgp.parse(m, paramArrayOfChar, paramInt1 - 1, i);
/* 648 */           if (n < 0) {
/* 649 */             if (malformedInputAction() != CodingErrorAction.REPLACE)
/* 650 */               return -1;
/* 651 */             paramArrayOfByte[(j++)] = replacement()[0];
/*     */           } else {
/* 653 */             paramArrayOfByte[(j++)] = ((byte)(0xF0 | n >> 18));
/* 654 */             paramArrayOfByte[(j++)] = ((byte)(0x80 | n >> 12 & 0x3F));
/* 655 */             paramArrayOfByte[(j++)] = ((byte)(0x80 | n >> 6 & 0x3F));
/* 656 */             paramArrayOfByte[(j++)] = ((byte)(0x80 | n & 0x3F));
/* 657 */             paramInt1++;
/*     */           }
/*     */         }
/*     */         else {
/* 661 */           paramArrayOfByte[(j++)] = ((byte)(0xE0 | m >> 12));
/* 662 */           paramArrayOfByte[(j++)] = ((byte)(0x80 | m >> 6 & 0x3F));
/* 663 */           paramArrayOfByte[(j++)] = ((byte)(0x80 | m & 0x3F));
/*     */         }
/*     */       }
/* 666 */       return j;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.cs.UTF_8
 * JD-Core Version:    0.6.2
 */