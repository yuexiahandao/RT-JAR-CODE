/*    */ package sun.nio.cs;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.charset.CharsetDecoder;
/*    */ import java.nio.charset.CharsetEncoder;
/*    */ 
/*    */ class UTF_16BE extends Unicode
/*    */ {
/*    */   public UTF_16BE()
/*    */   {
/* 36 */     super("UTF-16BE", StandardCharsets.aliases_UTF_16BE);
/*    */   }
/*    */ 
/*    */   public String historicalName() {
/* 40 */     return "UnicodeBigUnmarked";
/*    */   }
/*    */ 
/*    */   public CharsetDecoder newDecoder() {
/* 44 */     return new Decoder(this);
/*    */   }
/*    */ 
/*    */   public CharsetEncoder newEncoder() {
/* 48 */     return new Encoder(this);
/*    */   }
/*    */ 
/*    */   private static class Decoder extends UnicodeDecoder
/*    */   {
/*    */     public Decoder(Charset paramCharset) {
/* 54 */       super(1);
/*    */     }
/*    */   }
/*    */ 
/*    */   private static class Encoder extends UnicodeEncoder
/*    */   {
/*    */     public Encoder(Charset paramCharset) {
/* 61 */       super(0, false);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.cs.UTF_16BE
 * JD-Core Version:    0.6.2
 */