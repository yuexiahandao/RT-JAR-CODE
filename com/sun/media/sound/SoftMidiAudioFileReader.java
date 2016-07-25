/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import javax.sound.midi.InvalidMidiDataException;
/*     */ import javax.sound.midi.MetaMessage;
/*     */ import javax.sound.midi.MidiEvent;
/*     */ import javax.sound.midi.MidiMessage;
/*     */ import javax.sound.midi.MidiSystem;
/*     */ import javax.sound.midi.MidiUnavailableException;
/*     */ import javax.sound.midi.Receiver;
/*     */ import javax.sound.midi.Sequence;
/*     */ import javax.sound.midi.Track;
/*     */ import javax.sound.sampled.AudioFileFormat;
/*     */ import javax.sound.sampled.AudioFileFormat.Type;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ import javax.sound.sampled.AudioInputStream;
/*     */ import javax.sound.sampled.UnsupportedAudioFileException;
/*     */ import javax.sound.sampled.spi.AudioFileReader;
/*     */ 
/*     */ public final class SoftMidiAudioFileReader extends AudioFileReader
/*     */ {
/*  55 */   public static final AudioFileFormat.Type MIDI = new AudioFileFormat.Type("MIDI", "mid");
/*  56 */   private static AudioFormat format = new AudioFormat(44100.0F, 16, 2, true, false);
/*     */ 
/*     */   public AudioFileFormat getAudioFileFormat(Sequence paramSequence)
/*     */     throws UnsupportedAudioFileException, IOException
/*     */   {
/*  61 */     long l1 = paramSequence.getMicrosecondLength() / 1000000L;
/*  62 */     long l2 = ()(format.getFrameRate() * (float)(l1 + 4L));
/*  63 */     return new AudioFileFormat(MIDI, format, (int)l2);
/*     */   }
/*     */ 
/*     */   public AudioInputStream getAudioInputStream(Sequence paramSequence) throws UnsupportedAudioFileException, IOException
/*     */   {
/*  68 */     SoftSynthesizer localSoftSynthesizer = new SoftSynthesizer();
/*     */     Receiver localReceiver;
/*     */     try {
/*  72 */       localAudioInputStream = localSoftSynthesizer.openStream(format, null);
/*  73 */       localReceiver = localSoftSynthesizer.getReceiver();
/*     */     } catch (MidiUnavailableException localMidiUnavailableException) {
/*  75 */       throw new IOException(localMidiUnavailableException.toString());
/*     */     }
/*  77 */     float f = paramSequence.getDivisionType();
/*  78 */     Track[] arrayOfTrack = paramSequence.getTracks();
/*  79 */     int[] arrayOfInt = new int[arrayOfTrack.length];
/*  80 */     int i = 500000;
/*  81 */     int j = paramSequence.getResolution();
/*  82 */     long l1 = 0L;
/*  83 */     long l2 = 0L;
/*     */     while (true) {
/*  85 */       Object localObject1 = null;
/*  86 */       int k = -1;
/*     */       Object localObject3;
/*  87 */       for (int m = 0; m < arrayOfTrack.length; m++) {
/*  88 */         int n = arrayOfInt[m];
/*  89 */         localObject2 = arrayOfTrack[m];
/*  90 */         if (n < ((Track)localObject2).size()) {
/*  91 */           localObject3 = ((Track)localObject2).get(n);
/*  92 */           if ((localObject1 == null) || (((MidiEvent)localObject3).getTick() < localObject1.getTick())) {
/*  93 */             localObject1 = localObject3;
/*  94 */             k = m;
/*     */           }
/*     */         }
/*     */       }
/*  98 */       if (k == -1)
/*     */         break;
/* 100 */       arrayOfInt[k] += 1;
/* 101 */       l4 = localObject1.getTick();
/* 102 */       if (f == 0.0F)
/* 103 */         l2 += (l4 - l1) * i / j;
/*     */       else
/* 105 */         l2 = ()(l4 * 1000000.0D * f / j);
/* 106 */       l1 = l4;
/* 107 */       Object localObject2 = localObject1.getMessage();
/* 108 */       if ((localObject2 instanceof MetaMessage)) {
/* 109 */         if ((f == 0.0F) && 
/* 110 */           (((MetaMessage)localObject2).getType() == 81)) {
/* 111 */           localObject3 = ((MetaMessage)localObject2).getData();
/* 112 */           i = (localObject3[0] & 0xFF) << 16 | (localObject3[1] & 0xFF) << 8 | localObject3[2] & 0xFF;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 117 */         localReceiver.send((MidiMessage)localObject2, l2);
/*     */       }
/*     */     }
/*     */ 
/* 121 */     long l3 = l2 / 1000000L;
/* 122 */     long l4 = ()(localAudioInputStream.getFormat().getFrameRate() * (float)(l3 + 4L));
/* 123 */     AudioInputStream localAudioInputStream = new AudioInputStream(localAudioInputStream, localAudioInputStream.getFormat(), l4);
/* 124 */     return localAudioInputStream;
/*     */   }
/*     */ 
/*     */   public AudioInputStream getAudioInputStream(InputStream paramInputStream) throws UnsupportedAudioFileException, IOException
/*     */   {
/* 130 */     paramInputStream.mark(200);
/*     */     Sequence localSequence;
/*     */     try {
/* 133 */       localSequence = MidiSystem.getSequence(paramInputStream);
/*     */     } catch (InvalidMidiDataException localInvalidMidiDataException) {
/* 135 */       paramInputStream.reset();
/* 136 */       throw new UnsupportedAudioFileException();
/*     */     } catch (IOException localIOException) {
/* 138 */       paramInputStream.reset();
/* 139 */       throw new UnsupportedAudioFileException();
/*     */     }
/* 141 */     return getAudioInputStream(localSequence);
/*     */   }
/*     */ 
/*     */   public AudioFileFormat getAudioFileFormat(URL paramURL) throws UnsupportedAudioFileException, IOException
/*     */   {
/*     */     Sequence localSequence;
/*     */     try {
/* 148 */       localSequence = MidiSystem.getSequence(paramURL);
/*     */     } catch (InvalidMidiDataException localInvalidMidiDataException) {
/* 150 */       throw new UnsupportedAudioFileException();
/*     */     } catch (IOException localIOException) {
/* 152 */       throw new UnsupportedAudioFileException();
/*     */     }
/* 154 */     return getAudioFileFormat(localSequence);
/*     */   }
/*     */ 
/*     */   public AudioFileFormat getAudioFileFormat(File paramFile) throws UnsupportedAudioFileException, IOException
/*     */   {
/*     */     Sequence localSequence;
/*     */     try {
/* 161 */       localSequence = MidiSystem.getSequence(paramFile);
/*     */     } catch (InvalidMidiDataException localInvalidMidiDataException) {
/* 163 */       throw new UnsupportedAudioFileException();
/*     */     } catch (IOException localIOException) {
/* 165 */       throw new UnsupportedAudioFileException();
/*     */     }
/* 167 */     return getAudioFileFormat(localSequence);
/*     */   }
/*     */ 
/*     */   public AudioInputStream getAudioInputStream(URL paramURL) throws UnsupportedAudioFileException, IOException
/*     */   {
/*     */     Sequence localSequence;
/*     */     try {
/* 174 */       localSequence = MidiSystem.getSequence(paramURL);
/*     */     } catch (InvalidMidiDataException localInvalidMidiDataException) {
/* 176 */       throw new UnsupportedAudioFileException();
/*     */     } catch (IOException localIOException) {
/* 178 */       throw new UnsupportedAudioFileException();
/*     */     }
/* 180 */     return getAudioInputStream(localSequence);
/*     */   }
/*     */ 
/*     */   public AudioInputStream getAudioInputStream(File paramFile) throws UnsupportedAudioFileException, IOException
/*     */   {
/* 185 */     if (!paramFile.getName().toLowerCase().endsWith(".mid"))
/* 186 */       throw new UnsupportedAudioFileException();
/*     */     Sequence localSequence;
/*     */     try {
/* 189 */       localSequence = MidiSystem.getSequence(paramFile);
/*     */     } catch (InvalidMidiDataException localInvalidMidiDataException) {
/* 191 */       throw new UnsupportedAudioFileException();
/*     */     } catch (IOException localIOException) {
/* 193 */       throw new UnsupportedAudioFileException();
/*     */     }
/* 195 */     return getAudioInputStream(localSequence);
/*     */   }
/*     */ 
/*     */   public AudioFileFormat getAudioFileFormat(InputStream paramInputStream) throws UnsupportedAudioFileException, IOException
/*     */   {
/* 201 */     paramInputStream.mark(200);
/*     */     Sequence localSequence;
/*     */     try {
/* 204 */       localSequence = MidiSystem.getSequence(paramInputStream);
/*     */     } catch (InvalidMidiDataException localInvalidMidiDataException) {
/* 206 */       paramInputStream.reset();
/* 207 */       throw new UnsupportedAudioFileException();
/*     */     } catch (IOException localIOException) {
/* 209 */       paramInputStream.reset();
/* 210 */       throw new UnsupportedAudioFileException();
/*     */     }
/* 212 */     return getAudioFileFormat(localSequence);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SoftMidiAudioFileReader
 * JD-Core Version:    0.6.2
 */