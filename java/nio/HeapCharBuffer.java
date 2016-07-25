/*     */ package java.nio;
/*     */ 
/*     */ class HeapCharBuffer extends CharBuffer
/*     */ {
/*     */   HeapCharBuffer(int paramInt1, int paramInt2)
/*     */   {
/*  57 */     super(-1, 0, paramInt2, paramInt1, new char[paramInt1], 0);
/*     */   }
/*     */ 
/*     */   HeapCharBuffer(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */   {
/*  70 */     super(-1, paramInt1, paramInt1 + paramInt2, paramArrayOfChar.length, paramArrayOfChar, 0);
/*     */   }
/*     */ 
/*     */   protected HeapCharBuffer(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/*  86 */     super(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfChar, paramInt5);
/*     */   }
/*     */ 
/*     */   public CharBuffer slice()
/*     */   {
/*  98 */     return new HeapCharBuffer(this.hb, -1, 0, remaining(), remaining(), position() + this.offset);
/*     */   }
/*     */ 
/*     */   public CharBuffer duplicate()
/*     */   {
/* 107 */     return new HeapCharBuffer(this.hb, markValue(), position(), limit(), capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   public CharBuffer asReadOnlyBuffer()
/*     */   {
/* 117 */     return new HeapCharBufferR(this.hb, markValue(), position(), limit(), capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   protected int ix(int paramInt)
/*     */   {
/* 131 */     return paramInt + this.offset;
/*     */   }
/*     */ 
/*     */   public char get() {
/* 135 */     return this.hb[ix(nextGetIndex())];
/*     */   }
/*     */ 
/*     */   public char get(int paramInt) {
/* 139 */     return this.hb[ix(checkIndex(paramInt))];
/*     */   }
/*     */ 
/*     */   public CharBuffer get(char[] paramArrayOfChar, int paramInt1, int paramInt2) {
/* 143 */     checkBounds(paramInt1, paramInt2, paramArrayOfChar.length);
/* 144 */     if (paramInt2 > remaining())
/* 145 */       throw new BufferUnderflowException();
/* 146 */     System.arraycopy(this.hb, ix(position()), paramArrayOfChar, paramInt1, paramInt2);
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
/*     */   public CharBuffer put(char paramChar)
/*     */   {
/* 163 */     this.hb[ix(nextPutIndex())] = paramChar;
/* 164 */     return this;
/*     */   }
/*     */ 
/*     */   public CharBuffer put(int paramInt, char paramChar)
/*     */   {
/* 172 */     this.hb[ix(checkIndex(paramInt))] = paramChar;
/* 173 */     return this;
/*     */   }
/*     */ 
/*     */   public CharBuffer put(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */   {
/* 181 */     checkBounds(paramInt1, paramInt2, paramArrayOfChar.length);
/* 182 */     if (paramInt2 > remaining())
/* 183 */       throw new BufferOverflowException();
/* 184 */     System.arraycopy(paramArrayOfChar, paramInt1, this.hb, ix(position()), paramInt2);
/* 185 */     position(position() + paramInt2);
/* 186 */     return this;
/*     */   }
/*     */ 
/*     */   public CharBuffer put(CharBuffer paramCharBuffer)
/*     */   {
/* 194 */     if ((paramCharBuffer instanceof HeapCharBuffer)) {
/* 195 */       if (paramCharBuffer == this)
/* 196 */         throw new IllegalArgumentException();
/* 197 */       HeapCharBuffer localHeapCharBuffer = (HeapCharBuffer)paramCharBuffer;
/* 198 */       int j = localHeapCharBuffer.remaining();
/* 199 */       if (j > remaining())
/* 200 */         throw new BufferOverflowException();
/* 201 */       System.arraycopy(localHeapCharBuffer.hb, localHeapCharBuffer.ix(localHeapCharBuffer.position()), this.hb, ix(position()), j);
/*     */ 
/* 203 */       localHeapCharBuffer.position(localHeapCharBuffer.position() + j);
/* 204 */       position(position() + j);
/* 205 */     } else if (paramCharBuffer.isDirect()) {
/* 206 */       int i = paramCharBuffer.remaining();
/* 207 */       if (i > remaining())
/* 208 */         throw new BufferOverflowException();
/* 209 */       paramCharBuffer.get(this.hb, ix(position()), i);
/* 210 */       position(position() + i);
/*     */     } else {
/* 212 */       super.put(paramCharBuffer);
/*     */     }
/* 214 */     return this;
/*     */   }
/*     */ 
/*     */   public CharBuffer compact()
/*     */   {
/* 222 */     System.arraycopy(this.hb, ix(position()), this.hb, ix(0), remaining());
/* 223 */     position(remaining());
/* 224 */     limit(capacity());
/* 225 */     discardMark();
/* 226 */     return this;
/*     */   }
/*     */ 
/*     */   String toString(int paramInt1, int paramInt2)
/*     */   {
/*     */     try
/*     */     {
/* 561 */       return new String(this.hb, paramInt1 + this.offset, paramInt2 - paramInt1); } catch (StringIndexOutOfBoundsException localStringIndexOutOfBoundsException) {
/*     */     }
/* 563 */     throw new IndexOutOfBoundsException();
/*     */   }
/*     */ 
/*     */   public CharBuffer subSequence(int paramInt1, int paramInt2)
/*     */   {
/* 571 */     if ((paramInt1 < 0) || (paramInt2 > length()) || (paramInt1 > paramInt2))
/*     */     {
/* 574 */       throw new IndexOutOfBoundsException();
/* 575 */     }int i = position();
/* 576 */     return new HeapCharBuffer(this.hb, -1, i + paramInt1, i + paramInt2, capacity(), this.offset);
/*     */   }
/*     */ 
/*     */   public ByteOrder order()
/*     */   {
/* 590 */     return ByteOrder.nativeOrder();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.HeapCharBuffer
 * JD-Core Version:    0.6.2
 */