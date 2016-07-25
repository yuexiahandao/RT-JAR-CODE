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
/*     */ public final class AuFileWriter extends SunFileWriter
/*     */ {
/*     */   public static final int UNKNOWN_SIZE = -1;
/*     */ 
/*     */   public AuFileWriter()
/*     */   {
/*  61 */     super(new AudioFileFormat.Type[] { AudioFileFormat.Type.AU });
/*     */   }
/*     */ 
/*     */   public AudioFileFormat.Type[] getAudioFileTypes(AudioInputStream paramAudioInputStream)
/*     */   {
/*  66 */     AudioFileFormat.Type[] arrayOfType = new AudioFileFormat.Type[this.types.length];
/*  67 */     System.arraycopy(this.types, 0, arrayOfType, 0, this.types.length);
/*     */ 
/*  70 */     AudioFormat localAudioFormat = paramAudioInputStream.getFormat();
/*  71 */     AudioFormat.Encoding localEncoding = localAudioFormat.getEncoding();
/*     */ 
/*  73 */     if ((AudioFormat.Encoding.ALAW.equals(localEncoding)) || (AudioFormat.Encoding.ULAW.equals(localEncoding)) || (AudioFormat.Encoding.PCM_SIGNED.equals(localEncoding)) || (AudioFormat.Encoding.PCM_UNSIGNED.equals(localEncoding)))
/*     */     {
/*  78 */       return arrayOfType;
/*     */     }
/*     */ 
/*  81 */     return new AudioFileFormat.Type[0];
/*     */   }
/*     */ 
/*     */   public int write(AudioInputStream paramAudioInputStream, AudioFileFormat.Type paramType, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/*  94 */     AuFileFormat localAuFileFormat = (AuFileFormat)getAudioFileFormat(paramType, paramAudioInputStream);
/*     */ 
/*  96 */     int i = writeAuFile(paramAudioInputStream, localAuFileFormat, paramOutputStream);
/*  97 */     return i;
/*     */   }
/*     */ 
/*     */   public int write(AudioInputStream paramAudioInputStream, AudioFileFormat.Type paramType, File paramFile)
/*     */     throws IOException
/*     */   {
/* 105 */     AuFileFormat localAuFileFormat = (AuFileFormat)getAudioFileFormat(paramType, paramAudioInputStream);
/*     */ 
/* 108 */     FileOutputStream localFileOutputStream = new FileOutputStream(paramFile);
/* 109 */     BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(localFileOutputStream, 4096);
/* 110 */     int i = writeAuFile(paramAudioInputStream, localAuFileFormat, localBufferedOutputStream);
/* 111 */     localBufferedOutputStream.close();
/*     */ 
/* 116 */     if (localAuFileFormat.getByteLength() == -1)
/*     */     {
/* 120 */       RandomAccessFile localRandomAccessFile = new RandomAccessFile(paramFile, "rw");
/* 121 */       if (localRandomAccessFile.length() <= 2147483647L)
/*     */       {
/* 123 */         localRandomAccessFile.skipBytes(8);
/* 124 */         localRandomAccessFile.writeInt(i - 24);
/*     */       }
/*     */ 
/* 127 */       localRandomAccessFile.close();
/*     */     }
/*     */ 
/* 130 */     return i;
/*     */   }
/*     */ 
/*     */   private AudioFileFormat getAudioFileFormat(AudioFileFormat.Type paramType, AudioInputStream paramAudioInputStream)
/*     */   {
/* 142 */     AudioFormat localAudioFormat1 = null;
/* 143 */     AuFileFormat localAuFileFormat = null;
/* 144 */     Object localObject = AudioFormat.Encoding.PCM_SIGNED;
/*     */ 
/* 146 */     AudioFormat localAudioFormat2 = paramAudioInputStream.getFormat();
/* 147 */     AudioFormat.Encoding localEncoding = localAudioFormat2.getEncoding();
/*     */ 
/* 157 */     if (!this.types[0].equals(paramType))
/* 158 */       throw new IllegalArgumentException("File type " + paramType + " not supported.");
/*     */     int i;
/* 161 */     if ((AudioFormat.Encoding.ALAW.equals(localEncoding)) || (AudioFormat.Encoding.ULAW.equals(localEncoding)))
/*     */     {
/* 164 */       localObject = localEncoding;
/* 165 */       i = localAudioFormat2.getSampleSizeInBits();
/*     */     }
/* 167 */     else if (localAudioFormat2.getSampleSizeInBits() == 8)
/*     */     {
/* 169 */       localObject = AudioFormat.Encoding.PCM_SIGNED;
/* 170 */       i = 8;
/*     */     }
/*     */     else
/*     */     {
/* 174 */       localObject = AudioFormat.Encoding.PCM_SIGNED;
/* 175 */       i = localAudioFormat2.getSampleSizeInBits();
/*     */     }
/*     */ 
/* 179 */     localAudioFormat1 = new AudioFormat((AudioFormat.Encoding)localObject, localAudioFormat2.getSampleRate(), i, localAudioFormat2.getChannels(), localAudioFormat2.getFrameSize(), localAudioFormat2.getFrameRate(), true);
/*     */     int j;
/* 188 */     if (paramAudioInputStream.getFrameLength() != -1L)
/* 189 */       j = (int)paramAudioInputStream.getFrameLength() * localAudioFormat2.getFrameSize() + 24;
/*     */     else {
/* 191 */       j = -1;
/*     */     }
/*     */ 
/* 194 */     localAuFileFormat = new AuFileFormat(AudioFileFormat.Type.AU, j, localAudioFormat1, (int)paramAudioInputStream.getFrameLength());
/*     */ 
/* 199 */     return localAuFileFormat;
/*     */   }
/*     */ 
/*     */   private InputStream getFileStream(AuFileFormat paramAuFileFormat, InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/* 207 */     AudioFormat localAudioFormat1 = paramAuFileFormat.getFormat();
/*     */ 
/* 209 */     int i = 779316836;
/* 210 */     int j = 24;
/* 211 */     long l1 = paramAuFileFormat.getFrameLength();
/*     */ 
/* 214 */     long l2 = l1 == -1L ? -1L : l1 * localAudioFormat1.getFrameSize();
/* 215 */     if (l2 > 2147483647L) {
/* 216 */       l2 = -1L;
/*     */     }
/* 218 */     int k = paramAuFileFormat.getAuType();
/* 219 */     int m = (int)localAudioFormat1.getSampleRate();
/* 220 */     int n = localAudioFormat1.getChannels();
/*     */ 
/* 223 */     boolean bool = true;
/*     */ 
/* 225 */     byte[] arrayOfByte = null;
/* 226 */     ByteArrayInputStream localByteArrayInputStream = null;
/* 227 */     ByteArrayOutputStream localByteArrayOutputStream = null;
/* 228 */     DataOutputStream localDataOutputStream = null;
/* 229 */     SequenceInputStream localSequenceInputStream = null;
/*     */ 
/* 231 */     AudioFormat localAudioFormat2 = null;
/* 232 */     AudioFormat.Encoding localEncoding = null;
/* 233 */     Object localObject = paramInputStream;
/*     */ 
/* 237 */     localObject = paramInputStream;
/*     */ 
/* 239 */     if ((paramInputStream instanceof AudioInputStream))
/*     */     {
/* 242 */       localAudioFormat2 = ((AudioInputStream)paramInputStream).getFormat();
/* 243 */       localEncoding = localAudioFormat2.getEncoding();
/*     */ 
/* 246 */       if ((AudioFormat.Encoding.PCM_UNSIGNED.equals(localEncoding)) || ((AudioFormat.Encoding.PCM_SIGNED.equals(localEncoding)) && (bool != localAudioFormat2.isBigEndian())))
/*     */       {
/* 254 */         localObject = AudioSystem.getAudioInputStream(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, localAudioFormat2.getSampleRate(), localAudioFormat2.getSampleSizeInBits(), localAudioFormat2.getChannels(), localAudioFormat2.getFrameSize(), localAudioFormat2.getFrameRate(), bool), (AudioInputStream)paramInputStream);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 268 */     localByteArrayOutputStream = new ByteArrayOutputStream();
/* 269 */     localDataOutputStream = new DataOutputStream(localByteArrayOutputStream);
/*     */ 
/* 272 */     if (bool) {
/* 273 */       localDataOutputStream.writeInt(779316836);
/* 274 */       localDataOutputStream.writeInt(j);
/* 275 */       localDataOutputStream.writeInt((int)l2);
/* 276 */       localDataOutputStream.writeInt(k);
/* 277 */       localDataOutputStream.writeInt(m);
/* 278 */       localDataOutputStream.writeInt(n);
/*     */     } else {
/* 280 */       localDataOutputStream.writeInt(1684960046);
/* 281 */       localDataOutputStream.writeInt(big2little(j));
/* 282 */       localDataOutputStream.writeInt(big2little((int)l2));
/* 283 */       localDataOutputStream.writeInt(big2little(k));
/* 284 */       localDataOutputStream.writeInt(big2little(m));
/* 285 */       localDataOutputStream.writeInt(big2little(n));
/*     */     }
/*     */ 
/* 291 */     localDataOutputStream.close();
/* 292 */     arrayOfByte = localByteArrayOutputStream.toByteArray();
/* 293 */     localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
/* 294 */     localSequenceInputStream = new SequenceInputStream(localByteArrayInputStream, new SunFileWriter.NoCloseInputStream(this, (InputStream)localObject));
/*     */ 
/* 297 */     return localSequenceInputStream;
/*     */   }
/*     */ 
/*     */   private int writeAuFile(InputStream paramInputStream, AuFileFormat paramAuFileFormat, OutputStream paramOutputStream) throws IOException
/*     */   {
/* 302 */     int i = 0;
/* 303 */     int j = 0;
/* 304 */     InputStream localInputStream = getFileStream(paramAuFileFormat, paramInputStream);
/* 305 */     byte[] arrayOfByte = new byte[4096];
/* 306 */     int k = paramAuFileFormat.getByteLength();
/*     */ 
/* 308 */     while ((i = localInputStream.read(arrayOfByte)) >= 0) {
/* 309 */       if (k > 0) {
/* 310 */         if (i < k) {
/* 311 */           paramOutputStream.write(arrayOfByte, 0, i);
/* 312 */           j += i;
/* 313 */           k -= i;
/*     */         } else {
/* 315 */           paramOutputStream.write(arrayOfByte, 0, k);
/* 316 */           j += k;
/* 317 */           k = 0;
/* 318 */           break;
/*     */         }
/*     */       } else {
/* 321 */         paramOutputStream.write(arrayOfByte, 0, i);
/* 322 */         j += i;
/*     */       }
/*     */     }
/*     */ 
/* 326 */     return j;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.AuFileWriter
 * JD-Core Version:    0.6.2
 */