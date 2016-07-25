/*    */ package sun.nio.cs;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.charset.CharsetDecoder;
/*    */ import java.nio.charset.CharsetEncoder;
/*    */ 
/*    */ class UTF_16 extends Unicode
/*    */ {
/*    */   public UTF_16()
/*    */   {
/* 36 */     super("UTF-16", StandardCharsets.aliases_UTF_16);
/*    */   }
/*    */ 
/*    */   public String historicalName() {
/* 40 */     return "UTF-16";
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
/* 54 */       super(0);
/*    */     }
/*    */   }
/*    */ 
/*    */   private static class Encoder extends UnicodeEncoder
/*    */   {
/*    */     public Encoder(Charset paramCharset) {
/* 61 */       super(0, true);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.cs.UTF_16
 * JD-Core Version:    0.6.2
 */