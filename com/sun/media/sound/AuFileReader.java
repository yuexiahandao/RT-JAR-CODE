/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.DataInputStream;
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
/*     */ public final class AuFileReader extends SunFileReader
/*     */ {
/*     */   public AudioFileFormat getAudioFileFormat(InputStream paramInputStream)
/*     */     throws UnsupportedAudioFileException, IOException
/*     */   {
/*  81 */     AudioFormat localAudioFormat = null;
/*  82 */     AuFileFormat localAuFileFormat = null;
/*  83 */     int i = 28;
/*  84 */     boolean bool = false;
/*  85 */     int j = -1;
/*  86 */     int k = -1;
/*  87 */     int m = -1;
/*  88 */     int n = -1;
/*  89 */     int i1 = -1;
/*  90 */     int i2 = -1;
/*  91 */     int i3 = -1;
/*  92 */     int i4 = -1;
/*  93 */     int i5 = 0;
/*  94 */     int i6 = 0;
/*  95 */     int i7 = 0;
/*  96 */     AudioFormat.Encoding localEncoding = null;
/*     */ 
/*  98 */     DataInputStream localDataInputStream = new DataInputStream(paramInputStream);
/*     */ 
/* 100 */     localDataInputStream.mark(i);
/*     */ 
/* 102 */     j = localDataInputStream.readInt();
/*     */ 
/* 104 */     if ((j != 779316836) || (j == 779314176) || (j == 1684960046) || (j == 6583086))
/*     */     {
/* 108 */       localDataInputStream.reset();
/* 109 */       throw new UnsupportedAudioFileException("not an AU file");
/*     */     }
/*     */ 
/* 112 */     if ((j == 779316836) || (j == 779314176)) {
/* 113 */       bool = true;
/*     */     }
/*     */ 
/* 116 */     k = bool == true ? localDataInputStream.readInt() : rllong(localDataInputStream); i7 += 4;
/* 117 */     m = bool == true ? localDataInputStream.readInt() : rllong(localDataInputStream); i7 += 4;
/* 118 */     n = bool == true ? localDataInputStream.readInt() : rllong(localDataInputStream); i7 += 4;
/* 119 */     i1 = bool == true ? localDataInputStream.readInt() : rllong(localDataInputStream); i7 += 4;
/* 120 */     i4 = bool == true ? localDataInputStream.readInt() : rllong(localDataInputStream); i7 += 4;
/*     */ 
/* 122 */     i2 = i1;
/*     */ 
/* 124 */     switch (n) {
/*     */     case 1:
/* 126 */       localEncoding = AudioFormat.Encoding.ULAW;
/* 127 */       i5 = 8;
/* 128 */       break;
/*     */     case 27:
/* 130 */       localEncoding = AudioFormat.Encoding.ALAW;
/* 131 */       i5 = 8;
/* 132 */       break;
/*     */     case 2:
/* 135 */       localEncoding = AudioFormat.Encoding.PCM_SIGNED;
/* 136 */       i5 = 8;
/* 137 */       break;
/*     */     case 3:
/* 139 */       localEncoding = AudioFormat.Encoding.PCM_SIGNED;
/* 140 */       i5 = 16;
/* 141 */       break;
/*     */     case 4:
/* 143 */       localEncoding = AudioFormat.Encoding.PCM_SIGNED;
/*     */ 
/* 145 */       i5 = 24;
/* 146 */       break;
/*     */     case 5:
/* 148 */       localEncoding = AudioFormat.Encoding.PCM_SIGNED;
/*     */ 
/* 150 */       i5 = 32;
/* 151 */       break;
/*     */     default:
/* 178 */       localDataInputStream.reset();
/* 179 */       throw new UnsupportedAudioFileException("not a valid AU file");
/*     */     }
/*     */ 
/* 182 */     i3 = calculatePCMFrameSize(i5, i4);
/*     */ 
/* 184 */     if (m < 0) {
/* 185 */       i6 = -1;
/*     */     }
/*     */     else {
/* 188 */       i6 = m / i3;
/*     */     }
/*     */ 
/* 191 */     localAudioFormat = new AudioFormat(localEncoding, i1, i5, i4, i3, i2, bool);
/*     */ 
/* 194 */     localAuFileFormat = new AuFileFormat(AudioFileFormat.Type.AU, m + k, localAudioFormat, i6);
/*     */ 
/* 197 */     localDataInputStream.reset();
/* 198 */     return localAuFileFormat;
/*     */   }
/*     */ 
/*     */   public AudioFileFormat getAudioFileFormat(URL paramURL)
/*     */     throws UnsupportedAudioFileException, IOException
/*     */   {
/* 215 */     InputStream localInputStream = null;
/* 216 */     BufferedInputStream localBufferedInputStream = null;
/* 217 */     AudioFileFormat localAudioFileFormat = null;
/* 218 */     Object localObject1 = null;
/*     */ 
/* 220 */     localInputStream = paramURL.openStream();
/*     */     try
/*     */     {
/* 223 */       localBufferedInputStream = new BufferedInputStream(localInputStream, 4096);
/*     */ 
/* 225 */       localAudioFileFormat = getAudioFileFormat(localBufferedInputStream);
/*     */     } finally {
/* 227 */       localInputStream.close();
/*     */     }
/*     */ 
/* 230 */     return localAudioFileFormat;
/*     */   }
/*     */ 
/*     */   public AudioFileFormat getAudioFileFormat(File paramFile)
/*     */     throws UnsupportedAudioFileException, IOException
/*     */   {
/* 246 */     FileInputStream localFileInputStream = null;
/* 247 */     BufferedInputStream localBufferedInputStream = null;
/* 248 */     AudioFileFormat localAudioFileFormat = null;
/* 249 */     Object localObject1 = null;
/*     */ 
/* 251 */     localFileInputStream = new FileInputStream(paramFile);
/*     */     try
/*     */     {
/* 254 */       localBufferedInputStream = new BufferedInputStream(localFileInputStream, 4096);
/* 255 */       localAudioFileFormat = getAudioFileFormat(localBufferedInputStream);
/*     */     } finally {
/* 257 */       localFileInputStream.close();
/*     */     }
/*     */ 
/* 260 */     return localAudioFileFormat;
/*     */   }
/*     */ 
/*     */   public AudioInputStream getAudioInputStream(InputStream paramInputStream)
/*     */     throws UnsupportedAudioFileException, IOException
/*     */   {
/* 285 */     DataInputStream localDataInputStream = null;
/*     */ 
/* 287 */     AudioFileFormat localAudioFileFormat = null;
/* 288 */     AudioFormat localAudioFormat = null;
/*     */ 
/* 291 */     localAudioFileFormat = getAudioFileFormat(paramInputStream);
/*     */ 
/* 295 */     localAudioFormat = localAudioFileFormat.getFormat();
/*     */ 
/* 297 */     localDataInputStream = new DataInputStream(paramInputStream);
/*     */ 
/* 301 */     localDataInputStream.readInt();
/* 302 */     int i = localAudioFormat.isBigEndian() == true ? localDataInputStream.readInt() : rllong(localDataInputStream);
/* 303 */     localDataInputStream.skipBytes(i - 8);
/*     */ 
/* 309 */     return new AudioInputStream(localDataInputStream, localAudioFormat, localAudioFileFormat.getFrameLength());
/*     */   }
/*     */ 
/*     */   public AudioInputStream getAudioInputStream(URL paramURL)
/*     */     throws UnsupportedAudioFileException, IOException
/*     */   {
/* 326 */     InputStream localInputStream = null;
/* 327 */     BufferedInputStream localBufferedInputStream = null;
/* 328 */     Object localObject1 = null;
/*     */ 
/* 330 */     localInputStream = paramURL.openStream();
/* 331 */     AudioInputStream localAudioInputStream = null;
/*     */     try {
/* 333 */       localBufferedInputStream = new BufferedInputStream(localInputStream, 4096);
/* 334 */       localAudioInputStream = getAudioInputStream(localBufferedInputStream);
/*     */     } finally {
/* 336 */       if (localAudioInputStream == null) {
/* 337 */         localInputStream.close();
/*     */       }
/*     */     }
/* 340 */     return localAudioInputStream;
/*     */   }
/*     */ 
/*     */   public AudioInputStream getAudioInputStream(File paramFile)
/*     */     throws UnsupportedAudioFileException, IOException
/*     */   {
/* 357 */     FileInputStream localFileInputStream = null;
/* 358 */     BufferedInputStream localBufferedInputStream = null;
/* 359 */     Object localObject1 = null;
/*     */ 
/* 361 */     localFileInputStream = new FileInputStream(paramFile);
/* 362 */     AudioInputStream localAudioInputStream = null;
/*     */     try
/*     */     {
/* 365 */       localBufferedInputStream = new BufferedInputStream(localFileInputStream, 4096);
/* 366 */       localAudioInputStream = getAudioInputStream(localBufferedInputStream);
/*     */     } finally {
/* 368 */       if (localAudioInputStream == null) {
/* 369 */         localFileInputStream.close();
/*     */       }
/*     */     }
/*     */ 
/* 373 */     return localAudioInputStream;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.AuFileReader
 * JD-Core Version:    0.6.2
 */