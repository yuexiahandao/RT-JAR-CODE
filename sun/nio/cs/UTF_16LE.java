/*    */ package sun.nio.cs;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.charset.CharsetDecoder;
/*    */ import java.nio.charset.CharsetEncoder;
/*    */ 
/*    */ class UTF_16LE extends Unicode
/*    */ {
/*    */   public UTF_16LE()
/*    */   {
/* 36 */     super("UTF-16LE", StandardCharsets.aliases_UTF_16LE);
/*    */   }
/*    */ 
/*    */   public String historicalName() {
/* 40 */     return "UnicodeLittleUnmarked";
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
/* 54 */       super(2);
/*    */     }
/*    */   }
/*    */ 
/*    */   private static class Encoder extends UnicodeEncoder
/*    */   {
/*    */     public Encoder(Charset paramCharset) {
/* 61 */       super(1, false);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.cs.UTF_16LE
 * JD-Core Version:    0.6.2
 */