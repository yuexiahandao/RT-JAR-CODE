/*    */ package javax.xml.ws;
/*    */ 
/*    */ public class ProtocolException extends WebServiceException
/*    */ {
/*    */   public ProtocolException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ProtocolException(String message)
/*    */   {
/* 54 */     super(message);
/*    */   }
/*    */ 
/*    */   public ProtocolException(String message, Throwable cause)
/*    */   {
/* 71 */     super(message, cause);
/*    */   }
/*    */ 
/*    */   public ProtocolException(Throwable cause)
/*    */   {
/* 86 */     super(cause);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.ws.ProtocolException
 * JD-Core Version:    0.6.2
 */