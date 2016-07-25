/*    */ package javax.print;
/*    */ 
/*    */ public class PrintException extends Exception
/*    */ {
/*    */   public PrintException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public PrintException(String paramString)
/*    */   {
/* 51 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public PrintException(Exception paramException)
/*    */   {
/* 60 */     super(paramException);
/*    */   }
/*    */ 
/*    */   public PrintException(String paramString, Exception paramException)
/*    */   {
/* 70 */     super(paramString, paramException);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.PrintException
 * JD-Core Version:    0.6.2
 */