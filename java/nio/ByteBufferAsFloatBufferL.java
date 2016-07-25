/*     */ package java.nio;
/*     */ 
/*     */ class ByteBufferAsFloatBufferL extends FloatBuffer
/*     */ {
/*     */   protected final ByteBuffer bb;
/*     */   protected final int offset;
/*     */ 
/*     */   ByteBufferAsFloatBufferL(ByteBuffer paramByteBuffer)
/*     */   {
/*  44 */     super(-1, 0, paramByteBuffer.remaining() >> 2, paramByteBuffer.remaining() >> 2);
/*     */ 
/*  47 */     this.bb = paramByteBuffer;
/*     */ 
/*  49 */     int i = capacity();
/*  50 */     limit(i);
/*  51 */     int j = position();
/*  52 */     assert (j <= i);
/*  53 */     this.offset = j;
/*     */   }
/*     */ 
/*     */   ByteBufferAsFloatBufferL(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/*  64 */     super(paramInt1, paramInt2, paramInt3, paramInt4);
/*  65 */     this.bb = paramByteBuffer;
/*  66 */     this.offset = paramInt5;
/*     */   }
/*     */ 
/*     */   public FloatBuffer slice()
/*     */   {
/*  73 */     int i = position();
/*  74 */     int j = limit();
/*  75 */     assert (i <= j);
/*  76 */     int k = i <= j ? j - i : 0;
/*  77 */     int m = (i << 2) + this.offset;
/*  78 */     assert (m >= 0);
/*  79 */     return new ByteBufferAsFloatBufferL(this.bb, -1, 0, k, k, m);
/*     */   }
/*     */ 
/*     */   public FloatBuffer duplicate() {
/*  83 */     return new ByteBufferAsFloatBufferL(this.bb, markValue(), position(), limit(), capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   public FloatBuffer asReadOnlyBuffer()
/*     */   {
/*  93 */     return new ByteBufferAsFloatBufferRL(this.bb, markValue(), position(), limit(), capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   protected int ix(int paramInt)
/*     */   {
/* 107 */     return (paramInt << 2) + this.offset;
/*     */   }
/*     */ 
/*     */   public float get() {
/* 111 */     return Bits.getFloatL(this.bb, ix(nextGetIndex()));
/*     */   }
/*     */ 
/*     */   public float get(int paramInt) {
/* 115 */     return Bits.getFloatL(this.bb, ix(checkIndex(paramInt)));
/*     */   }
/*     */ 
/*     */   public FloatBuffer put(float paramFloat)
/*     */   {
/* 122 */     Bits.putFloatL(this.bb, ix(nextPutIndex()), paramFloat);
/* 123 */     return this;
/*     */   }
/*     */ 
/*     */   public FloatBuffer put(int paramInt, float paramFloat)
/*     */   {
/* 131 */     Bits.putFloatL(this.bb, ix(checkIndex(paramInt)), paramFloat);
/* 132 */     return this;
/*     */   }
/*     */ 
/*     */   public FloatBuffer compact()
/*     */   {
/* 140 */     int i = position();
/* 141 */     int j = limit();
/* 142 */     assert (i <= j);
/* 143 */     int k = i <= j ? j - i : 0;
/*     */ 
/* 145 */     ByteBuffer localByteBuffer1 = this.bb.duplicate();
/* 146 */     localByteBuffer1.limit(ix(j));
/* 147 */     localByteBuffer1.position(ix(0));
/* 148 */     ByteBuffer localByteBuffer2 = localByteBuffer1.slice();
/* 149 */     localByteBuffer2.position(i << 2);
/* 150 */     localByteBuffer2.compact();
/* 151 */     position(k);
/* 152 */     limit(capacity());
/* 153 */     discardMark();
/* 154 */     return this;
/*     */   }
/*     */ 
/*     */   public boolean isDirect()
/*     */   {
/* 161 */     return this.bb.isDirect();
/*     */   }
/*     */ 
/*     */   public boolean isReadOnly() {
/* 165 */     return false;
/*     */   }
/*     */ 
/*     */   public ByteOrder order()
/*     */   {
/* 215 */     return ByteOrder.LITTLE_ENDIAN;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.ByteBufferAsFloatBufferL
 * JD-Core Version:    0.6.2
 */