/*     */ package java.nio;
/*     */ 
/*     */ class HeapByteBuffer extends ByteBuffer
/*     */ {
/*     */   HeapByteBuffer(int paramInt1, int paramInt2)
/*     */   {
/*  57 */     super(-1, 0, paramInt2, paramInt1, new byte[paramInt1], 0);
/*     */   }
/*     */ 
/*     */   HeapByteBuffer(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/*  70 */     super(-1, paramInt1, paramInt1 + paramInt2, paramArrayOfByte.length, paramArrayOfByte, 0);
/*     */   }
/*     */ 
/*     */   protected HeapByteBuffer(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/*  86 */     super(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte, paramInt5);
/*     */   }
/*     */ 
/*     */   public ByteBuffer slice()
/*     */   {
/*  98 */     return new HeapByteBuffer(this.hb, -1, 0, remaining(), remaining(), position() + this.offset);
/*     */   }
/*     */ 
/*     */   public ByteBuffer duplicate()
/*     */   {
/* 107 */     return new HeapByteBuffer(this.hb, markValue(), position(), limit(), capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   public ByteBuffer asReadOnlyBuffer()
/*     */   {
/* 117 */     return new HeapByteBufferR(this.hb, markValue(), position(), limit(), capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   protected int ix(int paramInt)
/*     */   {
/* 131 */     return paramInt + this.offset;
/*     */   }
/*     */ 
/*     */   public byte get() {
/* 135 */     return this.hb[ix(nextGetIndex())];
/*     */   }
/*     */ 
/*     */   public byte get(int paramInt) {
/* 139 */     return this.hb[ix(checkIndex(paramInt))];
/*     */   }
/*     */ 
/*     */   public ByteBuffer get(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/* 143 */     checkBounds(paramInt1, paramInt2, paramArrayOfByte.length);
/* 144 */     if (paramInt2 > remaining())
/* 145 */       throw new BufferUnderflowException();
/* 146 */     System.arraycopy(this.hb, ix(position()), paramArrayOfByte, paramInt1, paramInt2);
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
/*     */   public ByteBuffer put(byte paramByte)
/*     */   {
/* 163 */     this.hb[ix(nextPutIndex())] = paramByte;
/* 164 */     return this;
/*     */   }
/*     */ 
/*     */   public ByteBuffer put(int paramInt, byte paramByte)
/*     */   {
/* 172 */     this.hb[ix(checkIndex(paramInt))] = paramByte;
/* 173 */     return this;
/*     */   }
/*     */ 
/*     */   public ByteBuffer put(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 181 */     checkBounds(paramInt1, paramInt2, paramArrayOfByte.length);
/* 182 */     if (paramInt2 > remaining())
/* 183 */       throw new BufferOverflowException();
/* 184 */     System.arraycopy(paramArrayOfByte, paramInt1, this.hb, ix(position()), paramInt2);
/* 185 */     position(position() + paramInt2);
/* 186 */     return this;
/*     */   }
/*     */ 
/*     */   public ByteBuffer put(ByteBuffer paramByteBuffer)
/*     */   {
/* 194 */     if ((paramByteBuffer instanceof HeapByteBuffer)) {
/* 195 */       if (paramByteBuffer == this)
/* 196 */         throw new IllegalArgumentException();
/* 197 */       HeapByteBuffer localHeapByteBuffer = (HeapByteBuffer)paramByteBuffer;
/* 198 */       int j = localHeapByteBuffer.remaining();
/* 199 */       if (j > remaining())
/* 200 */         throw new BufferOverflowException();
/* 201 */       System.arraycopy(localHeapByteBuffer.hb, localHeapByteBuffer.ix(localHeapByteBuffer.position()), this.hb, ix(position()), j);
/*     */ 
/* 203 */       localHeapByteBuffer.position(localHeapByteBuffer.position() + j);
/* 204 */       position(position() + j);
/* 205 */     } else if (paramByteBuffer.isDirect()) {
/* 206 */       int i = paramByteBuffer.remaining();
/* 207 */       if (i > remaining())
/* 208 */         throw new BufferOverflowException();
/* 209 */       paramByteBuffer.get(this.hb, ix(position()), i);
/* 210 */       position(position() + i);
/*     */     } else {
/* 212 */       super.put(paramByteBuffer);
/*     */     }
/* 214 */     return this;
/*     */   }
/*     */ 
/*     */   public ByteBuffer compact()
/*     */   {
/* 222 */     System.arraycopy(this.hb, ix(position()), this.hb, ix(0), remaining());
/* 223 */     position(remaining());
/* 224 */     limit(capacity());
/* 225 */     discardMark();
/* 226 */     return this;
/*     */   }
/*     */ 
/*     */   byte _get(int paramInt)
/*     */   {
/* 237 */     return this.hb[paramInt];
/*     */   }
/*     */ 
/*     */   void _put(int paramInt, byte paramByte)
/*     */   {
/* 242 */     this.hb[paramInt] = paramByte;
/*     */   }
/*     */ 
/*     */   public char getChar()
/*     */   {
/* 253 */     return Bits.getChar(this, ix(nextGetIndex(2)), this.bigEndian);
/*     */   }
/*     */ 
/*     */   public char getChar(int paramInt) {
/* 257 */     return Bits.getChar(this, ix(checkIndex(paramInt, 2)), this.bigEndian);
/*     */   }
/*     */ 
/*     */   public ByteBuffer putChar(char paramChar)
/*     */   {
/* 264 */     Bits.putChar(this, ix(nextPutIndex(2)), paramChar, this.bigEndian);
/* 265 */     return this;
/*     */   }
/*     */ 
/*     */   public ByteBuffer putChar(int paramInt, char paramChar)
/*     */   {
/* 273 */     Bits.putChar(this, ix(checkIndex(paramInt, 2)), paramChar, this.bigEndian);
/* 274 */     return this;
/*     */   }
/*     */ 
/*     */   public CharBuffer asCharBuffer()
/*     */   {
/* 281 */     int i = remaining() >> 1;
/* 282 */     int j = this.offset + position();
/* 283 */     return this.bigEndian ? new ByteBufferAsCharBufferB(this, -1, 0, i, i, j) : new ByteBufferAsCharBufferL(this, -1, 0, i, i, j);
/*     */   }
/*     */ 
/*     */   public short getShort()
/*     */   {
/* 304 */     return Bits.getShort(this, ix(nextGetIndex(2)), this.bigEndian);
/*     */   }
/*     */ 
/*     */   public short getShort(int paramInt) {
/* 308 */     return Bits.getShort(this, ix(checkIndex(paramInt, 2)), this.bigEndian);
/*     */   }
/*     */ 
/*     */   public ByteBuffer putShort(short paramShort)
/*     */   {
/* 315 */     Bits.putShort(this, ix(nextPutIndex(2)), paramShort, this.bigEndian);
/* 316 */     return this;
/*     */   }
/*     */ 
/*     */   public ByteBuffer putShort(int paramInt, short paramShort)
/*     */   {
/* 324 */     Bits.putShort(this, ix(checkIndex(paramInt, 2)), paramShort, this.bigEndian);
/* 325 */     return this;
/*     */   }
/*     */ 
/*     */   public ShortBuffer asShortBuffer()
/*     */   {
/* 332 */     int i = remaining() >> 1;
/* 333 */     int j = this.offset + position();
/* 334 */     return this.bigEndian ? new ByteBufferAsShortBufferB(this, -1, 0, i, i, j) : new ByteBufferAsShortBufferL(this, -1, 0, i, i, j);
/*     */   }
/*     */ 
/*     */   public int getInt()
/*     */   {
/* 355 */     return Bits.getInt(this, ix(nextGetIndex(4)), this.bigEndian);
/*     */   }
/*     */ 
/*     */   public int getInt(int paramInt) {
/* 359 */     return Bits.getInt(this, ix(checkIndex(paramInt, 4)), this.bigEndian);
/*     */   }
/*     */ 
/*     */   public ByteBuffer putInt(int paramInt)
/*     */   {
/* 366 */     Bits.putInt(this, ix(nextPutIndex(4)), paramInt, this.bigEndian);
/* 367 */     return this;
/*     */   }
/*     */ 
/*     */   public ByteBuffer putInt(int paramInt1, int paramInt2)
/*     */   {
/* 375 */     Bits.putInt(this, ix(checkIndex(paramInt1, 4)), paramInt2, this.bigEndian);
/* 376 */     return this;
/*     */   }
/*     */ 
/*     */   public IntBuffer asIntBuffer()
/*     */   {
/* 383 */     int i = remaining() >> 2;
/* 384 */     int j = this.offset + position();
/* 385 */     return this.bigEndian ? new ByteBufferAsIntBufferB(this, -1, 0, i, i, j) : new ByteBufferAsIntBufferL(this, -1, 0, i, i, j);
/*     */   }
/*     */ 
/*     */   public long getLong()
/*     */   {
/* 406 */     return Bits.getLong(this, ix(nextGetIndex(8)), this.bigEndian);
/*     */   }
/*     */ 
/*     */   public long getLong(int paramInt) {
/* 410 */     return Bits.getLong(this, ix(checkIndex(paramInt, 8)), this.bigEndian);
/*     */   }
/*     */ 
/*     */   public ByteBuffer putLong(long paramLong)
/*     */   {
/* 417 */     Bits.putLong(this, ix(nextPutIndex(8)), paramLong, this.bigEndian);
/* 418 */     return this;
/*     */   }
/*     */ 
/*     */   public ByteBuffer putLong(int paramInt, long paramLong)
/*     */   {
/* 426 */     Bits.putLong(this, ix(checkIndex(paramInt, 8)), paramLong, this.bigEndian);
/* 427 */     return this;
/*     */   }
/*     */ 
/*     */   public LongBuffer asLongBuffer()
/*     */   {
/* 434 */     int i = remaining() >> 3;
/* 435 */     int j = this.offset + position();
/* 436 */     return this.bigEndian ? new ByteBufferAsLongBufferB(this, -1, 0, i, i, j) : new ByteBufferAsLongBufferL(this, -1, 0, i, i, j);
/*     */   }
/*     */ 
/*     */   public float getFloat()
/*     */   {
/* 457 */     return Bits.getFloat(this, ix(nextGetIndex(4)), this.bigEndian);
/*     */   }
/*     */ 
/*     */   public float getFloat(int paramInt) {
/* 461 */     return Bits.getFloat(this, ix(checkIndex(paramInt, 4)), this.bigEndian);
/*     */   }
/*     */ 
/*     */   public ByteBuffer putFloat(float paramFloat)
/*     */   {
/* 468 */     Bits.putFloat(this, ix(nextPutIndex(4)), paramFloat, this.bigEndian);
/* 469 */     return this;
/*     */   }
/*     */ 
/*     */   public ByteBuffer putFloat(int paramInt, float paramFloat)
/*     */   {
/* 477 */     Bits.putFloat(this, ix(checkIndex(paramInt, 4)), paramFloat, this.bigEndian);
/* 478 */     return this;
/*     */   }
/*     */ 
/*     */   public FloatBuffer asFloatBuffer()
/*     */   {
/* 485 */     int i = remaining() >> 2;
/* 486 */     int j = this.offset + position();
/* 487 */     return this.bigEndian ? new ByteBufferAsFloatBufferB(this, -1, 0, i, i, j) : new ByteBufferAsFloatBufferL(this, -1, 0, i, i, j);
/*     */   }
/*     */ 
/*     */   public double getDouble()
/*     */   {
/* 508 */     return Bits.getDouble(this, ix(nextGetIndex(8)), this.bigEndian);
/*     */   }
/*     */ 
/*     */   public double getDouble(int paramInt) {
/* 512 */     return Bits.getDouble(this, ix(checkIndex(paramInt, 8)), this.bigEndian);
/*     */   }
/*     */ 
/*     */   public ByteBuffer putDouble(double paramDouble)
/*     */   {
/* 519 */     Bits.putDouble(this, ix(nextPutIndex(8)), paramDouble, this.bigEndian);
/* 520 */     return this;
/*     */   }
/*     */ 
/*     */   public ByteBuffer putDouble(int paramInt, double paramDouble)
/*     */   {
/* 528 */     Bits.putDouble(this, ix(checkIndex(paramInt, 8)), paramDouble, this.bigEndian);
/* 529 */     return this;
/*     */   }
/*     */ 
/*     */   public DoubleBuffer asDoubleBuffer()
/*     */   {
/* 536 */     int i = remaining() >> 3;
/* 537 */     int j = this.offset + position();
/* 538 */     return this.bigEndian ? new ByteBufferAsDoubleBufferB(this, -1, 0, i, i, j) : new ByteBufferAsDoubleBufferL(this, -1, 0, i, i, j);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.HeapByteBuffer
 * JD-Core Version:    0.6.2
 */