/*     */ package sun.awt.image;
/*     */ 
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ class PNGFilterInputStream extends FilterInputStream
/*     */ {
/*     */   PNGImageDecoder owner;
/*     */   public InputStream underlyingInputStream;
/*     */ 
/*     */   public PNGFilterInputStream(PNGImageDecoder paramPNGImageDecoder, InputStream paramInputStream)
/*     */   {
/* 811 */     super(paramInputStream);
/* 812 */     this.underlyingInputStream = this.in;
/* 813 */     this.owner = paramPNGImageDecoder;
/*     */   }
/*     */ 
/*     */   public int available() throws IOException {
/* 817 */     return this.owner.limit - this.owner.pos + this.in.available(); } 
/* 818 */   public boolean markSupported() { return false; } 
/*     */   public int read() throws IOException {
/* 820 */     if ((this.owner.chunkLength <= 0) && (!this.owner.getData())) return -1;
/* 821 */     this.owner.chunkLength -= 1;
/* 822 */     return this.owner.inbuf[(this.owner.chunkStart++)] & 0xFF;
/*     */   }
/* 824 */   public int read(byte[] paramArrayOfByte) throws IOException { return read(paramArrayOfByte, 0, paramArrayOfByte.length); } 
/*     */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/* 826 */     if ((this.owner.chunkLength <= 0) && (!this.owner.getData())) return -1;
/* 827 */     if (this.owner.chunkLength < paramInt2) paramInt2 = this.owner.chunkLength;
/* 828 */     System.arraycopy(this.owner.inbuf, this.owner.chunkStart, paramArrayOfByte, paramInt1, paramInt2);
/* 829 */     this.owner.chunkLength -= paramInt2;
/* 830 */     this.owner.chunkStart += paramInt2;
/* 831 */     return paramInt2;
/*     */   }
/*     */ 
/*     */   public long skip(long paramLong) throws IOException {
/* 835 */     for (int i = 0; (i < paramLong) && (read() >= 0); i++);
/* 836 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.PNGFilterInputStream
 * JD-Core Version:    0.6.2
 */