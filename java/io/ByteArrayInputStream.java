/*     */ package java.io;
/*     */ 
/*     */ public class ByteArrayInputStream extends InputStream
/*     */ {
/*     */   protected byte[] buf;
/*     */   protected int pos;
/*  78 */   protected int mark = 0;
/*     */   protected int count;
/*     */ 
/*     */   public ByteArrayInputStream(byte[] paramArrayOfByte)
/*     */   {
/* 104 */     this.buf = paramArrayOfByte;
/* 105 */     this.pos = 0;
/* 106 */     this.count = paramArrayOfByte.length;
/*     */   }
/*     */ 
/*     */   public ByteArrayInputStream(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 124 */     this.buf = paramArrayOfByte;
/* 125 */     this.pos = paramInt1;
/* 126 */     this.count = Math.min(paramInt1 + paramInt2, paramArrayOfByte.length);
/* 127 */     this.mark = paramInt1;
/*     */   }
/*     */ 
/*     */   public synchronized int read()
/*     */   {
/* 144 */     return this.pos < this.count ? this.buf[(this.pos++)] & 0xFF : -1;
/*     */   }
/*     */ 
/*     */   public synchronized int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 177 */     if (paramArrayOfByte == null)
/* 178 */       throw new NullPointerException();
/* 179 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt2 > paramArrayOfByte.length - paramInt1)) {
/* 180 */       throw new IndexOutOfBoundsException();
/*     */     }
/*     */ 
/* 183 */     if (this.pos >= this.count) {
/* 184 */       return -1;
/*     */     }
/*     */ 
/* 187 */     int i = this.count - this.pos;
/* 188 */     if (paramInt2 > i) {
/* 189 */       paramInt2 = i;
/*     */     }
/* 191 */     if (paramInt2 <= 0) {
/* 192 */       return 0;
/*     */     }
/* 194 */     System.arraycopy(this.buf, this.pos, paramArrayOfByte, paramInt1, paramInt2);
/* 195 */     this.pos += paramInt2;
/* 196 */     return paramInt2;
/*     */   }
/*     */ 
/*     */   public synchronized long skip(long paramLong)
/*     */   {
/* 212 */     long l = this.count - this.pos;
/* 213 */     if (paramLong < l) {
/* 214 */       l = paramLong < 0L ? 0L : paramLong;
/*     */     }
/*     */ 
/* 217 */     this.pos = ((int)(this.pos + l));
/* 218 */     return l;
/*     */   }
/*     */ 
/*     */   public synchronized int available()
/*     */   {
/* 232 */     return this.count - this.pos;
/*     */   }
/*     */ 
/*     */   public boolean markSupported()
/*     */   {
/* 243 */     return true;
/*     */   }
/*     */ 
/*     */   public void mark(int paramInt)
/*     */   {
/* 262 */     this.mark = this.pos;
/*     */   }
/*     */ 
/*     */   public synchronized void reset()
/*     */   {
/* 271 */     this.pos = this.mark;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.ByteArrayInputStream
 * JD-Core Version:    0.6.2
 */