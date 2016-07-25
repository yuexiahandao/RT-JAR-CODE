/*    */ package sun.io;
/*    */ 
/*    */ import sun.nio.cs.MS1253;
/*    */ 
/*    */ public class ByteToCharCp1253 extends ByteToCharSingleByte
/*    */ {
/* 39 */   private static final MS1253 nioCoder = new MS1253();
/*    */ 
/*    */   public String getCharacterEncoding() {
/* 42 */     return "Cp1253";
/*    */   }
/*    */ 
/*    */   public ByteToCharCp1253() {
/* 46 */     this.byteToCharTable = nioCoder.getDecoderSingleByteMappings();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.io.ByteToCharCp1253
 * JD-Core Version:    0.6.2
 */