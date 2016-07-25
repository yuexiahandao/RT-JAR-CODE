/*    */ package javax.sound.midi.spi;
/*    */ 
/*    */ import javax.sound.midi.MidiDevice;
/*    */ import javax.sound.midi.MidiDevice.Info;
/*    */ 
/*    */ public abstract class MidiDeviceProvider
/*    */ {
/*    */   public boolean isDeviceSupported(MidiDevice.Info paramInfo)
/*    */   {
/* 52 */     MidiDevice.Info[] arrayOfInfo = getDeviceInfo();
/*    */ 
/* 54 */     for (int i = 0; i < arrayOfInfo.length; i++) {
/* 55 */       if (paramInfo.equals(arrayOfInfo[i])) {
/* 56 */         return true;
/*    */       }
/*    */     }
/* 59 */     return false;
/*    */   }
/*    */ 
/*    */   public abstract MidiDevice.Info[] getDeviceInfo();
/*    */ 
/*    */   public abstract MidiDevice getDevice(MidiDevice.Info paramInfo);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.midi.spi.MidiDeviceProvider
 * JD-Core Version:    0.6.2
 */