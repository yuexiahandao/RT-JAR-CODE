/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ import javax.sound.sampled.AudioInputStream;
/*     */ import javax.sound.sampled.SourceDataLine;
/*     */ 
/*     */ public final class DataPusher
/*     */   implements Runnable
/*     */ {
/*     */   private static final int AUTO_CLOSE_TIME = 5000;
/*     */   private static final boolean DEBUG = false;
/*     */   private final SourceDataLine source;
/*     */   private final AudioFormat format;
/*  49 */   private AudioInputStream ais = null;
/*     */ 
/*  52 */   private byte[] audioData = null;
/*  53 */   private int audioDataByteLength = 0;
/*     */   private int pos;
/*  55 */   private int newPos = -1;
/*     */   private boolean looping;
/*  58 */   private Thread pushThread = null;
/*     */   private int wantedState;
/*     */   private int threadState;
/*  62 */   private final int STATE_NONE = 0;
/*  63 */   private final int STATE_PLAYING = 1;
/*  64 */   private final int STATE_WAITING = 2;
/*  65 */   private final int STATE_STOPPING = 3;
/*  66 */   private final int STATE_STOPPED = 4;
/*  67 */   private final int BUFFER_SIZE = 16384;
/*     */ 
/*     */   public DataPusher(SourceDataLine paramSourceDataLine, AudioFormat paramAudioFormat, byte[] paramArrayOfByte, int paramInt) {
/*  70 */     this.audioData = paramArrayOfByte;
/*  71 */     this.audioDataByteLength = paramInt;
/*  72 */     this.format = paramAudioFormat;
/*  73 */     this.source = paramSourceDataLine;
/*     */   }
/*     */ 
/*     */   public DataPusher(SourceDataLine paramSourceDataLine, AudioInputStream paramAudioInputStream) {
/*  77 */     this.ais = paramAudioInputStream;
/*  78 */     this.format = paramAudioInputStream.getFormat();
/*  79 */     this.source = paramSourceDataLine;
/*     */   }
/*     */ 
/*     */   public synchronized void start() {
/*  83 */     start(false);
/*     */   }
/*     */ 
/*     */   public synchronized void start(boolean paramBoolean)
/*     */   {
/*     */     try {
/*  89 */       if (this.threadState == 3)
/*     */       {
/*  92 */         stop();
/*     */       }
/*  94 */       this.looping = paramBoolean;
/*  95 */       this.newPos = 0;
/*  96 */       this.wantedState = 1;
/*  97 */       if (!this.source.isOpen())
/*     */       {
/*  99 */         this.source.open(this.format);
/*     */       }
/*     */ 
/* 102 */       this.source.flush();
/*     */ 
/* 104 */       this.source.start();
/* 105 */       if (this.pushThread == null)
/*     */       {
/* 107 */         this.pushThread = JSSecurityManager.createThread(this, null, false, -1, true);
/*     */       }
/*     */ 
/* 113 */       notifyAll();
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void stop()
/*     */   {
/* 123 */     if ((this.threadState == 3) || (this.threadState == 4) || (this.pushThread == null))
/*     */     {
/* 127 */       return;
/*     */     }
/*     */ 
/* 131 */     this.wantedState = 2;
/* 132 */     if (this.source != null)
/*     */     {
/* 134 */       this.source.flush();
/*     */     }
/* 136 */     notifyAll();
/* 137 */     int i = 50;
/* 138 */     while ((i-- >= 0) && (this.threadState == 1))
/*     */       try {
/* 140 */         wait(100L);
/*     */       }
/*     */       catch (InterruptedException localInterruptedException) {
/*     */       }
/*     */   }
/*     */ 
/*     */   synchronized void close() {
/* 147 */     if (this.source != null)
/*     */     {
/* 149 */       this.source.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/* 157 */     byte[] arrayOfByte = null;
/* 158 */     int i = this.ais != null ? 1 : 0;
/* 159 */     if (i != 0)
/* 160 */       arrayOfByte = new byte[16384];
/*     */     else {
/* 162 */       arrayOfByte = this.audioData;
/*     */     }
/* 164 */     while (this.wantedState != 3)
/*     */     {
/* 166 */       if (this.wantedState == 2)
/*     */       {
/*     */         try
/*     */         {
/* 170 */           synchronized (this) {
/* 171 */             this.threadState = 2;
/* 172 */             this.wantedState = 3;
/* 173 */             wait(5000L);
/*     */           }
/*     */         } catch (InterruptedException ) {
/*     */         }
/*     */       }
/*     */       else {
/* 179 */         if (this.newPos >= 0) {
/* 180 */           this.pos = this.newPos;
/* 181 */           this.newPos = -1;
/*     */         }
/* 183 */         this.threadState = 1;
/* 184 */         ??? = 16384;
/* 185 */         if (i != 0) {
/*     */           try {
/* 187 */             this.pos = 0;
/*     */ 
/* 190 */             ??? = this.ais.read(arrayOfByte, 0, arrayOfByte.length);
/*     */           }
/*     */           catch (IOException localIOException) {
/* 193 */             ??? = -1;
/*     */           }
/*     */         } else {
/* 196 */           if (??? > this.audioDataByteLength - this.pos) {
/* 197 */             ??? = this.audioDataByteLength - this.pos;
/*     */           }
/* 199 */           if (??? == 0) {
/* 200 */             ??? = -1;
/*     */           }
/*     */         }
/* 203 */         if (??? < 0)
/*     */         {
/* 205 */           if ((i == 0) && (this.looping))
/*     */           {
/* 207 */             this.pos = 0;
/*     */           }
/*     */           else
/*     */           {
/* 211 */             this.wantedState = 2;
/* 212 */             this.source.drain();
/*     */           }
/*     */         }
/*     */         else {
/* 216 */           int j = this.source.write(arrayOfByte, this.pos, ???);
/* 217 */           this.pos += j;
/*     */         }
/*     */       }
/*     */     }
/* 220 */     this.threadState = 3;
/*     */ 
/* 223 */     this.source.flush();
/*     */ 
/* 225 */     this.source.stop();
/*     */ 
/* 227 */     this.source.flush();
/*     */ 
/* 229 */     this.source.close();
/* 230 */     this.threadState = 4;
/* 231 */     synchronized (this) {
/* 232 */       this.pushThread = null;
/* 233 */       notifyAll();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.DataPusher
 * JD-Core Version:    0.6.2
 */