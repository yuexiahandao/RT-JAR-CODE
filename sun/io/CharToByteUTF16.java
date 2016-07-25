/*    */ package sun.io;
/*    */ 
/*    */ public class CharToByteUTF16 extends CharToByteUnicode
/*    */ {
/*    */   public CharToByteUTF16()
/*    */   {
/* 37 */     super(1, true);
/*    */   }
/*    */ 
/*    */   public String getCharacterEncoding() {
/* 41 */     return "UTF-16";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.io.CharToByteUTF16
 * JD-Core Version:    0.6.2
 */