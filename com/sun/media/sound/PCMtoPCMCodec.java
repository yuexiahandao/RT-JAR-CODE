/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Vector;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ import javax.sound.sampled.AudioFormat.Encoding;
/*     */ import javax.sound.sampled.AudioInputStream;
/*     */ 
/*     */ public final class PCMtoPCMCodec extends SunCodec
/*     */ {
/*  44 */   private static final AudioFormat.Encoding[] inputEncodings = { AudioFormat.Encoding.PCM_SIGNED, AudioFormat.Encoding.PCM_UNSIGNED };
/*     */ 
/*  49 */   private static final AudioFormat.Encoding[] outputEncodings = { AudioFormat.Encoding.PCM_SIGNED, AudioFormat.Encoding.PCM_UNSIGNED };
/*     */   private static final int tempBufferSize = 64;
/*  57 */   private byte[] tempBuffer = null;
/*     */ 
/*     */   public PCMtoPCMCodec()
/*     */   {
/*  64 */     super(inputEncodings, outputEncodings);
/*     */   }
/*     */ 
/*     */   public AudioFormat.Encoding[] getTargetEncodings(AudioFormat paramAudioFormat)
/*     */   {
/*  74 */     if ((paramAudioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED)) || (paramAudioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)))
/*     */     {
/*  77 */       AudioFormat.Encoding[] arrayOfEncoding = new AudioFormat.Encoding[2];
/*  78 */       arrayOfEncoding[0] = AudioFormat.Encoding.PCM_SIGNED;
/*  79 */       arrayOfEncoding[1] = AudioFormat.Encoding.PCM_UNSIGNED;
/*  80 */       return arrayOfEncoding;
/*     */     }
/*  82 */     return new AudioFormat.Encoding[0];
/*     */   }
/*     */ 
/*     */   public AudioFormat[] getTargetFormats(AudioFormat.Encoding paramEncoding, AudioFormat paramAudioFormat)
/*     */   {
/*  93 */     AudioFormat[] arrayOfAudioFormat1 = getOutputFormats(paramAudioFormat);
/*  94 */     Vector localVector = new Vector();
/*  95 */     for (int i = 0; i < arrayOfAudioFormat1.length; i++) {
/*  96 */       if (arrayOfAudioFormat1[i].getEncoding().equals(paramEncoding)) {
/*  97 */         localVector.addElement(arrayOfAudioFormat1[i]);
/*     */       }
/*     */     }
/*     */ 
/* 101 */     AudioFormat[] arrayOfAudioFormat2 = new AudioFormat[localVector.size()];
/*     */ 
/* 103 */     for (int j = 0; j < arrayOfAudioFormat2.length; j++) {
/* 104 */       arrayOfAudioFormat2[j] = ((AudioFormat)(AudioFormat)localVector.elementAt(j));
/*     */     }
/*     */ 
/* 107 */     return arrayOfAudioFormat2;
/*     */   }
/*     */ 
/*     */   public AudioInputStream getAudioInputStream(AudioFormat.Encoding paramEncoding, AudioInputStream paramAudioInputStream)
/*     */   {
/* 115 */     if (isConversionSupported(paramEncoding, paramAudioInputStream.getFormat()))
/*     */     {
/* 117 */       AudioFormat localAudioFormat1 = paramAudioInputStream.getFormat();
/* 118 */       AudioFormat localAudioFormat2 = new AudioFormat(paramEncoding, localAudioFormat1.getSampleRate(), localAudioFormat1.getSampleSizeInBits(), localAudioFormat1.getChannels(), localAudioFormat1.getFrameSize(), localAudioFormat1.getFrameRate(), localAudioFormat1.isBigEndian());
/*     */ 
/* 126 */       return getAudioInputStream(localAudioFormat2, paramAudioInputStream);
/*     */     }
/*     */ 
/* 129 */     throw new IllegalArgumentException("Unsupported conversion: " + paramAudioInputStream.getFormat().toString() + " to " + paramEncoding.toString());
/*     */   }
/*     */ 
/*     */   public AudioInputStream getAudioInputStream(AudioFormat paramAudioFormat, AudioInputStream paramAudioInputStream)
/*     */   {
/* 138 */     return getConvertedStream(paramAudioFormat, paramAudioInputStream);
/*     */   }
/*     */ 
/*     */   private AudioInputStream getConvertedStream(AudioFormat paramAudioFormat, AudioInputStream paramAudioInputStream)
/*     */   {
/* 157 */     Object localObject = null;
/*     */ 
/* 159 */     AudioFormat localAudioFormat = paramAudioInputStream.getFormat();
/*     */ 
/* 161 */     if (localAudioFormat.matches(paramAudioFormat))
/*     */     {
/* 163 */       localObject = paramAudioInputStream;
/*     */     }
/*     */     else {
/* 166 */       localObject = new PCMtoPCMCodecStream(paramAudioInputStream, paramAudioFormat);
/* 167 */       this.tempBuffer = new byte[64];
/*     */     }
/* 169 */     return localObject;
/*     */   }
/*     */ 
/*     */   private AudioFormat[] getOutputFormats(AudioFormat paramAudioFormat)
/*     */   {
/* 184 */     Vector localVector = new Vector();
/*     */ 
/* 187 */     int i = paramAudioFormat.getSampleSizeInBits();
/* 188 */     boolean bool = paramAudioFormat.isBigEndian();
/*     */     AudioFormat localAudioFormat;
/* 191 */     if (i == 8) {
/* 192 */       if (AudioFormat.Encoding.PCM_SIGNED.equals(paramAudioFormat.getEncoding()))
/*     */       {
/* 194 */         localAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, paramAudioFormat.getSampleRate(), paramAudioFormat.getSampleSizeInBits(), paramAudioFormat.getChannels(), paramAudioFormat.getFrameSize(), paramAudioFormat.getFrameRate(), false);
/*     */ 
/* 201 */         localVector.addElement(localAudioFormat);
/*     */       }
/*     */ 
/* 204 */       if (AudioFormat.Encoding.PCM_UNSIGNED.equals(paramAudioFormat.getEncoding()))
/*     */       {
/* 206 */         localAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, paramAudioFormat.getSampleRate(), paramAudioFormat.getSampleSizeInBits(), paramAudioFormat.getChannels(), paramAudioFormat.getFrameSize(), paramAudioFormat.getFrameRate(), false);
/*     */ 
/* 213 */         localVector.addElement(localAudioFormat);
/*     */       }
/*     */     }
/* 216 */     else if (i == 16)
/*     */     {
/* 218 */       if ((AudioFormat.Encoding.PCM_SIGNED.equals(paramAudioFormat.getEncoding())) && (bool))
/*     */       {
/* 220 */         localAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, paramAudioFormat.getSampleRate(), paramAudioFormat.getSampleSizeInBits(), paramAudioFormat.getChannels(), paramAudioFormat.getFrameSize(), paramAudioFormat.getFrameRate(), true);
/*     */ 
/* 227 */         localVector.addElement(localAudioFormat);
/* 228 */         localAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, paramAudioFormat.getSampleRate(), paramAudioFormat.getSampleSizeInBits(), paramAudioFormat.getChannels(), paramAudioFormat.getFrameSize(), paramAudioFormat.getFrameRate(), false);
/*     */ 
/* 235 */         localVector.addElement(localAudioFormat);
/* 236 */         localAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, paramAudioFormat.getSampleRate(), paramAudioFormat.getSampleSizeInBits(), paramAudioFormat.getChannels(), paramAudioFormat.getFrameSize(), paramAudioFormat.getFrameRate(), false);
/*     */ 
/* 243 */         localVector.addElement(localAudioFormat);
/*     */       }
/*     */ 
/* 246 */       if ((AudioFormat.Encoding.PCM_UNSIGNED.equals(paramAudioFormat.getEncoding())) && (bool))
/*     */       {
/* 248 */         localAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, paramAudioFormat.getSampleRate(), paramAudioFormat.getSampleSizeInBits(), paramAudioFormat.getChannels(), paramAudioFormat.getFrameSize(), paramAudioFormat.getFrameRate(), true);
/*     */ 
/* 255 */         localVector.addElement(localAudioFormat);
/* 256 */         localAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, paramAudioFormat.getSampleRate(), paramAudioFormat.getSampleSizeInBits(), paramAudioFormat.getChannels(), paramAudioFormat.getFrameSize(), paramAudioFormat.getFrameRate(), false);
/*     */ 
/* 263 */         localVector.addElement(localAudioFormat);
/* 264 */         localAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, paramAudioFormat.getSampleRate(), paramAudioFormat.getSampleSizeInBits(), paramAudioFormat.getChannels(), paramAudioFormat.getFrameSize(), paramAudioFormat.getFrameRate(), false);
/*     */ 
/* 271 */         localVector.addElement(localAudioFormat);
/*     */       }
/*     */ 
/* 274 */       if ((AudioFormat.Encoding.PCM_SIGNED.equals(paramAudioFormat.getEncoding())) && (!bool))
/*     */       {
/* 276 */         localAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, paramAudioFormat.getSampleRate(), paramAudioFormat.getSampleSizeInBits(), paramAudioFormat.getChannels(), paramAudioFormat.getFrameSize(), paramAudioFormat.getFrameRate(), false);
/*     */ 
/* 283 */         localVector.addElement(localAudioFormat);
/* 284 */         localAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, paramAudioFormat.getSampleRate(), paramAudioFormat.getSampleSizeInBits(), paramAudioFormat.getChannels(), paramAudioFormat.getFrameSize(), paramAudioFormat.getFrameRate(), true);
/*     */ 
/* 291 */         localVector.addElement(localAudioFormat);
/* 292 */         localAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, paramAudioFormat.getSampleRate(), paramAudioFormat.getSampleSizeInBits(), paramAudioFormat.getChannels(), paramAudioFormat.getFrameSize(), paramAudioFormat.getFrameRate(), true);
/*     */ 
/* 299 */         localVector.addElement(localAudioFormat);
/*     */       }
/*     */ 
/* 302 */       if ((AudioFormat.Encoding.PCM_UNSIGNED.equals(paramAudioFormat.getEncoding())) && (!bool))
/*     */       {
/* 304 */         localAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, paramAudioFormat.getSampleRate(), paramAudioFormat.getSampleSizeInBits(), paramAudioFormat.getChannels(), paramAudioFormat.getFrameSize(), paramAudioFormat.getFrameRate(), false);
/*     */ 
/* 311 */         localVector.addElement(localAudioFormat);
/* 312 */         localAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, paramAudioFormat.getSampleRate(), paramAudioFormat.getSampleSizeInBits(), paramAudioFormat.getChannels(), paramAudioFormat.getFrameSize(), paramAudioFormat.getFrameRate(), true);
/*     */ 
/* 319 */         localVector.addElement(localAudioFormat);
/* 320 */         localAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, paramAudioFormat.getSampleRate(), paramAudioFormat.getSampleSizeInBits(), paramAudioFormat.getChannels(), paramAudioFormat.getFrameSize(), paramAudioFormat.getFrameRate(), true);
/*     */ 
/* 327 */         localVector.addElement(localAudioFormat);
/*     */       }
/*     */     }
/*     */     AudioFormat[] arrayOfAudioFormat;
/* 332 */     synchronized (localVector)
/*     */     {
/* 334 */       arrayOfAudioFormat = new AudioFormat[localVector.size()];
/*     */ 
/* 336 */       for (int j = 0; j < arrayOfAudioFormat.length; j++)
/*     */       {
/* 338 */         arrayOfAudioFormat[j] = ((AudioFormat)(AudioFormat)localVector.elementAt(j));
/*     */       }
/*     */     }
/*     */ 
/* 342 */     return arrayOfAudioFormat;
/*     */   }
/*     */ 
/*     */   class PCMtoPCMCodecStream extends AudioInputStream
/*     */   {
/* 348 */     private final int PCM_SWITCH_SIGNED_8BIT = 1;
/* 349 */     private final int PCM_SWITCH_ENDIAN = 2;
/* 350 */     private final int PCM_SWITCH_SIGNED_LE = 3;
/* 351 */     private final int PCM_SWITCH_SIGNED_BE = 4;
/* 352 */     private final int PCM_UNSIGNED_LE2SIGNED_BE = 5;
/* 353 */     private final int PCM_SIGNED_LE2UNSIGNED_BE = 6;
/* 354 */     private final int PCM_UNSIGNED_BE2SIGNED_LE = 7;
/* 355 */     private final int PCM_SIGNED_BE2UNSIGNED_LE = 8;
/*     */     private final int sampleSizeInBytes;
/* 358 */     private int conversionType = 0;
/*     */ 
/*     */     PCMtoPCMCodecStream(AudioInputStream paramAudioFormat, AudioFormat arg3)
/*     */     {
/* 363 */       super(localAudioFormat1, -1L);
/*     */ 
/* 365 */       int i = 0;
/* 366 */       AudioFormat.Encoding localEncoding1 = null;
/* 367 */       AudioFormat.Encoding localEncoding2 = null;
/*     */ 
/* 371 */       AudioFormat localAudioFormat2 = paramAudioFormat.getFormat();
/*     */ 
/* 374 */       if (!PCMtoPCMCodec.this.isConversionSupported(localAudioFormat2, localAudioFormat1))
/*     */       {
/* 376 */         throw new IllegalArgumentException("Unsupported conversion: " + localAudioFormat2.toString() + " to " + localAudioFormat1.toString());
/*     */       }
/*     */ 
/* 379 */       localEncoding1 = localAudioFormat2.getEncoding();
/* 380 */       localEncoding2 = localAudioFormat1.getEncoding();
/* 381 */       boolean bool1 = localAudioFormat2.isBigEndian();
/* 382 */       boolean bool2 = localAudioFormat1.isBigEndian();
/* 383 */       i = localAudioFormat2.getSampleSizeInBits();
/* 384 */       this.sampleSizeInBytes = (i / 8);
/*     */ 
/* 388 */       if (i == 8) {
/* 389 */         if ((AudioFormat.Encoding.PCM_UNSIGNED.equals(localEncoding1)) && (AudioFormat.Encoding.PCM_SIGNED.equals(localEncoding2)))
/*     */         {
/* 391 */           this.conversionType = 1;
/*     */         }
/* 394 */         else if ((AudioFormat.Encoding.PCM_SIGNED.equals(localEncoding1)) && (AudioFormat.Encoding.PCM_UNSIGNED.equals(localEncoding2)))
/*     */         {
/* 396 */           this.conversionType = 1;
/*     */         }
/*     */ 
/*     */       }
/* 401 */       else if ((localEncoding1.equals(localEncoding2)) && (bool1 != bool2))
/*     */       {
/* 403 */         this.conversionType = 2;
/*     */       }
/* 407 */       else if ((AudioFormat.Encoding.PCM_UNSIGNED.equals(localEncoding1)) && (!bool1) && (AudioFormat.Encoding.PCM_SIGNED.equals(localEncoding2)) && (bool2))
/*     */       {
/* 410 */         this.conversionType = 5;
/*     */       }
/* 413 */       else if ((AudioFormat.Encoding.PCM_SIGNED.equals(localEncoding1)) && (!bool1) && (AudioFormat.Encoding.PCM_UNSIGNED.equals(localEncoding2)) && (bool2))
/*     */       {
/* 416 */         this.conversionType = 6;
/*     */       }
/* 419 */       else if ((AudioFormat.Encoding.PCM_UNSIGNED.equals(localEncoding1)) && (bool1) && (AudioFormat.Encoding.PCM_SIGNED.equals(localEncoding2)) && (!bool2))
/*     */       {
/* 422 */         this.conversionType = 7;
/*     */       }
/* 425 */       else if ((AudioFormat.Encoding.PCM_SIGNED.equals(localEncoding1)) && (bool1) && (AudioFormat.Encoding.PCM_UNSIGNED.equals(localEncoding2)) && (!bool2))
/*     */       {
/* 428 */         this.conversionType = 8;
/*     */       }
/*     */ 
/* 436 */       this.frameSize = localAudioFormat2.getFrameSize();
/* 437 */       if (this.frameSize == -1) {
/* 438 */         this.frameSize = 1;
/*     */       }
/* 440 */       if ((paramAudioFormat instanceof AudioInputStream))
/* 441 */         this.frameLength = paramAudioFormat.getFrameLength();
/*     */       else {
/* 443 */         this.frameLength = -1L;
/*     */       }
/*     */ 
/* 447 */       this.framePos = 0L;
/*     */     }
/*     */ 
/*     */     public int read()
/*     */       throws IOException
/*     */     {
/* 463 */       if (this.frameSize == 1) {
/* 464 */         if (this.conversionType == 1) {
/* 465 */           int i = super.read();
/*     */ 
/* 467 */           if (i < 0) return i;
/*     */ 
/* 469 */           int j = (byte)(i & 0xF);
/* 470 */           j = j >= 0 ? (byte)(0x80 | j) : (byte)(0x7F & j);
/* 471 */           i = j & 0xF;
/*     */ 
/* 473 */           return i;
/*     */         }
/*     */ 
/* 477 */         throw new IOException("cannot read a single byte if frame size > 1");
/*     */       }
/*     */ 
/* 480 */       throw new IOException("cannot read a single byte if frame size > 1");
/*     */     }
/*     */ 
/*     */     public int read(byte[] paramArrayOfByte)
/*     */       throws IOException
/*     */     {
/* 487 */       return read(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */     }
/*     */ 
/*     */     public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */       throws IOException
/*     */     {
/* 496 */       if (paramInt2 % this.frameSize != 0) {
/* 497 */         paramInt2 -= paramInt2 % this.frameSize;
/*     */       }
/*     */ 
/* 500 */       if ((this.frameLength != -1L) && (paramInt2 / this.frameSize > this.frameLength - this.framePos)) {
/* 501 */         paramInt2 = (int)(this.frameLength - this.framePos) * this.frameSize;
/*     */       }
/*     */ 
/* 504 */       int i = super.read(paramArrayOfByte, paramInt1, paramInt2);
/*     */ 
/* 507 */       if (i < 0) {
/* 508 */         return i;
/*     */       }
/*     */ 
/* 513 */       switch (this.conversionType)
/*     */       {
/*     */       case 1:
/* 516 */         switchSigned8bit(paramArrayOfByte, paramInt1, paramInt2, i);
/* 517 */         break;
/*     */       case 2:
/* 520 */         switchEndian(paramArrayOfByte, paramInt1, paramInt2, i);
/* 521 */         break;
/*     */       case 3:
/* 524 */         switchSignedLE(paramArrayOfByte, paramInt1, paramInt2, i);
/* 525 */         break;
/*     */       case 4:
/* 528 */         switchSignedBE(paramArrayOfByte, paramInt1, paramInt2, i);
/* 529 */         break;
/*     */       case 5:
/*     */       case 6:
/* 533 */         switchSignedLE(paramArrayOfByte, paramInt1, paramInt2, i);
/* 534 */         switchEndian(paramArrayOfByte, paramInt1, paramInt2, i);
/* 535 */         break;
/*     */       case 7:
/*     */       case 8:
/* 539 */         switchSignedBE(paramArrayOfByte, paramInt1, paramInt2, i);
/* 540 */         switchEndian(paramArrayOfByte, paramInt1, paramInt2, i);
/* 541 */         break;
/*     */       }
/*     */ 
/* 548 */       return i;
/*     */     }
/*     */ 
/*     */     private void switchSigned8bit(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
/*     */     {
/* 554 */       for (int i = paramInt1; i < paramInt1 + paramInt3; i++)
/* 555 */         paramArrayOfByte[i] = (paramArrayOfByte[i] >= 0 ? (byte)(0x80 | paramArrayOfByte[i]) : (byte)(0x7F & paramArrayOfByte[i]));
/*     */     }
/*     */ 
/*     */     private void switchSignedBE(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
/*     */     {
/* 561 */       for (int i = paramInt1; i < paramInt1 + paramInt3; i += this.sampleSizeInBytes)
/* 562 */         paramArrayOfByte[i] = (paramArrayOfByte[i] >= 0 ? (byte)(0x80 | paramArrayOfByte[i]) : (byte)(0x7F & paramArrayOfByte[i]));
/*     */     }
/*     */ 
/*     */     private void switchSignedLE(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
/*     */     {
/* 568 */       for (int i = paramInt1 + this.sampleSizeInBytes - 1; i < paramInt1 + paramInt3; i += this.sampleSizeInBytes)
/* 569 */         paramArrayOfByte[i] = (paramArrayOfByte[i] >= 0 ? (byte)(0x80 | paramArrayOfByte[i]) : (byte)(0x7F & paramArrayOfByte[i]));
/*     */     }
/*     */ 
/*     */     private void switchEndian(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
/*     */     {
/* 575 */       if (this.sampleSizeInBytes == 2)
/* 576 */         for (int i = paramInt1; i < paramInt1 + paramInt3; i += this.sampleSizeInBytes)
/*     */         {
/* 578 */           int j = paramArrayOfByte[i];
/* 579 */           paramArrayOfByte[i] = paramArrayOfByte[(i + 1)];
/* 580 */           paramArrayOfByte[(i + 1)] = j;
/*     */         }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.PCMtoPCMCodec
 * JD-Core Version:    0.6.2
 */