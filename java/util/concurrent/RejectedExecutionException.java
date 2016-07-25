/*    */ package java.util.concurrent;
/*    */ 
/*    */ public class RejectedExecutionException extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -375805702767069545L;
/*    */ 
/*    */   public RejectedExecutionException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public RejectedExecutionException(String paramString)
/*    */   {
/* 64 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public RejectedExecutionException(String paramString, Throwable paramThrowable)
/*    */   {
/* 76 */     super(paramString, paramThrowable);
/*    */   }
/*    */ 
/*    */   public RejectedExecutionException(Throwable paramThrowable)
/*    */   {
/* 89 */     super(paramThrowable);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.RejectedExecutionException
 * JD-Core Version:    0.6.2
 */