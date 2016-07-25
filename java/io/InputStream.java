/*     */ package java.io;
/*     */ 
/*     */ public abstract class InputStream
/*     */   implements Closeable
/*     */ {
/*     */   private static final int MAX_SKIP_BUFFER_SIZE = 2048;
/*     */ 
/*     */   public abstract int read()
/*     */     throws IOException;
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 101 */     return read(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 162 */     if (paramArrayOfByte == null)
/* 163 */       throw new NullPointerException();
/* 164 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt2 > paramArrayOfByte.length - paramInt1))
/* 165 */       throw new IndexOutOfBoundsException();
/* 166 */     if (paramInt2 == 0) {
/* 167 */       return 0;
/*     */     }
/*     */ 
/* 170 */     int i = read();
/* 171 */     if (i == -1) {
/* 172 */       return -1;
/*     */     }
/* 174 */     paramArrayOfByte[paramInt1] = ((byte)i);
/*     */ 
/* 176 */     int j = 1;
/*     */     try {
/* 178 */       for (; j < paramInt2; j++) {
/* 179 */         i = read();
/* 180 */         if (i == -1) {
/*     */           break;
/*     */         }
/* 183 */         paramArrayOfByte[(paramInt1 + j)] = ((byte)i);
/*     */       }
/*     */     } catch (IOException localIOException) {
/*     */     }
/* 187 */     return j;
/*     */   }
/*     */ 
/*     */   public long skip(long paramLong)
/*     */     throws IOException
/*     */   {
/* 212 */     long l = paramLong;
/*     */ 
/* 215 */     if (paramLong <= 0L) {
/* 216 */       return 0L;
/*     */     }
/*     */ 
/* 219 */     int j = (int)Math.min(2048L, l);
/* 220 */     byte[] arrayOfByte = new byte[j];
/* 221 */     while (l > 0L) {
/* 222 */       int i = read(arrayOfByte, 0, (int)Math.min(j, l));
/* 223 */       if (i < 0) {
/*     */         break;
/*     */       }
/* 226 */       l -= i;
/*     */     }
/*     */ 
/* 229 */     return paramLong - l;
/*     */   }
/*     */ 
/*     */   public int available()
/*     */     throws IOException
/*     */   {
/* 259 */     return 0;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   public synchronized void mark(int paramInt)
/*     */   {
/*     */   }
/*     */ 
/*     */   public synchronized void reset()
/*     */     throws IOException
/*     */   {
/* 347 */     throw new IOException("mark/reset not supported");
/*     */   }
/*     */ 
/*     */   public boolean markSupported()
/*     */   {
/* 363 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.InputStream
 * JD-Core Version:    0.6.2
 */