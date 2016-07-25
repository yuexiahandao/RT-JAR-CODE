/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ import javax.sound.sampled.Control;
/*     */ import javax.sound.sampled.DataLine;
/*     */ import javax.sound.sampled.DataLine.Info;
/*     */ import javax.sound.sampled.LineEvent;
/*     */ import javax.sound.sampled.LineEvent.Type;
/*     */ import javax.sound.sampled.LineUnavailableException;
/*     */ 
/*     */ abstract class AbstractDataLine extends AbstractLine
/*     */   implements DataLine
/*     */ {
/*     */   private final AudioFormat defaultFormat;
/*     */   private final int defaultBufferSize;
/*  52 */   protected final Object lock = new Object();
/*     */   protected AudioFormat format;
/*     */   protected int bufferSize;
/*  62 */   protected boolean running = false;
/*  63 */   private boolean started = false;
/*  64 */   private boolean active = false;
/*     */ 
/*     */   protected AbstractDataLine(DataLine.Info paramInfo, AbstractMixer paramAbstractMixer, Control[] paramArrayOfControl)
/*     */   {
/*  71 */     this(paramInfo, paramAbstractMixer, paramArrayOfControl, null, -1);
/*     */   }
/*     */ 
/*     */   protected AbstractDataLine(DataLine.Info paramInfo, AbstractMixer paramAbstractMixer, Control[] paramArrayOfControl, AudioFormat paramAudioFormat, int paramInt)
/*     */   {
/*  79 */     super(paramInfo, paramAbstractMixer, paramArrayOfControl);
/*     */ 
/*  82 */     if (paramAudioFormat != null) {
/*  83 */       this.defaultFormat = paramAudioFormat;
/*     */     }
/*     */     else {
/*  86 */       this.defaultFormat = new AudioFormat(44100.0F, 16, 2, true, Platform.isBigEndian());
/*     */     }
/*  88 */     if (paramInt > 0) {
/*  89 */       this.defaultBufferSize = paramInt;
/*     */     }
/*     */     else {
/*  92 */       this.defaultBufferSize = ((int)(this.defaultFormat.getFrameRate() / 2.0F) * this.defaultFormat.getFrameSize());
/*     */     }
/*     */ 
/*  96 */     this.format = this.defaultFormat;
/*  97 */     this.bufferSize = this.defaultBufferSize;
/*     */   }
/*     */ 
/*     */   public final void open(AudioFormat paramAudioFormat, int paramInt)
/*     */     throws LineUnavailableException
/*     */   {
/* 105 */     synchronized (this.mixer)
/*     */     {
/* 109 */       if (!isOpen())
/*     */       {
/* 112 */         Toolkit.isFullySpecifiedAudioFormat(paramAudioFormat);
/*     */ 
/* 117 */         this.mixer.open(this);
/*     */         try
/*     */         {
/* 121 */           implOpen(paramAudioFormat, paramInt);
/*     */ 
/* 124 */           setOpen(true);
/*     */         }
/*     */         catch (LineUnavailableException localLineUnavailableException)
/*     */         {
/* 128 */           this.mixer.close(this);
/* 129 */           throw localLineUnavailableException;
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 137 */         if (!paramAudioFormat.matches(getFormat())) {
/* 138 */           throw new IllegalStateException("Line is already open with format " + getFormat() + " and bufferSize " + getBufferSize());
/*     */         }
/*     */ 
/* 142 */         if (paramInt > 0)
/* 143 */           setBufferSize(paramInt);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void open(AudioFormat paramAudioFormat)
/*     */     throws LineUnavailableException
/*     */   {
/* 153 */     open(paramAudioFormat, -1);
/*     */   }
/*     */ 
/*     */   public int available()
/*     */   {
/* 161 */     return 0;
/*     */   }
/*     */ 
/*     */   public void drain()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */   {
/*     */   }
/*     */ 
/*     */   public final void start()
/*     */   {
/* 183 */     synchronized (this.mixer)
/*     */     {
/* 187 */       if (isOpen())
/*     */       {
/* 189 */         if (!isStartedRunning()) {
/* 190 */           this.mixer.start(this);
/* 191 */           implStart();
/* 192 */           this.running = true;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 197 */     synchronized (this.lock) {
/* 198 */       this.lock.notifyAll();
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void stop()
/*     */   {
/* 208 */     synchronized (this.mixer)
/*     */     {
/* 212 */       if (isOpen())
/*     */       {
/* 214 */         if (isStartedRunning())
/*     */         {
/* 216 */           implStop();
/* 217 */           this.mixer.stop(this);
/*     */ 
/* 219 */           this.running = false;
/*     */ 
/* 222 */           if ((this.started) && (!isActive())) {
/* 223 */             setStarted(false);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 229 */     synchronized (this.lock) {
/* 230 */       this.lock.notifyAll();
/*     */     }
/*     */   }
/*     */ 
/*     */   public final boolean isRunning()
/*     */   {
/* 250 */     return this.started;
/*     */   }
/*     */ 
/*     */   public final boolean isActive() {
/* 254 */     return this.active;
/*     */   }
/*     */ 
/*     */   public final long getMicrosecondPosition()
/*     */   {
/* 260 */     long l = getLongFramePosition();
/* 261 */     if (l != -1L) {
/* 262 */       l = Toolkit.frames2micros(getFormat(), l);
/*     */     }
/* 264 */     return l;
/*     */   }
/*     */ 
/*     */   public final AudioFormat getFormat()
/*     */   {
/* 269 */     return this.format;
/*     */   }
/*     */ 
/*     */   public final int getBufferSize()
/*     */   {
/* 274 */     return this.bufferSize;
/*     */   }
/*     */ 
/*     */   public final int setBufferSize(int paramInt)
/*     */   {
/* 281 */     return getBufferSize();
/*     */   }
/*     */ 
/*     */   public final float getLevel()
/*     */   {
/* 288 */     return -1.0F;
/*     */   }
/*     */ 
/*     */   final boolean isStartedRunning()
/*     */   {
/* 305 */     return this.running;
/*     */   }
/*     */ 
/*     */   final void setActive(boolean paramBoolean)
/*     */   {
/* 319 */     synchronized (this)
/*     */     {
/* 324 */       if (this.active != paramBoolean)
/* 325 */         this.active = paramBoolean;
/*     */     }
/*     */   }
/*     */ 
/*     */   final void setStarted(boolean paramBoolean)
/*     */   {
/* 355 */     int i = 0;
/* 356 */     long l = getLongFramePosition();
/*     */ 
/* 358 */     synchronized (this)
/*     */     {
/* 363 */       if (this.started != paramBoolean) {
/* 364 */         this.started = paramBoolean;
/* 365 */         i = 1;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 372 */     if (i != 0)
/*     */     {
/* 374 */       if (paramBoolean)
/* 375 */         sendEvents(new LineEvent(this, LineEvent.Type.START, l));
/*     */       else
/* 377 */         sendEvents(new LineEvent(this, LineEvent.Type.STOP, l));
/*     */     }
/*     */   }
/*     */ 
/*     */   final void setEOM()
/*     */   {
/* 393 */     setStarted(false);
/*     */   }
/*     */ 
/*     */   public final void open()
/*     */     throws LineUnavailableException
/*     */   {
/* 413 */     open(this.format, this.bufferSize);
/*     */   }
/*     */ 
/*     */   public final void close()
/*     */   {
/* 424 */     synchronized (this.mixer)
/*     */     {
/* 427 */       if (isOpen())
/*     */       {
/* 430 */         stop();
/*     */ 
/* 433 */         setOpen(false);
/*     */ 
/* 436 */         implClose();
/*     */ 
/* 439 */         this.mixer.close(this);
/*     */ 
/* 442 */         this.format = this.defaultFormat;
/* 443 */         this.bufferSize = this.defaultBufferSize;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   abstract void implOpen(AudioFormat paramAudioFormat, int paramInt)
/*     */     throws LineUnavailableException;
/*     */ 
/*     */   abstract void implClose();
/*     */ 
/*     */   abstract void implStart();
/*     */ 
/*     */   abstract void implStop();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.AbstractDataLine
 * JD-Core Version:    0.6.2
 */