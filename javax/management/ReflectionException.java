/*    */ package javax.management;
/*    */ 
/*    */ public class ReflectionException extends JMException
/*    */ {
/*    */   private static final long serialVersionUID = 9170809325636915553L;
/*    */   private Exception exception;
/*    */ 
/*    */   public ReflectionException(Exception paramException)
/*    */   {
/* 55 */     this.exception = paramException;
/*    */   }
/*    */ 
/*    */   public ReflectionException(Exception paramException, String paramString)
/*    */   {
/* 66 */     super(paramString);
/* 67 */     this.exception = paramException;
/*    */   }
/*    */ 
/*    */   public Exception getTargetException()
/*    */   {
/* 76 */     return this.exception;
/*    */   }
/*    */ 
/*    */   public Throwable getCause()
/*    */   {
/* 85 */     return this.exception;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.ReflectionException
 * JD-Core Version:    0.6.2
 */