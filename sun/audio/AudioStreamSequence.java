/*    */ package sun.audio;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.io.SequenceInputStream;
/*    */ import java.util.Enumeration;
/*    */ 
/*    */ public final class AudioStreamSequence extends SequenceInputStream
/*    */ {
/*    */   Enumeration e;
/*    */   InputStream in;
/*    */ 
/*    */   public AudioStreamSequence(Enumeration paramEnumeration)
/*    */   {
/* 56 */     super(paramEnumeration);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.audio.AudioStreamSequence
 * JD-Core Version:    0.6.2
 */