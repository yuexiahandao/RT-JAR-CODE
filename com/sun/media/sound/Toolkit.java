/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ import javax.sound.sampled.AudioFormat.Encoding;
/*     */ import javax.sound.sampled.AudioInputStream;
/*     */ import javax.sound.sampled.AudioSystem;
/*     */ 
/*     */ public final class Toolkit
/*     */ {
/*     */   static void getUnsigned8(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/*  50 */     for (int i = paramInt1; i < paramInt1 + paramInt2; i++)
/*     */     {
/*     */       int tmp11_10 = i; paramArrayOfByte[tmp11_10] = ((byte)(paramArrayOfByte[tmp11_10] + 128));
/*     */     }
/*     */   }
/*     */ 
/*     */   static void getByteSwapped(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/*  63 */     for (int j = paramInt1; j < paramInt1 + paramInt2; j += 2)
/*     */     {
/*  65 */       int i = paramArrayOfByte[j];
/*  66 */       paramArrayOfByte[j] = paramArrayOfByte[(j + 1)];
/*  67 */       paramArrayOfByte[(j + 1)] = i;
/*     */     }
/*     */   }
/*     */ 
/*     */   static float linearToDB(float paramFloat)
/*     */   {
/*  77 */     float f = (float)(Math.log(paramFloat == 0.0D ? 0.0001D : paramFloat) / Math.log(10.0D) * 20.0D);
/*  78 */     return f;
/*     */   }
/*     */ 
/*     */   static float dBToLinear(float paramFloat)
/*     */   {
/*  87 */     float f = (float)Math.pow(10.0D, paramFloat / 20.0D);
/*  88 */     return f;
/*     */   }
/*     */ 
/*     */   static long align(long paramLong, int paramInt)
/*     */   {
/*  97 */     if (paramInt <= 1) {
/*  98 */       return paramLong;
/*     */     }
/* 100 */     return paramLong - paramLong % paramInt;
/*     */   }
/*     */ 
/*     */   static int align(int paramInt1, int paramInt2)
/*     */   {
/* 105 */     if (paramInt2 <= 1) {
/* 106 */       return paramInt1;
/*     */     }
/* 108 */     return paramInt1 - paramInt1 % paramInt2;
/*     */   }
/*     */ 
/*     */   static long millis2bytes(AudioFormat paramAudioFormat, long paramLong)
/*     */   {
/* 116 */     long l = ()((float)paramLong * paramAudioFormat.getFrameRate() / 1000.0F * paramAudioFormat.getFrameSize());
/* 117 */     return align(l, paramAudioFormat.getFrameSize());
/*     */   }
/*     */ 
/*     */   static long bytes2millis(AudioFormat paramAudioFormat, long paramLong)
/*     */   {
/* 124 */     return ()((float)paramLong / paramAudioFormat.getFrameRate() * 1000.0F / paramAudioFormat.getFrameSize());
/*     */   }
/*     */ 
/*     */   static long micros2bytes(AudioFormat paramAudioFormat, long paramLong)
/*     */   {
/* 131 */     long l = ()((float)paramLong * paramAudioFormat.getFrameRate() / 1000000.0F * paramAudioFormat.getFrameSize());
/* 132 */     return align(l, paramAudioFormat.getFrameSize());
/*     */   }
/*     */ 
/*     */   static long bytes2micros(AudioFormat paramAudioFormat, long paramLong)
/*     */   {
/* 139 */     return ()((float)paramLong / paramAudioFormat.getFrameRate() * 1000000.0F / paramAudioFormat.getFrameSize());
/*     */   }
/*     */ 
/*     */   static long micros2frames(AudioFormat paramAudioFormat, long paramLong)
/*     */   {
/* 146 */     return ()((float)paramLong * paramAudioFormat.getFrameRate() / 1000000.0F);
/*     */   }
/*     */ 
/*     */   static long frames2micros(AudioFormat paramAudioFormat, long paramLong)
/*     */   {
/* 153 */     return ()(paramLong / paramAudioFormat.getFrameRate() * 1000000.0D);
/*     */   }
/*     */ 
/*     */   static void isFullySpecifiedAudioFormat(AudioFormat paramAudioFormat) {
/* 157 */     if ((!paramAudioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED)) && (!paramAudioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)) && (!paramAudioFormat.getEncoding().equals(AudioFormat.Encoding.ULAW)) && (!paramAudioFormat.getEncoding().equals(AudioFormat.Encoding.ALAW)))
/*     */     {
/* 162 */       return;
/*     */     }
/* 164 */     if (paramAudioFormat.getFrameRate() <= 0.0F) {
/* 165 */       throw new IllegalArgumentException("invalid frame rate: " + (paramAudioFormat.getFrameRate() == -1.0F ? "NOT_SPECIFIED" : String.valueOf(paramAudioFormat.getFrameRate())));
/*     */     }
/*     */ 
/* 169 */     if (paramAudioFormat.getSampleRate() <= 0.0F) {
/* 170 */       throw new IllegalArgumentException("invalid sample rate: " + (paramAudioFormat.getSampleRate() == -1.0F ? "NOT_SPECIFIED" : String.valueOf(paramAudioFormat.getSampleRate())));
/*     */     }
/*     */ 
/* 174 */     if (paramAudioFormat.getSampleSizeInBits() <= 0) {
/* 175 */       throw new IllegalArgumentException("invalid sample size in bits: " + (paramAudioFormat.getSampleSizeInBits() == -1 ? "NOT_SPECIFIED" : String.valueOf(paramAudioFormat.getSampleSizeInBits())));
/*     */     }
/*     */ 
/* 179 */     if (paramAudioFormat.getFrameSize() <= 0) {
/* 180 */       throw new IllegalArgumentException("invalid frame size: " + (paramAudioFormat.getFrameSize() == -1 ? "NOT_SPECIFIED" : String.valueOf(paramAudioFormat.getFrameSize())));
/*     */     }
/*     */ 
/* 184 */     if (paramAudioFormat.getChannels() <= 0)
/* 185 */       throw new IllegalArgumentException("invalid number of channels: " + (paramAudioFormat.getChannels() == -1 ? "NOT_SPECIFIED" : String.valueOf(paramAudioFormat.getChannels())));
/*     */   }
/*     */ 
/*     */   static boolean isFullySpecifiedPCMFormat(AudioFormat paramAudioFormat)
/*     */   {
/* 193 */     if ((!paramAudioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED)) && (!paramAudioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)))
/*     */     {
/* 195 */       return false;
/*     */     }
/* 197 */     if ((paramAudioFormat.getFrameRate() <= 0.0F) || (paramAudioFormat.getSampleRate() <= 0.0F) || (paramAudioFormat.getSampleSizeInBits() <= 0) || (paramAudioFormat.getFrameSize() <= 0) || (paramAudioFormat.getChannels() <= 0))
/*     */     {
/* 202 */       return false;
/*     */     }
/* 204 */     return true;
/*     */   }
/*     */ 
/*     */   public static AudioInputStream getPCMConvertedAudioInputStream(AudioInputStream paramAudioInputStream)
/*     */   {
/* 211 */     AudioFormat localAudioFormat1 = paramAudioInputStream.getFormat();
/*     */ 
/* 213 */     if ((!localAudioFormat1.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED)) && (!localAudioFormat1.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)))
/*     */     {
/*     */       try
/*     */       {
/* 217 */         AudioFormat localAudioFormat2 = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, localAudioFormat1.getSampleRate(), 16, localAudioFormat1.getChannels(), localAudioFormat1.getChannels() * 2, localAudioFormat1.getSampleRate(), Platform.isBigEndian());
/*     */ 
/* 225 */         paramAudioInputStream = AudioSystem.getAudioInputStream(localAudioFormat2, paramAudioInputStream);
/*     */       }
/*     */       catch (Exception localException) {
/* 228 */         paramAudioInputStream = null;
/*     */       }
/*     */     }
/*     */ 
/* 232 */     return paramAudioInputStream;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.Toolkit
 * JD-Core Version:    0.6.2
 */