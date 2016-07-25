/*     */ package java.io;
/*     */ 
/*     */ import java.nio.CharBuffer;
/*     */ 
/*     */ public abstract class Reader
/*     */   implements Readable, Closeable
/*     */ {
/*     */   protected Object lock;
/*     */   private static final int maxSkipBufferSize = 8192;
/* 163 */   private char[] skipBuffer = null;
/*     */ 
/*     */   protected Reader()
/*     */   {
/*  67 */     this.lock = this;
/*     */   }
/*     */ 
/*     */   protected Reader(Object paramObject)
/*     */   {
/*  77 */     if (paramObject == null) {
/*  78 */       throw new NullPointerException();
/*     */     }
/*  80 */     this.lock = paramObject;
/*     */   }
/*     */ 
/*     */   public int read(CharBuffer paramCharBuffer)
/*     */     throws IOException
/*     */   {
/*  98 */     int i = paramCharBuffer.remaining();
/*  99 */     char[] arrayOfChar = new char[i];
/* 100 */     int j = read(arrayOfChar, 0, i);
/* 101 */     if (j > 0)
/* 102 */       paramCharBuffer.put(arrayOfChar, 0, j);
/* 103 */     return j;
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/* 120 */     char[] arrayOfChar = new char[1];
/* 121 */     if (read(arrayOfChar, 0, 1) == -1) {
/* 122 */       return -1;
/*     */     }
/* 124 */     return arrayOfChar[0];
/*     */   }
/*     */ 
/*     */   public int read(char[] paramArrayOfChar)
/*     */     throws IOException
/*     */   {
/* 140 */     return read(paramArrayOfChar, 0, paramArrayOfChar.length);
/*     */   }
/*     */ 
/*     */   public abstract int read(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */ 
/*     */   public long skip(long paramLong)
/*     */     throws IOException
/*     */   {
/* 177 */     if (paramLong < 0L)
/* 178 */       throw new IllegalArgumentException("skip value is negative");
/* 179 */     int i = (int)Math.min(paramLong, 8192L);
/* 180 */     synchronized (this.lock) {
/* 181 */       if ((this.skipBuffer == null) || (this.skipBuffer.length < i))
/* 182 */         this.skipBuffer = new char[i];
/* 183 */       long l = paramLong;
/* 184 */       while (l > 0L) {
/* 185 */         int j = read(this.skipBuffer, 0, (int)Math.min(l, i));
/* 186 */         if (j == -1)
/*     */           break;
/* 188 */         l -= j;
/*     */       }
/* 190 */       return paramLong - l;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean ready()
/*     */     throws IOException
/*     */   {
/* 204 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean markSupported()
/*     */   {
/* 215 */     return false;
/*     */   }
/*     */ 
/*     */   public void mark(int paramInt)
/*     */     throws IOException
/*     */   {
/* 232 */     throw new IOException("mark() not supported");
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */     throws IOException
/*     */   {
/* 249 */     throw new IOException("reset() not supported");
/*     */   }
/*     */ 
/*     */   public abstract void close()
/*     */     throws IOException;
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.Reader
 * JD-Core Version:    0.6.2
 */