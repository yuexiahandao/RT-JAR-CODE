/*    */ package sun.io;
/*    */ 
/*    */ import sun.nio.cs.MS1250;
/*    */ 
/*    */ public class CharToByteCp1250 extends CharToByteSingleByte
/*    */ {
/* 39 */   private static final MS1250 nioCoder = new MS1250();
/*    */ 
/*    */   public String getCharacterEncoding() {
/* 42 */     return "Cp1250";
/*    */   }
/*    */ 
/*    */   public CharToByteCp1250() {
/* 46 */     this.mask1 = 65280;
/* 47 */     this.mask2 = 255;
/* 48 */     this.shift = 8;
/* 49 */     this.index1 = nioCoder.getEncoderIndex1();
/* 50 */     this.index2 = nioCoder.getEncoderIndex2();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.io.CharToByteCp1250
 * JD-Core Version:    0.6.2
 */