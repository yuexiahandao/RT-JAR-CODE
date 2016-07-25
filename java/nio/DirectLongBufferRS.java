/*     */ package java.nio;
/*     */ 
/*     */ import sun.nio.ch.DirectBuffer;
/*     */ 
/*     */ class DirectLongBufferRS extends DirectLongBufferS
/*     */   implements DirectBuffer
/*     */ {
/*     */   DirectLongBufferRS(DirectBuffer paramDirectBuffer, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/* 202 */     super(paramDirectBuffer, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
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
/* 213 */     return new DirectLongBufferRS(this, -1, 0, k, k, m);
/*     */   }
/*     */ 
/*     */   public LongBuffer duplicate() {
/* 217 */     return new DirectLongBufferRS(this, markValue(), position(), limit(), capacity(), 0);
/*     */   }
/*     */ 
/*     */   public LongBuffer asReadOnlyBuffer()
/*     */   {
/* 234 */     return duplicate();
/*     */   }
/*     */ 
/*     */   public LongBuffer put(long paramLong)
/*     */   {
/* 294 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public LongBuffer put(int paramInt, long paramLong)
/*     */   {
/* 303 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public LongBuffer put(LongBuffer paramLongBuffer)
/*     */   {
/* 344 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public LongBuffer put(long[] paramArrayOfLong, int paramInt1, int paramInt2)
/*     */   {
/* 373 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public LongBuffer compact()
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
/* 450 */     return ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.DirectLongBufferRS
 * JD-Core Version:    0.6.2
 */