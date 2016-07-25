/*     */ package java.nio;
/*     */ 
/*     */ import sun.misc.Cleaner;
/*     */ import sun.misc.Unsafe;
/*     */ import sun.nio.ch.DirectBuffer;
/*     */ 
/*     */ class DirectFloatBufferU extends FloatBuffer
/*     */   implements DirectBuffer
/*     */ {
/*  49 */   protected static final Unsafe unsafe = Bits.unsafe();
/*     */ 
/*  52 */   private static final long arrayBaseOffset = unsafe.arrayBaseOffset([F.class);
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
/*     */   DirectFloatBufferU(DirectBuffer paramDirectBuffer, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/* 195 */     super(paramInt1, paramInt2, paramInt3, paramInt4);
/* 196 */     this.address = (paramDirectBuffer.address() + paramInt5);
/*     */ 
/* 200 */     this.att = paramDirectBuffer;
/*     */   }
/*     */ 
/*     */   public FloatBuffer slice()
/*     */   {
/* 207 */     int i = position();
/* 208 */     int j = limit();
/* 209 */     assert (i <= j);
/* 210 */     int k = i <= j ? j - i : 0;
/* 211 */     int m = i << 2;
/* 212 */     assert (m >= 0);
/* 213 */     return new DirectFloatBufferU(this, -1, 0, k, k, m);
/*     */   }
/*     */ 
/*     */   public FloatBuffer duplicate() {
/* 217 */     return new DirectFloatBufferU(this, markValue(), position(), limit(), capacity(), 0);
/*     */   }
/*     */ 
/*     */   public FloatBuffer asReadOnlyBuffer()
/*     */   {
/* 227 */     return new DirectFloatBufferRU(this, markValue(), position(), limit(), capacity(), 0);
/*     */   }
/*     */ 
/*     */   public long address()
/*     */   {
/* 241 */     return this.address;
/*     */   }
/*     */ 
/*     */   private long ix(int paramInt) {
/* 245 */     return this.address + (paramInt << 2);
/*     */   }
/*     */ 
/*     */   public float get() {
/* 249 */     return unsafe.getFloat(ix(nextGetIndex()));
/*     */   }
/*     */ 
/*     */   public float get(int paramInt) {
/* 253 */     return unsafe.getFloat(ix(checkIndex(paramInt)));
/*     */   }
/*     */ 
/*     */   public FloatBuffer get(float[] paramArrayOfFloat, int paramInt1, int paramInt2)
/*     */   {
/* 258 */     if (paramInt2 << 2 > 6) {
/* 259 */       checkBounds(paramInt1, paramInt2, paramArrayOfFloat.length);
/* 260 */       int i = position();
/* 261 */       int j = limit();
/* 262 */       assert (i <= j);
/* 263 */       int k = i <= j ? j - i : 0;
/* 264 */       if (paramInt2 > k) {
/* 265 */         throw new BufferUnderflowException();
/*     */       }
/*     */ 
/* 268 */       if (order() != ByteOrder.nativeOrder()) {
/* 269 */         Bits.copyToIntArray(ix(i), paramArrayOfFloat, paramInt1 << 2, paramInt2 << 2);
/*     */       }
/*     */       else
/*     */       {
/* 274 */         Bits.copyToArray(ix(i), paramArrayOfFloat, arrayBaseOffset, paramInt1 << 2, paramInt2 << 2);
/*     */       }
/*     */ 
/* 277 */       position(i + paramInt2);
/*     */     } else {
/* 279 */       super.get(paramArrayOfFloat, paramInt1, paramInt2);
/*     */     }
/* 281 */     return this;
/*     */   }
/*     */ 
/*     */   public FloatBuffer put(float paramFloat)
/*     */   {
/* 291 */     unsafe.putFloat(ix(nextPutIndex()), paramFloat);
/* 292 */     return this;
/*     */   }
/*     */ 
/*     */   public FloatBuffer put(int paramInt, float paramFloat)
/*     */   {
/* 300 */     unsafe.putFloat(ix(checkIndex(paramInt)), paramFloat);
/* 301 */     return this;
/*     */   }
/*     */ 
/*     */   public FloatBuffer put(FloatBuffer paramFloatBuffer)
/*     */   {
/*     */     int j;
/*     */     int k;
/* 309 */     if ((paramFloatBuffer instanceof DirectFloatBufferU)) {
/* 310 */       if (paramFloatBuffer == this)
/* 311 */         throw new IllegalArgumentException();
/* 312 */       DirectFloatBufferU localDirectFloatBufferU = (DirectFloatBufferU)paramFloatBuffer;
/*     */ 
/* 314 */       j = localDirectFloatBufferU.position();
/* 315 */       k = localDirectFloatBufferU.limit();
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
/* 326 */       unsafe.copyMemory(localDirectFloatBufferU.ix(j), ix(n), m << 2);
/* 327 */       localDirectFloatBufferU.position(j + m);
/* 328 */       position(n + m);
/* 329 */     } else if (paramFloatBuffer.hb != null)
/*     */     {
/* 331 */       int i = paramFloatBuffer.position();
/* 332 */       j = paramFloatBuffer.limit();
/* 333 */       assert (i <= j);
/* 334 */       k = i <= j ? j - i : 0;
/*     */ 
/* 336 */       put(paramFloatBuffer.hb, paramFloatBuffer.offset + i, k);
/* 337 */       paramFloatBuffer.position(i + k);
/*     */     }
/*     */     else {
/* 340 */       super.put(paramFloatBuffer);
/*     */     }
/* 342 */     return this;
/*     */   }
/*     */ 
/*     */   public FloatBuffer put(float[] paramArrayOfFloat, int paramInt1, int paramInt2)
/*     */   {
/* 350 */     if (paramInt2 << 2 > 6) {
/* 351 */       checkBounds(paramInt1, paramInt2, paramArrayOfFloat.length);
/* 352 */       int i = position();
/* 353 */       int j = limit();
/* 354 */       assert (i <= j);
/* 355 */       int k = i <= j ? j - i : 0;
/* 356 */       if (paramInt2 > k) {
/* 357 */         throw new BufferOverflowException();
/*     */       }
/*     */ 
/* 360 */       if (order() != ByteOrder.nativeOrder()) {
/* 361 */         Bits.copyFromIntArray(paramArrayOfFloat, paramInt1 << 2, ix(i), paramInt2 << 2);
/*     */       }
/*     */       else
/*     */       {
/* 365 */         Bits.copyFromArray(paramArrayOfFloat, arrayBaseOffset, paramInt1 << 2, ix(i), paramInt2 << 2);
/*     */       }
/* 367 */       position(i + paramInt2);
/*     */     } else {
/* 369 */       super.put(paramArrayOfFloat, paramInt1, paramInt2);
/*     */     }
/* 371 */     return this;
/*     */   }
/*     */ 
/*     */   public FloatBuffer compact()
/*     */   {
/* 379 */     int i = position();
/* 380 */     int j = limit();
/* 381 */     assert (i <= j);
/* 382 */     int k = i <= j ? j - i : 0;
/*     */ 
/* 384 */     unsafe.copyMemory(ix(i), ix(0), k << 2);
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
/* 454 */     return ByteOrder.nativeOrder() != ByteOrder.BIG_ENDIAN ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.DirectFloatBufferU
 * JD-Core Version:    0.6.2
 */