/*     */ package java.io;
/*     */ 
/*     */ public class FilterInputStream extends InputStream
/*     */ {
/*     */   protected volatile InputStream in;
/*     */ 
/*     */   protected FilterInputStream(InputStream paramInputStream)
/*     */   {
/*  62 */     this.in = paramInputStream;
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/*  83 */     return this.in.read();
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 107 */     return read(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 133 */     return this.in.read(paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public long skip(long paramLong)
/*     */     throws IOException
/*     */   {
/* 151 */     return this.in.skip(paramLong);
/*     */   }
/*     */ 
/*     */   public int available()
/*     */     throws IOException
/*     */   {
/* 168 */     return this.in.available();
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 181 */     this.in.close();
/*     */   }
/*     */ 
/*     */   public synchronized void mark(int paramInt)
/*     */   {
/* 201 */     this.in.mark(paramInt);
/*     */   }
/*     */ 
/*     */   public synchronized void reset()
/*     */     throws IOException
/*     */   {
/* 226 */     this.in.reset();
/*     */   }
/*     */ 
/*     */   public boolean markSupported()
/*     */   {
/* 243 */     return this.in.markSupported();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.FilterInputStream
 * JD-Core Version:    0.6.2
 */