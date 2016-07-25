/*     */ package java.nio;
/*     */ 
/*     */ import sun.nio.ch.DirectBuffer;
/*     */ 
/*     */ class DirectShortBufferRU extends DirectShortBufferU
/*     */   implements DirectBuffer
/*     */ {
/*     */   DirectShortBufferRU(DirectBuffer paramDirectBuffer, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/* 202 */     super(paramDirectBuffer, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*     */   }
/*     */ 
/*     */   public ShortBuffer slice()
/*     */   {
/* 207 */     int i = position();
/* 208 */     int j = limit();
/* 209 */     assert (i <= j);
/* 210 */     int k = i <= j ? j - i : 0;
/* 211 */     int m = i << 1;
/* 212 */     assert (m >= 0);
/* 213 */     return new DirectShortBufferRU(this, -1, 0, k, k, m);
/*     */   }
/*     */ 
/*     */   public ShortBuffer duplicate() {
/* 217 */     return new DirectShortBufferRU(this, markValue(), position(), limit(), capacity(), 0);
/*     */   }
/*     */ 
/*     */   public ShortBuffer asReadOnlyBuffer()
/*     */   {
/* 234 */     return duplicate();
/*     */   }
/*     */ 
/*     */   public ShortBuffer put(short paramShort)
/*     */   {
/* 294 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public ShortBuffer put(int paramInt, short paramShort)
/*     */   {
/* 303 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public ShortBuffer put(ShortBuffer paramShortBuffer)
/*     */   {
/* 344 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public ShortBuffer put(short[] paramArrayOfShort, int paramInt1, int paramInt2)
/*     */   {
/* 373 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public ShortBuffer compact()
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
/*     */   public ByteOrder order()
/*     */   {
/* 454 */     return ByteOrder.nativeOrder() != ByteOrder.BIG_ENDIAN ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.DirectShortBufferRU
 * JD-Core Version:    0.6.2
 */