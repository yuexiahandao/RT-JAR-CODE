/*     */ package java.nio;
/*     */ 
/*     */ import sun.misc.Cleaner;
/*     */ import sun.misc.Unsafe;
/*     */ import sun.nio.ch.DirectBuffer;
/*     */ 
/*     */ class DirectCharBufferU extends CharBuffer
/*     */   implements DirectBuffer
/*     */ {
/*  49 */   protected static final Unsafe unsafe = Bits.unsafe();
/*     */ 
/*  52 */   private static final long arrayBaseOffset = unsafe.arrayBaseOffset([C.class);
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
/*     */   DirectCharBufferU(DirectBuffer paramDirectBuffer, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/* 195 */     super(paramInt1, paramInt2, paramInt3, paramInt4);
/* 196 */     this.address = (paramDirectBuffer.address() + paramInt5);
/*     */ 
/* 200 */     this.att = paramDirectBuffer;
/*     */   }
/*     */ 
/*     */   public CharBuffer slice()
/*     */   {
/* 207 */     int i = position();
/* 208 */     int j = limit();
/* 209 */     assert (i <= j);
/* 210 */     int k = i <= j ? j - i : 0;
/* 211 */     int m = i << 1;
/* 212 */     assert (m >= 0);
/* 213 */     return new DirectCharBufferU(this, -1, 0, k, k, m);
/*     */   }
/*     */ 
/*     */   public CharBuffer duplicate() {
/* 217 */     return new DirectCharBufferU(this, markValue(), position(), limit(), capacity(), 0);
/*     */   }
/*     */ 
/*     */   public CharBuffer asReadOnlyBuffer()
/*     */   {
/* 227 */     return new DirectCharBufferRU(this, markValue(), position(), limit(), capacity(), 0);
/*     */   }
/*     */ 
/*     */   public long address()
/*     */   {
/* 241 */     return this.address;
/*     */   }
/*     */ 
/*     */   private long ix(int paramInt) {
/* 245 */     return this.address + (paramInt << 1);
/*     */   }
/*     */ 
/*     */   public char get() {
/* 249 */     return unsafe.getChar(ix(nextGetIndex()));
/*     */   }
/*     */ 
/*     */   public char get(int paramInt) {
/* 253 */     return unsafe.getChar(ix(checkIndex(paramInt)));
/*     */   }
/*     */ 
/*     */   public CharBuffer get(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */   {
/* 258 */     if (paramInt2 << 1 > 6) {
/* 259 */       checkBounds(paramInt1, paramInt2, paramArrayOfChar.length);
/* 260 */       int i = position();
/* 261 */       int j = limit();
/* 262 */       assert (i <= j);
/* 263 */       int k = i <= j ? j - i : 0;
/* 264 */       if (paramInt2 > k) {
/* 265 */         throw new BufferUnderflowException();
/*     */       }
/*     */ 
/* 268 */       if (order() != ByteOrder.nativeOrder()) {
/* 269 */         Bits.copyToCharArray(ix(i), paramArrayOfChar, paramInt1 << 1, paramInt2 << 1);
/*     */       }
/*     */       else
/*     */       {
/* 274 */         Bits.copyToArray(ix(i), paramArrayOfChar, arrayBaseOffset, paramInt1 << 1, paramInt2 << 1);
/*     */       }
/*     */ 
/* 277 */       position(i + paramInt2);
/*     */     } else {
/* 279 */       super.get(paramArrayOfChar, paramInt1, paramInt2);
/*     */     }
/* 281 */     return this;
/*     */   }
/*     */ 
/*     */   public CharBuffer put(char paramChar)
/*     */   {
/* 291 */     unsafe.putChar(ix(nextPutIndex()), paramChar);
/* 292 */     return this;
/*     */   }
/*     */ 
/*     */   public CharBuffer put(int paramInt, char paramChar)
/*     */   {
/* 300 */     unsafe.putChar(ix(checkIndex(paramInt)), paramChar);
/* 301 */     return this;
/*     */   }
/*     */ 
/*     */   public CharBuffer put(CharBuffer paramCharBuffer)
/*     */   {
/*     */     int j;
/*     */     int k;
/* 309 */     if ((paramCharBuffer instanceof DirectCharBufferU)) {
/* 310 */       if (paramCharBuffer == this)
/* 311 */         throw new IllegalArgumentException();
/* 312 */       DirectCharBufferU localDirectCharBufferU = (DirectCharBufferU)paramCharBuffer;
/*     */ 
/* 314 */       j = localDirectCharBufferU.position();
/* 315 */       k = localDirectCharBufferU.limit();
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
/* 326 */       unsafe.copyMemory(localDirectCharBufferU.ix(j), ix(n), m << 1);
/* 327 */       localDirectCharBufferU.position(j + m);
/* 328 */       position(n + m);
/* 329 */     } else if (paramCharBuffer.hb != null)
/*     */     {
/* 331 */       int i = paramCharBuffer.position();
/* 332 */       j = paramCharBuffer.limit();
/* 333 */       assert (i <= j);
/* 334 */       k = i <= j ? j - i : 0;
/*     */ 
/* 336 */       put(paramCharBuffer.hb, paramCharBuffer.offset + i, k);
/* 337 */       paramCharBuffer.position(i + k);
/*     */     }
/*     */     else {
/* 340 */       super.put(paramCharBuffer);
/*     */     }
/* 342 */     return this;
/*     */   }
/*     */ 
/*     */   public CharBuffer put(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */   {
/* 350 */     if (paramInt2 << 1 > 6) {
/* 351 */       checkBounds(paramInt1, paramInt2, paramArrayOfChar.length);
/* 352 */       int i = position();
/* 353 */       int j = limit();
/* 354 */       assert (i <= j);
/* 355 */       int k = i <= j ? j - i : 0;
/* 356 */       if (paramInt2 > k) {
/* 357 */         throw new BufferOverflowException();
/*     */       }
/*     */ 
/* 360 */       if (order() != ByteOrder.nativeOrder()) {
/* 361 */         Bits.copyFromCharArray(paramArrayOfChar, paramInt1 << 1, ix(i), paramInt2 << 1);
/*     */       }
/*     */       else
/*     */       {
/* 365 */         Bits.copyFromArray(paramArrayOfChar, arrayBaseOffset, paramInt1 << 1, ix(i), paramInt2 << 1);
/*     */       }
/* 367 */       position(i + paramInt2);
/*     */     } else {
/* 369 */       super.put(paramArrayOfChar, paramInt1, paramInt2);
/*     */     }
/* 371 */     return this;
/*     */   }
/*     */ 
/*     */   public CharBuffer compact()
/*     */   {
/* 379 */     int i = position();
/* 380 */     int j = limit();
/* 381 */     assert (i <= j);
/* 382 */     int k = i <= j ? j - i : 0;
/*     */ 
/* 384 */     unsafe.copyMemory(ix(i), ix(0), k << 1);
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
/*     */   public String toString(int paramInt1, int paramInt2)
/*     */   {
/* 406 */     if ((paramInt2 > limit()) || (paramInt1 > paramInt2))
/* 407 */       throw new IndexOutOfBoundsException();
/*     */     try {
/* 409 */       int i = paramInt2 - paramInt1;
/* 410 */       char[] arrayOfChar = new char[i];
/* 411 */       CharBuffer localCharBuffer1 = CharBuffer.wrap(arrayOfChar);
/* 412 */       CharBuffer localCharBuffer2 = duplicate();
/* 413 */       localCharBuffer2.position(paramInt1);
/* 414 */       localCharBuffer2.limit(paramInt2);
/* 415 */       localCharBuffer1.put(localCharBuffer2);
/* 416 */       return new String(arrayOfChar); } catch (StringIndexOutOfBoundsException localStringIndexOutOfBoundsException) {
/*     */     }
/* 418 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */ 
/*     */   public CharBuffer subSequence(int paramInt1, int paramInt2)
/*     */   {
/* 426 */     int i = position();
/* 427 */     int j = limit();
/* 428 */     assert (i <= j);
/* 429 */     i = i <= j ? i : j;
/* 430 */     int k = j - i;
/*     */ 
/* 432 */     if ((paramInt1 < 0) || (paramInt2 > k) || (paramInt1 > paramInt2))
/* 433 */       throw new IndexOutOfBoundsException();
/* 434 */     return new DirectCharBufferU(this, -1, i + paramInt1, i + paramInt2, capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   public ByteOrder order()
/*     */   {
/* 454 */     return ByteOrder.nativeOrder() != ByteOrder.BIG_ENDIAN ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.DirectCharBufferU
 * JD-Core Version:    0.6.2
 */