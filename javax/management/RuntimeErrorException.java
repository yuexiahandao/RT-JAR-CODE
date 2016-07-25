/*    */ package javax.management;
/*    */ 
/*    */ public class RuntimeErrorException extends JMRuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 704338937753949796L;
/*    */   private Error error;
/*    */ 
/*    */   public RuntimeErrorException(Error paramError)
/*    */   {
/* 53 */     this.error = paramError;
/*    */   }
/*    */ 
/*    */   public RuntimeErrorException(Error paramError, String paramString)
/*    */   {
/* 63 */     super(paramString);
/* 64 */     this.error = paramError;
/*    */   }
/*    */ 
/*    */   public Error getTargetError()
/*    */   {
/* 73 */     return this.error;
/*    */   }
/*    */ 
/*    */   public Throwable getCause()
/*    */   {
/* 82 */     return this.error;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.RuntimeErrorException
 * JD-Core Version:    0.6.2
 */