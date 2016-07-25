/*     */ package java.nio;
/*     */ 
/*     */ class HeapFloatBuffer extends FloatBuffer
/*     */ {
/*     */   HeapFloatBuffer(int paramInt1, int paramInt2)
/*     */   {
/*  57 */     super(-1, 0, paramInt2, paramInt1, new float[paramInt1], 0);
/*     */   }
/*     */ 
/*     */   HeapFloatBuffer(float[] paramArrayOfFloat, int paramInt1, int paramInt2)
/*     */   {
/*  70 */     super(-1, paramInt1, paramInt1 + paramInt2, paramArrayOfFloat.length, paramArrayOfFloat, 0);
/*     */   }
/*     */ 
/*     */   protected HeapFloatBuffer(float[] paramArrayOfFloat, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/*  86 */     super(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfFloat, paramInt5);
/*     */   }
/*     */ 
/*     */   public FloatBuffer slice()
/*     */   {
/*  98 */     return new HeapFloatBuffer(this.hb, -1, 0, remaining(), remaining(), position() + this.offset);
/*     */   }
/*     */ 
/*     */   public FloatBuffer duplicate()
/*     */   {
/* 107 */     return new HeapFloatBuffer(this.hb, markValue(), position(), limit(), capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   public FloatBuffer asReadOnlyBuffer()
/*     */   {
/* 117 */     return new HeapFloatBufferR(this.hb, markValue(), position(), limit(), capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   protected int ix(int paramInt)
/*     */   {
/* 131 */     return paramInt + this.offset;
/*     */   }
/*     */ 
/*     */   public float get() {
/* 135 */     return this.hb[ix(nextGetIndex())];
/*     */   }
/*     */ 
/*     */   public float get(int paramInt) {
/* 139 */     return this.hb[ix(checkIndex(paramInt))];
/*     */   }
/*     */ 
/*     */   public FloatBuffer get(float[] paramArrayOfFloat, int paramInt1, int paramInt2) {
/* 143 */     checkBounds(paramInt1, paramInt2, paramArrayOfFloat.length);
/* 144 */     if (paramInt2 > remaining())
/* 145 */       throw new BufferUnderflowException();
/* 146 */     System.arraycopy(this.hb, ix(position()), paramArrayOfFloat, paramInt1, paramInt2);
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
/*     */   public FloatBuffer put(float paramFloat)
/*     */   {
/* 163 */     this.hb[ix(nextPutIndex())] = paramFloat;
/* 164 */     return this;
/*     */   }
/*     */ 
/*     */   public FloatBuffer put(int paramInt, float paramFloat)
/*     */   {
/* 172 */     this.hb[ix(checkIndex(paramInt))] = paramFloat;
/* 173 */     return this;
/*     */   }
/*     */ 
/*     */   public FloatBuffer put(float[] paramArrayOfFloat, int paramInt1, int paramInt2)
/*     */   {
/* 181 */     checkBounds(paramInt1, paramInt2, paramArrayOfFloat.length);
/* 182 */     if (paramInt2 > remaining())
/* 183 */       throw new BufferOverflowException();
/* 184 */     System.arraycopy(paramArrayOfFloat, paramInt1, this.hb, ix(position()), paramInt2);
/* 185 */     position(position() + paramInt2);
/* 186 */     return this;
/*     */   }
/*     */ 
/*     */   public FloatBuffer put(FloatBuffer paramFloatBuffer)
/*     */   {
/* 194 */     if ((paramFloatBuffer instanceof HeapFloatBuffer)) {
/* 195 */       if (paramFloatBuffer == this)
/* 196 */         throw new IllegalArgumentException();
/* 197 */       HeapFloatBuffer localHeapFloatBuffer = (HeapFloatBuffer)paramFloatBuffer;
/* 198 */       int j = localHeapFloatBuffer.remaining();
/* 199 */       if (j > remaining())
/* 200 */         throw new BufferOverflowException();
/* 201 */       System.arraycopy(localHeapFloatBuffer.hb, localHeapFloatBuffer.ix(localHeapFloatBuffer.position()), this.hb, ix(position()), j);
/*     */ 
/* 203 */       localHeapFloatBuffer.position(localHeapFloatBuffer.position() + j);
/* 204 */       position(position() + j);
/* 205 */     } else if (paramFloatBuffer.isDirect()) {
/* 206 */       int i = paramFloatBuffer.remaining();
/* 207 */       if (i > remaining())
/* 208 */         throw new BufferOverflowException();
/* 209 */       paramFloatBuffer.get(this.hb, ix(position()), i);
/* 210 */       position(position() + i);
/*     */     } else {
/* 212 */       super.put(paramFloatBuffer);
/*     */     }
/* 214 */     return this;
/*     */   }
/*     */ 
/*     */   public FloatBuffer compact()
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
 * Qualified Name:     java.nio.HeapFloatBuffer
 * JD-Core Version:    0.6.2
 */