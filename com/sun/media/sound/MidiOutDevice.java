/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import javax.sound.midi.MidiMessage;
/*     */ import javax.sound.midi.MidiUnavailableException;
/*     */ import javax.sound.midi.Receiver;
/*     */ import javax.sound.midi.ShortMessage;
/*     */ 
/*     */ final class MidiOutDevice extends AbstractMidiDevice
/*     */ {
/*     */   MidiOutDevice(AbstractMidiDeviceProvider.Info paramInfo)
/*     */   {
/*  44 */     super(paramInfo);
/*     */   }
/*     */ 
/*     */   protected synchronized void implOpen()
/*     */     throws MidiUnavailableException
/*     */   {
/*  53 */     int i = ((AbstractMidiDeviceProvider.Info)getDeviceInfo()).getIndex();
/*  54 */     this.id = nOpen(i);
/*  55 */     if (this.id == 0L)
/*  56 */       throw new MidiUnavailableException("Unable to open native device");
/*     */   }
/*     */ 
/*     */   protected synchronized void implClose()
/*     */   {
/*  65 */     long l = this.id;
/*  66 */     this.id = 0L;
/*     */ 
/*  68 */     super.implClose();
/*     */ 
/*  71 */     nClose(l);
/*     */   }
/*     */ 
/*     */   public long getMicrosecondPosition()
/*     */   {
/*  77 */     long l = -1L;
/*  78 */     if (isOpen()) {
/*  79 */       l = nGetTimeStamp(this.id);
/*     */     }
/*  81 */     return l;
/*     */   }
/*     */ 
/*     */   protected boolean hasReceivers()
/*     */   {
/*  93 */     return true;
/*     */   }
/*     */ 
/*     */   protected Receiver createReceiver()
/*     */   {
/*  98 */     return new MidiOutReceiver(); } 
/*     */   private native long nOpen(int paramInt) throws MidiUnavailableException;
/*     */ 
/*     */   private native void nClose(long paramLong);
/*     */ 
/*     */   private native void nSendShortMessage(long paramLong1, int paramInt, long paramLong2);
/*     */ 
/*     */   private native void nSendLongMessage(long paramLong1, byte[] paramArrayOfByte, int paramInt, long paramLong2);
/*     */ 
/*     */   private native long nGetTimeStamp(long paramLong);
/*     */ 
/* 104 */   final class MidiOutReceiver extends AbstractMidiDevice.AbstractReceiver { MidiOutReceiver() { super(); }
/*     */ 
/*     */     void implSend(MidiMessage paramMidiMessage, long paramLong) {
/* 107 */       int i = paramMidiMessage.getLength();
/* 108 */       int j = paramMidiMessage.getStatus();
/* 109 */       if ((i <= 3) && (j != 240) && (j != 247))
/*     */       {
/*     */         int k;
/*     */         Object localObject;
/* 111 */         if ((paramMidiMessage instanceof ShortMessage)) {
/* 112 */           if ((paramMidiMessage instanceof FastShortMessage)) {
/* 113 */             k = ((FastShortMessage)paramMidiMessage).getPackedMsg();
/*     */           } else {
/* 115 */             localObject = (ShortMessage)paramMidiMessage;
/* 116 */             k = j & 0xFF | (((ShortMessage)localObject).getData1() & 0xFF) << 8 | (((ShortMessage)localObject).getData2() & 0xFF) << 16;
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 121 */           k = 0;
/* 122 */           localObject = paramMidiMessage.getMessage();
/* 123 */           if (i > 0) {
/* 124 */             k = localObject[0] & 0xFF;
/* 125 */             if (i > 1)
/*     */             {
/* 131 */               if (j == 255) {
/* 132 */                 return;
/*     */               }
/* 134 */               k |= (localObject[1] & 0xFF) << 8;
/* 135 */               if (i > 2) {
/* 136 */                 k |= (localObject[2] & 0xFF) << 16;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 141 */         MidiOutDevice.this.nSendShortMessage(MidiOutDevice.this.id, k, paramLong);
/*     */       }
/*     */       else
/*     */       {
/*     */         byte[] arrayOfByte;
/* 144 */         if ((paramMidiMessage instanceof FastSysexMessage))
/* 145 */           arrayOfByte = ((FastSysexMessage)paramMidiMessage).getReadOnlyMessage();
/*     */         else {
/* 147 */           arrayOfByte = paramMidiMessage.getMessage();
/*     */         }
/* 149 */         int m = Math.min(i, arrayOfByte.length);
/* 150 */         if (m > 0)
/* 151 */           MidiOutDevice.this.nSendLongMessage(MidiOutDevice.this.id, arrayOfByte, m, paramLong);
/*     */       }
/*     */     }
/*     */ 
/*     */     synchronized void sendPackedMidiMessage(int paramInt, long paramLong)
/*     */     {
/* 158 */       if ((isOpen()) && (MidiOutDevice.this.id != 0L))
/* 159 */         MidiOutDevice.this.nSendShortMessage(MidiOutDevice.this.id, paramInt, paramLong);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.MidiOutDevice
 * JD-Core Version:    0.6.2
 */