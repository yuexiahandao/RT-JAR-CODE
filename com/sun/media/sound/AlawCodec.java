/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Vector;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ import javax.sound.sampled.AudioFormat.Encoding;
/*     */ import javax.sound.sampled.AudioInputStream;
/*     */ 
/*     */ public final class AlawCodec extends SunCodec
/*     */ {
/*  45 */   private static final byte[] ALAW_TABH = new byte[256];
/*  46 */   private static final byte[] ALAW_TABL = new byte[256];
/*     */ 
/*  48 */   private static final AudioFormat.Encoding[] alawEncodings = { AudioFormat.Encoding.ALAW, AudioFormat.Encoding.PCM_SIGNED };
/*     */ 
/*  50 */   private static final short[] seg_end = { 255, 511, 1023, 2047, 4095, 8191, 16383, 32767 };
/*     */ 
/*     */   public AlawCodec()
/*     */   {
/*  82 */     super(alawEncodings, alawEncodings);
/*     */   }
/*     */ 
/*     */   public AudioFormat.Encoding[] getTargetEncodings(AudioFormat paramAudioFormat)
/*     */   {
/*     */     AudioFormat.Encoding[] arrayOfEncoding;
/*  91 */     if (paramAudioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED))
/*     */     {
/*  93 */       if (paramAudioFormat.getSampleSizeInBits() == 16)
/*     */       {
/*  95 */         arrayOfEncoding = new AudioFormat.Encoding[1];
/*  96 */         arrayOfEncoding[0] = AudioFormat.Encoding.ALAW;
/*  97 */         return arrayOfEncoding;
/*     */       }
/*     */ 
/* 100 */       return new AudioFormat.Encoding[0];
/*     */     }
/* 102 */     if (paramAudioFormat.getEncoding().equals(AudioFormat.Encoding.ALAW))
/*     */     {
/* 104 */       if (paramAudioFormat.getSampleSizeInBits() == 8)
/*     */       {
/* 106 */         arrayOfEncoding = new AudioFormat.Encoding[1];
/* 107 */         arrayOfEncoding[0] = AudioFormat.Encoding.PCM_SIGNED;
/* 108 */         return arrayOfEncoding;
/*     */       }
/*     */ 
/* 111 */       return new AudioFormat.Encoding[0];
/*     */     }
/*     */ 
/* 115 */     return new AudioFormat.Encoding[0];
/*     */   }
/*     */ 
/*     */   public AudioFormat[] getTargetFormats(AudioFormat.Encoding paramEncoding, AudioFormat paramAudioFormat)
/*     */   {
/* 122 */     if (((paramEncoding.equals(AudioFormat.Encoding.PCM_SIGNED)) && (paramAudioFormat.getEncoding().equals(AudioFormat.Encoding.ALAW))) || ((paramEncoding.equals(AudioFormat.Encoding.ALAW)) && (paramAudioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED))))
/*     */     {
/* 124 */       return getOutputFormats(paramAudioFormat);
/*     */     }
/* 126 */     return new AudioFormat[0];
/*     */   }
/*     */ 
/*     */   public AudioInputStream getAudioInputStream(AudioFormat.Encoding paramEncoding, AudioInputStream paramAudioInputStream)
/*     */   {
/* 133 */     AudioFormat localAudioFormat1 = paramAudioInputStream.getFormat();
/* 134 */     AudioFormat.Encoding localEncoding = localAudioFormat1.getEncoding();
/*     */ 
/* 136 */     if (localEncoding.equals(paramEncoding)) {
/* 137 */       return paramAudioInputStream;
/*     */     }
/* 139 */     AudioFormat localAudioFormat2 = null;
/* 140 */     if (!isConversionSupported(paramEncoding, paramAudioInputStream.getFormat())) {
/* 141 */       throw new IllegalArgumentException("Unsupported conversion: " + paramAudioInputStream.getFormat().toString() + " to " + paramEncoding.toString());
/*     */     }
/* 143 */     if ((localEncoding.equals(AudioFormat.Encoding.ALAW)) && (paramEncoding.equals(AudioFormat.Encoding.PCM_SIGNED)))
/*     */     {
/* 146 */       localAudioFormat2 = new AudioFormat(paramEncoding, localAudioFormat1.getSampleRate(), 16, localAudioFormat1.getChannels(), 2 * localAudioFormat1.getChannels(), localAudioFormat1.getSampleRate(), localAudioFormat1.isBigEndian());
/*     */     }
/* 154 */     else if ((localEncoding.equals(AudioFormat.Encoding.PCM_SIGNED)) && (paramEncoding.equals(AudioFormat.Encoding.ALAW)))
/*     */     {
/* 157 */       localAudioFormat2 = new AudioFormat(paramEncoding, localAudioFormat1.getSampleRate(), 8, localAudioFormat1.getChannels(), localAudioFormat1.getChannels(), localAudioFormat1.getSampleRate(), false);
/*     */     }
/*     */     else
/*     */     {
/* 165 */       throw new IllegalArgumentException("Unsupported conversion: " + paramAudioInputStream.getFormat().toString() + " to " + paramEncoding.toString());
/*     */     }
/* 167 */     return getAudioInputStream(localAudioFormat2, paramAudioInputStream);
/*     */   }
/*     */ 
/*     */   public AudioInputStream getAudioInputStream(AudioFormat paramAudioFormat, AudioInputStream paramAudioInputStream)
/*     */   {
/* 175 */     return getConvertedStream(paramAudioFormat, paramAudioInputStream);
/*     */   }
/*     */ 
/*     */   private AudioInputStream getConvertedStream(AudioFormat paramAudioFormat, AudioInputStream paramAudioInputStream)
/*     */   {
/* 193 */     Object localObject = null;
/* 194 */     AudioFormat localAudioFormat = paramAudioInputStream.getFormat();
/*     */ 
/* 196 */     if (localAudioFormat.matches(paramAudioFormat))
/* 197 */       localObject = paramAudioInputStream;
/*     */     else {
/* 199 */       localObject = new AlawCodecStream(paramAudioInputStream, paramAudioFormat);
/*     */     }
/*     */ 
/* 202 */     return localObject;
/*     */   }
/*     */ 
/*     */   private AudioFormat[] getOutputFormats(AudioFormat paramAudioFormat)
/*     */   {
/* 216 */     Vector localVector = new Vector();
/*     */     AudioFormat localAudioFormat;
/* 219 */     if (AudioFormat.Encoding.PCM_SIGNED.equals(paramAudioFormat.getEncoding())) {
/* 220 */       localAudioFormat = new AudioFormat(AudioFormat.Encoding.ALAW, paramAudioFormat.getSampleRate(), 8, paramAudioFormat.getChannels(), paramAudioFormat.getChannels(), paramAudioFormat.getSampleRate(), false);
/*     */ 
/* 227 */       localVector.addElement(localAudioFormat);
/*     */     }
/*     */ 
/* 230 */     if (AudioFormat.Encoding.ALAW.equals(paramAudioFormat.getEncoding())) {
/* 231 */       localAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, paramAudioFormat.getSampleRate(), 16, paramAudioFormat.getChannels(), paramAudioFormat.getChannels() * 2, paramAudioFormat.getSampleRate(), false);
/*     */ 
/* 238 */       localVector.addElement(localAudioFormat);
/* 239 */       localAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, paramAudioFormat.getSampleRate(), 16, paramAudioFormat.getChannels(), paramAudioFormat.getChannels() * 2, paramAudioFormat.getSampleRate(), true);
/*     */ 
/* 246 */       localVector.addElement(localAudioFormat);
/*     */     }
/*     */ 
/* 249 */     AudioFormat[] arrayOfAudioFormat = new AudioFormat[localVector.size()];
/* 250 */     for (int i = 0; i < arrayOfAudioFormat.length; i++) {
/* 251 */       arrayOfAudioFormat[i] = ((AudioFormat)(AudioFormat)localVector.elementAt(i));
/*     */     }
/* 253 */     return arrayOfAudioFormat;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  57 */     for (int i = 0; i < 256; i++) {
/*  58 */       int j = i ^ 0x55;
/*  59 */       int k = (j & 0xF) << 4;
/*  60 */       int m = (j & 0x70) >> 4;
/*  61 */       int n = k + 8;
/*     */ 
/*  63 */       if (m >= 1)
/*  64 */         n += 256;
/*  65 */       if (m > 1) {
/*  66 */         n <<= m - 1;
/*     */       }
/*  68 */       if ((j & 0x80) == 0) {
/*  69 */         n = -n;
/*     */       }
/*  71 */       ALAW_TABL[i] = ((byte)n);
/*  72 */       ALAW_TABH[i] = ((byte)(n >> 8));
/*     */     }
/*     */   }
/*     */ 
/*     */   final class AlawCodecStream extends AudioInputStream
/*     */   {
/*     */     private static final int tempBufferSize = 64;
/* 261 */     private byte[] tempBuffer = null;
/*     */ 
/* 266 */     boolean encode = false;
/*     */     AudioFormat encodeFormat;
/*     */     AudioFormat decodeFormat;
/* 271 */     byte[] tabByte1 = null;
/* 272 */     byte[] tabByte2 = null;
/* 273 */     int highByte = 0;
/* 274 */     int lowByte = 1;
/*     */ 
/*     */     AlawCodecStream(AudioInputStream paramAudioFormat, AudioFormat arg3)
/*     */     {
/* 278 */       super(localAudioFormat1, -1L);
/*     */ 
/* 280 */       AudioFormat localAudioFormat2 = paramAudioFormat.getFormat();
/*     */ 
/* 283 */       if (!AlawCodec.this.isConversionSupported(localAudioFormat1, localAudioFormat2))
/*     */       {
/* 285 */         throw new IllegalArgumentException("Unsupported conversion: " + localAudioFormat2.toString() + " to " + localAudioFormat1.toString());
/*     */       }
/*     */       boolean bool;
/* 292 */       if (AudioFormat.Encoding.ALAW.equals(localAudioFormat2.getEncoding())) {
/* 293 */         this.encode = false;
/* 294 */         this.encodeFormat = localAudioFormat2;
/* 295 */         this.decodeFormat = localAudioFormat1;
/* 296 */         bool = localAudioFormat1.isBigEndian();
/*     */       } else {
/* 298 */         this.encode = true;
/* 299 */         this.encodeFormat = localAudioFormat1;
/* 300 */         this.decodeFormat = localAudioFormat2;
/* 301 */         bool = localAudioFormat2.isBigEndian();
/* 302 */         this.tempBuffer = new byte[64];
/*     */       }
/*     */ 
/* 305 */       if (bool) {
/* 306 */         this.tabByte1 = AlawCodec.ALAW_TABH;
/* 307 */         this.tabByte2 = AlawCodec.ALAW_TABL;
/* 308 */         this.highByte = 0;
/* 309 */         this.lowByte = 1;
/*     */       } else {
/* 311 */         this.tabByte1 = AlawCodec.ALAW_TABL;
/* 312 */         this.tabByte2 = AlawCodec.ALAW_TABH;
/* 313 */         this.highByte = 1;
/* 314 */         this.lowByte = 0;
/*     */       }
/*     */ 
/* 318 */       if ((paramAudioFormat instanceof AudioInputStream)) {
/* 319 */         this.frameLength = paramAudioFormat.getFrameLength();
/*     */       }
/*     */ 
/* 323 */       this.framePos = 0L;
/* 324 */       this.frameSize = localAudioFormat2.getFrameSize();
/* 325 */       if (this.frameSize == -1)
/* 326 */         this.frameSize = 1;
/*     */     }
/*     */ 
/*     */     private short search(short paramShort1, short[] paramArrayOfShort, short paramShort2)
/*     */     {
/* 336 */       for (short s = 0; s < paramShort2; s = (short)(s + 1)) {
/* 337 */         if (paramShort1 <= paramArrayOfShort[s]) return s;
/*     */       }
/* 339 */       return paramShort2;
/*     */     }
/*     */ 
/*     */     public int read()
/*     */       throws IOException
/*     */     {
/* 348 */       byte[] arrayOfByte = new byte[1];
/* 349 */       return read(arrayOfByte, 0, arrayOfByte.length);
/*     */     }
/*     */ 
/*     */     public int read(byte[] paramArrayOfByte)
/*     */       throws IOException
/*     */     {
/* 355 */       return read(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */     }
/*     */ 
/*     */     public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */       throws IOException
/*     */     {
/* 361 */       if (paramInt2 % this.frameSize != 0) {
/* 362 */         paramInt2 -= paramInt2 % this.frameSize;
/*     */       }
/*     */ 
/* 365 */       if (this.encode)
/*     */       {
/* 367 */         i = 15;
/* 368 */         j = 4;
/*     */ 
/* 377 */         int i3 = 0;
/* 378 */         int i4 = paramInt1;
/* 379 */         int i5 = paramInt2 * 2;
/* 380 */         int i6 = i5 > 64 ? 64 : i5;
/*     */ 
/* 382 */         while ((i3 = super.read(this.tempBuffer, 0, i6)) > 0)
/*     */         {
/* 384 */           for (int n = 0; n < i3; n += 2)
/*     */           {
/* 387 */             int i1 = (short)(this.tempBuffer[(n + this.highByte)] << 8 & 0xFF00);
/* 388 */             i1 = (short)(i1 | (short)(this.tempBuffer[(n + this.lowByte)] & 0xFF));
/*     */ 
/* 390 */             if (i1 >= 0) {
/* 391 */               k = 213;
/*     */             } else {
/* 393 */               k = 85;
/* 394 */               i1 = (short)(-i1 - 8);
/*     */             }
/*     */ 
/* 397 */             m = search(i1, AlawCodec.seg_end, (short)8);
/*     */             int i2;
/* 401 */             if (m >= 8) {
/* 402 */               i2 = (byte)(0x7F ^ k);
/*     */             } else {
/* 404 */               i2 = (byte)(m << j);
/* 405 */               if (m < 2)
/* 406 */                 i2 = (byte)(i2 | (byte)(i1 >> 4 & i));
/*     */               else {
/* 408 */                 i2 = (byte)(i2 | (byte)(i1 >> m + 3 & i));
/*     */               }
/* 410 */               i2 = (byte)(i2 ^ k);
/*     */             }
/*     */ 
/* 413 */             paramArrayOfByte[i4] = i2;
/* 414 */             i4++;
/*     */           }
/*     */ 
/* 417 */           i5 -= i3;
/* 418 */           i6 = i5 > 64 ? 64 : i5;
/*     */         }
/*     */ 
/* 421 */         if ((i4 == paramInt1) && (i3 < 0)) {
/* 422 */           return i3;
/*     */         }
/*     */ 
/* 425 */         return i4 - paramInt1;
/*     */       }
/*     */ 
/* 430 */       int j = paramInt2 / 2;
/* 431 */       int k = paramInt1 + paramInt2 / 2;
/* 432 */       int m = super.read(paramArrayOfByte, k, j);
/*     */ 
/* 434 */       for (int i = paramInt1; i < paramInt1 + m * 2; i += 2) {
/* 435 */         paramArrayOfByte[i] = this.tabByte1[(paramArrayOfByte[k] & 0xFF)];
/* 436 */         paramArrayOfByte[(i + 1)] = this.tabByte2[(paramArrayOfByte[k] & 0xFF)];
/* 437 */         k++;
/*     */       }
/*     */ 
/* 440 */       if (m < 0) {
/* 441 */         return m;
/*     */       }
/*     */ 
/* 444 */       return i - paramInt1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.AlawCodec
 * JD-Core Version:    0.6.2
 */