/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import javax.sound.midi.MidiUnavailableException;
/*     */ import javax.sound.midi.Transmitter;
/*     */ 
/*     */ final class MidiInDevice extends AbstractMidiDevice
/*     */   implements Runnable
/*     */ {
/*  41 */   private Thread midiInThread = null;
/*     */ 
/*     */   MidiInDevice(AbstractMidiDeviceProvider.Info paramInfo)
/*     */   {
/*  46 */     super(paramInfo);
/*     */   }
/*     */ 
/*     */   protected synchronized void implOpen()
/*     */     throws MidiUnavailableException
/*     */   {
/*  58 */     int i = ((MidiInDeviceProvider.MidiInDeviceInfo)getDeviceInfo()).getIndex();
/*  59 */     this.id = nOpen(i);
/*     */ 
/*  61 */     if (this.id == 0L) {
/*  62 */       throw new MidiUnavailableException("Unable to open native device");
/*     */     }
/*     */ 
/*  66 */     if (this.midiInThread == null) {
/*  67 */       this.midiInThread = JSSecurityManager.createThread(this, "Java Sound MidiInDevice Thread", false, -1, true);
/*     */     }
/*     */ 
/*  74 */     nStart(this.id);
/*     */   }
/*     */ 
/*     */   protected synchronized void implClose()
/*     */   {
/*  83 */     long l = this.id;
/*  84 */     this.id = 0L;
/*     */ 
/*  86 */     super.implClose();
/*     */ 
/*  89 */     nStop(l);
/*  90 */     if (this.midiInThread != null)
/*     */       try {
/*  92 */         this.midiInThread.join(1000L);
/*     */       }
/*     */       catch (InterruptedException localInterruptedException)
/*     */       {
/*     */       }
/*  97 */     nClose(l);
/*     */   }
/*     */ 
/*     */   public long getMicrosecondPosition()
/*     */   {
/* 103 */     long l = -1L;
/* 104 */     if (isOpen()) {
/* 105 */       l = nGetTimeStamp(this.id);
/*     */     }
/* 107 */     return l;
/*     */   }
/*     */ 
/*     */   protected boolean hasTransmitters()
/*     */   {
/* 115 */     return true;
/*     */   }
/*     */ 
/*     */   protected Transmitter createTransmitter()
/*     */   {
/* 120 */     return new MidiInTransmitter(null);
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/* 138 */     while (this.id != 0L)
/*     */     {
/* 140 */       nGetMessages(this.id);
/* 141 */       if (this.id != 0L)
/*     */         try {
/* 143 */           Thread.sleep(1L);
/*     */         }
/*     */         catch (InterruptedException localInterruptedException)
/*     */         {
/*     */         }
/*     */     }
/* 149 */     this.midiInThread = null;
/*     */   }
/*     */ 
/*     */   void callbackShortMessage(int paramInt, long paramLong)
/*     */   {
/* 161 */     if ((paramInt == 0) || (this.id == 0L)) {
/* 162 */       return;
/*     */     }
/*     */ 
/* 172 */     getTransmitterList().sendMessage(paramInt, paramLong);
/*     */   }
/*     */ 
/*     */   void callbackLongMessage(byte[] paramArrayOfByte, long paramLong) {
/* 176 */     if ((this.id == 0L) || (paramArrayOfByte == null)) {
/* 177 */       return;
/*     */     }
/* 179 */     getTransmitterList().sendMessage(paramArrayOfByte, paramLong);
/*     */   }
/*     */ 
/*     */   private native long nOpen(int paramInt)
/*     */     throws MidiUnavailableException;
/*     */ 
/*     */   private native void nClose(long paramLong);
/*     */ 
/*     */   private native void nStart(long paramLong)
/*     */     throws MidiUnavailableException;
/*     */ 
/*     */   private native void nStop(long paramLong);
/*     */ 
/*     */   private native long nGetTimeStamp(long paramLong);
/*     */ 
/*     */   private native void nGetMessages(long paramLong);
/*     */ 
/*     */   private final class MidiInTransmitter extends AbstractMidiDevice.BasicTransmitter
/*     */   {
/*     */     private MidiInTransmitter()
/*     */     {
/* 129 */       super();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.MidiInDevice
 * JD-Core Version:    0.6.2
 */