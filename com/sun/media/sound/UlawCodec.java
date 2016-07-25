/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Vector;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ import javax.sound.sampled.AudioFormat.Encoding;
/*     */ import javax.sound.sampled.AudioInputStream;
/*     */ 
/*     */ public final class UlawCodec extends SunCodec
/*     */ {
/*  46 */   private static final byte[] ULAW_TABH = new byte[256];
/*  47 */   private static final byte[] ULAW_TABL = new byte[256];
/*     */ 
/*  49 */   private static final AudioFormat.Encoding[] ulawEncodings = { AudioFormat.Encoding.ULAW, AudioFormat.Encoding.PCM_SIGNED };
/*     */ 
/*  52 */   private static final short[] seg_end = { 255, 511, 1023, 2047, 4095, 8191, 16383, 32767 };
/*     */ 
/*     */   public UlawCodec()
/*     */   {
/*  78 */     super(ulawEncodings, ulawEncodings);
/*     */   }
/*     */ 
/*     */   public AudioFormat.Encoding[] getTargetEncodings(AudioFormat paramAudioFormat)
/*     */   {
/*     */     AudioFormat.Encoding[] arrayOfEncoding;
/*  84 */     if (AudioFormat.Encoding.PCM_SIGNED.equals(paramAudioFormat.getEncoding())) {
/*  85 */       if (paramAudioFormat.getSampleSizeInBits() == 16) {
/*  86 */         arrayOfEncoding = new AudioFormat.Encoding[1];
/*  87 */         arrayOfEncoding[0] = AudioFormat.Encoding.ULAW;
/*  88 */         return arrayOfEncoding;
/*     */       }
/*  90 */       return new AudioFormat.Encoding[0];
/*     */     }
/*  92 */     if (AudioFormat.Encoding.ULAW.equals(paramAudioFormat.getEncoding())) {
/*  93 */       if (paramAudioFormat.getSampleSizeInBits() == 8) {
/*  94 */         arrayOfEncoding = new AudioFormat.Encoding[1];
/*  95 */         arrayOfEncoding[0] = AudioFormat.Encoding.PCM_SIGNED;
/*  96 */         return arrayOfEncoding;
/*     */       }
/*  98 */       return new AudioFormat.Encoding[0];
/*     */     }
/*     */ 
/* 101 */     return new AudioFormat.Encoding[0];
/*     */   }
/*     */ 
/*     */   public AudioFormat[] getTargetFormats(AudioFormat.Encoding paramEncoding, AudioFormat paramAudioFormat)
/*     */   {
/* 109 */     if (((AudioFormat.Encoding.PCM_SIGNED.equals(paramEncoding)) && (AudioFormat.Encoding.ULAW.equals(paramAudioFormat.getEncoding()))) || ((AudioFormat.Encoding.ULAW.equals(paramEncoding)) && (AudioFormat.Encoding.PCM_SIGNED.equals(paramAudioFormat.getEncoding()))))
/*     */     {
/* 114 */       return getOutputFormats(paramAudioFormat);
/*     */     }
/* 116 */     return new AudioFormat[0];
/*     */   }
/*     */ 
/*     */   public AudioInputStream getAudioInputStream(AudioFormat.Encoding paramEncoding, AudioInputStream paramAudioInputStream)
/*     */   {
/* 123 */     AudioFormat localAudioFormat1 = paramAudioInputStream.getFormat();
/* 124 */     AudioFormat.Encoding localEncoding = localAudioFormat1.getEncoding();
/*     */ 
/* 126 */     if (localEncoding.equals(paramEncoding)) {
/* 127 */       return paramAudioInputStream;
/*     */     }
/* 129 */     AudioFormat localAudioFormat2 = null;
/* 130 */     if (!isConversionSupported(paramEncoding, paramAudioInputStream.getFormat())) {
/* 131 */       throw new IllegalArgumentException("Unsupported conversion: " + paramAudioInputStream.getFormat().toString() + " to " + paramEncoding.toString());
/*     */     }
/* 133 */     if ((AudioFormat.Encoding.ULAW.equals(localEncoding)) && (AudioFormat.Encoding.PCM_SIGNED.equals(paramEncoding)))
/*     */     {
/* 135 */       localAudioFormat2 = new AudioFormat(paramEncoding, localAudioFormat1.getSampleRate(), 16, localAudioFormat1.getChannels(), 2 * localAudioFormat1.getChannels(), localAudioFormat1.getSampleRate(), localAudioFormat1.isBigEndian());
/*     */     }
/* 142 */     else if ((AudioFormat.Encoding.PCM_SIGNED.equals(localEncoding)) && (AudioFormat.Encoding.ULAW.equals(paramEncoding)))
/*     */     {
/* 144 */       localAudioFormat2 = new AudioFormat(paramEncoding, localAudioFormat1.getSampleRate(), 8, localAudioFormat1.getChannels(), localAudioFormat1.getChannels(), localAudioFormat1.getSampleRate(), false);
/*     */     }
/*     */     else
/*     */     {
/* 152 */       throw new IllegalArgumentException("Unsupported conversion: " + paramAudioInputStream.getFormat().toString() + " to " + paramEncoding.toString());
/*     */     }
/*     */ 
/* 155 */     return getAudioInputStream(localAudioFormat2, paramAudioInputStream);
/*     */   }
/*     */ 
/*     */   public AudioInputStream getAudioInputStream(AudioFormat paramAudioFormat, AudioInputStream paramAudioInputStream)
/*     */   {
/* 163 */     return getConvertedStream(paramAudioFormat, paramAudioInputStream);
/*     */   }
/*     */ 
/*     */   private AudioInputStream getConvertedStream(AudioFormat paramAudioFormat, AudioInputStream paramAudioInputStream)
/*     */   {
/* 179 */     Object localObject = null;
/*     */ 
/* 181 */     AudioFormat localAudioFormat = paramAudioInputStream.getFormat();
/*     */ 
/* 183 */     if (localAudioFormat.matches(paramAudioFormat))
/* 184 */       localObject = paramAudioInputStream;
/*     */     else {
/* 186 */       localObject = new UlawCodecStream(paramAudioInputStream, paramAudioFormat);
/*     */     }
/* 188 */     return localObject;
/*     */   }
/*     */ 
/*     */   private AudioFormat[] getOutputFormats(AudioFormat paramAudioFormat)
/*     */   {
/* 201 */     Vector localVector = new Vector();
/*     */     AudioFormat localAudioFormat;
/* 204 */     if ((paramAudioFormat.getSampleSizeInBits() == 16) && (AudioFormat.Encoding.PCM_SIGNED.equals(paramAudioFormat.getEncoding())))
/*     */     {
/* 206 */       localAudioFormat = new AudioFormat(AudioFormat.Encoding.ULAW, paramAudioFormat.getSampleRate(), 8, paramAudioFormat.getChannels(), paramAudioFormat.getChannels(), paramAudioFormat.getSampleRate(), false);
/*     */ 
/* 213 */       localVector.addElement(localAudioFormat);
/*     */     }
/*     */ 
/* 216 */     if (AudioFormat.Encoding.ULAW.equals(paramAudioFormat.getEncoding())) {
/* 217 */       localAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, paramAudioFormat.getSampleRate(), 16, paramAudioFormat.getChannels(), paramAudioFormat.getChannels() * 2, paramAudioFormat.getSampleRate(), false);
/*     */ 
/* 224 */       localVector.addElement(localAudioFormat);
/*     */ 
/* 226 */       localAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, paramAudioFormat.getSampleRate(), 16, paramAudioFormat.getChannels(), paramAudioFormat.getChannels() * 2, paramAudioFormat.getSampleRate(), true);
/*     */ 
/* 233 */       localVector.addElement(localAudioFormat);
/*     */     }
/*     */ 
/* 236 */     AudioFormat[] arrayOfAudioFormat = new AudioFormat[localVector.size()];
/* 237 */     for (int i = 0; i < arrayOfAudioFormat.length; i++) {
/* 238 */       arrayOfAudioFormat[i] = ((AudioFormat)(AudioFormat)localVector.elementAt(i));
/*     */     }
/* 240 */     return arrayOfAudioFormat;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  59 */     for (int i = 0; i < 256; i++) {
/*  60 */       int j = i ^ 0xFFFFFFFF;
/*     */ 
/*  63 */       j &= 255;
/*  64 */       int k = ((j & 0xF) << 3) + 132;
/*  65 */       k <<= (j & 0x70) >> 4;
/*  66 */       k = (j & 0x80) != 0 ? 132 - k : k - 132;
/*     */ 
/*  68 */       ULAW_TABL[i] = ((byte)(k & 0xFF));
/*  69 */       ULAW_TABH[i] = ((byte)(k >> 8 & 0xFF));
/*     */     }
/*     */   }
/*     */ 
/*     */   class UlawCodecStream extends AudioInputStream
/*     */   {
/*     */     private static final int tempBufferSize = 64;
/* 247 */     private byte[] tempBuffer = null;
/*     */ 
/* 252 */     boolean encode = false;
/*     */     AudioFormat encodeFormat;
/*     */     AudioFormat decodeFormat;
/* 257 */     byte[] tabByte1 = null;
/* 258 */     byte[] tabByte2 = null;
/* 259 */     int highByte = 0;
/* 260 */     int lowByte = 1;
/*     */ 
/*     */     UlawCodecStream(AudioInputStream paramAudioFormat, AudioFormat arg3) {
/* 263 */       super(localAudioFormat1, -1L);
/*     */ 
/* 265 */       AudioFormat localAudioFormat2 = paramAudioFormat.getFormat();
/*     */ 
/* 268 */       if (!UlawCodec.this.isConversionSupported(localAudioFormat1, localAudioFormat2))
/* 269 */         throw new IllegalArgumentException("Unsupported conversion: " + localAudioFormat2.toString() + " to " + localAudioFormat1.toString());
/*     */       boolean bool;
/* 276 */       if (AudioFormat.Encoding.ULAW.equals(localAudioFormat2.getEncoding())) {
/* 277 */         this.encode = false;
/* 278 */         this.encodeFormat = localAudioFormat2;
/* 279 */         this.decodeFormat = localAudioFormat1;
/* 280 */         bool = localAudioFormat1.isBigEndian();
/*     */       } else {
/* 282 */         this.encode = true;
/* 283 */         this.encodeFormat = localAudioFormat1;
/* 284 */         this.decodeFormat = localAudioFormat2;
/* 285 */         bool = localAudioFormat2.isBigEndian();
/* 286 */         this.tempBuffer = new byte[64];
/*     */       }
/*     */ 
/* 290 */       if (bool) {
/* 291 */         this.tabByte1 = UlawCodec.ULAW_TABH;
/* 292 */         this.tabByte2 = UlawCodec.ULAW_TABL;
/* 293 */         this.highByte = 0;
/* 294 */         this.lowByte = 1;
/*     */       } else {
/* 296 */         this.tabByte1 = UlawCodec.ULAW_TABL;
/* 297 */         this.tabByte2 = UlawCodec.ULAW_TABH;
/* 298 */         this.highByte = 1;
/* 299 */         this.lowByte = 0;
/*     */       }
/*     */ 
/* 303 */       if ((paramAudioFormat instanceof AudioInputStream)) {
/* 304 */         this.frameLength = paramAudioFormat.getFrameLength();
/*     */       }
/*     */ 
/* 307 */       this.framePos = 0L;
/* 308 */       this.frameSize = localAudioFormat2.getFrameSize();
/* 309 */       if (this.frameSize == -1)
/* 310 */         this.frameSize = 1;
/*     */     }
/*     */ 
/*     */     private short search(short paramShort1, short[] paramArrayOfShort, short paramShort2)
/*     */     {
/* 320 */       for (short s = 0; s < paramShort2; s = (short)(s + 1)) {
/* 321 */         if (paramShort1 <= paramArrayOfShort[s]) return s;
/*     */       }
/* 323 */       return paramShort2;
/*     */     }
/*     */ 
/*     */     public int read()
/*     */       throws IOException
/*     */     {
/* 331 */       byte[] arrayOfByte = new byte[1];
/* 332 */       if (read(arrayOfByte, 0, arrayOfByte.length) == 1) {
/* 333 */         return arrayOfByte[1] & 0xFF;
/*     */       }
/* 335 */       return -1;
/*     */     }
/*     */ 
/*     */     public int read(byte[] paramArrayOfByte) throws IOException {
/* 339 */       return read(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */     }
/*     */ 
/*     */     public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException
/*     */     {
/* 344 */       if (paramInt2 % this.frameSize != 0) {
/* 345 */         paramInt2 -= paramInt2 % this.frameSize;
/*     */       }
/* 347 */       if (this.encode) {
/* 348 */         i = 132;
/*     */ 
/* 356 */         int i2 = 0;
/* 357 */         int i3 = paramInt1;
/* 358 */         int i4 = paramInt2 * 2;
/* 359 */         int i5 = i4 > 64 ? 64 : i4;
/*     */ 
/* 361 */         while ((i2 = super.read(this.tempBuffer, 0, i5)) > 0) {
/* 362 */           for (m = 0; m < i2; m += 2)
/*     */           {
/* 364 */             int n = (short)(this.tempBuffer[(m + this.highByte)] << 8 & 0xFF00);
/* 365 */             n = (short)(n | (short)((short)this.tempBuffer[(m + this.lowByte)] & 0xFF));
/*     */ 
/* 368 */             if (n < 0) {
/* 369 */               n = (short)(i - n);
/* 370 */               j = 127;
/*     */             } else {
/* 372 */               n = (short)(n + i);
/* 373 */               j = 255;
/*     */             }
/*     */ 
/* 376 */             k = search(n, UlawCodec.seg_end, (short)8);
/*     */             int i1;
/* 381 */             if (k >= 8) {
/* 382 */               i1 = (byte)(0x7F ^ j);
/*     */             } else {
/* 384 */               i1 = (byte)(k << 4 | n >> k + 3 & 0xF);
/* 385 */               i1 = (byte)(i1 ^ j);
/*     */             }
/*     */ 
/* 388 */             paramArrayOfByte[i3] = i1;
/* 389 */             i3++;
/*     */           }
/*     */ 
/* 392 */           i4 -= i2;
/* 393 */           i5 = i4 > 64 ? 64 : i4;
/*     */         }
/* 395 */         if ((i3 == paramInt1) && (i2 < 0)) {
/* 396 */           return i2;
/*     */         }
/* 398 */         return i3 - paramInt1;
/*     */       }
/*     */ 
/* 401 */       int j = paramInt2 / 2;
/* 402 */       int k = paramInt1 + paramInt2 / 2;
/* 403 */       int m = super.read(paramArrayOfByte, k, j);
/*     */ 
/* 405 */       if (m < 0) {
/* 406 */         return m;
/*     */       }
/* 408 */       for (int i = paramInt1; i < paramInt1 + m * 2; i += 2) {
/* 409 */         paramArrayOfByte[i] = this.tabByte1[(paramArrayOfByte[k] & 0xFF)];
/* 410 */         paramArrayOfByte[(i + 1)] = this.tabByte2[(paramArrayOfByte[k] & 0xFF)];
/* 411 */         k++;
/*     */       }
/* 413 */       return i - paramInt1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.UlawCodec
 * JD-Core Version:    0.6.2
 */