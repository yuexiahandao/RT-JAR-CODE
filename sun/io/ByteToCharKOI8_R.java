/*    */ package sun.io;
/*    */ 
/*    */ import sun.nio.cs.KOI8_R;
/*    */ 
/*    */ public class ByteToCharKOI8_R extends ByteToCharSingleByte
/*    */ {
/* 38 */   private static final KOI8_R nioCoder = new KOI8_R();
/*    */ 
/*    */   public String getCharacterEncoding() {
/* 41 */     return "KOI8_R";
/*    */   }
/*    */ 
/*    */   public ByteToCharKOI8_R() {
/* 45 */     this.byteToCharTable = nioCoder.getDecoderSingleByteMappings();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.io.ByteToCharKOI8_R
 * JD-Core Version:    0.6.2
 */