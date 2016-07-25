/*    */ package java.io;
/*    */ 
/*    */ public class OptionalDataException extends ObjectStreamException
/*    */ {
/*    */   private static final long serialVersionUID = -8011121865681257820L;
/*    */   public int length;
/*    */   public boolean eof;
/*    */ 
/*    */   OptionalDataException(int paramInt)
/*    */   {
/* 56 */     this.eof = false;
/* 57 */     this.length = paramInt;
/*    */   }
/*    */ 
/*    */   OptionalDataException(boolean paramBoolean)
/*    */   {
/* 65 */     this.length = 0;
/* 66 */     this.eof = paramBoolean;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.OptionalDataException
 * JD-Core Version:    0.6.2
 */