/*    */ package sun.audio;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ public final class AudioTranslatorStream extends NativeAudioStream
/*    */ {
/* 37 */   private final int length = 0;
/*    */ 
/*    */   public AudioTranslatorStream(InputStream paramInputStream) throws IOException {
/* 40 */     super(paramInputStream);
/*    */ 
/* 42 */     throw new InvalidAudioFormatException();
/*    */   }
/*    */ 
/*    */   public int getLength() {
/* 46 */     return 0;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.audio.AudioTranslatorStream
 * JD-Core Version:    0.6.2
 */