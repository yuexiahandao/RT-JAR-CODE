/*    */ package javax.xml.bind;
/*    */ 
/*    */ public class UnmarshalException extends JAXBException
/*    */ {
/*    */   public UnmarshalException(String message)
/*    */   {
/* 54 */     this(message, null, null);
/*    */   }
/*    */ 
/*    */   public UnmarshalException(String message, String errorCode)
/*    */   {
/* 65 */     this(message, errorCode, null);
/*    */   }
/*    */ 
/*    */   public UnmarshalException(Throwable exception)
/*    */   {
/* 75 */     this(null, null, exception);
/*    */   }
/*    */ 
/*    */   public UnmarshalException(String message, Throwable exception)
/*    */   {
/* 86 */     this(message, null, exception);
/*    */   }
/*    */ 
/*    */   public UnmarshalException(String message, String errorCode, Throwable exception)
/*    */   {
/* 98 */     super(message, errorCode, exception);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.UnmarshalException
 * JD-Core Version:    0.6.2
 */