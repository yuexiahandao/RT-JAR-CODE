/*     */ package java.io;
/*     */ 
/*     */ public class PushbackInputStream extends FilterInputStream
/*     */ {
/*     */   protected byte[] buf;
/*     */   protected int pos;
/*     */ 
/*     */   private void ensureOpen()
/*     */     throws IOException
/*     */   {
/*  73 */     if (this.in == null)
/*  74 */       throw new IOException("Stream closed");
/*     */   }
/*     */ 
/*     */   public PushbackInputStream(InputStream paramInputStream, int paramInt)
/*     */   {
/*  92 */     super(paramInputStream);
/*  93 */     if (paramInt <= 0) {
/*  94 */       throw new IllegalArgumentException("size <= 0");
/*     */     }
/*  96 */     this.buf = new byte[paramInt];
/*  97 */     this.pos = paramInt;
/*     */   }
/*     */ 
/*     */   public PushbackInputStream(InputStream paramInputStream)
/*     */   {
/* 111 */     this(paramInputStream, 1);
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/* 135 */     ensureOpen();
/* 136 */     if (this.pos < this.buf.length) {
/* 137 */       return this.buf[(this.pos++)] & 0xFF;
/*     */     }
/* 139 */     return super.read();
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 166 */     ensureOpen();
/* 167 */     if (paramArrayOfByte == null)
/* 168 */       throw new NullPointerException();
/* 169 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt2 > paramArrayOfByte.length - paramInt1))
/* 170 */       throw new IndexOutOfBoundsException();
/* 171 */     if (paramInt2 == 0) {
/* 172 */       return 0;
/*     */     }
/*     */ 
/* 175 */     int i = this.buf.length - this.pos;
/* 176 */     if (i > 0) {
/* 177 */       if (paramInt2 < i) {
/* 178 */         i = paramInt2;
/*     */       }
/* 180 */       System.arraycopy(this.buf, this.pos, paramArrayOfByte, paramInt1, i);
/* 181 */       this.pos += i;
/* 182 */       paramInt1 += i;
/* 183 */       paramInt2 -= i;
/*     */     }
/* 185 */     if (paramInt2 > 0) {
/* 186 */       paramInt2 = super.read(paramArrayOfByte, paramInt1, paramInt2);
/* 187 */       if (paramInt2 == -1) {
/* 188 */         return i == 0 ? -1 : i;
/*     */       }
/* 190 */       return i + paramInt2;
/*     */     }
/* 192 */     return i;
/*     */   }
/*     */ 
/*     */   public void unread(int paramInt)
/*     */     throws IOException
/*     */   {
/* 207 */     ensureOpen();
/* 208 */     if (this.pos == 0) {
/* 209 */       throw new IOException("Push back buffer is full");
/*     */     }
/* 211 */     this.buf[(--this.pos)] = ((byte)paramInt);
/*     */   }
/*     */ 
/*     */   public void unread(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 230 */     ensureOpen();
/* 231 */     if (paramInt2 > this.pos) {
/* 232 */       throw new IOException("Push back buffer is full");
/*     */     }
/* 234 */     this.pos -= paramInt2;
/* 235 */     System.arraycopy(paramArrayOfByte, paramInt1, this.buf, this.pos, paramInt2);
/*     */   }
/*     */ 
/*     */   public void unread(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 252 */     unread(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public int available()
/*     */     throws IOException
/*     */   {
/* 275 */     ensureOpen();
/* 276 */     int i = this.buf.length - this.pos;
/* 277 */     int j = super.available();
/* 278 */     return i > 2147483647 - j ? 2147483647 : i + j;
/*     */   }
/*     */ 
/*     */   public long skip(long paramLong)
/*     */     throws IOException
/*     */   {
/* 306 */     ensureOpen();
/* 307 */     if (paramLong <= 0L) {
/* 308 */       return 0L;
/*     */     }
/*     */ 
/* 311 */     long l = this.buf.length - this.pos;
/* 312 */     if (l > 0L) {
/* 313 */       if (paramLong < l) {
/* 314 */         l = paramLong;
/*     */       }
/* 316 */       this.pos = ((int)(this.pos + l));
/* 317 */       paramLong -= l;
/*     */     }
/* 319 */     if (paramLong > 0L) {
/* 320 */       l += super.skip(paramLong);
/*     */     }
/* 322 */     return l;
/*     */   }
/*     */ 
/*     */   public boolean markSupported()
/*     */   {
/* 335 */     return false;
/*     */   }
/*     */ 
/*     */   public synchronized void mark(int paramInt)
/*     */   {
/*     */   }
/*     */ 
/*     */   public synchronized void reset()
/*     */     throws IOException
/*     */   {
/* 364 */     throw new IOException("mark/reset not supported");
/*     */   }
/*     */ 
/*     */   public synchronized void close()
/*     */     throws IOException
/*     */   {
/* 377 */     if (this.in == null)
/* 378 */       return;
/* 379 */     this.in.close();
/* 380 */     this.in = null;
/* 381 */     this.buf = null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.PushbackInputStream
 * JD-Core Version:    0.6.2
 */