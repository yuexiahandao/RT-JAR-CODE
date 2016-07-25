/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import javax.sound.sampled.AudioFileFormat.Type;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ import javax.sound.sampled.AudioFormat.Encoding;
/*     */ import javax.sound.sampled.AudioInputStream;
/*     */ import javax.sound.sampled.AudioSystem;
/*     */ import javax.sound.sampled.spi.AudioFileWriter;
/*     */ 
/*     */ public final class WaveFloatFileWriter extends AudioFileWriter
/*     */ {
/*     */   public AudioFileFormat.Type[] getAudioFileTypes()
/*     */   {
/*  47 */     return new AudioFileFormat.Type[] { AudioFileFormat.Type.WAVE };
/*     */   }
/*     */ 
/*     */   public AudioFileFormat.Type[] getAudioFileTypes(AudioInputStream paramAudioInputStream)
/*     */   {
/*  52 */     if (!paramAudioInputStream.getFormat().getEncoding().equals(AudioFormat.Encoding.PCM_FLOAT))
/*  53 */       return new AudioFileFormat.Type[0];
/*  54 */     return new AudioFileFormat.Type[] { AudioFileFormat.Type.WAVE };
/*     */   }
/*     */ 
/*     */   private void checkFormat(AudioFileFormat.Type paramType, AudioInputStream paramAudioInputStream) {
/*  58 */     if (!AudioFileFormat.Type.WAVE.equals(paramType)) {
/*  59 */       throw new IllegalArgumentException("File type " + paramType + " not supported.");
/*     */     }
/*  61 */     if (!paramAudioInputStream.getFormat().getEncoding().equals(AudioFormat.Encoding.PCM_FLOAT))
/*  62 */       throw new IllegalArgumentException("File format " + paramAudioInputStream.getFormat() + " not supported.");
/*     */   }
/*     */ 
/*     */   public void write(AudioInputStream paramAudioInputStream, RIFFWriter paramRIFFWriter)
/*     */     throws IOException
/*     */   {
/*  69 */     RIFFWriter localRIFFWriter1 = paramRIFFWriter.writeChunk("fmt ");
/*     */ 
/*  71 */     AudioFormat localAudioFormat = paramAudioInputStream.getFormat();
/*  72 */     localRIFFWriter1.writeUnsignedShort(3);
/*  73 */     localRIFFWriter1.writeUnsignedShort(localAudioFormat.getChannels());
/*  74 */     localRIFFWriter1.writeUnsignedInt((int)localAudioFormat.getSampleRate());
/*  75 */     localRIFFWriter1.writeUnsignedInt((int)localAudioFormat.getFrameRate() * localAudioFormat.getFrameSize());
/*     */ 
/*  77 */     localRIFFWriter1.writeUnsignedShort(localAudioFormat.getFrameSize());
/*  78 */     localRIFFWriter1.writeUnsignedShort(localAudioFormat.getSampleSizeInBits());
/*  79 */     localRIFFWriter1.close();
/*  80 */     RIFFWriter localRIFFWriter2 = paramRIFFWriter.writeChunk("data");
/*  81 */     byte[] arrayOfByte = new byte[1024];
/*     */     int i;
/*  83 */     while ((i = paramAudioInputStream.read(arrayOfByte, 0, arrayOfByte.length)) != -1)
/*  84 */       localRIFFWriter2.write(arrayOfByte, 0, i);
/*  85 */     localRIFFWriter2.close();
/*     */   }
/*     */ 
/*     */   private AudioInputStream toLittleEndian(AudioInputStream paramAudioInputStream)
/*     */   {
/* 113 */     AudioFormat localAudioFormat1 = paramAudioInputStream.getFormat();
/* 114 */     AudioFormat localAudioFormat2 = new AudioFormat(localAudioFormat1.getEncoding(), localAudioFormat1.getSampleRate(), localAudioFormat1.getSampleSizeInBits(), localAudioFormat1.getChannels(), localAudioFormat1.getFrameSize(), localAudioFormat1.getFrameRate(), false);
/*     */ 
/* 118 */     return AudioSystem.getAudioInputStream(localAudioFormat2, paramAudioInputStream);
/*     */   }
/*     */ 
/*     */   public int write(AudioInputStream paramAudioInputStream, AudioFileFormat.Type paramType, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 124 */     checkFormat(paramType, paramAudioInputStream);
/* 125 */     if (paramAudioInputStream.getFormat().isBigEndian())
/* 126 */       paramAudioInputStream = toLittleEndian(paramAudioInputStream);
/* 127 */     RIFFWriter localRIFFWriter = new RIFFWriter(new NoCloseOutputStream(paramOutputStream), "WAVE");
/* 128 */     write(paramAudioInputStream, localRIFFWriter);
/* 129 */     int i = (int)localRIFFWriter.getFilePointer();
/* 130 */     localRIFFWriter.close();
/* 131 */     return i;
/*     */   }
/*     */ 
/*     */   public int write(AudioInputStream paramAudioInputStream, AudioFileFormat.Type paramType, File paramFile) throws IOException
/*     */   {
/* 136 */     checkFormat(paramType, paramAudioInputStream);
/* 137 */     if (paramAudioInputStream.getFormat().isBigEndian())
/* 138 */       paramAudioInputStream = toLittleEndian(paramAudioInputStream);
/* 139 */     RIFFWriter localRIFFWriter = new RIFFWriter(paramFile, "WAVE");
/* 140 */     write(paramAudioInputStream, localRIFFWriter);
/* 141 */     int i = (int)localRIFFWriter.getFilePointer();
/* 142 */     localRIFFWriter.close();
/* 143 */     return i;
/*     */   }
/*     */ 
/*     */   private static class NoCloseOutputStream extends OutputStream
/*     */   {
/*     */     final OutputStream out;
/*     */ 
/*     */     NoCloseOutputStream(OutputStream paramOutputStream)
/*     */     {
/*  92 */       this.out = paramOutputStream;
/*     */     }
/*     */ 
/*     */     public void write(int paramInt) throws IOException {
/*  96 */       this.out.write(paramInt);
/*     */     }
/*     */ 
/*     */     public void flush() throws IOException {
/* 100 */       this.out.flush();
/*     */     }
/*     */ 
/*     */     public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/* 104 */       this.out.write(paramArrayOfByte, paramInt1, paramInt2);
/*     */     }
/*     */ 
/*     */     public void write(byte[] paramArrayOfByte) throws IOException {
/* 108 */       this.out.write(paramArrayOfByte);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.WaveFloatFileWriter
 * JD-Core Version:    0.6.2
 */