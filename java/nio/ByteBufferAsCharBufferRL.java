/*     */ package java.nio;
/*     */ 
/*     */ class ByteBufferAsCharBufferRL extends ByteBufferAsCharBufferL
/*     */ {
/*     */   ByteBufferAsCharBufferRL(ByteBuffer paramByteBuffer)
/*     */   {
/*  55 */     super(paramByteBuffer);
/*     */   }
/*     */ 
/*     */   ByteBufferAsCharBufferRL(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/*  68 */     super(paramByteBuffer, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
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
/*  79 */     return new ByteBufferAsCharBufferRL(this.bb, -1, 0, k, k, m);
/*     */   }
/*     */ 
/*     */   public CharBuffer duplicate() {
/*  83 */     return new ByteBufferAsCharBufferRL(this.bb, markValue(), position(), limit(), capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   public CharBuffer asReadOnlyBuffer()
/*     */   {
/* 100 */     return duplicate();
/*     */   }
/*     */ 
/*     */   public CharBuffer put(char paramChar)
/*     */   {
/* 125 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public CharBuffer put(int paramInt, char paramChar)
/*     */   {
/* 134 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public CharBuffer compact()
/*     */   {
/* 156 */     throw new ReadOnlyBufferException();
/*     */   }
/*     */ 
/*     */   public boolean isDirect()
/*     */   {
/* 161 */     return this.bb.isDirect();
/*     */   }
/*     */ 
/*     */   public boolean isReadOnly() {
/* 165 */     return true;
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
/* 199 */     return new ByteBufferAsCharBufferRL(this.bb, -1, i + paramInt1, i + paramInt2, capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   public ByteOrder order()
/*     */   {
/* 215 */     return ByteOrder.LITTLE_ENDIAN;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.ByteBufferAsCharBufferRL
 * JD-Core Version:    0.6.2
 */