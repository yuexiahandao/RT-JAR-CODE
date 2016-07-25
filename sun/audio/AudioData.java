/*    */ package sun.audio;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.IOException;
/*    */ import javax.sound.sampled.AudioFormat;
/*    */ import javax.sound.sampled.AudioFormat.Encoding;
/*    */ import javax.sound.sampled.AudioInputStream;
/*    */ import javax.sound.sampled.AudioSystem;
/*    */ import javax.sound.sampled.UnsupportedAudioFileException;
/*    */ 
/*    */ public final class AudioData
/*    */ {
/* 53 */   private static final AudioFormat DEFAULT_FORMAT = new AudioFormat(AudioFormat.Encoding.ULAW, 8000.0F, 8, 1, 1, 8000.0F, true);
/*    */   AudioFormat format;
/*    */   byte[] buffer;
/*    */ 
/*    */   public AudioData(byte[] paramArrayOfByte)
/*    */   {
/* 70 */     this.buffer = paramArrayOfByte;
/*    */ 
/* 73 */     this.format = DEFAULT_FORMAT;
/*    */     try
/*    */     {
/* 77 */       AudioInputStream localAudioInputStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(paramArrayOfByte));
/* 78 */       this.format = localAudioInputStream.getFormat();
/* 79 */       localAudioInputStream.close();
/*    */     }
/*    */     catch (IOException localIOException)
/*    */     {
/*    */     }
/*    */     catch (UnsupportedAudioFileException localUnsupportedAudioFileException)
/*    */     {
/*    */     }
/*    */   }
/*    */ 
/*    */   AudioData(AudioFormat paramAudioFormat, byte[] paramArrayOfByte)
/*    */   {
/* 95 */     this.format = paramAudioFormat;
/* 96 */     this.buffer = paramArrayOfByte;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.audio.AudioData
 * JD-Core Version:    0.6.2
 */