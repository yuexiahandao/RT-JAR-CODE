/*    */ package sun.audio;
/*    */ 
/*    */ import java.io.FilterInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ public class NativeAudioStream extends FilterInputStream
/*    */ {
/*    */   public NativeAudioStream(InputStream paramInputStream)
/*    */     throws IOException
/*    */   {
/* 53 */     super(paramInputStream);
/*    */   }
/*    */ 
/*    */   public int getLength() {
/* 57 */     return 0;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.audio.NativeAudioStream
 * JD-Core Version:    0.6.2
 */