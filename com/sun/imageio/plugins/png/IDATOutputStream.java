/*     */ package com.sun.imageio.plugins.png;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.zip.Deflater;
/*     */ import javax.imageio.stream.ImageOutputStream;
/*     */ import javax.imageio.stream.ImageOutputStreamImpl;
/*     */ 
/*     */ final class IDATOutputStream extends ImageOutputStreamImpl
/*     */ {
/* 153 */   private static byte[] chunkType = { 73, 68, 65, 84 };
/*     */   private ImageOutputStream stream;
/*     */   private int chunkLength;
/*     */   private long startPos;
/* 160 */   private CRC crc = new CRC();
/*     */ 
/* 162 */   Deflater def = new Deflater(9);
/* 163 */   byte[] buf = new byte[512];
/*     */   private int bytesRemaining;
/*     */ 
/*     */   public IDATOutputStream(ImageOutputStream paramImageOutputStream, int paramInt)
/*     */     throws IOException
/*     */   {
/* 169 */     this.stream = paramImageOutputStream;
/* 170 */     this.chunkLength = paramInt;
/* 171 */     startChunk();
/*     */   }
/*     */ 
/*     */   private void startChunk() throws IOException {
/* 175 */     this.crc.reset();
/* 176 */     this.startPos = this.stream.getStreamPosition();
/* 177 */     this.stream.writeInt(-1);
/*     */ 
/* 179 */     this.crc.update(chunkType, 0, 4);
/* 180 */     this.stream.write(chunkType, 0, 4);
/*     */ 
/* 182 */     this.bytesRemaining = this.chunkLength;
/*     */   }
/*     */ 
/*     */   private void finishChunk() throws IOException
/*     */   {
/* 187 */     this.stream.writeInt(this.crc.getValue());
/*     */ 
/* 190 */     long l = this.stream.getStreamPosition();
/* 191 */     this.stream.seek(this.startPos);
/* 192 */     this.stream.writeInt((int)(l - this.startPos) - 12);
/*     */ 
/* 195 */     this.stream.seek(l);
/* 196 */     this.stream.flushBefore(l);
/*     */   }
/*     */ 
/*     */   public int read() throws IOException {
/* 200 */     throw new RuntimeException("Method not available");
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/* 204 */     throw new RuntimeException("Method not available");
/*     */   }
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/* 208 */     if (paramInt2 == 0) {
/* 209 */       return;
/*     */     }
/*     */ 
/* 212 */     if (!this.def.finished()) {
/* 213 */       this.def.setInput(paramArrayOfByte, paramInt1, paramInt2);
/* 214 */       while (!this.def.needsInput())
/* 215 */         deflate();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void deflate() throws IOException
/*     */   {
/* 221 */     int i = this.def.deflate(this.buf, 0, this.buf.length);
/* 222 */     int j = 0;
/*     */ 
/* 224 */     while (i > 0) {
/* 225 */       if (this.bytesRemaining == 0) {
/* 226 */         finishChunk();
/* 227 */         startChunk();
/*     */       }
/*     */ 
/* 230 */       int k = Math.min(i, this.bytesRemaining);
/* 231 */       this.crc.update(this.buf, j, k);
/* 232 */       this.stream.write(this.buf, j, k);
/*     */ 
/* 234 */       j += k;
/* 235 */       i -= k;
/* 236 */       this.bytesRemaining -= k;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(int paramInt) throws IOException {
/* 241 */     byte[] arrayOfByte = new byte[1];
/* 242 */     arrayOfByte[0] = ((byte)paramInt);
/* 243 */     write(arrayOfByte, 0, 1);
/*     */   }
/*     */ 
/*     */   public void finish() throws IOException {
/*     */     try {
/* 248 */       if (!this.def.finished()) {
/* 249 */         this.def.finish();
/* 250 */         while (!this.def.finished()) {
/* 251 */           deflate();
/*     */         }
/*     */       }
/* 254 */       finishChunk();
/*     */     } finally {
/* 256 */       this.def.end();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void finalize()
/*     */     throws Throwable
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.png.IDATOutputStream
 * JD-Core Version:    0.6.2
 */