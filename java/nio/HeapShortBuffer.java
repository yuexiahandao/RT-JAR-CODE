/*     */ package java.nio;
/*     */ 
/*     */ class HeapShortBuffer extends ShortBuffer
/*     */ {
/*     */   HeapShortBuffer(int paramInt1, int paramInt2)
/*     */   {
/*  57 */     super(-1, 0, paramInt2, paramInt1, new short[paramInt1], 0);
/*     */   }
/*     */ 
/*     */   HeapShortBuffer(short[] paramArrayOfShort, int paramInt1, int paramInt2)
/*     */   {
/*  70 */     super(-1, paramInt1, paramInt1 + paramInt2, paramArrayOfShort.length, paramArrayOfShort, 0);
/*     */   }
/*     */ 
/*     */   protected HeapShortBuffer(short[] paramArrayOfShort, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/*  86 */     super(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfShort, paramInt5);
/*     */   }
/*     */ 
/*     */   public ShortBuffer slice()
/*     */   {
/*  98 */     return new HeapShortBuffer(this.hb, -1, 0, remaining(), remaining(), position() + this.offset);
/*     */   }
/*     */ 
/*     */   public ShortBuffer duplicate()
/*     */   {
/* 107 */     return new HeapShortBuffer(this.hb, markValue(), position(), limit(), capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   public ShortBuffer asReadOnlyBuffer()
/*     */   {
/* 117 */     return new HeapShortBufferR(this.hb, markValue(), position(), limit(), capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   protected int ix(int paramInt)
/*     */   {
/* 131 */     return paramInt + this.offset;
/*     */   }
/*     */ 
/*     */   public short get() {
/* 135 */     return this.hb[ix(nextGetIndex())];
/*     */   }
/*     */ 
/*     */   public short get(int paramInt) {
/* 139 */     return this.hb[ix(checkIndex(paramInt))];
/*     */   }
/*     */ 
/*     */   public ShortBuffer get(short[] paramArrayOfShort, int paramInt1, int paramInt2) {
/* 143 */     checkBounds(paramInt1, paramInt2, paramArrayOfShort.length);
/* 144 */     if (paramInt2 > remaining())
/* 145 */       throw new BufferUnderflowException();
/* 146 */     System.arraycopy(this.hb, ix(position()), paramArrayOfShort, paramInt1, paramInt2);
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
/*     */   public ShortBuffer put(short paramShort)
/*     */   {
/* 163 */     this.hb[ix(nextPutIndex())] = paramShort;
/* 164 */     return this;
/*     */   }
/*     */ 
/*     */   public ShortBuffer put(int paramInt, short paramShort)
/*     */   {
/* 172 */     this.hb[ix(checkIndex(paramInt))] = paramShort;
/* 173 */     return this;
/*     */   }
/*     */ 
/*     */   public ShortBuffer put(short[] paramArrayOfShort, int paramInt1, int paramInt2)
/*     */   {
/* 181 */     checkBounds(paramInt1, paramInt2, paramArrayOfShort.length);
/* 182 */     if (paramInt2 > remaining())
/* 183 */       throw new BufferOverflowException();
/* 184 */     System.arraycopy(paramArrayOfShort, paramInt1, this.hb, ix(position()), paramInt2);
/* 185 */     position(position() + paramInt2);
/* 186 */     return this;
/*     */   }
/*     */ 
/*     */   public ShortBuffer put(ShortBuffer paramShortBuffer)
/*     */   {
/* 194 */     if ((paramShortBuffer instanceof HeapShortBuffer)) {
/* 195 */       if (paramShortBuffer == this)
/* 196 */         throw new IllegalArgumentException();
/* 197 */       HeapShortBuffer localHeapShortBuffer = (HeapShortBuffer)paramShortBuffer;
/* 198 */       int j = localHeapShortBuffer.remaining();
/* 199 */       if (j > remaining())
/* 200 */         throw new BufferOverflowException();
/* 201 */       System.arraycopy(localHeapShortBuffer.hb, localHeapShortBuffer.ix(localHeapShortBuffer.position()), this.hb, ix(position()), j);
/*     */ 
/* 203 */       localHeapShortBuffer.position(localHeapShortBuffer.position() + j);
/* 204 */       position(position() + j);
/* 205 */     } else if (paramShortBuffer.isDirect()) {
/* 206 */       int i = paramShortBuffer.remaining();
/* 207 */       if (i > remaining())
/* 208 */         throw new BufferOverflowException();
/* 209 */       paramShortBuffer.get(this.hb, ix(position()), i);
/* 210 */       position(position() + i);
/*     */     } else {
/* 212 */       super.put(paramShortBuffer);
/*     */     }
/* 214 */     return this;
/*     */   }
/*     */ 
/*     */   public ShortBuffer compact()
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
 * Qualified Name:     java.nio.HeapShortBuffer
 * JD-Core Version:    0.6.2
 */