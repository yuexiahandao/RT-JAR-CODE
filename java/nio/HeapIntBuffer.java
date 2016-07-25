/*     */ package java.nio;
/*     */ 
/*     */ class HeapIntBuffer extends IntBuffer
/*     */ {
/*     */   HeapIntBuffer(int paramInt1, int paramInt2)
/*     */   {
/*  57 */     super(-1, 0, paramInt2, paramInt1, new int[paramInt1], 0);
/*     */   }
/*     */ 
/*     */   HeapIntBuffer(int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*     */   {
/*  70 */     super(-1, paramInt1, paramInt1 + paramInt2, paramArrayOfInt.length, paramArrayOfInt, 0);
/*     */   }
/*     */ 
/*     */   protected HeapIntBuffer(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/*  86 */     super(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt, paramInt5);
/*     */   }
/*     */ 
/*     */   public IntBuffer slice()
/*     */   {
/*  98 */     return new HeapIntBuffer(this.hb, -1, 0, remaining(), remaining(), position() + this.offset);
/*     */   }
/*     */ 
/*     */   public IntBuffer duplicate()
/*     */   {
/* 107 */     return new HeapIntBuffer(this.hb, markValue(), position(), limit(), capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   public IntBuffer asReadOnlyBuffer()
/*     */   {
/* 117 */     return new HeapIntBufferR(this.hb, markValue(), position(), limit(), capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   protected int ix(int paramInt)
/*     */   {
/* 131 */     return paramInt + this.offset;
/*     */   }
/*     */ 
/*     */   public int get() {
/* 135 */     return this.hb[ix(nextGetIndex())];
/*     */   }
/*     */ 
/*     */   public int get(int paramInt) {
/* 139 */     return this.hb[ix(checkIndex(paramInt))];
/*     */   }
/*     */ 
/*     */   public IntBuffer get(int[] paramArrayOfInt, int paramInt1, int paramInt2) {
/* 143 */     checkBounds(paramInt1, paramInt2, paramArrayOfInt.length);
/* 144 */     if (paramInt2 > remaining())
/* 145 */       throw new BufferUnderflowException();
/* 146 */     System.arraycopy(this.hb, ix(position()), paramArrayOfInt, paramInt1, paramInt2);
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
/*     */   public IntBuffer put(int paramInt)
/*     */   {
/* 163 */     this.hb[ix(nextPutIndex())] = paramInt;
/* 164 */     return this;
/*     */   }
/*     */ 
/*     */   public IntBuffer put(int paramInt1, int paramInt2)
/*     */   {
/* 172 */     this.hb[ix(checkIndex(paramInt1))] = paramInt2;
/* 173 */     return this;
/*     */   }
/*     */ 
/*     */   public IntBuffer put(int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*     */   {
/* 181 */     checkBounds(paramInt1, paramInt2, paramArrayOfInt.length);
/* 182 */     if (paramInt2 > remaining())
/* 183 */       throw new BufferOverflowException();
/* 184 */     System.arraycopy(paramArrayOfInt, paramInt1, this.hb, ix(position()), paramInt2);
/* 185 */     position(position() + paramInt2);
/* 186 */     return this;
/*     */   }
/*     */ 
/*     */   public IntBuffer put(IntBuffer paramIntBuffer)
/*     */   {
/* 194 */     if ((paramIntBuffer instanceof HeapIntBuffer)) {
/* 195 */       if (paramIntBuffer == this)
/* 196 */         throw new IllegalArgumentException();
/* 197 */       HeapIntBuffer localHeapIntBuffer = (HeapIntBuffer)paramIntBuffer;
/* 198 */       int j = localHeapIntBuffer.remaining();
/* 199 */       if (j > remaining())
/* 200 */         throw new BufferOverflowException();
/* 201 */       System.arraycopy(localHeapIntBuffer.hb, localHeapIntBuffer.ix(localHeapIntBuffer.position()), this.hb, ix(position()), j);
/*     */ 
/* 203 */       localHeapIntBuffer.position(localHeapIntBuffer.position() + j);
/* 204 */       position(position() + j);
/* 205 */     } else if (paramIntBuffer.isDirect()) {
/* 206 */       int i = paramIntBuffer.remaining();
/* 207 */       if (i > remaining())
/* 208 */         throw new BufferOverflowException();
/* 209 */       paramIntBuffer.get(this.hb, ix(position()), i);
/* 210 */       position(position() + i);
/*     */     } else {
/* 212 */       super.put(paramIntBuffer);
/*     */     }
/* 214 */     return this;
/*     */   }
/*     */ 
/*     */   public IntBuffer compact()
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
 * Qualified Name:     java.nio.HeapIntBuffer
 * JD-Core Version:    0.6.2
 */