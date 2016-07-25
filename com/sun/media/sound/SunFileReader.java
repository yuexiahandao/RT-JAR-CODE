/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import javax.sound.sampled.AudioFileFormat;
/*     */ import javax.sound.sampled.AudioInputStream;
/*     */ import javax.sound.sampled.UnsupportedAudioFileException;
/*     */ import javax.sound.sampled.spi.AudioFileReader;
/*     */ 
/*     */ abstract class SunFileReader extends AudioFileReader
/*     */ {
/*     */   protected static final int bisBufferSize = 4096;
/*     */ 
/*     */   public abstract AudioFileFormat getAudioFileFormat(InputStream paramInputStream)
/*     */     throws UnsupportedAudioFileException, IOException;
/*     */ 
/*     */   public abstract AudioFileFormat getAudioFileFormat(URL paramURL)
/*     */     throws UnsupportedAudioFileException, IOException;
/*     */ 
/*     */   public abstract AudioFileFormat getAudioFileFormat(File paramFile)
/*     */     throws UnsupportedAudioFileException, IOException;
/*     */ 
/*     */   public abstract AudioInputStream getAudioInputStream(InputStream paramInputStream)
/*     */     throws UnsupportedAudioFileException, IOException;
/*     */ 
/*     */   public abstract AudioInputStream getAudioInputStream(URL paramURL)
/*     */     throws UnsupportedAudioFileException, IOException;
/*     */ 
/*     */   public abstract AudioInputStream getAudioInputStream(File paramFile)
/*     */     throws UnsupportedAudioFileException, IOException;
/*     */ 
/*     */   final int rllong(DataInputStream paramDataInputStream)
/*     */     throws IOException
/*     */   {
/* 172 */     int n = 0;
/*     */ 
/* 174 */     n = paramDataInputStream.readInt();
/*     */ 
/* 176 */     int i = (n & 0xFF) << 24;
/* 177 */     int j = (n & 0xFF00) << 8;
/* 178 */     int k = (n & 0xFF0000) >> 8;
/* 179 */     int m = (n & 0xFF000000) >>> 24;
/*     */ 
/* 181 */     n = i | j | k | m;
/*     */ 
/* 183 */     return n;
/*     */   }
/*     */ 
/*     */   final int big2little(int paramInt)
/*     */   {
/* 196 */     int i = (paramInt & 0xFF) << 24;
/* 197 */     int j = (paramInt & 0xFF00) << 8;
/* 198 */     int k = (paramInt & 0xFF0000) >> 8;
/* 199 */     int m = (paramInt & 0xFF000000) >>> 24;
/*     */ 
/* 201 */     paramInt = i | j | k | m;
/*     */ 
/* 203 */     return paramInt;
/*     */   }
/*     */ 
/*     */   final short rlshort(DataInputStream paramDataInputStream)
/*     */     throws IOException
/*     */   {
/* 215 */     int i = 0;
/*     */ 
/* 218 */     i = paramDataInputStream.readShort();
/*     */ 
/* 220 */     int j = (short)((i & 0xFF) << 8);
/* 221 */     int k = (short)((i & 0xFF00) >>> 8);
/*     */ 
/* 223 */     i = (short)(j | k);
/*     */ 
/* 225 */     return i;
/*     */   }
/*     */ 
/*     */   final short big2littleShort(short paramShort)
/*     */   {
/* 238 */     int i = (short)((paramShort & 0xFF) << 8);
/* 239 */     int j = (short)((paramShort & 0xFF00) >>> 8);
/*     */ 
/* 241 */     paramShort = (short)(i | j);
/*     */ 
/* 243 */     return paramShort;
/*     */   }
/*     */ 
/*     */   static final int calculatePCMFrameSize(int paramInt1, int paramInt2)
/*     */   {
/* 254 */     return (paramInt1 + 7) / 8 * paramInt2;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SunFileReader
 * JD-Core Version:    0.6.2
 */