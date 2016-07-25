/*    */ package com.sun.media.sound;
/*    */ 
/*    */ import javax.sound.midi.MidiDevice;
/*    */ import javax.sound.midi.MidiDeviceReceiver;
/*    */ import javax.sound.midi.MidiMessage;
/*    */ import javax.sound.midi.Receiver;
/*    */ 
/*    */ public final class MidiDeviceReceiverEnvelope
/*    */   implements MidiDeviceReceiver
/*    */ {
/*    */   private final MidiDevice device;
/*    */   private final Receiver receiver;
/*    */ 
/*    */   public MidiDeviceReceiverEnvelope(MidiDevice paramMidiDevice, Receiver paramReceiver)
/*    */   {
/* 50 */     if ((paramMidiDevice == null) || (paramReceiver == null)) {
/* 51 */       throw new NullPointerException();
/*    */     }
/* 53 */     this.device = paramMidiDevice;
/* 54 */     this.receiver = paramReceiver;
/*    */   }
/*    */ 
/*    */   public void close()
/*    */   {
/* 59 */     this.receiver.close();
/*    */   }
/*    */ 
/*    */   public void send(MidiMessage paramMidiMessage, long paramLong) {
/* 63 */     this.receiver.send(paramMidiMessage, paramLong);
/*    */   }
/*    */ 
/*    */   public MidiDevice getMidiDevice()
/*    */   {
/* 68 */     return this.device;
/*    */   }
/*    */ 
/*    */   public Receiver getReceiver()
/*    */   {
/* 78 */     return this.receiver;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.MidiDeviceReceiverEnvelope
 * JD-Core Version:    0.6.2
 */