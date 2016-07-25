/*    */ package java.awt.print;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class PrinterIOException extends PrinterException
/*    */ {
/*    */   static final long serialVersionUID = 5850870712125932846L;
/*    */   private IOException mException;
/*    */ 
/*    */   public PrinterIOException(IOException paramIOException)
/*    */   {
/* 58 */     initCause(null);
/* 59 */     this.mException = paramIOException;
/*    */   }
/*    */ 
/*    */   public IOException getIOException()
/*    */   {
/* 75 */     return this.mException;
/*    */   }
/*    */ 
/*    */   public Throwable getCause()
/*    */   {
/* 86 */     return this.mException;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.print.PrinterIOException
 * JD-Core Version:    0.6.2
 */