/*     */ package java.nio;
/*     */ 
/*     */ import sun.misc.Cleaner;
/*     */ import sun.misc.Unsafe;
/*     */ import sun.nio.ch.DirectBuffer;
/*     */ 
/*     */ class DirectDoubleBufferS extends DoubleBuffer
/*     */   implements DirectBuffer
/*     */ {
/*  49 */   protected static final Unsafe unsafe = Bits.unsafe();
/*     */ 
/*  52 */   private static final long arrayBaseOffset = unsafe.arrayBaseOffset([D.class);
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
/*     */   DirectDoubleBufferS(DirectBuffer paramDirectBuffer, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/* 195 */     super(paramInt1, paramInt2, paramInt3, paramInt4);
/* 196 */     this.address = (paramDirectBuffer.address() + paramInt5);
/*     */ 
/* 200 */     this.att = paramDirectBuffer;
/*     */   }
/*     */ 
/*     */   public DoubleBuffer slice()
/*     */   {
/* 207 */     int i = position();
/* 208 */     int j = limit();
/* 209 */     assert (i <= j);
/* 210 */     int k = i <= j ? j - i : 0;
/* 211 */     int m = i << 3;
/* 212 */     assert (m >= 0);
/* 213 */     return new DirectDoubleBufferS(this, -1, 0, k, k, m);
/*     */   }
/*     */ 
/*     */   public DoubleBuffer duplicate() {
/* 217 */     return new DirectDoubleBufferS(this, markValue(), position(), limit(), capacity(), 0);
/*     */   }
/*     */ 
/*     */   public DoubleBuffer asReadOnlyBuffer()
/*     */   {
/* 227 */     return new DirectDoubleBufferRS(this, markValue(), position(), limit(), capacity(), 0);
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
/*     */   public double get() {
/* 249 */     return Double.longBitsToDouble(Bits.swap(unsafe.getLong(ix(nextGetIndex()))));
/*     */   }
/*     */ 
/*     */   public double get(int paramInt) {
/* 253 */     return Double.longBitsToDouble(Bits.swap(unsafe.getLong(ix(checkIndex(paramInt)))));
/*     */   }
/*     */ 
/*     */   public DoubleBuffer get(double[] paramArrayOfDouble, int paramInt1, int paramInt2)
/*     */   {
/* 258 */     if (paramInt2 << 3 > 6) {
/* 259 */       checkBounds(paramInt1, paramInt2, paramArrayOfDouble.length);
/* 260 */       int i = position();
/* 261 */       int j = limit();
/* 262 */       assert (i <= j);
/* 263 */       int k = i <= j ? j - i : 0;
/* 264 */       if (paramInt2 > k) {
/* 265 */         throw new BufferUnderflowException();
/*     */       }
/*     */ 
/* 268 */       if (order() != ByteOrder.nativeOrder()) {
/* 269 */         Bits.copyToLongArray(ix(i), paramArrayOfDouble, paramInt1 << 3, paramInt2 << 3);
/*     */       }
/*     */       else
/*     */       {
/* 274 */         Bits.copyToArray(ix(i), paramArrayOfDouble, arrayBaseOffset, paramInt1 << 3, paramInt2 << 3);
/*     */       }
/*     */ 
/* 277 */       position(i + paramInt2);
/*     */     } else {
/* 279 */       super.get(paramArrayOfDouble, paramInt1, paramInt2);
/*     */     }
/* 281 */     return this;
/*     */   }
/*     */ 
/*     */   public DoubleBuffer put(double paramDouble)
/*     */   {
/* 291 */     unsafe.putLong(ix(nextPutIndex()), Bits.swap(Double.doubleToRawLongBits(paramDouble)));
/* 292 */     return this;
/*     */   }
/*     */ 
/*     */   public DoubleBuffer put(int paramInt, double paramDouble)
/*     */   {
/* 300 */     unsafe.putLong(ix(checkIndex(paramInt)), Bits.swap(Double.doubleToRawLongBits(paramDouble)));
/* 301 */     return this;
/*     */   }
/*     */ 
/*     */   public DoubleBuffer put(DoubleBuffer paramDoubleBuffer)
/*     */   {
/*     */     int j;
/*     */     int k;
/* 309 */     if ((paramDoubleBuffer instanceof DirectDoubleBufferS)) {
/* 310 */       if (paramDoubleBuffer == this)
/* 311 */         throw new IllegalArgumentException();
/* 312 */       DirectDoubleBufferS localDirectDoubleBufferS = (DirectDoubleBufferS)paramDoubleBuffer;
/*     */ 
/* 314 */       j = localDirectDoubleBufferS.position();
/* 315 */       k = localDirectDoubleBufferS.limit();
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
/* 326 */       unsafe.copyMemory(localDirectDoubleBufferS.ix(j), ix(n), m << 3);
/* 327 */       localDirectDoubleBufferS.position(j + m);
/* 328 */       position(n + m);
/* 329 */     } else if (paramDoubleBuffer.hb != null)
/*     */     {
/* 331 */       int i = paramDoubleBuffer.position();
/* 332 */       j = paramDoubleBuffer.limit();
/* 333 */       assert (i <= j);
/* 334 */       k = i <= j ? j - i : 0;
/*     */ 
/* 336 */       put(paramDoubleBuffer.hb, paramDoubleBuffer.offset + i, k);
/* 337 */       paramDoubleBuffer.position(i + k);
/*     */     }
/*     */     else {
/* 340 */       super.put(paramDoubleBuffer);
/*     */     }
/* 342 */     return this;
/*     */   }
/*     */ 
/*     */   public DoubleBuffer put(double[] paramArrayOfDouble, int paramInt1, int paramInt2)
/*     */   {
/* 350 */     if (paramInt2 << 3 > 6) {
/* 351 */       checkBounds(paramInt1, paramInt2, paramArrayOfDouble.length);
/* 352 */       int i = position();
/* 353 */       int j = limit();
/* 354 */       assert (i <= j);
/* 355 */       int k = i <= j ? j - i : 0;
/* 356 */       if (paramInt2 > k) {
/* 357 */         throw new BufferOverflowException();
/*     */       }
/*     */ 
/* 360 */       if (order() != ByteOrder.nativeOrder()) {
/* 361 */         Bits.copyFromLongArray(paramArrayOfDouble, paramInt1 << 3, ix(i), paramInt2 << 3);
/*     */       }
/*     */       else
/*     */       {
/* 365 */         Bits.copyFromArray(paramArrayOfDouble, arrayBaseOffset, paramInt1 << 3, ix(i), paramInt2 << 3);
/*     */       }
/* 367 */       position(i + paramInt2);
/*     */     } else {
/* 369 */       super.put(paramArrayOfDouble, paramInt1, paramInt2);
/*     */     }
/* 371 */     return this;
/*     */   }
/*     */ 
/*     */   public DoubleBuffer compact()
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
 * Qualified Name:     java.nio.DirectDoubleBufferS
 * JD-Core Version:    0.6.2
 */