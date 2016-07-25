/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import javax.sound.sampled.AudioFileFormat;
/*     */ import javax.sound.sampled.AudioFileFormat.Type;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ import javax.sound.sampled.AudioFormat.Encoding;
/*     */ import javax.sound.sampled.AudioInputStream;
/*     */ import javax.sound.sampled.UnsupportedAudioFileException;
/*     */ 
/*     */ public final class WaveFileReader extends SunFileReader
/*     */ {
/*     */   private static final int MAX_READ_LENGTH = 12;
/*     */ 
/*     */   public AudioFileFormat getAudioFileFormat(InputStream paramInputStream)
/*     */     throws UnsupportedAudioFileException, IOException
/*     */   {
/*  82 */     AudioFileFormat localAudioFileFormat = getFMT(paramInputStream, true);
/*     */ 
/*  85 */     paramInputStream.reset();
/*  86 */     return localAudioFileFormat;
/*     */   }
/*     */ 
/*     */   public AudioFileFormat getAudioFileFormat(URL paramURL)
/*     */     throws UnsupportedAudioFileException, IOException
/*     */   {
/* 101 */     InputStream localInputStream = paramURL.openStream();
/* 102 */     AudioFileFormat localAudioFileFormat = null;
/*     */     try {
/* 104 */       localAudioFileFormat = getFMT(localInputStream, false);
/*     */     } finally {
/* 106 */       localInputStream.close();
/*     */     }
/* 108 */     return localAudioFileFormat;
/*     */   }
/*     */ 
/*     */   public AudioFileFormat getAudioFileFormat(File paramFile)
/*     */     throws UnsupportedAudioFileException, IOException
/*     */   {
/* 123 */     AudioFileFormat localAudioFileFormat = null;
/* 124 */     FileInputStream localFileInputStream = new FileInputStream(paramFile);
/*     */     try
/*     */     {
/* 127 */       localAudioFileFormat = getFMT(localFileInputStream, false);
/*     */     } finally {
/* 129 */       localFileInputStream.close();
/*     */     }
/*     */ 
/* 132 */     return localAudioFileFormat;
/*     */   }
/*     */ 
/*     */   public AudioInputStream getAudioInputStream(InputStream paramInputStream)
/*     */     throws UnsupportedAudioFileException, IOException
/*     */   {
/* 157 */     AudioFileFormat localAudioFileFormat = getFMT(paramInputStream, true);
/*     */ 
/* 161 */     return new AudioInputStream(paramInputStream, localAudioFileFormat.getFormat(), localAudioFileFormat.getFrameLength());
/*     */   }
/*     */ 
/*     */   public AudioInputStream getAudioInputStream(URL paramURL)
/*     */     throws UnsupportedAudioFileException, IOException
/*     */   {
/* 177 */     InputStream localInputStream = paramURL.openStream();
/* 178 */     AudioFileFormat localAudioFileFormat = null;
/*     */     try {
/* 180 */       localAudioFileFormat = getFMT(localInputStream, false);
/*     */     } finally {
/* 182 */       if (localAudioFileFormat == null) {
/* 183 */         localInputStream.close();
/*     */       }
/*     */     }
/* 186 */     return new AudioInputStream(localInputStream, localAudioFileFormat.getFormat(), localAudioFileFormat.getFrameLength());
/*     */   }
/*     */ 
/*     */   public AudioInputStream getAudioInputStream(File paramFile)
/*     */     throws UnsupportedAudioFileException, IOException
/*     */   {
/* 202 */     FileInputStream localFileInputStream = new FileInputStream(paramFile);
/* 203 */     AudioFileFormat localAudioFileFormat = null;
/*     */     try
/*     */     {
/* 206 */       localAudioFileFormat = getFMT(localFileInputStream, false);
/*     */     } finally {
/* 208 */       if (localAudioFileFormat == null) {
/* 209 */         localFileInputStream.close();
/*     */       }
/*     */     }
/* 212 */     return new AudioInputStream(localFileInputStream, localAudioFileFormat.getFormat(), localAudioFileFormat.getFrameLength());
/*     */   }
/*     */ 
/*     */   private AudioFileFormat getFMT(InputStream paramInputStream, boolean paramBoolean)
/*     */     throws UnsupportedAudioFileException, IOException
/*     */   {
/* 224 */     int i = 0;
/*     */ 
/* 226 */     int k = 0;
/* 227 */     int m = 0;
/*     */ 
/* 233 */     AudioFormat.Encoding localEncoding = null;
/*     */ 
/* 235 */     DataInputStream localDataInputStream = new DataInputStream(paramInputStream);
/*     */ 
/* 237 */     if (paramBoolean) {
/* 238 */       localDataInputStream.mark(12);
/*     */     }
/*     */ 
/* 241 */     int i3 = localDataInputStream.readInt();
/* 242 */     int i4 = rllong(localDataInputStream);
/* 243 */     int i5 = localDataInputStream.readInt();
/*     */     int i6;
/* 245 */     if (i4 <= 0) {
/* 246 */       i4 = -1;
/* 247 */       i6 = -1;
/*     */     } else {
/* 249 */       i6 = i4 + 8;
/*     */     }
/*     */ 
/* 252 */     if ((i3 != 1380533830) || (i5 != 1463899717))
/*     */     {
/* 254 */       if (paramBoolean) {
/* 255 */         localDataInputStream.reset();
/*     */       }
/* 257 */       throw new UnsupportedAudioFileException("not a WAVE file");
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/*     */       while (true)
/*     */       {
/* 265 */         int j = localDataInputStream.readInt();
/* 266 */         i += 4;
/* 267 */         if (j == 1718449184)
/*     */         {
/*     */           break;
/*     */         }
/*     */ 
/* 272 */         k = rllong(localDataInputStream);
/* 273 */         i += 4;
/* 274 */         if (k % 2 > 0) k++;
/* 275 */         i += localDataInputStream.skipBytes(k);
/*     */       }
/*     */     }
/*     */     catch (EOFException localEOFException1) {
/* 279 */       throw new UnsupportedAudioFileException("Not a valid WAV file");
/*     */     }
/*     */ 
/* 284 */     k = rllong(localDataInputStream);
/* 285 */     i += 4;
/*     */ 
/* 288 */     int i7 = i + k;
/*     */ 
/* 293 */     m = rlshort(localDataInputStream); i += 2;
/*     */ 
/* 295 */     if (m == 1)
/* 296 */       localEncoding = AudioFormat.Encoding.PCM_SIGNED;
/* 297 */     else if (m == 6)
/* 298 */       localEncoding = AudioFormat.Encoding.ALAW;
/* 299 */     else if (m == 7) {
/* 300 */       localEncoding = AudioFormat.Encoding.ULAW;
/*     */     }
/*     */     else {
/* 303 */       throw new UnsupportedAudioFileException("Not a supported WAV file");
/*     */     }
/*     */ 
/* 306 */     int n = rlshort(localDataInputStream); i += 2;
/*     */ 
/* 309 */     long l1 = rllong(localDataInputStream); i += 4;
/*     */ 
/* 312 */     long l2 = rllong(localDataInputStream); i += 4;
/*     */ 
/* 315 */     int i1 = rlshort(localDataInputStream); i += 2;
/*     */ 
/* 318 */     int i2 = rlshort(localDataInputStream); i += 2;
/*     */ 
/* 321 */     if ((i2 == 8) && (localEncoding.equals(AudioFormat.Encoding.PCM_SIGNED))) {
/* 322 */       localEncoding = AudioFormat.Encoding.PCM_UNSIGNED;
/*     */     }
/*     */ 
/* 331 */     if (k % 2 != 0) k++;
/*     */ 
/* 335 */     if (i7 > i) {
/* 336 */       i += localDataInputStream.skipBytes(i7 - i);
/*     */     }
/*     */ 
/* 341 */     i = 0;
/*     */     try {
/*     */       while (true) {
/* 344 */         int i8 = localDataInputStream.readInt();
/* 345 */         i += 4;
/* 346 */         if (i8 == 1684108385)
/*     */         {
/*     */           break;
/*     */         }
/*     */ 
/* 351 */         int i10 = rllong(localDataInputStream); i += 4;
/* 352 */         if (i10 % 2 > 0) i10++;
/* 353 */         i += localDataInputStream.skipBytes(i10);
/*     */       }
/*     */     }
/*     */     catch (EOFException localEOFException2) {
/* 357 */       throw new UnsupportedAudioFileException("Not a valid WAV file");
/*     */     }
/*     */ 
/* 361 */     int i9 = rllong(localDataInputStream); i += 4;
/*     */ 
/* 365 */     AudioFormat localAudioFormat = new AudioFormat(localEncoding, (float)l1, i2, n, calculatePCMFrameSize(i2, n), (float)l1, false);
/*     */ 
/* 371 */     return new WaveFileFormat(AudioFileFormat.Type.WAVE, i6, localAudioFormat, i9 / localAudioFormat.getFrameSize());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.WaveFileReader
 * JD-Core Version:    0.6.2
 */