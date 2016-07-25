/*    */ package com.sun.media.sound;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.sound.sampled.AudioInputStream;
/*    */ import javax.sound.sampled.SourceDataLine;
/*    */ 
/*    */ public final class SoftAudioPusher
/*    */   implements Runnable
/*    */ {
/* 39 */   private volatile boolean active = false;
/* 40 */   private SourceDataLine sourceDataLine = null;
/*    */   private Thread audiothread;
/*    */   private final AudioInputStream ais;
/*    */   private final byte[] buffer;
/*    */ 
/*    */   public SoftAudioPusher(SourceDataLine paramSourceDataLine, AudioInputStream paramAudioInputStream, int paramInt)
/*    */   {
/* 47 */     this.ais = paramAudioInputStream;
/* 48 */     this.buffer = new byte[paramInt];
/* 49 */     this.sourceDataLine = paramSourceDataLine;
/*    */   }
/*    */ 
/*    */   public synchronized void start() {
/* 53 */     if (this.active)
/* 54 */       return;
/* 55 */     this.active = true;
/* 56 */     this.audiothread = new Thread(this);
/* 57 */     this.audiothread.setDaemon(true);
/* 58 */     this.audiothread.setPriority(10);
/* 59 */     this.audiothread.start();
/*    */   }
/*    */ 
/*    */   public synchronized void stop() {
/* 63 */     if (!this.active)
/* 64 */       return;
/* 65 */     this.active = false;
/*    */     try {
/* 67 */       this.audiothread.join();
/*    */     }
/*    */     catch (InterruptedException localInterruptedException) {
/*    */     }
/*    */   }
/*    */ 
/*    */   public void run() {
/* 74 */     byte[] arrayOfByte = this.buffer;
/* 75 */     AudioInputStream localAudioInputStream = this.ais;
/* 76 */     SourceDataLine localSourceDataLine = this.sourceDataLine;
/*    */     try
/*    */     {
/* 79 */       while (this.active)
/*    */       {
/* 81 */         int i = localAudioInputStream.read(arrayOfByte);
/* 82 */         if (i < 0)
/*    */           break;
/* 84 */         localSourceDataLine.write(arrayOfByte, 0, i);
/*    */       }
/*    */     } catch (IOException localIOException) {
/* 87 */       this.active = false;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SoftAudioPusher
 * JD-Core Version:    0.6.2
 */