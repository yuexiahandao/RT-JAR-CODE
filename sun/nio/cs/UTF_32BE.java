/*    */ package sun.nio.cs;
/*    */ 
/*    */ import java.nio.charset.CharsetDecoder;
/*    */ import java.nio.charset.CharsetEncoder;
/*    */ 
/*    */ public class UTF_32BE extends Unicode
/*    */ {
/*    */   public UTF_32BE()
/*    */   {
/* 34 */     super("UTF-32BE", StandardCharsets.aliases_UTF_32BE);
/*    */   }
/*    */ 
/*    */   public String historicalName() {
/* 38 */     return "UTF-32BE";
/*    */   }
/*    */ 
/*    */   public CharsetDecoder newDecoder() {
/* 42 */     return new UTF_32Coder.Decoder(this, 1);
/*    */   }
/*    */ 
/*    */   public CharsetEncoder newEncoder() {
/* 46 */     return new UTF_32Coder.Encoder(this, 1, false);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.cs.UTF_32BE
 * JD-Core Version:    0.6.2
 */