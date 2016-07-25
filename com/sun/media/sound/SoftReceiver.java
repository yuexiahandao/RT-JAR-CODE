/*    */ package com.sun.media.sound;
/*    */ 
/*    */ import java.util.TreeMap;
/*    */ import javax.sound.midi.MidiDevice;
/*    */ import javax.sound.midi.MidiDeviceReceiver;
/*    */ import javax.sound.midi.MidiMessage;
/*    */ import javax.sound.midi.ShortMessage;
/*    */ 
/*    */ public final class SoftReceiver
/*    */   implements MidiDeviceReceiver
/*    */ {
/* 41 */   boolean open = true;
/*    */   private final Object control_mutex;
/*    */   private final SoftSynthesizer synth;
/*    */   TreeMap<Long, Object> midimessages;
/*    */   SoftMainMixer mainmixer;
/*    */ 
/*    */   public SoftReceiver(SoftSynthesizer paramSoftSynthesizer)
/*    */   {
/* 48 */     this.control_mutex = paramSoftSynthesizer.control_mutex;
/* 49 */     this.synth = paramSoftSynthesizer;
/* 50 */     this.mainmixer = paramSoftSynthesizer.getMainMixer();
/* 51 */     if (this.mainmixer != null)
/* 52 */       this.midimessages = this.mainmixer.midimessages;
/*    */   }
/*    */ 
/*    */   public MidiDevice getMidiDevice() {
/* 56 */     return this.synth;
/*    */   }
/*    */ 
/*    */   public void send(MidiMessage paramMidiMessage, long paramLong)
/*    */   {
/* 61 */     synchronized (this.control_mutex) {
/* 62 */       if (!this.open) {
/* 63 */         throw new IllegalStateException("Receiver is not open");
/*    */       }
/*    */     }
/* 66 */     if (paramLong != -1L) {
/* 67 */       synchronized (this.control_mutex) {
/* 68 */         this.mainmixer.activity();
/* 69 */         while (this.midimessages.get(Long.valueOf(paramLong)) != null)
/* 70 */           paramLong += 1L;
/* 71 */         if (((paramMidiMessage instanceof ShortMessage)) && (((ShortMessage)paramMidiMessage).getChannel() > 15))
/*    */         {
/* 73 */           this.midimessages.put(Long.valueOf(paramLong), paramMidiMessage.clone());
/*    */         }
/* 75 */         else this.midimessages.put(Long.valueOf(paramLong), paramMidiMessage.getMessage());
/*    */       }
/*    */     }
/*    */     else
/* 79 */       this.mainmixer.processMessage(paramMidiMessage);
/*    */   }
/*    */ 
/*    */   public void close()
/*    */   {
/* 84 */     synchronized (this.control_mutex) {
/* 85 */       this.open = false;
/*    */     }
/* 87 */     this.synth.removeReceiver(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SoftReceiver
 * JD-Core Version:    0.6.2
 */