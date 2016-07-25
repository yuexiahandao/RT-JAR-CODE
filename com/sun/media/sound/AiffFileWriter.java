/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.io.SequenceInputStream;
/*     */ import javax.sound.sampled.AudioFileFormat;
/*     */ import javax.sound.sampled.AudioFileFormat.Type;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ import javax.sound.sampled.AudioFormat.Encoding;
/*     */ import javax.sound.sampled.AudioInputStream;
/*     */ import javax.sound.sampled.AudioSystem;
/*     */ 
/*     */ public final class AiffFileWriter extends SunFileWriter
/*     */ {
/*     */   private static final int DOUBLE_MANTISSA_LENGTH = 52;
/*     */   private static final int DOUBLE_EXPONENT_LENGTH = 11;
/*     */   private static final long DOUBLE_SIGN_MASK = -9223372036854775808L;
/*     */   private static final long DOUBLE_EXPONENT_MASK = 9218868437227405312L;
/*     */   private static final long DOUBLE_MANTISSA_MASK = 4503599627370495L;
/*     */   private static final int DOUBLE_EXPONENT_OFFSET = 1023;
/*     */   private static final int EXTENDED_EXPONENT_OFFSET = 16383;
/*     */   private static final int EXTENDED_MANTISSA_LENGTH = 63;
/*     */   private static final int EXTENDED_EXPONENT_LENGTH = 15;
/*     */   private static final long EXTENDED_INTEGER_MASK = -9223372036854775808L;
/*     */ 
/*     */   public AiffFileWriter()
/*     */   {
/*  59 */     super(new AudioFileFormat.Type[] { AudioFileFormat.Type.AIFF });
/*     */   }
/*     */ 
/*     */   public AudioFileFormat.Type[] getAudioFileTypes(AudioInputStream paramAudioInputStream)
/*     */   {
/*  67 */     AudioFileFormat.Type[] arrayOfType = new AudioFileFormat.Type[this.types.length];
/*  68 */     System.arraycopy(this.types, 0, arrayOfType, 0, this.types.length);
/*     */ 
/*  71 */     AudioFormat localAudioFormat = paramAudioInputStream.getFormat();
/*  72 */     AudioFormat.Encoding localEncoding = localAudioFormat.getEncoding();
/*     */ 
/*  74 */     if ((AudioFormat.Encoding.ALAW.equals(localEncoding)) || (AudioFormat.Encoding.ULAW.equals(localEncoding)) || (AudioFormat.Encoding.PCM_SIGNED.equals(localEncoding)) || (AudioFormat.Encoding.PCM_UNSIGNED.equals(localEncoding)))
/*     */     {
/*  79 */       return arrayOfType;
/*     */     }
/*     */ 
/*  82 */     return new AudioFileFormat.Type[0];
/*     */   }
/*     */ 
/*     */   public int write(AudioInputStream paramAudioInputStream, AudioFileFormat.Type paramType, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/*  93 */     AiffFileFormat localAiffFileFormat = (AiffFileFormat)getAudioFileFormat(paramType, paramAudioInputStream);
/*     */ 
/*  96 */     if (paramAudioInputStream.getFrameLength() == -1L) {
/*  97 */       throw new IOException("stream length not specified");
/*     */     }
/*     */ 
/* 100 */     int i = writeAiffFile(paramAudioInputStream, localAiffFileFormat, paramOutputStream);
/* 101 */     return i;
/*     */   }
/*     */ 
/*     */   public int write(AudioInputStream paramAudioInputStream, AudioFileFormat.Type paramType, File paramFile)
/*     */     throws IOException
/*     */   {
/* 108 */     AiffFileFormat localAiffFileFormat = (AiffFileFormat)getAudioFileFormat(paramType, paramAudioInputStream);
/*     */ 
/* 111 */     FileOutputStream localFileOutputStream = new FileOutputStream(paramFile);
/* 112 */     BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(localFileOutputStream, 4096);
/* 113 */     int i = writeAiffFile(paramAudioInputStream, localAiffFileFormat, localBufferedOutputStream);
/* 114 */     localBufferedOutputStream.close();
/*     */ 
/* 119 */     if (localAiffFileFormat.getByteLength() == -1)
/*     */     {
/* 123 */       int j = localAiffFileFormat.getFormat().getChannels() * localAiffFileFormat.getFormat().getSampleSizeInBits();
/*     */ 
/* 125 */       int k = i;
/* 126 */       int m = k - localAiffFileFormat.getHeaderSize() + 16;
/* 127 */       long l = m - 16;
/* 128 */       int n = (int)(l * 8L / j);
/*     */ 
/* 130 */       RandomAccessFile localRandomAccessFile = new RandomAccessFile(paramFile, "rw");
/*     */ 
/* 132 */       localRandomAccessFile.skipBytes(4);
/* 133 */       localRandomAccessFile.writeInt(k - 8);
/*     */ 
/* 135 */       localRandomAccessFile.skipBytes(4 + localAiffFileFormat.getFverChunkSize() + 4 + 4 + 2);
/*     */ 
/* 137 */       localRandomAccessFile.writeInt(n);
/*     */ 
/* 139 */       localRandomAccessFile.skipBytes(16);
/* 140 */       localRandomAccessFile.writeInt(m - 8);
/*     */ 
/* 142 */       localRandomAccessFile.close();
/*     */     }
/*     */ 
/* 145 */     return i;
/*     */   }
/*     */ 
/*     */   private AudioFileFormat getAudioFileFormat(AudioFileFormat.Type paramType, AudioInputStream paramAudioInputStream)
/*     */   {
/* 157 */     AudioFormat localAudioFormat1 = null;
/* 158 */     AiffFileFormat localAiffFileFormat = null;
/* 159 */     AudioFormat.Encoding localEncoding1 = AudioFormat.Encoding.PCM_SIGNED;
/*     */ 
/* 161 */     AudioFormat localAudioFormat2 = paramAudioInputStream.getFormat();
/* 162 */     AudioFormat.Encoding localEncoding2 = localAudioFormat2.getEncoding();
/*     */ 
/* 171 */     int k = 0;
/*     */ 
/* 173 */     if (!this.types[0].equals(paramType))
/* 174 */       throw new IllegalArgumentException("File type " + paramType + " not supported.");
/*     */     int i;
/* 177 */     if ((AudioFormat.Encoding.ALAW.equals(localEncoding2)) || (AudioFormat.Encoding.ULAW.equals(localEncoding2)))
/*     */     {
/* 180 */       if (localAudioFormat2.getSampleSizeInBits() == 8)
/*     */       {
/* 182 */         localEncoding1 = AudioFormat.Encoding.PCM_SIGNED;
/* 183 */         i = 16;
/* 184 */         k = 1;
/*     */       }
/*     */       else
/*     */       {
/* 189 */         throw new IllegalArgumentException("Encoding " + localEncoding2 + " supported only for 8-bit data.");
/*     */       }
/* 191 */     } else if (localAudioFormat2.getSampleSizeInBits() == 8)
/*     */     {
/* 193 */       localEncoding1 = AudioFormat.Encoding.PCM_UNSIGNED;
/* 194 */       i = 8;
/*     */     }
/*     */     else
/*     */     {
/* 198 */       localEncoding1 = AudioFormat.Encoding.PCM_SIGNED;
/* 199 */       i = localAudioFormat2.getSampleSizeInBits();
/*     */     }
/*     */ 
/* 203 */     localAudioFormat1 = new AudioFormat(localEncoding1, localAudioFormat2.getSampleRate(), i, localAudioFormat2.getChannels(), localAudioFormat2.getFrameSize(), localAudioFormat2.getFrameRate(), true);
/*     */     int j;
/* 212 */     if (paramAudioInputStream.getFrameLength() != -1L) {
/* 213 */       if (k != 0)
/* 214 */         j = (int)paramAudioInputStream.getFrameLength() * localAudioFormat2.getFrameSize() * 2 + 54;
/*     */       else
/* 216 */         j = (int)paramAudioInputStream.getFrameLength() * localAudioFormat2.getFrameSize() + 54;
/*     */     }
/*     */     else {
/* 219 */       j = -1;
/*     */     }
/*     */ 
/* 222 */     localAiffFileFormat = new AiffFileFormat(AudioFileFormat.Type.AIFF, j, localAudioFormat1, (int)paramAudioInputStream.getFrameLength());
/*     */ 
/* 227 */     return localAiffFileFormat;
/*     */   }
/*     */ 
/*     */   private int writeAiffFile(InputStream paramInputStream, AiffFileFormat paramAiffFileFormat, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 233 */     int i = 0;
/* 234 */     int j = 0;
/* 235 */     InputStream localInputStream = getFileStream(paramAiffFileFormat, paramInputStream);
/* 236 */     byte[] arrayOfByte = new byte[4096];
/* 237 */     int k = paramAiffFileFormat.getByteLength();
/*     */ 
/* 239 */     while ((i = localInputStream.read(arrayOfByte)) >= 0) {
/* 240 */       if (k > 0) {
/* 241 */         if (i < k) {
/* 242 */           paramOutputStream.write(arrayOfByte, 0, i);
/* 243 */           j += i;
/* 244 */           k -= i;
/*     */         } else {
/* 246 */           paramOutputStream.write(arrayOfByte, 0, k);
/* 247 */           j += k;
/* 248 */           k = 0;
/* 249 */           break;
/*     */         }
/*     */       }
/*     */       else {
/* 253 */         paramOutputStream.write(arrayOfByte, 0, i);
/* 254 */         j += i;
/*     */       }
/*     */     }
/*     */ 
/* 258 */     return j;
/*     */   }
/*     */ 
/*     */   private InputStream getFileStream(AiffFileFormat paramAiffFileFormat, InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/* 265 */     AudioFormat localAudioFormat1 = paramAiffFileFormat.getFormat();
/* 266 */     AudioFormat localAudioFormat2 = null;
/* 267 */     AudioFormat.Encoding localEncoding = null;
/*     */ 
/* 272 */     int i = paramAiffFileFormat.getHeaderSize();
/*     */ 
/* 275 */     int j = paramAiffFileFormat.getFverChunkSize();
/*     */ 
/* 277 */     int k = paramAiffFileFormat.getCommChunkSize();
/* 278 */     int m = -1;
/* 279 */     int n = -1;
/*     */ 
/* 281 */     int i1 = paramAiffFileFormat.getSsndChunkOffset();
/* 282 */     int i2 = (short)localAudioFormat1.getChannels();
/* 283 */     int i3 = (short)localAudioFormat1.getSampleSizeInBits();
/* 284 */     int i4 = i2 * i3;
/* 285 */     int i5 = paramAiffFileFormat.getFrameLength();
/* 286 */     long l = -1L;
/* 287 */     if (i5 != -1) {
/* 288 */       l = i5 * i4 / 8L;
/* 289 */       n = (int)l + 16;
/* 290 */       m = (int)l + i;
/*     */     }
/* 292 */     float f = localAudioFormat1.getSampleRate();
/* 293 */     int i6 = 1313820229;
/*     */ 
/* 295 */     byte[] arrayOfByte = null;
/* 296 */     ByteArrayInputStream localByteArrayInputStream = null;
/* 297 */     ByteArrayOutputStream localByteArrayOutputStream = null;
/* 298 */     DataOutputStream localDataOutputStream = null;
/* 299 */     SequenceInputStream localSequenceInputStream = null;
/* 300 */     Object localObject = paramInputStream;
/*     */ 
/* 304 */     if ((paramInputStream instanceof AudioInputStream))
/*     */     {
/* 306 */       localAudioFormat2 = ((AudioInputStream)paramInputStream).getFormat();
/* 307 */       localEncoding = localAudioFormat2.getEncoding();
/*     */ 
/* 311 */       if ((AudioFormat.Encoding.PCM_UNSIGNED.equals(localEncoding)) || ((AudioFormat.Encoding.PCM_SIGNED.equals(localEncoding)) && (!localAudioFormat2.isBigEndian())))
/*     */       {
/* 315 */         localObject = AudioSystem.getAudioInputStream(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, localAudioFormat2.getSampleRate(), localAudioFormat2.getSampleSizeInBits(), localAudioFormat2.getChannels(), localAudioFormat2.getFrameSize(), localAudioFormat2.getFrameRate(), true), (AudioInputStream)paramInputStream);
/*     */       }
/* 325 */       else if ((AudioFormat.Encoding.ULAW.equals(localEncoding)) || (AudioFormat.Encoding.ALAW.equals(localEncoding)))
/*     */       {
/* 328 */         if (localAudioFormat2.getSampleSizeInBits() != 8) {
/* 329 */           throw new IllegalArgumentException("unsupported encoding");
/*     */         }
/*     */ 
/* 336 */         localObject = AudioSystem.getAudioInputStream(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, localAudioFormat2.getSampleRate(), localAudioFormat2.getSampleSizeInBits() * 2, localAudioFormat2.getChannels(), localAudioFormat2.getFrameSize() * 2, localAudioFormat2.getFrameRate(), true), (AudioInputStream)paramInputStream);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 350 */     localByteArrayOutputStream = new ByteArrayOutputStream();
/* 351 */     localDataOutputStream = new DataOutputStream(localByteArrayOutputStream);
/*     */ 
/* 354 */     localDataOutputStream.writeInt(1179603533);
/* 355 */     localDataOutputStream.writeInt(m - 8);
/* 356 */     localDataOutputStream.writeInt(1095321158);
/*     */ 
/* 364 */     localDataOutputStream.writeInt(1129270605);
/* 365 */     localDataOutputStream.writeInt(k - 8);
/* 366 */     localDataOutputStream.writeShort(i2);
/* 367 */     localDataOutputStream.writeInt(i5);
/* 368 */     localDataOutputStream.writeShort(i3);
/* 369 */     write_ieee_extended(localDataOutputStream, f);
/*     */ 
/* 377 */     localDataOutputStream.writeInt(1397968452);
/* 378 */     localDataOutputStream.writeInt(n - 8);
/*     */ 
/* 382 */     localDataOutputStream.writeInt(0);
/* 383 */     localDataOutputStream.writeInt(0);
/*     */ 
/* 387 */     localDataOutputStream.close();
/* 388 */     arrayOfByte = localByteArrayOutputStream.toByteArray();
/* 389 */     localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
/*     */ 
/* 391 */     localSequenceInputStream = new SequenceInputStream(localByteArrayInputStream, new SunFileWriter.NoCloseInputStream(this, (InputStream)localObject));
/*     */ 
/* 394 */     return localSequenceInputStream;
/*     */   }
/*     */ 
/*     */   private void write_ieee_extended(DataOutputStream paramDataOutputStream, float paramFloat)
/*     */     throws IOException
/*     */   {
/* 428 */     long l1 = Double.doubleToLongBits(paramFloat);
/*     */ 
/* 430 */     long l2 = (l1 & 0x0) >> 63;
/*     */ 
/* 432 */     long l3 = (l1 & 0x0) >> 52;
/*     */ 
/* 434 */     long l4 = l1 & 0xFFFFFFFF;
/*     */ 
/* 436 */     long l5 = l3 - 1023L + 16383L;
/*     */ 
/* 438 */     long l6 = l4 << 11;
/*     */ 
/* 440 */     long l7 = l2 << 15;
/* 441 */     int i = (short)(int)(l7 | l5);
/* 442 */     long l8 = 0x0 | l6;
/*     */ 
/* 444 */     paramDataOutputStream.writeShort(i);
/* 445 */     paramDataOutputStream.writeLong(l8);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.AiffFileWriter
 * JD-Core Version:    0.6.2
 */