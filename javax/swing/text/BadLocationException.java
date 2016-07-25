/*    */ package javax.swing.text;
/*    */ 
/*    */ public class BadLocationException extends Exception
/*    */ {
/*    */   private int offs;
/*    */ 
/*    */   public BadLocationException(String paramString, int paramInt)
/*    */   {
/* 51 */     super(paramString);
/* 52 */     this.offs = paramInt;
/*    */   }
/*    */ 
/*    */   public int offsetRequested()
/*    */   {
/* 61 */     return this.offs;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.BadLocationException
 * JD-Core Version:    0.6.2
 */