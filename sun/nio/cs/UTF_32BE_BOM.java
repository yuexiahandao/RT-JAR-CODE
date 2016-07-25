/*    */ package sun.nio.cs;
/*    */ 
/*    */ import java.nio.charset.CharsetDecoder;
/*    */ import java.nio.charset.CharsetEncoder;
/*    */ 
/*    */ public class UTF_32BE_BOM extends Unicode
/*    */ {
/*    */   public UTF_32BE_BOM()
/*    */   {
/* 34 */     super("X-UTF-32BE-BOM", StandardCharsets.aliases_UTF_32BE_BOM);
/*    */   }
/*    */ 
/*    */   public String historicalName() {
/* 38 */     return "X-UTF-32BE-BOM";
/*    */   }
/*    */ 
/*    */   public CharsetDecoder newDecoder() {
/* 42 */     return new UTF_32Coder.Decoder(this, 1);
/*    */   }
/*    */ 
/*    */   public CharsetEncoder newEncoder() {
/* 46 */     return new UTF_32Coder.Encoder(this, 1, true);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.cs.UTF_32BE_BOM
 * JD-Core Version:    0.6.2
 */