/*    */ package sun.io;
/*    */ 
/*    */ import sun.nio.cs.KOI8_R;
/*    */ 
/*    */ public class CharToByteKOI8_R extends CharToByteSingleByte
/*    */ {
/* 38 */   private static final KOI8_R nioCoder = new KOI8_R();
/*    */ 
/*    */   public String getCharacterEncoding() {
/* 41 */     return "KOI8_R";
/*    */   }
/*    */ 
/*    */   public CharToByteKOI8_R() {
/* 45 */     this.mask1 = 65280;
/* 46 */     this.mask2 = 255;
/* 47 */     this.shift = 8;
/* 48 */     this.index1 = nioCoder.getEncoderIndex1();
/* 49 */     this.index2 = nioCoder.getEncoderIndex2();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.io.CharToByteKOI8_R
 * JD-Core Version:    0.6.2
 */