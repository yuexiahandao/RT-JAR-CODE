/*     */ package java.nio;
/*     */ 
/*     */ class HeapIntBufferR extends HeapIntBuffer
/*     */ {
/*     */   HeapIntBufferR(int paramInt1, int paramInt2)
/*     */   {
/*  63 */     super(paramInt1, paramInt2);
/*  64 */     this.isReadOnly = true;
/*     */   }
/*     */ 
/*     */   HeapIntBufferR(int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*     */   {
/*  76 */     super(paramArrayOfInt, paramInt1, paramInt2);
/*  77 */     this.isReadOnly = true;
/*     */   }
/*     */ 
/*     */   protected HeapIntBufferR(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/*  92 */     super(paramArrayOfInt, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*  93 */     this.isReadOnly = true;
/*     */   }
/*     */ 
/*     */   public IntBuffer slice()
/*     */   {
/*  98 */     return new HeapIntBufferR(this.hb, -1, 0, remaining(), remaining(), position() + this.offset);
/*     */   }
/*     */ 
/*     */   public IntBuffer duplicate()
/*     */   {
/* 107 */     return new HeapIntBufferR(this.hb, markValue(), position(), limit(), capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   public IntBuffer asReadOnlyBuffer()
/*     */   {
/* 124 */     return duplicate();
/*     */   }
/*     */ 
/*     */   public boolean isReadOnly()
/*     */   {
/* 158 */     return true;
/*     */   }
/*     */ 
/*     */   public IntBuffer put(int paramInt)
/*     */   {
/* 166 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public IntBuffer put(int paramInt1, int paramInt2)
/*     */   {
/* 175 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public IntBuffer put(int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*     */   {
/* 188 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public IntBuffer put(IntBuffer paramIntBuffer)
/*     */   {
/* 216 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public IntBuffer compact()
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
 * Qualified Name:     java.nio.HeapIntBufferR
 * JD-Core Version:    0.6.2
 */