/*    */ package sun.io;
/*    */ 
/*    */ import sun.nio.cs.ISO_8859_15;
/*    */ 
/*    */ public class ByteToCharISO8859_15 extends ByteToCharSingleByte
/*    */ {
/* 43 */   private static final ISO_8859_15 nioCoder = new ISO_8859_15();
/*    */ 
/*    */   public String getCharacterEncoding() {
/* 46 */     return "ISO8859_15";
/*    */   }
/*    */ 
/*    */   public ByteToCharISO8859_15() {
/* 50 */     this.byteToCharTable = nioCoder.getDecoderSingleByteMappings();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.io.ByteToCharISO8859_15
 * JD-Core Version:    0.6.2
 */