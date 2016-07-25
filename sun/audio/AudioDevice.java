/*     */ package sun.audio;
/*     */ 
/*     */ import com.sun.media.sound.DataPusher;
/*     */ import com.sun.media.sound.Toolkit;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ import javax.sound.midi.InvalidMidiDataException;
/*     */ import javax.sound.midi.MetaEventListener;
/*     */ import javax.sound.midi.MetaMessage;
/*     */ import javax.sound.midi.MidiFileFormat;
/*     */ import javax.sound.midi.MidiSystem;
/*     */ import javax.sound.midi.MidiUnavailableException;
/*     */ import javax.sound.midi.Sequencer;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ import javax.sound.sampled.AudioFormat.Encoding;
/*     */ import javax.sound.sampled.AudioInputStream;
/*     */ import javax.sound.sampled.AudioSystem;
/*     */ import javax.sound.sampled.DataLine.Info;
/*     */ import javax.sound.sampled.LineUnavailableException;
/*     */ import javax.sound.sampled.Mixer;
/*     */ import javax.sound.sampled.SourceDataLine;
/*     */ import javax.sound.sampled.UnsupportedAudioFileException;
/*     */ 
/*     */ public final class AudioDevice
/*     */ {
/*  59 */   private boolean DEBUG = false;
/*     */   private Hashtable clipStreams;
/*     */   private Vector infos;
/*  67 */   private boolean playing = false;
/*     */ 
/*  70 */   private Mixer mixer = null;
/*     */ 
/*  78 */   public static final AudioDevice device = new AudioDevice();
/*     */ 
/*     */   private AudioDevice()
/*     */   {
/*  85 */     this.clipStreams = new Hashtable();
/*  86 */     this.infos = new Vector();
/*     */   }
/*     */ 
/*     */   private synchronized void startSampled(AudioInputStream paramAudioInputStream, InputStream paramInputStream)
/*     */     throws UnsupportedAudioFileException, LineUnavailableException
/*     */   {
/*  94 */     Info localInfo = null;
/*  95 */     DataPusher localDataPusher = null;
/*  96 */     DataLine.Info localInfo1 = null;
/*  97 */     SourceDataLine localSourceDataLine = null;
/*     */ 
/* 100 */     paramAudioInputStream = Toolkit.getPCMConvertedAudioInputStream(paramAudioInputStream);
/*     */ 
/* 102 */     if (paramAudioInputStream == null)
/*     */     {
/* 104 */       return;
/*     */     }
/*     */ 
/* 107 */     localInfo1 = new DataLine.Info(SourceDataLine.class, paramAudioInputStream.getFormat());
/*     */ 
/* 109 */     if (!AudioSystem.isLineSupported(localInfo1)) {
/* 110 */       return;
/*     */     }
/* 112 */     localSourceDataLine = (SourceDataLine)AudioSystem.getLine(localInfo1);
/* 113 */     localDataPusher = new DataPusher(localSourceDataLine, paramAudioInputStream);
/*     */ 
/* 115 */     localInfo = new Info(null, paramInputStream, localDataPusher);
/* 116 */     this.infos.addElement(localInfo);
/*     */ 
/* 118 */     localDataPusher.start();
/*     */   }
/*     */ 
/*     */   private synchronized void startMidi(InputStream paramInputStream1, InputStream paramInputStream2)
/*     */     throws InvalidMidiDataException, MidiUnavailableException
/*     */   {
/* 125 */     Sequencer localSequencer = null;
/* 126 */     Info localInfo = null;
/*     */ 
/* 128 */     localSequencer = MidiSystem.getSequencer();
/* 129 */     localSequencer.open();
/*     */     try {
/* 131 */       localSequencer.setSequence(paramInputStream1);
/*     */     } catch (IOException localIOException) {
/* 133 */       throw new InvalidMidiDataException(localIOException.getMessage());
/*     */     }
/*     */ 
/* 136 */     localInfo = new Info(localSequencer, paramInputStream2, null);
/*     */ 
/* 138 */     this.infos.addElement(localInfo);
/*     */ 
/* 141 */     localSequencer.addMetaEventListener(localInfo);
/*     */ 
/* 143 */     localSequencer.start();
/*     */   }
/*     */ 
/*     */   public synchronized void openChannel(InputStream paramInputStream)
/*     */   {
/* 155 */     if (this.DEBUG) {
/* 156 */       System.out.println("AudioDevice: openChannel");
/* 157 */       System.out.println("input stream =" + paramInputStream);
/*     */     }
/*     */ 
/* 160 */     Info localInfo = null;
/*     */ 
/* 163 */     for (int i = 0; i < this.infos.size(); i++) {
/* 164 */       localInfo = (Info)this.infos.elementAt(i);
/* 165 */       if (localInfo.in == paramInputStream)
/*     */       {
/* 167 */         return;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 172 */     AudioInputStream localAudioInputStream1 = null;
/*     */ 
/* 174 */     if ((paramInputStream instanceof AudioStream))
/*     */     {
/* 176 */       if (((AudioStream)paramInputStream).midiformat != null)
/*     */       {
/*     */         try
/*     */         {
/* 180 */           startMidi(((AudioStream)paramInputStream).stream, paramInputStream);
/*     */         } catch (Exception localException1) {
/* 182 */           return;
/*     */         }
/*     */ 
/*     */       }
/* 186 */       else if (((AudioStream)paramInputStream).ais != null)
/*     */       {
/*     */         try
/*     */         {
/* 190 */           startSampled(((AudioStream)paramInputStream).ais, paramInputStream);
/*     */         } catch (Exception localException2) {
/* 192 */           return;
/*     */         }
/*     */       }
/*     */     }
/* 196 */     else if ((paramInputStream instanceof AudioDataStream)) {
/* 197 */       if ((paramInputStream instanceof ContinuousAudioDataStream))
/*     */         try {
/* 199 */           AudioInputStream localAudioInputStream2 = new AudioInputStream(paramInputStream, ((AudioDataStream)paramInputStream).getAudioData().format, -1L);
/*     */ 
/* 202 */           startSampled(localAudioInputStream2, paramInputStream);
/*     */         } catch (Exception localException3) {
/* 204 */           return;
/*     */         }
/*     */       else
/*     */         try
/*     */         {
/* 209 */           AudioInputStream localAudioInputStream3 = new AudioInputStream(paramInputStream, ((AudioDataStream)paramInputStream).getAudioData().format, ((AudioDataStream)paramInputStream).getAudioData().buffer.length);
/*     */ 
/* 212 */           startSampled(localAudioInputStream3, paramInputStream);
/*     */         } catch (Exception localException4) {
/* 214 */           return;
/*     */         }
/*     */     }
/*     */     else {
/* 218 */       BufferedInputStream localBufferedInputStream = new BufferedInputStream(paramInputStream, 1024);
/*     */       try
/*     */       {
/*     */         try
/*     */         {
/* 223 */           localAudioInputStream1 = AudioSystem.getAudioInputStream(localBufferedInputStream);
/*     */         } catch (IOException localIOException1) {
/* 225 */           return;
/*     */         }
/*     */ 
/* 228 */         startSampled(localAudioInputStream1, paramInputStream);
/*     */       }
/*     */       catch (UnsupportedAudioFileException localUnsupportedAudioFileException1)
/*     */       {
/*     */         try {
/*     */           try {
/* 234 */             MidiFileFormat localMidiFileFormat = MidiSystem.getMidiFileFormat(localBufferedInputStream);
/*     */           }
/*     */           catch (IOException localIOException2) {
/* 237 */             return;
/*     */           }
/*     */ 
/* 240 */           startMidi(localBufferedInputStream, paramInputStream);
/*     */         }
/*     */         catch (InvalidMidiDataException localInvalidMidiDataException)
/*     */         {
/* 249 */           AudioFormat localAudioFormat = new AudioFormat(AudioFormat.Encoding.ULAW, 8000.0F, 8, 1, 1, 8000.0F, true);
/*     */           try
/*     */           {
/* 252 */             AudioInputStream localAudioInputStream4 = new AudioInputStream(localBufferedInputStream, localAudioFormat, -1L);
/*     */ 
/* 254 */             startSampled(localAudioInputStream4, paramInputStream);
/*     */           } catch (UnsupportedAudioFileException localUnsupportedAudioFileException2) {
/* 256 */             return;
/*     */           } catch (LineUnavailableException localLineUnavailableException2) {
/* 258 */             return;
/*     */           }
/*     */ 
/*     */         }
/*     */         catch (MidiUnavailableException localMidiUnavailableException)
/*     */         {
/* 264 */           return;
/*     */         }
/*     */       }
/*     */       catch (LineUnavailableException localLineUnavailableException1)
/*     */       {
/* 269 */         return;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 274 */     notify();
/*     */   }
/*     */ 
/*     */   public synchronized void closeChannel(InputStream paramInputStream)
/*     */   {
/* 283 */     if (this.DEBUG) {
/* 284 */       System.out.println("AudioDevice.closeChannel");
/*     */     }
/*     */ 
/* 287 */     if (paramInputStream == null) return;
/*     */ 
/* 291 */     for (int i = 0; i < this.infos.size(); i++)
/*     */     {
/* 293 */       Info localInfo = (Info)this.infos.elementAt(i);
/*     */ 
/* 295 */       if (localInfo.in == paramInputStream)
/*     */       {
/* 297 */         if (localInfo.sequencer != null)
/*     */         {
/* 299 */           localInfo.sequencer.stop();
/*     */ 
/* 301 */           this.infos.removeElement(localInfo);
/*     */         }
/* 303 */         else if (localInfo.datapusher != null)
/*     */         {
/* 305 */           localInfo.datapusher.stop();
/* 306 */           this.infos.removeElement(localInfo);
/*     */         }
/*     */       }
/*     */     }
/* 310 */     notify();
/*     */   }
/*     */ 
/*     */   public synchronized void open()
/*     */   {
/*     */   }
/*     */ 
/*     */   public synchronized void close()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void play()
/*     */   {
/* 344 */     if (this.DEBUG)
/* 345 */       System.out.println("exiting play()");
/*     */   }
/*     */ 
/*     */   public synchronized void closeStreams()
/*     */   {
/* 356 */     for (int i = 0; i < this.infos.size(); i++)
/*     */     {
/* 358 */       Info localInfo = (Info)this.infos.elementAt(i);
/*     */ 
/* 360 */       if (localInfo.sequencer != null)
/*     */       {
/* 362 */         localInfo.sequencer.stop();
/* 363 */         localInfo.sequencer.close();
/* 364 */         this.infos.removeElement(localInfo);
/*     */       }
/* 366 */       else if (localInfo.datapusher != null)
/*     */       {
/* 368 */         localInfo.datapusher.stop();
/* 369 */         this.infos.removeElement(localInfo);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 374 */     if (this.DEBUG) {
/* 375 */       System.err.println("Audio Device: Streams all closed.");
/*     */     }
/*     */ 
/* 378 */     this.clipStreams = new Hashtable();
/* 379 */     this.infos = new Vector();
/*     */   }
/*     */ 
/*     */   public int openChannels()
/*     */   {
/* 386 */     return this.infos.size();
/*     */   }
/*     */ 
/*     */   void setVerbose(boolean paramBoolean)
/*     */   {
/* 393 */     this.DEBUG = paramBoolean;
/*     */   }
/*     */ 
/*     */   final class Info
/*     */     implements MetaEventListener
/*     */   {
/*     */     final Sequencer sequencer;
/*     */     final InputStream in;
/*     */     final DataPusher datapusher;
/*     */ 
/*     */     Info(Sequencer paramInputStream, InputStream paramDataPusher, DataPusher arg4)
/*     */     {
/* 411 */       this.sequencer = paramInputStream;
/* 412 */       this.in = paramDataPusher;
/*     */       Object localObject;
/* 413 */       this.datapusher = localObject;
/*     */     }
/*     */ 
/*     */     public void meta(MetaMessage paramMetaMessage) {
/* 417 */       if ((paramMetaMessage.getType() == 47) && (this.sequencer != null))
/* 418 */         this.sequencer.close();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.audio.AudioDevice
 * JD-Core Version:    0.6.2
 */