/*     */ package java.nio;
/*     */ 
/*     */ import sun.nio.ch.DirectBuffer;
/*     */ 
/*     */ class DirectCharBufferRS extends DirectCharBufferS
/*     */   implements DirectBuffer
/*     */ {
/*     */   DirectCharBufferRS(DirectBuffer paramDirectBuffer, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/* 202 */     super(paramDirectBuffer, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
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
/* 213 */     return new DirectCharBufferRS(this, -1, 0, k, k, m);
/*     */   }
/*     */ 
/*     */   public CharBuffer duplicate() {
/* 217 */     return new DirectCharBufferRS(this, markValue(), position(), limit(), capacity(), 0);
/*     */   }
/*     */ 
/*     */   public CharBuffer asReadOnlyBuffer()
/*     */   {
/* 234 */     return duplicate();
/*     */   }
/*     */ 
/*     */   public CharBuffer put(char paramChar)
/*     */   {
/* 294 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public CharBuffer put(int paramInt, char paramChar)
/*     */   {
/* 303 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public CharBuffer put(CharBuffer paramCharBuffer)
/*     */   {
/* 344 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public CharBuffer put(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */   {
/* 373 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public CharBuffer compact()
/*     */   {
/* 390 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public boolean isDirect()
/*     */   {
/* 395 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isReadOnly() {
/* 399 */     return true;
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
/* 434 */     return new DirectCharBufferRS(this, -1, i + paramInt1, i + paramInt2, capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   public ByteOrder order()
/*     */   {
/* 450 */     return ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.DirectCharBufferRS
 * JD-Core Version:    0.6.2
 */