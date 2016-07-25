/*    */ package sun.io;
/*    */ 
/*    */ import sun.nio.cs.MS1253;
/*    */ 
/*    */ public class CharToByteCp1253 extends CharToByteSingleByte
/*    */ {
/* 38 */   private static final MS1253 nioCoder = new MS1253();
/*    */ 
/*    */   public String getCharacterEncoding() {
/* 41 */     return "Cp1253";
/*    */   }
/*    */ 
/*    */   public CharToByteCp1253() {
/* 45 */     this.mask1 = 65280;
/* 46 */     this.mask2 = 255;
/* 47 */     this.shift = 8;
/* 48 */     this.index1 = nioCoder.getEncoderIndex1();
/* 49 */     this.index2 = nioCoder.getEncoderIndex2();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.io.CharToByteCp1253
 * JD-Core Version:    0.6.2
 */