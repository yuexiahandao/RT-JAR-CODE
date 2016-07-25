/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.List;
/*     */ import javax.sound.midi.InvalidMidiDataException;
/*     */ import javax.sound.midi.Soundbank;
/*     */ import javax.sound.midi.spi.SoundbankReader;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ import javax.sound.sampled.AudioInputStream;
/*     */ import javax.sound.sampled.AudioSystem;
/*     */ import javax.sound.sampled.UnsupportedAudioFileException;
/*     */ 
/*     */ public final class AudioFileSoundbankReader extends SoundbankReader
/*     */ {
/*     */   public Soundbank getSoundbank(URL paramURL)
/*     */     throws InvalidMidiDataException, IOException
/*     */   {
/*     */     try
/*     */     {
/*  51 */       AudioInputStream localAudioInputStream = AudioSystem.getAudioInputStream(paramURL);
/*  52 */       Soundbank localSoundbank = getSoundbank(localAudioInputStream);
/*  53 */       localAudioInputStream.close();
/*  54 */       return localSoundbank;
/*     */     } catch (UnsupportedAudioFileException localUnsupportedAudioFileException) {
/*  56 */       return null; } catch (IOException localIOException) {
/*     */     }
/*  58 */     return null;
/*     */   }
/*     */ 
/*     */   public Soundbank getSoundbank(InputStream paramInputStream)
/*     */     throws InvalidMidiDataException, IOException
/*     */   {
/*  64 */     paramInputStream.mark(512);
/*     */     try {
/*  66 */       AudioInputStream localAudioInputStream = AudioSystem.getAudioInputStream(paramInputStream);
/*  67 */       Soundbank localSoundbank = getSoundbank(localAudioInputStream);
/*  68 */       if (localSoundbank != null)
/*  69 */         return localSoundbank;
/*     */     } catch (UnsupportedAudioFileException localUnsupportedAudioFileException) {
/*     */     } catch (IOException localIOException) {
/*     */     }
/*  73 */     paramInputStream.reset();
/*  74 */     return null;
/*     */   }
/*     */ 
/*     */   public Soundbank getSoundbank(AudioInputStream paramAudioInputStream)
/*     */     throws InvalidMidiDataException, IOException
/*     */   {
/*     */     try
/*     */     {
/*     */       byte[] arrayOfByte;
/*  81 */       if (paramAudioInputStream.getFrameLength() == -1L) {
/*  82 */         localObject1 = new ByteArrayOutputStream();
/*  83 */         localObject2 = new byte[1024 - 1024 % paramAudioInputStream.getFormat().getFrameSize()];
/*     */         int i;
/*  86 */         while ((i = paramAudioInputStream.read((byte[])localObject2)) != -1) {
/*  87 */           ((ByteArrayOutputStream)localObject1).write((byte[])localObject2, 0, i);
/*     */         }
/*  89 */         paramAudioInputStream.close();
/*  90 */         arrayOfByte = ((ByteArrayOutputStream)localObject1).toByteArray();
/*     */       } else {
/*  92 */         arrayOfByte = new byte[(int)(paramAudioInputStream.getFrameLength() * paramAudioInputStream.getFormat().getFrameSize())];
/*     */ 
/*  94 */         new DataInputStream(paramAudioInputStream).readFully(arrayOfByte);
/*     */       }
/*  96 */       Object localObject1 = new ModelByteBufferWavetable(new ModelByteBuffer(arrayOfByte), paramAudioInputStream.getFormat(), -4800.0F);
/*     */ 
/*  98 */       Object localObject2 = new ModelPerformer();
/*  99 */       ((ModelPerformer)localObject2).getOscillators().add(localObject1);
/*     */ 
/* 101 */       SimpleSoundbank localSimpleSoundbank = new SimpleSoundbank();
/* 102 */       SimpleInstrument localSimpleInstrument = new SimpleInstrument();
/* 103 */       localSimpleInstrument.add((ModelPerformer)localObject2);
/* 104 */       localSimpleSoundbank.addInstrument(localSimpleInstrument);
/* 105 */       return localSimpleSoundbank; } catch (Exception localException) {
/*     */     }
/* 107 */     return null;
/*     */   }
/*     */ 
/*     */   public Soundbank getSoundbank(File paramFile) throws InvalidMidiDataException, IOException
/*     */   {
/*     */     try
/*     */     {
/* 114 */       AudioInputStream localAudioInputStream = AudioSystem.getAudioInputStream(paramFile);
/* 115 */       localAudioInputStream.close();
/* 116 */       ModelByteBufferWavetable localModelByteBufferWavetable = new ModelByteBufferWavetable(new ModelByteBuffer(paramFile, 0L, paramFile.length()), -4800.0F);
/*     */ 
/* 118 */       ModelPerformer localModelPerformer = new ModelPerformer();
/* 119 */       localModelPerformer.getOscillators().add(localModelByteBufferWavetable);
/* 120 */       SimpleSoundbank localSimpleSoundbank = new SimpleSoundbank();
/* 121 */       SimpleInstrument localSimpleInstrument = new SimpleInstrument();
/* 122 */       localSimpleInstrument.add(localModelPerformer);
/* 123 */       localSimpleSoundbank.addInstrument(localSimpleInstrument);
/* 124 */       return localSimpleSoundbank;
/*     */     } catch (UnsupportedAudioFileException localUnsupportedAudioFileException) {
/* 126 */       return null; } catch (IOException localIOException) {
/*     */     }
/* 128 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.AudioFileSoundbankReader
 * JD-Core Version:    0.6.2
 */