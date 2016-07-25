/*    */ package sun.io;
/*    */ 
/*    */ import sun.nio.cs.MS1251;
/*    */ 
/*    */ public class ByteToCharCp1251 extends ByteToCharSingleByte
/*    */ {
/* 39 */   private static final MS1251 nioCoder = new MS1251();
/*    */ 
/*    */   public String getCharacterEncoding() {
/* 42 */     return "Cp1251";
/*    */   }
/*    */ 
/*    */   public ByteToCharCp1251() {
/* 46 */     this.byteToCharTable = nioCoder.getDecoderSingleByteMappings();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.io.ByteToCharCp1251
 * JD-Core Version:    0.6.2
 */