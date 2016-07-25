/*     */ package java.nio;
/*     */ 
/*     */ class HeapLongBufferR extends HeapLongBuffer
/*     */ {
/*     */   HeapLongBufferR(int paramInt1, int paramInt2)
/*     */   {
/*  63 */     super(paramInt1, paramInt2);
/*  64 */     this.isReadOnly = true;
/*     */   }
/*     */ 
/*     */   HeapLongBufferR(long[] paramArrayOfLong, int paramInt1, int paramInt2)
/*     */   {
/*  76 */     super(paramArrayOfLong, paramInt1, paramInt2);
/*  77 */     this.isReadOnly = true;
/*     */   }
/*     */ 
/*     */   protected HeapLongBufferR(long[] paramArrayOfLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/*  92 */     super(paramArrayOfLong, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*  93 */     this.isReadOnly = true;
/*     */   }
/*     */ 
/*     */   public LongBuffer slice()
/*     */   {
/*  98 */     return new HeapLongBufferR(this.hb, -1, 0, remaining(), remaining(), position() + this.offset);
/*     */   }
/*     */ 
/*     */   public LongBuffer duplicate()
/*     */   {
/* 107 */     return new HeapLongBufferR(this.hb, markValue(), position(), limit(), capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   public LongBuffer asReadOnlyBuffer()
/*     */   {
/* 124 */     return duplicate();
/*     */   }
/*     */ 
/*     */   public boolean isReadOnly()
/*     */   {
/* 158 */     return true;
/*     */   }
/*     */ 
/*     */   public LongBuffer put(long paramLong)
/*     */   {
/* 166 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public LongBuffer put(int paramInt, long paramLong)
/*     */   {
/* 175 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public LongBuffer put(long[] paramArrayOfLong, int paramInt1, int paramInt2)
/*     */   {
/* 188 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public LongBuffer put(LongBuffer paramLongBuffer)
/*     */   {
/* 216 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public LongBuffer compact()
/*     */   {
/* 228 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public ByteOrder order()
/*     */   {
/* 590 */     return ByteOrder.nativeOrder();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.HeapLongBufferR
 * JD-Core Version:    0.6.2
 */