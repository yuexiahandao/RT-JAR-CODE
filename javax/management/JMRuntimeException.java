/*    */ package javax.management;
/*    */ 
/*    */ public class JMRuntimeException extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 6573344628407841861L;
/*    */ 
/*    */   public JMRuntimeException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public JMRuntimeException(String paramString)
/*    */   {
/* 52 */     super(paramString);
/*    */   }
/*    */ 
/*    */   JMRuntimeException(String paramString, Throwable paramThrowable)
/*    */   {
/* 61 */     super(paramString, paramThrowable);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.JMRuntimeException
 * JD-Core Version:    0.6.2
 */