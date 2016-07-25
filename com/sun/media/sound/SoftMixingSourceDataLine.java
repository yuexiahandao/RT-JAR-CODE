/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ import javax.sound.sampled.AudioInputStream;
/*     */ import javax.sound.sampled.DataLine.Info;
/*     */ import javax.sound.sampled.LineEvent;
/*     */ import javax.sound.sampled.LineEvent.Type;
/*     */ import javax.sound.sampled.LineUnavailableException;
/*     */ import javax.sound.sampled.SourceDataLine;
/*     */ 
/*     */ public final class SoftMixingSourceDataLine extends SoftMixingDataLine
/*     */   implements SourceDataLine
/*     */ {
/*  47 */   private boolean open = false;
/*     */ 
/*  49 */   private AudioFormat format = new AudioFormat(44100.0F, 16, 2, true, false);
/*     */   private int framesize;
/*  53 */   private int bufferSize = -1;
/*     */   private float[] readbuffer;
/*  57 */   private boolean active = false;
/*     */   private byte[] cycling_buffer;
/*  61 */   private int cycling_read_pos = 0;
/*     */ 
/*  63 */   private int cycling_write_pos = 0;
/*     */ 
/*  65 */   private int cycling_avail = 0;
/*     */ 
/*  67 */   private long cycling_framepos = 0L;
/*     */   private AudioFloatInputStream afis;
/* 189 */   private boolean _active = false;
/*     */   private AudioFormat outputformat;
/*     */   private int out_nrofchannels;
/*     */   private int in_nrofchannels;
/*     */   private float _rightgain;
/*     */   private float _leftgain;
/*     */   private float _eff1gain;
/*     */   private float _eff2gain;
/*     */ 
/*     */   SoftMixingSourceDataLine(SoftMixingMixer paramSoftMixingMixer, DataLine.Info paramInfo)
/*     */   {
/* 124 */     super(paramSoftMixingMixer, paramInfo);
/*     */   }
/*     */ 
/*     */   public int write(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/* 128 */     if (!isOpen())
/* 129 */       return 0;
/* 130 */     if (paramInt2 % this.framesize != 0) {
/* 131 */       throw new IllegalArgumentException("Number of bytes does not represent an integral number of sample frames.");
/*     */     }
/* 133 */     if (paramInt1 < 0) {
/* 134 */       throw new ArrayIndexOutOfBoundsException(paramInt1);
/*     */     }
/* 136 */     if (paramInt1 + paramInt2 > paramArrayOfByte.length) {
/* 137 */       throw new ArrayIndexOutOfBoundsException(paramArrayOfByte.length);
/*     */     }
/*     */ 
/* 140 */     byte[] arrayOfByte = this.cycling_buffer;
/* 141 */     int i = this.cycling_buffer.length;
/*     */ 
/* 143 */     int j = 0;
/* 144 */     while (j != paramInt2)
/*     */     {
/*     */       int k;
/* 146 */       synchronized (this.cycling_buffer) {
/* 147 */         int m = this.cycling_write_pos;
/* 148 */         k = this.cycling_avail;
/* 149 */         while ((j != paramInt2) && 
/* 150 */           (k != i))
/*     */         {
/* 152 */           arrayOfByte[(m++)] = paramArrayOfByte[(paramInt1++)];
/* 153 */           j++;
/* 154 */           k++;
/* 155 */           if (m == i)
/* 156 */             m = 0;
/*     */         }
/* 158 */         this.cycling_avail = k;
/* 159 */         this.cycling_write_pos = m;
/* 160 */         if (j == paramInt2)
/* 161 */           return j;
/*     */       }
/* 163 */       if (k == i) {
/*     */         try {
/* 165 */           Thread.sleep(1L);
/*     */         } catch (InterruptedException localInterruptedException) {
/* 167 */           return j;
/*     */         }
/* 169 */         if (!isRunning()) {
/* 170 */           return j;
/*     */         }
/*     */       }
/*     */     }
/* 174 */     return j;
/*     */   }
/*     */ 
/*     */   protected void processControlLogic()
/*     */   {
/* 206 */     this._active = this.active;
/* 207 */     this._rightgain = this.rightgain;
/* 208 */     this._leftgain = this.leftgain;
/* 209 */     this._eff1gain = this.eff1gain;
/* 210 */     this._eff2gain = this.eff2gain;
/*     */   }
/*     */ 
/*     */   protected void processAudioLogic(SoftAudioBuffer[] paramArrayOfSoftAudioBuffer) {
/* 214 */     if (this._active) {
/* 215 */       float[] arrayOfFloat1 = paramArrayOfSoftAudioBuffer[0].array();
/* 216 */       float[] arrayOfFloat2 = paramArrayOfSoftAudioBuffer[1].array();
/* 217 */       int i = paramArrayOfSoftAudioBuffer[0].getSize();
/*     */ 
/* 219 */       int j = i * this.in_nrofchannels;
/* 220 */       if ((this.readbuffer == null) || (this.readbuffer.length < j)) {
/* 221 */         this.readbuffer = new float[j];
/*     */       }
/* 223 */       int k = 0;
/*     */       try {
/* 225 */         k = this.afis.read(this.readbuffer);
/* 226 */         if (k != this.in_nrofchannels)
/* 227 */           Arrays.fill(this.readbuffer, k, j, 0.0F);
/*     */       }
/*     */       catch (IOException localIOException) {
/*     */       }
/* 231 */       int m = this.in_nrofchannels;
/* 232 */       int n = 0; for (int i1 = 0; n < i; i1 += m) {
/* 233 */         arrayOfFloat1[n] += this.readbuffer[i1] * this._leftgain;
/*     */ 
/* 232 */         n++;
/*     */       }
/*     */ 
/* 235 */       if (this.out_nrofchannels != 1)
/* 236 */         if (this.in_nrofchannels == 1) {
/* 237 */           n = 0; for (i1 = 0; n < i; i1 += m) {
/* 238 */             arrayOfFloat2[n] += this.readbuffer[i1] * this._rightgain;
/*     */ 
/* 237 */             n++;
/*     */           }
/*     */         }
/*     */         else {
/* 241 */           n = 0; for (i1 = 1; n < i; i1 += m) {
/* 242 */             arrayOfFloat2[n] += this.readbuffer[i1] * this._rightgain;
/*     */ 
/* 241 */             n++;
/*     */           }
/*     */         }
/*     */       float[] arrayOfFloat3;
/*     */       int i2;
/* 248 */       if (this._eff1gain > 0.0001D) {
/* 249 */         arrayOfFloat3 = paramArrayOfSoftAudioBuffer[2].array();
/*     */ 
/* 251 */         i1 = 0; for (i2 = 0; i1 < i; i2 += m) {
/* 252 */           arrayOfFloat3[i1] += this.readbuffer[i2] * this._eff1gain;
/*     */ 
/* 251 */           i1++;
/*     */         }
/*     */ 
/* 254 */         if (this.in_nrofchannels == 2) {
/* 255 */           i1 = 0; for (i2 = 1; i1 < i; i2 += m) {
/* 256 */             arrayOfFloat3[i1] += this.readbuffer[i2] * this._eff1gain;
/*     */ 
/* 255 */             i1++;
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 261 */       if (this._eff2gain > 0.0001D) {
/* 262 */         arrayOfFloat3 = paramArrayOfSoftAudioBuffer[3].array();
/*     */ 
/* 264 */         i1 = 0; for (i2 = 0; i1 < i; i2 += m) {
/* 265 */           arrayOfFloat3[i1] += this.readbuffer[i2] * this._eff2gain;
/*     */ 
/* 264 */           i1++;
/*     */         }
/*     */ 
/* 267 */         if (this.in_nrofchannels == 2) {
/* 268 */           i1 = 0; for (i2 = 1; i1 < i; i2 += m) {
/* 269 */             arrayOfFloat3[i1] += this.readbuffer[i2] * this._eff2gain;
/*     */ 
/* 268 */             i1++;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void open()
/*     */     throws LineUnavailableException
/*     */   {
/* 278 */     open(this.format);
/*     */   }
/*     */ 
/*     */   public void open(AudioFormat paramAudioFormat) throws LineUnavailableException {
/* 282 */     if (this.bufferSize == -1) {
/* 283 */       this.bufferSize = ((int)(paramAudioFormat.getFrameRate() / 2.0F) * paramAudioFormat.getFrameSize());
/*     */     }
/* 285 */     open(paramAudioFormat, this.bufferSize);
/*     */   }
/*     */ 
/*     */   public void open(AudioFormat paramAudioFormat, int paramInt)
/*     */     throws LineUnavailableException
/*     */   {
/* 291 */     LineEvent localLineEvent = null;
/*     */ 
/* 293 */     if (paramInt < paramAudioFormat.getFrameSize() * 32) {
/* 294 */       paramInt = paramAudioFormat.getFrameSize() * 32;
/*     */     }
/* 296 */     synchronized (this.control_mutex)
/*     */     {
/* 298 */       if (!isOpen()) {
/* 299 */         if (!this.mixer.isOpen()) {
/* 300 */           this.mixer.open();
/* 301 */           this.mixer.implicitOpen = true;
/*     */         }
/*     */ 
/* 304 */         localLineEvent = new LineEvent(this, LineEvent.Type.OPEN, 0L);
/*     */ 
/* 306 */         this.bufferSize = (paramInt - paramInt % paramAudioFormat.getFrameSize());
/*     */ 
/* 308 */         this.format = paramAudioFormat;
/* 309 */         this.framesize = paramAudioFormat.getFrameSize();
/* 310 */         this.outputformat = this.mixer.getFormat();
/* 311 */         this.out_nrofchannels = this.outputformat.getChannels();
/* 312 */         this.in_nrofchannels = paramAudioFormat.getChannels();
/*     */ 
/* 314 */         this.open = true;
/*     */ 
/* 316 */         this.mixer.getMainMixer().openLine(this);
/*     */ 
/* 318 */         this.cycling_buffer = new byte[this.framesize * paramInt];
/* 319 */         this.cycling_read_pos = 0;
/* 320 */         this.cycling_write_pos = 0;
/* 321 */         this.cycling_avail = 0;
/* 322 */         this.cycling_framepos = 0L;
/*     */ 
/* 324 */         InputStream local1 = new InputStream()
/*     */         {
/*     */           public int read() throws IOException {
/* 327 */             byte[] arrayOfByte = new byte[1];
/* 328 */             int i = read(arrayOfByte);
/* 329 */             if (i < 0)
/* 330 */               return i;
/* 331 */             return arrayOfByte[0] & 0xFF;
/*     */           }
/*     */ 
/*     */           public int available() throws IOException {
/* 335 */             synchronized (SoftMixingSourceDataLine.this.cycling_buffer) {
/* 336 */               return SoftMixingSourceDataLine.this.cycling_avail;
/*     */             }
/*     */           }
/*     */ 
/*     */           public int read(byte[] paramAnonymousArrayOfByte, int paramAnonymousInt1, int paramAnonymousInt2)
/*     */             throws IOException
/*     */           {
/* 343 */             synchronized (SoftMixingSourceDataLine.this.cycling_buffer) {
/* 344 */               if (paramAnonymousInt2 > SoftMixingSourceDataLine.this.cycling_avail)
/* 345 */                 paramAnonymousInt2 = SoftMixingSourceDataLine.this.cycling_avail;
/* 346 */               int i = SoftMixingSourceDataLine.this.cycling_read_pos;
/* 347 */               byte[] arrayOfByte = SoftMixingSourceDataLine.this.cycling_buffer;
/* 348 */               int j = arrayOfByte.length;
/* 349 */               for (int k = 0; k < paramAnonymousInt2; k++) {
/* 350 */                 paramAnonymousArrayOfByte[(paramAnonymousInt1++)] = arrayOfByte[i];
/* 351 */                 i++;
/* 352 */                 if (i == j)
/* 353 */                   i = 0;
/*     */               }
/* 355 */               SoftMixingSourceDataLine.this.cycling_read_pos = i;
/* 356 */               SoftMixingSourceDataLine.access$120(SoftMixingSourceDataLine.this, paramAnonymousInt2);
/* 357 */               SoftMixingSourceDataLine.access$314(SoftMixingSourceDataLine.this, paramAnonymousInt2 / SoftMixingSourceDataLine.this.framesize);
/*     */             }
/* 359 */             return paramAnonymousInt2;
/*     */           }
/*     */         };
/* 364 */         this.afis = AudioFloatInputStream.getInputStream(new AudioInputStream(local1, paramAudioFormat, -1L));
/*     */ 
/* 368 */         this.afis = new NonBlockingFloatInputStream(this.afis);
/*     */ 
/* 370 */         if (Math.abs(paramAudioFormat.getSampleRate() - this.outputformat.getSampleRate()) > 1.0E-006D)
/*     */         {
/* 372 */           this.afis = new SoftMixingDataLine.AudioFloatInputStreamResampler(this.afis, this.outputformat);
/*     */         }
/*     */ 
/*     */       }
/* 376 */       else if (!paramAudioFormat.matches(getFormat())) {
/* 377 */         throw new IllegalStateException("Line is already open with format " + getFormat() + " and bufferSize " + getBufferSize());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 385 */     if (localLineEvent != null)
/* 386 */       sendEvent(localLineEvent);
/*     */   }
/*     */ 
/*     */   public int available()
/*     */   {
/* 391 */     synchronized (this.cycling_buffer) {
/* 392 */       return this.cycling_buffer.length - this.cycling_avail;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void drain()
/*     */   {
/*     */     while (true)
/*     */     {
/*     */       int i;
/* 399 */       synchronized (this.cycling_buffer) {
/* 400 */         i = this.cycling_avail;
/*     */       }
/* 402 */       if (i != 0)
/* 403 */         return;
/*     */       try {
/* 405 */         Thread.sleep(1L);
/*     */       } catch (InterruptedException localInterruptedException) {
/* 407 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void flush() {
/* 413 */     synchronized (this.cycling_buffer) {
/* 414 */       this.cycling_read_pos = 0;
/* 415 */       this.cycling_write_pos = 0;
/* 416 */       this.cycling_avail = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getBufferSize() {
/* 421 */     synchronized (this.control_mutex) {
/* 422 */       return this.bufferSize;
/*     */     }
/*     */   }
/*     */ 
/*     */   public AudioFormat getFormat() {
/* 427 */     synchronized (this.control_mutex) {
/* 428 */       return this.format;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getFramePosition() {
/* 433 */     return (int)getLongFramePosition();
/*     */   }
/*     */ 
/*     */   public float getLevel() {
/* 437 */     return -1.0F;
/*     */   }
/*     */ 
/*     */   public long getLongFramePosition() {
/* 441 */     synchronized (this.cycling_buffer) {
/* 442 */       return this.cycling_framepos;
/*     */     }
/*     */   }
/*     */ 
/*     */   public long getMicrosecondPosition() {
/* 447 */     return ()(getLongFramePosition() * (1000000.0D / getFormat().getSampleRate()));
/*     */   }
/*     */ 
/*     */   public boolean isActive()
/*     */   {
/* 452 */     synchronized (this.control_mutex) {
/* 453 */       return this.active;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isRunning() {
/* 458 */     synchronized (this.control_mutex) {
/* 459 */       return this.active;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void start()
/*     */   {
/* 465 */     LineEvent localLineEvent = null;
/*     */ 
/* 467 */     synchronized (this.control_mutex) {
/* 468 */       if (isOpen()) {
/* 469 */         if (this.active)
/* 470 */           return;
/* 471 */         this.active = true;
/* 472 */         localLineEvent = new LineEvent(this, LineEvent.Type.START, getLongFramePosition());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 477 */     if (localLineEvent != null)
/* 478 */       sendEvent(localLineEvent);
/*     */   }
/*     */ 
/*     */   public void stop() {
/* 482 */     LineEvent localLineEvent = null;
/*     */ 
/* 484 */     synchronized (this.control_mutex) {
/* 485 */       if (isOpen()) {
/* 486 */         if (!this.active)
/* 487 */           return;
/* 488 */         this.active = false;
/* 489 */         localLineEvent = new LineEvent(this, LineEvent.Type.STOP, getLongFramePosition());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 494 */     if (localLineEvent != null)
/* 495 */       sendEvent(localLineEvent);
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 500 */     LineEvent localLineEvent = null;
/*     */ 
/* 502 */     synchronized (this.control_mutex) {
/* 503 */       if (!isOpen())
/* 504 */         return;
/* 505 */       stop();
/*     */ 
/* 507 */       localLineEvent = new LineEvent(this, LineEvent.Type.CLOSE, getLongFramePosition());
/*     */ 
/* 510 */       this.open = false;
/* 511 */       this.mixer.getMainMixer().closeLine(this);
/*     */     }
/*     */ 
/* 514 */     if (localLineEvent != null)
/* 515 */       sendEvent(localLineEvent);
/*     */   }
/*     */ 
/*     */   public boolean isOpen()
/*     */   {
/* 520 */     synchronized (this.control_mutex) {
/* 521 */       return this.open;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class NonBlockingFloatInputStream extends AudioFloatInputStream
/*     */   {
/*     */     AudioFloatInputStream ais;
/*     */ 
/*     */     NonBlockingFloatInputStream(AudioFloatInputStream paramAudioFloatInputStream)
/*     */     {
/*  76 */       this.ais = paramAudioFloatInputStream;
/*     */     }
/*     */ 
/*     */     public int available() throws IOException {
/*  80 */       return this.ais.available();
/*     */     }
/*     */ 
/*     */     public void close() throws IOException {
/*  84 */       this.ais.close();
/*     */     }
/*     */ 
/*     */     public AudioFormat getFormat() {
/*  88 */       return this.ais.getFormat();
/*     */     }
/*     */ 
/*     */     public long getFrameLength() {
/*  92 */       return this.ais.getFrameLength();
/*     */     }
/*     */ 
/*     */     public void mark(int paramInt) {
/*  96 */       this.ais.mark(paramInt);
/*     */     }
/*     */ 
/*     */     public boolean markSupported() {
/* 100 */       return this.ais.markSupported();
/*     */     }
/*     */ 
/*     */     public int read(float[] paramArrayOfFloat, int paramInt1, int paramInt2) throws IOException {
/* 104 */       int i = available();
/* 105 */       if (paramInt2 > i) {
/* 106 */         int j = this.ais.read(paramArrayOfFloat, paramInt1, i);
/* 107 */         Arrays.fill(paramArrayOfFloat, paramInt1 + j, paramInt1 + paramInt2, 0.0F);
/* 108 */         return paramInt2;
/*     */       }
/* 110 */       return this.ais.read(paramArrayOfFloat, paramInt1, paramInt2);
/*     */     }
/*     */ 
/*     */     public void reset() throws IOException {
/* 114 */       this.ais.reset();
/*     */     }
/*     */ 
/*     */     public long skip(long paramLong) throws IOException {
/* 118 */       return this.ais.skip(paramLong);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SoftMixingSourceDataLine
 * JD-Core Version:    0.6.2
 */