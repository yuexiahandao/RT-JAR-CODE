/*    */ package com.sun.media.sound;
/*    */ 
/*    */ import javax.sound.sampled.AudioFormat;
/*    */ import javax.sound.sampled.AudioFormat.Encoding;
/*    */ import javax.sound.sampled.AudioInputStream;
/*    */ import javax.sound.sampled.spi.FormatConversionProvider;
/*    */ 
/*    */ abstract class SunCodec extends FormatConversionProvider
/*    */ {
/*    */   private final AudioFormat.Encoding[] inputEncodings;
/*    */   private final AudioFormat.Encoding[] outputEncodings;
/*    */ 
/*    */   SunCodec(AudioFormat.Encoding[] paramArrayOfEncoding1, AudioFormat.Encoding[] paramArrayOfEncoding2)
/*    */   {
/* 57 */     this.inputEncodings = paramArrayOfEncoding1;
/* 58 */     this.outputEncodings = paramArrayOfEncoding2;
/*    */   }
/*    */ 
/*    */   public final AudioFormat.Encoding[] getSourceEncodings()
/*    */   {
/* 65 */     AudioFormat.Encoding[] arrayOfEncoding = new AudioFormat.Encoding[this.inputEncodings.length];
/* 66 */     System.arraycopy(this.inputEncodings, 0, arrayOfEncoding, 0, this.inputEncodings.length);
/* 67 */     return arrayOfEncoding;
/*    */   }
/*    */ 
/*    */   public final AudioFormat.Encoding[] getTargetEncodings()
/*    */   {
/* 72 */     AudioFormat.Encoding[] arrayOfEncoding = new AudioFormat.Encoding[this.outputEncodings.length];
/* 73 */     System.arraycopy(this.outputEncodings, 0, arrayOfEncoding, 0, this.outputEncodings.length);
/* 74 */     return arrayOfEncoding;
/*    */   }
/*    */ 
/*    */   public abstract AudioFormat.Encoding[] getTargetEncodings(AudioFormat paramAudioFormat);
/*    */ 
/*    */   public abstract AudioFormat[] getTargetFormats(AudioFormat.Encoding paramEncoding, AudioFormat paramAudioFormat);
/*    */ 
/*    */   public abstract AudioInputStream getAudioInputStream(AudioFormat.Encoding paramEncoding, AudioInputStream paramAudioInputStream);
/*    */ 
/*    */   public abstract AudioInputStream getAudioInputStream(AudioFormat paramAudioFormat, AudioInputStream paramAudioInputStream);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SunCodec
 * JD-Core Version:    0.6.2
 */