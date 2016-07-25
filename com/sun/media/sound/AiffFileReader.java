/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
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
/*     */ public final class AiffFileReader extends SunFileReader
/*     */ {
/*     */   private static final int MAX_READ_LENGTH = 8;
/*     */ 
/*     */   public AudioFileFormat getAudioFileFormat(InputStream paramInputStream)
/*     */     throws UnsupportedAudioFileException, IOException
/*     */   {
/*  85 */     AudioFileFormat localAudioFileFormat = getCOMM(paramInputStream, true);
/*     */ 
/*  88 */     paramInputStream.reset();
/*  89 */     return localAudioFileFormat;
/*     */   }
/*     */ 
/*     */   public AudioFileFormat getAudioFileFormat(URL paramURL)
/*     */     throws UnsupportedAudioFileException, IOException
/*     */   {
/* 104 */     AudioFileFormat localAudioFileFormat = null;
/* 105 */     InputStream localInputStream = paramURL.openStream();
/*     */     try {
/* 107 */       localAudioFileFormat = getCOMM(localInputStream, false);
/*     */     } finally {
/* 109 */       localInputStream.close();
/*     */     }
/* 111 */     return localAudioFileFormat;
/*     */   }
/*     */ 
/*     */   public AudioFileFormat getAudioFileFormat(File paramFile)
/*     */     throws UnsupportedAudioFileException, IOException
/*     */   {
/* 126 */     AudioFileFormat localAudioFileFormat = null;
/* 127 */     FileInputStream localFileInputStream = new FileInputStream(paramFile);
/*     */     try
/*     */     {
/* 130 */       localAudioFileFormat = getCOMM(localFileInputStream, false);
/*     */     } finally {
/* 132 */       localFileInputStream.close();
/*     */     }
/*     */ 
/* 135 */     return localAudioFileFormat;
/*     */   }
/*     */ 
/*     */   public AudioInputStream getAudioInputStream(InputStream paramInputStream)
/*     */     throws UnsupportedAudioFileException, IOException
/*     */   {
/* 162 */     AudioFileFormat localAudioFileFormat = getCOMM(paramInputStream, true);
/*     */ 
/* 166 */     return new AudioInputStream(paramInputStream, localAudioFileFormat.getFormat(), localAudioFileFormat.getFrameLength());
/*     */   }
/*     */ 
/*     */   public AudioInputStream getAudioInputStream(URL paramURL)
/*     */     throws UnsupportedAudioFileException, IOException
/*     */   {
/* 182 */     InputStream localInputStream = paramURL.openStream();
/* 183 */     AudioFileFormat localAudioFileFormat = null;
/*     */     try {
/* 185 */       localAudioFileFormat = getCOMM(localInputStream, false);
/*     */     } finally {
/* 187 */       if (localAudioFileFormat == null) {
/* 188 */         localInputStream.close();
/*     */       }
/*     */     }
/* 191 */     return new AudioInputStream(localInputStream, localAudioFileFormat.getFormat(), localAudioFileFormat.getFrameLength());
/*     */   }
/*     */ 
/*     */   public AudioInputStream getAudioInputStream(File paramFile)
/*     */     throws UnsupportedAudioFileException, IOException
/*     */   {
/* 209 */     FileInputStream localFileInputStream = new FileInputStream(paramFile);
/* 210 */     AudioFileFormat localAudioFileFormat = null;
/*     */     try
/*     */     {
/* 213 */       localAudioFileFormat = getCOMM(localFileInputStream, false);
/*     */     } finally {
/* 215 */       if (localAudioFileFormat == null) {
/* 216 */         localFileInputStream.close();
/*     */       }
/*     */     }
/* 219 */     return new AudioInputStream(localFileInputStream, localAudioFileFormat.getFormat(), localAudioFileFormat.getFrameLength());
/*     */   }
/*     */ 
/*     */   private AudioFileFormat getCOMM(InputStream paramInputStream, boolean paramBoolean)
/*     */     throws UnsupportedAudioFileException, IOException
/*     */   {
/* 227 */     DataInputStream localDataInputStream = new DataInputStream(paramInputStream);
/*     */ 
/* 229 */     if (paramBoolean) {
/* 230 */       localDataInputStream.mark(8);
/*     */     }
/*     */ 
/* 236 */     int i = 0;
/* 237 */     int j = 0;
/* 238 */     AudioFormat localAudioFormat = null;
/*     */ 
/* 241 */     int k = localDataInputStream.readInt();
/*     */ 
/* 244 */     if (k != 1179603533)
/*     */     {
/* 246 */       if (paramBoolean) {
/* 247 */         localDataInputStream.reset();
/*     */       }
/* 249 */       throw new UnsupportedAudioFileException("not an AIFF file");
/*     */     }
/*     */ 
/* 252 */     int m = localDataInputStream.readInt();
/* 253 */     int n = localDataInputStream.readInt();
/* 254 */     i += 12;
/*     */     int i1;
/* 257 */     if (m <= 0) {
/* 258 */       m = -1;
/* 259 */       i1 = -1;
/*     */     } else {
/* 261 */       i1 = m + 8;
/*     */     }
/*     */ 
/* 265 */     int i2 = 0;
/*     */ 
/* 267 */     if (n == 1095321155) {
/* 268 */       i2 = 1;
/*     */     }
/*     */ 
/* 273 */     int i3 = 0;
/* 274 */     while (i3 == 0)
/*     */     {
/* 276 */       int i4 = localDataInputStream.readInt();
/* 277 */       int i5 = localDataInputStream.readInt();
/* 278 */       i += 8;
/*     */ 
/* 280 */       int i6 = 0;
/*     */       int i7;
/* 283 */       switch (i4)
/*     */       {
/*     */       case 1180058962:
/* 286 */         break;
/*     */       case 1129270605:
/* 291 */         if (((i2 == 0) && (i5 < 18)) || ((i2 != 0) && (i5 < 22))) {
/* 292 */           throw new UnsupportedAudioFileException("Invalid AIFF/COMM chunksize");
/*     */         }
/*     */ 
/* 295 */         i7 = localDataInputStream.readShort();
/* 296 */         localDataInputStream.readInt();
/* 297 */         int i8 = localDataInputStream.readShort();
/* 298 */         float f = (float)read_ieee_extended(localDataInputStream);
/* 299 */         i6 += 18;
/*     */ 
/* 303 */         AudioFormat.Encoding localEncoding = AudioFormat.Encoding.PCM_SIGNED;
/*     */ 
/* 305 */         if (i2 != 0) {
/* 306 */           i9 = localDataInputStream.readInt(); i6 += 4;
/* 307 */           switch (i9) {
/*     */           case 1313820229:
/* 309 */             localEncoding = AudioFormat.Encoding.PCM_SIGNED;
/* 310 */             break;
/*     */           case 1970037111:
/* 312 */             localEncoding = AudioFormat.Encoding.ULAW;
/* 313 */             i8 = 8;
/* 314 */             break;
/*     */           default:
/* 316 */             throw new UnsupportedAudioFileException("Invalid AIFF encoding");
/*     */           }
/*     */         }
/* 319 */         int i9 = calculatePCMFrameSize(i8, i7);
/*     */ 
/* 324 */         localAudioFormat = new AudioFormat(localEncoding, f, i8, i7, i9, f, true);
/*     */ 
/* 327 */         break;
/*     */       case 1397968452:
/* 332 */         int i10 = localDataInputStream.readInt();
/* 333 */         int i11 = localDataInputStream.readInt();
/* 334 */         i6 += 8;
/*     */ 
/* 347 */         if (i5 < m) {
/* 348 */           j = i5 - i6;
/*     */         }
/*     */         else {
/* 351 */           j = m - (i + i6);
/*     */         }
/* 353 */         i3 = 1;
/*     */       }
/*     */ 
/* 356 */       i += i6;
/*     */ 
/* 358 */       if (i3 == 0) {
/* 359 */         i7 = i5 - i6;
/* 360 */         if (i7 > 0) {
/* 361 */           i += localDataInputStream.skipBytes(i7);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 366 */     if (localAudioFormat == null) {
/* 367 */       throw new UnsupportedAudioFileException("missing COMM chunk");
/*     */     }
/* 369 */     AudioFileFormat.Type localType = i2 != 0 ? AudioFileFormat.Type.AIFC : AudioFileFormat.Type.AIFF;
/*     */ 
/* 371 */     return new AiffFileFormat(localType, i1, localAudioFormat, j / localAudioFormat.getFrameSize());
/*     */   }
/*     */ 
/*     */   private void write_ieee_extended(DataOutputStream paramDataOutputStream, double paramDouble)
/*     */     throws IOException
/*     */   {
/* 384 */     int i = 16398;
/* 385 */     double d = paramDouble;
/*     */ 
/* 389 */     while (d < 44000.0D) {
/* 390 */       d *= 2.0D;
/* 391 */       i--;
/*     */     }
/* 393 */     paramDataOutputStream.writeShort(i);
/* 394 */     paramDataOutputStream.writeInt((int)d << 16);
/* 395 */     paramDataOutputStream.writeInt(0);
/*     */   }
/*     */ 
/*     */   private double read_ieee_extended(DataInputStream paramDataInputStream)
/*     */     throws IOException
/*     */   {
/* 408 */     double d1 = 0.0D;
/* 409 */     int i = 0;
/* 410 */     long l1 = 0L; long l2 = 0L;
/*     */ 
/* 412 */     double d2 = 3.402823466385289E+038D;
/*     */ 
/* 415 */     i = paramDataInputStream.readUnsignedShort();
/*     */ 
/* 417 */     long l3 = paramDataInputStream.readUnsignedShort();
/* 418 */     long l4 = paramDataInputStream.readUnsignedShort();
/* 419 */     l1 = l3 << 16 | l4;
/*     */ 
/* 421 */     l3 = paramDataInputStream.readUnsignedShort();
/* 422 */     l4 = paramDataInputStream.readUnsignedShort();
/* 423 */     l2 = l3 << 16 | l4;
/*     */ 
/* 425 */     if ((i == 0) && (l1 == 0L) && (l2 == 0L)) {
/* 426 */       d1 = 0.0D;
/*     */     }
/* 428 */     else if (i == 32767) {
/* 429 */       d1 = d2;
/*     */     } else {
/* 431 */       i -= 16383;
/* 432 */       i -= 31;
/* 433 */       d1 = l1 * Math.pow(2.0D, i);
/* 434 */       i -= 32;
/* 435 */       d1 += l2 * Math.pow(2.0D, i);
/*     */     }
/*     */ 
/* 439 */     return d1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.AiffFileReader
 * JD-Core Version:    0.6.2
 */