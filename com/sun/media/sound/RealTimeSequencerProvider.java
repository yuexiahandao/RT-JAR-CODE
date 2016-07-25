/*    */ package com.sun.media.sound;
/*    */ 
/*    */ import javax.sound.midi.MidiDevice;
/*    */ import javax.sound.midi.MidiDevice.Info;
/*    */ import javax.sound.midi.MidiUnavailableException;
/*    */ import javax.sound.midi.spi.MidiDeviceProvider;
/*    */ 
/*    */ public final class RealTimeSequencerProvider extends MidiDeviceProvider
/*    */ {
/*    */   public MidiDevice.Info[] getDeviceInfo()
/*    */   {
/* 42 */     MidiDevice.Info[] arrayOfInfo = { RealTimeSequencer.info };
/* 43 */     return arrayOfInfo;
/*    */   }
/*    */ 
/*    */   public MidiDevice getDevice(MidiDevice.Info paramInfo)
/*    */   {
/* 48 */     if ((paramInfo != null) && (!paramInfo.equals(RealTimeSequencer.info))) {
/* 49 */       return null;
/*    */     }
/*    */     try
/*    */     {
/* 53 */       return new RealTimeSequencer(); } catch (MidiUnavailableException localMidiUnavailableException) {
/*    */     }
/* 55 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.RealTimeSequencerProvider
 * JD-Core Version:    0.6.2
 */