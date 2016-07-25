/*    */ package javax.sound.midi;
/*    */ 
/*    */ public class MidiEvent
/*    */ {
/*    */   private final MidiMessage message;
/*    */   private long tick;
/*    */ 
/*    */   public MidiEvent(MidiMessage paramMidiMessage, long paramLong)
/*    */   {
/* 67 */     this.message = paramMidiMessage;
/* 68 */     this.tick = paramLong;
/*    */   }
/*    */ 
/*    */   public MidiMessage getMessage()
/*    */   {
/* 76 */     return this.message;
/*    */   }
/*    */ 
/*    */   public void setTick(long paramLong)
/*    */   {
/* 85 */     this.tick = paramLong;
/*    */   }
/*    */ 
/*    */   public long getTick()
/*    */   {
/* 94 */     return this.tick;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.midi.MidiEvent
 * JD-Core Version:    0.6.2
 */