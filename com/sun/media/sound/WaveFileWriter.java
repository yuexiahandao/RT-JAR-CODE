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
/*     */ public final class WaveFileWriter extends SunFileWriter
/*     */ {
/*     */   static final int RIFF_MAGIC = 1380533830;
/*     */   static final int WAVE_MAGIC = 1463899717;
/*     */   static final int FMT_MAGIC = 1718449184;
/*     */   static final int DATA_MAGIC = 1684108385;
/*     */   static final int WAVE_FORMAT_UNKNOWN = 0;
/*     */   static final int WAVE_FORMAT_PCM = 1;
/*     */   static final int WAVE_FORMAT_ADPCM = 2;
/*     */   static final int WAVE_FORMAT_ALAW = 6;
/*     */   static final int WAVE_FORMAT_MULAW = 7;
/*     */   static final int WAVE_FORMAT_OKI_ADPCM = 16;
/*     */   static final int WAVE_FORMAT_DIGISTD = 21;
/*     */   static final int WAVE_FORMAT_DIGIFIX = 22;
/*     */   static final int WAVE_IBM_FORMAT_MULAW = 257;
/*     */   static final int WAVE_IBM_FORMAT_ALAW = 258;
/*     */   static final int WAVE_IBM_FORMAT_ADPCM = 259;
/*     */   static final int WAVE_FORMAT_DVI_ADPCM = 17;
/*     */   static final int WAVE_FORMAT_SX7383 = 7175;
/*     */ 
/*     */   public WaveFileWriter()
/*     */   {
/*  80 */     super(new AudioFileFormat.Type[] { AudioFileFormat.Type.WAVE });
/*     */   }
/*     */ 
/*     */   public AudioFileFormat.Type[] getAudioFileTypes(AudioInputStream paramAudioInputStream)
/*     */   {
/*  89 */     AudioFileFormat.Type[] arrayOfType = new AudioFileFormat.Type[this.types.length];
/*  90 */     System.arraycopy(this.types, 0, arrayOfType, 0, this.types.length);
/*     */ 
/*  93 */     AudioFormat localAudioFormat = paramAudioInputStream.getFormat();
/*  94 */     AudioFormat.Encoding localEncoding = localAudioFormat.getEncoding();
/*     */ 
/*  96 */     if ((AudioFormat.Encoding.ALAW.equals(localEncoding)) || (AudioFormat.Encoding.ULAW.equals(localEncoding)) || (AudioFormat.Encoding.PCM_SIGNED.equals(localEncoding)) || (AudioFormat.Encoding.PCM_UNSIGNED.equals(localEncoding)))
/*     */     {
/* 101 */       return arrayOfType;
/*     */     }
/*     */ 
/* 104 */     return new AudioFileFormat.Type[0];
/*     */   }
/*     */ 
/*     */   public int write(AudioInputStream paramAudioInputStream, AudioFileFormat.Type paramType, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 115 */     WaveFileFormat localWaveFileFormat = (WaveFileFormat)getAudioFileFormat(paramType, paramAudioInputStream);
/*     */ 
/* 120 */     if (paramAudioInputStream.getFrameLength() == -1L) {
/* 121 */       throw new IOException("stream length not specified");
/*     */     }
/*     */ 
/* 124 */     int i = writeWaveFile(paramAudioInputStream, localWaveFileFormat, paramOutputStream);
/* 125 */     return i;
/*     */   }
/*     */ 
/*     */   public int write(AudioInputStream paramAudioInputStream, AudioFileFormat.Type paramType, File paramFile)
/*     */     throws IOException
/*     */   {
/* 132 */     WaveFileFormat localWaveFileFormat = (WaveFileFormat)getAudioFileFormat(paramType, paramAudioInputStream);
/*     */ 
/* 135 */     FileOutputStream localFileOutputStream = new FileOutputStream(paramFile);
/* 136 */     BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(localFileOutputStream, 4096);
/* 137 */     int i = writeWaveFile(paramAudioInputStream, localWaveFileFormat, localBufferedOutputStream);
/* 138 */     localBufferedOutputStream.close();
/*     */ 
/* 143 */     if (localWaveFileFormat.getByteLength() == -1)
/*     */     {
/* 145 */       int j = i - localWaveFileFormat.getHeaderSize();
/* 146 */       int k = j + localWaveFileFormat.getHeaderSize() - 8;
/*     */ 
/* 148 */       RandomAccessFile localRandomAccessFile = new RandomAccessFile(paramFile, "rw");
/*     */ 
/* 150 */       localRandomAccessFile.skipBytes(4);
/* 151 */       localRandomAccessFile.writeInt(big2little(k));
/*     */ 
/* 153 */       localRandomAccessFile.skipBytes(12 + WaveFileFormat.getFmtChunkSize(localWaveFileFormat.getWaveType()) + 4);
/* 154 */       localRandomAccessFile.writeInt(big2little(j));
/*     */ 
/* 156 */       localRandomAccessFile.close();
/*     */     }
/*     */ 
/* 159 */     return i;
/*     */   }
/*     */ 
/*     */   private AudioFileFormat getAudioFileFormat(AudioFileFormat.Type paramType, AudioInputStream paramAudioInputStream)
/*     */   {
/* 169 */     AudioFormat localAudioFormat1 = null;
/* 170 */     WaveFileFormat localWaveFileFormat = null;
/* 171 */     Object localObject = AudioFormat.Encoding.PCM_SIGNED;
/*     */ 
/* 173 */     AudioFormat localAudioFormat2 = paramAudioInputStream.getFormat();
/* 174 */     AudioFormat.Encoding localEncoding = localAudioFormat2.getEncoding();
/*     */ 
/* 183 */     if (!this.types[0].equals(paramType)) {
/* 184 */       throw new IllegalArgumentException("File type " + paramType + " not supported.");
/*     */     }
/* 186 */     int k = 1;
/*     */     int i;
/* 188 */     if ((AudioFormat.Encoding.ALAW.equals(localEncoding)) || (AudioFormat.Encoding.ULAW.equals(localEncoding)))
/*     */     {
/* 191 */       localObject = localEncoding;
/* 192 */       i = localAudioFormat2.getSampleSizeInBits();
/* 193 */       if (localEncoding.equals(AudioFormat.Encoding.ALAW))
/* 194 */         k = 6;
/*     */       else
/* 196 */         k = 7;
/*     */     }
/* 198 */     else if (localAudioFormat2.getSampleSizeInBits() == 8) {
/* 199 */       localObject = AudioFormat.Encoding.PCM_UNSIGNED;
/* 200 */       i = 8;
/*     */     } else {
/* 202 */       localObject = AudioFormat.Encoding.PCM_SIGNED;
/* 203 */       i = localAudioFormat2.getSampleSizeInBits();
/*     */     }
/*     */ 
/* 207 */     localAudioFormat1 = new AudioFormat((AudioFormat.Encoding)localObject, localAudioFormat2.getSampleRate(), i, localAudioFormat2.getChannels(), localAudioFormat2.getFrameSize(), localAudioFormat2.getFrameRate(), false);
/*     */     int j;
/* 215 */     if (paramAudioInputStream.getFrameLength() != -1L) {
/* 216 */       j = (int)paramAudioInputStream.getFrameLength() * localAudioFormat2.getFrameSize() + WaveFileFormat.getHeaderSize(k);
/*     */     }
/*     */     else {
/* 219 */       j = -1;
/*     */     }
/*     */ 
/* 222 */     localWaveFileFormat = new WaveFileFormat(AudioFileFormat.Type.WAVE, j, localAudioFormat1, (int)paramAudioInputStream.getFrameLength());
/*     */ 
/* 227 */     return localWaveFileFormat;
/*     */   }
/*     */ 
/*     */   private int writeWaveFile(InputStream paramInputStream, WaveFileFormat paramWaveFileFormat, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 233 */     int i = 0;
/* 234 */     int j = 0;
/* 235 */     InputStream localInputStream = getFileStream(paramWaveFileFormat, paramInputStream);
/* 236 */     byte[] arrayOfByte = new byte[4096];
/* 237 */     int k = paramWaveFileFormat.getByteLength();
/*     */ 
/* 239 */     while ((i = localInputStream.read(arrayOfByte)) >= 0)
/*     */     {
/* 241 */       if (k > 0) {
/* 242 */         if (i < k) {
/* 243 */           paramOutputStream.write(arrayOfByte, 0, i);
/* 244 */           j += i;
/* 245 */           k -= i;
/*     */         } else {
/* 247 */           paramOutputStream.write(arrayOfByte, 0, k);
/* 248 */           j += k;
/* 249 */           k = 0;
/* 250 */           break;
/*     */         }
/*     */       } else {
/* 253 */         paramOutputStream.write(arrayOfByte, 0, i);
/* 254 */         j += i;
/*     */       }
/*     */     }
/*     */ 
/* 258 */     return j;
/*     */   }
/*     */ 
/*     */   private InputStream getFileStream(WaveFileFormat paramWaveFileFormat, InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/* 265 */     AudioFormat localAudioFormat1 = paramWaveFileFormat.getFormat();
/* 266 */     int i = paramWaveFileFormat.getHeaderSize();
/* 267 */     int j = 1380533830;
/* 268 */     int k = 1463899717;
/* 269 */     int m = 1718449184;
/* 270 */     int n = WaveFileFormat.getFmtChunkSize(paramWaveFileFormat.getWaveType());
/* 271 */     short s1 = (short)paramWaveFileFormat.getWaveType();
/* 272 */     int i1 = (short)localAudioFormat1.getChannels();
/* 273 */     int i2 = (short)localAudioFormat1.getSampleSizeInBits();
/* 274 */     int i3 = (int)localAudioFormat1.getSampleRate();
/* 275 */     int i4 = localAudioFormat1.getFrameSize();
/* 276 */     int i5 = (int)localAudioFormat1.getFrameRate();
/* 277 */     int i6 = i1 * i2 * i3 / 8;
/* 278 */     short s2 = (short)(i2 / 8 * i1);
/* 279 */     int i7 = 1684108385;
/* 280 */     int i8 = paramWaveFileFormat.getFrameLength() * i4;
/* 281 */     int i9 = paramWaveFileFormat.getByteLength();
/* 282 */     int i10 = i8 + i - 8;
/*     */ 
/* 284 */     byte[] arrayOfByte = null;
/* 285 */     ByteArrayInputStream localByteArrayInputStream = null;
/* 286 */     ByteArrayOutputStream localByteArrayOutputStream = null;
/* 287 */     DataOutputStream localDataOutputStream = null;
/* 288 */     SequenceInputStream localSequenceInputStream = null;
/*     */ 
/* 290 */     AudioFormat localAudioFormat2 = null;
/* 291 */     AudioFormat.Encoding localEncoding = null;
/* 292 */     Object localObject = paramInputStream;
/*     */ 
/* 295 */     if ((paramInputStream instanceof AudioInputStream)) {
/* 296 */       localAudioFormat2 = ((AudioInputStream)paramInputStream).getFormat();
/*     */ 
/* 298 */       localEncoding = localAudioFormat2.getEncoding();
/*     */ 
/* 300 */       if ((AudioFormat.Encoding.PCM_SIGNED.equals(localEncoding)) && 
/* 301 */         (i2 == 8)) {
/* 302 */         s1 = 1;
/*     */ 
/* 304 */         localObject = AudioSystem.getAudioInputStream(new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, localAudioFormat2.getSampleRate(), localAudioFormat2.getSampleSizeInBits(), localAudioFormat2.getChannels(), localAudioFormat2.getFrameSize(), localAudioFormat2.getFrameRate(), false), (AudioInputStream)paramInputStream);
/*     */       }
/*     */ 
/* 315 */       if (((AudioFormat.Encoding.PCM_SIGNED.equals(localEncoding)) && (localAudioFormat2.isBigEndian())) || ((AudioFormat.Encoding.PCM_UNSIGNED.equals(localEncoding)) && (!localAudioFormat2.isBigEndian())) || ((AudioFormat.Encoding.PCM_UNSIGNED.equals(localEncoding)) && (localAudioFormat2.isBigEndian())))
/*     */       {
/* 318 */         if (i2 != 8) {
/* 319 */           s1 = 1;
/*     */ 
/* 321 */           localObject = AudioSystem.getAudioInputStream(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, localAudioFormat2.getSampleRate(), localAudioFormat2.getSampleSizeInBits(), localAudioFormat2.getChannels(), localAudioFormat2.getFrameSize(), localAudioFormat2.getFrameRate(), false), (AudioInputStream)paramInputStream);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 337 */     localByteArrayOutputStream = new ByteArrayOutputStream();
/* 338 */     localDataOutputStream = new DataOutputStream(localByteArrayOutputStream);
/*     */ 
/* 341 */     localDataOutputStream.writeInt(j);
/* 342 */     localDataOutputStream.writeInt(big2little(i10));
/* 343 */     localDataOutputStream.writeInt(k);
/* 344 */     localDataOutputStream.writeInt(m);
/* 345 */     localDataOutputStream.writeInt(big2little(n));
/* 346 */     localDataOutputStream.writeShort(big2littleShort(s1));
/* 347 */     localDataOutputStream.writeShort(big2littleShort(i1));
/* 348 */     localDataOutputStream.writeInt(big2little(i3));
/* 349 */     localDataOutputStream.writeInt(big2little(i6));
/* 350 */     localDataOutputStream.writeShort(big2littleShort(s2));
/* 351 */     localDataOutputStream.writeShort(big2littleShort(i2));
/*     */ 
/* 353 */     if (s1 != 1)
/*     */     {
/* 355 */       localDataOutputStream.writeShort(0);
/*     */     }
/*     */ 
/* 358 */     localDataOutputStream.writeInt(i7);
/* 359 */     localDataOutputStream.writeInt(big2little(i8));
/*     */ 
/* 361 */     localDataOutputStream.close();
/* 362 */     arrayOfByte = localByteArrayOutputStream.toByteArray();
/* 363 */     localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
/* 364 */     localSequenceInputStream = new SequenceInputStream(localByteArrayInputStream, new SunFileWriter.NoCloseInputStream(this, (InputStream)localObject));
/*     */ 
/* 367 */     return localSequenceInputStream;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.WaveFileWriter
 * JD-Core Version:    0.6.2
 */