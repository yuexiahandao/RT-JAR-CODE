/*     */ package java.nio;
/*     */ 
/*     */ class ByteBufferAsDoubleBufferB extends DoubleBuffer
/*     */ {
/*     */   protected final ByteBuffer bb;
/*     */   protected final int offset;
/*     */ 
/*     */   ByteBufferAsDoubleBufferB(ByteBuffer paramByteBuffer)
/*     */   {
/*  44 */     super(-1, 0, paramByteBuffer.remaining() >> 3, paramByteBuffer.remaining() >> 3);
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
/*     */   ByteBufferAsDoubleBufferB(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/*  64 */     super(paramInt1, paramInt2, paramInt3, paramInt4);
/*  65 */     this.bb = paramByteBuffer;
/*  66 */     this.offset = paramInt5;
/*     */   }
/*     */ 
/*     */   public DoubleBuffer slice()
/*     */   {
/*  73 */     int i = position();
/*  74 */     int j = limit();
/*  75 */     assert (i <= j);
/*  76 */     int k = i <= j ? j - i : 0;
/*  77 */     int m = (i << 3) + this.offset;
/*  78 */     assert (m >= 0);
/*  79 */     return new ByteBufferAsDoubleBufferB(this.bb, -1, 0, k, k, m);
/*     */   }
/*     */ 
/*     */   public DoubleBuffer duplicate() {
/*  83 */     return new ByteBufferAsDoubleBufferB(this.bb, markValue(), position(), limit(), capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   public DoubleBuffer asReadOnlyBuffer()
/*     */   {
/*  93 */     return new ByteBufferAsDoubleBufferRB(this.bb, markValue(), position(), limit(), capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   protected int ix(int paramInt)
/*     */   {
/* 107 */     return (paramInt << 3) + this.offset;
/*     */   }
/*     */ 
/*     */   public double get() {
/* 111 */     return Bits.getDoubleB(this.bb, ix(nextGetIndex()));
/*     */   }
/*     */ 
/*     */   public double get(int paramInt) {
/* 115 */     return Bits.getDoubleB(this.bb, ix(checkIndex(paramInt)));
/*     */   }
/*     */ 
/*     */   public DoubleBuffer put(double paramDouble)
/*     */   {
/* 122 */     Bits.putDoubleB(this.bb, ix(nextPutIndex()), paramDouble);
/* 123 */     return this;
/*     */   }
/*     */ 
/*     */   public DoubleBuffer put(int paramInt, double paramDouble)
/*     */   {
/* 131 */     Bits.putDoubleB(this.bb, ix(checkIndex(paramInt)), paramDouble);
/* 132 */     return this;
/*     */   }
/*     */ 
/*     */   public DoubleBuffer compact()
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
/* 149 */     localByteBuffer2.position(i << 3);
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
/* 212 */     return ByteOrder.BIG_ENDIAN;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.ByteBufferAsDoubleBufferB
 * JD-Core Version:    0.6.2
 */