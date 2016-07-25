/*    */ package sun.io;
/*    */ 
/*    */ public class ByteToCharUTF16 extends ByteToCharUnicode
/*    */ {
/*    */   public ByteToCharUTF16()
/*    */   {
/* 37 */     super(0, true);
/*    */   }
/*    */ 
/*    */   public String getCharacterEncoding() {
/* 41 */     return "UTF-16";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.io.ByteToCharUTF16
 * JD-Core Version:    0.6.2
 */