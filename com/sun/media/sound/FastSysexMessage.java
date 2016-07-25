/*    */ package com.sun.media.sound;
/*    */ 
/*    */ import javax.sound.midi.InvalidMidiDataException;
/*    */ import javax.sound.midi.SysexMessage;
/*    */ 
/*    */ final class FastSysexMessage extends SysexMessage
/*    */ {
/*    */   FastSysexMessage(byte[] paramArrayOfByte)
/*    */     throws InvalidMidiDataException
/*    */   {
/* 38 */     super(paramArrayOfByte);
/* 39 */     if ((paramArrayOfByte.length == 0) || (((paramArrayOfByte[0] & 0xFF) != 240) && ((paramArrayOfByte[0] & 0xFF) != 247)))
/* 40 */       super.setMessage(paramArrayOfByte, paramArrayOfByte.length);
/*    */   }
/*    */ 
/*    */   byte[] getReadOnlyMessage()
/*    */   {
/* 49 */     return this.data;
/*    */   }
/*    */ 
/*    */   public void setMessage(byte[] paramArrayOfByte, int paramInt)
/*    */     throws InvalidMidiDataException
/*    */   {
/* 55 */     if ((paramArrayOfByte.length == 0) || (((paramArrayOfByte[0] & 0xFF) != 240) && ((paramArrayOfByte[0] & 0xFF) != 247))) {
/* 56 */       super.setMessage(paramArrayOfByte, paramArrayOfByte.length);
/*    */     }
/* 58 */     this.length = paramInt;
/* 59 */     this.data = new byte[this.length];
/* 60 */     System.arraycopy(paramArrayOfByte, 0, this.data, 0, paramInt);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.FastSysexMessage
 * JD-Core Version:    0.6.2
 */