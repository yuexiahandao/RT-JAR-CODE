/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.applet.AudioClip;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import javax.sound.midi.InvalidMidiDataException;
/*     */ import javax.sound.midi.MetaEventListener;
/*     */ import javax.sound.midi.MetaMessage;
/*     */ import javax.sound.midi.MidiFileFormat;
/*     */ import javax.sound.midi.MidiSystem;
/*     */ import javax.sound.midi.MidiUnavailableException;
/*     */ import javax.sound.midi.Sequence;
/*     */ import javax.sound.midi.Sequencer;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ import javax.sound.sampled.AudioInputStream;
/*     */ import javax.sound.sampled.AudioSystem;
/*     */ import javax.sound.sampled.Clip;
/*     */ import javax.sound.sampled.DataLine.Info;
/*     */ import javax.sound.sampled.Line;
/*     */ import javax.sound.sampled.LineEvent;
/*     */ import javax.sound.sampled.LineListener;
/*     */ import javax.sound.sampled.SourceDataLine;
/*     */ import javax.sound.sampled.UnsupportedAudioFileException;
/*     */ 
/*     */ public final class JavaSoundAudioClip
/*     */   implements AudioClip, MetaEventListener, LineListener
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   private static final int BUFFER_SIZE = 16384;
/*  65 */   private long lastPlayCall = 0L;
/*     */   private static final int MINIMUM_PLAY_DELAY = 30;
/*  68 */   private byte[] loadedAudio = null;
/*  69 */   private int loadedAudioByteLength = 0;
/*  70 */   private AudioFormat loadedAudioFormat = null;
/*     */ 
/*  72 */   private AutoClosingClip clip = null;
/*  73 */   private boolean clipLooping = false;
/*     */ 
/*  75 */   private DataPusher datapusher = null;
/*     */ 
/*  77 */   private Sequencer sequencer = null;
/*  78 */   private Sequence sequence = null;
/*  79 */   private boolean sequencerloop = false;
/*     */   private static final long CLIP_THRESHOLD = 1048576L;
/*     */   private static final int STREAM_BUFFER_SIZE = 1024;
/*     */ 
/*     */   public JavaSoundAudioClip(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/*  98 */     BufferedInputStream localBufferedInputStream = new BufferedInputStream(paramInputStream, 1024);
/*  99 */     localBufferedInputStream.mark(1024);
/* 100 */     boolean bool = false;
/*     */     try {
/* 102 */       AudioInputStream localAudioInputStream = AudioSystem.getAudioInputStream(localBufferedInputStream);
/*     */ 
/* 104 */       bool = loadAudioData(localAudioInputStream);
/*     */ 
/* 106 */       if (bool) {
/* 107 */         bool = false;
/* 108 */         if (this.loadedAudioByteLength < 1048576L) {
/* 109 */           bool = createClip();
/*     */         }
/* 111 */         if (!bool)
/* 112 */           bool = createSourceDataLine();
/*     */       }
/*     */     }
/*     */     catch (UnsupportedAudioFileException localUnsupportedAudioFileException)
/*     */     {
/*     */       try {
/* 118 */         MidiFileFormat localMidiFileFormat = MidiSystem.getMidiFileFormat(localBufferedInputStream);
/* 119 */         bool = createSequencer(localBufferedInputStream);
/*     */       } catch (InvalidMidiDataException localInvalidMidiDataException) {
/* 121 */         bool = false;
/*     */       }
/*     */     }
/* 124 */     if (!bool)
/* 125 */       throw new IOException("Unable to create AudioClip from input stream");
/*     */   }
/*     */ 
/*     */   public synchronized void play()
/*     */   {
/* 131 */     startImpl(false);
/*     */   }
/*     */ 
/*     */   public synchronized void loop()
/*     */   {
/* 136 */     startImpl(true);
/*     */   }
/*     */ 
/*     */   private synchronized void startImpl(boolean paramBoolean)
/*     */   {
/* 141 */     long l1 = System.currentTimeMillis();
/* 142 */     long l2 = l1 - this.lastPlayCall;
/* 143 */     if (l2 < 30L)
/*     */     {
/* 145 */       return;
/*     */     }
/* 147 */     this.lastPlayCall = l1;
/*     */     try
/*     */     {
/* 151 */       if (this.clip != null) {
/* 152 */         if (!this.clip.isOpen())
/*     */         {
/* 154 */           this.clip.open(this.loadedAudioFormat, this.loadedAudio, 0, this.loadedAudioByteLength);
/*     */         }
/*     */         else {
/* 157 */           this.clip.flush();
/* 158 */           if (paramBoolean != this.clipLooping)
/*     */           {
/* 161 */             this.clip.stop();
/*     */           }
/*     */         }
/* 164 */         this.clip.setFramePosition(0);
/* 165 */         if (paramBoolean)
/*     */         {
/* 167 */           this.clip.loop(-1);
/*     */         }
/*     */         else {
/* 170 */           this.clip.start();
/*     */         }
/* 172 */         this.clipLooping = paramBoolean;
/*     */       }
/* 175 */       else if (this.datapusher != null) {
/* 176 */         this.datapusher.start(paramBoolean);
/*     */       }
/* 179 */       else if (this.sequencer != null) {
/* 180 */         this.sequencerloop = paramBoolean;
/* 181 */         if (this.sequencer.isRunning()) {
/* 182 */           this.sequencer.setMicrosecondPosition(0L);
/*     */         }
/* 184 */         if (!this.sequencer.isOpen())
/*     */           try {
/* 186 */             this.sequencer.open();
/* 187 */             this.sequencer.setSequence(this.sequence);
/*     */           }
/*     */           catch (InvalidMidiDataException localInvalidMidiDataException)
/*     */           {
/*     */           }
/*     */           catch (MidiUnavailableException localMidiUnavailableException)
/*     */           {
/*     */           }
/* 195 */         this.sequencer.addMetaEventListener(this);
/*     */         try {
/* 197 */           this.sequencer.start();
/*     */         }
/*     */         catch (Exception localException1)
/*     */         {
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception localException2)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void stop()
/*     */   {
/* 211 */     this.lastPlayCall = 0L;
/*     */ 
/* 213 */     if (this.clip != null)
/*     */     {
/*     */       try {
/* 216 */         this.clip.flush();
/*     */       }
/*     */       catch (Exception localException1)
/*     */       {
/*     */       }
/*     */       try {
/* 222 */         this.clip.stop();
/*     */       }
/*     */       catch (Exception localException2)
/*     */       {
/*     */       }
/*     */     }
/* 228 */     else if (this.datapusher != null) {
/* 229 */       this.datapusher.stop();
/*     */     }
/* 232 */     else if (this.sequencer != null) {
/*     */       try {
/* 234 */         this.sequencerloop = false;
/* 235 */         this.sequencer.addMetaEventListener(this);
/* 236 */         this.sequencer.stop();
/*     */       }
/*     */       catch (Exception localException3) {
/*     */       }
/*     */       try {
/* 241 */         this.sequencer.close();
/*     */       }
/*     */       catch (Exception localException4)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void update(LineEvent paramLineEvent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public synchronized void meta(MetaMessage paramMetaMessage)
/*     */   {
/* 261 */     if (paramMetaMessage.getType() == 47)
/* 262 */       if (this.sequencerloop)
/*     */       {
/* 264 */         this.sequencer.setMicrosecondPosition(0L);
/* 265 */         loop();
/*     */       } else {
/* 267 */         stop();
/*     */       }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 274 */     return getClass().toString();
/*     */   }
/*     */ 
/*     */   protected void finalize()
/*     */   {
/* 280 */     if (this.clip != null)
/*     */     {
/* 282 */       this.clip.close();
/*     */     }
/*     */ 
/* 286 */     if (this.datapusher != null) {
/* 287 */       this.datapusher.close();
/*     */     }
/*     */ 
/* 290 */     if (this.sequencer != null)
/* 291 */       this.sequencer.close();
/*     */   }
/*     */ 
/*     */   private boolean loadAudioData(AudioInputStream paramAudioInputStream)
/*     */     throws IOException, UnsupportedAudioFileException
/*     */   {
/* 301 */     paramAudioInputStream = Toolkit.getPCMConvertedAudioInputStream(paramAudioInputStream);
/* 302 */     if (paramAudioInputStream == null) {
/* 303 */       return false;
/*     */     }
/*     */ 
/* 306 */     this.loadedAudioFormat = paramAudioInputStream.getFormat();
/* 307 */     long l1 = paramAudioInputStream.getFrameLength();
/* 308 */     int i = this.loadedAudioFormat.getFrameSize();
/* 309 */     long l2 = -1L;
/* 310 */     if ((l1 != -1L) && (l1 > 0L) && (i != -1) && (i > 0))
/*     */     {
/* 314 */       l2 = l1 * i;
/*     */     }
/* 316 */     if (l2 != -1L)
/*     */     {
/* 318 */       readStream(paramAudioInputStream, l2);
/*     */     }
/*     */     else {
/* 321 */       readStream(paramAudioInputStream);
/*     */     }
/*     */ 
/* 326 */     return true;
/*     */   }
/*     */ 
/*     */   private void readStream(AudioInputStream paramAudioInputStream, long paramLong)
/*     */     throws IOException
/*     */   {
/*     */     int i;
/* 334 */     if (paramLong > 2147483647L)
/* 335 */       i = 2147483647;
/*     */     else {
/* 337 */       i = (int)paramLong;
/*     */     }
/* 339 */     this.loadedAudio = new byte[i];
/* 340 */     this.loadedAudioByteLength = 0;
/*     */     while (true)
/*     */     {
/* 344 */       int j = paramAudioInputStream.read(this.loadedAudio, this.loadedAudioByteLength, i - this.loadedAudioByteLength);
/* 345 */       if (j <= 0) {
/* 346 */         paramAudioInputStream.close();
/* 347 */         break;
/*     */       }
/* 349 */       this.loadedAudioByteLength += j;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readStream(AudioInputStream paramAudioInputStream) throws IOException
/*     */   {
/* 355 */     DirectBAOS localDirectBAOS = new DirectBAOS();
/* 356 */     byte[] arrayOfByte = new byte[16384];
/* 357 */     int i = 0;
/* 358 */     int j = 0;
/*     */     while (true)
/*     */     {
/* 362 */       i = paramAudioInputStream.read(arrayOfByte, 0, arrayOfByte.length);
/* 363 */       if (i <= 0) {
/* 364 */         paramAudioInputStream.close();
/* 365 */         break;
/*     */       }
/* 367 */       j += i;
/* 368 */       localDirectBAOS.write(arrayOfByte, 0, i);
/*     */     }
/* 370 */     this.loadedAudio = localDirectBAOS.getInternalBuffer();
/* 371 */     this.loadedAudioByteLength = j;
/*     */   }
/*     */ 
/*     */   private boolean createClip()
/*     */   {
/*     */     try
/*     */     {
/* 382 */       DataLine.Info localInfo = new DataLine.Info(Clip.class, this.loadedAudioFormat);
/* 383 */       if (!AudioSystem.isLineSupported(localInfo))
/*     */       {
/* 386 */         return false;
/*     */       }
/* 388 */       Line localLine = AudioSystem.getLine(localInfo);
/* 389 */       if (!(localLine instanceof AutoClosingClip))
/*     */       {
/* 392 */         return false;
/*     */       }
/* 394 */       this.clip = ((AutoClosingClip)localLine);
/* 395 */       this.clip.setAutoClosing(true);
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 400 */       return false;
/*     */     }
/*     */ 
/* 403 */     if (this.clip == null)
/*     */     {
/* 405 */       return false;
/*     */     }
/*     */ 
/* 409 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean createSourceDataLine()
/*     */   {
/*     */     try {
/* 415 */       DataLine.Info localInfo = new DataLine.Info(SourceDataLine.class, this.loadedAudioFormat);
/* 416 */       if (!AudioSystem.isLineSupported(localInfo))
/*     */       {
/* 419 */         return false;
/*     */       }
/* 421 */       SourceDataLine localSourceDataLine = (SourceDataLine)AudioSystem.getLine(localInfo);
/* 422 */       this.datapusher = new DataPusher(localSourceDataLine, this.loadedAudioFormat, this.loadedAudio, this.loadedAudioByteLength);
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 426 */       return false;
/*     */     }
/*     */ 
/* 429 */     if (this.datapusher == null)
/*     */     {
/* 431 */       return false;
/*     */     }
/*     */ 
/* 435 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean createSequencer(BufferedInputStream paramBufferedInputStream)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 444 */       this.sequencer = MidiSystem.getSequencer();
/*     */     }
/*     */     catch (MidiUnavailableException localMidiUnavailableException) {
/* 447 */       return false;
/*     */     }
/* 449 */     if (this.sequencer == null) {
/* 450 */       return false;
/*     */     }
/*     */     try
/*     */     {
/* 454 */       this.sequence = MidiSystem.getSequence(paramBufferedInputStream);
/* 455 */       if (this.sequence == null)
/* 456 */         return false;
/*     */     }
/*     */     catch (InvalidMidiDataException localInvalidMidiDataException)
/*     */     {
/* 460 */       return false;
/*     */     }
/*     */ 
/* 464 */     return true;
/*     */   }
/*     */ 
/*     */   private static class DirectBAOS extends ByteArrayOutputStream
/*     */   {
/*     */     public byte[] getInternalBuffer()
/*     */     {
/* 478 */       return this.buf;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.JavaSoundAudioClip
 * JD-Core Version:    0.6.2
 */