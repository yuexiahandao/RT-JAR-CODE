/*     */ package java.io;
/*     */ 
/*     */ public class FilterOutputStream extends OutputStream
/*     */ {
/*     */   protected OutputStream out;
/*     */ 
/*     */   public FilterOutputStream(OutputStream paramOutputStream)
/*     */   {
/*  61 */     this.out = paramOutputStream;
/*     */   }
/*     */ 
/*     */   public void write(int paramInt)
/*     */     throws IOException
/*     */   {
/*  77 */     this.out.write(paramInt);
/*     */   }
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/*  97 */     write(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 121 */     if ((paramInt1 | paramInt2 | paramArrayOfByte.length - (paramInt2 + paramInt1) | paramInt1 + paramInt2) < 0) {
/* 122 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 124 */     for (int i = 0; i < paramInt2; i++)
/* 125 */       write(paramArrayOfByte[(paramInt1 + i)]);
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/* 140 */     this.out.flush();
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 157 */       flush();
/*     */     } catch (IOException localIOException) {
/*     */     }
/* 160 */     this.out.close();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.FilterOutputStream
 * JD-Core Version:    0.6.2
 */