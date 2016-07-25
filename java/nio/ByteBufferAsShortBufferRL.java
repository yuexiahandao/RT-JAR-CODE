/*     */ package java.nio;
/*     */ 
/*     */ class ByteBufferAsShortBufferRL extends ByteBufferAsShortBufferL
/*     */ {
/*     */   ByteBufferAsShortBufferRL(ByteBuffer paramByteBuffer)
/*     */   {
/*  55 */     super(paramByteBuffer);
/*     */   }
/*     */ 
/*     */   ByteBufferAsShortBufferRL(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/*  68 */     super(paramByteBuffer, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*     */   }
/*     */ 
/*     */   public ShortBuffer slice()
/*     */   {
/*  73 */     int i = position();
/*  74 */     int j = limit();
/*  75 */     assert (i <= j);
/*  76 */     int k = i <= j ? j - i : 0;
/*  77 */     int m = (i << 1) + this.offset;
/*  78 */     assert (m >= 0);
/*  79 */     return new ByteBufferAsShortBufferRL(this.bb, -1, 0, k, k, m);
/*     */   }
/*     */ 
/*     */   public ShortBuffer duplicate() {
/*  83 */     return new ByteBufferAsShortBufferRL(this.bb, markValue(), position(), limit(), capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   public ShortBuffer asReadOnlyBuffer()
/*     */   {
/* 100 */     return duplicate();
/*     */   }
/*     */ 
/*     */   public ShortBuffer put(short paramShort)
/*     */   {
/* 125 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public ShortBuffer put(int paramInt, short paramShort)
/*     */   {
/* 134 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public ShortBuffer compact()
/*     */   {
/* 156 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public boolean isDirect()
/*     */   {
/* 161 */     return this.bb.isDirect();
/*     */   }
/*     */ 
/*     */   public boolean isReadOnly() {
/* 165 */     return true;
/*     */   }
/*     */ 
/*     */   public ByteOrder order()
/*     */   {
/* 215 */     return ByteOrder.LITTLE_ENDIAN;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.ByteBufferAsShortBufferRL
 * JD-Core Version:    0.6.2
 */