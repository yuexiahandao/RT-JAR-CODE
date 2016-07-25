/*    */ package javax.xml.bind;
/*    */ 
/*    */ public class MarshalException extends JAXBException
/*    */ {
/*    */   public MarshalException(String message)
/*    */   {
/* 52 */     this(message, null, null);
/*    */   }
/*    */ 
/*    */   public MarshalException(String message, String errorCode)
/*    */   {
/* 63 */     this(message, errorCode, null);
/*    */   }
/*    */ 
/*    */   public MarshalException(Throwable exception)
/*    */   {
/* 73 */     this(null, null, exception);
/*    */   }
/*    */ 
/*    */   public MarshalException(String message, Throwable exception)
/*    */   {
/* 84 */     this(message, null, exception);
/*    */   }
/*    */ 
/*    */   public MarshalException(String message, String errorCode, Throwable exception)
/*    */   {
/* 96 */     super(message, errorCode, exception);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.MarshalException
 * JD-Core Version:    0.6.2
 */