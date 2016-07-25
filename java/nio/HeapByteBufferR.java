/*     */ package java.nio;
/*     */ 
/*     */ class HeapByteBufferR extends HeapByteBuffer
/*     */ {
/*     */   HeapByteBufferR(int paramInt1, int paramInt2)
/*     */   {
/*  63 */     super(paramInt1, paramInt2);
/*  64 */     this.isReadOnly = true;
/*     */   }
/*     */ 
/*     */   HeapByteBufferR(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/*  76 */     super(paramArrayOfByte, paramInt1, paramInt2);
/*  77 */     this.isReadOnly = true;
/*     */   }
/*     */ 
/*     */   protected HeapByteBufferR(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/*  92 */     super(paramArrayOfByte, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*  93 */     this.isReadOnly = true;
/*     */   }
/*     */ 
/*     */   public ByteBuffer slice()
/*     */   {
/*  98 */     return new HeapByteBufferR(this.hb, -1, 0, remaining(), remaining(), position() + this.offset);
/*     */   }
/*     */ 
/*     */   public ByteBuffer duplicate()
/*     */   {
/* 107 */     return new HeapByteBufferR(this.hb, markValue(), position(), limit(), capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   public ByteBuffer asReadOnlyBuffer()
/*     */   {
/* 124 */     return duplicate();
/*     */   }
/*     */ 
/*     */   public boolean isReadOnly()
/*     */   {
/* 158 */     return true;
/*     */   }
/*     */ 
/*     */   public ByteBuffer put(byte paramByte)
/*     */   {
/* 166 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public ByteBuffer put(int paramInt, byte paramByte)
/*     */   {
/* 175 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public ByteBuffer put(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 188 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public ByteBuffer put(ByteBuffer paramByteBuffer)
/*     */   {
/* 216 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public ByteBuffer compact()
/*     */   {
/* 228 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   byte _get(int paramInt)
/*     */   {
/* 237 */     return this.hb[paramInt];
/*     */   }
/*     */ 
/*     */   void _put(int paramInt, byte paramByte)
/*     */   {
/* 244 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public ByteBuffer putChar(char paramChar)
/*     */   {
/* 267 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public ByteBuffer putChar(int paramInt, char paramChar)
/*     */   {
/* 276 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public CharBuffer asCharBuffer()
/*     */   {
/* 281 */     int i = remaining() >> 1;
/* 282 */     int j = this.offset + position();
/* 283 */     return this.bigEndian ? new ByteBufferAsCharBufferRB(this, -1, 0, i, i, j) : new ByteBufferAsCharBufferRL(this, -1, 0, i, i, j);
/*     */   }
/*     */ 
/*     */   public ByteBuffer putShort(short paramShort)
/*     */   {
/* 318 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public ByteBuffer putShort(int paramInt, short paramShort)
/*     */   {
/* 327 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public ShortBuffer asShortBuffer()
/*     */   {
/* 332 */     int i = remaining() >> 1;
/* 333 */     int j = this.offset + position();
/* 334 */     return this.bigEndian ? new ByteBufferAsShortBufferRB(this, -1, 0, i, i, j) : new ByteBufferAsShortBufferRL(this, -1, 0, i, i, j);
/*     */   }
/*     */ 
/*     */   public ByteBuffer putInt(int paramInt)
/*     */   {
/* 369 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public ByteBuffer putInt(int paramInt1, int paramInt2)
/*     */   {
/* 378 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public IntBuffer asIntBuffer()
/*     */   {
/* 383 */     int i = remaining() >> 2;
/* 384 */     int j = this.offset + position();
/* 385 */     return this.bigEndian ? new ByteBufferAsIntBufferRB(this, -1, 0, i, i, j) : new ByteBufferAsIntBufferRL(this, -1, 0, i, i, j);
/*     */   }
/*     */ 
/*     */   public ByteBuffer putLong(long paramLong)
/*     */   {
/* 420 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public ByteBuffer putLong(int paramInt, long paramLong)
/*     */   {
/* 429 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public LongBuffer asLongBuffer()
/*     */   {
/* 434 */     int i = remaining() >> 3;
/* 435 */     int j = this.offset + position();
/* 436 */     return this.bigEndian ? new ByteBufferAsLongBufferRB(this, -1, 0, i, i, j) : new ByteBufferAsLongBufferRL(this, -1, 0, i, i, j);
/*     */   }
/*     */ 
/*     */   public ByteBuffer putFloat(float paramFloat)
/*     */   {
/* 471 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public ByteBuffer putFloat(int paramInt, float paramFloat)
/*     */   {
/* 480 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public FloatBuffer asFloatBuffer()
/*     */   {
/* 485 */     int i = remaining() >> 2;
/* 486 */     int j = this.offset + position();
/* 487 */     return this.bigEndian ? new ByteBufferAsFloatBufferRB(this, -1, 0, i, i, j) : new ByteBufferAsFloatBufferRL(this, -1, 0, i, i, j);
/*     */   }
/*     */ 
/*     */   public ByteBuffer putDouble(double paramDouble)
/*     */   {
/* 522 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public ByteBuffer putDouble(int paramInt, double paramDouble)
/*     */   {
/* 531 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public DoubleBuffer asDoubleBuffer()
/*     */   {
/* 536 */     int i = remaining() >> 3;
/* 537 */     int j = this.offset + position();
/* 538 */     return this.bigEndian ? new ByteBufferAsDoubleBufferRB(this, -1, 0, i, i, j) : new ByteBufferAsDoubleBufferRL(this, -1, 0, i, i, j);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.HeapByteBufferR
 * JD-Core Version:    0.6.2
 */