/*    */ package sun.io;
/*    */ 
/*    */ import sun.nio.cs.ISO_8859_2;
/*    */ 
/*    */ public class ByteToCharISO8859_2 extends ByteToCharSingleByte
/*    */ {
/* 38 */   private static final ISO_8859_2 nioCoder = new ISO_8859_2();
/*    */ 
/*    */   public String getCharacterEncoding() {
/* 41 */     return "ISO8859_2";
/*    */   }
/*    */ 
/*    */   public ByteToCharISO8859_2() {
/* 45 */     this.byteToCharTable = nioCoder.getDecoderSingleByteMappings();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.io.ByteToCharISO8859_2
 * JD-Core Version:    0.6.2
 */