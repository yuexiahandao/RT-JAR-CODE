/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ import javax.sound.sampled.AudioInputStream;
/*     */ import javax.sound.sampled.Clip;
/*     */ import javax.sound.sampled.DataLine.Info;
/*     */ import javax.sound.sampled.LineEvent;
/*     */ import javax.sound.sampled.LineEvent.Type;
/*     */ import javax.sound.sampled.LineUnavailableException;
/*     */ 
/*     */ public final class SoftMixingClip extends SoftMixingDataLine
/*     */   implements Clip
/*     */ {
/*     */   private AudioFormat format;
/*     */   private int framesize;
/*     */   private byte[] data;
/*  53 */   private final InputStream datastream = new InputStream()
/*     */   {
/*     */     public int read() throws IOException {
/*  56 */       byte[] arrayOfByte = new byte[1];
/*  57 */       int i = read(arrayOfByte);
/*  58 */       if (i < 0)
/*  59 */         return i;
/*  60 */       return arrayOfByte[0] & 0xFF;
/*     */     }
/*     */ 
/*     */     public int read(byte[] paramAnonymousArrayOfByte, int paramAnonymousInt1, int paramAnonymousInt2) throws IOException
/*     */     {
/*  65 */       if (SoftMixingClip.this._loopcount != 0) {
/*  66 */         i = SoftMixingClip.this._loopend * SoftMixingClip.this.framesize;
/*  67 */         j = SoftMixingClip.this._loopstart * SoftMixingClip.this.framesize;
/*  68 */         int k = SoftMixingClip.this._frameposition * SoftMixingClip.this.framesize;
/*     */ 
/*  70 */         if ((k + paramAnonymousInt2 >= i) && 
/*  71 */           (k < i)) {
/*  72 */           int m = paramAnonymousInt1 + paramAnonymousInt2;
/*  73 */           int n = paramAnonymousInt1;
/*     */           int i1;
/*  74 */           while (paramAnonymousInt1 != m) {
/*  75 */             if (k == i) {
/*  76 */               if (SoftMixingClip.this._loopcount == 0)
/*     */                 break;
/*  78 */               k = j;
/*  79 */               if (SoftMixingClip.this._loopcount != -1)
/*  80 */                 SoftMixingClip.access$010(SoftMixingClip.this);
/*     */             }
/*  82 */             paramAnonymousInt2 = m - paramAnonymousInt1;
/*  83 */             i1 = i - k;
/*  84 */             if (paramAnonymousInt2 > i1)
/*  85 */               paramAnonymousInt2 = i1;
/*  86 */             System.arraycopy(SoftMixingClip.this.data, k, paramAnonymousArrayOfByte, paramAnonymousInt1, paramAnonymousInt2);
/*  87 */             paramAnonymousInt1 += paramAnonymousInt2;
/*     */           }
/*  89 */           if (SoftMixingClip.this._loopcount == 0) {
/*  90 */             paramAnonymousInt2 = m - paramAnonymousInt1;
/*  91 */             i1 = i - k;
/*  92 */             if (paramAnonymousInt2 > i1)
/*  93 */               paramAnonymousInt2 = i1;
/*  94 */             System.arraycopy(SoftMixingClip.this.data, k, paramAnonymousArrayOfByte, paramAnonymousInt1, paramAnonymousInt2);
/*  95 */             paramAnonymousInt1 += paramAnonymousInt2;
/*     */           }
/*  97 */           SoftMixingClip.this._frameposition = (k / SoftMixingClip.this.framesize);
/*  98 */           return n - paramAnonymousInt1;
/*     */         }
/*     */       }
/*     */ 
/* 102 */       int i = SoftMixingClip.this._frameposition * SoftMixingClip.this.framesize;
/* 103 */       int j = SoftMixingClip.this.bufferSize - i;
/* 104 */       if (j == 0)
/* 105 */         return -1;
/* 106 */       if (paramAnonymousInt2 > j)
/* 107 */         paramAnonymousInt2 = j;
/* 108 */       System.arraycopy(SoftMixingClip.this.data, i, paramAnonymousArrayOfByte, paramAnonymousInt1, paramAnonymousInt2);
/* 109 */       SoftMixingClip.access$412(SoftMixingClip.this, paramAnonymousInt2 / SoftMixingClip.this.framesize);
/* 110 */       return paramAnonymousInt2; }  } ;
/*     */   private int offset;
/*     */   private int bufferSize;
/*     */   private float[] readbuffer;
/* 121 */   private boolean open = false;
/*     */   private AudioFormat outputformat;
/*     */   private int out_nrofchannels;
/*     */   private int in_nrofchannels;
/* 129 */   private int frameposition = 0;
/*     */ 
/* 131 */   private boolean frameposition_sg = false;
/*     */ 
/* 133 */   private boolean active_sg = false;
/*     */ 
/* 135 */   private int loopstart = 0;
/*     */ 
/* 137 */   private int loopend = -1;
/*     */ 
/* 139 */   private boolean active = false;
/*     */ 
/* 141 */   private int loopcount = 0;
/*     */ 
/* 143 */   private boolean _active = false;
/*     */ 
/* 145 */   private int _frameposition = 0;
/*     */ 
/* 147 */   private boolean loop_sg = false;
/*     */ 
/* 149 */   private int _loopcount = 0;
/*     */ 
/* 151 */   private int _loopstart = 0;
/*     */ 
/* 153 */   private int _loopend = -1;
/*     */   private float _rightgain;
/*     */   private float _leftgain;
/*     */   private float _eff1gain;
/*     */   private float _eff2gain;
/*     */   private AudioFloatInputStream afis;
/*     */ 
/* 166 */   SoftMixingClip(SoftMixingMixer paramSoftMixingMixer, DataLine.Info paramInfo) { super(paramSoftMixingMixer, paramInfo); }
/*     */ 
/*     */ 
/*     */   protected void processControlLogic()
/*     */   {
/* 171 */     this._rightgain = this.rightgain;
/* 172 */     this._leftgain = this.leftgain;
/* 173 */     this._eff1gain = this.eff1gain;
/* 174 */     this._eff2gain = this.eff2gain;
/*     */ 
/* 176 */     if (this.active_sg) {
/* 177 */       this._active = this.active;
/* 178 */       this.active_sg = false;
/*     */     } else {
/* 180 */       this.active = this._active;
/*     */     }
/*     */ 
/* 183 */     if (this.frameposition_sg) {
/* 184 */       this._frameposition = this.frameposition;
/* 185 */       this.frameposition_sg = false;
/* 186 */       this.afis = null;
/*     */     } else {
/* 188 */       this.frameposition = this._frameposition;
/*     */     }
/* 190 */     if (this.loop_sg) {
/* 191 */       this._loopcount = this.loopcount;
/* 192 */       this._loopstart = this.loopstart;
/* 193 */       this._loopend = this.loopend;
/*     */     }
/*     */ 
/* 196 */     if (this.afis == null) {
/* 197 */       this.afis = AudioFloatInputStream.getInputStream(new AudioInputStream(this.datastream, this.format, -1L));
/*     */ 
/* 200 */       if (Math.abs(this.format.getSampleRate() - this.outputformat.getSampleRate()) > 1.0E-006D)
/* 201 */         this.afis = new SoftMixingDataLine.AudioFloatInputStreamResampler(this.afis, this.outputformat);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void processAudioLogic(SoftAudioBuffer[] paramArrayOfSoftAudioBuffer)
/*     */   {
/* 207 */     if (this._active) {
/* 208 */       float[] arrayOfFloat1 = paramArrayOfSoftAudioBuffer[0].array();
/* 209 */       float[] arrayOfFloat2 = paramArrayOfSoftAudioBuffer[1].array();
/* 210 */       int i = paramArrayOfSoftAudioBuffer[0].getSize();
/*     */ 
/* 212 */       int j = i * this.in_nrofchannels;
/* 213 */       if ((this.readbuffer == null) || (this.readbuffer.length < j)) {
/* 214 */         this.readbuffer = new float[j];
/*     */       }
/* 216 */       int k = 0;
/*     */       try {
/* 218 */         k = this.afis.read(this.readbuffer);
/* 219 */         if (k == -1) {
/* 220 */           this._active = false;
/* 221 */           return;
/*     */         }
/* 223 */         if (k != this.in_nrofchannels)
/* 224 */           Arrays.fill(this.readbuffer, k, j, 0.0F);
/*     */       }
/*     */       catch (IOException localIOException) {
/*     */       }
/* 228 */       int m = this.in_nrofchannels;
/* 229 */       int n = 0; for (int i1 = 0; n < i; i1 += m) {
/* 230 */         arrayOfFloat1[n] += this.readbuffer[i1] * this._leftgain;
/*     */ 
/* 229 */         n++;
/*     */       }
/*     */ 
/* 233 */       if (this.out_nrofchannels != 1)
/* 234 */         if (this.in_nrofchannels == 1) {
/* 235 */           n = 0; for (i1 = 0; n < i; i1 += m) {
/* 236 */             arrayOfFloat2[n] += this.readbuffer[i1] * this._rightgain;
/*     */ 
/* 235 */             n++;
/*     */           }
/*     */         }
/*     */         else {
/* 239 */           n = 0; for (i1 = 1; n < i; i1 += m) {
/* 240 */             arrayOfFloat2[n] += this.readbuffer[i1] * this._rightgain;
/*     */ 
/* 239 */             n++;
/*     */           }
/*     */         }
/*     */       float[] arrayOfFloat3;
/*     */       int i2;
/* 246 */       if (this._eff1gain > 0.0002D)
/*     */       {
/* 248 */         arrayOfFloat3 = paramArrayOfSoftAudioBuffer[2].array();
/*     */ 
/* 250 */         i1 = 0; for (i2 = 0; i1 < i; i2 += m) {
/* 251 */           arrayOfFloat3[i1] += this.readbuffer[i2] * this._eff1gain;
/*     */ 
/* 250 */           i1++;
/*     */         }
/*     */ 
/* 253 */         if (this.in_nrofchannels == 2) {
/* 254 */           i1 = 0; for (i2 = 1; i1 < i; i2 += m) {
/* 255 */             arrayOfFloat3[i1] += this.readbuffer[i2] * this._eff1gain;
/*     */ 
/* 254 */             i1++;
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 260 */       if (this._eff2gain > 0.0002D) {
/* 261 */         arrayOfFloat3 = paramArrayOfSoftAudioBuffer[3].array();
/*     */ 
/* 263 */         i1 = 0; for (i2 = 0; i1 < i; i2 += m) {
/* 264 */           arrayOfFloat3[i1] += this.readbuffer[i2] * this._eff2gain;
/*     */ 
/* 263 */           i1++;
/*     */         }
/*     */ 
/* 266 */         if (this.in_nrofchannels == 2) {
/* 267 */           i1 = 0; for (i2 = 1; i1 < i; i2 += m) {
/* 268 */             arrayOfFloat3[i1] += this.readbuffer[i2] * this._eff2gain;
/*     */ 
/* 267 */             i1++;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getFrameLength()
/*     */   {
/* 277 */     return this.bufferSize / this.format.getFrameSize();
/*     */   }
/*     */ 
/*     */   public long getMicrosecondLength() {
/* 281 */     return ()(getFrameLength() * (1000000.0D / getFormat().getSampleRate()));
/*     */   }
/*     */ 
/*     */   public void loop(int paramInt)
/*     */   {
/* 286 */     LineEvent localLineEvent = null;
/*     */ 
/* 288 */     synchronized (this.control_mutex) {
/* 289 */       if (isOpen()) {
/* 290 */         if (this.active)
/* 291 */           return;
/* 292 */         this.active = true;
/* 293 */         this.active_sg = true;
/* 294 */         this.loopcount = paramInt;
/* 295 */         localLineEvent = new LineEvent(this, LineEvent.Type.START, getLongFramePosition());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 300 */     if (localLineEvent != null)
/* 301 */       sendEvent(localLineEvent);
/*     */   }
/*     */ 
/*     */   public void open(AudioInputStream paramAudioInputStream)
/*     */     throws LineUnavailableException, IOException
/*     */   {
/* 307 */     if (isOpen()) {
/* 308 */       throw new IllegalStateException("Clip is already open with format " + getFormat() + " and frame lengh of " + getFrameLength());
/*     */     }
/*     */ 
/* 311 */     if (AudioFloatConverter.getConverter(paramAudioInputStream.getFormat()) == null)
/* 312 */       throw new IllegalArgumentException("Invalid format : " + paramAudioInputStream.getFormat().toString());
/*     */     Object localObject;
/*     */     int j;
/* 315 */     if (paramAudioInputStream.getFrameLength() != -1L) {
/* 316 */       localObject = new byte[(int)paramAudioInputStream.getFrameLength() * paramAudioInputStream.getFormat().getFrameSize()];
/*     */ 
/* 318 */       int i = 512 * paramAudioInputStream.getFormat().getFrameSize();
/* 319 */       j = 0;
/* 320 */       while (j != localObject.length) {
/* 321 */         if (i > localObject.length - j)
/* 322 */           i = localObject.length - j;
/* 323 */         int k = paramAudioInputStream.read((byte[])localObject, j, i);
/* 324 */         if (k == -1)
/*     */           break;
/* 326 */         if (k == 0)
/* 327 */           Thread.yield();
/* 328 */         j += k;
/*     */       }
/* 330 */       open(paramAudioInputStream.getFormat(), (byte[])localObject, 0, j);
/*     */     } else {
/* 332 */       localObject = new ByteArrayOutputStream();
/* 333 */       byte[] arrayOfByte = new byte[512 * paramAudioInputStream.getFormat().getFrameSize()];
/* 334 */       j = 0;
/* 335 */       while ((j = paramAudioInputStream.read(arrayOfByte)) != -1) {
/* 336 */         if (j == 0)
/* 337 */           Thread.yield();
/* 338 */         ((ByteArrayOutputStream)localObject).write(arrayOfByte, 0, j);
/*     */       }
/* 340 */       open(paramAudioInputStream.getFormat(), ((ByteArrayOutputStream)localObject).toByteArray(), 0, ((ByteArrayOutputStream)localObject).size());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void open(AudioFormat paramAudioFormat, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws LineUnavailableException
/*     */   {
/* 347 */     synchronized (this.control_mutex) {
/* 348 */       if (isOpen()) {
/* 349 */         throw new IllegalStateException("Clip is already open with format " + getFormat() + " and frame lengh of " + getFrameLength());
/*     */       }
/*     */ 
/* 353 */       if (AudioFloatConverter.getConverter(paramAudioFormat) == null) {
/* 354 */         throw new IllegalArgumentException("Invalid format : " + paramAudioFormat.toString());
/*     */       }
/* 356 */       if (paramInt2 % paramAudioFormat.getFrameSize() != 0) {
/* 357 */         throw new IllegalArgumentException("Buffer size does not represent an integral number of sample frames!");
/*     */       }
/*     */ 
/* 360 */       this.data = paramArrayOfByte;
/* 361 */       this.offset = paramInt1;
/* 362 */       this.bufferSize = paramInt2;
/* 363 */       this.format = paramAudioFormat;
/* 364 */       this.framesize = paramAudioFormat.getFrameSize();
/*     */ 
/* 366 */       this.loopstart = 0;
/* 367 */       this.loopend = -1;
/* 368 */       this.loop_sg = true;
/*     */ 
/* 370 */       if (!this.mixer.isOpen()) {
/* 371 */         this.mixer.open();
/* 372 */         this.mixer.implicitOpen = true;
/*     */       }
/*     */ 
/* 375 */       this.outputformat = this.mixer.getFormat();
/* 376 */       this.out_nrofchannels = this.outputformat.getChannels();
/* 377 */       this.in_nrofchannels = paramAudioFormat.getChannels();
/*     */ 
/* 379 */       this.open = true;
/*     */ 
/* 381 */       this.mixer.getMainMixer().openLine(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setFramePosition(int paramInt)
/*     */   {
/* 387 */     synchronized (this.control_mutex) {
/* 388 */       this.frameposition_sg = true;
/* 389 */       this.frameposition = paramInt;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setLoopPoints(int paramInt1, int paramInt2) {
/* 394 */     synchronized (this.control_mutex) {
/* 395 */       if (paramInt2 != -1) {
/* 396 */         if (paramInt2 < paramInt1) {
/* 397 */           throw new IllegalArgumentException("Invalid loop points : " + paramInt1 + " - " + paramInt2);
/*     */         }
/* 399 */         if (paramInt2 * this.framesize > this.bufferSize) {
/* 400 */           throw new IllegalArgumentException("Invalid loop points : " + paramInt1 + " - " + paramInt2);
/*     */         }
/*     */       }
/* 403 */       if (paramInt1 * this.framesize > this.bufferSize) {
/* 404 */         throw new IllegalArgumentException("Invalid loop points : " + paramInt1 + " - " + paramInt2);
/*     */       }
/* 406 */       if (0 < paramInt1) {
/* 407 */         throw new IllegalArgumentException("Invalid loop points : " + paramInt1 + " - " + paramInt2);
/*     */       }
/* 409 */       this.loopstart = paramInt1;
/* 410 */       this.loopend = paramInt2;
/* 411 */       this.loop_sg = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setMicrosecondPosition(long paramLong) {
/* 416 */     setFramePosition((int)(paramLong * (getFormat().getSampleRate() / 1000000.0D)));
/*     */   }
/*     */ 
/*     */   public int available()
/*     */   {
/* 421 */     return 0;
/*     */   }
/*     */ 
/*     */   public void drain() {
/*     */   }
/*     */ 
/*     */   public void flush() {
/*     */   }
/*     */ 
/*     */   public int getBufferSize() {
/* 431 */     return this.bufferSize;
/*     */   }
/*     */ 
/*     */   public AudioFormat getFormat() {
/* 435 */     return this.format;
/*     */   }
/*     */ 
/*     */   public int getFramePosition() {
/* 439 */     synchronized (this.control_mutex) {
/* 440 */       return this.frameposition;
/*     */     }
/*     */   }
/*     */ 
/*     */   public float getLevel() {
/* 445 */     return -1.0F;
/*     */   }
/*     */ 
/*     */   public long getLongFramePosition() {
/* 449 */     return getFramePosition();
/*     */   }
/*     */ 
/*     */   public long getMicrosecondPosition() {
/* 453 */     return ()(getFramePosition() * (1000000.0D / getFormat().getSampleRate()));
/*     */   }
/*     */ 
/*     */   public boolean isActive()
/*     */   {
/* 458 */     synchronized (this.control_mutex) {
/* 459 */       return this.active;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isRunning() {
/* 464 */     synchronized (this.control_mutex) {
/* 465 */       return this.active;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void start()
/*     */   {
/* 471 */     LineEvent localLineEvent = null;
/*     */ 
/* 473 */     synchronized (this.control_mutex) {
/* 474 */       if (isOpen()) {
/* 475 */         if (this.active)
/* 476 */           return;
/* 477 */         this.active = true;
/* 478 */         this.active_sg = true;
/* 479 */         this.loopcount = 0;
/* 480 */         localLineEvent = new LineEvent(this, LineEvent.Type.START, getLongFramePosition());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 485 */     if (localLineEvent != null)
/* 486 */       sendEvent(localLineEvent);
/*     */   }
/*     */ 
/*     */   public void stop() {
/* 490 */     LineEvent localLineEvent = null;
/*     */ 
/* 492 */     synchronized (this.control_mutex) {
/* 493 */       if (isOpen()) {
/* 494 */         if (!this.active)
/* 495 */           return;
/* 496 */         this.active = false;
/* 497 */         this.active_sg = true;
/* 498 */         localLineEvent = new LineEvent(this, LineEvent.Type.STOP, getLongFramePosition());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 503 */     if (localLineEvent != null)
/* 504 */       sendEvent(localLineEvent);
/*     */   }
/*     */ 
/*     */   public void close() {
/* 508 */     LineEvent localLineEvent = null;
/*     */ 
/* 510 */     synchronized (this.control_mutex) {
/* 511 */       if (!isOpen())
/* 512 */         return;
/* 513 */       stop();
/*     */ 
/* 515 */       localLineEvent = new LineEvent(this, LineEvent.Type.CLOSE, getLongFramePosition());
/*     */ 
/* 518 */       this.open = false;
/* 519 */       this.mixer.getMainMixer().closeLine(this);
/*     */     }
/*     */ 
/* 522 */     if (localLineEvent != null)
/* 523 */       sendEvent(localLineEvent);
/*     */   }
/*     */ 
/*     */   public boolean isOpen()
/*     */   {
/* 528 */     return this.open;
/*     */   }
/*     */ 
/*     */   public void open() throws LineUnavailableException {
/* 532 */     if (this.data == null) {
/* 533 */       throw new IllegalArgumentException("Illegal call to open() in interface Clip");
/*     */     }
/*     */ 
/* 536 */     open(this.format, this.data, this.offset, this.bufferSize);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SoftMixingClip
 * JD-Core Version:    0.6.2
 */