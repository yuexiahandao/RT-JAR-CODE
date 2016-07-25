/*    */ package javax.xml.bind;
/*    */ 
/*    */ public class DataBindingException extends RuntimeException
/*    */ {
/*    */   public DataBindingException(String message, Throwable cause)
/*    */   {
/* 41 */     super(message, cause);
/*    */   }
/*    */ 
/*    */   public DataBindingException(Throwable cause) {
/* 45 */     super(cause);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.DataBindingException
 * JD-Core Version:    0.6.2
 */