/*     */ package java.nio;
/*     */ 
/*     */ class HeapLongBuffer extends LongBuffer
/*     */ {
/*     */   HeapLongBuffer(int paramInt1, int paramInt2)
/*     */   {
/*  57 */     super(-1, 0, paramInt2, paramInt1, new long[paramInt1], 0);
/*     */   }
/*     */ 
/*     */   HeapLongBuffer(long[] paramArrayOfLong, int paramInt1, int paramInt2)
/*     */   {
/*  70 */     super(-1, paramInt1, paramInt1 + paramInt2, paramArrayOfLong.length, paramArrayOfLong, 0);
/*     */   }
/*     */ 
/*     */   protected HeapLongBuffer(long[] paramArrayOfLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/*  86 */     super(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfLong, paramInt5);
/*     */   }
/*     */ 
/*     */   public LongBuffer slice()
/*     */   {
/*  98 */     return new HeapLongBuffer(this.hb, -1, 0, remaining(), remaining(), position() + this.offset);
/*     */   }
/*     */ 
/*     */   public LongBuffer duplicate()
/*     */   {
/* 107 */     return new HeapLongBuffer(this.hb, markValue(), position(), limit(), capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   public LongBuffer asReadOnlyBuffer()
/*     */   {
/* 117 */     return new HeapLongBufferR(this.hb, markValue(), position(), limit(), capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   protected int ix(int paramInt)
/*     */   {
/* 131 */     return paramInt + this.offset;
/*     */   }
/*     */ 
/*     */   public long get() {
/* 135 */     return this.hb[ix(nextGetIndex())];
/*     */   }
/*     */ 
/*     */   public long get(int paramInt) {
/* 139 */     return this.hb[ix(checkIndex(paramInt))];
/*     */   }
/*     */ 
/*     */   public LongBuffer get(long[] paramArrayOfLong, int paramInt1, int paramInt2) {
/* 143 */     checkBounds(paramInt1, paramInt2, paramArrayOfLong.length);
/* 144 */     if (paramInt2 > remaining())
/* 145 */       throw new BufferUnderflowException();
/* 146 */     System.arraycopy(this.hb, ix(position()), paramArrayOfLong, paramInt1, paramInt2);
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
/*     */   public LongBuffer put(long paramLong)
/*     */   {
/* 163 */     this.hb[ix(nextPutIndex())] = paramLong;
/* 164 */     return this;
/*     */   }
/*     */ 
/*     */   public LongBuffer put(int paramInt, long paramLong)
/*     */   {
/* 172 */     this.hb[ix(checkIndex(paramInt))] = paramLong;
/* 173 */     return this;
/*     */   }
/*     */ 
/*     */   public LongBuffer put(long[] paramArrayOfLong, int paramInt1, int paramInt2)
/*     */   {
/* 181 */     checkBounds(paramInt1, paramInt2, paramArrayOfLong.length);
/* 182 */     if (paramInt2 > remaining())
/* 183 */       throw new BufferOverflowException();
/* 184 */     System.arraycopy(paramArrayOfLong, paramInt1, this.hb, ix(position()), paramInt2);
/* 185 */     position(position() + paramInt2);
/* 186 */     return this;
/*     */   }
/*     */ 
/*     */   public LongBuffer put(LongBuffer paramLongBuffer)
/*     */   {
/* 194 */     if ((paramLongBuffer instanceof HeapLongBuffer)) {
/* 195 */       if (paramLongBuffer == this)
/* 196 */         throw new IllegalArgumentException();
/* 197 */       HeapLongBuffer localHeapLongBuffer = (HeapLongBuffer)paramLongBuffer;
/* 198 */       int j = localHeapLongBuffer.remaining();
/* 199 */       if (j > remaining())
/* 200 */         throw new BufferOverflowException();
/* 201 */       System.arraycopy(localHeapLongBuffer.hb, localHeapLongBuffer.ix(localHeapLongBuffer.position()), this.hb, ix(position()), j);
/*     */ 
/* 203 */       localHeapLongBuffer.position(localHeapLongBuffer.position() + j);
/* 204 */       position(position() + j);
/* 205 */     } else if (paramLongBuffer.isDirect()) {
/* 206 */       int i = paramLongBuffer.remaining();
/* 207 */       if (i > remaining())
/* 208 */         throw new BufferOverflowException();
/* 209 */       paramLongBuffer.get(this.hb, ix(position()), i);
/* 210 */       position(position() + i);
/*     */     } else {
/* 212 */       super.put(paramLongBuffer);
/*     */     }
/* 214 */     return this;
/*     */   }
/*     */ 
/*     */   public LongBuffer compact()
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
 * Qualified Name:     java.nio.HeapLongBuffer
 * JD-Core Version:    0.6.2
 */