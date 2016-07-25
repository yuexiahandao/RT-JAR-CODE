/*    */ package javax.management;
/*    */ 
/*    */ public class RuntimeOperationsException extends JMRuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -8408923047489133588L;
/*    */   private RuntimeException runtimeException;
/*    */ 
/*    */   public RuntimeOperationsException(RuntimeException paramRuntimeException)
/*    */   {
/* 54 */     this.runtimeException = paramRuntimeException;
/*    */   }
/*    */ 
/*    */   public RuntimeOperationsException(RuntimeException paramRuntimeException, String paramString)
/*    */   {
/* 65 */     super(paramString);
/* 66 */     this.runtimeException = paramRuntimeException;
/*    */   }
/*    */ 
/*    */   public RuntimeException getTargetException()
/*    */   {
/* 75 */     return this.runtimeException;
/*    */   }
/*    */ 
/*    */   public Throwable getCause()
/*    */   {
/* 84 */     return this.runtimeException;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.RuntimeOperationsException
 * JD-Core Version:    0.6.2
 */