/*     */ package sun.awt.windows;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ class WDropTargetContextPeerIStream extends InputStream
/*     */ {
/*     */   private long istream;
/*     */ 
/*     */   WDropTargetContextPeerIStream(long paramLong)
/*     */     throws IOException
/*     */   {
/* 179 */     if (paramLong == 0L) throw new IOException("No IStream");
/*     */ 
/* 181 */     this.istream = paramLong;
/*     */   }
/*     */ 
/*     */   public int available()
/*     */     throws IOException
/*     */   {
/* 189 */     if (this.istream == 0L) throw new IOException("No IStream");
/* 190 */     return Available(this.istream);
/*     */   }
/*     */ 
/*     */   private native int Available(long paramLong);
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/* 200 */     if (this.istream == 0L) throw new IOException("No IStream");
/* 201 */     return Read(this.istream);
/*     */   }
/*     */ 
/*     */   private native int Read(long paramLong)
/*     */     throws IOException;
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 211 */     if (this.istream == 0L) throw new IOException("No IStream");
/* 212 */     return ReadBytes(this.istream, paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   private native int ReadBytes(long paramLong, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 222 */     if (this.istream != 0L) {
/* 223 */       super.close();
/* 224 */       Close(this.istream);
/* 225 */       this.istream = 0L;
/*     */     }
/*     */   }
/*     */ 
/*     */   private native void Close(long paramLong);
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WDropTargetContextPeerIStream
 * JD-Core Version:    0.6.2
 */