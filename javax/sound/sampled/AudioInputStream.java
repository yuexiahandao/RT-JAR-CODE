/*     */ package javax.sound.sampled;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class AudioInputStream extends InputStream
/*     */ {
/*     */   private InputStream stream;
/*     */   protected AudioFormat format;
/*     */   protected long frameLength;
/*     */   protected int frameSize;
/*     */   protected long framePos;
/*     */   private long markpos;
/*  99 */   private byte[] pushBackBuffer = null;
/*     */ 
/* 104 */   private int pushBackLen = 0;
/*     */ 
/* 109 */   private byte[] markPushBackBuffer = null;
/*     */ 
/* 114 */   private int markPushBackLen = 0;
/*     */ 
/*     */   public AudioInputStream(InputStream paramInputStream, AudioFormat paramAudioFormat, long paramLong)
/*     */   {
/* 129 */     this.format = paramAudioFormat;
/* 130 */     this.frameLength = paramLong;
/* 131 */     this.frameSize = paramAudioFormat.getFrameSize();
/*     */ 
/* 135 */     if ((this.frameSize == -1) || (this.frameSize <= 0)) {
/* 136 */       this.frameSize = 1;
/*     */     }
/*     */ 
/* 139 */     this.stream = paramInputStream;
/* 140 */     this.framePos = 0L;
/* 141 */     this.markpos = 0L;
/*     */   }
/*     */ 
/*     */   public AudioInputStream(TargetDataLine paramTargetDataLine)
/*     */   {
/* 154 */     TargetDataLineInputStream localTargetDataLineInputStream = new TargetDataLineInputStream(paramTargetDataLine);
/* 155 */     this.format = paramTargetDataLine.getFormat();
/* 156 */     this.frameLength = -1L;
/* 157 */     this.frameSize = this.format.getFrameSize();
/*     */ 
/* 159 */     if ((this.frameSize == -1) || (this.frameSize <= 0)) {
/* 160 */       this.frameSize = 1;
/*     */     }
/* 162 */     this.stream = localTargetDataLineInputStream;
/* 163 */     this.framePos = 0L;
/* 164 */     this.markpos = 0L;
/*     */   }
/*     */ 
/*     */   public AudioFormat getFormat()
/*     */   {
/* 173 */     return this.format;
/*     */   }
/*     */ 
/*     */   public long getFrameLength()
/*     */   {
/* 182 */     return this.frameLength;
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/* 199 */     if (this.frameSize != 1) {
/* 200 */       throw new IOException("cannot read a single byte if frame size > 1");
/*     */     }
/*     */ 
/* 203 */     byte[] arrayOfByte = new byte[1];
/* 204 */     int i = read(arrayOfByte);
/* 205 */     if (i <= 0)
/*     */     {
/* 207 */       return -1;
/*     */     }
/* 209 */     return arrayOfByte[0] & 0xFF;
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 232 */     return read(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 259 */     if (paramInt2 % this.frameSize != 0) {
/* 260 */       paramInt2 -= paramInt2 % this.frameSize;
/* 261 */       if (paramInt2 == 0) {
/* 262 */         return 0;
/*     */       }
/*     */     }
/*     */ 
/* 266 */     if (this.frameLength != -1L) {
/* 267 */       if (this.framePos >= this.frameLength) {
/* 268 */         return -1;
/*     */       }
/*     */ 
/* 272 */       if (paramInt2 / this.frameSize > this.frameLength - this.framePos) {
/* 273 */         paramInt2 = (int)(this.frameLength - this.framePos) * this.frameSize;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 278 */     int i = 0;
/* 279 */     int j = paramInt1;
/*     */ 
/* 283 */     if ((this.pushBackLen > 0) && (paramInt2 >= this.pushBackLen)) {
/* 284 */       System.arraycopy(this.pushBackBuffer, 0, paramArrayOfByte, paramInt1, this.pushBackLen);
/*     */ 
/* 286 */       j += this.pushBackLen;
/* 287 */       paramInt2 -= this.pushBackLen;
/* 288 */       i += this.pushBackLen;
/* 289 */       this.pushBackLen = 0;
/*     */     }
/*     */ 
/* 292 */     int k = this.stream.read(paramArrayOfByte, j, paramInt2);
/* 293 */     if (k == -1) {
/* 294 */       return -1;
/*     */     }
/* 296 */     if (k > 0) {
/* 297 */       i += k;
/*     */     }
/* 299 */     if (i > 0) {
/* 300 */       this.pushBackLen = (i % this.frameSize);
/* 301 */       if (this.pushBackLen > 0)
/*     */       {
/* 304 */         if (this.pushBackBuffer == null) {
/* 305 */           this.pushBackBuffer = new byte[this.frameSize];
/*     */         }
/* 307 */         System.arraycopy(paramArrayOfByte, paramInt1 + i - this.pushBackLen, this.pushBackBuffer, 0, this.pushBackLen);
/*     */ 
/* 309 */         i -= this.pushBackLen;
/*     */       }
/*     */ 
/* 312 */       this.framePos += i / this.frameSize;
/*     */     }
/* 314 */     return i;
/*     */   }
/*     */ 
/*     */   public long skip(long paramLong)
/*     */     throws IOException
/*     */   {
/* 330 */     if (paramLong % this.frameSize != 0L) {
/* 331 */       paramLong -= paramLong % this.frameSize;
/*     */     }
/*     */ 
/* 334 */     if (this.frameLength != -1L)
/*     */     {
/* 336 */       if (paramLong / this.frameSize > this.frameLength - this.framePos) {
/* 337 */         paramLong = (this.frameLength - this.framePos) * this.frameSize;
/*     */       }
/*     */     }
/* 340 */     long l = this.stream.skip(paramLong);
/*     */ 
/* 343 */     if (l % this.frameSize != 0L)
/*     */     {
/* 346 */       throw new IOException("Could not skip an integer number of frames.");
/*     */     }
/* 348 */     if (l >= 0L) {
/* 349 */       this.framePos += l / this.frameSize;
/*     */     }
/* 351 */     return l;
/*     */   }
/*     */ 
/*     */   public int available()
/*     */     throws IOException
/*     */   {
/* 372 */     int i = this.stream.available();
/*     */ 
/* 375 */     if ((this.frameLength != -1L) && (i / this.frameSize > this.frameLength - this.framePos)) {
/* 376 */       return (int)(this.frameLength - this.framePos) * this.frameSize;
/*     */     }
/* 378 */     return i;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 389 */     this.stream.close();
/*     */   }
/*     */ 
/*     */   public void mark(int paramInt)
/*     */   {
/* 403 */     this.stream.mark(paramInt);
/* 404 */     if (markSupported()) {
/* 405 */       this.markpos = this.framePos;
/*     */ 
/* 407 */       this.markPushBackLen = this.pushBackLen;
/* 408 */       if (this.markPushBackLen > 0) {
/* 409 */         if (this.markPushBackBuffer == null) {
/* 410 */           this.markPushBackBuffer = new byte[this.frameSize];
/*     */         }
/* 412 */         System.arraycopy(this.pushBackBuffer, 0, this.markPushBackBuffer, 0, this.markPushBackLen);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */     throws IOException
/*     */   {
/* 427 */     this.stream.reset();
/* 428 */     this.framePos = this.markpos;
/*     */ 
/* 430 */     this.pushBackLen = this.markPushBackLen;
/* 431 */     if (this.pushBackLen > 0) {
/* 432 */       if (this.pushBackBuffer == null) {
/* 433 */         this.pushBackBuffer = new byte[this.frameSize - 1];
/*     */       }
/* 435 */       System.arraycopy(this.markPushBackBuffer, 0, this.pushBackBuffer, 0, this.pushBackLen);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean markSupported()
/*     */   {
/* 450 */     return this.stream.markSupported();
/*     */   }
/*     */ 
/*     */   private class TargetDataLineInputStream extends InputStream
/*     */   {
/*     */     TargetDataLine line;
/*     */ 
/*     */     TargetDataLineInputStream(TargetDataLine arg2)
/*     */     {
/*     */       Object localObject;
/* 467 */       this.line = localObject;
/*     */     }
/*     */ 
/*     */     public int available() throws IOException
/*     */     {
/* 472 */       return this.line.available();
/*     */     }
/*     */ 
/*     */     public void close()
/*     */       throws IOException
/*     */     {
/* 480 */       if (this.line.isActive()) {
/* 481 */         this.line.flush();
/* 482 */         this.line.stop();
/*     */       }
/* 484 */       this.line.close();
/*     */     }
/*     */ 
/*     */     public int read() throws IOException
/*     */     {
/* 489 */       byte[] arrayOfByte = new byte[1];
/*     */ 
/* 491 */       int i = read(arrayOfByte, 0, 1);
/*     */ 
/* 493 */       if (i == -1) {
/* 494 */         return -1;
/*     */       }
/*     */ 
/* 497 */       i = arrayOfByte[0];
/*     */ 
/* 499 */       if (this.line.getFormat().getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED)) {
/* 500 */         i += 128;
/*     */       }
/*     */ 
/* 503 */       return i;
/*     */     }
/*     */ 
/*     */     public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException
/*     */     {
/*     */       try {
/* 509 */         return this.line.read(paramArrayOfByte, paramInt1, paramInt2);
/*     */       } catch (IllegalArgumentException localIllegalArgumentException) {
/* 511 */         throw new IOException(localIllegalArgumentException.getMessage());
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.sampled.AudioInputStream
 * JD-Core Version:    0.6.2
 */