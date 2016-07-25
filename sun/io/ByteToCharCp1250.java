/*    */ package sun.io;
/*    */ 
/*    */ import sun.nio.cs.MS1250;
/*    */ 
/*    */ public class ByteToCharCp1250 extends ByteToCharSingleByte
/*    */ {
/* 39 */   private static final MS1250 nioCoder = new MS1250();
/*    */ 
/*    */   public String getCharacterEncoding() {
/* 42 */     return "Cp1250";
/*    */   }
/*    */ 
/*    */   public ByteToCharCp1250() {
/* 46 */     this.byteToCharTable = nioCoder.getDecoderSingleByteMappings();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.io.ByteToCharCp1250
 * JD-Core Version:    0.6.2
 */