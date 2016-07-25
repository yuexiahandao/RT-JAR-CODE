/*     */ package com.sun.jmx.snmp;
/*     */ 
/*     */ public class BerEncoder
/*     */ {
/*     */   public static final int BooleanTag = 1;
/*     */   public static final int IntegerTag = 2;
/*     */   public static final int OctetStringTag = 4;
/*     */   public static final int NullTag = 5;
/*     */   public static final int OidTag = 6;
/*     */   public static final int SequenceTag = 48;
/*     */   protected final byte[] bytes;
/* 466 */   protected int start = -1;
/*     */ 
/* 474 */   protected final int[] stackBuf = new int['È'];
/* 475 */   protected int stackTop = 0;
/*     */ 
/*     */   public BerEncoder(byte[] paramArrayOfByte)
/*     */   {
/*  55 */     this.bytes = paramArrayOfByte;
/*  56 */     this.start = paramArrayOfByte.length;
/*  57 */     this.stackTop = 0;
/*     */   }
/*     */ 
/*     */   public int trim()
/*     */   {
/*  75 */     int i = this.bytes.length - this.start;
/*     */ 
/*  80 */     if (i > 0) {
/*  81 */       System.arraycopy(this.bytes, this.start, this.bytes, 0, i);
/*     */     }
/*  83 */     this.start = this.bytes.length;
/*  84 */     this.stackTop = 0;
/*     */ 
/*  86 */     return i;
/*     */   }
/*     */ 
/*     */   public void putInteger(int paramInt)
/*     */   {
/*  96 */     putInteger(paramInt, 2);
/*     */   }
/*     */ 
/*     */   public void putInteger(int paramInt1, int paramInt2)
/*     */   {
/* 108 */     putIntegerValue(paramInt1);
/* 109 */     putTag(paramInt2);
/*     */   }
/*     */ 
/*     */   public void putInteger(long paramLong)
/*     */   {
/* 121 */     putInteger(paramLong, 2);
/*     */   }
/*     */ 
/*     */   public void putInteger(long paramLong, int paramInt)
/*     */   {
/* 133 */     putIntegerValue(paramLong);
/* 134 */     putTag(paramInt);
/*     */   }
/*     */ 
/*     */   public void putOctetString(byte[] paramArrayOfByte)
/*     */   {
/* 146 */     putOctetString(paramArrayOfByte, 4);
/*     */   }
/*     */ 
/*     */   public void putOctetString(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 158 */     putStringValue(paramArrayOfByte);
/* 159 */     putTag(paramInt);
/*     */   }
/*     */ 
/*     */   public void putOid(long[] paramArrayOfLong)
/*     */   {
/* 170 */     putOid(paramArrayOfLong, 6);
/*     */   }
/*     */ 
/*     */   public void putOid(long[] paramArrayOfLong, int paramInt)
/*     */   {
/* 182 */     putOidValue(paramArrayOfLong);
/* 183 */     putTag(paramInt);
/*     */   }
/*     */ 
/*     */   public void putNull()
/*     */   {
/* 192 */     putNull(5);
/*     */   }
/*     */ 
/*     */   public void putNull(int paramInt)
/*     */   {
/* 203 */     putLength(0);
/* 204 */     putTag(paramInt);
/*     */   }
/*     */ 
/*     */   public void putAny(byte[] paramArrayOfByte)
/*     */   {
/* 217 */     putAny(paramArrayOfByte, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public void putAny(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 229 */     System.arraycopy(paramArrayOfByte, 0, this.bytes, this.start - paramInt, paramInt);
/* 230 */     this.start -= paramInt;
/*     */   }
/*     */ 
/*     */   public void openSequence()
/*     */   {
/* 243 */     this.stackBuf[(this.stackTop++)] = this.start;
/*     */   }
/*     */ 
/*     */   public void closeSequence()
/*     */   {
/* 253 */     closeSequence(48);
/*     */   }
/*     */ 
/*     */   public void closeSequence(int paramInt)
/*     */   {
/* 262 */     int i = this.stackBuf[(--this.stackTop)];
/* 263 */     putLength(i - this.start);
/* 264 */     putTag(paramInt);
/*     */   }
/*     */ 
/*     */   protected final void putTag(int paramInt)
/*     */   {
/* 292 */     if (paramInt < 256) {
/* 293 */       this.bytes[(--this.start)] = ((byte)paramInt);
/*     */     }
/*     */     else
/* 296 */       while (paramInt != 0) {
/* 297 */         this.bytes[(--this.start)] = ((byte)(paramInt & 0x7F));
/* 298 */         paramInt <<= 7;
/*     */       }
/*     */   }
/*     */ 
/*     */   protected final void putLength(int paramInt)
/*     */   {
/* 311 */     if (paramInt < 0) {
/* 312 */       throw new IllegalArgumentException();
/*     */     }
/* 314 */     if (paramInt < 128) {
/* 315 */       this.bytes[(--this.start)] = ((byte)paramInt);
/*     */     }
/* 317 */     else if (paramInt < 256) {
/* 318 */       this.bytes[(--this.start)] = ((byte)paramInt);
/* 319 */       this.bytes[(--this.start)] = -127;
/*     */     }
/* 321 */     else if (paramInt < 65536) {
/* 322 */       this.bytes[(--this.start)] = ((byte)paramInt);
/* 323 */       this.bytes[(--this.start)] = ((byte)(paramInt >> 8));
/* 324 */       this.bytes[(--this.start)] = -126;
/*     */     }
/* 326 */     else if (paramInt < 16777126) {
/* 327 */       this.bytes[(--this.start)] = ((byte)paramInt);
/* 328 */       this.bytes[(--this.start)] = ((byte)(paramInt >> 8));
/* 329 */       this.bytes[(--this.start)] = ((byte)(paramInt >> 16));
/* 330 */       this.bytes[(--this.start)] = -125;
/*     */     }
/*     */     else {
/* 333 */       this.bytes[(--this.start)] = ((byte)paramInt);
/* 334 */       this.bytes[(--this.start)] = ((byte)(paramInt >> 8));
/* 335 */       this.bytes[(--this.start)] = ((byte)(paramInt >> 16));
/* 336 */       this.bytes[(--this.start)] = ((byte)(paramInt >> 24));
/* 337 */       this.bytes[(--this.start)] = -124;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final void putIntegerValue(int paramInt)
/*     */   {
/* 349 */     int i = this.start;
/* 350 */     int j = 2139095040;
/* 351 */     int k = 4;
/* 352 */     if (paramInt < 0) {
/* 353 */       while (((j & paramInt) == j) && (k > 1)) {
/* 354 */         j >>= 8;
/* 355 */         k--;
/*     */       }
/*     */     }
/*     */ 
/* 359 */     while (((j & paramInt) == 0) && (k > 1)) {
/* 360 */       j >>= 8;
/* 361 */       k--;
/*     */     }
/*     */ 
/* 364 */     for (int m = 0; m < k; m++) {
/* 365 */       this.bytes[(--this.start)] = ((byte)paramInt);
/* 366 */       paramInt >>= 8;
/*     */     }
/* 368 */     putLength(i - this.start);
/*     */   }
/*     */ 
/*     */   protected final void putIntegerValue(long paramLong)
/*     */   {
/* 379 */     int i = this.start;
/* 380 */     long l = 9187343239835811840L;
/* 381 */     int j = 8;
/* 382 */     if (paramLong < 0L) {
/* 383 */       while (((l & paramLong) == l) && (j > 1)) {
/* 384 */         l >>= 8;
/* 385 */         j--;
/*     */       }
/*     */     }
/*     */ 
/* 389 */     while (((l & paramLong) == 0L) && (j > 1)) {
/* 390 */       l >>= 8;
/* 391 */       j--;
/*     */     }
/*     */ 
/* 394 */     for (int k = 0; k < j; k++) {
/* 395 */       this.bytes[(--this.start)] = ((byte)(int)paramLong);
/* 396 */       paramLong >>= 8;
/*     */     }
/* 398 */     putLength(i - this.start);
/*     */   }
/*     */ 
/*     */   protected final void putStringValue(byte[] paramArrayOfByte)
/*     */   {
/* 409 */     int i = paramArrayOfByte.length;
/* 410 */     System.arraycopy(paramArrayOfByte, 0, this.bytes, this.start - i, i);
/* 411 */     this.start -= i;
/*     */ 
/* 415 */     putLength(i);
/*     */   }
/*     */ 
/*     */   protected final void putOidValue(long[] paramArrayOfLong)
/*     */   {
/* 427 */     int i = this.start;
/* 428 */     int j = paramArrayOfLong.length;
/*     */ 
/* 431 */     if ((j < 2) || (paramArrayOfLong[0] > 2L) || (paramArrayOfLong[1] >= 40L)) {
/* 432 */       throw new IllegalArgumentException();
/*     */     }
/* 434 */     for (int k = j - 1; k >= 2; k--) {
/* 435 */       long l = paramArrayOfLong[k];
/* 436 */       if (l < 0L) {
/* 437 */         throw new IllegalArgumentException();
/*     */       }
/* 439 */       if (l < 128L) {
/* 440 */         this.bytes[(--this.start)] = ((byte)(int)l);
/*     */       }
/*     */       else {
/* 443 */         this.bytes[(--this.start)] = ((byte)(int)(l & 0x7F));
/* 444 */         l >>= 7;
/* 445 */         while (l != 0L) {
/* 446 */           this.bytes[(--this.start)] = ((byte)(int)(l | 0x80));
/* 447 */           l >>= 7;
/*     */         }
/*     */       }
/*     */     }
/* 451 */     this.bytes[(--this.start)] = ((byte)(int)(paramArrayOfLong[0] * 40L + paramArrayOfLong[1]));
/* 452 */     putLength(i - this.start);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.BerEncoder
 * JD-Core Version:    0.6.2
 */