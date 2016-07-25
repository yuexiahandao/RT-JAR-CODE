/*    */ package java.io;
/*    */ 
/*    */ public class WriteAbortedException extends ObjectStreamException
/*    */ {
/*    */   private static final long serialVersionUID = -3326426625597282442L;
/*    */   public Exception detail;
/*    */ 
/*    */   public WriteAbortedException(String paramString, Exception paramException)
/*    */   {
/* 67 */     super(paramString);
/* 68 */     initCause(null);
/* 69 */     this.detail = paramException;
/*    */   }
/*    */ 
/*    */   public String getMessage()
/*    */   {
/* 77 */     if (this.detail == null) {
/* 78 */       return super.getMessage();
/*    */     }
/* 80 */     return super.getMessage() + "; " + this.detail.toString();
/*    */   }
/*    */ 
/*    */   public Throwable getCause()
/*    */   {
/* 91 */     return this.detail;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.WriteAbortedException
 * JD-Core Version:    0.6.2
 */