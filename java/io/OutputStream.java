/*     */ package java.io;
/*     */ 
/*     */ public abstract class OutputStream
/*     */   implements Closeable, Flushable
/*     */ {
/*     */   public abstract void write(int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/*  75 */     write(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 107 */     if (paramArrayOfByte == null)
/* 108 */       throw new NullPointerException();
/* 109 */     if ((paramInt1 < 0) || (paramInt1 > paramArrayOfByte.length) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfByte.length) || (paramInt1 + paramInt2 < 0))
/*     */     {
/* 111 */       throw new IndexOutOfBoundsException();
/* 112 */     }if (paramInt2 == 0) {
/* 113 */       return;
/*     */     }
/* 115 */     for (int i = 0; i < paramInt2; i++)
/* 116 */       write(paramArrayOfByte[(paramInt1 + i)]);
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.OutputStream
 * JD-Core Version:    0.6.2
 */