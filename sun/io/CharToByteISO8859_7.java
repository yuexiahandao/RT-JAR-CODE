/*    */ package sun.io;
/*    */ 
/*    */ import sun.nio.cs.ISO_8859_7;
/*    */ 
/*    */ public class CharToByteISO8859_7 extends CharToByteSingleByte
/*    */ {
/* 43 */   private static final ISO_8859_7 nioCoder = new ISO_8859_7();
/*    */ 
/*    */   public String getCharacterEncoding()
/*    */   {
/* 40 */     return "ISO8859_7";
/*    */   }
/*    */ 
/*    */   public CharToByteISO8859_7()
/*    */   {
/* 46 */     this.mask1 = 65280;
/* 47 */     this.mask2 = 255;
/* 48 */     this.shift = 8;
/* 49 */     this.index1 = nioCoder.getEncoderIndex1();
/* 50 */     this.index2 = nioCoder.getEncoderIndex2();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.io.CharToByteISO8859_7
 * JD-Core Version:    0.6.2
 */