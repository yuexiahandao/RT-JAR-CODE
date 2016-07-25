/*     */ package sun.rmi.log;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class LogInputStream extends InputStream
/*     */ {
/*     */   private InputStream in;
/*     */   private int length;
/*     */ 
/*     */   public LogInputStream(InputStream paramInputStream, int paramInt)
/*     */     throws IOException
/*     */   {
/*  43 */     this.in = paramInputStream;
/*  44 */     this.length = paramInt;
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/*  55 */     if (this.length == 0)
/*  56 */       return -1;
/*  57 */     int i = this.in.read();
/*  58 */     this.length = (i != -1 ? this.length - 1 : 0);
/*  59 */     return i;
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/*  71 */     return read(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/*  85 */     if (this.length == 0)
/*  86 */       return -1;
/*  87 */     paramInt2 = this.length < paramInt2 ? this.length : paramInt2;
/*  88 */     int i = this.in.read(paramArrayOfByte, paramInt1, paramInt2);
/*  89 */     this.length = (i != -1 ? this.length - i : 0);
/*  90 */     return i;
/*     */   }
/*     */ 
/*     */   public long skip(long paramLong)
/*     */     throws IOException
/*     */   {
/* 100 */     if (paramLong > 2147483647L)
/* 101 */       throw new IOException("Too many bytes to skip - " + paramLong);
/* 102 */     if (this.length == 0)
/* 103 */       return 0L;
/* 104 */     paramLong = this.length < paramLong ? this.length : paramLong;
/* 105 */     paramLong = this.in.skip(paramLong);
/* 106 */     this.length = ((int)(this.length - paramLong));
/* 107 */     return paramLong;
/*     */   }
/*     */ 
/*     */   public int available()
/*     */     throws IOException
/*     */   {
/* 116 */     int i = this.in.available();
/* 117 */     return this.length < i ? this.length : i;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 125 */     this.length = 0;
/*     */   }
/*     */ 
/*     */   protected void finalize()
/*     */     throws IOException
/*     */   {
/* 132 */     close();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.log.LogInputStream
 * JD-Core Version:    0.6.2
 */