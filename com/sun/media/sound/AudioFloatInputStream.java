/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ import javax.sound.sampled.AudioFormat.Encoding;
/*     */ import javax.sound.sampled.AudioInputStream;
/*     */ import javax.sound.sampled.AudioSystem;
/*     */ import javax.sound.sampled.UnsupportedAudioFileException;
/*     */ 
/*     */ public abstract class AudioFloatInputStream
/*     */ {
/*     */   public static AudioFloatInputStream getInputStream(URL paramURL)
/*     */     throws UnsupportedAudioFileException, IOException
/*     */   {
/* 216 */     return new DirectAudioFloatInputStream(AudioSystem.getAudioInputStream(paramURL));
/*     */   }
/*     */ 
/*     */   public static AudioFloatInputStream getInputStream(File paramFile)
/*     */     throws UnsupportedAudioFileException, IOException
/*     */   {
/* 222 */     return new DirectAudioFloatInputStream(AudioSystem.getAudioInputStream(paramFile));
/*     */   }
/*     */ 
/*     */   public static AudioFloatInputStream getInputStream(InputStream paramInputStream)
/*     */     throws UnsupportedAudioFileException, IOException
/*     */   {
/* 228 */     return new DirectAudioFloatInputStream(AudioSystem.getAudioInputStream(paramInputStream));
/*     */   }
/*     */ 
/*     */   public static AudioFloatInputStream getInputStream(AudioInputStream paramAudioInputStream)
/*     */   {
/* 234 */     return new DirectAudioFloatInputStream(paramAudioInputStream);
/*     */   }
/*     */ 
/*     */   public static AudioFloatInputStream getInputStream(AudioFormat paramAudioFormat, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 239 */     AudioFloatConverter localAudioFloatConverter = AudioFloatConverter.getConverter(paramAudioFormat);
/*     */ 
/* 241 */     if (localAudioFloatConverter != null) {
/* 242 */       return new BytaArrayAudioFloatInputStream(localAudioFloatConverter, paramArrayOfByte, paramInt1, paramInt2);
/*     */     }
/*     */ 
/* 245 */     ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte, paramInt1, paramInt2);
/* 246 */     long l = paramAudioFormat.getFrameSize() == -1 ? -1L : paramInt2 / paramAudioFormat.getFrameSize();
/*     */ 
/* 248 */     AudioInputStream localAudioInputStream = new AudioInputStream(localByteArrayInputStream, paramAudioFormat, l);
/* 249 */     return getInputStream(localAudioInputStream);
/*     */   }
/*     */ 
/*     */   public abstract AudioFormat getFormat();
/*     */ 
/*     */   public abstract long getFrameLength();
/*     */ 
/*     */   public abstract int read(float[] paramArrayOfFloat, int paramInt1, int paramInt2) throws IOException;
/*     */ 
/*     */   public final int read(float[] paramArrayOfFloat) throws IOException {
/* 259 */     return read(paramArrayOfFloat, 0, paramArrayOfFloat.length);
/*     */   }
/*     */ 
/*     */   public final float read() throws IOException {
/* 263 */     float[] arrayOfFloat = new float[1];
/* 264 */     int i = read(arrayOfFloat, 0, 1);
/* 265 */     if ((i == -1) || (i == 0))
/* 266 */       return 0.0F;
/* 267 */     return arrayOfFloat[0];
/*     */   }
/*     */ 
/*     */   public abstract long skip(long paramLong)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract int available()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract void close()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract void mark(int paramInt);
/*     */ 
/*     */   public abstract boolean markSupported();
/*     */ 
/*     */   public abstract void reset()
/*     */     throws IOException;
/*     */ 
/*     */   private static class BytaArrayAudioFloatInputStream extends AudioFloatInputStream
/*     */   {
/*  49 */     private int pos = 0;
/*  50 */     private int markpos = 0;
/*     */     private final AudioFloatConverter converter;
/*     */     private final AudioFormat format;
/*     */     private final byte[] buffer;
/*     */     private final int buffer_offset;
/*     */     private final int buffer_len;
/*     */     private final int framesize_pc;
/*     */ 
/*     */     BytaArrayAudioFloatInputStream(AudioFloatConverter paramAudioFloatConverter, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     {
/*  60 */       this.converter = paramAudioFloatConverter;
/*  61 */       this.format = paramAudioFloatConverter.getFormat();
/*  62 */       this.buffer = paramArrayOfByte;
/*  63 */       this.buffer_offset = paramInt1;
/*  64 */       this.framesize_pc = (this.format.getFrameSize() / this.format.getChannels());
/*  65 */       this.buffer_len = (paramInt2 / this.framesize_pc);
/*     */     }
/*     */ 
/*     */     public AudioFormat getFormat()
/*     */     {
/*  70 */       return this.format;
/*     */     }
/*     */ 
/*     */     public long getFrameLength() {
/*  74 */       return this.buffer_len;
/*     */     }
/*     */ 
/*     */     public int read(float[] paramArrayOfFloat, int paramInt1, int paramInt2) throws IOException {
/*  78 */       if (paramArrayOfFloat == null)
/*  79 */         throw new NullPointerException();
/*  80 */       if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt2 > paramArrayOfFloat.length - paramInt1))
/*  81 */         throw new IndexOutOfBoundsException();
/*  82 */       if (this.pos >= this.buffer_len)
/*  83 */         return -1;
/*  84 */       if (paramInt2 == 0)
/*  85 */         return 0;
/*  86 */       if (this.pos + paramInt2 > this.buffer_len)
/*  87 */         paramInt2 = this.buffer_len - this.pos;
/*  88 */       this.converter.toFloatArray(this.buffer, this.buffer_offset + this.pos * this.framesize_pc, paramArrayOfFloat, paramInt1, paramInt2);
/*     */ 
/*  90 */       this.pos += paramInt2;
/*  91 */       return paramInt2;
/*     */     }
/*     */ 
/*     */     public long skip(long paramLong) throws IOException {
/*  95 */       if (this.pos >= this.buffer_len)
/*  96 */         return -1L;
/*  97 */       if (paramLong <= 0L)
/*  98 */         return 0L;
/*  99 */       if (this.pos + paramLong > this.buffer_len)
/* 100 */         paramLong = this.buffer_len - this.pos;
/* 101 */       this.pos = ((int)(this.pos + paramLong));
/* 102 */       return paramLong;
/*     */     }
/*     */ 
/*     */     public int available() throws IOException {
/* 106 */       return this.buffer_len - this.pos;
/*     */     }
/*     */ 
/*     */     public void close() throws IOException {
/*     */     }
/*     */ 
/*     */     public void mark(int paramInt) {
/* 113 */       this.markpos = this.pos;
/*     */     }
/*     */ 
/*     */     public boolean markSupported() {
/* 117 */       return true;
/*     */     }
/*     */ 
/*     */     public void reset() throws IOException {
/* 121 */       this.pos = this.markpos;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class DirectAudioFloatInputStream extends AudioFloatInputStream {
/*     */     private final AudioInputStream stream;
/*     */     private AudioFloatConverter converter;
/*     */     private final int framesize_pc;
/*     */     private byte[] buffer;
/*     */ 
/* 134 */     DirectAudioFloatInputStream(AudioInputStream paramAudioInputStream) { this.converter = AudioFloatConverter.getConverter(paramAudioInputStream.getFormat());
/* 135 */       if (this.converter == null) {
/* 136 */         AudioFormat localAudioFormat1 = paramAudioInputStream.getFormat();
/*     */ 
/* 139 */         AudioFormat[] arrayOfAudioFormat = AudioSystem.getTargetFormats(AudioFormat.Encoding.PCM_SIGNED, localAudioFormat1);
/*     */         AudioFormat localAudioFormat2;
/* 141 */         if (arrayOfAudioFormat.length != 0) {
/* 142 */           localAudioFormat2 = arrayOfAudioFormat[0];
/*     */         } else {
/* 144 */           float f1 = localAudioFormat1.getSampleRate();
/* 145 */           int i = localAudioFormat1.getSampleSizeInBits();
/* 146 */           int j = localAudioFormat1.getFrameSize();
/* 147 */           float f2 = localAudioFormat1.getFrameRate();
/* 148 */           i = 16;
/* 149 */           j = localAudioFormat1.getChannels() * (i / 8);
/* 150 */           f2 = f1;
/*     */ 
/* 152 */           localAudioFormat2 = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, f1, i, localAudioFormat1.getChannels(), j, f2, false);
/*     */         }
/*     */ 
/* 158 */         paramAudioInputStream = AudioSystem.getAudioInputStream(localAudioFormat2, paramAudioInputStream);
/* 159 */         this.converter = AudioFloatConverter.getConverter(paramAudioInputStream.getFormat());
/*     */       }
/* 161 */       this.framesize_pc = (paramAudioInputStream.getFormat().getFrameSize() / paramAudioInputStream.getFormat().getChannels());
/*     */ 
/* 163 */       this.stream = paramAudioInputStream; }
/*     */ 
/*     */     public AudioFormat getFormat()
/*     */     {
/* 167 */       return this.stream.getFormat();
/*     */     }
/*     */ 
/*     */     public long getFrameLength() {
/* 171 */       return this.stream.getFrameLength();
/*     */     }
/*     */ 
/*     */     public int read(float[] paramArrayOfFloat, int paramInt1, int paramInt2) throws IOException {
/* 175 */       int i = paramInt2 * this.framesize_pc;
/* 176 */       if ((this.buffer == null) || (this.buffer.length < i))
/* 177 */         this.buffer = new byte[i];
/* 178 */       int j = this.stream.read(this.buffer, 0, i);
/* 179 */       if (j == -1)
/* 180 */         return -1;
/* 181 */       this.converter.toFloatArray(this.buffer, paramArrayOfFloat, paramInt1, j / this.framesize_pc);
/* 182 */       return j / this.framesize_pc;
/*     */     }
/*     */ 
/*     */     public long skip(long paramLong) throws IOException {
/* 186 */       long l1 = paramLong * this.framesize_pc;
/* 187 */       long l2 = this.stream.skip(l1);
/* 188 */       if (l2 == -1L)
/* 189 */         return -1L;
/* 190 */       return l2 / this.framesize_pc;
/*     */     }
/*     */ 
/*     */     public int available() throws IOException {
/* 194 */       return this.stream.available() / this.framesize_pc;
/*     */     }
/*     */ 
/*     */     public void close() throws IOException {
/* 198 */       this.stream.close();
/*     */     }
/*     */ 
/*     */     public void mark(int paramInt) {
/* 202 */       this.stream.mark(paramInt * this.framesize_pc);
/*     */     }
/*     */ 
/*     */     public boolean markSupported() {
/* 206 */       return this.stream.markSupported();
/*     */     }
/*     */ 
/*     */     public void reset() throws IOException {
/* 210 */       this.stream.reset();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.AudioFloatInputStream
 * JD-Core Version:    0.6.2
 */