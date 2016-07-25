/*    */ package javax.xml.bind;
/*    */ 
/*    */ public class ValidationException extends JAXBException
/*    */ {
/*    */   public ValidationException(String message)
/*    */   {
/* 52 */     this(message, null, null);
/*    */   }
/*    */ 
/*    */   public ValidationException(String message, String errorCode)
/*    */   {
/* 63 */     this(message, errorCode, null);
/*    */   }
/*    */ 
/*    */   public ValidationException(Throwable exception)
/*    */   {
/* 73 */     this(null, null, exception);
/*    */   }
/*    */ 
/*    */   public ValidationException(String message, Throwable exception)
/*    */   {
/* 84 */     this(message, null, exception);
/*    */   }
/*    */ 
/*    */   public ValidationException(String message, String errorCode, Throwable exception)
/*    */   {
/* 96 */     super(message, errorCode, exception);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.ValidationException
 * JD-Core Version:    0.6.2
 */