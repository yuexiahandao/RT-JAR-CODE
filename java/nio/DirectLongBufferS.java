/*     */ package java.nio;
/*     */ 
/*     */ import sun.misc.Cleaner;
/*     */ import sun.misc.Unsafe;
/*     */ import sun.nio.ch.DirectBuffer;
/*     */ 
/*     */ class DirectLongBufferS extends LongBuffer
/*     */   implements DirectBuffer
/*     */ {
/*  49 */   protected static final Unsafe unsafe = Bits.unsafe();
/*     */ 
/*  52 */   private static final long arrayBaseOffset = unsafe.arrayBaseOffset([J.class);
/*     */ 
/*  55 */   protected static final boolean unaligned = Bits.unaligned();
/*     */   private final Object att;
/*     */ 
/*     */   public Object attachment()
/*     */   {
/*  67 */     return this.att;
/*     */   }
/*     */ 
/*     */   public Cleaner cleaner()
/*     */   {
/* 107 */     return null;
/*     */   }
/*     */ 
/*     */   DirectLongBufferS(DirectBuffer paramDirectBuffer, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/* 195 */     super(paramInt1, paramInt2, paramInt3, paramInt4);
/* 196 */     this.address = (paramDirectBuffer.address() + paramInt5);
/*     */ 
/* 200 */     this.att = paramDirectBuffer;
/*     */   }
/*     */ 
/*     */   public LongBuffer slice()
/*     */   {
/* 207 */     int i = position();
/* 208 */     int j = limit();
/* 209 */     assert (i <= j);
/* 210 */     int k = i <= j ? j - i : 0;
/* 211 */     int m = i << 3;
/* 212 */     assert (m >= 0);
/* 213 */     return new DirectLongBufferS(this, -1, 0, k, k, m);
/*     */   }
/*     */ 
/*     */   public LongBuffer duplicate() {
/* 217 */     return new DirectLongBufferS(this, markValue(), position(), limit(), capacity(), 0);
/*     */   }
/*     */ 
/*     */   public LongBuffer asReadOnlyBuffer()
/*     */   {
/* 227 */     return new DirectLongBufferRS(this, markValue(), position(), limit(), capacity(), 0);
/*     */   }
/*     */ 
/*     */   public long address()
/*     */   {
/* 241 */     return this.address;
/*     */   }
/*     */ 
/*     */   private long ix(int paramInt) {
/* 245 */     return this.address + (paramInt << 3);
/*     */   }
/*     */ 
/*     */   public long get() {
/* 249 */     return Bits.swap(unsafe.getLong(ix(nextGetIndex())));
/*     */   }
/*     */ 
/*     */   public long get(int paramInt) {
/* 253 */     return Bits.swap(unsafe.getLong(ix(checkIndex(paramInt))));
/*     */   }
/*     */ 
/*     */   public LongBuffer get(long[] paramArrayOfLong, int paramInt1, int paramInt2)
/*     */   {
/* 258 */     if (paramInt2 << 3 > 6) {
/* 259 */       checkBounds(paramInt1, paramInt2, paramArrayOfLong.length);
/* 260 */       int i = position();
/* 261 */       int j = limit();
/* 262 */       assert (i <= j);
/* 263 */       int k = i <= j ? j - i : 0;
/* 264 */       if (paramInt2 > k) {
/* 265 */         throw new BufferUnderflowException();
/*     */       }
/*     */ 
/* 268 */       if (order() != ByteOrder.nativeOrder()) {
/* 269 */         Bits.copyToLongArray(ix(i), paramArrayOfLong, paramInt1 << 3, paramInt2 << 3);
/*     */       }
/*     */       else
/*     */       {
/* 274 */         Bits.copyToArray(ix(i), paramArrayOfLong, arrayBaseOffset, paramInt1 << 3, paramInt2 << 3);
/*     */       }
/*     */ 
/* 277 */       position(i + paramInt2);
/*     */     } else {
/* 279 */       super.get(paramArrayOfLong, paramInt1, paramInt2);
/*     */     }
/* 281 */     return this;
/*     */   }
/*     */ 
/*     */   public LongBuffer put(long paramLong)
/*     */   {
/* 291 */     unsafe.putLong(ix(nextPutIndex()), Bits.swap(paramLong));
/* 292 */     return this;
/*     */   }
/*     */ 
/*     */   public LongBuffer put(int paramInt, long paramLong)
/*     */   {
/* 300 */     unsafe.putLong(ix(checkIndex(paramInt)), Bits.swap(paramLong));
/* 301 */     return this;
/*     */   }
/*     */ 
/*     */   public LongBuffer put(LongBuffer paramLongBuffer)
/*     */   {
/*     */     int j;
/*     */     int k;
/* 309 */     if ((paramLongBuffer instanceof DirectLongBufferS)) {
/* 310 */       if (paramLongBuffer == this)
/* 311 */         throw new IllegalArgumentException();
/* 312 */       DirectLongBufferS localDirectLongBufferS = (DirectLongBufferS)paramLongBuffer;
/*     */ 
/* 314 */       j = localDirectLongBufferS.position();
/* 315 */       k = localDirectLongBufferS.limit();
/* 316 */       assert (j <= k);
/* 317 */       int m = j <= k ? k - j : 0;
/*     */ 
/* 319 */       int n = position();
/* 320 */       int i1 = limit();
/* 321 */       assert (n <= i1);
/* 322 */       int i2 = n <= i1 ? i1 - n : 0;
/*     */ 
/* 324 */       if (m > i2)
/* 325 */         throw new BufferOverflowException();
/* 326 */       unsafe.copyMemory(localDirectLongBufferS.ix(j), ix(n), m << 3);
/* 327 */       localDirectLongBufferS.position(j + m);
/* 328 */       position(n + m);
/* 329 */     } else if (paramLongBuffer.hb != null)
/*     */     {
/* 331 */       int i = paramLongBuffer.position();
/* 332 */       j = paramLongBuffer.limit();
/* 333 */       assert (i <= j);
/* 334 */       k = i <= j ? j - i : 0;
/*     */ 
/* 336 */       put(paramLongBuffer.hb, paramLongBuffer.offset + i, k);
/* 337 */       paramLongBuffer.position(i + k);
/*     */     }
/*     */     else {
/* 340 */       super.put(paramLongBuffer);
/*     */     }
/* 342 */     return this;
/*     */   }
/*     */ 
/*     */   public LongBuffer put(long[] paramArrayOfLong, int paramInt1, int paramInt2)
/*     */   {
/* 350 */     if (paramInt2 << 3 > 6) {
/* 351 */       checkBounds(paramInt1, paramInt2, paramArrayOfLong.length);
/* 352 */       int i = position();
/* 353 */       int j = limit();
/* 354 */       assert (i <= j);
/* 355 */       int k = i <= j ? j - i : 0;
/* 356 */       if (paramInt2 > k) {
/* 357 */         throw new BufferOverflowException();
/*     */       }
/*     */ 
/* 360 */       if (order() != ByteOrder.nativeOrder()) {
/* 361 */         Bits.copyFromLongArray(paramArrayOfLong, paramInt1 << 3, ix(i), paramInt2 << 3);
/*     */       }
/*     */       else
/*     */       {
/* 365 */         Bits.copyFromArray(paramArrayOfLong, arrayBaseOffset, paramInt1 << 3, ix(i), paramInt2 << 3);
/*     */       }
/* 367 */       position(i + paramInt2);
/*     */     } else {
/* 369 */       super.put(paramArrayOfLong, paramInt1, paramInt2);
/*     */     }
/* 371 */     return this;
/*     */   }
/*     */ 
/*     */   public LongBuffer compact()
/*     */   {
/* 379 */     int i = position();
/* 380 */     int j = limit();
/* 381 */     assert (i <= j);
/* 382 */     int k = i <= j ? j - i : 0;
/*     */ 
/* 384 */     unsafe.copyMemory(ix(i), ix(0), k << 3);
/* 385 */     position(k);
/* 386 */     limit(capacity());
/* 387 */     discardMark();
/* 388 */     return this;
/*     */   }
/*     */ 
/*     */   public boolean isDirect()
/*     */   {
/* 395 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isReadOnly() {
/* 399 */     return false;
/*     */   }
/*     */ 
/*     */   public ByteOrder order()
/*     */   {
/* 450 */     return ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.DirectLongBufferS
 * JD-Core Version:    0.6.2
 */