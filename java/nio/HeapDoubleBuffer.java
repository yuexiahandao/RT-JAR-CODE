/*     */ package java.nio;
/*     */ 
/*     */ class HeapDoubleBuffer extends DoubleBuffer
/*     */ {
/*     */   HeapDoubleBuffer(int paramInt1, int paramInt2)
/*     */   {
/*  57 */     super(-1, 0, paramInt2, paramInt1, new double[paramInt1], 0);
/*     */   }
/*     */ 
/*     */   HeapDoubleBuffer(double[] paramArrayOfDouble, int paramInt1, int paramInt2)
/*     */   {
/*  70 */     super(-1, paramInt1, paramInt1 + paramInt2, paramArrayOfDouble.length, paramArrayOfDouble, 0);
/*     */   }
/*     */ 
/*     */   protected HeapDoubleBuffer(double[] paramArrayOfDouble, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/*  86 */     super(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfDouble, paramInt5);
/*     */   }
/*     */ 
/*     */   public DoubleBuffer slice()
/*     */   {
/*  98 */     return new HeapDoubleBuffer(this.hb, -1, 0, remaining(), remaining(), position() + this.offset);
/*     */   }
/*     */ 
/*     */   public DoubleBuffer duplicate()
/*     */   {
/* 107 */     return new HeapDoubleBuffer(this.hb, markValue(), position(), limit(), capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   public DoubleBuffer asReadOnlyBuffer()
/*     */   {
/* 117 */     return new HeapDoubleBufferR(this.hb, markValue(), position(), limit(), capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   protected int ix(int paramInt)
/*     */   {
/* 131 */     return paramInt + this.offset;
/*     */   }
/*     */ 
/*     */   public double get() {
/* 135 */     return this.hb[ix(nextGetIndex())];
/*     */   }
/*     */ 
/*     */   public double get(int paramInt) {
/* 139 */     return this.hb[ix(checkIndex(paramInt))];
/*     */   }
/*     */ 
/*     */   public DoubleBuffer get(double[] paramArrayOfDouble, int paramInt1, int paramInt2) {
/* 143 */     checkBounds(paramInt1, paramInt2, paramArrayOfDouble.length);
/* 144 */     if (paramInt2 > remaining())
/* 145 */       throw new BufferUnderflowException();
/* 146 */     System.arraycopy(this.hb, ix(position()), paramArrayOfDouble, paramInt1, paramInt2);
/* 147 */     position(position() + paramInt2);
/* 148 */     return this;
/*     */   }
/*     */ 
/*     */   public boolean isDirect() {
/* 152 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isReadOnly()
/*     */   {
/* 158 */     return false;
/*     */   }
/*     */ 
/*     */   public DoubleBuffer put(double paramDouble)
/*     */   {
/* 163 */     this.hb[ix(nextPutIndex())] = paramDouble;
/* 164 */     return this;
/*     */   }
/*     */ 
/*     */   public DoubleBuffer put(int paramInt, double paramDouble)
/*     */   {
/* 172 */     this.hb[ix(checkIndex(paramInt))] = paramDouble;
/* 173 */     return this;
/*     */   }
/*     */ 
/*     */   public DoubleBuffer put(double[] paramArrayOfDouble, int paramInt1, int paramInt2)
/*     */   {
/* 181 */     checkBounds(paramInt1, paramInt2, paramArrayOfDouble.length);
/* 182 */     if (paramInt2 > remaining())
/* 183 */       throw new BufferOverflowException();
/* 184 */     System.arraycopy(paramArrayOfDouble, paramInt1, this.hb, ix(position()), paramInt2);
/* 185 */     position(position() + paramInt2);
/* 186 */     return this;
/*     */   }
/*     */ 
/*     */   public DoubleBuffer put(DoubleBuffer paramDoubleBuffer)
/*     */   {
/* 194 */     if ((paramDoubleBuffer instanceof HeapDoubleBuffer)) {
/* 195 */       if (paramDoubleBuffer == this)
/* 196 */         throw new IllegalArgumentException();
/* 197 */       HeapDoubleBuffer localHeapDoubleBuffer = (HeapDoubleBuffer)paramDoubleBuffer;
/* 198 */       int j = localHeapDoubleBuffer.remaining();
/* 199 */       if (j > remaining())
/* 200 */         throw new BufferOverflowException();
/* 201 */       System.arraycopy(localHeapDoubleBuffer.hb, localHeapDoubleBuffer.ix(localHeapDoubleBuffer.position()), this.hb, ix(position()), j);
/*     */ 
/* 203 */       localHeapDoubleBuffer.position(localHeapDoubleBuffer.position() + j);
/* 204 */       position(position() + j);
/* 205 */     } else if (paramDoubleBuffer.isDirect()) {
/* 206 */       int i = paramDoubleBuffer.remaining();
/* 207 */       if (i > remaining())
/* 208 */         throw new BufferOverflowException();
/* 209 */       paramDoubleBuffer.get(this.hb, ix(position()), i);
/* 210 */       position(position() + i);
/*     */     } else {
/* 212 */       super.put(paramDoubleBuffer);
/*     */     }
/* 214 */     return this;
/*     */   }
/*     */ 
/*     */   public DoubleBuffer compact()
/*     */   {
/* 222 */     System.arraycopy(this.hb, ix(position()), this.hb, ix(0), remaining());
/* 223 */     position(remaining());
/* 224 */     limit(capacity());
/* 225 */     discardMark();
/* 226 */     return this;
/*     */   }
/*     */ 
/*     */   public ByteOrder order()
/*     */   {
/* 590 */     return ByteOrder.nativeOrder();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.HeapDoubleBuffer
 * JD-Core Version:    0.6.2
 */