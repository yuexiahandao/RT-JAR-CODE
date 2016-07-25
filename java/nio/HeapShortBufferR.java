/*     */ package java.nio;
/*     */ 
/*     */ class HeapShortBufferR extends HeapShortBuffer
/*     */ {
/*     */   HeapShortBufferR(int paramInt1, int paramInt2)
/*     */   {
/*  63 */     super(paramInt1, paramInt2);
/*  64 */     this.isReadOnly = true;
/*     */   }
/*     */ 
/*     */   HeapShortBufferR(short[] paramArrayOfShort, int paramInt1, int paramInt2)
/*     */   {
/*  76 */     super(paramArrayOfShort, paramInt1, paramInt2);
/*  77 */     this.isReadOnly = true;
/*     */   }
/*     */ 
/*     */   protected HeapShortBufferR(short[] paramArrayOfShort, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/*  92 */     super(paramArrayOfShort, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*  93 */     this.isReadOnly = true;
/*     */   }
/*     */ 
/*     */   public ShortBuffer slice()
/*     */   {
/*  98 */     return new HeapShortBufferR(this.hb, -1, 0, remaining(), remaining(), position() + this.offset);
/*     */   }
/*     */ 
/*     */   public ShortBuffer duplicate()
/*     */   {
/* 107 */     return new HeapShortBufferR(this.hb, markValue(), position(), limit(), capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   public ShortBuffer asReadOnlyBuffer()
/*     */   {
/* 124 */     return duplicate();
/*     */   }
/*     */ 
/*     */   public boolean isReadOnly()
/*     */   {
/* 158 */     return true;
/*     */   }
/*     */ 
/*     */   public ShortBuffer put(short paramShort)
/*     */   {
/* 166 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public ShortBuffer put(int paramInt, short paramShort)
/*     */   {
/* 175 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public ShortBuffer put(short[] paramArrayOfShort, int paramInt1, int paramInt2)
/*     */   {
/* 188 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public ShortBuffer put(ShortBuffer paramShortBuffer)
/*     */   {
/* 216 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public ShortBuffer compact()
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
 * Qualified Name:     java.nio.HeapShortBufferR
 * JD-Core Version:    0.6.2
 */