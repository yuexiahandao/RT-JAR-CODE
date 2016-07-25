/*     */ package java.nio;
/*     */ 
/*     */ class ByteBufferAsCharBufferL extends CharBuffer
/*     */ {
/*     */   protected final ByteBuffer bb;
/*     */   protected final int offset;
/*     */ 
/*     */   ByteBufferAsCharBufferL(ByteBuffer paramByteBuffer)
/*     */   {
/*  44 */     super(-1, 0, paramByteBuffer.remaining() >> 1, paramByteBuffer.remaining() >> 1);
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
/*     */   ByteBufferAsCharBufferL(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/*  64 */     super(paramInt1, paramInt2, paramInt3, paramInt4);
/*  65 */     this.bb = paramByteBuffer;
/*  66 */     this.offset = paramInt5;
/*     */   }
/*     */ 
/*     */   public CharBuffer slice()
/*     */   {
/*  73 */     int i = position();
/*  74 */     int j = limit();
/*  75 */     assert (i <= j);
/*  76 */     int k = i <= j ? j - i : 0;
/*  77 */     int m = (i << 1) + this.offset;
/*  78 */     assert (m >= 0);
/*  79 */     return new ByteBufferAsCharBufferL(this.bb, -1, 0, k, k, m);
/*     */   }
/*     */ 
/*     */   public CharBuffer duplicate() {
/*  83 */     return new ByteBufferAsCharBufferL(this.bb, markValue(), position(), limit(), capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   public CharBuffer asReadOnlyBuffer()
/*     */   {
/*  93 */     return new ByteBufferAsCharBufferRL(this.bb, markValue(), position(), limit(), capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   protected int ix(int paramInt)
/*     */   {
/* 107 */     return (paramInt << 1) + this.offset;
/*     */   }
/*     */ 
/*     */   public char get() {
/* 111 */     return Bits.getCharL(this.bb, ix(nextGetIndex()));
/*     */   }
/*     */ 
/*     */   public char get(int paramInt) {
/* 115 */     return Bits.getCharL(this.bb, ix(checkIndex(paramInt)));
/*     */   }
/*     */ 
/*     */   public CharBuffer put(char paramChar)
/*     */   {
/* 122 */     Bits.putCharL(this.bb, ix(nextPutIndex()), paramChar);
/* 123 */     return this;
/*     */   }
/*     */ 
/*     */   public CharBuffer put(int paramInt, char paramChar)
/*     */   {
/* 131 */     Bits.putCharL(this.bb, ix(checkIndex(paramInt)), paramChar);
/* 132 */     return this;
/*     */   }
/*     */ 
/*     */   public CharBuffer compact()
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
/* 149 */     localByteBuffer2.position(i << 1);
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
/*     */   public String toString(int paramInt1, int paramInt2)
/*     */   {
/* 171 */     if ((paramInt2 > limit()) || (paramInt1 > paramInt2))
/* 172 */       throw new IndexOutOfBoundsException();
/*     */     try {
/* 174 */       int i = paramInt2 - paramInt1;
/* 175 */       char[] arrayOfChar = new char[i];
/* 176 */       CharBuffer localCharBuffer1 = CharBuffer.wrap(arrayOfChar);
/* 177 */       CharBuffer localCharBuffer2 = duplicate();
/* 178 */       localCharBuffer2.position(paramInt1);
/* 179 */       localCharBuffer2.limit(paramInt2);
/* 180 */       localCharBuffer1.put(localCharBuffer2);
/* 181 */       return new String(arrayOfChar); } catch (StringIndexOutOfBoundsException localStringIndexOutOfBoundsException) {
/*     */     }
/* 183 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */ 
/*     */   public CharBuffer subSequence(int paramInt1, int paramInt2)
/*     */   {
/* 191 */     int i = position();
/* 192 */     int j = limit();
/* 193 */     assert (i <= j);
/* 194 */     i = i <= j ? i : j;
/* 195 */     int k = j - i;
/*     */ 
/* 197 */     if ((paramInt1 < 0) || (paramInt2 > k) || (paramInt1 > paramInt2))
/* 198 */       throw new IndexOutOfBoundsException();
/* 199 */     return new ByteBufferAsCharBufferL(this.bb, -1, i + paramInt1, i + paramInt2, capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   public ByteOrder order()
/*     */   {
/* 215 */     return ByteOrder.LITTLE_ENDIAN;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.ByteBufferAsCharBufferL
 * JD-Core Version:    0.6.2
 */