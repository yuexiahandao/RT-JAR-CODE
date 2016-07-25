/*    */ package sun.io;
/*    */ 
/*    */ import sun.nio.cs.ISO_8859_15;
/*    */ 
/*    */ public class CharToByteISO8859_15 extends CharToByteSingleByte
/*    */ {
/* 43 */   private static final ISO_8859_15 nioCoder = new ISO_8859_15();
/*    */ 
/*    */   public String getCharacterEncoding() {
/* 46 */     return "ISO8859_15";
/*    */   }
/*    */ 
/*    */   public CharToByteISO8859_15() {
/* 50 */     this.mask1 = 65280;
/* 51 */     this.mask2 = 255;
/* 52 */     this.shift = 8;
/* 53 */     this.index1 = nioCoder.getEncoderIndex1();
/* 54 */     this.index2 = nioCoder.getEncoderIndex2();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.io.CharToByteISO8859_15
 * JD-Core Version:    0.6.2
 */