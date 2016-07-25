/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ import javax.sound.sampled.AudioFormat.Encoding;
/*     */ import javax.sound.sampled.AudioInputStream;
/*     */ import javax.sound.sampled.AudioSystem;
/*     */ import javax.sound.sampled.Clip;
/*     */ import javax.sound.sampled.Control;
/*     */ import javax.sound.sampled.Control.Type;
/*     */ import javax.sound.sampled.DataLine.Info;
/*     */ import javax.sound.sampled.Line;
/*     */ import javax.sound.sampled.Line.Info;
/*     */ import javax.sound.sampled.LineEvent;
/*     */ import javax.sound.sampled.LineEvent.Type;
/*     */ import javax.sound.sampled.LineListener;
/*     */ import javax.sound.sampled.LineUnavailableException;
/*     */ import javax.sound.sampled.Mixer;
/*     */ import javax.sound.sampled.Mixer.Info;
/*     */ import javax.sound.sampled.SourceDataLine;
/*     */ 
/*     */ public final class SoftMixingMixer
/*     */   implements Mixer
/*     */ {
/*     */   static final String INFO_NAME = "Gervill Sound Mixer";
/*     */   static final String INFO_VENDOR = "OpenJDK Proposal";
/*     */   static final String INFO_DESCRIPTION = "Software Sound Mixer";
/*     */   static final String INFO_VERSION = "1.0";
/*  67 */   static final Mixer.Info info = new Info();
/*     */ 
/*  69 */   final Object control_mutex = this;
/*     */ 
/*  71 */   boolean implicitOpen = false;
/*     */ 
/*  73 */   private boolean open = false;
/*     */ 
/*  75 */   private SoftMixingMainMixer mainmixer = null;
/*     */ 
/*  77 */   private AudioFormat format = new AudioFormat(44100.0F, 16, 2, true, false);
/*     */ 
/*  79 */   private SourceDataLine sourceDataLine = null;
/*     */ 
/*  81 */   private SoftAudioPusher pusher = null;
/*     */ 
/*  83 */   private AudioInputStream pusher_stream = null;
/*     */ 
/*  85 */   private final float controlrate = 147.0F;
/*     */ 
/*  87 */   private final long latency = 100000L;
/*     */ 
/*  89 */   private final boolean jitter_correction = false;
/*     */ 
/*  91 */   private final List<LineListener> listeners = new ArrayList();
/*     */   private final Line.Info[] sourceLineInfo;
/*     */ 
/*     */   public SoftMixingMixer()
/*     */   {
/*  97 */     this.sourceLineInfo = new Line.Info[2];
/*     */ 
/*  99 */     ArrayList localArrayList = new ArrayList();
/* 100 */     for (int i = 1; i <= 2; i++) {
/* 101 */       localArrayList.add(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, -1.0F, 8, i, i, -1.0F, false));
/*     */ 
/* 104 */       localArrayList.add(new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, -1.0F, 8, i, i, -1.0F, false));
/*     */ 
/* 107 */       for (int j = 16; j < 32; j += 8) {
/* 108 */         localArrayList.add(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, -1.0F, j, i, i * j / 8, -1.0F, false));
/*     */ 
/* 111 */         localArrayList.add(new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, -1.0F, j, i, i * j / 8, -1.0F, false));
/*     */ 
/* 114 */         localArrayList.add(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, -1.0F, j, i, i * j / 8, -1.0F, true));
/*     */ 
/* 117 */         localArrayList.add(new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, -1.0F, j, i, i * j / 8, -1.0F, true));
/*     */       }
/*     */ 
/* 121 */       localArrayList.add(new AudioFormat(AudioFormat.Encoding.PCM_FLOAT, -1.0F, 32, i, i * 4, -1.0F, false));
/*     */ 
/* 124 */       localArrayList.add(new AudioFormat(AudioFormat.Encoding.PCM_FLOAT, -1.0F, 32, i, i * 4, -1.0F, true));
/*     */ 
/* 127 */       localArrayList.add(new AudioFormat(AudioFormat.Encoding.PCM_FLOAT, -1.0F, 64, i, i * 8, -1.0F, false));
/*     */ 
/* 130 */       localArrayList.add(new AudioFormat(AudioFormat.Encoding.PCM_FLOAT, -1.0F, 64, i, i * 8, -1.0F, true));
/*     */     }
/*     */ 
/* 134 */     AudioFormat[] arrayOfAudioFormat = (AudioFormat[])localArrayList.toArray(new AudioFormat[localArrayList.size()]);
/*     */ 
/* 136 */     this.sourceLineInfo[0] = new DataLine.Info(SourceDataLine.class, arrayOfAudioFormat, -1, -1);
/*     */ 
/* 139 */     this.sourceLineInfo[1] = new DataLine.Info(Clip.class, arrayOfAudioFormat, -1, -1);
/*     */   }
/*     */ 
/*     */   public Line getLine(Line.Info paramInfo)
/*     */     throws LineUnavailableException
/*     */   {
/* 145 */     if (!isLineSupported(paramInfo)) {
/* 146 */       throw new IllegalArgumentException("Line unsupported: " + paramInfo);
/*     */     }
/* 148 */     if (paramInfo.getLineClass() == SourceDataLine.class) {
/* 149 */       return new SoftMixingSourceDataLine(this, (DataLine.Info)paramInfo);
/*     */     }
/* 151 */     if (paramInfo.getLineClass() == Clip.class) {
/* 152 */       return new SoftMixingClip(this, (DataLine.Info)paramInfo);
/*     */     }
/*     */ 
/* 155 */     throw new IllegalArgumentException("Line unsupported: " + paramInfo);
/*     */   }
/*     */ 
/*     */   public int getMaxLines(Line.Info paramInfo) {
/* 159 */     if (paramInfo.getLineClass() == SourceDataLine.class)
/* 160 */       return -1;
/* 161 */     if (paramInfo.getLineClass() == Clip.class)
/* 162 */       return -1;
/* 163 */     return 0;
/*     */   }
/*     */ 
/*     */   public Mixer.Info getMixerInfo() {
/* 167 */     return info;
/*     */   }
/*     */ 
/*     */   public Line.Info[] getSourceLineInfo() {
/* 171 */     Line.Info[] arrayOfInfo = new Line.Info[this.sourceLineInfo.length];
/* 172 */     System.arraycopy(this.sourceLineInfo, 0, arrayOfInfo, 0, this.sourceLineInfo.length);
/*     */ 
/* 174 */     return arrayOfInfo;
/*     */   }
/*     */ 
/*     */   public Line.Info[] getSourceLineInfo(Line.Info paramInfo)
/*     */   {
/* 180 */     ArrayList localArrayList = new ArrayList();
/*     */ 
/* 182 */     for (int i = 0; i < this.sourceLineInfo.length; i++) {
/* 183 */       if (paramInfo.matches(this.sourceLineInfo[i])) {
/* 184 */         localArrayList.add(this.sourceLineInfo[i]);
/*     */       }
/*     */     }
/* 187 */     return (Line.Info[])localArrayList.toArray(new Line.Info[localArrayList.size()]);
/*     */   }
/*     */ 
/*     */   public Line[] getSourceLines()
/*     */   {
/*     */     Line[] arrayOfLine;
/* 194 */     synchronized (this.control_mutex)
/*     */     {
/* 196 */       if (this.mainmixer == null)
/* 197 */         return new Line[0];
/* 198 */       SoftMixingDataLine[] arrayOfSoftMixingDataLine = this.mainmixer.getOpenLines();
/*     */ 
/* 200 */       arrayOfLine = new Line[arrayOfSoftMixingDataLine.length];
/*     */ 
/* 202 */       for (int i = 0; i < arrayOfLine.length; i++) {
/* 203 */         arrayOfLine[i] = arrayOfSoftMixingDataLine[i];
/*     */       }
/*     */     }
/*     */ 
/* 207 */     return arrayOfLine;
/*     */   }
/*     */ 
/*     */   public Line.Info[] getTargetLineInfo() {
/* 211 */     return new Line.Info[0];
/*     */   }
/*     */ 
/*     */   public Line.Info[] getTargetLineInfo(Line.Info paramInfo)
/*     */   {
/* 216 */     return new Line.Info[0];
/*     */   }
/*     */ 
/*     */   public Line[] getTargetLines() {
/* 220 */     return new Line[0];
/*     */   }
/*     */ 
/*     */   public boolean isLineSupported(Line.Info paramInfo) {
/* 224 */     if (paramInfo != null) {
/* 225 */       for (int i = 0; i < this.sourceLineInfo.length; i++) {
/* 226 */         if (paramInfo.matches(this.sourceLineInfo[i])) {
/* 227 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 231 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isSynchronizationSupported(Line[] paramArrayOfLine, boolean paramBoolean) {
/* 235 */     return false;
/*     */   }
/*     */ 
/*     */   public void synchronize(Line[] paramArrayOfLine, boolean paramBoolean) {
/* 239 */     throw new IllegalArgumentException("Synchronization not supported by this mixer.");
/*     */   }
/*     */ 
/*     */   public void unsynchronize(Line[] paramArrayOfLine)
/*     */   {
/* 244 */     throw new IllegalArgumentException("Synchronization not supported by this mixer.");
/*     */   }
/*     */ 
/*     */   public void addLineListener(LineListener paramLineListener)
/*     */   {
/* 249 */     synchronized (this.control_mutex) {
/* 250 */       this.listeners.add(paramLineListener);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void sendEvent(LineEvent paramLineEvent) {
/* 255 */     if (this.listeners.size() == 0)
/* 256 */       return;
/* 257 */     LineListener[] arrayOfLineListener1 = (LineListener[])this.listeners.toArray(new LineListener[this.listeners.size()]);
/*     */ 
/* 259 */     for (LineListener localLineListener : arrayOfLineListener1)
/* 260 */       localLineListener.update(paramLineEvent);
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 265 */     if (!isOpen()) {
/* 266 */       return;
/*     */     }
/* 268 */     sendEvent(new LineEvent(this, LineEvent.Type.CLOSE, -1L));
/*     */ 
/* 271 */     SoftAudioPusher localSoftAudioPusher = null;
/* 272 */     AudioInputStream localAudioInputStream = null;
/* 273 */     synchronized (this.control_mutex) {
/* 274 */       if (this.pusher != null) {
/* 275 */         localSoftAudioPusher = this.pusher;
/* 276 */         localAudioInputStream = this.pusher_stream;
/* 277 */         this.pusher = null;
/* 278 */         this.pusher_stream = null;
/*     */       }
/*     */     }
/*     */ 
/* 282 */     if (localSoftAudioPusher != null)
/*     */     {
/* 286 */       localSoftAudioPusher.stop();
/*     */       try
/*     */       {
/* 289 */         localAudioInputStream.close();
/*     */       } catch (IOException ) {
/* 291 */         ???.printStackTrace();
/*     */       }
/*     */     }
/*     */ 
/* 295 */     synchronized (this.control_mutex)
/*     */     {
/* 297 */       if (this.mainmixer != null)
/* 298 */         this.mainmixer.close();
/* 299 */       this.open = false;
/*     */ 
/* 301 */       if (this.sourceDataLine != null) {
/* 302 */         this.sourceDataLine.drain();
/* 303 */         this.sourceDataLine.close();
/* 304 */         this.sourceDataLine = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Control getControl(Control.Type paramType)
/*     */   {
/* 312 */     throw new IllegalArgumentException("Unsupported control type : " + paramType);
/*     */   }
/*     */ 
/*     */   public Control[] getControls()
/*     */   {
/* 317 */     return new Control[0];
/*     */   }
/*     */ 
/*     */   public Line.Info getLineInfo() {
/* 321 */     return new Line.Info(Mixer.class);
/*     */   }
/*     */ 
/*     */   public boolean isControlSupported(Control.Type paramType) {
/* 325 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isOpen() {
/* 329 */     synchronized (this.control_mutex) {
/* 330 */       return this.open;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void open() throws LineUnavailableException {
/* 335 */     if (isOpen()) {
/* 336 */       this.implicitOpen = false;
/* 337 */       return;
/*     */     }
/* 339 */     open(null);
/*     */   }
/*     */ 
/*     */   public void open(SourceDataLine paramSourceDataLine) throws LineUnavailableException {
/* 343 */     if (isOpen()) {
/* 344 */       this.implicitOpen = false;
/* 345 */       return;
/*     */     }
/* 347 */     synchronized (this.control_mutex)
/*     */     {
/*     */       try
/*     */       {
/* 351 */         if (paramSourceDataLine != null) {
/* 352 */           this.format = paramSourceDataLine.getFormat();
/*     */         }
/* 354 */         AudioInputStream localAudioInputStream = openStream(getFormat());
/*     */ 
/* 356 */         if (paramSourceDataLine == null) {
/* 357 */           synchronized (SoftMixingMixerProvider.mutex) {
/* 358 */             SoftMixingMixerProvider.lockthread = Thread.currentThread();
/*     */           }
/*     */ 
/*     */           try
/*     */           {
/* 363 */             ??? = AudioSystem.getMixer(null);
/* 364 */             if (??? != null)
/*     */             {
/* 368 */               Object localObject2 = null;
/* 369 */               AudioFormat localAudioFormat1 = null;
/*     */ 
/* 371 */               Line.Info[] arrayOfInfo = ???.getSourceLineInfo();
/*     */ 
/* 373 */               for (int k = 0; k < arrayOfInfo.length; k++)
/* 374 */                 if (arrayOfInfo[k].getLineClass() == SourceDataLine.class)
/*     */                 {
/* 376 */                   DataLine.Info localInfo = (DataLine.Info)arrayOfInfo[k];
/* 377 */                   AudioFormat[] arrayOfAudioFormat = localInfo.getFormats();
/* 378 */                   for (int m = 0; m < arrayOfAudioFormat.length; m++) {
/* 379 */                     AudioFormat localAudioFormat2 = arrayOfAudioFormat[m];
/* 380 */                     if ((localAudioFormat2.getChannels() == 2) || (localAudioFormat2.getChannels() == -1))
/*     */                     {
/* 382 */                       if ((localAudioFormat2.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED)) || (localAudioFormat2.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)))
/*     */                       {
/* 384 */                         if ((localAudioFormat2.getSampleRate() == -1.0F) || (localAudioFormat2.getSampleRate() == 48000.0D))
/*     */                         {
/* 386 */                           if ((localAudioFormat2.getSampleSizeInBits() == -1) || (localAudioFormat2.getSampleSizeInBits() == 16))
/*     */                           {
/* 389 */                             localObject2 = localInfo;
/* 390 */                             int n = localAudioFormat2.getChannels();
/* 391 */                             boolean bool1 = localAudioFormat2.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED);
/* 392 */                             float f = localAudioFormat2.getSampleRate();
/* 393 */                             boolean bool2 = localAudioFormat2.isBigEndian();
/* 394 */                             int i1 = localAudioFormat2.getSampleSizeInBits();
/* 395 */                             if (i1 == -1) i1 = 16;
/* 396 */                             if (n == -1) n = 2;
/* 397 */                             if (f == -1.0F) f = 48000.0F;
/* 398 */                             localAudioFormat1 = new AudioFormat(f, i1, n, bool1, bool2);
/*     */ 
/* 400 */                             break label359;
/*     */                           }
/*     */                         }
/*     */                       }
/*     */                     }
/*     */                   }
/*     */                 }
/* 406 */               label359: if (localAudioFormat1 != null)
/*     */               {
/* 408 */                 this.format = localAudioFormat1;
/* 409 */                 paramSourceDataLine = (SourceDataLine)???.getLine(localObject2);
/*     */               }
/*     */             }
/*     */ 
/* 413 */             if (paramSourceDataLine == null)
/* 414 */               paramSourceDataLine = AudioSystem.getSourceDataLine(this.format);
/*     */           } finally {
/* 416 */             synchronized (SoftMixingMixerProvider.mutex) {
/* 417 */               SoftMixingMixerProvider.lockthread = null;
/*     */             }
/*     */           }
/*     */ 
/* 421 */           if (paramSourceDataLine == null) {
/* 422 */             throw new IllegalArgumentException("No line matching " + info.toString() + " is supported.");
/*     */           }
/*     */         }
/*     */ 
/* 426 */         getClass(); double d = 100000.0D;
/*     */ 
/* 428 */         if (!paramSourceDataLine.isOpen()) {
/* 429 */           i = getFormat().getFrameSize() * (int)(getFormat().getFrameRate() * (d / 1000000.0D));
/*     */ 
/* 431 */           paramSourceDataLine.open(getFormat(), i);
/*     */ 
/* 435 */           this.sourceDataLine = paramSourceDataLine;
/*     */         }
/* 437 */         if (!paramSourceDataLine.isActive()) {
/* 438 */           paramSourceDataLine.start();
/*     */         }
/* 440 */         int i = 512;
/*     */         try {
/* 442 */           i = localAudioInputStream.available();
/*     */         }
/*     */         catch (IOException localIOException)
/*     */         {
/*     */         }
/*     */ 
/* 452 */         int j = paramSourceDataLine.getBufferSize();
/* 453 */         j -= j % i;
/*     */ 
/* 455 */         if (j < 3 * i) {
/* 456 */           j = 3 * i;
/*     */         }
/*     */ 
/* 462 */         this.pusher = new SoftAudioPusher(paramSourceDataLine, localAudioInputStream, i);
/* 463 */         this.pusher_stream = localAudioInputStream;
/* 464 */         this.pusher.start();
/*     */       }
/*     */       catch (LineUnavailableException localLineUnavailableException) {
/* 467 */         if (isOpen())
/* 468 */           close();
/* 469 */         throw new LineUnavailableException(localLineUnavailableException.toString());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public AudioInputStream openStream(AudioFormat paramAudioFormat)
/*     */     throws LineUnavailableException
/*     */   {
/* 478 */     if (isOpen()) {
/* 479 */       throw new LineUnavailableException("Mixer is already open");
/*     */     }
/* 481 */     synchronized (this.control_mutex)
/*     */     {
/* 483 */       this.open = true;
/*     */ 
/* 485 */       this.implicitOpen = false;
/*     */ 
/* 487 */       if (paramAudioFormat != null) {
/* 488 */         this.format = paramAudioFormat;
/*     */       }
/* 490 */       this.mainmixer = new SoftMixingMainMixer(this);
/*     */ 
/* 492 */       sendEvent(new LineEvent(this, LineEvent.Type.OPEN, -1L));
/*     */ 
/* 495 */       return this.mainmixer.getInputStream();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removeLineListener(LineListener paramLineListener)
/*     */   {
/* 502 */     synchronized (this.control_mutex) {
/* 503 */       this.listeners.remove(paramLineListener);
/*     */     }
/*     */   }
/*     */ 
/*     */   public long getLatency() {
/* 508 */     synchronized (this.control_mutex) {
/* 509 */       return 100000L;
/*     */     }
/*     */   }
/*     */ 
/*     */   public AudioFormat getFormat() {
/* 514 */     synchronized (this.control_mutex) {
/* 515 */       return this.format;
/*     */     }
/*     */   }
/*     */ 
/*     */   float getControlRate() {
/* 520 */     return 147.0F;
/*     */   }
/*     */ 
/*     */   SoftMixingMainMixer getMainMixer() {
/* 524 */     if (!isOpen())
/* 525 */       return null;
/* 526 */     return this.mainmixer;
/*     */   }
/*     */ 
/*     */   private static class Info extends Mixer.Info
/*     */   {
/*     */     Info()
/*     */     {
/*  55 */       super("OpenJDK Proposal", "Software Sound Mixer", "1.0");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SoftMixingMixer
 * JD-Core Version:    0.6.2
 */