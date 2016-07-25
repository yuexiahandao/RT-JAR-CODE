/*     */ package java.nio;
/*     */ 
/*     */ class HeapDoubleBufferR extends HeapDoubleBuffer
/*     */ {
/*     */   HeapDoubleBufferR(int paramInt1, int paramInt2)
/*     */   {
/*  63 */     super(paramInt1, paramInt2);
/*  64 */     this.isReadOnly = true;
/*     */   }
/*     */ 
/*     */   HeapDoubleBufferR(double[] paramArrayOfDouble, int paramInt1, int paramInt2)
/*     */   {
/*  76 */     super(paramArrayOfDouble, paramInt1, paramInt2);
/*  77 */     this.isReadOnly = true;
/*     */   }
/*     */ 
/*     */   protected HeapDoubleBufferR(double[] paramArrayOfDouble, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/*  92 */     super(paramArrayOfDouble, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*  93 */     this.isReadOnly = true;
/*     */   }
/*     */ 
/*     */   public DoubleBuffer slice()
/*     */   {
/*  98 */     return new HeapDoubleBufferR(this.hb, -1, 0, remaining(), remaining(), position() + this.offset);
/*     */   }
/*     */ 
/*     */   public DoubleBuffer duplicate()
/*     */   {
/* 107 */     return new HeapDoubleBufferR(this.hb, markValue(), position(), limit(), capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   public DoubleBuffer asReadOnlyBuffer()
/*     */   {
/* 124 */     return duplicate();
/*     */   }
/*     */ 
/*     */   public boolean isReadOnly()
/*     */   {
/* 158 */     return true;
/*     */   }
/*     */ 
/*     */   public DoubleBuffer put(double paramDouble)
/*     */   {
/* 166 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public DoubleBuffer put(int paramInt, double paramDouble)
/*     */   {
/* 175 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public DoubleBuffer put(double[] paramArrayOfDouble, int paramInt1, int paramInt2)
/*     */   {
/* 188 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public DoubleBuffer put(DoubleBuffer paramDoubleBuffer)
/*     */   {
/* 216 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public DoubleBuffer compact()
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
 * Qualified Name:     java.nio.HeapDoubleBufferR
 * JD-Core Version:    0.6.2
 */