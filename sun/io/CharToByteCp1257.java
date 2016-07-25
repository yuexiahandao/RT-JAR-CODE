/*    */ package sun.io;
/*    */ 
/*    */ import sun.nio.cs.MS1257;
/*    */ 
/*    */ public class CharToByteCp1257 extends CharToByteSingleByte
/*    */ {
/* 38 */   private static final MS1257 nioCoder = new MS1257();
/*    */ 
/*    */   public String getCharacterEncoding() {
/* 41 */     return "Cp1257";
/*    */   }
/*    */ 
/*    */   public CharToByteCp1257() {
/* 45 */     this.mask1 = 65280;
/* 46 */     this.mask2 = 255;
/* 47 */     this.shift = 8;
/* 48 */     this.index1 = nioCoder.getEncoderIndex1();
/* 49 */     this.index2 = nioCoder.getEncoderIndex2();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.io.CharToByteCp1257
 * JD-Core Version:    0.6.2
 */