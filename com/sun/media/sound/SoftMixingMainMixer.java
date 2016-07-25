/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ import javax.sound.sampled.AudioInputStream;
/*     */ 
/*     */ public final class SoftMixingMainMixer
/*     */ {
/*     */   public static final int CHANNEL_LEFT = 0;
/*     */   public static final int CHANNEL_RIGHT = 1;
/*     */   public static final int CHANNEL_EFFECT1 = 2;
/*     */   public static final int CHANNEL_EFFECT2 = 3;
/*     */   public static final int CHANNEL_EFFECT3 = 4;
/*     */   public static final int CHANNEL_EFFECT4 = 5;
/*     */   public static final int CHANNEL_LEFT_DRY = 10;
/*     */   public static final int CHANNEL_RIGHT_DRY = 11;
/*     */   public static final int CHANNEL_SCRATCH1 = 12;
/*     */   public static final int CHANNEL_SCRATCH2 = 13;
/*     */   public static final int CHANNEL_CHANNELMIXER_LEFT = 14;
/*     */   public static final int CHANNEL_CHANNELMIXER_RIGHT = 15;
/*     */   private final SoftMixingMixer mixer;
/*     */   private final AudioInputStream ais;
/*     */   private final SoftAudioBuffer[] buffers;
/*     */   private final SoftAudioProcessor reverb;
/*     */   private final SoftAudioProcessor chorus;
/*     */   private final SoftAudioProcessor agc;
/*     */   private final int nrofchannels;
/*     */   private final Object control_mutex;
/*  82 */   private final List<SoftMixingDataLine> openLinesList = new ArrayList();
/*     */ 
/*  84 */   private SoftMixingDataLine[] openLines = new SoftMixingDataLine[0];
/*     */ 
/*     */   public AudioInputStream getInputStream() {
/*  87 */     return this.ais;
/*     */   }
/*     */ 
/*     */   void processAudioBuffers() {
/*  91 */     for (int i = 0; i < this.buffers.length; i++)
/*  92 */       this.buffers[i].clear();
/*     */     SoftMixingDataLine[] arrayOfSoftMixingDataLine;
/*  96 */     synchronized (this.control_mutex) {
/*  97 */       arrayOfSoftMixingDataLine = this.openLines;
/*  98 */       for (int k = 0; k < arrayOfSoftMixingDataLine.length; k++) {
/*  99 */         arrayOfSoftMixingDataLine[k].processControlLogic();
/*     */       }
/* 101 */       this.chorus.processControlLogic();
/* 102 */       this.reverb.processControlLogic();
/* 103 */       this.agc.processControlLogic();
/*     */     }
/* 105 */     for (int j = 0; j < arrayOfSoftMixingDataLine.length; j++) {
/* 106 */       arrayOfSoftMixingDataLine[j].processAudioLogic(this.buffers);
/*     */     }
/*     */ 
/* 109 */     this.chorus.processAudio();
/* 110 */     this.reverb.processAudio();
/*     */ 
/* 112 */     this.agc.processAudio();
/*     */   }
/*     */ 
/*     */   public SoftMixingMainMixer(SoftMixingMixer paramSoftMixingMixer)
/*     */   {
/* 117 */     this.mixer = paramSoftMixingMixer;
/*     */ 
/* 119 */     this.nrofchannels = paramSoftMixingMixer.getFormat().getChannels();
/*     */ 
/* 121 */     int i = (int)(paramSoftMixingMixer.getFormat().getSampleRate() / paramSoftMixingMixer.getControlRate());
/*     */ 
/* 124 */     this.control_mutex = paramSoftMixingMixer.control_mutex;
/* 125 */     this.buffers = new SoftAudioBuffer[16];
/* 126 */     for (int j = 0; j < this.buffers.length; j++) {
/* 127 */       this.buffers[j] = new SoftAudioBuffer(i, paramSoftMixingMixer.getFormat());
/*     */     }
/*     */ 
/* 131 */     this.reverb = new SoftReverb();
/* 132 */     this.chorus = new SoftChorus();
/* 133 */     this.agc = new SoftLimiter();
/*     */ 
/* 135 */     float f1 = paramSoftMixingMixer.getFormat().getSampleRate();
/* 136 */     float f2 = paramSoftMixingMixer.getControlRate();
/* 137 */     this.reverb.init(f1, f2);
/* 138 */     this.chorus.init(f1, f2);
/* 139 */     this.agc.init(f1, f2);
/*     */ 
/* 141 */     this.reverb.setMixMode(true);
/* 142 */     this.chorus.setMixMode(true);
/* 143 */     this.agc.setMixMode(false);
/*     */ 
/* 145 */     this.chorus.setInput(0, this.buffers[3]);
/* 146 */     this.chorus.setOutput(0, this.buffers[0]);
/* 147 */     if (this.nrofchannels != 1)
/* 148 */       this.chorus.setOutput(1, this.buffers[1]);
/* 149 */     this.chorus.setOutput(2, this.buffers[2]);
/*     */ 
/* 151 */     this.reverb.setInput(0, this.buffers[2]);
/* 152 */     this.reverb.setOutput(0, this.buffers[0]);
/* 153 */     if (this.nrofchannels != 1) {
/* 154 */       this.reverb.setOutput(1, this.buffers[1]);
/*     */     }
/* 156 */     this.agc.setInput(0, this.buffers[0]);
/* 157 */     if (this.nrofchannels != 1)
/* 158 */       this.agc.setInput(1, this.buffers[1]);
/* 159 */     this.agc.setOutput(0, this.buffers[0]);
/* 160 */     if (this.nrofchannels != 1) {
/* 161 */       this.agc.setOutput(1, this.buffers[1]);
/*     */     }
/* 163 */     InputStream local1 = new InputStream()
/*     */     {
/* 165 */       private final SoftAudioBuffer[] buffers = SoftMixingMainMixer.this.buffers;
/*     */ 
/* 167 */       private final int nrofchannels = SoftMixingMainMixer.this.mixer.getFormat().getChannels();
/*     */ 
/* 170 */       private final int buffersize = this.buffers[0].getSize();
/*     */ 
/* 172 */       private final byte[] bbuffer = new byte[this.buffersize * (SoftMixingMainMixer.this.mixer.getFormat().getSampleSizeInBits() / 8) * this.nrofchannels];
/*     */ 
/* 176 */       private int bbuffer_pos = 0;
/*     */ 
/* 178 */       private final byte[] single = new byte[1];
/*     */ 
/*     */       public void fillBuffer() {
/* 181 */         SoftMixingMainMixer.this.processAudioBuffers();
/* 182 */         for (int i = 0; i < this.nrofchannels; i++)
/* 183 */           this.buffers[i].get(this.bbuffer, i);
/* 184 */         this.bbuffer_pos = 0;
/*     */       }
/*     */ 
/*     */       public int read(byte[] paramAnonymousArrayOfByte, int paramAnonymousInt1, int paramAnonymousInt2) {
/* 188 */         int i = this.bbuffer.length;
/* 189 */         int j = paramAnonymousInt1 + paramAnonymousInt2;
/* 190 */         byte[] arrayOfByte = this.bbuffer;
/* 191 */         while (paramAnonymousInt1 < j)
/* 192 */           if (available() == 0) {
/* 193 */             fillBuffer();
/*     */           } else {
/* 195 */             int k = this.bbuffer_pos;
/* 196 */             while ((paramAnonymousInt1 < j) && (k < i))
/* 197 */               paramAnonymousArrayOfByte[(paramAnonymousInt1++)] = arrayOfByte[(k++)];
/* 198 */             this.bbuffer_pos = k;
/*     */           }
/* 200 */         return paramAnonymousInt2;
/*     */       }
/*     */ 
/*     */       public int read() throws IOException {
/* 204 */         int i = read(this.single);
/* 205 */         if (i == -1)
/* 206 */           return -1;
/* 207 */         return this.single[0] & 0xFF;
/*     */       }
/*     */ 
/*     */       public int available() {
/* 211 */         return this.bbuffer.length - this.bbuffer_pos;
/*     */       }
/*     */ 
/*     */       public void close() {
/* 215 */         SoftMixingMainMixer.this.mixer.close();
/*     */       }
/*     */     };
/* 220 */     this.ais = new AudioInputStream(local1, paramSoftMixingMixer.getFormat(), -1L);
/*     */   }
/*     */ 
/*     */   public void openLine(SoftMixingDataLine paramSoftMixingDataLine)
/*     */   {
/* 226 */     synchronized (this.control_mutex) {
/* 227 */       this.openLinesList.add(paramSoftMixingDataLine);
/* 228 */       this.openLines = ((SoftMixingDataLine[])this.openLinesList.toArray(new SoftMixingDataLine[this.openLinesList.size()]));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void closeLine(SoftMixingDataLine paramSoftMixingDataLine)
/*     */   {
/* 234 */     synchronized (this.control_mutex) {
/* 235 */       this.openLinesList.remove(paramSoftMixingDataLine);
/* 236 */       this.openLines = ((SoftMixingDataLine[])this.openLinesList.toArray(new SoftMixingDataLine[this.openLinesList.size()]));
/*     */ 
/* 238 */       if ((this.openLines.length == 0) && 
/* 239 */         (this.mixer.implicitOpen))
/* 240 */         this.mixer.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public SoftMixingDataLine[] getOpenLines()
/*     */   {
/* 246 */     synchronized (this.control_mutex) {
/* 247 */       return this.openLines;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 253 */     SoftMixingDataLine[] arrayOfSoftMixingDataLine = this.openLines;
/* 254 */     for (int i = 0; i < arrayOfSoftMixingDataLine.length; i++)
/* 255 */       arrayOfSoftMixingDataLine[i].close();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SoftMixingMainMixer
 * JD-Core Version:    0.6.2
 */