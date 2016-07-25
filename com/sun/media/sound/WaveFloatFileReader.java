/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.BufferedInputStream;
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
/*     */ import javax.sound.sampled.spi.AudioFileReader;
/*     */ 
/*     */ public final class WaveFloatFileReader extends AudioFileReader
/*     */ {
/*     */   public AudioFileFormat getAudioFileFormat(InputStream paramInputStream)
/*     */     throws UnsupportedAudioFileException, IOException
/*     */   {
/*  52 */     paramInputStream.mark(200);
/*     */     AudioFileFormat localAudioFileFormat;
/*     */     try
/*     */     {
/*  55 */       localAudioFileFormat = internal_getAudioFileFormat(paramInputStream);
/*     */     } finally {
/*  57 */       paramInputStream.reset();
/*     */     }
/*  59 */     return localAudioFileFormat;
/*     */   }
/*     */ 
/*     */   private AudioFileFormat internal_getAudioFileFormat(InputStream paramInputStream)
/*     */     throws UnsupportedAudioFileException, IOException
/*     */   {
/*  65 */     RIFFReader localRIFFReader = new RIFFReader(paramInputStream);
/*  66 */     if (!localRIFFReader.getFormat().equals("RIFF"))
/*  67 */       throw new UnsupportedAudioFileException();
/*  68 */     if (!localRIFFReader.getType().equals("WAVE")) {
/*  69 */       throw new UnsupportedAudioFileException();
/*     */     }
/*  71 */     int i = 0;
/*  72 */     int j = 0;
/*     */ 
/*  74 */     int k = 1;
/*  75 */     long l = 1L;
/*  76 */     int m = 1;
/*  77 */     int n = 1;
/*     */ 
/*  79 */     while (localRIFFReader.hasNextChunk()) {
/*  80 */       localObject = localRIFFReader.nextChunk();
/*     */ 
/*  82 */       if (((RIFFReader)localObject).getFormat().equals("fmt ")) {
/*  83 */         i = 1;
/*     */ 
/*  85 */         int i1 = ((RIFFReader)localObject).readUnsignedShort();
/*  86 */         if (i1 != 3)
/*  87 */           throw new UnsupportedAudioFileException();
/*  88 */         k = ((RIFFReader)localObject).readUnsignedShort();
/*  89 */         l = ((RIFFReader)localObject).readUnsignedInt();
/*  90 */         ((RIFFReader)localObject).readUnsignedInt();
/*  91 */         m = ((RIFFReader)localObject).readUnsignedShort();
/*  92 */         n = ((RIFFReader)localObject).readUnsignedShort();
/*     */       }
/*  94 */       if (((RIFFReader)localObject).getFormat().equals("data")) {
/*  95 */         j = 1;
/*  96 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 100 */     if (i == 0)
/* 101 */       throw new UnsupportedAudioFileException();
/* 102 */     if (j == 0) {
/* 103 */       throw new UnsupportedAudioFileException();
/*     */     }
/* 105 */     Object localObject = new AudioFormat(AudioFormat.Encoding.PCM_FLOAT, (float)l, n, k, m, (float)l, false);
/*     */ 
/* 108 */     AudioFileFormat localAudioFileFormat = new AudioFileFormat(AudioFileFormat.Type.WAVE, (AudioFormat)localObject, -1);
/*     */ 
/* 111 */     return localAudioFileFormat;
/*     */   }
/*     */ 
/*     */   public AudioInputStream getAudioInputStream(InputStream paramInputStream)
/*     */     throws UnsupportedAudioFileException, IOException
/*     */   {
/* 117 */     AudioFileFormat localAudioFileFormat = getAudioFileFormat(paramInputStream);
/* 118 */     RIFFReader localRIFFReader1 = new RIFFReader(paramInputStream);
/* 119 */     if (!localRIFFReader1.getFormat().equals("RIFF"))
/* 120 */       throw new UnsupportedAudioFileException();
/* 121 */     if (!localRIFFReader1.getType().equals("WAVE"))
/* 122 */       throw new UnsupportedAudioFileException();
/* 123 */     while (localRIFFReader1.hasNextChunk()) {
/* 124 */       RIFFReader localRIFFReader2 = localRIFFReader1.nextChunk();
/* 125 */       if (localRIFFReader2.getFormat().equals("data")) {
/* 126 */         return new AudioInputStream(localRIFFReader2, localAudioFileFormat.getFormat(), localRIFFReader2.getSize());
/*     */       }
/*     */     }
/*     */ 
/* 130 */     throw new UnsupportedAudioFileException();
/*     */   }
/*     */ 
/*     */   public AudioFileFormat getAudioFileFormat(URL paramURL) throws UnsupportedAudioFileException, IOException {
/* 135 */     InputStream localInputStream = paramURL.openStream();
/*     */     AudioFileFormat localAudioFileFormat;
/*     */     try {
/* 138 */       localAudioFileFormat = getAudioFileFormat(new BufferedInputStream(localInputStream));
/*     */     } finally {
/* 140 */       localInputStream.close();
/*     */     }
/* 142 */     return localAudioFileFormat;
/*     */   }
/*     */ 
/*     */   public AudioFileFormat getAudioFileFormat(File paramFile) throws UnsupportedAudioFileException, IOException {
/* 147 */     FileInputStream localFileInputStream = new FileInputStream(paramFile);
/*     */     AudioFileFormat localAudioFileFormat;
/*     */     try {
/* 150 */       localAudioFileFormat = getAudioFileFormat(new BufferedInputStream(localFileInputStream));
/*     */     } finally {
/* 152 */       localFileInputStream.close();
/*     */     }
/* 154 */     return localAudioFileFormat;
/*     */   }
/*     */ 
/*     */   public AudioInputStream getAudioInputStream(URL paramURL) throws UnsupportedAudioFileException, IOException
/*     */   {
/* 159 */     return getAudioInputStream(new BufferedInputStream(paramURL.openStream()));
/*     */   }
/*     */ 
/*     */   public AudioInputStream getAudioInputStream(File paramFile) throws UnsupportedAudioFileException, IOException
/*     */   {
/* 164 */     return getAudioInputStream(new BufferedInputStream(new FileInputStream(paramFile)));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.WaveFloatFileReader
 * JD-Core Version:    0.6.2
 */