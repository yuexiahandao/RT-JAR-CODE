/*    */ package com.sun.media.sound;
/*    */ 
/*    */ import javax.sound.midi.InvalidMidiDataException;
/*    */ import javax.sound.midi.ShortMessage;
/*    */ 
/*    */ public final class SoftShortMessage extends ShortMessage
/*    */ {
/* 37 */   int channel = 0;
/*    */ 
/*    */   public int getChannel() {
/* 40 */     return this.channel;
/*    */   }
/*    */ 
/*    */   public void setMessage(int paramInt1, int paramInt2, int paramInt3, int paramInt4) throws InvalidMidiDataException
/*    */   {
/* 45 */     this.channel = paramInt2;
/* 46 */     super.setMessage(paramInt1, paramInt2 & 0xF, paramInt3, paramInt4);
/*    */   }
/*    */ 
/*    */   public Object clone() {
/* 50 */     SoftShortMessage localSoftShortMessage = new SoftShortMessage();
/*    */     try {
/* 52 */       localSoftShortMessage.setMessage(getCommand(), getChannel(), getData1(), getData2());
/*    */     } catch (InvalidMidiDataException localInvalidMidiDataException) {
/* 54 */       throw new IllegalArgumentException(localInvalidMidiDataException);
/*    */     }
/* 56 */     return localSoftShortMessage;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SoftShortMessage
 * JD-Core Version:    0.6.2
 */