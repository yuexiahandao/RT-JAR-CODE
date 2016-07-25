/*     */ package java.util.zip;
/*     */ 
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class CheckedInputStream extends FilterInputStream
/*     */ {
/*     */   private Checksum cksum;
/*     */ 
/*     */   public CheckedInputStream(InputStream paramInputStream, Checksum paramChecksum)
/*     */   {
/*  49 */     super(paramInputStream);
/*  50 */     this.cksum = paramChecksum;
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/*  59 */     int i = this.in.read();
/*  60 */     if (i != -1) {
/*  61 */       this.cksum.update(i);
/*     */     }
/*  63 */     return i;
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/*  82 */     paramInt2 = this.in.read(paramArrayOfByte, paramInt1, paramInt2);
/*  83 */     if (paramInt2 != -1) {
/*  84 */       this.cksum.update(paramArrayOfByte, paramInt1, paramInt2);
/*     */     }
/*  86 */     return paramInt2;
/*     */   }
/*     */ 
/*     */   public long skip(long paramLong)
/*     */     throws IOException
/*     */   {
/*  96 */     byte[] arrayOfByte = new byte[512];
/*  97 */     long l1 = 0L;
/*  98 */     while (l1 < paramLong) {
/*  99 */       long l2 = paramLong - l1;
/* 100 */       l2 = read(arrayOfByte, 0, l2 < arrayOfByte.length ? (int)l2 : arrayOfByte.length);
/* 101 */       if (l2 == -1L) {
/* 102 */         return l1;
/*     */       }
/* 104 */       l1 += l2;
/*     */     }
/* 106 */     return l1;
/*     */   }
/*     */ 
/*     */   public Checksum getChecksum()
/*     */   {
/* 114 */     return this.cksum;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.zip.CheckedInputStream
 * JD-Core Version:    0.6.2
 */