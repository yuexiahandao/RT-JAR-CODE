/*    */ package sun.audio;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ 
/*    */ public class AudioDataStream extends ByteArrayInputStream
/*    */ {
/*    */   private final AudioData ad;
/*    */ 
/*    */   public AudioDataStream(AudioData paramAudioData)
/*    */   {
/* 47 */     super(paramAudioData.buffer);
/* 48 */     this.ad = paramAudioData;
/*    */   }
/*    */ 
/*    */   final AudioData getAudioData() {
/* 52 */     return this.ad;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.audio.AudioDataStream
 * JD-Core Version:    0.6.2
 */