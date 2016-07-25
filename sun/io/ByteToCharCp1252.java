/*    */ package sun.io;
/*    */ 
/*    */ import sun.nio.cs.MS1252;
/*    */ 
/*    */ public class ByteToCharCp1252 extends ByteToCharSingleByte
/*    */ {
/* 38 */   private static final MS1252 nioCoder = new MS1252();
/*    */ 
/*    */   public String getCharacterEncoding() {
/* 41 */     return "Cp1252";
/*    */   }
/*    */ 
/*    */   public ByteToCharCp1252() {
/* 45 */     this.byteToCharTable = nioCoder.getDecoderSingleByteMappings();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.io.ByteToCharCp1252
 * JD-Core Version:    0.6.2
 */