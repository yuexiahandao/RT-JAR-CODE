/*    */ package javax.management;
/*    */ 
/*    */ public class MBeanException extends JMException
/*    */ {
/*    */   private static final long serialVersionUID = 4066342430588744142L;
/*    */   private Exception exception;
/*    */ 
/*    */   public MBeanException(Exception paramException)
/*    */   {
/* 56 */     this.exception = paramException;
/*    */   }
/*    */ 
/*    */   public MBeanException(Exception paramException, String paramString)
/*    */   {
/* 67 */     super(paramString);
/* 68 */     this.exception = paramException;
/*    */   }
/*    */ 
/*    */   public Exception getTargetException()
/*    */   {
/* 78 */     return this.exception;
/*    */   }
/*    */ 
/*    */   public Throwable getCause()
/*    */   {
/* 87 */     return this.exception;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.MBeanException
 * JD-Core Version:    0.6.2
 */