/*    */ package com.sun.media.sound;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import javax.sound.midi.MidiDevice;
/*    */ import javax.sound.midi.MidiDevice.Info;
/*    */ import javax.sound.midi.spi.MidiDeviceProvider;
/*    */ 
/*    */ public final class SoftProvider extends MidiDeviceProvider
/*    */ {
/* 39 */   static final MidiDevice.Info softinfo = SoftSynthesizer.info;
/* 40 */   private static final MidiDevice.Info[] softinfos = { softinfo };
/*    */ 
/*    */   public MidiDevice.Info[] getDeviceInfo() {
/* 43 */     return (MidiDevice.Info[])Arrays.copyOf(softinfos, softinfos.length);
/*    */   }
/*    */ 
/*    */   public MidiDevice getDevice(MidiDevice.Info paramInfo) {
/* 47 */     if (paramInfo == softinfo) {
/* 48 */       return new SoftSynthesizer();
/*    */     }
/* 50 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SoftProvider
 * JD-Core Version:    0.6.2
 */